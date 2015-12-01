package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;

/**
 * 
 * @author wilson
 *
 */
public class DtoFURIPS1 implements Serializable
{
	/**
	 * 
	 */
	private boolean esAccidenteTransito;

	/**
	 * 
	 */
	private boolean esEventoCatastrofico;

	/**
	 * 
	 */
	private boolean esDesplazado;

	/**
	 * 
	 */
	private String idCuenta;

	/**
	 * 
	 */
	private String nombreViaIngreso;

	/**
	 * 
	 */
	private String idFactura;

	/**
	 * 
	 */
	private String idCuentaCobro;

	/**
	 * 
	 */
	private boolean esInstitucionPublica;
	
	public String[] nombresCampos={"", 
			/*1*/"Número de Radicado Anterior", 
			/*2*/"RG Respuesta a Glosa.", 
			/*3*/"Número de factura o cuenta de cobro", 
			/*4*/"Número consecutivo de la Reclamación", 
			/*5*/"Código de habilitación del Prestador de Servicios de Salud ",
			/*6*/"Primer Apellido de la Victima",
			/*7*/"Segundo Apellido de la Victima",
			/*8*/"Primer Nombre de la Víctima",
			/*9*/"Segundo Nombre  de la Victima",
			/*10*/"Tipo de  documento de identidad de la Víctima",
			/*11*/"Número de  documento de identidad de la Victima",
			/*12*/"Fecha de Nacimiento de la Víctima",
			/*13*/"Sexo de la Víctima",
			/*14*/"Direccion de residencia de la Víctima",
			/*15*/"Código del Departamento de Residencia de la Victima",
			/*16*/"Código del Municipio de residencia  de la Víctima",
			/*17*/"Teléfono de la Víctima",
			/*18*/"Condición del Accidentado",
			/*19*/"Naturaleza del Evento",
			/*20*/"Descripción de otro evento",
			/*21*/"Dirección de ocurrencia del evento",
			/*22*/"Fecha Ocurrencia del evento.",
			/*23*/"Hora de Ocurrencia del evento",
			/*24*/"Código del Departamento de ocurrencia del evento.",
			/*25*/"Código del Municipio de ocurrencia del evento.",
			/*26*/"Zona de ocurrencia del evento.",
			/*27*/"Estado de Aseguramiento",
			/*28*/"Marca",
			/*29*/"Placa",
			/*30*/"Tipo de Vehiculo",
			/*31*/"Código de la Aseguradora",
			/*32*/"Número de póliza SOAT",
			/*33*/"Fecha de inicio de vigencia de la Póliza",
			/*34*/"Fecha final de vigencia de la póliza.",
			/*35*/"Intervencion Autoridad",
			/*36*/"Cobro Excedente Póliza",
			/*37*/"Placa del Segundo Vehiculo Involucrado",
			/*38*/"Tipo de Documento de Identidad del Propietario del Segundo Vehiculo Involucrado",
			/*39*/"Número de documento de  identidad del propietario del segundo vehículo involucrado.",
			/*40*/"Placa del tercer vehiculo involucrado",
			/*41*/"Tipo de Documento de Identidad del Propietario del tercer Vehiculo Involucrado",
			/*42*/"Número de documento de identidad del  propietario del tercer vehículo involucrado.",
			/*43*/"Tipo de documento de identidad del propietario",
			/*44*/"Número de documento de identidad del propietario",
			/*45*/"Primper apellido del propietario o razón social en caso de empresa.",
			/*46*/"Segundo apellido del Propietario",
			/*47*/"Primer Nombre del Propietario",
			/*48*/"Segundo Nombre del Propietario",
			/*49*/"Direccion de residencia del propietario",
			/*50*/"Teléfono de residencia del Propietario",
			/*51*/"Código  del departamento de residencia del propietario",
			/*52*/"Código del municipio de residencia del propietario",
			/*53*/"Primer Apellido del conductor",
			/*54*/"Segundo Apellido del conductor",
			/*55*/"Primer nombre del conductor",
			/*56*/"Segundo Nombre del Conductor",
			/*57*/"Tipo de  documento de identidad del conductor",
			/*58*/"Número de documento  de identidad del conductor",
			/*59*/"Direccion de residencia del conductor",
			/*60*/"Código del departamento de residencia conductor",
			/*61*/"Código del municipio de residencia del conductor",
			/*62*/"Teléfono de residencia del conductor",
			/*63*/"tipo de referencia",
			/*64*/"Fecha de remisión",
			/*65*/"Hora de salida",
			/*66*/"Código de Habilitación del Prestador de Servicios de Salud",
			/*67*/"Profesional que remite",
			/*68*/"Cargo de la persona que remite",
			/*79*/"Fecha de ingreso",
			/*70*/"Hora de ingreso",
			/*71*/"Código de Habilitación del Prestador de Servicios de Salud",
			/*72*/"Profesional que recibe",
			/*73*/"Cargo de la persona que recibe",
			/*74*/"placa",
			/*75*/"Transporte de la víctima desde el sitio del evento",
			/*76*/"Transporte de la víctima hasta el fin del recorrido",
			/*77*/"Tipo de Servicio de la Ambulancia",
			/*78*/"Zona donde recoge víctima",
			/*79*/"Fecha de ingreso",
			/*80*/"Hora de ingreso",
			/*81*/"Fecha de Egreso",
			/*82*/"Hora de Egreso",
			/*83*/"Código de diagnóstico principal del ingreso",
			/*84*/"Código de diagnóstico de ingreso asociado 1",
			/*85*/"Código de diagnóstico de ingreso asociado 2",
			/*86*/"Código diagnóstico principal de egreso",
			/*87*/"Código de diagnóstico de egreso asociado 1",
			/*88*/"Código de diagnóstico de egreso asociado 2",
			/*89*/"Primer apellido del médico o profesional de la salud",
			/*90*/"Segundo apellido del  médico o profesional de la salud",
			/*91*/"Primer nombre del médico o profesional de la salud",
			/*92*/"Segundo nombre del médico o profesional de la salud",
			/*93*/"Tipo de documento de identidad del médico o profesional de la salud",
			/*94*/"Número  de documento de identidad del médico o profesional de la salud",
			/*95*/"Número de registro del médico",
			/*96*/"Total facturado por amparo de gastos medico quirúrgicos",
			/*97*/"Total reclamado por amparo de gastos médico quirúrgicos",
			/*98*/"Total facturado por amparo de gastos de transporte y movilización de la víctima",
			/*99*/"Total reclamado por amparo de gastos de transporte y movilización de la víctima",
			/*100*/"Total Folios"};
	
	
	/**
	 * campo 1
	 */
	private String numeroRadicadoAnterior;
	
