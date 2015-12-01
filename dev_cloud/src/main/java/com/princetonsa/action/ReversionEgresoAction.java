/*
 * @(#)ReversionEgresoAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.action;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

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
import util.InfoDatosInt;
import util.RespuestaValidacion;
import util.RespuestaValidacionTratante;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.ReversionEgresoForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.AdmisionHospitalaria;
import com.princetonsa.mundo.AdmisionUrgencias;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.IngresoGeneral;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.TrasladoCamas;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.atencion.Egreso;
import com.princetonsa.mundo.facturacion.EntidadesSubContratadas;

/**
 *	@version 1.1, Jul 23, 2003
 */
public class ReversionEgresoAction extends Action
{

    /**
	 * Objeto de tipo Logger para manejar los logs
	 * que genera esta clase
	 */
    private Logger logger = Logger.getLogger(ReversionEgresoAction.class);
    
    //Objetos para validaciones
    private RespuestaValidacionTratante	resp1 = new RespuestaValidacionTratante("",false,false);
	private RespuestaValidacionTratante	resp2 = new RespuestaValidacionTratante("",false,false);
    
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute
	(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response
	) throws Exception
	{
		Connection	con=null;
		try{
			if(form instanceof ReversionEgresoForm)
			{

				if(response==null); //Para evitar que salga el warning

				if(logger.isDebugEnabled() )
					logger.debug("Entro al Action de Reversión de Egreso");

				boolean						terminoEgreso;


				ReversionEgresoForm			reversionEgresoForm;
				String						estado;

				try
				{
					DaoFactory myFactory	= DaoFactory.getDaoFactory( (String)System.getProperty("TIPOBD") );
					con						= myFactory.getConnection();
				}
				catch(Exception e)
				{
					e.printStackTrace();
					//No se cierra conexión porque si llega aca ocurrió un
					//error al abrirla
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				reversionEgresoForm	= (ReversionEgresoForm)form;
				estado				= reversionEgresoForm.getEstado();
				logger.info("estado ReversionEgresoAction=> "+estado);

				//Lo primero que vamos a hacer es validar que se
				//cumplan las condiciones.

				HttpSession		session		= request.getSession();
				PersonaBasica	paciente	= (PersonaBasica)session.getAttribute("pacienteActivo");
				UsuarioBasico	medico		= (UsuarioBasico)session.getAttribute("usuarioBasico");
				/********************************VALIDACIONES GENERALES***************************************************************************************************************/			
				ActionForward respuestaValidacion = validacionesGenerales(con,paciente,mapping,request,medico,reversionEgresoForm);
				if(respuestaValidacion!=null)
					return respuestaValidacion;
				/********************************FIN VALIDACIONES GENERALES***************************************************************************************************************/
				/** ***************************** ESTADO EMPEZAR ************************************************************************************************************* **/
				/*
				Existe un caso particular en el cual no se deben mostrar datos y es cuando nos
				encontramos en un semiegreso. Este solo puede existir si estamos en una
				admision de urgencias
				 */
				if(paciente.getAnioAdmision() > 0 &&estado.equals("empezar") )
				{				

					/**
					 * Validar concurrencia
					 * Si ya está en proceso de distribucion, no debe dejar entrar
					 **/
					if(UtilidadValidacion.estaEnProcesoDistribucion(con, paciente.getCodigoPersona(), medico.getLoginUsuario()) )
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoDistribucion", "error.facturacion.cuentaEnProcesoDistribucion", true);
					}
					/**
					 * Validar concurrencia
					 * Si ya está en proceso de facturación, no debe dejar entrar
					 **/
					else if(UtilidadValidacion.estaEnProcesofacturacion(con, paciente.getCodigoPersona(), "") )
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoFact", "error.facturacion.cuentaEnProcesoFact", true);
					}
					else
					{
						//Ahora revisamos si hay o no
						boolean esSemiEgreso[] = UtilidadValidacion.tieneSemiEgreso(con, paciente.getCodigoCuenta() );

						if(esSemiEgreso[0])
						{
							//Estamos en un semiEgreso
							//ahora definimos si está completo o no
							reversionEgresoForm.setSemiEgresoCompleto(esSemiEgreso[1]);
							reversionEgresoForm.setAccionAFinalizar("reversarSemiEgreso");
							if(con!=null&&!con.isClosed())
							{
								UtilidadBD.closeConnection(con);
							}

							if (!esSemiEgreso[1])
							{
								request.setAttribute
								(
										"descripcionError",
								"Paciente no tiene egreso para reversar");
								return mapping.findForward("paginaError");
							}
							else
							{
								logger.info("pasó por acá hacia el forward jspSemiEgreso");
								return mapping.findForward("jspSemiEgreso");
							}

						}
					}
					//Si no es un semiEgreso, no hacemos nada, continúa la
					//ejecución del action normalmente
					logger.info("Entré a ReversiónEgreso estado empezar 3");
				}
				/** ******************************************************************************************************* **/

				/** ***************** ESTADO REVERSARSEMIEGRESO ******************************************************************************** **/
				//Este estado no pudo definirse por medio de accion finalizar ya
				//que la validacion por tiempo falla antes de llegar a este punto
				if(paciente.getAnioAdmision() > 0&& estado.equals("reversarSemiEgreso") )
				{
					return accionReversarSemiEgreso(con,paciente,reversionEgresoForm,mapping,request);
				}
				/** *************************************************************************************************************** **/

				//************************SE REALIZAN LAS VALIDACIONES DE LA REVERSION*********************************************
				ActionForward respuestaValidacionReversion = validacionesReversion(con,paciente,medico,request,mapping);
				if(respuestaValidacionReversion!=null)
					return respuestaValidacionReversion;
				//*********************************************************************************************************************

				terminoEgreso = resp1.esTratante || resp2.esTratante;


				//*********************************************************************************************************************
				//***************************VUELVE Y SE ANALIZAN LOS ESTADOS*************************************************************
				//*************************************************************************************************************************
				if(estado.equalsIgnoreCase("empezar") )
				{
					logger.info("empezaaaaaaaaaaaaaaaaaaaaaaaaar");
					return accionEmpezar(con,reversionEgresoForm,paciente,mapping,session,terminoEgreso,request,medico);
				}
				else if(estado.equalsIgnoreCase("cancelar") )
				{
					return this.accionEstadoCancelar(request, response, con, reversionEgresoForm);
				}
				else if(estado.equalsIgnoreCase("salir") )
				{
					return accionSalir(con,paciente,reversionEgresoForm,medico,request,mapping);
				}
				//*************************************************************************************************************************
				//*************************************************************************************************************************
				//*************************************************************************************************************************
				//CODIGO DUMMY DE AQUI PARA ABAJO!!!!!
				if(con != null && !con.isClosed() )
					UtilidadBD.closeConnection(con);

				return mapping.findForward("principal");
			}
			else
			{
				//Todavía no existe conexión, por eso no se cierra
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
	 * Método implementado para guardar la reversión del egresp
	 * @param con
	 * @param paciente
	 * @param reversionEgresoForm
	 * @param medico
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionSalir(Connection con, PersonaBasica paciente, ReversionEgresoForm reversionEgresoForm, UsuarioBasico medico, HttpServletRequest request, ActionMapping mapping) 
	{
		try
		{
			logger.info("Está en el estado 'salir' con la accionAFinalizar: "+reversionEgresoForm.getAccionAFinalizar());
			if(reversionEgresoForm.getAccionAFinalizar() == null || reversionEgresoForm.getAccionAFinalizar().equals("") )
			{
				request.setAttribute
				(
					"descripcionError",
					"No se puede finalizar el proceso porque la acci&oacute;n a terminar no ha sido especificada. Por favor utilice los men&uacute;es"
				);
	
				if(con != null && !con.isClosed() )
					UtilidadBD.closeConnection(con);
	
				return mapping.findForward("paginaError");
			}
			else if(reversionEgresoForm.getAccionAFinalizar().equals("insertar") )
			{
				ActionErrors errores = new ActionErrors();
				
				reversionEgresoForm.setSeDebeMostrarResultado(true);
				//AQUI SE GUARDA EN BD
				logger.info("SE DEBE MOSTRAR CAMA? "+reversionEgresoForm.getSeDebeMostrarCama());
				//Hay dos posibles casos, que se haya completado el egreso o que
				//no. Esto nos lo da el hecho de mostrar camas
				if(reversionEgresoForm.getSeDebeMostrarCama() )
				{
					//Egreso completo se debe hacer reversión en el egreso y en la admisión
					Egreso			egreso;
	
					egreso = new Egreso();
					egreso.setMotivoReversionEgreso(reversionEgresoForm.getMotivoReversion() );
					egreso.actualizarPorReversionEgresoTransaccional(con, paciente.getCodigoCuenta(), medico.getCodigoPersona(), "empezar");
					
					//Actualizar estado del paciente
					UtilidadValidacion.actualizarPacienteAMuertoTransaccional(con,paciente.getCodigoPersona(),true,"", "", "",ConstantesBD.continuarTransaccion);
					
					logger.info("PEUDO SEGUIR? "+resp1.puedoSeguir);
					/* El egreso es de un ingreso a hospitalización */
					if(resp1.puedoSeguir)
					{
						//********VALIDACION DE CONCURRENCIA EN CAMA**********************************
						ArrayList filtro = new ArrayList();
						
						filtro.add(reversionEgresoForm.getDatosCama("codigoCama")+"");
						if(UtilidadCadena.noEsVacio(reversionEgresoForm.getDatosCama("codigoCama")+"") && !reversionEgresoForm.getDatosCama("codigoCama").equals("0"))
						{
						UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloqueoCama,filtro);
						}
						logger.info("\n\n\n\n [ReversionEgreso] CodigCama-> ("+reversionEgresoForm.getDatosCama("codigoCama")+")\n\n\n");
						
						
						if(UtilidadCadena.noEsVacio(reversionEgresoForm.getDatosCama("codigoCama")+"") && !reversionEgresoForm.getDatosCama("codigoCama").equals("0"))
						{
							//se consulta el estado de la cama
							int estadoCama=Utilidades.obtenerCodigoEstadoCama(con,Integer.parseInt(reversionEgresoForm.getDatosCama("codigoCama")+""));
							
							//se consulta el estado de la cama anterior
							int estadoCamaAnterior=ConstantesBD.codigoNuncaValido;
							if (UtilidadCadena.noEsVacio(reversionEgresoForm.getDatosCama("codigoCamaAnterior")+"") 
									&& reversionEgresoForm.getDatosCama().containsKey("codigoCamaAnterior") )
								estadoCamaAnterior=Utilidades.obtenerCodigoEstadoCama(con,Utilidades.convertirAEntero(reversionEgresoForm.getDatosCama("codigoCamaAnterior")+""));
								
							if(estadoCama !=ConstantesBD.codigoEstadoCamaDisponible &&
							   estadoCama !=ConstantesBD.codigoEstadoCamaDesinfeccion && 
							   estadoCama !=ConstantesBD.codigoEstadoCamaConSalida &&
							   !UtilidadesManejoPaciente.obtenerTipoPacienteCuenta(con, paciente.getCodigoCuenta()+"").equals(ConstantesBD.tipoPacienteCirugiaAmbulatoria))
							{
			                    errores.add("Cama estado diferente", new ActionMessage("error.cama.estadoDiferenteDisponible","SELECCIONADA"));
			                    reversionEgresoForm.setSeDebeMostrarResultado(false);
			                    saveErrors(request, errores);
			                    UtilidadBD.abortarTransaccion(con);
			                    UtilidadBD.closeConnection(con);            
			                    return mapping.findForward("principal");
							}
							//*****************************************************************************
						
														
							if (estadoCama ==ConstantesBD.codigoEstadoCamaDisponible ||
								estadoCama ==ConstantesBD.codigoEstadoCamaDesinfeccion || 
								estadoCama ==ConstantesBD.codigoEstadoCamaConSalida)
							{
								AdmisionHospitalaria adh;
								adh = new AdmisionHospitalaria();
								adh.setIdCuenta(paciente.getCodigoCuenta() );
								adh.setCodigoCama(Integer.parseInt(reversionEgresoForm.getDatosCama("codigoCama").toString() ));
								adh.init(System.getProperty("TIPOBD") );
								adh.actualizarPorReversionEgresoTransaccional(con, ConstantesBD.continuarTransaccion);
								
								//Se reversoa el ingreso cuidado especial
								if(paciente.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteHospitalizado))
									if(!UtilidadesManejoPaciente.reversarIngresoCuidadoEspecial(con, paciente.getCodigoIngreso(), medico.getLoginUsuario()))
										errores.add("", new ActionMessage("errors.problemasGenericos","reversando el ingreso cuidado especial"));
								
								
								/////INSERT DE LOS TRASLADOS DE LA CAMA
								TrasladoCamas objetoTrasladosCamas = new TrasladoCamas(); 
								logger.info("fecha asignacion---->"+reversionEgresoForm.getFechaIngresoDePiso());
								objetoTrasladosCamas.setFechaAsignacion(reversionEgresoForm.getFechaIngresoDePiso());
								logger.info("hora asignacion---->"+reversionEgresoForm.getHoraIngresoDePiso());
								objetoTrasladosCamas.setHoraAsignacion(reversionEgresoForm.getHoraIngresoDePiso());
								logger.info("inst-->"+medico.getCodigoInstitucionInt());
								objetoTrasladosCamas.setInstitucion(medico.getCodigoInstitucionInt());
								logger.info("paciente-->"+paciente.getCodigoPersona());
								objetoTrasladosCamas.setCodigoPaciente(paciente.getCodigoPersona());
								logger.info("cuenta--->"+adh.getIdCuenta());
								objetoTrasladosCamas.setCuenta(adh.getIdCuenta());
								Cuenta objetoCuenta= new Cuenta();
								objetoCuenta.cargarCuenta(con, objetoTrasladosCamas.getCuenta()+"");
								logger.info("Convenio--->"+objetoCuenta.getCodigoConvenio());
								objetoTrasladosCamas.setConvenioPaciente(new InfoDatosInt(Integer.parseInt(objetoCuenta.getCodigoConvenio())));
								objetoTrasladosCamas.setFechaFinalizacion("");
								objetoTrasladosCamas.setHoraFinalizacion("");
								logger.info("Nueva cama--->"+adh.getCodigoCama());
								objetoTrasladosCamas.setCodigoNuevaCama(adh.getCodigoCama());
								objetoTrasladosCamas.setCodigoCamaAntigua(ConstantesBD.codigoNuncaValido);
								
								if (estadoCamaAnterior == ConstantesBD.codigoEstadoCamaConSalida )
								{
									if (!(reversionEgresoForm.getDatosCama("codigoCama")+"").equals((reversionEgresoForm.getDatosCama("codigoCamaAnterior")+""))
											&& (Utilidades.convertirAEntero(reversionEgresoForm.getDatosCama("codigoCama")+"")>0))
									{
										//se actualiza en la tabla traslado_camas los campos fecha_finalizacion y hora_finalizacion 
										objetoTrasladosCamas.actualizarFechaHoraFinalizacion(con, adh.getIdCuenta(), UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual(), ConstantesBD.continuarTransaccion,"");
										//se actualiza el estado de la cama conforme el parametro "Estado de cama después de egreso"
										objetoTrasladosCamas.modificarEstadoCama(con, Utilidades.convertirAEntero(ValoresPorDefecto.getCodigoEstadoCama(medico.getCodigoInstitucionInt())+""), Utilidades.convertirAEntero(reversionEgresoForm.getDatosCama("codigoCamaAnterior")+""), medico.getCodigoInstitucionInt());
									
									}
									else
										if ((reversionEgresoForm.getDatosCama("codigoCama")+"").equals((reversionEgresoForm.getDatosCama("codigoCamaAnterior")+""))
												&& (Utilidades.convertirAEntero(reversionEgresoForm.getDatosCama("codigoCama")+"")>0))
										{
											//se toma el codigo del traslado de la cama.
											int codigoTrasladoCama = Utilidades.getCodigoTrasladoUltimoTraslado(con,  adh.getIdCuenta());
											
											if (codigoTrasladoCama>0)
											{
												HashMap datos = new HashMap ();
												datos.put("fechaFinaliza", "");
												datos.put("horaFinaliza", "");
												datos.put("codigoTraslado", codigoTrasladoCama);
												
												Utilidades.actualizarFechaHoraActualizacion(con, datos);
											}
										}
								}
								
								if(!errores.isEmpty())
								{
									saveErrors(request, errores);
									UtilidadBD.abortarTransaccion(con);
									UtilidadBD.closeConnection(con);
									return mapping.findForward("paginaErroresActionErrors");
								}
								
								if (estadoCama!=ConstantesBD.codigoEstadoCamaConSalida)
									objetoTrasladosCamas.insertarTrasladoCamaTransaccional(con, medico.getLoginUsuario(), ConstantesBD.finTransaccion);
								else
									UtilidadBD.finalizarTransaccion(con);
							}
							
								
						}
						else
						{
							AdmisionHospitalaria adh;
							adh = new AdmisionHospitalaria();
							adh.setIdCuenta(paciente.getCodigoCuenta() );
							adh.setCodigoCama(Types.NULL);
							adh.init(System.getProperty("TIPOBD") );
							adh.actualizarPorReversionEgresoTransaccional(con, ConstantesBD.continuarTransaccion);
							//Se reversoa el ingreso cuidado especial
							if(paciente.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteHospitalizado))
								if(!UtilidadesManejoPaciente.reversarIngresoCuidadoEspecial(con, paciente.getCodigoIngreso(), medico.getLoginUsuario()))
									errores.add("", new ActionMessage("errors.problemasGenericos","reversando el ingreso cuidado especial"));
							logger.info("no encontro cama, finaliza transaccion ");
							resp2.puedoSeguir=true;
							
							if(!errores.isEmpty())
							{
								saveErrors(request, errores);
								UtilidadBD.abortarTransaccion(con);
								UtilidadBD.closeConnection(con);
								return mapping.findForward("paginaErroresActionErrors");
							}
							
							UtilidadBD.finalizarTransaccion(con);
						}
						///////FIN INSERT TRASLADOS CAMAS
					}
					/* El egreso es de un ingreso a urgencias */
					else if(resp2.puedoSeguir)
					{
						AdmisionUrgencias lau_admision;
						lau_admision = new AdmisionUrgencias();
						lau_admision.setIdCuenta(paciente.getCodigoCuenta() );
						if(UtilidadCadena.noEsVacio(reversionEgresoForm.getDatosCama("codigoCama")+"") && !reversionEgresoForm.getDatosCama("codigoCama").equals("0"))
							lau_admision.setCodigoCama(Utilidades.convertirAEntero(reversionEgresoForm.getDatosCama("codigoCama").toString()) );
						else
							lau_admision.setCodigoCama(ConstantesBD.codigoNuncaValido);
						
						lau_admision.init(System.getProperty("TIPOBD") );
						lau_admision.actualizarPorReversionEgresoTransaccional(con, "finalizar");
					}
	
					//Si es una admisión hospitalaria puede cambiar el estado y  es necesario recargar el paciente. Por eso usamos el observer
					// Código necesario para notificar a todos los observadores que la cuentadel paciente en sesión pudo haber cambiado
					logger.info("SE CARGA EL PACIENTE EN SESION *************************************");
					UtilidadesManejoPaciente.cargarPaciente(con, medico, paciente, request);
				}
				else
				{
					//Solo orden de salida, se debe hacer reversión en el egreso
					Egreso egreso;
	
					egreso = new Egreso();
					egreso.setMotivoReversionEgreso(reversionEgresoForm.getMotivoReversion() );
					
					UtilidadBD.iniciarTransaccion(con);
					/* El egreso es de un ingreso a hospitalización */
					if(resp1.puedoSeguir)
					{
						//Actualizar estado del paciente
						UtilidadValidacion.actualizarPacienteAMuertoTransaccional(con,paciente.getCodigoPersona(),true,"", "", "",ConstantesBD.continuarTransaccion);
						
						//Se activa de nuevo la admision hospitalaria
						AdmisionHospitalaria adh;
						adh = new AdmisionHospitalaria();
						adh.setIdCuenta(paciente.getCodigoCuenta() );
						adh.setCodigoCama(0); //no se asigna cama porque es un ingreso
						adh.init(System.getProperty("TIPOBD") );
						adh.actualizarPorReversionEgresoTransaccional(con, ConstantesBD.continuarTransaccion);
						
						//Se reversoa el ingreso cuidado especial
						if(paciente.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteHospitalizado))
							if(!UtilidadesManejoPaciente.reversarIngresoCuidadoEspecial(con, paciente.getCodigoIngreso(), medico.getLoginUsuario()))
								errores.add("", new ActionMessage("errors.problemasGenericos","reversando el ingreso cuidado especial"));
					}
					
					egreso.actualizarPorReversionEgresoTransaccional(con, paciente.getCodigoCuenta(), medico.getCodigoPersona(), ConstantesBD.continuarTransaccion);
					
					
					if(!errores.isEmpty())
					{
						saveErrors(request, errores);
						UtilidadBD.abortarTransaccion(con);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("paginaErroresActionErrors");
					}
					
					
					
					//Como no hay otro componente en la transacción debemos hacerlo a mano
					con.commit();
					con.setAutoCommit(true);
					
					UtilidadesManejoPaciente.cargarPaciente(con, medico, paciente, request);
				}
	
				
				
				if(con != null && !con.isClosed() )
					UtilidadBD.closeConnection(con);
				
				return mapping.findForward("principal");
			}
			else
			{
				request.setAttribute
				(
					"descripcionError",
					"No se puede finalizar el proceso porque la acci&oacute;n a terminar no ha sido especificada. Por favor utilice los men&uacute;es"
				);
	
				if(con != null && !con.isClosed() )
					UtilidadBD.closeConnection(con);
	
				return mapping.findForward("paginaError");
			}
		}
		catch(SQLException e)
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.transaccion", "errors.transaccion", true);
		}
	}


	/**
	 * Método implementado para reversar un semiEgreso
	 * @param con
	 * @param paciente
	 * @param reversionEgresoForm
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionReversarSemiEgreso(Connection con, PersonaBasica paciente, ReversionEgresoForm reversionEgresoForm, ActionMapping mapping, HttpServletRequest request) 
	{
		try
		{
			//Ahora revisamos si hay o no
			boolean esSemiEgreso[] = UtilidadValidacion.tieneSemiEgreso(con, paciente.getCodigoCuenta() );
			logger.info("PASO POR AQUÍ        1!!!");
			if(esSemiEgreso[0])
			{
				//Si ya esta lleno
				if (esSemiEgreso[1])
				{
					Egreso semiEgreso=new Egreso();
					semiEgreso.setNumeroCuenta(paciente.getCodigoCuenta());
	
					try
					{
						if(semiEgreso.reversarSemiEgreso(con)>0)
						{
							//Se revisa cual fue la conducta de la valoración del paciente
							int codigoConductaValoracion = UtilidadesHistoriaClinica.obtenerCodigoConductaValoracionUrgenciasCuenta(con, paciente.getCodigoCuenta()+"");
							
							//Si la conducta fue de salida se revive al paciente
							if(codigoConductaValoracion==ConstantesBD.codigoConductaSeguirSalidaSinObservacion||codigoConductaValoracion==ConstantesBD.codigoConductaSeguirRemitirMayorComplejidad)
							{
								//Actualizar estado del paciente
								if(!UtilidadValidacion.actualizarPacienteAMuertoTransaccional(con,paciente.getCodigoPersona(),true,"", "", "",ConstantesBD.continuarTransaccion))
									return this.funcionalidadEnviarError(con, mapping, reversionEgresoForm, request, "No se pudo completar la reversion del  semiEgreso", "Error en la BD durante la reversion del egreso, por favor comuníquese con su administrador de red", false);
								
							}
							
							if(con!=null && !con.isClosed() )
								UtilidadBD.closeConnection(con);
	
							return mapping.findForward("semiEgresoExitoso");
						}
						else
						{
							return this.funcionalidadEnviarError(con, mapping, reversionEgresoForm, request, "No se pudo completar la reversion del  semiEgreso", "Error en la BD durante la reversion del egreso, por favor comuníquese con su administrador de red", false);
						}
					}
					catch (Exception e)
					{
						if(con!=null && !con.isClosed() )
							UtilidadBD.closeConnection(con);
	
						return this.funcionalidadEnviarError(con, mapping, reversionEgresoForm, request, "No se pudo completar la reversion del  semiEgreso. Excepcion " + e.toString(), "Error en la BD durante la reversion del egreso, por favor comuníquese con su administrador de red", false);
					}
				}
				else
				{
					//Si no es un semiEgreso, u otro usuario lo lleno previamente
					//o alguien se metio sin usar el menu
					return this.funcionalidadEnviarError(con, mapping, reversionEgresoForm, request, "Al tratar de finalizar la acción de revertir un semi-egreso, resulto ya estar respondido", "Algun usuario con privilegios similares ha revertido este egreso", false);
				}
			}
			else
			{
				//No esta en medio de un semiegreso, si esto ocurre
				//alguien intentó acceder por el menú a la aplicación 
				return this.funcionalidadEnviarError(con, mapping, reversionEgresoForm, request, "Usuario intento acceder a la funcionalidad de (salir)revertir semi-egreso sin usar los menus", "Por favor utilize los menués", false);
			}
		}
		catch(Exception e)
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.transaccion", "errors.transaccion", true);
		}
	}


	/**
	 * Método que inicia el flujo de la reversion del egreso
	 * @param con
	 * @param reversionEgresoForm
	 * @param paciente
	 * @param mapping
	 * @param session
	 * @param terminoEgreso
	 * @param request 
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, ReversionEgresoForm reversionEgresoForm, PersonaBasica paciente, ActionMapping mapping, HttpSession session, boolean terminoEgreso, HttpServletRequest request,UsuarioBasico usuario) 
	{
		logger.info("\n entre a accionEmpezar ");
		try
		{
			int		estadoCama,estadoCamaNueva=ConstantesBD.codigoNuncaValido;
			Egreso	egreso;
	
			reversionEgresoForm.reset();
			reversionEgresoForm.setHospitalDia(paciente.isHospitalDia());
			reversionEgresoForm.setAccionAFinalizar("insertar");
			reversionEgresoForm.setSeDebeMostrarResultado(false);
			reversionEgresoForm.setIdCuenta(paciente.getCodigoCuenta());
			reversionEgresoForm.setFechaIngresoDePiso(UtilidadFecha.getFechaActual());
			reversionEgresoForm.setHoraIngresoDePiso(UtilidadFecha.getHoraActual());
	
			//PRONTO AQUI EL NEGOCIO, DONDE SE CARGA Y DEMAS
			egreso = new Egreso();
			logger.info("entra ha este lugar para cargar los egresos pa' revision 11111.");
			if(reversionEgresoForm.isHospitalDia())
				egreso.cargarSemiEgreso(con, paciente.getCodigoCuenta());
			else
			{
				logger.info("entra ha este lugar para cargar los egresos pa' revision.");
				egreso.cargarEgresoReversionEgreso(con, paciente.getCodigoCuenta() );
	
				//Tambien definimos datos del form, para que estos queden precargados
				//Obviamente el estado de la cama debe ser disponible o si no no pueden
				//seleccionar esta cama (El estado debe ser 0 - disponible o 2-desinfección)
		
				estadoCama = egreso.getCamaReversionEgreso().getEstado();
				
			
				logger.info("\n estado cama -->"+estadoCama);
				if (estadoCama==ConstantesBD.codigoNuncaValido)
				{
					HashMap tmp = new HashMap ();
					
					tmp.put("codigoCama",Utilidades.getUltimaCamaTraslado(con, paciente.getCodigoCuenta()));
					tmp.put("institucion", usuario.getCodigoInstitucion());
					tmp=UtilidadesManejoPaciente.obtenerDatosCama(con, tmp);
			
					//se llenan los datos de la cama previa
					reversionEgresoForm.setDatosCama("pisoAnterior", tmp.get("piso"));
					reversionEgresoForm.setDatosCama("habitacionAnterior", tmp.get("habitacion"));
					reversionEgresoForm.setDatosCama("tipoHabitacionAnterior", tmp.get("tipoHabitacion"));
					reversionEgresoForm.setDatosCama("numeroCamaAnterior", tmp.get("numeroCama"));
					reversionEgresoForm.setDatosCama("tipoUsuarioAnterior",  tmp.get("tipoUsuario"));
					reversionEgresoForm.setDatosCama("nombreCentroCostoAnterior", tmp.get("nombreCentroCosto"));
					reversionEgresoForm.setDatosCama("estadoCamaAnterior", tmp.get("estadoCama"));
					reversionEgresoForm.setDatosCama("nombreEstadoCamaAnterior", tmp.get("nombreEstadoCama"));
					reversionEgresoForm.setDatosCama("codigoCamaAnterior", tmp.get("codigoCama"));
					estadoCamaNueva=Utilidades.convertirAEntero( tmp.get("estadoCama")+"");
				}
				else
				{
					//Se llenan los datos que tenía la cama previamente
					reversionEgresoForm.setDatosCama("pisoAnterior", egreso.getCamaReversionEgreso().getPiso());
					reversionEgresoForm.setDatosCama("habitacionAnterior", egreso.getCamaReversionEgreso().getHabitacionCama());
					reversionEgresoForm.setDatosCama("tipoHabitacionAnterior", egreso.getCamaReversionEgreso().getTipoHabitacion());
					reversionEgresoForm.setDatosCama("numeroCamaAnterior", egreso.getCamaReversionEgreso().getNumeroCama());
					reversionEgresoForm.setDatosCama("tipoUsuarioAnterior", egreso.getCamaReversionEgreso().getTipoUsuarioCama(false));
					reversionEgresoForm.setDatosCama("nombreCentroCostoAnterior", egreso.getCamaReversionEgreso().getCentroCostoCama(false));
					reversionEgresoForm.setDatosCama("estadoCamaAnterior", egreso.getCamaReversionEgreso().getEstado());
					reversionEgresoForm.setDatosCama("nombreEstadoCamaAnterior", egreso.getCamaReversionEgreso().getNombreEstado());
					reversionEgresoForm.setDatosCama("codigoCamaAnterior", egreso.getCamaReversionEgreso().getCodigoCama());
				}
				
				if(estadoCama == ConstantesBD.codigoEstadoCamaDisponible || estadoCama == ConstantesBD.codigoEstadoCamaDesinfeccion || estadoCama == ConstantesBD.codigoEstadoCamaConSalida || 
				   estadoCamaNueva==ConstantesBD.codigoEstadoCamaDisponible || estadoCamaNueva==ConstantesBD.codigoEstadoCamaDesinfeccion || estadoCamaNueva==ConstantesBD.codigoEstadoCamaConSalida )
				{
					//Si la cama está disponible o en desinfeccion entonces se puede postular la informacion de la cama
					reversionEgresoForm.setDatosCama("codigoCama", reversionEgresoForm.getDatosCama("codigoCamaAnterior").toString());
					reversionEgresoForm.setDatosCama("piso", reversionEgresoForm.getDatosCama("pisoAnterior").toString());
					reversionEgresoForm.setDatosCama("habitacion", reversionEgresoForm.getDatosCama("habitacionAnterior").toString());
					reversionEgresoForm.setDatosCama("tipoHabitacion", reversionEgresoForm.getDatosCama("tipoHabitacionAnterior").toString());
					reversionEgresoForm.setDatosCama("numeroCama", reversionEgresoForm.getDatosCama("numeroCamaAnterior").toString());
					reversionEgresoForm.setDatosCama("tipoUsuario", reversionEgresoForm.getDatosCama("tipoUsuarioAnterior").toString());
					reversionEgresoForm.setDatosCama("nombreCentroCosto", reversionEgresoForm.getDatosCama("nombreCentroCostoAnterior").toString());
				}
			}
	
			
			/** ARREGLO PENDIENTE MIENTRAS SE ENCUENTRA CAUSA DE ERROR **/
			/** ESTÁ APRECIENDO LA FECHA Y HORA DEL EGRESO NULA **/
			//se revisa el estado del egreso-------------------------------
			if(egreso.getFechaEgreso()==null)
				egreso.setFechaEgreso("");
			if(egreso.getHoraEgreso()==null)
				egreso.setHoraEgreso("");
			//----------------------------------------------------------
			/*************************************************************/
			session.setAttribute("egreso", egreso);
	
			
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//Modificacion por tarea 44659
			//Debemos saber si hay que mostrar la cama, luego revisamos si "terminoEgreso"
			if(terminoEgreso&&!reversionEgresoForm.isHospitalDia())
					reversionEgresoForm.setSeDebeMostrarCama(true);
		else
				reversionEgresoForm.setSeDebeMostrarCama(false);
			logger.info("voy para forward principal "+reversionEgresoForm.isSeDebeMostrarCama());
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			if(con != null && !con.isClosed() )
				UtilidadBD.closeConnection(con);
			
			return mapping.findForward("principal");
		}
		catch(SQLException e)
		{
			logger.error("excepcion : "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.transaccion", "errors.transaccion", true); 
		}
	}



	/**
	 * Método que verifica si se puede hacer reversión de egreso bien sea hospitalizacion o urgencias
	 * @param con
	 * @param paciente
	 * @param medico
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward validacionesReversion(Connection con, PersonaBasica paciente, UsuarioBasico medico, HttpServletRequest request, ActionMapping mapping) 
	{
		logger.info("\n entre a validacionesReversion");
		try
		{			
			
			//Verificamos que se pueda hacer el egreso (Si se desean ver las reglas
			//revisar UtilidadValidacion.puedoReversarEgresoHospitalizacion y
			//revisar UtilidadValidacion.puedoReversarEgresoUrgencias)
			resp1 = UtilidadValidacion.puedoReversarEgresoHospitalizacion(con, paciente.getCodigoCuenta(), medico.getCodigoCentroCosto(), medico.getCodigoInstitucionInt());
			resp2 = UtilidadValidacion.puedoReversarEgresoUrgencias(con, paciente.getCodigoCuenta(), medico.getCodigoCentroCosto() );
			logger.info("\n Respuesta 1"+resp1.puedoSeguir);
			logger.info("\n Respuesta 2"+resp2.puedoSeguir);
			
			//No puede ser válido en ambas validaciones
			if(resp1.puedoSeguir && resp2.puedoSeguir)
			{
				if(logger.isDebugEnabled() )
					logger.debug("El código de cuenta " + paciente.getCodigoCuenta() + " no puede tener dos egresos simultáneos");
	
				if(con != null && !con.isClosed() )
					UtilidadBD.closeConnection(con);
	
				request.setAttribute
				(
					"descripcionError",
					"El c&oacute;digo de cuenta " + paciente.getCodigoCuenta() + " no puede tener dos egresos simult&aacute;neos"
				);
				return mapping.findForward("paginaError");
			}
			logger.info("PASO POR AQUÍ        3!!! puedoSeguir1: "+resp1.puedoSeguir+", puedoSeguir2: "+resp2.puedoSeguir);
			//Cuando no es válido en los 2 casos
			logger.info("\n respuesta 1 --> "+resp1.puedoSeguir+"  respuesta 2 --> "+resp2.puedoSeguir);
			if(!(resp1.puedoSeguir || resp2.puedoSeguir) )
			{
				if(logger.isDebugEnabled() )
				{
					logger.debug(resp1.textoRespuesta);
					logger.debug(resp2.textoRespuesta);
				}
	
				if(con != null && !con.isClosed() )
					UtilidadBD.closeConnection(con);
	
				//Ahora no se especifica la razón por la que no puede hacer
				//la reversión del egreso, si en algún momento se necesita
				/*request.setAttribute
				(
					"descripcionError",
					"No se puede hacer reversión de egreso por alguno de los siguientes motivos:<br><ul>" +
						"<li>" + resp1.textoRespuesta + "</li>" +
						"<li>" + resp2.textoRespuesta + "</li>" +
						"</ul>"
				);*/
				request.setAttribute
								(
									"descripcionError",
									"Paciente no tiene egreso para reversar");
				
				logger.info("\n voy a sacar el error "); 
				return mapping.findForward("paginaError");
			}
			logger.info("PASO POR AQUÍ        4!!! esTratante1: "+resp1.esTratante+", esTratante2: "+resp2.esTratante);
			if(!UtilidadValidacion.puedoReversarEgresoPorTiempo(con, paciente.getCodigoCuenta() ) )
			{
				//Ha pasado más de un día desde la última evolución (caso solo orden de salida),
				// o desde el egreso (caso egreso completo) luego mostramos un error
				if(con != null && !con.isClosed() )
					UtilidadBD.closeConnection(con);
	
				request.setAttribute
				(
					"descripcionError",
					"No puede hacer una reversi&oacute;n sobre este egreso, ya que ha pasado m&aacute;s de 24 horas"
				);
				return mapping.findForward("paginaError");
			}
			
			return null;
		}
		catch(SQLException e)
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.transaccion", "errors.transaccion", true);
		}
	}


	/**
	 * Método que realiza las validaciones generales de la reversión del egreso
	 * @param con
	 * @param paciente
	 * @param mapping
	 * @param request
	 * @param medico
	 * @param reversionEgresoForm
	 * @return
	 */
	private ActionForward validacionesGenerales(Connection con, PersonaBasica paciente, ActionMapping mapping, HttpServletRequest request, UsuarioBasico medico, ReversionEgresoForm reversionEgresoForm) 
	{
		String estado = reversionEgresoForm.getEstado();
		try
		{
			//Se realizan las validaciones del paciente
			RespuestaValidacion resp = UtilidadValidacion.esValidoPacienteCargado(con, paciente);
			
			//Primera Condición: El usuario debe existir
			//la validación de si es médico o no solo se hace en insertar
			if(medico == null)
			{
				if(logger.isDebugEnabled() )
					logger.debug("No existe el usuario");
	
				if(con != null && !con.isClosed() )
					UtilidadBD.closeConnection(con);
	
				request.setAttribute("codigoDescripcionError", "errors.usuario.noCargado");
				return mapping.findForward("paginaError");
			}
			else if(!resp.puedoSeguir )
			{
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, resp.textoRespuesta, resp.textoRespuesta, true) ;
			}			
			 // Valida que el ingreso del paciente no se un Ingreso de Empresas SubContratadas			
			else if(IngresoGeneral.esIngresoComoEntidadSubContratada(con,paciente.getCodigoIngreso()).equals(ConstantesBD.acronimoSi))
			{
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.paciente.ingresoPacienteEntidadSub","Ingreso de Paciente en Entidades Subcontratadas. Entidad Subcontratada "+EntidadesSubContratadas.getDescripcionEntidadSubXIngreso(con,paciente.getCodigoIngreso()+"") , false);	
			}			
			//Se verifica que el ingreso esté abierto
			else if(!UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getAcronimo().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
			{
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.paciente.noIngreso", "errors.paciente.noIngreso", true) ;
			}
			else if(paciente.getCodigoCuenta()==0||paciente.getExisteAsocio()) 
			{
				
				//Puede ser que el paciente este en asocio de cuenta
				if (paciente.getExisteAsocio()&&paciente.getCodigoCuentaAsocio()<=0)//tiene asocio pero no se ha completado bien.
				{
					if(paciente.getExisteAsocio())
	
					//El paciente no tiene cuenta, saco error
					if(logger.isDebugEnabled() )
						logger.debug("paciente en asocio de cuenta");
	
					if(con != null && !con.isClosed() )
						UtilidadBD.closeConnection(con);
	
					request.setAttribute("codigoDescripcionError", "error.reversionegreso.asocioCuenta");
					return mapping.findForward("paginaError");
				}
				if(paciente.getCodigoCuenta()==0)
				{
					//El paciente no tiene cuenta, saco error
					if(logger.isDebugEnabled() )
						logger.debug("paciente sin cuenta");
		
					if(con != null && !con.isClosed() )
						UtilidadBD.closeConnection(con);
		
					request.setAttribute("codigoDescripcionError", "errors.paciente.cuentaNoAbierta");
					return mapping.findForward("paginaError");
				}
			}
			if(UtilidadValidacion.estaEnProcesofacturacion(con, paciente.getCodigoPersona(), "" /*idSesion*/))
			{
				request.setAttribute("codigoDescripcionError", "error.facturacion.cuentaEnProcesoFact");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
			/**
			 * Validar concurrencia
			 * Si ya está en proceso de distribucion, no debe dejar entrar
			 **/
			else if(UtilidadValidacion.estaEnProcesoDistribucion(con, paciente.getCodigoPersona(), medico.getLoginUsuario()) )
			{
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoDistribucion", "error.facturacion.cuentaEnProcesoDistribucion", true);
			}
			/**
			 * Validar concurrencia
			 * Si ya está en proceso de facturación, no debe dejar entrar
			 **/
			else if(UtilidadValidacion.estaEnProcesofacturacion(con, paciente.getCodigoPersona(), "") )
			{
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoFact", "error.facturacion.cuentaEnProcesoFact", true);
			}
			else
			if (paciente.getCodigoUltimaViaIngreso()!=ConstantesBD.codigoViaIngresoHospitalizacion&&paciente.getCodigoUltimaViaIngreso()!=ConstantesBD.codigoViaIngresoUrgencias)
			{
				//El paciente no tiene cuenta, saco error
				if(logger.isDebugEnabled() )
					logger.debug("Paciente con vía de ingreso diferente a urgencias / hospitalización");
	
				if(con != null && !con.isClosed() )
					UtilidadBD.closeConnection(con);
	
				request.setAttribute("codigoDescripcionError", "error.reversionegreso.viaIngresoNoValida");
				return mapping.findForward("paginaError");
			}
			else if(estado == null || estado.equals("") )
			{
				reversionEgresoForm.reset();
	
				if(logger.isDebugEnabled() )
					logger.debug("La acción especificada no está definida");
	
				if(con != null && !con.isClosed() )
					UtilidadBD.closeConnection(con);
	
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				return mapping.findForward("paginaError");
			}
			
			return null;
		}
		catch(SQLException e)
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.transaccion", "errors.transaccion", true); 
		}
	}

	/**
	 * Este método se encarga de manejar la funcionalidad necesaria
	 * (Que es practicamente comun entre actions) para manejar el
	 * estado cancelar
	 * 
	 * @param mapping Mapa (control) de Struts
	 * @param request request de la pagina web
	 * @param response response de la pagina web
	 * @param con Conexi?n con la fuente de datos
	 * @param egresoForm Forma de Egreso
	 * @return
	 * @throws Exception
	 */
	public ActionForward accionEstadoCancelar
	(
		HttpServletRequest request,
		HttpServletResponse response,
		Connection con,
		ReversionEgresoForm reversionEgresoForm
	)throws Exception
	{
		String paginaSiguiente;

		if(logger.isDebugEnabled() )
			logger.debug("La acción especificada es cancelar");

		paginaSiguiente = request.getParameter("paginaSiguiente");
		reversionEgresoForm.reset();

		if(con != null && !con.isClosed() )
			UtilidadBD.closeConnection(con);

		response.sendRedirect(paginaSiguiente);

		return null;
	}

	/**
	 * Este método se encarga de manejar la funcionalidad correspondiente 
	 * al manejo de errores (Guardar mensaje error, cerrar conexión y mostrar
	 * mensaje error al usuario)
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param mapping Mapping de las localizaciones en struts
	 * @param egresoForm Forma de esta funcionalidad
	 * @param request Objeto request
	 * @param textoLog Texto a guardar en el log
	 * @param textoUsuario Texto a mostrar al usuario
	 * @param esCodigoError, true si el texto del error es un codigo/id de
	 * error, false de lo contrario
	 * @return
	 * @throws SQLException
	 */
	private ActionForward funcionalidadEnviarError(Connection con, ActionMapping mapping, ReversionEgresoForm reversionEgresoForm, HttpServletRequest request, String textoLog, String textoUsuario, boolean esCodigoError) throws Exception
	{
		reversionEgresoForm.reset();

		if( logger.isDebugEnabled() )
			logger.debug(textoLog);

		if (con!=null&&!con.isClosed())
			UtilidadBD.closeConnection(con);

		if( esCodigoError )
			request.setAttribute("codigoDescripcionError", textoUsuario);
		else
			request.setAttribute("descripcionError", textoUsuario);

		return mapping.findForward("paginaError");
	}


}