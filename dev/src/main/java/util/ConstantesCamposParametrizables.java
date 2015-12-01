/*
 * Aug 16, 2007
 * Proyect axioma
 * Paquete util
 * @author Jorge Armando Osorio Velasquez
 * Compilador Java 1.5.0_07-b03
 */
package util;

/**
 * @author Jorge Armando Osorio Velasquez
 *
 */
public interface ConstantesCamposParametrizables 
{
	/***************************************************************************/
	/**						TIPOS CAMPOS PARAMETRICOS						****/										
	/***************************************************************************/
	public static final String campoTipoSelect="SELE";
	public static final String campoTipoRadio="RADI";
	public static final String campoTipoCheckBox="CHEK";
	public static final String campoTipoText="TEXT";
	public static final String campoTipoTextArea="TEAR";
	public static final String campoTipoARCH="ARCH";
	public static final String campoTipoLabel="LABE";
	
	/***************************************************************************/
	/**					FUNCIONALIDADES PARAMETRIZADAS						****/										
	/***************************************************************************/
	public static final int hojaQuirurgica=1;
	public static final int valoracion=2;

	/***************************************************************************/
	/**					SECCIONES PARAMETRIZADAS							****/										
	/***************************************************************************/
	public static final int seccionGeneral=1;
	public static final int seccionServicios=2;
	public static final int seccionInformacionQuirurgica=3;
	public static final int seccionPatologia=4;
	public static final int seccionOtrosProfesionales=5;
	public static final int seccionObservacionesGenerales=6;
	
	
	
	/***************************************************************************/
	/**					TIPOS CAMPOS PARAMETRIZABLES						****/										
	/***************************************************************************/
	public static final int tipoCampoCaracter=1;
	public static final int tipoCampoAreaTexto=2;
	public static final int tipoCampoTextoPredeterminado=3;
	public static final int tipoCampoNumericoEntero=4;
	public static final int tipoCampoNumericoDecimal=5;
	public static final int tipoCampoChequeo=6;
	public static final int tipoCampoSeleccion=7;
	public static final int tipoCampoFecha=8;
	public static final int tipoCampoHora=9;
	public static final int tipoCampoArchivo=10;
	public static final int tipoCampoFormula=11;
	
	
	
	/***************************************************************************/
	/**					TIPOS COMPONENTES PARAMETRIZACION					****/										
	/***************************************************************************/
	public static final int tipoComponenteBalanceLiquidos=1;
	public static final int tipoComponenteCardiologia=2;
	public static final int tipoComponenteClinicaMemoria=3;
	public static final int tipoComponenteCurvaCrecimiento=4;
	public static final int tipoComponenteHojaObstetrica=5;
	public static final int tipoComponenteGinecologia=6;
	public static final int tipoComponenteNeurologia=7;
	public static final int tipoComponenteNeuroSiquiatria=8;
	public static final int tipoComponenteNutricion=9;
	public static final int tipoComponenteTratamientoOdontologia=10;
	public static final int tipoComponenteHojaOftalmologica=11;
	public static final int tipoComponenteOftalmologia=12;
	public static final int tipoComponentePediatria=13;
	public static final int tipoComponenteSicologia=14;
	public static final int tipoComponenteSignosVitales=15;
	public static final int tipoComponenteSiquiatria=16;
	public static final int tipoComponenteTrabajoSocial=17;
	public static final int tipoComponenteIndicePlaca=18;
	public static final int tipoComponenteOdontogramaDiag=19;// Valoracion
	public static final int tipoComponenteOdontogramaEvol=20; // Evolucion
	public static final int tipoComponenteAntecendentesOdontologicos=21;
	
	
	/***************************************************************************/
	/**					 FUNCIONALIDADES PARAMETRIZABLES					****/										
	/***************************************************************************/
	public static final int funcParametrizableValoracionUrgencias=1;
	public static final int funcParametrizableValoracionHospitalizacion=2;
	public static final int funcParametrizableValoracionConsulta=3;
	public static final int funcParametrizableEvolucion=4;
	public static final int funcParametrizableOrdenesMedicas=5;
	public static final int funcParametrizableHojaAnestesia=6;
	public static final int funcParametrizableHojaQuirurgica=7;
	public static final int funcParametrizableRegistroEnfermeria=8;
	public static final int funcParametrizableInformacionParto=9;
	public static final int funcParametrizableInformacionRecienNacido=10;
	public static final int funcParametrizableJustificacionNoPos=11;
	public static final int funcParametrizableTriage=12;
	public static final int funcParametrizableRespuestaProcedimientos=13;
	public static final int funcParametrizableValoracionInterconsulta=14;
	public static final int funcParametrizableValoracionConsultaExternaOdontologia=15;
	public static final int funcParametrizableInformacionPacienteOdontologico=16;
	public static final int funcParametrizableEvolucionOdontologica=17;
	