	/**
	 * campo 2
	 */
	private String respuestaAGlosa;
	
	/**
	 * campo 3
	 */
	private String numeroFacturaCuentaCobro;
	
	/**
	 * campo 4
	 */
	private String numeroConsecutivoReclamacion;
	
	/**
	 * campo 5
	 */
	private String codigoHabilitacionPrestadorServicioDeSalud;
	
	/**
	 * campo 6
	 */
	private String primerApellidoVictima;
	
	/**
	 * campo 7
	 */
	private String segundoApellidoVictima;
	
	/**
	 * campo 8
	 */
	private String primerNombreVictima;
	
	/**
	 * campo 9
	 */
	private String segundoNombreVictima;
	
	/**
	 * campo 10
	 */
	private String tipoDocumentoVictima;
	
	/**
	 * campo 11
	 */
	private String numeroDocumentoVictima;
	
	/**
	 * campo 12
	 */
	private String fechaNacimientoVictima;
	
	/**
	 * campo 13
	 */
	private String sexoVictima;
	
	/**
	 * campo 14
	 */
	private String direccionResidenciaVictima;
	
	/**
	 * campo 15
	 */
	private String codigoDepartamentoResidenciaVictima;
	
	/**
	 * campo 16
	 */
	private String codigoMunicipioResidenciaVictima;
	
	/**
	 * campo 17
	 */
	private String telefonoVictima;
	
	/**
	 * campo 18
	 */
	private String codicionAccidentado;
	
	/**
	 * campo 19
	 */
	private String naturalezaEvento;
	
	/**
	 * campo 20
	 */
	private String descripcionOtroEvento;
	
	/**
	 * campo 21
	 */
	private String direccionOcurrenciaEvento;
	
	/**
	 * campo 22
	 */
	private String fechaOcurrenciaEvento;
	
	/**
	 * campo 23
	 */
	private String horaOcurrenciaEvento;
	
	/**
	 * campo 24
	 */
	private String codigoDepartamentoOcurrenciaEvento;
	
	/**
	 * campo 25
	 */
	private String codigoMunicipioOcorrenciaEvento;
	
	/**
	 * campo 26
	 */
	private String zonaOcurrenciaEvento;
	
	/**
	 * campo 27
	 */
	private String estadoAseguramiento;
	
	/**
	 * campo 28
	 */
	private String marca;
	
	/**
	 * campo 29
	 */
	private String placa;
	
	/**
	 * campo 30
	 */
	private String tipoVehiculo;
	
	/**
	 * campo 31
	 */
	private String codigoAseguradora;
	
	/**
	 * campo 32
	 */
	private String numeroPolizaSOAT;
	
	/**
	 * campo 33
	 */
	private String fechaInicioVigenciaPoliza;
	
	/**
	 * campo 34
	 */
	private String fechaFinalVigenciaPoliza;
	
	/**
	 * campo 35
	 */
	private String intervencionAutoridad;
	
	/**
	 * campo 36
	 */
	private String cobroExcedentePoliza;
	
	/**
	 * campo 37
	 */
	private String placaSegundoVehiculoInvolucrado;
	
	/**
	 * campo 38
	 */
	private String tipoDocumentoIdentidadPropietarioSegundoVehiculoInvolucrado;
	
	/**
	 * campo 39
	 */
	private String numeroDocumentoIdentidadPropietarioSegundoVehiculoInvolucrado;
	
	/**
	 * campo 40
	 */
	private String placaTercerVehiculoInvolucrado;
	
	/**
	 * campo 41
	 */
	private String tipoDocumentoIdentidadPropietarioTercerVehiculoInvolucrado;
	
	/**
	 * campo 42
	 */
	private String numeroDocumentoIdentidadPropietarioTercerVehiculoInvolucrado;
	
	


	/**
	 * campo 43
	 */
	private String tipoDocumentoIdentidadPropietario;
	
	/**
	 * campo 44
	 */
	private String numeroDocumentoIdentidadPropietario;
	
	/**
	 * campo 45
	 */
	private String primerApellidoORazonSocialPropietario;
	
	/**
	 * campo 46
	 */
	private String segundoApellidoPropietario;
	
	/**
	 * campo 47
	 */
	private String primerNombrePropietario;
	
	/**
	 * campo 48
	 */
	private String segundoNombrePropietario;
	
	/**
	 * campo 49
	 */
	private String direccionResidenciaPropietario;
	
	/**
	 * campo 50 
	 */
	private String telefonoResidenciaPropietario;
	
	/**
	 * campo 51
	 */
	private String codigoDepartamentoResidenciaPropietario;
	
	/**
	 * campo 52 
	 */
	private String codigoMunicipioResidenciaPropietario;
	
	/**
	 * campo 53 
	 */
	private String primerApellidoConductor;
	
	/**
	 * campo 54 
	 */
	private String segundoApellidoConductor;
	
	/**
	 * campo 55 
	 */
	private String primerNombreConductor;
	
	/**
	 * campo 56 
	 */
	private String segundoNombreConductor;
	
	/**
	 * campo 57 
	 */
	private String tipoDocumentoConductor;
	
	/**
	 * campo 58 
	 */
	private String numeroDocumentoConductor;
	
	/**
	 * campo 59 
	 */
	private String direccionResidenciaConductor;
	
	/**
	 * campo 60 
	 */
	private String codigoDepartamentoResidenciaConductor;
	
	/**
	 * campo 61 
	 */
	private String codigoMunicipioResidenciaConductor;
	
	/**
	 * campo 62 
	 */
	private String telefonoResidenciaConductor;
	
	/**
	 * campo 63 
	 */
	private String tipoReferencia;
	
	/**
	 * campo 64
	 */
	private String fechaReferencia;
	
	
	/**
	 * campo 65
	 */
	private String horaSalida;
	
	/**
	 * campo 66
	 */
	private String codigoHabilitacionPrestadorServiciosSaludRemitente;
	
