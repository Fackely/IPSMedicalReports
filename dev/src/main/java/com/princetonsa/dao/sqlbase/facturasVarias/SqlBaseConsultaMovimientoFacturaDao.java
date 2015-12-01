package com.princetonsa.dao.sqlbase.facturasVarias;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturasVarias.DtoConsolodadoFacturaVaria;
import com.princetonsa.dto.odontologia.DtoConsolidadoPresupuestoContratadoPorPromocion;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * @author Mauricio Jaramillo
 * Fecha: Agosto de 2008
 */

public class SqlBaseConsultaMovimientoFacturaDao
{

	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseConsultaMovimientoFacturaDao.class);
	
	/**
	 * Cadena SELECT para consultar los movimientos de deudores
	 */
	private static String strConSelMovimientosFacturas = "SELECT " +
															"fv.consecutivo AS consecutivofactura, " +
															"getnombreusuario(usu.login) as usuario, " +
															"pers.primer_nombre as prinomuscont, " +
															"pers.segundo_nombre as segnomuscont, " +
															"pers.primer_apellido as priapuscont, " +
															"pers.segundo_apellido as segapuscont, " +
															"fv.estado_factura AS estado, " +
															"ins.razon_social AS razonsocial, " +
															"inm.razon_social as razsolinstempresa," +
															"ca.consecutivo AS consecutivo, " +
															"ca.descripcion AS descripcioncentroatencion, " +
															"pa.descripcion as pais," +
															"rc.descripcion as region, " +
															"getnombreciudad(ca.pais,ca.departamento,ca.ciudad) as nombreciudad, " +
															"fv.codigo_fac_var AS codigofactura, " +
															"cfv.descripcion AS descripcion, " +
															"to_char(fv.fecha, 'DD/MM/YYYY') AS fechaelaboracion, " +
															"CASE " +
																"WHEN d.tipo ='"+ConstantesIntegridadDominio.acronimoPaciente+"' THEN getidentificacionpaciente(d.codigo_paciente)  " +
																"WHEN d.tipo='"+ConstantesIntegridadDominio.acronimoTipoDeudorEmpresa+"' THEN facturacion.getnitempresa(d.codigo_empresa)  " +
																"WHEN d.tipo ='"+ConstantesIntegridadDominio.acronimoTipoDeudorOtros+"' THEN getnittercero(d.codigo_tercero)  " +
																"WHEN d.tipo= '"+ConstantesIntegridadDominio.acronimoOtro+"' THEN  d.numero_identificacion " +
															"END AS deudor, " +
															"CASE " +
																"WHEN d.tipo ='"+ConstantesIntegridadDominio.acronimoPaciente+"' THEN getnombrepersona(d.codigo_paciente) " +
																"WHEN d.tipo='"+ConstantesIntegridadDominio.acronimoTipoDeudorEmpresa+"' THEN getnombreempresa(d.codigo_empresa) " +
																"WHEN d.tipo ='"+ConstantesIntegridadDominio.acronimoTipoDeudorOtros+"' THEN getdescripciontercero(d.codigo_tercero) " +
																"WHEN d.tipo= '"+ConstantesIntegridadDominio.acronimoOtro+"' THEN  d.primer_nombre ||'  '|| d.primer_apellido " +
															"END AS desdeudor, " +
															"CASE " +
																"WHEN d.tipo ='"+ConstantesIntegridadDominio.acronimoPaciente+"' THEN getidentificacionpaciente(d.codigo_paciente)  " +
																"WHEN d.tipo='"+ConstantesIntegridadDominio.acronimoTipoDeudorEmpresa+"' THEN facturacion.getnitempresa(d.codigo_empresa)  " +
																"WHEN d.tipo ='"+ConstantesIntegridadDominio.acronimoTipoDeudorOtros+"' THEN getnittercero(d.codigo_tercero)  " +
																"WHEN d.tipo= '"+ConstantesIntegridadDominio.acronimoOtro+"' THEN  d.numero_identificacion " +
															"END AS idtercero, " +
															"d.tipo AS tipodeudor, " +
															"coalesce(sum(fv.valor_factura), 0) AS valorinicial, " +
															"coalesce(sum(fv.ajustes_debito), 0) AS ajustesdebito, " +
															"coalesce(sum(fv.ajustes_credito), 0) AS ajustescredito, " +
															"coalesce(sum(fv.valor_pagos), 0) AS pagosaplicados, " +
															"(" +
																"coalesce(sum(fv.valor_factura), 0) " +
																"+ coalesce(sum(fv.ajustes_debito), 0) " +
																"- coalesce(sum(fv.ajustes_credito), 0) " +
																"- coalesce(sum(fv.valor_pagos), 0)" +
															") AS saldo " +
														"FROM " +
															"facturas_varias fv " +
															"INNER JOIN deudores d ON (d.codigo = fv.deudor) " +
															"INNER JOIN usuarios usu ON (usu.login = fv.usuario_modifica) " +
															"inner join personas pers on(usu.codigo_persona=pers.codigo) " +
															"INNER JOIN centro_atencion ca ON (ca.consecutivo = fv.centro_atencion) " +
															"inner join regiones_cobertura rc on ca.region_cobertura=rc.codigo " +
															"inner join paises pa on ca.pais=pa.codigo_pais " +
															"left outer join instituciones ins on(ins.codigo=ca.cod_institucion) " +
															"left outer join empresas_institucion inm on(inm.codigo=ca.empresa_institucion) " +
															"INNER JOIN conceptos_facturas_varias cfv ON (cfv.consecutivo = fv.concepto) " ;
														

	/**
	 * Cadena GROUP BY y ORDER BY para consultar los movimientos de deudores
	 */
	private static String strConGroupMovimientosFacturas = "GROUP BY " +
																"usu.login, pers.primer_nombre, pers.segundo_nombre, pers.primer_apellido, pers.segundo_apellido, ins.razon_social, inm.razon_social, ca.consecutivo, ca.descripcion, pa.descripcion, rc.descripcion, ca.pais, ca.departamento, ca.ciudad, fv.estado_factura, fv.consecutivo, fv.codigo_fac_var, fv.fecha, fv.deudor, d.tipo, cfv.descripcion, d.numero_identificacion, d.primer_nombre, d.primer_apellido, d.codigo_paciente, d.codigo_tercero, d.codigo_empresa " +
															"ORDER BY " +
																"ins.razon_social, inm.razon_social, ca.descripcion, fv.estado_factura, fv.consecutivo, fv.fecha, desdeudor ";
	
	/**
	 * Cadena SELECT para consultar el detalle de los movimientos por deudor (Información Factura Varia e Información Deudor)
	 */
	private static String strConSelDetalleMovimientosFacturas = "SELECT " +
																	"fv.consecutivo AS consecutivofactura, " +
																	"(" +
																		"coalesce(fv.valor_factura, 0) " +
																		"+ coalesce(fv.ajustes_debito, 0) " +
																		"- coalesce(fv.ajustes_credito, 0) " +
																		"- coalesce(fv.valor_pagos, 0)" +
																	") AS saldofactura, " +
																	"to_char(fv.fecha, 'DD/MM/YYYY') AS fechaelaboracion, " +
																	"fv.valor_factura AS valorinicial, " +
																	"fv.estado_factura AS estadofactura, " +
																	"getnomcentroatencion(fv.centro_atencion) AS nomcentroatencion, " +
																	"getnomcentrocosto(fv.centro_costo) AS nomcentrocosto, " +
																	"fv.concepto AS consecutivoconcepto, " +
																	"coalesce(cfv.descripcion, '') AS concepto, " +
																	"coalesce(fv.observaciones, '') AS observaciones, " +
																	"getdescripciondeudor(fv.deudor,d.tipo) AS deudor, " +
																	"d.tipo AS tipodeudor, " +
																	"coalesce(d.direccion, '') AS direccion, " +
																	"coalesce(d.telefono, '') AS telefono, " +
																	"coalesce(d.e_mail, '') AS email, " +
																	"coalesce(d.representante_legal, '') AS representantelegal, " +
																	"coalesce(d.nombre_contacto, '') AS nombrecontacto, " +
																	"to_char(fv.fecha_anulacion, 'DD/MM/YYYY') AS fechaaprobacion, " +
																	"coalesce(fv.usuario_anulacion, '') AS usuarioaprobacion, " +
																	"coalesce(fv.motivo_anulacion, '') AS motivoaprobacion " +
																"FROM " +
																	"facturas_varias fv " +
																	"INNER JOIN conceptos_facturas_varias cfv ON (fv.concepto = cfv.consecutivo) " +
																	"INNER JOIN deudores d ON (fv.deudor = d.codigo) " +
																"WHERE " +
																	"fv.consecutivo = ? ";
	
	/**
	 * Cadena SELECT para consultar el detalle de los movimientos de ajustes de la factura seleccionada
	 */
	private static String strConSelDetalleAjustesFacturas = "SELECT " +
																"afv.codigo AS codigoajuste, " +
																"afv.tipo_ajuste AS codtipoajuste, " +
																"afv.consecutivo AS consecutivoajuste, " +
																"CASE WHEN afv.estado = 'PEN' THEN " +
																	"to_char(afv.fecha_ajuste, 'DD/MM/YYYY') " +
																"ELSE " +
																	"to_char(afv.fecha_ajuste, 'DD/MM/YYYY') " +
																"END AS fechaajuste, " +
																"afv.estado AS estadoajuste, " +
																"afv.concepto_ajuste AS conceptoajuste, " +
																"getdescripcionconceptoajuste(afv.concepto_ajuste, afv.institucion) AS desconceptoajuste, " +
																"coalesce(afv.valor_ajuste, 0) AS valorajuste " +
															"FROM " +
																"ajus_facturas_varias afv " +
															"WHERE " +
																"afv.factura = ? ";
	
	/**
	 * Cadena SELECT para consultar el detalle de los movimientos de pagos de la factura seleccionada
	 */
	private static String strConSelDetallePagosFacturas = "SELECT " +
																"tdp.acronimo AS tipodocumento, " +
																"rc.consecutivo_recibo||' - '||apfv.numero_aplicacion AS numeroaplicacion, " +
																"CASE WHEN apfv.estado = "+ConstantesBD.codigoEstadoAplicacionPagosPendiente+" THEN " +
																	"to_char(apfv.fecha_aplicacion, 'DD/MM/YYYY') " +
																"ELSE " +
																	"to_char(apfv.fecha_anula, 'DD/MM/YYYY') " +
																"END AS fechaaplicacion, " +
																"getdesestadoaplicacionpago(apfv.estado) AS estadoaplicacion, " +
																"coalesce(apffv.valor_pago, 0) AS valorpago " +
															"FROM " +
																"pagos_general_empresa pge " +
																"INNER JOIN tipos_doc_pagos tdp ON (pge.tipo_doc = tdp.codigo) " +
																"INNER JOIN aplicacion_pagos_fvarias apfv ON (pge.codigo = apfv.pagos_general_fvarias) " +
																"INNER JOIN aplicac_pagos_factura_fvarias apffv ON (apfv.codigo = apffv.aplicacion_pagos) " +
																"LEFT JOIN recibos_caja rc ON (rc.numero_recibo_caja = pge.documento) "+
															"WHERE " +
																"apffv.factura = ? " +
															"ORDER BY " +
																"numeroaplicacion, fechaaplicacion ";
	
	/**
	 * Cadena SELECT para consultar el detalle del resumen de movimientos de la factura seleccionada
	 */
	private static String strConSelDetalleResumenFacturas = "SELECT " +
																"coalesce(sum(valor_factura), 0) AS valorinicial, " +
																"coalesce(sum(ajustes_debito), 0) AS ajustesdebito, " +
																"coalesce(sum(ajustes_credito), 0) AS ajustescredito, " +
																"coalesce(sum(valor_pagos), 0) AS pagosaplicados, " +
																"(" +
																	"coalesce(sum(valor_factura), 0) " +
																	"+ coalesce(sum(ajustes_debito), 0) " +
																	"- coalesce(sum(ajustes_credito), 0) " +
																	"- coalesce(sum(valor_pagos), 0)" +
																") AS saldo " +
																"FROM " +
																	"facturas_varias " +
																"WHERE " +
																	"codigo_fac_var = ? ";
	
	/**
	 * Método que consulta los movimientos
	 * de deudores según el tipo de deudor seleccionado
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static HashMap consultarMovimientosFactura(Connection con, HashMap criterios)
	{
		HashMap mapa = new HashMap<String, Object>();
        mapa.put("numRegistros","0");
       
        //**********************INICIO ARMANDO LA CONSULTA******************************
        String consulta = strConSelMovimientosFacturas;
        
        //Filtramos la consulta por el rango de fechas digitado. Como es requerido no es necesario validarlo
        consulta += "AND fv.fecha BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaInicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaFinal")+"")+"' ";
        
        //Filtramos la consulta por el Pais. Como es requerido no es necesario validarlo
        consulta += "AND ca.pais = '"+criterios.get("codigoPaisResidencia")+"' ";
        
        
        //Filtramos la consulta por la Ciudad seleccionada. Como no es requerida es necesario validarla
        if(UtilidadCadena.noEsVacio(criterios.get("codigoCiudad")+"")){
        	String vec[]= (criterios.get("codigoCiudad")+"").split(ConstantesBD.separadorSplit);			
			consulta=consulta+"AND ca.ciudad='"+vec[0]+"' ";
        }
        
        //Filtramos la consulta por la Region seleccionada. Como no es requerida es necesario validarla
        if(((Long) criterios.get("codigoRegion"))>0)
        	consulta += "AND ca.region_cobertura = '"+criterios.get("codigoRegion")+"' ";
        
        //Filtramos la consulta por la institucion seleccionada. Como no es requerida es necesario validarla
        if(UtilidadCadena.noEsVacio(criterios.get("esMultiempresa").equals(ConstantesBD.acronimoSi)+"")){
        	if(((Long) criterios.get("codigoEmpresaInstitucion"))>0)
        	consulta += "AND ca.empresa_institucion = '"+criterios.get("codigoEmpresaInstitucion")+"' ";    
		}else{
			if(((Long) criterios.get("codigoEmpresaInstitucion"))>0)
			consulta += "AND ca.cod_institucion = '"+criterios.get("codigoEmpresaInstitucion")+"' ";    
		}
        
        //Filtramos la consulta por Centro de Atencion seleccionado. Como no es requerido es necesario validarlo
        if(((Integer) criterios.get("consecutivoCentroAtencion"))>0)
        	consulta += "AND ca.consecutivo = '"+criterios.get("consecutivoCentroAtencion")+"' ";
        
                   
        //Filtramos la consulta por el tipo de dedudor seleccionado. Como no es requerido es necesario validarlo
       
        
        /**
         * NOTA.
         * LA BUSQUEDA GENERICA DEL DEUDOR EN LA FUNCIONALIDAD DE FACTURACION,
         * CARGA EL <<NUMERO DE IDENTIFICACION>> NO EL CODIGO DEL DEUDOR, TODO DEPENDIENDO DEL FILTRO; 
         * ES DECIR, SI ES TERCERO, EMPRESA O PACIENTE, 
         * POR CUYO MOTIVO HAY QUE HACER EL FILTRO CON TIPO DE DEUDOR PARA CLASIFICAR LA BUSQUEDA.
         */
        
        if(UtilidadCadena.noEsVacio(criterios.get("tipoDeudor")+""))
        	consulta += "AND d.tipo = '"+criterios.get("tipoDeudor")+"' ";
        
        
        
        //Filtramos la consulta por el deduor seleccionado. Como no es requerido es necesario validarlo
        if(UtilidadCadena.noEsVacio(criterios.get("deudor")+""))
        {
        		
		        if ((criterios.get("tipoDeudor")+"").equals(ConstantesIntegridadDominio.acronimoTipoDeudorEmpresa))
		        {
		        	consulta+= " AND d.codigo_empresa="+criterios.get("deudor"); 
		        }
		        
		        if ((criterios.get("tipoDeudor")+"").equals(ConstantesIntegridadDominio.acronimoTipoDeudorOtros))
		        {
		        	consulta+=" AND d.codigo_tercero="+criterios.get("deudor");
		        }
		        
				if ((criterios.get("tipoDeudor")+"").equals(ConstantesIntegridadDominio.acronimoPaciente))
				{
					consulta+=" AND d.codigo_paciente="+criterios.get("deudor"); 
				}
				
				if( (criterios.get("tipoDeudor")+"").equals(ConstantesIntegridadDominio.acronimoOtro))
				{
					consulta+=" AND fv.deudor = "+criterios.get("deudor")+" ";
				}	
	    }
        	
        //Filtramos la consulta por el Estado seleccionado. Como no es requerido es necesario validarlo	
        if( !UtilidadTexto.isEmpty( (String) criterios.get("codigoestadoFacturaVaria"))&& !( (String) criterios.get("codigoestadoFacturaVaria")).trim().equals(ConstantesBD.codigoNuncaValido+""))
        	consulta += "AND fv.estado_factura = '"+criterios.get("codigoestadoFacturaVaria")+"' ";
        
        //Filtramos la consulta por el Concepto seleccionado. Como no es requerido es necesario validarlo	
        if( !UtilidadTexto.isEmpty( (String) criterios.get("codigoConcepto"))&& !( (String) criterios.get("codigoConcepto")).trim().equals(ConstantesBD.codigoNuncaValido+""))
        	consulta += "AND cfv.codigo = '"+criterios.get("codigoConcepto")+"' ";
        
      //Filtramos la consulta por el Usuario seleccionado. Como no es requerido es necesario validarlo	
        if( !UtilidadTexto.isEmpty( (String) criterios.get("loginUsuario"))&& !( (String) criterios.get("loginUsuario")).trim().equals(ConstantesBD.codigoNuncaValido+""))
        	consulta += "AND usu.login = '"+criterios.get("loginUsuario")+"' ";
        
        //Adicionamos el Group By y el Order By a la consulta
        consulta += strConGroupMovimientosFacturas;
        //**********************FIN ARMANDO LA CONSULTA*********************************
        
        try
        {
        	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        	logger.info("====>Consulta Movimientos Facturas: "+consulta);
            mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            logger.error("ERROR EJECUNTANDO LA CONSULTA DE MOVIMIENTOS DEUDORES POR FACTURA");
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
	}

	/**
	 * Método que consulta las condiciones establecidas 
	 * para la consulta de movimientos de deudor por factura
	 * para generar el reporte
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static String consultarCondicionesMovimientosFactura(Connection con, HashMap criterios)
	{
		String condiciones = "";
		
		//**********************INICIO ARMANDO LAS CONDICIONES******************************
        //Filtramos la consulta por el estado de la factura. El cual debe ser Aprobado
		condiciones = "fv.estado_factura <> '"+ConstantesIntegridadDominio.acronimoEstadoPendiente+"' ";
		//Filtramos la consulta por el rango de fechas digitado. Como es requerido no es necesario validarlo
		condiciones += "AND TO_CHAR(fv.fecha,'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaInicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaFinal")+"")+"' ";
		//Filtramos la consulta por el tipo de dedudor seleccionado. Como no es requerido es necesario validarlo
        if(UtilidadCadena.noEsVacio(criterios.get("tipoDeudor")+""))
        	condiciones += "AND d.tipo = '"+criterios.get("tipoDeudor")+"' ";
        //Filtramos la consulta por el deduor seleccionado. Como no es requerido es necesario validarlo
        if(UtilidadCadena.noEsVacio(criterios.get("deudor")+""))
        	condiciones += "AND fv.deudor = "+criterios.get("deudor")+" ";
        //Filtramos la consulta por el número de factura digitado. Como no es requerido es necesario validarlo
        if(UtilidadCadena.noEsVacio(criterios.get("numeroFactura")+""))
        	condiciones += "AND fv.consecutivo = "+criterios.get("numeroFactura")+" ";
        //**********************FIN ARMANDO LAS CONDICIONES*********************************
		
		return condiciones;
	}
	
	/**
	 * Método que consulta la información del detalle
	 * de la factura varias seleccionada
	 * @param con
	 * @param consecutivoFactura
	 * @return
	 */
	public static HashMap consultarDetalleMovimientosFactura(Connection con, String consecutivoFactura)
	{
		HashMap mapa = new HashMap<String, Object>();
        mapa.put("numRegistros","0");
        try
        {
        	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strConSelDetalleMovimientosFacturas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        	ps.setDouble(1, Utilidades.convertirADouble(consecutivoFactura));
        	logger.info("====>Consulta Detalle Facturas Varias: "+strConSelDetalleMovimientosFacturas+ "===>Consecutivo Factura: "+consecutivoFactura);
            mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            logger.error("ERROR EJECUNTANDO LA CONSULTA DE  DETALLE DE MOVIMIENTOS DEUDORES POR FACTURA");
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
	}
	
	/**
	 * Métdo que consulta la información de los ajustes
	 * de la factura varia seleccionada
	 * @param con
	 * @param consecutivoFactura
	 * @return
	 */
	public static HashMap consultarDetalleAjustesFactura(Connection con, String codigoFactura)
	{
		HashMap mapa = new HashMap<String, Object>();
        mapa.put("numRegistros","0");
        try
        {
        	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strConSelDetalleAjustesFacturas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        	ps.setDouble(1, Utilidades.convertirADouble(codigoFactura));
        	logger.info("====>Consulta Detalle Ajustes Movimientos: "+strConSelDetalleAjustesFacturas+ "===>Codigo Factura: "+codigoFactura);
            mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            logger.error("ERROR EJECUNTANDO LA CONSULTA DE DETALLE DE AJUSTES DE MOVIMIENTOS");
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
	}

	/**
	 * Método que consulta la información de los pagos
	 * de la factura varia seleccionada
	 * @param con
	 * @param consecutivoFactura
	 * @return
	 */
	public static HashMap consultarDetallePagosFactura(Connection con, String codigoFactura)
	{
		HashMap mapa = new HashMap<String, Object>();
        mapa.put("numRegistros","0");
        try
        {
        	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strConSelDetallePagosFacturas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        	ps.setInt(1, Utilidades.convertirAEntero(codigoFactura));
        	logger.info("====>Consulta Detalle Pagos Movimientos: "+strConSelDetallePagosFacturas+ "===>Codigo Factura: "+codigoFactura);
            mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            logger.error("ERROR EJECUNTANDO LA CONSULTA DE DETALLE DE PAGOS DE MOVIMIENTOS");
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
	}

	/**
	 * Método que consulta la información del resumen de
	 * movimientos de la factura varia seleccionada
	 * @param con
	 * @param consecutivoFactura
	 * @return
	 */
	public static HashMap consultarDetalleResumenMovimientosFactura(Connection con, String codigoFactura)
	{
		HashMap mapa = new HashMap<String, Object>();
        mapa.put("numRegistros","0");
        try
        {
        	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strConSelDetalleResumenFacturas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        	ps.setDouble(1, Utilidades.convertirADouble(codigoFactura));
        	logger.info("====>Consulta Detalle Resumen Movimientos: "+strConSelDetalleResumenFacturas+ "===>Codigo Factura: "+codigoFactura);
            mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            logger.error("ERROR EJECUNTANDO LA CONSULTA DETALLE DE RESUMEN DE MOVIMIENTOS DE FACTURAS VARIAS");
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
	}
	
	
}