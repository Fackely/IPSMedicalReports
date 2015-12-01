package com.sysmedica.dao.sqlbase;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadFecha;

import com.sysmedica.util.CalendarioEpidemiologico;
import com.sysmedica.util.SemanaEpidemiologica;

import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

public class SqlBaseArchivosPlanosDao {


	/**
     * Objeto que maneja los logs de esta clase
     */
    private static Logger logger = Logger.getLogger(SqlBaseArchivosPlanosDao.class);
    
    
    
    
    private static final String consultaInfoBasicaParte1Str = "SELECT " +
																	
																	"ficha.pais as paisProcedencia," +
																	"ficha.areaProcedencia, " +
																	
																	"ficha.codigoDepProcedencia AS departamentoProcedencia, " +
																	"ficha.codigoMunProcedencia AS municipioProcedencia, " +
																	"ficha.fechaconsultageneral AS fechaConsultaGeneral, " +
																	"ficha.fechainiciosintomasgeneral AS fechaInicioSintomas, " +
																	"ficha.tipocaso AS tipoCaso, " +
																	"ficha.hospitalizadogeneral AS hospitalizado, " +
																	"ficha.fechahospitalizaciongeneral AS fechaHospitalizacion, " +
																	"ficha.estavivogeneral AS condicionFinal, " +
																	"ficha.fechadefuncion AS fechaDefuncion, " +
																	"ficha.nombreProfesionalDiligencio AS nombreProfesional, " +
																	"ficha.codigoDepNoti AS departamentoNotifica, " +
																	"ficha.codigoMunNoti AS municipioNotifica, " +
																	"ficha.institucionAtendio AS nombreUnidad, " +
																	"ficha.nombreprofesionaldiligencio AS nombreProfesional, " +
																	"ficha.fechaDiligenciamiento AS fechaDiligenciamiento, "+
																	
																	"per.primer_nombre," +
																	"per.segundo_nombre," +
																	"per.primer_apellido," +
																	"per.segundo_apellido," +
																	"dep.nombre AS dep_nacimiento," +
																	"ciu.nombre AS ciu_nacimiento," +
																	"dep2.nombre AS dep_vivienda," +
																	"ciu2.nombre AS ciu_vivienda," +
																	"per.direccion AS direccion_paciente," +
																	"per.telefono AS telefono_paciente," +
																	"per.fecha_nacimiento," +
																	"per.sexo," +
																	"per.estado_civil," +
																	"per.numero_identificacion, " +
																	"per.codigo_pais_nacimiento, " +
																	"per.codigo_pais_vivienda, " +
																	"per.codigo_pais_id, " +
																	
																	"bar.nombre AS barrio, " +
																	"pac.zona_domicilio AS zonaDomicilio, " +
																	"pac.ocupacion AS ocupacion_paciente, " +
																	"per.tipo_identificacion AS tipoId, " +
																	"conv.tipo_regimen AS aseguradora, " +
																	"conv.codigo_min_salud, " +
																	"regs.nombre AS regimenSalud, " +
																	"pac.etnia AS etnia, " +
																//	"ficha.desplazado AS desplazado " +
																	"pac.grupo_poblacional as grupoPoblacional " +
																
																"FROM ";
																
    
    
    
    private static final String consultaInfoBasicaParte2Str =
																	" ficha, personas per, departamentos dep, ciudades ciu, departamentos dep2, ciudades ciu2, " +
																	"usuarios usu, personas per2, barrios bar, pacientes pac, " +
																	"convenios conv, tipos_regimen regs " +
																"WHERE " +
																	"ficha.fechaDiligenciamiento >= ? " +
																	"AND ficha.fechaDiligenciamiento <= ? " +	
																	"AND per.codigo=ficha.codigoPaciente " +
																	"AND dep.codigo=per.codigo_departamento_nacimiento " +
																	"AND ciu.codigo_ciudad=per.codigo_ciudad_nacimiento " +
																	"AND ciu.codigo_departamento=per.codigo_departamento_nacimiento " +
																	"AND dep2.codigo=per.codigo_departamento_vivienda " +
																	"AND ciu2.codigo_ciudad=per.codigo_ciudad_vivienda " +
																	"AND ciu2.codigo_departamento=per.codigo_departamento_vivienda " +
																	
																	"AND ficha.loginUsuario=usu.login " +
																	"AND usu.codigo_persona=per2.codigo "+
																	"AND per.codigo_departamento_vivienda=bar.codigo_departamento " +
																	"AND per.codigo_ciudad_vivienda=bar.codigo_ciudad " +
																	"AND per.codigo_barrio_vivienda=bar.codigo_barrio " +
																	"AND per.codigo=pac.codigo_paciente " +
																	"AND pac.codigo_paciente=ficha.codigoPaciente " +
																	"AND conv.codigo=ficha.codigoAseguradora ";

    
    
    
    
    private static final String consultaCasosDengueStr = "SELECT " +
    														"ficha.codigoFichaDengue, " +
															"ficha.acronimo, " +
															"ficha.vacunaFiebreAmarilla, " +
															"ficha.fechaApliVacunaFiebre, " +
															"ficha.vacunaHepatitisBDosis1, " +
															"ficha.vacunaHepatitisBDosis2, " +
															"ficha.vacunaHepatitisBDosis3, " +
															"ficha.fechaVacunaHepaDosis1, " +
															"ficha.fechaVacunaHepaDosis2, " +
															"ficha.fechaVacunaHepaDosis3, " +
															"ficha.vacunaHepatitisADosis1, " +
															"ficha.fechaVacunaHepatADosis1, " +
															"ficha.observaciones, " +
															"ficha.desplazamiento, " +
															"ficha.fechaDesplazamiento, " +
															"ficha.codigoMunicipio, " +
															"ficha.codigoDepartamento, " +
															"ficha.casoFiebreAmarilla, " +
															"ficha.casoEpizootia, " +
															"ficha.presenciaAedes, " +
															
															"per.numero_identificacion, " +
															"per.tipo_identificacion AS tipoId " +
																														
														"FROM " +
															"epidemiologia.vigifichadengue ficha, " +
															"personas per " +
														"WHERE " +
														
															"ficha.fechaDiligenciamiento >= ? " +
															"AND ficha.fechaDiligenciamiento <= ? " +																
															"AND per.codigo=ficha.codigoPaciente ";
    
    
    private static final String consultaHallazgosDengueStr = "SELECT codigoHallazgo FROM epidemiologia.vigiDetalleHallazgos WHERE codigoFichaDengue = ?";
															
    
    
    
    private static final String consultaCasosIntoxicacionesStr = "SELECT " +
																    "ficha.codigoFichaIntoxicacion, " +
																	"ficha.acronimo, " +
																	"ficha.tipoIntoxicacion, " +
																    "ficha.nombreProducto, " +
																    "ficha.tipoExposicion, " +
																    "ficha.produccion, " +
																    "ficha.almacenamiento, " +
																    "ficha.agricola, " +
																    "ficha.saludPublica, " +
																    "ficha.domiciliaria, " +
																    "ficha.tratHumano, " +
																    "ficha.tratVeterinario, " +
																    "ficha.transporte, " +
																    "ficha.mezcla, " +
																    "ficha.mantenimiento, " +
																    "ficha.cultivo, " +
																    "ficha.otros, " +
																    "ficha.otraActividad, " +
																    "ficha.fechaExposicion, " +
																    "ficha.horaExposicion, " +
																    "ficha.viaExposicion, " +
																    "ficha.otraViaExposicion, " +
																    "ficha.escolaridad, " +
																    "ficha.embarazada, " +
																    "ficha.vinculoLaboral, " +
																    "ficha.afiliadoArp, " +
																    "ficha.nombreArp, " +
																    "ficha.codgoArp, " +
																//    "ficha.estadoCivil, " +
																    "ficha.alerta, " +
																    "ficha.investigacion, " +
																    "ficha.fechaInvestigacion, " +
																    "ficha.fechaInforma, " +
																    
																    "per.numero_identificacion, " +
																    "per.estado_civil, " +
																	"per.tipo_identificacion AS tipoId " +
																																
																"FROM " +
																	"epidemiologia.vigifichaintoxicacion ficha, " +
																	"personas per " +
																"WHERE " +
																
																	"ficha.fechaDiligenciamiento >= ? " +
																	"AND ficha.fechaDiligenciamiento <= ? " +																
																	"AND per.codigo=ficha.codigoPaciente ";
    

    


    private static final String consultaCasosLepraStr = "SELECT " +
														    "ficha.codigoFichaLepra, " +
															"ficha.acronimo, " +
															"ficha.criterioClinico, " +
													    	"ficha.indiceBacilar, " +
													    	"ficha.clasificacion, " +
													    	"ficha.resultadosBiopsia, " +
													    	"ficha.ojoDerecho, " +
													    	"ficha.ojoIzquierdo, " +
													    	"ficha.manoDerecha, " +
													    	"ficha.manoIzquierda, " +
													    	"ficha.pieDerecho, " +
													    	"ficha.pieIzquierdo, " +
													    	"ficha.tipoCasoLepra, " +
													    	"ficha.tieneCicatriz, " +
													    	"ficha.fuenteContagio, " +
													    	"ficha.metodoCaptacion, " +
													    	"ficha.fechaInvestigacion, " +
													    	"ficha.tieneConvivientes, " +
													    	"ficha.totalConvivientes, " +
													    	"ficha.totalExaminados, " +
													    	"ficha.sanosConCicatriz, " +
													    	"ficha.sanosSinCicatriz, " +
													    	"ficha.sintomaticosConCicatriz, " +
													    	"ficha.sintomaticosSinCicatriz, " +
													    	"ficha.vacunadosBcg, " +
													    	
													    	"per.numero_identificacion, " +
															"per.tipo_identificacion AS tipoId " +
																														
														"FROM " +
															"epidemiologia.vigifichalepra ficha, " +
															"personas per " +
														"WHERE " +
														
															"ficha.fechaDiligenciamiento >= ? " +
															"AND ficha.fechaDiligenciamiento <= ? " +																
															"AND per.codigo=ficha.codigoPaciente ";
    
    
    
    
    
