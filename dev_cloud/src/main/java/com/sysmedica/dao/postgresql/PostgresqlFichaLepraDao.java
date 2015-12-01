package com.sysmedica.dao.postgresql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

import com.sysmedica.dao.FichaLepraDao;
import com.sysmedica.dao.sqlbase.SqlBaseFichaLepraDao;

public class PostgresqlFichaLepraDao implements FichaLepraDao {

	private String secuenciaStr = "SELECT nextval('epidemiologia.seq_fichas')";

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
									   )
	{
		return SqlBaseFichaLepraDao.insertarFichaCompleta(con,
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
														    
														    criterioClinico,
															indiceBacilar,
															clasificacion,
															resultadosBiopsia,
															ojoDerecho,
															ojoIzquierdo,
															manoDerecha,
															manoIzquierda,
															pieDerecho,
															pieIzquierdo,
															tipoCasoLepra,
															tieneCicatriz,
															fuenteContagio,
															metodoCaptacion,
															fechaInvestigacion,
															tieneConvivientes,
															totalConvivientes,
															totalExaminados,
															sanosConCicatriz,
															sanosSinCicatriz,
															sintomaticosConCicatriz,
															sintomaticosSinCicatriz,
															vacunadosBcg,
															motivoNoAplicacion,
															investigadoPor,
															telefonoInvestigador,
															observaciones,
														    
														    activa,
														    pais,
														    areaProcedencia);
	}
	
	

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
								    )
	{
		return SqlBaseFichaLepraDao.modificarFicha(con,
													sire,
													notificar,
												    loginUsuario,
												    codigoFichaLepra,
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
									
												    criterioClinico,
													indiceBacilar,
													clasificacion,
													resultadosBiopsia,
													ojoDerecho,
													ojoIzquierdo,
													manoDerecha,
													manoIzquierda,
													pieDerecho,
													pieIzquierdo,
													tipoCasoLepra,
													tieneCicatriz,
													fuenteContagio,
													metodoCaptacion,
													fechaInvestigacion,
													tieneConvivientes,
													totalConvivientes,
													totalExaminados,
													sanosConCicatriz,
													sanosSinCicatriz,
													sintomaticosConCicatriz,
													sintomaticosSinCicatriz,
													vacunadosBcg,
													motivoNoAplicacion,
													investigadoPor,
													telefonoInvestigador,
													observaciones,
													pais,
												    areaProcedencia);
	}
	
	
	
	public ResultSet consultaTodoFicha(Connection con, int codigo)
	{
		return SqlBaseFichaLepraDao.consultarTodoFichaLepra(con,codigo);
	}
	
	
	
	public ResultSet consultarDatosPaciente(Connection con, int codigo, boolean empezarnuevo)
	{
		return SqlBaseFichaLepraDao.consultarDatosPaciente(con,codigo,empezarnuevo);
	}
}
