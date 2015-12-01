/*
 * Creado el Jun 1, 2006
 * por Julian Montoya
 */
package com.princetonsa.dao.sqlbase.historiaClinica;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;


public class SqlBaseSignosSintomasDao {

	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseSignosSintomasDao.class);

	
	/**
	 * Metodo para cargar la informacion de los signos y sintomas.
	 * @param con
	 * @param institucion 
	 * @return
	 */

	 
	public static HashMap cargarSignosSintomas(Connection con, int institucion)
	{
		//@todo  se debe hacer el join de las tablas nuevas que dependan de la tabla  
		//-------historiaclinica.signos_sintomas se deben adicionar a este join.
	

		String consulta = "	SELECT hss.consecutivo as consecutivo, hss.codigo as codigo, hss.descripcion as signo,    " +
						  "		   hss.codigo as h_codigo, hss.descripcion as h_signo, 								  " +
						  " 	   CASE WHEN hsxs.consecutivo IS NULL THEN 'true' ELSE 'false' END as puedeEliminar   " +
						  " 	   FROM historiaclinica.signos_sintomas	hss  										  " + 
						  " 	   		LEFT OUTER JOIN 															  " + 
						  "				(SELECT consecutivo_signos_sintomas as consecutivo FROM historiaclinica.signos_sintomas_x_sistema GROUP BY consecutivo_signos_sintomas) " +
				       	  "				hsxs ON ( hss.consecutivo = hsxs.consecutivo )								  " +
						  "			    WHERE hss.institucion = ? 													  " +
						  " 	              ORDER BY hss.codigo ";
		
		PreparedStatementDecorator ps =  null;
		try
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setInt(1, institucion);
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,false);
			ps.close();
			return mapaRetorno;
		}
		catch (SQLException e)
		{
			logger.error("Error en cargarSignosSintomas de SqlBaseSignosSintomasDao: "+e);
			return null;
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSignosSintomasDao "+sqlException.toString() );
			}
		}
	}

	/**
	 * Metodo para insertar datos de los signos y sintomas. 
	 * @param con
	 * @param consecutivo 
	 * @param codigo
	 * @param descripcion
	 * @param codigoInstitucion 
	 * @return
	 */
	public static int insertar(Connection con, int consecutivo, String secuencia, String codigo, String descripcion, int codigoInstitucion)
	{
		String consulta = "";

		if (consecutivo == ConstantesBD.codigoNuncaValido) //- Se inserta
			consulta = "INSERT INTO historiaclinica.signos_sintomas VALUES (" + secuencia + ", ?, ?, ?) ";
		else
			consulta = "UPDATE historiaclinica.signos_sintomas SET codigo = ?, descripcion = ? WHERE consecutivo = ?";
		
		PreparedStatementDecorator stm =  null;
		try
		{	logger.info("\n Consulta sinos_sintomas-------------->"+consulta);
			stm= new PreparedStatementDecorator(con.prepareStatement(consulta));
			
			if (consecutivo == ConstantesBD.codigoNuncaValido) //- Se inserta
			{	
				stm.setDouble(1, Utilidades.convertirADouble(codigo));
				stm.setString(2, descripcion);
				stm.setInt(3, codigoInstitucion);
			}
			else
			{	
				stm.setDouble(1, Utilidades.convertirADouble(codigo));
				stm.setString(2, descripcion);
				stm.setDouble(3, Utilidades.convertirADouble(consecutivo+""));
			}
		
			return stm.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Error ingresando/modificando los los signos y sintomas.  : "+e);
			return ConstantesBD.codigoNuncaValido;
		}finally{
			try {
				if(stm!=null){
					stm.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSignosSintomasDao "+sqlException.toString() );
			}
		}
	}

	/**
	 * Metodo para eliminar datos de los signos y sintomas. 
	 * @param con
	 * @param nroRegEliminar
	 * @return
	 */
	public static int eliminar(Connection con, int nroRegEliminar)
	{
		String consulta = "DELETE FROM historiaclinica.signos_sintomas WHERE consecutivo = ? ";
		PreparedStatementDecorator stm =  null;
		try
		{
			 stm= new PreparedStatementDecorator(con.prepareStatement(consulta));
			stm.setDouble(1, Utilidades.convertirADouble(nroRegEliminar+""));
			return stm.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Error eliminando los los signos y sintomas.  : "+e);
			return ConstantesBD.codigoNuncaValido;
		}finally{
			try {
				if(stm!=null){
					stm.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSignosSintomasDao "+sqlException.toString() );
			}
		}
	}
}
