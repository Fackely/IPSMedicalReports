package com.sysmedica.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

public interface FichaSolicitudLaboratoriosDao {

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
									String valor
									);
	
	
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
								);
	
	
	public ResultSet consultarFicha(Connection con, int codigo);
	
	
	public ResultSet consultarSolicitud(Connection con, int codigo);
	
	
	public ResultSet consultarServicios(Connection con, int codigoEnfNotificable);
	
	
	public int insertarServicioFicha(Connection con, 
										HashMap mapaServicios,
										int codigoFicha,
										int codigoFichaLaboratorios,
										String examenSolicitado,
										String muestraEnviada,
										String hallazgos);
}
