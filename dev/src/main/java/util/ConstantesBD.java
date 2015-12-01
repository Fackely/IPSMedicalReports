/*
 * @(#)ConstantesBD.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_03
 *
 */
package util;

import java.math.BigDecimal;
import java.sql.ResultSet;


/**
 * Interfaz para el manejo de todas las constantes de la base de datos
 * 
 * @version 1.0, Noviembre 15, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero </a>
 */
public interface ConstantesBD
{ 
	
	public static final String nombreSistema="nombre_sistema";
	
	/**
	 * Nï¿½mero de caracteres mï¿½ximo de nï¿½mero de autorizaciï¿½n
	 */
	public static final int numCaracteresNumeroAutorizacion=15;
	
	/***************************************************************************
	 * CONSTANTES PARA MANEJO DE TRANSACCIONES
	 **************************************************************************/

	/**
	 * Cadena constante que establece que una transacciï¿½n debe empezar
	 */
	public static final String inicioTransaccion = "empezar";

	/**
	 * Cadena constante que establece que una transacciï¿½n debe continuar
	 */
	public static final String continuarTransaccion = "continuar";

	/**
	 * Cadena constante que establece que una transacciï¿½n debe finalizar
	 */
	public static final String finTransaccion = "finalizar";
	
	/**
	 * Entero con el numero de Excpecion Sql que arroja cuando se viola una llave foranea
	 */
	public static final int excepcionViolacionLlaveForanea = 23503;

	/**
	 * **************************** GENERO
	 * **********************************************
	 */

	/**
	 * Entero con la constante que reune todos los sexos
	 */
	public static final int codigoSexoTodos = 0;

	/**
	 * Entero con la constante para el sexo masculino
	 */
	public static final int codigoSexoMasculino = 1;

	/**
	 * Entero con la constante para el sexo masculino
	 */
	public static final int codigoSexoFemenino = 2;
	
	/**
	 * Entero con la constante para el sexo ambos
	 */
	public static final int codigoSexoAmbos = -2;

	
	/***********************************************************
	 * 
	 * 
	 * MOTIVOS SUSPENSION
	 * 
	 */
	
	public static final int CancelarPresupuestoOdontologico = 1;
	public static final int CancelarPlandeTratamiento = 2;
	public static final int CancelarProgramasServicio = 3;
	public static final int ExcluirProgramasServicio = 4;
	public static final int NoAutorizarExclusionProgramasServicio = 5;
	public static final int NoAutorizarInclusionProgramasServicio = 6;
	public static final int NoAutorizarGarantiaProgramasServicio = 7;
	public static final int SolicitarGarantia = 8;
	public static final int SuspenderTemporalmentePlandeTratamientoPresupuesto=9;

	
	/***************************************************************************
	 * TIPOS DE SOLICITUD
	 **************************************************************************/
	/**
	 * Cadena que defina que la solicitud es de tipo valoracion (consulta)
	 */
	public static final String solicitudValoracion = "solicitudConsulta";

	/**
	 * Cadena que defina que la solicitud es de tipo evolucion
	 */
	public static final String solicitudEvolucion = "solicitudEvolucion";

	/**
	 * Cadena que defina que la solicitud es de tipo farmacia
	 */
	public static final String solicitudFarmacia = "solicitudFarmacia";

	/**
	 * C&oacute;digo manejado por el tipo de solicitud para el caso de solicitud para
	 * valoraciï¿½n inicial de urgencias
	 */
	public static int codigoTipoSolicitudInicialUrgencias = 1;

	/**
	 * C&oacute;digo manejado por el tipo de solicitud para el caso de solicitud para
	 * valoraciï¿½n inicial de hospitalizaciï¿½n
	 */
	public static int codigoTipoSolicitudInicialHospitalizacion = 2;

	/**
	 * C&oacute;digo manejado por el tipo de solicitud para el caso de solicitud para
	 * valoraciï¿½n en cita
	 */
	public static int codigoTipoSolicitudCita = 3;

	/**
	 * C&oacute;digo manejado por el tipo de solicitud para el caso de solicitud para
	 * interconsulta
	 */
	public static int codigoTipoSolicitudInterconsulta = 4;

	/**
	 * C&oacute;digo manejado por el tipo de solicitud para el caso de solicitud para
	 * procedimiento
	 */
	public static int codigoTipoSolicitudProcedimiento = 5;

	/**
	 * C&oacute;digo manejado por el tipo de solicitud para el caso de solicitud para
	 * farmacia
	 */
	public static int codigoTipoSolicitudMedicamentos = 6;

	/**
	 * C&oacute;digo manejado por el tipo de solicitud para el caso de solicitud para
	 * evoluciï¿½n
	 */
	public static int codigoTipoSolicitudEvolucion = 7;


	/**
	 * C&oacute;digo manejado por el tipo de solicitud para el caso de solicitud de
	 * cargos directos, para articulos
	 */
	public static int codigoTipoSolicitudCargosDirectosArticulos  = 9;


	/**
	 * C&oacute;digo manejado por el tipo de solicitud para el caso de solicitud de
	 * cargos directos, para servicios
	 */
	public static int codigoTipoSolicitudCargosDirectosServicios  = 12;
	
	/**
	 * C&oacute;digo manejado por el tipo de solicitud para el caso de solicitud de
	 * estancia automatica
	 */
	public static int codigoTipoSolicitudEstancia  = 13;
	
	/**
	 * C&oacute;digo manejado por el tipo de solicitud para el caso de solicitud de
	 * cirugï¿½a ambulatoria
	 */
	public static int codigoTipoSolicitudCirugia  = 14;
	
	
	/**
	 * C&oacute;digo manejado por el tipo de solicitud para el caso de solicitud de paquetes.
	 * 
	 */
	public static int codigoTipoSolicitudPaquetes  = 15;

	/***************************************************************************
	 * TIPOS DE REGIMEN
	 **************************************************************************/
	/**
	 * C&oacute;digo del tipo de regimen particular
	 */
	public static char codigoTipoRegimenParticular = 'P';
	/**
	 * C&oacute;digo del tipo de regimen Contributivo
	 */
	public static char codigoTipoRegimenContributivo = 'C';
	/**
	 * C&oacute;digo del tipo de regimen Vinculado
	 */
	public static char codigoTipoRegimenVinculado = 'V';
	/**
	 * C&oacute;digo del tipo de regimen Subsidiado
	 */
	public static char codigoTipoRegimenSubsidiado = 'S';
	/**
	 * C&oacute;digo del tipo de regimen Otro
	 */
	public static char codigoTipoRegimenOtro = 'O';

	/***************************************************************************
	 * VIAS DE INGRESO
	 **************************************************************************/
	/**
	 * C&oacute;digo de la vï¿½a de ingreso de hospitalizacion
	 */
	public static int codigoViaIngresoHospitalizacion = 1;
	
	/**
	 * C&oacute;digo de la vï¿½a de ingreso de hospitalizacion
	 */
	public static int codigoViaIngresoAmbulatorios = 2;

	/**
	 * C&oacute;digo de la vï¿½a de ingreso de hospitalizacion
	 */
	public static int codigoViaIngresoUrgencias = 3;

	/**
	 * C&oacute;digo de la vï¿½a de ingreso de consulta externa
	 */
	public static int codigoViaIngresoConsultaExterna = 4;

	/***************************************************************************
	 * CODIGOS TIPO TARIFARIOS
	 **************************************************************************/
	/**
	 * C&oacute;digo en la fuente de datos del tarifario Cups
	 */
	public static int codigoTarifarioCups = 0;

	/**
	 * C&oacute;digo en la fuente de datos del tarifario ISS
	 */
	public static int codigoTarifarioISS = 1;

	/**
	 * C&oacute;digo en la fuente de datos del tarifario Soat
	 */
	public static int codigoTarifarioSoat = 2;
	
	/**
	 * C&oacute;digo en la fuente de datos del tarifario Sonria
	 */
	public static int codigoTarifarioSonria = 4;

	/***************************************************************************
	 * ESTADOS SOLICITUD HISTORIA CLINICA
	 **************************************************************************/
	/**
	 * C&oacute;digo en la fuente de datos del estado de historia clinica "Solicitada"
	 */
	public static int codigoEstadoHCSolicitada = 1;

	/**
	 * C&oacute;digo en la fuente de datos del estado de historia clinica "Respondida"
	 */
	public static int codigoEstadoHCRespondida = 2;

	/**
	 * C&oacute;digo en la fuente de datos del estado de historia clinica
	 * "Interpretada"
	 */
	public static int codigoEstadoHCInterpretada = 3;

	/**
	 * C&oacute;digo en la fuente de datos del estado de historia clinica "Anulada"
	 */
	public static int codigoEstadoHCAnulada = 4;

	/**
	 * C&oacute;digo en la fuente de datos del estado de historia clinica "Despachada"
	 */
	public static int codigoEstadoHCDespachada = 5;

	/**
	 * C&oacute;digo en la fuente de datos del estado de historia clinica
	 * "Administrada"
	 */
	public static int codigoEstadoHCAdministrada = 6;

	/**
	 * C&oacute;digo en la fuente de datos del estado de historia clinica "Cargo
	 * Directo"
	 */
	public static int codigoEstadoHCCargoDirecto = 7;
	
	/**
	 * C&oacute;digo en la fuente de datos del estado de historia clinica 
	 * "Toma de Muestra"
	 */
	public static int codigoEstadoHCTomaDeMuestra = 8;
	
	/**
	 * C&oacute;digo en la fuente de datos del estado de historia clinica 
	 * "en Proceso"
	 */
	public static int codigoEstadoHCEnProceso = 9;

	/**
	 * C&oacute;digo en la fuente de datos del estado de historia clinica 
	 * "Nueva Muestra"
	 */
	public static int codigoEstadoHCNuevaMuestra = 10;

	/***************************************************************************
	 * TIPOS DE PERSONAS
	 **************************************************************************/
	
	/**
	 * tipo persona mï¿½dico
	 */
	public static String tipoPersonaMedico="Medico";
	
	/**
	 * tipo persona usuario
	 */
	public static String tipoPersonaUsuario="Usuario";
	
	/**
	 * tipo persona paciente
	 */
	public static String tipoPersonaPaciente="Paciente";

	/**
	 * Tipo de persona general, solamente estï¿½ registrada en la tabla de personas
	 */
	public static String tipoPersonaGeneral="Persona";

	
	/***************************************************************************
	 * ESTADOS SOLICITUD FACTURACIï¿½N
	 **************************************************************************/
	/**
	 * C&oacute;digo en la fuente de datos del estado de facturacion "Pendiente"
	 */
	public static int codigoEstadoFPendiente = 1;

	/**
	 * C&oacute;digo en la fuente de datos del estado de facturacion "Anulada"
	 */
	public static int codigoEstadoFAnulada = 2;

	/**
	 * C&oacute;digo en la fuente de datos del estado de facturacion "Cargada"
	 */
	public static int codigoEstadoFCargada = 3;

	/**
	 * C&oacute;digo en la fuente de datos del estado de facturacion "Inactiva"
	 */
	public static int codigoEstadoFInactiva = 4;

	/**
	 * C&oacute;digo en la fuente de datos del estado de facturacion "Exento"
	 */
	public static int codigoEstadoFExento = 5;

	/**
	 * C&oacute;digo en la fuente de datos del estado de facturacion "Externo"
	 */
	public static int codigoEstadoFExterno = 6;
	

	

	/***************************************************************************
	 * CODIGOS ESTADOS CUENTA
	 **************************************************************************/
	/**
	 * C&oacute;digo en la fuente de datos del estado de cuenta "Activa"
	 */
	public static int codigoEstadoCuentaActiva = 0;

	/**
	 * C&oacute;digo en la fuente de datos del estado de cuenta "Facturada"
	 */
	public static int codigoEstadoCuentaFacturada = 1;

	

	/**
	 * C&oacute;digo en la fuente de datos del estado de cuenta "Asociada"
	 */
	public static int codigoEstadoCuentaAsociada = 3;

	/**
	 * C&oacute;digo en la fuente de datos del estado de cuenta "Cerrada"
	 */
	public static int codigoEstadoCuentaCerrada = 4;

	/**
	 * C&oacute;digo en la fuente de datos del estado de cuenta "Excenta"
	 */
	public static int codigoEstadoCuentaExcenta = 5;

	/**
	 * C&oacute;digo en la fuente de datos del estado de cuenta "facturada parcial"
	 */
	public static int codigoEstadoCuentaFacturadaParcial = 6;


	/**
	 * C&oacute;digo en la fuente de datos del estado de cuenta "Cuenta En Proceso Facturacion"
	 */
	public static int codigoEstadoCuentaProcesoFacturacion = 10;
	
	/**
	 * C&oacute;digo en la fuente de datos del estado de cuenta "Cuenta En Proceso Distribucion"
	 */
	public static int codigoEstadoCuentaProcesoDistribucion = 11;

	/***************************************************************************
	 * CODIGOS ESTADOS ADMISION
	 **************************************************************************/
	/**
	 * C&oacute;digo en la fuente de datos del estado de admisiï¿½n "Hospitalizado"
	 */
	public static int codigoEstadoAdmisionHospitalizado = 1;

	/***************************************************************************
	 * CODIGOS ESTADOS PEDIDOS
	 **************************************************************************/
	/**
	 * Codigo de la funte de datos para el estado de SOLICITADO del pedido
	 */
	public static int codigoEstadoPedidoSolicitado = 1;

	/**
	 * Codigo de la funte de datos para el estado de DESPACHADO del pedido
	 */
	public static int codigoEstadoPedidoDespachado = 2;

	/**
	 * Codigo de la funte de datos para el estado de TERMINADO del pedido
	 */
	public static int codigoEstadoPedidoTerminado = 3;

	/**
	 * Codigo de la funte de datos para el estado de ANULADO del pedido
	 */
	public static int codigoEstadoPedidoAnulado = 4;

	/***************************************************************************
	 * CODIGOS ESTADOS DEVOLUCIONES
	 **************************************************************************/
	/**
	 * Codigo de la funte de datos para el estado de GENERADA de la devolucion
	 *  
	 */
	public static int codigoEstadoDevolucionGenerada = 1;

	/**
	 * Codigo de la funte de datos para el estado RECIBIDA de la devolucion
	 *  
	 */
	public static int codigoEstadoDevolucionRecibida = 2;

	/***************************************************************************
	 * CODIGOS TIPOS DEVOLUCION
	 **************************************************************************/
	/**
	 * Codigo de la fuente de datos para el tipo de devoluciï¿½n Automï¿½tica
	 */
	public static int codigoTipoDevolucionAutomatica = 1;

	/**
	 * Codigo de la fuente de datos para el tipo de devoluciï¿½n Manual
	 */
	public static int codigoTipoDevolucionManual = 2;

	/***************************************************************************
	 * CODIGOS TIPOS SERVICIO (SOLICITUDES)
	 **************************************************************************/

	/**
	 * C&oacute;digo en la fuente de datos del tipo de servicio Procedimiento
	 */
	public static char codigoServicioProcedimiento = 'P';

	/**
	 * C&oacute;digo en la fuente de datos del tipo de servicio Cama/Estancias
	 */
	public static char codigoServicioCamaEstancias = 'E';

	/**
	 * C&oacute;digo en la fuente de datos del tipo de servicio Cargos Consulta Externa
	 */
	public static char codigoServicioCargosConsultaExterna = 'C';

	/**
	 * C&oacute;digo en la fuente de datos del tipo de servicio No Cruentos
	 */
	public static char codigoServicioNoCruentos = 'D';

	/**
	 * C&oacute;digo en la fuente de datos del tipo de servicio Quirurgico
	 */
	public static char codigoServicioQuirurgico = 'Q';

	/**
	 * C&oacute;digo en la fuente de datos del tipo de servicio Partos/Cesarea
	 */
	public static char codigoServicioPartosCesarea = 'R';

	/**
	 * C&oacute;digo en la fuente de datos del tipo de servicio Paquetes
	 */
	public static char codigoServicioPaquetes = 'K';

	/**
	 * C&oacute;digo en la fuente de datos del tipo de servicio Interconsulta (Se uniï¿½
	 * la interconsulta con la consulta, pero para evitar fallos, se dejo esta
	 * constante con el mismo valor de la interconsulta)
	 */
	public static char codigoServicioInterconsulta = 'C';

	/**
	 * Para no tener problemas en las bï¿½squedas con restricciones se mantiene el
	 * cod anteriro que se manejaba como de tipo intercon.
	 */
	public static char codigoServicioAntiguoInterconsulta = 'I';

	/**
	 * C&oacute;digo en la fuente de datos del tipo de servicio Honorarios Cirugia
	 */
	public static char codigoServicioHonorariosCirugia = 'H';

	/**
	 * C&oacute;digo en la fuente de datos del tipo de servicio Ayudantes Cirugia
	 */
	public static char codigoServicioAyudantesCirugia = 'Y';

	/**
	 * C&oacute;digo en la fuente de datos del tipo de servicio Anestesia Cirugia
	 */
	public static char codigoServicioAnestesiaCirugia = 'A';

	/**
	 * Descripon en la fuente de datos del tipo de servicio Anestesia Cirugia
	 * 
	 * MT 7947, se pide que no se pueda consultar los profesionales que sean tipo honorario anestesia.
	 * 
	 * No es la solucion mas conveniente ya que la tabla es parametrica y la descripcion puede tener cualquier valor
	 */
	public static String nombreHonorarioServicioAnestesiaCirugia = "ANESTESIA";

	/**
	 * C&oacute;digo en la fuente de datos del tipo de servicio Materiales Cirugia
	 */
	public static char codigoServicioMaterialesCirugia = 'M';

	/**
	 * C&oacute;digo en la fuente de datos del tipo de servicio Sala Cirugia
	 */
	public static char codigoServicioSalaCirugia = 'S';
	
	/**
	 * C&oacute;digo en la fuente de datos del tipo de servicio DYT
	 */
	public static char codigoServicioDyT= 'T';

	/**
	 * C&oacute;digo en la fuente de datos del tipo de articulo Medicamentos
	 */
	public static char codigoArticuloMedicamento = 'W';

	/**
	 * C&oacute;digo que se utiliza cuando se especifica que el servicio no estï¿½
	 * definido
	 */
	public static int codigoServicioNoDefinido = 0;	

	/**
	 * C&oacute;digo en la fuente de datos del tipo de articulo Medicamentos
	 */
	public static char codigoServicioFalso = ' ';
	
	/**
	 * Codigo en la Fuente de Datos del tipo de servicio Otro
	 */
	public static char codigoServicioOtros = 'O';
	/***************************************************************************
	 * CODIGOS SOLICITUDES (Ver codigos Servicios para tipos particulares)
	 **************************************************************************/

	/**
	 * C&oacute;digo de "Impresion Diagnostica" en la base de datos en la tabla
	 * tipos_diagnostico que se va a postular en la pï¿½gina de ingreso de la
	 * respuesta de interconsulta
	 */
	public static int codigoAPostularTipoDiagnosticoPpal = 1;

	/**
	 * C&oacute;digo de "Sin Recargo" en la base de datos en la tabla tipos_recargo que
	 * se va a postular en la pï¿½gina de ingreso de la respuesta de interconsulta
	 */
	public static int codigoTipoRecargoSinRecargo = 5;

	/**
	 * tipo de contrato a postular en la funcionalidad de ingreso del registro
	 * del contrato, es un valor de la tabla "tipos_contrato" que corresponde a
	 * "Normal"
	 */
	public static int tipoContratoAPostular = 2;

	/**
	 * String con el nombre del flag centinela en la BD (Este flag sirve para
	 * saber si debo mostrar el "desea grabar informaciï¿½n" al hacer click en
	 * otro link
	 */
	public static String nombreFlagCentinelaEnBD = "flag_centinela";

	/***************************************************************************
	 * CODIGOS SOLICITUDES INTERCONSULTA
	 **************************************************************************/
	/**
	 * C&oacute;digo de las opciones de manejo para interconsulta donde se transfiere
	 * el manejo del paciente
	 */
	public static int codigoSeTransfiereManejoPaciente = 1;

	/**
	 * C&oacute;digo de las opciones de manejo para interconsulta donde se desea un
	 * concepto
	 */
	public static int codigoSeDeseaConceptoSolamente = 2;

	/**
	 * C&oacute;digo de las opciones de manejo para interconsulta donde se pide un
	 * manejo conjunto
	 */
	public static int codigoManejoConjunto = 3;

	/***************************************************************************
	 * CODIGOS CENTROS_COSTO
	 **************************************************************************/

	/**
	 * C&oacute;digo del centro de costo No Seleccionado
	 */
	public static int codigoCentroCostoNoSeleccionado = -1;

	/**
	 * C&oacute;digo en la fuente de datos del centro de costo urgencias (Usado en la
	 * solicitud)
	 */
	public static int codigoCentroCostoUrgencias = 3;

	/**
	 * C&oacute;digo en la fuente de datos del centro de costo consulta externa (Usado
	 * en la solicitud)
	 */
	public static int codigoCentroCostoConsultaExterna = 4;

	/**
	 * C&oacute;digo en la fuente de datos del centro de costo farmacia (Usado
	 * en la solicitud)
	 */
	public static int codigoCentroCostoFarmacia = 7;
	
	/**
	 * C&oacute;digo del centro de costo Externo (Muy usado en Solicitudes)
	 */
	public static int codigoCentroCostoExternos = 11;

	/**
	 * C&oacute;digo del centro de costo Todos (Muy usado en Solicitudes)
	 */
	public static int codigoCentroCostoTodos = 0;

	/**
	 * C&oacute;digo con el que por defecto se maneja la opciï¿½n "otros" en la
	 * aplicaciï¿½n
	 */
	public static int codigoOtros = 0;

	/**
	 * C&oacute;digo en la fuente de datos del centro de costo Hospitalizacion
	 */
	public static int codigoCentroCostoHospitalizacion = 8;
	
	/**
	 * C&oacute;digo del centro de costo ambulatorios
	 */
	public static int codigoCentroCostoAmbulatorios = 18;

	/***************************************************************************
	 * CODIGOS OCUPACIONES MEDICAS
	 **************************************************************************/
	/**
	 * C&oacute;digo de la ocupaciï¿½n mï¿½dica Ninguna,  para el manejo
	 * de solicitudes por parte de usuarios No medicos, es decir sin
	 * ocupacionMedica (func cargos directos)
	 */
	public static int codigoOcupacionMedicaNinguna = -1;

	/**
	 * C&oacute;digo de la ocupaciï¿½n mï¿½dica Todos (Muy usada en Solicitudes)
	 */
	public static int codigoOcupacionMedicaTodos = 0;

	/**
	 * C&oacute;digo de la ocupaciï¿½n mï¿½dica Enfermera (Usada en Cuadros de Turnos)
	 */
	public static int codigoOcupacionMedicaEnfermera = 9;
	
	
	/**
	 * 
	 */
	public static int codigoOcupacionOdontologo=26;
	
	/**
	 * 
	 */
	public static int codigoOcupacionAuxiliarOdontologia=28;

	/***************************************************************************
	 * CODIGOS ESPECIALIDADES MEDICAS
	 **************************************************************************/
	/**
	 * C&oacute;digo de la especialidad mï¿½dica Ninguna, para el manejo
	 * de solicitudes por parte de usuarios No medicos, es decir sin
	 * especialidad (func cargos directos)
	 */
	public static int codigoEspecialidadMedicaNinguna = -1;
	
	/**
	 * C&oacute;digo de la especialidad mï¿½dica Todos (Muy usada en Solicitudes)
	 */
	public static int codigoEspecialidadMedicaTodos = 0;

	/**
	 * C&oacute;digo de la especialidad mÃ¯Â¿Â½dica Todas usada en la funcionalidad 
	 * Capitacion Mantenimiento Niveles de autorizacion por servicios/articulos
	 */
	public static int codigoEspecialidadMedicaTodas = 1048;

	/***************************************************************************
	 * CODIGOS ESPECIALIDADES VALORACION
	 **************************************************************************/

	/**
	 * C&oacute;digo de la especialidad de valoraciï¿½n "general"
	 */
	public static int codigoEspecialidadValoracionGeneral = 0;

	/**
	 * C&oacute;digo de la especialidad de valoraciï¿½n "urgencias"
	 */
	public static int codigoEspecialidadValoracionUrgencias = 1;

	/**
	 * C&oacute;digo de la especialidad de valoraciï¿½n "hospitalizaciï¿½n"
	 */
	public static int codigoEspecialidadValoracionHospitalizacion = 2;

	/**
	 * C&oacute;digo de la especialidad de valoraciï¿½n "pediatrica"
	 */
	public static int codigoEspecialidadValoracionPediatrica = 3;

	/**
	 * C&oacute;digo de la especialidad de valoraciï¿½n "pediatrica desarrollo por
	 * conductas"
	 */
	public static int codigoEspecialidadValoracionPediatricaDC = 4;

	/**
	 * C&oacute;digo de la especialidad de valoraciï¿½n gineco obstetrica
	 */
	public static int codigoEspecialidadValoracionGinecoObstetrica = 5;

	/**
	 * C&oacute;digo de la especialidad de valoraciï¿½n odontologï¿½a
	 */
	public static int codigoEspecialidadValoracionOdontologia = 6;

	/**
	 * C&oacute;digo de la especialidad de valoraciï¿½n anestesiologï¿½a
	 */
	public static int codigoEspecialidadValoracionAnestesiologia = 7;

	/**
	 * C&oacute;digo de la especialidad de valoraciï¿½n enfermeria
	 */
	public static int codigoEspecialidadValoracionEnfermeria = 8;

	/***************************************************************************
	 * CODIGOS CITAS
	 **************************************************************************/
	/** Constante de definiciï¿½n de estado de Cita A Programar */
	public static final int codigoEstadoCitaAProgramar = 0;

	/** Constante de definiciï¿½n de estado de Cita Reservada */
	public static final int codigoEstadoCitaReservada = 1;

	/** Constante de definiciï¿½n de estado de Cita Asignada */
	public static final int codigoEstadoCitaAsignada = 2;

	/** Constante de definiciï¿½n de estado de Cita Reprogramada */
	public static final int codigoEstadoCitaReprogramada = 3;

	/** Constante de definiciï¿½n de estado de Cita A reprogramar */
	public static final int codigoEstadoCitaAReprogramar = 4;

	/** Constante de definiciï¿½n de estado de Cita Cancelada por el Paciente */
	public static final int codigoEstadoCitaCanceladaPaciente = 5;

	/** Constante de definiciï¿½n de estado de Cita Cancelada por la Instituciï¿½n */
	public static final int codigoEstadoCitaCanceladaInstitucion = 6;

	/** Constante de definiciï¿½n de estado de Cita Cumplida */
	public static final int codigoEstadoCitaAtendida = 7;

	/** Constante de definiciï¿½n de estado de Cita No Cumplida */
	public static final int codigoEstadoCitaNoCumplida = 8;
	
	/** Constante de definiciï¿½n de estado de Cita No Atencion */
	public static final int codigoEstadoCitaNoAtencion = 9;


	/** Constante de definiciï¿½n de estado de Liquidaciï¿½n Sin Liquidar */
	public static final String codigoEstadoLiquidacionSinLiquidar = "S";

	/** Constante de definiciï¿½n de estado de Liquidaciï¿½n Liquidada */
	public static final String codigoEstadoLiquidacionLiquidada = "L";

	/**
	 * Texto que aparece en las citas cuya agenda ha sido cancelada
	 */
	public static final String textoCitaAgendaCancelada = "Agenda Cancelada";

	/***************************************************************************
	 * CODIGOS FORMATOS RESPUESTA CONSULTA
	 **************************************************************************/
	/**
	 * C&oacute;digo de la respuesta de consulta general
	 */
	public static final int codigoRespuestaGeneral = 1;

	/**
	 * C&oacute;digo de la respuesta de consulta pediatrica
	 */
	public static final int codigoRespuestaPediatrica = 2;

	/**
	 * C&oacute;digo de la respuesta de consulta pediatrica
	 */
	public static final int codigoRespuestaGinecoObstetrica = 3;

	/**
	 * C&oacute;digo de la respuesta de consulta odontologia
	 */
	public static final int codigoRespuestaOdontologia = 4;

	/**
	 * C&oacute;digo de la respuesta de consulta odontologia
	 */
	public static final int codigoRespuestaOftalmologia = 5;

	/***************************************************************************
	 * CODIGOS CONDUCTAS A SEGUIR VAL. URGENCIAS
	 **************************************************************************/

	/**
	 * C&oacute;digo de la conducta a seguir "Sala de Espera"
	 */
	public static final int codigoConductaSeguirSalaEspera = 1;

	/**
	 * C&oacute;digo de la conducta a seguir "Sala de Cirugï¿½a Ambulatoria"
	 */
	public static final int codigoConductaSeguirSalaCirugiaAmbulatoria = 2;

	/**
	 * C&oacute;digo de la conducta a seguir "Cama de observaciï¿½n"
	 */
	public static final int codigoConductaSeguirCamaObservacion = 3;

	/**
	 * C&oacute;digo de la conducta a seguir "Interconsulta"
	 */
	public static final int codigoConductaSeguirInterconsulta = 4;

	/**
	 * C&oacute;digo de la conducta a seguir "Hospitalizar en piso"
	 */
	public static final int codigoConductaSeguirHospitalizarPiso = 5;

	/**
	 * C&oacute;digo de la conducta a seguir "Remitir a Instituciï¿½n de mayor
	 * complejidad"
	 */
	public static final int codigoConductaSeguirRemitirMayorComplejidad = 6;

	/**
	 * C&oacute;digo de la conducta a seguir "Salida pacientes sin observaciï¿½n"
	 */
	public static final int codigoConductaSeguirSalidaSinObservacion = 7;
	
	/**
	 * C&oacute;digo de la conducta a seguir "Sala de Reanimacion"
	 */
	public static final int codigoConductaSeguirSalaReanimacion = 8;
	
	/**
	 * C&oacute;digo de la conducta a seguir "Traslado Cuidado Especial"
	 */
	public static final int codigoConductaSeguirTrasladoCuidadoEspecial = 9;
	
	
	/**
	 * C&oacute;digo del destino a la salida "Dado de Alta"
	 */
	public static final int codigoDestinoSalidaDadoDeAlta = 1;
	
	/**
	 * C&oacute;digo del destino a la salida "Remitido Otro Nivel Complejidad"
	 */
	public static final int codigoDestinoSalidaRemitidoOtroNivelComplejidad = 2;
	
	/**
	 * C&oacute;digo del destino a la salida voluntaria
	 */
	public static final int codigoDestinoSalidaVoluntaria = 3;
	
	/**
	 * C&oacute;digo del destino a la salida "Hospitalizaciï¿½n"
	 */
	public static final int codigoDestinoSalidaHospitalizacion = 4;	
	
	/**
	 * C&oacute;digo del destino a la Salida Cirugia Ambulatoria
	 */
	public static final int codigoDestinoSalidaCirugiaAmbulatoria = 5;
	
	/**
	 * Codigo del destina a la salida Hospitalizar
	 * */
	public static final int codigoDestinoSalidaHospitalizar = 6;
	
	
	/**
	 * C&oacute;digo del destino a la salida "Otro"
	 */
	public static final int codigoDestinoSalidaOtro = 0;
	
	/**
	 * C&oacute;digo del destino a la salida "trasladocuidadoEspecial"
	 */
	public static final int codigoDestinoSalidaTrasladoCuidadoEspecial = 7;

	/***************************************************************************
	 * CODIGOS ORIGEN ADMISIï¿½N HOSPITALARIA
	 **************************************************************************/

	/**
	 * C&oacute;digo del origen de admisiï¿½n hospitalaria llamado "urgencias"
	 */
	public static final int codigoOrigenAdmisionHospitalariaEsUrgencias = 1;
	
	/**
	 * C&oacute;digo del origen de admisiï¿½n hospitalaria llamado "consulta externa"
	 */
	public static final int codigoOrigenAdmisionHospitalariaEsConsultaExterna = 2;
	
	/**
	 * C&oacute;digo del origen de admisiï¿½n hospitalaria llamado "remitido"
	 */
	public static final int codigoOrigenAdmisionHospitalariaEsRemitido = 3;
	
	/**
	 * C&oacute;digo del origen de admisiï¿½n hospitalaria llamado "nacido en institucion"
	 */
	public static final int codigoOrigenAdmisionHospitalariaEsNacidoInstitucion = 4;
	
	/***************************************************************************
	 * CODIGOS ORIGEN ADMISIï¿½N URGENCIAS
	 **************************************************************************/

	/**
	 * C&oacute;digo del origen de admisiï¿½n urgencias llamado "urgencias"
	 */
	public static final int codigoOrigenAdmisionUrgenciasEsUrgencias = 1;
	
	/**
	 * C&oacute;digo del origen de admisiï¿½n urgencias llamado "remitido"
	 */
	public static final int codigoOrigenAdmisionUrgenciasEsRemitido = 2;

	/***************************************************************************
	 * CODIGOS TIPOS LIQUIDACION SOAT
	 **************************************************************************/

	/**
	 * Cï¿½d del tipo de Liquidaciï¿½n invï¿½lido
	 */
	public static final int codigoTipoLiquidacionInvalido = 0;

	/**
	 * C&oacute;digo del tipo de Liquidaciï¿½n soat "Liquidacion por unidades
	 */
	public static final int codigoTipoLiquidacionSoatUnidades = 1;

	/**
	 * C&oacute;digo del tipo de Liquidaciï¿½n soat "Liquidacion por valor
	 */
	public static final int codigoTipoLiquidacionSoatValor = 2;
	
