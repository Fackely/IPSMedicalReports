/*
 * Creado en 10-ago-2005
 * 
 * Autor Santiago Salamanca
 * SysMédica
 */
package com.sysmedica.dao.postgresql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

import com.sysmedica.dao.sqlbase.SqlBaseFichaDengueDao;
import com.sysmedica.dao.sqlbase.SqlBaseFichaRabiaDao;

import com.sysmedica.dao.FichaRabiaDao;

/**
 * @author santiago
 *
 */
public class PostgresqlFichaRabiaDao implements FichaRabiaDao {
    
    private String secuenciaStr = "SELECT nextval('epidemiologia.seq_fichas')";
    
    /**
     * String con el statement para obtener el valor de la secuencia de las notificaciones (para el codigo)
     */
    private String secuenciaNotificacionesStr = "SELECT nextval('epidemiologia.seq_notificaciones')";
    
    
    /**
     * Metodo que inserta una ficha de Rabia
     */
    public int insertarFicha(Connection con,
    							String sire,
								int codigoPaciente,
								int codigoFichaRabia,
								int numeroSolicitud,
								String codigoDiagnostico,
								String loginUsuario,
								int estado,
								int codigoAseguradora,
								String nombreProfesional,
								int sueroAntirrabico,
								int tipoSuero,
							    int cantidadAplicada,
							    String fechaAplicacion,
							    int vacunaAntirrabica,
							    int tipoVacuna,
							    int dosisAplicadas,
							    String fechaUltimaDosis,
							    int tipoAgresion,
							    boolean provocada,
							    int tipoLesion,
							    int tipoExposicion,
							    boolean cabeza,
							    boolean cara,
							    boolean cuello,
							    boolean manos,
							    boolean tronco,
							    boolean extsuperiores,
							    boolean extinferiores,
							    String fechaAgresion,
							    
								// Elementos del Animal Agresor        															    						   
							    int especie,
							    String fechaInicioSintomas,
							    String fechaMuerte,
							    String fechaTomaMuestra,
							    int fuenteInformacionLaboratorio,
							    boolean vacunado,
							    String fechaUltimaDosisAnimal,
							    
							    
							    // Elementos del Tratamiento Antirrabico
							    boolean lavadoHerida,
							    boolean suturaHerida,
							    boolean aplicacionSuero,
							    String fechaAplicacionSuero,
							    int tipoSueroTratamiento,
							    int cantidadSueroGlutea,
							    int cantidadSueroHerida,
							    String numeroLote,
							    String laboratorioProductor,
							    boolean aplicarVacuna,
							    int numeroDosisTratamiento,
							    int tipoVacunaTratamiento,
							    String fechaVacunaDosis1,
							    String fechaVacunaDosis2,
							    String fechaVacunaDosis3,
							    String fechaVacunaDosis4,
							    String fechaVacunaDosis5,
							    boolean suspensionTratamiento,
							    int razonSuspension,
							    String fechaTomaMuestraMuerte,
							    boolean confirmacionDiagnostica,
							    int pruebasLaboratorio   
							)
	{
    	return SqlBaseFichaRabiaDao.insertarFicha(con,
									numeroSolicitud,
									loginUsuario,
									codigoPaciente,
									codigoDiagnostico,
									estado,	
									codigoAseguradora,
									nombreProfesional,
								    secuenciaStr);
	}

    
    
