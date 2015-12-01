package com.servinte.axioma.generadorReporte.historiaClinica;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

import java.awt.Color;
import java.util.Map;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.LineStyle;
import net.sf.dynamicreports.report.constant.VerticalAlignment;

import org.apache.struts.util.MessageResources;

import util.ConstantesIntegridadDominio;
import util.reportes.dinamico.EstilosReportesDinamicosHistoriaClinica;

import com.servinte.axioma.generadorReporteHistoriaClinica.comun.IConstantesReporteHistoriaClinica;


public class GeneradorSubReporteEncabezadoFormatoSinPrimeraPagina {
	

	/**
	 * Mensajes parametrizados de los reportes 
	 */
	private static MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.historiaclinica.impresionHistoriaClinica");
	
	
	public GeneradorSubReporteEncabezadoFormatoSinPrimeraPagina() {
	}
	
	
	/**
	 * Se crear encabezados del reporte
	 * @param params
	 * @return ComponentBuilder
	 */
	public JasperReportBuilder crearComponenteEncabezadoFormatoSinPrimeraPagina(Map<String, String> params) 
	{
		GeneradorDisenoSubReportes diseno= new GeneradorDisenoSubReportes();
		JasperReportBuilder infoBasica = report();
		ComponentBuilder<?, ?> componentBuilder = null;
		String nombrepaciente	=messageResource.getMessage("historia_clinica_lable_nombrepaciente")+ params.get(IConstantesReporteHistoriaClinica.nombrepaciente);
		String tipoNumeroID	=messageResource.getMessage("historia_clinica_lable_tipoNumeroID_encabezado")+ params.get(IConstantesReporteHistoriaClinica.tipoNumeroID);
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
					.setStyle(stl.style().setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK))
							.setLeftPadding(20).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER)).setWidth(60)
						,cmp.verticalList(
								cmp.text(saltoLinea).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloFont5),
								cmp.text(lineaTitulo).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloFont5).setHorizontalAlignment(HorizontalAlignment.CENTER),
								cmp.text(linea2).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloFont5).setHorizontalAlignment(HorizontalAlignment.CENTER),
								cmp.text(linea5).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloFont5).setHorizontalAlignment(HorizontalAlignment.CENTER),
								cmp.text(linea6).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloFont5).setHorizontalAlignment(HorizontalAlignment.CENTER)
						).setStyle(stl.style().setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)))
				);
		}else


		if(params.get(IConstantesReporteHistoriaClinica.ubicacionLogo).
				equals(ConstantesIntegridadDominio.acronimoUbicacionDerecha)){
			encabezado=	cmp.horizontalList(
					
					//Componente del Titulo y Subtitulos
				cmp.verticalList(
							cmp.text(saltoLinea).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloFont5),
							cmp.text(lineaTitulo).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloFont5).setHorizontalAlignment(HorizontalAlignment.CENTER),
							cmp.text(linea2).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloFont5).setHorizontalAlignment(HorizontalAlignment.CENTER),
							cmp.text(linea5).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloFont5).setHorizontalAlignment(HorizontalAlignment.CENTER),
							cmp.text(linea6).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloFont5).setHorizontalAlignment(HorizontalAlignment.CENTER)
					).setStyle(stl.style().setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)))
				,cmp.image(params.get(IConstantesReporteHistoriaClinica.rutaLogo)).setDimension(60, 30)
				.setStyle(stl.style().setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)).setLeftPadding(20).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER)).setWidth(60)
			);
		}
		
		
		
		componentBuilder =  
			cmp.verticalList(
					encabezado,cmp.text(""),cmp.horizontalList(cmp.text(nombrepaciente).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloFont5).setHorizontalAlignment(HorizontalAlignment.CENTER),
												  cmp.text(tipoNumeroID).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloFont5).setHorizontalAlignment(HorizontalAlignment.CENTER)
												  ) ).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloEjeCentradoMedioBordeSinBorde);
		
		infoBasica.pageHeader(componentBuilder)
		.setPageMargin(diseno.crearMagenesReporte())
		.build();  

		return infoBasica;
	}

	
	
	

}
