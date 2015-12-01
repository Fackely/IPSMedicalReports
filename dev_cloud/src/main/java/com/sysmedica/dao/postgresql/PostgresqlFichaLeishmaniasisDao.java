package com.sysmedica.dao.postgresql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

import com.sysmedica.dao.FichaLeishmaniasisDao;
import com.sysmedica.dao.sqlbase.SqlBaseFichaLeishmaniasisDao;

public class PostgresqlFichaLeishmaniasisDao implements FichaLeishmaniasisDao {

	private String secuenciaStr = "SELECT nextval('epidemiologia.seq_fichas')";
	private String secuenciaTam = "SELECT nextval('epidemiologia.seq_tam_lesion')";

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
										   )
	{
		return SqlBaseFichaLeishmaniasisDao.insertarFichaCompleta(con,
														    		numeroSolicitud,
																	login,
																	codigoPaciente,
																	codigoDiagnostico,
																	estado,
																	codigoAseguradora,
																	nombreProfesional,
																    secuenciaStr,
																    
																    sire,
																	notificar,
																	
																	codigoFichaIntoxicacion,										    
																    lugarProcedencia,
																    fechaConsultaGeneral,
																    fechaInicioSintomasGeneral,
																    tipoCaso,
																    hospitalizadoGeneral,
																    fechaHospitalizacionGeneral,
																    estaVivoGeneral,
																    fechaDefuncion,
																    lugarNoti,
																    unidadGeneradora,
																    
																    numeroLesiones,
																    localizacionLesiones,
																    anchoLesion1,
																    largoLesion1,
																    anchoLesion2,
																    largoLesion2,
																    anchoLesion3,
																    largoLesion3,
																    cicatrices,
																    tiempo,
																    unidadTiempo,
																    antecedenteTrauma,
																    mucosaAfectada,
																    rinorrea,
																    epistaxis,
																    obstruccion,
																    disfonia,
																    disfagia,
																    hiperemia,
																    ulceracion,
																    perforacion,
																    destruccion,
																    fiebre,
																    hepatomegalia,
																    esplenomegalia,
																    anemia,
																    leucopenia,
																    trombocitopenia,
																    recibioTratamiento,
																    numeroVeces,
																    medicamentoRecibio,
																    otroMedicamento,
																    pesoPaciente,
																    volumenDiario,
																    diasTratamiento,
																    totalAmpollas,
																    
																    activa,
																    pais,
																    areaProcedencia,
																    lesiones,
																    secuenciaTam,
																    
																    cara,
																    tronco,
																    superiores,
																    inferiores
																    );
	}
	
	
	
	
	

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
									    )
	{
		return SqlBaseFichaLeishmaniasisDao.modificarFicha(con,
															sire,
															notificar,
														    loginUsuario,
														    codigoFichaEasv,
														    codigoPaciente,
														    codigoDiagnostico,
														    codigoNotificacion,
														    numeroSolicitud,
														    estado,
														    
														    lugarProcedencia,
														    fechaConsultaGeneral,
														    fechaInicioSintomasGeneral,
														    tipoCaso,
														    hospitalizadoGeneral,
														    fechaHospitalizacionGeneral,
														    estaVivoGeneral,
														    fechaDefuncion,
														    lugarNoti,
														    unidadGeneradora,
											
														    numeroLesiones,
														    localizacionLesiones,
														    anchoLesion1,
														    largoLesion1,
														    anchoLesion2,
														    largoLesion2,
														    anchoLesion3,
														    largoLesion3,
														    cicatrices,
														    tiempo,
														    unidadTiempo,
														    antecedenteTrauma,
														    mucosaAfectada,
														    rinorrea,
														    epistaxis,
														    obstruccion,
														    disfonia,
														    disfagia,
														    hiperemia,
														    ulceracion,
														    perforacion,
														    destruccion,
														    fiebre,
														    hepatomegalia,
														    esplenomegalia,
														    anemia,
														    leucopenia,
														    trombocitopenia,
														    recibioTratamiento,
														    numeroVeces,
														    medicamentoRecibio,
														    otroMedicamento,
														    pesoPaciente,
														    volumenDiario,
														    diasTratamiento,
														    totalAmpollas,
															pais,
														    areaProcedencia,
														    lesiones,
														    secuenciaTam,
														    
														    cara,
														    tronco,
														    superiores,
														    inferiores
														    );
	}
	
	
	
	

	public ResultSet consultaTodoFicha(Connection con, int codigo)
	{
		return SqlBaseFichaLeishmaniasisDao.consultarTodoFichaLeishmaniasis(con,codigo);
	}
	
	
	
	public ResultSet consultarDatosPaciente(Connection con, int codigo, boolean empezarnuevo)
	{
		return SqlBaseFichaLeishmaniasisDao.consultarDatosPaciente(con,codigo,empezarnuevo);
	}
	
	
	
	public ResultSet consultarDatosLaboratorio(Connection con, int codigo)
	{
		return SqlBaseFichaLeishmaniasisDao.consultarDatosLaboratorio(con,codigo);
	}
}
