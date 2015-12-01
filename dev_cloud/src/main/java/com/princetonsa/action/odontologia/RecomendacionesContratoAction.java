package com.princetonsa.action.odontologia;

import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;
import util.UtilidadTexto;

import com.princetonsa.actionform.odontologia.RecomendacionesContratoForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.sort.odontologia.recomendaciones.SortRecomendaciones;
import com.servinte.axioma.orm.Instituciones;
import com.servinte.axioma.orm.RecomendacionesContOdonto;
import com.servinte.axioma.servicio.fabrica.odontologia.recomendacion.RecomendacionSERVICIOFabrica;
import com.servinte.axioma.servicio.interfaz.odontologia.recomendacion.IRecomendacionesContOdontoServicio;
import com.servinte.axioma.vista.odontologia.recomendaciones.RecomendacionesContView;

/**
 * 
 * @author EDGAR CARVAJAL
 *
 */

public class RecomendacionesContratoAction extends Action
{
	

	/**
	 * INSTANCIA VIEW HELPER RECOMENDACION
	 */
	RecomendacionesContView viewHelperRecomendacion = new RecomendacionesContView();
	
	/**
	 * 
	 */
	private final IRecomendacionesContOdontoServicio servicioRecomendacionI = RecomendacionSERVICIOFabrica.crearRecomendacionesCont();
	
	
	
	
	/**
	 * EXECUTE
	 */
	public ActionForward execute(	ActionMapping mapping, ActionForm form, HttpServletRequest request, 	HttpServletResponse response) throws Exception
	{
		
		/*
		 *INSTANCIA DE USUARIO BASICO 
		 */
		UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
		ActionErrors errores = new ActionErrors();
		
		
		/**
		 * 
		 */
		if(form instanceof RecomendacionesContratoForm)
		{
			
			
			RecomendacionesContratoForm forma = (RecomendacionesContratoForm)form;
			
			
			if(forma.getEstado().equals("empezar"))
			{
				
				forma.reset();
				forma.setListaRecomendaciones(viewHelperRecomendacion.cargarRecomendaciones(usuario)) ;
				return mapping.findForward("paginaPrincipal");
			}
			
			
			else if( forma.getEstado().equals("nuevo"))
			{
				forma.setEstado("nuevo");
				forma.getDtoRecomendaciones().setActivo(ConstantesBD.acronimoSi);
				return mapping.findForward("paginaPrincipal");
			}
			
			
			else if(forma.getEstado().equals("guardar"))
			{
				accionGuardarRecomendaciones(request, usuario, forma, errores);
				return mapping.findForward("paginaPrincipal");
			}
			
			else if(forma.getEstado().equals("modificar"))
			{
				forma.setDtoRecomendaciones(forma.getListaRecomendaciones().get(forma.getPostArray()));
				forma.setEstado("modificar");
				return mapping.findForward("paginaPrincipal");
			}
			
			
			else if(forma.getEstado().equals("guardarModificar"))
			{
				accionModificarRecomendaciones(request, usuario, forma);
				return mapping.findForward("paginaPrincipal");
			}
			
			
			else if(forma.getEstado().equals("eliminar"))
			{
				accionEliminarRecomendaciones(request, usuario, forma);
				return mapping.findForward("paginaPrincipal");
			}
			
			else if(forma.getEstado().equals("busquedaRecomen"))
			{
				accionBuscarRecomendaciones(usuario, forma);
				return mapping.findForward("consultarRecomen");
			}
			
			else if(forma.getEstado().equals("ordenar"))
			{
				accionOrdenar(forma);
				return mapping.findForward("paginaPrincipal");
			}
			
		}
				
		
		
		
		return null;
				
				
	}




	private void accionOrdenar(RecomendacionesContratoForm forma) {
		/*
		 *	ORDENAMIENTO DE LAS LISTA  
		 */
		Collections.sort(forma.getListaRecomendaciones(), new  SortRecomendaciones(forma.getPatronOrdenar()) );
	}




	private void accionBuscarRecomendaciones(UsuarioBasico usuario,
			RecomendacionesContratoForm forma) {
		forma.reset();
		forma.setDtoRecomendaciones(new RecomendacionesContOdonto());
		forma.getDtoRecomendaciones().setInstituciones(new Instituciones());
		forma.getDtoRecomendaciones().getInstituciones().setCodigo(usuario.getCodigoInstitucionInt());
		ArrayList<Integer> listaRecomenTMP =UtilidadTexto.convertirStringArrayList(forma.getListaCodigosRecomendacion());
		forma.setListaDtoRecom(servicioRecomendacionI.listarRecomendaciones(forma.getDtoRecomendaciones(), listaRecomenTMP));
	}




	private void accionEliminarRecomendaciones(HttpServletRequest request,
												UsuarioBasico usuario, 
												RecomendacionesContratoForm forma) {
		ActionErrors errores;
		errores= viewHelperRecomendacion.eliminarRecomendacion(forma);
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);//enviar errores	
		}
		
		accionEmpezar(usuario, forma);
	}




	private void accionModificarRecomendaciones(HttpServletRequest request,
			UsuarioBasico usuario, RecomendacionesContratoForm forma) {
		ActionErrors errores;
		errores= viewHelperRecomendacion.modificarRecomendacion(usuario, forma);

		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			forma.setEstado("errores");
		}
		else
		{
			accionEmpezar(usuario, forma);
		}
	}




	/**
	 * GUARDAR RECOMENACIONES 
	 * @author Edgar Carvajal Ruiz
	 * @param request
	 * @param usuario
	 * @param forma
	 */
	private void accionGuardarRecomendaciones(HttpServletRequest request,UsuarioBasico usuario, RecomendacionesContratoForm forma ,ActionErrors errores) {
		
		/*
		 *GUARDAR EL ERROR 
		 */
		errores=viewHelperRecomendacion.guardarRecomendacion(usuario, forma);
		
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			forma.setEstado("errores");
		}
		else
		{
			forma.reset();
			forma.setListaRecomendaciones(viewHelperRecomendacion.cargarRecomendaciones(usuario)) ;
			forma.setEstado("resumen");
		}
		
		
	}

	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param usuario
	 * @param forma
	 */
	private void accionEmpezar(UsuarioBasico usuario,RecomendacionesContratoForm forma) 
	{
		forma.reset();
		forma.setListaRecomendaciones(viewHelperRecomendacion.cargarRecomendaciones(usuario)) ;
		forma.setEstado("empezar");
	}
		
	

}
