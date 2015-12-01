/*
 * Created on 15/06/2004
 *
 * Juand David Ramírez
 * Princeton S.A.
 */
package com.princetonsa.mundo.resumenAtenciones;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.ResumenAtencionesDao;
import com.princetonsa.dto.historiaClinica.DtoEspecialidadesHC;
import com.princetonsa.dto.historiaClinica.DtoViasIngresoHC;
import com.princetonsa.mundo.CuentasPaciente;

/**
 * @author juanda
 * Objeto para el manejo del resumen de atenciones
 */
public class ResumenAtenciones
{
	Logger logger=Logger.getLogger(ResumenAtenciones.class);
	
	ResumenAtencionesDao resumentAtencionesDao;
	
	public ResumenAtenciones()
	{
		if(resumentAtencionesDao == null)
			resumentAtencionesDao = (ResumenAtencionesDao) DaoFactory.getDaoFactory(System.getProperty("TIPOBD") ).getResumenAtencionesDao();
	}
	
	public Collection busquedaCuentas(Connection con, int idPaciente)
	{
		try
		{
			return resumentAtencionesDao.busquedaCuentas(con, idPaciente);
		}
		catch (SQLException e)
		{
			logger.error("Error al obtener los ingresos del paciente (cuentas) " + e);
			return new ArrayList();			
		}
	}
	
	
	public Collection busquedaCuentasFiltro(Connection con, int idPaciente,String ano,String fechaIngresoInicial,
			String fechaIngresoFinal, String centroAtencion,String viaIngreso,String especialidad)
 {
		try {
			return resumentAtencionesDao.ingresosFiltroBusqueda(con,
					idPaciente, ano, fechaIngresoInicial, fechaIngresoFinal,
					centroAtencion, viaIngreso, especialidad);
		} catch (SQLException e) {
			logger.error("Error al obtener los ingresos del paciente (cuentas) "
					+ e);
			return new ArrayList();
		}
	}
		
	public Collection queAntecedentesExisten(Connection con, int codigoPaciente)
	{
		try
		{
			return resumentAtencionesDao.queAntecedentesExisten(con,codigoPaciente);
		}
		catch (SQLException e)
		{
			logger.error("Error al obtener los antecedentes existentes " + e);
			return null;			
		}
	}
	
	/**
	 * Implementación de la búsqueda de los datos de la admisión
	 * de urgencias para una cuenta dada en una BD Genérica
	 * 
	 * @see com.princetonsa.dao.ResumenAtencionesDao#busquedaDatosAdmisionUrgencias (Connection , int ) throws SQLException
	 */
	public Collection busquedaDatosAdmisionUrgencias(Connection con, int idCuenta)
	{
		try
		{
			return resumentAtencionesDao.busquedaDatosAdmisionUrgencias(con, idCuenta);
		}
		catch (SQLException e)
		{
			logger.error("Error obteniendo los datos de la admisión " + e);
			return null;
		}
	}
	
	/**
	 * Implementación de la revisión de si existe valoración inicial
	 * para una cuenta dada en una BD Genérica
	 * 
	 * @see com.princetonsa.dao.ResumenAtencionesDao#tieneValoracionInicial (Connection , int , boolean ) throws SQLException
	 */
	public boolean tieneValoracionInicial (Connection con, int idCuenta, boolean buscandoValoracionInicialUrgencias,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		try
		{
			return resumentAtencionesDao.tieneValoracionInicial(con, idCuenta, buscandoValoracionInicialUrgencias,fechaInicial,fechaFinal,horaInicial,horaFinal);
		}
		catch (SQLException e)
		{
			logger.error("Error verificando la existencia de valoración inicial " + e);
			return false;
		}
	}
	
	/**
	 * Implementación de la búsqueda de los datos de la admisión
	 * de hospitalización para una cuenta dada en una BD Genérica
	 * 
	 * @see com.princetonsa.dao.ResumenAtencionesDao#busquedaDatosAdmisionHospitalizacion (Connection , int ) throws SQLException
	 */
	public Collection busquedaDatosAdmisionHospitalizacion (Connection con, int idCuenta) 
	{
		try
		{
			return resumentAtencionesDao.busquedaDatosAdmisionHospitalizacion(con, idCuenta);
		}
		catch(SQLException e)
		{
			logger.error("Error en busquedaDatosAdmisionHospitalizacion de ResumenAtenciones :"+e);
			return new ArrayList();
		}
	}

	/**
	 * Implementación de la revisión de si existe una solicitud dado
	 * el tipo para una cuenta dada en una BD Genérica
	 * 
	 * @see com.princetonsa.dao.ResumenAtencionesDao#tieneSolicitudDe (Connection , int , int ) throws SQLException
	 */
	public boolean tieneSolicitudDe (
			Connection con, 
			int idCuenta,
			int codigoTipoSolicitudBuscada,
			String fechaInicial,
			String fechaFinal,
			String horaInicial,
			String horaFinal)
	{
		return resumentAtencionesDao.tieneSolicitudDe(con, idCuenta,codigoTipoSolicitudBuscada,fechaInicial,fechaFinal,horaInicial,horaFinal);
	}
	
