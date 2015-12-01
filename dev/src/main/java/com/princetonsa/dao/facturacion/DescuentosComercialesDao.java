/*
 * Creado May 22, 2007
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * DescuentosComercialesDao
 * com.princetonsa.dao.facturacion
 * java version "1.5.0_07"
 */
package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dto.facturacion.DtoConvenio;
import com.princetonsa.dto.facturacion.DtoProgDescComercialConvenioContrato;

/**
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * May 22, 2007
 */
public interface DescuentosComercialesDao
{

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param contrato
	 * @param anteriores 
	 * @param vigentes 
	 * @param anteriores2 
	 * @param vigentes2 
	 * @return
	 */
	public abstract HashMap obtenerListadoViasDescuento(Connection con, int institucion, int contrato, boolean vigentes, boolean todas);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public abstract boolean eliminarViaDescuento(Connection con, String codigo);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public abstract HashMap consultarViaDescuentoLLave(Connection con, String codigo);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean modificarViaDescuento(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean insertarViaDescuento(Connection con, HashMap vo);
	
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
	 * CARGAR 
	 * @param dto
	 * @return
	 */
	public ArrayList<DtoProgDescComercialConvenioContrato> cargarProgDescComercialContrato(DtoProgDescComercialConvenioContrato dto);
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public boolean  modificarProgDescComercialConvenioContrato(Connection con, DtoProgDescComercialConvenioContrato dto);
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public boolean insertProgDescComercialConvenioContrato(Connection con, DtoProgDescComercialConvenioContrato dto);

	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public  boolean eliminarProgDescComercialContrato(Connection con, DtoProgDescComercialConvenioContrato dto);
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public  int  validarTipoAtencionOdontologica(DtoConvenio dto );

}