	/**
	 * campo 67
	 */
	private String profesionaRemite;
	
	/**
	 * campo 68
	 */
	private String cargoPersonaRemite;
	
	/**
	 * campo 69 
	 */
	private String fechaIngresoRemision;
	
	/**
	 * campo 70
	 */
	private String horaIngresoRemision;
	
	/**
	 * campo 71
	 */
	private String codigoHabilitacionPrestadorServiciosSaludReceptor;
	
	/**
	 * campo 72
	 */
	private String profesionalRecibe;
	
	/**
	 * campo 73
	 */
	private String cargoPersonaRecibe;
	
	/**
	 * campo 74
	 */
	private String placaTransporte;
	
	/**
	 * campo 75
	 */
	private String transporteVictimaDesde;
	
	/**
	 * campo 76
	 */
	private String transporteVictimaHasta;
	
	/**
	 * campo 77
	 */
	private String tipoServicioAmbulancia;
	
	/**
	 * campo 78
	 */
	private String zonaDondeRecogeVictima;
	
	/**
	 * campo 79 
	 */
	private String fechaIngreso;
	
	/**
	 * campo 80
	 */
	private String horaIngreso;
	
	/**
	 * campo 81
	 */
	private String fechaEgreso;
	
	/**
	 * campo 82
	 */
	private String horaEgreso;
	
	/**
	 * campo 83
	 */
	private String codigoDiagnosticoPrincipalIngreso;
	
	/**
	 * campo 84
	 */
	private String codigoDiagnosticoIngreso1;
	
	/**
	 * campo 85
	 */
	private String codigoDiagnosticoIngreso2;
	
	/**
	 * campo 86
	 */
	private String codigoDiagnosticoPrincipalEgreso;
	
	/**
	 * campo 87
	 */
	private String codigoDiagnosticoEgreso1;
	
	/**
	 * campo 88
	 */
	private String codigoDiagnosticoEgreso2;
	
	/**
	 * campo 89
	 */
	private String primerApellidoMedico;
	
	/**
	 * campo 90
	 */
	private String segundoApellidoMedico;
	
	/**
	 * campo 91
	 */
	private String primerNombreMedico;
	
	/**
	 * campo 92
	 */
	private String segundoNombreMedico;
	
	/**
	 * campo 93 
	 */
	private String tipoDocumentoMedico;
	
	/**
	 * campo 94 
	 */
	private String numeroDocumentoMedico;
	
	/**
	 * campo 95 
	 */
	private String numeroRegistroMedico;
	
	/**
	 * campo 96
	 */
	private String totalFacturadoAmparoGastosMedicos;
	
	/**
	 * campo 97 
	 */
	private String totalReclamadoAmparoGastosMedicos;
	
	/**
	 * campo 98
	 */
	private String totalFacturadoAmparoTransporteMovilizacion;
	
	/**
	 * campo 99
	 */
	private String totalReclamadoAmparoTransporteMovilizacion;
	
	/**
	 * campo 100 
	 */
	private String totalFolios;
	


	/**
	 * constructor
	 */
	public DtoFURIPS1() 
	{
		super();
		
		this.esAccidenteTransito=false;
		this.esDesplazado=false;
		this.esEventoCatastrofico=false;
		this.idCuenta="";
		this.nombreViaIngreso="";
		this.idFactura="";
		this.idCuentaCobro="";
		this.esInstitucionPublica=false;
		this.numeroRadicadoAnterior = "";
		this.respuestaAGlosa = "";
		this.numeroFacturaCuentaCobro = "";
		this.numeroConsecutivoReclamacion = "";
		this.codigoHabilitacionPrestadorServicioDeSalud = "";
		this.primerApellidoVictima = "";
		this.segundoApellidoVictima = "";
		this.primerNombreVictima = "";
		this.segundoNombreVictima = "";
		this.tipoDocumentoVictima = "";
		this.numeroDocumentoVictima = "";
		this.fechaNacimientoVictima = "";
		this.sexoVictima = "";
		this.direccionResidenciaVictima = "";
		this.codigoDepartamentoResidenciaVictima = "";
		this.codigoMunicipioResidenciaVictima = "";
		this.telefonoVictima = "";
		this.codicionAccidentado = "";
		this.naturalezaEvento = "";
		this.descripcionOtroEvento = "";
		this.direccionOcurrenciaEvento = "";
		this.fechaOcurrenciaEvento = "";
		this.horaOcurrenciaEvento = "";
		this.codigoDepartamentoOcurrenciaEvento = "";
		this.codigoMunicipioOcorrenciaEvento = "";
		this.zonaOcurrenciaEvento = "";
		this.estadoAseguramiento = "";
		this.marca = "";
		this.placa = "";
		this.tipoVehiculo = "";
		this.codigoAseguradora = "";
		this.numeroPolizaSOAT = "";
		this.fechaInicioVigenciaPoliza = "";
		this.fechaFinalVigenciaPoliza = "";
		this.intervencionAutoridad = "";
		this.cobroExcedentePoliza = "";
		this.placaSegundoVehiculoInvolucrado = "";
		this.tipoDocumentoIdentidadPropietarioSegundoVehiculoInvolucrado = "";
		this.numeroDocumentoIdentidadPropietarioSegundoVehiculoInvolucrado = "";
		this.placaTercerVehiculoInvolucrado = "";
		this.tipoDocumentoIdentidadPropietarioTercerVehiculoInvolucrado = "";
		this.numeroDocumentoIdentidadPropietarioTercerVehiculoInvolucrado = "";
		this.tipoDocumentoIdentidadPropietario = "";
		this.numeroDocumentoIdentidadPropietario = "";
		this.primerApellidoORazonSocialPropietario = "";
		this.segundoApellidoPropietario = "";
		this.primerNombrePropietario = "";
		this.segundoNombrePropietario = "";
		this.direccionResidenciaPropietario = "";
		this.telefonoResidenciaPropietario = "";
		this.codigoDepartamentoResidenciaPropietario = "";
		this.codigoMunicipioResidenciaPropietario = "";
		this.primerApellidoConductor = "";
		this.segundoApellidoConductor = "";
		this.primerNombreConductor = "";
		this.segundoNombreConductor = "";
		this.tipoDocumentoConductor = "";
		this.numeroDocumentoConductor = "";
		this.direccionResidenciaConductor = "";
		this.codigoDepartamentoResidenciaConductor = "";
		this.codigoMunicipioResidenciaConductor = "";
		this.telefonoResidenciaConductor = "";
		this.tipoReferencia = "";
		this.fechaReferencia = "";
		this.horaSalida = "";
		this.codigoHabilitacionPrestadorServiciosSaludRemitente = "";
		this.profesionaRemite = "";
		this.cargoPersonaRemite = "";
		this.fechaIngresoRemision = "";
		this.horaIngresoRemision = "";
		this.codigoHabilitacionPrestadorServiciosSaludReceptor = "";
		this.profesionalRecibe = "";
		this.cargoPersonaRecibe = "";
		this.placaTransporte = "";
		this.transporteVictimaDesde = "";
		this.transporteVictimaHasta = "";
		this.tipoServicioAmbulancia = "";
		this.zonaDondeRecogeVictima = "";
		this.fechaIngreso = "";
		this.horaIngreso = "";
		this.fechaEgreso = "";
		this.horaEgreso = "";
		this.codigoDiagnosticoPrincipalIngreso = "";
		this.codigoDiagnosticoIngreso1 = "";
		this.codigoDiagnosticoIngreso2 = "";
		this.codigoDiagnosticoPrincipalEgreso = "";
		this.codigoDiagnosticoEgreso1 = "";
		this.codigoDiagnosticoEgreso2 = "";
		this.primerApellidoMedico = "";
		this.segundoApellidoMedico = "";
		this.primerNombreMedico = "";
		this.segundoNombreMedico = "";
		this.tipoDocumentoMedico = "";
		this.numeroDocumentoMedico = "";
		this.numeroRegistroMedico = "";
		this.totalFacturadoAmparoGastosMedicos = "";
		this.totalReclamadoAmparoGastosMedicos = "";
		this.totalFacturadoAmparoTransporteMovilizacion = "";
		this.totalReclamadoAmparoTransporteMovilizacion = "";
		this.totalFolios = "";
		
	}



