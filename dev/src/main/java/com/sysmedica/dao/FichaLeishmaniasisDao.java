package com.sysmedica.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

public interface FichaLeishmaniasisDao {


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
										    
										    String numeroLesiones,
										    int localizacionLesiones,
										    String anchoLesion1,
										    String largoLesion1,
										    String anchoLesion2,
										    String largoLesion2,
										    String anchoLesion3,
										    String largoLesion3,
										    int cicatrices,
										    String tiempo,
										    int unidadTiempo,
										    int antecedenteTrauma,
										    int mucosaAfectada,
										    int rinorrea,
										    int epistaxis,
										    int obstruccion,
										    int disfonia,
										    int disfagia,
										    int hiperemia,
										    int ulceracion,
										    int perforacion,
										    int destruccion,
										    int fiebre,
										    int hepatomegalia,
										    int esplenomegalia,
										    int anemia,
										    int leucopenia,
										    int trombocitopenia,
										    int recibioTratamiento,
										    String numeroVeces,
										    int medicamentoRecibio,
										    String otroMedicamento,
										    String pesoPaciente,
										    String volumenDiario,
										    String diasTratamiento,
										    String totalAmpollas,
										    
										    boolean activa,
										    String pais,
										    int areaProcedencia,
										    HashMap lesiones,
										    int cara,
										    int tronco,
										    int superiores,
										    int inferiores
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

									    String numeroLesiones,
									    int localizacionLesiones,
									    String anchoLesion1,
									    String largoLesion1,
									    String anchoLesion2,
									    String largoLesion2,
									    String anchoLesion3,
									    String largoLesion3,
									    int cicatrices,
									    String tiempo,
									    int unidadTiempo,
									    int antecedenteTrauma,
									    int mucosaAfectada,
									    int rinorrea,
									    int epistaxis,
									    int obstruccion,
									    int disfonia,
									    int disfagia,
									    int hiperemia,
									    int ulceracion,
									    int perforacion,
									    int destruccion,
									    int fiebre,
									    int hepatomegalia,
									    int esplenomegalia,
									    int anemia,
									    int leucopenia,
									    int trombocitopenia,
									    int recibioTratamiento,
									    String numeroVeces,
									    int medicamentoRecibio,
									    String otroMedicamento,
									    String pesoPaciente,
									    String volumenDiario,
									    String diasTratamiento,
									    String totalAmpollas,
										String pais,
									    int areaProcedencia,
									    HashMap lesiones,
									    int cara,
									    int tronco,
									    int superiores,
									    int inferiores
									    );
	
	
	
	

	public ResultSet consultaTodoFicha(Connection con, int codigo);
	
	
	
	public ResultSet consultarDatosPaciente(Connection con, int codigo, boolean empezarnuevo);
	
	
	
	public ResultSet consultarDatosLaboratorio(Connection con, int codigo);
}
