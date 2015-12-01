/*
 * Creado May 17, 2007
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * CoberturasConvenioDao
 * com.princetonsa.dao.facturacion
 * java version "1.5.0_07"
 */
package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dto.facturacion.DtoProExeCobConvXCont;


/**
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * May 17, 2007
 */
public interface CoberturasConvenioDao
{

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param contrato
	 * @return
	 */
	public abstract HashMap obtenerListadoCoberturas(Connection con, int institucion, int contrato);

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param contrato
	 * @return
	 */
	public abstract HashMap obtenerListadoExcepciones(Connection con, int institucion, int contrato);

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
	 * @param contrato
	 * @param institucion
	 * @return
	 */
	public abstract boolean eliminarCobertura(Connection con, String codigo, int contrato, int institucion);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean insertarCobertura(Connection con, HashMap vo);

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
	 * @param codigoCobertura
	 * @param contrato
	 * @param institucion
	 * @return
	 */
	public abstract HashMap consultarCoberturaLLave(Connection con, String codigoCobertura, int contrato, int institucion);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean modificarCobertura(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @return
	 */
	public abstract ArrayList obtenerConvenios(Connection con);
	
	/**
	 * 
	 */
	public boolean insertarProgExcepcionConvenio(Connection con, DtoProExeCobConvXCont dto);
	
	/**
	 * 
	 */
	public ArrayList<DtoProExeCobConvXCont> consultaProgExcepcionConvenio(Connection con,DtoProExeCobConvXCont dto);
	
	/**
	 * 
	 */
	public boolean eliminarProgExcepcionConvenio(Connection con, double excepcion);
}
