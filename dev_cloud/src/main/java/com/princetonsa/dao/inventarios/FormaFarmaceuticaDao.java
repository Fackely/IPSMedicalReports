package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.util.HashMap;


public interface FormaFarmaceuticaDao
{
/////
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public abstract ResultSetDecorator cargarInformacion(Connection con, int institucion);

	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @param institucion
	 * @return
	 */
	public abstract boolean eliminarRegistro(Connection con, String consecutivo, int institucion) ;

	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @param codigo
	 * @param descripcion
	 * @param color
	 * @return
	 */
	public abstract boolean existeModificacion(Connection con, String consecutivo, int institucion, String descripcion) ;

	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @param institucion
	 * @return
	 */
	public abstract ResultSetDecorator cargarFormaFarmaceutica(Connection con, String consecutivo,int institucion);

	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @param descripcion
	 * @param institucion
	 * @return
	 */
	public abstract boolean modificarRegistro(Connection con, String consecutivo,  String descripcion, int institucion);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @param descripcion
	 * @param color
	 * @return
	 */
	public abstract boolean insertarRegistro(Connection con, String codigo, int institucion, String descripcion);

	/**
	 * 
	 * @param con
	 * @param formaFarma
	 * @param institucion
	 * @param viasAdmin
	 */
	public abstract boolean actualizarRelacionesFormasFarmaViasAdmin(Connection con, String formaFarma,int institucion, HashMap viasAdmin);

	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @param viasAdmin
	 * @return
	 */
	public abstract boolean insertarRelacionesFormaFarmaViasAdmin(Connection con, String codigo,int institucion, HashMap viasAdmin);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public abstract HashMap cargarRelacionesViasAdminFormaFarma(Connection con, String codigo,int institucion);
	
/////	

}
