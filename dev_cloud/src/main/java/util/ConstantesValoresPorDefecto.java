/**
 * 
 */
package util;

/**
 * @author axioma
 *
 */
public interface ConstantesValoresPorDefecto 
{
	/*--------------------------------------------------------------------------------------------------------------*
	 * SECCION DE CODIGOS
	 *--------------------------------------------------------------------------------------------------------------*/
	
	/**
	 * C&oacute;digo para manejar los valores por defecto de direccion del paciente
	 */
	public static int codigoValoresDefectoDireccion = 1;

	/**
	 * C&oacute;digo para manejar los valores por defecto del centro de costo al
	 * ingreso del paciente
	 */
	public static int codigoValoresDefectoCentroCostoConsultaExterna = 2;

	/**
	 * C&oacute;digo para manejar los valores por defecto de la causa externa en el
	 * ingreso del paciente
	 */
	public static int codigoValoresDefectoCausaExterna = 3;

	/**
	 * C&oacute;digo para manejar los valores por defecto de la finalida de la consulta
	 * en las valoraciones
	 */
	public static int codigoValoresDefectoFinalidadConsulta = 4;

	/**
	 * C&oacute;digo para manejar los valores por defecto de la ciudad de naciemeiento
	 * del paciente
	 */
	public static int codigoValoresDefectoCiudadNacimiento = 5;

	/**
	 * C&oacute;digo para manejar los valores por defecto de la ciudad de vivenda del
	 * paciente
	 */
	public static int codigoValoresDefectoCiudadResidencia = 6;

	/**
	 * C&oacute;digo para manejar los valores por defecto del domicilio del paciente
	 * (Rural ï¿½ Urbano)
	 */
	public static int codigoValoresDefectoDomicilio = 7;

	/**
	 * C&oacute;digo para manejar los valores por defecto de la ocupacion del paciente
	 * en el ingreso
	 */
	public static int codigoValoresDefectoOcupacion = 8;

	/**
	 * C&oacute;digo para manejar los valores por defecto del tipo de identificaciï¿½n en
	 * en ingreso
	 */
	public static int codigoValoresDefectoTipoId = 9;

	/**
	 * C&oacute;digo para manejar los valores por defecto para el modo de ingreso de la
	 * edad
	 */
	public static int codigoValoresDefectoIngresoEdad = 10;

	/**
	 * C&oacute;digo para manejar los valores por defecto para definir si se ingresa el
	 * nï¿½mero de historia clï¿½nica
	 */
	public static int codigoValoresDefectoHistoriaClinica = 11;

	/**
	 * C&oacute;digo para manejar los valores por defecto para activar el centinela a
	 * la salida
	 */
	public static int codigoValoresDefectoCentinela = 12;

	/**
	 * C&oacute;digo para manejar los valores por defecto del centro de costo de la via
	 * de ingreso urgencias
	 */
	public static int codigoValoresDefectoCentroCostoUrgencias = 13;

	/**
	 * C&oacute;digo para manejar los valores por defecto de la ocupaciï¿½n solicitada
	 */
	public static int codigoValoresDefectoOcupacionSolicitada = 14;

	/**
	 * C&oacute;digo para manejar los valores por defecto del estado de la cama al
	 * hacer un egreso
	 */
	public static int codigoValoresDefectoEstadoCamaEgreso = 15;

	/**
	 * C&oacute;digo para manejar el valor de UVR para los servicios ISS
	 */
	public static int codigoValoresDefectoValorUVR = 16;

//	Se elimina debido a q ya no se esta utilizando el parametro Carnet_Requerido
	/**
	 * C&oacute;digo para manejar el carnet requerido en la cuenta del paciente
	 */
	/**public static int codigoValoresDefectoCarnetRequerido = 17;*/ 

	/**
	 * C&oacute;digo para manejar la validaciï¿½nj del estado mï¿½dico de las solicitudes
	 * al dar egreso
	 */
	public static int codigoValoresDefectoValidarInterpretadas = 19;
	
	/**
	 * C&oacute;digo para manejar el hecho de si se quiere validar
	 * por contratos vigentes
	 */
	public static int codigoValoresDefectoValidarContratosVencidos=20;

	/**
	 * C&oacute;digo para manejar el tipo de manejo
	 * de topes por paciente
	 */
	public static int codigoValoresDefectoManejoTopesPaciente=21;
	
	/**
	 * C&oacute;digo para manejar los valores por defecto del centro de costo al
	 * ingreso del paciente
	 */
	public static int codigoValoresDefectoCentroCostoAmbulatorios = 22;
	
	/**
	 * C&oacute;digo para manejar los valores por defecto de la justificaciï¿½n
	 * requerida para procedimietnos e interconsultas
	 */
	public static int codigoValoresDefectoJustificacionServiciosRequerida = 23;
	
	/**
	 * C&oacute;digo para manejar los valores por defecto para el
	 * ingreso de la cantidad de articulos en el despacho
	 */
	public static int codigoValoresDefectoIngresoCantidadFarmacia = 24;
	
	/**
	 * C&oacute;digo para manejar los valores por defecto para los Rips
	 */
	public static int codigoValoresDefectoRipsPorFactura = 25;
	
	/**
	 * C&oacute;digo para manejar el valor por defecto de la Fecha
	 * Corte Saldo Inicial Cartera
	 */
	public static int codigoValoresDefectoFechaCorteSaldoInicialC = 26;
	
	/**
	 * C&oacute;digo para manejar el valor por defecto del Tope 
	 * consecutivo CxC Saldo Inicial
	 */
	public static int codigoValoresDefectoTopeConsecutivoCxCSaldoI = 27;
	
	/**
	 * C&oacute;digo para manejar el valor del MaxPageItems
	 */
	public static int codigoValoresDefectoMaxPageItems = 28;
	
	/**
	 * C&oacute;digo para manejar el valor de la excepciï¿½n de rips consultorios
	 */
	public static int codigoValoresDefectoExcepcionRipsConsultorios = 30;
	
	/**
	 * C&oacute;digo para manejar el valor de ajustar cuenta cobro radicada
	 */
	public static int codigoValoresDefectoAjustarCuentaCobroRadicada = 31;
	
	/**
	 * C&oacute;digo para manejar el valor de cerrar cuenta en la anulaciï¿½n de facturas
	 */
	public static int codigoValoresDefectoCerrarCuentaAnulacionFactura = 32;
	
	/**
	 * C&oacute;digo para manejar el valor de barrio de residencia
	 */
	public static int codigoValoresDefectoBarrioResidencia = 33;
	
	/**
	 * C&oacute;digo para manejar el valor de materiales por acto
	 */
	public static int codigoValoresDefectoMaterialesPorActo = 34;

	
	/**
	 * C&oacute;digo para manejar la Hora Inicial de programacion de Salas
	 */
	public static int codigoValoresDefectoHoraInicioProgramacionSalas = 35;

	/**
	 * C&oacute;digo para manejar la Hora Inicial de programacion de Salas
	 */
	public static int codigoValoresDefectoHoraFinProgramacionSalas = 36;
	
	/**
	 * C&oacute;digo que indica la especialidad anestesiologï¿½a
	 */
	public static int codigoValoresDefectoEspecialidadAnestesiologia = 37;
	/**
	 * C&oacute;digo para el manejo de consecutivos en las transacciones de inventarios
	 */
	public static int codigoValoresDefectoManejoConsecutivoTransInv = 38;
	/**
	 * C&oacute;digo para el porcentaje de alerta en diferencia en los costos de inventarios
	 */
	public static int codigoValoresDefectoPorcentajeAlertaCostosInv = 40;
	/**
	 * C&oacute;digo de transacciï¿½n utilizado para las solicitudes de pacientes
	 */
	public static int codigoValoresDefectoCodigoTransSoliPacientes = 41;
	/**
	 * C&oacute;digo de transacciï¿½n utilizado para las devoluciones de pacientes
	 */
	public static int codigoValoresDefectoCodigoTransDevolucionesPaciente = 42;
	/**
	 * C&oacute;digo de transacciï¿½n utilizado por los pedidos
	 */
	public static int codigoValoresDefectoCodigoTransPedidos = 43;
	/**
	 * C&oacute;digo de transacciï¿½n utilizado para las devoluciones de pedidos
	 */
	public static int codigoValoresDefectoCodigoTransDevolucionesPedidos = 44;
	
	/**
	 * C&oacute;digo para permitir modificar la fecha de la elaboraciï¿½n de inventarios
	 */
	public static int codigoValoresDefectoModificarFechaaInventarios = 45;
	/**
	 * C&oacute;digo para el porcentaje de alerta de punto de pedido
	 */
	public static int codigoValoresDefectoPorcentajePuntoPedido = 46;	
	/**
	 * C&oacute;digo para los dias previos de notificaion para la proxima cita de control
	 */
	public static int codigoValoresDefectoDiasPreviosNotificacionProximoControl = 47;
	/**
	 * C&oacute;digo transacciï¿½n utilizado para traslado almacenes
	 */
	public static int codigoValoresDefectoCodigoTransTrasladoAlmacenes = 48;
	/**
	 * C&oacute;digo chequeo automï¿½tico del campo "Informacion Adicional" en el ingreso de convenios
	 */
	public static int codigoValoresDefectoInfoAdicIngresoConvenios = 49;
	/**
	 * C&oacute;digo para la ocupaciï¿½n del mï¿½dico especialista
	 */
	public static int codigoValoresDefectoCodigoOcupacionMedicoEspecialista = 50;
	/**
	 * C&oacute;digo para la ocupaciï¿½n del mï¿½dico general
	 */
	public static int codigoValoresDefectoCodigoOcupacionMedicoGeneral = 51;
	/**
	 * C&oacute;digo para la ocupaciï¿½n enfermera
	 */
	public static int codigoValoresDefectoCodigoOcupacionEnfermera = 52;
	/**
	 * C&oacute;digo para la ocupaciï¿½n auxiliar de enfermerï¿½a
	 */
	public static int codigoValoresDefectoCodigoOcupacionAuxiliarEnfermeria = 53;

	/**
	 * C&oacute;digo para la hora de inicio del primer turno de enfermerï¿½a 
	 */
	public static int codigoValoresDefectoHoraInicioPrimerTurno = 54;

	/**
	 * C&oacute;digo para la hora final del ï¿½ltimo turno de enfermerï¿½a 
	 */
	public static int codigoValoresDefectoHoraFinUltimoTurno = 55;
	
	/**
	 * C&oacute;digo para el aviso sobre la existencia de fichas pendientes
	 */
	public static int codigoValoresDefectoAlertaFichasPendientes = 56;
	/**
	 * C&oacute;digo para la alerta sobre la existencia de un caso de notificacion obligatoria
	 */
	public static int codigoValoresDefectoAlertaCasoVigilancia = 57;
	/**
	 * Codigo para controlar si se hace vigilancia epidemiologica para accidente rabico
	 */
	public static int codigoValoresDefectoVigilarAccRabico = 58;
	
	/**
	 * codigo para MINUTOS DE ESPERA PARA ASIGNAR CITAS CADUCADAS.
	 */
	public static int codigoValoresDefectoMinutosEsperaCitaCaduca = 59;
	
	/**
	 * C&oacute;digo para especificar el TIEMPO MAXIMO (HORAS) DE GRABACIï¿½N DE REGISTROS
	 */
	public static int codigoValoresDefectoTiempoMaximoGrabacion = 60;
	
	/**
	 * C&oacute;digo para especificar si se ingresa la cantidad en la solicitud de medicamentos
	 */
	public static int codigoValoresDefectoIngresoCantidadSolMedicamentos=61;
	
	/**
	 * C&oacute;digo para especificar el tipo de consecutivo de capitaciï¿½n
	 */
	public static int codigoValoresDefectoTipoConsecutivoCapitacion = 62;
	
	/**
	 * C&oacute;digo para especificar el valor por defecto modificar minutos espera cuentas proc fact
	 */
	public static int codigoValoresDefectoModificarMinutosEsperaCuentasProcFact=64;
	
	/**
	 * C&oacute;digo para manejar el valor por defecto de la Fecha
	 * Corte Saldo Inicial Cartera Capitacion
	 */
	public static int codigoValoresDefectoFechaCorteSaldoInicialCCapitacion = 65;
	
	/**
	 * C&oacute;digo para manejar el valor por defecto del Tope 
	 * consecutivo CxC Saldo Inicial capitacion
	 */
	public static int codigoValoresDefectoTopeConsecutivoCxCSaldoICapitacion = 66;
	
	/**
	 * C&oacute;digo para manejar el valor por defecto del Dato de la cuenta requerido en la reserva de cita 
	 */
	public static int codigoValoresDefectoDatosCuentaRequeridoReservaCitas = 67;

	/**
	 * C&oacute;digo para manejar el valor por defecto de Red no adscrita
	 */
	public static int codigoValoresDefectoRedNoAdscrita = 68;
	
	
	/**
	 * C&oacute;digo para manejar el valor por defecto de Red no adscrita
	 */
	public static int codigoValoresDefectoCodigoTransCompras = 69;
	
	/**
	 * C&oacute;digo para manejar el valor por defecto de Red no adscrita
	 */
	public static int codigoValoresDefectoCodigoTransDevolucionCompras = 70;
	
	/**
	 * C&oacute;digo para manejar el valor por defecto de nï¿½mero dï¿½as tratamiento solicitudes medicamentos
	 */
	public static int codigoValoresDefectoNumDiasTratamientoMedicamentos = 71;
	
	/**
	 * C&oacute;digo para manejar el valor por defecto de nï¿½mero dï¿½as generar ordenes ambulatorias articulos
	 */
	public static int codigoValoresDefectoNumDiasGenerarOrdenesArticulos = 72;
	
	/**
	 * C&oacute;digo para manejar el valor por defecto de nï¿½mero dï¿½as egreso ordenes ambulatorias
	 */
	public static int codigoValoresDefectoNumDiasEgresoOrdenesAmbulatorias = 73;
	
	/**
	 * C&oacute;digo para manejar el valor por defecto Convenio fisalud
	 */
	public static int codigoValoresDefectoConvenioFisalud = 74;
	
	/**
	 * C&oacute;digo para manejar el valor por defecto de confirmar ajueste pool
	 */
	public static int codigoValoresDefectoConfimraAjustePool = 75;
	
	/**
	 * C&oacute;digo para manejar el valor por defecto de horas caducidad referencias externas
	 */
	public static int codigoValoresDefectoHorasCaducidadReferenciasExternas = 76;
	
	/**
	 * C&oacute;digo para manejar el valor por defecto de llamado automatico a la funcionalidad referecnias
	 */
	public static int codigoValoresDefectoLlamadoAutomaticoReferencia = 77;
	
	/**
	 * codigo para manejar el valor por  defecto que indica si tiene interfaz paciente
	 */
	public static int codigoValoresDefectoInterfazPaciente = 78;
	
	/**
	 * codigo para manejar el valor por  defecto que indica si tiene interfaz abono tesoreria 
	 */
	public static int codigoValoresDefectoInterfazAbonosTesoreria = 79;
	
	/**
	 * C&oacute;digo para manejar el valor por defecto de Validar edad responsable paciente 
	 */
	public static int codigoValoresDefectoValidarEdadResponsablePaciente = 80;
	
	/**
	 * C&oacute;digo para manejar el valor por defecto de Validar edad deudor paciente
	 */
	public static int codigoValoresDefectoValidarEdadDeudorPaciente= 81;
	
	/**
	 * Codigo para manejar los aï¿½os base de edad adulta
	 */
	public static int codigoValoresDefectoAniosBaseEdadAdulta=82;
	
	/**
	 * Codigo para manejar validar egreso administrativo para paquetizar
	 */
	public static int codigoValoresDefectoValidarEgresoAdministrativoPaquetizar=83;
	
	/**
	 * Codigo para manejar la maxima cantidad de paquetes validos por ingreso paciente
	 */
	public static int codigoValoresDefectoMaxCantidPaquetesValidosIngresoPaciente=84;
	
	/**
	 * Codigo para manejar lo referente a Asignar valor paciente con el valor de los cargos
	 */
	public static int codigoValoresDefectoAsignarValorPacienteValorCargos=85;
	
	/**
	 * Codigo para manejar lo referente al pais de residencia del paciente
	 */
	public static int codigoValoresDefectoPaisResidencia=86;
	
	/**
	 * Codigo para manejar lo referente al pais de nacimiento del paciente
	 */
	public static int codigoValoresDefectoPaisNacimiento=87;
	
	/**
	 * Codigo para manejar lo referente a la interfaz de compras
	 */
	public static int codigoValoresDefectoInterfazCompras=88;
	
	/**
	 * Codigo para manejar lo referente a articulo inventario
	 */
	public static int codigoValoresDefectoArticuloInventario=89;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoLoginUsuario=90;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoDiasAlertaVigencia=91;
	
	/**
	 * Codigo para manejar lo referente a Interfaz Cartera Pacientes
	 */
	public static int codigoValoresDefectoInterfazCarteraPacientes=92;
	
	/**
	 * Codigo para manejar lo referente la Interfaz Contable Facturas
	 */
	public static int codigoValoresDefectoInterfazContableFacturas=93;
	
	/**
	 * Codigo para manejar lo referente la Interfaz Terceros
	 */
	public static int codigoValoresDefectoInterfazTerceros=94;
	
	/**
	 * Codigo para manejar lo referente Consolidar Cargos
	 */
	public static int codigoValoresDefectoConsolidarCargos=95;
	
	/**
	 * Codigo 
	 */
	public static int codigoValoresDefectoManejaConversionMonedaExtranjera=96;
	
	/**
	 * Codigo para manejar la hora de corte del historico de camas
	 */
	public static int codigoValoresDefectoHoraCorteHistoricoCamas=97;
	
	
	/**
	 * Codigo para manejar los minutos limite de alerta de reserva
	 */
	public static int codigoValoresDefectoMinutosLimiteAlertaReserva=98;
	
	
	/**
	 * Codigo para manejar los minutos limite de alerta del paciente con salida de urgenicas
	 */
	public static int codigoValoresDefectoMinutosLimiteAlertaPacienteConSalidaUrgencias=99;
	
	/**
	 * Codigo para manejar los minutos limite de alerta del paciente con salida de hospitalizacion
	 */
	public static int codigoValoresDefectoMinutosLimiteAlertaPacienteConSalidaHospitalizacion=100;
	
	/**
	 * Codigo para manejar los minutos limite de alerta del paciente por remitir urgencias
	 */
	public static int codigoValoresDefectoMinutosLimiteAlertaPacientePorRemitirUrgencias=101;
	
	/**
	 * Codigo para manejar los minutos limite de alerta del paciente por remitir hospitalizacion
	 */
	public static int codigoValoresDefectominutosLimiteAlertaPacientePorRemitirHospitalizacion=102;
	/**
	 * Codigo para manejar el identificador institucion para reportar archivos colsanitas
	 */
	public static int codigoValoresDefectoIdentificadorInstitucionArchivosColsanitas=103;	
	/**
	 * Codigo Interfaz Rips
	 * */
	public static int codigoValoresDefectoInterfazRips=104;
	
	/**
	 * Codigo entidad maneja hospital dia
	 */
	public static int codigoValoresDefectoEntidadManejaHospitalDia=105;
	
	/**
	 * Codigo entidad maneja rips
	 * */
	public static int codigoValoresDefectoEntidadManejaRips=106;
	
	/**
	 * Codigo valoracion urgencias en hospitalizacion
	 * */
	public static int codigoValoresDefectoValoracionUrgenciasEnHospitalizacion=107;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoCodigoManualEstandarBusquedaServicios = 108;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoLiquidacionAutomaticaCirugias=109;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoLiquidacionAutomaticaNoQx=110;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoManejoProgramacionSalasSolicitudes= 111;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoRequridaDescripcionEspecialidadCirugias= 112;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoRequridaDescripcionEspecialidadNoCruentos= 113;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoAsocioAyudantia= 114;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoIndicativoCobrableHonorariosCirugia= 115;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoIndicativoCobrableHonorariosNoCruento= 116;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoAsocioCirujano= 117;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoAsocioAnestesia= 118;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoModificarInformacionDescripcionQuirurgicaiDiagnosticos= 119;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoMinutosRegistroNotasCirugia= 120;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoMinutosRegistroNotasNoCruentos= 121;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoModificarInformacionQuirurgica= 122;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoMinutosMaximosRegistroAnestesia= 123;
	
	/**
	 * C&oacute;digo para manejar la ubicacion planos entidades subcontratadas
	 */
	public static int codigoValoresDefectoUbicacionPlanosEntidadesSubcontratadas= 124;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoInstitucionMultiempresa=125;
	
	/**
	 *  Codigo Para Manejar el Path de Archivos Planos de Repotes Manejo Paciente
	 */
	public static int codigoValoresDefectoPath=126;
	
	/**
	 *  Codigo Para Manejar el Path de Archivos Planos de Repotes Inventarios
	 */
	public static int codigoValoresDefectoPathInventarios=127;
	
	/**
	 *  Codigo Para Manejar los conteos vï¿½lidos para ajustar inventarios fï¿½sicos
	 */
	public static int codigoValoresDefectoConteosValidosAjustarInvFisico=128;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoEntidadControlaDespachoSaldosMultidosis= 129;	
	 
