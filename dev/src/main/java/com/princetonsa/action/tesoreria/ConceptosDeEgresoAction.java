/*
 * Marzo 24, 2010
 */
package com.princetonsa.action.tesoreria;

import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.UtilidadFecha;

import com.princetonsa.actionform.tesoreria.ConceptosDeEgresoForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.ConcEgrXusuXcatencion;
import com.servinte.axioma.orm.ConceptosDeEgreso;
import com.servinte.axioma.orm.InstitucionesHome;
import com.servinte.axioma.orm.delegate.UsuariosDelegate;
import com.servinte.axioma.orm.delegate.tesoreria.ConcEgrXusuXcatencionDelegate;
import com.servinte.axioma.orm.delegate.tesoreria.ConceptosDeEgresoDelegate;


/**
 * @author Cristhian Murillo
 *
 * Clase usada para controlar los procesos de la funcionalidad .
 * 
 */
public class ConceptosDeEgresoAction extends Action 
{
	
	
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	private Logger logger = Logger.getLogger(ConceptosDeEgresoAction.class);
	
	
	
	/**
	 * M&eacute;todo encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, 
	 * HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
								HttpServletResponse response )throws Exception
	{

		if(form instanceof ConceptosDeEgresoForm)
		{
			/* OBJETOS A USAR */
			/**
			 * Formulario
			 */
			ConceptosDeEgresoForm forma = (ConceptosDeEgresoForm)form;
			
			/** 
			 * Usuario
			 */
			UsuarioBasico usuario 		= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			
			String estado = forma.getEstado(); 
			logger.warn("Estado ConceptosDeEgresoAction --> "+estado);
			
			if(estado == null)
			{
				request.setAttribute("codigoDescripcionError","errors.estadoInvalido");
				return mapping.findForward("paginaError");
			}
			
			// empezar
			else if( (estado.equals("empezar")) || (estado.equals("resumen")) )
			{
				return accionEmpezar(mapping, forma, usuario);
			}
			
			// nuevo
			else if(estado.equals("nuevo"))
			{
				return accionNuevo(mapping, forma, usuario);
			}
			
			
			// guardar
			else if(estado.equals("guardar"))
			{
				return accionGuardar(mapping, forma, usuario, request);
			}
			
			// ordenar
			else if(estado.equals("ordenar"))
			{
				return accionOrdenar(mapping, forma, usuario);
			}
			
			// eliminar
			else if(estado.equals("eliminar"))
			{
				return accionEliminar(mapping, forma, usuario, request);
			}
			
			// modificar
			else if(estado.equals("modificar"))
			{
				return accionModificar(mapping,forma, usuario, request);
			}
			
			// guardarmodificar
			else if(estado.equals("guardarmodificar"))
			{
				return accionGuardarModificar(mapping, forma, usuario, request);
			}
			
