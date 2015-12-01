package com.sysmedica.dao.postgresql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

import com.sysmedica.dao.FichaEtasDao;
import com.sysmedica.dao.sqlbase.SqlBaseFichaEtasDao;

public class PostgresqlFichaEtasDao implements FichaEtasDao {

	private String secuenciaStr = "SELECT nextval('epidemiologia.seq_fichas')";
	private String secuenciaAli = "SELECT nextval('epidemiologia.seq_alimentos_ingeridos')";

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
										    
										    String otroSintoma,
										    String horaInicioSintomas,
										    String minutoInicioSintomas,
										    String nombreAlimento1,
										    String nombreAlimento2,
										    String nombreAlimento3,
										    String nombreAlimento4,
										    String nombreAlimento5,
										    String nombreAlimento6,
										    String nombreAlimento7,
										    String nombreAlimento8,
										    String nombreAlimento9,
										    String lugarConsumo1,
										    String lugarConsumo2,
										    String lugarConsumo3,
										    String lugarConsumo4,
										    String lugarConsumo5,
										    String lugarConsumo6,
										    String lugarConsumo7,
										    String lugarConsumo8,
										    String lugarConsumo9,
										    String horaConsumo1,
										    String horaConsumo2,
										    String horaConsumo3,
										    String horaConsumo4,
										    String horaConsumo5,
										    String horaConsumo6,
										    String horaConsumo7,
										    String horaConsumo8,
										    String horaConsumo9,
										    String minutoConsumo1,
										    String minutoConsumo2,
										    String minutoConsumo3,
										    String minutoConsumo4,
										    String minutoConsumo5,
										    String minutoConsumo6,
										    String minutoConsumo7,
										    String minutoConsumo8,
										    String minutoConsumo9,
										    int asociadoBrote,
										    int captadoPor,
										    int relacionExposicion,
										    int tomoMuestra,
										    int tipoMuestra,
										    String cualMuestra,
										    int agente1,
										    int agente2,
										    int agente3,
										    int agente4,
										    
										    boolean activa,
										    String pais,
										    int areaProcedencia,
										    
