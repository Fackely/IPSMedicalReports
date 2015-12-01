/*
 * Creado en 18/08/2004
 *
 */
package com.princetonsa.actionform;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dto.administracion.DtoAreaAperturaCuentaAutoPYP;
import com.princetonsa.dto.administracion.DtoFirmasValoresPorDefecto;
import com.princetonsa.dto.odontologia.DtoDetalleHallazgoProgramaServicio;
import com.princetonsa.dto.odontologia.DtoMotivoDescuento;
import com.princetonsa.dto.tesoreria.DtoConceptosIngTesoreria;
import com.princetonsa.mundo.glosas.ParametrosFirmasImpresionResp;
import com.servinte.axioma.orm.EsquemasTarifarios;
import com.servinte.axioma.orm.TiposPaciente;
import com.servinte.axioma.orm.ViasIngreso;

/**
 * @author Juan David Ramï¿½rez Lï¿½pez
 *
 * Princeton S.A.
 */
public class ValoresPorDefectoForm extends ValidatorForm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1687817478871502259L;


	private Logger logger = Logger.getLogger(ValoresPorDefectoForm.class);
	
	
	/**
     * Mensaje que informa sobre la generacion de la aplicacion de pagos facturas varias
     */
    private ResultadoBoolean mensaje = new ResultadoBoolean(false);
	
	/**
	 * Manejo de estados para el flujo de la funcionalidad
	 */
	private String estado;
	/**
	 * cï¿½digo del modulo, por el cual se
	 * filtran los parametros que correspondan
	 * al mismo
	 */
	private int modulo;
	/**
	 * nombre del modulo
	 */
	private String nombreModulo;
	
	/**
	 * Direcciï¿½n por defecto en el ingreso del paciente
	 */
	private String direccionPaciente;
	
	/**
	 * Centro de costo por defecto en el ingreso del paciente
	 */
	private String centroCostoConsultaExterna;
	
	/**
	 * Convenio fisalud
	 */
	private String convenioFisalud;
	
	/**
	 * Convenio fisalud
	 */
	private String formaPagoEfectivo;
	
	/**
	 * 
	 */
	private String servicioTemporal;
	
	/**
	 * 
	 */
	private int indiceServicioSeleccionado;
	
	/**
	 * 
	 */
	private String serviciosSeleccionados;
	
	/**
	 * 
	 */
	private String codigoManualEstandarBusquedaServicios;
	
	/**
	 * Causa Externa por defecto de las valoraciones
	 */
	private String causaExterna;
	
	/**
	 * Finalidad de la consulta por defecto
	 */
	private String finalidad;
	
	/**
	 * Pais de nacimiento por defecto en el ingreso del paciente
	 */
	private String paisNacimiento;
	
	/**
	 * Pais de residencia por defecto en el ingreso del paciente
	 */
	private String paisResidencia;
	
	/**
	 * Ciudad de nacimiento por defecto en el ingreso del paciente
	 */
	private String ciudadNacimiento;
	
	/**
	 * Ciudad de residencia por defecto en el ingreso del paciente
	 */
	private String ciudadVivienda;
	
	/**
	 * Domicilio por defecto en el ingreso del paciente Rural ï¿½ Urbano
	 */
	private String zonaDomicilio;
	
	/**
	 * Ocupaciï¿½n por defecto en el ingreso del paciente
	 */
	private String ocupacion;
	
	/**
	 * Tipo de identificaciï¿½n por defecto en el ingreso del paciente
	 */
	private String tipoId;
	
	/**
	 * Tipo de ingreso de la fecha de nacimiento Cï¿½lculo por meses y aï¿½os ï¿½ ingresando la fecha exacta
	 */
	private String fechaNacimiento;
	
	/**
	 * Se desea ï¿½ no capturar el número de historia clï¿½nica
	 */
	private String historiaClinica;
	
	/**
	 * Desea o no que le aparezca el mensaje de verificaciï¿½n a la salida
	 */
	private String flagCentinela;
	
	/**
	 * Default del centro de costo de urgencias
	 */
	private String centroCostoUrgencias;
	
	/**
	 * Default de la ocupaciï¿½n solicitada en solicitud de interconsultas
	 */
	private String ocupacionSolicitada;
	
	/**
	 * Cï¿½digo del estado de la cama
	 * en el que queda despues de hacer un egreso
	 */
	private String codigoEstadoCama;
	
	/**
	 * Valor del UVR para los servicios
	 */
	private String valorUVR;
	
	
