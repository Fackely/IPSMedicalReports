package com.servinte.axioma.generadorReporte.tesoreria.impresionArqueoCaja;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.MarginBuilder;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import util.reportes.dinamico.GeneradorReporteDinamico;

import com.princetonsa.dto.tesoreria.DtoConsolidadoMovimiento;
import com.servinte.axioma.dto.tesoreria.DtoFiltroReportesArqueosCierres;
import com.servinte.axioma.generadorReporte.tesoreria.comunSubReportesImpresionArqueos.GeneradorSubReporteCuadreCaja;
import com.servinte.axioma.generadorReporte.tesoreria.comunSubReportesImpresionArqueos.GeneradorSubReporteTotalDocumentos;

/**
 * Clase que genera el reporte impresión arqueo caja resumido
 * DCU 1127 Formato A
 * @author Fabián Becerra
 *
 */
public class GeneradorReporteArqueoCajaResumido extends GeneradorReporteDinamico{
	
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
	public GeneradorReporteArqueoCajaResumido() {
    }  
	
	/**
	 * Constructor de la clase
	 */
	public GeneradorReporteArqueoCajaResumido(DtoFiltroReportesArqueosCierres dtoFiltro, DtoConsolidadoMovimiento dtoConsolidado) {
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
	    report.title(cmp.horizontalList(this.detallesReporte.crearEncabezadoReporte()
	    								));
	    report.pageFooter(this.detallesReporte.crearPiePaginaReporte());
	    
	    //-----------SECCIONES DEL REPORTE-----------------------------
	    GeneradorSubReporteTotalDocumentos generadorSubReporteTotalDocumentos = new GeneradorSubReporteTotalDocumentos();
	    GeneradorSubReporteCuadreCaja generadorSubReporteCuadreCaja = new GeneradorSubReporteCuadreCaja();
	    
	    report.summary(this.detallesReporte.crearEncabezado());
	    Integer tamano = dtoConsolidado.getTotalesDocumentoDTOs().size();
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
