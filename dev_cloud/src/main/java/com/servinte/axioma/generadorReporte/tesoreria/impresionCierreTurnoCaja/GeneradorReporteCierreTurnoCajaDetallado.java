package com.servinte.axioma.generadorReporte.tesoreria.impresionCierreTurnoCaja;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.grp;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.MarginBuilder;
import net.sf.dynamicreports.report.builder.group.CustomGroupBuilder;
import net.sf.dynamicreports.report.constant.GroupHeaderLayout;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.constant.SplitType;
import util.UtilidadTexto;
import util.reportes.dinamico.GeneradorReporteDinamico;

import com.princetonsa.dto.tesoreria.DtoInformacionEntrega;
import com.servinte.axioma.dto.tesoreria.DtoFiltroReportesArqueosCierres;
import com.servinte.axioma.generadorReporte.tesoreria.comunSubReportesImpresionArqueos.GeneradorSubReporteCuadreCaja;
import com.servinte.axioma.generadorReporte.tesoreria.comunSubReportesImpresionArqueos.GeneradorSubReporteDevolucionesRecibosCaja;
import com.servinte.axioma.generadorReporte.tesoreria.comunSubReportesImpresionArqueos.GeneradorSubReporteEntregasCajaMayorPrincipalRealizadas;
import com.servinte.axioma.generadorReporte.tesoreria.comunSubReportesImpresionArqueos.GeneradorSubReporteRecibosCaja;
import com.servinte.axioma.generadorReporte.tesoreria.comunSubReportesImpresionArqueos.GeneradorSubReporteTotalDocumentos;

/**
 * Clase que genera el reporte impresión cierre turno detallado
 * DCU 1038 Formato B
 * @author Fabián Becerra
 *
 */
public class GeneradorReporteCierreTurnoCajaDetallado extends GeneradorReporteDinamico{
	
	private DetallesGeneralesFormatosImpCierreTurnoCaja detallesReporte;
	private DtoInformacionEntrega dtoConsolidado;
	
	/**
	 * Constructor de la claseO
	 */
	public GeneradorReporteCierreTurnoCajaDetallado() {
    }  
	
