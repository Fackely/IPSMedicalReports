
/*
 * Creado   1/11/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;

/**
 * Clase para manejar
 *
 * @version 1.0, 1/11/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class SqlBaseAprobacionPagosCarteraDao 
{
    /**
     * manejador de los logs de la clase
     */
    private static Logger logger=Logger.getLogger(SqlBaseAprobacionPagosCarteraDao.class);
    /**
     * query para consultar los datos de aplicación de pagos cartera empresas
     */
    private static String consultaAplicacionPagosStr=" SELECT "+
													    		"ape.numero_aplicacion as numero_aplicacion," +
													    		"ape.codigo as codigo_aplicacion_pago," +
													    		"pge.tipo_doc as tipo_documento," +
													    		"tdp.acronimo as acronimo_tipo_documento," +
													    		"pge.documento as documento," +
													    		"pge.fecha_documento as fecha," +
													    		"pge.convenio as codigo_convenio," +
													    		"getnombreconvenio(pge.convenio) as nombre_convenio," +
													    		"getTotalVlrFactApliPagos(ape.codigo) as valor_facturas," +
													    		"getTotalConceptosApliPagos(ape.codigo) as valor_conceptos," +
													    		"ape.observaciones as observaciones," +
													    		"ape.fecha_aplicacion as fecha_aplicacion," +
													    		"ape.estado as estado," +
													    		"getDescEstadoApliPagos(ape.estado) as descripcion_estado " +
													    		"from cartera.aplicacion_pagos_empresa ape " +
													    		"inner join pagos_general_empresa pge on (pge.codigo=ape.pagos_general_empresa) " +
													    		"inner join tipos_doc_pagos tdp on(tdp.codigo=pge.tipo_doc)"; 
    /**
     * query para consultar el detalle de una aplicación de pagos
     */
    private static String consultaDetalleAplicacionPagosStr="SELECT "+
															    		"apf.numero_cuenta_cobro as numero_cxc," +
															    		"apf.factura as codigo_factura," +
															    		"f.consecutivo_factura as factura," +
															    		"f.centro_aten as codigocentroatencion," +
															    		"getnomcentroatencion(f.centro_aten) as nombrecentroatencion," +
															    		"apf.valor_pago as valor_pago," +
															    		"f.estado_facturacion as estado_facturacion," +
															    		"getSaldoFacturaPagos(f.codigo) as saldo_factura " +
															    		"from aplicacion_pagos_factura apf " +
															    		"inner join facturas f on(f.codigo=apf.factura) " +
															    		"WHERE apf.aplicacion_pagos=?";
    /**
     * query para consultar los conceptos de aplicacción de pagos
     */
    private static String consultaConceptosAplicacionPagosStr="SELECT "+
															    		"cap.valor_concepto as valor," +
															    		"tcp.descripcion as tipo," +
															    		"cp.descripcion as concepto " +
															    		"FROM conceptos_apli_pagos cap " +
															    		"inner join concepto_pagos cp on(cp.codigo=cap.concepto_pagos) " +
															    		"inner join tipo_concepto_pagos tcp on (tcp.codigo=cp.tipo) " +
															    		"where cap.institucion=? and cap.aplica_pagos_empresa=? ";
    /**
     * query para consultar las cuentas de cobro para una aplicacion
     */
    private static String cuentasCobroPorAplicacionPagosStr="SELECT " +
														    		"numero_cuenta_cobro as numero_cuenta_cobro," +
														    		//"valor_pago_cxc as valor_pago_cxc," +
														    		"getVlrFactApliPagosXCxC(aplica_pagos_empresa,numero_cuenta_cobro) as valor_pago_cxc," +
														    		"getVlrFactApliPagosXCxC(aplica_pagos_empresa,numero_cuenta_cobro) as valor_pagos_fact " +
														    		"from aplicacion_pagos_cxc " +
														    		"where aplica_pagos_empresa=? and institucion=?";   
    /**
     * query para actualizar el estado de la aplicación de pagos
     */
    private static String actualizarEstadoPagoStr="UPDATE cartera.aplicacion_pagos_empresa SET estado="+ConstantesBD.codigoEstadoAplicacionPagosAprobado+" where codigo=?";
    /**
     * query para insertar el valor de pagos de la factura
     */
    private static String actualizarVlrPagoFacturaStr=" UPDATE facturas SET valor_pagos=(valor_pagos)+? where codigo=?";
    
    /**
     * query para insertar la aprobacion del pago
     */
    private static String insertarAprobacionStr="INSERT INTO aprobacion_apli_pagos" +
																		    		"(aplica_pagos_empresa," +
																		    		"usuario," +
																		    		"fecha_aprobacion," +
																		    		"fecha_grabacion," +
																		    		"hora_grabacion) " +
																		    		"VALUES(?,?,?,?,?)";
    
    /**
     * metodo para generar la consulta de aplicacion de 
     * pagos cartera
     * @param con Connection
     * @param codTipo String
     * @param documento String
     * @param consecutivoAplicacion String 
     * @param codConvenio String
     * @param  esAprobacion boolean
     * @return HashMap
     * @see com.princetonsa.dao.AprobacionPagosCarteraDao#generarConsultaAplicacionPagos(java.sql.Connection,String,String,String,String,boolean)
     */
    public static HashMap generarConsultaAplicacionPagos(Connection con,String codTipo,String documento,
            															String consecutivoAplicacion,String codConvenio,String estadoPago,
            															boolean esAprobacion)
    {
        try
	    {
            boolean esPrimero = true;
            PreparedStatementDecorator ps = null;
            String query=consultaAplicacionPagosStr+" WHERE ";            
	        if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}	   
	        if(!codTipo.equals(ConstantesBD.codigoNuncaValido+""))
	        {
	            query+=" pge.tipo_doc="+codTipo;
	            esPrimero = false;
	        }
	        if(!documento.equals(""))
	        {
	            if(!esPrimero)
	                query+=" AND ";
	            query+=" pge.documento='"+documento+"'";
	            esPrimero = false;
	        }
	        if(!consecutivoAplicacion.equals(""))
	        {
	            if(!esPrimero)
	                query+=" AND ";
	            query+=" ape.numero_aplicacion="+consecutivoAplicacion;
	            esPrimero = false;
	        }
	        if(!codConvenio.equals(ConstantesBD.codigoNuncaValido+""))
	        {
	            if(!esPrimero)
	                query+=" AND ";
	            query+=" pge.convenio="+codConvenio;
	            esPrimero = false;
	        }
	        
	        if(!estadoPago.equals(""))
	        {
	            if(!esPrimero)
	                query+=" AND ";
	            query+=" ape.estado="+estadoPago;
	            esPrimero = false;
	        }
	        
	        
	        
	        if(esAprobacion)
	            query+=" AND ape.estado="+ConstantesBD.codigoEstadoAplicacionPagosPendiente;
	        
	        logger.info("LA CONSULTA ES----->"+query);
	        ps= new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	 	        
	        ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
	        String[] colums={"numero_aplicacion","codigo_aplicacion_pago","tipo_documento",
			                 "acronimo_tipo_documento","documento",
			                 "fecha","codigo_convenio","nombre_convenio",
			                 "valor_facturas","valor_conceptos",
			                 "observaciones","fecha_aplicacion","estado",
			                 "descripcion_estado"
			                };	        
	        return UtilidadBD.resultSet2HashMap(colums,rs,false,true).getMapa();
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en generarConsultaAplicacionPagos"+e.toString() );
		   return null;
	    }
    }
    /**
     * metodo para consultar el detalle
     * de la aplicación de pagos
     * @param con Connection
     * @param int codAplicacionPago,código de la aplicación del pago
     * @return HashMap
     * @see com.princetonsa.dao.AprobacionPagosCarteraDao#generarConsultaDetalleAplicacionPagos(java.sql.Connection)
     */
    public static HashMap generarConsultaDetalleAplicacionPagos(Connection con,int codAplicacionPago)
    {
        try
	    {            
            PreparedStatementDecorator ps = null;
            ps= new PreparedStatementDecorator(con.prepareStatement(consultaDetalleAplicacionPagosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, codAplicacionPago);            
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            String[] colums={"numero_cxc","codigo_factura","factura","codigocentroatencion","nombrecentroatencion","valor_pago","estado_facturacion","saldo_factura"};	        
	        return UtilidadBD.resultSet2HashMap(colums,rs,false,true).getMapa();
	    }
        catch(SQLException e)
	    {
	        logger.warn(e+"Error en generarConsultaDetalleAplicacionPagos"+e.toString() );
		   return null;
	    }        
    }
    /**
     * metodo para consultar los conceptos de aplicación
     * de pagos
     * @param con Connection, conexión con la fuente de datos
     * @param institucion int, código de la institución
     * @param codigoAplicacionPago 
     * @return HashMap
     * @see com.princetonsa.dao.AprobacionPagosCarteraDao#generarConsultaConceptosAplicacionPagos(java.sql.Connection,int)
     */
    public static HashMap generarConsultaConceptosAplicacionPagos(Connection con,int institucion, int codigoAplicacionPago)
    {
        try
	    {            
            PreparedStatementDecorator ps = null;
            ps= new PreparedStatementDecorator(con.prepareStatement(consultaConceptosAplicacionPagosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, institucion);
            ps.setInt(2, codigoAplicacionPago);
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            String[] colums={"valor","concepto","tipo"};	        
	        return UtilidadBD.resultSet2HashMap(colums,rs,false,true).getMapa();
	    }
        catch(SQLException e)
	    {
	        logger.warn(e+"Error en generarConsultaConceptosAplicacionPagos"+e.toString() );
		   return null;
	    }        
    }
    /**
     * metodo para consultar las cuentas de 
     * cobro con su respectivo valor para
     * una aplicacón de pagos.
     * @param con Connection 
     * @param codAplicacion int
     * @param institucion int
     * @return HashMap
     * @see com.princetonsa.dao.AprobacionPagosCarteraDao#cuentasCobroPorAplicacionPagosStr(java.sql.Connection,int,int)
     */
    public static HashMap  cuentasCobroPorAplicacionPagosStr (Connection con,int codAplicacion,int institucion)    
    {
        try
	    {            
            PreparedStatementDecorator ps = null;
            ps= new PreparedStatementDecorator(con.prepareStatement(cuentasCobroPorAplicacionPagosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, codAplicacion);
            ps.setInt(2, institucion);            
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            String[] colums={"numero_cuenta_cobro","valor_pago_cxc","valor_pagos_fact"};	        
	        return UtilidadBD.resultSet2HashMap(colums,rs,false,true).getMapa();
	    }
        catch(SQLException e)
	    {
	        logger.warn(e+"Error en cuentasCobroPorAplicacionPagosStr"+e.toString() );
		   return null;
	    }   
    }
    /**
     * metodo para realizar la actualización 
     * de la aplicacion del pago
     * @param con
     * @param codAplicacion
     * @return boolean
     * @see com.princetonsa.dao.AprobacionPagosCarteraDao#actualizarEstadoPago(java.sql.Connection,int)
     */
    public static boolean actualizarEstadoPago (Connection con,int codAplicacion)
    {
        try
        {
            PreparedStatementDecorator ps = null;
            ps= new PreparedStatementDecorator(con.prepareStatement(actualizarEstadoPagoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, codAplicacion);
            int r=ps.executeUpdate();
            if(r>0)
                return true;
            else
                return false;
        }
        catch(SQLException e)
	    {
	        logger.warn(e+"Error en actualizarEstadoPago"+e.toString() );
		   return false;
	    }   
    }
    /**
     * metodo para actualizar el valor del 
     * pago de la factura
     * @param con Connection 
     * @param codigoFact int
     * @param valor double
     * @return boolean
     * @see com.princetonsa.dao.AprobacionPagosCarteraDao#actualizarValorPagoFactura(java.sql.Connection,int,double)
     */
    public static boolean actualizarValorPagoFactura(Connection con, int codigoFact,double valor)
    {
        try
        {
            PreparedStatementDecorator ps = null;
            ps= new PreparedStatementDecorator(con.prepareStatement(actualizarVlrPagoFacturaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));            
            ps.setDouble(1, valor);
            ps.setInt(2, codigoFact);            
            int r=ps.executeUpdate();
            if(r>0)
                return true;
            else
                return false;
        }
        catch(SQLException e)
	    {
	        logger.warn(e+"Error en actualizarValorPagoFactura"+e.toString() );
		   return false;
	    }   
    }
    /***
     * metodo para actualizar el saldo de la
     * cuenta de caobro
     * @param con Connection
     * @param numeroCxC double
     * @param institucion int
     * @param valor double
     * @return boolean
     * @see com.princetonsa.dao.AprobacionPagosCarteraDao#actualizarSaldoCxC(java.sql.Connection,int,double,double)
     */
    public static boolean actualizarSaldoCxC (Connection con,double numeroCxC, int institucion,double valor)
    {
        try
        {
            PreparedStatementDecorator ps = null;
            String cadena="UPDATE cuentas_cobro SET saldo_cuenta=(saldo_cuenta-"+valor+"),pagos=(pagos+"+valor+") where numero_cuenta_cobro=? and institucion=?";
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));            
            ps.setDouble(1, numeroCxC);
            ps.setInt(2, institucion);
            int r=ps.executeUpdate();
            if(r>0)
                return true;
            else
                return false;
        }
        catch(SQLException e)
	    {
	        logger.warn(e+"Error en actualizarSaldoCxC"+e.toString() );
		   return false;
	    }    
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
     * @see com.princetonsa.dao.AprobacionPagosCarteraDao#insertarAprobacion(java.sql.Connection,int,String,String,String,String)
     */
    public static boolean insertarAprobacion(Connection con,int codAplicacion,String usuario,String fechaA,String fechaG,String horaG)
    {
        try
        {
            PreparedStatementDecorator ps = null;
            ps= new PreparedStatementDecorator(con.prepareStatement(insertarAprobacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));            
            ps.setInt(1,codAplicacion);
            ps.setString(2, usuario);
            ps.setString(3, fechaA);
            ps.setString(4, fechaG);
            ps.setString(5, horaG);
            int r=ps.executeUpdate();
            if(r>0)
                return true;
            else
                return false;
        }
        catch(SQLException e)
	    {
	        logger.warn(e+"Error en insertarAprobacion"+e.toString() );
		   return false;
	    }    
    }
}
