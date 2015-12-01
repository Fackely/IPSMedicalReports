package com.servinte.axioma.generadorReporte.historiaClinica;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

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

public class GeneradorSubReporteNotasEnfermeria {

	/**
	 * Mensajes parametrizados de los reportes 
	 */
	private static MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.historiaclinica.impresionHistoriaClinica");
	
	public GeneradorSubReporteNotasEnfermeria() {
	}

	

	public   JasperReportBuilder notasEnfermiria(DtoImpresionHistoriaClinica dto
			,UsuarioBasico usuario, PersonaBasica paciente){
		JasperReportBuilder notasEnfermiria = report();
		
		ComponentBuilder<?, ?> componentBuilder = null;
		List<ComponentBuilder> listaComponentes = new ArrayList<ComponentBuilder>();
		ComponentBuilder[] componentesTotales;
		HorizontalListBuilder tituloSeccion = null;
		
		tituloSeccion=	cmp.horizontalList(cmp.verticalList(cmp.horizontalList(
				cmp.text(messageResource.getMessage("historia_clinica_lable_notas_enfermeria")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloTituloUnderline).underline().setHorizontalAlignment(HorizontalAlignment.LEFT))
		)))	;
		listaComponentes.add(tituloSeccion);
		
		TextColumnBuilder<String>     fecha = col.column("Fecha/hora",  "fecha",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     anotaciones = col.column("Anotaciones Enfermería",  "anotaciones",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     usuarioAnotacion = col.column("Usuario",  "usuarioAnotacion",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		
		
		componentBuilder = cmp.verticalList(tituloSeccion);
		notasEnfermiria
		.summary(componentBuilder)
		.setPageMargin(crearMagenesReporte())
		.summary(cmp.subreport(notasEnfermiriaTable(dto, usuario, paciente)))
		.build();
		
		
		
		return notasEnfermiria;
		
	}
	
	public   JasperReportBuilder notasEnfermiriaTable(DtoImpresionHistoriaClinica dto
			,UsuarioBasico usuario, PersonaBasica paciente){
		JasperReportBuilder notasEnfermiria = report();
		
		ComponentBuilder<?, ?> componentBuilder = null;
		List<ComponentBuilder> listaComponentes = new ArrayList<ComponentBuilder>();
		ComponentBuilder[] componentesTotales;
		HorizontalListBuilder tituloSeccion = null;
		GeneradorDisenoSubReportes generadorDisenoSubReportes = new GeneradorDisenoSubReportes();
		
		
		TextColumnBuilder<String>     fecha = col.column("Fecha/hora",  "fecha",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     anotaciones = col.column("Anotaciones Enfermería",  "anotaciones",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     usuarioAnotacion = col.column("Usuario",  "usuarioAnotacion",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		
		
		notasEnfermiria
		.columns(fecha.setWidth(40).setStretchWithOverflow(true),anotaciones.setWidth(250).setStretchWithOverflow(true),usuarioAnotacion.setWidth(45).setStretchWithOverflow(true))
		.setDataSource(crearDatasourceConsumoInsumos(dto.getNotasEnfermeria()))
		.setTemplate(generadorDisenoSubReportes.crearPlantillaReporte())
		.setDetailSplitType(SplitType.PREVENT)
		.build();
		
		
		
		return notasEnfermiria;
		
	}
	
	public JRDataSource crearDatasourceConsumoInsumos (Collection notas){
		
		ArrayList<HashMap> notasEnfermeria = (ArrayList<HashMap>) notas;
		
		
		
		
		
		DataSource dataSource = new DataSource("fecha","anotaciones","usuarioAnotacion");
			for (int i = 0; i <notasEnfermeria.size(); i++) {
				dataSource.add(String.valueOf(notasEnfermeria.get(i).get("fecha_hora_reg")),String.valueOf(notasEnfermeria.get(i).get("anotacion")),String.valueOf(notasEnfermeria.get(i).get("nombre_usuario")));
			}
		return dataSource;
		
	}
	
	/**
	 * Creacion de margenes del reporte
	 * @return MarginBuilder
	 */
	public MarginBuilder crearMagenesReporte()
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
