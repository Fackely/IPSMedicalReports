package com.sysmedica.dao.oracle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

import com.sysmedica.dao.FichaAcciOfidicoDao;
import com.sysmedica.dao.sqlbase.SqlBaseFichaAcciOfidicoDao;

public class OracleFichaAcciOfidicoDao implements FichaAcciOfidicoDao {

	private String secuenciaStr = "SELECT seq_fichas.nextval FROM dual";

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
										
										int codigoFichaIntoxicacion,										    
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
									   )
	{
		return SqlBaseFichaAcciOfidicoDao.insertarFichaCompleta(con,
													    		numeroSolicitud,
																login,
																codigoPaciente,
																codigoDiagnostico,
																estado,
																codigoAseguradora,
																nombreProfesional,
															    secuenciaStr,
															    
															    sire,
																notificar,
																
																codigoFichaIntoxicacion,										    
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
															    
															    fechaAccidente,
																nombreVereda,
																actividadAccidente,
																cualActividad,
																tipoAtencionInicial,
																cualAtencion,
																practicasNoMedicas,
																cualPractica,
																localizacionMordedura,
																huellasColmillos,
																serpienteIdentificada,
																serpienteCapturada,
																generoAgenteAgresor,
																cualAgente,
																nombreAgenteAgresor,
																cualLocal,
																cualComplicacion,
																cualSistemica,
																severidadAccidente,
																empleoSuero,
																diasTranscurridos,
																horasTranscurridas,
																tipoSueroAntiofidico,
																cualSuero,
																dosisSuero,
																horasSuero,
																minutosSuero,
																tratamientoQuirurgico,
																tipoTratamiento,
															    
															    activa,
															    pais,
															    areaProcedencia,
															    
															    manifestacionesLocales,
															    manifestacionesSistemicas,
															    complicacionesLocales,
															    complicacionesSistemicas);
	}
	
	

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
							    )
	{
		return SqlBaseFichaAcciOfidicoDao.modificarFicha(con,
														sire,
														notificar,
													    loginUsuario,
													    codigoFichaAcciOfidico,
													    codigoPaciente,
													    codigoDiagnostico,
													    codigoNotificacion,
													    numeroSolicitud,
													    estado,
													    
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
													    fechaAccidente,
														nombreVereda,
														actividadAccidente,
														cualActividad,
														tipoAtencionInicial,
														cualAtencion,
														practicasNoMedicas,
														cualPractica,
														localizacionMordedura,
														huellasColmillos,
														serpienteIdentificada,
														serpienteCapturada,
														generoAgenteAgresor,
														cualAgente,
														nombreAgenteAgresor,
														cualLocal,
														cualComplicacion,
														cualSistemica,
														severidadAccidente,
														empleoSuero,
														diasTranscurridos,
														horasTranscurridas,
														tipoSueroAntiofidico,
														cualSuero,
														dosisSuero,
														horasSuero,
														minutosSuero,
														tratamientoQuirurgico,
														tipoTratamiento,
														pais,
													    areaProcedencia,
													    
													    manifestacionesLocales,
													    manifestacionesSistemicas,
													    complicacionesLocales,
													    complicacionesSistemicas
													    );
	}
	
	
	
	
	public ResultSet consultaTodoFicha(Connection con, int codigo)
	{
		return SqlBaseFichaAcciOfidicoDao.consultarTodoFichaAcciOfidico(con,codigo);
	}
	
	
	
	public ResultSet consultarDatosPaciente(Connection con, int codigo, boolean empezarnuevo)
	{
		return SqlBaseFichaAcciOfidicoDao.consultarDatosPaciente(con,codigo,empezarnuevo);
	}
	
	
	
	public ResultSet consultarManifestacionLocal(Connection con, int codigo)
	{
		return SqlBaseFichaAcciOfidicoDao.consultarManifestacionLocal(con,codigo);
	}
	
	
	
	public ResultSet consultarManifestacionSistemica(Connection con, int codigo)
	{
		return SqlBaseFichaAcciOfidicoDao.consultarManifestacionSistemica(con,codigo);
	}
	
	
	
	public ResultSet consultarComplicacionLocal(Connection con, int codigo)
	{
		return SqlBaseFichaAcciOfidicoDao.consultarComplicacionLocal(con,codigo);
	}
	
	
	
	public ResultSet consultarComplicacionSistemica(Connection con, int codigo)
	{
		return SqlBaseFichaAcciOfidicoDao.consultarComplicacionSistemica(con,codigo);
	}
}