    private static final String consultaCasosParalisisStr = "SELECT " +
															    "ficha.codigoFichaParalisis, " +
																"ficha.acronimo, " +
																"ficha.nombreMadre," +
																"ficha.nombrePadre, " +
																"ficha.fechaInicioInvestigacion, " +
																"ficha.numeroDosis, " +
																"ficha.fechaUltimaDosis, " +
																"ficha.tieneCarnet, " +
																"ficha.fiebre, " +
																"ficha.respiratorios, " +
																"ficha.digestivos, " +
																"ficha.instalacion, " +
																"ficha.dolorMuscular, " +
																"ficha.signosMeningeos1, " +
																"ficha.fiebreInicioParalisis, " +
																"ficha.progresion, " +
																"ficha.fechaInicioParalisis, " +
																"ficha.musculosRespiratorios, " +
																"ficha.signosMeningeos2, " +
																"ficha.babinsky, " +
																"ficha.brudzinsky, " +
																"ficha.paresCraneanos, " +
																"ficha.liquidoCefalo, " +
																"ficha.fechaTomaLiquido, " +
																"ficha.celulas, " +
																"ficha.globulosRojos, " +
																"ficha.leucocitos, " +
																"ficha.linfocitos, " +
																"ficha.proteinas, " +
																"ficha.glucosa, " +
																"ficha.electromiografia, " +
																"ficha.fechaTomaElectro, " +
																"ficha.velocidadConduccion, " +
																"ficha.resultadoConduccion, " +
																"ficha.fechaTomaVelocidad, " +
																"ficha.impresionDiagnostica, " +
																"ficha.muestraMateriaFecal, " +
																"ficha.fechaTomaFecal, " +
																"ficha.fechaEnvioFecal, " +
																"ficha.fechaRecepcionFecal, " +
																"ficha.fechaResultadoFecal, " +
																"ficha.virusAislado, " +
																"ficha.fechaVacunacionBloqueo, " +
																"ficha.fechaCulminacionVacunacion, " +
																"ficha.municipiosVacunados, " +
																"ficha.telefonoContacto, " +
																"ficha.codigoextremidad1, " +
																"ficha.codigoextremidad2, " +
																"ficha.codigoextremidad3, " +
																"ficha.codigoextremidad4, " +
																"ficha.codigogrupo1, " +
																"ficha.codigogrupo2, " +
																"ficha.codigogrupo3, " +
																
																"per.numero_identificacion, " +
																"per.tipo_identificacion AS tipoId " +
																															
															"FROM " +
																"epidemiologia.vigifichaparalisis ficha, " +
																"personas per " +
															"WHERE " +
															
																"ficha.fechaDiligenciamiento >= ? " +
																"AND ficha.fechaDiligenciamiento <= ? " +																
																"AND per.codigo=ficha.codigoPaciente ";
    
    

    public static final String consultarExtremidadStr = "SELECT " +
    														"paresia, " +
    														"paralisis, " +
    														"flaccida," +
    														"localizacion," +
    														"sensibilidad," +
    														"rot " +
    													"FROM " +
    														"epidemiologia.vigiExtremidad " +
    													"WHERE " +
    														"codigo=? ";
    
    

    public static final String consultarGrupoEdadStr = "SELECT " +
    														"poblacionmeta," +
    														"reciennacido," +
    														"vop1," +
    														"vop2," +
    														"vop3," +
    														"adicional " +
    													"FROM " +
    														"epidemiologia.vigiGrupoEdad " +
    													"WHERE " +
    														"codigo=? ";
    
    
    
    
    
    
    
    
    

    private static final String consultaCasosRabiaStr = "SELECT " +
														    "ficha.codigoFichaRabia, " +
															"ficha.acronimo, " +
															"ficha.tipoAgresion," +
															"ficha.provocada,"+
															"ficha.tipoLesion," +
															"ficha.profundidadlesion," +
															"ficha.cabeza," +
															"ficha.manos," +
															"ficha.tronco," +
															"ficha.extresuperiores," +
															"ficha.extreinferiores,"+
															"ficha.tipoExposicion,"+
															"ficha.fechaAgresion," +
															"ficha.sueroAntirrabico,"+
															"ficha.fechaAplicacion,"+
															"ficha.vacunaAntirrabica,"+
															"ficha.dosisAplicadas,"+
															"ficha.fechaUltimaDosis,"+
															
															"trat.lavadoherida," +
															"trat.suturaherida," +
															"trat.aplicacionsuero," +
															"trat.fechaaplicacionsuero," +
															"trat.aplicarvacuna," +
															"trat.fechavacunadosis1," +
															"trat.fechavacunadosis2," +
															"trat.fechavacunadosis3," +
															"trat.fechavacunadosis4," +
															"trat.fechavacunadosis5," +
															"trat.suspensiontratamiento," +
															"trat.reaccionesvacunasuero," +
															"trat.evolucionpaciente," +
															
															"ani.especie," +
															"ani.vacunado," +
															"ani.fechaultimadosisanimal, " +
															"ani.nombrepropietario," +
	    													"ani.direccionpropietario," +
	    													"ani.estadomomentoagresion," +
	    													"ani.ubicacionanimal," +
	    													"ani.numerodiasobserva," +
	    													"ani.lugarobservacion," +
	    													"ani.estadoanimalobserva," +
	    													"ani.fechamuerte," +
	    													"ani.confirmaciondiagnostica AS confirmacionanimal, " +
	    													"ani.fechatomamuestra," +
	    													"trat.pruebaslaboratorio," +
	    													"ficha.confDiagnosticaCasoRabia, " +
	    													"ficha.fechaMuestraCasoRabia, " +
	    													"ani.informacionlaboratorio," +

															"per.numero_identificacion, " +
															"per.tipo_identificacion AS tipoId " +
															
														"FROM " +
															"epidemiologia.vigificharabia ficha,epidemiologia.vigianimal ani,epidemiologia.vigitratantirrabico trat, " +
															"personas per " +
														"WHERE " +
														
															"ficha.fechaDiligenciamiento >= ? " +
															"AND ficha.fechaDiligenciamiento <= ? " +																
															"AND per.codigo=ficha.codigoPaciente ";
	
	
    
    
    
    
    
    

    private static final String consultaCasosSarampionStr = "SELECT " +
	    														"ficha.codigoFichaSarampion, " +
																"ficha.acronimo, " +
																"ficha.nombrePadre," +
																"ficha.ocupacionPadre," +
																"ficha.fechaVisita1," +
																"ficha.fuenteNotificacion," +
																"ficha.vacunaSarampion," +
																"ficha.numeroDosisSarampion," +
																"ficha.fechaUltimaDosisSarampion," +
																"ficha.fuenteDatosSarampion," +
																"ficha.vacunaRubeola," +
																"ficha.numeroDosisRubeola," +
																"ficha.fechaUltimaDosisRubeola," +
																"ficha.fuenteDatosRubeola," +
																"ficha.fechaVisitaDomiciliaria," +
																"ficha.fiebre," +
																"ficha.fechaInicioFiebre," +
																"ficha.tipoErupcion," +
																"ficha.fechaInicioErupcion," +
																"ficha.duracionErupcion," +
																"ficha.tos," +
																"ficha.coriza," +
																"ficha.conjuntivitis," +
																"ficha.adenopatia," +
																"ficha.artralgia," +
																"ficha.embarazada," +
																"ficha.numeroSemanas," +
																"ficha.municipioParto," +
																"ficha.departamentoParto," +
																"ficha.huboContacto," +
																"ficha.huboCasoConfirmado," +
																"ficha.huboViaje," +
																"ficha.municipioViaje," +
																"ficha.departamentoViaje," +
																"ficha.huboContactoEmbarazada," +
																"ficha.diagnosticoFinal, " +
																
																"per.numero_identificacion, " +
																"per.tipo_identificacion AS tipoId " +
																															
															"FROM " +
																"epidemiologia.vigifichasarampion ficha, " +
																"personas per " +
															"WHERE " +
															
																"ficha.fechaDiligenciamiento >= ? " +
																"AND ficha.fechaDiligenciamiento <= ? " +																
																"AND per.codigo=ficha.codigoPaciente ";
	
    
    
    
    
    
    

    private static final String consultaCasosVIHStr = "SELECT " +
	    														"ficha.codigoFichaVIH, " +
																"ficha.acronimo, " +
																"ficha.tipoMuestra," +
	    														"ficha.tipoPrueba," +
	    														"ficha.resultado," +
	    														"ficha.fechaResultado," +
	    														"ficha.valor," +
	    														"ficha.estadioClinico," +
	    														"ficha.numeroHijos," +
	    														"ficha.numeroHijas," +
	    														"ficha.embarazo," +
	    														"ficha.numeroSemanas," +
																
																
																"per.numero_identificacion, " +
																"per.tipo_identificacion AS tipoId " +
																															
															"FROM " +
																"epidemiologia.vigifichavih ficha, " +
																"personas per " +
															"WHERE " +
															
																"ficha.fechaDiligenciamiento >= ? " +
																"AND ficha.fechaDiligenciamiento <= ? " +																
																"AND per.codigo=ficha.codigoPaciente ";
    
    
    
    private static final String consultaMecanismosVIHStr = "SELECT " +
																"codigoMecanismo " +
															"FROM " +
																"epidemiologia.vigiDetalleMecanismoTran " +
															"WHERE " +
																"codigoFichaVIH = ? ";
    
    
    

    public static final String consultaEnfermedadesAsociadasVIHStr = "SELECT " +
																		"codigoEnfermedad " +
																	"FROM " +
																		"epidemiologia.vigiDetalleEnfAsociada " +
																	"WHERE " +
																		"codigoFichaVIH = ? ";
																	
	
    
    
    
    
    
    

    private static final String consultaCasosAcciOfidicoStr = "SELECT " +
	    														"ficha.codigoFichaOfidico, " +
																"ficha.acronimo, " +
																"ficha.fechaAccidente, " +
																"ficha.nombreVereda, " +
																"ficha.actividadAccidente, " +
																"ficha.tipoAtencionInicial, " +
																"ficha.practicasNoMedicas, " +
																"ficha.localizacionMordedura, " +
																"ficha.huellasColmillos, " +
																"ficha.serpienteIdentificada, " +
																"ficha.serpienteCapturada, " +
																"ficha.generoAgenteAgresor, " +
																"ficha.nombreAgenteAgresor, " +
																"ficha.severidadAccidente, " +
																"ficha.empleoSuero, " +
																"ficha.diasTranscurridos, " +
																"ficha.horasTranscurridas, " +
																"ficha.tipoSueroAntiofidico, " +
																"ficha.dosisSuero, " +
																"ficha.horasSuero, " +
																"ficha.minutosSuero, " +
																"ficha.tratamientoQuirurgico, " +
																"ficha.tipoTratamiento, " +
																"ficha.cualLocal, " +
																"ficha.cualComplicacion, " +
																"ficha.cualSistemica, " +
																
																
																"per.numero_identificacion, " +
																"per.tipo_identificacion AS tipoId " +
																															