	/**
	 * Nombre para manejar el valor por defecto
	 */
	public static int codigoValoresDefectoNumeroDiasControMedOrdenados= 130;	
	
	/**
	 * Codigo Parametro que define si ejecutara o no proceso de estancia automatica en el sistema
	 */
	public static int codigoValoresDefectoGenerarEstanciaAutomatica= 131;

	/**
	 * Codigo Parametro que define si ejecutara o no proceso de estancia automatica en el sistema
	 */
	public static int codigoValoresDefectoHoraGenerarEstanciaAutomatica= 132;

	/**
	 * Codigo Parametro que define si ejecutara o no proceso de estancia automatica en el sistema
	 */
	public static int codigoValoresDefectoIncluirTipoPacienteCirugiaAmbulatoria= 133;	

	/**
	 * Nombre para vlor por defecto
	 */
	public static int codigoValoresDefectoTipoConsecutivoManejar=144;
	
	/**
	 * Hacer Requerida Informaciï¿½n RIPS en Cargos Drectos
	 * */
	public static int codigoValoresDefectoRequeridaInfoRipsCargosDirectos = 135;
	
	/**
	 * C&oacute;digo para manejar el valor por defecto Forma Pago Efectivo
	 */
	public static int codigoValoresDefectoFormaPagoEfectivo = 136;
	
	/**
	 * C&oacute;digo para manejar el valor por defecto en la generacion de planos forecat en la generacion de rips
	 */
	public static int codigoValoresDefectoGeneracionForecatEnRips = 137;
	
	/**
	 * C&oacute;digo para manejar el valor por defecto en la validadcion de la ocupacion MEdico Especialista para ingreso Justificacion NO POs articulos
	 */
	public static int codigoValoresDefectoValidacionOcupacionJustificacionNoPosArticulos = 138;	
	
	/**
	 * C&oacute;digo para manejar el valor por defecto en la validadcion de la ocupacion MEdico Especialista para ingreso Justificacion NO POs servicios
	 */
	public static int codigoValoresDefectoValidacionOcupacionJustificacionNoPosServicios = 139;
	
	/**
	 * Codigo para manejar el valor por defecto de asigna valoracion de cirugia ambulatoria a Hospitalizado
	 * */
	public static int codigoValoresDefectoAsignaValoracionCxAmbulatoriaHospitalizado = 140;
	
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoInterfazContableRecibosCajaERP=141;

	/**
	 * 
	 */
	public static int codigoValoresDefectoValidarAdministracionMedicamentosEgresoMedico=142;
	

	public static int codigoValoresDefectoManejoInterfazConsecutivoFacturasOtroSistema=143;
	
	
	public static int codigoValoresDefectoInterfazNutricion=145;

	/**
	 * C&oacute;digo para manejar el valor del MaxPageItems
	 */
	public static int codigoValoresDefectoMaxPageItemsEpicrisis = 146;
	
	/**
	 * Path archivos planos reportes facturacion
	 */
	public static int codigoValoresDefectoPathArchivosPlanosFacturacion = 147;
	
	/**
	 * Path archivos planos Manejo del Paciente
	 */
	public static int codigoValoresDefectoPathArchivosPlanosManejoPaciente = 148;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoMostrarAntecedentesEpicrisis = 149;
	
	/**
	 * Path archivos planos Manejo del Paciente
	 */
	public static int codigoValoresDefectoPermitirConsultarEpicrisisSoloProf = 150;
	
	/**
	 * Comprobacion de derechos Capitaciï¿½n Obligatoria
	 * */
	public static int codigoValoresDefectoComprobacionDerCapitacionObliga = 151;
	
	/**
	 * Requiere Autorizacion para Anular Facturas
	 */
	public static int codigoValoresDefectoRequiereAutorizarAnularFacturas = 152;
	
	/**
	 * Concepto Para Ajuste de Entrada
	 */
	public static int codigoValoresDefectoConceptoParaAjusteEntrada = 153;
	
	/**
	 * Concepto Para Ajuste de Salida
	 */
	public static int codigoValoresDefectoConceptoParaAjusteSalida = 154;
	
	/**
	 * Permitir Modificar Conceptos Ajuste
	 */
	public static int codigoValoresDefectoPermitirModificarConceptosAjuste = 155;
	
	/**
	 * Dï¿½as restricciï¿½n citas incumplidas
	 */
	public static int codigoValoresDefectoDiasRestriccionCitasIncumplidas = 156;
	
	/**
	 * Permitir modificar la fecha de solicitud pedidos insumos
	 */
	public static int codigoValoresDefectoPermitirModificarFechaSolicitudPedidos = 157;
	
	/**
	 * Permitir modificar la fecha de solicitud traslado
	 */
	public static int codigoValoresDefectoPermitirModificarFechaSolicitudTraslado = 158;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoCodigoManualEstandarBusquedaArticulos = 159;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoImpresionMediaCarta = 160;
	
	/**
	 * Parï¿½metro para hacer requeridos los comentarios al solicitar
	 */
	public static int codigoValoresDefectoRequeridoComentariosSolicitar = 161;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoPermitirModificarTiempoTratamientoJustificacionNopos=162;
	
	/**
	 * C&oacute;digo del Parï¿½metro General Nï¿½mero de Dï¿½as para Responder Glosas
	 */
	public static int codigoValoresDefectoNumeroDiasResponderGlosas = 163;
	

	/** * C&oacute;digo del Parï¿½metro General Generar Ajuste Automatico desde el Registro de la Respuesta */
	public static int codigoValoresDefectoGenerarAjusteAutoRegRespuesta = 164;

	/** * C&oacute;digo del Parï¿½metro General Generar Ajuste Automatico desde el Registro de la Respuesta para Respuestas de Conciliacion */
	public static int codigoValoresDefectoGenerarAjusteAutoRegRespuesConciliacion = 165;

	/** * C&oacute;digo del Parï¿½metro General Formato Impresion Respuesta de Glosa */
	public static int codigoValoresDefectoFormatoImpresionRespuesGlosa = 166;

	/** * C&oacute;digo del Parï¿½metro General Imprimir Firmas en Impresion Respuesta de Glosa */
	public static int codigoValoresDefectoImprimirFirmasImpresionRespuesGlosa = 167;

	/**
	 * Path archivos planos reportes furips
	 */
	public static int codigoValoresDefectoPathArchivosPlanosFurips = 168;
	
	/**
	 * C&oacute;digo Parï¿½metro General Validar Auditor
	 */
	public static int codigoValoresDefectoValidarAuditor = 169;
	
	/**
	 * C&oacute;digo Parï¿½metro General Validar Usuario Glosa
	 */
	public static int codigoValoresDefectoValidarUsuarioGlosa = 170;
	
	/**
	 * C&oacute;digo Parï¿½metro General Nï¿½mero de Glosas Registradas Por Factura
	 */
	public static int codigoValoresDefectoNumeroGlosasRegistradasXFactura = 171;
	
	/**
	 * C&oacute;digo Parï¿½metro General Nï¿½mero de Dï¿½as para Notificar Glosa
	 */
	public static int codigoValoresDefectoNumeroDiasNotificarGlosa = 172;
	
	/**
	 * C&oacute;digo Parï¿½metro General Produccion En Paralelo Con Sistema Anterior
	 */
	public static int codigoValoresDefectoProduccionEnParaleloConSistemaAnterior = 173;
	
	/**
	 * Validar pooles fact
	 */
	public static int codigoValoresDefectoValidarPoolesFact = 174;
	
	/**
	 * Postular fechas en Respuestas DyT
	 */
	public static int codigoValoresDefectoPostularFechasEnRespuestasDyT = 175;
	
	/**
	 * Clases de Inventario para Paquetes Materiales Qx
	 */
	public static int codigoValoresDefectoClasesInventariosPaqMatQx = 176;
	
	
	/**
	 * Numero de dias busqueda Reportes
	 */
	public static int codigoValoresDefectoNumeroDiasBusquedaReportes = 177;

	
	/**
	 * 70006 - Numero Digitos Captura Numero Identificacion Pacientes
	 */
	public static int codigoValoresDefectoNumDigitosCaptNumIdPac = 178;
	
	/**
	 * Permitir interpretar ordenes de respuesta multiple sin finalizar
	 */
	public static int codigoValoresDefectoPermIntOrdRespMulSinFin = 179;
	
	/**
	 * Codigo para concocer si se Requiere de una glosa en estador espondida para inactivar una factura 
	 */
	public static int codigoValoresDefectoRequiereGlosaInactivar = 180;

	
	/**
	 * Tiempo en segundos que indica cada cuanto verificar interfaz Shaio por procesar 
	 */
	public static int codigoValoresDefectoTiemSegVeriInterShaioProc = 181;
	
	/**
	 * Tiempo en segundos que indica cada cuanto verificar interfaz Shaio por procesar 
	 */
	public static int codigoValoresDefectoMostrarEnviarEpicrisisEvol = 182;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoUsuarioaReportarenSolicitAuto=183;
	
	/**
	 * Codigo valor por defecto Minutos Caduca Cita Reservada
	 */
	public static int codigoValoresDefectoMinutosCaducaCitaReservada = 189;
	
	
	/**
	 * Codigo valor por defecto 
	 */
	public static int codigoValoresDefectoMinutosCaducaCitaAsignadasReprogramadas = 190;
	
	/**
	 *  Codigo valor por defecto
	 */
	public static int codigoValoresDefectoEjecutarProcAutoActualizacionCitasOdo = 191;
	
	/**
	 * Codigo valor por defecto
	 */
	public static int codigoValoresDefectoHoraEjecutarProcAutoActualizacionCitasOdo = 192;
	
	/**
	 * Codigo valor por defecto
	 */
	public static int codigoValoresDefectoMinutosEsperaAsignarCitasOdoCaducadas = 193;
	
	/**
	 * Codigo valor por defecto
	 */
	public static int codigoValoresDefectoEscalaPerfilPaciente= 194;
	
	/**
	 * Codigo valor por defectoUtilizan Programas odontologicos en la isntitucion
	 */
	public static int codigoValoresDefectoUtilizanProgramasOdontologicosEnInstitucion=195;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoTiempoVigenciaPresupuestoOdo=196;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoPermiteCambiarServiciosCitaAtencionOdo=197;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoValidaPresupuestoOdoContratado=198;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoConveniosAMostrarXDefectoPresupuestoOdo=199;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoTiempoMaxEsperaInactivarPresupuestoOdoSuspTemp=200;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoEjecutarProcesoAutoActualizacionEstadosOdo=201;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoHoraEjecutarProcesoAutoActualizacionEstadosOdo=202;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoMotivoCancelacionPresupuestoSuspendidoTemp=203;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoMaximoTiempoSinEvolucionarParaInactivarPlanTratamiento=205;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoPrioridadParaAplicarPromocionesOdo=206;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoDiasParaDefinirMoraXDeudaPacientes=207;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoContabilizacionRequeridoProcesoAsocioFacCuentaCapitacion=208;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoCuentaContablePagare=209;

	/**
	 * 
	 */
	public static int codigoValoresDefectoCuentaContableLetra=210;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoInstitucionRegistraAtencionExterna=211;
	
	//Anexo 922
	/**
	 * 
	 */
	public static int codigoValoresDefectoImprimirFirmasImpresionCCCapitacion=212 ;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoEncabezadoFormatoImpresionFacturaOCCCapitacion=213;


	/**
	 * 
	 */
	public static int codigoValoresDefectoPiePaginaFormatoImpresionFacturaOCCCapitacion=214;
	/**

	 * 
	 * Codigo para manejar el valor por defecto de crear cuenta en atencion de citas
	 */
	public static int codigoCrearCuentaAtencionCitas=215;
	
	//El 215 se uso con otro código que no es para valores por fefecto por eso se omite, porque quedaría repetido en el case
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoImprimirFirmasEnImpresionCC=216;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoNumeroMesesAMostrarEnReportesPresupuestoCapitacion=217;
	

	/**
	 * Codigo para el parametro Tiempo maximo para el reingreso de urgencias
	 * */
	public static final int codigoTiempoMaximoReingresoUrgencias = 218;
	
	/**
	 * Codigo para el parametro Tiempo maximo para el reingreso de hospitalizaciï¿½n
	 * */
	public static final int codigoTiempoMaximoReingresoHospitalizacion = 219;
	
	/**
	 * Codigo para el parametro Permitir Facturar Reingresos Independientes
	 * */
	public static final int codigoPermitirFacturarReingresosIndependientes = 220;
	
	/**
	 * Codigo para el parametro Liberar cama de hospitalizacion despues de facturar
	 * */
	public static final int codigoLiberarCamaHospitalizacionDespuesFacturar = 221;
	
	/**
	 * Codigo Para el Parametro Controla Interpretacion Procedimientos para Permitir Evolucion
	 */
	public static final int codigoControlaInterpretacionProcedimientosParaEvolucionar = 222;
	
	/**
	 * Codigo Para el Parametro Validar Registro Evoluciones para Generar Ordenes
	 */
	public static final int codigoValidarRegistroEvolucionesParaGenerarOrdenes = 223;
	
	
	//Fin Anexo 992
	
	//Los valores del 218 al 223 ya fueron usados por otros codigos
	
	//Anexo 958
	/**
	 * 
	 */
	public static int codigoValoresDefectoReciboCajaAutomaticoGeneracionFacturaVaria=224;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoConceptoIngresoFacturasVarias=225;
	
	//Fin Anexo 958
	
	//Anexo 888
	/**
	 * 
	 */
	public static int codigoValoresDefectoEsRequeridoProgramarCitaAlContratarPresupuestoOdon=226;

	/**
	 * 
	 */
	public static int codigoValoresDefectoMotivoAnulacionSolicitudAutorizacionDescuentoPresupeustoOdontologico=227;
	
	//Fin anexo 888
	
	
	//Anexo 888 Parte II
	/**
	 * 
	 */
	public static int codigoValoresDefectoRequeridoProgramarCitaControlAlTerminarPresupuestoOdon=228;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoRequeridoProgramarCitaControlAlTerminarPresupuestoOdonServicio=229;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoTiempoDiasParaAgendarCitaControlAlTerminarPresupuestoOdon=230;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoInstitucionManejaFacturacionAutomatica=231;
	
	//Fin Anexo 888 Pt II
	
	//Anexo 959
	/**
	 * 
	 */
	public static int codigoValoresDefectoManejaConsecutivoFacturaPorCentroAtencion=232;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoManejaConsecutivosTesoreriaPorCentroAtencion=233;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoTamanioImpresionRC=234;
	
	public static int codigoValoresDefectoRequiereAperturaCierreCaja=235;
	
	/**
	 * Codigo para manejar el valor por defecto de Institucion Maneja Multas Por Incumplimiento
	 */
	public static int codigoInstitucionManejaMultasPorIncumplimiento=236;
	
	/**
	 * Codigo para manejar el valor por defecto de Bloquea Citas Reserva Asign Reprog Por Incump
	 */
	public static int codigoBloqueaCitasReservaAsignReprogPorIncump=237;
	
	/**
	 * Codigo para manejar el valor por defecto de Bloquea Atencion Citas Por Incump
	 */
	public static int codigoBloqueaAtencionCitasPorIncump=238;
	
	/**
	 * Codigo para manejar el valor por defecto de Fecha Inicio Control Multas Incumplimiento citas
	 */
	public static int codigoFechaInicioControlMultasIncumplimientoCitas=239;
	
	/**
	 * Codigo para manejar el valor por defecto de Valor Multa Por Incumplimiento Citas
	 */
	public static int codigoValorMultaPorIncumplimientoCitas=240;
	
	/**
	 * 
	 */
	public static int codigonumeroMaximoDiasGenOrdenesAmbServicios=241;
	
	/**
	 * C&oacute;digo para el manejo especialde Instituciones de Odontologï¿½a
	 */
	
	public static int codigoManejoEspecialInstitucionesOdontologia=242;
	
	/**
	 * C&oacute;digo para manejar el Mï¿½ximo Nï¿½mero Cuotas Financiaciï¿½n
	 */
	public static int codigoMaximoNumeroCuotasFinanciacion = 245;
	
	/**
	 * C&oacute;digo para el Mï¿½ximo nï¿½mero de dï¿½as de financiaciï¿½n por cuota
	 */
	public static int codigoMaximoNumeroDiasFinanciacionPorCuota= 246;
	
	/**
	 * C&oacute;digo para el Formato documentos de garantï¿½a - pagarï¿½
	 */
	public static int codigoFormatoDocumentosGarantia_Pagare= 247;
	
	public static int  codigoValoresDefectoAreaAperturaCuentaAutoPYP =248;
	
	
	/**
	 * C&oacute;digo para la ocupacion odontï¿½logo
	 */
	public static int codigoOcupacionOdontologo=249;
	
	/**
	 * C&oacute;digo para la ocupacion Auxiliar de odontï¿½logo
	 */
	public static int codigoOcupacionAuxiliarOdontologo=250;
	
	/**
	 * C&oacute;digo para la edad final niï¿½ez
	 */
	public static int codigoEdadFinalNinez=251;
	
	/**
	 * C&oacute;digo para la edad final niï¿½ez
	 */
	public static int codigoEdadInicioAdulto=252;
	
	
    public static int codigoMultiploMinGeneracionCita=253;
	
	public static int codigoNumDiasAntFActualAgendaOd=254;
	
	/**
	 * Código para manejar el formato para Impresión de Conciliación de Glosas
	 */
	public static int codigoFormatoImpresionConciliacion=256;
	
	/**
	 * Código para el manejo de Glosas Reiteradas
	 */
	public static int codigoValidarGlosaReiterada=257;

	
	/**
	 * C&oacute;digo para determinar si se obliga a dar incapacidad a los pacientes hospitalizados
	 */
	public static int codigoObligarRegIncapaPacienteHospitalizado=258;

	
	/**
	 * 
	 */
	public static int codigoValoresDefectoAprobarGlosaRegistro=259;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoRequeridoProgramarProximaCitaEnAtencion=261;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoActivarBotonGenerarSolicitudOrdenAmbulatora=262;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoEsRequeridoTestigoSolicitudAceptacionTrasladoCaja=263;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoInstitucionManejaCajaPrincipal=264;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoInstitucionManejaTrasladoOtraCajaRecaudo=265;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoInstitucionManejaEntregaATransportadoraValores=266;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoTrasladoAbonosPacienteSoloPacientesConPresupuestoContratado=267;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoControlarAbonoPacientePorNroIngreso=268;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoValidaEstadoContratoNominaALosProfesionalesSalud=269;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoManejaInterfazUsuariosSistemaERP=270;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoRequierGenerarSolicitudCambioServicio=271;
	
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoLasCitasDeControlSePermitenAsignarA=272;
	

	
	/**
	 * 
	 */
	public static int codigoManejaVentaTarjetaClienteOdontosinEmision=274;

	/**
	 * Valor por defecto para mostrar el componente flash del &iacute;ndice de placa
	 * en la funcionalidad tratamientos odontol&oacute;ogicos vieja.
	 */
	public static int codigoValorDefectoMostrarGraficaCalculoIndicePlaca=275;
	
	
	
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoValidarPacienteParaVentaTarjeta=276;

	
	/**
	 * 
	 */
	public static int codigoValoresDefectoReciboCajaAutomaticoVentaTarjeta=277;
	
	
	/**
	 * Codigo log valores por Defecto Cancela Citas Odontologicaa a reprogramas
	 */
	public static int codigoValoresDefectoCancelaCitasOdontologicasReprogramar=278;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoPermitirRegistrarReclamacionCuentasNoFacturadas=279;

	
	/**
	 * 
	 */
	public static int codigoValoresDefectoPermitirFacturarReclamarCuentasConRegistroPendientes=280;
	
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoPermitirOrdenarMedicamentosPacienteUrgenciasConEgreso=281;

	
	/**
	 * 
	 */
	public static int codigoValoresDefectoMostrarAdminMedicamentosArticulosDespachoCero=282;

	
	/**
	 * 
	 */
	public static int codigoValoresDefectoFormatoFacturaVaria=283;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoModificarFechaHoraInicioAtencionOdonto = 284;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada = 285;
	
	/**
	 * Codigo para manejar la prioridad de la entidad subcontratada.
	 */
	public static int codigoValoresDefectoPrioridadEntidadSubcontratada=286;
	
	/**
	 * Codigo para manejar si requiere autorización capitación subcontratada para facturar
	 */
	public static int codigoValoresDefectoRequiereAutorizacionCapitacionSubcontratadaParaFacturar=287;
	
	/**
	 * Codigo para manejar si maneja consecutivo facturas varias por centro de atención.
	 */
	public static int codigoValoresDefectoManejaConsecutivoFacturasVariasPorCentroAtencion=288;
	
	/**
	 * código del parámetro para definir el formato de impresión de una autorización de entidad subcontratada
	 */
	public static int codigoValoresDefectoFormatoImpresionAutorEntidadSub = 289;
	
	/**
	 * código del parámetro para definir el encabezado del formato de impresión de una autorización de entidad subcontratada
	 */
	public static int codigoValoresDefectoEncFormatoImpresionAutorEntidadSub  = 290;
	
