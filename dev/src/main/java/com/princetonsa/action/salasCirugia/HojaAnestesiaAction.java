/*
 * Creado en Febrero 2008
 */
package com.princetonsa.action.salasCirugia;


import java.sql.Connection;
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
import util.InfoDatos;
import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.manejoPaciente.UtilidadesManejoPaciente;
import util.parametrizacion.ConstantesSeccionesParametrizables;
import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.salasCirugia.HojaAnestesiaForm;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.UtilidadJustificacionPendienteArtServ;
import com.princetonsa.mundo.inventarios.FormatoJustArtNopos;
import com.princetonsa.mundo.ordenesmedicas.cirugias.HojaQuirurgica;
import com.princetonsa.mundo.ordenesmedicas.procedimientos.RespuestaProcedimientos;
import com.princetonsa.mundo.parametrizacion.AccesosVascularesHA;
import com.princetonsa.mundo.salasCirugia.HojaAnestesia;
import com.princetonsa.mundo.salasCirugia.LiquidacionServicios;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * @author Jose Eduardo Arias Doncel 
 * 
 * Nota: Se solicita para proximas modificaciones, guardar la estructura del c+odigo 
 *  y comentar debidamente los cambios y nuevas lineas.
 */
@SuppressWarnings("unchecked")
public class HojaAnestesiaAction extends Action
{
	
	
	Logger logger = Logger.getLogger(HojaAnestesiaAction.class);
	
	
	/** * Contiene la lista de mensajes correspondiente a esta forma */
	MessageResources fuenteMensaje = MessageResources.getMessageResources("com.servinte.mensajes.manejoPaciente.AutorizacionesIngresoEstanciaForm");
	
	
	
