/*
 * @(#)MovimientoFacturasDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;

import util.InfoDatosInt;
import util.Rangos;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 *  Interfaz para el acceder a la fuente de datos del movimiento de facturas
 *
 * @version 1.0, Abril 12 / 2005
 * @author wrios
 */
public interface MovimientoFacturasDao 
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
     * @param empresaInstitucion 
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
															int codigoCentroAtencion, String empresaInstitucion) throws SQLException;

}
