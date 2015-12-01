package com.princetonsa.dao.oracle.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.facturacion.ActualizacionAutomaticaEsquemaTarifarioDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseActualizacionAutomaticaEsquemaTarifarioDao;

public class OracleActualizacionAutomaticaEsquemaTarifarioDao implements
		ActualizacionAutomaticaEsquemaTarifarioDao 
		
{
	
	
	/**
	 * 
	 */
	public boolean insertarInventario(Connection con, HashMap vo) 
	{
		return SqlBaseActualizacionAutomaticaEsquemaTarifarioDao.insertarInventario(con, vo);
	}
	
	/**
	 * 
	 */
	public boolean insertarServicio(Connection con, HashMap vo) 
	{
		return SqlBaseActualizacionAutomaticaEsquemaTarifarioDao.insertarServicio(con, vo);
	}
	
	/**
	 * 
	 */
	public HashMap<String, Object> consultaInventarios(Connection con) 
	{
		return SqlBaseActualizacionAutomaticaEsquemaTarifarioDao.consultaInventarios(con);
	}
	
	/**
	 * 
	 */
	public HashMap<String, Object> consultaServicios(Connection con) 
	{
		return SqlBaseActualizacionAutomaticaEsquemaTarifarioDao.consultaServicios(con);
	}

	/**
	 * 
	 */
	public HashMap<String, Object> consultaConveniosVigentes(Connection con, String convenio, String contrato, String empresa, String tipoConvenio, String esquemaServicios, String esquemaInventarios) 
	{
		return SqlBaseActualizacionAutomaticaEsquemaTarifarioDao.consultaConveniosVigentes(con, convenio, contrato, empresa, tipoConvenio, esquemaServicios, esquemaInventarios);
	}
	
	
}
