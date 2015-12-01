package com.servinte.axioma.generadorReporte.tesoreria.impresionArqueoCaja;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.grp;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.MarginBuilder;
import net.sf.dynamicreports.report.builder.group.CustomGroupBuilder;
import net.sf.dynamicreports.report.constant.GroupHeaderLayout;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import util.UtilidadTexto;
import util.reportes.dinamico.GeneradorReporteDinamico;

import com.princetonsa.dto.tesoreria.DtoConsolidadoMovimiento;
import com.servinte.axioma.dto.tesoreria.DtoFiltroReportesArqueosCierres;
import com.servinte.axioma.generadorReporte.tesoreria.comunSubReportesImpresionArqueos.GeneradorSubReporteCuadreCaja;
import com.servinte.axioma.generadorReporte.tesoreria.comunSubReportesImpresionArqueos.GeneradorSubReporteDevolucionesRecibosCaja;
import com.servinte.axioma.generadorReporte.tesoreria.comunSubReportesImpresionArqueos.GeneradorSubReporteEntregasCajaMayorPrincipalRealizadas;
import com.servinte.axioma.generadorReporte.tesoreria.comunSubReportesImpresionArqueos.GeneradorSubReporteRecibosCaja;
import com.servinte.axioma.generadorReporte.tesoreria.comunSubReportesImpresionArqueos.GeneradorSubReporteTotalDocumentos;

/**
 * Clase que genera el reporte impresión arqueo caja detallado
 * DCU 1127 Formato B
 * @author Fabián Becerra
 *
 */
public class GeneradorReporteArqueoCajaDetallado extends GeneradorReporteDinamico{

	/**
	 * Atributo que contiene la información del titulo y encabezado del reporte
	 */
	private DetallesGeneralesFormatosImpArqueoCaja detallesReporte;
	
	/**
	 * Atributo que contiene la información del arqueo caja
	 */
	private DtoConsolidadoMovimiento dtoConsolidado;
	
	/**
	 * Constructor de la clase
	 */
	public GeneradorReporteArqueoCajaDetallado() {
    }  
	
