package com.servinte.axioma.generadorReporteHistoriaClinica.comun;

public interface IConstantesReporteHistoriaClinica {

	/***************************************************************************/
	/**						FORMATOS DE REPORTES   						****/										
	/***************************************************************************/
	public static final int formatoA=1;
	public static final int formatoPlanoA=2;
	public static final int formatoB=3;
	public static final int formatoPlanoB=4;
	public static final int formatoC=5;
	public static final int formatoPlanoC=6;

	/***************************************************************************/
	/**						PARAMETROS DE REPORTES   						****/										
	/***************************************************************************/
	public static final String fecha="fecha";
	public static final String convenio="convenio";
	public static final String contrato="contrato";
	public static final String nombreInstitucion="nombreInstitucion";
	public static final String nitInstitucion="nitInstitucion";
	public static final String tipoConsulta="tipoConsulta";
	public static final String rutaLogo="rutaLogo";
	public static final String ubicacionLogo="ubicacionLogo";
	public static final String usuarioProceso="usuarioProceso";
	public static final String actividadEconomica="actividadEconomica";
	public static final String direccion="direccion";
	public static final String centroAtencion="centroAtencion";
	public static final String telefono="telefono";
	public static final String institucionlabel="InstitucionLabel";
	public static final String loginUsuarioProceso="loginUsuarioProceso";
	public static final String valorMensualContrato="valorMensualContrato";
	public static final String porcentajeGastoMensual="porcentajeGastoMensual";
	public static final String valorGastoMensual="valorGastoMensual";
	public static final String fechaProcesa="fechaProcesa";


	/***************************************************************************/
	/**	  PARAMETROS DE REPORTE TOTAL SERV ART VALORIZADO POR CONVENIO   	****/										
	/***************************************************************************/
	//public static final String totalConvenio = "Total Convenio";
	public static final String servicio = "Servicio";
	public static final String articulo = "Articulo";
	public static final String medInsumo = "Med/Ins";
	public static final String grupoServicio = "Grupo Servicio";
	public static final String claseInv = "Clase Inventario";
	public static final String nivelAten = "Nivel Atención";
	public static final String ordCapSub = "Total Servicios Artículos Valorizados por Convenio";

	/***************************************************************************/
	/**	  PARAMETROS DE REPORTE HISTORIA CLINICA						  	****/										
	/***************************************************************************/
	public static final String nombrepaciente="nombrepaciente";
	public static final String fechaNacimiento="fechaNacimiento";
	public static final String estadoCivil="estadoCivil";
	public static final String residencia="residencia";
	public static final String fechahoraingreso="fechahoraingreso";
	public static final String fechahoraEgreso="fechahoraEgreso";
	public static final String acompanantePaciente="acompanantePaciente";
	public static final String responsablePaciente="responsablePaciente";
	public static final String convenioPaciente="convenioPaciente";
	public static final String tipoNumeroID="tipoNumeroID";
	public static final String edad="edad";
	public static final String ocupacion="ocupacion";
	public static final String telefonoPaciente="telefonoPaciente";
	public static final String viaIngreso="viaIngreso";
	public static final String viaEgreso="viaEgreso";
	public static final String telParentescoUno="telParentescoUno";
	public static final String telParentescoDos="telParentescoDos";
	public static final String nombreParentescoUno="nombreParentescoUno";
	public static final String nombreparentescoDos="nombreparentescoDos";
	public static final String tipoAfiliado="tipoAfiliado";
	public static final String sexo="sexo";
	public static final String regimen="regimen";


	/**
	 *Nombre del PDF geenrado de HC 
	 */
	public static final String nombreReporteHistoriaClinica="ImpresionHistoriaClinicaHC";

	/**
	 * Constante se Bd para parametro de historia clinica por cliente 
	 */
	public static final Integer estadoParametroTipoHistoriaClinica = new Integer(0);

