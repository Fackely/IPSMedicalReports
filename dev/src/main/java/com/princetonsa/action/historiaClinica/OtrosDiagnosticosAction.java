/*
 * Marzo 17, 2010
 */
package com.princetonsa.action.historiaClinica;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
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

import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.actionform.historiaClinica.OtrosDiagnosticosForm;
import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.InstitucionesHome;
import com.servinte.axioma.orm.OtrosDiagnosticos;
import com.servinte.axioma.orm.delegate.UsuariosDelegate;
import com.servinte.axioma.orm.delegate.odontologia.OtrosDiagnosticosDelegate;


/**
 * @author Cristhian Murillo
 *
 * Clase usada para controlar los procesos de la funcionalidad 
 */
public class OtrosDiagnosticosAction extends Action 
{
	
	
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	private Logger logger = Logger.getLogger(OtrosDiagnosticosAction.class);
	
	
	
	/**
	 * M&eacute;todo encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, 
	 * HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
								HttpServletResponse response )throws Exception
	{

		if(form instanceof OtrosDiagnosticosForm)
		{
			//OBJETOS A USAR
			/**
			 * Formulario
			 */
			OtrosDiagnosticosForm forma = (OtrosDiagnosticosForm)form;
			
			/** 
			 * Usuario
			 */
			UsuarioBasico usuario 		= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			//PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
			
			String estado = forma.getEstado(); 
			
			logger.warn("estado OtrosDiagnosticosAction--> "+estado);
			
			if(estado == null)
			{
				request.setAttribute("codigoDescripcionError","errors.estadoInvalido");
				return mapping.findForward("paginaError");
			}
			
