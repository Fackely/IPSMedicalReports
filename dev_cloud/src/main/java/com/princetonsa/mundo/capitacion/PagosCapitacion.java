/*
 * Creado en Jul 6, 2006
 * Andrés Mauricio Ruiz Vélez
 * Princeton S.A (Parquesoft - Manizales)
 */
package com.princetonsa.mundo.capitacion;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.capitacion.PagosCapitacionDao;

/**
 * Mundo de Aplicación de Pagos de Capitación
 * @author Andrés Mauricio Ruiz Vélez
 * Princeton S.A. (Parquesoft-Manizales) 
 * @version Jul 6, 2006
 */
public class PagosCapitacion
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(PagosCapitacion.class);
	
	/**
	 * Interfaz para acceder a la fuente de datos
	 */
	private PagosCapitacionDao  pagosCapitacionDao = null;
	
//	---------------------------------------------------DECLARACIÓN DE LOS ATRIBUTOS-----------------------------------------------------------//
	
//	---------------------------------------------------FIN DECLARACIÓN DE LOS ATRIBUTOS-----------------------------------------------------------//
	
	/**
	 * Constructor de la clase, inicializa en vacío todos los atributos
	 */
	public PagosCapitacion ()
	{
		reset();
		this.init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * Este método inicializa los atributos de la clase con valores vacíos
	 */
	public void reset()
	{
		
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD)
	{
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);

		if (myFactory != null)
		{
			pagosCapitacionDao = myFactory.getPagosCapitacionDao();
			wasInited = (pagosCapitacionDao != null);
		}
		return wasInited;
	}

//-----------------------------------------------------METODOS --------------------------------------------------------------------------//
	/**
	 * Método que consulta los documentos pendientes de aplicar aprobar pagos 
     * de convenios capitados
     * @param con
     * @param codigoInstitucion
     * @return HashMap
	 */
	public HashMap consultarDocumentosPagos(Connection con, int codigoInstitucion)
	{
		return pagosCapitacionDao.consultarDocumentosPagos (con, codigoInstitucion);
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
		return pagosCapitacionDao.busquedaAvanzadaDocumentos (con, tipoDocBusqueda, documentoBusqueda, fechaDocBusqueda, convenioBusqueda, codigoInstitucion);
	}

	/**
	 * Método que consulta los conceptos de pago para la aplicación del pago del documento 
	 * @param con
	 * @param codigoAplicacionPago
	 * @return
	 */
	public HashMap consultarConceptosPago(Connection con, int codigoAplicacionPago)
	{
		return pagosCapitacionDao.consultarConceptosPago(con, codigoAplicacionPago);
	}

	/**
	 * Método que guarda/elimina los concetos de pago y actualiza/inserta la aplicación de pagos
	 * @param con
	 * @param mapa
	 */
	public int guardarConceptosAplicacionPagos(Connection con, HashMap mapa)
	{
		return pagosCapitacionDao.guardarConceptosAplicacionPagos (con, mapa);
	}

	/**
	 * Método que realiza la anulación de la aplicación del pago
	 * @param con
	 * @param mapa
	 */
	public int anularAplicacionPago(Connection con, HashMap mapa)
	{
		return pagosCapitacionDao.anularAplicacionPago(con, mapa);
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
		 return pagosCapitacionDao.consultarPagosCuentaCobro (con, codAplicacionPago);
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
		return pagosCapitacionDao.buscarCuentasCobroConvenio (con, mapa);
	}

	/**
	 * Método que guarda las cuentas de cobro agregadas al pago de la aplicación
     * de capitación
	 * @param con
	 * @param mapaPagosCXC
	 */
	public int guardarAplicacionPagosCXC(Connection con, HashMap mapaPagosCXC)
	{
		return pagosCapitacionDao.guardarAplicacionPagosCXC (con, mapaPagosCXC);
		
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
		return pagosCapitacionDao.busquedaPagosParaAprobar (con, tipoDocBusqueda, documentoBusqueda, consecutivoPagoBusqueda, convenioBusqueda, institucion);
	}

	/**
	 * Método que calcula el nuevo saldo del documento 
	 * NuevoSaldo= Saldo anterior + conceptos tipo mayor valor ? conceptos tipo menor valor ? valor pago 
	 * @param con
	 * @param valorInicialDocumento
	 * @param codigoPago
	 * @param codigoAplicacion
	 * @return
	 */
	public double calcularNuevoSaldoDocumento(Connection con, float valorInicialDocumento, int codigoPago, int codigoAplicacion)
	{
		return pagosCapitacionDao.calcularNuevoSaldoDocumento (con, valorInicialDocumento, codigoPago, codigoAplicacion);
	}

	/**
	 * Método que realiza el proceso de aprobación del pago
	 * @param con
	 * @param mapa
	 */
	public int guardarAprobacionAplicacionPago(Connection con, HashMap mapa)
	{
		return pagosCapitacionDao.guardarAprobacionAplicacionPago (con, mapa);
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
		return pagosCapitacionDao.consultarPagosCxCAplicacion(con, codigoAplicacionPago);
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
		return pagosCapitacionDao.busquedaPagosConsulta(con, tipoDocBusqueda, documentoBusqueda, consecutivoPagoBusqueda, estadoPagoBusqueda, convenioBusqueda, institucion);
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
		return pagosCapitacionDao.consultarConceptosPagoAprobadosDoc(con, codigoDocumentoPago);
	}
	
}
