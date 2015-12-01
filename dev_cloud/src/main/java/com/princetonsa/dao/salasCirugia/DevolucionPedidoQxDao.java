package com.princetonsa.dao.salasCirugia;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

/**
 * @author Mauricio Jllo
 * Fecha: Junio de 2008
 */

public interface DevolucionPedidoQxDao
{

	/**
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public abstract HashMap listadoSolicitudes(Connection con, int codigoCuenta);
	
	/**
	 * @param con
	 * @param codigoPeticion
	 * @return
	 */
	public abstract String validarPeticionDevolucionesPendientes(Connection con, int codigoPeticion);
	
	/**
	 * @param con
	 * @param codigoPeticion
	 * @return
	 */
	public abstract HashMap<String,Object> detallePeticion(Connection con, int codigoPeticion);
	
	/**
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public abstract HashMap detalleSolicitudArticulos(Connection con, int codigoSolicitud, boolean validacionFactura);
	
	/**
	 * @param con
	 * @param parametros
	 * @return
	 */
	public abstract HashMap<String,Object> listadoPeticionesPorRangos(Connection con, HashMap vo);
	
	/**
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract int devolucionPedidoQx(Connection con, HashMap vo);
	
	/**
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean devolverDetallePedidoQx(Connection con, HashMap vo, int codigoDevolucion);
	
	/**
	 * @param con
	 * @param codigoPeticion
	 * @param codigoArticulo
	 * @return
	 */
	public abstract HashMap<String,Object> consultarAlmacenes(Connection con, int codigoPeticion, int codigoArticulo);
	
	/**
	 * @param con
	 * @param codigoDevolucion
	 * @return
	 */
	public abstract String consultarEstadoDevolucion(Connection con, String codigoDevolucion);
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public abstract Vector<String> obtenerPedidosPeticionesQx(Connection con, int numeroSolicitud);
	
	/**
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public abstract HashMap<String,Object> listadoPeticiones(Connection con, int codigoIngreso, int codigoInstitucion);
	
	/**
	 * @param con
	 * @param codigoPeticion
	 * @return
	 */
	public abstract HashMap<String,Object> detallePeticionArticulos(Connection con, int codigoPeticion);
}