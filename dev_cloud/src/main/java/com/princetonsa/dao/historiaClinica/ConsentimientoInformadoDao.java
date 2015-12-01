package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseConsentimientoInformadoDao;
import com.princetonsa.dto.odontologia.DtoConsentimientoInformadoOdonto;


/**
 * @author jhony Alexander Duque
 * jduque@princetonsa.com    
 * */

public interface ConsentimientoInformadoDao
{

	/**
	 * Insertar un registro de consentimiento informado
	 * @param Connection   connection
	 * @param HashMap 	consentimientoInformado
	 * */
	public boolean insertarConsentimientoInformado (Connection connection, HashMap consentimientoInformado);
	
	/**
	 * Modifica  consentimiento informado registrado
	 * @param Connection   connection
	 * @param HashMap 	consentimientoInformado
	 * */
	public  boolean modificarConsentimientoInformado(Connection connection, HashMap consentimientoInformado);
		
	/**
	 * Elimina consentimiento informado registrado
	 * @param Connection   connection
	 * @param HashMap 	consentimientoInformado
	 * */
	public boolean eliminarConsentimientoInformado(Connection connection, HashMap consentimientoInformado);
		
	/**
	 * Consulta basica de consentimiento informado por keys
	 * @param Connection connection
	 * @param HashMap 	consentimientoInformado
	 * */
	public HashMap consultaConsentimientoInformado(Connection connection, HashMap consentimientoInformado);
	
	/**
	 * Insertar un registro de detalle consentimiento informado
	 * @param Connection   connection
	 * @param HashMap 	detalleConsentimientoInformado
	 * */
	public  boolean insertarDetalleConsentimientoInformado (Connection connection, HashMap detalleConsentimientoInformado);
	
	
	/**
	 * Elimina detalle consentimiento informado registrado
	 * @param Connection   connection
	 * @param HashMap 	consentimientoInformado
	 * */
	public boolean eliminardetalleConsentimientoInformado(Connection connection, HashMap detalleConsentimientoInformado);
	
	/**
	 * Consulta basica de detalle consentimiento informado por keys
	 * @param Connection connection
	 * @param HashMap 	detalleConsentimientoInformado
	 * */
	public HashMap consultaDetalleConsentimientoInformado(Connection connection, HashMap detalleConsentimientoInformado);
	
	/**
	 * Modifica el detalle consentimiento informado registrado
	 * @param Connection   connection
	 * @param HashMap 	detalleConsentimientoInformado
	 * */
	public boolean modificarDetalleConsentimientoInf(Connection connection, HashMap detalleConsentimientoInformado);
	
	
	/**
	 * Consulta los consentimientos informados de un grupo de servicios a partir del servicio asociado 
	 * @param Connection connection 
	 * @param HashMap parametros
	 * */
	public HashMap buscarConsentimientoInfServicio(Connection connection, HashMap parametros);
	
	/**
	 * Inserta el registro que indica que sea imprimido un consentimiento informado
	 * @param Connection connection 
	 * @param HashMap parametros
	 * */
	public boolean insertarHistorialConsentimientoInf(Connection connection, HashMap parametros);
	
	
	/**
	 * Consulta que devuelve el servicio, grupo y nemobre del servicio.
	 * @param Connection connection
	 * @param HashMap 	consentimientoInformado
	 * */
	
	public  HashMap consultaConsentimientoInformadoXingreso(Connection connection, HashMap consentimientoInformado);

	/**
	 * Consulta de los formatos del servicio
	 * @param connection
	 * @param parametros
	 * @return
	 */
	public HashMap impresionCosentimientoinformadoXIngreso (Connection connection,HashMap parametros);

	/**
	 * 
	 */
	public boolean programaTieneConsentimientosInformados(int codigoPrograma);

	/**
	 * 
	 * @param codigophp
	 * @return
	 */
	public boolean programaHallazgoPiezaTienConsentimientoInfo(int codigophp);

	/**
	 * 
	 * @param codigosPHP
	 * @param planTratamiento 
	 * @param todos
	 * @return
	 */
	public ArrayList<DtoConsentimientoInformadoOdonto> consultarInfoConsentimientoOdonto(String codigosPHP, String planTratamiento, boolean todos);

	/**
	 * 
	 * @param con
	 * @param consentimientoOdonto
	 * @param codigoCita 
	 * @return
	 */
	public boolean guardarConsentimientoOdontologia(Connection con,ArrayList<DtoConsentimientoInformadoOdonto> consentimientoOdonto, String codigoCita);

	public ArrayList<Integer> obtenerProgramasHallazgoPiezasParaConsentimiento(
			Connection con, int planTratamiento, int codigoPrograma, String codigoCita, boolean validaPresupuestoContratado);

	
}

