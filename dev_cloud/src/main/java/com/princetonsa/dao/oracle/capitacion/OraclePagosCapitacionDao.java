/*
 * Creado en Jul 6, 2006
 * Andr�s Mauricio Ruiz V�lez
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
	 * M�todo que consulta los documentos pendientes de aplicar aprobar pagos 
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
	 * M�todo que realiza la b�squeda avanzada de los documentos de pago
	 * de acuerdo a los par�metros de b�squeda
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
	 * M�todo que consulta los conceptos de pago para la aplicaci�n del pago del documento 
	 * @param con
	 * @param codigoAplicacionPago
	 * @return
	 */
	public HashMap consultarConceptosPago(Connection con, int codigoAplicacionPago)
	{
		return SqlBasePagosCapitacionDao.consultarConceptosPago (con, codigoAplicacionPago);
	}
	
	/**
	 * M�todo que guarda/elimina los concetos de pago y actualiza/inserta la aplicaci�n de pagos
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
	 *  M�todo que realiza la anulaci�n de la aplicaci�n del pago
	 * @param con
	 * @param mapa
	 * @return
	 */
	public int anularAplicacionPago(Connection con, HashMap mapa)
	{
		return SqlBasePagosCapitacionDao.anularAplicacionPago (con, mapa);
	}
	
	/**
	 * M�todo que consulta para la aplicaci�n actual los posibles pagos
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
	 * M�todo que consulta la(s) cuentas de cobro del convenio capitado, que est�n radicadas y
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
	 * M�todo que guarda las cuentas de cobro agregadas al pago de la aplicaci�n
     * de capitaci�n
	 * @param con
	 * @param mapaPagosCXC
	 * @return
	 */
	public int guardarAplicacionPagosCXC(Connection con, HashMap mapaPagosCXC)
	{
		return SqlBasePagosCapitacionDao.guardarAplicacionPagosCXC (con, mapaPagosCXC);
	}
	
	/**
	 * M�todo que realiza la b�squeda de las aplicaciones de pago de capitaci�n pendientes
	 * para realizarles la aprobaci�n
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
	 *  M�todo que calcula el nuevo saldo del documento 
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
	 * M�todo que realiza el proceso de aprobaci�n del pago
	 * @param con
	 * @param mapa
	 * @return
	 */
	public int guardarAprobacionAplicacionPago(Connection con, HashMap mapa)
	{
		return SqlBasePagosCapitacionDao.guardarAprobacionAplicacionPago (con, mapa);
	}
	
	/**
	 * M�todo que consulta el detalle de pagos CxC  de la aplicaci�n de pagos, 
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
	 * M�todo que realiza la b�squeda de los pagos de acuerdo a los par�metros
	 * de b�squeda de la consulta
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
	 * M�todo que consulta los conceptos de pago del documento de pago
	 * que est�n en estado aprobado, para mostrar en el listado de conceptos pero
	 * s�lo ser�n de consulta
	 * @param con
	 * @param codigoDocumentoPago
	 * @return
	 */
	public HashMap consultarConceptosPagoAprobadosDoc(Connection con, int codigoDocumentoPago)
	{
		return SqlBasePagosCapitacionDao.consultarConceptosPagoAprobadosDoc (con, codigoDocumentoPago);
	}

}
