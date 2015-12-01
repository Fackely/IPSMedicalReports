package com.servinte.axioma.generadorReporte.tesoreria.impresionEntregaCajaMayorPrincipal;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.MarginBuilder;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import util.UtilidadTexto;
import util.reportes.dinamico.GeneradorReporteDinamico;

import com.princetonsa.dto.tesoreria.DtoInformacionEntrega;
import com.servinte.axioma.dto.tesoreria.DtoFiltroReportesArqueosCierres;
import com.servinte.axioma.generadorReporte.tesoreria.comunSubReportesImpresionArqueos.GeneradorSubReporteTotalDocumentos;

public class GeneradorReporteEntregaCajaMayorPrincResumido extends GeneradorReporteDinamico{

	private DetallesGeneralesFormatosImpEntregaCajaMayPrinc detallesReporte;
	private DtoInformacionEntrega dtoConsolidado;
	private boolean esConsulta;
	private String mostrarParaSeccionEspecial;
	
	/**
	 * Constructor de la clase
	 */
	public GeneradorReporteEntregaCajaMayorPrincResumido() {
    }  
	
	/**
	 * Constructor de la clase
	 */
	public GeneradorReporteEntregaCajaMayorPrincResumido(DtoFiltroReportesArqueosCierres dtoFiltro, DtoInformacionEntrega dtoConsolidado, boolean esConsulta, String mostrarParaSeccionEspecial) {
		this.detallesReporte = new DetallesGeneralesFormatosImpEntregaCajaMayPrinc(dtoFiltro);
		this.dtoConsolidado = dtoConsolidado;
		this.esConsulta=esConsulta;
		this.mostrarParaSeccionEspecial=mostrarParaSeccionEspecial;
    }
    
	/**
     * Este método genera y configura el reporte general
     * @return report reporte general
     */
	public JasperReportBuilder generarReporte() {
    	
		JasperReportBuilder report = report();
		
   	    //------------CONFIGURACIÓN GENERAL DEL REPORTE-----------------
   	    report.setPageFormat(PageType.LETTER, PageOrientation.PORTRAIT);
   	    MarginBuilder margenes = margin().setBottom(30).setTop(40).setLeft(40).setRight(20);
	   	report.setPageMargin(margenes); 
	    report.highlightDetailEvenRows();
	    report.title(cmp.horizontalList(this.detallesReporte.crearEncabezadoReporte()
	    								));
	    GeneradorSubReporteTotalDocumentos generadorSubReporteTotalDocumentos = new GeneradorSubReporteTotalDocumentos();
	    GeneradorSubReporteTotalesFormaPago generadorSubReporteTotalFormaPago = new GeneradorSubReporteTotalesFormaPago();
	    GeneradorSubReporteTotalFormaPagoEntregadoArqueoParcial generadorSubReporteTotalFormaPagoArqueoParcial 
	    									= new GeneradorSubReporteTotalFormaPagoEntregadoArqueoParcial();
	    report.pageFooter(this.detallesReporte.crearPiePaginaReporte());
	    report.summary(this.detallesReporte.crearEncabezado());
	    Integer tamano = dtoConsolidado.getTotalesDocumentoDTOs().size();
		if (tamano>0) {
			report.summary(cmp.subreport( generadorSubReporteTotalDocumentos.generarReporteTotalDocumentos(dtoConsolidado)));
		}
		
		if(UtilidadTexto.isEmpty(mostrarParaSeccionEspecial))
		{
			if (dtoConsolidado.getTrasladoCajaMayorEnCierre()!=null){
				report.summary(cmp.subreport( generadorSubReporteTotalFormaPago.generarReporteTotalesFormaPago(dtoConsolidado)));
			}
		}else
		{
			tamano = dtoConsolidado.getCuadreCajaDTOs().size();
			if(tamano>0)
			{
				report.summary(cmp.subreport( generadorSubReporteTotalFormaPagoArqueoParcial.generarReporteTotalesFormaPago(dtoConsolidado,esConsulta,mostrarParaSeccionEspecial,"")));
			}
		}
		
		report.setSummaryWithPageHeaderAndFooter(true);
	    report.summary(this.detallesReporte.crearFirmas());
	    return report;
   	}  
	
}
