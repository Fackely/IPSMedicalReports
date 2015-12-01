package com.princetonsa.dao.postgresql.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.facturacion.ConsultaTarifasArticulosDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseConsultaTarifasArticulosDao;

public class PostgresqlConsultaTarifasArticulosDao implements
		ConsultaTarifasArticulosDao

{

	/**
	 * 
	 */
	public HashMap<String, Object> consultarArticulos(Connection con, String codigoArticulo, String descripcionArticulo, String codigoInterfaz, String clase, String grupo, String subgrupo, String naturaleza,String codigoEstandarBusquedaArticulos) 
	{
		return SqlBaseConsultaTarifasArticulosDao.consultarArticulos(con, codigoArticulo, descripcionArticulo, codigoInterfaz, clase, grupo, subgrupo, naturaleza,codigoEstandarBusquedaArticulos);
	}
	
	
	
	/**
	 * 
	 */
	public HashMap<String, Object> consultarDetalleArticulos(Connection con, String articulo) 
	{
		return SqlBaseConsultaTarifasArticulosDao.consultarDetalleArticulos(con, articulo);
	}
	
	
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
	public String obtenerConsulta (HashMap criterios)
	{
		return SqlBaseConsultaTarifasArticulosDao.obtenerConsulta(criterios);
	}
	
	
    /**
     * Metodo encargado de ejecutar la  consulta enviada.
     * @param connection
     * @param consulta
     * @return
     */
    public HashMap ejecutarConsulta (Connection connection,String consulta)
    {
    	return SqlBaseConsultaTarifasArticulosDao.ejecutarConsulta(connection, consulta);
    }
	
}
