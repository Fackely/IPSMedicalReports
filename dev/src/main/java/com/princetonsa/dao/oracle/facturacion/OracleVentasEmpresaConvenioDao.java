package com.princetonsa.dao.oracle.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.facturacion.VentasEmpresaConvenioDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseVentasEmpresaConvenioDao;
import com.princetonsa.util.birt.reports.DesignEngineApi;

public class OracleVentasEmpresaConvenioDao implements VentasEmpresaConvenioDao 
{

	
	/**
	 * 
	 */
	public String cambiarConsulta(Connection con, DesignEngineApi comp, String fechaInicial, String fechaFinal, String empresa, String convenio) 
	{
		return SqlBaseVentasEmpresaConvenioDao.cambiarConsulta(con, comp, fechaInicial, fechaFinal, empresa, convenio);
	}

	/**
	 * 
	 */
	public HashMap consultarVentasCentroCosto(Connection con, HashMap vo) 
	{
		return SqlBaseVentasEmpresaConvenioDao.consultarVentasCentroCosto(con, vo);
	}

	
}
