package com.servinte.axioma.generadorReporte.tesoreria.impresionEntregaATransportadora;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

import java.awt.Color;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.MarginBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import util.ConstantesBD;
import util.UtilidadTexto;
import util.reportes.dinamico.GeneradorReporteDinamico;

import com.princetonsa.dto.tesoreria.DtoDetalleDocSopor;
import com.princetonsa.dto.tesoreria.DtoEntidadesFinancieras;
import com.princetonsa.dto.tesoreria.DtoFormaPagoDocSoporte;
import com.princetonsa.dto.tesoreria.DtoInformacionEntrega;
import com.servinte.axioma.dto.tesoreria.DtoFiltroReportesArqueosCierres;
import com.servinte.axioma.generadorReporte.tesoreria.impresionEntregaCajaMayorPrincipal.GeneradorSubReporteTotalFormaPagoEntregadoArqueoParcial;

public class GeneradorReporteEntregaTransportadora extends GeneradorReporteDinamico{

	private DetallesGeneralesImpresionEntregaTransportadora detallesReporte;
	private DtoInformacionEntrega dtoConsolidado;
	private boolean esConsulta;
	private String mostrarParaSeccionEspecial;
	
	/**
	 * Constructor de la claseO
	 */
	public GeneradorReporteEntregaTransportadora() {
    }  
	
	/**
	 * Constructor de la clase
	 */
	public GeneradorReporteEntregaTransportadora(DtoFiltroReportesArqueosCierres dtoFiltro, DtoInformacionEntrega dtoConsolidado, boolean esConsulta, String mostrarParaSeccionEspecial) {
		this.detallesReporte = new DetallesGeneralesImpresionEntregaTransportadora(dtoFiltro);
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
	    GeneradorSubReporteTotalFormaPagoEntregadoArqueoParcial generadorSubReporteTotalFormaPagoArqueoParcial 
		= new GeneradorSubReporteTotalFormaPagoEntregadoArqueoParcial();
	    GeneradorSubReporteDetalleEntrega generadorSubReporteDetalleFormaPago = new GeneradorSubReporteDetalleEntrega();
	    
	    report.summary(this.detallesReporte.crearEncabezado());
	    
	    report.summary(cmp.text("DETALLE ENTREGA").setStyle(stl.style().bold().setPadding(1).setFontSize(9).setHorizontalAlignment(HorizontalAlignment.LEFT)
				.setBackgroundColor(Color.LIGHT_GRAY)));
	    
	    Integer tamano = dtoConsolidado.getListadoDtoFormaPagoDocSoportes().size();
	    if (tamano>0) {
	    	
	    	for(DtoFormaPagoDocSoporte listaFormaPago:dtoConsolidado.getListadoDtoFormaPagoDocSoportes())
	    	{
	    		if(listaFormaPago.getCodigoTipoDetalleFormaPago()==ConstantesBD.codigoTipoDetalleFormasPagoNinguno)
	    		{
	    			report.summary(cmp.verticalList(
	    							cmp.horizontalList(
	    								cmp.text(listaFormaPago.getFormaPago()).setStyle(stl.style().bold()),
	    								cmp.text(UtilidadTexto.formatearValores(listaFormaPago.getDtoCuadreCaja().getValorSistema())).setStyle(stl.style()).setHorizontalAlignment(HorizontalAlignment.RIGHT)
	    							),
	    							cmp.text("")
	    						  )
	    			);
	    		}
	    		else{
	    			report.summary(cmp.text(listaFormaPago.getFormaPago()).setStyle(stl.style().bold()));
	    			report.summary(cmp.subreport(generadorSubReporteDetalleFormaPago.generarReporteTotalesFormaPago(listaFormaPago.getListadoDtoEntidadesFinancieras(),listaFormaPago.getCodigoTipoDetalleFormaPago())));
	    			double totalFormaPago=0;
	    			for(DtoEntidadesFinancieras listaEntidadFinanc:listaFormaPago.getListadoDtoEntidadesFinancieras())
		    		{
    					for(DtoDetalleDocSopor listaDtoDoc:listaEntidadFinanc.getListadoDtoDetDocSoporte())
    					{
    						totalFormaPago += listaDtoDoc.getValorSistemaUnico();
    					}
		    		}
	    			report.summary(cmp.verticalList(
	    								cmp.horizontalList(cmp.text("Valor Total "+listaFormaPago.getFormaPago()).setStyle(stl.style().bold()),
	    								cmp.text(UtilidadTexto.formatearValores(totalFormaPago)).setStyle(stl.style().bold()).setHorizontalAlignment(HorizontalAlignment.RIGHT)
	    								)
	    							),	    								
	    							cmp.text("")		 
					);
	    		}
	    	}
			
		}
		
	    
	    tamano = dtoConsolidado.getCuadreCajaDTOs().size();
		if(tamano>0)
		{
			String tituloSubReporte="TOTALES ENTREGADOS";
			report.summary(cmp.subreport( generadorSubReporteTotalFormaPagoArqueoParcial.generarReporteTotalesFormaPago(dtoConsolidado,esConsulta,mostrarParaSeccionEspecial,tituloSubReporte)));
		}
		
		if(!UtilidadTexto.isEmpty(dtoConsolidado.getMovimientosCaja().getEntregaTransportadoraByCodigoPk().getMovimientosCajaByCodigoPk().getObservaciones()))
		{
			report.summary(cmp.horizontalList(
					cmp.text("Observaciones: ").setStyle(stl.style().bold()).setWidth(18),
					cmp.text(dtoConsolidado.getMovimientosCaja().getEntregaTransportadoraByCodigoPk().getMovimientosCajaByCodigoPk().getObservaciones()).setStyle(stl.style())
				)
			);
		}
		
		
		
		report.setSummaryWithPageHeaderAndFooter(true);
	    report.summary(this.detallesReporte.crearFirmas());
	    report.pageFooter(this.detallesReporte.crearPiePaginaReporte());
	    
	    return report;
   	}  
	
}