	/**
	 * C&oacute;digo del tipo de Liquidaciï¿½n soat "Liquidacion por Porcentaje
	 */
	public static final int codigoTipoLiquidacionSoatPorcentaje = 3;
	
	/**
	 * C&oacute;digo del tipo de Liquidaciï¿½n soat "Liquidacion por nueva tarifa
	 */
	public static final int codigoTipoLiquidacionSoatNuevaTarifa = 4;

	/**
	 * C&oacute;digo del tipo de Liquidaciï¿½n soat "Grupo
	 */
	public static final int codigoTipoLiquidacionSoatGrupo = 5;
	
	/**
	 * C&oacute;digo del tipo de Liquidaciï¿½n soat "Uvr
	 */
	public static final int codigoTipoLiquidacionSoatUvr = 6;
	

	/**
	 * C&oacute;digo del tipo de Liquidaciï¿½n valor unidades
	 */
	public static final int codigoTipoLiquidacionValorUnidades = 7;
	
	/************************************************************************
	 * C&oacute;digos tipos detalle de formas de pago (Tesoreria)
	 ************************************************************************/
	public static final int codigoTipoDetalleFormasPagoNinguno=1;
	public static final int codigoTipoDetalleFormasPagoCheque=2;
	public static final int codigoTipoDetalleFormasPagoTarjeta=3;
	public static final int codigoTipoDetalleFormasPagoPagare=4;
	public static final int codigoTipoDetalleFormasPagoLetra=5;
	public static final int codigoTipoDetalleFormasPagoBono=6;
	/***************************************************************************
	 * CODIGOS TIPOS CIERRE (CARTERA)
	 **************************************************************************/
	
	/**
	 * C&oacute;digo String del tipo de cierre anual
	 */
	public static final String codigoTipoCierreAnualStr = "A";
	/**
	 * C&oacute;digo String del tipo de cierre mensual
	 */
	public static final String codigoTipoCierreMensualStr = "M";
	/**
	 * C&oacute;digo String del tipo de cierre de saldos iniciales
	 */
	public static final String codigoTipoCierreSaldoInicialStr = "S";
	
	/**
	 * C&oacute;digo del tipo de cierre inventario anual
	 */
	public static final int codigoTipoCierreInventarioAnualStr = 1;
	
	/**
	 * C&oacute;digo del tipo de cierre inventario inicial
	 */
	public static final int codigoTipoCierreInventarioSaldoInicialStr = 2;
	
	/**
	 * C&oacute;digo del tipo de cierre inventario mensual
	 */
	public static final int codigoTipoCierreInventarioMensualStr = 3;
	
	/**
	 * valor del mes de cierre anual
	 */
	public static final int valorMesCierreAnualInt=13;
	
	/**
	 * MT-5571 Texto que aparece en las solicitudes de consulta de hospitalización
	 * generadas automáticamente en caso de asocio de cuenta
	 */
	public static final String textoInterpretacionAutomatica = "INTERPRETACION AUTOMATICA POR ASOCIO DE CUENTAS";

	/**
	 * Fecha en la cual se empezarï¿½n las bï¿½squedas cuyo fecha inicial sea
	 * requerida, pero se desee que el sistema asigne automï¿½ticamente una fecha
	 * inicial
	 */
	public static final String fechaMinimaBusquedaFormatoAp = "01/01/1980";

	/**
	 * Fecha en la cual se empezarï¿½n las bï¿½squedas cuyo fecha final sea
	 * requerida, pero se desee que el sistema asigne automï¿½ticamente una fecha
	 * final
	 */
	public static final String fechaMaximaBusquedaFormatoAp = "01/01/2150";
	
	
	
	/***************************************************************************
	 * ACRONIMO INDICADORES DE CALIDAD 
	 **************************************************************************/
	/**
	 * Oportunidad de la asignacion de citas en la consulta medica general
	 */
	public static String acronimoOportunidadAsignacioncitasConsultaMedicaGeneral = "I.1.1.0";
	
	/**
	 * Oportunidad de la asignacion de citas en la consulta medica interna
	 */
	public static String acronimoOportunidadAsignacioncitasConsultaMedicaInterna = "I.1.2.1";
	/**
	 * Oportunidad de la asignacion de citas en la consulta ginecobstetricia
	 */
	public static String acronimoOportunidadAsignacioncitasConsultaGinecobstetricia = "I.1.2.2";
	/**
	 * Oportunidad de la asignacion de citas en la consulta pediatria
	 */
	public static String acronimoOportunidadAsignacioncitasConsultaPediatria = "I.1.2.3";
	/**
	 * Oportunidad de la asignacion de citas en la consulta cirugia general
	 */
	public static String acronimoOportunidadAsignacioncitasConsultaCirugiaGeneral = "I.1.2.4";
	/**
	 * Proporcion de cancelacion de cirugia programada
	 */
	public static String acronimoProporcionCancelacionCirugiaProgramada = "I.1.3.0";
	/**
	 * Oportunidad en la atencion en la consulta urgencias
	 */
	public static String acronimoOportunidadAtencionConsultaUrgencias = "I.1.4.0";
	/**
	 * Oportunidad en la atencion en servicios de imagenologia
	 */
	public static String acronimoOportunidadAtencionServicioImagenologia = "I.1.5.0";
	/**
	 * Oportunidad en la atencion en la consulta odontologia general
	 */
	public static String acronimoOportunidadAtencionConsultaOdontologiaGeneral = "I.1.6.0";
	/**
	 * Oportunidad en la realizacion de cirugia programada
	 */
	public static String acronimoOportunidadRealizacionCirugiaProgramada = "I.1.7.0";
	/**
	 * Tasa de reingreso de pacientes hospitalizados
	 */
	public static String acronimoTasaReingresoPacientesHospitalizados = "I.2.1.0";
	/**
	 * Proporcion de pacientes con hipertension arterial controlada
	 */
	public static String acronimoProporcionPacientesHipertensionArterialControlada = "I.2.2.0";
	/**
	 * Tasa mortaliad intrahospitalaria despues de 48 horas
	 */
	public static String acronimoTasaMortalidadIntrahospitalaria = "I.3.1.0";
	/**
	 * Tasa de infeccion intrahospitalaria
	 */
	public static String acronimoTasaInfeccionIntrahospitalaria = "I.3.2.0";
	/**
	 * Proporcion de vigilancia de eventos adversos
	 */
	public static String acronimoProporcionVigilanciaEventosAdversos = "I.3.3.0";
	/**
	 * Tasa satisfaccion Global
	 */
	public static String acronimoTasaSatisfaccionGlobal = "I.4.1.0";
	
	/***************************************************************************
	 *  FIN ACRONIMO INDICADORES DE CALIDAD
	 ***************************************************************************/
	
	/***************************************************************************
	 * NOMBRES DE LOS LOGS TIPO ARCHIVO *
	 **************************************************************************/

	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de facturaciï¿½n
	 */
	public static final String logFolderModuloFacturacion = "facturacion";
	
	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de facturaciï¿½n
	 */
	public static final String logFolderModuloOdontologia = "Odontologia";
	

	/**
	 * Floder para manejar el modulo de cuadro de Turnos
	 */
	public static final String logFolderModuloCuadroTurnos = "cuadroTurnos";
	
	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de facturaciï¿½n
	 */
	public static final String logFolderModuloDistribucionCuenta = "distribucionCuenta";

	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de facturaciï¿½n
	 */
	public static final String logFolderModuloPooles = "pooles";

	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de Insumos y
	 * Medicamentos
	 */
	public static final String logFolderModuloInsumosMedicamentos = "insumosMedicamentos";

	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de Consulta Externa
	 */
	public static final String logFolderModuloConsultaExterna = "consultaExterna";

	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de Historia Clï¿½nica
	 */
	public static final String logFolderModuloHistoriaClinica = "historiaClinica";
	
	/**
	 * 
	 */
	public static final String logFolderModuloPYP = "PYP";

	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de Manejo del Paciente
	 */
	public static final String logFolderModuloManejoPaciente = "manejoPaciente";

	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de Mantenimiento y
	 * Administraciï¿½n
	 */
	public static final String logFolderModuloMantenimientoAdministracion = "mantenimientoAdministracion";

	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de Ordenes Mï¿½dicas
	 */
	public static final String logFolderModuloOrdenesMedicas = "ordenes";
	
	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de Salas de Cirugia
	 */
	public static final String logFolderModuloSalasCirugia = "salasCirugia";
	
	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de Salas de Cirugia
	 */
	public static final String logFolderModuloTesoreria = "tesoreria";

	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de Salas de Cirugia
	 */
	public static final String logFolderModuloEnfermeria = "enfermeria";
	
	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de Salas de Cirugia
	 */
	public static final String logFolderModuloInterfaz = "interfaz";
	
	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de Capitacion
	 */
	public static final String logFolderModuloCapitacion = "capitacion";
	
	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de Agenda de Procedimiento
	 * */
	public static final String logFolderModuloAgendaProcedimiento = "agendaProcedimiento";

	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de Ordenes Mï¿½dicas
	 */
	public static final String logFolderCoberturaAccidentes = "coberturaAccidentesTransito";
	
	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de Cartera
	 */
	public static final String logFolderCartera = "Cartera";

	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de Hoja Obtetrica
	 */
	public static final String logFolderHojaObstetrica = "HojaObstetrica";	
	
	/**
	 * Folder en el cual se guardarï¿½ el Back Up de la Base de Datos
	 */
	public static final String logFolderBackUpBaseDatos = "BackUpBaseDatos";
	
	/**
	 * Folder en el cual se guardarï¿½ el log de matenimiento de Tablas en el modulo de facturacion
	 */
	public static final String logFolderMantenimientoTablas= "MantenimientoTablas";
	
	/**
	 * Folder en el cual se guardarï¿½ el log de coberturas.
	 */
	public static final String logFolderCoberturas= "Coberturas";
	
	/**
	 * Folder en el cual se guardarï¿½ el log de matenimiento de Tablas en el modulo de facturacion
	 */
	public static final String logFolderPaquetes= "Paquetes";
	
	/**
	 * Folder en el cual se guardarï¿½ el log de Liquidaciï¿½n en el modulo de Salas Cirugï¿½a
	 */
	public static final String logFolderLiquidacion= "Liquidacion";
	
	/**
	 * Folder en el cual se guardarï¿½ el log de Peticiones en el modulo de Salas Cirugï¿½a
	 */
	public static final String logFolderPeticiones= "Peticiones";
	
	/**
	 * Folder en el cual se guardarï¿½ el log de Solicitudes Cx en el modulo de Salas Cirugï¿½a
	 */
	public static final String logFolderSolicitudesCx= "Solicitudes_Cx";
	
	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de inventarios
	 */
	public static final String logFolderModuloInventario = "inventarios";
	/**
	 * Folder en el cual se guardarï¿½ el log de mantenimiento en el modulo de inventario
	 */
	public static final String logFolderMantenimientoInventario= "Mantenimiento";
	/**
	 * Folder en el cual se guardarï¿½ el log de Inventario Fisico en el modulo de inventario
	 */
	public static final String logFolderInventarioFisicoInventario= "inventarioFisico";
	/**
	 * Folder en el cual se guardarï¿½ el log de Cierre en el modulo de inventario
	 */
	public static final String logFolderCierreInventario= "Cierre";
	/**
	 * Folder en el cual se guardarï¿½ el log de transacciones de inventarios
	 */
	public static final String logFolderTransaccionesInventario= "Transacciones";
	/**
	 * Folder en el cual se guardarï¿½ el log de informaciï¿½n de la poliza
	 */
	public static final String logFolderInformacionPoliza= "InformacionPoliza";	
	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de Mantenimiento de Manejo del Paciente
	 */
	public static final String logFolderMantenimientoManejoPaciente = "Mantenimiento";	
	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de Pedidos
	 */
	public static final String logFolderPedidos = "Pedidos";
	/**
	 * Folder en el cual se guardarï¿½ el log de trasalados inventario
	 */
	public static final String logFolderTrasladosInventario= "Traslados";
	/**
	 * Folder en el cual se guardarï¿½ el log de trasalados inventario
	 */
	public static final String logFolderCargosDirectos= "CargosDirectos";
	
	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de Administraciï¿½n solamente
	 */
	public static final String logFolderAdministracion = "Administracion";

	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades del mï¿½dulo de Capitaciï¿½n
	 */
	public static final String logFolderSubirPacientes = "subirPacientes";
	
	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades del Registro de enfermeria
	 */
	public static final String logFolderRegistroEnfermeria= "registroEnfermeria";
	
	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades del Registro de enfermeria
	 */
	public static final String logFolderInterfazFacturacion= "facturacion";
	
	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de Responder Cirugï¿½as
	 */
	public static final String logFolderResponderCirugias= "responderCirugia";
	
	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de mantenimiento de capitacion
	 */
	public static final String logFolderMantenimientoCapitacion= "mantenimiento";
	
	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de inventarios (interfaz)
	 */
	public static final String logFolderInventarios= "inventarios";
	
	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de Agenda de Procedimiento 
	 * */
	public static final String logFolderMantenimientoAgendaProcedimiento = "mantenimiento";
	
	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de Cartera Paciente
	 */
	public static final String logFolderModuloCarteraPaciente= "carteraPaciente";
	
	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de Documentos en Garantia
	 */
	public static final String logFolderDocumentosGarantia= "documentosGarantia";
	
	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de Facturas Varias
	 */
	public static final String logFolderModuloFacturasVarias= "facturasVarias";
	
	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de listado ingresos - actualizacion autorizaciones
	 */
	public static final String logFolderModuloListadoIngresos= "manejoPaciente";
	
	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de EQUIVALENTES DE INVENTARIO
	 */
	public static final String logFolderModuloEquivalentesDeInventario= "inventarios";

	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de SERVICIOS ASOCIOS
	 */
	public static final String logFolderModuloServiciosAsocios = "salas";

	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de GLOSAS
	 */
	public static final String logFolderModuloGlosas = "glosas";
	
	/****************************************SIES***************************/
	
	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de Categoria
	 */
	public static final String logFolderCategoria= "categoria";
	
	
	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de Categoria
	 */
	public static final String logFolderNovedad= "novedad";
	
	
	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de Vacaciones
	 */
	public static final String logFolderVacaciones= "vacaciones";
	
	
	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de Enfermeras Asignadas a una Categoria
	 */
	public static final String logFolderEnfermeraCategoria= "enfermeraCategoria";
	
	/*******************FIN SIES ************************************/
	
	
	/*********************************************************************************************
	 * 	CONSTANTES PARA EL MANEJO DE METODOS DE JDBC
	 * *********************************************************************************************/
	public static final int typeResultSet=ResultSet.TYPE_SCROLL_SENSITIVE;
	public static final int concurrencyResultSet=ResultSet.CONCUR_UPDATABLE;
	

	
	
	
	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades RIPS
	 */
	public static final String logFolderRIPS= "RIPS";
	
	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de Mantenimiento
	 */
	public static final String logFolderMantenimiento= "mantenimiento";
	
	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de actualizacion autorizaciones
	 */
	public static final String logFolderActualizacionAutorizaciones= "actualizacionAutorizaciones";
	
	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de equivalentes de inventario
	 */
	public static final String logFolderEquivalentesDeInventario= "equivalentesDeInventario";
	
	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de servicios asocios
	 */
	public static final String logFolderServiciosAsocios= "serviciosAsocios";
	
	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de Archivos Planos
	 */
	public static final String logFolderArchivosPlanos= "archivosPlanos";
	
	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de gneracion modificacion ajustes facturas varias
	 */
	public static final String logFolderAjustesFacturasVarias= "ajustesFacturasVarias";
	
	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de gneracion modificacion ajustes facturas varias
	 */
	public static final String logFolderPagosFacturasVarias = "pagosFacturasVarias";
	
	/**
	 * Folder en el cual se guardarï¿½n las funcionalidades de gneracion modificacion de campos parametrizables
	 */
	public static final String logFolderCamposParametrizables="camposParametrizables";
	
	/**
	 * Folder en el cual se guardar los logs de los reportes.
	 */
	public static final String logFolderReportes = "reportes";
	
	/**
	 * Folder para guardar los logs del submï¿½dulo Registro Glosas
	 */
	public static String logRegistroGlosas = "registroGlosas";

	/**
	 * Folder para guardar los logs del submï¿½dulo Consultar/ImprimirGlosas
	 */
	public static String logConsultarImprimirGlosas = "logConsultarImprimirGlosas";
	
	/**
	 * Folder para guardar los logs del submï¿½dulo Mantenimiento en Facturaciï¿½n
	 */
	public static String logMantenimientoFacturacion = "logMantenimientoFacturacion";
	
	/**
	 * FOLDER PARA GUARDAR LOG. ES EL SUBMODULO MANTENIMIENTO TESORERIA
	 */
	public static String logMantenimientoTesoreria = "logMantenimientoTesoreria";
	/**
	 * Folder para guardar los logs del submï¿½dulo Mantenimiento en Facturaciï¿½n
	 */
	public static String logAdministracionOdontologia = "logAdministracionOdontologia";
	
	
	/**
	 * Folder para guardar los logs del submï¿½dulo Servicios en Mantenimiento, submï¿½dulo de Facturaciï¿½n
	 */
	public static String logServicios = "logServicios";
	
	/**
	 * Folder para guardar la modificaciï¿½n de Glosas Auditadas  
	 */
	public static String logFolderModificacionInsercionGlosa = "logFolderModificacionInsercionGlosa";
	
	/**
	 * Folder para guardar los log de auditorias glosas  
	 */
	public static String logAuditoriasGlosas = "auditoriaGlosas";
	
	
	
	/**
	 * Codigos Ubicacion Logo en los Reportes
	 * Izquierda = 0
	 * Derecha = 2
	 */
	public static final int codigoUbicacionLogoIzq = 0;
	public static final int codigoUbicacionLogoDer = 2;
	
	
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * Contratos
	 */
	public static final String logContratoNombre = "logContrato";

	/**
	 * C&oacute;digo del log de Contratos
	 */
	public static final int logContratoCodigo = 1;

	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * Empresas
	 */
	public static final String logEmpresaNombre = "logEmpresa";

	/**
	 * C&oacute;digo del log de Contratos
	 */
	public static final int logEmpresaCodigo = 2;

	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * Convenios
	 */
	public static final String logConvenioNombre = "logConvenio";

	/**
	 * C&oacute;digo del log de Convenios
	 */
	public static final int logConvenioCodigo = 3;

	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * Asocio de Cuenta
	 */
	public static final String logAsocioCuentasNombre = "logAsocioCuentas";

	/**
	 * C&oacute;digo del log de Asocio de Cuenta
	 */
	public static final int logAsocioCuentasCodigo = 4;

	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * Tarifas
	 */
	public static final String logTarifasNombre = "logTarifas";

	/**
	 * C&oacute;digo del log de tarifas
	 */
	public static final int logTarifasCodigo = 5;

	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * Excepciones de Tarifas
	 */
	public static final String logExcepcionesTarifasNombre = "logExcepcionesTarifas";

	/**
	 * C&oacute;digo del log de Excepciones de Tarifas
	 */
	public static final int logExcepcionesTarifasCodigo = 6;

	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * Recargos de Tarifas
	 */
	public static final String logRecargosTarifasNombre = "logRecargosTarifas";

	/**
	 * C&oacute;digo del log de Recargos de Tarifas
	 */
	public static final int logRecargosTarifasCodigo = 7;

	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * terceros
	 */
	public static final String logTerceroNombre = "logTercero";

	/**
	 * C&oacute;digo del log de Terceros
	 */
	public static final int logTerceroCodigo = 8;

	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * ClasificacionSocioEconomica
	 */
	public static final String logClasificacionSocioEconomicaNombre = "logClasificacionSocioEconomica";

	/**
	 * C&oacute;digo del log de Clasificaciï¿½n socioEconï¿½mica
	 */
	public static final int logClasificacionSocioEconomicaCodigo = 9;

	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * TopesFacturacion
	 */
	public static final String logTopesFacturacionNombre = "logTopesFacturacion";

	/**
	 * C&oacute;digo del log de TopesFacturacion
	 */
	public static final int logTopesFacturacionCodigo = 10;

	/**
	 * C&oacute;digo del log de TopesFacturacion
	 */
	public static final int logMontosCobroCodigo = 11;

	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * TopesFacturacion
	 */
	public static final String logMontosCobroNombre = "logMontosCobro";

	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * Excepciones de Servicios
	 */
	public static final String logExcepcionesServiciosNombre = "logExcepcionesServicios";

	/**
	 * C&oacute;digo del log de ExcepcionesServicios
	 */
	public static final int logExcepcionesServiciosCodigo = 12;

	/**
	 * Nombre para el log de pagos por paciente
	 */
	public static final String logPagosPacienteNombre = "logPagosPaciente";

	/**
	 * C&oacute;digo para el log de pagos por paciente
	 */
	public static final int logPagosPacienteCodigo = 13;

	/**
	 * Nombre para el log de vigencia de diagnï¿½sticos CIE
	 */
	public static final String logCIENombre = "logCIE";

	/**
	 * C&oacute;digo para el log de vigencia de diagnï¿½sticos CIE
	 */
	public static final int logCIECodigo = 14;

	/**
	 * Nombre para el log de Registro diagnosticos
	 */
	public static final String logRegistroDiagnosticoNombre = "logRegistroDiagnosticos";

	/**
	 * C&oacute;digo para el log de registro de diagnosticos.
	 */
	public static final int logRegistroDiagnosticoCodigo = 15;

	/***************************************************************************
	 * Modulo Parametrizacion
	 **************************************************************************/
	public static String logModificacionUnidadNombre = "logUnidadesConsulta";

	public static final int logModificacionUnidadCodigo = 16;

	/**
	 * C&oacute;digo del log de ExcepcionesNaturalezaPaciente
	 */
	public static final int logRegistroExcepcionesNaturalezaPaciente = 17;

	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * Excepciones Naturaleza Paciente
	 */
	public static final String logRegistroExcepcionesNaturalezaPacienteNombre = "logRegistroExcepcionesNaturalezaPaciente";

	/**
	 * C&oacute;digo de log de SustitutosInventarios
	 */
	public static final int logSustitutosInventario = 18;

	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los log
	 * Sustitutos Inventarios.
	 */
	public static final String logSustitutosInventarioNombre = "logSustitutosInventario";

	/**
	 * C&oacute;digo de log de TarifasInventarios
	 */
	public static final int logTarifasInventario = 19;

	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los log
	 * Tarifas Inventarios.
	 */
	public static final String logTarifasInventarioNombre = "logTarifasInventario";

	/**
	 * C&oacute;digo de log de PedidosInsumos
	 */
	public static final int logPedidosInsumos = 20;

	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los log
	 * Pedidos Insumos.
	 */
	public static final String logPedidosInsumosNombre = "logModificarAnularPedidos";

	/**
	 * Nombre para el log de Requisitos Paciente
	 */
	public static final String logRequisitosPacienteNombre = "logRequisitosPaciente";

	/**
	 * C&oacute;digo para el log de requisitos paciente
	 */
	public static final int logRequisitosPacienteCodigo = 21;

	/**
	 * Nombre para el log de Requisitos Paciente
	 */
	public static final String logRequisitosPacienteConvenioNombre = "logRequisitosPacienteConvenio";

	/**
	 * C&oacute;digo para el log de requisitos paciente
	 */
	public static final int logRequisitosPacienteConvenioCodigo = 22;

	/**
	 * Nombre para el log de Paramatrizaciï¿½n de instituciï¿½n
	 */
	public static final String logParamInstitucionNombre = "logParametrizacionInstitucion";

	/**
	 * C&oacute;digo para el log de parametrizaciï¿½n de instituciï¿½n
	 */
	public static final int logParamInstitucionCodigo = 23;
	
	/**
	 * Nombre para el log de Excepciones de Farmacia por convenio
	 */
	public static final String logExcepcionesFarmaciaConvenioNombre = "logExcepcionesFarmaciaConvenio";

	/**
	 * C&oacute;digo para el log de Excepciones de Farmacia por convenio
	 */
	public static final int logExcepcionesFarmaciaConvenioCodigo = 24;
	
	/**
	 * Nombre para el log de Participaciï¿½n pool X Tarifas
	 */
	public static final String logParticipacionPoolXTarifasNombre = "logParticipacionPoolXTarifas";

	/**
	 * C&oacute;digo para el log de Participaciï¿½n pool X Tarifas
	 */
	public static final int logParticipacionPoolXTarifasCodigo = 25;
	
	
	
	
	/**
	 * Nombre para el log de MedicosXPool
	 */
	public static final String logMedicosXPoolNombre = "logMedicosXPool";

	/**
	 * C&oacute;digo para el log de MedicosXPool
	 */
	public static final int logMedicosXPoolCodigo = 26;
	
	/**
	 * Nombre para el log de MedicosXPool
	 */
	public static final String logDistribucionCuentaNombre = "logDistribucionCuenta";

	/**
	 * C&oacute;digo para el log de distribuciï¿½n de la cuenta
	 */
	public static final int logDistribucionCuentaCodigo = 27;
	
	/**
	 * Nombre para el log de generaciï¿½n de excepciones farmacia
	 */
	public static final String logGeneracionExcepcionesFarmaciaNombre = "logGeneracionExcepcionesFarmacia";

	/**
	 * C&oacute;digo para el log de generaciï¿½n de excepciones farmacia
	 */
	public static final int logGeneracionExcepcionesFarmaciaCodigo = 28;
	
	/**
	 * C&oacute;digo para el log de cobertura de accidentes de transito
	 */
	public static final String logCoberturaAccidentesTransitoNombre = "logCoberturaAccidentesTransito";
	
	/**
	 * Nombre para el log de cobertura de accidentes de transito
	 */
	public static final int logCoberturaAccidentesTransitoCodigo = 29;
	
	/**
	 * Nombre para el log de Registro Pooles
	 */
	public static final String logRegistroPoolesNombre = "logRegistroPooles";

	/**
	 * C&oacute;digo para el log de Registro Pooles
	 */
	public static final int logRegistroPoolesCodigo = 30;
	
	/**
	 * Nombre para el log de Registro Pooles
	 */
	public static final String logSolicitudMedimcanetosNombre = "logSolicitudMedicamentos";

	/**
	 * C&oacute;digo para el log de Registro Pooles
	 */
	public static final int logSolicitudMedimcanetosCodigo = 31;
	
	/***************************************************************************
	 * Modulo Facturaciï¿½n
	 **************************************************************************/
	/**
	 * C&oacute;digo para el log de Abonos y Descuentos
	 */
	public static final int logAbonosYDescuentosCodigo = 32;
	
	/**
	 * Nombre para el log de Abonos y Descuentos
	 */
	public static final String logAbonosYDescuentosNombre = "logAbonosDescuentos";
	
	/**
	 * Nombre para el log de Revisiï¿½n Cuenta
	 */
	public static final String logRevisionCuentaNombre = "logRevisionCuenta";
	
	/**
	 * Nombre para el log de Revisiï¿½n Cuenta
	 */
	public static final String logRevisionCuentaRequisitosNombre = "logRequisitosPaciente";
	
	/**
	 * C&oacute;digo para el log de Revisiï¿½n Cuenta
	 */
	public static final int logRevisionCuentaRequisitosCodigo = 33;
	/**
	 * Nombre para el log de PoolXSolicitud en Revisiï¿½n Cuenta
	 */
	public static final String logRevisionCuentaPoolNombre = "logIngresoPool";
	/**
	 * C&oacute;digo para el log de PoolXSolicitud en Revisiï¿½n Cuenta
	 */
	public static final int logRevisionCuentaPoolCodigo = 34;
	/**
	 * Nombre para el log de Nï¿½mero de Autorizacion en Revisiï¿½n Cuenta
	 */
	public static final String logNumeroAutorizacionNombre = "logNumeroAutorizacion";
	/**
	 * C&oacute;digo para el log de Nï¿½mero de Autorizaciï¿½n en Revisiï¿½n Cuenta
	 */
	public static final int logNumeroAutorizacionCodigo = 35;
	
	/**
	 * Nombre para el log de Nï¿½mero de Autorizacion en Revisiï¿½n Cuenta
	 */
	public static final String logComentarioProcedimientoNombre = "logProcedimientos";
	/**
	 * C&oacute;digo para el log de Nï¿½mero de Autorizaciï¿½n en Revisiï¿½n Cuenta
	 */
	public static final int logComentarioProcedimientoCodigo = 36;
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * Esquemas Tarifarios
	 */
	public static final String logEsquemasTarifariosNombre = "logEsquemasTarifarios";

	/**
	 * C&oacute;digo del log de Esquemas Tarifarios
	 */
	public static final int logEsquemasTarifariosCodigo = 37;

	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * Esquemas Tarifarios
	 */
	public static final String logParametrosGeneralesNombre = "logParametrosGenerales";

	/**
	 * C&oacute;digo del log de Esquemas Tarifarios
	 */
	public static final int logParametrosGeneralesCodigo = 38;

	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * Salario Minimo
	 */
	public static final String logSalarioMinimoNombre = "logSalarioMinimo";

	/**
	 * C&oacute;digo del log de Salario Minimo
	 */
	public static final int logSalarioMinimoCodigo = 39;
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * Modificaciï¿½n de Cuenta
	 */
	public static final String logModificarCuentaNombre = "logModificarCuenta";

	/**
	 * C&oacute;digo del log de Modificaciï¿½n de Cuenta
	 */
	public static final int logModificarCuentaCodigo = 40;
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * Cuentas cobro
	 */
	public static final String logCuentasCobroNombre = "logCuentasCobro";

	/**
	 * C&oacute;digo del log de Cuentas cobro
	 */
	public static final int logCuentasCobroCodigo = 41;
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * Esquemas Tarifarios
	 */
	public static final String logArticuloNombre = "logArticulo";
	
	/**
	 * C&oacute;digo del log de Articulos
	 */
	public static final int logArticuloCodigo = 42;
	/**
	 * C&oacute;digo para el log de motivos de anulacion de facturas
	 */
	public static final int logMotivoAnulacionFacturasCodigo =43;
	
	/** Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 *Motivo Anulacion de facturas
	 */
	public static final String logMotivoAnulacionFacturasNombre = "logMotivoAnulacionFacturas";
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * Modificar Servicio
	 */
	public static final String logServicioNombre = "logServicio";

	/**
	 * C&oacute;digo del log de Modificar servicio
	 */
	public static final int logServicioCodigo = 44;

	/**
	 * Nombre para el log de Requisitos Radicacion
	 */
	public static final String logRequisitosRadicacionNombre = "logRequisitosRadicacion";

	/**
	 * C&oacute;digo para el log de requisitos radicacion
	 */
	public static final int logRequisitosRadicacionCodigo = 45;
	
	/**
	 * Nombre para el log de Requisitos Radicacion Convenio
	 */
	public static final String logRequisitosRadicacionConvenioNombre = "logRequisitosRadicacionConvenio";

	/**
	 * C&oacute;digo para el log de requisitos Radicacion
	 */
	public static final int logRequisitosRadicacionConvenioCodigo = 46;
		
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * Conceptos Pago Cartera
	 */
	public static final String logConceptosPagoCarteraNombre = "logConceptosPagoCartera";

	/**
	 * C&oacute;digo del log de Conceptos Pago Cartera
	 */
	public static final int logConceptosPagoCarteraCodigo = 47;
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * Conceptos Ajustes Cartera
	 */
	public static final String logConceptosAjustesCarteraNombre = "logConceptosAjustesCartera";

	/**
	 * C&oacute;digo del log de Conceptos Ajustes Cartera
	 */
	public static final int logConceptosAjustesCarteraCodigo = 48;
	
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * Actualizacion Autorizacion
	 */
	public static final String logActualizacionAutorizacionNombre = "logActualizacionAutorizacion";

	/**
	 * C&oacute;digo del log de Actualizacion Autorizacion
	 */
	public static final int logActualizacionAutorizacionCodigo = 49;
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * Conceptos Glosas
	 */
	public static final String logConceptosGlosasNombre = "logConceptosGlosas";

	/**
	 * C&oacute;digo del log de Conceptos Pago Cartera
	 */
	public static final int logConceptosGlosasCodigo = 50;
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * Concepto Castigos
	 */
	public static final String logConceptoCastigoNombre = "logConceptosCastigo";

	/**
	 * C&oacute;digo del log de Concepto Castigo 
	 */
	public static final int logConceptoCastigoCodigo = 51;
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * Concepto Respuesta Glosas 
	 */
	public static final String logConceptosRespuestasGlosasNombre = "logConceptosRespuestasGlosas";

	/**
	 * C&oacute;digo del log de Concepto Respuesta Glosas 
	 */
	public static final int logConceptosRespuestasGlosasCodigo = 52;
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * generaciï¿½n de RIPS
	 */
	public static final String logRipsNombre = "logRIPS";

	/**
	 * C&oacute;digo del log de Concepto Respuesta Glosas 
	 */
	public static final int logRipsCodigo = 53;

	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * concepto ingreso tesoreria
	 */
	public static final String logTesoreriaNombre = "logConceptoTesoreria";

	/**
	 * C&oacute;digo del log de Concepto ingreso Tesoreria 
	 */
	public static final int logTesoreriaCodigo = 54;

	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * Hoja Obstetrica
	 */
	public static final String logHojaObstetricaNombre = "hojaObstetrica";

	/**
	 * C&oacute;digo del log de Hoja Obstetrica 
	 */
	public static final int logHojaObstetricaCodigo = 55;
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * Registro saldo inicial
	 */
	public static final String logRegistroSaldoInicialNombre = "logRegistroSaldoInicial";

