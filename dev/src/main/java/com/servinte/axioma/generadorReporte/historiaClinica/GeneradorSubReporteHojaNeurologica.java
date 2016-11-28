package com.servinte.axioma.generadorReporte.historiaClinica;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;
import static net.sf.dynamicreports.report.builder.DynamicReports.grid;

import java.awt.Color;
import java.util.List;

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

import util.reportes.dinamico.DataSource;
import util.reportes.dinamico.EstilosReportesDinamicosHistoriaClinica;

import com.princetonsa.dto.historiaClinica.enfermeria.hojaNeurologica.DtoControlEsfinteres;
import com.princetonsa.dto.historiaClinica.enfermeria.hojaNeurologica.DtoConvulsion;
import com.princetonsa.dto.historiaClinica.enfermeria.hojaNeurologica.DtoFuerzaMuscular;
import com.princetonsa.dto.historiaClinica.enfermeria.hojaNeurologica.DtoPupila;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

public class GeneradorSubReporteHojaNeurologica {
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
	public JasperReportBuilder generarSubReporteHojaNeurologica(DtoImpresionHistoriaClinica dto, UsuarioBasico usuario, PersonaBasica paciente) {
		VerticalListBuilder list = cmp.verticalList();
		JasperReportBuilder reporteHojaNeurologica = report();
		
		HorizontalListBuilder tituloSeccion = cmp.horizontalList(cmp.verticalList(cmp.horizontalList(cmp.text(messageResource.getMessage("historia_clinica_lable_hoja_neurologica")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloTituloUnderline).underline().setHorizontalAlignment(HorizontalAlignment.LEFT)))));
		list.add(cmp.verticalList(tituloSeccion));
		
		list.add(cmp.subreport(generarSeccionPupilas(dto.getPupilaDerechaList(), dto.getPupilaIzquierdaList())));
		list.add(cmp.subreport(generarSeccionConvulsiones(dto.getConvulsiones())));
		list.add(cmp.subreport(generarSeccionControlEsfinteres(dto.getControlEsfinteresList())));
		list.add(cmp.subreport(generarSeccionFuerzaMuscular(dto.getFuerzaMuscularList())));

		reporteHojaNeurologica.addSummary(list);
		reporteHojaNeurologica.build();
		
