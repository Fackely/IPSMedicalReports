/*
 * Abr 29, 2008
 */
package com.princetonsa.dao.postgresql.historiaClinica;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import util.InfoDatosInt;
import util.InfoDatosString;
import util.ResultadoBoolean;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.ValoracionesDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseValoracionesDao;
import com.princetonsa.dto.historiaClinica.DtoValoracion;
import com.princetonsa.dto.historiaClinica.DtoValoracionHospitalizacion;
import com.princetonsa.dto.historiaClinica.DtoValoracionObservaciones;
import com.princetonsa.dto.historiaClinica.DtoValoracionUrgencias;

/**
 * @author Sebastián Gómez 
 *
 * Clase que maneja los métodos propìos de Postgres para el acceso a la fuente
 * de datos en la funcionalidad VALORACIONES
 */
public class PostgresqlValoracionesDao implements ValoracionesDao 
{
	/**
	 * Método que realiza la inserción de una valoración base
	 * @param con
	 * @param valoracionBase
	 * @return
	 */
	public ResultadoBoolean insertarBase(Connection con,DtoValoracion valoracionBase, String observacionCapitacion)
	{
		return SqlBaseValoracionesDao.insertarBase(con, valoracionBase, observacionCapitacion);
	}
	
	/**
	 * Método implementado para insertar una valoración de urgencias
	 * @param con
	 * @param valoracionUrgencias
	 * @return
	 */
	public ResultadoBoolean insertarUrgencias(Connection con,DtoValoracionUrgencias valoracionUrgencias)
	{

		return SqlBaseValoracionesDao.insertarUrgencias(con, valoracionUrgencias);
	}
	
	/**
	 * Método que inserta una nueva valoración
	 * @param con
	 * @param valoracionHospitalizacion
	 * @return
	 */
	public ResultadoBoolean insertarHospitalizacion(Connection con,DtoValoracionHospitalizacion valoracionHospitalizacion)
	{
		return SqlBaseValoracionesDao.insertarHospitalizacion(con, valoracionHospitalizacion);
	}
	
	/**
	 * Método para cargar la valoracion de hospitalizacion
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public DtoValoracionHospitalizacion cargarHospitalizacion(Connection con,String numeroSolicitud)
	{
		return SqlBaseValoracionesDao.cargarHospitalizacion(con, numeroSolicitud);
	}
	
	/**
	 * Método para cargar la valoración de urgencias
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public DtoValoracionUrgencias cargarUrgencias(Connection con,String numeroSolicitud)
	{
		return SqlBaseValoracionesDao.cargarUrgencias(con, numeroSolicitud);
	}
	public DtoValoracionUrgencias cargarDiagnosticosValoracion(Connection con,String numeroSolicitud)
	{
		return SqlBaseValoracionesDao.cargarDiagnosticosValoracion(con, numeroSolicitud);
	}
	
	/**
	 * Método implementado para cargar la valoracion base
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public DtoValoracion cargarBase(Connection con,String numeroSolicitud)
	{
		return SqlBaseValoracionesDao.cargarBase(con, numeroSolicitud, null);
	}
	
	/**
	 * Método para consultar la información triage
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public InfoDatosString consultarInformacionTriageUrgencias(Connection con,String idCuenta)
	{
		return SqlBaseValoracionesDao.consultarInformacionTriageUrgencias(con, idCuenta);
	}
	
	/**
	 * Método implementado para insertar la primera versión de epicrisis
	 * @param con
	 * @param codigoIngreso
	 * @param codigoProfesional
	 * @param isUrgencias
	 * @return
	 */
	public int insertarPrimeraVersionEpicrisis(Connection con, String codigoIngreso,int codigoProfesional,boolean isUrgencias)
	{
		return SqlBaseValoracionesDao.insertarPrimeraVersionEpicrisis(con, codigoIngreso, codigoProfesional, isUrgencias);
	}
	
	/**
	 * Método implementado para modificar la valroación de urgencias
	 * @param con
	 * @param valoracionUrgencias
	 * @return
	 */
	public ResultadoBoolean modificarUrgencias(Connection con,DtoValoracionUrgencias valoracionUrgencias)
	{
		return SqlBaseValoracionesDao.modificarUrgencias(con, valoracionUrgencias);
	}
	
	/**
	 * Método implementado para insertar observaciones
	 * @param con
	 * @param observaciones
	 * @param numeroSolicitud
	 * @param fechaActual
	 * @param horaActual
	 * @return
	 */
	public ResultadoBoolean insertarObservaciones(Connection con, ArrayList<DtoValoracionObservaciones> observaciones, String numeroSolicitud, String fechaActual, String horaActual)
	{
		return SqlBaseValoracionesDao.insertarObservaciones(con, observaciones, numeroSolicitud, fechaActual, horaActual);
	}
	
	/**
	 * Método implementado para cargar los históricos de valoraciones y/o evoluciones en caso de asocio
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public HashMap<String, Object> cargarHistorico(Connection con,String idIngreso)
	{
		return SqlBaseValoracionesDao.cargarHistorico(con, idIngreso);
	}
	
	/**
	 * Método implementado para actualizar la valoracion en el registro cuidado especial
	 * @param con
	 * @param codigoIngreso
	 * @param numeroSolicitud
	 * @param loginUsuario
	 * @return
	 */
	public ResultadoBoolean actualizarValoracionRegistroCuidadoEspecial(Connection con,String codigoIngreso,String numeroSolicitud, String loginUsuario, boolean obligatorio)
	{
		return SqlBaseValoracionesDao.actualizarValoracionRegistroCuidadoEspecial(con, codigoIngreso, numeroSolicitud, loginUsuario, obligatorio);
	}
	
