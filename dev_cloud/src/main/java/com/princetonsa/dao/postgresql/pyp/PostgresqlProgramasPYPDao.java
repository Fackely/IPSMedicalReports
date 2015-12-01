/*
 * Ago 13, 2006
 */
package com.princetonsa.dao.postgresql.pyp;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.InfoDatos;

import com.princetonsa.dao.pyp.ProgramasPYPDao;
import com.princetonsa.dao.sqlbase.pyp.SqlBaseProgramasPYPDao;
import com.princetonsa.dto.pyp.DtoObservacionProgramaPYP;

/**
 * @author Sebastián Gómez 
 *
 * Clase que maneja los métodos propìos de Postgres para el acceso a la fuente
 * de datos en la funcionalidad Programas de Promoción y Prevención
 */
public class PostgresqlProgramasPYPDao implements ProgramasPYPDao 
{
	/**
	 * Cadena que consulta los programas pyp existentes del paciente
	 */
	private static final String consultarProgramasExistentesPacienteStr = " SELECT "+ 
		"ppp.codigo AS consecutivo, "+
		"psp.codigo AS codigo_programa, "+
		"psp.institucion AS institucion, "+
		"psp.descripcion AS nombre_programa, "+
		"tpp.descripcion AS tipo_programa, "+
		"CASE WHEN psp.archivo IS NULL THEN '' ELSE psp.archivo END AS archivo, "+
		"CASE WHEN psp.formato IS NULL THEN '' ELSE psp.formato || '' END AS formato, "+
		"to_char(ppp.fecha_inicial,'DD/MM/YYYY') AS fecha_inicial, "+
		"CASE WHEN fecha_final IS NULL THEN '' ELSE to_char(ppp.fecha_final,'DD/MM/YYYY') END As fecha_final, "+
		"'true' AS existe, " +
		"CASE WHEN ppp.fecha_final IS NULL THEN 'false' ELSE 'true' END AS finalizado, " +
		"CASE WHEN ppp.motivo_finalizacion IS NULL THEN " +
			"'' " +
		"ELSE " +
			"ppp.motivo_finalizacion || ' (usuario: ' || ppp.usuario_finaliza || ', fecha: ' || to_char(ppp.fecha_final,'DD/MM/YYYY') ||', hora: ' || to_char(ppp.hora_final,'HH24:MI') || ')' " +
		"END AS motivo_finalizacion "+ 
		"FROM programas_pyp_paciente ppp "+ 
		"INNER JOIN programas_salud_pyp psp ON(psp.codigo=ppp.programa AND psp.institucion=ppp.institucion) "+ 
		"INNER JOIN tipos_programa_pyp tpp ON(tpp.codigo=psp.tipo_programa AND tpp.institucion=psp.institucion) ";
	
	
	/**
	 * Método implementado para verificar si un paciente tiene hoja obstétrica sin finalizar
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean tieneHojaObsSinFinalizar(Connection con,HashMap campos)
	{
		return SqlBaseProgramasPYPDao.tieneHojaObsSinFinalizar(con,campos);
	}
	
	/**
	 * Método que consulta los diangosticos de un paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public String consultarDiagnosticosPaciente(Connection con,HashMap campos)
	{
		return SqlBaseProgramasPYPDao.consultarDiagnosticosPaciente(con,campos);
	}
	
	/**
	 * Método implementado para consultar los programas x convenio nuevos que califican para el paciente
	 *  + sus programas pyp existentes 
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarProgramasPaciente(Connection con,HashMap campos)
	{
		campos.put("consultarProgramasExistentes",consultarProgramasExistentesPacienteStr);
		return SqlBaseProgramasPYPDao.consultarProgramasPaciente(con,campos);
	}
	
	/**
	 * Método implementado para insertar un programa PYP a un paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertarPrograma(Connection con,HashMap campos)
	{
		campos.put("secuencia","nextval('seq_prog_pyp_paciente')");
		return SqlBaseProgramasPYPDao.insertarPrograma(con,campos);
	}
	
	/**
	 * Método implementado para modificar un programa PYP a un paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public int modificarPrograma(Connection con,HashMap campos)
	{
		return SqlBaseProgramasPYPDao.modificarPrograma(con,campos);
	}
	
	/**
	 * Método implementado para consultar un programa de un paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarPrograma(Connection con,HashMap campos)
	{
		campos.put("consultarProgramasExistentes",consultarProgramasExistentesPacienteStr);
		return SqlBaseProgramasPYPDao.consultarPrograma(con,campos);
	}
	
	/**
	 * Método que consulta las actividades de un programa dependiendo de las
	 * características del paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarActividadesProgramaPaciente(Connection con,HashMap campos)
	{
		return SqlBaseProgramasPYPDao.consultarActividadesProgramaPaciente(con,campos);
	}
	
	/**
	 * Método que consulta los articulos x programa de un paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarArticulosProgramaPaciente(Connection con,HashMap campos)
	{
		return SqlBaseProgramasPYPDao.consultarArticulosProgramaPaciente(con,campos);
	}
	
	/**
	 * Método implementado para insertar una actividad al paciente PYP
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertarActividad(Connection con,HashMap campos)
	{
		campos.put("secuencia","nextval('seq_act_prog_pyp_pac')");
		return SqlBaseProgramasPYPDao.insertarActividad(con,campos);
	}
	
	/**
	 * Método implementado para modificar la actividad pyp de un paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public int modificarActividad(Connection con,HashMap campos)
	{
		return SqlBaseProgramasPYPDao.modificarActividad(con,campos);
	}
	
	/**
	 * Método implementado para consultar los centros de atención de una actividad
	 * @param con
	 * @param campos
	 * @return
	 */
	public String consultarCentrosAtencionActividad(Connection con,HashMap campos)
	{
		return SqlBaseProgramasPYPDao.consultarCentrosAtencionActividad(con,campos);
	}
	
