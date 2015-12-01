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
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.AplicacionPagosEmpresaDao;
import com.princetonsa.dao.sqlbase.SqlBaseAplicacionPagosEmpresaDao;

/**
 * @version 1.0, 3/11/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public class PostgresqlAplicacionPagosEmpresaDao implements AplicacionPagosEmpresaDao
{
    /** 
     * Metodo que retorna un mapa con el listado de las formas de pagos general empresa
     * @see com.princetonsa.dao.AplicacionPagosEmpresaDao#consultarPagosGeneralEmpresa(java.sql.Connection, int)
     */
    public HashMap consultarPagosGeneralEmpresa(Connection con, int codigoInstitucionInt)
    {
        return SqlBaseAplicacionPagosEmpresaDao.consultarPagosGeneralEmpresa(con,codigoInstitucionInt);
    }
    
    
    /**
     * @param con
     * @param vo
     * @return
     */
    public HashMap busquedaAvanzada(Connection con, HashMap vo)
    {
        return SqlBaseAplicacionPagosEmpresaDao.busquedaAvanzada(con,vo);
    }
    

    
    /**
     * @param con
     * @param codigoAplicacionPago
     * @return
     */
    public HashMap cargarConceptosPagos(Connection con, int codigoAplicacionPago)
    {
        return SqlBaseAplicacionPagosEmpresaDao.cargarConceptosPagos(con,codigoAplicacionPago);
    }
    

    
    /**
     * @param con
     * @param mapa
     * @return
     */
    public int guardarConceptosAplicacionPagos(Connection con, HashMap mapa)
    {
        String seq="nextval('seq_apli_pagos_empresa')";
        return SqlBaseAplicacionPagosEmpresaDao.guardarConceptosAplicacionPagos(con,mapa,seq);
    }
    

    
    /**
     * @param con
     * @param mapa
     * @return
     */
    public int anularAplicacionPago(Connection con, HashMap mapa)
    {
        return SqlBaseAplicacionPagosEmpresaDao.anularAplicacionPago(con,mapa);
    }
    
    
    /**
     * @param con
     * @param codAplicacionPago
     * @return
     */
    public HashMap consultarPagosCuentaCobro(Connection con, Integer codAplicacionPago)
    {
        return SqlBaseAplicacionPagosEmpresaDao.consultarPagosCuentaCobro(con,codAplicacionPago);
    }
    
    /**
     * @param con
     * @param mapa
     * @return
     */
    public HashMap buscarCuentasCobroConvenio(Connection con, HashMap mapa)
    {
        return SqlBaseAplicacionPagosEmpresaDao.buscarCuentasCobroConvenio(con,mapa);
    }
    


    
    /**
     * @param con
     * @param institucion
     * @param cxcBusAvanzada
     * @return
     */
    public HashMap buscarCuentasCobroLLave(Connection con, int institucion, int convenio, String cxcBusAvanzada,String cxc)
    {
        return SqlBaseAplicacionPagosEmpresaDao.buscarCuentasCobroLLave(con,institucion,convenio,cxcBusAvanzada,cxc);
    }
    
    
    /**
     * @param con
     * @param vo
     * @return
     */
    public int guardarAplicacionCXC(Connection con, HashMap vo)
    {
        return SqlBaseAplicacionPagosEmpresaDao.guardarAplicacionCXC(con,vo);
    }
    
    
    /**
     * @param con
     * @param aplicacionPago
     * @param cxc
     * @return
     */
    public HashMap consultarPagosFacturas(Connection con, int aplicacionPago, double cxc)
    {
        return SqlBaseAplicacionPagosEmpresaDao.consultarPagosFacturas(con,aplicacionPago,cxc);
    }
    
    
    /**
     * @param con
     * @param aplicacionPago
     * @param cxc
     * @return
     */
    public HashMap consultarPagosFacturas(Connection con, int aplicacionPago)
    {
        return SqlBaseAplicacionPagosEmpresaDao.consultarPagosFacturas(con,aplicacionPago);
    }
    
    /**
     * @param con
     * @param vo
     * @return
     */
    public int guardarAplicacionPagosFacturas(Connection con, HashMap vo)
    {
        return SqlBaseAplicacionPagosEmpresaDao.guardarAplicacionPagosFacturas(con,vo);
    }
    
    
    /**
     * @param con
     * @param mapa
     * @return
     */
    public HashMap buscarFacturasConvenio(Connection con, HashMap mapa)
    {
        return SqlBaseAplicacionPagosEmpresaDao.buscarFacturasConvenio(con,mapa);
    }
    

    
    /**
     * @param con
     * @param institucion
     * @param convenio
     * @param facturaBusquedaAvanzada
     * @param facturas
     * @return
     */
    public HashMap buscarFacturaLLave(Connection con, int institucion, int convenio, String facturaBusquedaAvanzada, String facturas)
    {
        return SqlBaseAplicacionPagosEmpresaDao.buscarFacturaLLave(con,institucion,convenio,facturaBusquedaAvanzada,facturas);
    }
    

    
    /**
     * @param con
     * @param vo
     * @return
     */
    public int guardarAplicacionFacturasCasoFacturas(Connection con, HashMap vo)
    {
        return SqlBaseAplicacionPagosEmpresaDao.guardarAplicacionFacturasCasoFacturas(con,vo);
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
        return SqlBaseAplicacionPagosEmpresaDao.buscarFacturaLLave(con,institucion,convenio,cxc,facturaBusquedaAvanzada,facturas);
    }
}
