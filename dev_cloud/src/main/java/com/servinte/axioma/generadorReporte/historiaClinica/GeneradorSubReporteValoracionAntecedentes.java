package com.servinte.axioma.generadorReporte.historiaClinica;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.awt.Color;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.SplitType;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.jasperreports.engine.JRDataSource;

import org.apache.struts.util.MessageResources;

import util.reportes.dinamico.DataSource;
import util.reportes.dinamico.EstilosReportesDinamicosHistoriaClinica;

public class GeneradorSubReporteValoracionAntecedentes {

	
	
	/** 
	 * Mensajes parametrizados del reporte.
	 */
	private static MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.historiaclinica.impresionHistoriaClinica");

	
	public GeneradorSubReporteValoracionAntecedentes() {
	}
	
	
	public JasperReportBuilder generarSubReporteOtrosAntecedentes(){
		JasperReportBuilder otrosAntecedentes = report();
		HorizontalListBuilder tituloSeccion = null;
		TextColumnBuilder []cols = new TextColumnBuilder[3];
		ComponentBuilder<?, ?> componentBuilder = null;
		GeneradorDisenoSubReportes generadorDisenoSubReportes = new GeneradorDisenoSubReportes();
		
		
		
		
		tituloSeccion=	cmp.horizontalList(cmp.verticalList(cmp.horizontalList(
				cmp.text(messageResource.getMessage("historia_clinica_lable_otros_antecedentes_titulo")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloTituloUnderline).underline().setHorizontalAlignment(HorizontalAlignment.LEFT))
		)))	;
		
		
		
		TextColumnBuilder<String>     tipoAntecedente = col.column(messageResource.getMessage("historia_clinica_columna_tipoAntecedente"),  "tipoAntecedente",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(9).setBorder(stl.pen1Point().setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     descripcion =col.column(messageResource.getMessage("historia_clinica_columna_descripcion"),  "descripcion",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(9).setBorder(stl.pen1Point().setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     profesional =col.column(messageResource.getMessage("historia_clinica_columna_profesional"),  "profesional",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(9).setBorder(stl.pen1Point().setLineColor(Color.BLACK)));
		
		cols[0]=tipoAntecedente;  
		cols[1]=descripcion;	
		cols[2]=profesional;
		
		componentBuilder = cmp.verticalList(tituloSeccion);
		otrosAntecedentes
		.summary(componentBuilder)
		.summary(cmp.subreport(generarSubReporteOtrosAntecedentesTable()))
		.setTemplate(generadorDisenoSubReportes.crearPlantillaReporte()).build();
		return otrosAntecedentes;
	}
	
	
	public JasperReportBuilder generarSubReporteOtrosAntecedentesTable(){
		JasperReportBuilder otrosAntecedentes = report();
		HorizontalListBuilder tituloSeccion = null;
		TextColumnBuilder []cols = new TextColumnBuilder[3];
		ComponentBuilder<?, ?> componentBuilder = null;
		GeneradorDisenoSubReportes generadorDisenoSubReportes = new GeneradorDisenoSubReportes();
		
		TextColumnBuilder<String>     tipoAntecedente = col.column(messageResource.getMessage("historia_clinica_columna_tipoAntecedente"),  "tipoAntecedente",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(9).setBorder(stl.pen1Point().setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     descripcion =col.column(messageResource.getMessage("historia_clinica_columna_descripcion"),  "descripcion",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(9).setBorder(stl.pen1Point().setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     profesional =col.column(messageResource.getMessage("historia_clinica_columna_profesional"),  "profesional",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(9).setBorder(stl.pen1Point().setLineColor(Color.BLACK)));
		
		cols[0]=tipoAntecedente;  
		cols[1]=descripcion;	
		cols[2]=profesional;
		
		otrosAntecedentes
		.columns(cols)
		.setDataSource(crearDatasourceOtrosAntecedentes())
		.setDetailSplitType(SplitType.PREVENT)
		.setTemplate(generadorDisenoSubReportes.crearPlantillaReporte())
		.setPageMargin(generadorDisenoSubReportes.crearMagenesSubReporte())
		.build();
		return otrosAntecedentes;
	}
	
	
	public JRDataSource crearDatasourceOtrosAntecedentes(){
		DataSource dataSource = new DataSource("tipoAntecedente","descripcion", "profesional" );
		dataSource.add("Inicial","descripcion Inicial", "axioma Axioma");
		dataSource.add("Inicial2","descripcion Inicial2", "axioma Axioma");
		return dataSource;
	}
	


	
}
