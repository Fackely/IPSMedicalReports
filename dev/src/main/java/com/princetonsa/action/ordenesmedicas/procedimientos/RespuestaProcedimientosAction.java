/**
 * 
 */
package com.princetonsa.action.ordenesmedicas.procedimientos;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesCamposParametrizables;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.consultaExterna.UtilidadesConsultaExterna;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.interfaces.UtilidadBDInterfaz;
import util.laboratorios.UtilidadLaboratorios;
import util.manejoPaciente.UtilidadesManejoPaciente;
import util.ordenesMedicas.UtilidadesOrdenesMedicas;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.ordenesmedicas.procedimientos.RespuestaProcedimientosForm;
import com.princetonsa.dto.facturacion.DtoArticuloIncluidoSolProc;
import com.princetonsa.dto.inventario.DtoAutorizacionEntSubcontratadasCapitacion;
import com.princetonsa.dto.inventario.DtoServiciosAutorizaciones;
import com.princetonsa.dto.ordenes.DtoSolicitud;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.Medico;
import com.princetonsa.mundo.ObservableBD;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.agenda.Cita;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.CargosEntidadesSubcontratadas;
import com.princetonsa.mundo.historiaClinica.Epicrisis1;
import com.princetonsa.mundo.historiaClinica.Plantillas;
import com.princetonsa.mundo.ordenesmedicas.procedimientos.RespuestaProcedimientos;
import com.princetonsa.mundo.ordenesmedicas.solicitudes.Procedimiento;
import com.princetonsa.mundo.parametrizacion.Servicio;
import com.princetonsa.mundo.solicitudes.DocumentoAdjunto;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.princetonsa.mundo.solicitudes.SolicitudProcedimiento;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.orm.AutorizacionesEntSubServi;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.ManejoPacienteServicioFabrica;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.interfaz.facturacion.convenio.IConvenioServicio;
import com.servinte.axioma.servicio.interfaz.inventario.IAurorizacionesEntSubCapitacionServicio;

/**
 * @author Jorge Armando Osorio Velasquez
 * @author Wilson Rios
 * @author Mantenimiento Jose Eduardo Arias Doncel
 *
 */
