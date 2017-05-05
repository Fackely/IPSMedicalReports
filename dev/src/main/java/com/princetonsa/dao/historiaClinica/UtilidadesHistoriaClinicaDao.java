/*
 * Mayo 10, 2007
 */
package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.InfoDatosInt;
import util.InfoDatosString;
import util.ResultadoBoolean;

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
 *	Interface usada para gestionar los métodos de acceso a la base
 * de datos en utilidades del modulo de HISTORIA CLINICA
 */
public interface UtilidadesHistoriaClinicaDao 
{
	/**
	 * Método que consulta los datos de accidente de trabajo del triage
	 * @param con
	 * @param consecutivo
	 * @param consecutivoFecha
	 * @return
	 */
	public HashMap consultaDatosAccidenteTrabajoTriage(Connection con,String consecutivo,String consecutivoFecha);
	
	/**
	 * Método que consulta la última evolucion de un ingreso
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public int consultarUltimaEvolucionIngreso(Connection con,int ingreso);
	
	/**
	 * Método que consula la última valoracion de un ingreso
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public int consultarUltimaValoracionIngreso(Connection con,int ingreso);
	
	/**
	 * Método que consulta los motivos sirc parametrizados por institucion
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap obtenerMotivosSirc(Connection con,HashMap campos);
	
	/**
	 * Método que consulta los servicios SIRC de una institucion
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap obtenerServiciosSirc(Connection con,HashMap campos);
	
	/**
	 * Método que consulta los estados de conciencia 
	 * @param con
	 * @param incluyeNoDefinido (si es true se incluirá el registro 'No definido')
	 * @return
	 */
	public HashMap obtenerEstadosConciencia(Connection con,boolean incluyeNoDefinido);
	
	/**
	 * Método que consulta las interpretaciones y la descripcion de los procedimientos de un ingreso
	 * validando el parámetro general "Valida en egreso solicitudes interpretadas:"
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap obtenerInterpretacionProcedimientosIngreso(Connection con,HashMap campos);
	
	/**
	 * Método que consulta la última anamnesis del ingreso
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String getUltimaAnamnesisIngreso(Connection con,String numeroSolicitud);
	
	/**
	 * Método que consulta la ultima referencia  del paciente 
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public String getUltimaReferenciaPaciente(Connection con,String codigoPaciente,String tipoReferencia,String estado);
	
	/**
	 * Método que actualiza el estado de la referencia
	 * @param con
	 * @param numeroReferencia
	 * @param ingreso
	 * @param estado
	 * @param loginUsuario
	 * @return
	 */
	public boolean actualizarEstadoReferencia(Connection con,String numeroReferencia,String ingreso,String estado,String loginUsuario);
	
	/**
	 * Método que consulta el estado de la referencia de un ingreso
	 * Nota* Si retorna cadena vacía quiere decir que ese ingreso no tiene
	 * referencia asociada
	 * @param con
	 * @param campos
	 * @return
	 */
	public String getEstadoReferenciaXIngreso(Connection con,HashMap campos);
	
	/**
	 * Método para consultar el diagnostico de ingreso que se debe postular
	 * en la valoracion de hospitalizacion en el caso de que se haya ingresado
	 * una referencia externa y el origen de la admision sea remitido
	 * @param con
	 * @param codigoCuenta
	 * Se retorna acronimo + separadorSplit + tipoCie + separadorSplit + nombre
	 * Si no encuentra nada retorna cadena vacía
	 */
	public String getDxIngresoDeReferencia(Connection con,String codigoCuenta);
	
	/**
	 * Método que consulta el número de referencia de un ingreso dependiendo del tipo de referencia
	 * Nota: En el caso de que no encuentre nada retorna una cadena vacía
	 * @param con
	 * @param idIngreso
	 * @param tipoReferencia
	 * @return
	 */
	public String getNumeroReferenciaIngreso(Connection con,String idIngreso,String tipoReferencia);
	
	/**
	 * Método que verifica si hay una contrarreferencia para dado un número de referencia
	 * @param con
	 * @param numeroReferencia
	 * @return
	 */
	public boolean existeContrarreferencia(Connection con,String numeroReferencia);
	
