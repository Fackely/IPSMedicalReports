/*
 * Creado el May 12, 2006
 * por Julian Montoya
 */
package com.princetonsa.dao.oracle.interfaz;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.interfaz.CuentaUnidadFuncionalDao;
import com.princetonsa.dao.sqlbase.interfaz.SqlBaseCuentaUnidadFuncionalDao;

public class OracleCuentaUnidadFuncionalDao implements  CuentaUnidadFuncionalDao 
{

	/**
	 * Metodo para insertar las unidades funcionales.
	 * @param con
	 * @param tipoInsercion
	 * @param institucion
	 * @param codUnidadFuncional
	 * @param cuentaIngresoMod
	 * @param modificado
	 * @param rubroPresupuestal
	 * @return
	 */
		
	public int insertarUnidadesFuncionales(Connection con, int tipoInsercion, int institucion, String codUnidadFuncional, int centroCosto, String cuentaIngreso, String cuentaMedicamento,  int consecutivo,  String modificado, String cuentaVigenciaAnterior , String cuentaVigenciaAnteriorMed, String rubroPresupuestal, String cuentaCosto)
	{
		return SqlBaseCuentaUnidadFuncionalDao.insertarUnidadesFuncionales( con,  tipoInsercion, institucion,  codUnidadFuncional, centroCosto, cuentaIngreso, cuentaMedicamento, consecutivo,  modificado , cuentaVigenciaAnterior , cuentaVigenciaAnteriorMed, rubroPresupuestal, cuentaCosto);
	}
	
	/**
	 * Metodo para cargar las unidades funcionales.
	 * @param con
	 * @param tipoGrupo
	 * @param institucion
	 * @return
	 */
	public HashMap cargarUnidadesFuncionales(Connection con, int tipoGrupo, int institucion,  String unidadFuncional)
	{
		return SqlBaseCuentaUnidadFuncionalDao.cargarUnidadesFuncionales(con, tipoGrupo, institucion,   unidadFuncional);
	}

	/**
	 *  Metodo para eliminar la Cuenta Ingreso ó Medicamento de una Unidad especifica. 
	 * @param con
	 * @param esCuentaIngreso
	 * @param consecutivo
	 * @return
	 */
	public int eliminarCuenta(Connection con, boolean esCuentaIngreso, int consecutivo, int centroCosto, int institucion, boolean esCuentaAnterior)
	{
		return SqlBaseCuentaUnidadFuncionalDao.eliminarCuenta(con, esCuentaIngreso, consecutivo,  centroCosto,  institucion, esCuentaAnterior);
	}
	
	/**
	 * Metodo para eliminar el Rubro Presupuestal de una unidad Especifica
	 * @param con
	 * @param consecutivo
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public int eliminarRubro(Connection con, int consecutivo, int centroCosto,int institucion) 
	{
		return SqlBaseCuentaUnidadFuncionalDao.eliminarRubro(con, consecutivo, centroCosto, institucion);
	}

}
