/*
 * @(#)CentrosCostoDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 10 /May/ 2006
 */
public interface CentrosCostoDao 
{
	/**
	 * Método para consultar los centros de costo asociados a un centro de atencion y una
	 * institucion en especifico
	 * @param con
	 * @param centroatencion
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarCentrosCosto(Connection con , int centroatencion, int institucion) throws SQLException;
	
	/**
	 * Método para modificar un centro de costo dado su codigo
	 * @param con
	 * @param identificador
	 * @param descripcion
	 * @param codigoTipoArea
	 * @param manejoCamas
	 * @param unidadFuncional
	 * @param activo
	 * @param codigo
	 * @param tipoEntidad 
	 * @return
	 */
	public int modificarCentroCosto(Connection con, String identificador, String descripcion, int codigoTipoArea,String reg_resp, boolean manejoCamas, int unidadFuncional, String codigo_interfaz, boolean activo, int codigo, String tipoEntidad) throws SQLException;
	
	/**
	 * Método para la insercion de un nuevo Centro de Costo con todos sus atributos
	 * @param con
	 * @param codigocentrocosto
	 * @param nombre
	 * @param codigoTipoArea
	 * @param institucion
	 * @param activo
	 * @param identificador
	 * @param manejoCamas
	 * @param unidadFuncional
	 * @param centroAtencion
	 * @param tipoEntidad 
	 * @param insertarCentroCostoStr -> Postgres - Oracle
	 * @return
	 */
	public int insertarCentrosCosto(Connection con, int codigoCentroCosto, String nombre, int codigoTipoArea, String reg_resp, int institucion, boolean activo, String identificador, boolean manejoCamas, String unidadFuncional, String codigo_interfaz, int centroAtencion, String tipoEntidad) throws SQLException;
	
	/**
	 * Método para eliminar un centro de costo dado su codigo
	 * @param con
	 * @param centroCosto
	 * @return
	 */
	public int eliminarCentroCosto(Connection con, int centroCosto) throws SQLException;
	
	/**
	 * 
	 * @param con
	 * @param almacen
	 * @return
	 * @throws SQLException
	 */
	public int eliminarAlmacen(Connection con, int almacen) throws SQLException;

	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param centroAtencion
	 * @return
	 */
	public HashMap consultarCentrosGrupoServicios(Connection con, int centroAtencion);
		
	
}