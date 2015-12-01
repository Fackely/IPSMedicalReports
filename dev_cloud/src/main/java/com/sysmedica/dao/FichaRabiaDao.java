/*
 * Creado en 04-ago-2005
 * 
 * Autor Santiago Salamanca
 * SysMédica
 */
package com.sysmedica.dao;

import java.sql.*;
import java.util.HashMap;

/**
 * @author santiago
 *
 */
public interface FichaRabiaDao {

    /**
     * Metodo para insertar una ficha de accidente rabico
     * @param con
     * @param codigoPaciente
     * @param codigoFichaRabia
     * @param numeroSolicitud
     * @param loginUsuario
     * @param estado
     * @param sueroAntirrabico
     * @param tipoSuero
     * @param cantidadAplicada
     * @param fechaAplicacion
     * @param vacunaAntirrabica
     * @param tipoVacuna
     * @param dosisAplicadas
     * @param fechaUltimaDosis
     * @param tipoAgresion
     * @param provocada
     * @param tipoLesion
     * @param tipoExposicion
     * @param cabeza
     * @param cara
     * @param cuello
     * @param manos
     * @param tronco
     * @param extsuperiores
     * @param extinferiores
     * @param fechaAgresion
     * @param especie
     * @param fechaInicioSintomas
     * @param fechaMuerte
     * @param fechaTomaMuestra
     * @param fuenteInformacionLaboratorio
     * @param vacunado
     * @param fechaUltimaDosisAnimal
     * @param lavadoHerida
     * @param suturaHerida
     * @param aplicacionSuero
     * @param fechaAplicacionSuero
     * @param tipoSueroTratamiento
     * @param cantidadSueroGlutea
     * @param cantidadSueroHerida
     * @param numeroLote
     * @param laboratorioProductor
     * @param aplicarVacuna
     * @param numeroDosisTratamiento
     * @param tipoVacunaTratamiento
     * @param fechaVacunaDosis1
     * @param fechaVacunaDosis2
     * @param fechaVacunaDosis3
     * @param fechaVacunaDosis4
     * @param fechaVacunaDosis5
     * @param suspensionTratamiento
     * @param razonSuspension
     * @param fechaTomaMuestraMuerte
     * @param confirmacionDiagnostica
     * @param pruebasLaboratorio
     * @return
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
    						);
    
    
    
    /**
     * Metodo para modificar una ficha de accidente rabico
     * @param con
     * @param sire
     * @param codigoPaciente
     * @param codigoFichaRabia
     * @param loginUsuario
     * @param estado
     * @param sueroAntirrabico
     * @param tipoSuero
     * @param cantidadAplicada
     * @param fechaAplicacion
     * @param vacunaAntirrabica
     * @param tipoVacuna
     * @param dosisAplicadas
     * @param fechaUltimaDosis
     * @param tipoAgresion
     * @param provocada
     * @param tipoLesion
     * @param tipoExposicion
     * @param cabeza
     * @param cara
     * @param cuello
     * @param manos
     * @param tronco
     * @param extsuperiores
     * @param extinferiores
     * @param fechaAgresion
     * @param codigoDepProcedencia
     * @param codigoMunProcedencia
     * @param fechaConsultaGeneral
     * @param fechaInicioSintomasGeneral
     * @param tipoCaso
     * @param hospitalizadoGeneral
     * @param fechaHospitalizacionGeneral
     * @param estaVivoGeneral
     * @param fechaDefuncion
     * @param codigoDepNoti
     * @param codigoMunNoti
     * @param unidadGeneradora
     * @param especie
     * @param fechaInicioSintomas
     * @param fechaMuerte
     * @param fechaTomaMuestra
     * @param fuenteInformacionLaboratorio
     * @param vacunado
     * @param fechaUltimaDosisAnimal
     * @param lavadoHerida
     * @param suturaHerida
     * @param aplicacionSuero
     * @param fechaAplicacionSuero
     * @param tipoSueroTratamiento
     * @param cantidadSueroGlutea
     * @param cantidadSueroHerida
     * @param numeroLote
     * @param laboratorioProductor
     * @param aplicarVacuna
     * @param numeroDosisTratamiento
     * @param tipoVacunaTratamiento
     * @param fechaVacunaDosis1
     * @param fechaVacunaDosis2
     * @param fechaVacunaDosis3
     * @param fechaVacunaDosis4
     * @param fechaVacunaDosis5
     * @param suspensionTratamiento
     * @param razonSuspension
     * @param fechaTomaMuestraMuerte
     * @param confirmacionDiagnostica
     * @param pruebasLaboratorio
     * @return
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
							    );
    
    
    /**
     * Metodo para consultar todos los datos de una ficha
     * @param con
     * @param codigo
     * @return
     */
    public ResultSet consultaTodoFicha(Connection con, int codigo);
    
    
    public ResultSet consultaDatosPaciente(Connection con, int codigo, boolean empezarnuevo);
    
    /**
     * Metodo para consultar las localizaciones anatomicas
     * @param con
     * @param codigo
     * @return
     */
    public ResultSet consultaLocalizacionAnatomica(Connection con, int codigo);
    
    
    
    
    
    
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
								    );
    
    
    public int terminarFicha(Connection con, int codigoFichaRabia);
}