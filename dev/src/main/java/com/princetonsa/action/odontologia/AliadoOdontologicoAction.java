package com.princetonsa.action.odontologia;

import java.sql.SQLException;
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

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadLog;

import com.princetonsa.actionform.odontologia.AliadoOdontologicoForm;
import com.princetonsa.dto.odontologia.DtoAliadoOdontologico;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.odontologia.AliadoOdontologico;
import com.princetonsa.sort.odontologia.SortGenerico;

/**
 * 
 * @author axioma
 *
 */
public class AliadoOdontologicoAction extends Action
{
	/**
	 * 
	 */
	private Logger logger = Logger.getLogger(AliadoOdontologicoAction.class);
	
	@Override
	public ActionForward execute(	ActionMapping mapping, 
									ActionForm form,
									HttpServletRequest request, 
									HttpServletResponse response)throws Exception 
	{
		if (form instanceof AliadoOdontologicoForm)
		{
			AliadoOdontologicoForm forma = (AliadoOdontologicoForm) form;
			logger.info("\n\n **************estado AliadoOdontologico -->" + forma.getEstado());
			UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");
			
			if(forma.getEstado().equals("empezar") || forma.getEstado().equals("empezarConsulta"))
			{
				return accionEmpezar(mapping, forma, usuario);
			}
			else if(forma.getEstado().equals("nuevo"))
			{
				return accionIngresarNuevo(mapping, forma);
			}
			else if(forma.getEstado().equals("guardar"))
			{
				return accionGuardar(mapping, forma, usuario);
			}
			else if(forma.getEstado().equals("ordenar"))
            {    
            	return accionOrdenar(mapping,  forma);
            }
			else if(forma.getEstado().equals("modificar"))
			{
				return accionModificar(mapping, forma);
			}
			else if(forma.getEstado().equals("detalle"))
			{
				return accionDetalle(mapping, forma, usuario);
			}
			else if(forma.getEstado().equals("busquedaAvanzada"))
			{
				return this.accionBusquedaAvanzada(forma,usuario,mapping);
			}
			else if(forma.getEstado().equals("resultadoBusquedaAvanzada"))
			{
				return this.accionConsultar(mapping, forma, usuario);
			}
			else if(forma.getEstado().equals("eliminar"))
			{
				return accionEliminar(mapping, forma, request, usuario);
			}
			else if (forma.getEstado().equals("mostrarErroresGuardar")) 
			{
				return mapping.findForward("paginaPrincipal");
			}
			else if(forma.getEstado().equals("guardarModificar"))
            {
            	return accionModificarGuardar(mapping, forma, usuario);
            }
			else
            {
            	return accionIncorrecta(mapping, request, forma);
            }
			
		}
		return null;		
		
	}
	
