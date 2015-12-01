package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dto.interfaz.DtoInterfazConsumosXFacturar;

/**
 * 
 * @author wilson
 *
 */
public interface ConsumosXFacturarDao 
{
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public ArrayList<DtoInterfazConsumosXFacturar> obtenerConsumosXFacturar(Connection con, int institucion);
	
	
	public ArrayList<DtoInterfazConsumosXFacturar> obtenerConsumosXFacturarReproceso(Connection con, int institucion);
}
