/*
 * @(#)PostgresqlResumenAtencionesDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.princetonsa.dao.ResumenAtencionesDao;
import com.princetonsa.dao.sqlbase.SqlBaseResumenAtencionesDao;
import com.princetonsa.dto.historiaClinica.DtoEspecialidadesHC;
import com.princetonsa.dto.historiaClinica.DtoViasIngresoHC;
import com.princetonsa.mundo.CuentasPaciente;

/**
 * Esta clase implementa el contrato estipulado en <code>ResumenAtencionesDao</code>, proporcionando los servicios
 * de acceso a una base de datos Postgresql requeridos por la clase <code>ResumenAtenciones</code>
 * 
 *	@version 1.0, 15/06/2004
 */
public class PostgresqlResumenAtencionesDao implements ResumenAtencionesDao
{
	/**
	 * Implementación de la revisión de los antecedentes que existen
	 * para paciente dado en una BD Postgresql
	 * 
	 * @see com.princetonsa.dao.ResumenAtencionesDao#queAntecedentesExisten (Connection , int ) throws SQLException
	 */
	public Collection queAntecedentesExisten (Connection con, int idPaciente) throws SQLException
	{
		return SqlBaseResumenAtencionesDao.queAntecedentesExisten (con, idPaciente);
	}
	
	/**
	 * Implementación de la búsqueda de todas las cuentas para un
	 * paciente dado en una BD Postgresql
	 * 
	 * @see com.princetonsa.dao.ResumenAtencionesDao#busquedaCuentas (Connection , int ) throws SQLException
	 */
	public Collection busquedaCuentas (Connection con, int idPaciente) throws SQLException
	{
		return SqlBaseResumenAtencionesDao.busquedaCuentas (con, idPaciente) ;
	}

	/**
	 * Implementación de la búsqueda de los datos de la admisión
	 * de urgencias para una cuenta dada en una BD Postgresql
	 * 
	 * @see com.princetonsa.dao.ResumenAtencionesDao#busquedaDatosAdmisionUrgencias (Connection , int ) throws SQLException
	 */
	public Collection busquedaDatosAdmisionUrgencias (Connection con, int idCuenta) throws SQLException
	{
		return SqlBaseResumenAtencionesDao.busquedaDatosAdmisionUrgencias (con, idCuenta) ;
	}
	
	/**
	 * Implementación de la búsqueda de los datos de la admisión
	 * de hospitalización para una cuenta dada en una BD Postgresql
	 * 
	 * @see com.princetonsa.dao.ResumenAtencionesDao#busquedaDatosAdmisionHospitalizacion (Connection , int ) throws SQLException
	 */
	public Collection busquedaDatosAdmisionHospitalizacion (Connection con, int idCuenta) throws SQLException
	{
		return SqlBaseResumenAtencionesDao.busquedaDatosAdmisionHospitalizacion (con, idCuenta) ;
	}

	/**
	 * Implementación de la revisión de si existe valoración inicial
	 * para una cuenta dada en una BD Postgresql
	 * 
	 * @see com.princetonsa.dao.ResumenAtencionesDao#tieneValoracionInicial (Connection , int , boolean ) throws SQLException
	 */
	public boolean tieneValoracionInicial (Connection con, int idCuenta, boolean buscandoValoracionInicialUrgencias,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal) throws SQLException
	{
		return SqlBaseResumenAtencionesDao.tieneValoracionInicial (con, idCuenta, buscandoValoracionInicialUrgencias,fechaInicial,fechaFinal,horaInicial,horaFinal) ;
	}
	
	/**
	 * Implementación de la revisión de si existe una solicitud dado
	 * el tipo para una cuenta dada en una BD Oracle
	 * 
	 * @see com.princetonsa.dao.ResumenAtencionesDao#tieneSolicitudDe (Connection , int , int ) throws SQLException
	 */
	public boolean tieneSolicitudDe (Connection con, int idCuenta,int codigoTipoSolicitudBuscada,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal) 
	{
		return SqlBaseResumenAtencionesDao.tieneSolicitudDe (con, idCuenta,codigoTipoSolicitudBuscada,fechaInicial,fechaFinal,horaInicial,horaFinal) ;
	}

	/**
	 * Implementación de la revisión de si existen evoluciones para 
	 * una cuenta dada en una BD Hsqldb Postgresql
	 * 
	 * @see com.princetonsa.dao.ResumenAtencionesDao#tieneEvoluciones (Connection , int ) throws SQLException
	 */
	public boolean tieneEvoluciones (Connection con, int idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal) throws SQLException
	{
		return SqlBaseResumenAtencionesDao.tieneEvoluciones (con, idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal) ;
	}