	/**
	 * código del parámetro para definir el encabezado del formato de impresión de una autorización de entidad subcontratada
	 */
	public static int codigoValoresDefectoPiePagFormatoImpresionAutorEntidadSub = 291;
	
	
	/**
	 * Código del parámetro para definir la vigencia que tendrá la autorización.
	 */
	public static int codigoValoresDefectoDiasVigenciaAutorIndicativoTemp = 292;
	
	/**
	 * Código del parámetro para definir los días de prórroga de una autorización
	 */
	public static int codigoValoresDefectoDiasProrrogaAutorizacion = 293;
	
	
	/**
	 * Código del parámetro para definir los días de cálculo de la fecha de vencimiento de la Autorización de servicios
	 */
	public static int codigoValoresDefectoDiasCalcularFechaVencAutorizacionServicio= 294;
		

	/**
	 * Código del parámetro para definir los días de cálculo de la fecha de vencimiento de la Autorización de artículos
	 */
	public static int codigoValoresDefectoDiasCalcularFechaVencAutorizacionArticulo = 295;
	
	/**
	 * Código del parámetro para definir los días de vigencia para solicitar una nueva autorización de estancia
	 */
	public static int codigoValoresDefectoDiasVigentesNuevaAutorizacionEstanciaSerArt = 296;
		
	/**
	 * Código del parámetro para definir la hora en la cual se debe ejecutar el proceso del cierre de capitación
	 */
	public static int codigoValoresDefectoHoraProcesoCierreCapitacion = 297;
	
	
	/**
	 * Código del parámetro para facturar y registrar reclamaciones en cuentas con R.A.T. o R.E.C. asociados en estado pendiente
	 */
	public static int codigoValoresDefectoPermitirfacturarReclamCuentaRATREC = 298;
	
	
	/**
	 * Código del parámetro para  definir si se muestran todas las solicitudes asociadas a la Factura en la funcionalidad Detalle de Glosas Confirmadas.
	 */
	public static int codigoValoresDefectoMostrarDetalleGlosasFacturaSolicFactura = 299;
	
	/**
	 * Código del parámetro para  definir el formato Impresión Reserva Cita Odontológica
	 */
	public static int codigoValoresDefectoFormatoImpReservaCitaOdonto = 300;
	
	
	/**
	 * Código del parámetro para  definir el formato Impresión de asignacion de Citas Odontológicas
	 */
	public static int codigoValoresDefectoFormatoImpAsignacionCitaOdonto = 301;
	
	/**
	 * Código del parámetro para validar que exista información de cierre de órdenes médicas
	 */
	public static int codigoValoresDefectoFechaInicioCierreOrdenMedica = 302;
	
	/**
	 * Código del parámetro para definir el esquema tarifario de servicios con el cual se realizará la valorización de las ordenes
	 */
	public static int codigoValoresDefectoEsquemaTariServiciosValorizarOrden = 303;
	
	/**
	 * Código del parámetro para definir si se debe o no generar una cita programada al generar la solicitud de interconsultas odontológicas.
	 */
	public static int codigoValoresDefectoSolicitudCitaInterconsultaOdontoCitaProgramada = 304;
	
	/**
	 * Código del parámetro para definir la hora de ejecución del proceso para la inactivación de usuario por inactividad en el sistema
	 */
	public static int codigoValoresDefectoHoraEjecProcesoInactivarUsuarioInacSistema = 305;
	
	/**
	 * Código del parámetro para definir la hora de ejecución del proceso para la caducidad de contraseña por inactividad en el sistema
	 */
	public static int codigoValoresDefectoHoraEjeProcesoCaduContraInacSistema = 306;
	
	/**
	 * Código del parámetro Número de días vigencia de contraseña usuario.
	 */
	public static int codigoValoresDefectoDiasVigenciaContraUsuario = 307;
	
	/**
	 * Código del parámetro Número de días para finalizar vigencia de contraseña y mostrar alerta
	 */
	public static int codigoValoresDefectoDiasFinalesVigenciaContraMostrarAlerta = 308;
	

	/**
	 * 
	 */
	public static int codigoValoresDefectoNumMaximoReclamacionesAccEventoXFactura = 309;
	

	public static int codigoValoresDefectoHacerRequeridoValorAbonoAplicadoAlFacturar=310;
	
	/**
	 * Código del parámetro para definir el esquema tarifario de medicamentos e insumos con el cual se realizará la valorización de las ordenes
	 */
	public static int codigoValoresDefectoEsquemaTariMedicamentosValorizarOrden = 311;
	
	
	
	
	/**
	 * Código del parámetro para definir permitir_modificar_datos_usu_capitados
	 */
	public static int codigoValoresDefectoPermitirModificarDatosUsuariosCapitados = 312;
	
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoManejaHojaAnestesia=313;
	
	
	/**
	 * Código del parámetro para definir permitir_modificar_datos_usu_capitados_modificar_cuenta
	 */
	public static int codigoValoresDefectoPermitirModificarDatosUsuariosCapitadosModificarCuenta = 314;
	
	
	/** * */
	public static int codigoValoresDefectoEsquemaTarifarioAutocapitaSubCirugiasNoCurentos = 315;

	/** * */
	public static int codigoValoresDefectoValidarDisponibilidadSaldoPresupuestoAutorizacionesCapotaAutomatica = 317; 
	
	/**
	 * Código del parámetro para definir hacer_requerida_seleccion_centro_atencion_asignado_subir_paciente_individual
	 */
	public static int codigoValoresDefectoHacerRequeridaSeleccionCentroAtencionAsignadoSubirPacienteIndividual = 319;
	
	/**
	 * Código del parámetro para definir maneja_consecutivos_notas_pacientes_centro_atención
	 */
	public static int codigoValoresDefectoManejaConsecutivosNotasPacientesCentroAtencion = 320;

	/**
	 * Código del parámetro para definir naturaleza_notas_pacientes_manejar
	 */
	public static int codigoValoresDefectoNaturalezaNotasPacientesManejar = 321;
	
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoServicioManejoTransportePrimario = 322;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoServicioManejoTransporteSecundario = 323;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoManejoOxigenoFurips = 324;
	
	/**
	 * 
	 */
	public static int codigoValoresDefectoPermitirRecaudosCajaMayor = 325;
	
	
	/**
	 * Parámetro que permite seleccionar la Vía de Ingreso por Tipo de Orden: Orden Ambulatoria
	 * 
	 **/
	public static int codigoValoresDefectoViaIngresoValidacionesOrdenesAmbulatorias = 326;
	
	
	/**
	 * Parámetro que permite seleccionar la Vía de Ingreso por Tipo de Orden: Petición
	 * 
	 **/
	public static int codigoValoresDefectoViaIngresoValidacionesPeticiones = 327;
	
	
	/**
	 *Parametro que permite ingresar los dias de maximo de consulta de ingresos 
	 */
	public static int codigoValoresDefectoMaximoDiasConsultarIngresosHistoriaClinica=328;
	
	/**
	 * Codigo del parametro para definir los días maximos de prorroga de una autorizacion de Articulo
	 */
	public static int codigoValoresDefectoDiasMaxProrrogaAutorizacionArticulo = 329;
	
	/**
	 * Codigo del parametro para definir los días maximos de prorroga de una autorizacion de Servicios
	 */
	public static int codigoValoresDefectoDiasMaxProrrogaAutorizacionServicio = 330;
	
	
	/**
	 * Parámetro que permite seleccionar el Tipo de Paciente por Tipo de Orden: Orden Ambulatoria
	 * 
	 **/
	public static int codigoValoresDefectoTipoPacienteValidacionesOrdenesAmbulatorias = 331;
	
	
	/**
	 * Parámetro que permite seleccionar el Tipo de Paciente por Tipo de Orden: Petición
	 * 
	 **/
	public static int codigoValoresDefectoTipoPacienteValidacionesPeticiones = 332;
	
	
	/**
	 * nombre del parametro para definir los Meses Maximo para Administrar Autorizaciones de Capitacion vencidas
	 */
	public static int codigoValoresDefectoMesesMaxAdminAutoCapVencidas = 333;
	/*--------------------------------------------------------------------------------------------------------------*
	 * SECCION DE VALORES
	 *
	 
	 /**
	 * Nombre para manejar el valor por defecto en la validadcion de la ocupacion MEdico Especialista para ingreso Justificacion NO POs articulos
	 */
	public static String nombreValoresDefectoValidacionOcupacionJustificacionNoPosArticulos = "validar_ocupacion_justificacion_no_pos_articulo";
	
	/**
	 * Valor por defecto para la validadcion de la ocupacion MEdico Especialista para ingreso Justificacion NO POs articulos
	 */
	public static String valorValoresDefectoValidacionOcupacionJustificacionNoPosArticulos = "@@";
	
	/**
	 * Nombre para manejar el valor por defecto en la validadcion de la ocupacion MEdico Especialista para ingreso Justificacion NO POs servicios
	 */
	public static String nombreValoresDefectoValidacionOcupacionJustificacionNoPosServicios = "validar_ocupacion_justificacion_no_pos_servicio";
	
	/**
	 * Valor por defecto para manejar el valor por defecto del parametro que permite modificar o no el tiempo de tratamiento para el formato nopos
	 */
	public static String valorValoresDefectoValidacionOcupacionJustificacionNoPosServicios = "@@";
	
	/**
	 * Nombre para manejar el valor por defecto del parametro que permite modificar o no el tiempo de tratamiento para el formato nopos
	 */
	public static String nombreValoresDefectoPermitirModificarTiempoTratamientoJustificacionNopos = "permitir_modificar_tiempo_tratamiento_jus_nopos";
	
	/**
	 * Valor por defecto para la validadcion de la ocupacion MEdico Especialista para ingreso Justificacion NO POs servicios
	 */
	public static String valorValoresDefectoPermitirModificarTiempoTratamientoJustificacionNopos = "@@";
	
	/**
	 * Nombre para manejar el valor por defecto en la generacion de planos forecat en la generacion de rips
	 */
	public static String nombreValoresDefectoGeneracionForecatEnRips = "gen_forecat_rips";
	
	/**
	 * Valor por defecto para la generacion de planos forecat en la generacion de rips
	 */
	public static String valorValoresDefectoGeneracionForecatEnRips = ConstantesBD.acronimoNo+"@@"+ConstantesBD.acronimoNo;
	
	/**
	 * Nombre para manejar el valor del control sobre la vigilancia epidemiologica de accidente rabico
	 */
	public static String nombreValoresDefectoVigilarAccRabico = "notificar_acc_rabico";
	
	/**
	 * Valor para el parametro del control sobre la vigilancia epid. de accidente rabico
	 */
	public static String valorValoresDefectoVigilarAccRabico = "true@@true";

	/**
	 * Nombre para manejar los valores por defecto de direccion del paciente
	 */
	public static String nombreValoresDefectoDireccion = "direccion_paciente";
	
	
	/**
	 * Nombre para manejar los valores por defecto del path de reportes para Manejo Paciente
	 */
	public static String nombreValoresDefectoPath = "path_archivo_plano";
	
	/**
	 * Nombre para manejar los valores por defecto del path de reportes para Inventarios
	 */
	public static String nombreValoresDefectoPathInventarios = "path_archivo_plano_inventario";
	
	/**
	 * Nombre para manejar los valores por defecto de direccion del paciente
	 */
	public static String valorValoresDefectoDireccion = "@@";
	
	/**
	 * Nombre para manejar los valores por defecto del Path de al Archivo Plano
	 */
	public static String valorValoresDefectoPath = "@@";

	/**
	 * Nombre para manejar los valores por defecto del Path de al Archivo Plano
	 */
	public static String valorValoresDefectoPathInventarios = "@@";
	
	/**
	 * Nombre para manejar los valores por defecto del centro de costo al
	 * ingreso del paciente
	 */
	public static String nombreValoresDefectoCentroCostoConsultaExterna = "centro_costo_consulta_externa";
	/**
	 * Nombre para manejar los valores por defecto del centro de costo al
	 * ingreso del paciente
	 */
	public static String valorValoresDefectoCentroCostoConsultaExterna = "4@@Consulta Externa";

	/**
	 * Nombre para manejar los valores por defecto de la causa externa en el
	 * ingreso del paciente
	 */
	public static String nombreValoresDefectoCausaExterna = "causa_externa";
	/**
	 * Nombre para manejar los valores por defecto de la causa externa en el
	 * ingreso del paciente
	 */
	public static String valorValoresDefectoCausaExterna = "1@@Accidente de Trabajo";

	/**
	 * Nombre para manejar los valores por defecto de la finalida de la consulta
	 * en las valoraciones
	 */
	public static String nombreValoresDefectoFinalidadConsulta = "finalidad";
	/**
	 * Nombre para manejar los valores por defecto de la finalida de la consulta
	 * en las valoraciones
	 */
	public static String valorValoresDefectoFinalidadConsulta = "10@@No aplica";

	/**
	 * Nombre para manejar los valores por defecto del pais de nacimiento
	 * del paciente
	 */
	public static String nombreValoresDefectoPaisNacimiento = "pais_nacimiento";
	/**
	 * Nombre para manejar los valores por defecto del Pais de nacimiento
	 * del paciente
	 */
	public static String valorValoresDefectoPaisNacimiento = "57-Colombia@@Colombia";
	
	/**
	 * Nombre para manejar los valores por defecto del pais de vivenda del
	 * paciente
	 */
	public static String nombreValoresDefectoPaisResidencia = "pais_residencia";
	/**
	 * Nombre para manejar los valores por defecto del pais de vivenda del
	 * paciente
	 */
	public static String valorValoresDefectoPaisResidencia = "57-Colombia@@Colombia";
	
	/**
	 * Nombre para manejar los valores por defecto de la ciudad de naciemeiento
	 * del paciente
	 */
	public static String nombreValoresDefectoCiudadNacimiento = "ciudad_nacimiento";
	/**
	 * Nombre para manejar los valores por defecto de la ciudad de naciemeiento
	 * del paciente
	 */
	public static String valorValoresDefectoCiudadNacimiento = "8-1-BARRANQUILLA (DISTRITO)@@BARRANQUILLA (DISTRITO)";

	/**
	 * Nombre para manejar los valores por defecto de la ciudad de vivenda del
	 * paciente
	 */
	public static String nombreValoresDefectoCiudadResidencia = "ciudad_vivienda";
	/**
	 * Nombre para manejar los valores por defecto de la ciudad de vivenda del
	 * paciente
	 */
	public static String valorValoresDefectoCiudadResidencia = "11-21-BOGOTA D.C.@@BOGOTï¿½ D.C.";

	/**
	 * Nombre para manejar los valores por defecto del domicilio del paciente
	 * (Rural ï¿½ Urbano)
	 */
	public static String nombreValoresDefectoDomicilio = "zona_domicilio";
	/**
	 * Nombre para manejar los valores por defecto del domicilio del paciente
	 * (Rural ï¿½ Urbano)
	 */
	public static String valorValoresDefectoDomicilio = "U@@Urbano";

	/**
	 * Nombre para manejar los valores por defecto de la ocupacion del paciente
	 * en el ingreso
	 */
	public static String nombreValoresDefectoOcupacion = "ocupacion_paciente";
	/**
	 * Nombre para manejar los valores por defecto de la ocupacion del paciente
	 * en el ingreso
	 */
	public static String valorValoresDefectoOcupacion = "440@@AGENTES: DE SEGUROS INMOBILIARIAS CAMBIO";

	/**
	 * Nombre para manejar los valores por defecto del tipo de identificaciï¿½n en
	 * en ingreso
	 */
	public static String nombreValoresDefectoTipoId = "tipo_id_paciente";
	/**
	 * Nombre para manejar los valores por defecto del tipo de identificaciï¿½n en
	 * en ingreso
	 */
	public static String valorValoresDefectoTipoId = "CC@@Cï¿½dula de Ciudadanï¿½a";

	/**
	 * Nombre para manejar los valores por defecto para el modo de ingreso de la
	 * edad
	 */
	public static String nombreValoresDefectoIngresoEdad = "fecha_nacimiento";
	/**
	 * Nombre para manejar los valores por defecto para el modo de ingreso de la
	 * edad
	 */
	public static String valorValoresDefectoIngresoEdad = "false@@false";

	/**
	 * Nombre para manejar los valores por defecto para definir si se ingresa el
	 * nï¿½mero de historia clï¿½nica
	 */
	public static String nombreValoresDefectoHistoriaClinica = "historia_clinica";
	/**
	 * Nombre para manejar los valores por defecto para definir si se ingresa el
	 * nï¿½mero de historia clï¿½nica
	 */
	public static String valorValoresDefectoHistoriaClinica = "true@@true";

	/**
	 * Nombre para manejar los valores por defecto para activar el centinela a
	 * la salida
	 */
	public static String nombreValoresDefectoCentinela = "flag_centinela";
	/**
	 * Nombre para manejar los valores por defecto para activar el centinela a
	 * la salida
	 */
	public static String valorValoresDefectoCentinela = "true@@true";

	/**
	 * Nombre para manejar los valores por defecto del centro de costo de la via
	 * de ingreso urgencias
	 */
	public static String nombreValoresDefectoCentroCostoUrgencias = "cc_observacion";
	/**
	 * Nombre para manejar los valores por defecto del centro de costo de la via
	 * de ingreso urgencias
	 */
	public static String valorValoresDefectoCentroCostoUrgencias = "3@@Urgencias";

	/**
	 * Nombre para manejar los valores por defecto del centro de costo de la via
	 * de ingreso urgencias
	 */
	public static String nombreValoresDefectoOcupacionSolicitada = "ocupacion_solicitada";
	
	/**
	 * Nombre para manejar los valores por defecto del centro de costo de la via
	 * de ingreso urgencias
	 */
	public static String valorValoresDefectoOcupacionSolicitada = "1@@Medico";

	/**
	 * Nombre para manejar los valores por defecto 
	 */
	public static String nombreValoresDefectoCentroCostoTerceros = "centro_costo_terceros";
	
	/**
	 * Nombre para manejar los valores por defecto 
	 */
	public static String valorValoresDefectoCentroCostoTerceros = "-1@@Seleccione";
	
	/**
	 * Nombre para manejar los valores por defecto 
	 */
	public static String nombreValoresDefectoHorasReproceso = "horas_reproceso";
	/**
	 * Nombre para manejar los valores por defecto 
	 */
	public static String valorValoresDefectoHorasReproceso = "@@";
	
	/**
	 * Nombre para manejar los valores por defecto 
	 */
	public static String nombreValoresDefectoRequiereAperturaCierreCaja = "requiere_apertura_cierre_caja";
	/**
	 * Nombre para manejar los valores por defecto 
	 */
	public static String valorValoresDefectoRequiereAperturaCierreCaja = "@@";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoRequeridoProgramarProximaCitaEnAtencion = "requerido_programar_proxima_cita_en_atencion";
	/**
	 * 
	 */
	public static String valorValoresDefectoRequeridoProgramarProximaCitaEnAtencion = ConstantesBD.acronimoNo + "@@"+ ConstantesBD.acronimoNo;

	/**
	 * Nombre para manejar los valores por defecto del centro de costo de la via
	 * de ingreso urgencias
	 */
	public static String nombreValoresDefectoEstadoCamaEgreso = "estado_cama_egreso";
	/**
	 * Nombre para manejar los valores por defecto del centro de costo de la via
	 * de ingreso urgencias
	 */
	public static String valorValoresDefectoEstadoCamaEgreso = "0@@Disponible";

	/**
	 * Nombre para manejar el valor de UVR para los servicios ISS
	 */
	public static String nombreValoresDefectoValorUVR = "valor_UVR";
	/**
	 * Nombre para manejar el valor de UVR para los servicios ISS
	 */
	public static String valorValoresDefectoValorUVR = "1@@1";
	
	/**
	 * Nombre de Tipo Usuario a reportar en Solicitud Autorizacion
	 */
	public static String nombreValoresporDefectoUsuarioaReportarenSolicitAuto="tip_usuario_report_sol_autorizacion";
     
	/**
	 * Valor por defecto a Tipo de Usuario a reportar en Solicitud Autorizacion
	 */
	public static String valorValoresDefectoUsuarioaReportarenSolicitAuto = "@@";
	
//	Se elimina debido a q ya no se esta utilizando el parametro Carnet_Requerido
	/**
	 * Nombre para manejar si el carnet es requerido o no
	 */
	/**public static String nombreValoresDefectoCarnetRequerido = "carnet_requerido";*/

//	Se elimina debido a q ya no se esta utilizando el parametro Carnet_Requerido
	/**
	 * Nombre para manejar si el carnet es requerido o no
	 */
	/**public static String valorValoresDefectoCarnetRequerido = "true@@true";*/

	
	/**
	 * Nombre para manejar el codigo de servicio asignado a las solicitudes de
	 * farmacia
	 */
	public static String valorValoresDefectoCodigoServicioFarmacia = "1@@1";

	/**
	 * Nombre para manejar el codigo de servicio asignado a las solicitudes de
	 * farmacia
	 */
	public static String nombreValoresDefectoValidarInterpretadas = "validar_interpretada";
	/**
	 * Nombre para manejar el codigo de servicio asignado a las solicitudes de
	 * farmacia
	 */
	public static String valorValoresDefectoValidarInterpretadas = "true@@true";
	
