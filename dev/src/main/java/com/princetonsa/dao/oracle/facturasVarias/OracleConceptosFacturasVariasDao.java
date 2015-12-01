package com.princetonsa.dao.oracle.facturasVarias;

/**
 * @author Juan Sebastian Castaño C.
 * 
 * Clase que maneja los métodos propìos de Oracle para el acceso a la fuente
 * de datos en la funcionalidad CONCEPTOS FACTURAS VARIAS 
 */

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.facturasVarias.ConceptosFacturasVariasDao;
import com.princetonsa.dao.sqlbase.facturasVarias.SqlBaseConceptosFacturasVariasDao;

public class OracleConceptosFacturasVariasDao implements
		ConceptosFacturasVariasDao {
	
	private static Logger logger = Logger.getLogger(OracleConceptosFacturasVariasDao.class);	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 * 
	 * Metodo para consultar facturas varias
	 * 
	 */
	
	public HashMap<String, Object> cargar(Connection con, int institucion,String descripciontercero)
	{
		return SqlBaseConceptosFacturasVariasDao.cargar(con, institucion,descripciontercero);
	}
	
	/**
	 * Metodo para buscar un registro de concepto de facturas varias
	 */
	public HashMap<String, Object> buscConceptFacVarByConsec (Connection con, int consecutivo)
	{
		return SqlBaseConceptosFacturasVariasDao.buscarConceptFactVarByConsec(con, consecutivo);
	}
	
	
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
			String  codigo, String descripcion,int cuenta_contable_debito,String activo, int institucion, String usuarioModifica, int cuenta_contable_credito,String tipoconcepto, String tercero, int ajusteDebitoVigAnterior, int ajusteCreditoVigAnterior, int cuentaContableCreditoVigenciaAnterior, int  cuentaIngresoVigencia)
	{
		
		return SqlBaseConceptosFacturasVariasDao.modificarConceptoFacturasVarias (con,consecutivo, 
				codigo, descripcion,cuenta_contable_debito,activo, institucion, usuarioModifica, cuenta_contable_credito,tipoconcepto,tercero, ajusteDebitoVigAnterior, ajusteCreditoVigAnterior, cuentaContableCreditoVigenciaAnterior , cuentaIngresoVigencia);
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @return
	 * 
	 * Metodo para eliminar conceptos de facturas varias
	 */
	
	public boolean eliminarConceptoFacturasVarias (Connection con, int consecutivo)
	{
		return SqlBaseConceptosFacturasVariasDao.eliminarConceptoFacturasVarias (con,consecutivo);
		
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param descripcion
	 * @param cuenta_contable
	 * @param activo
	 * @param institucion
	 * @param Secuencia
	 * @return
	 * Metodo para crear un nuevo registro de conceptos de facturas varias
	 */
	public boolean insertarConceptoFactura (Connection con, String codigo, String descripcion,
			int cuenta_contable_debito,String activo, int institucion, String usuarioModifica,int cuenta_contable_credito,String tipoconcepto,String tercero, int ajusteDebitoVigAnterior, int ajusteCreditoVigAnterior, int codigoCuentaContableCreditoVigenciaAnterior, int cuentaIngresoVigencia)
	{
			String Seq_concept_fac_varias = "seq_conceptos_fac_varias.nextval";
		return SqlBaseConceptosFacturasVariasDao.insertarConceptoFactura (con,codigo, descripcion,
				cuenta_contable_debito,activo, institucion,usuarioModifica,cuenta_contable_credito, Seq_concept_fac_varias,tipoconcepto,tercero, ajusteDebitoVigAnterior, ajusteCreditoVigAnterior, codigoCuentaContableCreditoVigenciaAnterior, cuentaIngresoVigencia);
		
	}

	
}
