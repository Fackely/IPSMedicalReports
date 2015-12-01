package com.princetonsa.dao.oracle.administracion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.administracion.FactorConversionMonedasDao;
import com.princetonsa.dao.sqlbase.administracion.SqlBaseFactorConversionMonedasDao;
import com.princetonsa.dto.administracion.DtoFactorConversionMonedas;

/**
 * 
 * @author wilson
 *
 */
public class OracleFactorConversionMonedasDao implements FactorConversionMonedasDao 
{
	/**
	 * Consulta  
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public HashMap cargar(Connection con, DtoFactorConversionMonedas dtoFactor)
	{
		return SqlBaseFactorConversionMonedasDao.cargar(con, dtoFactor);
	}
	
	/**
	 * 
	 * @param con
	 * @param string
	 * @return
	 */
	public boolean eliminarRegistro(Connection con, int codigo)
	{
		return SqlBaseFactorConversionMonedasDao.eliminarRegistro(con, codigo);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 */
	public boolean insertar(Connection con, DtoFactorConversionMonedas dtoFactor)
	{
		return SqlBaseFactorConversionMonedasDao.insertar(con, dtoFactor,DaoFactory.ORACLE);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 */
	public boolean modificar(Connection con, DtoFactorConversionMonedas dtoFactor)
	{
		return SqlBaseFactorConversionMonedasDao.modificar(con, dtoFactor, DaoFactory.ORACLE);
	}
}
