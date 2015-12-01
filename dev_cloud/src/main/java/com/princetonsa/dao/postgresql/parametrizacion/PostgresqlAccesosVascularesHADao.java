package com.princetonsa.dao.postgresql.parametrizacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.parametrizacion.AccesosVascularesHADao;
import com.princetonsa.dao.sqlbase.parametrizacion.SqlBaseAccesosVascularesHADao;

/**
 * 
 * @author wilson
 *
 */
public class PostgresqlAccesosVascularesHADao implements AccesosVascularesHADao 
{
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
    public double insertar(Connection con, HashMap<Object, Object> mapa)
    {
    	return SqlBaseAccesosVascularesHADao.insertar(con, mapa);
    }
    
    /**
     * 
     * @param con
     * @param viaAerea
     * @param articulo
     * @return
     */
    public boolean eliminar(Connection con, double codigoPKAccesoVascularHA)
    {
    	return SqlBaseAccesosVascularesHADao.eliminar(con, codigoPKAccesoVascularHA);
    }
    
    /**
     * 
     * @param con
     * @param mapa
     * @return
     */
    public boolean modificar(Connection con, HashMap<Object, Object> mapa)
    {
    	return SqlBaseAccesosVascularesHADao.modificar(con, mapa);
    }
    
    /**
     * 
     * @param con
     * @param centroCosto
     * @param institucion
     * @return
     */
    public HashMap<Object, Object> cargarTiposAccesoVascularCCInst( Connection con, int centroCosto, int institucion)
    {
    	return SqlBaseAccesosVascularesHADao.cargarTiposAccesoVascularCCInst(con, centroCosto, institucion);
    }
    
    /**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public HashMap<Object, Object> obtenerListadoAccesosVasculares(Connection con, int numeroSolicitud)
	{
		return SqlBaseAccesosVascularesHADao.obtenerListadoAccesosVasculares(con, numeroSolicitud);
	}
	
	/**
	 * 
	 * @param con
	 * @param articulo
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
    public HashMap<Object, Object> cargarLocalizacionesXTipoAcceso( Connection con, int tipoAccesoVascular, int centroCosto, int institucion)
    {
    	return SqlBaseAccesosVascularesHADao.cargarLocalizacionesXTipoAcceso(con, tipoAccesoVascular, centroCosto, institucion);
    }
    
    /**
     * 
     * @param con
     * @param fecha
     * @param hora
     * @param codigoPKAccesoVascularHA
     * @return
     */
	public boolean existeAccesoVascularHAFechaHora(Connection con, String fecha, String hora, double codigoPKAccesoVascularHA)
	{
		return SqlBaseAccesosVascularesHADao.existeAccesoVascularHAFechaHora(con, fecha, hora, codigoPKAccesoVascularHA);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPKAccesoVascularHA
	 * @return
	 */
	public boolean actualizarGeneroConsumo(Connection con, HashMap parametros)
	{
		return SqlBaseAccesosVascularesHADao.actualizarGeneroConsumo(con,parametros);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPKAccesoVascularHA
	 * @return
	 */
	public boolean actualizarGeneroOrden(Connection con, double codigoPKAccesoVascularHA)
	{
		return SqlBaseAccesosVascularesHADao.actualizarGeneroOrden(con, codigoPKAccesoVascularHA);
	}
}