/*
 * Creado el Aug 8, 2006
 * por Julian Montoya
 */
package com.princetonsa.dao.oracle.pyp;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.pyp.ProgramaArticuloPypDao;
import com.princetonsa.dao.sqlbase.pyp.SqlBaseProgramaArticuloDao;

public class OracleProgramaArticuloPypDao implements ProgramaArticuloPypDao {

	/**
	 *  Metodo para cargar la información General de la Funcionalidad
	 */
	public HashMap consultarInformacion(Connection con, HashMap mapaParam)
	{
		return SqlBaseProgramaArticuloDao.consultarInformacion(con, mapaParam);
	}
	
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
	public int insertarActividadesCentroAtencion(Connection con, int operacion, String programa, int codigoArticulo, int institucion, boolean activo)
	{
		return SqlBaseProgramaArticuloDao.insertarActividadesCentroAtencion(con, operacion, programa, codigoArticulo, institucion, activo);
	}
	
	/**
	 * Metodo para eliminar una Articulo de un Programa de Salud PYP Especifico. 
	 * @param con
	 * @param programa
	 * @param nroArticuloEliminado
	 * @param institucion
	 * @return
	 */
	public int eliminarActividadesCentroAtencion(Connection con, String programa, int nroArticuloEliminado, int institucion)
	{
		return SqlBaseProgramaArticuloDao.eliminarActividadesCentroAtencion(con, programa, nroArticuloEliminado, institucion);
	}

	/**
	 * 
	 */
	public HashMap cargarViasIngresoActividadPrograma(Connection con, String programa, String actividad, String institucion)
	{
		return SqlBaseProgramaArticuloDao.cargarViasIngresoActividadPrograma(con,programa,actividad,institucion);
	}
	
	public boolean eliminarRegistroViaIngreso(Connection con, String programa, String actividad, String institucion, String viaIngreso, String ocupacion)
	{
		return SqlBaseProgramaArticuloDao.eliminarRegistroViaIngreso(con,programa,actividad,institucion,viaIngreso,ocupacion);
	}

	public boolean modificarRegistroViaIngreso(Connection con, String programa, String actividad, String institucion, String viaIngreso, String ocupacion, boolean solicitar, boolean programar, boolean ejecutar)
	{
		return SqlBaseProgramaArticuloDao.modificarRegistroViaIngreso( con,  programa,  actividad,  institucion,  viaIngreso,  ocupacion,  solicitar,  programar,  ejecutar);
	}

	public boolean insertarRegistroViaIngreso(Connection con, String programa, String actividad, String institucion, String viaIngreso, String ocupacion, boolean solicitar, boolean programar, boolean ejecutar)
	{
		return SqlBaseProgramaArticuloDao.insertarRegistroViaIngreso(con,programa,  actividad,  institucion,  viaIngreso,  ocupacion,  solicitar,  programar,  ejecutar);
	}
	

	public HashMap cargarGruposEtareos(Connection con, String programa, String actividad, String institucion, String regimen)
	{
		 return SqlBaseProgramaArticuloDao.cargarGruposEtareos(con,programa,actividad,institucion,regimen);
	}


	public boolean eliminarRegistroGrupoEtareo(Connection con, String programa, String actividad, String institucion, String regimen, String grupoEtareo)
	{
		return SqlBaseProgramaArticuloDao.eliminarRegistroGrupoEtareo(con,programa,actividad,institucion,regimen,grupoEtareo);
	}

	public boolean insertarRegistroGrupoEtareo(Connection con, String programa, String actividad, String institucion, String regimen, String grupoEtareo, String frecuencia, String tipoFrecuencia)
	{
		return SqlBaseProgramaArticuloDao.insertarRegistroGrupoEtareo(con,programa,actividad,institucion,regimen,grupoEtareo,frecuencia,tipoFrecuencia);
	}

	public boolean modificarRegistroGrupoEtareo(Connection con, String programa, String actividad, String institucion, String regimen, String grupoEtareo, String frecuencia, String tipoFrecuencia)
	{
		return SqlBaseProgramaArticuloDao.modificarRegistroGrupoEtareo(con,programa,actividad,institucion,regimen,grupoEtareo,frecuencia,tipoFrecuencia);
	}

}
