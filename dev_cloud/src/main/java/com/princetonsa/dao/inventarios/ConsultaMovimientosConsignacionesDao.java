package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.HashMap;

public interface ConsultaMovimientosConsignacionesDao 
{

	
	/**
	 * 
	 * @param con
	 * @param centroAtencion
	 * @param almacen
	 * @param proveedor
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param tipoCodigo
	 * @return
	 */
	HashMap consultarMovimientosConsignaciones(Connection con, String centroAtencion, String almacen, String proveedor, String fechaInicial, String fechaFinal, String tipoCodigo);
	
	
	/**
	 * 
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param almacen
	 * @param proveedor
	 * @param centroAtencion 
	 * @return
	 */
	String cambiarConsulta(Connection con, String fechaInicial, String fechaFinal, String almacen, String proveedor, String centroAtencion);

	
	
}
