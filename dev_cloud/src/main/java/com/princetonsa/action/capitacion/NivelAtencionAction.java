/*
 * Creado el Jun 13, 2006
 * por Julian Montoya
 */
package com.princetonsa.action.capitacion;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.Listado;
import util.UtilidadBD;
import util.UtilidadCadena;

import com.princetonsa.action.enfermeria.RegistroEnfermeriaAction;
import com.princetonsa.actionform.capitacion.NivelAtencionForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.capitacion.NivelAtencion;





public class NivelAtencionAction extends Action {

	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(RegistroEnfermeriaAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
	{
		Connection con=null;
		try{
		if (form instanceof NivelAtencionForm)
		{
			
			try
			{
				con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
			}
			catch(SQLException e)
			{
				logger.warn("No se pudo abrir la conexión"+e.toString());
			} 
			
			UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			NivelAtencionForm forma = (NivelAtencionForm) form;
			String estado=forma.getEstado();
			logger.warn("NivelServicioAction el Estado [" + estado + "]\n\n");
			
			if(estado.equals("ingresar"))
			{
				forma.setSoloConsulta(false);
				return accionEmpezar(con, mapping, forma, usuario);
			}
			if(estado.equals("consultar"))
			{	
				forma.setSoloConsulta(true);
				return accionEmpezar(con, mapping, forma, usuario);
			}
			if(estado.equals("empezar"))
			{	
				return accionEmpezar(con, mapping, forma, usuario);
			}
			else if (estado.equals("guardar"))  
			{	
				return accionGuardar(forma, con, mapping, usuario);
			}
			else if (estado.equals("eliminar"))  
			{	
				return accionEliminar(forma, con, mapping, usuario);
			}
			else if (estado.equals("ordenar"))  
			{	
				return accionOrdenar(forma, con, mapping);
			}

		}//if
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;
	}

	/**
	 * Metodo para Ordenar el listado de Niveles de Servicio.   
	 * @param forma
	 * @param con
	 * @param mapping
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionOrdenar(NivelAtencionForm forma, Connection con, ActionMapping mapping) throws SQLException 
	{
		
		String[] indices = {"consecutivo_", "codigo_", "nivel_", "h_nivel_", "activo_", "h_activo_", "puedeeliminar_"};

		int num = 0;
		if ( UtilidadCadena.noEsVacio(forma.getMapa().get("numRegistros")+"") )  
			{
				num = Integer.parseInt(forma.getMapa().get("numRegistros")+"");
			}
			
		forma.getMapa().putAll(Listado.ordenarMapa(indices, forma.getPatronOrdenar(),
										                    forma.getUltimoPatronOrdenar(),
										                    forma.getMapa(),
										                    num ));
	    
	    forma.getMapa().put("numRegistros", num);
	    forma.setUltimoPatronOrdenar(forma.getPatronOrdenar());
	    UtilidadBD.cerrarConexion(con);
	    return mapping.findForward("principal");
	}

	
	/**
	 * Metodo para eliminar un nivel de servicio. 
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario 
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionEliminar(NivelAtencionForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) throws SQLException 
	{
		NivelAtencion mundo = new NivelAtencion();
		
		llenarMundo(forma, mundo);
		mundo.eliminar(con, usuario.getLoginUsuario() );
		return accionEmpezar(con, mapping, forma, usuario);  
	}

	/**
	 * Medoto Para Iniciar En La Funcionalidad.
	 * @param con 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionEmpezar(Connection con, ActionMapping mapping, NivelAtencionForm forma, UsuarioBasico usuario) throws SQLException
	{
		NivelAtencion mundo = new NivelAtencion();
		forma.reset(); 
		forma.setMapa( mundo.cargarInformacion(con, usuario.getCodigoInstitucionInt()) );
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");  
	}	
	/**
	 * Metodo para guardar la informacion ingresada por el usuario. 
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario 
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionGuardar(NivelAtencionForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) throws SQLException
	{
		NivelAtencion mundo = new NivelAtencion();
		
		llenarMundo(forma, mundo);
		
		if(mundo.insertar(con, usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario()))
			forma.setProcesoExitoso(true);
		else
			forma.setProcesoExitoso(false);
		
		return accionEmpezar(con, mapping, forma, usuario);   
	}


	/**
	 * Metodo para pasar la informacion al mundo.
	 * @param forma
	 * @param mundo
	 */
	private void llenarMundo(NivelAtencionForm forma, NivelAtencion mundo)
	{
		mundo.setNroRegistrosNuevos(forma.getNroRegistrosNuevos());
		mundo.setMapa(forma.getMapa());
	}

	
}
