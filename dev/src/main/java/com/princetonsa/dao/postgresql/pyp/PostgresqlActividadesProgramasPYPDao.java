package com.princetonsa.dao.postgresql.pyp;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.pyp.ActividadesProgramasPYPDao;
import com.princetonsa.dao.sqlbase.pyp.SqlBaseActividadesProgramasPYPDao;

public class PostgresqlActividadesProgramasPYPDao implements ActividadesProgramasPYPDao {
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public HashMap consultarActivadesProgramasPYP(Connection con, String programa, String institucion)
	{
		return SqlBaseActividadesProgramasPYPDao.consultarActivadesProgramasPYP(con,programa,institucion);
	}

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param programa
	 * @param actividad
	 * @param acronimoDiagnostico
	 * @param tipoCieDiagnostico
	 * @param embarazo
	 * @param semanasGestacion
	 * @param requerido
	 * @param archivo
	 * @param activo
	 */
	public boolean insertarActividadPrograma(Connection con, int institucion, String programa, String actividad,  boolean embarazo, String semanasGestacion, boolean requerido, String archivo, boolean activo, String finalidadConsulta, String finalidadServicio, boolean permitirEjecutar)
	{
		String sentencia="INSERT INTO actividades_programa_pyp(codigo,institucion,programa,actividad,embarazo,semanas_gestacion,requerido,archivo,activo,finalidad_consulta,finalidad_servicio,permitir_ejecutar) VALUES(nextval('seq_act_prog_pyp'),?,?,?,?,?,?,?,?,?,?,?)";
		return SqlBaseActividadesProgramasPYPDao.insertarActividadPrograma(con,institucion,programa,actividad,embarazo,semanasGestacion,requerido,archivo,activo,finalidadConsulta,finalidadServicio,sentencia,permitirEjecutar);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param programa
	 * @param actividad
	 * @param institucion
	 */
	public HashMap consultarActivadProgramaPYP(Connection con, String programa, String actividad, int institucion)
	{
		return SqlBaseActividadesProgramasPYPDao.consultarActivadProgramaPYP(con,programa,actividad,institucion);
	}

	
	/**
	 * 
	 * @param con
	 * @param actividad
	 * @param acronimoDiagnostico
	 * @param tipoCieDiagnostico
	 * @param embarazo
	 * @param semanasGestacion
	 * @param requerido
	 * @param archivo
	 * @param activo
	 */
	public boolean modifcarActividadPrograma(Connection con, String codigo,String actividad, boolean embarazo, String semanasGestacion, boolean requerido, String archivo, boolean activo, String finalidadConsulta, String finalidadServicio, boolean permitirEjecutar)
	{
		return SqlBaseActividadesProgramasPYPDao.modifcarActividadPrograma(con,codigo,actividad,embarazo,semanasGestacion,requerido,archivo,activo,finalidadConsulta,finalidadServicio,permitirEjecutar);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return 
	 */
	public boolean eliminarActividad(Connection con, String codigo)
	{
		return SqlBaseActividadesProgramasPYPDao.eliminarActividad(con,codigo);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap cargarViasIngresoActividadPrograma(Connection con, String codigo)
	{
		return SqlBaseActividadesProgramasPYPDao.cargarViasIngresoActividadPrograma(con,codigo);
	}
	
	/**
	 * 
	 * @param con
	 * @param programa
	 * @param viaIngreso
	 * @param ocupacion
	 */
	public boolean eliminarRegistroViaIngreso(Connection con, String codigoAP, String viaIngreso, String ocupacion)
	{
		return SqlBaseActividadesProgramasPYPDao.eliminarRegistroViaIngreso(con,codigoAP,viaIngreso,ocupacion);
	}

	/**
	 * 
	 * @param con
	 * @param codigoAP
	 * @param viaIngreso
	 * @param ocupacion
	 * @param accion
	 * @return
	 */
	public boolean existeModificacionViaIngreso(Connection con, String codigoAP, String viaIngreso, String ocupacion, boolean solicitar,boolean programar,boolean ejecutar)
	{
		return SqlBaseActividadesProgramasPYPDao.existeModificacionViaIngreso(con,codigoAP,viaIngreso,ocupacion,solicitar,programar,ejecutar);

	}
	
	/**
	 * 
	 * @param con
	 * @param codigoAP
	 * @param viaIngreso
	 * @param ocupacion
	 * @param accion
	 * @return
	 */
	public boolean modificarRegistroViaIngreso(Connection con, String codigoAP, String viaIngreso, String ocupacion, boolean solicitar,boolean programar,boolean ejecutar)
	{
		return SqlBaseActividadesProgramasPYPDao.modificarRegistroViaIngreso(con,codigoAP,viaIngreso,ocupacion,solicitar,programar,ejecutar);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoAP
	 * @param viaIngreso
	 * @param ocupacion
	 * @param accion
	 * @return
	 */
	public boolean insertarRegistroViaIngreso(Connection con, String codigoAP, String viaIngreso, String ocupacion, boolean solicitar,boolean programar,boolean ejecutar)
	{
		return SqlBaseActividadesProgramasPYPDao.insertarRegistroViaIngreso(con,codigoAP,viaIngreso,ocupacion,solicitar,programar,ejecutar);
	}
	

	/**
	 * 
	 * @param con
	 * @param codigo
	 */
	public HashMap cargarGruposEtareos(Connection con, String codigo, String regimen)
	{
		return SqlBaseActividadesProgramasPYPDao.cargarGruposEtareos(con,codigo,regimen);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param regimen
	 * @param grupoEtareo
	 * @return
	 */
	public boolean eliminarRegistroGrupoEtareo(Connection con, String codigo, String regimen, String grupoEtareo)
	{
		return SqlBaseActividadesProgramasPYPDao.eliminarRegistroGrupoEtareo(con,codigo,regimen,grupoEtareo);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param regimen
	 * @param grupoEtareo
	 * @param frecuencia
	 * @return
	 */
	public boolean existeModificacionGrupoEtareo(Connection con, String codigo, String regimen, String grupoEtareo, String frecuencia,String tipoFrecuencia)
	{
		return SqlBaseActividadesProgramasPYPDao.existeModificacionGrupoEtareo(con,codigo,regimen,grupoEtareo,frecuencia,tipoFrecuencia);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param regimen
	 * @param grupoEtareo
	 * @param frecuencia
	 * @return
	 */
	public boolean modificarRegistroGrupoEtareo(Connection con, String codigo, String regimen, String grupoEtareo, String frecuencia,String tipoFrecuencia)
	{
		return SqlBaseActividadesProgramasPYPDao.modificarRegistroGrupoEtareo(con,codigo,regimen,grupoEtareo,frecuencia,tipoFrecuencia);
	}
	
	/***
	 * 
	 * @param con
	 * @param codigo
	 * @param regimen
	 * @param grupoEtareo
	 * @param frecuencia
	 * @return
	 */
	public boolean insertarRegistroGrupoEtareo(Connection con, String codigo, String regimen, String grupoEtareo, String frecuencia,String tipoFrecuencia)
	{
		return SqlBaseActividadesProgramasPYPDao.insertarRegistroGrupoEtareo(con,codigo,regimen,grupoEtareo,frecuencia,tipoFrecuencia);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap cargarMetas(Connection con, String codigo)
	{
		return SqlBaseActividadesProgramasPYPDao.cargarMetas(con,codigo);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param regimen
	 * @return
	 */
	public boolean eliminarRegistroMeta(Connection con, String codigo, String regimen)
	{
		return SqlBaseActividadesProgramasPYPDao.eliminarRegistroMeta(con,codigo,regimen);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param regimen
	 * @param meta
	 * @return
	 */
	public boolean existeModificacionMetas(Connection con, String codigo, String regimen, String meta)
	{
		return SqlBaseActividadesProgramasPYPDao.existeModificacionMetas(con,codigo,regimen,meta);
	}


	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param regimen
	 * @param meta
	 * @return
	 */
	public boolean modificarRegistroMetas(Connection con, String codigo, String regimen, String meta)
	{
		return SqlBaseActividadesProgramasPYPDao.modificarRegistroMetas(con,codigo,regimen,meta);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param regimen
	 * @param meta
	 * @return
	 */
	public boolean insertarRegistroMeta(Connection con, String codigo, String regimen, String meta)
	{
		return SqlBaseActividadesProgramasPYPDao.insertarRegistroMeta(con,codigo,regimen,meta);
	}
	
	/**
	 * 
	 */
	public HashMap consultarDiagnosticosActProPYP(Connection con, String codigo)
	{
		return SqlBaseActividadesProgramasPYPDao.consultarDiagnosticosActProPYP(con,codigo);
	}
	
	

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param acronimo
	 * @param cie
	 * @return
	 */
	public boolean guardarDiagnostico(Connection con, String codigo, String acronimo, String cie)
	{
		return SqlBaseActividadesProgramasPYPDao.guardarDiagnostico(con,codigo,acronimo,cie);
	}

	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param acronimo
	 * @param cie
	 * @return
	 */
	public boolean eliminarDiagnostico(Connection con, String codigo, String acronimo, String cie)
	{
		return SqlBaseActividadesProgramasPYPDao.eliminarDiagnostico(con,codigo,acronimo,cie);
	}
}