	/**
	 * Implementación de la revisión de la búsqueda del tipo de
	 * valoración para una cuenta dada en una BD Postgresql
	 * 
	 * @see com.princetonsa.dao.ResumenAtencionesDao#tipoValoracionInicial (Connection , int ) throws SQLException
	 */
	public int numeroValoracionInicial (Connection con, int idCuenta) throws SQLException
	{
		return SqlBaseResumenAtencionesDao.numeroValoracionInicial (con, idCuenta) ;
	}

	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public boolean tieneRegistroEventoAdverso(Connection con, String idIngreso)
	{
		return SqlBaseResumenAtencionesDao.tieneRegistroEventoAdverso(con, idIngreso);
	}
	
	/**
	 * Implementación de la búsqueda de la evolución que generó el egreso
	 * en una BD Postgresql
	 * 
	 * @see com.princetonsa.dao.ResumenAtencionesDao#codigoEvolucionGeneroEgreso (Connection , int ) throws SQLException
	 */
	public int codigoEvolucionGeneroEgreso (Connection con, int idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal) throws SQLException
	{
		return SqlBaseResumenAtencionesDao.codigoEvolucionGeneroEgreso (con, idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal) ;
	}

	/**
	 * Búsqueda de citas para pacientes de consulta externa
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public boolean existenCitas(Connection con, int idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal) throws SQLException
	{
		return SqlBaseResumenAtencionesDao.existenCitas(con, idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal);
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
		return SqlBaseResumenAtencionesDao.busquedaDiagnosticoPrincipal(con,idCuenta);
	}
	
	/**
	 * Método para insertar los documentos adjuntos de una cuenta en la 
	 * Historia de Atenciones(Resumen de Atenciones)
	 * @param con
	 * @param idCuenta
	 * @param nombreOriginal
	 * @return
	 */
	public int adjuntarDocumento(Connection con, int idCuenta, String nombreOriginal, String nombreArchivo) throws SQLException
	{
		return SqlBaseResumenAtencionesDao.adjuntarDocumento(con, idCuenta, nombreOriginal, nombreArchivo);
	}
	
	
	/**
	 * Método para saber si hay documentos adjuntos
	 * @param con
	 * @param idCuenta
	 * @return
	 * @throws SQLException
	 */
	public int existenAdjuntos(Connection con, int idCuenta) throws SQLException
	{
		return  SqlBaseResumenAtencionesDao.existenAdjuntos(con, idCuenta);
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
		return SqlBaseResumenAtencionesDao.consultarDocumentosAdjuntos(con, idCuenta);
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
		return SqlBaseResumenAtencionesDao.existenAnteOftal(con, codigoPersona);
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
		return SqlBaseResumenAtencionesDao.tieneRegistroEnfermeria(con,idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal);
	}
	
	/**
	 * Método implementado para listar las consultas PYP de la cuenta
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public HashMap listarConsultasPYP(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		return SqlBaseResumenAtencionesDao.listarConsultasPYP(con,idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal);
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
		return SqlBaseResumenAtencionesDao.tieneRegistroAccidenteTransito(con,idIngreso,estado,fechaInicial,fechaFinal,horaInicial,horaFinal);
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
		return SqlBaseResumenAtencionesDao.existeOrdenesMedicas(con,idIngreso,idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal);
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
		return SqlBaseResumenAtencionesDao.existeSignosVitales(con,idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal);
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
		return SqlBaseResumenAtencionesDao.existeSoporteRespiratorio(con,idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal);
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
		return SqlBaseResumenAtencionesDao.existeCateterSonda(con,idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal);
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
		return SqlBaseResumenAtencionesDao.existeCuidadosEspeciales(con,idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal);
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
		return SqlBaseResumenAtencionesDao.existeAnotacionesEnfermeria(con,idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal);
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
		return SqlBaseResumenAtencionesDao.existeHojaNeurologica(con,idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal);
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
		return SqlBaseResumenAtencionesDao.existeAdminMedicamentos(con,idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal);
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
		return SqlBaseResumenAtencionesDao.existeConsumoInsumos(con,idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal);
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
		return SqlBaseResumenAtencionesDao.existeRespIntInterconsulta(con,idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal);
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
		return SqlBaseResumenAtencionesDao.existeRespuestaProcedimiento(con,idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal);
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
		return SqlBaseResumenAtencionesDao.existeRespuestaCirugias(con,idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal);
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
		return SqlBaseResumenAtencionesDao.listarCitas(con, idCuenta, fechaInicial, fechaFinal, horaInicial, horaFinal);
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
		return SqlBaseResumenAtencionesDao.listarSolicitudesCitas(con, idCuenta, fechaInicial, fechaFinal, horaInicial, horaFinal);
	}
	
	/**
	 * Método que lista los servicios de la cita
	 * @param con
	 * @param codigoCita
	 * @return
	 */
	public HashMap listarServiciosCita(Connection con,String codigoCita)
	{
		return SqlBaseResumenAtencionesDao.listarServiciosCita(con, codigoCita);
	}
	
