package com.princetonsa.action.odontologia;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;
import util.UtilidadFecha;

import com.princetonsa.actionform.odontologia.MotivosDescuentosOdonForm;
import com.princetonsa.dto.odontologia.DtoMotivoDescuento;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.odontologia.MotivosDescuentos;
import com.princetonsa.sort.odontologia.SortMotivosDescuentos;

/**
 * 
 * @author Juan Diego Luna
 *
 */
public class MotivosDescuentosOdonAction extends Action
{
	Logger logger = Logger.getLogger(MotivosDescuentosOdonAction.class);
	
	/**
	 * 
	 */
	public ActionForward execute(	ActionMapping mapping,
									ActionForm form, 
									HttpServletRequest request, 
									HttpServletResponse response) throws Exception
			 
	{
		if(form instanceof MotivosDescuentosOdonForm)
		{
			MotivosDescuentosOdonForm forma= (MotivosDescuentosOdonForm)form;
            UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
            logger.info("estado-->"+forma.getEstado());
            if(forma.getEstado().equals("empezar"))
            {
            	return accionEmpezar(mapping, forma, usuario);
            }
            if(forma.getEstado().equals("nuevo"))
            {
            	return accionNuevo(mapping, forma);
            }
            if(forma.getEstado().equals("guardar"))
            {
            	return accionGuardar(mapping, forma, usuario);
            }
            if(forma.getEstado().equals("ordenar"))
            {    
            	return accionOrdenar(mapping,  forma);
            }
            if(forma.getEstado().equals("modificar"))
            {
            	return accionModificar(mapping, forma);                   	
            }
            if(forma.getEstado().equals("eliminar"))
            {
            	return accionEliminar(mapping, forma);
            }
            if(forma.getEstado().equals("guardarModificar"))
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
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */

	private ActionForward accionModificarGuardar(	ActionMapping mapping,
													MotivosDescuentosOdonForm forma, 
													UsuarioBasico usuario) 
	{
		DtoMotivoDescuento dtoWhere = new DtoMotivoDescuento();
		dtoWhere.setCodigoPk(forma.getListMotivosDescuentos().get(forma.getPosArray()).getCodigoPk());
		DtoMotivoDescuento dtoNuevo = new DtoMotivoDescuento();
		dtoNuevo=forma.getDtoMotivosDesc();
		dtoNuevo.setFechaModifica(UtilidadFecha.getFechaActual());
		dtoNuevo.setHoraModifica(UtilidadFecha.getHoraActual());
		dtoNuevo.setUsuarioModifica(usuario.getLoginUsuario());
		dtoNuevo.setInstitucion(usuario.getCodigoInstitucionInt());
					
		if(!MotivosDescuentos.modificar(dtoNuevo,dtoWhere))
			forma.setMensaje("Proceso realizado Exitosamente!");
		
		forma.reset();
					
		forma.setListMotivosDescuentos(MotivosDescuentos.cargar(forma.getDtoMotivosDesc()));
		return mapping.findForward("paginaPrincipal");
	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionEliminar(	ActionMapping mapping,
											MotivosDescuentosOdonForm forma) 
	{
		DtoMotivoDescuento dtoEliminar= new DtoMotivoDescuento();
		dtoEliminar.setCodigoPk(forma.getListMotivosDescuentos().get(forma.getPosArray()).getCodigoPk());
		
		if(MotivosDescuentos.eliminar(dtoEliminar))
			forma.setMensaje("Proceso realizado Exitosamente!");
		else
			forma.setMensaje("No se puede eliminar el registro. El motivo ya fue utilizado.");
		forma.reset();
		forma.setListMotivosDescuentos(MotivosDescuentos.cargar(forma.getDtoMotivosDesc()));
		return mapping.findForward("paginaPrincipal");
	}	
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionEmpezar(ActionMapping mapping,
										MotivosDescuentosOdonForm forma,
										UsuarioBasico usuario) 
	{
		forma.reset();
		forma.setMensaje("");
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
											MotivosDescuentosOdonForm forma, 
											UsuarioBasico usuario) 
	{
		logger.info("LA INSTITUCION ES->"+usuario.getCodigoInstitucionInt());
		//forma.getDtoMotivosDesc().setInstitucion(usuario.getCodigoInstitucionInt());
		forma.setListMotivosDescuentos(MotivosDescuentos.cargar(forma.getDtoMotivosDesc()));
		forma.setMensaje("");
		return mapping.findForward("paginaPrincipal");
	}			

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionNuevo(	ActionMapping mapping,
										MotivosDescuentosOdonForm forma) 
	{
		forma.setDtoMotivosDesc(new DtoMotivoDescuento());
		forma.getDtoMotivosDesc().setActivo(ConstantesBD.acronimoSi);
		forma.setExisteMotivoParametrizado(Boolean.FALSE);
		forma.setMensaje("");
		return mapping.findForward("paginaPrincipal");
	}
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardar(ActionMapping mapping,
										MotivosDescuentosOdonForm forma, 
										UsuarioBasico usuario) 
	{
		forma.getDtoMotivosDesc().setFechaModifica(UtilidadFecha.getFechaActual());
		forma.getDtoMotivosDesc().setHoraModifica(UtilidadFecha.getHoraActual());
		forma.getDtoMotivosDesc().setUsuarioModifica(usuario.getLoginUsuario());
		forma.getDtoMotivosDesc().setInstitucion(usuario.getCodigoInstitucionInt());
		
		if (MotivosDescuentos.guardar(forma.getDtoMotivosDesc())>0)
			forma.setMensaje("Proceso realizado Exitosamente!");
			
		forma.setDtoMotivosDesc(new DtoMotivoDescuento());
		forma.getDtoMotivosDesc().setInstitucion(usuario.getCodigoInstitucionInt());
		forma.getDtoMotivosDesc().setActivo("");
		forma.setListMotivosDescuentos(MotivosDescuentos.cargar(forma.getDtoMotivosDesc()));
		
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
											MotivosDescuentosOdonForm forma) 
	{
		forma.reset();	
		forma.setMensaje("");
		logger.warn("Estado no valido dentro del flujo de Motivos Descuentos Odon (null) ");
		request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
		return mapping.findForward("paginaError");
	}
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionOrdenar(ActionMapping mapping,  MotivosDescuentosOdonForm forma) 
	{
		Collections.sort(forma.getListMotivosDescuentos(), new SortMotivosDescuentos(forma.getPatronOrdenar()));
		forma.setMensaje("");
		return mapping.findForward("paginaPrincipal");
	}
	
	
	
	
	/**
		VALIDAR EXISTENCIA MOTIVO DE DESCUENTO
	 * AL MOMENTO DE MODIFICAR SOLO SE PUEDE MODIFICAR EL CAMPO A
	 * @param forma
	 */
	private void validarExistenciaMotivoCambiarInterfaz(MotivosDescuentosOdonForm forma) {
		
		forma.setExisteMotivoParametrizado(Boolean.FALSE);
		if( MotivosDescuentos.validarExistenciaMotivos(forma.getDtoMotivosDesc().getCodigoPk())>0)
		{
			forma.setExisteMotivoParametrizado(Boolean.TRUE);
		}
	}
	
	/**
     * 
     * @param mapping
     * @param forma
     * @return
     */
	private ActionForward accionModificar(ActionMapping mapping, MotivosDescuentosOdonForm forma) 
	{
		logger.info("valor de la posicion del array: "+forma.getPosArray());
		forma.getDtoMotivosDesc().setCodigoPk(forma.getListMotivosDescuentos().get(forma.getPosArray()).getCodigoPk());
		validarExistenciaMotivoCambiarInterfaz(forma);

		
		forma.getDtoMotivosDesc().setCodigoMotivo(forma.getListMotivosDescuentos().get(forma.getPosArray()).getCodigoMotivo());
		forma.getDtoMotivosDesc().setDescripcion(forma.getListMotivosDescuentos().get(forma.getPosArray()).getDescripcion());
		forma.getDtoMotivosDesc().setTipo(forma.getListMotivosDescuentos().get(forma.getPosArray()).getTipo());
		forma.getDtoMotivosDesc().setActivo(forma.getListMotivosDescuentos().get(forma.getPosArray()).getActivo());
		forma.getDtoMotivosDesc().setIndicativo(forma.getListMotivosDescuentos().get(forma.getPosArray()).getIndicativo());
		
		
		logger.info("valor descripcion del motivo seleccionado: "+forma.getDtoMotivosDesc().getDescripcion());
		
		MotivosDescuentos.modificar(forma.getDtoMotivosDesc(), forma.getDtoMotivosDesc());
		forma.setMensaje("");
		
		logger.info("valor descripcion del motivo seleccionado despues: "+forma.getDtoMotivosDesc().getDescripcion());
		return mapping.findForward("paginaPrincipal");
	}
}
