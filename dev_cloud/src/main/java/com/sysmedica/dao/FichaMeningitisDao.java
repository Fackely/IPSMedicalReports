package com.sysmedica.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

public interface FichaMeningitisDao {


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
										   );
	
	
	
	

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
									    );
	
	
	
	

	public ResultSet consultaTodoFicha(Connection con, int codigo);
	
	
	
	public ResultSet consultarDatosPaciente(Connection con, int codigo, boolean empezarnuevo);
	
	
	
	public ResultSet consultarDatosLaboratorio(Connection con, int codigo);
}
