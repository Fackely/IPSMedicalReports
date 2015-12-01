package com.princetonsa.action.tesoreria;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
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
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoBeneficiarioCliente;
import com.princetonsa.dto.odontologia.DtoVentaTarjetasCliente;
import com.princetonsa.dto.tesoreria.DtoConsolidadoMovimiento;
import com.princetonsa.dto.tesoreria.DtoCuadreCaja;
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
import com.servinte.axioma.mundo.impl.tesoreria.TurnoDeCajaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IMovimientosCajaMundo;
import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.TurnoDeCaja;
import com.servinte.axioma.orm.delegate.tesoreria.TurnoDeCajaDelegate;

public class AprobacionAnulacionDevolucionesAction extends Action 
{
	
	/**
	 * objeto para manejar los logs de esta clase
	 */
	
	Logger logger =Logger.getLogger(AprobacionAnulacionDevolucionesAction.class);
	
	/**
	 * 
	 */
	private String[] indices={
								"codigodevolucion_",
								"fechadevolucion_",
								"nombrebeneficiario_",
								"tipoidbeneficiario_",
								"numidbeneficiario_",
								"motivodevol_",
								"valordevol_",
								"consecutivo_",
								"estadodevol_",
								"numerorc_"
							  };
	
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{

		Connection con=null;
		try{

			if (form instanceof AprobacionAnulacionDevolucionesForm) 
			{
				AprobacionAnulacionDevolucionesForm forma=(AprobacionAnulacionDevolucionesForm) form;

				ActionErrors errores = new ActionErrors();

				String estado=forma.getEstado();

				logger.info("Estado -->"+estado);
				
				con=UtilidadBD.abrirConexion();
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());

				AprobacionAnulacionDevoluciones mundo=new AprobacionAnulacionDevoluciones();

				forma.setMostrarMensaje(new ResultadoBoolean(false,""));

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de AprobacionAnulacionDevolucionesAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					forma.reset(usuario.getCodigoCentroAtencion()+"",usuario.getCodigoInstitucion());
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("continuar"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("busqueda");
				}
				else if(estado.equals("busquedaAvanzada"))
				{
					forma.reset(usuario.getCodigoCentroAtencion()+"",usuario.getCodigoInstitucion());
					UtilidadBD.closeConnection(con);
					return mapping.findForward("busqueda");
				}
				else if(estado.equals("buscar"))
				{
					forma.setMapaResultadoDevoluciones(mundo.BusquedaDevoluciones(con ,forma.getFechaInicial(), forma.getFechaFinal(), forma.getDevolInicial(), forma.getDevolFinal(), forma.getMotivoDevolucion(), forma.getTipoId(), forma.getNumeroId(), forma.getCentroAtencion(), forma.getCaja(), forma.getReciboCaja()));
					int numReg=Utilidades.convertirAEntero(forma.getMapaResultadoDevoluciones("numRegistros")+"");
					logger.info(">>> Número de registros = "+numReg);
					if(numReg==1)
					{	
						forma.setIndiceDetalle(0);
						int estadoRecibo=Utilidades.convertirAEntero(mundo.consultaEstadoRecibo(con, Utilidades.convertirAEntero(forma.getMapaResultadoDevoluciones().get("codigodevolucion_"+forma.getIndiceDetalle())+"")));
						int tipoConcepto= Utilidades.convertirAEntero(mundo.consultaTipoConcepto(con, Utilidades.convertirAEntero(forma.getMapaResultadoDevoluciones().get("codigodevolucion_"+forma.getIndiceDetalle())+"")));
						String documento= mundo.consultaPagoConvenio(con, Utilidades.convertirAEntero(forma.getMapaResultadoDevoluciones().get("codigodevolucion_"+forma.getIndiceDetalle())+""));

						if(estadoRecibo!=ConstantesBD.codigoEstadoReciboCajaAnulado)
						{
							if((tipoConcepto==ConstantesBD.codigoTipoIngresoTesoreriaConvenios)&&!documento.equals(""))
							{	
								errores.add("codigo", new ActionMessage("error.tesoreria.tipoPagoConvenios"));
							}
							int codigoPersona=Paciente.obtenerCodigoPersona( forma.getMapaResultadoDevoluciones().get("numidbeneficiario_"+forma.getIndiceDetalle())+"", forma.getMapaResultadoDevoluciones().get("tipoidbeneficiario_"+forma.getIndiceDetalle())+"");

							Integer ingreso=null;
							boolean controlarAbonoPaciente=UtilidadTexto.getBoolean(ValoresPorDefecto.getControlarAbonoPacientePorNroIngreso(usuario.getCodigoInstitucionInt()));
							
							try{
								if(forma.getDetalleDevolucion().get("ingreso_0")!=null)
								{
									ingreso=Integer.parseInt(forma.getDetalleDevolucion().get("ingreso_0")+"");
								}
								else{

									RecibosCaja mundoReciboCaja= new RecibosCaja();
									
									Connection con1=null;
									try {
										int pos=0;						
										mundoReciboCaja.setTipoIdentificacion(forma.getMapaResultadoDevoluciones().get("tipoidbeneficiario_0")+"");
										mundoReciboCaja.setIdentificacionPaciente(forma.getMapaResultadoDevoluciones().get("numidbeneficiario_0")+"");										
										con1=UtilidadBD.abrirConexion();
										forma.setMapaPacientes(mundoReciboCaja.generarConsultaPacientes(con1, controlarAbonoPaciente));
										ingreso = new Integer(forma.getMapaPacientes().get("ingreso_"+pos)+"");
										UtilidadBD.closeConnection(con1);								

									} catch (NumberFormatException e) {
										UtilidadBD.closeConnection(con1);
									} catch (Exception e) {			
										UtilidadBD.closeConnection(con1);
									}										
								}
							}
							catch (NumberFormatException e) {
							}
							
							if(controlarAbonoPaciente)
							{
								if((tipoConcepto==ConstantesBD.codigoTipoIngresoTesoreriaAbonos)&&(Utilidades.convertirADouble(forma.getMapaResultadoDevoluciones().get("valordevol_")+"")>Utilidades.obtenerAbonosDisponiblesPaciente(codigoPersona, ingreso, usuario.getCodigoInstitucionInt())))
								{
									errores.add("codigo", new ActionMessage("error.tesoreria.tipoAbonoPaciente"));
								}
							}
							forma.setIngreso(ingreso);

							if(errores.isEmpty() && (tipoConcepto==ConstantesBD.codigoTipoIngresoTesoreriaAbonos)&&(Utilidades.convertirADouble(forma.getMapaResultadoDevoluciones().get("valordevol_")+"")>Utilidades.obtenerAbonosDisponiblesPaciente(codigoPersona, null, usuario.getCodigoInstitucionInt())))
							{
								errores.add("codigo", new ActionMessage("error.tesoreria.tipoAbonoPaciente"));
							}

						}
						else
						{
							errores.add("codigo", new ActionMessage("error.tesoreria.estadoReciboCaja"));
						}
						if(!errores.isEmpty())
						{
							saveErrors(request, errores);
						}
						else
						{
							forma.setDetalleDevolucion(mundo.consultaDetalleDevolucion(con, Utilidades.convertirAEntero(forma.getMapaResultadoDevoluciones().get("codigodevolucion_"+forma.getIndiceDetalle())+"")));
							UtilidadBD.closeConnection(con);
							return mapping.findForward("detalle");
						}
					}
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listado");
				}
				else if(estado.equals("buscarCodigo"))
				{
					if(forma.getDevolucion().equals(""))
						errores.add("codigo", new ActionMessage("errors.required","El Consecutivo de la Devolución "));

					if(!errores.isEmpty())
					{
						saveErrors(request, errores);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("principal");
					}
					else
					{
						forma.setDetalleDevolucion(mundo.consultaDetalleDevolucionConsecutivo(con, Utilidades.convertirAEntero(forma.getDevolucion())));
						forma.setIndiceDetalle(0);
						int codigoPkDevolucion=Utilidades.convertirAEntero(forma.getDetalleDevolucion().get("numdevol_0").toString());
						int estadoRecibo= Utilidades.convertirAEntero(mundo.consultaEstadoRecibo(con, codigoPkDevolucion));
						int tipoConcepto= Utilidades.convertirAEntero(mundo.consultaTipoConcepto(con, codigoPkDevolucion));
						String documento= mundo.consultaPagoConvenio(con, Utilidades.convertirAEntero(forma.getDevolucion()));

						if(estadoRecibo!=ConstantesBD.codigoEstadoReciboCajaAnulado)
						{
							if((tipoConcepto==ConstantesBD.codigoTipoIngresoTesoreriaConvenios)&&!documento.equals(""))
								errores.add("codigo", new ActionMessage("error.tesoreria.tipoPagoConvenios"));

							Utilidades.imprimirMapa(forma.getDetalleDevolucion());

							int codigoPersona=Paciente.obtenerCodigoPersona( forma.getDetalleDevolucion().get("numidbeneficiario_0")+"", forma.getDetalleDevolucion().get("tipoidbeneficiario_0")+"");

							Integer ingreso=null;						
							boolean controlarAbonoPaciente=UtilidadTexto.getBoolean(ValoresPorDefecto.getControlarAbonoPacientePorNroIngreso(usuario.getCodigoInstitucionInt()));
							
							try{
								
//								if(forma.getMapaResultadoDevoluciones().get("ingreso_"+forma.getIndiceDetalle())!=null)
//								{
//									ingreso=Integer.parseInt(forma.getMapaResultadoDevoluciones().get("ingreso_"+forma.getIndiceDetalle())+"");
//								}else{

									/**
									 * 
									 * inc 2339
									 * Obtener el ingreso del paciente
									 * Diana Ruiz 
									 */

									RecibosCaja mundoReciboCaja= new RecibosCaja();

									Connection con1=null;
									try {
										int pos=0;						
										mundoReciboCaja.setTipoIdentificacion(forma.getDetalleDevolucion().get("tipoidbeneficiario_0")+"");
										mundoReciboCaja.setIdentificacionPaciente(forma.getDetalleDevolucion().get("numidbeneficiario_0")+"");										
										con1=UtilidadBD.abrirConexion();
										forma.setMapaPacientes(mundoReciboCaja.generarConsultaPacientes(con1, controlarAbonoPaciente));
//										String cadIngreso = forma.getMapaPacientes().get("ingreso_"+ pos) != null ? 
//												forma.getMapaPacientes().get("ingreso_"+ pos).toString() : null;
//										if(cadIngreso == null) {
											ingreso = mundoReciboCaja.obtenerIngresoPacientePorConsecutivoReciboCaja(forma.getDetalleDevolucion().get("consecutivorc_0").toString());
//										} else {
//											ingreso = new Integer(cadIngreso);
//										}
//										
										Log4JManager.info("El ingreso es ==>>" +ingreso );
										UtilidadBD.closeConnection(con1);								

									} catch (NumberFormatException e) {
										UtilidadBD.closeConnection(con1);
									} catch (Exception e) {		
										Log4JManager.error("ERROR EL NUMERO DE INGRESO DEL PACIENTE ESTA NULL", e);
										UtilidadBD.closeConnection(con1);
									}										
//								}
							}
							catch (NumberFormatException e) {
							}
								
							if(controlarAbonoPaciente)
							{
								if((tipoConcepto==ConstantesBD.codigoTipoIngresoTesoreriaAbonos)&&(Utilidades.convertirADouble(forma.getDetalleDevolucion().get("valordevol_0")+"")>Utilidades.obtenerAbonosDisponiblesPaciente(codigoPersona, ingreso, usuario.getCodigoInstitucionInt())))
								{
									errores.add("codigo", new ActionMessage("error.tesoreria.tipoAbonoPaciente"));
								}
							}						

							forma.setIngreso(ingreso);

							if(errores.isEmpty() && (tipoConcepto==ConstantesBD.codigoTipoIngresoTesoreriaAbonos)&&(Utilidades.convertirADouble(forma.getDetalleDevolucion().get("valordevol_0")+"")>Utilidades.obtenerAbonosDisponiblesPaciente(codigoPersona, null, usuario.getCodigoInstitucionInt())))
							{
								errores.add("codigo", new ActionMessage("error.tesoreria.tipoAbonoPaciente"));
							}

						}
						else
						{
							errores.add("codigo", new ActionMessage("error.tesoreria.estadoReciboCaja"));
						}

						if(!errores.isEmpty())
							saveErrors(request, errores);
						else
						{
							UtilidadBD.closeConnection(con);						
							return mapping.findForward("detalle");
						}

						forma.reset(usuario.getCodigoCentroAtencion()+"",usuario.getCodigoInstitucion());					
						UtilidadBD.closeConnection(con);
						return mapping.findForward("principal");
					}
				}
				else if(estado.equals("nuevaBusqueda"))
				{
					forma.reset(usuario.getCodigoCentroAtencion()+"",usuario.getCodigoInstitucion());
					UtilidadBD.closeConnection(con);
					return mapping.findForward("busqueda");
				}
				else if(estado.equals("detalleDevolucion"))
				{
					forma.setDetalleDevolucion(mundo.consultaDetalleDevolucion(con, Utilidades.convertirAEntero(forma.getIndiceDevolucion())));

					int estadoRecibo= Utilidades.convertirAEntero(mundo.consultaEstadoRecibo(con, Utilidades.convertirAEntero(forma.getIndiceDevolucion())));
					int tipoConcepto= Utilidades.convertirAEntero(mundo.consultaTipoConcepto(con, Utilidades.convertirAEntero(forma.getIndiceDevolucion())));
					String documento= mundo.consultaPagoConvenio(con, Utilidades.convertirAEntero(forma.getIndiceDevolucion()));

					if(estadoRecibo!=ConstantesBD.codigoEstadoReciboCajaAnulado)
					{
						if((tipoConcepto==ConstantesBD.codigoTipoIngresoTesoreriaConvenios)&&!documento.equals(""))
						{	
							errores.add("codigo", new ActionMessage("error.tesoreria.tipoPagoConvenios"));
						}	

						int codigoPersona=Paciente.obtenerCodigoPersona( forma.getMapaResultadoDevoluciones().get("tipoidbeneficiario_"+forma.getIndiceDetalle())+"", forma.getMapaResultadoDevoluciones().get("numidbeneficiario_"+forma.getIndiceDetalle())+"");
						Integer ingreso=null;
						boolean controlarAbonoPaciente=UtilidadTexto.getBoolean(ValoresPorDefecto.getControlarAbonoPacientePorNroIngreso(usuario.getCodigoInstitucionInt()));

						try{
							if(forma.getMapaResultadoDevoluciones().get("ingreso_"+forma.getIndiceDetalle())!=null)
							{
								ingreso=Integer.parseInt(forma.getMapaResultadoDevoluciones().get("ingreso_"+forma.getIndiceDetalle())+"");
							}
							else{

								RecibosCaja mundoReciboCaja= new RecibosCaja();

								Connection con1=null;
								try {
									int pos=0;						
									mundoReciboCaja.setTipoIdentificacion(forma.getDetalleDevolucion().get("tipoidbeneficiario_0")+"");
									mundoReciboCaja.setIdentificacionPaciente(forma.getDetalleDevolucion().get("numidbeneficiario_0")+"");										
									con1=UtilidadBD.abrirConexion();
									forma.setMapaPacientes(mundoReciboCaja.generarConsultaPacientes(con1, controlarAbonoPaciente));
									//ingreso = new Integer(forma.getMapaPacientes().get("ingreso_"+pos)+"");
									String cadIngreso = forma.getMapaPacientes().get("ingreso_"+ pos) != null ? 
											forma.getMapaPacientes().get("ingreso_"+ pos).toString() : null;
									if(cadIngreso == null) {
										ingreso = mundoReciboCaja.obtenerIngresoPacientePorConsecutivoReciboCaja(forma.getDetalleDevolucion().get("consecutivorc_0").toString());
									} else {
										ingreso = new Integer(cadIngreso);
									}
									UtilidadBD.closeConnection(con1);								

								} catch (NumberFormatException e) {
									UtilidadBD.closeConnection(con1);
								} catch (Exception e) {			
									UtilidadBD.closeConnection(con1);
								}										
							}
						}
						catch (NumberFormatException e) {
						}
						
						if(controlarAbonoPaciente)
						{
							if((tipoConcepto==ConstantesBD.codigoTipoIngresoTesoreriaAbonos)&&(Utilidades.convertirADouble(forma.getMapaResultadoDevoluciones().get("valordevol_")+"")>Utilidades.obtenerAbonosDisponiblesPaciente(codigoPersona, ingreso, usuario.getCodigoInstitucionInt())))
							{
								errores.add("codigo", new ActionMessage("error.tesoreria.tipoAbonoPaciente"));
							}
						}
						forma.setIngreso(ingreso);

						if(errores.isEmpty() && (tipoConcepto==ConstantesBD.codigoTipoIngresoTesoreriaAbonos)&&(Utilidades.convertirADouble(forma.getMapaResultadoDevoluciones().get("valordevol_")+"")>Utilidades.obtenerAbonosDisponiblesPaciente(codigoPersona, null, usuario.getCodigoInstitucionInt())))
						{
							errores.add("codigo", new ActionMessage("error.tesoreria.tipoAbonoPaciente"));
						}					
					}
					else
					{
						errores.add("codigo", new ActionMessage("error.tesoreria.estadoReciboCaja"));
					}
					if(!errores.isEmpty())
					{
						saveErrors(request, errores);
					}
					else
					{
						UtilidadBD.closeConnection(con);
						return mapping.findForward("detalle");
					}
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("ordernar"))
				{
					this.accionOrdenar(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listado");
				}
				else if(estado.equals("guardar"))
				{
					logger.info("\n\n\n\n\n\n\n\n\n\n");
					Utilidades.imprimirMapa(forma.getDetalleDevolucion());
					logger.info("\n\n\n\n\n\n\n\n\n\n");



					TurnoDeCaja turno= new TurnoDeCaja();
					//Se valida que el usuario tenga un turno activo para la caja en la cual se registro la devolución
					Cajas caja = new Cajas();
					caja.setConsecutivo(Integer.parseInt(forma.getDetalleDevolucion().get("caja_0").toString()));

					boolean validacionTurno=new TurnoDeCajaMundo().validarUsuarioTurnoCaja(caja, String.valueOf(forma.getDetalleDevolucion().get("cajero_0")));
					if(!validacionTurno)
					{
						errores.add("validacion Turno", new ActionMessage("error.devolucionrecibosCaja.turnoCerradoAsociadoACajero"));
						saveErrors(request, errores);
						return mapping.findForward("detalle");
					}

					ArrayList<TurnoDeCaja> listaTurnos=new TurnoDeCajaDelegate().obtenerTurnoCajaAbiertoPorCajeroDevolucion(String.valueOf(forma.getDetalleDevolucion().get("cajero_0")));
					if(listaTurnos!=null && !listaTurnos.isEmpty()){
						/* Solamente puede existir un turno de caja abierto*/
						turno=listaTurnos.get(0);
						if(turno == null){
							errores.add("validacion Turno", new ActionMessage("error.devolucionrecibosCaja.turnoCaja"));
							saveErrors(request, errores);
							return mapping.findForward("detalle");
						}
					}
					else{
						errores.add("validacion Turno", new ActionMessage("error.devolucionrecibosCaja.turnoCaja"));
						saveErrors(request, errores);
						return mapping.findForward("detalle");
					}

					if(forma.getEstadoDevolucion().equals("APR")) // APROBADO
					{
						if(!validacionSaldoDisponibleTurno(forma, turno, usuario)){
							errores.add("validacion Saldo Disponible", new ActionMessage("error.devolucionrecibosCaja.saldoInsuficiente"));
							saveErrors(request, errores);
							return mapping.findForward("detalle");
						}	
						if(forma.isValidacionAnticiposConveOdontologia())
						{
							forma.setValorTotalReciboCaja(ConstantesBD.codigoNuncaValidoDouble);
							forma.setValorAnticRecConv(ConstantesBD.codigoNuncaValidoDouble);

							logger.info("Es de tipo Validaciones Anticipos Convenios Odontologia" );
							ResultSetDecorator rs1=null;
							String numeroReciboCaja = forma.getDetalleDevolucion().get("numerorc_0")+"";					 
							int valorContrato= mundo.esReciboCajaXConceptAnticipConvenioOdonto(con, numeroReciboCaja);
							double valorReciboCaja=ConstantesBD.codigoNuncaValidoDouble;


							if(valorContrato > 0)
							{				        	
								logger.info("ENTRO a VALIDAR  ReciboCajaXConceptAnticipConvenioOdonto ");

								rs1 = mundo.consultarValorConceptosRecibosCaja(con, numeroReciboCaja, usuario.getCodigoInstitucionInt());

								while(rs1.next())
								{
									valorReciboCaja=valorReciboCaja + rs1.getDouble("valorconceptorc");
								}
								forma.setValorTotalReciboCaja(valorReciboCaja);
								double valorAnticRecConv=ConstantesBD.codigoNuncaValidoDoubleNegativo;
								valorAnticRecConv=Utilidades.convertirADouble(mundo.validarValorReciboCajaVsValorAnticipoDispContr(con, valorContrato,valorReciboCaja+"").get("valorAntConv")+"");






								if(valorAnticRecConv <= 0)
								{		
									logger.info("NOOOOO Continua de Validacion Anticipos.... ValorAnticipo"+valorAnticRecConv);  
									forma.setMostrarMensaje(new ResultadoBoolean(true,"No se puede aprobar devolución del recibo de caja "+numeroReciboCaja+" por que No tiene Saldo Disponible. "));
									UtilidadBD.closeConnection(con);
									return mapping.findForward("detalle");
								}
								else
								{   
									logger.info("Continua despues de Validacion Anticipos.... ValorAnticipo"+valorAnticRecConv);  
									forma.setValorAnticRecConv(valorAnticRecConv);		            	
									forma.setExisteReciboCajaXConceptAnticipConvenioOdonto(true);           	
									forma.setNumContratoOdontologia(valorContrato);
								} 	


							}  

						}



						/**
						 * 	ANEXO 830
						 *  APROBACION DE DEVOLUCIONES DE RECIBOS DE CAJA  
						 * 	SI EL ESTADO ESTA GENERADO Y CORRESPONDE A UN CONCEPTO DE RECAUDOS TIP0=3
						 *  SE CAMBIA EL ESTADO DE LA  TARJETA A INACTIVA DEL BENEFICIARIO
						 */
						if (forma.getDetalleDevolucion().get("estadodevol_0").toString().equals(ConstantesIntegridadDominio.acronimoEstadoGenerado))
						{
							logger.info("\n\n\n\n\n\n\n\n\n\n");
							logger.info("*****************************************************************************************************************************");
							logger.info(" CAMBIANDO EL ESTADO DEL BENEFICIARIO ");
							logger.info("********************** ENTRO A BUSCAR LAS VENTAS***********************************");	
							ArrayList<DtoBeneficiarioCliente> listaBeneficiarios = new ArrayList<DtoBeneficiarioCliente>();

							logger.info("EL RECIBO DE CAJA ES ***********************"+ forma.getReciboCaja());
							RegistroDevolucionRecibosCaja objmundo=new RegistroDevolucionRecibosCaja();
							ArrayList<DtoVentaTarjetasCliente> listaVentas = objmundo.consultaFacturas( forma.getDetalleDevolucion().get("numerorc_0")+"");


							DtoBeneficiarioCliente dtoWhereBeneficiario=new DtoBeneficiarioCliente();
							logger.info("EL NUMERO DE VENTAS ES ****************************"+ listaVentas.size());
							for(DtoVentaTarjetasCliente i : listaVentas)
							{

								dtoWhereBeneficiario = new DtoBeneficiarioCliente();
								dtoWhereBeneficiario.setVentaTarjetaCliente(i.getCodigoPk());

								DtoBeneficiarioCliente dtoNuevoBeneficiario = new DtoBeneficiarioCliente();
								dtoNuevoBeneficiario.setEstadoTarjeta(ConstantesIntegridadDominio.acronimoEstadoInactivo);
								dtoNuevoBeneficiario.setVentaTarjetaCliente(i.getCodigoPk());
								logger.info("\n\n\n MODIFICANADO");
								BeneficiariosTarjetaCliente.modificar(dtoNuevoBeneficiario,dtoWhereBeneficiario);

							}
						}

					}

					int inserto=mundo.insertarDevolucion(con, Utilidades.convertirAEntero(forma.getDetalleDevolucion().get("numdevol_0")+""), 
							forma.getEstadoDevolucion(), usuario.getLoginUsuario(), forma.getMotivoAnulacion(), 
							Utilidades.convertirADouble(forma.getDetalleDevolucion().get("valordevol_0")+""),
							turno.getCodigoPk());

					if(inserto>0)
					{
						this.guardarDevolucionReciboCaja(con,forma,usuario);
					}
					if(inserto==0)
					{
						forma.setMostrarMensaje(new ResultadoBoolean(true,"Valor de la Devolucion es mayor al Saldo del Recibo de Caja. No se permite su Devolucion."));
					}
					if(inserto<0)
					{
						forma.setMostrarMensaje(new ResultadoBoolean(true,"No se pudo generar el proceso exitosamente."));
					}
					HibernateUtil.endTransaction();
					return mapping.findForward("detalle");
				}
				else if(estado.equals("imprimirDevolucion"))
				{	
					return this.accionImprimirReporte(con, forma, mapping, request, usuario);
				}			
				else
				{
					forma.reset(usuario.getCodigoCentroAtencion()+"",usuario.getCodigoInstitucion());
					logger.warn("Estado no valido dentro del flujo de APROBACION/ANULACION DEVOLUCIONES ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}	
			}
			else
			{
				logger.error("El form no es compatible con el form de AprobacionAnulacionDevolucionesForm");
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
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private boolean guardarDevolucionReciboCaja(Connection con, AprobacionAnulacionDevolucionesForm forma, UsuarioBasico usuario) 
	{
				
		AprobacionAnulacionDevoluciones mundo=new AprobacionAnulacionDevoluciones();
		
		int consecutivoDevol= Utilidades.convertirAEntero(forma.getDetalleDevolucion().get("consecutivo_0")+"");
		
		boolean resultado=true;
		
		if(forma.getEstadoDevolucion().equals("APR"))
		{			
			resultado=acutalizacionesPorAprobacion(con,forma,mundo,usuario);
			// Aprobacion de una devolucion de recibos de caja de concepto Facturas Varias
			if(Utilidades.convertirAEntero(forma.getDetalleDevolucion().get("tipoingreso_0").toString()) == ConstantesBD.codigoTipoIngresoTesoreriaOtrasCxC)
			{
				// verifacar si el filtro es facturas
				RegistroDevolucionRecibosCaja regDevRecCaja = new RegistroDevolucionRecibosCaja();
				forma.setValorConceptoIngTes(regDevRecCaja.getValorConceptoIngTesoreria(
						forma.getDetalleDevolucion().get("concepto_0").toString(),
						usuario.getCodigoInstitucion(), 
						ConstantesBD.codigoTipoIngresoTesoreriaOtrasCxC));
				if(!forma.getValorConceptoIngTes().equals(""))
				{
					if(forma.getValorConceptoIngTes().equals(ConstantesIntegridadDominio.acronimoFactura))
					{
						int result = 0 ;
						AnulacionRecibosCaja obj = new AnulacionRecibosCaja();
						result = obj.anularReciboCajaFacturasVarias(con, forma.getDetalleDevolucion().get("numerorc_0").toString(),usuario);
						if(result == 0 || result==ConstantesBD.codigoNuncaValido)
							resultado = false;
					}
				} 
			}
		}
		if(resultado)
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"Devolucion N° "+consecutivoDevol+" Realizada exitosamente."));
		}
		else
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"No se pudo generar el proceso exitosamente."));
		}
		return resultado;	
		
	}
	
	
	/**
	 * 
	 * @param con 
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @return
	 */
	private boolean acutalizacionesPorAprobacion(Connection con, AprobacionAnulacionDevolucionesForm forma, AprobacionAnulacionDevoluciones mundo, UsuarioBasico usuario) 
	{
		
		
        int tipoConcepto= Utilidades.convertirAEntero(forma.getDetalleDevolucion().get("tipoingreso_0")+"");
        
        
        String reciboCaja= forma.getDetalleDevolucion().get("numerorc_0")+"";
        String numeroIdBeneficiario= forma.getDetalleDevolucion().get("numidbeneficiario_0")+"";
        String tipoIdBenefciario= forma.getDetalleDevolucion().get("tipoidbeneficiario_0")+"";
        int consecutivoDevol= Utilidades.convertirAEntero(forma.getDetalleDevolucion().get("consecutivo_0")+"");
        double valorDevol= Utilidades.convertirADouble(forma.getDetalleDevolucion().get("valordevol_0")+"");
        
        if(tipoConcepto==ConstantesBD.codigoTipoIngresoTesoreriaPacientes)
        {
            String[] codigoPagosFacturaPaciente=Utilidades.getPagosFacturaPacienteReciboCaja(con,ConstantesBD.codigoTipoDocumentoPagosReciboCaja,reciboCaja,usuario.getCodigoInstitucionInt());
            boolean resultado=true;
            for(int j=0;j<codigoPagosFacturaPaciente.length;j++)
            {
            	resultado=Utilidades.cambiarEstadoPacienteFactura(con,Utilidades.obtenerCodigoFacturaPagosPaciente(con,Integer.parseInt(codigoPagosFacturaPaciente[j])),ConstantesBD.codigoEstadoFacturacionPacientePorCobrar);
            	resultado=Utilidades.cambiarEstadoPagoFacturaPacient(con,Integer.parseInt(codigoPagosFacturaPaciente[j]),ConstantesBD.codigoEstadoPagosAnulado);
            }
            return resultado;
        }
        if(tipoConcepto==ConstantesBD.codigoTipoIngresoTesoreriaConvenios)
        {
            boolean resultado=true;
            String[] codigoPagosEmpresa=Utilidades.getPagosEmpresaReciboCaja(con,ConstantesBD.codigoTipoDocumentoPagosReciboCaja,reciboCaja,usuario.getCodigoInstitucionInt());
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
            return resultado;
        } 
        if(tipoConcepto==ConstantesBD.codigoTipoIngresoTesoreriaAbonos)
        {
        	boolean resultado=true;
        	
        	resultado=(AbonosYDescuentos.insertarMovimientoAbonos(con, Paciente.obtenerCodigoPersona( numeroIdBeneficiario, tipoIdBenefciario),consecutivoDevol,ConstantesBD.tipoMovimientoAbonoDevolucionReciboCaja,valorDevol, usuario.getCodigoInstitucionInt(), forma.getIngreso(), usuario.getCodigoCentroAtencion())>0);
            return resultado;
        } 
        
        if(tipoConcepto==ConstantesBD.codigoTipoIngresoTesoreriaAnticipoConvenioOdon)
        {
        	boolean resultado=true;
        	logger.info("Entro a Actualizar el valor Anticipo Recibido Convenio");
            double nuevoValorAnticipoRecConv;
            nuevoValorAnticipoRecConv=forma.getValorAnticRecConv()-forma.getValorTotalReciboCaja();
            resultado = mundo.actualizarValorAnticipo(con, forma.getNumContratoOdontologia() , nuevoValorAnticipoRecConv, usuario.getLoginUsuario());
            return resultado;
        }
        
        
        return true;
	}
	
	/**
	 * 
	 * @param forma
	 */
	private void accionOrdenar(AprobacionAnulacionDevolucionesForm forma) 
	{
		int numReg=Utilidades.convertirAEntero(forma.getMapaResultadoDevoluciones("numRegistros")+"");
		forma.setMapaResultadoDevoluciones(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaResultadoDevoluciones(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setMapaResultadoDevoluciones("numRegistros",numReg+"");
		
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @return
	 */
	private ActionForward accionImprimirReporte(Connection con, AprobacionAnulacionDevolucionesForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario) {
    	String newQuery;
		newQuery="SELECT dvrc.consecutivo AS codigo, dvrc.fecha_devolucion, dvrc.valor_devolucion, dvrc.usuario_devolucion, " +
				"getintegridaddominio(dvrc.estado) AS estado, md.descripcion, dtcrc.nombre_beneficiario, dtcrc.tipo_id_beneficiario, "+
				"dtcrc.numero_id_beneficiario, dvrc.numero_rc, rc.fecha, rc.hora, dvrc.forma_pago, dvrc.observaciones, " +
				"dvrc.fecha_aprobacion, dvrc.hora_aprobacion, dvrc.usuario_aprobacion, dvrc.caja_devolucion, dvrc.doc_soporte, " +
				"dvrc.fecha_anulacion, dvrc.hora_anulacion, dvrc.usuario_anulacion, dvrc.motivo_anulacion " +
		 "FROM devol_recibos_caja dvrc INNER JOIN recibos_caja rc ON (dvrc.numero_rc = rc.numero_recibo_caja AND dvrc.institucion = rc.institucion) " +
				"INNER JOIN detalle_conceptos_rc dtcrc ON (dtcrc.numero_recibo_caja = rc.numero_recibo_caja AND dtcrc.institucion = rc.institucion) " +
				"INNER JOIN motivos_devolucion_rc md ON (dvrc.motivo_devolucion = md.codigo) " +
				"WHERE dvrc.consecutivo = " + forma.getDetalleDevolucion().get("consecutivo_0") + "";
		
    	String nombreRptDesign = "ConsultaImpresionDevolucion.rptdesign";

		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		//Informacion del Cabezote
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"tesoreria/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,5);
        Vector v=new Vector();
        v.add(ins.getRazonSocial());
        v.add(ins.getTipoIdentificacion()+"         "+ins.getNit());
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
       return mapping.findForward("detalle");
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
	private boolean validacionSaldoDisponibleTurno(AprobacionAnulacionDevolucionesForm forma, TurnoDeCaja turno, UsuarioBasico usuario) {
		
		IMovimientosCajaMundo movimientosCajaMundo = TesoreriaFabricaMundo.crearMovimientosCajaMundo();
		DtoConsolidadoMovimiento consolidado = new DtoConsolidadoMovimiento();
		boolean resultado=false;
		
		MovimientosCaja movimientoCaja = new MovimientosCaja();
		movimientoCaja.setCodigoPk(ConstantesBD.codigoNuncaValidoLong);
		movimientoCaja.setTurnoDeCaja(turno);
		consolidado= movimientosCajaMundo.obtenerConsolidadoMovimiento(movimientoCaja);
		for(DtoCuadreCaja cuadreCaja : consolidado.getCuadreCajaDTOs()){
			if(esFormaPagoEfectivo(usuario.getCodigoInstitucionInt(), cuadreCaja.getTipoFormaPago())){
				double valorDevolucion= Double.parseDouble(forma.getDetalleDevolucion().get("valordevol_0").toString());
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
