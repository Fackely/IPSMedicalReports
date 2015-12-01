package com.sysmedica.dao.oracle;

import java.sql.Connection;
import java.sql.ResultSet;

import com.sysmedica.dao.FichaEsiDao;
import com.sysmedica.dao.sqlbase.SqlBaseFichaDifteriaDao;
import com.sysmedica.dao.sqlbase.SqlBaseFichaEsiDao;

public class OracleFichaEsiDao implements FichaEsiDao {

	private String secuenciaStr = "SELECT epidemiologia.seq_fichas.nextval FROM dual";

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
									   )
	{
		return SqlBaseFichaEsiDao.insertarFichaCompleta(con,
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
															
															codigoFichaEsi,										    
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
														    
														    clasificacionInicial,
														    ocupacion,
														    lugarTrabajo,
														    vacunaEstacional,
														    carneVacunacion,
														    numeroDosis,
														    fechaUltimaDosis,
														    verificacion,
														    fuenteNotificacion,
														    viajo,
														    codMunViajo,
														    codDepViajo,
														    lugarViajo,
														    contactoAves,
														    contactoPersona,
														    casoEsporadico,
														    casoEpidemico,
														    fiebre,
														    dolorGarganta,
														    tos,
														    dificultadRespiratoria,
														    hipoxia,
														    taquipnea,
														    rinorrea,
														    coriza,
														    conjuntivitis,
														    cefalea,
														    mialgias,
														    postracion,
														    infiltrados,
														    dolorAbdominal,
														    
														    activa,
														    pais,
														    areaProcedencia);
	}
	
	
	
	

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
									    )
	{
		return SqlBaseFichaEsiDao.modificarFicha(con,
													sire,
													notificar,
												    loginUsuario,
												    codigoFichaLepra,
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
									
												    clasificacionInicial,
												    ocupacion,
												    lugarTrabajo,
												    vacunaEstacional,
												    carneVacunacion,
												    numeroDosis,
												    fechaUltimaDosis,
												    verificacion,
												    fuenteNotificacion,
												    viajo,
												    codMunViajo,
												    codDepViajo,
												    lugarViajo,
												    contactoAves,
												    contactoPersona,
												    casoEsporadico,
												    casoEpidemico,
												    fiebre,
												    dolorGarganta,
												    tos,
												    dificultadRespiratoria,
												    hipoxia,
												    taquipnea,
												    rinorrea,
												    coriza,
												    conjuntivitis,
												    cefalea,
												    mialgias,
												    postracion,
												    infiltrados,
												    dolorAbdominal,
													pais,
												    areaProcedencia);
	}
	
	
	
	public ResultSet consultaTodoFicha(Connection con, int codigo)
	{
		return SqlBaseFichaEsiDao.consultarTodoFichaEsi(con,codigo);
	}
	
	
	
	
	public ResultSet consultarDatosPaciente(Connection con, int codigo, boolean empezarnuevo)
	{
		return SqlBaseFichaEsiDao.consultarDatosPaciente(con,codigo,empezarnuevo);
	}	
	
	
	
	

	public ResultSet consultarDatosLaboratorio(Connection con, int codigo)
	{
		return SqlBaseFichaDifteriaDao.consultarDatosLaboratorio(con,codigo);
	}
}
