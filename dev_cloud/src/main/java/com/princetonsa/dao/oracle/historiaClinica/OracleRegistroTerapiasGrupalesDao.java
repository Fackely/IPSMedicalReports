/*
 * Oct 22, 2007
 * Proyect axioma_oct0707
 * Paquete com.princetonsa.dao.oracle.historiaClinica
 * @author Jorge Armando Osorio Velasquez
 * Compilador Java 1.5.0_07-b03
 */
package com.princetonsa.dao.oracle.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.historiaClinica.RegistroTerapiasGrupalesDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseRegistroTerapiasGrupalesDao;

/**
 * @author Jorge Armando Osorio Velasquez
 *
 */
public class OracleRegistroTerapiasGrupalesDao implements RegistroTerapiasGrupalesDao 
{


	/**
	 * 
	 */
	public HashMap<String, Object> getpacientesTerapiasGrupales(Connection con, String fecha,int centroAtencion,int codigoSexo) 
	{
		return SqlBaseRegistroTerapiasGrupalesDao.getpacientesTerapiasGrupales(con,fecha,centroAtencion,codigoSexo);
	}
	

	

	/**
	 * 
	 */
	public boolean insertarCuentasTerapia(Connection con, int codigoTerapia, HashMap<String, Object> vo) 
	{
		return SqlBaseRegistroTerapiasGrupalesDao.insertarCuentasTerapia(con,codigoTerapia,vo);
	}
	
	
	public HashMap<String, Object> getpacientesTerapiasGrupalesCentro(Connection con, String fecha, int centroAtencion, int codigoSexo, String centroCosto) 
	{
		return SqlBaseRegistroTerapiasGrupalesDao.getpacientesTerapiasGrupalesCentro(con,fecha,centroAtencion,codigoSexo,centroCosto);
	}


}
