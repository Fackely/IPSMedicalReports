/**
 * 
 */
package com.princetonsa.action.odontologia;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadEmail;
import util.UtilidadFecha;
import util.Utilidades;
import util.Administracion.UtilidadesAdministracion;
import util.consultaExterna.UtilidadesConsultaExterna;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.odontologia.CancelarAgendaOdontoForm;
import com.princetonsa.dto.administracion.DtoEnvioEmailAutomatico;
import com.princetonsa.dto.odontologia.DtoAgendaOdontologica;
import com.princetonsa.dto.odontologia.DtoCitaOdontologica;
import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;
import com.princetonsa.dto.odontologia.DtoServicioCitaOdontologica;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.administracion.EnvioEmailAutomatico;
import com.princetonsa.mundo.cargos.CargosOdon;
import com.princetonsa.mundo.odontologia.CancelarAgendaOdonto;
import com.princetonsa.mundo.odontologia.CitaOdontologica;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.LogEnvioEmailAutomatico;
import com.servinte.axioma.orm.LogEnvioEmailAutomaticoHome;

/**
 * @author axioma
 *
 */
public class CancelarAgendaOdontoAction extends Action{
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	Logger logger = Logger.getLogger(CancelarAgendaOdontoAction.class);
	
	/**
	 * Mï¿½todo execute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Connection connection = null;
		try{
		if (form instanceof CancelarAgendaOdontoForm) {

			
			connection = UtilidadBD.abrirConexion();

			// se verifica si la conexion esta nula
			if (connection == null) {
				// de ser asi se envia a una pagina de error.
				request.setAttribute("CodigoDescripcionError", "erros.problemasBd");
				return mapping.findForward("paginaError");
			}

			// se obtiene el usuario cargado en sesion.
			UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");

			// obtenemos el valor de la forma.
			CancelarAgendaOdontoForm forma = (CancelarAgendaOdontoForm) form;
			
//			instanciamos el mundo
			CancelarAgendaOdonto mundo = new CancelarAgendaOdonto();

			// obtenemos el estado que contiene la forma.
			String estado = forma.getEstado();

			Log4JManager.info("\n\n***************************************************************************");
			Log4JManager.info("EL ESTADO DE CancelarAgendaOdontoAction ES ====>> " + forma.getEstado());
			Log4JManager.info("\n***************************************************************************");

			/*
			 * estados
			 */

