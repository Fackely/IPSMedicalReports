/**
 * 
 */
package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author axioma
 *
 */
public interface ExcepcionesTarifas1Dao
{
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param todas 
	 * @param vigentes 
	 * @return
	 */
	public abstract HashMap obtenerExcepciones(Connection con, int institucion, int contrato, boolean vigentes, boolean todas);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public abstract boolean eliminareExepciones(Connection con, String codigo);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public abstract HashMap consultarExcepcionLLave(Connection con, String codigo);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean modificarExcepcion(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean insertarExcepcion(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion 
	 * @return
	 */
	public abstract HashMap consultarAgrupacionArticulos(Connection con, String codigo);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public abstract HashMap consultarArticulos(Connection con, String codigo);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public abstract HashMap consultarAgrupacionServicios(Connection con, String codigo);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public abstract HashMap consultarServicios(Connection con, String codigo);
	
	/**
	 * 
	 */
	public abstract HashMap consultarAgrupacionArticulosLLave(Connection con, String codigo);
	
	/**
	 * 
	 */
	public abstract HashMap consultarArticulosLLave(Connection con, String codigo);
	
	/**
	 * 
	 */
	public abstract HashMap consultarAgrupacionServiciosLLave(Connection con, String codigo);	
	/**
	 * 
	 */
	public abstract HashMap consultarServiciosLLave(Connection con, String codigo);
	
	/**
	 * 
	 * @param con
	 * @param codigoAgrupacion
	 * @return
	 */
	public abstract boolean eliminarAgrupacionArticulos(Connection con, String codigoAgrupacion);


	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean modificarAgrupacionArticulos(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract int insertarAgrupacionArticulos(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param codigoComponente
	 * @return
	 */
	public abstract boolean eliminarArticulos(Connection con, String codigoComponente);


	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean modificarArticulos(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract int insertarArticulos(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param codigoAgrupacion
	 * @return
	 */
	public abstract boolean eliminarAgrupacionServicios(Connection con, String codigoAgrupacion);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean modificarAgrupacionServicios(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract int insertarAgrupacionServicios(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param codigoComponente
	 * @return
	 */
	public abstract boolean eliminarServicios(Connection con, String codigoComponente);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean modificarServicios(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract int insertarServicios(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param codigosPKSeccion
	 * @param codigoSeccion
	 * @return
	 */
	public abstract HashMap<String, Object> consultarPorcentaje(Connection con, String codigosPKSeccion, int codigoSeccion);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @param codigoSeccion
	 */
	public abstract boolean modificarPorcentaje(Connection con, HashMap vo, int codigoSeccion);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @param codigoSeccion
	 */
	public abstract boolean insertarPorcentaje(Connection con, HashMap vo, int codigoSeccion);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public abstract boolean eliminarPorcentajes(Connection con, String codigo,int codigoSeccion);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param codigoSeccion
	 * @return
	 */
	public abstract HashMap consultarPorcentajeLLave(Connection con, int codigo, int codigoSeccion, int posRegistroExcepcion);
	
	
}
