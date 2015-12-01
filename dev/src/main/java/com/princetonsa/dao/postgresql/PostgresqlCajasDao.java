/*
 * Created on 12/09/2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;

import com.princetonsa.dao.CajasDao;
import com.princetonsa.dao.sqlbase.SqlBaseCajasDao;

/**
 * @author artotor
 *
 * @todo To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PostgresqlCajasDao implements CajasDao
{
	/**
	 * Metodo que realiza la consula de la tabla cajas y retorna un 
	 * ResultSetDecorator con la informacion
	 * @param con
	 * @param institucion
	 * @param centroAtencion
	 * @return ResultSet
	 */
	public ResultSetDecorator cargarInformacion(Connection con,int institucion, int centroAtencion)
	{
		return SqlBaseCajasDao.cargarInformacion(con,institucion, centroAtencion);
	}

	/**
	 * @param con
	 * @param consecutivoCaja
	 * @return
	 */
	public boolean eliminarRegistro(Connection con, int consecutivoCaja)
	{
		return SqlBaseCajasDao.eliminarRegistro(con,consecutivoCaja);
	}
	
	
	/**
	 * @param con
	 * @param consecutivo
	 * @param codigo
	 * @param descripcion
	 * @param tipo
	 * @param activo
	 * @return
	 */
	public boolean modificarRegistro(Connection con, int consecutivo, int codigo, String descripcion, int tipo, boolean activo, Double valorBase)
	{
		return SqlBaseCajasDao.modificarRegistro(con,consecutivo,codigo,descripcion,tipo,activo, valorBase);
	}
	
	/**
	 * @param con
	 * @param codigo
	 * @param descripcion
	 * @param tipo
	 * @param activo
	 * @param centroAtencion
	 * @param institicuion
	 * @return
	 */
	public boolean insertarRegistro(Connection con, int codigo, int codigoInstitucionInt, String descripcion, int tipo, boolean activo, int centroAtencion, Double valorBase)
	{
		String cadena="insert into cajas (consecutivo,codigo,institucion,descripcion,tipo,activo, centro_atencion, valor_base) values (nextval('seq_cajas'),?,?,?,?,?,?,?)";
		return SqlBaseCajasDao.insertarRegistro(con,codigo,codigoInstitucionInt,descripcion,tipo,activo,centroAtencion,cadena, valorBase);
	}
	
	/**
	 * @param con
	 * @param consecutivo
	 * @param codigo
	 * @param descripcion
	 * @param tipo
	 * @param activo
	 * @return
	 */
	public boolean existeModificacion(Connection con, int consecutivo, int codigo, String descripcion, int tipo, boolean activo, Double valorBase)
	{
		return SqlBaseCajasDao.existeModificacion(con,consecutivo,codigo,descripcion,tipo,activo, valorBase);
	}

	/**
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public ResultSetDecorator cargarCaja(Connection con, int consecutivo)
	{
		return SqlBaseCajasDao.cargarCaja(con,consecutivo);
	}
}
