package util;

/**
 * Interfaz para el manejo de todas las constantes de Integridad de Dominio
 * 
 * @version 1.0, Noviembre 29, 2006
 */


public interface ConstantesIntegridadDominio {

	/************************************************************************************
	 * CONSTANTES DE LOS MOMENTOS DE MUERTE DE RECIEN NACIDO
	 ************************************************************************************/
	public static String acronimoMomentoMuerteAnteparto = "ANTP";
	public static String acronimoMomentoMuerteParto = "PART";
	public static String acronimoMomentoMuerteIgnora = "IGNM";

	/************************************************************************************
	 * CONSTANTES DE PESO EDAD GESTACIONAL DE RECIEN NACIDO
	 ************************************************************************************/
	public static String acronimoPesoEdadGestacionalAdecuada = "ADE";
	public static String acronimoPesoEdadGestacionalPequeno = "PEQ";
	public static String acronimoPesoEdadGestacionalGrande = "GRA";

	/************************************************************************************
	 * CONSTANTES DE DEFECTOS CONGENITOS DE RECIEN NACIDO
	 ************************************************************************************/
	public static String acronimoDefectoCongenitoMayor = "MAY";
	public static String acronimoDefectoCongenitoMenor = "MEN";

	/************************************************************************************
	 * CONSTANTES DE CONDICION DE EGRESO DE RECIEN NACIDO
	 ************************************************************************************/
	public static String acronimoCondicionEgresoVivo = "VIVO";
	public static String acronimoCondicionEgresoTrasladado = "TRAS";
	public static String acronimoCondicionEgresoFalleceDespuesTraslado = "FDTR";
	public static String acronimoCondicionEgresoViveDespuesTrasladado = "VDTR";
	public static String acronimoCondicionEgresoConPatologia = "CPAT";
	public static String acronimoCondicionEgresoFallece = "FALL";

	/************************************************************************************
	 * CONSTANTES DE LACTANCIA DE RECIEN NACIDO
	 ************************************************************************************/
	public static String acronimoLactanciaExclusiva = "EXCL";
	public static String acronimoLactanciaParcial = "PARC";
	public static String acronimoLactanciaFormula = "FORM";

	/************************************************************************************
	 * CONSTANTES DE CONDUCTA A SEGUIR DE RECIEN NACIDO
	 ************************************************************************************/
	public static String acronimoConductaSeguirAlojamientoConjunto = "ALOCON";
	public static String acronimoConductaSeguirHospitalizacion = "HOS";
	public static String acronimoConductaSeguirSalida = "SAL";

	/************************************************************************************
	 * CONSTANTES DE TIPO DE DIAGNOSTICO DE RECIEN NACIDO
	 ************************************************************************************/
	public static String acronimoTipoDiagnosticoImpresionDiagnostica = "TDID";
	public static String acronimoTipoDiagnosticoConfirmadoRepetido = "TDCR";
	public static String acronimoTipoDiagnosticoConfirmadoNuevo = "TDCN";
	public static String acronimoTipoDiagnosticoOdontologico="TDODO";
	
	/************************************************************************************
	 * CONSTANTES DE TAMIZACION NEONATAL DE RECIEN NACIDO
	 ************************************************************************************/
	public static String acronimoNormal = "NORMA";
	public static String acronimoAnormal = "ANORM";
	public static String acronimoNosehizo = "NOHIZ";
	public static String acronimoPendienteResultado = "PENRES";

	/************************************************************************************
	 * CONSTANTES DE TIPO PINZAMIENTO CORDON DE RECIEN NACIDO
	 ************************************************************************************/
	public static String acronimoHabitual = "HABIT";
	public static String acronimoInmediato = "INMED";
	public static String acronimoCordonPrecoz = "PRECO";
	public static String acronimoCordonDiferido = "DIFER";

	/************************************************************************************
	 * CONSTANTES DE APGAR RIESGO CORDON DE RECIEN NACIDO
	 ************************************************************************************/
	public static String acronimoBajo = "BAJ";
	public static String acronimoMedio = "MED";
	public static String acronimoAlto = "ALT";

	/************************************************************************************
	 * CONSTANTES DE PRESENTACION
	 ************************************************************************************/
	public static String acronimoPresentacionCefalica = "PCEF";
	public static String acronimoPresentacionPelviana = "PPEL";
	public static String acronimoPresentacionTransversal = "PTRA";

	/************************************************************************************
	 * CONSTANTES DE CORTOCOIDES
	 ************************************************************************************/
	public static String acronimoCicloUnicoCompleto = "CACUC";
	public static String acronimoCicloUnicoIncompleto = "CACUI";
	public static String acronimoMultiples = "CAMUL";
	public static String acronimoNinguna = "CANIN";

	/************************************************************************************
	 * CONSTANTES DE INICIO T. PARTO
	 ************************************************************************************/
	public static String acronimoEspontaneo = "TPESP";
	public static String acronimoInducido = "TPIND";
	public static String acronimoCesareaElectiva = "TPCES";
	public static String acronimoCesarea = "TPCEE";

	/************************************************************************************
	 * CONSTANTES DE ACOMPAï¿½ANTE
	 ************************************************************************************/
	public static String acronimoPareja = "APAR";
	public static String acronimoFamiliar = "AFAM";
	public static String acronimoOtro = "AOTR";
	public static String acronimoAcompanianteNinguno = "ANIN";
	public static String acronimoMultas = "MULT";

	/************************************************************************************
	 * CONSTANTES DE ENEMA/RASURADO
	 ************************************************************************************/
	public static String acronimoSoloEnema = "ERSE";
	public static String acronimoSoloRasurado = "ERSR";
	public static String acronimoEnemaRasurado = "ERER";

	/************************************************************************************
	 * CONSTANTES DE TERMINACIï¿½N
	 ************************************************************************************/
	public static String acronimoEspontanea = "TESPO";
	public static String acronimoForceps = "TFORC";
	public static String acronimoEspatula = "TESPA";

	/************************************************************************************
	 * CONSTANTES DE PLACENTA
	 ************************************************************************************/
	public static String acronimoCompleta = "PCOM";
	public static String acronimoRetenida = "PRET";

	/************************************************************************************
	 * CONSTANTES DE OXITï¿½CICOS
	 ************************************************************************************/
	public static String acronimoOcitocina = "OAOCI";
	public static String acronimoOtroOcitocico = "OAOOC";

	/************************************************************************************
	 * CONSTANTES DE CONDICIï¿½N EGRESO
	 ************************************************************************************/
	public static String acronimoViva = "CVI";
	public static String acronimoConPatologia = "CCP";
	public static String acronimoTrasladoReferida = "CTR";
	public static String acronimoFallece = "CFA";
	public static String acronimoFalleceDespuesTraslado = "CFT";
	public static String acronimoSanaDespuesTraslado = "CST";

	/************************************************************************************
	 * CONSTANTES DE ANTICONCEPCIï¿½N
	 ************************************************************************************/
	public static String acronimoNoSeInforma = "ACI";
	public static String acronimoDIU = "ACD";
	public static String acronimoNatural = "ACN";
	public static String acronimoBarrera = "ACB";
	public static String acronimoHormonal = "ACH";
	public static String acronimoLigaduraTubarica = "ACL";

	/************************************************************************************
	 * CONSTANTES DE POSICIONES MATERNA
	 ************************************************************************************/
	public static String acronimoPosicionesMaternaLateralDerecho = "PLD";
	public static String acronimoPosicionesMaternaLateralIzquiedo = "PLI";
	public static String acronimoPosicionesMaternaDorsal = "PD";
	public static String acronimoPosicionesMaternaSemisentada = "PSS";
	public static String acronimoPosicionesMaternaSentada = "PSE";
	public static String acronimoPosicionesMaternaLateralParadaOCaminando = "PPC";

	/************************************************************************************
	 * CONSTANTES DE FRECUENCIA CARDIACA FECAL
	 ************************************************************************************/
	public static String acronimoFrecuenciaFecalDesaceleracionPrecoz = "FFDP";
	public static String acronimoFrecuenciaFecalDesaceleracionTradia = "FFDT";
	public static String acronimoFrecuenciaFecalDesaceleracionVariable = "FFDV";
	public static String acronimoFrecuenciaFecalDesaceleracionNinguna = "FFNI";

	/************************************************************************************
	 * CONSTANTES DE INTENSIDAD DOLOR
	 ************************************************************************************/
	public static String acronimoIntensidadDolorFuerte = "DIF";
	public static String acronimoIntensidadDolorNormal = "DIN";
	public static String acronimoIntensidadDolorDebil = "DID";

	/************************************************************************************
	 * CONSTANTES DE INTENSIDAD LOCALIZACION
	 ************************************************************************************/
	public static String acronimoIntensidadLocalizacionSuprapublico = "DLSP";
	public static String acronimoIntensidadLocalizacionSacro = "DLS";

	public static String acronimoNinguno = "NING";

