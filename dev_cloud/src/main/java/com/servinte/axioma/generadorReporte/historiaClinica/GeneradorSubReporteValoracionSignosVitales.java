package com.servinte.axioma.generadorReporte.historiaClinica;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.VerticalAlignment;

import org.apache.struts.util.MessageResources;

import util.reportes.dinamico.EstilosReportesDinamicosHistoriaClinica;

public class GeneradorSubReporteValoracionSignosVitales {

	
	/**
	 * Mensajes parametrizados del reporte.
	 */ 
	private static MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.historiaclinica.impresionHistoriaClinica");

	public GeneradorSubReporteValoracionSignosVitales() {
	}
	
	public JasperReportBuilder generarReporteSeccionInformacionBasica(){
		JasperReportBuilder signosVitales = report();
		ComponentBuilder<?, ?> componentBuilder = null;
		HorizontalListBuilder signosVitalesSeccion = null;

		List<HorizontalListBuilder> listaHorizonatBuilder= new ArrayList<HorizontalListBuilder>();
		ComponentBuilder[] listaComponentesAPintar;
		GeneradorDisenoSubReportes generadorDiseñoSubReportes = new GeneradorDisenoSubReportes();
		TextColumnBuilder []cols = new TextColumnBuilder[6];
		
		TextColumnBuilder<String>     tas = col.column(messageResource.getMessage("historia_clinica_columna_tipoAntecedente"),  "tipoAntecedente",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(9).setBorder(stl.pen1Point().setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     tad = col.column(messageResource.getMessage("historia_clinica_columna_tipoAntecedente"),  "tipoAntecedente",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(9).setBorder(stl.pen1Point().setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     tam = col.column(messageResource.getMessage("historia_clinica_columna_tipoAntecedente"),  "tipoAntecedente",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(9).setBorder(stl.pen1Point().setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     fc = col.column(messageResource.getMessage("historia_clinica_columna_tipoAntecedente"),  "tipoAntecedente",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(9).setBorder(stl.pen1Point().setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     fr = col.column(messageResource.getMessage("historia_clinica_columna_tipoAntecedente"),  "tipoAntecedente",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(9).setBorder(stl.pen1Point().setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     t = col.column(messageResource.getMessage("historia_clinica_columna_tipoAntecedente"),  "tipoAntecedente",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(9).setBorder(stl.pen1Point().setLineColor(Color.BLACK)));
		



		signosVitalesSeccion=	cmp.horizontalList(cmp.verticalList(
				cmp.text(messageResource.getMessage("historia_clinica_lable_valoracion_signosVitales")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloTitulo).setHorizontalAlignment(HorizontalAlignment.LEFT))
		)
		).setStyle(stl.style().setBorder(stl.pen1Point().setLineColor(Color.BLACK)).setPadding(2))	;
		listaHorizonatBuilder.add(signosVitalesSeccion);

		listaComponentesAPintar = new ComponentBuilder[listaHorizonatBuilder.size()];
		for (int i = 0; i < listaComponentesAPintar.length; i++) {
			listaComponentesAPintar[i]=listaHorizonatBuilder.get(i);
		}


		componentBuilder = cmp.verticalList(listaComponentesAPintar);
		signosVitales.summary(componentBuilder)
		.setTemplate(generadorDiseñoSubReportes.crearPlantillaReporte()).build();

		return signosVitales;
	}
}
