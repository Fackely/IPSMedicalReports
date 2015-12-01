package com.sysmedica.dao;

import java.sql.Connection;
import java.sql.ResultSet;

public interface FichaTuberculosisDao {

	/**
	 * Metodo para insertar una ficha de tuberculosis
	 * @param con
	 * @param notificar
	 * @param loginUsuario
	 * @param codigoFichaTuberculosis
	 * @param codigoPaciente
	 * @param codigoDiagnostico
	 * @param codigoNotificacion
	 * @param numeroSolicitud
	 * @param codigoAseguradora
	 * @param nombreProfesional
	 * @param estado
	 * @return
	 */
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
							    );
	
	
	/**
	 * Metodo para modificar una ficha de tuberculosis
	 * @param con
	 * @param sire
	 * @param loginUsuario
	 * @param codigoFichaParalisis
	 * @param estado
	 * @param baciloscopia
	 * @param cultivo
	 * @param histopatologia
	 * @param clinicaPaciente
	 * @param nexoEpidemiologico
	 * @param radiologico
	 * @param tuberculina
	 * @param ada
	 * @param resultadoBk
	 * @param resultadoAda
	 * @param resultadoTuberculina
	 * @param tipoTuberculosis
	 * @param tieneCicatriz
	 * @param fuenteContagio
	 * @param metodoHallazgo
	 * @param otroCual
	 * @param asociadoVih
	 * @param asesoriaVih
	 * @param observaciones
	 * @param lugarProcedencia
	 * @param fechaConsultaGeneral
	 * @param fechaInicioSintomasGeneral
	 * @param tipoCaso
	 * @param hospitalizadoGeneral
	 * @param fechaHospitalizacionGeneral
	 * @param estaVivoGeneral
	 * @param fechaDefuncion
	 * @param lugarNoti
	 * @param unidadGeneradora
	 * @return
	 */
	public int modificarFicha(Connection con,
									String sire,
									String loginUsuario,
								    int codigoFichaParalisis,
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
									);
	
	
	/**
	 * Metodo para consultar una ficha de tuberculosis
	 * @param con
	 * @param codigo
	 * @return
	 */
	public ResultSet consultaTodoFicha(Connection con, int codigo);
	
	
	
	
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
										   );
}
