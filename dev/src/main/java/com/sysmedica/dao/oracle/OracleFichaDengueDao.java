package com.sysmedica.dao.oracle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

import com.sysmedica.dao.FichaDengueDao;
import com.sysmedica.dao.sqlbase.SqlBaseFichaDengueDao;

public class OracleFichaDengueDao implements FichaDengueDao {
	
	private String secuenciaStr = "SELECT seq_fichas.nextval FROM dual";
	
	/**
	 * Metodo para insertar una ficha de Dengue
	 * @param con
	 * @param notificar
	 * @param loginUsuario
	 * @param codigoFichaVIH
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
							    int codigoFichaVIH,
							    int codigoPaciente,
							    String codigoDiagnostico,
							    int codigoNotificacion,
							    int numeroSolicitud,
							    int codigoAseguradora,
								String nombreProfesional,
							    int estado
							    )
	{
		return SqlBaseFichaDengueDao.insertarFicha(con,
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
								boolean notificar,
							    String loginUsuario,
							    int codigoFichaDengue,
							    int estado,
							    int vacunaFiebreAmarilla,
								String fechaAplicacionVacunaFiebre,
								int vacunaHepatitisBDosis1,
								int vacunaHepatitisBDosis2,
								int vacunaHepatitisBDosis3,
								String fechaVacunaHepaDosis1,
								String fechaVacunaHepaDosis2,
								String fechaVacunaHepaDosis3,
								int vacunaHepatitisADosis1,
								String fechaVacunaHepatADosis1,
								String observaciones,
								boolean desplazamiento,
								String fechaDesplazamiento,
								String lugarDesplazamiento,
								String codigoMunicipio,
								String codigoDepartamento,
								int casoFiebreAmarilla,
								int casoEpizootia,
								String direccionSitio,
								int presenciaAedes,
								HashMap hallazgosSemiologicos,
								HashMap datosLaboratorio,
								
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
							    int estadoAnterior,
							    String pais,
							    int areaProcedencia
							    )
	{
		return SqlBaseFichaDengueDao.modificarFicha(con,
													sire,
												    loginUsuario,
												    codigoFichaDengue,
												    estado,
												    
												    vacunaFiebreAmarilla,
													fechaAplicacionVacunaFiebre,
													vacunaHepatitisBDosis1,
													vacunaHepatitisBDosis2,
													vacunaHepatitisBDosis3,
													fechaVacunaHepaDosis1,
													fechaVacunaHepaDosis2,
													fechaVacunaHepaDosis3,
													vacunaHepatitisADosis1,
													fechaVacunaHepatADosis1,
													observaciones,
													desplazamiento,
													fechaDesplazamiento,
													lugarDesplazamiento,
													codigoMunicipio,
													codigoDepartamento,
													casoFiebreAmarilla,
													casoEpizootia,
													direccionSitio,
													presenciaAedes,
													hallazgosSemiologicos,
													datosLaboratorio,
													
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
												    estadoAnterior,
												    pais,
												    areaProcedencia
													);
	}


	public ResultSet consultaTodoFicha(Connection con, int codigo)
	{
		return SqlBaseFichaDengueDao.consultarTodoFichaDengue(con,codigo);
	}
	
	
	
	public ResultSet consultarHallazgos(Connection con, int codigo)
	{
		return SqlBaseFichaDengueDao.consultarHallazgos(con,codigo);
	}
	
	
	
	public ResultSet consultarDatosLaboratorio(Connection con, int codigo)
	{
		return SqlBaseFichaDengueDao.consultarDatosLaboratorio(con,codigo);
	}
	
	
	public ResultSet consultaDatosPaciente(Connection con, int codigo, boolean empezarnuevo)
    {
    	return SqlBaseFichaDengueDao.consultarDatosPaciente(con,codigo,empezarnuevo);
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
									    
									    int vacunaFiebreAmarilla,
										String fechaAplicacionVacunaFiebre,
										int vacunaHepatitisBDosis1,
										int vacunaHepatitisBDosis2,
										int vacunaHepatitisBDosis3,
										String fechaVacunaHepaDosis1,
										String fechaVacunaHepaDosis2,
										String fechaVacunaHepaDosis3,
										int vacunaHepatitisADosis1,
										String fechaVacunaHepatADosis1,
										String observaciones,
										boolean desplazamiento,
										String fechaDesplazamiento,
										String lugarDesplazamiento,
										int casoFiebreAmarilla,
										int casoEpizootia,
										String direccionSitio,
										int presenciaAedes,
										
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
									    int estadoAnterior,
									    boolean activa,
									    HashMap hallazgosSemiologicos,
									    String pais,
									    int areaProcedencia
									    )
	{
		return SqlBaseFichaDengueDao.insertarFichaCompleta(con,
															numeroSolicitud,
															login,
															codigoPaciente,
															codigoDiagnostico,
															estado,
															codigoAseguradora,
															nombreProfesional,
														    secuenciaStr,
														    
														    sire,
														    
														    vacunaFiebreAmarilla,
															fechaAplicacionVacunaFiebre,
															vacunaHepatitisBDosis1,
															vacunaHepatitisBDosis2,
															vacunaHepatitisBDosis3,
															fechaVacunaHepaDosis1,
															fechaVacunaHepaDosis2,
															fechaVacunaHepaDosis3,
															vacunaHepatitisADosis1,
															fechaVacunaHepatADosis1,
															observaciones,
															desplazamiento,
															fechaDesplazamiento,
															lugarDesplazamiento,
															casoFiebreAmarilla,
															casoEpizootia,
															direccionSitio,
															presenciaAedes,
															
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
														    estadoAnterior,
														    activa,
														    hallazgosSemiologicos,
														    pais,
														    areaProcedencia);
	}
	
	
	
	public int terminarFicha(Connection con, int codigoFichaDengue)
	{
		return SqlBaseFichaDengueDao.terminarFicha(con,codigoFichaDengue);
	}
}
