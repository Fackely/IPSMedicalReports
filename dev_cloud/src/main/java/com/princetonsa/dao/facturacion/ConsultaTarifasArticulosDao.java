package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.HashMap;

public interface ConsultaTarifasArticulosDao 
{

	
	/**
	 * 
	 * @param con 
	 * @param codigoArticulo
	 * @param descripcionArticulo
	 * @param codigoInterfaz
	 * @param clase
	 * @param grupo
	 * @param subgrupo
	 * @param naturaleza
	 * @param codigoEstandarBusquedaArticulos 
	 * @return
	 */
	public abstract HashMap<String, Object> consultarArticulos(Connection con, String codigoArticulo, String descripcionArticulo, String codigoInterfaz, String clase, String grupo, String subgrupo, String naturaleza, String codigoEstandarBusquedaArticulos);
	
	/**
	 * 
	 * @param con
	 * @param articulo
	 * @return
	 */
	public abstract HashMap<String, Object> consultarDetalleArticulos(Connection con, String articulo);

	/**
	 * Metodo encargado de armar la consulta de analisis de costo
	 * @author Jhony Alexander Duque A.
	 * @param criterios
	 * ---------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ---------------------------
	 * -- institucion  --> Requerido
	 * -- codigoArticulo  --> opcional
	 * -- descripcionArticulo  --> opcional
	 * -- codigoInterfaz  --> opcional
	 * -- clase  --> opcional
	 * -- grupo  --> opcional
	 * -- subgrupo  --> opcional
	 * -- naturaleza  --> opcional
	 * -- esquemaTarifario  --> opcional
	 * @return String
	 */
	public String obtenerConsulta (HashMap criterios);
	
	
	
    /**
     * Metodo encargado de ejecutar la  consulta enviada.
     * @param connection
     * @param consulta
     * @return
     */
    public HashMap ejecutarConsulta (Connection connection,String consulta);
    
	
}
