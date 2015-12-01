package com.princetonsa.dao.facturasVarias;

import java.sql.Connection;
import java.util.HashMap;

/**
 * 
 * @author Juan Sebastian Castaño C.
 *
 *Interfaz utilizada para gestionar los métodos DAO de la funcionalidad
 *FACTURAS VARIAS
 */

public interface ConceptosFacturasVariasDao {
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 * Metodo para cargar los conceptos facturas varias
	 */
	public HashMap<String, Object> cargar (Connection con, int institucion,String descripciontercero);
	
	/**
	 * Funcion para buscar un concepto de factura varia por consecutivo
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public HashMap<String, Object> buscConceptFacVarByConsec (Connection con, int consecutivo);
	
	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @return
	 * 
	 * Metodo para eliminar conceptos de facturas varias
	 */
	
	public boolean eliminarConceptoFacturasVarias (Connection con, int consecutivo);
	
	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @param codigo
	 * @param descripcion
	 * @param cuenta_contable
	 * @param activo
	 * @param institucion
	 * @return
	 * 
	 * Metodo para modificar conceptos de facturas varias
	 */
	
	public boolean modificarConceptoFacturasVarias (Connection con, int consecutivo, 
			String  codigo, String descripcion,int cuenta_contable_debito,String activo, int institucion, String usuarioModifica, int cuenta_contable_credito,String tipoconcepto, String tercero, int ajusteDebitoVigAnterior, int ajusteCreditoVigAnterior, int cuentaContableCreditoVigenciaAnterior, int cuentaIngresoVigencia);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param descripcion
	 * @param cuenta_contable
	 * @param activo
	 * @param institucion
	 * @param ajusteCreditoVigAnterior 
	 * @param ajusteDebitoVigAnterior 
	 * @return
	 * Metodo para crear un nuevo registro de conceptos de facturas varias
	 */
	
	public boolean insertarConceptoFactura (Connection con,String codigo, String descripcion,
			int cuenta_contable_debito,String activo, int institucion, String usuarioModifica, int cuenta_contable_credito,String tipoconcepto, String tercero, int ajusteDebitoVigAnterior, int ajusteCreditoVigAnterior, int cuentaContableCreditoVigenciaAnteior ,  int cuentaIngresoVigencia);

}