															"FROM " +
																"epidemiologia.vigifichaofidico ficha, " +
																"personas per " +
															"WHERE " +
															
																"ficha.fechaDiligenciamiento >= ? " +
																"AND ficha.fechaDiligenciamiento <= ? " +																
																"AND per.codigo=ficha.codigoPaciente ";
    
    
    
    

    public static final String consultaManiLocalStr = "SELECT " +
															"codigoManifestacion " +
														"FROM " +
															"epidemiologia.vigiDetalleManiLocales " +
														"WHERE " +
															"codigoFichaOfidico = ? ";
    
    
    public static final String consultaManiSistemicaStr = "SELECT " +
																"codigoManifestacion " +
															"FROM " +
																"epidemiologia.vigiDetalleManiSistemica " +
															"WHERE " +
																"codigoFichaOfidico = ? ";
    
    
    public static final String consultaCompliLocalStr = "SELECT " +
																"codigoComplicacion " +
															"FROM " +
																"epidemiologia.vigiDetalleCompliLocales " +
															"WHERE " +
																"codigoFichaOfidico = ? ";
    
    
    public static final String consultaCompliSistemicaStr = "SELECT " +
																"codigoComplicacion " +
															"FROM " +
																"epidemiologia.vigiDetalleCompliSist " +
															"WHERE " +
																"codigoFichaOfidico = ? ";
    
    
    
    
    
    
    
    
    
    

    private static final String consultaCasosMortalidadStr = "SELECT " +
	    														"ficha.codigoFichaMortalidad, " +
																"ficha.acronimo, " +
																"ficha.sitioDefuncion, " +
    															"ficha.convivencia, " +
    															"ficha.otroConvivencia, " +
    															"ficha.escolaridad, " +
    															"ficha.fecundidad, " +
    															"ficha.gestaciones, " +
    															"ficha.partos, " +
    															"ficha.cesareas, " +
    															"ficha.abortos, " +
    															"ficha.muertos, " +
    															"ficha.vivos, " +
    															"ficha.infecciones, " +
    															"ficha.factoresRiesgo, " +
    															"ficha.cuantosControles, " +
    															"ficha.semanaInicioCpn, " +
    															"ficha.controlesRealizadosPor, " +
    															"ficha.nivelAtencion, " +
    															"ficha.clasificacionRiesgo, " +
    															"ficha.quienClasificoRiesgo, " +
    															"ficha.remisionesOportunas, " +
    															"ficha.complicaciones, " +
    															"ficha.momentoFallecimiento, " +
    															"ficha.semanasGestacion, " +
    															"ficha.tipoParto, " +
    															"ficha.atendidoPor, " +
    															"ficha.nivelAtencion2, " +
    															"ficha.momentoMuerteRelacion, " +
    															"ficha.edadGestacional, " +
    															"ficha.pesoNacimiento, " +
    															"ficha.tallaNacimiento, " +
    															"ficha.apgarNacimiento1, " +
    															"ficha.apgarNacimiento5, " +
    															"ficha.nivelAtencion3, " +
    															"ficha.remisionOportunaComplica, " +
    															"ficha.adaptacionNeonatal, " +
    															"ficha.causaBasicaDefuncion, " +
    															"ficha.muerteDemora, " +
    															"ficha.causaMuerteDet, " +
    																														
																
																"per.numero_identificacion, " +
																"per.tipo_identificacion AS tipoId " +
																															
															"FROM " +
																"epidemiologia.vigifichamortalidad ficha, " +
																"personas per " +
															"WHERE " +
															
																"ficha.fechaDiligenciamiento >= ? " +
																"AND ficha.fechaDiligenciamiento <= ? " +																
																"AND per.codigo=ficha.codigoPaciente ";
    
    
    

    private static final String consultaAntecedentesMortalidadStr = "SELECT " +
		    															"codigoAntecedente " +
		    														"FROM " +
		    															"epidemiologia.vigiDetalleAnteRiesgo " +
		    														"WHERE " +
		    															"codigoFichaMortalidad = ?";
    
    
    
    public static final String consultaComplicacionesMortalidadStr = "SELECT " +
																		"codigoComplicacion " +
																	"FROM " +
																		"epidemiologia.vigiDetalleCompEmbarazo " +
																	"WHERE " +
																		"codigoFichaMortalidad = ? ";
    
    
    
    
    
    
    

    private static final String consultaCasosRubCongenitaStr = "SELECT " +
	    														"ficha.codigoFichaRubCongenita, " +
																"ficha.acronimo, " +
																"ficha.clasificacionInicial, " +
																"ficha.nombreTutor, " +
																"ficha.lugarNacimientoPaciente, " +
																"ficha.fuenteNotificacion, " +
																"ficha.nombreMadre, " +
																"ficha.edadMadre, " +
																"ficha.embarazos, " +
																"ficha.carneVacunacion, " +
																"ficha.vacunaRubeola, " +
																"ficha.numeroDosis, " +
																"ficha.fechaUltimaDosis, " +
																"ficha.rubeolaConfirmada, " +
																"ficha.semanasEmbarazo, " +
																"ficha.similarRubeola, " +
																"ficha.semanasEmbarazo2, " +
																"ficha.expuestaRubeola, " +
																"ficha.semanasEmbarazo3, " +
																"ficha.donde, " +
																"ficha.viajes, " +
																"ficha.semanasEmbarazo4, " +
																"ficha.dondeViajo, " +
																"ficha.apgar, " +
																"ficha.bajoPesoNacimiento, " +
																"ficha.peso, " +
																"ficha.pequenoEdadGesta, " +
																"ficha.semanasEdad, " +
																"ficha.cataratas, " +
																"ficha.glaucoma, " +
																"ficha.retinopatia, " +
																"ficha.otrosOjo, " +
																"ficha.arterioso, " +
																"ficha.estenosis, " +
																"ficha.otrosCorazon, " +
																"ficha.sordera, " +
																"ficha.otrosOido, " +
																"ficha.microCefalia, " +
																"ficha.sicomotor, " +
																"ficha.purpura, " +
																"ficha.hepatomegalia, " +
																"ficha.ictericia, " +
																"ficha.esplenomegalia, " +
																"ficha.osteopatia, " +
																"ficha.meningoencefalitis, " +
																"ficha.otrosGeneral, " +
																"ficha.examenesEspeciales, " +
																"ficha.examen, " +
																"ficha.anatomoPatologico, " +
																"ficha.examen2, " +
																"ficha.compatibleSrc, " +
																"ficha.dxFinal, " +
    																														
																
																"per.numero_identificacion, " +
																"per.tipo_identificacion AS tipoId " +
																															
															"FROM " +
																"epidemiologia.vigificharubcongenita ficha, " +
																"personas per " +
															"WHERE " +
															
																"ficha.fechaDiligenciamiento >= ? " +
																"AND ficha.fechaDiligenciamiento <= ? " +																
																"AND per.codigo=ficha.codigoPaciente ";
    
    
    
    
    
    

    private static final String consultaCasosSifilisStr = "SELECT " +
	    														"ficha.codigoFichaSifilis, " +
																"ficha.acronimo, " +
																"ficha.controlPrenatal, " +
    															"ficha.edadGestacional, " +
    															"ficha.edadGestacionalSero1, " +
    															"ficha.edadGestacionalTrat, " +
    															"ficha.condicionmomentodx, " +
    															"ficha.lugarAtencionParto, " +
    															"ficha.estadoNacimiento, " +
    															"ficha.recibioTratamiento, " +
    															"ficha.tipoTratamiento, " +
    															"ficha.medicamentoAdmin, " +
    															"ficha.esquemaCompleto, " +
    															"ficha.otrasIts, " +
    															"ficha.alergiaPenicilina, " +
    															"ficha.desensibilizaPenicilina, " +
    															"ficha.diagnosticoContactos, " +
    															"ficha.tratamientoContactos, " +
    															"ficha.tratamientoHospitalario, " +
    															"ficha.tratamientoAmbulatorio, " +    															
    															
																"per.numero_identificacion, " +
																"per.tipo_identificacion AS tipoId " +
																															
															"FROM " +
																"epidemiologia.vigifichasifilis ficha, " +
																"personas per " +
															"WHERE " +
															
																"ficha.fechaDiligenciamiento >= ? " +
																"AND ficha.fechaDiligenciamiento <= ? " +																
																"AND per.codigo=ficha.codigoPaciente ";
    
    
    
    
    
    

    private static final String consultaCasosTetanosStr = "SELECT " +
	    														"ficha.codigoFichaTetanos, " +
																"ficha.acronimo, " +
																"ficha.nombreMadre, " +
																"ficha.edadMadre, " +
																"ficha.fechaNacimiento, " +
																"ficha.fechaEgresoHospital, " +
																"ficha.nacimientoTraumatico, " +
																"ficha.llantoNacer, " +
																"ficha.mamabaNormal, " +
																"ficha.dejoMamar, " +
																"ficha.fechaDejo, " +
																"ficha.dificultadRespiratoria, " +
																"ficha.episodiosApnea, " +
																"ficha.hipotermia, " +
																"ficha.hipertermia, " +
																"ficha.fontAbombada, " +
																"ficha.rigidezNuca, " +
																"ficha.trismus, " +
																"ficha.convulsiones, " +
																"ficha.espasmos, " +
																"ficha.contracciones, " +
																"ficha.opistotonos, " +
																"ficha.llantoExcesivo, " +
																"ficha.sepsisUmbilical, " +
																"ficha.numeroEmbarazos, " +
																"ficha.asistioControl, " +
																"ficha.explicacionNoAsistencia, " +
																"ficha.atendidoPorMedico, " +
																"ficha.atendidoPorEnfermero, " +
																"ficha.atendidoPorAuxiliar, " +
																"ficha.atendidoPorPromotor, " +
																"ficha.atendidoPorOtro, " +
																"ficha.quienAtendio, " +
																"ficha.numeroControlesPrevios, " +
																"ficha.fechaUltimoControl, " +
																"ficha.madreVivioMismoLugar, " +
																"ficha.codigoMunicipioVivienda, " +
																"ficha.codigoDepVivienda, " +
																"ficha.antecedenteVacunaAnti, " +
																"ficha.dosisDpt, " +
																"ficha.explicacionNoVacuna, " +
																"ficha.fechaDosisTd1, " +
																"ficha.fechaDosisTd2, " +
																"ficha.fechaDosisTd3, " +
																"ficha.fechaDosisTd4, " +
																"ficha.lugarParto, " +
																"ficha.institucionParto, " +
																"ficha.fechaIngresoParto, " +
																"ficha.fechaEgresoParto, " +
																"ficha.quienAtendioParto, " +
																"ficha.instrumentoCordon, " +
																"ficha.metodoEsterilizacion, " +
																"ficha.recibioInformacionMunon, " +
																"ficha.aplicacionSustanciasMunon, " +
																"ficha.cualesSustancias, " +
																"ficha.distanciaMinutos, " +
																"ficha.fechaInvestigacionCampo, " +
																"ficha.fechaVacunacion, " +
																"ficha.dosisTd1AplicadasMef, " +
																"ficha.dosisTd2AplicadasMef, " +
																"ficha.dosisTd3AplicadasMef, " +
																"ficha.dosisTd4AplicadasMef, " +
																"ficha.dosisTd5AplicadasMef, " +
																"ficha.dosisTd1AplicadasGest, " +
																"ficha.dosisTd2AplicadasGest, " +
																"ficha.dosisTd3AplicadasGest, " +
																"ficha.dosisTd4AplicadasGest, " +
																"ficha.dosisTd5AplicadasGest, " +
																"ficha.coberturaLograda, " +															
    															
