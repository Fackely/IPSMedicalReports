package com.sysmedica.dao.oracle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

import com.sysmedica.dao.sqlbase.SqlBaseFichaSarampionDao;
import com.sysmedica.dao.FichaSarampionDao;

public class OracleFichaSarampionDao implements FichaSarampionDao {

	private String secuenciaStr = "SELECT seq_fichas.nextval FROM dual";
    
    /**
     * String con el statement para obtener el valor de la secuencia de las notificaciones (para el codigo)
     */
    private String secuenciaNotificacionesStr = "SELECT seq_notificaciones.nextval FROM dual";
    
    public int insertarFicha(Connection con,
								boolean notificar,
							    String loginUsuario,
							    int codigoFichaSarampion,
							    int codigoPaciente,
							    String codigoDiagnostico,
							    int codigoNotificacion,
							    int numeroSolicitud,
							    int estado,
							    int codigoAseguradora,
								String nombreProfesional,
							    
							    String nombrePadre,
							    String ocupacionPadre,
							    String direccionTrabajoPadre,
							    String fechaVisita1,
							    int fuenteNotificacion,
							    int vacunaSarampion,
							    int numeroDosisSarampion,
							    String fechaUltimaDosisSarampion,
							    int fuenteDatosSarampion,
							    int vacunaRubeola,
							    int numeroDosisRubeola,
							    String fechaUltimaDosisRubeola,
							    int fuenteDatosRubeola,
							    
							    String fechaVisitaDomiciliaria,
							    int fiebre,
							    String fechaInicioFiebre,
							    int tipoErupcion,
							    String fechaInicioErupcion,
							    int duracionErupcion,
							    int tos,
							    int coriza,
							    int conjuntivitis,
							    int adenopatia,
							    int artralgia,
							    int embarazada,
							    int numeroSemanas,
							    String lugarParto,
							    
							    int huboContacto,
							    int huboCasoConfirmado,
							    int huboViaje,
							    String lugarViaje,
							    int huboContactoEmbarazada,
							    int diagnosticoFinal,
							    
							    HashMap datosLaboratorio)
    {
    	return SqlBaseFichaSarampionDao.insertarFicha(con,numeroSolicitud,loginUsuario,codigoPaciente,codigoDiagnostico,estado,codigoAseguradora,nombreProfesional,datosLaboratorio,secuenciaStr);
    }
    
    
    public int modificarFicha(Connection con,
    							String sire,
								boolean notificar,
							    String loginUsuario,
							    int codigoFichaSarampion,
							    int codigoPaciente,
							    String codigoDiagnostico,
							    int codigoNotificacion,
							    int numeroSolicitud,
							    int estado,
							    
							    String nombrePadre,
							    String ocupacionPadre,
							    String direccionTrabajoPadre,
							    String fechaVisita1,
							    int fuenteNotificacion,
							    int vacunaSarampion,
							    int numeroDosisSarampion,
							    String fechaUltimaDosisSarampion,
							    int fuenteDatosSarampion,
							    int vacunaRubeola,
							    int numeroDosisRubeola,
							    String fechaUltimaDosisRubeola,
							    int fuenteDatosRubeola,
							    
							    String fechaVisitaDomiciliaria,
							    int fiebre,
							    String fechaInicioFiebre,
							    int tipoErupcion,
							    String fechaInicioErupcion,
							    int duracionErupcion,
							    int tos,
							    int coriza,
							    int conjuntivitis,
							    int adenopatia,
							    int artralgia,
							    int embarazada,
							    int numeroSemanas,
							    String lugarParto,
							    
							    int huboContacto,
							    int huboCasoConfirmado,
							    int huboViaje,
							    String lugarViaje,
							    int huboContactoEmbarazada,
							    int diagnosticoFinal,
							    
							    String lugarProcedencia,
							    String fechaConsultaGeneral,
							    String fechaInicioSintomasGeneral,
							    int tipoCaso,
							    boolean hospitalizadoGeneral,
							    String fechaHospitalizacionGeneral,
							    boolean estaVivoGeneral,
							    String fechaDefuncion,
							    String lugarNoti,
							    String nombreInvestigador,
							    String telefonoInvestigador,
							    int unidadGeneradora,
							    
							    HashMap datosLaboratorio,
							    String pais,
							    int areaProcedencia)
    {
    	return SqlBaseFichaSarampionDao.modificarFicha(con,
    													sire,
														notificar,
													    loginUsuario,
													    codigoFichaSarampion,
													    codigoPaciente,
													    codigoDiagnostico,
													    codigoNotificacion,
													    numeroSolicitud,
													    estado,
													    
													    nombrePadre,
													    ocupacionPadre,
													    direccionTrabajoPadre,
													    fechaVisita1,
													    fuenteNotificacion,
													    vacunaSarampion,
													    numeroDosisSarampion,
													    fechaUltimaDosisSarampion,
													    fuenteDatosSarampion,
													    vacunaRubeola,
													    numeroDosisRubeola,
													    fechaUltimaDosisRubeola,
													    fuenteDatosRubeola,
													    
													    fechaVisitaDomiciliaria,
													    fiebre,
													    fechaInicioFiebre,
													    tipoErupcion,
													    fechaInicioErupcion,
													    duracionErupcion,
													    tos,
													    coriza,
													    conjuntivitis,
													    adenopatia,
													    artralgia,
													    embarazada,
													    numeroSemanas,
													    lugarParto,
													    
													    huboContacto,
													    huboCasoConfirmado,
													    huboViaje,
													    lugarViaje,
													    huboContactoEmbarazada,
													    diagnosticoFinal,
													    
													    lugarProcedencia,
													    fechaConsultaGeneral,
													    fechaInicioSintomasGeneral,
													    tipoCaso,
													    hospitalizadoGeneral,
													    fechaHospitalizacionGeneral,
													    estaVivoGeneral,
													    fechaDefuncion,
													    lugarNoti,
													    nombreInvestigador,
													    telefonoInvestigador,
													    unidadGeneradora,
													    
													    datosLaboratorio,
													    secuenciaNotificacionesStr,
													    pais,
													    areaProcedencia);
    }
    
    
    /**
     * Metodo que consulta todos los datos de una ficha de Rabia
     */
    public ResultSet consultaTodoFicha(Connection con, int codigo) {
        
        return SqlBaseFichaSarampionDao.consultarTodoFichaSarampion(con,codigo);
    }
    
    
    /**
     * Metodo que consulta los datos de laboratorio de una ficha de sarampion
     */
    public ResultSet consultarDatosLaboratorio(Connection con, int codigo) {
    	
    	return SqlBaseFichaSarampionDao.consultarDatosLaboratorio(con,codigo);
    }
    
    
    
