/*
 * @(#)PostgresqlMovimientoFacturasDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import com.princetonsa.dao.MovimientoFacturasDao;
import com.princetonsa.dao.sqlbase.SqlBaseMovimientoFacturasDao;

import util.InfoDatosInt;
import util.Rangos;

/**
 * Implementaci�n postgresql de las funciones de acceso a la fuente de datos
 * para el movimiento de facturas
 *
 * @version 1.0, Abril 12 / 2005
 * @author wrios
 */
public class PostgresqlMovimientoFacturasDao implements MovimientoFacturasDao
{
    /**
	 * busqueda avanzada de movimiento facturas
	 * @param con
	 * @param empresa
	 * @param convenio
	 * @param facturaRangos
	 * @param fechaRangos
	 * @param valorFacturaRangos
	 * @param numeroCuentaCobroRangos
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator busquedaAvanzada(	Connection con,
															InfoDatosInt empresa,
															InfoDatosInt convenio,
															Rangos facturaRangos,
															Rangos fechaRangos,
															Rangos valorFacturaRangos,
															Rangos numeroCuentaCobroRangos,
															int codigoInstitucion,
															int codigoCentroAtencion, String empresaInstitucion) throws SQLException
	{
	    return SqlBaseMovimientoFacturasDao.busquedaAvanzada(con, empresa, convenio, facturaRangos, fechaRangos, valorFacturaRangos, numeroCuentaCobroRangos,codigoInstitucion, codigoCentroAtencion,empresaInstitucion);
	}
}