	public boolean isEsAccidenteTransito() {
		return esAccidenteTransito;
	}



	public void setEsAccidenteTransito(boolean esAccidenteTransito) {
		this.esAccidenteTransito = esAccidenteTransito;
	}



	public boolean isEsEventoCatastrofico() {
		return esEventoCatastrofico;
	}



	public void setEsEventoCatastrofico(boolean esEventoCatastrofico) {
		this.esEventoCatastrofico = esEventoCatastrofico;
	}



	public boolean isEsDesplazado() {
		return esDesplazado;
	}



	public void setEsDesplazado(boolean esDesplazado) {
		this.esDesplazado = esDesplazado;
	}



	public String getIdCuenta() {
		return idCuenta;
	}



	public void setIdCuenta(String idCuenta) {
		this.idCuenta = idCuenta;
	}



	public String getNombreViaIngreso() {
		return nombreViaIngreso;
	}



	public void setNombreViaIngreso(String nombreViaIngreso) {
		this.nombreViaIngreso = nombreViaIngreso;
	}



	public String getIdFactura() {
		return idFactura;
	}



	public void setIdFactura(String idFactura) {
		this.idFactura = idFactura;
	}



	public String getIdCuentaCobro() {
		return idCuentaCobro;
	}



	public void setIdCuentaCobro(String idCuentaCobro) {
		this.idCuentaCobro = idCuentaCobro;
	}



	public boolean isEsInstitucionPublica() {
		return esInstitucionPublica;
	}



	public void setEsInstitucionPublica(boolean esInstitucionPublica) {
		this.esInstitucionPublica = esInstitucionPublica;
	}



	public String[] getNombresCampos() {
		return nombresCampos;
	}



	public void setNombresCampos(String[] nombresCampos) {
		this.nombresCampos = nombresCampos;
	}



	public String getNumeroRadicadoAnterior() {
		return numeroRadicadoAnterior;
	}



	public void setNumeroRadicadoAnterior(String numeroRadicadoAnterior) {
		this.numeroRadicadoAnterior = numeroRadicadoAnterior;
	}



	public String getRespuestaAGlosa() {
		return respuestaAGlosa;
	}



	public void setRespuestaAGlosa(String respuestaAGlosa) {
		this.respuestaAGlosa = respuestaAGlosa;
	}



	public String getNumeroFacturaCuentaCobro() {
		return numeroFacturaCuentaCobro;
	}



	public void setNumeroFacturaCuentaCobro(String numeroFacturaCuentaCobro) {
		this.numeroFacturaCuentaCobro = numeroFacturaCuentaCobro;
	}



	public String getNumeroConsecutivoReclamacion() {
		return numeroConsecutivoReclamacion;
	}



	public void setNumeroConsecutivoReclamacion(String numeroConsecutivoReclamacion) {
		this.numeroConsecutivoReclamacion = numeroConsecutivoReclamacion;
	}



	public String getCodigoHabilitacionPrestadorServicioDeSalud() {
		return codigoHabilitacionPrestadorServicioDeSalud;
	}



	public void setCodigoHabilitacionPrestadorServicioDeSalud(
			String codigoHabilitacionPrestadorServicioDeSalud) {
		this.codigoHabilitacionPrestadorServicioDeSalud = codigoHabilitacionPrestadorServicioDeSalud;
	}



	public String getPrimerApellidoVictima() {
		return primerApellidoVictima;
	}



	public void setPrimerApellidoVictima(String primerApellidoVictima) {
		this.primerApellidoVictima = primerApellidoVictima;
	}



	public String getSegundoApellidoVictima() {
		return segundoApellidoVictima;
	}



	public void setSegundoApellidoVictima(String segundoApellidoVictima) {
		this.segundoApellidoVictima = segundoApellidoVictima;
	}



	public String getPrimerNombreVictima() {
		return primerNombreVictima;
	}



	public void setPrimerNombreVictima(String primerNombreVictima) {
		this.primerNombreVictima = primerNombreVictima;
	}



	public String getSegundoNombreVictima() {
		return segundoNombreVictima;
	}



	public void setSegundoNombreVictima(String segundoNombreVictima) {
		this.segundoNombreVictima = segundoNombreVictima;
	}



