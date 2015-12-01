package com.princetonsa.dao.oracle;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ResultadoBoolean;

import com.princetonsa.dao.ExcepcionesNaturalezaPacienteDao;
import com.princetonsa.dao.sqlbase.SqlBaseExcepcionesNaturalezaPacienteDao;

public class OracleExcepcionesNaturalezaPacienteDao implements
		ExcepcionesNaturalezaPacienteDao {

	private Logger logger=Logger.getLogger(OracleExcepcionesNaturalezaPacienteDao.class);
	
	public int insertar(Connection con, String codigoRegimen, int codigoNaturaleza, boolean indicativoExcepcion){
		String insertQuery = SqlBaseExcepcionesNaturalezaPacienteDao.insertQuery;
		PreparedStatementDecorator insert = null;
		int resultado = 0;

		try{
			insertQuery += " (seq_excepciones_naturaleza.nextval, ?, ?, ?) ";
			con = SqlBaseExcepcionesNaturalezaPacienteDao.refreshDBConnection(con);
			insert =  new PreparedStatementDecorator(con.prepareStatement(insertQuery,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			insert.setString(1, codigoRegimen);
			insert.setInt(2, codigoNaturaleza);
			insert.setBoolean(3, indicativoExcepcion);
			resultado = insert.executeUpdate();
		}
		catch(SQLException e){
			logger.error("Error insertando el registro en la base de datos [Clase: OracleExcepcionesNaturalezaPacienteDao | Tabla: excepciones_naturaleza ]: ");
			logger.error("insertQuery:  " +  insert.toString());
			logger.error(e);
			resultado = 0;
		}
		return resultado;
	}
	

	public int modificar(Connection con, int codigo, String codigoRegimen, int codigoNaturaleza, boolean indicativoExcepcion){
		return SqlBaseExcepcionesNaturalezaPacienteDao.modificar(con, codigo, codigoRegimen, codigoNaturaleza, indicativoExcepcion);
	}
	
	public int eliminar(Connection con, int codigo){
		return SqlBaseExcepcionesNaturalezaPacienteDao.eliminar(con, codigo);
	}
	
	public HashMap buscar(Connection con, String[] selectedColumns, String where, String orderBy){
		return SqlBaseExcepcionesNaturalezaPacienteDao.buscar(con, selectedColumns, where, orderBy); 
	}

	public HashMap buscar(Connection con, int codigo){
		return SqlBaseExcepcionesNaturalezaPacienteDao.buscar(con, codigo); 
	}

	/*
	 * *************************************** OJO *********************************************
	 * @author Juan David Ramírez López
	 *
	 * Princeton S.A.
	 * 
	 * Este método es utilizado por facturación, favor consultar para su modificación
	 * Atte: Juan David
	 */
	/**
	 * Método para buscar la excepcion de farmacia de acuerdo
	 * al tipo de regimen y la naturaleza del paciente
	 * @param con Conexión con la BD
	 * @param tipoRegimen Tipo del regimen del paciente
	 * @param naturalezaPaciente Naturaleza del paciente
	 * @return true si tiene excepción, false de lo contrario, false con descripción
	 * (Codificada ApplicationReources) en caso de error
	 */
	public ResultadoBoolean buscarExcepcionPorNaturaleza(Connection con, String tipoRegimen, int naturalezaPaciente)
	{
		return SqlBaseExcepcionesNaturalezaPacienteDao.buscarExcepcionPorNaturaleza(con, tipoRegimen, naturalezaPaciente);
	}

}