	/***
	 * Método que consulta los datos de la referencia de un ingreso para postular un mensaje
	 * de advertencia 
	 * @param con
	 * @param campos
	 * @return
	 */
	public String getMensajeReferenciaParaValidacion(Connection con,HashMap campos);

	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public InfoDatosInt obtenerEstadoCuenta(Connection con, int idCuenta);

	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public InfoDatosInt obtenerCentroAtencionCuenta(Connection con, int idCuenta);

	/**
	 * 
	 * @param con 
	 * @param codigoIngreso
	 * @param todos 
	 * @param excluirResponsables 
	 * @param pyp
	 * @return
	 */
	public ArrayList<DtoSubCuentas> obtenerResponsablesIngreso(Connection con, int codigoIngreso, boolean todos, String[] excluirResponsables,boolean pyp, String subCuenta,int codigoViaIngreso) throws BDException;

	

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @param estados 
	 * @return
	 */
	public ArrayList<DtoSolicitudesSubCuenta> obtenerSolicitudesSubCuenta(Connection con, String subCuenta, int[] estados,boolean incluirPaquetizadas,boolean agruparPortatiles);
	
	/**
	 * 
	 * @param con
	 * @param codigoSubcuenta
	 * @param codigoConvenio 
	 * @return
	 */
	public DtoSubCuentas obtenerResponsable(Connection con, int codigoIngreso, int codigoConvenio);

	/**
	 * 
	 * @param con
	 * @param codigoSubcuenta
	 * @return
	 */
	public DtoSubCuentas obtenerResponsable(Connection con, int codigoSubcuenta) throws BDException;
	
	
	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public InfoDatosString obtenerEstadoIngreso(Connection con, int codigoIngreso);
	
	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public int cuentaTieneAsocioCompleto(Connection con, int codigoCuenta);
	
	/**
	 * 
	 * @param con
	 * @param codigoCargo
	 * @return
	 */
	public InfoDatosInt obtenerEstadoDetalleCargo(Connection con, double codigoDetalleCargo);
	
	/**
	 * 
	 * @param con
	 * @param codigoSubCuenta
	 * @return
	 */
	public boolean esResponsableFacturado(Connection con, int codigoSubCuenta);
	
	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @param codigoConvenio
	 * @return
	 */
	public boolean esResponsableFacturado(Connection con, int codigoIngreso, int codigoConvenio);
	
	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public ArrayList obtenerCodigoSolicitudesPendienteCargo(Connection con, int codigoIngreso);
	
	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @param codigoConvenio
	 * @return
	 */
	public ArrayList obtenerCodigoSolicitudesPendienteCargo(Connection con, int codigoIngreso, int codigoConvenio);

	/**
	 * 
	 * @param con
	 * @param codigoConvenio
	 * @param codigoViaIngreso
	 * @return
	 */
	public ArrayList<DtoRequsitosPaciente> obtenerRequisitosPacienteConvenio(Connection con, int codigoConvenio,int codigoViaIngreso);

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codArtiServ
	 * @param esServicio
	 * @return
	 */
	public ArrayList<DtoSubCuentas> obtenerResponsablesSolServArt(Connection con, int numeroSolicitud, int codArtiServ, boolean esServicio);
	
	/**
	 * Método que consulta los convenios pyp de un ingreso
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerConveniosPypIngreso(Connection con,HashMap campos);
	
	/**
	 * Método que consulta el convenio PYP que tenga asociada la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public DtoConvenio obtenerConvenioPypSolicitud(Connection con,String numeroSolicitud);

	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @param centroAtencion
	 * @return
	 */
	public HashMap<String, Object> consultarIngresosValidos(Connection con, int codigoPersona, int centroAtencion);
	
	/**
	 * Método que consulta los tipos de consulta
	 * @param con
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerTiposConsulta(Connection con);
	
	/**
	 * Metodo que retorna el codigo del cento de costo dada la id de la cuenta
	 * @param con
	 * @param id
	 * @return
	 */
	public int obtenerCentroCostoCuenta(Connection con,int id);

