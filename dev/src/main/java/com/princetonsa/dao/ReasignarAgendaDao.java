/*
 * Created on 09/05/2006
 * 
 * @author <a href="mailto:artotor@hotmail.com">Jorge Armando Osorio Velásquez</a>
 * 
 * Lenguaje		:Java
 *
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * @author artotor
 *
 */
public interface ReasignarAgendaDao 
{

	/**
	 * Metodo que busca las agendas generadas.
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract HashMap buscarAgendas(Connection con, HashMap vo);

	/**
	 * Metodo para consultar las citas de una agenda
	 * @param con
	 * @param codigoAgenda
	 * @return
	 */
	public abstract HashMap consultarCitasAgenda(Connection con, int codigoAgenda);

	/**
	 * Metodo que reasigna una agenda a otro profesional.
	 * @param con
	 * @param codigoAgenda
	 * @param nuevoProfesional
	 * @return
	 */
	public abstract boolean reasignarProfesiona(Connection con, int codigoAgenda, int nuevoProfesional);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean insertarLogReasignacionAgenda(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract HashMap ejecutarBusquedalogs(Connection con, HashMap vo);
	
	/**
	 * Método para verificar si un profesional pertenece a una unidad de agenda
	 * @param con
	 * @param campos
	 * @return
	 */
	public abstract boolean perteneceProfesionalAUnidadAgenda(Connection con,HashMap campos);
	
	/**
	 * Método para obtener los profesionales que aplican para una unidad de agenda específica
	 * @param con
	 * @param campos
	 * @return
	 */
	public abstract ArrayList<HashMap<String, Object>> obtenerProfesionalesUnidadAgenda(Connection con,HashMap campos);

}
