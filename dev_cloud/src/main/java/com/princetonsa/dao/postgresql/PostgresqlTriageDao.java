/*
 * Created on Feb 8, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.TriageDao;
import com.princetonsa.dao.sqlbase.SqlBaseTriageDao;
import com.princetonsa.dto.historiaClinica.DtoClasificacionesTriage;
import com.princetonsa.dto.historiaClinica.DtoTriage;

/**
 * @author sebastián gómez rivillas
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PostgresqlTriageDao implements TriageDao {
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(PostgresqlTriageDao.class);
	/**
	 * Para insertar el triage sin los signos vitales
	 */
	private String insertarTriageinicioStr="";
	
	/* (non-Javadoc)
	 * @see com.princetonsa.dao.TriageDao#insertarSignosVitalesTriage(java.sql.Connection, int, int, java.lang.String, int)
	 */
	public int insertarSignosVitalesTriage(Connection con, int codigo,
			String consecutivo, String valor,String estado) 
	{
		return SqlBaseTriageDao.insertarSignosVitalesTriage(con,codigo,consecutivo,valor,estado);
	}

	
	/* (non-Javadoc)
	 * @see com.princetonsa.dao.TriageDao#insertarTriage(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, int, int, int, java.lang.String)
	 */
	public int insertarTriage(Connection con,HashMap datosTriage,String estado) 
	{
		return SqlBaseTriageDao.insertarTriage(con,datosTriage,estado);
	}



	/* (non-Javadoc)
	 * @see com.princetonsa.dao.TriageDao#obtenerConsecutivo(java.sql.Connection)
	 */
	public int obtenerConsecutivo(Connection con) {
		// TODO Auto-generated method stub
		return SqlBaseTriageDao.obtenerConsecutivo(con);
	}



	/* (non-Javadoc)
	 * @see com.princetonsa.dao.TriageDao#resumenTriage(java.sql.Connection, int)
	 */
	public Collection resumenTriage(Connection con, String consecutivo) {
		// TODO Auto-generated method stub
		return SqlBaseTriageDao.resumenTriage(con,consecutivo);
	}



	/* (non-Javadoc)
	 * @see com.princetonsa.dao.TriageDao#cargarTriage(java.sql.Connection, int, java.lang.String)
	 */
	public Collection cargarTriage(Connection con, String consecutivo, String consecutivo_fecha) {
		
		return SqlBaseTriageDao.cargarTriage(con,consecutivo,consecutivo_fecha);
	}



	/* (non-Javadoc)
	 * @see com.princetonsa.dao.TriageDao#actualizarTriage(java.sql.Connection, int, java.lang.String, java.lang.String)
	 */
	public int actualizarTriage(Connection con, String consecutivo, String consecutivo_fecha, String observacion,String noRespondioLLamado) {
		// TODO Auto-generated method stub
		return SqlBaseTriageDao.actualizarTriage(con,consecutivo,consecutivo_fecha,observacion, noRespondioLLamado);
	}



	/* (non-Javadoc)
	 * @see com.princetonsa.dao.TriageDao#reporteTriage(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public Collection reporteTriage(Connection con, String fechainicial, String fechafinal, String usuario, String responsable,int centroAtencion) {
		// TODO Auto-generated method stub
		return SqlBaseTriageDao.reporteTriage(con,fechainicial,fechafinal,usuario,responsable,centroAtencion);
	}


	/**
	 * Metodo para buscar un triage por medio de busqueda avanzada
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param codigoCalificacion
	 * @param codigoDestino
	 * @param admision
	 * @param codigoSala
	 * @param centroAtencion
	 * @return
	 */
	public Collection buscarTriage(Connection con, String fechaInicial, String fechaFinal, int codigoCalificacion, int codigoDestino, String admision, int codigoSala, int centroAtencion )
	{
		return SqlBaseTriageDao.buscarTriage(con, fechaInicial, fechaFinal, codigoCalificacion, codigoDestino, admision, codigoSala, centroAtencion);
	}
	
	/**
	 * Método implementado para actualizar el estado del paciente que estuvo
	 * registrado para Triage cambiando su estado a atendido e ingresando su consecutivo triage
	 * respectivo
	 * @param con
	 * @param codigo
	 * @param consecutivoTriage
	 * @return
	 */
	public int actualizarPacienteParaTriage(Connection con,String codigo,String consecutivoTriage)
	{
		return SqlBaseTriageDao.actualizarPacienteParaTriage(con,codigo,consecutivoTriage);
	}
	
	/**
	 * Método implementado para cargar los signos vitales del Triage
	 * @param con
	 * @param consecutivo
	 * @param consecutivo_fecha
	 * @return
	 */
	public HashMap cargarSignosVitalesTriage(Connection con,String consecutivo,String consecutivo_fecha)
	{
		return SqlBaseTriageDao.cargarSignosVitalesTriage(con,consecutivo,consecutivo_fecha);
	}
	
	/**
	 * Método que consulta los datos del triage
	 * @param con
	 * @param campos
	 * @return
	 */
	public DtoTriage obtenerDatosTriage(Connection con,HashMap campos)
	{
		return SqlBaseTriageDao.obtenerDatosTriage(con, campos);
	}

	@Override
	public DtoTriage consultarInfoResumenTriagePorCuenta(int codigoCuenta) {
		return SqlBaseTriageDao.consultarInfoResumenTriagePorCuenta(codigoCuenta);
	}
	


	@Override
	public ArrayList<DtoClasificacionesTriage> consultarClasificacionesTriage() 
	{
		return SqlBaseTriageDao.consultarClasificacionesTriage();
	}
	

	/**
	 * 
	 */
	public Collection buscarTriage(Connection con, String tipoID,String numeroID )
	{
		return SqlBaseTriageDao.buscarTriage(con, tipoID,numeroID);
	}
	
}
