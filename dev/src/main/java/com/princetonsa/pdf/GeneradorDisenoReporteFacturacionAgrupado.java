package com.princetonsa.pdf;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.template;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import com.servinte.axioma.generadorReporteHistoriaClinica.comun.IConstantesReporteHistoriaClinica;

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
import util.ConstantesIntegridadDominio;


public class GeneradorDisenoReporteFacturacionAgrupado {

	
	public GeneradorDisenoReporteFacturacionAgrupado() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	/**
	 * Estandarizacion de estilos basicos del reporte
	 * @return ReportTemplateBuilder
	 */
	public ReportTemplateBuilder crearPlantillaReporte(String tamanoReporte)
	{
		ReportTemplateBuilder reportTemplate;

		StyleBuilder rootStyle 				= stl.style().setPadding(1).setFontSize(7);
		StyleBuilder columnStyle 			= stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK));
		StyleBuilder columnTitleStyle		= stl.style(columnStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT)
		.setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.2), LineStyle.SOLID).setLineColor(Color.BLACK));
		StyleBuilder boldStyle          	= stl.style(rootStyle).bold();
		StyleBuilder groupStyle				= stl.style(boldStyle).setPadding(1).setFontSize(7).setHorizontalAlignment(HorizontalAlignment.LEFT);
		StyleBuilder subtotalStyle      	= stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTotalRSB);

		reportTemplate = template()
		.setLocale(Locale.ENGLISH)
		
		.setColumnStyle(columnStyle)
		.setColumnTitleStyle(columnTitleStyle)
		.setGroupStyle(groupStyle)
		.setGroupTitleStyle(groupStyle)
		.setSubtotalStyle(subtotalStyle)
		;

		
		if(tamanoReporte.equals("mediaCarta")){
			reportTemplate
			//Se define el tamano de la hoja y la orientación de la misma
			//.setPageFormat(PageType.HALFLETTER, PageOrientation.LANDSCAPE);
			.setPageFormat(PageType.HALFLETTER, PageOrientation.LANDSCAPE);
		}else{
			reportTemplate
			//Se define el tamano de la hoja y la orientación de la misma
			.setPageFormat(PageType.LETTER, PageOrientation.PORTRAIT);
		}
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
		.setBottom(1)
		.setLeft(5)
		.setRight(5)
		;

		return margin;
	}
	
	/**
	 * Creacion de margenes del reporte
	 * @return MarginBuilder
	 */
	public MarginBuilder crearMagenesReporteGeneral()
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
	 * Se crear encabezados del reporte
	 * @param params
	 * @return ComponentBuilder
	 */
	public ComponentBuilder<?, ?> crearComponenteEncabezado(Map<String, String> params) 
	{
		ComponentBuilder<?, ?> dynamicReportsComponent = null;


		//titulos del encambezado 
		String lineaTitulo	= params.get("razonSocialInst")+" - " +params.get("centroAtencionFact");
		String linea2		= params.get("nitInst")+ params.get("direccionInst")+params.get("telefonoInst")+params.get("ciudadInst");
		String linea3		= params.get("fechaHoraAnulacion");
		String linea4		= params.get("numerofactAsociada");
		String linea5		= params.get("responsableFact");
		String linea7 		= "Número ID: "+params.get("numeroId");
		String linea8 		= "Autorización: "+params.get("autorizacion");
		String linea6		= "Dirección: "+params.get("direccionResponsable");
		String linea9		= "Teléfono: "+params.get("telefonoResponsable");
		String linea10		= "Fecha de Fact.:"+params.get("fechaFacturacion"); 
		String linea11		= "Fecha de Vencimiento: "+params.get("fechaVencimiento");
		String encabezadoConvenioInstitucion=params.get("encabezadofacturaconvenio");		


		HorizontalListBuilder encabezado = null;
	
		
		
		
		
		if(params.get(IConstantesReporteAgrupadoFacturacion.ubicacionLogo).
				equals(ConstantesIntegridadDominio.acronimoUbicacionIzquierda)){
			
		
		
		//se crea el encabezado  con logo y datos
		encabezado=	cmp.horizontalList(

				//Componente del Titulo y Subtitulos
				cmp.image(params.get("institucionBasica")).setDimension(60, 30)
				.setStyle(stl.style().setLeftPadding(20).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER)).setWidth(60)
				,cmp.verticalList(
						cmp.text(lineaTitulo).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTitulo).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.CENTER),
						cmp.text(linea2).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTituloSinBold).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.CENTER),
						cmp.text(encabezadoConvenioInstitucion).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTituloSinBold).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.CENTER),
						cmp.text(linea3).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTitulo).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.CENTER),
						cmp.text(linea4).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTitulo).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.CENTER)
				)
		).newRow().
		add(cmp.verticalList(cmp.horizontalList(cmp.text(linea5).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTituloSinBold).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.LEFT),
				cmp.text(linea7).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTituloSinBold).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.CENTER),
				cmp.text(linea8).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTituloSinBold).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.RIGHT)
		),
		cmp.horizontalList(cmp.text(linea6).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTituloSinBold).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.LEFT),
				cmp.text(linea9).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTituloSinBold).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.CENTER),
				cmp.text(linea10).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTituloSinBold).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.CENTER),
				cmp.text(linea11).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTituloSinBold).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.RIGHT))
		));
		}else{
			
			//se crea el encabezado  con logo y datos
			encabezado=	cmp.horizontalList(

					//Componente del Titulo y Subtitulos
					
					cmp.verticalList(
							cmp.text(lineaTitulo).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTitulo).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.CENTER),
							cmp.text(linea2).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTituloSinBold).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.CENTER),
							cmp.text(encabezadoConvenioInstitucion).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTituloSinBold).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.CENTER),
							cmp.text(linea3).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTitulo).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.CENTER),
							cmp.text(linea4).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTitulo).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.CENTER)
					),cmp.image(params.get("institucionBasica")).setDimension(60, 30)
					.setStyle(stl.style().setLeftPadding(20).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER)).setWidth(60)
			).newRow().
			add(cmp.verticalList(cmp.horizontalList(cmp.text(linea5).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTituloSinBold).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.LEFT),
					cmp.text(linea7).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTituloSinBold).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.CENTER),
					cmp.text(linea8).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTituloSinBold).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.RIGHT)
			),
			cmp.horizontalList(cmp.text(linea6).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTituloSinBold).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.LEFT),
					cmp.text(linea9).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTituloSinBold).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.CENTER),
					cmp.text(linea10).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTituloSinBold).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.CENTER),
					cmp.text(linea11).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTituloSinBold).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.RIGHT))
			));
			
		}



		//se adiciona el encabezado 
		dynamicReportsComponent = 
			cmp.verticalList(
					encabezado ).setStyle(EstilosReportesDinamicosReporteFacturaAgrupada.estiloEjeCentradoMedioBordeSinBorde);

		//se retorna el encabezado
		return dynamicReportsComponent;
	}
	
	
	
	
	/**
	 * Se crea el pie de pagina
	 * @param params
	 * @return ComponentBuilder
	 */
	public ComponentBuilder<?, ?> crearcomponentePiePagina(Map<String, String> params)
	{
		//tirulo de fehca y formato de fecha
		String espacio = "Fecha: ";
		SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy - HH:mm");

		//se retorna el pir de pagina segun el dcu
		return 
		cmp.horizontalList(	
				cmp.text(espacio+sdf.format(new Date())).setStyle(stl.style().setFontSize(7))
				.setStretchWithOverflow(true)
				.setHorizontalAlignment(HorizontalAlignment.LEFT),
				cmp.text("Usuario: "+params.get("loginusuario")).setStyle(stl.style().setFontSize(7)).setStretchWithOverflow(true).setHorizontalAlignment(HorizontalAlignment.CENTER),
				cmp.pageXofY().setFormatExpression("Página {0} de {1}").setStyle(stl.style().setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.RIGHT)
		);
	}
	

	
}