public class RespuestaProcedimientosAction extends Action
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(ProcedimientoAction.class);
	
	
	/**
	 * Servicios necesarios para la implementaci�n de la funcionalidad
	 */
	IAurorizacionesEntSubCapitacionServicio aurorizacionesEntSubCapitacionServicio = ManejoPacienteServicioFabrica.crearAurorizacionesEntSubCapitacionServicio(); 
	IConvenioServicio convenioServicio = FacturacionServicioFabrica.crearConvenioServicio();
	
	
	/** * Contiene la lista de mensajes correspondiente a esta forma */
	MessageResources fuenteMensaje = MessageResources.getMessageResources("com.servinte.mensajes.manejoPaciente.AutorizacionesIngresoEstanciaForm");
	
	
	/**
	 * Cadena que contiene los indices del mapa.
	 */
	private static final String indicesMapa[]={	"solicitud_", 
												"ccsolicitante_",
												"fechasolicitud_",
												"horasolicitud_",
												"orden_",
												"servicio_",
												"procedimiento_",
												"centroatencion_",
												"codigoviaingreso_",
												"codigopaciente_",											
												"nombrepaciente_",
												"cuenta_",											
												"viaingreso_",											
												"solicitante_",												
												"codigoestadohc_",
												"tiposolicitud_",
												"requierediagnostico_",											
												"estadohc_",
												"urgente_",											
												"modificarestado_",											
												"diagnostico_",
												"cama_",
												"cantidadincluidos_",
												"indqx_",
												"peticion_",
												"inclart_",
												"inclserv_"
												}; 
	
	
	/**
	 * M�todo encargado de el flujo y control de la funcionalidad
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */	
	public ActionForward execute(	ActionMapping mapping, 
														ActionForm form, 
														HttpServletRequest request,
														HttpServletResponse response) throws Exception
														{
		Connection con = null;
		try {
			if (response==null); //Para evitar que salga el warning	
			if( form instanceof RespuestaProcedimientosForm)
			{

				con=UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				RespuestaProcedimientosForm forma=(RespuestaProcedimientosForm)form;
				RespuestaProcedimientos mundo=new RespuestaProcedimientos();			
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				PersonaBasica paciente= Utilidades.getPersonaBasicaSesion(request.getSession());			
				String estado=forma.getEstado();

				//**********************************************************************************************************************

				logger.info("\n\n ESTADO RESPUESTA PROCEDIMIENTOS======="+estado);

				if (usuario == null) 
				{	
					request.setAttribute("codigoDescripcionError",	"errors.usuario.noCargado");
					return mapping.findForward("paginaError");
				} 

				//Validaciones no aplican para el flujo de responder procedimientos entidades subcontratadas

				if(!estado.equals("menuRes") 
						&& !forma.getTipoOrigen().equals(RespuestaProcedimientos.codIngPacRes) 
						&& !forma.getTipoOrigen().equals(RespuestaProcedimientos.codIngRangoRes))
				{

					if(!forma.isVieneDePyp()&&UtilidadesManejoPaciente.obtenerTipoEntidadEjecutaCentroCosto(con,usuario.getCodigoCentroCosto()).equals(ConstantesIntegridadDominio.acronimoExterna))
					{
						request.setAttribute("codigoDescripcionError", "error.ordenes.centroCostoInvalidoRespProce");
						UtilidadBD.cerrarConexion(con);
						logger.info("VIENE DE PYP? "+forma.isVieneDePyp());
						if(forma.isVieneDePyp())
						{
							request.setAttribute("ocultarEncabezadoErrores", "true");
						}


						return mapping.findForward("paginaError");

					}	

					if( !UtilidadValidacion.esProfesionalSalud(usuario) )
					{
						//Validaci�n de Parametrizaci�n de Centros de Costos marcado como Registro de Respuesta Procedimiento X Terceros
						if(Utilidades.esCentroCostoRespuestaProcTercero(con,usuario.getCodigoCentroCosto()).equals(ConstantesBD.acronimoSi))
						{
							logger.info("\n\nUsuario no valido, pero posee centro de Costos parametrizado como Registro de Respuesta Procedimiento X Terceros");
							forma.setMostrarProcedimientosDyT(ConstantesBD.acronimoNo);
						}
						else
						{
							if(UtilidadValidacion.estaMedicoInactivo(con,usuario.getCodigoPersona(),usuario.getCodigoInstitucionInt()))
								request.setAttribute("codigoDescripcionError", "errors.profesionalSaludInactivo");
							else
								request.setAttribute("codigoDescripcionError", "errors.usuario.noAutorizado");
							UtilidadBD.cerrarConexion(con);

							if(forma.isVieneDePyp())
							{
								request.setAttribute("ocultarEncabezadoErrores", "true");
							}

							return mapping.findForward("paginaError");

						}
					}


					/**
					 * Validar concurrencia
					 * Si ya est� en proceso de distribucion, no debe dejar entrar
					 **/
					else if(UtilidadValidacion.estaEnProcesoDistribucion(con, paciente.getCodigoPersona(), usuario.getLoginUsuario()) )
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoDistribucion", "error.facturacion.cuentaEnProcesoDistribucion", true);
					}
					/**
					 * Validar concurrencia
					 * Si ya est� en proceso de facturaci�n, no debe dejar entrar
					 **/
					else if(UtilidadValidacion.estaEnProcesofacturacion(con, paciente.getCodigoPersona(), "") )
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoFact", "error.facturacion.cuentaEnProcesoFact", true);
					}

					//Se verifica si viene de PYP para hacer las validaciones de al responder procedimiento
					if(forma.isVieneDePyp())
					{
						request.setAttribute("ocultarEncabezadoErrores", "true");
						int numSol=(forma.getNumeroSolicitud()==null||forma.getNumeroSolicitud().trim().equals(""))?-1:forma.getNumeroSolicitudInt();
						if(numSol>0)
						{
							if(Utilidades.esSolicitudPYP(con, numSol) && !estado.equals("resumen"))  //presenta error en esta validacion cuando es el resumen de una solicitud de pyp.
							{
								if(!UtilidadValidacion.medicoPuedeResponderSolicitud(con,numSol,usuario.getCodigoCentroCosto()))
								{
									return ComunAction.accionSalirCasoError(mapping, request, con, logger, "El usuario no teine Centro de Costo adecuado para Responder", "El usuario no tiene Centro de Costo adecuado para Responder", false);
								}
							}
						}

					}
				}

				//**********************************************************************************************************************
				//**********************************************************************************************************************
				//**********************************************************************************************************************

				if(estado.equals("empezar"))
				{
					forma.reset(true);				
					forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("subMenuOrdenes");
				}
				else if(estado.equals("listadoPaciente") || estado.equals("listadoPacienteBotonVolver"))
				{
					forma.setFiltro("");
					if(estado.equals("listadoPacienteBotonVolver"))
					{
						forma.reset(false);
						mundo.reset(true);
						forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
					}
					ActionForward forward= validacionAccesoPaciente(paciente, request, mapping,usuario);
					if(forward!=null)
					{
						UtilidadBD.closeConnection(con);
						return forward;
					}
					forma.setLinkVolver("../respuestaProcedimientos/respuestaProcedimientos.do?estado=listadoPacienteBotonVolver");
					this.accionListarPaciente(con,forma,mundo,paciente,usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listado");
				}			
				else if(estado.equals("listadoRango") || estado.equals("listadoRangoBotonVolver"))
				{
					if(estado.equals("listadoRangoBotonVolver"))
					{
						forma.reset(false);
						mundo.reset(true);
						forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
					}
					forma.setLinkVolver("../respuestaProcedimientos/respuestaProcedimientos.do?estado=listadoRangoBotonVolver&fechaInicialFiltro="+forma.getFechaInicialFiltro()+"&fechaFinalFiltro="+forma.getFechaFinalFiltro()+
							"&areaFiltro="+forma.getAreaFiltro()+"&pisoFiltro="+forma.getPisoFiltro()+"&habitacionFiltro="+forma.getHabitacionFiltro()+"&camaFiltro="+forma.getCamaFiltro());
					accionListarRango(con,forma,mundo,paciente,usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listado");
				}
				else if(estado.equals("rango"))
				{
					String fechaActual=UtilidadFecha. getFechaActual(con);
					forma.setFechaInicialFiltro(UtilidadFecha.incrementarDiasAFecha(fechaActual, -1, false));
					forma.setFechaFinalFiltro(fechaActual);
					forma.setCentroCostosSolicitanteFiltro("");
					forma.setFiltro("filtroRangoEjecutado");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("filtroSolicitudes");
				}
				else if(estado.equals("ordenar"))
				{
					this.accionOrdenarMapa(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listado");
				}
				else if(estado.equals("modificarEstadoSolicitud"))
				{
					if(!forma.getCodigoEstadoNuevo().trim().equals(""))
					{
						this.accionCambiarEstadoSolicitud(con,forma,mundo,usuario,paciente,request);
					}

					forma.setFechaCambio("");
					forma.setHoraCambio("");
					forma.setCodigoEstadoNuevo("");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listado");
				}			
				else if(estado.equals("elegirPlantilla") || estado.equals("cargarPlantilla"))
				{
					this.cargarPaciente(con, request, usuario, forma);
					forma.setMostrarEnlaceOrdenesAmbulatorias(Plantillas.consultarVisibilidadPlantillaFijaSinOrden(con, forma.getCodigoPlantillaPk(),ConstantesCamposParametrizables.formatoRespuestaCitas ));
					if(estado.equals("elegirPlantilla"))
					{
						HashMap validaciones=new  HashMap(); 	
						//*********************** SE VALIDAN LOS PARAMETROS GENERALES ************************
						validaciones=UtilidadesConsultaExterna.validacionesBloqueoAtencionCitas(con, usuario, paciente.getCodigoPersona());
						ActionErrors errores1 = new ActionErrors(); 
						errores1 =(ActionErrors)validaciones.get("errores");

						if(!errores1.isEmpty())
						{
							forma.setCitasIncumplidas((ArrayList<HashMap<String, Object>>)validaciones.get("citasincumplidas"));
							forma.setEstado("listaCitasIncumplidas");
							if(forma.getListaSolicitudes("numRegistros")==null)
							{
								forma.setListaSolicitudes("numRegistros", "0");
							}

							saveErrors(request, errores1);
							UtilidadBD.closeConnection(con);
							return mapping.findForward("listado");
						}

						//*************************************************************************************
						forma.reset(true);


						if(accionElegirPlantilla(con,usuario,forma,request, paciente))
						{
							if(forma.getEstado().equals("resumen"))
							{
								return this.accionResumen(con, forma, mundo,paciente, usuario, mapping, request);
							}
							else
							{
								UtilidadBD.closeConnection(con);
								return mapping.findForward("listadoPlantillas");
							}
						}
					}


					// Anexo 41 - Cambio 1.52
					forma.setSinAutorizacionEntidadsubcontratada(false);
					ArrayList<String> listaAdvertencias = new ArrayList<String>();
					listaAdvertencias = validacionCapitacion(forma, usuario);

					if(!Utilidades.isEmpty(listaAdvertencias))
					{
						forma.setSinAutorizacionEntidadsubcontratada(true);
					}

					forma.setListaAdvertencias(listaAdvertencias);
					//------------------------------------

					return this.accionDetalle(con, forma, mundo, mapping, request,paciente,usuario);
				}
				else if(estado.equals("filtrarEspecialidad"))
				{
					logger.info("\n\n Entro a filtrar en el estado filtrarEspecialidad");
					accionFiltrarEspecialidadesProfesional(con,forma,response);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listado");
				}

				else if(estado.equals("cambiarDiagnosticoPlantillas"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listadoPlantillas");
				}			
				else if (estado.equals("redireccion"))// estado para mantener los datos del pager
				{			    
					UtilidadBD.cerrarConexion(con);
					forma.getLinkSiguiente();
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				else if(estado.equals("modificarCheck"))
				{
					for(int i=0;i<Integer.parseInt(forma.getListaSolicitudes("numRegistros")+"");i++)
					{
						forma.setListaSolicitudes("modificarestado_"+i,forma.getValorCheck());
					}
					UtilidadBD.cerrarConexion(con);
					return null;
				}
				else if(estado.equals("resumen"))
				{
					forma.setCodigoCita("");
					return this.accionResumen(con, forma, mundo,paciente, usuario, mapping, request);
				}
				else if(estado.equals("resumenServicioConsultaExt"))
				{
					return this.accionResumen(con, forma, mundo,paciente,usuario, mapping, request);
				}
				else if(estado.equals("continuarConErrores"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");
				}
				else if (estado.equals("recargarPlantilla"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("seccionesValoresParametrizables");
				}
				else if(estado.equals("imprimir"))
				{
					String codigos="-1";
					for (int i=0; i<Integer.parseInt(forma.getListaSolicitudes("numRegistros")+""); i ++)
					{    
						if(UtilidadTexto.getBoolean(forma.getListaSolicitudes("modificarestado_"+i)+""))
						{
							codigos+=","+forma.getListaSolicitudes("solicitud_"+i);
						}
					}
					return this.accionGenerarReporte(con, forma, mapping, request, paciente, usuario,codigos);						
				}
				//*********************************************************************************************
				else if(estado.equals("guardar"))
				{
					return this.accionGuardar(con, forma, mapping, request, paciente, usuario, mundo);
				}
				//*********************************************************************************************
				else if(estado.equals("cargarIncluidosSolicitud"))
				{
					accionCargarIncluidosSolicitud(con,forma, usuario.getCodigoInstitucionInt());
					UtilidadBD.closeConnection(con);
					return mapping.findForward("incluidos");
				}
				//*********************************************************************************************
				else if(estado.equals("confirmarCantInclu"))
				{
					accionConfirmarCantInclu(forma);				
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("incluidos");				
				}
				//*********************************************************************************************
				else if(estado.equals("addOtroComentario"))
				{
					accionAddOtroComentario(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");
				}				
				//*********************************************************************************************
				else if(estado.equals("eliminarOtroComentario"))
				{
					accionEliminarOtroComentario(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");
				}
				else if(estado.equals("verEstadoAutorizacion"))
				{
					return this.accionEstadosAutorizacion(forma, con,mapping);
				}
				else if(estado.equals("mostrarDetalleCitasIncumplidas"))
				{
					return mapping.findForward("citasIncumplidas");
				}
				//*********************************************************************************************
				else if(estado.equals("menuRes"))
				{
					forma.setLinkVolver("volverMenuRes");
					forma.reset(true);				
					forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));

					accionMenuRespuestaEntidadesSub(con,forma,usuario,request);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("menuRes");
				}
				else if(estado.equals("iniciarPacienteRes"))
				{
					ActionForward forward= validacionAccesoPaciente(paciente, request, mapping,usuario);

					if(forward!=null)
					{
						UtilidadBD.closeConnection(con);
						return forward;
					}

					forma.setLinkVolver("../respuestaProcedimientos/respuestaProcedimientos.do?estado=buscarPacienteRes");
					forma.setLinkVolverListado("../responderprocentidsub/responderprocentidsub.do?estado=iniciarPacienteRes");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("entidadPacienteRes");
				}
				else if(estado.equals("buscarPacienteRes"))
				{
					this.accionListarPacienteEntidadesSub(con,forma,mundo,paciente,usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listado");
				}
				else if(estado.equals("iniciarRangoRes"))
				{
					forma.setLinkVolverListado("../responderprocentidsub/responderprocentidsub.do?estado=volverRangoRes");
					String fechaActual=UtilidadFecha. getFechaActual(con);
					forma.setFechaInicialFiltro(UtilidadFecha.incrementarDiasAFecha(fechaActual, -1, false));
					forma.setFechaFinalFiltro(fechaActual);
					forma.setCentroCostosSolicitanteFiltro("");
					forma.setFiltro("filtroRangoEjecutado");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("rangoRes");
				}
				else if(estado.equals("volverRangoRes"))
				{
					return mapping.findForward("rangoRes");
				}
				else if(estado.equals("buscarRangoRes"))
				{
					forma.setLinkVolver("../respuestaProcedimientos/respuestaProcedimientos.do?estado=buscarRangoRes&fechaInicialFiltro="+forma.getFechaInicialFiltro()+"&fechaFinalFiltro="+forma.getFechaFinalFiltro()+
							"&areaFiltro="+forma.getAreaFiltro()+"&pisoFiltro="+forma.getPisoFiltro()+"&habitacionFiltro="+forma.getHabitacionFiltro()+"&camaFiltro="+forma.getCamaFiltro()+"&codigoEntidadSub="+forma.getCodigoEntidadSub());
					accionListarRangoEntidadesSub(con, forma, mundo, paciente, usuario);
					//logger.info("numero de elementos "+forma.getListaSolicitudes().get("numRegistros"));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listado");
				}
				else if(estado.equals("guardarObservacionesConsultaExterna"))
				{
					forma.setEstado("resumenServicioConsultaExt");
					String observacionesRes=UtilidadCadena.cargarObservaciones(forma.getNuevasObservaciones(),forma.getObservaciones(), usuario);
					RespuestaProcedimientos.actualizarObservacionesRespuesta(con,forma.getCodigoRespuesta(),observacionesRes);
					forma.setNuevasObservaciones("");
					return this.accionResumen(con, forma, mundo,paciente,usuario, mapping, request);
				}

				//*********************************************************************************************
				else/*-----------------------------------------------
					Estado encargado de direccionar la respuesta
					de los procedimientos a la funcionalidad 
					correspondiente.
			 	 -----------------------------------------------*/
					if(estado.equals("seleccionRespuesta"))
					{
						UtilidadBD.closeConnection(con);
						return mapping.findForward("listado");
					}

					else
					{
						UtilidadBD.closeConnection(con);
					}

			}
			return null;
		} catch (Exception e) {
			Log4JManager.error(e);
			return null;
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
	}
	
	
	//**********************************************************************************************************************
	//**********************************************************************************************************************
	//**********************************************************************************************************************
	//Metodos***************************************************************************************************************

	
	private ActionForward accionGenerarReporte(
			Connection con,
			RespuestaProcedimientosForm forma,
			ActionMapping mapping,
			HttpServletRequest request, 
			PersonaBasica paciente, 
			UsuarioBasico usuario,
			String codigos) 
	{	
		String nombreRptDesign = "impresionProcedimientos.rptdesign";
		
		//PRIMERO SE INSERTA LA INFORMACION DEL CABEZOTE
        DesignEngineApi comp;
        InstitucionBasica ins= new InstitucionBasica();
        ins.cargarXConvenio(usuario.getCodigoInstitucionInt(), paciente.getCodigoConvenio());
        
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"ordenes/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        comp.insertLabelInGridPpalOfHeader(1,1,"REPORTE PROCEDIMIENTOS");
        comp.insertLabelInGridPpalOfHeader(2,1,usuario.getCentroAtencion()+" - "+usuario.getCentroCosto());
        
        if (UtilidadCadena.noEsVacio(forma.getFechaInicialFiltro()) && UtilidadCadena.noEsVacio(forma.getFechaFinalFiltro()))
        	comp.insertLabelInGridPpalOfHeader(3,1, "Del  "+forma.getFechaInicialFiltro()+"  Al  "+forma.getFechaFinalFiltro());
        else
        	comp.insertLabelInGridPpalOfHeader(3,1, "Del Paciente "+paciente.getApellidosNombresPersona(false));
                
        comp.insertLabelInGridPpalOfFooter(0,0,"USUARIO: "+usuario.getLoginUsuario());        
            
        comp.obtenerComponentesDataSet("impresionProcedimientos");
        
        String cadena="s.numero_solicitud in ("+codigos+")";
        
        String newQuery=comp.obtenerQueryDataSet().replaceAll("2=3", cadena+" ORDER BY getnombrepersona(c.codigo_paciente), s.consecutivo_ordenes_medicas");
        newQuery = newQuery.replace("valorTrueParaConsultas", ValoresPorDefecto.getValorTrueParaConsultas());
        
        //Se modifica el query
        comp.modificarQueryDataSet(newQuery);
                
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        comp.updateJDBCParameters(newPathReport);
        
        //Se mandan los par�metros al reporte
        //newPathReport += "&centro_atencion="+;
        
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
        
        UtilidadBD.closeConnection(con);
		return mapping.findForward("listado");
		
	}
	
	//*********************************************************************************************************
	/**
	 * Carga los datos necesarios para escoger la parametrizacion de plantilla a llenar
	 * @param Connection con
	 * @param UsuarioBasico usuario
	 * @param RespuestaProcedimientosForm forma
	 * @param HttpServletRequest request
	 * */
	private boolean accionElegirPlantilla(
			Connection con,
			UsuarioBasico usuario,
			RespuestaProcedimientosForm forma,
			HttpServletRequest request,
			PersonaBasica paciente)
	{
		int servicio=ConstantesBD.codigoNuncaValido;
		
		String funcionalidad = request.getParameter("funcionalidad");
		forma.setFuncionalidad("");
		if(funcionalidad != null)
			forma.setFuncionalidad(funcionalidad);
		
		String peticion = request.getParameter("peticion");
		forma.setCodigoPeticion("");
		if(funcionalidad != null)
			forma.setCodigoPeticion(peticion);
		
		//Se verifica si el procedimiento viene de agenda de servicios********************
		String codigoCita = request.getParameter("codigoCita");
		if(codigoCita==null)
			forma.setCodigoCita("");
		else
		{
			forma.setCodigoCita(codigoCita);
			//Se verifica si se debe mostrar el link de otros servicios de la cita
			if(UtilidadValidacion.esValidoPacienteCargado(con, paciente).puedoSeguir&&
				UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getAcronimo().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto)&&
				UtilidadesConsultaExterna.deboMostrarOtrosServiciosCita(con, forma.getCodigoCita()))
				forma.setDeboMostrarOtrosServiciosCita(true);
			else
				forma.setDeboMostrarOtrosServiciosCita(false);
		}
		//*******************************************************************************		
				
		logger.info("VALOR VARIABLE QUE VIENE DE PYP=> "+forma.isVieneDePyp());
		forma.setVieneDePyp(UtilidadTexto.getBoolean(request.getParameter("vieneDePyp")));
		logger.info("VALOR VARIABLE request QUE VIENE DE PYP=> "+request.getParameter("vieneDePyp"));
		
		
		if(request.getParameter("servicio")==null)
			servicio=UtilidadesOrdenesMedicas.obtenerServicioSolicitud(con, request.getParameter("numeroSolicitud").toString());
		else
			servicio=Utilidades.convertirAEntero(request.getParameter("servicio").toString());
		
		forma.setNumeroSolicitud(request.getParameter("numeroSolicitud").toString());
		forma.setCodigoServicio(servicio+"");
		
		//Evalua si debe mostrar como resumen la respuesta de Procedimiento
		if(forma.getFuncionalidad().equals("hqx"))
		{
			HashMap respuesta =  UtilidadesOrdenesMedicas.obtenerSolCirugiaServicio(con,forma.getNumeroSolicitud(),forma.getCodigoServicio());
									
			if(!respuesta.get("numRegistros").toString().equals("0"))
			{				
				forma.setCodigoSolCirugiaSer(respuesta.get("codigo_0").toString());
				ArrayList<String> array = Procedimiento.obtenerCodigosRespuestas(con,forma.getNumeroSolicitud(),respuesta.get("codigo_0").toString());
				
				if(array.size() > 0)
				{
					forma.setEstado("resumen");
					forma.setCodigoRespuesta(array.get(0));
					return true;
				}
			}
		}
		
		//carga el Dto de Plantilla 
		forma.setPlantillasServDiagArray(Servicio.cargarFormulariosServicioArray(con, servicio, usuario.getCodigoInstitucionInt()));
		
		if(forma.getPlantillasServDiagArray().size() > 1)
			return true;
		else if(forma.getPlantillasServDiagArray().size() == 1)
		{
			forma.setCodigoPlantillaPk(forma.getPlantillasServDiagArray().get(0).getCodigoPkPlantilla());
			return false;
		}
		
		forma.setCodigoPlantillaPk(0);
		return false;		
	}
	
	//*********************************************************************************************************

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param paciente 
	 * @return
	 */
	private ActionForward accionDetalle(Connection con, RespuestaProcedimientosForm forma, RespuestaProcedimientos mundo, 
			ActionMapping mapping,HttpServletRequest request, PersonaBasica paciente, UsuarioBasico usuario) 
	{	
		accionCargarDtoPlantilla(con,forma,forma.getCodigoServicio(),paciente,usuario);	
		accionCargarDtoArticulosIncluidos(con,forma,paciente,usuario);
		//Carga la informacion del Dto de Procedimiento
		accionCargarDtoProcedimiento(con,request,forma,paciente,usuario);		
				
		if(forma.getFuncionalidad().equals("hqx"))		
			llenarForma(forma, mundo, false, forma.getCodigoSolCirugiaSer());
		if(!llenarForma(forma, mundo, false,""))
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error", "error.respuestaPrcedimientos.noCargoSolicitud", true);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalle");
	}
	
	//*********************************************************************************************************

	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param esResumen
	 * @return
	 */
	private boolean llenarForma(
			RespuestaProcedimientosForm forma, 
			RespuestaProcedimientos mundo, 
			boolean esResumen,
			String codigoSolCxServicio)
	{
		logger.info("valor de los campos pasados por action >> "+forma.getNumeroSolicitud()+" "+esResumen+" "+forma.getCodigoRespuesta());
		boolean cargo=mundo.cargarRespuestaProcedimientos(forma.getNumeroSolicitud(),esResumen, forma.getCodigoRespuesta(),codigoSolCxServicio);
		
		forma.setCodigoRespuesta(mundo.getCodigoRespuesta());
		forma.setCodigoTipoRecargo(mundo.getCodigoTipoRecargo());
		forma.setComentariosHistoriaClinica(mundo.getComentariosHistoriaClinica());
		forma.setDatosHistoriaClinica(mundo.getDatosHistoriaClinica());
		forma.setDiagnosticos(mundo.getDiagnosticos());
		forma.setFechaEjecucion(mundo.getFechaEjecucion());
		forma.setFinalizar(mundo.getFinalizar());
		forma.setHoraEjecucion(mundo.getHoraEjecucion());
		forma.setNumDiagnosticos(mundo.getNumDiagnosticos());
		forma.setObservaciones(mundo.getObservaciones());
		forma.setResultados(mundo.getResultados());
		forma.setResultadosAnteriores(mundo.getResultadosAnteriores());
		forma.setSolicitudProcedimiento(mundo.getSolicitudProcedimiento());
		forma.setNombreTipoRecargo(mundo.getNombreTipoRecargo());
		forma.setPortatil(mundo.getPortatil());
		forma.setFinalidadRespuesta(mundo.getFinalidad());
		
		if(esResumen)
		{
			int numAdjuntos = mundo.getDocumentosAdjuntos().getNumDocumentosAdjuntos();
			forma.setNumDocumentosAdjuntos(numAdjuntos);
			
			for( int i=0; i<numAdjuntos; i++ )
			{
				DocumentoAdjunto documento = mundo.getDocumentosAdjuntos().getDocumentoAdjunto(i);
				
				forma.setDocumentoAdjuntoGenerado("original_"+i, documento.getNombreOriginal());
				forma.setDocumentoAdjuntoGenerado("generado_"+i, documento.getNombreGenerado());
				forma.setDocumentoAdjuntoGenerado("codigo_"+i, documento.getCodigoArchivo()+"");
				forma.setDocumentoAdjuntoGenerado(""+i, documento.getNombreGenerado()+"@"+documento.getNombreOriginal());
			}			
		}		
		return cargo;	
	}
	//*********************************************************************************************************
	
	/**
	 * 
	 * @param forma
	 * @param mundo
	 */
	private void llenarMundo(
			RespuestaProcedimientosForm forma,
			RespuestaProcedimientos mundo) 
	{
		mundo.setCodigoRespuesta(forma.getCodigoRespuesta());
		mundo.setCodigoTipoRecargo(forma.getCodigoTipoRecargo());
		mundo.setComentariosHistoriaClinica(forma.getComentariosHistoriaClinica());
		mundo.setDatosHistoriaClinica(forma.getDatosHistoriaClinica());
		mundo.setDiagnosticos(forma.getDiagnosticos());
		mundo.setFechaEjecucion(forma.getFechaEjecucion());
		mundo.setFinalizar(forma.getFinalizar());
		mundo.setHoraEjecucion(forma.getHoraEjecucion());
		mundo.setNumDiagnosticos(forma.getNumDiagnosticos());
		mundo.setNumeroSolicitud(forma.getNumeroSolicitud());
		mundo.setObservaciones(forma.getObservaciones());
		mundo.setResultados(forma.getResultados());
		mundo.setResultadosAnteriores(forma.getResultadosAnteriores());
		mundo.setSolicitudProcedimiento(forma.getSolicitudProcedimiento());
		mundo.setNombreTipoRecargo(forma.getNombreTipoRecargo());
		mundo.setPortatil(forma.getPortatil());
		mundo.setEspecialidadSolicitada(forma.getProcedimientoDto().getRespuestaProceEspecificoDto().getCodEspecialidadProfResponde());
	    mundo.setNomEspecialidadSolicitada(forma.getProcedimientoDto().getRespuestaProceEspecificoDto().getNombreEspecialidadProfResponde());

	    mundo.setFinalidad(forma.getFinalidadRespuesta());
	}
	
	//*********************************************************************************************************
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardar(Connection con, RespuestaProcedimientosForm forma, ActionMapping mapping,
			HttpServletRequest request, PersonaBasica paciente, UsuarioBasico usuarioLogeado, RespuestaProcedimientos mundo) throws IPSException 
	{
		
		//se limpian los mensajes de la intefaz
		 forma.resetInterfaz ();
		
		//*************************************************************************************
		UsuarioBasico medico;
		//Si el centro de costo del usuario esta parametrizado como Centro Costo Respuesta Procedimiento X Tercero, la informaci�n del medico 
		//debe tomarce de la ingresa por el usuario en Profesional que Responde
		if(forma.getProcedimientoDto().getIndicativoCentroCostoRegRespuestaProc().equals(ConstantesBD.acronimoSi))
		{
			medico = new UsuarioBasico();
			try
			{
				logger.info("carga el medico por Profesional que responde >> "+forma.getProcedimientoDto().getRespuestaProceEspecificoDto().getCodigoMedicoCentroCostoTercero());
				medico.cargarUsuarioBasico(con,Utilidades.convertirAEntero(forma.getProcedimientoDto().getRespuestaProceEspecificoDto().getCodigoMedicoCentroCostoTercero()));
				
				if(medico.getCodigoPersona() != Utilidades.convertirAEntero(forma.getProcedimientoDto().getRespuestaProceEspecificoDto().getCodigoMedicoCentroCostoTercero()))
				{
					logger.info("error al cargar el medico que responde el procedimiento, codigo persona "+forma.getProcedimientoDto().getRespuestaProceEspecificoDto().getCodigoMedicoCentroCostoTercero());
					ActionErrors errores = new ActionErrors();
					errores.add("descripcion",new ActionMessage("errors.notEspecific","No fue Posible Cargar el Profesional que Responde como un usuario del sistema."));				
					saveErrors(request, errores);					
					return mapping.findForward("detalle");					
				}
				medico.setCodigoCentroAtencion(usuarioLogeado.getCodigoCentroAtencion());
				medico.setCodigoCentroCosto(usuarioLogeado.getCodigoCentroCosto());
			}
			catch (Exception e) {
				logger.info("error en cargar usuario responde");
			}
		}
		else		
			medico = usuarioLogeado;
		//*************************************************************************************
		
		//se llena el objeto del mundo
		llenarMundo(forma, mundo);
		
		boolean insertoCorrectamente=false;
		//se inicializa la transaccion
		UtilidadBD.iniciarTransaccion(con);
		
		//PRIMERO
		if(forma.getSolicitudProcedimiento().getMultiple() && forma.getFinalizar().equals(ConstantesBD.acronimoSi))
		{
			forma.getSolicitudProcedimiento().setRespuestaMultiple(forma.getSolicitudProcedimiento().getEsRespuestaMultiple());
			//si la solicitud ya fue finalizada entonces no se debe generrar otra
			if(!Utilidades.esSolicitudProcedimientosFinalizada(con, forma.getNumeroSolicitud()))
			{	
				if(UtilidadTexto.getBoolean(forma.getFinalizarSolicitudMultible()))
				{
					SolicitudProcedimiento.finalizarSolicitudMultiple(con,forma.getNumeroSolicitud());
					
				}
				else
				{
					int nuevaSolicitud=mundo.insertarNuevaSolicitudYGenerarEstructuraLabsoft(con,forma.getSolicitudProcedimiento(),medico, paciente);
					if(nuevaSolicitud<0)
					{
						UtilidadBD.abortarTransaccion(con);
						logger.info("no genero la nueva solicitud ni la estructura para labsoft");
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error", "error.respuestaPrcedimientos.transaccion", true);
					}
				}
			}	
		}		
		
		
		// Anexo 42 - Cambio 1.52
		String observacionCapitacion = null;
		if(forma.isSinAutorizacionEntidadsubcontratada()){
			observacionCapitacion = ConstantesIntegridadDominio.acronimoRegistroSinAutorizacionIngresoEstancia;
		}
		else{
			observacionCapitacion = ConstantesIntegridadDominio.acronimoRegistroConAutorizacionIngresoEstancia;
		}
		//-------------------------------------------------------------
		
		
		//SEGUNDO
		insertoCorrectamente = mundo.insertarRespuestaYGenerarCargo(con,medico,usuarioLogeado.getLoginUsuario(),paciente, observacionCapitacion);
		forma.setCodigoRespuesta(mundo.getCodigoRespuesta());
		
		if(!insertoCorrectamente)
		{
			UtilidadBD.abortarTransaccion(con);	
			logger.info("no genero inserto la respuesta");
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error", "error.respuestaPrcedimientos.transaccion", true);
		}
		else
		{
			logger.info("Codigo Respuesta de Procedimiento Creada >> "+forma.getCodigoRespuesta());
			//***********************************************************************************************************
			ActionErrors errores = new ActionErrors();
					
			//Almacena la Informaci�n Parametrizada en la Plantilla
			ResultadoBoolean resultado = Plantillas.guardarDatosRespuestaProcedimiento(
					con, 
					forma.getPlantillaDto(),
					paciente.getCodigoIngreso()+"",
					forma.getCodigoRespuesta(),
					mundo.getFechaEjecucion(),
					mundo.getHoraEjecucion(),
					medico);
			
			if(!resultado.isTrue())
			{
				insertoCorrectamente = false;
				UtilidadBD.abortarTransaccion(con);				
				errores.add("descripcion",new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
				saveErrors(request,errores);
				logger.info("no se puedo almacenar la informaci�n de la plantilla parametrizable");
				return mapping.findForward("detalle");
			}
			else
			{
				insertoCorrectamente = true;
				logger.info("\n\nGuardo Informacion de Plantilla Parametrizables con Exito!\n\n");
				
				//Evalua si debe mostrar como resumen la respuesta de Procedimiento
				if(forma.getFuncionalidad().equals("hqx") && 
						!forma.getCodigoSolCirugiaSer().equals(""))
				{
					logger.info("\n\nActualiza el campo indicador de Respuesta en Solicitud no Cruenta!\n\n");
					RespuestaProcedimientos.actualizarCodigoCxServicioRespProc(con,forma.getCodigoRespuesta(),forma.getCodigoSolCirugiaSer());					
				}
				
				//Guarda informaci�n del paciente muerto
				if(RespuestaProcedimientos.guardarOtrosComentarios(
						con,
						forma.getProcedimientoDto().getRespuestaProceEspecificoDto().getOtrosComentariosArray(), 
						forma.getCodigoRespuesta(), 
						medico))
				{
					logger.info("\n\nGuardo la Informacion de otros comentarios!");
					
					if(forma.getProcedimientoDto().getRespuestaProceEspecificoDto().getEsMuerto().equals(ConstantesBD.acronimoSi))
					{
						if(RespuestaProcedimientos.guardarMuertePacienteRespProc(
								con, 
								forma.getCodigoRespuesta(),
								forma.getProcedimientoDto().getCodigoCuenta()+"",
								paciente.getCodigoPersona()+"",
								forma.getProcedimientoDto().getRespuestaProceEspecificoDto().getCodigoDiagnosticoMuerte(), 
								forma.getProcedimientoDto().getRespuestaProceEspecificoDto().getCodigoTipoCieDiagnosticoMuerte(),
								forma.getProcedimientoDto().getRespuestaProceEspecificoDto().getFechaMuerte(),
								forma.getProcedimientoDto().getRespuestaProceEspecificoDto().getHoraMuerte(),
								forma.getProcedimientoDto().getRespuestaProceEspecificoDto().getCertificadoDefuncion()))
						{
							logger.info("\n\nGuardo la Informacion de Muerte!");						
						}					
					}
					
															
					int cantidad = RespuestaProcedimientos.getNumeroRespuestasAnteriores(
																						 con,
																						 medico.getCodigoCentroAtencion(),
																						 forma.getProcedimientoDto().getCodigoGrupoServicio(),								
																						 forma.getProcedimientoDto().getCodigoCentroCostoSolicitado());					
					
					if(cantidad > forma.getProcedimientoDto().getRespuestaProceEspecificoDto().getNumeroResSolProcHistorico())
					{
						logger.info("\n\nDurante esta respuesta se realizaron otras respuestas >> "+forma.getProcedimientoDto().getRespuestaProceEspecificoDto().getNumeroResSolProcHistorico()+" nueva cantidad >> "+cantidad);
						forma.getProcedimientoDto().getRespuestaProceEspecificoDto().setNumeroResSolProcHistorico(cantidad);
					}				
					
					//Actualiza el numero de respuestas
					if(RespuestaProcedimientos.actualizarNoRespuestasAnteriores(
							con, 
							forma.getProcedimientoDto().getRespuestaProceEspecificoDto().getNumeroResSolProcHistorico(), 
							forma.getCodigoRespuesta(),
							medico.getCodigoCentroAtencion(),
							forma.getProcedimientoDto().getCodigoGrupoServicio(),
							forma.getProcedimientoDto().getCodigoCentroCostoSolicitado()
							))
						logger.info("\n\nActualizo El numero de Respuesta de Procedimientos!");					
				}			
			}
			//***********************************************************************************************************			
		}
		
		//TERCERO
		insertoCorrectamente= this.insertarDocumentosAdjuntos(con, forma, mundo);
		if(!insertoCorrectamente)
		{
			UtilidadBD.abortarTransaccion(con);	
			logger.info("no genero inserto los archivos adjuntos");
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error", "error.respuestaPrcedimientos.transaccion", true);
		}	
		
		//CUARTO
		if(Utilidades.esSolicitudPYP(con,forma.getNumeroSolicitudInt()))
		{
			logger.info("\n\n\n\n\nACTUALIZANDO SOLICITUDES PYP\n\n\n\n\n\n vienedepyp? "+forma.isVieneDePyp()+", centro de atencion del medico: "+medico.getCodigoCentroAtencion());
			String codigoActividad=Utilidades.obtenerCodigoActividadProgramaPypPacienteDadaSolicitud(con,forma.getNumeroSolicitudInt());
			Utilidades.actualizarEstadoActividadProgramaPypPaciente(con,codigoActividad,ConstantesBD.codigoEstadoProgramaPYPEjecutado,medico.getLoginUsuario(),"");
			if(!forma.isVieneDePyp())
				Utilidades.actualizarAcumuladoPYP(con,forma.getNumeroSolicitud()+"",medico.getCodigoCentroAtencion()+"");
		}		
		
		//QUINTO
		if(forma.getFinalizar().equals(ConstantesBD.acronimoSi))
			RespuestaProcedimientos.finaizarRespuestaSolProcedimiento(con, forma.getNumeroSolicitud(), ConstantesBD.acronimoSi);
		
		//sexto se cambia el estado a respondida
		if(!forma.getFuncionalidad().equals("hqx"))
		{	
			int codigoEstadoHistoriaClinica = Utilidades.convertirAEntero(Utilidades.obtenerEstadoHistoriaClinicaSolicitud(con, forma.getNumeroSolicitud()));
			
			//Si la solicitud ya estaba interpretada ya no se cambia el estado
			if(codigoEstadoHistoriaClinica!=ConstantesBD.codigoEstadoHCInterpretada)
			{
				Solicitud.cambiarEstadosSolicitudStatico(con, forma.getNumeroSolicitudInt(), 0 /*AL ENVIAR EL 0 NO CAMBIA EL ESTADO DEL CARGO*/, ConstantesBD.codigoEstadoHCRespondida);
			}
			Epicrisis1.insertarInfoAutomaticaJusServiciosEpicrisis(con, forma.getNumeroSolicitud(), medico, paciente, ConstantesBD.codigoTipoEvolucionEpicrisisProcedimientos, UtilidadFecha.conversionFormatoFechaABD(mundo.getFechaEjecucion()), mundo.getHoraEjecucion());
		}
		
		//si este campo se encuentra vacio, quiere decir que el flujo viene por ordenes, de lo contrario por consulta externa.
		String codCitaTemp="";
		if(forma.getCodigoCita().equals(""))
		{
			codCitaTemp=RespuestaProcedimientos.validacionCita(con,forma.getNumeroSolicitudInt());
			forma.setCodigoCita(codCitaTemp);
		}
		
		logger.info("VALIDACION CONSULTA EXTERNA!!!!!!!!!!!! es Respuesta multiple: "+forma.getSolicitudProcedimiento().getEsRespuestaMultiple()+" , finlaizar=> "+forma.getFinalizar());
		//SEXTO (validacion de agenda de servicios)
		//Se verifica si la solicitud proviene de una cita
		if(!forma.getCodigoCita().equals("") && 
			!forma.getFuncionalidad().equals("hqx")&&
			(
				(forma.getSolicitudProcedimiento().getEsRespuestaMultiple() && forma.getFinalizar().equals(ConstantesBD.acronimoSi))
				||
				!forma.getSolicitudProcedimiento().getEsRespuestaMultiple()
			))
		{
			logger.info("PAS� VALIDACI�N DE CONSULTA EXTERNA!!!");
			Cita cita = new Cita();
			
			ResultadoBoolean resp = cita.actualizarEstadoCitaTransaccional(con, ConstantesBD.codigoEstadoCitaAtendida, Integer.parseInt(forma.getCodigoCita()), ConstantesBD.continuarTransaccion, medico.getLoginUsuario());
			if(!resp.isTrue())
			{
				UtilidadBD.abortarTransaccion(con);	
				logger.info("no se puedo actualizar el estado de la cita del procedimiento");
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error", "error.respuestaPrcedimientos.actualizacionCita", true);
			}
			
			if(interpretarSolicitud(con,paciente,forma))
			{
				//La solicitud se debe interpetar cuando viene de consulta externa
				HashMap campos = new HashMap();
				campos.put("interpretacion", "");
				campos.put("codigoMedico", medico.getCodigoPersona()+"");
				campos.put("fechaInterpretacion", UtilidadFecha.getFechaActual(con));
				campos.put("horaInterpretacion", UtilidadFecha.getHoraActual(con));
				campos.put("numeroSolicitud", forma.getNumeroSolicitudInt()+"");
				
				int resp0 = Solicitud.interpretarSolicitud(con, campos);
				if(resp0<=0)
				{
					UtilidadBD.abortarTransaccion(con);	
					logger.info("no se puedo interpretar la solicitud de procedimientos");
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error", "error.respuestaPrcedimientos.interpretacionSolicitud", true);
				}				
			}
			
			//Se verifica si se debe mostrar el link de otros servicios de la cita
			if(UtilidadValidacion.esValidoPacienteCargado(con, paciente).puedoSeguir&&
				UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getAcronimo().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto)&&
				UtilidadesConsultaExterna.deboMostrarOtrosServiciosCita(con, forma.getCodigoCita()))
				forma.setDeboMostrarOtrosServiciosCita(true);
			else
				forma.setDeboMostrarOtrosServiciosCita(false);
				
		}
		else
		{
			forma.setDeboMostrarOtrosServiciosCita(false);
		}
		
		//si !codCitaTemp.equals("") quiere decir que el flujo es por ordenes entonces forma.setDeboMostrarOtrosServiciosCita(false);
		if(!codCitaTemp.equals(""))
		{
			forma.setDeboMostrarOtrosServiciosCita(false);
		}
									
		//SEPTIMO Guarda la informacio de los Articulos incluidos si existen
		if(forma.getArrayArticuloIncluidoDto().size() > 0)
		{
			/**
			 * MT 6814 Version 1.5
			 * @autor leoquico
			 * @fecha 10/04/2013
			 */
			HashMap seccionMapa = (HashMap)request.getSession().getAttribute("MAPASECJUSART");
			forma.getJustificacionMap().put("mapasecciones", seccionMapa);
						
			HashMap respuesta = RespuestaProcedimientos.generarCargosDirectosArticulosIncluidos(
					con,
					medico, 
					paciente,
					forma.getNumeroSolicitudInt(),
					forma.getArrayArticuloIncluidoDto(),
					forma.getJustificacionMap(),
					forma.getMedicamentosNoPosMap(),
					forma.getMedicamentosPosMap(),
					forma.getSustitutosNoPosMap(),
					forma.getDiagnosticosDefinitivos()
					);
			
			if(!UtilidadTexto.getBoolean(respuesta.get("exito").toString()))
			{	
				UtilidadBD.abortarTransaccion(con);	
				ActionErrors errores = new ActionErrors();
				
				//Captura los errores 
				if(!respuesta.get("contMensajes").toString().equals("0"))
				{
					for(int i = 0 ; i < Utilidades.convertirAEntero(respuesta.get("contMensajes").toString()); i++)					
						errores.add("descripcion",new ActionMessage("errors.notEspecific",respuesta.get("mensaje_"+i).toString()));					
				}				
				
				errores.add("descripcion",new ActionMessage("errors.notEspecific","No fue Posible Generar Cargos Directos de Articulos"));				
				saveErrors(request, errores);
				logger.info("\n\n error no se pudo generar cargos directos de articulos ");
				return mapping.findForward("detalle");
			}
			else
			{
				logger.info("\n\n #################################### Se genero con exito los cargos directos \n\n");
			}				
		}
		//********************************************************************************************************************			
		//POR ULTIMO HACEMOS LA ACTUALIZACION DE LA INTERFAZ DE SHAIO AX_DFAC Y AX_DPAQ
		//ANEXO 777
		//Modificado por anexo 779
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInterfazContableFacturas(usuarioLogeado.getCodigoInstitucionInt())))
		{
			UtilidadBDInterfaz utilidadInterfaz= new UtilidadBDInterfaz();
			ResultadoBoolean resultadoBoolean1=utilidadInterfaz.actualizarDetalleFactProcedimientosFacturados(usuarioLogeado.getNumeroIdentificacion() /*numeroIdentificacionMedicoResponde*/, usuarioLogeado.getCodigoInstitucionInt(), false/*esDetallePaquete*/, Utilidades.getConsecutivoOrdenesMedicas(Utilidades.convertirAEntero(mundo.getNumeroSolicitud()))+""  /*consecutivoOrdenMedica*/,false,"","","");
			ResultadoBoolean resultadoBoolean2=utilidadInterfaz.actualizarDetalleFactProcedimientosFacturados(usuarioLogeado.getNumeroIdentificacion() /*numeroIdentificacionMedicoResponde*/, usuarioLogeado.getCodigoInstitucionInt(), true/*esDetallePaquete*/, Utilidades.getConsecutivoOrdenesMedicas(Utilidades.convertirAEntero(mundo.getNumeroSolicitud()))+""  /*consecutivoOrdenMedica*/,false,"","","");
			ArrayList tmp = new ArrayList ();
			if (!resultadoBoolean1.isTrue() && UtilidadCadena.noEsVacio(resultadoBoolean1.getDescripcion()))
				tmp.add(resultadoBoolean1.getDescripcion());
			else
				if (!resultadoBoolean2.isTrue() && UtilidadCadena.noEsVacio(resultadoBoolean2.getDescripcion()))
					tmp.add(resultadoBoolean2.getDescripcion());
			
			forma.setMensajes(tmp);
			
		}
		//******************************************************************************************************************
		UtilidadBD.finalizarTransaccion(con);		
		//LA CONEXION SE CIERRA EN EL RESUMEN
		return accionResumen(con, forma, mundo,paciente, medico,  mapping, request);
	}
	
	//*********************************************************************************************************

	/**
	 * 
	 * @param con 
	 * @param paciente
	 * @param forma
	 * @return
	 */
	private boolean interpretarSolicitud(Connection con, PersonaBasica paciente, RespuestaProcedimientosForm forma) 
	{
		//encaso de consulta externa o ambulatorio interpretar.
		if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoConsultaExterna||paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoAmbulatorios)
			return true;
		//en caso de urgencias u hospitalizacion, verificar si el servicio solicitado requiere interpretacion, si requiere interpretacion, no interpretar, de lo contrario si.
		return !RespuestaProcedimientos.servicioRequiereInterpretacion(con,forma.getNumeroSolicitudInt());
	}

	//*********************************************************************************************************

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 */
	private boolean insertarDocumentosAdjuntos(Connection con, RespuestaProcedimientosForm forma, RespuestaProcedimientos mundo)
	{
		logger.info("\n\n DOCUMENTROS ADJUNTOS--> "+forma.getDocumentosAdjuntosGenerados()+" numDocsAdjuntos-->"+forma.getNumDocumentosAdjuntos());
		
		//Documentos adjuntos
		int numAdjuntos = forma.getNumDocumentosAdjuntos();
		
		for( int i=0; i<numAdjuntos; i++ )
		{
			String nombre = (String)forma.getDocumentoAdjuntoGenerado(""+i);
			String checkbox = (String)forma.getDocumentoAdjuntoGenerado("checkbox_"+i);
			
			if( nombre != null && checkbox != null && !checkbox.equals("off") )
			{
				String[] nombres = nombre.split("@");
				
				if( nombres.length == 2 )
				{
					String codigoStr = (String)forma.getDocumentoAdjuntoGenerado("codigo_"+i);
					int codigo = 0;
					
					if( UtilidadCadena.noEsVacio(codigoStr) )
						codigo = Integer.parseInt(codigoStr);
						
					DocumentoAdjunto documento = new DocumentoAdjunto(nombres[1], nombres[0], false, codigo, forma.getCodigoRespuesta());					
					mundo.getDocumentosAdjuntos().addDocumentoAdjunto(documento);
				}
			}		
		}
		return mundo.insertarDocumentosAdjuntos(con, forma.getNumeroSolicitudInt());
	}
	
	//*********************************************************************************************************

	/**
	 * 
	 * @param con
	 * @param forma
	 * @return
	 */
	private ActionForward accionResumen(
			Connection con, 
			RespuestaProcedimientosForm forma, 
			RespuestaProcedimientos mundo,
			PersonaBasica paciente,
			UsuarioBasico usuario,
			ActionMapping mapping, 
			HttpServletRequest request) 
	{		
		mundo.reset(false);
		
		if(forma.getFuncionalidad().equals("hqx"))		
			llenarForma(forma, mundo, true, forma.getCodigoSolCirugiaSer());
		else if(!llenarForma(forma, mundo, true,""))
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error", "error.respuestaPrcedimientos.noCargoSolicitud", true);
						
		logger.info("codigo de Respuesta >> "+forma.getCodigoRespuesta());
		
		accionCargarDtoArticulosIncluidos(con,forma,paciente,usuario);
		//Carga la informacion del Dto de Procedimiento
		accionCargarDtoProcedimiento(con,request,forma,paciente, usuario);
		
		if(!forma.getCodigoRespuesta().equals(""))
				forma.setPlantillaDto(Plantillas.cargarPlantillaXRespuestaProcedimiento(
						con,
						Plantillas.consultarBasicaPlantillasResProc(con,forma.getCodigoRespuesta()).getId()+"", 
						usuario.getCodigoInstitucion(),
						forma.getCodigoRespuesta()						
						));		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("resumen");
	}
	
	//*********************************************************************************************************
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param paciente
	 * @param usuario
	 */
	private void accionListarRango(
			Connection con,
			RespuestaProcedimientosForm forma,
			RespuestaProcedimientos mundo,
			PersonaBasica paciente,
			UsuarioBasico usuario)
	{
		HashMap vo = generarFiltroUsuario(con,usuario);
	
		vo.put("usuario", usuario.getLoginUsuario());
		vo.put("paciente", ConstantesBD.codigoNuncaValido+"");
		vo.put("fechaInicialFiltro", forma.getFechaInicialFiltro()+"");
		vo.put("fechaFinalFiltro", forma.getFechaFinalFiltro()+"");
		vo.put("centroCostroSolicitanteFiltro", forma.getCentroCostosSolicitanteFiltro()+"");
		vo.put("areaFiltro",forma.getAreaFiltro());
		vo.put("pisoFiltro",forma.getPisoFiltro());
		vo.put("habitacionFiltro", forma.getHabitacionFiltro());
		vo.put("camaFiltro", forma.getCamaFiltro());
		vo.put("institucion",usuario.getCodigoInstitucionInt());
		vo.put("permitirInterpretarMultiple", ValoresPorDefecto.getPermIntOrdRespMulSinFin(usuario.getCodigoInstitucionInt()));
		vo.put("tipCCEjecuta",UtilidadesManejoPaciente.obtenerTipoEntidadEjecutaCentroCosto(con,usuario.getCodigoCentroCosto()));
		
		forma.setListaSolicitudes(mundo.generarListadoSolicitudesProcedimientosResponder(con, vo));

	}

	//*********************************************************************************************************
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param paciente
	 * @param usuario
	 */
	private void accionListarRangoEntidadesSub(
			Connection con,
			RespuestaProcedimientosForm forma,
			RespuestaProcedimientos mundo,
			PersonaBasica paciente,
			UsuarioBasico usuario)
	{
		HashMap vo = generarFiltroUsuario(con,usuario);
	
		vo.put("usuario", usuario.getLoginUsuario());
		vo.put("paciente", ConstantesBD.codigoNuncaValido+"");
		vo.put("fechaInicialFiltro", forma.getFechaInicialFiltro()+"");
		vo.put("fechaFinalFiltro", forma.getFechaFinalFiltro()+"");
		vo.put("centroCostroSolicitanteFiltro", forma.getCentroCostosSolicitanteFiltro()+"");
		vo.put("areaFiltro",forma.getAreaFiltro());
		vo.put("pisoFiltro",forma.getPisoFiltro());
		vo.put("habitacionFiltro", forma.getHabitacionFiltro());
		vo.put("camaFiltro", forma.getCamaFiltro());
		vo.put("institucion",usuario.getCodigoInstitucionInt());
		vo.put("permitirInterpretarMultiple", ValoresPorDefecto.getPermIntOrdRespMulSinFin(usuario.getCodigoInstitucionInt()));
		
		vo.put("entidadSubcontratada",ConstantesBD.acronimoSi);
		vo.put("codigoEntidadSub",Utilidades.convertirAEntero(forma.getCodigoEntidadSub()));
		
		forma.setListaSolicitudes(mundo.generarListadoSolicitudesProcedimientosResponder(con, vo));

	}

	//*********************************************************************************************************
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param paciente 
	 */
	private void accionCambiarEstadoSolicitud(
			Connection con, 
			RespuestaProcedimientosForm forma,
			RespuestaProcedimientos mundo, 
			UsuarioBasico usuario, 
			PersonaBasica paciente,
			HttpServletRequest request) throws IPSException
	{
		UtilidadBD.iniciarTransaccion(con);
		ActionErrors errores = new ActionErrors();
		
		
		for(int i=0;i<Integer.parseInt(forma.getListaSolicitudes("numRegistros")+"");i++)
		{
			if((forma.getListaSolicitudes("modificarestado_"+i)+"").equals(ConstantesBD.acronimoSi))
			{
				//solo puede pasar a toma de muestras si antes tenia un setado solicitada
				if(forma.getCodigoEstadoNuevo().equals(ConstantesBD.codigoEstadoHCTomaDeMuestra+"") && (forma.getListaSolicitudes("codigoestadohc_"+i)+"").trim().equals(ConstantesBD.codigoEstadoHCSolicitada+""))
				{
					if(UtilidadLaboratorios.pasarSolicitudATomaMuestras(con,forma.getListaSolicitudes("solicitud_"+i)+"",forma.getFechaCambio(),forma.getHoraCambio(),usuario.getLoginUsuario(),ConstantesBD.codigoEstadoHCTomaDeMuestra))
					{
						forma.setListaSolicitudes("codigoestadohc_"+i,ConstantesBD.codigoEstadoHCTomaDeMuestra+"");
						forma.setListaSolicitudes("estadohc_"+i,Utilidades.obtenerDescripcionEstadoHC(con,ConstantesBD.codigoEstadoHCTomaDeMuestra+""));
					}
					else
					{
						errores.add("", new ActionMessage("errors.problemasGenericos","actualizando el estado de la orden "+forma.getListaSolicitudes("orden_"+i)));
					}
				}
				//solo puede pasar a en proceso, si antes estaba en toma de muestra o solicitada
				else if(forma.getCodigoEstadoNuevo().equals(ConstantesBD.codigoEstadoHCEnProceso+"") 
						&& ((forma.getListaSolicitudes("codigoestadohc_"+i)+"").trim().equals(ConstantesBD.codigoEstadoHCSolicitada+"") || (forma.getListaSolicitudes("codigoestadohc_"+i)+"").trim().equals(ConstantesBD.codigoEstadoHCTomaDeMuestra+"")))
				{
					logger.info("esta a modificar el estado y ponerlo en proceso");
					if(UtilidadLaboratorios.pasarSolicitudAEnProceso(con,forma.getListaSolicitudes("solicitud_"+i)+"",forma.getFechaCambio(),forma.getHoraCambio(),usuario.getLoginUsuario()))
					{
						logger.info("modificacion existosa");
						forma.setListaSolicitudes("codigoestadohc_"+i,ConstantesBD.codigoEstadoHCEnProceso+"");
						forma.setListaSolicitudes("estadohc_"+i,Utilidades.obtenerDescripcionEstadoHC(con,ConstantesBD.codigoEstadoHCEnProceso+""));
						//generacion del cargo de acuerdo al parametro
						if(UtilidadValidacion.esServicioViaIngresoCargoProceso(con,forma.getListaSolicitudes("codigoviaingreso_"+i)+"",forma.getListaSolicitudes("servicio_"+i)+"",usuario.getCodigoInstitucion()))
						{
							//SIEMPRE SER RECALCULA EL CARGO
							//GENERACION DEL CARGO DE SERVICIO CUANDO SE RESPONDE
							Cargos cargos= new Cargos();
							//como solo es 1 servicio entonces no puede tener n responsables con cargo pendiente entonces le enviamos el convenio vacio
							if(cargos.recalcularCargoServicio(con, Integer.parseInt(forma.getListaSolicitudes("solicitud_"+i)+""), usuario, ConstantesBD.codigoNuncaValido/*codigoEvolucionOPCIONAL*/, "" /*observaciones*/, ConstantesBD.codigoNuncaValido /*codigoServicioOPCIONAL*/, ConstantesBD.codigoNuncaValido /*subCuentaResponsable*/, ConstantesBD.codigoNuncaValido /*codigoEsquemaTarifarioOPCIONAL*/, false /*filtrarSoloCantidadesMayoresCero*/, ConstantesBD.acronimoNo /*esComponentePaquete*/, ConstantesBD.acronimoNo/*esPortatil*/,forma.getFechaEjecucion()))
							{
									
								int codigoServicioPortatil=SolicitudProcedimiento.obtenerPortatilSolicitud(con, Integer.parseInt(forma.getListaSolicitudes("solicitud_"+i)+""));
								if(codigoServicioPortatil>0)
								{
									logger.info("RECALCULANDO EL CARGO DEL SERVICIO-----------------------------------------------------------RECALCULANDO EL CARGO DEL SERVICIO");
									if(!cargos.recalcularCargoServicio(con, Integer.parseInt(forma.getListaSolicitudes("solicitud_"+i)+""), usuario, 
 										ConstantesBD.codigoNuncaValido//codigoEvolucionOPCIONAL
 										, "" //observaciones
 										, codigoServicioPortatil //codigoServicioOPCIONAL
 										, ConstantesBD.codigoNuncaValido //subCuentaResponsable
 										, ConstantesBD.codigoNuncaValido //codigoEsquemaTarifarioOPCIONAL
 										, false //filtrarSoloCantidadesMayoresCero
 										, ConstantesBD.acronimoNo //esComponentePaquete
 										, ConstantesBD.acronimoSi//esPortatil
 										,forma.getFechaEjecucion()))
									{
										errores.add("", new ActionMessage("errors.problemasGenericos","calculando el cargo del servicio portatil de la orden "+forma.getListaSolicitudes("orden_"+i)));
									}
										
								}							
							}
							else
							{
								errores.add("", new ActionMessage("errors.problemasGenericos","calculando el cargo de la orden "+forma.getListaSolicitudes("orden_"+i)));
							}
						}
					
						//genera los cargos de los articulos incluidos. si los posee
						if((forma.getListaSolicitudes().containsKey("inclart_"+forma.getListaSolicitudes("solicitud_"+i).toString()) 
								&& forma.getListaSolicitudes("inclart_"+forma.getListaSolicitudes("solicitud_"+i)).toString().trim().equals("")) || 
									!forma.getListaSolicitudes().containsKey("inclart_"+forma.getListaSolicitudes("solicitud_"+i).toString()))
						{	
							//Almacena la informaci�n de los servicio incluidos dentro del listado 
							forma.setListaSolicitudes("inclart_"+forma.getListaSolicitudes("solicitud_"+i),RespuestaProcedimientos.cargarArticulosIncluidosSolicitud(con,forma.getListaSolicitudes("solicitud_"+i).toString()));
							forma.setListaSolicitudes("inclserv_"+forma.getListaSolicitudes("solicitud_"+i),RespuestaProcedimientos.cargarServiciosIncluidosSolicitud(con,forma.getListaSolicitudes("solicitud_"+i).toString(),usuario.getCodigoInstitucionInt()));												
						}
							
						
						if(!forma.getListaSolicitudes("inclart_"+forma.getListaSolicitudes("solicitud_"+i)).toString().trim().equals(""))
						{						
							HashMap respuesta = RespuestaProcedimientos.generarCargosDirectosArticulosIncluidos(
												con,
												usuario,
												paciente,
												Utilidades.convertirAEntero(forma.getListaSolicitudes("solicitud_"+i)+""),
												(HashMap)forma.getListaSolicitudes("inclart_"+forma.getListaSolicitudes("solicitud_"+i)));
							
							//Valida la respuesta 
							if(!UtilidadTexto.getBoolean(respuesta.get("exito").toString()))
							{
									
								
								
								//Captura los errores 
								if(!respuesta.get("contMensajes").toString().equals("0"))
								{
									for(int e = 0; e < Utilidades.convertirAEntero(respuesta.get("contMensajes").toString()); e++)					
										errores.add("descripcion",new ActionMessage("errors.notEspecific",respuesta.get("mensaje_"+e).toString()));					
								}
								
								errores.add("descripcion",new ActionMessage("errors.notEspecific","No fue posible generar los cargos de articulos incluidos para la oden: "+forma.getListaSolicitudes("orden_"+i)));				
								
								
							}
						}
					}
					else
					{
						errores.add("", new ActionMessage("errors.problemasGenericos","actualizando el estado de la orden "+forma.getListaSolicitudes("orden_"+i)));
					}
				}
				//deschequear nuvamente.
				forma.setListaSolicitudes("modificarestado_"+i,ConstantesBD.acronimoNo);
			}
		}
		
		if(errores.isEmpty())
		{
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
			saveErrors(request, errores);
			forma.setEstado("");
		}
		
		
	}
	
	//*********************************************************************************************************

	/**
	 * 
	 * @param forma
	 */
	private void accionOrdenarMapa(RespuestaProcedimientosForm forma)
	{
		String[] indices=indicesMapa;
		int numReg=Integer.parseInt(forma.getListaSolicitudes("numRegistros")+"");
		forma.setListaSolicitudes(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getListaSolicitudes(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setListaSolicitudes("numRegistros",numReg+"");
	}
	
	//*********************************************************************************************************
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario 
	 * @param paciente 
	 */
	private void accionListarPaciente(
			Connection con,
			RespuestaProcedimientosForm forma, 
			RespuestaProcedimientos mundo, 
			PersonaBasica paciente, 
			UsuarioBasico usuario)
	{
		HashMap vo = generarFiltroUsuario(con,usuario);
		vo.put("centroCostoSolicitado", usuario.getCodigoCentroCosto());
		vo.put("usuario", usuario.getLoginUsuario());
		vo.put("paciente", paciente.getCodigoPersona());
		vo.put("fechaInicialFiltro", "");
		vo.put("fechaFinalFiltro", "");
		vo.put("centroCostroSolicitanteFiltro", "");
		vo.put("areaFiltro","");
		vo.put("pisoFiltro","");
		vo.put("habitacionFiltro", "");
		vo.put("camaFiltro", "");
		vo.put("institucion",usuario.getCodigoInstitucionInt());
		vo.put("permitirInterpretarMultiple", ValoresPorDefecto.getPermIntOrdRespMulSinFin(usuario.getCodigoInstitucionInt()));
		vo.put("tipCCEjecuta",UtilidadesManejoPaciente.obtenerTipoEntidadEjecutaCentroCosto(con,usuario.getCodigoCentroCosto()));
		
		forma.setListaSolicitudes(mundo.generarListadoSolicitudesProcedimientosResponder(con, vo));
	}
	
	//*********************************************************************************************************
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario 
	 * @param paciente 
	 */
	private void accionListarPacienteEntidadesSub(
				Connection con, 
				RespuestaProcedimientosForm forma,
				RespuestaProcedimientos mundo,
				PersonaBasica paciente, UsuarioBasico usuario)
	{
		HashMap vo = generarFiltroUsuario(con,usuario);
		vo.put("paciente", paciente.getCodigoPersona());
		vo.put("usuario", usuario.getLoginUsuario()); //Se envia este parametro para que la consulta a la bd muestre los resultados correspondientes. 
		vo.put("fechaInicialFiltro", "");
		vo.put("fechaFinalFiltro", "");
		vo.put("centroCostroSolicitanteFiltro", "");
		vo.put("areaFiltro","");
		vo.put("pisoFiltro","");
		vo.put("habitacionFiltro", "");
		vo.put("camaFiltro", "");
		vo.put("institucion",usuario.getCodigoInstitucionInt());
		vo.put("permitirInterpretarMultiple", ValoresPorDefecto.getPermIntOrdRespMulSinFin(usuario.getCodigoInstitucionInt()));
		
		vo.put("entidadSubcontratada",ConstantesBD.acronimoSi);
		vo.put("codigoEntidadSub",Utilidades.convertirAEntero(forma.getCodigoEntidadSub()));
		forma.setListaSolicitudes(mundo.generarListadoSolicitudesProcedimientosResponder(con, vo));
	}
	
	//*********************************************************************************************************

	/**
	 * 
	 * @param con 
	 * @param usuario
	 * @return
	 */
	private HashMap generarFiltroUsuario(Connection con, UsuarioBasico usuario)
	{
		HashMap vo=new HashMap();
		//cargar medico
		Medico m = new Medico();
		m.init(System.getProperty("TIPOBD"));
		try
		{
			m.cargarMedico(con, usuario.getCodigoPersona());
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		vo.put("tipoSolicitud", ConstantesBD.codigoTipoSolicitudProcedimiento+"");
		vo.put("especialidadSolicitada", m.getEspecialidades());
		vo.put("centroCostoSolicitado",usuario.getCodigoCentroCosto()+"");
		vo.put("centroCostroTratante",usuario.getCodigoCentroCosto()+"");
		vo.put("ocupacionSolicitada",usuario.getCodigoOcupacionMedica()+"");
		vo.put("centroCostoIntentaAcceso", usuario.getCodigoCentroCosto()+"");
		
		return vo;
	}

	//*********************************************************************************************************
	
	/**
	 * 
	 * @param paciente
	 * @param request
	 * @param mapping
	 * @param usuario 
	 * @return
	 */
	private ActionForward validacionAccesoPaciente(PersonaBasica paciente, HttpServletRequest request, ActionMapping mapping, UsuarioBasico usuario)
	{
		if(paciente == null|| paciente.getCodigoTipoIdentificacionPersona().equals(""))
		{
			logger.warn("Paciente no v�lido (null)");			
			request.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
			return mapping.findForward("paginaError");
		}
		//Validar que el usuario no se autoatienda
		ResultadoBoolean respuesta = UtilidadesHistoriaClinica.esUsuarioAutoatendido(usuario, paciente);
		if(respuesta.isTrue())
		{
			request.setAttribute("codigoDescripcionError", respuesta.getDescripcion());
			return mapping.findForward("paginaError");
		}
		return null;
	}
	
	//*********************************************************************************************************
	
	/**
	 * 
	 * @param con
	 * @param request
	 * @param usuario
	 * @param forma
	 */
	private void cargarPaciente(Connection con, HttpServletRequest request, UsuarioBasico usuario, RespuestaProcedimientosForm forma)
	{
		if(!forma.getCodigoPaciente().equals(""))
		{	
			PersonaBasica persona= (PersonaBasica) request.getSession().getAttribute("pacienteActivo");
	        persona.setCodigoPersona(Integer.parseInt(forma.getCodigoPaciente()));
	        try
			{
	        	ObservableBD observable = (ObservableBD)servlet.getServletContext().getAttribute("observable");
	    		persona.cargar(con, Integer.parseInt(forma.getCodigoPaciente()));
				persona.cargarPaciente(con, Integer.parseInt(forma.getCodigoPaciente()), usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");
				observable.addObserver(persona);
				UtilidadSesion.notificarCambiosObserver(persona.getCodigoPersona(),servlet.getServletContext());
			} 
	        catch (NumberFormatException e)
			{
				e.printStackTrace();
			} 
	        catch (SQLException e)
			{
				e.printStackTrace();
			}
	   }   
   }
	
	
	
	
	//**************************************************************************************************************************	
	//**************************************************************************************************************************
	//METODOS PARA FORMULARIOS PARAMETRIZABLES**********************************************************************************
	//**************************************************************************************************************************
	
	/**
	 * Carga el dto plantilla
	 * @param Connection con
	 * @param RespuestaProcedimientosForm forma
	 * @param String servicio  
	 * @param PersonaBasica paciente
	 * */
	private void accionCargarDtoPlantilla(Connection con,RespuestaProcedimientosForm forma,String servicio,PersonaBasica paciente,UsuarioBasico usuario)	
	{		
		logger.info("VALOR CONSECUTIVO PLANTILLA >> "+forma.getCodigoPlantillaPk());		
		boolean filtrarDatosPaciente = true;
		int numeroDias = 0;
			
		if(UtilidadFecha.validarFecha(paciente.getFechaNacimiento()))		
			numeroDias = UtilidadFecha.numeroDiasEntreFechas(paciente.getFechaNacimiento(),UtilidadFecha.getFechaActual());		
		else
		{
			logger.info("valor de las fechas >> "+paciente.getFechaNacimiento()+" >> "+UtilidadFecha.validarFecha(paciente.getFechaNacimiento()));
			filtrarDatosPaciente = false;
		}
		
		//Carga la plantilla parametrica 
		forma.setPlantillaDto(Plantillas.cargarPlantillaParametrica(
				con, 
				forma.getCodigoPlantillaPk(), 
				usuario.getCodigoInstitucionInt(), 
				ConstantesCamposParametrizables.funcParametrizableRespuestaProcedimientos, 
				ConstantesBD.codigoNuncaValido, 
				ConstantesBD.codigoNuncaValido, 
				ConstantesBD.codigoNuncaValido,
				paciente.getCodigoSexo(),
				numeroDias,
				filtrarDatosPaciente,
				"",
				"",
				""));
		
		//Carga los textos del campo resultado
		forma.setTextoResultadoMap(Plantillas.consultarTextosRespuestaProc(con,usuario.getCodigoInstitucionInt(),servicio));		
		forma.getPlantillaDto().cargarEdadesPaciente(paciente);
		
		//Valida si es requerido el campo resultados
		if(forma.getCodigoPlantillaPk() > 0)
			forma.setUtilitarioMap("resultadosRequerido",Plantillas.consultarResultadosProcRequeridos(con,forma.getCodigoPlantillaPk()+""));
		else
			forma.setUtilitarioMap("resultadosRequerido",ConstantesBD.acronimoSi);		
	}	
	//**************************************************************************************************************************
	
	/**
	 * Carga el Dto de Procedimiento
	 * @param Connection con
	 * @param RespuestaProcedimientosForm forma
	 * @param PersonaBasica paciente
	 * */
	private void accionCargarDtoProcedimiento(Connection con,HttpServletRequest request, RespuestaProcedimientosForm forma,PersonaBasica paciente,UsuarioBasico usuario)
	{
		logger.info("VALOR NUMERO SOLICITUD DTO PROCEDIMIENTO >> "+forma.getNumeroSolicitud()+" NUMERO DE RESPUESTA >> "+forma.getCodigoRespuesta());
				
		forma.setProcedimientoDto(RespuestaProcedimientos.cargarDtoProcedimiento(
				con,
				paciente.getCodigoPersona()+"",
				forma.getNumeroSolicitud(),
				forma.getCodigoRespuesta(),
				usuario.getCodigoInstitucionInt(),
				usuario.getCodigoCentroAtencion()));
		
		logger.info("\n\n\nESPECIALIDAD PROFESIONAL QUE RESPONDE  >> "+forma.getProcedimientoDto().getRespuestaProceEspecificoDto().getNombreEspecialidadProfResponde());
		
		
		//Inserta una nueva respuesta de Procedimiento en el caso en el que no exista ninguna
		if(forma.getProcedimientoDto().getRespuestaProceArray().size() <= 0)		
			forma.getProcedimientoDto().getRespuestaProceEspecificoDto().setNumeroResSolProcHistorico(forma.getProcedimientoDto().getNumeroResSolProcAnteriores());
		
		//Carga la estructura de medicos para otros comentarios
		forma.getProcedimientoDto().getRespuestaProceEspecificoDto().setMedicosOtrosComentarios(UtilidadesManejoPaciente.obtenerProfesionales(
				con, 
				usuario.getCodigoInstitucionInt(),
				true,
				true,
				usuario.getCodigoCentroCosto()+"", 
				usuario.getCodigoPersona()+","));
		
		//evalua si el centro de costos del usuario esta parametrizado como centro de costos de respuesta de procedimiento por terceros
		forma.getProcedimientoDto().setIndicativoCentroCostoRegRespuestaProc(Utilidades.esCentroCostoRespuestaProcTercero(con,usuario.getCodigoCentroCosto()));
		
		//Si el indicador esta en Si se carga los medicos por centro de costos
		if(forma.getProcedimientoDto().getIndicativoCentroCostoRegRespuestaProc().equals(ConstantesBD.acronimoSi))
		{
			forma.getProcedimientoDto().getRespuestaProceEspecificoDto().setMedicosCentroCosto(UtilidadesManejoPaciente.obtenerProfesionales(
					con, 
					usuario.getCodigoInstitucionInt(),
					true,
					false,
					usuario.getCodigoCentroCosto()+"", 
					""));
			
		}else
		{
			InfoDatosInt[] esp=usuario.getEspecialidades1();
			if(esp.length==1)
			{
				forma.getProcedimientoDto().getRespuestaProceEspecificoDto().setCodEspecialidadProfResponde(esp[0].getCodigo()+"");
				forma.getProcedimientoDto().getRespuestaProceEspecificoDto().setNombreEspecialidadProfResponde(esp[0].getNombre());
			}
			//Carga las especialidades del Profesional Responsable
			forma.getProcedimientoDto().getRespuestaProceEspecificoDto().setEspecialidadesProfResponde(usuario.getEspecialidades1());
			forma.getProcedimientoDto().getRespuestaProceEspecificoDto().setNombreyRMProfesional(usuario.getNombreyRMPersonalSalud());
			forma.getProcedimientoDto().getRespuestaProceEspecificoDto().setCodigoMedicoCentroCostoTercero(usuario.getCodigoPersona()+"");
		}
		
		
		//Valida si se debe mostrar o no la secci�n de Muerto		
		if(forma.getProcedimientoDto().getCodigoViaIngreso() == ConstantesBD.codigoViaIngresoAmbulatorios || 
				forma.getProcedimientoDto().getCodigoViaIngreso() == ConstantesBD.codigoViaIngresoConsultaExterna)
		{
			forma.getProcedimientoDto().getRespuestaProceEspecificoDto().setMostrarSeccionMuerto(ConstantesBD.acronimoSi);
		    
		}
		else
			forma.getProcedimientoDto().getRespuestaProceEspecificoDto().setMostrarSeccionMuerto(ConstantesBD.acronimoNo);
		
		//evalua si el centro de costos del usuario esta parametrizado como centro de costos de respuesta de procedimiento por terceros
		forma.getProcedimientoDto().setIndicativoCentroCostoRegRespuestaProc(Utilidades.esCentroCostoRespuestaProcTercero(con,usuario.getCodigoCentroCosto()));
	}
	
	//**************************************************************************************************************************
	
	/**
	 * Carga un Array de Dto con todos los articulos incluidos
	 * @param Connection con
	 * @param RespuestaProcedimientosForm forma
	 * @param PersonaBasica paciente
	 * @param UsuarioBasico usuario
	 * */
	private void accionCargarDtoArticulosIncluidos(Connection con,RespuestaProcedimientosForm forma,PersonaBasica paciente,UsuarioBasico usuario)
	{
		//evalua si el estado de la cuenta del paciente se encuentra facturada o cerrada, la primera cuenta es la mas reciente de todas			
		if(paciente.getCuentasPacienteArray().size() > 0 
				&& !paciente.getCuentasPacienteArray(0).getEstadoCuenta().equals(ConstantesBD.codigoEstadoCuentaFacturada+"") 
					&& !paciente.getCuentasPacienteArray(0).getEstadoCuenta().equals(ConstantesBD.codigoEstadoCuentaCerrada+""))
		{		
			//Carga el Dto de Solicitud de Procedimientos
			forma.setArrayArticuloIncluidoDto(RespuestaProcedimientos.cargarArticulosIncluidosSolicitudDto(con,forma.getNumeroSolicitud()+""));
			forma.setIncluidosArticulosMap(new HashMap());
			forma.setIncluidosArticulosMap("abrir_seccion_art_incluidos",ConstantesBD.acronimoSi);
			forma.setJustificacionMap(new HashMap());
			forma.setHiddens("");
		}
		else
		{
			logger.info("\n no se carga la informacion de los articulos incluidos, la cuenta se encuentra en estado cerrada o facturada ");
			forma.setArrayArticuloIncluidoDto(new ArrayList<DtoArticuloIncluidoSolProc>());
			forma.setIncluidosArticulosMap(new HashMap());
			forma.setIncluidosArticulosMap("abrir_seccion_art_incluidos",ConstantesBD.acronimoNo);
			forma.setJustificacionMap(new HashMap());
			forma.setHiddens("");
		}
	}
	
	//**************************************************************************************************************************
	
	/**
	 * Carga los incluidos de una solicitud (Servicios/Articulos)
	 * @param Connection con
	 * @param RespuestaProcedimientosForm forma
	 * */
	private void accionCargarIncluidosSolicitud(Connection con,RespuestaProcedimientosForm forma, int institucion)
	{				
		//valida que la solicitud no venga vacia
		if(!forma.getNumeroSolicitud().equals(""))
		{
			//Se busca dentro de la estructura permanente si ya existe la solicitud y sus campos
			if(forma.getListaSolicitudes().containsKey("inclart_"+forma.getNumeroSolicitud()) &&
					!forma.getListaSolicitudes("inclart_"+forma.getNumeroSolicitud()).equals("") && 
						forma.getIndicativoMarcado().equals(ConstantesBD.acronimoSi))
			{				
				//Captura la informaci�n de los articulos
				forma.setIncluidosArticulosMap((HashMap)forma.getListaSolicitudes("inclart_"+forma.getNumeroSolicitud()));
				forma.setIncluidosServiciosMap((HashMap)forma.getListaSolicitudes("inclserv_"+forma.getNumeroSolicitud()));
			}
			else
			{ 
				forma.setIncluidosArticulosMap(RespuestaProcedimientos.cargarArticulosIncluidosSolicitud(con,forma.getNumeroSolicitud()));
				forma.setIncluidosServiciosMap(RespuestaProcedimientos.cargarServiciosIncluidosSolicitud(con,forma.getNumeroSolicitud(), institucion));
				
				//guarda la informaci�n del encabezado
				if(!forma.getIncluidosArticulosMap("numRegistros").toString().equals("0"))
				{
					forma.setIncluidosServiciosMap("ordenPrincipal",forma.getIncluidosArticulosMap("ordenPpal_0").toString());				
					forma.setIncluidosServiciosMap("descripcionOrdenPrincipal",forma.getIncluidosArticulosMap("servicioPpal_0").toString()+" "+forma.getIncluidosArticulosMap("descripcionServicio_0").toString());
					forma.setIncluidosServiciosMap("especialidadOrdenPrincipal",forma.getIncluidosArticulosMap("especialidadServicioPpal_0").toString());				
				}
				else if(!forma.getIncluidosServiciosMap("numRegistros").toString().equals("0"))
				{
					forma.setIncluidosServiciosMap("ordenPrincipal",forma.getIncluidosServiciosMap("ordenPpal_0").toString());				
					forma.setIncluidosServiciosMap("descripcionOrdenPrincipal",forma.getIncluidosServiciosMap("servicioPpal_0").toString()+" "+forma.getIncluidosServiciosMap("descripcionServicioPpal_0").toString());
					forma.setIncluidosServiciosMap("especialidadOrdenPrincipal",forma.getIncluidosServiciosMap("especialidadServicioPpal_0").toString());
				}
				
				//Almacena la informaci�n de los servicio incluidos dentro del listado 
				forma.setListaSolicitudes("inclart_"+forma.getNumeroSolicitud(),forma.getIncluidosArticulosMap());
				forma.setListaSolicitudes("inclserv_"+forma.getNumeroSolicitud(),forma.getIncluidosServiciosMap());
			}
		}
		else
		{
			HashMap mapa = new HashMap();
			mapa.put("numRegistros","0");
			forma.setIncluidosArticulosMap(mapa);
			forma.setIncluidosServiciosMap(mapa);
		}
	}
	//**************************************************************************************************************************
	
	/**
	 * Confirma la cantidad de los articulos incluidos
	 * @param Connection con
	 * @param RespuestaProcedimientosForm forma
	 * */
	public void accionConfirmarCantInclu(RespuestaProcedimientosForm forma)
	{
		//Almacena la informaci�n de los servicio incluidos dentro del listado 
		forma.setListaSolicitudes("inclart_"+forma.getNumeroSolicitud(),forma.getIncluidosArticulosMap());
		forma.setListaSolicitudes("inclserv_"+forma.getNumeroSolicitud(),forma.getIncluidosServiciosMap());
	}
	
	//**************************************************************************************************************************
	
	/**
	 * Adiciona otro nuevo comentario
	 * @param RespuestaProcedimientosForm forma
	 * */
	public void accionAddOtroComentario(RespuestaProcedimientosForm forma)
	{		
		if(Utilidades.convertirAEntero(forma.getProcedimientoDto().getRespuestaProceEspecificoDto().getIndexOtrosComentarios())>=0)
		{	
			InfoDatosString nuevo = new InfoDatosString();
			HashMap medico = forma.getProcedimientoDto().getRespuestaProceEspecificoDto().getMedicosOtrosComentarios().get(Utilidades.convertirAEntero(forma.getProcedimientoDto().getRespuestaProceEspecificoDto().getIndexOtrosComentarios()));			
			nuevo.setCodigo(medico.get("codigoMedico").toString());			
			nuevo.setDescripcion("");
			nuevo.setNombre(medico.get("primerApellido").toString().toUpperCase()+" "+medico.get("segundoApellido").toString().toUpperCase()+" "+medico.get("primerNombre").toString().toUpperCase()+" "+medico.get("segundoNombre").toString().toUpperCase());
			nuevo.setIndicativo(ConstantesBD.acronimoNo);
			nuevo.setActivo(true);		
			
			forma.getProcedimientoDto().getRespuestaProceEspecificoDto().getOtrosComentariosArray().add(nuevo);
		}		
	}
	//**************************************************************************************************************************	
	
	/**
	 * Elimina otro Comentarios
	 * @param RespuestaProcedimientosForm forma
	 * */
	public void accionEliminarOtroComentario(RespuestaProcedimientosForm forma)
	{
		if(Utilidades.convertirAEntero(forma.getProcedimientoDto().getRespuestaProceEspecificoDto().getIndexOtrosComentarios())>=0)		
			forma.getProcedimientoDto().getRespuestaProceEspecificoDto().getOtrosComentariosArray().remove(Utilidades.convertirAEntero(forma.getProcedimientoDto().getRespuestaProceEspecificoDto().getIndexOtrosComentarios()));		
	}
	
	//****************************************************************************************************************************
	
	private ActionForward accionEstadosAutorizacion(RespuestaProcedimientosForm procedimientos, Connection con,ActionMapping mapping) {
		
		logger.info("El numero de solicitud es: "+ procedimientos.getNumeroSolicitudInt());
		procedimientos.setEstadoAuto(UtilidadesOrdenesMedicas.obtenerConvenioEstadoSolicitud(procedimientos.getNumeroSolicitudInt()));
		
		return mapping.findForward("estadosAutorizacion");
	}
	
	//*******************************************************************************************************************************
	
	/**
	 * accion menu respuestas entidades subcontratadas
	 * @param Connection con
	 * @param RespuestaProcedimientosForm forma
	 * @param UsuarioBasico usuario
	 * @param HttpServletRequest request
	 * */
	private void accionMenuRespuestaEntidadesSub(Connection con,RespuestaProcedimientosForm forma,UsuarioBasico usuario,HttpServletRequest request)
	{
		ActionErrors errores = RespuestaProcedimientos.validacionesIngresoRespProcEntiSub(con, usuario);
		
		if(errores.isEmpty())
			forma.setEntidadesSubArray(CargosEntidadesSubcontratadas.obtenerEntidadesSubcontratadasCentroCosto(con,usuario.getCodigoCentroCosto(),usuario.getCodigoInstitucionInt()));
		
		saveErrors(request, errores);
	}
	
	
	
	 private ActionForward accionFiltrarEspecialidadesProfesional(Connection con,RespuestaProcedimientosForm forma,HttpServletResponse response) 
		{
			logger.info(" >>>  ENTRO A FILTRAR ESPECIALIDADES    "+"Codigo Profesional: "+forma.getCodProfesionalFiltro());
			HashMap especialidades=new HashMap();
			String resultado = "<respuesta>" +
				"<infoid>" +
					"<sufijo>ajaxBusquedaTipoSelect</sufijo>" +
					"<id-select>especiliadadResponsable</id-select>" +
					"<id-arreglo>especialidades</id-arreglo>" +
				"</infoid>" ;
			
			if(!forma.getCodProfesionalFiltro().equals(""))
			{
			  especialidades = Utilidades.obtenerEspecialidadesMedico(con, forma.getCodProfesionalFiltro()+""); 	
		    }
			
			for(int i=0; i< Utilidades.convertirAEntero(especialidades.get("numRegistros")+"");i++)
			{
					resultado += "<especialidades>";
					resultado += "<codigo>"+especialidades.get("codigo_"+i)+"</codigo>";
					resultado += "<descripcion>"+especialidades.get("descripcion_"+i)+"</descripcion>";
				    resultado += "</especialidades>";	
				
			}
			
			resultado += "</respuesta>";
			
			UtilidadBD.closeConnection(con);
			//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
			try
			{
				response.reset();
				response.resetBuffer();
				
				response.setContentType("text/xml");
				response.setHeader("Cache-Control", "no-cache");
				response.getWriter().flush();			
		        response.getWriter().write(resultado);
		        response.getWriter().close();
		     
			}
			catch(IOException e)
			{
				logger.error("Error al enviar respuesta AJAX en accionFiltrarEsepecialidades: "+e);
			}
			return null;
		}
	
	 
	 
	
	 	/**
		 * Realiza las validaciones de capitaci�n referentes al cambio n�mero 1.52 del Anexo 42
		 * @param forma
		 * @param usuario
		 * @return ArrayList<String>
		 * @author Cristhian Murillo
		 */
		private ArrayList<String> validacionCapitacion(RespuestaProcedimientosForm forma, UsuarioBasico usuario)
		{
			ArrayList<String> listaAdvertencias = new ArrayList<String>();
			
			UtilidadTransaccion.getTransaccion().begin();
			String numeroSolicitud = forma.getNumeroSolicitud()+"";
			
			boolean tieneConvenioCapitado 			= false;
			boolean servicioAutorizado 				= false;
						
			MessageResources fuenteMensaje = MessageResources.getMessageResources(
	    		"com.servinte.mensajes.manejoPaciente.AutorizacionesIngresoEstanciaForm");
			
			DtoSolicitud dtoSolicitud = new DtoSolicitud();
			ArrayList<DtoSolicitud> listasolicitudes = new ArrayList<DtoSolicitud>();
			dtoSolicitud.setNumeroSolicitud(Integer.parseInt(numeroSolicitud));
			dtoSolicitud.setInstitucion(usuario.getCodigoInstitucionInt());
			
			/* Si se le env�a el n�mero de la solicitud, se supone que solo debe retornar una. Por eso se toma  listasolicitudes.get(0) */
			listasolicitudes.addAll(aurorizacionesEntSubCapitacionServicio.obtenerSolicitudesSubcuenta(dtoSolicitud));
			
			
			if(!Utilidades.isEmpty(listasolicitudes))
			{
				dtoSolicitud = new DtoSolicitud(); dtoSolicitud = listasolicitudes.get(0);
				
				/* Se obtiene el convenio de la solicitud para validar si es capitado */
				Convenios convenios = new Convenios();
				convenios = convenioServicio.findById(dtoSolicitud.getCodigoConvenio());
				
				
				/* Se valida que el convenio sea capitado al igual que sus contratod y que a su ves los contratos esten vigentes */
				if(convenios.getCapitacionSubcontratada() != null){
					if(convenios.getCapitacionSubcontratada().equals(ConstantesBD.acronimoSiChar))
					{
						tieneConvenioCapitado =  true;
					}
					else {
						/* El convenio no maneja capitaci�n */
						tieneConvenioCapitado =  false;
					}
				}
				/* -------------------------------------------------------------------------------------------------------------------- */
				
				
				if(tieneConvenioCapitado)
				{
					/* Se toma la solicitud y se cargan todas las autorizaciones de entidad subcontratada asociadas */
					DtoAutorizacionEntSubcontratadasCapitacion dtoAutorizacionEntSubcontratadasCapitacion = new DtoAutorizacionEntSubcontratadasCapitacion();
					dtoAutorizacionEntSubcontratadasCapitacion.setNumeroOrden(dtoSolicitud.getNumeroSolicitud());
					ArrayList<DtoAutorizacionEntSubcontratadasCapitacion> listaAutorizacionesEntSubPorSolicitud = 
						aurorizacionesEntSubCapitacionServicio.obtenerAutorizacionesPorEntSubPorNumeroSolicitud(dtoAutorizacionEntSubcontratadasCapitacion);
				
					ArrayList<AutorizacionesEntSubServi> listaTodosServiciosAutorizados = new ArrayList<AutorizacionesEntSubServi>();
					
					
					/* Busco las autorizaciones que est�n Autorizadas y cargo todos los servicios de estas */
					for (DtoAutorizacionEntSubcontratadasCapitacion autorizacionPorSolicitud : listaAutorizacionesEntSubPorSolicitud) 
					{
						if(autorizacionPorSolicitud.getEstado().equals(ConstantesIntegridadDominio.acronimoAutorizado))
						{
							// Se carga la lista de Lista Servicios por Autorizaci�n de entidad Subcontratada
					    	DtoAutorizacionEntSubcontratadasCapitacion dtoAutorizacionEntSubcontratadasCapitacionServi = new  DtoAutorizacionEntSubcontratadasCapitacion();
					    	dtoAutorizacionEntSubcontratadasCapitacionServi.setAutorizacion(autorizacionPorSolicitud.getAutorizacion());
					    	listaTodosServiciosAutorizados.addAll(aurorizacionesEntSubCapitacionServicio.listarAutorizacionesEntSubServiPorAutoEntSub(dtoAutorizacionEntSubcontratadasCapitacionServi));
						}
					}
					//----------------------------------------------------------------------------------------------------------------------------
					
					
					/*  Comparo los servicios a responder contra los autorizados a ver si concuerdan */
					for (DtoServiciosAutorizaciones dtoServiciosAutorizaciones : dtoSolicitud.getListaServicios()) 
					{
						// Cortar el ciclo para mejorar rendimiento
						if(servicioAutorizado){ break; }
						
						for (AutorizacionesEntSubServi autorizacionesEntSubServi : listaTodosServiciosAutorizados) {
							Log4JManager.info(dtoServiciosAutorizaciones.getCodigoServicio()+ "=" + autorizacionesEntSubServi.getServicios().getCodigo());
							if(dtoServiciosAutorizaciones.getCodigoServicio() == autorizacionesEntSubServi.getServicios().getCodigo()){
								servicioAutorizado 	= true;
								break;
							}
						}
					}
					
					
					if(servicioAutorizado){
						/* Si SI tiene asociada una Autorizaci�n de Capitaci�n Subcontratada, 
						 * se debe continuar con el flujo actual de la funcionalidad. */
						listaAdvertencias = new ArrayList<String>();
					}
					else{
						/* Si NO tiene asociada una Autorizaci�n de Capitaci�n Subcontratada, 
						 * se debe mostrar el siguiente mensaje informativo, permitiendo informarle al  
						 * usuario que la orden que responde no tiene asociada una autorizaci�n de capitaci�n subcontratada: */
						String mensajeConcreto = fuenteMensaje.getMessage("AutorizacionesIngresoEstanciaForm.ordenSinAutorizacionIE");
						listaAdvertencias.add(mensajeConcreto);
					}
				}
				else
				{
					/* Si NO maneja Capitaci�n Subcontratada, se debe continuar con el 
					 * flujo actual de la funcionalidad. */
					listaAdvertencias = new ArrayList<String>();
				}
			}
			
			UtilidadTransaccion.getTransaccion().commit();
			
			return listaAdvertencias;
		}

	
}