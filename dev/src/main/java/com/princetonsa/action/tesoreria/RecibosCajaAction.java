
/*
 * Creado   28/09/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.action.tesoreria;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.BloqueosConcurrencia;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.FacturasVarias.IConstantesNegocioFacturasVarias;
import util.interfaces.UtilidadBDInterfaz;

import com.princetonsa.actionform.tesoreria.RecibosCajaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.carteraPaciente.DtoCuotasDatosFinanciacion;
import com.princetonsa.dto.carteraPaciente.DtoDeudor;
import com.princetonsa.dto.carteraPaciente.DtoDeudoresDatosFinan;
import com.princetonsa.dto.facturasVarias.DtoRecibosCaja;
import com.princetonsa.dto.tesoreria.DtoBonoSerialValor;
import com.princetonsa.dto.tesoreria.DtoDetallePagosBonos;
import com.princetonsa.dto.tesoreria.DtoEntidadesFinancieras;
import com.princetonsa.dto.tesoreria.DtoFormaPago;
import com.princetonsa.dto.tesoreria.DtoInfoIngresoTrasladoAbonoPaciente;
import com.princetonsa.dto.tesoreria.DtoTipoDetalleFormaPago;
import com.princetonsa.dto.tesoreria.DtoTurnoDeCajaApta;
import com.princetonsa.enu.administracion.EmunConsecutivosTesoreriaCentroAtencion;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.administracion.ConsecutivosCentroAtencion;
import com.princetonsa.mundo.carteraPaciente.DatosFinanciacion;
import com.princetonsa.mundo.consultaExterna.Multas;
import com.princetonsa.mundo.facturasVarias.Deudores;
import com.princetonsa.mundo.facturasVarias.GenModFacturasVarias;
import com.princetonsa.mundo.tesoreria.Arqueos;
import com.princetonsa.mundo.tesoreria.EntidadesFinancieras;
import com.princetonsa.mundo.tesoreria.RecibosCaja;
import com.princetonsa.sort.carteraPaciente.SortDeudores;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.servinte.axioma.orm.delegate.tesoreria.CajasDelegate;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.ManejoPacienteServicioFabrica;
import com.servinte.axioma.servicio.fabrica.TesoreriaFabricaServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IIngresosServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IFormasPagoServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IMovimientosCajaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IRecibosCajaServicio;

/**
 * Clase para manejar el workflow de 
 * recibos de caja
 *
 * @version 1.0, 28/09/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
@SuppressWarnings("unchecked")
public class RecibosCajaAction extends Action 
{

	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(RecibosCajaAction.class);
	/**
	 * Mï¿½todo execute del action
	 */
	
	private String[] indices={"nroFactura_", 
			"fecha_", 
			"saldoFactura_", 
			"deudor_", 
			"identificacionDeudor_", 
			"conceptoFactura_",
			"multasCitas_",
			"consecutivo_"};
	
	public ActionForward execute(	ActionMapping mapping, 	
															ActionForm form, 
															HttpServletRequest request, 
															HttpServletResponse response) throws Exception
															{

		Connection con = null;	
		try{

			if(form instanceof RecibosCajaForm)
			{

				//intentamos abrir una conexion con la fuente de datos 
				con = UtilidadBD.abrirConexion();

				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				RecibosCajaForm formRecibos=(RecibosCajaForm) form;

				logger.info("CONCEPTOS-------->>>>>>>>>>>>><"+formRecibos.getEstado());
				
				RecibosCaja mundoRecibos=new RecibosCaja();
				HttpSession sesion = request.getSession();			
				UsuarioBasico usuario = null;
				usuario = getUsuarioBasicoSesion(sesion);
				String valorParametro = ValoresPorDefecto.getPermitirRecaudosCajaMayor(usuario.getCodigoInstitucionInt());
				Integer cantidadCajasUsuario=Utilidades.obtenerCantidadCajasCajero(con, usuario.getCodigoCentroAtencion(), 
						usuario.getLoginUsuario(), valorParametro);
				request.setAttribute("cantidadCajasPorUsuario",cantidadCajasUsuario);
				InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica"); 

				String estado=formRecibos.getEstado();
				logger.warn("estado->"+estado);

				formRecibos.setCerrarPopupTarjetas(false);
				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de RecibosCajaAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
				if(estado.equals("empezarSeleccion") || estado.equals("generarEstructuraDesdeFacturaSeleccion")||estado.equals("empezarSeleccionFV") || estado.equals("generarReciboAgendaOdon"))
				{
					int numeroCajas=Utilidades.numCajasAsociadasUsuarioXcentroAtencion(usuario.getLoginUsuario(), usuario.getCodigoCentroAtencion()+"", new int[]{ConstantesBD.codigoTipoCajaMayor, ConstantesBD.codigoTipoCajaRecaudado});

					//boolean flujoAgendaOdontologica = false;

					if(request.getParameter("flujoAgendaOdontologica")!=null && "true".equals(request.getParameter("flujoAgendaOdontologica"))){

						//flujoAgendaOdontologica = true;
						this.inicializarFuncionalidad(formRecibos, mundoRecibos,true,usuario.getCodigoInstitucionInt());

						formRecibos.setActividadAgenda(request.getParameter("actividadAgenda"));
						formRecibos.setFlujoAgendaOdontologica(true);
					}

					//hermorhu - MT3536
					if(estado.equals("empezarSeleccionFV")) {
						formRecibos.setEsWorflowFactura("false");
					}
					
					boolean consecutivoEncontrado = validarConsecutivoDisponible(con, request, usuario);

					if (!consecutivoEncontrado) {
						UtilidadBD.closeConnection(con);
						return mapping.findForward("paginaErroresActionErrors");
					}

					if(numeroCajas==1 || formRecibos.isFlujoAgendaOdontologica())
					{
						boolean encontro=false;
						HashMap mapa=Utilidades.obtenerCajasCajero(usuario.getLoginUsuario(), usuario.getCodigoInstitucionInt(), ConstantesBD.codigoTipoCajaRecaudado, usuario.getCodigoCentroAtencion());

						//Utilidades.imprimirMapa(mapa);

						numeroCajas=Utilidades.convertirAEntero(mapa.get("numRegistros")+"");

						int codigoCaja=0;
						int consecutivoCaja=0;

						String descripcionCaja="";
						//Utilidades.imprimirMapa(mapa);
						boolean errorValidacionApertura=false;

						/*
						 * Cuando se va a generar el registro desde la Agenda odontolï¿½gica
						 */
						if(request.getParameter("consecutivoCaja")!=null){

							consecutivoCaja = Integer.parseInt(request.getParameter("consecutivoCaja"));

							for(int i=0; i< numeroCajas; i++)
							{
								if(consecutivoCaja == Utilidades.convertirAEntero(mapa.get("consecutivo_"+i)+"")){

									errorValidacionApertura=false;
									encontro=true;
									//consecutivoCaja=Utilidades.convertirAEntero(mapa.get("consecutivo_"+i)+"");
									descripcionCaja=mapa.get("descripcion_"+i)+"";
									usuario.setConsecutivoCaja(consecutivoCaja);
									usuario.setCodigoCaja(codigoCaja);
									usuario.setDescripcionCaja(descripcionCaja);
									break;
								}
							}
						}else{

							for(int i=0; i< numeroCajas; i++)
							{
								int centroAtencion=Utilidades.convertirAEntero(mapa.get("centro_atencion_"+i)+"");
								logger.info("centroAtencion "+centroAtencion);
								logger.info("centroAtencion "+usuario.getCodigoCentroAtencion());

								if(consecutivoCaja == Utilidades.convertirAEntero(mapa.get("consecutivo_"+i)+"")){

									errorValidacionApertura=false;
									encontro=true;
									//consecutivoCaja=Utilidades.convertirAEntero(mapa.get("consecutivo_"+i)+"");
									descripcionCaja=mapa.get("descripcion_"+i)+"";
									usuario.setConsecutivoCaja(consecutivoCaja);
									usuario.setCodigoCaja(codigoCaja);
									usuario.setDescripcionCaja(descripcionCaja);
									break;

								}else if(centroAtencion==usuario.getCodigoCentroAtencion())
								{
									codigoCaja=Utilidades.convertirAEntero(mapa.get("consecutivo_"+i)+"");
									int tipoCaja=Utilidades.convertirAEntero(mapa.get("tipo_caja_"+i)+"");

									IMovimientosCajaServicio movimientosCajaServicio 	= TesoreriaFabricaServicio.crearMovimientosCajaServicio();

									UtilidadTransaccion.getTransaccion().begin();
									DtoTurnoDeCajaApta dtoApto = movimientosCajaServicio.esCajaAptaParaApertura(usuario, new CajasDelegate().findById(codigoCaja));
									UtilidadTransaccion.getTransaccion().commit();

									if(tipoCaja==ConstantesBD.codigoTipoCajaMayor || (!dtoApto.isTieneTurnoCajaCerrado() && !dtoApto.isTieneTurnoCajeroCerrado()))
									{
										errorValidacionApertura=false;
										encontro=true;
										consecutivoCaja=Utilidades.convertirAEntero(mapa.get("consecutivo_"+i)+"");
										descripcionCaja=mapa.get("descripcion_"+i)+"";
										usuario.setConsecutivoCaja(consecutivoCaja);
										usuario.setCodigoCaja(codigoCaja);
										usuario.setDescripcionCaja(descripcionCaja);
										break;
									}
									else
									{
										errorValidacionApertura=true;
									}
								}
							}
							if(errorValidacionApertura) 
							{
								ActionErrors errores=new ActionErrors();
								errores.add("no tiene turno abierto", new ActionMessage("error.recibosCaja.turnoCaja"));
								saveErrors(request, errores);
								UtilidadBD.closeConnection(con);
								return mapping.findForward("paginaErroresActionErrors");
							}

							if(!encontro)
							{
								mapa=Utilidades.obtenerCajasCajero(usuario.getLoginUsuario(), usuario.getCodigoInstitucionInt(), ConstantesBD.codigoTipoCajaMayor, usuario.getCodigoCentroAtencion());
								numeroCajas=Utilidades.convertirAEntero(mapa.get("numRegistros")+"");
								codigoCaja=0;
								consecutivoCaja=0;
								//Utilidades.imprimirMapa(mapa);
								for(int i=0; i< numeroCajas; i++)
								{
									int centroAtencion=Utilidades.convertirAEntero(mapa.get("centro_atencion_"+i)+"");
									if(centroAtencion==usuario.getCodigoCentroAtencion())
									{
										encontro=true;
										codigoCaja=Utilidades.convertirAEntero(mapa.get("codigo_"+i)+"");
										consecutivoCaja=Utilidades.convertirAEntero(mapa.get("consecutivo_"+i)+"");
										descripcionCaja=mapa.get("descripcion_"+i)+"";
										usuario.setConsecutivoCaja(consecutivoCaja);
										usuario.setCodigoCaja(codigoCaja);
										usuario.setDescripcionCaja(descripcionCaja);
									}
								}
								if(!encontro)
								{
									logger.error("Aqui nunca debe llegar!!!!!!!!! REVISAR URGENTE ");
									request.setAttribute("codigoDescripcionError", "errors.faltaDefinirCajaParaUsuario");
									UtilidadBD.closeConnection(con);
									return mapping.findForward("paginaError");
								}
							}
						}
					}
					//logger.info("Cajas "+numeroCajas);
					else if(numeroCajas>1)
					{					  
						if(estado.equals("generarEstructuraDesdeFacturaSeleccion"))
						{
							request.setAttribute("ruta","recibosCaja/recibosCaja.do?estado=generarEstructuraDesdeFactura");
						}
						//Agregado por anexo 958
						else if (estado.equals("empezarSeleccionFV"))
						{
							request.setAttribute("ruta","recibosCaja/recibosCaja.do?estado=empezarSeleccionFVAccion");
						}
						//Fin anexo 958

						else
						{
							request.setAttribute("ruta","recibosCaja/recibosCaja.do?estado=empezar");
						}

						if(valorParametro==null){
							request.setAttribute("nTiposCaja",ConstantesBD.codigoNuncaValido);
						}else if (valorParametro.equals(ConstantesBD.acronimoSi)){
							request.setAttribute("nTiposCaja",ConstantesBD.codigoTipoCajaRecaudado+","+ConstantesBD.codigoTipoCajaMayor);
						}else if (valorParametro.equals(ConstantesBD.acronimoNo)) {
							request.setAttribute("nTiposCaja",ConstantesBD.codigoTipoCajaRecaudado);
						}
						UtilidadBD.closeConnection(con);
						return mapping.findForward("cargarCajaUsuario");
					}
					else
					{
						if(usuario.getCodigoCaja()==ConstantesBD.codigoNuncaValido && numeroCajas==0)
						{
							request.setAttribute("codigoDescripcionError", "errors.faltaDefinirCajaParaUsuario");
							UtilidadBD.closeConnection(con);
							return mapping.findForward("paginaError");
						}
					}
					if(estado.equals("generarEstructuraDesdeFacturaSeleccion"))
					{
						estado="generarEstructuraDesdeFactura";

					}else if (estado.equals("empezarSeleccionFV"))
					{
						estado=("empezarSeleccionFVAccion");

					}else if("generarReciboAgendaOdon".equals(estado)){

						generarReciboAgendaOdon(mapping, request, con, formRecibos,mundoRecibos, usuario);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("paginaPrincipal");

					}else
					{
						estado="empezar";
					}


				}else if(estado.equals("volverAgendaOdontologica")){

					if(formRecibos.getActividadAgenda().equals(ConstantesIntegridadDominio.acronimoTipoFuncionalidadAsignar)){

						UtilidadBD.closeConnection(con);
						return mapping.findForward("paginaAsignacion");

					}else{

						UtilidadBD.closeConnection(con);
						return mapping.findForward("paginaReservacion");
					}

				}



				int tipoCaja=Utilidades.obtenerTipoCaja(con,usuario.getConsecutivoCaja());
				if(tipoCaja!=ConstantesBD.codigoTipoCajaRecaudado&&tipoCaja!=ConstantesBD.codigoTipoCajaMayor)
				{
					ActionErrors errores = new ActionErrors();  
					errores.add("La caja del usuario, no es de Recaudo", new ActionMessage("error.recibosCaja.cajaNoRecuadoNiMayor"));
					saveErrors(request, errores);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaErroresActionErrors");
				}
				formRecibos.setMensaje(new ResultadoBoolean(false));
				if(estado.equals("empezar"))
				{
					formRecibos.setFlujoAgendaOdontologica(false);

					IMovimientosCajaServicio movimientosCajaServicio 	= TesoreriaFabricaServicio.crearMovimientosCajaServicio();

					UtilidadTransaccion.getTransaccion().begin();
					DtoTurnoDeCajaApta dtoApto = movimientosCajaServicio.esCajaAptaParaApertura(usuario, new CajasDelegate().findById(usuario.getConsecutivoCaja()));
					UtilidadTransaccion.getTransaccion().commit();

					if(
							(tipoCaja==ConstantesBD.codigoTipoCajaRecaudado || tipoCaja==ConstantesBD.codigoTipoCajaPpal) 
							&& (!dtoApto.isTurnoAbierto() || !dtoApto.isUsuarioTurnoCaja())
					)
					{
						ActionErrors errores=new ActionErrors();
						errores.add("no tiene turno abierto", new ActionMessage("error.recibosCaja.turnoCaja"));
						saveErrors(request, errores);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("paginaErroresActionErrors");
					}

					UtilidadTransaccion.getTransaccion().begin();
					formRecibos.setEntidadesFinancieras(EntidadesFinancieras.consultarEntidadesFinancieras(true));
					UtilidadTransaccion.getTransaccion().commit();


					formRecibos.setEsWorflowFactura("false");
					ActionForward validacionesArqueosCierresForward= this.validacionesArqueosCierres(con, request, mapping, usuario.getCodigoInstitucionInt(), UtilidadFecha.getFechaActual(), usuario.getLoginUsuario(), usuario.getConsecutivoCaja()+"" );
					if(validacionesArqueosCierresForward!=null)
						return validacionesArqueosCierresForward;
					else
					{
						boolean consecutivoEncontrado = validarConsecutivoDisponible(con, request, usuario);

						if (!consecutivoEncontrado) {
							UtilidadBD.closeConnection(con);
							return mapping.findForward("paginaErroresActionErrors");
						}
						else
						{
							this.inicializarFuncionalidad(formRecibos, mundoRecibos,true,usuario.getCodigoInstitucionInt());
							UtilidadBD.closeConnection(con);
							return mapping.findForward("paginaPrincipal");   
						}
					}				  			
				}

				if(estado.equals("generarRecibo"))
				{				
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaPrincipal");	  		
				}
				if(estado.equals("nuevoRegistroConcepto"))
				{							
					return this.accionConcepto(con,formRecibos,mapping,mundoRecibos,usuario);					
				}
				if(estado.equals("nuevoRegistroFormaPago"))
				{							
					return this.accionFormaPago(con,formRecibos,mapping,mundoRecibos,request);					
				}
				else if(estado.equals("ordenarColumnaFactura"))
				{
					return this.accionOrdenarColumnaFactura(con,formRecibos,mapping);
				}
				else if(estado.equals("ordenarColumnaConvenio"))
				{
					return this.accionOrdenarColumnaConvenios(con,formRecibos,mapping);
				}
				else if(estado.equals("ordenarColumnaPaciente"))
				{
					return this.accionOrdenarColumnaPacientes(con,formRecibos,mapping);
				}			
				else if(estado.equals("buscarPacientes"))
				{
					return buscarPacientes(mapping, con, formRecibos, mundoRecibos,	usuario);   
				}
				if(estado.equals("buscarFacturas"))
				{							
					return this.accionBuscarFacturas(con, formRecibos, mundoRecibos, mapping, usuario);				
				}
				if(estado.equals("buscarConvenios"))
				{
					return this.accionBuscarConvenios(con, formRecibos, mundoRecibos, mapping, usuario);
				}
				else if(estado.equals("eliminarConcepto"))
				{
					return this.accionEliminarRegistroConcepto(con,formRecibos,mapping);  
				}
				else if(estado.equals("eliminarFormaPago"))
				{
					return this.accionEliminarRegistroFormaPago(con,formRecibos,mapping);  
				}
				else if(estado.equals("facturaSeleccionada"))
				{
					return this.accionFacturaSeleccionada(con,formRecibos,mapping);  
				}
				else if(estado.equals("convenioSeleccionado"))
				{
					return this.accionConvenioSeleccionado(con,formRecibos,mapping);  
				}
				else if(estado.equals("pacienteSeleccionado"))
				{
					return this.accionPacienteSeleccionado(con,formRecibos,mapping);  
				}
				else if(estado.equals("generarReciboCaja"))
				{
					return this.accionGenerarReciboCaja(con,formRecibos,mundoRecibos,mapping,usuario,request, response);
				}else if(estado.equals("buscarFacturasVarias"))
				{
					// busqueda facturas varias
					return this.accionBuscarFacturasVarias(con, formRecibos, mundoRecibos, mapping, usuario);
				}
				else if(estado.equals("ingresarCheque"))
				{					
					formRecibos.setMapaCheques("informacionCompletaDelCheque_"+formRecibos.getPosSelFormaPago(),"true");
					formRecibos.setMapaFormasPago("entidadFormaPago_"+formRecibos.getPosSelFormaPago(), 
							formRecibos.getMapaCheques().get("girador_"+formRecibos.getPosSelFormaPago()));
					formRecibos.setAbrirPopUp("");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaPrincipal");   
				}
				else if(estado.equals("ingresarBono"))
				{					
					formRecibos.setAbrirPopUp("");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaPrincipal");   
				}
				else if(estado.equals("ingresarTarjeta"))
				{				
					formRecibos.setMapaTarjetas("informacionCompletaTarjeta_"+formRecibos.getPosSelFormaPago(),"true");
					formRecibos.setAbrirPopUp("");
					//hermorhu - MT6301
					formRecibos.setCerrarPopupTarjetas(true);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaPrincipal");   
				}
				else if(estado.equals("seleccionarRegistroEditar"))
				{				
					return this.accionSeleccionRegistroEditar(con,formRecibos,mapping);				   
				}
				else if(estado.equals("imprimirReciboCaja"))
				{	 
					//Cambio por anexo 959
					//Formato de impresion del recibo de caja formatos POS y carta

					String newPathReport = imprimirReciboCaja(usuario, institucionBasica, formRecibos.getNumReciboCaja(), 
							formRecibos.getIdentificacionPaciente(), formRecibos.getTipoIdentificacion(), formRecibos.getObservacionesImprimir(),formRecibos.getConsecutivoReciboCaja());


					if (!newPathReport.equals("")) 
					{
						request.setAttribute("isOpenReport", "true");
						request.setAttribute("newPathReport", newPathReport);
					}

					UtilidadBD.closeConnection(con);
					formRecibos.setEstado("resumen");

					return mapping.findForward("paginaPrincipal");
				}
				else if(estado.equals("consultaDeptos"))
				{
					formRecibos.setEstado("empezar");
					formRecibos.setMapaTarjetas("codigoDeptoGirador_", "--");

					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaTarjetas");
				}
				else if(estado.equals("consultaCiudades"))
				{
					formRecibos.setEstado("empezar");
					formRecibos.setMapaTarjetas("codigoCiudadGirador_", "--");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaTarjetas");
				}
				else if(estado.equals("consultaDeptosCheque"))
				{
					formRecibos.setEstado("nuevoRegistroFormaPago");
					formRecibos.setMapaCheques("codigoDeptoGirador_", "--");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaCheques");
				}
				else if(estado.equals("consultaCiudadesCheque"))
				{
					formRecibos.setEstado("nuevoRegistroFormaPago");
					formRecibos.setMapaCheques("codigoCiudadGirador_", "--");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaCheques");
				}
				else if(estado.equals("consultaDeptosCheques"))
				{
					formRecibos.setEstado("nuevoRegistroFormaPago");
					formRecibos.setMapaCheques("codigoDeptoPlaza_", "--");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaCheques");
				}
				else if(estado.equals("consultaCiudadesCheques"))
				{
					formRecibos.setEstado("nuevoRegistroFormaPago");
					formRecibos.setMapaCheques("codigoCiudadPlaza_", "--");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaCheques");
				}
				else if(estado.equals("generarEstructuraDesdeFactura"))
				{
					return this.accionGenerarEstructuraDesdeFactura(con,formRecibos, mundoRecibos, mapping, usuario, request);

				}else if(estado.equals("facturaVariaSeleccionado"))
				{
					return accionFacturaVariaSeleccionada(con, formRecibos, mapping);
				}else if(estado.equals("cargarConcepto"))
				{
					return accionVerificarConcepto(con,mundoRecibos, formRecibos, mapping);
				}else if(estado.equals("ordenarColumna"))
				{
					//return ordenarColumnas(formRecibos, mundoRecibos, mapping);
					return ordenarListaBusquedaFacturasVarias(formRecibos, mapping);

				}else if(estado.equals("resetBusqFacturasVarias"))
				{
					return restBusquedaFacVarias(formRecibos, mapping);
				}
				//**************************************************************************************************
				// Anexo 762 Cambio en Funcionalidade X Cartera Paciente
				else if(estado.equals("nuevoDocGarantia"))
				{
					return empezarNuevoDocGarantia(mapping, con, formRecibos,institucionBasica);
				}else if(estado.equals("asociarDocGarantia"))
				{
					return asociarDocGarantia(mapping, con, formRecibos,institucionBasica);
				}else if(estado.equals("menuPagareLetra"))
				{
					return menuPagareLetra(mapping, request, con, formRecibos,institucionBasica);
				}else if(estado.equals("cargarDatosDeudor"))
				{
					return cargarDatosDeudor(mapping, con, formRecibos,institucionBasica, request);
				}else if(estado.equals("cargarDatosCodeudor"))
				{
					return cargarDatosCodeudor(mapping, con, formRecibos,institucionBasica,request);
				}else if(estado.equals("guardarDatosFinan"))
				{
					return guardarDatosFinan(mapping, request, con, formRecibos,usuario, institucionBasica);
				}else if(estado.equals("cuotasDatFac"))
				{
					return generarCuotasDatosFinan(mapping, request, formRecibos,usuario);
				}else if(estado.equals("guardarCuotDatFinan"))
				{
					return guardarCuotasDatosFinan(mapping, request, formRecibos); 
				}else if(estado.equals("busquedaDeudor"))
				{
					return empezarBusquedaDeudor(mapping, con, formRecibos,usuario, institucionBasica); 
				}else if(estado.equals("buscarDeudores"))
				{
					return buscarDeudores(mapping, request, con, formRecibos); 
				}else if(estado.equals("redireccion"))
				{
					response.sendRedirect(formRecibos.getPaginaLinkSiguiente());
				}else if(estado.equals("ordenar"))
				{
					return ordenarDeudores(mapping, con, formRecibos);
				}else if(estado.equals("cargarDeudor"))
				{
					return seleccionarDeudor(mapping, con, formRecibos); 
				}
				// Fin Anexo 762 Cambio en Funcionalidade X Cartera Paciente
				//**************************************************************************************************

				//Anexo 958
				else if(estado.equals("empezarSeleccionFVAccion"))
				{
					//FIXME Se agrega validación cuando se hace llamado a esta funcionalidad desde 'Recibos de Caja'
					//y se debe validar que la caja se encuentre abierta sino debe mostrar mensaje de error al igual como
					//se hace por la funcionalidad de Tesoreria/Control Caja/Arqueos -Cierre Caja
					IMovimientosCajaServicio movimientosCajaServicio 	= TesoreriaFabricaServicio.crearMovimientosCajaServicio();

					UtilidadTransaccion.getTransaccion().begin();
					DtoTurnoDeCajaApta dtoApto = movimientosCajaServicio.esCajaAptaParaApertura(usuario, new CajasDelegate().findById(usuario.getConsecutivoCaja()));
					UtilidadTransaccion.getTransaccion().commit();

					if(
							(tipoCaja==ConstantesBD.codigoTipoCajaRecaudado || tipoCaja==ConstantesBD.codigoTipoCajaPpal) 
							&& (!dtoApto.isTurnoAbierto() || !dtoApto.isUsuarioTurnoCaja())
					)
					{
						ActionErrors errores=new ActionErrors();
						errores.add("no tiene turno abierto", new ActionMessage("error.recibosCaja.turnoCaja"));
						saveErrors(request, errores);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("paginaErroresActionErrors");
					}

					return this.accionEmpezarSeleccionFVAccion(con,formRecibos, mundoRecibos, mapping, usuario, request);
				}
				else if(estado.equals("ingresoCantidadBonos"))
				{
					return this.accionIngresoCantidadBonos(formRecibos, mapping);
				}
				else if(estado.equals("guardarSeriales"))
				{
					return this.accionGuardarSerialesBonos(formRecibos, mapping, request);
				}
				else if(estado.equals("selecionCodigoTarjeta"))
				{
					return this.accionSelecionCodigoTarjeta(formRecibos, mapping, request);

				}else if (estado.equals("imprimirFacturaVaria")){

					UtilidadBD.closeConnection(con);
					return mapping.findForward(imprimirFacturaVaria (formRecibos, usuario.getCodigoInstitucionInt(), institucionBasica.getCodigo(), request, usuario));



				}else if (estado.equals("volverFacturasVarias")){

					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaFacturasVarias");
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de RecibosCajaForm");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				return mapping.findForward("paginaError");
			}
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;
}

	/**
	 * 
	 * @param con 
	 * @param request 
	 * @param usuario
	 * @return
	 */
	private boolean validarConsecutivoDisponible(Connection con, HttpServletRequest request, UsuarioBasico usuario) {
		
		int codigoInstitucion = usuario.getCodigoInstitucionInt();
		int codigoCentroAtencion = usuario.getCodigoCentroAtencion();		
		String consecutivo="";
		boolean consecutivoEncontrado = false;
		boolean manejaConsecutivoTesoreriaXCentroAtencion=UtilidadTexto.getBoolean(ValoresPorDefecto.getManejaConsecutivosTesoreriaPorCentroAtencion(codigoInstitucion));
		
		
		if (manejaConsecutivoTesoreriaXCentroAtencion) {
			consecutivo=ConsecutivosCentroAtencion.obtenerValorActualConsecutivo(codigoCentroAtencion, EmunConsecutivosTesoreriaCentroAtencion.ReciboCaja.getNombreConsecutivoBaseDatos(), UtilidadFecha.getAnioActual()).toString();
		
			
			if (!UtilidadTexto.isEmpty(consecutivo) && UtilidadTexto.isNumber(consecutivo)) {
				
				int consec = Integer.parseInt(consecutivo);
				
				if (consec > 0) {
					consecutivoEncontrado = true;
				}else{
				ActionErrors errores=new ActionErrors();
				errores.add("falta definir consecutivo del recibo de caja", new ActionMessage("errors.faltaDefinirConsecutivoReciboCajaCentrosAtencion"));
				saveErrors(request, errores);
				
				}
			}
			
		}else{
			
			consecutivo=UtilidadBD.obtenerValorActualTablaConsecutivos(con, ConstantesBD.nombreConsecutivoReciboCaja, usuario.getCodigoInstitucionInt());
			
			if (!UtilidadTexto.isEmpty(consecutivo) && UtilidadTexto.isNumber(consecutivo)) {
				
				int consec = Integer.parseInt(consecutivo);
				
				if (consec > 0) {
					consecutivoEncontrado = true;
				}else{
					ActionErrors errores=new ActionErrors();
					errores.add("falta definir consecutivo del recibo de caja", new ActionMessage("errors.faltaDefinirConsecutivoReciboCaja"));
					saveErrors(request, errores);
				
				}
				
			}
		}
		
		return consecutivoEncontrado;
	}
	
	/**
	 * Lista las entidades financieras de acurdo al cï¿½digo de la tarjeta seleccionado
	 * @param forma
	 * @param mapping
	 * @param request
	 * @return
	 * @author Juan Daid Ram&iacute;rez
	 */
	private ActionForward accionSelecionCodigoTarjeta(RecibosCajaForm forma, ActionMapping mapping,HttpServletRequest request)
	{
		Integer codigoTarjeta=null;
		if(forma.getMapaTarjetas().get("codigoTarjeta_"+forma.getPosSelFormaPago())!=null)
		{
			codigoTarjeta=Integer.parseInt(forma.getMapaTarjetas().get("codigoTarjeta_"+forma.getPosSelFormaPago())+"");
		}
		if(!UtilidadTexto.isEmpty(codigoTarjeta))
		{
			forma.setEntidadesFinancieras(EntidadesFinancieras.consultarEntidadesFinancierasPorTarjeta(codigoTarjeta));
		}
		else
		{
			forma.setEntidadesFinancieras(new ArrayList<DtoEntidadesFinancieras>());
		}
		return mapping.findForward("paginaTarjetas");
	}

	private ActionForward accionGuardarSerialesBonos(
			RecibosCajaForm forma, ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errores=validarSerialesRepetidos(forma);
		if(errores.isEmpty())
		{
			forma.setEstado("cerrarPopup");
		}
		else
		{
			saveErrors(request, errores);
		}
		return mapping.findForward("paginaBonos");
	}

	/**
	 * Validar los seriales de los bonos ingresados
	 * No se permiten seriales repetidos
	 * Es requerido serial y valor mayor que 0
	 * @param forma
	 * @param mapping
	 * @param request
	 * @return Errores
	 * @author Juan David Ram&iacute;rez
	 */
	private ActionErrors validarSerialesRepetidos(RecibosCajaForm forma)
	{
		ArrayList<String> serialesRepetidos=new ArrayList<String>();
		ActionErrors errores=new ActionErrors();
		int i=0;
		DtoDetallePagosBonos detallePagoBono=forma.getFormaPagoBonos().get(forma.getPosSelFormaPago());
		detallePagoBono.setUtilizado(true);
		for(DtoBonoSerialValor bono:detallePagoBono.getSerialesBonos())
		{
			if(!UtilidadTexto.isEmpty(bono.getSerial()))
			{
				if(serialesRepetidos.contains(bono.getSerial()))
				{
					errores.add("serial repetido", new ActionMessage("error.recibosCaja.serialesbonos.repetidos"));
					//saveErrors(request, errores);
					break;
				}
				else
				{
					serialesRepetidos.add(bono.getSerial());
				}
			}
			else
			{
				errores.add("serial requerido "+i, new ActionMessage("errors.required", "El serial para el bono "+(i+1)));
			}
			if(bono.getValor()<=0)
			{
				errores.add("serial requerdido "+i, new ActionMessage("errors.MayorQue", "El serial para el bono "+(i+1), "0"));
			}
			i++;
		}
		
		return errores;
	}

	private ActionErrors validarTodosSerialesRepetidos(RecibosCajaForm forma)
	{
		ActionErrors errores=new ActionErrors();
		
		IFormasPagoServicio formasPagoServicio = TesoreriaFabricaServicio.crearFormasPagoServicio();
		
		DtoFormaPago formaPagoFiltro= new DtoFormaPago();
		
		DtoTipoDetalleFormaPago tipoDetalleFormaPago = new DtoTipoDetalleFormaPago();
		tipoDetalleFormaPago.setCodigo(ConstantesBD.codigoTipoDetalleFormasPagoBono);
		formaPagoFiltro.setTipoDetalle(tipoDetalleFormaPago);
		
		List<DtoFormaPago> listadoFormasPagoBono = formasPagoServicio.obtenerFormasPagos(formaPagoFiltro);
		
		for (DtoFormaPago dtoFormaPago : listadoFormasPagoBono) {
			
			ArrayList<String> serialesRepetidos=new ArrayList<String>();
			int i=0;
			int formaPago=0;
			
			for(DtoDetallePagosBonos detallePagoBono:forma.getFormaPagoBonos())
			{
				if(detallePagoBono.getConsecutivoFormaPago() == dtoFormaPago.getConsecutivo()){
					
					if(detallePagoBono.isUtilizado() && detallePagoBono!=null && detallePagoBono.getSerialesBonos()!=null)
					{
						if(detallePagoBono.getCantidadBonos()<=0)
						{
							errores.add("serial requerdido "+i, new ActionMessage("errors.MayorQue", "La cantidad de bonos para la forma de pago "+(formaPago+1)+" en la forma de pago "+(formaPago+1), "0"));
						}
						else
						{
							for(DtoBonoSerialValor bono:detallePagoBono.getSerialesBonos())
							{
								if(!UtilidadTexto.isEmpty(bono.getSerial()))
								{
									if(serialesRepetidos.contains(bono.getSerial()))
									{
										errores.add("serial repetido", new ActionMessage("error.recibosCaja.serialesbonos.repetidos"));
										//saveErrors(request, errores);
										break;
									}
									else
									{
										serialesRepetidos.add(bono.getSerial());
									}
								}
								else
								{
									errores.add("serial requerido "+i, new ActionMessage("errors.required", "El serial para el bono "+(i+1)+" en la forma de pago "+(formaPago+1)));
								}
								
								if(bono.getValor()<=0)
								{
									errores.add("serial requerdido "+i, new ActionMessage("errors.MayorQue", "El serial para el bono "+(i+1)+" en la forma de pago "+(formaPago+1), "0"));
								}
								
								i++;
							}
						}
					}
					
					formaPago++;
				}
			}
		}
		
		return errores;
	}

	private ActionForward accionIngresoCantidadBonos(RecibosCajaForm forma, ActionMapping mapping)
	{
		DtoDetallePagosBonos detallePago=forma.getFormaPagoBonos().get(forma.getPosSelFormaPago());
		
		int consecutivoFormaPago;
			
		try {
			
			consecutivoFormaPago = Integer.parseInt((String)forma.getMapaFormasPago("codigoFormaPago_"+ forma.getPosSelFormaPago()));
			
		} catch (Exception e) {
		
			consecutivoFormaPago = ConstantesBD.codigoNuncaValido;
		}	
		
		detallePago.setConsecutivoFormaPago(consecutivoFormaPago);
		
		detallePago.setSerialesBonos(new ArrayList<DtoBonoSerialValor>());
		
		for(int i=0; i<detallePago.getCantidadBonos(); i++)
		{
			detallePago.getSerialesBonos().add(new DtoBonoSerialValor());
		}
		return mapping.findForward("paginaBonos");
	}

	private ActionForward asociarDocGarantia(ActionMapping mapping,
			Connection con, RecibosCajaForm formRecibos,
			InstitucionBasica institucionBasica) {
		if(formRecibos.getDatosFinanciacion().getIsNuevoDoc().equals(ConstantesBD.acronimoSi))
			formRecibos.getDatosFinanciacion().reset();
		
		// se carga el deudor
		formRecibos.getDatosFinanciacion().setDeudor(DatosFinanciacion.obtenerDatosDeudorCO(con, 
				formRecibos.getIngresoFac(), 
				Utilidades.convertirAEntero(institucionBasica.getCodigo()), 
				ConstantesIntegridadDominio.acronimoDeudor));
		if(!formRecibos.getDatosFinanciacion().getDeudor().getTipoDeudor().equals(""))
			formRecibos.setSeccionDeudor(ConstantesBD.acronimoSi);
		
		// se carga el codeudor si existe
		formRecibos.getDatosFinanciacion().setCodeudor(DatosFinanciacion.obtenerDatosDeudorCO(con, 
				formRecibos.getIngresoFac(), 
				Utilidades.convertirAEntero(institucionBasica.getCodigo()), 
				ConstantesIntegridadDominio.acronimoCoDeudor));
		if(!formRecibos.getDatosFinanciacion().getCodeudor().getTipoDeudor().equals(""))
			formRecibos.setSeccionCodeudor(ConstantesBD.acronimoSi);
		
		if(!formRecibos.getDatosFinanciacion().getDeudor().getTipoDeudor().equals(""))
			formRecibos.setSeccionDeudor(ConstantesBD.acronimoSi);
		if(!formRecibos.getDatosFinanciacion().getCodeudor().getTipoDeudor().equals(""))
			formRecibos.setSeccionCodeudor(ConstantesBD.acronimoSi);
		if(formRecibos.getDatosFinanciacion().getMaxNroCuotasFinan()<=0)
			formRecibos.getDatosFinanciacion().setMaxNroCuotasFinan(Utilidades.convertirAEntero(ValoresPorDefecto.getMaximoNumeroCuotasFinanciacion(Utilidades.convertirAEntero(institucionBasica.getCodigo()))));
		if(formRecibos.getDatosFinanciacion().getMaxNroDiasFinanCuo()<=0)
			formRecibos.getDatosFinanciacion().setMaxNroDiasFinanCuo(Utilidades.convertirAEntero(ValoresPorDefecto.getMaximoNumeroDiasFinanciacionPorCuota(Utilidades.convertirAEntero(institucionBasica.getCodigo()))));
		if(formRecibos.getDatosFinanciacion().getFechaInicio().equals(""))
			formRecibos.getDatosFinanciacion().setFechaInicio(formRecibos.getMapaConceptos("fechaGenFactura_0")+"");
		if(formRecibos.getDatosFinanciacion().getNroCoutas()==ConstantesBD.codigoNuncaValido)
			formRecibos.getDatosFinanciacion().setNroCoutas(1);
		if(formRecibos.getDatosFinanciacion().getDiasPorCuota()==ConstantesBD.codigoNuncaValido)
			formRecibos.getDatosFinanciacion().setDiasPorCuota(30);
		formRecibos.getDatosFinanciacion().setProcesoExito(ConstantesBD.acronimoNo);
		formRecibos.getDatosFinanciacion().setProcesoExitosoCuo(ConstantesBD.acronimoNo);
		formRecibos.getDatosFinanciacion().setIsNuevoDoc(ConstantesBD.acronimoNo);
		formRecibos.setArrayTipIdent(Utilidades.obtenerTiposIdentificacion(con, "ingresoPoliza", Utilidades.convertirAEntero(institucionBasica.getCodigo())));
		
		return mapping.findForward("nuevoDocGarantia");
	}

	private ActionForward seleccionarDeudor(ActionMapping mapping,
			Connection con, RecibosCajaForm formRecibos) {
		this.inicializarPopUps(formRecibos);	   
		int copcepto=formRecibos.getPosSelConcepto();
		formRecibos.setMapaConceptos("readOnlyValoConcepto_"+formRecibos.getPosSelConcepto(),"false");
		formRecibos.setMapaConceptos("docSoporte_"+copcepto,"");
		formRecibos.setMapaConceptos("nombreBeneficiario_"+copcepto, formRecibos.getResultBusDeu().get(formRecibos.getPosDeudorSel()).getPrimerApellidoDeu()+" "+formRecibos.getResultBusDeu().get(formRecibos.getPosDeudorSel()).getSegundoApellidoDeu()+" "+formRecibos.getResultBusDeu().get(formRecibos.getPosDeudorSel()).getPrimerNombreDeu()+" "+formRecibos.getResultBusDeu().get(formRecibos.getPosDeudorSel()).getSegundoNombreDeu());
		formRecibos.setMapaConceptos("numeroBeneficiario_"+copcepto, formRecibos.getResultBusDeu().get(formRecibos.getPosDeudorSel()).getNumeroIdentificacionDeu());
		formRecibos.setMapaConceptos("tipoBeneficiario_"+copcepto, formRecibos.getResultBusDeu().get(formRecibos.getPosDeudorSel()).getTipoIdentificacionDeu());		
		formRecibos.setMapaConceptos("valorConcepto_"+copcepto,0);
		//formRecibos.setMapaConceptos("saldoPaciente_"+copcepto,formRecibos.getMapaFacturas("saldoPaciente_"+factura));
		//formRecibos.setMapaConceptos("codigoFactura_"+copcepto,formRecibos.getMapaFacturas("codigo_factura_"+factura));
		//formRecibos.setMapaConceptos("fechaGenFactura_"+copcepto,formRecibos.getMapaFacturas("fecha_"+factura));
		formRecibos.setMapaConceptos("poseeRegistroSeleccionado_"+copcepto,"true");
		//para validaciones
		//formRecibos.getMapaFacturas("saldoPaciente_"+factura)
		formRecibos.setValoresConceptos("valorConcepto_"+copcepto,0);
		formRecibos.setRecibidoDe(formRecibos.getMapaConceptos("nombreBeneficiario_"+copcepto)+"");
		
		//************************************************************************************
		// se cargan los datos del deudor a  vincular al recibo de caja
		formRecibos.getDatosFinanciacion().getDeudor().setClaseDeudor(formRecibos.getResultBusDeu().get(formRecibos.getPosDeudorSel()).getClaseDeudor());
		formRecibos.getDatosFinanciacion().getDeudor().setNumeroIdentificacion(formRecibos.getResultBusDeu().get(formRecibos.getPosDeudorSel()).getNumeroIdentificacionDeu());
		formRecibos.getDatosFinanciacion().getDeudor().setInstitucion(formRecibos.getResultBusDeu().get(formRecibos.getPosDeudorSel()).getCodInstitucion());
		formRecibos.getDatosFinanciacion().getDeudor().setIngreso(formRecibos.getResultBusDeu().get(formRecibos.getPosDeudorSel()).getIngreso());
		// se cargan los datos del deudor a  vincular al recibo de caja
		//************************************************************************************
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaPrincipal");
	}

	private ActionForward buscarDeudores(ActionMapping mapping,
			HttpServletRequest request, Connection con,
			RecibosCajaForm formRecibos) {
		ActionErrors errores = new ActionErrors(); 
		
		if(!formRecibos.getDeudor().getTipoIdentificacionDeu().equals(""))
			if(formRecibos.getDeudor().getNumeroIdentificacionDeu().equals(""))
				errores.add("numero ide deudor", new ActionMessage("errors.required","El Nï¿½mero de Identificaciï¿½n Deudor "));
		
		if(!formRecibos.getDeudor().getNumeroIdentificacionDeu().equals(""))
			if(formRecibos.getDeudor().getTipoIdentificacionDeu().equals(""))
				errores.add("tipo ide deudor", new ActionMessage("errors.required","El Tipo de Identificaciï¿½n Deudor "));
		 
		if(!formRecibos.getDeudor().getTipoIdentificacionPac().equals(""))
			if(formRecibos.getDeudor().getNumeroIdentificacionPac().equals(""))
				errores.add("numero ide paciente", new ActionMessage("errors.required","El Nï¿½mero de Identificaciï¿½n Paciente "));
		
		if(!formRecibos.getDeudor().getNumeroIdentificacionPac().equals(""))
			if(formRecibos.getDeudor().getTipoIdentificacionPac().equals(""))
				errores.add("tipo ide paciente", new ActionMessage("errors.required","El Tipo de Identificaciï¿½n Paciente "));
		
		if(errores.isEmpty())
		{
			formRecibos.getRompimientoPac().clear();
			formRecibos.getResultBusDeu().clear();
			formRecibos.setResultBusDeu(DatosFinanciacion.busquedaDeudores(con, formRecibos.getDeudor()));
			if(formRecibos.getResultBusDeu().size()>0 && formRecibos.getResultBusDeu().get(0).getRompimiento().equals(ConstantesBD.acronimoSi)){
				formRecibos.getRompimientoPac();
				formRecibos.setIsRomp(ConstantesBD.acronimoSi);
				formRecibos.setResultBusDeu(ordenarRompimiento(formRecibos));
			}
		}else
			saveErrors(request, errores);
		
		logger.info("sale a mostrarse los datos a la jsp");
		return mapping.findForward("busquedaDeudor");
	}

	private ActionForward empezarBusquedaDeudor(ActionMapping mapping,
			Connection con, RecibosCajaForm formRecibos, UsuarioBasico usuario,
			InstitucionBasica institucionBasica) {
		formRecibos.getResultBusDeu().clear();
		formRecibos.getRompimientoPac().clear();
		formRecibos.getDeudor().reset();
		formRecibos.getDeudor().setCodInstitucion(usuario.getCodigoInstitucionInt());
		formRecibos.setArrayTipIdent(Utilidades.obtenerTiposIdentificacion(con, "ingresoPoliza", Utilidades.convertirAEntero(institucionBasica.getCodigo())));
		logger.info("listo pa' cargar el popup");
		return mapping.findForward("busquedaDeudor");
	}

	private ActionForward guardarCuotasDatosFinan(ActionMapping mapping,
			HttpServletRequest request, RecibosCajaForm formRecibos) {
		ActionErrors errores = new ActionErrors(); 
		errores=validarCuotasDatosFinanciacion(formRecibos);
		if(errores.isEmpty()){
			formRecibos.getDatosFinanciacion().setProcesoExitosoCuo(ConstantesBD.acronimoSi);
		}else
			saveErrors(request, errores);
		return mapping.findForward("cuotasDatFinan");
	}

	private ActionForward generarCuotasDatosFinan(ActionMapping mapping,
			HttpServletRequest request, RecibosCajaForm formRecibos,
			UsuarioBasico usuario) {
		// realizar el calculo de numero de cuotas y los valores por cuota
		ActionErrors errores = new ActionErrors();
		if(!formRecibos.getMapaFormasPago("valorFormaPago_"+formRecibos.getPosSelFormaPago()).equals(""))
		{
			//logger.info("valor forma pago: "+formRecibos.getMapaFormasPago("valorFormaPago_"+formRecibos.getPosSelFormaPago()));
			if(formRecibos.getDatosFinanciacion().getNroCoutas()!=formRecibos.getDatosFinanciacion().getCuotasDatosFinan().size())
			{
				formRecibos.getDatosFinanciacion().getCuotasDatosFinan().clear();
				BigDecimal valorFormaPago = new BigDecimal(Utilidades.convertirAEntero(formRecibos.getMapaFormasPago("valorFormaPago_"+formRecibos.getPosSelFormaPago())+""));
				BigDecimal valorCuota = new BigDecimal(0);
				BigDecimal aux = new BigDecimal(0);
				
				valorCuota = valorFormaPago.divide(new BigDecimal(formRecibos.getDatosFinanciacion().getNroCoutas()),2, BigDecimal.ROUND_HALF_EVEN);
				//logger.info("Valor Cuota: "+valorCuota);
				for(int i=0;i<formRecibos.getDatosFinanciacion().getNroCoutas();i++)
				{
					DtoCuotasDatosFinanciacion dto = new DtoCuotasDatosFinanciacion();
					dto.setUsuarioModifica(usuario.getLoginUsuario());
					dto.setActivo(ConstantesBD.acronimoSi);
					aux = aux.add(valorCuota);
					//logger.info("valor acomulativo de las cuotas: "+aux.toString());
					//logger.info("scala o con formato: "+aux.setScale(2,BigDecimal.ROUND_CEILING).toString());
					if(i+1==formRecibos.getDatosFinanciacion().getNroCoutas())
					{
						//logger.info("valor cuota:"+valorFormaPago.toString());
						//logger.info("valor aux: "+aux.toString());
						if(!aux.equals(valorFormaPago)){
							//logger.info("antes de la resta: "+aux.toString());
							aux = valorFormaPago.subtract(aux);
							//logger.info("despues de la resta: "+aux.toString());
							//logger.info("despues de la resta valor absoluto: "+aux.abs().toString());
							valorCuota = valorCuota.add(aux);
							dto.setValorCuota(valorCuota);
						}else
							dto.setValorCuota(valorCuota);
					}else
						dto.setValorCuota(valorCuota);
					formRecibos.getDatosFinanciacion().getCuotasDatosFinan().add(dto);
				}
			}
		}else{
			errores.add("valor forma pago", new ActionMessage("errors.required","El Valor de la Forma de Pago "));
			saveErrors(request, errores);
		}
		return mapping.findForward("cuotasDatFinan");
	}

	
	/**
	 * 
	 * @param mapping
	 * @param request
	 * @param con
	 * @param formRecibos
	 * @param usuario
	 * @param institucionBasica
	 * @return
	 */
	private ActionForward guardarDatosFinan(ActionMapping mapping,
			HttpServletRequest request, Connection con,
			RecibosCajaForm formRecibos, UsuarioBasico usuario,
			InstitucionBasica institucionBasica) {
		// validaciones
		ActionErrors errores = new ActionErrors(); 
		errores = validarDatosFinanciacion(formRecibos);
		if(errores.isEmpty())
		{
			HashMap mapa = new HashMap();
			
			logger.info("Consecutivo de la Factura: "+formRecibos.getMapaConceptos("docSoporte_0"));
			
			mapa = DatosFinanciacion.obtenerIngresoFactura(con,
					Utilidades.convertirAEntero(formRecibos.getMapaConceptos("docSoporte_0")+""));
			
			if(formRecibos.getDatosFinanciacion().getIsNuevoDoc().equals(ConstantesBD.acronimoSi))
			{
				logger.info("valor del ingreso: "+mapa.get("ingreso").toString());
				//logger.info("id cuenta: "+mapa.get("cuenta").toString());
				
				if(mapa.containsKey("ingreso")&&!mapa.get("ingreso").toString().equals(""))
				{
					formRecibos.getDatosFinanciacion().setIngreso(Utilidades.convertirAEntero(mapa.get("ingreso").toString()));
					formRecibos.getDatosFinanciacion().setCodigoPacienteFac(Utilidades.convertirAEntero(mapa.get("codigo_paciente").toString()));
					formRecibos.getDatosFinanciacion().setCodigoFactura(Utilidades.convertirAEntero(mapa.get("codigo_factura")+""));
					formRecibos.getDatosFinanciacion().setUsuarioModifica(usuario.getLoginUsuario());
					//formRecibos.getDatosFinanciacion().getDeudor().setCuenta(Utilidades.convertirAEntero(mapa.get("cuenta").toString()));
					formRecibos.getDatosFinanciacion().getDeudor().setIngreso(Utilidades.convertirAEntero(mapa.get("ingreso").toString()));
					formRecibos.getDatosFinanciacion().getDeudor().setInstitucion(Utilidades.convertirAEntero(institucionBasica.getCodigo()));
					formRecibos.getDatosFinanciacion().getDeudor().setUsuarioModifica(usuario.getLoginUsuario());
					if(!formRecibos.getDatosFinanciacion().getCodeudor().getTipoDeudor().equals("")){
						//formRecibos.getDatosFinanciacion().getCodeudor().setCuenta(Utilidades.convertirAEntero(mapa.get("cuenta").toString()));
						formRecibos.getDatosFinanciacion().getCodeudor().setIngreso(Utilidades.convertirAEntero(mapa.get("ingreso").toString()));
						formRecibos.getDatosFinanciacion().getCodeudor().setInstitucion(Utilidades.convertirAEntero(institucionBasica.getCodigo()));
						formRecibos.getDatosFinanciacion().getCodeudor().setUsuarioModifica(usuario.getLoginUsuario());
					}else
						formRecibos.setSeccionCodeudor(ConstantesBD.acronimoNo);
					formRecibos.getDatosFinanciacion().setProcesoExito(ConstantesBD.acronimoSi);
					formRecibos.setDatFinanGuarExis(ConstantesBD.acronimoSi);
				}else{
					formRecibos.getDatosFinanciacion().setProcesoExito(ConstantesBD.acronimoNo);
					formRecibos.setDatFinanGuarExis(ConstantesBD.acronimoNo);
					errores.add("datos financiacion", new ActionMessage("errors.notEspecific","Error al Guardar los datos financiaciï¿½n "));
				}
			}else{
				if(formRecibos.getDatosFinanciacion().getIsNuevoDoc().equals(ConstantesBD.acronimoNo))
				{// se guarda datos de financiacion de un documento asociado
					logger.info("entra en la parte de asociar documentos");
					if(mapa.containsKey("ingreso")&&!mapa.get("ingreso").toString().equals(""))
					{
						formRecibos.getDatosFinanciacion().setIngreso(Utilidades.convertirAEntero(mapa.get("ingreso").toString()));
						formRecibos.getDatosFinanciacion().setCodigoPacienteFac(Utilidades.convertirAEntero(mapa.get("codigo_paciente").toString()));
						formRecibos.getDatosFinanciacion().setCodigoFactura(Utilidades.convertirAEntero(mapa.get("codigo_factura")+""));
						formRecibos.getDatosFinanciacion().setUsuarioModifica(usuario.getLoginUsuario());
						formRecibos.getDatosFinanciacion().getDeudor().setUsuarioModifica(usuario.getLoginUsuario());
						if(!formRecibos.getDatosFinanciacion().getCodeudor().getTipoDeudor().equals("")){
							formRecibos.getDatosFinanciacion().getCodeudor().setUsuarioModifica(usuario.getLoginUsuario());
						}else
							formRecibos.setSeccionCodeudor(ConstantesBD.acronimoNo);
						formRecibos.getDatosFinanciacion().setProcesoExito(ConstantesBD.acronimoSi);
						formRecibos.setDatFinanGuarExis(ConstantesBD.acronimoSi);
					}else{
						formRecibos.getDatosFinanciacion().setProcesoExito(ConstantesBD.acronimoNo);
						formRecibos.setDatFinanGuarExis(ConstantesBD.acronimoNo);
						errores.add("datos financiacion", new ActionMessage("errors.notEspecific","Error al Guardar los datos financiaciï¿½n "));
					}
				}
			}
		}else
			saveErrors(request, errores);
		logger.info("existe deudor: "+formRecibos.getDatosFinanciacion().getDeudor().getExiteDeudor());
		logger.info("existe codeudor: "+formRecibos.getDatosFinanciacion().getCodeudor().getExiteDeudor());
		return mapping.findForward("nuevoDocGarantia");
	}

	
	/**
	 * 
	 * @param mapping
	 * @param con
	 * @param formRecibos
	 * @param institucionBasica
	 * @param request
	 * @return
	 */
	private ActionForward cargarDatosCodeudor(ActionMapping mapping,
			Connection con, RecibosCajaForm formRecibos,
			InstitucionBasica institucionBasica, HttpServletRequest request) {
			
			
			ActionErrors errores = new ActionErrors(); 
			DtoDeudoresDatosFinan dto = new DtoDeudoresDatosFinan();
			logger.info("Tipo Deudor: "+formRecibos.getDatosFinanciacion().getCodeudor().getTipoDeudor());
			
			if(!formRecibos.getDatosFinanciacion().getDeudor().getTipoDeudor().equals(formRecibos.getDatosFinanciacion().getCodeudor().getTipoDeudor()))
			{
				formRecibos.getDatosFinanciacion().getCodeudor().setClaseDeudor(ConstantesIntegridadDominio.acronimoCoDeudor);
				if(formRecibos.getDatosFinanciacion().getCodeudor().getTipoDeudor().equals(ConstantesIntegridadDominio.acronimoPaciente))
				{
					logger.info("D. Soporte [consecutivo factura]: "+formRecibos.getMapaConceptos("docSoporte_0"));
					dto = DatosFinanciacion.obtenerDatosDeudor(con, 
							Utilidades.convertirAEntero(formRecibos.getMapaConceptos("docSoporte_0")+""),
							ConstantesIntegridadDominio.acronimoPaciente);
					formRecibos.getDatosFinanciacion().getCodeudor().setCodigoPaciente(dto.getCodigoPaciente());
					formRecibos.getDatosFinanciacion().getCodeudor().setIngreso(dto.getIngreso());
					formRecibos.getDatosFinanciacion().getCodeudor().setInstitucion(dto.getInstitucion());
					//formRecibos.getDatosFinanciacion().getCodeudor().setClaseDeudor(dto.getClaseDeudor());
					formRecibos.getDatosFinanciacion().getCodeudor().setTipoIdentificacion(dto.getTipoIdentificacion());
					formRecibos.getDatosFinanciacion().getCodeudor().setNumeroIdentificacion(dto.getNumeroIdentificacion());
					formRecibos.getDatosFinanciacion().getCodeudor().setPrimerApellido(dto.getPrimerApellido());
					formRecibos.getDatosFinanciacion().getCodeudor().setSegundoApellido(dto.getSegundoApellido());
					formRecibos.getDatosFinanciacion().getCodeudor().setPrimerNombre(dto.getPrimerNombre());
					formRecibos.getDatosFinanciacion().getCodeudor().setSegundoNombre(dto.getSegundoNombre());
					formRecibos.getDatosFinanciacion().getCodeudor().setDireccion(dto.getDireccion());
					formRecibos.getDatosFinanciacion().getCodeudor().setTelefono(dto.getTelefono());
					formRecibos.getDatosFinanciacion().getCodeudor().setExiteDeudor(dto.getExiteDeudor());
					formRecibos.getDatosFinanciacion().getCodeudor().setExistePerRes(dto.getExistePerRes());
					formRecibos.setSeccionDeudor(ConstantesBD.acronimoSi);
				}else{
					if(formRecibos.getDatosFinanciacion().getCodeudor().getTipoDeudor().equals(ConstantesIntegridadDominio.acronimoResponsablePaci))
					{
						if((dto=DatosFinanciacion.obtenerDatosResponsable(con, 
								Utilidades.convertirAEntero(formRecibos.getMapaConceptos("docSoporte_0")+""),
								ConstantesIntegridadDominio.acronimoCoDeudor))!=null)
						{
							formRecibos.getDatosFinanciacion().getCodeudor().setCodigoPaciente(dto.getCodigoPaciente());
							formRecibos.getDatosFinanciacion().getCodeudor().setIngreso(dto.getIngreso());
							formRecibos.getDatosFinanciacion().getCodeudor().setInstitucion(dto.getInstitucion());
							//formRecibos.getDatosFinanciacion().getCodeudor().setClaseDeudor(dto.getClaseDeudor());
							formRecibos.getDatosFinanciacion().getCodeudor().setTipoIdentificacion(dto.getTipoIdentificacion());
							formRecibos.getDatosFinanciacion().getCodeudor().setNumeroIdentificacion(dto.getNumeroIdentificacion());
							formRecibos.getDatosFinanciacion().getCodeudor().setPrimerApellido(dto.getPrimerApellido());
							formRecibos.getDatosFinanciacion().getCodeudor().setSegundoApellido(dto.getSegundoApellido());
							formRecibos.getDatosFinanciacion().getCodeudor().setPrimerNombre(dto.getPrimerNombre());
							formRecibos.getDatosFinanciacion().getCodeudor().setSegundoNombre(dto.getSegundoNombre());
							formRecibos.getDatosFinanciacion().getCodeudor().setDireccion(dto.getDireccion());
							formRecibos.getDatosFinanciacion().getCodeudor().setTelefono(dto.getTelefono());
							formRecibos.getDatosFinanciacion().getCodeudor().setExiteDeudor(dto.getExiteDeudor());
							formRecibos.getDatosFinanciacion().getCodeudor().setExistePerRes(dto.getExistePerRes());
							formRecibos.getDatosFinanciacion().getCodeudor().setCodigoPK(dto.getCodigoPkDeudor());
							formRecibos.setSeccionDeudor(ConstantesBD.acronimoSi);
						}
					}else{
						if(formRecibos.getDatosFinanciacion().getCodeudor().getTipoDeudor().equals(ConstantesIntegridadDominio.acronimoOtro)
								|| formRecibos.getDatosFinanciacion().getCodeudor().getTipoDeudor().equals(""))
						{
							if(formRecibos.getDatosFinanciacion().getCodeudor().getTipoDeudor().equals(ConstantesIntegridadDominio.acronimoOtro)
									&& !formRecibos.getDatosFinanciacion().getCodeudor().getTipoIdentificacion().equals("")
									&& !formRecibos.getDatosFinanciacion().getCodeudor().getNumeroIdentificacion().equals(""))
							{
								if((dto=DatosFinanciacion.verificarExistenciaPer(con, 
										formRecibos.getDatosFinanciacion().getCodeudor().getTipoIdentificacion(),
										formRecibos.getDatosFinanciacion().getCodeudor().getNumeroIdentificacion(),
										ConstantesIntegridadDominio.acronimoCoDeudor))!=null)
								{
									
									HashMap mapa = new HashMap(); 
									mapa = DatosFinanciacion.obtenerIngresoFactura(con,
											Utilidades.convertirAEntero(formRecibos.getMapaConceptos("docSoporte_0")+""));
									
									formRecibos.getDatosFinanciacion().getCodeudor().setCodigoPaciente(dto.getCodigoPaciente());
									formRecibos.getDatosFinanciacion().getCodeudor().setIngreso(dto.getIngreso());
									formRecibos.getDatosFinanciacion().getCodeudor().setInstitucion(dto.getInstitucion());
									//formRecibos.getDatosFinanciacion().getCodeudor().setClaseDeudor(dto.getClaseDeudor());
									formRecibos.getDatosFinanciacion().getCodeudor().setTipoIdentificacion(dto.getTipoIdentificacion());
									formRecibos.getDatosFinanciacion().getCodeudor().setNumeroIdentificacion(dto.getNumeroIdentificacion());
									formRecibos.getDatosFinanciacion().getCodeudor().setPrimerApellido(dto.getPrimerApellido());
									formRecibos.getDatosFinanciacion().getCodeudor().setSegundoApellido(dto.getSegundoApellido());
									formRecibos.getDatosFinanciacion().getCodeudor().setPrimerNombre(dto.getPrimerNombre());
									formRecibos.getDatosFinanciacion().getCodeudor().setSegundoNombre(dto.getSegundoNombre());
									formRecibos.getDatosFinanciacion().getCodeudor().setDireccion(dto.getDireccion());
									formRecibos.getDatosFinanciacion().getCodeudor().setTelefono(dto.getTelefono());
									
									if(!mapa.get("ingreso").toString().equals(ConstantesBD.acronimoNo)){
										if(dto.getIngreso()==Utilidades.convertirAEntero(mapa.get("ingreso")+""))
											formRecibos.getDatosFinanciacion().getCodeudor().setExiteDeudor(ConstantesBD.acronimoSi);
										else
											formRecibos.getDatosFinanciacion().getCodeudor().setExiteDeudor(ConstantesBD.acronimoNo);
									}else
										formRecibos.getDatosFinanciacion().getCodeudor().setExiteDeudor(ConstantesBD.acronimoNo);
									
									formRecibos.getDatosFinanciacion().getCodeudor().setExistePerRes(dto.getExistePerRes());
									formRecibos.setSeccionDeudor(ConstantesBD.acronimoSi);
								}else{
									formRecibos.getDatosFinanciacion().getDeudor().setCodigoPaciente("");
									formRecibos.getDatosFinanciacion().getDeudor().setIngreso(ConstantesBD.codigoNuncaValido);
									formRecibos.getDatosFinanciacion().getDeudor().setInstitucion(ConstantesBD.codigoNuncaValido);
									//formRecibos.getDatosFinanciacion().getDeudor().setClaseDeudor("");
									formRecibos.getDatosFinanciacion().getCodeudor().setPrimerApellido("");
									formRecibos.getDatosFinanciacion().getCodeudor().setSegundoApellido("");
									formRecibos.getDatosFinanciacion().getCodeudor().setPrimerNombre("");
									formRecibos.getDatosFinanciacion().getCodeudor().setSegundoNombre("");
									formRecibos.getDatosFinanciacion().getCodeudor().setDireccion("");
									formRecibos.getDatosFinanciacion().getCodeudor().setTelefono("");
									formRecibos.getDatosFinanciacion().getCodeudor().setExiteDeudor(ConstantesBD.acronimoNo);
									formRecibos.getDatosFinanciacion().getCodeudor().setExistePerRes(ConstantesBD.acronimoNo);
								}
							}else{
								formRecibos.getDatosFinanciacion().getDeudor().setCodigoPaciente("");
								formRecibos.getDatosFinanciacion().getDeudor().setIngreso(ConstantesBD.codigoNuncaValido);
								formRecibos.getDatosFinanciacion().getDeudor().setInstitucion(ConstantesBD.codigoNuncaValido);
								//formRecibos.getDatosFinanciacion().getDeudor().setClaseDeudor("");
								formRecibos.getDatosFinanciacion().getCodeudor().setTipoIdentificacion(ValoresPorDefecto.getTipoId(Utilidades.convertirAEntero(institucionBasica.getCodigo())));
								formRecibos.getDatosFinanciacion().getCodeudor().setNumeroIdentificacion("");
								formRecibos.getDatosFinanciacion().getCodeudor().setPrimerApellido("");
								formRecibos.getDatosFinanciacion().getCodeudor().setSegundoApellido("");
								formRecibos.getDatosFinanciacion().getCodeudor().setPrimerNombre("");
								formRecibos.getDatosFinanciacion().getCodeudor().setSegundoNombre("");
								formRecibos.getDatosFinanciacion().getCodeudor().setDireccion("");
								formRecibos.getDatosFinanciacion().getCodeudor().setTelefono("");
								formRecibos.getDatosFinanciacion().getCodeudor().setExiteDeudor(ConstantesBD.acronimoNo);
								formRecibos.getDatosFinanciacion().getCodeudor().setExistePerRes(ConstantesBD.acronimoNo);
							}
						}
					}
				}
			//}
			
			if(formRecibos.getDatosFinanciacion().getDeudor().getTipoIdentificacion().equals(formRecibos.getDatosFinanciacion().getCodeudor().getTipoIdentificacion())
					&& formRecibos.getDatosFinanciacion().getDeudor().getNumeroIdentificacion().equals(formRecibos.getDatosFinanciacion().getCodeudor().getNumeroIdentificacion())
					&& !formRecibos.getDatosFinanciacion().getDeudor().getTipoDeudor().equals(""))
					errores.add("descripcion", new ActionMessage("errors.notEspecific","El Codeudor debe ser diferente al deudor. "));
			
			if(!errores.isEmpty()){
				formRecibos.getDatosFinanciacion().getCodeudor().setExiteDeudor(ConstantesBD.acronimoNo);
				formRecibos.getDatosFinanciacion().getCodeudor().setExistePerRes(ConstantesBD.acronimoNo);
				saveErrors(request, errores);
			}
		}else{
			if(!formRecibos.getDatosFinanciacion().getDeudor().getTipoDeudor().equals("")
					&& !formRecibos.getDatosFinanciacion().getCodeudor().getTipoDeudor().equals(""))
			{
				errores.add("descripcion", new ActionMessage("errors.notEspecific","El Tipo de Codeudor debe ser diferente al Tipo de Deudor. "));
				formRecibos.getDatosFinanciacion().getCodeudor().setTipoDeudor("");
				formRecibos.getDatosFinanciacion().getCodeudor().setExiteDeudor(ConstantesBD.acronimoNo);
				formRecibos.getDatosFinanciacion().getCodeudor().setExistePerRes(ConstantesBD.acronimoNo);
				saveErrors(request, errores);
			}else{
				formRecibos.getDatosFinanciacion().getDeudor().setCodigoPaciente("");
				formRecibos.getDatosFinanciacion().getDeudor().setIngreso(ConstantesBD.codigoNuncaValido);
				formRecibos.getDatosFinanciacion().getDeudor().setInstitucion(ConstantesBD.codigoNuncaValido);
				//formRecibos.getDatosFinanciacion().getDeudor().setClaseDeudor("");
				formRecibos.getDatosFinanciacion().getCodeudor().setTipoIdentificacion(ValoresPorDefecto.getTipoId(Utilidades.convertirAEntero(institucionBasica.getCodigo())));
				formRecibos.getDatosFinanciacion().getCodeudor().setNumeroIdentificacion("");
				formRecibos.getDatosFinanciacion().getCodeudor().setPrimerApellido("");
				formRecibos.getDatosFinanciacion().getCodeudor().setSegundoApellido("");
				formRecibos.getDatosFinanciacion().getCodeudor().setPrimerNombre("");
				formRecibos.getDatosFinanciacion().getCodeudor().setSegundoNombre("");
				formRecibos.getDatosFinanciacion().getCodeudor().setDireccion("");
				formRecibos.getDatosFinanciacion().getCodeudor().setTelefono("");
				formRecibos.getDatosFinanciacion().getCodeudor().setExiteDeudor(ConstantesBD.acronimoNo);
				formRecibos.getDatosFinanciacion().getCodeudor().setExistePerRes(ConstantesBD.acronimoNo);
			}
		}
			
		return mapping.findForward("nuevoDocGarantia");
	}

	private ActionForward cargarDatosDeudor(ActionMapping mapping,
			Connection con, RecibosCajaForm formRecibos,
			InstitucionBasica institucionBasica, HttpServletRequest request) {
		
		ActionErrors errores = new ActionErrors();
		DtoDeudoresDatosFinan dto = new DtoDeudoresDatosFinan();
		logger.info("D soporte [consecutivo_facatura]: "+formRecibos.getMapaConceptos("docSoporte_0"));
		if(!formRecibos.getDatosFinanciacion().getDeudor().getTipoDeudor().equals(formRecibos.getDatosFinanciacion().getCodeudor().getTipoDeudor()))
		{
			if(!formRecibos.getMapaConceptos("docSoporte_0").equals(""))
			{
				logger.info("Tipo Deudor: "+formRecibos.getDatosFinanciacion().getDeudor().getTipoDeudor());
				formRecibos.getDatosFinanciacion().getDeudor().setClaseDeudor(ConstantesIntegridadDominio.acronimoDeudor);
				if(formRecibos.getDatosFinanciacion().getDeudor().getTipoDeudor().equals(ConstantesIntegridadDominio.acronimoPaciente))
				{
					logger.info("D. Soporte [consecutivo factura]: "+formRecibos.getMapaConceptos("docSoporte_0"));
					dto = DatosFinanciacion.obtenerDatosDeudor(con, 
							Utilidades.convertirAEntero(formRecibos.getMapaConceptos("docSoporte_0")+""),
							ConstantesIntegridadDominio.acronimoDeudor);
					formRecibos.getDatosFinanciacion().getDeudor().setCodigoPaciente(dto.getCodigoPaciente());
					formRecibos.getDatosFinanciacion().getDeudor().setCodigoPK(dto.getCodigoPkDeudor());
					formRecibos.getDatosFinanciacion().getDeudor().setIngreso(dto.getIngreso());
					formRecibos.getDatosFinanciacion().getDeudor().setInstitucion(dto.getInstitucion());
					//formRecibos.getDatosFinanciacion().getDeudor().setClaseDeudor(dto.getClaseDeudor());
					formRecibos.getDatosFinanciacion().getDeudor().setTipoIdentificacion(dto.getTipoIdentificacion());
					formRecibos.getDatosFinanciacion().getDeudor().setNumeroIdentificacion(dto.getNumeroIdentificacion());
					formRecibos.getDatosFinanciacion().getDeudor().setPrimerApellido(dto.getPrimerApellido());
					formRecibos.getDatosFinanciacion().getDeudor().setSegundoApellido(dto.getSegundoApellido());
					formRecibos.getDatosFinanciacion().getDeudor().setPrimerNombre(dto.getPrimerNombre());
					formRecibos.getDatosFinanciacion().getDeudor().setSegundoNombre(dto.getSegundoNombre());
					formRecibos.getDatosFinanciacion().getDeudor().setDireccion(dto.getDireccion());
					formRecibos.getDatosFinanciacion().getDeudor().setTelefono(dto.getTelefono());
					formRecibos.getDatosFinanciacion().getDeudor().setExiteDeudor(dto.getExiteDeudor());
					formRecibos.getDatosFinanciacion().getDeudor().setExistePerRes(dto.getExistePerRes());
					formRecibos.setSeccionDeudor(ConstantesBD.acronimoSi);
				}else{
					if(formRecibos.getDatosFinanciacion().getDeudor().getTipoDeudor().equals(ConstantesIntegridadDominio.acronimoResponsablePaci))
					{
						if((dto=DatosFinanciacion.obtenerDatosResponsable(con, 
								Utilidades.convertirAEntero(formRecibos.getMapaConceptos("docSoporte_0")+""),
								ConstantesIntegridadDominio.acronimoDeudor))!=null)
						{
							formRecibos.getDatosFinanciacion().getDeudor().setCodigoPaciente(dto.getCodigoPaciente());
							formRecibos.getDatosFinanciacion().getDeudor().setIngreso(dto.getIngreso());
							formRecibos.getDatosFinanciacion().getDeudor().setInstitucion(dto.getInstitucion());
							//formRecibos.getDatosFinanciacion().getDeudor().setClaseDeudor(dto.getClaseDeudor());
							formRecibos.getDatosFinanciacion().getDeudor().setTipoIdentificacion(dto.getTipoIdentificacion());
							formRecibos.getDatosFinanciacion().getDeudor().setNumeroIdentificacion(dto.getNumeroIdentificacion());
							formRecibos.getDatosFinanciacion().getDeudor().setPrimerApellido(dto.getPrimerApellido());
							formRecibos.getDatosFinanciacion().getDeudor().setSegundoApellido(dto.getSegundoApellido());
							formRecibos.getDatosFinanciacion().getDeudor().setPrimerNombre(dto.getPrimerNombre());
							formRecibos.getDatosFinanciacion().getDeudor().setSegundoNombre(dto.getSegundoNombre());
							formRecibos.getDatosFinanciacion().getDeudor().setDireccion(dto.getDireccion());
							formRecibos.getDatosFinanciacion().getDeudor().setTelefono(dto.getTelefono());
							formRecibos.getDatosFinanciacion().getDeudor().setExiteDeudor(dto.getExiteDeudor());
							formRecibos.getDatosFinanciacion().getDeudor().setExistePerRes(dto.getExistePerRes());
							formRecibos.getDatosFinanciacion().getDeudor().setCodigoPK(dto.getCodigoPkDeudor());
							formRecibos.setSeccionDeudor(ConstantesBD.acronimoSi);
						}
					}else{
						if(formRecibos.getDatosFinanciacion().getDeudor().getTipoDeudor().equals(ConstantesIntegridadDominio.acronimoOtro)
								|| formRecibos.getDatosFinanciacion().getDeudor().getTipoDeudor().equals(""))
						{
							if(formRecibos.getDatosFinanciacion().getDeudor().getTipoDeudor().equals(ConstantesIntegridadDominio.acronimoOtro)
									&& !formRecibos.getDatosFinanciacion().getDeudor().getTipoIdentificacion().equals("")
									&& !formRecibos.getDatosFinanciacion().getDeudor().getNumeroIdentificacion().equals(""))
							{
								if((dto=DatosFinanciacion.verificarExistenciaPer(con, 
										formRecibos.getDatosFinanciacion().getDeudor().getTipoIdentificacion(),
										formRecibos.getDatosFinanciacion().getDeudor().getNumeroIdentificacion(),
										ConstantesIntegridadDominio.acronimoDeudor))!=null)
								{
									HashMap mapa = new HashMap(); 
									mapa = DatosFinanciacion.obtenerIngresoFactura(con,
											Utilidades.convertirAEntero(formRecibos.getMapaConceptos("docSoporte_0")+""));
									
									
									formRecibos.getDatosFinanciacion().getDeudor().setCodigoPaciente(dto.getCodigoPaciente());
									formRecibos.getDatosFinanciacion().getDeudor().setIngreso(dto.getIngreso());
									formRecibos.getDatosFinanciacion().getDeudor().setInstitucion(dto.getInstitucion());
									//formRecibos.getDatosFinanciacion().getDeudor().setClaseDeudor(dto.getClaseDeudor());
									formRecibos.getDatosFinanciacion().getDeudor().setTipoIdentificacion(dto.getTipoIdentificacion());
									formRecibos.getDatosFinanciacion().getDeudor().setNumeroIdentificacion(dto.getNumeroIdentificacion());
									formRecibos.getDatosFinanciacion().getDeudor().setPrimerApellido(dto.getPrimerApellido());
									formRecibos.getDatosFinanciacion().getDeudor().setSegundoApellido(dto.getSegundoApellido());
									formRecibos.getDatosFinanciacion().getDeudor().setPrimerNombre(dto.getPrimerNombre());
									formRecibos.getDatosFinanciacion().getDeudor().setSegundoNombre(dto.getSegundoNombre());
									formRecibos.getDatosFinanciacion().getDeudor().setDireccion(dto.getDireccion());
									formRecibos.getDatosFinanciacion().getDeudor().setTelefono(dto.getTelefono());
									
									if(!mapa.get("ingreso").toString().equals(ConstantesBD.acronimoNo)){
										if(dto.getIngreso()==Utilidades.convertirAEntero(mapa.get("ingreso")+""))
											formRecibos.getDatosFinanciacion().getDeudor().setExiteDeudor(ConstantesBD.acronimoSi);
										else
											formRecibos.getDatosFinanciacion().getDeudor().setExiteDeudor(ConstantesBD.acronimoNo);
									}else
										formRecibos.getDatosFinanciacion().getDeudor().setExiteDeudor(ConstantesBD.acronimoNo);
									
									formRecibos.getDatosFinanciacion().getDeudor().setExistePerRes(dto.getExistePerRes());
									formRecibos.setSeccionDeudor(ConstantesBD.acronimoSi);
								}else{
									formRecibos.getDatosFinanciacion().getDeudor().setCodigoPaciente("");
									formRecibos.getDatosFinanciacion().getDeudor().setIngreso(ConstantesBD.codigoNuncaValido);
									formRecibos.getDatosFinanciacion().getDeudor().setInstitucion(ConstantesBD.codigoNuncaValido);
									//formRecibos.getDatosFinanciacion().getDeudor().setClaseDeudor("");
									formRecibos.getDatosFinanciacion().getDeudor().setPrimerApellido("");
									formRecibos.getDatosFinanciacion().getDeudor().setSegundoApellido("");
									formRecibos.getDatosFinanciacion().getDeudor().setPrimerNombre("");
									formRecibos.getDatosFinanciacion().getDeudor().setSegundoNombre("");
									formRecibos.getDatosFinanciacion().getDeudor().setDireccion("");
									formRecibos.getDatosFinanciacion().getDeudor().setTelefono("");
									formRecibos.getDatosFinanciacion().getDeudor().setExiteDeudor(ConstantesBD.acronimoNo);
									formRecibos.getDatosFinanciacion().getDeudor().setExistePerRes(ConstantesBD.acronimoNo);
								}
							}else{
								formRecibos.getDatosFinanciacion().getDeudor().setCodigoPaciente("");
								formRecibos.getDatosFinanciacion().getDeudor().setIngreso(ConstantesBD.codigoNuncaValido);
								formRecibos.getDatosFinanciacion().getDeudor().setInstitucion(ConstantesBD.codigoNuncaValido);
								//formRecibos.getDatosFinanciacion().getDeudor().setClaseDeudor("");
								formRecibos.getDatosFinanciacion().getDeudor().setTipoIdentificacion(ValoresPorDefecto.getTipoId(Utilidades.convertirAEntero(institucionBasica.getCodigo())));
								formRecibos.getDatosFinanciacion().getDeudor().setNumeroIdentificacion("");
								formRecibos.getDatosFinanciacion().getDeudor().setPrimerApellido("");
								formRecibos.getDatosFinanciacion().getDeudor().setSegundoApellido("");
								formRecibos.getDatosFinanciacion().getDeudor().setPrimerNombre("");
								formRecibos.getDatosFinanciacion().getDeudor().setSegundoNombre("");
								formRecibos.getDatosFinanciacion().getDeudor().setDireccion("");
								formRecibos.getDatosFinanciacion().getDeudor().setTelefono("");
								formRecibos.getDatosFinanciacion().getDeudor().setExiteDeudor(ConstantesBD.acronimoNo);
								formRecibos.getDatosFinanciacion().getDeudor().setExistePerRes(ConstantesBD.acronimoNo);
							}
						}
					}
				}
			}
			
			if(formRecibos.getDatosFinanciacion().getDeudor().getTipoIdentificacion().equals(formRecibos.getDatosFinanciacion().getCodeudor().getTipoIdentificacion())
					&& formRecibos.getDatosFinanciacion().getDeudor().getNumeroIdentificacion().equals(formRecibos.getDatosFinanciacion().getCodeudor().getNumeroIdentificacion())
					&& !formRecibos.getDatosFinanciacion().getCodeudor().getTipoDeudor().equals(""))
					errores.add("descripcion", new ActionMessage("errors.notEspecific","El Deudor debe ser diferente al Codeudor. "));
			
			if(!errores.isEmpty()){
				formRecibos.getDatosFinanciacion().getDeudor().setExiteDeudor(ConstantesBD.acronimoNo);
				formRecibos.getDatosFinanciacion().getDeudor().setExistePerRes(ConstantesBD.acronimoNo);
				saveErrors(request, errores);
			}
		}else{
			if(!formRecibos.getDatosFinanciacion().getDeudor().getTipoDeudor().equals("")
					&& !formRecibos.getDatosFinanciacion().getCodeudor().getTipoDeudor().equals(""))
			{
				errores.add("descripcion", new ActionMessage("errors.notEspecific","El Tipo de Deudor debe ser diferente al Tipo de Codeudor. "));
				formRecibos.getDatosFinanciacion().getDeudor().setTipoDeudor("");
				formRecibos.getDatosFinanciacion().getDeudor().setExiteDeudor(ConstantesBD.acronimoNo);
				formRecibos.getDatosFinanciacion().getDeudor().setExistePerRes(ConstantesBD.acronimoNo);
				saveErrors(request, errores);
			}else{
				formRecibos.getDatosFinanciacion().getDeudor().setCodigoPaciente("");
				formRecibos.getDatosFinanciacion().getDeudor().setIngreso(ConstantesBD.codigoNuncaValido);
				formRecibos.getDatosFinanciacion().getDeudor().setInstitucion(ConstantesBD.codigoNuncaValido);
				//formRecibos.getDatosFinanciacion().getDeudor().setClaseDeudor("");
				formRecibos.getDatosFinanciacion().getDeudor().setTipoIdentificacion(ValoresPorDefecto.getTipoId(Utilidades.convertirAEntero(institucionBasica.getCodigo())));
				formRecibos.getDatosFinanciacion().getDeudor().setNumeroIdentificacion("");
				formRecibos.getDatosFinanciacion().getDeudor().setPrimerApellido("");
				formRecibos.getDatosFinanciacion().getDeudor().setSegundoApellido("");
				formRecibos.getDatosFinanciacion().getDeudor().setPrimerNombre("");
				formRecibos.getDatosFinanciacion().getDeudor().setSegundoNombre("");
				formRecibos.getDatosFinanciacion().getDeudor().setDireccion("");
				formRecibos.getDatosFinanciacion().getDeudor().setTelefono("");
				formRecibos.getDatosFinanciacion().getDeudor().setExiteDeudor(ConstantesBD.acronimoNo);
				formRecibos.getDatosFinanciacion().getDeudor().setExistePerRes(ConstantesBD.acronimoNo);
			}
		}
		return mapping.findForward("nuevoDocGarantia");
	}

	private ActionForward menuPagareLetra(ActionMapping mapping,
			HttpServletRequest request, Connection con,
			RecibosCajaForm formRecibos, InstitucionBasica institucionBasica) {
		ActionErrors errores = new ActionErrors(); 
		HashMap docgarantia = new HashMap();
		String tipDoc = "";
		formRecibos.setSeccionDeudor(ConstantesBD.acronimoNo);
		formRecibos.setSeccionCodeudor(ConstantesBD.acronimoNo);
		//************************************************************
		// verificacion existencia de documento de garantia
		String tipoDoc = "";
		if(Utilidades.convertirAEntero(formRecibos.getMapaFormasPago("tipoDetalle_"+formRecibos.getPosSelFormaPago()).toString())==ConstantesBD.codigoTipoDetalleFormasPagoPagare)
			tipoDoc = ConstantesIntegridadDominio.acronimoTipoDocumentoPagare;
		else
			if(Utilidades.convertirAEntero(formRecibos.getMapaFormasPago("tipoDetalle_"+formRecibos.getPosSelFormaPago()).toString())==ConstantesBD.codigoTipoDetalleFormasPagoLetra)
				tipoDoc = ConstantesIntegridadDominio.acronimoTipoDocumentoLetra;
		logger.info("tipo doc: "+tipoDoc);
		
		docgarantia = DatosFinanciacion.verificarDocumentoGarantia(con, 
				Utilidades.convertirAEntero(formRecibos.getMapaConceptos("docSoporte_0")+""),
				tipoDoc);
		if(docgarantia.containsKey("consecutivo")){
			formRecibos.setIngresoFac(Utilidades.convertirAEntero(docgarantia.get("ingreso").toString())); 
			formRecibos.getDatosFinanciacion().setConsecutivo(docgarantia.get("consecutivo").toString());
			formRecibos.getDatosFinanciacion().setAnioConsecutivo(docgarantia.get("anio_consecutivo").toString());
			formRecibos.getDatosFinanciacion().setTipoDocumento(docgarantia.get("tipo_documento").toString());
			formRecibos.setExistDocGarAso(ConstantesBD.acronimoSi);
			tipDoc=docgarantia.get("tipo_documento").toString().equals(ConstantesIntegridadDominio.acronimoTipoDocumentoLetra)?"Letra":"Pagarï¿½";
			formRecibos.setMessAdver("Para el ingreso asociado a la factura del paciente se tiene registrado un documento de garantï¿½a ("+tipDoc+")");
		}else
			formRecibos.setExistDocGarAso(ConstantesBD.acronimoNo);
		// verificacion existencia de documento de garantia
		//************************************************************
		
		//************************************************************
		// Validacion de los paramatros de maximo nro cuotas y max dias financiacion
		if(Utilidades.convertirAEntero(ValoresPorDefecto.getMaximoNumeroCuotasFinanciacion(Utilidades.convertirAEntero(institucionBasica.getCodigo())))<=0)
			errores.add("datos financiacion", new ActionMessage("errors.notEspecific","Se requiere parametrizar el Mï¿½ximo Nï¿½mero Cuotas de Financiaciï¿½n en Parametros Generales. "));
		if(Utilidades.convertirAEntero(
				ValoresPorDefecto.getMaximoNumeroDiasFinanciacionPorCuota(Utilidades.convertirAEntero(institucionBasica.getCodigo())))<=0)
			errores.add("datos financiacion", new ActionMessage("errors.notEspecific","Se requiere parametrizar el Mï¿½ximo Nï¿½mero Dï¿½as Financiaciï¿½n por Cuota en Parametros Generales. "));
			
		if(!errores.isEmpty()){
			formRecibos.setParanGenVal(ConstantesBD.acronimoNo);
			saveErrors(request, errores);
		}else
			formRecibos.setParanGenVal(ConstantesBD.acronimoSi);
				
		// Fin Validacion de los paramatros de maximo nro cuotas y max dias financiacion
		//************************************************************
		
		if(!formRecibos.getValorFormaPago().equals(formRecibos.getMapaFormasPago("valorFormaPago_"+formRecibos.getPosSelFormaPago())+""))
		{
			formRecibos.setValorFormaPago(formRecibos.getMapaFormasPago("valorFormaPago_"+formRecibos.getPosSelFormaPago())+"");
			formRecibos.getDatosFinanciacion().getCuotasDatosFinan().clear();
			formRecibos.getDatosFinanciacion().setNroCoutas(1);
		}
		
		return mapping.findForward("menuPagareLetra");
	}

	private ActionForward empezarNuevoDocGarantia(ActionMapping mapping,
			Connection con, RecibosCajaForm formRecibos,
			InstitucionBasica institucionBasica) {
		
		if(formRecibos.getDatosFinanciacion().getIsNuevoDoc().equals(ConstantesBD.acronimoNo))
			formRecibos.getDatosFinanciacion().reset();
			
		logger.info("Codigo de la Institucion: "+institucionBasica.getCodigo());
		if(formRecibos.getDatosFinanciacion().getDeudor().getTipoIdentificacion().equals(""))
			formRecibos.getDatosFinanciacion().getDeudor().setTipoIdentificacion(ValoresPorDefecto.getTipoId(Utilidades.convertirAEntero(institucionBasica.getCodigo())));
		if(formRecibos.getDatosFinanciacion().getCodeudor().getTipoIdentificacion().equals(""))
			formRecibos.getDatosFinanciacion().getCodeudor().setTipoIdentificacion(ValoresPorDefecto.getTipoId(Utilidades.convertirAEntero(institucionBasica.getCodigo())));
		if(!formRecibos.getDatosFinanciacion().getDeudor().getTipoDeudor().equals(""))
			formRecibos.setSeccionDeudor(ConstantesBD.acronimoSi);
		if(!formRecibos.getDatosFinanciacion().getCodeudor().getTipoDeudor().equals(""))
			formRecibos.setSeccionCodeudor(ConstantesBD.acronimoSi);
		if(formRecibos.getDatosFinanciacion().getMaxNroCuotasFinan()<=0)
			formRecibos.getDatosFinanciacion().setMaxNroCuotasFinan(Utilidades.convertirAEntero(ValoresPorDefecto.getMaximoNumeroCuotasFinanciacion(Utilidades.convertirAEntero(institucionBasica.getCodigo()))));
		if(formRecibos.getDatosFinanciacion().getMaxNroDiasFinanCuo()<=0)
			formRecibos.getDatosFinanciacion().setMaxNroDiasFinanCuo(Utilidades.convertirAEntero(ValoresPorDefecto.getMaximoNumeroDiasFinanciacionPorCuota(Utilidades.convertirAEntero(institucionBasica.getCodigo()))));
		if(formRecibos.getDatosFinanciacion().getFechaInicio().equals(""))
			formRecibos.getDatosFinanciacion().setFechaInicio(formRecibos.getMapaConceptos("fechaGenFactura_0")+"");
		if(formRecibos.getDatosFinanciacion().getNroCoutas()==ConstantesBD.codigoNuncaValido)
			formRecibos.getDatosFinanciacion().setNroCoutas(1);
		if(formRecibos.getDatosFinanciacion().getDiasPorCuota()==ConstantesBD.codigoNuncaValido)
			formRecibos.getDatosFinanciacion().setDiasPorCuota(30);
		formRecibos.getDatosFinanciacion().setProcesoExito(ConstantesBD.acronimoNo);
		formRecibos.getDatosFinanciacion().setProcesoExitosoCuo(ConstantesBD.acronimoNo);
		formRecibos.getDatosFinanciacion().setIsNuevoDoc(ConstantesBD.acronimoSi);
		formRecibos.setArrayTipIdent(Utilidades.obtenerTiposIdentificacion(con, "ingresoPoliza", Utilidades.convertirAEntero(institucionBasica.getCodigo())));
		
		//***************************************************************************
		//verifiar si existe rsponsble de paciente  para el ingreso de la factura
		formRecibos.setExistResPacIngFac(DatosFinanciacion.verificarResponsablePacIngFac(con, Utilidades.convertirAEntero(formRecibos.getMapaConceptos("docSoporte_0")+"")));
		//***************************************************************************
		
		return mapping.findForward("nuevoDocGarantia");
	}

	/**
	 * 
	 * @param mapping
	 * @param con
	 * @param formRecibos
	 * @return
	 */
	private ActionForward ordenarDeudores(ActionMapping mapping,
			Connection con, RecibosCajaForm formRecibos) {
		SortDeudores sort= new SortDeudores();
		sort.setPatronOrdenar(formRecibos.getPatronOrdenarDeudores());
		Collections.sort(formRecibos.getResultBusDeu(), sort);
		if(formRecibos.getResultBusDeu().size()>0 && formRecibos.getResultBusDeu().get(0).getRompimiento().equals(ConstantesBD.acronimoSi)){
			//formRecibos.getRompimientoPac();
			formRecibos.setResultBusDeu(ordenarRompimiento(formRecibos));
		}
		return mapping.findForward("busquedaDeudor");
	}

	/**
	 * ordenar rompimientos
	 * @param formRecibos
	 */
	private ArrayList<DtoDeudor> ordenarRompimiento(RecibosCajaForm formRecibos) {
		Iterator iter = formRecibos.getRompimientoPac().iterator();
		String rompimiento = "";
		ArrayList<DtoDeudor> array = new ArrayList<DtoDeudor>();
		while(iter.hasNext())
		{
			rompimiento = (String) iter.next();
			for(int i=0; i<formRecibos.getResultBusDeu().size();i++)
			{
				if(rompimiento.equals(formRecibos.getResultBusDeu().get(i).getNombreCompletoPac()))
					array.add(formRecibos.getResultBusDeu().get(i));
			}
		}
		return array;
	}
	
	/**
	 * accion para buscar las facturas
	 * @param con
	 * @param formRecibos
	 * @param mundoRecibos
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionBuscarFacturas(	Connection con,	
												RecibosCajaForm formRecibos,	
												RecibosCaja mundoRecibos, 
												ActionMapping mapping, 
												UsuarioBasico usuario)
	{
		mundoRecibos.setInstitucion(usuario.getCodigoInstitucionInt());
		mundoRecibos.setConsecutivoFact(formRecibos.getConsecutivoFact());
		mundoRecibos.setFechaFact(formRecibos.getFechaFact());
		mundoRecibos.setIdentificacionPaciente(formRecibos.getIdentificacionPaciente());
		mundoRecibos.setTipoIdentificacion(formRecibos.getTipoIdentificacion());
		mundoRecibos.setValorConcepto(formRecibos.getValorConcepto());
		formRecibos.setMapaFacturas(mundoRecibos.generarConsultaFacturas(con));
		//limpiar atributos para nueva seleccion
		formRecibos.setCodigoTipoIngresoTesoreria(ConstantesBD.codigoNuncaValido);
		if(!formRecibos.getMapaFacturas().isEmpty())
			if(Utilidades.convertirAEntero(formRecibos.getMapaFacturas("numReg")+"")==1)//si solo es un registro postularlo automaticamente
			{
				formRecibos.setRegSeleccionado(0);//el unico registro se encuentra en el mapa en la posicion 0
				return this.accionFacturaSeleccionada(con,formRecibos,mapping);
			}
		formRecibos.setAbrirPopUp("facturas");								
		
		return mapping.findForward("paginaPrincipal"); 
	}
	
	/**
	 * 
	 * @param con
	 * @param formRecibos
	 * @param mundoRecibos
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionBuscarConvenios(	Connection con,	
													RecibosCajaForm formRecibos,	
													RecibosCaja mundoRecibos, 
													ActionMapping mapping, 
													UsuarioBasico usuario)
	{
		mundoRecibos.setInstitucion(usuario.getCodigoInstitucionInt());
		mundoRecibos.setConvenioFiltro(formRecibos.getConvenioFiltro());
		mundoRecibos.setEmpresaFiltro(formRecibos.getEmpresaFiltro());
		mundoRecibos.setIdentificacionTerceroFiltro(formRecibos.getIdentificacionTerceroFiltro());
		mundoRecibos.setValorConcepto(formRecibos.getValorConcepto());
		mundoRecibos.setCodigoTipoIngresoTesoreria(Utilidades.convertirAEntero(formRecibos.getMapaConceptos().get("codigoTipoConcepto_"+formRecibos.getPosSelConcepto()).toString()));
		
		if(Utilidades.convertirAEntero(formRecibos.getMapaConceptos().get("codigoTipoConcepto_"+formRecibos.getPosSelConcepto()).toString())==ConstantesBD.codigoTipoIngresoTesoreriaAnticipoConvenioOdon)
			formRecibos.setTipoAtencion(ConstantesIntegridadDominio.acronimoTipoAtencionConvenioOdontologico);
			
		formRecibos.setMapaConvenios(mundoRecibos.generarConsultaConvenios(con));
		//limpiar atributos para nueva seleccion
		formRecibos.setCodigoTipoIngresoTesoreria(ConstantesBD.codigoNuncaValido);
		if(!formRecibos.getMapaConvenios().isEmpty())
		if(Utilidades.convertirAEntero(formRecibos.getMapaConvenios("numReg")+"")==1)//si solo es un registro postularlo automaticamente
		{
			formRecibos.setRegSeleccionado(0);//el unico registro se encuentra en el mapa en la posicion 0
			return this.accionConvenioSeleccionado(con, formRecibos, mapping);
		}
		formRecibos.setAbrirPopUp("convenios");								
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaPrincipal"); 
	}
	
	
	
	/**
	 * Mï¿½todo para inicializar los objetos
	 * @param formRecibos
	 * @param mundoRecibos
	 * @param institucion 
	 */
	private void inicializarFuncionalidad(RecibosCajaForm formRecibos,RecibosCaja mundoRecibos,boolean resetNumReciboCaja, int institucion)
	{	
		if( formRecibos.getAplicaVentaTarjeta().equals(ConstantesBD.acronimoNo)){
			
			formRecibos.resetInfoVenta();
		}
		
		formRecibos.reset(resetNumReciboCaja,institucion, null);
		mundoRecibos.reset();
		this.generarRecibosVacios(formRecibos);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * metodo para pasar los datos al mundo para
	 * generar el recibo de caja
	 * @param con
	 * @param forma
	 * @param mundoRecibos
	 * @param mapping
	 * @param usuario
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionGenerarReciboCaja(Connection con, RecibosCajaForm forma, 
			RecibosCaja mundoRecibos, ActionMapping mapping, 
			UsuarioBasico usuario,HttpServletRequest request, HttpServletResponse response) throws SQLException 
	{
		IIngresosServicio servicio = ManejoPacienteServicioFabrica.crearIngresosServicio();
		forma.setIndexConversionOriginal(Utilidades.convertirAEntero(forma.getIndexTipoMoneda().split(ConstantesBD.separadorSplit)[0]));
		ArrayList filtro=new ArrayList();
		ActionErrors error=new ActionErrors();
		int pos=0;
		HashMap mapa=new HashMap();
		this.inicializarPopUps(forma);
		int tipoCaja=Utilidades.obtenerTipoCaja(con,usuario.getConsecutivoCaja());
		DtoInfoIngresoTrasladoAbonoPaciente dtoInfoIngreso = new DtoInfoIngresoTrasladoAbonoPaciente();

		mundoRecibos.setInstitucion(usuario.getCodigoInstitucionInt());
		mundoRecibos.setUsuario(usuario.getLoginUsuario());
		mundoRecibos.setRecibidoDe(forma.getRecibidoDe());
		mundoRecibos.setObservaciones(forma.getObservaciones());	
		mundoRecibos.setCaja(usuario.getConsecutivoCaja()+"");
		boolean inicioTrans=false;
	   
		
		inicioTrans=UtilidadBD.iniciarTransaccion(con);
		String auxCodDeudor = "";
		
		for(int k=0;k<Utilidades.convertirAEntero(forma.getMapaConceptos("numReg")+"");k++)
		{
			if(!(forma.getMapaConceptos("codigo_concepto_"+k)+"").equals(ConstantesBD.codigoNuncaValido+""))
			{
				if(forma.getTipoFiltro().equals(ConstantesIntegridadDominio.acronimoDeudor))
				{
					String aux[];
					auxCodDeudor = forma.getMapaConceptos("numeroBeneficiario_"+k).toString();
					logger.info("codigo deudor >>>> "+auxCodDeudor);
					
					aux = forma.getMapaConceptos("nombreBeneficiario_"+k).toString().split(" - ");
					
					logger.info("aux-->"+forma.getMapaConceptos("nombreBeneficiario_"+k).toString());
					
					mapa.put("numeroBeneficiario_"+k,aux[0].toString());
					mapa.put("nombreBeneficiario_"+k,aux[1].toString());
					aux=mapa.get("numeroBeneficiario_"+k).toString().split(" ");
					if(aux.length>1){
						mapa.put("tipoBeneficiario_"+k,aux[0].toString());
						mapa.put("numeroBeneficiario_"+k,aux[1].toString());
					}else
						mapa.put("tipoBeneficiario_"+k,"");
				}else{
					mapa.put("numeroBeneficiario_"+k,forma.getMapaConceptos("numeroBeneficiario_"+k));
					mapa.put("nombreBeneficiario_"+k,forma.getMapaConceptos("nombreBeneficiario_"+k));
					mapa.put("tipoBeneficiario_"+k,forma.getMapaConceptos("tipoBeneficiario_"+k));
				}
				
				mapa.put("codigoTipoConcepto_"+k,forma.getMapaConceptos("codigoTipoConcepto_"+k));
				/**
				 * Inc 2864
				 * Se corrige el tipo de dato del campo codigo_concepto ya que habia un error al guardar este codigo.
				 * Diana Ruiz.
				 */
				mapa.put("codigo_concepto_"+k,Utilidades.convertirAEntero((String) forma.getMapaConceptos("codigo_concepto_"+k)));
				//mapa.put("codigo_concepto_"+k,forma.getMapaConceptos("codigo_concepto_"+k).toString());
				if (forma.getLlegoDeFV().equals(ConstantesBD.acronimoSi)) {
					mapa.put("docSoporte_"+k,forma.getMapaConceptos("nroFactura"));
				} else {
					mapa.put("docSoporte_"+k,forma.getMapaConceptos("docSoporte_"+k));
				}
				mapa.put("valorConcepto_"+k,forma.getMapaConceptos("valorConcepto_"+k));
				mapa.put("conceptoFactura_"+k,forma.getMapaConceptos("conceptoFactura_"+k));
				mapa.put("codFacturaVaria_"+k,forma.getMapaConceptos("codFacturaVaria_"+k));
				mundoRecibos.setIngreso((Integer)forma.getMapaConceptos("ingreso_"+k));
				
				if((forma.getMapaConceptos("codigoTipoConcepto_"+k)+"").equals(ConstantesBD.codigoTipoIngresoTesoreriaPacientes+""))
				{
					//bloquear
					filtro.clear();
					filtro.add(forma.getMapaConceptos("codigoFactura_"+k));
					
					if(DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).bloquearRegistroActualizacion(con,BloqueosConcurrencia.bloqueoFacturaDeterminada,filtro))
					{
						mapa.put("codigoFactura_"+k,forma.getMapaConceptos("codigoFactura_"+k));
						mapa.put("saldoPaciente_"+k,forma.getMapaConceptos("saldoPaciente_"+k));
						if(Utilidades.obtenerCodigoEstadoFacturacionFactura(Utilidades.convertirAEntero(forma.getMapaConceptos("codigoFactura_"+k).toString()))!=ConstantesBD.codigoEstadoFacturacionFacturada)
						{
							error.add("Estado Factura Diferente", new ActionMessage("error.factura.estadoFacturacion",Utilidades.obtenerConsecutivoFactura(con,Utilidades.convertirAEntero(forma.getMapaConceptos("codigoFactura_"+k).toString())),"DIFERENTE A FACTURADA"));
						}
						if(Utilidades.obtenerCodigoEstadoPacienteFactura(Utilidades.convertirAEntero(forma.getMapaConceptos("codigoFactura_"+k).toString()))!=ConstantesBD.codigoEstadoFacturacionPacientePorCobrar)
						{
							error.add("Estado Factura Diferente", new ActionMessage("error.factura.estadoPaciente",Utilidades.obtenerConsecutivoFactura(con,Utilidades.convertirAEntero(forma.getMapaConceptos("codigoFactura_"+k).toString())),"DIFERENTE A POR COBRAR"));
						}
					}
				}
				else if((forma.getMapaConceptos("codigoTipoConcepto_"+k)+"").equals(ConstantesBD.codigoTipoIngresoTesoreriaConvenios+""))
					mapa.put("codigoConvenio_"+k,forma.getMapaConceptos("codigoConvenio_"+k));
				else if((forma.getMapaConceptos("codigoTipoConcepto_"+k)+"").equals(ConstantesBD.codigoTipoIngresoTesoreriaAnticipoConvenioOdon+""))
				{
					mapa.put("codigoConvenio_"+k,forma.getMapaConceptos("codigoConvenio_"+k));
					mapa.put("codigoContrato_"+k,forma.getMapaConceptos("codigoContrato_"+k));
				}
				else if((forma.getMapaConceptos("codigoTipoConcepto_"+k)+"").equals(ConstantesBD.codigoTipoIngresoTesoreriaOtrasCxC+""))
				{
					mapa.put("codigoConvenio_"+k,forma.getMapaConceptos("codigoConvenio_"+k));
					if(forma.getTipoFiltro().equals(ConstantesIntegridadDominio.acronimoDeudor))
					{
						String[] codigoDeudor=new String[]{auxCodDeudor};
						if(auxCodDeudor.contains("-"))
						{
							codigoDeudor=auxCodDeudor.split("-");
						}
						mapa.put("codDeudor_"+k,codigoDeudor[0]);
					}
					else
						mapa.put("codDeudor_"+k,forma.getMapaConceptos("codDeudor_"+k));
									
				}
				//else if((forma.getMapaConceptos("codigoTipoConcepto_"+k)+"").equals(ConstantesBD.codigoTipoIngresoTesoreriaAbonos+""))
			 //	{
					mapa.put("codigoPaciente_"+k,forma.getMapaConceptos("codigoPaciente_"+k));
			  //}
				pos=k;
			}
		}
		error=validarTodosSerialesRepetidos(forma);
		if(!error.isEmpty())
		{
			 saveErrors(request, error);
			 UtilidadBD.closeConnection(con);	   			
			 return mapping.findForward("paginaPrincipal");  
		}
		mapa.put("numRegC",(++pos)+"");
		
		for(int k=0;k<Utilidades.convertirAEntero(forma.getMapaFormasPago("numReg")+"")-1;k++)
		{
			mapa.put("codigoFormaPago_"+k,forma.getMapaFormasPago("codigoFormaPago_"+k));
			mapa.put("tipoDetalle_"+k,forma.getMapaFormasPago("tipoDetalle_"+k));
			mapa.put("valorFormaPago_"+k,forma.getMapaFormasPago("valorFormaPago_"+k));
			mapa.put("entidadFormaPago_"+k,forma.getMapaFormasPago("entidadFormaPago_"+k));
			pos=k;
		}	 
		mapa.put("numRegFP",(++pos)+"");
		mapa.put("mapaCheques",forma.getMapaCheques());  
		mapa.put("mapaTarjetas",forma.getMapaTarjetas());

		int numRegFP =Utilidades.convertirAEntero(forma.getMapaFormasPago("numReg")+"");

		mundoRecibos.setMapaRecibosCaja(mapa);
		
		//Se debe evaluar que no exista cierre caja 
		ActionForward validacionesArqueosCierresForward= this.validacionesArqueosCierres(con, request, mapping, usuario.getCodigoInstitucionInt(), UtilidadFecha.getFechaActual(), mundoRecibos.getUsuario(), mundoRecibos.getCaja() );
		
		if(validacionesArqueosCierresForward!=null)
			return validacionesArqueosCierresForward;
		else
		{
			int estadoRC=ConstantesBD.codigoNuncaValido;
			if(tipoCaja==ConstantesBD.codigoTipoCajaMayor)
				estadoRC=ConstantesBD.codigoEstadoReciboCajaEnCierre;
			
			mundoRecibos.setPosSelConcepto(forma.getPosSelConcepto());
			mundoRecibos.setRegSeleccionado(forma.getRegSeleccionado());
			mundoRecibos.setTipoConcepto(Utilidades.convertirAEntero(forma.getMapaConceptos("codigoTipoConcepto_"+forma.getPosSelConcepto()).toString()));
			mundoRecibos.setFacturasVarias(forma.getRecibosCaja());
			mundoRecibos.setUsuario(usuario.getLoginUsuario());
			mundoRecibos.setTipoFiltro(forma.getTipoFiltro());
			mundoRecibos.setCodigoCentroAtencion(usuario.getCodigoCentroAtencion());
			mundoRecibos.setDatosFinanciacion(forma.getDatosFinanciacion());
			
			////////////////////////////////////////////////////
			
			/*
			 * GENERAR RECIBO DE CAJA 
			 */
			int codigoIngreso = ConstantesBD.codigoNuncaValido;
			
			if (dtoInfoIngreso != null) {
				codigoIngreso = dtoInfoIngreso.getIdIngreso();
			}
			
			boolean endTransaction=mundoRecibos.generarReciboCajaTrans(con,inicioTrans,estadoRC, forma.getFormaPagoBonos(), usuario.getCodigoCentroAtencion(), codigoIngreso);

			if(tipoCaja==ConstantesBD.codigoTipoCajaMayor&&UtilidadTexto.getBoolean(ValoresPorDefecto.getInterfazContableRecibosCajaERP(usuario.getCodigoInstitucionInt())))
			{
				try
				{
					UtilidadBDInterfaz.ejecucionInterfazRC(1/* temporal 1 mientras synersis hace el arreglo. usuario.getCodigoInstitucionInt()*/,UtilidadFecha.getFechaActual(),UtilidadFecha.getFechaActual(),ConstantesBD.acronimoInterfazRCTodosRC,ConstantesBD.acronimoNo,forma.getMapaConceptos("descripcion_concepto_0")+"",mundoRecibos.getNumReciboCaja());
				}
				catch(SQLException e)
				{
					logger.info(e.getMessage());
					forma.setMensaje(new ResultadoBoolean(true,"Error pasando el RC a Caja Mayor. <br>"+e.getMessage()));
					
				}
			}
			UtilidadBD.closeConnection(con);
			if(endTransaction)
			{	
				String numeroRCTemp=mundoRecibos.getNumReciboCaja();
				forma.setNumReciboCaja(mundoRecibos.getNumReciboCaja());
				forma.setConsecutivoReciboCaja(mundoRecibos.getConsecutivoReciboCaja());
				//poner aca el consecutivo para el resumen.
				//forma.set
				forma.setObservacionesImprimir(forma.getObservaciones());
				mundoRecibos.reset();
				forma.setEstado("resumen");
				
				//Agregado pro anexo 958
				String llegoDeFV=ConstantesBD.acronimoNo;
				HashMap mapaConceptos=new HashMap();
				if (forma.getLlegoDeFV().equals(ConstantesBD.acronimoSi))
				{
					llegoDeFV=ConstantesBD.acronimoSi;
					mapaConceptos=forma.getMapaConceptos();
				}
				
				String identificacionPaciente = forma.getMapaConceptos().get("numeroBeneficiario_0").toString(); //forma.getIdentificacionPaciente();
				String tipoIdentificacion = forma.getMapaConceptos().get("tipoBeneficiario_0").toString(); //forma.getTipoIdentificacion();
				String actividadAgenda = forma.getActividadAgenda();
					
				this.inicializarFuncionalidad(forma, mundoRecibos,false,usuario.getCodigoInstitucionInt());
			
				//Agrego anexo 958
				//Se hace con el finde poder realizar la impresion de la factura varia generada
				forma.setLlegoDeFV(llegoDeFV);
				forma.setMapaConceptos(mapaConceptos);
				forma.setTipoIdentificacion(tipoIdentificacion);
				forma.setIdentificacionPaciente(identificacionPaciente);
				forma.setActividadAgenda(actividadAgenda);
				
				UtilidadBD.closeConnection(con);
				
				//si es el flujo de generacion de la factura entonces enviar al resumen de factura
				if(forma.getEsWorflowFactura().equals("true"))
				{
					try 
					{
						response.sendRedirect("../facturacion/facturacion.do?estado=resumen&numeroReciboCajaGenerado="+numeroRCTemp+"&consecutivoReciboCajaGenerado="+forma.getConsecutivoReciboCaja());
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
					return null;
				}
				return mapping.findForward("paginaPrincipal");   
			}	
			else
			{
				logger.warn("Error generando el Recibo de Caja"); 
				ActionErrors errores = new ActionErrors();
				errores.add("generar Recibo", new ActionMessage("prompt.noSeGraboInformacion"));
				logger.warn("No se guardo informaciï¿½n");
				saveErrors(request, errores);			
				return mapping.findForward("paginaPrincipal");
			}
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	/**
//	 * @param con
//	 * @param formRecibos
//	 * @param usuario
//	 * @param mapping
//	 * @return
//	 */
//	private ActionForward validarCaja(Connection con, UsuarioBasico usuario, ActionMapping mapping,HttpServletRequest request) 
//	{		
//		if(usuario.getCodigoCaja()==ConstantesBD.codigoNuncaValido)
//		{
//			UtilidadBD.closeConnection(con);
//			
//			if(Utilidades.numCajasAsociadasUsuarioXcentroAtencion(usuario.getLoginUsuario(),usuario.getCodigoCentroAtencion()+"")>0)
//			{
//				request.setAttribute("ruta","recibosCaja/recibosCaja.do?estado=empezar");
//				return mapping.findForward("cargarCajaUsuario");
//			}
//			else
//			{
//				request.setAttribute("codigoDescripcionError", "errors.faltaDefinirCajaParaUsuario");
//				return mapping.findForward("paginaError");
//			}
//		}
//
//		return null;
//	}

	/**
	 * @param con
	 * @param formRecibos
	 * @return
	 */
	private ActionForward accionSeleccionRegistroEditar(Connection con, RecibosCajaForm formRecibos,ActionMapping mapping) 
	{		  
		if(formRecibos.getTipoDePoUp().equals("cheque"))
		{
			formRecibos.setAbrirPopUp("formaPago");   
			formRecibos.setCodigoTipoDetalleFormaPago(ConstantesBD.codigoTipoDetalleFormasPagoCheque);   
		}
		else if(formRecibos.getTipoDePoUp().equals("tarjeta"))
		{
			formRecibos.setAbrirPopUp("formaPago");   
			formRecibos.setCodigoTipoDetalleFormaPago(ConstantesBD.codigoTipoDetalleFormasPagoTarjeta); 
		}else if(formRecibos.getTipoDePoUp().equals("pagareletra"))
		{
			formRecibos.setAbrirPopUp("formaPago");   
			logger.info("codigo detalle forma pago: "+formRecibos.getMapaFormasPago("tipoDetalle_"+formRecibos.getPosSelFormaPago()));
			formRecibos.setCodigoTipoDetalleFormaPago(Utilidades.convertirAEntero(formRecibos.getMapaFormasPago("tipoDetalle_"+formRecibos.getPosSelFormaPago())+"")); 
		}
		else if(formRecibos.getTipoDePoUp().equals("bono"))
		{
			formRecibos.setAbrirPopUp("formaPago");
			if(formRecibos.getPosSelFormaPago()<formRecibos.getFormaPagoBonos().size())
			{
				for(int i=formRecibos.getFormaPagoBonos().size(); i<formRecibos.getPosSelFormaPago(); i++)
				{
					formRecibos.getFormaPagoBonos().add(new DtoDetallePagosBonos());
				}
			}
			
			formRecibos.setCodigoTipoDetalleFormaPago(ConstantesBD.codigoTipoDetalleFormasPagoBono); 
		}
		
		UtilidadBD.closeConnection(con);
  	   	return mapping.findForward("paginaPrincipal"); 
	}

	/**
	 * metodo para realizar la asignacion
	 * de valores y realizar los procesos respectivos
	 * cuando se selecciona un paciente
	 * @param con
	 * @param formRecibos
	 * @param mapping
	 * @return
	 */
	private ActionForward accionPacienteSeleccionado(Connection con, RecibosCajaForm formRecibos, ActionMapping mapping) 
	{
		this.inicializarPopUps(formRecibos);	   
		int concepto=formRecibos.getPosSelConcepto();
		int paciente=formRecibos.getRegSeleccionado();
		String nombre=formRecibos.getMapaPacientes("primerNombre_"+paciente)+" "+formRecibos.getMapaPacientes("segundoNombre_"+paciente)+
		" "+formRecibos.getMapaPacientes("primerApellido_"+paciente)+" "+formRecibos.getMapaPacientes("segundoApellido_"+paciente);
		
		formRecibos.setMapaConceptos("valorConcepto_"+concepto,"");
		formRecibos.setMapaConceptos("readOnlyValoConcepto_"+formRecibos.getPosSelConcepto(),"false");
		formRecibos.setMapaConceptos("nombreBeneficiario_"+concepto,nombre+"");
		formRecibos.setMapaConceptos("numeroBeneficiario_"+concepto,formRecibos.getMapaPacientes("numeroIdentificacion_"+paciente));
		formRecibos.setMapaConceptos("ingreso_"+concepto,formRecibos.getMapaPacientes("ingreso_"+paciente));
		formRecibos.setMapaConceptos("tipoBeneficiario_"+concepto,formRecibos.getMapaPacientes("tipoIdentificacacion_"+paciente));		
		formRecibos.setMapaConceptos("codigoPaciente_"+concepto,formRecibos.getMapaPacientes("codigoPaciente_"+paciente));
		formRecibos.setMapaConceptos("poseeRegistroSeleccionado_"+concepto,"true");
		
		formRecibos.setIdentificacionPaciente(formRecibos.getMapaPacientes("numeroIdentificacion_"+paciente)+"");
		formRecibos.setTipoIdentificacion(formRecibos.getMapaPacientes("tipoIdentificacacion_"+paciente)+"");
		
		
//		"&tipoid="+ formRecibos.getTipoIdentificacion()+
//		"&nroid="+formRecibos.getIdentificacionPaciente()+
		
		UtilidadBD.closeConnection(con);
  	   	return mapping.findForward("paginaPrincipal");
	}
	/**
	 * metodo para realizar la asignacion
	 * de valores y realizar los procesos respectivos
	 * cuando se selecciona un convenio
	 * @param con
	 * @param formRecibos
	 * @param mapping
	 * @return
	 */
	private ActionForward accionConvenioSeleccionado(Connection con, RecibosCajaForm formRecibos, ActionMapping mapping) 
	{
		this.inicializarPopUps(formRecibos);	   
		int copcepto=formRecibos.getPosSelConcepto();
		int convenio=formRecibos.getRegSeleccionado();
		formRecibos.setMapaConceptos("valorConcepto_"+copcepto,"");
		formRecibos.setMapaConceptos("readOnlyValoConcepto_"+formRecibos.getPosSelConcepto(),"false");
		formRecibos.setMapaConceptos("nombreBeneficiario_"+copcepto,formRecibos.getMapaConvenios("nombreEmpresa_"+convenio));
		formRecibos.setMapaConceptos("numeroBeneficiario_"+copcepto,formRecibos.getMapaConvenios("identificacion_"+convenio));
		formRecibos.setMapaConceptos("tipoBeneficiario_"+copcepto,formRecibos.getMapaConvenios("tipoIdentificacion_"+convenio));
		formRecibos.setMapaConceptos("codigoConvenio_"+copcepto,formRecibos.getMapaConvenios("codigoConvenio_"+convenio));
		//****************** Anexo 785 *************************
		if(formRecibos.getTipoAtencion().equals(ConstantesIntegridadDominio.acronimoTipoAtencionConvenioOdontologico))
			formRecibos.setMapaConceptos("codigoContrato_"+copcepto,formRecibos.getMapaConvenios("codigoContrato_"+convenio));
		//****************** Anexo 785 *************************
		formRecibos.setMapaConceptos("poseeRegistroSeleccionado_"+copcepto,"true");
		
		UtilidadBD.closeConnection(con);
  	   	return mapping.findForward("paginaPrincipal"); 
	}
	/**
	 * metodo para realizar la asignacion
	 * de valores y realizar los procesos respectivos
	 * cuando se selecciona una factura
	 * @param con
	 * @param formRecibos
	 * @param mapping
	 * @return
	 */
	private ActionForward accionFacturaSeleccionada(Connection con, RecibosCajaForm formRecibos, ActionMapping mapping) 
	{
		this.inicializarPopUps(formRecibos);	   
		int copcepto=formRecibos.getPosSelConcepto();
		int factura=formRecibos.getRegSeleccionado();	   
		formRecibos.setMapaConceptos("readOnlyValoConcepto_"+formRecibos.getPosSelConcepto(),"false");
		formRecibos.setMapaConceptos("docSoporte_"+copcepto,formRecibos.getMapaFacturas("consecutivo_factura_"+factura));
		formRecibos.setMapaConceptos("nombreBeneficiario_"+copcepto,formRecibos.getMapaFacturas("nombre_paciente_"+factura));
		formRecibos.setMapaConceptos("numeroBeneficiario_"+copcepto,formRecibos.getMapaFacturas("numeroIdentificacion_"+factura));
		formRecibos.setMapaConceptos("tipoBeneficiario_"+copcepto,formRecibos.getMapaFacturas("tipoIdentificacion_"+factura));		
		formRecibos.setMapaConceptos("valorConcepto_"+copcepto,formRecibos.getMapaFacturas("saldoPaciente_"+factura));
		formRecibos.setMapaConceptos("saldoPaciente_"+copcepto,formRecibos.getMapaFacturas("saldoPaciente_"+factura));
		formRecibos.setMapaConceptos("codigoFactura_"+copcepto,formRecibos.getMapaFacturas("codigo_factura_"+factura));
		formRecibos.setMapaConceptos("fechaGenFactura_"+copcepto,formRecibos.getMapaFacturas("fecha_"+factura));
		formRecibos.setMapaConceptos("poseeRegistroSeleccionado_"+copcepto,"true");
		formRecibos.setMapaConceptos("codigoPaciente_"+copcepto,formRecibos.getMapaFacturas("codigo_paciente_"+factura));
		//para validaciones
		formRecibos.setValoresConceptos("valorConcepto_"+copcepto,formRecibos.getMapaFacturas("saldoPaciente_"+factura));
		
		UtilidadBD.closeConnection(con);
  	   	return mapping.findForward("paginaPrincipal"); 
	}
	/**
	 * metodo para ordenar el mapa de pacientes
	 * @param con
	 * @param formRecibos
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenarColumnaPacientes(Connection con, RecibosCajaForm formRecibos, ActionMapping mapping) 
	{
		int numRegTemp=0;
		formRecibos.setAbrirPopUp("pacientes");	   
		numRegTemp=Utilidades.convertirAEntero(formRecibos.getMapaPacientes("numReg")+"");		
		String[] indices={
 							"primerNombre_", 
 							"segundoNombre_", 
 							"primerApellido_", 
 							"segundoApellido_",
 							"tipoIdentificacacion_",
 							"numeroIdentificacion_"
 						};
			
		formRecibos.setMapaPacientes(Listado.ordenarMapa(indices,
 													   formRecibos.getPatronOrdenar(),
 													   formRecibos.getUltimoPatron(),
 													   formRecibos.getMapaPacientes(),
													   Utilidades.convertirAEntero(formRecibos.getMapaPacientes("numReg")+"")));
		formRecibos.setMapaPacientes("numReg",numRegTemp+"");
		formRecibos.setUltimoPatron(formRecibos.getPatronOrdenar());
		UtilidadBD.closeConnection(con);
  	   	return mapping.findForward("paginaPrincipal");		 
	}
	/**
	 * metodo para ordenar el mapa de convenios
	 * @param con
	 * @param formRecibos
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenarColumnaConvenios(Connection con, RecibosCajaForm formRecibos, ActionMapping mapping) 
	{
	   
		int numRegTemp=0;
		formRecibos.setAbrirPopUp("concepto");
		formRecibos.setCodigoTipoIngresoTesoreria(ConstantesBD.codigoTipoIngresoTesoreriaConvenios);
		numRegTemp=Utilidades.convertirAEntero(formRecibos.getMapaConvenios("numReg")+"");		
		String[] indices={
 							"nombreEmpresa_", 
 							"nombreConvenio_", 
 							"regimen_", 
 							"identificacion_",
 							"tipoIdentificacion_",
 							"codigoConvenio_",
 							"codigoContrato_",
 							"numeroContrato_",
 							"fechaIniContrato_",
 							"fechaFinContrato_"
 						};
			
		formRecibos.setMapaConvenios(Listado.ordenarMapa(indices,
 													   formRecibos.getPatronOrdenar(),
 													   formRecibos.getUltimoPatron(),
 													   formRecibos.getMapaConvenios(),
													   Utilidades.convertirAEntero(formRecibos.getMapaConvenios("numReg")+"")));
		formRecibos.setMapaConvenios("numReg",numRegTemp+"");
		formRecibos.setUltimoPatron(formRecibos.getPatronOrdenar());
		UtilidadBD.closeConnection(con);
  	   	return mapping.findForward("paginaPrincipal"); 
	}
	/**
	 * inicializar las variebles encargadas
	 * de abrir los popups
	 * @param formRecibos
	 */
	private void inicializarPopUps(RecibosCajaForm formRecibos)
	{
		formRecibos.setCodigoTipoIngresoTesoreria(ConstantesBD.codigoNuncaValido);
		formRecibos.setCodigoTipoDetalleFormaPago(ConstantesBD.codigoNuncaValido);
		formRecibos.setAbrirPopUp("");		
	}
	/**
	 * metodo para eliminar un registro del mapa de conceptos
	 * @param con
	 * @param formRecibos
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEliminarRegistroConcepto(Connection con, RecibosCajaForm formRecibos, ActionMapping mapping) 
	{
		int esUltimo=Utilidades.convertirAEntero(formRecibos.getMapaConceptos("numReg")+"")-1;
		int posDel=formRecibos.getPosEliminarC(),nuevaPos=ConstantesBD.codigoNuncaValido;
		this.inicializarPopUps(formRecibos);
		if(posDel!=ConstantesBD.codigoNuncaValido)
		{			
			formRecibos.getMapaConceptos().remove("codigo_concepto_"+posDel);
			formRecibos.getMapaConceptos().remove("descripcion_concepto_"+posDel);
			formRecibos.getMapaConceptos().remove("codigoTipoConcepto_"+posDel);
			formRecibos.getMapaConceptos().remove("valorConcepto_"+posDel);
			formRecibos.getMapaConceptos().remove("docSoporte_"+posDel);
			formRecibos.getMapaConceptos().remove("nombreBeneficiario_"+posDel);
			formRecibos.getMapaConceptos().remove("tipoBeneficiario_"+posDel);
			formRecibos.getMapaConceptos().remove("numeroBeneficiario_"+posDel);
			formRecibos.getMapaConceptos().remove("valor_"+posDel);
			formRecibos.getMapaConceptos().remove("poseeRegistroSeleccionado_"+posDel);
			formRecibos.getMapaConceptos().remove("esEditable_"+posDel);
			formRecibos.getMapaConceptos().remove("yaGeneroNuevoRegistro_"+posDel);
			nuevaPos=Utilidades.convertirAEntero(formRecibos.getMapaConceptos("numReg")+"")-1;
			formRecibos.setMapaConceptos("numReg",nuevaPos+"");
	   
			if(posDel != esUltimo)//si no es el ultimo registro, se organiza el HashMap
			{ 
				for(int k=posDel;k<Utilidades.convertirAEntero(formRecibos.getMapaConceptos("numReg")+"");k++)
				{
					formRecibos.setMapaConceptos("codigo_concepto_"+k,formRecibos.getMapaConceptos("codigo_concepto_"+(k+1)));	
					formRecibos.setMapaConceptos("descripcion_concepto_"+k,formRecibos.getMapaConceptos("descripcion_concepto_"+(k+1)));
					formRecibos.setMapaConceptos("codigoTipoConcepto_"+k,formRecibos.getMapaConceptos("codigoTipoConcepto_"+(k+1)));
					formRecibos.setMapaConceptos("valorConcepto_"+k,formRecibos.getMapaConceptos("valorConcepto_"+(k+1)));
					formRecibos.setMapaConceptos("docSoporte_"+k,formRecibos.getMapaConceptos("docSoporte_"+(k+1)));
					formRecibos.setMapaConceptos("nombreBeneficiario_"+k,formRecibos.getMapaConceptos("nombreBeneficiario_"+(k+1)));
					formRecibos.setMapaConceptos("tipoBeneficiario_"+k,formRecibos.getMapaConceptos("tipoBeneficiario_"+(k+1)));
					formRecibos.setMapaConceptos("numeroBeneficiario_"+k,formRecibos.getMapaConceptos("numeroBeneficiario_"+(k+1)));
					formRecibos.setMapaConceptos("valor_"+k,formRecibos.getMapaConceptos("valor_"+(k+1)));
					formRecibos.setMapaConceptos("poseeRegistroSeleccionado_"+k,formRecibos.getMapaConceptos("poseeRegistroSeleccionado_"+(k+1)));
					formRecibos.setMapaConceptos("esEditable_"+k,formRecibos.getMapaConceptos("esEditable_"+(k+1)));
					formRecibos.setMapaConceptos("yaGeneroNuevoRegistro_"+k,formRecibos.getMapaConceptos("yaGeneroNuevoRegistro_"+(k+1)));
				}
			}
		}
		UtilidadBD.closeConnection(con);
  	   	return mapping.findForward("paginaPrincipal");	   
	}
	/**
	 * metodo para eliminar un registro del 
	 * mapa de formas de pago
	 * @param con
	 * @param formRecibos
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEliminarRegistroFormaPago(Connection con, RecibosCajaForm formRecibos, ActionMapping mapping) 
	{
		int esUltimo=Utilidades.convertirAEntero(formRecibos.getMapaFormasPago("numReg")+"")-1;
		int posDel=formRecibos.getPosEliminarC(),nuevaPos=ConstantesBD.codigoNuncaValido;
		this.inicializarPopUps(formRecibos); 
		/**si se elimina forma de pago ninguno(efectivo), se quita la restricciï¿½n,
		 * de solo adicionar una sola forma de pago en efectivo, para volverla adicionar
		 * si es del caso*/
		/*if(Utilidades.convertirAEntero(formRecibos.getMapaFormasPago("tipoDetalle_"+posDel)+"")==ConstantesBD.codigoTipoDetalleFormasPagoNinguno)
			formRecibos.setMapaFormasPago("existeRegistroEfectivo","false");*/
		if(posDel!=ConstantesBD.codigoNuncaValido)
		{			
			formRecibos.getMapaFormasPago().remove("yaGeneroNuevoRegistro_"+posDel);
			formRecibos.getMapaFormasPago().remove("nombreFormaPago_"+posDel);
			formRecibos.getMapaFormasPago().remove("codigoFormaPago_"+posDel);			
			formRecibos.getMapaFormasPago().remove("entidadFormaPago_"+posDel);
			formRecibos.getMapaFormasPago().remove("valorFormaPago_"+posDel);
			formRecibos.getMapaFormasPago().remove("tipoDetalle_"+posDel);
			formRecibos.getMapaFormasPago().remove("cheque_"+posDel);
			formRecibos.getMapaFormasPago().remove("esEditable_"+posDel);			
			nuevaPos=Utilidades.convertirAEntero(formRecibos.getMapaFormasPago("numReg")+"")-1;
			formRecibos.setMapaFormasPago("numReg",nuevaPos+"");
	   
			if(posDel != esUltimo)//si no es el ultimo registro, se organiza el HashMap
			{ 
				for(int k=posDel;k<Utilidades.convertirAEntero(formRecibos.getMapaFormasPago("numReg")+"");k++)
				{
					formRecibos.setMapaFormasPago("yaGeneroNuevoRegistro_"+k,formRecibos.getMapaFormasPago("yaGeneroNuevoRegistro_"+(k+1)));
					formRecibos.setMapaFormasPago("nombreFormaPago_"+k,formRecibos.getMapaFormasPago("nombreFormaPago_"+(k+1)));
					formRecibos.setMapaFormasPago("codigoFormaPago_"+k,formRecibos.getMapaFormasPago("codigoFormaPago_"+(k+1)));
					formRecibos.setMapaFormasPago("entidadFormaPago_"+k,formRecibos.getMapaFormasPago("entidadFormaPago_"+(k+1)));
					formRecibos.setMapaFormasPago("valorFormaPago_"+k,formRecibos.getMapaFormasPago("valorFormaPago_"+(k+1)));
					formRecibos.setMapaFormasPago("tipoDetalle_"+k,formRecibos.getMapaFormasPago("tipoDetalle_"+(k+1)));
					formRecibos.setMapaFormasPago("cheque_"+k,formRecibos.getMapaFormasPago("cheque_"+(k+1)));
					formRecibos.setMapaFormasPago("esEditable_"+k,formRecibos.getMapaFormasPago("esEditable_"+(k+1)));
				}
			}
		}
		UtilidadBD.closeConnection(con);
  	   	return mapping.findForward("paginaPrincipal");  	  
	}
	/**
	 * Metodo para ordenar el mapa de facturas
	 * @param con
	 * @param formRecibos
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenarColumnaFactura(Connection con, RecibosCajaForm formRecibos, ActionMapping mapping) 
	{
	   int numRegTemp=0;
	   formRecibos.setAbrirPopUp("facturas");
	   numRegTemp=Utilidades.convertirAEntero(formRecibos.getMapaFacturas("numReg")+"");		
	   String[] indices={
							"nombre_via_ingreso_", 
							"consecutivo_factura_", 
							"fecha_", 
							"nombre_paciente_",
							"fecha_elaboracion_",
							"valor_neto_paciente_",
							"saldoPaciente_",
							"codigo_factura_",
							"numeroIdentificacion_",
							"tipoIdentificacion_",
							"nombre_centro_atencion_",
							"pagosPaciente_"
						};
	   
	   formRecibos.setMapaFacturas(Listado.ordenarMapa(indices,
													   formRecibos.getPatronOrdenar(),
													   formRecibos.getUltimoPatron(),
													   formRecibos.getMapaFacturas(),
		   											   Utilidades.convertirAEntero(formRecibos.getMapaFacturas("numReg")+"")));
	   formRecibos.setMapaFacturas("numReg",numRegTemp+"");
	   formRecibos.setUltimoPatron(formRecibos.getPatronOrdenar());
	   UtilidadBD.closeConnection(con);
 	   return mapping.findForward("paginaPrincipal"); 
	}
	/**
	 * @param con
	 * @param formRecibos
	 * @param mapping
	 * @return
	 */
	private ActionForward accionConcepto(Connection con, RecibosCajaForm formRecibos, ActionMapping mapping,RecibosCaja mundoRecibos,UsuarioBasico usuario) 
	{
		String concepto="";
		int codTipoIngreso=ConstantesBD.codigoNuncaValido;
		this.inicializarPopUps(formRecibos);
		mundoRecibos.setInstitucion(usuario.getCodigoInstitucionInt());
		mundoRecibos.setCodConcepto(formRecibos.getMapaConceptos("codigo_concepto_"+formRecibos.getPosSelConcepto())+"");
		this.accionNuevoRegistroConcepto(formRecibos);
		String array=mundoRecibos.generarConsultaAjustes(con);//se consulta el tipo de concepto y el valor, para conocer el caso de concepto que se aplica
		String[] tipoConcepto={};//almacena el tipo de concepto y el valor, donde el tipo es ninguno, paciente, etc; y el valor si es por paciente por ejemplo la via de ingreso(el filtro)
		if(!array.equals(""))
		{
			tipoConcepto=array.split("@");//pos [0] el tipo de concepto y en la [1] el valor
			formRecibos.setMapaConceptos("codigoTipoConcepto_"+formRecibos.getPosSelConcepto(),tipoConcepto[0]+"");
			if(tipoConcepto[0].equals(ConstantesBD.codigoTipoIngresoTesoreriaNinguno+""))
			{
				concepto=""; 
				codTipoIngreso=ConstantesBD.codigoTipoIngresoTesoreriaNinguno;
				formRecibos.setMapaConceptos("mostrarCampos_"+formRecibos.getPosSelConcepto(),"true");//para visualizar los campos en la forma
			   
			}
			else if(tipoConcepto[0].equals(ConstantesBD.codigoTipoIngresoTesoreriaPacientes+"")) 
		   	{   // Pago Pacientes
				concepto="concepto"; 
				codTipoIngreso=ConstantesBD.codigoTipoIngresoTesoreriaPacientes;
				formRecibos.setValorConcepto(tipoConcepto[1]);
				formRecibos.setMapaConceptos("esEditable_"+formRecibos.getPosSelConcepto(),"true");//si el registro abre popOut, para ser modificado
				formRecibos.setMapaConceptos("mostrarCampos_"+formRecibos.getPosSelConcepto(),"true");//para visualizar los campos en la forma
		   	}
			else if(tipoConcepto[0].equals(ConstantesBD.codigoTipoIngresoTesoreriaConvenios+""))
			{
				concepto="concepto"; 
				formRecibos.setValorConcepto(tipoConcepto[1]);
				codTipoIngreso=ConstantesBD.codigoTipoIngresoTesoreriaConvenios;
				formRecibos.setMapaConceptos("esEditable_"+formRecibos.getPosSelConcepto(),"true");//si el registro abre popOut, para ser modificado
				//@todo comentariado
				//formRecibos.setMapaConvenios(mundoRecibos.generarConsultaConvenios(con));
				formRecibos.setMapaConceptos("mostrarCampos_"+formRecibos.getPosSelConcepto(),"true");//para visualizar los campos en la forma
			}
			else if(tipoConcepto[0].equals(ConstantesBD.codigoTipoIngresoTesoreriaOtrasCxC+""))
			{
				concepto="concepto"; 
				formRecibos.setValorConcepto(tipoConcepto[1]);
				codTipoIngreso=ConstantesBD.codigoTipoIngresoTesoreriaOtrasCxC;
				formRecibos.setMapaConceptos("mostrarCampos_"+formRecibos.getPosSelConcepto(),"true");//para visualizar los campos en la forma
			   
				//cambio para la tarea 2195
				formRecibos.setMapaConceptos("esEditable_"+formRecibos.getPosSelConcepto(),"true");//si el registro abre popOut, para ser modificado
				//Cambio por la Tarea 2248. Se resetean las llaves (nombreBeneficiario_, numeroBeneficiario_, tipoBeneficiario_)
				// cambio anexo 791 - manejo de multas citas incumplidas
			   
				if(mundoRecibos.getValorConceptoIngresoTesoreria(con, formRecibos.getMapaConceptos().get("codigo_concepto_"+formRecibos.getPosSelConcepto()).toString()).equals(ConstantesIntegridadDominio.acronimoFactura))
				{
					formRecibos.resetBusq();
					formRecibos.setMapaConceptos("readOnlyValoConcepto_"+formRecibos.getPosSelConcepto(),"true");
					Multas multas = new Multas();
					formRecibos.setConceptoFVarias(multas.getArrayConcepFactVarias(con, usuario));
					formRecibos.setTipoFiltro(ConstantesIntegridadDominio.acronimoFactura);
				}else{
					formRecibos.setTipoFiltro(ConstantesIntegridadDominio.acronimoDeudor);
				}
			}
			else if(tipoConcepto[0].equals(ConstantesBD.codigoTipoIngresoTesoreriaCarteraParticular+""))
			{	// Cartera Paciente o Particular
				concepto="concepto"; 
				formRecibos.setValorConcepto(tipoConcepto[1]);
				codTipoIngreso=ConstantesBD.codigoTipoIngresoTesoreriaCarteraParticular;
				formRecibos.setMapaConceptos("esEditable_"+formRecibos.getPosSelConcepto(),"true");//si el registro abre popOut, para ser modificado
				formRecibos.setMapaConceptos("mostrarCampos_"+formRecibos.getPosSelConcepto(),"true");//para visualizar los campos en la forma
			}
			else if(tipoConcepto[0].equals(ConstantesBD.codigoTipoIngresoTesoreriaAbonos+""))
			{
				concepto="concepto"; 
				formRecibos.setValorConcepto(tipoConcepto[1]);
				codTipoIngreso=ConstantesBD.codigoTipoIngresoTesoreriaAbonos;
				formRecibos.setMapaConceptos("esEditable_"+formRecibos.getPosSelConcepto(),"true");//si el registro abre popOut, para ser modificado
				formRecibos.setMapaConceptos("mostrarCampos_"+formRecibos.getPosSelConcepto(),"true");//para visualizar los campos en la forma
			}
			else if(tipoConcepto[0].equals(ConstantesBD.codigoTipoIngresoTesoreriaAnticipoConvenioOdon+""))
			{// Concepto Anticipos Convenios Odontologia 
				concepto="concepto"; 
				formRecibos.setValorConcepto(tipoConcepto[1]);
				codTipoIngreso=ConstantesBD.codigoTipoIngresoTesoreriaAnticipoConvenioOdon;
				formRecibos.setMapaConceptos("esEditable_"+formRecibos.getPosSelConcepto(),"true");//si el registro abre popOut, para ser modificado
				formRecibos.setMapaConceptos("mostrarCampos_"+formRecibos.getPosSelConcepto(),"true");//para visualizar los campos en la forma
			}else
			{
				logger.warn("TIPO DE CONCEPTO NO VALIDO PARA RECIBOS DE CAJA");  
			}		   
		}
	   
		if(tipoConcepto.length>0 && (!tipoConcepto[0].equals(ConstantesBD.codigoTipoIngresoTesoreriaPacientes+""))
			 &&  Utilidades.convertirAEntero(formRecibos.getMapaFormasPago("numReg")+"")>0){
		   accionEliminarRegistroFormaPagoPL(con, formRecibos, mapping);
		}
   	   		
	   
		UtilidadBD.closeConnection(con);
		formRecibos.setAbrirPopUp(concepto);
		formRecibos.setCodigoTipoIngresoTesoreria(codTipoIngreso);
		return mapping.findForward("paginaPrincipal");
	}
	/**
	 * @param con
	 * @param formRecibos
	 * @param mapping
	 * @param mundoRecibos
	 * @param usuario
	 * @return
	 */
	private ActionForward accionFormaPago(Connection con, RecibosCajaForm formRecibos, 
			ActionMapping mapping, RecibosCaja mundoRecibos, HttpServletRequest request) 
	{	   
		int tipoDetalle=mundoRecibos.generarConsultaTipoDetalleFormaPago(con,Utilidades.convertirAEntero(formRecibos.getMapaFormasPago("codigoFormaPago_"+formRecibos.getPosSelFormaPago())+""));
		if(tipoDetalle!=ConstantesBD.codigoTipoDetalleFormasPagoNinguno)//solo se debe generar un solo registro que tenga definido tipo de detalle (ninguno) en la parametrizaciï¿½n
		{
			this.accionNuevoRegisroFormaPago(formRecibos);
		}
		if(tipoDetalle!=ConstantesBD.codigoNuncaValido)
		{
			
			
			//************************************************************************************************* 
			// Anexo 762: Validacion de Formas de Pago
			ActionErrors errores = validacionFormaPagoPL(formRecibos,tipoDetalle);
			//*************************************************************************************************
			if(errores.isEmpty())
			{
				formRecibos.setMapaFormasPago("tipoDetalle_"+formRecibos.getPosSelFormaPago(),tipoDetalle+"");
				if(tipoDetalle==ConstantesBD.codigoTipoDetalleFormasPagoNinguno)
				{
					formRecibos.setAbrirPopUp("");   
					formRecibos.setCodigoTipoDetalleFormaPago(tipoDetalle);
					/*if((formRecibos.getMapaFormasPago("existeRegistroEfectivo")+"").equals("false"))
					{*/
						//formRecibos.setMapaFormasPago("existeRegistroEfectivo","true");
						formRecibos.setMapaFormasPago("entidadFormaPago_"+formRecibos.getPosSelFormaPago(),formRecibos.getRecibidoDe()+"");
						formRecibos.setMapaFormasPago("mostrarCampos_"+formRecibos.getPosSelFormaPago(),"true");//para visualizar los campos en la forma
						this.accionNuevoRegisroFormaPago(formRecibos);
					/*}
					else
					{
						logger.warn("YA SE GENERO FORMA DE PAGO CON TIPO DETALLE NINGUNO");			   	 
						formRecibos.setMapaFormasPago("nombreFormaPago_"+formRecibos.getPosSelFormaPago(),"Seleccione");
						formRecibos.setMapaFormasPago("codigoFormaPago_"+formRecibos.getPosSelFormaPago(),"-1");
					}*/
				}
				else if(tipoDetalle==ConstantesBD.codigoTipoDetalleFormasPagoCheque)
				{
					formRecibos.setAbrirPopUp("formaPago");   
					formRecibos.setCodigoTipoDetalleFormaPago(tipoDetalle);
					formRecibos.setMapaFormasPago("esEditable_"+formRecibos.getPosSelFormaPago(),"true");
					formRecibos.setMapaFormasPago("mostrarCampos_"+formRecibos.getPosSelFormaPago(),"true");//para visualizar los campos en la forma
					this.generarObjetoCheque(formRecibos);
				}
				else if(tipoDetalle==ConstantesBD.codigoTipoDetalleFormasPagoTarjeta)
				{
					formRecibos.setAbrirPopUp("formaPago");   
					formRecibos.setCodigoTipoDetalleFormaPago(tipoDetalle);
					formRecibos.setMapaFormasPago("esEditable_"+formRecibos.getPosSelFormaPago(),"true");
					formRecibos.setMapaFormasPago("mostrarCampos_"+formRecibos.getPosSelFormaPago(),"true");//para visualizar los campos en la forma
					this.generarobjetoTarjeta(formRecibos);
				}
				else if(tipoDetalle==ConstantesBD.codigoTipoDetalleFormasPagoPagare)
				{
					formRecibos.setAbrirPopUp("formaPago");
					formRecibos.setCodigoTipoDetalleFormaPago(tipoDetalle);
					formRecibos.setMapaFormasPago("esEditable_"+formRecibos.getPosSelFormaPago(),"true");
					formRecibos.setMapaFormasPago("mostrarCampos_"+formRecibos.getPosSelFormaPago(),"true");
					formRecibos.setMapaFormasPago("entidadFormaPago_"+formRecibos.getPosSelFormaPago(), formRecibos.getRecibidoDe());
				}else if(tipoDetalle==ConstantesBD.codigoTipoDetalleFormasPagoLetra)
				{
					formRecibos.setAbrirPopUp("formaPago");
					formRecibos.setCodigoTipoDetalleFormaPago(tipoDetalle);
					formRecibos.setMapaFormasPago("esEditable_"+formRecibos.getPosSelFormaPago(),"true");
					formRecibos.setMapaFormasPago("mostrarCampos_"+formRecibos.getPosSelFormaPago(),"true");
					formRecibos.setMapaFormasPago("entidadFormaPago_"+formRecibos.getPosSelFormaPago(), formRecibos.getRecibidoDe());
				}else if(tipoDetalle==ConstantesBD.codigoTipoDetalleFormasPagoBono)
				{
					formRecibos.setAbrirPopUp("formaPago");
					formRecibos.setCodigoTipoDetalleFormaPago(tipoDetalle);
					formRecibos.setMapaFormasPago("esEditable_"+formRecibos.getPosSelFormaPago(),"true");
					formRecibos.setMapaFormasPago("mostrarCampos_"+formRecibos.getPosSelFormaPago(),"true");
					formRecibos.setMapaFormasPago("mostrarEntidad_"+formRecibos.getPosSelFormaPago(),"false");
					formRecibos.setMapaFormasPago("entidadFormaPago_"+formRecibos.getPosSelFormaPago(), formRecibos.getRecibidoDe());
					
					if(formRecibos.getPosSelFormaPago()>=formRecibos.getFormaPagoBonos().size())
					{
						for(int i=formRecibos.getFormaPagoBonos().size(); i<formRecibos.getPosSelFormaPago()+1; i++)
						{
							formRecibos.getFormaPagoBonos().add(new DtoDetallePagosBonos());
						}
					}
				}
				else
				{
					logger.warn("TIPO DETALLE NO VALIDO PARA RECIBOS DE CAJA");
					formRecibos.setAbrirPopUp("");   
					formRecibos.setCodigoTipoDetalleFormaPago(ConstantesBD.codigoNuncaValido);
					formRecibos.setMapaFormasPago("esEditable_"+formRecibos.getPosSelFormaPago(),"true");
					formRecibos.setMapaFormasPago("mostrarCampos_"+formRecibos.getPosSelFormaPago(),"true");
					formRecibos.setMapaFormasPago("entidadFormaPago_"+formRecibos.getPosSelFormaPago(), formRecibos.getRecibidoDe());
				}
			}else{
				eliminarFormaPagoRep(formRecibos);
				this.inicializarPopUps(formRecibos);
				saveErrors(request, errores);
			}
		}
		UtilidadBD.closeConnection(con);
  	   	return mapping.findForward("paginaPrincipal");		   
	}

	/**
	 * validacion de formas de pago pagarï¿½ y letra
	 * @param formRecibos
	 * @param tipoDetalle
	 * @return
	 */
	private ActionErrors validacionFormaPagoPL(RecibosCajaForm formRecibos,int tipoDetalle) 
	{
		ActionErrors errores = new ActionErrors(); 
		boolean band1 = false, band2 = false;
		//Utilidades.imprimirMapa(formRecibos.getMapaFormasPago());
		for(int i=0; i<Utilidades.convertirAEntero(formRecibos.getMapaFormasPago("numReg")+"")-1;i++)
		{
			for(int j=i+1; j<Utilidades.convertirAEntero(formRecibos.getMapaFormasPago("numReg")+"")-1;j++)
			{
				//logger.info("i:"+i+" j:"+j);
				if(j!=i)
				{
					/*logger.info("---------------------------------------------------------------------------");
					logger.info("Codigo Forma Pago i: "+formRecibos.getMapaFormasPago("codigoFormaPago_"+i));
					logger.info("Codigo Forma Pago j: "+formRecibos.getMapaFormasPago("codigoFormaPago_"+j));
					logger.info("tipo Detalle i:"+formRecibos.getMapaFormasPago("tipoDetalle_"+i));
					logger.info("Tipo Detalle nuevo: "+tipoDetalle);
					logger.info("---------------------------------------------------------------------------");
					*/
					if(formRecibos.getMapaFormasPago("codigoFormaPago_"+i).equals(formRecibos.getMapaFormasPago("codigoFormaPago_"+j)+"")
							&& (Utilidades.convertirAEntero(formRecibos.getMapaFormasPago("tipoDetalle_"+i)+"")==ConstantesBD.codigoTipoDetalleFormasPagoPagare
									|| Utilidades.convertirAEntero(formRecibos.getMapaFormasPago("tipoDetalle_"+i)+"")==ConstantesBD.codigoTipoDetalleFormasPagoLetra))
					{
						if(!band1){
							errores.add("Forma Pago", new ActionMessage("errors.notEspecific", "La Forma de Pago ya se encuentra vinculda. "));
							band1 = true;
						}
						formRecibos.setPosEliminarC(j);
					}else{
						switch (Utilidades.convertirAEntero(formRecibos.getMapaFormasPago("tipoDetalle_"+i)+"")) {
						case ConstantesBD.codigoTipoDetalleFormasPagoPagare:
							if(tipoDetalle==ConstantesBD.codigoTipoDetalleFormasPagoLetra){
								if(!band2){
									errores.add("Forma Pago", new ActionMessage("errors.notEspecific", "La Forma de pago seleccionada no puede asociarse por que ya existe un Pagarï¿½. "));
									band2=true;
								}
								formRecibos.setPosEliminarC(j);
							}else if(tipoDetalle==ConstantesBD.codigoTipoDetalleFormasPagoPagare){
								if(!band2){
									errores.add("Forma Pago", new ActionMessage("errors.notEspecific", "La Forma de pago seleccionada no puede asociarse por que ya existe un Pagarï¿½. "));
									band2=true;
								}
								formRecibos.setPosEliminarC(j);
							}
							break;
						case ConstantesBD.codigoTipoDetalleFormasPagoLetra:
							if(tipoDetalle==ConstantesBD.codigoTipoDetalleFormasPagoPagare){
								if(!band2){
									errores.add("Forma Pago", new ActionMessage("errors.notEspecific", "La Forma de pago seleccionada no puede asociarse por que ya existe una Letra. "));
									band2=true;
								}
								formRecibos.setPosEliminarC(j);
							}else if(tipoDetalle==ConstantesBD.codigoTipoDetalleFormasPagoLetra){
								if(!band2){
									errores.add("Forma Pago", new ActionMessage("errors.notEspecific", "La Forma de pago seleccionada no puede asociarse por que ya existe una Letra. "));
									band2=true;
								}
								formRecibos.setPosEliminarC(j);
							}
							break;
						}
					}
				}
			}
		}
		return errores;
	}

	/**
	 * Elimina una forma de pago despues de verificar que ya estaba incluida
	 * esto aplica para las forma de pago tipo pagarï¿½ y letra
	 * @param formRecibos
	 */
	private void eliminarFormaPagoRep(RecibosCajaForm formRecibos) {
		int esUltimo=Utilidades.convertirAEntero(formRecibos.getMapaFormasPago("numReg")+"")-1;
		int posDel=formRecibos.getPosEliminarC(),
		nuevaPos=ConstantesBD.codigoNuncaValido;
		if(posDel!=ConstantesBD.codigoNuncaValido)
		{			
			formRecibos.getMapaFormasPago().remove("yaGeneroNuevoRegistro_"+posDel);
			formRecibos.getMapaFormasPago().remove("nombreFormaPago_"+posDel);
			formRecibos.getMapaFormasPago().remove("codigoFormaPago_"+posDel);			
			formRecibos.getMapaFormasPago().remove("entidadFormaPago_"+posDel);
			formRecibos.getMapaFormasPago().remove("valorFormaPago_"+posDel);
			formRecibos.getMapaFormasPago().remove("tipoDetalle_"+posDel);
			formRecibos.getMapaFormasPago().remove("cheque_"+posDel);
			formRecibos.getMapaFormasPago().remove("esEditable_"+posDel);			
			nuevaPos=Utilidades.convertirAEntero(formRecibos.getMapaFormasPago("numReg")+"")-1;
			formRecibos.setMapaFormasPago("numReg",nuevaPos+"");
			
			if(posDel != esUltimo)//si no es el ultimo registro, se organiza el HashMap
			{ 
				for(int k=posDel;k<Utilidades.convertirAEntero(formRecibos.getMapaFormasPago("numReg")+"");k++)
				{
					formRecibos.setMapaFormasPago("yaGeneroNuevoRegistro_"+k,formRecibos.getMapaFormasPago("yaGeneroNuevoRegistro_"+(k+1)));
					formRecibos.setMapaFormasPago("nombreFormaPago_"+k,formRecibos.getMapaFormasPago("nombreFormaPago_"+(k+1)));
					formRecibos.setMapaFormasPago("codigoFormaPago_"+k,formRecibos.getMapaFormasPago("codigoFormaPago_"+(k+1)));
					formRecibos.setMapaFormasPago("entidadFormaPago_"+k,formRecibos.getMapaFormasPago("entidadFormaPago_"+(k+1)));
					formRecibos.setMapaFormasPago("valorFormaPago_"+k,formRecibos.getMapaFormasPago("valorFormaPago_"+(k+1)));
					formRecibos.setMapaFormasPago("tipoDetalle_"+k,formRecibos.getMapaFormasPago("tipoDetalle_"+(k+1)));
					formRecibos.setMapaFormasPago("cheque_"+k,formRecibos.getMapaFormasPago("cheque_"+(k+1)));
					formRecibos.setMapaFormasPago("esEditable_"+k,formRecibos.getMapaFormasPago("esEditable_"+(k+1)));
				}
			}
		}
	}
	/**
	 * metodo para ingresar el formato de 
	 * cheque.
	 * @param formRecibos
	 */	
	private void generarObjetoCheque(RecibosCajaForm formRecibos)
	{		
		int pos=formRecibos.getNumRegCheques();
		formRecibos.setMapaCheques("codigoDeptoPlaza_"+formRecibos.getPosSelFormaPago(),"-1");
		formRecibos.setMapaCheques("nombreDeptoPlaza_"+formRecibos.getPosSelFormaPago(),"Seleccione");
		formRecibos.setMapaCheques("codigoCiudadPlaza_"+formRecibos.getPosSelFormaPago(),"-1");
		formRecibos.setMapaCheques("nombreCiudadPlaza_"+formRecibos.getPosSelFormaPago(),"Seleccione");
		formRecibos.setMapaCheques("codigoCiudadGirador_"+formRecibos.getPosSelFormaPago(),"-1");
		formRecibos.setMapaCheques("nombreCiudadGirador_"+formRecibos.getPosSelFormaPago(),"Seleccione");
		formRecibos.setMapaCheques("codigoDeptoGirador_"+formRecibos.getPosSelFormaPago(),"-1");
		formRecibos.setMapaCheques("nombreDeptoGirador_"+formRecibos.getPosSelFormaPago(),"Seleccione");
		formRecibos.setMapaCheques("numeroCheque_"+formRecibos.getPosSelFormaPago(),"");
		formRecibos.setMapaCheques("codigoBanco_"+formRecibos.getPosSelFormaPago(),"");
		formRecibos.setMapaCheques("numeroCuenta_"+formRecibos.getPosSelFormaPago(),"");		
		formRecibos.setMapaCheques("fechaGiro_"+formRecibos.getPosSelFormaPago(),UtilidadFecha.getFechaActual()+"");
		formRecibos.setMapaCheques("valor_"+formRecibos.getPosSelFormaPago(),"");
		formRecibos.setMapaCheques("girador_"+formRecibos.getPosSelFormaPago(),"");
		formRecibos.setMapaCheques("direccionGirador_"+formRecibos.getPosSelFormaPago(),"");	   
		formRecibos.setMapaCheques("telefonoGirador_"+formRecibos.getPosSelFormaPago(),"");
		formRecibos.setMapaCheques("observaciones_"+formRecibos.getPosSelFormaPago(),"");
		formRecibos.setMapaCheques("codigoPaisPlaza_"+formRecibos.getPosSelFormaPago(),"-1");
		formRecibos.setMapaCheques("nombrePaisPlaza_"+formRecibos.getPosSelFormaPago(),"Seleccione");
		formRecibos.setMapaCheques("codigoPaisGirador_"+formRecibos.getPosSelFormaPago(),"-1");
		formRecibos.setMapaCheques("nombrePaisGirador_"+formRecibos.getPosSelFormaPago(),"Seleccione");
		formRecibos.setMapaCheques("informacionCompletaDelCheque_"+formRecibos.getPosSelFormaPago(),"false");	
		formRecibos.setMapaCheques("numeroRegistroFormaPago_"+formRecibos.getPosSelFormaPago(),formRecibos.getPosSelFormaPago()+"");
		pos ++;
		formRecibos.setNumRegCheques(pos);	   
		formRecibos.setMapaCheques("numReg",pos+"");		
	}
	/**
	 * metodo para ingresar el formato 
	 * de una tarjeta
	 * @param formRecibos
	 */
	private void generarobjetoTarjeta(RecibosCajaForm formRecibos)
	{
	  int pos=formRecibos.getNumRegTarjetas();
	  formRecibos.setMapaTarjetas("codigoTarjeta_"+formRecibos.getPosSelFormaPago(),"-1");
	  formRecibos.setMapaTarjetas("nombreCodigoTarjeta_"+formRecibos.getPosSelFormaPago(),"Seleccione");
	  formRecibos.setMapaTarjetas("codigoCiudadGirador_"+formRecibos.getPosSelFormaPago(),"-1");
	  formRecibos.setMapaTarjetas("nombreCiudadGirador_"+formRecibos.getPosSelFormaPago(),"Seleccione");
	  formRecibos.setMapaTarjetas("codigoDeptoGirador_"+formRecibos.getPosSelFormaPago(),"-1");
	  formRecibos.setMapaTarjetas("nombreDeptoGirador_"+formRecibos.getPosSelFormaPago(),"Seleccione");
	  formRecibos.setMapaTarjetas("entidad_"+formRecibos.getPosSelFormaPago(),"");
	  formRecibos.setMapaTarjetas("numeroTarjeta_"+formRecibos.getPosSelFormaPago(),"");
	  formRecibos.setMapaTarjetas("numeroComprobante_"+formRecibos.getPosSelFormaPago(),"");
	  formRecibos.setMapaTarjetas("numeroAutorizacion_"+formRecibos.getPosSelFormaPago(),"");
	  formRecibos.setMapaTarjetas("fecha_"+formRecibos.getPosSelFormaPago(),"");
	  formRecibos.setMapaTarjetas("valor_"+formRecibos.getPosSelFormaPago(),"");
	  formRecibos.setMapaTarjetas("girador_"+formRecibos.getPosSelFormaPago(),"");
	  formRecibos.setMapaTarjetas("direccion_"+formRecibos.getPosSelFormaPago(),"");
	  formRecibos.setMapaTarjetas("telefono_"+formRecibos.getPosSelFormaPago(),"");
	  formRecibos.setMapaTarjetas("observaciones_"+formRecibos.getPosSelFormaPago(),"");
	  formRecibos.setMapaTarjetas("codigoPaisGirador_"+formRecibos.getPosSelFormaPago(),"-1");
	  formRecibos.setMapaTarjetas("nombrePaisGirador_"+formRecibos.getPosSelFormaPago(),"Seleccione");
	  formRecibos.setMapaTarjetas("informacionCompletaTarjeta_"+formRecibos.getPosSelFormaPago(),"false");	
	  formRecibos.setMapaTarjetas("numeroRegistroFormaPago_"+formRecibos.getPosSelFormaPago(),formRecibos.getPosSelFormaPago()+"");
	  pos ++;
	  formRecibos.setNumRegTarjetas(pos);	   
	  formRecibos.setMapaTarjetas("numReg",pos+""); 
	}
	/**
	 * metodo para adicionar un nuevo registro
	 * al mapa de formas de pago
	 * @param formRecibos
	 */
	private void accionNuevoRegisroFormaPago(RecibosCajaForm formRecibos)
	{
		int pos=Utilidades.convertirAEntero(formRecibos.getMapaFormasPago("numReg")+"");
		if((formRecibos.getMapaFormasPago("yaGeneroNuevoRegistro_"+formRecibos.getPosSelFormaPago())+"").equals("false"))
		{
			formRecibos.setMapaFormasPago("yaGeneroNuevoRegistro_"+formRecibos.getPosSelFormaPago(),"true");
			formRecibos.setMapaFormasPago("nombreFormaPago_"+pos,"Seleccione");
			formRecibos.setMapaFormasPago("codigoFormaPago_"+pos,"-1");
			formRecibos.setMapaFormasPago("entidadFormaPago_"+pos,"");
			formRecibos.setMapaFormasPago("valorFormaPago_"+pos,"");
			formRecibos.setMapaFormasPago("tipoDetalle_"+pos,"-1");
			formRecibos.setMapaFormasPago("cheque_"+pos,"null");
			formRecibos.setMapaFormasPago("esEditable_"+pos,"false");
			formRecibos.setMapaFormasPago("yaGeneroNuevoRegistro_"+pos,"false");   
			formRecibos.setMapaFormasPago("mostrarCampos_"+pos,"false");//se debe generar el proximo registro, sin los campos para editar
			pos++;
			formRecibos.setMapaFormasPago("numReg",pos+"");  
		}
	}
	/**
	 * 
	 * @param formRecibos
	 */
	private void accionNuevoRegistroConcepto(RecibosCajaForm formRecibos)
	{				
		int pos=formRecibos.getPosSelConcepto();
		if((formRecibos.getMapaConceptos("yaGeneroNuevoRegistro_"+formRecibos.getPosSelConcepto())+"").equals("false"))
		{			
			formRecibos.setMapaConceptos("yaGeneroNuevoRegistro_"+formRecibos.getPosSelConcepto(),"true");
			//formRecibos.setMapaConceptos("codigo_concepto_"+pos,"-1");
			//formRecibos.setMapaConceptos("descripcion_concepto_"+pos,"Seleccione");
			//formRecibos.setMapaConceptos("codigoTipoConcepto_"+pos,"-1");
			formRecibos.setMapaConceptos("valorConcepto_"+pos,"");
			formRecibos.setMapaConceptos("yaGeneroNuevoRegistro_"+pos,"false");
			formRecibos.setMapaConceptos("esEditable_"+pos,"false");
			formRecibos.setMapaConceptos("docSoporte_"+pos,"");
			formRecibos.setMapaConceptos("nombreBeneficiario_"+pos,"");
			formRecibos.setMapaConceptos("tipoBeneficiario_"+pos,ConstantesBD.codigoNuncaValido+"");//joan 29/03/2006 cambio de radio button a select
			formRecibos.setMapaConceptos("nombreTipoBeneficiario_"+pos,"Seleccione");//joan 29/03/2006 cambio de radio button a select
			formRecibos.setMapaConceptos("numeroBeneficiario_"+pos,"");
			formRecibos.setMapaConceptos("valor_"+pos,"");
			formRecibos.setMapaConceptos("poseeRegistroSeleccionado_"+pos,"false");
			formRecibos.setMapaConceptos("mostrarCampos_"+pos,"false");//se debe generar el proximo registro, sin los campos para editar
			//siempre es modificacion, nuca es nuevo registro.
			//pos ++;
			//formRecibos.setMapaConceptos("numReg",pos+"");
			//por ahora solo se maneja un concepto.
			formRecibos.setMapaConceptos("numReg","1");
			  
		}
	}
	/**
	 * @param formRecibos
	 * @param mapping
	 * @return
	 */
	private void generarRecibosVacios(RecibosCajaForm formRecibos) 
	{		
		//conceptos
		resetMapaConceptos(formRecibos);
		//formas de pago
		formRecibos.setMapaFormasPago("nombreFormaPago_0","Seleccione");
		formRecibos.setMapaFormasPago("codigoFormaPago_0","-1");
		formRecibos.setMapaFormasPago("entidadFormaPago_0","");
		formRecibos.setMapaFormasPago("valorFormaPago_0","");
		formRecibos.setMapaFormasPago("tipoDetalle_0","-1");
		formRecibos.setMapaFormasPago("cheque_0","null");
		formRecibos.setMapaFormasPago("yaGeneroNuevoRegistro_0","false");
		formRecibos.setMapaFormasPago("esEditable_0","false"); 
		//formRecibos.setMapaFormasPago("existeRegistroEfectivo","false");//para el caso de tipo de forma de pago con detalle (ninguno)
		formRecibos.setMapaFormasPago("mostrarCampos_0","false");//se debe generar el primer registro, sin los campos para editar
		formRecibos.setMapaFormasPago("numReg",1+"");	   
	}

	/**
	 * 
	 * @param formRecibos
	 */
	private void resetMapaConceptos(RecibosCajaForm formRecibos) {
		formRecibos.setMapaConceptos("codigo_concepto_0","-1");
		formRecibos.setMapaConceptos("descripcion_concepto_0","Seleccione");  
		formRecibos.setMapaConceptos("yaGeneroNuevoRegistro_0","false");		
		formRecibos.setMapaConceptos("codigoTipoConcepto_0","-1");
		formRecibos.setMapaConceptos("valorConcepto_0","");
		formRecibos.setMapaConceptos("esEditable_0","false");
		formRecibos.setMapaConceptos("docSoporte_0","");		
		formRecibos.setMapaConceptos("nombreBeneficiario_0","");
		formRecibos.setMapaConceptos("tipoBeneficiario_0",ConstantesBD.codigoNuncaValido+"");//joan 29/03/2006 cambio de radio button a select
		formRecibos.setMapaConceptos("nombreTipoBeneficiario_0","Seleccione");//joan 29/03/2006 cambio de radio button a select
		formRecibos.setMapaConceptos("numeroBeneficiario_0","");
		formRecibos.setMapaConceptos("valor_0","");
		formRecibos.setMapaConceptos("poseeRegistroSeleccionado_0","false");
		formRecibos.setMapaConceptos("mostrarCampos_0","false");//se debe generar el primer registro, sin los campos para editar
		formRecibos.setMapaConceptos("numReg",1+"");
	}
//	/**
//	 * 
//	 * @param con
//	 * @return
//	 */
//	public Connection openDBConnection(Connection con)
//	{
//
//		if(con != null)
//		return con;
//					
//		try{
//			String tipoBD = System.getProperty("TIPOBD");
//			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
//			con = myFactory.getConnection();
//			}
//			catch(Exception e)
//			{
//				logger.warn(e+"Problemas con la base de datos al abrir la conexion"+e.toString());
//				return null;
//			}
//					
//			return con;
//	}
		 
//	/**
//	 * Mï¿½todo en que se cierra la conexiï¿½n (Buen manejo
//	 * recursos), usado ante todo al momento de hacer un forward
//	 * @param con Conexiï¿½n con la fuente de datos
//	 */
//	public void cerrarConexion (Connection con)
//	{
//		try{
//			if (con!=null&&!con.isClosed())
//			{
//				UtilidadBD.closeConnection(con);
//			}
//		}
//		catch(Exception e){
//			logger.error(e+"Error al tratar de cerrar la conexion con la fuente de datos. \n Excepcion: " +e.toString());
//		}
//	}
	
	/**
	 * 
	 * @param session
	 * @return
	 */
	private UsuarioBasico getUsuarioBasicoSesion(HttpSession session)
	{
		UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
			if(usuario == null)
				logger.warn("El usuario no esta cargado (null)");
			
			return usuario;
	}
	
	
	/**
	 * realiza las validaciones de acceso para poder generar el arqueo definitivo
	 * @param conLocal
	 * @param arqueosForm
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward validacionesArqueosCierres(Connection conLocal, HttpServletRequest request, ActionMapping mapping, int codigoInstitucion, String fechaDDMMYYYYRC, String loginUsuarioCajero, String consecutivoCaja) throws SQLException
	{
		Arqueos mundoArqueos= new Arqueos();
		//se evalua que no exista previamente el cierre caja
		if(mundoArqueos.existeCierreCaja(conLocal, codigoInstitucion, fechaDDMMYYYYRC,  loginUsuarioCajero, consecutivoCaja))
		{
			ActionErrors errores=new ActionErrors();
			errores.add("error.arqueos.yaExisteCierreCaja", new ActionMessage("error.arqueos.yaExisteCierreCaja", loginUsuarioCajero, consecutivoCaja, fechaDDMMYYYYRC));
			logger.warn("entra al error de [error.arqueos.yaExisteCierreCaja] ");
			saveErrors(request, errores);
			UtilidadBD.closeConnection(conLocal);
			return mapping.findForward("paginaErroresActionErrors");
		}
		//UtilidadBD.cerrarConexion(conLocal);
		return null;
	}
	

	//Agregado pro anexo 958
	private ActionForward accionEmpezarSeleccionFVAccion(Connection con, RecibosCajaForm forma, RecibosCaja mundoRecibos, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request)
	{
		
		//Se asignan los nuevos elementos para generar el recibo de caja
		//String concepto=ValoresPorDefecto.getConceptoIngresoFacturasVarias(usuario.getCodigoInstitucionInt());
		
		String concepto=ValoresPorDefecto.getConceptoIngresoFacturasVarias(usuario.getCodigoInstitucionInt());
		
		String idConcepto= "";
		
		if(!UtilidadTexto.isEmpty(concepto)){
			
			idConcepto= new BigDecimal(concepto).intValue()+"";
		
		}else{
			
			idConcepto = "";
		}
		//String idConcepto= new BigDecimal(conceptoPorDefecto).intValue()+"";
		
		int nroFactura=Integer.parseInt(forma.getNroFactura());
		
		//Inicio la funcionalidada para empezar
		this.inicializarFuncionalidad(forma, mundoRecibos,true,usuario.getCodigoInstitucionInt());
		
		//Seteo el filtro deudor
		forma.setTipoFiltro(ConstantesIntegridadDominio.acronimoFactura);
		
		//Pongo el identificador de que viene de facturas varias
		forma.setLlegoDeFV(ConstantesBD.acronimoSi);

		//Cargo los conceptos solicitados por el anexo
		//Agregado por anexo 958
		forma.setListadoConceptos(Utilidades.obtenerConceptosIngresoTesoreria(ConstantesBD.codigoTipoIngresoTesoreriaOtrasCxC, ConstantesIntegridadDominio.acronimoFactura));

		//Cargo los elementos en el mapa con los datos de la factura varia generada
		forma.setPosSelConcepto(0);
		cargarMapaConceptosFlujoFacturasVarias(con,forma, usuario.getCodigoInstitucion(), nroFactura, idConcepto);
		
		//Viene de facturas varias apra reconocer alguno elementos del jsp
		forma.setLlegoDeFV(ConstantesBD.acronimoSi);
		
		if(con==null)
			con = UtilidadBD.abrirConexion();
		
		//se limpian los atributos hidden de la busqueda
		forma.setAbrirPopUp("");
		forma.setCodigoTipoIngresoTesoreria(ConstantesBD.codigoNuncaValido);
		
		//en este punto hacemos lo de la accion buscar factura teniendo en cuenta que debemos tener cargada en la forma el consecutivo de factura
		forma.setEstado("buscarFacturas");
		
		DtoRecibosCaja facturaVaria=new DtoRecibosCaja();
		facturaVaria.setConceptoFactura(ConstantesBD.codigoTipoIngresoTesoreriaOtrasCxC+"");
		facturaVaria.setNroFactura(nroFactura+"");
		facturaVaria.setSaldoFactura(forma.getTotalConceptos());
		if(forma.getRecibosCaja()==null)
		{
			forma.setRecibosCaja(new ArrayList<DtoRecibosCaja>());
		}
		if(forma.getRecibosCaja().size()==0)
		{
			forma.getRecibosCaja().add(facturaVaria);
		}
		else
		{
			forma.getRecibosCaja().set(0, facturaVaria);
		}
		forma.setRegSeleccionado(0);
		
		Utilidades.imprimirMapa(forma.getMapaConceptos());
		
		try {
			UtilidadBD.cerrarConexion(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return mapping.findForward("paginaPrincipal");
		
		//retorna a la pagina ppal de una porque solo es una factura la que retorna en la busqueda 
		//porque la restriccion es por consecutivo e institucion (unique)
		
		//return this.accionBuscarFacturasVarias(con, formRecibos, mundoRecibos, mapping, usuario);
		
		//return mapping.findForward("paginaPrincipal"); 
	}
	
	
	/**
	 * 
	 */
	/**
	 * 
	 * @param forma
	 */
	private void cargarMapaConceptosFlujoFacturasVarias(Connection con,RecibosCajaForm forma, String institucion, int nroFactura, String concepto)
	{
		//Busco todos los datos correspondientes a la factura varia solicitada
		HashMap datosFacturaVaria=new HashMap();
		GenModFacturasVarias mundoFacturasVarias=new GenModFacturasVarias();
		datosFacturaVaria=mundoFacturasVarias.buscarFacVar(con, nroFactura, institucion);
		
		
		//busco ahora la informacion del deudor
		com.princetonsa.dto.facturasVarias.DtoDeudor dtoDeu=new com.princetonsa.dto.facturasVarias.DtoDeudor();
		//dtoDeu=Deudores.cargarInformacionNuevoDeudor(con, datosFacturaVaria.get("deudor")+"", datosFacturaVaria.get("tipoDeudor")+"");
		
		dtoDeu=Deudores.cargar(con, datosFacturaVaria.get("deudor")+"");
		
		
		logger.info("\n\n\n\n\n\n**********************!!!!la factura varia*****************"+nroFactura);
		Utilidades.imprimirMapa(datosFacturaVaria);
		
		
		
		forma.setMapaConceptos("tipoBeneficiario_1", ConstantesBD.codigoNuncaValido);
		forma.setMapaConceptos("poseeRegistroSeleccionado_1", false);
		//TODO Revisar este caso. Se cambia el numero de registros a 1 - (estaba en 2)
		forma.setMapaConceptos("numReg", "1");
		forma.setMapaConceptos("poseeRegistroSeleccionado_0", false);
		forma.setMapaConceptos("readOnlyValoConcepto_0", true);
		forma.setMapaConceptos("codigoTipoConcepto_0", ConstantesBD.codigoTipoIngresoTesoreriaOtrasCxC);
		forma.setMapaConceptos("codigoTipoConcepto_1",ConstantesBD.codigoNuncaValido);
		forma.setMapaConceptos("nombreBeneficiario_1", "");
		forma.setMapaConceptos("nombreBeneficiario_0",obtenerNombreDeudorPorTIpo(dtoDeu));
		forma.setMapaConceptos("valor_0", datosFacturaVaria.get("valorFactura"));
		forma.setMapaConceptos("valor_1", "");
		forma.setMapaConceptos("esEditable_0", true);
		forma.setMapaConceptos("esEditable_1", false);
		forma.setMapaConceptos("numeroBeneficiario_1", "");
		//TODO Revisar este caso. Se agrega el - (split)
		forma.setMapaConceptos("numeroBeneficiario_0", obtenerNumeroIdentificacion(dtoDeu));
		forma.setMapaConceptos("docSoporte_1", "");
		forma.setMapaConceptos("nombreTipoBeneficiario_1", "Seleccione");
		forma.setMapaConceptos("docSoporte_0", datosFacturaVaria.get("consecutivo"));
		forma.setMapaConceptos("mostrarCampos_0", true);
		forma.setMapaConceptos("mostrarCampos_1", false);
		forma.setMapaConceptos("valorConcepto_0", datosFacturaVaria.get("valorFactura"));
		forma.setMapaConceptos("valorConcepto_1", "");
		forma.setMapaConceptos("descripcion_concepto_0", "");
		forma.setMapaConceptos("codigo_concepto_0", concepto);
		forma.setMapaConceptos("codigo_concepto_1", ConstantesBD.codigoNuncaValido);
		forma.setMapaConceptos("descripcion_concepto_1","Seleccione");
		forma.setMapaConceptos("yaGeneroNuevoRegistro_1", false);
		forma.setMapaConceptos("yaGeneroNuevoRegistro_0", true); 
		forma.setMapaConceptos("tipoBeneficiario_0", dtoDeu.getTipoIdentificacion());
		forma.setMapaConceptos("tipoIdentificacion_2",obtenerTipoDeudor(dtoDeu));
		forma.setMapaConceptos("tipoIdentificacion_1", obtenerTipoIdentificaionDeudorPorTIpo(dtoDeu));
		forma.setMapaConceptos("codDeudor_1",dtoDeu.getCodigo());
		forma.setMapaConceptos("nroFactura",nroFactura);
		forma.setTotalConceptos(datosFacturaVaria.get("valorFactura")+"");
		forma.setRecibidoDe(dtoDeu.getRazonSocial());
		forma.setLlegoDeFV(ConstantesBD.acronimoSi);

	}
	
	
	/**
	 * @param dtoDeu
	 * @return Nombre del paciente o institución
	 */
	public String obtenerTipoDeudor(com.princetonsa.dto.facturasVarias.DtoDeudor dtoDeu){
		if(dtoDeu.getTipoDeudor().equals(IConstantesNegocioFacturasVarias.CONSTANTE_DEUDOR_PACIENTE)){
			return "paciente";
		}else{
			return "noPaciente";
		}
	}
	
	
	
	
	
	/**
	 * @param dtoDeu
	 * @return Nombre del paciente o institución
	 */
	public String obtenerNombreDeudorPorTIpo(com.princetonsa.dto.facturasVarias.DtoDeudor dtoDeu){
		if(dtoDeu.getTipoDeudor().equals(IConstantesNegocioFacturasVarias.CONSTANTE_DEUDOR_PACIENTE)){
			return dtoDeu.getPrimerNombre()+" "+dtoDeu.getSegundoNombre()+" "+dtoDeu.getPrimerApellido()+" "+dtoDeu.getSegundoApellido()+" ";
		}else{
			return dtoDeu.getRazonSocial().split("-")[2];
		}
	}
	
	/**
	 * @param dtoDeu
	 * @return tipo identificacion 
	 */
	public String obtenerTipoIdentificaionDeudorPorTIpo(com.princetonsa.dto.facturasVarias.DtoDeudor dtoDeu){
		if(dtoDeu.getTipoDeudor().equals(IConstantesNegocioFacturasVarias.CONSTANTE_DEUDOR_PACIENTE)){
			return dtoDeu.getTipoIdentificacion();
		}else{
			return IConstantesNegocioFacturasVarias.CONSTANTE_TIPO_IDENTIFICACION_NIT;
		}
	}
	
	/**
	 * @param dtoDeu
	 * @return Nombre del paciente o institución
	 */
	public String obtenerNumeroIdentificacion(com.princetonsa.dto.facturasVarias.DtoDeudor dtoDeu){
		String numero = "";
		
		if(dtoDeu.getTipoDeudor().equals(IConstantesNegocioFacturasVarias.CONSTANTE_DEUDOR_PACIENTE)){
			return dtoDeu.getNumeroIdentificacion();
		}else{
			if( dtoDeu.getRazonSocial().split("-")[0]!=null){
				numero=dtoDeu.getRazonSocial().split("-")[0];
			}
			if (dtoDeu.getRazonSocial().split("-")[1]!=null && 
					!dtoDeu.getRazonSocial().split("-")[1].equals("")&&
					  !dtoDeu.getRazonSocial().split("-")[1].equals("-")){
				
				numero+="-"+dtoDeu.getRazonSocial().split("-")[1];
			}
			return numero;
		}
	}
	
	/**
	 * Accion que genera toda la estructura de recibos de caja para ser utilizado cuando se genera 
	 * una (1) factura. 
	 * 
	 * Requerimientos= debe estar cargado el codigoConceptoIngTesoreriaFacturaUnica y 
	 * descConceptoIngTesoreriaFacturaUnica de la forma recibos de caja para asignarlos posteriormente:
	 * 
	 *  mapaConceptos(codigo_concepto_"+k+") --> codigoConceptoIngTesoreriaFacturaUnica
	 *  mapaConceptos(descripcion_concepto_"+k+") --> descConceptoIngTesoreriaFacturaUnica
	 *  
	 * Tambiï¿½n debe tenerse el consecutivo de factura en el form 
	 * RecibosCajaForm consecutivoFact
	 * 
	 * @param con
	 * @param formRecibos
	 * @param mundoRecibos
	 * @param mapping
	 * @return
	 */
	private ActionForward accionGenerarEstructuraDesdeFactura(Connection con, RecibosCajaForm formRecibos, RecibosCaja mundoRecibos, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request)
	{
		//en este punto ya debiï¿½ haber realizado todas las validaciones de acceso
		
		String temp1=formRecibos.getCodigoConceptoIngTesoreriaFacturaUnica();
		String temp2=formRecibos.getDescConceptoIngTesoreriaFacturaUnica();
		int temp3= formRecibos.getConsecutivoFact();
		
		//1.  SIMULACION PRIMER ESTADO = EMPEZAR
		this.inicializarFuncionalidad(formRecibos, mundoRecibos,true,usuario.getCodigoInstitucionInt());
		formRecibos.setPosSelConcepto(0);
		//se asigna el codigo y descripcion del concepto al mapa, [simulacion del select]
		Vector<Object> tiposIngresoTesoreria= new Vector<Object>();
		
		//tiposIngresoTesoreria.add(ConstantesBD.codigoTipoIngresoTesoreriaAbonos);//TAREA 307 MANTIS
		tiposIngresoTesoreria.add(ConstantesBD.codigoTipoIngresoTesoreriaPacientes);
		
		
		/*
		formRecibos.setMapaConceptos(UtilidadValidacion.getCodigoDescripcionConceptoTipoRegimen(con, usuario.getCodigoInstitucionInt(), formRecibos.getTipoRegimenAcronimo(), tiposIngresoTesoreria));
		formRecibos.setMapaConceptos("numReg", formRecibos.getMapaConceptos("numRegistros"));
		
		//formRecibos.setMapaConceptos("codigo_concepto_0", formRecibos.getCodigoConceptoIngTesoreriaFacturaUnica());
		//formRecibos.setMapaConceptos("descripcion_concepto_0", formRecibos.getDescConceptoIngTesoreriaFacturaUnica());
		PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
		//formRecibos.setMapaConceptos("codigoPaciente_0", paciente.getCodigoPersona());
		
		for(int w=0; w<Utilidades.convertirAEntero(formRecibos.getMapaConceptos("numRegistros")+""); w++)
			formRecibos.setMapaConceptos("codigoPaciente_"+w, paciente.getCodigoPersona());
		*/

		formRecibos.setMapaConceptosAuxiliarFlujoFact(UtilidadValidacion.getCodigoDescripcionConceptoTipoRegimen(con, usuario.getCodigoInstitucionInt(), formRecibos.getTipoRegimenAcronimo(), tiposIngresoTesoreria, true));
		this.cargarMapaConceptosFlujoFacturas(formRecibos);
		//cuando se selecciona el concepto se cambia el estado a nuevoregistro concepto que es nuestro 2 paso
		
		//2.  SIMULACION SEGUNDO ESTADO = nuevoRegistroConcepto
		//Este estado llama al metodo provado accionConcepto, y pues la idea es reutilizarlo pero sin hacer el mapping.forward que este retorna
		//this.accionConcepto(con, formRecibos, mapping, mundoRecibos, usuario);
		//se tiene que volver a abrir la conexion debido a que se cierra en la accion concepto
		
		//en ese momento se despliega el popup de la busqueda de facturas
		
		//3. SIMULACION DE LA BUSQUEDA DE FACTURAS buscarFacturas.jsp
		//se limpian los atributos hidden de la busqueda
		formRecibos.setAbrirPopUp("");
		formRecibos.setCodigoTipoIngresoTesoreria(ConstantesBD.codigoNuncaValido);
		
		//en este punto hacemos lo de la accion buscar factura teniendo en cuenta que debemos tener cargada en la forma el consecutivo de factura
		formRecibos.setEstado("buscarFacturas");
		
		formRecibos.setCodigoConceptoIngTesoreriaFacturaUnica(temp1);
		formRecibos.setDescConceptoIngTesoreriaFacturaUnica(temp2);
		formRecibos.setConsecutivoFact(temp3);
		
		//retorna a la pagina ppal de una porque solo es una factura la que retorna en la busqueda 
		//porque la restriccion es por consecutivo e institucion (unique)
		return this.accionBuscarFacturas(con, formRecibos, mundoRecibos, mapping, usuario);
	}

	
	
	/**
	 * 
	 * @param forma
	 */
	private void cargarMapaConceptosFlujoFacturas(RecibosCajaForm forma)
	{
		forma.setMapaConceptos("tipoBeneficiario_1", ConstantesBD.codigoNuncaValido);
		forma.setMapaConceptos("poseeRegistroSeleccionado_1", false);
		forma.setMapaConceptos("numReg", "2");
		forma.setMapaConceptos("poseeRegistroSeleccionado_0", false);
		forma.setMapaConceptos("readOnlyValoConcepto_0", true);
		forma.setMapaConceptos("codigoTipoConcepto_0", forma.getMapaConceptosAuxiliarFlujoFact("codigo_tipo_ingreso_0"));
		forma.setMapaConceptos("codigoTipoConcepto_1",ConstantesBD.codigoNuncaValido);
		forma.setMapaConceptos("nombreBeneficiario_1", "");
		forma.setMapaConceptos("nombreBeneficiario_0", "");
		forma.setMapaConceptos("valor_0", "");
		forma.setMapaConceptos("valor_1", "");
		forma.setMapaConceptos("esEditable_0", true);
		forma.setMapaConceptos("esEditable_1", false);
		forma.setMapaConceptos("numeroBeneficiario_1", "");
		forma.setMapaConceptos("numeroBeneficiario_0", "");
		forma.setMapaConceptos("docSoporte_1", "");
		forma.setMapaConceptos("nombreTipoBeneficiario_1", "Seleccione");
		forma.setMapaConceptos("docSoporte_0", "");
		forma.setMapaConceptos("mostrarCampos_0", true);
		forma.setMapaConceptos("mostrarCampos_1", false);
		forma.setMapaConceptos("valorConcepto_0", "");
		forma.setMapaConceptos("valorConcepto_1", "");
		forma.setMapaConceptos("descripcion_concepto_0", forma.getMapaConceptosAuxiliarFlujoFact("descripcion_concepto_0"));
		forma.setMapaConceptos("codigo_concepto_0", forma.getMapaConceptosAuxiliarFlujoFact("codigo_concepto_0"));
		forma.setMapaConceptos("codigo_concepto_1", ConstantesBD.codigoNuncaValido);
		forma.setMapaConceptos("descripcion_concepto_1","Seleccione");
		forma.setMapaConceptos("yaGeneroNuevoRegistro_1", false);
		forma.setMapaConceptos("yaGeneroNuevoRegistro_0", true);
		forma.setMapaConceptos("tipoBeneficiario_0", ConstantesBD.codigoNuncaValido);
	
	}

	
	/**
	 * accion para buscar las facturas varias
	 * @param con
	 * @param formRecibos
	 * @param mundoRecibos
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionBuscarFacturasVarias(	Connection con,	
												RecibosCajaForm formRecibos,	
												RecibosCaja mundoRecibos, 
												ActionMapping mapping, 
												UsuarioBasico usuario)
	{
		formRecibos.resetBusqFacVarias();
		formRecibos.setRecibosCaja(mundoRecibos.facturasVarias(con, 
				formRecibos.getNroFactura(), 
				formRecibos.getTipoDeudor(), 
				formRecibos.getCodigoDeudor(), 
				formRecibos.getConceptoFacVarias(), 
				formRecibos.getCodigoMultaCita(),
				formRecibos.getFechaGenMultaCita(), usuario));
		if(formRecibos.getRecibosCaja().size()==1){
			
			if( formRecibos.getRecibosCaja().get(0).getAplicaReciboCaja().equals(ConstantesBD.acronimoSi) )
			{
			
				formRecibos.setMapaConceptos("nombreBeneficiario_"+formRecibos.getPosSelConcepto(), formRecibos.getRecibosCaja().get(0).getDeudor());
				formRecibos.setMapaConceptos("numeroBeneficiario_"+formRecibos.getPosSelConcepto(), formRecibos.getRecibosCaja().get(0).getIdentificacionDeudor());
				formRecibos.setMapaConceptos("tipoBeneficiario_"+formRecibos.getPosSelConcepto(), "");
				formRecibos.setMapaConceptos("codigoConvenio_"+formRecibos.getPosSelConcepto(), "");
				formRecibos.setMapaConceptos("codDeudor_"+formRecibos.getPosSelConcepto(),formRecibos.getRecibosCaja().get(0).getCodDeudor());
				formRecibos.setMapaConceptos("tipoIdentificacion_1", formRecibos.getRecibosCaja().get(0).getTipoIdentificacion());
				formRecibos.setMapaConceptos("tipoIdentificacion_2", formRecibos.getRecibosCaja().get(0).getTipoDeudor());
				formRecibos.setRecibidoDe(formRecibos.getRecibosCaja().get(0).getDeudor());
				formRecibos.setRegSeleccionado(0);
				formRecibos.setCloseVenBusq(ConstantesBD.acronimoSi);
				
				formRecibos.setMapaConceptos("conceptoFactura_"+formRecibos.getPosSelConcepto(), formRecibos.getRecibosCaja().get(0).getConceptoFactura());
				formRecibos.setMapaConceptos("codFacturaVaria_"+formRecibos.getPosSelConcepto(), formRecibos.getRecibosCaja().get(0).getNroFactura());
			}
		}
		formRecibos.setUltimaPropiedad(indices[0]);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("buscarFacVarias");
	}
	
	
	/**
	 * metodo para realizar la asignacion
	 * de valores y realizar los procesos respectivos
	 * cuando se selecciona un convenio
	 * @param con
	 * @param formRecibos
	 * @param mapping
	 * @return
	 */
	private ActionForward accionFacturaVariaSeleccionada(Connection con, RecibosCajaForm formRecibos, ActionMapping mapping) 
	{
		
		formRecibos.setMostrarConsecutivoFv(false);
		
		this.inicializarPopUps(formRecibos);	   
		int copcepto=formRecibos.getPosSelConcepto();
		int convenio=formRecibos.getRegSeleccionado();
		formRecibos.setMapaConceptos("nombreBeneficiario_"+copcepto, formRecibos.getRecibosCaja().get(convenio).getDeudor());
		formRecibos.setMapaConceptos("numeroBeneficiario_"+copcepto,formRecibos.getRecibosCaja().get(convenio).getIdentificacionDeudor());
		formRecibos.setMapaConceptos("valorConcepto_"+copcepto,formRecibos.getRecibosCaja().get(convenio).getSaldoFactura());
		formRecibos.setMapaConceptos("tipoBeneficiario_"+copcepto,formRecibos.getRecibosCaja().get(convenio).getTipoIdentificacion());
		formRecibos.setMapaConceptos("tipoIdentificacion_1", formRecibos.getRecibosCaja().get(convenio).getTipoIdentificacion());
		formRecibos.setMapaConceptos("tipoIdentificacion_2", formRecibos.getRecibosCaja().get(convenio).getTipoDeudor());
		formRecibos.setMapaConceptos("codigoConvenio_"+copcepto,"");
		formRecibos.setMapaConceptos("codDeudor_"+copcepto,formRecibos.getRecibosCaja().get(convenio).getCodDeudor());
		formRecibos.setMapaConceptos("poseeRegistroSeleccionado_"+copcepto,"true");
		formRecibos.setMapaConceptos("codigoTipoConcepto_"+copcepto, ConstantesBD.codigoTipoIngresoTesoreriaOtrasCxC);
		
		
		//Cambio por Anexo 958
		if (Utilidades.obtenerFiltroValorConceptoIngreso(con,Utilidades.convertirAEntero(formRecibos.getMapaConceptos("codigo_concepto_0")+"")).equals(ConstantesIntegridadDominio.acronimoFactura))
		{
			formRecibos.setMapaConceptos("docSoporte_"+copcepto,formRecibos.getRecibosCaja().get(convenio).getNroFactura());
			formRecibos.setMapaConceptos("consecutivoDocSoporte_"+copcepto,formRecibos.getRecibosCaja().get(convenio).getConsecutivo());
			formRecibos.setMostrarConsecutivoFv(true);
		}

		
		formRecibos.setCloseVenBusq(ConstantesBD.acronimoNo);
		formRecibos.setRecibidoDe(formRecibos.getRecibosCaja().get(convenio).getDeudor());
		UtilidadBD.closeConnection(con);
  	   	return mapping.findForward("paginaPrincipal"); 
	}
	
	/**
	 * verifica si el concepto es de tipo multa o no 
	 * @param con
	 * @param formRecibos
	 * @param mundoRecibos
	 * @param mapping
	 * @return
	 */
	private ActionForward accionVerificarConcepto(Connection con, RecibosCaja mundoRecibos, RecibosCajaForm formRecibos, ActionMapping mapping) 
	{
		formRecibos.resetBusqFacVarias();
		if(mundoRecibos.tipoConceptoIsMulta(con, Utilidades.convertirAEntero(formRecibos.getConceptoFacVarias())))
			formRecibos.setMostrarMultasCitas(ConstantesBD.acronimoSi);
		else
			formRecibos.setMostrarMultasCitas(ConstantesBD.acronimoNo);
		
		UtilidadBD.closeConnection(con);
		
		return mapping.findForward("buscarFacVarias");
	}
	
//		   
//	/**
//	 * Metodo para el ordenamieto por columnas
//	 * @param con
//	 * @param formRecibos
//	 * @param mundoRecibos
//	 * @param mapping
//	 * @param usuario
//	 * @return
//	 */
//	private ActionForward ordenarColumnas(RecibosCajaForm formRecibos, RecibosCaja mundoRecibos, ActionMapping mapping)
//	{
//		ArrayList<DtoRecibosCaja> dto = new ArrayList<DtoRecibosCaja>();
//		dto = mundoRecibos.ordenarColumna(indices, 
//					formRecibos.getRecibosCaja(), 
//					formRecibos.getUltimaPropiedad(), 
//					indices[Utilidades.convertirAEntero(formRecibos.getColumna())]);
//
//		if(dto.size()==formRecibos.getRecibosCaja().size()){
//			formRecibos.setUltimaPropiedad(indices[Utilidades.convertirAEntero(formRecibos.getColumna())]);
//			formRecibos.resetBusqFacVarias();
//			formRecibos.setRecibosCaja(dto);
//		}
//		
//		return mapping.findForward("buscarFacVarias");
//	}
	
	/**
	 * reset Busqueda Facturas Varias
	 */
	private ActionForward restBusquedaFacVarias(RecibosCajaForm formRecibos,  ActionMapping mapping)
	{
		formRecibos.resetBusq();
		return mapping.findForward("iniciarBuscarFacVarias");
	}
	
	/**
	 * validacion datos financiacion
	 * @param formRecibos
	 * @return
	 */
	private ActionErrors validarDatosFinanciacion(RecibosCajaForm formRecibos)
	{
		ActionErrors errores = new ActionErrors(); 
		logger.info("entra ha esta parte a velidar");
		if(formRecibos.getDatosFinanciacion().getDeudor().getTipoDeudor().equals(""))
			errores.add("tipo deudor", new ActionMessage("errors.required","El Tipo Deudor "));
		if(formRecibos.getDatosFinanciacion().getDeudor().getTipoIdentificacion().equals(""))
			errores.add("tipo identificacion deudor", new ActionMessage("errors.required","El Tipo Identificacï¿½n del Deudor "));
		if(formRecibos.getDatosFinanciacion().getDeudor().getNumeroIdentificacion().equals(""))
			errores.add("numero identificacion deudor", new ActionMessage("errors.required","El Nï¿½mero Identificaciï¿½n del Deudor "));
		if(formRecibos.getDatosFinanciacion().getDeudor().getPrimerApellido().equals(""))
			errores.add("primer apellido deudor", new ActionMessage("errors.required","El Primer Apellido del Deudor "));
		if(formRecibos.getDatosFinanciacion().getDeudor().getPrimerNombre().equals(""))
			errores.add("primer nombre deudor", new ActionMessage("errors.required","El Primer Nombre del Deudor "));
		if(formRecibos.getDatosFinanciacion().getDeudor().getDireccion().equals(""))
			errores.add("primer nombre deudor", new ActionMessage("errors.required","La Direcciï¿½n del Deudor "));
		if(formRecibos.getDatosFinanciacion().getDeudor().getTelefono().equals(""))
			errores.add("primer nombre deudor", new ActionMessage("errors.required","El Telï¿½fono del Deudor "));
		if(formRecibos.getDatosFinanciacion().getDeudor().getOcupacion().equals(""))
			errores.add("primer nombre deudor", new ActionMessage("errors.required","La Ocupaciï¿½n del Deudor "));
		if(formRecibos.getDatosFinanciacion().getDeudor().getDetalleocupacion().equals(""))
			errores.add("primer nombre deudor", new ActionMessage("errors.required","El Detalle Ocupaciï¿½n del Deudor "));
		
		if(!formRecibos.getDatosFinanciacion().getDeudor().getOcupacion().equals(""))
		{
			if(formRecibos.getDatosFinanciacion().getDeudor().getOcupacion().equals(ConstantesIntegridadDominio.acronimoTipoTrabajadorEmpleado))
			{
				if(formRecibos.getDatosFinanciacion().getDeudor().getEmpresa().equals(""))
					errores.add("primer nombre deudor", new ActionMessage("errors.required","La Empresa del Deudor "));
				if(formRecibos.getDatosFinanciacion().getDeudor().getCargo().equals(""))
					errores.add("primer nombre deudor", new ActionMessage("errors.required","El Cargo del Deudor "));
				if(formRecibos.getDatosFinanciacion().getDeudor().getAntiguedad().equals(""))
					errores.add("primer nombre deudor", new ActionMessage("errors.required","La Antigï¿½edad del Deudor "));
				if(formRecibos.getDatosFinanciacion().getDeudor().getDireccionOficina().equals(""))
					errores.add("Direccion", new ActionMessage("errors.required","La Direcciï¿½n de la Oficina del Deudor "));
				if(formRecibos.getDatosFinanciacion().getDeudor().getTelefonoOficina().equals(""))
					errores.add("primer nombre deudor", new ActionMessage("errors.required","El Telï¿½fono de la Oficina del Deudor "));
			}else{
				if(formRecibos.getDatosFinanciacion().getDeudor().getNombresReferenciaFamiliar().equals(""))
					errores.add("nombre referencia familiar", new ActionMessage("errors.required","El Nombre en la secciÃ³n Referencia Familiar del Deudor "));
				if(formRecibos.getDatosFinanciacion().getDeudor().getDireccionReferenciaFamiliar().equals(""))
					errores.add("direccion referencia familiar", new ActionMessage("errors.required","La Direccion en la secciÃ³n Referencia Familiar del Deudor "));
				if(formRecibos.getDatosFinanciacion().getDeudor().getOcupacion().equals(ConstantesIntegridadDominio.acronimoTipoTrabajadorIndependiente))
				{
					if(formRecibos.getDatosFinanciacion().getDeudor().getDireccionOficina().equals(""))
						errores.add("Direccion", new ActionMessage("errors.required","La Direcciï¿½n de la Oficina del Deudor "));
					if(formRecibos.getDatosFinanciacion().getDeudor().getTelefonoOficina().equals(""))
						errores.add("primer nombre deudor", new ActionMessage("errors.required","El Telï¿½fono de la Oficina del Deudor "));
				}
			}
		}
		
		// Fin validaciones de Deudor
		
		// validaciones de Codeudor
		if(!formRecibos.getDatosFinanciacion().getCodeudor().getTipoDeudor().equals(""))
		{
			if(formRecibos.getDatosFinanciacion().getDeudor().getTipoIdentificacion().equals(formRecibos.getDatosFinanciacion().getCodeudor().getTipoIdentificacion())
				&& formRecibos.getDatosFinanciacion().getDeudor().getNumeroIdentificacion().equals(formRecibos.getDatosFinanciacion().getCodeudor().getNumeroIdentificacion()))
				errores.add("descripcion", new ActionMessage("errors.notEspecific","El Codeudor debe ser diferente al deudor. "));
			
			if(formRecibos.getDatosFinanciacion().getCodeudor().getTipoIdentificacion().equals(""))
				errores.add("tipo identificacion codeudor", new ActionMessage("errors.required","El Tipo Identificacï¿½n del Codeudor "));
			if(formRecibos.getDatosFinanciacion().getCodeudor().getNumeroIdentificacion().equals(""))
				errores.add("numero identificacion codeudor", new ActionMessage("errors.required","El Nï¿½mero Identificaciï¿½n del Codeudor "));
			if(formRecibos.getDatosFinanciacion().getCodeudor().getPrimerApellido().equals(""))
				errores.add("primer apellido codeudor", new ActionMessage("errors.required","El Primer Apellido del Codeudor "));
			if(formRecibos.getDatosFinanciacion().getCodeudor().getPrimerNombre().equals(""))
				errores.add("primer nombre codeudor", new ActionMessage("errors.required","El Primer Nombre del Codeudor "));
			if(formRecibos.getDatosFinanciacion().getCodeudor().getDireccion().equals(""))
				errores.add("primer nombre codeudor", new ActionMessage("errors.required","La Direcciï¿½n del Codeudor "));
			if(formRecibos.getDatosFinanciacion().getCodeudor().getTelefono().equals(""))
				errores.add("primer nombre codeudor", new ActionMessage("errors.required","El Telï¿½fono del Codeudor "));
			if(formRecibos.getDatosFinanciacion().getCodeudor().getOcupacion().equals(""))
				errores.add("primer nombre codeudor", new ActionMessage("errors.required","La Ocupaciï¿½n del Codeudor "));
			if(formRecibos.getDatosFinanciacion().getCodeudor().getDetalleocupacion().equals(""))
				errores.add("primer nombre codeudor", new ActionMessage("errors.required","El Detalle Ocupaciï¿½n del Codeudor "));
			
			if(!formRecibos.getDatosFinanciacion().getCodeudor().getOcupacion().equals(""))
			{
				if(formRecibos.getDatosFinanciacion().getCodeudor().getOcupacion().equals(ConstantesIntegridadDominio.acronimoTipoTrabajadorEmpleado))
				{
					if(formRecibos.getDatosFinanciacion().getCodeudor().getEmpresa().equals(""))
						errores.add("primer nombre codeudor", new ActionMessage("errors.required","La Empresa del Codeudor "));
					if(formRecibos.getDatosFinanciacion().getCodeudor().getCargo().equals(""))
						errores.add("primer nombre codeudor", new ActionMessage("errors.required","El Cargo del Codeudor "));
					if(formRecibos.getDatosFinanciacion().getCodeudor().getAntiguedad().equals(""))
						errores.add("primer nombre codeudor", new ActionMessage("errors.required","La Antigï¿½edad del Codeudor "));
					if(formRecibos.getDatosFinanciacion().getCodeudor().getDireccionOficina().equals(""))
						errores.add("Direccion", new ActionMessage("errors.required","La Direcciï¿½n de la Oficina del Codeudor "));
					if(formRecibos.getDatosFinanciacion().getCodeudor().getTelefonoOficina().equals(""))
						errores.add("primer nombre codeudor", new ActionMessage("errors.required","El Telï¿½fono de la Oficina del Codeudor "));
				}else{
					if(formRecibos.getDatosFinanciacion().getCodeudor().getNombresReferenciaFamiliar().equals(""))
						errores.add("nombre referencia familiar", new ActionMessage("errors.required","El Nombre en la secciÃ³n Referencia Familiar del Codeudor "));
					if(formRecibos.getDatosFinanciacion().getCodeudor().getDireccionReferenciaFamiliar().equals(""))
						errores.add("direccion referencia familiar", new ActionMessage("errors.required","La Direccion en la secciÃ³n Referencia Familiar del Codeudor "));
					if(formRecibos.getDatosFinanciacion().getCodeudor().getOcupacion().equals(ConstantesIntegridadDominio.acronimoTipoTrabajadorIndependiente))
					{
						if(formRecibos.getDatosFinanciacion().getCodeudor().getDireccionOficina().equals(""))
							errores.add("Direccion", new ActionMessage("errors.required","La Direcciï¿½n de la Oficina del Codeudor "));
						if(formRecibos.getDatosFinanciacion().getCodeudor().getTelefonoOficina().equals(""))
							errores.add("primer nombre codeudor", new ActionMessage("errors.required","El Telï¿½fono de la Oficina del Codeudor "));
					}
				}
			}
		}
		// Fin validaciones de Codeudor
		
		// validaciones Datos Financiaciï¿½n
		if(formRecibos.getDatosFinanciacion().getNroCoutas()<1 || 
				formRecibos.getDatosFinanciacion().getNroCoutas()>formRecibos.getDatosFinanciacion().getMaxNroCuotasFinan()){
			errores.add("Numero Cuotas", new ActionMessage("errors.range",
					"No Cuotas","1",formRecibos.getDatosFinanciacion().getMaxNroCuotasFinan()));
		}else{// verificar el numero de documento de las cuotas datos financiacion
			if(formRecibos.getDatosFinanciacion().getNroCoutas()==formRecibos.getDatosFinanciacion().getCuotasDatosFinan().size())
			{
				for(int i=0;i<formRecibos.getDatosFinanciacion().getCuotasDatosFinan().size();i++)
				{
					if(formRecibos.getDatosFinanciacion().getCuotasDatosFinan().get(i).getNumeroDocumento().equals(""))
						errores.add("numero documento", new ActionMessage("errors.required","El Nï¿½mero de Documento de la Cuota ["+(i+1)+"]"));
				}
			}else
				errores.add("numero documento", new ActionMessage("errors.notEspecific","Es requerida la generaciï¿½n de cuotas con su respectivo nï¿½mero de documento "));
		}
		
		if(!formRecibos.getDatosFinanciacion().getFechaInicio().equals(""))
		{
			if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(
					UtilidadFecha.conversionFormatoFechaAAp(formRecibos.getDatosFinanciacion().getFechaInicio()),
					UtilidadFecha.conversionFormatoFechaAAp(UtilidadFecha.getFechaActual())))
				errores.add("fecha inicio", new ActionMessage("errors.range",
						"La Fecha Inicio","fecha generaciï¿½n factura ["+formRecibos.getMapaConceptos("fechaGenFactura_0")+"]",
						"fecha actual del Sistema ["+UtilidadFecha.getFechaActual()+"]"));
			if(UtilidadFecha.esFechaMenorQueOtraReferencia(
					UtilidadFecha.conversionFormatoFechaAAp(formRecibos.getDatosFinanciacion().getFechaInicio()),
					UtilidadFecha.conversionFormatoFechaAAp(formRecibos.getMapaConceptos("fechaGenFactura_0")+"")))
				errores.add("fecha inicio", new ActionMessage("errors.range",
						"La Fecha Inicio","fecha generaciï¿½n factura ["+formRecibos.getMapaConceptos("fechaGenFactura_0")+"]",
						"fecha actual del Sistema ["+UtilidadFecha.getFechaActual()+"]"));
			
			if(formRecibos.getDatosFinanciacion().getDiasPorCuota()<1 ||
					formRecibos.getDatosFinanciacion().getDiasPorCuota()>formRecibos.getDatosFinanciacion().getMaxNroDiasFinanCuo())
				errores.add("Numero Cuotas", new ActionMessage("errors.range",
						"Dï¿½as por Cuotas","1",formRecibos.getDatosFinanciacion().getMaxNroDiasFinanCuo()));
		}else{
			errores.add("fecha inicio", new ActionMessage("errors.required","El Campo Fecah Inicio "));
		}
		// Fin validaciones Datos Financiaciï¿½n
			
		return errores;
	}
	
	/**
	 * validacion cuotas datos financiacion
	 * @param formRecibos
	 * @return
	 */
	private ActionErrors validarCuotasDatosFinanciacion(RecibosCajaForm formRecibos)
	{
		ActionErrors errores = new ActionErrors();
		for(int i=0;i<formRecibos.getDatosFinanciacion().getCuotasDatosFinan().size();i++)
		{
			if(formRecibos.getDatosFinanciacion().getCuotasDatosFinan().get(i).getNumeroDocumento().equals(""))
				errores.add("numero documento", new ActionMessage("errors.required","El Nï¿½mero de Documento de la Cuota ["+(i+1)+"]"));
		}
		return errores;
	}
	
	
	/**
	 * metodo para eliminar un registro del 
	 * mapa de formas de pago
	 * @param con
	 * @param formRecibos
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEliminarRegistroFormaPagoPL(Connection con, RecibosCajaForm formRecibos, ActionMapping mapping) 
	{
		int esUltimo=Utilidades.convertirAEntero(formRecibos.getMapaFormasPago("numReg")+"")-1;
		int posDel= ConstantesBD.codigoNuncaValido;
		int nuevaPos=ConstantesBD.codigoNuncaValido;
		
		int numRegFP =Utilidades.convertirAEntero(formRecibos.getMapaFormasPago("numReg")+"");
		
		this.inicializarPopUps(formRecibos); 
		/**si se elimina forma de pago ninguno(efectivo), se quita la restricciï¿½n,
		 * de solo adicionar una sola forma de pago en efectivo, para volverla adicionar
		 * si es del caso*/
		/*if(Utilidades.convertirAEntero(formRecibos.getMapaFormasPago("tipoDetalle_"+posDel)+"")==ConstantesBD.codigoTipoDetalleFormasPagoNinguno)
			formRecibos.setMapaFormasPago("existeRegistroEfectivo","false");*/
		//if(posDel!=ConstantesBD.codigoNuncaValido)
		for(int i=0;i<numRegFP;i++)
		{
			
			if(Utilidades.convertirAEntero(formRecibos.getMapaFormasPago("tipoDetalle_"+i)+"") == ConstantesBD.codigoTipoDetalleFormasPagoPagare
					|| Utilidades.convertirAEntero(formRecibos.getMapaFormasPago("tipoDetalle_"+i)+"") == ConstantesBD.codigoTipoDetalleFormasPagoLetra)
			{
				formRecibos.getMapaFormasPago().remove("yaGeneroNuevoRegistro_"+i);
				formRecibos.getMapaFormasPago().remove("nombreFormaPago_"+i);
				formRecibos.getMapaFormasPago().remove("codigoFormaPago_"+i);			
				formRecibos.getMapaFormasPago().remove("entidadFormaPago_"+i);
				formRecibos.getMapaFormasPago().remove("valorFormaPago_"+i);
				formRecibos.getMapaFormasPago().remove("tipoDetalle_"+i);
				formRecibos.getMapaFormasPago().remove("cheque_"+i);
				formRecibos.getMapaFormasPago().remove("esEditable_"+i);			
				nuevaPos=Utilidades.convertirAEntero(formRecibos.getMapaFormasPago("numReg")+"")-1;
				formRecibos.setMapaFormasPago("numReg",nuevaPos+"");
				
				if(i != esUltimo)//si no es el ultimo registro, se organiza el HashMap
				{ 
					for(int k=posDel;k<Utilidades.convertirAEntero(formRecibos.getMapaFormasPago("numReg")+"");k++)
					{
						formRecibos.setMapaFormasPago("yaGeneroNuevoRegistro_"+k,formRecibos.getMapaFormasPago("yaGeneroNuevoRegistro_"+(k+1)));
						formRecibos.setMapaFormasPago("nombreFormaPago_"+k,formRecibos.getMapaFormasPago("nombreFormaPago_"+(k+1)));
						formRecibos.setMapaFormasPago("codigoFormaPago_"+k,formRecibos.getMapaFormasPago("codigoFormaPago_"+(k+1)));
						formRecibos.setMapaFormasPago("entidadFormaPago_"+k,formRecibos.getMapaFormasPago("entidadFormaPago_"+(k+1)));
						formRecibos.setMapaFormasPago("valorFormaPago_"+k,formRecibos.getMapaFormasPago("valorFormaPago_"+(k+1)));
						formRecibos.setMapaFormasPago("tipoDetalle_"+k,formRecibos.getMapaFormasPago("tipoDetalle_"+(k+1)));
						formRecibos.setMapaFormasPago("cheque_"+k,formRecibos.getMapaFormasPago("cheque_"+(k+1)));
						formRecibos.setMapaFormasPago("esEditable_"+k,formRecibos.getMapaFormasPago("esEditable_"+(k+1)));
					}
				}
			}
		}
		UtilidadBD.closeConnection(con);
  	   	return mapping.findForward("paginaPrincipal");  	  
	}
	
	
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @return
	 */
	public ActionForward ordenarListaBusquedaFacturasVarias(RecibosCajaForm forma, ActionMapping mapping)
	{
		forma.setEstado("consultaVentasBeneficiarios");
		logger.info("patron->" + forma.getPatronOrdenar());
		logger.info("des -->" + forma.getEsDescendente() );
		
		boolean ordenamiento= false;
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente")){
			
			ordenamiento = true;
		}
		SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		Collections.sort(forma.getRecibosCaja() ,sortG);
		return mapping.findForward("buscarFacVarias");
	}
	
	/**
	 * Mï¿½todo que se encarga de buscar los pacientes para asociarlos
	 * al recibo de caja.
	 * 
	 * @param mapping
	 * @param con
	 * @param formRecibos
	 * @param mundoRecibos
	 * @param usuario
	 * @return
	 */
	private ActionForward buscarPacientes(ActionMapping mapping,Connection con, RecibosCajaForm formRecibos,RecibosCaja mundoRecibos, UsuarioBasico usuario) {
		
		mundoRecibos.setInstitucion(usuario.getCodigoInstitucionInt());
		mundoRecibos.setValorConcepto(formRecibos.getValorConcepto());
		mundoRecibos.setIdentificacionPaciente(formRecibos.getIdentificacionPaciente());
		mundoRecibos.setTipoIdentificacion(formRecibos.getTipoIdentificacion());
		mundoRecibos.setPrimerNombre(formRecibos.getPrimerNombre());
		mundoRecibos.setSegundoNombre(formRecibos.getSegundoNombre());
		mundoRecibos.setPrimerApellido(formRecibos.getPrimerApellido());
		mundoRecibos.setSegundoApellido(formRecibos.getSegundoApellido());

		String tipoConcepto=formRecibos.getMapaConceptos("codigoTipoConcepto_0")+"";
		boolean controlarAbonoPaciente=false;
		if(tipoConcepto.equals(ConstantesBD.codigoTipoIngresoTesoreriaAbonos+""))
		{
			controlarAbonoPaciente=UtilidadTexto.getBoolean(ValoresPorDefecto.getControlarAbonoPacientePorNroIngreso(usuario.getCodigoInstitucionInt()));
		}
	
		formRecibos.setMapaPacientes(mundoRecibos.generarConsultaPacientes(con, controlarAbonoPaciente));
		//limpiar atributos para nueva seleccion
		formRecibos.setCodigoTipoIngresoTesoreria(ConstantesBD.codigoNuncaValido);
		
		if(!formRecibos.getMapaPacientes().isEmpty()){
			
			if(Utilidades.convertirAEntero(formRecibos.getMapaPacientes("numReg")+"")==1)//si solo es un registro postularlo automaticamente
			{
				//Utilidades.imprimirMapa(formRecibos.getMapaConceptos());
				formRecibos.setRegSeleccionado(0);//el unico registro se encuentra en el mapa en la posicion 0
				return this.accionPacienteSeleccionado(con,formRecibos,mapping); 
			}
		}
		
		formRecibos.setAbrirPopUp("pacientes");
		//Utilidades.imprimirMapa(formRecibos.getMapaConceptos());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaPrincipal");
	}
	
	/**
	 * Mï¿½todo que carga la informaciï¿½n necesaria para generar el recibo de caja
	 * cuando se realiza el llamado a la funciï¿½n desde la agenda odontolï¿½gica.
	 * 
	 * @param mapping
	 * @param request
	 * @param con
	 * @param formRecibos
	 * @param mundoRecibos
	 * @param usuario
	 */
	private void generarReciboAgendaOdon(ActionMapping mapping,
			HttpServletRequest request, Connection con,
			RecibosCajaForm formRecibos, RecibosCaja mundoRecibos,
			UsuarioBasico usuario) {
		
		Log4JManager.info("consecutivo de la caja " + request.getParameter("consecutivoCaja"));
		Log4JManager.info("codigo del paciente " + request.getParameter("idPaciente"));
		Log4JManager.info("tipoIdentificacion del paciente " + request.getParameter("tipoIdentificacion"));
		
		
		formRecibos.setPosSelConcepto(0);
		formRecibos.setFlujoAgendaOdontologica(true);
		formRecibos.setCodigoTipoIngresoTesoreria(ConstantesBD.codigoTipoIngresoTesoreriaAbonos);
		
		formRecibos.setMensaje(new ResultadoBoolean(false));
		//formRecibos.setTipoFiltro(ConstantesIntegridadDominio.acronimoDeudor);
		
		Vector<Object> tiposIngresoTesoreria= new Vector<Object>();
		tiposIngresoTesoreria.add(ConstantesBD.codigoTipoIngresoTesoreriaAbonos);
		
		con = UtilidadBD.abrirConexion();
		
//		formRecibos.setMapaConceptosAuxiliarFlujoFact(UtilidadValidacion.getCodigoDescripcionConceptoTipoRegimen(con, usuario.getCodigoInstitucionInt(), formRecibos.getTipoRegimenAcronimo(), tiposIngresoTesoreria));
//		this.cargarMapaConceptosFlujoFacturas(formRecibos);
//		
		if(request.getParameter("idPaciente")!=null){
			
			formRecibos.setIdentificacionPaciente(request.getParameter("idPaciente"));
		
		}else{
			
			formRecibos.setIdentificacionPaciente("");
		}
		
		if(request.getParameter("tipoIdentificacion")!=null){
			
			formRecibos.setTipoIdentificacion(request.getParameter("tipoIdentificacion"));
		
		}else{
			
			formRecibos.setTipoIdentificacion("");
		}
		
		formRecibos.setMapaConceptosFlujoAbonosAgenda(UtilidadValidacion.getCodigoDescripcionConceptoTipoRegimen(con, usuario.getCodigoInstitucionInt(), formRecibos.getTipoRegimenAcronimo(), tiposIngresoTesoreria, true));
		this.cargarMapaConceptosFlujoAbonosAgenda(formRecibos);
		
		//formRecibos.setMapaConceptos("codigo_concepto_"+formRecibos.getPosSelConcepto(), ConstantesBD.codigoTipoIngresoTesoreriaAbonos);
		
		//accionConcepto(con, formRecibos, mapping, mundoRecibos, usuario);
		
		buscarPacientes(mapping, con, formRecibos, mundoRecibos, usuario);
		
		UtilidadTransaccion.getTransaccion().begin();
		formRecibos.setEntidadesFinancieras(EntidadesFinancieras.consultarEntidadesFinancieras(true));
		UtilidadTransaccion.getTransaccion().commit();
		
	}
//	
//	/**
//	 * Mï¿½todo que se encarga de abrir la conexiï¿½n con la base
//	 * de datos
//	 * @param con
//	 * @return
//	 */
//	private Connection abrirConexion(Connection con) {
//		try 
//		{
//			if(con==null)
//				con = openDBConnection(con);
//			else if(con.isClosed())
//				con = openDBConnection(con);
//		} 
//		catch (SQLException e) 
//		{
//			e.printStackTrace();
//		}
//		return con;
//	}

	/**
	 * Mï¿½todo que carga el mapa de conceptos para el generar el recibo de caja
	 * desde la Agenda Odontolï¿½gica.
	 * 
	 * @param forma
	 */
	private void cargarMapaConceptosFlujoAbonosAgenda(RecibosCajaForm forma)
	{
		forma.setMapaConceptos("tipoBeneficiario_1", ConstantesBD.codigoNuncaValido);
		forma.setMapaConceptos("poseeRegistroSeleccionado_1", false);
		forma.setMapaConceptos("numReg", "1");
		forma.setMapaConceptos("poseeRegistroSeleccionado_0", false);
		forma.setMapaConceptos("readOnlyValoConcepto_0", true);
		forma.setMapaConceptos("codigoTipoConcepto_0", forma.getMapaConceptosFlujoAbonosAgenda("codigo_tipo_ingreso_0"));
		forma.setMapaConceptos("codigoTipoConcepto_1",ConstantesBD.codigoNuncaValido);
		forma.setMapaConceptos("nombreBeneficiario_1", "");
		forma.setMapaConceptos("nombreBeneficiario_0", "");
		forma.setMapaConceptos("valor_0", "");
		forma.setMapaConceptos("valor_1", "");
		forma.setMapaConceptos("esEditable_0", true);
		forma.setMapaConceptos("esEditable_1", false);
		forma.setMapaConceptos("numeroBeneficiario_1", "");
		forma.setMapaConceptos("numeroBeneficiario_0", "");
		forma.setMapaConceptos("docSoporte_1", "");
		forma.setMapaConceptos("nombreTipoBeneficiario_1", "Seleccione");
		forma.setMapaConceptos("docSoporte_0", "");
		forma.setMapaConceptos("mostrarCampos_0", true);
		forma.setMapaConceptos("mostrarCampos_1", false);
		forma.setMapaConceptos("valorConcepto_0", "");
		forma.setMapaConceptos("valorConcepto_1", "");
		forma.setMapaConceptos("descripcion_concepto_0", forma.getMapaConceptosFlujoAbonosAgenda("descripcion_concepto_0"));
		forma.setMapaConceptos("codigo_concepto_0", forma.getMapaConceptosFlujoAbonosAgenda("codigo_concepto_0"));
		forma.setMapaConceptos("codigo_concepto_1", ConstantesBD.codigoNuncaValido);
		forma.setMapaConceptos("descripcion_concepto_1","Seleccione");
		forma.setMapaConceptos("yaGeneroNuevoRegistro_1", false);
		forma.setMapaConceptos("yaGeneroNuevoRegistro_0", true);
		forma.setMapaConceptos("tipoBeneficiario_0", ConstantesBD.codigoNuncaValido);
	
	}
	
	@SuppressWarnings("unchecked")
	private String imprimirReciboCaja(UsuarioBasico usuario, InstitucionBasica  institucion, String numReciboCaja, String identificacionPaciente, String tipoIdentificacion, String observacionesImprimir,String consecutivorc) {
		
//		String nombreRptDesign="";
//		//InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
//		Vector v;
//		//String rutaArchivoPlano="";
//		String newPathReport = "";
//		
//		String formatoImpresion=ValoresPorDefecto.getTamanioImpresionRC(usuario.getCodigoInstitucionInt());
//		if (formatoImpresion.equals(ConstantesIntegridadDominio.acronimoFormatoImpresionRCPOS))
//			nombreRptDesign="ReciboCajaPOS.rptdesign";
//		else if (formatoImpresion.equals(ConstantesIntegridadDominio.acronimoFormatoImpresionRCCarta))
//			nombreRptDesign="ReciboCajaCarta.rptdesign";
//		
//		// ***************** INFORMACIï¿½N DEL CABEZOTE
//		DesignEngineApi comp;
//		comp = new DesignEngineApi(ParamsBirtApplication.getReportsPath()+ "tesoreria/", nombreRptDesign);
//
//		// Logo
//		if (formatoImpresion.equals(ConstantesIntegridadDominio.acronimoFormatoImpresionRCCarta))
//		{
//			comp.insertImageHeaderOfMasterPage1(0, 0, institucion.getLogoReportes());
//			// Nombre Instituciï¿½n, titulo 
//			comp.insertGridHeaderOfMasterPageWithName(0, 1, 1, 2, "titulo");
//
//			v = new Vector<String>();
//			
//			String digitoVerificacion = !UtilidadTexto.isEmpty(institucion.getDigitoVerificacion()) ? " - " + institucion.getDigitoVerificacion() : "";
//			
//			v.add(institucion.getRazonSocial()+ " NIT " + institucion.getNit() + digitoVerificacion);
//			
//			comp.insertLabelInGridOfMasterPage(0, 1, v);
//
//			// Fecha hora de proceso y usuario
//			comp.insertLabelInGridPpalOfFooter(0, 0, "Fecha: "+ UtilidadFecha.getFechaActual() + " Hora:"+ UtilidadFecha.getHoraActual());
//			comp.insertLabelInGridPpalOfFooter(0, 1, "Usuario:"+ usuario.getLoginUsuario());
//			// ****************** FIN INFORMACIï¿½N DEL CABEZOTE"
//		}
//
//		
//		// ***************** NUEVAS CONSULTAS DEL REPORTE
//		String newquery = "";
//		
//		
//		//Obtengo la neuva consulta de conceptos rc
//		comp.obtenerComponentesDataSet("conceptosRC");
//		newquery=ConsultasBirt.impresionConceptosReciboCaja(usuario.getCodigoInstitucionInt(),numReciboCaja);
//		comp.modificarQueryDataSet(newquery);
//		
//		//Obtengo la nueva consulta de pagos RC
//		comp.obtenerComponentesDataSet("pagosRC");
//		newquery=ConsultasBirt.impresionTotalesReciboCaja(usuario.getCodigoInstitucionInt(),numReciboCaja);
//		comp.modificarQueryDataSet(newquery);
//  
//		comp.lowerAliasDataSet(); 			
//		newPathReport = comp.saveReport1(false);
//		comp.updateJDBCParameters(newPathReport);
//		
//		
//		//Consulto el total para luego convertirlo en letras
//		ConsultaRecibosCaja mundoConsultaRecibos=new ConsultaRecibosCaja();
//		HashMap<String, Object> mapaTotalPagos = mundoConsultaRecibos.consultarTotalesPagos(usuario.getCodigoInstitucionInt(),numReciboCaja);
//		double totalPagos=0;
//		
//		for(int i=0;i<Utilidades.convertirAEntero(mapaTotalPagos.get(("numRegistros"))+"");i++)
//			totalPagos+=Utilidades.convertirADouble(mapaTotalPagos.get("valor_"+i)+"");
//		
//		//FIXME La unidad Decimales no debe pasarse de esta manera.
//		DtoTiposMoneda tipoMoneda=UtilConversionMonedas.obtenerTipoMonedaManejaInstitucion(usuario.getCodigoInstitucionInt());
//		String monedaLetras=UtilidadN2T.convertirLetras(UtilidadTexto.formatearValores(totalPagos+"" ).replaceAll(",", ""),tipoMoneda.getDescripcion(),"centavos MCTE");
//		
//
//		
//		//Envio los elementos que se necesitan mostrar por parametro
//		newPathReport += 	"&recibocaja="+numReciboCaja+
//							"&fechaemision="+UtilidadFecha.getFechaActual()+
//							"&horaemision="+UtilidadFecha.getHoraActual()+
//							"&caja="+usuario.getDescripcionCaja()+
//							"&estado="+(Utilidades.obtenerEstadoReciboCaja(numReciboCaja,usuario.getCodigoInstitucionInt())).split(ConstantesBD.separadorSplit)[1]+
//							"&recibidode="+Utilidades.reciboCajaRecibidoDe(usuario.getCodigoInstitucionInt(),numReciboCaja)+
//							"&tipoid="+ tipoIdentificacion+
//							"&nroid="+ identificacionPaciente+
//							"&observaciones="+ observacionesImprimir+
//							"&usuariogenero="+usuario.getNombreUsuario()+
//							"&totalletras="+monedaLetras;
//		
//		
//		return newPathReport;
		
		
		IRecibosCajaServicio recibosCajaServicio = TesoreriaFabricaServicio.crearRecibosCajaServicio();
		
		HashMap<String, String> parametros = new HashMap<String, String>();
		
		parametros.put("numReciboCaja", numReciboCaja);
		parametros.put("consecutivorc", consecutivorc);
		
		parametros.put("identificacionPaciente", identificacionPaciente);
		parametros.put("tipoIdentificacion", tipoIdentificacion);
		parametros.put("observacionesImprimir", observacionesImprimir);
		parametros.put("usuarioElabora", usuario.getNombreUsuario());
		parametros.put("funcionalidadOrigen", "RecibosCaja");
		
		return recibosCajaServicio.imprimirReciboCaja(usuario, institucion, parametros); 
		
	}
	
	
	/**
	 * 
	 * Mï¿½todo que se encarga de realizar la impresiï¿½n de la factura varia que se acaba de
	 * generar.
	 * 
	 * @param formRecibos
	 * @param codigoInstitucion
	 * @param institucion
	 * @param request
	 * @return
	 */
	private String imprimirFacturaVaria(RecibosCajaForm formRecibos, int codigoInstitucion, String institucion, HttpServletRequest request, UsuarioBasico usuario) {		
			
		GenModFacturasVarias genModFacturasVariasMundo = new GenModFacturasVarias();
		
		//Siempre la informacion del consecutivo de la factura varia viene con esta clave
		Object valor = formRecibos.getMapaConceptos().get("docSoporte_0");
		
		String codigoFacturaVaria = valor!=null ? valor + "" : "";
		
		if(UtilidadTexto.isNumber(codigoFacturaVaria)){
			
			String newPathReport = genModFacturasVariasMundo.imprimirFacturaVaria(codigoInstitucion, Utilidades.convertirAEntero(institucion), 
										Utilidades.convertirAEntero(codigoFacturaVaria), usuario);
		   
			if(!newPathReport.equals(""))
	        {
	        	request.setAttribute("isOpenReport", "true");
	        	request.setAttribute("newPathReport", newPathReport);
	        }
		}

		formRecibos.setEstado("resumen");
		return "paginaPrincipal";
	}

}