	/**
	 * 
	 * @param con
	 * @param informacionParametrizable
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean insertarInformacionParametrizableValoraciones(Connection con, HashMap informacionParametrizable, int numeroSolicitud);

	/**
	 * Método implementado para insertar información de parto para RIPS
	 * @param con
	 * @param infoParto
	 * @param arregloRecienNacido
	 * @return
	 */
	public boolean insertarInformacionPartoParaRips(Connection con,DtoInformacionParto infoParto, ArrayList<DtoInformacionRecienNacido> arregloRecienNacido);
	
	/**
	 * Obtiene el listado de Causas Externas	  
	 * @param con	 
	 * @return llaves(codigo,descripcion)
	 */
	public ArrayList obtenerCausasExternas(Connection con,HashMap campos);
	
	/**
	 * Obtiene el listado de Finalidades de la consulta	  
	 * @param con	 
	 * @return llaves(codigo,descripcion)
	 */
	public ArrayList obtenerFinalidadesConsulta(Connection con);
	
	/**
	 * Método implementado para cargar la información del parto ingresada para RIPS
	 * @param con
	 * @param campos
	 * @return
	 */
	public DtoInformacionParto cargarInformacionPartoParaRips(Connection con,HashMap campos);
	
	/**
	 * Método para obtener el nombre de uan causa externa
	 * @param con
	 * @param codigo
	 * @return
	 */
	public String obtenerNombreCausaExterna(Connection con,int codigo);
	
	/**
	 * Método para obtener el nombre de la finalidad de la consulta
	 * @param con
	 * @param codigo
	 * @return
	 */
	public String obtenerNombreFinalidadConsulta(Connection con,String codigo);
	
	/**
	 * Método que consulta el id del ingreso a partir de una solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String obtenerIdIngresoSolicitud(Connection con,String numeroSolicitud);
	
	/**
	 * Método para obtener el codigo del paciente de la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public int obtenerCodigoPacienteSolicitud(Connection con,String numeroSolicitud);
	
	
	/**
	 * Obtiene la informacion de la muerte del paciente 
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap obtenerInfoMuertePaciente(Connection con, HashMap parametros); 
	
	/**
	 * Metodo Validacion Medico especialista jusitificacion No Pos y Ocupacion Medico Especialista
	 * @param con
	 * @param usuario
	 * @param paraArticulo
	 * @return
	 */
	public boolean validarEspecialidadProfesionalSalud(Connection con, UsuarioBasico usuario, boolean paraArticulo);
	
	/**
	 * Consultar Ingresos por paciente
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public HashMap consultarIngresosXPaciente(Connection con, int codigoPersona);
	
	/**
	 * Consultar Ingresos por paciente por cuenta
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public HashMap consultarIngresosCuentaXPaciente(Connection con, int codigoPersona);
	
	/**
	 * Método que consulta los signos vitales activos
	 * @param con
	 * @return
	 */
	public ArrayList<SignoVital> cargarSignosVitales(Connection con);
	
	/**
	 * Metodo que consulta las clasificaciones de eventos
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap consultarClasificacionesEventos(Connection con, int institucion);
	
	/**
	 * Método implementado para cargar la revision x sistemas comun y la del un listado de componentes
	 * @param con
	 * @param tiposComponente
	 * @return
	 */
	public ArrayList<DtoRevisionSistema> cargarRevisionesSistemas(Connection con,ArrayList<Integer> tiposComponente);
	
	/**
	 * Método implementado para cargar estados de conciencia
	 * @param con
	 * @param institucion
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> cargarEstadosConciencia(Connection con,int institucion);
	
	/**
	 * Método para cargar las conductas de la valoracion
	 * @param con
	 * @return
	 */
	public ArrayList<HashMap<String,Object>> cargarConductasValoracion(Connection con);
	
	/**
	 * Método implementado para cargar los tipos de diagnostico
	 * @param con
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> cargarTiposDiagnostico(Connection con);
	
	/**
	 * Método que consuta el nombre de una conducta de valoración
	 * @param con
	 * @param codigo
	 * @return
	 */
	public String obtenerNombreConductaValoracion(Connection con,int codigo);
	
