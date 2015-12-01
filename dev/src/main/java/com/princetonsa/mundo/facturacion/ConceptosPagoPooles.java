package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.ConceptosPagoPoolesDao;



public class ConceptosPagoPooles
{
	/*-----------------------
	 * 		ATRIBUTOS
	 ------------------------*/
	
	/*-----------------------
	 * 		FIN ATRIBUTOS
	 ------------------------*/

	
/*----------------------------------
 *  		METODOS
 ---------------------------------*/
	static Logger logger = Logger.getLogger(ConceptosPagoPooles.class);
	
	/**
	 * Instancia DAO
	 * */
	public static ConceptosPagoPoolesDao conceptosPagoPoolesDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConceptosPagoPoolesDao();		
	}	
	
	
	/**
	 * Metodo encargado de hacer la insercion el la BD
	 * El HashMap parametros debe contener los siguientes
	 * campos.
	 * --------------------------------------------------
	 * 				KEY'S DE PARAMETROS
	 * --------------------------------------------------
	 *--codigoConcepto --> Requerido.
	 *--descripcionConcepto --> Requerido.
	 *--tipoConcepto --> Requerido.
	 *--institucion --> Requerido.
	 *--usuario --> Requerido
	 *@param connection
	 *@param parametros 
	 **/
	public static boolean insertarConceptosPagoPooles (Connection connection, HashMap parametros)
	{
		return conceptosPagoPoolesDao().insertarConceptosPagoPooles(connection, parametros);
	}
	
	/**
	 * Este metodo esta encargado de Modificar los datos ya existentes en ls BD
	 * Los key's del HashMap parametros son:
	 * -------------------------------
	 * 		 KEY'S PARAMETROS
	 * -------------------------------
	 * --
	 * @param connection
	 * @param parametros
	 * @return
	 */
	public static boolean modificarConceptosPagosPooles (Connection  connection, HashMap parametros)
	{
		return conceptosPagoPoolesDao().modificarConceptosPagosPooles(connection, parametros);
	}
	
	/**
	 * Este metodo se encarga de Consultar los datos de la tabla conceptos_pagos_pooles
	 * Los key's del HashMap parametros son:
	 * ---------------------------------
	 * 		KEY'S DE PARAMETROS
	 * ---------------------------------
	 * --institucion --> Requerido
	 * 
	 * Los key del mapa que devuelve son:
	 * -------------------------------
	 *  KEY'S DEL MAPA QUE DEVUELVE
	 * ------------------------------
	 * --codigoConcepto, descripcionConcepto, 
	 *   tipoConcepto, estabd, INDICES_MAPA.
	 * @param connection
	 * @param parametros
	 * @return
	 **/
	public static HashMap consultarConceptosPagosPooles (Connection connection, HashMap parametros)
	{
		return conceptosPagoPoolesDao().consultarConceptosPagosPooles(connection, parametros);
	}
	
	/**
	 * Metodo encargado de eliminar un registro dentro
	 * del a tabla conceptos_pagos_pooles.
	 * Los key's de paramtros son:
	 * ---------------------------
	 * 	  KEY'S DE PARAMETROS
	 * ---------------------------
	 * --codigo --> Requerido
	 * --institucion --> Requerido
	 * @param connection
	 * @param parametros
	 * @return
	 */
	public static boolean eliminarConceptosPagosPooles (Connection connection, HashMap parametros)
	{
		return conceptosPagoPoolesDao().eliminarConceptosPagosPooles(connection, parametros);
	}
	
	
	
	
	/*----------------------------------
	 *  		FIN METODOS
	 ---------------------------------*/
	
}