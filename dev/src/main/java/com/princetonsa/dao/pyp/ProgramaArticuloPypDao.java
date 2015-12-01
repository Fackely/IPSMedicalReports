/*
 * Creado el Aug 8, 2006
 * por Julian Montoya
 */
package com.princetonsa.dao.pyp;

import java.sql.Connection;
import java.util.HashMap;

public interface ProgramaArticuloPypDao {

	/**
	 * Metodo para cargar la información General de la Funcionalidad
	 * @param con
	 * @param mapaParam
	 * @return
	 */
	public HashMap consultarInformacion(Connection con, HashMap mapaParam);

	/**
	 * Metodo para Guardar Modificar Los Articulos por Programa PYP 
	 * @param con
	 * @param Operacion
	 * @param programa
	 * @param codigoArticulo
	 * @param institucion
	 * @param activo
	 * @return
	 */
	public int insertarActividadesCentroAtencion(Connection con, int operacion, String programa, int codigoArticulo, int institucion, boolean activo);

	/**
	 * Metodo para eliminar una Articulo de un Programa de Salud PYP Especifico. 
	 * @param con
	 * @param programa
	 * @param nroArticuloEliminado
	 * @param institucion
	 * @return
	 */
	public int eliminarActividadesCentroAtencion(Connection con, String programa, int nroArticuloEliminado, int institucion);

	/**
	 * 
	 * @param con
	 * @param programa
	 * @param actividad
	 * @param institucion
	 * @return
	 */
	public HashMap cargarViasIngresoActividadPrograma(Connection con, String programa, String actividad, String institucion);

	/**
	 * 
	 * @param con
	 * @param programa
	 * @param actividad
	 * @param institucion
	 * @param viaIngreso
	 * @param ocupacion
	 * @return
	 */
	public boolean eliminarRegistroViaIngreso(Connection con, String programa, String actividad, String institucion, String viaIngreso, String ocupacion);

	/**
	 * 
	 * @param con
	 * @param programa
	 * @param actividad
	 * @param institucion
	 * @param viaIngreso
	 * @param ocupacion
	 * @param solicitar
	 * @param programar
	 * @param ejecutar
	 * @return
	 */
	public boolean modificarRegistroViaIngreso(Connection con, String programa, String actividad, String institucion, String viaIngreso, String ocupacion, boolean solicitar, boolean programar, boolean ejecutar);

	/**
	 * 
	 * @param con
	 * @param programa
	 * @param actividad
	 * @param institucion
	 * @param viaIngreso
	 * @param ocupacion
	 * @param solicitar
	 * @param programar
	 * @param ejecutar
	 * @return
	 */
	public boolean insertarRegistroViaIngreso(Connection con, String programa, String actividad, String institucion, String viaIngreso, String ocupacion, boolean solicitar, boolean programar, boolean ejecutar);

	/**
	 * 
	 * @param con
	 * @param programa
	 * @param actividad
	 * @param institucion
	 * @param regimen
	 * @return
	 */
	public HashMap cargarGruposEtareos(Connection con, String programa, String actividad, String institucion, String regimen);

	/**
	 * 
	 * @param con
	 * @param programa
	 * @param actividad
	 * @param institucion
	 * @param regimen
	 * @param grupoEtareo
	 * @return
	 */
	public boolean eliminarRegistroGrupoEtareo(Connection con, String programa, String actividad, String institucion, String regimen, String grupoEtareo);

	/**
	 * 
	 * @param con
	 * @param programa
	 * @param actividad
	 * @param institucion
	 * @param regimen
	 * @param grupoEtareo
	 * @param frecuencia
	 * @param tipoFrecuencia
	 * @return
	 */
	public boolean modificarRegistroGrupoEtareo(Connection con, String programa, String actividad, String institucion, String regimen, String grupoEtareo, String frecuencia, String tipoFrecuencia);

	/**
	 * 
	 * @param con
	 * @param programa
	 * @param actividad
	 * @param institucion
	 * @param regimen
	 * @param grupoEtareo
	 * @param frecuencia
	 * @param tipoFrecuencia
	 * @return
	 */
	public boolean insertarRegistroGrupoEtareo(Connection con, String programa, String actividad, String institucion, String regimen, String grupoEtareo, String frecuencia, String tipoFrecuencia);
}