	/************************************************************************************
	 * CONSTANTES DE GRUPO POBLACIONAL DEL PACIENTE
	 ************************************************************************************/
	public static String acronimoDesplazados = "DESP";
	public static String acronimoMigratorios = "MIGR";
	public static String acronimoCarcelarios = "CARC";
	public static String acronimoOtrosGruposPoblacionales = "OGPO";

	/************************************************************************************
	 * CONSTANTES DE SEXO
	 ************************************************************************************/
	public static String acronimoMasculino = "M";
	public static String acronimoFemenino = "F";

	/************************************************************************************
	 * CONSTANTES PARA EL TIPO DE EMBARAZO
	 ************************************************************************************/
	public static String acronimoTipoEmbarazoTermino = "TERM";
	public static String acronimoTipoEmbarazoProlongado = "PROL";
	public static String acronimoTipoEmbarazoPreterminado = "PRETE";

	/************************************************************************************
	 * CONSTANTES PARA LA POSICION DEL PARTO
	 ************************************************************************************/
	public static String acronimoAcostada = "PDPA";
	public static String acronimoCuclillas = "PDPC";
	public static String acronimoSentada = "PDPS";

	/************************************************************************************
	 * CONSTANTES FORMATO IMPRESION CUENTA COBRO
	 ************************************************************************************/
	public static String acronimoEstandar = "FIE";
	public static String acronimoSuba = "FIU";
	public static String acronimoSasaima = "FIS";

	/*********************************************************************************************
	 * CONSTANTES TIPOS DE CONSULTORIOS
	 * *********************************************************************************************/
	public static final String acronimoTipoConsultorioConsultaExterna = "CEXT";
	public static final String acronimoTipoConsultorioProcedimientos = "CPRO";

	/*********************************************************************************************
	 * CONSTANTES TIPO DE ESTADO
	 * *********************************************************************************************/
	public static final String acronimoEstadoProcesado = "PRO";
	public static final String acronimoEstadoPendiente = "PEN";
	public static final String acronimoEstadoAnulado = "ANU";
	public static final String acronimoEstadoFinalizado = "FIN";
	public static final String acronimoEstadoEnTramite = "TRA";
	public static final String acronimoEstadoSolicitado = "SOL";
	public static final String acronimoEstadoConfirmado = "CONF";
	public static final String acronimoEstadoAdmitido = "ADM";
	public static final String acronimoEstadoAceptado = "ACE";
	public static final String acronimoEstadoNegado = "NEG";
	public static final String acronimoEstadoCancelado = "CAN";
	public static final String acronimoEstadoEntregado = "ENT";
	public static final String acronimoEstadoAbierto = "ABI";
	public static final String acronimoEstadoCerrado = "CER";
	public static final String acronimoEstadoEnviado = "ENV";
	public static final String acronimoEstadoIncompletoGarantias = "IGAR";
	public static final String acronimoEstadoPendientePorVerificar = "PPV";
	public static final String acronimoEstadoRechazado = "REC";
	public static final String acronimoEstadoOcupado = "OCU";
	public static final String acronimoEstadoActivo = "ACT";
	public static final String acronimoEstadoGenerado = "GEN";
	public static final String acronimoEstadoAprobado = "APR";
	public static final String acronimoEstadoFacturado = "FCO";
	public static final String acronimoEstadoRequerido = "RQD";
	public static final String acronimoEstadoNoRequerido = "NRQD";
	public static final String acronimoEstadoNoAplica = "NAPL";
	public static final String acronimoEstadoRegistrado = "REGI";
	public static final String acronimoEstadoAuditado = "AUDI";
	public static final String acronimoEstadoCondonado = "CON";
	public static final String acronimoEstadoInactivo = "INAC";
	public static final String acronimoEstadoEnProceso = "ENP";
	public static final String acronimoEstadoInactivoPlanTratamiento = "INA";
	public static final String acronimoCancelado = "CAN";
	public static final String acronimoEstadoRecibido = "RBD";

	/*********************************************************************************************
	 * CONSTANTES FORMAS DE PAGO
	 * *********************************************************************************************/
	public static final String acronimoFormaPagoCheque = "CHEQU";
	public static final String acronimoFormaPagoEfectivo = "EFECT";

	/*********************************************************************************************
	 * CONSTANTES TIPOS DE DISTRIBUCION DE COSTOS PARA PAQUETES
	 * *********************************************************************************************/
	public static final String acronimoTipoDistribucionCostoProporcional = "PROPO";
	public static final String acronimoTipoDistribucionCostoUnica = "UNICA";
	public static final String acronimoMulti = "MULTI";

	/*********************************************************************************************
	 * CONSTANTES TIPO EVENTO
	 * *********************************************************************************************/
	public static final String acronimoAccidenteTransito = "ACTE";
	public static final String acronimoEventoCatastrofico = "ECTE";
	public static final String acronimoAccidenteTrabajo = "ATTE";

	/*********************************************************************************************
	 * CONSTANTES REGISTRO ACCIDENTES TRANSITO
	 * *********************************************************************************************/
	public static final String acronimoEstrellarseConVehiculo = "AECV";
	public static final String acronimoVolcarseDelVehiculo = "AVDV";
	public static final String acronimoCaerseDelVehiculo = "ACDV";
	public static final String acronimoManiobrarBruscamente = "AMB";
	public static final String acronimoEstrellarseConObjetoFijo = "AEOF";
	public static final String acronimoAtropelladoVehiculo = "AAPV";
	public static final String acronimoGolpeadoObjeto = "AGPO";
	public static final String acronimoExpulsadoVehiculo = "AEPV";
	public static final String acronimoRemision = "REM";
	public static final String acronimoOrdenServicio = "OSE";
	public static final String acronimoRemisionInterconsulta = "ITC";
	public static final String acronimoTransferenciaTecnologia = "TTE";
	public static final String acronimoVehiculo = "AVEH";
	public static final String acronimoMotocicleta = "AMOT";
	public static final String acronimoConductorVehiculo = "CVEH";
	public static final String acronimoConductorMotocicleta = "CMOT";
	public static final String acronimoPeaton = "APEA";
	public static final String acronimoCiclista = "ACIC";
	public static final String acronimoAseguradoFantasma = "FAN";
	public static final String acronimoPolizaVigente = "PVIG";
	public static final String acronimoPolizaFalsa = "PFAL";
	public static final String acronimoPolizaVencida = "PVEN";
	public static final String acronimoConductor = "ACOND";
	public static final String acronimoVehiculoFuga = "VFU";

	/*********************************************************************************************
	 * CONSTANTES TIPOS DE TRANSPORTE
	 * *********************************************************************************************/
	public static final String acronimoTipoTransporteAmbulancia = "TAMBU";
	public static final String acronimoTipoTransporteServicioPublico = "TSEPU";
	public static final String acronimoTipoTransporteParticular = "TPART";
	public static final String acronimoTipoTransporteOtros = "TOTRO";
	public static final String acronimoTipoTransporteAmbulanciaBasica = "TAMBB";
	public static final String acronimoTipoTransporteAmbulanciaMedicalizada = "TAMBM";

	/*********************************************************************************************
	 * CONSTANTES INTEGRIDAD DOMINIO TIPO ENVENTO CATASTROFICO
	 * *********************************************************************************************/
	public static final String acronimoTipoEventoCatastroficoNatural = "ENAT";
	public static final String acronimoTipoEventoCatastroficoTecnologico = "ETEC";
	public static final String acronimoTipoEventoCatastroficoTerrorista = "ETER";

	/*********************************************************************************************
	 * CONSTANTES INTEGRIDAD DOMINIO CONDICIï¿½N AFILIACIï¿½N AL SGSSS
	 * *********************************************************************************************/
	public static final String acronimoAfiliadoSgsss = "AFIL";
	public static final String acronimoNoAfiliadoSgsss = "NAFI";

	/*********************************************************************************************
	 * CONSTANTES TIPOS DE DISTRIBUCION DE COSTOS PARA PAQUETES
	 * *********************************************************************************************/
	public static final String acronimoTipoReporteResumido = "RESCON";
	public static final String acronimoTipoReporteDetalladoCuenta = "DETC";
	public static final String acronimoTipoReporteDetalladoFactura = "DETFAC";

	/*********************************************************************************************
	 * CONSTANTES INTEGRIDAD DOMINIO
	 * *********************************************************************************************/
	public static final String acronimoInterna = "INT";
	public static final String acronimoExterna = "EXT";
	public static final String acronimoIgual = "IGUAL";
	public static final String acronimoDiferente = "DIFE";
	public static final String acronimoIncremento = "INCRE";
	public static final String acronimoDecremento = "DECRE";
	public static final String acronimoPrimeraVez = "PRIVE";
	public static final String acronimoControl = "CTRL";
	public static final String acronimoTodos = "TDOS";
	public static final String acronimoInconsistencia = "INCON";
	public static final String acronimoTotal = "TTAL";
	public static final String acronimoSubTotal = "SUBTAL";

	/*********************************************************************************************
	 * CONSTANTES TIPOS DE ARCHIVO
	 * *********************************************************************************************/

	public static final String acronimoPdf = "PDF";
	public static final String acronimoArchivoPlano = "TXT";

