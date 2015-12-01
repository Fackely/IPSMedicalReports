
package com.princetonsa.dao.sqlbase.historiaClinica;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;

/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * Clase para las transacciones de Sistemas Motivo de Consultas de Urgencias
 * @version 1.0  31 /May/ 2006
 */
public class SqlBaseSistemasMotivoConsultaDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseSistemasMotivoConsultaDao.class);
	
	/**
	 * Cadena con el statement necesario para consultar los sistemas motivos de consulta de urgencias
	 */
	private final static String consultarMotivosConsultaUrgStr=" SELECT mcu.codigo as codigo, " +
															   " mcu.identificador as identificador, " +
															   " mcu.descripcion as descripcion , " +
															   " mcu.institucion as institucion," +
															   " 'si' as existebd " +
															   " FROM motivo_consulta_urg mcu " +
															   " WHERE mcu.institucion = ? " +
															   " ORDER BY identificador ASC ";
	
	
	/**
	 * Hace la modificación de los datos de los motivos de consultas de urgencias
	 */
	private final static String modificarMotivosConsultaUrgStr=" UPDATE motivo_consulta_urg SET " +
															   " identificador = ? , " +
															   " descripcion = ? " +
															   " WHERE codigo = ?";
	
	/**
	 * Cadena con el statement necesario para saber si existe un motivo de consulta de urgencias determinado
	 */
	private final static String existeMotivosConsultaUrgStr=" SELECT count(1) as cantidad " +
													 	    " FROM motivo_consulta_urg " +
													 	    " WHERE codigo = ? ";
													 	  
	
	/**
	 * Cadena con el statement necesario para eliminar un motivo de consulta de urgencias dado su codigo
	 */
	private final static String eliminarMotivoConsultaUrgStr=" DELETE FROM motivo_consulta_urg WHERE codigo = ? ";
	
	/**
	 * Cadena con el statement necesario para saber si un sistema motivo de consulta de urgencias
	 * esta siendo utilizado
	 */
	private static final String estaSiendoUtilizadaStr=" SELECT cod_mot_consulta_urg " +
													   " FROM signos_sintomas_x_sistema " +
													   " WHERE cod_mot_consulta_urg = ? " +
													   " AND institucion = ? ";
	
	
	/**
	 * Método para consultar los sistemas motivos de consulta de urgencias existenes en la base de datos
	 * @param con
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarMotivosConsultaUrg(Connection con , int institucion) throws SQLException
	{
		PreparedStatementDecorator ps = null;
		try
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(consultarMotivosConsultaUrgStr));
			ps.setInt(1, institucion);
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,false);
			ps.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error en consultarMotivosConsultaUrg : [SqlBaseSistemasMotivoConsultaDao] "+e.toString() );
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSistemasMotivoConsultaDao "+sqlException.toString() );
			}
		}
		return null;
	}
	
	
	/**
	 * Método para eliminar un motivo de consulta de urgencias dado su codigo
	 * @param con
	 * @param codigoMotivo
	 * @return int
	 * @throws SQLException
	 */
	public static int eliminarMotivoConsultaUrg(Connection con, int codigoMotivo) throws SQLException
	{
		PreparedStatementDecorator ps = null;
		try
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(eliminarMotivoConsultaUrgStr));
			ps.setDouble(1, Utilidades.convertirADouble(codigoMotivo+""));
			return ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.warn(e.getSQLState()+"Error en eliminarMotivoConsultaUrg : [SqlBaseSistemasMotivoConsultaDao] "+e.toString() );
			return Utilidades.convertirAEntero(e.getSQLState());
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSistemasMotivoConsultaDao "+sqlException.toString() );
			}
		}
		
	}
	
	
	/**
	 * Método para la insercion de un nuevo Motivo de Consulta de Urgencias con todos sus atributos
	 * @param con
	 * @param codigoMotivo
	 * @param descripcion
	 * @param identificador
	 * @param institucion
	 * @param insertarMotivoConsultaUrgStr -> Postgres - Oracle
	 * @return
	 * @throws SQLException
	 */
	public static int insertarMotivoConsultaUrg(Connection con, String codigoMotivo, String descripcion, String identificador, int institucion, String insertarMotivoConsultaUrgStr) throws SQLException
	{
	   ResultSetDecorator rs ;
	   int resp = -1;
	   int temp = 0;
		PreparedStatementDecorator ps = null;
		PreparedStatementDecorator pst = null;
		try
		{
			if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}
			
			 pst =  new PreparedStatementDecorator(con.prepareStatement(existeMotivosConsultaUrgStr));
			pst.setDouble(1, Utilidades.convertirADouble(codigoMotivo));
			rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				temp=rs.getInt("cantidad");
			}
			//Si existe un Motivo lo que hacemos es modificarlo
			if(temp > 0)
			{
				 ps =  new PreparedStatementDecorator(con.prepareStatement(modificarMotivosConsultaUrgStr));
				ps.setDouble(1, Utilidades.convertirADouble(identificador));
				ps.setString(2, descripcion);
				ps.setDouble(3, Utilidades.convertirADouble(codigoMotivo));
				resp = ps.executeUpdate();
				return ConstantesBD.tipoRegistroLogModificacion;
			}
			else
			{
				//Insertamos un nuevo Motivo de Consulta de Urgencias
				 ps =  new PreparedStatementDecorator(con.prepareStatement(insertarMotivoConsultaUrgStr));
				
				/**
				 * INSERT INTO motivo_consulta_urg (
				 * codigo, 
				 * identificador, 
				 * descripcion, 
				 * institucion )  VALUES ('seq_motivo_consulta_urg'), ?, ?, ? )
				 */
				
				ps.setDouble(1, Utilidades.convertirADouble(identificador));
				ps.setString(2, descripcion);
				ps.setInt(3, institucion);
				resp = ps.executeUpdate();
				return resp;
			}
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en insertarMotivoConsultaUrg : [SqlBaseSistemasMotivoConsultaDao] "+e.toString() );
			resp = ConstantesBD.codigoNuncaValido;
			return resp;
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSistemasMotivoConsultaDao "+sqlException.toString() );
			}
		}
	}
	
	
	/**
	 * Método para saber si un sistema motivo de consulta de urgencias esta siendo utilizado en 
	 * la funcionalidad de signos sintomas x sistema
	 * @param con
	 * @param codigoMotivo
	 * @param institucion
	 * @return
	 */
	public static boolean estaSiendoUtilizada(Connection con, int codigoMotivo, int institucion) 
	{
		PreparedStatementDecorator ps = null;
		try
	    {
	        ps=  new PreparedStatementDecorator(con.prepareStatement(estaSiendoUtilizadaStr));
	        
	        /**
	         * SELECT cod_mot_consulta_urg " +
													   " FROM signos_sintomas_x_sistema " +
													   " WHERE cod_mot_consulta_urg = ? " +
													   " AND institucion = ? 
	         */
	        
	        ps.setDouble(1,Utilidades.convertirADouble(codigoMotivo+""));
	        ps.setInt(2,institucion);
	        ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
	        return (!rs.next());
	    }
	    catch(SQLException e)
	    {
	    	logger.warn(e+"Error en estaSiendoUtilizada [SqlBaseSistemasMotivoConsultaDao] "+e.toString() );
	        return false;
	    }finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSistemasMotivoConsultaDao "+sqlException.toString() );
			}
		}
		
	}
	
}