	public String getTipoDocumentoVictima() {
		return tipoDocumentoVictima;
	}



	public void setTipoDocumentoVictima(String tipoDocumentoVictima) {
		this.tipoDocumentoVictima = tipoDocumentoVictima;
	}



	public String getNumeroDocumentoVictima() {
		return numeroDocumentoVictima;
	}



	public void setNumeroDocumentoVictima(String numeroDocumentoVictima) {
		this.numeroDocumentoVictima = numeroDocumentoVictima;
	}



	public String getFechaNacimientoVictima() {
		return fechaNacimientoVictima;
	}



	public void setFechaNacimientoVictima(String fechaNacimientoVictima) {
		this.fechaNacimientoVictima = fechaNacimientoVictima;
	}



	public String getSexoVictima() {
		return sexoVictima;
	}



	public void setSexoVictima(String sexoVictima) {
		this.sexoVictima = sexoVictima;
	}



	public String getDireccionResidenciaVictima() {
		return direccionResidenciaVictima;
	}



	public void setDireccionResidenciaVictima(String direccionResidenciaVictima) {
		this.direccionResidenciaVictima = direccionResidenciaVictima;
	}



	public String getCodigoDepartamentoResidenciaVictima() {
		return codigoDepartamentoResidenciaVictima;
	}



	public void setCodigoDepartamentoResidenciaVictima(
			String codigoDepartamentoResidenciaVictima) {
		this.codigoDepartamentoResidenciaVictima = codigoDepartamentoResidenciaVictima;
	}



	public String getCodigoMunicipioResidenciaVictima() {
		return codigoMunicipioResidenciaVictima;
	}



	public void setCodigoMunicipioResidenciaVictima(
			String codigoMunicipioResidenciaVictima) {
		this.codigoMunicipioResidenciaVictima = codigoMunicipioResidenciaVictima;
	}



	public String getTelefonoVictima() {
		return telefonoVictima;
	}



	public void setTelefonoVictima(String telefonoVictima) {
		this.telefonoVictima = telefonoVictima;
	}



	public String getCodicionAccidentado() {
		return codicionAccidentado;
	}



	public void setCodicionAccidentado(String codicionAccidentado) {
		this.codicionAccidentado = codicionAccidentado;
	}



	public String getNaturalezaEvento() {
		return naturalezaEvento;
	}



	public void setNaturalezaEvento(String naturalezaEvento) {
		this.naturalezaEvento = naturalezaEvento;
	}



	public String getDescripcionOtroEvento() {
		return descripcionOtroEvento;
	}



	public void setDescripcionOtroEvento(String descripcionOtroEvento) {
		this.descripcionOtroEvento = descripcionOtroEvento;
	}



	public String getDireccionOcurrenciaEvento() {
		return direccionOcurrenciaEvento;
	}



	public void setDireccionOcurrenciaEvento(String direccionOcurrenciaEvento) {
		this.direccionOcurrenciaEvento = direccionOcurrenciaEvento;
	}



	public String getFechaOcurrenciaEvento() {
		return fechaOcurrenciaEvento;
	}



	public void setFechaOcurrenciaEvento(String fechaOcurrenciaEvento) {
		this.fechaOcurrenciaEvento = fechaOcurrenciaEvento;
	}



	public String getHoraOcurrenciaEvento() {
		return horaOcurrenciaEvento;
	}



	public void setHoraOcurrenciaEvento(String horaOcurrenciaEvento) {
		this.horaOcurrenciaEvento = horaOcurrenciaEvento;
	}



	public String getCodigoDepartamentoOcurrenciaEvento() {
		return codigoDepartamentoOcurrenciaEvento;
	}



	public void setCodigoDepartamentoOcurrenciaEvento(
			String codigoDepartamentoOcurrenciaEvento) {
		this.codigoDepartamentoOcurrenciaEvento = codigoDepartamentoOcurrenciaEvento;
	}



	public String getCodigoMunicipioOcorrenciaEvento() {
		return codigoMunicipioOcorrenciaEvento;
	}



	public void setCodigoMunicipioOcorrenciaEvento(
			String codigoMunicipioOcorrenciaEvento) {
		this.codigoMunicipioOcorrenciaEvento = codigoMunicipioOcorrenciaEvento;
	}



	public String getZonaOcurrenciaEvento() {
		return zonaOcurrenciaEvento;
	}



	public void setZonaOcurrenciaEvento(String zonaOcurrenciaEvento) {
		this.zonaOcurrenciaEvento = zonaOcurrenciaEvento;
	}



	public String getEstadoAseguramiento() {
		return estadoAseguramiento;
	}



	public void setEstadoAseguramiento(String estadoAseguramiento) {
		this.estadoAseguramiento = estadoAseguramiento;
	}



	public String getMarca() {
		return marca;
	}



	public void setMarca(String marca) {
		this.marca = marca;
	}



	public String getPlaca() {
		return placa;
	}



	public void setPlaca(String placa) {
		this.placa = placa;
	}



	public String getTipoVehiculo() {
		return tipoVehiculo;
	}



	public void setTipoVehiculo(String tipoVehiculo) {
		this.tipoVehiculo = tipoVehiculo;
	}



	public String getCodigoAseguradora() {
		return codigoAseguradora;
	}



	public void setCodigoAseguradora(String codigoAseguradora) {
		this.codigoAseguradora = codigoAseguradora;
	}



	public String getNumeroPolizaSOAT() {
		return numeroPolizaSOAT;
	}



	public void setNumeroPolizaSOAT(String numeroPolizaSOAT) {
		this.numeroPolizaSOAT = numeroPolizaSOAT;
	}



	public String getFechaInicioVigenciaPoliza() {
		return fechaInicioVigenciaPoliza;
	}



	public void setFechaInicioVigenciaPoliza(String fechaInicioVigenciaPoliza) {
		this.fechaInicioVigenciaPoliza = fechaInicioVigenciaPoliza;
	}



	public String getFechaFinalVigenciaPoliza() {
		return fechaFinalVigenciaPoliza;
	}



	public void setFechaFinalVigenciaPoliza(String fechaFinalVigenciaPoliza) {
		this.fechaFinalVigenciaPoliza = fechaFinalVigenciaPoliza;
	}



	public String getIntervencionAutoridad() {
		return intervencionAutoridad;
	}



