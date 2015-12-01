package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.HashMap;

public interface ImpresionSoportesFacturasDao{
	
	/**
	 * Método para consultar las facturas
	 * @param con
	 * @param listado
	 * @param codigoInstitucionInt
	 * @param loginUsuario
	 * @return
	 */
	public HashMap listarImprimir(Connection con,HashMap<String, Object> listado, int codigoInstitucionInt, String loginUsuario);
	
}