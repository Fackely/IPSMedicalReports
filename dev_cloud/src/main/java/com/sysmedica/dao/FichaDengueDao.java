package com.sysmedica.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

public interface FichaDengueDao {

	
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
							    );
	
	
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
							    );
	
	
	public ResultSet consultaTodoFicha(Connection con, int codigo);
	
	
	public ResultSet consultarHallazgos(Connection con, int codigo);
	
	
	public ResultSet consultarDatosLaboratorio(Connection con, int codigo);
	
	
	
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
										    );
	
	
	public int terminarFicha(Connection con, int codigoFichaDengue);
}
