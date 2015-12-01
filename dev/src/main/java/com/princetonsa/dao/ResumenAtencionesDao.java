/*
 * @(#)ResumenAtencionesDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.princetonsa.dto.historiaClinica.DtoEspecialidadesHC;
import com.princetonsa.dto.historiaClinica.DtoViasIngresoHC;
import com.princetonsa.mundo.CuentasPaciente;

/**
 * Esta <i>interface</i> define el contrato de operaciones que debe implementar la clase que presta el servicio
 * de acceso a datos para el objeto <code>ResumenAtenciones</code>.
 * 
 *	@version 1.0, 15/06/2004
 */
public interface ResumenAtencionesDao 
{
	/**
	 * M�todo que busca todos los antecedentes que tiene un paciente
	 * dado. Si no tiene ning�n antecedente devuelve null, si no, la
	 * colecci�n devolver� un �nico elemento, donde cada campo
	 * (ej. campo alergias) si es nulo define que el paciente NO tiene
	 * antecedentes alergias, si no es nulo es porque si tiene este
	 * antecedente
	 * 
	 * @param con Conexi�n con la fuente de datos 
	 * @param idPaciente C�digo del paciente
	 * @return
	 * @throws SQLException
	 */
	public Collection queAntecedentesExisten (Connection con, int idPaciente) throws SQLException;
	
	/**
	 * M�todo que devuelve una colecci�n de todas las cuentas, de 
	 * acuerdo al anexo resumen de atenciones Id: 25 
	 * Versi�n: 25/May/2004
	 * 
	 * @param con Conexi�n con la fuente de datos 
	 * @param idPaciente C�digo del paciente
	 * @return
	 * @throws SQLException
	 */
	public Collection busquedaCuentas (Connection con, int idPaciente) throws SQLException;
	
	/**
	 * M�todo que devuelve una colecci�n de un elemento con
	 * los datos de la admisi�n de urgencias que se deben mostrar
	 * 
	 * @param con Conexi�n con la fuente de datos 
	 * @param idCuenta C�digo de la cuenta en la que se desea
	 * mostrar la admisi�n de urgencias
	 * @return
	 * @throws SQLException
	 */
	public Collection busquedaDatosAdmisionUrgencias (Connection con, int idCuenta) throws SQLException;
	
	/**
	 * M�todo que devuelve una colecci�n de un elemento con
	 * los datos de la admisi�n de hospitalizaci�n que se deben mostrar
	 * 
	 * @param con Conexi�n con la fuente de datos 
	 * @param idCuenta C�digo de la cuenta en la que se desea
	 * mostrar la admisi�n de hospitalizaci�n
	 * @return
	 * @throws SQLException
	 */
	public Collection busquedaDatosAdmisionHospitalizacion (Connection con, int idCuenta) throws SQLException;

	/**
	 * Boolean que indica si la cuenta especificada tiene valoraci�n
	 * inicial, el boolean sirve para saber si se est� buscando la 
	 * valoraci�n de urgencias (true) o la de hospitalizaci�n (false)
	 * 
	 * @param con Conexi�n con la fuente de datos 
	 * @param idCuenta C�digo de la cuenta en la que se est� 
	 * buscando si tiene valoraci�n inicial
	 * @param buscandoValoracionInicialUrgencias Boolean que
	 * indica si se est� buscando la valoraci�n inicial de urgencias 
	 * o la de hospitalizaci�n
	 * 
	 * @return
	 * @throws SQLException
	 */
	public boolean tieneValoracionInicial (Connection con, int idCuenta, boolean buscandoValoracionInicialUrgencias,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal) throws SQLException;
	
	/**
	 * M�todo para revisar si existe al menos una solicitud de un
	 * tipo determinado
	 * 
	 * @param con Conexi�n con la fuente de datos 
	 * @param idCuenta C�digo de la cuenta en la que se est�n 
	 * buscando solicitudes del tipo definido en codigoTipoSolicitudBuscada
	 * @param codigoTipoSolicitudBuscada C�digo (sacado de
	 * ConstantesBD) del tipo de solicitud para la que se quiere
	 * averiguar si existe al menos una
	 * 
	 * @return
	 * @throws SQLException
	 */
	public boolean tieneSolicitudDe (Connection con, int idCuenta, int codigoTipoSolicitudBuscada,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal) ;
	
	/**
	 * M�todo para revisar si una cuenta tiene evoluciones
	 * 
	 * @param con Conexi�n con la fuente de datos 
	 * @param idCuenta C�digo de la cuenta en la que se est�n
	 * buscando evoluciones 
	 * @return
	 * @throws SQLException
	 */
	public boolean tieneEvoluciones (Connection con, int idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal) throws SQLException;
	
