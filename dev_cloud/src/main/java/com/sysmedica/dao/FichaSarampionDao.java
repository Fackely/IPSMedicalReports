package com.sysmedica.dao;

import java.util.HashMap;
import java.sql.Connection;
import java.sql.ResultSet;

public interface FichaSarampionDao {

	
	/**
	 * Metodo para insertar una ficha de Sarampion
	 * @param con
	 * @param notificar
	 * @param loginUsuario
	 * @param codigoFichaSarampion
	 * @param codigoPaciente
	 * @param codigoDiagnostico
	 * @param codigoNotificacion
	 * @param numeroSolicitud
	 * @param estado
	 * @param nombrePadre
	 * @param ocupacionPadre
	 * @param direccionTrabajoPadre
	 * @param fechaVisita1
	 * @param fuenteNotificacion
	 * @param vacunaSarampion
	 * @param numeroDosisSarampion
	 * @param fechaUltimaDosisSarampion
	 * @param fuenteDatosSarampion
	 * @param vacunaRubeola
	 * @param numeroDosisRubeola
	 * @param fechaUltimaDosisRubeola
	 * @param fuenteDatosRubeola
	 * @param fechaVisitaDomiciliaria
	 * @param fiebre
	 * @param fechaInicioFiebre
	 * @param tipoErupcion
	 * @param fechaInicioErupcion
	 * @param duracionErupcion
	 * @param tos
	 * @param coriza
	 * @param conjuntivitis
	 * @param adenopatia
	 * @param artralgia
	 * @param embarazada
	 * @param numeroSemanas
	 * @param lugarParto
	 * @param huboContacto
	 * @param huboCasoConfirmado
	 * @param huboViaje
	 * @param lugarViaje
	 * @param huboContactoEmbarazada
	 * @param diagnosticoFinal
	 * @param datosLaboratorio
	 * @return
	 */
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
							    
							    HashMap datosLaboratorio);
	
	
	/**
	 * Metodo para modificar una ficha de sarampion
	 * @param con
	 * @param sire
	 * @param notificar
	 * @param loginUsuario
	 * @param codigoFichaSarampion
	 * @param codigoPaciente
	 * @param codigoDiagnostico
	 * @param codigoNotificacion
	 * @param numeroSolicitud
	 * @param estado
	 * @param nombrePadre
	 * @param ocupacionPadre
	 * @param direccionTrabajoPadre
	 * @param fechaVisita1
	 * @param fuenteNotificacion
	 * @param vacunaSarampion
	 * @param numeroDosisSarampion
	 * @param fechaUltimaDosisSarampion
	 * @param fuenteDatosSarampion
	 * @param vacunaRubeola
	 * @param numeroDosisRubeola
	 * @param fechaUltimaDosisRubeola
	 * @param fuenteDatosRubeola
	 * @param fechaVisitaDomiciliaria
	 * @param fiebre
	 * @param fechaInicioFiebre
	 * @param tipoErupcion
	 * @param fechaInicioErupcion
	 * @param duracionErupcion
	 * @param tos
	 * @param coriza
	 * @param conjuntivitis
	 * @param adenopatia
	 * @param artralgia
	 * @param embarazada
	 * @param numeroSemanas
	 * @param lugarParto
	 * @param huboContacto
	 * @param huboCasoConfirmado
	 * @param huboViaje
	 * @param lugarViaje
	 * @param huboContactoEmbarazada
	 * @param diagnosticoFinal
	 * @param datosLaboratorio
	 * @return
	 */
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
							    int areaProcedencia);
	
	/**
	 * Metodo para consultar una ficha de Sarampion
	 * @param con
	 * @param codigo
	 * @return
	 */
	public ResultSet consultaTodoFicha(Connection con, int codigo);
	
	
	public ResultSet consultarDatosLaboratorio(Connection con, int codigo);
	
	
	
	public ResultSet consultaDatosPaciente(Connection con, int codigo, boolean empezarnuevo);
	
	
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
									    );
	
	
	
	public int terminarFicha(Connection con, int codigoFichaSarampion);
}
