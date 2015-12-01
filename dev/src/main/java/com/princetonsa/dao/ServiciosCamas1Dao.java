/*
 * @(#)Camas1Dao.java
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

import com.princetonsa.decorator.ResultSetDecorator;

/**
 *  Interfaz para el acceder a la fuente de datos de servicioscamas1
 *
 * @version 1.0, Mayo 31 / 2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public interface ServiciosCamas1Dao 
{
    /**
	 * Detalle de los servicios de una cama filtrado por la institucion, numeroCama, habitacion
	 * @param con
	 * @param codigoCama
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator detalleServiciosCama(Connection con, int codigoCama, boolean esUci);

	
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
																	int codigoSequenciaTablaServicioCama);
	
	/**
	 * Metodo que elimina un servicio de cama dado el codigo (axioma) de la tabla servicios cama
	 * @param con
	 * @param codigoTablaServiciosCama
	 * @return
	 */
	public boolean eliminarServiciosCama(		Connection con, 
																	int codigoTablaServiciosCama);
	
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
																int codigoTipoMonitoreo);
	
}
