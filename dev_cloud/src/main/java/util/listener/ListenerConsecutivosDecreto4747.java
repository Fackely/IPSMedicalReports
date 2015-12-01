package util.listener;

import java.sql.Statement;
import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

public class ListenerConsecutivosDecreto4747 extends Thread implements ServletContextListener {

	
	/**
	 * Variable que activa el hilo
	 */
	private boolean valor;
	
	/**
	 * 
	 * */
	private int codigoInstitucion = 2;
	
	/**
	 * Manejador de logs de la clase
	 */
	private Logger logger=Logger.getLogger(ListenerConsecutivosDecreto4747.class);
	

	/**
	 * Método invocado cuando se baja el contexto
	 */
	public void contextDestroyed(ServletContextEvent arg0) 
	{		
		interrupt();
	}

	/**
	 * Método invocado cuando se sube el contexto
	 */
	public void contextInitialized(ServletContextEvent arg0) 
	{
		logger.info("SE INICIA HILO CONSECUTIVOS DISPONIBLES DECRETO 4747");
		this.valor = false;		
		this.codigoInstitucion = Utilidades.convertirAEntero(arg0.getServletContext().getInitParameter("CODIGOINSTITUCION"));
		start();		
	}

	/**
	 * Método donde se corre el hilo 
	 */
	public void run() 
	{
		Connection con = null;
		while(con == null)
		{
		    try{
		    	con = UtilidadBD.abrirConexion();			
			}
			catch(Exception e)
			{
				logger.info("\n\n\n\n Error abriendo conexion");
				con = null;
				try {
					sleep(30000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		try 
		{
			String fechaActual = UtilidadFecha.getFechaActual(con); 
			String horaActual = UtilidadFecha.getHoraActual(con);
			UtilidadBD.closeConnection(con);
			
			//Espera un minuto para iniciar
			if(!valor)
			{
				if(!horaActual.equals("00:00"))
				{
					long milisegundosFalta = UtilidadFecha.numeroMilisegundosEntreFechas(
							fechaActual,
							horaActual,
							UtilidadFecha.incrementarDiasAFecha(fechaActual,1,false),
							"00:00");
					
					logger.info("Proxima Ejecucion >> "+milisegundosFalta);
					
					//..
					sleep(milisegundosFalta);
					con = UtilidadBD.abrirConexion();
					fechaActual = UtilidadFecha.getFechaActual(con); 
					horaActual = UtilidadFecha.getHoraActual(con);
					UtilidadBD.closeConnection(con);
				}	
				else{
					UtilidadBD.closeConnection(con);
				}
								
				if(horaActual.equals("00:00")){
					this.valor = true;					
				}
			}
				
			while(valor)
			{
				if(horaActual.equals("00:00"))
				{
					con = UtilidadBD.abrirConexion();
					//Consecutivo Informe Inconsistencias en Verificación de Base de Datos
					consecutivoInfInconVerifBD(con,fechaActual,horaActual);
					//Consecutivo Informe Atención Inicial de Urgencias
					consecutivoInfAtenIniUrg(con, fechaActual, horaActual);
					//Consecutivo Solicitud Autorizaciones
					if(fechaActual.split("/").length > 0 
							&& fechaActual.split("/")[0].equals("01") 
								&& fechaActual.split("/")[1].equals("01"))
						consecutivoSoliAutori(con, fechaActual, horaActual);				
					
					
					logger.info("Actualizo Informacion de Consecutivos");
					fechaActual = UtilidadFecha.getFechaActual(con); 
					horaActual = UtilidadFecha.getHoraActual(con);
					UtilidadBD.closeConnection(con);
				}
				
				long milisegundosFalta = UtilidadFecha.numeroMilisegundosEntreFechas(
						fechaActual,
						horaActual,
						UtilidadFecha.incrementarDiasAFecha(fechaActual,1,false),
						"00:00");
				
				logger.info("Proxima Ejecucion >> "+milisegundosFalta);
				//...
				sleep(milisegundosFalta);
				
				con = UtilidadBD.abrirConexion();
				fechaActual = UtilidadFecha.getFechaActual(con); 
				horaActual = UtilidadFecha.getHoraActual(con);
				UtilidadBD.closeConnection(con);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();			
		}
	}
	
	//***********************************************************************************************
	
	/**
	 * Consecutivo Informe Inconsistencias en Verificación de Base de Datos
	 * @param Connection con
	 * @param String fechaActual
	 * @param String horaActual
	 * */
	public void consecutivoInfInconVerifBD(Connection con,String fechaActual,String horaActual)
	{
		try
		{
			String cadenaConsulta = "SELECT " +
									"valor,anio_vigencia " +
									"FROM consecutivos " +
									"WHERE nombre = 'consecutivo_infor_incon_veribd' AND institucion = "+this.codigoInstitucion;
			String anio = "";
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(cadenaConsulta));

			if(rs.next())
			{
				if(rs.getString("anio_vigencia").equals(""))
					anio = "";
				else
				{
					if(fechaActual.split("/").length > 0)
						anio = fechaActual.split("/")[2];
				}

				String cadenaActualizacion = "UPDATE consecutivos " +
											 "SET valor = '1', anio_vigencia = '"+anio+"' "+
											 "WHERE nombre = 'consecutivo_infor_incon_veribd' " +
											 "AND valor != '1' AND valor IS NOT NULL AND valor != '' AND institucion = "+this.codigoInstitucion;

				st.executeUpdate(cadenaActualizacion);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	//*******************************************************************************************************
	
	/**
	 * Consecutivo Informe Atención Inicial de Urgencias
	 * @param Connection con
	 * @param String fechaActual
	 * @param String horaActual
	 * */
	public void consecutivoInfAtenIniUrg(Connection con,String fechaActual,String horaActual)
	{
		try
		{
			String cadenaConsulta = "SELECT " +
									"valor,anio_vigencia " +
									"FROM consecutivos " +
									"WHERE nombre = 'consecutivo_infor_atenc_inic_urg' AND institucion = "+this.codigoInstitucion;
			String anio = "";
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(cadenaConsulta));

			if(rs.next())
			{
				if(rs.getString("anio_vigencia").equals(""))
					anio = "";
				else
				{
					if(fechaActual.split("/").length > 0)
						anio = fechaActual.split("/")[2];
				}

				String cadenaActualizacion = "UPDATE consecutivos " +
											 "SET valor = '1', anio_vigencia = '"+anio+"' "+
											 "WHERE nombre = 'consecutivo_infor_atenc_inic_urg' " +
											 "AND valor != '1' AND valor IS NOT NULL AND valor != '' AND institucion = "+this.codigoInstitucion;

				st.executeUpdate(cadenaActualizacion);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	//*********************************************************************************************************
	
	/**
	 * Consecutivo Solicitud de Autorizaciones
	 * @param Connection con
	 * @param String fechaActual
	 * @param String horaActual
	 * */
	public void consecutivoSoliAutori(Connection con,String fechaActual,String horaActual)
	{
		try
		{
			String cadenaConsulta = "SELECT " +
									"valor,anio_vigencia " +
									"FROM consecutivos " +
									"WHERE nombre = 'consecutivo_solicitud_autori' AND institucion = "+this.codigoInstitucion;
			String anio = "";
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(cadenaConsulta));

			if(rs.next())
			{
				if(rs.getString("anio_vigencia").equals(""))
					anio = "";
				else
				{
					if(fechaActual.split("/").length > 0)
						anio = fechaActual.split("/")[2];
				}

				String cadenaActualizacion = "UPDATE consecutivos " +
											 "SET valor = '1', anio_vigencia = '"+anio+"' "+
											 "WHERE nombre = 'consecutivo_solicitud_autori' " +
											 "AND valor != '1' AND valor IS NOT NULL AND valor != '' AND institucion = "+this.codigoInstitucion;

				st.executeUpdate(cadenaActualizacion);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//******************************************************************************************************************
}