package com.princetonsa.dao.postgresql.facturasVarias;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.facturasVarias.PagosFacturasVariasDao;
import com.princetonsa.dao.sqlbase.facturasVarias.SqlBasePagosFacturasVariasDao;

/**
 * Fecha: Abril de 2008
 * @author Mauricio Jaramillo
 */

public class PostgresqlPagosFacturasVariasDao implements PagosFacturasVariasDao 
{

	/** 
     * @see com.princetonsa.dao.AplicacionPagosEmpresaDao#consultarPagosGeneralEmpresa(java.sql.Connection, int)
     */
    public HashMap consultarPagosGeneralFacturasVarias(Connection con, int codigoInstitucionInt)
    {
        return SqlBasePagosFacturasVariasDao.consultarPagosGeneralFacturasVarias(con, codigoInstitucionInt);
    }
    
    /**
     * @param con
     * @param codigoAplicacionPago
     * @return
     */
    public HashMap cargarConceptosPagosFacturasVarias(Connection con, int codigoAplicacionPago)
    {
        return SqlBasePagosFacturasVariasDao.cargarConceptosPagosFacturasVarias(con, codigoAplicacionPago);
    }
    
    /**
     * 
     */
    public HashMap busquedaAvanzada(Connection con, HashMap vo)
    {
        return SqlBasePagosFacturasVariasDao.busquedaAvanzada(con, vo);
    }
 
    /**
     * @param con
     * @param mapa
     * @return
     */
    public String guardarConceptosAplicacionPagosFacturasVarias(Connection con, HashMap mapa)
    {
        return SqlBasePagosFacturasVariasDao.guardarConceptosAplicacionPagosFacturasVarias(con, mapa);
    }
    
    /**
     * 
     */
    public HashMap buscarFacturasDeudor(Connection con, HashMap mapa)
    {
        return SqlBasePagosFacturasVariasDao.buscarFacturasDeudor(con, mapa);
    }
    
    /**
     * 
     */
    public HashMap buscarFacturaLLave(Connection con, int institucion, int deudor, String facturaBusquedaAvanzada, String facturas)
    {
        return SqlBasePagosFacturasVariasDao.buscarFacturaLLave(con, institucion, deudor, facturaBusquedaAvanzada, facturas);
    }
    
    /**
     * @param con
     * @param vo
     * @return
     */
    public int guardarAplicacionFacturas(Connection con, HashMap vo)
    {
        return SqlBasePagosFacturasVariasDao.guardarAplicacionFacturas(con,vo);
    }
    
    /**
     * @param con
     * @param aplicacionPago
     * @return
     */
    public HashMap consultarPagosFacturas(Connection con, int aplicacionPago)
    {
        return SqlBasePagosFacturasVariasDao.consultarPagosFacturas(con,aplicacionPago);
    }
    
}