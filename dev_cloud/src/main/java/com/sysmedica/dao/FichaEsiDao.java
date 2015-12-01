package com.sysmedica.dao;

import java.sql.Connection;
import java.sql.ResultSet;

public interface FichaEsiDao {

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
										
										int codigoFichaEsi,										    
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
									    
									    int clasificacionInicial,
									    int ocupacion,
									    String lugarTrabajo,
									    int vacunaEstacional,
									    int carneVacunacion,
									    int numeroDosis,
									    String fechaUltimaDosis,
									    int verificacion,
									    int fuenteNotificacion,
									    int viajo,
									    String codMunViajo,
									    String codDepViajo,
									    String lugarViajo,
									    int contactoAves,
									    int contactoPersona,
									    int casoEsporadico,
									    int casoEpidemico,
									    int fiebre,
									    int dolorGarganta,
									    int tos,
									    int dificultadRespiratoria,
									    int hipoxia,
									    int taquipnea,
									    int rinorrea,
									    int coriza,
									    int conjuntivitis,
									    int cefalea,
									    int mialgias,
									    int postracion,
									    int infiltrados,
									    int dolorAbdominal,
									    
									    boolean activa,
									    String pais,
									    int areaProcedencia
									   );
	
	
	
	

	public int modificarFicha(Connection con,
										String sire,
										boolean notificar,
									    String loginUsuario,
									    int codigoFichaLepra,
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

									    int clasificacionInicial,
									    int ocupacion,
									    String lugarTrabajo,
									    int vacunaEstacional,
									    int carneVacunacion,
									    int numeroDosis,
									    String fechaUltimaDosis,
									    int verificacion,
									    int fuenteNotificacion,
									    int viajo,
									    String codMunViajo,
									    String codDepViajo,
									    String lugarViajo,
									    int contactoAves,
									    int contactoPersona,
									    int casoEsporadico,
									    int casoEpidemico,
									    int fiebre,
									    int dolorGarganta,
									    int tos,
									    int dificultadRespiratoria,
									    int hipoxia,
									    int taquipnea,
									    int rinorrea,
									    int coriza,
									    int conjuntivitis,
									    int cefalea,
									    int mialgias,
									    int postracion,
									    int infiltrados,
									    int dolorAbdominal,
										String pais,
									    int areaProcedencia
									    );
	
	
	
	public ResultSet consultaTodoFicha(Connection con, int codigo);
	
	
	
	
	public ResultSet consultarDatosPaciente(Connection con, int codigo, boolean empezarnuevo);
	
	
	
	
	public ResultSet consultarDatosLaboratorio(Connection con, int codigo);
}