	/**
	 * M�todo para revisar el tipo de valoraci�n inicial de una cuenta
	 * 
	 * @param con Conexi�n con la fuente de datos 
	 * @param idCuenta C�digo de la cuenta en la que se est�
	 * buscando el tipo de la valoraci�n inicial
	 * @return
	 * @throws SQLException
	 */
	public int numeroValoracionInicial (Connection con, int idCuenta) throws SQLException;
	
	/**
	 * M�todo que dada la cuenta busca el c�digo de la evoluci�n
	 * que gener� el egreso
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param idCuenta C�digo de la cuenta en la que se est�
	 * buscando el c�digo de la evoluci�n que gener� el egreso
	 * @return
	 * @throws SQLException
	 */
	public int codigoEvolucionGeneroEgreso (Connection con, int idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal) throws SQLException;
	
	/**
	 * B�squeda de citas para pacientes de consulta externa
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public boolean existenCitas(Connection con, int idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal) throws SQLException;
	/**
	 * Adici�n de Sebasti�n
	 * M�todo que consulta el diagnositco principal de semiEgreso en Urgencias
	 * para saber si es necesario crear el link "egresos" en el resumen de la Cuenta
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public int busquedaDiagnosticoPrincipal(Connection con,int idCuenta);
	
	/**
	 * M�todo para insertar los documentos adjuntos de una cuenta en la 
	 * Historia de Atenciones(Resumen de Atenciones)
	 * @param con
	 * @param idCuenta
	 * @param nombreOriginal
	 * @param nombreArchivo
	 * @return
	 * @throws SQLException
	 */
	public int adjuntarDocumento(Connection con, int idCuenta, String nombreOriginal, String nombreArchivo) throws SQLException;
	
	/**
	 * M�todo para saber si hay documentos adjuntos
	 * @param con
	 * @param idCuenta
	 * @return
	 * @throws SQLException
	 */
	public int existenAdjuntos(Connection con, int idCuenta) throws SQLException;
	
	
	/**
	 * M�todod para consultar la fecha - hora de todas las notas de recuperacion
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarDocumentosAdjuntos (Connection con, int idCuenta) throws SQLException;

	/**
	 * Metodo para saber si hay antecedentes oftalmologicos personales y/o familiares. 
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public boolean[] existenAnteOftal(Connection con, int codigoPersona) throws SQLException;
	
	/**
	 * M�todo implementado para verificar si a la cuenta se le ingres�
	 * registro de enfermer�a
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public boolean tieneRegistroEnfermeria(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal);
	
	/**
	 * M�todo implementado para listar las consultas PYP de la cuenta
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public HashMap listarConsultasPYP(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal);
	
	/**
	 * M�todo que verifica si el ingreso tiene registro de accidente de transito
	 * @param con
	 * @param idIngreso
	 * @param estado
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public boolean tieneRegistroAccidenteTransito(Connection con,String idIngreso,String estado,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal);
	
	/**
	 * M�todo implementado para verificar si existen ordenes m�dicas
	 * @param con
	 * @param idIngreso
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public boolean existeOrdenesMedicas(Connection con,String idIngreso,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal);
	
	/**
	 * M�todo implementado para verificar si existe signos vitales
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public boolean existeSignosVitales(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal);
	
	/**
	 * M�todo implementado para verificar si existe soporte respiratorio
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public boolean existeSoporteRespiratorio(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal);
	
	/**
	 * M�todo que verifica si hay cateter y sonda
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public boolean existeCateterSonda(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal);
	
	/**
	 * M�todo que verifica si existen cuidados especiales
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public boolean existeCuidadosEspeciales(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal);
	
	/**
	 * M�todo que verifica si hay anotaciones de enfermeria
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public boolean existeAnotacionesEnfermeria(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal);
	
	/**
	 * M�todo implementado para verificar si existe la hoja neurologica
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public boolean existeHojaNeurologica(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal);
	
	/**
	 * M�todo que verifica si hay adminsitracion de medicamentos
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public boolean existeAdminMedicamentos(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal);
	
	/**
	 * M�todo que verifica si hay consumo de insumos
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public boolean existeConsumoInsumos(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal);
	
	 /** Metodo que verifica si hay respuesta o interpertacion de interconsultas
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public boolean existeRespIntInterconsulta(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal);
	
	/**
	 * M�todo que verifica si hay respuesta de procedimientos
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public boolean existeRespuestaProcedimiento(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal);
	
	/**
	 * M�todo que verifica si existe respuesta de cirugias
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public boolean existeRespuestaCirugias(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal);
	
	/**
	 * M�todo implementado para listar las citas de un paciente 
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public HashMap listarCitas(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal);
	
	/**
	 * M�todo implementado para listar las solicitudes de citas de un paciente 
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public HashMap listarSolicitudesCitas(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal);
	
	/**
	 * M�todo que lista los servicios de la cita
	 * @param con
	 * @param codigoCita
	 * @return
	 */
	public HashMap listarServiciosCita(Connection con,String codigoCita);
	
