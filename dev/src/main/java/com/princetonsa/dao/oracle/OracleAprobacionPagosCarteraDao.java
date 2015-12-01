
/*
 * Creado   1/11/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.AprobacionPagosCarteraDao;
import com.princetonsa.dao.sqlbase.SqlBaseAprobacionPagosCarteraDao;

/**
 * Clase para manejar
 *
 * @version 1.0, 1/11/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class OracleAprobacionPagosCarteraDao implements  AprobacionPagosCarteraDao 
{
    /**
     * metodo para generar la consulta de aplicacion de 
     * pagos cartera
     * @param con Connection
     * @param codTipo String
     * @param documento String
     * @param consecutivoAplicacion String 
     * @param codConvenio String
     * @param esAprobacion boolean
     * @return HashMap
     * @see com.princetonsa.dao.SqlBase.SqlBaseAprobacionPagosCarteraDao#generarConsultaAplicacionPagos(java.sql.Connection,String,String,String,String,boolean)
     */
    public HashMap generarConsultaAplicacionPagos(Connection con,String codTipo,String documento,String consecutivoAplicacion,String codConvenio,String estadoPago, boolean esAprobacion)
    {
        return SqlBaseAprobacionPagosCarteraDao.generarConsultaAplicacionPagos(con, codTipo, documento, consecutivoAplicacion, codConvenio,estadoPago, esAprobacion);
    }
    /**
     * metodo para consultar el detalle
     * de la aplicación de pagos
     * @param con Connection
     * @param int codAplicacionPago,código de la aplicación del pago
     * @return HashMap
     * @see com.princetonsa.dao.SqlBase.SqlBaseAprobacionPagosCarteraDao#generarConsultaDetalleAplicacionPagos(java.sql.Connection,int)
     */
    public HashMap generarConsultaDetalleAplicacionPagos(Connection con,int codAplicacionPago)
    {
        return SqlBaseAprobacionPagosCarteraDao.generarConsultaDetalleAplicacionPagos(con,codAplicacionPago);   
    }
    /**
     * metodo para consultar los conceptos de aplicación
     * de pagos
     * @param con Connection, conexión con la fuente de datos
     * @param institucion int, código de la institución
     * @return HashMap
     * @see com.princetonsa.dao.SqlBase.SqlBaseAprobacionPagosCarteraDao#generarConsultaConceptosAplicacionPagos(java.sql.Connection,int)
     */
    public HashMap generarConsultaConceptosAplicacionPagos(Connection con,int institucion, int codigoAplicacionPago)
    {
        return SqlBaseAprobacionPagosCarteraDao.generarConsultaConceptosAplicacionPagos(con, institucion,codigoAplicacionPago);
    }
    /**
     * metodo para consultar las cuentas de 
     * cobro con su respectivo valor para
     * una aplicacón de pagos.
     * @param con Connection 
     * @param codAplicacion int
     * @param institucion int
     * @return HashMap
     * @see com.princetonsa.dao.SqlBase.SqlBaseAprobacionPagosCarteraDao#cuentasCobroPorAplicacionPagosStr(java.sql.Connection,int,int)
     */
    public HashMap  cuentasCobroPorAplicacionPagosStr (Connection con,int codAplicacion,int institucion)
    {
        return SqlBaseAprobacionPagosCarteraDao.cuentasCobroPorAplicacionPagosStr(con, codAplicacion, institucion);
    }
    /**
     * metodo para realizar la actualización 
     * de la aplicacion del pago
     * @param con
     * @param codAplicacion
     * @return boolean
     * @see com.princetonsa.dao.SqlBase.SqlBaseAprobacionPagosCarteraDao#actualizarEstadoPago(java.sql.Connection,int)
     */
    public boolean actualizarEstadoPago (Connection con,int codAplicacion)
    {
        return SqlBaseAprobacionPagosCarteraDao.actualizarEstadoPago(con, codAplicacion); 
    }
    /**
     * metodo para actualizar el valor del 
     * pago de la factura
     * @param con Connection 
     * @param codigoFact int
     * @param valor double
     * @return boolean
     * @see com.princetonsa.dao.SqlBase.SqlBaseAprobacionPagosCarteraDao#actualizarValorPagoFactura(java.sql.Connection,int,double)
     */
    public boolean actualizarValorPagoFactura(Connection con, int codigoFact,double valor)
    {
        return SqlBaseAprobacionPagosCarteraDao.actualizarValorPagoFactura(con, codigoFact, valor);
    }
    /***
     * metodo para actualizar el saldo de la
     * cuenta de caobro
     * @param con Connection
     * @param numeroCxC double
     * @param institucion int
     * @param valor double
     * @return boolean
     * @see com.princetonsa.dao.SqlBase.SqlBaseAprobacionPagosCarteraDao#actualizarSaldoCxC(java.sql.Connection,int,double,double)
     */
    public boolean actualizarSaldoCxC (Connection con,double numeroCxC, int institucion,double valor)
    {
        return SqlBaseAprobacionPagosCarteraDao.actualizarSaldoCxC(con, numeroCxC, institucion,valor);
    }
    /**
     * metodo para generar la aprobacion del
     * pago
     * @param con Connection
     * @param codAplicacion int
     * @param usuario String
     * @param fechaA string
     * @param fechaG String
     * @param horaG String
     * @return boolena
     * @see com.princetonsa.dao.SqlBase.SqlBaseAprobacionPagosCarteraDao#insertarAprobacion(java.sql.Connection,int,String,String,String,String)
     */
    public boolean insertarAprobacion(Connection con,int codAplicacion,String usuario,String fechaA,String fechaG,String horaG)
    {
        return SqlBaseAprobacionPagosCarteraDao.insertarAprobacion(con, codAplicacion, usuario, fechaA, fechaG, horaG);
    }
}