	/**
	 * Metodo execute del action
	 * */	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
								 HttpServletResponse response) throws Exception
								 {		
		Connection con = null;
		try{
			if(response == null);

			if (form instanceof HojaAnestesiaForm) 
			{
				System.gc();
				con = UtilidadBD.abrirConexion();

				if(con == null)
				{
					request.setAttribute("CodigoDescripcionError","erros.problemasBd");
					return mapping.findForward("paginaError");
				}

				//Usuario cargado en session
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");			 
				//paciente cargado en sesion 
				PersonaBasica paciente= (PersonaBasica)request.getSession().getAttribute("pacienteActivo");			 
				//Hoja de Anestesia
				HojaAnestesia mundo = new HojaAnestesia();			 


				HojaAnestesiaForm forma = (HojaAnestesiaForm)form;		
				String estado = forma.getEstado();
				String codigoSeccion = forma.getSeccion();
				//Se toma el valor de validacion capitacion desde responder cirugia para saber si es de capitacion y si tiene orden
				boolean validacionCapitacion=forma.isValidacionCapitacion();

				logger.info("-------------------------------------");
				logger.info("Valor del Estado    >> "+forma.getEstado());
				logger.info("Valor de la Seccion >> "+forma.getSeccion());
				logger.info("-------------------------------------");

				//Carga la informacion del paciente pasados por parametros
				forma.setCodigoPaciente(request.getParameter("codigoPaciente")!=null?request.getParameter("codigoPaciente"):"");
				if(paciente==null || paciente.getTipoIdentificacionPersona().equals(""))
				{
					//Evalua si se carga el paciente a partir del codigo del Paciente				
					if(!forma.getCodigoPaciente().equals(""))
					{
						paciente.setCodigoPersona(Utilidades.convertirAEntero(forma.getCodigoPaciente()));					
						UtilidadesManejoPaciente.cargarPaciente(con, usuario, paciente, request);					 
					}

					if(paciente==null || paciente.getTipoIdentificacionPersona().equals(""))
						return ComunAction.accionSalirCasoError(mapping, request, null, logger, "paciente null o sin  id", "errors.paciente.noCargado", true);
				}//si el paciente que viene por parametro es diferente al que esta cargado se carga el que viene por parametro 
				else if(!forma.getCodigoPaciente().equals("") 
						&& !forma.getCodigoPaciente().equals(paciente.getCodigoPersona()))
				{
					logger.info("Se carga el Paciente pasado por parametro. actual >> "+paciente.getCodigoPersona()+" >> nuevo "+forma.getCodigoPaciente());
					paciente.setCodigoPersona(Utilidades.convertirAEntero(forma.getCodigoPaciente()));				 
					UtilidadesManejoPaciente.cargarPaciente(con, usuario, paciente, request);				 
				}
				//Validar que el usuario no se autoatienda
				ResultadoBoolean respuesta = UtilidadesHistoriaClinica.esUsuarioAutoatendido(usuario, paciente);
				if(respuesta.isTrue())
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "El paciente no puede ser autoatendido", respuesta.getDescripcion(), true);


				if (!UtilidadValidacion.esOcupacionQueRealizaCx(con, usuario.getCodigoOcupacionMedica(), usuario.getCodigoInstitucionInt()))
				{
					ArrayList<String> mensajes = new ArrayList<String>();
					mensajes.add("Profecional debe tener ocupaci�n realiza cirug�as y especialidad anestesiolog�a, opcion solo de consulta.");
					forma.setMensajes(mensajes);

				}

				if(estado == null)
				{
					forma.reset(ConstantesBD.codigoNuncaValido);
					logger.warn("Estado no Valido dentro del Flujo de Hoja de Anestecia (null)");				 
					request.setAttribute("CodigoDescripcionError","errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}

				//Debido a las caracteristicas de distribucion de la Hoja de Anestesia cualquier error no esperado
				//se toma y se cierran las conexiones y transacciines abiertas 
				try
				{				 
					//-----------------------------------------------------------------------------------			 
					//***********************************************************************************
					//*****************Estados del Action para la Hoja de Anestesia DUMMY****************
					//-----------------------------------------------------------------------------------

					//Estado inicial del action para llamados Dummy
					if(estado.equals("empezarDummy"))
					{
						//Se valida si el parametro enviado desde ResponderCirugiasAction es true o false para asignar descripcion a guardar en bd
						if (request.getParameter("validacionCapitacion")!=null&&request.getParameter("validacionCapitacion").equals("true"))
						{
							forma.setValidacionCapitacion(true);
						}
						return metodoEstadoEmpezar(con,forma, usuario, mundo, paciente,request,mapping,true);		 
					}		 

					//-----------------------------------------------------------------------------------
					//***********************************************************************************
					//********************************Estados del Action para el Listado de Peticiones***
					//-----------------------------------------------------------------------------------

					//Estado inicial del action, inicio de la funcionalidad
					else if(estado.equals("empezar"))
					{			
						return metodoEstadoEmpezar(con,forma, usuario, mundo, paciente,request,mapping,false);			 
					}
					//Estado ordena el listado de peticiones 
					else if(estado.equals("ordenarListado"))
					{				 
						metodoEstadoOrdenarListado(con,forma,mundo);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("listadoPeticiones");				 
					}		
					//Estado para la paginacion
					else if(estado.equals("redireccionListado"))
					{
						UtilidadBD.closeConnection(con);
						response.sendRedirect(forma.getLinkSiguiente());
						return null;
					}
					//Estado para el ingreso a la hoja de anestesia
					else if(estado.equals("cargarSecGeneral"))
					{
						metodoEstadoResponderPeticion(con,forma,mundo,usuario);
						UtilidadBD.closeConnection(con);

						//Por defecto se redirecciona el flujo a la primer seccion de la Hoja Anestesia 
						return mapping.findForward("secInfoCirugia");
					}
					//Estado para la creacion de la Solicitud asociada a la peticion
					else if(estado.equals("crearOrden"))
					{
						return metodoEstadoCrearOrden(con,request,mapping,forma,mundo,usuario,paciente);				 				 				 
					}			 

					//-----------------------------------------------------------------------------------
					//***********************************************************************************
					//***************************Estados del Action para las Secciones ******************
					//-----------------------------------------------------------------------------------


					//Seccion Informacion de la Cirugia
					else if(estado.equals("mostrarSeccion") && 
							codigoSeccion.equals(ConstantesSeccionesParametrizables.seccionInformacionCirugia+""))
					{				
						forma.setSeccion("");
						UtilidadBD.closeConnection(con);	
						llenarMensajeSinAutorizacionIE(forma);
						return mapping.findForward("secInfoCirugia");
					}
					//Seccion Preanestesia
					else if(estado.equals("mostrarSeccion") && 
							codigoSeccion.equals(ConstantesSeccionesParametrizables.seccionPreanestesia+""))
					{
						forma.setSeccion("");
						UtilidadBD.closeConnection(con);				 
						//Abre la funcionalidad externa
						response.sendRedirect(metodoEstadoMostrarSeccionPreanestesia(forma));				 				
					}
					//Seccion Signos Vitales
					else if(estado.equals("mostrarSeccion") && 
							codigoSeccion.equals(ConstantesSeccionesParametrizables.seccionSignosVitales+""))
					{
						forma.setSeccion("");
						UtilidadBD.closeConnection(con);				 
						//Abre la funcionalidad externa
						response.sendRedirect(metodoEstadoMostrarSeccionSignosVitales(forma));
					}
					//Seccion Record de Anestesia
					else if(estado.equals("mostrarSeccion") && 
							codigoSeccion.equals(ConstantesSeccionesParametrizables.seccionRecordAnestesia+""))
					{
						forma.setSeccion("");
						UtilidadBD.closeConnection(con);				 
						//Abre la funcionalidad externa
						response.sendRedirect(metodoEstadoMostrarSeccionRecordAnestesia(forma));
					}
					//Seccion Observaciones Generales
					else if(codigoSeccion.equals(ConstantesSeccionesParametrizables.seccionObservacionesGenerales+""))
					{				
						if(estado.equals("mostrarSeccion"))
						{
							//forma.setSeccion("");				 				 
							metodoEstadoMostrarSeccionObservacionesGenerales(con,forma,mundo);
							UtilidadBD.closeConnection(con);
							return mapping.findForward("secObservaGene");
						}
						else if(estado.equals("guardarObservaGene"))
						{
							forma.setSeccion("");
							metodoGuardarObservacionesGene(con,request,forma,mundo,usuario);
							UtilidadBD.closeConnection(con);
							return mapping.findForward("secObservaGene");
						}
					}
					//Seccion Anestesicos y Medicamentos Administrados
					//Seccion Liquidos
					//Seccion Hemoderivados
					else if(codigoSeccion.equals(ConstantesSeccionesParametrizables.seccionAnestesicosMedicaAdminis+"") || 
							codigoSeccion.equals(ConstantesSeccionesParametrizables.seccionLiquidos+"") || 
							codigoSeccion.equals(ConstantesSeccionesParametrizables.seccionHemoderivados+""))
					{
						String fPrincipal = "",dPrincipal = "";

						//Captura la informacion de los Forward a manejar por seccion
						if(codigoSeccion.equals(ConstantesSeccionesParametrizables.seccionAnestesicosMedicaAdminis+""))
						{
							fPrincipal = "secAnesMedAdm";
							dPrincipal = "secAnesMedDet";
						}
						else if (codigoSeccion.equals(ConstantesSeccionesParametrizables.seccionLiquidos+""))
						{
							fPrincipal = "secLiquidos";
							dPrincipal = "secLiquidosDet";
						}
						else if (codigoSeccion.equals(ConstantesSeccionesParametrizables.seccionHemoderivados+""))
						{
							fPrincipal = "secHemo";
							dPrincipal = "secHemoDet";
						}				 

						if(estado.equals("mostrarSeccion"))
						{						 
							forma.setSeccion("");
							forma.reset(ConstantesSeccionesParametrizables.seccionAnestesicosMedicaAdminis);

							//*****************************************************************************
							inicializarInfoSolicitudCx(con, forma, mundo,3);
							//inicializa los errores, validaciones fecha, hora ingreso
							mundo.setErrores(new ActionErrors());									
							if(!mundo.validacionesFechaHoraIngreso(
									forma.getMapaUtilitario_3_Map(HojaAnestesia.indicesSolicitudesCx[2]).toString(),
									forma.getMapaUtilitario_3_Map(HojaAnestesia.indicesSolicitudesCx[3]).toString(),
									codigoSeccion))
							{
								saveErrors(request,mundo.getErrores());
								UtilidadBD.closeConnection(con);						
								return mapping.findForward("secInfoCirugia");
							}
							//*****************************************************************************

							metodoEstadoMostrarSeccionAnestesicosMedicamentosAdmin(con,forma,mundo,Integer.parseInt(codigoSeccion));					
							UtilidadBD.closeConnection(con);						
							return mapping.findForward(fPrincipal);					
						}
						else if(estado.equals("nuevaMedica"))
						{
							nuevoMedicamentoAnestesicoSuministradoMap(forma,Integer.parseInt(codigoSeccion));
							UtilidadBD.closeConnection(con);
							return mapping.findForward(fPrincipal);
						}
						else if(estado.equals("mostrarOtrosMedicamentos"))
						{
							metodosEstadoMostrarOtrosMedicamentos(con,mundo,forma,usuario.getCodigoInstitucionInt(),Integer.parseInt(codigoSeccion));
							UtilidadBD.closeConnection(con);
							return mapping.findForward("otrosMedicam");
						}
						else if(estado.equals("mostrarDetalleMed"))
						{
							metodoEstadoMostrarDetalleMed(con,mundo,forma,Integer.parseInt(codigoSeccion));					 
							UtilidadBD.closeConnection(con);
							return mapping.findForward(dPrincipal);
						}
						else if(estado.equals("guardarAnesteMedAdmin"))
						{
							metodoGuardarAnestesicoMedicamentosAdm(con,request,forma,mundo,usuario,Integer.parseInt(codigoSeccion));	
							UtilidadBD.closeConnection(con);
							return mapping.findForward(fPrincipal);
						}
						else if(estado.equals("guardarAnesteMedAdminDet"))
						{
							metodoGuardarAnestesicoMedicamentosAdmDet(con,request,forma,mundo,usuario,Integer.parseInt(codigoSeccion));
							UtilidadBD.closeConnection(con);
							return mapping.findForward(dPrincipal);
						}				 
					}
					//Seccion Infusiones
					else if(codigoSeccion.equals(ConstantesSeccionesParametrizables.seccionInfusiones+""))
					{
						if(estado.equals("mostrarSeccion"))
						{
							forma.setSeccion("");
							forma.reset(ConstantesSeccionesParametrizables.seccionAnestesicosMedicaAdminis);

							//*****************************************************************************
							inicializarInfoSolicitudCx(con, forma, mundo,3);
							//inicializa los errores, validaciones fecha, hora ingreso
							mundo.setErrores(new ActionErrors());									
							if(!mundo.validacionesFechaHoraIngreso(
									forma.getMapaUtilitario_3_Map(HojaAnestesia.indicesSolicitudesCx[2]).toString(),
									forma.getMapaUtilitario_3_Map(HojaAnestesia.indicesSolicitudesCx[3]).toString(),
									codigoSeccion))
							{
								saveErrors(request,mundo.getErrores());
								UtilidadBD.closeConnection(con);						
								return mapping.findForward("secInfoCirugia");
							}
							//*****************************************************************************						 

							metodoEstadoMostrarSeccionInfusiones(con,forma,mundo,usuario.getCodigoInstitucionInt());
							UtilidadBD.closeConnection(con);
							return mapping.findForward("secInfu");
						}				 
						else if(estado.equals("mostrarMezclas"))
						{
							metodosEstadoMostrarMezclas(con,forma,mundo,usuario.getCodigoInstitucionInt());
							UtilidadBD.closeConnection(con);
							return mapping.findForward("otrosMedicam");
						}				
						else if(estado.equals("mostrarAdmInfusion"))
						{					 
							metodosMostrarAdmInfusion(con,forma,mundo,usuario.getCodigoInstitucionInt());
							UtilidadBD.closeConnection(con);
							return mapping.findForward("secInfuAdm");
						}				
						else if(estado.equals("nuevaInfusion"))
						{
							nuevaInfusion(forma,request,mundo);
							UtilidadBD.closeConnection(con);
							return mapping.findForward("secInfu");
						}	
						else if(estado.equals("nuevoArticulo"))
						{
							nuevoArticuloAdmInfusionMap(forma);
							UtilidadBD.closeConnection(con);
							return mapping.findForward("secInfuAdm"); 
						}					 
						else if(estado.equals("mostrarHistorialnfusion"))
						{
							metodoMostrarHistorialInfusion(con,forma,mundo);
							UtilidadBD.closeConnection(con);
							return mapping.findForward("secInfuDet");
						}
						else if(estado.equals("volverInfusiones"))
						{						
							UtilidadBD.closeConnection(con);
							return mapping.findForward("secInfu");
						}
						else if(estado.equals("eliminarArticulo"))
						{
							metodoEliminarArticulo(forma);
							UtilidadBD.closeConnection(con);
							return mapping.findForward("secInfuAdm");
						}
						else if(estado.equals("guardarInfusion"))
						{
							metodoGuardarInfusion(con,request,forma,mundo,usuario);	
							UtilidadBD.closeConnection(con);
							return mapping.findForward("secInfu");
						}
						else if(estado.equals("guardarAdmInfusion"))
						{
							metodoGuardarAdmInfusion(con,request,forma,mundo,usuario);	
							UtilidadBD.closeConnection(con);
							return mapping.findForward("secInfuAdm");
						}
						else if(estado.equals("guardarDetInfusion"))
						{
							metodoGuardarDetInfusion(con,request,forma,mundo,usuario);	
							UtilidadBD.closeConnection(con);
							return mapping.findForward("secInfuDet");
						}

					}
					//Seccion Balance de Liquidos
					else if(codigoSeccion.equals(ConstantesSeccionesParametrizables.seccionBalanceLiquidos+""))
					{
						if(estado.equals("mostrarSeccion"))
						{
							forma.setSeccion("");
							forma.reset(ConstantesSeccionesParametrizables.seccionAnestesicosMedicaAdminis);
							metodoEstadoMostrarSeccionBalanceLiq(con,forma,mundo,usuario.getCodigoInstitucionInt());
							UtilidadBD.closeConnection(con);
							return mapping.findForward("secBalanceLiq");
						}						 			
						else if(estado.equals("mostrarOtrosLiquidos"))
						{
							metodoEstadoMostrarOtrosLiquidos(con,forma,mundo,usuario.getCodigoInstitucionInt());
							UtilidadBD.closeConnection(con);
							return mapping.findForward("otrosMedicam");
						}
						else if(estado.equals("eliminarOtrosLiquidos"))
						{
							metodoEliminarOtrosLiquidos(forma);					 
							UtilidadBD.closeConnection(con);
							return mapping.findForward("secBalanceLiq");
						}
						else if(estado.equals("guardarBalanceLiq"))
						{
							metodoGuardarBalanceLiq(con,request,forma,mundo,usuario);
							UtilidadBD.closeConnection(con);
							return mapping.findForward("secBalanceLiq");
						}					 
						else if(estado.equals("nuevoLiquido"))
						{
							nuevoOtroLiquidoBalanceMap(forma);
							UtilidadBD.closeConnection(con);
							return mapping.findForward("secBalanceLiq");
						}
					}
					//Seccion Salida Paciente
					else if(codigoSeccion.equals(ConstantesSeccionesParametrizables.seccionSalidaPaciente+""))
					{
						if(estado.equals("mostrarSeccion"))
						{						 
							forma.reset(ConstantesSeccionesParametrizables.seccionSalidaPaciente);
							forma.setSeccion(ConstantesSeccionesParametrizables.seccionSalidaPaciente+"");
							metodoEstadoMostrarSeccionSalidaPaciente(con,forma,mundo,usuario.getCodigoInstitucionInt(),paciente.getCodigoPersona());

							//postulamos la fecha - hora salida paciente en caso de que no exista
							if(!UtilidadFecha.esFechaValidaSegunAp(forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesSalidasSalaPaciente[5])+""))
								forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesSalidasSalaPaciente[5], UtilidadFecha.getFechaActual());
							if(!UtilidadFecha.validacionHora(forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesSalidasSalaPaciente[6])+"").puedoSeguir)
								forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesSalidasSalaPaciente[6], UtilidadFecha.getHoraActual());


							UtilidadBD.closeConnection(con);
							return mapping.findForward("secSalidaPac");
						}
						else if(estado.equals("pacienteFallece"))
						{
							metodoPacienteFallece(con,forma,mundo,usuario.getCodigoInstitucionInt(),paciente.getCodigoPersona());
							UtilidadBD.closeConnection(con);
							return mapping.findForward("secSalidaPac");
						}
						else if(estado.equals("guardarSalida"))
						{
							metodoGuardarSalidaPacienteFinalizada(con,forma,mundo,usuario,request,paciente);
							UtilidadBD.closeConnection(con);
							return mapping.findForward("secSalidaPac");
						}
					}

					//-----------------------------------------------------------------------------------
					//***********************************************************************************
					//***********************Estados del Action para las SUB secciones ******************
					//-----------------------------------------------------------------------------------

					//SUB seccion Especialidades que Intervienen y Cirujanos Principales (Seccion Informacion General)
					else if(codigoSeccion.equals(ConstantesSeccionesParametrizables.subSeccionEspecialidadesIntervienenCiru+""))
					{
						if(estado.equals("mostrarSeccion")) 
						{
							forma.setSeccion("");
							forma.reset(ConstantesSeccionesParametrizables.subSeccionEspecialidadesIntervienenCiru);
							metodoEstadoMostrarSubSeccionEspeciInterCiru(con,forma,mundo);
							UtilidadBD.closeConnection(con);				
							return mapping.findForward("secEspInterCir");
						}			 
						else if(estado.equals("nuevaEspecInterv"))						 
						{	
							nuevaEspecialidadIntervieneMapa(con,request,forma,mundo,usuario);				 				 
							UtilidadBD.closeConnection(con);				
							return mapping.findForward("secEspInterCir");
						}
						else if(estado.equals("volverEspecialidades"))
						{				 
							UtilidadBD.closeConnection(con);
							return mapping.findForward("secEspInterCir");
						}
						else if(estado.equals("guardarEspecialidad"))
						{					
							metodoGuardarTodoEspecialiCiruja(con,request,forma,mundo,usuario); 
							UtilidadBD.closeConnection(con);
							return mapping.findForward("secEspInterCir");
						}
						else if(estado.equals("eliminarEspecialidad"))
						{
							metodoEliminarEspecialidad(forma);
							UtilidadBD.closeConnection(con);
							return mapping.findForward("secEspInterCir");
						}		
						else if(estado.equals("mostrarCirujanos"))						 
						{
							metodoEstadoMostrarCirujanos(con,forma,mundo);
							UtilidadBD.closeConnection(con);
							return mapping.findForward("secEspCirDet");					 
						}
						else if(estado.equals("nuevoCirujano"))
						{
							nuevoCirujanoPrincipal(request,forma,mundo);
							UtilidadBD.closeConnection(con);
							return mapping.findForward("secEspCirDet");	
						}				 
						else if(estado.equals("guardarCirujano"))
						{					
							metodoGuardarCirujano(con,request,forma,mundo,usuario);					 
							UtilidadBD.closeConnection(con);
							return mapping.findForward("secEspCirDet");
						}
						else if(estado.equals("eliminarCirujano"))
						{
							metodoEliminarCirujano(forma);
							UtilidadBD.closeConnection(con);
							return mapping.findForward("secEspCirDet");
						}						 
					}

					//SUB seccion Anestesiologos (Seccion Informacion General)
					else if(codigoSeccion.equals(ConstantesSeccionesParametrizables.subSeccionAnestesiologos+""))
					{
						if(estado.equals("mostrarSeccion")) 
						{

							forma.reset(ConstantesSeccionesParametrizables.subSeccionAnestesiologos);
							forma.setSeccion(ConstantesSeccionesParametrizables.subSeccionAnestesiologos+"");
							metodoEstadoMostrarSubSeccionAnestesiologos(con,forma,mundo,usuario);
							UtilidadBD.closeConnection(con);				
							return mapping.findForward("secAnestesiol");
						}	
						else if(estado.equals("guardarAnestesiologos"))
						{
							metodoEstadoguardarAnestesiologos(request,con,forma,mundo,usuario);
							UtilidadBD.closeConnection(con);
							return mapping.findForward("secAnestesiol");
						}				 
					}

					//SUB seccion Fecha y Hora de Ingreso a la Sala (Seccion Informacion General)
					else if(codigoSeccion.equals(ConstantesSeccionesParametrizables.subSeccionFechaHoraIngresoSala+""))
					{
						if(estado.equals("mostrarSeccion")) 
						{
							metodoEstadoMostrarSubSeccionFechaHoraIng(con,forma,mundo);
							UtilidadBD.closeConnection(con);
							return mapping.findForward("secFechaHoraIng");
						}
						else if(estado.equals("guardarFechaHoraIng"))
						{
							metodoEstadoGuardarFechaHoraIngreso(con,request,mundo,forma,paciente,usuario);
							UtilidadBD.closeConnection(con);
							return mapping.findForward("secFechaHoraIng");
						}
					}
				}
				catch (Exception e) {
					logger.warn("\n\n\n Error no Esperado en la Hoja de Anestesia **************************************** ");
					UtilidadBD.abortarTransaccion(con);						
					ActionErrors errores = new ActionErrors();
					errores.add("errors.notEspecific",new ActionMessage("Error en La Hoja de Anestesia. Por Favor Remitir Error con el Administrador del Sistema."));
					saveErrors(request,errores);								
					e.printStackTrace();
					logger.warn("************************************************************************************\n\n\n");
					UtilidadBD.closeConnection(con);		
					return mapping.findForward("paginaError");
				}
			}
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		} 
		return mapping.findForward("secInfoCirugia");
	}

	
	//******************************************************************************************
	//******************************************************************************************
	//******************************************************************************************
	//******************************************************************************************	

	
	/**
	 * Operaciones del estado Empezar  
	 * @param Connection con
	 * @param HojaAnestesiaForm forma
	 * @param UsuarioBasico usuario
	 * @param HojaAnestesia mundo
	 * @param PersonaBasica paciente
	 * @return ActionErrors
	 * */
	private ActionForward metodoEstadoEmpezar(Connection con, HojaAnestesiaForm forma, UsuarioBasico usuario,
			HojaAnestesia mundo, PersonaBasica paciente, HttpServletRequest request, ActionMapping mapping, boolean esDummy)
	{
		
		//Incializa los atributos de la forma 
		forma.reset(ConstantesBD.codigoNuncaValido);
		
		
		if(esDummy){ 
			this.obtenerDatosRequest(forma, request);
		}
			
			 
		 //Evalua el ingreso a la funcionalidad 
		 if(mundo.validacionRolFuncionalidad(con,usuario,670,"Hoja Anestesia"))
		 {
			 //Evalua requerimientos para el paciente y medico
			 if(!mundo.validacionIngresoFuncional(con,paciente,usuario))			
				 saveErrors(request,mundo.getErrores());			 
			 else
			 {
				 //Evalua si el usuario puede modificar o no la hoja Anestesia				 
				 forma.setValidacionesMap("esModificableHojaXMedico",HojaAnestesia.validacionEsModificableHojaXMedico(con, usuario,true)?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);					 
				 
				 //Almacena el indicador de la cuenta si es valida o no
				 forma.setValidacionesMap("esCuentaValida",mundo.validacionEsCuentaValida(con, paciente)?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				 
				 //Dependiendo del llamado a la funcionalidad carga la informaci�n de la petici�n, en el primer
				 //caso carga la informaci�n sin realizar las validaciones de petici�n y la Hoja de Anestesia es solo 
				 //de consulta, en el segundo caso valida la informaci�n de la petici�n  
				 
				 if(esDummy && (forma.getFuncionalidad().equals("consultarMod") 
						 || forma.getFuncionalidad().equals("notasRecu") 
						 	|| forma.getFuncionalidad().equals("notasGen")) 
						 	&&	forma.getSolicitud() > 0)
				 {
					 forma.setListadoPeticionesMap(mundo.consultarPeticionEspecifica(con,forma.getPeticion(),forma.getSolicitud()));
				 }
				 else				 
					 forma.setListadoPeticionesMap(mundo.consultarPeticiones(
							 con,
							 usuario.getCodigoInstitucionInt(),
							 paciente.getCodigoPersona(),
							 paciente.getCodigoIngreso(),
							 forma.getPeticion()));
								 
				//No existe Registros la petici�n no paso las validaciones necesarias para realizar la 
				//modificaci�n, se abre la hoja de anestesia solo de consulta RespCx
				if(forma.getListadoPeticionesMap("numRegistros").toString().equals("0"))
				{
					//Para el llamado desde Respuesta de Procedimientos si no es valida, se permite la consulta de la hoja
					if(esDummy && forma.getFuncionalidad().equals("RespCx") && forma.getSolicitud() > 0)
					{
						forma.setListadoPeticionesMap(mundo.consultarPeticionEspecifica(con,forma.getPeticion(),forma.getSolicitud()));
					}					 
				 }
				
				 //Si solo existe un registro y la peticion es valida, ingresa directamente a la Hoja de Anestesia 
				 if(forma.getListadoPeticionesMap("numRegistros").toString().equals("1"))
				 {
					 if(forma.getListadoPeticionesMap(HojaQuirurgica.indicesPeticiones[10]+"0").toString().equals(ConstantesBD.acronimoSi))
					 {							 
						//Indica la posicion en el mapa, el cual es solo una
						forma.setIndexPeticion("0");						 
						iniciarSolicitudMap(con,forma,false);
						
						//Validaci�n de la Hoja de Anestesia
						InfoDatos datos = new InfoDatos();
						datos = HojaAnestesia.validacionEsFinalizadaCreadaHoja(con,
									Integer.parseInt(forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString()),false);
							
						forma.setValidacionesMap("esCreadaHoja",datos.getDescripcion());
						forma.setValidacionesMap("esFinalizadaHoja",datos.getAcronimo());			
							
						//Inicializa el valor que indica si solo se puede consultar la pagina y no modificarla							
						if(esDummy && !forma.isEsModificableDummy())						
							forma.setValidacionesMap("esSoloConsultar",ConstantesBD.acronimoSi);						
						else
							forma.setValidacionesMap("esSoloConsultar",HojaAnestesia.validacionEsSoloConsulta(
									forma.getValidacionesMap("esModificableHojaXMedico").toString(),
									forma.getValidacionesMap("esFinalizadaHoja").toString()));				 
						 
						
						 return mapping.findForward("secInfoCirugia");
					 }					
				 }				 
				 				 
				 //Carga la informacion para la generacion de solicitudes
				 forma.setCentroCostosMap("numRegistros","0");			 
				 forma.setEspecialidadesMap("numRegistros","0");				 
				 iniciarSolicitudMap(con,forma,true);								 
			 }					 
		 }
		 else	
			 saveErrors(request,mundo.getErrores());
		 
		 UtilidadBD.closeConnection(con);	 
		 
		 return mapping.findForward("listadoPeticiones");	 			
	}
	
	//******************************************************************************************	
	
	/**
	 * Operaciones del estado ordenar
	 * @param Connection con
	 * @param HojaAnestesiaForm forma
	 * @param HojaAnestesia mundo
	 * */
	private void metodoEstadoOrdenarListado(Connection con,HojaAnestesiaForm forma,HojaAnestesia mundo)
	{
		 forma.setListadoPeticionesMap(mundo.accionOrdenarMapa(forma.getListadoPeticionesMap(), forma.getPatronOrdenar(),forma.getUltimoPatronOrdenar()));
		 forma.setUltimoPatronOrdenar(forma.getPatronOrdenar());
	}
	
	//******************************************************************************************
	
	/**
	 * Operaciones del estado responder peticion
	 * @param Connection con
	 * @param HojaAnestesiaForm forma
	 * @param HojaAnestesia mundo 
	 * */
	private void metodoEstadoResponderPeticion(Connection con,HojaAnestesiaForm forma,HojaAnestesia mundo, UsuarioBasico usuario)
	{
		//Incializa el indicador de operaciones (insertar,modificar,eliminar,actualizar) Exitosa
		forma.setValidacionesMap("esOperacionExitosa",ConstantesBD.acronimoNo);
		
		//Si la Peticion posee Solicitud Asociada 
		if(forma.getListadoPeticionesMap(HojaQuirurgica.indicesPeticiones[10]+forma.getIndexPeticion()).toString().equals(ConstantesBD.acronimoSi))
		{
			//Inicializa la informaci�n de la solicitud
			iniciarSolicitudMap(con, forma,false);		
			
			//Validaci�n de la Hoja de Anestesia
			InfoDatos datos = new InfoDatos();
			datos = HojaAnestesia.validacionEsFinalizadaCreadaHoja(
					con,
					Integer.parseInt(forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString()),
					false);
			
			forma.setValidacionesMap("esCreadaHoja",datos.getDescripcion());
			forma.setValidacionesMap("esFinalizadaHoja",datos.getAcronimo());			
			
			//Inicializa el valor que indica si solo se puede consultar la pagina y no modificarla
			forma.setValidacionesMap("esSoloConsultar",HojaAnestesia.validacionEsSoloConsulta(
					forma.getValidacionesMap("esModificableHojaXMedico").toString(),
					forma.getValidacionesMap("esFinalizadaHoja").toString()));
		}		
	}	
	//******************************************************************************************
	
	/**
	 * Operaciones del estado creacion peticiones
	 * @param Connection con
	 * @param HttpServletRequest request
	 * @param ActionMapping mapping
	 * @param HojaAnestesiaForm forma
	 * @param HojaAnestesia mundo
	 * @param UsuarioBasico usuario
	 * @param PersonaBasica paciente
	 * */
	private ActionForward metodoEstadoCrearOrden(
			Connection con,
			HttpServletRequest request,
			ActionMapping mapping,
			HojaAnestesiaForm forma,
			HojaAnestesia mundo,
			UsuarioBasico usuario,
			PersonaBasica paciente)
	{
		
		//Incializa el indicador de operaciones (insertar,modificar,eliminar,actualizar) Exitosa
		forma.setValidacionesMap("esOperacionExitosa",ConstantesBD.acronimoNo);
		
		//Utilidades.imprimirMapa(forma.getListadoPeticionesMap());
		//logger.info("valor del index peticion >> "+forma.getIndexPeticion());
		
		//Si la Peticion NO posee Solicitud Asociada 
		if(forma.getListadoPeticionesMap(HojaQuirurgica.indicesPeticiones[10]+forma.getIndexPeticion()).toString().equals(ConstantesBD.acronimoNo))
		{		
			//Validacion de los datos para la generacion de la solicitud		
			if(!mundo.validacionDatosSolicitud(
					forma.getSolicitudMap(),
					Listado.copyOnIndexMap(forma.getListadoPeticionesMap(),forma.getIndexPeticion(),HojaQuirurgica.indicesPeticiones),
					forma.getIndexPeticion()))
			{
				saveErrors(request,mundo.getErrores());
				UtilidadBD.closeConnection(con);
				return mapping.findForward("listadoPeticiones");
			}		
					
			//************************************************************************************
			try
			{
				UsuarioBasico usuarioResponsable = new UsuarioBasico();
				usuarioResponsable.cargarUsuarioBasico(
						con,
						Integer.parseInt(forma.getListadoPeticionesMap(HojaQuirurgica.indicesPeticiones[11]+forma.getIndexPeticion()).toString()));
				
				//Generaci�n de la Solicitud
				if(!mundo.crearSolicitud(
						con, 
						Listado.copyOnIndexMap(forma.getListadoPeticionesMap(),forma.getIndexPeticion(),HojaQuirurgica.indicesPeticiones),
						forma.getSolicitudMap(),
						(HashMap)forma.getListadoPeticionesMap(HojaQuirurgica.indicesPeticiones[9]+forma.getIndexPeticion()),
						usuarioResponsable, 
						paciente))
						{
							saveErrors(request,mundo.getErrores());
							UtilidadBD.closeConnection(con);
							return mapping.findForward("listadoPeticiones");
						}
				
			}
			catch (Exception e) {
				e.printStackTrace();
			}		
			
			//************************************************************************************	

			//Inicializa la informacion de la solicitud
			iniciarSolicitudMap(con,forma,false);			
						
			//Validacion de la Hoja de Anestesia
			InfoDatos datos = new InfoDatos();
			datos = HojaAnestesia.validacionEsFinalizadaCreadaHoja(
					con,
					Integer.parseInt(forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString()),
					false);
			
			forma.setValidacionesMap("esCreadaHoja",datos.getDescripcion());
			forma.setValidacionesMap("esFinalizadaHoja",datos.getAcronimo());
			
			//Inicializa el valor que indica si solo se puede consultar la pagina y no modificarla
			forma.setValidacionesMap("esSoloConsultar",HojaAnestesia.validacionEsSoloConsulta(
					forma.getValidacionesMap("esModificableHojaXMedico").toString(),
					forma.getValidacionesMap("esFinalizadaHoja").toString()));
			
			//**********************************************************************************************
			
			UtilidadBD.closeConnection(con);
			return mapping.findForward("secInfoCirugia");
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoPeticiones");
	}
	
	//------------------------------------------------------------------------------------------------
	// Metodos Bases de Datos Ordenados por Secciones y Subsecciones**********************************
	//------------------------------------------------------------------------------------------------
	
	/**
	 * Metodo para guardar la informacion de la SUB seccion Especialidades que Intervienen y Cirujanos Principales (Seccion Informacion General)
	 * @param Connection con
	 * @param HttpServletRequest request,
	 * @param HojaAnestesiaForm forma
	 * @param HojaAnestesia mundo
	 * */
	 private void metodoGuardarTodoEspecialiCiruja(
			 Connection con,
			 HttpServletRequest request,
			 HojaAnestesiaForm forma,
			 HojaAnestesia mundo,
			 UsuarioBasico usuario)
	 {		 		 
		 //Inicia la transaccion para el guardar
		 UtilidadBD.iniciarTransaccion(con);	 
		 
		 //inicializa los errores
		 mundo.setErrores(new ActionErrors());
		 
		 //Incializa el indicador de operaciones (insertar,modificar,eliminar,actualizar) Exitosa
		forma.setValidacionesMap("esOperacionExitosa",ConstantesBD.acronimoNo);
		
		//Valida que existan datos o procesos que realizar
		if(forma.getMapaUtilitario_4_Map("numRegistros").toString().equals("0"))
		{
			if(forma.getMapaUtilitario_1_Map("numRegistros").toString().equals("0"))
			{
				mundo.getErrores().add("descripcion",new ActionMessage("errors.notEspecific","Es Requerida una Especialidad."));
				saveErrors(request,mundo.getErrores());
			}			
		}
		
		//Operaciones que se deben de realizar antes de guardar, modificar, actualizar o eliminar Informacion de la Base de Datos
		if(!OperacionesAntesGuardar(con, forma, usuario, mundo,forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString(),usuario.getCodigoPersona()))			
		{
			 saveErrors(request,mundo.getErrores());
			 UtilidadBD.abortarTransaccion(con);
		}
		
		 //Elimina la informacion de las especialidades que intervienen
		 if(mundo.eliminarEspecialidadesIntervienen(con,forma.getMapaUtilitario_4_Map(),ConstantesBD.acronimoNo))
		 {		
			 forma.setSeccion("");
			 forma.setMapaUtilitario_4_Map(new HashMap());
			 forma.setMapaUtilitario_4_Map("numRegistros","0");
		 }
		 else
		 {			
			 saveErrors(request,mundo.getErrores());
			 UtilidadBD.abortarTransaccion(con);
		 }
		 
		 if(mundo.getErrores().isEmpty())
		 {
			 //Guardar la informaci�n de las especialidades que intervienen
			 if(mundo.insertarEspecialidadesIntervienen(con,forma.getMapaUtilitario_1_Map(),usuario.getLoginUsuario()))
			 {			
				 forma.setSeccion("");
				 metodoEstadoMostrarSubSeccionEspeciInterCiru(con,forma,mundo);				
			 }
			 else
			 {
				saveErrors(request,mundo.getErrores());
				UtilidadBD.abortarTransaccion(con);
			 }
		 }
		 
		 if(mundo.getErrores().isEmpty())
		 {			 
			 forma.setValidacionesMap("esOperacionExitosa",ConstantesBD.acronimoSi);
			 UtilidadBD.finalizarTransaccion(con);		
		 }
		 else
		 {			
			 saveErrors(request,mundo.getErrores());
			 UtilidadBD.abortarTransaccion(con);
		 }
	 }
	 
	 //************************************************************************************	
	 
	 /**
	 * Metodo para guardar la informacion de la SUB seccion Especialidades que Intervienen y Cirujanos Principales (Seccion Informacion General)
	 * @param Connection con
	 * @param HttpServletRequest request,
	 * @param HojaAnestesiaForm forma
	 * @param HojaAnestesia mundo
	 * */
	 private void metodoGuardarCirujano(Connection con,HttpServletRequest request,HojaAnestesiaForm forma,HojaAnestesia mundo,UsuarioBasico usuario)
	 {		 
		 //Inicia la transaccion para el guardar
		 UtilidadBD.iniciarTransaccion(con);
		 
		 //	inicializa los errores
		 mundo.setErrores(new ActionErrors());
		 
		 //Incializa el indicador de operaciones (insertar,modificar,eliminar,actualizar) Exitosa
		forma.setValidacionesMap("esOperacionExitosa",ConstantesBD.acronimoNo);
		
		//Valida que existan datos o procesos que realizar
		if(forma.getMapaUtilitario_3_Map("numRegistros").toString().equals("0"))
		{
			if(forma.getMapaUtilitario_2_Map("numRegistros").toString().equals("0"))
			{
				mundo.getErrores().add("descripcion",new ActionMessage("errors.notEspecific","Es Requerido un Cirujano."));
				saveErrors(request,mundo.getErrores());
			}			
		}
		 
		//Operaciones que se deben de realizar antes de guardar, modificar, actualizar o eliminar Informacion de la Base de Datos
		if(!OperacionesAntesGuardar(con, forma, usuario, mundo ,forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString(),usuario.getCodigoPersona()))			
		{
			 saveErrors(request,mundo.getErrores());
			 UtilidadBD.abortarTransaccion(con);
		}
		 
		 
		 //Elimina la informacion de los Cirujanos
		 if(mundo.eliminarCirujanosPrincipales(con,forma.getMapaUtilitario_3_Map()))
		 {
			 forma.setSeccion("");
			 forma.reset(ConstantesSeccionesParametrizables.subSeccionEspecialidadesIntervienenCiru);
		 }
		 else
		 {
			 saveErrors(request,mundo.getErrores());
			 UtilidadBD.abortarTransaccion(con);
		 }
		 
		 
		 //Guarda la informacion de los cirujanos
		 if(mundo.insertarCirujanosIntervienen(con,forma.getMapaUtilitario_2_Map(),usuario.getLoginUsuario()))		 
			 forma.setSeccion("");		 
		 else
		 {
			 saveErrors(request,mundo.getErrores());
			 UtilidadBD.abortarTransaccion(con);
		 }
		 

		 if(mundo.getErrores().isEmpty())
		 {		 
			 forma.setValidacionesMap("esOperacionExitosa",ConstantesBD.acronimoSi);
			 UtilidadBD.finalizarTransaccion(con);
		 }
	 }
	 
	 //************************************************************************************	
	 
	 /**
	  * SUB seccion Especialidades que Intervienen y Cirujanos Principales (Seccion Informacion General)
	  * Eliminar un registro del Mapa de Cirujanos
	  * @param HojaAnestesiaForm forma
	  * */
	 private void metodoEliminarCirujano(HojaAnestesiaForm forma)
	 {
		Utilidades.eliminarRegistroMapaGenerico(
				forma.getMapaUtilitario_2_Map(), 
				forma.getMapaUtilitario_3_Map(), 
				Integer.parseInt(forma.getMapaUtilitario_2_Map("indexEliminado").toString()), 
				HojaAnestesia.indicesCirujanosPrincipales, 
				"numRegistros", 
				HojaAnestesia.indicesCirujanosPrincipales[9], 
				ConstantesBD.acronimoSi, 
				false);
	 }
	 
	 //************************************************************************************	
	 
	 /**
	  * SUB seccion Especialidades que Intervienen y Cirujanos Principales (Seccion Informacion General)
	  * Eliminar un registro del Mapa de Cirujanos
	  * @param HojaAnestesiaForm forma
	  * */
	 private void metodoEliminarEspecialidad(HojaAnestesiaForm forma)
	 {
		 Utilidades.eliminarRegistroMapaGenerico(
					forma.getMapaUtilitario_1_Map(), 
					forma.getMapaUtilitario_4_Map(), 
					Integer.parseInt(forma.getIndicadorUtilitario()), 
					HojaAnestesia.indicesEspecialidadesInter, 
					"numRegistros", 
					HojaAnestesia.indicesEspecialidadesInter[4], 
					ConstantesBD.acronimoSi, 
					false);		 
	 }
	 
	 //************************************************************************************
	 
	 /**
	  * SUB seccion Anestesiologo (Seccion Informacion General)
	  * Guarda la informacion de los anestesiologos.
	  *  
	  * @param Connection con
	  * @param HojaAnestesiaForm forma
	  * @param HojaAnestesia mundo
	  * */
	 private void metodoEstadoguardarAnestesiologos(
			 HttpServletRequest request,
			 Connection con,
			 HojaAnestesiaForm forma, 
			 HojaAnestesia mundo, 
			 UsuarioBasico usuario)
	 {		 
		//Inicia la transaccion para el guardar
		UtilidadBD.iniciarTransaccion(con);
		
		//inicializa los errores
		mundo.setErrores(new ActionErrors());
		 
		 //Incializa el indicador de operaciones (insertar,modificar,eliminar,actualizar) Exitosa
		forma.setValidacionesMap("esOperacionExitosa",ConstantesBD.acronimoNo);		
		 
		//Operaciones que se deben de realizar antes de guardar, modificar, actualizar o eliminar Informacion de la Base de Datos
		if(!OperacionesAntesGuardar(con, forma, usuario, mundo,forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString(),usuario.getCodigoPersona()))			
		{
			 saveErrors(request,mundo.getErrores());
			 UtilidadBD.abortarTransaccion(con);
		}	 
		 
		 //Actualizar indicador del medico a quien se le paga los Honorarios
		 if(mundo.actualizarDefinitivoHonorarios(con, forma.getMapaUtilitario_1_Map()))		 
			forma.setMapaUtilitario_1_Map(mundo.consultarAnestesiologos(con,forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString(),"","",usuario,true));
		
		 
		 //Actualizar el Indicador de Honorario Cobrables
		 if(forma.getMapaUtilitario_2_Map().containsKey(HojaAnestesia.indicesHojaAnestesia[1]+"0") && 
				 !forma.getMapaUtilitario_2_Map(HojaAnestesia.indicesHojaAnestesia[1]+"0").toString().equals(""))
		 {
			 mundo.actualizarCobrable(con,				 
					 forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString(),
					 forma.getMapaUtilitario_2_Map(HojaAnestesia.indicesHojaAnestesia[1]+"0").toString());
		 }
		 
		 
		 if(mundo.getErrores().isEmpty())
		 {		 
			forma.setValidacionesMap("esOperacionExitosa",ConstantesBD.acronimoSi);			 
			//Finalizar transaccion
			UtilidadBD.finalizarTransaccion(con);
		 }
	 }
	 
	 //************************************************************************************
	 
	 /**
	  * 
	  * SUB seccion Fecha y Hora de Ingreso (Seccion Informacion General)
	  * Guarda la informacion de los anestesiologos.
	  * 
	  * @param Connection con
	  * @param HttpServletRequest request
	  * @param HojaAnestesia mundo
	  * @param HojaAnestesiaForm forma
	  * @param PersonaBasica paciente
	  * @param int institucion
	  * */
	 private void metodoEstadoGuardarFechaHoraIngreso(Connection con,
			 HttpServletRequest request,
			 HojaAnestesia mundo,
			 HojaAnestesiaForm forma, 
			 PersonaBasica paciente, 
			 UsuarioBasico usuario)
	 {
		 
		 //Inicia la transaccion para el guardar
		 UtilidadBD.iniciarTransaccion(con);	
		 
		//inicializa los errores
		mundo.setErrores(new ActionErrors());
		 
		 //Indicador para el exito de la operacion
		 forma.setValidacionesMap("esOperacionExitosa",ConstantesBD.acronimoNo);		 	 
		 
		//Operaciones que se deben de realizar antes de guardar, modificar, actualizar o eliminar Informacion de la Base de Datos
		if(!OperacionesAntesGuardar(con, forma, usuario, mundo,forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString(),usuario.getCodigoPersona()))			
		{
			 saveErrors(request,mundo.getErrores());
			 UtilidadBD.abortarTransaccion(con);
		}
 
		 
		//Validaciones del ingreso de la fecha y hora
		 if(mundo.validacionIngresoFechaHoraIngresoSala(
				 con,
				 Utilidades.convertirAEntero(forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString()),
				 forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesSolicitudesCx[2]).toString(),
				 forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesSolicitudesCx[3]).toString(),
				 forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesSolicitudesCx[7]).toString(),
				 forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesSolicitudesCx[8]).toString(),
				 paciente.getFechaIngreso(),
				 UtilidadesManejoPaciente.getHoraIngreso(con, paciente.getCodigoCuenta(),paciente.getCodigoUltimaViaIngreso()),
				 forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesSolicitudesCx[12]).toString(),
				 forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesSolicitudesCx[13]).toString(),				
				 usuario.getCodigoInstitucionInt()))
		 {
			 if(!mundo.actualizarFechaHoraIngreso(
					 con, 
					 forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString(),
					 forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesSolicitudesCx[2]).toString(),
					 forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesSolicitudesCx[3]).toString()))
			 {
				 saveErrors(request,mundo.getErrores());
				 UtilidadBD.abortarTransaccion(con);
			 }
			 else
				 forma.setValidacionesMap("esOperacionExitosa",ConstantesBD.acronimoSi);			 
		}
		 else
		 {
			 saveErrors(request,mundo.getErrores());
			 UtilidadBD.abortarTransaccion(con);
		 }
		 
		 
		 if(mundo.getErrores().isEmpty())
		 {	
			 //Indicador para el exito de la operacion
			 forma.setValidacionesMap("esOperacionExitosa",ConstantesBD.acronimoSi);			 
			 UtilidadBD.finalizarTransaccion(con);
		 }
	 }
	 
	 
	 //************************************************************************************
	 	 
	 /**
	  * Seccion Observaciones Generales 
	  * @param Connection con
	  * @param HttpServletRequest request
	  * @param HojaAnestesiaForm forma
	  * @param HojaAnestesia mundo
	  * @param  UsuarioBasico usuario
	  * */
	 private boolean metodoGuardarObservacionesGene(Connection con,
			 HttpServletRequest request,
			 HojaAnestesiaForm forma,
			 HojaAnestesia mundo, 
			 UsuarioBasico usuario)
	 {
		 //Inicia la transaccion para el guardar
		 UtilidadBD.iniciarTransaccion(con);		 
		 
		//inicializa los errores
		mundo.setErrores(new ActionErrors());
		 
		 //Indicador para el exito de la operacion
		 forma.setValidacionesMap("esOperacionExitosa",ConstantesBD.acronimoNo);		 	 
		 
		//Operaciones que se deben de realizar antes de guardar, modificar, actualizar o eliminar Informacion de la Base de Datos
		if(!OperacionesAntesGuardar(con, forma, usuario, mundo,forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString(),usuario.getCodigoPersona()))			
		{
			 saveErrors(request,mundo.getErrores());
			 UtilidadBD.abortarTransaccion(con);
			 return false;
		}
		else if(forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesObservacionesGenerales[7]).toString().length() > 4000)
		{
			mundo.getErrores().add("descripcion",new ActionMessage("errors.notEspecific","El tama�o maximo de caracteres para la Nueva Observaci�n es de 3999. "));
			saveErrors(request,mundo.getErrores());
			UtilidadBD.abortarTransaccion(con);
			return false;
		}		
		
		//Guarda la informacion de la seccion 
		if(!mundo.insertarObservacionesGenerales(con,
				forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString(),
				forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesObservacionesGenerales[7]).toString(),
				usuario.getLoginUsuario()))
		{
			saveErrors(request,mundo.getErrores());
			UtilidadBD.abortarTransaccion(con);
			return false;
		}
		else
			forma.setMapaUtilitario_1_Map(mundo.consultarObservacionesGenerales(con,forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString()));		
		
		
		 if(mundo.getErrores().isEmpty())
		 {	
			 //Indicador para el exito de la operacion
			 forma.setValidacionesMap("esOperacionExitosa",ConstantesBD.acronimoSi);			 
			 UtilidadBD.finalizarTransaccion(con);
			 return true;
		 }
		 
		 return true;
	 }
	
	 //************************************************************************************
	 
	 /**
	  * Seccion Anestesicos y Medicamentos Administrados. ENCABEZADO
	  * @param Connection con
	  * @param HttpServletRequest request
	  * @param HojaAnestesiaForm forma
	  * @param HojaAnestesia mundo
	  * @param UsuarioBasico usuario
	  * @param int codigoSeccion
	  * */	 
	 private void metodoGuardarAnestesicoMedicamentosAdm(
			 Connection con, 
			 HttpServletRequest request,
			 HojaAnestesiaForm forma,
			 HojaAnestesia mundo, 
			 UsuarioBasico usuario,
			 int codigoSeccion) throws IPSException
	 {			
		 //Indicador para el exito de la operaci�n
		 forma.setValidacionesMap("esOperacionExitosa",ConstantesBD.acronimoNo);
		 		 
		 //inicializa los errores
		mundo.setErrores(new ActionErrors());
		 
		 //validaciones antes de almacenar los datos
		if(mundo.validacionesExistDosisAnesteMedporIngresar(forma.getMapaUtilitario_1_Map(),false) && 
				mundo.validacionesDatosAnestesicosMedicaAdm(
					forma.getMapaUtilitario_1_Map(),
					forma.getJustificacionMap(),					
					forma.getMapaUtilitario_3_Map(HojaAnestesia.indicesSolicitudesCx[2]).toString(),
					forma.getMapaUtilitario_3_Map(HojaAnestesia.indicesSolicitudesCx[3]).toString(),
					false,
					codigoSeccion))
		{
			 //Inicia la transacci�n para el guardar
			 UtilidadBD.iniciarTransaccion(con);
			 
			//Operaciones que se deben de realizar antes de guardar, modificar, actualizar o eliminar Informacion de la Base de Datos
			if(!OperacionesAntesGuardar(con, forma, usuario, mundo,forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString(),usuario.getCodigoPersona()))			
			{
				 saveErrors(request,mundo.getErrores());
				 UtilidadBD.abortarTransaccion(con);
			}
			
			//Guarda la informaci�n de la secci�n
			if(!mundo.insertarSeccioinsertarAdminisHojaAnest(con,forma.getMapaUtilitario_1_Map(),usuario.getLoginUsuario(),codigoSeccion))
			{
				saveErrors(request, mundo.getErrores());
				UtilidadBD.abortarTransaccion(con);
			}
			else
			{
				//Justificando dependiendo del codigo de la seccion
				if(codigoSeccion == ConstantesSeccionesParametrizables.seccionAnestesicosMedicaAdminis || 
						codigoSeccion == ConstantesSeccionesParametrizables.seccionLiquidos)				
					formatoJustificacionNoPos(forma, con, usuario);
			}
			
			 if(mundo.getErrores().isEmpty())
			 {	
				 //Indicador para el exito de la operacion
				 forma.setValidacionesMap("esOperacionExitosa",ConstantesBD.acronimoSi);			
				 UtilidadBD.finalizarTransaccion(con);
			 }		
		}
		else
		{
			logger.info("--------------------------------");
			logger.info("No paso las valiaciones para guardar la Informaci�n");
			logger.info("--------------------------------");
			saveErrors(request,mundo.getErrores());
		}
		
		if(mundo.getErrores().isEmpty())
			 metodoEstadoMostrarSeccionAnestesicosMedicamentosAdmin(con,forma,mundo,codigoSeccion);
	 }
	 
	 //************************************************************************************
	 
	 public void formatoJustificacionNoPos(HojaAnestesiaForm forma, Connection con, UsuarioBasico usuario) throws IPSException
	 {
		 int numRegistros = Integer.parseInt(forma.getMapaUtilitario_1_Map().get("numRegistros").toString());
		FormatoJustArtNopos fjan=new FormatoJustArtNopos();
		
		for(int w = 0; w < numRegistros; w++ )
		{
			if(forma.getMapaUtilitario_1_Map("espos11_"+w).toString().equals("NOPOS") || forma.getMapaUtilitario_1_Map("espos11_"+w).toString().equals("f"))
			{
				if(forma.getMapaUtilitario_1_Map("suministrar8_"+w).equals(ConstantesBD.acronimoSi)){
					if(forma.getJustificacionMap().get(forma.getMapaUtilitario_1_Map("articulo2_"+w)+"_pendiente").toString().equals("0"))
					{
						double subcuenta = Cargos.obtenerCodigoSubcuentaDetalleCargo(con, Integer.parseInt(forma.getMapaUtilitario_1_Map("articulo2_"+w).toString()), forma.getNumeroSolicitud(), ConstantesBD.codigoNuncaValido, true);
						
						UtilidadJustificacionPendienteArtServ.insertarJusNP(con, forma.getNumeroSolicitud(), Integer.parseInt(forma.getMapaUtilitario_1_Map("articulo2_"+w).toString()), Utilidades.convertirAEntero(forma.getMapaUtilitario_1_Map("dosis2_"+w).toString()),usuario.getLoginUsuario(), true, false, Utilidades.convertirAEntero(subcuenta+""),"");	
					}
					else
					{
						if(forma.getJustificacionMap().get(forma.getMapaUtilitario_1_Map("articulo2_"+w)+"_pendiente").equals("1"))
						{
							// lafrecuencia estaba en 0
							fjan.insertarJustificacion(	con,
														forma.getNumeroSolicitud(),
														ConstantesBD.codigoNuncaValido,
														forma.getJustificacionMap(),
														forma.getMedicamentosNoPosMap(),
														forma.getMedicamentosPosMap(),
														forma.getSustitutosNoPosMap(),
														forma.getDiagnosticosDefinitivos(),
														Integer.parseInt(forma.getMapaUtilitario_1_Map("articulo2_"+w).toString()),
														usuario.getCodigoInstitucionInt(), 
														"", 
														ConstantesBD.continuarTransaccion,
														Integer.parseInt(forma.getMapaUtilitario_1_Map("articulo2_"+w).toString()), 
														"", 
														forma.getMapaUtilitario_1_Map("dosis2_"+w).toString(), 
														"", 
														0, 
														"", 
														"", 
														Utilidades.convertirAEntero(forma.getMapaUtilitario_1_Map("dosis2_"+w).toString()), 
														"0",
														usuario.getLoginUsuario()
														);
						}
					}
				}
			}
			//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		}
	 }
	 
	 public void formatoJustificacionNoPosInfusiones(HojaAnestesiaForm forma, Connection con, UsuarioBasico usuario) throws IPSException
	 {
		 int numRegistros = Integer.parseInt(forma.getMapaUtilitario_2_Map().get("numRegistros").toString());
		FormatoJustArtNopos fjan=new FormatoJustArtNopos();
		
		for(int w = 0; w < numRegistros; w++ )
		{
			if(forma.getMapaUtilitario_2_Map("espos9_"+w).toString().equals("NOPOS") || forma.getMapaUtilitario_2_Map("espos9_"+w).toString().equals("f"))
			{
				if(forma.getJustificacionMap().get(forma.getMapaUtilitario_2_Map("articulo2_"+w)+"_pendiente").toString().equals("0"))
				{
					double subcuenta = Cargos.obtenerCodigoSubcuentaDetalleCargo(con, Integer.parseInt(forma.getMapaUtilitario_2_Map("articulo2_"+w).toString()), forma.getNumeroSolicitud(), ConstantesBD.codigoNuncaValido, true);
					
					UtilidadJustificacionPendienteArtServ.insertarJusNP(con, forma.getNumeroSolicitud(), Integer.parseInt(forma.getMapaUtilitario_2_Map("articulo2_"+w).toString()), Utilidades.convertirAEntero(forma.getMapaUtilitario_2_Map("dosis4_"+w).toString()), usuario.getLoginUsuario(), true, false, Utilidades.convertirAEntero(subcuenta+""),"");	
				}
				else
				{
					if(forma.getJustificacionMap().get(forma.getMapaUtilitario_2_Map("articulo2_"+w)+"_pendiente").equals("1"))
					{
						//la frecuencia estaba en 0 int
						fjan.insertarJustificacion(	con,
													forma.getNumeroSolicitud(),
													ConstantesBD.codigoNuncaValido,
													forma.getJustificacionMap(),
													forma.getMedicamentosNoPosMap(),
													forma.getMedicamentosPosMap(),
													forma.getSustitutosNoPosMap(),
													forma.getDiagnosticosDefinitivos(),
													Integer.parseInt(forma.getMapaUtilitario_2_Map("articulo2_"+w).toString()),
													usuario.getCodigoInstitucionInt(), 
													"", 
													ConstantesBD.continuarTransaccion,
													Integer.parseInt(forma.getMapaUtilitario_2_Map("articulo2_"+w).toString()), 
													"", 
													forma.getMapaUtilitario_2_Map("dosis4_"+w).toString(), 
													"", 
													0, 
													"", 
													"", 
													Utilidades.convertirAEntero(forma.getMapaUtilitario_2_Map("dosis4_"+w).toString()), 
													"0",
													usuario.getLoginUsuario()
													);
					}
				}
			}
			//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		}
	 }
	 
	 /**
	  * Seccion Anestesicos y Medicamentos Administrados. DETALLE HISTORIAL DE ADMINISTRACIONES
	  * @param Connection con
	  * @param HttpServletRequest request
	  * @param HojaAnestesiaForm forma
	  * @param HojaAnestesia mundo
	  * @param UsuarioBasico usuario
	  * @param int codigoSeccion
	  * */	 
	 public void metodoGuardarAnestesicoMedicamentosAdmDet(
			 Connection con, 
			 HttpServletRequest request,
			 HojaAnestesiaForm forma,
			 HojaAnestesia mundo, 
			 UsuarioBasico usuario,
			 int codigoSeccion)
	 {
		 //Indicador para el exito de la operaci�n
		 forma.setValidacionesMap("esOperacionExitosa",ConstantesBD.acronimoNo);	 

		 //inicializa los errores
		mundo.setErrores(new ActionErrors());
		
		 //validaciones antes de almacenar los datos
		if(mundo.validacionesExistDosisAnesteMedporIngresar(forma.getMapaUtilitario_2_Map(),true) && 
				mundo.validacionesDatosAnestesicosMedicaAdm(
					forma.getMapaUtilitario_2_Map(),
					null,
					forma.getMapaUtilitario_3_Map(HojaAnestesia.indicesSolicitudesCx[2]).toString(),
					forma.getMapaUtilitario_3_Map(HojaAnestesia.indicesSolicitudesCx[3]).toString(),
					true,
					codigoSeccion))
		{
			 //Inicia la transacci�n para el guardar
			 UtilidadBD.iniciarTransaccion(con);				 	 
			 
			//Operaciones que se deben de realizar antes de guardar, modificar, actualizar o eliminar Informacion de la Base de Datos
			if(!OperacionesAntesGuardar(con, forma, usuario, mundo,forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString(),usuario.getCodigoPersona()))			
			{
				 saveErrors(request,mundo.getErrores());
				 UtilidadBD.abortarTransaccion(con);
			}
			
			//Guarda la informaci�n de la seccion		
			if(!mundo.actualizarDetaAdminisHojaAnest(con,forma.getMapaUtilitario_2_Map(),usuario.getLoginUsuario()))
			{
				saveErrors(request, mundo.getErrores());
				UtilidadBD.abortarTransaccion(con);
			}
			
			 if(mundo.getErrores().isEmpty())
			 {	
				 //Indicador para el exito de la operacion
				 forma.setValidacionesMap("esOperacionExitosa",ConstantesBD.acronimoSi);			
				 UtilidadBD.finalizarTransaccion(con);
			 }		
		}
		else
			saveErrors(request,mundo.getErrores());			
		
		if(mundo.getErrores().isEmpty())
			metodoEstadoMostrarDetalleMed(con, mundo, forma, codigoSeccion);			 
	 }
	 
	 //************************************************************************************
	 
	 /**
	  * Seccion Infusiones.
	  * @param Connection con
	  * @param HttpServletRequest request
	  * @param HojaAnestesiaForm forma
	  * @param HojaAnestesia mundo
	  * @param UsuarioBasico usuario
	  */	
	 private void metodoGuardarInfusion(Connection con,
			 HttpServletRequest request,
			 HojaAnestesiaForm forma,
			 HojaAnestesia mundo,
			 UsuarioBasico usuario)
	 {
		//Indicador para el exito de la operaci�n
		forma.setValidacionesMap("esOperacionExitosa",ConstantesBD.acronimoNo);	 

		//inicializa los errores
		mundo.setErrores(new ActionErrors());
		
		//Inicia la transacci�n para el guardar
		UtilidadBD.iniciarTransaccion(con);				 	 
		 
		//Operaciones que se deben de realizar antes de guardar, modificar, actualizar o eliminar Informacion de la Base de Datos
		if(!OperacionesAntesGuardar(con, forma, usuario, mundo,forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString(),usuario.getCodigoPersona()))			
		{
			 saveErrors(request,mundo.getErrores());
			 UtilidadBD.abortarTransaccion(con);
		}
		
		//Guarda la informaci�n de la secci�n		
		if(!mundo.actualizarInfusionesHojaAnes(con,forma.getMapaUtilitario_1_Map(),usuario.getLoginUsuario()))
		{
			saveErrors(request, mundo.getErrores());
			UtilidadBD.abortarTransaccion(con);
		}
		
		if(mundo.getErrores().isEmpty())
		{	
			//Indicador para el exito de la operacion
			forma.setValidacionesMap("esOperacionExitosa",ConstantesBD.acronimoSi);
			UtilidadBD.finalizarTransaccion(con);
		}					
		
		if(mundo.getErrores().isEmpty())
			metodoEstadoMostrarSeccionInfusiones(con, forma, mundo,usuario.getCodigoInstitucionInt());			 
	 }
	 
	 //***********************************************************************************************
	 
	 /**
	  * Seccion Infusiones
	  * Elimina registros del mapa
	  * @param HojaAnestesiaForm forma
	  * */
	 public void metodoEliminarArticulo(HojaAnestesiaForm forma)
	 {		 
		 Utilidades.eliminarRegistroMapaGenerico(
					forma.getMapaUtilitario_2_Map(), 
					forma.getMapaUtilitario_3_Map(), 
					Integer.parseInt(forma.getMapaUtilitario_2_Map("indexEliminar").toString()), 
					HojaAnestesia.indicesDetAdmInfusionesHojaAnes, 
					"numRegistros", 
					HojaAnestesia.indicesDetAdmInfusionesHojaAnes[5], 
					ConstantesBD.acronimoSi, 
					false);		 
		 
		 //Captura la informacion de los articulos insertados
		 forma.setMapaUtilitario_2_Map("codigosArticulosInsertados",HojaAnestesia.getCodigosInsertados(forma.getMapaUtilitario_2_Map(),HojaAnestesia.indicesDetAdmInfusionesHojaAnes[2]));		 
	 }
	 
	 //***********************************************************************************************
	 
	 /**
	  * Almacena la informacion sobre las administraciones de los articulos asociados a la mezcla
	  * @param Connection con
	  * @param HttpServletRequest request
	  * @param HojaAnestesiaForm forma
	  * @param HojaAnestesia mundo
	  * @param UsuarioBasico usuario
	  * */
	 public void metodoGuardarAdmInfusion(
			 Connection con,
			 HttpServletRequest request,
			 HojaAnestesiaForm forma,
			 HojaAnestesia mundo,
			 UsuarioBasico usuario) throws IPSException
	 {
		 //Indicador para el exito de la operaci�n
		 forma.setValidacionesMap("esOperacionExitosa",ConstantesBD.acronimoNo);	 

		 //inicializa los errores
		 mundo.setErrores(new ActionErrors());
		 
		 //validaciones antes de almacenar los datos
		if(mundo.validacionesDatosInfusionesHojaAnes(
					forma.getMapaUtilitario_1_Map(),
					Integer.parseInt(forma.getIndicadorUtilitario()),
					forma.getMapaUtilitario_3_Map(HojaAnestesia.indicesSolicitudesCx[2]).toString(),
					forma.getMapaUtilitario_3_Map(HojaAnestesia.indicesSolicitudesCx[3]).toString(),
					""
					) && mundo.validacionesDatosDetInfusiones(forma.getMapaUtilitario_2_Map(),forma.getJustificacionMap()))
		{			
			//Inicia la transacci�n para el guardar
			UtilidadBD.iniciarTransaccion(con);				 	 
			 
			//Operaciones que se deben de realizar antes de guardar, modificar, actualizar o eliminar Informacion de la Base de Datos
			if(!OperacionesAntesGuardar(con, forma, usuario, mundo,forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString(),usuario.getCodigoPersona()))			
			{
				 saveErrors(request,mundo.getErrores());
				 UtilidadBD.abortarTransaccion(con);
			}
			
			int consecutivo = ConstantesBD.codigoNuncaValido;
			
			if(forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[0]+forma.getIndicadorUtilitario()).toString().equals(""))
			{
			
			//Guarda la informaci�n de la Infusion		
			consecutivo = mundo.insertarInfusionesHojaAnes(con,
							forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString(), 
							forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[12]+forma.getIndicadorUtilitario()).toString(), 
							forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[4]+forma.getIndicadorUtilitario()).toString(), 
							forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[6]+forma.getIndicadorUtilitario()).toString(), 
							forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[7]+forma.getIndicadorUtilitario()).toString(), 
							usuario.getLoginUsuario(),
							forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[11]+forma.getIndicadorUtilitario()).toString());			
			}
			else
				consecutivo = Integer.parseInt(forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[0]+forma.getIndicadorUtilitario()).toString());			
			
			if(consecutivo != ConstantesBD.codigoNuncaValido)
			{				
				//Guarda la informaci�n de la Administraci�n
				int consecutivoAdm = mundo.insertarAdmInfusionesHojaAnes(con,
						consecutivo,
						forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesAdmInfusionesHojaAnes[2]+forma.getIndicadorUtilitario()).toString(),
						forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesAdmInfusionesHojaAnes[3]+forma.getIndicadorUtilitario()).toString(),
						usuario.getLoginUsuario());				
				
				if(consecutivoAdm != ConstantesBD.codigoNuncaValido)
				{					
					if(!mundo.insertarDetInfusionesHojaAnes(con,consecutivoAdm,forma.getMapaUtilitario_2_Map(),usuario.getLoginUsuario()))
					{
						saveErrors(request, mundo.getErrores());
						UtilidadBD.abortarTransaccion(con);
					}
					else
					{
						//************Guarda Justificacion de articulo infusion**********//
						formatoJustificacionNoPosInfusiones(forma, con, usuario);
						//**************************************************************//
					}
				}
				else
				{
					saveErrors(request, mundo.getErrores());
					UtilidadBD.abortarTransaccion(con);
				}				
			}
			else
			{
				saveErrors(request, mundo.getErrores());
				UtilidadBD.abortarTransaccion(con);
			}
				
			
			 if(mundo.getErrores().isEmpty())
			 {	
				 //Indicador para el exito de la operaci�n
				 forma.setValidacionesMap("esOperacionExitosa",ConstantesBD.acronimoSi);
				 
				 //Actualiza el consecutivo de la funcion en el caso de no existir en base de datos
				 if(forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[5]+forma.getIndicadorUtilitario()).toString().equals(ConstantesBD.acronimoNo))
				 {					 
					forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[0]+forma.getIndicadorUtilitario(),consecutivo);
					forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[5]+forma.getIndicadorUtilitario(),ConstantesBD.acronimoSi);
					forma.setMapaUtilitario_1_Map("codigosArticulosInsertados",HojaAnestesia.getCodigosInsertados(forma.getMapaUtilitario_1_Map(),HojaAnestesia.indicesInfusionesHojaAnes[0]));
				 }
				 
				 UtilidadBD.finalizarTransaccion(con);
			 }		
		}
		else
			saveErrors(request,mundo.getErrores());
	 }
	 
	 //***********************************************************************************************
	 
	 /**
	  * Guarda la informaci�n del detalle de la infusion
	  * @param Connection con
	  * @param HttpServletRequest request
	  * @param HojaAnestesiaForm forma
	  * @param HojaAnestesia mundo
	  * @param UsuarioBasico usuario
	  * */
	 public void metodoGuardarDetInfusion(Connection con,
			 HttpServletRequest request,
			 HojaAnestesiaForm forma,
			 HojaAnestesia mundo,
			 UsuarioBasico usuario)
	 {
		//Indicador para el exito de la operaci�n
		forma.setValidacionesMap("esOperacionExitosa",ConstantesBD.acronimoNo);	 


		 //inicializa los errores
		 mundo.setErrores(new ActionErrors());
		
		
		 //validaciones antes de almacenar los datos
		if(mundo.validacionesDatosHistorialInfusiones(
					forma.getMapaUtilitario_2_Map(),
					forma.getJustificacionMap(),
					forma.getMapaUtilitario_3_Map(HojaAnestesia.indicesSolicitudesCx[2]).toString(),
					forma.getMapaUtilitario_3_Map(HojaAnestesia.indicesSolicitudesCx[3]).toString()
					))
		{			
			//Inicia la transacci�n para el guardar
			UtilidadBD.iniciarTransaccion(con);				 	 
			 
			//Operaciones que se deben de realizar antes de guardar, modificar, actualizar o eliminar Informaci�n de la Base de Datos
			if(!OperacionesAntesGuardar(con, forma, usuario, mundo,forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString(),usuario.getCodigoPersona()))			
			{
				 saveErrors(request,mundo.getErrores());
				 UtilidadBD.abortarTransaccion(con);
			}			
							
			//Actualiza la informaci�n de Administraci�n
			if(!mundo.actualizarAdmInfusionesHojaAnes(
					con,
					forma.getMapaUtilitario_2_Map(),
					usuario.getLoginUsuario()))										
			{
				saveErrors(request, mundo.getErrores());
				UtilidadBD.abortarTransaccion(con);
			}				
			
			 if(mundo.getErrores().isEmpty())
			 {	
				 //Indicador para el exito de la operaci�n
				 forma.setValidacionesMap("esOperacionExitosa",ConstantesBD.acronimoSi);			 
				 UtilidadBD.finalizarTransaccion(con);
			 }		
		}
		else
			saveErrors(request,mundo.getErrores());		 		 
	 }	 

	 //***********************************************************************************************
	 
	 /**
	  * Seccion Balance de Liquidos
	  * Elimina registros del mapa
	  * @param HojaAnestesiaForm forma
	  * */
	 public void metodoEliminarOtrosLiquidos(HojaAnestesiaForm forma)
	 {		 
		 Utilidades.eliminarRegistroMapaGenerico(
					forma.getMapaUtilitario_1_Map(), 
					forma.getMapaUtilitario_3_Map(), 
					Integer.parseInt(forma.getMapaUtilitario_1_Map("indexEliminar").toString()), 
					HojaAnestesia.indicesBalancesLiquidosHojaAnes, 
					"numRegistros", 
					HojaAnestesia.indicesBalancesLiquidosHojaAnes[4], 
					ConstantesBD.acronimoSi, 
					false);		 
		 
		 //Captura la informaci�n de los codigos Insertados
		 forma.setMapaUtilitario_1_Map("codigosInsertados",HojaAnestesia.getCodigosInsertados(forma.getMapaUtilitario_1_Map(),HojaAnestesia.indicesBalancesLiquidosHojaAnes[1]));
		 //Se captura el nuevo valor de total de secciones
		 forma.setMapaUtilitario_1_Map("valorTotal",Integer.parseInt(forma.getMapaUtilitario_2_Map("totalSecciones").toString())-HojaAnestesia.getTotalCampo(forma.getMapaUtilitario_1_Map(),HojaAnestesia.indicesBalancesLiquidosHojaAnes[2]));
	 }	 

	 //***********************************************************************************************
	 
	 /**
	  * Seccion Balance de Liquidos
	  * Guarda la informacion de la seccion Balance de liquidos
	  * @param Connection con
	  * @param HttpServletRequest request
	  * @param HojaAnestesiaForm forma
	  * @param HojaAnestesia mundo
	  * @param UsuarioBasico usuario
	  * */
	 public void metodoGuardarBalanceLiq(
			 Connection con,
			 HttpServletRequest request,
			 HojaAnestesiaForm forma,
			 HojaAnestesia mundo,
			 UsuarioBasico usuario)
	 {
		//Indicador para el exito de la operaci�n
		forma.setValidacionesMap("esOperacionExitosa",ConstantesBD.acronimoNo);
		

		 //inicializa los errores
		 mundo.setErrores(new ActionErrors());
		
		 
		 //validaciones antes de almacenar los datos
		if(mundo.validacionesDatosBalanceLiquidos(forma.getMapaUtilitario_1_Map()))
		{			
			//Inicia la transacci�n para el guardar
			UtilidadBD.iniciarTransaccion(con);				 	 
			 
			//Operaciones que se deben de realizar antes de guardar, modificar, actualizar o eliminar Informaci�n de la Base de Datos
			if(!OperacionesAntesGuardar(con, forma, usuario, mundo,forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString(),usuario.getCodigoPersona()))			
			{
				 saveErrors(request,mundo.getErrores());
				 UtilidadBD.abortarTransaccion(con);
			}			
							
			//Inserta la informaci�n del balance de liquidos
			if(!mundo.insertarBalancesLiquidosHojaAnes(
					con,
					forma.getMapaUtilitario_1_Map(),
					usuario.getLoginUsuario()))										
			{
				saveErrors(request, mundo.getErrores());
				UtilidadBD.abortarTransaccion(con);
			}				
			
			//Actualiza la informaci�n del balance de liquidos
			if(!mundo.actualizarBalancesLiquidos(
					con,
					forma.getMapaUtilitario_1_Map(),
					usuario.getLoginUsuario()))										
			{
				saveErrors(request, mundo.getErrores());
				UtilidadBD.abortarTransaccion(con);
			}
			
			
			 if(mundo.getErrores().isEmpty())
			 {	
				 //Indicador para el exito de la operaci�n
				 forma.setValidacionesMap("esOperacionExitosa",ConstantesBD.acronimoSi);		
				 UtilidadBD.finalizarTransaccion(con);
				 this.metodoEstadoMostrarSeccionBalanceLiq(con,forma,mundo,usuario.getCodigoInstitucionInt());
			 }		
		}
		else
			saveErrors(request,mundo.getErrores());		  
	 }
	 
	 
	 //***********************************************************************************************
	 
	 /**
	  * Guarda la informaci�n de la salida del paciente
	  * @param Connection con
	  * @param HojaAnestesiaForm forma
	  * @param HojaAnestesia mundo
	  * @param String loginUsuario
	  * @param HttpServletRequest request
	  * */
	 public boolean metodoGuardarSalidaPacienteFinalizada(
			 Connection con,
			 HojaAnestesiaForm forma,
			 HojaAnestesia mundo,
			 UsuarioBasico usuario,
			 HttpServletRequest request,
			 PersonaBasica paciente) throws IPSException
	 {	
		 
		//Utilidades.imprimirMapa(forma.getMapaUtilitario_1_Map());
		
		logger.info("\n\n");
		logger.info("\n\n");
		logger.info("------------------------------------------------------------------");
		logger.info("**********************Informe Operaciones Guardar Salida Paciente ");
		logger.info("\n\n");
		 
		 //Indicador para el exito de la operaci�n
		forma.setValidacionesMap("esOperacionExitosa",ConstantesBD.acronimoNo);		

		//inicializa los errores
		mundo.setErrores(new ActionErrors());			
		//Inicializa los arrayList Utilitarios
		mundo.setUtilitarioList(new ArrayList());		
		forma.setArrayListUtilitario_2(new ArrayList());
		forma.setArrayListUtilitario_3(new ArrayList());
			 
		//validaciones antes de almacenar los datos
		logger.info("\n");
		logger.info("OPERACION: >> Validaciones Datos Salida Paciente");
		if(mundo.validacionesDatosSalidaPaciente(
				con,
				Utilidades.convertirAEntero(forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString()),
				forma.getMapaUtilitario_1_Map(),
				forma.getMapaUtilitario_3_Map(HojaAnestesia.indicesSolicitudesCx[2]).toString(),
				forma.getMapaUtilitario_3_Map(HojaAnestesia.indicesSolicitudesCx[3]).toString(),
				forma.getMapaUtilitario_3_Map(HojaAnestesia.indicesSolicitudesCx[4]).toString(),
				forma.getMapaUtilitario_3_Map(HojaAnestesia.indicesSolicitudesCx[5]).toString(),
				forma.getArrayArticuloIncluidoDto(),
				forma.getJustificacionMap())				
				)
		{			
			//Inicia la transacci�n para el guardar
			UtilidadBD.iniciarTransaccion(con);				 	 
			
			//-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
			logger.info("\n");
			logger.info("OPERACION: >> Realiza Operaciones antes de Guardar ");
			//Operaciones que se deben de realizar antes de guardar, modificar, actualizar o eliminar Informaci�n de la Base de Datos
			if(!OperacionesAntesGuardar(con, forma, usuario, mundo,forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString(),usuario.getCodigoPersona()))			
			{
				 saveErrors(request,mundo.getErrores());
				 UtilidadBD.abortarTransaccion(con);
				 logger.info("------------------------------------------------------------------");
				 logger.info("\n\n");
				 return false;
			}			
						
			//-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
			
			//Se verifica que la operacion no sea deseleccionar la salida del paciente			
			if(!forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesSalidasSalaPaciente[2]).toString().equals(""))
			{
				logger.info("\n");
				logger.info("OPERACION: >> Entro dar Salida Paciente ");				
			
				//Se actualiza las cantidades del detalle de materiales Qx
				logger.info("\n");
				logger.info("OPERACION: >> Actualiza las cantidades de Materiales Qx >> ");
				HojaAnestesia.actualizarCantidadesMaterialesQx(con, forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString());
				
				 //Se verifica si existen medicamentos pendientes por generar consumo
				logger.info("\n");
				logger.info("OPERACION: >> Valida existencia de Medicamentos pendientes por generar consumo >> "+forma.getValidacionesMap("existeArticulosXConsumo").toString());
				
				 if(mundo.getErrores().isEmpty() && 
						 forma.getValidacionesMap("existeArticulosXConsumo").toString().equals(ConstantesBD.acronimoSi) && 
						 	!forma.getMapaUtilitario_2_Map("numRegistros").toString().equals("0") && 
						 		mundo.validacionesExisteArticuloGenerarCons(forma.getMapaUtilitario_2_Map(),HojaAnestesia.indicesDetMedAdminHojaAnes[6]))
				 {
					 //Inserta los consumos
					 logger.info("\n");
					 logger.info("OPERACION: >> Insertar Consumos ");
					 if(!mundo.insertarConsumosSecciones(con,
							forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString(),
							forma.getMapaUtilitario_2_Map(),
							usuario))	
					 {
						 saveErrors(request,mundo.getErrores());
						 UtilidadBD.abortarTransaccion(con);
						 logger.info("------------------------------------------------------------------");
	 					 logger.info("\n\n");
						 return false;
					 }				 			 
					 
					 //Almacena la informaci�n de los mensajes generados para Advertencia
					 forma.setArrayListUtilitario_2(mundo.getUtilitarioList());			 
				 }			 
				 
				 //-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
				 logger.info("\n");
				 logger.info("OPERACION: >> Generacion de Ordenes de Procedimientos");
				 //Generaci�n de Ordenes de Procedimientos Automaticas
				 if(mundo.getErrores().isEmpty())
				 {										 				 		 
					 if(!AccesosVascularesHA.generarOrdenProcedimientosAccesosVasculares(con, forma.getNumeroSolicitud(), usuario, paciente))					 	 
						UtilidadBD.abortarTransaccion(con);					 
				 }
				 else
				 {
					 saveErrors(request,mundo.getErrores());
					 UtilidadBD.abortarTransaccion(con);
					 logger.info("------------------------------------------------------------------");
 					 logger.info("\n\n");
					 return false;
				 }
			 
				 //-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*			 
			 
				 if(mundo.getErrores().isEmpty())
				 {					
					 logger.info("\n");
					 logger.info("OPERACION: >> Actualizacion del Estado de la Solicitud");
					 //Actualizaci�n del estado de la solicitud
					 mundo.actualizarEstadoSolicitud(
							 con, 
							 forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString(), 
							 forma.getMapaUtilitario_3_Map(HojaAnestesia.indicesSolicitudesCx[6]).toString(), 
							 forma.getListadoPeticionesMap(HojaQuirurgica.indicesPeticiones[2]+forma.getIndexPeticion()).toString(),
							 usuario.getCodigoPersona());
					 
					 //Almacena la informaci�n de los mensajes generados para Advertencia
					 forma.setArrayListUtilitario_2(mundo.getUtilitarioList());			 				 
				 }				 
			}
			else
			{
				logger.info("\n");
				logger.info("OPERACION: >> Entro Reversar Salida Paciente ");
			}
			 
			if(mundo.getErrores().isEmpty())
			{
				logger.info("\n");
				logger.info("OPERACION: >> Finalizacion de la Hoja de Anestesia");
				//finalizaci�n de la Hoja de Anestesia
				if(!mundo.finalizarHojaAnestesia(
					 con, 
					 forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString(),
					 forma.getListadoPeticionesMap(HojaQuirurgica.indicesPeticiones[2]+forma.getIndexPeticion()).toString(),
					 forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesSalidasSalaPaciente[2]).toString(),						 
					 forma.getValidacionesMap("esFinalizadaHoja").toString(), 
					 forma.getMapaUtilitario_3_Map(HojaAnestesia.indicesSolicitudesCx[2]).toString(),
					 forma.getMapaUtilitario_3_Map(HojaAnestesia.indicesSolicitudesCx[3]).toString(),
					 forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesSalidasSalaPaciente[5]).toString(),
					 forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesSalidasSalaPaciente[6]).toString(),
					 usuario,			
					 paciente,
					 forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesSalidasSalaPaciente[8]).toString(),
					 forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesSalidasSalaPaciente[9]).toString(),
					 forma.getAcronimoDiagFallece(),
					 forma.getTipoCieDiagFallece(),
					 forma.getArrayArticuloIncluidoDto(),
					 forma.getJustificacionMap(),
					 forma.getMedicamentosNoPosMap(),
					 forma.getMedicamentosPosMap(),
					 forma.getSustitutosNoPosMap(),
					 forma.getDiagnosticosDefinitivos()
					))
				{
					 saveErrors(request,mundo.getErrores());
					 UtilidadBD.abortarTransaccion(con);
					 logger.info("------------------------------------------------------------------");
					 logger.info("\n\n");
					 return false;
				}	
				else
				{
					logger.info("\n");
					logger.info("OPERACION: >> Exito!! Finalizacion de la Hoja de Anestesia");
					
					//Indicador para el exito de la operaci�n
					forma.setValidacionesMap("esOperacionExitosa",ConstantesBD.acronimoSi);
					
					//UtilidadBD.abortarTransaccion(con);
					UtilidadBD.finalizarTransaccion(con);		
					
					 //-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
					if(!mundo.getIndicadorUtilitario().equals("reversarPaciente"))
					{
						//Inicia la transacci�n para el guardar
						UtilidadBD.iniciarTransaccion(con);
						
						logger.info("\n");
						logger.info("OPERACION: >> Generacion Automatica de la Liquidacion de la Cirugia");
						//Generaci�n de la Liquidaci�n de Cirugias
						LiquidacionServicios liquidacion = new LiquidacionServicios();
						if(!mundo.generarliquidacion(
							 con,
							 liquidacion, 
							 forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString(), 
							 usuario))
						{
							forma.setArrayListUtilitario_3(mundo.getUtilitarioList1());				
							//UtilidadBD.abortarTransaccion(con);
							UtilidadBD.abortarTransaccion(con);		
						 }
						 
						 //Almacena la informaci�n de los mensajes generados para Advertencia
						 forma.setArrayListUtilitario_2(mundo.getUtilitarioList());
						 
						 //UtilidadBD.abortarTransaccion(con);
						 UtilidadBD.finalizarTransaccion(con);					
					}
					//-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*					
					
					//Validaci�n de la Hoja de Anestesia
					InfoDatos datos = new InfoDatos();
					datos = HojaAnestesia.validacionEsFinalizadaCreadaHoja(
								con,
								Integer.parseInt(forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString()),
								false);
						
					forma.setValidacionesMap("esCreadaHoja",datos.getDescripcion());
					forma.setValidacionesMap("esFinalizadaHoja",datos.getAcronimo());					
					
					//Inicializa el valor que indica si solo se puede consultar la pagina y no modificarla
					forma.setValidacionesMap("esSoloConsultar",HojaAnestesia.validacionEsSoloConsulta(
							forma.getValidacionesMap("esModificableHojaXMedico").toString(),
							forma.getValidacionesMap("esFinalizadaHoja").toString()));					
										
					metodoEstadoMostrarSeccionSalidaPaciente(con,forma,mundo,Integer.parseInt(usuario.getCodigoInstitucion()),paciente.getCodigoPersona());
										
					//Si se reversa la salida del paciente el ckeck de fallece queda activo para su modificaci�n
					if(mundo.getIndicadorUtilitario().equals("reversarPaciente"))					
						forma.setValidacionesMap("esFalleceDesdeQx",ConstantesBD.acronimoNo);
					
					logger.info("------------------------------------------------------------------");
					logger.info("\n\n");
					return true;
				}
			}			
		}
		else
			saveErrors(request,mundo.getErrores());
		
		logger.info("------------------------------------------------------------------");
		logger.info("\n\n");
		return false;
	 }
	 
	 //***********************************************************************************************
	 
	//------------------------------------------------------------------------------------------------
	// Metodos Mostrar Secciones *********************************************************************
	//------------------------------------------------------------------------------------------------

	/**
	 * Operaciones para el estado de mostrar seccion Preanestesia
	 * @param Connection con
	 * @param HojaAnestesiaForm forma
	 * */
	private String metodoEstadoMostrarSeccionPreanestesia(HojaAnestesiaForm forma)
	{
		boolean esResumen = false;
		int ocultarCabezote = 0;
		
		if(forma.isOcultarEncabezado())
			ocultarCabezote = 1;
		
		//Evalua si la Hoja de Anestesia se encuentra finalizada
		if(forma.getValidacionesMap("esSoloConsultar").toString().equals(ConstantesBD.acronimoSi))
			esResumen = true;
			
		return "../ingresarPreanestesia/ingresarPreanestesia.do?" +
				"estado=empezar" +
				"&nroPeticion="+forma.getListadoPeticionesMap(HojaQuirurgica.indicesPeticiones[2]+forma.getIndexPeticion())+"" +
				"&peticionCirugia="+forma.getListadoPeticionesMap(HojaQuirurgica.indicesPeticiones[2]+forma.getIndexPeticion())+"&" +
				"ocultarCabezote="+ocultarCabezote+"&"+
				"mostrarMenuHojaAnestesia="+ConstantesBD.acronimoSi+"&" +
				"estadoSolicitud="+forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[15]+"_0").toString()+"&" +
				"numeroSolicitud="+forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString()+"&" +
				"esResumen="+esResumen;
	}
	

	//******************************************************************************************
	
	
	/**
	 * Operaciones para el estado de mostrar seccion Signos Vitales
	 * @param Connection con
	 * @param HojaAnestesiaForm forma
	 * */
	private String metodoEstadoMostrarSeccionSignosVitales(HojaAnestesiaForm forma)
	{
		boolean esResumen = false;
		
		//Evalua si la Hoja de Anestesia se encuentra finalizada
		if(forma.getValidacionesMap("esSoloConsultar").toString().equals(ConstantesBD.acronimoSi))
			esResumen = true;
			
				
		return "../signosVitalesAnestesia/signosVitalesAnestesia.do?" +
				"estado=empezarSignosVitalesAnestesia&numeroSolicitud="+forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString()+"&"+				
				"esConsulta="+esResumen;		
	}
	
	//******************************************************************************************
		
	/**
	 * Operaciones para el estado de mostrar seccion Record de Anestesia
	 * @param Connection con
	 * @param HojaAnestesiaForm forma
	 * */
	private String metodoEstadoMostrarSeccionRecordAnestesia(HojaAnestesiaForm forma)
	{			
		return "../graficaRecordAnestesia/graficaRecordAnestesia.do?" +
				"estado=graficaRecordAnestesia&numeroSolicitud="+forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString();	
	}
	
	
	//******************************************************************************************
	
	/**
	 * Operaciones para el estado mostrar seccion observaciones generales
	 * @param Connection con
	 * @param HojaAnestesiaForm forma
	 * @param HojaAnestesia mundo
	 * */
	private void metodoEstadoMostrarSeccionObservacionesGenerales(Connection con,HojaAnestesiaForm forma,HojaAnestesia mundo)
	{
		//Consulta la informacion de las Observaciones Generales 
		forma.setMapaUtilitario_1_Map(mundo.consultarObservacionesGenerales(con,forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString()));		
	}
	
	//******************************************************************************************
	
	/**
	 * Operaciones para el estado mostrar seccion anestesicos y medicamentos administrados
	 * @param Connection con
	 * @param HojaAnestesiaForm forma
	 * @param HojaAnestesia mundo
	 * @param codigoSeccion
	 * */
	private void metodoEstadoMostrarSeccionAnestesicosMedicamentosAdmin(Connection con,HojaAnestesiaForm forma,HojaAnestesia mundo, int codigoSeccion)
	{
		//Consulta la informacion de los medicamentos administrados en la Hoja de Anestesia
		forma.setMapaUtilitario_1_Map(mundo.consultarAdminisHojaAnest(con, 
				forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString(),
				codigoSeccion+"",
				ConstantesBD.acronimoNo,
				""));
		
		//Indicador de Otro medicamento
		forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesMedAdminHojaAnes[3],ConstantesBD.acronimoNo);
		//Seccion
		forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesMedAdminHojaAnes[5],codigoSeccion);
		
		//Carga la informaci�n de la solicitud CX
		if(!forma.getMapaUtilitario_3_Map().containsKey(HojaAnestesia.indicesSolicitudesCx[0]))
			forma.setMapaUtilitario_3_Map(mundo.consultarInfoSolicitudCx(con,forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString()));		
	}
	
	//******************************************************************************************
	
	/**
	 * Operaciones para el estado mostrar secci�n infusiones
	 * @param Connection con
	 * @param HojaAnestesiaForm forma
	 * @param HojaAnestesia mundo
	 * */
	private void metodoEstadoMostrarSeccionInfusiones(Connection con,HojaAnestesiaForm forma,HojaAnestesia mundo, int institucion)
	{
		//Consulta la informaci�n de las infusiones
		forma.setMapaUtilitario_1_Map(mundo.consultarInfusionesHojaAnestesia(con, forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString()));
		
		//Carga la informaci�n de la solicitud CX
		if(!forma.getMapaUtilitario_3_Map().containsKey(HojaAnestesia.indicesSolicitudesCx[0]))
			forma.setMapaUtilitario_3_Map(mundo.consultarInfoSolicitudCx(con,forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString()));
	}
	
	//******************************************************************************************
	
	/**
	 * Muestra la seccion de salida del paciente
	 * @param Connection con
	 * @param HojaAnestesiaForm forma
	 * @param HojaAnestesia mundo
	 * @param int institucion
	 * */
	private void metodoEstadoMostrarSeccionSalidaPaciente(
			Connection con,
			HojaAnestesiaForm forma,
			HojaAnestesia mundo,
			int institucion,
			int codigoPaciente)
	{
		//Validaci�n de la funcionalidad de Hoja de Anestesia para el ingreso a la salida del paciente
		if(!mundo.centralValidacionesSeccionesHojaAnestesia(
				con,
				forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString(),
				institucion))
		{
			forma.setArrayListUtilitario_1(mundo.getUtilitarioList());				
			forma.setEstado("mostrarErrores");
		}		
		else
		{				
			
			HashMap tmp = HojaQuirurgica.consultarSalidaPaciente(con,forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString());
			
			//Indicador de viene fallecido desde Hoja Quirurgica
			forma.setValidacionesMap("esFalleceDesdeQx",tmp.get(HojaQuirurgica.indicesSalidaSala[8]));
			//Fallece
			forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesSalidasSalaPaciente[7],tmp.get(HojaQuirurgica.indicesSalidaSala[8]));
			//Diagnostico Fallece
			forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesSalidasSalaPaciente[10],tmp.get(HojaQuirurgica.indicesSalidaSala[9]));
			//Fecha Fallece
			forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesSalidasSalaPaciente[8],tmp.get(HojaQuirurgica.indicesSalidaSala[6]));
			//Hora Fallece
			forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesSalidasSalaPaciente[9],tmp.get(HojaQuirurgica.indicesSalidaSala[7]));	

			//Consulta la informaci�n de las salidas del paciente
			metodoPacienteFallece(con, forma, mundo, institucion, codigoPaciente); 						
							
			//consulta la informaci�n de los articulos por generar consumos
			forma.setMapaUtilitario_2_Map(mundo.consultarGeneracionConsumos(
					con, 
					institucion+"", 
					forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString()));
			
			if(forma.getMapaUtilitario_2_Map("numRegistros").toString().equals("0"))				
				forma.setValidacionesMap("existeArticulosXConsumo",ConstantesBD.acronimoNo);
			else
				forma.setValidacionesMap("existeArticulosXConsumo",ConstantesBD.acronimoSi);			
						
			//Verifica que los cargos de la solicitud tengan estado pendiente y no tenga faturado ninguno de los cargos
			forma.setValidacionesMap("esSolicitudTotalPendiente",
					HojaQuirurgica.essolicitudtotalpendiente(con,forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString()));	
									
			//Carga la informaci�n de la solicitud CX
			forma.setMapaUtilitario_3_Map(mundo.consultarInfoSolicitudCx(con,forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString()));
			
			//Captura la informacion de la Salida Sala Paciente
			//Codigo Salida Paciente Centro Costo
			forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesSalidasSalaPaciente[1],forma.getMapaUtilitario_3_Map(HojaAnestesia.indicesSolicitudesCx[10]));
			//Descripcion Salida Paciente Centro Costo
			forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesSalidasSalaPaciente[3],forma.getMapaUtilitario_3_Map(HojaAnestesia.indicesSolicitudesCx[11]));
			//Fecha Salida Sala
			forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesSalidasSalaPaciente[5],forma.getMapaUtilitario_3_Map(HojaAnestesia.indicesSolicitudesCx[7]));
			//Hora Salida Sala
			forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesSalidasSalaPaciente[6],forma.getMapaUtilitario_3_Map(HojaAnestesia.indicesSolicitudesCx[8]));
			
			//Carga la informacion de los articulos incluidos 
			forma.setArrayArticuloIncluidoDto(RespuestaProcedimientos.cargarArticulosIncluidosSolicitudDto(con,forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString()));
			forma.setValidacionesMap("abrir_seccion_art_incluidos",ConstantesBD.acronimoSi);
			forma.setJustificacionMap(new HashMap());
			forma.setHiddens("");
		}
	}
	
	//******************************************************************************************
	
	/**
	 * Metodo que activa o desactiva la opcion fallece del paciente
	 * @param Connection con
	 * @param HojaAnestesiaForm forma
	 * @param HojaAnestesia mundo
	 * @param int institucion
	 * */
	private void metodoPacienteFallece(
			Connection con, 
			HojaAnestesiaForm forma,
			HojaAnestesia mundo,
			int institucion,
			int codigoPaciente)
	{
		boolean esPacienteMuerto = false; 
		
		//Valida si se selecciona la opcion fallece 
		if(forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesSalidasSalaPaciente[7]).toString().equals(ConstantesBD.acronimoSi))
		{
			esPacienteMuerto = true;
		}
		else
		{
			esPacienteMuerto = UtilidadValidacion.esPacienteMuerto(con,codigoPaciente);			
			//Fallece
			forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesSalidasSalaPaciente[7],ConstantesBD.acronimoNo);			
			//Diagnostico Fallece
			forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesSalidasSalaPaciente[10],"");
			//Fecha Fallece
			forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesSalidasSalaPaciente[8],"");
			//Hora Fallece
			forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesSalidasSalaPaciente[9],"");			
		}			
		
		//Consulta la informaci�n de las salidas del paciente 
		forma.setArrayListUtilitario_1(new ArrayList());			
		forma.setArrayListUtilitario_1(mundo.consultarSalidasPacienteInstCCosto(
				con, 
				institucion+"", 
				forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[0]+"_0").toString(),
				esPacienteMuerto));
	}
	
	//******************************************************************************************	
	
	//------------------------------------------------------------------------------------------------
	// Metodos Mostrar Sub Secciones *****************************************************************
	//------------------------------------------------------------------------------------------------
	
	/**
	 * Operaciones para el estado de mostrar SubSeccion Especialidades que Intervienen y Cirujanos Principales.
	 * @param Connection con
	 * @param HojaAnestesiaForm forma
	 * @param HojaAnestesia mundo
	 * */
	private void metodoEstadoMostrarSubSeccionEspeciInterCiru(Connection con,HojaAnestesiaForm forma, HojaAnestesia mundo)
	{		
		//Consultar la informacion de las especialidades
		forma.setEspecialidadesMap(Utilidades.obtenerEspecialidades());
		
		//Consultar la informacion de las especialidades existentes
		forma.setMapaUtilitario_1_Map(mundo.consultarEspecialidadesIntervienen(con,forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString(),""));
		
		//Inicializa la informacion de los medicos
		forma.setMapaUtilitario_2_Map(new HashMap());		
	}
	
	//******************************************************************************************
	
	/**
	 * Operaciones para el estado de mostrar SubSeccion Especialidades que Intervienen y Cirujanos Principales.
	 * @param Connection con
	 * @param HojaAnestesiaForm forma 
	 * @param HojaAnestesia mundo
	 * */
	private void metodoEstadoMostrarCirujanos(Connection con,HojaAnestesiaForm forma,HojaAnestesia mundo)
	{
		//consultar la informacion de los cirujanos
		forma.setMapaUtilitario_2_Map(mundo.consultarCirujanosIntervienen(
				con,
				forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString(), 
				forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesEspecialidadesInter[1]+forma.getIndicadorUtilitario()).toString()));
		
		//Obtienen el listado de medicos por la especialidad
		forma.setMapaUtilitarioBusquedas_1_Map(Utilidades.obtenerMedicosEspecialidad(forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesEspecialidadesInter[1]+forma.getIndicadorUtilitario()).toString()));
	}
	
	//******************************************************************************************

	/**
	 * Operaciones para el estado de mostrar SubSeccion Anestesiologos.
	 * @param Connection con
	 * @param HojaAnestesiaForm forma
	 * @param HojaAnestesia mundo
	 * @param UsuarioBasico usuario 
	 * */	
	private void metodoEstadoMostrarSubSeccionAnestesiologos(Connection con,HojaAnestesiaForm forma,HojaAnestesia mundo,UsuarioBasico usuario)
	{
		//Consulta la informacion de los anestesiologos existentes 
		forma.setMapaUtilitario_1_Map(mundo.consultarAnestesiologos(
				con, 
				forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString(),				 
				"",
				"",
				usuario,
				true));
		
		//Consulta la informacion de la Hoja de Anestesia
		forma.setMapaUtilitario_2_Map(mundo.consultarHojaAnestesia(
				con,
				forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString(),
				usuario.getCodigoInstitucionInt(),
				forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[14]+"_0").toString()));
	}
	
	//******************************************************************************************
	
	/**
	 * Operaciones para el estado de mostrar SubSeccion Fecha y Hora Ingreso a la Sala
	 * @param Connection con
	 * @param HojaAnestesiaForm forma
	 * @param HojaAnestesia mundo	
	 * */
	 private void metodoEstadoMostrarSubSeccionFechaHoraIng(
			 Connection con,
			 HojaAnestesiaForm forma,
			 HojaAnestesia mundo)
	 {		
		 forma.setMapaUtilitario_1_Map(mundo.consultarInfoSolicitudCx(con,forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString()));
		 
		 /*
		  * ESTA POSTULACION NO SE DEBE HACER PORQUE AL NO GUARDARLO EN LA BD SERIA REQUERIDA EN OTRAS SECCIONES, 
		  * Y AL ENTRAR EL USUARIO PENSARIA QUE YA INGRESO UNA FECHA Y HORA INGRESO SALA (SIENDO UNA POSTULACION), 
		  * if(!UtilidadFecha.esFechaValidaSegunAp(forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesSolicitudesCx[2])+""))
			 forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesSolicitudesCx[2], UtilidadFecha.getFechaActual());
		 if(!UtilidadFecha.validacionHora(forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesSolicitudesCx[3])+"").puedoSeguir)
			 forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesSolicitudesCx[3], UtilidadFecha.getHoraActual());*/
	 }
	
	//------------------------------------------------------------------------------------------------
	//Metodos Utilitarios*****************************************************************************
	//------------------------------------------------------------------------------------------------
	
	/**
	 * Incializa los datos de la solicitud
	 * @param Connection con
	 * @param HojaAnestesiaForm forma
	 * @param boolean infoBasica
	 * */
	private void iniciarSolicitudMap(Connection con,HojaAnestesiaForm forma,boolean esInfoBasica)
	{
		//Se carga la informacion de la solicitud basica
		if(esInfoBasica)
		{
			forma.setSolicitudMap("ccSolicitado0","");
			forma.setSolicitudMap("fechaSolicitud2",UtilidadFecha.getFechaActual());
			forma.setSolicitudMap("horaSolicitud3",UtilidadFecha.getHoraActual());
			forma.setSolicitudMap("especialidad4","");
			forma.setSolicitudMap("urgente5","");
		}
		//Se carga la informacion de la solicitus existente en la Base de Datos
		else
		{
			//Consula la informaci�n de la solicitud 
			HashMap parametros = new HashMap();
			parametros.put("peticion",forma.getListadoPeticionesMap(HojaQuirurgica.indicesPeticiones[2]+forma.getIndexPeticion()));
			forma.setSolicitudMap(HojaQuirurgica.consultaSolicitud(con,parametros));			
		}
	}	
	
	//******************************************************************************************
	
	//******************************************************************************************
	/**
	 * Inicializa la informaci�n de la SolicitudCX
	 * @param Connection con
	 * @param HojaAnestesiaForm forma
	 * @param HojaAnestesia mundo
	 * */
	private void inicializarInfoSolicitudCx(Connection con, HojaAnestesiaForm forma, HojaAnestesia mundo,int indicadorMapa)
	{		
		if(indicadorMapa == 3)
		{
			//Carga la informaci�n de la solicitud CX
			if(!forma.getMapaUtilitario_3_Map().containsKey(HojaAnestesia.indicesSolicitudesCx[0]))
				forma.setMapaUtilitario_3_Map(mundo.consultarInfoSolicitudCx(con,forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString()));
		}		
	}
	//******************************************************************************************
	
	/***
	 * Operaciones que se deben de realizar antes de guardar, modificar, actualizar o eliminar Informacion de la Base de Datos
	 * @param Connection con
	 * @param HttpServletRequest request
	 * @param UsuarioBasico usuario
	 * @param HojaAnestesia mundo
	 * */
	private boolean OperacionesAntesGuardar(Connection con,			
			HojaAnestesiaForm forma,
			UsuarioBasico usuario,
			HojaAnestesia mundo,
			String numeroSolicitud,
			int codigoMedico)
	{	
		//Verifica si Existe la Hoja de Anestesia, Si no exite la crea
				
		if(!mundo.crearHojaAnestesiaBasica(
			 con,
			 forma.getValidacionesMap("esCreadaHoja").toString(),
			 forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString(),
			 usuario.getCodigoInstitucionInt(),
			 forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[14]+"_0").toString(),
			 usuario.getCodigoPersona()+"",forma.isValidacionCapitacion()
			 ))
		 {	
			 return false;
		 }
		 else
			 forma.setValidacionesMap("esCreadaHoja",ConstantesBD.acronimoSi);
				
		//Registra el Anestesiologo indicando que realizo operaciones en la hoja de anestesia
		if(forma.getValidacionesMap("esCreadaHoja").toString().equals(ConstantesBD.acronimoSi))
		{
			if(mundo.consultarExisteAnestesiologo(
					con,
					forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString(), 
					usuario.getCodigoPersona()+""))
			{	
				if(!mundo.actualizarAnestesiologos(
						con,
						forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString(),
						usuario.getCodigoPersona()+""))
					return false;
			}
			else
			{			
				if(!mundo.insertarAnestesiologos(
						con, 
						forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString(),
						usuario.getCodigoPersona()+"",
						ConstantesBD.acronimoNo))
					return false;
			}
		}
				
		//Actualiza el estado de la Solicitud a Respondida
		if(!numeroSolicitud.equals("") && (codigoMedico > 0))
		{			
			if(!HojaQuirurgica.cambiarEstadoSolicitud(con,ConstantesBD.codigoEstadoHCRespondida+"",numeroSolicitud,codigoMedico+""))
				return false;			
		}	

		return true;
	}
	
	//******************************************************************************************
	
	/**
	 * Operaciones para el estado de mostrar otros medicamentos 
	 * @param Connection con
	 * @param HojaAnestesia mundo
	 * @param HojaAnestesiaForm forma
	 * */
	private void metodosEstadoMostrarOtrosMedicamentos(Connection con, HojaAnestesia mundo,HojaAnestesiaForm forma, int institucion, int codioSeccion)
	{	
		forma.setMapaUtilitario_1_Map("articulo","");
		forma.setMapaUtilitario_1_Map("descripcionArticulo","");
		
		
		if(forma.getEsBusquedaPorNombre().toString().equals("false"))
			forma.setMapaUtilitario_1_Map("articulo",forma.getCriterioBusqueda());
		else
			forma.setMapaUtilitario_1_Map("descripcionArticulo",forma.getCriterioBusqueda());			
						
		//Consulta la informaci�n de los medicamentos parametrizados por seccion y centro de costo
		forma.setArrayListUtilitario_1(mundo.consultarArticulosHojaAnestesia(
				con,   
				institucion, 
				Integer.parseInt(forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[0]+"_0").toString()),
				true,
				codioSeccion,
				forma.getMapaUtilitario_1_Map("articulo").toString(),
				forma.getMapaUtilitario_1_Map("descripcionArticulo").toString(),
				forma.getMapaUtilitario_1_Map("codigosArticulosInsertados").toString()
				)
		);
	}
	
	//******************************************************************************************
	/**
	 * Muestra las mezclas parametrizadas en el sistema
	 * @param Connection con
	 * @param HojaAnestesiaForm forma
	 * @param HojaAnestesia mundo 
	 * @param int institucion
	 * */
	private void metodosEstadoMostrarMezclas(Connection con, HojaAnestesiaForm forma,  HojaAnestesia mundo, int institucion)
	{
		forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[12],"");
		forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[3],"");		
		
		
		if(forma.getEsBusquedaPorNombre().toString().equals("false"))
			forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[12],forma.getCriterioBusqueda());
		else
			forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[3],forma.getCriterioBusqueda());		
		
		//Llena el Arraylist con el listado de mezclas
		forma.setArrayListUtilitario_1(mundo.consultarMezclas(
				con,
				institucion+"",
				forma.getMapaUtilitario_1_Map("codigosMezclaInsertados").toString(),
				forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[12]).toString(),
				forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[3]).toString(),
				Integer.parseInt(forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[0]+"_0").toString())				
				));	
		
	}
	//******************************************************************************************
	
	/**
	 * @param Connection con
	 * @param HojaAnetesia mundo
	 * @param HojaAnestesiaForm forma
	 * @param int codigoSeccion 
	 * */
	private void metodoEstadoMostrarDetalleMed(Connection con, HojaAnestesia mundo, HojaAnestesiaForm forma, int codigoSeccion)
	{
		//Carga la informacion del historial de los medicamentos administrados
		forma.setMapaUtilitario_2_Map(mundo.consultarDetAdminisHojaAnest(con, forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesMedAdminHojaAnes[0]+forma.getIndicadorUtilitario()).toString()));
		//Almacena la informacion del nombre del medicamento a mostrar el detalle 
		forma.setMapaUtilitario_2_Map(HojaAnestesia.indicesMedAdminHojaAnes[6],forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesMedAdminHojaAnes[6]+forma.getIndicadorUtilitario()).toString());
			
		if(codigoSeccion == ConstantesSeccionesParametrizables.seccionLiquidos)
		{
			//Descripcion del tipo de liquidos
			forma.setMapaUtilitario_2_Map(HojaAnestesia.indicesMedAdminHojaAnes[10],forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesMedAdminHojaAnes[10]+forma.getIndicadorUtilitario()).toString());
		}
	}
	
	//******************************************************************************************
	/**
	 * 	Metodo para mostrar las administraciones de la infusion
	 *  @param Connection con
	 *  @param HojaAnestesia forma
	 *  @param HojaAnestesia mundo
	 *  @param int institucion
	 * */
	private void metodosMostrarAdmInfusion(Connection con,HojaAnestesiaForm forma,HojaAnestesia mundo,int institucion)
	{
		//inicializa los codigos Insertados
		forma.setMapaUtilitario_2_Map("codigosArticulosInsertados","");
		
		//Evalua de donde proviene la infusi�n
		if(forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[11]+forma.getIndicadorUtilitario()).toString().equals(ConstantesBD.acronimoSi))
		{
			//Si se encuentra en la bases de datos
			if(forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[5]+forma.getIndicadorUtilitario()).toString().equals(ConstantesBD.acronimoSi))
			{
				forma.setMapaUtilitario_2_Map(mundo.consultarDetInfusionesHojaAnes(
						con, 
						"",
						forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[0]+forma.getIndicadorUtilitario()).toString(),						 
						"", 
						"", 
						"",
						false)
				);
			}
			else			
				forma.setMapaUtilitario_2_Map("numRegistros","0");			
		}
		else
		{
			//Consulta la informaci�n de los articulos contenidos en la mezcla
			forma.setMapaUtilitario_2_Map(mundo.consultarArticulosMezcla(
					con,
					forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[12]+forma.getIndicadorUtilitario()).toString()));
		}
	}	
	//******************************************************************************************
	
	/**
	 * Muestra la informacion del historial de las infusiones
	 * @param Connection con
	 * @param HojaAnestesiaForm forma
	 * @param HojaAnestesia mundo
	 * */
	public void metodoMostrarHistorialInfusion(Connection con,HojaAnestesiaForm forma,HojaAnestesia mundo)
	{
		//Consulta la informaci�n del detalle de las Infusiones
		forma.setMapaUtilitario_2_Map(mundo.consultarHistorialInfusiones(con,forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[0]+forma.getIndicadorUtilitario()).toString()));
	}
	
	//******************************************************************************************	

	/**
	 * Muestra la informacion de la seccion Balance de liquidos
	 * @param Connection con
	 * @param HojaAnestesiaForm forma
	 * @param HojaAnestesia mundo
	 * @param int institucion
	 * */
	private void metodoEstadoMostrarSeccionBalanceLiq(Connection con,HojaAnestesiaForm forma, HojaAnestesia mundo,int institucion)
	{
		//Captura la informaci�n de la seccion Balance Liquidos
		forma.setMapaUtilitario_1_Map((HashMap)mundo.consultarBalanceLiquidos(con,forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString(),true));
		
		//Captura la informaci�n de las secciones Homederivados y liquidos
		//este mapa se toma del mundo y se llena en el metodo consultarBalanceLiquidos
		forma.setMapaUtilitario_2_Map(mundo.getUtilitarioMap());
	}
	
	//******************************************************************************************
	
	/**
	 * Muestra la informacion de la busqueda generica de otros liquidos del balance de liquidos
	 * @param Connection con
	 * @param HojaAnestesiaForm forma
	 * @param HojaAnestesia mundo
	 * @param int institucion
	 * @param String descripcionLiquido
	 * */
	private void metodoEstadoMostrarOtrosLiquidos(
			Connection con,
			HojaAnestesiaForm forma,
			HojaAnestesia mundo, 
			int institucion	)
	{
		//Almacena la informaci�n del array 
		forma.setArrayListUtilitario_1(mundo.consultarOtrosLiquidosBalanceLiq(con, 
				institucion+"", 
				forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[0]+"_0").toString(),
				forma.getMapaUtilitario_1_Map("codigosInsertados").toString(),
				forma.getMapaUtilitario_1_Map("criterioBusqueda").toString()));		
	}
	
	//******************************************************************************************
	
	/**
	 * Inicializa los datos del mapa de especialidades
	 * @param HttpServletRequest request 
	 * @param HojaAnestesiaForm forma
	 * @param HojaAnestesia mundo
	 * */
	private void nuevaEspecialidadIntervieneMapa(Connection con,HttpServletRequest request,HojaAnestesiaForm forma,HojaAnestesia mundo, UsuarioBasico usuario)
	{
		//Validaciones para el ingreso de una nueva especialidad
		if(!mundo.validacionEspecialidadesIntervienen(forma.getMapaUtilitario_1_Map()))
			saveErrors(request,mundo.getErrores());		
		else
		{
			int numRegistros = Integer.parseInt(forma.getMapaUtilitario_1_Map("numRegistros").toString());			
			
			//Numero de la solicitud
			forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesEspecialidadesInter[0]+numRegistros,forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString());
			//Especialidad
			forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesEspecialidadesInter[1]+numRegistros,forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesEspecialidadesInter[5]));
			//nombre especialidad
			forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesEspecialidadesInter[2]+numRegistros,forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesEspecialidadesInter[6]));
			//asignada
			forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesEspecialidadesInter[3]+numRegistros,ConstantesBD.acronimoNo);
			//estabd
			forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesEspecialidadesInter[4]+numRegistros,ConstantesBD.acronimoNo);
			
			//Actualiza el numero de registros
			forma.setMapaUtilitario_1_Map("numRegistros",(numRegistros+1));		
		}
	}
	
	//******************************************************************************************
	
	/**
	 * Inicializa los datos del mapa de especialidades
	 * @param HttpServletRequest request 
	 * @param HojaAnestesiaForm forma
	 * @param HojaAnestesia mundo
	 * */
	private void nuevoCirujanoPrincipal(HttpServletRequest request,HojaAnestesiaForm forma,HojaAnestesia mundo)
	{
		//Validacione para el ingreso de una nuevo Cirujano por la especialidad
		if(!mundo.validacionCirujanosPrincipales(forma.getMapaUtilitario_2_Map()))
			saveErrors(request,mundo.getErrores());		
		else
		{
			int numRegistros = Integer.parseInt(forma.getMapaUtilitario_2_Map("numRegistros").toString());
			
			forma.setMapaUtilitario_2_Map(HojaAnestesia.indicesCirujanosPrincipales[0]+numRegistros,"");
			//numero solicitud
			forma.setMapaUtilitario_2_Map(HojaAnestesia.indicesCirujanosPrincipales[1]+numRegistros,forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString());
			//especialidad
			forma.setMapaUtilitario_2_Map(HojaAnestesia.indicesCirujanosPrincipales[2]+numRegistros,forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesEspecialidadesInter[1]+forma.getIndicadorUtilitario()));
			//Nombre Especialidad
			forma.setMapaUtilitario_2_Map(HojaAnestesia.indicesCirujanosPrincipales[3]+numRegistros,forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesEspecialidadesInter[2]+forma.getIndicadorUtilitario()));
			//profesional
			forma.setMapaUtilitario_2_Map(HojaAnestesia.indicesCirujanosPrincipales[4]+numRegistros,forma.getMapaUtilitario_2_Map(HojaAnestesia.indicesCirujanosPrincipales[7]));
			//nombre profesional
			forma.setMapaUtilitario_2_Map(HojaAnestesia.indicesCirujanosPrincipales[5]+numRegistros,forma.getMapaUtilitario_2_Map(HojaAnestesia.indicesCirujanosPrincipales[8]));
			//asignada
			forma.setMapaUtilitario_2_Map(HojaAnestesia.indicesCirujanosPrincipales[6]+numRegistros,ConstantesBD.acronimoNo);
			//estabd
			forma.setMapaUtilitario_2_Map(HojaAnestesia.indicesCirujanosPrincipales[9]+numRegistros,ConstantesBD.acronimoNo);
						
			//Actualiza el numero de registros
			forma.setMapaUtilitario_2_Map("numRegistros",(numRegistros+1));					
		}	
		
		forma.setEstado("mostrarCirujanos");
	}
	 
	//******************************************************************************************
	
	/**
	 * Inserta un nuevo registro en el mapa para la seccion de anestesicos y medicamentos suministrados
	 * @param HojaAnestesiaForm forma
	 * @param int codigoSeccion
	 * */
	private void nuevoMedicamentoAnestesicoSuministradoMap(HojaAnestesiaForm forma, int codigoSeccion)
	{
		int numRegistros = Integer.parseInt(forma.getMapaUtilitario_1_Map("numRegistros").toString());
		String articulosInsertados = "";
		
		//Numero de Solicitud 
		forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesMedAdminHojaAnes[1],forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString());
		//Codigo Articulo
		forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesMedAdminHojaAnes[2]+numRegistros,forma.getMapaUtilitario_1_Map("articulo"));		
		
		//Descripcion Articulos
		forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesMedAdminHojaAnes[6]+numRegistros,forma.getMapaUtilitario_1_Map("descripcionArticulo"));
		//estabd
		forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesMedAdminHojaAnes[7]+numRegistros,ConstantesBD.acronimoNo);
	
		//Dosis
		forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesDetMedAdminHojaAnes[2]+numRegistros,"");
		//Fecha
		forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesDetMedAdminHojaAnes[3]+numRegistros,UtilidadFecha.getFechaActual());
		//Hora
		forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesDetMedAdminHojaAnes[4]+numRegistros,UtilidadFecha.getHoraActual());
		//Graficar
		forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesDetMedAdminHojaAnes[5]+numRegistros,ConstantesBD.acronimoNo);
		//Suministrar
		forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesMedAdminHojaAnes[8]+numRegistros,ConstantesBD.acronimoNo);
		//Genero Consumo
		forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesDetMedAdminHojaAnes[6]+numRegistros,ConstantesBD.acronimoNo);
		//Otro Medicamento
		forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesDetMedAdminHojaAnes[7]+numRegistros,forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesMedAdminHojaAnes[3]));
		//Tipo de Liquidos
		forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesMedAdminHojaAnes[9]+numRegistros,"");
		
		//Llenado de valores dependiendo del codigo de la seccion
		if(codigoSeccion == ConstantesSeccionesParametrizables.seccionLiquidos)
		{
			//Tipo de Liquidos
			forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesMedAdminHojaAnes[9]+numRegistros,((HashMap)forma.getArrayListUtilitario_1().get(Integer.parseInt(forma.getIndicadorUtilitario()))).get("tipoLiquido"));
			//Descripci�n de Tipo de Liquidos
			forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesMedAdminHojaAnes[10]+numRegistros,((HashMap)forma.getArrayListUtilitario_1().get(Integer.parseInt(forma.getIndicadorUtilitario()))).get("descripcionLiquido"));
		}
		
		
		//Llenado de valores dependiendo del codigo de la seccion
		if(codigoSeccion == ConstantesSeccionesParametrizables.seccionAnestesicosMedicaAdminis || 
				codigoSeccion == ConstantesSeccionesParametrizables.seccionLiquidos)
		{
		
			//Tipo Pos Articulos
			forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesMedAdminHojaAnes[11]+numRegistros,forma.getMapaUtilitario_1_Map("espos"));
			
			//Justificacion Articulos
			forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesMedAdminHojaAnes[12]+numRegistros,forma.getMapaUtilitario_1_Map("tienejus"));
		}
		
		forma.setMapaUtilitario_1_Map("numRegistros",numRegistros+1);
		
		articulosInsertados = forma.getMapaUtilitario_1_Map("codigosArticulosInsertados").toString();
		articulosInsertados+= forma.getMapaUtilitario_1_Map("articulo").toString()+",";
		forma.setMapaUtilitario_1_Map("codigosArticulosInsertados",articulosInsertados);		
	}
	
	//******************************************************************************************
	
	/**
	 * Inserta una nueva infusion en el mapa de Infusiones
	 * @param HojaAnestesiaForm forma
	 * @param HojaAnestesia mundo
	 * */
	private boolean nuevaInfusion(HojaAnestesiaForm forma,HttpServletRequest request,HojaAnestesia mundo)
	{
		int numRegistros = Integer.parseInt(forma.getMapaUtilitario_1_Map("numRegistros").toString());
		String articulosInsertados = "";
		
		//-----------------		
		//Si la mezcla es ingresada por el usuario, actualiza el indicador de hayOtraInfusion		
		if(forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[11]).toString().equals(ConstantesBD.acronimoSi))
		{	
			if(mundo.validacionesExisteOtraMezcla(
					forma.getMapaUtilitario_1_Map(),
					forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[3]).toString()))
			{
				saveErrors(request,mundo.getErrores());
				return false;
			}
		}
		//-----------------
		
		
		//Codigo
		forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[0]+numRegistros,"");
		//Numero Solicitud 
		forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[1]+numRegistros,forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString());
		//Mezcla
		forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[12]+numRegistros,forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[12]));
		
		//Descripcion de la mezcla
		forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[3]+numRegistros,forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[3]));
		forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[4]+numRegistros,"");
		
		//Indicador de otra infusion
		forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[11]+numRegistros,forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[11]));
		
		//Si la mezcla es ingresada por el usuario, actualiza el indicador de hayOtraInfusion		
		if(forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[11]).toString().equals(ConstantesBD.acronimoSi))
		{
			forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[10],ConstantesBD.acronimoSi);
			
			//Descripcion otra mezcla
			forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[4]+numRegistros,forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[3]).toString().toUpperCase());
			//Descripcion de la mezcla
			forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[3]+numRegistros,"");
		}
			
		//estabd
		forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[5]+numRegistros,ConstantesBD.acronimoNo);
		//Graficar
		forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[6]+numRegistros,ConstantesBD.acronimoNo);
		//Suspender
		forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[7]+numRegistros,ConstantesBD.acronimoNo);
		
		//Fecha
		forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesAdmInfusionesHojaAnes[2]+numRegistros,UtilidadFecha.getFechaActual());
		//Hora
		forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesAdmInfusionesHojaAnes[3]+numRegistros,UtilidadFecha.getHoraActual());				
		
		forma.setMapaUtilitario_1_Map("numRegistros",numRegistros+1);
		
		if(forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[11]).toString().equals(ConstantesBD.acronimoNo))
		{
			articulosInsertados = forma.getMapaUtilitario_1_Map("codigosMezclaInsertados").toString();
			articulosInsertados+= forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesInfusionesHojaAnes[12]).toString()+",";	
			forma.setMapaUtilitario_1_Map("codigosMezclaInsertados",articulosInsertados);
		}
		
		return true;
	}
	
	//******************************************************************************************
	
	/**
	 * Ingresa un nuevo articulo a la administracion
	 * @param HojaAnestesiaForm forma
	 * */
	public void nuevoArticuloAdmInfusionMap(HojaAnestesiaForm forma)
	{
		String articulosInsertados = "";
		int numRegistros = Integer.parseInt(forma.getMapaUtilitario_2_Map("numRegistros").toString());
		
		forma.setMapaUtilitario_2_Map(HojaAnestesia.indicesDetAdmInfusionesHojaAnes[2]+numRegistros,forma.getMapaUtilitario_2_Map("articulo"));
		forma.setMapaUtilitario_2_Map(HojaAnestesia.indicesDetAdmInfusionesHojaAnes[3]+numRegistros,forma.getMapaUtilitario_2_Map("descripcionArticulo"));
		forma.setMapaUtilitario_2_Map(HojaAnestesia.indicesDetAdmInfusionesHojaAnes[4]+numRegistros,"");
		forma.setMapaUtilitario_2_Map(HojaAnestesia.indicesDetAdmInfusionesHojaAnes[5]+numRegistros,ConstantesBD.acronimoNo);
		forma.setMapaUtilitario_2_Map(HojaAnestesia.indicesDetAdmInfusionesHojaAnes[9]+numRegistros,forma.getMapaUtilitario_2_Map("espos"));
		
		articulosInsertados = forma.getMapaUtilitario_2_Map("codigosArticulosInsertados").toString();
		articulosInsertados+= forma.getMapaUtilitario_2_Map(HojaAnestesia.indicesDetAdmInfusionesHojaAnes[2]+numRegistros).toString()+",";	
		forma.setMapaUtilitario_2_Map("codigosArticulosInsertados",articulosInsertados);		
		
		forma.setMapaUtilitario_2_Map("numRegistros",numRegistros+1);
	}
	
	//******************************************************************************************
	
	/**
	 * Ingresa un nuevo articulo a la administracion
	 * @param HojaAnestesiaForm forma
	 * */
	public void nuevoOtroLiquidoBalanceMap(HojaAnestesiaForm forma)
	{
		String codigosInsertados = "";
		int numRegistros = Integer.parseInt(forma.getMapaUtilitario_1_Map("numRegistros").toString());
		
		forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesBalancesLiquidosHojaAnes[0]+numRegistros,forma.getSolicitudMap(HojaQuirurgica.indicesSolicitud[8]+"_0").toString());
		forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesBalancesLiquidosHojaAnes[1]+numRegistros,forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesBalancesLiquidosHojaAnes[1]));
		forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesBalancesLiquidosHojaAnes[2]+numRegistros,"");
		forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesBalancesLiquidosHojaAnes[3]+numRegistros,forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesBalancesLiquidosHojaAnes[3]));		
		forma.setMapaUtilitario_1_Map(HojaAnestesia.indicesBalancesLiquidosHojaAnes[4]+numRegistros,ConstantesBD.acronimoNo);
		
		codigosInsertados = forma.getMapaUtilitario_1_Map("codigosInsertados").toString();
		codigosInsertados+= forma.getMapaUtilitario_1_Map(HojaAnestesia.indicesBalancesLiquidosHojaAnes[1]+numRegistros).toString()+",";	
		forma.setMapaUtilitario_1_Map("codigosInsertados",codigosInsertados);		
		
		forma.setMapaUtilitario_1_Map("numRegistros",numRegistros+1);

		//Se captura el nuevo valor del total de secciones
		 forma.setMapaUtilitario_1_Map("valorTotal",Integer.parseInt(forma.getMapaUtilitario_2_Map("totalSecciones").toString())-HojaAnestesia.getTotalCampo(forma.getMapaUtilitario_1_Map(),HojaAnestesia.indicesBalancesLiquidosHojaAnes[2]));
	}
	
		
	//**************************************************************************************************
	
	/**
	 * Obtener Datos Request
	 * @param HojaAnestesiaForm forma
	 * @param HttpServletRequest request
	 * */
	public void obtenerDatosRequest (HojaAnestesiaForm forma, HttpServletRequest request)
	{
		//los valores enviados por request
		//---------------------------------------------------------------
		//1) esDummy 
		forma.setEsDummy(UtilidadTexto.getBoolean(request.getParameter("esDummy")+""));
		//2) peticion
		if (!(request.getParameter("peticion")+"").equals("") && !(request.getParameter("peticion")+"").equals("null"))
			forma.setPeticion(request.getParameter("peticion")+"");
		//3)funcionalidad
		if (!(request.getParameter("funcionalidad")+"").equals("") && !(request.getParameter("funcionalidad")+"").equals("null"))
			forma.setFuncionalidad(request.getParameter("funcionalidad")+"");
		//4)codigoCita
		if (!(request.getParameter("codigoCita")+"").equals("") && !(request.getParameter("codigoCita")+"").equals("null"))
			forma.setCodigoCita(request.getParameter("codigoCita")+"");
		//5)esModificable
		if (!(request.getParameter("esModificableDummy")+"").equals("") && !(request.getParameter("esModificableDummy")+"").equals("null"))		
			forma.setEsModificableDummy(UtilidadTexto.getBoolean(request.getParameter("esModificableDummy")+""));
		//6)ocultarEncabezado
		if (!(request.getParameter("ocultarEncabezado")+"").equals("") && !(request.getParameter("ocultarEncabezado")+"").equals("null"))
			forma.setOcultarEncabezado(UtilidadTexto.getBoolean(request.getParameter("ocultarEncabezado")+""));
		//7)ocultarEncabezado
		if (!(request.getParameter("ocultarMenu")+"").equals("") && !(request.getParameter("ocultarMenu")+"").equals("null"))
			forma.setOcultarMenu(UtilidadTexto.getBoolean(request.getParameter("ocultarEncabezado")+""));
		//8)Codigo Paciente
		forma.setCodigoPaciente(request.getParameter("codigoPaciente")!=null?request.getParameter("codigoPaciente"):""); 
		
		
		

		// Anexo 179 - Cambio 1.50
		String tieneAutoIEstr = request.getParameter("tieneAutoIE");
		if(!UtilidadTexto.isEmpty(tieneAutoIEstr))
		{
			boolean tieneAutoIE = UtilidadTexto.getBoolean(tieneAutoIEstr);
			
			if(tieneAutoIE)
			{
				forma.setSinAutorizacionEntidadsubcontratada(false);
			}
			else
			{
				forma.setSinAutorizacionEntidadsubcontratada(true);
				
				String mensajeConcreto = fuenteMensaje.getMessage("AutorizacionesIngresoEstanciaForm.ordenSinAutorizacionIE");
				
				if(Utilidades.isEmpty(forma.getListaAdvertencias())){
					forma.setListaAdvertencias(new ArrayList<String>());
				}
				forma.getListaAdvertencias().add(mensajeConcreto);
			}
		}
		//---------------------------------------------------------------
	}
	
	
	
	
	/**
	 * Se encarga de mostrar el mensaje luego de evaluar la variable que indica si tiene Autorizaci�n de Ingreso Estancia.
	 * Se hace en un m�todo aparte para conservar el mensaje  a�n cuando se cambie de secci�n a mostrar en la presentaci�n.
	 * @author Cristhian Murillo
	 * @param forma
	 */
	private void llenarMensajeSinAutorizacionIE(HojaAnestesiaForm forma)
	{
		if(forma.isSinAutorizacionEntidadsubcontratada())
		{
			forma.setListaAdvertencias(new ArrayList<String>());
			
			String mensajeConcreto = fuenteMensaje.getMessage("AutorizacionesIngresoEstanciaForm.ordenSinAutorizacionIE");
			
			forma.getListaAdvertencias().add(mensajeConcreto);
		}
	}
	
	
	
}