	/**
	 * M�todo que consulta los archivos adjuntos de las respuestas de procedimientos
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoRespuesta
	 * @return
	 */
	public HashMap consultarAdjuntosProcedimientos(Connection con,String numeroSolicitud,String codigoRespuesta);
	
	/**
	 * M�todo que verifica si el paciente ha tenido registros de triage
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean existeTriage(Connection con,HashMap campos);
	
	/**
	 * M�todo implementado para consultar los registros triage de un paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap cargarListadoTriage(Connection con,HashMap campos);

	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param estado
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public boolean tieneRegistroEventoCatastrofico(Connection con, String idIngreso, String estado, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal);
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public boolean tieneRegistroEventoAdverso(Connection con, String idIngreso);
	
	/**
	 * Consulta el historial Consentimiento Informado 
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap consultaHistorialConsentimientoInf(Connection con, HashMap parametros);
	
	
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
			String horaFinal);
	
	/**
	 * Verifica si existe Solicitudes de tipo Cargo Directos
	 * @param Connection con
	 * @param parametros
	 */
	public  HashMap consultarListadoSolCargoDirectoDe (
			Connection con, 
			HashMap parametros);
	
	/**
	 * Consulta la informacion de los articulos de una solicitud de cargo directo 
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public  HashMap consultarDetalleArticulosDirectos(Connection con, HashMap parametros);
	
	/**
	 * Consulta la informaci�n de los servicios de la solicitud de Cargo Directo 
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public  HashMap consultarDetalleServicioCDirectos(Connection con, HashMap parametros);
	
	/**
	 * Consulta la informaci�n de los diagnosticos del servicio de Cargo Directo
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public  HashMap consultarDiagnosticosServiciosCDirecto(Connection con, HashMap parametros);
	
	/**
	 * Consulta la informaci�n de los diagnosticos del servicio de Cargo Directo
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public  HashMap consultarServiciosCirugiaCDirecto(Connection con, HashMap parametros);
	
	/**
	 * M�todo que verifica si existen escalas x ingresos
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean existenEscalasXIngreso(Connection con,HashMap campos);
	
	/**
	 * M�todo implementado para cargar las cuentas del ingreso del paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<CuentasPaciente> getCuentasPaciente(Connection con,HashMap campos);
	
	/**
	 * M�todo implementado para saber si existen valoraciones de cuidados especiales
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean existenValoracionesCuidadoEspecial(Connection con,HashMap campos);
	
	/**
	 * M�todo implementado para cargar el listado de las valoraciones de cuidado especial
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String, Object> cargarValoracionesCuidadoEspecial(Connection con,HashMap campos);

	/**
	 * M�todo implementado para saber si se han registrado ordenes ambulatorias
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
			String horaInicial, String horaFinal);
	

	public boolean existeValoracionEnfermeria(Connection con, String idIngreso,
			String fechaInicial, String fechaFinal, String horaInicial,
			String horaFinal);

	public boolean existeResultadosLaboratorios(Connection con,
			String idIngreso, String fechaInicial, String fechaFinal,
			String horaInicial, String horaFinal);
	
	/**
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return si existen notas generales
	 */
	public  boolean existeNotasGenerales(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal,Integer idPaciente);
	
	public  boolean existeNotasGeneralesAsocio(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal,Integer idPaciente);
	
	
	/**
	 * @param con
	 * @param idCuenta
	 * @return Si existen notas de recuperacion 
	 * @throws Exception
	 */
	public  Boolean existenNotasRecuperacion(Connection con,String idCuenta,Integer idPaciente) throws Exception;
	
	/**
	 * @param con
	 * @param idCuenta
	 * @param idPaciente
	 * @return
	 * @throws Exception
	 */
	public  Boolean existenNotasRecuperacionAsocio(Connection con,String idCuenta,Integer idPaciente) throws Exception;
	
	/**
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public  boolean existeRespuestaCirugiasSolicitudes(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal);
	
	
	/**
	 * @param con
	 * @param idPaciente
	 * @param ano
	 * @param fechaIngresoInicial
	 * @param fechaIngresoFinal
	 * @param centroAtencion
	 * @param viaIngreso
	 * @param especialidad
	 * @return lista de ingresos
	 * @throws SQLException
	 */
	public  Collection ingresosFiltroBusqueda(Connection con, int idPaciente,String ano,String fechaIngresoInicial,
			String fechaIngresoFinal, String centroAtencion,String viaIngreso,String especialidad) throws SQLException;
	
	
	/**
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public  List<DtoViasIngresoHC> obtenerViasIngreso(Connection con) throws SQLException;
	
	public  List<DtoEspecialidadesHC> obtenerEspecialidades(Connection con)throws SQLException;
	
	public  List<String> obtenerAnos(Connection con, int idPaciente)throws SQLException;

	public boolean valoracionHospEsCopiaValoracionUrg(Connection con, int idCuenta) throws SQLException;
	
	public List<String> consultarNumeroSolicitudXIngreso(Connection con, String ingreso);
	
}