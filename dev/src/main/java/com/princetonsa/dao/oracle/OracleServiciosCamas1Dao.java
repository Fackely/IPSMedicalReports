/*
 * @(#)OracleServiciosCamas1Dao.java
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

import com.princetonsa.dao.ServiciosCamas1Dao;
import com.princetonsa.dao.sqlbase.SqlBaseServiciosCamas1Dao;

/**
 * Implementación Oracle de las funciones de acceso a la fuente de datos
 * para un servicioscamas1
 *
 * @version 1.0, Mayo 31 / 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class OracleServiciosCamas1Dao implements ServiciosCamas1Dao
{

    /**
	 * Inserta un servicio de cama
	 */
	private final static String ingresarServicioCamaStr=	"INSERT INTO servicios_cama " +
																					"( codigo, " +
																					"codigo_cama, " +
																					"servicio, " +
																					"tipo_monitoreo ) " +
																					"VALUES (seq_servicios_cama.nextval, ?, ?, ?)";
    
	 /**
	 * Detalle de los servicios de una cama filtrado por la institucion, numeroCama, habitacion
	 * @param con
	 * @param codigoCama
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator detalleServiciosCama(Connection con, int codigoCama, boolean esUci)
	{
	    return SqlBaseServiciosCamas1Dao.detalleServiciosCama(con, codigoCama, esUci);
	}
	
	/**
	 * Metodo que modifica un servicio de cama dado el codigo (axioma) del servicio de cama
	 * @param con
	 * @param codigoServicioNuevo
	 * @param tipoMonitoreo
	 * @param codigoSequenciaTablaServicioCama
	 * @return
	 */
	public boolean modificarServiciosCama(	Connection con, 
																	int codigoServicioNuevo, 
																	int tipoMonitoreo,
																	int codigoSequenciaTablaServicioCama)
	{
	    return SqlBaseServiciosCamas1Dao.modificarServiciosCama(con, codigoServicioNuevo, tipoMonitoreo, codigoSequenciaTablaServicioCama);
	}
	
	/**
	 * Metodo que elimina un servicio de cama dado el codigo (axioma) de la tabla servicios cama
	 * @param con
	 * @param codigoTablaServiciosCama
	 * @return
	 */
	public boolean eliminarServiciosCama(		Connection con, 
																	int codigoTablaServiciosCama)
	{
	    return SqlBaseServiciosCamas1Dao.eliminarServiciosCama(con, codigoTablaServiciosCama);
	}
	
	/**
	 * Inserta un servicio cama 
	 * @param con
	 * @param codigoCama
	 * @param codigoServicio
	 * @param codigoTipoMonitoreo
	 * @return
	 */
	public boolean  insertarServicioCama(	Connection con,
																int codigoCama,
																int codigoServicio,
																int codigoTipoMonitoreo)
	{
	    return SqlBaseServiciosCamas1Dao.insertarServicioCama(con, codigoCama, codigoServicio, codigoTipoMonitoreo, ingresarServicioCamaStr);
	}
}
