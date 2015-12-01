package com.princetonsa.dao.sqlbase.capitacion;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.mundo.capitacion.EdadCarteraCapitacion;

/**
 * 
 * @author wilson
 *
 */
public class SqlBaseEdadCarteraCapitacionDao 
{
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseEdadCarteraCapitacionDao.class);
	
	/**
     * 
     * @param mundo
     * @param oldQuery
     * @return
     */
    public static String armarConsultaReporte(EdadCarteraCapitacion mundo, String oldQuery)
    {
    	String consulta="";
		if((mundo.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteResumido)
			|| mundo.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoCuenta)
			|| mundo.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoFactura))
			)
        {
			consulta="";
			if(UtilidadFecha.esFechaValidaSegunAp(mundo.getFechaCorte()))
			{
				consulta+=" and ( to_char(cc.fecha_elaboracion, 'YYYY-MM-DD') <= '"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFechaCorte())+"') ";
			}
			if(mundo.getTipoConvenio()!=ConstantesBD.codigoNuncaValido)
			{
				consulta+=" and tc.codigo = '"+mundo.getTipoConvenio()+"' ";
			}
			if(mundo.getConvenioCapitado()!=ConstantesBD.codigoNuncaValido)
			{
				consulta+=" and c.codigo = "+mundo.getConvenioCapitado()+" ";
			}
			if(mundo.getContrato()!=ConstantesBD.codigoNuncaValido)
			{
				consulta+=" and cargue.contrato = "+mundo.getContrato()+" ";
			}
		}
		
		if(!consulta.trim().equals(""))
			consulta= oldQuery.replaceAll("and 1=1", consulta);
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
}
