package util.listener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;

public class ListenerConsecutivos extends Thread implements ServletContextListener {

	
	/**
	 * Variable que activa el hilo
	 */
	private boolean valor;
	
	
	/**
	 * Método invocado cuando se baja el contexto
	 */
	public void contextDestroyed(ServletContextEvent arg0) 
	{
//		this.valor = false;
//		interrupt();
	}

	/**
	 * Método invocado cuando se sube el contexto
	 */
	public void contextInitialized(ServletContextEvent arg0) 
	{
//		Log4JManager.info("Se inicia HILO CONSECUTIVOS DISPONIBLES");
//		this.valor = true;
//		start();
		
	}

	/**
	 * Método donde se corre el hilo que revisa cada 5 minutos
	 * la tabla interfaz_laboratorio
	 */
	public void run() 
	{
//		Connection con=null;
//		PreparedStatement pst=null;
//		PreparedStatement pst2=null;
//		try 
//		{
//			//Se hace una espera mientras sube el contexto
//			while(valor)
//			{
//				try {
//					Log4JManager.info("Esperando....");
//					//Espera de 120 minutos (7200000 milisegundos)
//					sleep(7200000);
//					con=UtilidadBD.abrirConexion();
//					con.setAutoCommit(false);
//					//Llamado a metodo que toma la informacion del laboratorio
//					String tipoBD=System.getProperty("TIPOBD");
//					String cadenaActualizacion="";
//					if(tipoBD.equals("ORACLE"))
//					{
//						cadenaActualizacion="UPDATE administracion.uso_consecutivos set usado='"+ConstantesBD.acronimoNo+"' where  usado='"+ConstantesBD.acronimoSi+"' and finalizado='"+ConstantesBD.acronimoNo+"' and fecha_toma<CURRENT_TIMESTAMP-NUMTODSINTERVAL(30, 'MINUTE')";
//					}
//					else if(tipoBD.equals("POSTGRESQL"))
//					{
//						cadenaActualizacion="UPDATE administracion.uso_consecutivos set usado='"+ConstantesBD.acronimoNo+"' where  usado='"+ConstantesBD.acronimoSi+"' and finalizado='"+ConstantesBD.acronimoNo+"' and fecha_toma<CURRENT_TIMESTAMP-CAST('30 MINUTES' AS INTERVAL)";
//					}
//					String cadenaEliminacion="DELETE FROM administracion.uso_consecutivos where usado='"+ConstantesBD.acronimoSi+"' and finalizado='"+ConstantesBD.acronimoSi+"'";
//					
//					pst= con.prepareStatement(cadenaActualizacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
//					pst.executeUpdate();
//					pst.close();
//					con.commit();
//					
//					pst2= con.prepareStatement(cadenaEliminacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
//					pst2.executeUpdate();
//					pst2.close();
//					con.commit();
//					
//					con.setAutoCommit(true);
//					UtilidadBD.closeConnection(con);
//				} catch (SQLException e) {
//					try {
//						con.rollback();
//						Log4JManager.error("ERROR listener Consecutivos",e);
//					}catch (SQLException sqle) {
//						Log4JManager.error("ERROR rollback listener Consecutivos",e);
//					}
//				}
//			} 
//		}
//		catch (InterruptedException e) 
//		{
//			Log4JManager.info("Se finaliza HILO INTERFAZ CONSECUTIVOS");
//		}
//		finally{
//			try {
//				if(pst!=null && !pst.isClosed()){
//					pst.close();
//				}
//				if(pst2 != null && !pst2.isClosed()){
//					pst2.close();
//				}
//			} catch (SQLException e) {
//				Log4JManager.error("ERROR listener Consecutivos",e);
//			}
//		}

	}
}
