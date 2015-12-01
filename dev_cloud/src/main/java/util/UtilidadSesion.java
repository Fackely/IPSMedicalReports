/*
 * @(#)UtilidadSesion.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package util;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.axioma.util.log.Log4JManager;

import com.princetonsa.mundo.ObservableBD;

/**
 * Clase auxiliar para saber si una sesión esta viva o no. Se usa para evitar
 * el acceso a paginas JSP (sobre todo a los menus dinámicos) que pueden
 * generar errores en tiempo de Ejecución que un usuario Nunca debería 
 * ver 
 *
 * @version 1.0, Jun 20, 2003
 */

public class UtilidadSesion implements Serializable, HttpSessionBindingListener
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(UtilidadSesion.class);
	
	/**
	 * Empieza en true, porque se pone en la sesión 
	 */
	public boolean sesionActiva=true;
	
	/**
	 * Listener que le indica a este objeto cuándo fue añadido a una sesión. 
	 * @param event objeto que encapsula este evento
	 */
	public void valueBound(HttpSessionBindingEvent event) 
	{
		sesionActiva=true;
	}

	/**
	 * Listener que le indica a este objeto cuándo fue removido de una sesión.
	 * @param event objeto que encapsula este evento
	 */
	public void valueUnbound(HttpSessionBindingEvent event) 
	{
		// Cuando se remueve este dato de sesión ya sea explicitamente (Nunca se da este caso),
		//por logout o vencimiento de sesión, en este caso se cambia el estado del objeto 
		sesionActiva=false;
	}

	/**
	 * Método que notifica cambios al observer
	 * @param paciente
	 */
	public static void notificarCambiosObserver (int codigoPersona, ServletContext servletContext)
	{
		ObservableBD observable = (ObservableBD)servletContext.getAttribute("observable");
		if (observable != null) 
		{
			synchronized (observable) 
			{		
				observable.setChanged();
				observable.notifyObservers(new Integer(codigoPersona));
			}
		}
	}
	
	
	/**
	 * Método que notifica cambios al observer
	 * @param paciente
	 */
	public static void notificarCambiosObserverXIngreso (String idIngreso,int codigoPersona, ServletContext servletContext)
	{
		ObservableBD observable = (ObservableBD)servletContext.getAttribute("observable");
		if (observable != null) 
		{
			synchronized (observable) 
			{		
				observable.setChanged();
				observable.notifyObservers(new String(codigoPersona+ConstantesBD.separadorSplit+idIngreso));
			}
		}
	}

	/**
	 * Metodo que redirecciona el pager.
	 * muy util en las funcionalidades donde modifico,inserto,elimino en la misma página y que tiene pager.
	 * @param linkSiguiente. en caso de que toqur retornar a la pagina actual, es decir al ultimo linkSiguiente que se retorno. si no trabajamos con link siguiente enviar vacio.
	 * @param maxPageItems
	 * @param numeroElmentosListado
	 * @param response
	 * @param request
	 * @param enlace
	 * @param calcularOffset
	 * @return
	 */
	public static ActionForward redireccionar(String linkSiguiente, int maxPageItems, int numeroElmentosListado, HttpServletResponse response, HttpServletRequest request, String enlace, boolean calcularOffset)
	{
		int offset; 
		if(!calcularOffset)
		{
			if(/*request.getParameter("ultimaPage")==null||*/linkSiguiente.trim().equals(""))
			{
				try 
				{
					 response.sendRedirect(enlace+"?pager.offset=0");
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			else
			{    
				try 
				{
					logger.info("paso por aqui con link siguiente: "+linkSiguiente);
					response.sendRedirect(linkSiguiente);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
			return null;
		}
		else
		{
			int numRegistros = numeroElmentosListado;
			offset=((int)((numRegistros-1)/maxPageItems))*maxPageItems;
			if(request.getParameter("ultimaPage")==null)
			{
				if(numRegistros > (offset+maxPageItems))
					offset=((int)(numRegistros)/maxPageItems)*maxPageItems;
				
				try 
				{
					 response.sendRedirect(enlace+"?pager.offset="+offset);
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			else
			{    
				String ultimaPagina=request.getParameter("ultimaPage");
				String tempOffset="offset=";
				int posOffSet=ultimaPagina.indexOf(tempOffset)+tempOffset.length();
				if(numRegistros>(offset+maxPageItems))
					offset=offset+maxPageItems;
				try 
				{
					response.sendRedirect(ultimaPagina.substring(0,posOffSet)+offset);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
			return null;
		}
	}
	
	/**
	 * Metyodo encargado de calcular el offset del pager
	 * @author Jhony Alexander Duque A.
	 * @param maxPageItems
	 * @param numRegistros
	 * @param request
	 * @return
	 */
	public static int obtenerOffset( int maxPageItems, int numRegistros, HttpServletRequest request)
	{
		int offset=0; 
			
		offset=((int)((numRegistros-1)/maxPageItems))*maxPageItems;
		if(request.getParameter("ultimaPage")==null)
		{
			if(numRegistros > (offset+maxPageItems))
				offset=((int)(numRegistros)/maxPageItems)*maxPageItems;
			
			return offset;
		}
		else
		{
			if(numRegistros>(offset+maxPageItems))
				offset=offset+maxPageItems;
			return offset;
		}
			
	}

	/**
	 * Metodo encargado de calcular el numero de la pagina en la que va.
	 * @author Jhony Alexander Duque.
	 * @param maxPageItems
	 * @param numRegistros
	 * @return
	 */
	public static int obtenerCurrentPageNumber( int maxPageItems, int numRegistros)
	{
		double currentPageNumber=0;
		String cadena="";
	
		currentPageNumber=(Utilidades.convertirADouble(numRegistros+"")/Utilidades.convertirADouble(maxPageItems+""));
			
		try 
		{
			cadena=(currentPageNumber+"").replace(".", "@@@@");
	    	String vector[]=cadena.split("@@@@");
	    	
	    	if (vector.length>1)
	    	{
	    		if (Utilidades.convertirAEntero(vector[1])>0)
	    			return Utilidades.convertirAEntero(vector[0])+1;
	    		else
	    			return Utilidades.convertirAEntero(vector[0]);
	    	}
	    	else
	    		return Utilidades.convertirAEntero(vector[0]);
	    	
		} catch (Exception e) 
		{
			logger.info("\n problema al obtener CurrentPageNumber "+e);
		}
		
		return 0;
		
	}

	/**
	 * 
	 * @param con
	 * @param codigoSession
	 * @param loginUsuario
	 * @param id
	 */
	public static void insertarUsuarioSession(Connection con,int codigoSession, String loginUsuario, String id) 
	{
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("insert into login_usuarios_activos(codigo,id_session,login) values(?,?,?)"));
			ps.setInt(1, codigoSession);
			ps.setString(2, id);
			ps.setString(3, loginUsuario);
			ps.executeUpdate();
			
			//llenar historico
			ps= new PreparedStatementDecorator(con.prepareStatement("insert into login_usuarios(codigo,id_session,login) values(?,?,?)"));
			ps.setInt(1, codigoSession);
			ps.setString(2, id);
			ps.setString(3, loginUsuario);
			ps.executeUpdate();
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
				
	}

	/**
	 * 
	 * @param convertirAEntero
	 */
	public static void eliminarUsuarioSession(int codigoSession) 
	{
		try 
		{
			Connection con=UtilidadBD.abrirConexion();
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("DELETE FROM login_usuarios_activos where codigo=?"));
			ps.setInt(1, codigoSession);
			ps.executeUpdate();
			
			String tipoBD = System.getProperty("TIPOBD");
			String consulta = "";
			consulta = "UPDATE login_usuarios set fecha_logout=current_date,hora_logout="+ValoresPorDefecto.getSentenciaHoraActualBD()+",activo='"+ConstantesBD.acronimoNo+"' where codigo=?";
			
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setInt(1, codigoSession);
			ps.executeUpdate();
			UtilidadBD.cerrarConexion(con);
			
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}

	
	
	public static void eliminarUsuariosSession(Connection con) 
	{
		PreparedStatement pst=null;
		PreparedStatement pst2=null;
		String consultaUpdate = null;
		String consultaDelete = null;
		try 
		{
			String tipoBD = System.getProperty("TIPOBD");
			if(tipoBD.equals("POSTGRESQL"))
			{
				consultaUpdate = "UPDATE login_usuarios set fecha_logout=current_date,hora_logout=substr(current_time||'',0,8),activo='"+ConstantesBD.acronimoNo+"' where codigo in (SELECT lua.codigo from login_usuarios_activos lua)";
			}
			else if(tipoBD.equals("ORACLE"))
			{
				consultaUpdate = "UPDATE login_usuarios set fecha_logout=(SELECT sysdate FROM dual), hora_logout='"+UtilidadFecha.getHoraActual(con)+"',activo='"+ConstantesBD.acronimoNo+"' where codigo in (SELECT lua.codigo from login_usuarios_activos lua)";	
			}
			pst= con.prepareStatement(consultaUpdate);
			pst.executeUpdate();
			consultaDelete="DELETE FROM login_usuarios_activos";
			pst2= con.prepareStatement(consultaDelete);
			int numeroUsuarios=pst2.executeUpdate();
			logger.warn("Se cancelaron "+numeroUsuarios+" sesiones.");
		} 
		catch (SQLException e) 
		{
			Log4JManager.error("ERROR eliminarUsuariosSession", e);
		}
		finally{
			try{
				if(pst2!=null){
					pst2.close();
				}
				if(pst!=null){
					pst.close();
				}
			}catch (SQLException sql) {
				Log4JManager.error("ERROR eliminarUsuariosSession eliminar Objetos Persistentes", sql);
			}
		}
	}
	
	public static int obtenerNumeroSessionesUsuario(Connection con,String loginUsuario)
	{
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("SELECT count(1) from login_usuarios_activos where login=?"));
			ps.setString(1, loginUsuario);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return rs.getInt(1);
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	
	
}
