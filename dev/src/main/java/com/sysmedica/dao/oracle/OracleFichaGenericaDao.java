package com.sysmedica.dao.oracle;

import com.sysmedica.dao.FichaGenericaDao;
import java.sql.Connection;
import java.sql.ResultSet;

import com.sysmedica.dao.FichaGenericaDao;
import com.sysmedica.dao.sqlbase.SqlBaseFichaGenericaDao;

public class OracleFichaGenericaDao implements FichaGenericaDao {

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
		return SqlBaseFichaGenericaDao.insertarFicha(con,
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
							    int codigoFichaGenerica,
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
								)
	{
		return SqlBaseFichaGenericaDao.modificarFicha(con,
														sire,
														loginUsuario,
													    codigoFichaGenerica,
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
													    pais,
													    areaProcedencia
														);
	}


	public ResultSet consultaTodoFicha(Connection con, int codigo)
	{
		return SqlBaseFichaGenericaDao.consultarTodoFichaGenerica(con,codigo);
	}
	
	
	
	public ResultSet consultaDatosPaciente(Connection con, int codigo,boolean empezarnuevo)
    {
    	return SqlBaseFichaGenericaDao.consultarDatosPaciente(con,codigo,empezarnuevo);
    }
	
	
	
	public ResultSet consultaDatosAdicionalesPaciente(Connection con, int codigo)
	{
		return SqlBaseFichaGenericaDao.consultarDatosAdicionalesPaciente(con,codigo);
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
									   )
	{
		return SqlBaseFichaGenericaDao.insertarFichaCompleta(con,
																numeroSolicitud,
																login,
																codigoPaciente,
																codigoDiagnostico,
																estado,
																codigoAseguradora,
																nombreProfesional,
															    secuenciaStr,
															    
															    sire,
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
															    activa,
															    pais,
															    areaProcedencia);
	}
}
