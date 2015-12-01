/*
 * Created on 20/09/2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;

import com.princetonsa.dao.EntidadesFinancierasDao;
import com.princetonsa.dao.sqlbase.SqlBaseEntidadesFinancierasDao;

/**
 * @author artotor
 *
 * @todo To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PostgresqlEntidadesFinancierasDao implements EntidadesFinancierasDao 
{
	/**
	 * Metodo que realiza la consula de las Entidades Financeras y retorna un 
	 * ResultSetDecorator con la informacion
	 * @param con
	 * @param institicuion
	 * @return ResultSet
	 */
	public ResultSetDecorator cargarEntidadesFinancieras(Connection con,int institucion)
	{
		return SqlBaseEntidadesFinancierasDao.cargarEntidadesFinancieras(con,institucion);
	}
	

	/**
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public boolean eliminarRegistro(Connection con, int consecutivo)
	{
		return SqlBaseEntidadesFinancierasDao.eliminarRegistro(con,consecutivo);
	}
	
	
	/**
	 * @param con
	 * @param consecutivo
	 * @param codigo
	 * @param codigoTercero
	 * @param tipo
	 * @param activo
	 * @return
	 */
	public boolean existeModificacion(Connection con, int consecutivo, String codigo, int codigoTercero, int tipo, boolean activo)
	{
		return SqlBaseEntidadesFinancierasDao.existeModificacion(con,consecutivo,codigo,codigoTercero,tipo,activo);
	}
	

	/**
	 * @param con
	 * @param consecutivo
	 * @param codigo
	 * @param codigoTercero
	 * @param tipo
	 * @param activo
	 * @return
	 */
	public boolean modificarRegistro(Connection con, int consecutivo, String codigo, int codigoTercero, int tipo, boolean activo)
	{
		return SqlBaseEntidadesFinancierasDao.modificarRegistro(con,consecutivo,codigo,codigoTercero,tipo,activo);
	}
	
	

	/**
	 * @param con
	 * @param codigo
	 * @param codigoInstitucionInt
	 * @param codigoTercero
	 * @param tipo
	 * @param activo
	 * @return
	 */
	public boolean insertarRegistro(Connection con, String codigo, int codigoInstitucionInt, int codigoTercero, int tipo, boolean activo)
	{
		String cadena="insert into entidades_financieras(consecutivo,codigo,institucion,tercero,tipo, activo) values(nextval('seq_entidades_financieras'),?,?,?,?,?)";
		return SqlBaseEntidadesFinancierasDao.insertarRegistro(con,codigo,codigoInstitucionInt,codigoTercero,tipo,activo,cadena);
	}
	
	
	/**
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public ResultSetDecorator cargarEntidadFinanciera(Connection con, int consecutivo)
	{
		return SqlBaseEntidadesFinancierasDao.cargarEntidadFinanciera(con,consecutivo);
	}
}
