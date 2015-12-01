package com.princetonsa.action.capitacion;

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

import com.princetonsa.actionform.capitacion.UnidadPagoForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.capitacion.UnidadPago;

public class UnidadPagoAction extends Action {
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(UnidadPagoAction.class);

	/**
	 * Metodo para manejar la navegacion.
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
	{
		Connection con=null;
		try{
		if (form instanceof UnidadPagoForm)
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
				
			UnidadPagoForm forma = (UnidadPagoForm) form;
			String estado=forma.getEstado();
			logger.warn("\n\n  El Estado en UnidadPagoAction [" + estado + "] \n\n ");
			
			if (estado.equals("empezar"))
			{
				forma.setEsConsulta(false);				
				return accionEmpezar(mapping, forma, con, true);
			}
			if (estado.equals("consultar"))
			{
				forma.setEsConsulta(true);
				return accionEmpezar(mapping, forma, con, true);
			}
			else if (estado.equals("nuevo"))  		//-- Para Insertar una Nueva Fila en el Mapa.
			{
				insertarNuevoMapa(forma);	
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("principal");
			}
			else if (estado.equals("eliminarNuevo"))  		  //-- Para Eliminar Una Fila de los nuevos registros del Mapa.
			{
				return eliminarFilaActividad(con, mapping, forma);
			}
			else if (estado.equals("guardar"))  		  	  //-- Inserta los registros nuevos y los viejos los actualiza. 
			{
				return accionGuardar(con, mapping, forma, usuario.getLoginUsuario());
			}
			else if (estado.equals("eliminarUnidad"))  		   //-- Se elimina un registro.  
			{
				return accionEliminar(con, mapping, forma, usuario.getLoginUsuario());
			}
			else if (estado.equals("redireccion"))  //--Estado para mantener los datos del pager
			{			    
			    UtilidadBD.cerrarConexion(con);

			    //-- Si se esta insertando un nuevo registro.	
			    if (forma.getEnviarUltimaPagina()) 
			    {
					insertarNuevoMapa(forma);	
			    }
			    forma.setEnviarUltimaPagina(false);	
			    response.sendRedirect(forma.getLinkSiguiente());
			    return null;
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
	 * Para adicionar un nuevo registro en el mapa
	 *
	 */
	private void insertarNuevoMapa(  UnidadPagoForm forma )
	{
		if ( UtilidadCadena.noEsVacio(forma.getMapa("nroRegistrosNv")+"") )
		{
			forma.setMapa("nroRegistrosNv", Integer.parseInt(forma.getMapa("nroRegistrosNv")+"")+1 +"");
		}
		else
		{
			forma.setMapa("nroRegistrosNv", "1");
		}
	}


	/**
	 * Metodo para eliminar una unidad de pago.
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param loginUsuario
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionEliminar(Connection con, ActionMapping mapping, UnidadPagoForm forma, String loginUsuario) throws SQLException
	{
		UnidadPago mundo = new UnidadPago();
		llenarMundo(forma, mundo);
		int res = mundo.eliminar(con, loginUsuario); 
		return accionEmpezar(mapping, forma, con, false);
	}


	/**
	 * Metodo para Inserta los registros nuevos y los viejos los actualiza. 
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param loginUsuario 
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionGuardar(Connection con, ActionMapping mapping, UnidadPagoForm forma, String loginUsuario) throws SQLException 
	{
		UnidadPago mundo = new UnidadPago();
		
		llenarMundo(forma, mundo);
		
		//----El Cero es para indicar que se insertara la cuenta de ingreso para las unidades funcionales
		mundo.insertar(con, loginUsuario); 
		
		return accionEmpezar(mapping, forma, con, true);
	}
	
	/**
	 * Enviar la informacion al mundo para registrarla.
	 * @param forma
	 * @param mundo
	 */
	void llenarMundo(UnidadPagoForm forma, UnidadPago mundo)
	{
		mundo.setMapa(forma.getMapa());	
	}

	/**
	 * Metodo para eliminar una fila del Mapa. de los nuevos.
	 * @param con
	 * @param mapping
	 * @param forma
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward eliminarFilaActividad(Connection con, ActionMapping mapping, UnidadPagoForm forma) throws SQLException
	{
		int indice = 0, nroRows = 0;
		

		if ( UtilidadCadena.noEsVacio(forma.getMapa("nroFilaEliminar")+"") )
		{
			indice = Integer.parseInt(forma.getMapa("nroFilaEliminar")+"");
			nroRows = Integer.parseInt(forma.getMapa("nroRegistrosNv")+""); 
			boolean encontro = false;
			int k = 0;	

			
			HashMap mp  = forma.getMapa();
			
			for (int i = 0; i < nroRows; i++)
			{
				if ( i == indice )
				{
					mp.remove("fi_nv_"+ i);
					mp.remove("ff_nv_"+ i);
					mp.remove("valor_nv_"+ i);
					encontro = true;
				}
				else
				{
					mp.put("fi_nv_"+ k, forma.getMapa("fi_nv_"+ i)+"");
					mp.put("ff_nv_"+ k, forma.getMapa("ff_nv_"+ i)+"");
					mp.put("valor_nv_"+ k, forma.getMapa("valor_nv_"+ i)+"");
					k++;	
				}
			}  
			

			forma.setMapa(mp);
			
			//-- Si encontro el indice puede Actualizar el Contador
			if ( encontro )
			{
				forma.getMapa().remove("nroFilaEliminar");
				forma.setMapa("nroRegistrosNv", Integer.parseInt(forma.getMapa("nroRegistrosNv")+"")-1 +"");
			}	
		}

		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}


	/**
	 * Metodo para iniciar la funcionalidad. 
	 * @param mapping
	 * @param forma
	 * @param con
	 * @param resetear 
	 * @param codigoInstitucionInt
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionEmpezar(ActionMapping mapping, UnidadPagoForm forma, Connection con, boolean resetear) throws SQLException
	{
		HashMap MapaParam = new HashMap(); 
		UnidadPago mundo = new UnidadPago();
		
		//----Limpiar la Información
		if (resetear)
		{
			forma.reset();
		}	
		
		//---- Establecer el numero de la consulta.
		MapaParam.put("nroConsulta","1"); 
		
		//----Consultar las unidades funcionales (para no borrar la unidad funcional seleccionada).
		forma.setMapa(mundo.consultarInformacion(con, MapaParam));
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}
	
}