	/**
	 * Implementación de la revisión de si existen evoluciones para 
	 * una cuenta dada en una BD Genérica
	 * 
	 * @see com.princetonsa.dao.ResumenAtencionesDao#tieneEvoluciones (Connection , int ) throws SQLException
	 */
	public boolean tieneEvoluciones (
			Connection con, 
			int idCuenta,
			String fechaInicial,
			String fechaFinal,
			String horaInicial,
			String horaFinal)
	{
		try
		{
			return  resumentAtencionesDao.tieneEvoluciones(con, idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal);
		}
		catch (SQLException e)
		{
			logger.error("Error verificando la existencia de evoluciones " + e);
			return false;
		}
	}

	public int numeroValoracionInicial (Connection con, int idCuenta)
	{
		try
		{
			return resumentAtencionesDao.numeroValoracionInicial(con, idCuenta);
		}
		catch (SQLException e)
		{
			logger.error("Error verificando el tipo de valoracion inicial " + e);
			return 0;
		}
	}
	
	public static int numeroValoracionInicialStatic(Connection con, int idCuenta)
	{
		try
		{
			return DaoFactory.getDaoFactory(System.getProperty("TIPOBD") ).getResumenAtencionesDao().numeroValoracionInicial(con, idCuenta);
		}
		catch (SQLException e)
		{
			return 0;
		}
	}
	
	
	/**
	 * Adición de Sebastián
	 * Método que consulta el diagnositco principal de semiEgreso en Urgencias
	 * para saber si es necesario crear el link "egresos" en el resumen de la Cuenta
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public int busquedaDiagnosticoPrincipal(Connection con,int idCuenta){
		return resumentAtencionesDao.busquedaDiagnosticoPrincipal(con,idCuenta);
	}
	/**
	 * Implementación de la búsqueda de la evolución que generó el egreso
	 * en una BD Postgresql
	 * 
	 * @see com.princetonsa.dao.ResumenAtencionesDao#codigoEvolucionGeneroEgreso (Connection , int ) throws SQLException
	 */
	public int codigoEvolucionGeneroEgreso (Connection con, int idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		try
		{
			return resumentAtencionesDao.codigoEvolucionGeneroEgreso (con, idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal) ;
		}
		catch (SQLException e)
		{
			logger.error("Error obteniendo el código de la evolución que tiene egreso " + e);
			return 0;
		}
	}
	
	public boolean existenCitas(Connection con, int idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		try
		{
			return resumentAtencionesDao.existenCitas(con, idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal);
		}
		catch (SQLException e)
		{
			logger.error("Error cargando las citas existentes " + e);
			return false;
		}
	}
	
	/**
	 * Método para insertar los documentos adjuntos de una cuenta en la 
	 * Historia de Atenciones(Resumen de Atenciones)
	 * @param con
	 * @param idCuenta
	 * @param nombreOriginal
	 * @param nombreArchivo
	 * @return
	 * @throws SQLException
	 */
	public int adjuntarDocumento(Connection con, int idPaciente, String nombreOrginal, String nombreArchivo) throws SQLException
	{
		return resumentAtencionesDao.adjuntarDocumento(con, idPaciente, nombreOrginal, nombreArchivo);
	}
	
	/**
	 * Método para saber si hay documentos adjuntos
	 * @param con
	 * @param idCuenta
	 * @return
	 * @throws SQLException
	 */
	public  int existenAdjuntos(Connection con, int idCuenta) throws SQLException
	{
		return resumentAtencionesDao.existenAdjuntos(con, idCuenta);
	}
	
	/**
	 * Métodod para consultar la fecha - hora de todas las notas de recuperacion
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarDocumentosAdjuntos (Connection con, int idCuenta) throws SQLException
	{
		return resumentAtencionesDao.consultarDocumentosAdjuntos(con, idCuenta);
	}

	/**
	 * Metodo para saber si hay antecedentes oftalmologicos personales y/o familiares. 
	 * @param con
	 * @param codigoPersona
	 * @return
	 * @throws SQLException
	 */
	public boolean[] existenAnteOftal(Connection con, int codigoPersona) throws SQLException
	{
		return resumentAtencionesDao.existenAnteOftal(con, codigoPersona);
	}
	
