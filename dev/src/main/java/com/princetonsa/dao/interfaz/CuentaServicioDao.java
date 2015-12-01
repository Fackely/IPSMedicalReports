/*
 * Creado el Apr 18, 2006
 * por Julian Montoya
 */
package com.princetonsa.dao.interfaz;

import java.sql.Connection;
import java.util.HashMap;

public interface CuentaServicioDao {

	
	/**
	 * Metodo para cargar los grupos de servicios. 
	 * @param con @todo
	 * @param tipoGrupo 
	 * @param grupoServicio @todo
	 * @param tipoServicio @todo
	 * @param institucion 
	 * @return
	 */

	public HashMap cargarGrupos(Connection con, int tipoGrupo, int centroCosto, int grupoServicio, String tipoServicio, int institucion, int especialidad);

	/**
	 * Metodo para insertar los grupos.
	 * @param con
	 * @param centroCosto
	 * @param codigoTipoGrupo
	 * @param cuentaIngreso
	 * @param modificado
	 * @param tipoSerSel 
	 * @param Acronimo
	 * @param cuentaVigenciaAnterior
	 * @return
	 */
	public int insertarDatos(Connection con, int codigoTipoGrupo, int centroCosto, int codigoGrupo, String cuentaIngreso, boolean modificado, String acronimo, String tipoSerSel, String cuentaVigenciaAnterior, String cuentaCosto);

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
	public int eliminar(Connection con, int tablaDestino, int centroCosto, int grupoServicio, String tipoServicio, int especialidad, int servicio);

	
	/**
	 * Metodo para eliminar el registro cuando las 2 cuentas contables estan en nulo
	 * @param con
	 * @param tablaEliminar
	 * @return
	 */
	public int eliminarCuentasContablesNulas(Connection con, int tablaEliminar);
}