	/*********************************************************************************************
	 * CONSTANTES TIPOS USUARIOS
	 * *********************************************************************************************/
	public static final String acronimoTipoUsuarioAmbulatorio = "TUAMBU";
	public static final String acronimoTipoUsuarioUrgencias = "TUURGE";
	public static final String acronimoTipoUsuarioHospitalizacion = "TUHOSP";

	/*********************************************************************************************
	 * CONSTANTES TIPOS Atencion
	 * *********************************************************************************************/
	public static final String acronimoTipoAtencionElectiva = "ELE";
	public static final String acronimoTipoAtencionElectivaPrioritaria = "ELP";
	public static final String acronimoTipoAtencionUrgente = "URG";
	public static final String acronimoTipoAtencionGeneral = "GENE";

	/*********************************************************************************************
	 * CONSTANTES TIPOS RA RN
	 * *********************************************************************************************/
	public static final String acronimoRA = "RA";
	public static final String acronimoRN = "RN";

	/*********************************************************************************************
	 * CONSTANTES Contrarreferencia
	 * *********************************************************************************************/
	public static final String acronimoContrarreferencia = "CTF";

	/*********************************************************************************************
	 * TIPOS BASE EXCEPCION
	 * *********************************************************************************************/
	public static final String acronimoPrecioUltimaCompra = "PULCOM";
	public static final String acronimoPrecioBaseVenta = "PBAVEN";
	public static final String acronimoValorFijo = "VALFIJ";
	public static final String acronimoTarifa = "TARIFA";
	public static final String acronimoCostoPromedio = "COSPRO";
	public static final String acronimoPrecioCompraMasAlta = "PCOMAL";
	public static final String acronimoTarifario = "TARFIO";
	public static final String acronimoConvenio = "CONVEN";
	public static final String acronimoCosto = "COSTO";
	public static final String acronimoRangoTiempo = "RTIEM";

	/*********************************************************************************************
	 * CONSTANTES Naturaleza Paciente
	 * *********************************************************************************************/
	public static final String acronimoIndigena = "INDIG";
	public static final String acronimoIvaSocial = "IVASO";
	public static final String acronimoMenorEnProteccion = "MENPR";
	public static final String acronimoIndigente = "INDEG";
	public static final String acronimoDesplazado = "DESPL";

	/*********************************************************************************************
	 * CONSTANTES TIPOS DE REPORTE PARA FACTURACION
	 * *********************************************************************************************/
	public static final String acronimoTipoReporteRealizado = "REPREA";
	public static final String acronimoTipoReporteFacturado = "REPFAC";

	/*********************************************************************************************
	 * CONSTANTES TIPO DE DOCUMENTO CONTABLE
	 * *********************************************************************************************/
	public static final String acronimoTipoDocumentoPagare = "PAGA";
	public static final String acronimoTipoDocumentoCheque = "CHEQ";
	public static final String acronimoTipoDocumentoLetra = "LETR";
	public static final String acronimoTipoDocumentoVaucher = "VAUC";

	/*********************************************************************************************
	 * CONSTANTES TIPO DEUDOR
	 * *********************************************************************************************/
	public static final String acronimoPaciente = "PACI";
	public static final String acronimoResponsablePaci = "RPACI";
	public static final String acronimoDeudor = "DEU";
	public static final String acronimoCoDeudor = "CDEU";

	public static final String acronimoInstitucion = "INSTI";

	/*********************************************************************************************
	 * CONSTANTES TIPO DISTRIBUCION
	 * *********************************************************************************************/
	public static final String acronimoTipoDistribucionValor = "VALOR";
	public static final String acronimoTipoDistribucionCantidad = "CANT";
	public static final String acronimoTipoDistribucionPorcentual = "PORC";
	public static final String acronimoTipodistribucionMonto = "MONT";
	public static final String acronimoTipodistribucionValor = "VALOR";

	/*********************************************************************************************
	 * CONSTANTES TIPO TRABAJADOR
	 * *********************************************************************************************/
	public static final String acronimoTipoTrabajadorEmpleado = "EMPL";
	public static final String acronimoTipoTrabajadorIndependiente = "INDE";

	public static final String acronimoIngreso = "ING";
	public static final String acronimoEgreso = "EGR";

	public static final String acronimoInternet = "INTE";
	public static final String acronimoTelefonica = "TELE";
	public static final String acronimoFax = "FAX";

	public static final String acronimoOcasionalmente = "OCA";

	/*********************************************************************************************
	 * METODOS DE AJUSTES
	 * *********************************************************************************************/
	public static final String acronimoAjusteCentena = "AJCE";
	public static final String acronimoAjusteDecena = "AJDE";
	public static final String acronimoAjusteUnidad = "AJUN";
	public static final String acronimoSinAjuste = "SIAJ";

	public static final String acronimoManual = "MANU";
	public static final String acronimoAutomatica = "AUTO";

	public static final String acronimoTipoDespachoConsignacion = "CONSI";
	public static final String acronimoTipoDespachoCompraProveedor = "PROVE";

	public static final String acronimoTipoDevolucionConsignacion = "DEVCON";
	public static final String acronimoTipoDevolucionCompraProveedor = "DEVPRO";

	/*********************************************************************************************
	 * CONSTANTES TIPO DE CONCEPTO PAGOS
	 * *********************************************************************************************/

	public static final String acronimoMayorValor = "MAYV";
	public static final String acronimoMenorValor = "MENV";
	public static final String acronimoReingreso = "REIN";

	/*************************************************************************************************
	 * CONSTANTES TIPO TIEMPO BASE
	 *************************************************************************************************/
	public static final String acronimoDuracionUsoSala = "DUS";
	public static final String acronimoDuracionCirugia = "DC";

	/*************************************************************************************************
	 * CONSTANTES TIPO ESPECIALISTA
	 *************************************************************************************************/
	public static final String acronimoTipoEspecialistaIgual = "IGU";
	public static final String acronimoTipoEspecialistaDiferente = "DIFE";

	/*************************************************************************************************
	 * CONSTANTES INDICATIVO DE CARGO
	 *************************************************************************************************/
	public static final String acronimoIndicativoCargoCirugia = "CX";
	public static final String acronimoIndicativoCargoCirugiaCargoDirecto = "CXCTO";
	public static final String acronimoIndicativoCargoNoCruento = "NCTO";
	public static final String acronimoIndicativoCargoNoCruentoCargoDirecto = "NCTCTO";
	
	

	/*************************************************************************************************
	 * CONSTANTES CLIENTE/SERVIDOR
	 *************************************************************************************************/
	public static final String acronimoCliente = "CLI";
	public static final String acronimoServidor = "SER";

	/*******************************************************************************************************
	 * CONSTANTES PARA MANEJAR LOS CAMPOS DE PARAMETROS ENTIDADES SUBCONTRATADAS
	 * *****************************************************************************************************/
	public static String acronimoMontoCobroXViaIngreso = "MCXVI";
	public static String acronimoOrigenAdminisionXViaIngreso = "OAXVI";
	public static String acronimoTipoPacienteXViaIngreso = "TPXVI";
	public static String acronimoAreaIngresoPacientesXViaIngreso = "AIPXVI";
	public static String acronimoValidarRequeridoNumeroAutorizacionIngreso = "VRNAI";
	public static String acronimoFarmaciaCargosDirectosArticulos = "FCDA";
	public static String acronimoCodigoUtilizadoArticulos = "CUA";
	public static String acronimoProfecionalResponde = "PQR";
	public static String acronimoTipoTarifaCargosDirectos = "TTCD";
	public static String acronimoAfectaInventariosCargosInventarios = "AICI";
	public static String acronimoMedicoAdmisionesEgresoHospitalizacion = "MAEH";
	public static String acronimoMedicoAdmisionesEgresoUrgencias = "MAEU";
	public static String acronimoEspecialidadResponde = "ESPRES";

	public static String acronimoTerapiaGrupal = "TERGR";
	public static String acronimoValorArchivoPlano = "VAPL";
	public static String acronimoCalcularAutomaticamente = "CALA";

	/*******************************************************************************************************
	 * CONSTANTES PARA MANEJAR LOS TIPO DE CODIGO ARTICULO
	 * *****************************************************************************************************/
	public static String acronimoAxioma = "AXM";
	public static String acronimoInterfaz = "INZ";
	public static String acronimoAmbos = "AMBOS";

	/*******************************************************************************************************
	 * CONSTANTES PARA MANEJAR LOS TIPOS DE INFORME
	 * *****************************************************************************************************/
	public static String acronimoConsumoPacienteArticulo = "CPAI";
	public static String acronimoTotalesConsumoArticulo = "TCDA";

	/*******************************************************************************************************
	 * CONSTANTES PARA MANEJAR LOS TIPOS DE REPORTE
	 * *****************************************************************************************************/
	public static String acronimoDetalladoTipoTransaccion = "DTT";
	public static String acronimoDetalladoProveedorTransaccion = "DPTT";
	public static String acronimoTotalizadoAlmacenTransaccion = "TATT";
	public static String acronimoEntradasSalidasTotalizadasAlmacen = "ESTATT";
	public static String acronimoEntradasSalidasArticulo = "ESA";

