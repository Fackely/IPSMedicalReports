package com.princetonsa.action.capitacion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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

import util.ConstantesIntegridadDominio;

import com.ibm.icu.text.SimpleDateFormat;
import com.princetonsa.actionform.capitacion.ConsultaLogSubirPacientesForm;
import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.capitacion.DTOConsultaLogSubirPacientes;
import com.princetonsa.dto.capitacion.DtoInconsistenciasArchivoPlano;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IInconsistenSubirPacienteMundo;
import com.servinte.axioma.orm.CapitadoInconsistencia;
import com.servinte.axioma.orm.InconsistenSubirPaciente;
import com.servinte.axioma.orm.InconsistenciaPersona;
import com.servinte.axioma.orm.InconsistenciasCampos;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.capitacion.CapitacionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.ILogSubirPacientesServicio;

public class ConsultaLogSubirPacientesAction extends Action
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(ConsultaLogSubirPacientesAction.class);
	
	/** * Contiene la lista de mensajes correspondiente a esta forma */
	MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.capitacion.SubirPacienteForm");
	
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception
	{
        UsuarioBasico usuario;
        ConsultaLogSubirPacientesForm consultaLogSubirPacientesForm;
		if (form instanceof ConsultaLogSubirPacientesForm)
		{
			consultaLogSubirPacientesForm = (ConsultaLogSubirPacientesForm)form;
			String estado=consultaLogSubirPacientesForm.getEstado();
			logger.warn("Estado ConsultaLogSubirPacientesForm [" + estado + "]");
			ActionErrors errores=new ActionErrors();
			usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			
            if( usuario == null )
            {
                consultaLogSubirPacientesForm.cleanCompleto();                   
                request.setAttribute("codigoDescripcionError", "errors.usuario.noCargado");
                return mapping.findForward("paginaError");              
            }
            else if(estado == null )
            {
                consultaLogSubirPacientesForm.cleanCompleto();
                request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
                return mapping.findForward("descripcionError");             
            }
            else if(estado.equals("empezar"))
			{
            	return this.accionEmpezar(consultaLogSubirPacientesForm, mapping, request, errores);
			}
            else if(estado.equals("buscar"))
			{
            	return this.accionBuscar(consultaLogSubirPacientesForm, mapping, request, errores);
			}
            else if(estado.equals("cambiarOrden"))
			{
            	return this.accionOrdenar(consultaLogSubirPacientesForm, mapping, request, errores);
            	
			}else if(estado.equals("consultaLog")){
				
				return this.buscarIconsistenciasLog( consultaLogSubirPacientesForm,  mapping, request, errores);
			}else
			{
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				return mapping.findForward("paginaError");
			}
		}
	    else
	    {
			 request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
			 return mapping.findForward("paginaError");
	    }
	}

		
	/**
	 * Ordenar por columna
	 * 
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenar(ConsultaLogSubirPacientesForm forma, ActionMapping mapping, 
											HttpServletRequest request, ActionErrors errores) 
	{
		boolean ordenamiento= false;
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente")){
			ordenamiento = true;
		}
		SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		Collections.sort(forma.getListaConsultaLogsubirPacientes() ,sortG);
		forma.setEstado("consultando");	
		return mapping.findForward("principal");		
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado empezar
	 * @param modificarCarguesForm
	 * @param mapping
	 * @param request
	 * @param errores
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionEmpezar(ConsultaLogSubirPacientesForm consultaLogSubirPacientesForm, 
											ActionMapping mapping , HttpServletRequest request, ActionErrors errores) throws Exception
	{
		consultaLogSubirPacientesForm.cleanCompleto();
		consultaLogSubirPacientesForm.setEstado("buscando");		
		return mapping.findForward("principal");
	}
	
	/**
	 * Realiza la busqueda de los logs según los criterios de búsqueda especificados
	 * @param consultaLogSubirPacientesForm
	 * @param mapping
	 * @param request
	 * @param errores
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionBuscar(ConsultaLogSubirPacientesForm forma, ActionMapping mapping, 
											HttpServletRequest request, ActionErrors errores) throws Exception
	{
		try		
		{	
			UtilidadTransaccion.getTransaccion().begin();
					
			ILogSubirPacientesServicio logSubirPacientesServicio=CapitacionFabricaServicio.crearLogSubirPacientesServicio();			
			ArrayList<DTOConsultaLogSubirPacientes> listaConsultaLog=logSubirPacientesServicio.consultarLog(forma);
			forma.setListaConsultaLogsubirPacientes(listaConsultaLog);
			forma.setEstado("consultando");		
		}
		catch(Exception e)
		{
			errores.add("Error Consultando Log", new ActionMessage("errors.notEspecific", e.getMessage()));
			saveErrors(request, errores);
            logger.warn(e);
            forma.setEstado("buscando");
		}
		finally{
			UtilidadTransaccion.getTransaccion().commit();
		}
		return mapping.findForward("principal");
	}
	
	
	@SuppressWarnings("unchecked")
	private ActionForward buscarIconsistenciasLog(ConsultaLogSubirPacientesForm forma, ActionMapping mapping, HttpServletRequest request, ActionErrors errores) throws Exception
	{
		try		
		{	
			UtilidadTransaccion.getTransaccion().begin();
			IInconsistenSubirPacienteMundo inconsistenSubirPacientesMundo=CapitacionFabricaMundo.crearInconsistenSubirPacienteMundo();
			List<InconsistenSubirPaciente> listaInconsistencias =	inconsistenSubirPacientesMundo.buscarInconsistenciasPorLog(forma.getCodigoPkSubirPaciente());
			ArrayList<DtoInconsistenciasArchivoPlano> inconsistenciasGenerales = new ArrayList<DtoInconsistenciasArchivoPlano>();
			ArrayList<DtoInconsistenciasArchivoPlano> inconsistenciasCarguePrevio = new ArrayList<DtoInconsistenciasArchivoPlano>();
			List<DtoInconsistenciasArchivoPlano> inconsistenciasCampos = new ArrayList<DtoInconsistenciasArchivoPlano>();
			ArrayList<DtoInconsistenciasArchivoPlano> inconsistenciasPersonas = new ArrayList<DtoInconsistenciasArchivoPlano>();
			if(listaInconsistencias != null && !listaInconsistencias.isEmpty()){
				for(InconsistenSubirPaciente inconsistencia:listaInconsistencias){
					if(inconsistencia.getTipoInconsistencia().equals(ConstantesIntegridadDominio.acronimoInconsistenciaGeneral)){
						DtoInconsistenciasArchivoPlano dtoInconsistencia = new DtoInconsistenciasArchivoPlano();
						dtoInconsistencia.setTipoInconsistencia(inconsistencia.getTipoInconsistencia());
						dtoInconsistencia.setDescripcion(inconsistencia.getDescripcion());
						inconsistenciasGenerales.add(dtoInconsistencia);
						break;
					}
					else{
						if(inconsistencia.getTipoInconsistencia().equals(ConstantesIntegridadDominio.acronimoInconsistenciaDatosBasicos)){
							if(inconsistencia.getInconsistenciasCamposes() != null && !inconsistencia.getInconsistenciasCamposes().isEmpty()){
								Iterator<InconsistenciasCampos> it = (Iterator<InconsistenciasCampos>)inconsistencia.getInconsistenciasCamposes().iterator();
								while (it.hasNext()) {
									InconsistenciasCampos element = it.next();
									DtoInconsistenciasArchivoPlano dtoCampos = new DtoInconsistenciasArchivoPlano();
									dtoCampos.setCampo(element.getNombreCampo());
									String nombreInconsistencia=(String)util.ValoresPorDefecto.getIntegridadDominio(element.getTipoInconsistencia());
									dtoCampos.setDescripcion(nombreInconsistencia+" en la linea: "+element.getLinea());
									dtoCampos.setNumeroFila(element.getLinea());
									inconsistenciasCampos.add(dtoCampos);
								}
							}
						}
						if(inconsistencia.getTipoInconsistencia().equals(ConstantesIntegridadDominio.acronimoCarguePrevioMismoPeriodo)){
							if(inconsistencia.getCapitadoInconsistencias() != null && !inconsistencia.getCapitadoInconsistencias().isEmpty()){
								Iterator<CapitadoInconsistencia> it = (Iterator<CapitadoInconsistencia>)inconsistencia.getCapitadoInconsistencias().iterator();
								while (it.hasNext()) {
									CapitadoInconsistencia element = it.next();
									DtoInconsistenciasArchivoPlano dtoCarguePrevio = new DtoInconsistenciasArchivoPlano();
									DtoPersonas dtoPersona= new DtoPersonas();
									dtoPersona.setTipoIdentificacion(element.getTipoIdentificacion());
									dtoPersona.setNumeroIdentificacion(element.getNumeroIdentificacion());
									dtoPersona.setPrimerNombre(element.getPrimerNombre());
									if(element.getSegundoNombre() != null){
										dtoPersona.setSegundoNombre(element.getSegundoNombre());
									}
									else{
										dtoPersona.setSegundoNombre("");
									}
									dtoPersona.setPrimerApellido(element.getPrimerApellido());
									if(element.getSegundoApellido() != null){
										dtoPersona.setSegundoApellido(element.getSegundoApellido());
									}
									else{
										dtoPersona.setSegundoApellido("");
									}
									if(element.getFechaNacimiento() != null){
										dtoPersona.setFechaNacimiento(element.getFechaNacimiento());
									}
									else{
										dtoPersona.setFechaNacimiento(element.getFechaNacimiento());
									}
									dtoCarguePrevio.setPersonaArchivo(dtoPersona);
									dtoCarguePrevio.setTipoInconsistencia(element.getTipoInconsistencia());
									inconsistenciasCarguePrevio.add(dtoCarguePrevio);
								}
							}
						}
						if(inconsistencia.getTipoInconsistencia().equals(ConstantesIntegridadDominio.acronimoInconsistenciaDatosIguales)){
							if(inconsistencia.getCapitadoInconsistencias() != null && !inconsistencia.getCapitadoInconsistencias().isEmpty()){
								Iterator<CapitadoInconsistencia> it = (Iterator<CapitadoInconsistencia>)inconsistencia.getCapitadoInconsistencias().iterator();
								while (it.hasNext()) {
									CapitadoInconsistencia element = it.next();
									DtoInconsistenciasArchivoPlano dtoInconsisPersonas = new DtoInconsistenciasArchivoPlano();
									DtoPersonas dtoPersona= new DtoPersonas();
									dtoPersona.setTipoIdentificacion(element.getTipoIdentificacion());
									dtoPersona.setNumeroIdentificacion(element.getNumeroIdentificacion());
									dtoPersona.setPrimerNombre(element.getPrimerNombre());
									if(element.getSegundoNombre() != null){
										dtoPersona.setSegundoNombre(element.getSegundoNombre());
									}
									else{
										dtoPersona.setSegundoNombre("");
									}
									dtoPersona.setPrimerApellido(element.getPrimerApellido());
									if(element.getSegundoApellido() != null){
										dtoPersona.setSegundoApellido(element.getSegundoApellido());
									}
									else{
										dtoPersona.setSegundoApellido("");
									}
									if(element.getFechaNacimiento() != null){
										dtoPersona.setFechaNacimiento(element.getFechaNacimiento());
									}
									else{
										dtoPersona.setFechaNacimiento(element.getFechaNacimiento());
									}
									dtoInconsisPersonas.setPersonaArchivo(dtoPersona);
									dtoInconsisPersonas.setTipoInconsistencia(element.getTipoInconsistencia());
									if(element.getInconsistenciaPersonas() != null && !element.getInconsistenciaPersonas().isEmpty()){
										SimpleDateFormat format= new SimpleDateFormat("dd/MM/yyyy");
										Iterator<InconsistenciaPersona> itp = (Iterator<InconsistenciaPersona>)element.getInconsistenciaPersonas().iterator();
										ArrayList<DtoPersonas> listaPersonas = new ArrayList<DtoPersonas>();
										while (itp.hasNext()) {
											InconsistenciaPersona elementPer = itp.next();
											DtoPersonas dtoPersonaBD = new DtoPersonas();
											dtoPersonaBD.setTipoIdentificacion(elementPer.getPersonas().getTiposIdentificacion().getAcronimo());
											dtoPersonaBD.setNumeroIdentificacion(elementPer.getPersonas().getNumeroIdentificacion());
											dtoPersonaBD.setPrimerNombre(elementPer.getPersonas().getPrimerNombre());
											if(elementPer.getPersonas().getSegundoNombre() != null){
												dtoPersonaBD.setSegundoNombre(elementPer.getPersonas().getSegundoNombre());
											}
											else{
												dtoPersonaBD.setSegundoNombre("");
											}
											dtoPersonaBD.setPrimerApellido(elementPer.getPersonas().getPrimerApellido());
											if(elementPer.getPersonas().getSegundoApellido() != null){
												dtoPersonaBD.setSegundoApellido(elementPer.getPersonas().getSegundoApellido());
											}
											else{
												dtoPersonaBD.setSegundoApellido("");
											}
											if(elementPer.getPersonas().getFechaNacimiento() != null){
												dtoPersonaBD.setFechaNacimiento(format.format(elementPer.getPersonas().getFechaNacimiento()));
											}
											else{
												dtoPersonaBD.setFechaNacimiento("");
											}
											listaPersonas.add(dtoPersonaBD);
										}
										dtoInconsisPersonas.setListaPersonas(listaPersonas);
									}
									inconsistenciasPersonas.add(dtoInconsisPersonas);
								}
							}
						}
					}
				}
				forma.setListaInconsistenciasGenerales(inconsistenciasGenerales);
				forma.setListaInconsistenciasCarguePrevio(inconsistenciasCarguePrevio);
				Collections.sort(inconsistenciasCampos);
				forma.setListaInconsitenciasCampos(inconsistenciasCampos);
				forma.setListaInconsitenciasPersonas(inconsistenciasPersonas);
			}
			else{
				errores.add("Error Consultando Log", new ActionMessage("errors.notEspecific", "No existen inconsistencias asociadas al log: "+forma.getCodigoPkSubirPaciente()));
				saveErrors(request, errores);
	            forma.setEstado("buscando");
			}
		}
		catch(Exception e)
		{
			errores.add("Error Consultando Log", new ActionMessage("errors.notEspecific", e.getMessage()));
			saveErrors(request, errores);
            logger.error(e);
            forma.setEstado("buscando");
		}
		finally{
			UtilidadTransaccion.getTransaccion().commit();
		}
		return mapping.findForward("detalle");
	}
}