package com.princetonsa.dao.postgresql.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.ParamArchivoPlanoColsanitasDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseParamArchivoPlanoColsanitasDao;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public class PostgresqlParamArchivoPlanoColsanitasDao implements
		ParamArchivoPlanoColsanitasDao {

	public HashMap consultarArchivoPlanoColsanitas(Connection con, int convenio) {
		return SqlBaseParamArchivoPlanoColsanitasDao.consultarArchivoPlanoColsanitas(con, convenio);
	}

	public boolean eliminarArchivoPlanoColsanitas(Connection con, int convenio) {
		return SqlBaseParamArchivoPlanoColsanitasDao.eliminarArchivoPlanoColsanitas(con, convenio);
	}

	public boolean insertarArchivoPlanoColsanitas(Connection con,
			HashMap<String, Object> vo) {
		return SqlBaseParamArchivoPlanoColsanitasDao.insertarArchivoPlanoColsanitas(con, vo, DaoFactory.POSTGRESQL);
	}

	public boolean modificarArchivoPlanoColsanitas(Connection con, HashMap vo) {
		return SqlBaseParamArchivoPlanoColsanitasDao.modificarArchivoPlanoColsanitas(con, vo);
	}

}
