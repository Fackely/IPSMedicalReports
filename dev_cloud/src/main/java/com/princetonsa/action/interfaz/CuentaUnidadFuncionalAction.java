/*
 * Creado el Apr 18, 2006
 * por Julian Montoya
 */
package com.princetonsa.action.interfaz;

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

import util.UtilidadBD;
import util.UtilidadCadena;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.interfaz.CuentaUnidadFuncionalForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.interfaz.CuentaUnidadFuncional;


public class CuentaUnidadFuncionalAction extends Action 
{

	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(CuentaUnidadFuncionalAction.class);
	
	
	/**
	 * Metodo para manejar la navegacion.
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
	{

		Connection con=null;
		try{

			if (form instanceof CuentaUnidadFuncionalForm)
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

				CuentaUnidadFuncionalForm forma = (CuentaUnidadFuncionalForm) form;
				String estado=forma.getEstado();

				logger.warn("\n\n  El Estado en CuentaUnidadFuncionalAction [" + estado + "] \n\n ");

				//-----Definir el numero de registros para el paginador.
				if ( UtilidadCadena.noEsVacio(ValoresPorDefecto.getMaxPageItems( usuario.getCodigoInstitucionInt() )) ) { forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems( usuario.getCodigoInstitucionInt() ))); }
				else { forma.setMaxPageItems(20); }

				if (estado.equals("empezar"))
				{
					return accionEmpezar(mapping, forma, con, usuario.getCodigoInstitucionInt());
				}
				else if (estado.equals("grabarUnidadesFuncionales"))
				{
					return accionGrabarUnidadesFuncionales(mapping, forma, con, usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario());
				} 
				else if (estado.equals("consutarUnidadFuncional"))
				{
					return accionConsutarUnidadFuncional(mapping, forma, con, usuario.getCodigoInstitucionInt());
				} 
				else if (estado.equals("grabarUnidadFuncionalCc"))
				{
					return grabarUnidadFuncionalCc(mapping, forma, con, usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario());
				} 
				else if (estado.equals("eliminar"))  //-- Para eliminar la Cuenta Ingreso ó Medicamento de una Unidad especifica.
				{
					return accionEliminar(mapping, forma, con, usuario);
				} 
				else if (estado.equals("eliminarCc"))  //-- Para eliminar la Cuenta Ingreso ó Medicamento de una Unidad especifica.
				{
					return accionEliminarCc(mapping, forma, con, usuario);
				}
				else if (estado.equals("eliminarRubro"))  //-- Para eliminar la Cuenta Rublo Presupuestal.
				{
					return accionEliminarRubro(mapping, forma, con, usuario);
				}
				else if (estado.equals("eliminarRubroCc"))  //-- Para eliminar la Cuenta Rublo Presupuestal.
				{
					return accionEliminarRubroCC(mapping, forma, con, usuario);
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
	 * Metodo para eliminar Unidades Funcionales por centro de atencion
	 * @throws SQLException 
	 * 
	 */
	private ActionForward accionEliminarCc(ActionMapping mapping, CuentaUnidadFuncionalForm forma, Connection con, UsuarioBasico usuario) throws SQLException
	{
		CuentaUnidadFuncional mundo = new CuentaUnidadFuncional();
		
		llenarMundo(forma, mundo);
	
		//--Eliminar 
		mundo.eliminarCuenta(con, usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario()); 
	
		//----Consultar las unidades funcionales
		forma.getMapa().putAll( mundo.cargarUnidadesFuncionales(con, 1, usuario.getCodigoInstitucionInt()) );
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("unidadFuncionalCentroCosto"); 
	}