    /**
     * Metodo para modificar una ficha de vigilancia epidemiologica de accidente rabico
     */
    public int modificarFicha(Connection con,
    							String sire,
								int codigoPaciente,
								int codigoFichaRabia,
								String loginUsuario,
								int estado,
								int sueroAntirrabico,
								int tipoSuero,
							    int cantidadAplicada,
							    String fechaAplicacion,
							    int vacunaAntirrabica,
							    int tipoVacuna,
							    int dosisAplicadas,
							    String fechaUltimaDosis,
							    int tipoAgresion,
							    boolean provocada,
							    int tipoLesion,
							    int tipoExposicion,
							    boolean cabeza,
							    boolean cara,
							    boolean cuello,
							    boolean manos,
							    boolean tronco,
							    boolean extsuperiores,
							    boolean extinferiores,
							    String fechaAgresion,
							    int confDiagnosticaCasoRabia,
							    String fechaMuestraCasoRabia,
							    String pais,
							    int areaProcedencia,
							    
							    String lugarProcedencia,
							    String fechaConsultaGeneral,
							    String fechaInicioSintomasGeneral,
							    int tipoCaso,
							    boolean hospitalizadoGeneral,
							    String fechaHospitalizacionGeneral,
							    boolean estaVivoGeneral,
							    String fechaDefuncion,
							    String lugarNoti,
							    int unidadGeneradora,
							    
								// Elementos del Animal Agresor        															    						   
							    int especie,
							    String fechaInicioSintomas,
							    String fechaMuerte,
							    String fechaTomaMuestra,
							    int fuenteInformacionLaboratorio,
							    boolean vacunado,
							    String fechaUltimaDosisAnimal,
							    String nombrePropietario,
							    String direccionPropietario,
							    int estadoMomentoAgresion,
							    int ubicacionAnimal,
							    int numeroDiasObserva,
							    int lugarObservacion,
							    int estadoAnimalObserva,
							    int confirmacionDiagnosticaAnimal,
							    
							    
							    // Elementos del Tratamiento Antirrabico
							    boolean lavadoHerida,
							    boolean suturaHerida,
							    boolean aplicacionSuero,
							    String fechaAplicacionSuero,
							    int tipoSueroTratamiento,
							    int cantidadSueroGlutea,
							    int cantidadSueroHerida,
							    String numeroLote,
							    String laboratorioProductor,
							    boolean aplicarVacuna,
							    int numeroDosisTratamiento,
							    int tipoVacunaTratamiento,
							    String fechaVacunaDosis1,
							    String fechaVacunaDosis2,
							    String fechaVacunaDosis3,
							    String fechaVacunaDosis4,
							    String fechaVacunaDosis5,
							    boolean suspensionTratamiento,
							    int razonSuspension,
							    String fechaTomaMuestraMuerte,
							    boolean confirmacionDiagnostica,
							    int pruebasLaboratorio,
							    int profundidadLesion,
							    int reaccionesVacunaSuero,
							    int evolucionPaciente
							    )
    {
    	return SqlBaseFichaRabiaDao.modificarFicha(con,
    												sire,
													codigoPaciente,
													codigoFichaRabia,
													loginUsuario,
													estado,
													sueroAntirrabico,
													tipoSuero,
												    cantidadAplicada,
												    fechaAplicacion,
												    vacunaAntirrabica,
												    tipoVacuna,
												    dosisAplicadas,
												    fechaUltimaDosis,
												    tipoAgresion,
												    provocada,
												    tipoLesion,
												    tipoExposicion,
												    cabeza,
												    cara,
												    cuello,
												    manos,
												    tronco,
												    extsuperiores,
												    extinferiores,
												    fechaAgresion,
												    confDiagnosticaCasoRabia,
												    fechaMuestraCasoRabia,
												    pais,
												    areaProcedencia,
												    
												    lugarProcedencia,
												    fechaConsultaGeneral,
												    fechaInicioSintomasGeneral,
												    tipoCaso,
												    hospitalizadoGeneral,
												    fechaHospitalizacionGeneral,
												    estaVivoGeneral,
												    fechaDefuncion,
												    lugarNoti,
												    unidadGeneradora,
												    
													// Elementos del Animal Agresor        															    						   
												    especie,
												    fechaInicioSintomas,
												    fechaMuerte,
												    fechaTomaMuestra,
												    fuenteInformacionLaboratorio,
												    vacunado,
												    fechaUltimaDosisAnimal,
												    nombrePropietario,
												    direccionPropietario,
												    estadoMomentoAgresion,
												    ubicacionAnimal,
												    numeroDiasObserva,
												    lugarObservacion,
												    estadoAnimalObserva,
												    confirmacionDiagnosticaAnimal,
												    
												    
												    // Elementos del Tratamiento Antirrabico
												    lavadoHerida,
												    suturaHerida,
												    aplicacionSuero,
												    fechaAplicacionSuero,
												    tipoSueroTratamiento,
												    cantidadSueroGlutea,
												    cantidadSueroHerida,
												    numeroLote,
												    laboratorioProductor,
												    aplicarVacuna,
												    numeroDosisTratamiento,
												    tipoVacunaTratamiento,
												    fechaVacunaDosis1,
												    fechaVacunaDosis2,
												    fechaVacunaDosis3,
												    fechaVacunaDosis4,
												    fechaVacunaDosis5,
												    suspensionTratamiento,
												    razonSuspension,
												    fechaTomaMuestraMuerte,
												    confirmacionDiagnostica,
												    pruebasLaboratorio,
												    profundidadLesion,
												    reaccionesVacunaSuero,
												    evolucionPaciente);
	}
    
    
    
