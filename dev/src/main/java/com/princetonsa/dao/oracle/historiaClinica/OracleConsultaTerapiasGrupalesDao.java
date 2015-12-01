package com.princetonsa.dao.oracle.historiaClinica;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseConsultaTerapiasGrupalesDao;
import com.princetonsa.dao.historiaClinica.ConsultaTerapiasGrupalesDao;

public class OracleConsultaTerapiasGrupalesDao implements ConsultaTerapiasGrupalesDao
{
	/**
	 * Consulta las terapias Grupales a partir del codigo paciente
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap consultarTGrupalPaciente(Connection con, HashMap parametros)
	{
		return SqlBaseConsultaTerapiasGrupalesDao.consultarTGrupalPaciente(con, parametros);
	}
	
	
	/**
	 * Consulta de la terapias Grupales
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap consultarTerapiasGrupales(Connection con, HashMap parametros)
	{
		return SqlBaseConsultaTerapiasGrupalesDao.consultarTerapiasGrupales(con, parametros);
	}
	
	/**
	 * Consulta los pacientes asociados a una terapia Grupal
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public  HashMap consultarDetalleTGrupal(Connection con, HashMap parametros)
	{
		return SqlBaseConsultaTerapiasGrupalesDao.consultarDetalleTGrupal(con, parametros);
	}
	
	
	/**
	 * Consulta los Responsables del Registro de Terapias Grupales
	 * @param Connection con
	 * @param HashMao parametros
	 * */
	public  ArrayList<HashMap<String, Object>> consultarResponsableRegistro(Connection con, HashMap parametros)
	{
		return SqlBaseConsultaTerapiasGrupalesDao.consultarResponsableRegistro(con, parametros);
	}
}