	/**
	 * Constructor de la clase
	 */
	public GeneradorReporteArqueoCajaDetallado(DtoFiltroReportesArqueosCierres dtoFiltro, DtoConsolidadoMovimiento dtoConsolidado) {
		this.detallesReporte = new DetallesGeneralesFormatosImpArqueoCaja(dtoFiltro);
		this.dtoConsolidado = dtoConsolidado;
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
	    report.pageHeader(cmp.verticalList(this.detallesReporte.crearEncabezadoReporte()));
	    report.pageFooter(this.detallesReporte.crearPiePaginaReporte());
	    							
	    //-----------SECCIONES DEL REPORTE-----------------------------
	    GeneradorSubReporteRecibosCaja generadorSubReporteRecibosCaja = new GeneradorSubReporteRecibosCaja();
	    GeneradorSubReporteDevolucionesRecibosCaja generadorSubReporteDevolucionRecibosCaja = new GeneradorSubReporteDevolucionesRecibosCaja();
	    GeneradorSubReporteTrasladoCajaRecaudoRecibidos generadorSubReporteTrasladoCajaRecaudo = new GeneradorSubReporteTrasladoCajaRecaudoRecibidos();
	    GeneradorSubReporteEntregasTransportadoraRealizadas generadorSubReporteEntregasTransportadora = new GeneradorSubReporteEntregasTransportadoraRealizadas();
	    GeneradorSubReporteEntregasCajaMayorPrincipalRealizadas generadorSubReporteEntregasCajaMayorPrincRealizadas = new GeneradorSubReporteEntregasCajaMayorPrincipalRealizadas();
	    GeneradorSubReporteTotalDocumentos generadorSubReporteTotalDocumentos = new GeneradorSubReporteTotalDocumentos();
	    GeneradorSubReporteCuadreCaja generadorSubReporteCuadreCaja = new GeneradorSubReporteCuadreCaja();
	    
	    CustomGroupBuilder groupEncabezado = grp.group("groupEncabezado", "groupEncabezado", String.class).setStyle(stl.style().bold()).setPadding(0).setHeaderLayout(GroupHeaderLayout.EMPTY);
	    groupEncabezado.addHeaderComponent(
	    		
		    this.detallesReporte.crearEncabezado(),
		    		(dtoConsolidado.getMovimientosCaja().getTurnoDeCaja().getValorBase()!=null?cmp.horizontalList(
							cmp.text("Base en Caja (+)").setStyle(stl.style().bold()).setHorizontalAlignment(HorizontalAlignment.LEFT),
							cmp.text(UtilidadTexto.formatearValores(dtoConsolidado.getMovimientosCaja().getTurnoDeCaja().getValorBase().doubleValue())).setStyle(stl.style().bold()).setHorizontalAlignment(HorizontalAlignment.RIGHT),
							cmp.text("")
						):cmp.horizontalList()));
						
		 report.addGroup(groupEncabezado);
	    
	    Integer tamano = dtoConsolidado.getReciboCajaDTOs().size();
		if (tamano>0) {
			//En grupos para evitar el error overflow
	    	CustomGroupBuilder grupoRec = grp.group("grupoRecibosCaja", "grupoRecibosCaja", String.class).setStyle(stl.style().bold()).setPadding(0).setHeaderLayout(GroupHeaderLayout.EMPTY);
	    	grupoRec.addHeaderComponent(cmp.subreport( generadorSubReporteRecibosCaja.generarReporteRecibosCaja(dtoConsolidado)));
		    report.addGroup(grupoRec);
		}
		tamano =  dtoConsolidado.getDevolucionReciboCajaDTOs( ).size();
		if (tamano>0) {
			//En grupos para evitar el error overflow
	    	CustomGroupBuilder grupoDevol = grp.group("grupoDevolRecibosCaja", "grupoDevolRecibosCaja", String.class).setStyle(stl.style().bold()).setPadding(0).setHeaderLayout(GroupHeaderLayout.EMPTY);
	    	grupoDevol.addHeaderComponent(cmp.subreport( generadorSubReporteDevolucionRecibosCaja.generarReporteDevolucionRecibosCaja(dtoConsolidado)));
		    report.addGroup(grupoDevol);
		}
		tamano = dtoConsolidado.getTotalesParcialesTrasladosDTOs().size();
		if (tamano>0) {
			report.summary(cmp.subreport( generadorSubReporteTrasladoCajaRecaudo.generarReporteTrasladoCajaRecaudo(dtoConsolidado)));
		}
		tamano = dtoConsolidado.getTotalesParcialesEntrTransDTOs().size();
		if (tamano>0) {
			report.summary(cmp.subreport( generadorSubReporteEntregasTransportadora.generarReporteEntregasTransportadora(dtoConsolidado)));
		}
		tamano = dtoConsolidado.getTotalesParcialesEntrCajaDTOs().size();
		if (tamano>0) {
			report.summary(cmp.subreport( generadorSubReporteEntregasCajaMayorPrincRealizadas.generarReporteEntregasCajaMayPrincRealizadas(dtoConsolidado)));
		}
		tamano = dtoConsolidado.getTotalesDocumentoDTOs().size();
		if (tamano>0) {
			report.summary(cmp.subreport( generadorSubReporteTotalDocumentos.generarReporteTotalDocumentos(dtoConsolidado)));
		}
		tamano = dtoConsolidado.getCuadreCajaDTOs().size();
		if (tamano>0) {
			report.summary(cmp.subreport( generadorSubReporteCuadreCaja.generarReporteCuadreCaja(dtoConsolidado)));
		}
		report.setSummaryWithPageHeaderAndFooter(true);
		report.summary(this.detallesReporte.crearFirmas());
	    return report;
   	}  
}
