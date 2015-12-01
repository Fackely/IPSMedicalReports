/*
 * Abril 08, 2010
 */
package com.princetonsa.action.tesoreria;

import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.actionform.tesoreria.ConcEgrXusuXcatencionForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.CentroAtencionHome;
import com.servinte.axioma.orm.ConcEgrXusuXcatencion;
import com.servinte.axioma.orm.ConceptosDeEgreso;
import com.servinte.axioma.orm.HistConcEgrXusuXcatencion;
import com.servinte.axioma.orm.RegistroEgresosDeCaja;
import com.servinte.axioma.orm.TiposUsuarios;
import com.servinte.axioma.orm.delegate.UsuariosDelegate;
import com.servinte.axioma.orm.delegate.administracion.CentroAtencionDelegate;
import com.servinte.axioma.orm.delegate.odontologia.TiposUsuariosDelegate;
import com.servinte.axioma.orm.delegate.tesoreria.ConcEgrXusuXcatencionDelegate;
import com.servinte.axioma.orm.delegate.tesoreria.ConceptosDeEgresoDelegate;
import com.servinte.axioma.orm.delegate.tesoreria.HistConcEgrXusuXcatencionDelegate;
import com.servinte.axioma.orm.delegate.tesoreria.RegistroEgresosDeCajaDelegate;


/**
 * @author Cristhian Murillo
 *
 * Clase usada para controlar los procesos de la funcionalidad .
 * 
 */
public class ConcEgrXusuXcatencionAction extends Action 
{
	
	/**
	 * M&eacute;todo encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, 
	 * HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
								HttpServletResponse response )throws Exception
	{

		if(form instanceof ConcEgrXusuXcatencionForm)
		{
			/* OBJETOS A USAR */
			/**
			 * Formulario
			 */
			ConcEgrXusuXcatencionForm forma = (ConcEgrXusuXcatencionForm)form;
			
			/** 
			 * Usuario
			 */
			UsuarioBasico usuario 		= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			
			String estado = forma.getEstado(); 
			Log4JManager.info("Estado ConcEgrXusuXcatencionAction --> "+estado); 
			
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
	private ActionForward accionEmpezar(ActionMapping mapping, ConcEgrXusuXcatencionForm forma, UsuarioBasico usuario)
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
	private ActionForward accionNuevo(ActionMapping mapping, ConcEgrXusuXcatencionForm forma, UsuarioBasico usuario)
	{
		forma.reset();
		forma.setMostrarFormularioIngreso(true);
		mostrarLista(forma, usuario);
		llenarListaConceptosEgreso(forma, usuario);
		llenarListaTiposUsuarios(forma, usuario);

		return mapping.findForward("principal");
	}
	
	
	