	/**
	 * Método que consulta los archivos adjuntos de las respuestas de procedimientos
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoRespuesta
	 * @return
	 */
	public HashMap consultarAdjuntosProcedimientos(Connection con,String numeroSolicitud,String codigoRespuesta)
	{
		return SqlBaseResumenAtencionesDao.consultarAdjuntosProcedimientos(con, numeroSolicitud, codigoRespuesta);
	}
	
	/**
	 * Método que verifica si el paciente ha tenido registros de triage
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean existeTriage(Connection con,HashMap campos)
	{
		return SqlBaseResumenAtencionesDao.existeTriage(con, campos);
	}
	
	/**
	 * Método implementado para consultar los registros triage de un paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap cargarListadoTriage(Connection con,HashMap campos)
	{
		return SqlBaseResumenAtencionesDao.cargarListadoTriage(con, campos);
	}

	/**
	 * 
	 */
	public boolean tieneRegistroEventoCatastrofico(Connection con, String idIngreso, String estado, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal)
	{
		return SqlBaseResumenAtencionesDao.tieneRegistroEventoCatastrofico(con,idIngreso,estado,fechaInicial,fechaFinal,horaInicial,horaFinal);
	}
	
	
	/**
	 * Consulta el historial Consentimiento Informado 
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap consultaHistorialConsentimientoInf(Connection con, HashMap parametros)
	{
		return SqlBaseResumenAtencionesDao.consultaHistorialConsentimientoInf(con, parametros);
	}
	
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
	public boolean tieneSolicitudCargoDirectoDe (
			Connection con, 
			int idCuenta,
			int codigoTipoSolicitudBuscada,
			String fechaInicial,
			String fechaFinal,
			String horaInicial,
			String horaFinal) 
	{
		return SqlBaseResumenAtencionesDao.tieneSolicitudCargoDirectoDe(con, idCuenta, codigoTipoSolicitudBuscada, fechaInicial, fechaFinal, horaInicial, horaFinal);
	}
	
	/**
	 * Verifica si existe Solicitudes de tipo Cargo Directos
	 * @param Connection con
	 * @param parametros
	 */
	public  HashMap consultarListadoSolCargoDirectoDe (
			Connection con, 
			HashMap parametros) 
	{
		return SqlBaseResumenAtencionesDao.consultarListadoSolCargoDirectoDe(con, parametros);		
	}
	
	
	/**
	 * Consulta la informacion de los articulos de una solicitud de cargo directo 
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public  HashMap consultarDetalleArticulosDirectos(Connection con, HashMap parametros)
	{
		return SqlBaseResumenAtencionesDao.consultarDetalleArticulosDirectos(con, parametros);
	}
	
	/**
	 * Consulta la información de los servicios de la solicitud de Cargo Directo 
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public  HashMap consultarDetalleServicioCDirectos(Connection con, HashMap parametros)
	{	
		return SqlBaseResumenAtencionesDao.consultarDetalleServicioCDirectos(con, parametros);
	}
	
	/**
	 * Consulta la información de los diagnosticos del servicio de Cargo Directo
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public  HashMap consultarDiagnosticosServiciosCDirecto(Connection con, HashMap parametros)
	{		
		return SqlBaseResumenAtencionesDao.consultarDiagnosticosServiciosCDirecto(con, parametros);
	}
	
	/**
	 * Consulta la información de los diagnosticos del servicio de Cargo Directo
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public  HashMap consultarServiciosCirugiaCDirecto(Connection con, HashMap parametros)
	{
		return SqlBaseResumenAtencionesDao.consultarServiciosCirugiaCDirecto(con, parametros);		
	}
	
	/**
	 * Método que verifica si existen escalas x ingresos
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean existenEscalasXIngreso(Connection con,HashMap campos)
	{
		return SqlBaseResumenAtencionesDao.existenEscalasXIngreso(con, campos);
	}
	
	/**
	 * Método implementado para cargar las cuentas del ingreso del paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<CuentasPaciente> getCuentasPaciente(Connection con,HashMap campos)
	{
		return SqlBaseResumenAtencionesDao.getCuentasPaciente(con, campos);
	}
	
	/**
	 * Método implementado para saber si existen valoraciones de cuidados especiales
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean existenValoracionesCuidadoEspecial(Connection con,HashMap campos)
	{
		return SqlBaseResumenAtencionesDao.existenValoracionesCuidadoEspecial(con, campos);
	}
	
	/**
	 * Método implementado para cargar el listado de las valoraciones de cuidado especial
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String, Object> cargarValoracionesCuidadoEspecial(Connection con,HashMap campos)
	{
		return SqlBaseResumenAtencionesDao.cargarValoracionesCuidadoEspecial(con, campos);
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
	public boolean tieneOrdenesAmbulatorias(Connection con, String idIngreso,
			String idCuenta, String fechaInicial, String fechaFinal,
			String horaInicial, String horaFinal)
	{
		return SqlBaseResumenAtencionesDao.tieneOrdenesAmbulatorias(con,idIngreso,idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal);
	}
	
	
	

	@Override
	public boolean existeValoracionEnfermeria(Connection con, String idIngreso,
			String fechaInicial, String fechaFinal, String horaInicial,
			String horaFinal) {
		return SqlBaseResumenAtencionesDao.existeValoracionEnfermeria(con,idIngreso,idIngreso,fechaInicial,fechaFinal,horaInicial,horaFinal);

	}

	@Override
	public boolean existeResultadosLaboratorios(Connection con,
			String idIngreso, String fechaInicial, String fechaFinal,
			String horaInicial, String horaFinal) {
		return SqlBaseResumenAtencionesDao.existeResultadosLaboratorios(con,idIngreso,idIngreso,fechaInicial,fechaFinal,horaInicial,horaFinal);

	}
	
	
	public  boolean existeNotasGenerales(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal,Integer idPaciente)
	{
		return SqlBaseResumenAtencionesDao.existeNotasGenerales(con, idCuenta, fechaInicial, fechaFinal, horaInicial, horaFinal,idPaciente);
	}
	
	public  boolean existeNotasGeneralesAsocio(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal,Integer idPaciente)
	{
		return SqlBaseResumenAtencionesDao.existeNotasGeneralesAsocio(con, idCuenta, fechaInicial, fechaFinal, horaInicial, horaFinal,idPaciente);
	}
	
	/**
	 * @see com.princetonsa.dao.ResumenAtencionesDao#existenNotasRecuperacion(java.sql.Connection, java.lang.String)
	 */
	public  Boolean existenNotasRecuperacion(Connection con,String idCuenta,Integer idpaciente) throws Exception
	{
		return SqlBaseResumenAtencionesDao.existenNotasRecuperacion(con, idCuenta,idpaciente);
	}
	
