package com.sysmedica.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

public interface FichaHepatitisDao {

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
									   );
	
	
	
	

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
									    );
	
	
	
	
	
	
	public ResultSet consultaTodoFicha(Connection con, int codigo);
	
	
	
	
	public ResultSet consultarDatosPaciente(Connection con, int codigo, boolean empezarnuevo);
	
	
	
	public ResultSet consultarSintomas(Connection con, int codigo);
	
	
	
	public ResultSet consultarPoblacion(Connection con, int codigo);
	
	
	
	public ResultSet consultarDatosLaboratorio(Connection con, int codigo);
}