			// volver
			else if(estado.equals("volver"))
			{
				return accionEmpezar(mapping,forma, usuario);
			}
						
		}
		return null;
	}
	
	
	
	



	/**
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezar(ActionMapping mapping, ConceptosDeEgresoForm forma, UsuarioBasico usuario)
	{
		forma.reset();
		mostrarLista(forma, usuario);
		return mapping.findForward("principal");
	}
		
	
	
	/**
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionNuevo(ActionMapping mapping, ConceptosDeEgresoForm forma, UsuarioBasico usuario)
	{
		forma.reset();
		forma.setMostrarFormularioIngreso(true);
		mostrarLista(forma, usuario);
		return mapping.findForward("principal");
	}
	
	
	
	/**
	 * @param mapping
	 * @param forma
	 * @param request 
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardar(ActionMapping mapping,	ConceptosDeEgresoForm forma, UsuarioBasico usuario, HttpServletRequest request)
	{
		if(validarCamposUnicos(forma, request))
		{
			llenarDetallesForma(forma, usuario);
			new ConceptosDeEgresoDelegate().persist(forma.getDto());
			HibernateUtil.endTransaction();
			forma.reset();
			forma.setEstado("resumen");
			mostrarLista(forma, usuario);
		}
		
		return mapping.findForward("principal");
	}
	
	

	/**
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionOrdenar(ActionMapping mapping,	ConceptosDeEgresoForm forma, UsuarioBasico usuario) 
	{
		logger.info("patron-> " + forma.getPatronOrdenar());
		logger.info("des --> " + forma.getEsDescendente() );
		
		boolean ordenamiento= false;
		
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente"))
		{
			ordenamiento = true;
		}
		
		SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		Collections.sort(forma.getListaDto(),sortG);
		return mapping.findForward("principal");
	}
	
	
	
	
	/**
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEliminar(ActionMapping mapping, ConceptosDeEgresoForm forma, UsuarioBasico usuario, HttpServletRequest request) 
	{
		forma.setDto(forma.getListaDto().get(forma.getPosArray()));
		
		if(esEliminableModificable(forma, usuario))
		{
			new ConceptosDeEgresoDelegate().delete(forma.getDto());
			HibernateUtil.endTransaction();
			forma.reset();
			mostrarLista(forma, usuario);		
			forma.setEstado("resumen");
		}
		else{
			mostraErrorEliminableModificable(forma, request);
		}
		
		return accionEmpezar(mapping,forma, usuario);
	}
	
	
	

	/**
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request 
	 * @return
	 */
	private ActionForward accionModificar(ActionMapping mapping, ConceptosDeEgresoForm forma, UsuarioBasico usuario, HttpServletRequest request) 
	{
		forma.setDto(forma.getListaDto().get(forma.getPosArray()));
		if(esEliminableModificable(forma, usuario))
		{
			mostrarLista(forma, usuario);
			forma.setMostrarFormularioIngreso(true);
		}
		else{
			mostraErrorEliminableModificable(forma, request);
		}
		
		return mapping.findForward("principal");
	}
	
	
	
	
	/**
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request 
	 * @return
	 */
	private ActionForward accionGuardarModificar(ActionMapping mapping, ConceptosDeEgresoForm forma, UsuarioBasico usuario, HttpServletRequest request) 
	{
		if(validarCamposUnicosOtraInstancia(forma, request))
		{
			llenarDetallesForma(forma, usuario);
			new ConceptosDeEgresoDelegate().merge(forma.getDto());
			HibernateUtil.endTransaction();
			forma.reset();
			forma.setEstado("resumen");
			mostrarLista(forma, usuario);		
		}
		
		return mapping.findForward("principal");
	}
	
	



	/**
	 * Ingresa en el dto los datos de usuario
	 * @param usuario
	 * @param forma
	 */
	private void llenarDetallesForma(ConceptosDeEgresoForm forma, UsuarioBasico usuario)
	{
		forma.getDto().setUsuarios(new UsuariosDelegate().findById(usuario.getLoginUsuario()));
		forma.getDto().setFechaModifica(UtilidadFecha.getFechaActualTipoBD());
		forma.getDto().setHoraModifica(UtilidadFecha.getHoraActual());
		forma.getDto().setInstituciones(new InstitucionesHome().findById(usuario.getCodigoInstitucionInt()));
	}
	
	
	
	/** 
	 * Carga una lista de todos los diagnosticos por institucion
	 * @param usuario
	 * @param forma
	 */
	private void mostrarLista(ConceptosDeEgresoForm forma, UsuarioBasico usuario) 
	{
		ArrayList<ConceptosDeEgreso> listaConceptos=new ConceptosDeEgresoDelegate()
		.listarTodosPorInstitucion(Integer.parseInt(usuario.getCodigoInstitucion()));
		
		// Traigo todo lo que necesito
		for(ConceptosDeEgreso concepto: listaConceptos)
		{
			if(concepto.getCuentasContables() != null){
				concepto.getCuentasContables().getAnioVigencia();
				concepto.getCuentasContables().getCuentaContable();
			}
		}
		
		forma.setListaDto(listaConceptos);
		
		HibernateUtil.endTransaction();
	}
	

	
	
	/**
	 * 	Valida que los campos unicos de la tabla no sean repetidos por otro dto
	 * 	Retorna true si los datos son validos
	 * @param forma
	 * @param request 
	 * 
	 */
	private boolean validarCamposUnicos(ConceptosDeEgresoForm forma, HttpServletRequest request)
	{
		ConceptosDeEgresoDelegate delegate 	= new ConceptosDeEgresoDelegate();
		ActionErrors errores 				= new ActionErrors();
		
				
		if(delegate.buscarPorCampo("codigo", forma.getDto().getCodigo()) != null)
		{
			errores.add("error campo repetido", new ActionMessage("errors.yaExisteCampoEnTabla", "c&oacute;digo"));
		}

		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			return false;
		}
		
		return true;
	}

	
	
	
	/**
	 * 	Valida que los campos unicos de la tabla no sean repetidos
	 * 	Solo permite que el mismo dto pueda actualizar los campos
	 *  (Aplica para el caso de que se quiera actualizar campos unicos del modelo)
	 *  @param forma 
	 *  @param request 
	 * 	@return true si los datos son validos
	 */
	private boolean validarCamposUnicosOtraInstancia(ConceptosDeEgresoForm forma, HttpServletRequest request)
	{
		ConceptosDeEgresoDelegate delegate 	= new ConceptosDeEgresoDelegate();
		ActionErrors errores 				= new ActionErrors();
		ConceptosDeEgreso instanciaBD		= new ConceptosDeEgreso();
		
		instanciaBD = delegate.buscarPorCampo("codigo", forma.getDto().getCodigo());
		if(instanciaBD != null)
		{
			/* Se verifica si es una isntancia distinta */
			if(instanciaBD.getCodigoPk() != forma.getDto().getCodigoPk())
			{
				errores.add("error campo repetido", new ActionMessage("errors.yaExisteCampoEnTabla", "c&oacute;digo"));
			}
		}
		
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			return false;
		}
		return true;
	}
	
	
	
	
	
	/**
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private boolean esEliminableModificable (ConceptosDeEgresoForm forma, UsuarioBasico usuario)
	{
		ArrayList<ConcEgrXusuXcatencion> listaConEgresoX = new ConcEgrXusuXcatencionDelegate().listarTodos();
		
		Boolean posible = true;
		for(ConcEgrXusuXcatencion conEgresoX : listaConEgresoX)
		{
			if(forma.getDto().getCodigoPk() == conEgresoX.getConceptosDeEgreso().getCodigoPk()){
				posible = false;
			}
		}
		
		return posible;
	}
	
	
	/**
	 * Si no se puede eliminar/modificar un registro muestra una pagina de error
	 * @param forma
	 * @param request
	 */
	private void mostraErrorEliminableModificable(ConceptosDeEgresoForm forma, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		errores.add("error registro_relacionado", new ActionMessage("errors.noEliminableModificable", "Concepto de Egreso por Usuario por Centro de Atenci&oacute;n"));
		saveErrors(request, errores);
		forma.setEstado("empezar");
	}
}	
	
	
	