package com.sysmedica.dao;

import java.sql.Connection;
import java.sql.ResultSet;

public interface FichaTetanosDao {

	/**
	 * Metodo para insertar una ficha de Tetanos Neonatal
	 * @param con
	 * @param notificar
	 * @param loginUsuario
	 * @param codigoFichaSifilis
	 * @param codigoPaciente
	 * @param codigoDiagnostico
	 * @param codigoNotificacion
	 * @param numeroSolicitud
	 * @param estado
	 * @return
	 */
	public int insertarFicha(Connection con,
								boolean notificar,
							    String loginUsuario,
							    int codigoFichaTetanos,
							    int codigoPaciente,
							    String codigoDiagnostico,
							    int codigoNotificacion,
							    int numeroSolicitud,
							    int codigoAseguradora,
								String nombreProfesional,
							    int estado
							    );
	
	
	public int modificarFicha(Connection con,
								String sire,
								String loginUsuario,
							    int codigoFichaTetanos,
							    int estado,
							    
							    String nombreMadre,
							    int edadMadre,
							    String fechaNacimientoMadre,
							    String fechaEgresoHospital,
							    boolean nacimientoTraumatico,
							    boolean llantoNacer,
							    boolean mamabaNormal,
							    boolean dejoMamar,
							    String fechaDejo,
							    boolean dificultadRespiratoria,
							    boolean episodiosApnea,
							    boolean hipotermia,
							    boolean hipertermia,
							    boolean fontAbombada,
							    boolean rigidezNuca,
							    boolean trismus,
							    boolean convulsiones,
							    boolean espasmos,
							    boolean contracciones,
							    boolean opistotonos,
							    boolean llantoExcesivo,
							    boolean sepsisUmbilical,
							    int numeroEmbarazos,
							    boolean asistioControl,
							    String explicacionNoAsistencia,
							    boolean atendidoPorMedico,
							    boolean atendidoPorEnfermero,
							    boolean atendidoPorAuxiliar,
							    boolean atendidoPorPromotor,
							    boolean atendidoPorOtro,
							    String quienAtendio,
							    int numeroControlesPrevios,
							    String fechaUltimoControl,
							    boolean madreVivioMismoLugar,
							    String codigoMunicipioVivienda,
							    String codigoDepartamentoVivienda,
							    String lugarVivienda,
							    boolean antecedenteVacunaAnti,
							    int dosisDpt,
							    String explicacionNoVacuna,
							    String fechaDosisTd1,
							    String fechaDosisTd2,
							    String fechaDosisTd3,
							    String fechaDosisTd4,
							    int lugarParto,
							    String institucionParto,
							    String fechaIngresoParto,
							    String fechaEgresoParto,
							    int quienAtendioParto,
							    String instrumentoCordon,
							    String metodoEsterilizacion,
							    boolean recibioInformacionMunon,
							    boolean aplicacionSustanciasMunon,
							    String cualesSustancias,
							    int distanciaMinutos,
							    String fechaInvestigacionCampo,
							    String fechaVacunacion,
							    int dosisTd1AplicadasMef,
							    int dosisTd2AplicadasMef,
							    int dosisTd3AplicadasMef,
							    int dosisTd4AplicadasMef,
							    int dosisTd5AplicadasMef,
							    int dosisTd1AplicadasGest,
							    int dosisTd2AplicadasGest,
							    int dosisTd3AplicadasGest,
							    int dosisTd4AplicadasGest,
							    int dosisTd5AplicadasGest,
							    int coberturaLograda,
							    
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
							    String pais,
							    int areaProcedencia
							    );
	
	
	public ResultSet consultaTodoFicha(Connection con, int codigo);
	
	
	
	public ResultSet consultaDatosPaciente(Connection con, int codigo,boolean empezarnuevo);
	
	
	public int insertarFichaCompleta(Connection con,
											int numeroSolicitud,
											String login,
											int codigoPaciente,
											String codigoDiagnostico,
											int estado,
											int codigoAseguradora,
											String nombreProfesional,
										    
										    String sire,
										    
										    String nombreMadre,
										    int edadMadre,
										    String fechaNacimientoMadre,
										    String fechaEgresoHospital,
										    boolean nacimientoTraumatico,
										    boolean llantoNacer,
										    boolean mamabaNormal,
										    boolean dejoMamar,
										    String fechaDejo,
										    boolean dificultadRespiratoria,
										    boolean episodiosApnea,
										    boolean hipotermia,
										    boolean hipertermia,
										    boolean fontAbombada,
										    boolean rigidezNuca,
										    boolean trismus,
										    boolean convulsiones,
										    boolean espasmos,
										    boolean contracciones,
										    boolean opistotonos,
										    boolean llantoExcesivo,
										    boolean sepsisUmbilical,
										    int numeroEmbarazos,
										    boolean asistioControl,
										    String explicacionNoAsistencia,
										    boolean atendidoPorMedico,
										    boolean atendidoPorEnfermero,
										    boolean atendidoPorAuxiliar,
										    boolean atendidoPorPromotor,
										    boolean atendidoPorOtro,
										    String quienAtendio,
										    int numeroControlesPrevios,
										    String fechaUltimoControl,
										    boolean madreVivioMismoLugar,
										    String codigoMunicipioVivienda,
										    String codigoDepartamentoVivienda,
										    String lugarVivienda,
										    boolean antecedenteVacunaAnti,
										    int dosisDpt,
										    String explicacionNoVacuna,
										    String fechaDosisTd1,
										    String fechaDosisTd2,
										    String fechaDosisTd3,
										    String fechaDosisTd4,
										    int lugarParto,
										    String institucionParto,
										    String fechaIngresoParto,
										    String fechaEgresoParto,
										    int quienAtendioParto,
										    String instrumentoCordon,
										    String metodoEsterilizacion,
										    boolean recibioInformacionMunon,
										    boolean aplicacionSustanciasMunon,
										    String cualesSustancias,
										    int distanciaMinutos,
										    String fechaInvestigacionCampo,
										    String fechaVacunacion,
										    int dosisTd1AplicadasMef,
										    int dosisTd2AplicadasMef,
										    int dosisTd3AplicadasMef,
										    int dosisTd4AplicadasMef,
										    int dosisTd5AplicadasMef,
										    int dosisTd1AplicadasGest,
										    int dosisTd2AplicadasGest,
										    int dosisTd3AplicadasGest,
										    int dosisTd4AplicadasGest,
										    int dosisTd5AplicadasGest,
										    int coberturaLograda,
										    
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
										    boolean activa,
										    String pais,
										    int areaProcedencia
										   );
}