	/**
	 * Método implementado para verificar si a la cuenta se le ingresó
	 * registro de enfermería
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public boolean tieneRegistroEnfermeria(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		return resumentAtencionesDao.tieneRegistroEnfermeria(con,idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal);
	}
	
	/**
	 * Método implementado para listar las consultas PYP de la cuenta
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public HashMap listarConsultasPYP(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		return resumentAtencionesDao.listarConsultasPYP(con,idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal);
	}
	
	/**
	 * Método que verifica si el ingreso tiene registro de accidente de transito
	 * @param con
	 * @param idIngreso
	 * @param estado
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public boolean tieneRegistroAccidenteTransito(Connection con,String idIngreso,String estado,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		return resumentAtencionesDao.tieneRegistroAccidenteTransito(con,idIngreso,estado,fechaInicial,fechaFinal,horaInicial,horaFinal);
	}
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param acronimoEstadoFinalizado
	 * @param string
	 * @param string2
	 * @param string3
	 * @param string4
	 * @return
	 */
	public boolean tieneRegistroEventoCatastrofico(Connection con,String idIngreso,String estado,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		return resumentAtencionesDao.tieneRegistroEventoCatastrofico(con,idIngreso,estado,fechaInicial,fechaFinal,horaInicial,horaFinal);
	}
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public boolean tieneRegistroEventoAdverso(Connection con,String idIngreso)
	{
		return resumentAtencionesDao.tieneRegistroEventoAdverso(con,idIngreso);
	}
	
	/**
	 * Método que verifica si el paciente ha tenido registros de triage
	 * @param con
	 * @param tipoIdentificacion
	 * @param numeroIdentificacion
	 * @return
	 */
	public static boolean existeTriage(Connection con,String tipoIdentificacion,String numeroIdentificacion)
	{
		HashMap campos = new HashMap();
		campos.put("tipoIdentificacion",tipoIdentificacion);
		campos.put("numeroIdentificacion",numeroIdentificacion);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD") ).getResumenAtencionesDao().existeTriage(con, campos);
	}
	
	/**
	 * Método implementado para consultar los registros triage de un paciente
	 * @param con
	 * @param tipoIdentificacion
	 * @param numeroIdentificacion
	 * @return
	 */
	public static HashMap cargarListadoTriage(Connection con,String tipoIdentificacion,String numeroIdentificacion)
	{
		HashMap campos = new HashMap();
		campos.put("tipoIdentificacion",tipoIdentificacion);
		campos.put("numeroIdentificacion",numeroIdentificacion);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD") ).getResumenAtencionesDao().cargarListadoTriage(con, campos);
	}
	
	/**
	 * Método implementado para verificar si existen ordenes médicas
	 * @param con
	 * @param idIngreso
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public boolean existeOrdenesMedicas(Connection con,String idIngreso,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		return resumentAtencionesDao.existeOrdenesMedicas(con,idIngreso,idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal);
	}
	
	/**
	 * Método implementado para verificar si existe signos vitales
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public boolean existeSignosVitales(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		return resumentAtencionesDao.existeSignosVitales(con,idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal);
	}
	
	/**
	 * Método implementado para verificar si existe soporte respiratorio
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public boolean existeSoporteRespiratorio(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		return resumentAtencionesDao.existeSoporteRespiratorio(con,idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal);
	}
	
	/**
	 * Método que verifica si hay cateter y sonda
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public boolean existeCateterSonda(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		return resumentAtencionesDao.existeCateterSonda(con,idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal);
	}
	
	/**
	 * Método que verifica si existen cuidados especiales
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public boolean existeCuidadosEspeciales(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		return resumentAtencionesDao.existeCuidadosEspeciales(con,idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal);
	}
	
	/**
	 * Método que verifica si hay anotaciones de enfermeria
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public boolean existeAnotacionesEnfermeria(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		return resumentAtencionesDao.existeAnotacionesEnfermeria(con,idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal);
	}
	
	/**
	 * Método implementado para verificar si existe la hoja neurologica
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public boolean existeHojaNeurologica(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		return resumentAtencionesDao.existeHojaNeurologica(con,idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal);
	}
	
	/**
	 * Método que verifica si hay adminsitracion de medicamentos
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public boolean existeAdminMedicamentos(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		return resumentAtencionesDao.existeAdminMedicamentos(con,idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal);
	}
	
	/**
	 * Método que verifica si hay consumo de insumos
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public boolean existeConsumoInsumos(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		return resumentAtencionesDao.existeConsumoInsumos(con,idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal);
	}
	
	 /** Metodo que verifica si hay respuesta o interpertacion de interconsultas
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public boolean existeRespIntInterconsulta(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		return resumentAtencionesDao.existeRespIntInterconsulta(con,idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal);
	}
	
	/**
	 * Método que verifica si hay respuesta de procedimientos
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public boolean existeRespuestaProcedimiento(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		return resumentAtencionesDao.existeRespuestaProcedimiento(con,idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal);
	}
	
	/**
	 * Método que verifica si existe respuesta de cirugias
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public boolean existeRespuestaCirugias(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		return resumentAtencionesDao.existeRespuestaCirugias(con,idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal);
	}
	
	/**
	 * Método que verifica si existen escalas x ingresos
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean existenEscalasXIngreso(Connection con,String idIngreso,String fechaInicial,String horaInicial,String fechaFinal,String horaFinal)
	{
		HashMap campos = new HashMap();
		campos.put("idIngreso",idIngreso);
		campos.put("fechaInicial",fechaInicial);
		campos.put("horaInicial",horaInicial);
		campos.put("fechaFinal",fechaFinal);
		campos.put("horaFinal",horaFinal);
		return resumentAtencionesDao.existenEscalasXIngreso(con, campos);
	}
	
	/**
	 * Método implementado para saber si existen valoraciones de cuidados especiales
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean existenValoracionesCuidadoEspecial(Connection con,String idIngreso,String fechaInicial,String horaInicial,String fechaFinal,String horaFinal)
	{
		HashMap campos = new HashMap();
		campos.put("idIngreso",idIngreso);
		campos.put("fechaInicial",fechaInicial);
		campos.put("horaInicial",horaInicial);
		campos.put("fechaFinal",fechaFinal);
		campos.put("horaFinal",horaFinal);
		return resumentAtencionesDao.existenValoracionesCuidadoEspecial(con, campos);
	}
	
	/**
	 * Método implementado para cargar el listado de las valoraciones de cuidado especial
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String, Object> cargarValoracionesCuidadoEspecial(Connection con,String idIngreso,String fechaInicial,String horaInicial,String fechaFinal,String horaFinal)
	{
		HashMap campos = new HashMap();
		campos.put("idIngreso",idIngreso);
		campos.put("fechaInicial",fechaInicial);
		campos.put("horaInicial",horaInicial);
		campos.put("fechaFinal",fechaFinal);
		campos.put("horaFinal",horaFinal);
		return resumentAtencionesDao.cargarValoracionesCuidadoEspecial(con, campos);
	}
	/**
	 * Método implementado para listar las citas de un paciente 
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public HashMap listarCitas(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		return resumentAtencionesDao.listarCitas(con, idCuenta, fechaInicial, fechaFinal, horaInicial, horaFinal);
	}
	
	/**
	 * Método implementado para listar las solicitudes de citas de un paciente 
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public HashMap listarSolicitudesCitas(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		return resumentAtencionesDao.listarSolicitudesCitas(con, idCuenta, fechaInicial, fechaFinal, horaInicial, horaFinal);
	}
	
	/**
	 * Método que lista los servicios de la cita
	 * @param con
	 * @param codigoCita
	 * @return
	 */
	public HashMap listarServiciosCita(Connection con,String codigoCita)
	{
		return resumentAtencionesDao.listarServiciosCita(con, codigoCita);
	}
	
