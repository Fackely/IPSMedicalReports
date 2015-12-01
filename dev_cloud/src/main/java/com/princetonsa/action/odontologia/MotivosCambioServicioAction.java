/**
 * 
 */
package com.princetonsa.action.odontologia;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import util.ResultadoBoolean;
import util.UtilidadFecha;

import com.princetonsa.actionform.odontologia.MotivosCambioServicioForm;
import com.princetonsa.dto.odontologia.DtoMotivosCambioServicio;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.odontologia.MotivosCambioServicioMundo;

/**
 * @author armando
 *
 */
public class MotivosCambioServicioAction extends Action 
{
	/**
	 * 
	 */
	private static Logger logger=Logger.getLogger(MotivosCambioServicioAction.class);
	
	
	/**
	 * 
	 */
	public ActionForward execute(	ActionMapping mapping,
		 	ActionForm form,
		 	HttpServletRequest request,
		 	HttpServletResponse response) throws Exception
	{
		
	
		if(form instanceof MotivosCambioServicioForm)
		{
			
			UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			MotivosCambioServicioForm forma=(MotivosCambioServicioForm)form;
			logger.info("estado : "+forma.getEstado());
			forma.setMensaje(new ResultadoBoolean(false));
			if(forma.getEstado().equals("empezar"))
			{
				forma.reset();
				forma.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
				forma.setMotivos(MotivosCambioServicioMundo.consultarMotivosCambioServicios(forma.getCodigoInstitucion(),""));
				return mapping.findForward("principal");
			}
			else if(forma.getEstado().equals("nuevo"))
			{
				forma.resetNuevo(); 
				forma.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
				return mapping.findForward("principal");
			}
			else if(forma.getEstado().equals("cargarModificar"))
			{
				forma.setCodigoPk(forma.getMotivos().get(forma.getRegistroSeleccionado()).getCodigoPk());
				forma.setCodigo(forma.getMotivos().get(forma.getRegistroSeleccionado()).getCodigo());
				forma.setDescripcion(forma.getMotivos().get(forma.getRegistroSeleccionado()).getDescripcion());
				forma.setTipo(forma.getMotivos().get(forma.getRegistroSeleccionado()).getTipo());
				forma.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
				forma.setEstado("nuevo");//setea el estado a nuevo, para que el sistema tratae la modificacion del registro como si fuera un registro nuevo.
				return mapping.findForward("principal");
			}
			else if(forma.getEstado().equals("eliminar"))
			{
				if(MotivosCambioServicioMundo.eliminarRegistro(forma.getMotivos().get(forma.getRegistroSeleccionado()).getCodigoPk()))
				{
					forma.setMensaje(new ResultadoBoolean(true,"Registro Eliminado Exitosamente."));
				}
				else
				{
					forma.setMensaje(new ResultadoBoolean(true,"El registro no se pudo eliminar. Por favor verifique"));
				}
				forma.setMotivos(MotivosCambioServicioMundo.consultarMotivosCambioServicios(forma.getCodigoInstitucion(),""));
				return mapping.findForward("principal");
			}
			else if(forma.getEstado().equals("guardar"))
			{
				DtoMotivosCambioServicio dto=new DtoMotivosCambioServicio();
				dto.setCodigo(forma.getCodigo());
				dto.setCodigoPk(forma.getCodigoPk());
				dto.setDescripcion(forma.getDescripcion());
				dto.setFechaModifica(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
				dto.setHoraModifica(UtilidadFecha.getHoraActual());
				dto.setInstitucion(forma.getCodigoInstitucion());
				dto.setTipo(forma.getTipo());
				dto.setUsuarioModifica(usuario.getLoginUsuario());
				if(MotivosCambioServicioMundo.guardar(dto))
				{
					if(forma.getCodigoPk()>0)
						forma.setMensaje(new ResultadoBoolean(true,"Registro Actualizado exitosamente."));
					else
						forma.setMensaje(new ResultadoBoolean(true,"Registro Insertado Exitosamente."));
				}
				else
				{
					if(forma.getCodigoPk()>0)
						forma.setMensaje(new ResultadoBoolean(true,"El registro no se pudo actualizar. Por favor verifique"));
					else
						forma.setMensaje(new ResultadoBoolean(true,"El registro no se pudo insertar. Por favor verifique."));
				}
				forma.setMotivos(MotivosCambioServicioMundo.consultarMotivosCambioServicios(forma.getCodigoInstitucion(),""));
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
