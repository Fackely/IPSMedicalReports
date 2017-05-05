/*
 * Mayo 10, 2007
 */
package com.princetonsa.dao.oracle.historiaClinica;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.InfoDatosInt;
import util.InfoDatosString;
import util.ResultadoBoolean;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.UtilidadesHistoriaClinicaDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseUtilidadesHistoriaClinicaDao;
import com.princetonsa.dto.facturacion.DtoConvenio;
import com.princetonsa.dto.historiaClinica.DtoEspecialidad;
import com.princetonsa.dto.historiaClinica.DtoInformacionParto;
import com.princetonsa.dto.historiaClinica.DtoInformacionRecienNacido;
import com.princetonsa.dto.historiaClinica.DtoMedicamentosOriginales;
import com.princetonsa.dto.historiaClinica.DtoRevisionSistema;
import com.princetonsa.dto.historiaClinica.componentes.DtoHistoriaMenstrual;
import com.princetonsa.dto.historiaClinica.componentes.DtoOftalmologia;
import com.princetonsa.dto.historiaClinica.componentes.DtoPediatria;
import com.princetonsa.dto.manejoPaciente.DtoRequsitosPaciente;
import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.atencion.Diagnostico;
import com.princetonsa.mundo.atencion.SignoVital;
import com.servinte.axioma.fwk.exception.BDException;

/**
 * 
 * @author sgomez
 * Métodos de oracle para el acceso a la fuente de datos para las utilidades
 * del módulo de HISTORIA CLINICA
 */
public class OracleUtilidadesHistoriaClinicaDao implements UtilidadesHistoriaClinicaDao 
{