	/**
	 * Método implementado para consultar el histórico de una  actividad
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarHistoricosActividad(Connection con,HashMap campos)
	{
		return SqlBaseProgramasPYPDao.consultarHistoricosActividad(con,campos);
	}
	
	/**
	 * Método que inserta una actividad acumulada
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertarActividadAcumulada(Connection con,HashMap campos)
	{
		campos.put("secuencia","nextval('seq_acum_act_prog_pac_pyp')");
		return SqlBaseProgramasPYPDao.insertarActividadAcumulada(con,campos);
	}
	
	/**
	 * Método que retorna el consecutivo de una actividad acumulada existente
	 * @param con
	 * @param campos
	 * @return
	 */
	public String consultarConsecutivoActividadAcumulada(Connection con,HashMap campos)
	{
		return SqlBaseProgramasPYPDao.consultarConsecutivoActividadAcumulada(con,campos);
	}
	
	/**
	 * Método que aumenta el acumulado de una actividad
	 * @param con
	 * @param campos
	 * @return
	 */
	public int aumentarAcumuladoActividad(Connection con,HashMap campos)
	{
		return SqlBaseProgramasPYPDao.aumentarAcumuladoActividad(con,campos);
	}
	
	/**
	 * Método que verifica si una actividad ya fue ejecutada para la fecha actual
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean estaActividadEjecutada(Connection con,HashMap campos)
	{
		return SqlBaseProgramasPYPDao.estaActividadEjecutada(con,campos);
	}
	
	/**
	 * Método que verifica si la actividad ya fue ejecutada para la fecha,
	 * y si la actividad permite ser ejecutada varias veces al día
	 *
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean permiteEjecutarActividad(Connection con,HashMap campos)
	{
		return SqlBaseProgramasPYPDao.permiteEjecutarActividad(con,campos);
	}
	
	/**
	 * Método implementado para consultar la finalidad de una actividad
	 * @param con
	 * @param campos
	 * @return
	 */
	public String consultarFinalidadActividad(Connection con,HashMap campos)
	{
		return SqlBaseProgramasPYPDao.consultarFinalidadActividad(con,campos);
	}
	
	/**
	 * Método que consulta la finalidad de una consulta PYP
	 * basándose de lo parametrizado de actividades x programa
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public InfoDatos consultarFinalidadActividadConsulta(Connection con,String numeroSolicitud)
	{
		return SqlBaseProgramasPYPDao.consultarFinalidadActividadConsulta(con,numeroSolicitud);
	}
	
	/**
	 * Método que consulta el consecutivo de un programa ya existente
	 * @param con
	 * @param codigoPaciente
	 * @param codigoPrograma
	 * @param institucion
	 * @return
	 */
	public String consultarConsecutivoProgramaExistente(Connection con,String codigoPaciente,String codigoPrograma,String institucion)
	{
		return SqlBaseProgramasPYPDao.consultarConsecutivoProgramaExistente(con,codigoPaciente,codigoPrograma,institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPaciente
	 * @param codigoPrograma
	 * @param institucion
	 * @return
	 */
	public ArrayList<DtoObservacionProgramaPYP> obtenerObservacionesProgramaPYP(Connection con, HashMap filtros)
	{
		return SqlBaseProgramasPYPDao.obtenerObservacionesProgramaPYP(con, filtros);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPaciente
	 * @param codigoPrograma
	 * @param institucion
	 * @return
	 */
	public boolean guardarObservacioProgramaPYP(Connection con, DtoObservacionProgramaPYP dto)
	{
		return SqlBaseProgramasPYPDao.guardarObservacioProgramaPYP(con, dto);
	}
}
