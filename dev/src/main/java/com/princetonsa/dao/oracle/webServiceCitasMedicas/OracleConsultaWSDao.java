
/**
 * 
 * @author Jorge Armando Osorio Velasquez
 *
 */
package com.princetonsa.dao.oracle.webServiceCitasMedicas;

import java.sql.Connection;

import com.princetonsa.dao.sqlbase.webServiceCitasMedicas.SqlBaseConsultaWSDao;
import com.princetonsa.dao.webServiceCitasMedicas.ConsultaWSDao;

/**
 * 
 * @author Jorge Armando Osorio Velasquez
 *
 */
public class OracleConsultaWSDao implements ConsultaWSDao
{


	/**
	 * 
	 */
	public int obtenerCitasAntendiasPortal(Connection con, String fechaInicial, String fechaFinal)
	{
		return SqlBaseConsultaWSDao.obtenerCitasAntendiasPortal(con,fechaInicial,fechaFinal);
	}

	/**
	 * 
	 */
	public int obtenerCitasSolicitadasPortal(Connection con, String fechaInicial, String fechaFinal)
	{
		return SqlBaseConsultaWSDao.obtenerCitasSolicitadasPortal(con,fechaInicial,fechaFinal);
	}

	/**
	 * 
	 */
	public int obtenerTotalCitas(Connection con, String fechaInicial, String fechaFinal)
	{
		return SqlBaseConsultaWSDao.obtenerTotalCitas(con,fechaInicial,fechaFinal);
	}
}