																"per.numero_identificacion, " +
																"per.tipo_identificacion AS tipoId " +
																															
															"FROM " +
																"epidemiologia.vigifichatetanos ficha, " +
																"personas per " +
															"WHERE " +
															
																"ficha.fechaDiligenciamiento >= ? " +
																"AND ficha.fechaDiligenciamiento <= ? " +																
																"AND per.codigo=ficha.codigoPaciente ";
    
    
    
    
    
    
    
    

    private static final String consultaCasosTuberculosisStr = "SELECT " +
	    														"ficha.codigoFichaTuberculosis, " +
																"ficha.acronimo, " +
																"ficha.baciloscopia, " +
																"ficha.cultivo," +
																"ficha.histopatologia," +
																"ficha.clinicapaciente," +
																"ficha.nexoepidemiologico," +
																"ficha.radiologico," +
																"ficha.tuberculina," +
																"ficha.ada," +
																"ficha.otroDx," +
																"ficha.bk," +
																"ficha.valorada," +
																"ficha.valortuberculina," +
																"ficha.tipotuberculosis," +
																"ficha.cicatrizvacuna," +
																"ficha.fuentecontagio," +
																"ficha.metodohallazgo," +
																"ficha.otrometodo," +
																"ficha.asociacionvih," +
																"ficha.asesoriavih," +
																"ficha.observaciones, "+
																"ficha.realizoCultivo," +
																"ficha.otroTipoTuberculosis, " +
																"ficha.fuenteContagio2, " +														
    															
																"per.numero_identificacion, " +
																"per.tipo_identificacion AS tipoId " +
																															
															"FROM " +
																"epidemiologia.vigifichatuberculosis ficha, " +
																"personas per " +
															"WHERE " +
															
																"ficha.fechaDiligenciamiento >= ? " +
																"AND ficha.fechaDiligenciamiento <= ? " +																
																"AND per.codigo=ficha.codigoPaciente ";
    
    
    
    
    
    
    

    private static final String consultaCasosDifteriaStr = "SELECT " +
	    														"ficha.codigoFichaDifteria, " +
																"ficha.acronimo, " +
																"ficha.nombrePadre," +
																"ficha.fechaInvestigacion," +
																"ficha.casoIdentificadoPor," +
																"ficha.contactoCasoConfirmado," +
																"ficha.carneVacunacion," +
																"ficha.dosisAplicadas," +
																"ficha.tipoVacuna," +
																"ficha.fechaUltimaDosis," +
																"ficha.fiebre," +
																"ficha.amigdalitis," +
																"ficha.faringitis," +
																"ficha.laringitis," +
																"ficha.membranas," +
																"ficha.complicaciones," +
																"ficha.tipoComplicacion," +
																"ficha.tratAntibiotico," +
																"ficha.tipoAntibiotico," +
																"ficha.duracionTratamiento," +
																"ficha.antitoxina," +
																"ficha.dosisAntitoxina," +
																"ficha.fechaAplicacionAntitox," +
																"ficha.investigacionCampo," +
																"ficha.fechaOperacionBarrido," +
																"ficha.numeroContactos," +
																"ficha.quimioprofilaxis," +
																"ficha.poblacionGrupo1," +
																"ficha.poblacionGrupo2," +
																"ficha.poblacionGrupo3," +
																"ficha.dosisDpt1Grupo1," +
																"ficha.dosisDpt1Grupo2," +
																"ficha.dosisDpt1Grupo3," +
																"ficha.dosisDpt2Grupo1," +
																"ficha.dosisDpt2Grupo2," +
																"ficha.dosisDpt2Grupo3," +  
																"ficha.dosisDpt3Grupo1," +
																"ficha.dosisDpt3Grupo2," +
																"ficha.dosisDpt3Grupo3," +
																"ficha.dosisRef1Grupo1," +
																"ficha.dosisRef1Grupo2," +
																"ficha.dosisRef1Grupo3," +
																"ficha.dosisRef2Grupo1," +
																"ficha.dosisRef2Grupo2," +
																"ficha.dosisRef2Grupo3," +											
    															
																"per.numero_identificacion, " +
																"per.tipo_identificacion AS tipoId " +
																															
															"FROM " +
																"epidemiologia.vigifichadifteria ficha, " +
																"personas per " +
															"WHERE " +
															
																"ficha.fechaDiligenciamiento >= ? " +
																"AND ficha.fechaDiligenciamiento <= ? " +																
																"AND per.codigo=ficha.codigoPaciente ";
    
    
    
    
    
    

    private static final String consultaCasosEasvStr = "SELECT " +
	    														"ficha.codigoFichaEasv, " +
																"ficha.acronimo, " +
																"ficha.otroHallazgo, " +
															    "ficha.tiempo, " +
															    "ficha.unidadTiempo, " +
															    "ficha.lugarVacunacion, " +
															    "ficha.codDepVacunacion, " +
															    "ficha.codMunVacunacion, " +
															    "ficha.estadoSalud, " +
															    "ficha.recibiaMedicamentos, " +
															    "ficha.medicamentos, " +
															    "ficha.antPatologicos, " +
															    "ficha.cualesAntPatologicos, " +
															    "ficha.antAlergicos, " +
															    "ficha.cualesAntAlergicos, " +
															    "ficha.antEasv, " +
															    "ficha.cualesAntEasv, " +
															    "ficha.biologico1, " +
															    "ficha.fabricanteMuestra1, " +
															    "ficha.loteMuestra1, " +
															    "ficha.cantidadMuestra1, " +
															    "ficha.fechaEnvioMuestra1, " +
															    "ficha.biologico2, " +
															    "ficha.fabricanteMuestra2, " +
															    "ficha.loteMuestra2, " +
															    "ficha.estadoFinal, " +									
    															
																"per.numero_identificacion, " +
																"per.tipo_identificacion AS tipoId " +
																															
															"FROM " +
																"epidemiologia.vigifichaeasv ficha, " +
																"personas per " +
															"WHERE " +
															
																"ficha.fechaDiligenciamiento >= ? " +
																"AND ficha.fechaDiligenciamiento <= ? " +																
																"AND per.codigo=ficha.codigoPaciente ";
    
    
    

	private static final String consultarVacunaEasvStr = "SELECT " +
															"codigo," +
															"vacuna," +
															"dosis," +
															"via," +
															"sitio," +
															"fechaVacunacion," +
															"fabricante," +
															"lote " +
														"FROM epidemiologia.vigiVacunasImplicadas " +
														"WHERE " +
															"codigoFicha=?";
	
	

	public static final String consultaHallazgoEasvStr = "SELECT " +
															"codigoHallazgo " +
														"FROM " +
															"epidemiologia.vigiDetalleHallazgosEasv " +
														"WHERE " +
															"codigoFichaEasv = ? ";
	
	
	
	
	
	
	

    private static final String consultaCasosHepatitisStr = "SELECT " +
	    														"ficha.codigoFichaHepatitis, " +
																"ficha.acronimo, " +
																"ficha.embarazada, " +
																"ficha.edadGestacional, " +
																"ficha.controlPrenatal, " +
																"ficha.donanteSangre, " +
																"ficha.poblacionRiesgo, " +
																"ficha.modoTransmision, " +
																"ficha.otrasIts, " +
																"ficha.vacunaAntihepatitis, " +
																"ficha.numeroDosis, " +
																"ficha.fechaPrimeraDosis, " +
																"ficha.fechaUltimaDosis, " +
																"ficha.fuenteInformacion, " +
																"ficha.tratamiento, " +
																"ficha.complicacion, " +							
    															
																"per.numero_identificacion, " +
																"per.tipo_identificacion AS tipoId " +
																															
															"FROM " +
																"epidemiologia.vigifichahepatitis ficha, " +
																"personas per " +
															"WHERE " +
															
																"ficha.fechaDiligenciamiento >= ? " +
																"AND ficha.fechaDiligenciamiento <= ? " +																
																"AND per.codigo=ficha.codigoPaciente ";
    
    
    

	public static String consultarPoblacionHepatitisStr = "SELECT codigo FROM epidemiologia.vigiDetallePobRiesgo WHERE codigoFicha=? ";	
	
    

    public static final String consultarSintomasHepatitisStr = "SELECT " +
																"codigo " +
															"FROM " +
																"epidemiologia.vigiDetalleSintHepatitis " +
															"WHERE " +
																"codigoFichaHepatitis = ? ";
    
    
    
    
    
    
    
    

