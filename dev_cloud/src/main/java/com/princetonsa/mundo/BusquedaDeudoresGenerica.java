/*
 * Junio 26, 2009
 */
package com.princetonsa.mundo;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.BusquedaDeudoresGenericaDao;
import com.princetonsa.dao.DaoFactory;

/**
 * 
 * @author Sebastián Gómez R.
 * Clase para el manejo de la busqueda de deudores generica
 */
public class BusquedaDeudoresGenerica 
{
	/**
     * Para hacer logs de debug / warn / error de esta funcionalidad.
     */
    private static Logger logger = Logger.getLogger(BusquedaDeudoresGenerica.class);
    
    /**
     * Indices para las llaves dwel listado
     */
    public static final String[] indicesListado = {"codigo_","identificacion_","descripcion_","tipo_"}; 
    
    /**
     * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
     * @param tipoBD el tipo de base de datos que va a usar este objeto
     * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
     * son los nombres y constantes definidos en <code>DaoFactory</code>.
     * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
     */
    public static BusquedaDeudoresGenericaDao busquedaDao()
    {
        return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getBusquedaDeudoresGenericaDao();
    
    }
    
    /**
	 * Método que realiza la busqueda de deudores
	 * @param con
     * @param nuevoDeudor 
	 * @param campos
	 * @return
	 */
	public static HashMap<String, Object> busqueda(Connection con,String tipoDeudor,String codigoTipoIdentificacion,String numeroIdentificacion,String primerApellido,String primerNombre,String descripcion, boolean nuevoDeudor,boolean activo)
	{
		HashMap<String, Object> campos = new HashMap<String, Object>();
		campos.put("tipoDeudor",tipoDeudor);
		campos.put("codigoTipoIdentificacion",codigoTipoIdentificacion);
		campos.put("numeroIdentificacion",numeroIdentificacion);
		campos.put("primerApellido",primerApellido);
		campos.put("primerNombre",primerNombre);
		campos.put("descripcion",descripcion);
		campos.put("nuevoDeudor",nuevoDeudor);
		campos.put("activo",activo);
		return busquedaDao().busqueda(con, campos);
	}
}
