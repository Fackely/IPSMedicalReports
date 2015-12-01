/**
 * 
 */
package com.sies.dao.sqlbase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.sies.mundo.UtilidadSiEs;

/**
 * @author Juan David Ramírez López
 * Creado el 18/03/2008
 */
public class SqlBaseLogsSiesDao
{
	private static Logger logger=Logger.getLogger(SqlBaseLogsSiesDao.class);
	
	/**
	 * Generación de logs para eliminación de cuadro de turnos
	 * @param con Conexión con la BD
	 * @param usuario Usuario que elimió el cuado de turnos
	 * @param observacion Observación de la eliminación
	 */
	public static void eliminarCuadroTurnos(Connection con, String usuario, String observacion)
	{
		int codigo=UtilidadSiEs.obtenerSiguienteValorSecuencia(con, "seq_log_eliminar");
		String sentencia="INSERT INTO log_eliminar(codigo, usuario, fecha, hora, observacion) VALUES(?, ?, CURRENT_DATE, to_char(CURRENT_TIMESTAMP, 'HH24:MI'), ?)";
		try
		{
			PreparedStatement stm=con.prepareStatement(sentencia);
			stm.setInt(1, codigo);
			stm.setString(2, usuario);
			stm.setString(3, observacion);
			stm.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Error ingresando log de eliminación de cuadro de turnos "+e);
			e.printStackTrace();
		}
	}
}
