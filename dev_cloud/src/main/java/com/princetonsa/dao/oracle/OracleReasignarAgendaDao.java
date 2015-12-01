/*
 * Created on 09/05/2006
 * 
 * @author <a href="mailto:artotor@hotmail.com">Jorge Armando Osorio Velásquez</a>
 * 
 * Lenguaje		:Java
 *
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.ReasignarAgendaDao;
import com.princetonsa.dao.sqlbase.SqlBaseReasignarAgendaDao;

/**
 * 
 * @author artotor
 *
 */
public class OracleReasignarAgendaDao implements ReasignarAgendaDao 
{
	
	/**
	 * Metodo que busca las agendas generadas.
	 * @param con
	 * @param vo
	 * @return
	 */
	public HashMap buscarAgendas(Connection con, HashMap vo)
	{
		return SqlBaseReasignarAgendaDao.buscarAgendas(con,vo);
	}
	
	/**
	 * Metodo para consultar las citas de una agenda
	 * @param con
	 * @param codigoAgenda
	 * @return
	 */
	public HashMap consultarCitasAgenda(Connection con, int codigoAgenda)
	{
		return SqlBaseReasignarAgendaDao.consultarCitasAgenda(con,codigoAgenda);
	}

	/**
	 * Metodo que reasigna una agenda a otro profesional.
	 * @param con
	 * @param codigoAgenda
	 * @param nuevoProfesional
	 * @return
	 */
	public boolean reasignarProfesiona(Connection con, int codigoAgenda, int nuevoProfesional)
	{
		return SqlBaseReasignarAgendaDao.reasignarProfesiona(con,codigoAgenda,nuevoProfesional);
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean insertarLogReasignacionAgenda(Connection con, HashMap vo)
	{
		String insertarLogReasignacionAgenda="INSERT INTO log_reasignacion_citas (codigo,fecha_proceso,hora_proceso,usuario,cod_agenda_modificada,codigo_medico_anterior) VALUES (seq_log_reasignacion_citas.nextval,?,?,?,?,?)";
		return SqlBaseReasignarAgendaDao.insertarLogReasignacionAgenda(con,vo,insertarLogReasignacionAgenda);
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public HashMap ejecutarBusquedalogs(Connection con, HashMap vo)
	{
		return SqlBaseReasignarAgendaDao.ejecutarBusquedalogs(con,vo);
	}
	
	/**
	 * Método para verificar si un profesional pertenece a una unidad de agenda
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean perteneceProfesionalAUnidadAgenda(Connection con,HashMap campos)
	{
		return SqlBaseReasignarAgendaDao.perteneceProfesionalAUnidadAgenda(con, campos);
	}
	
	/**
	 * Método para obtener los profesionales que aplican para una unidad de agenda específica
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerProfesionalesUnidadAgenda(Connection con,HashMap campos)
	{
		return SqlBaseReasignarAgendaDao.obtenerProfesionalesUnidadAgenda(con, campos);
	}
}