	/**
	 * Método que consulta los archivos adjuntos de las respuestas de procedimientos
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoRespuesta
	 * @return
	 */
	public static HashMap consultarAdjuntosProcedimientos(Connection con,String numeroSolicitud,String codigoRespuesta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getResumenAtencionesDao().consultarAdjuntosProcedimientos(con, numeroSolicitud, codigoRespuesta);
	}
	
	
	/**
	 * Consulta el historial Consentimiento Informado 
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static HashMap consultaHistorialConsentimientoInf(Connection con, HashMap parametros)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getResumenAtencionesDao().consultaHistorialConsentimientoInf(con, parametros);
	}
	


	//******************************************************************************************************************************
	//Anexo 550. Cargos Directos****************************************************************************************************
	//******************************************************************************************************************************
	

	/**
	 * Verifica si existe Solicitudes de tipo Cargo Directos
	 * @param Connection con
	 * @param int idCuenta
	 * @param int codigoTipoSolicitudBuscada
	 * @param String fechaInicial
	 * @param String fechaFinal
	 * @param String horaInicial
	 * @param String horaFinal
	 */
	public static boolean tieneSolicitudCargoDirectoDe (
			Connection con,
			int idCuenta,
			int codigoTipoSolicitudBuscada,
			String fechaInicial,
			String fechaFinal,
			String horaInicial,
			String horaFinal)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getResumenAtencionesDao().tieneSolicitudCargoDirectoDe(
				con,
				idCuenta,
				codigoTipoSolicitudBuscada,
				fechaInicial,
				fechaFinal,
				horaInicial,
				horaFinal);		
	}
	
	//******************************************************************************************************************************
	
	/**
	 * Verifica si existe Solicitudes de tipo Cargo Directos
	 * @param Connection con
	 * @param String codigoCuenta,
	 * @param String fechaInicial
	 * @param String fechaFinal
	 * @param String horaInicial
	 * @param String horaFinal
	 */
	public static HashMap consultarListadoSolCargoDirectoDe (
			Connection con, 
			String codigoCuenta,
			String fechaInicial,
			String fechaFinal,
			String horaInicial,
			String horaFinal)
	{
		HashMap parametros = new HashMap();
		
		parametros.put("idCuenta",codigoCuenta);
		parametros.put("fechaInicial",UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
		parametros.put("fechaFinal",UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
		parametros.put("horaFinal",horaFinal);
		parametros.put("horaInicial",horaInicial);
		
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getResumenAtencionesDao().consultarListadoSolCargoDirectoDe(con, parametros);
	}
	
	//******************************************************************************************************************************
	
	/**
	 * 
	 * @param Connection con
	 * @param HashMap solicitudesMap, devuelve un mapa de solicitudesMap del metodo consultarListadoSolCargoDirectoDe() 
	 * @param String tipoSolicitud
	 * @param String tipoServicio
	 * */
	public static HashMap armarMapaCargoDirectoDe(Connection con, HashMap solicitudesMap, String tipoSolicitud,String tipoServicio)
	{
		HashMap respuesta = new HashMap();
		HashMap temporal = new HashMap();
		int numRegistrosArticulo = 0;
		int numRegistrosConsulta = 0;
		int numRegistrosProcedimientos = 0;
		int numRegistrosServicios = 0;
		int numRegistrosCirugias = 0;
		
		respuesta.put("numRegistros","0");		
		
		for(int sol = 0; sol < Integer.parseInt(solicitudesMap.get("numRegistros").toString()); sol++)
		{
			//Tipo de Cargo Directo Articulos
			if(solicitudesMap.get("tipoSolicitud_"+sol).toString().equals(tipoSolicitud) && 
					tipoSolicitud.equals(ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+""))
			{
				respuesta.put("numeroSolicitud_"+numRegistrosArticulo,solicitudesMap.get("numeroSolicitud_"+sol));
				respuesta.put("fechaSolicitud_"+numRegistrosArticulo,solicitudesMap.get("fechaSolicitud_"+sol));
				respuesta.put("horaSolicitud_"+numRegistrosArticulo,solicitudesMap.get("horaSolicitud_"+sol));
				respuesta.put("tipoSolicitud_"+numRegistrosArticulo,solicitudesMap.get("tipoSolicitud_"+sol));
				respuesta.put("centroCostoSolicitud_"+numRegistrosArticulo,solicitudesMap.get("centroCostoSolicitud_"+sol));
				respuesta.put("descripcionCCostoSolicitante_"+numRegistrosArticulo,solicitudesMap.get("descCcSolicitante_"+sol));
				respuesta.put("especialidadSolicitante_"+numRegistrosArticulo,solicitudesMap.get("especialidadSolicitante_"+sol));
				respuesta.put("descripcionEspecialidadSolicitante_"+numRegistrosArticulo,solicitudesMap.get("descripcionEspecialidadSolicitante_"+sol));
				respuesta.put("centroCostoSolicitado_"+numRegistrosArticulo,solicitudesMap.get("centroCostoSolicitado_"+sol));
				respuesta.put("descripcionCCostoSolicitado_"+numRegistrosArticulo,solicitudesMap.get("descCcSolicitado_"+sol));
				
				//Carga el detalle de los articulos de la solicitud
				temporal = consultarDetalleArticulosDirectos(con,respuesta.get("numeroSolicitud_"+numRegistrosArticulo).toString());
				respuesta.put("articulos_"+numRegistrosArticulo,temporal);				
				
				numRegistrosArticulo++;
				respuesta.put("numRegistros",numRegistrosArticulo);
			}
			//Tipo de Cargo Directo Servicios
			if(solicitudesMap.get("tipoSolicitud_"+sol).toString().equals(tipoSolicitud) && 
					tipoSolicitud.equals(ConstantesBD.codigoTipoSolicitudCargosDirectosServicios+""))
			{					
				
				//Carga el detalle de los servicios de la solicitud
				temporal = consultarDetalleServicioCDirectos(con,solicitudesMap.get("numeroSolicitud_"+sol).toString());			
				
				//Tipo Servicio Consulta
				if(temporal.get("tipoServicio_0").toString().equals(tipoServicio) && 
						tipoServicio.equals(ConstantesBD.codigoServicioCargosConsultaExterna+""))
				{
					respuesta.put("numeroSolicitud_"+numRegistrosConsulta,solicitudesMap.get("numeroSolicitud_"+sol));
					respuesta.put("fechaSolicitud_"+numRegistrosConsulta,solicitudesMap.get("fechaSolicitud_"+sol));
					respuesta.put("horaSolicitud_"+numRegistrosConsulta,solicitudesMap.get("horaSolicitud_"+sol));
					respuesta.put("tipoSolicitud_"+numRegistrosConsulta,solicitudesMap.get("tipoSolicitud_"+sol));
					respuesta.put("centroCostoSolicitud_"+numRegistrosConsulta,solicitudesMap.get("centroCostoSolicitante_"+sol));
					respuesta.put("descripcionCCostoSolicitante_"+numRegistrosConsulta,solicitudesMap.get("descCcSolicitante_"+sol));
					respuesta.put("especialidadSolicitante_"+numRegistrosConsulta,solicitudesMap.get("especialidadSolicitante_"+sol));
					respuesta.put("descripcionEspecialidadSolicitante_"+numRegistrosConsulta,solicitudesMap.get("descripcionEspecialidadSolicitante_"+sol));
					respuesta.put("centroCostoSolicitado_"+numRegistrosConsulta,solicitudesMap.get("centroCostoSolicitado_"+sol));
					respuesta.put("descripcionCCostoSolicitado_"+numRegistrosConsulta,solicitudesMap.get("descCcSolicitado_"+sol));
					
					//Almacena la información de los servicios
					respuesta.put("servicios_"+numRegistrosConsulta,temporal);
					
					//Almacena la información de los diagnosticos del servicio
					if(!temporal.get("codigoCdirectoHc_0").toString().equals(""))
					{
						temporal = consultarDiagnosticosServiciosCDirecto(con,temporal.get("codigoCdirectoHc_0").toString());
						respuesta.put("diagnosticos_"+numRegistrosConsulta,temporal);
						
					}
					else					
					{
						respuesta.put("diagnosticos_"+numRegistrosConsulta,"");
					}
					
					numRegistrosConsulta++;
					respuesta.put("numRegistros",numRegistrosConsulta);
				}											
							
				//Tipo Servicio Procedimiento
				else if(temporal.get("tipoServicio_0").toString().equals(tipoServicio) && 
						tipoServicio.equals(ConstantesBD.codigoServicioProcedimiento+""))
				{
					respuesta.put("numeroSolicitud_"+numRegistrosProcedimientos,solicitudesMap.get("numeroSolicitud_"+sol));
					respuesta.put("fechaSolicitud_"+numRegistrosProcedimientos,solicitudesMap.get("fechaSolicitud_"+sol));
					respuesta.put("horaSolicitud_"+numRegistrosProcedimientos,solicitudesMap.get("horaSolicitud_"+sol));
					respuesta.put("tipoSolicitud_"+numRegistrosProcedimientos,solicitudesMap.get("tipoSolicitud_"+sol));
					respuesta.put("centroCostoSolicitud_"+numRegistrosProcedimientos,solicitudesMap.get("centroCostoSolicitante_"+sol));
					respuesta.put("descripcionCCostoSolicitante_"+numRegistrosProcedimientos,solicitudesMap.get("descCcSolicitante_"+sol));
					respuesta.put("especialidadSolicitante_"+numRegistrosProcedimientos,solicitudesMap.get("especialidadSolicitante_"+sol));
					respuesta.put("descripcionEspecialidadSolicitante_"+numRegistrosProcedimientos,solicitudesMap.get("descripcionEspecialidadSolicitante_"+sol));
					respuesta.put("centroCostoSolicitado_"+numRegistrosProcedimientos,solicitudesMap.get("centroCostoSolicitado_"+sol));
					respuesta.put("descripcionCCostoSolicitado_"+numRegistrosProcedimientos,solicitudesMap.get("descCcSolicitado_"+sol));
					
					//Almacena la información de los servicios
					respuesta.put("servicios_"+numRegistrosProcedimientos,temporal);
					
					//Almacena la información de los diagnosticos del servicio
					if(!temporal.get("codigoCdirectoHc_0").toString().equals(""))
					{
						temporal = consultarDiagnosticosServiciosCDirecto(con,temporal.get("codigoCdirectoHc_0").toString());
						respuesta.put("diagnosticos_"+numRegistrosProcedimientos,temporal);
					}
					else
						respuesta.put("diagnosticos_"+numRegistrosProcedimientos,"");
					
					numRegistrosProcedimientos++;
					respuesta.put("numRegistros",numRegistrosProcedimientos);
				}				
				else if(tipoServicio.equals("") && 
						!temporal.get("tipoServicio_0").toString().equals(ConstantesBD.codigoServicioProcedimiento+"") && 
							!temporal.get("tipoServicio_0").toString().equals(ConstantesBD.codigoServicioCargosConsultaExterna+""))
				{
					respuesta.put("numeroSolicitud_"+numRegistrosServicios,solicitudesMap.get("numeroSolicitud_"+sol));
					respuesta.put("fechaSolicitud_"+numRegistrosServicios,solicitudesMap.get("fechaSolicitud_"+sol));
					respuesta.put("horaSolicitud_"+numRegistrosServicios,solicitudesMap.get("horaSolicitud_"+sol));
					respuesta.put("tipoSolicitud_"+numRegistrosServicios,solicitudesMap.get("tipoSolicitud_"+sol));
					respuesta.put("centroCostoSolicitud_"+numRegistrosServicios,solicitudesMap.get("centroCostoSolicitante_"+sol));
					respuesta.put("descripcionCCostoSolicitante_"+numRegistrosServicios,solicitudesMap.get("descCcSolicitante_"+sol));
					respuesta.put("especialidadSolicitante_"+numRegistrosServicios,solicitudesMap.get("especialidadSolicitante_"+sol));
					respuesta.put("descripcionEspecialidadSolicitante_"+numRegistrosServicios,solicitudesMap.get("descripcionEspecialidadSolicitante_"+sol));
					respuesta.put("centroCostoSolicitado_"+numRegistrosServicios,solicitudesMap.get("centroCostoSolicitado_"+sol));
					respuesta.put("descripcionCCostoSolicitado_"+numRegistrosServicios,solicitudesMap.get("descCcSolicitado_"+sol));
					
					//Almacena la información de los servicios
					respuesta.put("servicios_"+numRegistrosServicios,temporal);
					
					numRegistrosServicios++;
					respuesta.put("numRegistros",numRegistrosServicios);
				}
			}
			//Tipo de Cargo Directo Cirugias
			if(solicitudesMap.get("tipoSolicitud_"+sol).toString().equals(tipoSolicitud) && 
					tipoSolicitud.equals(ConstantesBD.codigoTipoSolicitudCirugia+""))
			{			
				respuesta.put("numero_solicitud_"+numRegistrosCirugias,solicitudesMap.get("numeroSolicitud_"+sol));
				respuesta.put("numeroSolicitud_"+numRegistrosCirugias,solicitudesMap.get("numeroSolicitud_"+sol));
				respuesta.put("fechaSolicitud_"+numRegistrosCirugias,solicitudesMap.get("fechaSolicitud_"+sol));
				respuesta.put("horaSolicitud_"+numRegistrosCirugias,solicitudesMap.get("horaSolicitud_"+sol));
				respuesta.put("tipoSolicitud_"+numRegistrosCirugias,solicitudesMap.get("tipoSolicitud_"+sol));
				respuesta.put("centroCostoSolicitud_"+numRegistrosCirugias,solicitudesMap.get("centroCostoSolicitante_"+sol));
				respuesta.put("descripcionCCostoSolicitante_"+numRegistrosCirugias,solicitudesMap.get("descCcSolicitante_"+sol));
				respuesta.put("especialidadSolicitante_"+numRegistrosCirugias,solicitudesMap.get("especialidadSolicitante_"+sol));
				respuesta.put("descripcionEspecialidadSolicitante_"+numRegistrosCirugias,solicitudesMap.get("descripcionEspecialidadSolicitante_"+sol));
				respuesta.put("centroCostoSolicitado_"+numRegistrosCirugias,solicitudesMap.get("centroCostoSolicitado_"+sol));
				respuesta.put("descripcionCCostoSolicitado_"+numRegistrosCirugias,solicitudesMap.get("descCcSolicitado_"+sol));
				respuesta.put("codigoPeticion_"+numRegistrosCirugias,solicitudesMap.get("codigoPeticion_"+sol));
				
				//Carga el detalle de los articulos de la solicitud
				temporal = consultarServiciosCirugiaCDirecto(con,respuesta.get("numeroSolicitud_"+numRegistrosCirugias).toString());
				respuesta.put("servicios_"+numRegistrosCirugias,temporal);				
				
				numRegistrosCirugias++;
				respuesta.put("numRegistros",numRegistrosCirugias);
			}
		}
		
		return respuesta;
	}
	
	//******************************************************************************************************************************
	
	/**
	 * Consulta la informacion de los articulos de una solicitud de cargo directo 
	 * @param Connection con
	 * @param String numeroSolicitud
	 * */
	public static HashMap consultarDetalleArticulosDirectos(Connection con,String numeroSolicitud)
	{
		HashMap parametros =new HashMap();
		parametros.put("numeroSolicitud",numeroSolicitud);
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getResumenAtencionesDao().consultarDetalleArticulosDirectos(con, parametros);
	}
	
	//******************************************************************************************************************************
	
	/**
	 * Consulta la información de los servicios de la solicitud de Cargo Directo 
	 * @param Connection con
	 * @param String numeroSolicitud
	 * */
	public static HashMap consultarDetalleServicioCDirectos(Connection con, String numeroSolicitud)
	{
		HashMap parametros =new HashMap();
		parametros.put("numeroSolicitud",numeroSolicitud);
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getResumenAtencionesDao().consultarDetalleServicioCDirectos(con, parametros);
	}

	//******************************************************************************************************************************
	
	/**
	 * Consulta la información de los diagnosticos del servicio de Cargo Directo
	 * @param Connection con
	 * @param String codigoCargoDirectoHc
	 * */
	public static HashMap consultarDiagnosticosServiciosCDirecto(Connection con,String codigoCargoDirecto)
	{
		HashMap parametros =new HashMap();
		parametros.put("codigoCargoDirectoHc",codigoCargoDirecto);
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getResumenAtencionesDao().consultarDiagnosticosServiciosCDirecto(con, parametros);	
	}
	
	//******************************************************************************************************************************
	
	/**
	 * Consulta la información de los diagnosticos del servicio de Cargo Directo
	 * @param Connection con
	 * @param String numeroSolicitud
	 * */
	public static HashMap consultarServiciosCirugiaCDirecto(Connection con,String numeroSolicitud)
	{
		HashMap parametros =new HashMap();
		parametros.put("numeroSolicitud",numeroSolicitud);
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getResumenAtencionesDao().consultarServiciosCirugiaCDirecto(con, parametros);
	}
	
	
	/**
	 * Método implementado para cargar las cuentas del ingreso del paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<CuentasPaciente> getCuentasPaciente(Connection con,int codigoIngreso)
	{
		HashMap campos = new HashMap();
		campos.put("codigoIngreso",codigoIngreso);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getResumenAtencionesDao().getCuentasPaciente(con, campos);
	}
	//******************************************************************************************************************************
	
	/**
	 * Verfica si un registro existe en el mapa
	 * @param HashMap mapaBusqueda
	 * @param String codigoArt
	 * @param String dosis
	 * @param String frecuencia
	 * @param String via
	 * */
	public static boolean existeRegistroMapa(
			HashMap mapaBusqueda,
			String codigoArt,
			String dosis,
			String frecuencia,
			String via)
	{
		for(int i = 0; i<Utilidades.convertirAEntero(mapaBusqueda.get("numRegistros").toString()); i++)
		{
			if(mapaBusqueda.get("codigo_art_"+i).toString().equals(codigoArt) &&
					mapaBusqueda.get("dosis_"+i).toString().equals(dosis) && 
						mapaBusqueda.get("frecuencia_"+i).toString().equals(frecuencia) && 
							mapaBusqueda.get("via_"+i).toString().equals(via))
			 return true;		
		}
		
		return false;		
	}

	/**
	 * Método implementado para saber si se han registrado ordenes ambulatorias
	 * @param con
	 * @param idIngreso
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public static boolean tieneOrdenesAmbulatorias(Connection con, String idIngreso,
			String idCuenta, String fechaInicial, String fechaFinal,
			String horaInicial, String horaFinal) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getResumenAtencionesDao().tieneOrdenesAmbulatorias(con,idIngreso,idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal);
	}
	
	
	

	
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public boolean existeValoracionEnfermeria(Connection con,String idIngreso,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		return resumentAtencionesDao.existeValoracionEnfermeria(con,idIngreso,fechaInicial,fechaFinal,horaInicial,horaFinal);
	}

	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public boolean existeResultadosLaboratorios(Connection con,String idIngreso,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		return resumentAtencionesDao.existeResultadosLaboratorios(con,idIngreso,fechaInicial,fechaFinal,horaInicial,horaFinal);
	}

	
	//******************************************************************************************************************************
	//******************************************************************************************************************************
	
	/**
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return Si hay notas generales
	 */
	public  boolean existeNotasGenerales(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal,Integer idPaciente)
	{
		return resumentAtencionesDao.existeNotasGenerales(con, idCuenta, fechaInicial, fechaFinal, horaInicial, horaFinal,idPaciente);
	}
	
	
	public  boolean existeNotasGeneralesAsocio(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal,Integer idPaciente)
	{
		return resumentAtencionesDao.existeNotasGeneralesAsocio(con, idCuenta, fechaInicial, fechaFinal, horaInicial, horaFinal,idPaciente);
	}
	/**
	 * @param con
	 * @param idCuenta
	 * @return si existe nostas de recuperacion 
	 * @throws Exception
	 */
	public  Boolean existenNotasRecuperacion(Connection con,String idCuenta,Integer idPaciente) throws Exception
	{
		return resumentAtencionesDao.existenNotasRecuperacion(con, idCuenta,idPaciente);
	}
	
	/**
	 * @param con
	 * @param idCuenta
	 * @param idPaciente
	 * @return
	 * @throws Exception
	 */
	public  Boolean existenNotasRecuperacionAsocio(Connection con,String idCuenta,Integer idPaciente) throws Exception
	{
		return resumentAtencionesDao.existenNotasRecuperacionAsocio(con, idCuenta,idPaciente);
	}
	
	/**
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public boolean existeRespuestaCirugiasSolicitudes(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		return resumentAtencionesDao.existeRespuestaCirugiasSolicitudes(con,idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal);
	}
	
	
	public   List<DtoViasIngresoHC> obtenerViasIngreso(Connection con) throws SQLException
	{
		return resumentAtencionesDao.obtenerViasIngreso(con);
	}
	
	public  List<DtoEspecialidadesHC> obtenerEspecialidades(Connection con)throws SQLException{
		return resumentAtencionesDao.obtenerEspecialidades(con);
	}
	
	public  List<String> obtenerAnos(Connection con, int idPaciente)throws SQLException{
		return resumentAtencionesDao.obtenerAnos(con, idPaciente);
	}

	public boolean valoracionHospEsCopiaValoracionUrg(Connection con, int idCuenta)throws SQLException{
		return resumentAtencionesDao.valoracionHospEsCopiaValoracionUrg(con, idCuenta);
	}
}