	/**
	 * Nombre para manejar el codigo  de los valores por defecto
	 */
	public static String nombreValoresDefectoValidarContratosVencidos="validar_contratos_vencidos";
	/**
	 * Nombre para manejar el codigo  de los valores por defecto
	 */
	public static String valorValoresDefectoValidarContratosVencidos="@@";

	/**
	 * Nombre para manejar el tipo de topes por paciente
	 */
	public static String nombreValoresDefectoManejoTopesPaciente="manejo_topes_paciente";
	/**
	 * Nombre para manejar el tipo de topes por paciente
	 */
	public static String valorValoresDefectoManejoTopesPaciente="@@";

	/**
	 * Nombre para manejar los valores por defecto del centro de costo al
	 * ingreso del paciente
	 */
	public static String nombreValoresDefectoCentroCostoAmbulatorios = "centro_costo_ambulatorios";
	/**
	 * Nombre para manejar los valores por defecto del centro de costo al
	 * ingreso del paciente
	 */
	public static String valorValoresDefectoCentroCostoAmbulatorios = "18@@Ambulatorios";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoServicioManejoTransportePrimario = "servicios_manejo_transporte_primario";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoServicioManejoTransportePrimario =  "-1@@Seleccione";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoServicioManejoTransporteSecundario = "servicios_manejo_transporte_secundario";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoServicioManejoTransporteSecundario = "-1@@Seleccione";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoManejoOxigenoFurips = "manejo_oxigeno_furips";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoManejoOxigenoFurips = "-1@@Seleccione";
	
	
	
	/**
	 * Nombre para manejar los valores por defecto de la justificacion de servicios requerida
	 */
	public static String nombreValoresDefectoJustificacionServiciosRequerida="justificacion_servicio_requerida";
	
	/**
	 * Nombre para manejar los valores por defecto de la justificacion de servicios requerida
	 * (Valor por defecto)
	 */
	public static String valorValoresDefectoJustificacionServiciosRequerida="true@@true";

	/**
	 * Nombre para manejar los valores por defecto del ingreso de la
	 * cantidad de artï¿½culos en farmacia
	 */
	public static String nombreValoresDefectoIngresoCantidadFarmacia="ingreso_cantidad_farmacia";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoDiasAlertaVigencia="dias_alerta_vigencia";
	
	/**
	 * Nombre para manejar rips por factura
	 */
	public static String nombreValoresDefectoRipsPorFactura="rips_por_factura";
	
	/**
	 * Nombre para manejar rips por factura
	 */
	public static String nombreValoresDefecto="rips_por_factura";
	
	/**
	 * Nombre para manejar fecha corte saldo inicial cartera
	 */
	public static String nombreValoresDefectoFechaCorteSaldoInicialC="fecha_corte_saldo";
	
	/**
	 * Nombre para manejar fecha corte saldo inicial cartera capitacion
	 */
	public static String nombreValoresDefectoFechaCorteSaldoInicialCCapitacion="fecha_corte_saldo_capitacion";
	
	/**
	 * Nombre para manejar tope consecutivo CxC saldo inicial
	 */
	public static String nombreValoresDefectoTopeConsecutivoCxCSaldoI="tope_conse_cxc_saldo";
	
	/**
	 * Nombre para manejar tope consecutivo CxC saldo inicial capitacion
	 */
	public static String nombreValoresDefectoTopeConsecutivoCxCSaldoICapitacion="tope_conse_cxc_saldo_capitacion";
	
	/**
	 * Nombre para manejar el valor por defecto del Dato de la cuenta requerido en la reserva de cita 
	 */
	public static String nombreValoresDefectoDatosCuentaRequeridoReservaCitas="datos_cuenta_requerido_reserva_citas";
	
	
	/**
	 * Nombre para manejar el valor por defecto del Dato de la cuenta requerido en la reserva de cita 
	 */
	public static String nombreValoresDefectoRedNoAdscrita="red_no_adscrita";
	
	/**
	 * Nombre para manejar MaxPageItems
	 */
	public static String nombreValoresDefectoMaxPageItems="max_page_items";
	
	/**
	 * Nombre para manejar MaxPageItems
	 */
	public static String nombreValoresDefectoMaxPageItemsEpicrisis="max_page_items_epicrisis";

	/**
	 * Nombre para manejar los archivos planos facturacion
	 */
	public static String nombreValoresDefectoPathArchivosPlanosFacturacion="path_archivos_planos_facturacion";
	
	/**
	 * Nombre para manejar los archivos planos facturacion
	 */
	public static String nombreValoresDefectoPathArchivosPlanosFurips="path_archivos_planos_furips";
	
	/**
	 * Nombre para manejar los archivos planos Manejo Paciente
	 */
	public static String nombreValoresDefectoPathArchivosPlanosManejoPaciente="path_archivos_planos_manejo_paciente";
	
	/**
	 * Nombre para manejar excepciones de rips en consultorios
	 */
	public static String nombreValoresDefectoExcepcionRipsConsultorios="excepciones_rips_consultorios";
	
	/**
	 * Nombre para manejar ajuste de la cuenta de cobro radicada
	 */
	public static String nombreValoresDefectoAjustarCuentaCobroRadicada="ajustar_cuenta_cobro_radicada";
	
	/**
	 * Nombre para manejar el campo de cerrar cuenta en la anulaciï¿½n de la factura
	 */
	public static String nombreValoresDefectoCerrarCuentaAnulacionFactura="cierre_cuenta_anulacion_factura";
	
	/**
	 * Nombre para manejar el campo de barrio de residencia
	 */
	public static String nombreValoresDefectoBarrioResidencia="barrio_residencia";
	
	/**
	 * Nombre para manejar el campo de Materiales por Acto
	 */
	public static String nombreValoresDefectoMaterialesPorActo="materiales_por_acto";
	
	/**
	 * Nombre para manejar el campo de la Hora Inicial de Programacion Salas
	 */
	public static String nombreValoresDefectoHoraInicioProgramacionSalas="hora_inicio_programacion_salas";

	/**
	 * Nombre para manejar el campo de la Hora Inicial de Programacion Salas
	 */
	public static String nombreValoresDefectoHoraFinProgramacionSalas="hora_fin_programacion_salas";
	/**
	 * Liquidacion automatica de cirugias
	 */
	public static String nombreValoresDefectoLiquidacionAutomaticaCirugias="liquidacion_automatica_cirugias";
	/**
	 * 
	 */
	public static String nombreValoresDefectoLiquidacionAutomaticaNoQx="liquidacion_automatica_no_qx";
	/**
	 * 
	 */
	public static String nombreValoresDefectoManejoProgramacionSalasSolicitudes= "manejo_programacion_salas_solicitudes_dyt";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoRequridaDescripcionEspecialidadCirugias= "requerida_descripcion_especialidad_cirugias";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoRequridaDescripcionEspecialidadNoCruentos= "requerida_descripcion_especialidad_no_cruentos";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoAsocioAyudantia= "asocio_ayudantia";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoIndicativoCobrableHonorariosCirugia= "indicativo_cobrable_honorarios_cirugia";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoIndicativoCobrableHonorariosNoCruento= "indicativo_cobrable_honorarios_no_cruento";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoAsocioCirujano= "asocio_cirujano";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoAsocioAnestesia= "asocio_anestesia";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoModificarInformacionDescripcionQuirurgicaiDiagnosticos= "modificar_informacion_descripcion_quirurgica";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoMinutosRegistroNotasCirugia= "minutos_regisro_notas_cirugia";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoMinutosRegistroNotasNoCruentos= "minutos_regisro_notas_no_cruentos";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoModificarInformacionQuirurgica= "modificar_informacion_general_quirurgica";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoMinutosMaximosRegistroAnestesia= "minutos_maximos_registro_anestesia";
	/**
	 * Nombre para el manejo de consecutivos en las transacciones de inventarios
	 */
	public static String nombreValoresDefectoManejoConsecutivoTransInv="manejo_conse_trans_inventarios";
	/**
	 * Nombre para el porcentaje de alerta en diferencia en los costos de inventarios
	 */
	public static String nombreValoresDefectoPorcentajeAlertaCostosInv = "porcentaje_costos_inventario";
	/**
	 * Nombre de transacciï¿½n utilizado para las solicitudes de pacientes
	 */
	public static String nombreValoresDefectoCodigoTransSoliPacientes = "codigo_trans_soli_pacientes";
	/**
	 * Nombre de transacciï¿½n utilizado para las devoluciones de pacientes
	 */
	public static String nombreValoresDefectoCodigoTransDevolucionesPaciente = "codigo_trans_devolucion_paciente";
	/**
	 * Nombre de transacciï¿½n utilizado por los pedidos
	 */
	public static String nombreValoresDefectoCodigoTransCompras = "codigo_transaccion_compras";
	
	/**
	 * Nombre de transacciï¿½n utilizado por los pedidos
	 */
	public static String nombreValoresDefectoCodigoTransDevolucionCompras = "codigo_trans_devolucion_compras";
	
	/**
	 * Nombre de transacciï¿½n utilizado por los pedidos
	 */
	public static String nombreValoresDefectoCodigoTransPedidos = "codigo_transaccion_pedidos";
	/**
	 * Nombre de transacciï¿½n utilizado para las devoluciones de pedidos
	 */
	public static String  nombreValoresDefectoCodigoTransDevolucionesPedidos = "codigo_trans_devolucion_pedidos";
	/**
	 * Nombre para permitir modificar la fecha de la elaboraciï¿½n de inventarios
	 */
	public static String nombreValoresDefectoModificarFechaaInventarios = "modificar_fecha_inventario";
	/**
	 * Nombre para el porcentaje de alerta de punto de pedido
	 */
	public static String nombreValoresDefectoPorcentajePuntoPedido = "porcentaje_punto_pedido";
	
	/**
	 * Nombre para manejar el valor de dias previos notificacion proxima cita de control 
	 */
	public static String nombreValoresDefectoDiasPreviosNotificacionProximoControl = "dias_previos_notificacion_proximo_control";
	/**
	 * Nombre transacciï¿½n utilizado para traslado almacenes
	 */
	public static String nombreValoresDefectoCodigoTransTrasladoAlmacenes = "codigo_trans_traslado_almacen";
	/**
	 * Nombre para manejar ele chequeo automï¿½tico del campo "Informaciï¿½n Adicional" en el ingreso de convenios
	 */
	public static String nombreValoresDefectoInfoAdicIngresoConvenios = "info_adicional_ingreso_convenios";
	/**
	 * Nombre para manejar valor de la ocupaciï¿½n mï¿½dico especialista
	 */
	public static String nombreValoresDefectoCodigoOcupacionMedicoEspecialista = "codigo_ocupacion_medico_especialista";
	/**
	 * Nombre para manejar valor de la ocupaciï¿½n mï¿½dico general
	 */
	public static String nombreValoresDefectoCodigoOcupacionMedicoGeneral = "codigo_ocupacion_medico_general";
	/**
	 * Nombre para manejar valor de la ocupaciï¿½n enfermera
	 */
	public static String nombreValoresDefectoCodigoOcupacionEnfermera = "codigo_ocupacion_enfermera";
	/**
	 * Nombre para manejar valor ocupaciï¿½n auxiliar enfermerï¿½a
	 */
	public static String nombreValoresDefectoCodigoOcupacionAuxiliarEnfermeria = "codigo_ocupacion_auxiliar_enfermeria";
	/**
	 * Nombre para manejar el valor de dias previos notificacion proxima cita de control 
	 */
	public static String valorValoresDefectoDiasPreviosNotificacionProximoControl="@@";

	/**
	 * Nombre para manejar el C&oacute;digo de la hora del primer turno de enfermerï¿½a
	 */
	public static String nombreValoresDefectoHoraInicioPrimerTurno="hora_primer_turno";

	/**
	 * Nombre para manejar el C&oacute;digo de la hora final del ï¿½ltimo turno de enfermerï¿½a
	 */
	public static String nombreValoresDefectoHoraFinUltimoTurno="hora_ultimo_turno";
	
	/**
	 * Nombre para manejar los minutos para caducar una consulta  
	 */
	public static String nombreValoresDefectoMinutosEsperaCitaCaduca = "minutos_espera_cita_caduca";
	
	/**
	 * Nombre para manejar los minutos para el tiempo mï¿½ximo de grabaciï¿½n de registros  
	 */
	public static String nombreValoresDefectoTiempoMaximoGrabacion = "tiempo_maximo_grabacion";
	
	/**
	 * Nombre para manejar el ingreso de la cantidad en sol medicamentos  
	 */
	public static String nombreValoresDefectoIngresoCantidadSolMedicamentos="ingreso_cantidad_sol_m";
	
	/**
	 * Nombre para manejar el tipo de consecutivo capitacion  
	 */
	public static String nombreValoresDefectoTipoConsecutivoCapitacion = "tipo_consecutivo_capitacion";
	
	/**
	 * Nombre del parï¿½metro para modificar los minutos espera ceuntas proceso fact
	 */
	public static String nombreValoresDefectoModificarMinutosEsperaCuentasProcFact="minutos_espera_cuentas_proc_fact";
	
	/**
	 * Nombre para manejar el valor por defecto de nï¿½mero dï¿½as tratamiento solicitudes medicamentos
	 */
	public static String nombreValoresDefectoNumDiasTratamientoMedicamentos = "numero_dias_tratamiento_medicamentos";
	
	/**
	 * Nombre para manejar el valor por defecto de nï¿½mero dï¿½as generar ordenes ambulatorias articulos
	 */
	public static String nombreValoresDefectoNumDiasGenerarOrdenesArticulos = "numero_dias_generar_orden_articulo";
	
	/**
	 * Nombre para manejar el valor por defecto de nï¿½mero dï¿½as egreso ordenes ambulatorias
	 */
	public static String nombreValoresDefectoNumDiasEgresoOrdenesAmbulatorias = "numero_dias_egreso_orden_articulo";
	
	/**
	 * Nombre para manejar el valor por  defecto que indica si tiene interfaz paciente
	 */
	public static String nombreValoresDefectoInterfazPaciente = "interfaz_pacientes";
	
	/**
	 * Nombre para manejar el valor por  defecto que indica si tiene interfaz abono tesoreria 
	 */
	public static String nombreValoresDefectoInterfazAbonosTesoreria = "interfaz_abonos_tesoreria";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoInterfazCompras= "interfaz_compras";
	
	/**
	 * 
	 */
	public static String nombreMinutosCaducaCitaReservada = "min_caduca_citas_reservadas";
	
	/**
	 * 
	 */
	public static String nombreMinutosCaducaCitaAsignadasReprogramadas = "min_caduca_citas_asignadas_reprog";
	
	/**
	 * 
	 */
	public static String nombreEjecutarProcAutoActualizacionCitasOdo = "ejecutar_proc_auto_actualizacion_citas_odo";
	
	/**
	 * 
	 */
	public static String nombreHoraEjecutarProcAutoActualizacionCitasOdo = "hora_ejecutar_proc_auto_actualizacion_citas_odo";
	
	/**
	 * 
	 */
	public static String nombreMinutosEsperaAsignarCitasOdoCaducadas = "minutos_espera_asignar_citas_odo_caducadas";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoArticuloInventario = "articulo_inventario";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoLoginUsuario= "login_usuario";
	
	/**
	 * Nombre para manejar el valor por defecto de convenio fisalud
	 */
	public static String nombreValoresDefectoConvenioFisalud = "convenio_fisalud";
	
	/**
	 * Nombre para manejar el valor por defecto de convenio fisalud
	 */
	public static String nombreValoresDefectoFormaPagoEfectivo = "forma_pago_efectivo";
	
	/**
	 * nombre para manejar el valor por defecto de confirmar ajustes pooles
	 */
	public static String nombreValoresDefectoConfirmarAjustesPooles="confirmar_ajustes_pooles";
	
	/**
	 * nombre para manejar el valor por defecto de Horas de Caducidad de referencias externas
	 */
	public static String nombreValoresDefectoHorasCaducidadReferenciasExternas="horas_caducidad_referencias_externas";
	
	/**
	 * nombre para manejar el valor por defecto de llamado automatico referencia
	 */
	public static String nombreValoresDefectoLlamadoAutomaticoReferencia="llamado_automatico_referencia";
	
	//Anexo 992
	
	/**
	 * nombre para el valor por defecto de imprimir firmas impresion cc capitacion
	 */
	public static String nombreValoresDefectoImprimirFirmasImpresionCCCapitacion="imprimir_firmas_impresion_cc_capitacion";
	
	/**
	 * nombre para el valor por defecto del encabezado formato Impresion factura o cc capitacion
	 */
	public static String nombreValoresDefectoEncabezadoFormatoImpresionFacturaOCCCapitacion="encabezado_formato_impresion_facturaocc_capitacion";


	/**
	 * nombre para el valor por defecto de pie de pagina del formato de impresion defactura o cc capitacion
	 */
	public static String nombreValoresDefectoPiePaginaFormatoImpresionFacturaOCCCapitacion="pie_pagina_formato_impresion_facturaocc_capitacion";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoImprimirFirmasEnImpresionCC="imprimir_firmas_impresion_cc";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoNumeroMesesAMostrarEnReportesPresupuestoCapitacion="numero_meses_a_mostrar_reportes_de_presupuesto_capitacion";
	
	//Fin anexo 992
	
	//Anexo 958
	/**
	 * 
	 */
	public static String nombreValoresDefectoReciboCajaAutomaticoGeneracionFacturaVaria="recibo_caja_automatico_generacion_factura_varia";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoConceptoIngresoFacturasVarias="concepto_ingreso_facturas_varias";
	
	//Fin Anexo 958
	
	
	//Anexo 888
	/**
	 * 
	 */
	public static String nombreValoresDefectoEsRequeridoProgramarCitaAlContratarPresupuestoOdon="es_requerido_programar_cita_al_contratar_presupuesto_odon";

	/**
	 * 
	 */
	public static String nombreValoresDefectoMotivoAnulacionSolicitudAutorizacionDescuentoPresupeustoOdontologico="motivo_anulacion_solicitud_autorizacion_descuento_presupuesto_odon";
	
	//Fin anexo 888
	
	//Anexo 888 Pt II
	/**
	 * 
	 */
	public static String nombreValoresDefectoRequeridoProgramarCitaControlAlTerminarPresupuestoOdon="requerido_programar_cita_control_al_terminar_presupuesto_odon";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoRequeridoProgramarCitaControlAlTerminarPresupuestoOdonServicio="requerido_programar_cita_control_al_terminar_presupuesto_odon_servicio";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoTiempoDiasParaAgendarCitaControlAlTerminarPresupuestoOdon="tiempo_dias_para_agendar_cita_control_al_terminar_presupuesto_odon";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoInstitucionManejaFacturacionAutomatica="institucion_maneja_facturacion_automatica";
	
	//Fin Anexo 888 Pt II
	
	
	//Anexo 959
	/**
	 * 
	 */
	public static String nombreValoresDefectoManejaConsecutivoFacturaPorCentroAtencion="maneja_consecutivo_factura_por_centro_atencion";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoManejaConsecutivosTesoreriaPorCentroAtencion="maneja_consecutivos_tesoreria_por_centro_atencion";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoTamanioImpresionRC="tamanio_impresion_rc";
	
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoActivarBotonGenerarSolicitudOrdenAmbulatora="activar_bonot_generar_solicitudes_orden_ambulatoria";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoEsRequeridoTestigoSolicitudAceptacionTrasladoCaja="requerido_testigo_solicitud_acetpacion_traslado_caja";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoInstitucionManejaCajaPrincipal="institucion_maneja_caja_principal";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoInstitucionManejaTrasladoOtraCajaRecaudo="institucion_maneja_traslado_otra_caja_recaudo";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoInstitucionManejaEntregaATransportadoraValores="institucion_maneja_entrega_transportado_valores";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoTrasladoAbonosPacienteSoloPacientesConPresupuestoContratado="traslado_abonos_paciente_solo_pacientes_presupuesto_contratado";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoControlarAbonoPacientePorNroIngreso="controlar_abono_paciente_por_numero_ingreso";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoValidaEstadoContratoNominaALosProfesionalesSalud="valida_estado_contrato_nomina_a_profesionales";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoManejaInterfazUsuariosSistemaERP="maneja_interfaz_usuarios_con_sistema_erp";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoRequierGenerarSolicitudCambioServicio="requiere_generar_solicitud_cambio_servicio";
	
	
	public static String nombreValoresDefectoManejaVentaTarjetaClienteOdontosinEmision="maneja_venta_tarjeta_cliente_odonto_sin_emision";
	
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoLasCitasDeControlSePermitenAsignarA="citas_odontologicas_contro_se_permiten_asignar_a";
	
	/**
	 * nombre del parametro para definir los Meses Maximo para Administrar Autorizaciones de Capitacion vencidas
	 */
	public static String nombreValoresDefectoMesesMaxAdminAutoCapVencidas = "meses_max_admin_auto_cap_vencidas";
	/**
	 * nombre del parametro para definir los días maximos de prorroga de una autorizacion de Articulo
	 */
	public static String nombreValoresDefectoDiasMaxProrrogaAutorizacionArticulo = "dias_max_prorroga_auto_articulo";
	
