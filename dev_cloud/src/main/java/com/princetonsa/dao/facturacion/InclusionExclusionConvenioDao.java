/*
 * Creado May 23, 2007
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * InclusionExclusionConvenioDao
 * com.princetonsa.dao.facturacion
 * java version "1.5.0_07"
 */
package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

import util.InfoDatosString;

/**
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * May 23, 2007
 */
public interface InclusionExclusionConvenioDao
{

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public abstract Vector<InfoDatosString> obtenerDetalleInclusionesExclusiones(Connection con, int institucion);

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param contrato 
	 * @return
	 */
	public abstract HashMap obtenerInclusionesExclusiones(Connection con, int institucion, int contrato);

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public abstract HashMap obtenerExcepciones(Connection con, int institucion, int contrato,boolean vigentes,boolean todas);
	
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
	 * @param codigoIncluExclu
	 * @param contrato
	 * @param institucion
	 * @return
	 */
	public abstract boolean eliminarInclusionExclusion(Connection con, String codigoIncluExclu, int contrato, int institucion);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean insertarInclusionExclusion(Connection con, HashMap vo);

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
	public abstract boolean insertarAgrupacionArticulos(Connection con, HashMap vo);

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
	public abstract boolean insertarArticulos(Connection con, HashMap vo);

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
	public abstract boolean insertarAgrupacionServicios(Connection con, HashMap vo);

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
	public abstract boolean insertarServicios(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param codigoIncluExclu
	 * @param contrato
	 * @param institucion
	 * @return
	 */
	public abstract HashMap consultarIncluExcluLLave(Connection con, String codigoIncluExclu, int contrato, int institucion);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean modificarIncluExclu(Connection con, HashMap vo);
	
}
