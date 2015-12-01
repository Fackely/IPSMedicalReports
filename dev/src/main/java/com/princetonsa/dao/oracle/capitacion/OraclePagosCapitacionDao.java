/*
 * Creado en Jul 6, 2006
 * Andrés Mauricio Ruiz Vélez
 * Princeton S.A (Parquesoft - Manizales)
 */
package com.princetonsa.dao.oracle.capitacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.capitacion.PagosCapitacionDao;
import com.princetonsa.dao.sqlbase.capitacion.SqlBasePagosCapitacionDao;

public class OraclePagosCapitacionDao implements PagosCapitacionDao
{
	/**
	 * Método que consulta los documentos pendientes de aplicar aprobar pagos 
     * de convenios capitados
     * @param con
     * @param codigoInstitucion
     * @return HashMap
	 */
	public HashMap consultarDocumentosPagos(Connection con, int codigoInstitucion)
	{
		return SqlBasePagosCapitacionDao.consultarDocumentosPagos (con, codigoInstitucion);
	}
	
	/**
	 * Método que realiza la búsqueda avanzada de los documentos de pago
	 * de acuerdo a los parámetros de búsqueda
	 * @param con
	 * @param tipoDocBusqueda
	 * @param documentoBusqueda
	 * @param fechaDocBusqueda
	 * @param convenioBusqueda
	 * @param codigoInstitucion
	 * @return
	 */
	public HashMap busquedaAvanzadaDocumentos(Connection con, int tipoDocBusqueda, String documentoBusqueda, String fechaDocBusqueda, int convenioBusqueda, int codigoInstitucion)
	{
		return SqlBasePagosCapitacionDao.busquedaAvanzadaDocumentos (con, tipoDocBusqueda, documentoBusqueda, fechaDocBusqueda, convenioBusqueda, codigoInstitucion);
	}
	
	/**
	 * Método que consulta los conceptos de pago para la aplicación del pago del documento 
	 * @param con
	 * @param codigoAplicacionPago
	 * @return
	 */
	public HashMap consultarConceptosPago(Connection con, int codigoAplicacionPago)
	{
		return SqlBasePagosCapitacionDao.consultarConceptosPago (con, codigoAplicacionPago);
	}
	
	/**
	 * Método que guarda/elimina los concetos de pago y actualiza/inserta la aplicación de pagos
	 * @param con
	 * @param mapa
	 * @return
	 */
	public int guardarConceptosAplicacionPagos(Connection con, HashMap mapa)
	{
		String seq="capitacion.seq_apli_pagos_capitacion.nextval";
		return SqlBasePagosCapitacionDao.guardarConceptosAplicacionPagos (con, mapa, seq);
	}
	
	/**
	 *  Método que realiza la anulación de la aplicación del pago
	 * @param con
	 * @param mapa
	 * @return
	 */
	public int anularAplicacionPago(Connection con, HashMap mapa)
	{
		return SqlBasePagosCapitacionDao.anularAplicacionPago (con, mapa);
	}
	
	/**
	 * Método que consulta para la aplicación actual los posibles pagos
	 * por cuentas de cobro
	 * @param con
	 * @param codAplicacionPago
	 * @return
	 */
	public HashMap consultarPagosCuentaCobro(Connection con, int codAplicacionPago)
	{
		return SqlBasePagosCapitacionDao.consultarPagosCuentaCobro (con, codAplicacionPago);
	}
	
	/**
	 * Método que consulta la(s) cuentas de cobro del convenio capitado, que están radicadas y
	 * que la cuenta de cobro tenga saldo mayor a cero
	 * @param con
	 * @param mapa
	 * @return
	 */
	public HashMap buscarCuentasCobroConvenio(Connection con, HashMap mapa)
	{
		return SqlBasePagosCapitacionDao.buscarCuentasCobroConvenio (con, mapa);
	}
	