//	Se deshabilita debido a q ya no se esta utilizando el parametro Carnet_Requerido en el modulo Manejo Paciente
	/**
	 * Indica si es requerido ingresar número
	 * del carnet en la cuenta del paciente
	 */
	/**private String carnetRequerido;*/
	
	
	/**
	 * Codigo de servicio asignado a las solicitudes de farmacia
	 */
	private String codigoServicioFarmacia;
	
	/**
	 * Parï¿½metro que indica el tipo de validaciï¿½n del estado
	 * mï¿½dico de las solicitudes para dar egreso (Sin importar el tipo de egreso)
	 * Si el parï¿½metro se encuentra en "SI" quiere
	 * decir que se deve validar el estado "Interpretada"
	 * Si el parï¿½metro se encuentra en "NO" quiere
	 * decir que se deve validar el estado "Interpretada" o "Respondida"
	 */
	private String validarEstadoSolicitudesInterpretadas;

	/**
	 * Parï¿½metro que indica si se debe validar en facturaciï¿½n
	 * el hecho que los contratos estï¿½n vencidos
	 */
	private String validarContratosVencidos;
	
	/**
	 * Parï¿½metro que indica el tipo de manejo que se le va a dar
	 * a los topes de facturaciï¿½n
	 */
	private String manejoTopesPaciente;
	
	/**
	 * Centro de costo por defecto en el ingreso del paciente
	 */
	private String centroCostoAmbulatorios;
	
	/**
	 * Justificacion de Servicios Requerida
	 */
	private String justificacionServiciosRequerida;
	
	/**
	 * Manejo de la cantidad despachada en la farmacia
	 */
	private String ingresoCantidadFarmacia;
	
	/**
	 * Indicador de manejo de Rips por factura
	 *
	 */
	private String ripsPorFactura;
	
	
	/**
	 * 
	 */
	private String confirmarAjustesPooles;
	/**
	 * fecha corte saldo inicial cartera
	 */
	private String fechaCorteSaldoInicial;
	
	/**
	 * fecha corte saldo inicial cartera capitacion
	 */
	private String fechaCorteSaldoInicialCapitacion;
	
	/**
	 * tope consecutivo CxC saldo inicial.
	 */
	private String topeConsecutivoCxC;
	
	/**
	 * tope consecutivo CxC saldo inicial capitaciï¿½n.
	 */
	private String topeConsecutivoCxCCapitacion;
	
	/**
	 * numero de lineas por pager
	 */
	private String maxPageItems;
	
	/**
	 * numero de lineas por pager
	 */
	private String maxPageItemsEpicrisis;
	
	/**
	 * almacena el estado de la generaciï¿½n de excepciones
	 * de farmacia automatica, SI ï¿½ NO
	 */
	private String genExcepcionesFarmAut;
	
	/**
	 * Alamcena el envï¿½o de rips para consulta externa
	 */
	private String excepcionRipsConsultorios;
	
	/**
	 * Almacena el indicativo de ajuste de cuenta de cobro radicada
	 */
	private String ajustarCuentaCobroRadicada;
	
	/**
	 * Almacena el indicativo de cerrar la cuenta en la anulaciï¿½n de la factura
	 */
	private String cerrarCuentaAnulacionFactura;
	
	/**
	 * Almacena el barrio de residencia
	 */
	private String barrioResidencia;
	
	/**
	 * Almacena el barrio de residencia
	 */
	private String materialesPorActo;

	/**
	 * Almacena la hora inicial para la programacion de Salas de Cirugia 
	 */
	private String horaInicioProgramacionSalas;

	/**
	 * Almacena la hora Final para la programacion de Salas de Cirugia 
	 */
	private String horaFinProgramacionSalas;

	/**
	 * Almacena la hora inicial para el primer turno de enfermerï¿½a 
	 */
	private String horaInicioPrimerTurno;

	/**
	 * Almacena la hora Final para la programacion de Salas de Cirugia 
	 */
	private String horaFinUltimoTurno;

	/**
	 * Almacena el codigo de la especialidad anestesiï¿½logï¿½a 
	 */
	private String codigoEspecialidadAnestesiologia;	
	/**
	 * almacena los registros de las
	 * etiquetas de los parametros segun 
	 * el modulo
	 */
	private HashMap mapaEtiquetas;
	/**
	 * manejo del consecutivo de tansacciones inventario
	 */
	private String manejoConsecTransInv;
	/**
	 * porcentaje de alerta en diferencia en los costos de inventarios
	 */
	private String porcentajesCostoInv;
	/**
	 * cï¿½digo de transacciï¿½n utilizado para las solicitudes de pacientes
	 */
	private String codigoTransSolicPacientes;
	/**
	 * cï¿½digo de transacciï¿½n utilizado para las devoluciones de pacientes
	 */
	private String codigoTransDevolPacientes;
	/**
	 * Cï¿½digo de transacciï¿½n utilizado para los pedidos
	 */
	private String codigoTransPedidos;
	/**
	 * Cï¿½digo de transacciï¿½n de la devoluciï¿½n de pedidos
	 */
	private String codigoTransDvolPedidos;
	
	/**
	 * Cï¿½digo de transacciï¿½n de la devoluciï¿½n de pedidos
	 */
	private String codigoTransCompras;
	
	/**
	 * Cï¿½digo de transacciï¿½n de la devoluciï¿½n de pedidos
	 */
	private String codigoTransDevolCompras;
	/**
	 * permitir modificar fecha de elaboraciï¿½n de inventario
	 */
	private String permitirModificarFechaInv;
	/**
	 * porcentaje de alerta del punto de pedido
	 */
	private String porcentajePuntoPedido;
	/**
	 * 
	 */
	private String diasAlertaVigencia;
	/**
	 * Dias previos notificacion de proxima cita de control
	 */
	private String diasPreviosNotificacionProximoControl;
	/**
	 * cï¿½digo transacciï¿½n utilizado para traslado almacenes
	 */
	private String codigoTransTrasladoAlmacenes;
	/**
	 * Desea o no la "Informaciï¿½n Adicional" en el Ingreso de Convenios
	 */
	private String flagInfoAdicIngresoConvenios;
	
	/**
	 * Cï¿½digo ocupaciï¿½n mï¿½dica especialista
	 */
	private String ocupacionMedicoEspecialista;
	/**
	 * Cï¿½digo ocupaciï¿½n mï¿½dica general
	 */
	private String ocupacionMedicoGeneral;
	
	/**
	 * Cï¿½digo ocupaciï¿½n Odontologico
	 */
	private String ocupacionOdontologo;
	
	/**
	 * Cï¿½digo ocupaciï¿½n Auxiliar Odontologico
	 */
	private String ocupacionAuxiliarOdontologo;
	
	/**
	 * Cï¿½digo ocupaciï¿½n enfermera
	 */
	private String ocupacionEnfermera;
	/**
	 * Cï¿½digo ocupaciï¿½n auxiliar enfermerï¿½a
	 */
	private String ocupacionAuxiliarEnfermeria;
	/**
	 * Alertar sobre la existencia de fichas de vigilancia epid. pendientes
	 */
	private String alertaFichasPendientes;
	/**
	 * Alertar sobre la ocurrencia de un caso de vigilancia epidemiologica obligatoria
	 */
	private String alertaCasoVigilancia;
	/**
	 * vigilar los casos de accidente rabico
	 */
	private String vigilarAccRabico;
	
	/**
	 * variable para la variable de MINUTOS DE ESPERA PARA ASIGNAR CITAS CADUCADAS
	 */
	private String minutosEsperaCitaCaduca;
	
	/**
	 * Almacena el tiempo máximo en horas de grabaciï¿½n de registros
	 */
	private String tiempoMaximoGrabacion;
	
	/**
	 * Ingresar cantidad en solicitud de medicamentos
	 */
	private String ingresoCantidadSolMedicamentos;
	
	/**
	 * Almacena el tipo de consecutivo de capitaciï¿½n
	 */
	private String tipoConsecutivoCapitacion;
	
	
	/**
	 * minutosEsperaCuentasProcFact
	 */
	private String minutosEsperaCuentasProcFact;
	
	/**
	 * variable para la variable de Datos Cuenta Requerido Reserva Citas Consulta Externa
	 */
	private String datosCuentaRequeridoReservaCitas;
	
	/**
	 * variable para la variable de red no adscrita
	 */
	private String redNoAdscrita;
	
	/**
	 * Campo para la variable número máximo de dï¿½as tratamiento en solicitudes de medicamentos
	 */
	private String numDiasTratamientoMedicamentos;
	
	/**
	 * Campo para la variable número máximo de dï¿½as para generar solicitudes de ordenes ambulatorias de articulos
	 */
	private String numDiasGenerarOrdenesArticulos;
	
	/**
	 * Campo para la variable número de dï¿½as de egreso para generar ï¿½rdenes ambulatorias
	 */
	private String numDiasEgresoOrdenesAmbulatorias;
	
	/**
	 * Campo que almacena las horas de caducidad de las referencias externas
	 */
	private String horasCaducidadReferenciasExternas;
	
	/**
	 * Campo que indica si se debe hacer llamado o no la funcionalidad
	 * referencia desde otras funcionalidades
	 */
	private String llamadoAutomaticoReferencia;

	/**
	 * 
	 */
	private String interfazPacientes;
	
	/**
	 * 
	 */
	private String articuloInventario;
	
	/**
	 * 
	 */
	private String interfazCompras;
	
	/**
	 * 
	 */
	private String loginUsuario;
	
	/**
	 * 
	 */
	private String interfazAbonosTesoreria;
	
	/**
	 * almacena los n centros de costo de terceros
	 */
	private HashMap centroCostoTerceros= new HashMap();
	
	/**
	 * almacena los n horas reporceso
	 */
	private HashMap horasReproceso= new HashMap();
	
	/**
	 * variable para parametro validar edad responsable paciente
	 */
	private String validarEdadResponsablePaciente;

	/**
	 * Variable para parametro validar edad deudor paciente
	 */
	private String validarEdadDeudorPaciente;
	
	/**
	 * Variable para parametro Aï¿½os base edad adulta
	 */
	private String aniosBaseEdadAdulta;
	
	/**
	 * Variable para parametro validar egreso administrativo para paquetizar
	 */
	private String validarEgresoAdministrativoPaquetizar;
	
	/**
	 * Variable para parametro Maxima cantidad de paquetes por ingreso paciente
	 */
	private String maxCantidPaquetesValidosIngresoPaciente;
	
	/**
	 * Variable para parametro Asignar valor paciente con el valor de los cargos
	 */
	private String asignarValorPacienteValorCargos;
	
	/**
	 * Variable para parametro Interfaz Cartera Pacientes
	 */
	private String interfazCarteraPacientes;
	
	/**
	 * Variable para parametro Interfaz Contable Facturas
	 */
	private String interfazContableFacturas;
	
	/**
	 * Variable para parametro Interfaz Terceros
	 */
	private String interfazTerceros;
	
	/**
	 * Variable para parametro Consolidar Cargos
	 */
	private String consolidarCargos;
	
	/**
	 * maneja conversion moneda
	 */
	private String manejaConversionMonedaExtranjera;
	
	/**
	 * impresion media carta
	 */
	private String impresionMediaCarta;
	
	/**
	 * Hora corte historico camas
	 */
	private String horaCorteHistoricoCamas;
	
	
	/**
	 * tiempo limite de la fecha de la reserva.
	 */
	private String minutosLimiteAlertaReserva;
	
	/**
	 * tiempo limite de la fecha de la de registro de salida para las
	 * camas con estado "con salida".
	 */
	private String minutosLimiteAlertaPacienteConSalidaUrgencias;
	

	/**
	 * tiempo limite de la fecha de la de registro de salida para las
	 * camas con estado "con salida".
	 */
	private String minutosLimiteAlertaPacienteConSalidaHospitalizacion;
	

	/**
	 * tiempo de alerta de urgencias para las camas con estado
	 * "pendiente por remitir"
	 */
	private String minutosLimiteAlertaPacientePorRemitirUrgencias;
	

	/**
	 * tiempo de alerta de hospitalizacion para las camas con estado
	 * "pendiente por remitir"
	 */
	private String minutosLimiteAlertaPacientePorRemitirHospitalizacion;
	
	/**
	 * Idenitifcados institucion para reportar archivos colsanitas
	 */
	private String identificadorInstitucionArchivosColsanitas;
	
	
	/**
	 * Interfaz Rips
	 * */
	private String interfazRips;
	
	/**
	 * Entidad maneja hospital dï¿½a
	 */
	private String entidadManejaHospitalDia;
	
	/**
	 * Entidad Maneja Rips
	 */
	private String entidadManejaRips;
	
	/**
	 * Valoracion de urgencias en hospitalizacion
	 */
	private String valoracionUrgenciasEnHospitalizacion;
	
	/**
	 * 
	 */
	private String liquidacionAutomaticaCirugias;
	
	/**
	 * 
	 */
	private String liquidacionAutomaticaNoQx;
	
	/**
	 * 
	 */
	private String manejoProgramacionSalasSolicitudesDyt;
	/**
	 * 
	 */
	private String requeridaDescripcionEspecialidadCirugias;
	/**
	 * 
	 */
	private String requeridaDescripcionEspecialidadNoCruentos;
	/**
	 * 
	 */
	private String asocioAyudantia;
	/**
	 * 
	 */
	private String indicativoCobrableHonorariosCirugia;
	/**
	 * 
	 */
	private String indicativoCobrableHonorariosNoCruento;
	/**
	 * 
	 */
	private String asocioCirujano;
	/**
	 * 
	 */
	private String asocioAnestesia;
	/**
	 * 
	 */
	private String modificarInformacionDescripcionQuirurgica;
	
	/**
	 * 
	 */
	private String minutosRegistroNotasCirugia;
	
	/**
	 * 
	 */
	private String minutosRegistroNotasNoCruentos;
	
	/**
	 * 
	 */
	private String modificarInformacionQuirurgica;
	
	/**
	 * 
	 */
	private String minutosMaximosRegistroAnestesia;
	
	/**
	 * Campo ubicacion planos entidades subcontratadas
	 */
	private String ubicacionPlanosEntidadesSubcontratadas;

	/**
	 * 
	 */
	private String institucionMultiempresa;
	
	/**
	 * Manejo Especial Instituciones Multiempresa
	 */
	private String manejoEspecialInstitucionesOdontologia;
	/**
	 * 
	 */
	private String archivosPlanosReportes;
	
	/**
	 * 
	 */
	private String archivosPlanosReportesInventarios;
	
	/**
	 * 
	 */
	private String conteosValidosAjustarInventarioFisico;
	
	/**
	 * 
	 */
	private String entidadControlaDespachoSaldosMultidosis;
	
	/**
	 * 
	 */
	private String numeroDiasControlMedicamentosOrdenados;
	
	
	/**
	 * 
	 */
	private String generaEstanciaAutomatica;
	/**
	 * 
	 */
	private String horaGeneraEstanciaAutomatica;
	/**
	 * 
	 */
	private String incluirTipoPacientesCirugiaAmbulatoria;
	
	private String tipoConsecutivoManejar;
	
	/**
	 * 
	 * */
	private String requeridoInfoRipsCargoDirecto;
	
	/**
	 * 
	 */
	private ArrayList<Integer> serviciosManejoTransPrimario;
	
	/**
	 * 
	 */
	private ArrayList<Integer> serviciosManejoTransSecundario;
	
	/**
	 * 
	 */
	private String manejoOxigenoFurips;
	
	
	/**
	 * 
	 */
	private String genForecatRips;
	
	/**
	 * 
	 */
	private String validacionOcupacionJustificacionNoPosArticulos;
	
	/**
	 * 
	 */
	private String validacionOcupacionJustificacionNoPosServicios;
	
	/**
	 * Asigna Valoracion de Cirugia Ambulatoria a Hospitalizacion
	 * */
	private String asignaValoracionCxAmbulaHospita;
	
	/**
	 * 
	 */
	private String interfazContableRecibosCajaERP;
	
	/**
	 * 
	 */
	private String crearCuentaAtencionCitas;
	
	/**
	 * 
	 */
	private String institucionManejaMultasPorIncumplimiento;
	
	/**
	 * 
	 */
	public String bloqueaCitasReservaAsignReprogPorIncump;
	
	/**
	 * 
	 */
	public String bloqueaAtencionCitasPorIncump;
	
	/**
	 * 
	 */
	public String fechaInicioControlMultasIncumplimientoCitas;
	
	/**
	 * 
	 */
	public String valorMultaPorIncumplimientoCitas;
	
	
	/**
	 * 
	 */
	public String numeroMaximoDiasGenOrdenesAmbServicios;
	
	/**
	 * 
	 */
	public String multiploMinGeneracionCita; 
	
	/**
	 * 
	 */
	public String numDiasAntFActualAgendaOd; 
	
	/**
	 * 
	 */
	private String validadAdministracionMedEgresoMedico;
	
	/**
	 * 
	 */
	private String mostrarAntecedentesParametrizadosEpicrisis;
	
	/**
	 * 
	 */
	private String permitirConsultarEpicrisisSoloProf;
	
	
	/**
	 * 
	 */
	private String interfazConsecutivoFacturasOtroSistema;
	
	/**
	 * 
	 */
	private String interfazNutricion;
	
	/**
	 * 
	 */
	private String tiempoMaximoReingresoUrgencias;
	
	/**
	 * 
	 */
	private String tiempoMaximoReingresoHospitalizacion;
	
	/**
	 * 
	 */
	private String permitirFacturarReingresosIndependientes;

	/**
	 * 
	 */
	private String liberarCamaHospitalizacionDespuesFacturar;
	
	/**
	 * 
	 */
	private String controlaInterpretacionProcedimientosEvolucion;

	
	private String validarRegistroEvolucionGenerarOrdenes;
	
	private String pathArchivosPlanosFacturacion;
	
	/**
	 * 
	 */
	private String pathArchivosPlanosFurips;
	
	private String pathArchivosPlanosManejoPaciente;
	
	/**
	 * */
	private String comprobacionDerechosCapitacionObligatoria;
	
	/**
	 * 
	 */
	private String requiereAutorizarAnularFacturas;
	
	/**
	 * 
	 */
	private String validarPoolesFact;
	
	/**
	 * 
	 */
	private String mostrarEnviarEpicrisisEvol;
	
	/**
	 * 
	 */
	private String conceptoParaAjusteEntrada;
	
	/**
	 * 
	 */
	private String conceptoParaAjusteSalida;
	
	/**
	 * 
	 */
	private String permitirModificarConceptosAjuste;
	
	private String diasRestriccionCitasIncumplidas;
	
	/**
	 * 
	 */
	private String permitirModificarFechaSolicitudPedidos;
	
	/**
	 * 
	 */
	private String permitirModificarFechaSolicitudTraslado;
	
	/**
	 * 
	 */
	private String codigoManualEstandarBusquedaArticulos;
	
	/**
	 * Parï¿½metro de hacer requerido comentarios al solicitar
	 */
	private String requeridoComentariosSolicitar;
	
	/**
	 * Parï¿½metro para el número de Días para responder Glosas
	 */
	private String numeroDiasResponderGlosas;
	

	/** Parï¿½metro para el Valor de Generar Ajuste Automatico desde el Registro de la Respuesta */
	private String generarAjusteAutoRegRespuesta;


	/** Parï¿½metro para el Valor de Generar Ajuste Automatico desde el Registro de la Respuesta para Respuestas de Conciliacion */
	private String generarAjusteAutoRegRespuesConciliacion;


	/** Parï¿½metro para el Valor de Formato Impresion Respuesta Glosa */
	private String formatoImpresionRespuesGlosa;
	
	/**
	 * Parï¿½metro para el valor del Formato Impresiï¿½n Conciliaciï¿½n
	 */
	private String formatoImpresionConciliacion;
	
	/**
	 * Parï¿½metro para validar Glosa Reiterada
	 */
	private String validarGlosaReiterada;


	/** Parï¿½metro para el Valor de Formato Impresion Respuesta Glosa */
	private String imprimirFirmasImpresionRespuesGlosa;
	
	/**
	 * Parï¿½metro para Validar Auditor
	 */
	private String validarAuditor;
	
	/**
	 * Parï¿½metro para Validar Usuario Glosa
	 */
	private String validarUsuarioGlosa;
	

	/**
	 * Parï¿½metro para número de Glosas Registradas Por Factura
	 */
	private String numeroGlosasRegistradasXFactura;
	
	/**
	 * Parï¿½metro para número de Días para Notificar Glosa
	 */
	private String numeroDiasNotificarGlosa;
	
	/**
	 * Parï¿½metro para Produccion En Paralelo Con Sistema Anterior
	 */
	private String produccionEnParaleloConSistemaAnterior;
	
	/**
	 * 
	 */
	private String permitirModificarTiempoTratamientoJustificacionNopos;
	
	/**
	 * Variable que indica si existen registros en la tabla facturas_varias
	 * Cuando se hace el set, obtiene el primer registro de la tabla
	 */
	private HashMap existeFacturaVaria;
	
	private String postularFechasEnRespuestasDyT="";
	
	private String numeroDiasBusquedaReportes="";
	

	//numero digitos captura numero identificacion paciente //70006
	private String numDigCaptNumIdPac = "";
	
	/**
	 * Permitir interpretar ordenes de respuesta multiple sin finalizar
	 */
	private String permIntOrdRespMulSinFin;
	
	
	/**
	 * Almacena las N Clases de Inventarios para Paquetes Materiales Qx  
	 */
	private HashMap clasesInventariosPaqMatQx = new HashMap();
	
	/**
	 * Permite conocer si se requiere que la factura tenga una glosa en estado respondida para ser inactivada
	 */
	private String requiereGlosaInactivar="";
	
	/**
	 * 
	 */
	private String aprobarGlosaRegistro="";
	
	/**
	 * parametro utilizado para 
	 * Tiempo en segundos que indica cada cuanto verificar interfaz Shaio por procesar
	 */
	private String tiemSegVeriInterShaioProc="";
	
	private boolean readOnly;
	
	/**
	 * 
	 */
	private String tipoUsuarioaReportarSol;
	
	/**
	 * 
	 */
	private String maximoNumeroCuotasFinanciacion;
	
	/**
	 * 
	 */
	private String maximoNumeroDiasFinanciacionPorCuota;
	
	/**
	 * 
	 */
	private String formatoDocumentosGarantia_Pagare; 
	
	/**
	 * 
	 */
	private String edadFinalNinez;
	
	/**
	 * 
	 */
	private String edadInicioAdulto;
	
	/**
	 * 
	 */
	private String obligarRegIncapaPacienteHospitalizado;
	
	/**
	 * 
	 */
	private String minutosCaducaCitasReservadas;
	
	/**
	 * 
	 */
	private String minutosCaducaCitasAsignadasReprogramadas;
	
	/**
	 * 
	 */
	private String ejecutarProcAutoActualizacionCitasOdoNoAsistio;
	
	/**
	 * 
	 */
	private String horaEjecutarProcAutoActualizacionCitasOdoNoAsistio;
	
	/**
	 * 
	 */
	private String minutosEsperaAsignarCitasOdoCaducadas;
	
	/**
	 * Parï¿½metro de escala paciente perfil (NED)
	 */
	private String escalaPacientePerfil;
	
	/**
	 * Arreglo que almacena las escalas parametrizadas
	 */
	private ArrayList<HashMap<String,Object>> escalas;
	
	private String utilizanProgramasOdontologicosEnInstitucion;
	
	private ArrayList<DtoDetalleHallazgoProgramaServicio> listadoExistenDHPS;
	
	boolean modificarUPOEI;
	
	/**
	 * 
	 */
	private String tiempoVigenciaPresupuestoOdo;
	
	/**
	 * 
	 */
	private String permiteCambiarServiciosCitaAtencionOdo;
	
	/**
	 * 
	 */
	private String validaPresupuestoOdoContratado;
	
	/**
	 * 
	 */
	private String conveniosAMostrarXDefectoPresupuestoOdo;
	
	/**
	 * 
	 */
	private String tiempoMaxEsperaInactivarPresupuestoOdoSuspTemp;
	
	/**
	 * 
	 */
	private String ejecutarProcesoAutoActualizacionEstadosOdo;
	
	/**
	 * 
	 */
	private String horaEjecutarProcesoAutoActualizacionEstadosOdo;
	
	/**
	 * 
	 */
	private String motivoCancelacionPresupuestoSuspendidoTemp;
	
	/**
	 * 
	 */
	private String maximoTiempoSinEvolucionarParaInactivarPlanTratamiento;
	
	/**
	 * 
	 */
	private String prioridadParaAplicarPromocionesOdo;
	
	/**
	 * 
	 */
	private String diasParaDefinirMoraXDeudaPacientes;
	
	/**
	 * 
	 */
	private String contabilizacionRequeridoProcesoAsocioFacCuentaCapitacion;
	
	/**
	 * 
	 */
	private String cuentaContablePagare;
	
	/**
	 * 
	 */
	private String cuentaContableLetra;
	
	/**
	 * 
	 */
	private String institucionRegistraAtencionExterna;
	
	/**
	 * 
	 */
	private ArrayList<HashMap<String, Object>> conveniosAMostrarPresupuestoOdo;
	
	/**
	 * 
	 */
	private ArrayList<HashMap<String, Object>> convenios;
	
	/**
	 * 
	 */
	private ArrayList<HashMap<String, Object>> motivosAtencionOdontologica;
	
	
	//Anexo 922
	/**
	 * 
	 */
	private String imprimirFirmasImpresionCCCapitacion;
	
	/**
	 * 
	 */
	private String encabezadoFormatoImpresionFacturaOCCCapitacion;
	
	/**
	 * 
	 */
	private String piePaginaFormatoImpresionFacturaOCCCapitacion;
	
	/**
	 * 
	 */
	private String imprimirFirmasEnImpresionCC;
	
	/**
	 * 
	 */
	private String numeroMesesAMostrarEnReportesPresupuestoCapitacion;
	
	
	private DtoFirmasValoresPorDefecto dtoFirmas;
	
	private ArrayList<DtoFirmasValoresPorDefecto> listadoFirmas;
	
	private DtoAreaAperturaCuentaAutoPYP areaAperturaCuentaAutoPYP;
	
	private ArrayList<DtoAreaAperturaCuentaAutoPYP> listadoAreasAperturaCuentaAutoPYP;
	
	private String valorDefecto="";
	
	private int posFirma;
	
	//Fin Anexo 992
	
	//Anexo 958
	private String reciboCajaAutomaticoGeneracionFacturaVaria;
	
	private String conceptoIngresoFacturasVarias;
	
	private ArrayList<DtoConceptosIngTesoreria> listadoConceptos;
	//Anexo 958
	

	//Anexo 888
	private String esRequeridoProgramarCitaAlContratarPresupuestoOdon;
	
	private String motivoAnulacionSolicitudAutorizacionDescuentoPresupeustoOdontologico;
	
	private ArrayList<DtoMotivoDescuento> listMotivosDescuentos;
	
	//Fin anexo 888
	
	//Anexo 888 Pt II
	/**
	 * 
	 */
	public String requeridoProgramarCitaControlAlTerminarPresupuestoOdon;
	
	/**
	 * 
	 */
	public String requeridoProgramarCitaControlAlTerminarPresupuestoOdonServicio;
	
	/**
	 * 
	 */
	public String tiempoDiasParaAgendarCitaControlAlTerminarPresupuestoOdon;
	
	/**
	 * 
	 */
	public String institucionManejaFacturacionAutomatica;
	
	/**
	 * 
	 */
	private String requeridoProgramarProximaCitaEnAtencion;
	
	
	public HashMap serviciosMap;
	//Fin Anexo 888 Pt II
	
	//Anexo 959
	/**
	 * 
	 */
	public String manejaConsecutivoFacturaPorCentroAtencion;
	
	/**
	 * 
	 */
	public String manejaConsecutivosTesoreriaPorCentroAtencion;
	
	/**
	 * 
	 */
	public String tamanioImpresionRC;
	
	public String requiereAperturaCierreCaja;
	
	//Fin Anexo 959
	
	/**
	 * 
	 */
	public boolean modificarMultiploMinGeneracionCita;
	
	/**
	 * 
	 */
	public boolean existePresupuesto;
	
	/**
	 * 
	 */
	private String activarBotonGenerarSolicitudOrdenAmbulatora="";
	
	/**
	 * 
	 */
	private String esRequeridoTestigoSolicitudAceptacionTrasladoCaja="";
	
	/**
	 * 
	 */
	private String institucionManejaCajaPrincipal="";
	
	/**
	 * 
	 */
	private String institucionManejaTrasladoOtraCajaRecaudo="";
	
	/**
	 * 
	 */
	private String institucionManejaEntregaATransportadoraValores="";
	
	/**
	 * 
	 */
	private String trasladoAbonosPacienteSoloPacientesConPresupuestoContratado="";
	
	/**
	 * 
	 */
	private String controlarAbonoPacientePorNroIngreso="";
	
	/**
	 * 
	 */
	private String validaEstadoContratoNominaALosProfesionalesSalud="";
	
	/**
	 * 
	 */
	private String manejaInterfazUsuariosSistemaERP="";
	
	/**
	 * 
	 */
	private String requierGenerarSolicitudCambioServicio="";
	
	
	private String manejaVentaTarjetaClienteOdontoSinEmision="";
	
	/**
	 * 
	 */
	private String lasCitasDeControlSePermitenAsignarA="";
	
	/**
	 * 
	 */
	private HashMap centrosAtencion;
	
	/**
	 * 
	 */
	private HashMap centrosCosto;
	
	/**
	 * 
	 */
	private int pos;
	
	/**
	 * 
	 */
	private String mostrarGraficaCalculoIndicePlaca;
	

	/**
	 * 
	 */
	private String validarPacienteParaVentaTarjeta;
	
	/**
	 * 
	 */
	private String reciboCajaAutomaticoVentaTarjeta;
	
	
	
	/**
	 * Atributo para almacenar el estado a reprogramar cuando se cancelar cita
	 */
	private String cancelarCitaAutoEstadoReprogramar;

	
		
	
	/**
	 * permitirRegistrarReclamacionCuentasNoFacturadas
	 */
	private String permitirRegistrarReclamacionCuentasNoFacturadas;
	
	/**
	 * permitirFacturarReclamarCuentasConRegistroPendientes
	 */
	private String permitirFacturarReclamarCuentasConRegistroPendientes;
	
	/**
	 * permitirOrdenarMedicamentosPacienteUrgenciasConEgreso
	 */
	private String permitirOrdenarMedicamentosPacienteUrgenciasConEgreso;
	
	/**
	 * mostrarAdminMedicamentosArticulosDespachoCero
	 */
	private String mostrarAdminMedicamentosArticulosDespachoCero;
		
	/**
	 * Formato factura Varia
	 */
	private String formatoFacturaVaria;
	
	/**
	 * Permite modificar la fecha y la hora del inicio de la atención odontológica. 
	 */
	private String modificarFechaHoraInicioAtencionOdonto;
	
	/**
	 * Atributo que Permite definir la entidad subcontratada a la cual se 
	 * autorizarán los servicios, medicamentos e insumos de ordenes con Centro de Costo. 
	 */
	private String entidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada;
	
	/**
	 * Atributo que permite definir la prioridad de la entidad subcontratada.
	 */
	private String prioridadEntidadSubcontratada;
	
	/**
	 * Atributo que permite definir si se requiere autorización capitación subcontratada
	 * para facturar. 
	 */
	private String requiereAutorizacionCapitacionSubcontratadaParaFacturar;
	
	/**
	 * Atributo que indica si se debe manejar consecutivo para facturas varias 
	 * por centro de atención.
	 */
	private String manejaConsecutivoFacturasVariasPorCentroAtencion;
	
	/**
	 * Atributo que indica el formato de impresión de una autorización de entidad subcontratada
	 */
	private String formatoImpresionAutorEntidadSub;
	
	/**
	 * Atributo que indica el encabezado del formato de impresión de una autorización de entidad subcontratada
	 */
	private String encFormatoImpresionAutorEntidadSub;
	
	/**
	 * Atributo que indica el pié de página del formato de impresión de una autorización de entidad subcontratada
	 */
	private String piePagFormatoImpresionAutorEntidadSub;
	
	/**
	 * Atributo que indica el pié de página del formato de impresión de una autorización de entidad subcontratada
	 */
	private String diasVigenciaAutorIndicativoTemp;
		
	/**
	 * Atributo que define los días de prórroga de una autorización
	 */
	private String diasProrrogaAutorizacion;
	
	/**
	 * Atributo que define los días para calcular la fecha de vencimiento de la Autorización de servicios
	 */
	private String diasCalcularFechaVencAutorizacionServicio;
	

	/**
	 * Atributo que define los días para calcular la fecha de vencimiento de la Autorización de servicios
	 */
	private String diasCalcularFechaVencAutorizacionArticulo; 
	
	
	/**
	 * Atributo que define los días para calcular la fecha de vencimiento de la Autorización de servicios
	 */
	private String diasVigentesNuevaAutorizacionEstanciaSerArt;
	
	/**
	 * Atributo que define la hora en la cual se debe ejecutar el proceso del cierre de capitación
	 */
	private String horaProcesoCierreCapitacion;
	
	/**
	 * Atributo para facturar y registrar reclamaciones en cuentas con R.A.T. o R.E.C. asociados en estado pendiente
	 */
	private String permitirfacturarReclamCuentaRATREC;
	
	/**
	 * Atributo para facturar y registrar reclamaciones en cuentas con R.A.T. o R.E.C. asociados en estado pendiente
	 */
	private String[] viaIngresoDetaleGlosa;
	
	
	/**
	 * Atributo para  definir el formato Impresión Reserva Cita Odontológica
	 */
	private String formatoImpReservaCitaOdonto;
	
	
	/**
	 * Atributo para  definir el formato de asignacion de Citas Odontológicas
	 */
	private String formatoImpAsignacionCitaOdonto;
	
	
	/**
	 * Atributo para validar que exista información de cierre de órdenes médicas
	 */
	private String fechaInicioCierreOrdenMedica;
	
	
	/**
	 * Atributo que indica el esquema tarifario de servicios para valorizar las órdenes
	 */
	private String esquemaTariServiciosValorizarOrden;
	
	/**
	 * Atributo que indica el esquema tarifario de medicamentos e insumos para valorizar las órdenes
	 */
	private String esquemaTariMedicamentosValorizarOrden;
	
	/**
	 * 
	 */
	private String permitirModificarDatosUsuariosCapitados;
	

	/**
	 * 
	 */
	private String permitirModificarDatosUsuariosCapitadosModificarCuenta;
	
	/**
	 * 
	 */
	private String manejaHojaAnestesia;
	
	
	
	/**
	 * Atributo para validar que exista información de cierre de órdenes médicas
	 */
	private String solicitudCitaInterconsultaOdontoCitaProgramada;
	

	/**
	 * Atributo parámetro para definir definir la hora en la cual se debe ejecutar el proceso automático para la inactivación  de usuario
	 */
	private String horaEjecProcesoInactivarUsuarioInacSistema;
	
	/**
	 * Atributo parámetro para definir definir la hora en la cual se debe ejecutar el proceso automático para la inactivación  de usuario
	 */
	private String horaEjeProcesoCaduContraInacSistema;
	
	
	/**
	 * Atributo parámetro Número días de vigencia de contraseña usuario.
	 */
	private String diasVigenciaContraUsuario;
	
	/**
	 * Atributo parámetro Número de días para finalizar vigencia de contraseña y mostrar alerta
	 */
	private String diasFinalesVigenciaContraMostrarAlerta;
	
	/**
	 * 
	 */
	private String numMaximoReclamacionesAccEventoXFactura;
	
	/**
	 * 
	 */
	private String hacerRequeridoValorAbonoAplicadoAlFacturar;
	
	
	/** * esquemaTarifarioAutocapitaSubCirugiasNoCurentos */
	private String esquemaTarifarioAutocapitaSubCirugiasNoCurentos;
	
	/** * viaIngresoValidarNivelAutorizacionCapitacion  */
	private String viaIngresoValidarNivelAutorizacionCapitacion;
	
	/** * tipoPacienteViaIngresoValidarNivelAutorizacionCapitacion  */
	private String tipoPacienteViaIngresoValidarNivelAutorizacionCapitacion;
	
	/** * validarDisponibilidadSaldoPresupuestoAutorizacionesCapotaAutomatica  */
	private String validarDisponibilidadSaldoPresupuestoAutorizacionesCapotaAutomatica;
	
	/** * HacerRequeridaSeleccionCentroAtencionAsignadoSubirPacienteIndividual  */
	private String hacerRequeridaSeleccionCentroAtencionAsignadoSubirPacienteIndividual;
	
	/** atributo para definir maneja_consecutivos_notas_pacientes_centro_atención */
	private String manejaConsecutivosNotasPacientesCentroAtencion;
	
	private boolean permiteModificarManejaConsecutivosNotasPacienteCentroAtencion;
	
	/** atributo para definir naturaleza_notas_pacientes_manejar */
	private String naturalezaNotasPacientesManejar;
	
	private boolean permiteModificarNaturalezaNotasPaciente;
	
	private boolean permiteControlarAbonoPacientesPorNumeroIngreso;
	
	/** * listaEsquemasTarifarios  */
	private ArrayList<EsquemasTarifarios> listaEsquemasTarifarios;
	
	/** * listaviasIngreso  */
	private ArrayList<ViasIngreso> listaviasIngreso;
	
	/** * listaTiposPaciente  */
	private  ArrayList<TiposPaciente> listaTiposPaciente;
	
	/**
	 * Permitir Reacudos Caja Mayor
	 */
	private String permitirRecaudosCajaMayor;
	
	
	/** * viaIngresoValidacionesOrdenesAmbulatorias  */
	private String viaIngresoValidacionesOrdenesAmbulatorias;
	
	/** * viaIngresoValidacionesPeticiones  */
	private String viaIngresoValidacionesPeticiones;
	
	/**
	 * ingresar maximo de dias a consultar ingresos en HC
	 */
	private String maximoDiasIngresosAConsultar;
	
	/**
	 * nombre del parametro para definir los Meses Maximo para Administrar Autorizaciones de Capitacion vencidas
	 */
	public String mesesMaxAdminAutoCapVencidas;
	
	/**
	 * ingresar los días maximos de prorroga de una autorizacion de Articulo
	 */
	public String diasMaxProrrogaAutorizacionArticulo ;
	
	/**
	 * ingresar los días maximos de prorroga de una autorizacion de Servicios
	 */
	public String diasMaxProrrogaAutorizacionServicio ;
	
	
	/** * tipoPacienteValidacionesOrdenesAmbulatorias  */
	private String tipoPacienteValidacionesOrdenesAmbulatorias;
	
	/** * tipoPacienteValidacionesPeticiones  */
	private String tipoPacienteValidacionesPeticiones;
	
	private boolean desactivarEntidadSubPorCentroCosto;
	//-------------------------------------------------------- Inicio Metodo Reset ------------------------------------------------------------------------	
	/**
	 * Resetear la forma
	 */
	public void reset()
	{
		this.direccionPaciente = "";
		this.centroCostoConsultaExterna = "";
		this.causaExterna = "";
		this.finalidad = "";
		this.paisNacimiento = "";
		this.paisResidencia = "";
		this.ciudadNacimiento = "";
		this.ciudadVivienda = "";
		this.zonaDomicilio = "";
		this.ocupacion = "";
		this.tipoId = "";
		this.fechaNacimiento = "";
		this.historiaClinica = "";
		this.flagCentinela = "";
		this.centroCostoUrgencias = "";
		//carnetRequerido="";
		this.codigoServicioFarmacia = "";
		this.validarEstadoSolicitudesInterpretadas = "";
		this.validarContratosVencidos = "";
		this.centroCostoAmbulatorios = "";
		this.justificacionServiciosRequerida = "";
		this.ingresoCantidadFarmacia = "";
		this.ripsPorFactura = "";
		this.fechaCorteSaldoInicial = "";
		this.confirmarAjustesPooles="";
		this.fechaCorteSaldoInicialCapitacion="";
		this.topeConsecutivoCxC = "";
		this.topeConsecutivoCxCCapitacion = "";
		this.maxPageItems = "0";
		this.maxPageItemsEpicrisis="0";
		this.genExcepcionesFarmAut = "";
		this.excepcionRipsConsultorios="";
		this.ajustarCuentaCobroRadicada="";
		this.cerrarCuentaAnulacionFactura="";
		this.barrioResidencia="";
		this.materialesPorActo="";
		this.horaInicioProgramacionSalas = "";
		this.horaFinProgramacionSalas = "";
		this.codigoEspecialidadAnestesiologia = "";
		this.modulo = ConstantesBD.codigoNuncaValido;
		this.nombreModulo = "";
		this.mapaEtiquetas = new HashMap();
		this.manejoConsecTransInv = "";
		this.porcentajesCostoInv = "";
		this.codigoTransSolicPacientes = "";
		this.codigoTransDevolPacientes = "";
		this.codigoTransPedidos = "";
		this.codigoTransDvolPedidos = "";
		this.codigoTransCompras = "";
		this.codigoTransDevolCompras="";
		this.permitirModificarFechaInv = "";
		this.porcentajePuntoPedido = "";
		this.diasAlertaVigencia = "";
		this.diasPreviosNotificacionProximoControl = "";
		this.codigoTransTrasladoAlmacenes = "";
		this.flagInfoAdicIngresoConvenios = "";
		this.ocupacionMedicoEspecialista = "";
		this.ocupacionMedicoGeneral = "";
//		Anexo 844
		this.ocupacionOdontologo = new String("");
		this.ocupacionAuxiliarOdontologo = new String("");
//		
		this.ocupacionEnfermera = "";
		this.ocupacionAuxiliarEnfermeria = "";
		this.horaInicioPrimerTurno="";
		this.horaFinUltimoTurno = "";
		this.alertaFichasPendientes = "";
		this.alertaCasoVigilancia = "";
		this.vigilarAccRabico = "";
		this.tiempoMaximoGrabacion = "";
		this.tipoConsecutivoCapitacion = "";
		this.minutosEsperaCuentasProcFact = "";
		this.datosCuentaRequeridoReservaCitas = "";
		this.redNoAdscrita = "";
		this.centroCostoTerceros = new HashMap();
		this.centroCostoTerceros.put("numRegistros", "0");
		this.horasReproceso= new HashMap();
		this.horasReproceso.put("numRegistros", "0");
		this.numDiasTratamientoMedicamentos = "";
		this.numDiasGenerarOrdenesArticulos = "";
		this.numDiasEgresoOrdenesAmbulatorias = "";
		this.convenioFisalud="";
		this.formaPagoEfectivo="";
		this.codigoManualEstandarBusquedaServicios="";
		this.horasCaducidadReferenciasExternas = "";
		this.llamadoAutomaticoReferencia = "";
		this.interfazPacientes="";
		this.interfazAbonosTesoreria="";
		this.interfazCompras="";
		this.articuloInventario="";
		this.loginUsuario="";
		this.validarEdadResponsablePaciente="";
		this.validarEdadDeudorPaciente="";
		this.aniosBaseEdadAdulta="";
		this.validarEgresoAdministrativoPaquetizar="";
		this.maxCantidPaquetesValidosIngresoPaciente="";
		this.asignarValorPacienteValorCargos="";
		this.interfazCarteraPacientes="";
		this.interfazContableFacturas="";
		this.interfazTerceros="";
		this.consolidarCargos="";
		this.manejaConversionMonedaExtranjera="";
		this.impresionMediaCarta="";
		this.horaCorteHistoricoCamas = "";
		this.minutosLimiteAlertaReserva = "";
		this.minutosLimiteAlertaPacienteConSalidaUrgencias ="";
		this.minutosLimiteAlertaPacienteConSalidaHospitalizacion="";
		this.minutosLimiteAlertaPacientePorRemitirUrgencias ="";
		this.minutosLimiteAlertaPacientePorRemitirHospitalizacion ="";
		this.identificadorInstitucionArchivosColsanitas = "";
		this.interfazRips = "";
		this.entidadManejaHospitalDia = "";
		this.entidadManejaRips = "";
		this.valoracionUrgenciasEnHospitalizacion = "";
		this.liquidacionAutomaticaCirugias="";
		this.liquidacionAutomaticaNoQx="";
		this.manejoProgramacionSalasSolicitudesDyt="";
		this.requeridaDescripcionEspecialidadCirugias="";
		this.requeridaDescripcionEspecialidadNoCruentos="";
		this.asocioAyudantia="";
		this.indicativoCobrableHonorariosCirugia="";
		this.indicativoCobrableHonorariosNoCruento="";
		this.asocioCirujano="";
		this.asocioAnestesia="";
		this.modificarInformacionDescripcionQuirurgica="";
		this.minutosRegistroNotasCirugia="";
		this.minutosRegistroNotasNoCruentos="";
		this.modificarInformacionQuirurgica="";
		this.minutosMaximosRegistroAnestesia="";
		this.ubicacionPlanosEntidadesSubcontratadas = "";
		this.institucionMultiempresa="";
		this.archivosPlanosReportes="";
		this.archivosPlanosReportesInventarios="";
		this.conteosValidosAjustarInventarioFisico = "";
		this.entidadControlaDespachoSaldosMultidosis="";
		this.numeroDiasControlMedicamentosOrdenados="";
		this.generaEstanciaAutomatica="";
		this.horaGeneraEstanciaAutomatica="";
		this.incluirTipoPacientesCirugiaAmbulatoria="";
		this.tipoConsecutivoManejar="";		
		this.requeridoInfoRipsCargoDirecto = "";	
		this.genForecatRips = "";
		this.asignaValoracionCxAmbulaHospita ="";
		this.interfazContableRecibosCajaERP="";
		this.validadAdministracionMedEgresoMedico="";
		this.permitirConsultarEpicrisisSoloProf="";
		this.mostrarAntecedentesParametrizadosEpicrisis="";
		this.interfazConsecutivoFacturasOtroSistema="";
		this.interfazNutricion="";
		this.crearCuentaAtencionCitas="";
		this.institucionManejaMultasPorIncumplimiento = "";
		this.bloqueaCitasReservaAsignReprogPorIncump="";
		this.bloqueaAtencionCitasPorIncump="";
		this.bloqueaAtencionCitasPorIncump ="";
		this.tiempoMaximoReingresoUrgencias="";
		this.tiempoMaximoReingresoHospitalizacion="";
		this.fechaInicioControlMultasIncumplimientoCitas = "";
		this.permitirFacturarReingresosIndependientes="";
		this.liberarCamaHospitalizacionDespuesFacturar="";
		this.controlaInterpretacionProcedimientosEvolucion="";
		this.validarRegistroEvolucionGenerarOrdenes="";
		this.pathArchivosPlanosFacturacion="";
		this.pathArchivosPlanosFurips="";
		this.pathArchivosPlanosManejoPaciente="";
		this.comprobacionDerechosCapitacionObligatoria = "";
		this.requiereAutorizarAnularFacturas = "";
		this.validarPoolesFact="";
		this.mostrarEnviarEpicrisisEvol="";
		this.conceptoParaAjusteEntrada="";
		this.conceptoParaAjusteSalida="";
		this.permitirModificarConceptosAjuste="";
		this.diasRestriccionCitasIncumplidas = "";
		this.permitirModificarFechaSolicitudPedidos="";
		this.permitirModificarFechaSolicitudTraslado="";
		this.codigoManualEstandarBusquedaArticulos = "";
		this.requeridoComentariosSolicitar = "";
		this.numeroDiasResponderGlosas = "";
		this.generarAjusteAutoRegRespuesta = "";
		this.generarAjusteAutoRegRespuesConciliacion = "";
		this.formatoImpresionRespuesGlosa = "";
		this.formatoImpresionConciliacion="";
		this.validarGlosaReiterada="";
		this.imprimirFirmasImpresionRespuesGlosa = "";
		this.permitirModificarTiempoTratamientoJustificacionNopos = "";
		this.validarAuditor = "";
		this.validarUsuarioGlosa = "";
		this.numeroGlosasRegistradasXFactura = "";
		this.numeroDiasNotificarGlosa = "";
		this.produccionEnParaleloConSistemaAnterior = "";
		this.existeFacturaVaria = new HashMap();
		this.postularFechasEnRespuestasDyT = "";
		this.clasesInventariosPaqMatQx = new HashMap();
		this.clasesInventariosPaqMatQx.put("numRegistros", "0");
		this.numeroDiasBusquedaReportes="";

		this.numDigCaptNumIdPac = ""; //70006
		this.permIntOrdRespMulSinFin = "";
		this.requiereGlosaInactivar="";
		this.aprobarGlosaRegistro="";
		this.tiemSegVeriInterShaioProc="";
		
		this.tipoUsuarioaReportarSol="";
		
		this.readOnly=false;
		
		this.maximoNumeroCuotasFinanciacion = "";
		this.maximoNumeroDiasFinanciacionPorCuota = "";
		this.formatoDocumentosGarantia_Pagare = "";
		this.edadFinalNinez = "";
		this.edadFinalNinez = "";
		
		this.minutosCaducaCitasReservadas="";
		this.minutosCaducaCitasAsignadasReprogramadas="";
		this.ejecutarProcAutoActualizacionCitasOdoNoAsistio="";
		this.horaEjecutarProcAutoActualizacionCitasOdoNoAsistio="";
		this.minutosEsperaAsignarCitasOdoCaducadas="";
		

		this.servicioTemporal="";
		this.indiceServicioSeleccionado=ConstantesBD.codigoNuncaValido;
		this.serviciosSeleccionados="";
		
		this.escalaPacientePerfil = "";
		this.escalas = new ArrayList<HashMap<String,Object>>();
		
		this.utilizanProgramasOdontologicosEnInstitucion="";
		this.listadoExistenDHPS=new ArrayList<DtoDetalleHallazgoProgramaServicio>();
		this.modificarUPOEI=false;
		
		this.tiempoVigenciaPresupuestoOdo = "";
		this.permiteCambiarServiciosCitaAtencionOdo = "";
		this.validaPresupuestoOdoContratado = "";
		this.conveniosAMostrarXDefectoPresupuestoOdo = "";
		this.conveniosAMostrarPresupuestoOdo = new ArrayList<HashMap<String,Object>>();
		this.tiempoMaxEsperaInactivarPresupuestoOdoSuspTemp = "";
		this.ejecutarProcesoAutoActualizacionEstadosOdo = "";
		this.horaEjecutarProcesoAutoActualizacionEstadosOdo = "";
		this.motivoCancelacionPresupuestoSuspendidoTemp = "";
		this.maximoTiempoSinEvolucionarParaInactivarPlanTratamiento = "";
		this.prioridadParaAplicarPromocionesOdo = "";
		this.diasParaDefinirMoraXDeudaPacientes="";
		
		this.contabilizacionRequeridoProcesoAsocioFacCuentaCapitacion="";
		this.cuentaContablePagare="";
		this.cuentaContableLetra="";

		
		this.convenios = new ArrayList<HashMap<String,Object>>();
		this.motivosAtencionOdontologica = new ArrayList<HashMap<String,Object>>();
		
		this.institucionRegistraAtencionExterna = "";
		
		//Anexo 922
		this.imprimirFirmasImpresionCCCapitacion="";
		this.encabezadoFormatoImpresionFacturaOCCCapitacion="";
		this.piePaginaFormatoImpresionFacturaOCCCapitacion="";
		this.dtoFirmas=new DtoFirmasValoresPorDefecto();
		this.listadoFirmas=new ArrayList<DtoFirmasValoresPorDefecto>();
		this.valorDefecto="";
		this.posFirma=ConstantesBD.codigoNuncaValido;
		this.imprimirFirmasEnImpresionCC="";
		this.numeroMesesAMostrarEnReportesPresupuestoCapitacion="";
			
		//Anexo 958
		this.reciboCajaAutomaticoGeneracionFacturaVaria="";
		this.conceptoIngresoFacturasVarias="";
		//this.listadoConceptos=new ArrayList<DtoConceptosIngTesoreria>();
		
		//Anexo 888
		this.esRequeridoProgramarCitaAlContratarPresupuestoOdon="";
		this.motivoAnulacionSolicitudAutorizacionDescuentoPresupeustoOdontologico="";
		//this.listMotivosDescuentos=new ArrayList<DtoMotivoDescuento>();
		
		//Anexo 888 Pt II
		this.requeridoProgramarCitaControlAlTerminarPresupuestoOdon="";
		this.requeridoProgramarCitaControlAlTerminarPresupuestoOdonServicio="";
		this.tiempoDiasParaAgendarCitaControlAlTerminarPresupuestoOdon="";
		this.institucionManejaFacturacionAutomatica="";
		this.serviciosMap=new HashMap();
		
		//Anexo 959
		this.manejaConsecutivoFacturaPorCentroAtencion="";
		this.manejaConsecutivosTesoreriaPorCentroAtencion="";
		this.tamanioImpresionRC="";
		
		this.modificarMultiploMinGeneracionCita=false;
		this.existePresupuesto=false;
		
		this.requiereAperturaCierreCaja="";
		this.requeridoProgramarProximaCitaEnAtencion="";
		
		this.areaAperturaCuentaAutoPYP = new DtoAreaAperturaCuentaAutoPYP();
		this.listadoAreasAperturaCuentaAutoPYP = new ArrayList<DtoAreaAperturaCuentaAutoPYP>();
		this.centrosAtencion= new HashMap();
		this.centrosCosto=new HashMap();
		this.centrosCosto.put("numRegistros", "0");
		this.pos = ConstantesBD.codigoNuncaValido;
		
		this.validarPacienteParaVentaTarjeta="";
		this.reciboCajaAutomaticoVentaTarjeta="";
		
		
		this.cancelarCitaAutoEstadoReprogramar=ConstantesBD.acronimoSi;

		
		this.permitirRegistrarReclamacionCuentasNoFacturadas="";
		this.permitirFacturarReclamarCuentasConRegistroPendientes="";
		this.permitirOrdenarMedicamentosPacienteUrgenciasConEgreso="";
		this.mostrarAdminMedicamentosArticulosDespachoCero="";
		
		this.formatoFacturaVaria="";
		this.modificarFechaHoraInicioAtencionOdonto = "";
		this.entidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada = "";
		this.prioridadEntidadSubcontratada = "";
		this.requiereAutorizacionCapitacionSubcontratadaParaFacturar = "";
		this.manejaConsecutivoFacturasVariasPorCentroAtencion = "";
		this.formatoImpresionAutorEntidadSub="";
		this.encFormatoImpresionAutorEntidadSub="";
		this.piePagFormatoImpresionAutorEntidadSub="";
		this.diasVigenciaAutorIndicativoTemp="";
		this.diasProrrogaAutorizacion="";
		this.diasCalcularFechaVencAutorizacionServicio="";
		this.diasCalcularFechaVencAutorizacionArticulo="";	
		this.diasVigentesNuevaAutorizacionEstanciaSerArt="";
		this.horaProcesoCierreCapitacion="";
		this.permitirfacturarReclamCuentaRATREC="";
		this.viaIngresoDetaleGlosa = new String[]{};
		this.formatoImpReservaCitaOdonto="";
		this.formatoImpAsignacionCitaOdonto="";
		this.fechaInicioCierreOrdenMedica="";
		this.esquemaTariServiciosValorizarOrden="";
		this.esquemaTariMedicamentosValorizarOrden="";
		this.permitirModificarDatosUsuariosCapitados="";
		this.permitirModificarDatosUsuariosCapitadosModificarCuenta="";
		this.manejaHojaAnestesia="";
		this.solicitudCitaInterconsultaOdontoCitaProgramada="";
		this.horaEjecProcesoInactivarUsuarioInacSistema="";
		this.horaEjeProcesoCaduContraInacSistema="";
		this.diasVigenciaContraUsuario="";
		this.diasFinalesVigenciaContraMostrarAlerta="";
		this.numMaximoReclamacionesAccEventoXFactura="";
		this.hacerRequeridoValorAbonoAplicadoAlFacturar="";
		this.esquemaTarifarioAutocapitaSubCirugiasNoCurentos="";
		this.viaIngresoValidarNivelAutorizacionCapitacion="";
		this.tipoPacienteViaIngresoValidarNivelAutorizacionCapitacion = "";
		this.validarDisponibilidadSaldoPresupuestoAutorizacionesCapotaAutomatica="";
		this.listaEsquemasTarifarios = new ArrayList<EsquemasTarifarios>();
		this.listaviasIngreso = new ArrayList<ViasIngreso>();
		this.listaTiposPaciente = new ArrayList<TiposPaciente>();
		this.hacerRequeridaSeleccionCentroAtencionAsignadoSubirPacienteIndividual="";
		this.manejaConsecutivosNotasPacientesCentroAtencion="";
		this.setPermiteModificarManejaConsecutivosNotasPacienteCentroAtencion(true);
		this.naturalezaNotasPacientesManejar="";
		this.setPermiteModificarNaturalezaNotasPaciente(true);
		this.permiteControlarAbonoPacientesPorNumeroIngreso = true;
		this.serviciosManejoTransPrimario =new ArrayList<Integer>();
		this.serviciosManejoTransSecundario =new ArrayList<Integer>();
		this.manejoOxigenoFurips ="";
		this.permitirRecaudosCajaMayor="";
		this.viaIngresoValidacionesOrdenesAmbulatorias="";
		this.viaIngresoValidacionesPeticiones="";
		this.maximoDiasIngresosAConsultar="";
		this.mesesMaxAdminAutoCapVencidas="";
		this.diasMaxProrrogaAutorizacionArticulo="";
		this.diasMaxProrrogaAutorizacionServicio="";
		this.tipoPacienteValidacionesOrdenesAmbulatorias="";
		this.tipoPacienteValidacionesPeticiones="";
		this.desactivarEntidadSubPorCentroCosto=false;
	}

	
	/**
	 * @return the readOnly
	 */
	public boolean isReadOnly() {
		return readOnly;
	}

	/**
	 * @param readOnly the readOnly to set
	 */
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	/**
	 * @return the tiemSegVeriInterShaioProc
	 */
	public String getTiemSegVeriInterShaioProc() {
		return tiemSegVeriInterShaioProc;
	}

	/**
	 * @param tiemSegVeriInterShaioProc the tiemSegVeriInterShaioProc to set
	 */
	public void setTiemSegVeriInterShaioProc(String tiemSegVeriInterShaioProc) {
		this.tiemSegVeriInterShaioProc = tiemSegVeriInterShaioProc;
	}

	/**
	 * @return the permIntOrdRespMulSinFin
	 */
	public String getPermIntOrdRespMulSinFin() {
		return permIntOrdRespMulSinFin;
	}

	/**
	 * @param permIntOrdRespMulSinFin the permIntOrdRespMulSinFin to set
	 */
	public void setPermIntOrdRespMulSinFin(String permIntOrdRespMulSinFin) {
		this.permIntOrdRespMulSinFin = permIntOrdRespMulSinFin;
	}

	public String getNumeroDiasBusquedaReportes() {
		return numeroDiasBusquedaReportes;
	}

	public void setNumeroDiasBusquedaReportes(String numeroDiasBusquedaReportes) {
		this.numeroDiasBusquedaReportes = numeroDiasBusquedaReportes;
	}
	
	public String getPostularFechasEnRespuestasDyT() {
		return postularFechasEnRespuestasDyT;
	}

	public void setPostularFechasEnRespuestasDyT(
			String postularFechasEnRespuestasDyT) {
		this.postularFechasEnRespuestasDyT = postularFechasEnRespuestasDyT;
	}

