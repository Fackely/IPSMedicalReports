package com.princetonsa.dao.oracle.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.InfoDatosString;

import com.princetonsa.dao.manejoPaciente.RegistroEnvioInfAtencionIniUrgDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseRegistroEnvioInfAtencionIniUrgDao;
import com.princetonsa.dto.manejoPaciente.DtoInformeAtencionIniUrg;


public class OracleRegistroEnvioInfAtencionIniUrgDao implements RegistroEnvioInfAtencionIniUrgDao
{
	/**
	 * Listado de Informe Atencion Inicial de Urgencias
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public ArrayList<DtoInformeAtencionIniUrg> getListadoInformeInicUrge(Connection con, HashMap parametros)
	{
		return SqlBaseRegistroEnvioInfAtencionIniUrgDao.getListadoInformeInicUrge(con, parametros);
	}
	
	/**
	 * Carga la informacion de atencion inicial de urgencias
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public DtoInformeAtencionIniUrg cargarInfoPaciente(Connection con,HashMap parametros)
	{
		return SqlBaseRegistroEnvioInfAtencionIniUrgDao.cargarInfoPaciente(con, parametros);
	}
	
	/**
	 * Inserta informacion del informe atencion inicial de urgencias
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap insertarInformeAtencionIniUrg(Connection con,HashMap parametros)
	{
		return SqlBaseRegistroEnvioInfAtencionIniUrgDao.insertarInformeAtencionIniUrg(con, parametros);
	}
	
	/**
	 * Inserta informacion del envio del informe de atencion inicial de urgencias
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public int insertarEnvioInformeAtencionIniUrg(Connection con,HashMap parametros)
	{
		return SqlBaseRegistroEnvioInfAtencionIniUrgDao.insertarEnvioInformeAtencionIniUrg(con, parametros);
	}
	
	/**
	 * Verifica si el paciente posee ingresos con valoraciones de urgencias
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public InfoDatosString tienePacienteIngresoValoracionUrg(Connection con, HashMap parametros)
	{
		return SqlBaseRegistroEnvioInfAtencionIniUrgDao.tienePacienteIngresoValoracionUrg(con, parametros);
	}
	
	/**
	 * Listado de Informe Atencion Inicial de Urgencias
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public ArrayList<DtoInformeAtencionIniUrg> getListadoInformeInicUrgeXPaciente(Connection con, HashMap parametros)
	{
		return SqlBaseRegistroEnvioInfAtencionIniUrgDao.getListadoInformeInicUrgeXPaciente(con, parametros);
	}
	
	/**
	 * Listado de Informe Atencion Inicial de Urgencias
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public ArrayList<DtoInformeAtencionIniUrg> getListadoInformeInicUrgeXPacienteIngreso(Connection con, HashMap parametros)
	{
		return SqlBaseRegistroEnvioInfAtencionIniUrgDao.getListadoInformeInicUrgeXPacienteIngreso(con, parametros);
	}
	
	/**
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public ArrayList<DtoInformeAtencionIniUrg> getConveniosPacienteReporte(Connection con, HashMap parametros)
	{
		return SqlBaseRegistroEnvioInfAtencionIniUrgDao.getConveniosPacienteReporte(con, parametros);
	}
}