package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.sql.SQLException;

import util.ResultadoBoolean;

import com.princetonsa.dao.AntecedenteMedicamentoDao;
import com.princetonsa.dao.sqlbase.SqlBaseAntecedenteMedicamentoDao;

/**
 * Implementación de la interfaz para el acceso a la base de datos del
 * antecedente a medicamento para postgresql.
 *
 * @version 1.0, Agosto 26, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class PostgresqlAntecedenteMedicamentoDao implements AntecedenteMedicamentoDao
{
	public ResultadoBoolean insertar(	Connection con,
																int codigoPaciente,
																int codigo,
																String nombre,
																String dosis,
																String frecuencia,
																String fechaInicio,
																String fechaFin,
																String observaciones, String cantidad, String dosisD, String tiempoT,
																String tipofrecuencia, String unidosis)
	{
		return SqlBaseAntecedenteMedicamentoDao.insertar(con, codigoPaciente, codigo, nombre,	 dosis, frecuencia, fechaInicio, fechaFin, observaciones,cantidad,dosisD,tiempoT, tipofrecuencia, unidosis);
	}

	public ResultadoBoolean insertarTransaccional(	Connection con,
																		int codigoPaciente,
																		int codigo,
																		String codigoA,
																		String nombre,
																		String dosis,
																		String frecuencia,
																		String fechaInicio,
																		String fechaFin,
																		String observaciones,
																		String estado, String cantidad, String dosisD, String tiempoT,
																		String tipofrecuencia, String unidosis) throws SQLException
	{
	    return SqlBaseAntecedenteMedicamentoDao.insertarTransaccional(	con,
				codigoPaciente,
				codigo,
				codigoA,
				nombre,
				dosis,
				frecuencia,
				fechaInicio,
				fechaFin,
				observaciones,
				estado,cantidad,dosisD,tiempoT, tipofrecuencia, unidosis);
	}

	public ResultadoBoolean modificar(	Connection con,
																int codigoPaciente,
																int codigo,
																String dosis,
																String frecuencia,
																String fechaInicio,
																String fechaFin,
																String observaciones, String cantidad, String dosisD, String tiempoT, 
																String tipofrecuencia, String unidosis)
	{		
				return SqlBaseAntecedenteMedicamentoDao.modificar(con, codigoPaciente, codigo, dosis, frecuencia, fechaInicio, fechaFin, observaciones,cantidad,dosisD,tiempoT, tipofrecuencia, unidosis);
	}

	public ResultadoBoolean modificarTransaccional(	Connection con,
																						int codigoPaciente,
																						int codigo,
																						String dosis,
																						String frecuencia,
																						String fechaInicio,
																						String fechaFin,
																						String observaciones,
																						String estado, String cantidad, String dosisD, String tiempoT,
																						String tipofrecuencia, String unidosis) throws SQLException
	{
	    return SqlBaseAntecedenteMedicamentoDao.modificarTransaccional(	con,
				codigoPaciente,
				codigo,
				dosis,
				frecuencia,
				fechaInicio,
				fechaFin,
				observaciones,
				estado,cantidad,dosisD,tiempoT, tipofrecuencia, unidosis);
	}

	public ResultadoBoolean existeAntecedente(	Connection con, 
																				int codigoPaciente, 
																				int codigo)
	{
		return SqlBaseAntecedenteMedicamentoDao.existeAntecedente(con,codigoPaciente,codigo);
	}

}
