package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.util.birt.reports.DesignEngineApi;

public interface VentasEmpresaConvenioDao 
{

	
	/**
	 * 
	 * @param con
	 * @param comp
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param empresa
	 * @param convenio
	 * @return
	 */
	String cambiarConsulta(Connection con, DesignEngineApi comp, String fechaInicial, String fechaFinal, String empresa, String convenio);
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	HashMap consultarVentasCentroCosto(Connection con, HashMap vo);

	
	
}
