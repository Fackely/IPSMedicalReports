/*
 * Created on 19/12/2005
 * 
 * @author <a href="mailto:artotor@hotmail.com">Jorge Armando Osorio Velásquez</a>
 * 
 * Copyright Princeton S.A. &copy;&reg; 2005. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 *
 */
package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @version 1.0, 19/12/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public interface CierresInventarioDao
{

	/**
	 * Metodo que retorna un mapa con el detalle de los movimientos de inventario
	 * @param con
	 * @param institucion
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	public abstract HashMap cargarMovimientosInventarios(Connection con, int institucion, String fechaInicial, String fechaFinal);

	/**
	 * Metodo que genera el cierre dado un mapa con el encabezado del cierre
	 * y un mapa con el detalle de los movimientos.
	 * @param con
	 * @param encabezadoCirre
	 * @param movimientos
	 * @return
	 */
	public abstract boolean generarCierre(Connection con, HashMap encabezadoCirre, HashMap movimientos);

	/**
	 * 
	 * @param con
	 * @param codigoCierre
	 * @return
	 */
	public abstract int eliminarCierreInventarios(Connection con, String codigoCierre);


}