	public void setIntervencionAutoridad(String intervencionAutoridad) {
		this.intervencionAutoridad = intervencionAutoridad;
	}




	public String getPlacaSegundoVehiculoInvolucrado() {
		return placaSegundoVehiculoInvolucrado;
	}



	public void setPlacaSegundoVehiculoInvolucrado(
			String placaSegundoVehiculoInvolucrado) {
		this.placaSegundoVehiculoInvolucrado = placaSegundoVehiculoInvolucrado;
	}



	public String getTipoDocumentoIdentidadPropietarioSegundoVehiculoInvolucrado() {
		return tipoDocumentoIdentidadPropietarioSegundoVehiculoInvolucrado;
	}



	public void setTipoDocumentoIdentidadPropietarioSegundoVehiculoInvolucrado(
			String tipoDocumentoIdentidadPropietarioSegundoVehiculoInvolucrado) {
		this.tipoDocumentoIdentidadPropietarioSegundoVehiculoInvolucrado = tipoDocumentoIdentidadPropietarioSegundoVehiculoInvolucrado;
	}



	public String getNumeroDocumentoIdentidadPropietarioSegundoVehiculoInvolucrado() {
		return numeroDocumentoIdentidadPropietarioSegundoVehiculoInvolucrado;
	}



	public void setNumeroDocumentoIdentidadPropietarioSegundoVehiculoInvolucrado(
			String numeroDocumentoIdentidadPropietarioSegundoVehiculoInvolucrado) {
		this.numeroDocumentoIdentidadPropietarioSegundoVehiculoInvolucrado = numeroDocumentoIdentidadPropietarioSegundoVehiculoInvolucrado;
	}



	public String getPlacaTercerVehiculoInvolucrado() {
		return placaTercerVehiculoInvolucrado;
	}



	public void setPlacaTercerVehiculoInvolucrado(
			String placaTercerVehiculoInvolucrado) {
		this.placaTercerVehiculoInvolucrado = placaTercerVehiculoInvolucrado;
	}



	public String getTipoDocumentoIdentidadPropietarioTercerVehiculoInvolucrado() {
		return tipoDocumentoIdentidadPropietarioTercerVehiculoInvolucrado;
	}



	public void setTipoDocumentoIdentidadPropietarioTercerVehiculoInvolucrado(
			String tipoDocumentoIdentidadPropietarioTercerVehiculoInvolucrado) {
		this.tipoDocumentoIdentidadPropietarioTercerVehiculoInvolucrado = tipoDocumentoIdentidadPropietarioTercerVehiculoInvolucrado;
	}



	public String getNumeroDocumentoIdentidadPropietarioTercerVehiculoInvolucrado() {
		return numeroDocumentoIdentidadPropietarioTercerVehiculoInvolucrado;
	}



	public void setNumeroDocumentoIdentidadPropietarioTercerVehiculoInvolucrado(
			String numeroDocumentoIdentidadPropietarioTercerVehiculoInvolucrado) {
		this.numeroDocumentoIdentidadPropietarioTercerVehiculoInvolucrado = numeroDocumentoIdentidadPropietarioTercerVehiculoInvolucrado;
	}



	public String getTipoDocumentoIdentidadPropietario() {
		return tipoDocumentoIdentidadPropietario;
	}



	public void setTipoDocumentoIdentidadPropietario(
			String tipoDocumentoIdentidadPropietario) {
		this.tipoDocumentoIdentidadPropietario = tipoDocumentoIdentidadPropietario;
	}



	public String getNumeroDocumentoIdentidadPropietario() {
		return numeroDocumentoIdentidadPropietario;
	}



	public void setNumeroDocumentoIdentidadPropietario(
			String numeroDocumentoIdentidadPropietario) {
		this.numeroDocumentoIdentidadPropietario = numeroDocumentoIdentidadPropietario;
	}



	public String getPrimerApellidoORazonSocialPropietario() {
		return primerApellidoORazonSocialPropietario;
	}



	public void setPrimerApellidoORazonSocialPropietario(
			String primerApellidoORazonSocialPropietario) {
		this.primerApellidoORazonSocialPropietario = primerApellidoORazonSocialPropietario;
	}



	public String getSegundoApellidoPropietario() {
		return segundoApellidoPropietario;
	}



	public void setSegundoApellidoPropietario(String segundoApellidoPropietario) {
		this.segundoApellidoPropietario = segundoApellidoPropietario;
	}



	public String getPrimerNombrePropietario() {
		return primerNombrePropietario;
	}



	public void setPrimerNombrePropietario(String primerNombrePropietario) {
		this.primerNombrePropietario = primerNombrePropietario;
	}



	public String getSegundoNombrePropietario() {
		return segundoNombrePropietario;
	}



	public void setSegundoNombrePropietario(String segundoNombrePropietario) {
		this.segundoNombrePropietario = segundoNombrePropietario;
	}



	public String getDireccionResidenciaPropietario() {
		return direccionResidenciaPropietario;
	}



	public void setDireccionResidenciaPropietario(
			String direccionResidenciaPropietario) {
		this.direccionResidenciaPropietario = direccionResidenciaPropietario;
	}



	public String getTelefonoResidenciaPropietario() {
		return telefonoResidenciaPropietario;
	}



	public void setTelefonoResidenciaPropietario(
			String telefonoResidenciaPropietario) {
		this.telefonoResidenciaPropietario = telefonoResidenciaPropietario;
	}



	public String getCodigoDepartamentoResidenciaPropietario() {
		return codigoDepartamentoResidenciaPropietario;
	}



	public void setCodigoDepartamentoResidenciaPropietario(
			String codigoDepartamentoResidenciaPropietario) {
		this.codigoDepartamentoResidenciaPropietario = codigoDepartamentoResidenciaPropietario;
	}



	public String getCodigoMunicipioResidenciaPropietario() {
		return codigoMunicipioResidenciaPropietario;
	}



	public void setCodigoMunicipioResidenciaPropietario(
			String codigoMunicipioResidenciaPropietario) {
		this.codigoMunicipioResidenciaPropietario = codigoMunicipioResidenciaPropietario;
	}



	public String getPrimerApellidoConductor() {
		return primerApellidoConductor;
	}



	public void setPrimerApellidoConductor(String primerApellidoConductor) {
		this.primerApellidoConductor = primerApellidoConductor;
	}



