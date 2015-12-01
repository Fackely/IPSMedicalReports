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
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @version 1.0, 3/11/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public interface AplicacionPagosEmpresaDao
{

    
    /**
     * @param con
     * @param codigoInstitucionInt
     * @return
     */
    public abstract HashMap consultarPagosGeneralEmpresa(Connection con, int codigoInstitucionInt);

    
    /**
     * @param con
     * @param vo
     * @return
     */
    public abstract HashMap busquedaAvanzada(Connection con, HashMap vo);


    
    /**
     * @param con
     * @param codigoAplicacionPago
     * @return
     */
    public abstract HashMap cargarConceptosPagos(Connection con, int codigoAplicacionPago);


    
    /**
     * @param con
     * @param mapa
     * @return
     */
    public abstract int guardarConceptosAplicacionPagos(Connection con, HashMap mapa);


    
    /**
     * @param con
     * @param mapa
     * @return
     */
    public abstract int anularAplicacionPago(Connection con, HashMap mapa);


    
    /**
     * @param con
     * @param codAplicacionPago
     * @return
     */
    public abstract HashMap consultarPagosCuentaCobro(Connection con, Integer codAplicacionPago);


    
    /**
     * @param con
     * @param mapa
     * @return
     */
    public abstract HashMap buscarCuentasCobroConvenio(Connection con, HashMap mapa);


    
    /**
     * @param con
     * @param institucion
     * @param convenio
     * @param cxcBusAvanzada
     * @param cxc
     * @return
     */
    public abstract HashMap buscarCuentasCobroLLave(Connection con, int institucion, int convenio, String cxcBusAvanzada, String cxc);


    
    /**
     * @param con
     * @param vo
     * @return
     */
    public abstract int guardarAplicacionCXC(Connection con, HashMap vo);


    
    /**
     * @param con
     * @param aplicacionPago
     * @param cxc
     * @return
     */
    public abstract HashMap consultarPagosFacturas(Connection con, int aplicacionPago, double cxc);


    
    /**
     * @param con
     * @param vo
     * @return
     */
    public abstract int guardarAplicacionPagosFacturas(Connection con, HashMap vo);


    
    /**
     * @param con
     * @param aplicacionPago
     * @return
     */
    public abstract HashMap consultarPagosFacturas(Connection con, int aplicacionPago);


    
    /**
     * @param con
     * @param mapa
     * @return
     */
    public abstract HashMap buscarFacturasConvenio(Connection con, HashMap mapa);


    
    /**
     * @param con
     * @param institucion
     * @param convenio
     * @param facturaBusquedaAvanzada
     * @param facturas
     * @return
     */
    public abstract HashMap buscarFacturaLLave(Connection con, int institucion, int convenio, String facturaBusquedaAvanzada, String facturas);


    
    /**
     * @param con
     * @param vo
     * @return
     */
    public abstract int guardarAplicacionFacturasCasoFacturas(Connection con, HashMap vo);


    
    /**
     * @param con
     * @param institucion
     * @param convenio
     * @param cxc
     * @param facturaBusquedaAvanzada
     * @param facturas
     * @return
     */
    public abstract HashMap buscarFacturaLLave(Connection con, int institucion, int convenio, double cxc, String facturaBusquedaAvanzada, String facturas);

   

}
