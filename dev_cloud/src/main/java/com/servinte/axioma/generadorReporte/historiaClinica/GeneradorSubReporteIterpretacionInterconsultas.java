/**
 * 
 */
package com.servinte.axioma.generadorReporte.historiaClinica;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.template;

import java.awt.Color;
import java.util.Locale;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.MarginBuilder;
import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.LineStyle;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import util.reportes.dinamico.EstilosReportesDinamicosHistoriaClinica;

/**
 * @author JorOsoVe
 *
 */ 
public class GeneradorSubReporteIterpretacionInterconsultas 
{

	/**
	 * 
	 * @param interpretacion
	 * @return
	 */
	public JasperReportBuilder generarReporte(String interpretacion) 
	{
		JasperReportBuilder report = report();
		report.setTemplate(crearPlantillaReporte());

		
		HorizontalListBuilder itemComponent;
		
		
		TextFieldBuilder<String> texto;
		TextFieldBuilder<String> titulo;
		
		
		titulo=cmp.text("INTERPRETACION").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent=cmp.horizontalList(titulo);
		
		texto=cmp.text(interpretacion).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)).setPadding(2).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent.newRow().add(texto);
		
		
		
		ComponentBuilder componente= itemComponent;
		
				
	
		
		report
		.setPageMargin(crearMagenesReporte())
		.summary(cmp.verticalList(componente));
		
		return report;
		
	}
	
	public MarginBuilder crearMagenesReporte()
	 {
	  MarginBuilder margin;
	  margin = margin()
	  .setLeft(0)
	  .setRight(0)
	  ;

	  return margin;
	 }

	/**
	 * @return templante para columnas de tablas de subreportes
	 */
	public ReportTemplateBuilder crearPlantillaReporte()
	{
		ReportTemplateBuilder reportTemplate;

		StyleBuilder rootStyle 				= stl.style().setPadding(1).setFontSize(9);
		StyleBuilder columnStyle 			= EstilosReportesDinamicosHistoriaClinica.estiloDetalleC.setBorder(stl.pen1Point().setLineColor(Color.BLACK));
		StyleBuilder columnTitleStyle		= EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSombra.setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBorder(stl.pen1Point().setLineColor(Color.BLACK)).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen1Point().setLineColor(Color.BLACK));
		
		reportTemplate = template();
		reportTemplate.setLocale(Locale.ENGLISH);
		//Se define el tamano de la hoja y la orientación de la misma
		reportTemplate.setPageFormat(PageType.LETTER, PageOrientation.PORTRAIT);
		reportTemplate.setColumnStyle(columnStyle);
		reportTemplate.setColumnTitleStyle(columnTitleStyle);
		
		
		
		return reportTemplate;
	}
	
	

}
