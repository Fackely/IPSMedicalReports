package com.sysmedica.dao.postgresql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

import com.sysmedica.dao.FichaHepatitisDao;
import com.sysmedica.dao.sqlbase.SqlBaseFichaDifteriaDao;
import com.sysmedica.dao.sqlbase.SqlBaseFichaHepatitisDao;

public class PostgresqlFichaHepatitisDao implements FichaHepatitisDao {

	private String secuenciaStr = "SELECT nextval('epidemiologia.seq_fichas')";

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
									    
									    int embarazada,
									    String edadGestacional,
									    int controlPrenatal,
									    int donanteSangre,
									    int poblacionRiesgo,
									    int modoTransmision,
									    int otrasIts,
									    int vacunaAntihepatitis,
									    String numeroDosis,
									    String fechaPrimeraDosis,
									    String fechaUltimaDosis,
									    int fuenteInformacion,
									    int tratamiento,
									    String cualTratamiento,
									    int complicacion,
									    String cualComplicacion,
									    String observaciones,
									    
									    boolean activa,
									    String pais,
									    int areaProcedencia,
									    
									    HashMap sintomas,
									    HashMap poblaRiesgo
									   )
	{
		return SqlBaseFichaHepatitisDao.insertarFichaCompleta(con,
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
															    
															    embarazada,
															    edadGestacional,
															    controlPrenatal,
															    donanteSangre,
															    poblacionRiesgo,
															    modoTransmision,
															    otrasIts,
															    vacunaAntihepatitis,
															    numeroDosis,
															    fechaPrimeraDosis,
															    fechaUltimaDosis,
															    fuenteInformacion,
															    tratamiento,
															    cualTratamiento,
															    complicacion,
															    cualComplicacion,
															    observaciones,
															    
															    activa,
															    pais,
															    areaProcedencia,
															    
															    sintomas,
															    poblaRiesgo);
	}
	
	
	
	

	public int modificarFicha(Connection con,
										String sire,
										boolean notificar,
									    String loginUsuario,
									    int codigoFichaEasv,
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

									    int embarazada,
									    String edadGestacional,
									    int controlPrenatal,
									    int donanteSangre,
									    int poblacionRiesgo,
									    int modoTransmision,
									    int otrasIts,
									    int vacunaAntihepatitis,
									    String numeroDosis,
									    String fechaPrimeraDosis,
									    String fechaUltimaDosis,
									    int fuenteInformacion,
									    int tratamiento,
									    String cualTratamiento,
									    int complicacion,
									    String cualComplicacion,
									    String observaciones,
										String pais,
									    int areaProcedencia,
									    HashMap sintomas,
									    HashMap poblaRiesgo
									    )
	{
		return SqlBaseFichaHepatitisDao.modificarFicha(con,
														sire,
														notificar,
													    loginUsuario,
													    codigoFichaEasv,
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
										
													    embarazada,
													    edadGestacional,
													    controlPrenatal,
													    donanteSangre,
													    poblacionRiesgo,
													    modoTransmision,
													    otrasIts,
													    vacunaAntihepatitis,
													    numeroDosis,
													    fechaPrimeraDosis,
													    fechaUltimaDosis,
													    fuenteInformacion,
													    tratamiento,
													    cualTratamiento,
													    complicacion,
													    cualComplicacion,
													    observaciones,
														pais,
													    areaProcedencia,
													    sintomas,
													    poblaRiesgo);
	}
	
	
	
	
	
	
	public ResultSet consultaTodoFicha(Connection con, int codigo)
	{
		return SqlBaseFichaHepatitisDao.consultarTodoFichaHepatitis(con,codigo);
	}
	
	
	
	
	public ResultSet consultarDatosPaciente(Connection con, int codigo, boolean empezarnuevo)
	{
		return SqlBaseFichaHepatitisDao.consultarDatosPaciente(con,codigo,empezarnuevo);
	}
	
	
	
	public ResultSet consultarSintomas(Connection con, int codigo)
	{
		return SqlBaseFichaHepatitisDao.consultarSintomas(con,codigo);
	}
	
	
	
	public ResultSet consultarPoblacion(Connection con, int codigo)
	{
		return SqlBaseFichaHepatitisDao.consultarPoblacion(con,codigo);
	}
	
	
	
	public ResultSet consultarDatosLaboratorio(Connection con, int codigo)
	{
		return SqlBaseFichaDifteriaDao.consultarDatosLaboratorio(con,codigo);
	}
}
