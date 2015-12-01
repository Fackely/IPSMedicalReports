package com.sysmedica.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

public interface FichaInfeccionesDao {

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
							    );
	
	
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
							    HashMap microorganismos);
	
	
	public ResultSet consultaTodoFicha(Connection con, int codigo);
	
	
	public ResultSet consultarFactoresRiesgo(Connection con, int codigo);
	
	
	public ResultSet consultaDatosPaciente(Connection con, int codigo, boolean empezarnuevo);
	
	
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
										   );
	
	
	
	
	
	
	public ResultSet consultarMicros(Connection con, int codigo);
}
