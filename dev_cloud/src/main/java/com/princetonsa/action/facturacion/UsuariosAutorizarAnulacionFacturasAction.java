package com.princetonsa.action.facturacion;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.UtilidadBD;
import util.Utilidades;

import com.princetonsa.actionform.facturacion.UsuariosAutorizarAnulacionFacturasForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.UsuariosAutorizarAnulacionFacturas;


public class UsuariosAutorizarAnulacionFacturasAction extends Action 
{

	
	
	/**
	 * objeto para manejar los logs de esta clase
	 */
	Logger logger =Logger.getLogger(UsuariosAutorizarAnulacionFacturasAction.class);
	
	
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con=null;
		try{
			if (form instanceof UsuariosAutorizarAnulacionFacturasForm) 
			{
				UsuariosAutorizarAnulacionFacturasForm forma=(UsuariosAutorizarAnulacionFacturasForm) form;

				String estado=forma.getEstado();

				logger.info("Estado -->"+estado);

				con=UtilidadBD.abrirConexion();
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());

				UsuariosAutorizarAnulacionFacturas mundo=new UsuariosAutorizarAnulacionFacturas();

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de UsuariosAutorizarAnulacionFacturasAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					forma.reset(usuario.getCodigoCentroAtencion()+"",usuario.getCodigoInstitucion());
					forma.setMapaConsultaUsuarios(mundo.consultarUsuariosAutorizados(con, usuario.getCodigoCentroAtencion()+"", usuario.getCodigoInstitucion()));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("cargarusuarios"))
				{
					forma.setMapaConsultaUsuarios(mundo.consultarUsuariosAutorizados(con, forma.getCentroAtencion(), usuario.getCodigoInstitucion()));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("nuevoRegistro"))
				{
					forma.setUsuarioAutorizado("");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("guardarNuevo"))
				{
					if(mundo.insertarUsuarios(con, forma.getCentroAtencion(), forma.getUsuarioAutorizado(), usuario.getCodigoInstitucion(), usuario.getLoginUsuario()))
					{
						forma.setMapaConsultaUsuarios(mundo.consultarUsuariosAutorizados(con, forma.getCentroAtencion(), usuario.getCodigoInstitucion()));
						forma.setEstado("operacionExitosa");
					}
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("guardarModificacion"))
				{
					if(mundo.modificarUsuario(con, forma.getCodigoPk(), forma.getUsuarioAutorizado(), usuario.getLoginUsuario()))
					{
						forma.setMapaConsultaUsuarios(mundo.consultarUsuariosAutorizados(con, forma.getCentroAtencion(), usuario.getCodigoInstitucion()));
						forma.setEstado("operacionExitosa");
					}
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("modificarRegistro"))
				{
					return this.accionModificarRegistro(forma, con, mapping, usuario);
				}
				else if(estado.equals("eliminarRegistro"))
				{
					if(mundo.eliminarUsuario(con, forma.getCodigoPk()))
					{
						forma.setMapaConsultaUsuarios(mundo.consultarUsuariosAutorizados(con, forma.getCentroAtencion(), usuario.getCodigoInstitucion()));
						forma.setEstado("operacionExitosa");
					}
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else
				{
					forma.reset(usuario.getCodigoCentroAtencion()+"",usuario.getCodigoInstitucion());
					logger.warn("Estado no valido dentro del flujo de USUARIOS AUTORIZADOS PARA ANULACION DE FACTURAS");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}		
			}
			else
			{
				logger.error("El form no es compatible con el form de UsuariosAutorizarAnulacionFacturasForm");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				return mapping.findForward("paginaError");
			}
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;
	}
	
	
	/**
	 * 
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionModificarRegistro(UsuariosAutorizarAnulacionFacturasForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
    {	
    	logger.info("forma.getCodigoPk()->"+forma.getCodigoPk());
    	if(forma.getCodigoPk()>0)
    	{	
    		logger.info("numReg->"+forma.getMapaConsultaUsuarios().get("numRegistros"));
    		for(int w=0; w<Utilidades.convertirAEntero(forma.getMapaConsultaUsuarios().get("numRegistros")+"");w++)
    		{
    			logger.info("forma.getSeccionesMap().get(codigo_+w).toString()->"+forma.getMapaConsultaUsuarios().get("codigo_"+w)+"");
    			if(forma.getMapaConsultaUsuarios().get("codigo_"+w).toString().equals(forma.getCodigoPk()+""))
    			{
    				forma.setUsuarioAutorizado(forma.getMapaConsultaUsuarios().get("loginusuario_"+w)+"");
    				UtilidadBD.closeConnection(con);
    		    	return mapping.findForward("principal");
    			}
    		}
    	}
    	UtilidadBD.closeConnection(con);
    	return mapping.findForward("principal");
	}
	
	
	
	
}
