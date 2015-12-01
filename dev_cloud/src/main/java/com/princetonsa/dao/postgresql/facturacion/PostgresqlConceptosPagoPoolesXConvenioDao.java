package com.princetonsa.dao.postgresql.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.facturacion.ConceptosPagoPoolesXConvenioDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseConceptosPagoPoolesXConvenioDao;



/**
 * 
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com
 *
 */
public class PostgresqlConceptosPagoPoolesXConvenioDao implements ConceptosPagoPoolesXConvenioDao
{
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
	public boolean insertarConceptosPagoPoolesXConvenio (Connection connection, HashMap parametros)
	{
		return SqlBaseConceptosPagoPoolesXConvenioDao.insertarConceptosPagoPoolesXConvenio(connection, parametros);
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
	public boolean modificarConceptosPagosPoolesXConvenio (Connection  connection, HashMap parametros)
	{
		return SqlBaseConceptosPagoPoolesXConvenioDao.modificarConceptosPagosPoolesXConvenio(connection, parametros);
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
	public HashMap consultarConceptosPagosPoolesXConvenio (Connection connection, HashMap parametros)
	{
		return SqlBaseConceptosPagoPoolesXConvenioDao.consultarConceptosPagosPoolesXConvenio(connection, parametros);
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
	public boolean eliminarConceptosPagosPoolesXConvenio (Connection connection, HashMap parametros)
	{
		return SqlBaseConceptosPagoPoolesXConvenioDao.eliminarConceptosPagosPoolesXConvenio(connection, parametros);
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
	public HashMap consultarInfoLog (Connection connection, HashMap parametros)
	{
		return SqlBaseConceptosPagoPoolesXConvenioDao.consultarInfoLog(connection, parametros);
	}
	
}