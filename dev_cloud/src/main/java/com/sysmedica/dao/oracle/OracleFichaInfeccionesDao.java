package com.sysmedica.dao.oracle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

import com.sysmedica.dao.FichaInfeccionesDao;
import com.sysmedica.dao.sqlbase.SqlBaseFichaEasvDao;
import com.sysmedica.dao.sqlbase.SqlBaseFichaInfeccionesDao;

public class OracleFichaInfeccionesDao implements FichaInfeccionesDao {

	private String secuenciaStr = "SELECT seq_fichas.nextval FROM dual";
	private String secuenciaMicro = "SELECT seq_microorganismos.nextval FROM dual";
	
	public int insertarFicha(Connection con,
								boolean notificar,
							    String loginUsuario,
							    int codigoFichaVIH,
							    int codigoPaciente,
							    String codigoDiagnostico,
							    int codigoNotificacion,
							    int numeroSolicitud,
							    int estado,
							    int codigoAseguradora,
								String nombreProfesional
							    )
	{
		return SqlBaseFichaInfeccionesDao.insertarFicha(con,
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
							    int codigoFichaInfecciones,
							    int codigoPaciente,
							    String codigoDiagnostico,
							    int codigoNotificacion,
							    int numeroSolicitud,
							    int estado,
							    
							    int numeroCama,
							    int servicio,
							    String fechaIngreso,
							    String fechaDxIh,
							    String fechaEgreso,
							    String fechaDxHisto,
							    String dxHospital,
							    String dxIh,
							    String dxEgreso,
							    String dxHisto,
							    int localizacionAnatomica,
							    int generoMicro,
							    String especieMicro,
							    String bioTipoMicro,
							    int tipoMuestra1,
							    int locAnatomica1,
							    String fechaToma1,
							    String fechaRemision1,
							    String mIdentificacion1,
							    String pruAdicionales1,
							    int tipoMuestra2,
							    int locAnatomica2,
							    String fechaToma2,
							    String fechaRemision2,
							    String mIdentificacion2,
							    String pruAdicionales2,
							    int antibiotico1,
							    String sensibilidad1,
							    String tDosis1,
							    String fechaInicioAntibiotico1,
							    String fechaFinAntibiotico1,
							    int antibiotico2,
							    String sensibilidad2,
							    String tDosis2,
							    String fechaInicioAntibiotico2,
							    String fechaFinAntibiotico2,
							    int antibiotico3,
							    String sensibilidad3,
							    String tDosis3,
							    String fechaInicioAntibiotico3,
							    String fechaFinAntibiotico3,
							    int clasificacionCaso,
							    HashMap factoresRiesgo,
							    int estadoAnterior,
							    int unidadGeneradora,
							    String medicos,
							    String servicio2,
							    HashMap microorganismos)
	{
		return SqlBaseFichaInfeccionesDao.modificarFicha(con,
														sire,
														notificar,
													    loginUsuario,
													    codigoFichaInfecciones,
													    codigoPaciente,
													    codigoDiagnostico,
													    codigoNotificacion,
													    numeroSolicitud,
													    estado,
													    
													    numeroCama,
													    servicio,
													    fechaIngreso,
													    fechaDxIh,
													    fechaEgreso,
													    fechaDxHisto,
													    dxHospital,
													    dxIh,
													    dxEgreso,
													    dxHisto,
													    localizacionAnatomica,
													    generoMicro,
													    especieMicro,
													    bioTipoMicro,
													    tipoMuestra1,
													    locAnatomica1,
													    fechaToma1,
													    fechaRemision1,
													    mIdentificacion1,
													    pruAdicionales1,
													    tipoMuestra2,
													    locAnatomica2,
													    fechaToma2,
													    fechaRemision2,
													    mIdentificacion2,
													    pruAdicionales2,
													    antibiotico1,
													    sensibilidad1,
													    tDosis1,
													    fechaInicioAntibiotico1,
													    fechaFinAntibiotico1,
													    antibiotico2,
													    sensibilidad2,
													    tDosis2,
													    fechaInicioAntibiotico2,
													    fechaFinAntibiotico2,
													    antibiotico3,
													    sensibilidad3,
													    tDosis3,
													    fechaInicioAntibiotico3,
													    fechaFinAntibiotico3,
													    clasificacionCaso,
													    factoresRiesgo,
													    estadoAnterior,
													    unidadGeneradora,
													    medicos,
													    servicio2,
													    microorganismos,
													    secuenciaMicro);
	}


	public ResultSet consultaTodoFicha(Connection con, int codigo)
	{
		return SqlBaseFichaInfeccionesDao.consultarTodoFichaInfecciones(con,codigo);
	}


	public ResultSet consultarFactoresRiesgo(Connection con, int codigo)
	{
		return SqlBaseFichaInfeccionesDao.consultarFactoresRiesgo(con,codigo);
	}
	
	
	
	public ResultSet consultaDatosPaciente(Connection con, int codigo,boolean empezarnuevo)
    {
    	return SqlBaseFichaInfeccionesDao.consultarDatosPaciente(con,codigo,empezarnuevo);
    }
	
	
	public int insertarFichaCompleta(Connection con,
										int numeroSolicitud,
										String login,
										int codigoPaciente,
										String codigoDiagnostico,
										int estado,
										int codigoAseguradora,
										String nombreProfesional,
									    
									    int numeroCama,
									    int servicio,
									    String fechaIngreso,
									    String fechaDxIh,
									    String fechaEgreso,
									    String fechaDxHisto,
									    String dxHospital,
									    String dxIh,
									    String dxEgreso,
									    String dxHisto,
									    int localizacionAnatomica,
									    int generoMicro,
									    String especieMicro,
									    String bioTipoMicro,
									    int tipoMuestra1,
									    int locAnatomica1,
									    String fechaToma1,
									    String fechaRemision1,
									    String mIdentificacion1,
									    String pruAdicionales1,
									    int tipoMuestra2,
									    int locAnatomica2,
									    String fechaToma2,
									    String fechaRemision2,
									    String mIdentificacion2,
									    String pruAdicionales2,
									    int antibiotico1,
									    String sensibilidad1,
									    String tDosis1,
									    String fechaInicioAntibiotico1,
									    String fechaFinAntibiotico1,
									    int antibiotico2,
									    String sensibilidad2,
									    String tDosis2,
									    String fechaInicioAntibiotico2,
									    String fechaFinAntibiotico2,
									    int antibiotico3,
									    String sensibilidad3,
									    String tDosis3,
									    String fechaInicioAntibiotico3,
									    String fechaFinAntibiotico3,
									    int clasificacionCaso,
									    HashMap factoresRiesgo,
									    int estadoAnterior,
									    int unidadGeneradora,
									    String medicosTratantes,
									    boolean activa,
									    String servicio2,
									    HashMap microorganismos
									   )
	{
		return SqlBaseFichaInfeccionesDao.insertarFichaCompleta(con,
																numeroSolicitud,
																login,
																codigoPaciente,
																codigoDiagnostico,
																estado,
																codigoAseguradora,
																nombreProfesional,
															    secuenciaStr,
															    
															    numeroCama,
															    servicio,
															    fechaIngreso,
															    fechaDxIh,
															    fechaEgreso,
															    fechaDxHisto,
															    dxHospital,
															    dxIh,
															    dxEgreso,
															    dxHisto,
															    localizacionAnatomica,
															    generoMicro,
															    especieMicro,
															    bioTipoMicro,
															    tipoMuestra1,
															    locAnatomica1,
															    fechaToma1,
															    fechaRemision1,
															    mIdentificacion1,
															    pruAdicionales1,
															    tipoMuestra2,
															    locAnatomica2,
															    fechaToma2,
															    fechaRemision2,
															    mIdentificacion2,
															    pruAdicionales2,
															    antibiotico1,
															    sensibilidad1,
															    tDosis1,
															    fechaInicioAntibiotico1,
															    fechaFinAntibiotico1,
															    antibiotico2,
															    sensibilidad2,
															    tDosis2,
															    fechaInicioAntibiotico2,
															    fechaFinAntibiotico2,
															    antibiotico3,
															    sensibilidad3,
															    tDosis3,
															    fechaInicioAntibiotico3,
															    fechaFinAntibiotico3,
															    clasificacionCaso,
															    factoresRiesgo,
															    estadoAnterior,
															    unidadGeneradora,
															    medicosTratantes,
															    activa,
															    servicio2,
															    microorganismos,
															    secuenciaMicro);
	}
	
	
	
	
	

	public ResultSet consultarMicros(Connection con, int codigo)
	{
		return SqlBaseFichaInfeccionesDao.consultarMicros(con,codigo);
	}
}
