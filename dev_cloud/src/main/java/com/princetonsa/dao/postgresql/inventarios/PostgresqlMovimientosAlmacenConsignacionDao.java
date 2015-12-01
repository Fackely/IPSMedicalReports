package com.princetonsa.dao.postgresql.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.actionform.inventarios.MovimientosAlmacenConsignacionForm;
import com.princetonsa.dao.inventarios.MovimientosAlmacenConsignacionDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseMovimientosAlmacenConsignacionDao;
import com.princetonsa.mundo.inventarios.MovimientosAlmacenConsignacion;

public class PostgresqlMovimientosAlmacenConsignacionDao implements MovimientosAlmacenConsignacionDao 
{
	/**
	 * 
	 */
	public HashMap consultarTransacciones(Connection con, int codInstitucion) 
	{
		return SqlBaseMovimientosAlmacenConsignacionDao.consultarTransacciones(con, codInstitucion);
	}
	
	/**
	 * 
	 */
	public HashMap consultarProveedores(Connection con, String proveedor) 
	{
		return SqlBaseMovimientosAlmacenConsignacionDao.consultarProveedores(con, proveedor);
	}

	/**
	 * 
	 */
    public HashMap consultarMovimientos(Connection con, MovimientosAlmacenConsignacionForm forma, MovimientosAlmacenConsignacion mundo, HashMap criterios)
    {
    	return SqlBaseMovimientosAlmacenConsignacionDao.consultarMovimientos(con, forma, mundo, criterios);
    }
    




    /**
     * 
     */
	public String obtenerConsulta(Connection con,
			MovimientosAlmacenConsignacionForm forma, MovimientosAlmacenConsignacion mundo, String codigoAImprimir, int bandera) {
		return SqlBaseMovimientosAlmacenConsignacionDao.obtenerConsulta(con, forma, mundo, codigoAImprimir, bandera);
	}


	@Override
	public void insertarLog(Connection con, HashMap criterios) {
		
		
	}

}
