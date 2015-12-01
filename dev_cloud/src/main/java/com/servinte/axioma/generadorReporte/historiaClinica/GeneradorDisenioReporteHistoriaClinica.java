package com.servinte.axioma.generadorReporte.historiaClinica;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.template;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import net.sf.dynamicreports.report.builder.MarginBuilder;
import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.LineStyle;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.constant.VerticalAlignment;

import org.apache.struts.util.MessageResources;

import util.ConstantesIntegridadDominio;
import util.reportes.dinamico.EstilosReportesDinamicosHistoriaClinica;

import com.servinte.axioma.generadorReporteHistoriaClinica.comun.IConstantesReporteHistoriaClinica;

public class GeneradorDisenioReporteHistoriaClinica {


	/**
	 * Mensajes parametrizados del reporte.
	 */
	private static MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.historiaclinica.impresionHistoriaClinica");





	public GeneradorDisenioReporteHistoriaClinica() {
	}



	/**
	 * Estandarizacion de estilos basicos del reporte
	 * @return ReportTemplateBuilder
	 */
	public ReportTemplateBuilder crearPlantillaReporte()
	{
		ReportTemplateBuilder reportTemplate;

		StyleBuilder rootStyle 				= stl.style().setPadding(1).setFontSize(7);
		StyleBuilder columnStyle 			= stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK));
		StyleBuilder columnTitleStyle		= stl.style(columnStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT)
		.setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.2), LineStyle.SOLID).setLineColor(Color.BLACK));
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
		.setLeft(10)
		.setRight(10)
		;

		return margin;
	}


	/**
	 * Se crear encabezados del reporte
	 * @param params
	 * @return ComponentBuilder
	 */
	public ComponentBuilder<?, ?> crearComponenteEncabezadoFormatoA(Map<String, String> params) 
	{
		ComponentBuilder<?, ?> dynamicReportsComponent = null;

		
		
		String lineaTitulo	= params.get(IConstantesReporteHistoriaClinica.nombreInstitucion);
		String linea2		= params.get(IConstantesReporteHistoriaClinica.nitInstitucion);
		String linea3		= params.get(IConstantesReporteHistoriaClinica.actividadEconomica);
		String linea4		= params.get(IConstantesReporteHistoriaClinica.direccion);
		String linea5		= params.get(IConstantesReporteHistoriaClinica.centroAtencion);
		String linea6		= params.get(IConstantesReporteHistoriaClinica.tipoConsulta);
		String saltoLinea	= " ";
		String espacio		="                      ";
		String linea7		= "Fecha Cierre: ";
		String fecha		= params.get(IConstantesReporteHistoriaClinica.fecha);
		String labelInstitucion		= params.get(IConstantesReporteHistoriaClinica.institucionlabel);


		HorizontalListBuilder encabezado = null;
		if(params.get(IConstantesReporteHistoriaClinica.ubicacionLogo).
				equals(ConstantesIntegridadDominio.acronimoUbicacionIzquierda)){
			
			encabezado=	cmp.horizontalList(
						
						//Componente del Titulo y Subtitulos
					cmp.image(params.get(IConstantesReporteHistoriaClinica.rutaLogo)).setDimension(60, 30)
					.setStyle(stl.style().setLeftPadding(20).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER)).setWidth(60)
						,cmp.verticalList(
								cmp.text(saltoLinea).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTitulo).setFontSize(7)),
								cmp.text(lineaTitulo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTitulo).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.CENTER),
								cmp.text(linea2).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTitulo).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.CENTER),
								cmp.text(linea3).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTitulo).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.CENTER),
								cmp.text(linea4).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTitulo).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.CENTER),
								cmp.text(linea5).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTitulo).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.CENTER),
								cmp.text(linea6).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTitulo).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.CENTER)
						)
				);
		}else


		if(params.get(IConstantesReporteHistoriaClinica.ubicacionLogo).
				equals(ConstantesIntegridadDominio.acronimoUbicacionDerecha)){
			encabezado=	cmp.horizontalList(
					
					//Componente del Titulo y Subtitulos
				cmp.verticalList(
							cmp.text(saltoLinea).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTitulo).setFontSize(7)),
							cmp.text(lineaTitulo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTitulo).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.CENTER),
							cmp.text(linea2).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTitulo).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.CENTER),
							cmp.text(linea3).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTitulo).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.CENTER),
							cmp.text(linea4).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTitulo).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.CENTER),
							cmp.text(linea5).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTitulo).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.CENTER),
							cmp.text(linea6).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTitulo).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.CENTER)
					)
				,cmp.image(params.get(IConstantesReporteHistoriaClinica.rutaLogo)).setDimension(60, 30)
				.setStyle(stl.style().setLeftPadding(20).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER)).setWidth(60)
			);
		}
		
		
		
		dynamicReportsComponent = 
			cmp.verticalList(
					encabezado ).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloEjeCentradoMedioBordeSinBorde);

		return dynamicReportsComponent;
	}
	
	
	
	
	

	/**
	 * Se crea el pie de pagina
	 * @param params
	 * @return ComponentBuilder
	 */
	public ComponentBuilder<?, ?> crearcomponentePiePagina(Map<String, String> params)
	{
		String espacio = "     ";
		SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy HH:mm");
		  return cmp.horizontalList(cmp.text("Firmado Electrónicamente").setStyle(stl.style().bold().setFontSize(7))
		          .setStretchWithOverflow(true)
		          .setHorizontalAlignment(HorizontalAlignment.LEFT),
		          cmp.text(
				           espacio+sdf.format(new Date())+"       "
				           +params.get(IConstantesReporteHistoriaClinica.usuarioProceso)).setStyle(stl.style().setFontSize(7))
				          .setStretchWithOverflow(false)
				          .setHorizontalAlignment(HorizontalAlignment.CENTER),
		          cmp.pageXofY().setFormatExpression("Página {0} de {1}").setStyle(stl.style().setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.RIGHT));
	}
	


	/**
	 * Crea el componente de las margenes del sub reporte 
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
	 * Crea el componente de las margenes del sub reporte 
	 * @return MarginBuilder
	 */
	public MarginBuilder crearMagenesNulasSubReporte()
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
