package com.sysmedica.dao.oracle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

import com.sysmedica.dao.FichaMeningitisDao;
import com.sysmedica.dao.sqlbase.SqlBaseFichaMeningitisDao;

public class OracleFichaMeningitisDao implements FichaMeningitisDao {

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
										    
										    int vacunaAntihib,
										    int dosis,
										    String fechaPrimeraDosis,
										    String fechaUltimaDosis,
										    int tieneCarne,
										    int vacunaAntimenin,
										    int dosis2,
										    String fechaPrimeraDosis2,
										    String fechaUltimaDosis2,
										    int tieneCarne2,
										    int vacunaAntineumo,
										    int dosis3,
										    String fechaPrimeraDosis3,
										    String fechaUltimaDosis3,
										    int tieneCarne3,
										    int fiebre,
										    int rigidez,
										    int irritacion,
										    int rash,
										    int abombamiento,
										    int alteracion,
										    int usoAntibioticos,
										    String fechaUltimaDosis4,
										    String observaciones,
										    
										    boolean activa,
										    String pais,
										    int areaProcedencia
										   )
	{
		return SqlBaseFichaMeningitisDao.insertarFichaCompleta(con,
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
															    
															    vacunaAntihib,
															    dosis,
															    fechaPrimeraDosis,
															    fechaUltimaDosis,
															    tieneCarne,
															    vacunaAntimenin,
															    dosis2,
															    fechaPrimeraDosis2,
															    fechaUltimaDosis2,
															    tieneCarne2,
															    vacunaAntineumo,
															    dosis3,
															    fechaPrimeraDosis3,
															    fechaUltimaDosis3,
															    tieneCarne3,
															    fiebre,
															    rigidez,
															    irritacion,
															    rash,
															    abombamiento,
															    alteracion,
															    usoAntibioticos,
															    fechaUltimaDosis4,
															    observaciones,
															    
															    activa,
															    pais,
															    areaProcedencia);
	}
	
	
	
	

	public int modificarFicha(Connection con,
										String sire,
										boolean notificar,
									    String loginUsuario,
									    int codigoFichaMeningitis,
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

									    int vacunaAntihib,
									    int dosis,
									    String fechaPrimeraDosis,
									    String fechaUltimaDosis,
									    int tieneCarne,
									    int vacunaAntimenin,
									    int dosis2,
									    String fechaPrimeraDosis2,
									    String fechaUltimaDosis2,
									    int tieneCarne2,
									    int vacunaAntineumo,
									    int dosis3,
									    String fechaPrimeraDosis3,
									    String fechaUltimaDosis3,
									    int tieneCarne3,
									    int fiebre,
									    int rigidez,
									    int irritacion,
									    int rash,
									    int abombamiento,
									    int alteracion,
									    int usoAntibioticos,
									    String fechaUltimaDosis4,
									    String observaciones,
									    
										String pais,
									    int areaProcedencia
									    )
	{
		return SqlBaseFichaMeningitisDao.modificarFicha(con,
															sire,
															notificar,
														    loginUsuario,
														    codigoFichaMeningitis,
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
											
														    vacunaAntihib,
														    dosis,
														    fechaPrimeraDosis,
														    fechaUltimaDosis,
														    tieneCarne,
														    vacunaAntimenin,
														    dosis2,
														    fechaPrimeraDosis2,
														    fechaUltimaDosis2,
														    tieneCarne2,
														    vacunaAntineumo,
														    dosis3,
														    fechaPrimeraDosis3,
														    fechaUltimaDosis3,
														    tieneCarne3,
														    fiebre,
														    rigidez,
														    irritacion,
														    rash,
														    abombamiento,
														    alteracion,
														    usoAntibioticos,
														    fechaUltimaDosis4,
														    observaciones,
														    
															pais,
														    areaProcedencia);
	}
	
	
	
	

	public ResultSet consultaTodoFicha(Connection con, int codigo)
	{
		return SqlBaseFichaMeningitisDao.consultarTodoFichaMeningitis(con,codigo);
	}
	
	
	
	public ResultSet consultarDatosPaciente(Connection con, int codigo, boolean empezarnuevo)
	{
		return SqlBaseFichaMeningitisDao.consultarDatosPaciente(con,codigo,empezarnuevo);
	}
	
	
	
	public ResultSet consultarDatosLaboratorio(Connection con, int codigo)
	{
		return SqlBaseFichaMeningitisDao.consultarDatosLaboratorio(con,codigo);
	}
}