	/**
	 * C&oacute;digo del log de Registro de Saldo Inicial
	 */
	public static final int logRegistroSaldoInicialCodigo = 56;
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * consecutivos disponibles
	 */
	public static final String logConsecutivosDisponiblesNombre = "logConsecutivosDisponibles";

	/**
	 * C&oacute;digo del log de consecutivos disponibles
	 */
	public static final int logConsecutivosDisponiblesCodigo = 57;
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar el BackUp de la Base de Datos en Postgres
	 */
	public static final String logBackUpBaseDatosPostgresNombre = "BackUpPostgres";

	/**
	 * C&oacute;digo del log del Back Up de la Base de Datos de Postgres
	 */
	public static final int logBackUpBaseDatosPostgresCodigo = 58;
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar el BackUp de la Base de Datos en Oracle
	 */
	public static final String logBackUpBaseDatosOracleNombre = "BackUpOracle";

	/**
	 * C&oacute;digo del log del Back Up de la Base de Datos de Oracle
	 */
	public static final int logBackUpBaseDatosOracleCodigo = 59;
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * Cierres Mensuales
	 */
	public static final String logCierreCarteraNombre = "logCierreCartera";

	/**
	 * C&oacute;digo del log de Cierres Mensuales
	 */
	public static final int logCierreCarteraCodigo = 60;
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * Tipo Monitoreo
	 */
	public static final String logTiposMonitoreoNombre = "logTiposDeMonitoreo";

	/**
	 * C&oacute;digo del log de Tipos de Monitoreo
	 */
	public static final int logTiposMonitoreoCodigo = 61;
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * Tipos de Salas
	 */
	public static final String logTipoSalasNombre = "logTiposSalas";

	/**
	 * C&oacute;digo del log de Tipos de Salas
	 */
	public static final int logTipoSalasCodigo = 62;
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * Salas
	 */
	public static final String logSalasNombre = "logSalas";

	/**
	 * C&oacute;digo del log de Salas
	 */
	public static final int logSalasCodigo = 63;
	
	/**
	 * Nombre para el log de grupos quirurgicos
	 */
	public static final String logGruposNombre = "logGrupos";

	/**
	 * C&oacute;digo para el log de grupos quirurgicos
	 */
	public static final int logGruposCodigo = 64;
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * Porcentajes Cirugï¿½as Mï¿½ltiples
	 */
	public static final String logPorcentajesCxMultiplesNombre = "logPorcentajesCxMultiples";

	/**
	 * C&oacute;digo del log de Porcentajes Cirugï¿½as Mï¿½ltiples
	 */
	public static final int logPorcentajesCxMultiplesCodigo = 65;
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * Cajas
	 */
	public static final String logCajasNombre = "logCajas";

	/**
	 * C&oacute;digo del log de Cajas
	 */
	public static final int logCajasCodigo = 66;
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * CajasCajeros
	 */
	public static final String logCajasCajerosNombre = "logCajasCajeros";

	/**
	 * C&oacute;digo del log de CajasCajeros
	 */
	public static final int logCajasCajerosCodigo = 67;
	
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * CajasCajeros
	 */
	public static final String logFormasPagoNombre = "logFormasPago";

	/**
	 * C&oacute;digo del log de CajasCajeros
	 */
	public static final int logFormasPagoCodigo = 68;
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * Excepciones Asocio por Tipo Sala
	 */
	public static final String logExcepcionAsocioTipoSalaNombre = "logExcepcionAsocioTipoSala";

	/**
	 * C&oacute;digo del log de Excepciones Asocio por Tipo Sala
	 */
	public static final int logExcepcionAsocioTipoSalaCodigo = 69;
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * Excepciones Asocio por Tipo Sala
	 */
	public static final String logEntidadesFinancierasNombre = "logEntidadesFinancieras";

	/**
	 * C&oacute;digo del log de Excepciones Asocio por Tipo Sala
	 */
	public static final int logEntidadesFinancierasCodigo = 70;
	
	/**
	 * Nombre para el log de asocios x tipo servicio
	 */
	public static final String logAsociosTipoServicioNombre = "logAsociosTipoServicio";

	/**
	 * C&oacute;digo para el log de asocios x tipo servicio
	 */
	public static final int logAsociosTipoServicioCodigo = 71;
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * Tarjetas financieras
	 */
	public static final String logTarjetasFinancierasNombre = "logTarjetasFinancieras";

	/**
	 * C&oacute;digo del log de tarjetas financieras
	 */
	public static final int logTarjetasFinancierasCodigo = 72;
	
	/**
	 * Nombre para el log de Asocios X Uvr
	 */
	public static final String logAsociosXUvrNombre = "logAsociosXUvr";

	/**
	 * C&oacute;digo para el log de Asocios X Uvr
	 */
	public static final int logAsociosXUvrCodigo= 73;
	
	/**
	 * Nombre para el log de Excepciones QX
	 */
	public static final String logExcepcionesQXNombre = "logExcepcionesQX";

	/**
	 * C&oacute;digo para el log de Excepciones QX
	 */
	public static final int logExcepcionesQXCodigo= 74;
	
	/**
	 * Nombre para el log de Ingreso Consumo Materiales Qx.
	 */
	public static final String logIngresoConsumoMaterialesQxNombre = "logIngresoConsumoMaterialesQx";

	/**
	 * C&oacute;digo para el log de Ingreso Consumo Materiales Qx.
	 */
	public static final int logIngresoConsumoMaterialesQxCodigo= 75;

	/**
	 * Nombre para el log de Ingreso Consumo Materiales Qx.
	 */
	public static final String logModificarPeticionQXNombre = "logModificarPeticionQX";

	/**
	 * C&oacute;digo para el log de Modificacion de Peticiones.
	 */
	public static final int logModificarPeticionQXCodigo=76;
	/**
	 * C&oacute;digo para el log de tipos de transacciones inventarios.
	 */
	public static final int logTiposTransaccionesInvCodigo= 77;

	/**
	 * Nombre para el log de tipos de transacciones inventarios.
	 */
	public static final String logTiposTransaccionesInvNombre = "logTiposTransaccionesInventarios";
	/**
	 * C&oacute;digo para el log de transacciones validas por centro de costo
	 */
	public static final int logTransaccionesValidasXCCInvCodigo= 78;

	/**
	 * Nombre para el log de transacciones validas por centro de costo
	 */
	public static final String logTransaccionesValidasXCCInvNombre = "logTransaccionesValidasXCentroCostoInventarios";
	
	/**
	 * Codigo log motivos devoulcion
	 */
	public static final int logMotivoDevolucionInvCodigo=79;
	
	/**
	 * nombre log motivos devolucion
	 */
	public static final String logMotivoDevolucionInvNombre="logMotivoDevolucionInventarios";
	/**
	 * Codigo log usuarios por almacen
	 */
	public static final int logUsuariosXAlmacenInvCodigo=80;
	
	/**
	 * nombre log usuarios por almacen
	 */
	public static final String logUsuariosXAlmacenInvNombre="logUsuariosXAlmacenInventarios";
	
	/**
	 * Codigo log reversiï¿½n liquidacion Qx
	 */
	public static final int logReversionLiquidacionQxCodigo=81;
	
	/**
	 * nombre log reversiï¿½n liquidacion Qx.
	 */
	public static final String logReversionLiquidacionQxNombre="logReversionLiquidacionQx";
	
	/**
	 * Nombre para el log de Solicitudes Cx
	 */
	public static final String logSolicitudesCxNombre = "logSolicitudesCx";

	/**
	 * C&oacute;digo para el log de SolicitudesCx
	 */
	public static final int logSolicitudesCxCodigo= 82;
	/**
	 * Nombre para el log de registro transacciones inventario
	 */
	public static final String logRegistroTransaccionesInvNombre = "logRegistroTransaccionesInventario";

	/**
	 * C&oacute;digo para el log de registro transacciones inventario
	 */
	public static final int logRegistroTransaccionesInvCodigo= 83;
	
	/**
	 * Nombre para el log de la informaciï¿½n de la poliza
	 */
	public static final String logInformacionPolizaNombre = "logInformacionPoliza";

	/**
	 * C&oacute;digo para el log la informaciï¿½n de la poliza
	 */
	public static final int logInformacionPolizaCodigo= 84;
	
	
	/**
	 * Nombre para el log del cierre de inventarios
	 */
	public static final String logCierreInventariosNombre = "logCierreInventarios";

	/**
	 * C&oacute;digo para el log del cierre de invnetarios
	 */
	public static final int logCierreInventariosCodigo= 85;
	
	/**
	 * Nombre para el log de la informacion del formato de impresion de un presupuesto
	 */
	public static final String logFormatoImpresionPresupuestoNombre = "logFormatoImpresionPresupuesto";

	/**
	 * C&oacute;digo para el log de la informacion del formato de impresion de un presupuesto
	 */
	public static final int logFormatoImpresionPresupuestoCodigo= 86;
	
	/**
	 * Nombre para el log del Grupos de Servcios
	 */
	public static final String logGruposServiciosNombre = "logGruposServicios";

	/**
	 * C&oacute;digo para el log del Grupos de Servicios
	 */
	public static final int logGruposServiciosCodigo= 87;
	
	/**
	 * Nombre para el log de solicitud traslado almacenes
	 */
	public static final String logSolicitudTrasladoAlmacenNombre = "logSolicitudTrasladoAlmacen";

	/**
	 * C&oacute;digo para el log de solicitud traslado almacenes
	 */
	public static final int logSolicitudTrasladoAlmacenCodigo= 88;
	
	/**
	 * Nombre para el log de cargos directos servicios
	 */
	public static final String logCargosDirectosServiciosNombre = "logCargosDirectosServicios";

	/**
	 * C&oacute;digo para el log de cargos directos servicios
	 */
	public static final int logCargosDirectosServiciosCodigo= 89;
	
	/**
	 * Nombre para el log de cargos directos articulos
	 */
	public static final String logCargosDirectosArticulosNombre = "logCargosDirectosArticulos";

	/**
	 * C&oacute;digo para el log de cargos directos articulos
	 */
	public static final int logCargosDirectosArticulosCodigo= 90;
	
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar el log del medicamento eliminado de la secciï¿½n 
	 * Dieta de la funcionalidad registro de enfermeria.
	 */ 
	public static final String logRegEnferMedDietaElimNombre = "logRegEnferMedicamentoEliminadoDieta";

	/**
	 * Codigo al cual se le aï¿½adirï¿½ la fecha y la hora para manejar el log del mecicamnto eliminado de la secciï¿½n 
	 * Dieta de la funcionalidad registro de enfermeria.
	 */
	public static final int logRegEnferMedDietaElimCodigo = 91;  


	/**
	 * Nombre para manejar el log de Excepciones Tarifas Asocios
	 */
	public static final String logExcepcionTarifasQuirurgicasNombre = "logExcepcionTarifasQuirurgicas";

	/**
	 * Codigo para manejar el log de Excepciones Tarifas Asocios
	 */
	public static final int logExcepcionTarifasQuirurgicasCodigo = 92; 
	

	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar el log del medicamento eliminado de la secciï¿½n 
	 * Dieta de la funcionalidad registro de enfermeria.
	 */
	public static final String logRegEnferMedDietaModNombre = "logRegEnferMedicamentoModificadoDieta";

	/**
	 * Codigo al cual se le aï¿½adirï¿½ la fecha y la hora para manejar el log del mecicamnto eliminado de la secciï¿½n 
	 * Dieta de la funcionalidad registro de enfermeria.
	 */
	public static final int logRegEnferMedDietaModCodigo = 93;  

	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar el log del medicamento eliminado del 
	 * modulo interfaz funcionalidad Facturacion en Cuentas Interfaz Servicios  (para la edicion de la cuenta de grupos de servicios)
	 */
	public static final String logInterfazFactServicioGrpMod = "logInterfazFactServicioGrpMod";

	/**
	 * Codigo al cual se le aï¿½adirï¿½ la fecha y la hora para manejar el log del medicamento eliminado del 
	 * modulo interfaz funcionalidad Facturacion en Cuentas Interfaz Servicios  (para la edicion de la cuenta de grupos de servicios)
	 */
	public static final int logInterfazFactServicioGrpModCodigo = 94;  

	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar el log del medicamento eliminado del 
	 * modulo interfaz funcionalidad Facturacion en Cuentas Interfaz Servicios (para la edicion de la cuenta de tipos de servicios)
	 */
	public static final String logInterfazFactServicioTipoMod = "logInterfazFactServicioTipoMod";

	/**
	 * Codigo al cual se le aï¿½adirï¿½ la fecha y la hora para manejar el log del medicamento eliminado del 
	 * modulo interfaz funcionalidad Facturacion en Cuentas Interfaz Servicios  (para la edicion de la cuenta de tipos de servicios)
	 */
	public static final int logInterfazFactServicioTipoModCodigo = 95;  
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar el log de modificaciï¿½n de cuenta de ingreso de la clase de inventario 
	 * modulo interfaz funcionalidad Facturacion en Cuentas Interfaz Inventarios
	 */
	public static final String logInterfazFactInventarioClaseInvMod = "logInterfazFactInventarioClaseInvMod";

	/**
	 * Codigo al cual se le aï¿½adirï¿½ la fecha y la hora para manejar el log de modificaciï¿½n de cuenta de ingreso de la clase de inventario
	 * modulo interfaz funcionalidad Facturacion en Cuentas Interfaz Inventarios
	 */
	public static final int logInterfazFactInventarioClaseInvModCodigo = 96;
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar el log de modificaciï¿½n de cuenta de ingreso del grupo de inventario 
	 * modulo interfaz funcionalidad Facturacion en Cuentas Interfaz Inventarios
	 */
	public static final String logInterfazFactInventarioGrupoInvMod = "logInterfazFactInventarioGrupoInvMod";

	/**
	 * Codigo al cual se le aï¿½adirï¿½ la fecha y la hora para manejar el log de modificaciï¿½n de cuenta de ingreso del grupo de inventario
	 * modulo interfaz funcionalidad Facturacion en Cuentas Interfaz Inventarios
	 */
	public static final int logInterfazFactInventarioGrupoInvModCodigo = 97;  
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar el log de modificaciï¿½n de cuenta de ingreso del subgrupo de inventario 
	 * modulo interfaz funcionalidad Facturacion en Cuentas Interfaz Inventarios
	 */
	public static final String logInterfazFactInventarioSubGrupoInvMod = "logInterfazFactInventarioSubGrupoInvMod";

	/**
	 * Codigo al cual se le aï¿½adirï¿½ la fecha y la hora para manejar el log de modificaciï¿½n de cuenta de ingreso del subgrupo de inventario
	 * modulo interfaz funcionalidad Facturacion en Cuentas Interfaz Inventarios
	 */
	public static final int logInterfazFactInventarioSubGrupoInvModCodigo = 98;  
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar el log de modificaciï¿½n de cuenta de ingreso del articulo de inventario 
	 * modulo interfaz funcionalidad Facturacion en Cuentas Interfaz Inventarios
	 */
	public static final String logInterfazFactInventarioArticuloInvMod = "logInterfazFactInventarioArticuloInvMod";

	/**
	 * Codigo al cual se le aï¿½adirï¿½ la fecha y la hora para manejar el log de modificaciï¿½n de cuenta de ingreso del articulo de inventario
	 * modulo interfaz funcionalidad Facturacion en Cuentas Interfaz Inventarios
	 */
	public static final int logInterfazFactInventarioArticuloInvModCodigo = 99;  

	
	/**
	 * Codigo al cual se le aï¿½adirï¿½ la fecha y la hora para manejar el log del medicamento eliminado del 
	 * modulo interfaz funcionalidad Facturacion en Cuentas Interfaz Servicios  (para la edicion de la cuenta de Especialidades  de servicios)
	 */
	public static final int logInterfazFactServicioEspModCodigo = 100;  

	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar el log del medicamento eliminado del 
	 * modulo interfaz funcionalidad Facturacion en Cuentas Interfaz Servicios (para la edicion de la cuenta de Especialidades de servicios)
	 */
	public static final String logInterfazFactServicioEspMod = "logInterfazFactServicioEspMod";
	
	/**
	 * Codigo al cual se le aï¿½adirï¿½ la fecha y la hora para manejar el log del medicamento eliminado del 
	 * modulo interfaz funcionalidad Facturacion en Cuentas Interfaz Servicios  (para la edicion de la cuenta de los servicios)
	 */
	public static final int logInterfazFactServicioSerModCodigo = 101;  

	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar el log del medicamento eliminado del 
	 * modulo interfaz funcionalidad Facturacion en Cuentas Interfaz Servicios (para la edicion de la cuenta de los servicios)
	 */
	public static final String logInterfazFactServicioSerMod = "logInterfazFactServicioSerMod";
	
	/**
	 * Codigo para el log de interfaz facturacion cuentas convenios
	 */
	public static final int logInterfazFactCuentasConveniosModCodigo = 102;
	
	/**
	 * Nombre para el archivo del log interfaz facturacion cuentas convenios
	 */
	public static final String logInterfazFactCuentasConveniosMod = "logInterfazFactCuentasConveniosMod";
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar el log de modificaciï¿½n de alguno de los campos
	 * de la secciï¿½n info gral de la parametrizaciï¿½n de campos interfaz
	 */
	public static final String logInterfazFactParamInfoGralCamposInterfazMod = "logInterfazFactParamInfoGralCamposInterfazMod";

	/**
	 * Codigo al cual se le aï¿½adirï¿½ la fecha y la hora para manejarel log de modificaciï¿½n de alguno de los campos
	 * de la secciï¿½n info gral de la parametrizaciï¿½n de campos interfaz
	 */
	public static final int logInterfazFactParamInfoGralCamposInterfazModCodigo = 103;
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar el log de modificaciï¿½n
	 * de la funcionalidad Unidades Funcionales
	 */
	public static final String logUnidadesFuncionalesNombre = "logUnidadesFuncionales";

	/**
	 * Codigo al cual se le aï¿½adirï¿½ la fecha y la hora para manejar el log de modificaciï¿½n
	 * de la funcionalidad Unidades Funcionales
	 */
	public static final int logUnidadesFuncionalesCodigo = 104;
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar el log de modificaciï¿½n de la hoja
	 * quirï¿½rgica modulo ordenes, responder
	 */
	public static final String logHojaQuirugicaMod = "logHojaQuirurgicaMod";

	/**
	 * Codigo al cual se le aï¿½adirï¿½ la fecha y la hora para manejar el log de modificaciï¿½n de hoja
	 * quirï¿½rgica modulo ordenes, responder
	 */
	public static final int logHojaQuirugicaModCodigo = 105;  
	
	/**
	 * 
	 */
	public static final int logReasignarProfesionalCodigo = 106;
	
	
	/**
	 * 
	 */
	public static final String logReasignarProfesionalNombre = "logReasignarProfesional";
	
	/**
	 * C&oacute;digo para el log de los Centros de Costo
	 */
	public static final int logCentrosCostoCodigo =107;
	
	/** Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 *	Centros de Costo
	 */
	public static final String logCentrosCostoNombre = "logCentrosCosto";

		/**
	 * Codigo para el log de modificaciï¿½n y eliminaciï¿½n de centros de atenciï¿½n
	 */
	public static final int logCentrosAtencionCodigo = 108;
	
	/**
	 * Nombre para el archivo del log interfaz facturacion cuentas convenios
	 */
	public static final String logCentrosAtencionNombre = "logCentrosAtencion";
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar el log de eliminaciï¿½n 
	 * de centros costo X grupos servicios
	 */
	public static final String logCentroCostoXgrupoServicioElimNombre = "logCentroCostoXgrupoServicio";

	/**
	 * Codigo al cual se le aï¿½adirï¿½ la fecha y la hora para manejar el log de eliminaciï¿½n  
	 * de centros costo X grupos servicios
	 */
	public static final int logCentroCostoXgrupoServicioElimCodigo = 109;
	
	/**
	 * Codigo al cual se le aï¿½adirï¿½ la fecha y la hora para manejar el log de eliminaciï¿½n  
	 * de centros costo X unidades consulta
	 */
	public static final int logCentroCostoXUnidadesConsultaCodigo = 110;
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar el log de eliminaciï¿½n 
	 * de centros costo X unidades consulta
	 */
	public static final String logCentroCostoXUnidadesConsultaNombre="logCentroCostoXUnidadConsulta";
	
	/**
	 * String con los C&oacute;digos de funcionalidades reservadas
	 */
	public static int codigosFuncionalidadesReservadasSuperAdministrador[] = {10, 147,157 };
	
	
	/**
	 * 
	 */
	public static int codigoCuotoOdontEspecialidad=273;
	

	//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
	
	
	/**
	 * codigo para el LOG para la modificacion de unidades funcionales del modulo Interfaz y la funcionalidad facturacion.
	 */
	public static final int logInterfazCuentaInterfazUnidadFuncionalCodigo = 111;  

	/**
	 * codigo para el LOG para la modificacion de unidades funcionales del modulo Interfaz y la funcionalidad facturacion.
	 */
	public static final String logInterfazCuentaInterfazUnidadFuncional = "logInterfazFactUnidadFuncional";

	
	/**
	 * C&oacute;digo para el log de los Centros de Costo X Via de Ingreso
	 */
	public static final int logCentrosCostoXViaIngresoCodigo =112;
	
	/** Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 *	Centros de Costo X Via de Ingreso
	 */
	public static final String logCentrosCostoXViaIngresoNombre = "logCentrosCostoXViaIngreso";
	
	/**
	 * codigo para el LOG para la modificacion de los centros de costo para las unidades funcionales del modulo Interfaz y la funcionalidad facturacion.
	 */
	public static final int logInterfazCuentaInterfazUnidadFuncionalCCCodigo = 113;  

	/**
	 * codigo para el LOG para la modificacion  de los centros de costo para funcionales del modulo Interfaz y la funcionalidad facturacion.
	 */
	public static final String logInterfazCuentaInterfazUnidadFuncionalCC = "logInterfazFactUnidadFuncionalCentroCosto";

	/**
	 * C&oacute;digo para el log de los Grupos Etareos
	 */
	public static final int logGruposEtareosCodigo =114;
	
	/** Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 *	Grupos Etareos 
	 */
	public static final String logGruposEtareosNombre = "logGruposEtareos";
	
	/**
	 * codigo para el LOG para la modificacion de los centros de costo para las unidades funcionales del modulo Interfaz y la funcionalidad facturacion.
	 */
	public static final int logOrdenesRespCirugiaHojaQuirurDiagSerDiagCodigo = 115;  

	/**
	 * codigo para el LOG para la modificacion  de los centros de costo para funcionales del modulo Interfaz y la funcionalidad facturacion.
	 */
	public static final String logOrdenesRespCirugiaHojaQuirurDiagSerDiag = "logOrdenesRespCirugiaHojaQuirurDiagSerDiag";
	
	/**
	 * codigo para el LOG para la modificacion/eliminacion  de Destinos Triage.
	 */
	public static final int logDestinoTriageCodigo = 116;  

	/**
	 * codigo para el LOG para la modificacion/eliminacion  de Destinos Triage.
	 */
	public static final String logDestinoTriageNombre = "logDestinoTriage";

	/**
	 * C&oacute;digo para el log de los Sistemas Motivo de Consulta de Urgencias
	 */
	public static final int logSistemasMotivoConsultaUrgCodigo =117;
	
	/** Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 *	Sistemas Motivo de Consulta de Urgencias 
	 */
	public static final String logSistemasMotivoConsultaUrgNombre = "logSistemasMotivoConsultaUrg";
	
	/**
	 * Codigo para el log de modificaciï¿½n y eliminaciï¿½n de mezcla
	 */
	public static final int logMezclasCodigo = 118;
	
	/**
	 * Nombre para el archivo del log inventarios mantenimiento mezclas
	 */
	public static final String logMezclasNombre = "logMezclas";

	/**
	 * Nombre para el log de signos sintomas x sistema
	 */
	public static final String logSignosSintomasXSistemaUvrNombre = "logSignosSintomasXSistema";

	/**
	 * C&oacute;digo para el log de signos sintomas x sistema
	 */
	public static final int logSignosSintomasXSistemaCodigo= 119;
	
	/**
	 * Nombre para el log de categorias triage.
	 */
	public static final String logCategoriasTriageNombre = "logCategoriasTriage";
	
	/**
	 * codigo para el log de categorias triage.
	 */
	public static final int logCategoriasTriageCodigo = 120;
	
	/**
	 * C&oacute;digo para el log de eliminaciï¿½n y eliminaciï¿½n de artï¿½culos por mezcla
	 */
	public static final int logArticulosXMezclaCodigo = 121;
	
	/**
	 * Nombre para el log de eliminaciï¿½n y eliminaciï¿½n de artï¿½culos por mezcla
	 */
	public static final String logArticulosXMezclaNombre = "logArticulosXMezcla";
	
	/**
	 * C&oacute;digo para el log de modificaciï¿½n y eliminaciï¿½n de unidades de medida
	 */
	public static final int logUnidadMedidaCodigo = 122;
	
	/**
	 * Nombre para el log de modificaciï¿½n y eliminaciï¿½n de unidades de medida
	 */
	public static final String logUnidadMedidaNombre = "logUnidadMedida";
	

	/**
	 * C&oacute;digo para el log de modificaciï¿½n y eliminaciï¿½n de unidades de medida
	 */
	public static final int logSignosSintomasCodigo = 123;
	
	/**
	 * Nombre para el log de modificaciï¿½n y eliminaciï¿½n de unidades de medida
	 */
	public static final String logSignosSintomas = "logSignosSintomas";

	
	/**
	 * C&oacute;digo para el log de modificaciï¿½n y eliminaciï¿½n de unidades de medida
	 */
	public static final int logNivelServicioCodigo = 124;
	
	/**
	 * Nombre para el log de modificaciï¿½n y eliminaciï¿½n de unidades de medida
	 */
	public static final String logNivelServicio = "logNivelServicio";

	
	/**
	 * C&oacute;digo para el log de modificaciï¿½n y eliminaciï¿½n de excepciones de niveles de servicios 
	 */
	public static final int logExcepcionNivelServicioCodigo = 125;
	
	/**
	 * Nombre para el log de modificaciï¿½n y eliminaciï¿½n de excepciones de niveles de servicios
	 */
	public static final String logExcepcionNivelServicio = "logExcepcionNivelServicio";
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * Cuentas cobro de Capitaciï¿½n
	 */
	public static final String logCuentasCobroCapitacionNombre = "logCuentasCobroCapitacion";

	/**
	 * C&oacute;digo del log de Cuentas cobro de Capitaciï¿½n
	 */
	public static final int logCuentasCobroCapitacionCodigo = 126;
	
	/**
	 * Nombre log Cuentas proceso facturacion
	 */
	public static final String logCuentasProcesoFacturacionNombre = "logCuentasProcesoFacturacionNombre";

	
	/**
	 * C&oacute;digo del log de Cuentas proceso facturacion
	 */
	public static final int logCuentasProcesoFacturacionCodigo = 127;
	
	/**
	 * 
	 */
	public static final String logTiposProgramaPYPNombre="logTiposProgramaPYP";
	
	/**
	 * 
	 */
	public static final int logTiposProgramaPYPCodigo =128;
	
	/**
	 * 
	 */
	public static final String logProgramaPYPNombre="logProgramaPYP";
	
	/**
	 * 
	 */
	public static final int logProgramaPYPCodigo =129;
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * Actividades de pormociï¿½n y prevenciï¿½n
	 */
	public static final String logActividadesPYPNombre = "logActividadesPYP";
	
	/**
	 * C&oacute;digo del log de Actividades de promociï¿½n y prevenciï¿½n
	 */
	public static final int logActividadesPYPCodigo =130;
	
	/**
	 * Nombre para el archivo del log capitacion, subir paciente, modificar cargues
	 */
	public static final String logContratoCargueNombre = "logContratoCargue";
	
	/**
	 * Codigo para el log de inserciï¿½n, modificaciï¿½n y eliminaciï¿½n de capitacion, subir paciente, modificar cargues
	 */
	public static final int logContratoCargueCodigo = 131;


	
	/**
	 * Nombre para el archivo del log de Programas y Actividades por Convenio
	 */
	public static final String logProgramasActividadesConvenioNombre = "logProgramasActividadesConvenio";
	
	/**
	 * Codigo para el log de Programas y Actividades por Convenio
	 */
	public static final int logProgramasActividadesConvenioCodigo = 132;
	
	/**
	 * Nombre para el archivo del log capitacion, subir paciente, modificar cargues
	 */
	public static final String logProgramaArticuloPyp = "logProgramaArticuloPyp";
	
	/**
	 * Codigo para el log de inserciï¿½n, modificaciï¿½n y eliminaciï¿½n de capitacion, subir paciente, modificar cargues
	 */
	public static final int logProgramaArticuloPypCodigo = 133;
	
	/**
	 * C&oacute;digo para el log de modificaciï¿½n y eliminaciï¿½n de calificaciones por cumplimiento
	 * de metas
	 */
	public static final int logCalificacionXCumpliMetasCodigo = 134;
	
	/**
	 * Nombre para el log de modificaciï¿½n y eliminaciï¿½n de calificaciones por cumplimiento
	 * de metas
	 */
	public static final String logCalificacionXCumpliMetas = "logCalificacionXCumpliMetas";
	
	/**
	 * C&oacute;digo para el log de Metas PYP
	 * de metas
	 */
	public static final int logMetasPYPCodigo = 135;
	
	/**
	 * Nombre para el log de Metas PYP
	 * de metas
	 */
	public static final String logMetasPYPNombre = "logMetasPYP";
	
	
	/**
	 * C&oacute;digo para el log de ActividadesPrograma PYP
	 */
	public static final int logActividadesProgramaPYPCodigo = 136;
	
	/**
	 * Nombre para el log de ActividadesPrograma PYP
	 */
	public static final String logActividadesProgramaPYPNombre = "logActividadesProgramaPYP";

	/**
	 * C&oacute;digo para el log de Tipos de Calificacion de Metas PYP
	 */
	public static final int logTipoCalificacionMetaPypCodigo = 137;
	
	/**
	 * Nombre para el log de Tipos de Calificacion de Metas PYP
	 */
	public static final String logTipoCalificacionMetaPypNombre = "logTipoCalificacionMetaPyp";


	/**
	 * C&oacute;digo para el log de Actividades PYP por Centro Atencion
	 */
	public static final int logActividadesPypCentroAtencionCodigo = 138;
	
	/**
	 * Nombre para el log de Actividades PYP por Centro Atencion
	 */
	public static final String logActividadesPypCentroAtencionNombre = "logActividadesPypCentroAtencion";

	
	/**
	 * C&oacute;digo para el log de los Grupos Etareos
	 */
	public static final int logGruposEtareosCredDesaCodigo =139;
	
	/** Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 *	Grupos Etareos 
	 */
	public static final String logGruposEtareosCredDesaNombre = "logGruposEtareosCrecimientoDesarrollo";
	

	/**
	 * C&oacute;digo para el log de los Unidades de Pago Capitacion
	 */
	public static final int logUnidadPagoCapitacionCodigo =140;
	
	/** 
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 *	Unidades de Pago Capitacion 
	 */
	public static final String logUnidadPagoCapitacionNombre = "logUnidadPagoCapitacion";
	
	/**
	 * 
	 */
	public static final String logFolderFosyga="fosyga";
	
	/**
	 * 
	 */
	public static final int logRegistroAccidentesTransitoCodigo = 141;
	
	/**
	 * 
	 */
	public static final String logRegistroAccidentesTransitoNombre = "logRegistroAccidentesTrancito";

	
	/**
	 * C&oacute;digo para el log de la modificacion de camas
	 */
	public static final int logModificacionCamaCodigo =142;
	
	/** 
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 *	Modificacion Camas
	 */
	public static final String logModificacionCamaNombre = "logModificacionCama";
	
	
	/**
	 * C&oacute;digo para el log de motivos sirc
	 */
	public static final int logMotivosSircCodigo =143;
	
	/** 
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 *	motivos sirc
	 */
	public static final String logMotivosSircNombre = "logMotivosSirc";
	
	/**
	 * C&oacute;digo para el log de instituciones sirc
	 */
	public static final int logInstitucionesSircCodigo =144;
	
	/** 
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 *	instituciones sirc
	 */
	public static final String logInstitucionesSircNombre = "logInstitucionesSirc";
	
	
	/**
	 * C&oacute;digo para el log de servicios sirc
	 */
	public static final int logServiciosSircCodigo =145;
	
	/** 
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 *	servicios sirc
	 */
	public static final String logServiciosSircNombre = "logServiciosSirc";
	
	
	/**
	 * C?digo para el log de servicios sirc
	 */
	public static final int logIndicativosolicitudGrupoServicioCodigo =146;
	
	/** 
	 * Nombre al cual se le a?adir? la fecha y la hora para manejar los logs de
	 *	servicios sirc
	 */
	public static final String logIndicativosolicitudGrupoServicioNombre = "logIndicativosolicitudGrupoServicio";
	
	
	/**
	 * Nombre para el log 
	 */
	public static final String logParametrizacionCurvaAlertaNombre = "logParametrizacionCurvaAlerta";

	/**
	 * C?digo para el log 
	 */
	public static final int logParametrizacionCurvaAlertaNombreCodigo= 147;
	
	/**
	 * Nombre para el log 
	 */
	public static final String logIndicativoCargoViaIngresoGrupoServicioNombre = "logIndicativoCargoViaIngresoGrupoServicio";