	/**
	 * nombre del parametro para definir los días maximos de prorroga de una autorizacion de Servicios
	 */
	public static String nombreValoresDefectoDiasMaxProrrogaAutorizacionServicio = "dias_max_prorroga_auto_servicio";
	
	//Fin Anexo 959
	
	/**
	 * Nombre para manejar tope consecutivo
	 */
	public static String valorValoresDefectoRipsPorFactura="@@";

	/**
	 * Nombre para manejar fecha corte saldo inicial cartera 
	 */
	public static String valorValoresDefectoFechaCorteSaldoInicialC="@@";

	/**
	 * Nombre para manejar fecha corte saldo inicial cartera capitacion
	 */
	public static String valorValoresDefectoFechaCorteSaldoInicialCCapitacion="@@";
	
	/**
	 * Nombre para manejar fecha corte saldo inicial cartera 
	 */
	public static String valorValoresDefectoTopeConsecutivoCxCSaldoI="@@";
	
	/**
	 * Nombre para manejar fecha corte saldo inicial cartera capitacion
	 */
	public static String valorValoresDefectoTopeConsecutivoCxCSaldoICapitacion="@@";
	
	/**
	 * Nombre para manejar Dato de la cuenta requerido en la reserva de cita 
	 */
	public static String valorValoresDefectoDatosCuentaRequeridoReservaCitas= ConstantesBD.acronimoNo + "@@"+ ConstantesBD.acronimoNo ;
	
	/**
	 * Nombre para manejar Dato de red no adscrita 
	 */
	public static String valorValoresDefectoRedNoAdscrita="@@";
	/**
	 * Nombre para manejar el MaxPageItems
	 */
	public static String valorValoresDefectoMaxPageItems="20@@20";
	
	/**
	 * Nombre para manejar el MaxPageItems
	 */
	public static String valorValoresDefectoMaxPageItemsEpicrisis="2@@2";
	
	/**
	 * Nombre para manejar el path archivos planos facturacion
	 */
	public static String valorValoresDefectoPathArchivosPlanosFacturacion="@@";
	
	/**
	 * Nombre para manejar el path archivos planos manejo del paciente
	 */
	public static String valorValoresDefectoPathArchivosPlanosManejoPaciente="@@";
	
	/**
	 * Nombre para manejar la generaciï¿½n de farmacia automatico
	 */
	public static String valorValoresDefectoGenExcepcionesFarmAutomatico="@@";
	
	/**
	 * Valor para manejar los valores por defecto del ingreso de la
	 * cantidad de artï¿½culos en farmacia (Valor por defecto)
	 */
	public static String valorValoresDefectoIngresoCantidadFarmacia="true@@true";
	
	/**
	 * Valor para concocer si se Requiere de una glosa en estador espondida para inactivar una factura 
	 */
	public static String valorValoresDefectoRequiereGlosaInactivar ="false@@false";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoDiasAlertaVigencia="@@";
	
	/**
	 * Nombre para manejar los valores por defecto del ingreso de rips
	 * en la valoraciï¿½n de consulta externa (Valor por defecto)
	 */
	public static String valorValoresDefectoExcepcionRipsConsultorios="false@@false";
	
	/**
	 * Nombre para manejar el C&oacute;digo de la especialidad anestesionlogia
	 */
	public static String nombreValoresDefectoEspecialidadAnestesiologia="especialidad_anestesiologia";
	
	/**
	 * Nombre para manejar el valor por defecto de validar edad responsable paciente 
	 */
	public static String nombreValoresDefectoValidarEdadResponsablePaciente="validar_edad_responsable_paciente";

	/**
	 * Nombre para manejar el valor por defecto de validar edad deudor paciente
	 */
	public static String nombreValoresDefectoValidarEdadDeudorPaciente="validar_edad_deudor_paciente";
		
	/**
	 * Nombre para manejar aï¿½os base de la edad adulta
	 */
	public static String nombreValoresDefectoAniosBaseEdadAdulta="anios_base_edad_adulta";
	
	/**
	 * Nombre para manejar validad egreso administrativo para paquetizar
	 */
	public static String nombreValoresDefectoValidarEgresoAdministrativoPaquetizar="validar_egreso_administrativo_paquetizar";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoMostrarAntecedentesParametrizadosEpicrisis="mostrar_antecedentes_epicrisis";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoPermitirConsultarEpicrisisSoloProfesionales="permitir_consulta_epicrisis_solo_prof";
	
	
	/**
	 * Nombre para manejar la maxima cantidad de paquetes validos por ingreso paciente
	 */
	public static String nombreValoresDefectoMaxCantidPaquetesValidosIngresoPaciente="max_cantid_paquetes_validos_ingreso_paciente";
	
	/**
	 * Nombre para manejar lo referente a  asignar valor paciente con el valor de los cargos
	 */
	public static String nombreValoresDefectoAsignarValorPacienteValorCargos="asignar_valor_paciente_valor_cargos";
	
	/**
	 * Nombre para manejar lo referente a Interfaz Cartera Pacientes
	 */
	public static String nombreValoresDefectoInterfazCarteraPacientes="interfaz_cartera_pacientes";
	
	/**
	 * Nombre para manejar lo referente a Interfaz Contable Facturas
	 */
	public static String nombreValoresDefectoInterfazContableFacturas="interfaz_contable_facturas";
	
	/**
	 * Nombre para manejar lo referente a Interfaz Terceros
	 */
	public static String nombreValoresDefectoInterfazTerceros="interfaz_terceros";
	
	/**
	 * Nombre para manejar lo referente a crear cuenta en atencion de citas
	 */
	public static String nombreCrearCuentaAtencionCitas="crear_cuenta_atencion_citas";
	
	/**
	 * Nombre para manejar lo referente a  si la institucion maneja multas por incumplimiento de citas
	 */
	public static String nombreInstitucionManejaMultasPorIncumplimiento="institucion_maneja_multas_por_incumplimiento";
	
	/**
	 * Nombre para manejar el valor por defecto de Bloquea Citas Reserva Asign Reprog Por Incump
	 */
	public static String nombreBloqueaCitasReservaAsignReprogPorIncump = "bloquea_citas_reserva_asign_reprog_por_incump";
	
	/**
	 * Nombre para manejar el valor por defecto de Bloquea Atencion Citas Por Incump
	 */
	public static String nombreBloqueaAtencionCitasPorIncump="bloquea_atencion_citas_por_incump";
	
	/**
	 * Nombre para manejar el valor por defecto de Fecha Inicio Control Multas Incumplimiento citas
	 */
	public static String nombreFechaInicioControlMultasIncumplimientoCitas="fecha_inicio_control_multas_incumplimiento_citas";
	
	/**
	 * Nombre para manejar el valor por defecto de Valor Multa Por Incumplimiento Citas
	 */
	
	public static String nombreValorMultaPorIncumplimientoCitas="valor_multa_por_incumplimiento_citas";
	
	/**
	 * Nombre para manejar el valor por defecto del Numero maximo de dias para generear solicitudes de ordenes ambulatorias de servicios
	 */
	public static String numMaxDiasGenOrdenesAmbServ="num_max_dias_gen_ordenes_amb_serv"; 
	
	/**
	 * Nombre para manejar el valor por defecto del Múltiplo en minutos para generación de citas
	 */
	public static String multiploMinGeneracionCita="multiplo_min_generacion_cita"; 
	
	/**
	 * Nombre para manejar el valor por defecto del Número de días anteriores a la fecha actual para mostrar agenda odontológica
	 */
	public static String numDiasAntFActualAgendaOd="num_dias_ant_f_actual_agenda_odo"; 
	
	/**
	 * Nombre para el manejo del Formatio de Impresión de Conciliación de Glosas
	 */
	public static String nombreFormatoImpresionConciliacion="formato_imp_conciliacion";
	
	/**
	 * Nombre para el manejo de Glosas Reiteradas
	 */
	public static String nombreValidarGlosaReiterada="validar_glosa_reiterada";
	
	
	/**
	 * Nombre para manejar lo referente a Consolidar Cargos
	 */
	public static String nombreValoresDefectoConsolidarCargos="consolidar_cargos";
	
	/**
	 * Nombre 
	 */
	public static String nombreValoresDefectoManejaConversionMonedaExtranjera="manejo_conversion_moneda_extranjera";
	
	/**
	 * Nombre para manejar la hora de corte del historico de camas
	 */
	public static String nombreValoresDefectoHoraCorteHistoricoCamas="hora_corte_historico_camas";
	
	/**
	 * Nombre para manejar los minutos limite de alerta para la reserva.
	 */
	public static String nombreValoresDefectoMinutosLimiteAlertaReserva="minutos_limite_alerta_reserva";
	
	
	/**
	 * Nombre para manejar los minutos limite de alerta para pacientes con salida de urgencias.
	 */
	public static String nombreValoresDefectoMinutosLimiteAlertaPacienteConSalidaUrgencias="min_limit_alert_pac_con_salida_urg";
	
	/**
	 * Nombre para manejar los minutos limite de alerta para pacientes con salida de hospitalizacion.
	 */
	public static String nombreValoresDefectoMinutosLimiteAlertaPacienteConSalidaHospitalizacion="min_limit_alert_pac_con_salida_hospi";
	
	
	/**
	 * Nombre para manejar los minutos limite de alerta para pacientes por remitir uregencias.
	 */
	public static String nombreValoresDefectoMinutosLimiteAlertaPacientePorRemitirUrgencias="min_limit_alert_pac_por_remitir_urg";
	
	
	/**
	 * Nombre para manejar los minutos limite de alerta para pacientes por remitir Hospitalizacion.
	 */
	public static String nombreValoresDefectoMinutosLimiteAlertaPacientePorRemitirHospitalizacion="min_limit_alert_pac_por_remitir_hospi";
	
	/**
	 * Nombre para manejar el identificardo institucion para reportar archivos colsanitas
	 */
	public static String nombreValoresDefectoIdentificadorInstitucionArchivosColsanitas="identificador_inst_archivos_colsanitas";
	
	/**
	 * Nombre de la interfaz rips
	 * */
	public static String nombreValoresDefectoInterfazRips = "interfaz_rips";
	/**
	 * Nombre para sabes si entidad maneja hospital dï¿½a
	 * */
	public static String nombreValoresDefectoEntidadManejaHospitalDia = "entidad_maneja_hospital_dia";
	/**
	 * Nombre para sabes si entidad maneja rips
	 * */
	public static String nombreValoresDefectoEntidadManejaRips = "entidad_maneja_rips";
	
	/**
	 * Nombre para sabes si valoraciï¿½n de urgencias en hospitalizacion
	 * */
	public static String nombreValoresDefectoValoracionUrgenciasEnHospitalizacion = "valoracion_urgencias_en_hospitalizacion";
	
	/**
	 * Nombre para manejar el valor por defecto
	 */
	public static String nombreValoresDefectoCodigoManualEstandarBusquedaServicios = "codigo_manual_busqueda_servicios";
	
	/**
	 * Nombre para sabes la ubicacion de los planos de las entidades subcontratadas
	 * */
	public static String nombreValoresDefectoUbicacionPlanosEntidadesSubcontratadas = "ubicacion_planos_entidades_subcontratadas";
	
	/**
	 * Nombre para manejar el valor por defecto
	 */
	public static String nombreValoresDefectoInstitucionMultiempresa="institucion_multiempresa";
	
	/**
	 * Nombre para manejar el valor por defecto conteos validos ajustar inventario fisico
	 */
	public static String nombreValoresDefectoConteosValidosAjustarInvFisico="conteos_val_ajustar_inv_fisico";

	/**
	 * Valor 
	 */
	public static String valorValoresDefectoInstitucionMultiempresa= ConstantesBD.acronimoNo + "@@"+ ConstantesBD.acronimoNo;
	
	
	/**
	 * Nombre para manejar el valor por defecto
	 */
	public static String nombreValoresDefectoEntidadControlaDespachoSaldosMultidosis="entidad_controla_despacho_saldos_multidosis";
	
	 
	/**
	 * Nombre para manejar el valor por defecto
	 */
	public static String nombreValoresDefectoNumeroDiasControMedOrdenados="numero_dias_control_medicamentos_ordenados";
	
	/**
	 * Nombre Parï¿½metro General Produccion En Paralelo Con Sistema Anterior
	 */
	public static String nombreValoresDefectoProduccionEnParaleloConSistemaAnterior = "produccion_en_paralelo_con_sistema_anterior";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoInterfazContableRecibosCajaERP="interfaz_contable_recibos_caja_erp";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoValidarAdministracionMedicamentosEgresoMedico="validar_administracion_medicamentos_egreso_medico";
	



	/**
	 * Nombre para manejar el valor por defecto
	 */
	public static String nombreValoresDefectoGenerarEstanciaAutomatica="generar_estancia_automatica";

	/**
	 * Nombre para manejar el valor por defecto
	 */
	public static String nombreValoresDefectoHoraGenerarEstanciaAutomatica="hora_generar_estancia_automatica";

	/**
	 * Nombre para manejar el valor por defecto
	 */
	public static String nombreValoresDefectoIncluirTipoPacienteCirugiaAmbulatoria="incluir_tipo_paciente_cirugia_ambulatoria";
	
	/**
	 * Nombre para vlor por defecto Consecutivo facturas Varias
	 */
	public static String nombreValoresDefectoTipoConsecutivoManejar="tipo_consecutivo_manejar_facturas_varias";

	
	/**
	 * Nombre para vlor por defecto Consecutivo Ajustes facturas Varias 
	 */
	public static String nombreValoresDefectoTipoConsecutivoManejarAjustes="tipo_consecutivo_manejar_facturas_varias_ajuste";
	
	/**
	 * Nombre para Hacer Requerida Informaciï¿½n RIPS en Cargos Drectos
	 * */
	public static String nombreValoresDefectoRequeridaInfoRipsCargosDirectos = "requerida_info_rips_cargos_directos";
		
	/**
	 * Nombre para manejar el valor por defecto de asigna valoracion de cirugia ambulatoria a Hospitalizado
	 * */
	public static String nombreValoresDefectoAsignaValoracionCxAmbulatoriaHospitalizado = "asigna_valoracion_cirugia_ambu_ahospi";
	
	/**
	 * Nombre para Manejar el Valor por defecto de Facturas de Cuentas Varias 
	 */
	public static String nombreValoresDefectoManejoInterfazConsecutivoFacturasOtroSistema = "manejo_interfaz_consecutivo_facturas_otro_sistema";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoInterfazNutricion = "interfaz_nutricion";
	
	/**
	 * Nombre para el parametro concepto para ajuste de entrada
	 */
	public static String nombreValoresDefectoConceptoParaAjusteEntrada = "concepto_ajuste_entrada";
	
	/**
	 * Nombre para el parametro concepto para ajuste de salida
	 */
	public static String nombreValoresDefectoConceptoParaAjusteSalida = "concepto_ajuste_salida";
	
	/**
	 * Nombre para el parametro concepto para ajuste de entrada
	 */
	public static String nombreValoresDefectoPermitirModificarConceptosAjuste = "permitir_modificar_conceptos_ajuste";
	
	/**
	 * Nombre para el parametro Tiempo maximo para el reingreso de urgencias
	 * */
	public static String nombreTiempoMaximoReingresoUrgencias = "tiempo_maximo_reingreso_urgencias";
	
	/**
	 * Nombre para el parametro Tiempo maximo para el reingreso de hospitalizaciï¿½n
	 * */
	public static String nombreTiempoMaximoReingresoHospitalizacion = "tiempo_maximo_reingreso_hospitalizacion";
	
	/**
	 * Nombre para el parametro Permitir Facturar Reingresos Independientes
	 * */
	public static String nombrePermitirFacturarReingresosIndependientes = "permitir_facturar_reingresos_independientes";
	
	/**
	 * Liberar cama de hospitalizacion despues de facturar
	 * */
	public static String nombreLiberarCamaHospitalizacionDespuesFacturar= "liberar_cama_hospitalizacion_despues_facturar";
	
	/**
	 * Controla Interpretacion Procedimientos para Permitir Evolucion
	 */
	public static String nombreControlaInterpretacionProcedimientosParaEvolucionar = "controla_procedimientos_para_evolucionar";
	
	/**
	 * Validar Registro Evoluciones para Generar Ordenes
	 */
	public static String nombreValidarRegistroEvolucionParaGenerarOrdenes = "validar_registro_evolucion_generar_ordenes";
	
	/**
	 * Comprobaciï¿½n de Derechos Capitacion Obligatoria
	 * */
	public static String nombreComprobacionDerechosCapitacionObligatoria = "comprobacion_derechos_capitacion_obligatoria";
	
	/**
	 * Requiere Autorizacion para Anular Facturas
	 */
	public static String nombreRequiereAutorizarAnularFacturas = "requiere_autorizar_anular_facturas";
	
	/**
	 * Validar Pooles factura
	 */
	public static String nombreValidarPoolesFact = "valida_pooles_fact";
	
	/**
	 * Mostrar Enviar Epicrisis Evol
	 */
	public static String nombreMostrarEnviarEpicrisisEvol = "mostrar_enviar_epicrisis_evol";
	
	/**
	 * Dï¿½as restricciï¿½n citas incumplidas
	 */
	public static String nombreValoresDefectoDiasRestriccionCitasIncumplidas = "dias_restriccion_citas_incumplidas";
	
	/**
	 * Permitir Modificar la fecha de solicitud pedidos insumos
	 */
	public static String nombreValoresDefectoPermitirModificarFechaSolicitudPedidos = "permitir_modificar_fecha_solicitud_pedidos";
	
	/**
	 * Permitir Modificar la fecha de solicitud pedidos insumos
	 */
	public static String nombreValoresDefectoPermitirModificarFechaSolicitudTraslado = "permitir_modificar_fecha_solicitud_Traslado";
	
	/**
	 * Nombre para manejar el valor por defecto
	 */
	public static String nombreValoresDefectoCodigoManualEstandarBusquedaArticulos = "codigo_manual_busqueda_articulos";
	
	/**
	 * Impresion media carta 
	 */
	public static String nombreValoresDefectoImpresionMediaCarta="impresion_media_carta";
	
	/**
	 * Parï¿½metro para hcer requerido los comentarios al solicitar
	 */
	public static String nombreValoresDefectoRequeridoComentariosSolicitar="requiere_comentarios_solicitar";
	
	/**
	 * Nombre del Parï¿½metro General Nï¿½mero de Dï¿½as para Responder Glosas
	 */
	public static String nombreValoresDefectoNumeroDiasResponderGlosas = "numero_dias_responder_glosas";

	
	/** * Nombre del Parï¿½metro General Generar Ajuste Automatico desde el Registro de la Respuesta */
	public static String nombreValoresDefectoGenerarAjusteAutoRegRespuesta = "generar_ajuste_automatico_registro_respuesta";

	/** * Nombre del Parï¿½metro General Generar Ajuste Automatico desde el Registro de la Respuesta para Respuestas de Conciliacion */
	public static String nombreValoresDefectoGenerarAjusteAutoRegRespuesConciliacion = "gen_ajuste_auto_reg_resp_respuestas_conciliacion";

	/** * Nombre del Parï¿½metro General Formato Impresion Respuesta de Glosa */
	public static String nombreValoresDefectoFormatoImpresionRespuesGlosa = "formato_impresion_respuesta_glosa";

	/** * Nombre del Parï¿½metro General Imprimir Firmas en Impresion Respuesta de Glosa */
	public static String nombreValoresDefectoImprimirFirmasImpresionRespuesGlosa = "imprimir_firmas_impresion_respuesta_glosa";
	
	/**
	 * Nombre Parï¿½metro General Validar Auditor
	 */
	public static String nombreValoresDefectoValidarAuditor = "validar_auditor";
	
	/**
	 * Nombre Parï¿½metro General Validar Usuario Glosa
	 */
	public static String nombreValoresDefectoValidarUsuarioGlosa = "validar_usuario_glosa";
	
	/**
	 * Nombre Parï¿½metro General Nï¿½mero de Glosas Registradas Por Factura
	 */
	public static String nombreValoresDefectoNumeroGlosasRegistradasXFactura = "numero_glosas_registradas_por_factura";
	
	/**
	 * Nombre Parï¿½metro General Nï¿½mero de Dï¿½as para Notificar Glosa
	 */
	public static String nombreValoresDefectoNumeroDiasNotificarGlosa = "numero_dias_para_notificar_glosa";
	
	/**
	 * Postular fechas en rspuestas DyT
	 */
	public static String nombreValoresDefectoPostularFechasEnRespuestasDyT = "postular_fechas_en_respuestas_dyt";
	
	/**
	 * Nombre para manejar los valores por defecto 
	 */
	public static String nombreValoresDefectoClasesInventariosPaqMatQx = "clases_inventarios_paquetes_materiales_qx";
	
	/**
	 * Nombre para manejar los valores por defecto  de numero de dias busqueda reportes
	 */
	public static String nombreValoresDefectoNumeroDiasBusquedaReportes= "numero_dias_busqueda_reportes";
	

