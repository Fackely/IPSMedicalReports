package com.sysmedica.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

public interface FichaAcciOfidicoDao {
	
	
	public int insertarFichaCompleta(Connection con,
							    		int numeroSolicitud,
										String login,
										int codigoPaciente,
										String codigoDiagnostico,
										int estado,
										int codigoAseguradora,
										String nombreProfesional,
									    
									    String sire,
										boolean notificar,
										
										int codigoFichaOfidico,										    
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
									    
									    String fechaAccidente,
										String nombreVereda,
										int actividadAccidente,
										String cualActividad,
										int tipoAtencionInicial,
										String cualAtencion,
										int practicasNoMedicas,
										String cualPractica,
										int localizacionMordedura,
										int huellasColmillos,
										int serpienteIdentificada,
										int serpienteCapturada,
										int generoAgenteAgresor,
										String cualAgente,
										int nombreAgenteAgresor,
										String cualLocal,
										String cualComplicacion,
										String cualSistemica,
										int severidadAccidente,
										int empleoSuero,
										int diasTranscurridos,
										int horasTranscurridas,
										int tipoSueroAntiofidico,
										String cualSuero,
										int dosisSuero,
										int horasSuero,
										int minutosSuero,
										int tratamientoQuirurgico,
										int tipoTratamiento,
									    
									    boolean activa,
									    String pais,
									    int areaProcedencia,
									    
									    HashMap manifestacionesLocales,
									    HashMap manifestacionesSistemicas,
									    HashMap complicacionesLocales,
									    HashMap complicacionesSistemicas
									   );
	
	

	public int modificarFicha(Connection con,
								String sire,
								boolean notificar,
							    String loginUsuario,
							    int codigoFichaAcciOfidico,
							    int codigoPaciente,
							    String codigoDiagnostico,
							    int codigoNotificacion,
							    int numeroSolicitud,
							    int estado,
							    
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
							    
							    String fechaAccidente,
								String nombreVereda,
								int actividadAccidente,
								String cualActividad,
								int tipoAtencionInicial,
								String cualAtencion,
								int practicasNoMedicas,
								String cualPractica,
								int localizacionMordedura,
								int huellasColmillos,
								int serpienteIdentificada,
								int serpienteCapturada,
								int generoAgenteAgresor,
								String cualAgente,
								int nombreAgenteAgresor,
								String cualLocal,
								String cualComplicacion,
								String cualSistemica,
								int severidadAccidente,
								int empleoSuero,
								int diasTranscurridos,
								int horasTranscurridas,
								int tipoSueroAntiofidico,
								String cualSuero,
								int dosisSuero,
								int horasSuero,
								int minutosSuero,
								int tratamientoQuirurgico,
								int tipoTratamiento,
							    String pais,
							    int areaProcedencia,
							    
							    HashMap manifestacionesLocales,
							    HashMap manifestacionesSistemicas,
							    HashMap complicacionesLocales,
							    HashMap complicacionesSistemicas
							    );
	
	
	
	
	public ResultSet consultaTodoFicha(Connection con, int codigo);
	
	
	
	public ResultSet consultarDatosPaciente(Connection con, int codigo, boolean empezarnuevo);
	
	
	
	public ResultSet consultarManifestacionLocal(Connection con, int codigo);
	
	
	
	public ResultSet consultarManifestacionSistemica(Connection con, int codigo);
	
	
	
	public ResultSet consultarComplicacionLocal(Connection con, int codigo);
	
	
	
	public ResultSet consultarComplicacionSistemica(Connection con, int codigo);
	
}
