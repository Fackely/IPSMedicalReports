/*
 * Creado en Jul 6, 2006
 * Andr�s Mauricio Ruiz V�lez
 * Princeton S.A (Parquesoft - Manizales)
 */
package com.princetonsa.mundo.capitacion;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.capitacion.PagosCapitacionDao;

/**
 * Mundo de Aplicaci�n de Pagos de Capitaci�n
 * @author Andr�s Mauricio Ruiz V�lez
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
	
//	---------------------------------------------------DECLARACI�N DE LOS ATRIBUTOS-----------------------------------------------------------//
	
//	---------------------------------------------------FIN DECLARACI�N DE LOS ATRIBUTOS-----------------------------------------------------------//
	
	/**
	 * Constructor de la clase, inicializa en vac�o todos los atributos
	 */
	public PagosCapitacion ()
	{
		reset();
		this.init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * Este m�todo inicializa los atributos de la clase con valores vac�os
	 */
	public void reset()
	{
		
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores v�lidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicializaci�n fue exitosa, <code>false</code> si no.
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
	 * M�todo que consulta los documentos pendientes de aplicar aprobar pagos 
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
		return pagosCapitacionDao.busquedaAvanzadaDocumentos (con, tipoDocBusqueda, documentoBusqueda, fechaDocBusqueda, convenioBusqueda, codigoInstitucion);
	}

	/**
	 * M�todo que consulta los conceptos de pago para la aplicaci�n del pago del documento 
	 * @param con
	 * @param codigoAplicacionPago
	 * @return
	 */
	public HashMap consultarConceptosPago(Connection con, int codigoAplicacionPago)
	{
		return pagosCapitacionDao.consultarConceptosPago(con, codigoAplicacionPago);
	}

	/**
	 * M�todo que guarda/elimina los concetos de pago y actualiza/inserta la aplicaci�n de pagos
	 * @param con
	 * @param mapa
	 */
	public int guardarConceptosAplicacionPagos(Connection con, HashMap mapa)
	{
		return pagosCapitacionDao.guardarConceptosAplicacionPagos (con, mapa);
	}

	/**
	 * M�todo que realiza la anulaci�n de la aplicaci�n del pago
	 * @param con
	 * @param mapa
	 */
	public int anularAplicacionPago(Connection con, HashMap mapa)
	{
		return pagosCapitacionDao.anularAplicacionPago(con, mapa);
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
		 return pagosCapitacionDao.consultarPagosCuentaCobro (con, codAplicacionPago);
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
		return pagosCapitacionDao.buscarCuentasCobroConvenio (con, mapa);
	}

	/**
	 * M�todo que guarda las cuentas de cobro agregadas al pago de la aplicaci�n
     * de capitaci�n
	 * @param con
	 * @param mapaPagosCXC
	 */
	public int guardarAplicacionPagosCXC(Connection con, HashMap mapaPagosCXC)
	{
		return pagosCapitacionDao.guardarAplicacionPagosCXC (con, mapaPagosCXC);
		
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
		return pagosCapitacionDao.busquedaPagosParaAprobar (con, tipoDocBusqueda, documentoBusqueda, consecutivoPagoBusqueda, convenioBusqueda, institucion);
	}

	/**
	 * M�todo que calcula el nuevo saldo del documento 
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
	 * M�todo que realiza el proceso de aprobaci�n del pago
	 * @param con
	 * @param mapa
	 */
	public int guardarAprobacionAplicacionPago(Connection con, HashMap mapa)
	{
		return pagosCapitacionDao.guardarAprobacionAplicacionPago (con, mapa);
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
		return pagosCapitacionDao.consultarPagosCxCAplicacion(con, codigoAplicacionPago);
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
		return pagosCapitacionDao.busquedaPagosConsulta(con, tipoDocBusqueda, documentoBusqueda, consecutivoPagoBusqueda, estadoPagoBusqueda, convenioBusqueda, institucion);
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
		return pagosCapitacionDao.consultarConceptosPagoAprobadosDoc(con, codigoDocumentoPago);
	}
	
}