	/**
	 * C?digo para el log 
	 */
	public static final int logIndicativoCargoViaIngresoGrupoServicioCodigo= 148;
	
	/**
	 * C&oacute;digo para el log 
	 */
	public static final int logConsultoriosCodigo =149;
	
	/** 
	 *	logs consultorios
	 */
	public static final String logConsultoriosNombre = "logConsultoriosSirc";
	
	/**
	 * C&oacute;digo para el log 
	 */
	public static final int logPaquetesMaterialesQxCodigo =150;
	
	/** 
	 *	logs consultorios
	 */
	public static final String logPaquetesMaterialesQxNombre = "logPaquetesMaterialesQx";
	
	/**
	 * C&oacute;digo para el log 
	 */
	public static final int logCancelacionAgendaCodigo =151;
	
	/** 
	 *	logs cancelacion de agenda
	 */
	public static final String logCancelacionAgendaNombre = "logCancelacionAgenda";
	
	
	/**
	 * C&oacute;digo para el log de eliminaciï¿½n y eliminaciï¿½n de Formas Farmaceuticas
	 */
	public static final int logFormaFarmaceuticaCodigo = 152;
	
	/**
	 * Nombre para el log de eliminaciï¿½n y eliminaciï¿½n de Formas Farmaceuticas
	 */
	public static final String logFormaFarmaceuticaNombre = "logFormaFarmaceutica";
	
	/**
	 * C&oacute;digo para el log de modificacion de Paciente
	 */
	public static final int logModificarPacienteCodigo = 154;
	
	/**
	 * Nombre para el log de modificacion de Paciente
	 */
	public static final String logModificarPacienteNombre = "logModificarPaciente";
	
	/**
	 * C&oacute;digo para el log de modificacion de Paciente
	 */
	public static final int logModificarClaseGrupoSubgrupoInvCodigo = 155;
	
	/**
	 * Nombre para el log de modificacion de Paciente
	 */
	public static final String logModificarClaseGrupoSubgrupoInvNombre = "logModificarClaseGrupoSubgrupoInv";
	
	/**
	 * C&oacute;digo para el log de cuentas inventario x unidad funcional
	 */
	public static final int logCuentasInventarioUnidadFuncionalCodigo = 156;
	
	/**
	 * Nombre para el log de cuentas inventario x unidad funcional
	 */
	public static final String logCuentasInventarioUnidadFuncionalNombre = "logCuentasInventario";
	
	/**
	 * C&oacute;digo para el log de Cobertura
	 */
	public static final int logCoberturaCodigo = 157;
	
	/**
	 * Nombre para el log de Cobertura
	 */
	public static final String logCoberturaNombre = "logCobertura";
	
	
	/**
	 * C&oacute;digo para el log de Paquetes
	 */
	public static final int logPaquetesCodigo = 158;
	
	/**
	 * Nombre para el log de Paquetes
	 */
	public static final String logPaquetesNombre = "logPaquetes";
	
	/**
	 * C&oacute;digo para el log de Horarios de Atenciï¿½n
	 */
	public static final int logHorarioAtencionCodigo = 159;
	
	/**
	 * Nombre para el log de Horarios de Atenciï¿½n
	 */
	public static final String logHorarioAtencionNombre = "logHorarioAtencion";
	
	/**
	 * C&oacute;digo para el log de Inclusiones Exclusiones
	 */
	public static final int logInclusionesExclusionesCodigo = 160;
	
	/**
	 * Nombre para el log de Inclusiones Exclusiones
	 */
	public static final String logInclusionesExclusionesNombre = "logInclusionesExclusiones";
	
	/**
	 * C&oacute;digo para el log de Examen Condiciones de Toma
	 * */
	public static final int logExamenCondiTomaCodigo = 161;
	
	/**
	 * Nombre para el log de Examen Condiciones de Toma 
	 * */
	public static final String logExamenCondiTomaNombre = "logCondicionesTomaExamen";
	
	/**
	 * C&oacute;digo para el log de RIPS Capitaciï¿½n
	 * */
	public static final int logRipsCapitacionCodigo = 162;
	
	/**
	 * Nombre para el log de RIPS Capitaciï¿½n 
	 * */
	public static final String logRipsCapitacionNombre = "logRipsCapitacion";
	
	/**
	 * C&oacute;digo para el log de Paquetes Por Convenio
	 */
	public static final int logPaquetesConvenioCodigo = 163;
	
	/**
	 * Nombre para el log de Paquetes Por Convenio
	 */
	public static final String logPaquetesConvenioNombre = "logPaquetesConvenio";
	
	
	/**
	 * 
	 */
	public static final int logRegistroEventoCatastroficoCodigo = 165;
	
	/**
	 * 
	 */
	public static final String logRegistroEventoCatastroficoNombre = "logRegistroEventoCatastrofico";

	
	/**
	 * Codigo para el log de Servicios por Grupos Esteticos
	 */
	public static final int logServiciosGruposEsteticosCodigo = 166;
	
	/**
	 * Nombre para el log de Servicios por Grupos Esteticos
	 */
	public static final String logServiciosGruposEsteticosNombre = "logServiciosGruposEsteticos";
	
	/**
	 * Codigo para el log de Generaciï¿½n Anexos Forecat
	 */
	public static final int logGeneracionAnexosForecatCodigo = 167;
	
	/**
	 * Nombre para el log de Generaciï¿½n Anexos Forecat
	 */
	public static final String logGeneracionAnexosForecatNombre = "logGeneracionAnexosForecat";
	
	
	/**
	 * C&oacute;digo para el log de Tipos Convenio
	 */
	public static final int logTiposConvenioCodigo = 168;
	
	/**
	 * Nombre para el log de Tipos Convenio
	 */
	public static final String logTiposConvenioNombre = "logTiposConvenio";
	
	
	/**
	 * C&oacute;digo para el log de Unidad de Procedimientp
	 * */
	public static final int logUnidadProcedimientoCodigo = 169;
	
	/**
	 * Nombre para el log de Unidad de Procedimiento 
	 * */
	public static final String logUnidadProcedimientoNombre = "logUnidadProcedimiento";
	
	/**
	 * C&oacute;digo para el log de componentes paquetes
	 */
	public static final int logComponentesPaquetesCodigo =170;
	
	/**
	 * Nombre para el log de componentes paquetes
	 * */
	public static final String logComponentesPaquetesNombre = "logComponentesPaquetes";
	
	/**
	 * C&oacute;digo para el log de Pisos
	 */
	public static final int logPisosCodigo = 171;
	
	/**
	 * Nombre para el log de Pisos
	 */
	public static final String logPisosNombre = "logPisos";
	
	/**
	 * C&oacute;digo para el log de Tipo Habitacion
	 */
	public static final int logTipoHabitacionCodigo = 172;
	
	/**
	 * Nombre para el log de Tipo Habitacion
	 */
	public static final String logTipoHabitacionNombre = "logTipoHabitacion";
	
	/**
	 * C&oacute;digo para el log de Habitaciones
	 */
	public static final int logHabitacionesCodigo = 173;
	
	/**
	 * Nombre para el log de Habitaciones
	 */
	public static final String logHabitacionesNombre = "logHabitaciones";
	
	/**
	 * Nombre para el log de Almacen Parametros
	 */
	public static final String logAlmacenParametrosNombre = "logAlmacenParametros";
	
	/**
	 * C&oacute;digo del log de Almacen Parametros
	 */
	public static final int logAlmacenParametrosCodigo = 174;	
	
	
	/**
	 * Nombre para el log de Detalle Cobertura
	 */
	public static final String logDetalleCoberturaNombre = "logDetalleCobertura";
	
	/**
	 * C&oacute;digo del log de Detalle Cobertura
	 */
	public static final int logDetalleCoberturaCodigo = 175;
	
	
	/**
	 * Nombre para el log de CoberturaCovenio
	 */
	public static final String logCoberturaConvenioNombre = "logCoberturaConvenio";
	
	/**
	 * C&oacute;digo del log de CoberturaCovenio
	 */
	public static final int logCoberturaConvenioCodigo = 176;	
	
	
	/**
	 * Nombre para el log de Descunetos comerciales
	 */
	public static final String logDescuentosComercialesNombre = "logDescuentosComerciales";
	
	/**
	 * C&oacute;digo del log de Descunetos comerciales
	 */
	public static final int logDescuentosComercialesCodigo = 177;		
	
	/**
	 * C&oacute;digo para el log Tipos Usuario Cama
	 */
	public static final int logTiposUsuarioCamaCodigo = 178;
	
	/**
	 * Nombre para el log de Tipos Usuario Cama
	 */
	public static final String logTiposUsuarioCamaNombre = "logTiposUsuarioCama";
	
	
	
	/**
	 * C&oacute;digo para el log DetalleInclusionesExclusiones
	 */
	public static final int logDetalleInclusionesExclusionesCodigo=179;
	
	/**
	 * Nombre para el log de DetalleInclusionesExclusiones
	 */
	public static final String logDetalleInclusionesExclusionesNombre = "logDetalleInclusionesExclusiones";
	
	/**
	 * C&oacute;digo para el log Tipos Ambulancia
	 */
	public static final int logTiposAmbulanciaCodigo = 180;
	
	/**
	 * Nombre para el log de Tipos Ambulancia
	 */
	public static final String logTiposAmbulanciaNombre = "logTiposAmbulancia";
	
	/**
	 * C&oacute;digo para el log InclusionesExclusionesConvenio
	 */
	public static final int logInclusionesExclusionesConvenioCodigo = 181;
	
	/**
	 * Nombre para el log de InclusionesExclusionesConvenio
	 */
	public static final String logInclusionesExclusionesConvenioNombre = "logInclusionesExclusionesConvenio";
	
	/**
	 * C&oacute;digo para el log Naturaleza de Articulos
	 */
	public static final int logNaturalezaArticulosCodigo = 182;
	
	/**
	 * Nombre para el log de Naturaleza de Articulos
	 */
	public static final String logNaturalezaArticulosNombre = "logNaturalezaArticulos";
	
	/**
	 * C&oacute;digo para el log Vias de Ingreso
	 */
	public static final int logViasIngresoCodigo = 183;
	
	/**
	 * Nombre para el log de Vias de Ingreso
	 */
	public static final String logViasIngresoNombre = "logViasIngreso";
	
	/**
	 * C&oacute;digo para el log Ubicacion Geografica
	 */
	public static final int logUbicacionGeograficaCodigo = 184;
	
	/**
	 * Nombre para el log de Ubicacion Geografica
	 */
	public static final String logUbicacionGeograficaNombre = "logUbicacionGeografica";
	
	/**
	 * C&oacute;digo para el log de Consentimeinto Informado
	 */
	public static final int logConsentimientoInformadoCodigo = 185;
	
	/**
	 * Nombre para el log de Consentimeinto Informado
	 */
	public static final String logConsentimientoInformadoNombre = "logConsentimientoInformado";
	
	/**
	 * C&oacute;digo para el log de Registro Documentos Garantia 
	 */
	public static final int logRegistroDocumentoGarantiaCodigo = 186;
	
	
	/**
	 * Nombre para el log de Regsitro Documentos Garantia
	 */
	public static final String logRegistroDocumentosGarantiaNombre = "logRegistroDocumentosGarantia";
	
	/**
	 * C&oacute;digo para el log de Servicios Automaticos 
	 */
	public static final int logServiciosAutomaticosCodigo = 187;
	
	
	/**
	 * Nombre para el log de Servicios Automaticos
	 */
	public static final String logServiciosAutomaticosNombre = "logServiciosAutomaticos";
	
	/**
	 * C&oacute;digo para el log de Conceptos Pago Pooles 
	 */
	public static final int logConceptosPagoPoolesCodigo = 188;
	
	
	/**
	 * Nombre para el log de Conceptos Pago Pooles
	 */
	public static final String logConceptosPagoPoolesNombre = "logConceptosPagoPooles";
	
	/**
	 * C&oacute;digo para el log de Tipos de Monedas
	 */
	public static final int logTiposMonedaCodigo = 189;
	
	
	/**
	 * Nombre para el log de Tipos de Monedas
	 */
	public static final String logTiposMonedaNombre = "logTiposMonedas";
	
	/**
	 * C&oacute;digo para el log de Conceptos Pago Pooles X Convenios 
	 */
	public static final int logConceptosPagoPoolesXConvenioCodigo = 190;
	
	
	/**
	 * Nombre para el log de Conceptos Pago Pooles X Convenios
	 */
	public static final String logConceptosPagoPoolesXConvenioNombre = "logConceptosPagoPoolesXConvenios";
	
	/**
	 * C&oacute;digo para el log de Conceptos Pago Pooles X Convenios 
	 */
	public static final int logFactorConversionMonedasCodigo = 191;
	
	
	/**
	 * Nombre para el log de Conceptos Pago Pooles X Convenios
	 */
	public static final String logFactorConversionMonedasNombre = "logFactorConversionMonedas";
	
	/**
	 * C&oacute;digo para el log de Cancelacion de Citas 
	 */
	public static final int logCancelacionCitasCodigo = 192;
	
	
	/**
	 * Nombre para el log de Cancelacion de Citas
	 */
	public static final String logCancelacionCitasNombre = "logCancelacionCitas";
	
	/**
	 * Codigo para el log de la parametrizacion de archivos planos de colsanitas
	 */
	public static final int logArchivoPlanoColsanitasCodigo=193;
	
	/**
	 * Nombre para el log de la parametrizacion de archivos planos colsanitas
	 */
	public static final String logArchivoPlanoColsanitasNombre="logArchivoPlanoColsanitas";
	
	/**
	 * Codigo para el log de la parametrizacion de archivos planos de colsanitas
	 */
	public static final int logServiciosViaAccesoCodigo=194;
	
	/**
	 * Nombre para el log de la parametrizacion de archivos planos colsanitas
	 */
	public static final String logServiciosViaAccesoNombre="logServiciosViaAcceso";
	
	/**
	 * Codigo para el log de la parametrizacion de Asocios Sala Cirugia
	 * */
	public static final int logAsociosSalaCirugiaCodigo = 195;
	
	/**
	 * Nombre para el log de la parametrizacion de Asocios Sala Cirugia
	 * */
	public static final String logAsociosSalaCirugiaNombre = "logAsociosSalaCirugia";
	
	/**
	 * Codigo para el log de la parametrizacion de Procedimientos por paquetes quirï¿½rgicos
	 * */
	public static final int logProcedimientosPaquetesQuirugicosCodigo = 196;
	
	/**
	 * Nombre para el log de la parametrizacion de Procedimientos por paquetes quirï¿½rgicos
	 * */
	public static final String logProcedimientosPaquetesQuirugicosNombre = "logProcedimientosPaquetesQuirurgicos";
	
	
	/**
	 * Codigo para el log de la parametrizacion de Procedimientos por paquetes quirï¿½rgicos
	 * */
	public static final int logAsociosServiciosTarifasCodigo = 197;
	
	/**
	 * Nombre para el log de la parametrizacion de Procedimientos por paquetes quirï¿½rgicos
	 * */
	public static final String logAsociosServiciosTarifasNombre = "logAsociosServiciosTarifas";
		
	/**
	 * Codigo para el log de la parametrizacion de Farmacia por Centros de Costos
	 * */
	public static final int logFarmaciaCentroCostoCodigo = 198;
	
	/**
	 * Nombre para el log de la parametrizacion de Farmacia por Centros de Costos
	 * */
	public static final String logFarmaciaCentroCostoNombre = "logFarmaciaCentroCosto";
	
	/**
	 * Codigo para el log de la parametrizacion de Ubicacion de articulos por almacen
	 * */
	public static final int logArticulosPorAlmacenCodigo = 199;
	
	/**
	 * Nombre para el log de la parametrizacion de Ubicacion de articulos por almacen
	 * */
	public static final String logArticulosPorAlmacenNombre = "logArticulosPorAlmacen";
	
	
		/**
	 * Codigo para el log de la parametrizacion de Secciones
	 * */
	public static final int logSeccionesCodigo = 200;
	
	
	/**
	 * Nombre para el log de la parametrizacion de Secciones
	 * */
	public static final String logSeccionesNombre = "logSecciones";

	
	/**
	 * Codigo para el log de la parametrizacion de Subsecciones
	 * */
	public static final int logSubseccionesCodigo = 201;
	
	
	/**
	 * Nombre para el log de la parametrizacion de Subsecciones
	 * */
	public static final String logSubseccionesNombre = "logSubsecciones";
	
	/**
	 * Codigo para el log de la parametrizacion de Conceptos Facturas Varias
	 * */
	public static final int logConceptosFacturasVariasCodigo = 202;
	
	
	/**
	 * Nombre para el log de la parametrizacion de Conceptos Facturas Vatias
	 * */
	public static final String logConceptosFacturasVariasNombre = "logConceptosFacturasVarias";
	
	
	
	/**
	 * Codigo para el log de la parametrizacion de listado ingresos - actualizacion autorizaciones
	 * */
	public static final int logListadoIngresosCodigo = 203;
	
	
	/**
	 * Nombre para el log de la parametrizacion de Conceptos Facturas Vatias
	 * */
	public static final String logListadoIngresosNombre = "logActualizacionAutorizaciones";

	
	
	/**
	 * Codigo para el log de la parametrizacion de motivo cierre / apertura ingreso
	 * */
	public static final int logMotivoCierreAperturaIngresoCodigo = 204;
	
	
	/**
	 * Nombre para el log de la parametrizacion de motivo cierre / apertura ingreso
	 * */
	public static final String logMotivoCierreAperturaIngresoNombre = "logMotivoCierreAperturaIngreso";

	
	/**
	 * Codigo para el log de la parametrizacion de ARTICULOS EQUIVALENTES 
	 * */
	public static final int logEquivalentesDeInventarioCodigo = 205;
	
	
	/**
	 * Nombre para el log de la parametrizacion de Conceptos Facturas Vatias
	 * */
	public static final String logEquivalentesDeInventarioNombre = "logEquivalentesDeInventario";
	
	/**
	 * Codigo para el log de la parametrizacion de sustitutos no Pos
	 * */
	public static final int logSustitutosNoPosCodigo = 206;
	
	
	/**
	 * Nombre para el log de la parametrizacion de sustitutos no Pos
	 * */
	public static final String logSustitutosNoPosNombre = "logSustitutosNoPos";
	
	/**
	 * Codigo para el log de la parametrizacion de Registro Conteo Inventarios
	 * */
	public static final int logRegistroConteoInventariosCodigo = 207;
	
	
	/**
	 * Nombre para el log de la parametrizacion de Registro Conteo Inventarios
	 * */
	public static final String logRegistroConteoInventariosNombre = "logRegistroConteoInventarios";
	
	
	/**
	 * Codigo para el log de servicios de asocios
	 * */
	public static final int logServiciosAsocioCodigo = 208;
	
	
	/**
	 * Nombre para el log de servicios de asocios
	 * */
	public static final String logServiciosAsocioNombre = "logServiciosAsocio";
	
	/**
	 * Codigo para el log de liquidacion servicios
	 * */
	public static final int logLiquidacionServiciosCodigo = 209;
	
	
	/**
	 * Nombre para el log de liquidacion servicios
	 * */
	public static final String logLiquidacionServiciosNombre = "logLiquidacionServicios";
	
	
	
	/**
	 * Codigo para el log de la funcionalidad de Deudores
	 * */
	public static final int logDeudoresCodigo = 210;
	
	
	/**
	 * Nombre para el log de la parametrizacion de Conceptos Facturas Vatias
	 * */
	public static final String logDeudoresNombre = "logDeudores";
	
	
	/**
	 * Codigo para el log de la parametrizacion de Tipos Tratamientos Odontologicos
	 * */
	public static final int logTiposTratamientosOdontologicosCodigo = 211;
	
	
	/**
	 * Nombre para el log de la parametrizacion de Tipos Tratamientos Odontologicos
	 * */
	public static final String logTiposTratamientosOdontologicosNombre = "logTiposTratamientosOdontologicos";
	
	
	/**
	 * Codigo al cual se le aï¿½adirï¿½ la fecha y la hora para manejar el log de eliminaciï¿½n  
	 * de MotivosDevolucionRecibosCaja
	 */
	public static final int logMotivosDevolucionRecibosCajaCodigo = 212;
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar el log de eliminaciï¿½n 
	 * de MotivosDevolucionRecibosCaja
	 */
	public static final String logMotivosDevolucionRecibosCajaNombre="logMotivosDevolucionRecibosCaja";
	
	/**
	 * Codigo al cual se le aï¿½adirï¿½ la fecha y la hora para manejar el log de eliminaciï¿½n  
	 * de RegistroRipsCargosDirectos
	 */
	public static final int logRegistroRipsCargosDirectosCodigo = 213;
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar el log de eliminaciï¿½n 
	 * de RegistroRipsCargosDirectos
	 */
	public static final String logRegistroRipsCargosDirectosNombre="logRegistroRipsCargosDirectos";
	
	/**
	 * Codigo para el log de la parametrizacion de Indicadores de Calidad
	 * */
	public static final int logParamArchivoPlanoIndCalidadCodigo = 214;
	
	/**
	 * Nombre para el log de la parametrizacion de Indicadores de Calidad
	 * */
	public static final String logParamArchivoPlanoIndCalidadNombre = "logParamArchivoPlanoIndCalidad";
	

	
	/**
	 * Codigo para el log de la parametrizacion de Unidad de Agenda por Usuario por CA
	 * */
	public static final int logUnidadAgendaUsuarioCentroCodigo = 216;
	
	/**
	 * Nombre para el log de la parametrizacion de Unidad de Agenda por Usuario por CA
	 * */
	public static final String logUnidadAgendaUsuarioCentroNombre = "logUnidadAgendaUsuarioCentro";
	
	
	/**
	 * Codigo para el log de la parametrizacion de Unidad de Agenda por Usuario por CA
	 * */
	public static final int logGeneracionModificacionAjustesFacVariasCodigo = 217;
	
	
	

	
	/**
	 * Nombre para el log de la parametrizacion de Unidad de Agenda por Usuario por CA
	 * */
	public static final String logGeneracionModificacionAjustesFacVariasNombre = "logGeneracionModificacionAjustesFacVarias";
	
	
	/**
	 * Codigo para el log de la parametrizacion de los motivos de satisafaccion e insatisfaccion
	 * */
	public static final int logMotivosSatisfaccionInsatisfaccionCodigo = 224;
	
	/**
	 * Nombre para el log de la parametrizacion de los motivos de satisafaccion e insatisfaccion
	 * */
	public static final String logMotivosSatisfaccionInsatisfaccionNombre = "logMotivosSatisfaccionInsatisfaccion";
	

	/**
	 * Codigo para el log de la parametrizacion de los motivos de satisafaccion e insatisfaccion
	 * */
	public static final int logParametrizacionEscalas = 225;
	
	/**
	 * Nombre para el log de la parametrizacion de los motivos de satisafaccion e insatisfaccion
	 * */
	public static final String logParametrizacionEscalasNombre = "logParametrizacionEscalas";
	
	/**
	 * Codigo para el log de la parametrizacion de los motivos de satisafaccion e insatisfaccion
	 * */
	public static final int logServiciosXTipoTratamientoOdontologicoCodigo = 226;
	
	/**
	 * Nombre para el log de la parametrizacion de los motivos de satisafaccion e insatisfaccion
	 * */
	public static final String logServiciosXTipoTratamientoOdontologicoNombre = "logServiciosXTipoTratamientoOdontologico";
	
	/**
	 * Codigo para el log de la funcionalidad Conceptos Generales Glosas
	 * */
	public static final int logConceptosGeneralesGlosasCodigo = 227;
	
	/**
	 * Nombre para el log de la funcionalidad Conceptos Generales Glosas
	 */
	public static final String logConceptosGeneralesGlosasNombre = "logConceptosGeneralesGlosas";
	
	/**
	 * Codigo para el log de la funcionalidad Conceptos Especificos Glosas
	 * */
	public static final int logConceptosEspecificosGlosasCodigo = 228;
	
	/**
	 * Nombre para el log de la funcionalidad Conceptos Especificos Glosas
	 */
	public static final String logConceptosEspecificosGlosasNombre = "logConceptosEspecificosGlosas";
	

	
	/**
	 * Codigo para el log de la funcionalidad Aplicacion/Modificacion Pagos Facturas Varias
	 * */
	public static final int logAplicacionPagosFacturasVariasCodigo = 229;
	
	/**
	 * Nombre para el log de la funcionalidad Aplicacion/Modificacion Pagos Facturas Varias
	 */
	public static final String logAplicacionPagosFacturasVariasNombre = "logAplicacionPagosFacturasVarias";
	
	/**
	 * Codigo para el log de la consulta del total facturado por convenio contrato
	 */
	public static final int logTotalFacturadoConvenioContratoCodigo = 230;
	
	/**
	 * Nombre para el log de la consulta del total facturado por convenio contrato
	 */
	public static final String logTotalFacturadoConvenioContratoNombre = "logTotalFacturadoConvenioContrato";
	
	/**
	 * Codigo para el log de la consulta de glosas
	 */
	public static final int logListadoGlosasCodigo = 231;
	
	/**
	 * Nombre para el log de la consulta de glosas
	 */
	public static final String logListadoGlosasNombre = "logConsultarImprimirGlosas";
	
	/**
	 * Codigo para el log de la consulta de glosas
	 */
	public static final int logImpresionGlosaSolicitudCodigo = 232;
	
	/**
	 * Nombre para el log de la consulta de glosas
	 */
	public static final String logImpresionGlosaSolicitudNombre = "logConsultarImprimirDetalleGlosaFactura";
	
	/**
	 * Codigo para el log cuando se borra una excpciï¿½n centro de costo interconsulta
	 */
	public static final int logEliminarExceCCInterconsultasCodigo = 233;
	
	/**
	 * Nombre para el log cuando se borra una excpciï¿½n centro de costo interconsulta
	 */
	public static final String logEliminarExceCCInterconsultasNombre = "logEliminarExcepcionCCInterconsultas";
	
	/**
	 * Codigo para el log de la consulta de glosas
	 */
	public static final int logImpresionDetalleGlosaCodigo = 234;
	
	/**
	 * Nombre para el log de la consulta de glosas
	 */
	public static final String logImpresionDetalleGlosaNombre = "logImpresionDetalleGlosa";
	
	/**
	 * Codigo para el log al modificar la preglosa
	 */
	public static final int logModificacionPreglosaCodigo = 235;
	
	
	/**
	 * Nombre para el log al modificar la preglosa
	 */
	
	/**
	 * Log para Honorarios Especialidad Servicio
	 * 
	 */
	public static int logHonorariosEspecialidadServicios=242;
	/**
	 * Log para Honorarios Especialidad Servicio
	 * 
	 */
	
	public static int logContactosEmpresa=243;
	
	/**
	 * 
	 */
	public static int logEmisionBonosDescuentosOdontologicos=244;
	
	/**
	 * 
	 */
	public static int logAliadosOdontologicosCodigo=255;
	

	

	
	/**
	 * 
	 */

	public static String logContactosEmpresaNombre="logContactosEmpresaNombre";
	
	/**
	 * 
	 */

	public static String logEmisionBonosOdontologia="logEmisionBonosOdontologia";
	
	/**
	 * 
	 */
	public static String logEmisionBonosDescuentoOdontologico="logEmisionBonosDescuentoOdontologico";
	
	/**
	 * 
	 */
	
	public static String logHonorariosEspecialidadNombre="logHonorariosEspecialidad";
	/**
	 * 
	 * 
	 * 
	 */
	
	public static String logBeneficiariosNombre="logBeneficiarios";
	/**
	 * 
	 * 
	 * 
	 */
	
	public static String logHallazgoProgramaServicioNombre="logHallazgosPS";
	/**
	 * 
	 * 
	 * 
	 */
	public static final String logModificacionPreglosaNombre = "logModificacionPreglosa";
	
	/**
	 * codigo del log de soportes de facturas
	 */
	
	public static int logSoportesFacturasCodigo=248;
	
	/**
	 * 
	 */
	public static String logSoportesFacturasNombre=	"logSoportesFacturas";
	
	/**
	 * 
	 */
	public static String logAliadosOdontologicosNombre=	"logAliadosOdontologicos";
	/**
	 * 
	 */
	public static String logHonorariosPoolTarifasConvenios="logHonorariosPoolTarifasConvenios";
	/**
	 * LOG TRANSPORTADORA DE VALORES
	 */
	public static String logTransportadoraValoresN="logTransportadoraValores";
	
	/**
	 * LOG CUOTAS ODONTOLOGICAS 
	 */
	public static String logCuotaOdontologicas="logCuotasOdontologicas";
	
	
	//-------------------------------------------------------------------------------------------------------------------------------------------------------------------//
	public static int logHonorariosPooles=259;
	
	public static final int logTransportadoraValores=260;
	
	

	
	
	
	
	/**
	 * Define el tipo de registro de log insertar
	 */
	public static final int tipoRegistroLogInsercion = 1;

	/**
	 * Define el tipo de registro de log modificar
	 */
	public static final int tipoRegistroLogModificacion = 2;

	/**
	 * Define el tipo de registro de log eliminar
	 */
	public static final int tipoRegistroLogEliminacion = 3;
	/**
	 * 
	 */
	

	
	/****************************************************
	 * MODULO CUADRO DE TURNOS
	 **************************************************/
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * Categoria
	 */
	public static final String logCategoriaNombre = "logCategoria";

	/**
	 * C&oacute;digo del log de Categoria
	 */
	public static final int logCategoriaCodigo = 10000;

	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * Novedad
	 */
	public static final String logNovedadNombre = "logNovedad";
	
	/**
	 * C&oacute;digo del log de Novedad
	 */
	public static final int logNovedadCodigo = 10001;
	
	
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * Vacaciones
	 */
	public static final String logVacacionesNombre = "logVacaciones";
	
	/**
	 * C&oacute;digo del log de Novedad
	 */
	public static final int logVacacionesCodigo = 10002;
	
	

	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * Enfermeras en Categoria
	 */
	public static final String logEnfermeraCategoriaNombre = "logEnfermeraCategoria";
	
	/**
	 * C&oacute;digo del log de Novedad
	 */
	public static final int logEnfermeraCategoriaCodigo = 10003;
	
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar los logs de
	 * Novedad en enfermera
	 */
	public static final String logNovedadEnfermeraNombre = "logNovedadEnfermera";
	
	/**
	 * C&oacute;digo del log de Novedad
	 */
	public static final int logNovedadEnfermeraCodigo = 10004;
	
	
	/**
	 * Nombre al cual se le aï¿½adirï¿½ la fecha y la hora para manejar el archvio plano
	 * para nomina
	 */
	public static final String logNominaNombre = "logNomina";
	
	/**
	 * C&oacute;digo del log de Novedad
	 */
	public static final int logNominaCodigo = 10005;
	
	
	public static int logBeneficiarios=1006;
	
	
	public static int logHallazgosPS=1007;
	/**
	 * Log para Beneficiarios
	 * 
	 */
	
		
	/***************************************************************************
	 * C&oacute;digoS DE TIPOS MONTO
	 **************************************************************************/
	
	/**
	 * Constante que indica que el tipo de monto 
	 * es de copago 
	 */
	public static final int codigoTipoMontoCopago=1;
	
	/**
	 * Constante que indica que el tipo de monto 
	 * es cuota moderadora 
	 */
	public static final int codigoTipoMontoCuotaModeradora=2;

	/***************************************************************************
	 * TIPOS VALORACION INICIAL
	 **************************************************************************/

	/**
	 * Constante que dice que la valoraciï¿½n inicial no existe
	 */
	public static final int tipoValoracionInicialNoExiste = 0;

	/**
	 * Constante que dice que la valoraciï¿½n inicial no ha sido respondida
	 */
	public static final int tipoValoracionInicialNoRespondida = 1;

	/**
	 * Constante que dice que la valoraciï¿½n inicial es general de urgencias
	 */
	public static final int tipoValoracionInicialUrgenciasGeneral = 2;

	/**
	 * Constante que dice que la valoraciï¿½n inicial es general de
	 * hospitalizaciï¿½n
	 */
	public static final int tipoValoracionInicialHospitalizacionGeneral = 3;

	/**
	 * Constante que dice que la valoraciï¿½n inicial es pediatrica de urgencias
	 */
	public static final int tipoValoracionInicialUrgenciasPediatrica = 4;

	/**
	 * Constante que dice que la valoraciï¿½n inicial es pediatrica de
	 * hospitalizaciï¿½n
	 */
	public static final int tipoValoracionInicialHospitalizacionPediatrica = 5;

	/***************************************************************************
	 * CODIGOS ESPECIALIDADES VALORACIï¿½N: Estos C&oacute;digos permiten que diferentes
	 * tipos de valoraciï¿½n manejen conjuntos diferentes de examenes fï¿½sicos y
	 * similares
	 **************************************************************************/

	/**
	 * Constante que se refiere a la especialidad de la valoraciï¿½n general
	 */
	public static final int tipoEspecialidadValoracionGeneral = 0;

	/**
	 * Constante que se refiere a la especialidad de la valoraciï¿½n urgencias
	 */
	public static final int tipoEspecialidadValoracionUrgencias = 1;

	/**
	 * Constante que se refiere a la especialidad de la valoraciï¿½n de
	 * hospitalizaciï¿½n
	 */
	public static final int tipoEspecialidadValoracionHospitalizacion = 2;

	/**
	 * Constante que se refiere a la especialidad de la valoraciï¿½n de pediatrico
	 */
	public static final int tipoEspecialidadValoracionPediatrico = 3;

