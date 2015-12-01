package com.sysmedica.dao.postgresql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

import com.sysmedica.dao.FichaSolicitudLaboratoriosDao;
import com.sysmedica.dao.sqlbase.SqlBaseFichaSolicitudLaboratoriosDao;

public class PostgresqlFichaSolicitudLaboratoriosDao implements FichaSolicitudLaboratoriosDao {

//	private String secuenciaStr = "SELECT nextval('seq_fichas')";
	private String secuenciaStr = "SELECT nextval('epidemiologia.seq_labs_epi')";
	
	public int insertarFicha(Connection con,
								int codigoFicha,
								int codigoFichaLaboratorios,
								String sire,
							    
								String examenSolicitado,
								String muestraEnviada,
								String hallazgos,
								String fechaToma,
								String fechaRecepcion,
								int muestra,
								int prueba,
								int agente,
								int resultado,
								String fechaResultado,
								String valor)
	{
		return SqlBaseFichaSolicitudLaboratoriosDao.insertarFicha(con,
																	codigoFicha,
																	codigoFichaLaboratorios,
																	sire,
																    
																	examenSolicitado,
																	muestraEnviada,
																	hallazgos,
																	fechaToma,
																	fechaRecepcion,
																	muestra,
																	prueba,
																	agente,
																	resultado,
																	fechaResultado,
																	valor,
																	secuenciaStr);
	}
	
	
	
	public int modificarFicha(Connection con,
								String examenSolicitado,
								String muestraEnviada,
								String hallazgos,
								String fechaToma,
								String fechaRecepcion,
								int muestra,
								int prueba,
								int agente,
								int resultado,
								String fechaResultado,
								String valor,
								int codigoFichaLaboratorios
							)
	{
		return SqlBaseFichaSolicitudLaboratoriosDao.modificarFicha(con,
																	examenSolicitado,
																	muestraEnviada,
																	hallazgos,
																	fechaToma,
																	fechaRecepcion,
																	muestra,
																	prueba,
																	agente,
																	resultado,
																	fechaResultado,
																	valor,
																	codigoFichaLaboratorios);
	}
	
	
	
	public ResultSet consultarFicha(Connection con, int codigo)
	{
		return SqlBaseFichaSolicitudLaboratoriosDao.consultarFicha(con,codigo);
	}
	
	
	
	public ResultSet consultarSolicitud(Connection con, int codigo)
	{
		return SqlBaseFichaSolicitudLaboratoriosDao.consultarSolicitud(con,codigo);
	}
	
	
	public ResultSet consultarServicios(Connection con, int codigoEnfNotificable)
	{
		return SqlBaseFichaSolicitudLaboratoriosDao.consultarServicios(con,codigoEnfNotificable);
	}
	
	
	public int insertarServicioFicha(Connection con, 
										HashMap mapaServicios,
										int codigoFicha,
										int codigoFichaLaboratorios,
										String examenSolicitado,
										String muestraEnviada,
										String hallazgos)
	{
		return SqlBaseFichaSolicitudLaboratoriosDao.insertarServicioFicha(con, 
																			mapaServicios,
																			codigoFicha,
																			codigoFichaLaboratorios,
																			examenSolicitado,
																			muestraEnviada,
																			hallazgos,
																			secuenciaStr);
	}
}
