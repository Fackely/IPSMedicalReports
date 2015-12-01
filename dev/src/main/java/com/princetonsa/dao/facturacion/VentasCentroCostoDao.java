package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.HashMap;

public interface VentasCentroCostoDao 
{

	
	/**
	 * 
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param centroCosto
	 * @return
	 */
	String cambiarConsulta(Connection con, String fechaInicial, String fechaFinal, String centroCosto);
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	HashMap consultarVentasCentroCosto(Connection con, HashMap vo);

	
	
}
