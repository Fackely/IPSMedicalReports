package com.princetonsa.dao.oracle;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import util.ResultadoBoolean;

import com.princetonsa.dao.AntecedentesTransfusionalesDao;
import com.princetonsa.dao.sqlbase.SqlBaseAntecedentesTransfusionalesDao;

/**
 * Definición de la interfaz para el acceso a la base de datos del módulo de
 * antecedentes transfusionales
 *
 * @version 1.0, Septiembre 2, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class OracleAntecedentesTransfusionalesDao implements AntecedentesTransfusionalesDao
{
	public ResultadoBoolean existenAntecedentes(Connection con, int codigoPaciente)
	{
		return SqlBaseAntecedentesTransfusionalesDao.existenAntecedentes(con, codigoPaciente);
	}

	public ResultadoBoolean insertarAntecedenteGeneral(Connection con, int codigoPaciente)
	{
		return SqlBaseAntecedentesTransfusionalesDao.insertarAntecedenteGeneral(con, codigoPaciente);
	}

	public ResultadoBoolean existenAntecedentesTransfusionales( Connection con, int codigoPaciente)
	{
		return SqlBaseAntecedentesTransfusionalesDao.existenAntecedentesTransfusionales(con, codigoPaciente);
	}


	public ResultSetDecorator consultarAntecedentesTransfusionales( Connection con, int codigoPaciente)
	{
		return SqlBaseAntecedentesTransfusionalesDao.consultarAntecedentesTransfusionales(con, codigoPaciente);
	}

	public ResultadoBoolean insertar(Connection con, int codigoPaciente, String observaciones)
	{
	    return SqlBaseAntecedentesTransfusionalesDao.insertar(con, codigoPaciente, observaciones);
	}

	public ResultadoBoolean insertarTransaccional(Connection con, int codigoPaciente, String observaciones, String estado) throws SQLException
	{
	    return SqlBaseAntecedentesTransfusionalesDao.insertarTransaccional(con, codigoPaciente, observaciones, estado); 
	}

	public ResultadoBoolean modificar(Connection con, int codigoPaciente, String observaciones)
	{
		return SqlBaseAntecedentesTransfusionalesDao.modificar(con, codigoPaciente, observaciones);
	}

	public ResultadoBoolean modificarTransaccional(Connection con,	int codigoPaciente, String observaciones, String estado)throws SQLException
	{
	    return modificarTransaccional(con,	codigoPaciente, observaciones, estado);
	}

	public ResultSetDecorator consultarTransfusiones( Connection con, int codigoPaciente)
	{
		return SqlBaseAntecedentesTransfusionalesDao.consultarTransfusiones(con, codigoPaciente);
	}

}
