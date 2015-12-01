/*
 * @(#)SqlBaseEdadCarteraDao.java
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

import com.princetonsa.mundo.cartera.EdadCartera;

/**
 * 
 * @author wilson
 *
 */
public class SqlBaseEdadCarteraDao 
{
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseEdadCarteraDao.class);
	
	/**
     * 
     * @param mundo
     * @param oldQuery
     * @return
     */
    public static String armarConsultaReporte(EdadCartera mundo, String oldQuery)
    {
    	String consulta="";
		if((mundo.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteResumido)
			|| mundo.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoCuenta)
			|| mundo.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoFactura))
			&& mundo.getEsFactura().equals(ConstantesBD.acronimoNo))
        {
			consulta="";
			if(UtilidadFecha.esFechaValidaSegunAp(mundo.getFechaCorte()))
			{
				consulta+=" and (to_char(cc.fecha_elaboracion, 'YYYY-MM-DD') <= '"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFechaCorte())+"') ";
			}
			if(mundo.getTipoConvenio()!=ConstantesBD.codigoNuncaValido)
			{
				consulta+=" and tc.codigo = '"+mundo.getTipoConvenio()+"' ";
			}
			if(mundo.getConvenioNormal()!=ConstantesBD.codigoNuncaValido)
			{
				consulta+=" and c.codigo = "+mundo.getConvenioNormal()+" ";
			}
		}
		//X FACTURA
		if((mundo.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteResumido)
			||mundo.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoFactura))
			&& mundo.getEsFactura().equals(ConstantesBD.acronimoSi))
		{
			consulta="";
			if(UtilidadFecha.esFechaValidaSegunAp(mundo.getFechaCorte()))
			{
				consulta+=" and (to_char(f.fecha, 'YYYY-MM-DD') <= '"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFechaCorte())+"') ";
			}
			if(mundo.getTipoConvenio()!=ConstantesBD.codigoNuncaValido)
			{
				consulta+=" and tc.codigo = '"+mundo.getTipoConvenio()+"' ";
			}
			if(mundo.getConvenioNormal()!=ConstantesBD.codigoNuncaValido)
			{
				consulta+=" and c.codigo = "+mundo.getConvenioNormal()+" ";
			}
		}
		
		if(mundo.getEmpresaInstitucion()>0)
		{
			consulta+=" and c.empresa_institucion="+mundo.getEmpresaInstitucion()+" ";
		}
		
		consulta += "and c.tipo_contrato="+ConstantesBD.codigoTipoContratoEvento+" ";
		
		if(!consulta.trim().equals(""))
		{	
			consulta= oldQuery.replaceAll("and 1=1", consulta);
		}	
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
		
		
		
		//logger.info("\n\n CONSULTA RESULTANTE-->"+consulta+"\n\n");		
		return consulta;
    }

    /**
     * Metodo que ejecuta la consulta con el fin de guardar los datos en
     * un mapa para realizar el archivo .txt
     * @param con 
     * @param query
     * @return
     */
	public static HashMap ejecutarConsultaReporte(Connection con, String query)
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
