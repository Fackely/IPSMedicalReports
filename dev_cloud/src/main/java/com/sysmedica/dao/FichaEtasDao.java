package com.sysmedica.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

public interface FichaEtasDao {


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
										   );
	
	
	

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
									    );
	
	
	
	
	public ResultSet consultaTodoFicha(Connection con, int codigo);
	
	
	
	public ResultSet consultarDatosPaciente(Connection con, int codigo, boolean empezarnuevo);
	
	
	
	public ResultSet consultarSintomas(Connection con, int codigo);
	
	
	
	public ResultSet consultarAlimentos(Connection con, int codigo);
}