		return reporteHojaNeurologica;
	}
	
	/*
	 * SECCION PUPILAS
	 */
	
	private JasperReportBuilder generarSeccionPupilas(List<DtoPupila> pupilaDerechaList, List<DtoPupila> pupilaIzquierdaList) {
		JasperReportBuilder subReporteSeccionPupilas = report();
		JasperReportBuilder subReporteSeccionPupilasDerecha = report();
		JasperReportBuilder subReporteSeccionPupilasIzquierda = report();
		
		GeneradorDisenoSubReportes generadorDisenoSubReportes = new GeneradorDisenoSubReportes();
		
		TextFieldBuilder<String> subtituloSeccionPupilas = cmp.text("Pupilas").setStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		
		TextFieldBuilder<String> subtituloSeccionPupilaDerecha = cmp.text("Pupila Derecha").setStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextFieldBuilder<String> subtituloSeccionPupilaIzquierda = cmp.text("Pupila Izquierda").setStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		
		TextColumnBuilder<String> fechaHora = col.column("Fecha/hora", "fechaHora", type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String> tamanio = col.column("Tamaño", "tamanio", type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String> reaccion = col.column("Reacción", "reaccion", type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String> usuario = col.column("Usuario", "usuario", type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String> observaciones = col.column("observaciones", type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
				
		subReporteSeccionPupilasDerecha
		.addTitle(subtituloSeccionPupilaDerecha)
		.columns(fechaHora.setStretchWithOverflow(true),tamanio.setStretchWithOverflow(true),reaccion.setStretchWithOverflow(true),usuario.setStretchWithOverflow(true),observaciones)
		.columnGrid(
				grid.horizontalColumnGridList().add(fechaHora, tamanio, reaccion, usuario).newRow()
				.add(observaciones)
				)
		.setDataSource(crearDataSourceSeccionPupilas(pupilaDerechaList))
		.setTemplate(generadorDisenoSubReportes.crearPlantillaReporte())
		.setDetailSplitType(SplitType.PREVENT)
		.setPageMargin(generadorDisenoSubReportes.crearMagenesSubReporte());

		subReporteSeccionPupilasIzquierda
		.addTitle(subtituloSeccionPupilaIzquierda)
		.columns(fechaHora.setStretchWithOverflow(true),tamanio.setStretchWithOverflow(true),reaccion.setStretchWithOverflow(true),usuario.setStretchWithOverflow(true),observaciones)
		.columnGrid(
				grid.horizontalColumnGridList().add(fechaHora, tamanio, reaccion, usuario).newRow()
				.add(observaciones)
				)
		.setDataSource(crearDataSourceSeccionPupilas(pupilaIzquierdaList))
		.setTemplate(generadorDisenoSubReportes.crearPlantillaReporte())
		.setDetailSplitType(SplitType.PREVENT)
		.setPageMargin(generadorDisenoSubReportes.crearMagenesSubReporte());
		
		subReporteSeccionPupilas
		.addTitle(subtituloSeccionPupilas)
		.summary(cmp.subreport(subReporteSeccionPupilasDerecha), cmp.subreport(subReporteSeccionPupilasIzquierda))
		.setTemplate(generadorDisenoSubReportes.crearPlantillaReporte())
		.setDetailSplitType(SplitType.PREVENT)
		.setPageMargin(generadorDisenoSubReportes.crearMagenesSubReporte())
		.build();
				
		return subReporteSeccionPupilas;
	}

	private JRDataSource crearDataSourceSeccionPupilas (List<DtoPupila> pupilas) {
		DataSource dataSource = new DataSource("fechaHora", "tamanio", "reaccion", "usuario", "observaciones");
		for (DtoPupila p : pupilas) {
			dataSource.add(p.getFechaRegistro() + " - " + p.getHoraRegistro(),
					p.getValorTamanio() + "-" + p.getAbreviaturaTamanio() + " " + p.getNombreTamanio(),
					p.getNombreReaccion(),
					p.getUsuario(),
					"Observaciones: " + p.getObservaciones());
		}
		return dataSource;
	}
	
	/*
	 * SECCION CONVULSIONES
	 */
	
	private JasperReportBuilder generarSeccionConvulsiones(List<DtoConvulsion> convulsiones) {
		JasperReportBuilder subReporteSeccionConvulsiones = report();
		
		GeneradorDisenoSubReportes generadorDisenoSubReportes = new GeneradorDisenoSubReportes();
		
		HorizontalListBuilder subtituloSeccion = cmp.horizontalList(cmp.verticalList(cmp.horizontalList(cmp.text("Convulsiones").setStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK))))));
		
		TextColumnBuilder<String> fechaHora = col.column("Fecha/hora", "fechaHora", type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String> resultado = col.column("Resultado", "resultado", type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String> usuario = col.column("Usuario", "usuario", type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String> observaciones = col.column("observaciones", type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		
		subReporteSeccionConvulsiones
		.addTitle(subtituloSeccion)
		.columns(fechaHora.setStretchWithOverflow(true),resultado.setStretchWithOverflow(true),usuario.setStretchWithOverflow(true),observaciones)
		.columnGrid(
				grid.horizontalColumnGridList().add(fechaHora, resultado, usuario).newRow()
				.add(observaciones)
				)
		.setDataSource(crearDataSourceSeccionConvulsiones(convulsiones))
		.setTemplate(generadorDisenoSubReportes.crearPlantillaReporte())
		.setDetailSplitType(SplitType.PREVENT)
		.setPageMargin(generadorDisenoSubReportes.crearMagenesSubReporte())
		.build();

		return subReporteSeccionConvulsiones; 
	}

	private JRDataSource crearDataSourceSeccionConvulsiones (List<DtoConvulsion> convulsiones) {
		DataSource dataSource = new DataSource("fechaHora", "resultado", "usuario", "observaciones");
		for (DtoConvulsion c : convulsiones) {
			dataSource.add(c.getFecha() + " - " + c.getHora(),
					c.getTipoConvulsion(),
					c.getUsuario(),
					"Observaciones: " + c.getObservacion());
		}
		return dataSource;
	}
	
	/*
	 * SECCION CONTROL ESFINTERES
	 */
	
	private JasperReportBuilder generarSeccionControlEsfinteres (List<DtoControlEsfinteres> controlEsfinteresList) {
		JasperReportBuilder subReporteSeccionControlEsfinteres = report();
		
		GeneradorDisenoSubReportes generadorDisenoSubReportes = new GeneradorDisenoSubReportes();
		
		HorizontalListBuilder subtituloSeccion = cmp.horizontalList(cmp.verticalList(cmp.horizontalList(cmp.text("Control Esfínteres").setStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK))))));
		
		TextColumnBuilder<String> fechaHora = col.column("Fecha/hora", "fechaHora", type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String> resultado = col.column("Resultado", "resultado", type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String> usuario = col.column("Usuario", "usuario", type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String> observaciones = col.column("observaciones", type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		
		subReporteSeccionControlEsfinteres
		.addTitle(subtituloSeccion)
		.columns(fechaHora.setStretchWithOverflow(true),resultado.setStretchWithOverflow(true),usuario.setStretchWithOverflow(true),observaciones)
		.columnGrid(
				grid.horizontalColumnGridList().add(fechaHora, resultado, usuario).newRow()
				.add(observaciones)
				)
		.setDataSource(crearDataSourceSeccionControlEsfinteres(controlEsfinteresList))
		.setTemplate(generadorDisenoSubReportes.crearPlantillaReporte())
		.setDetailSplitType(SplitType.PREVENT)
		.setPageMargin(generadorDisenoSubReportes.crearMagenesSubReporte())
		.build();

		return subReporteSeccionControlEsfinteres; 
	}

	private JRDataSource crearDataSourceSeccionControlEsfinteres (List<DtoControlEsfinteres> controlEsfinteresList) {
		DataSource dataSource = new DataSource("fechaHora", "resultado", "usuario", "observaciones");
		for (DtoControlEsfinteres c : controlEsfinteresList) {
			dataSource.add(c.getFecha() + " - " + c.getHora(),
					c.getAusente() ? "Ausente - " + c.getCtrlEsfinter() : c.getCtrlEsfinter(),
					c.getUsuario(),
					"Observaciones: " + c.getObservaciones());
		}
		return dataSource;
	}
	
	/*
	 * SECCION FUERZA MUSCULAR
	 */
	
	private JasperReportBuilder generarSeccionFuerzaMuscular(List<DtoFuerzaMuscular> fuerzaMuscularList) {
		JasperReportBuilder subReporteSeccionFuerzaMuscular = report();
		JasperReportBuilder subReporteSeccionFuerzaMuscularSuperior = report();
		JasperReportBuilder subReporteSeccionFuerzaMuscularInferior = report();
		
		GeneradorDisenoSubReportes generadorDisenoSubReportes = new GeneradorDisenoSubReportes();
		
		HorizontalListBuilder subtituloSeccionFuerzaMuscular = cmp.horizontalList(cmp.verticalList(cmp.horizontalList(cmp.text("Fuerza Muscular").setStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK))))));
		
		HorizontalListBuilder subtituloSeccionFuerzaMuscularSuperior = cmp.horizontalList(cmp.verticalList(cmp.horizontalList(cmp.text("Miembro Superior").setStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK))))));
		HorizontalListBuilder subtituloSeccionFuerzaMuscularInferior = cmp.horizontalList(cmp.verticalList(cmp.horizontalList(cmp.text("Miembro Inferior").setStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK))))));
		
		TextColumnBuilder<String> fechaHora = col.column("Fecha/hora", "fechaHora", type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String> miembro = col.column("Miembro", "miembro", type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String> resultado = col.column("Resultado", "resultado", type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String> usuario = col.column("Usuario", "usuario", type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String> observaciones = col.column("observaciones", type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		
		subReporteSeccionFuerzaMuscularSuperior
		.addTitle(subtituloSeccionFuerzaMuscularSuperior)
		.columns(fechaHora.setStretchWithOverflow(true),miembro.setStretchWithOverflow(true),resultado.setStretchWithOverflow(true),usuario.setStretchWithOverflow(true),observaciones)
		.columnGrid(
				grid.horizontalColumnGridList().add(fechaHora, miembro, resultado, usuario).newRow()
				.add(observaciones)
				)
		.setDataSource(crearDataSourceSeccionFuerzaMuscular(fuerzaMuscularList, "SUP"))
		.setTemplate(generadorDisenoSubReportes.crearPlantillaReporte())
		.setDetailSplitType(SplitType.PREVENT)
		.setPageMargin(generadorDisenoSubReportes.crearMagenesSubReporte());

		subReporteSeccionFuerzaMuscularInferior
		.addTitle(subtituloSeccionFuerzaMuscularInferior)
		.columns(fechaHora.setStretchWithOverflow(true),miembro.setStretchWithOverflow(true),resultado.setStretchWithOverflow(true),usuario.setStretchWithOverflow(true),observaciones)
		.columnGrid(
				grid.horizontalColumnGridList().add(fechaHora, miembro, resultado, usuario).newRow()
				.add(observaciones)
				)
		.setDataSource(crearDataSourceSeccionFuerzaMuscular(fuerzaMuscularList, "INF"))
		.setTemplate(generadorDisenoSubReportes.crearPlantillaReporte())
		.setDetailSplitType(SplitType.PREVENT)
		.setPageMargin(generadorDisenoSubReportes.crearMagenesSubReporte());
		
		subReporteSeccionFuerzaMuscular
		.addTitle(subtituloSeccionFuerzaMuscular)
		.addSummary(cmp.subreport(subReporteSeccionFuerzaMuscularSuperior))
		.addSummary(cmp.subreport(subReporteSeccionFuerzaMuscularInferior))
		.setTemplate(generadorDisenoSubReportes.crearPlantillaReporte())
		.setDetailSplitType(SplitType.PREVENT)
		.setPageMargin(generadorDisenoSubReportes.crearMagenesSubReporte())
		.build();
				
		return subReporteSeccionFuerzaMuscular;
	}

	private JRDataSource crearDataSourceSeccionFuerzaMuscular (List<DtoFuerzaMuscular> fuerzaMuscularList, String miembro) {
		DataSource dataSource = new DataSource("fechaHora", "miembro", "resultado", "usuario", "observaciones");
		for (DtoFuerzaMuscular f : fuerzaMuscularList) {
			if (f.getMiembro().equals(miembro)) {
				dataSource.add(f.getFecha() + " - " + f.getHora(),
					f.getCostado(),
					f.getResultado(),
					f.getUsuario(),
					"Observaciones: " + f.getObservaciones());
			}
		}
		return dataSource;
	}
}