	/**
	 *parametro de solicitudesFactura  
	 */
	public static final String parametroSolicitudesFactura="solicitudesFactura";



	/***********************************************************************************************************/
	/**	  PARAMETROS DE REPORTE HISTORIA CLINICA	-->ImpresionResumenAtencionesAction					  	****/										
	/***********************************************************************************************************/


	public static final String  constanteValidarUrgencias="U";
	public static final String constanteValidacionValoracionHospitalizacion="H";
	public static final String constanteCodigoCuenta="cuenta";
	public static final String constanteSignosVitales="signosVitales";
	public static final String constanteValoracionesEnfermeria="valoracionesEnfermeria";
	public static final String constanteAdministracionMedicamentos="administracionMedicamentos";
	public static final String constanteConsumoInsumos="consumoInsumos";
	public static final String constanteRespuestaInterpretacionProcedimientos="respuestaInterpretacionProcedimientos";
	public static final String constanteNotasEnfermeria="notasEnfermeria";
	public static final String constanteCateteresSondas="cateteresSondas";
	public static final String constanteCuidadosEspeciales="cuidadosEspeciales";
	public static final String constanteSoporteRespiratorio="soporteRespiratorio";
	public static final String constanteControLiquidos="controLiquidos";
	public static final String constanteOrdenesAmbulatorias="ordenesAmbulatorias";
	public static final String constanteAntecedentes="antecedentes";  
	public static final String constanteCirugias="cirugias";
	public static final String constanteHojaQuirurgica="hojaQuirurgica";
	public static final String constanteHojaAnestesia="hojaAnestesia";
	public static final String constanteCodigoviaingreso="codigoviaingreso";
	public static final String constanteFechaegreso="fechaegreso";
	public static final String constanteHoraegreso="horaegreso";
	public static final String constanteHospitalizacion="Hospitalización";
	public static final String constanteAmbulatorio="Ambulatorio";
	public static final String constanteUrgencia="Urgencia";
	public static final String constanteConsultaExterna="Consulta Externa";
	public static final String constanteUrgHospHospitalizacion="Urg/Hosp Hospitalización";
	public static final String constanteUrgHospCirugia="Urg/Hosp Cirugía";
	public static final String constanteHosHosHospitalizacion="Hos/Hos Hospitalización";
	public static final String constanteUrgUrg="Urg/Urg";
	public static final String constanteViaIngresoUno="1";
	public static final String constanteViaIngresoDos="2";
	public static final String constanteViaIngresoTres="3";
	public static final String constanteViaIngresoCuatro="4";
	public static final String constanteViaIngresoDiez="10";
	public static final String constanteViaIngresoOnce="11";
	public static final String constanteViaIngresoDoce="12";
	public static final String constanteViaIngresoTrece="13";
	public static final String constanteViaIngresoCartoce="14";
	public static final String constanteViaIngresoQuince="15";
	public static final String constantePacienteActivo="pacienteActivo";
	public static final String constanteSlash="../";
	public static final String constanteSolicitudesFactura="solicitudesFactura";
	public static final String constanteValoracionInicial="valoracionInicial";
	public static final String constanteFiltroAsocioA="A";
	public static final String constanteValoracionesCE="valoracionesCE";
	public static final String constanteOrdenesMedicas="ordenesMedicas";
	public static final String constanteOrdenesMedicamentos="ordenesMedicamentos";
	public static final String constanteOrdenesProcedimientos="ordenesProcedimientos";
	public static final String constanteConsultasPYP="consultasPYP";
	public static final String constanteHojaAdministracionMedicamentos="hojaAdministracionMedicamentos";
	public static final String constanteEventosAdversos="eventosAdversos";
	public static final String constanteResumenParcialHistoriaClinica="resumenParcialHistoriaClinica";
	public static final String constanteIngreso="ingreso";
	public static final String constanteValoracionesCuidadoEspecial="valoracionesCuidadoEspecial";
	public static final String constanteRespuestaInterpretacionInterconsulta="respuestaInterpretacionInterconsulta";
	public static final String constanteEvoluciones="evoluciones";
	public static final String constanteResultadosLaboratorios="resultadosLaboratorios";
	public static final String constanteHojaNeurologica="hojaNeurologica";
	public static final String constanteEscalaGlasgow="escalaGlasgow";
	public static final String constanteEstadoGenerarReporte="generarReporte";
	public static final String constanteNotasGeneralesCirugia="notasGeneralesCirugia";
	public static final String constanteNotasRecuperacion="notasRecuperacion";
	public static final String constanteNotasAclaratoriasSeccion="notasAclaratorias";



