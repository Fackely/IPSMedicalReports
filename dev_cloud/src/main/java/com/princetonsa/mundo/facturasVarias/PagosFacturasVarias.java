package com.princetonsa.mundo.facturasVarias;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturasVarias.PagosFacturasVariasDao;;

/**
 * Fecha: Abril 2008 
 * @author Mauricio Jaramillo H.
 */

public class PagosFacturasVarias 
{

	/**
     * Constructor de la Clase
     */
    public PagosFacturasVarias()
    {
        this.reset();
        this.init(System.getProperty("TIPOBD"));
    }
	
    /**
	 * DAO de este objeto, para trabajar con la fuente de datos
	 */
	private static PagosFacturasVariasDao aplicacionDao;

	/**
    * Método que limpia este objeto
    */
	public void reset()
	{

	}
	    
	/**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
	    if ( aplicacionDao== null ) 
		{ 
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			aplicacionDao = myFactory.getPagosFacturasVariasDao();
			if( aplicacionDao!= null )
				return true;
		}
		return false;
	}
	
	/**
	 * Metodo que retorna un mapa con el listado de documentos pendientes que pertenecen a deudores
	 * @param con
	 * @param codigoInstitucionInt
	 * @return
	 */
	public HashMap consultarPagosGeneralFacturasVarias(Connection con, int codigoInstitucionInt)
	{
	  return aplicacionDao.consultarPagosGeneralFacturasVarias(con, codigoInstitucionInt);
	}

	/**
	 * Metodo que carga el concepto de un Pago por Facturas Varias
	 * @param con
	 * @param codigoAplicacionPago
	 * @return
	 */
	public HashMap cargarConceptosPagosFacturasVarias(Connection con, int codigoAplicacionPago)
    {
        return aplicacionDao.cargarConceptosPagosFacturasVarias(con,codigoAplicacionPago);
    }
	
	/**
	 * Metodo que retorna los resultados segun la busqueda seleccionada en el listado de documentos pendientes para los deudores
	 * @param con
	 * @param vo
	 * @return
	 */
	public HashMap busquedaAvanzada(Connection con, HashMap vo)
    {
        return aplicacionDao.busquedaAvanzada(con,vo);
    }
	
	/**
	 * Metodo que guarda los conceptos de aplicacion de pagos de facturas varias
	 * @param con
	 * @param mapa
	 * @return
	 */
	public String guardarConceptosAplicacionPagosFacturasVarias(Connection con, HashMap mapa)
    {
        return aplicacionDao.guardarConceptosAplicacionPagosFacturasVarias(con, mapa);
    }
	
	/**
	 * Metodo que consulta las facturas por el deudor
	 * @param con
	 * @param mapa
	 * @return
	 */
	public HashMap buscarFacturasDeudor(Connection con, HashMap mapa)
    {
        return aplicacionDao.buscarFacturasDeudor(con, mapa);
    }

	/**
	 * Metodo utilizado para realizar la busqueda avanzada de las facturas del deudor
	 * @param con
	 * @param institucion
	 * @param convenio
	 * @param facturaBusquedaAvanzada
	 * @param facturas
	 * @return
	 */
	public HashMap buscarFacturaLLave(Connection con, int institucion, int deudor, String facturaBusquedaAvanzada, String facturas)
    {
        return aplicacionDao.buscarFacturaLLave(con, institucion, deudor, facturaBusquedaAvanzada, facturas);
    }
	
	/**
	 * @param con
	 * @param vo
	 * @return
	 */
	public int guardarAplicacionFacturas(Connection con, HashMap vo)
    {
        return aplicacionDao.guardarAplicacionFacturas(con, vo);
    }
	
	/**
	 * @param con
	 * @param aplicacionPago
	 * @return
	 */
	public HashMap consultarPagosFacturas(Connection con, int aplicacionPago)
    {
        return aplicacionDao.consultarPagosFacturas(con, aplicacionPago);
    }
	
}