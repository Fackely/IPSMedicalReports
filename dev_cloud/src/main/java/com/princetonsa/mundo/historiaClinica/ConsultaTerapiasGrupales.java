package com.princetonsa.mundo.historiaClinica;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.ConsultaTerapiasGrupalesDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseConsultaTerapiasGrupalesDao;


public class ConsultaTerapiasGrupales
{
	
	
	/**
	 * Instancia el Dao
	 * */
	public static ConsultaTerapiasGrupalesDao getConsultTerapiasGrupalesDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaTerapiasGrupalesDao();
	}
	
	/**
	 * Consulta las terapias Grupales a partir del codigo paciente
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static HashMap consultarTGrupalPaciente(Connection con, HashMap parametros)
	{
		return getConsultTerapiasGrupalesDao().consultarTGrupalPaciente(con, parametros);
	}
	
	
	/**
	 * Consulta de la terapias Grupales
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static  HashMap consultarTerapiasGrupales(Connection con, HashMap parametros)
	{
		return getConsultTerapiasGrupalesDao().consultarTerapiasGrupales(con, parametros);
	}
	
	/**
	 * Consulta los pacientes asociados a una terapia Grupal
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static  HashMap consultarDetalleTGrupal(Connection con, HashMap parametros)
	{
		return getConsultTerapiasGrupalesDao().consultarDetalleTGrupal(con, parametros);
	}
	
	
	/**
	 * Consulta los Responsables del Registro de Terapias Grupales
	 * @param Connection con
	 * @param HashMao parametros
	 * */
	public static ArrayList<HashMap<String, Object>> consultarResponsableRegistro(Connection con, HashMap parametros)
	{
		return getConsultTerapiasGrupalesDao().consultarResponsableRegistro(con, parametros);
	}
}