package com.princetonsa.dao.sqlbase.facturasVarias;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

/**
 * Fecha: Abril de 2008
 * @author Mauricio Jaramillo
 */

public class SqlBaseConsultaImpresionPagosFacturasVariasDao 
{

	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseConsultaImpresionPagosFacturasVariasDao.class);
	
	/**
     * Cadena para consultar los documentos pendientes que pertenecen a deudores 
     */
    private static String strConPagFacturasVarias =
    											"SELECT "+
    												"pge.tipo_doc as codigotipo, "+
		    										"tdp.descripcion as destipo, "+
		    										"tdp.acronimo as acronimotipo, "+
		    										"pge.documento as documento, "+
		    										"pge.codigo as codigopago, "+
		    										"to_char(pge.fecha_documento, 'DD/MM/YYYY') as fechadocumento, "+
		    										"apf.codigo as codigoaplicacion, "+
		    										"apf.estado as estadoaplicacion, "+
		    										"esp.descripcion as nombreestadoaplicacion, "+
		    										"getTotalConcApliPagFactVarias(apf.codigo) as valorconceptos, "+
		    										"getTotalVlrFactVariasApliPagos(apf.codigo) as valorfacturas, "+
		    										"pge.deudor as codigodeudor, "+
		    										"getdescripciontercero(pge.deudor) as nombredeudor, "+
		    										"getnumeroidentificaciontercero(pge.deudor) as identificaciondeudor, "+
		    										"apf.observaciones as observaciones, "+
		    										"apf.usuario as usuario, "+
		    										"coalesce(to_char(apf.fecha_aplicacion, 'DD/MM/YYYY'), '') as fecha, "+
		    										"apf.usuario_anula as usuarioaprobanul, "+
		    										"coalesce(to_char(apf.fecha_anula, 'DD/MM/YYYY'), '') as fechaaprobanul "+
		    									"FROM "+
		    										"aplicacion_pagos_fvarias apf "+
		    										"INNER JOIN pagos_general_empresa pge ON (apf.pagos_general_fvarias=pge.codigo) "+
													"INNER JOIN tipos_doc_pagos tdp ON (tdp.codigo=pge.tipo_doc) "+
													"INNER JOIN estados_aplicaciones_pagos esp ON (apf.estado=esp.codigo) "+
												"WHERE "+
													"1=1 ";
    
    /**
     * Cadena para consultar los conceptos de la aplicacion de pago seleccionada en el listado
     */
    private static String strConConPagFacturasVarias = 
										    	"SELECT "+
													"capf.aplicacion_pagos_fvarias as aplicacion, "+
													"capf.concepto_pagos as conceptopagos, "+
													"getdescripcionconceptopago(capf.concepto_pagos, ?) as desconcepto, "+
													"capf.valor_base as valorbase, "+
													"capf.porcentaje as porcentaje, "+
													"capf.valor_concepto as valorconcepto "+
												"FROM "+
													"conceptos_apli_pagos_fvarias capf "+
													"INNER JOIN aplicacion_pagos_fvarias apf ON (apf.codigo = capf.aplicacion_pagos_fvarias) "+
												"WHERE "+
													"capf.aplicacion_pagos_fvarias = ? "+
													"AND capf.institucion = ?";										
    	
    /**
     * Cadena para consultar las facturas de la aplicacion de pago seleccionada en el listado
     */
    private static String strConFactPagFacturasVarias =
									    	"SELECT "+
										    	"apff.aplicacion_pagos as aplicacion, "+
												"apff.valor_pago as valorpago, "+
												"apff.factura as factura, "+
												"getconsecutivofacturavarias(apff.factura) AS consecutivofactura, "+
												"coalesce(to_char(apf.fecha_anula, 'DD/MM/YYYY'), '') as fechaaprobanul "+
											"FROM "+
												"aplicac_pagos_factura_fvarias apff "+
												"INNER JOIN aplicacion_pagos_fvarias apf ON (apf.codigo = apff.aplicacion_pagos) "+
											"WHERE "+
												"apff.aplicacion_pagos = ? "+
											"ORDER BY apff.factura";
    											
	/**
	 * @param con
	 * @param codigoInstitucionInt
	 * @param criterios
	 * @return
	 */
	public static HashMap buscarPagosFacturasVarias(Connection con, int codigoInstitucionInt, HashMap criterios)
	{
        HashMap mapa = new HashMap<String, Object>();
        mapa.put("numRegistros","0");
        String condiciones = "";
        try
        {
        	if(Utilidades.convertirAEntero(criterios.get("tipo")+"") != ConstantesBD.codigoNuncaValido)
        		condiciones += "AND pge.tipo_doc = "+criterios.get("tipo")+" ";
        	if(!(criterios.get("numeroDocumento")+"").trim().equals(""))
        		condiciones += "AND pge.documento = '"+criterios.get("numeroDocumento")+"' ";
        	if(!(criterios.get("consecutivoPago")+"").trim().equals(""))
        		condiciones += "AND apf.codigo = "+criterios.get("consecutivoPago")+" ";
        	if(Utilidades.convertirAEntero(criterios.get("estadoPago")+"") != ConstantesBD.codigoNuncaValido)
        		condiciones += "AND apf.estado = "+criterios.get("estadoPago")+" ";
        	if(!(criterios.get("fechaInicial")+"").trim().equals("") && !(criterios.get("fechaFinal")+"").trim().equals(""))
        		condiciones += "AND apf.fecha_aplicacion BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaInicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaFinal")+"")+"' ";
        	condiciones += "AND pge.institucion = ?";
        	
            PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strConPagFacturasVarias+condiciones,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, codigoInstitucionInt);
            logger.info("-------->CONSULTA: "+strConPagFacturasVarias+condiciones);
            mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            logger.error("ERROR EJECUNTANDO LA CONSULTA DE APLICACION DE PAGOS DE FACTURAS VARIAS");
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
    }
	
	/**
	 * @param con
	 * @param codigoInstitucionInt
	 * @param codigoAplicacion
	 * @return
	 */
	public static HashMap buscarConceptos(Connection con, int codigoInstitucionInt, int codigoAplicacion)
	{
		HashMap mapa = new HashMap();
		mapa.put("numRegistros","0");
        try
        {
        	 PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strConConPagFacturasVarias,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        	 ps.setInt(1, codigoInstitucionInt);
        	 ps.setInt(2, codigoAplicacion);
             ps.setInt(3, codigoInstitucionInt);
             logger.info("-------->CONSULTA CONCEPTOS: "+strConConPagFacturasVarias);
             logger.info("-------->CODIGO APLICACION: "+codigoAplicacion);
             logger.info("-------->CODIGO INSTITUCION: "+codigoInstitucionInt);
             mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            logger.error("ERROR EJECUNTANDO LA CONSULTA DE CONCEPTOS DE LA APLICACION DE PAGOS FACTURAS VARIAS");
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
	}

	/**
	 * @param con
	 * @param codigoInstitucionInt
	 * @param codigoAplicacion
	 * @return
	 */
	public static HashMap buscarFacturas(Connection con, int codigoInstitucionInt, int codigoAplicacion)
	{
		HashMap mapa = new HashMap();
		mapa.put("numRegistros","0");
        try
        {
        	 PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strConFactPagFacturasVarias,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
             ps.setInt(1, codigoAplicacion);
             logger.info("-------->CONSULTA FACTURAS: "+strConFactPagFacturasVarias);
             logger.info("-------->CODIGO APLICACION: "+codigoAplicacion);
             mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            logger.error("ERROR EJECUNTANDO LA CONSULTA DE FACTURAS DE LA APLICACION DE PAGOS FACTURAS VARIAS");
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
	}

	
	

}