    private static final String consultaCasosLeishmaniasisStr = "SELECT " +
		    														"ficha.codigoFichaLeishmaniasis, " +
																	"ficha.acronimo, " +
																	"ficha.numeroLesiones, " +
															        "ficha.cara, " +
															        "ficha.tronco, " +
															        "ficha.superiores, " +
															        "ficha.inferiores, " +
															        "ficha.cicatrices, " +
															        "ficha.tiempo, " +
															        "ficha.unidadTiempo, " +
															        "ficha.antecedenteTrauma, " +
															        "ficha.mucosaAfectada, " +
															        "ficha.rinorrea, " +
															        "ficha.epistaxis, " +
															        "ficha.obstruccion, " +
															        "ficha.disfonia, " +
															        "ficha.disfagia, " +
															        "ficha.hiperemia, " +
															        "ficha.ulceracion, " +
															        "ficha.perforacion, " +
															        "ficha.destruccion, " +
															        "ficha.fiebre, " +
															        "ficha.hepatomegalia, " +
															        "ficha.esplenomegalia, " +
															        "ficha.anemia, " +
															        "ficha.leucopenia, " +
															        "ficha.trombocitopenia, " +
															        "ficha.recibioTratamiento, " +
															        "ficha.numeroVeces, " +
															        "ficha.medicamentoRecibio, " +
															        "ficha.otroMedicamento, " +
															        "ficha.pesoPaciente, " +
															        "ficha.volumenDiario, " +
															        "ficha.diasTratamiento, " +
															        "ficha.totalAmpollas, " +					
	    															
																	"per.numero_identificacion, " +
																	"per.tipo_identificacion AS tipoId " +
																																
																"FROM " +
																	"epidemiologia.vigifichaleishmaniasis ficha, " +
																	"personas per " +
																"WHERE " +
																
																	"ficha.fechaDiligenciamiento >= ? " +
																	"AND ficha.fechaDiligenciamiento <= ? " +																
																	"AND per.codigo=ficha.codigoPaciente ";
    
    
    
    

	private static final String consultarTamLesionLeishStr = "SELECT " +
																"codigo," +
																"codigoficha," +
																"largo," +
																"ancho" +
															"FROM epidemiologia.vigiTamLesion " +
															"WHERE " +
																"codigoFicha=?";
    
    
       
	
	
	

    private static final String consultaCasosMalariaStr = "SELECT " +
	    														"ficha.codigoFichaMalaria, " +
																"ficha.acronimo, " +
																"ficha.viajo, " +
															    "ficha.codDepViajo, " +
															    "ficha.codMunViajo, " +
															    "ficha.padecioMalaria, " +
															    "ficha.fechaAproximada, " +
															    "ficha.automedicacion, " +
															    "ficha.antecedenteTrans, " +
															    "ficha.fechaAntecedente, " +
															    "ficha.tipoComplicacion, " +
															    "ficha.especiePlasmodium, " +
															    "ficha.embarazo, " +
															    "ficha.tratAntimalarico, " +
															    "ficha.cualTratamiento, " +				
    															
																"per.numero_identificacion, " +
																"per.tipo_identificacion AS tipoId " +
																															
															"FROM " +
																"epidemiologia.vigifichamalaria ficha, " +
																"personas per " +
															"WHERE " +
															
																"ficha.fechaDiligenciamiento >= ? " +
																"AND ficha.fechaDiligenciamiento <= ? " +																
																"AND per.codigo=ficha.codigoPaciente ";
    
    
    

    public static final String consultaTratamientoMalariaStr = "SELECT " +
																	"codigo " +
																"FROM " +
																	"epidemiologia.vigiDetalleTratMalaria " +
																"WHERE " +
																	"codigoFichaMalaria = ? ";
    
    
    

    public static final String consultaSintomasMalariaStr = "SELECT " +
																"codigo " +
															"FROM " +
																"epidemiologia.vigiDetalleSintMalaria " +
															"WHERE " +
																"codigoFichaMalaria = ? ";
    
    
    
    
    
    

    private static final String consultaCasosMeningitisStr = "SELECT " +
	    														"ficha.codigoFichaMeningitis, " +
																"ficha.acronimo, " +
																"ficha.vacunaAntihib, " +
															    "ficha.dosis, " +
															    "ficha.fechaPrimeraDosis, " +
															    "ficha.fechaUltimaDosis, " +
															    "ficha.tieneCarne, " +
															    "ficha.vacunaAntimenin, " +
															    "ficha.dosis2, " +
															    "ficha.fechaPrimeraDosis2, " +
															    "ficha.fechaUltimaDosis2, " +
															    "ficha.tieneCarne2, " +
															    "ficha.vacunaAntineumo, " +
															    "ficha.dosis3, " +
															    "ficha.fechaPrimeraDosis3, " +
															    "ficha.fechaUltimaDosis3, " +
															    "ficha.tieneCarne3, " +
															    "ficha.fiebre, " +
															    "ficha.rigidez, " +
															    "ficha.irritacion, " +
															    "ficha.rash, " +
															    "ficha.abombamiento, " +
															    "ficha.alteracion, " +
															    "ficha.usoAntibioticos, " +
															    "ficha.fechaUltimaDosis4, " +
															    "ficha.observaciones, " +		
    															
																"per.numero_identificacion, " +
																"per.tipo_identificacion AS tipoId " +
																															
															"FROM " +
																"epidemiologia.vigifichameningitis ficha, " +
																"personas per " +
															"WHERE " +
															
																"ficha.fechaDiligenciamiento >= ? " +
																"AND ficha.fechaDiligenciamiento <= ? " +																
																"AND per.codigo=ficha.codigoPaciente ";
    
    
    
    
    
    
    

    private static final String consultaCasosEsiStr = "SELECT " +
    														"ficha.codigoFichaEsi, " +
															"ficha.acronimo, " +
															"ficha.clasificacionInicial," +
													        "ficha.lugarTrabajo, " +
													        "ficha.vacunaEstacional," +
													        "ficha.carneVacunacion," +
													        "ficha.numeroDosis," +
													        "ficha.fechaUltimaDosis," +
													        "ficha.verificacion," +
													        "ficha.fuenteNotificacion," +
													        "ficha.viajo," +
													        "ficha.codMunViajo," +
													        "ficha.codDepViajo," +
													        "ficha.contactoAves," +
													        "ficha.contactoPersona," +
													        "ficha.casoEsporadico," +
													        "ficha.casoEpidemico," +
													        "ficha.fiebre," +
													        "ficha.dolorGarganta," +
													        "ficha.tos," +
													        "ficha.dificultadRespiratoria," +
													        "ficha.hipoxia," +
													        "ficha.taquipnea," +
													        "ficha.rinorrea," +
													        "ficha.coriza," +
													        "ficha.conjuntivitis," +
													        "ficha.cefalea," +
													        "ficha.mialgias," +
													        "ficha.postracion," +
													        "ficha.infiltrados," +
													        "ficha.dolorAbdominal," +	
															
															"per.numero_identificacion, " +
															"per.tipo_identificacion AS tipoId " +
																														
														"FROM " +
															"epidemiologia.vigifichaesi ficha, " +
															"personas per " +
														"WHERE " +
														
															"ficha.fechaDiligenciamiento >= ? " +
															"AND ficha.fechaDiligenciamiento <= ? " +																
															"AND per.codigo=ficha.codigoPaciente ";

    
    
    
    
    
    

    private static final String consultaCasosEtasStr = "SELECT " +
    														"ficha.codigoFichaEtas, " +
															"ficha.acronimo, " +
															"ficha.otroSintoma, " +
														    "ficha.horaInicioSintomas, " +
														    "ficha.asociadoBrote, " +
														    "ficha.captadoPor, " +
														    "ficha.relacionExposicion, " +
														    "ficha.tomoMuestra, " +
														    "ficha.tipoMuestra, " +
														    "ficha.cualMuestra, " +
														    "ficha.agente1, " +
														    "ficha.agente2, " +
														    "ficha.agente3, " +
														    "ficha.agente4, " +
															
															"per.numero_identificacion, " +
															"per.tipo_identificacion AS tipoId " +
																														
														"FROM " +
															"epidemiologia.vigifichaetas ficha, " +
															"personas per " +
														"WHERE " +
														
															"ficha.fechaDiligenciamiento >= ? " +
															"AND ficha.fechaDiligenciamiento <= ? " +																
															"AND per.codigo=ficha.codigoPaciente ";
    
    
    

    public static final String consultaSintomasEtasStr = "SELECT " +
															"codigo " +
														"FROM " +
															"epidemiologia.vigiDetalleSintomasEtas " +
														"WHERE " +
															"codigoFichaEtas = ? ";
    
    
    
    
    
    

    private static final String consultaCasosTosferinaStr = "SELECT " +
	    														"ficha.codigoFichaTosferina, " +
																"ficha.acronimo, " +
																"ficha.nombrePadre, " +
																"ficha.fechaInvestigacion, " +
																"ficha.identificacionCaso, " +
																"ficha.contactoCaso, " +
																"ficha.carneVacunacion, " +
																"ficha.dosisAplicadas, " +
																"ficha.tipoVacuna, " +
																"ficha.fechaUltimaDosis, " +
																"ficha.etapaEnfermedad, " +
																"ficha.tos, " +
																"ficha.duracionTos, " +
																"ficha.tosParoxistica, " +
																"ficha.estridor, " +
																"ficha.apnea, " +
																"ficha.fiebre, " +
																"ficha.vomitoPostusivo, " +
																"ficha.complicaciones, " +
																"ficha.tipoComplicacion, " +
																"ficha.tratamientoAntibiotico, " +
																"ficha.tipoAntibiotico, " +
																"ficha.duracionTratamiento, " +
																"ficha.investigacionCampo, " +
																"ficha.fechaOperacionBarrido, " +
																"ficha.numeroContactos, " +
																"ficha.quimioprofilaxis, " +
																"ficha.totalPoblacion1, " +
																"ficha.totalPoblacion2, " +
																"ficha.totalPoblacion3, " +
																"ficha.totalPoblacion4, " +
																"ficha.dosisDpt1Grupo1, " +
																"ficha.dosisDpt1Grupo2, " +
																"ficha.dosisDpt1Grupo3, " +
																"ficha.dosisDpt1Grupo4, " +
																"ficha.dosisDpt2Grupo1, " +
																"ficha.dosisDpt2Grupo2, " +
																"ficha.dosisDpt2Grupo3, " +
																"ficha.dosisDpt2Grupo4, " +
																"ficha.dosisDpt3Grupo1, " +
																"ficha.dosisDpt3Grupo2, " +
																"ficha.dosisDpt3Grupo3, " +
																"ficha.dosisDpt3Grupo4, " +
																"ficha.dosisRef1Grupo1, " +
																"ficha.dosisRef1Grupo2, " +
																"ficha.dosisRef1Grupo3, " +
																"ficha.dosisRef1Grupo4, " +
																"ficha.dosisRef2Grupo1, " +
																"ficha.dosisRef2Grupo2, " +
																"ficha.dosisRef2Grupo3, " +
																"ficha.dosisRef2Grupo4, " +
																"ficha.municipiosVacunados, " +
																
