package com.sysmedica.dao.oracle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

import com.sysmedica.dao.FichaSifilisDao;
import com.sysmedica.dao.sqlbase.SqlBaseFichaSifilisDao; 

public class OracleFichaSifilisDao implements FichaSifilisDao {

	private String secuenciaStr = "SELECT seq_fichas.nextval FROM dual";
	
	public int insertarFicha(Connection con,
								boolean notificar,
							    String loginUsuario,
							    int codigoFichaSifilis,
							    int codigoPaciente,
							    String codigoDiagnostico,
							    int codigoNotificacion,
							    int numeroSolicitud,
							    int codigoAseguradora,
								String nombreProfesional,
							    int estado
							    )
	{
		return SqlBaseFichaSifilisDao.insertarFicha(con,
													numeroSolicitud,
													loginUsuario,
													codigoPaciente,
													codigoDiagnostico,
													estado,
													codigoAseguradora,
													nombreProfesional,
												    secuenciaStr);
	}
	
	
	
	public int modificarFicha(Connection con,
								String sire,
								String loginUsuario,
							    int codigoFichaSifilis,
							    int estado,
							    boolean controlPrenatal,
								int edadGestacional,
								int numeroControles,
								int edadGestacionalSero1,
								int edadGestacionalDiag1,
								int edadGestacionalTrat,
								int edadGestacionalParto,
								int estadoNacimiento,
								int recienNacido,
								int lugarAtencionParto,
								boolean recibioTratamiento,
								String medicamentoAdmin,
								int dosisAplicadas,
								boolean tratamientoHospitalario,
								boolean tratamientoAmbulatorio,
								boolean diagnosticoContactos,
								boolean tratamientoContactos,
								boolean diagnosticoIts,
								String cualesIts,
								String observaciones,
								
								HashMap signosRecienNacido,
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
							    int condicionMomentoDx,
							    int tipoTratamiento,
							    int otrasIts,
							    int esquemaCompleto,
							    int alergiaPenicilina,
							    int desensibilizaPenicilina,
							    String pais,
							    int areaProcedencia
								)
	{
		return SqlBaseFichaSifilisDao.modificarFicha(con,
														sire,
														loginUsuario,
													    codigoFichaSifilis,
													    estado,
													    
													    controlPrenatal,
														edadGestacional,
														numeroControles,
														edadGestacionalSero1,
														edadGestacionalDiag1,
														edadGestacionalTrat,
														edadGestacionalParto,
														estadoNacimiento,
														recienNacido,
														lugarAtencionParto,
														recibioTratamiento,
														medicamentoAdmin,
														dosisAplicadas,
														tratamientoHospitalario,
														tratamientoAmbulatorio,
														diagnosticoContactos,
														tratamientoContactos,
														diagnosticoIts,
														cualesIts,
														observaciones,
														
														signosRecienNacido,
														datosLaboratorio,
														
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
													    estadoAnterior,
													    condicionMomentoDx,
													    tipoTratamiento,
													    otrasIts,
													    esquemaCompleto,
													    alergiaPenicilina,
													    desensibilizaPenicilina,
													    pais,
													    areaProcedencia
														);
	}
	
	
	
	public ResultSet consultaTodoFicha(Connection con, int codigo)
	{
		return SqlBaseFichaSifilisDao.consultarTodoFichaSifilis(con,codigo);
	}
	
	
	
	public ResultSet consultarSignos(Connection con, int codigo)
	{
		return SqlBaseFichaSifilisDao.consultarSignos(con,codigo);
	}
	
	
	public ResultSet consultarDatosLaboratorio(Connection con, int codigo)
	{
		return SqlBaseFichaSifilisDao.consultarDatosLaboratorio(con,codigo);
	}
	
	
	
	public ResultSet consultaDatosPaciente(Connection con, int codigo,boolean empezarnuevo)
    {
    	return SqlBaseFichaSifilisDao.consultarDatosPaciente(con,codigo,empezarnuevo);
    }
	
	
	
	public int insertarFichaCompleta(Connection con,
										int numeroSolicitud,
										String login,
										int codigoPaciente,
										String codigoDiagnostico,
										int estado,
										int codigoAseguradora,
										String nombreProfesional,
									    
									    String sire,
									    boolean controlPrenatal,
										int edadGestacional,
										int numeroControles,
										int edadGestacionalSero1,
										int edadGestacionalDiag1,
										int edadGestacionalTrat,
										int edadGestacionalParto,
										int estadoNacimiento,
										int recienNacido,
										int lugarAtencionParto,
										boolean recibioTratamiento,
										String medicamentoAdmin,
										int dosisAplicadas,
										boolean tratamientoHospitalario,
										boolean tratamientoAmbulatorio,
										boolean diagnosticoContactos,
										boolean tratamientoContactos,
										boolean diagnosticoIts,
										String cualesIts,
										String observaciones,
										
										HashMap signosRecienNacido,
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
									    boolean activa,
									    int condicionMomentoDx,
									    int tipoTratamiento,
									    int otrasIts,
									    int esquemaCompleto,
									    int alergiaPenicilina,
									    int desensibilizaPenicilina,
									    String pais,
									    int areaProcedencia
									    )
	{
		return SqlBaseFichaSifilisDao.insertarFichaCompleta(con,
															numeroSolicitud,
															login,
															codigoPaciente,
															codigoDiagnostico,
															estado,
															codigoAseguradora,
															nombreProfesional,
														    secuenciaStr,
														    
														    sire,
														    controlPrenatal,
															edadGestacional,
															numeroControles,
															edadGestacionalSero1,
															edadGestacionalDiag1,
															edadGestacionalTrat,
															edadGestacionalParto,
															estadoNacimiento,
															recienNacido,
															lugarAtencionParto,
															recibioTratamiento,
															medicamentoAdmin,
															dosisAplicadas,
															tratamientoHospitalario,
															tratamientoAmbulatorio,
															diagnosticoContactos,
															tratamientoContactos,
															diagnosticoIts,
															cualesIts,
															observaciones,
															
															signosRecienNacido,
															datosLaboratorio,
															
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
														    estadoAnterior,
														    activa,
														    condicionMomentoDx,
														    tipoTratamiento,
														    otrasIts,
														    esquemaCompleto,
														    alergiaPenicilina,
														    desensibilizaPenicilina,
														    pais,
														    areaProcedencia);
	}
	
	
	public int terminarFicha(Connection con, int codigoFichaSifilis)
    {
    	return SqlBaseFichaSifilisDao.terminarFicha(con,codigoFichaSifilis);
    }
}
