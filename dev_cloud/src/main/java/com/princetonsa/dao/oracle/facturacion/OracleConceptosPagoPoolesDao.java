package com.princetonsa.dao.oracle.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.facturacion.ConceptosPagoPoolesDao;

import com.princetonsa.dao.sqlbase.facturacion.SqlBaseConceptosPagoPoolesDao;




public class OracleConceptosPagoPoolesDao implements ConceptosPagoPoolesDao
{

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
	public boolean insertarConceptosPagoPooles (Connection connection, HashMap parametros)
	{
		return SqlBaseConceptosPagoPoolesDao.insertarConceptosPagoPooles(connection, parametros);
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
	public  boolean modificarConceptosPagosPooles (Connection  connection, HashMap parametros)
	{
		return SqlBaseConceptosPagoPoolesDao.modificarConceptosPagosPooles(connection, parametros);
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
	public HashMap consultarConceptosPagosPooles (Connection connection, HashMap parametros)
	{
		return SqlBaseConceptosPagoPoolesDao.consultarConceptosPagosPooles(connection, parametros);
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
	public boolean eliminarConceptosPagosPooles (Connection connection, HashMap parametros)
	{
		return SqlBaseConceptosPagoPoolesDao.eliminarConceptosPagosPooles(connection, parametros);
	}
	
}