/**
	 * @return the requeridoComentariosSolicitar
	 */
	public String getRequeridoComentariosSolicitar() {
		return requeridoComentariosSolicitar;
	}

	/**
	 * @param requeridoComentariosSolicitar the requeridoComentariosSolicitar to set
	 */
	public void setRequeridoComentariosSolicitar(
			String requeridoComentariosSolicitar) {
		this.requeridoComentariosSolicitar = requeridoComentariosSolicitar;
	}

	//---------------------------------------------------------- Fin Metodo Reset ----------------------------------------------------------------------
	/**
	 * Mï¿½todo para validar los datos de entrada del usuario
	 * En el momento solo se valida que el UVR sea numï¿½rico mayor que 0
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		if(!estado.equals("guardar"))
		{
			return null;
		}
		ActionErrors errores=new ActionErrors();
		/******************************************************************
		 * CONSULTA EXTERNA
		 ******************************************************************/
		
		
		if(this.modulo==ConstantesBD.codigoModuloConsultaExterna)
		{
		    /*if(diasPreviosNotificacionProximoControl.equals(""))
			{
				errores.add("errors.required",new ActionMessage("errors.required","El parï¿½metro Días previos para notificaciï¿½n de Cita de Control "));
			}*/
		    if(!minutosEsperaCitaCaduca.equals(""))
			{
		    	try{
		    		int minutos=Integer.parseInt(minutosEsperaCitaCaduca);
		    		if(minutos<0)
		    		{
						errores.add("entero",new ActionMessage("errors.integerMayorIgualQue","El parámetro  \"Minutos de Espera para Asignar Citas Caducadas\" ", "0"));
		    		}
		    	}
		    	catch(NumberFormatException e)
		    	{
					errores.add("entero",new ActionMessage("errors.integerMayorIgualQue","El parámetro  \"Minutos de Espera para Asignar Citas Caducadas\" ", "0"));
		    	}
			}
		    
		    if (!institucionManejaMultasPorIncumplimiento.equals(ConstantesBD.acronimoSi) && !institucionManejaMultasPorIncumplimiento.equals(ConstantesBD.acronimoNo))
    		{
		    	readOnly = true;
    			errores.add("cadena",new ActionMessage("errors.required","El parámetro  \"La Institución Maneja Multas por incumplimiento de Cita\" ", "0"));
    		}
		    else if(institucionManejaMultasPorIncumplimiento.equals(ConstantesBD.acronimoSi))
		    {
		    	readOnly = false;
		    	if(!bloqueaCitasReservaAsignReprogPorIncump.equals(ConstantesBD.acronimoSi) && !bloqueaCitasReservaAsignReprogPorIncump.equals(ConstantesBD.acronimoNo))
		    	{
		    		errores.add("cadena",new ActionMessage("errors.required","El parámetro  \"Bloquean las Citas en Reserva, Asignación y/o Reprogramación por incumplimiento de Citas\" ", "0"));
		    	}
		    	if(!bloqueaAtencionCitasPorIncump.equals(ConstantesBD.acronimoSi) && !bloqueaAtencionCitasPorIncump.equals(ConstantesBD.acronimoNo))
		    	{
		    		errores.add("cadena",new ActionMessage("errors.required","El parï¿½metro  \"Bloquea la Atencion de Citas por Incumplimiento de Citas\" ", "0"));
		    	}
		    	if(fechaInicioControlMultasIncumplimientoCitas.equals(""))
		    	{
		    		errores.add("cadena",new ActionMessage("errors.required","El parï¿½metro  \"Fecha Inicio Control de Multas por incumplimiento de Citas\" ", "Actual"));
		    	}
		    	else if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fechaInicioControlMultasIncumplimientoCitas, UtilidadFecha.getFechaActual()))
		    	{
		    		errores.add("fecha",new ActionMessage("errors.fechaPosteriorAOtraDeReferencia","El parï¿½metro  \"Fecha Inicio Control de Multas por incumplimiento de Citas\" ", "Actual"));
		    	}
		    	if(valorMultaPorIncumplimientoCitas.equals(""))
		    	{
		    		errores.add("cadena",new ActionMessage("errors.required","El parï¿½metro  \"Valor Multa por Incumplimiento de Citas\" ", "0"));
		    	}
		    	else if(Utilidades.convertirAEntero(valorMultaPorIncumplimientoCitas) <= 0)
		    	{
		    		errores.add("entero",new ActionMessage("errors.integerMayorQue","El parï¿½metro  \"Valor Multa por Incumplimiento de Citas\" ", "0"));
		    	}
		    	try {
					Integer.parseInt(this.valorMultaPorIncumplimientoCitas);
				} catch (Exception e) {
					errores.add("errors.integer",new ActionMessage("errors.integer","El  Valor Multa por Incumplimiento de Citas"));
				}	
		    }   	
		    
		    if(!this.diasRestriccionCitasIncumplidas.equals("")&&Utilidades.convertirAEntero(this.diasRestriccionCitasIncumplidas)==ConstantesBD.codigoNuncaValido)
		    	errores.add("errors.integer",new ActionMessage("errors.integer","El parï¿½metro Días restricciï¿½n citas incumplidas"));
		    		    
		    if(UtilidadTexto.isEmpty(this.formatoImpReservaCitaOdonto)){
		    	errores.add("errors.required",new ActionMessage("errors.required","El parámetro: Formato Impresión Reserva Cita Odontológica "));
		    }
		    
		    if(UtilidadTexto.isEmpty(this.formatoImpAsignacionCitaOdonto)){
		    	errores.add("errors.required",new ActionMessage("errors.required","El parámetro: Formato Impresión Asignación Cita Odontológica  "));
		    }
		   
		}
		/******************************************************************
		 * CARTERA
		 ******************************************************************/
		if(this.modulo==ConstantesBD.codigoModuloCartera)
		{
		    if(ripsPorFactura.equals(""))
			{
				errores.add("errors.required",new ActionMessage("errors.required","El parámetro Rips con Factura?"));
			}	
		    if(!fechaCorteSaldoInicial.equals(""))
			{
			    boolean respuesta=UtilidadFecha.validarFechaMMYYYY(fechaCorteSaldoInicial);
			    if(!respuesta)
			        errores.add("errors.fechaInvalidaMMYYYY",new ActionMessage("errors.fechaInvalidaMMYYYY",fechaCorteSaldoInicial));
			}
		}
		/******************************************************************
		 * FACTURACION
		 ******************************************************************/
		if(this.modulo==ConstantesBD.codigoModuloFacturacion)
		{
		    try
			{
				float valor=Float.parseFloat(valorUVR);
				if(valor<=0)
				{
					errores.add("UVR", new ActionMessage("errors.floatMayorQue", "El Valor del UVR","0"));
				}
				else
				{
					int index=valorUVR.indexOf(".");
					if(index>=0 && (valorUVR.length()>index+ConstantesBD.numeroDecimalesUVR))
					{
						valorUVR=valorUVR.substring(0, index+ConstantesBD.numeroDecimalesUVR+1);
						valor=Float.parseFloat(valorUVR);
						valorUVR=valor+"";
					}
					else
					{
						valorUVR=valor+"";
					}
					
				}
			}
			catch(Exception e)
			{
				errores.add("UVR", new ActionMessage("errors.floatMayorQue", "El Valor del UVR","0"));
			} 
			
			try
			{
				if(!codigoServicioFarmacia.equals(""))
				{
					// Validaciï¿½n de que sea numero entero mayor que 0
					int tempo=Integer.parseInt(codigoServicioFarmacia);
					if(tempo<=0)
					{
						errores.add("servicioFarmacia", new ActionMessage("errors.integerMayorQue", "El campo Definición del consecutivo para generar la factura de la farmacia","0"));
					}
				}
			}
			catch(NumberFormatException e)
			{
				errores.add("servicioFarmacia", new ActionMessage("errors.integer", "El campo Definición del consecutivo para generar la factura de la farmacia"));
			}
			
			
			if(!this.horaGeneraEstanciaAutomatica.trim().equals(""))
			{
				if (this.horaGeneraEstanciaAutomatica.split(":").length==1)
			    this.horaGeneraEstanciaAutomatica=this.horaGeneraEstanciaAutomatica+":00";
			    
				if(!UtilidadFecha.validacionHora(this.horaGeneraEstanciaAutomatica).puedoSeguir)
				{
					errores.add("hora", new ActionMessage("errors.formatoHoraInvalido", "para Generar Estancia Automatica "+this.horaGeneraEstanciaAutomatica));
				}
				else if(this.horaGeneraEstanciaAutomatica.compareTo("00:00")<0)
				{
					errores.add("hora", new ActionMessage("errors.horaDebeSerMayor", "para Generar Estancia Automática", "00:00"));
				}
			}
			//---------Se valida el tiempo máximo de grabaciï¿½n de registros -------------//
			if(UtilidadCadena.noEsVacio(this.tiempoMaximoGrabacion))
				{
				 if(!UtilidadFecha.validacionHora(this.tiempoMaximoGrabacion).puedoSeguir)
					{
						errores.add("hora", new ActionMessage("errors.formatoHoraInvalido", "de Tiempo Máximo Grabación Registros "+this.tiempoMaximoGrabacion));
					}
				}
			
			if (UtilidadTexto.isEmpty(requiereAutorizacionCapitacionSubcontratadaParaFacturar) ||
				requiereAutorizacionCapitacionSubcontratadaParaFacturar.trim().equals(ConstantesBD.codigoNuncaValido + "")) {
				errores.add("errors.required",new ActionMessage("errors.required","El parámetro Requiere Autorización Capitación Subcontratada para Facturar "));
			}
			
			if (UtilidadTexto.isEmpty(permitirfacturarReclamCuentaRATREC)) {
				errores.add("errors.required",new ActionMessage("errors.required","Permitir facturar y registrar reclamaciones en cuentas con R.A.T. o R.E.C. asociados en estado Pendiente"));
			}
				
		}	
		/******************************************************************
		 * MANEJO DEL PACIENTE
		 ******************************************************************/
		if(this.modulo==ConstantesBD.codigoModuloManejoPaciente)
		{
		    if (!direccionPaciente.equals(""))
			{
				if(direccionPaciente.trim().equals(""))
					{
						errores.add("errors.espaciosBlanco", new ActionMessage("errors.espaciosBlanco", "La dirección del paciente"));	
					}
			}
		    
		    if(((validarEdadDeudorPaciente.equals(ConstantesBD.acronimoSi))||(validarEdadResponsablePaciente.equals(ConstantesBD.acronimoSi)))&&(aniosBaseEdadAdulta.equals("")))
			{
				errores.add("errors.required",new ActionMessage("errors.required","El parámetro Años Base Edad Adulta "));
			}
		    
		    if(!this.horaCorteHistoricoCamas.equals(""))
		    {
		    	try
		    	{
		    		int horaCorte = Integer.parseInt(this.horaCorteHistoricoCamas);
		    		
		    		if(horaCorte<0)
		    			errores.add("errors.debeSerNumeroMayo",new ActionMessage("errors.debeSerNumeroMayorIgual","La hora de corte histórico de camas","0"));
		    		if(horaCorte>24)
		    			errores.add("errors.debeSerMenorIgualGenerico",new ActionMessage("errors.debeSerMenorIgualGenerico","La hora de corte histórico de camas","23"));
		    	}
		    	catch(Exception e)
		    	{
		    		errores.add("",new ActionMessage("errors.integer","La hora de corte histórico de camas"));
		    	}
		    }
		    
		    /*if(this.archivosPlanosReportes.trim().equals("") || this.archivosPlanosReportes.trim().equals("null"))
		    	errores.add("errors.required",new ActionMessage("errors.required","El path para el almacenamiento de archivos planos "));*/
		    	
		    if (this.minutosLimiteAlertaReserva.equals(""))
		    	errores.add("errors.required",new ActionMessage("errors.required","El parámetro Minutos límite para alerta de reserva "));
		    
		    if (this.minutosLimiteAlertaPacientePorRemitirUrgencias.equals(""))
		    	errores.add("errors.required",new ActionMessage("errors.required","El parámetro Minutos límite para alerta de paciente por remitir urgencias "));
		    
		    if (this.minutosLimiteAlertaPacientePorRemitirHospitalizacion.equals(""))
		    	errores.add("errors.required",new ActionMessage("errors.required","El parámetro Minutos límite para alerta de paciente por remitir hospitalizacion "));
		    
		    if (this.minutosLimiteAlertaPacienteConSalidaUrgencias.equals(""))
		    	errores.add("errors.required",new ActionMessage("errors.required","El parámetro Minutos límite para alerta de paciente con salida de urgencias "));
		  
		    if (this.minutosLimiteAlertaPacienteConSalidaHospitalizacion.equals(""))
		    	errores.add("errors.required",new ActionMessage("errors.required","El parámetro Minutos límite para alerta de paciente con salida de hospitalizacion "));
		    
		    if (this.tipoUsuarioaReportarSol.trim().equals(""))
		    	errores.add("errors.required",new ActionMessage("errors.required","El parámetro Tipo Usuario a Reportar en Solicitudes de Autorizacion "));
		    
		    if (UtilidadTexto.isEmpty(entidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada) || 
		    		entidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada.trim().equals(ConstantesBD.codigoNuncaValido +"")) {
		    	errores.add("errors.required",new ActionMessage("errors.required","El parámetro Entidad Subcontratada Para Centros de Costo Internos en Autorización de Capitación Subcontratada "));
		    	
			}
		    if (UtilidadTexto.isEmpty(prioridadEntidadSubcontratada) || 
					prioridadEntidadSubcontratada.trim().equals(ConstantesBD.codigoNuncaValido +"")) {
				errores.add("errors.required",new ActionMessage("errors.required","El parámetro Prioridad de la Entidad Subcontratada"));
				
			}else {
				
				int prioridad = Integer.parseInt(prioridadEntidadSubcontratada);
				if (prioridad <= 0) {
					errores.add("errors.prioridadMenorIgualCero",new ActionMessage("errors.prioridadMenorIgualCero","La Prioridad de la Entidad Subcontratada debe ser mayor a cero","150"));
				}
				
			}
		    
		    if (UtilidadTexto.isEmpty(permitirRegistrarReclamacionCuentasNoFacturadas)) {
				errores.add("errors.required",new ActionMessage("errors.required","El parámetro Permitir registrar reclamaciones en cuentas No facturadas"));
			}	    
			
		    
		    if(!UtilidadTexto.isEmpty(viaIngresoValidarNivelAutorizacionCapitacion)){
		    	if(UtilidadTexto.isEmpty(viaIngresoValidarNivelAutorizacionCapitacion)){
		    		errores.add("errors.required",new ActionMessage("errors.required","El parámetro Vía de Ingreso para validar Autorización de Capitación"));
		    	}
		    }
		    
		    if(!UtilidadTexto.isEmpty(tipoPacienteViaIngresoValidarNivelAutorizacionCapitacion)){
		    	if(UtilidadTexto.isEmpty(tipoPacienteViaIngresoValidarNivelAutorizacionCapitacion)){
		    		errores.add("errors.required",new ActionMessage("errors.required","El parámetro Tipo Paciente de la Vía de Ingreso para validar Autorización de Capitación"));
		    	}
		    }
		    
		}	
		
		/******************************************************************
		 * ADMINISTRACION
		 ******************************************************************/
		if(this.modulo==ConstantesBD.codigoModuloAdministracion)
		{
		    if(!maxPageItems.equals(""))
			{
		    	try
		    	{
		    		int numLineas=Integer.parseInt(maxPageItems);
		    		if(numLineas > 100)
		    			errores.add("MaxPageItems mayor que 100",new ActionMessage("errors.MayorIgualQue",100+""));
		    	}
		    	catch (Exception e) 
		    	{
		    		errores.add("Numero invalido",new ActionMessage("errors.invalid",maxPageItems));
				}
			}
		    
		    if (!UtilidadTexto.isEmpty(this.horaEjecProcesoInactivarUsuarioInacSistema)){
				String[] segmentos=this.horaEjecProcesoInactivarUsuarioInacSistema.split(":");
				if(segmentos.length==2)
				{
					try{
						int hora = Integer.parseInt(segmentos[0]);
						int minuto=Integer.parseInt(segmentos[1]);
						
						if((hora<0 || hora>24) || (minuto<0 || minuto>60))
						{
							errores.add("errors.invalid", new ActionMessage("errors.formatoHoraInvalido",
							"Del parámetro: Hora Ejecución Proceso Inactivación de Usuario por Inactividad en el Sistema"));
						}						
					}catch(NumberFormatException e)
					{
						errores.add("errors.invalid", new ActionMessage("errors.formatoHoraInvalido",
							"Del parámetro: Hora Ejecución Proceso Inactivación de Usuario por Inactividad en el Sistema"));
					}
					
				}else{
					errores.add("errors.invalid", new ActionMessage("errors.formatoHoraInvalido",
						"Del parámetro: Hora Ejecución Proceso Inactivación de Usuario por Inactividad en el Sistema"));
				}
			}
		    
		    if (!UtilidadTexto.isEmpty(this.horaEjeProcesoCaduContraInacSistema)){
				String[] segmentos=this.horaEjeProcesoCaduContraInacSistema.split(":");
				if(segmentos.length==2)
				{
					try{
						int hora = Integer.parseInt(segmentos[0]);
						int minuto=Integer.parseInt(segmentos[1]);
						
						if((hora<0 || hora>24) || (minuto<0 || minuto>60))
						{
							errores.add("errors.invalid", new ActionMessage("errors.formatoHoraInvalido",
							"Del parámetro: Hora Ejecución Proceso Caducidad de Contraseña por Inactividad en el Sistema"));
						}						
					}catch(NumberFormatException e)
					{
						errores.add("errors.invalid", new ActionMessage("errors.formatoHoraInvalido",
							"Del parámetro: Hora Ejecución Proceso Caducidad de Contraseña por Inactividad en el Sistema"));
					}
					
				}else{
					errores.add("errors.invalid", new ActionMessage("errors.formatoHoraInvalido",
						"Del parámetro: Hora Ejecución Proceso Caducidad de Contraseña por Inactividad en el Sistema"));
				}
			}
		}
		
		/******************************************************************
		 * SALAS DE CIRUGIA
		 ******************************************************************/
		if(this.modulo==ConstantesBD.codigoModuloSalasCirugia)
		{
			if(codigoEspecialidadAnestesiologia.equals(""))
				errores.add("especialidad anestesiología requerida",new ActionMessage("errors.required","La Especialidad de Anestesiología"));
			
			if(!this.horaInicioProgramacionSalas.trim().equals("") && !this.horaFinProgramacionSalas.trim().equals(""))
			{
			    if(!UtilidadFecha.validacionHora(this.horaInicioProgramacionSalas).puedoSeguir)
				{
					errores.add("hora", new ActionMessage("errors.formatoHoraInvalido", "de Hora Inicio Programación Salas "+this.horaInicioProgramacionSalas));
				}
				else if(this.horaInicioProgramacionSalas.compareTo("00:00")<=0)
				{
					errores.add("hora", new ActionMessage("errors.horaDebeSerMayor", "de Hora Inicio Programación Salas ", "00:00"));
				}
				if(!UtilidadFecha.validacionHora(this.horaFinProgramacionSalas).puedoSeguir)
				{
					errores.add("hora", new ActionMessage("errors.formatoHoraInvalido", "de Hora Fin Programación Salas "+this.horaFinProgramacionSalas));
				}
				else if(this.horaFinProgramacionSalas.compareTo("00:00")<=0)
				{
					errores.add("hora", new ActionMessage("errors.horaDebeSerMayor", "de Hora Fin Programación Salas ", "00:00"));
				}
			}
		}
		/******************************************************************
		 * HISTORIA CLï¿½NICA
		 ******************************************************************/
		if(this.modulo == ConstantesBD.codigoModuloHistoriaClinica)
		{
			if(ocupacionMedicoEspecialista.equals(""))
				errores.add("ocupacion medica especialista requerida",new ActionMessage("errors.required","La ocupación médico especialista"));
			if(ocupacionMedicoGeneral.equals(""))
				errores.add("ocupación médica general requerida",new ActionMessage("errors.required","La ocupación médico general"));
			if(ocupacionOdontologo.equals(""))
				errores.add("ocupación Odontológico requerido",new ActionMessage("errors.required","La ocupación Odontólogo"));
			if(ocupacionAuxiliarOdontologo.equals(""))
				errores.add("ocupación Auxiliar Odontológico requerido",new ActionMessage("errors.required","La ocupación Auxiliar de Odontólogo"));
			if(ocupacionEnfermera.equals(""))
				errores.add("ocupación enfermera requerida",new ActionMessage("errors.required","La ocupación enfermera"));
			if(ocupacionAuxiliarEnfermeria.equals(""))
				errores.add("ocupación auxiliar enfermería requerida",new ActionMessage("errors.required","La ocupación auxiliar enfermería"));
			if(redNoAdscrita.equals(""))
				errores.add("red no adscrita",new ActionMessage("errors.required","Red no adscrita"));
			if(!horasCaducidadReferenciasExternas.equals("")&&!UtilidadFecha.validacionHora(horasCaducidadReferenciasExternas).puedoSeguir)
				errores.add("horas de caducidad de referencias externas",new ActionMessage("errors.formatoHoraInvalido","del campo horas de caducidad de referencias externas"));
			if(!maxPageItemsEpicrisis.equals(""))
			{
		    	try
		    	{
		    		int numLineas=Integer.parseInt(maxPageItemsEpicrisis);
		    		if(numLineas > 10)
		    			errores.add("MaxPageItems mayor que 10",new ActionMessage("errors.MayorIgualQue",10+""));
		    	}
		    	catch (Exception e) 
		    	{
		    		errores.add("Numero invalido",new ActionMessage("errors.invalid",maxPageItemsEpicrisis));
				}
			}
			
			/**
			* Tipo Modificacion: Segun incidencia 4683
			* Autor: Jesús Darío Ríos Rosas
			* usuario: jesrioro
			* Fecha: 20/02/2013
			* Descripcion: se  valida el  ingreso  de  valores  nulos  o iguales a cero a  la  variable 
			* maximoDiasIngresosAConsultar
			**/

				if(!this.maximoDiasIngresosAConsultar.matches("\\d*")|| this.maximoDiasIngresosAConsultar.equals("")){
					errores.add("red no adscrita",new ActionMessage("errors.caracterEspecial.numerico"));
				}else {
					Integer tmp = Integer.valueOf(this.maximoDiasIngresosAConsultar);
					if (tmp<1){
						errores.add("red no adscrita",new ActionMessage("errors.caracterEspecial.numerico"));
					}else if(tmp >365){
						errores.add("tamano no valido ",new ActionMessage("errors.caracterEspecial.tamano"));
					}
				}
			
			
		}

		/******************************************************************
		 * ENFERMERï¿½A
		 ******************************************************************/
		if(this.modulo==ConstantesBD.codigoModuloEnfermeria)
		{
			if(!this.horaInicioPrimerTurno.trim().equals("") && !this.horaFinUltimoTurno.trim().equals(""))
			{
			    if(!UtilidadFecha.validacionHora(this.horaInicioPrimerTurno).puedoSeguir)
				{
					errores.add("hora", new ActionMessage("errors.formatoHoraInvalido", "de Hora Inicio Primer Turno "+this.horaInicioPrimerTurno));
				}
				else if(this.horaInicioPrimerTurno.compareTo("00:00")<=0)
				{
					errores.add("hora", new ActionMessage("errors.horaDebeSerMayor", "de Hora Inicio Primer Turno ", "00:00"));
				}
				if(!UtilidadFecha.validacionHora(this.horaFinUltimoTurno).puedoSeguir)
				{
					errores.add("hora", new ActionMessage("errors.formatoHoraInvalido", "de Hora Fin Ultimo Turno "+this.horaFinUltimoTurno));
				}
				else if(this.horaFinUltimoTurno.compareTo("00:00")<=0)
				{
					errores.add("hora", new ActionMessage("errors.horaDebeSerMayor", "de Hora Fin Ultimo Turno ", "00:00"));
				}
			}
			//---------Se valida el tiempo máximo de grabaciï¿½n de registros -------------//
			if(UtilidadCadena.noEsVacio(this.tiempoMaximoGrabacion))
				{
				 if(!UtilidadFecha.validacionHora(this.tiempoMaximoGrabacion).puedoSeguir)
					{
						errores.add("hora", new ActionMessage("errors.formatoHoraInvalido", "de Tiempo Máximo Grabación Registros "+this.tiempoMaximoGrabacion));
					}
				}
		}
		
		/******************************************************************
		 * CAPITACIï¿½N
		 ******************************************************************/
		if(this.modulo==ConstantesBD.codigoModuloCapitacion)
		{
			if(this.tipoConsecutivoCapitacion.equals("-1"))
			{
				errores.add("tipoConsecutivoCapitacion requerido",new ActionMessage("errors.required","El tipo de consecutivo a manejar"));
			}
			if(!fechaCorteSaldoInicialCapitacion.equals(""))
			{
			    boolean respuesta=UtilidadFecha.validarFechaMMYYYY(fechaCorteSaldoInicialCapitacion);
			    if(!respuesta)
			        errores.add("errors.fechaInvalidaMMYYYY",new ActionMessage("errors.fechaInvalidaMMYYYY",fechaCorteSaldoInicialCapitacion));
			}
			if(!topeConsecutivoCxCCapitacion.equals(""))
			{
			    try
			    {
			    	Double.parseDouble(topeConsecutivoCxCCapitacion);
			    }
			    catch (Exception e) {
			    	errores.add("errors.integer",new ActionMessage("errors.integer","Tope consecutivo CxC saldos Iniciales Capitación"));
				}
			}
			
			if (!UtilidadTexto.isEmpty(this.horaProcesoCierreCapitacion)){
				String[] segmentos=this.horaProcesoCierreCapitacion.split(":");
				if(segmentos.length==2)
				{
					try{
						int hora = Integer.parseInt(segmentos[0]);
						int minuto=Integer.parseInt(segmentos[1]);
						
						if((hora<0 || hora>24) || (minuto<0 || minuto>60))
						{
							errores.add("errors.invalid", new ActionMessage("errors.formatoHoraInvalido",
							"Del parámetro: Hora para Ejecutar Proceso Automático de Cierre de Capitación"));
						}						
					}catch(NumberFormatException e)
					{
						errores.add("errors.invalid", new ActionMessage("errors.formatoHoraInvalido",
							"Del parámetro: Hora para Ejecutar Proceso Automático de Cierre de Capitación"));
					}
					
				}else{
					errores.add("errors.invalid", new ActionMessage("errors.formatoHoraInvalido",
						"Del parámetro: Hora para Ejecutar Proceso Automático de Cierre de Capitación"));
				}
			}
		}
		
		/******************************************************************
		 * ORDENES
		 ******************************************************************/
		if(this.modulo==ConstantesBD.codigoModuloOrdenes)
		{
			if(this.numDiasTratamientoMedicamentos.equals(""))
			{
				errores.add("numDiasTratamientoMedicamentos requerido",new ActionMessage("errors.required","El número de días tratamiento en solicitudes de medicamentos"));
			}
			if(this.numDiasGenerarOrdenesArticulos.equals(""))
			{
				errores.add("numDiasGenerarOrdenesArticulos requerido",new ActionMessage("errors.required","El número máximo de días para generar solicitudes de órdenes ambulatorias de artículos"));
			}
			//Según ANEXO 167 se quita el requerido de este parametro
//			if(this.numDiasEgresoOrdenesAmbulatorias.equals(""))
//			{
//				errores.add("numDiasEgresoOrdenesAmbulatorias requerido",new ActionMessage("errors.required","El número de dï¿½as de egreso para generar ï¿½rdenes ambulatorias"));
//			}
			if(this.obligarRegIncapaPacienteHospitalizado == null || this.obligarRegIncapaPacienteHospitalizado.equals("")){
				errores.add("",new ActionMessage("errors.required","la obligacion de gererar incapacidad en pacientes hospitalizados"));
			}
		}
		
		/******************************************************************
		 * INVENTARIOS
		 ******************************************************************/
		if(this.modulo == ConstantesBD.codigoModuloInventarios)
		{
			/*if(this.archivosPlanosReportesInventarios.trim().equals("") || this.archivosPlanosReportesInventarios.trim().equals("null"))
		    	errores.add("errors.required",new ActionMessage("errors.required","El path para el almacenamiento de archivos planos "));*/
		}
		
		/******************************************************************
		 * GLOSAS
		 ******************************************************************/
		if(this.modulo == ConstantesBD.codigoModuloGlosas)
		{
			logger.info("===> Valida Auditor: "+this.validarAuditor);
			logger.info("===> Validar Usuario Glosa: "+this.validarUsuarioGlosa);
			logger.info("===> Numero Glosas Registrada X Factura: "+this.numeroGlosasRegistradasXFactura);
			logger.info("===> Numero Dias Notificar Glosas: "+this.numeroDiasNotificarGlosa);
			
			if(this.validarAuditor.equals(""))
			{
				errores.add("validarAuditor requerido",new ActionMessage("errors.required","Validar Auditor"));
			}
			
			else if(this.validarUsuarioGlosa.equals(""))
			{
				errores.add("validarUsuarioGlosa requerido",new ActionMessage("errors.required","Validar Usuario Glosa"));
			}
			
			else if(this.numeroGlosasRegistradasXFactura.equals(""))
			{
				errores.add("numeroGlosasRegistradasXFactura requerido",new ActionMessage("errors.required","Numero Glosas Registradas Por Factura"));
			}
			
			else if(this.numeroDiasNotificarGlosa.equals(""))
			{
				errores.add("numeroDiasNotificarGlosa requerido",new ActionMessage("errors.required","Numero de Dias para Notificacion Glosa"));
			}
			
			else if(Integer.parseInt(this.numeroDiasNotificarGlosa)<=0)
			{
				errores.add("numeroDiasNotificarGlosa requerido",new ActionMessage("errors.required","El Numero de Dias para Notificar Glosa debe ser mayor a 0. "));
			}
			
			else if(Integer.parseInt(this.numeroGlosasRegistradasXFactura)<=0)
			{
				errores.add("numeroGlosasRegistradasXFactura requerido",new ActionMessage("errors.required","El Numero Glosas Registradas por Factura debe ser mayor a 0. "));
			}
			if(this.imprimirFirmasImpresionRespuesGlosa.equals(ConstantesBD.acronimoSi))
			{
				HashMap<String, Object> mapaAux = new HashMap<String, Object>();
				ParametrosFirmasImpresionResp mundoImpresionResp = new ParametrosFirmasImpresionResp();
				mapaAux = mundoImpresionResp.listadoFirmasSinConexion();
				if(Utilidades.convertirAEntero(mapaAux.get("numRegistros")+"")<=0)
					errores.add("imprimirFirmasImpresionRespuestaGlosa",new ActionMessage("prompt.generico","Es requerido que por lo menos se parametrice una Firma para el Parametro General Imprimir Firmas."));
			}
		}
		
		/******************************************************************
		 * ODONTOLOGÍA
		 ******************************************************************/
		if(this.modulo==ConstantesBD.codigoModuloOdontologia)
		{
			if(this.edadFinalNinez.equals(""))
			{
				errores.add("Edad Final Niñez requerido",new ActionMessage("errors.required","La Edad Final Niñez"));
			}
			else
			{
				if(Utilidades.convertirAEntero(this.edadFinalNinez) == ConstantesBD.codigoNuncaValido)
					errores.add("Edad Final Niñez entero",new ActionMessage("errors.integer","La Edad Final Niñez"));
			}
			if(this.edadInicioAdulto.equals(""))
			{
				errores.add("Edad Inicio Adulto requerido",new ActionMessage("errors.required","La Edad Inicial Adulto"));
			}
			else
			{
				if(Utilidades.convertirAEntero(this.edadInicioAdulto) == ConstantesBD.codigoNuncaValido)
					errores.add("Edad Final Niñez entero",new ActionMessage("errors.integer","La Edad Final Niñez"));
			}
			if(errores.isEmpty())
			{
				if(Utilidades.convertirAEntero(this.edadFinalNinez) >= Utilidades.convertirAEntero(this.edadInicioAdulto))
					errores.add("",new ActionMessage("errors.integerMayorQue","La Edad Inicio Adulto","La Edad Final Niñez"));
			}
			
			if(this.institucionRegistraAtencionExterna.equals(""))
			{
				errores.add("", new ActionMessage("errors.required","El campo Institución Registra Atención Externa"));
			}
			if(this.validarPacienteParaVentaTarjeta.equals(""))
			{
				errores.add("", new ActionMessage("errors.required","El campo Validar Paciente para Venta Tarjeta"));
			}
			if(this.reciboCajaAutomaticoVentaTarjeta.equals(""))
			{
				errores.add("", new ActionMessage("errors.required","El campo Recibo Caja Automatico Venta Tarjeta Externa"));
			}
			if (UtilidadTexto.isEmpty(this.modificarFechaHoraInicioAtencionOdonto)) {
				errores.add("", new ActionMessage("errors.required","El campo Permite modificar la Fecha y Hora del inicio de la atención odontológica "));
			}
			if (UtilidadTexto.isEmpty(this.solicitudCitaInterconsultaOdontoCitaProgramada)) {
				errores.add("", new ActionMessage("errors.required","El parámetro: Para solicitud de citas de Interconsulta Odontológica se genera cita programada? "));
			}
			
		}
		
		/******************************************************************
		 * CARTERA PACIENTE
		 ******************************************************************/
		if(this.modulo==ConstantesBD.codigoModuloCarteraPaciente)
		{
			if(Utilidades.convertirAEntero(this.maximoNumeroCuotasFinanciacion) == ConstantesBD.codigoNuncaValido)
				errores.add("El Máximo número Cuotas Financiación",new ActionMessage("errors.integer","El Máximo número Cuotas Financiación"));
			if(Utilidades.convertirAEntero(this.maximoNumeroDiasFinanciacionPorCuota) == ConstantesBD.codigoNuncaValido)
				errores.add("El Máximo número Días Financiación Por Cuota",new ActionMessage("errors.integer","El Máximo número Días Financiación Por Cuota"));
		}
		
		/******************************************************************
		 * TESORERIA
		 ******************************************************************/
		if(this.modulo==ConstantesBD.codigoModuloTesoreria)
		{
		    if (this.tamanioImpresionRC.isEmpty())
		    	errores.add("El tamaño de impresión de RC",new ActionMessage("errors.required","El tamaño de impresión de recibo de caja "));
		}
		
		
		/*************************************************************************************************
		 * FACTURAS VARIAS 
		 *************************************************************************************************/
		
		if(this.modulo==ConstantesBD.codigoModuloFacturasVarias)
		{
			if( this.reciboCajaAutomaticoGeneracionFacturaVaria.equals(ConstantesBD.acronimoSi)) 
			{
				if( UtilidadTexto.isEmpty(this.conceptoIngresoFacturasVarias) )
				{
					errores.add("",	new ActionMessage("errors.required"," Concepto Ingreso Facturas Varias"));	
				}
			}
			
			if(UtilidadTexto.isEmpty(this.formatoFacturaVaria)){
				errores.add("",	new ActionMessage("errors.required"," El parámetro: Formato Factura Varia"));	
			}
		}
		
		return errores;
	}
	
	
	
	/**
	 * @return Returns the flagInfoAdicIngresoConvenios.
	 */
	public String getFlagInfoAdicIngresoConvenios()
	{
		return flagInfoAdicIngresoConvenios;
	}
	/**
	 * @param flagInfoAdicIngresoConvenios The flagInfoAdicIngresoConvenios to set.
	 */
	public void setFlagInfoAdicIngresoConvenios(String flagInfoAdicIngresoConvenios)
	{
		this.flagInfoAdicIngresoConvenios=flagInfoAdicIngresoConvenios;
	}
	/**
	 * @return Returns the diasPreviosNotificacionProximoControl.
	 */
	public String getDiasPreviosNotificacionProximoControl()
	{
		return diasPreviosNotificacionProximoControl;
	}
	/**
	 * @param diasPreviosNotificacionProximoControl The diasPreviosNotificacionProximoControl to set.
	 */
	public void setDiasPreviosNotificacionProximoControl(String diasPreviosNotificacionProximoControl)
	{
		this.diasPreviosNotificacionProximoControl=diasPreviosNotificacionProximoControl;
	}
	/**
	 * @return Retorna causaExterna.
	 */
	public String getCausaExterna()
	{
		return causaExterna;
	}
	/**
	 * @param causaExterna Asigna causaExterna.
	 */
	public void setCausaExterna(String causaExterna)
	{
		this.causaExterna = causaExterna;
	}
	/**
	 * @return Retorna centroCostoConsultaExterna.
	 */
	public String getCentroCostoConsultaExterna()
	{
		return centroCostoConsultaExterna;
	}
	/**
	 * @param centroCostoConsultaExterna Asigna centroCostoConsultaExterna.
	 */
	public void setCentroCostoConsultaExterna(String centroCostoConsultaExterna)
	{
		this.centroCostoConsultaExterna = centroCostoConsultaExterna;
	}
	/**
	 * @return Retorna centroCostoUrgencias.
	 */
	public String getCentroCostoUrgencias()
	{
		return centroCostoUrgencias;
	}
	/**
	 * @param centroCostoUrgencias Asigna centroCostoUrgencias.
	 */
	public void setCentroCostoUrgencias(String centroCostoUrgencias)
	{
		this.centroCostoUrgencias = centroCostoUrgencias;
	}
	/**
	 * @return Retorna ciudadNacimiento.
	 */
	public String getCiudadNacimiento()
	{
		return ciudadNacimiento;
	}
	/**
	 * @param ciudadNacimiento Asigna ciudadNacimiento.
	 */
	public void setCiudadNacimiento(String ciudadNacimiento)
	{
		this.ciudadNacimiento = ciudadNacimiento;
	}
	/**
	 * @return Retorna ciudadVivienda.
	 */
	public String getCiudadVivienda()
	{
		return ciudadVivienda;
	}
	/**
	 * @param ciudadVivienda Asigna ciudadVivienda.
	 */
	public void setCiudadVivienda(String ciudadVivienda)
	{
		this.ciudadVivienda = ciudadVivienda;
	}
	/**
	 * @return Retorna direccionPaciente.
	 */
	public String getDireccionPaciente()
	{
		return direccionPaciente;
	}
	/**
	 * @param direccionPaciente Asigna direccionPaciente.
	 */
	public void setDireccionPaciente(String direccionPaciente)
	{
		this.direccionPaciente = direccionPaciente;
	}
	/**
	 * @return Retorna estado.
	 */
	public String getEstado()
	{
		return estado;
	}
	/**
	 * @param estado Asigna estado.
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}
	/**
	 * @return Retorna fechaNacimiento.
	 */
	public String getFechaNacimiento()
	{
		return fechaNacimiento;
	}
	/**
	 * @param fechaNacimiento Asigna fechaNacimiento.
	 */
	public void setFechaNacimiento(String fechaNacimiento)
	{
		this.fechaNacimiento = fechaNacimiento;
	}
	/**
	 * @return Retorna finalidad.
	 */
	public String getFinalidad()
	{
		return finalidad;
	}
	/**
	 * @param finalidad Asigna finalidad.
	 */
	public void setFinalidad(String finalidad)
	{
		this.finalidad = finalidad;
	}
	/**
	 * @return Retorna flagCentinela.
	 */
	public String getFlagCentinela()
	{
		return flagCentinela;
	}
	/**
	 * @param flagCentinela Asigna flagCentinela.
	 */
	public void setFlagCentinela(String flagCentinela)
	{
		this.flagCentinela = flagCentinela;
	}
	/**
	 * @return Retorna historiaClinica.
	 */
	public String getHistoriaClinica()
	{
		return historiaClinica;
	}
	/**
	 * @param historiaClinica Asigna historiaClinica.
	 */
	public void setHistoriaClinica(String historiaClinica)
	{
		this.historiaClinica = historiaClinica;
	}
	/**
	 * @return Retorna ocupacion.
	 */
	public String getOcupacion()
	{
		return ocupacion;
	}
	/**
	 * @param ocupacion Asigna ocupacion.
	 */
	public void setOcupacion(String ocupacion)
	{
		this.ocupacion = ocupacion;
	}
	/**
	 * @return Retorna tipoId.
	 */
	public String getTipoId()
	{
		return tipoId;
	}
	/**
	 * @param tipoId Asigna tipoId.
	 */
	public void setTipoId(String tipoId)
	{
		this.tipoId = tipoId;
	}
	/**
	 * @return Retorna zonaDomicilio.
	 */
	public String getZonaDomicilio()
	{
		return zonaDomicilio;
	}
	/**
	 * @param zonaDomicilio Asigna zonaDomicilio.
	 */
	public void setZonaDomicilio(String zonaDomicilio)
	{
		this.zonaDomicilio = zonaDomicilio;
	}
	/**
	 * @return Retorna ocupacionSolicitada.
	 */
	public String getOcupacionSolicitada()
	{
		return ocupacionSolicitada;
	}
	/**
	 * @param ocupacionSolicitada Asigna ocupacionSolicitada.
	 */
	public void setOcupacionSolicitada(String ocupacionSolicitada)
	{
		this.ocupacionSolicitada = ocupacionSolicitada;
	}
	/**
	 * @return Retorna codigoEstadoCama.
	 */
	public String getCodigoEstadoCama()
	{
		return codigoEstadoCama;
	}
	/**
	 * @param codigoEstadoCama Asigna codigoEstadoCama.
	 */
	public void setCodigoEstadoCama(String codigoEstadoCama)
	{
		this.codigoEstadoCama = codigoEstadoCama;
	}
	/**
	 * @return Retorna valorUVR.
	 */
	public String getValorUVR()
	{
		return valorUVR;
	}
	/**
	 * @param valorUVR Asigna valorUVR.
	 */
	public void setValorUVR(String valorUVR)
	{
		this.valorUVR = valorUVR;
	}
