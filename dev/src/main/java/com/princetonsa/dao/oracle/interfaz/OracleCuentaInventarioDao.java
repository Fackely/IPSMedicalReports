/*
 * Creado en Apr 18, 2006
 * Andr�s Mauricio Ruiz V�lez
 * Princeton S.A (Parquesoft - Manizales)
 */
package com.princetonsa.dao.oracle.interfaz;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.interfaz.CuentaInventarioDao;
import com.princetonsa.dao.sqlbase.interfaz.SqlBaseCuentaInventarioDao;

public class OracleCuentaInventarioDao implements CuentaInventarioDao
{
	/**
	 * M�todo para consultar las clases de inventario parametrizados para el centro de costo y 
	 * la instituci�n con la informaci�n ingresada de la cuenta de ingreso.
	 * @param con
	 * @param centroCostoSeleccionado
	 * @param institucion
	 * @return HashMap
	 */
	public HashMap consultarClaseInventariosCuentaIngreso(Connection con, int centroCostoSeleccionado, int institucion)
	{
		return SqlBaseCuentaInventarioDao.consultarClaseInventariosCuentaIngreso (con, centroCostoSeleccionado, institucion);
	}
	
	/**
	 * M�todo que carga los grupos de inventario para el centro de costo seleccionado y la 
	 * clase de inventario seleccionada
	 * @param con
	 * @param centroCostoSeleccionado
	 * @param claseInventarioSeleccionado
	 * @return HashMap
	 */
	public HashMap consultarGrupoInventariosCuentaIngreso(Connection con, int centroCostoSeleccionado, int claseInventarioSeleccionado)
	{
		return SqlBaseCuentaInventarioDao.consultarGrupoInventariosCuentaIngreso (con, centroCostoSeleccionado, claseInventarioSeleccionado);
	}
	
	/**
	 * M�todo que carga los subgrupos de inventario para el centro de costo seleccionado y el 
	 * grupo de inventario seleccionado
	 * @param con
	 * @param centroCostoSeleccionado
	 * @param grupoInventarioSeleccionado
	 * @return HashMap
	 */
	public HashMap consultarSubGrupoInventariosCuentaIngreso(Connection con, int centroCostoSeleccionado, int grupoInventarioSeleccionado)
	{
		return SqlBaseCuentaInventarioDao.consultarSubGrupoInventariosCuentaIngreso (con, centroCostoSeleccionado, grupoInventarioSeleccionado);
	}
	
	/**
	 * M�todo que carga los articulos de inventario para el centro de costo seleccionado y el 
	 * subgrupo de inventario seleccionado
	 * @param con
	 * @param centroCostoSeleccionado
	 * @param subgrupoInventarioSel
	 * @return
	 */
	public HashMap consultarArticulosInventarioCuentaIngreso(Connection con, int centroCostoSeleccionado, int subgrupoInventarioSel)
	{
		return SqlBaseCuentaInventarioDao.consultarArticulosInventarioCuentaIngreso (con, centroCostoSeleccionado, subgrupoInventarioSel);
	}
	
	/**
	 * M�todo para insertar o modificar la cuenta de ingreso de una clase de 
	 * inventario para el centro de costo seleccionado
	 * @param con
	 * @param centroCosto
	 * @param claseInventario
	 * @param cuentaIngresoNueva
	 * @param esInsertar
	 * @param cuentaVigenciaAnterior
	 * @return
	 */
	public int insertarActualizarClaseInventario(Connection con, int centroCosto, int claseInventario, String cuentaIngresoNueva, int esInsertar, String cuentaVigenciaAnterior)
	{
		return SqlBaseCuentaInventarioDao.insertarActualizarClaseInventario (con, centroCosto, claseInventario, cuentaIngresoNueva, esInsertar, cuentaVigenciaAnterior);
	}
	
	/**
	 * M�todo para insertar o modificar la cuenta de ingreso de un grupo de 
	 * inventario para el centro de costo y clase de inventario seleccionado
	 * @param con
	 * @param centroCosto
	 * @param grupoInventario
	 * @param claseInventario
	 * @param cuentaIngresoNueva
	 * @param esInsertar
	 * @param cuentaVigenciaAnterior
	 * @return
	 */
	public int insertarActualizarGrupoInventario(Connection con, int centroCosto, int grupoInventario, int claseInventario, String cuentaIngresoNueva, int esInsertar, String cuentaVigenciaAnterior)
	{
		return SqlBaseCuentaInventarioDao.insertarActualizarGrupoInventario (con, centroCosto, grupoInventario, claseInventario, cuentaIngresoNueva, esInsertar,  cuentaVigenciaAnterior);
	}
	
	/**
	 * M�todo para insertar o modificar la cuenta de ingreso de un subgrupo de 
	 * inventario para el centro de costo, clase y grupo de inventario seleccionado
	 * @param con
	 * @param centroCosto
	 * @param subGrupoInventario
	 * @param cuentaIngresoNueva
	 * @param esInsertar
	 * @param cuentaVigenciaAnterior
	 * @return
	 */
	public int insertarActualizarSubGrupoInventario(Connection con, int centroCosto, int subGrupoInventario, String cuentaIngresoNueva, int esInsertar , String cuentaVigenciaAnterior)
	{
		return SqlBaseCuentaInventarioDao.insertarActualizarSubGrupoInventario (con, centroCosto, subGrupoInventario, cuentaIngresoNueva, esInsertar, cuentaVigenciaAnterior);
	}
	
	/**
	 * M�todo para insertar o modificar la cuenta de ingreso de un articulo de 
	 * inventario para el centro de costo, clase, grupo  y subgrupo de inventario seleccionado
	 * @param con
	 * @param centroCosto
	 * @param articuloInventario
	 * @param cuentaIngresoNueva
	 * @param esInsertar
	 * @param cuentaVigenciaAnterior
	 * @return
	 */
	public int insertarActualizarArticuloInventario(Connection con, int centroCosto, int articuloInventario, String cuentaIngresoNueva, int esInsertar, String cuentaVigenciaAnterior)
	{
		return SqlBaseCuentaInventarioDao.insertarActualizarArticuloInventario (con, centroCosto, articuloInventario, cuentaIngresoNueva, esInsertar, cuentaVigenciaAnterior);
	}
	
	/**
	 * M�todo que elimina de acuerdo al valor enviado en el par�metro tabla eliminar, 
	 * la cuenta contable respectiva 
	 * tablaEliminar 1 -> ClaseInventario
	 * 				 2 -> GrupoInventario
	 * 				 3 -> SubGrupoInventario
	 * 				 4 -> ArticuloInventario
	 * @param con
	 * @param tablaEliminar
	 * @param centroCosto
	 * @param claseInventario
	 * @param grupoInventario
	 * @param subGrupoInventario
	 * @param articuloInventario
	 * @return
	 */
	public int eliminarCuentaContable(Connection con, int tablaEliminar, int centroCosto, int claseInventario, int grupoInventario, int subGrupoInventario, int articuloInventario)
	 {
		return SqlBaseCuentaInventarioDao.eliminarCuentaContable(con, tablaEliminar, centroCosto, claseInventario, grupoInventario, subGrupoInventario, articuloInventario);
	 }
	
	/**
	 * M�todo que elimina un registro si las dos cuentas contables estan nulas
	 */
	public  int eliminarCuentasContablesNulas(Connection con, int tablaEliminar)
	{
		return SqlBaseCuentaInventarioDao.eliminarCuentasContablesNulas(con, tablaEliminar);
	}
}
