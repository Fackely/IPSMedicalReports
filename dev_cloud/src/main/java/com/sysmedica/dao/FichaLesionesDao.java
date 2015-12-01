package com.sysmedica.dao;

import java.sql.Connection;
import java.sql.ResultSet;

public interface FichaLesionesDao {

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
							    );
	
	
	public int modificarFicha(Connection con,
										String sire,
										boolean notificar,
									    String loginUsuario,
									    int codigoFichaInfecciones,
									    int codigoPaciente,
									    String codigoDiagnostico,
									    int codigoNotificacion,
									    int numeroSolicitud,
									    int estado,
									    
									    int estadoIngreso,
									    String fechaOcurrencia,
									    int horaOcurrencia,
									    String lugarOcurrencia,
									    int localidad,
									    String municipioOcurrencia,
									    String departamentoOcurrencia,
									    int armaFuego,
									    int armaCortopunzante,
									    int armaContundente,
									    int asfixia,
									    int intoxicacion,
									    int inmersion,
									    int explosivo,
									    int polvora,
									    int otracausa,
									    int caida,
									    int mordedura,
									    int vivienda,
									    int lugarTrabajo,
									    int lugarEstudio,
									    int taberna,
									    int establecimientoPublico,
									    int viaPublica,
									    int otroLugar,
									    int lesionAccidenteTrabajo,
									    int codigoArp,
									    int actividadDuranteHecho,
									    int lesionIntencional,
									    int tipoLesion,
									    int tipoVehiculo,
									    int condicionLesionado,
									    int tipoViolencia,
									    int agresor,
									    int denunciado,
									    int consumoAlcohol,
									    int consumoOtrasSustancias,
									    String impresionDiagnostica,
									    int gravedadLesion,
									    int unidadGeneradora,
									    int fueraBogota
									    );
	
	
	public ResultSet consultaTodoFicha(Connection con, int codigo);
	
	
	
	public ResultSet consultaDatosPaciente(Connection con, int codigo);
	
	
	public int insertarFichaCompleta(Connection con,
											int numeroSolicitud,
											String login,
											int codigoPaciente,
											String codigoDiagnostico,
											int estado,
											int codigoAseguradora,
											String nombreProfesional,
										    
										    int estadoIngreso,
										    String fechaOcurrencia,
										    int horaOcurrencia,
										    String lugarOcurrencia,
										    int localidad,
										    String municipioOcurrencia,
										    String departamentoOcurrencia,
										    int armaFuego,
										    int armaCortopunzante,
										    int armaContundente,
										    int asfixia,
										    int intoxicacion,
										    int inmersion,
										    int explosivo,
										    int polvora,
										    int otracausa,
										    int caida,
										    int mordedura,
										    int vivienda,
										    int lugarTrabajo,
										    int lugarEstudio,
										    int taberna,
										    int establecimientoPublico,
										    int viaPublica,
										    int otroLugar,
										    int lesionAccidenteTrabajo,
										    int codigoArp,
										    int actividadDuranteHecho,
										    int lesionIntencional,
										    int tipoLesion,
										    int tipoVehiculo,
										    int condicionLesionado,
										    int tipoViolencia,
										    int agresor,
										    int denunciado,
										    int consumoAlcohol,
										    int consumoOtrasSustancias,
										    String impresionDiagnostica,
										    int gravedadLesion,
										    int unidadGeneradora,
										    int fueraBogota,
										    boolean activa
										   );
}
