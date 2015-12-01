/*
 * @(#)SqlBaseMovimientoFacturasDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.Rangos;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dao.DaoFactory;

/**
 * Implementación sql genérico de todas las funciones de acceso a la fuente de datos
 * para la consulta de movimientos de facturas
 *
 * @version 1.0, Abril 12 / 2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class SqlBaseMovimientoFacturasDao 
{
    /**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseMovimientoFacturasDao.class);
    
	/**
	 * busqueda avanzada de movimiento facturas
	 * @param con
	 * @param empresa
	 * @param convenio
	 * @param facturaRangos
	 * @param fechaRangos
	 * @param valorFacturaRangos
	 * @param numeroCuentaCobroRangos
	 * @param empresaInstitucion 
	 * @return
	 * @throws SQLException
	 */
	public static  ResultSetDecorator busquedaAvanzada(	Connection con,
																		InfoDatosInt empresa,
																		InfoDatosInt convenio,
																		Rangos facturaRangos,
																		Rangos fechaRangos,
																		Rangos valorFacturaRangos,
																		Rangos numeroCuentaCobroRangos,
																		int codigoInstitucion,
																		int codigoCentroAtencion, String empresaInstitucion) throws SQLException
	{
		ResultSetDecorator respuesta=null;
		String consultaArmada="";
		if(con==null || con.isClosed())
		{
			try
			{
				DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}
			catch(SQLException e)
			{
				logger.warn("No se pudo realizar la conexión "+e.toString());
				respuesta= null;
			}
		}
		try
		{
			consultaArmada=armarConsulta(		empresa, convenio, facturaRangos, fechaRangos, valorFacturaRangos, numeroCuentaCobroRangos, codigoInstitucion, codigoCentroAtencion,empresaInstitucion);
			logger.info("consulta-->"+consultaArmada);
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consultaArmada,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			respuesta=new ResultSetDecorator(ps.executeQuery());
		}
		
		catch(SQLException e)
		{
			logger.warn("Error en la búsqueda avanzada de los movimientos de facturas " +e.toString());
			respuesta=null;
		}
		return respuesta;
	}
	
	/**
	 * Metodo que arma la consulta para los resultados de la busqueda avanzada
	 * @param empresa
	 * @param convenio
	 * @param facturaRangos
	 * @param fechaRangos
	 * @param valorFacturaRangos
	 * @param numeroCuentaCobroRangos
	 * @param empresaInstitucion 
	 * @return
	 */
	private static String armarConsulta(		InfoDatosInt empresa,
	        													InfoDatosInt convenio,
																Rangos facturaRangos,
																Rangos fechaRangos,
																Rangos valorFacturaRangos,
																Rangos numeroCuentaCobroRangos,
																int codigoInstitucion,
																int codigoCentroAtencion, String empresaInstitucion)
	{
	    /*String consulta=		"SELECT v.codigoFactura, v.centroAtencion, v.consecutivoFactura, " +
	    							"v.codigoEstadoFactura, v.nombreEstadoFactura, to_char(v.fechaEmisionFactura, 'dd/mm/yyyy') AS fechaEmisionFactura, " +
	    							"v.numeroCuentaCobro, v.valorCartera, v.saldo, v.pagos, (v.ajustesDebito+v.ajustesCredito) AS netoAjustes, " +
	    							"v.codigoConvenio, v.nombreConvenio, v.tipoIdentificacionPaciente, v.numeroIdentificacionPaciente " +
	    							"FROM viewcarteraemp v " ;*/
	    
	   String consulta = "SELECT " +
	   							"f.codigo AS codigoFactura, " +
	   							"CASE WHEN f.centro_aten IS NULL THEN '' ELSE f.centro_aten ||'' END AS centroAtencion, " +
	   							"f.consecutivo_factura AS consecutivoFactura, " +
			    				"f.estado_facturacion AS codigoEstadoFactura, " +
			    				"e.nombre AS nombreEstadoFactura, " +
			    				"to_char(f.fecha, 'dd/mm/yyyy') AS fechaEmisionFactura, " +
			    				"CASE WHEN f.numero_cuenta_cobro IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE f.numero_cuenta_cobro END AS numeroCuentaCobro, " +
			    				"f.valor_cartera AS valorCartera, " +
			    				"(f.valor_cartera - f.valor_pagos - f.ajustes_credito + f.ajustes_debito) AS saldo, " +
			    				"f.valor_pagos AS pagos, " +
			    				"(f.ajustes_credito - f.ajustes_debito) AS netoAjustes," +
			    				"f.convenio AS codigoConvenio, " +
			    				"c.nombre AS nombreConvenio, " +
			    				"p.tipo_identificacion AS tipoIdentificacionPaciente, " +
			    				"p.numero_identificacion AS numeroIdentificacionPaciente," +
			    				"getDescempresainstitucion(f.empresa_institucion) as nombreentidad," +
			    				"getnomcentroatencion(f.centro_aten) AS nombreCentroAtencion, " +
			    				"( " +
			    					"SELECT " +
			    						"SUM(rg.valor_glosa) " +
			    					"FROM " +
			    						"auditorias_glosas ag " +
			    					"INNER JOIN " +
			    						"registro_glosas rg ON (rg.codigo=ag.glosa) " +
			    					"WHERE " +
			    						"ag.codigo_factura=f.codigo " +
			    						"AND rg.estado IN ('"+ConstantesIntegridadDominio.acronimoEstadoGlosaAprobada+"','"+ConstantesIntegridadDominio.acronimoEstadoGlosaRespondida+"') " +
			    				") AS glosas " +
		    				"FROM " +
		    					"facturas f " +
		    				"INNER JOIN " +
		    					"estados_factura_f e on (e.codigo=f.estado_facturacion) " +
		    				"INNER JOIN " +
		    					"convenios c ON (c.codigo= f.convenio ) " +
		    				"LEFT OUTER JOIN " +
		    					"personas p ON (f.cod_paciente= p.codigo) ";
	    
	    String whereStr = "WHERE f.institucion= "+codigoInstitucion+" " ;
	    
	    //se documento porque en este momento se hara la busqueda estrictamente 
	    //por la descripcion, pero en caso de que se necesite el cod de la empresa hasta aca llega
	    //si se hace la busqueda en el popup.
	    
	    if(!empresa.getDescripcion().equals("") && empresa.getCodigo()<=0)
	    {   
	        consulta+="INNER JOIN empresas em ON (em.codigo= c.empresa ) ";
	        whereStr+="AND UPPER(em.razon_social) LIKE  UPPER('%"+empresa.getDescripcion()+"%') ";
	    }
	    
	    if(empresa.getCodigo()>0)
	    {
	        whereStr+=" AND c.empresa = "+empresa.getCodigo()+" ";
	    }
	    if(convenio.getCodigo()>0)
	    {
	        whereStr+=" AND c.codigo = "+convenio.getCodigo()+" ";
	    }
	    
	    /*if(convenio.getCodigo()>ConstantesBD.codigoNuncaValido)
	        whereStr+="AND v.codigoConvenio = "+convenio.getCodigo()+" ";*/
	    if(!convenio.getNombre().equals("") && convenio.getCodigo()<=0)
	        whereStr+="AND UPPER(c.nombre) LIKE UPPER('%"+convenio.getNombre()+"%') ";		
	    if(!facturaRangos.getRangoInicial().equals("") && !facturaRangos.getRangoFinal().equals(""))
	        whereStr+="AND f.consecutivo_factura >= "+facturaRangos.getRangoInicial()+" AND f.consecutivo_factura <= "+facturaRangos.getRangoFinal()+" ";
	    if(facturaRangos.getCodigoEstado()>=1)
	        whereStr+="AND f.estado_facturacion = "+facturaRangos.getCodigoEstado()+" ";
	    
	    if(!fechaRangos.getRangoInicial().equals("") && !fechaRangos.getRangoFinal().equals(""))
	    {    
	        whereStr+="AND (to_char(f.fecha, 'YYYY-MM-DD') >= '"+UtilidadFecha.conversionFormatoFechaABD(fechaRangos.getRangoInicial())+"' " +
	        							"AND to_char(f.fecha, 'YYYY-MM-DD') <= '"+UtilidadFecha.conversionFormatoFechaABD(fechaRangos.getRangoFinal())+"') ";
	    }
	    if(!valorFacturaRangos.getRangoInicial().equals("") && !valorFacturaRangos.getRangoFinal().equals(""))
	        whereStr+="AND f.valor_cartera >= "+valorFacturaRangos.getRangoInicial()+" AND f.valor_cartera <= "+valorFacturaRangos.getRangoFinal()+" ";
	    if(!numeroCuentaCobroRangos.getRangoInicial().equals("") && !numeroCuentaCobroRangos.getRangoFinal().equals(""))
	        whereStr+="AND f.numero_cuenta_cobro >= "+numeroCuentaCobroRangos.getRangoInicial()+" AND f.numero_cuenta_cobro <= "+numeroCuentaCobroRangos.getRangoFinal()+" ";
	   
	    if(codigoCentroAtencion>0)
	    	whereStr+=" and f.centro_aten= "+codigoCentroAtencion+" ";
	    if(!UtilidadTexto.isEmpty(empresaInstitucion))
	    {
	    	whereStr+=" and f.empresa_institucion= "+empresaInstitucion+" ";
	    }
	    
	    consulta+=whereStr;
	    
	    return consulta;
	}
    
    
}