    public ResultSet consultaDatosPaciente(Connection con, int codigo, boolean empezarnuevo)
    {
    	return SqlBaseFichaSarampionDao.consultarDatosPaciente(con,codigo,empezarnuevo);
    }
    
    
    
    public int insertarFichaCompleta(Connection con,
							    		int numeroSolicitud,
										String login,
										int codigoPaciente,
										String codigoDiagnostico,
										int estado,
										int codigoAseguradora,
										String nombreProfesional,
										HashMap datosLaboratorio,
									    
									    String sire,
										boolean notificar,
									    String loginUsuario,
									    
									    String nombrePadre,
									    String ocupacionPadre,
									    String direccionTrabajoPadre,
									    String fechaVisita1,
									    int fuenteNotificacion,
									    int vacunaSarampion,
									    int numeroDosisSarampion,
									    String fechaUltimaDosisSarampion,
									    int fuenteDatosSarampion,
									    int vacunaRubeola,
									    int numeroDosisRubeola,
									    String fechaUltimaDosisRubeola,
									    int fuenteDatosRubeola,
									    
									    String fechaVisitaDomiciliaria,
									    int fiebre,
									    String fechaInicioFiebre,
									    int tipoErupcion,
									    String fechaInicioErupcion,
									    int duracionErupcion,
									    int tos,
									    int coriza,
									    int conjuntivitis,
									    int adenopatia,
									    int artralgia,
									    int embarazada,
									    int numeroSemanas,
									    String lugarParto,
									    
									    int huboContacto,
									    int huboCasoConfirmado,
									    int huboViaje,
									    String lugarViaje,
									    int huboContactoEmbarazada,
									    
									    String lugarProcedencia,
									    String fechaConsultaGeneral,
									    String fechaInicioSintomasGeneral,
									    int tipoCaso,
									    boolean hospitalizadoGeneral,
									    String fechaHospitalizacionGeneral,
									    boolean estaVivoGeneral,
									    String fechaDefuncion,
									    String lugarNoti,
									    String nombreInvestigador,
									    String telefonoInvestigador,
									    int unidadGeneradora,
									    boolean activa,
									    int diagnosticoFinal,
									    String pais,
									    int areaProcedencia
									    )
	{
    	return SqlBaseFichaSarampionDao.insertarFichaCompleta(con,
													    		numeroSolicitud,
																login,
																codigoPaciente,
																codigoDiagnostico,
																estado,
																codigoAseguradora,
																nombreProfesional,
																datosLaboratorio,
															    secuenciaStr,
															    
															    sire,
																notificar,
															    loginUsuario,
															    
															    nombrePadre,
															    ocupacionPadre,
															    direccionTrabajoPadre,
															    fechaVisita1,
															    fuenteNotificacion,
															    vacunaSarampion,
															    numeroDosisSarampion,
															    fechaUltimaDosisSarampion,
															    fuenteDatosSarampion,
															    vacunaRubeola,
															    numeroDosisRubeola,
															    fechaUltimaDosisRubeola,
															    fuenteDatosRubeola,
															    
															    fechaVisitaDomiciliaria,
															    fiebre,
															    fechaInicioFiebre,
															    tipoErupcion,
															    fechaInicioErupcion,
															    duracionErupcion,
															    tos,
															    coriza,
															    conjuntivitis,
															    adenopatia,
															    artralgia,
															    embarazada,
															    numeroSemanas,
															    lugarParto,
															    
															    huboContacto,
															    huboCasoConfirmado,
															    huboViaje,
															    lugarViaje,
															    huboContactoEmbarazada,
															    
															    lugarProcedencia,
															    fechaConsultaGeneral,
															    fechaInicioSintomasGeneral,
															    tipoCaso,
															    hospitalizadoGeneral,
															    fechaHospitalizacionGeneral,
															    estaVivoGeneral,
															    fechaDefuncion,
															    lugarNoti,
															    nombreInvestigador,
															    telefonoInvestigador,
															    unidadGeneradora,
															    activa,
															    diagnosticoFinal,
															    pais,
															    areaProcedencia);
	}
    
    
    
    
    public int terminarFicha(Connection con, int codigoFichaSarampion)
    {
    	return SqlBaseFichaSarampionDao.terminarFicha(con,codigoFichaSarampion);
    }
}
