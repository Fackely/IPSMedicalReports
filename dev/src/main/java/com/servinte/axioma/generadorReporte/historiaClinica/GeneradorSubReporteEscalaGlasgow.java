package com.servinte.axioma.generadorReporte.historiaClinica;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;
import static net.sf.dynamicreports.report.builder.DynamicReports.grid;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.LineStyle;
import net.sf.dynamicreports.report.constant.SplitType;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.jasperreports.engine.JRDataSource;

import org.apache.struts.util.MessageResources;

import util.Utilidades;
import util.reportes.dinamico.DataSource;
import util.reportes.dinamico.EstilosReportesDinamicosHistoriaClinica;

import com.princetonsa.dto.historiaClinica.enfermeria.escalaGlasgow.DtoEscalaGlasgow;
import com.princetonsa.dto.historiaClinica.enfermeria.hojaNeurologica.DtoControlEsfinteres;
import com.princetonsa.dto.historiaClinica.enfermeria.hojaNeurologica.DtoConvulsion;
import com.princetonsa.dto.historiaClinica.enfermeria.hojaNeurologica.DtoFuerzaMuscular;
import com.princetonsa.dto.historiaClinica.enfermeria.hojaNeurologica.DtoPupila;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

public class GeneradorSubReporteEscalaGlasgow {
		/**
	 * Mensajes parametrizados de los reportes 
	 */
	private static MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.historiaclinica.impresionHistoriaClinica");
	
	/**
	 * 
	 * @param dto
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	public JasperReportBuilder generarSubReporteEscalaGlasgow(DtoImpresionHistoriaClinica dto, UsuarioBasico usuario, PersonaBasica paciente) {
		VerticalListBuilder list = cmp.verticalList();
		JasperReportBuilder reporteEscalaGlasgow = report();
		
		HorizontalListBuilder tituloSeccion = cmp.horizontalList(cmp.verticalList(cmp.horizontalList(cmp.text(messageResource.getMessage("historia_clinica_lable_escala_glasgow")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloTituloUnderline).underline().setHorizontalAlignment(HorizontalAlignment.LEFT)))));
		list.add(cmp.verticalList(tituloSeccion));
		
		list.add(cmp.subreport(generarSubReporteEscalaGlasgow(dto.getHistoricoEscalaGlasgowList())));
		
		reporteEscalaGlasgow.addSummary(list);
		reporteEscalaGlasgow.build();
		
		return reporteEscalaGlasgow;
	}
	
	private JasperReportBuilder generarSubReporteEscalaGlasgow(List<DtoEscalaGlasgow> historicoEscalaGlasgowList) {
		JasperReportBuilder reporteEscalaGlasgow = report();
		
		GeneradorDisenoSubReportes generadorDisenoSubReportes = new GeneradorDisenoSubReportes();
				
		TextColumnBuilder<String> fechaHora = col.column("Fecha/hora", "fechaHora", type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String> aperturaOjos = col.column("Apertura Ojos", "aperturaOjos", type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String> respuestaVerbal = col.column("Respuesta Verbal", "respuestaVerbal", type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String> respuestaMotora = col.column("Respuesta Motora", "respuestaMotora", type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String> clasificacionGlasgow = col.column("Clasificacion Glasgow", "clasificacionGlasgow", type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String> usuarioCol = col.column("Usuario", "usuario", type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));

		reporteEscalaGlasgow
		.columns(fechaHora.setStretchWithOverflow(true), aperturaOjos.setStretchWithOverflow(true), respuestaVerbal.setStretchWithOverflow(true), 
				respuestaMotora.setStretchWithOverflow(true), clasificacionGlasgow.setStretchWithOverflow(true), usuarioCol.setStretchWithOverflow(true))
		.setDataSource(crearDataSourceEscalaGlasgow(historicoEscalaGlasgowList))
		.setTemplate(generadorDisenoSubReportes.crearPlantillaReporte())
		.setDetailSplitType(SplitType.PREVENT)
		.setPageMargin(generadorDisenoSubReportes.crearMagenesSubReporte())
		.build();
		
		return reporteEscalaGlasgow;
	}
	
	private JRDataSource crearDataSourceEscalaGlasgow (List<DtoEscalaGlasgow> historicoEscalaGlasgowList) {
		DataSource dataSource = new DataSource("fechaHora", "aperturaOjos", "respuestaVerbal", "respuestaMotora", "clasificacionGlasgow", "usuario");
		for (DtoEscalaGlasgow h : historicoEscalaGlasgowList) {
			dataSource.add(h.getFechaHora(),
					h.getAperturaOjos(),
					h.getRespuestaVerbal(),
					h.getRespuestaMotora(),
					h.getEscalaGlasgow(),
					h.getUsuario());
		}
		return dataSource;
	}
	
	
}
