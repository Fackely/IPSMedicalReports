package com.servinte.axioma.generadorReporte.tesoreria.impresionCierreTurnoCaja;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.MarginBuilder;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import util.reportes.dinamico.GeneradorReporteDinamico;

import com.princetonsa.dto.tesoreria.DtoInformacionEntrega;
import com.servinte.axioma.dto.tesoreria.DtoFiltroReportesArqueosCierres;
import com.servinte.axioma.generadorReporte.tesoreria.comunSubReportesImpresionArqueos.GeneradorSubReporteCuadreCaja;
import com.servinte.axioma.generadorReporte.tesoreria.comunSubReportesImpresionArqueos.GeneradorSubReporteTotalDocumentos;

/**
 * Clase que genera el reporte impresión cierre turno resumido
 * DCU 1038 Formato A
 * @author Fabián Becerra
 *
 */
public class GeneradorReporteCierreTurnoCajaResumido extends GeneradorReporteDinamico{
	
	private DetallesGeneralesFormatosImpCierreTurnoCaja detallesReporte;
	private DtoInformacionEntrega dtoConsolidado;
	
	/**
	 * Constructor de la clase
	 */
	public GeneradorReporteCierreTurnoCajaResumido() {
    }  
	
	/**
	 * Constructor de la clase
	 */
	public GeneradorReporteCierreTurnoCajaResumido(DtoFiltroReportesArqueosCierres dtoFiltro, DtoInformacionEntrega dtoConsolidado) {
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
	    report.setSummaryWithPageHeaderAndFooter(true);
	    GeneradorSubReporteTotalDocumentos generadorSubReporteTotalDocumentos = new GeneradorSubReporteTotalDocumentos();
	    GeneradorSubReporteCuadreCaja generadorSubReporteCuadreCaja = new GeneradorSubReporteCuadreCaja();
	    GeneradorSubReporteFaltanteSobranteCierreTurno generadorSubReporteFaltanteSobrante = new GeneradorSubReporteFaltanteSobranteCierreTurno();
	    GeneradorSubReporteRegistroTrasladoCajaRecaudoCierreTurno generadorSubReporteTrasladoCajaRecaudo = new GeneradorSubReporteRegistroTrasladoCajaRecaudoCierreTurno();
	    GeneradorSubReporteRegistroEntregaCajaMayorCierreTurno generadorSubReporteCajaMayorCierre = new GeneradorSubReporteRegistroEntregaCajaMayorCierreTurno();
	    report.pageFooter(this.detallesReporte.crearPiePaginaReporte());
	    report.summary(this.detallesReporte.crearEncabezado());
	    Integer tamano = dtoConsolidado.getTotalesDocumentoDTOs().size();
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
			report.summary(cmp.subreport( generadorSubReporteTrasladoCajaRecaudo.generarReporteTraladoCaja(dtoConsolidado)));
		}
		if (dtoConsolidado.getTrasladoCajaMayorEnCierre()!=null) {
			report.summary(cmp.subreport( generadorSubReporteCajaMayorCierre.generarReporteEntregaCajaMayor(dtoConsolidado)));
		}
		
		
		report.setSummaryWithPageHeaderAndFooter(true);
	    report.summary(this.detallesReporte.crearFirmas());
	    return report;
   	}  
	
	
	
	
	
}
