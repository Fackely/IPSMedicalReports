/*
 * Created on May 5, 2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;

import org.apache.log4j.Logger;

import com.princetonsa.dao.ValidacionesCierreCuentaDao;
import com.princetonsa.dao.sqlbase.SqlBaseValidacionesCierreCuentaDao;

/**
 * @author sebastián gómez
 *
 * @todo To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PostgresqlValidacionesCierreCuentaDao implements ValidacionesCierreCuentaDao{
	/**
	 * Manejador de logs de la clase
	 */
	private Logger logger=Logger.getLogger(PostgresqlValidacionesCierreCuentaDao.class);

	/* (non-Javadoc)
	 * @see com.princetonsa.dao.ValidacionesCierreCuentaDao#validarEstadoCuenta(java.sql.Connection, int)
	 */
	public boolean validarEstadoCuenta(Connection con, int idCuenta) {
		
		return SqlBaseValidacionesCierreCuentaDao.validarEstadoCuenta(con,idCuenta);
	}

	/* (non-Javadoc)
	 * @see com.princetonsa.dao.ValidacionesCierreCuentaDao#validarCargosSeviciosXCuenta(java.sql.Connection, int)
	 */
	public boolean validarCargosSeviciosXCuenta(Connection con, int idCuenta) {
		
		return SqlBaseValidacionesCierreCuentaDao.validarCargosSeviciosXCuenta(con,idCuenta);
	}

	/* (non-Javadoc)
	 * @see com.princetonsa.dao.ValidacionesCierreCuentaDao#validarCargosMedicamentosXCuenta(java.sql.Connection, int)
	 */
	public boolean validarCargosMedicamentosXCuenta(Connection con, int idCuenta) {
		return SqlBaseValidacionesCierreCuentaDao.validarCargosMedicamentosXCuenta(con,idCuenta);
	}

	/* (non-Javadoc)
	 * @see com.princetonsa.dao.ValidacionesCierreCuentaDao#validarEstadosFactSolicitudes(java.sql.Connection, int)
	 */
	public boolean validarEstadosFactSolicitudes(Connection con, int idCuenta) {
		
		return SqlBaseValidacionesCierreCuentaDao.validarEstadosFactSolicitudes(con,idCuenta);
	}


	/* (non-Javadoc)
	 * @see com.princetonsa.dao.ValidacionesCierreCuentaDao#validarSolicitudesConsultaExterna(java.sql.Connection, int)
	 */
	public boolean validarSolicitudesConsultaExterna(Connection con, int idCuenta) {
		
		return SqlBaseValidacionesCierreCuentaDao.validarSolicitudesConsultaExterna(con,idCuenta);
	}

	/* (non-Javadoc)
	 * @see com.princetonsa.dao.ValidacionesCierreCuentaDao#validarEstadoFactSolicitudValoracion(java.sql.Connection, int)
	 */
	public boolean validarEstadoFactSolicitudValoracion(Connection con, int idCuenta) {
		
		return SqlBaseValidacionesCierreCuentaDao.validarEstadoFactSolicitudValoracion(con,idCuenta);
	}


}