	/***************************************************************************/
	/**					 SIGNOS VITALES (COMPONENTE)					    ****/										
	/***************************************************************************/
	public static final int signoVitalTalla=9;
	public static final int signoVitalPeso=10;
	public static final int signoVitalIMC=11;
	public static final int signoVitalTAS=12;
	public static final int signoVitalTAD=13;
	public static final int signoVitalTAM=14;
	public static final int signoVitalFC=15;
	public static final int signoVitalFR=16;
	public static final int signoVitalTemp=17;
	public static final int signoVitalRespuestaMotor=21;
	public static final int signoVitalRespuestaVerbal=19;
	public static final int signoVitalRespuestaOcular=20;
	public static final int signoVitalGlasgow=18;
	public static final int signoVitalSO2=22;
	public static final int signoVitalPerimteroCefalico=23;
	
	
	/***************************************************************************/
	/**					 SECCIONES FIJAS        		    			    ****/										
	/***************************************************************************/
	public static final int seccionFijaInformacionGeneral = 1;
	public static final int seccionFijaAntecedentes = 2;
	public static final int seccionFijaPyp = 3;
	public static final int seccionFijaRevisionSistemas = 4;
	public static final int seccionFijaExamenFisico = 5;
	public static final int seccionFijaCausaExterna = 6;
	public static final int seccionFijaFinalidadConsulta = 7;
	public static final int seccionFijaConductaSeguir = 8;
	public static final int seccionFijaDiagnosticos = 9;
	public static final int seccionFijaObservaciones = 10;
	public static final int seccionFijaProfesionalResponsable = 11;
	public static final int seccionFijaNumeroAutorizacion = 12;
	public static final int seccionFijaConceptoConsulta = 13;
	public static final int seccionFijaFechaProximoControl = 14;
	public static final int seccionFijaDatosSubjetivos = 15;
	public static final int seccionfijaHallazgosImportantes = 16;
	public static final int seccionFijaAnalisis = 17;
	public static final int seccionFijaPlanManejo = 18;
	public static final int seccionFijaResultados = 19;
	public static final int seccionFijaIncapacidadFuncional = 20;
	public static final int seccionFijaDatosObjetivos = 21;
	public static final int seccionFijaAdjuntarDocumentos = 22;
	public static final int seccionFijaBalanceLiquidos = 23;
	public static final int seccionFijaHistoriaAtenciones = 24;
	public static final int seccionFijaComentariosGenerales = 25;
	public static final int seccionFijaMuerto = 26;
	public static final int seccionFijaOtrosComentarios = 27;
	public static final int seccionFijaProfesionalQueResponde = 28;
	public static final int seccionFijaMotivoConsulta=29;
	public static final int seccionFijaEnfermedadActual=30;
	public static final int seccionFijaConfirmar=31;
	public static final int seccionFijaMotivoCita=32;
	public static final int seccionFijaBeneficiarios=33;
	public static final int seccionFijaConvenios=34;
	
	
	
	/***************************************************************************/
	/**					 CONSTANTES EDAD PACIENTE PARA FORMULAS			    ****/										
	/***************************************************************************/
	public static final String edadPacienteDias = "EPD";
	public static final String edadPacienteMeses = "EPM";
	public static final String edadPacienteAnios = "EPA";
	
	/****************************************************************************
	/*					CONSTANTES DEL TIPO DE FORMATO 
	/*****************************************************************************/

	public static final String formatoUrgencias="URG";
	public static final String formatoConsultaExterna="CONEXT";
	public static final String formatoRespuestaCitas="FORMRESP";
	
	
}
