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
package com.princetonsa.dao.laboratorio;

import java.sql.Connection;
import java.util.HashMap;

/**
 * interface que contiene las diferentes utilidades de laboratorios
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Rios/a>
 */
public interface UtilidadLaboratoriosDao 
{

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param fechaCambio
	 * @param horaCambio
	 * @param loginUsuario
	 * @param estadoHistoriaClinica
	 */
	public abstract boolean pasarSolicitudATomaMuestras(Connection con, String numeroSolicitud, String fechaCambio, String horaCambio, String loginUsuario,int estadoHistoriaClinica);

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param fechaCambio
	 * @param horaCambio
	 * @param loginUsuario
	 * @return
	 */
	public abstract boolean pasarSolicitudAEnProceso(Connection con, String numeroSolicitud, String fechaCambio, String horaCambio, String loginUsuario);
	
	/**
	 * Método implementado para consultar los registros de la tabla de interfaz laboratorios que no se hayan leido
	 * @param con
	 * @return
	 */
	public abstract HashMap consultarInterfazLaboratorios(Connection con );
	
	/**
	 * Método implementado para actualizar a leidos los registros que no se hayan leido
	 * de la tabla interfaz_laboratorio
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public abstract int actualizarLeidoInterfazLaboratorios( Connection con, String consecutivo);
	
	/**
	 * Método que inserta una respuesta de procedimientos
	 * @param con
	 * @param campos
	 * @param finalizar
	 * @return
	 */
	public abstract boolean insertarRespuestaProcedimientos(Connection con,HashMap campos,boolean finalizar);
	
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
	public abstract boolean validarTiempoReproceso(Connection con,String numeroSolicitud,String fechaReproceso,String horaReproceso);
	
	/**
	 * Método para obtener informacion adicional de la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public abstract HashMap getInformacionAdicionalSolicitud(Connection con,String numeroSolicitud);
	
	/**
	 * Método implementado para insertar el log de inconsistencias
	 * @param con
	 * @param campos
	 * @return
	 */
	public abstract int insertarLogInconsistencias(Connection con,HashMap campos);
	
	/**
	 * Método para obtener el codigo de laboratorio del servicio
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	public abstract int obtenerCodigoLaboratorioServicio(Connection con,int codigoServicio);

}
