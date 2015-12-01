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
 * @author Juan David Ram�rez L�pez
 * Creado el 18/03/2008
 */
public class SqlBaseLogsSiesDao
{
	private static Logger logger=Logger.getLogger(SqlBaseLogsSiesDao.class);
	
	/**
	 * Generaci�n de logs para eliminaci�n de cuadro de turnos
	 * @param con Conexi�n con la BD
	 * @param usuario Usuario que elimi� el cuado de turnos
	 * @param observacion Observaci�n de la eliminaci�n
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
			logger.error("Error ingresando log de eliminaci�n de cuadro de turnos "+e);
			e.printStackTrace();
		}
	}
}