	/**
	 * @param mapping
	 * @param forma
	 * @param request 
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardar(ActionMapping mapping,	ConcEgrXusuXcatencionForm forma, UsuarioBasico usuario, HttpServletRequest request)
	{
		if(validarCamposUnicos(forma, request))
		{
			llenarDetallesForma(forma, usuario);
			new ConcEgrXusuXcatencionDelegate().persist(forma.getDto());
			HibernateUtil.endTransaction();
			guardarHistorico(forma, usuario);	// GUARDANDO EL HISTORICO DE LO REALIZADO
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
	private ActionForward accionOrdenar(ActionMapping mapping,	ConcEgrXusuXcatencionForm forma, UsuarioBasico usuario) 
	{
		boolean ordenamiento = false;
		
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
	 * @param request
	 * @return
	 */
	private ActionForward accionEliminar(ActionMapping mapping, ConcEgrXusuXcatencionForm forma, UsuarioBasico usuario, HttpServletRequest request) 
	{
		forma.setDto(forma.getListaDto().get(forma.getPosArray()));
		
		if(esEliminableModificable(forma, usuario))
		{
			new ConcEgrXusuXcatencionDelegate().delete(forma.getDto());
			HibernateUtil.endTransaction();
			guardarHistorico(forma, usuario);	// GUARDANDO EL HISTORICO DE LO REALIZADO
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
	private ActionForward accionModificar(ActionMapping mapping, ConcEgrXusuXcatencionForm forma, UsuarioBasico usuario, HttpServletRequest request) 
	{
		forma.setDto(forma.getListaDto().get(forma.getPosArray()));
		if(esEliminableModificable(forma, usuario))
		{
			llenarListaConceptosEgreso(forma, usuario);
			llenarListaTiposUsuarios(forma, usuario);
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
	private ActionForward accionGuardarModificar(ActionMapping mapping, ConcEgrXusuXcatencionForm forma, UsuarioBasico usuario, HttpServletRequest request) 
	{
		if(validarCamposUnicosOtraInstancia(forma, request))
		{
			llenarDetallesForma(forma, usuario);
			new ConcEgrXusuXcatencionDelegate().merge(forma.getDto());
			HibernateUtil.endTransaction();
			guardarHistorico(forma, usuario);	// GUARDANDO EL HISTORICO DE LO REALIZADO
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
	private void llenarDetallesForma(ConcEgrXusuXcatencionForm forma, UsuarioBasico usuario)
	{
		forma.getDto().setUsuarios(new UsuariosDelegate().findById(usuario.getLoginUsuario()));
		forma.getDto().setFechaModifica(UtilidadFecha.getFechaActualTipoBD());
		forma.getDto().setHoraModifica(UtilidadFecha.getHoraActual());
		forma.getDto().setCentroAtencion(new CentroAtencionHome().findById(usuario.getCodigoCentroAtencion()));
	}
	
	
	
	/** 
	 * Carga una lista de artefactos necesarios en la forma
	 * @param usuario
	 * @param forma
	 */
	private void mostrarLista(ConcEgrXusuXcatencionForm forma, UsuarioBasico usuario) 
	{
		ArrayList<ConcEgrXusuXcatencion> listaConcEgrXusuXcatencion = new ArrayList<ConcEgrXusuXcatencion>();
		
		if(UtilidadTexto.isEmpty(forma.getStrCentroAtencion()))
		{
			listaConcEgrXusuXcatencion = new ConcEgrXusuXcatencionDelegate()
			.listarActivosYconcepEgresoActivoPorInstitucion(Integer.parseInt(usuario.getCodigoInstitucion()));
		}
		else 
		{
			listaConcEgrXusuXcatencion = new ConcEgrXusuXcatencionDelegate()
			.listarActivosYconcepEgresoActivoPorInstitucionYCentroAtencion(Integer.parseInt(usuario.getCodigoInstitucion()), 
					Integer.parseInt(forma.getStrCentroAtencion()));
		}
		
		
		for(ConcEgrXusuXcatencion objList: listaConcEgrXusuXcatencion)
		{
			objList.getConceptosDeEgreso().getCodigo();
			objList.getConceptosDeEgreso().getDescripcion();
			objList.getTiposUsuarios().getCodigo();
			objList.getTiposUsuarios().getDescripcion();
		}

		forma.setListaDto(listaConcEgrXusuXcatencion);
		listaConcEgrXusuXcatencion = null;
		HibernateUtil.endTransaction();
		
		llenarListaHistorico (forma, usuario);
		llenarListaCentroAtencion(forma, usuario);
	}
	
	
	
	/** 
	 * Carga una lista de artefactos necesarios en la forma
	 * @param usuario
	 * @param forma
	 */
	private void llenarListaConceptosEgreso(ConcEgrXusuXcatencionForm forma, UsuarioBasico usuario) 
	{
		// listaConceptosEgreso
		ArrayList<ConceptosDeEgreso> listaConceptosEgreso = new ConceptosDeEgresoDelegate()
			.listarActivosPorInstitucion(Integer.parseInt(usuario.getCodigoInstitucion()));
		for(ConceptosDeEgreso objList: listaConceptosEgreso)
		{
			objList.getCodigoPk();
			objList.getCodigo();
			objList.getDescripcion();
		}

		forma.setListaConceptosEgreso(listaConceptosEgreso);
		listaConceptosEgreso = null;
		HibernateUtil.endTransaction();
	}
	

	
	/** 
	 * Carga una lista de artefactos necesarios en la forma
	 * @param usuario
	 * @param forma
	 */
	private void llenarListaCentroAtencion(ConcEgrXusuXcatencionForm forma, UsuarioBasico usuario) 
	{
		// listaCentroAtencion
		ArrayList<CentroAtencion> listaCentroAtencion = new CentroAtencionDelegate()
			.listarTodosActivosPorInstitucion(Integer.parseInt(usuario.getCodigoInstitucion()));
		for(CentroAtencion objList: listaCentroAtencion)
		{
			objList.getConsecutivo();
			objList.getCodigo();
			objList.getDescripcion();
		}

		forma.setListaCentroAtencion(listaCentroAtencion);
		listaCentroAtencion = null;
		HibernateUtil.endTransaction();
	}
	
	
	
	/** 
	 * Carga una lista de artefactos necesarios en la forma
	 * @param usuario
	 * @param forma
	 */
	private void llenarListaTiposUsuarios(ConcEgrXusuXcatencionForm forma, UsuarioBasico usuario) 
	{
		// se genere un vector con las etiquetas a mostrar
		String[] listaConstantesStr = { 
				// Traigo solo acronimos de generacion y autorizacion de Egreso
				ConstantesIntegridadDominio.acronimoActividadTipoUsuarioGenSolEgreso,
				ConstantesIntegridadDominio.acronimoActividadTipoUsuarioAutoSolEgreso
		};

		ArrayList<TiposUsuarios> listaTiposUsuarios = new TiposUsuariosDelegate().
			listarTodosXActividadesXInstitucion(listaConstantesStr, Integer.parseInt(usuario.getCodigoInstitucion()));
		
		forma.setListaTiposUsuarios(listaTiposUsuarios);
		
	}
	
	
	
	/**
	 * 	Valida que los campos unicos de la tabla no sean repetidos por otro dto
	 * 	Retorna true si los datos son validos
	 * @param forma
	 * @param request 
	 * 
	 */
	private boolean validarCamposUnicos(ConcEgrXusuXcatencionForm forma, HttpServletRequest request)
	{
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
	private boolean validarCamposUnicosOtraInstancia(ConcEgrXusuXcatencionForm forma, HttpServletRequest request)
	{
		return true;
	}
	
	
	/**
	 * Crea un registro en la base de datos para manejar un historico de cambios
	 * @param forma
	 * @param usuario
	 */
	private void guardarHistorico(ConcEgrXusuXcatencionForm forma, UsuarioBasico usuario)
	{
		/**
		 * Define la accion que se ejecuto en el formulario para que sea guardado el historico 
		 */
		String accRealizada = null;
		
		if (forma.getEstado().equals("guardar")){				//INS
			accRealizada = ConstantesIntegridadDominio.acronimoAccionHistoricaInsertar;	
		}
		else if (forma.getEstado().equals("guardarmodificar")){	//MDF
			accRealizada = ConstantesIntegridadDominio.acronimoAccionHistoricaModificar;	
		}
		else if (forma.getEstado().equals("eliminar")){			//ELM
			accRealizada = ConstantesIntegridadDominio.acronimoAccionHistoricaEliminar;
		}
		else {													//NDF
			// Esta parte es para evitar errores
			accRealizada = ConstantesIntegridadDominio.acronimoAccionNoDefinida;
		}
		
		ConcEgrXusuXcatencion		obj	 = forma.getDto();
		HistConcEgrXusuXcatencion 	hist = new HistConcEgrXusuXcatencion(
				0,											// pk - enviar 0. Por defecto toma la secuencia
				obj.getCentroAtencion(),					// Centro Atencion
				obj.getUsuarios(),							// Usuario
				obj.getConceptosDeEgreso().getCodigo()		// Concepto Egreso
				+"-"+obj.getConceptosDeEgreso().getDescripcion(),
				obj.getFechaInicialVigencia(),				// Fecha Inicial Vigencia
				obj.getFechaFinalVigencia(),				// Fecha Final vigencia
				obj.getValorMaximoAutorizado(),				// Valor Maximo autorizado
				obj.getActivo(),							// Activo
				obj.getTiposUsuarios().getRoles().getNombreRol()	// Tipo Usuario autoriza
				+"-"+obj.getTiposUsuarios().getDescripcion(),	
				obj.getFechaModifica(),						// Fecha Modifica
				obj.getHoraModifica(),						// Hora Modifica
				accRealizada       							// Accion Realizada	- verificar la contante
			);
		hist.setPeriodicidadDias(obj.getPeriodicidadDias());
		
		new HistConcEgrXusuXcatencionDelegate().persist(hist);
		HibernateUtil.endTransaction();
	}
	
	
	
	/**
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private boolean esEliminableModificable (ConcEgrXusuXcatencionForm forma, UsuarioBasico usuario)
	{
		ArrayList<RegistroEgresosDeCaja> listaRegEgreso = new RegistroEgresosDeCajaDelegate().listarTodos();
		
		Boolean posible = true;
		for(RegistroEgresosDeCaja rEgreso : listaRegEgreso)
		{
			if(forma.getDto().getCodigoPk() == rEgreso.getConcEgrXusuXcatencion().getCodigoPk()){
				posible = false;
			}
		}
		
		return posible;
	}
	
	
	
	
	
	
	
	/** 
	 * Carga una lista de artefactos necesarios en la forma
	 * @param usuario
	 * @param forma
	 */
	private void llenarListaHistorico(ConcEgrXusuXcatencionForm forma, UsuarioBasico usuario) 
	{
		ArrayList<HistConcEgrXusuXcatencion> listaHistConcEgrXusuXcatencion = new HistConcEgrXusuXcatencionDelegate()
			.listarTodosPorInstitucion(Integer.parseInt(usuario.getCodigoInstitucion()));
		
		for(HistConcEgrXusuXcatencion objList: listaHistConcEgrXusuXcatencion)
		{
			objList.getTipoUsuarioAutoriza();
			objList.getCentroAtencion().getDescripcion();
		}

		forma.setListaDtoH(listaHistConcEgrXusuXcatencion);
		listaHistConcEgrXusuXcatencion = null;
		HibernateUtil.endTransaction();
		
	}
	
	
	
	
	/**
	 * Si no se puede eliminar/modificar un registro muestra una pagina de error
	 * @param forma
	 * @param request
	 */
	private void mostraErrorEliminableModificable(ConcEgrXusuXcatencionForm forma, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		errores.add("error registro_relacionado", new ActionMessage("errors.noEliminableModificable", "Registro de Egresos de Caja"));
		saveErrors(request, errores);
		forma.setEstado("empezar");
	}
	
	
	
}	
	
	
	