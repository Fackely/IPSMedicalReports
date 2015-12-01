package com.sysmedica.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

public interface FichaSifilisDao {

	/**
	 * Metodo para insertar una ficha de Sifilis Congenita
	 * @param con
	 * @param notificar
	 * @param loginUsuario
	 * @param codigoFichaSifilis
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
							    int codigoFichaSifilis,
							    int codigoPaciente,
							    String codigoDiagnostico,
							    int codigoNotificacion,
							    int numeroSolicitud,
							    int codigoAseguradora,
								String nombreProfesional,
							    int estado
							    );
	
	
	public int modificarFicha(Connection con,
								String sire,
								String loginUsuario,
							    int codigoFichaParalisis,
							    int estado,
							    boolean controlPrenatal,
								int edadGestacional,
								int numeroControles,
								int edadGestacionalSero1,
								int edadGestacionalDiag1,
								int edadGestacionalTrat,
								int edadGestacionalParto,
								int estadoNacimiento,
								int recienNacido,
								int lugarAtencionParto,
								boolean recibioTratamiento,
								String medicamentoAdmin,
								int dosisAplicadas,
								boolean tratamientoHospitalario,
								boolean tratamientoAmbulatorio,
								boolean diagnosticoContactos,
								boolean tratamientoContactos,
								boolean diagnosticoIts,
								String cualesIts,
								String observaciones,
								
								HashMap signosRecienNacido,
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
							    int condicionMomentoDx,
							    int tipoTratamiento,
							    int otrasIts,
							    int esquemaCompleto,
							    int alergiaPenicilina,
							    int desensibilizaPenicilina,
							    String pais,
							    int areaProcedencia
								);
	
	
	public ResultSet consultaTodoFicha(Connection con, int codigo);
	
	
	public ResultSet consultarSignos(Connection con, int codigo);
	
	
	public ResultSet consultarDatosLaboratorio(Connection con, int codigo);
	
	
	public ResultSet consultaDatosPaciente(Connection con, int codigo,boolean empezarnuevo);
	
	
	public int insertarFichaCompleta(Connection con,
											int numeroSolicitud,
											String login,
											int codigoPaciente,
											String codigoDiagnostico,
											int estado,
											int codigoAseguradora,
											String nombreProfesional,
										    
										    String sire,
										    boolean controlPrenatal,
											int edadGestacional,
											int numeroControles,
											int edadGestacionalSero1,
											int edadGestacionalDiag1,
											int edadGestacionalTrat,
											int edadGestacionalParto,
											int estadoNacimiento,
											int recienNacido,
											int lugarAtencionParto,
											boolean recibioTratamiento,
											String medicamentoAdmin,
											int dosisAplicadas,
											boolean tratamientoHospitalario,
											boolean tratamientoAmbulatorio,
											boolean diagnosticoContactos,
											boolean tratamientoContactos,
											boolean diagnosticoIts,
											String cualesIts,
											String observaciones,
											
											HashMap signosRecienNacido,
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
										    boolean activa,
										    int condicionMomentoDx,
										    int tipoTratamiento,
										    int otrasIts,
										    int esquemaCompleto,
										    int alergiaPenicilina,
										    int desensibilizaPenicilina,
										    String pais,
										    int areaProcedencia
										    );
	
	
	public int terminarFicha(Connection con, int codigoFichaSifilis);
}