	/**
	 * Constante que se refiere a la especialidad de la valoraciï¿½n de pediatrico
	 * para valoraciï¿½n pediatrica en desarrollo por conductas
	 */
	public static final int tipoEspecialidadValoracionPediatricoDesarrolloConductas = 4;

	/**
	 * Constante con el error mï¿½ximo permitido al trabajar con variables de tipo
	 * double
	 */
	public static final double margenErrorDouble = 0.0000000000000000000001;

	/**
	 * Manejo de los origenes de pago
	 */
	public static int codigoOrigenPagoExterno = 2;

	/**
	 * Manejo de los origenes de pago
	 */
	public static int codigoOrigenPagoInterno = 1;

	/***************************************************************************
	 * C&oacute;digoS CONVENIOS
	 **************************************************************************/
	public static int codigoConvenioParticular = 31;

	/**
	 * Constante que nunca se usa en el sistema, ï¿½til para saber caso en que
	 * bï¿½squeda avanzada o acciï¿½n de usuario que no necesita respuesta
	 */
	public static int codigoNuncaValido = -1;
	

	/**
	 * Constante que nunca se usa en el sistema, ï¿½til para saber caso en que
	 * bï¿½squeda avanzada o acciï¿½n de usuario que no necesita respuesta
	 */
	public static long codigoNuncaValidoLong = -1;
	
	/**
	 * Constante que nunca se usa en el sistema, ï¿½til para saber caso en que
	 * bï¿½squeda avanzada o acciï¿½n de usuario que no necesita respuesta
	 */
	
	public static short codigoNuncaValidoShort = -1;
		
	/**
	 * Constante que nunca se usa en el sistema, ï¿½til para saber caso en que
	 * bï¿½squeda avanzada o acciï¿½n de usuario que no necesita respuesta
	 */
	public static double codigoNuncaValidoDouble = 0.0;
	
	/**
	 * Constante que nunca se usa en el sistema, ï¿½til para saber caso en que
	 * bï¿½squeda avanzada o acciï¿½n de usuario que no necesita respuesta
	 */
	public static double codigoNuncaValidoDoubleNegativo = -1.0;
	

	/***************************************************************************
	 * FORMATOS FECHAS
	 **************************************************************************/

	/**
	 * Constante que define el formato que utilizarï¿½ la aplicaciï¿½n si se quieren
	 * sacar datos en formato BD
	 */
	public static String formatoFechaBD = "YYYY-MM-DD";

	/**
	 * Constante que define el formato que utilizarï¿½ la aplicaciï¿½n si se quieren
	 * sacar datos en formato de la aplicaciï¿½n
	 */
	public static String formatoFechaAp = "DD/MM/YYYY";
	
	public static String formatoFechaApp = "dd/MM/yyyy";
	
	public static String formatoHora24BD = "HH24:MI";

	/**
	 * Constante que indica el C&oacute;digo de la naturaleza del paciente denominado
	 * "ninguno"
	 */
	public static int codigoNaturalezaPacientesNinguno = 0;
	
	public static int codigoNaturalezaPacienteSinNaturaleza = -1;
	
	public static String valorNaturalezaPacientesSinNaturaleza = "Sin Naturaleza";

	
	//Fin Anexo 959


	/***************************************************************************
	 * CONSTANTES DE LA NATURALEZA DE SERVICIOS
	 **************************************************************************/
	
	/**
	 * Acrï¿½nimo de la naturaleza de servicio para Consultas
	 */
	public static final  String codigoNaturalezaServicioConsultas = "01";
	
	/**
	 * Acrï¿½nimo de la naturaleza de servicio para Procedimientos de Diagnï¿½stico
	 */
	public static final String codigoNaturalezaServicioDiagnostico = "02";
		
	/**
	 * Acrï¿½nimo de la naturaleza de servicio para Procedimientos Terapeuticos no Quirurgicos 
	 */
	public static final String codigoNaturalezaServicioTerapeuticoNoQx = "03";
	
	/**
	 * Acrï¿½nimo de la naturaleza de servicio para Procedimientos Terapeuticos Quirurgicos 
	 */
	public static final String codigoNaturalezaServicioTerapeuticoQx = "04";
	
	/**
	 * Acrï¿½nimo de la naturaleza de servicio para Procedimientos de promociï¿½n y prevenciï¿½n
	 */
	public static final String codigoNaturalezaServicioPromocionPrevencion = "05";
	
	/**
	 * Acrï¿½nimo de la naturaleza de servicio para Estancias
	 */
	public static final String codigoNaturalezaServicioEstancias = "06";
	
	/**
	 * Acrï¿½nimo de la naturaleza de servicio para Honorarios
	 */
	public static final String codigoNaturalezaServicioHonorarios = "07";
	
	/**
	 * Acrï¿½nimo de la naturaleza de servicio Derechos de Sala
	 */
	public static final String codigoNaturalezaServicioDerechosSala = "08";
	
	/**
	 * Acrï¿½nimo de la naturaleza de servicio Materiales Insumos
	 */
	public static final String codigoNaturalezaServicioMaterialesInsumos = "09";
	 
	/**
	 * Acrï¿½nimo de la naturaleza de servicio Banco de Sangre
	 */
	public static final String codigoNaturalezaServicioBancoSangre = "10";
	
	/**
	 * Acrï¿½nimo de la naturaleza de servicio Traslado de Pacientes
	 */
	public static final String codigoNaturalezaServicioTrasladoPaciente = "14";
	
	/**
	 * Acrï¿½nimo de la naturaleza de servicio No Aplica
	 */
	public static final String codigoNaturalezaServicioNoAplica = "99";
	
	
	 

	
	/***************************************************************************
	 * CONSTANTES DE LA NATURALEZA DE ARTICULOS
	 **************************************************************************/

	/**
	 * Acrï¿½nimo de la naturaleza de articulo Medicamento POS
	 */
	public static final String acronimoNaturalezaArticuloMedicamentoPos = "1";

	/**
	 * Acrï¿½nimo de la naturaleza de articulo Medicamento POS
	 */
	public static final String acronimoNaturalezaArticuloMedicamentoNoPos = "2";

	/**
	 * Acrï¿½nimo de la naturaleza de articulo Medicamento POS
	 */
	public static final String codigoNaturalezaArticuloMedicamentoPos = "12";//ok
	
	/**
	 * Acrï¿½nimo de la naturaleza de articulo Medicamento No Pos
	 */
	public static final String codigoNaturalezaArticuloMedicamentoNoPos = "13";//estaba en 19, no coincidia con el archivo naturaleza_articulo 
	
	/**
	 * Acrï¿½nimo de la naturaleza de articulo Materiales e Insumos
	 */
	public static final String codigoNaturalezaArticuloMaterialesInsumos = "09";//ok

	/**
	 * Acronimo de la naturaleza de articulo Materiales Medicamentos
	 */
	public static final String codigoNaturalezaArticuloMaterialesMedicamentos = "02";
	
	/**
	 * Acrï¿½nimo de la naturaleza de articulo Protesis y Ortesis
	 */
	public static final String codigoNaturalezaArticuloProtesisOrtesis = "11";//ok
	
	/**
	 * Acrï¿½nimo de la naturaleza de articulo No Aplica
	 */
	public static final String codigoNaturalezaArticuloNoAplica = "99"; //ok

	
	/*******************************************************************************
	 * ACRONIMOS INVENTARIO TIPO DE ARTICULOS
	 */
	//Acronimo para Articulos Medicamentos
	public static final String acronimoTipoInventarioMedicamento = "M";
	//Acronimo para Articulos Elementos
	public static final String acronimoTipoInventarioElemento = "E";

	
	
	/***************************************************************************
	 * TIPOS AREA DE CONTROS DE COSTO *
	 **************************************************************************/

	/**
	 * C&oacute;digo del tipo de ï¿½rea del centro de costo directo
	 */
	public static int codigoTipoAreaDirecto = 1;

	/**
	 * C&oacute;digo del tipo de ï¿½rea del centro de costo indirecto
	 */
	public static int codigoTipoAreaIndirecto = 2;

	/**
	 * C&oacute;digo del tipo de ï¿½rea del centro de costo subalmacen
	 */
	public static int codigoTipoAreaSubalmacen = 3;

	/**
	 * C&oacute;digo del origen de la admisiï¿½n hospitalaria remitido
	 */
	public static int origenAdmisionHospitalariaRemitido = 3;

	/***************************************************************************
	 * ALCANCES DE CAMPO PARAMETRIZABLE *
	 **************************************************************************/

	/**
	 * Constante que indica que un campo parametrizable es de alcance Mï¿½dico
	 * (Este campo parametrizable aplica solo para el mï¿½dico)
	 */
	public static int campoParametrizableAlcanceMedico = 1;

	/**
	 * Constante que indica que un campo parametrizable es de alcance Centro de
	 * Costo (Este campo parametrizable aplica para el centro de costo que tenï¿½a
	 * el mï¿½dico al momento de aï¿½adirlo)
	 */
	public static int campoParametrizableAlcanceCentroCosto = 2;

	/**
	 * Constante que indica que un campo parametrizable es de alcance
	 * Instituciï¿½n (Este campo aplica para la instituciï¿½n que tenï¿½a el mï¿½dico al
	 * momento de aï¿½adirlo)
	 */
	public static int campoParametrizableAlcanceInstitucion = 3;

	/***************************************************************************
	 * TIPOS DE DATOS PEDIATRICO *
	 **************************************************************************/

	/**
	 * Constante que almacena el C&oacute;digo del tipo de desarrollo Psicomotor
	 */
	public static int codigoTipoDesarrolloPsicomotor = 1;

	/**
	 * Constante que almacena el C&oacute;digo del tipo de desarrollo del Lenguaje
	 */
	public static int codigoTipoDesarrolloLenguaje = 2;

	/***************************************************************************
	 * CODIGOS DE LOS ESTADOS DE LAS CAMAS *
	 **************************************************************************/

	public static int codigoEstadoCamaDisponible = 0;

	public static int codigoEstadoCamaOcupada = 1;

	public static int codigoEstadoCamaDesinfeccion = 2;

	public static int codigoEstadoCamaMantenimiento = 3;

	public static int codigoEstadoCamaFueraServicio = 4;
	
	public static int codigoEstadoCamaReservada = 5;
	
	public static int codigoEstadoCamaPendientePorTrasladar = 6;
	
	public static int codigoEstadoCamaPendientePorRemitir = 7;
	
	public static int codigoEstadoCamaConSalida = 8;
	
	/***************************************************************************
	 * NUMERO DE DECIMALES QUE DEBE MANEJAR EL UVR *
	 **************************************************************************/

	public static int numeroDecimalesUVR = 3;

	/***************************************************************************
	 * ACRONIMOS DE LOS METODOS DE AJUSTE *
	 **************************************************************************/

	public static String metodoAjusteDecena = "D";

	public static String metodoAjusteCentena = "C";

	public static String metodoAjusteUnidad = "U";
	
	public static String metodoSinAjuste = "S";

	/***************************************************************************
	 * STRING's RETORNADOS POR LA BASE DE DATOS PARTICULAR CASO BOOLEAN *
	 **************************************************************************/
	public static String valorFalseLargoString = "false";
	
	public static String valorFalseEnString = "f";
	
	public static String valorTrueLargoEnString = "true";

	public static String valorTrueEnString = "t";
	
	/******************************************************************************
	 * CONSTANTES CON LAS POSIBLES FACTURAS A GENERAR
	 * ****************************************************************************/
	
	/**
	 * Generar una sola factura para las dos cuentas
	 */
	public static int codCasoAsocioCuentasNoDistribuidaEnUnaFactura = 1;
	
	/**
	 * Generar facturas independientemente (1-Urgencias  1-hospitalizaciï¿½n)
	 */
	public static int codCasoAsocioCuentasNoDistribuidaEnDifFacturas = 2;
	
	/**
	 * Generar una sola factura
	 */
	public static int codCasoSinAsocioCuentasNoDistribuida = 3;
	
	/**
	 * Generar tantas facturas como defina el facturador al iniciar el proceso
	 */
	public static int codCasoCuentaDistribuidaConOSinAsocio = 4;
	
	/******************************************************************************
	 * CONSTANTES CON LOS POSIBLES TIPOS DE CARGO
	 * ****************************************************************************/
	
	/**
	 * C&oacute;digo del tipo de cargo para Servicios
	 * SI CAMBIA ESTE DATO NO OLVIDE MODIFICAR
	 * LAS IMPLEMENTACIONES DE LA FUNCIï¿½N
	 * getNombrePropDetFact.fun y la tabla tipos_cargo
	 */
	public static int codigoTipoCargoServicios=1;
	
	/**
	 * C&oacute;digo del tipo de cargo para Artï¿½culos
	 * SI CAMBIA ESTE DATO NO OLVIDE MODIFICAR
	 * LAS IMPLEMENTACIONES DE LA FUNCIï¿½N
	 * getNombrePropDetFact.fun y la tabla tipos_cargo
	 */
	public static int codigoTipoCargoArticulos=2;
	
	/******************************************************************************
	 * CONSTANTES PARA LA GENERACIï¿½N DE LA FACTURA
	 * ****************************************************************************/	
	
	public static String descripcionGenracionFacturaAutomatica= "FACTURACION AUTOMATICA";
	
	/******************************************************************************
	 * CONSTANTES PARA ESTADOS PARA FACTURACION
	 * ****************************************************************************/
	/**
	 * C&oacute;digos de los estados factura tabla= 'estados_factura_f'
	 */
	public static int codigoEstadoFacturacionFacturada=1;
	public static int codigoEstadoFacturacionAnulada=2;
	
	/**
	 * C&oacute;digos de los estados factura paciente tabla= 'estados_factura_paciente'
	 */
	public static int codigoEstadoFacturacionPacientePorCobrar=1;
	public static int codigoEstadoFacturacionPacienteCancelada=2;
	public static int codigoEstadoFacturacionPacienteAnulada=3;
	public static int codigoEstadoFacturacionPacienteConDevolucion=4;
	public static int codigoEstadoFacturacionPacienteSinValorPaciente=5;
	
	/**
	 * String con el permiso necesario para abrir una subcuenta
	 * solo para inserciï¿½n (En el resto de casos debe tener
	 * cuentaMadre y cuentaHija como referencias)
	 */
	public static String permisoNuevaSubCuentaParaInsercion="Permiso Entregado";
	
	/**
	 * Acrï¿½nimo para el diagnï¿½stico no seleccionado
	 */
	public static String acronimoDiagnosticoNoSeleccionado="1";
	
	/**
	 * Tipo cie para un diagnï¿½stico no seleccionado
	 */
	public static int codigoCieDiagnosticoNoSeleccionado=0;
	
	/**
	 * C&oacute;digos de tipos movimiento de la tabla = 'tipos_movimiento'
	 */
	public static int codigoTipoMovimientoAnulacion=1;
	public static int codigoTipoMovimientoDevolucion=2;
	
	
	/*************************************************************************************
	 * CONSTANTES PARA ESTADOS PARA CARTERA
	 *  ********************************************************************************/
	/**
	 * C&oacute;digos de los estados cartera de la tabla = 'estados_cartera'
	 */
	public static int codigoEstadoCarteraGenerado=1; 
	public static int codigoEstadoCarteraAnulado=2;
	public static int codigoEstadoCarteraAprobado=3;
	public static int codigoEstadoCarteraRadicada=4;
	
	/**
	 * Codigos de los tipos de ajuestes de la tabla = 'tipos_ajustes'
	 */
	public static int codigoAjusteDebitoFactura=1;
	public static int codigoAjusteCreditoFactura=2;
	public static int codigoAjusteDebitoCuentaCobro=3;
	public static int codigoAjusteCreditoCuentaCobro=4;
	public static int codigoConceptosCarteraDebito=5;
	public static int codigoConceptosCarteraCredito=6;
	
	/**
	 * Constantes para Tipo de Pago
	 */
	public static String acronimoTipoPagoTotal = "T";
	public static String acronimoTipoPagoParcial = "P";
	
	public static InfoDatos ajustesDebitoFuncionalidadAjustes=new InfoDatos("DB","Débito");
	public static InfoDatos ajustesCreditoFuncionalidadAjustes=new InfoDatos("CR","Crédito");
	
	public static InfoDatos tiposTarjetaDebito=new InfoDatos("D","Débito");
	public static InfoDatos tiposTarjetaCredito=new InfoDatos("C","Crédito");
	
	
	public static InfoDatos ajusteTotal=new InfoDatos("T","Total");
	public static InfoDatos ajusteParcia=new InfoDatos("P","Parcial");
	public static InfoDatos ajusteSinAjuste=new InfoDatos("N","No Ajuste");
	
	
	public static InfoDatos tipoPagoTotal=new InfoDatos("T","Pago Total");
	public static InfoDatos tipoPagoParcial=new InfoDatos("P","Pago Parcial");
	public static InfoDatos tipoPagoNoPago=new InfoDatos("N","No Pago");
	/**
	 * Tipo de Anulacion de Movimientos de CXC,
	 * Anulacion
	 */	
	public static int codigoAnulacionCuentaCobro=1;
	/**
	 * Tipo de Anulacion de Movimientos de CXC,
	 * Devolucion
	 */	
	public static int codigoDevolucionCuentaCobro=2;
	
	/*************************************************************************************
	 * CONSTANTES SEPARATOR
	 * ***********************************************************************************
	 **/
	
	/**
	 * Separator para hacer split de los resultados de los tags
	 */
	public static String separadorTags = "@@@@@";
	
	/**
	 * Separator para split.
	 */
	public static String separadorSplit = "@@@@@";
	
	/**
	 * Separator para split.
	 */
	public static String separadorSplitComplejo = "--@@##&&##@@--";
	
	
	/*************************************************************************************
	 * CONSTANTES PARA ESTADOS PARA	LOS TIPOS DE CONTRATO
	 *  ********************************************************************************/
	/**
	 * Contrato Capitado
	 */
	public static int codigoTipoContratoCapitado=1;
	/**
	 * Normal
	 */
	public static int codigoTipoContratoEvento=2;
	
	
	/**************************************************************************************
	 * CONSTANTES TIPOS Dï¿½AS FESTIVOS
	 **************************************************************************************/
	/**
	 * Tipo dia festivo estï¿½tico
	 */
	public static int codigoTipoDiaFestivoEstatico=1;
	
	/**
	 * Tipo dia festivo variable
	 */
	public static int codigoTipoDiaFestivoVariable=2;
	
	/***************************************************************************************
	 * CONSTANTES TIPO RETENCION
	 * *************************************************************************************/
	/**
	 * Tipo Retencion Normal declarante
	 */
	public static int codigoTipoRetencionNormalDeclarante= 0;
	
	/**
	 * Nombre del consecutivo para el manejo de cuentas de cobro.
	 */
	public static String nombreConsecutivoCuentasCobro="consecutivo_cuentas_cobro";
	
	/**
	 * Nombre del consecutivo para el manejo de facturas.
	 */
	public static String nombreConsecutivoFacturas="consecutivo_facturas";
	
	/**
	 * Nombre del consecutivo para los ajustes debito.
	 */
	public static String nombreConsecutivoAjustesDebito="consecutivo_ajustes_debito";
	
	/**
	 * Nombre para manejar el consecutivo para los ajustes creditos
	 */
	public static String nombreConsecutivoAjustesCredito="consecutivo_ajustes_credito";

	/**
	 * Nombre del consecutivo para el manejo de admisiï¿½n hospitalaria.
	 */
	public static String nombreConsecutivoAdmisionHospitalizacion="consecutivo_admision_hospitalizacion";
	
	/**
	 * Nombre del consecutivo para el manejo de admisiï¿½n urgencias.
	 */
	public static String nombreConsecutivoAdmisionUrgencias="consecutivo_admision_urgencias";
	/**
	 * Nombre del consecutivo para el manejo de pedidos de inventarios.
	 */
	public static String nombreConsecutivoPedidosInventarios="consecutivo_pedidos_inventarios";
	/**
	 * Nombre del consecutivo para el manejo de recibo de caja.
	 */
	public static String nombreConsecutivoReciboCaja="consecutivo_recibo_caja";
	
	
	/**
	 * Nombre del consecutivo para el manejo de recibo de caja.
	 */
	public static String nombreConsecutivoDevolucionReciboCaja="consecutivo_devolucion_recibo_caja";
	
	/**
	 * Nombre del consecutivo para el manejo de triage.
	 */
	public static String nombreConsecutivoTriage="consecutivo_triage";
	/**
	 * Nombre del consecutivo para la anulaciï¿½n de la factura.
	 */
	public static String nombreConsecutivoAnulacionFactura="consecutivo_anulacion_factura";
	
	/**
	 * Nombre del consecutivo para la anulaciï¿½n de Recibos de Caja
	 */
	public static String nombreConsecutivoAnulacionRecibosCaja="consecutivo_anulacion_recibos_caja";
	
	/**
	 * Nombre del consecutivo para la generaciï¿½n del presupuesto
	 */
	public static String nombreConsecutivoPresupuesto="consecutivo_presupuesto";
	/**
	 * Nombre del consecutivo para Acturizacion Entidad Subcontratada
	 */
	public static String nombreConsecutivoAutorizacionEntiSub="concecutivo_autorizacion_entidades_subcontratadas";
	/**
	 * Nombre del consecutivo para el ajuste de costos de inventarios
	 */
	public static String nombreConsecutivoAjusteCostoInv="consecutivo_ajuste_costo_inv";
	
	
	/**
	 * Nombre del consecutivo para el ajuste de costos de inventarios
	 */
	public static String nombreConsecutivoAmparosXReclamar="consecutivo_reclamacion_amparo_acc_trans_even_cat";

	/**
	 * Nombre del consecutivo para el arqueos definitivos de caja
	 */
	public static String nombreConsecutivoArqueosEntregaParcial="consecutivo_arqueo_entrega_parcial";

	/**
	 * Nombre del consecutivo para el arqueos caja
	 */
	public static String nombreConsecutivoArqueosCaja="consecutivo_arqueo_caja";

	/**
	 * Nombre del consecutivo para el entrega transportadora
	 */
	public static String nombreConsecutivoEntregaTransportadora="consecutivo_entrega_a_transportadora";

	/**
	 * 
	 */
	public static String nombreConsecutivoTrasladoAbonosPaciente="consecutivo_traslado_abono_paciente";
	
	/**
	 * 
	 */
	public static String nombreConsecutivoEntregaCajaMayor="consecutivo_entrega_caja_mayor_principal";
	
	/**
	 * Nombre del consecutivo para el movimiento de Solicitud de Traslado a Caja de Recaudo
	 */
	public static String nombreConsecutivoTrasladoCajas="consecutivo_traslado_cajas";
	
	/**
	 * 
	 */
	public static String nombreConsecutivoRegistroFaltanteSobrante="consecutivo_registro_faltante_sobrante";
	
	/**
	 * 
	 */
	public static String nombreConsecutivoArqueoCaja="consecutivo_arqueo_caja";
	
	/**
	 * Nombre del consecutivo para el cierre de caja
	 */
	public static String nombreConsecutivoCierreCaja="consecutivo_cierre_turno_caja";
	
	/**
	 * Nombre del consecutivo cuentas de cobro capitaciï¿½n
	 */
	public static String nombreConsecutivoCuentaCobroCapitacion="consecutivo_cuenta_cobro_capitacion";
	
	/**
	 * Nombre del consecutivo para los ajustes debito.
	 */
	public static String nombreConsecutivoAjustesDebitoCapitacion="consecutivo_ajustes_debito_capitacion";
	
	/**
	 * Nombre para manejar el consecutivo para los ajustes creditos
	 */
	public static String nombreConsecutivoAjustesCreditoCapitacion="consecutivo_ajustes_credito_capitacion";

	/**
	 * Nombre para manejar el consecutivo para los ajustes creditos
	 */
	public static String nombreConsecutivoTrasladosCaja="consecutivo_traslados_caja";
	
	/**
	 * Nombre para manejar el consecutivo para referencia
	 */
	public static String nombreConsecutivoReferencia="consecutivo_referencia";
	
	/**
	 * Nombre para manejar el consecutivo para contrarreferencia
	 */
	public static String nombreConsecutivoContrarreferencia="consecutivo_contrarreferencia";
	
	/**
	 * Nombre para manejar el consecutivo para historia clinica del paciente
	 */
	public static String nombreConsecutivoHistoriaClinica="consecutivo_historia_clinica";
	
	/**
	 * Nombre para manejar el consecutivo para el documento en garantia Cheque
	 * */
	public static String nombreConsecutivoCheque = "consecutivo_cheque";
	
	/**
	 * Nombre para manejar el consecutivo para el documento en garantia Letra de Cambio
	 * */
	public static String nombreConsecutivoLetraCambio = "consecutivo_letra_cambio";
	
	/**
	 * Nombre para manejar el consecutivo para el documento en garantia pagare
	 * */
	public static String nombreConsecutivoPagare = "consecutivo_pagare";
	
	/**
	 * Nombre para manejar el consecutivo para el documento en garantia voucher
	 * */
	public static String nombreConsecutivoVoucher = "consecutivo_voucher";
	
	/**
	 * Nombre para manejar el consecutivo para el documento en garantia a Paz Y Salvo
	 * */
	public static String nombreConsecutivoPazYSalvo = "consecutivo_paz_y_salvo";
	
	/**
	 * Nombre para manejar el consecutivo de incapacidades
	 * */
	public static String nombreConsecutivoIncapacidades = "consecutivo_incapacidades"; 
	
	/**
	 * Nombre para manejar el consecutivo para ingresos del paciente
	 * 
	 */
	public static String nombreConsecutivoIngresos="consecutivo_ingreso";
	
	/**
	 * Nombre para manejar el consecutivo para Registros Pacientes Entidades SubContratadas
	 * */
	public static String nombreConsecutivoRegistrosPacientesEntidadesSub = "consecutivo_registro_pacientes_entidades_subcontratadas";

	/**
	 * Nombre para manejar el consecutivo para nombreConsecutivoJustificacionNOPOSServicios
	 * */
	public static String nombreConsecutivoJustificacionNOPOSServicios = "consecutivo_justificacion_nopos_servicios";

	/**
	 * Nombre para manejar el consecutivo Inclusiï¿½n Programa / Servicio Odontolï¿½gico
	 */
	public static String nombreConsecutivoInclusionProgServOdo = "consecutivo_inclusion_prog_serv_odo";
	
	/**
	 * Nombre para manejar el consecutivo Exclusiï¿½n Programa / Servicio Odontolï¿½gico
	 */
	public static String nombreConsecutivoExclusionProgServOdo = "consecutivo_exclusion_prog_serv_odo";
	
	/**
	 * Nombre para manejar el consecutivo Inclusiï¿½n Programa / Servicio Odontolï¿½gico
	 */
	public static String nombreConsecutivoSolicitudDescuentoOdo = "consecutivo_sol_descuento_odo";
	
	/**
	 * Nombre para manejar el consecutivo para nombreConsecutivoJustificacionNOPOSArticulos
	 * */
	public static String nombreConsecutivoJustificacionNOPOSArticulos = "consecutivo_justificacion_nopos_articulos";

	/**
	 * Nombre del consecutivo para el manejo de facturas varias.
	 */
	public static String nombreConsecutivoFacturasVarias="consecutivo_facturas_varias";
	
	/**
	 * Nombre del consecutivo para la anulaciï¿½n de la factura.
	 */
	public static String nombreConsecutivoAnulacionFacturaVaria="consecutivo_anulacion_factura_varia";
	
	/**
	 * Nombre del consecutivo para los ajustes debito de facturas varias.
	 */
	public static String nombreConsecutivoAjustesDebitoFacturasVarias="consecutivo_ajustes_debito_facturas_varias";
	
	/**
	 * Nombre para manejar el consecutivo para los ajustes creditos de facturas varias.
	 */
	public static String nombreConsecutivoAjustesCreditoFacturasVarias="consecutivo_ajustes_credito_facturas_varias";
	
	/**
	 * Nombre para manejar el consecutivo para las glosas
	 */
	public static String nombreConsecutivoGlosas = "consecutivo_glosas";
	
	/**
	 * Nombre para manejar el consecutivo de las Respuestas Glosa
	 */
	public static String nombreConsecutivoRespuestaGlosas = "consecutivo_respuesta_glosas";
	
	/**
	 * Nombre para manejar el consecutivo Informe atencion inicial de urgencias
	 */
	public static String nombreConsecutivoInformeAtencIniUrg = "consecutivo_infor_atenc_inic_urg";
	
	/**
	 * Nombre para manejar el consecutivo Informe inconsistencia verificacion base de datos
	 */
	public static String nombreConsecutivoInformeInconVeribd = "consecutivo_infor_incon_veribd";
	
	/**
	 * Nombre para manejar el consecutivo Solicitud Autorizaciones
	 */
	public static String nombreConsecutivoSolicitudAutori = "consecutivo_solicitud_autori";
	
	/**
	 * 
	 */
	public static String nombreConsecutivoVentasTarjetasCliente="consecutivo_venta_tarjeta_cli";
	
	/**
	 * 
	 */
	public static String nombreConsecutivoContratoPresupustoOdontologico="consecutivo_contrato_asoc_presupuesto";
	
	/**
	 * 
	 */
	public static String nombreConsecutivoAutorizacionPoblacionCapitada="consecutivo_autorizacion_poblacion_capitada";
	
	/**
	 * 
	 */
	public static String nombreConsecutivoOrdenesAmbulatorias="consecutivo_ordenes_ambulatorias";
	
	/**
	 * Nombre del consecutivo para el notas dï¿½bito pacientes.
	 */
	public static String nombreConsecutivoNotasDebitoPacientes="consecutivo_notas_debito_pacientes";
	
	/**
	 * Nombre del consecutivo para el notas crï¿½dito pacientes.
	 */
	public static String nombreConsecutivoNotasCreditoPacientes="consecutivo_notas_credito_pacientes";
	
	/**************************************************************************************
	 * CONSTANTES RIPS
	 * *****************************************************************************/
	/**
	 * Prefijos de los archivos RIPS
	 */
	public static String ripsCT= "CT";
	public static String ripsAF= "AF";
	public static String ripsAD= "AD";
	public static String ripsUS= "US";
	public static String ripsAC= "AC";
	public static String ripsAH= "AH";
	public static String ripsAM= "AM";
	public static String ripsAN= "AN";
	public static String ripsAP= "AP";
	public static String ripsAT= "AT";
	public static String ripsAU= "AU";
	/**
	 * Prefijo de archivo de inconsistencias
	 */
	public static String ripsInconsistencias="Incon";
	
	/**************************************************************************************
	 * CONSTANTES FORECAT
	 * *****************************************************************************/
	/**
	 * Prefijos de los archivos FORECAT
	 */
	public static String forecatAC= "AC";
	public static String forecatAA= "AA";
	public static String forecatVH= "VH";
	public static String forecatAV= "AV";
	
	
	/*-------------------------EXTENSIONES PARA LA VALORACION---------------------------*/
	/**
	 * Manejo de las extensiones de las valoraciones
	 * caso valoracion sin extension
	 */
	public static int codigoExtensionValoracionSinExtension=0;
	
	/**
	 * Manejo de las extensiones de las valoraciones
	 * caso valoracion Gineco - Obstetrica
	 */
	public static int codigoExtensionValoracionGineco=1;
	
	/**
	 * Manejo de las extensiones de las valoraciones
	 * caso valoracion Odontologia
	 */
	public static int codigoExtensionValoracionOdontologia=2;
	
	/**
	 * Manejo de las extensiones de las valoraciones
	 * caso valoracion Oftalmologia
	 */
	public static int codigoExtensionValoracionOftalmologica=3;

	/*-------------------------RANGOS EDAD MENARQIA Y MENOPAUSIA---------------------------*/
	
	public static int codigoRangoEdadMenarquiaNoDefinido=-1;
	public static int codigoRangoEdadMenarquia8a11=1;
	public static int codigoRangoEdadMenarquia12a15=2;
	public static int codigoRangoEdadMenarquia16a18=3;
	public static int codigoRangoEdadMenarquiaOtra=0;
	
	
	/*--------------------------CODIGOS TIPOS PARTO ------------------------*/
	public static int codigoTipoPartoForceps=1;
	public static int codigoTipoPartoEspatula=2;
	public static int codigoTipoPartoVaginal=3;
	public static int codigoTipoPartoMortinato=4;
	public static int codigoTipoPartoCesarea=5;
	public static int codigoTipoPartoOtro=0;
	
	public static int codigoTipoTrabajoPartoOtro=0;
	
	
	
	/*-----------------------------CODIGOS RESTRICCIONES CUADRO DE TURNOS-----------------------*/
	public static int codigoRestriccionM=1;//una enfermera no puede hacer Maï¿½anas
	public static int codigoRestriccionT=2;//una enfermera no puede hacer Tardes
	public static int codigoRestriccionN=3;//una enfermera no puede hacer Noches
	public static int codigoRestriccionC=4;//una enfermera no puede hacer Corridos
	public static int codigoRestriccionNN=5;//una enfermera puede hacer dos noches seguidas
	public static int codigoRestriccionMinM=6;//Minimo de enfermeras en la Maï¿½ana dia Ordinario
	public static int codigoRestriccionMinT=7;//Minimo de enfermeras en la tarde dia Ordinario
	public static int codigoRestriccionesEnfN=8;//Minimo de enfermeras en la Noche dia Ordinario
	public static int codigoRestriccionMinMFestivo=9;//Minimo de enfermeras en la Maï¿½ana dia Festivo
	public static int codigoRestriccionMinTFestivo=10;//Minimo de enfermeras en la tarde dia Festivo
	public static int codigoRestriccionMinNFestivo=11;//Minimo de enfermeras en la Noche dia Festivo
	
	
	
	
	
