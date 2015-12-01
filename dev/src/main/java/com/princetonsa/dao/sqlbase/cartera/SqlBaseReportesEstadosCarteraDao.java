/*
 * @(#)SqlBaseReportesEstadosCarteraDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK jdk1.5.0_07
 *
 */
package com.princetonsa.dao.sqlbase.cartera;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.mundo.cartera.ReportesEstadosCartera;

/**
 * Consultas estandar de reportes cartera
 * @author Wilson Rios
 * wrios@princetonsa.com
 */
public class SqlBaseReportesEstadosCarteraDao 
{
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseReportesEstadosCarteraDao.class);
	
	/**
	 * 
	 * @param mundo
	 * @return
	 */
	public static String armarConsultaEstadoCarteraEvento(ReportesEstadosCartera mundo, String oldQuery)
	{
		String consulta="";
		if((mundo.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteResumido)
			|| mundo.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoCuenta)
			|| mundo.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoFactura)))
        {
			consulta="1=1";
			if(UtilidadFecha.esFechaValidaSegunAp(mundo.getFechaInicial()) && UtilidadFecha.esFechaValidaSegunAp(mundo.getFechaFinal()))
			{
				consulta+=" and (to_char(cc.fecha_elaboracion, 'YYYY-MM-DD') between '"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFechaInicial())+"' and '"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFechaFinal())+"') ";
			}
			if(mundo.getEmpresa()!=ConstantesBD.codigoNuncaValido)
			{
				consulta+=" and e.codigo = "+mundo.getEmpresa()+" ";
			}
			if(mundo.getConvenio()!=ConstantesBD.codigoNuncaValido)
			{
				consulta+=" and c.codigo = "+mundo.getConvenio()+" ";
			}
			if(mundo.getEstadoCuenta()!=ConstantesBD.codigoNuncaValido)
			{
				consulta+=" and cc.estado = "+mundo.getEstadoCuenta()+" ";
			}
			if(mundo.getEmpresaInstitucion()>0)
			{
				consulta+=" and c.empresa_institucion= "+mundo.getEmpresaInstitucion()+" ";
			}
		}
		if(!consulta.trim().equals(""))
			consulta= oldQuery.replaceAll("1=1", consulta);
		else
			consulta=oldQuery;
		
		logger.info("mundo.getManejaConversionMoneda()-->"+mundo.getManejaConversionMoneda()+" index->"+mundo.getIndex());
		if(mundo.getManejaConversionMoneda() && mundo.getIndex()>=0)
		{
			try
			{
				logger.info("f->"+UtilidadTexto.formatearExponenciales(Double.parseDouble(mundo.getTiposMonedaTagMap("factorconversion_"+mundo.getIndex()).toString())));
				double factor= 1/Double.parseDouble(mundo.getTiposMonedaTagMap("factorconversion_"+mundo.getIndex()).toString());
				logger.info("Factor--->"+UtilidadTexto.formatearExponenciales(factor));
				if(factor>0)
				{
					consulta= consulta.replaceAll("\\*1", "*"+UtilidadTexto.formatearExponenciales(factor));
				}
			}
			catch (Exception e) 
			{
				logger.error("no hizo el replace del documento!!!!");
				e.printStackTrace();
			}
		}
		
		return consulta;
	}
	
	/**
	 * 
	 * @param mundo
	 * @param oldQuery
	 * @return
	 */
	public static String armarConsultaTOTALESestadoCarteraEvento(ReportesEstadosCartera mundo, String oldQuery)
	{
		String consulta="";
		if(	(mundo.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteResumido) 
			 || mundo.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoCuenta)
			 || mundo.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoFactura))
			 && mundo.getEsCapitado().equals(ConstantesBD.acronimoNo))
        {
			if(UtilidadFecha.esFechaValidaSegunAp(mundo.getFechaInicial()) && UtilidadFecha.esFechaValidaSegunAp(mundo.getFechaFinal()))
			{
				consulta+=" and (to_char(cc.fecha_elaboracion, 'YYYY-MM-DD') between '"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFechaInicial())+"' and '"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFechaFinal())+"') ";
			}
			if(mundo.getEmpresa()!=ConstantesBD.codigoNuncaValido)
			{
				consulta+=" and e.codigo = "+mundo.getEmpresa()+" ";
			}
			if(mundo.getConvenio()!=ConstantesBD.codigoNuncaValido)
			{
				consulta+=" and c.codigo = "+mundo.getConvenio()+" ";
			}
			if(mundo.getEstadoCuenta()!=ConstantesBD.codigoNuncaValido)
			{
				consulta+=" and cc.estado = "+mundo.getEstadoCuenta()+" ";
			}
			if(mundo.getEmpresaInstitucion()>0)
			{
				consulta+=" and c.empresa_institucion= "+mundo.getEmpresaInstitucion()+" ";
			}
	    }
		
		//PARTE DE CAPITACION -- ES PRACTICAMENTE LO MISMO PERO LO HAGO APARTE POR SI HAY CAMBIOS
		if(	(mundo.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteResumido) 
				 || mundo.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoCuenta)
				 || mundo.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoFactura))
				 && mundo.getEsCapitado().equals(ConstantesBD.acronimoSi))
        {
			if(UtilidadFecha.esFechaValidaSegunAp(mundo.getFechaInicial()) && UtilidadFecha.esFechaValidaSegunAp(mundo.getFechaFinal()))
			{
				consulta+=" and (to_char(cc.fecha_elaboracion, 'YYYY-MM-DD') between '"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFechaInicial())+"' and '"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFechaFinal())+"') ";
			}
			if(mundo.getEmpresa()!=ConstantesBD.codigoNuncaValido)
			{
				consulta+=" and e.codigo = "+mundo.getEmpresa()+" ";
			}
			if(mundo.getConvenio()!=ConstantesBD.codigoNuncaValido)
			{
				consulta+=" and c.codigo = "+mundo.getConvenio()+" ";
			}
			if(mundo.getEstadoCuenta()!=ConstantesBD.codigoNuncaValido)
			{
				consulta+=" and cc.estado = "+mundo.getEstadoCuenta()+" ";
			}
			
			//empresa institucion en el documento no apararece para cambios
	    }
		
		if(!consulta.trim().equals(""))
			consulta= oldQuery.replaceAll("and 1=1", consulta);
		else
			consulta= oldQuery;
		
		logger.info("mundo.getManejaConversionMoneda()-->"+mundo.getManejaConversionMoneda()+" index->"+mundo.getIndex());
		if(mundo.getManejaConversionMoneda() && mundo.getIndex()>=0)
		{
			try
			{
				logger.info("f->"+UtilidadTexto.formatearExponenciales(Double.parseDouble(mundo.getTiposMonedaTagMap("factorconversion_"+mundo.getIndex()).toString())));
				double factor= 1/Double.parseDouble(mundo.getTiposMonedaTagMap("factorconversion_"+mundo.getIndex()).toString());
				logger.info("Factor--->"+UtilidadTexto.formatearExponenciales(factor));
				if(factor>0)
				{
					consulta= consulta.replaceAll("\\*1", "*"+UtilidadTexto.formatearExponenciales(factor));
				}
			}
			catch (Exception e) 
			{
				logger.error("no hizo el replace del documento!!!!");
				e.printStackTrace();
			}
		}
		
		return consulta;
	}

	/**
     * Metodo que ejecuta la consulta con el fin de guardar los datos de la cartera en
     * un mapa para realizar el archivo .txt
     * @param con 
     * @param query
     * @return
     */
	public static HashMap ejecutarConsultaEstadoCarteraEvento(Connection con, String query)
	{
		HashMap mapa = new HashMap();
		mapa.put("numRegistros","0");
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
		return (HashMap)mapa.clone();
	}
	
}