/*
 * Ago 13, 2006
 */
package com.princetonsa.dao.pyp;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dto.pyp.DtoObservacionProgramaPYP;

import util.InfoDatos;

/**
 * @author Sebastián Gómez 
 *
 * Interface utilizada para gestionar los métodos DAO de la funcionalidad
 * Programas de Promoción y Prevención
 */
public interface ProgramasPYPDao 
{
	
	/**
	 * Método implementado para verificar si un paciente tiene hoja obstétrica sin finalizar
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean tieneHojaObsSinFinalizar(Connection con,HashMap campos);
	
	/**
	 * Método que consulta los diangosticos de un paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public String consultarDiagnosticosPaciente(Connection con,HashMap campos);
	
	/**
	 * Método implementado para consultar los programas x convenio nuevos que califican para el paciente
	 *  + sus programas pyp existentes 
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarProgramasPaciente(Connection con,HashMap campos);
	
	/**
	 * Método implementado para insertar un programa PYP a un paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertarPrograma(Connection con,HashMap campos);
	
	/**
	 * Método implementado para modificar un programa PYP a un paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public int modificarPrograma(Connection con,HashMap campos);
	
	/**
	 * Método implementado para consultar un programa de un paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarPrograma(Connection con,HashMap campos);
	
	/**
	 * Método que consulta las actividades de un programa dependiendo de las
	 * características del paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarActividadesProgramaPaciente(Connection con,HashMap campos);
	
	/**
	 * Método que consulta los articulos x programa de un paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarArticulosProgramaPaciente(Connection con,HashMap campos);
	
	/**
	 * Método implementado para insertar una actividad al paciente PYP
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertarActividad(Connection con,HashMap campos);
	
	/**
	 * Método implementado para modificar la actividad pyp de un paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public int modificarActividad(Connection con,HashMap campos);
	
	/**
	 * Método implementado para consultar los centros de atención de una actividad
	 * @param con
	 * @param campos
	 * @return
	 */
	public String consultarCentrosAtencionActividad(Connection con,HashMap campos);
	
	/**
	 * Método implementado para consultar el histórico de una  actividad
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarHistoricosActividad(Connection con,HashMap campos);
	
	/**
	 * Método que inserta una actividad acumulada
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertarActividadAcumulada(Connection con,HashMap campos);
	
	/**
	 * Método que retorna el consecutivo de una actividad acumulada existente
	 * @param con
	 * @param campos
	 * @return
	 */
	public String consultarConsecutivoActividadAcumulada(Connection con,HashMap campos);
	
	/**
	 * Método que aumenta el acumulado de una actividad
	 * @param con
	 * @param campos
	 * @return
	 */
	public int aumentarAcumuladoActividad(Connection con,HashMap campos);
	
	/**
	 * Método que verifica si una actividad ya fue ejecutada para la fecha actual
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean estaActividadEjecutada(Connection con,HashMap campos);
	
	/**
	 * Método que verifica si la actividad ya fue ejecutada para la fecha,
	 * y si la actividad permite ser ejecutada varias veces al día
	 *
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean permiteEjecutarActividad(Connection con,HashMap campos);
	
	/**
	 * Método implementado para consultar la finalidad de una actividad
	 * @param con
	 * @param campos
	 * @return
	 */
	public String consultarFinalidadActividad(Connection con,HashMap campos);
	
	/**
	 * Método que consulta la finalidad de una consulta PYP
	 * basándose de lo parametrizado de actividades x programa
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public InfoDatos consultarFinalidadActividadConsulta(Connection con,String numeroSolicitud);
	
	/**
	 * Método que consulta el consecutivo de un programa ya existente
	 * @param con
	 * @param codigoPaciente
	 * @param codigoPrograma
	 * @param institucion
	 * @return
	 */
	public String consultarConsecutivoProgramaExistente(Connection con,String codigoPaciente,String codigoPrograma,String institucion);

	/**
	 * 
	 * @param con
	 * @param codigoPaciente
	 * @param codigoPrograma
	 * @param institucion
	 * @return
	 */
	public ArrayList<DtoObservacionProgramaPYP> obtenerObservacionesProgramaPYP(Connection con, HashMap filtros);

	/**
	 * 
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean guardarObservacioProgramaPYP(Connection con, DtoObservacionProgramaPYP dto);
	
}
