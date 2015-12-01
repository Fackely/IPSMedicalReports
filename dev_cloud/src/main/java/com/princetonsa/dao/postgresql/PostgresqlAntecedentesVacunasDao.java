package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.AntecedentesVacunasDao;
import com.princetonsa.dao.sqlbase.SqlBaseAntecedentesVacunasDao;

/**
 * PostgresqlAntecedentesVacunasDao
 */
@SuppressWarnings("rawtypes")
public class PostgresqlAntecedentesVacunasDao implements AntecedentesVacunasDao {

	
	/**
	 * Funcion para cosultar informacion 
	 * @param con
	 * @param mapaParam
	 * @return
	 */
	public HashMap consultarInformacion(Connection con, HashMap mapaParam)
	{
		return SqlBaseAntecedentesVacunasDao.consultarInformacion(con, mapaParam);
	}
	
	/**
	 * Método para insertar/modificar los antecedentes vacunas e insertar si es necesari
	 * en antecedentes pacientes
	 * @param con
	 * @param codigoPaciente
	 * @param observaciones
	 * @param esInsertar
	 * @return
	 */
	public int insertarModificarAntecedenteVacuna(Connection con, int codigoPaciente, String observaciones, boolean esInsertar)
	{
		return SqlBaseAntecedentesVacunasDao.insertarModificarAntecedenteVacuna(con, codigoPaciente, observaciones, esInsertar);
	}
	
	/**
	 * Método que inserta la aclaración de la dosis o el refuerzo de la vacuna, ya que se insertan
	 * en la misma tabla, sino que el refuerzo se inserta -1 en numero_dosis
	 * @param con
	 * @param codigoPaciente
	 * @param codigoTipoInmunizacion 
	 * @param dosis (Si es igual -1 es refuerzo sino es una aclaración de dosis)
	 * @param texto (Puede ser la aclaración o el refuerzo)
	 * @param loginUsuario
	 * @param datosMedico
	 * @return
	 */
	public int insertarDosisRefuerzo(Connection con, int codigoPaciente, int codigoTipoInmunizacion, int dosis, String texto, String loginUsuario, String datosMedico)
	{
		return SqlBaseAntecedentesVacunasDao.insertarDosisRefuerzo (con, codigoPaciente, codigoTipoInmunizacion, dosis, texto, loginUsuario, datosMedico);
	}
	
	/**
	 * Método que inserta o modifica el comentario agragado a la vacuna
	 * @param con
	 * @param codigoPaciente
	 * @param codigoTipoInmunizacion
	 * @param comentarioVacuna
	 * @param loginUsuario
	 * @param datosMedico
	 * @return
	 */
	public int insertarModificarComentarioVacuna(Connection con, int codigoPaciente, int codigoTipoInmunizacion, String comentarioVacuna, String loginUsuario, String datosMedico)
	{
		return SqlBaseAntecedentesVacunasDao.insertarModificarComentarioVacuna(con, codigoPaciente, codigoTipoInmunizacion,comentarioVacuna, loginUsuario, datosMedico);
	}

}
