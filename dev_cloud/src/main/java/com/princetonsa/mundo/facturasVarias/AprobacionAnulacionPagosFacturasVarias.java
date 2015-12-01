package com.princetonsa.mundo.facturasVarias;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturasVarias.AprobacionAnulacionPagosFacturasVariasDao;

/**
 * Fecha: Abril de 2008
 * @author axioma
 * Mauricio Jaramillo H.
 */

public class AprobacionAnulacionPagosFacturasVarias 
{

	/**
     * Constructor de la Clase
     */
    public AprobacionAnulacionPagosFacturasVarias()
    {
        this.reset();
        this.init(System.getProperty("TIPOBD"));
    }
	
    /**
	 * DAO de este objeto, para trabajar con la fuente de datos
	 */
	private static AprobacionAnulacionPagosFacturasVariasDao aplicacionDao;
	
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
			aplicacionDao = myFactory.getAprobacionAnulacionPagosFacturasVariasDao();
			if( aplicacionDao!= null )
				return true;
		}
		return false;
	}
	
	/**
	 * Metodo que retorna un mapa con el listado de aplicaciones de pago de facturas varias
	 * @param con
	 * @param codigoInstitucionInt
	 * @return
	 */
	public HashMap consultarAplicacionesPagosFacturasVarias(Connection con, int codigoInstitucionInt)
	{
	  return aplicacionDao.consultarAplicacionesPagosFacturasVarias(con, codigoInstitucionInt);
	}

	/**
	 * Metodo que retorna un boolean que indica que la aplicacion de pago ha sido aprobado
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean modificarAprobado(Connection con, HashMap vo)
	{
		return aplicacionDao.modificarAprobado(con, vo);
	}

	/**
	 * Metodo que retorna un boolean que indica que las facturas aplicadas han sido aprobadas
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean modificarAprobadoFacturas(Connection con, HashMap vo)
	{
		return aplicacionDao.modificarAprobadoFacturas(con, vo);
	}
	
	/**
	 * Metodo que retorna un boolean que indica que la aplicacion de pago ha sido anulado
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean modificarAnulado(Connection con, HashMap vo)
	{
		return aplicacionDao.modificarAnulado(con, vo);
	}
	
	/**
	 * Metodo que retorna un HashMap con los resultados de la busqueda avanzada
	 * @param con
	 * @param vo
	 * @return
	 */
	public HashMap busquedaAvanzada(Connection con, HashMap vo)
    {
        return aplicacionDao.busquedaAvanzada(con,vo);
    }
	
	/**
	 * @param con
	 * @param codigoDeudor
	 * @param codigoAplicacionPago
	 * @return
	 */
	public HashMap busquedaFacturasDeudores(Connection con, int codigoDeudor, int codigoAplicacionPago)
	{
		return aplicacionDao.busquedaFacturasDeudores(con, codigoDeudor, codigoAplicacionPago);		
	}
	
}