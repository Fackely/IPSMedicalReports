package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.HashMap;

public interface ActualizacionAutomaticaEsquemaTarifarioDao 
{

	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	boolean insertarInventario(Connection con, HashMap vo);
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	boolean insertarServicio(Connection con, HashMap vo);
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	HashMap<String, Object> consultaInventarios(Connection con);
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	HashMap<String, Object> consultaServicios(Connection con);

	/**
	 * 
	 * @param con
	 * @param convenio
	 * @param contrato
	 * @param empresa
	 * @param tipoConvenio
	 * @param esquemaServicios
	 * @param esquemaInventarios
	 * @return
	 */
	HashMap<String, Object> consultaConveniosVigentes(Connection con, String convenio, String contrato, String empresa, String tipoConvenio, String esquemaServicios, String esquemaInventarios);
	

}
