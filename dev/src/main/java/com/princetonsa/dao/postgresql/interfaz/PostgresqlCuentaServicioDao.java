/*
 * Creado el Apr 18, 2006
 * por Julian Montoya
 */
package com.princetonsa.dao.postgresql.interfaz;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.interfaz.CuentaServicioDao;
import com.princetonsa.dao.sqlbase.interfaz.SqlBaseCuentaServicioDao;

public class PostgresqlCuentaServicioDao implements CuentaServicioDao 
{


	/**
	 * Metodo para cargar los grupos de servicios. 
	 * @return
	 */

	public HashMap cargarGrupos( Connection con, int tipoGrupos, int centroCosto, int grupoServicio, String tipoServicio, int institucion, int especialidad) 
	{
		return SqlBaseCuentaServicioDao.cargarGrupos(con, tipoGrupos, centroCosto, grupoServicio, tipoServicio, institucion, especialidad);	 
	}
	/**
	 * Metodo para insertar los grupos.
	 * @param con
	 * @param centroCosto
	 * @param codigoTipoGrupo
	 * @param cuentaIngreso
	 * @param modificado
	 * @return
	 */
	public int insertarDatos(Connection con, int codigoTipoGrupo, int centroCosto, int codigoGrupo, String cuentaIngreso, boolean modificado,  String acronimo,  String tipoSerSel , String cuentaVigenciaAnterior, String cuentaCosto)
	{
		return SqlBaseCuentaServicioDao.insertarDatos(con,  codigoTipoGrupo,  centroCosto, codigoGrupo, cuentaIngreso, modificado, acronimo, tipoSerSel, cuentaVigenciaAnterior, cuentaCosto);
	}

	
	/**
	 * Metodo para eliminar la cuenta contable.
	 * @param con
	 * @param tablaDestino
	 * @param grupoServicio
	 * @param tipoServicio
	 * @param especialidad
	 * @param servicio
	 * @return
	 */
	public int eliminar(Connection con, int tablaDestino, int centroCosto, int grupoServicio, String tipoServicio, int especialidad, int servicio)
	{
		return SqlBaseCuentaServicioDao.eliminar(con, tablaDestino, centroCosto, grupoServicio, tipoServicio, especialidad, servicio);
	}

	public int eliminarCuentasContablesNulas(Connection con, int tablaEliminar)
	{
		return SqlBaseCuentaServicioDao.eliminarCuentasContablesNulas(con, tablaEliminar);
	}	
}
