package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.HashMap;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public interface DevolucionInventariosPacienteDao {
	
	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @param codigoCentroAtencion
	 * @return
	 */
	public abstract HashMap<String, Object> consultarIngresoCuentaPaciente(Connection con, int codigoPersona,int codigoCentroAtencion);
	
	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public abstract HashMap<String, Object> consultarPedidos(Connection con, int codigoPersona);
	
	
	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public abstract HashMap<String, Object> consultarSolicitudes(Connection con, int codigoPersona);
	
	/**
	 * 
	 * @param con
	 * @param despacho
	 * @return
	 */
	public abstract HashMap<String, Object> consultaDetalleSolicitudes(Connection con,int despacho);
	
	/**
	 * 
	 * @param con
	 * @param solicitud
	 * @return
	 */
	public abstract HashMap<String, Object> consultarFechaHoraDespachoSolicitudes(Connection con,int solicitud);
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract int insertarDevolucionSolicitudes(Connection con, HashMap vo);
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean insertarDetalleDevolucionSolicitudes(Connection con, HashMap vo);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public abstract HashMap<String, Object> consultaDevolucionSolicitudes(Connection con,int codigo);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public abstract HashMap<String, Object> consultaDetalleDevolucionSolicitudes(Connection con,int codigo);

}
