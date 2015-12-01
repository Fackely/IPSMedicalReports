package com.servinte.axioma.generadorReporte.tesoreria.notasPacientes;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.template;

import java.awt.Color;
import java.util.Locale;
import java.util.Map;

import net.sf.dynamicreports.report.builder.MarginBuilder;
import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.constant.StretchType;
import net.sf.dynamicreports.report.constant.VerticalAlignment;

import org.apache.struts.util.MessageResources;

import util.ConstantesIntegridadDominio;
import util.UtilidadTexto;
import util.reportes.dinamico.EstilosReportesDinamicos;

import com.servinte.axioma.generadorReporte.comun.IConstantesReporte;

public class GeneradorDisenioReporte {
	/** * Contiene los mensajes correspondiente a este Generador*/
	MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.tesoreria.NotasPacientesForm");
	
	/**
	 * Atributo que representa un espacio
	 */
	private static final String espacio=" ";
	
	/**
	 * Atributo que representa 4 espacio 
	 */
	private static final String espacioLargo="    ";
	
	/**
	 * Contructor de la clase
	 */
	public GeneradorDisenioReporte(){
		
	}

	
	
	/**
	 * Crea el componente de pie de página según el formato 
	 * definido para el reporte.
	 * 
	 * @param parmas
	 * @return ComponentBuilder
	 */
	public ComponentBuilder<?, ?> crearComponentePiePagina(Map<String, String> params)
	{
		return	cmp.horizontalList(cmp.text(messageResource.getMessage("notaPaciente.reporte.footer.impresion")+
										   espacio+params.get(IConstantesReporte.fechaProcesa)+espacioLargo
											+params.get(IConstantesReporte.usuarioProceso)+" ("
											+params.get(IConstantesReporte.loginUsuarioProceso)+")")
										.setStyle(EstilosReportesDinamicos.estiloLetraFooter)	
										.setStretchWithOverflow(true)
										.setHorizontalAlignment(HorizontalAlignment.CENTER));
	}
	
