/*
 * Junio 3, 2008
 */
package com.princetonsa.dao.ordenesmedicas;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.InfoDatosInt;

/**
 * 
 * @author sgomez
 *	Interface usada para gestionar los métodos de acceso a la base
 * de datos en utilidades del modulo de ORDENES
 */
public interface UtilidadesOrdenesMedicasDao 
{
	/**
	 * Método para obtener la especialidad solicitada de una interconsulta
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public InfoDatosInt obtenerEspecialidadSolicitadaInterconsulta(Connection con,String numeroSolicitud);

	/**
	 * Método para obtener la fecha valoracion inicial de la solicitud
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public String consultarFechaValoracionInicial(Connection con, int codigoCuenta);
	
	/**
	 * Método para obtener la fecha / hora de la solicitud
	 * [0] fecha
	 * [1] hora
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 * 
	 */
	public String[] obtenerFechaHoraSolicitud(Connection con,String numeroSolicitud);

	/**
	 * Metodo para obtener el servicio de la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public int obtenerServicioSolicitud(Connection con, String numeroSolicitud);
	
	/**
	 * Método para obtener la subcuenta de una solicitud de cirugia
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String obtenerSubCuentaSolicitudCirugia(Connection con,String numeroSolicitud);
	
	/**
	 * Método que verifica si se puede abrir la seccion de prescripción diálisis
	 * validando por codigo de institucion y codigo de centro de costo
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean deboAbrirPrescripcionDialisis(Connection con,HashMap campos);
	
	
	/**
	 * Método para obtener el centro de costos solicitante / solicitado servicio de la solicitud
	 * @param con
	 * @param parametros
	 * @return 
	 */
	public InfoDatosInt obtenerCentroCostoSoli(Connection con,HashMap parametros);	
	
	/**
	 * Método para consultar el codigo pk de solicitud de cirugia por servicio
	 * @param Connection con
	 * @param String numeroSolicitud
	 * @param String codigoServicio
	 * @return
	 */
	public HashMap obtenerSolCirugiaServicio(Connection con,HashMap parametros);
	
	/**
	 * Método para obtener el codigo del médico que responde la solicitud
	 * @param con
	 * @param campos
	 * @return
	 */
	public int obtenerCodigoMedicoRespondeSolicitud(Connection con,HashMap campos);
	
	/**
	 * Método para cargar las opciones de manejo de la interconsulta
	 * 
	 * @param con
	 * @param filtroOpciones: Si se desea filtrar por unas opciones específicas entonces se mandan los codigos separados por comas
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerOpcionesManejoInterconsulta(Connection con,String filtroOpciones);
	
	/**
	 * Método para verificar si existen solicitudes de medicamentos pendientes para despachar
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public boolean existenSolicitudesMedicamentosPendientesDespachar(Connection con,int idCuenta);
	
	/**
	 * Método que devuelve el centro de costo que ejecuta parametrizado en la funcionalidad
	 * de "Excepciones Centros de Costo Interconsultas" si este existe
	 * @param con
	 * @param centroCostoSolicita
	 * @param servicio
	 * @param medico
	 * @return
	 */
	public int obtenerCCEjecutaXExcepCCInter(Connection con, String centroCosto, String servicio, String medico);
	
	/**
	 * 
	 * @param servicio
	 * @param institucion
	 * @param centroAtencion
	 * @param centroCostoSolicita
	 * @return
	 */
	public ArrayList<InfoDatosInt> obtenerCentrosCostoEjecuta(int servicio, int institucion, int centroAtencion, int centroCostoSolicita);
	
	/**
	 * 
	 * @param institucion
	 * @param centroAtencion
	 * @param centroCostoEjecuta
	 * @param centroCostoSolicita
	 * @param servicio
	 * @return
	 */
	public ArrayList<InfoDatosInt> obtenerProfesionalesEjecutan(int institucion, int centroAtencion, int centroCostoEjecuta, int centroCostoSolicita, int servicio);

	/**
	 * Metodo para obtener los convenios y su estado asociado a un numero de solicitud 
	 * @param numSolicitud
	 * @return
	 */
	public HashMap obtenerConvenioEstadoSolicitud(int numSolicitud);
	
	/**
	 * verifica si el usuario tiene registro con la entidad subcontratadas 
	 * @param String codigoEntidadSub
	 * @param String usuario
	 * */
	public boolean tieneUsuarioEntidadSubcontratada(HashMap parametros);
	
	/**
	 * Obtiene las especialidades de un profesional
	 * @param codigoMedico
	 * @return ArrayList<InfoDatosInt>
	 */
	public ArrayList<InfoDatosInt> obtenerEspecialidadProfesionalEjecutan(int codigoMedico);
	
	/**
	 * Método para obtener el ingreso de una solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String obtenerIngresoXNumeroSolicitud(Connection con,String numeroSolicitud);
}