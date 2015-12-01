/*
 * @(#)OracleCuposExtraDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import com.princetonsa.dao.CentrosCostoDao;
import com.princetonsa.dao.sqlbase.SqlBaseCentrosCostoDao;

/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 10 /May/ 2006
 */
public class OracleCentrosCostoDao implements CentrosCostoDao 
{
	
	/**
	 * Cadena con el statement necesario para insertar un cetro de costo
	 */
	private final static String insertarCentrosCostoStr = " INSERT INTO centros_costo " +
														  " (codigo, " +
														  " nombre, " +
														  " tipo_area, " +
														  " institucion, " +
														  " es_activo, " +
														  " identificador, " +
														  " manejo_camas, " +
														  " unidad_funcional," +
														  " codigo_interfaz," +
														  " reg_res_porc_ter,"+
														  " centro_atencion , " +
														  " tipo_entidad_ejecuta )" +
														  " VALUES (seq_centros_costo.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?)" ;
	
	
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
	 * @param insertarCentroCostoStr -> Postgres - Oracle
	 * @return
	 */
	public int insertarCentrosCosto(Connection con, int codigoCentroCosto, String nombre, int codigoTipoArea,String reg_resp, int institucion, boolean activo, String identificador, boolean manejoCamas, String unidadFuncional, String codigo_interfaz, int centroAtencion, String tipoEntidad) throws SQLException
	{
		return SqlBaseCentrosCostoDao.insertarCentrosCosto(con, codigoCentroCosto, nombre, codigoTipoArea,reg_resp, institucion, activo, identificador, manejoCamas, unidadFuncional, codigo_interfaz, centroAtencion, insertarCentrosCostoStr,tipoEntidad);
	}
	
	/**
	 * Método para consultar los centros de costo asociados a un centro de atencion y una
	 * institucion en especifico
	 * @param con
	 * @param centroatencion
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarCentrosCosto(Connection con , int centroatencion, int institucion) throws SQLException
	{
		return SqlBaseCentrosCostoDao.consultarCentrosCosto(con, centroatencion, institucion);
	}
	
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
	 * @return
	 */
	public int modificarCentroCosto(Connection con, String identificador, String descripcion, int codigoTipoArea, String reg_resp, boolean manejoCamas, int unidadFuncional, String codigo_interfaz, boolean activo, int codigo, String tipoEntidad) throws SQLException
	{
		return SqlBaseCentrosCostoDao.modificarCentroCosto(con, identificador, descripcion, codigoTipoArea,reg_resp, manejoCamas, unidadFuncional, codigo_interfaz, activo, codigo, tipoEntidad);
	}
	
	/**
	 * Método para eliminar un centro de costo dado su codigo
	 * @param con
	 * @param centroCosto
	 * @return
	 */
	public int eliminarCentroCosto(Connection con, int centroCosto) throws SQLException
	{
		return SqlBaseCentrosCostoDao.eliminarCentroCosto(con, centroCosto);
	}
	
	/**
	 * 
	 */
	public int eliminarAlmacen(Connection con, int almacen) throws SQLException 
	{
		return SqlBaseCentrosCostoDao.eliminarAlmacen(con, almacen);
	}

	
	/**
	 * 
	 */
	public HashMap consultarCentrosGrupoServicios(Connection con,int centroAtencion) 
	{
		return SqlBaseCentrosCostoDao.consultarCentrosGrupoServicios(con, centroAtencion);
	}
	
}