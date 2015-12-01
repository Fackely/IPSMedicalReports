package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.ConceptosPagoPoolesXConvenioDao;



public class ConceptosPagoPoolesXConvenio
{
	/*-----------------------
	 * 		ATRIBUTOS
	 ------------------------*/
	static Logger logger = Logger.getLogger(ConceptosPagoPoolesXConvenio.class);
	
	/*-----------------------
	 * 		FIN ATRIBUTOS
	 ------------------------*/

	
/*----------------------------------
 *  		METODOS
 ---------------------------------*/
	
	/**
	 * Instancia DAO
	 * */
	public static ConceptosPagoPoolesXConvenioDao conceptosPagoPoolesXConvenioDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConceptosPagoPoolesXConvenioDao();		
	}	
	
	/**
	 * Metodo encargado de ingresar los datos en la tabla
	 * concept_pag_pool_x_conv.
	 * @param connection
	 * @param parametros
	 * @return boolean
	 * ------------------------------------------------
	 * 				KEY'S DE PARAMETROS
	 * ------------------------------------------------
	 * --codigopool --> Requerido
	 * --codigoconvenio --> Requerido
	 * --codigoconcepto --> Requerido
	 * --codigotiposervicio --> Requerido
	 * --porcentaje --> Requerido
	 * --institucion --> Requerido
	 * --usuario --> Requerido
	 *--codigocuentacontable --> Opcional
	 */
	public static boolean insertarConceptosPagoPoolesXConvenio (Connection connection, HashMap parametros)
	{
		return conceptosPagoPoolesXConvenioDao().insertarConceptosPagoPoolesXConvenio(connection, parametros);
	}
	
	/**
	 * Metodo encargado de modificar los datos de la tabla 
	 * modificarConceptosPagosPoolesXConvenio.
	 * @param connection
	 * @param parametros
	 * @return boolean
	 * -------------------------------------------
	 * 				KEY'S DE PARAMETROS
	 * -------------------------------------------
	 * --codigoconceptoxconvenio --> Requerido
	 * --institucion --> Requerido
	 * --codigopool --> Requerido
	 * --codigoconvenio --> Requerido
	 * --codigoconcepto --> Requerido
	 * --codigotiposervicio --> Requerido
	 * --porcentaje --> Requerido
	 * --usuario --> Requerido
	 * --codigocuentacontable --> Opcional
	 */
	public static boolean modificarConceptosPagosPoolesXConvenio (Connection  connection, HashMap parametros)
	{
		return conceptosPagoPoolesXConvenioDao().modificarConceptosPagosPoolesXConvenio(connection, parametros);
	}
	
	
	/**
	 * Metodo encargado de Consutlar los conceptos por 
	 * pago de pooles por convenio; esta consulta se puede
	 * filtar por los siguientes criterios que vienen en 
	 * el HashMap parametros.
	 * ----------------------------------
	 * 			KEY'S PARAMETROS
	 * ----------------------------------
	 * --institucion --> Requerido
	 * --codigopool --> Opcional
	 * --codigoconvenio --> Opcional
	 * -----------------------------------
	 * 	KEY'S DEL MAPA QUE DEVUELVE
	 * -----------------------------------
	 * codigoconceptoxconvenio, codigopool, 
	 * codigoconvenio, codigoconcepto, 
	 * codigotiposervicio, porcentaje, 
	 * codigocuentacontable, institucion, 
	 * estabd
	 * @param connection
	 * @param parametros
	 * @return mapa
	 */
	public static HashMap consultarConceptosPagosPoolesXConvenio (Connection connection, HashMap parametros)
	{
		return conceptosPagoPoolesXConvenioDao().consultarConceptosPagosPoolesXConvenio(connection, parametros);
	}
	
	/**
	 * Metodo encargado de elimiar un registro de conceptos
	 * pagos pooles por convenio.
	 * ------------------------------
	 * 		KEY'S PARAMETROS
	 * ------------------------------
	 * --codigoconceptoxconvenio --> Requerido
	 * --institucion --> Requerido
	 * @param connection
	 * @param parametros
	 * @return boolean
	 */
	public static boolean eliminarConceptosPagosPoolesXConvenio (Connection connection, HashMap parametros)
	{
		return conceptosPagoPoolesXConvenioDao().eliminarConceptosPagosPoolesXConvenio(connection, parametros);
	}
	
	
	/**
	 * Metodo encargado de consultar los nombreso descripcion para generar el log 
	 * tipo archivo; a este metodo me le entra un HashMap de parametros y devuelve 
	 * con la respuesta llamado Mapa.
	 * El HashMap parametros cuenta con los siguientes key's:
	 * --------------------------------
	 *       KEY'S DE PARAMETROS
	 * --------------------------------
	 * --codigopool --> Requerido
	 * --codigoconvenio --> Requerido
	 * --codigoconcepto --> Requerido
	 * --codigotiposervicio --> Requerido
	 * --institucion --> Requerido
	 * --codigocuentacontable --> Opcional
	 * 
	 * -------------------------------
	 * 		  KEY'S DE MAPA
	 * -------------------------------
	 * nombrepool, nombrepool, nombreconcepto,
	 * nombretiposervicico, nombreinstitucion, 
	 * nombrecuentacontable.
	 */
	public static HashMap consultarInfoLog (Connection connection, HashMap parametros)
	{
		return conceptosPagoPoolesXConvenioDao().consultarInfoLog(connection, parametros);
	}
}