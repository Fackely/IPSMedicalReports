/*
 * Jun 15, 2007
 */
package com.princetonsa.mundo;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.BusquedaBarriosGenericaDao;
import com.princetonsa.dao.DaoFactory;

/**
 * 
 * @author Sebastián Gómez R.
 * Clase para el manejo de la busqueda de barrios generica
 */
public class BusquedaBarriosGenerica 
{
	 /**
     * Para hacer logs de debug / warn / error de esta funcionalidad.
     */
    private static Logger logger = Logger.getLogger(BusquedaBarriosGenerica.class);
    
    /**
     * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
     * @param tipoBD el tipo de base de datos que va a usar este objeto
     * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
     * son los nombres y constantes definidos en <code>DaoFactory</code>.
     * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
     */
    public static BusquedaBarriosGenericaDao busquedaDao()
    {
        return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getBusquedaBarriosGenericaDao();
    
    }
    
    /**
     * Método que realiza la consulta de los barrios segun parámetro enviados
     * @param con
     * @param codigoPais
     * @param codigoDepartamento
     * @param codigoCiudad
     * @param codigoLocalidad
     * @param criterioBarrio
     * @return
     */
    public static ArrayList<HashMap<String, Object>> consultar(Connection con,String codigoPais,String codigoDepartamento,String codigoCiudad,String criterioBarrio)
    {
    	HashMap campos = new HashMap();
    	campos.put("codigoPais", codigoPais);
    	campos.put("codigoDepartamento", codigoDepartamento);
    	campos.put("codigoCiudad", codigoCiudad);
    	campos.put("criterioBarrio", criterioBarrio);
    	return busquedaDao().consulta(con, campos);
    }
    
}
