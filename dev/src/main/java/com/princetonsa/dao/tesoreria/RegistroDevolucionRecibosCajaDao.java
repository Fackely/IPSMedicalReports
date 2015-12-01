/**
 * 
 */
package com.princetonsa.dao.tesoreria;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dto.odontologia.DtoVentaTarjetasCliente;
import com.princetonsa.dto.tesoreria.DtoProcesoDevolucionRC;

/**
 * @author axioma
 *
 */
public interface RegistroDevolucionRecibosCajaDao {

	/**
	 * 
	 * @param vo
	 * @return
	 */
	public abstract HashMap<String, Object> consultarRecibosCaja(HashMap<String, Object> vo);

	/**
	 * 
	 * @param vo
	 * @return
	 */
	public abstract boolean guardarDevolucion(HashMap<String, Object> vo);
	
	/**
	 * se obtiene el valor del concepto de ingreso de tesoreria
	 * @param parametros
	 * @return String 
	 */
	public String getValorConceptoIngTesoreria(HashMap parametros);
	
	/**
	 * 
	 * @param numeroRecibo
	 * @return
	 */
	public   ArrayList<DtoVentaTarjetasCliente> consultaFacturas(String numeroRecibo);
	
	/**
	 * 
	 * @param con
	 * @param arqueo
	 * @param cierre
	 */
	public boolean actualizarEstadoArqueoCierreDevol(Connection con, String arqueo, String cierreCaja, ArrayList<Integer> codigosPkDevol);
	
	
	/**
	 * 
	 */
	public DtoProcesoDevolucionRC estaEnProcesoDevolucion(String nroRC,int institucion, String idSesionOPCIONAL, boolean igualSession);
	
	/**
	 * 
	 */
	public boolean empezarBloqueoDevolucion(String nroRC,int institucion,String loginUsuario,String idSesion);
	
	/**
	 * 
	 */
	public boolean cancelarBloqueoDevolucion(String nroRC, int institucion, String idSesion);
	
}