	/**
	 * Método que consulta los datos de accidente de trabajo del triage
	 * @param con
	 * @param consecutivo
	 * @param consecutivoFecha
	 * @return
	 */
	public HashMap consultaDatosAccidenteTrabajoTriage(Connection con,String consecutivo,String consecutivoFecha)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.consultaDatosAccidenteTrabajoTriage(con, consecutivo, consecutivoFecha);
	}
	
	/**
	 * Método que consulta la última evolucion de un ingreso
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public int consultarUltimaEvolucionIngreso(Connection con,int ingreso)
	{
		String seccionWHERE = " c.id_ingreso = ? ";
		return SqlBaseUtilidadesHistoriaClinicaDao.consultarUltimaEvolucionIngreso(con, ingreso, seccionWHERE);
	}
	
	/**
	 * Método que consula la última valoracion de un ingreso
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public int consultarUltimaValoracionIngreso(Connection con,int ingreso)
	{
		String seccionWHERE = " c.id_ingreso =  ? ";
		return SqlBaseUtilidadesHistoriaClinicaDao.consultarUltimaValoracionIngreso(con, ingreso, seccionWHERE);
	}
	
	/**
	 * Método que consulta los motivos sirc parametrizados por institucion
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap obtenerMotivosSirc(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerMotivosSirc(con, campos);
	}
	
	/**
	 * Método que consulta los servicios SIRC de una institucion
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap obtenerServiciosSirc(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerServiciosSirc(con, campos);
	}
	
	/**
	 * Método que consulta los estados de conciencia 
	 * @param con
	 * @param incluyeNoDefinido (si es true se incluirá el registro 'No definido')
	 * @return
	 */
	public HashMap obtenerEstadosConciencia(Connection con,boolean incluyeNoDefinido)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerEstadosConciencia(con, incluyeNoDefinido);
	}
	
	/**
	 * Método que consulta las interpretaciones y la descripcion de los procedimientos de un ingreso
	 * validando el parámetro general "Valida en egreso solicitudes interpretadas:"
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap obtenerInterpretacionProcedimientosIngreso(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerInterpretacionProcedimientosIngreso(con, campos);
	}
	
	/**
	 * Método que consulta la última anamnesis del ingreso
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String getUltimaAnamnesisIngreso(Connection con,String numeroSolicitud)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.getUltimaAnamnesisIngreso(con, numeroSolicitud);
	}
	
	/**
	 * Método que consulta la ultima referencia  del paciente 
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public String getUltimaReferenciaPaciente(Connection con,String codigoPaciente,String tipoReferencia,String estado)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.getUltimaReferenciaPaciente(con, codigoPaciente, tipoReferencia, estado);
	}
	
	/**
	 * Método que actualiza el estado de la referencia
	 * @param con
	 * @param numeroReferencia
	 * @param ingreso
	 * @param estado
	 * @param loginUsuario
	 * @return
	 */
	public boolean actualizarEstadoReferencia(Connection con,String numeroReferencia,String ingreso,String estado,String loginUsuario)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.actualizarEstadoReferencia(con, numeroReferencia, ingreso, estado, loginUsuario);
	}
	
	/**
	 * Método que consulta el estado de la referencia de un ingreso
	 * Nota* Si retorna cadena vacía quiere decir que ese ingreso no tiene
	 * referencia asociada
	 * @param con
	 * @param campos
	 * @return
	 */
	public String getEstadoReferenciaXIngreso(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.getEstadoReferenciaXIngreso(con, campos);
	}
	
	/**
	 * Método para consultar el diagnostico de ingreso que se debe postular
	 * en la valoracion de hospitalizacion en el caso de que se haya ingresado
	 * una referencia externa y el origen de la admision sea remitido
	 * @param con
	 * @param codigoCuenta
	 * Se retorna acronimo + separadorSplit + tipoCie + separadorSplit + nombre
	 * Si no encuentra nada retorna cadena vacía
	 */
	public String getDxIngresoDeReferencia(Connection con,String codigoCuenta)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.getDxIngresoDeReferencia(con, codigoCuenta);
	}
	
	/**
	 * Método que consulta el número de referencia de un ingreso dependiendo del tipo de referencia
	 * Nota: En el caso de que no encuentre nada retorna una cadena vacía
	 * @param con
	 * @param idIngreso
	 * @param tipoReferencia
	 * @return
	 */
	public String getNumeroReferenciaIngreso(Connection con,String idIngreso,String tipoReferencia)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.getNumeroReferenciaIngreso(con, idIngreso, tipoReferencia);
	}
	
	/**
	 * Método que verifica si hay una contrarreferencia para dado un número de referencia
	 * @param con
	 * @param numeroReferencia
	 * @return
	 */
	public boolean existeContrarreferencia(Connection con,String numeroReferencia)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.existeContrarreferencia(con, numeroReferencia);
	}
	
	/***
	 * Método que consulta los datos de la referencia de un ingreso para postular un mensaje
	 * de advertencia 
	 * @param con
	 * @param campos
	 * @return
	 */
	public String getMensajeReferenciaParaValidacion(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.getMensajeReferenciaParaValidacion(con, campos);
	}
	
	/**
	 * 
	 */
	public InfoDatosInt obtenerEstadoCuenta(Connection con, int idCuenta) 
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerEstadoCuenta(con,idCuenta);
	}
	

	/**
	 * 
	 */
	public InfoDatosInt obtenerCentroAtencionCuenta(Connection con, int idCuenta) 
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerCentroAtencionCuenta(con,idCuenta);
	}

	/**
	 * 
	 */
	public ArrayList<DtoSubCuentas> obtenerResponsablesIngreso(Connection con,int codigoIngreso,boolean todos, String[] excluirResponsables,boolean pyp, String subCuenta,int codigoViaIngreso) throws BDException 
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerResponsablesIngreso(con,codigoIngreso,todos,excluirResponsables,pyp, subCuenta,codigoViaIngreso);
	}
	

	/**
	 * 
	 */
	public DtoSubCuentas obtenerResponsable(Connection con, int codigoIngreso, int codigoConvenio) 
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerResponsable(con,codigoIngreso,codigoConvenio);
	}

	/**
	 * 
	 */
	public DtoSubCuentas obtenerResponsable(Connection con, int codigoSubcuenta) throws BDException
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerResponsable(con,codigoSubcuenta);
	}
	
	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public ArrayList<DtoSolicitudesSubCuenta> obtenerSolicitudesSubCuenta(Connection con, String subCuenta,int[] estados,boolean incluirPaquetizadas,boolean agruparPortatiles)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerSolicitudesSubCuenta(con,subCuenta,estados,incluirPaquetizadas,agruparPortatiles);
	}

	
	
	/**
	 * 
	 */
	public InfoDatosString obtenerEstadoIngreso(Connection con, int codigoIngreso) 
	{
		
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerEstadoIngreso(con,codigoIngreso);
	}
	
	/**
	 * 
	 */
	public int cuentaTieneAsocioCompleto(Connection con, int codigoCuenta) 
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.cuentaTieneAsocioCompleto(con,codigoCuenta);
	}
	
	/**
	 * 
	 */
	public InfoDatosInt obtenerEstadoDetalleCargo(Connection con, double codigoDetalleCargo) {
		
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerEstadoDetalleCargo(con,codigoDetalleCargo);
	}
	
	/**
	 * 
	 */
	public boolean esResponsableFacturado(Connection con, int codigoSubCuenta) 
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.esResponsableFacturado(con,codigoSubCuenta);
	}
	
	/**
	 * 
	 */
	public boolean esResponsableFacturado(Connection con, int codigoIngreso, int codigoConvenio) 
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.esResponsableFacturado(con,codigoIngreso,codigoConvenio);
	}
	
	/**
	 * 
	 */
	public ArrayList obtenerCodigoSolicitudesPendienteCargo(Connection con, int codigoIngreso) 
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerCodigoSolicitudesPendienteCargo(con,codigoIngreso);
	}
	
	/**
	 * 
	 */
	public ArrayList obtenerCodigoSolicitudesPendienteCargo(Connection con, int codigoIngreso, int codigoConvenio) 
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerCodigoSolicitudesPendienteCargo(con,codigoIngreso,codigoConvenio);
	}

	/**
	 * 
	 */
	public ArrayList<DtoRequsitosPaciente> obtenerRequisitosPacienteConvenio(Connection con, int codigoConvenio,int codigoViaIngreso) 
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerRequisitosPacienteConvenio(con,codigoConvenio,codigoViaIngreso);
	}

	/**
	 * 
	 */
	public ArrayList<DtoSubCuentas> obtenerResponsablesSolServArt(Connection con, int numeroSolicitud, int codArtiServ, boolean esServicio) 
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerResponsablesSolServArt(con,numeroSolicitud,codArtiServ,esServicio);
	}
	
	/**
	 * Método que consulta los convenios pyp de un ingreso
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerConveniosPypIngreso(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerConveniosPypIngreso(con, campos);
	}
	
	/**
	 * Método que consulta el convenio PYP que tenga asociada la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public DtoConvenio obtenerConvenioPypSolicitud(Connection con,String numeroSolicitud)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerConvenioPypSolicitud(con, numeroSolicitud);
	}

	/**
	 * 
	 */
	public HashMap<String, Object> consultarIngresosValidos(Connection con, int codigoPersona, int centroAtencion) 
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.consultarIngresosValidos(con, codigoPersona,centroAtencion);
	}
	
	/**
	 * Método que consulta los tipos de consulta
	 * @param con
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerTiposConsulta(Connection con)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerTiposConsulta(con);
	}
	
	/**
	 * 
	 */
	public int obtenerCentroCostoCuenta(Connection con,int id)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerCentroCostoCuenta(con, id);
	}
	

	/**
	 * 
	 */
	public boolean insertarInformacionParametrizableValoraciones(Connection con, HashMap informacionParametrizable, int numeroSolicitud) 
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.insertarInformacionParametrizableValoraciones(con, informacionParametrizable,numeroSolicitud);
	}
	
	/**
	 * Método implementado para insertar información de parto para RIPS
	 * @param con
	 * @param infoParto
	 * @param arregloRecienNacido
	 * @return
	 */
	public boolean insertarInformacionPartoParaRips(Connection con,DtoInformacionParto infoParto, ArrayList<DtoInformacionRecienNacido> arregloRecienNacido)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.insertarInformacionPartoParaRips(con, infoParto, arregloRecienNacido);
	}
	
	
	/**
	 * Obtiene el listado de Causas Externas	  
	 * @param con	 
	 * @return llaves(codigo,descripcion)
	 */
	public ArrayList obtenerCausasExternas(Connection con,HashMap campos) 
	{
		
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerCausasExternas(con,campos);
		
	}
	
	/**
	 * Obtiene el listado de Finalidades de la consulta	  
	 * @param con	 
	 * @return llaves(codigo,descripcion)
	 */
	public ArrayList obtenerFinalidadesConsulta(Connection con) 
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerFinalidadesConsulta(con);	
	}
	
	/**
	 * Método implementado para cargar la información del parto ingresada para RIPS
	 * @param con
	 * @param campos
	 * @return
	 */
	public DtoInformacionParto cargarInformacionPartoParaRips(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.cargarInformacionPartoParaRips(con, campos);
	}
	
	/**
	 * Método para obtener el nombre de uan causa externa
	 * @param con
	 * @param codigo
	 * @return
	 */
	public String obtenerNombreCausaExterna(Connection con,int codigo)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerNombreCausaExterna(con, codigo);
	}
	
	/**
	 * Método para obtener el nombre de la finalidad de la consulta
	 * @param con
	 * @param codigo
	 * @return
	 */
	public String obtenerNombreFinalidadConsulta(Connection con,String codigo)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerNombreFinalidadConsulta(con, codigo);
	}
	
	/**
	 * Método que consulta el id del ingreso a partir de una solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String obtenerIdIngresoSolicitud(Connection con,String numeroSolicitud)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerIdIngresoSolicitud(con, numeroSolicitud);
	}
	
	/**
	 * Método para obtener el codigo del paciente de la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public int obtenerCodigoPacienteSolicitud(Connection con,String numeroSolicitud)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerCodigoPacienteSolicitud(con, numeroSolicitud);
	}
	
	
	/**
	 * Obtiene la informacion de la muerte del paciente 
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap obtenerInfoMuertePaciente(Connection con, HashMap parametros)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerInfoMuertePaciente(con,parametros);
	}
	
	/**
	 * Metodo validacion Medico especialista justificacion No Pos y Ocupacion Medico Especialista
	 * @param con
	 * @param usuario
	 * @param paraArticulo
	 * @return
	 */
	public boolean validarEspecialidadProfesionalSalud(Connection con, UsuarioBasico usuario, boolean paraArticulo)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.validarEspecialidadProfesionalSalud(con, usuario, paraArticulo);
	}
	
	/**
	 * Consultar Ingresos por paciente
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public HashMap consultarIngresosXPaciente(Connection con, int codigoPersona)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.consultarIngresosXPaciente(con, codigoPersona);
	}
	
	/**
	 * Consultar Ingresos por paciente por cuenta
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public HashMap consultarIngresosCuentaXPaciente(Connection con, int codigoPersona)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.consultarIngresosCuentaXPaciente(con, codigoPersona);
	}
	
	/**
	 * Método que consulta los signos vitales activos
	 * @param con
	 * @return
	 */
	public ArrayList<SignoVital> cargarSignosVitales(Connection con)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.cargarSignosVitales(con);
	}
	
	/**
	 * Metodo que consulta las clasificaciones de eventos
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap consultarClasificacionesEventos(Connection con, int institucion){
		return SqlBaseUtilidadesHistoriaClinicaDao.consultarClasificacionesEventos(con, institucion);
	}
	
	/**
	 * Método implementado para cargar la revision x sistemas comun y la del un listado de componentes
	 * @param con
	 * @param tiposComponente
	 * @return
	 */
	public ArrayList<DtoRevisionSistema> cargarRevisionesSistemas(Connection con,ArrayList<Integer> tiposComponente)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.cargarRevisionesSistemas(con, tiposComponente);
	}
	
	/**
	 * Método implementado para cargar estados de conciencia
	 * @param con
	 * @param institucion
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> cargarEstadosConciencia(Connection con,int institucion)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.cargarEstadosConciencia(con, institucion);
	}
	
	/**
	 * Método para cargar las conductas de la valoracion
	 * @param con
	 * @return
	 */
	public ArrayList<HashMap<String,Object>> cargarConductasValoracion(Connection con)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.cargarConductasValoracion(con);
	}
	
	/**
	 * Método implementado para cargar los tipos de diagnostico
	 * @param con
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> cargarTiposDiagnostico(Connection con)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.cargarTiposDiagnostico(con);
	}
	
	/**
	 * Método que consuta el nombre de una conducta de valoración
	 * @param con
	 * @param codigo
	 * @return
	 */
	public String obtenerNombreConductaValoracion(Connection con,int codigo)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerNombreConductaValoracion(con, codigo);
	}
	
	/**
	 * Método usado para obtener el codigo de la valoración de urgencias
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public int obtenerCodigoConductaValoracionUrgenciasCuenta(Connection con,String idCuenta)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerCodigoConductaValoracionUrgenciasCuenta(con, idCuenta);
	}
	
	/**
	 * Método implementado para consultar los ultimos diagnosticos del paciente, primero se busca por la evolucion,
	 * luego se busca por las valoraciones.
	 * Nota * En el caso de encontrar diagnosticos, el primer elemento del arreglo corresponderá a el diagnóstico principal
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public ArrayList<Diagnostico> obtenerUltimosDiagnosticosPaciente(Connection con,String codigoPaciente)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerUltimosDiagnosticosPaciente(con, codigoPaciente);
	}
	
	/**
	 * Método que realiza la consulta de la historia menstrual 
	 * @param con
	 * @param codigoPaciente
	 * @param numeroSolicitud
	 * @return
	 */
	public DtoHistoriaMenstrual cargarHistoriaMenstrual(Connection con,String codigoPaciente,String numeroSolicitud)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.cargarHistoriaMenstrual(con, codigoPaciente, numeroSolicitud, DaoFactory.ORACLE);
	}
	
	/**
	 * Método para obtener los rangos edad menarquia
	 * @param con
	 * @param incluirOpcionInvalida
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerRangosEdadMenarquia(Connection con,boolean incluirOpcionInvalida)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerRangosEdadMenarquia(con, incluirOpcionInvalida);
	}
	
	/**
	 * Método para obtener los rangos de la edad de menopausia
	 * @param con
	 * @param incluirOpcionInvalida
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerRangosEdadMenopausia(Connection con,boolean incluirOpcionInvalida)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerRangosEdadMenopausia(con, incluirOpcionInvalida);
	}
	
	/**
	 * Método para obtener los conceptos de menstruacion
	 * @param con
	 * @param incluirOpcionInvalida
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerConceptosMenstruacion(Connection con,boolean incluirOpcionInvalida)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerConceptosMenstruacion(con, incluirOpcionInvalida);
	}
	
	/**
	 * Método para cargar toda la información del componente de oftalmología
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoInstitucion
	 * @return
	 */
	public DtoOftalmologia cargarOftalmologia(Connection con,String numeroSolicitud,int codigoInstitucion)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.cargarOftalmologia(con, numeroSolicitud, codigoInstitucion);
	}
	
	/**
	 * Método implementado para ingrear informacion de oftalmología (Componente)
	 * @param con
	 * @param oftalmologia
	 * @return
	 */
	public ResultadoBoolean ingresarOftalmologia(Connection con,DtoOftalmologia oftalmologia)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.ingresarOftalmologia(con, oftalmologia);
	}
	
	/**
	 * Método implementado para cargar la informacion del componente de pediatría
	 * @param con
	 * @param codigoPaciente
	 * @param numeroSolicitud
	 * @param edadPaciente
	 * @return
	 */
	public DtoPediatria cargarPediatria(Connection con,String codigoPaciente,String numeroSolicitud,int edadPaciente,UsuarioBasico usuario)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.cargarPediatria(con, codigoPaciente, numeroSolicitud, edadPaciente, usuario);
	}
	
	/**
	 * Método implementado para insertar informacion del componente de pediatría
	 * @param con
	 * @param pediatria
	 * @return
	 */
	public ResultadoBoolean ingresarPediatria(Connection con,DtoPediatria pediatria)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.ingresarPediatria(con, pediatria);
	}
	
	/**
	* Método para obtener los signos y sintomas
	* @param con
	* @param institucion
	* @return
	*/
	public ArrayList<HashMap<String, Object>> obtenerSignosSintomas(Connection con,int institucion)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerSignosSintomas(con, institucion);
	}
	
	/**
	* Método para obtener las categorias triage
	* @param con
	* @param institucion
	* @return
	*/
	public ArrayList<HashMap<String, Object>> obtenerCategoriasTriage(Connection con,int institucion)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerCategoriasTriage(con, institucion);
	}
	
	/**
	* Método para obtener los motivos de consulta urgencias
	* @param con
	* @param institucion
	* @return
	*/
	public ArrayList<HashMap<String, Object>> obtenerMotivosConsultaUrgencias(Connection con,int institucion)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerMotivosConsultaUrgencias(con, institucion);
	}
	
	
	/**
	 * Metodo encargado de consultar la instituciones Sirc
	 * pudiendo filtrar por lo diferentes criterios.
	 * @author Felipe Perez
	 * @param con
	 * @param criterios
	 * ------------------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ------------------------------------------
	 * -- institucion --> Requerido
	 * -- codigo --> Opcional
	 * -- descripcion --> Opcional
	 * -- nivelservicio --> Opcional
	 * -- tipored --> Opcional
	 * -- tipoinstreferencia --> Opcional
	 * -- tipoinstambulancia --> Opcional
	 * -- activo --> Opcional
	 * @return ArrayListHashMap
	 *-----------------------------------------
	 *KEY'S DEL HASHMAP DENTRO DEL ARRAYLIST
	 *----------------------------------------- 
	 * codigo,descripcion,nivelservicio,tipored,
	 * tipoinstreferencia,tipoinstambulancia,activo
	 */
	public  ArrayList<HashMap<String, Object>> obtenerInstitucionesSirc(Connection con,HashMap criterios)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerInstitucionesSirc(con, criterios);
	}
	
	/**
	 * Método para consultar el diagnostico de ingreso del paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public String obtenerDescripcionDxIngresoPaciente(Connection con,HashMap campos)
	{
		campos.put("tipoBD", DaoFactory.ORACLE);
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerDescripcionDxIngresoPaciente(con, campos);
	}
	
	/**
	 * Método para consultar el pérfil de farmacoterapia
	 * El HashMap resultante contiene las siguientes llaves:
	 * 	-- cod_axioma_art_"i"
	 *  -- cod_interfaz_art_"i"
	 *  -- nombre_art_"i"
	 *  -- dosis_"i"
	 *  -- frecuencia_"i"
	 *  -- via_"i"
	 *  -- dia_"i"
	 *  -- mes_"i"
	 *  -- paciente
	 *  -- entidad
	 *  -- nro_ingreso
	 *  -- fecha ingreso
	 *  -- nro_doc
	 *  -- sexo
	 *  -- edad
	 *  -- cama
	 *  -- numRegistros
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public HashMap obtenerPerfilFarmacoterapia(Connection con, int codigoIngreso)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerPerfilFarmacoterapia(con, codigoIngreso);
	}
	
	/**
	 * Metodo encargado de consultar todos los ingresos con su informacion
	 * no importa el estado del ingreso o de la cuenta.
	 * @param con
	 * @param codigoPersona
	 * @return HashMap
	 *------------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 *------------------------------------
	 * centroAtencion_,nomCentroAtencion_,
	 * viaIngreso_, numIngreso_,codigoIngreso_,
	 * fechaIngreso_, estadoIngreso_,
	 * nomEstadoIngreso_,numCuenta_, nomEstadoCuenta_,
	 * estadoCuenta_, fechaEgreso_
	 */
	public HashMap consultarTodosIngresosXPaciente(Connection con, int codigoPersona)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.consultarTodosIngresosXPaciente(con, codigoPersona); 
	}
	
	/**
	 * Método para obtener los ultimos diagnósticos de un ingreso
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<Diagnostico> obtenerUltimosDiagnosticoIngreso(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerUltimosDiagnosticoIngreso(con, campos);
	}
	
	/**
	 * 
	 */
	public HashMap obtenerInfoInstitucionXIngreso(Connection con, String numeroIngreso)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerInfoInstitucionXIngreso(con, numeroIngreso);
	}
	
	/**
	 * Método para consultar los datos de la especialidad
	 * @param con
	 * @param especialidad
	 * @return
	 */
	public void consultarEspecialidad(Connection con,DtoEspecialidad especialidad)
	{
		SqlBaseUtilidadesHistoriaClinicaDao.consultarEspecialidad(con, especialidad);
	}
	

	@Override
	public boolean pacienteTieneHisotircosOtrosSistemas(int codigoPersona) 
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.pacienteTieneHisotircosOtrosSistemas(codigoPersona);
	}
	
	public int ultimaEspecialidadEvolucionValoracionIngreso(Connection con,int solicitud)
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.ultimaEspecialidadEvolucionValoracionIngreso(con, solicitud);
	}
	
	/**
	 * @see com.princetonsa.dao.historiaClinica.UtilidadesHistoriaClinicaDao#consultarEstadoFuncionalidadAntecedentes(java.sql.Connection)
	 */
	public  Integer consultarEstadoFuncionalidadAntecedentes(Connection con) throws Exception
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.consultarEstadoFuncionalidadAntecedentes(con);
	}
	
	
	/**
	 * @see com.princetonsa.dao.historiaClinica.UtilidadesHistoriaClinicaDao#consultarEstadoFuncionalidadHistoriaClincia(java.sql.Connection)
	 */
	public  Integer consultarEstadoFuncionalidadHistoriaClincia(Connection con) throws Exception
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.consultarEstadoFuncionalidadHistoriaClincia(con);
	}
	
	/**
	 * @see com.princetonsa.dao.historiaClinica.UtilidadesHistoriaClinicaDao#consultarTipoMonitoreoXCodigo(java.lang.Integer, java.sql.Connection)
	 */
	public  String consultarTipoMonitoreoXCodigo(Integer codigoTipoMonitoreo,Connection con)throws Exception
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.consultarTipoMonitoreoXCodigo(codigoTipoMonitoreo, con);
	}
	
	/**
	 * @see com.princetonsa.dao.historiaClinica.UtilidadesHistoriaClinicaDao#consultarCentroCostosMonitoreoXCodigo(java.lang.Integer, java.sql.Connection)
	 * mt5887
	 */
	public  String consultarCentroCostosMonitoreoXCodigo(Integer codigoCentroCostos,Connection con)throws Exception
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.consultarCentroCostosMonitoreoXCodigo(codigoCentroCostos, con);
	}
	/**
	 * @see com.princetonsa.dao.historiaClinica.UtilidadesHistoriaClinicaDao#consultarMedicamentosOriginales(java.lang.Integer, java.sql.Connection, java.lang.Integer)
	 */
	public DtoMedicamentosOriginales consultarMedicamentosOriginales(Integer codigoEquivalente, Integer codigoAdm, Connection con,Integer ingreso )throws Exception
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.consultarMedicamentosOriginales(codigoEquivalente, codigoAdm, con, ingreso);
	}
	
	/**
	 * @see com.princetonsa.dao.historiaClinica.UtilidadesHistoriaClinicaDao#obtenerUnidadMedidaMedicamentoPrincipal(java.sql.Connection, java.lang.Integer)
	 */
	public  String obtenerUnidadMedidaMedicamentoPrincipal(Connection con, Integer numeroSolicitud, Integer codigoArt) throws Exception{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerUnidadMedidaMedicamentoPrincipal(con, numeroSolicitud, codigoArt);}
	
	/**
	 * @see com.princetonsa.dao.historiaClinica.UtilidadesHistoriaClinicaDao#serviciosHojaQuirurgica(java.sql.Connection, java.lang.Integer)
	 */
	public  ArrayList<String> serviciosHojaQuirurgica(Connection con, Integer numeroSolicitud)throws Exception
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.serviciosHojaQuirurgica(con, numeroSolicitud);
	}
	
	/**
	 * @see com.princetonsa.dao.historiaClinica.UtilidadesHistoriaClinicaDao#obtenerTipoPaciente(java.sql.Connection, java.lang.String)
	 */
	public  String obtenerTipoPaciente(Connection con,String numeroCuenta) throws Exception
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerTipoPaciente(con, numeroCuenta);
	}
	


	/**
	 * @see com.princetonsa.dao.historiaClinica.UtilidadesHistoriaClinicaDao#especialidadUrgencia(java.sql.Connection, java.lang.String)
	 */
	public  String especialidadUrgencia(Connection con,String numeroCuenta) throws Exception
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.especialidadUrgencia(con, numeroCuenta);
	}


	/**
	 * @see com.princetonsa.dao.historiaClinica.UtilidadesHistoriaClinicaDao#obtenerTipoPacienteHosp(java.sql.Connection, java.lang.String)
	 */
	public  String obtenerTipoPacienteHosp(Connection con,String numeroCuenta) throws Exception
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerTipoPacienteHosp(con, numeroCuenta);
	}


	/**
	 * @see com.princetonsa.dao.historiaClinica.UtilidadesHistoriaClinicaDao#obtenerTipoPacienteConsultaExterna(java.sql.Connection, java.lang.String)
	 */
	public  String obtenerTipoPacienteConsultaExterna(Connection con,String numeroCuenta) throws Exception
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerTipoPacienteConsultaExterna(con, numeroCuenta);
	}
	
	/**
	 * @see com.princetonsa.dao.historiaClinica.UtilidadesHistoriaClinicaDao#obtenerFechaMuerte(java.sql.Connection, java.lang.String)
	 */
	public  String obtenerFechaMuerte(Connection con,String codigoPaciente) throws Exception
	{
		return SqlBaseUtilidadesHistoriaClinicaDao.obtenerFechaMuerte(con, codigoPaciente);
	}
	
	/**
	 * @see com.princetonsa.dao.historiaClinica.UtilidadesHistoriaClinicaDao#consultarUltimaValoracionIngreso(java.sql.Connection, int)
	 */
	public  int consultarUltimaValoracionIngresoNoPos(Connection con,int ingreso)
	{
		String seccionWHERE = " c.id_ingreso =  ? ";
		return SqlBaseUtilidadesHistoriaClinicaDao.consultarUltimaValoracionIngresoNoPos(con, ingreso, seccionWHERE);
	}
	
	
}