																"per.numero_identificacion, " +
																"per.tipo_identificacion AS tipoId " +
																															
															"FROM " +
																"epidemiologia.vigifichatosferina ficha, " +
																"personas per " +
															"WHERE " +
															
																"ficha.fechaDiligenciamiento >= ? " +
																"AND ficha.fechaDiligenciamiento <= ? " +																
																"AND per.codigo=ficha.codigoPaciente ";
    
    
    
    
    
    

    public static final String consultaDatosLaboratorioStr = "SELECT " +
	    														"fechaToma," +
	    														"fechaRecepcion," +
	    														"muestra," +
	    														"prueba," +
	    														"agente," +
	    														"resultado," +
	    														"fechaResultado," +
	    														"valor," +
	    														"codigofichalaboratorios " +
	    													  "FROM " +
	    													  	"epidemiologia.vigifichalaboratorios " +
	    													  "WHERE " +
	    														"codigoFicha=?";
    

    
    
    


    /**
     * Union de tablas de fichas para poder hacer la consulta de todas las fichas
     */
    private static final String strUnionFichas = "(SELECT " +
													"codigoFichaRabia as codigoFicha, " +
													"fechaDiligenciamiento, " +
													"codigoPaciente," +
													"acronimo," +
													"activa " +
												 "FROM " +
													"epidemiologia.vigiFichaRabia " +
												 "UNION " +
												 "SELECT " +
												 	"codigoFichaSarampion as codigoFicha, " +
												 	"fechaDiligenciamiento, " +
													"codigoPaciente," +
													"acronimo," +
													"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaSarampion " +
												 "UNION " +
												 "SELECT " +
												 	"codigoFichaVih as codigoFicha, " +
												 	"fechaDiligenciamiento, " +
													"codigoPaciente," +
													"acronimo," +
													"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaVih " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaDengue as codigoFicha, " +
												 	"fechaDiligenciamiento, " +
													"codigoPaciente," +
													"acronimo," +
													"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaDengue " +
												 "UNION " +
												 "SELECT " +
												 	"codigoFichaParalisis as codigoFicha, " +
												 	"fechaDiligenciamiento, " +
													"codigoPaciente," +
													"acronimo," +
													"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaParalisis " +
											 	 "UNION " +
												 "SELECT " +
												 	"codigoFichaSifilis as codigoFicha, " +
												 	"fechaDiligenciamiento, " +
													"codigoPaciente," +
													"acronimo," +
													"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaSifilis " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaTetanos as codigoFicha, " +
												 	"fechaDiligenciamiento, " +
													"codigoPaciente," +
													"acronimo," +
													"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaTetanos " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaGenerica as codigoFicha, " +
												 	"fechaDiligenciamiento, " +
													"codigoPaciente," +
													"acronimo," +
													"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaGenerica " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaTuberculosis as codigoFicha, " +
												 	"fechaDiligenciamiento, " +
													"codigoPaciente," +
													"acronimo," +
													"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaTuberculosis " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaMortalidad as codigoFicha, " +
												 	"fechaDiligenciamiento, " +
													"codigoPaciente," +
													"acronimo," +
													"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaMortalidad " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaInfecciones as codigoFicha, " +
												 	"fechaDiligenciamiento, " +
													"codigoPaciente," +
													"acronimo," +
													"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaInfecciones " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaLesiones as codigoFicha, " +
												 	"fechaDiligenciamiento, " +
													"codigoPaciente," +
													"acronimo," +
													"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaLesiones " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaIntoxicacion as codigoFicha, " +
												 	"fechaDiligenciamiento, " +
													"codigoPaciente," +
													"acronimo," +
													"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaIntoxicacion " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaRubCongenita as codigoFicha, " +
												 	"fechaDiligenciamiento, " +
													"codigoPaciente," +
													"acronimo," +
													"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaRubCongenita " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaOfidico as codigoFicha, " +
												 	"fechaDiligenciamiento, " +
													"codigoPaciente," +
													"acronimo," +
													"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaOfidico " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaLepra as codigoFicha, " +
												 	"fechaDiligenciamiento, " +
													"codigoPaciente," +
													"acronimo," +
													"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaLepra " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaDifteria as codigoFicha, " +
												 	"fechaDiligenciamiento, " +
													"codigoPaciente," +
													"acronimo," +
													"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaDifteria " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaEasv as codigoFicha, " +
												 	"fechaDiligenciamiento, " +
													"codigoPaciente," +
													"acronimo," +
													"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaEasv " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaEsi as codigoFicha, " +
												 	"fechaDiligenciamiento, " +
													"codigoPaciente," +
													"acronimo," +
													"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaEsi " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaEtas as codigoFicha, " +
												 	"fechaDiligenciamiento, " +
													"codigoPaciente," +
													"acronimo," +
													"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaEtas " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaHepatitis as codigoFicha, " +
												 	"fechaDiligenciamiento, " +
													"codigoPaciente," +
													"acronimo," +
													"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaHepatitis " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaLeishmaniasis as codigoFicha, " +
												 	"fechaDiligenciamiento, " +
													"codigoPaciente," +
													"acronimo," +
													"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaLeishmaniasis " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaMalaria as codigoFicha, " +
												 	"fechaDiligenciamiento, " +
													"codigoPaciente," +
													"acronimo," +
													"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaMalaria " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaMeningitis as codigoFicha, " +
												 	"fechaDiligenciamiento, " +
													"codigoPaciente," +
													"acronimo," +
													"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaMeningitis " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaTosferina as codigoFicha, " +
												 	"fechaDiligenciamiento, " +
													"codigoPaciente," +
													"acronimo," +
													"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaTosferina " +
											
												 ") ficha ";
    
    
    
    
    
    

    private static final String consultaLaboratoriosStr = "SELECT " +
    														"ficha.acronimo, " +
    														
    														"per.numero_identificacion, " +
    														"per.tipo_identificacion AS tipoId, " +
    														
    														"labs.fechaToma, " +
    														"labs.fechaRecepcion, " +
    														"labs.muestra, " +
    														"labs.prueba, " +
    														"labs.agente, " +
    														"labs.resultado, " +
    														"labs.fechaResultado, " +
    														"labs.valor " +
    													"FROM " +
    														"administracion.personas per, " +
    														"epidemiologia.vigifichalaboratorios labs, " +
    														strUnionFichas +
    													"WHERE " +
    														"ficha.codigoFicha = labs.codigoFicha " +
    													"AND " +
    														"per.codigo = ficha.codigoPaciente " +
    													"AND " +
    														"ficha.fechaDiligenciamiento >= ? " +
														"AND " +
															"ficha.fechaDiligenciamiento <= ? ";
    
    
    
    
    
    
    

    private static final String consultaBrotesStr = "SELECT " +
														"ficha.fechadiligenciamiento, " +
														"ficha.horadiligenciamiento, " +
														"ficha.loginusuario, " +
														"ficha.codigofichabrotes, " +
														"ficha.evento, " +
														"ficha.fechanotificacion, " +
														"ficha.pacientesgrupo1," +
														"ficha.pacientesgrupo2," +
														"ficha.pacientesgrupo3," +
														"ficha.pacientesgrupo4," +
														"ficha.pacientesgrupo5," +
														"ficha.pacientesgrupo6," +
														"ficha.probables," +
														"ficha.confirmadoslaboratorio," +
														"ficha.confirmadosclinica," +
														"ficha.confirmadosnexo," +
														"ficha.hombres," +
														"ficha.mujeres," +
														"ficha.vivos," +
														"ficha.muertos," +
														"ficha.municipioprocedencia," +
														"ficha.departamentoprocedencia " +
														
													"FROM " +
														"epidemiologia.vigifichabrotes ficha " +
													"WHERE " +
														
														"ficha.evento < 4 " +
														"AND ficha.fechaDiligenciamiento >= ? " +
														"AND ficha.fechaDiligenciamiento <= ? ";
    
    
    
    
    
    
    
    

    private static final String consultaETASColectivaStr = "SELECT " +
    															"ficha.fechaInvestigacion, " +
    															"ficha.alimentos," +
    															"ficha.muestrabiologica," +
    															"ficha.agenteBiologica1, " +
    															"ficha.agenteBiologica2, " +
    															"ficha.agenteBiologica3, " +
    															"ficha.agenteBiologica4, " +
    															"ficha.muestraalimentos," +
    															"ficha.agenteAlimentos1, " +
    															"ficha.agenteAlimentos2, " +
    															"ficha.agenteAlimentos3, " +
    															"ficha.agenteAlimentos4, " +
    															"ficha.muestraSuperficies, " +
    															"ficha.agenteSuperficies1, " +
    															"ficha.agenteSuperficies2, " +
    															"ficha.agenteSuperficies3, " +
    															"ficha.agenteSuperficies4, " +
    															"ficha.estudioManipuladores, " +
    															"ficha.agenteManipuladores1, " +
    															"ficha.agenteManipuladores2, " +
    															"ficha.agenteManipuladores3, " +
    															"ficha.agenteManipuladores4, " +
    															"ficha.lugarConsumoImplicado " +
    																															
															"FROM " +
																"epidemiologia.vigifichabrotes ficha " +
															"WHERE " +
																
																"ficha.evento = 4 " +
																"AND ficha.fechaDiligenciamiento >= ? " +
																"AND ficha.fechaDiligenciamiento <= ? ";
    
    
    
    
    
    
    public static ResultSet consultaCasosDengue(Connection con, int semana, int anyo)
    {
    	try {
    		   		
    		SemanaEpidemiologica semanaEpidemiologica = CalendarioEpidemiologico.obtenerSemanaEpidemiologica(semana,anyo);
    		
    		String fechaInicial = semanaEpidemiologica.getFechaInicial();
    		String fechaFinal = semanaEpidemiologica.getFechaFinal();
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaCasosDengueStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		
    		consulta.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
    		consulta.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
    		
    		return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de Dengue "+sqle);
			return null;
		}
    }
    
    
    