//	Se deshabilita debido a q ya no se esta utilizando el parametro Carnet_Requerido en el modulo Manejo Paciente	
	/**
	public String getCarnetRequerido()
	{
		return carnetRequerido;
	}
	
	public void setCarnetRequerido(String carnetRequerido)
	{
		this.carnetRequerido = carnetRequerido;
	}
	*/
	
	/**
	 * @return Retorna codigoServicioFarmacia.
	 */
	public String getCodigoServicioFarmacia()
	{
		return codigoServicioFarmacia;
	}
	/**
	 * @param codigoServicioFarmacia Asigna codigoServicioFarmacia.
	 */
	public void setCodigoServicioFarmacia(String codigoServicioFarmacia)
	{
		this.codigoServicioFarmacia = codigoServicioFarmacia;
	}
	/**
	 * @return Retorna validarEstadoSolicitudesInterpretadas.
	 */
	public String getValidarEstadoSolicitudesInterpretadas()
	{
		return validarEstadoSolicitudesInterpretadas;
	}
	/**
	 * @param validarEstadoSolicitudesInterpretadas Asigna validarEstadoSolicitudesInterpretadas.
	 */
	public void setValidarEstadoSolicitudesInterpretadas(String validarEstadoSolicitudesInterpretadas)
	{
		this.validarEstadoSolicitudesInterpretadas = validarEstadoSolicitudesInterpretadas;
	}
    /**
     * @return Retorna el/la validarContratosVencidos.
     */
    public String getValidarContratosVencidos() {
        return validarContratosVencidos;
    }
    /**
     * El/La validarContratosVencidos a establecer.
     * @param validarContratosVencidos 
     */
    public void setValidarContratosVencidos(String validarContratosVencidos) {
        this.validarContratosVencidos = validarContratosVencidos;
    }
	/**
	 * @return Retorna manejoTopesPaciente.
	 */
	public String getManejoTopesPaciente()
	{
		return manejoTopesPaciente;
	}
	/**
	 * @param manejoTopesPaciente Asigna manejoTopesPaciente.
	 */
	public void setManejoTopesPaciente(String manejoTopesPaciente)
	{
		this.manejoTopesPaciente = manejoTopesPaciente;
	}
    /**
     * @return Returns the centroCostoAmbulatorios.
     */
    public String getCentroCostoAmbulatorios() {
        return centroCostoAmbulatorios;
    }
    /**
     * @param centroCostoAmbulatorios The centroCostoAmbulatorios to set.
     */
    public void setCentroCostoAmbulatorios(String centroCostoAmbulatorios) {
        this.centroCostoAmbulatorios = centroCostoAmbulatorios;
    }
    
	/**
	 * @return Returns the justificacionServiciosRequerida.
	 */
	public String getJustificacionServiciosRequerida() {
		return justificacionServiciosRequerida;
	}
	/**
	 * @param justificacionServiciosRequerida The justificacionServiciosRequerida to set.
	 */
	public void setJustificacionServiciosRequerida(
			String justificacionServiciosRequerida) {
		this.justificacionServiciosRequerida = justificacionServiciosRequerida;
	}
	/**
	 * @return Retorna ingresoCantidadFarmacia.
	 */
	public String getIngresoCantidadFarmacia()
	{
		return ingresoCantidadFarmacia;
	}
	/**
	 * @param ingresoCantidadFarmacia Asigna ingresoCantidadFarmacia.
	 */
	public void setIngresoCantidadFarmacia(String ingresoCantidadFarmacia)
	{
		this.ingresoCantidadFarmacia = ingresoCantidadFarmacia;
	}
	/**
	 * @return Returns the ripsPorFactura.
	 */
	public String getRipsPorFactura() {
		return ripsPorFactura;
	}
	/**
	 * @param ripsPorFactura The ripsPorFactura to set.
	 */
	public void setRipsPorFactura(String ripsPorFactura) {
		this.ripsPorFactura = ripsPorFactura;
	}
    /**
     * @return Retorna fechaCorteSaldoInicial.
     */
    public String getFechaCorteSaldoInicial() {
        return fechaCorteSaldoInicial;
    }
    /**
     * @param fechaCorteSaldoInicial Asigna fechaCorteSaldoInicial.
     */
    public void setFechaCorteSaldoInicial(String fechaCorteSaldoInicial) {
        this.fechaCorteSaldoInicial = fechaCorteSaldoInicial;
    }
    /**
     * @return Retorna topeConsecutivoCxC.
     */
    public String getTopeConsecutivoCxC() {
        return topeConsecutivoCxC;
    }
    /**
     * @return Retorna maxPageItems.
     */
    public String getMaxPageItems() {
        return maxPageItems;
    }
    /**
     * @param topeConsecutivoCxC Asigna topeConsecutivoCxC.
     */
    public void setTopeConsecutivoCxC(String topeConsecutivoCxC) {
        this.topeConsecutivoCxC = topeConsecutivoCxC;
    }
    /**
     * @param maxPageItems Asigna maxPageItems.
     */
    public void setMaxPageItems(String maxPageItems) {
        this.maxPageItems = maxPageItems;
    }
    /**
     * @return Retorna genExcepcionesFarmAut.
     */
    public String getGenExcepcionesFarmAut() {
        return genExcepcionesFarmAut;
    }
    /**
     * @param genExcepcionesFarmAut Asigna genExcepcionesFarmAut.
     */
    public void setGenExcepcionesFarmAut(String genExcepcionesFarmAut) {
        this.genExcepcionesFarmAut = genExcepcionesFarmAut;
    }
	/**
	 * @return Returns the excepcionRipsConsultorios.
	 */
	public String getExcepcionRipsConsultorios() {
		return excepcionRipsConsultorios;
	}
	/**
	 * @param excepcionRipsConsultorios The excepcionRipsConsultorios to set.
	 */
	public void setExcepcionRipsConsultorios(String excepcionRipsConsultorios) {
		this.excepcionRipsConsultorios = excepcionRipsConsultorios;
	}
	/**
	 * @return Returns the ajustarCuentaCobroRadicada.
	 */
	public String getAjustarCuentaCobroRadicada() {
		return ajustarCuentaCobroRadicada;
	}
	/**
	 * @param ajustarCuentaCobroRadicada The ajustarCuentaCobroRadicada to set.
	 */
	public void setAjustarCuentaCobroRadicada(String ajustarCuentaCobroRadicada) {
		this.ajustarCuentaCobroRadicada = ajustarCuentaCobroRadicada;
	}
	/**
	 * @return Returns the cerrarCuentaAnulacionFactura.
	 */
	public String getCerrarCuentaAnulacionFactura() {
		return cerrarCuentaAnulacionFactura;
	}
	/**
	 * @param cerrarCuentaAnulacionFactura The cerrarCuentaAnulacionFactura to set.
	 */
	public void setCerrarCuentaAnulacionFactura(
			String cerrarCuentaAnulacionFactura) {
		this.cerrarCuentaAnulacionFactura = cerrarCuentaAnulacionFactura;
	}
	/**
	 * @return Returns the barrioResidencia.
	 */
	public String getBarrioResidencia() {
		return barrioResidencia;
	}
	/**
	 * @param barrioResidencia The barrioResidencia to set.
	 */
	public void setBarrioResidencia(String barrioResidencia) {
		this.barrioResidencia = barrioResidencia;
	}
	
	/**
	 * @return Returns the materialesPorActo.
	 */
	public String getMaterialesPorActo() {
		return materialesPorActo;
	}
	/**
	 * @param materialesPorActo The materialesPorActo to set.
	 */
	public void setMaterialesPorActo(String materialesPorActo) {
		this.materialesPorActo = materialesPorActo;
	}
	/**
	 * @return Retorna horaInicioProgramacionSalas.
	 */
	public String getHoraInicioProgramacionSalas() {
		return horaInicioProgramacionSalas;
	}
	/**
	 * @param Asigna horaInicioProgramacionSalas.
	 */
	public void setHoraInicioProgramacionSalas(
			String horaInicioProgramacionSalas) {
		this.horaInicioProgramacionSalas = horaInicioProgramacionSalas;
	}
	/**
	 * @return Retorna horaFinProgramacionSalas.
	 */
	public String getHoraFinProgramacionSalas() {
		return horaFinProgramacionSalas;
	}
	/**
	 * @param Asigna horaFinProgramacionSalas.
	 */
	public void setHoraFinProgramacionSalas(String horaFinProgramacionSalas) {
		this.horaFinProgramacionSalas = horaFinProgramacionSalas;
	}
	/**
	 * @return Returns the codigoEspecialidadAnestesiologia.
	 */
	public String getCodigoEspecialidadAnestesiologia()
	{
		return codigoEspecialidadAnestesiologia;
	}
	/**
	 * @param codigoEspecialidadAnestesiologia The codigoEspecialidadAnestesiologia to set.
	 */
	public void setCodigoEspecialidadAnestesiologia(
			String codigoEspecialidadAnestesiologia)
	{
		this.codigoEspecialidadAnestesiologia = codigoEspecialidadAnestesiologia;
	}
    /**
     * @return Retorna modulo.
     */
    public int getModulo() {
        return modulo;
    }
    /**
     * @param modulo Asigna modulo.
     */
    public void setModulo(int modulo) {
        this.modulo = modulo;
    }
    /**
     * @return Retorna nombreModulo.
     */
    public String getNombreModulo() {
        return nombreModulo;
    }
    /**
     * @param nombreModulo Asigna nombreModulo.
     */
    public void setNombreModulo(String nombreModulo) {
        this.nombreModulo = nombreModulo;
    }
    /**
     * @return Retorna mapaEtiquetas.
     */
    public HashMap getMapaEtiquetas() {
        return mapaEtiquetas;
    }
    /**
     * @param mapaEtiquetas Asigna mapaEtiquetas.
     */
    public void setMapaEtiquetas(HashMap mapaEtiquetas) {
        this.mapaEtiquetas = mapaEtiquetas;
    }
    /**
     * @return Retorna mapaEtiquetas.
     */
    public Object getMapaEtiquetas(String key) {
        return mapaEtiquetas.get(key);
    }
    /**
     * @param mapaEtiquetas Asigna mapaEtiquetas.
     */
    public void setMapaEtiquetas(String key,Object value) {
        this.mapaEtiquetas.put(key,value);
    }
    /**
     * @return Retorna manejoConsecTransInv.
     */
    public String getManejoConsecTransInv() {
        return manejoConsecTransInv;
    }
    /**
     * @param manejoConsecTransInv Asigna manejoConsecTransInv.
     */
    public void setManejoConsecTransInv(String manejoConsecTransInv) {
        this.manejoConsecTransInv = manejoConsecTransInv;
    }
    /**
     * @return Retorna porcentajesCostoInv.
     */
    public String getPorcentajesCostoInv() {
        return porcentajesCostoInv;
    }
    /**
     * @param porcentajesCostoInv Asigna porcentajesCostoInv.
     */
    public void setPorcentajesCostoInv(String porcentajesCostoInv) {
        this.porcentajesCostoInv = porcentajesCostoInv;
    }
    /**
     * @return Retorna codigoTransSolicPacientes.
     */
    public String getCodigoTransSolicPacientes() {
        return codigoTransSolicPacientes;
    }
    /**
     * @param codigoTransSolicPacientes Asigna codigoTransSolicPacientes.
     */
    public void setCodigoTransSolicPacientes(String codigoTransSolicPacientes) {
        this.codigoTransSolicPacientes = codigoTransSolicPacientes;
    }    
    /**
     * @return Retorna codigoTransDevolPacientes.
     */
    public String getCodigoTransDevolPacientes() {
        return codigoTransDevolPacientes;
    }
    /**
     * @param codigoTransDevolPacientes Asigna codigoTransDevolPacientes.
     */
    public void setCodigoTransDevolPacientes(String codigoTransDevolPacientes) {
        this.codigoTransDevolPacientes = codigoTransDevolPacientes;
    }
    /**
     * @return Retorna codigoTransPedidos.
     */
    public String getCodigoTransPedidos() {
        return codigoTransPedidos;
    }
    /**
     * @param codigoTransPedidos Asigna codigoTransPedidos.
     */
    public void setCodigoTransPedidos(String codigoTransPedidos) {
        this.codigoTransPedidos = codigoTransPedidos;
    }
    /**
     * @return Retorna codigoTransDvolPedidos.
     */
    public String getCodigoTransDvolPedidos() {
        return codigoTransDvolPedidos;
    }
    /**
     * @param codigoTransDvolPedidos Asigna codigoTransDvolPedidos.
     */
    public void setCodigoTransDvolPedidos(String codigoTransDvolPedidos) {
        this.codigoTransDvolPedidos = codigoTransDvolPedidos;
    }
    /**
     * @return Retorna permitirModificarFechaInv.
     */
    public String getPermitirModificarFechaInv() {
        return permitirModificarFechaInv;
    }
    /**
     * @param permitirModificarFechaInv Asigna permitirModificarFechaInv.
     */
    public void setPermitirModificarFechaInv(String permitirModificarFechaInv) {
        this.permitirModificarFechaInv = permitirModificarFechaInv;
    }
    /**
     * @return Retorna porcentajePuntoPedido.
     */
    public String getPorcentajePuntoPedido() {
        return porcentajePuntoPedido;
    }
    /**
     * @param porcentajePuntoPedido Asigna porcentajePuntoPedido.
     */
    public void setPorcentajePuntoPedido(String porcentajePuntoPedido) {
        this.porcentajePuntoPedido = porcentajePuntoPedido;
    }
    /**
     * @return Retorna codigoTransTrasladoAlmacenes.
     */
    public String getCodigoTransTrasladoAlmacenes() {
        return codigoTransTrasladoAlmacenes;
    }
    /**
     * @param codigoTransTrasladoAlmacenes Asigna codigoTransTrasladoAlmacenes.
     */
    public void setCodigoTransTrasladoAlmacenes(
            String codigoTransTrasladoAlmacenes) {
        this.codigoTransTrasladoAlmacenes = codigoTransTrasladoAlmacenes;
    }
	/**
	 * @return Returns the ocupacionAuxiliarEnfermeria.
	 */
	public String getOcupacionAuxiliarEnfermeria() {
		return ocupacionAuxiliarEnfermeria;
	}
	/**
	 * @param ocupacionAuxiliarEnfermeria The ocupacionAuxiliarEnfermeria to set.
	 */
	public void setOcupacionAuxiliarEnfermeria(
			String ocupacionAuxiliarEnfermeria) {
		this.ocupacionAuxiliarEnfermeria = ocupacionAuxiliarEnfermeria;
	}
	/**
	 * @return Returns the ocupacionEnfermera.
	 */
	public String getOcupacionEnfermera() {
		return ocupacionEnfermera;
	}
	/**
	 * @param ocupacionEnfermera The ocupacionEnfermera to set.
	 */
	public void setOcupacionEnfermera(String ocupacionEnfermera) {
		this.ocupacionEnfermera = ocupacionEnfermera;
	}
	/**
	 * @return Returns the ocupacionMedicoEspecialista.
	 */
	public String getOcupacionMedicoEspecialista() {
		return ocupacionMedicoEspecialista;
	}
	/**
	 * @param ocupacionMedicoEspecialista The ocupacionMedicoEspecialista to set.
	 */
	public void setOcupacionMedicoEspecialista(
			String ocupacionMedicoEspecialista) {
		this.ocupacionMedicoEspecialista = ocupacionMedicoEspecialista;
	}
	/**
	 * @return Returns the ocupacionMedicoGeneral.
	 */
	public String getOcupacionMedicoGeneral() {
		return ocupacionMedicoGeneral;
	}
	/**
	 * @param ocupacionMedicoGeneral The ocupacionMedicoGeneral to set.
	 */
	public void setOcupacionMedicoGeneral(String ocupacionMedicoGeneral) {
		this.ocupacionMedicoGeneral = ocupacionMedicoGeneral;
	}

	/**
	 * @return Retorna horaFinUltimoTurno.
	 */
	public String getHoraFinUltimoTurno()
	{
		return horaFinUltimoTurno;
	}

	/**
	 * @param horaFinUltimoTurno Asigna horaFinUltimoTurno.
	 */
	public void setHoraFinUltimoTurno(String horaFinUltimoTurno)
	{
		this.horaFinUltimoTurno = horaFinUltimoTurno;
	}

	/**
	 * @return Retorna horaInicioPrimerTurno.
	 */
	public String getHoraInicioPrimerTurno()
	{
		return horaInicioPrimerTurno;
	}

	/**
	 * @param horaInicioPrimerTurno Asigna horaInicioPrimerTurno.
	 */
	public void setHoraInicioPrimerTurno(String horaInicioPrimerTurno)
	{
		this.horaInicioPrimerTurno = horaInicioPrimerTurno;
	}
    /**
     * @return Returns the alertaCasoVigilancia.
     */
    public String getAlertaCasoVigilancia() {
        return alertaCasoVigilancia;
    }
    /**
     * @param alertaCasoVigilancia The alertaCasoVigilancia to set.
     */
    public void setAlertaCasoVigilancia(String alertaCasoVigilancia) {
        this.alertaCasoVigilancia = alertaCasoVigilancia;
    }
    /**
     * @return Returns the alertaFichasPendientes.
     */
    public String getAlertaFichasPendientes() {
        return alertaFichasPendientes;
    }
    /**
     * @param alertaFichasPendientes The alertaFichasPendientes to set.
     */
    public void setAlertaFichasPendientes(String alertaFichasPendientes) {
        this.alertaFichasPendientes = alertaFichasPendientes;
    }
    /**
     * @return Returns the vigilarAccRabico.
     */
    public String getVigilarAccRabico() {
        return vigilarAccRabico;
    }
    /**
     * @param vigilarAccRabico The vigilarAccRabico to set.
     */
    public void setVigilarAccRabico(String vigilarAccRabico) {
        this.vigilarAccRabico = vigilarAccRabico;
    }

	/**
	 * @return Retorna minutosEsperaCitaCaduca.
	 */
	public String getMinutosEsperaCitaCaduca() {
		return minutosEsperaCitaCaduca;
	}

	/**
	 * @param Asigna minutosEsperaCitaCaduca.
	 */
	public void setMinutosEsperaCitaCaduca(String minutosEsperaCitaCaduca) {
		this.minutosEsperaCitaCaduca = minutosEsperaCitaCaduca;
	}

	/**
	 * @return Retorna the tiempoMaximoGrabacion.
	 */
	public String getTiempoMaximoGrabacion()
	{
		return tiempoMaximoGrabacion;
	}

	/**
	 * @param tiempoMaximoGrabacion The tiempoMaximoGrabacion to set.
	 */
	public void setTiempoMaximoGrabacion(String tiempoMaximoGrabacion)
	{
		this.tiempoMaximoGrabacion = tiempoMaximoGrabacion;
	}

	/**
	 * @return Retorna ingresoCantidadSolMedicamentos.
	 */
	public String getIngresoCantidadSolMedicamentos()
	{
		return ingresoCantidadSolMedicamentos;
	}

	/**
	 * @param ingresoCantidadSolMedicamentos Asigna ingresoCantidadSolMedicamentos.
	 */
	public void setIngresoCantidadSolMedicamentos(
			String ingresoCantidadSolMedicamentos)
	{
		this.ingresoCantidadSolMedicamentos = ingresoCantidadSolMedicamentos;
	}

	/**
	 * @return Retorna the tipoConsecutivoCapitacion.
	 */
	public String getTipoConsecutivoCapitacion()
	{
		return tipoConsecutivoCapitacion;
	}

	/**
	 * @param tipoConsecutivoCapitacion The tipoConsecutivoCapitacion to set.
	 */
	public void setTipoConsecutivoCapitacion(String tipoConsecutivoCapitacion)
	{
		this.tipoConsecutivoCapitacion = tipoConsecutivoCapitacion;
	}


	/**
	 * @return Returns the minutosEsperaCuentasProcFact.
	 */
	public String getMinutosEsperaCuentasProcFact() {
		return minutosEsperaCuentasProcFact;
	}

	/**
	 * @param minutosEsperaCuentasProcFact The minutosEsperaCuentasProcFact to set.
	 */
	public void setMinutosEsperaCuentasProcFact(String minutosEsperaCuentasProcFact) {
		this.minutosEsperaCuentasProcFact = minutosEsperaCuentasProcFact;
	}

	/**
	 * @return Returns the fechaCorteSaldoInicialCapitacion.
	 */
	public String getFechaCorteSaldoInicialCapitacion() {
		return fechaCorteSaldoInicialCapitacion;
	}

	/**
	 * @param fechaCorteSaldoInicialCapitacion The fechaCorteSaldoInicialCapitacion to set.
	 */
	public void setFechaCorteSaldoInicialCapitacion(
			String fechaCorteSaldoInicialCapitacion) {
		this.fechaCorteSaldoInicialCapitacion = fechaCorteSaldoInicialCapitacion;
	}

	/**
	 * @return Returns the topeConsecutivoCxCCapitacion.
	 */
	public String getTopeConsecutivoCxCCapitacion() {
		return topeConsecutivoCxCCapitacion;
	}

	/**
	 * @param topeConsecutivoCxCCapitacion The topeConsecutivoCxCCapitacion to set.
	 */
	public void setTopeConsecutivoCxCCapitacion(String topeConsecutivoCxCCapitacion) {
		this.topeConsecutivoCxCCapitacion = topeConsecutivoCxCCapitacion;
	}
	
	/**
	 * @return Returns the centroCostoTerceros.
	 */
	public HashMap getCentroCostoTerceros() {
		return centroCostoTerceros;
	}

	/**
	 * @param centroCostoTerceros The centroCostoTerceros to set.
	 */
	public void setCentroCostoTerceros(HashMap centroCostoTerceros) {
		this.centroCostoTerceros = centroCostoTerceros;
	}
	
	/**
	 * @return Returns the centroCostoTerceros.
	 */
	public Object getCentroCostoTerceros(Object key) {
		return centroCostoTerceros.get(key);
	}

	/**
	 * @param centroCostoTerceros The centroCostoTerceros to set.
	 */
	public void setCentroCostoTerceros(Object key, Object value) {
		this.centroCostoTerceros.put(key, value);
	}

	/**
	 * @return Returns the horasReproceso.
	 */
	public HashMap getHorasReproceso() {
		return horasReproceso;
	}

	/**
	 * @param horasReproceso The horasReproceso to set.
	 */
	public void setHorasReproceso(HashMap horasReproceso) {
		this.horasReproceso = horasReproceso;
	}
	
	/**
	 * @return Returns the horasReproceso.
	 */
	public Object getHorasReproceso(Object key) {
		return horasReproceso.get(key);
	}

	/**
	 * @param horasReproceso The horasReproceso to set.
	 */
	public void setHorasReproceso(Object key, Object value) {
		this.horasReproceso.put(key, value);
	}

	/**
	 * 
	 * @return Returns the datosCuentaRequeridoReservaCitas.
	 */
	public String getDatosCuentaRequeridoReservaCitas() {
		return datosCuentaRequeridoReservaCitas;
	}

	/**
	 * 
	 * @param datosCuentaRequeridoReservaCitas to set
	 */
	public void setDatosCuentaRequeridoReservaCitas(
			String datosCuentaRequeridoReservaCitas) {
		this.datosCuentaRequeridoReservaCitas = datosCuentaRequeridoReservaCitas;
	}

	public String getRedNoAdscrita() {
		return redNoAdscrita;
	}

	public void setRedNoAdscrita(String redNoAdscrita) {
		this.redNoAdscrita = redNoAdscrita;
	}

	public String getCodigoTransCompras()
	{
		return codigoTransCompras;
	}

	public void setCodigoTransCompras(String codigoTransCompras)
	{
		this.codigoTransCompras = codigoTransCompras;
	}

	public String getCodigoTransDevolCompras()
	{
		return codigoTransDevolCompras;
	}

	public void setCodigoTransDevolCompras(String codigoTransDevolCompras)
	{
		this.codigoTransDevolCompras = codigoTransDevolCompras;
	}

	public String getNumDiasEgresoOrdenesAmbulatorias() {
		return numDiasEgresoOrdenesAmbulatorias;
	}

	public void setNumDiasEgresoOrdenesAmbulatorias(
			String numDiasEgresoOrdenesAmbulatorias) {
		this.numDiasEgresoOrdenesAmbulatorias = numDiasEgresoOrdenesAmbulatorias;
	}

	public String getNumDiasGenerarOrdenesArticulos() {
		return numDiasGenerarOrdenesArticulos;
	}

	public void setNumDiasGenerarOrdenesArticulos(
			String numDiasGenerarOrdenesArticulos) {
		this.numDiasGenerarOrdenesArticulos = numDiasGenerarOrdenesArticulos;
	}

	public String getNumDiasTratamientoMedicamentos() {
		return numDiasTratamientoMedicamentos;
	}

	public void setNumDiasTratamientoMedicamentos(
			String numDiasTratamientoMedicamentos) {
		this.numDiasTratamientoMedicamentos = numDiasTratamientoMedicamentos;
	}

	/**
	 * @return the convenioFisalud
	 */
	public String getConvenioFisalud() {
		return convenioFisalud;
	}

	/**
	 * @return the convenioFisalud
	 */
	public String getFormaPagoEfectivo() {
		return formaPagoEfectivo;
	}
	
	/**
	 * @param convenioFisalud the convenioFisalud to set
	 */
	public void setConvenioFisalud(String convenioFisalud) {
		this.convenioFisalud = convenioFisalud;
	}

	/**
	 * @param 
	 */
	public void setFormaPagoEfectivo(String formaPagoEfectivo) {
		this.formaPagoEfectivo = formaPagoEfectivo;
	}
	
	public String getConfirmarAjustesPooles()
	{
		return confirmarAjustesPooles;
	}

	public void setConfirmarAjustesPooles(String confirmarAjustesPooles)
	{
		this.confirmarAjustesPooles = confirmarAjustesPooles;
	}

	/**
	 * @return the horasCaducidadReferenciasExternas
	 */
	public String getHorasCaducidadReferenciasExternas() {
		return horasCaducidadReferenciasExternas;
	}

	/**
	 * @param horasCaducidadReferenciasExternas the horasCaducidadReferenciasExternas to set
	 */
	public void setHorasCaducidadReferenciasExternas(
			String horasCaducidadReferenciasExternas) {
		this.horasCaducidadReferenciasExternas = horasCaducidadReferenciasExternas;
	}

	/**
	 * @return the llamadoAutomaticoReferencia
	 */
	public String getLlamadoAutomaticoReferencia() {
		return llamadoAutomaticoReferencia;
	}

	/**
	 * @param llamadoAutomaticoReferencia the llamadoAutomaticoReferencia to set
	 */
	public void setLlamadoAutomaticoReferencia(String llamadoAutomaticoReferencia) {
		this.llamadoAutomaticoReferencia = llamadoAutomaticoReferencia;
	}

	public String getInterfazAbonosTesoreria() {
		return interfazAbonosTesoreria;
	}

	public void setInterfazAbonosTesoreria(String interfazAbonosTesoreria) {
		this.interfazAbonosTesoreria = interfazAbonosTesoreria;
	}

	public String getInterfazPacientes() {
		return interfazPacientes;
	}

	public void setInterfazPacientes(String interfazPacientes) {
		this.interfazPacientes = interfazPacientes;
	}

	/**
	 * @return the validarEdadResponsablePaciente
	 */
	public String getValidarEdadResponsablePaciente() {
		return validarEdadResponsablePaciente;
	}

	/**
	 * @param validarEdadResponsablePaciente the validarEdadResponsablePaciente to set
	 */
	public void setValidarEdadResponsablePaciente(
			String validarEdadResponsablePaciente) {
		this.validarEdadResponsablePaciente = validarEdadResponsablePaciente;
	}

	/**
	 * @return the validarEdadDeudorPaciente
	 */
	public String getValidarEdadDeudorPaciente() {
		return validarEdadDeudorPaciente;
	}

	/**
	 * @param validarEdadDeudorPaciente the validarEdadDeudorPaciente to set
	 */
	public void setValidarEdadDeudorPaciente(
			String validarEdadDeudorPaciente) {
		this.validarEdadDeudorPaciente = validarEdadDeudorPaciente;
	}

	/**
	 * @return the aï¿½osBaseEdadAdulta
	 */
	public String getAniosBaseEdadAdulta() {
		return aniosBaseEdadAdulta;
	}

	/**
	 * @param aï¿½osBaseEdadAdulta the aï¿½osBaseEdadAdulta to set
	 */
	public void setAniosBaseEdadAdulta(
			String aniosBaseEdadAdulta) {
		this.aniosBaseEdadAdulta = aniosBaseEdadAdulta;
	}

	/**
	 * @return the validarEgresoAdministrativoPaquetizar
	 */
	public String getValidarEgresoAdministrativoPaquetizar() {
		return validarEgresoAdministrativoPaquetizar;
	}

	/**
	 * @param validarEgresoAdministrativoPaquetizar the validarEgresoAdministrativoPaquetizar to set
	 */
	public void setValidarEgresoAdministrativoPaquetizar(
			String validarEgresoAdministrativoPaquetizar) {
		this.validarEgresoAdministrativoPaquetizar = validarEgresoAdministrativoPaquetizar;
	}

	/**
	 * @return the maxCantidPaquetesValidosIngresoPaciente
	 */
	public String getMaxCantidPaquetesValidosIngresoPaciente() {
		return maxCantidPaquetesValidosIngresoPaciente;
	}

	/**
	 * @param maxCantidPaquetesValidosIngresoPaciente the maxCantidPaquetesValidosIngresoPaciente to set
	 */
	public void setMaxCantidPaquetesValidosIngresoPaciente(
			String maxCantidPaquetesValidosIngresoPaciente) {
		this.maxCantidPaquetesValidosIngresoPaciente = maxCantidPaquetesValidosIngresoPaciente;
	}

	/**
	 * @return the asignarValorPacienteValorCargos
	 */
	public String getAsignarValorPacienteValorCargos() {
		return asignarValorPacienteValorCargos;
	}

	/**
	 * @param asignarValorPacienteValorCargos the asignarValorPacienteValorCargos to set
	 */
	public void setAsignarValorPacienteValorCargos(
			String asignarValorPacienteValorCargos) {
		this.asignarValorPacienteValorCargos = asignarValorPacienteValorCargos;
	}

	public String getPaisNacimiento() {
		return paisNacimiento;
	}

	public void setPaisNacimiento(String paisNacimiento) {
		this.paisNacimiento = paisNacimiento;
	}

	public String getPaisResidencia() {
		return paisResidencia;
	}

	public void setPaisResidencia(String paisResidencia) {
		this.paisResidencia = paisResidencia;
	}

	public String getArticuloInventario() {
		return articuloInventario;
	}

	public void setArticuloInventario(String articuloInventario) {
		this.articuloInventario = articuloInventario;
	}

	public String getInterfazCompras() {
		return interfazCompras;
	}

	public void setInterfazCompras(String interfazCompras) {
		this.interfazCompras = interfazCompras;
	}

	public String getLoginUsuario() {
		return loginUsuario;
	}

	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}

	public String getDiasAlertaVigencia() {
		return diasAlertaVigencia;
	}

	public void setDiasAlertaVigencia(String diasAlertaVigencia) {
		this.diasAlertaVigencia = diasAlertaVigencia;
	}

	/**
	 * @return the interfazCarteraPacientes
	 */
	public String getInterfazCarteraPacientes() {
		return interfazCarteraPacientes;
	}

	/**
	 * @param interfazCarteraPacientes the interfazCarteraPacientes to set
	 */
	public void setInterfazCarteraPacientes(String interfazCarteraPacientes) {
		this.interfazCarteraPacientes = interfazCarteraPacientes;
	}

	/**
	 * @return the interfazContableFacturas
	 */
	public String getInterfazContableFacturas() {
		return interfazContableFacturas;
	}

	/**
	 * @param interfazContableFacturas the interfazContableFacturas to set
	 */
	public void setInterfazContableFacturas(String interfazContableFacturas) {
		this.interfazContableFacturas = interfazContableFacturas;
	}

	/**
	 * @return the interfazTerceros
	 */
	public String getInterfazTerceros() {
		return interfazTerceros;
	}

	/**
	 * @param interfazTerceros the interfazTerceros to set
	 */
	public void setInterfazTerceros(String interfazTerceros) {
		this.interfazTerceros = interfazTerceros;
	}

	/**
	 * @return the consolidarCargos
	 */
	public String getConsolidarCargos() {
		return consolidarCargos;
	}

	/**
	 * @param consolidarCargos the consolidarCargos to set
	 */
	public void setConsolidarCargos(String consolidarCargos) {
		this.consolidarCargos = consolidarCargos;
	}

	/**
	 * @return the manejaConversionMonedaExtranjera
	 */
	public String getManejaConversionMonedaExtranjera() {
		return manejaConversionMonedaExtranjera;
	}

	/**
	 * @param manejaConversionMonedaExtranjera the manejaConversionMonedaExtranjera to set
	 */
	public void setManejaConversionMonedaExtranjera(
			String manejaConversionMonedaExtranjera) {
		this.manejaConversionMonedaExtranjera = manejaConversionMonedaExtranjera;
	}

	/**
	 * @return the impresionMediaCarta
	 */
	public String getImpresionMediaCarta() {
		return impresionMediaCarta;
	}

	/**
	 * @param impresionMediaCarta the impresionMediaCarta to set
	 */
	public void setImpresionMediaCarta(String impresionMediaCarta) {
		this.impresionMediaCarta = impresionMediaCarta;
	}

	public String getHoraCorteHistoricoCamas() {
		return horaCorteHistoricoCamas;
	}

	public void setHoraCorteHistoricoCamas(String horaCorteHistoricoCamas) {
		this.horaCorteHistoricoCamas = horaCorteHistoricoCamas;
	}

	public String getMinutosLimiteAlertaPacienteConSalidaHospitalizacion() {
		return minutosLimiteAlertaPacienteConSalidaHospitalizacion;
	}

	public void setMinutosLimiteAlertaPacienteConSalidaHospitalizacion(
			String minutosLimiteAlertaPacienteConSalidaHospitalizacion) {
		this.minutosLimiteAlertaPacienteConSalidaHospitalizacion = minutosLimiteAlertaPacienteConSalidaHospitalizacion;
	}

	public String getMinutosLimiteAlertaPacienteConSalidaUrgencias() {
		return minutosLimiteAlertaPacienteConSalidaUrgencias;
	}

	public void setMinutosLimiteAlertaPacienteConSalidaUrgencias(
			String minutosLimiteAlertaPacienteConSalidaUrgencias) {
		this.minutosLimiteAlertaPacienteConSalidaUrgencias = minutosLimiteAlertaPacienteConSalidaUrgencias;
	}

	public String getMinutosLimiteAlertaPacientePorRemitirHospitalizacion() {
		return minutosLimiteAlertaPacientePorRemitirHospitalizacion;
	}

	public void setMinutosLimiteAlertaPacientePorRemitirHospitalizacion(
			String minutosLimiteAlertaPacientePorRemitirHospitalizacion) {
		this.minutosLimiteAlertaPacientePorRemitirHospitalizacion = minutosLimiteAlertaPacientePorRemitirHospitalizacion;
	}

	public String getMinutosLimiteAlertaPacientePorRemitirUrgencias() {
		return minutosLimiteAlertaPacientePorRemitirUrgencias;
	}

	public void setMinutosLimiteAlertaPacientePorRemitirUrgencias(
			String minutosLimiteAlertaPacientePorRemitirUrgencias) {
		this.minutosLimiteAlertaPacientePorRemitirUrgencias = minutosLimiteAlertaPacientePorRemitirUrgencias;
	}

	public String getMinutosLimiteAlertaReserva() {
		return minutosLimiteAlertaReserva;
	}

	public void setMinutosLimiteAlertaReserva(String minutosLimiteAlertaReserva) {
		this.minutosLimiteAlertaReserva = minutosLimiteAlertaReserva;
	}

	/**
	 * @return the identificadorInstitucionArchivosColsanitas
	 */
	public String getIdentificadorInstitucionArchivosColsanitas() {
		return identificadorInstitucionArchivosColsanitas;
	}

	/**
	 * @param identificadorInstitucionArchivosColsanitas the identificadorInstitucionArchivosColsanitas to set
	 */
	public void setIdentificadorInstitucionArchivosColsanitas(
			String identificadorInstitucionArchivosColsanitas) {
		this.identificadorInstitucionArchivosColsanitas = identificadorInstitucionArchivosColsanitas;
	}

	/**
	 * @return the interfazRips
	 */
	public String getInterfazRips() {
		return interfazRips;
	}

	/**
	 * @param interfazRips the interfazRips to set
	 */
	public void setInterfazRips(String interfazRips) {
		this.interfazRips = interfazRips;
	}

	/**
	 * @return the entidadManejaHospitalDia
	 */
	public String getEntidadManejaHospitalDia() {
		return entidadManejaHospitalDia;
	}

	/**
	 * @param entidadManejaHospitalDia the entidadManejaHospitalDia to set
	 */
	public void setEntidadManejaHospitalDia(String entidadManejaHospitalDia) {
		this.entidadManejaHospitalDia = entidadManejaHospitalDia;
	}

	/**
	 * @return the entidadManejaRips
	 */
	public String getEntidadManejaRips() {
		return entidadManejaRips;
	}

	/**
	 * @param entidadManejaRips the entidadManejaRips to set
	 */
	public void setEntidadManejaRips(String entidadManejaRips) {
		this.entidadManejaRips = entidadManejaRips;
	}

	/**
	 * @return the valoracionUrgenciasEnHospitalizacion
	 */
	public String getValoracionUrgenciasEnHospitalizacion() {
		return valoracionUrgenciasEnHospitalizacion;
	}

	/**
	 * @param valoracionUrgenciasEnHospitalizacion the valoracionUrgenciasEnHospitalizacion to set
	 */
	public void setValoracionUrgenciasEnHospitalizacion(
			String valoracionUrgenciasEnHospitalizacion) {
		this.valoracionUrgenciasEnHospitalizacion = valoracionUrgenciasEnHospitalizacion;
	}

	/**
	 * @return the codigoManualEstandarBusquedaServicios
	 */
	public String getCodigoManualEstandarBusquedaServicios() {
		return codigoManualEstandarBusquedaServicios;
	}

	/**
	 * @param codigoManualEstandarBusquedaServicios the codigoManualEstandarBusquedaServicios to set
	 */
	public void setCodigoManualEstandarBusquedaServicios(
			String codigoManualEstandarBusquedaServicios) {
		this.codigoManualEstandarBusquedaServicios = codigoManualEstandarBusquedaServicios;
	}

	/**
	 * @return the liquidacionAutomaticaCirugias
	 */
	public String getLiquidacionAutomaticaCirugias() {
		return liquidacionAutomaticaCirugias;
	}

	/**
	 * @param liquidacionAutomaticaCirugias the liquidacionAutomaticaCirugias to set
	 */
	public void setLiquidacionAutomaticaCirugias(
			String liquidacionAutomaticaCirugias) {
		this.liquidacionAutomaticaCirugias = liquidacionAutomaticaCirugias;
	}

	/**
	 * @return the liquidacionAutomaticaNoQx
	 */
	public String getLiquidacionAutomaticaNoQx() {
		return liquidacionAutomaticaNoQx;
	}

	/**
	 * @param liquidacionAutomaticaNoQx the liquidacionAutomaticaNoQx to set
	 */
	public void setLiquidacionAutomaticaNoQx(String liquidacionAutomaticaNoQx) {
		this.liquidacionAutomaticaNoQx = liquidacionAutomaticaNoQx;
	}

	/**
	 * @return the asocioAnestesia
	 */
	public String getAsocioAnestesia() {
		return asocioAnestesia;
	}

	/**
	 * @param asocioAnestesia the asocioAnestesia to set
	 */
	public void setAsocioAnestesia(String asocioAnestesia) {
		this.asocioAnestesia = asocioAnestesia;
	}

	/**
	 * @return the asocioAyudantia
	 */
	public String getAsocioAyudantia() {
		return asocioAyudantia;
	}

	/**
	 * @param asocioAyudantia the asocioAyudantia to set
	 */
	public void setAsocioAyudantia(String asocioAyudantia) {
		this.asocioAyudantia = asocioAyudantia;
	}

	/**
	 * @return the asocioCirujano
	 */
	public String getAsocioCirujano() {
		return asocioCirujano;
	}

	/**
	 * @param asocioCirujano the asocioCirujano to set
	 */
	public void setAsocioCirujano(String asocioCirujano) {
		this.asocioCirujano = asocioCirujano;
	}

	/**
	 * @return the indicativoCobrableHonorariosCirugia
	 */
	public String getIndicativoCobrableHonorariosCirugia() {
		return indicativoCobrableHonorariosCirugia;
	}

	/**
	 * @param indicativoCobrableHonorariosCirugia the indicativoCobrableHonorariosCirugia to set
	 */
	public void setIndicativoCobrableHonorariosCirugia(
			String indicativoCobrableHonorariosCirugia) {
		this.indicativoCobrableHonorariosCirugia = indicativoCobrableHonorariosCirugia;
	}

	/**
	 * @return the indicativoCobrableHonorariosNoCruento
	 */
	public String getIndicativoCobrableHonorariosNoCruento() {
		return indicativoCobrableHonorariosNoCruento;
	}

	/**
	 * @param indicativoCobrableHonorariosNoCruento the indicativoCobrableHonorariosNoCruento to set
	 */
	public void setIndicativoCobrableHonorariosNoCruento(
			String indicativoCobrableHonorariosNoCruento) {
		this.indicativoCobrableHonorariosNoCruento = indicativoCobrableHonorariosNoCruento;
	}

	/**
	 * @return the manejoProgramacionSalasSolicitudesDyt
	 */
	public String getManejoProgramacionSalasSolicitudesDyt() {
		return manejoProgramacionSalasSolicitudesDyt;
	}

	/**
	 * @param manejoProgramacionSalasSolicitudesDyt the manejoProgramacionSalasSolicitudesDyt to set
	 */
	public void setManejoProgramacionSalasSolicitudesDyt(
			String manejoProgramacionSalasSolicitudesDyt) {
		this.manejoProgramacionSalasSolicitudesDyt = manejoProgramacionSalasSolicitudesDyt;
	}

	/**
	 * @return the modificarInformacionDescripcionQuirurgica
	 */
	public String getModificarInformacionDescripcionQuirurgica() {
		return modificarInformacionDescripcionQuirurgica;
	}

	/**
	 * @param modificarInformacionDescripcionQuirurgica the modificarInformacionDescripcionQuirurgica to set
	 */
	public void setModificarInformacionDescripcionQuirurgica(
			String modificarInformacionDescripcionQuirurgica) {
		this.modificarInformacionDescripcionQuirurgica = modificarInformacionDescripcionQuirurgica;
	}

	/**
	 * @return the requeridaDescripcionEspecialidadCirugias
	 */
	public String getRequeridaDescripcionEspecialidadCirugias() {
		return requeridaDescripcionEspecialidadCirugias;
	}

	/**
	 * @param requeridaDescripcionEspecialidadCirugias the requeridaDescripcionEspecialidadCirugias to set
	 */
	public void setRequeridaDescripcionEspecialidadCirugias(
			String requeridaDescripcionEspecialidadCirugias) {
		this.requeridaDescripcionEspecialidadCirugias = requeridaDescripcionEspecialidadCirugias;
	}

	/**
	 * @return the requeridaDescripcionEspecialidadNoCruentos
	 */
	public String getRequeridaDescripcionEspecialidadNoCruentos() {
		return requeridaDescripcionEspecialidadNoCruentos;
	}

	/**
	 * @param requeridaDescripcionEspecialidadNoCruentos the requeridaDescripcionEspecialidadNoCruentos to set
	 */
	public void setRequeridaDescripcionEspecialidadNoCruentos(
			String requeridaDescripcionEspecialidadNoCruentos) {
		this.requeridaDescripcionEspecialidadNoCruentos = requeridaDescripcionEspecialidadNoCruentos;
	}

	/**
	 * @return the minutosMaximosRegistroAnestesia
	 */
	public String getMinutosMaximosRegistroAnestesia() {
		return minutosMaximosRegistroAnestesia;
	}

	/**
	 * @param minutosMaximosRegistroAnestesia the minutosMaximosRegistroAnestesia to set
	 */
	public void setMinutosMaximosRegistroAnestesia(
			String minutosMaximosRegistroAnestesia) {
		this.minutosMaximosRegistroAnestesia = minutosMaximosRegistroAnestesia;
	}

	/**
	 * @return the minutosRegistroNotasCirugia
	 */
	public String getMinutosRegistroNotasCirugia() {
		return minutosRegistroNotasCirugia;
	}

	/**
	 * @param minutosRegistroNotasCirugia the minutosRegistroNotasCirugia to set
	 */
	public void setMinutosRegistroNotasCirugia(String minutosRegistroNotasCirugia) {
		this.minutosRegistroNotasCirugia = minutosRegistroNotasCirugia;
	}

	/**
	 * @return the minutosRegistroNotasNoCruentos
	 */
	public String getMinutosRegistroNotasNoCruentos() {
		return minutosRegistroNotasNoCruentos;
	}

	/**
	 * @param minutosRegistroNotasNoCruentos the minutosRegistroNotasNoCruentos to set
	 */
	public void setMinutosRegistroNotasNoCruentos(
			String minutosRegistroNotasNoCruentos) {
		this.minutosRegistroNotasNoCruentos = minutosRegistroNotasNoCruentos;
	}

	/**
	 * @return the modificarInformacionQuirurgica
	 */
	public String getModificarInformacionQuirurgica() {
		return modificarInformacionQuirurgica;
	}

	/**
	 * @param modificarInformacionQuirurgica the modificarInformacionQuirurgica to set
	 */
	public void setModificarInformacionQuirurgica(
			String modificarInformacionQuirurgica) {
		this.modificarInformacionQuirurgica = modificarInformacionQuirurgica;
	}

	/**
	 * @return the ubicacionPlanosEntidadesSubcontratadas
	 */
	public String getUbicacionPlanosEntidadesSubcontratadas() {
		return ubicacionPlanosEntidadesSubcontratadas;
	}

	/**
	 * @param ubicacionPlanosEntidadesSubcontratadas the ubicacionPlanosEntidadesSubcontratadas to set
	 */
	public void setUbicacionPlanosEntidadesSubcontratadas(
			String ubicacionPlanosEntidadesSubcontratadas) {
		this.ubicacionPlanosEntidadesSubcontratadas = ubicacionPlanosEntidadesSubcontratadas;
	}

	/**
	 * 
	 * @return
	 */
	public String getInstitucionMultiempresa() {
		return institucionMultiempresa;
	}

	/**
	 * 
	 * @param institucionMultiempresa
	 */
	public void setInstitucionMultiempresa(String institucionMultiempresa) {
		this.institucionMultiempresa = institucionMultiempresa;
	}

	/**
	 * 
	 * @return
	 */
	public String getArchivosPlanosReportes() {
		return archivosPlanosReportes;
	}

	/**
	 * 
	 * @param archivosPlanosReportes
	 */
	public void setArchivosPlanosReportes(String archivosPlanosReportes) {
		this.archivosPlanosReportes = archivosPlanosReportes;
	}

	/**
	 * 
	 * @return
	 */
	public String getArchivosPlanosReportesInventarios() {
		return archivosPlanosReportesInventarios;
	}

	/**
	 * 
	 * @param archivosPlanosReportesInventarios
	 */
	public void setArchivosPlanosReportesInventarios(
			String archivosPlanosReportesInventarios) {
		this.archivosPlanosReportesInventarios = archivosPlanosReportesInventarios;
	}

	/**
	 * @return the conteosValidosAjustarInventarioFisico
	 */
	public String getConteosValidosAjustarInventarioFisico() {
		return conteosValidosAjustarInventarioFisico;
	}

	/**
	 * @param conteosValidosAjustarInventarioFisico the conteosValidosAjustarInventarioFisico to set
	 */
	public void setConteosValidosAjustarInventarioFisico(
			String conteosValidosAjustarInventarioFisico) {
		this.conteosValidosAjustarInventarioFisico = conteosValidosAjustarInventarioFisico;
	}

	public String getEntidadControlaDespachoSaldosMultidosis() {
		return entidadControlaDespachoSaldosMultidosis;
	}

	public void setEntidadControlaDespachoSaldosMultidosis(
			String entidadControlaDespachoSaldosMultidosis) {
		this.entidadControlaDespachoSaldosMultidosis = entidadControlaDespachoSaldosMultidosis;
	}

	public String getNumeroDiasControlMedicamentosOrdenados() {
		return numeroDiasControlMedicamentosOrdenados;
	}

	public void setNumeroDiasControlMedicamentosOrdenados(
			String numeroDiasControlMedicamentosOrdenados) {
		this.numeroDiasControlMedicamentosOrdenados = numeroDiasControlMedicamentosOrdenados;
	}

	/**
	 * @return the generaEstanciaAutomatica
	 */
	public String getGeneraEstanciaAutomatica() {
		return generaEstanciaAutomatica;
	}

	/**
	 * @param generaEstanciaAutomatica the generaEstanciaAutomatica to set
	 */
	public void setGeneraEstanciaAutomatica(String generaEstanciaAutomatica) {
		this.generaEstanciaAutomatica = generaEstanciaAutomatica;
	}

	/**
	 * @return the horaGeneraEstanciaAutomatica
	 */
	public String getHoraGeneraEstanciaAutomatica() {
		return horaGeneraEstanciaAutomatica;
	}

	/**
	 * @param horaGeneraEstanciaAutomatica the horaGeneraEstanciaAutomatica to set
	 */
	public void setHoraGeneraEstanciaAutomatica(String horaGeneraEstanciaAutomatica) {
		this.horaGeneraEstanciaAutomatica = horaGeneraEstanciaAutomatica;
	}

	/**
	 * @return the incluirTipoPacientesCirugiaAmbulatoria
	 */
	public String getIncluirTipoPacientesCirugiaAmbulatoria() {
		return incluirTipoPacientesCirugiaAmbulatoria;
	}

	/**
	 * @param incluirTipoPacientesCirugiaAmbulatoria the incluirTipoPacientesCirugiaAmbulatoria to set
	 */
	public void setIncluirTipoPacientesCirugiaAmbulatoria(
			String incluirTipoPacientesCirugiaAmbulatoria) {
		this.incluirTipoPacientesCirugiaAmbulatoria = incluirTipoPacientesCirugiaAmbulatoria;
	}

	public String getTipoConsecutivoManejar() {
		return tipoConsecutivoManejar;
	}

	public void setTipoConsecutivoManejar(String tipoConsecutivoManejar) {
		this.tipoConsecutivoManejar = tipoConsecutivoManejar;
	}

	/**
	 * @return the requeridoInfoRipsCargoDirecto
	 */
	public String getRequeridoInfoRipsCargoDirecto() {
		return requeridoInfoRipsCargoDirecto;
	}

	/**
	 * @param requeridoInfoRipsCargoDirecto the requeridoInfoRipsCargoDirecto to set
	 */
	public void setRequeridoInfoRipsCargoDirecto(
			String requeridoInfoRipsCargoDirecto) {
		this.requeridoInfoRipsCargoDirecto = requeridoInfoRipsCargoDirecto;
	}

	/**
	 * @return the genForecatRips
	 */
	public String getGenForecatRips() {
		return genForecatRips;
	}

	/**
	 * @param genForecatRips the genForecatRips to set
	 */
	public void setGenForecatRips(String genForecatRips) {
		this.genForecatRips = genForecatRips;
	}

	public String getValidacionOcupacionJustificacionNoPosArticulos() {
		return validacionOcupacionJustificacionNoPosArticulos;
	}

	public void setValidacionOcupacionJustificacionNoPosArticulos(
			String validacionOcupacionJustificacionNoPosArticulos) {
		this.validacionOcupacionJustificacionNoPosArticulos = validacionOcupacionJustificacionNoPosArticulos;
	}
	
	public String getValidacionOcupacionJustificacionNoPosServicios() {
		return validacionOcupacionJustificacionNoPosServicios;
	}

	public void setValidacionOcupacionJustificacionNoPosServicios(
			String validacionOcupacionJustificacionNoPosServicios) {
		this.validacionOcupacionJustificacionNoPosServicios = validacionOcupacionJustificacionNoPosServicios;
	}

	/**
	 * @return the asignaValoracionCxAmbulaHospita
	 */
	public String getAsignaValoracionCxAmbulaHospita() {
		return asignaValoracionCxAmbulaHospita;
	}

	/**
	 * @param asignaValoracionCxAmbulaHospita the asignaValoracionCxAmbulaHospita to set
	 */
	public void setAsignaValoracionCxAmbulaHospita(
			String asignaValoracionCxAmbulaHospita) {
		this.asignaValoracionCxAmbulaHospita = asignaValoracionCxAmbulaHospita;
	}

	public String getInterfazContableRecibosCajaERP() {
		return interfazContableRecibosCajaERP;
	}

	public void setInterfazContableRecibosCajaERP(
			String interfazContableRecibosCajaERP) {
		this.interfazContableRecibosCajaERP = interfazContableRecibosCajaERP;
	}

	public String getValidadAdministracionMedEgresoMedico() {
		return validadAdministracionMedEgresoMedico;
	}

	public void setValidadAdministracionMedEgresoMedico(
			String validadAdministracionMedEgresoMedico) {
		this.validadAdministracionMedEgresoMedico = validadAdministracionMedEgresoMedico;
	}

	public String getInterfazConsecutivoFacturasOtroSistema() {
		return interfazConsecutivoFacturasOtroSistema;
	}

	public void setInterfazConsecutivoFacturasOtroSistema(
			String interfazConsecutivoFacturasOtroSistema) {
		this.interfazConsecutivoFacturasOtroSistema = interfazConsecutivoFacturasOtroSistema;
	}

	public String getInterfazNutricion() {
		return interfazNutricion;
	}

	public void setInterfazNutricion(String interfazNutricion) {
		this.interfazNutricion = interfazNutricion;
	}

	/**
	 * @return the crearCuentaAtencionCitas
	 */
	public String getCrearCuentaAtencionCitas() {
		return crearCuentaAtencionCitas;
	}

	/**
	 * @param crearCuentaAtencionCitas the crearCuentaAtencionCitas to set
	 */
	public void setCrearCuentaAtencionCitas(String crearCuentaAtencionCitas) {
		this.crearCuentaAtencionCitas = crearCuentaAtencionCitas;
	}

	/**
	 * @return the institucionManejaMultasPorIncumplimiento
	 */
	public String getInstitucionManejaMultasPorIncumplimiento() {
		return institucionManejaMultasPorIncumplimiento;
	}

	/**
	 * @param institucionManejaMultasPorIncumplimiento the institucionManejaMultasPorIncumplimiento to set
	 */
	public void setInstitucionManejaMultasPorIncumplimiento(
			String institucionManejaMultasPorIncumplimiento) {
		this.institucionManejaMultasPorIncumplimiento = institucionManejaMultasPorIncumplimiento;
	}
	
	/**
	 * @param bloqueaCitasReservaAsignReprogPorIncump the bloqueaCitasReservaAsignReprogPorIncump to set
	 */
	public String getBloqueaCitasReservaAsignReprogPorIncump() {
		return this.bloqueaCitasReservaAsignReprogPorIncump;
	}

	/**
	 * @param bloqueaCitasReservaAsignReprogPorIncump the bloqueaCitasReservaAsignReprogPorIncump to set
	 */
	public void setBloqueaCitasReservaAsignReprogPorIncump(
			String bloqueaCitasReservaAsignReprogPorIncump) {
		this.bloqueaCitasReservaAsignReprogPorIncump = bloqueaCitasReservaAsignReprogPorIncump;
	}

	/**
	 * @param bloqueaAtencionCitasPorIncump the bloqueaAtencionCitasPorIncump to set
	 */
	public String getBloqueaAtencionCitasPorIncump() {
		return this.bloqueaAtencionCitasPorIncump;
	}

	/**
	 * @param bloqueaAtencionCitasPorIncump the bloqueaAtencionCitasPorIncump to set
	 */
	public void setBloqueaAtencionCitasPorIncump(
			String bloqueaAtencionCitasPorIncump) {
		this.bloqueaAtencionCitasPorIncump = bloqueaAtencionCitasPorIncump;
	}

	/**
	 * @param fechaInicioControlMultasIncumplimientoCitas the fechaInicioControlMultasIncumplimientoCitas to set
	 */
	public String getFechaInicioControlMultasIncumplimientoCitas() {
		return this.fechaInicioControlMultasIncumplimientoCitas;
	}

	/**
	 * @param fechaInicioControlMultasIncumplimientoCitas the fechaInicioControlMultasIncumplimientoCitas to set
	 */
	public void setFechaInicioControlMultasIncumplimientoCitas(
			String fechaInicioControlMultasIncumplimientoCitas) {
		this.fechaInicioControlMultasIncumplimientoCitas = fechaInicioControlMultasIncumplimientoCitas;
	}

	/**
	 * @param valorMultaPorIncumplimientoCitas the valorMultaPorIncumplimientoCitas to set
	 */
	public String getValorMultaPorIncumplimientoCitas() {
		return this.valorMultaPorIncumplimientoCitas;
	}

	/**
	 * @param valorMultaPorIncumplimientoCitas the valorMultaPorIncumplimientoCitas to set
	 */
	public void setValorMultaPorIncumplimientoCitas(
			String valorMultaPorIncumplimientoCitas) {
		this.valorMultaPorIncumplimientoCitas = valorMultaPorIncumplimientoCitas;
	}

	/**
	 * @return the permitirFacturarReingresosIndependientes
	 */
	public String getPermitirFacturarReingresosIndependientes() {
		return permitirFacturarReingresosIndependientes;
	}

	/**
	 * @param permitirFacturarReingresosIndependientes the permitirFacturarReingresosIndependientes to set
	 */
	public void setPermitirFacturarReingresosIndependientes(
			String permitirFacturarReingresosIndependientes) {
		this.permitirFacturarReingresosIndependientes = permitirFacturarReingresosIndependientes;
	}

	/**
	 * @return the tiempoMaximoReingresoHospitalizacion
	 */
	public String getTiempoMaximoReingresoHospitalizacion() {
		return tiempoMaximoReingresoHospitalizacion;
	}

	/**
	 * @param tiempoMaximoReingresoHospitalizacion the tiempoMaximoReingresoHospitalizacion to set
	 */
	public void setTiempoMaximoReingresoHospitalizacion(
			String tiempoMaximoReingresoHospitalizacion) {
		this.tiempoMaximoReingresoHospitalizacion = tiempoMaximoReingresoHospitalizacion;
	}

	/**
	 * @return the tiempoMaximoReingresoUrgencias
	 */
	public String getTiempoMaximoReingresoUrgencias() {
		return tiempoMaximoReingresoUrgencias;
	}

	/**
	 * @param tiempoMaximoReingresoUrgencias the tiempoMaximoReingresoUrgencias to set
	 */
	public void setTiempoMaximoReingresoUrgencias(
			String tiempoMaximoReingresoUrgencias) {
		this.tiempoMaximoReingresoUrgencias = tiempoMaximoReingresoUrgencias;
	}

	public String getLiberarCamaHospitalizacionDespuesFacturar() {
		return liberarCamaHospitalizacionDespuesFacturar;
	}

	/**
	 * @param liberarCamaHospitalizacionDespuesFacturar
	 */
	public void setLiberarCamaHospitalizacionDespuesFacturar(
			String liberarCamaHospitalizacionDespuesFacturar) {
		this.liberarCamaHospitalizacionDespuesFacturar = liberarCamaHospitalizacionDespuesFacturar;
	}
	
	/**
	 * @return
	 */
	public String getControlaInterpretacionProcedimientosEvolucion() {
		return controlaInterpretacionProcedimientosEvolucion;
	}
	
	/**
	 * @param controlaInterpretacionProcedimientosEvolucion
	 */
	public void setControlaInterpretacionProcedimientosEvolucion(
			String controlaInterpretacionProcedimientosEvolucion) {
		this.controlaInterpretacionProcedimientosEvolucion = controlaInterpretacionProcedimientosEvolucion;
	}
	
	/**
	 * @return
	 */
	public String getValidarRegistroEvolucionGenerarOrdenes() {
		return validarRegistroEvolucionGenerarOrdenes;
	}
	
	/**
	 * @param validarRegistroEvolucionGenerarOrdenes
	 */
	public void setValidarRegistroEvolucionGenerarOrdenes(
			String validarRegistroEvolucionGenerarOrdenes) {
		this.validarRegistroEvolucionGenerarOrdenes = validarRegistroEvolucionGenerarOrdenes;
	}

	/**
	 * @return the maxPageItemsEpicrisis
	 */
	public String getMaxPageItemsEpicrisis() {
		return maxPageItemsEpicrisis;
	}

	/**
	 * @param maxPageItemsEpicrisis the maxPageItemsEpicrisis to set
	 */
	public void setMaxPageItemsEpicrisis(String maxPageItemsEpicrisis) {
		this.maxPageItemsEpicrisis = maxPageItemsEpicrisis;
	}

	public String getPathArchivosPlanosFacturacion() {
		return pathArchivosPlanosFacturacion;
	}

	public void setPathArchivosPlanosFacturacion(
			String pathArchivosPlanosFacturacion) {
		this.pathArchivosPlanosFacturacion = pathArchivosPlanosFacturacion;
	}

	public String getPathArchivosPlanosManejoPaciente() {
		return pathArchivosPlanosManejoPaciente;
	}

	public void setPathArchivosPlanosManejoPaciente(
			String pathArchivosPlanosManejoPaciente) {
		this.pathArchivosPlanosManejoPaciente = pathArchivosPlanosManejoPaciente;
	}

	/**
	 * @return the mostrarAntecedentesParametrizadosEpicrisis
	 */
	public String getMostrarAntecedentesParametrizadosEpicrisis() {
		return mostrarAntecedentesParametrizadosEpicrisis;
	}

	/**
	 * @param mostrarAntecedentesParametrizadosEpicrisis the mostrarAntecedentesParametrizadosEpicrisis to set
	 */
	public void setMostrarAntecedentesParametrizadosEpicrisis(
			String mostrarAntecedentesParametrizadosEpicrisis) {
		this.mostrarAntecedentesParametrizadosEpicrisis = mostrarAntecedentesParametrizadosEpicrisis;
	}

	/**
	 * @return the permitirConsultarEpicrisisSoloProf
	 */
	public String getPermitirConsultarEpicrisisSoloProf() {
		return permitirConsultarEpicrisisSoloProf;
	}

	/**
	 * @param permitirConsultarEpicrisisSoloProf the permitirConsultarEpicrisisSoloProf to set
	 */
	public void setPermitirConsultarEpicrisisSoloProf(
			String permitirConsultarEpicrisisSoloProf) {
		this.permitirConsultarEpicrisisSoloProf = permitirConsultarEpicrisisSoloProf;
	}

	/**
	 * @return the comprobacionDerechosCapitacionObligatoria
	 */
	public String getComprobacionDerechosCapitacionObligatoria() {
		return comprobacionDerechosCapitacionObligatoria;
	}

	/**
	 * @param comprobacionDerechosCapitacionObligatoria the comprobacionDerechosCapitacionObligatoria to set
	 */
	public void setComprobacionDerechosCapitacionObligatoria(
			String comprobacionDerechosCapitacionObligatoria) {
		this.comprobacionDerechosCapitacionObligatoria = comprobacionDerechosCapitacionObligatoria;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getRequiereAutorizarAnularFacturas() {
		return requiereAutorizarAnularFacturas;
	}

	/**
	 * 
	 * @param requiereAutorizarAnularFacturas
	 */
	public void setRequiereAutorizarAnularFacturas(
			String requiereAutorizarAnularFacturas) {
		this.requiereAutorizarAnularFacturas = requiereAutorizarAnularFacturas;
	}

	/**
	 * @return the conceptoParaAjusteEntrada
	 */
	public String getConceptoParaAjusteEntrada() {
		return conceptoParaAjusteEntrada;
	}

	/**
	 * @param conceptoParaAjusteEntrada the conceptoParaAjusteEntrada to set
	 */
	public void setConceptoParaAjusteEntrada(String conceptoParaAjusteEntrada) {
		this.conceptoParaAjusteEntrada = conceptoParaAjusteEntrada;
	}

	/**
	 * @return the conceptoParaAjusteSalida
	 */
	public String getConceptoParaAjusteSalida() {
		return conceptoParaAjusteSalida;
	}

	/**
	 * @param conceptoParaAjusteSalida the conceptoParaAjusteSalida to set
	 */
	public void setConceptoParaAjusteSalida(String conceptoParaAjusteSalida) {
		this.conceptoParaAjusteSalida = conceptoParaAjusteSalida;
	}

	/**
	 * @return the permitirModificarConceptosAjuste
	 */
	public String getPermitirModificarConceptosAjuste() {
		return permitirModificarConceptosAjuste;
	}

	/**
	 * @param permitirModificarConceptosAjuste the permitirModificarConceptosAjuste to set
	 */
	public void setPermitirModificarConceptosAjuste(
			String permitirModificarConceptosAjuste) {
		this.permitirModificarConceptosAjuste = permitirModificarConceptosAjuste;
	}

	/**
	 * @return the diasRestriccionCitasIncumplidas
	 */
	public String getDiasRestriccionCitasIncumplidas() {
		return diasRestriccionCitasIncumplidas;
	}

	/**
	 * @param diasRestriccionCitasIncumplidas the diasRestriccionCitasIncumplidas to set
	 */
	public void setDiasRestriccionCitasIncumplidas(
			String diasRestriccionCitasIncumplidas) {
		this.diasRestriccionCitasIncumplidas = diasRestriccionCitasIncumplidas;
	}

	/**
	 * @return the permitirModificarFechaSolicitudPedidos
	 */
	public String getPermitirModificarFechaSolicitudPedidos() {
		return permitirModificarFechaSolicitudPedidos;
	}

	/**
	 * @param permitirModificarFechaSolicitudPedidos the permitirModificarFechaSolicitudPedidos to set
	 */
	public void setPermitirModificarFechaSolicitudPedidos(
			String permitirModificarFechaSolicitudPedidos) {
		this.permitirModificarFechaSolicitudPedidos = permitirModificarFechaSolicitudPedidos;
	}

	/**
	 * @return the permitirModificarFechaSolicitudTraslado
	 */
	public String getPermitirModificarFechaSolicitudTraslado() {
		return permitirModificarFechaSolicitudTraslado;
	}

	/**
	 * @param permitirModificarFechaSolicitudTraslado the permitirModificarFechaSolicitudTraslado to set
	 */
	public void setPermitirModificarFechaSolicitudTraslado(
			String permitirModificarFechaSolicitudTraslado) {
		this.permitirModificarFechaSolicitudTraslado = permitirModificarFechaSolicitudTraslado;
	}
	
	/**
	 * @return the codigoManualEstandarBusquedaArticulos
	 */
	public String getCodigoManualEstandarBusquedaArticulos() {
		return codigoManualEstandarBusquedaArticulos;
	}

	/**
	 * @param codigoManualEstandarBusquedaArticulos the codigoManualEstandarBusquedaArticulos to set
	 */
	public void setCodigoManualEstandarBusquedaArticulos(
			String codigoManualEstandarBusquedaArticulos) {
		this.codigoManualEstandarBusquedaArticulos = codigoManualEstandarBusquedaArticulos;
	}

	/**
	 * @return the numeroDiasResponderGlosas
	 */
	public String getNumeroDiasResponderGlosas() {
		return numeroDiasResponderGlosas;
	}

	/**
	 * @param numeroDiasResponderGlosas the numeroDiasResponderGlosas to set
	 */
	public void setNumeroDiasResponderGlosas(String numeroDiasResponderGlosas) {
		this.numeroDiasResponderGlosas = numeroDiasResponderGlosas;
	}

	/**
	 * @return the permitirModificarTiempoTratamientoJustificacionNopos
	 */
	public String getPermitirModificarTiempoTratamientoJustificacionNopos() {
		return permitirModificarTiempoTratamientoJustificacionNopos;
	}

	/**
	 * @param permitirModificarTiempoTratamientoJustificacionNopos the permitirModificarTiempoTratamientoJustificacionNopos to set
	 */
	public void setPermitirModificarTiempoTratamientoJustificacionNopos(
			String permitirModificarTiempoTratamientoJustificacionNopos) {
		this.permitirModificarTiempoTratamientoJustificacionNopos = permitirModificarTiempoTratamientoJustificacionNopos;
	}

	/**
	 * @return the generarAjusteAutoRegRespuesta
	 */
	public String getGenerarAjusteAutoRegRespuesta() {
		return generarAjusteAutoRegRespuesta;
	}

	/**
	 * @param generarAjusteAutoRegRespuesta the generarAjusteAutoRegRespuesta to set
	 */
	public void setGenerarAjusteAutoRegRespuesta(
			String generarAjusteAutoRegRespuesta) {
		this.generarAjusteAutoRegRespuesta = generarAjusteAutoRegRespuesta;
	}

	/**
	 * @return the generarAjusteAutoRegRespuesConciliacion
	 */
	public String getGenerarAjusteAutoRegRespuesConciliacion() {
		return generarAjusteAutoRegRespuesConciliacion;
	}

	/**
	 * @param generarAjusteAutoRegRespuesConciliacion the generarAjusteAutoRegRespuesConciliacion to set
	 */
	public void setGenerarAjusteAutoRegRespuesConciliacion(
			String generarAjusteAutoRegRespuesConciliacion) {
		this.generarAjusteAutoRegRespuesConciliacion = generarAjusteAutoRegRespuesConciliacion;
	}	
	
	public String getValidarGlosaReiterada() {
		return validarGlosaReiterada;
	}

	public void setValidarGlosaReiterada(String validarGlosaReiterada) {
		this.validarGlosaReiterada = validarGlosaReiterada;
	}

	public String getFormatoImpresionConciliacion() {
		return formatoImpresionConciliacion;
	}

	public void setFormatoImpresionConciliacion(String formatoImpresionConciliacion) {
		this.formatoImpresionConciliacion = formatoImpresionConciliacion;
	}

	/**
	 * @return the formatoImpresionRespuesGlosa
	 */
	public String getFormatoImpresionRespuesGlosa() {
		return formatoImpresionRespuesGlosa;
	}

	/**
	 * @param formatoImpresionRespuesGlosa the formatoImpresionRespuesGlosa to set
	 */
	public void setFormatoImpresionRespuesGlosa(String formatoImpresionRespuesGlosa) {
		this.formatoImpresionRespuesGlosa = formatoImpresionRespuesGlosa;
	}

	/**
	 * @return the imprimirFirmasImpresionRespuesGlosa
	 */
	public String getImprimirFirmasImpresionRespuesGlosa() {
		return imprimirFirmasImpresionRespuesGlosa;
	}

	/**
	 * @param imprimirFirmasImpresionRespuesGlosa the imprimirFirmasImpresionRespuesGlosa to set
	 */
	public void setImprimirFirmasImpresionRespuesGlosa(
			String imprimirFirmasImpresionRespuesGlosa) {
		this.imprimirFirmasImpresionRespuesGlosa = imprimirFirmasImpresionRespuesGlosa;
	}
	

	/**
	 * @return the pathArchivosPlanosFurips
	 */
	public String getPathArchivosPlanosFurips() {
		return pathArchivosPlanosFurips;
	}

	/**
	 * @param pathArchivosPlanosFurips the pathArchivosPlanosFurips to set
	 */
	public void setPathArchivosPlanosFurips(String pathArchivosPlanosFurips) {
		this.pathArchivosPlanosFurips = pathArchivosPlanosFurips;
	}
	
	/**
	 * 
	 * @return validarAuditor
	 */
	public String getValidarAuditor() {
		return validarAuditor;
	}

	/**
	 * 
	 * @param validarAuditor
	 */
	public void setValidarAuditor(String validarAuditor) {
		this.validarAuditor = validarAuditor;
	}

	/**
	 * 
	 * @return validarUsuarioGlosa
	 */
	public String getValidarUsuarioGlosa() {
		return validarUsuarioGlosa;
	}

	/**
	 * 
	 * @param validarUsuarioGlosa
	 */
	public void setValidarUsuarioGlosa(String validarUsuarioGlosa) {
		this.validarUsuarioGlosa = validarUsuarioGlosa;
	}

	/**
	 * 
	 * @return numeroGlosasRegistradasXFactura
	 */
	public String getNumeroGlosasRegistradasXFactura() {
		return numeroGlosasRegistradasXFactura;
	}

	/**
	 * 
	 * @param numeroGlosasRegistradasXFactura
	 */
	public void setNumeroGlosasRegistradasXFactura(
			String numeroGlosasRegistradasXFactura) {
		this.numeroGlosasRegistradasXFactura = numeroGlosasRegistradasXFactura;
	}

	/**
	 * 
	 * @return numeroDiasNotificarGlosa
	 */
	public String getNumeroDiasNotificarGlosa() {
		return numeroDiasNotificarGlosa;
	}

	/**
	 * 
	 * @param numeroDiasNotificarGlosa
	 */
	public void setNumeroDiasNotificarGlosa(String numeroDiasNotificarGlosa) {
		this.numeroDiasNotificarGlosa = numeroDiasNotificarGlosa;
	}

	/**
	 * Mï¿½todo set de ProduccionEnParaleloConSistemaAnterior
	 * @param produccionEnParaleloConSistemaAnterior
	 */
	public void setProduccionEnParaleloConSistemaAnterior(
			String produccionEnParaleloConSistemaAnterior) {
		this.produccionEnParaleloConSistemaAnterior = produccionEnParaleloConSistemaAnterior;
	}

	/**
	 * Mï¿½todo get de ProduccionEnParaleloConSistemaAnterior
	 * @return produccionEnParaleloConSistemaAnterior
	 */
	public String getProduccionEnParaleloConSistemaAnterior() {
		return produccionEnParaleloConSistemaAnterior;
	}

	/**
	 * Mï¿½todo set de ExisteFacturaVaria
	 * @param HashMap existeFacturaVaria
	 */
	public void setExisteFacturaVaria(HashMap existeFacturaVaria) {
		this.existeFacturaVaria = existeFacturaVaria;
	}

	/**
	 * Mï¿½todo get de ExisteFacturaVaria
	 * @return HashMap con el primer registro de facturas_varias
	 */
	public HashMap getExisteFacturaVaria() {
		return existeFacturaVaria;
	}
	
	/**
	 * Mï¿½todo set de ExisteFacturaVaria para llaves individuales
	 * @param HashMap existeFacturaVaria
	 */
	public Object getExisteFacturaVaria(String key) {
		return existeFacturaVaria.get(key);
	}
	
	/**
	 * Mï¿½todo get de ExisteFacturaVaria para llaves sencillas
	 * @return HashMap con el primer registro de facturas_varias
	 */
	public void setExisteFacturaVaria(String key,Object value) {
		this.existeFacturaVaria.put(key, value);
	}

	/**
	 * @return the mensaje
	 */
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	/**
	 * @param mensaje the mensaje to set
	 */
	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * @return the validarPoolesFact
	 */
	public String getValidarPoolesFact() {
		return validarPoolesFact;
	}

	/**
	 * @param validarPoolesFact the validarPoolesFact to set
	 */
	public void setValidarPoolesFact(String validarPoolesFact) {
		this.validarPoolesFact = validarPoolesFact;
	}

	/**
	 * @return the clasesInventariosPaqMatQx
	 */
	public HashMap getClasesInventariosPaqMatQx() {
		return clasesInventariosPaqMatQx;
	}

	/**
	 * @param clasesInventariosPaqMatQx the clasesInventariosPaqMatQx to set
	 */
	public void setClasesInventariosPaqMatQx(HashMap clasesInventariosPaqMatQx) {
		this.clasesInventariosPaqMatQx = clasesInventariosPaqMatQx;
	}
 	
	/**
	 * @return Returns the clasesInventariosPaqMatQx.
	 */
	public Object getClasesInventariosPaqMatQx(Object key) {
		return clasesInventariosPaqMatQx.get(key);
	}

	/**
	 * @param clasesInventariosPaqMatQx The clasesInventariosPaqMatQx to set.
	 */
	public void setClasesInventariosPaqMatQx(Object key, Object value) {
		this.clasesInventariosPaqMatQx.put(key, value);
	}
	
	public String getRequiereGlosaInactivar() {
		return requiereGlosaInactivar;
	}

	public void setRequiereGlosaInactivar(String requiereGlosaInactivar) {
		this.requiereGlosaInactivar = requiereGlosaInactivar;
	}

	/** numDigCaptNumIdPac	 */
	public String getNumDigCaptNumIdPac() {	return numDigCaptNumIdPac;	}
	public void setNumDigCaptNumIdPac(String numDigCaptNumIdPac) {	this.numDigCaptNumIdPac = numDigCaptNumIdPac;	}
	
	/**
	 * @return the mostrarEnviarEpicrisisEvol
	 */
	public String getMostrarEnviarEpicrisisEvol() {
		return mostrarEnviarEpicrisisEvol;
	}

	public String getNumeroMaximoDiasGenOrdenesAmbServicios() {
		return numeroMaximoDiasGenOrdenesAmbServicios;
	}

	public void setNumeroMaximoDiasGenOrdenesAmbServicios(
			String numeroMaximoDiasGenOrdenesAmbServicios) {
		this.numeroMaximoDiasGenOrdenesAmbServicios = numeroMaximoDiasGenOrdenesAmbServicios;
	}
	
	
	public String getMultiploMinGeneracionCita() {
		return multiploMinGeneracionCita;
	}

	public void setMultiploMinGeneracionCita(
			String multiploMinGeneracionCita) {
		this.multiploMinGeneracionCita = multiploMinGeneracionCita;
	}
	
	public String getNumDiasAntFActualAgendaOd() {
		return numDiasAntFActualAgendaOd;
	}

	public void setNumDiasAntFActualAgendaOd(
			String numDiasAntFActualAgendaOd) {
		this.numDiasAntFActualAgendaOd = numDiasAntFActualAgendaOd;
	}


	/**
	 * @param mostrarEnviarEpicrisisEvol the mostrarEnviarEpicrisisEvol to set
	 */
	public void setMostrarEnviarEpicrisisEvol(String mostrarEnviarEpicrisisEvol) {
		this.mostrarEnviarEpicrisisEvol = mostrarEnviarEpicrisisEvol;
	}

	public String getTipoUsuarioaReportarSol() {
		return tipoUsuarioaReportarSol;
	}

	public void setTipoUsuarioaReportarSol(String tipoUsuarioaReportarSol) {
		this.tipoUsuarioaReportarSol = tipoUsuarioaReportarSol;
	}

	public String getManejoEspecialInstitucionesOdontologia() {
		return manejoEspecialInstitucionesOdontologia;
	}

	public void setManejoEspecialInstitucionesOdontologia(
			String manejoEspecialInstitucionesOdontologia) {
		this.manejoEspecialInstitucionesOdontologia = manejoEspecialInstitucionesOdontologia;
	}

	/**
	 * @return the maximoNumeroCuotasFinanciacion
	 */
	public String getMaximoNumeroCuotasFinanciacion() {
		return maximoNumeroCuotasFinanciacion;
	}

	/**
	 * @param maximoNumeroCuotasFinanciacion the maximoNumeroCuotasFinanciacion to set
	 */
	public void setMaximoNumeroCuotasFinanciacion(String maximoNumeroCuotasFinanciacion) {
		this.maximoNumeroCuotasFinanciacion = maximoNumeroCuotasFinanciacion;
	}

	/**
	 * @return the maximoNumeroDiasFinanciacionPorCuota
	 */
	public String getMaximoNumeroDiasFinanciacionPorCuota() {
		return maximoNumeroDiasFinanciacionPorCuota;
	}

	/**
	 * @param maximoNumeroDiasFinanciacionPorCuota the maximoNumeroDiasFinanciacionPorCuota to set
	 */
	public void setMaximoNumeroDiasFinanciacionPorCuota(
			String maximoNumeroDiasFinanciacionPorCuota) {
		this.maximoNumeroDiasFinanciacionPorCuota = maximoNumeroDiasFinanciacionPorCuota;
	}

	/**
	 * @return the formatoDocumentosGarantia_Pagare
	 */
	public String getFormatoDocumentosGarantia_Pagare() {
		return formatoDocumentosGarantia_Pagare;
	}

	/**
	 * @param formatoDocumentosGarantia_Pagare the formatoDocumentosGarantia_Pagare to set
	 */
	public void setFormatoDocumentosGarantia_Pagare(
			String formatoDocumentosGarantia_Pagare) {
		this.formatoDocumentosGarantia_Pagare = formatoDocumentosGarantia_Pagare;
	}

	/**
	 * @return the ocupacionOdontologo
	 */
	public String getOcupacionOdontologo() {
		return ocupacionOdontologo;
	}

	/**
	 * @param ocupacionOdontologo the ocupacionOdontologo to set
	 */
	public void setOcupacionOdontologo(String ocupacionOdontologo) {
		this.ocupacionOdontologo = ocupacionOdontologo;
	}

	/**
	 * @return the ocupacionAuxiliarOdontologo
	 */
	public String getOcupacionAuxiliarOdontologo() {
		return ocupacionAuxiliarOdontologo;
	}

	/**
	 * @param ocupacionAuxiliarOdontologo the ocupacionAuxiliarOdontologo to set
	 */
	public void setOcupacionAuxiliarOdontologo(String ocupacionAuxiliarOdontologo) {
		this.ocupacionAuxiliarOdontologo = ocupacionAuxiliarOdontologo;
	}

	/**
	 * @return the edadFinalNinez
	 */
	public String getEdadFinalNinez() {
		return edadFinalNinez;
	}

	/**
	 * @param edadFinalNinez the edadFinalNinez to set
	 */
	public void setEdadFinalNinez(String edadFinalNinez) {
		this.edadFinalNinez = edadFinalNinez;
	}

	/**
	 * @return the edadInicioAdulto
	 */
	public String getEdadInicioAdulto() {
		return edadInicioAdulto;
	}

	/**
	 * @param edadInicioAdulto the edadInicioAdulto to set
	 */
	public void setEdadInicioAdulto(String edadInicioAdulto) {
		this.edadInicioAdulto = edadInicioAdulto;
	}

	/**
	 * @return the minutosCaducaCitasReservadas
	 */
	public String getMinutosCaducaCitasReservadas() {
		return minutosCaducaCitasReservadas;
	}

	/**
	 * @param minutosCaducaCitasReservadas the minutosCaducaCitasReservadas to set
	 */
	public void setMinutosCaducaCitasReservadas(String minutosCaducaCitasReservadas) {
		this.minutosCaducaCitasReservadas = minutosCaducaCitasReservadas;
	}

	/**
	 * @return the minutosCaducaCitasAsignadasReprogramadas
	 */
	public String getMinutosCaducaCitasAsignadasReprogramadas() {
		return minutosCaducaCitasAsignadasReprogramadas;
	}

	/**
	 * @param minutosCaducaCitasAsignadasReprogramadas the minutosCaducaCitasAsignadasReprogramadas to set
	 */
	public void setMinutosCaducaCitasAsignadasReprogramadas(
			String minutosCaducaCitasAsignadasReprogramadas) {
		this.minutosCaducaCitasAsignadasReprogramadas = minutosCaducaCitasAsignadasReprogramadas;
	}

	/**
	 * @return the ejecutarProcAutoActualizacionCitasOdoNoAsistio
	 */
	public String getEjecutarProcAutoActualizacionCitasOdoNoAsistio() {
		return ejecutarProcAutoActualizacionCitasOdoNoAsistio;
	}

	/**
	 * @param ejecutarProcAutoActualizacionCitasOdoNoAsistio the ejecutarProcAutoActualizacionCitasOdoNoAsistio to set
	 */
	public void setEjecutarProcAutoActualizacionCitasOdoNoAsistio(
			String ejecutarProcAutoActualizacionCitasOdoNoAsistio) {
		this.ejecutarProcAutoActualizacionCitasOdoNoAsistio = ejecutarProcAutoActualizacionCitasOdoNoAsistio;
	}

	/**
	 * @return the horaEjecutarProcAutoActualizacionCitasOdoNoAsistio
	 */
	public String getHoraEjecutarProcAutoActualizacionCitasOdoNoAsistio() {
		return horaEjecutarProcAutoActualizacionCitasOdoNoAsistio;
	}

	/**
	 * @param horaEjecutarProcAutoActualizacionCitasOdoNoAsistio the horaEjecutarProcAutoActualizacionCitasOdoNoAsistio to set
	 */
	public void setHoraEjecutarProcAutoActualizacionCitasOdoNoAsistio(
			String horaEjecutarProcAutoActualizacionCitasOdoNoAsistio) {
		this.horaEjecutarProcAutoActualizacionCitasOdoNoAsistio = horaEjecutarProcAutoActualizacionCitasOdoNoAsistio;
	}

	/**
	 * @return the minutosEsperaAsignarCitasOdoCaducadas
	 */
	public String getMinutosEsperaAsignarCitasOdoCaducadas() {
		return minutosEsperaAsignarCitasOdoCaducadas;
	}

	/**
	 * @param minutosEsperaAsignarCitasOdoCaducadas the minutosEsperaAsignarCitasOdoCaducadas to set
	 */
	public void setMinutosEsperaAsignarCitasOdoCaducadas(
			String minutosEsperaAsignarCitasOdoCaducadas) {
		this.minutosEsperaAsignarCitasOdoCaducadas = minutosEsperaAsignarCitasOdoCaducadas;
	}

	/**
	 * @return the escalaPacientePerfil
	 */
	public String getEscalaPacientePerfil() {
		return escalaPacientePerfil;
	}

	/**
	 * @param escalaPacientePerfil the escalaPacientePerfil to set
	 */
	public void setEscalaPacientePerfil(String escalaPacientePerfil) {
		this.escalaPacientePerfil = escalaPacientePerfil;
	}

	/**
	 * @return the escalas
	 */
	public ArrayList<HashMap<String, Object>> getEscalas() {
		return escalas;
	}

	/**
	 * @param escalas the escalas to set
	 */
	public void setEscalas(ArrayList<HashMap<String, Object>> escalas) {
		this.escalas = escalas;
	}

	public String getUtilizanProgramasOdontologicosEnInstitucion() {
		return utilizanProgramasOdontologicosEnInstitucion;
	}

	public void setUtilizanProgramasOdontologicosEnInstitucion(
			String utilizanProgramasOdontologicosEnInstitucion) {
		this.utilizanProgramasOdontologicosEnInstitucion = utilizanProgramasOdontologicosEnInstitucion;
	}

	public ArrayList<DtoDetalleHallazgoProgramaServicio> getListadoExistenDHPS() {
		return listadoExistenDHPS;
	}

	public void setListadoExistenDHPS(
			ArrayList<DtoDetalleHallazgoProgramaServicio> listadoExistenDHPS) {
		this.listadoExistenDHPS = listadoExistenDHPS;
	}

	public boolean isModificarUPOEI() {
		return modificarUPOEI;
	}

	public void setModificarUPOEI(boolean modificarUPOEI) {
		this.modificarUPOEI = modificarUPOEI;
	}

	/**
	 * @return the tiempoVigenciaPresupuestoOdo
	 */
	public String getTiempoVigenciaPresupuestoOdo() {
		return tiempoVigenciaPresupuestoOdo;
	}

	/**
	 * @param tiempoVigenciaPresupuestoOdo the tiempoVigenciaPresupuestoOdo to set
	 */
	public void setTiempoVigenciaPresupuestoOdo(String tiempoVigenciaPresupuestoOdo) {
		this.tiempoVigenciaPresupuestoOdo = tiempoVigenciaPresupuestoOdo;
	}

	/**
	 * @return the permiteCambiarServiciosCitaAtencionOdo
	 */
	public String getPermiteCambiarServiciosCitaAtencionOdo() {
		return permiteCambiarServiciosCitaAtencionOdo;
	}

	/**
	 * @param permiteCambiarServiciosCitaAtencionOdo the permiteCambiarServiciosCitaAtencionOdo to set
	 */
	public void setPermiteCambiarServiciosCitaAtencionOdo(
			String permiteCambiarServiciosCitaAtencionOdo) {
		this.permiteCambiarServiciosCitaAtencionOdo = permiteCambiarServiciosCitaAtencionOdo;
	}

	/**
	 * @return the validaPresupuestoOdoContratado
	 */
	public String getValidaPresupuestoOdoContratado() {
		return validaPresupuestoOdoContratado;
	}

	/**
	 * @param validaPresupuestoOdoContratado the validaPresupuestoOdoContratado to set
	 */
	public void setValidaPresupuestoOdoContratado(
			String validaPresupuestoOdoContratado) {
		this.validaPresupuestoOdoContratado = validaPresupuestoOdoContratado;
	}

	/**
	 * @return the conveniosAMostrarXDefectoPresupuestoOdo
	 */
	public String getConveniosAMostrarXDefectoPresupuestoOdo() {
		return conveniosAMostrarXDefectoPresupuestoOdo;
	}

	/**
	 * @param conveniosAMostrarXDefectoPresupuestoOdo the conveniosAMostrarXDefectoPresupuestoOdo to set
	 */
	public void setConveniosAMostrarXDefectoPresupuestoOdo(
			String conveniosAMostrarXDefectoPresupuestoOdo) {
		this.conveniosAMostrarXDefectoPresupuestoOdo = conveniosAMostrarXDefectoPresupuestoOdo;
	}

	/**
	 * @return the tiempoMaxEsperaInactivarPresupuestoOdoSuspTemp
	 */
	public String getTiempoMaxEsperaInactivarPresupuestoOdoSuspTemp() {
		return tiempoMaxEsperaInactivarPresupuestoOdoSuspTemp;
	}

	/**
	 * @param tiempoMaxEsperaInactivarPresupuestoOdoSuspTemp the tiempoMaxEsperaInactivarPresupuestoOdoSuspTemp to set
	 */
	public void setTiempoMaxEsperaInactivarPresupuestoOdoSuspTemp(
			String tiempoMaxEsperaInactivarPresupuestoOdoSuspTemp) {
		this.tiempoMaxEsperaInactivarPresupuestoOdoSuspTemp = tiempoMaxEsperaInactivarPresupuestoOdoSuspTemp;
	}

	/**
	 * @return the ejecutarProcesoAutoActualizacionEstadosOdo
	 */
	public String getEjecutarProcesoAutoActualizacionEstadosOdo() {
		return ejecutarProcesoAutoActualizacionEstadosOdo;
	}

	/**
	 * @param ejecutarProcesoAutoActualizacionEstadosOdo the ejecutarProcesoAutoActualizacionEstadosOdo to set
	 */
	public void setEjecutarProcesoAutoActualizacionEstadosOdo(
			String ejecutarProcesoAutoActualizacionEstadosOdo) {
		this.ejecutarProcesoAutoActualizacionEstadosOdo = ejecutarProcesoAutoActualizacionEstadosOdo;
	}

	/**
	 * @return the horaEjecutarProcesoAutoActualizacionEstadosOdo
	 */
	public String getHoraEjecutarProcesoAutoActualizacionEstadosOdo() {
		return horaEjecutarProcesoAutoActualizacionEstadosOdo;
	}

	/**
	 * @param horaEjecutarProcesoAutoActualizacionEstadosOdo the horaEjecutarProcesoAutoActualizacionEstadosOdo to set
	 */
	public void setHoraEjecutarProcesoAutoActualizacionEstadosOdo(
			String horaEjecutarProcesoAutoActualizacionEstadosOdo) {
		this.horaEjecutarProcesoAutoActualizacionEstadosOdo = horaEjecutarProcesoAutoActualizacionEstadosOdo;
	}

	/**
	 * @return the motivoCancelacionPresupuestoSuspendidoTemp
	 */
	public String getMotivoCancelacionPresupuestoSuspendidoTemp() {
		return motivoCancelacionPresupuestoSuspendidoTemp;
	}

	/**
	 * @param motivoCancelacionPresupuestoSuspendidoTemp the motivoCancelacionPresupuestoSuspendidoTemp to set
	 */
	public void setMotivoCancelacionPresupuestoSuspendidoTemp(
			String motivoCancelacionPresupuestoSuspendidoTemp) {
		this.motivoCancelacionPresupuestoSuspendidoTemp = motivoCancelacionPresupuestoSuspendidoTemp;
	}

	/**
	 * @return the maximoTiempoSinEvolucionarParaInactivarPlanTratamiento
	 */
	public String getMaximoTiempoSinEvolucionarParaInactivarPlanTratamiento() {
		return maximoTiempoSinEvolucionarParaInactivarPlanTratamiento;
	}

	/**
	 * @param maximoTiempoSinEvolucionarParaInactivarPlanTratamiento the maximoTiempoSinEvolucionarParaInactivarPlanTratamiento to set
	 */
	public void setMaximoTiempoSinEvolucionarParaInactivarPlanTratamiento(
			String maximoTiempoSinEvolucionarParaInactivarPlanTratamiento) {
		this.maximoTiempoSinEvolucionarParaInactivarPlanTratamiento = maximoTiempoSinEvolucionarParaInactivarPlanTratamiento;
	}

	/**
	 * @return the prioridadParaAplicarPromocionesOdo
	 */
	public String getPrioridadParaAplicarPromocionesOdo() {
		return prioridadParaAplicarPromocionesOdo;
	}

	/**
	 * @param prioridadParaAplicarPromocionesOdo the prioridadParaAplicarPromocionesOdo to set
	 */
	public void setPrioridadParaAplicarPromocionesOdo(
			String prioridadParaAplicarPromocionesOdo) {
		this.prioridadParaAplicarPromocionesOdo = prioridadParaAplicarPromocionesOdo;
	}

	/**
	 * @return the diasParaDefinirMoraXDeudaPacientes
	 */
	public String getDiasParaDefinirMoraXDeudaPacientes() {
		return diasParaDefinirMoraXDeudaPacientes;
	}

	/**
	 * @param diasParaDefinirMoraXDeudaPacientes the diasParaDefinirMoraXDeudaPacientes to set
	 */
	public void setDiasParaDefinirMoraXDeudaPacientes(
			String diasParaDefinirMoraXDeudaPacientes) {
		this.diasParaDefinirMoraXDeudaPacientes = diasParaDefinirMoraXDeudaPacientes;
	}

	/**
	 * @return the conveniosAMostrarPresupuestoOdo
	 */
	public ArrayList<HashMap<String, Object>> getConveniosAMostrarPresupuestoOdo() {
		return conveniosAMostrarPresupuestoOdo;
	}

	/**
	 * @param conveniosAMostrarPresupuestoOdo the conveniosAMostrarPresupuestoOdo to set
	 */
	public void setConveniosAMostrarPresupuestoOdo(
			ArrayList<HashMap<String, Object>> conveniosAMostrarPresupuestoOdo) {
		this.conveniosAMostrarPresupuestoOdo = conveniosAMostrarPresupuestoOdo;
	}

	/**
	 * @return the convenios
	 */
	public ArrayList<HashMap<String, Object>> getConvenios() {
		return convenios;
	}

	/**
	 * @param convenios the convenios to set
	 */
	public void setConvenios(ArrayList<HashMap<String, Object>> convenios) {
		this.convenios = convenios;
	}

	/**
	 * @return the obligarRegIncapaPacienteHospitalizado
	 */
	public String getObligarRegIncapaPacienteHospitalizado() {
		return obligarRegIncapaPacienteHospitalizado;
	}

	/**
	 * @param obligarRegIncapaPacienteHospitalizado the obligarRegIncapaPacienteHospitalizado to set
	 */
	public void setObligarRegIncapaPacienteHospitalizado(
			String obligarRegIncapaPacienteHospitalizado) {
		this.obligarRegIncapaPacienteHospitalizado = obligarRegIncapaPacienteHospitalizado;
	}

	/**
	 * @return the motivosAtencionOdontologica
	 */
	public ArrayList<HashMap<String, Object>> getMotivosAtencionOdontologica() {
		return motivosAtencionOdontologica;
	}

	/**
	 * @param motivosAtencionOdontologica the motivosAtencionOdontologica to set
	 */
	public void setMotivosAtencionOdontologica(
			ArrayList<HashMap<String, Object>> motivosAtencionOdontologica) {
		this.motivosAtencionOdontologica = motivosAtencionOdontologica;
	}
	
	/**
	 * @return the institucionRegistraAtencionExterna
	 */
	public String getInstitucionRegistraAtencionExterna() {
		return institucionRegistraAtencionExterna;
	}

	/**
	 * @param institucionRegistraAtencionExterna the institucionRegistraAtencionExterna to set
	 */
	public void setInstitucionRegistraAtencionExterna(
			String institucionRegistraAtencionExterna) {
		this.institucionRegistraAtencionExterna = institucionRegistraAtencionExterna;
	}

	/**
	 * @return the contabilizacionRequeridoProcesoAsocioFacCuentaCapitacion
	 */
	public String getContabilizacionRequeridoProcesoAsocioFacCuentaCapitacion() {
		return contabilizacionRequeridoProcesoAsocioFacCuentaCapitacion;
	}

	/**
	 * @param contabilizacionRequeridoProcesoAsocioFacCuentaCapitacion the contabilizacionRequeridoProcesoAsocioFacCuentaCapitacion to set
	 */
	public void setContabilizacionRequeridoProcesoAsocioFacCuentaCapitacion(
			String contabilizacionRequeridoProcesoAsocioFacCuentaCapitacion) {
		this.contabilizacionRequeridoProcesoAsocioFacCuentaCapitacion = contabilizacionRequeridoProcesoAsocioFacCuentaCapitacion;
	}

	/**
	 * @return the cuentaContablePagare
	 */
	public String getCuentaContablePagare() {
		return cuentaContablePagare;
	}

	/**
	 * @param cuentaContablePagare the cuentaContablePagare to set
	 */
	public void setCuentaContablePagare(String cuentaContablePagare) {
		this.cuentaContablePagare = cuentaContablePagare;
	}

	/**
	 * @return the cuentaContableLetra
	 */
	public String getCuentaContableLetra() {
		return cuentaContableLetra;
	}

	/**
	 * @param cuentaContableLetra the cuentaContableLetra to set
	 */
	public void setCuentaContableLetra(String cuentaContableLetra) {
		this.cuentaContableLetra = cuentaContableLetra;
	}

	public String getImprimirFirmasImpresionCCCapitacion() {
		return imprimirFirmasImpresionCCCapitacion;
	}

	//Anexo 992
	
	public void setImprimirFirmasImpresionCCCapitacion(
			String imprimirFirmasImpresionCCCapitacion) {
		this.imprimirFirmasImpresionCCCapitacion = imprimirFirmasImpresionCCCapitacion;
	}

	public String getEncabezadoFormatoImpresionFacturaOCCCapitacion() {
		return encabezadoFormatoImpresionFacturaOCCCapitacion;
	}

	public void setEncabezadoFormatoImpresionFacturaOCCCapitacion(
			String encabezadoFormatoImpresionFacturaOCCCapitacion) {
		this.encabezadoFormatoImpresionFacturaOCCCapitacion = encabezadoFormatoImpresionFacturaOCCCapitacion;
	}

	public String getPiePaginaFormatoImpresionFacturaOCCCapitacion() {
		return piePaginaFormatoImpresionFacturaOCCCapitacion;
	}

	public void setPiePaginaFormatoImpresionFacturaOCCCapitacion(
			String piePaginaFormatoImpresionFacturaOCCCapitacion) {
		this.piePaginaFormatoImpresionFacturaOCCCapitacion = piePaginaFormatoImpresionFacturaOCCCapitacion;
	}

	public DtoFirmasValoresPorDefecto getDtoFirmas() {
		return dtoFirmas;
	}

	public void setDtoFirmas(DtoFirmasValoresPorDefecto dtoFirmas) {
		this.dtoFirmas = dtoFirmas;
	}

	public ArrayList<DtoFirmasValoresPorDefecto> getListadoFirmas() {
		return listadoFirmas;
	}

	public void setListadoFirmas(ArrayList<DtoFirmasValoresPorDefecto> listadoFirmas) {
		this.listadoFirmas = listadoFirmas;
	}

	public String getValorDefecto() {
		return valorDefecto;
	}

	public void setValorDefecto(String valorDefecto) {
		this.valorDefecto = valorDefecto;
	}

	public int getPosFirma() {
		return posFirma;
	}

	public void setPosFirma(int posFirma) {
		this.posFirma = posFirma;
	}

	public String getImprimirFirmasEnImpresionCC() {
		return imprimirFirmasEnImpresionCC;
	}

	public void setImprimirFirmasEnImpresionCC(String imprimirFirmasEnImpresionCC) {
		this.imprimirFirmasEnImpresionCC = imprimirFirmasEnImpresionCC;
	}

	public String getNumeroMesesAMostrarEnReportesPresupuestoCapitacion() {
		return numeroMesesAMostrarEnReportesPresupuestoCapitacion;
	}

	public void setNumeroMesesAMostrarEnReportesPresupuestoCapitacion(
			String numeroMesesAMostrarEnReportesPresupuestoCapitacion) {
		this.numeroMesesAMostrarEnReportesPresupuestoCapitacion = numeroMesesAMostrarEnReportesPresupuestoCapitacion;
	}
	//Fin anexo 992

	public String getReciboCajaAutomaticoGeneracionFacturaVaria() {
		return reciboCajaAutomaticoGeneracionFacturaVaria;
	}

	public void setReciboCajaAutomaticoGeneracionFacturaVaria(
			String reciboCajaAutomaticoGeneracionFacturaVaria) {
		this.reciboCajaAutomaticoGeneracionFacturaVaria = reciboCajaAutomaticoGeneracionFacturaVaria;
	}

	//Anexo 958
	public String getConceptoIngresoFacturasVarias() {
		return conceptoIngresoFacturasVarias;
	}

	public void setConceptoIngresoFacturasVarias(
			String conceptoIngresoFacturasVarias) {
		this.conceptoIngresoFacturasVarias = conceptoIngresoFacturasVarias;
	}

	public ArrayList<DtoConceptosIngTesoreria> getListadoConceptos() {
		return listadoConceptos;
	}

	public void setListadoConceptos(
			ArrayList<DtoConceptosIngTesoreria> listadoConceptos) {
		this.listadoConceptos = listadoConceptos;
	}

	//Fin anexo 958
	
	
	//Anexo 888
	
	public String getEsRequeridoProgramarCitaAlContratarPresupuestoOdon() {
		return esRequeridoProgramarCitaAlContratarPresupuestoOdon;
	}

	public void setEsRequeridoProgramarCitaAlContratarPresupuestoOdon(
			String esRequeridoProgramarCitaAlContratarPresupuestoOdon) {
		this.esRequeridoProgramarCitaAlContratarPresupuestoOdon = esRequeridoProgramarCitaAlContratarPresupuestoOdon;
	}

	public String getMotivoAnulacionSolicitudAutorizacionDescuentoPresupeustoOdontologico() {
		return motivoAnulacionSolicitudAutorizacionDescuentoPresupeustoOdontologico;
	}

	public void setMotivoAnulacionSolicitudAutorizacionDescuentoPresupeustoOdontologico(
			String motivoAnulacionSolicitudAutorizacionDescuentoPresupeustoOdontologico) {
		this.motivoAnulacionSolicitudAutorizacionDescuentoPresupeustoOdontologico = motivoAnulacionSolicitudAutorizacionDescuentoPresupeustoOdontologico;
	}
	//Fin anexo 888

	public ArrayList<DtoMotivoDescuento> getListMotivosDescuentos() {
		return listMotivosDescuentos;
	}

	public void setListMotivosDescuentos(
			ArrayList<DtoMotivoDescuento> listMotivosDescuentos) {
		this.listMotivosDescuentos = listMotivosDescuentos;
	}

	
	//Anexo 888 Pt II

	public String getRequeridoProgramarCitaControlAlTerminarPresupuestoOdon() {
		return requeridoProgramarCitaControlAlTerminarPresupuestoOdon;
	}

	public void setRequeridoProgramarCitaControlAlTerminarPresupuestoOdon(
			String requeridoProgramarCitaControlAlTerminarPresupuestoOdon) {
		this.requeridoProgramarCitaControlAlTerminarPresupuestoOdon = requeridoProgramarCitaControlAlTerminarPresupuestoOdon;
	}

	public String getTiempoDiasParaAgendarCitaControlAlTerminarPresupuestoOdon() {
		return tiempoDiasParaAgendarCitaControlAlTerminarPresupuestoOdon;
	}

	public void setTiempoDiasParaAgendarCitaControlAlTerminarPresupuestoOdon(
			String tiempoDiasParaAgendarCitaControlAlTerminarPresupuestoOdon) {
		this.tiempoDiasParaAgendarCitaControlAlTerminarPresupuestoOdon = tiempoDiasParaAgendarCitaControlAlTerminarPresupuestoOdon;
	}

	public String getInstitucionManejaFacturacionAutomatica() {
		return institucionManejaFacturacionAutomatica;
	}

	public void setInstitucionManejaFacturacionAutomatica(
			String institucionManejaFacturacionAutomatica) {
		this.institucionManejaFacturacionAutomatica = institucionManejaFacturacionAutomatica;
	}

	public HashMap getServiciosMap() {
		return serviciosMap;
	}

	public void setServiciosMap(HashMap serviciosMap) {
		this.serviciosMap = serviciosMap;
	}

	public String getRequeridoProgramarCitaControlAlTerminarPresupuestoOdonServicio() {
		return requeridoProgramarCitaControlAlTerminarPresupuestoOdonServicio;
	}

	public void setRequeridoProgramarCitaControlAlTerminarPresupuestoOdonServicio(
			String requeridoProgramarCitaControlAlTerminarPresupuestoOdonServicio) {
		this.requeridoProgramarCitaControlAlTerminarPresupuestoOdonServicio = requeridoProgramarCitaControlAlTerminarPresupuestoOdonServicio;
	}

	public String getManejaConsecutivoFacturaPorCentroAtencion() {
		return manejaConsecutivoFacturaPorCentroAtencion;
	}

	public void setManejaConsecutivoFacturaPorCentroAtencion(
			String manejaConsecutivoFacturaPorCentroAtencion) {
		this.manejaConsecutivoFacturaPorCentroAtencion = manejaConsecutivoFacturaPorCentroAtencion;
	}

	public String getManejaConsecutivosTesoreriaPorCentroAtencion() {
		return manejaConsecutivosTesoreriaPorCentroAtencion;
	}

	public void setManejaConsecutivosTesoreriaPorCentroAtencion(
			String manejaConsecutivosTesoreriaPorCentroAtencion) {
		this.manejaConsecutivosTesoreriaPorCentroAtencion = manejaConsecutivosTesoreriaPorCentroAtencion;
	}

	public String getTamanioImpresionRC() {
		return tamanioImpresionRC;
	}

	public void setTamanioImpresionRC(String tamanioImpresionRC) {
		this.tamanioImpresionRC = tamanioImpresionRC;
	}

	public boolean isModificarMultiploMinGeneracionCita() {
		return modificarMultiploMinGeneracionCita;
	}

	public void setModificarMultiploMinGeneracionCita(
			boolean modificarMultiploMinGeneracionCita) {
		this.modificarMultiploMinGeneracionCita = modificarMultiploMinGeneracionCita;
	}

	public boolean isExistePresupuesto() {
		return existePresupuesto;
	}

	public void setExistePresupuesto(boolean existePresupuesto) {
		this.existePresupuesto = existePresupuesto;
	}

	/**
	 * @return the requiereAperturaCierreCaja
	 */
	public String getRequiereAperturaCierreCaja() {
		return requiereAperturaCierreCaja;
	}

	/**
	 * @param requiereAperturaCierreCaja the requiereAperturaCierreCaja to set
	 */
	public void setRequiereAperturaCierreCaja(String requiereAperturaCierreCaja) {
		this.requiereAperturaCierreCaja = requiereAperturaCierreCaja;
	}
	
	/**
	 * @return the requeridoProgramarProximaCitaEnAtencion
	 */
	public String getRequeridoProgramarProximaCitaEnAtencion() {
		return requeridoProgramarProximaCitaEnAtencion;
	}

	/**
	 * @param requeridoProgramarProximaCitaEnAtencion the requeridoProgramarProximaCitaEnAtencion to set
	 */
	public void setRequeridoProgramarProximaCitaEnAtencion(
			String requeridoProgramarProximaCitaEnAtencion) {
		this.requeridoProgramarProximaCitaEnAtencion = requeridoProgramarProximaCitaEnAtencion;
	}

	public String getActivarBotonGenerarSolicitudOrdenAmbulatora() {
		return activarBotonGenerarSolicitudOrdenAmbulatora;
	}

	public void setActivarBotonGenerarSolicitudOrdenAmbulatora(
			String activarBotonGenerarSolicitudOrdenAmbulatora) {
		this.activarBotonGenerarSolicitudOrdenAmbulatora = activarBotonGenerarSolicitudOrdenAmbulatora;
	}

	public String getEsRequeridoTestigoSolicitudAceptacionTrasladoCaja() {
		return esRequeridoTestigoSolicitudAceptacionTrasladoCaja;
	}

	public void setEsRequeridoTestigoSolicitudAceptacionTrasladoCaja(
			String esRequeridoTestigoSolicitudAceptacionTrasladoCaja) {
		this.esRequeridoTestigoSolicitudAceptacionTrasladoCaja = esRequeridoTestigoSolicitudAceptacionTrasladoCaja;
	}

	public String getInstitucionManejaCajaPrincipal() {
		return institucionManejaCajaPrincipal;
	}

	public void setInstitucionManejaCajaPrincipal(
			String institucionManejaCajaPrincipal) {
		this.institucionManejaCajaPrincipal = institucionManejaCajaPrincipal;
	}

	public String getInstitucionManejaTrasladoOtraCajaRecaudo() {
		return institucionManejaTrasladoOtraCajaRecaudo;
	}

	public void setInstitucionManejaTrasladoOtraCajaRecaudo(
			String institucionManejaTrasladoOtraCajaRecaudo) {
		this.institucionManejaTrasladoOtraCajaRecaudo = institucionManejaTrasladoOtraCajaRecaudo;
	}

	public String getInstitucionManejaEntregaATransportadoraValores() {
		return institucionManejaEntregaATransportadoraValores;
	}

	public void setInstitucionManejaEntregaATransportadoraValores(
			String institucionManejaEntregaATransportadoraValores) {
		this.institucionManejaEntregaATransportadoraValores = institucionManejaEntregaATransportadoraValores;
	}

	public String getTrasladoAbonosPacienteSoloPacientesConPresupuestoContratado() {
		return trasladoAbonosPacienteSoloPacientesConPresupuestoContratado;
	}

	public void setTrasladoAbonosPacienteSoloPacientesConPresupuestoContratado(
			String trasladoAbonosPacienteSoloPacientesConPresupuestoContratado) {
		this.trasladoAbonosPacienteSoloPacientesConPresupuestoContratado = trasladoAbonosPacienteSoloPacientesConPresupuestoContratado;
	}

	public String getControlarAbonoPacientePorNroIngreso() {
		return controlarAbonoPacientePorNroIngreso;
	}

	public void setControlarAbonoPacientePorNroIngreso(
			String controlarAbonoPacientePorNroIngreso) {
		this.controlarAbonoPacientePorNroIngreso = controlarAbonoPacientePorNroIngreso;
	}

	public String getValidaEstadoContratoNominaALosProfesionalesSalud() {
		return validaEstadoContratoNominaALosProfesionalesSalud;
	}

	public void setValidaEstadoContratoNominaALosProfesionalesSalud(
			String validaEstadoContratoNominaALosProfesionalesSalud) {
		this.validaEstadoContratoNominaALosProfesionalesSalud = validaEstadoContratoNominaALosProfesionalesSalud;
	}

	public String getManejaInterfazUsuariosSistemaERP() {
		return manejaInterfazUsuariosSistemaERP;
	}

	public void setManejaInterfazUsuariosSistemaERP(
			String manejaInterfazUsuariosSistemaERP) {
		this.manejaInterfazUsuariosSistemaERP = manejaInterfazUsuariosSistemaERP;
	}

	public String getRequierGenerarSolicitudCambioServicio() {
		return requierGenerarSolicitudCambioServicio;
	}

	public void setRequierGenerarSolicitudCambioServicio(
			String requierGenerarSolicitudCambioServicio) {
		this.requierGenerarSolicitudCambioServicio = requierGenerarSolicitudCambioServicio;
	}

	public String getLasCitasDeControlSePermitenAsignarA() {
		return lasCitasDeControlSePermitenAsignarA;
	}

	public void setLasCitasDeControlSePermitenAsignarA(
			String lasCitasDeControlSePermitenAsignarA) {
		this.lasCitasDeControlSePermitenAsignarA = lasCitasDeControlSePermitenAsignarA;
	}

	/**
	 * @return the areaAperturaCuentaAutoPYP
	 */
	public DtoAreaAperturaCuentaAutoPYP getAreaAperturaCuentaAutoPYP() {
		return areaAperturaCuentaAutoPYP;
	}

	/**
	 * @param areaAperturaCuentaAutoPYP the areaAperturaCuentaAutoPYP to set
	 */
	public void setAreaAperturaCuentaAutoPYP(
			DtoAreaAperturaCuentaAutoPYP areaAperturaCuentaAutoPYP) {
		this.areaAperturaCuentaAutoPYP = areaAperturaCuentaAutoPYP;
	}

	/**
	 * @return the listadoAreasAperturaCuentaAutoPYP
	 */
	public ArrayList<DtoAreaAperturaCuentaAutoPYP> getListadoAreasAperturaCuentaAutoPYP() {
		return listadoAreasAperturaCuentaAutoPYP;
	}

	/**
	 * @param listadoAreasAperturaCuentaAutoPYP the listadoAreasAperturaCuentaAutoPYP to set
	 */
	public void setListadoAreasAperturaCuentaAutoPYP(
			ArrayList<DtoAreaAperturaCuentaAutoPYP> listadoAreasAperturaCuentaAutoPYP) {
		this.listadoAreasAperturaCuentaAutoPYP = listadoAreasAperturaCuentaAutoPYP;
	}

	/**
	 * @return the centrosAtencion
	 */
	public HashMap getCentrosAtencion() {
		return centrosAtencion;
	}

	/**
	 * @param centrosAtencion the centrosAtencion to set
	 */
	public void setCentrosAtencion(HashMap centrosAtencion) {
		this.centrosAtencion = centrosAtencion;
	}

	/**
	 * @return the centrosCosto
	 */
	public HashMap getCentrosCosto() {
		return centrosCosto;
	}

	/**
	 * @param centrosCosto the centrosCosto to set
	 */
	public void setCentrosCosto(HashMap centrosCosto) {
		this.centrosCosto = centrosCosto;
	}

	/**
	 * @return the pos
	 */
	public int getPos() {
		return pos;
	}

	/**
	 * @param pos the pos to set
	 */
	public void setPos(int pos) {
		this.pos = pos;
	}

	public String getManejaVentaTarjetaClienteOdontoSinEmision() {
		return manejaVentaTarjetaClienteOdontoSinEmision;
	}

	public void setManejaVentaTarjetaClienteOdontoSinEmision(
			String manejaVentaTarjetaClienteOdontoSinEmision) {
		this.manejaVentaTarjetaClienteOdontoSinEmision = manejaVentaTarjetaClienteOdontoSinEmision;
	}

	

	/**
	 * @return Retorna atributo mostrarGraficaCalculoIndicePlaca
	 */
	public String getMostrarGraficaCalculoIndicePlaca()
	{
		return mostrarGraficaCalculoIndicePlaca;
	}

	/**
	 * @param mostrarGraficaCalculoIndicePlaca Asigna atributo mostrarGraficaCalculoIndicePlaca
	 */
	public void setMostrarGraficaCalculoIndicePlaca(
			String mostrarGraficaCalculoIndicePlaca)
	{
		this.mostrarGraficaCalculoIndicePlaca = mostrarGraficaCalculoIndicePlaca;
	}
	
	
	public String getAprobarGlosaRegistro() {
		return aprobarGlosaRegistro;
	}

	public void setAprobarGlosaRegistro(String aprobarGlosaRegistro) {
		this.aprobarGlosaRegistro = aprobarGlosaRegistro;
	}

	public String getValidarPacienteParaVentaTarjeta() {
		return validarPacienteParaVentaTarjeta;
	}

	public void setValidarPacienteParaVentaTarjeta(
			String validarPacienteParaVentaTarjeta) {
		this.validarPacienteParaVentaTarjeta = validarPacienteParaVentaTarjeta;
	}

	public String getReciboCajaAutomaticoVentaTarjeta() {
		return reciboCajaAutomaticoVentaTarjeta;
	}

	public void setReciboCajaAutomaticoVentaTarjeta(
			String reciboCajaAutomaticoVentaTarjeta) {
		this.reciboCajaAutomaticoVentaTarjeta = reciboCajaAutomaticoVentaTarjeta;
	}
	

	public void setCancelarCitaAutoEstadoReprogramar(
			String cancelarCitaAutoEstadoReprogramar) {
		this.cancelarCitaAutoEstadoReprogramar = cancelarCitaAutoEstadoReprogramar;
	}

	public String getCancelarCitaAutoEstadoReprogramar() {
		return cancelarCitaAutoEstadoReprogramar;
	}
	

	public String getPermitirRegistrarReclamacionCuentasNoFacturadas() {
		return permitirRegistrarReclamacionCuentasNoFacturadas;
	}

	public void setPermitirRegistrarReclamacionCuentasNoFacturadas(
			String permitirRegistrarReclamacionCuentasNoFacturadas) {
		this.permitirRegistrarReclamacionCuentasNoFacturadas = permitirRegistrarReclamacionCuentasNoFacturadas;
	}

	public String getPermitirFacturarReclamarCuentasConRegistroPendientes() {
		return permitirFacturarReclamarCuentasConRegistroPendientes;
	}

	public void setPermitirFacturarReclamarCuentasConRegistroPendientes(
			String permitirFacturarReclamarCuentasConRegistroPendientes) {
		this.permitirFacturarReclamarCuentasConRegistroPendientes = permitirFacturarReclamarCuentasConRegistroPendientes;
	}

	public String getPermitirOrdenarMedicamentosPacienteUrgenciasConEgreso() {
		return permitirOrdenarMedicamentosPacienteUrgenciasConEgreso;
	}

	public void setPermitirOrdenarMedicamentosPacienteUrgenciasConEgreso(
			String permitirOrdenarMedicamentosPacienteUrgenciasConEgreso) {
		this.permitirOrdenarMedicamentosPacienteUrgenciasConEgreso = permitirOrdenarMedicamentosPacienteUrgenciasConEgreso;
	}

	public String getMostrarAdminMedicamentosArticulosDespachoCero() {
		return mostrarAdminMedicamentosArticulosDespachoCero;
	}

	public void setMostrarAdminMedicamentosArticulosDespachoCero(
			String mostrarAdminMedicamentosArticulosDespachoCero) {
		this.mostrarAdminMedicamentosArticulosDespachoCero = mostrarAdminMedicamentosArticulosDespachoCero;
	}

	public String getFormatoFacturaVaria() {
		return formatoFacturaVaria;
	}

	public void setFormatoFacturaVaria(String formatoFacturaVaria) {
		this.formatoFacturaVaria = formatoFacturaVaria;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo modificarFechaHoraInicioAtencionOdonto
	 * 
	 * @return  Retorna la variable modificarFechaHoraInicioAtencionOdonto
	 */
	public String getModificarFechaHoraInicioAtencionOdonto() {
		return modificarFechaHoraInicioAtencionOdonto;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo modificarFechaHoraInicioAtencionOdonto
	 * 
	 * @param  valor para el atributo modificarFechaHoraInicioAtencionOdonto 
	 */
	public void setModificarFechaHoraInicioAtencionOdonto(
			String modificarFechaHoraInicioAtencionOdonto) {
		this.modificarFechaHoraInicioAtencionOdonto = modificarFechaHoraInicioAtencionOdonto;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo entidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada
	 * 
	 * @return  Retorna la variable entidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada
	 */
	public String getEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada() {
		return entidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo entidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada
	 * 
	 * @param  valor para el atributo entidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada 
	 */
	public void setEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada(
			String entidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada) {
		this.entidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada = entidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada;
	}

	public String getPrioridadEntidadSubcontratada() {
		return prioridadEntidadSubcontratada;
	}

	public void setPrioridadEntidadSubcontratada(
			String prioridadEntidadSubcontratada) {
		this.prioridadEntidadSubcontratada = prioridadEntidadSubcontratada;
	}

	public String getRequiereAutorizacionCapitacionSubcontratadaParaFacturar() {
		return requiereAutorizacionCapitacionSubcontratadaParaFacturar;
	}

	public void setRequiereAutorizacionCapitacionSubcontratadaParaFacturar(
			String requiereAutorizacionCapitacionSubcontratadaParaFacturar) {
		this.requiereAutorizacionCapitacionSubcontratadaParaFacturar = requiereAutorizacionCapitacionSubcontratadaParaFacturar;
	}

	public String getManejaConsecutivoFacturasVariasPorCentroAtencion() {
		return manejaConsecutivoFacturasVariasPorCentroAtencion;
	}

	public void setManejaConsecutivoFacturasVariasPorCentroAtencion(
			String manejaConsecutivoFacturasVariasPorCentroAtencion) {
		this.manejaConsecutivoFacturasVariasPorCentroAtencion = manejaConsecutivoFacturasVariasPorCentroAtencion;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo formatoImpresionAutorEntidadSub
	
	 * @return retorna la variable formatoImpresionAutorEntidadSub 
	 * @author Angela Maria Aguirre 
	 */
	public String getFormatoImpresionAutorEntidadSub() {
		return formatoImpresionAutorEntidadSub;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo formatoImpresionAutorEntidadSub
	
	 * @param valor para el atributo formatoImpresionAutorEntidadSub 
	 * @author Angela Maria Aguirre 
	 */
	public void setFormatoImpresionAutorEntidadSub(
			String formatoImpresionAutorEntidadSub) {
		this.formatoImpresionAutorEntidadSub = formatoImpresionAutorEntidadSub;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo encFormatoImpresionAutorEntidadSub
	
	 * @return retorna la variable encFormatoImpresionAutorEntidadSub 
	 * @author Angela Maria Aguirre 
	 */
	public String getEncFormatoImpresionAutorEntidadSub() {
		return encFormatoImpresionAutorEntidadSub;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo encFormatoImpresionAutorEntidadSub
	
	 * @param valor para el atributo encFormatoImpresionAutorEntidadSub 
	 * @author Angela Maria Aguirre 
	 */
	public void setEncFormatoImpresionAutorEntidadSub(
			String encFormatoImpresionAutorEntidadSub) {
		this.encFormatoImpresionAutorEntidadSub = encFormatoImpresionAutorEntidadSub;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo piePagFormatoImpresionAutorEntidadSub
	
	 * @return retorna la variable piePagFormatoImpresionAutorEntidadSub 
	 * @author Angela Maria Aguirre 
	 */
	public String getPiePagFormatoImpresionAutorEntidadSub() {
		return piePagFormatoImpresionAutorEntidadSub;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo piePagFormatoImpresionAutorEntidadSub
	
	 * @param valor para el atributo piePagFormatoImpresionAutorEntidadSub 
	 * @author Angela Maria Aguirre 
	 */
	public void setPiePagFormatoImpresionAutorEntidadSub(
			String piePagFormatoImpresionAutorEntidadSub) {
		this.piePagFormatoImpresionAutorEntidadSub = piePagFormatoImpresionAutorEntidadSub;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo diasVigenciaAutorIndicativoTemp
	
	 * @return retorna la variable diasVigenciaAutorIndicativoTemp 
	 * @author Angela Maria Aguirre 
	 */
	public String getDiasVigenciaAutorIndicativoTemp() {
		return diasVigenciaAutorIndicativoTemp;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo diasVigenciaAutorIndicativoTemp
	
	 * @param valor para el atributo diasVigenciaAutorIndicativoTemp 
	 * @author Angela Maria Aguirre 
	 */
	public void setDiasVigenciaAutorIndicativoTemp(
			String diasVigenciaAutorIndicativoTemp) {
		this.diasVigenciaAutorIndicativoTemp = diasVigenciaAutorIndicativoTemp;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo diasProrrogaAutorizacion
	
	 * @return retorna la variable diasProrrogaAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public String getDiasProrrogaAutorizacion() {
		return diasProrrogaAutorizacion;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo diasProrrogaAutorizacion
	
	 * @param valor para el atributo diasProrrogaAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setDiasProrrogaAutorizacion(String diasProrrogaAutorizacion) {
		this.diasProrrogaAutorizacion = diasProrrogaAutorizacion;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo diasCalcularFechaVencAutorizacionServicio
	
	 * @return retorna la variable diasCalcularFechaVencAutorizacionServicio 
	 * @author Angela Maria Aguirre 
	 */
	public String getDiasCalcularFechaVencAutorizacionServicio() {
		return diasCalcularFechaVencAutorizacionServicio;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo diasCalcularFechaVencAutorizacionServicio
	
	 * @param valor para el atributo diasCalcularFechaVencAutorizacionServicio 
	 * @author Angela Maria Aguirre 
	 */
	public void setDiasCalcularFechaVencAutorizacionServicio(
			String diasCalcularFechaVencAutorizacionServicio) {
		this.diasCalcularFechaVencAutorizacionServicio = diasCalcularFechaVencAutorizacionServicio;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo diasCalcularFechaVencAutorizacionArticulo
	
	 * @return retorna la variable diasCalcularFechaVencAutorizacionArticulo 
	 * @author Angela Maria Aguirre 
	 */
	public String getDiasCalcularFechaVencAutorizacionArticulo() {
		return diasCalcularFechaVencAutorizacionArticulo;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo diasCalcularFechaVencAutorizacionArticulo
	
	 * @param valor para el atributo diasCalcularFechaVencAutorizacionArticulo 
	 * @author Angela Maria Aguirre 
	 */
	public void setDiasCalcularFechaVencAutorizacionArticulo(
			String diasCalcularFechaVencAutorizacionArticulo) {
		this.diasCalcularFechaVencAutorizacionArticulo = diasCalcularFechaVencAutorizacionArticulo;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo diasVigentesNuevaAutorizacionEstanciaSerArt
	
	 * @return retorna la variable diasVigentesNuevaAutorizacionEstanciaSerArt 
	 * @author Angela Maria Aguirre 
	 */
	public String getDiasVigentesNuevaAutorizacionEstanciaSerArt() {
		return diasVigentesNuevaAutorizacionEstanciaSerArt;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo diasVigentesNuevaAutorizacionEstanciaSerArt
	
	 * @param valor para el atributo diasVigentesNuevaAutorizacionEstanciaSerArt 
	 * @author Angela Maria Aguirre 
	 */
	public void setDiasVigentesNuevaAutorizacionEstanciaSerArt(
			String diasVigentesNuevaAutorizacionEstanciaSerArt) {
		this.diasVigentesNuevaAutorizacionEstanciaSerArt = diasVigentesNuevaAutorizacionEstanciaSerArt;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo horaProcesoCierreCapitacion
	
	 * @return retorna la variable horaProcesoCierreCapitacion 
	 * @author Angela Maria Aguirre 
	 */
	public String getHoraProcesoCierreCapitacion() {
		return horaProcesoCierreCapitacion;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo horaProcesoCierreCapitacion
	
	 * @param valor para el atributo horaProcesoCierreCapitacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setHoraProcesoCierreCapitacion(String horaProcesoCierreCapitacion) {
		this.horaProcesoCierreCapitacion = horaProcesoCierreCapitacion;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo permitirfacturarReclamCuentaRATREC
	
	 * @return retorna la variable permitirfacturarReclamCuentaRATREC 
	 * @author Angela Maria Aguirre 
	 */
	public String getPermitirfacturarReclamCuentaRATREC() {
		return permitirfacturarReclamCuentaRATREC;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo permitirfacturarReclamCuentaRATREC
	
	 * @param valor para el atributo permitirfacturarReclamCuentaRATREC 
	 * @author Angela Maria Aguirre 
	 */
	public void setPermitirfacturarReclamCuentaRATREC(
			String permitirfacturarReclamCuentaRATREC) {
		this.permitirfacturarReclamCuentaRATREC = permitirfacturarReclamCuentaRATREC;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo viaIngresoDetaleGlosa
	
	 * @return retorna la variable viaIngresoDetaleGlosa 
	 * @author Angela Maria Aguirre 
	 */
	public String[] getViaIngresoDetaleGlosa() {
		return viaIngresoDetaleGlosa;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo viaIngresoDetaleGlosa
	
	 * @param valor para el atributo viaIngresoDetaleGlosa 
	 * @author Angela Maria Aguirre 
	 */
	public void setViaIngresoDetaleGlosa(String[] viaIngresoDetaleGlosa) {
		this.viaIngresoDetaleGlosa = viaIngresoDetaleGlosa;
	}
	
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo viaIngresoDetaleGlosa
	
	 * @param valor para el atributo viaIngresoDetaleGlosa 
	 * @author Angela Maria Aguirre 
	 */
	public void setViaIngresoDetaleGlosa(String viaIngresoDetaleGlosa) {
		
		String[] valores = viaIngresoDetaleGlosa.split("-");
		if(valores!=null && valores.length>0){			
			this.viaIngresoDetaleGlosa = valores;		
		}		
	}
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo viaIngresoDetaleGlosa
	
	 * @return retorna la variable viaIngresoDetaleGlosa 
	 * @author Angela Maria Aguirre 
	 */
	public String getViaIngresoDetaleGlosaConcatenado() {
		String valor="";
		for(int i=0; i<this.viaIngresoDetaleGlosa.length;i++){
			valor+=this.viaIngresoDetaleGlosa[i]+"-";
		}
		if(valor.length()>=2){
			valor = valor.substring(0,valor.length()-1);
		}		
		return valor;		
	}
	
	public boolean getAyudanteViaIngresoDetaleGlosa(String valor) {
		boolean respuesta=false;
		for(int i=0; i<this.viaIngresoDetaleGlosa.length; i++)
		{
			if(viaIngresoDetaleGlosa[i].equals(valor))
			{
				respuesta=true;
			}
		}
		return respuesta;
	}
	
	/**
	 * Limpia el indicativo de confirmación para que se dejen deschekear los elementos
	 * @return
	 */
	public boolean getLimpiarAyudanteViaIngresoDetaleGlosa()
	{
		viaIngresoDetaleGlosa=new String[0];
		return true;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo formatoImpReservaCitaOdonto
	
	 * @return retorna la variable formatoImpReservaCitaOdonto 
	 * @author Angela Maria Aguirre 
	 */
	public String getFormatoImpReservaCitaOdonto() {
		return formatoImpReservaCitaOdonto;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo formatoImpReservaCitaOdonto
	
	 * @param valor para el atributo formatoImpReservaCitaOdonto 
	 * @author Angela Maria Aguirre 
	 */
	public void setFormatoImpReservaCitaOdonto(String formatoImpReservaCitaOdonto) {
		this.formatoImpReservaCitaOdonto = formatoImpReservaCitaOdonto;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo formatoImpAsignacionCitaOdonto
	
	 * @return retorna la variable formatoImpAsignacionCitaOdonto 
	 * @author Angela Maria Aguirre 
	 */
	public String getFormatoImpAsignacionCitaOdonto() {
		return formatoImpAsignacionCitaOdonto;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo formatoImpAsignacionCitaOdonto
	
	 * @param valor para el atributo formatoImpAsignacionCitaOdonto 
	 * @author Angela Maria Aguirre 
	 */
	public void setFormatoImpAsignacionCitaOdonto(
			String formatoImpAsignacionCitaOdonto) {
		this.formatoImpAsignacionCitaOdonto = formatoImpAsignacionCitaOdonto;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo fechaInicioCierreOrdenMedica
	
	 * @return retorna la variable fechaInicioCierreOrdenMedica 
	 * @author Angela Maria Aguirre 
	 */
	public String getFechaInicioCierreOrdenMedica() {
		return fechaInicioCierreOrdenMedica;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo fechaInicioCierreOrdenMedica
	
	 * @param valor para el atributo fechaInicioCierreOrdenMedica 
	 * @author Angela Maria Aguirre 
	 */
	public void setFechaInicioCierreOrdenMedica(String fechaInicioCierreOrdenMedica) {
		this.fechaInicioCierreOrdenMedica = fechaInicioCierreOrdenMedica;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo esquemaTariServiciosValorizarOrden
	
	 * @return retorna la variable esquemaTariServiciosValorizarOrden 
	 * @author Angela Maria Aguirre 
	 */
	public String getEsquemaTariServiciosValorizarOrden() {
		return esquemaTariServiciosValorizarOrden;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo esquemaTariServiciosValorizarOrden
	
	 * @param valor para el atributo esquemaTariServiciosValorizarOrden 
	 * @author Angela Maria Aguirre 
	 */
	public void setEsquemaTariServiciosValorizarOrden(String esquemaTariServiciosValorizarOrden) {
		this.esquemaTariServiciosValorizarOrden = esquemaTariServiciosValorizarOrden;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo solicitudCitaInterconsultaOdontoCitaProgramada
	
	 * @return retorna la variable solicitudCitaInterconsultaOdontoCitaProgramada 
	 * @author Angela Maria Aguirre 
	 */
	public String getSolicitudCitaInterconsultaOdontoCitaProgramada() {
		return solicitudCitaInterconsultaOdontoCitaProgramada;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo solicitudCitaInterconsultaOdontoCitaProgramada
	
	 * @param valor para el atributo solicitudCitaInterconsultaOdontoCitaProgramada 
	 * @author Angela Maria Aguirre 
	 */
	public void setSolicitudCitaInterconsultaOdontoCitaProgramada(
			String solicitudCitaInterconsultaOdontoCitaProgramada) {
		this.solicitudCitaInterconsultaOdontoCitaProgramada = solicitudCitaInterconsultaOdontoCitaProgramada;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo horaEjecProcesoInactivarUsuarioInacSistema
	
	 * @return retorna la variable horaEjecProcesoInactivarUsuarioInacSistema 
	 * @author Angela Maria Aguirre 
	 */
	public String getHoraEjecProcesoInactivarUsuarioInacSistema() {
		return horaEjecProcesoInactivarUsuarioInacSistema;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo horaEjecProcesoInactivarUsuarioInacSistema
	
	 * @param valor para el atributo horaEjecProcesoInactivarUsuarioInacSistema 
	 * @author Angela Maria Aguirre 
	 */
	public void setHoraEjecProcesoInactivarUsuarioInacSistema(
			String horaEjecProcesoInactivarUsuarioInacSistema) {
		this.horaEjecProcesoInactivarUsuarioInacSistema = horaEjecProcesoInactivarUsuarioInacSistema;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo horaEjeProcesoCaduContraInacSistema
	
	 * @return retorna la variable horaEjeProcesoCaduContraInacSistema 
	 * @author Angela Maria Aguirre 
	 */
	public String getHoraEjeProcesoCaduContraInacSistema() {
		return horaEjeProcesoCaduContraInacSistema;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo horaEjeProcesoCaduContraInacSistema
	
	 * @param valor para el atributo horaEjeProcesoCaduContraInacSistema 
	 * @author Angela Maria Aguirre 
	 */
	public void setHoraEjeProcesoCaduContraInacSistema(
			String horaEjeProcesoCaduContraInacSistema) {
		this.horaEjeProcesoCaduContraInacSistema = horaEjeProcesoCaduContraInacSistema;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo diasVigenciaContraUsuario
	
	 * @return retorna la variable diasVigenciaContraUsuario 
	 * @author Angela Maria Aguirre 
	 */
	public String getDiasVigenciaContraUsuario() {
		return diasVigenciaContraUsuario;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo diasVigenciaContraUsuario
	
	 * @param valor para el atributo diasVigenciaContraUsuario 
	 * @author Angela Maria Aguirre 
	 */
	public void setDiasVigenciaContraUsuario(String diasVigenciaContraUsuario) {
		this.diasVigenciaContraUsuario = diasVigenciaContraUsuario;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo diasFinalesVigenciaContraMostrarAlerta
	
	 * @return retorna la variable diasFinalesVigenciaContraMostrarAlerta 
	 * @author Angela Maria Aguirre 
	 */
	public String getDiasFinalesVigenciaContraMostrarAlerta() {
		return diasFinalesVigenciaContraMostrarAlerta;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo diasFinalesVigenciaContraMostrarAlerta
	
	 * @param valor para el atributo diasFinalesVigenciaContraMostrarAlerta 
	 * @author Angela Maria Aguirre 
	 */
	public void setDiasFinalesVigenciaContraMostrarAlerta(
			String diasFinalesVigenciaContraMostrarAlerta) {
		this.diasFinalesVigenciaContraMostrarAlerta = diasFinalesVigenciaContraMostrarAlerta;
	}

	public String getNumMaximoReclamacionesAccEventoXFactura() {
		return numMaximoReclamacionesAccEventoXFactura;
	}

	public void setNumMaximoReclamacionesAccEventoXFactura(
			String numMaximoReclamacionesAccEventoXFactura) {
		this.numMaximoReclamacionesAccEventoXFactura = numMaximoReclamacionesAccEventoXFactura;
	}

	public void setEsquemaTariMedicamentosValorizarOrden(
			String esquemaTariMedicamentosValorizarOrden) {
		this.esquemaTariMedicamentosValorizarOrden = esquemaTariMedicamentosValorizarOrden;
	}

	public String getEsquemaTariMedicamentosValorizarOrden() {
		return esquemaTariMedicamentosValorizarOrden;
	}

	
	public String getHacerRequeridoValorAbonoAplicadoAlFacturar() {
		return hacerRequeridoValorAbonoAplicadoAlFacturar;
	}

	public void setHacerRequeridoValorAbonoAplicadoAlFacturar(
			String hacerRequeridoValorAbonoAplicadoAlFacturar) {
		this.hacerRequeridoValorAbonoAplicadoAlFacturar = hacerRequeridoValorAbonoAplicadoAlFacturar;
	}

	public String getPermitirModificarDatosUsuariosCapitados() {
		return permitirModificarDatosUsuariosCapitados;
	}

	public void setPermitirModificarDatosUsuariosCapitados(
			String permitirModificarDatosUsuariosCapitados) {
		this.permitirModificarDatosUsuariosCapitados = permitirModificarDatosUsuariosCapitados;
	}

	public String getManejaHojaAnestesia() {
		return manejaHojaAnestesia;
	}

	public void setManejaHojaAnestesia(String manejaHojaAnestesia) {
		this.manejaHojaAnestesia = manejaHojaAnestesia;
	}

	public String getPermitirModificarDatosUsuariosCapitadosModificarCuenta() {
		return permitirModificarDatosUsuariosCapitadosModificarCuenta;
	}

	public void setPermitirModificarDatosUsuariosCapitadosModificarCuenta(
			String permitirModificarDatosUsuariosCapitadosModificarCuenta) {
		this.permitirModificarDatosUsuariosCapitadosModificarCuenta = permitirModificarDatosUsuariosCapitadosModificarCuenta;
	}


	/**
	 * @return valor de esquemaTarifarioAutocapitaSubCirugiasNoCurentos
	 */
	public String getEsquemaTarifarioAutocapitaSubCirugiasNoCurentos() {
		return esquemaTarifarioAutocapitaSubCirugiasNoCurentos;
	}


	/**
	 * @param esquemaTarifarioAutocapitaSubCirugiasNoCurentos el esquemaTarifarioAutocapitaSubCirugiasNoCurentos para asignar
	 */
	public void setEsquemaTarifarioAutocapitaSubCirugiasNoCurentos(
			String esquemaTarifarioAutocapitaSubCirugiasNoCurentos) {
		this.esquemaTarifarioAutocapitaSubCirugiasNoCurentos = esquemaTarifarioAutocapitaSubCirugiasNoCurentos;
	}


	/**
	 * @return valor de viaIngresoValidarNivelAutorizacionCapitacion
	 */
	public String getViaIngresoValidarNivelAutorizacionCapitacion() {
		return viaIngresoValidarNivelAutorizacionCapitacion;
	}


	/**
	 * @param viaIngresoValidarNivelAutorizacionCapitacion el viaIngresoValidarNivelAutorizacionCapitacion para asignar
	 */
	public void setViaIngresoValidarNivelAutorizacionCapitacion(
			String viaIngresoValidarNivelAutorizacionCapitacion) {
		this.viaIngresoValidarNivelAutorizacionCapitacion = viaIngresoValidarNivelAutorizacionCapitacion;
	}


	/**
	 * @return valor de validarDisponibilidadSaldoPresupuestoAutorizacionesCapotaAutomatica
	 */
	public String getValidarDisponibilidadSaldoPresupuestoAutorizacionesCapotaAutomatica() {
		return validarDisponibilidadSaldoPresupuestoAutorizacionesCapotaAutomatica;
	}


	/**
	 * @param validarDisponibilidadSaldoPresupuestoAutorizacionesCapotaAutomatica el validarDisponibilidadSaldoPresupuestoAutorizacionesCapotaAutomatica para asignar
	 */
	public void setValidarDisponibilidadSaldoPresupuestoAutorizacionesCapotaAutomatica(
			String validarDisponibilidadSaldoPresupuestoAutorizacionesCapotaAutomatica) {
		this.validarDisponibilidadSaldoPresupuestoAutorizacionesCapotaAutomatica = validarDisponibilidadSaldoPresupuestoAutorizacionesCapotaAutomatica;
	}


	/**
	 * @return valor de listaEsquemasTarifarios
	 */
	public ArrayList<EsquemasTarifarios> getListaEsquemasTarifarios() {
		return listaEsquemasTarifarios;
	}


	/**
	 * @param listaEsquemasTarifarios el listaEsquemasTarifarios para asignar
	 */
	public void setListaEsquemasTarifarios(ArrayList<EsquemasTarifarios> listaEsquemasTarifarios) {
		this.listaEsquemasTarifarios = listaEsquemasTarifarios;
	}


	/**
	 * @return valor de listaviasIngreso
	 */
	public ArrayList<ViasIngreso> getListaviasIngreso() {
		return listaviasIngreso;
	}
	//DTOEstanciaViaIngCentroCosto
	
	/**
	 * @param listaviasIngreso el listaviasIngreso para asignar
	 */
	public void setListaviasIngreso(ArrayList<ViasIngreso> listaviasIngreso) {
		this.listaviasIngreso = listaviasIngreso;
	}


	/**
	 * @return valor de listaTiposPaciente
	 */
	public ArrayList<TiposPaciente> getListaTiposPaciente() {
		return listaTiposPaciente;
	}


	/**
	 * @param listaTiposPaciente el listaTiposPaciente para asignar
	 */
	public void setListaTiposPaciente(ArrayList<TiposPaciente> listaTiposPaciente) {
		this.listaTiposPaciente = listaTiposPaciente;
	}


	/**
	 * @return valor de tipoPacienteViaIngresoValidarNivelAutorizacionCapitacion
	 */
	public String getTipoPacienteViaIngresoValidarNivelAutorizacionCapitacion() {
		return tipoPacienteViaIngresoValidarNivelAutorizacionCapitacion;
	}

	/**
	 * @param tipoPacienteViaIngresoValidarNivelAutorizacionCapitacion el tipoPacienteViaIngresoValidarNivelAutorizacionCapitacion para asignar
	 */
	public void setTipoPacienteViaIngresoValidarNivelAutorizacionCapitacion(
			String tipoPacienteViaIngresoValidarNivelAutorizacionCapitacion) {
		this.tipoPacienteViaIngresoValidarNivelAutorizacionCapitacion = tipoPacienteViaIngresoValidarNivelAutorizacionCapitacion;
	}


	/**
	 * @return the hacerRequeridaSeleccionCentroAtencionAsignadoSubirPacienteIndividual
	 */
	public String getHacerRequeridaSeleccionCentroAtencionAsignadoSubirPacienteIndividual() {
		return hacerRequeridaSeleccionCentroAtencionAsignadoSubirPacienteIndividual;
	}


	/**
	 * @param hacerRequeridaSeleccionCentroAtencionAsignadoSubirPacienteIndividual the hacerRequeridaSeleccionCentroAtencionAsignadoSubirPacienteIndividual to set
	 */
	public void setHacerRequeridaSeleccionCentroAtencionAsignadoSubirPacienteIndividual(
			String hacerRequeridaSeleccionCentroAtencionAsignadoSubirPacienteIndividual) {
		this.hacerRequeridaSeleccionCentroAtencionAsignadoSubirPacienteIndividual = hacerRequeridaSeleccionCentroAtencionAsignadoSubirPacienteIndividual;
	}


	public String getManejaConsecutivosNotasPacientesCentroAtencion() {
		return manejaConsecutivosNotasPacientesCentroAtencion;
	}


	public void setManejaConsecutivosNotasPacientesCentroAtencion(
			String manejaConsecutivosNotasPacientesCentroAtencion) {
		this.manejaConsecutivosNotasPacientesCentroAtencion = manejaConsecutivosNotasPacientesCentroAtencion;
	}


	public String getNaturalezaNotasPacientesManejar() {
		return naturalezaNotasPacientesManejar;
	}


	public void setNaturalezaNotasPacientesManejar(
			String naturalezaNotasPacientesManejar) {
		this.naturalezaNotasPacientesManejar = naturalezaNotasPacientesManejar;
	}
	
	/**
	 * @param permiteModificarManejaConsecutivosNotasPacienteCentroAtencion the permiteModificarManejaConsecutivosNotasPacienteCentroAtencion to set
	 */
	public void setPermiteModificarManejaConsecutivosNotasPacienteCentroAtencion(
			boolean permiteModificarManejaConsecutivosNotasPacienteCentroAtencion) {
		this.permiteModificarManejaConsecutivosNotasPacienteCentroAtencion = permiteModificarManejaConsecutivosNotasPacienteCentroAtencion;
	}

	/**
	 * @return the permiteModificarManejaConsecutivosNotasPacienteCentroAtencion
	 */
	public boolean isPermiteModificarManejaConsecutivosNotasPacienteCentroAtencion() {
		return Utilidades.existenNotasPaciente();
	}

	/**
	 * @param permiteModificarNaturalezaNotasPaciente the permiteModificarNaturalezaNotasPaciente to set
	 */
	public void setPermiteModificarNaturalezaNotasPaciente(
			boolean permiteModificarNaturalezaNotasPaciente) {
		this.permiteModificarNaturalezaNotasPaciente = permiteModificarNaturalezaNotasPaciente;
	}

	/**
	 * @return the permiteModificarNaturalezaNotasPaciente
	 */
	public boolean isPermiteModificarNaturalezaNotasPaciente() {
		return Utilidades.existenConceptosNotasPaciente();
	}

	/**
	 * @param permiteControlarAbonoPacientesPorNumeroIngreso the permiteControlarAbonoPacientesPorNumeroIngreso to set
	 */
	public void setPermiteControlarAbonoPacientesPorNumeroIngreso(
			boolean permiteControlarAbonoPacientesPorNumeroIngreso) {
		this.permiteControlarAbonoPacientesPorNumeroIngreso = permiteControlarAbonoPacientesPorNumeroIngreso;
	}

	/**
	 * @return the permiteControlarAbonoPacientesPorNumeroIngreso
	 */
	public boolean isPermiteControlarAbonoPacientesPorNumeroIngreso() {
		return Utilidades.existenMovimientosAbonos();
	}


	public ArrayList<Integer> getServiciosManejoTransPrimario() {
		return serviciosManejoTransPrimario;
	}


	public void setServiciosManejoTransPrimario(
			ArrayList<Integer> serviciosManejoTransPrimario) {
		this.serviciosManejoTransPrimario = serviciosManejoTransPrimario;
	}


	public ArrayList<Integer> getServiciosManejoTransSecundario() {
		return serviciosManejoTransSecundario;
	}


	public void setServiciosManejoTransSecundario(
			ArrayList<Integer> serviciosManejoTransSecundario) {
		this.serviciosManejoTransSecundario = serviciosManejoTransSecundario;
	}


	public String getManejoOxigenoFurips() {
		return manejoOxigenoFurips;
	}


	public void setManejoOxigenoFurips(String manejoOxigenoFurips) {
		this.manejoOxigenoFurips = manejoOxigenoFurips;
	}


	public String getServicioTemporal() {
		return servicioTemporal;
	}


	public void setServicioTemporal(String servicioTemporal) {
		this.servicioTemporal = servicioTemporal;
	}


	public int getIndiceServicioSeleccionado() {
		return indiceServicioSeleccionado;
	}


	public void setIndiceServicioSeleccionado(int indiceServicioSeleccionado) {
		this.indiceServicioSeleccionado = indiceServicioSeleccionado;
	}


	public String getServiciosSeleccionados() {
		return serviciosSeleccionados;
	}


	public void setServiciosSeleccionados(String serviciosSeleccionados) {
		this.serviciosSeleccionados = serviciosSeleccionados;
	}


	/**
	 * @return the permitirRecaudosCajaMayor
	 */
	public String getPermitirRecaudosCajaMayor() {
		return permitirRecaudosCajaMayor;
	}


	/**
	 * @param permitirRecaudosCajaMayor the permitirRecaudosCajaMayor to set
	 */
	public void setPermitirRecaudosCajaMayor(String permitirRecaudosCajaMayor) {
		this.permitirRecaudosCajaMayor = permitirRecaudosCajaMayor;
	}


	/**
	 * @return the viaIngresoValidacionesOrdenesAmbulatorias
	 */
	public String getViaIngresoValidacionesOrdenesAmbulatorias() {
		return viaIngresoValidacionesOrdenesAmbulatorias;
	}


	/**
	 * @param viaIngresoValidacionesOrdenesAmbulatorias the viaIngresoValidacionesOrdenesAmbulatorias to set
	 */
	public void setViaIngresoValidacionesOrdenesAmbulatorias(
			String viaIngresoValidacionesOrdenesAmbulatorias) {
		this.viaIngresoValidacionesOrdenesAmbulatorias = viaIngresoValidacionesOrdenesAmbulatorias;
	}


	/**
	 * @return the viaIngresoValidacionesPeticiones
	 */
	public String getViaIngresoValidacionesPeticiones() {
		return viaIngresoValidacionesPeticiones;
	}


	/**
	 * @param viaIngresoValidacionesPeticiones the viaIngresoValidacionesPeticiones to set
	 */
	public void setViaIngresoValidacionesPeticiones(
			String viaIngresoValidacionesPeticiones) {
		this.viaIngresoValidacionesPeticiones = viaIngresoValidacionesPeticiones;
	}


	/**
	 * @return the maximoDiasIngresosAConsultar
	 */
	public String getMaximoDiasIngresosAConsultar() {
		return maximoDiasIngresosAConsultar;
	}


	/**
	 * @param maximoDiasIngresosAConsultar the maximoDiasIngresosAConsultar to set
	 */
	public void setMaximoDiasIngresosAConsultar(String maximoDiasIngresosAConsultar) {
		this.maximoDiasIngresosAConsultar = maximoDiasIngresosAConsultar;
	}


	/**
	 * @return the diasMaxProrrogaAutorizacionArticulo
	 */
	public String getDiasMaxProrrogaAutorizacionArticulo() {
		return diasMaxProrrogaAutorizacionArticulo;
	}


	/**
	 * @param diasMaxProrrogaAutorizacionArticulo the diasMaxProrrogaAutorizacionArticulo to set
	 */
	public void setDiasMaxProrrogaAutorizacionArticulo(
			String diasMaxProrrogaAutorizacionArticulo) {
		this.diasMaxProrrogaAutorizacionArticulo = diasMaxProrrogaAutorizacionArticulo;
	}


	/**
	 * @return the diasMaxProrrogaAutorizacionServicio
	 */
	public String getDiasMaxProrrogaAutorizacionServicio() {
		return diasMaxProrrogaAutorizacionServicio;
	}


	/**
	 * @param diasMaxProrrogaAutorizacionServicio the diasMaxProrrogaAutorizacionServicio to set
	 */
	public void setDiasMaxProrrogaAutorizacionServicio(
			String diasMaxProrrogaAutorizacionServicio) {
		this.diasMaxProrrogaAutorizacionServicio = diasMaxProrrogaAutorizacionServicio;
	}
	
	/**
	 * @return the tipoPacienteValidacionesOrdenesAmbulatorias
	 */
	public String getTipoPacienteValidacionesOrdenesAmbulatorias() {
		return tipoPacienteValidacionesOrdenesAmbulatorias;
	}


	/**
	 * @param tipoPacienteValidacionesOrdenesAmbulatorias the tipoPacienteValidacionesOrdenesAmbulatorias to set
	 */
	public void setTipoPacienteValidacionesOrdenesAmbulatorias(
			String tipoPacienteValidacionesOrdenesAmbulatorias) {
		this.tipoPacienteValidacionesOrdenesAmbulatorias = tipoPacienteValidacionesOrdenesAmbulatorias;
	}


	/**
	 * @return the tipoPacienteValidacionesPeticiones
	 */
	public String getTipoPacienteValidacionesPeticiones() {
		return tipoPacienteValidacionesPeticiones;
	}


	/**
	 * @param tipoPacienteValidacionesPeticiones the tipoPacienteValidacionesPeticiones to set
	 */
	public void setTipoPacienteValidacionesPeticiones(
			String tipoPacienteValidacionesPeticiones) {
		this.tipoPacienteValidacionesPeticiones = tipoPacienteValidacionesPeticiones;
	}


	/**
	 * @return the mesesMaxAdminAutoCapVencidas
	 */
	public String getMesesMaxAdminAutoCapVencidas() {
		return mesesMaxAdminAutoCapVencidas;
	}


	/**
	 * @param mesesMaxAdminAutoCapVencidas the mesesMaxAdminAutoCapVencidas to set
	 */
	public void setMesesMaxAdminAutoCapVencidas(String mesesMaxAdminAutoCapVencidas) {
		this.mesesMaxAdminAutoCapVencidas = mesesMaxAdminAutoCapVencidas;
	}


	/**
	 * @return the desactivarEntidadSubPorCentroCosto
	 */
	public boolean isDesactivarEntidadSubPorCentroCosto() {
		return desactivarEntidadSubPorCentroCosto;
	}


	/**
	 * @param desactivarEntidadSubPorCentroCosto the desactivarEntidadSubPorCentroCosto to set
	 */
	public void setDesactivarEntidadSubPorCentroCosto(
			boolean desactivarEntidadSubPorCentroCosto) {
		this.desactivarEntidadSubPorCentroCosto = desactivarEntidadSubPorCentroCosto;
	}


}