	/**
	 * @see com.princetonsa.dao.ResumenAtencionesDao#existenNotasRecuperacionAsocio(java.sql.Connection, java.lang.String, java.lang.Integer)
	 */
	public  Boolean existenNotasRecuperacionAsocio(Connection con,String idCuenta,Integer idpaciente) throws Exception
	{
		return SqlBaseResumenAtencionesDao.existenNotasRecuperacionAsocio(con, idCuenta,idpaciente);
	}
	/**
	 * @see com.princetonsa.dao.ResumenAtencionesDao#existeRespuestaCirugiasSolicitudes(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public  boolean existeRespuestaCirugiasSolicitudes(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		return SqlBaseResumenAtencionesDao.existeRespuestaCirugiasSolicitudes(con, idCuenta, fechaInicial, fechaFinal, horaInicial, horaFinal);
	}
	
	/**
	 * @see com.princetonsa.dao.ResumenAtencionesDao#ingresosFiltroBusqueda(java.sql.Connection, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public  Collection ingresosFiltroBusqueda(Connection con, int idPaciente,String ano,String fechaIngresoInicial,
			String fechaIngresoFinal, String centroAtencion,String viaIngreso,String especialidad) throws SQLException
			{
		return SqlBaseResumenAtencionesDao.ingresosFiltroBusqueda(con, idPaciente, ano, fechaIngresoInicial, fechaIngresoFinal, centroAtencion, viaIngreso, especialidad);
			}
	
	
	
	/**
	 * @see com.princetonsa.dao.ResumenAtencionesDao#obtenerViasIngreso(java.sql.Connection)
	 */
	public  List<DtoViasIngresoHC> obtenerViasIngreso(Connection con) throws SQLException
	{
		return SqlBaseResumenAtencionesDao.obtenerViasIngreso(con);
	}
	
	public  List<DtoEspecialidadesHC> obtenerEspecialidades(Connection con)throws SQLException{
		return SqlBaseResumenAtencionesDao.obtenerEspecialidades(con);
	}
	
	public  List<String> obtenerAnos(Connection con, int idPaciente)throws SQLException{
		return SqlBaseResumenAtencionesDao.obtenerAnos(con, idPaciente);
	}

	@Override
	public boolean valoracionHospEsCopiaValoracionUrg(Connection con, int idCuenta) throws SQLException {
		return SqlBaseResumenAtencionesDao.valoracionHospEsCopiaValoracionUrg(con, idCuenta);
	}

	@Override
	public List<String> consultarNumeroSolicitudXIngreso(Connection con, String ingreso) {
		return SqlBaseResumenAtencionesDao.consultarNumeroSolicitudXIngreso(con, ingreso);
	}
	
}