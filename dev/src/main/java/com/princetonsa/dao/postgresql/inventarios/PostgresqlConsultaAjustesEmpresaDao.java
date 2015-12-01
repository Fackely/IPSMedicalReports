/*
 * 
 */
package com.princetonsa.dao.postgresql.inventarios;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.util.HashMap;

import com.princetonsa.dao.inventarios.ConsultaAjustesEmpresaDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseConsultaAjustesEmpresaDao;

/**
 * 
 * @author axioma
 *
 */
public class PostgresqlConsultaAjustesEmpresaDao implements ConsultaAjustesEmpresaDao
{

	/**
	 * 
	 */
	public ResultSetDecorator busquedaGeneralAjustes(Connection con, HashMap vo)
	{
		return SqlBaseConsultaAjustesEmpresaDao.busquedaGeneralAjustes(con,vo);
	}
	
	

	/**
	 * 
	 */
	public ResultSetDecorator consltarDetalleAjuste(Connection con, double codigoAjuste)
	{
		return SqlBaseConsultaAjustesEmpresaDao.consltarDetalleAjuste(con,codigoAjuste);
	}

}