	/**
	 * Nombre para manejar los valores por defecto  de Nï¿½mero de Dï¿½gitos Captura en Nï¿½mero de Identificaciï¿½n Pacientes
	 * Modulo Administracion //70006
	 */
	public static String nombreValoresDefectoNumDigitosCaptNumIdPac = "num_digitos_captura_num_id_paciente";
	
	/**
	 * Nombre para manejar los valores por defecto  de indicador permiri interpretar ordenes de respuesta multiple
	 * sin finalizar
	 */
	public static String nombreValoresDefectoPermIntOrdRespMulSinFin = "pem_int_ord_resp_mul_sin_fin";
	
	/**
	 * Nombre para concocer si se Requiere de una glosa en estador espondida para inactivar una factura 
	 */
	public static String nombreValoresDefectoRequiereGlosaInactivar = "requiere_glosa_para_inactivar";
	
	/**
	 *  
	 */
	public static String nombreValoresDefectoAprobarGlosaRegistro = "aprobar_glosa_desde_registro";
	

	
	/**
	 * Nombre para manejar los valores por defecto  de Tiempo en segundos que indica cada cuanto verificar interfaz Shaio por procesar 
	 */
	public static String nombreValoresDefectoTiemSegVeriInterShaioProc= "tiem_seg_veri_inter_shaio_proc";
	
	/**
	 * Nombre para el Manejo Especial de Instituciones Odontologia
	 */
	public static String nombreManejoEspecialInstitucionesOdontologia="man_esp_ins_odon";
	
	/**
	 * Nombre para el Mï¿½ximo nï¿½mero de cuotas de financiaciï¿½n
	 */
	public static String nombreMaximoNumeroCuotasFinanciacion="max_num_cuotas_financiacion";
	
	/**
	 * Nombre para el Mï¿½ximo nï¿½mero de dï¿½as de financiaciï¿½n por cuota
	 */
	public static String nombreMaximoNumeroDiasFinanciacionPorCuota="max_num_dias_financiacion";
	
	/**
	 * Nombre para el Formato documentos de garantï¿½a - pagarï¿½
	 */
	public static String nombreFormatoDocumentosGarantia_Pagare="formato_doc_garantia";
	
	/**
	 * Nombre para la ocupacion odontï¿½logo
	 */
	public static String nombreOcupacionOdontologo="ocupacion_odontologico";
	
	/**
	 * Nombre para la ocupacion Auxiliar de odontï¿½logo
	 */
	public static String nombreOcupacionAuxiliarOdontologo="ocupacion_aux_odontologia";
	
	/**
	 * Nombre para la edad final niï¿½ez
	 */
	public static String nombreEdadFinalNinez="edad_final_ninez";
	
	/**
	 * Nombre para la edad inicio adulto
	 */
	public static String nombreEdadInicioAdulto="edad_inicio_adulto";
	
	/**
	 * Nombre para determinar si se obliga a dar incapacidad a los pacientes hospitalizados
	 */
	public static String nombreObligarRegIncapaPacienteHospitalizado="obligar_reg_incapa_paciente_hospitalizado";
	
	/**
	 * Valor para manejar los valores por defecto 
	 */
	public static String valorValoresDefectoClasesInventariosPaqMatQx = ""+ConstantesBD.codigoNuncaValido+"@@Seleccione";
	
	/**
	 * Nombre para el parámetro geneal escala pefil paciente
	 */
	public static String nombreValoresDefectoEscalaPerfilPaciente="escala_paciente_perfil";
	
	/**
	 * Nombre para el parámetro general de utilizan Programas Odontologicos en la institucion
	 */
	public static String nombreValoresDefectoUtilizanProgramasOdontologicosEnInstitucion="utiliza_programas_odo_inst";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoTiempoVigenciaPresupuestoOdo = "tiempo_vigencia_presupuesto_odo";

	/**
	 * 
	 */
	public static String nombreValoresDefectoPermiteCambiarServiciosCitaAtencionOdo = "permite_cambiar_servicios_cita_atencion_odo";

	/**
	 * 
	 */
	public static String nombreValoresDefectoValidaPresupuestoOdoContratado = "valida_presupuesto_odo_contratado";

	/**
	 * 
	 */
	public static String nombreValoresDefectoConveniosAMostrarXDefectoPresupuestoOdo = "convenios_a_mostrar_x_defecto_presupuesto_odo";

	/**
	 * 
	 */
	public static String nombreValoresDefectoTiempoMaxEsperaInactivarPresupuestoOdoSuspTemp = "tiempo_max_espera_inact_presupu_odo_susp_temp";

	/**
	 * 
	 */
	public static String nombreValoresDefectoEjecutarProcesoAutoActualizacionEstadosOdo = "ejecutar_proc_auto_actua_estados_odo";

	/**
	 * 
	 */
	public static String nombreValoresDefectoHoraEjecutarProcesoAutoActualizacionEstadosOdo = "hora_ejecutar_proc_auto_actua_estados_odo";

	/**
	 * 
	 */
	public static String nombreValoresDefectoMotivoCancelacionPresupuestoSuspendidoTemp = "motivo_cancelacion_presupuesto_suspendido_temp";

	/**
	 * 
	 */
	public static String nombreValoresDefectoMaximoTiempoSinEvolucionarParaInactivarPlanTratamiento = "max_tiempo_sin_evol_para_inact_plan_tratami";

	/**
	 * 
	 */
	public static String nombreValoresDefectoPrioridadParaAplicarPromocionesOdo = "prioridad_para_aplicar_promociones_odo";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoDiasParaDefinirMoraXDeudaPacientes = "dias_definir_mora_x_deuda_pacientes";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoContabilizacionRequeridoProcesoAsocioFacCuentaCapitacion = "contabilizacion_requerido_proceso_asocio_fac_cuenta_capitacion";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoCuentaContablePagare = "cuenta_contable_pagare";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoCuentaContableLetra = "cuenta_contable_letra";
	
	public static String nombreValoresDefectoInstitucionRegistraAtencionExterna = "institucion_registra_atencion_externa";
	
	/**
	 * Nombre para manejar valor Mostrar gr&aacute;fica de &Iacute;ndice de placa para calcular el porcentaje de &iaacute;ndice de placa 
	 */
	public static String nombreValoresDefectoMostrarGraficaCalculoIndicePlaca = "mostrar_grafica_calculo_indice_placa";
	
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoValidarPacienteParaVentaTarjeta="validar_paciente_venta_tarjeta";
	
	
	/**
	 * valor por defecto de: al cancelar citas odontologicas dejarlas automaticamente en estado A reprogramar
	 */
	public static String nombreValoresPorDefectoAlCancelarCitasOdontoDejarAutoEstadoReprogramar="cancelar_cita_automa_estado_reprogramar";


	
	/**
	 * 
	 */
	public static String nombreValoresDefectoReciboCajaAutomaticoVentaTarjeta="recibo_caja_automatico_venta_tarjeta";

	/**
	 * 
	 */
	public static String nombreValoresDefectoPermitirRegistrarReclamacionCuentasNoFacturadas="permitir_registrar_reclamacion_cuentas_no_facturadas";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoPermitirFacturarReclamarCuentasConRegistroPendientes="permitir_facturar_y_reclmar_cuentas_con_registro_pendiente";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoPermitirOrdenarMedicamentosPacienteUrgenciasConEgreso="permitir_ordenar_medicamentos_paciente_urgencias_con_egreso";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoMostrarAdminMedicamentosArticulosDespachoCero="mostrar_administracion_medicamentos_articulos_despacho_cero";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoNumMaximoReclamacionesAccEventoXFactura="numero_recl_acc_trans_eve_catas_x_facturas";
	
	
	public static String nombreValoresDefectoHacerRequeridoValorAbonoAplicadoAlFacturar="hacer_requerido_valor_abono_aplicado_generacion_factura";



	/************************************************************************************************************************
	 * ------------------------------------------ VALORES DE VALORES POR DEFECTO --------------------------------------------
	 ************************************************************************************************************************/
	
	/**
	 * Valor para manejar los valores por defecto del cierre de la cuenta
	 * en la anulaciï¿½n de la factura (Valor por defecto)
	 */
	public static String valorValoresDefectoGeneral="@@";
	
	
	/**
	 * Valor para manejar los valores por defecto del ajuste de cuenta de cobro radicada (Valor por defecto)
	 */
	public static String valorValoresDefectoAjustarCuentaCobroRadicada="false@@false";
	
	/**
	 * Valor para manejar los valores por defecto del cierre de la cuenta
	 * en la anulaciï¿½n de la factura (Valor por defecto)
	 */
	public static String valorValoresDefectoCerrarCuentaAnulacionFactura="@@";
	
	/**
	 * Valor para manejar los valores por defecto del barrio de residencia
	 */
	public static String valorValoresDefectoBarrioResidencia="@@";
	
	/**
	 * Valor para manejar los valores por defecto de materiales por acto
	 */
	public static String valorValoresDefectoMaterialesPorActo="@@";
	
	/**
	 * Valor para manejar los valores de la Hora Inicial de Programacion de Salas
	 */
	public static String valorValoresDefectoHoraInicialProgramacionSalas = "@@"; 

	/**
	 * Valor para manejar los valores de la Hora Final de Programacion de Salas
	 */
	public static String valorValoresDefectoHoraFinalProgramacionSalas = "@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoLiquidacionAutomaticaCirugias = "@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoLiquidacionAutomaticaNoQx= "@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoManejoProgramacionSalasSolicitudes= "@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoRequridaDescripcionEspecialidadCirugias= "@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoRequridaDescripcionEspecialidadNoCruentos= "@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoAsocioAyudantia= "@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoIndicativoCobrableHonorariosCirugia= "@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoIndicativoCobrableHonorariosNoCruento= "@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoAsocioCirujano= "@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoAsocioAnestesia= "@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoModificarInformacionDescripcionQuirurgicaiDiagnosticos= "@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoMinutosRegistroNotasCirugia= "@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoMinutosRegistroNotasNoCruentos= "@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoModificarInformacionQuirurgica= "@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoMinutosMaximosRegistroAnestesia= "@@";

	/**
	 * Valor para manejar el valor por defecto de la especialidad anestesiologia
	 */
	public static String valorValoresDefectoEspecialidadAnestesiologia = "21@@ANESTESIOLOGIA";
	/**
	 * Valor para manejo de consecutivos en las transacciones de inventarios
	 */
	public static String valorValoresDefectoManejoConsecutivoTransInv = "@@";
	/**
	 * Valor para el porcentaje de alerta en diferencia en los costos de inventarios
	 */
	public static String valorValoresDefectoPorcentajeAlertaCostosInv = "@@";
	/**
	 * Valor de transacciï¿½n utilizado para las solicitudes de pacientes
	 */
	public static String valorValoresDefectoCodigoTransSoliPacientes = "@@";
	/**
	 * Valor de transacciï¿½n utilizado para las devoluciones de pacientes
	 */
	public static String valorValoresDefectoCodigoTransDevolucionesPaciente = "@@";
	/**
	 * Valor de transacciï¿½n utilizado por los pedidos
	 */
	public static String valorValoresDefectoCodigoTransPedidos = "@@";
	/**
	 * Valor de transacciï¿½n utilizado para las devoluciones de pedidos
	 */
	public static String  valorValoresDefectoCodigoTransDevolucionesPedidos = "@@";
	
	/**
	 * Valor de transacciï¿½n utilizado para las devoluciones de pedidos
	 */
	public static String  valorValoresDefectoCodigoTransCompra = "@@";
	
	/**
	 * Valor de transacciï¿½n utilizado para las devoluciones de pedidos
	 */
	public static String  valorValoresDefectoCodigoTransDevolCompra = "@@";
	/**
	 * valor para permitir modificar la fecha de la elaboraciï¿½n de inventarios
	 */
	public static String valorValoresDefectoModificarFechaaInventarios = "@@";
	/**
	 * Valor para el porcentaje de alerta de punto de pedido
	 */
	public static String valorValoresDefectoPorcentajePuntoPedido = "@@";
	/**
	 * Valor transacciï¿½n utilizado para traslado almacenes
	 */
	public static String valorValoresDefectoCodigoTransTrasladoAlmacenes = "@@";
	/**
	 * Valor para el chequeo automï¿½tico del campo "Informaciï¿½n Adicional" en el ingreso de Convenios
	 */
	public static String valorValoresDefectoInfoAdicIngresoConvenios = "@@";
	/**
	 * Valor de la ocupaciï¿½n medico especialista
	 */
	public static String valorValoresDefectoCodigoOcupacionMedicoEspecialista = "@@";
	/**
	 * Valor de la ocupaciï¿½n mï¿½dico general
	 */
	public static String valorValoresDefectoCodigoOcupacionMedicoGeneral = "@@";
	/**
	 * Valor de la ocupaciï¿½n enfermera
	 */
	public static String valorValoresDefectoCodigoOcupacionEnfermera = "@@";
	/**
	 * Valor de la ocupaciï¿½n auxiliar enfermerï¿½a
	 */
	public static String valorValoresDefectoCodigoOcupacionAuxiliarEnfermeria = "@@";

	/**
	 * Valor para manejar los valores de la Hora Inicial de Programacion de Salas
	 */
	public static String valorValoresDefectoHoraInicioPrimerTurno = "@@"; 

	/**
	 * Valor para manejar los valores de la Hora Final de Programacion de Salas
	 */
	public static String valorValoresDefectoHoraFinUltimoTurno = "@@"; 

	/**
	 * 
	 */
	public static String nombreValoresDefectoAreaAperturaCuentaAutoPYP = "area_apertura_cuenta_auto_pyp";
	

	/**
	 * Valor de la ocupaciï¿½n enfermera
	 */
	public static String valorValoresDefectoCodigoMinutosEsperaCitaCaduca = "@@";
	
	/**
	 * Valor para manejar los valores del tiempo mï¿½ximo de grabaciï¿½n de registro
	 */
	public static String valorValoresDefectoCodigoTiempoMaximoGrabacion = "@@";
	
	/**
	 * Valor para manejar el ingreso de la cantidad en la farmacia
	 */
	public static String valorValoresDefectoCodigoIngresoCantidadSolMedicamentos = "@@";
	
	/**
	 * Valor para manejar el tipo de consecutivo de capitaciï¿½n
	 */
	public static String valorValoresDefectoTipoConsecutivoCapitacion = "@@";
	
	/**
	 * Valor para permitir modificar minutos en espera de cuentas en proc fact
	 */
	public static String valorValoresDefectoModificarMinutosEsperaCuentasProcFact = "@@";
	
	/**
	 * Valor para manejar el valor por defecto de nï¿½mero dï¿½as tratamiento solicitudes medicamentos
	 */
	public static String valorValoresDefectoNumDiasTratamientoMedicamentos = "@@";
	
	
	/**
	 * valor para manejar el valor por  defecto que indica si tiene interfaz paciente
	 */
	public static String valorValoresDefectoInterfazPaciente = "@@";
	
	/**
	 * valor para manejar el valor por  defecto que indica si tiene interfaz abono tesoreria 
	 */
	public static String valorValoresDefectoInterfazAbonosTesoreria = "@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoInterfazCompras= "@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoArticuloInventario= "@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoLoginUsuario= "@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoInterfazRips= ConstantesBD.acronimoNo+"@@"+ConstantesBD.acronimoNo;
	
	/**
	 * Valor para entidad maneja hospital dï¿½a
	 */
	public static String valorValoresDefectoEntidadManejaHospitalDia= ConstantesBD.acronimoNo+"@@"+ConstantesBD.acronimoNo;
	
	/**
	 * Valor para entidad maneja Rips
	 */
	public static String valorValoresDefectoEntidadManejaRips= ConstantesBD.acronimoSi+"@@"+ConstantesBD.acronimoSi;
	
	/**
	 * Valor para valoracion urgencias en hospitalizacion
	 */
	public static String valorValoresDefectoValoracionUrgenciasEnHospitalizacion= "@@";
	
	/**
	 * Valor para ubicacion planos entidades subcontratadas
	 */
	public static String valorValoresDefectoUbicacionPlanosEntidadesSubcontratadas= "@@";
	
	/**
	 * Valor para manejar el valor por defecto de nï¿½mero dï¿½as generar ordenes ambulatorias articulos
	 */
	public static String valorValoresDefectoNumDiasGenerarOrdenesArticulos = "@@";
	
	/**
	 * Valor para manejar el valor por defecto de nï¿½mero dï¿½as egreso ordenes ambulatorias
	 */
	public static String valorValoresDefectoNumDiasEgresoOrdenesAmbulatorias = "@@";

	/**
	 * Valor para manejar el valor por defecto de convenio fisalud
	 */
	public static String valorValoresDefectoConvenioFisalud = "@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoConfirmarAjustesPooles="@@";
	
	/**
	 * valor para manejar el valor por defecto de crear cuenta en atencion de citas
	 */
	public static String valorCrearCuentaAtencionCitas="@@";
	
	/**
	 * valor para manejar el valor por defecto de crear cuenta en atencion de citas
	 */
	public static String valorInstitucionManejaMultasPorIncumplimiento="@@";
	
	/**
	 * valor para manejar el valor por defecto de Bloquea Citas Reserva Asign Reprog Por Incump
	 */
	public static String valorBloqueaCitasReservaAsignReprogPorIncump = "@@";
	
	/**
	 * valor para manejar el valor por defecto de Bloquea Atencion Citas Por Incump
	 */
	public static String valorBloqueaAtencionCitasPorIncump="@@";
	
	/**
	 * valor para manejar el valor por defecto de Fecha Inicio Control Multas Incumplimiento citas
	 */
	public static String valorFechaInicioControlMultasIncumplimientoCitas="@@";
	
	/**
	 * valor para manejar el valor por defecto de Valor Multa Por Incumplimiento Citas
	 */
	public static String valorValorMultaPorIncumplimientoCitas="@@";
	
	/**
	 * Valor para manejar el valor por defecto de horas caducidad referencias externas
	 */
	public static String valorValoresDefectoHorasCaducidadReferenciasExternas = "@@";
	
	/**
	 * Valor para manejar el valor por defecto de llamado automatico funcionalidad referencia
	 */
	public static String valorValoresDefectoLlamadoAutomaticoReferencia = "@@";
	
	/**
	 * Valor para manejar validar edad responsable paciente
	 */
	public static String valorValoresDefectoValidarEdadResponsablePaciente= ConstantesBD.acronimoNo + "@@"+ ConstantesBD.acronimoNo ;
	
	/**
	 * Valor para manejar validar edad deudor paciente
	 */
	public static String valorValoresDefectoValidarEdadDeudorPaciente= ConstantesBD.acronimoNo + "@@"+ ConstantesBD.acronimoNo;
	
	/**
	 * Valor para manejar los aï¿½os base de la edad adulta
	 */
	public static String valorValoresDefectoAniosBaseEdadAdulta= "@@";
	
	/**
	 * Valor para manejar validar egreso administrativo para paquetizar
	 */
	public static String valorValoresDefectoValidarEgresoAdministrativoPaquetizar= ConstantesBD.acronimoSi + "@@"+ ConstantesBD.acronimoSi;
	
	/**
	 * Valor para manejar la maxima cantidad de paquetes validos por ingreso paciente
	 */
	public static String valorValoresDefectoMaxCantidPaquetesValidosIngresoPaciente="@@";
	
	/**
	 * Valor para manejar lo referente a Asignar valor paciente con el valor de los cargos
	 */
	public static String valorValoresDefectoAsignarValorPacienteValorCargos= ConstantesBD.acronimoSi + "@@"+ ConstantesBD.acronimoSi;
	
	/**
	 * Valor para manejar lo referente a Interfaz Cartera Pacientes
	 */
	public static String valorValoresDefectoInterfazCarteraPacientes= ConstantesBD.acronimoNo + "@@"+ ConstantesBD.acronimoNo;
	
	/**
	 * Valor para manejar lo referente a Interfaz Contable Facturas
	 */
	public static String valorValoresDefectoInterfazContableFacturas= ConstantesBD.acronimoNo + "@@"+ ConstantesBD.acronimoNo;
	
	/**
	 * Valor para manejar lo referente la Interfaz Terceros
	 */
	public static String valorValoresDefectoInterfazTerceros= ConstantesBD.acronimoNo + "@@"+ ConstantesBD.acronimoNo;
	
	/**
	 * Valor 
	 */
	public static String valorValoresDefectoManejaConversionMonedaExtranjera= ConstantesBD.acronimoNo + "@@"+ ConstantesBD.acronimoNo;
	
	/**
	 * Valor 
	 */
	public static String valorValoresDefectoGeneralNo= ConstantesBD.acronimoNo + "@@"+ ConstantesBD.acronimoNo;
	
	
	/**
	 * 
	 */
	public static String valorValoresDefectoEntidadControlaDespachoSaldosMultidosis= ConstantesBD.acronimoNo + "@@"+ ConstantesBD.acronimoNo;
	
	/**
	 * 
	 */
	public static String valorValoresDefectoActivarBotonGenerarSolicitudOrdenAmbulatora=ConstantesBD.acronimoNo + "@@"+ ConstantesBD.acronimoNo;
	
