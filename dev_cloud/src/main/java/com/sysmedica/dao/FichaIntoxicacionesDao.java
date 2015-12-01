package com.sysmedica.dao;

import java.sql.Connection;
import java.sql.ResultSet;

public interface FichaIntoxicacionesDao {

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
											    
											    int tipoIntoxicacion,
											    String nombreProducto,
											    int tipoExposicion,
											    int produccion,
											    int almacenamiento,
											    int agricola,
											    int saludPublica,
											    int domiciliaria,
											    int tratHumano,
											    int tratVeterinario,
											    int transporte,
											    int mezcla,
											    int mantenimiento,
											    int cultivo,
											    int otros,
											    String otraActividad,
											    String fechaExposicion,
											    int horaExposicion,
											    int viaExposicion,
											    String otraViaExposicion,
											    int escolaridad,
											    int embarazada,
											    int vinculoLaboral,
											    int afiliadoArp,
											    String nombreArp,
											    int codgoArp,
											    int estCivil,
											    int alerta,
											    int investigacion,
											    String fechaInvestigacion,
											    String fechaInforma,
											    String nombreResponsable,
											    String telefonoResponsable,
											    String observaciones,
											    
											    boolean activa,
											    String pais,
											    int areaProcedencia
											   );
	
	
	public int modificarFicha(Connection con,
									String sire,
									boolean notificar,
								    String loginUsuario,
								    int codigoFichaIntoxicacion,
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
								    
								    int tipoIntoxicacion,
								    String nombreProducto,
								    int tipoExposicion,
								    int produccion,
								    int almacenamiento,
								    int agricola,
								    int saludPublica,
								    int domiciliaria,
								    int tratHumano,
								    int tratVeterinario,
								    int transporte,
								    int mezcla,
								    int mantenimiento,
								    int cultivo,
								    int otros,
								    String otraActividad,
								    String fechaExposicion,
								    int horaExposicion,
								    int viaExposicion,
								    String otraViaExposicion,
								    int escolaridad,
								    int embarazada,
								    int vinculoLaboral,
								    int afiliadoArp,
								    String nombreArp,
								    int codgoArp,
								    int estCivil,
								    int alerta,
								    int investigacion,
								    String fechaInvestigacion,
								    String fechaInforma,
								    String nombreResponsable,
								    String telefonoResponsable,
								    String observaciones,
								    String pais,
								    int areaProcedencia
								    );
	
	
	public ResultSet consultaTodoFicha(Connection con, int codigo);
	
	
	
	public ResultSet consultarDatosPaciente(Connection con, int codigo, boolean empezarnuevo);
}
