package com.servinte.axioma.generadorReporte.tesoreria.impresionEntregaCajaMayorPrincipal;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.MarginBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import util.ConstantesBD;
import util.UtilidadTexto;
import util.reportes.dinamico.GeneradorReporteDinamico;

import com.princetonsa.dto.tesoreria.DtoEntidadesFinancieras;
import com.princetonsa.dto.tesoreria.DtoFormaPagoDocSoporte;
import com.princetonsa.dto.tesoreria.DtoInformacionEntrega;
import com.servinte.axioma.dto.tesoreria.DtoFiltroReportesArqueosCierres;
import com.servinte.axioma.generadorReporte.tesoreria.comunSubReportesImpresionArqueos.GeneradorSubReporteDetallesFormaPago;
import com.servinte.axioma.generadorReporte.tesoreria.comunSubReportesImpresionArqueos.GeneradorSubReporteTotalDocumentos;

public class GeneradorReporteEntregaCajaMayorPrincDetallado  extends GeneradorReporteDinamico{
	
	private DetallesGeneralesFormatosImpEntregaCajaMayPrinc detallesReporte;
	private DtoInformacionEntrega dtoConsolidado;
	private boolean esConsulta;
	private String mostrarParaSeccionEspecial;
	
	/**
	 * Constructor de la claseO
	 */
	public GeneradorReporteEntregaCajaMayorPrincDetallado() {
    }  
	
	/**
	 * Constructor de la clase
	 */
	public GeneradorReporteEntregaCajaMayorPrincDetallado(DtoFiltroReportesArqueosCierres dtoFiltro, DtoInformacionEntrega dtoConsolidado, boolean esConsulta, String mostrarParaSeccionEspecial) {
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
	    report.pageHeader(cmp.horizontalList(this.detallesReporte.crearEncabezadoReporte()
	    								));
	    
	    GeneradorSubReporteDetallesFormaPago generadorSubReporteDetalleFormaPago = new GeneradorSubReporteDetallesFormaPago();
	    GeneradorSubReporteTotalesFormaPago generadorSubReporteTotalFormaPago = new GeneradorSubReporteTotalesFormaPago();
	    GeneradorSubReporteTotalDocumentos generadorSubReporteTotalDocumentos = new GeneradorSubReporteTotalDocumentos();
	    GeneradorSubReporteTotalFormaPagoEntregadoArqueoParcial generadorSubReporteTotalFormaPagoArqueoParcial 
														= new GeneradorSubReporteTotalFormaPagoEntregadoArqueoParcial();
	    
	    report.summary(this.detallesReporte.crearEncabezado(),
	    		(dtoConsolidado.getMovimientosCaja().getTurnoDeCaja().getValorBase()!=null?cmp.horizontalList(
						cmp.text("Base en Caja (+)").setStyle(stl.style().bold()).setHorizontalAlignment(HorizontalAlignment.LEFT),
						cmp.text(UtilidadTexto.formatearValores(dtoConsolidado.getMovimientosCaja().getTurnoDeCaja().getValorBase().doubleValue())).setStyle(stl.style().bold()).setHorizontalAlignment(HorizontalAlignment.RIGHT),
						cmp.text("")
					):cmp.horizontalList()));
	   
	    Integer tamano = dtoConsolidado.getListadoDtoFormaPagoDocSoportes().size();
	    if (tamano>0) {
	    	
	    	for(DtoFormaPagoDocSoporte listaFormaPago:dtoConsolidado.getListadoDtoFormaPagoDocSoportes())
	    	{
	    		if(listaFormaPago.getCodigoTipoDetalleFormaPago()==ConstantesBD.codigoTipoDetalleFormasPagoNinguno)
	    		{
	    			report.summary(cmp.verticalList(
	    							cmp.horizontalList(
	    								cmp.text(listaFormaPago.getFormaPago()).setStyle(stl.style().bold()),
	    								cmp.text("Valor Recaudado en caja").setStyle(stl.style()).setHorizontalAlignment(HorizontalAlignment.CENTER),
	    								cmp.text(UtilidadTexto.formatearValores(listaFormaPago.getDtoCuadreCaja().getValorSistema())).setStyle(stl.style()).setHorizontalAlignment(HorizontalAlignment.RIGHT)
	    							),
	    							cmp.text("")
	    						  )
	    			);
	    		}
	    		else{
	    			report.summary(cmp.text(listaFormaPago.getFormaPago()).setStyle(stl.style().bold()));
	    			for(DtoEntidadesFinancieras listaEntidadFinanc:listaFormaPago.getListadoDtoEntidadesFinancieras())
		    		{
		    			report.summary(cmp.text(listaEntidadFinanc.getDescripcionTercero()).setStyle(stl.style().bold()),
		    					cmp.subreport(generadorSubReporteDetalleFormaPago.generarReporteTotalesFormaPago(listaEntidadFinanc.getListadoDtoDetDocSoporte())),
		    					cmp.horizontalList(cmp.text("Total "+listaFormaPago.getFormaPago()+" "+listaEntidadFinanc.getDescripcionTercero()).setStyle(stl.style().bold()),
		    							 cmp.text(UtilidadTexto.formatearValores(listaEntidadFinanc.getTotalValorSistema())).setStyle(stl.style().bold()).setHorizontalAlignment(HorizontalAlignment.RIGHT)),
		    					cmp.text("")		 
		    					);
		    		}
	    		}
	    	}
			
		}
		
	    tamano = dtoConsolidado.getTotalesDocumentoDTOs().size();
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
	    report.pageFooter(this.detallesReporte.crearPiePaginaReporte());
	    
	    return report;
   	}  

}
