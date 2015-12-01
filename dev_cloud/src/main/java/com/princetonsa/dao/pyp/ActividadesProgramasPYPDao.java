package com.princetonsa.dao.pyp;

import java.sql.Connection;
import java.util.HashMap;

public interface ActividadesProgramasPYPDao {

	public abstract HashMap consultarActivadesProgramasPYP(Connection con, String programa, String institucion);

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param programa
	 * @param actividad
	 * @param embarazo
	 * @param semanasGestacion
	 * @param requerido
	 * @param archivo
	 * @param activo
	 * @param string2 
	 * @param string 
	 */
	public abstract boolean insertarActividadPrograma(Connection con, int institucion, String programa, String actividad,  boolean embarazo, String semanasGestacion, boolean requerido, String archivo, boolean activo, String finalidadConsulta, String finalidadServicio, boolean permitirEjecutar);

	/**
	 * 
	 * @param con
	 * @param programa
	 * @param actividad
	 * @param institucion
	 */
	public abstract HashMap consultarActivadProgramaPYP(Connection con, String programa, String actividad, int institucion);

	
	/**
	 * 
	 * @param con
	 * @param actividad
	 * @param embarazo
	 * @param semanasGestacion
	 * @param requerido
	 * @param archivo
	 * @param activo
	 * @param finalidadConsulta 
	 * @param finalidadServicio 
	 */
	public abstract boolean modifcarActividadPrograma(Connection con, String codigo,String actividad,  boolean embarazo, String semanasGestacion, boolean requerido, String archivo, boolean activo, String finalidadConsulta, String finalidadServicio, boolean permitirEjecutar);

	/**
	 * 
	 * @param con
	 * @param codigo
	 */
	public abstract boolean eliminarActividad(Connection con, String codigo);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public abstract HashMap cargarViasIngresoActividadPrograma(Connection con, String codigo);

	/**
	 * 
	 * @param con
	 * @param programa
	 * @param viaIngreso
	 * @param ocupacion
	 */
	public abstract boolean eliminarRegistroViaIngreso(Connection con, String codigoAP, String viaIngreso, String ocupacion);

	/**
	 * 
	 * @param con
	 * @param codigoAP
	 * @param viaIngreso
	 * @param ocupacion
	 * @param accion
	 * @return
	 */
	public abstract boolean existeModificacionViaIngreso(Connection con, String codigoAP, String viaIngreso, String ocupacion, boolean solicitar,boolean programar,boolean ejecutar);

	/**
	 * 
	 * @param con
	 * @param codigoAP
	 * @param viaIngreso
	 * @param ocupacion
	 * @param accion
	 * @return
	 */
	public abstract boolean modificarRegistroViaIngreso(Connection con, String codigoAP, String viaIngreso, String ocupacion, boolean solicitar,boolean programar,boolean ejecutar);

	/**
	 * 
	 * @param con
	 * @param codigoAP
	 * @param viaIngreso
	 * @param ocupacion
	 * @param accion
	 * @return
	 */
	public abstract boolean insertarRegistroViaIngreso(Connection con, String codigoAP, String viaIngreso, String ocupacion, boolean solicitar,boolean programar,boolean ejecutar);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param regimen 
	 */
	public abstract HashMap cargarGruposEtareos(Connection con, String codigo, String regimen);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param regimen
	 * @param grupoEtareo
	 * @return
	 */
	public abstract boolean eliminarRegistroGrupoEtareo(Connection con, String codigo, String regimen, String grupoEtareo);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param regimen
	 * @param grupoEtareo
	 * @param frecuencia
	 * @param tipoFrecuencia 
	 * @return
	 */
	public abstract boolean existeModificacionGrupoEtareo(Connection con, String codigo, String regimen, String grupoEtareo, String frecuencia, String tipoFrecuencia);

	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param regimen
	 * @param grupoEtareo
	 * @param frecuencia
	 * @param tipoFrecuencia 
	 * @return
	 */
	public abstract boolean modificarRegistroGrupoEtareo(Connection con, String codigo, String regimen, String grupoEtareo, String frecuencia, String tipoFrecuencia);

	/***
	 * 
	 * @param con
	 * @param codigo
	 * @param regimen
	 * @param grupoEtareo
	 * @param frecuencia
	 * @param tipoFrecuencia 
	 * @return
	 */
	public abstract boolean insertarRegistroGrupoEtareo(Connection con, String codigo, String regimen, String grupoEtareo, String frecuencia, String tipoFrecuencia);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public abstract HashMap cargarMetas(Connection con, String codigo);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param regimen
	 * @return
	 */
	public abstract boolean eliminarRegistroMeta(Connection con, String codigo, String regimen);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param regimen
	 * @param meta
	 * @return
	 */
	public abstract boolean existeModificacionMetas(Connection con, String codigo, String regimen, String meta);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param regimen
	 * @param meta
	 * @return
	 */
	public abstract boolean modificarRegistroMetas(Connection con, String codigo, String regimen, String meta);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param regimen
	 * @param meta
	 * @return
	 */
	public abstract boolean insertarRegistroMeta(Connection con, String codigo, String regimen, String meta);

	/**
	 * 
	 */
	public abstract HashMap consultarDiagnosticosActProPYP(Connection con, String codigo);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param acronimo
	 * @param cie
	 * @return
	 */
	public abstract boolean guardarDiagnostico(Connection con, String codigo, String acronimo, String cie);

	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param acronimo
	 * @param cie
	 * @return
	 */
	public abstract boolean eliminarDiagnostico(Connection con, String codigo, String acronimo, String cie);

}
