package com.sysmedica.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

public interface FichaMalariaDao {


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
										    
										    int viajo,
										    String codDepViajo,
										    String codMunViajo,
										    int padecioMalaria,
										    String fechaAproximada,
										    int automedicacion,
										    int antecedenteTrans,
										    String fechaAntecedente,
										    int tipoComplicacion,
										    int especiePlasmodium,
										    int embarazo,
										    int tratAntimalarico,
										    String cualTratamiento,
										    HashMap sintomas,
										    HashMap tratamiento,
										    
										    boolean activa,
										    String pais,
										    int areaProcedencia,
										    String lugarViajo
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

									    int viajo,
									    String codDepViajo,
									    String codMunViajo,
									    int padecioMalaria,
									    String fechaAproximada,
									    int automedicacion,
									    int antecedenteTrans,
									    String fechaAntecedente,
									    int tipoComplicacion,
									    int especiePlasmodium,
									    int embarazo,
									    int tratAntimalarico,
									    String cualTratamiento,
									    HashMap sintomas,
									    HashMap tratamiento,
									    
										String pais,
									    int areaProcedencia,
									    String lugarViajo
									    );
	
	
	
	

	public ResultSet consultaTodoFicha(Connection con, int codigo);
	
	
	public ResultSet consultarDatosPaciente(Connection con, int codigo, boolean empezarnuevo);
	
	
	public ResultSet consultarSintomas(Connection con, int codigo);
	
	
	public ResultSet consultarTratamiento(Connection con, int codigo);
	
	
	public ResultSet consultarDatosLaboratorio(Connection con, int codigo);
}