	/**
	 * Método usado para obtener el codigo de la valoración de urgencias
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public int obtenerCodigoConductaValoracionUrgenciasCuenta(Connection con,String idCuenta);
	
	/**
	 * Método implementado para consultar los ultimos diagnosticos del paciente, primero se busca por la evolucion,
	 * luego se busca por las valoraciones.
	 * Nota * En el caso de encontrar diagnosticos, el primer elemento del arreglo corresponderá a el diagnóstico principal
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public ArrayList<Diagnostico> obtenerUltimosDiagnosticosPaciente(Connection con,String codigoPaciente);
	
	/**
	 * Método que realiza la consulta de la historia menstrual 
	 * @param con
	 * @param codigoPaciente
	 * @param numeroSolicitud
	 * @return
	 */
	public DtoHistoriaMenstrual cargarHistoriaMenstrual(Connection con,String codigoPaciente,String numeroSolicitud);
	
	/**
	 * Método para obtener los rangos edad menarquia
	 * @param con
	 * @param incluirOpcionInvalida
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerRangosEdadMenarquia(Connection con,boolean incluirOpcionInvalida);
	
	/**
	 * Método para obtener los rangos de la edad de menopausia
	 * @param con
	 * @param incluirOpcionInvalida
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerRangosEdadMenopausia(Connection con,boolean incluirOpcionInvalida);
	
	/**
	 * Método para obtener los conceptos de menstruacion
	 * @param con
	 * @param incluirOpcionInvalida
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerConceptosMenstruacion(Connection con,boolean incluirOpcionInvalida);
	
	/**
	 * Método para cargar toda la información del componente de oftalmología
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoInstitucion
	 * @return
	 */
	public DtoOftalmologia cargarOftalmologia(Connection con,String numeroSolicitud,int codigoInstitucion);
	
	/**
	 * Método implementado para ingrear informacion de oftalmología (Componente)
	 * @param con
	 * @param oftalmologia
	 * @return
	 */
	public ResultadoBoolean ingresarOftalmologia(Connection con,DtoOftalmologia oftalmologia);
	
	/**
	 * Método implementado para cargar la informacion del componente de pediatría
	 * @param con
	 * @param codigoPaciente
	 * @param numeroSolicitud
	 * @param edadPaciente
	 * @return
	 */
	public DtoPediatria cargarPediatria(Connection con,String codigoPaciente,String numeroSolicitud,int edadPaciente,UsuarioBasico usuario);
	
	/**
	 * Método implementado para insertar informacion del componente de pediatría
	 * @param con
	 * @param pediatria
	 * @return
	 */
	public ResultadoBoolean ingresarPediatria(Connection con,DtoPediatria pediatria);
	
	/**
	* Método para obtener los signos y sintomas
	* @param con
	* @param institucion
	* @return
	*/
	public ArrayList<HashMap<String, Object>> obtenerSignosSintomas(Connection con,int institucion);
	
	/**
	* Método para obtener las categorias triage
	* @param con
	* @param institucion
	* @return
	*/
	public ArrayList<HashMap<String, Object>> obtenerCategoriasTriage(Connection con,int institucion);
	
	/**
	* Método para obtener los motivos de consulta urgencias
	* @param con
	* @param institucion
	* @return
	*/
	public ArrayList<HashMap<String, Object>> obtenerMotivosConsultaUrgencias(Connection con,int institucion);
	
	
	
	
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
	public ArrayList<HashMap<String, Object>> obtenerInstitucionesSirc(Connection con,HashMap criterios);
	
	/**
	 * Método para consultar el diagnostico de ingreso del paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public String obtenerDescripcionDxIngresoPaciente(Connection con,HashMap campos);
	
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
	public HashMap obtenerPerfilFarmacoterapia(Connection con, int codigoIngreso);
	
	
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
	public HashMap consultarTodosIngresosXPaciente(Connection con, int codigoPersona);
	
	/**
	 * Método para obtener los ultimos diagnósticos de un ingreso
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<Diagnostico> obtenerUltimosDiagnosticoIngreso(Connection con,HashMap campos);
	
	/**
	 * 
	 * @param con
	 * @param numeroIngreso
	 * @return
	 */
	public HashMap obtenerInfoInstitucionXIngreso(Connection con, String numeroIngreso);
	