    public static ResultSet consultaHallazgosDengue(Connection con, int codigo)
    {
    	try {
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaHallazgosDengueStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            
            return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
            
            logger.error("Error consultando los hallazgos semiologicos (ficha de Dengue) "+sqle);
			return null;
        }
    }
    
    
    
    
    
    
    
    
    
    public static ResultSet consultaCasosIntoxicacion(Connection con, int semana, int anyo)
    {
    	try {
    		   		
    		SemanaEpidemiologica semanaEpidemiologica = CalendarioEpidemiologico.obtenerSemanaEpidemiologica(semana,anyo);
    		
    		String fechaInicial = semanaEpidemiologica.getFechaInicial();
    		String fechaFinal = semanaEpidemiologica.getFechaFinal();
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaCasosIntoxicacionesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		
    		consulta.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
    		consulta.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
    		
    		return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de Intoxicaciones "+sqle);
			return null;
		}
    }
    
    
    
    
    
    
    
    

    public static ResultSet consultaCasosLepra(Connection con, int semana, int anyo)
    {
    	try {
    		   		
    		SemanaEpidemiologica semanaEpidemiologica = CalendarioEpidemiologico.obtenerSemanaEpidemiologica(semana,anyo);
    		
    		String fechaInicial = semanaEpidemiologica.getFechaInicial();
    		String fechaFinal = semanaEpidemiologica.getFechaFinal();
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaCasosLepraStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		
    		consulta.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
    		consulta.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
    		
    		return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de Lepra "+sqle);
			return null;
		}
    }
    
    
    
    
    
    
    

    public static ResultSet consultaCasosParalisis(Connection con, int semana, int anyo)
    {
    	try {
    		   		
    		SemanaEpidemiologica semanaEpidemiologica = CalendarioEpidemiologico.obtenerSemanaEpidemiologica(semana,anyo);
    		
    		String fechaInicial = semanaEpidemiologica.getFechaInicial();
    		String fechaFinal = semanaEpidemiologica.getFechaFinal();
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaCasosParalisisStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		
    		consulta.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
    		consulta.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
    		
    		return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de Paralisis Flacida "+sqle);
			return null;
		}
    }
    
    

    public static ResultSet consultaExtremidadParalisis(Connection con, int codigo)
    {
    	try {
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarExtremidadStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            
            return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
            
            logger.error("Error consultando las extremidades (ficha de Parálisis flácida) "+sqle);
			return null;
        }
    }
    
    

    public static ResultSet consultarGrupoEdadParalisis(Connection con, int codigo)
    {
    	try {
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarGrupoEdadStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            
            return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
            
            logger.error("Error consultando los grupos de edad (ficha de Parálisis flácida) "+sqle);
			return null;
        }
    }
    
    
    
    
    
    
    
    
    
    

    public static ResultSet consultaCasosRabia(Connection con, int semana, int anyo)
    {
    	try {
    		   		
    		SemanaEpidemiologica semanaEpidemiologica = CalendarioEpidemiologico.obtenerSemanaEpidemiologica(semana,anyo);
    		
    		String fechaInicial = semanaEpidemiologica.getFechaInicial();
    		String fechaFinal = semanaEpidemiologica.getFechaFinal();
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaCasosRabiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		
    		consulta.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
    		consulta.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
    		
    		return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de Rabia "+sqle);
			return null;
		}
    }
    
    
    
    
    
    

    public static ResultSet consultaCasosSarampion(Connection con, int semana, int anyo)
    {
    	try {
    		   		
    		SemanaEpidemiologica semanaEpidemiologica = CalendarioEpidemiologico.obtenerSemanaEpidemiologica(semana,anyo);
    		
    		String fechaInicial = semanaEpidemiologica.getFechaInicial();
    		String fechaFinal = semanaEpidemiologica.getFechaFinal();
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaCasosSarampionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		
    		consulta.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
    		consulta.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
    		
    		return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de Sarampion "+sqle);
			return null;
		}
    }
    
    
    
    
    
    
    

    public static ResultSet consultaCasosVIH(Connection con, int semana, int anyo)
    {
    	try {
    		   		
    		SemanaEpidemiologica semanaEpidemiologica = CalendarioEpidemiologico.obtenerSemanaEpidemiologica(semana,anyo);
    		
    		String fechaInicial = semanaEpidemiologica.getFechaInicial();
    		String fechaFinal = semanaEpidemiologica.getFechaFinal();
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaCasosVIHStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		
    		consulta.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
    		consulta.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
    		
    		return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de VIH "+sqle);
			return null;
		}
    }
    
    
    

    public static ResultSet consultarMecanismoVIH(Connection con, int codigo)
    {
    	try {
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaMecanismosVIHStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            
            return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
            
            logger.error("Error consultando los mecanismos de transmision (ficha de VIH) "+sqle);
			return null;
        }
    }
    
    
    
    

    public static ResultSet consultarEnfermedadesVIH(Connection con, int codigo)
    {
    	try {
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaEnfermedadesAsociadasVIHStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            
            return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
            
            logger.error("Error consultando las enfermedades asociadas (ficha de VIH) "+sqle);
			return null;
        }
    }
    
    
    
    

    public static ResultSet consultaCasosOfidico(Connection con, int semana, int anyo)
    {
    	try {
    		   		
    		SemanaEpidemiologica semanaEpidemiologica = CalendarioEpidemiologico.obtenerSemanaEpidemiologica(semana,anyo);
    		
    		String fechaInicial = semanaEpidemiologica.getFechaInicial();
    		String fechaFinal = semanaEpidemiologica.getFechaFinal();
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaCasosAcciOfidicoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		
    		consulta.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
    		consulta.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
    		
    		return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de Sarampion "+sqle);
			return null;
		}
    }
    
    
    

    public static ResultSet consultarManiLocalesOfidico(Connection con, int codigo)
    {
    	try {
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaManiLocalStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            
            return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
            
            logger.error("Error consultando las manifestaciones locales (ficha de Accidente Ofidico) "+sqle);
			return null;
        }
    }
    
    

    public static ResultSet consultarManiSistemicaOfidico(Connection con, int codigo)
    {
    	try {
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaManiSistemicaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            
            return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
            
            logger.error("Error consultando las manifestaciones sistemicas (ficha de Accidente Ofidico) "+sqle);
			return null;
        }
    }
    
    
    

    public static ResultSet consultarCompliLocalesOfidico(Connection con, int codigo)
    {
    	try {
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaCompliLocalStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            
            return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
            
            logger.error("Error consultando las complicaciones locales (ficha de Accidente Ofidico) "+sqle);
			return null;
        }
    }
    
    

    public static ResultSet consultarCompliSistemicaOfidico(Connection con, int codigo)
    {
    	try {
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaCompliSistemicaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            
            return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
            
            logger.error("Error consultando las complicaciones sistemicas (ficha de Accidente Ofidico) "+sqle);
			return null;
        }
    }
    
    
    
    
    
    
    
    
    

    public static ResultSet consultaCasosMortalidad(Connection con, int semana, int anyo)
    {
    	try {
    		   		
    		SemanaEpidemiologica semanaEpidemiologica = CalendarioEpidemiologico.obtenerSemanaEpidemiologica(semana,anyo);
    		
    		String fechaInicial = semanaEpidemiologica.getFechaInicial();
    		String fechaFinal = semanaEpidemiologica.getFechaFinal();
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaCasosMortalidadStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		
    		consulta.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
    		consulta.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
    		
    		return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de Sarampion "+sqle);
			return null;
		}
    }
    
    
    

    public static ResultSet consultarAntecedentesMortalidad(Connection con, int codigo)
    {
    	try {
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaAntecedentesMortalidadStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            
            return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
            
            logger.error("Error consultando los antecedentes de la ficha de mortalidad "+sqle);
			return null;
        }
    }
    
    
    

    public static ResultSet consultarComplicacionesMortalidad(Connection con, int codigo)
    {
    	try {
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaComplicacionesMortalidadStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            
            return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
            
            logger.error("Error consultando las complicaciones de la ficha de mortalidad "+sqle);
			return null;
        }
    }
    
    
    
    
    
    

    public static ResultSet consultaCasosRubCongenita(Connection con, int semana, int anyo)
    {
    	try {
    		   		
    		SemanaEpidemiologica semanaEpidemiologica = CalendarioEpidemiologico.obtenerSemanaEpidemiologica(semana,anyo);
    		
    		String fechaInicial = semanaEpidemiologica.getFechaInicial();
    		String fechaFinal = semanaEpidemiologica.getFechaFinal();
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaCasosRubCongenitaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		
    		consulta.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
    		consulta.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
    		
    		return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de Rubeola Congenita "+sqle);
			return null;
		}
    }
    
    
    
    
    
    
    

    public static ResultSet consultaCasosSifilis(Connection con, int semana, int anyo)
    {
    	try {
    		   		
    		SemanaEpidemiologica semanaEpidemiologica = CalendarioEpidemiologico.obtenerSemanaEpidemiologica(semana,anyo);
    		
    		String fechaInicial = semanaEpidemiologica.getFechaInicial();
    		String fechaFinal = semanaEpidemiologica.getFechaFinal();
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaCasosSifilisStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		
    		consulta.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
    		consulta.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
    		
    		return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de Rubeola Congenita "+sqle);
			return null;
		}
    }
    
    
    
    
    
    
    
    
    

    public static ResultSet consultaCasosTetanos(Connection con, int semana, int anyo)
    {
    	try {
    		   		
    		SemanaEpidemiologica semanaEpidemiologica = CalendarioEpidemiologico.obtenerSemanaEpidemiologica(semana,anyo);
    		
    		String fechaInicial = semanaEpidemiologica.getFechaInicial();
    		String fechaFinal = semanaEpidemiologica.getFechaFinal();
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaCasosTetanosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		
    		consulta.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
    		consulta.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
    		
    		return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de Tetanos "+sqle);
			return null;
		}
    }
    
    
    
    
    
    
    
    

    public static ResultSet consultaCasosTuberculosis(Connection con, int semana, int anyo)
    {
    	try {
    		   		
    		SemanaEpidemiologica semanaEpidemiologica = CalendarioEpidemiologico.obtenerSemanaEpidemiologica(semana,anyo);
    		
    		String fechaInicial = semanaEpidemiologica.getFechaInicial();
    		String fechaFinal = semanaEpidemiologica.getFechaFinal();
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaCasosTuberculosisStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		
    		consulta.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
    		consulta.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
    		
    		return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de Tuberculosis "+sqle);
			return null;
		}
    }
    
    
    
    
    
    
    
    

