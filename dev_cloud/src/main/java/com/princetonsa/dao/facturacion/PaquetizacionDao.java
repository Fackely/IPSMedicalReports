/*
 * Jun 15, 2007
 * Proyect axioma
 * Paquete com.princetonsa.dao.facturacion
 * @author Jorge Armando Osorio Velasquez
 * Compilador Java 1.5.0_07-b03
 */
package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;

/**
 * @author Jorge Armando Osorio Velasquez
 *
 */
public interface PaquetizacionDao 
{

	/**
	 * 
	 * @param con 
	 * @param subCuenta
	 * @return
	 */
	public abstract HashMap consultarPaquetesAsocioadosResponsableSubcuenta(Connection con, String subCuenta);

	/**
	 * 
	 * @param con
	 * @param codigoPaquetizaciones
	 * @return
	 */
	public abstract HashMap<String, Object> consultarServiciosPaquetes(Connection con, String[] codigoPaquetizaciones);

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public abstract ArrayList<DtoSolicitudesSubCuenta> obtenerSolicitudesSubCuentaPaquetizar(Connection con, String subCuenta);

	/**
	 * 
	 * @param con
	 * @param codServicio
	 * @return
	 */
	public abstract HashMap obtenerParametrosServicio(Connection con, String codServicio);

	/**
	 * 
	 * @param con
	 * @param codArticulo
	 * @return
	 */
	public abstract HashMap obtenerParametrosArticulos(Connection con, String codArticulo);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract int insertarEncabezadoPaquetizacion(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param codPaquetizacion
	 * @param numeroSolicitud 
	 * @return
	 */
	public abstract boolean eliminarPaquetizacion(Connection con, String codPaquetizacion, String numeroSolicitud,boolean eliminarEncabezado);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean guardarDetallePaquete(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public abstract HashMap consultarLiquidacionPaquete(Connection con, String subCuenta);

	/**
	 * 
	 * @param con
	 * @param evento
	 * @param numeroSolicitud
	 * @param codServArt
	 * @param codigoDetPadre
	 * @param esServicio
	 * @param facturado 
	 * @return
	 */
	public abstract boolean actualizarCantidadDetCargo(Connection con, int cantidad,String subCuenta, String numeroSolicitud, String codServArt, int codigoDetPadre, boolean esServicio, String facturado);
	
	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @param aplicarValidaciones 
	 * @return
	 */
	public abstract HashMap<String, Object> consultarIngresos(Connection con, int codigoPersona, boolean aplicarValidaciones);

	/**
	 * 
	 * @param con
	 * @param cantidad
	 * @param subCuenta
	 * @param numeroSolicitud
	 * @param servicio
	 * @param servicioCX
	 * @param tipoAsocio
	 * @param codigoDetPadre
	 * @param facturado
	 * @return
	 */
	public abstract boolean actualizarCantidadDetCargoServicioCx(Connection con,int cantidad, String subCuenta, String numeroSolicitud,String servicio, String servicioCX, int tipoAsocio,int codigoDetPadre, String facturado,int detcxhonorarios,int detasicxsalasmat);
	
	
	
	

}
