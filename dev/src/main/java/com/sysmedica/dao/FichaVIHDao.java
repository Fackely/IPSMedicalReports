package com.sysmedica.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

public interface FichaVIHDao {

	/**
	 * Metodo para insertar una ficha de VIH
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
							    int estado,
							    int codigoAseguradora,
								String nombreProfesional
							    );
	
	
	/**
	 * Metodo para modificar una ficha de VIH
	 * @param con
	 * @param notificar
	 * @param loginUsuario
	 * @param codigoFichaVIH
	 * @param codigoPaciente
	 * @param codigoDiagnostico
	 * @param codigoNotificacion
	 * @param numeroSolicitud
	 * @param estado
	 * @param mecanismosTransmision
	 * @param tipoMuestra
	 * @param tipoPrueba
	 * @param resultado
	 * @param fechaResultado
	 * @param valor
	 * @param estadioClinico
	 * @param numeroHijos
	 * @param numeroHijas
	 * @param embarazo
	 * @param numeroSemanas
	 * @param enfermedadesAsociadas
	 * @return
	 */
	public int modificarFicha(Connection con,
								String sire,
								boolean notificar,
							    String loginUsuario,
							    int codigoFichaVIH,
							    int codigoPaciente,
							    String codigoDiagnostico,
							    int codigoNotificacion,
							    int numeroSolicitud,
							    int estado,
							    HashMap mecanismosTransmision,
							    int tipoMuestra,
							    int tipoPrueba,
							    int resultado,
							    String fechaResultado,
							    String valor,
							    int estadioClinico,
							    int numeroHijos,
							    int numeroHijas,
							    int embarazo,
							    int numeroSemanas,
							    HashMap enfermedadesAsociadas,
							    
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
							    int areaProcedencia);
	
	
	/**
	 * Metodo para consultar todos los atributos de una ficha de VIH
	 * @param con
	 * @param codigo
	 * @return
	 */
	public ResultSet consultaTodoFicha(Connection con, int codigo);
	
	
	public ResultSet consultarMecanismosTransmision(Connection con, int codigo);
	
	public ResultSet consultarEnfermedadesAsociadas(Connection con, int codigo);
	
	
	public ResultSet consultaDatosPaciente(Connection con, int codigo, boolean empezarnuevo);
	
	
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
											
											int codigoFichaVIH,										    
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
										    
										    HashMap mecanismosTransmision,
										    int tipoMuestra,
										    int tipoPrueba,
										    int resultado,
										    String fechaResultado,
										    String valor,
										    int estadioClinico,
										    int numeroHijos,
										    int numeroHijas,
										    int embarazo,
										    int numeroSemanas,
										    HashMap enfermedadesAsociadas,
										    int estadoAnterior,
										    boolean activa,
										    String pais,
										    int areaProcedencia
										   );
}