	/*******************************************************************************************************
	 * CONSTANTES PARA MANEJAR LOS TIPOS DE INTERFAZ EN SISTEMA UNO
	 * *****************************************************************************************************/
	public static String acronimoTipoInterfazTodos = "TODO";
	public static String acronimoTipoInterfazFacturas = "FAC";
	public static String acronimoTipoInterfazRecibosCaja = "RECA";
	
	/*******************************************************************************************************
	 * CONSTANTES PARA MANEJAR EL TIPO DE DOCUMENTO EN INTERFAZ - REPORTES
	 * *****************************************************************************************************/
	public static String acronimoFacturasPaciente = "FACPAC";
	public static String acronimoFacturasVarias = "FACVAR";
	public static String acronimoRecivoCaja = "RECAJA";

	/*******************************************************************************************************
	 * CONSTANTES PARA MANEJAR LOS TIPOS DE DESCRIPCIONES EN LA HOJA QX
	 * *****************************************************************************************************/
	public static String acronimoTipoInformacionQuirurgica = "INFOQX";
	public static String acronimoTipoPatologia = "PATO";
	public static String acronimoTipoObservaciones = "OBS";
	public static String acronimoTipoNotasAclaratorias = "NOTA";
	public static String acronimoTipoPerfusion = "PERF";
	public static String acronimoTipoHallazgos = "HALLA";

	/*******************************************************************************************************
	 * CONSTANTES PARA MANEJAR LOS TIPOS DE HERIDAS
	 * *****************************************************************************************************/
	public static String acronimoTipoHeridaLimpia = "LIMP";
	public static String acronimoTipoHeridaSucia = "SUC";
	public static String acronimoTipoHeridaLimpiaContaminada = "LIMCON";
	public static String acronimoTipoHeridaContaminada = "CONT";

	/*******************************************************************************************************
	 * CONSTANTES PARA MANEJAR TIPO CONSECUTIVO FACTURAS VARIAS.
	 * *****************************************************************************************************/
	public static String acronimoTipoConsecutivoUnicoFacturaFacturasVarias = "CFFV";
	public static String acronimoTipoConsecutivoPropiFacturasVarias = "CPFV";

	public static String acronimoTipoDeudorEmpresa = "DEUEMP";
	public static String acronimoTipoDeudorOtros = "DEUOTR";

	public static String acronimoEstadoFacturaGenerada = "GEN";
	public static String acronimoEstadoFacturaAprobada = "APR";

	/*******************************************************************************************************
	 * CONSTANTES PARA MANEJAR TIPO DE AJUSTE
	 * *****************************************************************************************************/

	public static String acronimoCredito = "C";
	public static String acronimoDebito = "D";

	/*******************************************************************************************************
	 * CONSTANTES PARA POSITIVO / NEGATIVO
	 * *****************************************************************************************************/
	public static String acronimoPositivo = "+";
	public static String acronimoNegativo = "-";

	/*******************************************************************************************************
	 * CONSTANTES PARA OBSERVACIONES VALORACIONES
	 * *****************************************************************************************************/
	public static String acronimoMotivoConsulta = "MOTCO";
	public static String acronimoEnfermedadActual = "ENFAC";
	public static String acronimoPlanDiagnosticoTerapeutico = "PLDIA";
	public static String acronimoComentariosGenerales = "COMGE";
	public static String acronimoPronostico = "PRONO";
	public static String acronimoComentariosAdicionalesHC = "COMAD";
	public static String acronimoConceptoConsulta = "CONCO";

	/*******************************************************************************************************
	 * CONSTANTES PARA MOTIVOS DE SATISFACCION E INSATISFACCION
	 * *****************************************************************************************************/
	public static String acronimoSatisfaccion = "SATI";
	public static String acronimoInsatisfaccion = "INSA";

	/************************************************************************************
	 * CONSTANTES FORMATO IMPRESION TIPO DE SALIDA (CARTERA Y FACTURACION)
	 ************************************************************************************/
	public static String acronimoTipoSalidaImpresion = "IMPR";
	public static String acronimoTipoSalidaArchivo = "ARPL";
	public static String acronimoValorLetrasFacturaConvenio = "CONV";
	public static String acronimoValorLetrasFacturaCargo = "CARG";
	public static String acronimoFactura = "FACT";

	/************************************************************************************
	 * CONSTANTES FORMATO TIPO REPORTE ANEXO 633 (CAMBIOS EN FACTURACION)
	 ************************************************************************************/
	public static String acronimoTipoReporteConvenioPaciente = "COPA";
	public static String acronimoTipoReporteConvenio = "CONV";
	public static String acronimoTipoReporteFacturadoRadicado = "FARA";

	/************************************************************************************
	 * CONSTANTES TIPO DE REPORTE EN CARTERA
	 ************************************************************************************/
	public static String acronimoReporteResumidoXTipoConvenio = "RXTC";
	public static String acronimoReporteResumidoXConvenio = "RXC";
	public static String acronimoReporteDetalladoXFactura = "DXF";

	/************************************************************************************
	 * CONSTANTES ESTADOS JUSTIFICACION NO POS
	 ************************************************************************************/
	public static String acronimoJustificado = "JUSTI";
	public static String acronimoRadicado = "RADIC";
	public static String acronimoAutorizado = "AUTOR";
	public static String acronimoNoAautorizado = "NOAUT";
	public static String acronimoAplazado = "APLAZ";
	public static String acronimoAplazadoRadicado = "APLRA";
	public static String acronimoConciliado = "CONCI";

	/************************************************************************************
	 * CONSTANTES TIPO DE EVENTO
	 ************************************************************************************/
	public static String acronimoTipoEventoAsistencial = "ASIS";
	public static String acronimoTipoEventoAdministrativo = "ADMI";

	/************************************************************************************
	 * CONSTANTES CIERRE INGRESO
	 ************************************************************************************/
	public static String acronimoCierreIngreso = "CIERRE";
	public static String acronimoAperturaIngreso = "APERTU";

	public static String acronimoCampo = "CAMPO";
	public static String acronimoSeccion = "SECCIO";

	/************************************************************************************
	 * CONSTANTES PACIENTES HOSPITALIZADOS
	 ************************************************************************************/
	public static String acronimoIndicativoConEgreso = "CONEGR";
	public static String acronimoIndicativoSinEgreso = "SINEGR";

	/************************************************************************************
	 * CONSTANTES TRANSPLANTE
	 ************************************************************************************/
	public static String acronimoIndicativoDonante = "DONANT";
	public static String acronimoIndicativoReceptor = "RECEPT";

	/************************************************************************************
	 * CONSTANTES OBSERVACIONES PEDIATRIA
	 ************************************************************************************/
	public static final String acronimoValNutricionalMenor2Anios = "NUT<2";
	public static final String acronimoValNutricionalMayor2Anios = "NUT>2";
	public static final String acronimoSueniosHabitos = "SUHAB";
	public static final String acronimoTipoAlimentacion = "TAAH";
	public static final String acronimoObservacionesDesarrollo = "OBDES";

	/************************************************************************************
	 * CONSTANTES PARAMETRICAS CIRUGIAS
	 ************************************************************************************/
	public static final String acronimoPorEspecialidad = "ESPE";
	public static final String acronimoPorCxMayor = "CXMAY";

	/************************************************************************************
	 * CONSTANTES UBICACION LOGOS
	 ************************************************************************************/
	public static final String acronimoUbicacionDerecha = "DER";
	public static final String acronimoUbicacionIzquierda = "IZQ";

	/************************************************************************************
	 * VALORES POR TIPO DE REPORTES ESTADISTICOS
	 ************************************************************************************/
	public static final String acronimoTipoRangoEdad = "EDAD";
	public static final String acronimoTipoRangoTiempo = "TIEM";
	public static final String acronimoUnidadMedidaDias = "DIAS";
	public static final String acronimoUnidadMedidaMinutos = "MINU";
	public static final String acronimoPosicionFila = "FILA";
	public static final String acronimoPosicionColumna = "COLU";

	/************************************************************************************
	 * CONSTANTES INTEGRIDAD DOMINIO GLOSAS
	 ************************************************************************************/
	public static final String acronimoTipoGlosaGlosa = "GLO";
	public static final String acronimoTipoGlosaDevolucion = "DEV";
	public static final String acronimoTipoGlosaRespuesta = "RES";
	public static final String acronimoEstadoGlosaConfirmada = "CONF";
	public static final String acronimoEstadoGlosaAnulada = "ANUL";
	public static final String acronimoEstadoGlosaAprobada = "APRO";
	public static final String acronimoEstadoGlosaAuditada = "AUDI";
	public static final String acronimoEstadoGlosaRegistrada = "REGI";
	public static final String acronimoEstadoGlosaRespondida = "RESP";
	public static final String acronimoEstadoGlosaConciliada = "CONC";
	