	/**
	 * 
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionBusquedaAvanzada(	AliadoOdontologicoForm forma, 
													UsuarioBasico usuario, 
													ActionMapping mapping
												)
	{
		forma.reset(false);
		forma.getDtoAliadoOdontologico().setEstado(ConstantesBD.acronimoSi);
		forma.setEstado("busquedaAvanzada");
		logger.info("SI ESTA BUSCANDO AVANZADOOO");
		return mapping.findForward("paginaPrincipal");
	}
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardar(	ActionMapping mapping,
											AliadoOdontologicoForm forma, 
											UsuarioBasico usuario) 
	{
		forma.getDtoAliadoOdontologico().setFechaModifica(UtilidadFecha.getFechaActual());
		forma.getDtoAliadoOdontologico().setHoraModifica(UtilidadFecha.getHoraActual());
		forma.getDtoAliadoOdontologico().setUsuarioModifica(usuario.getLoginUsuario());
		forma.getDtoAliadoOdontologico().setInstitucion(usuario.getCodigoInstitucionInt());
		AliadoOdontologico.guardar(forma.getDtoAliadoOdontologico());
		forma.setDtoAliadoOdontologico(new DtoAliadoOdontologico());
		forma.getDtoAliadoOdontologico().setInstitucion(usuario.getCodigoInstitucionInt());
		forma.getDtoAliadoOdontologico().setEstado("");
		forma.setListaAliadoOdontologico(AliadoOdontologico.cargar(forma.getDtoAliadoOdontologico()));
		logger.info("SI ESTA GUARDANDOOOO");
		forma.setEstado("resumen");
		return accionEmpezar(mapping, forma, usuario);
	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionModificarGuardar(	ActionMapping mapping,
													AliadoOdontologicoForm forma,
													UsuarioBasico usuario
													)
	{
		DtoAliadoOdontologico dtoWhere = new DtoAliadoOdontologico();
		dtoWhere.setCodigoPk(forma.getListaAliadoOdontologico().get(forma.getPosArray()).getCodigoPk());
		DtoAliadoOdontologico dtoNuevo = new DtoAliadoOdontologico();
		dtoNuevo=forma.getDtoAliadoOdontologico();
		dtoNuevo.setFechaModifica(UtilidadFecha.getFechaActual());
		dtoNuevo.setHoraModifica(UtilidadFecha.getHoraActual());
		dtoNuevo.setUsuarioModifica(usuario.getLoginUsuario());
		dtoNuevo.setInstitucion(usuario.getCodigoInstitucionInt());
					
		AliadoOdontologico.modificar(dtoNuevo,dtoWhere);
		UtilidadLog.generarLog(usuario, forma.getListaAliadoOdontologico().get(forma.getPosArray()), dtoNuevo, ConstantesBD.tipoRegistroLogModificacion , ConstantesBD.logAliadosOdontologicosCodigo);
		
		forma.reset(true);
		
		logger.info("SI ESTA GUARDANDOOOO MODIFICAAA");
		forma.setListaAliadoOdontologico(AliadoOdontologico.cargar(forma.getDtoAliadoOdontologico()));
		forma.setEstado("resumen");
		return mapping.findForward("paginaPrincipal");
	}
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionOrdenar(	ActionMapping mapping,
											AliadoOdontologicoForm forma) 
	{
		logger.info("patron->" + forma.getPatronOrdenar());
		logger.info("des -->" + forma.getEsDescendente() );
		
		boolean ordenamiento= false;
		
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente")){
			
			ordenamiento = true;
		}
		
		SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		
		Collections.sort(forma.getListaAliadoOdontologico(),sortG);
		forma.setEstado("empezar");
		return mapping.findForward("paginaPrincipal");
		
	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionEliminar(	ActionMapping mapping,
											AliadoOdontologicoForm forma,
											HttpServletRequest request,
											UsuarioBasico usuario) 
	{
		DtoAliadoOdontologico dtoEliminar= new DtoAliadoOdontologico();
		dtoEliminar.setCodigoPk(forma.getListaAliadoOdontologico().get(forma.getPosArray()).getCodigoPk());
		if(!AliadoOdontologico.eliminar(dtoEliminar))
		{
			ActionErrors errores = new ActionErrors();
			errores.add("", new ActionMessage("errors.notEspecific", "No se puede eliminar el registro debido a que se encuentra asociado a un beneficiario tarjeta cliente"));
			logger.warn("entra al error de elim");
			saveErrors(request, errores);	
			forma.setEstado("empezar");
			return mapping.findForward("paginaPrincipal");
		}
		UtilidadLog.generarLog(usuario, forma.getListaAliadoOdontologico().get(forma.getPosArray()), null, ConstantesBD.tipoRegistroLogEliminacion , ConstantesBD.logAliadosOdontologicosCodigo);
		forma.reset(true);
		forma.setListaAliadoOdontologico(AliadoOdontologico.cargar(forma.getDtoAliadoOdontologico()));
		logger.info("SI ESTA ELIMINANDOOOOO");
		forma.setEstado("resumen");
		return mapping.findForward("paginaPrincipal");
	}
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionIngresarNuevo(	ActionMapping mapping,
												AliadoOdontologicoForm forma) 
	{
		forma.setDtoAliadoOdontologico(new DtoAliadoOdontologico());
		forma.getDtoAliadoOdontologico().setEstado(ConstantesBD.acronimoSi);
		logger.info("SI ESTA INGRESANDOOOO");
		return mapping.findForward("paginaPrincipal");
	}
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionModificar(	ActionMapping mapping,
											AliadoOdontologicoForm forma) 
	{
		logger.info("valor de la posicion del array: "+forma.getPosArray());
		forma.setDtoAliadoOdontologico((DtoAliadoOdontologico)forma.getListaAliadoOdontologico().get(forma.getPosArray()).clone());
		logger.info("SI ESTA MODIFICANDOOOOOOO");
		return mapping.findForward("paginaPrincipal");
	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezar(	ActionMapping mapping,
											AliadoOdontologicoForm forma, 
											UsuarioBasico usuario) 
	{
		forma.reset(true);
		logger.info("SI ESTA EMPEZANDOOOOO");
		return accionConsultar(mapping, forma, usuario);
	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionConsultar(	ActionMapping mapping,
											AliadoOdontologicoForm forma, 
											UsuarioBasico usuario) 
	{
		
		
		
		
		logger.info("LA INSTITUCION ES->"+usuario.getCodigoInstitucionInt());
		forma.getDtoAliadoOdontologico().setInstitucion(usuario.getCodigoInstitucionInt());
		forma.setListaAliadoOdontologico(AliadoOdontologico.cargar(forma.getDtoAliadoOdontologico()));
		logger.info("SI ESTA CONSULTANDOOOOO");
		return mapping.findForward("paginaPrincipal");
	}
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionDetalle(	ActionMapping mapping,
											AliadoOdontologicoForm forma, 
											UsuarioBasico usuario) 
	{
		logger.info("LA INSTITUCION ES->"+usuario.getCodigoInstitucionInt());
		forma.setDtoAliadoOdontologico((DtoAliadoOdontologico)forma.getListaAliadoOdontologico().get(forma.getPosArray()).clone());
		logger.info("SI ESTA CONSULTANDOOOOO DETALLEE");
		return mapping.findForward("paginaPrincipal");
	}
	
	/**
	 * 
	 * @param mapping
	 * @param request
	 * @param forma
	 * @return
	 */
	private ActionForward accionIncorrecta(	ActionMapping mapping,
											HttpServletRequest request, 
											AliadoOdontologicoForm forma) 
	{
		forma.reset(true);	
		logger.warn("Estado no valido dentro del flujo de Aliado Odontologico (null) ");
		request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
		
		logger.info("ESTA MOSTRANDO UN ERROOOOOR");
		return mapping.findForward("paginaError");
	}
}