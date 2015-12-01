package com.princetonsa.mundo.historiaClinica;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.ConsentimientoInformadoDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseConsentimientoInformadoDao;
import com.princetonsa.dto.odontologia.DtoConsentimientoInformadoOdonto;


/**
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com
 */

public class ConsentimientoInformado
{
	/**
	 *Atributos de Consentimiento Informado 
	 */
	private ConsentimientoInformadoDao consentimientoInformadoDao;
	private Logger logger = Logger.getLogger(ConsentimientoInformado.class);
	
	
	/**
	 * ----------Metodos------------------------
	 */

	
	/**
	 * Constructos de la clase ConsentiemientoInformado
	 */
	
	public void ConsentimientoInformado()
	{
		DaoFactory MyFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		consentimientoInformadoDao = MyFactory.getConsentimientoInformadoDao();
	}
	
	
	/**
	 *se instancia el DAO 
	 */
	
	public static ConsentimientoInformadoDao consentimientoInformadoDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsentimientoInformadoDao();
	}

	/**
	 * Insertar un registro de consentimiento informado postgres
	 * @param Connection   connection
	 * @param HashMap 	consentimientoInformado
	 * */
	
	public static boolean insertarConsentimientoInformado (Connection connection, HashMap consentimientoInformado) 
	{
		return consentimientoInformadoDao().insertarConsentimientoInformado(connection, consentimientoInformado);
		
	}
	
	/**
	 * Modifica  consentimiento informado registrado
	 * @param Connection   connection
	 * @param HashMap 	consentimientoInformado
	 * */
	
	public static boolean modificarConsentimientoInformado(Connection connection, HashMap consentimientoInformado)
	{
		return consentimientoInformadoDao().modificarConsentimientoInformado(connection, consentimientoInformado);
	}
	
	/**
	 * Elimina consentimiento informado registrado
	 * @param Connection   connection
	 * @param HashMap 	consentimientoInformado
	 * */
	
	public static boolean eliminarConsentimientoInformado(Connection connection, HashMap consentimientoInformado)
	{
		return consentimientoInformadoDao().eliminarConsentimientoInformado(connection, consentimientoInformado);
	}
	
	/**
	 * Consulta basica de consentimiento informado por keys
	 * @param Connection connection
	 * @param HashMap 	consentimientoInformado
	 * */
	
	public static HashMap consultaConsentimientoInformado(Connection connection, HashMap consentimientoInformado)
	{
		return consentimientoInformadoDao().consultaConsentimientoInformado(connection, consentimientoInformado);
	}
	
	
	/**
	 * Insertar un registro de detalle consentimiento informado
	 * @param Connection   connection
	 * @param HashMap 	detalleConsentimientoInformado
	 * */
	
	public static boolean insertarDetalleConsentimientoInformado (Connection connection, HashMap detalleConsentimientoInformado)
	{
		return consentimientoInformadoDao().insertarDetalleConsentimientoInformado(connection, detalleConsentimientoInformado);
	}
	
	/**
	 * Elimina detalle consentimiento informado registrado
	 * @param Connection   connection
	 * @param HashMap 	consentimientoInformado
	 * */
	
	public static boolean eliminardetalleConsentimientoInformado(Connection connection, HashMap detalleConsentimientoInformado)
	{
		return consentimientoInformadoDao().eliminardetalleConsentimientoInformado(connection, detalleConsentimientoInformado);
	}
	
	
	/**
	 * Consulta basica de detalle consentimiento informado por keys
	 * @param Connection connection
	 * @param HashMap 	detalleConsentimientoInformado
	 * */
	
	public static HashMap consultaDetalleConsentimientoInformado(Connection connection, HashMap detalleConsentimientoInformado)
	{
		return consentimientoInformadoDao().consultaDetalleConsentimientoInformado(connection, detalleConsentimientoInformado);
	}
	
	/**
	 * Modifica el detalle consentimiento informado registrado
	 * @param Connection   connection
	 * @param HashMap 	detalleConsentimientoInformado
	 * */
	public static boolean modificarDetalleConsentimientoInf(Connection connection, HashMap detalleConsentimientoInformado)
	{
		return consentimientoInformadoDao().modificarDetalleConsentimientoInf(connection, detalleConsentimientoInformado);
	}
	
	
	/**
	 * Consulta los consentimientos informados de un grupo de servicios a partir del servicio asociado 
	 * @param Connection connection 
	 * @param HashMap parametros
	 * */
	public static HashMap buscarConsentimientoInfServicio(Connection connection, HashMap parametros)
	{
		return consentimientoInformadoDao().buscarConsentimientoInfServicio(connection, parametros);
	}	
	
	
	/**
	 * Inserta el registro que indica que sea imprimido un consentimiento informado
	 * @param Connection connection 
	 * @param HashMap parametros
	 * */
	public static boolean insertarHistorialConsentimientoInf(Connection connection, HashMap parametros)
	{
		return consentimientoInformadoDao().insertarHistorialConsentimientoInf(connection, parametros);
	}
	
	/**
	 * Consulta que devuelve el servicio, grupo y nemobre del servicio.
	 * @param Connection connection
	 * @param HashMap 	consentimientoInformado
	 * */
	
	public  static HashMap consultaConsentimientoInformadoXingreso(Connection connection, HashMap consentimientoInformado)
	{
		return consentimientoInformadoDao().consultaConsentimientoInformadoXingreso(connection, consentimientoInformado);
	}
	
	/**
	 * Consulta de los formatos del servicio
	 * @param connection
	 * @param parametros
	 * @return
	 */
	public static HashMap impresionCosentimientoinformadoXIngreso (Connection connection,HashMap parametros)
	{
		return consentimientoInformadoDao().impresionCosentimientoinformadoXIngreso(connection, parametros);
	}
	
	/**
	 * 
	 * @param codigoPrograma
	 * @return
	 */
	public static boolean programaTieneConsentimientosInformados(int codigoPrograma)
	{
		return consentimientoInformadoDao().programaTieneConsentimientosInformados(codigoPrograma);
	}
	
	/**
	 * 
	 * @param codigophp
	 * @return
	 */
	public static boolean programaHallazgoPiezaTienConsentimientoInfo(int codigophp)
	{
		return consentimientoInformadoDao().programaHallazgoPiezaTienConsentimientoInfo(codigophp);
	}


	/**
	 * 
	 * @param codigosPHP
	 * @param planTratamiento 
	 * @param todos
	 * @return
	 */
	public static ArrayList<DtoConsentimientoInformadoOdonto> consultarInfoConsentimientoOdonto(String codigosPHP, String planTratamiento, boolean todos) 
	{
		return consentimientoInformadoDao().consultarInfoConsentimientoOdonto(codigosPHP,planTratamiento,todos);
	}


	/**
	 * 
	 * @param con
	 * @param consentimientoOdonto
	 * @param codigoCita 
	 * @return
	 */
	public static boolean guardarConsentimientoOdontologia(Connection con,ArrayList<DtoConsentimientoInformadoOdonto> consentimientoOdonto, String codigoCita) 
	{
		return consentimientoInformadoDao().guardarConsentimientoOdontologia(con,consentimientoOdonto,codigoCita);
	}


	public static ArrayList<Integer> obtenerProgramasHallazgoPiezasParaConsentimiento(
			Connection con, int planTratamiento, int codigoPrograma, String codigoCita, boolean validaPresupuestoContratado) {
		return consentimientoInformadoDao().obtenerProgramasHallazgoPiezasParaConsentimiento(con,planTratamiento,codigoPrograma,codigoCita,validaPresupuestoContratado);
	}
}