			// empezar
			else if( (estado.equals("empezar")) || (estado.equals("resumen")) )
			{
				return accionEmpezar(mapping,forma, usuario);
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
				return accionEliminar(mapping, forma, usuario);
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
	private ActionForward accionEmpezar(ActionMapping mapping, OtrosDiagnosticosForm forma, UsuarioBasico usuario)
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
	private ActionForward accionNuevo(ActionMapping mapping, OtrosDiagnosticosForm forma, UsuarioBasico usuario)
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
	private ActionForward accionGuardar(ActionMapping mapping,	OtrosDiagnosticosForm forma, UsuarioBasico usuario, HttpServletRequest request)
	{
		if(validarCamposUnicos(forma, request))
		{
			llenarDetallesForma(forma, usuario);
			new OtrosDiagnosticosDelegate().persist(forma.getDto());
			HibernateUtil.endTransaction();
			forma.reset();
			forma.setEstado("resumen");
		}
		mostrarLista(forma, usuario);		
		
		return mapping.findForward("principal");
	}
	
	

	/**
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionOrdenar(ActionMapping mapping,	OtrosDiagnosticosForm forma, UsuarioBasico usuario) 
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
	private ActionForward accionEliminar(ActionMapping mapping, OtrosDiagnosticosForm forma, UsuarioBasico usuario) 
	{
		forma.setDto(forma.getListaDto().get(forma.getPosArray()));
		new OtrosDiagnosticosDelegate().delete(forma.getDto());
		HibernateUtil.endTransaction();
		
		forma.reset();
		mostrarLista(forma, usuario);		
		forma.setEstado("resumen");
		return accionEmpezar(mapping,forma, usuario);
	}
	
	
	

	/**
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request 
	 * @return
	 */
	private ActionForward accionModificar(ActionMapping mapping, OtrosDiagnosticosForm forma, UsuarioBasico usuario, HttpServletRequest request) 
	{
		forma.setDto(forma.getListaDto().get(forma.getPosArray()));
		mostrarLista(forma, usuario);
		forma.setMostrarFormularioIngreso(true);
		
		return mapping.findForward("principal");
	}
	
	
	
	
	/**
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request 
	 * @return
	 */
	private ActionForward accionGuardarModificar(ActionMapping mapping, OtrosDiagnosticosForm forma, UsuarioBasico usuario, HttpServletRequest request) 
	{
		if(validarCamposUnicosOtraInstancia(forma, request))
		{
			llenarDetallesForma(forma, usuario);
			new OtrosDiagnosticosDelegate().merge(forma.getDto());
			HibernateUtil.endTransaction();
			forma.reset();
			forma.setEstado("resumen");
			mostrarLista(forma, usuario);		
		}
		
		return mapping.findForward("principal");
	}
	
	



	/**
	 * 	Llena la variable tipo de diagnostico para que sea mostrada como una
	 *  lista desplegable en la forma
	 *  @param forma
	 */
	private void llenarListaTipoDiagnosticos(OtrosDiagnosticosForm forma)
	{
		// se genere un vector con las etiquetas a mostrar
		String[] listaConstantesStr = {   
				ConstantesIntegridadDominio.acronimoTipoDiagnosticoOdontologico
		};

		// se hace una conexion para la utilidad de integridad dominio
		Connection con = UtilidadBD.abrirConexion(); 
		List<DtoIntegridadDominio> listaConstantes = Utilidades.generarListadoConstantesIntegridadDominio(con, listaConstantesStr, false);
		forma.setListaTiposDiagnostico(new ArrayList<DtoIntegridadDominio>(listaConstantes));
		
		try {
			UtilidadBD.cerrarConexion(con);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("Problema cerrando la conexion - llenarListaTipoDiagnosticos");
		}
	}
	
	
	
	
	/**
	 * Ingresa en el dto los datos de usuario
	 * @param usuario
	 * @param forma
	 */
	private void llenarDetallesForma(OtrosDiagnosticosForm forma, UsuarioBasico usuario)
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
	private void mostrarLista(OtrosDiagnosticosForm forma, UsuarioBasico usuario) 
	{
		llenarListaTipoDiagnosticos(forma);
		forma.setListaDto(new OtrosDiagnosticosDelegate().listarTodosPorInstitucion(
									Integer.parseInt(usuario.getCodigoInstitucion())));
		
		HibernateUtil.endTransaction();
	}
	

	
	
	/**
	 * 	Valida que los campos unicos de la tabla no sean repetidos por otro dto
	 * 	Retorna true si los datos son validos
	 * @param forma
	 * @param request 
	 * 
	 */
	private boolean validarCamposUnicos(OtrosDiagnosticosForm forma, HttpServletRequest request)
	{
		OtrosDiagnosticosDelegate delegate 	= new OtrosDiagnosticosDelegate();
		ActionErrors errores 				= new ActionErrors();
		
				
		if(delegate.buscarPorCampo("codigo", forma.getDto().getCodigo()) != null)
		{
			errores.add("error campo repetido", new ActionMessage("errors.yaExisteCampoEnTabla", "c&oacute;digo"));
		}
		
		if(delegate.buscarPorCampo("descripcion", forma.getDto().getDescripcion()) != null)
		{
			errores.add("error campo repetido", new ActionMessage("errors.yaExisteCampoEnTabla", "descripci&oacute;n"));
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
	private boolean validarCamposUnicosOtraInstancia(OtrosDiagnosticosForm forma, HttpServletRequest request)
	{
		OtrosDiagnosticosDelegate delegate 	= new OtrosDiagnosticosDelegate();
		ActionErrors errores 				= new ActionErrors();
		OtrosDiagnosticos instanciaBD			= new OtrosDiagnosticos();
			
		instanciaBD = delegate.buscarPorCampo("codigo", forma.getDto().getCodigo());
		if(instanciaBD != null)
		{
			/* Se verifica si es una isntancia distinta */
			if(instanciaBD.getCodigoPk() != forma.getDto().getCodigoPk())
			{
				errores.add("error campo repetido", new ActionMessage("errors.yaExisteCampoEnTabla", "c&oacute;digo"));
			}
		}
		
		instanciaBD = new OtrosDiagnosticos();
		instanciaBD = delegate.buscarPorCampo("descripcion", forma.getDto().getDescripcion());
		if(instanciaBD != null)
		{
			/* Se verifica si es una isntancia distinta */
			if(instanciaBD.getCodigoPk() != forma.getDto().getCodigoPk())
			{
				errores.add("error campo repetido", new ActionMessage("errors.yaExisteCampoEnTabla", "descripci&oacute;n"));
			}
		}
		
		
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			return false;
		}
		return true;
	}
	
}	
	
	
	