package com.servinte.axioma.generadorReporte.historiaClinica;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.awt.Color;
import java.util.HashMap;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.MarginBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.LineStyle;
import net.sf.dynamicreports.report.constant.SplitType;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.jasperreports.engine.JRDataSource;

import org.apache.struts.util.MessageResources;

import util.reportes.dinamico.DataSource;
import util.reportes.dinamico.EstilosReportesDinamicosHistoriaClinica;

import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;


public class GeneradorSubReporteAntecentesVarios {
	
	/**
	 * Mensajes parametrizados de los reportes 
	 */
	private static MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.historiaclinica.impresionHistoriaClinica");
	
	
	public GeneradorSubReporteAntecentesVarios() {
		// TODO Auto-generated constructor stub
	}
	
	
	public   JasperReportBuilder otrosAntecedentes(DtoImpresionHistoriaClinica dto
			,UsuarioBasico usuario, PersonaBasica paciente){
		JasperReportBuilder antecedentesVarios = report();

		ComponentBuilder<?, ?> componentBuilder = null;
		HorizontalListBuilder tituloSeccion = null;
		GeneradorDisenoSubReportes generadorDiseñoSubReportes = new GeneradorDisenoSubReportes();
		
		tituloSeccion=	cmp.horizontalList(cmp.verticalList(cmp.horizontalList(
				cmp.text("Antecedentes").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloTituloUnderline).underline().setHorizontalAlignment(HorizontalAlignment.LEFT))
		)))	;

		Integer tamano = Integer.valueOf(String.valueOf(dto.getAntecedentes().get("numRegistros")));
		if (tamano>0) {
			componentBuilder = cmp.verticalList(tituloSeccion);
			antecedentesVarios
			.summary(componentBuilder)
			.summary(cmp.subreport(otrosAntecedentesDetail(dto, usuario, paciente)))
			.setDataSource(crearDatasourceAntecedentes(dto.getAntecedentes()))
			.setPageMargin(crearMagenesSubReporte())
			.setTemplate(generadorDiseñoSubReportes.crearPlantillaReporte());
		}
		
		antecedentesVarios.build();
		
		return  antecedentesVarios;

	}
	
	
	public   JasperReportBuilder otrosAntecedentesDetail(DtoImpresionHistoriaClinica dto
			,UsuarioBasico usuario, PersonaBasica paciente){
		JasperReportBuilder antecedentesVarios = report();

		ComponentBuilder<?, ?> componentBuilder = null;
		HorizontalListBuilder tituloSeccion = null;
		GeneradorDisenoSubReportes generadorDisenoSubReportes = new GeneradorDisenoSubReportes();

		TextColumnBuilder<String>     fecha = col.column("Fecha",  "fecha",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     hora = col.column("Hora",  "hora",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     nombre = col.column("Antecedente",  "nombre",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     descripcion = col.column("Descripción",  "descripcion",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));

		Integer tamano = Integer.valueOf(String.valueOf(dto.getAntecedentes().get("numRegistros")));
		if (tamano>0) {
			antecedentesVarios
			.columns(fecha.setWidth(30),hora.setWidth(30),nombre.setWidth(75),descripcion)
			.setDataSource(crearDatasourceAntecedentes(dto.getAntecedentes()))
			.setTemplate(generadorDisenoSubReportes.crearPlantillaReporte())
			.setPageMargin(generadorDisenoSubReportes.crearMagenesSubReporte())
			.setDetailSplitType(SplitType.PREVENT)
			;
		}
		
		antecedentesVarios.build();
		
		return  antecedentesVarios;

	}
	
	
	
	public JRDataSource crearDatasourceAntecedentes(HashMap  antecedentes){
		
		Integer tamano = Integer.valueOf(String.valueOf(antecedentes.get("numRegistros")));
		
		DataSource dataSource = new DataSource("fecha","hora","nombre","descripcion");
			for (int i = 0; i <tamano; i++) {
				dataSource.add(String.valueOf(antecedentes.get("fecha_"+i)),
						String.valueOf(antecedentes.get("hora_"+i)),
						String.valueOf(antecedentes.get("nombre_"+i)),
						String.valueOf(antecedentes.get("descripcion_"+i)));
			}
		return dataSource;
		
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

	
}
