/*
 * Created on 16/11/2006
 * 
 * @author <a href="mailto:wilson@hotmail.com">Wilson Rios</a>
 * 
 * Copyright Princeton S.A. &copy;&reg; 2005. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 *
 */
package com.princetonsa.dao.postgresql.laboratorios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.laboratorio.UtilidadLaboratoriosDao;
import com.princetonsa.dao.sqlbase.laboratorios.SqlBaseUtilidadLaboratoriosDao;

/**
 * clase que contiene las diferentes utilidades de laboratorios para postgres
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Rios/a>
 */
public class PostgresqlUtilidadLaboratoriosDao implements UtilidadLaboratoriosDao 
{

	/**
	 * 
	 */
	public boolean pasarSolicitudATomaMuestras(Connection con, String numeroSolicitud, String fechaCambio, String horaCambio, String loginUsuario,int estadoHistoriaClinica)
	{
		return SqlBaseUtilidadLaboratoriosDao.pasarSolicitudATomaMuestras(con,numeroSolicitud,fechaCambio,horaCambio,loginUsuario,estadoHistoriaClinica);
	}

	/**
	 * 
	 */
	public boolean pasarSolicitudAEnProceso(Connection con, String numeroSolicitud, String fechaCambio, String horaCambio, String loginUsuario)
	{
		return SqlBaseUtilidadLaboratoriosDao.pasarSolicitudAEnProceso(con,numeroSolicitud,fechaCambio,horaCambio,loginUsuario);
	}
	
	/**
	 * Método implementado para consultar los registros de la tabla de interfaz laboratorios que no se hayan leido
	 * @param con
	 * @return
	 */
	public HashMap consultarInterfazLaboratorios(Connection con )
	{
		return SqlBaseUtilidadLaboratoriosDao.consultarInterfazLaboratorios(con);
	}
	
	/**
	 * Método implementado para actualizar a leidos los registros que no se hayan leido
	 * de la tabla interfaz_laboratorio
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public int actualizarLeidoInterfazLaboratorios( Connection con,String consecutivo)
	{
		return SqlBaseUtilidadLaboratoriosDao.actualizarLeidoInterfazLaboratorios(con,consecutivo);
	}
	
	/**
	 * Método que inserta una respuesta de procedimientos
	 * @param con
	 * @param campos
	 * @param finalizar
	 * @return
	 */
	public boolean insertarRespuestaProcedimientos(Connection con,HashMap campos,boolean finalizar)
	{
		campos.put("secuencia","nextval('seq_res_sol_proc')");
		return SqlBaseUtilidadLaboratoriosDao.insertarRespuestaProcedimientos(con,campos,finalizar);
	}
	
	/**
	 * Método que valida el tiempo de reproceso
	 * Si es True es válido
	 * Si es Falso se superó el tiempo de reproceso
	 * @param con
	 * @param numeroSolicitud
	 * @param fechaReproceso
	 * @param horaReproceso
	 * @return
	 */
	public boolean validarTiempoReproceso(Connection con,String numeroSolicitud,String fechaReproceso,String horaReproceso)
	{
		return SqlBaseUtilidadLaboratoriosDao.validarTiempoReproceso(con,numeroSolicitud,fechaReproceso,horaReproceso);
	}
	
	/**
	 * Método para obtener informacion adicional de la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public HashMap getInformacionAdicionalSolicitud(Connection con,String numeroSolicitud)
	{
		return SqlBaseUtilidadLaboratoriosDao.getInformacionAdicionalSolicitud(con,numeroSolicitud); 
	}
	
	/**
	 * Método implementado para insertar el log de inconsistencias
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertarLogInconsistencias(Connection con,HashMap campos)
	{
		campos.put("secuencia","nextval('seq_incon_interfaz_lab')");
		return SqlBaseUtilidadLaboratoriosDao.insertarLogInconsistencias(con,campos);
	}
	
	/**
	 * Método para obtener el codigo de laboratorio del servicio
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	public int obtenerCodigoLaboratorioServicio(Connection con,int codigoServicio)
	{
		return SqlBaseUtilidadLaboratoriosDao.obtenerCodigoLaboratorioServicio(con, codigoServicio);
	}
}
