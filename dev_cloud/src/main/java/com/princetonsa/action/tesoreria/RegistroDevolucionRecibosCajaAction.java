/**
 * 
 */
package com.princetonsa.action.tesoreria;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.tesoreria.AprobacionAnulacionDevolucionesForm;
import com.princetonsa.actionform.tesoreria.RegistroDevolucionRecibosCajaForm;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoBeneficiarioCliente;
import com.princetonsa.dto.odontologia.DtoVentaTarjetasCliente;
import com.princetonsa.dto.tesoreria.DtoConsolidadoMovimiento;
import com.princetonsa.dto.tesoreria.DtoCuadreCaja;
import com.princetonsa.dto.tesoreria.DtoFormaPago;
import com.princetonsa.dto.tesoreria.DtoProcesoDevolucionRC;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.Paciente;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.AbonosYDescuentos;
import com.princetonsa.mundo.odontologia.BeneficiariosTarjetaCliente;
import com.princetonsa.mundo.tesoreria.AnulacionRecibosCaja;
import com.princetonsa.mundo.tesoreria.AprobacionAnulacionDevoluciones;
import com.princetonsa.mundo.tesoreria.RecibosCaja;
import com.princetonsa.mundo.tesoreria.RegistroDevolucionRecibosCaja;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IFormasPagoMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IMovimientosCajaMundo;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.RecibosCajaId;
import com.servinte.axioma.orm.RecibosCajaXTurno;
import com.servinte.axioma.orm.TurnoDeCaja;
import com.servinte.axioma.orm.delegate.tesoreria.CajasDelegate;
import com.servinte.axioma.orm.delegate.tesoreria.RecibosCajaDelegate;
import com.servinte.axioma.orm.delegate.tesoreria.RecibosCajaXTurnoDelegate;
import com.servinte.axioma.orm.delegate.tesoreria.TurnoDeCajaDelegate;
import com.servinte.axioma.persistencia.UtilidadPersistencia;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

/** 
 * @author Jorge ARmando Osorio Velasquez
 *
 */
public class RegistroDevolucionRecibosCajaAction extends Action 
{
	
	/**
	 * 
	 */
	private static final String[] indices={"valor_","fechahoraelaboracion_","consecutivorc_","numerorc_","descestado_","codigoestado_","beneficiario_","concepto_","docsoporte_","idbeneficiario_","numidbeneficiario_","fechaoriginal_"};

