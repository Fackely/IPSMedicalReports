/*
 * Creado    Jun 16 2005
 * Modificado Sep 27 2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */

package com.princetonsa.dao;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

/**
 * @author Sebastián Gómez R
 * Interfaz para el acceder a la fuente de datos de 
 * conceptos de ingreso de Tesorería.
 *
 */

public interface ConceptoTesoreriaDao 
{	
	/**
	 * Método usado para cargar los conceptos de ingreso de tesorería 
	 * parametrizados por institución
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap cargarConceptosTesoreria(Connection con,int institucion);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public String cargarTipoPagoEspecial (Connection con, int codigo);
	
	/**
	 * Método usado para ingresar un nuevo concepto de tesoreria
	 * @param con
	 * @param codigo
	 * @param descripcion
	 * @param tipo
	 * @param valor
	 * @param cuenta
	 * @param docum_ingreso
	 * @param docum_anulacion
	 * @param centroCosto
	 * @param nit
	 * @param activo
	 * @param institucion
	 * @return
	 */
	public int insertarConceptoTesoreria (
			Connection con, 
			String codigo, 
			String descripcion, 
			int tipo, 
			String valor,
			String cuenta, 
			int documIngreso, 
			int documAnulacion,
			int centroCosto, 
			int nit, 
			boolean activo,
			int institucion,String rubroPresupuestal);
	
	/**
	 * Método usado para actualizar un registro de conceptos ingreso tesorería
	 * @param con
	 * @param codigo
	 * @param descripcion
	 * @param tipo
	 * @param valor
	 * @param cuenta
	 * @param documIngreso
	 * @param documAnulacion
	 * @param centroCosto
	 * @param nit
	 * @param activo
	 * @param codigoAntiguo
	 * @param institucion
	 * @return
	 */
	public int actualizarConceptoTesoreria (Connection con,
			String codigo, 
			String descripcion, 
			int tipo, 
			String valor,
			String cuenta, 
			int documIngreso, 
			int documAnulacion,
			int centroCosto, 
			int nit, 
			boolean activo,
			String codigoAntiguo,
			int institucion,String rubroPresupuestal);
	
	/**
	 * Método para eliminar un concepto de ingreso tesoreria
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public int eliminarConceptoTesoreria(Connection con,String codigo,int institucion);
	
	/**
	 * Método usado para cargar un registro de los conceptos de ingreso de tesoreria
	 * por su código y la institución
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public HashMap cargarConceptoTesoreria(Connection con,String codigo,int institucion);
	
	/**
	 * Método usado para la búsqueda avanzada de los conceptos de tesorería
	 * en la opción de consulta
	 * @param con
	 * @param codigo
	 * @param descripcion
	 * @param tipo
	 * @param valor
	 * @param cuenta
	 * @param documIngreso
	 * @param documAnulacion
	 * @param centroCosto
	 * @param nit
	 * @param activo
	 * @param institucion
	 * @return
	 */
	public HashMap busquedaConceptosTesoreria(
			Connection con,
			String codigo,
			String descripcion,
			int tipo,
			String valor,
			String cuenta,
			int documIngreso,
			int documAnulacion,
			int centroCosto,
			int nit,
			String activo,
			int institucion);
	
	/**
	 * Método implementado para verificar que el concepto de ingreso
	 * tesorería no se esté utilizando en otras funcionalidades
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public boolean revisarUsoConcepto(Connection con,String codigo,int institucion);
	
	/**
	 * Método usado para la búsqueda avanzada de los registros vinculados
	 * con el ingreso/modificación de los conceptos de ingreso tesorería
	 * @param con
	 * @param codigo
	 * @param descripcion
	 * @param tipo
	 * @param valor
	 * @param cuenta
	 * @param documIngreso
	 * @param documAnulacion
	 * @param centroCosto
	 * @param nit
	 * @param activo
	 * @param institucion
	 * @return
	 */
	public HashMap busquedaConceptosTesoreria2(
			Connection con,
			String codigo,
			String descripcion,
			int tipo,
			String valor,
			String cuenta,
			int documIngreso,
			int documAnulacion,
			int centroCosto,
			int nit,
			String activo,
			int institucion);
	/**
	 * Método que carga los tipos de pagos
	 * @param con
	 * @return
	 */
	public Collection cargarTiposPagos(Connection con);
	
	/**
	 * Método que carga los tipos de documentos de contabilidad
	 * @param con
	 * @param institucion
	 * @return
	 */
	public Collection cargarTiposDocContabilidad(Connection con,int institucion);
	
	/**
	 * Método que carga los centros de costo
	 * @param con
	 * @param institucion
	 * @return
	 */
	public Collection cargarCentrosCosto(Connection con,int institucion);
	
	/**
	 * Método que carga los terceros
	 * @param con
	 * @param institucion
	 * @return
	 */
	public Collection cargarTerceros(Connection con,int institucion);

}