	/**
	 * Crea el componente de pie de página según el formato 
	 * definido para el reporte.
	 * 
	 * @param parmas
	 * @return ComponentBuilder
	 */
	@SuppressWarnings("rawtypes")
	public ComponentBuilder<?, ?> crearComponenteInfoUsuario(Map<String, String> params)	{
		ComponentBuilder<?, ?> dynamicReportsComponent = null;

		StyleBuilder columnStyle 			= stl.style().setPadding(1).setFontSize(7).bold()
												.setBorder(stl.pen1Point().setLineColor(Color.BLACK))
											  	.setVerticalAlignment(VerticalAlignment.MIDDLE).setPadding(2);
		
		StyleBuilder columnStyleWithoutBorder= stl.style().setPadding(1).setFontSize(7)
	  											.setVerticalAlignment(VerticalAlignment.MIDDLE).setPadding(2);
		
		StyleBuilder negrita 			= stl.style().setPadding(1).setFontSize(7)
	  										.setVerticalAlignment(VerticalAlignment.MIDDLE).setPadding(2).bold();
	  	
		
		String linea1		= messageResource.getMessage("notasPaciente.reporte.titulo.informacionPaciente");
		String linea2		= messageResource.getMessage("notasPaciente.labelPaciente") + ":" + espacioLargo + 
								params.get(IConstantesReporte.nombrepaciente);
		String linea3		= messageResource.getMessage("notasPaciente.labelIdPaciente") + ":" + espacioLargo +
								params.get(IConstantesReporte.tipoNumeroID);
		String linea4		= messageResource.getMessage("notasPaciente.labelCentroAtencionDueno") + ":" + espacioLargo +
			params.get(IConstantesReporte.centroAtencionDuenio);
		
		ComponentBuilder titulo;
		ComponentBuilder infoPaciente;
		ComponentBuilder infoCentroAtencionDuenio;
		
		/*titulo = cmp.horizontalFlowList(cmp.text(linea1).setStyle(EstilosReportesDinamicos.estiloTituloSombreadoSuaveLBorder));
		infoPaciente = cmp.horizontalFlowList(
				cmp.horizontalFlowList(cmp.text("Paciente: ").setStyle(negrita)),
				cmp.filler().setWidth(50),
				cmp.horizontalFlowList(cmp.text(linea2).setStyle(columnStyleWithoutBorder)),
				cmp.horizontalFlowList(cmp.text("Identificación: ").setStyle(negrita)),
				cmp.filler().setWidth(50),
				cmp.horizontalFlowList(cmp.text(linea3).setStyle(columnStyleWithoutBorder))).setStyle(columnStyle);
		infoCentroAtencionDuenio = cmp.horizontalFlowList(
				cmp.horizontalFlowList(cmp.text("Centro Atención Dueño Paciente:").setStyle(negrita)),
				cmp.filler().setWidth(50),
				cmp.horizontalFlowList(cmp.text(linea4).setStyle(columnStyleWithoutBorder))).setStyle(columnStyle);*/
		
		titulo = cmp.horizontalList(cmp.text(linea1).setStyle(EstilosReportesDinamicos.estiloTituloSombreadoSuaveLBorder));
		infoPaciente = cmp.horizontalList(cmp.text(linea2).setStyle(columnStyle),
				cmp.text(linea3).setStyle(columnStyle));
		infoCentroAtencionDuenio = cmp.horizontalList(cmp.text(linea4).setStyle(columnStyle));
			
		dynamicReportsComponent = cmp.verticalList(titulo, infoPaciente, infoCentroAtencionDuenio);
		
		return dynamicReportsComponent;
	}
	
	
	/**
	 * Crea el componente del encabezado (títulos y subtítulos) del reporte
	 * Consolidado de Notas pacientes para el formato A. 
	 * @return ComponentBuilder
	 */
	@SuppressWarnings("rawtypes")
	public ComponentBuilder<?, ?> crearComponenteEncabezadoConsolidadoNotasPacientesFormatoAyB(
			Map<String, String> params) 
	{
		ComponentBuilder<?, ?> dynamicReportsComponent = null;
		
		String lineaInst			= params.get(IConstantesReporte.nombreInstitucion);
		String lineaNit				= params.get(IConstantesReporte.nitInstitucion);
		String lineaActEconomica	= params.get(IConstantesReporte.actividadEconomica);
		String lineaTitulo			= messageResource.getMessage("notasPaciente.reporte.titulo.notasPacientes");
		
		ComponentBuilder encabezado;
		
		VerticalListBuilder verticalList;
		if (UtilidadTexto.isEmpty(lineaActEconomica)) {
			verticalList = cmp.verticalList(
					cmp.text(lineaInst).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
					cmp.text(lineaNit).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
					cmp.text(lineaTitulo).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER)
				).setStretchType(StretchType.RELATIVE_TO_TALLEST_OBJECT);
		} else {
			verticalList = cmp.verticalList(
					cmp.text(lineaInst).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
					cmp.text(lineaNit).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
					cmp.text(lineaActEconomica).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
					cmp.text(lineaTitulo).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER)
				).setStretchType(StretchType.RELATIVE_TO_TALLEST_OBJECT);
		}
		if(params.get(IConstantesReporte.ubicacionLogo).
				equals(ConstantesIntegridadDominio.acronimoUbicacionIzquierda)){
			//Componente del Logo
			encabezado=cmp.horizontalList(
							cmp.image(params.get(IConstantesReporte.rutaLogo))
									.setDimension(100, 80)
									.setStyle(stl.style().setLeftPadding(30))
									.setWidth(100),
							//Componente del Titulo y Subtitulos
							verticalList
			);
		}
		else{		
			//Componente del Logo
			encabezado=cmp.horizontalList(
							//Componente del Titulo y Subtitulos
							verticalList,
							cmp.image(params.get(IConstantesReporte.rutaLogo))
								.setDimension(100, 80)
								.setStyle(stl.style().setLeftPadding(30))
								.setWidth(100));
		}
		dynamicReportsComponent = 
			cmp.verticalList(encabezado);

		return dynamicReportsComponent;
	}
	
	
	/**
	 * Crea el componente del encabezado (títulos y subtítulos) del reporte
	 * Consolidado de Notas pacientes para el formato A. 
	 * @return ComponentBuilder
	 */
	@SuppressWarnings("rawtypes")
	public ComponentBuilder<?, ?> crearComponenteEncabezadoNotasPacientesFormatoAyB(
			Map<String, String> params) 
	{
		ComponentBuilder<?, ?> dynamicReportsComponent = null;
		
		String lineaInstitucion				= params.get(IConstantesReporte.nombreInstitucion);
		String lineaNit						= params.get(IConstantesReporte.nitInstitucion);
		String lineaActEconomica			= params.get(IConstantesReporte.actividadEconomica);
		String lineaCentroAtencionOrigen	= params.get(IConstantesReporte.centroAtencionOrigen);
		String lineaDireccion				= params.get(IConstantesReporte.direccionCentroAtencionOrigen);
		
		ComponentBuilder encabezado;
		
		VerticalListBuilder verticalList;
		if (UtilidadTexto.isEmpty(lineaActEconomica)) {
			verticalList = cmp.verticalList(
					cmp.text(lineaInstitucion).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
					cmp.text(lineaNit).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
					cmp.text(messageResource.getMessage("notasPaciente.labelCentrodeAtencionOrigen") + ": " + lineaCentroAtencionOrigen).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
					cmp.text(lineaDireccion).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER)
				).setStretchType(StretchType.RELATIVE_TO_TALLEST_OBJECT);
		} else {
			verticalList = cmp.verticalList(
					cmp.text(lineaInstitucion).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
					cmp.text(lineaNit).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
					cmp.text(lineaActEconomica).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
					cmp.text(messageResource.getMessage("notasPaciente.labelCentrodeAtencionOrigen") + ": " + lineaCentroAtencionOrigen).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
					cmp.text(lineaDireccion).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER)
				).setStretchType(StretchType.RELATIVE_TO_TALLEST_OBJECT);
		}
		if(params.get(IConstantesReporte.ubicacionLogo).
				equals(ConstantesIntegridadDominio.acronimoUbicacionIzquierda)){
			//Componente del Logo
			encabezado=cmp.horizontalList(
							cmp.image(params.get(IConstantesReporte.rutaLogo))
									.setDimension(100, 80)
									.setStyle(stl.style().setLeftPadding(30))
									.setWidth(100),
							//Componente del Titulo y Subtitulos
							verticalList
			);
		}
		else{		
			//Componente del Logo
			encabezado=cmp.horizontalList(
							//Componente del Titulo y Subtitulos
							verticalList,
							cmp.image(params.get(IConstantesReporte.rutaLogo))
								.setDimension(100, 80)
								.setStyle(stl.style().setLeftPadding(30))
								.setWidth(100));
		}
		dynamicReportsComponent = 
			cmp.verticalList(encabezado);

		return dynamicReportsComponent;
	}
	
	/**
	 * Crea la plantilla del reporte de Ordenes de Capitación Subcontratada para todos los formatos. 
	 * @return ReportTemplateBuilder
	 */
	public ReportTemplateBuilder crearPlantillaReporteConsolidado()
	{
		ReportTemplateBuilder reportTemplate;
		
		StyleBuilder rootStyle 				= stl.style().setPadding(1).setFontSize(7);
		StyleBuilder columnStyle 			= stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE).setPadding(2)
													.setBorder(stl.pen1Point().setLineColor(Color.BLACK));
		StyleBuilder columnTitleStyle		= stl.style(columnStyle)
										        .setBackgroundColor(new Color(225, 225, 225)).bold().setFontSize(7)
										        .setBorder(stl.pen1Point().setLineColor(Color.BLACK));
		StyleBuilder boldStyle          	= stl.style(rootStyle).bold();
		StyleBuilder groupStyle				= stl.style(boldStyle).setPadding(2).setFontSize(7)
													.setHorizontalAlignment(HorizontalAlignment.LEFT)
													.setBorder(stl.pen1Point().setLineColor(Color.BLACK));
		StyleBuilder subtotalStyle      	= stl.style().bold().setPadding(2).setFontSize(7).setHorizontalAlignment(HorizontalAlignment.RIGHT)
												 	.setBackgroundColor(new Color(230, 230, 230)).setBorder(stl.pen1Point().setLineColor(Color.BLACK));

		reportTemplate = template()
	        .setLocale(Locale.ENGLISH)
	        //Se define el tamano de la hoja y la orientación de la misma
	        .setPageFormat(PageType.LETTER, PageOrientation.LANDSCAPE)
	        .setColumnStyle(columnStyle)
	        .setColumnTitleStyle(columnTitleStyle)
	        .setGroupStyle(groupStyle)
	        .setGroupTitleStyle(groupStyle)
	        .setSubtotalStyle(subtotalStyle)
	    ;
		
		return reportTemplate;
	}

	/**
	 * Crea la plantilla del reporte de Ordenes de Capitación Subcontratada para todos los formatos. 
	 * @return ReportTemplateBuilder
	 */
	public ReportTemplateBuilder crearPlantillaReporteDetalle()
	{
		ReportTemplateBuilder reportTemplate;
		
		StyleBuilder rootStyle 				= stl.style().setPadding(1).setFontSize(7);
		StyleBuilder columnStyle 			= stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE).setPadding(2)
													.setBorder(stl.pen1Point().setLineColor(Color.BLACK));
		StyleBuilder columnTitleStyle		= stl.style(columnStyle)
										        .setBackgroundColor(new Color(225, 225, 225)).bold().setFontSize(7)
										        .setBorder(stl.pen1Point().setLineColor(Color.BLACK));
		StyleBuilder boldStyle          	= stl.style(rootStyle).bold();
		StyleBuilder groupStyle				= stl.style(boldStyle).setPadding(2).setFontSize(7)
													.setHorizontalAlignment(HorizontalAlignment.LEFT)
													.setBorder(stl.pen1Point().setLineColor(Color.BLACK));
		StyleBuilder subtotalStyle      	= stl.style().bold().setPadding(2).setFontSize(7).setHorizontalAlignment(HorizontalAlignment.RIGHT)
												 	.setBackgroundColor(new Color(230, 230, 230)).setBorder(stl.pen1Point().setLineColor(Color.BLACK));

		reportTemplate = template()
	        .setLocale(Locale.ENGLISH)
	        //Se define el tamano de la hoja y la orientación de la misma
	        .setPageFormat(PageType.HALFLETTER, PageOrientation.LANDSCAPE)
	        .setColumnStyle(columnStyle)
	        .setColumnTitleStyle(columnTitleStyle)
	        .setGroupStyle(groupStyle)
	        .setGroupTitleStyle(groupStyle)
	        .setSubtotalStyle(subtotalStyle)
	    ;
		
		return reportTemplate;
	}

	/**
	 * Crea el componente de las margenes del reporte de Ordenes 
	 * de Capitación Subcontratada para todos los formatos. 
	 * @return MarginBuilder
	 */
	public MarginBuilder crearMagenesReporte()
	{
		MarginBuilder margin;
		
		margin = margin()
	        .setTop(30)
			.setBottom(30)
	        .setLeft(20)
	        .setRight(20)
	    ;
		
		return margin;
	}
	
	/**
	 * Crea el componente de las margenes del reporte de Ordenes 
	 * de Capitación Subcontratada para todos los formatos. 
	 * @return MarginBuilder
	 */
	public MarginBuilder crearMagenesReporteDetalle()
	{
		MarginBuilder margin;
		
		margin = margin()
	        .setTop(20)
			.setBottom(20)
	        .setLeft(30)
	        .setRight(30)
	    ;
		
		return margin;
	}
	
	/**
	 * Crea el componente de las margenes del reporte de Ordenes 
	 * de Capitación Subcontratada para todos los formatos. 
	 * @return MarginBuilder
	 */
	public MarginBuilder crearMagenesSubReporteInstitucion()
	{
		MarginBuilder margin;
		
		margin = margin()
	        .setTop(10)
			.setBottom(10)
	        .setLeft(20)
	        .setRight(20)
	    ;
		
		return margin;
	}
	
	/**
	 * Crea el componente de las margenes del sub reporte de Ordenes 
	 * de Capitación Subcontratada para todos los formatos. 
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