	/**
	 * Método para obtener la causa externa de la primera valoración
	 * @param con
	 * @param idCuenta
	 * @param seccionOrderBy
	 * @return
	 */
	public InfoDatosInt obtenerCausaExternaValoracion(Connection con,String idCuenta)
	{
		String seccionOrderBy = " ORDER BY s.numero_solicitud "+ValoresPorDefecto.getValorLimit1()+" 1";
		return SqlBaseValoracionesDao.obtenerCausaExternaValoracion(con, idCuenta, seccionOrderBy);
	}
	
	/**
	 * Método para obtener la finalidad de la consulta de la valoracion
	 * @param con
	 * @param idCuenta
	 * @param tipoBD
	 * @return
	 */
	public InfoDatosString obtenerFinalidadConsultaValoracion(Connection con,String idCuenta)
	{
		return SqlBaseValoracionesDao.obtenerFinalidadConsultaValoracion(con, idCuenta, DaoFactory.POSTGRESQL);
	}
	
	/**
	 * Método implementado para obtener la fecha/hora grabacion de la valoración de urgencias
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public String[] obtenerFechahoraGrabacionValoracionUrgencias(Connection con,String idCuenta)
	{
		return SqlBaseValoracionesDao.obtenerFechahoraGrabacionValoracionUrgencias(con, idCuenta);
	}

	@Override
	public void ponerPacienteEnEvaloracion(Connection con, int paciente, boolean estaEnValoracion, String usuario, String sessionId) {
		SqlBaseValoracionesDao.ponerPacienteEnEvaloracion(con, paciente, estaEnValoracion, usuario, sessionId);
	}

	@Override
	public int cancelarPacientesEnEvaloracion(Connection con) {
		return SqlBaseValoracionesDao.cancelarPacientesEnEvaloracion(con);
	}
	
	/**
	 * 
	 */
	public boolean actualizarPoolSolicitudXAgenda(Connection con, int solicitud, int pool)
	{
		return SqlBaseValoracionesDao.actualizarPoolSolicitudXAgenda(con, solicitud, pool);
	}


	
	/**
	 * @see com.princetonsa.dao.historiaClinica.ValoracionesDao#tieneValoraciones(java.sql.Connection, java.lang.String)
	 */
	public  Boolean tieneValoraciones(Connection con,String numeroSolcitud){
		return SqlBaseValoracionesDao.tieneValoraciones(con, numeroSolcitud);
	}

	/**
	 * Método para cargar las observaciones
	 * @param con
	 * @param numeroSolicitud
	 */
	@Override
	public List<DtoValoracionObservaciones> cargarObservaciones(Connection con,
			String numeroSolicitud) {
		return SqlBaseValoracionesDao.cargarObservaciones(con, numeroSolicitud);
	}
	
	/**
	 * @see com.princetonsa.dao.historiaClinica.ValoracionesDao#esSolicitudDeCuidadosEspeciales(Connection con, int numeroCuenta)
	 * @param con
	 * @param numeroCuenta
	 * @return boolean
	 */
	@Override
	public boolean esSolicitudDeCuidadosEspeciales(Connection con,int idSolicitud) {		
		return SqlBaseValoracionesDao.esSolicitudDeCuidadosEspeciales(con, idSolicitud);
	}	
	
	/**
	 * Alberto Ovalle
	 * mt5749
	 * metodo para obtener las observaciones
	 * @param con
	 * @param numeroSolicitud
	 * @return List
	 */
	@Override
	public List<DtoValoracion> cargarObservacionesVistaValoracion(Connection con,String numeroSolicitud) {
		
		return SqlBaseValoracionesDao.cargarObservacionesVistaValoracion(con,numeroSolicitud);
	}
	
	/**
	 * Alberto Ovalle
	 * mt5749
	 * metodo para obtener la cadena Registro ConductaValoracion
	 * @param con
	 * @param numeroSolicitud
	 * @return String
	 */
	public String selecionarCadenaRegistroConductaValoracion(Connection con,String numeroSolicitud) {
		
		return SqlBaseValoracionesDao.selecionarCadenaRegistroConductaValoracion(con,numeroSolicitud);
	}
	
	/**
	 * Alberto Ovalle
	 * mt5749
	 * metodo para modificar las obsevaciones de urgencias
	 * @param con
	 * @param dtoValoracionObservaciones
	 * @param dtoValoracionUrgencias
	 * @param fechaActual
	 * @param horaActual
	 */
	public void modificarObservaciones(Connection con,DtoValoracionObservaciones dtoValoracionObservaciones,DtoValoracionUrgencias dtoValoracionUrgencias,String fechaActual,String horaActual)
	{
		SqlBaseValoracionesDao.modificarObservaciones(con,dtoValoracionObservaciones,dtoValoracionUrgencias,fechaActual,horaActual);
	}
	
	/**
	 * Alberto Ovalle
	 * mt5749
	 * metodo para modificar las obsevaciones de hospitalizacion
	 * @param con
	 * @param dtoValoracionObservaciones
	 * @param dtoValoracion
	 * @param fechaActual
	 * @param horaActual
	 */
	
	public void modificarObsevacionesHospitalizacion(Connection con,DtoValoracionObservaciones dtoValoracionObservaciones,DtoValoracionHospitalizacion dtoValoracionHospitalizacion,String fechaActual,String horaActual)
	{
		SqlBaseValoracionesDao.modificarObsevacionesHospitalizacion(con,dtoValoracionObservaciones,dtoValoracionHospitalizacion,fechaActual,horaActual);
	}
	
}