	/**
	 * Constructor de la clase
	 */
	public GeneradorReporteCierreTurnoCajaDetallado(DtoFiltroReportesArqueosCierres dtoFiltro, DtoInformacionEntrega dtoConsolidado) {
		this.detallesReporte = new DetallesGeneralesFormatosImpCierreTurnoCaja(dtoFiltro);
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
	    report.pageHeader(cmp.horizontalList(this.detallesReporte.crearEncabezadoReporte()
	    								));
	    
	    GeneradorSubReporteRecibosCaja generadorSubReporteRecibosCajaCierre = new GeneradorSubReporteRecibosCaja();
	    GeneradorSubReporteDevolucionesRecibosCaja generadorSubReporteDevolRecibos = new GeneradorSubReporteDevolucionesRecibosCaja();
	    GeneradorSubReporteTrasladoCajaRecaudoRecibidos generadorSubReporteTrasladoRecaudo = new GeneradorSubReporteTrasladoCajaRecaudoRecibidos();
	    GeneradorSubReporteEntregasTransportadoraRealizadasCierreTurno generadorSubReporteEntregaTransCierre = new GeneradorSubReporteEntregasTransportadoraRealizadasCierreTurno();
	    GeneradorSubReporteEntregasCajaMayorPrincipalRealizadas generadorSubReporteEntregaCajaMayor = new GeneradorSubReporteEntregasCajaMayorPrincipalRealizadas();
	    
	    GeneradorSubReporteTotalDocumentos generadorSubReporteTotalDocumentos = new GeneradorSubReporteTotalDocumentos();
	    GeneradorSubReporteCuadreCaja generadorSubReporteCuadreCaja = new GeneradorSubReporteCuadreCaja();
	    GeneradorSubReporteFaltanteSobranteCierreTurno generadorSubReporteFaltanteSobrante = new GeneradorSubReporteFaltanteSobranteCierreTurno();
	    GeneradorSubReporteRegistroTrasladoCajaRecaudoCierreTurno generadorSubReporteTrasladoCajaRecaudoEnCierre = new GeneradorSubReporteRegistroTrasladoCajaRecaudoCierreTurno();
	    GeneradorSubReporteRegistroEntregaCajaMayorCierreTurno generadorSubReporteCajaMayorCierre = new GeneradorSubReporteRegistroEntregaCajaMayorCierreTurno();

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
	    	grupoRec.addHeaderComponent(cmp.subreport( generadorSubReporteRecibosCajaCierre.generarReporteRecibosCaja(dtoConsolidado)));
		    report.addGroup(grupoRec);
		}
	    tamano = dtoConsolidado.getDevolucionReciboCajaDTOs().size();
	    if (tamano>0) {
	    	//En grupos para evitar el error overflow
	    	CustomGroupBuilder grupoDevol = grp.group("grupoDevolRecibosCaja", "grupoDevolRecibosCaja", String.class).setStyle(stl.style().bold()).setPadding(0).setHeaderLayout(GroupHeaderLayout.EMPTY);
	    	grupoDevol.addHeaderComponent(cmp.subreport( generadorSubReporteDevolRecibos.generarReporteDevolucionRecibosCaja(dtoConsolidado)));
		    report.addGroup(grupoDevol);
		}
	    tamano = dtoConsolidado.getTrasladoCajaDTOs().size();
	    if (tamano>0) {
			report.summary(cmp.subreport( generadorSubReporteTrasladoRecaudo.generarReporteTrasladoCajaRecaudo(dtoConsolidado)));
		}
	    tamano = dtoConsolidado.getTotalesParcialesEntrTransDTOs().size();
	    if (tamano>0) {
			report.summary(cmp.subreport( generadorSubReporteEntregaTransCierre.generarReporteEntregasTransportadora(dtoConsolidado)));
		}
	    tamano = dtoConsolidado.getTotalesParcialesEntrCajaDTOs().size();
	    if (tamano>0) {
			report.summary(cmp.subreport( generadorSubReporteEntregaCajaMayor.generarReporteEntregasCajaMayPrincRealizadas(dtoConsolidado)));
		}
	    tamano = dtoConsolidado.getTotalesDocumentoDTOs().size();
	    if (tamano>0) {
			report.summary(cmp.subreport( generadorSubReporteTotalDocumentos.generarReporteTotalDocumentos(dtoConsolidado)));
		}
		tamano = dtoConsolidado.getCuadreCajaDTOs().size();
		if (tamano>0) {
			report.summary(cmp.subreport( generadorSubReporteCuadreCaja.generarReporteCuadreCaja(dtoConsolidado)));
		}
		tamano = dtoConsolidado.getFaltanteSobranteDTOs().size();
		if (tamano>0) {
			report.summary(cmp.subreport( generadorSubReporteFaltanteSobrante.generarReporteFaltanteSobrante(dtoConsolidado)));
		}
		if (dtoConsolidado.getTrasladoCajaRecaudoEnCierre()!=null) {
			report.summary(cmp.subreport( generadorSubReporteTrasladoCajaRecaudoEnCierre.generarReporteTraladoCaja(dtoConsolidado)));
		}
		if (dtoConsolidado.getTrasladoCajaMayorEnCierre()!=null) {
			report.summary(cmp.subreport( generadorSubReporteCajaMayorCierre.generarReporteEntregaCajaMayor(dtoConsolidado)));
		}
		report.setSummaryWithPageHeaderAndFooter(true);
		report.setSummarySplitType(SplitType.IMMEDIATE);
	    report.summary(this.detallesReporte.crearFirmas());
	    report.pageFooter(this.detallesReporte.crearPiePaginaReporte());
	    report.build();
	     
	    return report;
   	}  
	
}