	/************************************************************************************
	 * CONSTANTES INTEGRIDAD DOMINIO RESPUESTA GLOSAS
	 ************************************************************************************/
	public static final String acronimoEstadoRespuestaGlosaRegistrada = "REGI";
	public static final String acronimoEstadoRespuestaGlosaAnulada = "ANUL";
	public static final String acronimoEstadoRespuestaGlosaAprobada = "APRO";
	public static final String acronimoEstadoRespuestaGlosaRadicada = "RADIC";

	/************************************************************************************
	 * CONSTANTES INTEGRIDAD INDICATIVOS GLOSAS
	 ************************************************************************************/
	public static final String acronimoIndicativoGlosa = "GL";
	public static final String acronimoIndicativoPreglosa = "PG";
	
	/************************************************************************************
	 * ESTADOS INDEPENDIENTES PARA MANEJO DE AUTORIZACIONES ANULADAS Y DENEGADAS
	 ************************************************************************************/
	public static final String acronimoEstadoAutorizacionAnulada = "ANA";
	public static final String acronimoEstadoAutorizacionDenegada = "DEN";

	/*************************************************************************************
	 * TIPO DE REPORTE DE REFERENCIA INTERNA
	 *************************************************************************************/
	public static final String acronimoPacientesReferidosAOtraInstitucion = "PROI";
	public static final String acronimoPacientesReferidosParaExamenes = "PRPE";

	/*************************************************************************************
	 * MODALIDAD TERAPIA HEMODIALISIS
	 *************************************************************************************/
	public static final String acronimoHemodialisis = "HEMO";
	public static final String acronimoCAPD = "CAPD";
	public static final String acronimoAPD = "APD";

	/*************************************************************************************
	 * TIPO REPORTE INVENTARIOS
	 *************************************************************************************/
	public static final String acronimoTipoReporteDetalladoXAlmacen = "RDXA";
	public static final String acronimoTipoReporteDetalladoXTransaccion = "RDXT";
	public static final String acronimoTipoReporteDetalladoXClaseInventario = "RDXCI";

	/*************************************************************************************
	 * TIPO REPORTE FACTURACION
	 *************************************************************************************/

	public static final String acronimoTipoReporteInformacionGeneralTarifa = "RIG";
	public static final String acronimoTipoReporteAnalisisCosto = "RAC";

	/*************************************************************************************
	 * TIPO REPORTE - CONSTANTES DETALLADAS POR PROVEEDOR
	 *************************************************************************************/

	public static final String acronimoTipoReporteDetalladoAlmacenXProveedor = "RDAXP";
	public static final String acronimoTipoReporteDetalladoPacienteXProveedor = "RDPXP";

	/*************************************************************************************
	 * GLOSAS - CONSTANTES USUARIOS GLOSAS CONVENIO
	 *************************************************************************************/

	public static final String acronimoTipoUsuarioGlosa = "GLOSA";
	public static final String acronimoTipoUsuarioAuditor = "AUDIT";

	/*************************************************************************************
	 * GLOSAS - CONSTANTES FORMATO IMPRESION DE RESPUESTA DE GLOSA
	 *************************************************************************************/

	public static final String acronimoFormatoImpresionResEstandar = "Estï¿½ndar";
	public static final String acronimoFormatoImpresionResSuba = "Hospital Suba";

	/*************************************************************************************
	 * RESPUESTA A GLOSA
	 *************************************************************************************/
	public static final String acronimoRespGlosaPagoParcial = "PPARCI";
	public static final String acronimoRespGlosaTotal = "GLOSAT";

	/*************************************************************************************
	 * TIPOS Rï¿½GIMEN PARAMETRIZADOS EN EVENTOS CATASTRï¿½FICOS
	 *************************************************************************************/
	public static final String acronimoTipoRegimenContributivo = "CONTR";
	public static final String acronimoTipoRegimenSubsidiado = "SUBSI";
	public static final String acronimoTipoRegimenExcepcion = "EXCEP";

	/*************************************************************************************
	 * ZONAS DE TRANSPORTE
	 *************************************************************************************/
	public static final String acronimoZonaUrbana = "U";
	public static final String acronimoZonaRural = "R";
	
	/*************************************************************************************
	 * ACTUALIZACION AUTORIZACIONES MEDICAS POR RANGO
	 *************************************************************************************/
	public static final String acronimoTodas = "TOD";
	public static final String acronimoPendientePorAutorizar = "PXA";
	
	/*************************************************************************************
	 * TIPOS DE FECUENCIA
	 *************************************************************************************/
	public static final String acronimoMinutos = "Minutos";
	public static final String acronimoHoras = "Horas";
	public static final String acronimoHoras2 = "HORAS";
	public static final String acronimoDias = "Días";
	
	public static final  String acronimoServicio="SERV";
    public static final  String acronimoMedicamento="MEDI";
    public static final  String acronimoInsumo="INSU";
	
	/*************************************************************************************
	 * TIPOS DE ERRORES 
	 *************************************************************************************/
	
	public static final String acronimoNoConexion = "NOCX";
	public static final String acronimoSinIntegridad = "SINT";
	
	/*****************************************************************************************
	 * INDICADORES DE VARIABLES INCORRECTAS
	 *****************************************************************************************/
     public static final  String acronimoPrimerNombre= "PN";
     public static final  String acronimoSegundoNombre= "SN";
     public static final  String acronimoPrimerApellido= "AP";
     public static final  String acronimoSegundoApellido= "SA";
     public static final  String acronimoTipoDocIdentificacion= "TD";
     public static final  String acronimoNumDocIdentificacion= "ND";
     public static final  String acronimoFechaNacimiento= "FN";
     
     /*****************************************************************************************
 	 * CONSTANTES MEDIOS DE ENVIO
 	 *****************************************************************************************/
     public static final  String acronimoEmail="MAIL";
     public static final  String acronimoIntercambioElectDatos="EDI";
    
     
     /************************************************************************************
 	 * CONSTANTES FORMATO AUTORIZACIONES
 	 ************************************************************************************/
 	public static String acronimoFormatoEstandar = "FAE";
 	public static String acronimoAnexo3Res003047 = "FAR";
 	public static String acronimoSolicitud = "SOLIC";
 	public static String acronimoAdmision = "ADMIS";
 	public static String acronimoEstancia = "ESTAN";
    
    /************************************************************************************
 	 * CONSTANTES TIPO USUARIO A REPORTAR EN SOLICITUDES DE AUTORIZACION 
 	 ************************************************************************************/
 	public static String acronimoPersonaqueSolicista = "PQS";
 	public static String acronimoUsuarioqueTramitaSolicitud = "UQS";
 	
 	/************************************************************************************
 	 * CONSTANTES DE IMPRESION DE LA RSPUESTA Y SOLICITUD DE AUTORIZACION 
 	 ************************************************************************************/
 	public static String acronimoImpresionRespAuto = "IRA";
 	public static String acronimoImpresionSolAuto = "ISA";
 	
 	/************************************************************************************
 	 * ACRONIMOS DE ENTIDADES SUBCONTRATADAS
 	 ************************************************************************************/
 	public static String acronimoArticulo = "ART";
 	
 	/************************************************************************************
 	 * ACRONIMOS TIPO DE MOVIMIENTO INTERFAZ
 	 ************************************************************************************/
 	public static String acronimoTipoMovVentasHonoraInte = "VTAHON";
 	public static String acronimoTipoMovRecaudos = "RECAU";
 	public static String acronimoTipoMovAjusteReclasi = "AJUREC";
 	public static String acronimoTipoMovServiciEntidaExter = "HONENT";
 	public static String acronimoTipoMovCartera = "CARTE"; 	
 	
 	/*******************************************************************************
 	 * ACRONIMO TIPO PROCESO INTERFAZ
 	 *******************************************************************************/
 	public static String acronimoTipoProcesoDesmarcar="DSM";
 	public static String acronimoTipoProcesoGeneracionInterfaz="INZ";
 	
 	/*******************************************************************************
 	 * ACRONIMO TIPO DE DESCUENTO - AUTORIZACION DESCUENTOS ODONTOLOGICOS A PARTICULARES
 	 *******************************************************************************/
 	public static String acronimoNegado="NEG";
 	public static String acronimoPorConfirmar="XCONF";
 	public static String acronimoPorCompletar="XCOMP";
 	
 	public static String acronimoDocHonorariosFacturasPacientes = "DHFP";
 	public static String acronimoDocServiciosAutorizacionEntidadesSub = "DSAE";
 	public static String acronimoDocDespachoMedEntidadesSubcontratadas = "DDME";
 	public static String acronimoDocFacturasPacientes = "DFP";
 	public static String acronimoDocFacturasVarias = "DFV";
 	public static String acronimoDocCuentasCobroCapitacion = "DCCC";
 	
 	public static String acronimoConceptoRetencionIntegralClaseDoc = "CRICD";
 	public static String acronimoConceptoAutoretencionIntegralClaseDoc = "CAICD";
 	

 	public static String acronimoPorValorPresupuesto= "XVPRE";
 	public static String acronimoPorAtencion= "XATEN";
 	