	public String getSegundoApellidoConductor() {
		return segundoApellidoConductor;
	}



	public void setSegundoApellidoConductor(String segundoApellidoConductor) {
		this.segundoApellidoConductor = segundoApellidoConductor;
	}



	public String getPrimerNombreConductor() {
		return primerNombreConductor;
	}



	public void setPrimerNombreConductor(String primerNombreConductor) {
		this.primerNombreConductor = primerNombreConductor;
	}



	public String getSegundoNombreConductor() {
		return segundoNombreConductor;
	}



	public void setSegundoNombreConductor(String segundoNombreConductor) {
		this.segundoNombreConductor = segundoNombreConductor;
	}



	public String getTipoDocumentoConductor() {
		return tipoDocumentoConductor;
	}



	public void setTipoDocumentoConductor(String tipoDocumentoConductor) {
		this.tipoDocumentoConductor = tipoDocumentoConductor;
	}



	public String getNumeroDocumentoConductor() {
		return numeroDocumentoConductor;
	}



	public void setNumeroDocumentoConductor(String numeroDocumentoConductor) {
		this.numeroDocumentoConductor = numeroDocumentoConductor;
	}



	public String getDireccionResidenciaConductor() {
		return direccionResidenciaConductor;
	}



	public void setDireccionResidenciaConductor(String direccionResidenciaConductor) {
		this.direccionResidenciaConductor = direccionResidenciaConductor;
	}



	public String getCodigoDepartamentoResidenciaConductor() {
		return codigoDepartamentoResidenciaConductor;
	}



	public void setCodigoDepartamentoResidenciaConductor(
			String codigoDepartamentoResidenciaConductor) {
		this.codigoDepartamentoResidenciaConductor = codigoDepartamentoResidenciaConductor;
	}



	public String getCodigoMunicipioResidenciaConductor() {
		return codigoMunicipioResidenciaConductor;
	}



	public void setCodigoMunicipioResidenciaConductor(
			String codigoMunicipioResidenciaConductor) {
		this.codigoMunicipioResidenciaConductor = codigoMunicipioResidenciaConductor;
	}



	public String getTelefonoResidenciaConductor() {
		return telefonoResidenciaConductor;
	}



	public void setTelefonoResidenciaConductor(String telefonoResidenciaConductor) {
		this.telefonoResidenciaConductor = telefonoResidenciaConductor;
	}



	public String getTipoReferencia() {
		return tipoReferencia;
	}



	public void setTipoReferencia(String tipoReferencia) {
		this.tipoReferencia = tipoReferencia;
	}



	public String getFechaReferencia() {
		return fechaReferencia;
	}



	public void setFechaReferencia(String fechaReferencia) {
		this.fechaReferencia = fechaReferencia;
	}



	public String getHoraSalida() {
		return horaSalida;
	}



	public void setHoraSalida(String horaSalida) {
		this.horaSalida = horaSalida;
	}



	public String getCodigoHabilitacionPrestadorServiciosSaludRemitente() {
		return codigoHabilitacionPrestadorServiciosSaludRemitente;
	}



	public void setCodigoHabilitacionPrestadorServiciosSaludRemitente(
			String codigoHabilitacionPrestadorServiciosSaludRemitente) {
		this.codigoHabilitacionPrestadorServiciosSaludRemitente = codigoHabilitacionPrestadorServiciosSaludRemitente;
	}



	public String getProfesionaRemite() {
		return profesionaRemite;
	}



	public void setProfesionaRemite(String profesionaRemite) {
		this.profesionaRemite = profesionaRemite;
	}



	public String getCargoPersonaRemite() {
		return cargoPersonaRemite;
	}



	public void setCargoPersonaRemite(String cargoPersonaRemite) {
		this.cargoPersonaRemite = cargoPersonaRemite;
	}



	public String getFechaIngresoRemision() {
		return fechaIngresoRemision;
	}



	public void setFechaIngresoRemision(String fechaIngresoRemision) {
		this.fechaIngresoRemision = fechaIngresoRemision;
	}



	public String getHoraIngresoRemision() {
		return horaIngresoRemision;
	}



	public void setHoraIngresoRemision(String horaIngresoRemision) {
		this.horaIngresoRemision = horaIngresoRemision;
	}



	public String getCodigoHabilitacionPrestadorServiciosSaludReceptor() {
		return codigoHabilitacionPrestadorServiciosSaludReceptor;
	}



	public void setCodigoHabilitacionPrestadorServiciosSaludReceptor(
			String codigoHabilitacionPrestadorServiciosSaludReceptor) {
		this.codigoHabilitacionPrestadorServiciosSaludReceptor = codigoHabilitacionPrestadorServiciosSaludReceptor;
	}



	public String getProfesionalRecibe() {
		return profesionalRecibe;
	}



	public void setProfesionalRecibe(String profesionalRecibe) {
		this.profesionalRecibe = profesionalRecibe;
	}



	public String getCargoPersonaRecibe() {
		return cargoPersonaRecibe;
	}



	public void setCargoPersonaRecibe(String cargoPersonaRecibe) {
		this.cargoPersonaRecibe = cargoPersonaRecibe;
	}



	public String getPlacaTransporte() {
		return placaTransporte;
	}



	public void setPlacaTransporte(String placaTransporte) {
		this.placaTransporte = placaTransporte;
	}



	public String getTransporteVictimaDesde() {
		return transporteVictimaDesde;
	}



	public void setTransporteVictimaDesde(String transporteVictimaDesde) {
		this.transporteVictimaDesde = transporteVictimaDesde;
	}



	public String getTransporteVictimaHasta() {
		return transporteVictimaHasta;
	}



	public void setTransporteVictimaHasta(String transporteVictimaHasta) {
		this.transporteVictimaHasta = transporteVictimaHasta;
	}



	public String getTipoServicioAmbulancia() {
		return tipoServicioAmbulancia;
	}



	public void setTipoServicioAmbulancia(String tipoServicioAmbulancia) {
		this.tipoServicioAmbulancia = tipoServicioAmbulancia;
	}



	public String getZonaDondeRecogeVictima() {
		return zonaDondeRecogeVictima;
	}



	public void setZonaDondeRecogeVictima(String zonaDondeRecogeVictima) {
		this.zonaDondeRecogeVictima = zonaDondeRecogeVictima;
	}



