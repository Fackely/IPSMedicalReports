package com.servinte.axioma.generadorReporte.manejoPaciente.egresosEstancias.indicadoresHospitalizacion;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;

import java.util.ArrayList;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.MarginBuilder;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import util.reportes.dinamico.GeneradorReporteDinamico;

import com.princetonsa.dto.manejoPaciente.DtoFiltroReporteIndicadoresHospitalizacion;
import com.princetonsa.dto.manejoPaciente.DtoResultadoConsultaIndicadoresHospitalizacion;


/**
 * Clase encargada de generar el reporte indicadores hospitalizacion
 * DCU 627 v 1.1
 * @author Fabián Becerra
 *
 */
public class GeneradorReporteIndicadoresHospitalizacion extends GeneradorReporteDinamico{

	/**
	 * Método constructor de la clas
	 */
	public GeneradorReporteIndicadoresHospitalizacion() {
	}

	/**
	 * Método que genera el reporte indicadores hospitalización
	 * @param dtoFiltro datos ingresados por el usuario en la forma
	 * @param dtoResultado datos de la consulta
	 * @return JasperReportBuilder reporte consolidado
	 */
	public JasperReportBuilder generarReporte(DtoFiltroReporteIndicadoresHospitalizacion dtoFiltro,
			ArrayList<DtoResultadoConsultaIndicadoresHospitalizacion> listaDtoResultados)
	{
		JasperReportBuilder reporte = report();
		
		DetallesGeneralesReportesEgresosEstancias detGen= new DetallesGeneralesReportesEgresosEstancias(dtoFiltro);
		GeneradorSubReporteIndicadoresHosp subreporte= new GeneradorSubReporteIndicadoresHosp();
		
		reporte.setPageFormat(PageType.LETTER, PageOrientation.PORTRAIT);
		reporte.pageHeader(cmp.verticalList(detGen.crearBandaLogos(),cmp.text("")));
		reporte.pageFooter(detGen.crearPiePaginaReporte());
		
		MarginBuilder margenes =margin().setBottom(30).setTop(40).setLeft(40).setRight(20);
		reporte.setPageMargin(margenes);
		reporte.setSummaryWithPageHeaderAndFooter(true);
		
		reporte.summary(detGen.crearEncabezado());
		reporte.summary(cmp.subreport(subreporte.generarReporte(listaDtoResultados, false)));
		
		reporte.build();
		
		return reporte;
	}
	
	/**
	 * Método que genera el reporte plano indicadores hospitalización
	 * @param dtoFiltro datos ingresados por el usuario en la forma
	 * @param dtoResultado datos de la consulta
	 * @return JasperReportBuilder reporte consolidado
	 */
	public JasperReportBuilder generarReportePlano(DtoFiltroReporteIndicadoresHospitalizacion dtoFiltro,
			ArrayList<DtoResultadoConsultaIndicadoresHospitalizacion> listaDtoResultados)
	{
		JasperReportBuilder reporte = report();
		
		DetallesGeneralesReportesEgresosEstancias detGen= new DetallesGeneralesReportesEgresosEstancias(dtoFiltro);
		GeneradorSubReporteIndicadoresHosp subreporte= new GeneradorSubReporteIndicadoresHosp();
		
		reporte.setPageFormat(PageType.LETTER, PageOrientation.PORTRAIT);
		reporte.ignorePagination();
		
		reporte.summary(detGen.crearEncabezadoReportePlano());
		reporte.summary(cmp.subreport(subreporte.generarReporte(listaDtoResultados, true)));
		
		reporte.build();
		
		return reporte;
	}
}
