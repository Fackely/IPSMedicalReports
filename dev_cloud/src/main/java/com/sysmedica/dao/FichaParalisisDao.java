package com.sysmedica.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

public interface FichaParalisisDao {

	/**
	 * Metodo para insertar una ficha de Paralisis Flacida
	 * @param con
	 * @param notificar
	 * @param loginUsuario
	 * @param codigoFichaParalisis
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
							    int codigoFichaParalisis,
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
								String nombreMadre,
							    String nombrePadre,
							    String fechaInicioInvestigacion,
							    int numeroDosis,
							    String fechaUltimaDosis,
							    int tieneCarnet,
							    int fiebre,
							    int respiratorios,
							    int digestivos,
							    int instalacion,
							    int dolorMuscular,
							    int signosMeningeos1,
							    int fiebreInicioParalisis,
							    int progresion,
							    String fechaInicioParalisis,
							    int musculosRespiratorios,
							    int signosMeningeos2,
							    int babinsky,
							    int brudzinsky,
							    int paresCraneanos,
							    int liquidoCefalo,
							    String fechaTomaLiquido,
							    int celulas,
							    int globulosRojos,
							    int leucocitos,
							    int proteinas,
							    int glucosa,
							    int electromiografia,
							    String fechaTomaElectro,
							    int velocidadConduccion,
							    int resultadoConduccion,
							    String fechaTomaVelocidad,
							    String impresionDiagnostica,
							    int muestraMateriaFecal,
							    String fechaTomaFecal,
							    String fechaEnvioFecal,
							    String fechaRecepcionFecal,
							    String fechaResultadoFecal,
							    int virusAislado,
							    String fechaVacunacionBloqueo,
							    String fechaCulminacionVacunacion,
							    String municipiosVacunados,
							    int codigoExtremidad1,
							    int codigoExtremidad2,
							    int codigoExtremidad3,
							    int codigoExtremidad4,
							    int codigoGrupoEdad1,
							    int codigoGrupoEdad2,
							    int codigoGrupoEdad3,
							    
							    HashMap datosExtremidades,
							    HashMap datosGrupoEdad,
							    
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
							    String pais,
							    int areaProcedencia
							    );
	
	
	
	public ResultSet consultaTodoFicha(Connection con, int codigo);
	
	
	
	public ResultSet consultarDatosExtremidades(Connection con, int codigo);
	
	
	public ResultSet consultarDatosGrupoEdad(Connection con, int codigo);
	
	
	
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
										    String nombreMadre,
										    String nombrePadre,
										    String fechaInicioInvestigacion,
										    int numeroDosis,
										    String fechaUltimaDosis,
										    int tieneCarnet,
										    int fiebre,
										    int respiratorios,
										    int digestivos,
										    int instalacion,
										    int dolorMuscular,
										    int signosMeningeos1,
										    int fiebreInicioParalisis,
										    int progresion,
										    String fechaInicioParalisis,
										    int musculosRespiratorios,
										    int signosMeningeos2,
										    int babinsky,
										    int brudzinsky,
										    int paresCraneanos,
										    int liquidoCefalo,
										    String fechaTomaLiquido,
										    int celulas,
										    int globulosRojos,
										    int leucocitos,
										    int proteinas,
										    int glucosa,
										    int electromiografia,
										    String fechaTomaElectro,
										    int velocidadConduccion,
										    int resultadoConduccion,
										    String fechaTomaVelocidad,
										    String impresionDiagnostica,
										    int muestraMateriaFecal,
										    String fechaTomaFecal,
										    String fechaEnvioFecal,
										    String fechaRecepcionFecal,
										    String fechaResultadoFecal,
										    int virusAislado,
										    String fechaVacunacionBloqueo,
										    String fechaCulminacionVacunacion,
										    String municipiosVacunados,
										    int codigoExtremidad1,
										    int codigoExtremidad2,
										    int codigoExtremidad3,
										    int codigoExtremidad4,
										    int codigoGrupoEdad1,
										    int codigoGrupoEdad2,
										    int codigoGrupoEdad3,
										    
										    HashMap datosExtremidades,
										    HashMap datosGrupoEdad,
										    
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
										    String telefonoContacto,
										    boolean activa,
										    String pais,
										    int areaProcedencia
										   );
}
