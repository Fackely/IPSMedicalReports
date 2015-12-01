/*
 * Junio 3, 2008
 */
package com.princetonsa.dao.postgresql.ordenesmedicas;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.InfoDatosInt;
import util.ValoresPorDefecto;

import com.princetonsa.dao.ordenesmedicas.UtilidadesOrdenesMedicasDao;
import com.princetonsa.dao.sqlbase.ordenesmedicas.SqlBaseUtilidadesOrdenesMedicasDao;

/**
 * 
 * @author sgomez
 * Clase que maneja los m�todos prop�os de Postgres para el acceso a la fuente
 * de datos en las utilidades del m�dulo de ORDENES
 */
public class PostgresqlUtilidadesOrdenesMedicasDao implements
		UtilidadesOrdenesMedicasDao 
{
	/**
	 * M�todo para obtener la especialidad solicitada de una interconsulta
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public InfoDatosInt obtenerEspecialidadSolicitadaInterconsulta(Connection con,String numeroSolicitud)
	{
		return SqlBaseUtilidadesOrdenesMedicasDao.obtenerEspecialidadSolicitadaInterconsulta(con, numeroSolicitud);
	}

	
	/**
	 * M�todo para obtener la fecha valoracion inicial de la solicitud
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public String consultarFechaValoracionInicial(Connection con, int codigoCuenta) 
	{
		String consultarFechaValoracion = "SELECT val.fecha_valoracion as fecha_valoracion from valoraciones val INNER JOIN solicitudes sol " +
		"ON(sol.numero_solicitud=val.numero_solicitud) " +
		"where " +
		"sol.cuenta=? " +
		"order by val.numero_solicitud "+ValoresPorDefecto.getValorLimit1()+" 1";

		return SqlBaseUtilidadesOrdenesMedicasDao.consultarFechaValoracionInicial(con, codigoCuenta,consultarFechaValoracion);
	}
	
	/**
	 * M�todo para obtener la fecha / hora de la solicitud
	 * [0] fecha
	 * [1] hora
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 * 
	 */
	public String[] obtenerFechaHoraSolicitud(Connection con,String numeroSolicitud)
	{
		return SqlBaseUtilidadesOrdenesMedicasDao.obtenerFechaHoraSolicitud(con, numeroSolicitud);
	}
	
	
	/**
	 * M�todo para obtener el servicio de la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 * 
	 */
	public int obtenerServicioSolicitud(Connection con, String numeroSolicitud) {
		
		return SqlBaseUtilidadesOrdenesMedicasDao.obtenerServicioSolicitud(con, numeroSolicitud);
	}
	
	/**
	 * M�todo para obtener la subcuenta de una solicitud de cirugia
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String obtenerSubCuentaSolicitudCirugia(Connection con,String numeroSolicitud)
	{
		return SqlBaseUtilidadesOrdenesMedicasDao.obtenerSubCuentaSolicitudCirugia(con, numeroSolicitud);
	}
	
	/**
	 * M�todo que verifica si se puede abrir la seccion de prescripci�n di�lisis
	 * validando por codigo de institucion y codigo de centro de costo
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean deboAbrirPrescripcionDialisis(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesOrdenesMedicasDao.deboAbrirPrescripcionDialisis(con, campos);
	}
	
	/**
	 * M�todo para obtener el centro de costos solicitante / solicitado servicio de la solicitud
	 * @param con
	 * @param parametros
	 * @return 
	 */
	public InfoDatosInt obtenerCentroCostoSoli(Connection con,HashMap parametros)
	{
		return SqlBaseUtilidadesOrdenesMedicasDao.obtenerCentroCostoSoli(con,parametros);
	}
	
	/**
	 * M�todo para consultar el codigo pk de solicitud de cirugia por servicio
	 * @param Connection con
	 * @param HashMap parametros
	 * @return
	 */
	public HashMap obtenerSolCirugiaServicio(Connection con,HashMap parametros)
	{
		return SqlBaseUtilidadesOrdenesMedicasDao.obtenerSolCirugiaServicio(con, parametros);
	}
	
	/**
	 * M�todo para obtener el codigo del m�dico que responde la solicitud
	 * @param con
	 * @param campos
	 * @return
	 */
	public int obtenerCodigoMedicoRespondeSolicitud(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesOrdenesMedicasDao.obtenerCodigoMedicoRespondeSolicitud(con, campos);
	}
	
	/**
	 * M�todo para cargar las opciones de manejo de la interconsulta
	 * 
	 * @param con
	 * @param filtroOpciones: Si se desea filtrar por unas opciones espec�ficas entonces se mandan los codigos separados por comas
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerOpcionesManejoInterconsulta(Connection con,String filtroOpciones)
	{
		return SqlBaseUtilidadesOrdenesMedicasDao.obtenerOpcionesManejoInterconsulta(con, filtroOpciones);
	}
	
	/**
	 * M�todo para verificar si existen solicitudes de medicamentos pendientes para despachar
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public boolean existenSolicitudesMedicamentosPendientesDespachar(Connection con,int idCuenta)
	{
		return SqlBaseUtilidadesOrdenesMedicasDao.existenSolicitudesMedicamentosPendientesDespachar(con, idCuenta);
	}
	
		
	/**
	 * M�todo que devuelve el centro de costo que ejecuta parametrizado en la funcionalidad
	 * de "Excepciones Centros de Costo Interconsultas" si este existe
	 * @param con
	 * @param centroCostoSolicita
	 * @param servicio
	 * @param medico
	 * @return
	 */
	public int obtenerCCEjecutaXExcepCCInter(Connection con, String centroCosto, String servicio, String medico)
	{
		return SqlBaseUtilidadesOrdenesMedicasDao.obtenerCCEjecutaXExcepCCInter(con, centroCosto, servicio, medico);
	}
	
	
	/**
	 * 
	 * @param servicio
	 * @param institucion
	 * @param centroAtencion
	 * @param centroCostoSolicita
	 * @return
	 */
	public ArrayList<InfoDatosInt> obtenerCentrosCostoEjecuta(int servicio, int institucion, int centroAtencion, int centroCostoSolicita)
	{
		return SqlBaseUtilidadesOrdenesMedicasDao.obtenerCentrosCostoEjecuta(servicio, institucion, centroAtencion, centroCostoSolicita);
	}
	
	/**
	 * 
	 * @param institucion
	 * @param centroAtencion
	 * @param centroCostoEjecuta
	 * @param centroCostoSolicita
	 * @param servicio
	 * @return
	 */
	public ArrayList<InfoDatosInt> obtenerProfesionalesEjecutan(int institucion, int centroAtencion, int centroCostoEjecuta, int centroCostoSolicita, int servicio)
	{
		return SqlBaseUtilidadesOrdenesMedicasDao.obtenerProfesionalesEjecutan(institucion, centroAtencion, centroCostoEjecuta, centroCostoSolicita, servicio);
	}


	@Override
	public HashMap obtenerConvenioEstadoSolicitud(int numSolicitud) {
		
		return SqlBaseUtilidadesOrdenesMedicasDao.obtenerConvenioEstadoSolicitud(numSolicitud);
	}
	
	/**
	 * verifica si el usuario tiene registro con la entidad subcontratadas 
	 * @param HashMap parametros
	 * */
	public boolean tieneUsuarioEntidadSubcontratada(HashMap parametros)
	{
		return SqlBaseUtilidadesOrdenesMedicasDao.tieneUsuarioEntidadSubcontratada(parametros);
	}
	
	/**
	 * Obtiene las especialidades de un profesional
	 * @param codigoMedico
	 * @return ArrayList<InfoDatosInt>
	 */
	public ArrayList<InfoDatosInt> obtenerEspecialidadProfesionalEjecutan(int codigoMedico)
	{
		return	SqlBaseUtilidadesOrdenesMedicasDao.obtenerEspecialidadProfesionalEjecutan(codigoMedico);
	}
	
	/**
	 * M�todo para obtener el ingreso de una solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String obtenerIngresoXNumeroSolicitud(Connection con,String numeroSolicitud)
	{
		return SqlBaseUtilidadesOrdenesMedicasDao.obtenerIngresoXNumeroSolicitud(con, numeroSolicitud);
	}
}