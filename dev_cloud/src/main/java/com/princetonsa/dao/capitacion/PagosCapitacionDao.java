/*
 * Creado en Jul 6, 2006
 * Andrés Mauricio Ruiz Vélez
 * Princeton S.A (Parquesoft - Manizales)
 */
package com.princetonsa.dao.capitacion;

import java.sql.Connection;
import java.util.HashMap;

public interface PagosCapitacionDao
{

	/**
	 * Método que consulta los documentos pendientes de aplicar aprobar pagos 
     * de convenios capitados
     * @param con
     * @param codigoInstitucion
     * @return HashMap
	 */
	public HashMap consultarDocumentosPagos(Connection con, int codigoInstitucion);

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
	public HashMap busquedaAvanzadaDocumentos(Connection con, int tipoDocBusqueda, String documentoBusqueda, String fechaDocBusqueda, int convenioBusqueda, int codigoInstitucion);

	/**
	 * Método que consulta los conceptos de pago para la aplicación del pago del documento 
	 * @param con
	 * @param codigoAplicacionPago
	 * @return
	 */
	public HashMap consultarConceptosPago(Connection con, int codigoAplicacionPago);

	/**
	 * Método que guarda/elimina los concetos de pago y actualiza/inserta la aplicación de pagos
	 * @param con
	 * @param mapa
	 * @return
	 */
	public int guardarConceptosAplicacionPagos(Connection con, HashMap mapa);

	/**
	 *  Método que realiza la anulación de la aplicación del pago
	 * @param con
	 * @param mapa
	 * @return
	 */
	public int anularAplicacionPago(Connection con, HashMap mapa);
	
	/**
	 * Método que consulta para la aplicación actual los posibles pagos
	 * por cuentas de cobro
	 * @param con
	 * @param codAplicacionPago
	 * @return
	 */
	public HashMap consultarPagosCuentaCobro(Connection con, int codAplicacionPago);

	/**
	 * Método que consulta la(s) cuentas de cobro del convenio capitado, que están radicadas y
	 * que la cuenta de cobro tenga saldo mayor a cero
	 * @param con
	 * @param mapa
	 * @return
	 */
	public HashMap buscarCuentasCobroConvenio(Connection con, HashMap mapa);

	/**
	 * Método que guarda las cuentas de cobro agregadas al pago de la aplicación
     * de capitación
	 * @param con
	 * @param mapaPagosCXC
	 * @return
	 */
	public int guardarAplicacionPagosCXC(Connection con, HashMap mapaPagosCXC);

//----------------------------------------------------------------- APROBACIÒN DE PAGOS ------------------------------------------------------------------------------//

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
	public HashMap busquedaPagosParaAprobar(Connection con, int tipoDocBusqueda, String documentoBusqueda, String consecutivoPagoBusqueda, int convenioBusqueda, int institucion);

	/**
	 *  Método que calcula el nuevo saldo del documento 
	 * NuevoSaldo= Saldo anterior + conceptos tipo mayor valor ? conceptos tipo menor valor ? valor pago 
	 * @param con
	 * @param valorInicialDocumento
	 * @param codigoPago
	 * @param codigoAplicacion
	 * @return
	 */
	public double calcularNuevoSaldoDocumento(Connection con, float valorInicialDocumento, int codigoPago, int codigoAplicacion);

	/**
	 * Método que realiza el proceso de aprobación del pago
	 * @param con
	 * @param mapa
	 * @return
	 */
	public int guardarAprobacionAplicacionPago(Connection con, HashMap mapa);

	/**
	 * Método que consulta el detalle de pagos CxC  de la aplicación de pagos, 
	 * las cuentas de cobro agregadas al pago
	 * @param con
	 * @param codigoAplicacionPago
	 * @return
	 */
	public HashMap consultarPagosCxCAplicacion(Connection con, int codigoAplicacionPago);

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
	public HashMap busquedaPagosConsulta(Connection con, int tipoDocBusqueda, String documentoBusqueda, String consecutivoPagoBusqueda, int estadoPagoBusqueda, int convenioBusqueda, int institucion);
	
	/**
	 * Método que consulta los conceptos de pago del documento de pago
	 * que están en estado aprobado, para mostrar en el listado de conceptos pero
	 * sólo serán de consulta
	 * @param con
	 * @param codigoDocumentoPago
	 * @return
	 */
	public HashMap consultarConceptosPagoAprobadosDoc(Connection con, int codigoDocumentoPago);

}
