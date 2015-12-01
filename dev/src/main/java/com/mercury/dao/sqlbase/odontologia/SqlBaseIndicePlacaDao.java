package com.mercury.dao.sqlbase.odontologia;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;

import com.mercury.util.UtilidadBaseDatos;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

public class SqlBaseIndicePlacaDao
{
	/**
	 * Inserta un diagrama de &iacute;ndice de placa
	 * @param con
	 * @param codTratamientoOdo
	 * @param observaciones
	 * @param codMedico
	 * @param fecha
	 * @param numeroSuperficies
	 * @param numeroDientes
	 * @return
	 * @throws SQLException
	 */
	public static int insertar(
			Connection con,
			String codTratamientoOdo,
			String observaciones,
			int codMedico, 
			String fecha,
			int numeroSuperficies,
			int numeroDientes) throws SQLException
	{
		 String insertarIndicePlacaStr=
				"insert into indice_placa (" +
				"codigo, " +
				"cod_tratamiento_odo, " +
				"observaciones, " +
				"cod_medico, " +
				"fecha, " +
				"numero_dientes) " +
				"values (?, ?, ?, ?, ?, ?)";
		try
		{
			int codigo = util.UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_indice_placa");
			PreparedStatementDecorator insertarStatement= new PreparedStatementDecorator(con, insertarIndicePlacaStr);
			insertarStatement.setInt(1, codigo);
			UtilidadBaseDatos.establecerParametro(2, Types.CHAR, codTratamientoOdo, insertarStatement);
			UtilidadBaseDatos.establecerParametro(3, Types.VARCHAR, observaciones, insertarStatement);
			insertarStatement.setInt(4, codMedico);
			UtilidadBaseDatos.establecerParametro(5, Types.DATE, fecha, insertarStatement);
			insertarStatement.setInt(6, numeroDientes);
			insertarStatement.executeUpdate();
			insertarStatement.close();
			
			if(numeroSuperficies>0)
			{
				String sentenciaNumeroSuperficies="INSERT INTO historiaclinica.num_superficies_indice_placa (cod_indice_placa, numero_superficies) VALUES(?, ?)";
				insertarStatement=new PreparedStatementDecorator(con, sentenciaNumeroSuperficies);
				insertarStatement.setInt(1, codigo);
				insertarStatement.setInt(2, numeroSuperficies);
				insertarStatement.executeUpdate();
				insertarStatement.close();
			}
			return codigo;
		}
		catch(SQLException e)
		{
			Log4JManager.error("Error insertando índice placa", e);
			throw e;
		}

	}
	
	public static Collection<HashMap<String, Object>> consultarIndicesPlacaTratamiento(Connection con, int codTratamiento) throws SQLException
	{
		String consultarIndicesPlacaTratamientoStr=
			"select " +
			"codigo as codigo, " +
			"observaciones as observaciones, " +
			"cod_medico as cod_medico, " +
			"to_char(fecha, 'DD/MM/YYYY') as fecha, " +
			"numero_dientes AS numero_dientes " +
			"from indice_placa where " +
			"cod_tratamiento_odo = ?";
		try
		{
			PreparedStatementDecorator consultarStatement= new PreparedStatementDecorator(con, consultarIndicesPlacaTratamientoStr);
			consultarStatement.setInt(1, codTratamiento);
			ResultSetDecorator rs=new ResultSetDecorator(consultarStatement.executeQuery());
			Collection<HashMap<String, Object>> coleccion=UtilidadBD.resultSet2Collection(rs);
			rs.close();
			consultarStatement.close();
			return coleccion;
		}
		catch(SQLException e)
		{
			Log4JManager.error("Error consultando índices Placa Tratamiento", e);
			throw e;
		}
	}

	/**
	 * Consulta la informaci&oacute;n de un diagrama de &iacute;ndice de placa
	 * @param con
	 * @param codigo
	 * @return
	 * @throws SQLException
	 */
	public static Collection<HashMap<String, Object>> consultar(Connection con, int codigo) throws SQLException
	{
		String consultarIndicePlacaStr=
			"select "+
			"codigo as codigo, " +
			"cod_tratamiento_odo as cod_tratamiento_odo, " +
			"observaciones as observaciones, " +
			"cod_medico as cod_medico, " +
			"to_char(fecha, 'DD/MM/YYYY') as fecha, " +
			"numero_dientes AS numero_dientes " +
			"from indice_placa where " +
			"codigo = ?";
		try
		{
			PreparedStatementDecorator consultarStatement= new PreparedStatementDecorator(con, consultarIndicePlacaStr);
			consultarStatement.setInt(1, codigo);
			ResultSetDecorator rs=new ResultSetDecorator(consultarStatement.executeQuery());
			Collection<HashMap<String, Object>> coleccion=UtilidadBD.resultSet2Collection(rs);
			rs.close();
			consultarStatement.close();
			return coleccion;
		}
		catch(SQLException e)
		{
			Log4JManager.error("Error consultando el índice de placa", e);
			throw e;
		}
	}

