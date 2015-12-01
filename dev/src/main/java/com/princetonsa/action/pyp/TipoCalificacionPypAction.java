/*
 * Creado el Aug 8, 2006
 * por Julian Montoya
 */
package com.princetonsa.action.pyp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

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
import util.UtilidadCadena;

import com.princetonsa.actionform.pyp.TipoCalificacionPypForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.pyp.TipoCalificacionPyp;


public class TipoCalificacionPypAction extends Action  {

	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(TipoCalificacionPypAction.class);
	

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
			if(form instanceof TipoCalificacionPypForm)
			{

				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}

				TipoCalificacionPypForm forma =(TipoCalificacionPypForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				String estado=forma.getEstado();  

				logger.warn("En TipoCalificacionPypAction El Estado [" + estado + "] \n\n");	


				if(estado == null)
				{
					forma.reset();	
					logger.warn("Estado no valido dentro de TipoCalificacionPypAction (NULL) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}			
				else if (estado.equals("empezar"))
				{
					return accionEmpezar(con, mapping,  forma, request, usuario); 
				}
				else if (estado.equals("guardar"))
				{
					return accionGuardar(con, mapping,  forma, request, usuario); 
				}
				else if (estado.equals("eliminar"))
				{
					return accionEliminar(con, mapping,  forma, request, usuario); 
				}
				else if (estado.equals("insertarFila")) //-- Para Insertar una Nueva Fila en el mapa.
				{
					return insertarFila(con, forma, mapping);
				}
				else if (estado.equals("eliminarFila")) //-- Para Eliminar una Nueva Fila en el mapa.
				{
					return eliminarFila(con, mapping, forma);
				}
				else
				{
					forma.reset();
					logger.warn("Estado no valido dentro del flujo de TipoCalificacionPypAction (null) ");
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
	 * Metodo para insertar una fila de nuevos.
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 * @throws SQLException
	 */
	private ActionForward insertarFila(Connection con, TipoCalificacionPypForm forma, ActionMapping mapping) throws SQLException
	{
		if ( UtilidadCadena.noEsVacio(forma.getMapa("nroRegistrosNv")+"") )
		{
			forma.setMapa("nroRegistrosNv", Integer.parseInt(forma.getMapa("nroRegistrosNv")+"")+1 +"");
		}
		else
		{
			forma.setMapa("nroRegistrosNv", "1");
		}
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}

	/**
	 * Metodo para eliminar las filas nuevas ..
	 * @param con
	 * @param mapping
	 * @param forma
	 * @return
	 * @throws SQLException
	 */
	private ActionForward eliminarFila(Connection con, ActionMapping mapping, TipoCalificacionPypForm forma) throws SQLException 
	{
		

		if ( forma.getIndiceEliminado() != -1 )
		{
			int indice = forma.getIndiceEliminado();
			int k = 0, nroRows = 0;

			if ( UtilidadCadena.noEsVacio(forma.getMapa("nroRegistrosNv")+"") )
			{
				nroRows = Integer.parseInt(forma.getMapa("nroRegistrosNv")+"");
			}

			for (int i = 0; i < nroRows; i++)
			{
				if ( i == indice )
				{
					forma.getMapa().remove("tc_cod_nv_"+ i);
					forma.getMapa().remove("tc_nom_nv_"+ i);
					forma.getMapa().remove("tc_act_nv_"+ i);
				}
				else
				{
					forma.getMapa().put("tc_cod_nv_"+ k, forma.getMapa("tc_cod_nv_"+ i)+"");
					forma.getMapa().put("tc_nom_nv_"+ k, forma.getMapa("tc_nom_nv_"+ i)+"");
					forma.getMapa().put("tc_act_nv_"+ k, forma.getMapa("tc_act_nv_"+ i)+"");
					k++;	
				}
			}  
			
			forma.setIndiceEliminado(-1);
			forma.setMapa("nroRegistrosNv", Integer.parseInt(forma.getMapa("nroRegistrosNv")+"")-1 +"");
		}

		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}




	/**
	 * Metodo para eliminar una Articulo de un Programa de Salud PYP Especifico. 
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param usuario
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionEliminar(Connection con, ActionMapping mapping, TipoCalificacionPypForm forma, HttpServletRequest request, UsuarioBasico usuario) throws SQLException
	{
		TipoCalificacionPyp mundo = new TipoCalificacionPyp(); 
		mundo.eliminarTipoCalificacion( con,  forma , usuario );
		return accionEmpezar(con, mapping, forma, request, usuario);
	}

	/**
	 * Metodo para Guardar Modificar Los Articulos por Programa PYP
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param usuario
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionGuardar(Connection con, ActionMapping mapping, TipoCalificacionPypForm forma, HttpServletRequest request, UsuarioBasico usuario) throws SQLException 
	{
		TipoCalificacionPyp mundo = new TipoCalificacionPyp();
		mundo.insertarTipoCalificacion( con, forma, usuario );
		return accionEmpezar(con, mapping, forma, request, usuario);
	}


	/**
	 * Metodo para iniciar el Flujo de la funcionalidad.
	 * @param con
	 * @param mapping 
	 * @param forma
	 * @param request
	 * @param usuario
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionEmpezar(Connection con, ActionMapping mapping, TipoCalificacionPypForm forma, HttpServletRequest request, UsuarioBasico usuario) throws SQLException
	{
		TipoCalificacionPyp mundo = new TipoCalificacionPyp();

		//-- Limpiar la Forma.
		forma.reset();
		
		//-- Cargar Los Programas.
		HashMap mp = new HashMap();
		mp.put("nroConsulta", "1");
		mp.put("codigoInstitucion", "" + usuario.getCodigoInstitucionInt());
		
		forma.setMapa( mundo.consultarInformacion(con, mp) );
		forma.setMapa("nroRegistrosNv","0");

		//--
		UtilidadBD.cerrarConexion(con);
		return  mapping.findForward("principal");
	}
	

}
