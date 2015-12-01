package com.servinte.axioma.generadorReporte.tesoreria.impresionTrasladoEntreCajas;

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
import util.ConstantesIntegridadDominio;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.reportes.dinamico.GeneradorReporteDinamico;

import com.princetonsa.dto.tesoreria.DtoConsultaTrasladosCajasRecaudo;
import com.princetonsa.dto.tesoreria.DtoEntidadesFinancieras;
import com.princetonsa.dto.tesoreria.DtoFormaPagoDocSoporte;
import com.princetonsa.dto.tesoreria.DtoInformacionEntrega;
import com.servinte.axioma.dto.tesoreria.DtoFiltroReportesArqueosCierres;
import com.servinte.axioma.generadorReporte.tesoreria.comunSubReportesImpresionArqueos.GeneradorSubReporteDetallesFormaPago;

public class GeneradorReporteTrasladoEntreCajas extends GeneradorReporteDinamico{

	private DetallesGeneralesImpresionTrasladoEntreCajas detallesReporte;
	private DtoInformacionEntrega dtoConsolidado;
	private DtoConsultaTrasladosCajasRecaudo dtoConsulta;
	
	/**
	 * Constructor de la clase
	 */
	public GeneradorReporteTrasladoEntreCajas() {
    }  
	
	/**
	 * Constructor de la clase
	 */
	public GeneradorReporteTrasladoEntreCajas(DtoFiltroReportesArqueosCierres dtoFiltro, DtoInformacionEntrega dtoConsolidado, DtoConsultaTrasladosCajasRecaudo dtoConsulta) {
		this.detallesReporte = new DetallesGeneralesImpresionTrasladoEntreCajas(dtoFiltro,dtoConsulta);
		this.dtoConsolidado = dtoConsolidado;
		this.dtoConsulta = dtoConsulta;
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
		report.setSummaryWithPageHeaderAndFooter(true);
	    report.pageFooter(this.detallesReporte.crearPiePaginaReporte());
	    report.summary(this.detallesReporte.crearEncabezado());
	    GeneradorSubReporteDetallesFormaPago generadorSubReporteDetalleFormaPago = new GeneradorSubReporteDetallesFormaPago();
	    GeneradorSubReporteTotalEntregados generadorSubReporteTotalEntregados = new GeneradorSubReporteTotalEntregados();
	    
	    Integer tamano = dtoConsolidado.getListadoDtoFormaPagoDocSoportes().size();
	    if (tamano>0) {
	    	
	    	report.summary(cmp.text("Detalle de la Solicitud de Traslado").setStyle(stl.style().bold()));
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
	    	
	    	report.summary(cmp.subreport(generadorSubReporteTotalEntregados.generarReporteTotalesEntregados(dtoConsolidado)));
	    	report.summary(cmp.verticalList(
	    						cmp.text("Observaciones Solicitud Traslado:").setStyle(stl.style().bold()),
	    						cmp.text(dtoConsulta.getObservaciones()).setStyle(stl.style()),
	    						(dtoConsulta.getEstadoSolicitud().equals((String)ValoresPorDefecto.getIntegridadDominio(
	    								ConstantesIntegridadDominio.acronimoEstadoSolicitudTrasladoCajaSolicitado))?cmp.text(""):
	    																											cmp.verticalList(
																		    										cmp.text(""),
																		    			    						cmp.text("Observaciones Aceptación Traslado:").setStyle(stl.style().bold()),
																		    			    						cmp.text(dtoConsulta.getObservacionesAceptacion()).setStyle(stl.style()),
																		    			    						cmp.text("")
	    																											)
	    						)
	    					)
	    				);
		}
	    
	    report.summary(this.detallesReporte.crearFirmas());
	    return report;
   	}  
}