	/**
	 * Método que guarda las cuentas de cobro agregadas al pago de la aplicación
     * de capitación
	 * @param con
	 * @param mapaPagosCXC
	 * @return
	 */
	public int guardarAplicacionPagosCXC(Connection con, HashMap mapaPagosCXC)
	{
		return SqlBasePagosCapitacionDao.guardarAplicacionPagosCXC (con, mapaPagosCXC);
	}
	
	/**
	 * Método que realiza la búsqueda de las aplicaciones de pago de capitación pendientes
	 * para realizarles la aprobación
	 * @param con
	 * @param tipoDocBusqueda
	 * @param documentoBusqueda
	 * @param consecutivoPagoBusqueda
	 * @param convenioBusqueda
	 * @param institucion
	 * @return
	 */
	public HashMap busquedaPagosParaAprobar(Connection con, int tipoDocBusqueda, String documentoBusqueda, String consecutivoPagoBusqueda, int convenioBusqueda, int institucion)
	{
		return SqlBasePagosCapitacionDao.busquedaPagosParaAprobar (con, tipoDocBusqueda, documentoBusqueda, consecutivoPagoBusqueda, convenioBusqueda, institucion);
	}
	
	/**
	 *  Método que calcula el nuevo saldo del documento 
	 * NuevoSaldo= Saldo anterior + conceptos tipo mayor valor ? conceptos tipo menor valor ? valor pago 
	 * @param con
	 * @param valorInicialDocumento
	 * @param codigoPago
	 * @param codigoAplicacion
	 * @return
	 */
	public double calcularNuevoSaldoDocumento(Connection con, float valorInicialDocumento, int codigoPago, int codigoAplicacion)
	{
		return SqlBasePagosCapitacionDao.calcularNuevoSaldoDocumento(con, valorInicialDocumento, codigoPago, codigoAplicacion);
	}
	
	/**
	 * Método que realiza el proceso de aprobación del pago
	 * @param con
	 * @param mapa
	 * @return
	 */
	public int guardarAprobacionAplicacionPago(Connection con, HashMap mapa)
	{
		return SqlBasePagosCapitacionDao.guardarAprobacionAplicacionPago (con, mapa);
	}
	
	/**
	 * Método que consulta el detalle de pagos CxC  de la aplicación de pagos, 
	 * las cuentas de cobro agregadas al pago
	 * @param con
	 * @param codigoAplicacionPago
	 * @return
	 */
	public HashMap consultarPagosCxCAplicacion(Connection con, int codigoAplicacionPago)
	{
		return SqlBasePagosCapitacionDao.consultarPagosCxCAplicacion(con, codigoAplicacionPago);
	}
	
	/**
	 * Método que realiza la búsqueda de los pagos de acuerdo a los parámetros
	 * de búsqueda de la consulta
	 * @param con
	 * @param tipoDocBusqueda
	 * @param documentoBusqueda
	 * @param consecutivoPagoBusqueda
	 * @param estadoPagoBusqueda
	 * @param convenioBusqueda
	 * @param institucion
	 * @return
	 */
	public HashMap busquedaPagosConsulta(Connection con, int tipoDocBusqueda, String documentoBusqueda, String consecutivoPagoBusqueda, int estadoPagoBusqueda, int convenioBusqueda, int institucion)
	{
		return SqlBasePagosCapitacionDao.busquedaPagosConsulta (con, tipoDocBusqueda, documentoBusqueda, consecutivoPagoBusqueda, estadoPagoBusqueda, convenioBusqueda, institucion);
	}
	
	/**
	 * Método que consulta los conceptos de pago del documento de pago
	 * que están en estado aprobado, para mostrar en el listado de conceptos pero
	 * sólo serán de consulta
	 * @param con
	 * @param codigoDocumentoPago
	 * @return
	 */
	public HashMap consultarConceptosPagoAprobadosDoc(Connection con, int codigoDocumentoPago)
	{
		return SqlBasePagosCapitacionDao.consultarConceptosPagoAprobadosDoc (con, codigoDocumentoPago);
	}

}
