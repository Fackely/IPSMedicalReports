package com.sysmedica.dao.postgresql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

import com.sysmedica.dao.sqlbase.SqlBaseFichaVIHDao;
import com.sysmedica.dao.FichaVIHDao;

public class PostgresqlFichaVIHDao implements FichaVIHDao {

	private String secuenciaStr = "SELECT nextval('epidemiologia.seq_fichas')";
    
    /**
     * String con el statement para obtener el valor de la secuencia de las notificaciones (para el codigo)
     */
    private String secuenciaNotificacionesStr = "SELECT nextval('seq_notificaciones')";
    
    
	public int insertarFicha(Connection con,
								boolean notificar,
							    String loginUsuario,
							    int codigoFichaVIH,
							    int codigoPaciente,
							    String codigoDiagnostico,
							    int codigoNotificacion,
							    int numeroSolicitud,
							    int estado,
							    int codigoAseguradora,
								String nombreProfesional
							    )
	{
		return SqlBaseFichaVIHDao.insertarFicha(con,
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
								boolean notificar,
							    String loginUsuario,
							    int codigoFichaVIH,
							    int codigoPaciente,
							    String codigoDiagnostico,
							    int codigoNotificacion,
							    int numeroSolicitud,
							    int estado,
							    HashMap mecanismosTransmision,
							    int tipoMuestra,
							    int tipoPrueba,
							    int resultado,
							    String fechaResultado,
							    String valor,
							    int estadioClinico,
							    int numeroHijos,
							    int numeroHijas,
							    int embarazo,
							    int numeroSemanas,
							    HashMap enfermedadesAsociadas,
							    
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
							    int areaProcedencia)
	{
		return SqlBaseFichaVIHDao.modificarFicha(con,
													sire,
													notificar,
												    loginUsuario,
												    codigoFichaVIH,
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
												    
												    mecanismosTransmision,
												    tipoMuestra,
												    tipoPrueba,
												    resultado,
												    fechaResultado,
												    valor,
												    estadioClinico,
												    numeroHijos,
												    numeroHijas,
												    embarazo,
												    numeroSemanas,
												    enfermedadesAsociadas,
												    secuenciaNotificacionesStr,
												    estadoAnterior,
												    pais,
												    areaProcedencia);
	}


	public ResultSet consultaTodoFicha(Connection con, int codigo)
	{
		return SqlBaseFichaVIHDao.consultarTodoFichaVIH(con,codigo);
	}
	
	
	public ResultSet consultarMecanismosTransmision(Connection con, int codigo)
	{
		return SqlBaseFichaVIHDao.consultarMecanismosTransmision(con,codigo);
	}

	public ResultSet consultarEnfermedadesAsociadas(Connection con, int codigo)
	{
		return SqlBaseFichaVIHDao.consultarEnfermedadesAsociadas(con,codigo);
	}
	
	
	public ResultSet consultaDatosPaciente(Connection con, int codigo, boolean empezarnuevo)
    {
    	return SqlBaseFichaVIHDao.consultarDatosPaciente(con,codigo,empezarnuevo);
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
										boolean notificar,
										
										int codigoFichaVIH,										    
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
									    
									    HashMap mecanismosTransmision,
									    int tipoMuestra,
									    int tipoPrueba,
									    int resultado,
									    String fechaResultado,
									    String valor,
									    int estadioClinico,
									    int numeroHijos,
									    int numeroHijas,
									    int embarazo,
									    int numeroSemanas,
									    HashMap enfermedadesAsociadas,
									    int estadoAnterior,
									    boolean activa,
									    String pais,
									    int areaProcedencia
									   )
	{
		return SqlBaseFichaVIHDao.insertarFichaCompleta(con,
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
														
														codigoFichaVIH,										    
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
													    
													    mecanismosTransmision,
													    tipoMuestra,
													    tipoPrueba,
													    resultado,
													    fechaResultado,
													    valor,
													    estadioClinico,
													    numeroHijos,
													    numeroHijas,
													    embarazo,
													    numeroSemanas,
													    enfermedadesAsociadas,
													    estadoAnterior,
													    activa,
													    pais,
													    areaProcedencia);
	}
}
