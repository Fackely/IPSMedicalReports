package com.sysmedica.dao.postgresql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

import com.sysmedica.dao.FichaMalariaDao;
import com.sysmedica.dao.sqlbase.SqlBaseFichaMalariaDao;

public class PostgresqlFichaMalariaDao implements FichaMalariaDao {

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
										    
										    int viajo,
										    String codDepViajo,
										    String codMunViajo,
										    int padecioMalaria,
										    String fechaAproximada,
										    int automedicacion,
										    int antecedenteTrans,
										    String fechaAntecedente,
										    int tipoComplicacion,
										    int especiePlasmodium,
										    int embarazo,
										    int tratAntimalarico,
										    String cualTratamiento,
										    HashMap sintomas,
										    HashMap tratamiento,
										    
										    boolean activa,
										    String pais,
										    int areaProcedencia,
										    String lugarViajo
										   )
	{
		return SqlBaseFichaMalariaDao.insertarFichaCompleta(con,
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
														    
														    viajo,
														    codDepViajo,
														    codMunViajo,
														    padecioMalaria,
														    fechaAproximada,
														    automedicacion,
														    antecedenteTrans,
														    fechaAntecedente,
														    tipoComplicacion,
														    especiePlasmodium,
														    embarazo,
														    tratAntimalarico,
														    cualTratamiento,
														    sintomas,
														    tratamiento,
														    
														    activa,
														    pais,
														    areaProcedencia,
														    lugarViajo);
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

									    int viajo,
									    String codDepViajo,
									    String codMunViajo,
									    int padecioMalaria,
									    String fechaAproximada,
									    int automedicacion,
									    int antecedenteTrans,
									    String fechaAntecedente,
									    int tipoComplicacion,
									    int especiePlasmodium,
									    int embarazo,
									    int tratAntimalarico,
									    String cualTratamiento,
									    HashMap sintomas,
									    HashMap tratamiento,
									    
										String pais,
									    int areaProcedencia,
									    String lugarViajo
									    )
	{
		return SqlBaseFichaMalariaDao.modificarFicha(con,
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
									
												    viajo,
												    codDepViajo,
												    codMunViajo,
												    padecioMalaria,
												    fechaAproximada,
												    automedicacion,
												    antecedenteTrans,
												    fechaAntecedente,
												    tipoComplicacion,
												    especiePlasmodium,
												    embarazo,
												    tratAntimalarico,
												    cualTratamiento,
												    sintomas,
												    tratamiento,
												    
													pais,
												    areaProcedencia,
												    lugarViajo);
	}
	
	
	
	

	public ResultSet consultaTodoFicha(Connection con, int codigo)
	{
		return SqlBaseFichaMalariaDao.consultarTodoFichaMalaria(con,codigo);
	}
	
	
	public ResultSet consultarDatosPaciente(Connection con, int codigo, boolean empezarnuevo)
	{
		return SqlBaseFichaMalariaDao.consultarDatosPaciente(con,codigo,empezarnuevo);
	}
	
	
	public ResultSet consultarSintomas(Connection con, int codigo)
	{
		return SqlBaseFichaMalariaDao.consultarSintomas(con,codigo);
	}
	
	
	public ResultSet consultarTratamiento(Connection con, int codigo)
	{
		return SqlBaseFichaMalariaDao.consultarTratamiento(con,codigo);
	}
	
	
	public ResultSet consultarDatosLaboratorio(Connection con, int codigo)
	{
		return SqlBaseFichaMalariaDao.consultarDatosLaboratorio(con,codigo);
	}
}
