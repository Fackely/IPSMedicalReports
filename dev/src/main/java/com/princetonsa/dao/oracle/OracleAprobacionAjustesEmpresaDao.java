
/*
 * Creado   23/08/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.util.HashMap;

import com.princetonsa.dao.AprobacionAjustesEmpresaDao;
import com.princetonsa.dao.sqlbase.SqlBaseAprobacionAjustesEmpresaDao;

/**
 * Clase para manejar
 *
 * @version 1.0, 23/08/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class OracleAprobacionAjustesEmpresaDao implements
        AprobacionAjustesEmpresaDao 
{
    /**
	 * Metodo implementado para realizar una busqueda avanzada,
	 * por medio de un HashMap que contiene los objetos con
	 * los respectivos nombres de los campos y los valores
	 * de cada uno de ellos por los cuales se filtra la busqueda.
	 * @param con Connection
	 * @param mapa HashMap
	 * @return ResultSet
	 * @author jarloc
	 * @see com.princetonsa.dao.SqlBase.SqlBaseCierreSaldoInicialCarteraDao#consultaAvanzadaAjustes(java.sql.Connection,HashMap)
	 */
	public ResultSetDecorator consultaAvanzadaAjustes(Connection con,HashMap mapa)
	{
	    return SqlBaseAprobacionAjustesEmpresaDao.consultaAvanzadaAjustes(con,mapa);
	}
	
	/**
	 * Metodo para actualizar los valores credito y debito
	 * de la factura
	 * @param con Connection
	 * @param numFact int, número de la factura
	 * @param vlrAjuste double, valor del ajuste
	 * @param institucion int, código de la institución
	 * @param tipoAjuste int, código del tipo del ajuste.
	 * @return boolean, true si es efectivo
	 * @see com.princetonsa.dao.SqlBase.SqlBaseCierreSaldoInicialCarteraDao#actualizarValoresFacturas(java.sql.Connection,HashMap)
	 */
	public boolean actualizarValoresFacturas(Connection con,
													        int numFact,double vlrAjuste,
													        int institucion,int tipoAjuste)
	{
	    return SqlBaseAprobacionAjustesEmpresaDao.actualizarValoresFacturas(con,numFact,vlrAjuste,institucion,tipoAjuste);
	}
	
	/**
	 * Metodo para actualizar el estado del ajuste
	 * @param con Connection
	 * @param numAjuste double, número del ajuste
	 * @param institucion int, código de la institución
	 * @return boolean, true si es efectivo
	 * @see com.princetonsa.dao.SqlBase.SqlBaseCierreSaldoInicialCarteraDao#actualizarEstadoAjustes(java.sql.Connection,HashMap)
	 */
	public boolean actualizarEstadoAjustes(Connection con,double numAjuste,int institucion)
	{
	    return SqlBaseAprobacionAjustesEmpresaDao.actualizarEstadoAjustes(con,numAjuste,institucion);
	}
	
	/**
	 * metodo para actualizar los valores del detalle de
	 * factura, ajustes debito y credito.
	 * @param con Connection
	 * @param codSolicitud int, código de la solicitud
	 * @param numFactura int, númro de la factura
	 * @param ajuste double, valor del ajuste
	 * @param ajusteMedico double, valor del ajuste medico
	 * @param tipoAjuste int, código del tipo de ajuste
	 * @return boolean, true si es efectivo
	 * @see com.princetonsa.dao.SqlBase.SqlBaseCierreSaldoInicialCarteraDao#actualizarValoresFacturaServicios(java.sql.Connection,HashMap)
	 */
	public boolean actualizarValoresFacturaServicios(Connection con,int codSolicitud,int numFactura,
															        double ajuste,double ajusteMedico,
															        int tipoAjuste, boolean actualizarAjusteMedicoPool)
	{
	    return SqlBaseAprobacionAjustesEmpresaDao.actualizarValoresFacturaServicios(con,codSolicitud,numFactura,ajuste,ajusteMedico,tipoAjuste,actualizarAjusteMedicoPool);
	}
	
	/**
	 * Metodo para realizar la actualizacion de los
	 * valores de cuentas de cobro.
	 * @param con Connection
	 * @param numCxC double, número de la cuenta de cobro
	 * @param valorAjuste double, valor del ajuste
	 * @param tipoAjuste int, código del tipo de ajuste
	 * @param institucion int, código de la institución
	 * @param estadoCxC int, código del estado de la cuenta de cobro
	 * @return boolean, true si es efectivo
	 * @see com.princetonsa.dao.SqlBase.SqlBaseCierreSaldoInicialCarteraDao#actualizarCuentaDeCobro(java.sql.Connection,HashMap)
	 */
	public boolean actualizarCuentaDeCobro (Connection con,
													        double numCxC,double valorAjuste,
													        int tipoAjuste,int institucion,int estadoCxC)
	{
	    return SqlBaseAprobacionAjustesEmpresaDao.actualizarCuentaDeCobro(con,numCxC,valorAjuste,tipoAjuste,institucion,estadoCxC);
	}
	
	/**
	 * Metodo para realizar la insercion de la 
	 * aprobación del ajuste
	 * @param con Connection
	 * @param codAjuste double
	 * @param usuario String
	 * @param fechaApro String
	 * @return boolean, true efectivo
	 * @see com.princetonsa.dao.SqlBase.SqlBaseCierreSaldoInicialCarteraDao#insertarAprobacion(java.sql.Connection,HashMap)
	 */
	public boolean insertarAprobacion(Connection con,double codAjuste,String usuario,String fechaApro)
	{
	    return SqlBaseAprobacionAjustesEmpresaDao.insertarAprobacion(con,codAjuste,usuario,fechaApro);
	}
    /**
     * metodo para consultar el detalle de facturas
     * para un ajuste, obteniendo el tipo de solicitud,
     * si es de cirugia ó no.
     * @param con Connection
     * @param codAjuste double
     * @param codFactura int 
     * @param institucion int 
     * @return HashMap
     */
    public HashMap consultarDetalleFacturasXAjustes(Connection con,double codAjuste,int codFactura,int institucion)
    {
        return SqlBaseAprobacionAjustesEmpresaDao.consultarDetalleFacturasXAjustes(con, codAjuste, codFactura, institucion);
    }
    /**
     * metodo para generar la consulta del detalle de
     * las facturas a nivel de asocios, si existe información
     * de las mismas en ajustes de solicitudes cirugias, previamente
     * consultadas
     * @param con Connection
     * @param institucion int
     * @param codAjuste double
     * @param codFactura int 
     * @param detAsocFactSol int
     * @return HashMap
     */
    public HashMap consultaDetalleFacturaAsocios(Connection con,int institucion,double codAjuste,int codFactura,int detAsocFactSol,int codigoPkServArt)
    {
        return SqlBaseAprobacionAjustesEmpresaDao.consultaDetalleFacturaAsocios(con, institucion, codAjuste, codFactura, detAsocFactSol,codigoPkServArt);
    }
    public HashMap consultaDetalleFacturaNivelAsocios(Connection con,int institucion,double codAjuste,int codFactura,int detAsocFactSol,int codigoPkServArt)
    {
        return SqlBaseAprobacionAjustesEmpresaDao.consultaDetalleFacturaNivelAsocios(con, institucion, codAjuste, codFactura, detAsocFactSol,codigoPkServArt);
    }
    

    /**
     * 
     * @param con
     * @param institucion
     * @param codAjuste
     * @param codFactura
     * @param detAsocFactSol
     * @return
     */
	public HashMap consultaDetalleFacturaNivelPaquetes(Connection con, int institucion, double codAjuste, int codFactura, int detFacSol)
	{
		 return SqlBaseAprobacionAjustesEmpresaDao.consultaDetalleFacturaNivelPaquetes(con, institucion, codAjuste, codFactura, detFacSol);
	}

	/**
     * metodo para actualizar los valores del detalle de
     * asocio de facturas
     * @param con Connection
     * @param detAsocFactSol int
     * @param codServicio int
     * @param ajuste double, valor del ajuste
     * @param ajusteMedico double, valor del ajuste medico
     * @param tipoAjuste int, código del tipo de ajuste
     * @return boolean, true si es efectivo     
     */
    public boolean actualizarValoresAsocioDetalleFactura(Connection con,int detAsocFactSol,int codServicio,double ajuste,double ajusteMedico,int tipoAjuste, boolean actualizarAjusteMedicoPool)
    {
        return SqlBaseAprobacionAjustesEmpresaDao.actualizarValoresAsocioDetalleFactura(con, detAsocFactSol, codServicio, ajuste, ajusteMedico, tipoAjuste,actualizarAjusteMedicoPool);
    }
    
    /**
     * 
     */
    public boolean actualizarValoresPaquetesDetalleFactura(Connection con,int detPaqFacSol,double ajuste,double ajusteMedico,int tipoAjuste, boolean actualizarAjusteMedicoPool)
    {
    	 return SqlBaseAprobacionAjustesEmpresaDao.actualizarValoresPaquetesDetalleFactura(con, detPaqFacSol, ajuste, ajusteMedico, tipoAjuste,actualizarAjusteMedicoPool);
    }
    
    /**
	 * Método que verifica si el ajuste y la factura tienen valor de pool mayor a 0
	 * @param con
	 * @param codigoAjustes
	 * @param codigoFactura
	 * @return
	 */
	public String tieneRegistroAjustePoolFactura(Connection con,String codigoAjustes,String codigoFactura)
	{
		return SqlBaseAprobacionAjustesEmpresaDao.tieneRegistroAjustePoolFactura(con, codigoAjustes, codigoFactura);
	}
	
	
	/**
	 * Método para actualizar el valor de los ajustes al pool
	 * @param con
	 * @param codigoAjustes
	 * @param codigoFactura
	 * @return
	 */
	public boolean actualizarValorAjustePool(Connection con,String codigoAjustes,String codigoFactura)
	{
		return SqlBaseAprobacionAjustesEmpresaDao.actualizarValorAjustePool(con, codigoAjustes, codigoFactura);
	}
}
