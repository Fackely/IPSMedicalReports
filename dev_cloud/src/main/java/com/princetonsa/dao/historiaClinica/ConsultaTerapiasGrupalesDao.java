package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseConsultaTerapiasGrupalesDao;

public interface ConsultaTerapiasGrupalesDao
{
	/**
	 * Consulta las terapias Grupales a partir del codigo paciente
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap consultarTGrupalPaciente(Connection con, HashMap parametros);
	
		
	/**
	 * Consulta de la terapias Grupales
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap consultarTerapiasGrupales(Connection con, HashMap parametros);
	
	/**
	 * Consulta los pacientes asociados a una terapia Grupal
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap consultarDetalleTGrupal(Connection con, HashMap parametros);
	
	
	/**
	 * Consulta los Responsables del Registro de Terapias Grupales
	 * @param Connection con
	 * @param HashMao parametros
	 * */
	public ArrayList<HashMap<String, Object>> consultarResponsableRegistro(Connection con, HashMap parametros);	
}