										    HashMap sintomas,
										    HashMap alimentos
										   )
	{
		return SqlBaseFichaEtasDao.insertarFichaCompleta(con,
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
														    
														    otroSintoma,
														    horaInicioSintomas,
														    minutoInicioSintomas,
														    nombreAlimento1,
														    nombreAlimento2,
														    nombreAlimento3,
														    nombreAlimento4,
														    nombreAlimento5,
														    nombreAlimento6,
														    nombreAlimento7,
														    nombreAlimento8,
														    nombreAlimento9,
														    lugarConsumo1,
														    lugarConsumo2,
														    lugarConsumo3,
														    lugarConsumo4,
														    lugarConsumo5,
														    lugarConsumo6,
														    lugarConsumo7,
														    lugarConsumo8,
														    lugarConsumo9,
														    horaConsumo1,
														    horaConsumo2,
														    horaConsumo3,
														    horaConsumo4,
														    horaConsumo5,
														    horaConsumo6,
														    horaConsumo7,
														    horaConsumo8,
														    horaConsumo9,
														    minutoConsumo1,
														    minutoConsumo2,
														    minutoConsumo3,
														    minutoConsumo4,
														    minutoConsumo5,
														    minutoConsumo6,
														    minutoConsumo7,
														    minutoConsumo8,
														    minutoConsumo9,
														    asociadoBrote,
														    captadoPor,
														    relacionExposicion,
														    tomoMuestra,
														    tipoMuestra,
														    cualMuestra,
														    agente1,
														    agente2,
														    agente3,
														    agente4,
														    
														    activa,
														    pais,
														    areaProcedencia,
														    
														    sintomas,
														    alimentos,
														    secuenciaAli);
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

									    String otroSintoma,
									    String horaInicioSintomas,
									    String minutoInicioSintomas,
									    String nombreAlimento1,
									    String nombreAlimento2,
									    String nombreAlimento3,
									    String nombreAlimento4,
									    String nombreAlimento5,
									    String nombreAlimento6,
									    String nombreAlimento7,
									    String nombreAlimento8,
									    String nombreAlimento9,
									    String lugarConsumo1,
									    String lugarConsumo2,
									    String lugarConsumo3,
									    String lugarConsumo4,
									    String lugarConsumo5,
									    String lugarConsumo6,
									    String lugarConsumo7,
									    String lugarConsumo8,
									    String lugarConsumo9,
									    String horaConsumo1,
									    String horaConsumo2,
									    String horaConsumo3,
									    String horaConsumo4,
									    String horaConsumo5,
									    String horaConsumo6,
									    String horaConsumo7,
									    String horaConsumo8,
									    String horaConsumo9,
									    String minutoConsumo1,
									    String minutoConsumo2,
									    String minutoConsumo3,
									    String minutoConsumo4,
									    String minutoConsumo5,
									    String minutoConsumo6,
									    String minutoConsumo7,
									    String minutoConsumo8,
									    String minutoConsumo9,
									    int asociadoBrote,
									    int captadoPor,
									    int relacionExposicion,
									    int tomoMuestra,
									    int tipoMuestra,
									    String cualMuestra,
									    int agente1,
									    int agente2,
									    int agente3,
									    int agente4,
										String pais,
									    int areaProcedencia,
									    HashMap sintomas,
									    HashMap alimentos
									    )
	{
		return SqlBaseFichaEtasDao.modificarFicha(con,
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
									
												    otroSintoma,
												    horaInicioSintomas,
												    minutoInicioSintomas,
												    nombreAlimento1,
												    nombreAlimento2,
												    nombreAlimento3,
												    nombreAlimento4,
												    nombreAlimento5,
												    nombreAlimento6,
												    nombreAlimento7,
												    nombreAlimento8,
												    nombreAlimento9,
												    lugarConsumo1,
												    lugarConsumo2,
												    lugarConsumo3,
												    lugarConsumo4,
												    lugarConsumo5,
												    lugarConsumo6,
												    lugarConsumo7,
												    lugarConsumo8,
												    lugarConsumo9,
												    horaConsumo1,
												    horaConsumo2,
												    horaConsumo3,
												    horaConsumo4,
												    horaConsumo5,
												    horaConsumo6,
												    horaConsumo7,
												    horaConsumo8,
												    horaConsumo9,
												    minutoConsumo1,
												    minutoConsumo2,
												    minutoConsumo3,
												    minutoConsumo4,
												    minutoConsumo5,
												    minutoConsumo6,
												    minutoConsumo7,
												    minutoConsumo8,
												    minutoConsumo9,
												    asociadoBrote,
												    captadoPor,
												    relacionExposicion,
												    tomoMuestra,
												    tipoMuestra,
												    cualMuestra,
												    agente1,
												    agente2,
												    agente3,
												    agente4,
													pais,
												    areaProcedencia,
												    sintomas,
												    alimentos,
												    secuenciaAli);
	}
	
	
	
	
	public ResultSet consultaTodoFicha(Connection con, int codigo)
	{
		return SqlBaseFichaEtasDao.consultarTodoFichaEtas(con,codigo);
	}

	
	
	public ResultSet consultarDatosPaciente(Connection con, int codigo, boolean empezarnuevo)
	{
		return SqlBaseFichaEtasDao.consultarDatosPaciente(con,codigo,empezarnuevo);
	}
	
	
	
	public ResultSet consultarSintomas(Connection con, int codigo)
	{
		return SqlBaseFichaEtasDao.consultarSintomas(con,codigo);
	}
	
	
	
	
	public ResultSet consultarAlimentos(Connection con, int codigo)
	{
		return SqlBaseFichaEtasDao.consultarAlimentos(con,codigo);
	}
}
