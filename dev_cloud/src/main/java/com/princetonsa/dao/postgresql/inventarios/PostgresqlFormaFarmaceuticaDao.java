package com.princetonsa.dao.postgresql.inventarios;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.util.HashMap;

import com.princetonsa.dao.inventarios.FormaFarmaceuticaDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseFormaFarmaceuticaDao;


public class PostgresqlFormaFarmaceuticaDao implements FormaFarmaceuticaDao 
{
/////
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public ResultSetDecorator cargarInformacion(Connection con, int institucion)
	{ 
		return SqlBaseFormaFarmaceuticaDao.cargarInformacion(con, institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @param institucion
	 * @return
	 */
	public boolean eliminarRegistro(Connection con, String consecutivo,int institucion)
	{
		return SqlBaseFormaFarmaceuticaDao.eliminarRegistro(con,consecutivo,institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @param codigo
	 * @param descripcion
	 * @param color
	 * @param destino
	 * @return
	 */
	public boolean existeModificacion(Connection con, String consecutivo, int institucion, String descripcion) 
	{
		return SqlBaseFormaFarmaceuticaDao.existeModificacion(con,consecutivo,institucion,descripcion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public ResultSetDecorator cargarFormaFarmaceutica(Connection con, String codigo,int institucion)
	{
		return SqlBaseFormaFarmaceuticaDao.cargarFormaFarmaceutica(con,codigo,institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param descripcion
	 * @param institucion
	 * @return
	 */
	
	public boolean modificarRegistro(Connection con, String codigo,  String descripcion, int institucion)
	{
		return SqlBaseFormaFarmaceuticaDao.modificarRegistro(con,codigo,descripcion,institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @param descripcion
	 * @param color
	 * @param destino
	 * @return
	 */
	public  boolean insertarRegistro(Connection con, String codigo, int institucion, String descripcion)
	{ 
		
		return SqlBaseFormaFarmaceuticaDao.insertarRegistro(con,codigo,institucion,descripcion);
	}
	
	/**
	 * 
	 * @param con
	 * @param formaFarma
	 * @param institucion
	 * @param viasAdmin
	 */
	public boolean actualizarRelacionesFormasFarmaViasAdmin(Connection con, String formaFarma, int institucion,HashMap viasAdmin)
	{
		return SqlBaseFormaFarmaceuticaDao.actualizarRelacionesFormasFarmaViasAdmin(con, formaFarma,institucion, viasAdmin);
	}


	
	/**
	 * 
	 * @param con
	 * @param formaFarma
	 * @param institucion
	 * @param viasAdmin
	 * @return
	 */
	public boolean insertarRelacionesFormaFarmaViasAdmin(Connection con, String formaFarma,int institucion, HashMap viasAdmin)
	{
		return SqlBaseFormaFarmaceuticaDao.insertarRelacionesFormaFarmaViasAdmin(con, formaFarma,institucion, viasAdmin);
	}
	

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public HashMap cargarRelacionesViasAdminFormaFarma(Connection con, String codigo, int institucion)
	{
		return SqlBaseFormaFarmaceuticaDao.cargarRelacionesViasAdminFormaFarma(con, codigo,institucion);
	}

	
	

}