 	/*******************************************************************************
 	 * ACRONIMO TIPO DE CONVENIO - TIPOS ATENCION ODONTOLOGICO CONVENIOS
 	 *******************************************************************************/
 	public static String acronimoTipoAtencionConvenioOdontologico="ATEN_ODON_ESP";
 	public static String acronimoTipoAtencionConvenioMedicoGeneral="ATEN_MEDI_GEN";
	public static String acronimoTipoVentaTarjetaodontologica="VTO";	
 	/*******************************************************************************
 	 * ACRONIMO CARTERA PACIENTE FORMATO DOCUMENTOS GARANTÍA - PAGARÉ
 	 *******************************************************************************/
 	public static String acronimoFormatoDocGarantiaShaio = "FORSHA";
 	
 	/******************************************************************************
 	 * ACRONIMOS PARA EL TIPO DE ATENCIÓN EN PLANTILLAS
 	 ******************************************************************************/
 	public static String acronimoTipoAtencionValoracionInicial="VALINI";
 	public static String acronimoTipoAtencionValoracionPrimeraVezXEspecialista="VALPVE";
 	public static String acronimoTipoAtencionPrioritariaUrgencias="APU";
 	public static String acronimoTipoAtencionTratamiento = "TRAT";
 	public static String acronimoTipoAtencionRegistroHistoria = "REGH";
 	
 	
 	/*******************************************************************************
 	 * ACRONIMO HISTORIA CLINICA - TIPO ESPECIALIDADES
 	 *******************************************************************************/
 	public static String acronimoTipoEspecialidadMedica = "MEDI";
 	public static String acronimoTipoEspecialidadOdontologica = "ODON";
	
 	/*****************************************************************************
 	 * ACRONIMOS ODONTOLOGIA 
 	 *****************************************************************************/
 	public static String acronimoTipoElementGraficoImagen="IMG";
 	public static String acronimoTipoElementGraficoTrama="TRAM";
	public static String acronimoOdontogramaTratamiento="ODOTRA";
 	public static String acronimoOdontogramaDiagnostico="ODODIA";
 	public static String acronimoLocalizacion="LOC";
 	public static String acronimoSuspendidoTemporalmente = "SUT";
 	public static String acronimoContratado="COT";
 	public static String acronimoPrecontratado="PCT";
 	public static String acronimoContratarPrecontratado="COTPCT";
 	public static String acronimoNecesario = "NEC";
 	public static String acronimoEstetico = "EST";
 	public static String acronimoInactivo = "INA";
 	public static String acronimoTerminado = "TER";
 	public static String acronimoRealizadoInterno = "RI";
 	public static String acronimoRealizadoExterno = "RE";
 	public static String acronimoExcluido = "EXC";
 	public static String acronimoInclusion = "INC";
 	public static String acronimoGarantia = "GAR";
 	public static String acronimoPorAutorizar = "PAU";
 	public static String acronimoInicial = "INI" ;
 	public static String acronimoPresupuesto = "PRESU";
 	public static String acronimoSinPresupuesto = "SPRE";
 	public static String acronimoAdulto = "ADUL";
 	public static String acronimoNino = "NINO";

 	public static String acronimoEntre = "ENTRE";
 	public static String acronimoMayorQue = "MAY";
 	public static String acronimoMenorQue = "MEN";
 	public static String acronimoIgualA = "IGUAL";
 	
 	/*****************************************************************************
 	 * ACRONIMOS HISTORIA CLINICA - TIPO FUNCIONALIDAD
 	 *****************************************************************************/
 	public static String acronimoTipoFuncionalidadReservar="RESER"; // Ahora se llama datos básicos
 	public static String acronimoTipoFuncionalidadAsignar ="ASIGN";
 	
 	public static String acronimoAplicaABoca="BOC";
 	public static String acronimoAplicaADiente="DIE";
 	public static String acronimoAplicaASuperficie="SUP";
 	
 	public static String acronimoAplicaABocaBD="Boca";
 	public static String acronimoAplicaADienteBD="Diente";
 	public static String acronimoAplicaASuperficieBD="Superficie";
 	
 	/******************************************************************************
 	 * ACRONIMOS TIPO PERIODO
 	 * ***************************************************************************/
 	public static String acronimoTipoPeriodoAnual="ANU";
 	public static String acronimoTipoPeriodoMensual="MEN";
 	
 	 	/******************************************************************************
 	 * ACRONIMOS CITAS ODONTOLOGICAS
 	 * ***************************************************************************/
 	public static String acronimoPrioritaria= "PRIO";
 	public static String acronimoControlCitaOdon= "CTROL";
 	public static String acronimoRevaloracion= "REVAL";
 	public static String acronimoReservado= "RESE";
 	public static String acronimoProgramado= "PROG";
 	public static String acronimoReprogramado= "REPR";
 	public static String acronimoAsignado= "ASIG";
 	public static String acronimoTratamiento= "TRAT";
 	public static String acronimoNoAsistio= "NOAS";
 	public static String acronimoNoAtencion= "NOAT";
 	public static String acronimoEnProceso= "ENP";
 	public static String acronimoAreprogramar= "AREP";
	public static String acronimoAtendida= "ATE";
	public static String acronimoCancelada= "CANC";
	public static String acronimoTipoCitaOdonValoracionInicial = "VALINI";
 	
 
 	/******************************************************************************
 	 * secciones plan de tratamiento odon
 	 * ***************************************************************************/
 	public static String acronimoDetalle="DET";
 	public static String acronimoBoca="BOCA";
 	public static String acronimoDiente="DIENTE";
 	public static String acronimoCita="CITA";
 	
 	/**
 	 * Tipos de movimiento en la interfaz 1E
 	 */
 	public static String acronimoVentas="VEN";
 	public static String acronimoHonorarios="HON";
 	
 	/*****************************************************************************
 	 * ACRONIMOS HISTORIA CLINICA - COMPONENETE ANTECEDENTES ODONTOLOGICOS
 	 *****************************************************************************/
 	public static String acronimoMostrarProgramas ="PRGM";
 	public static String acronimoMostrarServicios ="SERV";
 	
 	/*****************************************************************************
 	 * ACRONIMOS HISTORIA CLINICA - COMPONENETE INDICE PLACA
 	 *****************************************************************************/
 	public static String acronimoPlaca ="PLA";
 	public static String acronimoAusente ="AUS";
 	
 	/*****************************************************************************
 	 * ACRONIMOS TIPO DE COBERTURA - COBERTURAS
 	 *****************************************************************************/
 	public static String acronimoTipoCoberturaGeneral="GENE";
 	public static String acronimoTipoCoberturaOdontologico="ODON";
 	
 	public static final String acronimoXDefinir="XDEFI";
 	public static final String acronimoContratado1="CTDO";
 	
 	/******************************************************************************
 	 * ACRONIMOS ESTADOS SOLICITUD DESCUENTO ODONTOLOGICO
 	 *****************************************************************************/
 	public static final String acronimoEstadoSolicitudDctoXDefinir="XDEFI";
 	public static final String acronimoEstadoSolicitudDctoPendienteXAutorizar="PXA";
 	public static final String acronimoEstadoSolicitudDctoAutorizado="AUTOR";
 	public static final String acronimoEstadoSolicitudDctoNoAutorizado="NOAUT";
 	public static final String acronimoEstadoSolicitudDctoAnulado="ANUL";
 	public static final String acronimoEstadoSolicitudDctoContratado="CTDO";
 	
 	
 	public static final String funcionalidadAperturaCaja="APECAJ";
 	public static final String funcionalidadCierreCaja="CIECAJ";
 	public static final String funcionalidadCancelacionAgendaOdontologica="CANAGO";
 	
 	public static final String acronimoEspecialidadProfesional= "ESPPRO";
 	public static final String acronimoPool = "POOL";
 	
 	/******************************************************************************
 	 * ACRONIMOS TIPOS MOTIVOS CITA
 	 *****************************************************************************/
 	
 	public static final String acronimoMotivoNoconfirma="NOCO"; 
 	public static final String acronimoMotivoLlamada="LLAM";
 	
 	
 	
 	//Anexo 992
 	/**
 	 * ACRONIMOS TIPOS DE FIRMA VALORES POR DEFECTO
 	 */
 	public static final String acronimoTipoFirmaValoresPorDefectoCapitacion="CAPI";
 	public static final String acronimoTipoFirmaValoresPorDefectoCartera="CART";
 	
 	public static final String acronimoReprogramar="REPRO";
 	public static final String acronimoCancelar="CANCE";
 	public static final String acronimoConfirmar="CONFI";
 	public static final String acronimoCambiarServicio="CAMSER";
 	public static final String acronimoConfirmarSolicitudCambiarServicio="CONSOL"; 	
 	public static final String acronimoAnularSolicitudCambiarServicio="ANUSOL";
 	public static final String acronimoCupoExtra = "CUPO_EXTRA";
 	