	//-------------------CODIGOS TIPOS DE TURNOS QUE PUEDE HACER UNA ENFERMERA---------------
	
	public static int codigoTodos=0;//puede hacer todos los turnos
	public static int codigoCMNT=1;//una enfermera no puede hacer ningun turno
	public static int codigoCMT=2;//una enfermera solo puede hacer noches
	public static int codigoCNT=3;//una enfermera solo puede hacer maï¿½anas
	public static int codigoMNT=4;//una enfermera solo puede hacer corridos
	public static int codigoCM=5;//una enfermera solo puede hacer noches y tardes
	public static int codigoCN=6;//una enfermera solo puede hacer maï¿½anas y tardes
	public static int codigoCT=7;//Una enfermera solo puede hacer noches y maï¿½anas
	public static int codigoMN=8;//Una enfermera solo puede hacer corridos y tardes
	public static int codigoMT=9;//Una enfermera solo puede hacer corridos y noches
	public static int codigoNT=10;//Una enfermera solo puede hacer corridos y maï¿½anas
	public static int codigoC=11;//Una enfermera no puede hacer corridos
	public static int codigoM=12;//Una enfermera no puede hacer Manaï¿½as
	public static int codigoT=13;//Una enfermera no puede hacer Tardes
	public static int codigoN=14;//Una enfermera no puede hacer Noches
	public static int codigoCMN=15;//una enfermera solo puede hacer noches
	
	
	
	
	/*--------------------------------------CONSTANTES PARA LOS METODOS DE AJUSTES Cartera-----------------------------------*/
	
	public static String tipoMetodoAjusteCarteraManual="M";
	public static String tipoMetodoAjusteCarteraAutomatico="A";
	public static String tipoMetodoAjusteCarteraPorcentual="P";
	
	/*--------------------------------------CONSTANTES PARA LOS METODOS DE PAGOS Cartera-----------------------------------*/
	
	public static int tipoMetodoPagoManual=3;
	public static int tipoMetodoPagoAutomatico=2;
	public static int tipoMetodoPagoPorcentual=1;
	
	
	
	
	/*--------------------------------------CONSTANTES EN LA HOJA OBSTETRICA-----------------------------------*/
	
	
	/**
	 * C&oacute;digo para postular la edad gestacional de la informaciï¿½n del embarazo en el campo 
	 * edad gestacional de la secciï¿½n ultrasonidos de la hoja obstï¿½trica 
	 */
	public static int codigoPostularEdadGestacionalUltrasonido=1;
	
	/**
	 * C&oacute;digo para postular la edad gestacional de ultrasonidos en la  
	 * edad gestacional eco de la secciï¿½n ultrasonidos de la hoja obstï¿½trica 
	 */
	public static int codigoPostularEdadGestEcoUltrasonido=2;
	
	/**
	 * Codigo del tipo de ultrasonido llamado observaciones
	 */
	public static int codigoObservacionesUltrasonido=4;
	
	/**
	 * C&oacute;digo del tipo de resumen llamado altura uterina
	 */
	public static int codigoAlturaUterina=7;
	
	/**
	 * C&oacute;digo del tipo de resumen gestacional llamado Peso
	 */
	public static int codigoTipoResumenGestacionalPeso=5;
	
	/**
	 * C&oacute;digo del tipo de resumen gestacional llamado Talla
	 */
	public static int codigoTipoResumenGestacionalTalla=15;
	
	
	/**************************************************************************************
	 * CONSTANTES TIPOS MOVIMIENTOS DE ABONOS
	 * *****************************************************************************/
	/**
	 * C&oacute;digo del tipo de movimiento de abono al ingresar un recibo de caja
	 * con concepto de abono
	 */
	public static int tipoMovimientoAbonoIngresoReciboCaja=1;
	
	/**
	 * C&oacute;digo del tipo de movimiento de abono al generar factura
	 */
	public static int tipoMovimientoAbonoFacturacion=2;
	
	/**
	 * C&oacute;digo del tipo de movimiento de abono al anular factura
	 */
	public static int tipoMovimientoAbonoAnulacionFactura=3;
	
	/**
	 * C&oacute;digo del tipo de movimiento de abono al anular recibo caja
	 */
	public static int tipoMovimientoAbonoAnulacionReciboCaja=4;
	
	/**
	 * Reservar abonos del paciente
	 */
	public static int tipoMovimientoAbonoSalidaReservaAbono=5;
	
	/**
	 * Hacer efectiva reserva abonos del paciente
	 */
	public static int tipoMovimientoAbonoEntradaReservaAbono=6;
	
	/**
	 * Anular reserva abonos del paciente
	 */
	
	public static int tipoMovimientoAbonoAnulacionReservaAbono=7;
	/**
	 * C&oacute;digo del tipo de movimiento de abono trasladar abonos pacientes
	 */
	
	public static int tipoMovimientoAbonoIngresoPorTraslado=8;
	
	/**
	 * Cï¿½digo del tipo de movimiento de abono al trasladar abonos pacientes
	 */
	
	public static int tipoMovimientoAbonoSalidaPorTraslado=9;
	/**
	  * C&oacute;digo del tipo de movimiento de abono para migracion
	 */
	
	public static int tipoMovimientoAbonoSaldoInicialPositivo=10;
	
	/**
	 * C&oacute;digo del tipo de movimiento de abono para migracion
	 */
	public static int tipoMovimientoAbonoSaldoInicialNegativo=11;
	
	/**
	  * C&oacute;digo del tipo de movimiento de abono para migracion
	 */
	
	public static int tipoMovimientoAbonoAjusteSaldoInicialPositivo=12;
	
	/**
	 * C&oacute;digo del tipo de movimiento de abono para migracion
	 */
	public static int tipoMovimientoAbonoAjusteSaldoInicialNegativo=13;
	
	/**
	 * C&oacute;digo del tipo de movimiento de abono para devolucion
	 */
	
	public static int tipoMovimientoAbonoDevolucionAbono=14;
	
	/**
	 * C&oacute;digo del tipo de movimiento de abono al devolver recibo caja
	 */
	public static int tipoMovimientoAbonoDevolucionReciboCaja=15;
	
	//public static int tipoMovimientoSaldoInicialMigrado=16;
	
	//public static int tipoMovimientoAjusteMigracionPositivo=17;
	
	//public static int tipoMovimientoAjusteMigracionNegativo=18;
	
	/**
	 * C&oacute;digo del tipo de movimiento de Salida por Nota Dï¿½bito Paciente
	 */
	public static int tipoMovimientoSalidaNotaDebitoPaciente=16;
	
	/**
	 * C&oacute;digo del tipo de movimiento de Entrada por Nota Crï¿½dito Paciente
	 */
	public static int tipoMovimientoEntradaNotaCreditoPaciente=17;

	
	
	/**************************************************************************************
	 * CONSTANTES TIPOS ESTANCIAS AUTOMï¿½TICAS
	 * *****************************************************************************/
	/**
	 * C&oacute;digo del tipo estancia por ï¿½rea
	 */
	public static int codigoTipoEstanciaPorArea=1;
	/**
	 * C&oacute;digo del tipo estancia por paciente
	 */
	public static int codigoTipoEstanciaPorPaciente=2;
	
	/**************************************************************************************
	 * CONSTANTES PARA TIPOS DE ANTECEDENTES 
	 * *****************************************************************************/

	/**
	 * C&oacute;digo de los antecedentes personales
	 */
	public static int codigoTipoAntecedentePersonal=1;
	/**
	 * C&oacute;digo de los antecedentes Familiares
	 */
	public static int codigoTipoAntecedenteFamiliar=2;
	/**
	 * C&oacute;digo de los antecedentes para AMBOS (personales y familiares)
	 */
	public static int codigoTipoAntecedentePerFam=3;
	
	/*********************************************************
	 * CONSTANTES DEL MODULO EPIDEMIOLOGICO
	 *********************************************************/
	
	public static int codigoOpcionBasicaNo=1;
	public static int codigoOpcionBasicaSi=2;
	public static int codigoOpcionBasicaDesconocido=3;
	
	public static int codigoTipoNotificacionIndividual=1;
	public static int codigoTipoNotificacionColectiva=2;
	
	public static int codigoUbicacionObservable=1;
	public static int codigoUbicacionNoObservable=2;
	public static int codigoUbicacionMuerto=3;
	
	public static int codigoManejoAnimalObservar=1;
	
	public static int codigoManejoHeridaSutura=2;
	
	public static int codigoFichaMalaria=1;
	public static int codigoFichaHepatitis=2;
	public static int codigoFichaIntoxicacionPlaguicidas=3;
	public static int codigoFichaVIH=4;
	public static int codigoFichaSarampion=5;
	public static int codigoFichaRabia=6;
	public static int codigoFichaFiebreAmarilla=7;
	public static int codigoFichaIntoxicacionAlimentaria=8;
	public static int codigoFichaRubeola=9;
	public static int codigoFichaViolenciaIntrafamiliar=10;
	public static int codigoFichaDengue=11;
	public static int codigoFichaParalisisFlacida=12;
	public static int codigoFichaSifilisCongenita=13;
	public static int codigoFichaTetanosNeo=14;
	public static int codigoFichaColera=15;
	public static int codigoFichaMeningitisMeningo=16;
	public static int codigoFichaMeningitisHemofilos=17;
	public static int codigoFichaTetanosAdulto=18;
	public static int codigoFichaDifteria=19;
	public static int codigoFichaHepatitisA=20;
	public static int codigoFichaParotiditis=21;
	public static int codigoFichaTosferina=22;
	public static int codigoFichaTuberculosis=23;
	public static int codigoFichaVaricela=24;
	public static int codigoFichaHepatitisC=25;
	public static int codigoFichaMortalidadMaterna=26;
	public static int codigoFichaMortalidadPerinatal=27;
	public static int codigoFichaInfecciones=28;
	public static int codigoFichaLesiones=29;
	public static int codigoFichaIntoxicaciones=30;
	public static int codigoFichaRubCongenita=31;
	public static int codigoFichaLepra=32;
	public static int codigoFichaAcciOfidico=33;
	public static int codigoFichaRabiaAnimal=34;
	public static int codigoFichaRabiaHumana=35;
	public static int codigoFichaEasv=36;
	public static int codigoFichaEsi=37;
	public static int codigoFichaDengueHemo=38;
	public static int codigoFichaEtas=39;
	public static int codigoFichaLeishmaniasis=40;
	public static int codigoFichaMeningitis=41;
	
	public static int codigoEstadoFichaIncompleta=1;
	public static int codigoEstadoFichaCompleta=2;
	public static int codigoEstadoFichaPendienteLaboratorio=3;
	public static int codigoEstadoFichaPendienteSeguimiento=4;
	
	public static int codigoNotificacionIndividual=1;
	public static int codigoNotificacionColectiva=2;
	
	public static String nombreNotificacionIndividual="INDIVIDUAL";
	public static String nombreNotificacionColectiva="COLECTIVA";
	
	public static int codigoFuncionalidadNotificarCasos = 30000;
	
	
	public static int codigoMorbilidadColera = 1;
	public static int codigoMorbilidadDengueClasico = 2;
	public static int codigoMorbilidadDengueHemorragico = 3;
	public static int codigoMorbilidadDifteria = 4;
	public static int codigoMorbilidadEtas = 5;
	public static int codigoMorbilidadExpoRabica = 6;
	public static int codigoMorbilidadFiebreAmarilla = 7;
	public static int codigoMorbilidadHepatitisA = 8;
	public static int codigoMorbilidadHepatitisB = 9;
	public static int codigoMorbilidadHepatitisC = 10;
	public static int codigoMorbilidadIntoxOh = 11;
	public static int codigoMorbilidadIntoxQuim = 12;
	public static int codigoMorbilidadLepra = 13;
	public static int codigoMorbilidadMalaria = 14;
	public static int codigoMorbilidadMeningitis = 15;
	public static int codigoMorbilidadParalisisFlac = 16;
	public static int codigoMorbilidadParotiditis = 17;
	public static int codigoMorbilidadPeste = 18;
	public static int codigoMorbilidadRabiaAnimal = 19;
	public static int codigoMorbilidadRabiaHumana = 20;
	public static int codigoMorbilidadReaccionPost = 21;
	public static int codigoMorbilidadRubeola = 22;
	public static int codigoMorbilidadSarampion = 23;
	public static int codigoMorbilidadSifilis = 24;
	public static int codigoMorbilidadTetanosAdulto = 25;
	public static int codigoMorbilidadTetanosNeo = 26;
	public static int codigoMorbilidadTorch = 27;
	public static int codigoMorbilidadTosferina = 28;
	public static int codigoMorbilidadTuberculosis = 29;
	public static int codigoMorbilidadVaricela = 30;
	public static int codigoMorbilidadVIH = 31;
	
	public static int codigoMortalidadPerinatal = 1;
	public static int codigoMortalidadMaterna = 2;
	public static int codigoMortalidadMenorIra = 3;
	public static int codigoMortalidadMenorEda = 4;
	public static int codigoMortalidadMalaria = 5;
	
	public static int codigoBroteHepatitisA = 1;
	public static int codigoBroteIntoxAlimentaria = 2;
	public static int codigoBroteVaricela = 3;
//	public static int codigoBroteEta = 3;
	public static int codigoBroteIntoxPlaguicida = 4;
	public static int codigoBrotePaperas = 5;
	public static int codigoBroteIntoxQuimico = 6;
	public static int codigoBroteOtro = 7;
	
	public static int codigoSolicitudLabExterna = 1;
	public static int codigoSolicitudLabInterna = 2;
	
	public static String nombreMorbilidadColera = "COLERA";
	public static String nombreMorbilidadDengueClasico = "DENGUE CLASICO";
	public static String nombreMorbilidadDengueHemorragico = "DENGUE HEMORRAGICO";
	public static String nombreMorbilidadDifteria = "DIFTERIA";
	public static String nombreMorbilidadEtas = "ETAS";
	public static String nombreMorbilidadExpoRabica = "EXPOSICION RABICA";
	public static String nombreMorbilidadFiebreAmarilla = "FIEBRE AMARILLA";
	public static String nombreMorbilidadHepatitisA = "HEPATITIS A";
	public static String nombreMorbilidadHepatitisB = "HEPATITIS B";
	public static String nombreMorbilidadHepatitisC = "HEPATITIS C";
	public static String nombreMorbilidadIntoxOh = "INTOX POR OH";
	public static String nombreMorbilidadIntoxQuim = "INTOX POR QUIMICOS";
	public static String nombreMorbilidadLepra = "LEPRA";
	public static String nombreMorbilidadMalaria = "MALARIA";
	public static String nombreMorbilidadMeningitis = "MENINGITIS";
	public static String nombreMorbilidadParalisisFlac = "PARALISIS FLACIDA < 15";
	public static String nombreMorbilidadParotiditis = "PAROTIDITIS";
	public static String nombreMorbilidadPeste = "PESTE";
	public static String nombreMorbilidadRabiaAnimal = "RABIA ANIMAL";
	public static String nombreMorbilidadRabiaHumana = "RABIA HUMANA";
	public static String nombreMorbilidadReaccionPost = "REACCION POSTVACUNAL";
	public static String nombreMorbilidadRubeola = "RUBEOLA";
	public static String nombreMorbilidadSarampion = "SARAMPION";
	public static String nombreMorbilidadSifilis = "SIFILIS";
	public static String nombreMorbilidadTetanosAdulto = "TETANOS ADULTO";
	public static String nombreMorbilidadTetanosNeo = "TETANOS NEONATAL";
	public static String nombreMorbilidadTorch = "TORCH";
	public static String nombreMorbilidadTosferina = "TOS FERINA";
	public static String nombreMorbilidadTuberculosis = "TUBERCULOSIS";
	public static String nombreMorbilidadVaricela = "VARICELA";
	public static String nombreMorbilidadVIH = "VIH";
	
	public static String nombreBroteHepatitis = "BROTE DE HEPATITIS A";
	public static String nombreBroteIntoxicacionAlimentaria = "BROTE DE ETA";
	public static String nombreBroteVaricela = "BROTE DE VARICELA";
	public static String nombreBroteIntoxAlcoholica = "INTOX POR ALCOHOLES";
	public static String nombreBroteIntoxPorQuimicos = "INTOX POR QUIMICO";
	public static String nombreBroteDifteria = "";
	public static String nombreBrotePaperas = "BROTE DE PAPERAS";
	
	public static String nombreMorbilidad = "MORBILIDAD";
	public static String nombreMortalidad = "MORTALIDAD";
	public static String nombreBrotes = "BROTES";
	public static String nombreSivim = "SIVIM";
	public static String nombreSisvan = "SISVAN";
	
	
	/**************************************************************************************
	 * CONSTANTES GRUPOS ASOCIOS SALAS CIRUGIA
	 * *****************************************************************************/
	/**
	 * C&oacute;digo del grupo de asocio salas - cirugia honorarios
	 */
	public static int codigoGrupoAsocioHonorarios=1;
	
	/**
	 * C&oacute;digo del grupo de asocio salas - cirugia salas
	 */
	public static int codigoGrupoAsocioSalas=2;
	
	/**
	 * C&oacute;digo del grupo de asocio salas - cirugia materiales
	 */
	public static int codigoGrupoAsocioMateriales=3;
	
	/**************************************************************************************
	 * CONSTANTES TIPOS CONCEPTO INGRESO TESORERï¿½A
	 * *****************************************************************************/
	
	/**
	 * C&oacute;digo del tipo de ingreso tesorerï¿½a Ninguno
	 */
	public static int codigoTipoIngresoTesoreriaNinguno=0;
	
	/**
	 * C&oacute;digo del tipo de ingreso tesorerï¿½a Pacientes
	 */
	public static int codigoTipoIngresoTesoreriaPacientes=1;
	
	/**
	 * C&oacute;digo del tipo de ingreso tesorerï¿½a Convenios
	 */
	public static int codigoTipoIngresoTesoreriaConvenios=2;
	
	/**
	 * C&oacute;digo del tipo de ingreso tesorerï¿½a Otras Cuentas X Cobrar
	 */
	public static int codigoTipoIngresoTesoreriaOtrasCxC=3;
	
	/**
	 * C&oacute;digo del tipo de ingreso tesorerï¿½a Cartera Particular
	 */
	public static int codigoTipoIngresoTesoreriaCarteraParticular=4;
	
	/**
	 * C&oacute;digo del tipo de ingreso tesorerï¿½a Abonos
	 */
	public static int codigoTipoIngresoTesoreriaAbonos=5;
	
	/**
	 * Codigo del tipo de ingreso tesoreria  anticipo de Convenio  odontologia
	 */
	public static int codigoTipoIngresoTesoreriaAnticipoConvenioOdon=6;
	
	/**************************************************************************************
	 * ESTADOS RECIBOS CAJA
	 * *****************************************************************************/
	public static int codigoEstadoReciboCajaRecaudado=1;
	public static int codigoEstadoReciboCajaEnArqueo=2;
	public static int codigoEstadoReciboCajaEnCierre=3;
	public static int codigoEstadoReciboCajaAnulado=4;
	
	/**************************************************************************************
	 * ESTADOS PAGOS (PARTICULARES - EMPRESA)
	 * *****************************************************************************/
	
	/**
	 * Aplica para parituclares y empresa
	 */
	public static int codigoEstadoPagosRecaudado=1;
	
	/**
	 * Aplica solo para pagos de convenios(Empresas);
	 */
	public static int codigoEstadoPagosAplicado=3;
	/**
	 * Aplica para parituclares y empresa
	 */
	public static int codigoEstadoPagosAnulado=2;
	
	/**************************************************************************************
	 * CONSTANTES ESQUEMAS TARIFARIOS GENERALES
	 * *****************************************************************************/
	
	/**
	 * C&oacute;digo del esquema tarifario todos.
	 */
	public static int codigoEsqTarifarioGeneralTodos=1;
	
	/**
	 * C&oacute;digo del esquema tarifario todos iss
	 */
	public static int codigoEsqTarifarioGeneralTodosIss=2;
	
	/**
	 * C&oacute;digo del esquema tarifario todos soat
	 */
	public static int codigoEsqTarifarioGeneralTodosSoat=3;
	
	
	/**
	 * Constante para el tipo_doc_pagos igual a RecibosCaja
	 */
	public static final int codigoTipoDocumentoPagosReciboCaja=1;
	
	/**************************************************************************************
	 * CONSTANTES TIPOS PACIENTE
	 * *****************************************************************************/
	
	/**
	 * Acrï¿½nimo del tipo paciente Hospitalizado
	 */
	public static final String tipoPacienteHospitalizado="H";
	
	/**
	 * Acrï¿½nimo del tipo paciente Ambulatorio
	 */
	public static final String tipoPacienteAmbulatorio="A";
	
	/**
	 * Acrï¿½nimo del tipo paciente Cirugï¿½a Ambulatoria
	 */
	public static final String tipoPacienteCirugiaAmbulatoria="C";
	
	/************************************************************************************
	 * CONSTANTES EXAMENES OFTALMOLï¿½GICOS
	 * ****************************************************************************/
	public static final int codigoExamenOftalSK=1;
	public static final int codigoExamenOftalSKCiclo=2;
	public static final int codigoExamenOftalSKSubj=3;
	public static final int codigoExamenOftalADD=4;
	public static final int codigoExamenOftalDIP=5;
	public static final int codigoExamenOftalQueratometria=6;
	
	/************************************************************************************
	 * CONSTANTES QUE IDENTIFICAN CADA UNA DE LAS SECCIONES DE LA HOJA OFTALMOLï¿½GICA
	 * ****************************************************************************/
	/**
	 * Secciï¿½n de Estrabismo
	 */
	public static int seccionEstrabismo=1;
	
	/**
	 * Secciï¿½n de Segmento Anterior
	 */
	public static int seccionSegmentoAnt=2;
	
	/**
	 * Secciï¿½n de Retina y Vï¿½treo
	 */
	public static int seccionRetinaVitreo=3;
	
	/**
	 * Secciï¿½n de Orbita y Anexos
	 */
	public static int seccionOrbitaAnexos=4;
	
	/************************************************************************************
	 * CONSTANTES ESTADOS PETICION (tabla estados_peticion)
	 * ****************************************************************************/
	/**
	 * Estado Peticion 'Pendiente'
	 */
	public static final int codigoEstadoPeticionPendiente = 0;
	/**
	 * Estado Peticiï¿½n 'Programada'
	 */
	public static final int codigoEstadoPeticionProgramada = 1;
	/**
	 * Estado Peticiï¿½n 'Reprogramada'
	 */
	public static final int codigoEstadoPeticionReprogramada = 2;
	/**
	 * Estado Peticiï¿½n 'Atendida'
	 */
	public static final int codigoEstadoPeticionAtendida = 3;
	/**
	 * Estado Peticiï¿½n 'Anulada'
	 */
	public static final int codigoEstadoPeticionAnulada = 4;
	
	/************************************************************************************
	 * CONSTANTES TIPOS PROFESIONALES PETICION QX (tabla tipo_prof_peticion)
	 * ****************************************************************************/
	
	/**
	 * Codigo del tipo profesional 'solicitante' en la peticion Qx.
	 */
	public static final int codigoTipoProfSolicitantePeticionQx = 1;
	
	/**
	 * C&oacute;digo del tipo profesional 'participante' en la peticion Qx.
	 */
	public static final int codigoTipoProfParticipantePeticionQx = 2;

	/**
	 * 
	 */
	public static final String cadenaUnidades = "Pesos";
	
	/**
	 * 
	 */
	public static final String cadenaUnidadesDecimales="Centavo";

	/**
	 * 
	 */
	public static final String acronimoTipoActividadPYPServicio = "S";
	/**
	 * 
	 */
	public static final String acronimoTipoActividadPYPArticulo = "A";

	/**
	 * 
	 */
	public static final String codigoEstadoProgramaPYPSolicitado = "1";
	
	/**
	 * 
	 */
	public static final String codigoEstadoProgramaPYPEjecutado = "2";
	
	/**
	 * 
	 */
	public static final String codigoEstadoProgramaPYPProgramado = "3";
	

	/**
	 * 
	 */
	public static final String codigoEstadoProgramaPYPCancelado = "4";

	/**
	 * Codigo estad orden ambulatoria pendiente
	 */
	public static final int codigoEstadoOrdenAmbulatoriaPendiente = 1;

	/**
	 * Codigo estad orden ambulatoria pendiente
	 */
	public static final int codigoEstadoOrdenAmbulatoriaSolicitada = 2;
	
	/**
	 * Codigo estad orden ambulatoria Anulada
	 */
	public static final int codigoEstadoOrdenAmbulatoriaAnulada = 3;
	
	/**
	 * Codigo estad orden ambulatoria Respondida
	 */
	public static final int codigoEstadoOrdenAmbulatoriaRespondida = 4;
	
	/**
	 * codigoTipoOrdenAmbulatoriaServicios
	 */
	public static final int codigoTipoOrdenAmbulatoriaServicios=1;

	/**
	 * codigoTipoOrdenAmbulatoriaArticulos
	 */
	public static final int codigoTipoOrdenAmbulatoriaArticulos=2;


	

	
		
	/************************************************************************************
	 *  ESTADOS APLICACION DE PAGOS
	 ************************************************************************************/
	 /**
	 * Aplica para parituclares y empresa
	 */
	public static int codigoEstadoAplicacionPagosPendiente=1;
	
	/**
	 * Aplica solo para pagos de convenios(Empresas);
	 */
	public static int codigoEstadoAplicacionPagosAprobado=2;
	/**
	 * Aplica para parituclares y empresa
	 */
	public static int codigoEstadoAplicacionPagosAnulado=3;
	
	/**
	 * Tipo del concepto Mayor Valor(Suma)
	 */
	public static int codigoTipoConceptoMayorValor=1;
	
	/**
	 * Tipo del Conceptos Menor Valor(Resta)
	 */
	public static int codigoTipoConceptoMenorValor=2;
	
	/************************************************************************************
	 *  CONSTANTES DE HOJA DE ANESTESIA 
	 ************************************************************************************/
	/**
	 * C&oacute;digo del tipo de tï¿½cnica de anestesia general que tiene opciones
	 */
	public static int codigoTecAnestesiaGralOpciones = 1;
	
	/**
	 * C&oacute;digo del tipo de tï¿½cnica de anestesia general que no tiene opciones
	 */
	public static int codigoTecAnestesiaGral = 2;
	
	/**
	 * C&oacute;digo del tipo de tï¿½cnica de anestesia regional
	 */
	public static int codigoTecAnestesiaRegional = 3;
	
	/**
	 * Tipos de anestesia
	 */
	public static int codigoTipoAnestesiaLocal = 1;
	public static int codigoTipoAnestesiaGeneral = 2;
	public static int codigoTipoAnestesiaTodas = 3;

	/************************************************************************************
	 *  CONSTANTES DE MEDICO Y Vï¿½A ACCESO CIRUGï¿½AS 
	 ************************************************************************************/
	/**
	 * Valor igual mï¿½dico/vï¿½a de liquidacion de cirugias
	 */
	public static int codigoIgualCx = 1;
	
	/**
	 * Valor diferente medico/vï¿½a de liquidacion de cirugias
	 */
	public static int codigoDiferenteCx = 2;
	/***************************************************************************************
	 * CONSTANTES DE LOS MODULOS 
	 ****************************************************************************************/	
	public static int codigoModuloConsultaExterna=1;	
	public static int codigoModuloFacturacion=2;
	public static int codigoModuloAdministracion=3;
	public static int codigoModuloManejoPaciente=4;
	public static int codigoModuloHistoriaClinica=5;
	public static int codigoModuloOrdenes=6;
	public static int codigoModuloDummy=7;
	public static int codigoModuloCartera=8;
	public static int codigoModuloSalasCirugia=9;
	public static int codigoModuloCuadroTurnos=10;
	public static int codigoModuloTesoreria=11;
	public static int codigoModuloInventarios=12;
	public static int codigoModuloEnfermeria=13;
	public static int codigoModuloEpidemiologia=14;
	public static int codigoModuloInterfaz=15;
	public static int codigoModuloCapitacion=16;
	
	//faltan 17-18
	public static int codigoModuloCarteraPaciente=19;
	public static int codigoModuloFacturasVarias=20;
	public static int codigoModuloGlosas=21;
	public static int codigoModuloOdontologia=22;
	public static String nombreModuloSalasCirugia="Salas Cirugï¿½a";
	
	/************************************************************************************
	 *  CONSTANTES DE PROGRAMACION DE SALAS De CIRUGIA 
	 ************************************************************************************/
	public static int codigoEstadoSalaDisponible=1;
	public static int codigoEstadoSalaOcupada=2;
	public static int codigoEstadoSalaRestringida=3;
	
	/************************************************************************************
	 *  CONSTANTES DE TIPOS DE CONCEPTOS INVENTARIO
	 ************************************************************************************/
	public static int codigoTipoConceptoEntradaInv=1;
	public static int codigoTipoConceptoSalidaInv=2;	
	
	/************************************************************************************
	 *  CONSTANTES DE TIPOS CAMBIOS (tabla tipos_cambio)
	 ************************************************************************************/
	public static int codigoTipoCambioModificacion=1;
	public static int codigoTipoCambioReversion=2;

	/**
	 * Referencias de la tabla tipo_dato
	 */
	public static int codigoTipoDatoLista=1;
	public static int codigoTipoDatoCheck=2;
	public static int codigoTipoDatoTexto=3;
	
	
	/************************************************************************************
	 *  CONSTANTES DE FORMATO IMPRESION DE FACTURA
	 ************************************************************************************/
	
	public static int codigoFormatoImpresionEstandar = 1;
	public static int codigoFormatoImpresionShaio = 2;
	public static int codigoFormatoImpresionVenezuela = 3;
	public static int codigoFormatoImpresionVersalles = 4;
	public static int codigoFormatoImpresionSonria = 5;
	public static int codigoFormatoImpresionFacturaAgrupada=6;

	
	
	
	public static int codigoTipoRompimientoArtSubAlmacen=1;
	public static int codigoTipoRompimientoArCentroCostoSolicita=2;
	public static int codigoTipoRompimientoArtClaseInventario=3;
	public static int codigoTipoRompimientoArtGrupo=4;
	public static int codigoTipoRompimientoArtSubGrupo=5;
	
	public static String colorCitasCuponNoDisponiblesAgenda="#0095D9";
	//public static String colorCitasCuponNoDisponiblesAgenda="#CEDEDF";
	
	public static int codigoProfesionalSaludNoAsignado = -2;
	
	/**
	 * C&oacute;digo de las secciones del encabezado del formato impresion
	 * Factura (det_sub_sec_encabezado)
	 */
	public static int codigoSeccionFormatoFacturaInstitucion = 1;
	public static int codigoSeccionFormatoFacturaPaciente = 2;
	public static int codigoSeccionFormatoFacturaResponsable = 3;
	public static int codigoSeccionFormatoFacturaAtencion = 4;
	
	/**
	 * C&oacute;digo Datos de la subSeccion Institucion del encabezado
	 */
	public static int codigoDatoFormatoFacturaRazonSocial = 1;
	public static int codigoDatoFormatoFacturaTipoNumeroId = 2;
	public static int codigoDatoFormatoFacturaDireccion = 3;
	public static int codigoDatoFormatoFacturaTelefono = 4;
	public static int codigoDatoFormatoFacturaCiudad = 5;
	public static int codigoDatoFormatoFacturaDepartamento = 6;
	public static int codigoDatoFormatoFacturaCodigoMinSal = 7;
	public static int codigoDatoFormatoFacturaActividadEconomica = 8;
	public static int codigoDatoFormatoFacturaResolucion = 9;
	public static int codigoDatoFormatoFacturaRangoInicialFinalFactura = 10;
	/**
	 * C&oacute;digo Datos de la subSeccion Paciente del encabezado
	 */
	public static int codigoDatoFormatoFacturaApellidosNombres = 11;
	public static int codigoDatoFormatoFacturaFechaNacimiento = 12;
	public static int codigoDatoFormatoFacturaEdad = 13;
	public static int codigoDatoFormatoFacturaSexo = 14;
	/**
	 * C&oacute;digo Datos de la subSeccion Responsable del encabezado
	 */
	public static int codigoDatoFormatoFacturaEmpresa = 15;
	public static int codigoDatoFormatoFacturaConvenio = 16;
	public static int codigoDatoFormatoFacturaPlanBeneficios = 17;
	public static int codigoDatoFormatoFacturaEmail = 18;
	public static int codigoDatoFormatoFacturaImprimirTitularParticular = 19;
	public static int codigoDatoFormatoFacturaContratoTitular = 20;
	public static int codigoDatoFormatoFacturaMontoTitular = 22;
	public static int codigoDatoFormatoFacturaTipoNumeroIdTitular = 37;
	public static int codigoDatoFormatoFacturaDireccionTitular = 38;
	public static int codigoDatoFormatoFacturaTelefonoTitular = 39;
	public static int codigoDatoFormatoFacturaApellidosNombresTitular = 40;
	public static int codigoDatoFormatoFacturaAutorizacionTitular = 43;
	/**
	 * C&oacute;digo Datos de la subSeccion Atenciï¿½n del encabezado
	 * 
	 */
	public static int codigoDatoFormatoFacturaViaIngreso = 23;
	public static int codigoDatoFormatoFacturaRegimen = 24;
	public static int codigoDatoFormatoFacturaClasificacionSocioeconomica = 25;
	public static int codigoDatoFormatoFacturaTipoAfiliado = 26;
	public static int codigoDatoFormatoFacturaTipoMonto = 27;
	public static int codigoDatoFormatoFacturaNaturaleza = 28;
	public static int codigoDatoFormatoFacturaCarnet = 29;
	public static int codigoDatoFormatoFacturaAccidenteTransito = 30;
	public static int codigoDatoFormatoFacturaNoPoliza = 31;
	public static int codigoDatoFormatoFacturaAutorizacionAdmision = 32;
	public static int codigoDatoFormatoFacturaFechaIngreso = 33;
	public static int codigoDatoFormatoFacturaDxIngreso = 34;
	public static int codigoDatoFormatoFacturaFechaEgreso = 35;
	public static int codigoDatoFormatoFacturaDxEgreso = 36;
	
	
	/************************************************************************************
	 *  CONSTANTES DEL CODIGO FUNCIONALIDAD TIPOS DE ARQUEOS ---- En proceso de Modificaciï¿½n.
	 ************************************************************************************/
	public static int codigoFuncionalidadTipoArqueoProvisional=394;
	public static int codigoFuncionalidadTipoArqueoDefinitivo=395;
	public static int codigoFuncionalidadTipoArqueoCierre=396;

	
	/************************************************************************************
	 *  CONSTANTES DE LOS TIPOS DE PARTICIPANTE EN UNA CIRUGIA 
	 ************************************************************************************/
	public static int codigoTipoParticipanteCirujano = 1;
	public static int codigoTipoParticipanteAyudante = 2;
	public static int codigoTipoParticipanteInstrumentador = 3;
	public static int codigoTipoParticipanteCirculante = 4;
	public static int codigoTipoParticipanteAnestesiologo = 5;
	
	
	/*******************************************************************************
	 * Constantes para el cuerpo de la Impresion de Facturas
	 *******************************************************************************/
	