	public String getFechaIngreso() {
		return fechaIngreso;
	}



	public void setFechaIngreso(String fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}



	public String getHoraIngreso() {
		return horaIngreso;
	}



	public void setHoraIngreso(String horaIngreso) {
		this.horaIngreso = horaIngreso;
	}



	public String getFechaEgreso() {
		return fechaEgreso;
	}



	public void setFechaEgreso(String fechaEgreso) {
		this.fechaEgreso = fechaEgreso;
	}



	public String getHoraEgreso() {
		return horaEgreso;
	}



	public void setHoraEgreso(String horaEgreso) {
		this.horaEgreso = horaEgreso;
	}



	public String getCodigoDiagnosticoPrincipalIngreso() {
		return codigoDiagnosticoPrincipalIngreso;
	}



	public void setCodigoDiagnosticoPrincipalIngreso(
			String codigoDiagnosticoPrincipalIngreso) {
		this.codigoDiagnosticoPrincipalIngreso = codigoDiagnosticoPrincipalIngreso;
	}



	public String getCodigoDiagnosticoIngreso1() {
		return codigoDiagnosticoIngreso1;
	}



	public void setCodigoDiagnosticoIngreso1(String codigoDiagnosticoIngreso1) {
		this.codigoDiagnosticoIngreso1 = codigoDiagnosticoIngreso1;
	}



	public String getCodigoDiagnosticoIngreso2() {
		return codigoDiagnosticoIngreso2;
	}



	public void setCodigoDiagnosticoIngreso2(String codigoDiagnosticoIngreso2) {
		this.codigoDiagnosticoIngreso2 = codigoDiagnosticoIngreso2;
	}



	public String getCodigoDiagnosticoPrincipalEgreso() {
		return codigoDiagnosticoPrincipalEgreso;
	}



	public void setCodigoDiagnosticoPrincipalEgreso(
			String codigoDiagnosticoPrincipalEgreso) {
		this.codigoDiagnosticoPrincipalEgreso = codigoDiagnosticoPrincipalEgreso;
	}



	public String getCodigoDiagnosticoEgreso1() {
		return codigoDiagnosticoEgreso1;
	}



	public void setCodigoDiagnosticoEgreso1(String codigoDiagnosticoEgreso1) {
		this.codigoDiagnosticoEgreso1 = codigoDiagnosticoEgreso1;
	}



	public String getCodigoDiagnosticoEgreso2() {
		return codigoDiagnosticoEgreso2;
	}



	public void setCodigoDiagnosticoEgreso2(String codigoDiagnosticoEgreso2) {
		this.codigoDiagnosticoEgreso2 = codigoDiagnosticoEgreso2;
	}



	public String getPrimerApellidoMedico() {
		return primerApellidoMedico;
	}



	public void setPrimerApellidoMedico(String primerApellidoMedico) {
		this.primerApellidoMedico = primerApellidoMedico;
	}



	public String getSegundoApellidoMedico() {
		return segundoApellidoMedico;
	}



	public void setSegundoApellidoMedico(String segundoApellidoMedico) {
		this.segundoApellidoMedico = segundoApellidoMedico;
	}



	public String getPrimerNombreMedico() {
		return primerNombreMedico;
	}



	public void setPrimerNombreMedico(String primerNombreMedico) {
		this.primerNombreMedico = primerNombreMedico;
	}



	public String getSegundoNombreMedico() {
		return segundoNombreMedico;
	}



	public void setSegundoNombreMedico(String segundoNombreMedico) {
		this.segundoNombreMedico = segundoNombreMedico;
	}



	public String getTipoDocumentoMedico() {
		return tipoDocumentoMedico;
	}



	public void setTipoDocumentoMedico(String tipoDocumentoMedico) {
		this.tipoDocumentoMedico = tipoDocumentoMedico;
	}



	public String getNumeroDocumentoMedico() {
		return numeroDocumentoMedico;
	}



	public void setNumeroDocumentoMedico(String numeroDocumentoMedico) {
		this.numeroDocumentoMedico = numeroDocumentoMedico;
	}



	public String getNumeroRegistroMedico() {
		return numeroRegistroMedico;
	}



	public void setNumeroRegistroMedico(String numeroRegistroMedico) {
		this.numeroRegistroMedico = numeroRegistroMedico;
	}



	public String getTotalFacturadoAmparoGastosMedicos() {
		return totalFacturadoAmparoGastosMedicos;
	}



	public void setTotalFacturadoAmparoGastosMedicos(
			String totalFacturadoAmparoGastosMedicos) {
		this.totalFacturadoAmparoGastosMedicos = totalFacturadoAmparoGastosMedicos;
	}



	public String getTotalReclamadoAmparoGastosMedicos() {
		return totalReclamadoAmparoGastosMedicos;
	}



	public void setTotalReclamadoAmparoGastosMedicos(
			String totalReclamadoAmparoGastosMedicos) {
		this.totalReclamadoAmparoGastosMedicos = totalReclamadoAmparoGastosMedicos;
	}



	public String getTotalFacturadoAmparoTransporteMovilizacion() {
		return totalFacturadoAmparoTransporteMovilizacion;
	}



	public void setTotalFacturadoAmparoTransporteMovilizacion(
			String totalFacturadoAmparoTransporteMovilizacion) {
		this.totalFacturadoAmparoTransporteMovilizacion = totalFacturadoAmparoTransporteMovilizacion;
	}



	public String getTotalReclamadoAmparoTransporteMovilizacion() {
		return totalReclamadoAmparoTransporteMovilizacion;
	}



	public void setTotalReclamadoAmparoTransporteMovilizacion(
			String totalReclamadoAmparoTransporteMovilizacion) {
		this.totalReclamadoAmparoTransporteMovilizacion = totalReclamadoAmparoTransporteMovilizacion;
	}



	public String getTotalFolios() {
		return totalFolios;
	}



	public void setTotalFolios(String totalFolios) {
		this.totalFolios = totalFolios;
	}



	public String getCobroExcedentePoliza() {
		return cobroExcedentePoliza;
	}



	public void setCobroExcedentePoliza(String cobroExcedentePoliza) {
		this.cobroExcedentePoliza = cobroExcedentePoliza;
	}

}
