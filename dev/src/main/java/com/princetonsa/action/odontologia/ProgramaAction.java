package com.princetonsa.action.odontologia;


import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.actionform.odontologia.ProgramaForm;
import com.princetonsa.dto.odontologia.DtoPrograma;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.odontologia.Programa;
import com.princetonsa.sort.odontologia.SortProgramasOdontologicos;



/**
 * 
 * @author axioma
 *
 */
public class ProgramaAction extends Action {
	
	private Logger logger = Logger.getLogger(ProgramaAction.class);
			
	public ActionForward execute(	ActionMapping mapping,
		 	ActionForm form,
		 	HttpServletRequest request,
		 	HttpServletResponse response) throws Exception
		 	{
		
			request.setAttribute("programaEncontrado", "false");
		
				if(form instanceof ProgramaForm)
				{
				ProgramaForm forma = (ProgramaForm)form;	
				UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
	
				
				if(forma.getEstado().equals("empezar"))
		    	{
					return accionEmpezar(mapping, forma);
		    	}
				
				if(forma.getEstado().equals("nuevo"))
				{
					return accionNuevo(mapping, forma);
				}
				
				else
		
				if(forma.getEstado().equals("guardar"))
					{
					return accionGuardar( mapping, forma, usuario);
					}

				else
		
				if(forma.getEstado().equals("guardarModificar"))
				{	
					return accionGurdarModificar(mapping, forma, usuario);
				}
				else 
							
				if(forma.getEstado().equals("modificar"))
				{
					return accionModificar(mapping, forma);	
				}

				else 
					if(forma.getEstado().equals("eliminar"))
					{
					return	accionEliminar(forma , mapping);
					}
				else if(forma.getEstado().equals("busquedaAvanzadaPrograma"))
				{
					return accionBusquedaAvanzadaPrograma(forma , mapping);
				}
				else if(forma.getEstado().equals("consultarProgramas"))
					{
					 return accionConsultarBusquedaPrograma(forma, mapping, request);
					}
				else if( forma.getEstado().equals("ordenarAvanzada"))
				{
					
					logger.info("ORDENAR AVANZADADO PROGRAMAS");
					return accionOrdenarAvanzada(mapping, forma, usuario);
					
				}
		
				}
		
				return null;
				
		 	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionModificar(ActionMapping mapping,
			ProgramaForm forma) {
		forma.setDtoProgramas(forma.getListaPrograma().get(forma.getPosArray()));
		return mapping.findForward("paginaPrincipal");
	}

	/**
	 * 
	 * @param forma
	 */
	private ActionForward accionEliminar(ProgramaForm forma, ActionMapping mapping) {
		DtoPrograma dtoEliminar = new DtoPrograma();
		dtoEliminar.setCodigo(forma.getListaPrograma().get(forma.getPosArray()).getCodigo());
		Programa.eliminar(dtoEliminar);
		 return accionEmpezar(mapping, forma);	
	
	}
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionGurdarModificar(ActionMapping mapping,
			ProgramaForm forma,  UsuarioBasico usuario) {
		forma.getDtoProgramas().setCodigo(forma.getListaPrograma().get(forma.getPosArray()).getCodigo());
		forma.getDtoProgramas().setFechaModifica(UtilidadFecha.getFechaActual());
		forma.getDtoProgramas().setHoraModifica(UtilidadFecha.getHoraActual());
		forma.getDtoProgramas().setUsuarioModifica(usuario.getLoginUsuario());
		Programa.modificar(forma.getDtoProgramas());
		forma.reset();
		forma.setListaPrograma(Programa.cargar(forma.getDtoProgramas()));
		forma.setEstado("resumen");
		 return accionEmpezar(mapping, forma);	
	}
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward  accionGuardar(ActionMapping mapping,ProgramaForm forma, UsuarioBasico usuario) {
		forma.getDtoProgramas().setFechaModifica(UtilidadFecha.getFechaActual());
		forma.getDtoProgramas().setHoraModifica(UtilidadFecha.getHoraActual());
		forma.getDtoProgramas().setUsuarioModifica(usuario.getLoginUsuario());
		Programa.guardar(forma.getDtoProgramas());
		forma.reset();
		forma.setListaPrograma(Programa.cargar(forma.getDtoProgramas()));
		forma.setEstado("resumen");
		logger.info("Estado--->"+forma.getEstado());
		return mapping.findForward("paginaPrincipal");
		
	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionNuevo(ActionMapping mapping, ProgramaForm forma) {
		forma.setEstado("nuevo");
		return mapping.findForward("paginaPrincipal");
	}
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionEmpezar(ActionMapping mapping,
			ProgramaForm forma) {
		forma.reset();
		forma.setListaPrograma(Programa.cargar(forma.getDtoProgramas()));
		return mapping.findForward("paginaPrincipal");
	}
	
	
	
	/**
	 * CARGAR POPUP DEL LA BUSQUEDA AVANZADA DE PROGRAMAS
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward accionBusquedaAvanzadaPrograma(ProgramaForm forma, ActionMapping mapping)
	{
		logger.info("*******************************************************");
		logger.info("*************************	CARGAR POPUP PROGRAMAS ******************************");
		
		boolean incluirInactivos= forma.isIncluirInactivos();
		forma.reset();
		forma.setIncluirInactivos(incluirInactivos);
		return mapping.findForward("busquedaAvanzadaPrograma");	
	}
	
	
	
	
	/**
	 * CARGAR BUSQUEDAD AVANZADA DEL SISTEMA 
	 * @param request 
	 * @return
	 */
	private ActionForward accionConsultarBusquedaPrograma(ProgramaForm forma, ActionMapping mapping, HttpServletRequest request)
	{
		logger.info("*********************************************");
		logger.info(" CARGAR PROGRAMAS ");
		logger.info("Codigo <<<>>>"+forma.getCodigoHallazgos());
		logger.info("ESPECIALIDAD "+forma.getCodigoEspecialidad());
		boolean esPorCriterio= false;
		
		if (forma.getDtoProgramas() != null) {
			forma.getDtoProgramas().setYaSeleccionado(forma.getCodigoHallazgos());
		}
		
		if(forma.getCodigoEspecialidad()>0)
		{
			forma.getDtoProgramas().setEspecialidad(forma.getCodigoEspecialidad());
			logger.info("BUSCAR POR LA ESPECIALIDAD "+forma.getDtoProgramas().getEspecialidad());
		}
		
		if (!UtilidadTexto.isEmpty(forma.getCriterioBusquedaPorCodigo())) {
			forma.setDtoProgramas(new DtoPrograma());
			forma.getDtoProgramas().setCodigoPrograma(forma.getCriterioBusquedaPorCodigo());
			forma.setCriterioBusquedaPorCodigo("");
			esPorCriterio = true;
		}else if (!UtilidadTexto.isEmpty(forma.getCriterioBusquedaPorDescripcion())) {
			forma.setDtoProgramas(new DtoPrograma());
			forma.getDtoProgramas().setNombre(forma.getCriterioBusquedaPorDescripcion());
			esPorCriterio = true;
			forma.setCriterioBusquedaPorDescripcion("");
		}
		
		
		forma.setListaPrograma(Programa.cargarAvanzado(forma.getDtoProgramas(), forma.isIncluirInactivos()));
		
		if (forma.getListaPrograma() != null && forma.getListaPrograma().size() > 0 && esPorCriterio) {
			request.setAttribute("programaEncontrado", "true");
			request.setAttribute("codigo", forma.getListaPrograma().get(0).getCodigo());
			request.setAttribute("nombre", forma.getListaPrograma().get(0).getNombre());
			request.setAttribute("especialidad", forma.getListaPrograma().get(0).getEspecialidad());
			request.setAttribute("codigoMostrar", forma.getListaPrograma().get(0).getCodigoPrograma());
		}else{
			request.setAttribute("programaEncontrado", "false");
		}
		
		return mapping.findForward("busquedaAvanzadaPrograma");	
	}
	
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionOrdenarAvanzada(ActionMapping mapping,
			ProgramaForm forma, UsuarioBasico usuario) {
			
		SortProgramasOdontologicos sort= new SortProgramasOdontologicos();
		sort.setPatronOrdenar(forma.getPatronOrdenar());
		Collections.sort(forma.getListaPrograma(), sort);
		forma.setEstado(forma.getEstadoAnterior());
		return mapping.findForward("busquedaAvanzadaPrograma");
		
	}

	

}