    public static ResultSet consultaCasosDifteria(Connection con, int semana, int anyo)
    {
    	try {
    		   		
    		SemanaEpidemiologica semanaEpidemiologica = CalendarioEpidemiologico.obtenerSemanaEpidemiologica(semana,anyo);
    		
    		String fechaInicial = semanaEpidemiologica.getFechaInicial();
    		String fechaFinal = semanaEpidemiologica.getFechaFinal();
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaCasosDifteriaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		
    		consulta.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
    		consulta.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
    		
    		return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de Difteria "+sqle);
			return null;
		}
    }
    
    
    
    
    
    
    
    

    public static ResultSet consultaCasosEasv(Connection con, int semana, int anyo)
    {
    	try {
    		   		
    		SemanaEpidemiologica semanaEpidemiologica = CalendarioEpidemiologico.obtenerSemanaEpidemiologica(semana,anyo);
    		
    		String fechaInicial = semanaEpidemiologica.getFechaInicial();
    		String fechaFinal = semanaEpidemiologica.getFechaFinal();
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaCasosEasvStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		
    		consulta.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
    		consulta.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
    		
    		return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de EASV "+sqle);
			return null;
		}
    }
    
    
    
    

    public static ResultSet consultarVacunasEasv(Connection con, int codigo)
    {
    	try {
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarVacunaEasvStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            
            return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
            
            logger.error("Error consultando las vacunas de la ficha de EASV "+sqle);
			return null;
        }
    }
    
    
    
    

    public static ResultSet consultarHallazgosEasv(Connection con, int codigo)
    {
    	try {
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaHallazgoEasvStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            
            return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
            
            logger.error("Error consultando los hallazgos de la ficha de EASV "+sqle);
			return null;
        }
    }
    
    
    
    
    
    

    public static ResultSet consultaCasosHepatitis(Connection con, int semana, int anyo)
    {
    	try {
    		   		
    		SemanaEpidemiologica semanaEpidemiologica = CalendarioEpidemiologico.obtenerSemanaEpidemiologica(semana,anyo);
    		
    		String fechaInicial = semanaEpidemiologica.getFechaInicial();
    		String fechaFinal = semanaEpidemiologica.getFechaFinal();
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaCasosHepatitisStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		
    		consulta.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
    		consulta.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
    		
    		return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de Hepatitis "+sqle);
			return null;
		}
    }
    
    
    

    public static ResultSet consultarPoblacionHepatitis(Connection con, int codigo)
    {
    	try {
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarPoblacionHepatitisStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            
            return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
            
            logger.error("Error consultando la poblacion de la ficha Hepatitis "+sqle);
			return null;
        }
    }
    
    
    

    public static ResultSet consultarSintomasHepatitis(Connection con, int codigo)
    {
    	try {
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarSintomasHepatitisStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            
            return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
            
            logger.error("Error consultando los sintomas de la ficha Hepatitis "+sqle);
			return null;
        }
    }
    
    
    
    
    
    
    
    
    

    public static ResultSet consultaCasosLeishmaniasis(Connection con, int semana, int anyo)
    {
    	try {
    		   		
    		SemanaEpidemiologica semanaEpidemiologica = CalendarioEpidemiologico.obtenerSemanaEpidemiologica(semana,anyo);
    		
    		String fechaInicial = semanaEpidemiologica.getFechaInicial();
    		String fechaFinal = semanaEpidemiologica.getFechaFinal();
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaCasosLeishmaniasisStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		
    		consulta.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
    		consulta.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
    		
    		return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de Leishmaniasis "+sqle);
			return null;
		}
    }
    
    
    

    public static ResultSet consultarTamLesionLeish(Connection con, int codigo)
    {
    	try {
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarTamLesionLeishStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            
            return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
            
            logger.error("Error consultando las lesiones de la ficha de Leishmaniasis "+sqle);
			return null;
        }
    }
    
    
    
    

    public static ResultSet consultaCasosMalaria(Connection con, int semana, int anyo)
    {
    	try {
    		   		
    		SemanaEpidemiologica semanaEpidemiologica = CalendarioEpidemiologico.obtenerSemanaEpidemiologica(semana,anyo);
    		
    		String fechaInicial = semanaEpidemiologica.getFechaInicial();
    		String fechaFinal = semanaEpidemiologica.getFechaFinal();
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaCasosMalariaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		
    		consulta.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
    		consulta.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
    		
    		return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de Malaria "+sqle);
			return null;
		}
    }
    
    
    

    public static ResultSet consultarSintomasMalaria(Connection con, int codigo)
    {
    	try {
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaSintomasMalariaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            
            return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
            
            logger.error("Error consultando los sintomas de la ficha de Malaria "+sqle);
			return null;
        }
    }
    
    
    
    

    public static ResultSet consultarTratamientoMalaria(Connection con, int codigo)
    {
    	try {
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaTratamientoMalariaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            
            return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
            
            logger.error("Error consultando los tratamientos de la ficha de Malaria "+sqle);
			return null;
        }
    }
    
    
    
    
    

    public static ResultSet consultaCasosMeningitis(Connection con, int semana, int anyo)
    {
    	try {
    		   		
    		SemanaEpidemiologica semanaEpidemiologica = CalendarioEpidemiologico.obtenerSemanaEpidemiologica(semana,anyo);
    		
    		String fechaInicial = semanaEpidemiologica.getFechaInicial();
    		String fechaFinal = semanaEpidemiologica.getFechaFinal();
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaCasosMeningitisStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		
    		consulta.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
    		consulta.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
    		
    		return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de Meningitis "+sqle);
			return null;
		}
    }
    
    
    
    
    

    public static ResultSet consultaCasosEsi(Connection con, int semana, int anyo)
    {
    	try {
    		   		
    		SemanaEpidemiologica semanaEpidemiologica = CalendarioEpidemiologico.obtenerSemanaEpidemiologica(semana,anyo);
    		
    		String fechaInicial = semanaEpidemiologica.getFechaInicial();
    		String fechaFinal = semanaEpidemiologica.getFechaFinal();
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaCasosEsiStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		
    		consulta.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
    		consulta.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
    		
    		return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de ESI "+sqle);
			return null;
		}
    }
    
    
    
    
    
    

    public static ResultSet consultaCasosEtas(Connection con, int semana, int anyo)
    {
    	try {
    		   		
    		SemanaEpidemiologica semanaEpidemiologica = CalendarioEpidemiologico.obtenerSemanaEpidemiologica(semana,anyo);
    		
    		String fechaInicial = semanaEpidemiologica.getFechaInicial();
    		String fechaFinal = semanaEpidemiologica.getFechaFinal();
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaCasosEtasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		
    		consulta.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
    		consulta.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
    		
    		return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de ETAS (Notificación Individual) "+sqle);
			return null;
		}
    }
    
    
    
    

    public static ResultSet consultarSintomasEtas(Connection con, int codigo)
    {
    	try {
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaSintomasEtasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            
            return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
            
            logger.error("Error consultando los sintomas de la ficha de ETAs "+sqle);
			return null;
        }
    }
    
    
    
    

    public static ResultSet consultaCasosTosferina(Connection con, int semana, int anyo)
    {
    	try {
    		   		
    		SemanaEpidemiologica semanaEpidemiologica = CalendarioEpidemiologico.obtenerSemanaEpidemiologica(semana,anyo);
    		
    		String fechaInicial = semanaEpidemiologica.getFechaInicial();
    		String fechaFinal = semanaEpidemiologica.getFechaFinal();
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaCasosTosferinaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		
    		consulta.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
    		consulta.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
    		
    		return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de Tosferina (Notificación Individual) "+sqle);
			return null;
		}
    }
    
    
    
    
    
    
    

    public static ResultSet consultaLaboratorios(Connection con, int semana, int anyo)
    {
    	try {
    		   		
    		SemanaEpidemiologica semanaEpidemiologica = CalendarioEpidemiologico.obtenerSemanaEpidemiologica(semana,anyo);
    		
    		String fechaInicial = semanaEpidemiologica.getFechaInicial();
    		String fechaFinal = semanaEpidemiologica.getFechaFinal();
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaLaboratoriosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		
    		consulta.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
    		consulta.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
    		
    		return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
			
			logger.error("Error consultando los laboratorios "+sqle);
			return null;
		}
    }
    
    
    
    
    
    
    
    

    public static ResultSet consultaInfoBasica(Connection con, int semana, int anyo, String tablaFicha)
    {
    	try {
    		   		
    		SemanaEpidemiologica semanaEpidemiologica = CalendarioEpidemiologico.obtenerSemanaEpidemiologica(semana,anyo);
    		
    		String fechaInicial = semanaEpidemiologica.getFechaInicial();
    		String fechaFinal = semanaEpidemiologica.getFechaFinal();
    		
    		String consultaInfoBasicaStr = consultaInfoBasicaParte1Str + tablaFicha + consultaInfoBasicaParte2Str;
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaInfoBasicaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		
    		consulta.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
    		consulta.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
    		
    		return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
			
			logger.error("Error consultando la informacion basica de las fichas "+sqle);
			return null;
		}
    }
    
    
    
    
    

    public static ResultSet consultaBrotes(Connection con, int semana, int anyo)
    {
    	try {
    		   		
    		SemanaEpidemiologica semanaEpidemiologica = CalendarioEpidemiologico.obtenerSemanaEpidemiologica(semana,anyo);
    		
    		String fechaInicial = semanaEpidemiologica.getFechaInicial();
    		String fechaFinal = semanaEpidemiologica.getFechaFinal();
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaBrotesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		
    		consulta.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
    		consulta.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
    		
    		return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
			
			logger.error("Error consultando los brotes "+sqle);
			return null;
		}
    }
    
    
    
    
    

    public static ResultSet consultaBrotesEtas(Connection con, int semana, int anyo)
    {
    	try {
    		   		
    		SemanaEpidemiologica semanaEpidemiologica = CalendarioEpidemiologico.obtenerSemanaEpidemiologica(semana,anyo);
    		
    		String fechaInicial = semanaEpidemiologica.getFechaInicial();
    		String fechaFinal = semanaEpidemiologica.getFechaFinal();
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaETASColectivaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		
    		consulta.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
    		consulta.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
    		
    		return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
			
			logger.error("Error consultando los brotes "+sqle);
			return null;
		}
    }
}
