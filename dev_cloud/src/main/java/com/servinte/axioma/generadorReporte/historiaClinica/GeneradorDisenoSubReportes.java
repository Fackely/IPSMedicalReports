package com.servinte.axioma.generadorReporte.historiaClinica;

import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.template;

import java.awt.Color;
import java.util.Locale;

import net.sf.dynamicreports.report.builder.MarginBuilder;
import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.LineStyle;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import util.reportes.dinamico.EstilosReportesDinamicosHistoriaClinica;

public class GeneradorDisenoSubReportes {

	
	public GeneradorDisenoSubReportes() {
	}
	
	
	/**
	 * @return templante para columnas de tablas de subreportes
	 */
	public ReportTemplateBuilder crearPlantillaReporte()
	{
		ReportTemplateBuilder reportTemplate;

		StyleBuilder rootStyle 				= stl.style().setPadding(1).setFontSize(7);
		StyleBuilder columnStyle 			= stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK));
		StyleBuilder columnTitleStyle		= stl.style(columnStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT)
		.setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK));
		StyleBuilder boldStyle          	= stl.style(rootStyle).bold();
		StyleBuilder groupStyle				= stl.style(boldStyle).setPadding(1).setFontSize(7).setHorizontalAlignment(HorizontalAlignment.LEFT);
		StyleBuilder subtotalStyle      	= stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTotalRSB);

		reportTemplate = template()
		.setLocale(Locale.ENGLISH)
		//Se define el tamano de la hoja y la orientación de la misma
		.setPageFormat(PageType.LETTER, PageOrientation.PORTRAIT)
		.setColumnStyle(columnStyle)
		.setColumnTitleStyle(columnTitleStyle)
		.setGroupStyle(groupStyle)
		.setGroupTitleStyle(groupStyle)
		.setSubtotalStyle(subtotalStyle)
		;

		return reportTemplate;
	}
	
	/**
	 * Creacion de margenes del reporte
	 * @return MarginBuilder
	 */
	public MarginBuilder crearMagenesReporte()
	{
		MarginBuilder margin;
		margin = margin()
		.setTop(1)
		.setBottom(30)
		.setLeft(0)
		.setRight(0)
		;

		return margin;
	}
	
	/**
	 * Creacion de margenes del reporte
	 * @return MarginBuilder
	 */
	public MarginBuilder crearMagenesSubReporte()
	{
		MarginBuilder margin;
		margin = margin()
		.setTop(0)
		.setBottom(0)
		.setLeft(0)
		.setRight(0)
		;

		return margin;
	}

	/**
	 * Creacion de margenes del reporte
	 * @return MarginBuilder
	 */
	public MarginBuilder crearMagenesSubReporteBordeSuperiorEspacio()
	{
		MarginBuilder margin;
		margin = margin()
		.setTop(3)
		.setBottom(0)
		.setLeft(0)
		.setRight(0)
		;

		return margin;
	}
	
	/**
	 * Creacion de margenes del reporte
	 * @return MarginBuilder
	 */
	public MarginBuilder crearMagenesSubReporteVacias()
	{
		MarginBuilder margin;
		margin = margin()
		.setTop(3)
		.setBottom(0)
		.setLeft(0)
		.setRight(0)
		;

		return margin;
	}
	
	

	
}
