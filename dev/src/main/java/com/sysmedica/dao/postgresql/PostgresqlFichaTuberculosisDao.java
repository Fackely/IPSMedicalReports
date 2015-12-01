package com.sysmedica.dao.postgresql;

import java.sql.Connection;
import java.sql.ResultSet;

import com.sysmedica.dao.sqlbase.SqlBaseFichaTuberculosisDao;

import com.sysmedica.dao.FichaTuberculosisDao;

public class PostgresqlFichaTuberculosisDao implements FichaTuberculosisDao {

	private String secuenciaStr = "SELECT nextval('epidemiologia.seq_fichas')";
	
	public int insertarFicha(Connection con,
								boolean notificar,
							    String loginUsuario,
							    int codigoFichaTuberculosis,
							    int codigoPaciente,
							    String codigoDiagnostico,
							    int codigoNotificacion,
							    int numeroSolicitud,
							    int codigoAseguradora,
								String nombreProfesional,
							    int estado
							    )
	{
		return SqlBaseFichaTuberculosisDao.insertarFicha(con,
															numeroSolicitud,
															loginUsuario,
															codigoPaciente,
															codigoDiagnostico,
															estado,
															codigoAseguradora,
															nombreProfesional,
														    secuenciaStr);
	}
	
	
	public int modificarFicha(Connection con,
								String sire,
								String loginUsuario,
							    int codigoFichaTuberculosis,
							    int estado,
							    boolean baciloscopia,
							    boolean cultivo,
							    boolean histopatologia,
							    boolean clinicaPaciente,
							    boolean nexoEpidemiologico,
							    boolean radiologico,
							    boolean tuberculina,
							    boolean ada,
							    boolean otroDx,
							    int resultadoBk,
							    String resultadoAda,
							    String resultadoTuberculina,
							    int tipoTuberculosis,
							    boolean tieneCicatriz,
							    String fuenteContagio,
							    int metodoHallazgo,
							    String otroCual,
							    int asociadoVih,
							    boolean asesoriaVih,
							    String observaciones,
								
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
							    int realizoCultivo,
							    String otroTipoTuberculosis,
							    int fuenteContagio2,
							    String pais,
							    int areaProcedencia
								)
	{
		return SqlBaseFichaTuberculosisDao.modificarFicha(con,
															sire,
															loginUsuario,
														    codigoFichaTuberculosis,
														    estado,
														    
														    baciloscopia,
														    cultivo,
														    histopatologia,
														    clinicaPaciente,
														    nexoEpidemiologico,
														    radiologico,
														    tuberculina,
														    ada,
														    otroDx,
														    resultadoBk,
														    resultadoAda,
														    resultadoTuberculina,
														    tipoTuberculosis,
														    tieneCicatriz,
														    fuenteContagio,
														    metodoHallazgo,
														    otroCual,
														    asociadoVih,
														    asesoriaVih,
														    observaciones,
															
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
														    realizoCultivo,
														    otroTipoTuberculosis,
														    fuenteContagio2,
														    pais,
														    areaProcedencia);
	}
	
	
	public ResultSet consultaTodoFicha(Connection con, int codigo)
	{
		return SqlBaseFichaTuberculosisDao.consultarTodoFichaTuberculosis(con,codigo);
	}
	
	
	public ResultSet consultaDatosPaciente(Connection con, int codigo, boolean empezarnuevo)
    {
    	return SqlBaseFichaTuberculosisDao.consultarDatosPaciente(con,codigo,empezarnuevo);
    }
	
	
	
	public int insertarFichaCompleta(Connection con,
										int numeroSolicitud,
										String login,
										int codigoPaciente,
										String codigoDiagnostico,
										int estado,
										int codigoAseguradora,
										String nombreProfesional,
									    
									    String sire,
									    boolean baciloscopia,
									    boolean cultivo,
									    boolean histopatologia,
									    boolean clinicaPaciente,
									    boolean nexoEpidemiologico,
									    boolean radiologico,
									    boolean tuberculina,
									    boolean ada,
									    boolean otroDx,
									    int resultadoBk,
									    String resultadoAda,
									    String resultadoTuberculina,
									    int tipoTuberculosis,
									    boolean tieneCicatriz,
									    String fuenteContagio,
									    int metodoHallazgo,
									    String otroCual,
									    int asociadoVih,
									    boolean asesoriaVih,
									    String observaciones,
										
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
									    int realizoCultivo,
									    String otroTipoTuberculosis,
									    int fuenteContagio2,
									    String pais,
									    int areaProcedencia
									   )
	{
		return SqlBaseFichaTuberculosisDao.insertarFichaCompleta(con,
																	numeroSolicitud,
																	login,
																	codigoPaciente,
																	codigoDiagnostico,
																	estado,
																	codigoAseguradora,
																	nombreProfesional,
																    secuenciaStr,
																    
																    sire,
																    baciloscopia,
																    cultivo,
																    histopatologia,
																    clinicaPaciente,
																    nexoEpidemiologico,
																    radiologico,
																    tuberculina,
																    ada,
																    otroDx,
																    resultadoBk,
																    resultadoAda,
																    resultadoTuberculina,
																    tipoTuberculosis,
																    tieneCicatriz,
																    fuenteContagio,
																    metodoHallazgo,
																    otroCual,
																    asociadoVih,
																    asesoriaVih,
																    observaciones,
																	
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
																    activa,
																    realizoCultivo,
																    otroTipoTuberculosis,
																    fuenteContagio2,
																    pais,
																    areaProcedencia);
	}
}
