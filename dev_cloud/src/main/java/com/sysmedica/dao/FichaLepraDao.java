package com.sysmedica.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

public interface FichaLepraDao {

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
									    
									    int criterioClinico,
										String indiceBacilar,
										int clasificacion,
										int resultadosBiopsia,
										int ojoDerecho,
										int ojoIzquierdo,
										int manoDerecha,
										int manoIzquierda,
										int pieDerecho,
										int pieIzquierdo,
										int tipoCasoLepra,
										int tieneCicatriz,
										int fuenteContagio,
										int metodoCaptacion,
										String fechaInvestigacion,
										int tieneConvivientes,
										String totalConvivientes,
										String totalExaminados,
										String sanosConCicatriz,
										String sanosSinCicatriz,
										String sintomaticosConCicatriz,
										String sintomaticosSinCicatriz,
										String vacunadosBcg,
										String motivoNoAplicacion,
										String investigadoPor,
										String telefonoInvestigador,
										String observaciones,
									    
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

								    int criterioClinico,
									String indiceBacilar,
									int clasificacion,
									int resultadosBiopsia,
									int ojoDerecho,
									int ojoIzquierdo,
									int manoDerecha,
									int manoIzquierda,
									int pieDerecho,
									int pieIzquierdo,
									int tipoCasoLepra,
									int tieneCicatriz,
									int fuenteContagio,
									int metodoCaptacion,
									String fechaInvestigacion,
									int tieneConvivientes,
									String totalConvivientes,
									String totalExaminados,
									String sanosConCicatriz,
									String sanosSinCicatriz,
									String sintomaticosConCicatriz,
									String sintomaticosSinCicatriz,
									String vacunadosBcg,
									String motivoNoAplicacion,
									String investigadoPor,
									String telefonoInvestigador,
									String observaciones,
									String pais,
								    int areaProcedencia
								    );
	
	
	
	public ResultSet consultaTodoFicha(Connection con, int codigo);
	
	
	
	public ResultSet consultarDatosPaciente(Connection con, int codigo, boolean empezarnuevo);
}