 	/**ESTADOS DEL PRESUPUESTO**/
 	public static final String acronimoContratadoContratado= "CCOT";
 	public static final String acronimoContratadoCancelado= "CCAN";
 	public static final String acronimoContratadoSuspendidoTemporalmente= "CSUT";
 	public static final String acronimoContratadoTerminado= "CTER";
	public static final String acronimoPresupuestoContratado = "C";

 	
 	public static final String acronimoAuditoria= "AUDITO";
 	
 	/******************************************************************************
 	 * ACRONIMOS FORMATOS IMPRESION RECIBO DE CAJA
 	 *****************************************************************************/
 	public static final String acronimoFormatoImpresionRCPOS="IMPOS";
 	public static final String acronimoFormatoImpresionRCCarta="IMCAR";
 	
 	/******************************************************************************
 	 * ACRONIMOS TIPO TIPOS IDENTIFICACION
 	 *****************************************************************************/
 	public static final String acronimoTipoTipoIdentificacionPersona="TIPER";
 	public static final String acronimoTipoTipoIdentificacionTercero="TITER";
 	public static final String acronimoTipoTipoIdentificacionAmbos="TIAMB";
 	
 	/******************************************************************************
 	 * ACRONIMOS TIPO BENEFICIARIO
 	 *****************************************************************************/
 	public static String acronimoTipoBeneficiarioPersona = "PER";
	public static String acronimoTipoBeneficiarioEmpresa = "EMP";
	public static String acronimoTipoBeneficiarioFamiliar = "AFAM";
	
	/******************************************************************************
 	 * ACRONIMOS TIPO DE INGRESO
 	 *****************************************************************************/
	public static String acronimoTipoIngresoGeneral="GENE";
	public static String acronimoTipoIngresoOdontologico="ODON";
	
	public static String acronimoEstadoCitaOdontoAtendida = "ATE";
	
	
	/************************************************************************************
	 * ACRONIMOS ACCIONES HISTORICAS
	 ************************************************************************************/
	public static String acronimoAccionHistoricaInsertar 	= "INS";
	public static String acronimoAccionHistoricaModificar 	= "MDF";
	public static String acronimoAccionHistoricaEliminar 	= "ELM";
	public static String acronimoAccionNoDefinida		 	= "NDF";
	public static String acronimoAccionProrrogar		 	= "PRR";
	public static String acronimoAccionEliminarTemporal	 	= "ELT";
		
	
	/************************************************************************************
	 * ACRONIMOS ESTADOS EGRESOS DE CAJA
	 ************************************************************************************/
	public static String acronimoEstadoEgresoCajaAutorizado 	= "EGAUTO";
	public static String acronimoEstadoEgresoCajaPendiente		= "EGPEND";
	
	
	/************************************************************************************
	 * ACRONIMOS ACTIVIDADES DE TIPOS DE USUARIO
	 ************************************************************************************/
	public static String acronimoActividadTipoUsuarioAutoDescuentos 	= "AUDES";
	public static String acronimoActividadTipoUsuarioGenSolEgreso		= "GSEGR";
	public static String acronimoActividadTipoUsuarioAutoSolEgreso		= "ASEGR";


	/************************************************************************************
	 * ACRONIMOS ESTADOS SOLICITUD TRASLADO DE CAJA
	 ************************************************************************************/
	public static String acronimoEstadoAperturaDeCajaAbierto =	"CAJABI";
	public static String acronimoEstadoAperturaDeCajaCerrado =  "CAJCER";
	
	public static String acronimoEstadoSolicitudTrasladoCajaAceptado 	= "STCACE";
	public static String acronimoEstadoSolicitudTrasladoCajaSolicitado 	= "STCSOL";
	
	
	
	/************************************************************************************
	 * ACRONIMOS ESTADOS ENVIO DE E-MAIL AUTOMATICO
	 ************************************************************************************/
	public static String acronimoEstadoEnvioCompleto 	= "EEC";
	public static String acronimoEstadoEnvioIncompleto 	= "EEI";
	
	/************************************************************************************
	 * ACRONIMOS TIPO DE DIFERENCIA FALTANTE O SOBRANTE
	 ************************************************************************************/
	public static String acronimoDiferenciaFaltante 	= "DF";
	public static String acronimoDiferenciaSobrante 	= "DS";
	
	/************************************************************************************
	 * ACRONIMOS DE INDICATIVOS TRANSPORTADORA DE VALORES
	 ************************************************************************************/
	public static String acronimoEstadoEntregaTransportadoraValores = "TRASL";
	
	/**
	 * 
	 */
	public static String acronimoTipoMotivoCambioServicioAnular ="ANULAR";
	

	/**
	 * 
	 */
	public static String acronimoTipoMotivoCambioServicioAdicionar ="ADICIO";
	
	/**
	 * 
	 */
	public static String acronimoTipoMotivoCambioServicioEliminar ="ELIMIN";
	
	
	public static String acronimoSoloPacienteConPlanTratamientoTerminado="SPPTER";
	
	public static String acronimoCualquierTipoPaciente="CTIPAC"; 
	
	
	
	/************************************************************************************
	 * ACRONIMOS DE INDICATIVOS DEL ESTADO DE SOBRANTE FALTANTE 
	 ************************************************************************************/
	public static String acronimoEstadoFaltanteSobranteGenerado	= "GEN";
	
	public static String acronimoEstadoFaltanteSobranteConciliado = "CONC";	
	
	/************************************************************************************
	 * ACRONIMOS DE INDICATIVOS DEL ESTADO DE LA VALIDACION EN BD DE CONVENIO INGRESO PACIENTE
	 ************************************************************************************/
	public static String acronimoEstadoValBDConvenioPacExitoso	= "EXTO";
	public static String acronimoEstadoValBDConvenioPacNoExitoso= "NEXT";
	
	/**
	 * 
	 */
	public static String acronimoTipoPacienteManejaMontos = "TPMM";
	public static String acronimoTipoPacienteNoManejaMontos = "TPNMM";
	
	
	/************************************************************************************
	 * ACRONIMOS PARA LOS TIPOS DE DETALLE DE UN MONTO DE COBRO 
	 ************************************************************************************/
	public static String acronimoTipoDetalleMontoCobroDET = "MONDET";
	public static String acronimoTipoDetalleMontoCobroGEN= "MONGEN";
	
		
	/************************************************************************************
	 * ACRONIMOS PARA LOS TIPOS DEL VALOR DE LOS BONOS DEL PACIENTE
	 ************************************************************************************/
	public static String acronimoTipoValorBonoDescuento = "DESCU";
	public static String acronimoTipoValorBonoValor 	= "VALOR";
	
	
	/************************************************************************************
	 * ACRONIMOS PARA LOS TIPOS DE AUTORIZACION PARA UN NIVEL DE AUTORIZACION 
	 ************************************************************************************/
	public static String acronimoTipoAutorizacionManual = "NIVMAN";
	public static String acronimoTipoAutorizacionAuto= "NIVAUT";	
	
	
	/************************************************************************************
	 * ACRONIMOS PARA LOS TIPOS DE TARIFA DE CONTRATOS_ENTIDADES_SUBCONTRATADAS 
	 ************************************************************************************/
	public static String acronimoTipoTarifaPropia="TP";
	public static String acronimoTipoTarifaConvenioPaciente="TCP";
	
	
	/******************************************************************************************************
	 * ACRONIMOS PARA LOS TIPOS DE FORMATOS DE IMPRESION 
	 ******************************************************************************************************/
	public static String acronimoFormatoImpresionEstandar="FOREST";
	public static String acronimoFormatoImpresionVersalles="FORVER";
	public static String acronimoFormatoImpresionSonria="FORSON";
	public static String acronimoFormatoImpresionPOS="FORPOS";	
	

	
	/**
	 * 
	 */
	public static String acronimoCarta ="CARTA";
	
	/**
	 * 
	 */
	public static String acronimoMediaCarta ="MCARTA";
	
	/******************************************************************************************************
	 * ACRONIMOS UTILIZADOS EN LA VALIDACION DE AUTORIZACIONES POBLACION CAPITADA
	 ******************************************************************************************************/
	public static String acronimoFuncionalidadListadoOrdenesPorCentroCosto="FLOCC";
	public static String acronimoFuncionalidadListadoOrdenesPorEntidadSubcontratada="FLOES";
	public static String acronimoFuncionalidadAutorizacionServiciosMedicamentosInsumosIngresoEstancia="FASMIE";
	
	/******************************************************************************************************
	 * ACRONIMOS UTILIZADOS PARA EL MANEJO DE INCONSISTENCIAS EN EL PROCESO DE RIPS ENTIDADES SUBCONTRATADAS PARA ARCHIVOS
	 ******************************************************************************************************/
	public static String acronimoNombreArchivoNoValido="NAN";
	public static String acronimoFormatoArchivoNoValido="FAN";
	public static String acronimoNumeroRemisionNoValido="NRN";
	public static String acronimoSumatoriasValoresAFADNoIguales="SNI";
	
