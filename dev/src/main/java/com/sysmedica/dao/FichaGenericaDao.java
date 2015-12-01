package com.sysmedica.dao;

import java.sql.Connection;
import java.sql.ResultSet;

public interface FichaGenericaDao {

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
							    );
	
	public int modificarFicha(Connection con,
								String sire,
								String loginUsuario,
							    int codigoFichaParalisis,
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
							    String pais,
							    int areaProcedencia
								);


	public ResultSet consultaTodoFicha(Connection con, int codigo);
	
	
	public ResultSet consultaDatosPaciente(Connection con, int codigo,boolean empezarnuevo);
	
	
	public ResultSet consultaDatosAdicionalesPaciente(Connection con, int codigo);
	
	
	public int insertarFichaCompleta(Connection con,
									int numeroSolicitud,
									String login,
									int codigoPaciente,
									String codigoDiagnostico,
									int estado,
									int codigoAseguradora,
									String nombreProfesional,
								    
								    String sire,
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
								    boolean activa,
								    String pais,
								    int areaProcedencia
								   );
}