	/**
	 * 
	 */
	public static String valorValoresDefectoEsRequeridoTestigoSolicitudAceptacionTrasladoCaja=ConstantesBD.acronimoNo + "@@"+ ConstantesBD.acronimoNo;
	
	/**
	 * 
	 */
	public static String valorValoresDefectoInstitucionManejaCajaPrincipal=ConstantesBD.acronimoNo + "@@"+ ConstantesBD.acronimoNo;
	
	/**
	 * 
	 */
	public static String valorValoresDefectoInstitucionManejaTrasladoOtraCajaRecaudo=ConstantesBD.acronimoNo + "@@"+ ConstantesBD.acronimoNo;
	
	/**
	 * 
	 */
	public static String valorValoresDefectoInstitucionManejaEntregaATransportadoraValores=ConstantesBD.acronimoNo + "@@"+ ConstantesBD.acronimoNo;
	
	/**
	 * 
	 */
	public static String valorValoresDefectoTrasladoAbonosPacienteSoloPacientesConPresupuestoContratado=ConstantesBD.acronimoNo + "@@"+ ConstantesBD.acronimoNo;
	
	/**
	 * 
	 */
	public static String valorValoresDefectoControlarAbonoPacientePorNroIngreso=ConstantesBD.acronimoNo + "@@"+ ConstantesBD.acronimoNo;
	
	/**
	 * 
	 */
	public static String valorValoresDefectoValidaEstadoContratoNominaALosProfesionalesSalud=ConstantesBD.acronimoNo + "@@"+ ConstantesBD.acronimoNo;
	
	/**
	 * 
	 */
	public static String valorValoresDefectoManejaInterfazUsuariosSistemaERP=ConstantesBD.acronimoNo + "@@"+ ConstantesBD.acronimoNo;
	
	/**
	 * 
	 */
	public static String valorValoresDefectoRequierGenerarSolicitudCambioServicio=ConstantesBD.acronimoNo + "@@"+ ConstantesBD.acronimoNo;
	
	
	public static String valorValoresDefectoManejaVentaTarjetaClienteOdontosinEmision =ConstantesBD.acronimoNo + "@@"+ ConstantesBD.acronimoNo;
	
	/**
	 * 
	 */
	public static String valorValoresDefectoLasCitasDeControlSePermitenAsignarA="@@";

	 
	/**
	 * Nombre para manejar el valor por defecto
	 */
	public static String valorValoresDefectoNumeroDiasControMedOrdenados= "@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoInterfazContableRecibosCajaERP="@@";
	
	
	/**
	 * 
	 */
	public static String valorValoresDefectoValidarAdministracionMedicamentosEgresoMedico="@@";

	/**
	 * 
	 */
	public static String valorValoresDefectoPermitirConsultarEpicrisisSoloProf=ConstantesBD.acronimoSi+"@@S"+ConstantesBD.acronimoSi;
	
	/**
	 * 
	 */
	public static String valorValoresDefectoMostrarAntecedentesParametrizadosEpicrisis=ConstantesBD.acronimoNo+"@@"+ConstantesBD.acronimoNo;
	
	
	/**
	 * Valor para manejar el valor de la hora de corte del historico de camas
	 */
	public static String valorValoresDefectoHoraCorteHistoricoCamas= "@@";
	
	
	/**
	 * valor para manejar los minutos limite de alerta para la reserva.
	 */
	public static String valorValoresDefectoMinutosLimiteAlertaReserva="@@";
	
	
	/**
	 * valor para manejar los minutos limite de alerta para pacientes con salida de urgencias.
	 */
	public static String valorValoresDefectoMinutosLimiteAlertaPacienteConSalidaUrgencias="@@";
	
	/**
	 * valor para manejar los minutos limite de alerta para pacientes con salida de hospitalizacion.
	 */
	public static String valorValoresDefectoMinutosLimiteAlertaPacienteConSalidaHospitalizacion="@@";
	
	
	/**
	 * valor para manejar los minutos limite de alerta para pacientes por remitir uregencias.
	 */
	public static String valorValoresDefectoMinutosLimiteAlertaPacientePorRemitirUrgencias="@@";
	
	
	/**
	 * valor para manejar los minutos limite de alerta para pacientes por remitir Hospitalizacion.
	 */
	public static String valorValoresDefectoMinutosLimiteAlertaPacientePorRemitirHospitalizacion="@@";
	
	/**
	 * valor para manejar el identificador institucion para reportar archivos colsanitas
	 */
	public static String valorValoresDefectoIdentificadorInstitucionArchivosColsanitas="@@";	
	
	/**
	 * Valor para manejar lo referente a Consolidar Cargos
	 */
	public static String valorValoresDefectoConsolidarCargos= ConstantesBD.acronimoNo + "@@"+ ConstantesBD.acronimoNo;
	
	/**
	 * Valor para manejar lo referente a Conteos Vï¿½lisos Ajustar Inventario Fï¿½sico
	 */
	public static String valorValoresDefectoConteosValidosAjustarInvFisico= "@@";
	
	/**
	 * Mensaje que se muestra al usuario en caso de no tener un CIE vï¿½lido
	 */
	public static String mensajeTipoCieInvalido = "FALTA DEFINIR TIPO CIE VIGENTE PARA LOS DIAGNOSTICOS";

	
	
	/**
	 * Valor para el parametro Tiempo maximo para el reingreso de urgencias
	 * */
	public static String valorTiempoMaximoReingresoUrgencias = "@@";
	
	/**
	 * Valor para el parametro Tiempo maximo para el reingreso de hospitalizaciï¿½n
	 * */
	public static String valorTiempoMaximoReingresoHospitalizacion = "@@";
	
	/**
	 * Valor para el parametro Permitir Facturar Reingresos Independientes
	 * */
	public static String valorPermitirFacturarReingresosIndependientes = "@@";

	/**
	 * Valor para el parametro Liberar cama de hospitalizacion despues de facturar
	 * */
	public static String  valorLiberarCamaHospitalizacionDespuesFacturar= "@@";
	
	/**
	 *  Parametro que define si ejecutara o no proceso de estancia automatica en el sistema
	 */
	public static String valorValoresDefectoGenerarEstanciaAutomatica="@@";
	
	/**
	 *  Parametro que define la hora en que ejecutara o no proceso de estancia automatica en el sistema
	 */
	public static String valorValoresDefectoHoraGenerarEstanciaAutomatica="@@";
	
	/**
	 *  Parametro que define si incluye o no tipo de pacientes cirugia ambulatoria para gneracion del cargo automatico de la estancia
	 */
	public static String valorValoresDefectoIncluirTipoPacienteCirugiaAmbulatoria="@@";
	
	
	/**
	 * valor para vlor por defecto
	 */
	public static String valorValoresDefectoTipoConsecutivoManejar="@@";
	
	/**
	 * Valor para Hacer Requerida Informaciï¿½n RIPS en Cargos Drectos
	 * */
	public static String valorValoresDefectoRequeridaInfoRipsCargosDirectos = "@@";
	
	/**
	 * Valor para manejar el valor por defecto de asigna valoracion de cirugia ambulatoria a Hospitalizado
	 * */
	public static String valorValoresDefectoAsignaValoracionCxAmbulatoriaHospitalizado = "@@";

	
	public static String valorValoresDefectoInterfazConsecutivoFacturasOtroSistema = ConstantesBD.acronimoNo+"@@"+ConstantesBD.acronimoNo;
	
	public static String valorValoresDefectoControlaInterpretacionProcedimientosEvolucion = ConstantesBD.acronimoNo+"@@"+ConstantesBD.acronimoNo;
	
	public static String valorValoresDefectoValidarRegistroEvolucionesParaOrdenes = ConstantesBD.acronimoNo+"@@"+ConstantesBD.acronimoNo;
	
	/**
	 * Comprobacion de derechos capitacion obligatoria
	 * */
	public static String valorValoresDefectoComprobacionDerCapitacionObliga = "@@";
	
	/**
	 * Requiere Autorizacion para Anular Facturas
	 */
	public static String valorValoresDefectoRequiereAutorizarAnularFacturas = "@@";
	
	/**
	 * Concepto para ajuste de entrada
	 */
	public static String valorValoresDefectoConceptoParaAjusteEntrada = "@@";
	
	/**
	 * Concepto para ajuste de salida
	 */
	public static String valorValoresDefectoConceptoParaAjusteSalida = "@@";
	
	/**
	 * Permitir Modificar Conceptos de Ajuste
	 */
	public static String valorValoresDefectoPermitirModificarConceptosAjuste = "@@";
	
	/**
	 * Valor por defecto Dias Restriccion Citas Incumplidas
	 */
	public static String valorValoresDefectoDiasRestriccionCitasIncumplidas = "0@@0";
	
	/**
	 * Permitir Modificar la fecha de solicitud pedidos insumos
	 */
	public static String valorValoresDefectoPermitirModificarFechaSolicitudPedidos = ConstantesBD.acronimoNo+"@@"+ConstantesBD.acronimoNo;
	
	/**
	 * Permitir Modificar la fecha de solicitud pedidos traslado
	 */
	public static String valorValoresDefectoPermitirModificarFechaSolicitudTraslado = ConstantesBD.acronimoNo+"@@"+ConstantesBD.acronimoNo;
	
