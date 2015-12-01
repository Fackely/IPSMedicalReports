package com.princetonsa.dao.facturasVarias;

import java.sql.Connection;
import java.util.HashMap;

/**
 * Fecha: Abril de 2008 
 * @author Mauricio Jaramillo
 */

public interface PagosFacturasVariasDao 
{

	/**
     * @param con
     * @param codigoInstitucionInt
     * @return
     */
    public abstract HashMap consultarPagosGeneralFacturasVarias(Connection con, int codigoInstitucionInt);
    
    /**
     * @param con
     * @param codigoAplicacionPago
     * @return
     */
    public abstract HashMap cargarConceptosPagosFacturasVarias(Connection con, int codigoAplicacionPago);
    
    /**
     * @param con
     * @param vo
     * @return
     */
    public abstract HashMap busquedaAvanzada(Connection con, HashMap vo);
    
    /**
     * @param con
     * @param mapa
     * @return
     */
    public abstract String guardarConceptosAplicacionPagosFacturasVarias(Connection con, HashMap mapa);
    
    /**
     * @param con
     * @param mapa
     * @return
     */
    public abstract HashMap buscarFacturasDeudor(Connection con, HashMap mapa);
    
    /**
     * @param con
     * @param institucion
     * @param convenio
     * @param facturaBusquedaAvanzada
     * @param facturas
     * @return
     */
    public abstract HashMap buscarFacturaLLave(Connection con, int institucion, int deudor, String facturaBusquedaAvanzada, String facturas);
    
    /**
     * @param con
     * @param vo
     * @return
     */
    public abstract int guardarAplicacionFacturas(Connection con, HashMap vo);
    
    /**
     * @param con
     * @param aplicacionPago
     * @return
     */
    public abstract HashMap consultarPagosFacturas(Connection con, int aplicacionPago);
    
}
