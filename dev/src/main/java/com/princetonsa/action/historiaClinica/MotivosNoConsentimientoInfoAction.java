/**
 * 
 */
package com.princetonsa.action.historiaClinica;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadSesion;
import util.Utilidades;

import com.princetonsa.actionform.historiaClinica.MotivosNoConsentimientoInfoForm;
import com.princetonsa.dto.historiaClinica.DtoMotivosNoConsentimientoInformado;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.MotivosNoConsentimientoInfoMundo;
/**
 * @author armando
 *
 */
public class MotivosNoConsentimientoInfoAction extends Action 
{
	
	/**
	 * 
	 */
	public ActionForward execute(	ActionMapping mapping,
		 	ActionForm form,
		 	HttpServletRequest request,
		 	HttpServletResponse response) throws Exception
	{
		
		if(form instanceof MotivosNoConsentimientoInfoForm)
		{
			UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			MotivosNoConsentimientoInfoForm forma=(MotivosNoConsentimientoInfoForm)form;
			if(forma.getEstado().equals("empezar"))
			{
				forma.reset();
				forma.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
				forma.setMotivos(MotivosNoConsentimientoInfoMundo.cosultarMotivos(true));
				return mapping.findForward("principal");
			}
			else if(forma.getEstado().equals("nuevo"))
			{
				DtoMotivosNoConsentimientoInformado motivo=new DtoMotivosNoConsentimientoInformado();
				motivo.setActivo(ConstantesBD.acronimoSi);
				motivo.setInstitucion(forma.getCodigoInstitucion());
				motivo.setPuedoEliminar(true);
				forma.setMensaje(new ResultadoBoolean(false, ""));
				forma.getMotivos().add(motivo);
				return UtilidadSesion.redireccionar("",Utilidades.convertirAEntero(util.ValoresPorDefecto.getMaxPageItems(forma.getCodigoInstitucion())),forma.getMotivos().size(), response, request, "motivosNoConsentimientoInformado.jsp",true);
			}
			else if(forma.getEstado().equals("eliminar"))
			{
				boolean redireccionar=false;
				redireccionar=(forma.getRegistroSeleccionado()==(forma.getMotivos().size()-1));
				if(forma.getMotivos().get(forma.getRegistroSeleccionado()).getCodigoPk()>0)
				{
					forma.getMotivosEliminados().add(forma.getMotivos().get(forma.getRegistroSeleccionado()));
				}
				forma.getMotivos().remove(forma.getRegistroSeleccionado());
				
				if(redireccionar)
					return UtilidadSesion.redireccionar("",Utilidades.convertirAEntero(util.ValoresPorDefecto.getMaxPageItems(forma.getCodigoInstitucion())),forma.getMotivos().size(), response, request, "motivosNoConsentimientoInformado.jsp",true);
				else
					return mapping.findForward("principal");
			}
			if(forma.getEstado().equals("guardar"))
			{
				if(MotivosNoConsentimientoInfoMundo.guardarMotivos(forma.getMotivos(),forma.getMotivosEliminados(),usuario))
				{
					forma.reset();
					forma.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
					forma.setMotivos(MotivosNoConsentimientoInfoMundo.cosultarMotivos(true));
					forma.setMensaje(new ResultadoBoolean(true,"PROCESO EXITOSO"));
				}
				else
				{
					forma.setMensaje(new ResultadoBoolean(false,"No se pudo almacenar los registros. Por favor Verifique."));
				}
				return mapping.findForward("principal");
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
	}

}