	/*******************************************************************************************************************************************/
	/**	  PARAMETROS DE REPORTE HISTORIA CLINICA TITULOS Y NOMBRES DE CAMPOS 	-->GeneradorSubReporteHojaQuirurgica 					  	****/										
	/*******************************************************************************************************************************************/
	public static final String constanteTituloHojaQuirurgica="HOJA QUIRÚRGICA";
	public static final String constanteFechaHoraCirugia="Fecha/Hora Cirugía:";
	public static final String constanteDuracionFinalCirugia="Duración final cirugía:";  
	public static final String constanteFechaHoraIngresoSala="Fecha/Hora Ingreso Sala: ";
	public static final String constanteFechaHoraSalidaSala="Fecha/Hora Salida Sala: ";
	public static final String constanteSalaDeSalidaPaciente="Sala de Salida Paciente:";
	public static final String constanteDiagnosticosPreOperatorio="Diagnósticos Pre-Operatorio";
	public static final String constanteDxPrincipal="Dx. Principal: "; 
	public static final String constanteDxRelacionado="Dx. Relacionado";
	public static final String constantePatologia="Patología";
	public static final String constanteObservacionesGenerales="Observaciones Generales";
	public static final String constanteNotasAclaratorias="Notas Aclaratorias";
	public static final String constanteDescripcionActoQuirurgico="Descripción Acto Quirúrgico";
	public static final String constanteServicios="SERVICIOS";
	public static final String constanteEspecialidadQueInterviene="Especialidad que Interviene";
	public static final String constantePos=" POS";
	public static final String constanteNoPos=" NO POS";
	public static final String constanteServicio="Servicio";
	public static final String constanteDescripcionQuirurgica="Descripción Quirúrgica";
	public static final String constanteDiagnosticosPostOperatorio="Diagnósticos Post-Operatorio";
	public static final String constanteDxComplicacion="Dx. Complicación:";
	public static final String constanteGenerarReporteNotasAclratorias="generarReporteNotasAclaratorias";

	/*******************************************************************************************************************************************/
	/**	  PARAMETROS DE REPORTE HISTORIA CLINICA TITULOS Y NOMBRES DE CAMPOS 	-->GeneradorSubResumenParcialHC				  	            ****/										
	/*******************************************************************************************************************************************/
	public static final String constanteTituloResumenParcial="Resumen Parcial de Historia Clínica ";
	public static final String constanteMensajeFecha="Fecha:";
	public static final String constanteMensajeHora="Hora:";


	/*********************************************************************
	 * 
	 * MENSAJE FINAL DEL REPORTE DE HC 
	 * 
	 ******************************************************************** */
	public static final String constanteMensajeFinalHC="\"Los prestadores de servicios de salud pueden utilizar medios físicos o técnicos como" +
			" computadoras y medios magneto-ópticos permitiendo la identificación del personal responsable de los datos consignados, mediante " +
			"códigos, indicadores u otros medios que reemplacen la firma y sello de las historias en medios físicos, de forma que se " +
			" establezca con exactitud quien realizo los registro, la hora y fecha del registro.\"";


	public static final String PAGINA_JSP_HISTORIA_CLINICA="impresionResumenAtenciones/impresionResumenAtenciones.jsp";
}
