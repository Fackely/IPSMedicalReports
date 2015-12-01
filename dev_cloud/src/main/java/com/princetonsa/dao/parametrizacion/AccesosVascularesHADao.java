package com.princetonsa.dao.parametrizacion;

import java.sql.Connection;
import java.util.HashMap;

/**
 * 
 * @author wilson
 *
 */
public interface AccesosVascularesHADao 
{
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
    public double insertar(Connection con, HashMap<Object, Object> mapa);
    
    /**
     * 
     * @param con
     * @param viaAerea
     * @param articulo
     * @return
     */
    public boolean eliminar(Connection con, double codigoPKAccesoVascularHA);
    
    /**
     * 
     * @param con
     * @param mapa
     * @return
     */
    public boolean modificar(Connection con, HashMap<Object, Object> mapa);
    
    /**
     * 
     * @param con
     * @param centroCosto
     * @param institucion
     * @return
     */
    public HashMap<Object, Object> cargarTiposAccesoVascularCCInst( Connection con, int centroCosto, int institucion);
    
    /**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public HashMap<Object, Object> obtenerListadoAccesosVasculares(Connection con, int numeroSolicitud);
	
	/**
	 * 
	 * @param con
	 * @param articulo
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
    public HashMap<Object, Object> cargarLocalizacionesXTipoAcceso( Connection con, int tipoAccesoVascular, int centroCosto, int institucion);
    
    /**
     * 
     * @param con
     * @param fecha
     * @param hora
     * @param codigoPKAccesoVascularHA
     * @return
     */
	public boolean existeAccesoVascularHAFechaHora(Connection con, String fecha, String hora, double codigoPKAccesoVascularHA);
	
	/**
	 * 
	 * @param con
	 * @param codigoPKAccesoVascularHA
	 * @return
	 */
	public boolean actualizarGeneroConsumo(Connection con, HashMap parametros);
	
	/**
	 * 
	 * @param con
	 * @param codigoPKAccesoVascularHA
	 * @return
	 */
	public boolean actualizarGeneroOrden(Connection con, double codigoPKAccesoVascularHA);
}