	/**
	 * Mï¿½todo execute del action
	 */
	@SuppressWarnings("unchecked")
	public ActionForward execute(	ActionMapping mapping, 	
															ActionForm form, 
															HttpServletRequest request, 
															HttpServletResponse response) throws Exception
															{

		Connection con= null;
		try{
			if(form instanceof RegistroDevolucionRecibosCajaForm)
			{
				RegistroDevolucionRecibosCajaForm forma=(RegistroDevolucionRecibosCajaForm) form;
				UsuarioBasico usuario = getUsuarioBasicoSesion(request.getSession());
				ActionErrors errores = new ActionErrors(); 

				//if(usuario.getConsecutivoCaja()==ConstantesBD.codigoNuncaValido)
				String estado=forma.getEstado();
				if(estado.equals("empezarPorCajas"))
				{
					if(Utilidades.numCajasAsociadasUsuarioXcentroAtencion(usuario.getLoginUsuario(), usuario.getCodigoCentroAtencion()+"")>0)
					{
						//Buscar el turno asociado al usuario en sesión
						ArrayList<TurnoDeCaja> listaTurnos=new TurnoDeCajaDelegate().obtenerTurnoCajaAbiertoPorCajero(usuario);
						if(listaTurnos==null || listaTurnos.isEmpty())
						{
							request.setAttribute("codigoDescripcionError", "error.recibosCaja.turnoCaja");
							return mapping.findForward("paginaError");
						}
						else{
							usuario.setConsecutivoCaja(listaTurnos.get(0).getCajas().getConsecutivo());
							estado="empezar";
						}
					}
					else
					{
						request.setAttribute("codigoDescripcionError", "errors.faltaDefinirCajaParaUsuario");
						return mapping.findForward("paginaError");
					}
				}

				ActionForward forward=validacionAcceso(usuario, request, mapping);
				if(forward!=null)
					return forward;


				forma.setMostrarMensaje(new ResultadoBoolean(false,""));
				if(Utilidades.obtenerTipoCaja(usuario.getConsecutivoCaja())!=ConstantesBD.codigoTipoCajaRecaudado)
				{ 
					errores.add("La caja del usuario, no es de Recaudo", new ActionMessage("error.recibosCaja.cajaNoRecuado"));
					saveErrors(request, errores);
					return mapping.findForward("paginaErroresActionErrors");
				}

				//forma.setPaginaRetornaImpresion("");

				Log4JManager.info("estado->"+estado);
				if(estado.equals("empezar"))
				{
					con=UtilidadBD.abrirConexion();
					String conRecibo=UtilidadBD.obtenerValorActualTablaConsecutivos(con,ConstantesBD.nombreConsecutivoDevolucionReciboCaja,usuario.getCodigoInstitucionInt());
					if(conRecibo.equals(ConstantesBD.codigoNuncaValido+"") || conRecibo.trim().equals(""))
					{
						errores.add("FALTA CONSECUTIVO RECIBOS CAJA", new ActionMessage("error.DevolucionRecibosCaja.faltaDefinirConsecutivoDevolucionReciboCaja"));
						saveErrors(request, errores);
						UtilidadBD.closeConnection(con);			
						return mapping.findForward("paginaErroresActionErrors"); 
					}
					else
					{
						forma.reset();

						IFormasPagoMundo formasPagoMundo = TesoreriaFabricaMundo.crearFormasPagoMundo();
						DtoFormaPago filtros=new DtoFormaPago();

						int formaPagoEfectivo=ConstantesBD.codigoNuncaValido;
						String formaPagoEfStr=ValoresPorDefecto.getFormaPagoEfectivo(usuario.getCodigoInstitucionInt());
						if(!UtilidadTexto.isEmpty(formaPagoEfStr))
						{
							formaPagoEfectivo=Integer.parseInt(formaPagoEfStr);
						}
						filtros.setConsecutivo(formaPagoEfectivo);
						ArrayList<DtoFormaPago> listadoFormasPago = (ArrayList<DtoFormaPago>)formasPagoMundo.obtenerFormasPagos(filtros);
						UtilidadTransaccion.getTransaccion().commit();
						forma.setListaFormasPago(listadoFormasPago);
						UtilidadBD.closeConnection(con);			
						return mapping.findForward("principal");   
					}
				}
				else if(estado.equals("busquedaAvanzada"))
				{
					forma.setMostrarSeccionBusquedaAvanzada(!forma.isMostrarSeccionBusquedaAvanzada());
					forma.resetCamposBusqueda();
					forma.setCentroAtencionBusqueda(usuario.getCodigoCentroAtencion()+"");
					return mapping.findForward("principal"); 
				}
				else if(estado.equals("ejecutarBusqueda"))
				{

					return ejecutarBusqueda(mapping, request, forma, usuario); 
				}
				else if(estado.equals("ordenar"))
				{
					this.accionOrdenar(forma);
					return mapping.findForward("resultadoBusqueda");
				}
				else if(estado.equals("cargarReciboCaja"))
				{
					return cargarReciboCaja(forma,usuario,mapping,request);				

				}
				else if(estado.equals("guardarDevolucion"))
				{
					//Buscar el turno asociado al usuario en sesión
					ArrayList<TurnoDeCaja> listaTurnos=new TurnoDeCajaDelegate().obtenerTurnoCajaAbiertoPorCajero(usuario);
					if(listaTurnos!=null && !listaTurnos.isEmpty())
					{
						/* Solamente puede existir un turno de caja abierto*/
						TurnoDeCaja turno=listaTurnos.get(0);
						// Solamente se guarda en el caso de que sea una caja que requiere turno abierto 
						if(turno!=null)
						{
							if(forma.isAprobarDevolucion()){
								if(!validacionSaldoDisponibleTurno(forma, turno, usuario)){
									errores.add("Saldo Insuficiente efectivo", new ActionMessage("error.devolucionrecibosCaja.saldoInsuficiente"));
									saveErrors(request, errores);
									HibernateUtil.endTransaction();
									return mapping.findForward("principal");
								}
							}
							this.guardarDevolucionReciboCaja(forma,usuario,request, turno);
						}
						else{
							errores.add("La caja no tiene un turno", new ActionMessage("La caja del usaurio no tiene definido un turno"));
							saveErrors(request, errores);
						}
					}
					else{
						errores.add("La caja no tiene un turno", new ActionMessage("La caja del usaurio no tiene definido un turno"));
						saveErrors(request, errores);
					}

					if(Utilidades.convertirAEntero(forma.getRecibosCaja("numRegistros")+"")==1)
					{
						forma.reset();
						forma.setPaginaRetornaImpresion("principal");
						HibernateUtil.endTransaction();
						return mapping.findForward("principal");
					}
					HashMap<String, Object> tempo=new HashMap<String, Object>();
					tempo=(HashMap<String, Object> )forma.getRecibosCaja().clone();
					/*
					 * Según Tarea 2329 
					 * forma.reset();
				forma.setRecibosCaja((HashMap<String, Object> )tempo.clone());
					 */


					forma.resetSinBusqueda();
					Log4JManager.info(">>> Voy a ejecutar el método ejecutarBusqueda");

					forma.setPaginaRetornaImpresion("resultadoBusqueda");

					HibernateUtil.endTransaction();
					
					return ejecutarBusqueda(mapping, request, forma, usuario);

					/*
					 * forma.setPaginaRetornaImpresion("resultadoBusqueda");
				return mapping.findForward("resultadoBusqueda");  
					 */

				}
				else if(estado.equals("imprimirDevolucion"))
				{
					con=UtilidadBD.abrirConexion();
					return this.accionImprimirReporte(con, forma, mapping, request, usuario);
				}

				else if(forma.getEstado().equals("procesoCancelado"))
				{
					return accionProcesoCancelado(forma, mapping, response, request.getSession().getId(), usuario);
				}

				else if(forma.getEstado().equals("generacionAprobacion")){

					//FIXME 
					con=UtilidadBD.abrirConexion();
					if(usuario.obtenerFuncionalidadPorusuario(con, usuario.getLoginUsuario(), ConstantesBD.aprobacionAnulacionDevolucionRecibos )){
						UtilidadBD.cerrarConexion(con);
						forma.setEstado("abrirPagGeneracionAprobacion");
						return mapping.findForward("registroDevolucion");
					}else{



						forma.setAprobarDevolucion(false);

						//Buscar el turno asociado al usuario en sesión
						ArrayList<TurnoDeCaja> listaTurnos=new TurnoDeCajaDelegate().obtenerTurnoCajaAbiertoPorCajero(usuario);
						if(listaTurnos!=null && !listaTurnos.isEmpty())
						{
							/* Solamente puede existir un turno de caja abierto*/
							TurnoDeCaja turno=listaTurnos.get(0);
							// Solamente se guarda en el caso de que sea una caja que requiere turno abierto 
							if(turno!=null)
							{
								if(forma.isAprobarDevolucion()){
									if(!validacionSaldoDisponibleTurno(forma, turno, usuario)){
										errores.add("Saldo Insuficiente efectivo", new ActionMessage("error.devolucionrecibosCaja.saldoInsuficiente"));
										saveErrors(request, errores);
										return mapping.findForward("principal");
									}
								}
								this.guardarDevolucionReciboCaja(forma,usuario,request, turno);
							}
							else{
								errores.add("La caja no tiene un turno", new ActionMessage("La caja del usaurio no tiene definido un turno"));
								saveErrors(request, errores);
							}
						}
						else{
							errores.add("La caja no tiene un turno", new ActionMessage("La caja del usaurio no tiene definido un turno"));
							saveErrors(request, errores);
						}

						if(Utilidades.convertirAEntero(forma.getRecibosCaja("numRegistros")+"")==1)
						{
							forma.reset();
							forma.setPaginaRetornaImpresion("principal");
							return mapping.findForward("principal");
						}
						HashMap<String, Object> tempo=new HashMap<String, Object>();
						tempo=(HashMap<String, Object> )forma.getRecibosCaja().clone();
						/*
						 * Según Tarea 2329 
						 * forma.reset();
					forma.setRecibosCaja((HashMap<String, Object> )tempo.clone());
						 */


						forma.resetSinBusqueda();
						Log4JManager.info(">>> Voy a ejecutar el método ejecutarBusqueda");

						forma.setPaginaRetornaImpresion("resultadoBusqueda");

						return ejecutarBusqueda(mapping, request, forma, usuario);

						/*
						 * forma.setPaginaRetornaImpresion("resultadoBusqueda");
					return mapping.findForward("resultadoBusqueda");  
						 */




					}
				}




				else
				{
					Log4JManager.warning("Estado no valido dentro del flujo de RecibosCajaAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				Log4JManager.error("El form no es compatible con el form de RegistroDevolucionRecibosCajaForm");
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
	 * Método encargado de ejecutar la búsqueda
	 * @param mapping
	 * @param request
	 * @param forma
	 * @param usuario
	 * @return
	 */
	
	private ActionForward ejecutarBusqueda(
			ActionMapping mapping,
			HttpServletRequest request,
			RegistroDevolucionRecibosCajaForm forma,
			UsuarioBasico usuario)
	{
		RegistroDevolucionRecibosCaja mundo=new RegistroDevolucionRecibosCaja();
		forma.setRecibosCaja(mundo.consultarRecibosCaja(cargarValueObjectConsulta(forma,usuario)));
		if(Utilidades.convertirAEntero(forma.getRecibosCaja("numRegistros")+"")==1)
		{
			int codEstado=Utilidades.convertirAEntero(forma.getRecibosCaja("codigoestado_0")+"");
			//ConstantesBD.codigoEstadoReciboCajaEnArqueo+","+ConstantesBD.codigoEstadoReciboCajaRecaudado+","+ConstantesBD.codigoEstadoReciboCajaEnCierre
			if(codEstado!=ConstantesBD.codigoEstadoReciboCajaEnArqueo && codEstado!=ConstantesBD.codigoEstadoReciboCajaRecaudado && codEstado!=ConstantesBD.codigoEstadoReciboCajaEnCierre)
			{
				ActionErrors errores=new ActionErrors();
				errores.add("", new ActionMessage("error.errorEnBlanco", "Recibo de caja "+forma.getNumeroReciboCajaBusqueda()+" en estado "+forma.getRecibosCaja("descestado_0")+". No se permite su Devolucion."));
				saveErrors(request, errores);
				return mapping.findForward("principal");
			}
			forma.setIndiceReciboCaja(0);
			
			Connection con=UtilidadBD.abrirConexion();
			AnulacionRecibosCaja anulacion = new AnulacionRecibosCaja();
			int numeroContrato= anulacion.esReciboCajaXConceptAnticipConvenioOdonto(con, forma.getNumeroReciboCajaBusqueda());
			if(numeroContrato > 0) {
				if(!anulacion.validarValorReciboCajaVsValorAnticipoDispContr(con, numeroContrato, forma.getRecibosCaja("valor_0")+"")) {
					ActionErrors error=new ActionErrors();
					error.add("", new ActionMessage("errors.notEspecific", "No se puede devolver el recibo de caja "+anulacion.getNumeroReciboCaja()+" por que No tiene Saldo Disponible. "));
					saveErrors(request,error);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
			}	
			UtilidadBD.closeConnection(con);
			
			// Paso todas las validaciones
			return cargarReciboCaja(forma,usuario,mapping,request);
		}
		if(Utilidades.convertirAEntero(forma.getRecibosCaja("numRegistros")+"")==0)
		{
			ActionErrors errores=new ActionErrors();
			errores.add("", new ActionMessage("error.errorEnBlanco", "La Busqueda no arrojo resultados."));
			saveErrors(request, errores);
			return mapping.findForward("principal");
		}
		return mapping.findForward("resultadoBusqueda");
	}
	
	/**
	 * 
	 * @param forma
	 * @param usuario
	 */
	private boolean guardarDevolucionReciboCaja(RegistroDevolucionRecibosCajaForm forma, UsuarioBasico usuario, HttpServletRequest request, TurnoDeCaja turno) 
	{
		HashMap<String, Object> vo=new HashMap<String, Object>();
		String consecutivo=UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoDevolucionReciboCaja, usuario.getCodigoInstitucionInt());
		if (!(Utilidades.convertirAEntero(consecutivo) > 0)) {
			ActionErrors errores=new ActionErrors();
			errores.add("falta definir consecutivo del recibo de caja", new ActionMessage("errors.consecutivoNoDefinido","Devolucion Recibos Caja"));
			saveErrors(request, errores);
		
		}
		
		if(Utilidades.convertirAEntero(consecutivo)>0)
		{
			vo.put("estado", ConstantesIntegridadDominio.acronimoEstadoGenerado);
			if(forma.isAprobarDevolucion())
				vo.put("estado", ConstantesIntegridadDominio.acronimoEstadoAprobado);
			vo.put("usuarioGenera", usuario.getLoginUsuario());
			vo.put("consecutivo",consecutivo);
			vo.put("concepto", forma.getConcepto());
			vo.put("numeroRC", forma.getReciboCaja());
			vo.put("caja", usuario.getConsecutivoCaja()+"");
			vo.put("docSoporte",forma.getDocumentoSoporte());
			vo.put("motivoDevolucion", forma.getMotivoDevolucion());
			vo.put("valorDevolucion",forma.getValorDevolucion());
			vo.put("formaPago",forma.getFormaPago());
			vo.put("observaciones",forma.getObservaciones());
			vo.put("institucion", usuario.getCodigoInstitucionInt()+"");
			vo.put("realizarope", forma.getEliminarRC());
			vo.put("turnoCaja", turno.getCodigoPk());
			
			RegistroDevolucionRecibosCaja mundo=new RegistroDevolucionRecibosCaja();
			if(mundo.guardarDevolucion(vo))
			{
				Log4JManager.info("se ha guardado la devolucion ");
				boolean resultado=true;
				forma.setConsecutivoDevolucion(consecutivo);
				if(forma.isAprobarDevolucion())
				{
					
					if(Utilidades.convertirAEntero(forma.getTipoConcepto())==ConstantesBD.codigoTipoIngresoTesoreriaAnticipoConvenioOdon)
					{
						Connection con=UtilidadBD.abrirConexion();
						AprobacionAnulacionDevoluciones mundo2=new AprobacionAnulacionDevoluciones();
						forma.setValorTotalReciboCaja(ConstantesBD.codigoNuncaValidoDouble);
						forma.setValorAnticRecConv(ConstantesBD.codigoNuncaValidoDouble);
						
						
						Log4JManager.info("Es de tipo Validaciones Anticipos Convenios Odontologia" );
						forma.setValidacionAnticiposConveOdontologia(true);
						ResultSetDecorator rs1=null;
						String numeroReciboCaja = forma.getReciboCaja()+"";					 
						int valorContrato= mundo2.esReciboCajaXConceptAnticipConvenioOdonto(con, numeroReciboCaja);
						double valorReciboCaja=ConstantesBD.codigoNuncaValidoDouble;
						
						
						if(valorContrato > 0)
						{							
							Log4JManager.info("ENTRO a VALIDAR  ReciboCajaXConceptAnticipConvenioOdonto ");
							try{
								rs1 = mundo2.consultarValorConceptosRecibosCaja(con, numeroReciboCaja, usuario.getCodigoInstitucionInt());
								
								while(rs1.next())
								{
									valorReciboCaja=valorReciboCaja + rs1.getDouble("valorconceptorc");
								}
							}catch(SQLException e){
								
							}
							forma.setValorTotalReciboCaja(valorReciboCaja);
							double valorAnticRecConv=ConstantesBD.codigoNuncaValidoDoubleNegativo;
							valorAnticRecConv=Utilidades.convertirADouble(mundo2.validarValorReciboCajaVsValorAnticipoDispContr(con, valorContrato,valorReciboCaja+"").get("valorAntConv")+"");
							
							
							if(valorAnticRecConv <= 0)
							{		
								Log4JManager.info("NOOOOO Continua de Validacion Anticipos.... ValorAnticipo "+valorAnticRecConv);  
								forma.setMostrarMensaje(new ResultadoBoolean(true,"No se puede aprobar devolución del recibo de caja "+numeroReciboCaja+" por que No tiene Saldo Disponible. "));								
								resultado= false;
							}
							else
							{   
								Log4JManager.info("Continua despues de Validacion Anticipos.... ValorAnticipo"+valorAnticRecConv);  
								forma.setValorAnticRecConv(valorAnticRecConv);						
								forma.setExisteReciboCajaXConceptAnticipConvenioOdonto(true);		   	
								forma.setNumContratoOdontologia(valorContrato);
							} 					
						}
						
						UtilidadBD.closeConnection(con);
					}
					if(resultado)
					{	
						resultado=acutalizacionesPorAprobacion(forma,mundo,usuario);
					}
					
					if(resultado)
					{
						// Aprobacion de una devolucion de recibos de caja de concepto Facturas Varias
						if(	!forma.getValorConceptoIngTes().equals(""))
						{
							if(forma.getValorConceptoIngTes().equals(ConstantesIntegridadDominio.acronimoFactura))
							{								
								Log4JManager.info("************* EL TIPO FAXCTURA ES *************** "+ forma.getTipoConcepto());								
								
								int result = 0 ;
								AnulacionRecibosCaja obj = new AnulacionRecibosCaja();
								Connection con=UtilidadBD.abrirConexion();
								result = obj.anularReciboCajaFacturasVarias(con, forma.getReciboCaja(),usuario);
								if(result == 0 || result==ConstantesBD.codigoNuncaValido)
									resultado = false;
								UtilidadBD.closeConnection(con);
								
							}
							
							
							
						}
						
						if(forma.getValorConceptoIngTes().equals(ConstantesIntegridadDominio.acronimoFactura) && forma.getTipoConcepto().equals("3")){
							Log4JManager.info("\n\n\n\n\n\n\n\n\n\n");
							Log4JManager.info("*****************************************************************************************************************************");
							Log4JManager.info(" CAMBIANDO EL ESTADO DEL BENEFICIARIO ");
							Log4JManager.info("********************** ENTRO A BUSCAR LAS VENTAS***********************************");	
							ArrayList<DtoBeneficiarioCliente> listaBeneficiarios = new ArrayList<DtoBeneficiarioCliente>();
							
							Log4JManager.info("EL RECIBO DE CAJA ES ***********************"+forma.getReciboCaja());
							ArrayList<DtoVentaTarjetasCliente> listaVentas = mundo.consultaFacturas(forma.getReciboCaja());
							
							
							DtoBeneficiarioCliente dtoWhereBeneficiario=new DtoBeneficiarioCliente();
							Log4JManager.info("EL NUMERO DE VENTAS ES ****************************"+ listaVentas.size());
							for(DtoVentaTarjetasCliente i : listaVentas){
							 
								dtoWhereBeneficiario = new DtoBeneficiarioCliente();
							
								dtoWhereBeneficiario.setVentaTarjetaCliente(i.getCodigoPk());
								
								DtoBeneficiarioCliente dtoNuevoBeneficiario = new DtoBeneficiarioCliente();
								dtoNuevoBeneficiario.setEstadoTarjeta(ConstantesIntegridadDominio.acronimoEstadoInactivo);
								dtoNuevoBeneficiario.setVentaTarjetaCliente(i.getCodigoPk());
								Log4JManager.info("\n\n\n MODIFICANADO");
								BeneficiariosTarjetaCliente.modificar(dtoNuevoBeneficiario,dtoWhereBeneficiario);
								
							}
							
							Log4JManager.info("*****************************************************************************************************************************\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
							
						}
						
						//---------------------
						
					}
				}
				if(resultado)
				{
					if(forma.isAprobarDevolucion())
						forma.setMostrarMensaje(new ResultadoBoolean(true,"Devolucion Nro "+forma.getConsecutivoDevolucion()+" Realizada y Aprobada exitosamente."));
					else
						forma.setMostrarMensaje(new ResultadoBoolean(true,"Devolucion Nro "+forma.getConsecutivoDevolucion()+" Realizada exitosamente."));
					
					Connection conH=UtilidadBD.abrirConexion();
					UtilidadBD.cambiarUsoFinalizadoConsecutivo(conH, ConstantesBD.nombreConsecutivoDevolucionReciboCaja, usuario.getCodigoInstitucionInt(), consecutivo, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
					UtilidadBD.closeConnection(conH);
					
				}
				else
				{
					forma.setMostrarMensaje(new ResultadoBoolean(true,"No se pudo realizar la devolucion."));
					
					if(forma.isValidacionAnticiposConveOdontologia())
					{
					  forma.setMostrarMensaje(new ResultadoBoolean(true,"No se puede aprobar devolución del recibo de caja "+forma.getReciboCaja()+" por que No tiene Saldo Disponible. "));
					}
				}
				
				//Se cierra la concurrencia
				String nroRC=vo.get("numeroRC")+"";
				int institucion=usuario.getCodigoInstitucionInt();
				RegistroDevolucionRecibosCaja.cancelarBloqueoDevolucion(nroRC, institucion, request.getSession().getId());
				
				return resultado;
			}
			else
			{
				Connection con=UtilidadBD.abrirConexion();
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoDevolucionReciboCaja, usuario.getCodigoInstitucionInt(), consecutivo, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
				UtilidadBD.closeConnection(con);
				return false;
			}
		}
		return false;
	}


	/**
	 * 
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @return
	 */
	private boolean acutalizacionesPorAprobacion(RegistroDevolucionRecibosCajaForm forma, RegistroDevolucionRecibosCaja mundo, UsuarioBasico usuario) 
	{
		if(Utilidades.convertirAEntero(forma.getTipoConcepto())==ConstantesBD.codigoTipoIngresoTesoreriaPacientes)
		{
			Connection con=UtilidadBD.abrirConexion();
			String[] codigoPagosFacturaPaciente=Utilidades.getPagosFacturaPacienteReciboCaja(con,ConstantesBD.codigoTipoDocumentoPagosReciboCaja,forma.getReciboCaja(),usuario.getCodigoInstitucionInt());
			boolean resultado=true;
			for(int j=0;j<codigoPagosFacturaPaciente.length;j++)
			{
				resultado=Utilidades.cambiarEstadoPacienteFactura(con,Utilidades.obtenerCodigoFacturaPagosPaciente(con,Integer.parseInt(codigoPagosFacturaPaciente[j])),ConstantesBD.codigoEstadoFacturacionPacientePorCobrar);
				resultado=Utilidades.cambiarEstadoPagoFacturaPacient(con,Integer.parseInt(codigoPagosFacturaPaciente[j]),ConstantesBD.codigoEstadoPagosAnulado);
			}
			UtilidadBD.closeConnection(con);
			return resultado;
		}
		if(Utilidades.convertirAEntero(forma.getTipoConcepto())==ConstantesBD.codigoTipoIngresoTesoreriaConvenios)
		{
			Connection con=UtilidadBD.abrirConexion();
			boolean resultado=true;
			String[] codigoPagosEmpresa=Utilidades.getPagosEmpresaReciboCaja(con,ConstantesBD.codigoTipoDocumentoPagosReciboCaja,forma.getReciboCaja(),usuario.getCodigoInstitucionInt());
			for(int j=0;j<codigoPagosEmpresa.length;j++)
			{
				String[] estado=(Utilidades.obtenerEstadoPagosEmpresa(con,Integer.parseInt(codigoPagosEmpresa[j]))).split(ConstantesBD.separadorSplit);
				if(Integer.parseInt(estado[0])==ConstantesBD.codigoEstadoPagosAplicado)
				{
					//alguno de los pagos cambio de estado a aplicado en el transcurso.
					return false;
				}
				resultado=Utilidades.cambiarEstadoPagosGeneralEmpresa(con,Integer.parseInt(codigoPagosEmpresa[j]),ConstantesBD.codigoEstadoPagosAnulado);
			}
			UtilidadBD.closeConnection(con);
			return resultado;
		} 
		if(Utilidades.convertirAEntero(forma.getTipoConcepto())==ConstantesBD.codigoTipoIngresoTesoreriaAbonos)
		{
			boolean resultado=true;
			Connection con=UtilidadBD.abrirConexion();
			resultado=(AbonosYDescuentos.insertarMovimientoAbonos(con, Paciente.obtenerCodigoPersona( forma.getNumeroIDBeneficiario(), forma.getTipoIDBeneficiario()),Utilidades.convertirAEntero(forma.getConsecutivoDevolucion()),ConstantesBD.tipoMovimientoAbonoDevolucionReciboCaja,Utilidades.convertirADouble(forma.getValorDevolucion()), usuario.getCodigoInstitucionInt(), forma.getIngreso(), usuario.getCodigoCentroAtencion())>0);
			UtilidadBD.closeConnection(con);
			return resultado;
		} 
		
				
		if(Utilidades.convertirAEntero(forma.getTipoConcepto()) == ConstantesBD.codigoTipoIngresoTesoreriaAnticipoConvenioOdon)
		{
			AprobacionAnulacionDevoluciones mundo2=new AprobacionAnulacionDevoluciones();
			boolean resultado=true;
			Connection con=UtilidadBD.abrirConexion();
			Log4JManager.info("Entro a Actualizar el valor Anticipo Recibido Convenio");
			double nuevoValorAnticipoRecConv;
			nuevoValorAnticipoRecConv=forma.getValorAnticRecConv()-forma.getValorTotalReciboCaja();
			resultado = mundo2.actualizarValorAnticipo(con, forma.getNumContratoOdontologia() , nuevoValorAnticipoRecConv, usuario.getLoginUsuario());
			UtilidadBD.closeConnection(con);
			return resultado;
			
		}
		
		return true;
	}

	/**
	 * 
	 * @param forma
	 * @param usuario 
	 * @param mapping 
	 * @param request 
	 * @return
	 */
	private ActionForward cargarReciboCaja(RegistroDevolucionRecibosCajaForm forma, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) 
	{
		
		//Comienzo el proceso de concurrencia de devolución tarea 1630
		String nroRC=forma.getRecibosCaja("numerorc_"+forma.getIndiceReciboCaja())+"";
		
		
		int institucion=usuario.getCodigoInstitucionInt();
		
		DtoProcesoDevolucionRC dtoConcurrencia=RegistroDevolucionRecibosCaja.estaEnProcesoDevolucion(nroRC, institucion, request.getSession().getId(), true);
		
		if(!dtoConcurrencia.existeConcurrencia())
		{
			if(!RegistroDevolucionRecibosCaja.empezarBloqueoDevolucion(nroRC, institucion, usuario.getLoginUsuario(), request.getSession().getId()))
			{
				Log4JManager.error("\n\nCONCURRENCIA!!!!!!!");
				ActionErrors errores = new ActionErrors();
				errores.add("", new ActionMessage("errors.notEspecific","Otro usuario está usando el Recibo de Caja. Por favor inténtelo más tarde."));
				saveErrors(request, errores);
				return mapping.findForward("resultadoBusqueda");
			}
			
		}
		
		
		
		forma.setReciboCaja(forma.getRecibosCaja("numerorc_"+forma.getIndiceReciboCaja())+"");
		forma.setConsecutivoReciboCaja(forma.getRecibosCaja("consecutivorc_"+forma.getIndiceReciboCaja())+"");
		
		RegistroDevolucionRecibosCaja regDevRecCaja = new RegistroDevolucionRecibosCaja();
		forma.setValorConceptoIngTes(regDevRecCaja.getValorConceptoIngTesoreria(forma.getRecibosCaja("concepto_"+forma.getIndiceReciboCaja())+"",
				usuario.getCodigoInstitucion(), 
				ConstantesBD.codigoTipoIngresoTesoreriaOtrasCxC));
		ActionErrors errores=validacionesDevolucion(forma,usuario);
		
		if(forma.getRecibosCaja("iseliminarrc_"+forma.getIndiceReciboCaja()).toString().equals(ConstantesBD.acronimoNo))
			errores.add("", new ActionMessage("errors.notEspecific", "No se puede realizar la devolucion del recibo de caja por que existen aplicaciones de pagos. "));
		
		if(errores.isEmpty())
		{
			forma.setFechaHoraElaboracion(forma.getRecibosCaja("fechahoraelaboracion_"+forma.getIndiceReciboCaja())+"");
			forma.setNombreEstadoReciboCaja(forma.getRecibosCaja("descestado_"+forma.getIndiceReciboCaja())+"");
			forma.setBeneficiario(forma.getRecibosCaja("beneficiario_"+forma.getIndiceReciboCaja())+"");
			forma.setConcepto(forma.getRecibosCaja("concepto_"+forma.getIndiceReciboCaja())+"");
			forma.setTipoConcepto(Utilidades.obtenerTipoConceptoTesoreria(forma.getConcepto(),usuario.getCodigoInstitucionInt())+"");
			forma.setValor(UtilidadTexto.formatearValores(forma.getRecibosCaja("valor_"+forma.getIndiceReciboCaja())+"","0.######"));
			forma.setDocumentoSoporte(forma.getRecibosCaja("docsoporte_"+forma.getIndiceReciboCaja())+"");
			forma.setTipoIDBeneficiario(forma.getRecibosCaja("idbeneficiario_"+forma.getIndiceReciboCaja())+"");
			forma.setNumeroIDBeneficiario(forma.getRecibosCaja("numidbeneficiario_"+forma.getIndiceReciboCaja())+"");
			//El ingreso que debe asigarse el el correspondiente al ingreso en el
			//cual se generó el recibo de caja y no el actual MT 3223
			RecibosCaja mundoReciboCaja= new RecibosCaja();
			forma.setIngreso(mundoReciboCaja.obtenerIngresoPacientePorConsecutivoReciboCaja(forma.getConsecutivoReciboCaja()));
			
			
			forma.setMotivoDevolucion("");
			forma.setValorDevolucion(UtilidadTexto.formatearValores(forma.getRecibosCaja("valor_"+forma.getIndiceReciboCaja())+"","0.######"));
			forma.setFormaPago(ConstantesIntegridadDominio.acronimoFormaPagoEfectivo);
			forma.setEliminarRC(forma.getRecibosCaja("iseliminarrc_"+forma.getIndiceReciboCaja())+"");
			return mapping.findForward("registroDevolucion");
		}
		else
		{
			forma.setReciboCaja("");
			saveErrors(request, errores);
			if(Utilidades.convertirAEntero(forma.getRecibosCaja("numRegistros")+"")==1)
				return mapping.findForward("principal");
			return mapping.findForward("resultadoBusqueda"); 

		}
	}


	/**
	 * 
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionErrors validacionesDevolucion(RegistroDevolucionRecibosCajaForm forma, UsuarioBasico usuario) 
	{
		ActionErrors errores =new ActionErrors();
		String[] estadoRC=Utilidades.obtenerEstadoReciboCaja(forma.getReciboCaja(), usuario.getCodigoInstitucionInt()).split(ConstantesBD.separadorSplit);
		if(Utilidades.convertirAEntero(estadoRC[0])==ConstantesBD.codigoEstadoReciboCajaEnArqueo||Utilidades.convertirAEntero(estadoRC[0])==ConstantesBD.codigoEstadoReciboCajaRecaudado||Utilidades.convertirAEntero(estadoRC[0])==ConstantesBD.codigoEstadoReciboCajaEnCierre)
		{
			if(Utilidades.convertirAEntero(estadoRC[0])==ConstantesBD.codigoEstadoReciboCajaEnArqueo||Utilidades.convertirAEntero(estadoRC[0])==ConstantesBD.codigoEstadoReciboCajaRecaudado)
			{
				String[] cajeroCaja=RecibosCaja.obtenerCajaCajeroRC(forma.getReciboCaja(), usuario.getCodigoInstitucionInt()).split(ConstantesBD.separadorSplit);
				if(usuario.getConsecutivoCaja()!=Utilidades.convertirAEntero(cajeroCaja[1])||!usuario.getLoginUsuario().equals(cajeroCaja[0]))
				{
					errores.add("", new ActionMessage("error.errorEnBlanco", "El Recibo de Caja solo puede ser devuelto por el mismo cajero y caja que lo genero."));
				}
				
			}
			if( (Utilidades.reciboCajaConPagosGeneralEmpresa(forma.getReciboCaja(),usuario.getCodigoInstitucionInt()))
				 &&
				(!forma.getValorConceptoIngTes().equals(ConstantesIntegridadDominio.acronimoFactura))
			  )
			{
				errores.add("", new ActionMessage("error.errorEnBlanco", "El Recibo de Caja esta asociado a aplicacion de pagos."));
			}
		}
		else
		{
			errores.add("", new ActionMessage("error.errorEnBlanco", "El Recibo de Caja se encuentra en estado "+estadoRC[1]));
		}
		return errores;
	}

	/**
	 * @param forma
	 */
	private void accionOrdenar(RegistroDevolucionRecibosCajaForm forma) 
	{
		int numReg=Utilidades.convertirAEntero(forma.getRecibosCaja("numRegistros")+"");
		forma.setRecibosCaja(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getRecibosCaja(),numReg));
		forma.setRecibosCaja("numRegistros",numReg);
		forma.setUltimoPatron(forma.getPatronOrdenar());
	}
	
	/**
	 * 
	 * 
	 * @param forma
	 * @param usuario 
	 * @return
	 */
	private HashMap<String,Object> cargarValueObjectConsulta(RegistroDevolucionRecibosCajaForm forma, UsuarioBasico usuario) 
	{
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		mapa.put("numReciboCaja", forma.getNumeroReciboCajaBusqueda());
		mapa.put("fechaInicial", forma.getFechaInicialBusqueda());
		mapa.put("fechaFinal", forma.getFechaFinalBusqueda());
		mapa.put("tipoIDBeneficiario", forma.getTipoIDBeneficiarioBusqueda());
		mapa.put("numIDBeneficiario", forma.getNumeroIDBeneficiarioBusqueda());
		mapa.put("conceptoRC", forma.getConceptoBusqueda());
		mapa.put("estadoRC", forma.getEstadoReciboCajaBusqueda());
		mapa.put("centroAtencion", forma.getCentroAtencionBusqueda());
		mapa.put("institucion", usuario.getCodigoInstitucionInt()+"");
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward validacionAcceso(UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping)
	{
		Log4JManager.info("\n*******************************************************************************");
		Log4JManager.info("getFormaPagoEfectivo-->"+ValoresPorDefecto.getFormaPagoEfectivo(usuario.getCodigoInstitucionInt()));
		Log4JManager.info("*******************************************************************************\n");
		if(UtilidadTexto.isEmpty(ValoresPorDefecto.getFormaPagoEfectivo(usuario.getCodigoInstitucionInt())))
		{
			ActionErrors errores=new ActionErrors();
			errores.add("error.parametrosGenerales.faltaDefinirParametro", new ActionMessage("error.parametrosGenerales.faltaDefinirParametro", "<Forma Pago Efectivo>"));
			Log4JManager.warning("entra al error de [error.parametrosGenerales.faltaDefinirParametro] ");
			saveErrors(request, errores);
			return mapping.findForward("paginaErroresActionErrors");
		}
		return null;
	}

	/**
	 * 
	 * @param session
	 * @return
	 */
	private UsuarioBasico getUsuarioBasicoSesion(HttpSession session)
	{
		UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
			if(usuario == null)
				Log4JManager.warning("El usuario no esta cargado (null)");
			
			return usuario;
	}
	
	private ActionForward accionImprimirReporte(Connection con, RegistroDevolucionRecibosCajaForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario) {
		String newQuery;
		newQuery = "SELECT " +
						"dvrc.consecutivo AS codigo, " +
						"dvrc.fecha_devolucion AS fecha_devolucion, " +
						"dvrc.valor_devolucion AS valor_devolucion, " +
						"dvrc.usuario_devolucion AS usuario_devolucion, " +
						"getintegridaddominio(dvrc.estado) AS estado, " +
						"md.descripcion AS descripcion, " +
						"dtcrc.nombre_beneficiario AS nombre_beneficiario, " +
						"dtcrc.tipo_id_beneficiario AS tipo_id_beneficiario, "+
						"dtcrc.numero_id_beneficiario AS numero_id_beneficiario, " +
						"rc.consecutivo_recibo AS numero_rc, " +
						"rc.fecha AS fecha, " +
						"rc.hora AS hora, " +
						"dvrc.forma_pago AS forma_pago, " +
						"dvrc.observaciones AS observaciones, " +
						"coalesce(dvrc.fecha_aprobacion, dvrc.fecha_devolucion) AS fecha_arp_format, " +
						"dvrc.hora_aprobacion AS hora_aprobacion, " +
						"dvrc.usuario_aprobacion AS usuario_aprobacion, " +
						"cj.descripcion AS caja_devolucion, " +
						"dvrc.doc_soporte AS doc_soporte, " +
						"dvrc.fecha_anulacion AS fecha_anulacion, " +
						"dvrc.hora_anulacion AS hora_anulacion, " +
						"dvrc.usuario_anulacion AS usuario_anulacion, " +
						"dvrc.motivo_anulacion AS motivo_anulacion " +
		 "FROM devol_recibos_caja dvrc INNER JOIN recibos_caja rc ON (dvrc.numero_rc = rc.numero_recibo_caja AND dvrc.institucion = rc.institucion) " +
				"INNER JOIN detalle_conceptos_rc dtcrc ON (dtcrc.numero_recibo_caja = rc.numero_recibo_caja AND dtcrc.institucion = rc.institucion) " +
				"INNER JOIN motivos_devolucion_rc md ON (dvrc.motivo_devolucion = md.codigo) " +
				"INNER JOIN tesoreria.cajas cj ON (dvrc.caja_devolucion = cj.consecutivo) " +
				"WHERE dvrc.consecutivo = " + forma.getConsecutivoDevolucion() + "";
		
		
		String nombreRptDesign = "ConsultaImpresionDevolucion.rptdesign";

		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		//Informacion del Cabezote
		DesignEngineApi comp; 
		comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"tesoreria/",nombreRptDesign);
		comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
		comp.insertGridHeaderOfMasterPage(0,1,1,5);
		Vector v=new Vector();
		v.add(ins.getRazonSocial());
		v.add(ins.getTipoIdentificacion()+"	     "+ins.getNit());
		v.add(ins.getDireccion());
		v.add("Tels. "+ins.getTelefono());
		comp.insertLabelInGridOfMasterPage(0,1,v);
		
		
		
		comp.obtenerComponentesDataSet("ConsultaImpresionDevolucion");
		comp.modificarQueryDataSet(newQuery);
		 
		//debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
	  	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
		comp.updateJDBCParameters(newPathReport);
		
		
		if(!newPathReport.equals(""))
		{
			request.setAttribute("isOpenReport", "true");
		 	request.setAttribute("newPathReport", newPathReport);
		}
		
	   UtilidadBD.closeConnection(con);
	   return mapping.findForward(forma.getPaginaRetornaImpresion());
	}
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param response
	 * @return
	 */
	private ActionForward accionProcesoCancelado(RegistroDevolucionRecibosCajaForm forma, ActionMapping mapping, HttpServletResponse response, String idSesion, UsuarioBasico usuario) 
	{
		String nroRC=forma.getRecibosCaja("numerorc_"+forma.getIndiceReciboCaja())+"";
		int institucion=usuario.getCodigoInstitucionInt();
		
		RegistroDevolucionRecibosCaja.cancelarBloqueoDevolucion(nroRC, institucion, idSesion);
		String paginaSiguiente=forma.getSiguientePagina();
		if(!paginaSiguiente.equals(""))
		{
			try
			{
				Log4JManager.info("paginaSiguiente "+paginaSiguiente);
				response.sendRedirect(paginaSiguiente);
			}
			catch (IOException e)
			{
				Log4JManager.error("Error redireccionando a la página seleccionada "+e);
			}
		}
		return null;
	}
	
	/**
	 * Método que se encarga de realizar la validación del saldo disponible
	 * en el turno para los siguientes movimiento de caja:
	 * 
	 * Recibos de caja
	 * Anulaciones de recibos de caja
	 * Devoluciones de Recibos de caja
	 * Solicitudes de Traslados de caja realizadas y aceptadas.
	 * Entregas a transportadora de valores 
	 * Entregas a caja mayor/principal.
	 * 
	 * @param movimientosCaja
	 * @return
	 */
	private boolean validacionSaldoDisponibleTurno(RegistroDevolucionRecibosCajaForm forma, TurnoDeCaja turno, UsuarioBasico usuario) {
		
		IMovimientosCajaMundo movimientosCajaMundo = TesoreriaFabricaMundo.crearMovimientosCajaMundo();
		DtoConsolidadoMovimiento consolidado = new DtoConsolidadoMovimiento();
		boolean resultado=false;
		
		MovimientosCaja movimientoCaja = new MovimientosCaja();
		movimientoCaja.setCodigoPk(ConstantesBD.codigoNuncaValidoLong);
		movimientoCaja.setTurnoDeCaja(turno);
		consolidado= movimientosCajaMundo.obtenerConsolidadoMovimiento(movimientoCaja);
		for(DtoCuadreCaja cuadreCaja : consolidado.getCuadreCajaDTOs()){
			if(esFormaPagoEfectivo(usuario.getCodigoInstitucionInt(), cuadreCaja.getTipoFormaPago())){
				double valorDevolucion= Double.parseDouble(forma.getValorDevolucion());
				if((cuadreCaja.getValorSistema()-cuadreCaja.getValorBaseEnCaja()) >= valorDevolucion){
					resultado=true;
				}
				break;
			}
		}
		
		
		return resultado;
	}
	
	private boolean esFormaPagoEfectivo(int codigoInstitucion, int consecutivoFormaPago){
		
		boolean resultado = false;
		String formaPagoEfectivo = ValoresPorDefecto.getFormaPagoEfectivo(codigoInstitucion);
		if(UtilidadTexto.isNumber(formaPagoEfectivo)){
			int consecutivoFP = Integer.parseInt(formaPagoEfectivo);
			if(consecutivoFP == consecutivoFormaPago){
				resultado = true;
			}
		}		
		return resultado;
	}

}
