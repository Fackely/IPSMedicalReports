package com.servinte.axioma.generadorReporte.historiaClinica;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

import java.awt.Color;
import java.util.HashMap;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.MarginBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import util.reportes.dinamico.EstilosReportesDinamicosHistoriaClinica;

import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.generadorReporteHistoriaClinica.comun.IConstantesReporteHistoriaClinica;

public class GeneradorSubResumenParcialHC {


	public   JasperReportBuilder reporteResumenParcialHC(HashMap mapaResumen,  UsuarioBasico usuario, PersonaBasica paciente){
		JasperReportBuilder reportResumenParcialHC = report();

		HorizontalListBuilder itemComponent=null;

		TextFieldBuilder<String> titulo=cmp.text(IConstantesReporteHistoriaClinica.constanteTituloResumenParcial).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setBackgroundColor(Color.LIGHT_GRAY).setHorizontalAlignment(HorizontalAlignment.CENTER));
		TextFieldBuilder<String> vacio=cmp.text("").setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT));
		Integer tamanio=Integer.parseInt(String.valueOf(mapaResumen.get("numRegistros")));


		if(tamanio>0){
			//Se inicializa la lista con el titulo
			itemComponent=cmp.horizontalList(titulo);
			for (int i = 0; i < tamanio; i++) {
				titulo=cmp.text(IConstantesReporteHistoriaClinica.constanteMensajeFecha).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
				TextFieldBuilder<String>  contenido=cmp.text(String.valueOf(mapaResumen.get("fecha_"+i)).split(" ")[0]).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
				HorizontalListBuilder itemComponent1=cmp.horizontalList(titulo);
				itemComponent1.add(contenido);
				

				titulo=cmp.text(IConstantesReporteHistoriaClinica.constanteMensajeHora).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
				contenido=cmp.text(String.valueOf(mapaResumen.get("hora_"+i))).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
				itemComponent1.add(titulo);
				itemComponent1.add(contenido);
				itemComponent1.add(vacio);
				itemComponent1.add(vacio);
				itemComponent1.add(vacio);
				itemComponent1.add(vacio);
				itemComponent1.add(vacio);
				itemComponent1.add(vacio);
				itemComponent1.add(vacio);
				itemComponent1.add(vacio);
				itemComponent1.add(vacio);
				itemComponent1.add(vacio);
				
				
				itemComponent.newRow().add(itemComponent1.setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBorde));


				String datosMedico=String.valueOf(mapaResumen.get("profesional_"+i))+" "+
								   String.valueOf(mapaResumen.get("registromedico_"+i))+" "+
								   String.valueOf(mapaResumen.get("infomedico_"+i));
				
				contenido=cmp.text(String.valueOf(mapaResumen.get("notas_"+i))+"\n\n"+ datosMedico ).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
				itemComponent1=cmp.horizontalList(contenido);
				itemComponent.newRow().add(itemComponent1.setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBorde));



			}
		}

		if(itemComponent!=null){
			reportResumenParcialHC
			.summary(itemComponent)
			.setPageMargin(crearMagenesSubReporte())
			.build();
		}
		return reportResumenParcialHC;

	}

	/**
	 * @return Margenes de reporte 
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
}
