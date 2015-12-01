package com.princetonsa.action.odontologia;

import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.Administracion.UtilidadesAdministracion;
import util.consultaExterna.UtilidadesConsultaExterna;

import com.princetonsa.actionform.odontologia.GenerarAgendaOdontologicaForm;
import com.princetonsa.dto.odontologia.DtoAgendaOdontologica;
import com.princetonsa.dto.odontologia.DtoGenerarAgenda;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.odontologia.GenerarAgendaOdontologica;


/**
 * 
 * @author axioma
 *
 */
public class GenerarAgendaOdontologicaAction extends Action{
	
	@Override
	public ActionForward execute(	ActionMapping mapping, 
									ActionForm form,
									HttpServletRequest request, 
									HttpServletResponse response)throws Exception 
									{
		Connection con=null;
		try{
			if (form instanceof GenerarAgendaOdontologicaForm)
			{

				con = UtilidadBD.abrirConexion();	

				GenerarAgendaOdontologicaForm forma = (GenerarAgendaOdontologicaForm) form;
				GenerarAgendaOdontologica mundo = new GenerarAgendaOdontologica();  
				Log4JManager.info("*******************************");
				Log4JManager.info("estado-->" + forma.getEstado());
				UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");

				if (forma.getEstado()== null) {
					forma.reset();
					Log4JManager.warning("Estado no valido dentro del flujo de Generaciï¿½n de Agenda Odontologica (null) ");
					request.setAttribute("codigoDescripcionError","errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if(forma.getEstado().equals("generarAgendaAsignar"))
				{				
					forma.resetAsignar();

					forma.setCentroAtencion(usuario.getCodigoCentroAtencion()+"");
					forma.setCodigoActividadAutorizacion(ConstantesBD.codigoActividadAutorizadaGenerarAgenda);
					cargarDatosIniciales(con, forma, usuario);
					forma.setConsultorio(forma.getConsultorioAsignar());				
					forma.setCentroAtencion(forma.getCentroAtencionAsignar()+"");
					forma.setFechaInicial(forma.getFechaInicialAsignar());
					forma.setFechaFinal(forma.getFechaFinalAsignar());
					forma.setUnidadAgenda(forma.getUnidadAgendaAsignar());
					forma.setDiaSemana(forma.getDiaSemanaAsignar());
					forma.setProfesionalSalud(forma.getProfesionalAsignar());

					UtilidadBD.closeConnection(con);
					return mapping.findForward("popUpGenerar");
				}
				/**
				 * estado empezar
				 */
				else if(forma.getEstado().equals("generarAgenda"))
				{
					forma.reset();
					forma.setCentroAtencion(usuario.getCodigoCentroAtencion()+ConstantesBD.separadorSplit+usuario.getCentroAtencion());
					forma.setCodigoActividadAutorizacion(ConstantesBD.codigoActividadAutorizadaGenerarAgenda);
					cargarDatosIniciales(con, forma, usuario);
					ActionErrors errores = new ActionErrors();
					if((forma.getListCentroAtencion().size()<=0) && (forma.getListUnidadesAgendaXUsuario().size()<=0))
					{
						errores.add("descripcion", new ActionMessage("errors.notEspecific","El Usuario No tiene los permisos para generar Agenda Odontologica"));
						saveErrors(request, errores);
					}

					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}else if(forma.getEstado().equals("generarAgenOdon"))
				{
					return accionGenerarAgenOdo(mapping, request, con, forma,mundo, usuario);
				}else if (forma.getEstado().equals("redireccion")) {

					if(!forma.getPaginaLinkSiguiente().equals("")){
						//Log4JManager.info("redirecionar: siguiente link: "+forma.getPaginaLinkSiguiente());
						response.sendRedirect(forma.getPaginaLinkSiguiente());
					}else{
						forma.setMostrarResumenGenAgenOdon(ConstantesBD.acronimoSi);
						//Log4JManager.info("pagina link siguiente esta vacio");
						return mapping.findForward("principal");
					}
				}else if (forma.getEstado().equals("redireccionExcepciones")) {
					forma.setEstado("excepciones");
					if(!forma.getPaginaLinkSiguienteExcep().equals("")){
						//Log4JManager.info("redirecionar: siguiente link: "+forma.getPaginaLinkSiguiente());
						response.sendRedirect(forma.getPaginaLinkSiguienteExcep());
					}else{
						//Log4JManager.info("pagina link siguiente esta vacio");
						return mapping.findForward("excepcionesAgenOdon");
					}
				}else if (forma.getEstado().equals("listar")) {
					String centroAtencion = "";

					if(forma.getCentroAtencion().equals("-1"))
					{
						forma.setNombreCentroAtencion("TODOS");
						centroAtencion = forma.getListCentroAtencion().get(forma.getListCentroAtencion().size()-1).get("todos").toString();
					}
					else
					{
						centroAtencion = forma.getCentroAtencion().split(ConstantesBD.separadorSplit)[0]+"";
						forma.setNombreCentroAtencion(forma.getCentroAtencion().split(ConstantesBD.separadorSplit)[1]+"");
						forma.setCentroAtencion(centroAtencion);
					}

					if(forma.getUnidadAgenda().equals(ConstantesBD.codigoNuncaValido+""))
						forma.setUnidadAgenda(forma.getListUnidadesAgendaXUsuario().get(forma.getListUnidadesAgendaXUsuario().size()-1).get("todos").toString());

					forma.getGenerarAgenda().setAgendaOdonGen(mundo.cosultaAgendaOdontologica(con, 
							centroAtencion,
							forma.getUnidadAgenda(),
							forma.getConsultorio(), 
							forma.getDiaSemana(),
							forma.getProfesionalSalud(),
							forma.getFechaInicial(), 
							forma.getFechaFinal()));

					forma.setCentroAtencion(forma.getCentroAtencion()+ConstantesBD.separadorSplit+forma.getNombreCentroAtencion());
					return mapping.findForward("principal");

				}else if (forma.getEstado().equals("volver")) {
					forma.reset();
					return mapping.findForward("volver");
				}else if (forma.getEstado().equals("excepciones") || forma.getEstado().equals("detalleExcep") || forma.getEstado().equals("exHorarios")) {
					// listar las excepciones ocurridas durante la generaciï¿½n de la agenda odontologica
					return mapping.findForward("excepcionesAgenOdon");
				}else if (forma.getEstado().equals("volverRegenerar")) {
					if(forma.getGenerarAgenda().getAgendaOdonXGen().size()>0)
						forma.setMostrarResumenGenAgenOdon(ConstantesBD.acronimoNo);
					else
						forma.setMostrarResumenGenAgenOdon(ConstantesBD.acronimoSi);
					return mapping.findForward("principal");
				}else if (forma.getEstado().equals("listarConsulta") || forma.getEstado().equals("consultaGrafica")) {

					return accionConsulta(forma,usuario,mapping,request,mundo, con);
				}
				else if (forma.getEstado().equals("realizarBusquedaAtras")||forma.getEstado().equals("realizarBusquedaAdelante"))
				{
					return accionRealizarBusqueda(forma,usuario,mapping,request,mundo, con);
				}
				else if(forma.getEstado().equals("filtrarConsultorios"))
				{
					listarConsultorios(forma);
					forma.setMostrarResumenGenAgenOdon("");
					return mapping.findForward("principal");
				}
				else{
					forma.reset();
					Log4JManager.warning("Estado no valido dentro del flujo de generaciï¿½n de Agenda Odontologica -> "+ forma.getEstado());
					request.setAttribute("codigoDescripcionError","errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}

			}
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;		

	}
	
	private ActionForward accionConsulta(GenerarAgendaOdontologicaForm forma,
			UsuarioBasico usuario, ActionMapping mapping,
			HttpServletRequest request, GenerarAgendaOdontologica mundo, Connection con)
	{
		ActionErrors errores= new ActionErrors();
		
		if(forma.getEstado().equals("listarConsulta"))
			errores=validacionesGenerarAgendaOdon(forma, mundo, mapping, request);
		
		if(!errores.isEmpty())
			saveErrors(request, errores);
		else
		{
			forma.setMultiploMinGenCita(Utilidades.convertirAEntero(ValoresPorDefecto.getMultiploMinGeneracionCita(usuario.getCodigoInstitucionInt())));
			String centroAtencion = "";
			
			if(forma.getCentroAtencion().equals("-1"))
			{
				forma.setNombreCentroAtencion("TODOS");
				centroAtencion = forma.getListCentroAtencion().get(forma.getListCentroAtencion().size()-1).get("todos").toString();
			}
			else
			{
				centroAtencion = forma.getCentroAtencion().split(ConstantesBD.separadorSplit)[0]+"";
				forma.setNombreCentroAtencion(forma.getCentroAtencion().split(ConstantesBD.separadorSplit)[1]+"");
				forma.setCentroAtencion(centroAtencion);
			}
			
			if(forma.getUnidadAgenda().equals(ConstantesBD.codigoNuncaValido+""))
				forma.setUnidadAgenda(forma.getListUnidadesAgendaXUsuario().get(forma.getListUnidadesAgendaXUsuario().size()-1).get("todos").toString());
			
			if(forma.getEstado().equals("consultaGrafica"))
				forma.setFechaFinal(forma.getFechaInicial());
			
			forma.getGenerarAgenda().setAgendaOdonGen(mundo.cosultaAgendaOdontologica(con, 
										centroAtencion,
										forma.getUnidadAgenda(),
										forma.getConsultorio(), 
										forma.getDiaSemana(),
										forma.getProfesionalSalud(),
										forma.getFechaInicial(), 
										forma.getFechaFinal()));
			
			forma.setExcepcionesHorario(mundo.cosultaExAgendaOdontologica(con, 
					centroAtencion, 
					forma.getUnidadAgenda(), 
					forma.getConsultorio(), 
					forma.getDiaSemana(),
					forma.getProfesionalSalud(),
					forma.getFechaInicial(), 
					forma.getFechaFinal(), null));
			
			if(forma.getExcepcionesHorario().size() > 0)
				forma.setMostrarLinkExHorario(ConstantesBD.acronimoSi);
			else
				forma.setMostrarLinkExHorario(ConstantesBD.acronimoNo);
			
			forma.setMostrarResumenGenAgenOdon(ConstantesBD.acronimoSi);
			
			if(forma.getEstado().equals("consultaGrafica"))
			{
				forma.setXmlProfesionales(GenerarAgendaOdontologica.generacionXMLProfesionales(forma, false));
				forma.setXmlUnidadesAgenda(GenerarAgendaOdontologica.generacionXMLUnidadesAgenda(forma, false));
				forma.setXmlAgendaOdon(GenerarAgendaOdontologica.generacionXMLAgendaOdon(forma, false));
				forma.setXmlParametros(GenerarAgendaOdontologica.generacionXMLparametros(forma,false));					
			}
		}
		forma.setCentroAtencion(forma.getCentroAtencion()+ConstantesBD.separadorSplit+forma.getNombreCentroAtencion());
		return mapping.findForward("principal");
	}
	
	/**
	 * Método implementado para realizar la busqueda de agenda generada
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @param mundo 
	 * @return
	 */
	private ActionForward accionRealizarBusqueda(GenerarAgendaOdontologicaForm forma,
			UsuarioBasico usuario, ActionMapping mapping,
			HttpServletRequest request, GenerarAgendaOdontologica mundo, Connection con) 
	{
		if(forma.getEstado().equals("realizarBusquedaAtras"))
		{
			forma.setFechaInicial(UtilidadFecha.incrementarDiasAFecha(forma.getFechaInicial(), -1, false));
		}
		else if(forma.getEstado().equals("realizarBusquedaAdelante"))
		{
			forma.setFechaInicial(UtilidadFecha.incrementarDiasAFecha(forma.getFechaInicial(), 1, false));
		}
		forma.setEstado("consultaGrafica");
		return accionConsulta(forma, usuario, mapping, request, mundo, con);
	}

	/**
	 * Método para validar las fechas de la agenda odontológica
	 * @param forma
	 * @param mundo
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionErrors validacionesGenerarAgendaOdon( GenerarAgendaOdontologicaForm forma, GenerarAgendaOdontologica mundo, ActionMapping mapping,
			HttpServletRequest request)
	{
		ActionErrors errores = new ActionErrors(); 
		
		// validacion de las fechas
		boolean fechaInicialValida = true, fechaFinalValida = true;
		
		// se valida que las fecha inicial y la final contengan datos
		if(forma.getFechaInicial().equals("")){
			errores.add("fechaInicio",new ActionMessage("errors.required", "El Campo Fecha Inicial "));
			fechaInicialValida = false;
		}
		if(forma.getEstado().equals("")){
			errores.add("fechaFinal",new ActionMessage("errors.required", "El Campo Fecha Final "));
			fechaFinalValida = false;
		}
			
		// se valida que la fecha inicial y la final esten en el formato adecuado
		if(fechaInicialValida && fechaFinalValida)
		{
			if(!UtilidadFecha.validarFecha(forma.getFechaInicial())){
				errores.add("fechaInicio",new ActionMessage("errors.formatoFechaInvalido", "Fecha Inicial"));
				fechaInicialValida = false;
			}			
			if(!UtilidadFecha.validarFecha(forma.getFechaFinal())){
				errores.add("fechaFinal",new ActionMessage("errors.formatoFechaInvalido", "Fecha Final"));
				fechaFinalValida = false;
			}
		}
		
		// valida que la fecha cumplan con la estipulaciones de validaciones del anexo 864
		if(fechaInicialValida && fechaFinalValida)
		{
			// validar que la fecha de incio se igual o mayor a la fecha de actual
			if(!UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(forma.getFechaInicial()),UtilidadFecha.getFechaActual())||forma.getEstado().equals("listarConsulta"))
			{
				// se valida la fecha final si es igual o mayor a la fecha inicial
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(
						UtilidadFecha.conversionFormatoFechaAAp(forma.getFechaFinal()), 
						UtilidadFecha.conversionFormatoFechaAAp(forma.getFechaInicial()))){
					// error en la fecha final
					errores.add("fechaFinal",new ActionMessage("errors.fechaAnteriorIgualActual", "La Fecha Final", "Fecha Inicial"));
					fechaFinalValida = false;
				}
			}else{
				// error en la fecha inicial
				errores.add("fechaInicio",new ActionMessage("errors.fechaAnteriorIgualActual", "Inicial", "Actual"));
				fechaInicialValida = false;
			}
		}
		
		// se valida que el rango entre la fecha inicial y la final no se superior al  tres meses
		// esta parte se valida si las validaciones de la fecha inicial y final han sido correctas
		if(fechaInicialValida && fechaFinalValida){
			int nroMeses = UtilidadFecha.numeroMesesEntreFechas(
					UtilidadFecha.conversionFormatoFechaAAp(forma.getFechaInicial()),
					UtilidadFecha.conversionFormatoFechaAAp(forma.getFechaFinal()),true);
			if (nroMeses > 3)
				errores.add("rango agenda mayor", new ActionMessage("error.agenda.rangoMayorTresMeses", "GENERAR AGENDA ODONTOLOGICA"));
		}
		
		if((forma.getListCentroAtencion().size()<=0) && (forma.getListUnidadesAgendaXUsuario().size()<=0))
		{
			errores.add("descripcion", new ActionMessage("errors.notEspecific","El Usuario No tiene los permisos para generar Agenda Odontologica"));			
		}
		
		return errores;
	}
	
	/**
	 * 
	 * @param mapping
	 * @param request
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGenerarAgenOdo(ActionMapping mapping, HttpServletRequest request, Connection con,	GenerarAgendaOdontologicaForm forma, GenerarAgendaOdontologica mundo, UsuarioBasico usuario) 
	{
		ActionErrors errores = new ActionErrors(); 
		MessageResources fuenteMensaje = MessageResources.getMessageResources("com.servinte.mensajes.odontologia.GenerarAgendaOdontologicaForm");
		String mensajeConcreto 	= "";
		errores= validacionesGenerarAgendaOdon(forma, mundo, mapping, request);//validaciones de Fecha
		forma.setListaAdvertencias(new ArrayList<String>());
		
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
		}
		else			
		{
			generarAgendaOdontologica(con,forma, mundo, usuario);

			
			
			/**
			 * Escribir error
			 */
			if( mundo.isEsConsultorioOcupado())
			{
				errores.add("", new ActionMessage("errors.notEspecific", " No se puede generar agenda por que el consultorio está ocupado"));
				saveErrors(request, errores);
				//forma.setMostrarLinkExHorario(ConstantesBD.acronimoNo);
			}
			
			if (forma.isAgendaGenerada()){
				Log4JManager.info("ERROR AL REALIZAR EL PROCESO DE GENERACION DE AGENDA ODONTOLOGICA, YA EXISTE UNA GENERADA CON LOS PARAMETROS ESTABLECIDOS");
				errores.add("descripcion", new ActionMessage("errors.notEspecific","No se puede generar agenda Odontológica porque ya existe una generada para los parámetros seleccionados."));
				saveErrors(request, errores);
			}

			if(forma.getGenerarAgenda()!=null)
			{
				if (!forma.isAgendaGenerada()){
					if(forma.getGenerarAgenda().getAgendaOdonGen().size()>0 && forma.getGenerarAgenda().getAgenGeneradas()> 0){
						mensajeConcreto = fuenteMensaje.getMessage("GenerarAgendaOdontologicaForm.seGeneraronNagendas", forma.getGenerarAgenda().getAgenGeneradas() + "");
						forma.getListaAdvertencias().add(mensajeConcreto);
						forma.setAgendaGenerada(true);
					}
				}
				
				if(forma.getGenerarAgenda().getAgendaOdonXGen().size()>0)
				{
					forma.setMostrarResumenGenAgenOdon(ConstantesBD.acronimoNo);
					
					mensajeConcreto = fuenteMensaje.getMessage("GenerarAgendaOdontologicaForm.noSeGeneraronNagendas", forma.getGenerarAgenda().getAgendaOdonXGen().size());
					forma.getListaAdvertencias().add(mensajeConcreto);
					
				}
				else
				{
					forma.setMostrarResumenGenAgenOdon(ConstantesBD.acronimoSi);
				}
				
				
				if (mundo.getGenerarAgenda().getAgenGeneradas() <=0) {
					if (!mundo.isHorarioAtencion()){
						
						if (forma.getGenerarAgenda().getAgendaOdonXGen().size() <= 0) {
							Log4JManager.info("ERROR AL REALIZAR EL PROCESO DE GENERACION DE AGENDA ODONTOLOGICA, NO EXISTEN HORARIOS DE ATENCION");
							errores.add("descripcion", new ActionMessage("errors.notEspecific","No existen horarios de atención que cumplan los criterios especificados."));
							saveErrors(request, errores);
						}
					} else {
						if(forma.getGenerarAgenda().getAgendaOdonGen().isEmpty() && 
								(forma.getListaAdvertencias() == null || forma.getListaAdvertencias().size()<=0)){
							Log4JManager.info("ERROR AL REALIZAR EL PROCESO DE GENERACION DE AGENDA ODONTOLOGICA, YA EXISTE UNA GENERADA CON LOS PARAMETROS ESTABLECIDOS");
							errores.add("descripcion", new ActionMessage("errors.notEspecific","No se puede generar agenda Odontológica porque ya existe una generada para los parámetros seleccionados."));
							saveErrors(request, errores);
						}
					}
				}else if ((forma.getListaAdvertencias() == null || forma.getListaAdvertencias().size()<=0)&&
						mundo.getGenerarAgenda().getAgenXGenerar()==0 && 
						(mundo.getGenerarAgenda().getAgendaOdonGen() == null || mundo.getGenerarAgenda().getAgendaOdonGen().size() <=0)){
					Log4JManager.info("ERROR AL REALIZAR EL PROCESO DE GENERACION DE AGENDA ODONTOLOGICA POR FAVOR REVISAR");
					forma.setMostrarResumenGenAgenOdon("");
					forma.setGenerarAgenda(new DtoGenerarAgenda());
					errores.add("descripcion", new ActionMessage("errors.notEspecific","No se puede generar agenda porque existen "+mundo.getNumAgendasGenAntes()+"agenda(s) generada(s) para los parametros seleccionados."));
					saveErrors(request, errores);
				}
			}else if (forma.getListaAdvertencias() == null || forma.getListaAdvertencias().size()<=0) {
				Log4JManager.info("ERROR AL REALIZAR EL PROCESO DE GENERACION DE AGENDA ODONTOLOGICA POR FAVOR REVISAR");
				forma.setMostrarResumenGenAgenOdon("");
				forma.setGenerarAgenda(new DtoGenerarAgenda());
				errores.add("descripcion", new ActionMessage("errors.notEspecific","No se puede generar agenda porque existen "+mundo.getNumAgendasGenAntes()+"agenda(s) generada(s) para los parametros seleccionados."));
				saveErrors(request, errores);
			//	forma.setMostrarLinkExHorario(ConstantesBD.acronimoNo);
			}
			UtilidadBD.closeConnection(con);
		}
		forma.setCentroAtencion(forma.getCentroAtencion()+ConstantesBD.separadorSplit+forma.getNombreCentroAtencion());
		if(forma.getConsultorioAsignar().equals(""))
			return mapping.findForward("principal");
		else
			return mapping.findForward("popUpGenerar");
	}
	
	/**
	 * Si no hay errores en la fecha se procede a generar la agenda odontológica.
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void generarAgendaOdontologica(Connection con,
			GenerarAgendaOdontologicaForm forma,
			GenerarAgendaOdontologica mundo, UsuarioBasico usuario) {
		
		String centroAtencion = "";
		if(forma.getCentroAtencion().equals(ConstantesBD.codigoNuncaValido+""))
		{
			forma.setNombreCentroAtencion("TODOS");// APLICA PARA TODOS LOS CENTROS DE ATENCION
			centroAtencion = forma.getListCentroAtencion().get(forma.getListCentroAtencion().size()-1).get("todos").toString();
		}
		else
		{
			centroAtencion = forma.getCentroAtencion().split(ConstantesBD.separadorSplit)[0]+"";
			forma.setNombreCentroAtencion(forma.getCentroAtencion().split(ConstantesBD.separadorSplit)[1]+"");
			forma.setCentroAtencion(centroAtencion);
		}
		
		if(forma.getUnidadAgenda().equals(ConstantesBD.codigoNuncaValido+""))
		{
			forma.setUnidadAgenda(forma.getListUnidadesAgendaXUsuario().get(forma.getListUnidadesAgendaXUsuario().size()-1).get("todos").toString());
		}
																								
		//PRESENTACION MOSTRAR LINK EX HORARIO			
		if(forma.getGenerarAgenda().getAgendaOdonConExcepciones().size() > 0)
		{
			forma.setMostrarLinkExHorario(ConstantesBD.acronimoSi);// Excepciones al Horario de Atención 
		}
		else
		{
			forma.setMostrarLinkExHorario(ConstantesBD.acronimoNo);
		}
		
		llenarMundo(forma, mundo, usuario);
		
		// se genera la agenda odontologica
		forma.setGenerarAgenda(mundo.generarAgendaOdontologica(con, mundo.getGenerarAgenda()));
		/*forma.getGenerarAgenda().setAgendaOdonGen(mundo.cosultaAgendaOdontologica(con, 
												centroAtencion,
												forma.getUnidadAgenda(),
												forma.getConsultorio(), 
												forma.getDiaSemana(),
												forma.getProfesionalSalud(),
												forma.getFechaInicial(), 
												forma.getFechaFinal()));*/
		
		forma.setExcepcionesHorario(mundo.getGenerarAgenda().getAgendaOdonConExcepciones());
		
	}

	
	/**
	 * 
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void llenarMundo(GenerarAgendaOdontologicaForm forma,	GenerarAgendaOdontologica mundo, UsuarioBasico usuario) 
	{
		Log4JManager.info("todos los centro de atencion: "+forma.getListCentroAtencion().get(forma.getListCentroAtencion().size()-1).get("todos").toString());
		
		
		if(forma.getCentroAtencion().equals(ConstantesBD.codigoNuncaValido+""))
			mundo.getGenerarAgenda().setCentroAtencion(forma.getListCentroAtencion().get(forma.getListCentroAtencion().size()-1).get("todos").toString());
		else
			mundo.getGenerarAgenda().setCentroAtencion(forma.getCentroAtencion().split(ConstantesBD.separadorSplit)[0]+"");
		
		Log4JManager.info("todas las unidades de agenda: "+forma.getListUnidadesAgendaXUsuario().get(forma.getListUnidadesAgendaXUsuario().size()-1).get("todos").toString());
		
		if(forma.getUnidadAgenda().equals(ConstantesBD.codigoNuncaValido+""))
			mundo.getGenerarAgenda().setUnidadAgenda(forma.getListUnidadesAgendaXUsuario().get(forma.getListUnidadesAgendaXUsuario().size()-1).get("todos").toString());
		else
			mundo.getGenerarAgenda().setUnidadAgenda(forma.getUnidadAgenda());
		
		mundo.getGenerarAgenda().setConsultorio(forma.getConsultorio());
		mundo.getGenerarAgenda().setDiaSemana(forma.getDiaSemana());
		mundo.getGenerarAgenda().setProfesionalSalud(forma.getProfesionalSalud());
		mundo.getGenerarAgenda().setFechaInicial(forma.getFechaInicial());
		mundo.getGenerarAgenda().setFechaFinal(forma.getFechaFinal());
		mundo.getGenerarAgenda().setUsuarioModifica(usuario.getLoginUsuario());
		mundo.getGenerarAgenda().setRegenerarAgendaOdon(forma.getMostrarResumenGenAgenOdon().equals(ConstantesBD.acronimoNo)?ConstantesBD.acronimoSi:"");
		mundo.getGenerarAgenda().setAgendaOdonGen(forma.getGenerarAgenda().getAgendaOdonGen());
		mundo.getGenerarAgenda().setAgendaOdonXGen(forma.getGenerarAgenda().getAgendaOdonXGen());
		mundo.getGenerarAgenda().setAgendaOdonConExcepciones(forma.getGenerarAgenda().getAgendaOdonConExcepciones());
		mundo.getGenerarAgenda().setAgenGeneradas(forma.getGenerarAgenda().getAgenGeneradas());
		mundo.getGenerarAgenda().setAgenXGenerar(forma.getGenerarAgenda().getAgenXGenerar());
		mundo.getGenerarAgenda().setAgenOdonConExcep(forma.getGenerarAgenda().getAgenOdonConExcep());
		/*Log4JManager.info("\n\n\n");
		Log4JManager.info("valor de los consultorios:");
		Log4JManager.info("\n");
		for(DtoAgendaOdontologica elem: forma.getGenerarAgenda().getAgendaOdonXGen())
			Log4JManager.info("Consultorio "+elem.getConsultorio());
		Log4JManager.info("\n\n\n");**/
		
		if(forma.getMostrarResumenGenAgenOdon().equals(ConstantesBD.acronimoNo))
		{// concatenar los codigos de horarios de atencion
			String codigosHorAten="";
			for(int i=0; i<forma.getGenerarAgenda().getAgendaOdonXGen().size(); i++){
				codigosHorAten += forma.getGenerarAgenda().getAgendaOdonXGen().get(i).getCodigoHorarioAtencion()+", ";
			}
			codigosHorAten += ConstantesBD.codigoNuncaValido+"";
			mundo.getGenerarAgenda().setCodigosHorariosAtencion(codigosHorAten);
		}
	}

	/**
	 * Se cargan los ArrayList iniciales para la generación de la agenda
	 * @param con
	 * @param forma
	 * @param usuario
	 */
	private void cargarDatosIniciales(Connection con,
			GenerarAgendaOdontologicaForm forma, UsuarioBasico usuario) {
		
		// se cargan los centros de atencion
		forma.setListCentroAtencion(
				UtilidadesConsultaExterna.centrosAtencionValidosXUsuario(
						con, 
						usuario.getLoginUsuario(), 
						forma.getCodigoActividadAutorizacion(),
						ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica)
				);
		
		
		// se cargan las unidades de agenda por usuario
		forma.setListUnidadesAgendaXUsuario(
				UtilidadesConsultaExterna.unidadesAgendaXUsuarioArray(
						con, 
						usuario.getLoginUsuario(), 
						ConstantesBD.codigoNuncaValido, 
						forma.getCodigoActividadAutorizacion(),
						ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica, "")
					);
		
		
		// se carga los profesionales de la salud activos
		forma.setListProfesionalesActivos(
				UtilidadesAdministracion.obtenerProfesionales(
						con, 
						usuario.getCodigoInstitucionInt(), 
						ConstantesBD.codigoNuncaValido, 
						false, true, ConstantesBD.codigoNuncaValido)
					);
		
		
		// se cargan los dias de la semana
		forma.setListDiaSemana(Utilidades.obtenerDiasSemanaArray(con));
		
		/*
		forma.setListConsultorios(
				UtilidadesConsultaExterna.consultoriosCentroAtencionTipo(
						con, 
						usuario.getCodigoInstitucionInt(), 
						Utilidades.convertirAEntero(forma.getCentroAtencion().split(ConstantesBD.separadorSplit)[0]+""))
					);
		*/

		this.listarConsultorios(forma);
		
	}

	/**
	 * Lista los consultorios de la sección de generación de agenda
	 * @param forma {@link GenerarAgendaOdontologicaForm} Formulario
	 */
	private void listarConsultorios(GenerarAgendaOdontologicaForm forma)
	{
		DtoAgendaOdontologica dtoAgenda=new DtoAgendaOdontologica();
		int centroAtencion=ConstantesBD.codigoNuncaValido;
		int diaSemana=ConstantesBD.codigoNuncaValido;
		int codigoProfesional=ConstantesBD.codigoNuncaValido;
		int unidadAgenda=ConstantesBD.codigoNuncaValido;
		try{
			centroAtencion=Integer.parseInt(forma.getCentroAtencion().split(ConstantesBD.separadorSplit)[0]+"");
			unidadAgenda=Integer.parseInt(forma.getUnidadAgenda());
		}
		catch (NumberFormatException e) {
			return ;
			// Todavía no hay suficientes datos para filtrar los consultorios
		}
		try{
			diaSemana=Integer.parseInt(forma.getDiaSemana());
			codigoProfesional=Integer.parseInt(forma.getProfesionalSalud());
		}
		catch (NumberFormatException e) {
			// Simplemente no hay día de la semana, no se hace necesario asignarlo
		}
		dtoAgenda.setCentroAtencion(centroAtencion);
		dtoAgenda.setFechaInicio(forma.getFechaInicial());
		dtoAgenda.setFechaFin(forma.getFechaFinal());
		dtoAgenda.setDia(diaSemana);
		dtoAgenda.setCodigoMedico(codigoProfesional);
		dtoAgenda.setUnidadAgenda(unidadAgenda);
		forma.setListConsultorios(GenerarAgendaOdontologica.listarConsultoriosParaGenerarAgenda(dtoAgenda));
	}
}