			if (estado == null) {
				// se asigana un error a mostar por ser un estado invalido
				request.setAttribute("CodigoDescripcionError", "errors.estadoInvalido");
				// se cierra la conexion con la BD
				UtilidadBD.cerrarConexion(connection);

				// se retorna el error al forward paginaError.
				return mapping.findForward("paginaError");
			} else {
				
				/*
				 * empezar
				 */
				if (estado.equals("empezar")){
					forma.reset();
					return accionEmpezar(connection, mapping, usuario, forma,request);
				}else if (estado.equals("filtrarUnidadesAgenda")) {
					return accionFiltrarUnidadesAgenda(connection, forma, usuario, response);
				}else if (estado.equals("filtrarConsultorios")) {
					return accionFiltrarConsultorios(connection, forma, usuario, response);
				}else if (estado.equals("validaCancelarAgenda")) {
					cierraConexion(connection);
					return mapping.findForward("principal");
				}else if (estado.equals("listarAgenda")) {
					return accionListarAgenda(connection, forma, mundo, mapping);
				}else if (estado.equals("volver")) {
					cierraConexion(connection);
					return mapping.findForward("volver");
				}else if (estado.equals("ordenar")) {
					return accionOrdenar(connection, mapping, forma, usuario);
				}else if (estado.equals("cancelarAgenda")) {
					return accionCancelarAgenda(connection, mapping, forma, usuario, mundo, request);
				}else if (estado.equals("redireccion")) {
					response.sendRedirect(forma.getLinkSiguiente());
					cierraConexion(connection);
					return null;
				}else if (estado.equals("generarReporte")) {
					return accionGenerarReporte(connection, forma, mapping, usuario, request);
				}
			}
		}
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(connection);
		}
		return null;
	}
	
	/**
	 * Mï¿½todo que realiza la cancelaciï¿½n de agenda
	 * @param connection
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param mundo
	 * @param request 
	 * @return
	 */
	private ActionForward accionCancelarAgenda(Connection connection,
			ActionMapping mapping, CancelarAgendaOdontoForm forma,
			UsuarioBasico usuario, CancelarAgendaOdonto mundo, HttpServletRequest request) throws IPSException {
		
		ActionErrors errores = new ActionErrors();
		DtoInfoFechaUsuario fechaUsuario = new DtoInfoFechaUsuario(usuario.getLoginUsuario());
		forma.setNoItemCancelados(0);
		int codigoPkCancAgen=0;
		realizarConsultaAgenda(connection, forma, mundo);
 		boolean transaction = UtilidadBD.iniciarTransaccion(connection);
 		DtoAgendaOdontologica dtoAgenda= new DtoAgendaOdontologica();
 		if (transaction && forma.getListAgenda().size()>0){		
//			se itera la lista de agenda consultada
			int registrosCancelados = 0;
			
			ArrayList<String> agendasEliminadas = new ArrayList<String>();
			Iterator<DtoAgendaOdontologica> iterator = forma.getListAgenda().listIterator();
			while(iterator.hasNext()){
				dtoAgenda = iterator.next();
				dtoAgenda.setFechaModifica(fechaUsuario.getFechaModifica());
				dtoAgenda.setHoraModifica(fechaUsuario.getHoraModifica());
				dtoAgenda.setUsuarioModifica(fechaUsuario.getUsuarioModifica());
				boolean eliminaRegistroAgenda = true;
				transaction = mundo.actualizarActivoAgenda(connection, dtoAgenda.getCodigoPk());
				if(transaction){
					ResultadoBoolean rb = mundo.insertarCancelaAgendaOdo(connection, dtoAgenda);
					transaction = rb.isTrue();
					if(transaction){
						registrosCancelados++;
						ArrayList<DtoCitaOdontologica> listCita =  CitaOdontologica.consultarCitaOdontologicaXAgenda(connection, dtoAgenda.getCodigoPk(),usuario.getCodigoInstitucionInt());
						Log4JManager.info("tamanio listi cita::"+listCita.size());
						codigoPkCancAgen=Utilidades.convertirAEntero(rb.getDescripcion());
						for(DtoCitaOdontologica dtoCita: listCita){
							Log4JManager.info("codigopk cita::"+dtoCita.getCodigoPk());
							
							eliminaRegistroAgenda = false;
							agendasEliminadas.add(dtoCita.getCodigoPk()+"");


							Log4JManager.info("estado cita::"+dtoCita.getEstado());
		//						cancelacion de citas
								if(dtoCita.getEstado().equals(ConstantesIntegridadDominio.acronimoAsignado)){
									Log4JManager.info("cita estado = asignada");
									Log4JManager.info("tamanio list servicios:::"+dtoCita.getServiciosCitaOdon().size());
									for(DtoServicioCitaOdontologica elem: dtoCita.getServiciosCitaOdon()){
										Log4JManager.info("recorro mis servicios");
										if(elem.getFacturado().equals(ConstantesBD.acronimoNo)){
											Log4JManager.info("si el servicio se encuentra facturado");
											if(!Solicitud.cambiarEstadosSolicitudStatico(connection, elem.getNumeroSolicitud(), 
													ConstantesBD.codigoEstadoFAnulada,  ConstantesBD.codigoEstadoHCAnulada).isTrue())
											{
												forma.setMensaje(new ResultadoBoolean(true, "SE HA PRESENTADO UN INCONVENIENTE ANULANDO LAS SOLICITUDES. POR FAVOR VERIFIQUE."));
												Log4JManager.info("se aborta la transaccion por errores en la anulacion de solicitudes");
												UtilidadBD.abortarTransaccion(connection);
												transaction = false;
												break;
											}
											elem.setEliminarSer(ConstantesBD.acronimoSi);
										}
										Log4JManager.info("entreeee"+elem.getCodigoPk());
//										log de citas por agenda
										transaction = mundo.insertardetalleCancelacionAgenOdo(connection, dtoCita, Utilidades.convertirAEntero(rb.getDescripcion()),elem.getNumeroSolicitud());
										if (!transaction){
											Log4JManager.info("no c inserto el detalle q ....");
											forma.setMensaje(new ResultadoBoolean(true, "SE HA PRESENTADO UN INCONVENIENTE GUARDANDO EL DETALLE DE LA AGENDA. POR FAVOR VERIFIQUE."));
											Log4JManager.info("error en la insercion del detalle cancelacion agenda odo");
											UtilidadBD.abortarTransaccion(connection);
											saveErrors(request, errores);
											break;
										}
									}
									Log4JManager.info("se verifican los saldos");
									// verificar control de saldos
									CargosOdon.validacionControlAbonos(connection, dtoCita.getServiciosCitaOdon(),
											new BigDecimal(ConstantesBD.codigoNuncaValido), false, errores, usuario, 
											dtoCita.getCodigoPk(), ConstantesBD.codigoNuncaValido);
									Log4JManager.info("saldos verificados");
									if(errores.isEmpty())
									{
										transaction = true;
									}else{
										forma.setMensaje(new ResultadoBoolean(true, "SE HA PRESENTADO UN INCONVENIENTE ELIMINANDO LOS ABONOS. POR FAVOR VERIFIQUE."));
										Log4JManager.info("se aborta la transaccion por errores en la eliminacion abonos");
										UtilidadBD.abortarTransaccion(connection);
										transaction = false;
									}
								}else
								{ 
							     if(dtoCita.getEstado().equals(ConstantesIntegridadDominio.acronimoReservado))
							     {
									Log4JManager.info("esta es una cita reservada");
									Log4JManager.info("tamanio list servicios:::"+dtoCita.getServiciosCitaOdon().size());									
									transaction = mundo.insertardetalleCancelacionAgenOdo(connection, dtoCita, Utilidades.convertirAEntero(rb.getDescripcion()),ConstantesBD.codigoNuncaValido);
									if (!transaction){
										Log4JManager.info("no c inserto el detalle q .... pero ahora reservada");
										forma.setMensaje(new ResultadoBoolean(true, "SE HA PRESENTADO UN INCONVENIENTE GUARDANDO EL DETALLE DE LA AGENDA. POR FAVOR VERIFIQUE."));
										Log4JManager.info("error en la insercion del detalle cancelacion agenda odo");
										UtilidadBD.abortarTransaccion(connection);
										saveErrors(request, errores);
										break;
									}
									for(DtoServicioCitaOdontologica elem: dtoCita.getServiciosCitaOdon())
									{
										Log4JManager.info("entreee por reservadas");
										
										
										elem.setEliminarSer(ConstantesBD.acronimoSi);
									}
							      }
								}
								
								if(transaction)
								{
									// actualizacion de los datos de la cita que se reprograma
									if(!dtoCita.getEstado().equals(ConstantesIntegridadDominio.acronimoAtendida))
								     {
										dtoCita.setEstado(ConstantesIntegridadDominio.acronimoAreprogramar);
										dtoCita.setFechaProgramacion("");
										
										dtoCita.setUsuarioModifica(usuario.getLoginUsuario());
										
										if(!CitaOdontologica.cambiarSerOdontologicos(connection, dtoCita))
										{
											transaction = false;
											forma.setMensaje(new ResultadoBoolean(true, "SE HA PRESENTADO UN INCONVENIENTE GENERANDO LOGS Y ACTUALIZACIONES. POR FAVOR VERIFIQUE."));
											Log4JManager.info("erroer en la creacion de los logs y las actualizaciones");
											UtilidadBD.abortarTransaccion(connection);
											saveErrors(request, errores);
										}
								     }
								}else{
									UtilidadBD.abortarTransaccion(connection);
									break;
								}
						}
						if (eliminaRegistroAgenda){
							if(mundo.consultarNoCitasXAgenda(connection, dtoAgenda.getCodigoPk()) <= 0){
								transaction = mundo.eliminarAgendaOdontologica(connection, dtoAgenda.getCodigoPk());
								if(!transaction){
									forma.setMensaje(new ResultadoBoolean(true, "NO SE HA PODIDO LLEVAR A CABO LA ELIMINACION DE LA AGENDA. POR FAVOR VERIFIQUE."));
									UtilidadBD.abortarTransaccion(connection);
									break;
								}
							}
						}
					}
					else{
						forma.setMensaje(new ResultadoBoolean(true, "NO SE HA PODIDO LLEVAR A CABO LA INSERCION DE LA CANCELACION DE AGENDA. POR FAVOR VERIFIQUE."));
						UtilidadBD.abortarTransaccion(connection);
						break;
					}
				}
				else{
					forma.setMensaje(new ResultadoBoolean(true, "NO SE HA PODIDO LLEVAR A CABO LA ACTUALIZACION DE LA AGENDA. POR FAVOR VERIFIQUE."));
					UtilidadBD.abortarTransaccion(connection);
					break;
				}
			}
			if(transaction)
				forma.setNoItemCancelados(registrosCancelados);
			forma.setListItemCancelados(agendasEliminadas);
		}
		if(errores.isEmpty())
		{
			UtilidadBD.finalizarTransaccion(connection);
			ArrayList<DtoEnvioEmailAutomatico> listaDtoEnvioAuto=EnvioEmailAutomatico.listar(connection, usuario.getCodigoInstitucionInt());
			int n=listaDtoEnvioAuto.size();			
			for(int i=0;i<n;i++)
			{					
				if((listaDtoEnvioAuto.get(i).getFuncionalidad()).equals(ConstantesIntegridadDominio.funcionalidadCancelacionAgendaOdontologica))
				{	
					forma.setListaCancelacionAgenda(mundo.consultarCancelacionAgendaOdonto(codigoPkCancAgen));

					int numReg=0;
					
					if(forma.getListaCancelacionAgenda().size() > 0)
						numReg=forma.getListaCancelacionAgenda().size();

					String contenido="";
					
					if(numReg > 0)
					{
					
						contenido+="<tr>" +
											"<td>" +
												"<table width=\"100%\" border=\"0\" cellpadding=\"4\" cellspacing=\"1\" bgcolor=\"#006898\">" +
													"<tr bgcolor=\"#FFFFFF\" align=\"center\">" +
														"<td>Fecha Cancelacion:</td>" +
														"<td>"+forma.getListaCancelacionAgenda().get(0).getFechaModificacion()+"</td>" +
														"<td>Hora Cancelacion:</td>" +
														"<td>"+forma.getListaCancelacionAgenda().get(0).getHoraModificacion()+"</td>" +											
													"</tr>" +
													"<tr bgcolor=\"#FFFFFF\" align=\"center\">" +
														"<td>Fecha Inicio -  Agenda Cancelada:</td>" +
														"<td>"+forma.getDatosBusqueda().getFechaInicial()+"</td>" +
														"<td>Fecha Fin -  Agenda Cancelada:</td>" +
														"<td>"+forma.getDatosBusqueda().getFechaFinal()+"</td>" +
													"</tr>" +
													"<tr bgcolor=\"#FFFFFF\" align=\"center\">" +
														"<td colspan=\"2\">Numero de Items de Agenda Cancelados:</td>" +
														"<td colspan=\"2\">"+forma.getNoItemCancelados()+"</td>" +
													"</tr>" +
												 "</table>" +
											"</td>" +
										"</tr><br>";
					
							contenido +="<tr>" +
											"<td>" +
												"<table width=\"100%\" border=\"0\" cellpadding=\"4\" cellspacing=\"1\" bgcolor=\"#006898\">" +
												 	"<tr bgcolor=\"#FFFFFF\">" +
												 		"<td colspan=\"8\">AGENDA CANCELADA</td>" +
												 	"</tr>" +
												 "</table><br>" +
												 "<table width=\"100%\" border=\"0\" cellpadding=\"4\" cellspacing=\"1\" bgcolor=\"#006898\">" +
												 	"<tr bgcolor=\"#FFFFFF\">" +
												 		"<td width=\"10%\" align=\"center\">Centro de Atencion</td>" +
												 		"<td width=\"50%\">"+forma.getListaCancelacionAgenda().get(0).getNombreCentroAtencion()+"</td>" +
												 	"</tr>" +
												 "</table><br>" +
												 "<table width=\"100%\" border=\"0\" cellpadding=\"4\" cellspacing=\"1\" bgcolor=\"#006898\">" +
												 	"<tr bgcolor=\"#FFFFFF\" align=\"center\">" +
											 			"<td> Fecha</td>" +
												 		"<td> Hora</td>" +
												 		"<td> Unidad de Consulta</td>" +
												 		"<td> Profesional de la Salud</td>" +
												 		"<td> Consultorio</td>" +
												 		"<td> No ID</td>" +
												 		"<td> Paciente</td>" +
												 		"<td> Telefono</td>" +
												 	"</tr>";					
						
						for(int j=0;j<forma.getListaCancelacionAgenda().get(0).getDetCancelacionAgendaOdo().size();j++)
						{
							contenido += "<tr bgcolor=\"#FFFFFF\" align=\"center\">" +
												"<td>"+forma.getListaCancelacionAgenda().get(0).getDetCancelacionAgendaOdo().get(j).getFecha()+"</td>" +
							    				"<td>"+forma.getListaCancelacionAgenda().get(0).getDetCancelacionAgendaOdo().get(j).getHora()+"</td>" +
							    				"<td>"+forma.getListaCancelacionAgenda().get(0).getNombreUnidadAgenda()+"</td>" +
							    				"<td>"+forma.getListaCancelacionAgenda().get(0).getNombreMedico()+"</td>" +
							    				"<td>"+forma.getListaCancelacionAgenda().get(0).getNombreConsultorio()+"</td>" +
							    				"<td>"+forma.getListaCancelacionAgenda().get(0).getDetCancelacionAgendaOdo().get(j).getIdPaciente()+"</td>" +
							    				"<td>"+forma.getListaCancelacionAgenda().get(0).getDetCancelacionAgendaOdo().get(j).getNomPaciente()+"</td>" +
							    				"<td>"+forma.getListaCancelacionAgenda().get(0).getDetCancelacionAgendaOdo().get(j).getTelefonoPaciente()+"</td>" +
						    			  "</tr>"; 
						}	        				
						
						contenido += "</table>" +
								"</td>" +
							"</tr>";
						
						LogEnvioEmailAutomatico envioMail = new LogEnvioEmailAutomatico();
						String estadito;
						try{
							UtilidadEmail.enviarCorreo(
									"adminscso@servinte.com.co", 
									listaDtoEnvioAuto.get(i).getEmailUsuario(), 
									contenido,
									"Correo Automático- Cancelación de Agenda Odontológica - "+
										forma.getListaCancelacionAgenda().get(0).getUsuarioModificacion()+ " - " +
										forma.getListaCancelacionAgenda().get(0).getNombreCentroAtencion()
							);
							
							estadito = ConstantesIntegridadDominio.acronimoEstadoEnvioCompleto;
							
							
						}catch(Exception e){
							estadito = ConstantesIntegridadDominio.acronimoEstadoEnvioIncompleto;
						}
						
						HibernateUtil.beginTransaction();
						envioMail.setCodigoPk(0);
						envioMail.setNombreFuncionalidad("Cancelación de Agenda Odontológica");
						envioMail.setCorreoDestino(listaDtoEnvioAuto.get(i).getEmailUsuario());
						envioMail.setFechaProceso(UtilidadFecha.getFechaActualTipoBD());
						envioMail.setHoraProceso(UtilidadFecha.getHoraActual());
						envioMail.setEstado(estadito);
						new LogEnvioEmailAutomaticoHome().persist(envioMail);
						HibernateUtil.endTransaction();
						
						
					}
				}
			}
		}
		saveErrors(request, errores);
		cierraConexion(connection);
		return mapping.findForward("principal");
	}

	/**
	 * Mï¿½todo que permite ordenar la lista
	 * @param connection
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionOrdenar(Connection connection, ActionMapping mapping,
			CancelarAgendaOdontoForm forma, UsuarioBasico usuario) {
		
		boolean ordenamiento= false;
		
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente")){
			
			ordenamiento = true;
		}
		
		SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		
		Collections.sort(forma.getListAgenda(),sortG);
		cierraConexion(connection);
		return mapping.findForward("listarAgenda");
	}
	
	/**
	 * Mï¿½todo que realiza la consulta a la BD de la agenda odontologica
	 * @param connection
	 * @param forma
	 * @param mundo
	 */
	private void realizarConsultaAgenda (Connection connection, CancelarAgendaOdontoForm forma, CancelarAgendaOdonto mundo){
//		se valida la seleccion de centro de atenciï¿½n y unidad de agenda
		String centrosAtencion = "";
		String unidadesAgenda = "";
		if(forma.getDatosBusqueda().getCentroAtencion() == ConstantesBD.codigoNuncaValido)
			centrosAtencion = forma.getListCentroAtencion().get(forma.getListCentroAtencion().size()-1).get("todos").toString();
		if(forma.getDatosBusqueda().getUnidadAgenda() == ConstantesBD.codigoNuncaValido)
			unidadesAgenda = forma.getListUnidadesAgendaXUsuario().get(forma.getListUnidadesAgendaXUsuario().size()-1).get("todos").toString();
		
//		se realiza la consulta de la agenda odontologica
		forma.setListAgenda(mundo.consultarAgendaOdonto(connection, forma.getDatosBusqueda(), centrosAtencion, unidadesAgenda));
	}

	/**
	 * Mï¿½todo que lista las agendas odontologicas
	 * @param connection
	 * @param forma
	 * @param mundo
	 * @param mapping
	 * @return
	 */
	private ActionForward accionListarAgenda(Connection connection,
			CancelarAgendaOdontoForm forma, CancelarAgendaOdonto mundo,
			ActionMapping mapping) {
		realizarConsultaAgenda(connection, forma, mundo);
		cierraConexion(connection);
		return mapping.findForward("listarAgenda");
	}

	/**
	 * Mï¿½todo que inicializa la funcionalidad de cancelacion de agenda odontolï¿½gica
	 * @param connection
	 * @param mapping
	 * @param request 
	 * @return
	 */
	private ActionForward accionEmpezar(Connection connection,
			ActionMapping mapping, UsuarioBasico usuario, CancelarAgendaOdontoForm forma, HttpServletRequest request) {
//		se almacena el centro de atencion del usuario
		forma.getDatosBusqueda().setCentroAtencion(usuario.getCodigoCentroAtencion());
//		se carga el listado de centros de atenciï¿½n 
		forma.setListCentroAtencion(
				UtilidadesConsultaExterna.centrosAtencionValidosXUsuario(connection, 
						usuario.getLoginUsuario(), 
						ConstantesBD.codigoActividadAutorizadaCancelarAgenda,
						ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica));
		if(forma.getListCentroAtencion().size()<=0)
		{
			return ComunAction.accionSalirCasoError(mapping, request, connection, logger, "error.agenda.usuarioNoCentrosAtencion", "error.agenda.usuarioNoCentrosAtencion", true);
		}
//		se cargarn la unidades de agenda por centro de atencion y usuario
		forma.setListUnidadesAgendaXUsuario(
				UtilidadesConsultaExterna.unidadesAgendaXUsuarioArray(
						connection, 
						usuario.getLoginUsuario(), 
						ConstantesBD.codigoNuncaValido, 
						ConstantesBD.codigoActividadAutorizadaCancelarAgenda,
						ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica, ""));
		// se carga los profesionales de la salud activos
		forma.setListProfesionalesActivos(
				UtilidadesAdministracion.obtenerProfesionales(
						connection, 
						usuario.getCodigoInstitucionInt(), 
						ConstantesBD.codigoNuncaValido, 
						false, true, ConstantesBD.codigoNuncaValido));
//		se carga los consultorios
		forma.setListConsultorios(
				UtilidadesConsultaExterna.consultoriosCentroAtencionTipo(
						connection, 
						usuario.getCodigoInstitucionInt(), 
						forma.getDatosBusqueda().getCentroAtencion()));
//		se cargan los dias de la semana
		forma.setListDiaSemana(Utilidades.obtenerDiasSemanaArray(connection));
		cierraConexion(connection);
		return mapping.findForward("principal");
	}
	
	/**
	 * Mï¿½todo que carga las unidades de agenda segï¿½n el centro de atenciï¿½n asincronamente
	 * @param connection
	 * @param forma
	 * @param usuario
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltrarUnidadesAgenda(Connection connection,
			CancelarAgendaOdontoForm forma, UsuarioBasico usuario,
			HttpServletResponse response) {

		// Se consultan las unidades de agenda
		forma.getListUnidadesAgendaXUsuario().clear();
		forma.setListUnidadesAgendaXUsuario(UtilidadesConsultaExterna.unidadesAgendaXUsuarioArray(connection, usuario.getLoginUsuario(), 
				forma.getIndex(), ConstantesBD.codigoActividadAutorizadaReasignarProfesional, 
				ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica, ""));
		
		
		String resultado = "<respuesta>" + "<infoid>"
				+ "<activar-seleccione>S</activar-seleccione>"
				+ "<sufijo>ajaxBusquedaTipoSelect</sufijo>"
				+ "<id-select>unidadAgendaBusqueda</id-select>"
				+ "<id-arreglo>unidadesAgenda</id-arreglo>" + 
				"</infoid>";
//		ArrayList<HashMap> unidadesAgenda = ;
		
		ListIterator<HashMap> iterator = forma.getListUnidadesAgendaXUsuario().listIterator();
		
		while(iterator.hasNext()){
			HashMap unidadAgenda = iterator.next();
			
			if (!(unidadAgenda.get("nombre")+"").equals("Todos Autorizados") && !(unidadAgenda.get("nombre")+"").equals("Seleccione")){
				resultado += "<unidadesAgenda>";
				resultado += "<codigo>" + unidadAgenda.get("codigo")
						+ "</codigo>";
				resultado += "<descripcion>" + unidadAgenda.get("nombre")
						+ "</descripcion>";
				resultado += "</unidadesAgenda>";
			}
		}

		resultado += "</respuesta>";
		
		UtilidadBD.closeConnection(connection);
		// **********SE GENERA RESPUESTA PARA AJAX EN
		// XML**********************************************
		try {
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(resultado);
		} catch (IOException e) {
			logger.error("Error al enviar respuesta AJAX en accionFiltrarUnidadesAgenda: " + e);
		}
		return null;
	}
	
	/**
	 * Mï¿½todo que carga los consultorios segï¿½n el centro de atenciï¿½n asincronamente
	 * @param connection
	 * @param forma
	 * @param usuario
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltrarConsultorios(Connection connection,
			CancelarAgendaOdontoForm forma, UsuarioBasico usuario,
			HttpServletResponse response) {

//		se carga los consultorios
		forma.getListConsultorios().clear();
		forma.setListConsultorios(
				UtilidadesConsultaExterna.consultoriosCentroAtencionTipo(
						connection, 
						usuario.getCodigoInstitucionInt(),forma.getIndex()));
		
		String resultado = "<respuesta>" + "<infoid>"
				+ "<activar-seleccione>S</activar-seleccione>"
				+ "<sufijo>ajaxBusquedaTipoSelect</sufijo>"
				+ "<id-select>consultorio</id-select>"
				+ "<id-arreglo>listConsultorios</id-arreglo>" + 
				"</infoid>";
//		ArrayList<HashMap> unidadesAgenda = ;
		
		ListIterator<HashMap> iterator = forma.getListConsultorios().listIterator();
		
		while(iterator.hasNext()){
			HashMap consultorio = iterator.next();
			
			resultado += "<listConsultorios>";
			resultado += "<codigo>" + consultorio.get("codigo")
					+ "</codigo>";
			resultado += "<descripcion>" + consultorio.get("descripcion")
					+ "</descripcion>";
			resultado += "</listConsultorios>";
		}

		resultado += "</respuesta>";
		
		UtilidadBD.closeConnection(connection);
		// **********SE GENERA RESPUESTA PARA AJAX EN
		// XML**********************************************
		try {
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(resultado);
		} catch (IOException e) {
			logger.error("Error al enviar respuesta AJAX en accionFiltrarConsultorios: " + e);
		}
		return null;
	}
	
	/**
	 * Mï¿½todo utilizado para cerrar la conexion y manejar la excepciï¿½n
	 */
	private void cierraConexion(Connection connection){
		try {
			UtilidadBD.cerrarConexion(connection);
		} catch (SQLException e) {
			Log4JManager.info("error cerrando la conexiï¿½n");
			e.printStackTrace();
		}
	}
	
	/**
	 * Mï¿½todo utilizado para imprimir las agendas canceladas con sus citas 
	 * @param connection
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @param esConsulta
	 * @return
	 */
	private ActionForward accionGenerarReporte(Connection connection,
			CancelarAgendaOdontoForm forma, ActionMapping mapping,
			UsuarioBasico usuario, HttpServletRequest request) {
		
		String nombreRptDesign = "CancelarAgendaOdonto.rptdesign";
		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		Vector v;
		
		//***************** INFORMACIï¿½N DEL CABEZOTE
		DesignEngineApi comp;
		comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"odontologia/",nombreRptDesign);
		
		// Logo
		comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
		
		// Nombre Instituciï¿½n, titulo y rango de fechas
		comp.insertGridHeaderOfMasterPageWithName(0,1,1,2, "titulo");
		v=new Vector();
		v.add(""+ins.getRazonSocial());
		v.add("NIT: "+ins.getNit()+"\n"+ins.getDireccion()+"\nTels: "+ins.getTelefono());
		comp.insertLabelInGridOfMasterPage(0,1,v);
		
		// Parametros de Generaciï¿½n
		comp.insertGridHeaderOfMasterPageWithName(1,0,1,1,"param");
		v=new Vector();
		
		// Fecha hora de proceso y usuario
		comp.insertLabelInGridPpalOfFooter(0,0,"Fecha: "+UtilidadFecha.getFechaActual()+" Hora: "+UtilidadFecha.getHoraActual());
		comp.insertLabelInGridPpalOfFooter(0,1,"Usuario: "+usuario.getLoginUsuario());
		
		 //***************** NUEVO WHERE DEL REPORTE
		String cadenaCodigosPk = "";
		if(forma.getListItemCancelados().size()>0){
			for(String codigo: forma.getListItemCancelados()){
				cadenaCodigosPk += codigo+",";
			}
			Log4JManager.info("cadenaCodigosPk:::"+cadenaCodigosPk);
			Log4JManager.info("lenght:::"+cadenaCodigosPk.length());
			cadenaCodigosPk = cadenaCodigosPk.substring(0, cadenaCodigosPk.length()-1);
			Log4JManager.info("cadenaCodigosPk1:::"+cadenaCodigosPk);
		}
		else
			cadenaCodigosPk = "-1";
		comp.obtenerComponentesDataSet("citasCanceladas");
		String newquery = "SELECT t.\"unidad_agenda\" AS \"unidad_agenda1\"    ," +
				"t.\"profesional_salud\"   AS \"profesional_salud1\"," +
				"t.\"fecha_cita\"          AS \"fecha_cita1\"       ," +
				"t.\"hora_cita\"           AS \"hora_cita1\"        ," +
				"t.\"consultorio\"         AS \"consultorio1\"      ," +
				"t.\"id_paciente\"         AS \"id_paciente1\"      ," +
				"t.\"paciente\"            AS \"paciente1\"         ," +
				"t.\"numero_solicitud\"    AS \"numero_solicitud1\" ," +
				"t.\"telefono\"            AS \"telefono1\" " +
				"FROM " +
				"(SELECT consultaexterna.getnombreunidadconsulta(cao.unidad_agenda) AS \"unidad_agenda\"    ," +
				"administracion.getnombremedico(cao.codigo_medico)                AS \"profesional_salud\"," +
				"cao.fecha                                                        AS \"fecha_cita\"       ," +
				"dcao.hora_inicio                                                 AS \"hora_cita\"        ," +
				"consultaexterna.getnombreconsultorio(cao.consultorio)            AS \"consultorio\"      ," +
				"manejopaciente.gettipoid(dcao.codigo_paciente) ||' ' ||manejopaciente.getidentificacionpaciente(dcao.codigo_paciente) AS \"id_paciente\"     ," +
				"administracion.getnombrepersona(dcao.codigo_paciente)            AS \"paciente\"        ," +
				"dcao.numero_solicitud                                            AS \"numero_solicitud\"," +
				"coalesce(p.telefono,'')          AS \"telefono\" " +
				"FROM odontologia.cancelacion_agenda_odo cao " +
				"INNER JOIN odontologia.det_cancel_agen_odo dcao " +
				"INNER JOIN administracion.personas p ON(p.codigo = dcao.codigo_paciente) " +
				"ON (cao.codigo_pk          = dcao.cancelacion_agenda) " +
				"WHERE dcao.cita_odontologica IN ("+cadenaCodigosPk+")" +
				") t " +
				"ORDER BY t.\"fecha_cita\"," +
				"t.\"hora_cita\"        ," +
				"t.\"unidad_agenda\"    ," +
				"t.\"profesional_salud\"," +
				"t.\"consultorio\"      ," +
				"t.\"id_paciente\"      ," +
				"t.\"paciente\"         ," +
				"t.\"telefono\"";
		
		Log4JManager.info("\n\nQUERY ------>>>"+newquery);
		comp.modificarQueryDataSet(newquery);
		
		String newPathReport = comp.saveReport1(false);
		comp.updateJDBCParameters(newPathReport+"");
		
		if(!newPathReport.equals("")){
			request.setAttribute("isOpenReport", "true");
			request.setAttribute("newPathReport", newPathReport);
		}

		cierraConexion(connection);
		return mapping.findForward("principal");
	}
}
