/*
 * Creado el Jun 1, 2006
 * por Julian Montoya
 */
package com.princetonsa.action.historiaClinica;

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
import util.UtilidadSesion;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.historiaClinica.SignosSintomasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.SignosSintomas;

public class SignosSintomasAction extends Action {


	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(SignosSintomasAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
	{
		Connection con = null;
		try {
			if (form instanceof SignosSintomasForm)
			{
				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
					return null;
				} 

				UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

				SignosSintomasForm forma = (SignosSintomasForm) form;
				String estado=forma.getEstado();

				logger.warn("[SignosSintomasAction]  --> " + estado);

				if(estado.equals("empezar"))
				{
					forma.setOperacionExitosa(false);
					return accionEmpezar(forma, con, mapping, usuario);
				} else 
					if(estado.equals("ordenar"))
					{
						return accionOrdenar(forma, con, mapping);
					}
					else if (estado.equals("redireccion"))  //--Estado para mantener los datos del pager
					{
						forma.setOperacionExitosa(false);
						UtilidadBD.cerrarConexion(con);
						response.sendRedirect(forma.getLinkSiguiente());
						return null;
					}
					else if (estado.equals("guardar"))  
					{			    
						return accionGuardar(forma, con, mapping, usuario);
					}
					else if (estado.equals("eliminar"))  
					{			    
						return accionEliminar(forma, con, mapping, usuario);
					}
					else if(estado.equals("nuevoRegistro"))
					{
						return accionNuevo(forma,mapping,usuario, request, response);
					}

			}//if
			return null;
		} catch (Exception e) {
			Log4JManager.error(e);
			return null;
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
	}

	
	/**
	 * 
	 * @param SignosSintomasForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward accionNuevo(SignosSintomasForm forma , ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request,HttpServletResponse response)
	{
		int numregistros=Utilidades.convertirAEntero(forma.getMapa().get("numRegistros")+"");
		forma.getMapa().put("numRegistros", numregistros+1);
		forma.getMapa().put("consecutivo_"+numregistros,"");
		forma.getMapa().put("codigo_"+numregistros,"");
		forma.getMapa().put("signo_"+numregistros,"");
		forma.getMapa().put("h_codigo_"+numregistros,"");
		forma.getMapa().put("h_signo_"+numregistros,"");
		forma.getMapa().put("puedeeliminar_" + numregistros,"true"); 
		return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Utilidades.convertirAEntero(forma.getMapa().get("numRegistros")+""), response, request, "tarjetasFinancieras.jsp",true);
	}
	
	
	/**
	 * Metodo para eliminar 
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionEliminar(SignosSintomasForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) throws SQLException
	{
		SignosSintomas mundo = new SignosSintomas();
		llenarMundo(forma, mundo);
		
		if(forma.getNroRegEliminar()>0){
			if(mundo.eliminar(con, forma.getNroRegEliminar(), usuario.getLoginUsuario())>0){
				forma.setOperacionExitosa(true);
				return accionEmpezar(forma, con, mapping, usuario);
			}
		}else
		{
			int numregistros=Utilidades.convertirAEntero(forma.getMapa().get("numRegistros")+"");
			forma.getMapa().remove("consecutivo_"+forma.getMapa().get("indiceEliminado")+"");
			forma.getMapa().put("numRegistros",numregistros-1);
			forma.setOperacionExitosa(true);
		}
		
		//return accionEmpezar(forma, con, mapping, usuario);
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
	private ActionForward accionGuardar(SignosSintomasForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) throws SQLException
	{
		SignosSintomas mundo = new SignosSintomas();
		llenarMundo(forma, mundo);
		if(mundo.insertar(con, usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario())>0);
			forma.setOperacionExitosa(true);
		return accionEmpezar(forma, con, mapping, usuario);   
	}

	/**
	 * Metodo para pasar la informacion al mundo.
	 * @param forma
	 * @param mundo
	 */
	private void llenarMundo(SignosSintomasForm forma, SignosSintomas mundo)
	{
		mundo.setNroRegistros(forma.getNroRegistros());
		mundo.setMapa(forma.getMapa());
	}

	/**
	 * Metodo que inicializa el estado de la funcionalidad.
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario 
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionEmpezar(SignosSintomasForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) throws SQLException
	{
		SignosSintomas mundo = new SignosSintomas(); 
		
		forma.reset();
		
		forma.setMapa( mundo.cargarSignosSintomas(con, usuario.getCodigoInstitucionInt()) );
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");  
	}

	/**
	 * Metodo para Ordenar el listado de sintomas y signos.   
	 * @param forma
	 * @param con
	 * @param mapping
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionOrdenar(SignosSintomasForm forma, Connection con, ActionMapping mapping) throws SQLException 
	{
		
		String[] indices = {"consecutivo_", "codigo_", "signo_", "codigoN_", "signoN_", "h_codigo_", "h_signo_", "puedeeliminar_"};

		 															
		int num = 0;
		if ( UtilidadCadena.noEsVacio(forma.getMapa().get("numRegistros")+"") )  
			{
				num = Integer.parseInt(forma.getMapa().get("numRegistros")+"");
			}

			
		forma.getMapa().putAll(Listado.ordenarMapa(indices, forma.getPatronOrdenar(),
										                    forma.getUltimoPatronOrdenar(),
										                    forma.getMapa(),
										                    num ));
	    
	    forma.getMapa().put("numRegistros", new Integer(num));
	    forma.setUltimoPatronOrdenar(forma.getPatronOrdenar());
	    UtilidadBD.cerrarConexion(con);
	    return mapping.findForward("principal");
	}

}
