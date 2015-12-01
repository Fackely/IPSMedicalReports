package com.princetonsa.dao.postgresql.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.facturacion.TotalFacturadoConvenioContratoDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseTotalFacturadoConvenioContrato;

public class PostgresqlTotalFacturadoConvenioContratoDao implements
		TotalFacturadoConvenioContratoDao 
		
{

	
	/**
	 * 
	 */
	public String cambiarConsulta(Connection con, String excluirFacturas, String convenio, String contrato, String periodo) 
	{
		return SqlBaseTotalFacturadoConvenioContrato.cambiarConsulta(con, excluirFacturas, convenio, contrato, periodo);
	}

	
	/**
	 * 
	 */
	public HashMap consultarTotalFacturado(Connection con, HashMap vo) 
	{
		return SqlBaseTotalFacturadoConvenioContrato.consultarTotalFacturado(con, vo);
	}

	
	
	
}