	/**
	 * Método para consultar los datos de la especialidad
	 * @param con
	 * @param especialidad
	 * @return
	 */
	public void consultarEspecialidad(Connection con,DtoEspecialidad especialidad);

	/**
	 * 
	 * @param codigoPersona
	 * @return
	 */
	public boolean pacienteTieneHisotircosOtrosSistemas(int codigoPersona);
	
	public int ultimaEspecialidadEvolucionValoracionIngreso(Connection con,int solicitud);
	
	/**
	 * @param con
	 * @return estado de funcionalidad de antecedentes 
	 */
	public  Integer consultarEstadoFuncionalidadAntecedentes(Connection con) throws Exception;
	
	/**
	 * @param con
	 * @return estado de la funcionalidad de historia clinica
	 * @throws Exception
	 */
	public  Integer consultarEstadoFuncionalidadHistoriaClincia(Connection con) throws Exception;
	
	
	/**
	 * Metodo que consulta el nombre del tipo de monitoreo 
	 * @param codigoTipoMonitoreo
	 * @param con
	 * @return nombre del tipo de monitoreo
	 * @throws Exception
	 */
	public  String consultarTipoMonitoreoXCodigo(Integer codigoTipoMonitoreo,Connection con)throws Exception;
	
	/**
	 * Metodo que consulta el nombre del centro de costos de monitoreo 
	 * @param codigoCentroCostos
	 * @param con
	 * @return nombre del centro de costos de monitoreo
	 * @throws Exception
	 * MT5887
	 */
	public  String consultarCentroCostosMonitoreoXCodigo(Integer codigoCentroCostos,Connection con)throws Exception;
	/**
	 * @param codigoEquivalente
	 * @param con
	 * @param ingreso
	 * @return medicamento original
	 * @throws Exception
	 */
	public DtoMedicamentosOriginales consultarMedicamentosOriginales(Integer codigoEquivalente, Integer codigoAdm, Connection con,Integer ingreso )throws Exception;
	
	/**
	 * @param con
	 * @param numeroSolicitud
	 * @return Unidad de medida
	 * @throws Exception
	 */
	public  String obtenerUnidadMedidaMedicamentoPrincipal(Connection con, Integer numeroSolicitud, Integer codigoArt) throws Exception;
	
	
	/**
	 * @param con
	 * @param numeroSolicitud
	 * @return servicios de hoja QX
	 * @throws Exception
	 */
	public  ArrayList<String> serviciosHojaQuirurgica(Connection con, Integer numeroSolicitud)throws Exception;
	
	/**
	 * @param con
	 * @param numeroCuenta
	 * @return
	 * @throws Exception
	 */
	public  String obtenerTipoPaciente(Connection con,String numeroCuenta) throws Exception;
	
	/**
	 * @param con
	 * @param numeroCuenta
	 * @return
	 * @throws Exception
	 */
	public  String especialidadUrgencia(Connection con,String numeroCuenta) throws Exception;
	
	/**
	 * @param con
	 * @param numeroCuenta
	 * @return
	 * @throws Exception
	 */
	public  String obtenerTipoPacienteHosp(Connection con,String numeroCuenta) throws Exception;
	
	/**
	 * @param con
	 * @param numeroCuenta
	 * @return
	 * @throws Exception
	 */
	public  String obtenerTipoPacienteConsultaExterna(Connection con,String numeroCuenta) throws Exception;
	
	/**
	 * @param con
	 * @param codigoPaciente
	 * @return
	 * @throws Exception
	 */
	public  String obtenerFechaMuerte(Connection con,String codigoPaciente) throws Exception;
	
	/**
	 * @param con
	 * @param ingreso
	 * @param seccionWHERE
	 * @return
	 */
	public  int consultarUltimaValoracionIngresoNoPos(Connection con,int ingreso);
	
	
}