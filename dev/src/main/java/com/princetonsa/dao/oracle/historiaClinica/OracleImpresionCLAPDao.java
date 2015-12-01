package com.princetonsa.dao.oracle.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.historiaClinica.ImpresionCLAPDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseImpresionCLAPDao;

public class OracleImpresionCLAPDao implements ImpresionCLAPDao
{

	/**
	 * 
	 */
	public HashMap consultarSolicitudes(Connection con, HashMap vo)
	{
		return SqlBaseImpresionCLAPDao.consultarSolicitudes(con,vo);
	}

	/**
	 * 
	 */
	public boolean generarRegistroLogImpresion(Connection con, HashMap vo)
	{
		return SqlBaseImpresionCLAPDao.generarRegistroLogImpresion(con,vo);
	}


	/**
	 * 
	 */
	public HashMap consultarInformacionRecienNacido(Connection con, String codigoCirugia,String codigoPaciente)
	{
		return SqlBaseImpresionCLAPDao.consultarInformacionRecienNacido(con,codigoCirugia,codigoPaciente);
	}

	
//	-------------------------------------- MANIZALES ---------------------------------------------------//
	
	/**
	 * Metodo que consulta la información obstetrica del paciente
	 * @param con -> Conexion
	 * @param parametros -> Mapa que contiene los parametros de la consulta
	 */
	public HashMap consultarInformacionObstetrica(Connection con, HashMap parametros)
	{
		return SqlBaseImpresionCLAPDao.consultarInformacionObstetrica(con, parametros);
	}

}
