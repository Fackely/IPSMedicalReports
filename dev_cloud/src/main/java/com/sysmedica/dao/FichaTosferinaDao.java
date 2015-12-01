package com.sysmedica.dao;

import java.sql.Connection;
import java.sql.ResultSet;

public interface FichaTosferinaDao {


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
										    int identificacionCaso,
										    int contactoCaso,
										    int carneVacunacion,
										    int dosisAplicadas,
										    int tipoVacuna,
										    String cualVacuna,
										    String fechaUltimaDosis,
										    int etapaEnfermedad,
										    int tos,
										    String duracionTos,
										    int tosParoxistica,
										    int estridor,
										    int apnea,
										    int fiebre,
										    int vomitoPostusivo,
										    int complicaciones,
										    int tipoComplicacion,
										    int tratamientoAntibiotico,
										    String tipoAntibiotico,
										    String duracionTratamiento,
										    int investigacionCampo,
										    String fechaOperacionBarrido,
										    String numeroContactos,
										    int quimioprofilaxis,
										    int totalPoblacion1,
										    int totalPoblacion2,
										    int totalPoblacion3,
										    int totalPoblacion4,
										    int dosisDpt1Grupo1,
										    int dosisDpt1Grupo2,
										    int dosisDpt1Grupo3,
										    int dosisDpt1Grupo4,
										    int dosisDpt2Grupo1,
										    int dosisDpt2Grupo2,
										    int dosisDpt2Grupo3,
										    int dosisDpt2Grupo4,
										    int dosisDpt3Grupo1,
										    int dosisDpt3Grupo2,
										    int dosisDpt3Grupo3,
										    int dosisDpt3Grupo4,
										    int dosisRef1Grupo1,
										    int dosisRef1Grupo2,
										    int dosisRef1Grupo3,
										    int dosisRef1Grupo4,
										    int dosisRef2Grupo1,
										    int dosisRef2Grupo2,
										    int dosisRef2Grupo3,
										    int dosisRef2Grupo4,
										    String municipiosVacunados,
										    
										    boolean activa,
										    String pais,
										    int areaProcedencia
										   );
	
	
	
	

	public int modificarFicha(Connection con,
										String sire,
										boolean notificar,
									    String loginUsuario,
									    int codigoFichaTosferina,
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
									    int identificacionCaso,
									    int contactoCaso,
									    int carneVacunacion,
									    int dosisAplicadas,
									    int tipoVacuna,
									    String cualVacuna,
									    String fechaUltimaDosis,
									    int etapaEnfermedad,
									    int tos,
									    String duracionTos,
									    int tosParoxistica,
									    int estridor,
									    int apnea,
									    int fiebre,
									    int vomitoPostusivo,
									    int complicaciones,
									    int tipoComplicacion,
									    int tratamientoAntibiotico,
									    String tipoAntibiotico,
									    String duracionTratamiento,
									    int investigacionCampo,
									    String fechaOperacionBarrido,
									    String numeroContactos,
									    int quimioprofilaxis,
									    int totalPoblacion1,
									    int totalPoblacion2,
									    int totalPoblacion3,
									    int totalPoblacion4,
									    int dosisDpt1Grupo1,
									    int dosisDpt1Grupo2,
									    int dosisDpt1Grupo3,
									    int dosisDpt1Grupo4,
									    int dosisDpt2Grupo1,
									    int dosisDpt2Grupo2,
									    int dosisDpt2Grupo3,
									    int dosisDpt2Grupo4,
									    int dosisDpt3Grupo1,
									    int dosisDpt3Grupo2,
									    int dosisDpt3Grupo3,
									    int dosisDpt3Grupo4,
									    int dosisRef1Grupo1,
									    int dosisRef1Grupo2,
									    int dosisRef1Grupo3,
									    int dosisRef1Grupo4,
									    int dosisRef2Grupo1,
									    int dosisRef2Grupo2,
									    int dosisRef2Grupo3,
									    int dosisRef2Grupo4,
									    String municipiosVacunados,
										String pais,
									    int areaProcedencia
									    );
	
	
	
	
	

	public ResultSet consultaTodoFicha(Connection con, int codigo);
	
	
	
	public ResultSet consultarDatosPaciente(Connection con, int codigo, boolean empezarnuevo);
	
	
	
	public ResultSet consultarDatosLaboratorio(Connection con, int codigo);
}
