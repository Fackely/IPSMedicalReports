/*
 * @author jorge armando osorio velasquez
 */
package com.princetonsa.action.agenda;

import java.io.IOException;
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
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.Administracion.UtilidadesAdministracion;
import util.consultaExterna.UtilidadesConsultaExterna;

import com.princetonsa.actionform.agenda.ReasignarAgendaForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.agenda.HorarioAtencion;
import com.princetonsa.mundo.agenda.ReasignarAgenda;


/**
 * 
 * @author Jorge Armando Osorio Velasquez.
 *
 */
public class ReasignarAgendaAction extends Action 
{
	
	 /**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */ 
	private Logger logger = Logger.getLogger(ReasignarAgendaAction.class);
	

	/**
	 * Método execute del action
	 */
	public ActionForward execute(	ActionMapping mapping, 	
							        ActionForm form, 
							        HttpServletRequest request, 
							        HttpServletResponse response) throws Exception
	{

		Connection con = null;

		try {

	    if(form instanceof ReasignarAgendaForm)
	    {
		    
		    //intentamos abrir una conexion con la fuente de datos 
			con = UtilidadBD.abrirConexion();
			if(con == null)
			{
				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
				return mapping.findForward("paginaError");
			}
			
			ReasignarAgendaForm forma=(ReasignarAgendaForm)form;
			ReasignarAgenda mundo=new ReasignarAgenda();
			
			String estado = forma.getEstado();
			logger.warn("ESTADO [ReasignarAgendaAction] --> "+estado);
			UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
			if(estado == null)
			{
				logger.warn("Estado no valido dentro del flujo de ConceptoTesoreriaAction (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaError");
			}
			else if(estado.equals("empezar"))
			{
			    forma.reset();
			    ValoresPorDefecto.cargarValoresIniciales(con);
			    forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
			    
			    forma.setCentroAtencion(usuario.getCodigoCentroAtencion()+"");
			    
			    // Obtener centros de atención validos para el usuario
			    forma.setCentrosAtencionAutorizados(UtilidadesConsultaExterna.centrosAtencionValidosXUsuario(con, usuario.getLoginUsuario(), ConstantesBD.codigoActividadAutorizadaReasignarProfesional));
			    forma.setUnidadesAgendaAutorizadas(UtilidadesConsultaExterna.unidadesAgendaXUsuario(con, usuario.getLoginUsuario(), usuario.getCodigoCentroAtencion(), ConstantesBD.codigoActividadAutorizadaReasignarProfesional,ConstantesIntegridadDominio.acronimoTipoAtencionGeneral));
			    
			    UtilidadBD.closeConnection(con);
			    return mapping.findForward("paginaPrincipal");
			}
			else if(estado.equals("cambiarCentroAtencion"))
			{
			    forma.setCentrosAtencionAutorizados(UtilidadesConsultaExterna.centrosAtencionValidosXUsuario(con, usuario.getLoginUsuario(), ConstantesBD.codigoActividadAutorizadaReasignarProfesional));
			    forma.setUnidadesAgendaAutorizadas(UtilidadesConsultaExterna.unidadesAgendaXUsuario(con, usuario.getLoginUsuario(), Integer.parseInt(forma.getCentroAtencion()), ConstantesBD.codigoActividadAutorizadaReasignarProfesional,ConstantesIntegridadDominio.acronimoTipoAtencionGeneral));
			    
			    UtilidadBD.closeConnection(con);
			    return mapping.findForward("paginaPrincipal");
			}
			else if(estado.equals("ejecutarBusqueda"))
			{
				this.accionEjecutarBusqueda(con,mundo,forma,usuario);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("listadoAgenda");
			}
			else if(estado.equals("seleccionarTodos"))
			{
				this.accionSeleccionarTodos(forma);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("listadoAgenda");
			}
			else if (estado.equals("redireccion"))
			{
				UtilidadBD.closeConnection(con);
				response.sendRedirect(forma.getLinkSiguiente());
				return null;
			}
			else if(estado.equals("cambiarProfesional"))
			{
				return this.accionCambiarProfesional(con,mundo,forma,usuario,request,mapping);
			}
			else if(estado.equals("ordenar"))
			{
				this.accionOrdenarListadoAgendas(con,forma);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("listadoAgenda");
			}
			else if(estado.equals("volver"))
			{
				forma.getAgendas().clear();
				forma.setNuevoProfesional(ConstantesBD.codigoNuncaValido);
				forma.setSeleccionarTodo(false);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaPrincipal");
			}
			else if(estado.equals("buscarLog"))
			{
				this.accionEjecutarBusquedaLog(con,mundo,forma);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("listadoLog");
			}
			else if(estado.equals("volverConsultaLog"))
			{
				forma.getLogs().clear();
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaPrincipal");
			}
			else if(estado.equals("ordenarListadoLog"))
			{
				this.accionOrdenarListadoLog(con,forma);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("listadoLog");
			}
			else if(estado.equals("filtraUnidadAgenda"))
			{
				return accionFiltrarUnidadAgenda(con, forma, usuario, response);
			}
			
	    }
	    else
		{
			logger.error("El form no es compatible con el form de ReasignarAgendaForm");
			request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
			return mapping.findForward("paginaError");
		}
	    logger.error("NINGUN ESTADO VALIDO");

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
	 */
	private void accionOrdenarListadoLog(Connection con, ReasignarAgendaForm forma)
	{
		int numReg=Integer.parseInt(forma.getLogs().get("numRegistros").toString());
		String[] indices={"codigolog_","fecha_","centroatencion_","hora_","usuario_","fechacita_","horacita_","unidadconsulta_","nombreunidadconsulta_","codmedicoanterior_","nommedicoanterior_","codigoprofesional_","nombreprofesional_"};
		forma.setLogs(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getLogs(),numReg));
		forma.getLogs().put("numRegistros",numReg);
		forma.setUltimoPatron(forma.getPatronOrdenar());
	}


	/**
	 * 
	 * @param con
	 * @param mundo
	 * @param forma
	 */
	private void accionEjecutarBusquedaLog(Connection con, ReasignarAgenda mundo, ReasignarAgendaForm forma) 
	{
		// Capturar los centros de atencion autorizados
		mundo.setCentrosAtencion(forma.getCentrosAtencionAutorizados("todos").toString());
		
		mundo.setCodigoProfesional(forma.getCodigoProfesional());
		mundo.setUsuarioLog(forma.getUsuarioLog());
		mundo.setFechaInicial(forma.getFechaInicial());
		mundo.setFechaFinal(forma.getFechaFinal());
		mundo.setCentroAtencion(forma.getCentroAtencion());
		mundo.ejecutarBusquedalogs(con);
		forma.setLogs((HashMap)mundo.getLogs().clone());
	}


	/**
	 * 
	 * @param con
	 * @param forma
	 */
	private void accionOrdenarListadoAgendas(Connection con, ReasignarAgendaForm forma) 
	{
		int numReg=Integer.parseInt(forma.getAgendas("numRegistros").toString());
		HashMap mapOrdenado=new HashMap();
		if(forma.getPatronOrdenar().equals("horainicio_")||forma.getPatronOrdenar().equals("horafin_"))
		{
			String[] indices={"codigo_","unidadconsulta_","nombrecentroatencion_","nombreunidadconsulta_","consultorio_","descconsultorio_","fechaagenda_","horainicio_","horafin_","cupos_","reasignar_","temp_"};
			for(int i=0;i<numReg;i++)
			{
				forma.setAgendas("temp_"+i,forma.getAgendas(forma.getPatronOrdenar()+i).toString().replaceAll(":",""));
			}
			forma.setPatronOrdenar("temp_");
			mapOrdenado=Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getAgendas(),numReg);
		}
		else
		{
			String[] indices={"codigo_","unidadconsulta_","nombrecentroatencion_","nombreunidadconsulta_","consultorio_","descconsultorio_","fechaagenda_","horainicio_","horafin_","cupos_","reasignar_"};
			mapOrdenado=Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getAgendas(),numReg);
		}
		forma.setAgendas((HashMap)mapOrdenado.clone());
		forma.setAgendas("numRegistros",numReg);
		forma.setUltimoPatron(forma.getPatronOrdenar());
	}

	/**
	 * 
	 * @param con
	 * @param mundo
	 * @param forma
	 * @param usuario 
	 * @param mapping 
	 * @param response 
	 * @param request 
	 */
	private ActionForward accionCambiarProfesional(Connection con, ReasignarAgenda mundo, ReasignarAgendaForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) 
	{
		boolean enTransaccion=false;
		ActionErrors errores= new ActionErrors();
		logger.info("numRegistros Agendasd=> "+forma.getAgendas("numRegistros"));
		logger.info("codigo unidad consulta elegida=> "+forma.getCodigoUnidadConsulta());
		HashMap unidadesValidadas = new HashMap();
		boolean noPerteneceAUnidadAgenda = false;
		
		enTransaccion=UtilidadBD.iniciarTransaccion(con);
		for(int i=0;i<Integer.parseInt(forma.getAgendas("numRegistros").toString());i++)
		{
			if(UtilidadTexto.getBoolean(forma.getAgendas("reasignar_"+i).toString()))
			{ 
				//Se verifica que el profesional pertenezca a la unidad de agenda seleccionada dependiendo de la especialidad
				//Esta validacion solo aplica cuando se le dice a la busqueda que son TODAS las unidades de agenda
				if(!ReasignarAgenda.perteneceProfesionalAUnidadAgenda(con, forma.getNuevoProfesional(), Integer.parseInt(forma.getAgendas("unidadconsulta_"+i).toString()))&&
					forma.getCodigoUnidadConsulta()==ConstantesBD.codigoNuncaValido)
				{
					//Se verifica que la unidad de agenda ya no se haya reportado como error
					if(!unidadesValidadas.containsKey(forma.getAgendas("unidadconsulta_"+i).toString()))
					{
						errores.add("",new ActionMessage("error.agenda.profesionalNoUnidadAgenda",forma.getAgendas("nombreunidadconsulta_"+i)+""));
						unidadesValidadas.put(forma.getAgendas("unidadconsulta_"+i).toString(), forma.getAgendas("nombreunidadconsulta_"+i));
						noPerteneceAUnidadAgenda = true;
					}
				}
				else if(this.valdiacionesAgenda(con,mundo,forma,Integer.parseInt(forma.getAgendas("codigo_"+i).toString()),usuario)&&enTransaccion)
				{
					enTransaccion=mundo.reasignarProfesiona(con,Integer.parseInt(forma.getAgendas("codigo_"+i).toString()),forma.getNuevoProfesional());
					HashMap vo=new HashMap();
					vo.put("usuario",usuario.getLoginUsuario());
					vo.put("codigoAgenda",forma.getAgendas("codigo_"+i));
					vo.put("medicoAnterior",forma.getCodigoProfesionalAnterior());
					if(enTransaccion)
						enTransaccion=mundo.insertarLogReasignacionAgenda(con,vo);
				}
			}
		}
		
		
		/**
		 * Nota *  Si se encontró que el profesional elegido no hace parte de alguna agenda
		 * se cancela el proceso
		 */
		if(noPerteneceAUnidadAgenda)
			enTransaccion = false;
		
		if(enTransaccion)
		{
			if(!errores.isEmpty())
				saveErrors(request, errores);
			UtilidadBD.finalizarTransaccion(con);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("procesoExitoso");
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
			
			errores.add("PROCESO NO EXITOSO", new ActionMessage("errors.procesoNoExitoso"));
			saveErrors(request, errores);
		}
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoAgenda");
	}


	/**
	 * 
	 * @param forma
	 */
	private void accionSeleccionarTodos(ReasignarAgendaForm forma)
	{
		for(int i=0;i<Integer.parseInt(forma.getAgendas("numRegistros").toString());i++)
		{
			forma.setAgendas("reasignar_"+i,forma.isSeleccionarTodo()+"");
		}
		//MT 2094 Validacion para que no aparezca en la lista el medico que se desea cambiar.
		forma.setCodigoProfesional(Utilidades.convertirAEntero(forma.getCodigoProfesionalAnterior()));
	}


	/**
	 * 
	 * @param con
	 * @param mundo
	 * @param forma 
	 * @param usuario 
	 * @param agendas
	 */
	public boolean valdiacionesAgenda(Connection con, ReasignarAgenda mundo, ReasignarAgendaForm forma, int codigoAgenda, UsuarioBasico usuario) 
	{
		boolean respuesta=false;
		HashMap citas=new HashMap();
		citas=(HashMap)mundo.consultarCitasAgenda(con,codigoAgenda).clone();
		Utilidades.imprimirMapa(citas);
		for(int i=0;i<Integer.parseInt(citas.get("numRegistros").toString());i++)
		{
			int codEstCita=Integer.parseInt(citas.get("estadocita_"+i).toString());
			logger.info("estadoCita > "+codEstCita);
			if(codEstCita==ConstantesBD.codigoEstadoCitaCanceladaPaciente||codEstCita==ConstantesBD.codigoEstadoCitaCanceladaInstitucion||codEstCita==ConstantesBD.codigoEstadoCitaAtendida||codEstCita==ConstantesBD.codigoEstadoCitaNoCumplida)
			{
				//generar el log tipo archivo aqui.
				this.accionGenerarLog(usuario,codigoAgenda,citas.get("codigocita_"+i).toString(),codEstCita,forma.getCodigoProfesional());
			}
			else
				respuesta = true;
		}
		
		if(Integer.parseInt(citas.get("numRegistros").toString())==0)
			respuesta = true;
		
		logger.info("RESPUESTA--->>>>>>> "+respuesta);
		return respuesta;
	}
	
	/**
	 * 
	 * @param usuario
	 * @param codigoAgenda
	 * @param string
	 * @param codigoProfesional
	 * @param i 
	 */
	private void accionGenerarLog(UsuarioBasico usuario, int codigoAgenda, String codCita, int estadoCita, int codigoProfesional) 
	{
		String log = "";
		int tipoLog=0;
		log = 		 "\n   ============REGISTRO ELIMINADO=========== " +
		"\n*  Codigo Agenda [" +codigoAgenda+""+"] "+
		"\n*  Codigo Cita ["+codCita+""+"] " +
		"\n*  Estado Cita ["+estadoCita+""+"] " +
		"\n*  Medico Anterior [" +codigoProfesional+""+"] "+
		"\n========================================================\n\n\n ";
		tipoLog=ConstantesBD.tipoRegistroLogModificacion;
		LogsAxioma.enviarLog(ConstantesBD.logReasignarProfesionalCodigo,log,tipoLog,usuario.getLoginUsuario());
	}

	/**
	 * Metodo que ejecuta la busqueda de las agendas.
	 * @param con
	 * @param mundo
	 * @param forma
	 * @param usuario 
	 */
	private void accionEjecutarBusqueda(Connection con, ReasignarAgenda mundo, ReasignarAgendaForm forma, UsuarioBasico usuario) 
	{
		// Capturar los centros de atencion autorizados
		mundo.setCentrosAtencion(forma.getCentrosAtencionAutorizados("todos").toString());
		
		// Capturar las unidades de agenda autorizados
		mundo.setUnidadesAgenda(forma.getUnidadesAgendaAutorizadas("todos").toString());
		mundo.setCodigoProfesional(forma.getCodigoProfesional());
		mundo.setCodigoUnidadConsulta(forma.getCodigoUnidadConsulta());
		mundo.setFechaInicial(forma.getFechaInicial());
		mundo.setFechaFinal(forma.getFechaFinal());
		mundo.setCentroAtencion(forma.getCentroAtencion());
		
		logger.info("UNIDADES AGENDA: "+mundo.getUnidadesAgenda());
		logger.info("CODIGO UNIDAD AGENDA: "+mundo.getCodigoUnidadConsulta());
		

		ArrayList<HashMap<String, Object>> array = new ArrayList<HashMap<String, Object>>();
		if(mundo.getCodigoUnidadConsulta()>0){
			HorarioAtencion horarioAten = new HorarioAtencion();
			forma.getProfesionaleSaludUniAgen().clear();
			if(forma.getCodigoUnidadConsulta()!=ConstantesBD.codigoNuncaValido)
			{
				forma.setDatosUnidadAgenda(horarioAten.getEspecialidad(con, forma.getIndexCodUniAgen()));
				if(Utilidades.convertirAEntero(forma.getDatosUnidadAgenda().get("especialidad").toString()) != ConstantesBD.codigoNuncaValido
						&& forma.getDatosUnidadAgenda().containsKey("especialidad")
						)
				{
					if(forma.getDatosUnidadAgenda().get("profesionales").toString().equals(ConstantesIntegridadDominio.acronimoAmbos))
					{
						
						array = UtilidadesAdministracion.obtenerCodCentroCostoXUnidadConsulta(con, forma.getIndexCodUniAgen());
						forma.setProfesionaleSaludUniAgen(UtilidadesAdministracion.obtenerProfesionales(con, 
								usuario.getCodigoInstitucionInt(), 
								Utilidades.convertirAEntero(forma.getDatosUnidadAgenda().get("especialidad").toString()), 
								false, true, array));
					}else{
						if(forma.getDatosUnidadAgenda().get("profesionales").toString().charAt(0) == ConstantesBD.codigoServicioProcedimiento)
						{
							array = UtilidadesAdministracion.obtenerCodCentroCostoXUnidadConsulta(con, forma.getIndexCodUniAgen());
							forma.setProfesionaleSaludUniAgen(UtilidadesAdministracion.obtenerProfesionales(con, 
									usuario.getCodigoInstitucionInt(), 
									ConstantesBD.codigoNuncaValido, 
									false, true, array));
						}else{
							if(forma.getDatosUnidadAgenda().get("profesionales").toString().charAt(0) == ConstantesBD.codigoServicioInterconsulta)
							{
								forma.setProfesionaleSaludUniAgen(UtilidadesAdministracion.obtenerProfesionales(con, 
										usuario.getCodigoInstitucionInt(), 
										Utilidades.convertirAEntero(forma.getDatosUnidadAgenda().get("especialidad").toString()),
										false, true, array));
							}
						}
					}
				}
			}
			//forma.setProfesionales(ReasignarAgenda.obtenerProfesionalesUnidadAgenda(con, mundo.getCodigoUnidadConsulta()));
		}else
			forma.setProfesionales(UtilidadesAdministracion.obtenerProfesionales(con, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoNuncaValido, false, true, ConstantesBD.codigoNuncaValido));
		
		
		mundo.ejecutarBusquedaAgendas(con);
		forma.setAgendas((HashMap)mundo.getAgendas().clone());
	}
	
	
	/**
	 * Método implementado para realizar el filtro de unidades de agenda
	 * @param con
	 * @param cargosForm
	 * @param usuario
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltrarUnidadAgenda(Connection con, ReasignarAgendaForm forma, UsuarioBasico usuario, HttpServletResponse response) 
	{
		//Se consultan los Profesionales de la Salud
		ArrayList<HashMap<String, Object>> array = new ArrayList<HashMap<String, Object>>();
		HorarioAtencion horarioAten = new HorarioAtencion();
		forma.getProfesionaleSaludUniAgen().clear();
		if(Utilidades.convertirAEntero(forma.getIndexCodUniAgen())!=ConstantesBD.codigoNuncaValido)
		{
			forma.setDatosUnidadAgenda(horarioAten.getEspecialidad(con, forma.getIndexCodUniAgen()));
			if(Utilidades.convertirAEntero(forma.getDatosUnidadAgenda().get("especialidad").toString()) != ConstantesBD.codigoNuncaValido
					&& forma.getDatosUnidadAgenda().containsKey("especialidad")
					)
			{
				if(forma.getDatosUnidadAgenda().get("profesionales").toString().equals(ConstantesIntegridadDominio.acronimoAmbos))
				{
					
					array = UtilidadesAdministracion.obtenerCodCentroCostoXUnidadConsulta(con, forma.getIndexCodUniAgen());
					forma.setProfesionaleSaludUniAgen(UtilidadesAdministracion.obtenerProfesionales(con, 
							usuario.getCodigoInstitucionInt(), 
							Utilidades.convertirAEntero(forma.getDatosUnidadAgenda().get("especialidad").toString()), 
							false, true, array));
				}else{
					if(forma.getDatosUnidadAgenda().get("profesionales").toString().charAt(0) == ConstantesBD.codigoServicioProcedimiento)
					{
						array = UtilidadesAdministracion.obtenerCodCentroCostoXUnidadConsulta(con, forma.getIndexCodUniAgen());
						forma.setProfesionaleSaludUniAgen(UtilidadesAdministracion.obtenerProfesionales(con, 
								usuario.getCodigoInstitucionInt(), 
								ConstantesBD.codigoNuncaValido, 
								false, true, array));
					}else{
						if(forma.getDatosUnidadAgenda().get("profesionales").toString().charAt(0) == ConstantesBD.codigoServicioInterconsulta)
						{
							forma.setProfesionaleSaludUniAgen(UtilidadesAdministracion.obtenerProfesionales(con, 
									usuario.getCodigoInstitucionInt(), 
									Utilidades.convertirAEntero(forma.getDatosUnidadAgenda().get("especialidad").toString()),
									false, true, array));
						}
					}
				}
			}
		}
		
		String resultado = "<respuesta>" +
			"<infoid>" +
				"<sufijo>ajaxBusquedaTipoSelect</sufijo>";
		if(forma.getProfesionaleSaludUniAgen().size()==0)
			resultado+="<valor-opcion-default>No existen profesionales vinculados a la unidad de agenda</valor-opcion-default>";
		resultado+="<id-select>profesional</id-select>" +
				"<id-arreglo>profesionaleSaludUniAgen</id-arreglo>" + //nombre de la etiqueta de cada elemento
			"</infoid>";
		
		for(HashMap elemento:forma.getProfesionaleSaludUniAgen())
		{
			resultado += "<profesionaleSaludUniAgen>";
				resultado += "<codigo>"+elemento.get("codigo")+ConstantesBD.separadorSplit+elemento.get("nombre").toString()+"</codigo>";
				resultado += "<descripcion>"+elemento.get("nombre").toString()+"</descripcion>";
			resultado += "</profesionaleSaludUniAgen>";
		}
		
		resultado += "</respuesta>";
		
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.setCharacterEncoding("ISO-8859-1");
	        response.getWriter().write(resultado);
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltrarSalas: "+e);
		}
		return null;
	}

}
