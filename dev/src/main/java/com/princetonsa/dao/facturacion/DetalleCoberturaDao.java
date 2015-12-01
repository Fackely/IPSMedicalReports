/*
 * Creado May 11, 2007
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * DetalleCoberturaDao
 * com.princetonsa.dao.facturacion
 * java version "1.5.0_07"
 */
package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import com.princetonsa.dto.facturacion.DtoCoberturaProgramas;
import com.princetonsa.dto.facturacion.DtoCoberturaServicios;

import util.InfoDatosString;

/**
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * May 11, 2007
 */
public interface DetalleCoberturaDao
{

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public abstract HashMap obtenerListadoCoberturas(Connection con, int institucion);

	/**
	 * 
	 * @param con
	 * @param codigoDetCobertura
	 * @return
	 */
	public abstract boolean eliminarDetalleCobertura(Connection con, String codigoDetCobertura);

	/**
	 * 
	 * @param con
	 * @param codigoDetallecobertura
	 * @return
	 */
	public abstract HashMap consultarDetalleCoberturaLLave(Connection con, String codigoDetallecobertura);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean modificarDetalleCobertura(Connection con, HashMap vo);

	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean insertarDetalleCobertura(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public abstract Vector<InfoDatosString> obtenerListadoCoberturasInstitucion(Connection con, int institucion);

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
	 * @param con
	 * @param codigoAgrupacion
	 * @return
	 */
	public abstract boolean eliminarAgrupacionArticulos(Connection con, String codigoAgrupacion);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public abstract HashMap consultarAgrupacionArticulosLLave(Connection con, String codigo);

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
	 * @param codigo
	 * @return
	 */
	public abstract HashMap consultarArticulosLLave(Connection con, String codigo);

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
	 * @param codigo
	 * @return
	 */
	public abstract HashMap consultarAgrupacionServiciosLLave(Connection con, String codigo);

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
	 * @param codigo
	 * @return
	 */
	public abstract HashMap consultarServiciosLLave(Connection con, String codigo);

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
	 */
	public abstract boolean insertarCoberturaProgramas(Connection con, DtoCoberturaProgramas dto);
	
	/**
	 * 
	 */
	public abstract ArrayList<DtoCoberturaProgramas> consultarCoberturaProgramas(Connection con,DtoCoberturaProgramas dto);
	
	/**
	 * 
	 */
	public abstract boolean eliminarCoberturaProgramas(Connection con, double codigoCoberturaAEliminar);
	
	/**
	 * 
	 */
	public abstract ArrayList<DtoCoberturaServicios> consultarCoberturaServicios(Connection con, DtoCoberturaServicios dto);
	
	/**
	 * 
	 */
	public abstract boolean insertarCoberturaServicios(Connection con, DtoCoberturaServicios dto);
	
	/**
	 * 
	 */
	public abstract boolean eliminarCoberturaServicios(Connection con,double codigoCoberturaAEliminar);
}
