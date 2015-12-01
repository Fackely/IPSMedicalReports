package com.sysmedica.dao;

import java.sql.Connection;
import java.sql.ResultSet;

public interface FichaDifteriaDao {


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
										    
										    String nombrePadre,
										    String fechaInvestigacion,
										    int casoIdentificadoPor,
										    int contactoCasoConfirmado,
										    int carneVacunacion,
										    int dosisAplicadas,
										    int tipoVacuna,
										    String cualVacuna,
										    String fechaUltimaDosis,
										    int fiebre,
										    int amigdalitis,
										    int faringitis,
										    int laringitis,
										    int membranas,
										    int complicaciones,
										    int tipoComplicacion,
										    int tratAntibiotico,
										    String tipoAntibiotico,
										    String duracionTratamiento,
										    int antitoxina,
										    String dosisAntitoxina,
										    String fechaAplicacionAntitox,
										    int investigacionCampo,
										    String fechaOperacionBarrido,
										    int numeroContactos,
										    int quimioprofilaxis,
										    int poblacionGrupo1,
										    int poblacionGrupo2,
										    int poblacionGrupo3,
										    int dosisDpt1Grupo1,
										    int dosisDpt1Grupo2,
										    int dosisDpt1Grupo3,
										    int dosisDpt2Grupo1,
										    int dosisDpt2Grupo2,
										    int dosisDpt2Grupo3,  
										    int dosisDpt3Grupo1,
										    int dosisDpt3Grupo2,
										    int dosisDpt3Grupo3,
										    int dosisRef1Grupo1,
										    int dosisRef1Grupo2,
										    int dosisRef1Grupo3,
										    int dosisRef2Grupo1,
										    int dosisRef2Grupo2,
										    int dosisRef2Grupo3,
										    String municipiosVacunados,
										    
										    boolean activa,
										    String pais,
										    int areaProcedencia
										   );
	
	

	public int modificarFicha(Connection con,
										String sire,
										boolean notificar,
									    String loginUsuario,
									    int codigoFichaLepra,
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

									    String nombrePadre,
									    String fechaInvestigacion,
									    int casoIdentificadoPor,
									    int contactoCasoConfirmado,
									    int carneVacunacion,
									    int dosisAplicadas,
									    int tipoVacuna,
									    String cualVacuna,
									    String fechaUltimaDosis,
									    int fiebre,
									    int amigdalitis,
									    int faringitis,
									    int laringitis,
									    int membranas,
									    int complicaciones,
									    int tipoComplicacion,
									    int tratAntibiotico,
									    String tipoAntibiotico,
									    String duracionTratamiento,
									    int antitoxina,
									    String dosisAntitoxina,
									    String fechaAplicacionAntitox,
									    int investigacionCampo,
									    String fechaOperacionBarrido,
									    int numeroContactos,
									    int quimioprofilaxis,
									    int poblacionGrupo1,
									    int poblacionGrupo2,
									    int poblacionGrupo3,
									    int dosisDpt1Grupo1,
									    int dosisDpt1Grupo2,
									    int dosisDpt1Grupo3,
									    int dosisDpt2Grupo1,
									    int dosisDpt2Grupo2,
									    int dosisDpt2Grupo3,  
									    int dosisDpt3Grupo1,
									    int dosisDpt3Grupo2,
									    int dosisDpt3Grupo3,
									    int dosisRef1Grupo1,
									    int dosisRef1Grupo2,
									    int dosisRef1Grupo3,
									    int dosisRef2Grupo1,
									    int dosisRef2Grupo2,
									    int dosisRef2Grupo3,
									    String municipiosVacunados,
										String pais,
									    int areaProcedencia
									    );
	
	
	
	

	public ResultSet consultaTodoFicha(Connection con, int codigo);
	
	
	
	public ResultSet consultarDatosPaciente(Connection con, int codigo, boolean empezarnuevo);
	
	
	
	public ResultSet consultarDatosLaboratorio(Connection con, int codigo);
}
