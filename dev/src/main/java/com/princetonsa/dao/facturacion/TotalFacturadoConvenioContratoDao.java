package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.HashMap;

public interface TotalFacturadoConvenioContratoDao 
{

	
	/**
	 * 
	 * @param con
	 * @param excluirFacturas
	 * @param convenio
	 * @param contrato
	 * @return
	 */
	String cambiarConsulta(Connection con, String excluirFacturas, String convenio, String contrato, String periodo);
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	HashMap consultarTotalFacturado(Connection con, HashMap vo);
	

}
