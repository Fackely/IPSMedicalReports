/*
 * Created on 3/11/2005
 * 
 * @author <a href="mailto:artotor@hotmail.com">Jorge Armando Osorio Velásquez</a>
 * 
 * Copyright Princeton S.A. &copy;&reg; 2005. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 *
 */
package com.princetonsa.mundo.cartera;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.AplicacionPagosEmpresaDao;
import com.princetonsa.dao.DaoFactory;

/**
 * @version 1.0, 3/11/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public class AplicacionPagosEmpresa
{ 
    /**
     * Constructor de la Clase
     */
    public AplicacionPagosEmpresa()
    {
        this.reset();
        this.init(System.getProperty("TIPOBD"));
    }
    /**
	 * DAO de este objeto, para trabajar con
	 * la fuente de datos
	 */
   private static AplicacionPagosEmpresaDao aplicacionDao;
   
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
			aplicacionDao= myFactory.getAplicacionPagosEmpresaDao();
			if( aplicacionDao!= null )
				return true;
		}
		return false;
	}


	/**
	 * metodo que retorana un mapa con el listado de los diferentes pagos general empresa
	 * @param con
	 * @param codigoInstitucionInt
	 * @return
	 */
	public HashMap consultarPagosGeneralEmpresa(Connection con, int codigoInstitucionInt)
	{
	  return aplicacionDao.consultarPagosGeneralEmpresa(con,codigoInstitucionInt);
	}

    
    /**
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
     * @param codigoAplicacionPago
     * @return
     */
    public HashMap cargarConceptosPagos(Connection con, int codigoAplicacionPago)
    {
        return aplicacionDao.cargarConceptosPagos(con,codigoAplicacionPago);
    }

    
    /**
     * @param con
     * @param mapa
     */
    public int guardarConceptosAplicacionPagos(Connection con, HashMap mapa)
    {
        return aplicacionDao.guardarConceptosAplicacionPagos(con,mapa);
    }

    
    /**
     * @param con
     * @param mapa
     */
    public int anularAplicacionPago(Connection con, HashMap mapa)
    {
        return aplicacionDao.anularAplicacionPago(con,mapa);
    }

    
    /**
     * @param con
     * @param integer
     * @return
     */
    public HashMap consultarPagosCuentaCobro(Connection con, Integer codAplicacionPago)
    {
        return aplicacionDao.consultarPagosCuentaCobro(con,codAplicacionPago);
    }

    
    /**
     * @param con
     * @param mapa
     * @return
     */
    public HashMap buscarCuentasCobroConvenio(Connection con, HashMap mapa)
    {
        return aplicacionDao.buscarCuentasCobroConvenio(con,mapa);
    }

    
    /**
     * @param con
     * @param institucion
     * @param convenio
     * @param cxcBusAvanzada
     * @param cxc
     * @return
     */
    public HashMap buscarCuentasCobroLLave(Connection con, int institucion, int convenio, String cxcBusAvanzada, String cxc)
    {
        return aplicacionDao.buscarCuentasCobroLLave(con,institucion,convenio,cxcBusAvanzada,cxc);
    }

    
    /**
     * @param con
     * @param pagosCXC
     */
    public int guardarAplicacionCXC(Connection con, HashMap vo)
    {
        return aplicacionDao.guardarAplicacionCXC(con,vo);
    }

    
    /**
     * @param con
     * @param aplicacionPago
     * @param cxc
     * @return
     */
    public HashMap consultarPagosFacturas(Connection con, int aplicacionPago, double cxc)
    {
        return aplicacionDao.consultarPagosFacturas(con,aplicacionPago,cxc);
    }

    
    /**
     * @param con
     * @param mapaFacturasCXC
     */
    public int guardarAplicacionPagosFacturas(Connection con, HashMap vo)
    {
        return aplicacionDao.guardarAplicacionPagosFacturas(con,vo);
    }

    
    /**
     * @param con
     * @param i
     * @return
     */
    public HashMap consultarPagosFacturas(Connection con, int aplicacionPago)
    {
        return aplicacionDao.consultarPagosFacturas(con,aplicacionPago);
    }

    
    /**
     * @param con
     * @param mapa
     * @return
     */
    public HashMap buscarFacturasConvenio(Connection con, HashMap mapa)
    {
        return aplicacionDao.buscarFacturasConvenio(con,mapa);
    }

    
    /**
     * @param con
     * @param institucion
     * @param i
     * @param facturaBusquedaAvanzada
     * @param facturas
     * @return
     */
    public HashMap buscarFacturaLLave(Connection con, int institucion, int convenio, String facturaBusquedaAvanzada, String facturas)
    {
        return aplicacionDao.buscarFacturaLLave(con,institucion,convenio,facturaBusquedaAvanzada,facturas);
    }

    
    /**
     * @param con
     * @param pagosFactura
     */
    public int guardarAplicacionFacturasCasoFacturas(Connection con, HashMap vo)
    {
        return aplicacionDao.guardarAplicacionFacturasCasoFacturas(con,vo);
    }

    
    /**
     * @param con
     * @param institucion
     * @param convenio
     * @param cxc
     * @param facturaBusquedaAvanzada
     * @param facturas
     * @return
     */
    public HashMap buscarFacturaLLave(Connection con, int institucion, int convenio, double cxc, String facturaBusquedaAvanzada, String facturas)
    {
        return aplicacionDao.buscarFacturaLLave(con,institucion,convenio,cxc,facturaBusquedaAvanzada,facturas);
    }
}
