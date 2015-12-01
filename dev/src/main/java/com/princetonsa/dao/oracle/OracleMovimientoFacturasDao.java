/*
 * @(#)OracleMovimientoFacturasDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import util.InfoDatosInt;
import util.Rangos;

import com.princetonsa.dao.MovimientoFacturasDao;
import com.princetonsa.dao.sqlbase.SqlBaseMovimientoFacturasDao;

/**
 * Implementación Oracle de las funciones de acceso a la fuente de datos
 * para el movimiento de facturas
 *
 * @version 1.0, Abril 12 / 2005
 * @author wrios
 */
public class OracleMovimientoFacturasDao implements MovimientoFacturasDao
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
