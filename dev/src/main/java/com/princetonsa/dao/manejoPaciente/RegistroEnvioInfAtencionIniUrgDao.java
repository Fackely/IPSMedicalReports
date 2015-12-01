package com.princetonsa.dao.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.InfoDatosString;

import com.princetonsa.dto.manejoPaciente.DtoInformeAtencionIniUrg;

/**
 * 
 * */
public interface RegistroEnvioInfAtencionIniUrgDao
{
	/**
	 * Listado de Informe Atencion Inicial de Urgencias
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public ArrayList<DtoInformeAtencionIniUrg> getListadoInformeInicUrge(Connection con, HashMap parametros);
	
	/**
	 * Carga la informacion de atencion inicial de urgencias
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public DtoInformeAtencionIniUrg cargarInfoPaciente(Connection con,HashMap parametros);
	
	/**
	 * Inserta informacion del informe atencion inicial de urgencias
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap insertarInformeAtencionIniUrg(Connection con,HashMap parametros);
	
	/**
	 * Inserta informacion del envio del informe de atencion inicial de urgencias
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public int insertarEnvioInformeAtencionIniUrg(Connection con,HashMap parametros);
	
	/**
	 * Verifica si el paciente posee ingresos con valoraciones de urgencias
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public InfoDatosString tienePacienteIngresoValoracionUrg(Connection con, HashMap parametros);
	
	/**
	 * Listado de Informe Atencion Inicial de Urgencias
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public ArrayList<DtoInformeAtencionIniUrg> getListadoInformeInicUrgeXPaciente(Connection con, HashMap parametros);
	
	/**
	 * Listado de Informe Atencion Inicial de Urgencias
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public ArrayList<DtoInformeAtencionIniUrg> getListadoInformeInicUrgeXPacienteIngreso(Connection con, HashMap parametros);
	
	/**
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public ArrayList<DtoInformeAtencionIniUrg> getConveniosPacienteReporte(Connection con, HashMap parametros);
}