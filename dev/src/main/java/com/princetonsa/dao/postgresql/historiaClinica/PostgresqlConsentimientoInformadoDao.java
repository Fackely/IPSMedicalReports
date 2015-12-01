package com.princetonsa.dao.postgresql.historiaClinica;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.historiaClinica.ConsentimientoInformadoDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseConsentimientoInformadoDao;
import com.princetonsa.dto.odontologia.DtoConsentimientoInformadoOdonto;


/**
 * @author jhony Alexander Duque
 * jduque@princetonsa.com    
 * */

public class PostgresqlConsentimientoInformadoDao implements ConsentimientoInformadoDao
{
	/**
	 * Cadena de insercion Postgresql de consentimiento informado
	 */
	
	private static final String cadenaInsertarStr = "INSERT INTO historiaclinica.consentimientoinformado (codigo,institucion,descripcion,nombre_archivo,nombre_original,usuario_modifica,fecha_modifica,hora_modifica) VALUES(nextval('historiaclinica.seq_consentimientoinformado'),?,?,?,?,?,?,?) ";
	 
	
	/**
	 * Insertar un registro de consentimiento informado postgres
	 * @param Connection   connection
	 * @param HashMap 	consentimientoInformado
	 * */
	
	public boolean insertarConsentimientoInformado (Connection connection, HashMap consentimientoInformado) 
	{
		return SqlBaseConsentimientoInformadoDao.insertarConsentimientoInformado(connection, consentimientoInformado, cadenaInsertarStr);
		
	}
	  
	
	
	/**
	 * Modifica  consentimiento informado registrado
	 * @param Connection   connection
	 * @param HashMap 	consentimientoInformado
	 * */
	
	public boolean modificarConsentimientoInformado(Connection connection, HashMap consentimientoInformado)
	{
		return SqlBaseConsentimientoInformadoDao.modificarConsentimientoInformado(connection, consentimientoInformado);
	}
	
	/**
	 * Elimina consentimiento informado registrado
	 * @param Connection   connection
	 * @param HashMap 	consentimientoInformado
	 * */
	
	public boolean eliminarConsentimientoInformado(Connection connection, HashMap consentimientoInformado)
	{
		return SqlBaseConsentimientoInformadoDao.eliminarConsentimientoInformado(connection, consentimientoInformado);
	}
	
	/**
	 * Consulta basica de consentimiento informado por keys
	 * @param Connection connection
	 * @param HashMap 	consentimientoInformado
	 * */
	
	public HashMap consultaConsentimientoInformado(Connection connection, HashMap consentimientoInformado)
	{
		return SqlBaseConsentimientoInformadoDao.consultaConsentimientoInformado(connection, consentimientoInformado);
	}
	
	/**
	 * Insertar un registro de detalle consentimiento informado
	 * @param Connection   connection
	 * @param HashMap 	detalleConsentimientoInformado
	 * */
	
	public  boolean insertarDetalleConsentimientoInformado (Connection connection, HashMap detalleConsentimientoInformado)
	{
		return SqlBaseConsentimientoInformadoDao.insertarDetalleConsentimientoInformado(connection, detalleConsentimientoInformado);
	}
	
	/**
	 * Elimina detalle consentimiento informado registrado
	 * @param Connection   connection
	 * @param HashMap 	consentimientoInformado
	 * */
	
	public boolean eliminardetalleConsentimientoInformado(Connection connection, HashMap detalleConsentimientoInformado)
	{
		return SqlBaseConsentimientoInformadoDao.eliminardetalleConsentimientoInformado(connection, detalleConsentimientoInformado);
	}
	
	/**
	 * Consulta basica de detalle consentimiento informado por keys
	 * @param Connection connection
	 * @param HashMap 	detalleConsentimientoInformado
	 * */
	
	public HashMap consultaDetalleConsentimientoInformado(Connection connection, HashMap detalleConsentimientoInformado)
	{
		return SqlBaseConsentimientoInformadoDao.consultaDetalleConsentimientoInformado(connection, detalleConsentimientoInformado);
	}
	
	
	/**
	 * Modifica el detalle consentimiento informado registrado
	 * @param Connection   connection
	 * @param HashMap 	detalleConsentimientoInformado
	 * */
	public boolean modificarDetalleConsentimientoInf(Connection connection, HashMap detalleConsentimientoInformado)
	{
		return SqlBaseConsentimientoInformadoDao.modificarDetalleConsentimientoInf(connection, detalleConsentimientoInformado);
	}
	
	
	/**
	 * Consulta los consentimientos informados de un grupo de servicios a partir del servicio asociado 
	 * @param Connection connection 
	 * @param HashMap parametros
	 * */
	public HashMap buscarConsentimientoInfServicio(Connection connection, HashMap parametros)
	{
		return SqlBaseConsentimientoInformadoDao.buscarConsentimientoInfServicio(connection, parametros);
	}
	
	
	/**
	 * Inserta el registro que indica que sea imprimido un consentimiento informado
	 * @param Connection connection 
	 * @param HashMap parametros
	 * */
	public boolean insertarHistorialConsentimientoInf(Connection connection, HashMap parametros)
	{
		return SqlBaseConsentimientoInformadoDao.insertarHistorialConsentimientoInf(connection, parametros);
	}
	
	
	/**
	 * Consulta que devuelve el servicio, grupo y nemobre del servicio.
	 * @param Connection connection
	 * @param HashMap 	consentimientoInformado
	 * */
	
	public  HashMap consultaConsentimientoInformadoXingreso(Connection connection, HashMap consentimientoInformado)
	{
		return SqlBaseConsentimientoInformadoDao.consultaConsentimientoInformadoXingreso(connection, consentimientoInformado);
	}
	
	/**
	 * Consulta de los formatos del servicio
	 * @param connection
	 * @param parametros
	 * @return
	 */
	public HashMap impresionCosentimientoinformadoXIngreso (Connection connection,HashMap parametros)
	{
		return SqlBaseConsentimientoInformadoDao.impresionCosentimientoinformadoXIngreso(connection, parametros);
	}



	@Override
	public boolean programaTieneConsentimientosInformados(int codigoPrograma) 
	{
		return SqlBaseConsentimientoInformadoDao.programaTieneConsentimientosInformados(codigoPrograma);
	}



	@Override
	public boolean programaHallazgoPiezaTienConsentimientoInfo(int codigophp) 
	{
		return SqlBaseConsentimientoInformadoDao.programaHallazgoPiezaTienConsentimientoInfo(codigophp);
	}



	@Override
	public ArrayList<DtoConsentimientoInformadoOdonto> consultarInfoConsentimientoOdonto(String codigosPHP,String planTratamiento, boolean todos) 
	{
		return SqlBaseConsentimientoInformadoDao.consultarInfoConsentimientoOdonto(codigosPHP,planTratamiento,todos);
	}



	@Override
	public boolean guardarConsentimientoOdontologia(Connection con,ArrayList<DtoConsentimientoInformadoOdonto> consentimientoOdonto, String codigoCita) 
	{
		return SqlBaseConsentimientoInformadoDao.guardarConsentimientoOdontologia(con,consentimientoOdonto,codigoCita);
	}



	@Override
	public ArrayList<Integer> obtenerProgramasHallazgoPiezasParaConsentimiento(
			Connection con, int planTratamiento, int codigoPrograma, String codigoCita, boolean validaPresupuestoContratado) {
		return SqlBaseConsentimientoInformadoDao.obtenerProgramasHallazgoPiezasParaConsentimiento(con,planTratamiento,codigoPrograma,codigoCita,validaPresupuestoContratado);

	}

}