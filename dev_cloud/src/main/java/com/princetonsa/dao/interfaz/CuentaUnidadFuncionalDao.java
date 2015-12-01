/*
 * Creado el May 12, 2006
 * por Julian Montoya
 */
package com.princetonsa.dao.interfaz;

import java.sql.Connection;
import java.util.HashMap;

public interface CuentaUnidadFuncionalDao 
{

	/**
	 *  Metodo para insertar las unidades funcionales.
	 * @param con
	 * @param i
	 * @param institucion
	 * @param codUnidadFuncional
	 * @param cuentaIngreso
	 * @param rubroPresupuestal 
	 * @param modificado2 
	 * @param codigo_unifun_ingresado
	 * @return
	 */

	int insertarUnidadesFuncionales(Connection con, int tipoInsercion, int institucion, String codUnidadFuncional, int centroCosto, String cuentaIngreso, String cuentaMedicamento,  int consecutivo, String modificado , String cuentaVigenciaAnterior , String cuentaVigenciaAnteriorMed, String rubroPresupuestal, String cuentaCosto);
	

	/** 
	 * Metodo para cargar las unidades funcionales.
	 * @param con
	 * @param tipoGrupo
	 * @param institucion
	 * @param unidadFuncional 
	 * @return
	 */
	HashMap cargarUnidadesFuncionales(Connection con, int tipoGrupo, int institucion, String unidadFuncional);


	/**
	 *  Metodo para eliminar la Cuenta Ingreso ó Medicamento de una Unidad especifica. 
	 * @param con
	 * @param esCuentaIngreso
	 * @param consecutivo
	 * @return
	 */
	int eliminarCuenta(Connection con, boolean esCuentaIngreso, int consecutivo, int centroCosto, int institucion, boolean esCuentaAnterior);

	/**
	 * Metodo para eliminar el Rubro Presupuestal de una unidad Especifica
	 * @param con
	 * @param consecutivo
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	int eliminarRubro(Connection con, int consecutivo, int centroCosto,	int institucion);
}