	/**
	 * Valor por Defecto Bï¿½squeda Estï¿½ndar Artï¿½culos
	 */
	public static String valorValoresDefectoCodigoManualEstandarBusquedaArticulos = ConstantesIntegridadDominio.acronimoAxioma+"@@"+ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoAxioma);
	

	/**
	 * Valor del Parï¿½metro General Nï¿½mero de Dï¿½as para Responder Glosas
	 */
	public static String valorValoresDefectoNumeroDiasResponderGlosas = "@@";
	
	/**
	 * Valor Parï¿½metro General Produccion En Paralelo Con Sistema Anterior
	 */
	public static String valorValoresDefectoProduccionEnParaleloConSistemaAnterior = "@@";
	

	/** * Valor del Parï¿½metro General Generar Ajuste Automatico desde el Registro de la Respuesta  */
	public static String valorValoresDefectoGenerarAjusteAutoRegRespuesta = "@@";

	/** * Valor del Parï¿½metro General Generar Ajuste Automatico desde el Registro de la Respuesta para Respuestas de Conciliacion */
	public static String valorValoresDefectoGenerarAjusteAutoRegRespuesConciliacion = "@@";

	/** * Valor del Parï¿½metro General Formato Impresion Respuesta de Glosa */
	public static String valorValoresDefectoFormatoImpresionRespuesGlosa = "@@";

	/** * Valor del Parï¿½metro General Imprimir Firmas en Impresion Respuesta de Glosa */
	public static String valorValoresDefectoImprimirFirmasImpresionRespuesGlosa = "@@";
	
	/**
	 * Valor Parï¿½metro General Validar Auditor
	 */
	public static String valorValoresDefectoValidarAuditor = "@@";
	
	/**
	 * Valor Parï¿½metro General Validar Usuario Glosa
	 */
	public static String valorValoresDefectoValidarUsuarioGlosa = "@@";
	
	/**
	 * Valor Parï¿½metro General Nï¿½mero de Glosas Registradas Por Factura
	 */
	public static String valorValoresDefectoNumeroGlosasRegistradasXFactura = "@@";
	
	/**
	 * Valor Parï¿½metro General Nï¿½mero de Dï¿½as para Notificar Glosa
	 */
	public static String valorValoresDefectoNumeroDiasNotificarGlosa = "@@";
	/**
	 * Postular Fechas en respuesta de DyT
	 */
	public static String valorValoresDefectoPostularFechasEnRespuestaDyT = ConstantesBD.acronimoNo+"@@"+ConstantesBD.acronimoNo;
	
	
	/**
	 * Valor Parï¿½metro General Nï¿½mero de Dï¿½as busqueda reportes 
	 */
	public static String valorValoresDefectoNumeroDiasBusquedaReportes = "0@@0";


	/**
	 * Valor Parï¿½metro General Nï¿½mero de Dï¿½gitos Captura en Nï¿½mero de Identificaciï¿½n Pacientes
	 */
	public static String valorValoresDefectoNumDigCaptNumIdPac = "@@";
	
	/**
	 * Valor parï¿½metro general permitir interpretar ordenes de respuesta mï¿½ltiple sin finalizar
	 */
	public static String valorValoresDefectoPermIntOrdRespMulSinFin = ConstantesBD.acronimoNo + "@@" + ConstantesBD.acronimoNo; 
	
	/**
	 * Valor parï¿½metro general permitir interpretar ordenes de respuesta mï¿½ltiple sin finalizar
	 */
	public static String valorValoresDefectoTiemSegVeriInterShaioProc = "0@@0";
	
	/**
	 * Valor parï¿½metro general nomero mpaximo de dias generaciï¿½n ordenes amb de servicios
	 */
	public static String valorValoresDefectoNumMaxDiasGenOrdenesAmbServ = "30@@30";
	
	 /**
	 * Valor parámetro general Múltiplo en minutos para generación de citas
	 */
	public static String valorMultiploMinGeneracionCita = "5@@5";
	
	/**
	 * Valor parámetro general Número de días anteriores a la fecha actual para mostrar agenda odontológica
	 */
	public static String valorNumDiasAntFActualAgendaOd= "0@@0";
	
	/**
	 * Valor parámetro general Formato Impresión Conciliación
	 */
	public static String valorFormatoImpresionConciliacion= ConstantesIntegridadDominio.acronimoEstandar +"@@"+ ConstantesIntegridadDominio.acronimoEstandar;
	
	/**
	 * Valor parámetro general Validar que la Glosa reiterada sea igual a la Glosa inicial de la factura
	 */
	public static String valorValidarGlosaReiterada= ConstantesBD.acronimoNo +"@@"+ ConstantesBD.acronimoNo;
	
	
	/**
	 * Valor parï¿½metro general nomero mpaximo de dias generaciï¿½n ordenes amb de servicios
	 */
	public static String valorManejoEspecialInstitucionesOdontologia =  ConstantesBD.acronimoNo + "@@" + ConstantesBD.acronimoNo;
	
	/**
	 * valor para manejar el Mï¿½ximo Nï¿½mero Cuotas Financiaciï¿½n
	 */
	public static String valorMaximoNumeroCuotasFinanciacion="@@";
	
	/**
	 * valor para el Mï¿½ximo nï¿½mero de dï¿½as de financiaciï¿½n por cuota
	 */
	public static String valorMaximoNumeroDiasFinanciacionPorCuota="@@";
	
	/**
	 * valor para el Formato documentos de garantï¿½a - pagarï¿½
	 */
	public static String valorFormatoDocumentosGarantia_Pagare="@@";
	
	/**
	 * valor para la ocupacion odontï¿½logo
	 */
	public static String valorOcupacionOdontologo="@@";
	
	/**
	 * valor para la ocupacion Auxiliar de odontï¿½logo
	 */
	public static String valorOcupacionAuxiliarOdontologo="@@";
	
	/**
	 * valor para la edad final niï¿½ez
	 */
	public static String valorEdadFinalNinez="@@";
	
	/**
	 * valor para la edad final niï¿½ez
	 */
	public static String valorEdadInicioAdulto="@@";
	
	/**
	 * valor para determinar si se obliga a dar incapacidad a los pacientes hospitalizados
	 */
	public static String valorObligarRegIncapaPacienteHospitalizado="@@";
	
	/**
	 * 
	 */
	public static String valorMinutosCaducaCitaReservada = "@@";
	
	/**
	 * 
	 */
	public static String valorMinutosCaducaCitaAsignadasReprogramadas = "@@";
	
	/**
	 * 
	 */
	public static String valorEjecutarProcAutoActualizacionCitasOdo = "@@";
	
	/**
	 * 
	 */
	public static String valorHoraEjecutarProcAutoActualizacionCitasOdo = "@@";

	/**
	 * 
	 */
	public static String valorMinutosEsperaAsignarCitasOdoCaducadas = "@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoEscalaPacientePerfil = "@@";
	
	/**
	 * Valor parï¿½metro general Utilizan programas odontologicos en institucion
	 */
	public static String valorValoresDefectoUtilizanProgramasOdontologicosEnInstitucion =  "@@";
	
	
	/**
	 * 
	 */
	public static String valorValoresDefectoTiempoVigenciaPresupuestoOdo = "@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoPermiteCambiarServiciosCitaAtencionOdo = "@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoValidaPresupuestoOdoContratado = "@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoConveniosAMostrarXDefectoPresupuestoOdo = "@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoTiempoMaxEsperaInactivarPresupuestoOdoSuspTemp = "@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoEjecutarProcesoAutoActualizacionEstadosOdo = "@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoHoraEjecutarProcesoAutoActualizacionEstadosOdo = "@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoMotivoCancelacionPresupuestoSuspendidoTemp = "@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoMaximoTiempoSinEvolucionarParaInactivarPlanTratamiento = "@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoPrioridadParaAplicarPromocionesOdo = "@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoDiasParaDefinirMoraXDeudaPacientes = "@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoContabilizacionRequeridoProcesoAsocioFacCuentaCapitacion = "@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoCuentaContablePagare = "@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoCuentaContableLetra = "@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoInstitucionRegistraAtencionExterna = "@@";
	
	
	//Anexo 992
	
	/**
	 * nombre para el valor por defecto de imprimir firmas impresion cc capitacion
	 */
	public static String valorValoresDefectoImprimirFirmasImpresionCCCapitacion=ConstantesBD.acronimoNo +"@@"+ConstantesBD.acronimoNo ;
	
	/**
	 * nombre para el valor por defecto del encabezado formato Impresion factura o cc capitacion
	 */
	public static String valorValoresDefectoEncabezadoFormatoImpresionFacturaOCCCapitacion="@@";


	/**
	 * nombre para el valor por defecto de pie de pagina del formato de impresion defactura o cc capitacion
	 */
	public static String valorValoresDefectoPiePaginaFormatoImpresionFacturaOCCCapitacion="@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoImprimirFirmasEnImpresionCC=ConstantesBD.acronimoNo +"@@"+ConstantesBD.acronimoNo ;
	
	/**
	 * 
	 */
	public static String valorValoresDefectoNumeroMesesAMostrarEnReportesPresupuestoCapitacion="@@";
	
	//Fin Anexo 992
	
	//Anexo 958
	/**
	 * 
	 */
	public static String valorValoresDefectoReciboCajaAutomaticoGeneracionFacturaVaria=ConstantesBD.acronimoNo +"@@"+ConstantesBD.acronimoNo;
	
	/**
	 * 
	 */
	public static String valorValoresDefectoConceptoIngresoFacturasVarias="@@";
	
	//Fin Anexo 958
	
	
	
	//Anexo 888
	/**
	 * 
	 */
	public static String valorValoresDefectoEsRequeridoProgramarCitaAlContratarPresupuestoOdon=ConstantesBD.acronimoNo +"@@"+ConstantesBD.acronimoNo;

	/**
	 * 
	 */
	public static String valorValoresDefectoMotivoAnulacionSolicitudAutorizacionDescuentoPresupeustoOdontologico="@@";
	
	
	//Fin anexo 888
	
	//Anexo 888 Pt II
	/**
	 * 
	 */
	public static String valorValoresDefectoRequeridoProgramarCitaControlAlTerminarPresupuestoOdon="@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoRequeridoProgramarCitaControlAlTerminarPresupuestoOdonServicio="@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoTiempoDiasParaAgendarCitaControlAlTerminarPresupuestoOdon="@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoInstitucionManejaFacturacionAutomatica="@@";
	//Fin anexo 888 Pt II
	
	//Anexo 959
	/**
	 * 
	 */
	public static String valorValoresDefectoManejaConsecutivoFacturaPorCentroAtencion="@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoAreaAperturaCuentaAutoPYP="@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoAprobarGlosaRegistro="@@";

	
	
	/**
	 * 
	 */
	public static String valorValoresDefectoManejaConsecutivosTesoreriaPorCentroAtencion="@@";
	
	/**
	 * 
	 */
	public static String valorValoresDefectoTamanioImpresionRC="@@";
	
	/**
	 * Valor por defecto Mostrar gr&aacute;fica de &Iacute;ndice de placa para calcular el porcentaje de &iacute;ndice de placa 
	 */
	public static String valorValoresDefectoMostrarGraficaCalculoIndicePlaca = "@@";


	
	/**
	 * 
	 */
	public static String valorValoresDefectoValidarPacienteParaVentaTarjeta=ConstantesBD.acronimoSi+"@@"+ConstantesBD.acronimoSi;

	
	/**
	 * 
	 */
	public static String valorValoresDefectoReciboCajaAutomaticoVentaTarjeta=ConstantesBD.acronimoSi+"@@"+ConstantesBD.acronimoSi;
	
	/**
	 * 
	 */
	public static String valorValoresDefectoPermitirRegistrarReclamacionCuentasNoFacturadas=ConstantesBD.acronimoNo+"@@"+ConstantesBD.acronimoNo;
	
	/**
	 * 
	 */
	public static String valorValoresDefectoPermitirFacturarReclamarCuentasConRegistroPendientes=ConstantesBD.acronimoNo+"@@"+ConstantesBD.acronimoNo;
	
	
	/**
	 * 
	 */
	public static String valorValoresDefectoPermitirOrdenarMedicamentosPacienteUrgenciasConEgreso=ConstantesBD.acronimoNo+"@@"+ConstantesBD.acronimoNo;
	
	
	/**
	 * 
	 */
	public static String valorValoresDefectoMostrarAdminMedicamentosArticulosDespachoCero=ConstantesBD.acronimoNo+"@@"+ConstantesBD.acronimoNo;



	/**
	 * Valor valores por defecto al cancelar cita odontologica deja automaticamente reservado
	 */
	public static String valorValoresDefectoAlCancelarCitasOdontoDejarAutoEstadoReprogramar=ConstantesBD.acronimoSi+"@@"+ConstantesBD.acronimoSi;
	
	/**
	 * 
	 */
	public static String valorValoresDefectoNumMaximoReclamacionesAccEventoXFactura="@@";
	


	public static String valorValoresDefectoHacerRequeridoValorAbonoAplicadoAlFacturar="@@";
	

	/**
	 * Nombre para manejar el valor por defecto del formato factura varia
	 */
	public static String nombreValoresDefectoFormatoFacturaVaria= "formato_factura_varia";
	
	
	/**
	 * Valor por defecto para el formato de la factura varia
	 */
	public static String valorValoresDefectoFormatoFacturaVaria=1+"@@"+"Estandar";
	
	
	
	
	
	/**
	 * Nombre para manejar el valor por defecto de modificar fecha hora inicio de atencion odontologica
	 */
	public static String nombreValoresDefectoModificarFechaHoraInicioAtencionOdonto= "modificar_fecha_hora_inicio_atencion_odonto";
	
	
	/**
	 * Valor por defecto para  modificar fecha hora inicio de atencion odontologica
	 */
	public static String valorValoresDefectoModificarFechaHoraInicioAtencionOdonto= ConstantesBD.acronimoNo+"@@"+ ConstantesBD.acronimoNo;
	
	/**
	 * Nombre para manejar el valor por defecto de Entidad subcontratada para centros de costo internos en autorización de capitación subcontratada.
	 */
	public static String nombreValoresDefectoEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada= "entidad_subcontratada_centros_costo_autorizacion_capitacion_subcontratada";
	
	/**
	 * Valor por defecto de Entidad subcontratada para centros de costo internos en autorización de capitación subcontratada.
	 */
	public static String valorValoresDefectoEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada = "@@";
	
	
	/**
	 * Nombre para manejar el valor por defecto de la Prioridad de la Entidad subcontratada.
	 */
	public static String nombreValoresDefectoPrioridadEntidadSubcontratada= "prioridad_entidad_subcontratada";
	
	/**
	 * Valor por defecto de la Prioridad de la Entidad subcontratada.
	 */
	public static String valorValoresDefectoPrioridadEntidadSubcontratada = "@@";
	
	
	
	
	
	
	/**
	 * Nombre para manejar si requiere autorización capitación subcontratada para facturar.
	 */
	public static String nombreValoresDefectoRequiereAutorizacionCapitacionSubcontratadaParaFacturar= "requiere_autorizacion_capitacion_subcontratada_para_facturar";
	
	/**
	 * Valor por defecto para manejar si requiere autorización capitación subcontratada para facturar.
	 */
	public static String valorValoresDefectoRequiereAutorizacionCapitacionSubcontratadaParaFacturar = ConstantesBD.acronimoNo+"@@"+ ConstantesBD.acronimoNo;
	
	/**
	 * Nombre para manejar el valor por defecto de la Prioridad de la Entidad subcontratada.
	 */
	public static String nombreValoresDefectoManejaConsecutivoFacturasVariasPorCentroAtencion= "maneja_consecutivo_facturas_varias_centro_atencion";
	
	/**
	 * Valor por defecto de la Prioridad de la Entidad subcontratada.
	 */
	public static String valorValoresDefectoManejaConsecutivoFacturasVariasPorCentroAtencion = ConstantesBD.acronimoNo+"@@"+ ConstantesBD.acronimoNo;
	
	/**
	 * Nombre del parámetro para definir el formato de impresiòn de una autorización de entidad subcontratada
	 */
	public static String nombreValoresDefectoFormatoImpresionAutorEntidadSub = "formato_impresion_autor_entidad_sub";	
	
	/**
	 * valor por defecto del parámetro para definir el formato de impresiòn de una autorización de entidad subcontratada
	 */
	public static String valorValoresDefectoFormatoImpresionAutorEntidadSub = "@@";
	
	
	/**
	 * Nombre del parámetro para definir el encabezado del formato de impresión de una autorización de entidad subcontratada
	 */
	public static String nombreValoresDefectoEncFormatoImpresionAutorEntidadSub = "encabezado_formato_impresion_autor_entidad_sub";	
	
	/**
	 * Valor por defecto del parámetro para definir el encabezado del formato de impresión de una autorización de entidad subcontratada
	 */
	public static String valorValoresDefectoEncFormatoImpresionAutorEntidadSub = "@@";
	
	
	/**
	 * Nombre del parámetro para definir el pié de página del formato de impresión de una autorización de entidad subcontratada
	 */
	public static String nombreValoresDefectoPiePagFormatoImpresionAutorEntidadSub = "pie_pag_formato_impresion_autor_entidad_sub";	
	
	/**
	 * Valor por defecto del parámetro para definir el pié de página del formato de impresión de una autorización de entidad subcontratada
	 */
	public static String valorValoresDefectoPiePagFormatoImpresionAutorEntidadSub = "@@";
	
	
	/**
	 * Nombre del parámetro para definir la vigencia que tendrá la autorización.
	 */
	public static String nombreValoresDefectoDiasVigenciaAutorIndicativoTemp = "dias_vigencia_autor_indicativo_temp";	
	
	/**
	 * Valor del parámetro para definir la vigencia que tendrá la autorización.
	 */
	public static String valorValoresDefectoDiasVigenciaAutorIndicativoTemp = "@@";
	
	/**
	 * Nombre del parámetro para definir los días de prórroga de una autorización
	 */
	public static String nombreValoresDefectoDiasProrrogaAutorizacion = "dias_prorroga_autorizacion";	
	
	/**
	 * Valor del parámetro para definir los días de prórroga de una autorización
	 */
	public static String valorValoresDefectoDiasProrrogaAutorizacion = "@@";	
	
	/**
	 * Nombre del parámetro para definir los días de cálculo de la fecha de vencimiento de la Autorización de servicios
	 */
	public static String nombreValoresDefectoDiasCalcularFechaVencAutorizacionServicio = "dias_calculo_fecha_venc_auto_servicio";	
	
	/**
	 * Valor del parámetro para definir los días de cálculo de la fecha de vencimiento de la Autorización de servicios
	 */
	public static String valorValoresDefectoDiasCalcularFechaVencAutorizacionServicio = "@@";	
		
	/**
	 * Nombre del parámetro para definir los días de cálculo de la fecha de vencimiento de la Autorización de artículos
	 */
	public static String nombreValoresDefectoDiasCalcularFechaVencAutorizacionArticulo = "dias_calculo_fecha_venc_auto_articulo";	
	
	/**
	 * Valor del parámetro para definir los días de cálculo de la fecha de vencimiento de la Autorización de artículos
	 */
	public static String valorValoresDefectoDiasCalcularFechaVencAutorizacionArticulo = "@@";	
		
	/**
	 * Nombre del parámetro para definir los días de vigencia para solicitar una nueva autorización de estancia
	 */
	public static String nombreValoresDefectoDiasVigentesNuevaAutorizacionEstanciaSerArt = "dias_vigentes_fecha_sol_auto_estan_serv_art";	
	
	/**
	 * Valor del parámetro para definir los días de vigencia para solicitar una nueva autorización de estancia
	 */
	public static String valorValoresDefectoDiasVigentesNuevaAutorizacionEstanciaSerArt = "@@";	
	
	
	/**
	 * Nombre del parámetro para definir la hora en la cual se debe ejecutar el proceso del cierre de capitación
	 */
	public static String nombreValoresDefectoHoraProcesoCierreCapitacion = "hora_proceso_cierre_capitacion";	
	
	/**
	 * Valor del parámetro para definir la hora en la cual se debe ejecutar el proceso del cierre de capitación
	 */
	public static String valorValoresDefectoHoraProcesoCierreCapitacion = "@@";	
	
	/**
	 * Nombre del parámetro para facturar y registrar reclamaciones en cuentas con R.A.T. o R.E.C. asociados en estado pendiente
	 */
	public static String nombreValoresDefectoPermitirfacturarReclamCuentaRATREC = "permitir_facturar_recl_cuentas_rat_rec_pend";	
	
	/**
	 * Valor del parámetro para facturar y registrar reclamaciones en cuentas con R.A.T. o R.E.C. asociados en estado pendiente
	 */
	public static String valorValoresDefectoPermitirfacturarReclamCuentaRATREC = "@@";
	
	
	/**
	 * Nombre del parámetro para  definir si se muestran todas las solicitudes asociadas a la Factura en la funcionalidad Detalle de Glosas Confirmadas.
	 */
	public static String nombreValoresDefectoMostrarDetalleGlosasFacturaSolicFactura= "mostrar_detalle_glosas_factura_solic_factura";	
	
	/**
	 * Valor del parámetro para  definir si se muestran todas las solicitudes asociadas a la Factura en la funcionalidad Detalle de Glosas Confirmadas.
	 */
	public static String valorValoresDefectoMostrarDetalleGlosasFacturaSolicFactura = "@@";
	
	/**
	 * Nombre del parámetro para  definir el formato Impresión Reserva Cita Odontológica 
	 */
	public static String nombreValoresDefectoFormatoImpReservaCitaOdonto= "formato_imp_reserva_cita_odonto";	
	
	/**
	 * Valor del parámetro para  definir el formato Impresión Reserva Cita Odontológica 
	 */
	public static String valorValoresDefectoFormatoImpReservaCitaOdonto = "@@";	
	
	/**
	 * Nombre del parámetro para  definir el formato Impresión de asignaciónn de Cita Odontológica 
	 */
	public static String nombreValoresDefectoFormatoImpAsignacionCitaOdonto= "formato_imp_asignacion_cita_odonto";	
	
	/**
	 * Valor del parámetro para  definir el formato Impresión de asignaciónn de Citas Odontológicas
	 */
	public static String valorValoresDefectoFormatoImpAsignacionCitaOdonto = "@@";
	
	
	/**
	 * Nombre del parámetro para validar que exista información de cierre de órdenes médicas 
	 */
	public static String nombreValoresDefectoFechaInicioCierreOrdenMedica= "fecha_inicio_cierre_orden_medica";	
	
	/**
	 * Valor del parámetro para validar que exista información de cierre de órdenes médicas
	 */
	public static String valorValoresDefectoFechaInicioCierreOrdenMedica ="@@";
	
	
	/**
	 * Nombre del parámetro para definir el esquema tarifario con el cual se realizará la valorización de las ordenes
	 */
	public static String nombreValoresDefectoEsquemaTariServiciosValorizarOrden= "esquema_tarif_serv_valorizar_orden";	
	
	/**
	 * Valor del parámetro para definir el esquema tarifario de servicios con el cual se realizará la valorización de las ordenes
	 */
	public static String valorValoresDefectoEsquemaTariServiciosValorizarOrden ="@@";
	
	
	/**
	 * Nombre del parámetro para definir si se debe o no generar una cita programada al generar la solicitud de interconsultas odontológicas.
	 */
	public static String nombreValoresDefectoSolicitudCitaInterconsultaOdontoCitaProgramada= "solicitud_cita_interconsulta_odonto_cita_programada";	
	
	/**
	 * Valor del parámetro para definir si se debe o no generar una cita programada al generar la solicitud de interconsultas odontológicas.
	 */
	public static String valorValoresDefectoSolicitudCitaInterconsultaOdontoCitaProgramada="@@";
	
	
	/**
	 * Valor del parámetro para definir la hora de ejecución del proceso para la inactivación de usuario por inactividad en el sistema
	 */
	public static String nombreValoresDefectoHoraEjecProcesoInactivarUsuarioInacSistema = "hora_ejec_proceso_inactivar_usuario_inact_sistema";

	/**
	 * Valor del parámetro para definir la hora de ejecución del proceso para la inactivación de un usuario por inactividad en el sistema
	 */
	public static String valorValoresDefectoHoraEjecProcesoInactivarUsuarioInacSistema="@@";
	
	/**
	 * Valor del parámetro para definir la hora de ejecución del proceso para la caducidad de contraseña por inactividad en el sistema
	 */
	public static String nombreValoresDefectoHoraEjeProcesoCaduContraInacSistema = "hora_eje_proceso_cadu_contra_inac_sistema";

	/**
	 * Valor del parámetro para definir la hora de ejecución del proceso para la caducidad de contraseña por inactividad en el sistema
	 */
	public static String valorValoresDefectoHoraEjeProcesoCaduContraInacSistema="@@";
	
	/**
	 * Valor del parámetro Número de días vigencia de contraseña usuario.
	 */
	public static String nombreValoresDefectoDiasVigenciaContraUsuario = "dias_vigencia_contra_usuario";

	/**
	 * Valor del parámetro Número de días vigencia de contraseña usuario.
	 */
	public static String valorValoresDefectoDiasVigenciaContraUsuario="@@";
	
	/**
	 * Valor del parámetro Número de días para finalizar vigencia de contraseña y mostrar alerta
	 */
	public static String nombreValoresDefectoDiasFinalesVigenciaContraMostrarAlerta = "dias_finales_vigencia_contra_mostrar_alerta";

	/**
	 * Valor del parámetro Número de días para finalizar vigencia de contraseña y mostrar alerta
	 */
	public static String valorValoresDefectoDiasFinalesVigenciaContraMostrarAlerta="@@";
	
	/**
	 * Nombre del parámetro para definir el esquema tarifario de medicamentos e insumos con el cual se realizará la valorización de las ordenes
	 */
	public static String nombreValoresDefectoEsquemaTariMedicamentosValorizarOrden= "esquema_tarif_medic_insum_valorizar_orden";	
	
	/**
	 * Valor del parámetro para definir el esquema tarifario de medicamentos e insumos con el cual se realizará la valorización de las ordenes
	 */
	public static String valorValoresDefectoEsquemaTariMedicamentosValorizarOrden ="@@";
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoPermitirModificarDatosUsuariosCapitados= "permitir_modificar_datos_usu_capitados";	
	
	/**
	 * 
	 */
	public static String valorValoresDefectoPermitirModificarDatosUsuariosCapitados =ConstantesBD.acronimoSi+"@@"+ConstantesBD.acronimoSi;
	
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoManejaHojaAnestesia="se_maneja_hoja_anestesia";
		
		
	/**
	 * 
	 */
	public static String valorValoresDefectoManejaHojaAnestesia=ConstantesBD.acronimoNo+"@@"+ConstantesBD.acronimoNo;
	
	
	/**
	 * 
	 */
	public static String nombreValoresDefectoPermitirModificarDatosUsuariosCapitadosModificarCuenta= "permitir_modificar_datos_usu_capitados_modificar_cuenta";	
	
	/**
	 * 
	 */
	public static String valorValoresDefectoPermitirModificarDatosUsuariosCapitadosModificarCuenta =ConstantesBD.acronimoSi+"@@"+ConstantesBD.acronimoSi;
	
	
	/** * */
	public static String nombreValoresDefectoEsquemaTarifarioAutocapitaSubCirugiasNoCurentos = "esquema_tarifario_auto_capita_sub_cirugias_nocurentos";
	public static String valorValoresDefectoEsquemaTarifarioAutocapitaSubCirugiasNoCurentos = "@@";

	
	/** * */
	public static String nombreValoresDefectoValidarDisponibilidadSaldoPresupuestoAutorizacionesCapotaAutomatica = "validar_disponibilidad_saldo_presupuesto_autorizaciones_capita_automatica";
	public static String valorValoresDefectoValidarDisponibilidadSaldoPresupuestoAutorizacionesCapotaAutomatica = "@@";
	
	/**
	 * Nombre del parámetro para definir hacer_requerida_seleccion_centro_atencion_asignado_subir_paciente_individual
	 */
	public static String nombreValoresDefectoHacerRequeridaSeleccionCentroAtencionAsignadoSubirPacienteIndividual = "hacer_requerida_seleccion_centro_atencion_asignado_subir_paciente_individual";
	
	/**
	 * Valor del parámetro para definir hacer_requerida_seleccion_centro_atencion_asignado_subir_paciente_individual
	 */
	public static String valorValoresDefectoHacerRequeridaSeleccionCentroAtencionAsignadoSubirPacienteIndividual = ConstantesBD.acronimoNo+"@@"+ConstantesBD.acronimoNo;

	/**
	 * Nombre del parámetro para definir maneja_consecutivos_notas_pacientes_centro_atención
	 */
	public static String nombreValoresDefectoManejaConsecutivosNotasPacientesCentroAtencion = "maneja_consecutivos_notas_pacientes_centro_atencion";

	/**
	 * Valor del parámetro para definir maneja_consecutivos_notas_pacientes_centro_atención
	 */
	public static String valorValoresDefectoManejaConsecutivosNotasPacientesCentroAtencion = ConstantesBD.acronimoNo+"@@"+ConstantesBD.acronimoNo;
	
	/**
	 * Nombre del parámetro para definir naturaleza_notas_pacientes_manejar
	 */
	public static String nombreValoresDefectoNaturalezaNotasPacientesManejar = "naturaleza_notas_pacientes_manejar";

	/**
	 * Valor del parámetro para definir naturaleza_notas_pacientes_manejar
	 */
	public static String valorValoresDefectoNaturalezaNotasPacientesManejar = "@@";
	
	/**
	 * Nombre del parámetro para definir sise permite el recaudo de cajas mayores
	 */
	public static String nombreValoresDefectoPermitirRecaudosCajaMayor="permitir_realiziar_recaudos_a_cajas_tipo_mayor";
	
	
	/**
	 * Valor por defecto para la permitir realziar recuados de cajas tipo mayor
	 */
	public static String valorValoresDefectoPermitirRecaudosCajaMayor = ConstantesBD.acronimoNo+"@@"+ConstantesBD.acronimoNo;
	
	/**
	 * Parámetro que permite seleccionar la Vía de Ingreso por Tipo de Orden: Orden Ambulatoria
	 * 
	 **/
	public static String nombreValoresDefectoViaIngresoValidacionesOrdenesAmbulatorias = "via_ingreso_validaciones_ordenes_ambulatorias";
	public static String valorValoresDefectoViaIngresoValidacionesOrdenesAmbulatorias = ConstantesBD.codigoViaIngresoAmbulatorios+"@@"+ConstantesBD.codigoViaIngresoAmbulatorios;
	
	
	/**
	 * Parámetro que permite seleccionar la Vía de Ingreso por Tipo de Orden: Petición
	 * 
	 **/
	public static String nombreValoresDefectoViaIngresoValidacionesPeticiones = "via_ingreso_validaciones_peticiones";
	public static String valorValoresDefectoViaIngresoValidacionesPeticiones = ConstantesBD.codigoViaIngresoAmbulatorios+"@@"+ConstantesBD.codigoViaIngresoAmbulatorios;
	
	public static String nombreValoresDefectoCantidadMaximoDiasConsultaIngreso="dias_maximo_para_consultar_ingresos_en_Historia_de_Atenciones";
	public static String valorValoresDefectoCantidadMaximoDiasConsultaIngreso ="@@";
	
	
	/**
	 * nombre del parametro para definir los Meses Maximo para Administrar Autorizaciones de Capitacion vencidas
	 */
	public static String valorValoresDefectoMesesMaxAdminAutoCapVencidas = "@@";
	/**
	 * nombre del parametro para definir los días maximos de prorroga de una autorizacion de Articulo
	 */
	public static String valorValoresDefectoDiasMaxProrrogaAutorizacionArticulo = "@@";
	
	/**
	 * nombre del parametro para definir los días maximos de prorroga de una autorizacion de Servicios
	 */
	public static String valorValoresDefectoDiasMaxProrrogaAutorizacionServicio = "@@";
	
	/**
	 * Parámetro que permite seleccionar El tipo de Paciente por Tipo de Orden: Orden Ambulatoria
	 * 
	 **/
	public static String nombreValoresDefectoTipoPacienteValidacionesOrdenesAmbulatorias = "tipo_paciente_validaciones_ordenes_ambulatorias";
	public static String valorValoresDefectoTipoPacienteValidacionesOrdenesAmbulatorias = ConstantesBD.acronimoTipoPacienteAmbulatorio+"@@"+ConstantesBD.acronimoTipoPacienteAmbulatorio;
	
	
	/**
	 * Parámetro que permite seleccionar El tipo de Paciente por Tipo de Orden: Petición
	 * 
	 **/
	public static String nombreValoresDefectoTipoPacienteValidacionesPeticiones = "tipo_paciente_validaciones_peticiones";
	public static String valorValoresDefectoTipoPacienteValidacionesPeticiones = ConstantesBD.acronimoTipoPacienteAmbulatorio+"@@"+ConstantesBD.acronimoTipoPacienteAmbulatorio;
	
}
