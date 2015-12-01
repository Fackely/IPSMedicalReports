/*
 * 
 */
package com.princetonsa.dao.oracle.inventarios;

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
public class OracleConsultaAjustesEmpresaDao implements ConsultaAjustesEmpresaDao
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
