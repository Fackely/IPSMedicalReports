package com.sies.dao.sqlbase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;
import com.sies.mundo.SiEsSecuencias;
import com.sies.mundo.UtilidadSiEs;

import util.UtilidadBD;

public class SqlBaseIngresarTurnoDao
{
	/**
	 * Manejador de logs de la Clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseIngresarTurnoDao.class);
	
	/**
	 * Consultar los tipos de turnos
	 */
	private static String consultarTiposStr="SELECT tt.acronimo AS acronimo, tt.nombre AS nombre FROM tipos_turno tt WHERE tt.por_defecto=false";

	/**
	 * Consultar el listado de todos los turnos
	 */
	private static String consultarTurnosStr="SELECT t.codigoturno AS codigo, t.descripcion AS descripcion, to_char(t.horadesde, 'HH24:MI') AS hora_inicio, t.numero_horas AS numero_horas, t.simbolo AS simbolo, t.tipo_turno AS tipo_turno, tt.nombre AS nombre_tipo_turno, t.color_letra AS color_letra, t.color_fondo AS color_fondo, t.es_festivo AS es_festivo, tt.por_defecto AS por_defecto, tcc.centro_costo AS centro_costo FROM turno t INNER JOIN tipos_turno tt ON(t.tipo_turno=tt.acronimo) LEFT OUTER JOIN turno_centro_costo tcc ON(tcc.codigo_turno=t.codigoTurno) WHERE t.activo=true ORDER BY t.simbolo";
	/**
	 * Sentencia para el ingreso de los turnos
	 */
	private static String ingresarTurnoStr="INSERT INTO turno(codigoturno, descripcion, horadesde, numero_horas, simbolo, tipo_turno, color_letra, color_fondo, es_festivo, activo) VALUES(?,?,?,?,?,?,?,?,?,?)";

	/**
	 * Sentencia para ingresar los centros de costo del turno
	 */
	private static String ingresarCentroCostoTurnoStr="INSERT INTO turno_centro_costo(codigo_turno, centro_costo) VALUES(?, ?)";

	/**
	 * Sentencia para consultar el centro de costo de un turno
	 */
	private static String consultarCentrosCostoTurnoStr="SELECT centro_costo FROM turno_centro_costo WHERE codigo_turno=?";

	/**
	 * Sentencia para eliminar los centros de costo relacionados a un turno
	 */
	private static String eliminarCentrosCostoTurnoStr="DELETE FROM turno_centro_costo WHERE codigo_turno=?";
	
	/**
	 * Sentencia para modificar un turno
	 */
	private static String modificarTurnoStr="UPDATE turno SET descripcion=?, horadesde=?, numero_horas=?, simbolo=?, tipo_turno=?, color_letra=?, color_fondo=?, es_festivo=? WHERE codigoturno=?";
	
	/**
	 * Sentencia para inactivar los turnos
	 */
	private static final String inavtivarTurnoStr="UPDATE turno SET activo=? WHERE codigoturno=?";
	
	/**
	 * Método que consulta los tipos de turno existentes
	 * @param con
	 * @param tipoConsulta
	 * @return Colección con los datos
	 */
	public static Collection<HashMap<String, Object>> consultarTipos(Connection con, int tipoConsulta)
	{
		String consulta;
		try
		{
			switch(tipoConsulta)
			{
				case 1:
					consulta=consultarTiposStr;
				break;
				case 2:
					consulta=consultarTurnosStr;
				break;
				default:
					logger.error("Error consultando tipos (Tipo consulta "+tipoConsulta+" invalido)");
					return null;
			}
			
			PreparedStatement stm=con.prepareStatement(consulta);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(stm.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error consultando tipos "+e);
			return null;
		}
	}

	/**
	 * Método para guardar el nuevo turno
	 * @param con
	 * @param descripcion
	 * @param horaInicio
	 * @param horaFin
	 * @param simbolo
	 * @param tipoTurno
	 * @param colorLetra
	 * @param colorFondo
	 * @param codigo
	 * @param esModificar
	 * @param esFestivo
	 * @param centroCosto
	 * @return true si se guardo bien, false de lo contrario
	 */
	public static int guardarModificar(Connection con, String descripcion, String horaInicio, String horaFin, String simbolo, char tipoTurno, String colorLetra, String colorFondo, int codigo, boolean esModificar, int esFestivo, Integer centroCosto)
	{
		int resultado=-1;
		try
		{
			if(esModificar)
			{
				PreparedStatement stm=con.prepareStatement(modificarTurnoStr);
				stm.setString(1, descripcion);
				stm.setString(2, horaInicio);
				stm.setString(3, horaFin);
				stm.setString(4, simbolo);
				stm.setString(5, tipoTurno+"");
				stm.setString(6, colorLetra);
				stm.setString(7, colorFondo);
				stm.setInt(8, esFestivo);
				stm.setInt(9, codigo);
				resultado=stm.executeUpdate();
				if(resultado<1)
				{
					logger.error("Error modificando el turno "+codigo);
				}
				stm=con.prepareStatement(eliminarCentrosCostoTurnoStr);
				stm.setInt(1, codigo);
				resultado=stm.executeUpdate();
				if(resultado<1)
				{
					logger.error("Error eliminando el centro de costo relacionado al turno "+codigo);
				}
				if(centroCosto!=null)
				{
					stm=con.prepareStatement(ingresarCentroCostoTurnoStr);
					stm.setInt(1, codigo);
					stm.setInt(2, centroCosto);
					resultado=stm.executeUpdate();
					if(resultado<1)
					{
						logger.error("Error ingresando el centro de costo relacionado al turno "+codigo);
					}
				}
				
			}
			else
			{
				int codigoIngreso=UtilidadSiEs.obtenerSiguienteValorSecuencia(con, SiEsSecuencias.NOMBRE_SECUENCIA_TURNOS);
				PreparedStatement stm=con.prepareStatement(ingresarTurnoStr);
				stm.setInt(1, codigoIngreso);
				stm.setString(2, descripcion);
				stm.setString(3, horaInicio);
				stm.setString(4, horaFin);
				stm.setString(5, simbolo);
				stm.setString(6, tipoTurno+"");
				stm.setString(7, colorLetra);
				stm.setString(8, colorFondo);
				stm.setInt(9, esFestivo);
				stm.setBoolean(10, true);
				resultado=stm.executeUpdate();
				if(resultado<1)
				{
					logger.error("Error ingresando el turno");
				}
				if(centroCosto!=null)
				{
					stm=con.prepareStatement(ingresarCentroCostoTurnoStr);
					stm.setInt(1, codigoIngreso);
					stm.setInt(2, centroCosto);
					resultado=stm.executeUpdate();
					if(resultado<1)
					{
						logger.error("Error ingresando el centro de costo relacionado al turno "+codigo);
					}
				}
			}
			return resultado;
		}
		catch (SQLException e)
		{
			logger.error("Error ingresando el turno "+e);
		}
		return -1;
	}

	/**
	 * Método para eliminar un tipo de turno
	 * La eliminación se hace inactivando el turno específico
	 * @param con
	 * @param codigo
	 */
	public static int inactivar(Connection con, int codigo)
	{
		try
		{
			PreparedStatement stm=con.prepareStatement(inavtivarTurnoStr);
			stm.setBoolean(1, false);
			stm.setInt(2, codigo);
			return stm.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Error inactivando los turnos");
			return 0;
		}
	}

}