	/******************************************************************************************************
	 * ACRONIMOS UTILIZADOS PARA EL MANEJO DE INCONSISTENCIAS EN EL PROCESO DE RIPS ENTIDADES SUBCONTRATADAS PARA CAMPOS/REGISTROS
	 ******************************************************************************************************/
	public static String acronimoCamposSeparacionComasErroneo="CSE";
	public static String acronimoCampoNoValido="CNV";
	public static String acronimoCodigoMinsaludNoParametrizado="MNP";
	public static String acronimoAutorizacionNoEncontrada="ANE";
	public static String acronimoAutorizacionProcesadaConAnterioridad="APA";
	public static String acronimoValoresAutorizacionNoValidos="VAN";
	public static String acronimoCampoNumericoNoValido="CNN";
	public static String acronimoCampoDecimalNoValido="CDN";
	public static String acronimoLongitudCampoNoValida="LNV";
	public static String acronimoFormatoFechaNoValido="FFN";
	public static String acronimoFormatoHoraNoValido="FHN";
	public static String acronimoCampoEsRequerido="CRQ";
	public static String acronimoCampoNoAlineado="CNA";
	public static String acronimoValoresFacturasADConACAPATAMNoIguales="VFN";
	public static String acronimoNumeroFacturaNoExisteEnAF="FNA";
	public static String acronimoUsuarioNoEncontrado="UNE";
	public static String acronimoInconsistenciaDatosBasicos="TIDBP";
	public static String acronimoInconsistenciaGeneral="TIGRL";
	public static String acronimoInconsistenciaDatosIguales="TIPDI";
	public static String acronimoNumeroCamposInvalido="NCI";
	public static String acronimoTipoNumIDNoCorresponde="TNINC";
	public static String acronimoNombApellNoCorresponde="NANC";
	public static String acronimoSexoInvalido="SEIN";
	public static String acronimoContratoNoCorresConvenio="CNCC";
	public static String acronimoConvenioNoCorresponde="CVNC";
	public static String acronimoContratoNoCorresponde="CTNC";
	public static String acronimoClasificacionSENoCorresponde="CSENC";
	public static String acronimoExcepcionMontoNoCorresponde="EXMNC";
	public static String acronimoTodosDatosObligatorios="TDSO";
	public static String acronimoCarguePrevioMismoPeriodo="CPMP";
	
	/*****CAMBIOS POR FURIPS-FURPRO*****/
	
	public static String acronimoFURIPS="FURIPS";
	public static String acronimoFURPRO="FURPRO";	
	
	public static String acronimoRespuestaGlosaPagoParcia="PAGPAR";
	public static String acronimoRespuestaGlosaGlosaTotal="GLOSTO";
	
	
	/******************************************************************************************************
	 * ACRONIMOS PARA LA BUSQUEDA DE AUTORIZACIONES POBLACIÓN CAPITADA   
	 ******************************************************************************************************/
	public static String acronimoTemporal="TEMP";
	
	

	/******************************************************************************************************
	 * ACRONIMOS PARA INDICAR SI EL REGISTRO ES GUARDADO CON O SIN AUTORIZACIÓN D EINGRESO ESTANCIA
	 ******************************************************************************************************/
	public static String acronimoRegistroSinAutorizacionIngresoEstancia = "RSAIE";
	public static String acronimoRegistroConAutorizacionIngresoEstancia = "RCAIE";
	
	
	/******************************************************************************************************
	 * ACRONIMOS PARA LOS PROCESOS DE PRESUPUESTO CAPITADO
	 ******************************************************************************************************/
	
	public static String acronimoTipoProcesoOrden="ATPO";
	public static String acronimoTipoProcesoAutorizacion="ATPA";
	public static String acronimoTipoProcesoCargoCuenta="ATPC";
	public static String acronimoTipoProcesoFacturacion="ATPF"; 
	
	public static String acronimoTipoTotalNivelAtencionArticulo="ATTNAA"; 
	public static String acronimoTipoTotalNivelAtencionServicio="ATTNAS"; 
	public static String acronimoTipoTotalArticulo="ATTA"; 
	public static String acronimoTipoTotalServicio="ATTS"; 
	public static String acronimoTipoTotalGrupoServicio="ATTGS"; 
	public static String acronimoTipoTotalClaseInventario="ATTCI"; 
	public static String acronimoTipoTotalNivelAtencionGrupoServicio="ATTNAG"; 
	public static String acronimoTipoTotalNivelAtencionClaseInventario="ATTNAC";
	
	public static String acronimoTipoInconsistenciaNoDefinidoNivelAtencion="NDN";
	public static String acronimoTipoInconsistenciaNoDefinidoParametro="NDP";
	public static String acronimoTipoInconsistenciaNoDefinidaTarifa="NDT";
	
	public static String acronimoTipoConsultaNivelAtencion="CNAT";
	public static String acronimoTipoConsultaGrupoServicioClaseInventario="CGCI";
	public static String acronimoTipoConsultaDetallado="CDET";

	/******************************************************************************************************
	 * ACRONIMOS PARA REPORTES DE CONSOLIDADO DE CIERRE
	 ******************************************************************************************************/
	public static String acronimoConsolidadoCentroAtencion="CCA";
	public static String acronimoConsolidadoCajaCajero="CCC";
	
			/******************************************************************************************************
	 * ACRONIMOS PARA REPORTES DE SEMAFORIZACION CONSULTAS Y REPORTES
	 ******************************************************************************************************/
	public static String acronimoTipoReportePresupuesto="RPRE";
	public static String acronimoTipoReporteEstadisticasOrdenadores="FEOR";
	
	
	/******************************************************************************************************
	 * ACRONIMOS PARA PROCESOS AUTOMATICOS
	 ******************************************************************************************************/
	public static String acronimoProcesoExitoso="PEX";
	public static String acronimoProcesoAutomatico="AUT";
	
	
	/******************************************************************************************************
	 * TIPOS DE ORDENES
	******************************************************************************************************/
	public static String acronimoTipoOrdenMedica				="TORMED";
	public static String acronimoTipoOrdenambulatoria			="TORAMB";
	public static String acronimoTipoOrdenPeticionCx			="TORPET";

	/******************************************************************************************************
	 * ACRONIMO NOTAS CREDITO Y DEBITO NATURALEZA NOTAS PACIENTE 
	 ******************************************************************************************************/
	public static String acronimoNaturalezaNotaPacienteCreditoDebito="CYD";
	
	
	/******************************************************************************************************
	 * INDICATIVO AUTORIZACIONES ENTIDADES SUBCONTRATADAS 
	******************************************************************************************************/
	/*
	public static String acronimoIndicativoAutorizacionMediAmbuServicio  = "IAOS";
	public static String acronimoIndicativoAutorizacionMediAmbuArticulo  = "IAOA";
	public static String acronimoIndicativoAutorizacionIEstanciaServicio = "IAES";
	public static String acronimoIndicativoAutorizacionIEstanciaArticulo = "IAEA";
	*/
	
	
	/******************************************************************************************************
	 * NOMBRES TIPOS DE AUTORIZACIONES CAPITACION
	 ******************************************************************************************************/
	public static final String TIPO_AUTORIZACION_SERVICIOS = "Servicios";
	public static final String TIPO_AUTORIZACION_ARTICULOS = "Medicamentos e Insumos";
	public static final String TIPO_AUTORIZACION_INGRESO_ESTANCIA = "Ingreso/Estancia";
	public static final String TIPO_AUTORIZACION_SERVICIOS_ING_EST = "Servicios Ing/Est";
	public static final String TIPO_AUTORIZACION_ARTICULOS_ING_EST = "Medicamentos e Insumos Ing/Est";
	
	/******************************************************************************************************
	 * TIPO DE AUTORIZACION /ORDEN AMB/SOLICITUD/PETICIONES QX/ING ESTANCIA/CARGOS DIRECTOS
	 ******************************************************************************************************/
	public static final int TIPO_AUTORIZACION_SOLICITUD= 1;
	public static final int TIPO_AUTORIZACION_PETICION= 2;
	public static final int TIPO_AUTORIZACION_ORDEN_AMB= 3;
	public static final int TIPO_AUTORIZACION_ING_EST= 4;
	public static final int TIPO_AUTORIZACION_CARGOS_DIRECTOS= 5;
	
	
	/******************************************************************************************************
	 * ACRONIMOS PARA LOS TIPOS DE REPORTE EN LA IMPRESIÓN DE TESORERIA
	******************************************************************************************************/
	
	public static String acronimoTipoReporteImpArqueoCajaResumido="IACR";
	public static String acronimoTipoReporteImpArqueoCajaDetallado="IACD";
	public static String acronimoTipoReporteImpCierreTurnoResumido="ICTR";
	public static String acronimoTipoReporteImpCierreTurnoDetallado="ICTD";
	public static String acronimoTipoReporteImpEntregaCajaMayPrincResumido="IECR";
	public static String acronimoTipoReporteImpEntregaCajaMayPrincDetallado="IECD";
	
	public static String acronimoTipoReporteAmbos="TRTA";
	
	/*******************************************************************************************************
	 * CONSTANTES PARA MANEJAR TIPO DE SERVICIO
	 * *****************************************************************************************************/

	public static String acronimoTipoServicioConsulta = "C";
	
	
}