    /**
     * Metodo que consulta todos los datos de una ficha de Rabia
     */
    public ResultSet consultaTodoFicha(Connection con, int codigo) {
        
        return SqlBaseFichaRabiaDao.consultarTodoFichaRabia(con,codigo);
    }
    
    
    
    public ResultSet consultaDatosPaciente(Connection con, int codigo,boolean empezarnuevo)
    {
    	return SqlBaseFichaRabiaDao.consultarDatosPaciente(con,codigo,empezarnuevo);
    }
    
    
    
    /**
     * Metodo para consultar las localizaciones anatomicas para la ficha de accidente rabico
     */
    public ResultSet consultaLocalizacionAnatomica(Connection con, int codigo) {
        
        return SqlBaseFichaRabiaDao.consultarLocalizacionAnatomica(con,codigo);
    }
    
    
    
    
    
    
    
    public int insertarFichaCompleta(Connection con,
							    		String login,
										int codigoPaciente,
										String codigoDiagnostico,
										int estado,
										int codigoAseguradora,
										String nombreProfesional,
									    								    
									    String sire,
										int sueroAntirrabico,
										int tipoSuero,
									    int cantidadAplicada,
									    String fechaAplicacion,
									    int vacunaAntirrabica,
									    int tipoVacuna,
									    int dosisAplicadas,
									    String fechaUltimaDosis,
									    int tipoAgresion,
									    boolean provocada,
									    int tipoLesion,
									    int tipoExposicion,
									    boolean cabeza,
									    boolean cara,
									    boolean cuello,
									    boolean manos,
									    boolean tronco,
									    boolean extsuperiores,
									    boolean extinferiores,
									    String fechaAgresion,
									    int confDiagnosticaCasoRabia,
									    String fechaMuestraCasoRabia,
									    String pais,
									    int areaProcedencia,
									    
									    String lugarProcedencia,
									    String fechaConsultaGeneral,
									    String fechaInicioSintomasGeneral,
									    int tipoCaso,
									    boolean hospitalizadoGeneral,
									    String fechaHospitalizacionGeneral,
									    boolean estaVivoGeneral,
									    String fechaDefuncion,
									    String lugarNoti,
									    int unidadGeneradora,
									    
										// Elementos del Animal Agresor        															    						   
									    int especie,
									    String fechaInicioSintomas,
									    String fechaMuerte,
									    String fechaTomaMuestra,
									    int fuenteInformacionLaboratorio,
									    boolean vacunado,
									    String fechaUltimaDosisAnimal,
									    String nombrePropietario,
									    String direccionPropietario,
									    int estadoMomentoAgresion,
									    int ubicacionAnimal,
									    int numeroDiasObserva,
									    int lugarObservacion,
									    int estadoAnimalObserva,
									    int confirmacionDiagnosticaAnimal,
									    
									    
									    // Elementos del Tratamiento Antirrabico
									    boolean lavadoHerida,
									    boolean suturaHerida,
									    boolean aplicacionSuero,
									    String fechaAplicacionSuero,
									    int tipoSueroTratamiento,
									    int cantidadSueroGlutea,
									    int cantidadSueroHerida,
									    String numeroLote,
									    String laboratorioProductor,
									    boolean aplicarVacuna,
									    int numeroDosisTratamiento,
									    int tipoVacunaTratamiento,
									    String fechaVacunaDosis1,
									    String fechaVacunaDosis2,
									    String fechaVacunaDosis3,
									    String fechaVacunaDosis4,
									    String fechaVacunaDosis5,
									    boolean suspensionTratamiento,
									    int razonSuspension,
									    String fechaTomaMuestraMuerte,
									    boolean confirmacionDiagnostica,
									    int pruebasLaboratorio,
									    boolean activa,
									    int profundidadLesion,
									    int reaccionesVacunaSuero,
									    int evolucionPaciente
									    )
    {
    	return SqlBaseFichaRabiaDao.insertarFichaCompleta(con,
												    		login,
															codigoPaciente,
															codigoDiagnostico,
															estado,
															codigoAseguradora,
															nombreProfesional,
														    
														    secuenciaStr,
														    
														    sire,
															sueroAntirrabico,
															tipoSuero,
														    cantidadAplicada,
														    fechaAplicacion,
														    vacunaAntirrabica,
														    tipoVacuna,
														    dosisAplicadas,
														    fechaUltimaDosis,
														    tipoAgresion,
														    provocada,
														    tipoLesion,
														    tipoExposicion,
														    cabeza,
														    cara,
														    cuello,
														    manos,
														    tronco,
														    extsuperiores,
														    extinferiores,
														    fechaAgresion,
														    confDiagnosticaCasoRabia,
														    fechaMuestraCasoRabia,
														    pais,
														    areaProcedencia,
														    
														    lugarProcedencia,
														    fechaConsultaGeneral,
														    fechaInicioSintomasGeneral,
														    tipoCaso,
														    hospitalizadoGeneral,
														    fechaHospitalizacionGeneral,
														    estaVivoGeneral,
														    fechaDefuncion,
														    lugarNoti,
														    unidadGeneradora,
														    
															// Elementos del Animal Agresor        															    						   
														    especie,
														    fechaInicioSintomas,
														    fechaMuerte,
														    fechaTomaMuestra,
														    fuenteInformacionLaboratorio,
														    vacunado,
														    fechaUltimaDosisAnimal,
														    nombrePropietario,
														    direccionPropietario,
														    estadoMomentoAgresion,
														    ubicacionAnimal,
														    numeroDiasObserva,
														    lugarObservacion,
														    estadoAnimalObserva,
														    confirmacionDiagnosticaAnimal,
														    														    
														    // Elementos del Tratamiento Antirrabico
														    lavadoHerida,
														    suturaHerida,
														    aplicacionSuero,
														    fechaAplicacionSuero,
														    tipoSueroTratamiento,
														    cantidadSueroGlutea,
														    cantidadSueroHerida,
														    numeroLote,
														    laboratorioProductor,
														    aplicarVacuna,
														    numeroDosisTratamiento,
														    tipoVacunaTratamiento,
														    fechaVacunaDosis1,
														    fechaVacunaDosis2,
														    fechaVacunaDosis3,
														    fechaVacunaDosis4,
														    fechaVacunaDosis5,
														    suspensionTratamiento,
														    razonSuspension,
														    fechaTomaMuestraMuerte,
														    confirmacionDiagnostica,
														    pruebasLaboratorio,
														    activa,
														    profundidadLesion,
														    reaccionesVacunaSuero,
														    evolucionPaciente);
    }
    
    
    
    public int terminarFicha(Connection con, int codigoFichaRabia)
	{
		return SqlBaseFichaDengueDao.terminarFicha(con,codigoFichaRabia);
	}    
}