	/**
	 * Modifica las observaciones de un diagrama de &iacute;ndice de placa
	 * @param con
	 * @param codigo
	 * @param observaciones
	 * @return
	 * @throws SQLException
	 */
	public static int modificar(
			Connection con, 
			int codigo, 
			String observaciones) throws SQLException
	{
		String modificarIndicePlacaStr=
			"update indice_placa " +
			"set observaciones=? where codigo=?";
		try
		{
			PreparedStatementDecorator modificarStatement= new PreparedStatementDecorator(con, modificarIndicePlacaStr);			
			UtilidadBaseDatos.establecerParametro(1, Types.VARCHAR, observaciones, modificarStatement);
			modificarStatement.setInt(2, codigo);
			int resultado=modificarStatement.executeUpdate();
			return resultado;
		}
		catch(SQLException e)
		{
			Log4JManager.error("error modificando índice de placa", e);
			throw e;
		}
	}

	/**
	 * Inserta información de placa en un sector de un diente para un diagrama de indice de placa especifico.
	 * @param con
	 * @param codIndicePlaca
	 * @param numeroDiente
	 * @param numeroSector
	 * @return
	 * @throws SQLException
	 */
	public static int insertarSectorDientePlaca(Connection con, int codIndicePlaca, int numeroDiente, int numeroSector) throws SQLException
	{
		String insertarSectorDientePlacaStr=
			"INSERT INTO placa_sector_diente (" +
				"cod_indice_placa, " +
				"numero_diente, " +
				"numero_sector) " +
				"values (?,?,?)";
		try
		{
			PreparedStatementDecorator insertarStatement= new PreparedStatementDecorator(con, insertarSectorDientePlacaStr);
			insertarStatement.setInt(1, codIndicePlaca);
			insertarStatement.setInt(2, numeroDiente);
			insertarStatement.setInt(3, numeroSector);
			int resultado=insertarStatement.executeUpdate();
			insertarStatement.close();
			return resultado;
		}
		catch(SQLException e)
		{
			Log4JManager.error("Error insertando sector indice placa", e);
			throw e;
		}
	}
	
	/**
	 * Consulta los sectores que tienen placa en los dientes de un diagrama de &iacute;ndice de placa especificado.
	 * @param con
	 * @param codIndicePlaca
	 * @return
	 * @throws SQLException
	 */
	public static Collection<HashMap<String, Object>> consultarSectoresDientesPlaca(Connection con, int codIndicePlaca) throws SQLException
	{
		String consultarSectoresDientesPlacaStr=
			"SELECT " +
			"cod_indice_placa as cod_indice_placa, " +
			"numero_diente as numero_diente, " +
			"numero_sector as numero_sector " +
			"from placa_sector_diente where " +
			"cod_indice_placa=?";
		try
		{
			PreparedStatementDecorator consultarStatement= new PreparedStatementDecorator(con, consultarSectoresDientesPlacaStr);
			consultarStatement.setInt(1, codIndicePlaca);
			ResultSetDecorator rs=new ResultSetDecorator(consultarStatement.executeQuery());
			Collection<HashMap<String, Object>> coleccion=UtilidadBD.resultSet2Collection(rs);
			rs.close();
			consultarStatement.close();
			return coleccion;
		}
		catch(SQLException e)
		{
			Log4JManager.error("Error consultando sector indice placa", e);
			throw e;
		}
	}
	
	/**
	 * Obtener el n&uacute;mero de superficies del &iacute;ndice de placa
	 * @param con Conexi&oacute;n con la BD
	 * @param codigoIndicePlaca C&oacute;sdigo del &iacute;ndice de placa relacionado
	 * @return entero con el n&uacute;mero de superficies
	 */
	public static int consultarNumeroSuperficies(Connection con, int codigoIndicePlaca)
	{
		String sentencia="SELECT numero_superficies AS numero_superficies FROM historiaclinica.num_superficies_indice_placa WHERE cod_indice_placa=?";
		PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentencia);
		int numeroSuperficies=ConstantesBD.codigoNuncaValido;
		try
		{
			psd.setInt(1, codigoIndicePlaca);
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			if(rsd.next())
			{
				numeroSuperficies=rsd.getInt("numero_superficies");
			}
			rsd.close();
			psd.close();
		} catch (SQLException e)
		{
			Log4JManager.error("Error consultando el número de superficies del índice de placa", e);
		}
		return numeroSuperficies;
	}
}
