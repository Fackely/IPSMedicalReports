/*
 * Creado el Apr 18, 2006
 * por Julian Montoya
 */
package com.princetonsa.action.interfaz;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.UtilidadBD;
import util.UtilidadCadena;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.interfaz.CuentaServicioForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.interfaz.CuentaServicio;


public class CuentaServicioAction extends Action 
{

	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(CuentaServicioAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
	{
		Connection con=null;
		try{


			if (form instanceof CuentaServicioForm)
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

				CuentaServicioForm forma = (CuentaServicioForm) form;
				String estado=forma.getEstado();

				logger.warn("\n\n  El Estado en CuentaServicioAction [" + estado + "] \n\n ");

				//-----Definir el numero de registros para el paginador.
				if ( UtilidadCadena.noEsVacio(ValoresPorDefecto.getMaxPageItems( usuario.getCodigoInstitucionInt() )) )
				{
					forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems( usuario.getCodigoInstitucionInt() )));
				}
				else
				{
					forma.setMaxPageItems(20);
				}

				if (estado.equals("empezar"))
				{
					return accionEmpezar(mapping, forma, con);
				}
				else if (estado.equals("listarGruposServicios"))
				{
					return accionListarGruposServicios(mapping, forma, con, "claseServicio", usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario());
				}
				else if (estado.equals("grabarGruposServicios"))
				{
					return accionGrabarGruposServicios(mapping, forma, con, usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario());
				} 
				else if (estado.equals("eliminarGruposServicios"))
				{
					return accionEliminarGruposServicios(mapping, forma, con, usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario());
				} 
				else if (estado.equals("consutarTiposServicios"))
				{
					return accionListarGruposServicios(mapping, forma, con, "tipoServicio", usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario());
				}
				else if (estado.equals("grabarTiposServicios"))
				{
					return accionGrabarTiposServicios(mapping, forma, con, "tipoServicio", usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario());
				}
				else if (estado.equals("eliminarTiposServicios"))
				{
					return accionEliminarTiposServicios(mapping, forma, con, "tipoServicio", usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario());
				}
				else if (estado.equals("consultarEspecialidades"))
				{
					return accionListarGruposServicios(mapping, forma, con, "especialidad", usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario());
				}
				else if (estado.equals("grabarEspecialidades"))
				{
					return accionGrabarEspecialidades(mapping, forma, con, "especialidad", usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario());
				}
				else if (estado.equals("eliminarEspecialidades"))
				{
					return accionEliminarEspecialidades(mapping, forma, con, "especialidad", usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario());
				}
				else if (estado.equals("consutarServicios"))
				{
					return accionListarGruposServicios(mapping, forma, con, "servicio", usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario());
				}
				else if (estado.equals("grabarServicios"))
				{
					return accionGrabarServicios(mapping, forma, con, "servicio", usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario());
				}
				else if (estado.equals("eliminarServicios"))
				{
					return accionEliminarServicios(mapping, forma, con, "servicio", usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario());
				}
				else if (estado.equals("redireccion"))
				{			    
					UtilidadBD.cerrarConexion(con);
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
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
	 * Metodo para eliminar los servicios.
	 * @param mapping
	 * @param forma
	 * @param con
	 * @param pagina
	 * @param institucion
	 * @param loginUsuario
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionEliminarServicios(ActionMapping mapping, CuentaServicioForm forma, Connection con, String pagina, int institucion, String loginUsuario) throws SQLException
	{
		CuentaServicio mundo = new CuentaServicio();
		llenarMundo(forma, mundo);
		mundo.eliminar(con, 3, loginUsuario); //------ El Cero (3) Se va a Borrar SERVICIOS.
		
		HashMap hm = new HashMap();							//------Cargando la información en el mapa La nformacion ingresada. 
		hm = mundo.cargarGrupos(con, 3, institucion);
		
		
		forma.getMapa().putAll(hm);			 
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward(pagina);
	}
	
	/**
	 * Metodo para insertar los servicios.
	 * @param mapping
	 * @param forma
	 * @param con
	 * @param pagina
	 * @param institucion
	 * @param loginUsuario
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionGrabarServicios(ActionMapping mapping, CuentaServicioForm forma, Connection con, String pagina, int institucion, String loginUsuario) throws SQLException
	{
		CuentaServicio mundo = new CuentaServicio();
		
		llenarMundo(forma, mundo);
		mundo.insertarGrupos(con, 3, loginUsuario); 		//------El Dos es para indicar que se insertan especialidades de servicios.
				
		HashMap hm = new HashMap();							//------Cargando la información en el mapa La nformacion ingresada. 
		hm = mundo.cargarGrupos(con, 3, institucion);
		
		forma.getMapa().putAll(hm);			 
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward(pagina);
	}

	/**
	 * Metodo para guardar las especialidades.
	 * @param mapping
	 * @param forma
	 * @param con
	 * @param string
	 * @param codigoInstitucionInt
	 * @param loginUsuario
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionGrabarEspecialidades(ActionMapping mapping, CuentaServicioForm forma, Connection con, String pagina, int institucion, String loginUsuario) throws SQLException
	{
		CuentaServicio mundo = new CuentaServicio();
		
		llenarMundo(forma, mundo);
		mundo.insertarGrupos(con, 2, loginUsuario); 		//------El Dos es para indicar que se insertan especialidades de servicios.
				
		HashMap hm = new HashMap();							//------Cargando la información en el mapa La nformacion ingresada. 
		hm = mundo.cargarGrupos(con, 2, institucion);
		
		forma.setMapa("nroRegEspecialidades", hm.get("numRegistros")+"");
		hm.remove("numRegistros");
		forma.getMapa().putAll(hm);
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward(pagina);
	}
	
	
	/**
	 * Metodo para eliminar las especialidades.
	 * @param mapping
	 * @param forma
	 * @param con
	 * @param string
	 * @param codigoInstitucionInt
	 * @param loginUsuario
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionEliminarEspecialidades(ActionMapping mapping, CuentaServicioForm forma, Connection con, String pagina, int institucion, String loginUsuario) throws SQLException
	{
		CuentaServicio mundo = new CuentaServicio();
		llenarMundo(forma, mundo);
		mundo.eliminar(con, 2, loginUsuario); 				//------ El Cero (2) Se va a Borrar Especialidades.
		
		HashMap hm = new HashMap();							//------ Cargando la información en el mapa La nformacion ingresada. 
		hm = mundo.cargarGrupos(con, 2, institucion);

		
		forma.setMapa("nroRegEspecialidades", hm.get("numRegistros")+"");
		hm.remove("numRegistros");
		forma.getMapa().putAll(hm);
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward(pagina);
	}

	/**
	 * Metodo para guardar los tipos de servicios.
	 * @param mapping
	 * @param forma
	 * @param con
	 * @param pagina
	 * @param institucion
	 * @param loginUsuario
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionGrabarTiposServicios(ActionMapping mapping, CuentaServicioForm forma, Connection con, String pagina, int institucion, String loginUsuario) throws SQLException
	{
		CuentaServicio mundo = new CuentaServicio();
		
		llenarMundo(forma, mundo);
		mundo.insertarGrupos(con, 1, loginUsuario); 		//------ El Cero Es para indicar que se insertan los grupos los Grupos de servicios.
				
		HashMap hm = new HashMap();							//------ Cargando la información en el MAPA La informacion ingresada. 
		hm = mundo.cargarGrupos(con, 1, institucion);
		
		forma.setMapa("nroRegTiposServicios", hm.get("numRegistros"));
		
		forma.getMapa().putAll(hm);			
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward(pagina);
	}

	/**
	 * Metodo para eliminar los tipos de servicios.
	 * @param mapping
	 * @param forma
	 * @param con
	 * @param pagina
	 * @param institucion
	 * @param loginUsuario
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionEliminarTiposServicios(ActionMapping mapping, CuentaServicioForm forma, Connection con, String pagina, int institucion, String loginUsuario) throws SQLException
	{
		CuentaServicio mundo = new CuentaServicio();
		llenarMundo(forma, mundo);
		mundo.eliminar(con, 1, loginUsuario); //------ El Cero (1) Se va a Borrar Tipos de Servicios
		
		HashMap hm = new HashMap();							//------ Cargando la información en el MAPA La informacion ingresada. 
		hm = mundo.cargarGrupos(con, 1, institucion);
		
		
		forma.setMapa("nroRegTiposServicios", hm.get("numRegistros"));
		
		forma.getMapa().putAll(hm);			
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward(pagina);
	}

	
	/**
	 * Metodo para guardar los grupos de servicios.
	 * @param mapping
	 * @param forma
	 * @param loginUsuario 
	 * @param con 
	 * @param con
	 * @param institucion
	 * @return
	 * @throws SQLException 
	 */
	
	private ActionForward accionGrabarGruposServicios(ActionMapping mapping, CuentaServicioForm forma, Connection con, int institucion, String loginUsuario) throws SQLException
	{
		CuentaServicio mundo = new CuentaServicio();
		
		
		llenarMundo(forma, mundo);
		mundo.insertarGrupos(con, 0, loginUsuario); 		//------El Cero Es para indicar que se insertan los grupos los Grupos de servicios
				
		HashMap hm = new HashMap();							//------Cargando la información en el MAPA.
		hm = mundo.cargarGrupos(con, 0, institucion);
		
		forma.getMapa().putAll(hm);			
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("claseServicio");
	}

	
	/**
	 * Metodo para eliminar los grupos de servicios.
	 * @param mapping
	 * @param forma
	 * @param loginUsuario 
	 * @param con 
	 * @param con
	 * @param institucion
	 * @return
	 * @throws SQLException 
	 */
	
	private ActionForward accionEliminarGruposServicios(ActionMapping mapping, CuentaServicioForm forma, Connection con, int institucion, String loginUsuario) throws SQLException
	{
		CuentaServicio mundo = new CuentaServicio();
		llenarMundo(forma, mundo);
		mundo.eliminar(con, 0, loginUsuario); //------ El Cero (0) Se va a Borrar Grupos de Servicios.

		HashMap hm = new HashMap();			  //------Cargando la información en el MAPA.
		hm = mundo.cargarGrupos(con, 0, institucion);
		
		
		forma.getMapa().putAll(hm);			
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("claseServicio");
	}
	
	/**
	 * Metodo para cargar la información
	 * @param forma
	 * @param mundo
	 */

	private void llenarMundo(CuentaServicioForm forma, CuentaServicio mundo) 
	{
		mundo.setCentroCosto(forma.getCentroCosto()); 
		mundo.setMapa(forma.getMapa());
		
	}

	/**
	 * Metodo que inicializa la informacion de la funcionalidad
	 * @param mapping
	 * @param request
	 * @param forma
	 * @param con 
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionEmpezar(ActionMapping mapping, CuentaServicioForm forma, Connection con) throws SQLException
	{
		forma.reset();
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("claseServicio");
	}

	/**
	 * Metodo que lista los grupos de servicios seleccionado un centro de costo. 
	 * @param mapping
	 * @param request
	 * @param forma
	 * @param con
	 * @param loginUsuario 
	 * @param CodigoInstitucion
	 * @param    
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionListarGruposServicios(ActionMapping mapping, CuentaServicioForm forma, Connection con, String  pagina, int institucion, String loginUsuario)  throws SQLException
	{
		CuentaServicio mundo = new CuentaServicio();
		

		if (forma.getTipoListado().equals("gruposServicios"))
	    {
			
			llenarMundo(forma, mundo);
			
			
			HashMap hm = new HashMap();
			hm = mundo.cargarGrupos(con, 0, institucion);   //------El Cero Es para indicar los Grupos de servicios.
			forma.getMapa().putAll(hm);
	
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward(pagina);
	    }	
		
	    if (forma.getTipoListado().equals("tiposServicios"))
	    {
			llenarMundo(forma, mundo);
			HashMap hm = new HashMap();
			
			hm = mundo.cargarGrupos(con, 1, institucion);   //------El Uno Es para indicar los Tipos de Servicios. 
			
			forma.setMapa("nroRegTiposServicios", hm.get("numRegistros")+"");
			hm.remove("numRegistros");

			forma.getMapa().putAll(hm);
			
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward(pagina);
	    }	

	    if (forma.getTipoListado().equals("especialidades"))
	    {
			llenarMundo(forma, mundo);
			HashMap hm = new HashMap();
			
			hm = mundo.cargarGrupos(con, 2, institucion);   //------El Uno Es para indicar las Especialidades de los Servicios. 
			
			forma.setMapa("nroRegEspecialidades", hm.get("numRegistros")+"");
			hm.remove("numRegistros");

			forma.getMapa().putAll(hm);
			
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward(pagina);
	    }	
	    
	    if (forma.getTipoListado().equals("servicios"))
	    {
			llenarMundo(forma, mundo);
			HashMap hm = new HashMap();
			
			hm = mundo.cargarGrupos(con, 3, institucion);   //------El Uno Es para indicar las Especialidades de los Servicios. 
			
			forma.setMapa("nroRegServicios", hm.get("numRegistros")+"");
			hm.remove("numRegistros");

			forma.getMapa().putAll(hm);
			
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward(pagina);
	    }	

	
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}
	
	
	
}