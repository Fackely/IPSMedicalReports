package com.princetonsa.dao.oracle.pyp;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.pyp.ActivPyPXFacturarDao;
import com.princetonsa.dao.sqlbase.pyp.SqlBaseActivPyPXFacturarDao;

/**
 * 
 * @author wrios
 *
 */
public class OracleActivPyPXFacturarDao implements ActivPyPXFacturarDao
{
	/**
	 * 
	 * @param con
	 * @param codigoConvenio
	 * @param codigoCentroAtencion
	 * @param codigoInstitucion
	 * @return
	 */
	public HashMap listado(	Connection con,
									int codigoConvenio,
									int codigoCentroAtencion,
	        						int codigoInstitucion
								 )
	{
		return SqlBaseActivPyPXFacturarDao.listado(con, codigoConvenio, codigoCentroAtencion, codigoInstitucion);
	}

}