	public static int codigoRompimientoServTipoServicios = 1;
	public static int codigoRompimientoServGrupoServicios = 2;
	public static int codigoRompimientoServEspecialidadServicios = 3;
	public static int codigoRompimientoServCCSolicitaServicios = 4;
	public static int codigoRompimientoServCCEjecutaServicios = 5;
	
	public static int codigoColumnaSecPpalConsecutivoRegistro = 1;
	public static int codigoColumnaSecPpalCodigoInterno = 2;
	public static int codigoColumnaSecPpalCodigoManual = 3;
	public static int codigoColumnaSecPpalDescripcion = 4;
	public static int codigoColumnaSecPpalCantidad = 5;
	public static int codigoColumnaSecPpalValorUnitario = 6;
	public static int codigoColumnaSecPpalValorTotal = 7;
	
	public static int codigoRompimientoArtSubAlmacen = 1;
	public static int codigoRompimientoArtCCSolicita = 2;
	public static int codigoRompimientoArtClaseInventario = 3;
	public static int codigoRompimientoArtGrupo = 4;
	public static int codigoRompimientoArtSubGrupo = 5;
	
	public static int codigoValorLetrasTotalCargos = 1;
	public static int codigoValorLetrasTotalConvenio = 2;
	public static int codigoValorLetrasTotalPaciente = 3;
	public static int codigoValorLetrasTotalNetoPaciente = 4;
	

	/************************************************************************************
	 * CONSTANTES DE PARAMETRIZACION REGISTROS INTERFAZ 
	 ************************************************************************************/
	public static int codIndicativoSiNoExisteDefault=1;
	public static int codTamanioFijo=2;
	public static int codTamanioMenorQue=3;
	
	public static int tipoInterfazFacturacion=1;	
	public static int tipoInterfazAnulacion=2;
	public static int tipoInterfazAmbos=3;
	
	//tabla tipos_indicativo_existe
	public static int tipoIndicativoExisteValorDefault = 1;
	public static int tipoIndicativoExisteBlanco = 2;
	public static int tipoIndicativoExisteGenerarInconsistencia = 3;
	
	public static int tipoRegistroInterfazTotalesFactura=1;	
	public static int tipoRegistroInterfazServiciosFactura=2;
	public static int tipoRegistroInterfazArticulosFactura=3;
	
	public static int tipoSelectorInterfazFijo=46;
	public static int tipoSelectorInterfazDescripcionFija=57;
	public static int tipoSelectorInterfazCampoDescripcionFija=97;
	
	public static int tipoValorInterfazLibre=1;
	public static int tipoValorInterfazDescripcionFija=2;
	public static int tipoValorInterfazDefault=3;
	public static int tipoValorInterfazTamanoMenorQue=4;
	public static int tipoValorInterfazTamanoFijo=5;
	
	/**Valores de cada uno de los campos**/
	public static int valorCampoNumeroFactura = 1;
	public static int valorCampoNumeroAnulacion = 2;
	public static int valorCampoNumeroOrden = 3;
	public static int valorCampoAnioFactura = 4;
	public static int valorCampoAnioAnulacion = 5;
	public static int valorCampoAnioGeneracion = 6;
	public static int valorCampoMesFactura = 7;
	public static int valorCampoMesAnulacion = 8;
	public static int valorCampoMesGeneracion = 9;
	public static int valorCampoDiaFactura = 10;
	public static int valorCampoDiaAnulacion = 11;
	public static int valorCampoDiaGeneracion = 12;
	public static int valorCampoHoraFactura = 13;
	public static int valorCampoHoraAnulacion = 14;
	public static int valorCampoHoraGeneracion = 15;
	public static int valorCampoFechaFactura = 16;
	public static int valorCampoFechaAnulacion = 17;
	public static int valorCampoFechaGeneracion = 18;
	public static int valorCampoFechaHoraFactura = 19;
	public static int valorCampoFechaHoraAnulacion = 20;
	public static int valorCampoFechaHoraGeneracion = 21;
	public static int valorCampoValorConvenio = 22;
	public static int valorCampoValorPaciente = 23;
	public static int valorCampoValorDevolucionPaciente = 24;
	public static int valorCampoValorDescuentoPaciente = 25;
	public static int valorCampoValorAbonosPaciente = 26;
	public static int valorCampoValorServicios = 27;
	public static int valorCampoValorInventarios = 28;
	public static int valorCampoValorPool = 29;
	public static int valorCampoValorIngresoPool = 30;
	public static int valorCampoCuentaConvenio = 31;
	public static int valorCampoCuentaPaciente = 32;
	public static int valorCampoCuentaDescuentoPaciente = 33;
	public static int valorCampoCuentaAbonosPaciente = 34;
	public static int valorCampoCuentaServicios = 35;
	public static int valorCampoCuentaInventarios = 36;
	public static int valorCampoCuentaPool = 37;
	public static int valorCampoCuentaIngresoPool = 38;
	public static int valorCampoTerceroConvenioRsponsable = 39;
	public static int valorCampoTerceroPaciente = 40;
	public static int valorCampoTerceroPool = 41;
	public static int valorCampoTerceroMedico = 42;
	public static int valorCampoDescripcionCentroCostoSolicita = 43;
	public static int valorCampoDescripcionCentroCostoSolicitado = 44;
	public static int valorCampoNulo = 45;
	public static int valorCampoValorFijo = 46;
	public static int valorCampoApellidosNombresPaciente = 47;
	public static int valorCampoNombreConvenioResponsable = 48;
	public static int valorCampoRazonSocialEmpresa = 49;
	public static int valorCampoNombreTerceroEmpresa = 50;
	public static int valorCampoDescripcionServicio = 51;
	public static int valorCampoDescripcionArticulo = 52;
	public static int valorCampoNombrePool = 53;
	public static int valorCampoNombreMedico = 54;
	public static int valorCampoNombreUsuarioFactura = 55;
	public static int valorCampoNombreUsuarioAnula = 56;
	public static int valorCampoDescripcionFija = 57;
	public static int valorCampoUsuarioAnula = 58;
	public static int valorCampoUsuarioFactura = 59;
	public static int valorCampoDebito = 60;
	public static int valorCampoCredito = 61;
	public static int valorCampoSi = 62;
	public static int valorCampoNo = 63;
	public static int valorCampoCuentaDevolucionPaciente = 64;
	public static int valorCampoCentroCostoSolicitado = 65;
	public static int valorCampoCentroCostoSolicita = 66;


	/************************************************************
	 * CONSTANTES DE LOS TIPOS DE ARQUEOS
	 ************************************************************/
	public static int codigoTipoArqueoProvisional=1;
	public static int codigoTipoArqueoDefinitivo=2;
	public static int codigoTipoArqueoCierreCaja=3;
	
	
	/************************************************************************************
	 * CONSTANTES DE PARAMETRIZACION CAMPOS INTERFAZ 
	 ************************************************************************************/
	public static int codigoTipoCampoSecuencia=1;
	public static int codigoTipoCampoLibre=2;
	public static int codigoTipoCampoNumeroDocuemnto=3;
	public static int codigoTipoCampoPrefijoFactura=4;
	public static int codigoTipoCampoFehcaAnio=5;
	public static int codigoTipoCampoFechaMes=6;
	public static int codigoTipoCampoFechaDia=7;
	public static int codigoTipoCampoFechaHora=8;
	public static int codigoTipoCampoFechaDDMMAAAA=9;
	public static int codigoTipoCampoFehcaAAAAMMDD=10;
	public static int codigoTipoCampoFehcaHoraAAAAMMDDMMSS=11;
	public static int codigoTipoCampoFechaHoraDDMMAAAAMMSS=12;
	public static int codigoTipoCampoValores=13;
	public static int codigoTipoCampoCuentasContables=14;
	public static int codigoTipoCampoTipoMovimiento=15;
	public static int codigoTipoCampoNumeroIdSistema=16;
	public static int codigoTipoCampoCentroCostoSistema=17;
	public static int codigoTipoCampoCampoDetalleSistema=18;
	public static int codigoTipoCampoUsuario=19;
	
	/**Tipos de separador decimal**/
	public static int tipoSeparadorDecimalPunto = 1;
	public static int tipoSeparadorDecimalComa = 2;
	
	/***codigo funcionalidad recibos de caja****/
	public static int codigoFuncionalidadRecibosCaja=325;
	
	
	
	/************************************************************************************
	 * CONSTANTES DE LOS TIPOS DE SOPORTE RESPIRATORIO 
	 ************************************************************************************/
	/**
	 * C&oacute;digo del tipo de soporte respiratorio FiO2
	 */
	public static int codigoTipoSoporteRespiraFiO2=1;
	
	/**
	 * C&oacute;digo del tipo de soporte respiratorio Sist Admin O2
	 */
	public static int codigoTipoSoporteRespiraSistAdminO2=2;
	
	/**
	 * C&oacute;digo del tipo de soporte respiratorio SaO2
	 */
	public static int codigoTipoSoporteRespiraSaO2=3;
	
	
	/************************************************************************************
	 * CONSTANTES DE LOS TIPOS DE TIPOS AFILIADO 
	 ************************************************************************************/
	public static String codigoTipoAfiliadoBeneficiario = "B";
	public static String codigoTipoAfiliadoCotizante = "C";
	
	/************************************************************************************
	 * CONSTANTES DE LOS TIPOS DE TIPOS PAGOS 
	 ************************************************************************************/
	public static String codigoTipoPagoUpc = "1";
	public static String codigoTipoPagoGrupoEtareo = "2";
	
	/************************************************************************************
	 * CONSTANTES DE LOS TIPOS DE TIPOS DE CARGUE 
	 ************************************************************************************/
	public static int codigoTipoCargueMasivo = 1;
	public static int codigoTipoCargueIndividual = 2;
	
	/************************************************************************************
	 * CONSTANTES DE LOS TIPOS DE CONSECUTIVOS DE CAPITACION 
	 ************************************************************************************/
	public static int codigoTipoConsecutivoCapitacionUnicoCartera = 1;
	public static int codigoTipoConsecutivoCapitacionDiferenteCartera = 2;
	public static int codigoTipoConsecutivoFacturaPaciente = 3;
	
	/***************************************************************************************
	 ****       CONSTANTES DE ALCANCE PARA LA DEFINICION DE CAMPOS PARAMETRIZABLES ****
	 **************************************************************************************/
	public static int codigoAlcanceMedico = 1;
	public static int codigoAlcanceCentroCosto = 2 ;
	public static int codigoAlcanceInstitucion = 3;
	
	/************************************************************************************
	 * CONSTANTES DE GENERACIï¿½N INTERFAZ FACTURACION 
	 ************************************************************************************/
	/**
	 * Constantes usadas para la identificar Cuentas Interfaz Parametrizables
	 */
	public static int tipoCuentaConvenio = 1;
	public static int tipoCuentaPaciente = 2;
	public static int tipoCuentaAbonosPaciente = 3;
	public static int tipoCuentaDescuentosPaciente = 4;
	public static int tipoCuentaDevolucionPaciente = 5;
	public static int tipoCuentaFavorConvenio = 6;
	public static int tipoCuentaCxCGlosas = 7;
	
	public static int tipoCuentaServicios = 6;
	public static int tipoCuentaInventarios = 7;
	public static int tipoCuentaPool = 8;
	public static int tipoCuentaIngresoPool = 9;
	public static int tipoCuentaAsociadaUtilidadPerdida=10;
	
	/*
	 * Constantes para Estados de los Ajustes
	 */
	public static int codigoEstadoAjusteCxCPendiente=1;
	public static int codigoEstadoAjusteCxCAprobado=2;
	public static int codigoEstadoAjusteCxCAnulado=3;
	
	/*
	 * Constantes tipo cartera
	 */
	public static int codigoTipoCarteraTodos=-1;
	public static int codigoTipoCarteraEmpresas=1;
	public static int codigoTipoCarteraCapitacion=2;
	public static int codigoTipoCarteraParticulares=3;
	public static int codigoTipoCarteraFacturasVarias=4;
	
	/************************************************************************************
	 * CONSTANTES DE ACTIVIDADES DE PROMOCIï¿½N Y PREVENCIï¿½N 
	 ************************************************************************************/
	/**
	 * Constantes usadas para identificar el tipo de actividad
	 */
	public static String tipoActividadPYPArticulo = "A";
	public static String tipoActividadPYPServicio = "S";
	
	/************************************************************************************
	 * CONSTANTES DE PROGRAMAS DE PROMOCIï¿½N Y PREVENCIï¿½N 
	 ************************************************************************************/
	/**
	 * Constantes usadas para identificar la unidad de medida en las
	 * edades de los grupos etï¿½reos
	 */
	public static int codigoUnidadMedidaFechaDias = 1;
	public static int codigoUnidadMedidaFechaMeses = 2;
	public static int codigoUnidadMedidaFechaAnios = 3;
	
	/************************************************************************************
	 * CONSTANTES DE FINALIDADES SERVICIOS 
	 ************************************************************************************/
	/**
	 * Constantes usadas para identificar el tipo de actividad
	 */
	public static String codigoFinalidadServicioDiagnostico = "1";
	public static String codigoFinalidadServicioTerapeutico = "2";
	public static String codigoFinalidadServicioEnfermedadGeneral = "3";
	public static String codigoFinalidadServicioEnfermedadProfesional = "4";
	
	/************************************************************************************
	 * CONSTANTES DE LOS TIPOS DE CUENTAS DE COBRO DE CAPITACION 
	 ************************************************************************************/
	public static int codigoTipoCuentaCobroCapitacionNormal = 1;
	public static int codigoTipoCuentaCobroCapitacionSaldosIniciales = 2;
	
	
	/*********************************************************************************************
	 * 	CONSTANTES PARA LA INTEGRIDAD DE DOMINIO DE TIPO MOTIVO EN MOTIVOS SIR
	 * *********************************************************************************************/
	public static String acronimoTipoMotivoReferencia = "MREF";
	public static String acronimoTipoMotivoNegacion = "MNEG";
	public static String acronimoTipoMotivoCancelacion = "MCAN";

	/**************************************
	 * 	CONSTANTES TIPOS GRUPOS SERVICIOS LABORATORIOS
	 * ****************************************/
	
	public static String codigoTipoGrupoServiciosLaboratorios="LAB";
	
	
	/**
	 * 
	 */
	public static String acronimoSi="S";
	
	/**
	 * 
	 */
	public static String acronimoNo="N";

	/**
	 * 
	 */
	public static String acronimoCambio="C";

	/**
	 * Acronimo Utilizado en la Justificacion No pos para describir el estado Descartado
	 */
	public static String acronimoDescartado="D";
	
	
	/**
	 * Acrï¿½nimo para el manejo de la letra t (true)
	 */
	public static String acronimoTrueCorto = "t";
	
	/**
	 * Acrï¿½nimo para el manejo de la letra f (false)
	 */
	public static String acronimoFalseCorto = "f";
	/*********************************************************************************************
	 * 	CONSTANTES PARA LA GRAFICA DE LA CURVA DE CRECIMIENTO Y DESARROLLO 
	 * *********************************************************************************************/
	
	//------ Grafica de niï¿½os y niï¿½as de  meses (Estatura X Edad)
	public static final int codTpGrafCurvaCreciDlloM036_EXE = 1;
	public static final int codTpGrafCurvaCreciDlloF036_EXE = 2;
	public static final int codTpGrafCurvaCreciDlloM220_EXE = 3;
	public static final int codTpGrafCurvaCreciDlloF220_EXE = 4;
	
	//------ Grafica de niï¿½os y niï¿½as de  meses (Edad X Peso)
	public static final int codTpGrafCurvaCreciDlloM036_EdXP = 5;
	public static final int codTpGrafCurvaCreciDlloF036_EdXP = 6;
	public static final int codTpGrafCurvaCreciDlloM220_EdXP = 7;
	public static final int codTpGrafCurvaCreciDlloF220_EdXP = 8;
	
	//------ Grafica de niï¿½os y niï¿½as de  meses (Perimetro Cefalico X Edad)
	public static final int codTpGrafCurvaCreciDlloM036_PCE = 9;
	public static final int codTpGrafCurvaCreciDlloF036_PCE = 10;

	//------ Grafica de niï¿½os y niï¿½as de  meses (IMC X Edad)
	public static final int codTpGrafCurvaCreciDlloM220_IMCXE = 11;
	public static final int codTpGrafCurvaCreciDlloF220_IMCXE = 12;

	//------ Grafica de niï¿½os y niï¿½as de  meses (Peso X Estatura)
	public static final int codTpGrafCurvaCreciDlloM220_EsXP = 13;
	public static final int codTpGrafCurvaCreciDlloF220_EsXP = 14;
	public static final int codTpGrafCurvaCreciDlloM036_EsXP = 15;
	public static final int codTpGrafCurvaCreciDlloF036_EsXP = 16;
	
	/*********************************************************************************************
	 * 	CONSTANTES DE LOS TIPO DE DOSIS DE ANTITETANICA EN LA HOJA OBSTETRICA
	 **********************************************************************************************/
	public static final String acronimoTipoDosisAntetetanicaPrimera="PRA";
	public static final String acronimoTipoDosisAntetetanicaSegunda="SDA";
	
	/*********************************************************************************************
	 * 	CONSTANTES DE LOS TIPO DE ANTIRUBEOLA EN LA HOJA OBSTETRICA
	 **********************************************************************************************/
	public static final String acronimoTipoAntirubeolaPrevia="PRE";
	public static final String acronimoTipoAntirubeolaEmbarazo="EMB";
	public static final String acronimoTipoAntirubeolaNoSabe="NOSABE";
	
	/*********************************************************************************************
	 * 	CONSTANTES PARA LA INTEGRIDAD DE DOMINIO DE TIPO MOTIVO EN MOTIVOS SIR
	 * *********************************************************************************************/
	public static String acronimoTipoPrimeraSemana = "PS";
	public static String acronimoTipoDespuesPrimeraSemana = "DPS";
	
	
	/*********************************************************************************************
	 * 	CONSTANTES PARA SIGNOS VITALES DE VALORACION
	 * *********************************************************************************************/
	public static int codigoSignoVitalTalla = 9;
	public static int codigoSignoVitalPeso = 10;
	public static int codigoSignoVitalIMC = 11;
	public static int codigoSignoVitalPerimetroCefalico = 23;
	
	/*********************************************************************************************
	 * 	CONSTANTES PARA TIPOS DE DIAGNOSTICOS
	 *  (BUSQUEDA DIAGNOSTICOS GENï¿½RICA)
	 * *********************************************************************************************/
	public static int codigoDxPrincipal = 1;
	public static int codigoDxRelacionado = 2;
	public static int codigoDxIngreso = 3;
	public static int codigoDxComplicacion = 4;
	public static int codigoDxMuerte = 5;
	//para el manejo de diagnosticos preoperatorios
	public static int codigoDxPreoperatorioRelacionado = 6;
    
	/*************************************************************************************************
	 * CONSTANTE PARA BUSQUEDA GENERICA CONVENCIONES ODONTOLOGICAS
	 ************************************************************************************************/
	public static int codigoBusquedaGenericaConvenciones=1;
	public static int codigoBusquedaGenericaTramas=2;
	public static int codigoBusquedaGenericaImagenes=3;
	public static int codigoBusquedaGenericaImagenesBase=4;
	
	/*********************************************************************************************
	 * 	CONSTANTE RESPUESTA REPORTADO PREVIAMENTE EN FICHA EPIDEMIOLï¿½GICA
	 * *********************************************************************************************/
	public static String respuestaLogFichaEpidemiologia = "LOG";
	
	/*********************************************************************************************
	 * 	CONSTANTES ESTADOS CIVILES
	 * *********************************************************************************************/
	public static String acronimoEstadoCivilSoltero = "S";
	public static String acronimoEstadoCivilCasado = "C";
	public static String acronimoEstadoCivilUnionLibre = "U";
	public static String acronimoEstadoCivilViudo = "V";
	public static String acronimoEstadoCivilSeparado = "P";
	public static String acronimoEstadoCivilDesconocido = "D";
	
	
	/*********************************************************************************************
	 * 	CONSTANTES ESTUDIOS PACIENTE
	 * *********************************************************************************************/
	public static int codigoEstudioPrimaria = 1;
	public static int codigoEstudioSecundaria = 2;
	public static int codigoEstudioUniversitaria = 3;
	public static int codigoEstudioNinguna = 4;
	
	/*********************************************************************************************
	 * 	CONSTANTES SECCIONES CAMPOS PARAMETRIZABLES
	 * *********************************************************************************************/
	public static String seccionParametrizablesExamenesFisicosPediatricos = "9";
	public static String seccionParametrizablesDesarrolloPediatricos = "10";
	public static String seccionParametrizablesRevisionSistemasPediatricos = "11";
	public static String seccionParametrizablesValoracionNutricionalPediatricos = "12";
	public static String seccionParametrizablesSuenoHabitosPediatricos = "13";
	public static String seccionParametrizablesFinalidadPediatricos = "14";
	public static String seccionParametrizablesCausaExternaPediatricos = "15";
	public static String seccionParametrizablesObservacionesPediatricos = "16";
	public static String seccionParametrizablesConceptoConsultaPediatricos = "23";
	public static String seccionParametrizablesRevisionSistemasInterconsulta = "18";
	public static String seccionParametrizablesExamenesFisicosInterconsulta = "17";
	public static String seccionParametrizablesFinalidadInterconsulta = "19";
	public static String seccionParametrizablesCausaExternaInterconsulta = "20";
	public static String seccionParametrizablesConceptoConsultaInterconsulta = "21";
	public static String seccionParametrizablesObservacionesInterconsulta = "22";
	
	/*********************************************************************************************
	 * 	CONSTANTES TIPOS DE CAMPOS PARAMETRIZABLES
	 * *********************************************************************************************/
	public static int tipoParametrizablesTexto = 1;
	public static int tipoParametrizablesTextArea = 2;
	public static int tipoParametrizablesSelect = 3;
	public static int tipoParametrizablesArchivo = 4;
	
	
	/*********************************************************************************************
	 * 	CONSTANTES TIPOS DE CAJAS
	 * *********************************************************************************************/
	
	public static int codigoTipoCajaMayor = 1;
	public static int codigoTipoCajaPpal = 2;
	public static int codigoTipoCajaRecaudado = 3;
	
	/*********************************************************************************************
	 * 	CONSTANTES TIPOS DE FRECUENCIOA
	 * *********************************************************************************************/
	
	public static int codigoTipoFrecuenciaHoras = 1;
	public static int codigoTipoFrecuenciaMinutos = 2;
	public static int codigoTipoFrecuenciaDias = 3;
	
	public static String nombreTipoFrecuenciaHoras = "Horas";
	public static String nombreTipoFrecuenciaMinutos = "Minutos";
	//MT6753
		public static String nombreTipoFrecuenciaDias = "Días";
	
	/*********************************************************************************************
	 * 	CONSTANTES CONDUCTAS EVOLUCION
	 * *********************************************************************************************/
	
	public static int codigoConductaASeguirHospitalizarEvolucion = 1;
	public static int codigoConductaASeguirRemitirEvolucion = 2;
	public static int codigoConductaASeguirSalidaEvolucion = 3;
	public static int codigoConductaASeguirContinuarObservacionEvolucion = 4;
	public static int codigoConductaASeguirContinuaHospitalOcirugiaAmbulaEvolucion = 5;	
	public static int codigoConductaASeguirCirugiaAmbulatoriaEvolucion = 6;
	public static int codigoConductaASeguirTrasladarAPisoEvolucion = 7;
	public static int codigoConductaASeguirTrasladoCuidadoEspecial = 8;
	public static int codigoConductaASeguirContinuarCirugiaAmbulatoria=9;
	public static int codigoConductaASeguirContinuarCuidadoEspecial=10;
	public static int codigoConductaASeguirCambioTipoMonitoreo=11;
	
	/*********************************************************************************************
	 * 	CONSTANTES CONDUCTAS contrareferencia
	 * *********************************************************************************************/
	
	public static int codigoConductaASeguirContrareferenciaOtro = 11;
	

	/*********************************************************************************************
	 * 	CONSTANTES CLASIFICACION TIPOS CONVENIOS
	 * *********************************************************************************************/
	public static int codigoClasificacionTipoConvenioSoat=1;
	public static int codigoClasificacionTipoConvenioPos=2;
	public static int codigoClasificacionTipoConvenioEps=3;
	public static int codigoClasificacionTipoConvenioArp=4;
	public static int codigoClasificacionTipoConvenioArs=5;
	public static int codigoClasificacionTipoConvenioVinculado=6;
	public static int codigoClasificacionTipoConvenioAseguradora=7;
	public static int codigoClasificacionTipoConvenioEmpresarial=8;
	public static int codigoClasificacionTipoConvenioParticular=9;
	public static int codigoClasificacionTipoConvenioPrepagada=10;
	public static int codigoClasificacionTipoConvenioEventoCatastrofico=11;
	public static int codigoClasificacionTipoConvenioOtra=12;

	/*********************************************************************************************
	 * 	CONSTANTES TIPO FACTURACION	
	 * *********************************************************************************************/
	public static int codigoTipoFacturacionPacienteConAtencionFinalizada=1;
	public static int codigoTipoFacturacionPacienteContinuaProcesoAtencion=2;
	
	
	/*******************************************************************************************************
	 *	CONSTANTES PARA MANEJAR LAS TRANSACCIONES DE LA INTERFAZ 
	 * *****************************************************************************************************/
	public static int codigoTransaccionEntradaComprasInterfaz=1;
	public static int codigoTransaccionSalidaDevolucionCompraInterfaz=2;
	public static int codigoTransaccionSalidaConsignacionConsumos=3;
	public static int codigoTransaccionEntradaCompraConsignacion=4;
	public static int codigoTransaccionSalidaDevolucionCompraConsignacion=5;
	public static int codigoTransaccionEtradaDevolucionComprasConsignacion=6;
	public static int codigoTransaccionEntradaComprasProveedor=7;
	public static int codigoTransaccionSalidaDevolucionComprasProveedor=8;
	public static int codigoTransaccionEntradaTrasaladoDeConsignacion=9;
	public static int codigoTransaccionSalidadTrasladoAConsignacion=10;
	public static int codigoTransaccionEntradaDonacionInterfaz=11;
	public static int codigoTransaccionSalidaDevolucionDonacionInterfaz=12;
	
	
	/*******************************************************************************************************
	 *	CONSTANTES PARA MANEJAR LOS TIPOS DE COMPAï¿½IAS DE LA PARAMETRIZACION DE ARCHIVOS PLANOS COLSANITAS
	 * *****************************************************************************************************/
	public static InfoDatos identificadorCompaniaColsanitas=new InfoDatos("C","Colsanitas"); 
	public static InfoDatos identificadorCompaniaMedisanitas=new InfoDatos("M","Medisanitas");
	public static InfoDatos identificadorCompaniaEps=new InfoDatos("E","Eps");
	
	/**************************************************************************************************
	 * 	CONSTATES PARA MANEJAR LAS SECCIONES DE PARAMETROS ENTIDADES SUBCONTRATADAS
	 * ************************************************************************************************/
	 
	public static int tipoInformacionIngresoCuenta = 1;
	public static int tipoCargosDirectos = 2;
	public static int parametrosGenerales = 3;
	
	/**************************************************************************************************
	 * 	CONSTATES PARA MANEJAR EL TIPO DE MOVIMIENTO PARA FACTURACION Y TESORERIA INTERFAZ SISTEMA UNO
	 * ************************************************************************************************/
	
	public static String tipoMovimientoFacturaVenta = "AP";
	public static String tipoMovimientoReciboCaja = "RC";
	
	/**************************************************************************************************
	 * 	CONSTATES EVENTOS HOJA ANESTESIA
	 * ************************************************************************************************/
	public static  int codigoEventoHojaAnestesiaInicioFinAnestesia=1;
	public static  int codigoEventoHojaAnestesiaInicioFinCx=2;
	public static  int codigoEventoHojaAnestesiaOtros=12;
	
	/**************************************************************************************************
	 * 	CONSTATES PARA EL MANEJO DE LAS OPERACIONES EN LA BD
	 * ************************************************************************************************/
	
	
	public static String acronimoNada = "N";
	public static String acronimoInsertar = "I";
	public static String acronimoEliminar = "E";
	public static String acronimoModificar = "M";
	
	/**************************************************************************************************
	 * 	CONSTATES PARA TIPOS GASES ANESTESICOS 
	 * ************************************************************************************************/
	public static int codigoTipoGasAnestesicoNinguno=1;
	
	
	/**************************************************************************************************
	 * 	CONSTATES PARA MONITOREO HEMODINAMICA 
	 * ************************************************************************************************/
	public static int codigoMonitoreoHemodinamicaPAPSistolica=8;
	public static int codigoMonitoreoHemodinamicaPAPDiastolica=9;
	public static int codigoMonitoreoHemodinamicaPAPMedia=10;
	public static int codigoMonitoreoHemodinamicaPVC=11;
	public static int codigoMonitoreoHemodinamicaPCP=12;
	public static int codigoMonitoreoHemodinamicaGC=13;
	public static int codigoMonitoreoHemodinamicaIC=14;
	public static int codigoMonitoreoHemodinamicaRVS=16;
	public static int codigoMonitoreoHemodinamicaRVP=17;
	public static int codigoMonitoreoHemodinamicaITVD=18;
	public static int codigoMonitoreoHemodinamicaITVI=19;
	
	/**************************************************************************************************
	 * 	CONSTATES PARA LOS TIPOS DE TERCEROS 
	 * ************************************************************************************************/
	public static int codigoTipoTerceroPersonaNatural=0;
	public static int codigoTipoTerceroPersonaJuridica=1;
	public static int codigoTipoTerceroNoAplica=9;
	
	/**************************************************************************************************
	 * 	CONSTANTES PARA LAS SECCIONES Y CAMPOS DE EL FORMATO DE JUSTIFICACION DE SERVICIOS Y ARTICULOS NO POS 
	 * ************************************************************************************************/
	public static int JusSeccionEncabezado=1;
	public static int JusSeccionPaciente=2;
	public static int JusSeccionDiagnosticos=3;
	public static int JusSeccionMedicamentosPos=4;
	public static int JusSeccionMedicamentosNopos=5;
	public static int JusSeccionSustitutos=6;
	public static int JusSeccionSolicitud=9;
	public static int JusSeccionSolicitudServReemplazan=30;
	public static int JusSeccionObservaciones=7;
	public static int JusSeccionFirmas=8;
	public static int JusSeccionPiePagina=10;
	public static int JusSeccionPiePaginaArt=9;
	
	public static int JusOrdenCampoRiesgo=4;
	
	public static int JusCampoEdad=5;
	public static int JusCampoEntidad=3;
	public static int JusCampoTipoUsuario=4;
	public static int JusCampoMedicamentoSolicitado=6;
	public static int JusCampoIngreso=92;
	public static int JusCampoTiempoRespuestaEsperado=23;
	public static int JusCampoRegistroInvima=24;
	public static int JusCampoEfectoDeseadoTratamiento=25;
	public static int JusCampoEfectosSecundariosPosiblesRiesgos=26;
	public static int JusCampoBibliografia=29;
	public static int JusSeccionIgualGrupoTerapeutico=6;
	public static int JusCampoDosificacion=30;
	public static int JusCampoFormaFarmaceutica=31;
	public static int JusCampoDosisDiaria=32;
	public static int JusCampoTiempoTratamiento=33;
	public static int JusCampoGrupoTerapeutico=34;
	public static int JusCampoNumeroDosisEquivalentes=35;
	public static int JusCampoObservaciones=36;
	public static int JusCampoDeclaracionConstancia=37;
	public static int JusCampoNombreMedicoTratante=38;
	public static int JusCampoEspecialidadMedico=39;
	public static int JusCampoTipoIdMedico=40;
	public static int JusCampoRegistroMedicoTratante=41;
	
