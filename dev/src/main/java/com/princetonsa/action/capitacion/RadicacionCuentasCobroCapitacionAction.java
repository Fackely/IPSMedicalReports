/*
 * @(#)RadicacionCuentasCobroCapitacionAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.action.capitacion;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.UtilidadBD;

import com.princetonsa.actionform.capitacion.RadicacionCuentasCobroCapitacionForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.capitacion.RadicacionCuentasCobroCapitacion;

/**
 *   Action, controla todas las opciones dentro del registro de
 *   radicacion cuentas cobro, incluyendo los posibles casos de error. 
 *   Y los casos de flujo.
 * @version 1.0, Julio 4, 2006
 * @author wrios
 */
public class RadicacionCuentasCobroCapitacionAction extends Action 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(RadicacionCuentasCobroCapitacionAction.class);
		
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(	ActionMapping mapping,
									ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response ) throws Exception
	{
		Connection con=null;
		try{
		if (response==null); //Para evitar que salga el warning
		if(form instanceof RadicacionCuentasCobroCapitacionForm)
		{
			
			try
			{
				con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
			}
			catch(SQLException e)
			{
				logger.warn("No se pudo abrir la conexión"+e.toString());
			}
						
			RadicacionCuentasCobroCapitacionForm forma =(RadicacionCuentasCobroCapitacionForm)form;
			HttpSession session=request.getSession();		
			UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
			String estado=forma.getEstado(); 
			logger.warn("Estado Racicacion CCC-->"+estado);
				
			if(estado == null)
			{
				forma.reset();	
				logger.warn("Estado no valido dentro del flujo de radicacion cuentas cobro (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
			else if (estado.equals("empezar"))
			{
				return this.accionEmpezar(forma,mapping, con);
			}
			else if(estado.equals("busquedaAvanzada"))
			{
				return this.accionBusquedaAvanzada(forma,mapping,con, "");
			}
			else if(estado.equals("continuarMostrarErrores"))
			{
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("principal");
			}
			else if(estado.equals("guardar"))
			{
				return this.accionGuardar(forma,mapping, usuario, con);
			}
			else
			{
				forma.reset();
				logger.warn("Estado no valido dentro del flujo de radicacion cuentas cobro ");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
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
	 * Este método especifica las acciones a realizar en el estado
	 * empezar.
	 * 
	 * @param forma RadicacionCuentasCobroCapitacionForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal "radicacionCuentaCobroCapitacion.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionEmpezar(	RadicacionCuentasCobroCapitacionForm forma, 
											ActionMapping mapping, 
											Connection con) throws SQLException
	{
		//Limpiamos lo que venga del form
		forma.reset();
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");		
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * busquedaAvanzada.
	 * 
	 * @param forma RadicacionCuentasCobroCapitacionForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página "radicacionCuentasCobroCapitacion.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionBusquedaAvanzada(	RadicacionCuentasCobroCapitacionForm forma, 
													ActionMapping mapping, 
													Connection con, String mensaje
												) throws SQLException
	{
		forma.setEstado("busquedaAvanzada");
		RadicacionCuentasCobroCapitacion mundo= new RadicacionCuentasCobroCapitacion();
		mundo.reset();
		forma.setCol(mundo.busquedaCuentasCobroARadicar(con, forma.getCriteriosBusquedaMap()));
		forma.setMensaje(mensaje);
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}
	
	
	
	private ActionForward accionGuardar(	RadicacionCuentasCobroCapitacionForm forma, 
											ActionMapping mapping,
											UsuarioBasico usuario,
											Connection con) throws SQLException
    {
		RadicacionCuentasCobroCapitacion mundo= new RadicacionCuentasCobroCapitacion();
		mundo.reset();
		llenarMundo(mundo, forma, usuario);
		mundo.insertarRadicacionCxC(con);
		
		String mensaje="LA RADICACION No "+forma.getNumeroRadicacion()+" FUE EXITOSA PARA LA CUENTA COBRO "+forma.getNumeroCuentaCobro();
		forma.resetInsertar();
		return this.accionBusquedaAvanzada(forma, mapping, con, mensaje);
    }
	
	/**
	 * llena los atributos del mundo
	 * @param mundo
	 * @param forma
	 * @param usuario
	 */
	private static void llenarMundo(RadicacionCuentasCobroCapitacion mundo, RadicacionCuentasCobroCapitacionForm forma, UsuarioBasico usuario)
	{
		mundo.setNumeroRadicacion(forma.getNumeroRadicacion());
		mundo.setObservaciones(forma.getObservaciones());
		mundo.setNumeroCuentaCobro(forma.getNumeroCuentaCobro());
		mundo.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
		mundo.setFechaRadicacionFormatApp(forma.getFechaRadicacionFormatApp());
		mundo.setLoginUsuario(usuario.getLoginUsuario());
	}
}