	/**
	 * Metodo para eliminar la Cuenta Ingreso ó Medicamento de una Unidad especifica.
	 * @throws SQLException 
	 */
	private ActionForward accionEliminar(ActionMapping mapping, CuentaUnidadFuncionalForm forma, Connection con, UsuarioBasico usuario) throws SQLException 
	{
		CuentaUnidadFuncional mundo = new CuentaUnidadFuncional();
		llenarMundo(forma, mundo);
		
		//--Eliminar 
		mundo.eliminarCuenta(con, usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario()); 
		
		return accionEmpezar(mapping, forma, con, usuario.getCodigoInstitucionInt());
	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param con
	 * @param usuario
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionEliminarRubro(ActionMapping mapping, CuentaUnidadFuncionalForm forma, Connection con, UsuarioBasico usuario) throws SQLException
	{
		CuentaUnidadFuncional mundo = new CuentaUnidadFuncional();
		llenarMundo(forma, mundo);
		
		//--Eliminar 
		mundo.eliminarRubro(con, usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario()); 
		
		return accionEmpezar(mapping, forma, con, usuario.getCodigoInstitucionInt());
	}
	
	
	private ActionForward accionEliminarRubroCC(ActionMapping mapping, CuentaUnidadFuncionalForm forma, Connection con, UsuarioBasico usuario) throws SQLException
	{
		CuentaUnidadFuncional mundo = new CuentaUnidadFuncional();
		
		llenarMundo(forma, mundo);
	
		//--Eliminar 
		mundo.eliminarRubro(con, usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario()); 
	
		//----Consultar las unidades funcionales
		forma.getMapa().putAll( mundo.cargarUnidadesFuncionales(con, 1, usuario.getCodigoInstitucionInt()) );
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("unidadFuncionalCentroCosto");
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
	private ActionForward grabarUnidadFuncionalCc(ActionMapping mapping, CuentaUnidadFuncionalForm forma, Connection con, int institucion, String loginUsuario) throws SQLException
	{
		CuentaUnidadFuncional mundo = new CuentaUnidadFuncional();
		
		llenarMundo(forma, mundo);
		
		//----El Cero es para indicar que se insertara la cuenta de ingreso para las unidades funcionales
		mundo.insertarUnidadesFuncionales(con, 1, institucion, loginUsuario);   		
		
		//----Consultar las unidades funcionales
		forma.getMapa().putAll(mundo.cargarUnidadesFuncionales(con, 1, institucion));
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("unidadFuncionalCentroCosto"); 
	}

	/**
	 * Metodo para consultar la información de una Unidad funcional Asociada a los centros de costo.  
	 * @param mapping
	 * @param forma
	 * @param con
	 * @param institucion 
	 * @param codigoInstitucionInt
	 * @param loginUsuario
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionConsutarUnidadFuncional(ActionMapping mapping, CuentaUnidadFuncionalForm forma, Connection con, int institucion) throws SQLException
	{
		CuentaUnidadFuncional mundo = new CuentaUnidadFuncional();
		
		llenarMundo(forma, mundo);
		
		
		//----Consultar las unidades funcionales ( se coloca putall para que no se borre la unidad funcional seleccionada )
		forma.getMapa().putAll(mundo.cargarUnidadesFuncionales(con, 1, institucion));
		
		//-- Cargar Las Cuentas Contables.
		forma.setMapaCuenta( mundo.cargarUnidadesFuncionales(con, 2, institucion) );
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("unidadFuncionalCentroCosto");

	}


	/**
	 * Metodo que inicializa la informacion de la funcionalidad
	 * @param mapping
	 * @param request
	 * @param forma
	 * @param con 
	 * @param institucion 
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionEmpezar(ActionMapping mapping, CuentaUnidadFuncionalForm forma, Connection con, int institucion) throws SQLException
	{
		CuentaUnidadFuncional mundo = new CuentaUnidadFuncional();
		
		//----Limpiar la Información
		forma.reset();
		
		//----Consultar las unidades funcionales (para no borrar la unidad funcional seleccionada).
		forma.setMapa(mundo.cargarUnidadesFuncionales(con, 0, institucion));
		
		//-- Cargar Las Cuentas Contables.
		forma.setMapaCuenta( mundo.cargarUnidadesFuncionales(con, 2, institucion) );
		
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("unidadFuncional");
	}

	
	/**
	 * Metodo para insertar las unidades funcionales.
	 * @param mapping
	 * @param forma
	 * @param con
	 * @param pagina
	 * @param institucion
	 * @param loginUsuario
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionGrabarUnidadesFuncionales(ActionMapping mapping, CuentaUnidadFuncionalForm forma, Connection con, int institucion, String loginUsuario) throws SQLException
	{
		CuentaUnidadFuncional mundo = new CuentaUnidadFuncional();
		
		llenarMundo(forma, mundo);
		
		//----El Cero es para indicar que se insertara la cuenta de ingreso para las unidades funcionales
		mundo.insertarUnidadesFuncionales(con, 0, institucion, loginUsuario);   		
		
		//----Consultar las unidades funcionales
		forma.setMapa(mundo.cargarUnidadesFuncionales(con, 0, institucion));
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("unidadFuncional"); 
	}

		/**
		 * Metodo para cargar la información
		 * @param forma
		 * @param mundo
		 */
	
		private void llenarMundo(CuentaUnidadFuncionalForm forma, CuentaUnidadFuncional mundo) 
		{
			mundo.setCentroCosto(forma.getCentroCosto()); 
			mundo.setNombreUnidadFuncional(forma.getNombreUnidadFuncional()); 
			mundo.setMapa(forma.getMapa()); 
			
		}
}