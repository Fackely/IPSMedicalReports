package com.princetonsa.action.inventarios;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.Errores;
import util.Listado;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadTexto;

import com.princetonsa.actionform.inventarios.MezclasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.Mezcla;

public class MezclasAction extends Action 
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(MezclasAction.class);
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception
			{
		Connection con=null;

		try{
			UsuarioBasico usuario;
			MezclasForm mezclasForm;
			if (form instanceof MezclasForm)
			{
				mezclasForm = (MezclasForm)form;
				String estado=mezclasForm.getEstado();
				logger.warn("Estado MezclasForm [" + estado + "]");

				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
					mezclasForm.clean();

					logger.warn("Problemas con la base de datos "+e);
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

				if( usuario == null )
				{
					if( logger.isDebugEnabled() )
					{
						logger.debug("Usuario no válido (null)");
					}

					UtilidadBD.cerrarConexion(con);                
					mezclasForm.clean();                   

					request.setAttribute("codigoDescripcionError", "errors.usuario.noCargado");
					return mapping.findForward("paginaError");              
				}
				else if( estado == null )
				{
					if( logger.isDebugEnabled() )
					{
						logger.debug("estado no valido dentro del flujo de valoración (null) ");
					}

					UtilidadBD.cerrarConexion(con);                
					mezclasForm.clean();

					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					return mapping.findForward("descripcionError");             
				}
				else if(estado.equals("empezar"))
				{
					return this.accionEmpezar(mezclasForm, mapping, con, usuario, request);
				}
				else if(estado.equals("consultar"))
				{
					return this.accionConsultar(mezclasForm, mapping, con, usuario, request);
				}
				else if(estado.equals("ingresar"))
				{
					return this.accionIngresar(mezclasForm, mapping, con, usuario, request);
				}
				else if(estado.equals("editar"))
				{
					return this.accionEditar(mezclasForm, mapping, con, usuario, request);
				}
				else if(estado.equals("guardarNuevo"))
				{
					return this.accionGuardarNuevo(mezclasForm, mapping, con, usuario, request);
				}
				else if(estado.equals("guardarCambios"))
				{
					return this.accionGuardarCambios(mezclasForm, mapping, con, usuario, request);
				}
				else if(estado.equals("eliminar"))
				{
					return this.accionEliminar(mezclasForm, mapping, con, usuario, request);
				}
				else if(estado.equals("cambiarOrden"))
				{
					return this.accionCambiarOrden(mezclasForm, mapping, con);
				}
				else
				{
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}

			}
			else
			{
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
	 * Este método especifica las acciones a realizar en el estado empezar de Mezclas
	 * @param mezclasForm
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionEmpezar(
			MezclasForm mezclasForm, ActionMapping mapping, Connection con, UsuarioBasico usuario, HttpServletRequest request) throws SQLException
	{
		mezclasForm.clean();
		mezclasForm.setMezclas(Mezcla.consultarMezclasInstitucion(con, usuario.getCodigoInstitucionInt()));
		mezclasForm.setEstado("empezar");
    		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}
	
	private ActionForward accionConsultar(
			MezclasForm mezclasForm, ActionMapping mapping, Connection con, UsuarioBasico usuario, HttpServletRequest request) throws SQLException
	{
		mezclasForm.clean();
		mezclasForm.setMezclas(Mezcla.consultarMezclasInstitucion(con, usuario.getCodigoInstitucionInt()));
		mezclasForm.setEstado("consultar");	
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}
	
	private ActionForward accionIngresar(MezclasForm mezclasForm, ActionMapping mapping, Connection con, UsuarioBasico usuario, HttpServletRequest request) throws SQLException
	{
		mezclasForm.clean();
		mezclasForm.setActivo("true");
		mezclasForm.setMezclas(Mezcla.consultarMezclasInstitucion(con, usuario.getCodigoInstitucionInt()));
		mezclasForm.setEstado("ingresar");
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}

	/**
	 * 
	 * @param mezclasForm
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionCambiarOrden(MezclasForm mezclasForm, ActionMapping mapping, Connection con) throws SQLException
	{
		String[] indices={"consecutivo_","codigo_","nombre_","codtipo_","nomtipo_","activo_","codinstitucion_"};
		int numReg=Integer.parseInt(mezclasForm.getMezclas().get("numRegistros")+"");
		mezclasForm.setMezclas(Listado.ordenarMapa(indices,mezclasForm.getColumna(),mezclasForm.getUltimaColumna(),mezclasForm.getMezclas(),numReg));
		mezclasForm.setUltimaColumna(mezclasForm.getColumna());
		mezclasForm.getMezclas().put("numRegistros",numReg+"");
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
		
	}

	/**
	 * 
	 * @param mezclasForm
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionEditar(MezclasForm mezclasForm, ActionMapping mapping, Connection con, UsuarioBasico usuario, HttpServletRequest request) throws SQLException
	{
		try
		{
			Mezcla mezcla = new Mezcla();
			mezcla.consultar(con, mezclasForm.getConsecutivo());
			mezclasForm.clean();
			mezclasForm.setConsecutivo(mezcla.getConsecutivo());
			mezclasForm.setCodigo(mezcla.getCodigo());
			mezclasForm.setNombre(mezcla.getNombre());
			mezclasForm.setTipo(mezcla.getTipo());
			mezclasForm.setActivo(mezcla.getActivo());
			mezclasForm.setMezclas(Mezcla.consultarMezclasInstitucion(con, usuario.getCodigoInstitucionInt()));

			String log="\n          =====INFORMACION ANTES DE LA MODIFICACION===== " +
			"\n*  Código: [" +mezclasForm.getCodigo() +"] "+
			"\n*  Mezcla: [" +mezclasForm.getNombre() +"] "+
			"\n*  Tipo:   [" +mezclasForm.getTipo().getNombre() +"] "+
			"\n*  Activo: [" +(UtilidadTexto.getBoolean(mezclasForm.getActivo())?"Sí":"No")+"] "+
			"\n*  Institución: ["+usuario.getCodigoInstitucion()+"] "+
			"\n========================================================";
			mezclasForm.setLogModificacion(log);
			
			mezclasForm.setEstado("editar");
		}
		catch(Errores e)
		{
			ActionErrors errores = new ActionErrors();
			errores.add(e.getMessage(), e.getActionMessage());
			saveErrors(request, errores);
            logger.warn(e);
    		mezclasForm.setEstado("empezar");
		}
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}

	private ActionForward accionEliminar(
			MezclasForm mezclasForm, ActionMapping mapping, Connection con, UsuarioBasico usuario, HttpServletRequest request) throws SQLException
	{
		try
		{
			Mezcla mezcla = new Mezcla();
			mezcla.consultar(con, mezclasForm.getConsecutivo());
			mezcla.eliminar(con);

			String log=
			"\n          =====REGISTRO ELIMINADO===== " +
			"\n*  Código: [" +mezcla.getCodigo() +"] "+
			"\n*  Mezcla: [" +mezcla.getNombre() +"] "+
			"\n*  Tipo:   [" +mezcla.getTipo().getNombre() +"] "+
			"\n*  Activo: [" +(UtilidadTexto.getBoolean(mezcla.getActivo())?"Sí":"No")+"] "+
			"\n*  Institución: ["+usuario.getCodigoInstitucion()+"] "+
			"\n========================================================";

			LogsAxioma.enviarLog(ConstantesBD.logMezclasCodigo, log, ConstantesBD.tipoRegistroLogEliminacion, usuario.getInformacionGeneralPersonalSalud());
			
			mezclasForm.setMezclas(Mezcla.consultarMezclasInstitucion(con, usuario.getCodigoInstitucionInt()));
			mezclasForm.setMensaje("¡Mezcla eliminada con éxito!");
		}
		catch(Errores e)
		{
			ActionErrors errores = new ActionErrors();
			errores.add(e.getMessage(), e.getActionMessage());
			saveErrors(request, errores);
            logger.warn(e);
		}
		mezclasForm.setEstado(mezclasForm.getEstadoAnterior());
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}

	private ActionForward accionGuardarNuevo(
			MezclasForm mezclasForm, ActionMapping mapping, Connection con, UsuarioBasico usuario, HttpServletRequest request) throws SQLException
	{
		ActionErrors errores= new ActionErrors();
		
		Mezcla mezcla = new Mezcla();
		mezcla.setCodigo(mezclasForm.getCodigo());
		mezcla.setNombre(mezclasForm.getNombre());
		mezcla.setTipo(mezclasForm.getTipo());
		mezcla.setActivo(mezclasForm.getActivo());
		mezcla.setCodInstitucion(usuario.getCodigoInstitucionInt());
		errores = mezcla.insertar(con);
		mezclasForm.clean();
		mezclasForm.setMezclas(Mezcla.consultarMezclasInstitucion(con, usuario.getCodigoInstitucionInt()));
		mezclasForm.setEstado("empezar");
			
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			mezclasForm.setEstado("ingresar");
		} else {
			mezclasForm.setMensaje("¡Mezcla guardada con éxito!");
		}
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}

	private ActionForward accionGuardarCambios(
			MezclasForm mezclasForm, ActionMapping mapping, Connection con, UsuarioBasico usuario, HttpServletRequest request) throws SQLException
	{
		try
		{
			Mezcla mezcla = new Mezcla();
			mezcla.setConsecutivo(mezclasForm.getConsecutivo());
			mezcla.setCodigo(mezclasForm.getCodigo());
			mezcla.setNombre(mezclasForm.getNombre());
			mezcla.setTipo(mezclasForm.getTipo());
			mezcla.setActivo(mezclasForm.getActivo());
			mezcla.setCodInstitucion(usuario.getCodigoInstitucionInt());
			mezcla.actualizar(con);

			String log=mezclasForm.getLogModificacion()+
			"\n          =====INFORMACION DESPUES DE LA MODIFICACION===== " +
			"\n*  Código: [" +mezclasForm.getCodigo() +"] "+
			"\n*  Mezcla: [" +mezclasForm.getNombre() +"] "+
			"\n*  Tipo:   [" +mezclasForm.getTipo().getNombre() +"] "+
			"\n*  Activo: [" +(UtilidadTexto.getBoolean(mezcla.getActivo())?"Sí":"No")+"] "+
			"\n*  Institución: ["+usuario.getCodigoInstitucion()+"] "+
			"\n========================================================";

			LogsAxioma.enviarLog(ConstantesBD.logMezclasCodigo, log, ConstantesBD.tipoRegistroLogModificacion, usuario.getInformacionGeneralPersonalSalud());

			mezclasForm.clean();
			mezclasForm.setMensaje("¡Mezcla modificada con éxito!");
			mezclasForm.setMezclas(Mezcla.consultarMezclasInstitucion(con, usuario.getCodigoInstitucionInt()));
			mezclasForm.setEstado("empezar");
		}
		catch(Errores e)
		{
			ActionErrors errores = new ActionErrors();
			errores.add(e.getMessage(), e.getActionMessage());
			saveErrors(request, errores);
            logger.warn(e);
			mezclasForm.setEstado("editar");
		}
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}
}
