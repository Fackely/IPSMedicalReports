/*
 * Nov 29, 2006
 */
package util.historiaClinica;

/**
 * Constantes para el modulo de historia clínica
 *
 * @author Sebastián Gómez
 */
public interface ConstantesBDHistoriaClinica 
{
	/**=================INFORMACIÓN DE PARTOS=========================================*/
	//Constantes campo Por Gestacional
	public static final String acronimoPorGestacionalFUM = "FUM";
	public static final String acronimoPorGestacionalECO = "ECO";
	
	//Constantes campo Ligadura Cordón
	public static final String valorMayor30s = "> 30s";
	public static final String valor30s1m = "30s 1m";
	public static final String valorMayor1m = "> 1m";
	
	//Constantes secciones informacion parto
	public static final String acronimoSecIdentificacionRiesgos = "I";
	public static final String acronimoSecEnfermedades = "E";
	public static final String acronimoSecMedicacion = "M";
	public static final String acronimoSecTransfusion = "T";
	
	//Constantes campos parametrizables sección Identificadores Riesgos
	public static final int codigoCampoHistoriaObstetricaAdversa = 4;
	public static final int codigoCampoParaclinicosAnormales = 12;
	public static final int codigoCampoHallazgosEcograficosAnormales = 13;
	public static final int codigoCampoFiebre = 14;
	public static final int codigoCampoHipertension = 15;
	public static final int codigoCampoEdemaAnasarca = 16;
	public static final int codigoCampoDisnea = 17;
	
	//Constantes campos parametrizables seccion Enfermedades
	public static final int codigoCampoOtrosGraves = 49;
	public static final int codigoCampoNinguna = 50;
		
	/**================================================================================**/
	
	/**=================INFORMACIÓN RECIEN NACIDOS=========================================*/
	
	//codigo del campo parametrico
	public static final String codigoCampoTamizacionNeonatalMeconio1erDia="5";
	//codigo del campo parametrico
	public static final String codigoCampoTamizacionNeonatalBocaArriba="6";
//	codigo del campo parametrico
	public static final String codigoCampoSecAdapNeonatalInmediatoCampoTipoPinzamientoCordon="7";

	//parametrizacion examenes fisicos.
	public static final String codigoCampoExamenFisicoPeso="1";
	public static final String codigoCampoExamenFisicoTalla="2";
	public static final String codigoCampoExamenFisicoPerimetroCefalico="3";
	public static final String codigoCampoExamenFisicoPerimetroToraxico="4";
	public static final String codigoCampoExamenFisicoFuerzaCardiaca="5";
	public static final String codigoCampoExamenFisicoFuerzaRespiratoria="6";
	public static final String codigoCampoExamenFisicoTemperatura="7";
	public static final String codigoCampoExamenFisicoCabeza="8";
	public static final String codigoCampoExamenFisicoOjos="9";
	public static final String codigoCampoExamenFisicoORL="10";
	public static final String codigoCampoExamenFisicoCuello="11";
	public static final String codigoCampoExamenFisicoCP="12";
	public static final String codigoCampoExamenFisicoAbdomen="13";
	public static final String codigoCampoExamenFisicoGenitoUrinario="14";
	public static final String codigoCampoExamenFisicoExtremidades="15";
	public static final String codigoCampoExamenFisicoMalformaciones="16";
	public static final String codigoCampoExamenFisicoInfeccion="17";
	public static final String codigoCampoExamenFisicoPermeabilidadRectal="18";
	public static final String codigoCampoExamenFisicoPermeabilidadEsofacica="19";
	public static final String codigoCampoExamenFisicoProfilaxisOftalmica="20";
	public static final String codigoCampoExamenFisicoPresenciaDeposicion="22";
	public static final String codigoCampoExamenFisicoPresenciaOrina="23";
	public static final String codigoCampoExamenFisicoVomito="24";
	public static final String codigoCampoExamenFisicoSialorrea="25";
	public static final String codigoCampoExamenFisicoMunionUmbilical="21";
	
	public static final String codigoCampoSeccionSanoCertificadoNacimiento="2";
	
	/**================================================================================**/
	/**=====================FINALIDADES CONSULTA=========================================**/
	public static final String codigoFinalidadConsultaAtencionParto = "01";
	public static final String codigoFinalidadConsultaAtencionRecienNacido = "02";
	public static final String codigoFinalidadConsultaAtencionPlanificacionFamiliar = "03";
	public static final String codigoFinalidadConsultaDeteccionAlteracionesCrecimiento = "04";
	public static final String codigoFinalidadConsultaDeteccionAlteracionesDesarrollo = "05";
	public static final String codigoFinalidadConsultaDeteccionAlteracionesEmbarazo = "06";
	public static final String codigoFinalidadConsultaDeteccionAlteracionesAdulto = "07";
	public static final String codigoFinalidadConsultaDeteccionAlteracionesAgudezaVisual = "08";
	public static final String codigoFinalidadConsultaDeteccionAlteracionesEnfermedad = "09";
	public static final String codigoFinalidadConsultaNoAplica = "10";
	
	/**================================================================================**/
	/**====================CAMPOS JUSTIFICACION NO POS DE INSUMOS======================**/
	public static final String codigoCampoJusNoPosInsTituloEncabezado = "50";
	public static final String codigoCampoJusNoPosInsTextoEncabezado = "51";
	public static final String codigoCampoJusNoPosInsNombrePaciente = "52";
	public static final String codigoCampoJusNoPosInsIdPaciente = "53";
	public static final String codigoCampoJusNoPosEntidad = "54";
	public static final String codigoCampoJusNoPosIngreso = "55";
	public static final String codigoCampoJusNoPosFecha = "56";
	public static final String codigoCampoJusNoPosTipoUsuario = "57";
	public static final String codigoCampoJusNoPosEdad = "58";
	public static final String codigoCampoJusNoPosEstado = "59";
	public static final String codigoCampoJusNoPosCentroCosto = "60";
	public static final String codigoCampoJusNoPosConsecutivo = "61";
	public static final String codigoCampoJusNoPosRiesgoInminente = "62";
	public static final String codigoCampoJusNoPosTipoRiesgoSoportado = "63";
	public static final String codigoCampoJusNoPosDxComplicacion = "64";
	public static final String codigoCampoJusNoPosDxPrincipal = "65";
	public static final String codigoCampoJusNoPosDxRelacionado = "66";
	public static final String codigoCampoJusNoPosNombreArtNoPos = "71";
	public static final String codigoCampoJusNoPosCantidadArtNoPos = "72";
	public static final String codigoCampoJusNoPosPacienteInformado = "76";
	public static final String codigoCampoJusNoPosBibliografia = "77";
	public static final String codigoCampoJusNoPosExisteSustituto = "78";
	public static final String codigoCampoJusNoPosArticuloSustituye = "79";
	public static final String codigoCampoJusNoPosCantidadSustituye = "80";
	public static final String codigoCampoJusNoPosResumenJustifica = "81";
	public static final String codigoCampoJusNoPosMedico = "82";
	public static final String codigoCampoJusNoPosEspecialidadMedico = "83";
	public static final String codigoCampoJusNoPosIdMedico = "84";
	public static final String codigoCampoJusNoPosRegistroMedico = "85";
	public static final String codigoCampoJusNoPosFirmaSelloMedico = "86";
	/**====================================================================================**/
}