	/**************************************************************************************************
	 * 	CONSTATES PARA LOS TIPOS REGISTROS EN INTERFAZ RC
	 * ************************************************************************************************/
	public static String acronimoInterfazRCTodosRC="T";
	
	
	/*********************************************************************************************************
	 * 	CONSTANTES PARA LAS ACTIVIDADES AUTORIZADAS DE UNIDAD DE AGENDA POR USUARIO POR CENTRO DE ATENCION
	 * *******************************************************************************************************\
	 */
	public static int codigoActividadAutorizadaAsignarCitas=1;
	public static int codigoActividadAutorizadaCancelarCitas=2;
	public static int codigoActividadAutorizadaReservarCitas=3;
	public static int codigoActividadAutorizadaCuposExtra=4;
	public static int codigoActividadAutorizadaReasignarProfesional=5;
	public static int codigoActividadAutorizadaReprogramarCitas=6;
	public static int codigoActividadAutorizadaHorarioAtencion=7;
	public static int codigoActividadAutorizadaGenerarAgenda=8;
	public static int codigoActividadAutorizadaCancelarAgenda=9;
	public static int codigoActividadAutorizadaCitasNoRealizadas=10;
	public static int codigoActividadAutorizadaCondonarMultasCitas=11;
	public static int codigoActividadAutorizadaAnularMultasCitas=12;
	public static int codigoActividadAutorizadaAutorizarCitasPacientesConMulta=13;
	public static int codigoActividadAutorizadaCambiarServicion=14;
	public static int codigoActividadAutorizadaConfirmarSolicitudCambiarServicio=15;
	public static int codigoActividadAutorizadaAnularSolicitudCambiarServicio=16;
	public static int codigoActividadAutorizadaConfirmar=17;
	public static int codigoActividadAutorizadaExcepcionesHorarioAtencion=18;
	
	/**********************************************************************************************************
	 * CONSTANTES PARA LOS TIPOS DE EVOLUCION DE LA EPICRISIS
	 * *******************************************************************************************************/
	 public static int codigoTipoEvolucionEpicrisisRegistroEvoluciones=1;
	 public static int codigoTipoEvolucionEpicrisisEventosAdversosAdministrativos=2;
	 public static int codigoTipoEvolucionEpicrisisEventosAdversosAsistenciales=3;
	 public static int codigoTipoEvolucionEpicrisisProcedimientos=4;
	 public static int codigoTipoEvolucionEpicrisisMedicamentos=5;
	 public static int codigoTipoEvolucionEpicrisisCirugia=6;
	 public static int codigoTipoEvolucionEpicrisisInterconsulta=7;
	 public static int codigoTipoEvolucionEpicrisisCuidadosEspeciales=8;
	 public static int codigoTipoEvolucionEpicrisisValoracionInicialUrg=9;
	 public static int codigoTipoEvolucionEpicrisisValoracionInicialHosp=10;
	 
	 /**********************************************************************************************************
	  * CONSTANTES PARA LOS TIPOS DE EVOLUCION DE LA EPICRISIS
	  * *******************************************************************************************************/
	 public static int codigoFuncionalidadAntecentesVarios=71;
	 public static int codigoFuncionalidadAntecentesGinecoObstetricos=72;
	 public static int codigoFuncionalidadAntecentesPediatricos=73;
	 public static int codigoFuncionalidadAntecentesToxicos=74;
	 public static int codigoFuncionalidadAntecentesAlergias=75;
	 public static int codigoFuncionalidadAntecentesMedicamentos=76;
	 public static int codigoFuncionalidadAntecentesTransafuncionales=77;
	 public static int codigoFuncionalidadAntecentesMedicosYQx=78;
	 public static int codigoFuncionalidadAntecentesFamiliares=79;
	 public static int codigoFuncionalidadAntecentesPersonalesOculares=319;
	 public static int codigoFuncionalidadAntecentesFamiliaresOculares=320;
	 public static int codigoFuncionalidadAntecentesVacunas=500;
	 public static int codigoFuncionalidadAntecentesOdontologicos=2001;
	 
	 /**********************************************************************************************************
	  * CONSTANTES PARA LOS tipos_examen_fisico_pre
	  * *******************************************************************************************************/
	 public static int codigoTipoExamenFisicoPrePeso=5;
	 public static int codigoTipoExamenFisicoPreTalla=22;
	 
	 /**************************************************************************************************
	 * 	CONSTATES NATURALEZAS CUENTAS CONTABLES
	 * ************************************************************************************************/
		public static String naturalezaCredito = "C";
		public static String naturalezaDebito = "D";
	
	/***************************************************************************
	 * CONSTANTES REPORTES ESTADISTICOS	
	 **************************************************************************/
	public static int codigoReporteIndicadoresGestionConsEx = 1;
	public static int codigoReporteOportuConsulExEspecialidad = 2;	
	public static int codigoReporteOportuConsulExProfesionalSalud = 3;
	public static int codigoReporteIndicadoresOportuEspecMes = 4;
	public static int codigoReporteIndiceCancelacionCitaMes= 5;
	public static int codigoReporteMotivoCancelacionCitaPaciente= 6;
	public static int codigoReporteMotivoCancelacionCitaInstitucion= 7;
	public static int codigoReporteMotivoCancelacionCitaMes= 8;
	public static int codigoReporteMotivoCancelacionCitaEspecialidad = 9;
	
	
	/***************************************************************************
	 * CONSTANTES ESTADOS TRASLADO ALMACEN
	 **************************************************************************/
	public static int codigoEstadoTrasladoPendiente = 1;
	public static int codigoEstadoTrasladoCerrada = 2;	
	public static int codigoEstadoTrasladoAnulada = 3;
	public static int codigoEstadoTrasladoDespachada = 4;
	


/********************************************************************************* 
 * CODIGOS DE LAS CATEGORIAS DEL ARTICULO 
 * ******************************************************************************/
	public static int codigoCategoriaArtNormal = 1; //Normal
	public static int codigoCategoriaArtControl= 2; //control


	/********************************************************************************* 
	 * CONSTANTES PARA LE SENTIDO DE ORIENTACION DEL LOS ORDENAMIENTOS
	 * ******************************************************************************/
		public static String tipoOrdenamientoAscendente = "ASC"; 
		public static String tipoOrdenamientoDescendente= "DESC";
	
	/********************************************************************************* 
	 * CLIENTES DE AXIOMA 
	 * ******************************************************************************/
	public static String clienteSUBA = "SUBA";
	public static String clienteSHAIO = "SHAIO";
	public static String clienteUNIDADSANTAFE = "UNIDADSANTAFE";
	public static String clienteINMACULADA = "INMACULADA";
	public static String clienteCAIHCRON = "CAIHCRON";
	public static String clienteHILARIOLUGO = "HILARIOLUGO";
	public static String clienteSANJUANDIOS = "SANJUANDIOS";
	public static String clienteSOMOSSALUD = "SOMOSSALUD";
	public static String clienteVERSALLES = "VERSALLES";
	
	/********************************************************************************* 
	 * TIPOS IDENTIFICACION 
	 * ******************************************************************************/
	public static String acronimoTipoIdMenorSinId="MS";
	public static String acronimoTipoIdAdultosSinId="AS";

	/********************************************************************************* 
	 * NAT EVENTOS CATASTROFICOS 
	 * ******************************************************************************/
	public static int codigoNatEventoSismo=1;
	public static int codigoNatEventoMaremotos=2;
	public static int codigoNatEventoEruoVolcanicas=3;
	public static int codigoNatEventoDeslizamientoTierra=4;
	public static int codigoNatEventoInundaciones=5;
	public static int codigoNatEventoAvalanchas=6;
	public static int codigoNatEventoIncendioNatural=7; 
	public static int codigoNatEventoOtrosEnat=8;
	public static int codigoNatEventoSustanciasToxicas=9;
	public static int codigoNatEventoIncendio=10;
	public static int codigoNatEventoCorrosivos=11;
	public static int codigoNatEventoEnvenenamiento=12;
	public static int codigoNatEventoOtrosEtec=13;
	public static int codigoNatEventoExplosionTerrorista=14;
	public static int codigoNatEventoIncendioTerrorista=15;
	public static int codigoNatEventoCombate=16;
	public static int codigoNatEventoTomaGuerrillera=17;
	public static int codigoNatEventoMasacre=18;
	public static int codigoNatEventoOtros=19;
	public static int codigoNatEventoHuracan=20;
	public static int codigoNatEventoMinaAntipersonal=21;
	
	/*************************************************************************************
	 * TIPOS SERV VEHICULOS 
	 *************************************************************************************/
	public static int codigoVehiculoServicioParticular=1;
	public static int codigoVehiculoServicioPublico=2;
	public static int codigoVehiculoServicioOficial=3;
	public static int codigoVehiculoEmergencia=4;
	public static int codigoVehiculoServicioDiplomaticoOConsultar=5;
	public static int codigoVehiculoTransporteMasivo=6;
	public static int codigoVehiculoEscolar=7;
	
	/*************************************************************************************
	 * TIPOS SOPORTES FACTURAS
	 *************************************************************************************/
	
	public static int codigoTipoSoporteFacturaCentroCosto=1;
	public static int codigoTipoSoporteFacturaItem=2;
	public static int codigoTipoSoporteEstadoCuentaSol=3;
	public static int codigoTipoSoporteEstadoCuentaItem=4;
	public static int codigoTipoSoporteEstadoCuentaCC=5;
	public static int codigoTipoSoporteAutorizaciones=6;
	public static int codigoTipoSoporteEpicrisis=7;
	public static int codigoTipoSoporteOrdenFormulaMedica=8;
	public static int codigoTipoSoporteResultadoExamenesApoyoDiagnostico=9;
	public static int codigoTipoSoporteDescripcionQuirurjica=10;
	public static int codigoTipoSoporteRegistroAnestesia=11;
	public static int codigoTipoSoporteHistoriaClinica=12;
	public static int codigoTipoSoporteHojaAtencionUrgencias=13;
	public static int codigoTipoSoporteHojaAdministracionMedicamentos=14;
	public static int codigoTipoSoporteJustificacionNOPOS=15;
	
	/***************************************************************************
	 * TIPOS TRATAMIENTOS *
	 **************************************************************************/
	public static int codigoTipoTratamientoOdontologia = 1;
	public static int codigoTipoTratamientoOrtodoncia = 2;
	public static int codigoTipoTratamientoEndodoncia = 3;
	public static int codigoTipoTratamientoPeriodoncia = 4;
	public static int codigoTipoTratamientoHigieneOral = 5;
	public static int codigoTipoTratamientoOperatoria = 6;
	public static int codigoTipoTratamientoDientesNoTrat = 7;
	public static int codigoTipoTratamientoCirugia = 8;
	
	/***************************************************************************
	 * TIPOS DETINOS PACIENTES CONDUCTA
	 **************************************************************************/
	public static int codigoDestinoPacDomicilio = 1;
	public static int codigoDestinoPacObservacion = 2;
	public static int codigoDestinoPacInternacion = 3;
	public static int codigoDestinoPacRemision = 4;	
	public static int codigoDestinoPacContrarremision = 5;
	public static int codigoDestinoPacOtro = 6;		

	/***************************************************************************
	 * TIPOS DE INCONSISTENCIAS EN BASE DE DATOS
	 **************************************************************************/
	public static final int usuarioNoExiste = 1;
	public static final int datosNoCorrespondenconDocumento = 2;
	
	/***************************************************************************
	 * ESTADOS SOLICITUD FACTURA 
	 **************************************************************************/
	public static final int estadoSolFactPendiente = 1;
	public static final int estadoSolFactAnulada = 2;
	public static final int estadoSolFactCargada = 3;
	public static final int estadoSolFactInactiva = 4;
	public static final int estadoSolFactExcento = 5;
	public static final int estadoSolFactExterno = 6;
	
	/********************************************************************************* 
	 * CODIGOS DE LOS LOGOS PARA FORMATOS ESTANDAR 
	 * ******************************************************************************/
	public static final String logoprecidencia = "logo_presidencia.gif";
	
	/********************************************************************************* 
	 * TIPOS DE DOCUMENTO INTERFAZ 1E
	 * ******************************************************************************/
	
	public static final int codigoTipoDocInteFacturaPaciente=1;
	public static final int codigoTipoDocInteAnulaFacturaPaciente=2;
	public static final int codigoTipoDocInteFacturasVarias=3;
	public static final int codigoTipoDocInteAnulaFacturasVarias=4;
	public static final int codigoTipoDocInteAjusFacturasVarias=5;
	public static final int codigoTipoDocInteCCCapitacion=6;
	public static final int codigoTipoDocInteAjustesCCCapitacion=7;
	public static final int codigoTipoDocInteReciboCaja=8;
	public static final int codigoTipoDocInteAnulaRecCaja=9;
	public static final int codigoTipoDocInteAjustesFactPaciente=10;
	public static final int codigoTipoDocInteAutoServicioEntSub=11;
	public static final int codigoTipoDocInteAnulaAutorServicioEntSub=12;
	public static final int codigoTipoDocInteDespachoMed=13;
	public static final int codigoTipoDocInteDevolucionMedi=14;
	public static final int codigoTipoDocInteDespaPedidoInsumo=15;
	public static final int codigoTipoDocInteDevolPedidoInsumo=16;
	public static final int codigoTipoDocInteDespachoPedidoQx=17;
	public static final int codigoTipoDocInteDevolucionPedidoQx=18;
	public static final int codigoTipoDocInteCargosDirectosArt=19;
	public static final int codigoTipoDocInteAnulaCargosDirectosArticulo=20;
	public static final int codigoTipoDocInteDevolRecibosCaja=21;
	public static final int codigoTipoDocInteRegistroGlosas=22;
	
	
	/********************************************************************************* 
	 * TIPOS DE CONSECUTIVO INTERFAZ 1E
	 * ******************************************************************************/
	
	public static final int tipoConsecutivoInteNumeroFactura=1;
	public static final int tipoConsecutivoInteNumeroAnulacion=2;
	public static final int tipoConsecutivoInteNumeroFacturaVaria=3;
	public static final int tipoConsecutivoInteNumeroAjuste=4;
	public static final int tipoConsecutivoInteNumeroCC=5;
	public static final int tipoConsecutivoInteNumeroReciboCaja=6;
	public static final int tipoConsecutivoInteNumeroDocAutor=7;
	public static final int tipoConsecutivoInteNumeroDespacho=8;
	public static final int tipoConsecutivoInteNumeroDevolucion=9;
	public static final int tipoConsecutivoInteNumeroPedido=10;
	public static final int tipoConsecutivoInteNumeroSolicitud=11;
	public static final int tipoConsecutivoInteNumeroGlosaInterno=12;
	public static final int tipoConsecutivoInteNumeroGlosaEntidad=13;
	
	/********************************************************************************
	 * TIPOS EVENTO CARTERA Y GLOSAS PARA INTERFAZ 1E
	 ********************************************************************************/
	
	public static final int eventoFacturasPacientes=1;
	public static final int eventoAuditoriaFacturas=2;
	public static final int eventoCuentasCobroCartera=3;
	public static final int eventoRadicacionCuentasCobroCartera=4;
	public static final int eventoInactivacionFacturasCuentasCobro=5;
	public static final int eventoDevolucionCuentasCobro=6;
	public static final int eventoRegistroGlosas=7;
	public static final int eventoRegistroGlosasDevolucion=8;
	public static final int eventoRegistroRespuestaGlosas=9;
	public static final int eventorespuestaGlosasConciliadas=10;
	public static final int eventoRadicacionrespuestaGlosas=11;

	public static final int codigoSuperficieBoca = 1;
	public static final int codigoSuperficieDiente = 2;

	/**
	 * Acronimo para el constraint S de la BD de tipo char
	 */
	public static final char acronimoSiChar = 'S';

	/**
	 * Acronimo para el constraint N de la BD de tipo char
	 */
	public static final char acronimoNoChar = 'N';
	
	
	/**************************************************************************************
	 * ESTADOS PAGOS APLICACIONES PAGOS
	 * *****************************************************************************/
	
	public static int codigoEstadoPagosAplicacionesPendiente=1;
	public static int codigoEstadoPagosAplicacionesAprobado=2;
	public static int codigoEstadoPagosAplicacionesAnulado=3;

	/**********************************************************************************************
	 * CONSTANTES PARA EL MANEJO DE MSG DE ERROR Y ADVERTENCIA DE LA GENERACION AGENDA ODONTOLOGICA
	 **********************************************************************************************/
	public static String acronimoAgendaOdonError = "ERROR";
	public static String acronimoAgendaOdonAdvertencia= "ADV";
	
	/**********************************************************************************************
	 * CONSTANTES PARA EL MANEJO DE LA PRIORIDAD PARA APLICAR PROMOCIONES ODONTOLï¿½GICAS
	 **********************************************************************************************/
	public static String acronimoMayorPorcentajeDescuentoValor = "MAPDV";
	public static String acronimoMenorPorcentajeDescuentoValor= "MEPDV";
	
	
	
	/**********************************************************************************************
	 * CONSTANTES PARA LOS CASOS CITAS ODONTOLOGICAS
	 **********************************************************************************************/
	public static String acronimoSinPlanTratamiento = "SINPT";
	public static String acronimoConPlanTrataActivoEnProceso = "CONPT";
	public static String acronimoConPlanTratamientoTerminado = "CONPTTER";
	public static String acronimoConPlanTratamientoInactivo = "CONPTINAC";
	
	/**********************************************************************************************
	 * CONSTANTES PARA LOS CASOS AGENDA ODONTOLOGICA
	 **********************************************************************************************/
	public static String acronimoNuevaAgendaOdon = "NAGEN";
	public static String acronimoNuevaSubagendaOdon = "NSAGEN";
	public static String acronimoCitaOdonPac = "CODO";
	
	/**********************************************************************************************
	 * CONSTANTES PARA LOS CASOS CITA ODONTOLOGICA
	 **********************************************************************************************/
	public static String acronimoCambiarServicioCitaOdon = "CAMSER";
	public static String acronimoCancelarCitaOdon = "CANCIT";
	
	/**********************************************************************************************
	 * CONSTANTES PARA EL ODONTOGRAMA
	 **********************************************************************************************/
	public static int codigoSectorVestibular = 1;
	public static int codigoSectorMesial = 2;
	public static int codigoSectorLingual = 3;
	public static int codigoSectorDistal = 4;
	public static int codigoSectorOclusal = 5;
	
	/**********************************************************************************************
	 * NUMERO DIAS CUOTAS
	 **********************************************************************************************/
	public static int numeroDiasCuotas=30;
	
	/**********************************************************************************************
	 * PERMISOS  PRESUPUESTO
	 **********************************************************************************************/
	public static int codigoFuncionaildadModificarPresupuesto=1004;
	public static int codigoFuncionalidadContratarPresupuesto=1005;
	public static int codigoFuncionalidadSuspenderPresupuesto=1006;
	public static int codigoFuncionalidadReactivarPresupuesto=1007;
	public static int codigoFuncionalidadCancelarPresupuesto=1008;
	public static int codigoFuncionalidadCopiarPresupuesto=1009;
	public static int codigoFuncionalidadPrecontratadoPresupuesto=1080;
	
	/**********************************************************************************************
	 * PERMISOS TIPO ARQUEOS
	 **********************************************************************************************/
	public static int codigoFuncionalidadTipoArqueoCaja=1049;
	public static int codigoFuncionalidadTipoArqueoEntregaParcial=1050;
	public static int codigoFuncionalidadTipoArqueoCierreTurno=1051;
	
	public static int codigoTipoMovimientoArqueoCaja=1;
	public static int codigoTipoMovimientoArqueoEntregaParcial=2;
	public static int codigoTipoMovimientoArqueoCierreTurno=3;
	public static int codigoTipoMovimientoAperturaTurnoCaja=4;
	public static int codigoTipoMovimientoEntregaTransportadora=5;
	public static int codigoTipoMovimientoEntregaCajaPrincipalMayor=6;
	public static int codigoTipoMovimientoSolicitudTraslado=7;


	
	/**********************************************************************************************
	 * CASOS ACTUALIZACION ESTADOS PROGRAMA SERVICIOS PLAN TRATAMIENTO OTRAS EVOLUCIONES
	 **********************************************************************************************/
	public static char acronimoRegistroServicioEvolucionar = 'E';
	public static char acronimoRegistrarAtencionExterna = 'A';
	public static char acronimoRegistrarExclusiones = 'X';
	public static char acronimoRegistrarInclusiones = 'I';
	public static char acronimoRegistrarGarantias = 'G';
	public static char acronimoRegistrarCancelaciones = 'C';
	public static char acronimoRegistrarContratado = 'T'; 
	public static char acronimoRegistrarPendiente = 'P';
	public static char acronimoAutorizarExcInc = 'U';
	public static char acronimoEnprocesoTerminado = 'P';
	
	
	/**********************************************************************************************
	 * CODIGO ENTIDAD NO VALIDO PARA TESORERIA
	 **********************************************************************************************/
	public static String acronimoEntidadNoValida = "SIN_ENTIDAD";
	
	
	/************************************************************************************
	 *  CONSTANTES DEL CODIGO FUNCIONALIDAD Menu Control Caja - Tesoreria
	 ************************************************************************************/
	public static int codigoFuncionalidadMenuControlCaja = 530;
	
	
	/**********************************************************************************************
	 * ANEXOS DE PRESUPUESTO A IMPRIMIR
	 **********************************************************************************************/
	public static String anexoPresupuestoContrato 			= "CONTRATO";
	public static String anexoPresupuestoRecomendaciones 	= "RECOMENDACIONES";
	public static String anexoPresupuestoPresupuesto 		= "PRESUPUESTO";
	public static String anexoPresupuestoOtro 				= "OTRO SI";
	

	/**********************************************************************************************
	 * REPORTE INGRESOS ODONTOLOGICOS 
	 **********************************************************************************************/
	public static int codigoFuncionalidadReporteIngresosOdontologicos=1067;
	
	
	/**************************************************************************
	 * Tipos de persona TABLA: administracion.tipos_persona
	 *****************************************************************************/
	public static int codigoTipoPersonaNatural=1;
	public static int codigoTipoPersonaJuridica=2;

	
	/**********************************************************************************************
	 * PERMISOS ANEXO 870 
	 * 	- FUNCIONALIDAD CHEQUEAR/DESCHEQUEAR SERVICIOS CITAS ODONTOLOGICAS TTO.
	 * 	- FUNCIONALIDAD LINK OTROS SERVICIOS CITAS ODONTOLOGICAS.
	 * 	- FUNCIONALIDAD MODIFICAR DURACION SERVICIO.
	 **********************************************************************************************/
	public static int codigoFuncionalidadSeleccionarServicios=1077;
	public static int codigoFuncionalidadLinkOtrosServicios=1078;
	public static int codigoFuncionalidadModificarDuracionServicio=1081;

	
	/**********************************************************************************************
	 * ROLES ANEXO 870 
	 **********************************************************************************************/
	public static int codigoFuncionalidadIngresarModificarConveiosPac	= 1083;
	public static int codigoFuncionalidadIngresarModificarBonosPac		= 1084;
	
	/**********************************************************************************************
	 * REPORTE INGRESOS POR TARJETA CLIENTE
	 **********************************************************************************************/
	public static int codigoFuncionalidadReporteIngresosPorTarjetaCliente=1079;
	
	
	public static int codigoFuncionalidadReportesOdontologia=1066;
	
	
	/**********************************************************************************************
	 * REPORTE PRESUPUESTOS ODONTOLOGICOS CONTRATADOS
	 **********************************************************************************************/
	public static int codigoFuncionalidadReportePresupuestosOdontologicosContratados=1090;
	
	/**********************************************************************************************
	 * REPORTE CITAS ODONTOLï¿½GICAS
	 **********************************************************************************************/
	public static int codigoFuncionalidadMenuReportesCitasOdonto=1075;
	
   /**********************************************************************************************
    * Cï¿½DIGO FUNCIONALIDAD ATENCIï¿½N CITA ODONTOLï¿½GICA
    **********************************************************************************************/
   public static int codigoFuncionalidadAtencionCitaOdontologica=1001;
   
   /**********************************************************************************************
	 * CONSULTA MOVIMIENTOS FACTURA VARIA
	 **********************************************************************************************/
	public static int codigoFuncionalidadmenuReportesFacturasVarias=815;
	
	   
	 /**********************************************************************************************
	 * REPORTES TESORERIA
	 **********************************************************************************************/
	public static int codigoFuncionalidadMenuReportesTesoreria=1098;
	
	/**********************************************************************************************
	 * REPORTE PRESUPUESTOS ODONTOLOGICOS CONTRATADOS CON PROMOCION
	 **********************************************************************************************/
	public static int codigoFuncionalidadReportesPromociones=1101;
	
	/**********************************************************************************************
	 * REPORTE COBERTURAS CONVENIOS ODONTOLOGICOS 
	 **********************************************************************************************/
	public static int codigoFuncionalidadReportesCoberturas=1106;
	
	/**********************************************************************************************
	 * MENU AUTORIZACIONES MANEJO DEL PACIENTE
	 **********************************************************************************************/
	public static int codigoFuncionalidadAutorizacionesCapitacionSubcontratada=1103;
	
	/**********************************************************************************************
	 * MENU AUTORIZACIONES SERVICIOS MEDICAMENTOS DE INGRESO ESTANCIA
	 **********************************************************************************************/
	public static int codigoFuncionalidadAutorizacionesServiciosMedicamentosIngresoEstancia=1114;

	/**********************************************************************************************
	 * MENU RIPS ENTIDADES SUBCONTRATADAS
	 **********************************************************************************************/
	public static int codigoFuncionalidadRipsEntidadesSubcontratadas=1116;
	
	/**********************************************************************************************
	 * FORMAS DE PAGO DE CONSOLIDACION DE CIERRE
	 **********************************************************************************************/
	
	public static String  pagoEfectivo="EFECTIVO";
	public static String pagoCheque="CHEQUE";
	public static String pagoTarjetaCredito="TARJETACREDITO";
	public static String pagotarjetaDebito="TARJETADEBITO";
	public static String letra="LETRA";
	public static String pagare="PAGARE";
	
	/**********************************************************************************************
	 * TIPOS DE MOVIMIENTO POR CIERRE EN CONSOLIDACION DE CIERRE
	 **********************************************************************************************/
	
	public static String  translados="TRASLADOS";
	public static String  solicitudes="SOLICITUTES";
	public static String  faltantesSobrantes="FALTANTES_SOBRANTES";
	public static String  recibosCaja="RECIBOS_CAJA";
	public static String  devolucionreciboscaja="DEVOLUCION_RECIBOS_CAJA";
	public static String  entregasparciales="ENTREGAS_PARCIALES";
	public static String  entregascajamayor="ENTREGAS_CAJA_MAYOR";	
	public static String  reportePorCentroAtencion="Consolidado por Centro Atencion";
	public static String  reportePorCajaCajero="Consolidado Caja Cajero";
	public static String  reporteLabelnit="NIT: ";
	public static String  reporteLabelDireccion="Dirección: ";
	public static String  reporteLabelCentroAtencion="Centro Atención: ";
	public static String  reporteLabelTipoConsulta="Tipo Consulta: ";
	public static String  telefono=" ,Teléfono: ";
	public static String  reportePlano="2";
	public static String  reporteExcel="3";
	public static String  reportePdf="1";
	public static Integer entregaParcialCaja=2;
	public static String  labelInstitucion="Institución: ";
	public static String  ordenamientoPorHoraAperura="HoraApertura";
	
		/**********************************************************************************************
	 * PARAMETRIZACION SEMAFORIZACION 
	 **********************************************************************************************/
	public static Integer  cantidadVecesRepetidoElemento=2;
	public static String 	colorInicialpaleta="#FFFFFF";
	public static BigDecimal valorLimiteParametrizaciones= new BigDecimal(100);
	
	/**********************************************************************************************
	 * MOTIVOS DE MODIFICACION DE PRESUPUESTOS
	 **********************************************************************************************/
	public static String estadoActivoChecbox="S";
	
	public static String estadoInActivoChecbox="N";
	
	/**********************************************************************************************
	 * PERMISOS ANEXO 1000
	 * 	- FUNCIONALIDAD CONSULTAR/MODIFICAR PRESUPUESTO CAPITACIï¿½N
	 ***********************************************************************************************/
	public static int codigoFuncionalidadConsultarModificarPresupuestoCapitacion=1139;
	
	/**
	 * FORMATO TIPO NUMERICO PARA LOS BEAN WRITE
	 */
	public static final String formatoMonetarioBeanWrite="#,000.00";
	
	/**
	 * ANCHO DEFINIDO PARA EL TAMAï¿½O DE LAS IMAGENES EN LOS REPORTES DE BIRT
	 */
	public static final String anchoImagenesBirt = "120pt";
	
	/**
	 * ALTO DEFINIDO PARA EL TAMAï¿½O DE LAS IMAGENES EN LOS REPORTES DE BIRT
	 */
	public static final String altoImagenesBirt = "50pt";
	
		
	/**********************************************************************************************
	 * CONSTANTES TRATAMIENTOS ODONTOLOGICOS
	 **********************************************************************************************/
	public static Integer valorInicial=0;
	public static String  splitterNumeroRegistro="CC";
	
	
	/**
	 *CONSTATE PARA REFERENCIAR UNA CAJA DE TESORERIA DE TIPO RECAUDO 
	 */
	public static Integer codigoCajaRecaudo=3; 
	
	
	
	/**********************************************************************************************
	 * CONSULTA MOVIMIENTOS ABONOS PACNTE
	 **********************************************************************************************/
	public static int codigoFuncionalidadMenuConsultaAbonosPaciente = 732;
	
	public static int codigoFuncionalidadMenuIngresosTesoreria = 321;
	
	/**********************************************************************************************
	 * CONSTANTE PARA MANEJAR EL PROCESO DE APERTURA DE TURNO
	 **********************************************************************************************/
	public static String  estadoAperturaTurno="En proceso";
	
	
	/**
	 * Constante con el ID de la funcionalidad Aprobaciï¿½n Anulaciï¿½n Devoluciï¿½n Recibos
	 */
	public static Integer aprobacionAnulacionDevolucionRecibos= new Integer(730);

	
	/**
	 * Constante con el nombre de la funcionalidad para usar antecedentes completos o solo varios antecedentes 
	 */
	public static String seccionAntecedentesPorCLientes="ANTECEDENTES_COMPLETOS";
	
	/**
	 * Constante con el nombre de la funcionalidad para usar el reporte historia clinica en pdf o jsp 
	 */
	public static String seccionHistoriaClinicaPorCLientes="HISTORIA_CLINICA_DYNAMIC_REPORTS";

	/**************************************************************************************
	 * CONSTANTES TIPOS HALLAZGOS ODONTOLOGICOS (CEO - COP)
	 * *****************************************************************************/
	
	public static Long tipoHallazgoCariado  = 1L;
	
	public static Long tipoHallazgoObturado = 2L;
	
	public static Long tipoHallazgoExtraido = 3L;
	
	public static Long tipoHallazgoPerdido  = 4L;
	
	/**************************************************************************************
	 * CONSTANTES TIPOS SECCIONES ORDENES MEDICAS
	 * *****************************************************************************/
	
	public static int seccionSoporteRespiratorio 	 = 1;
	
	public static int seccionDieta				  	 = 2;
	
	public static int seccionMezclas			 	 = 3;
	
	public static int seccionProcedimientos	 		 = 4;
	
	public static int seccionMedicamentos		  	 = 5;
	
	public static int seccionInterconsultas	  		 = 6;
	
	public static int seccionCirugias			   	 = 7;
	
	public static int seccionHojaNeurologica		 = 8;
	
	public static int seccionObservacionesGenerales	 = 9;
	
	/**************************************************************************************
	 * Permisos de impresion de HC
	**************************************************************************************/
	
	public static Integer permisoImprimirDetalleItemHC=1152;
	
	public static Integer permisoImprimirHC=1153;
	
	public static Integer permisoConsultarHistoriaDeAtenciones=1154;
	
	/**************************************************************************************
	 * Permisos de impresion de original de facturas
	 **************************************************************************************/
	
	public static Integer permisoImprimirOriginalFactura=1159;
	
	
	/**
	 * Codigo de la funcionalidad de reportes del modulo de consulta externa
	 */
	public static Integer codigoFuncionalidadReportesConsultaExterna=1160;
	
	/**********************************************************************************************
	 * PERMISOS  NOTAS ACLARATORIAS
	 **********************************************************************************************/
	public static int codigoFuncionalidadRegistroNotasAclaratorias=1155;
	
	/**********************************************************************************************
	 * CLASES DE ORDENES
	 **********************************************************************************************/
	public static int claseOrdenOrdenAmbulatoria=1;
	public static int claseOrdenPeticion=2;
	public static int claseOrdenCargoDirecto=3;
	public static int claseOrdenOrdenMedica=4;
	
	public static String claseOrdenOrdenAmbulatoriaDesc="Orden Ambulatoria";
	public static String claseOrdenPeticionDesc="Peticiï¿½n";
	public static String claseOrdenCargoDirectoDesc="Orden Mï¿½dica";
	public static String claseOrdenOrdenMedicaDesc="Orden Mï¿½dica";
	
	/**********************************************************************************************
	 * CONSTANTES DE TIPO DE SOLICITUD 
	 **********************************************************************************************/
	public static Integer  tipoSolicitudCirugia=14;
	
	
	public static String acronimoTipoPacienteAmbulatorio="A";

}

