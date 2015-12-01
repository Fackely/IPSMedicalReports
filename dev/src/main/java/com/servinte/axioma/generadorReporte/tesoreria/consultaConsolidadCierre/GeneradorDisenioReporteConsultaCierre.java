package com.servinte.axioma.generadorReporte.tesoreria.consultaConsolidadCierre;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.template;

import java.awt.Color;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.dynamicreports.report.builder.MarginBuilder;
import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.constant.VerticalAlignment;

import org.apache.struts.util.MessageResources;

import util.ConstantesIntegridadDominio;
import util.UtilidadTexto;
import util.reportes.dinamico.EstilosReportesDinamicos;

import com.servinte.axioma.generadorReporte.comun.IConstantesReporte;
import com.servinte.axioma.orm.FormasPago;
 
public class GeneradorDisenioReporteConsultaCierre {


	/**
	 * Mensajes parametrizados del reporte.
	 */
	private MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.tesoreria.ConsultarConsolidadosCierres");





	public GeneradorDisenioReporteConsultaCierre() {
		// TODO Auto-generated constructor stub
	}



	/**
	 * Estandarizacion de estilos basicos del reporte
	 * @return ReportTemplateBuilder
	 */
	public ReportTemplateBuilder crearPlantillaReporte()
	{
		ReportTemplateBuilder reportTemplate;

		StyleBuilder rootStyle 				= stl.style().setPadding(1).setFontSize(7);
		StyleBuilder columnStyle 			= stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE);
		StyleBuilder columnTitleStyle		= stl.style(columnStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT)
		.setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen1Point().setLineColor(Color.WHITE));
		StyleBuilder boldStyle          	= stl.style(rootStyle).bold();
		StyleBuilder groupStyle				= stl.style(boldStyle).setPadding(1).setFontSize(7).setHorizontalAlignment(HorizontalAlignment.LEFT);
		StyleBuilder subtotalStyle      	= stl.style(EstilosReportesDinamicos.estiloSubTotalRSB);

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
		.setTop(30)
		.setBottom(30)
		.setLeft(20)
		.setRight(20)
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

		String lineaTitulo	= params.get(IConstantesReporte.nombreInstitucion);
		String linea2		= params.get(IConstantesReporte.nitInstitucion);
		String linea3		= params.get(IConstantesReporte.actividadEconomica);
		String linea4		= params.get(IConstantesReporte.direccion);
		String linea5		= params.get(IConstantesReporte.centroAtencion);
		String linea6		= params.get(IConstantesReporte.tipoConsulta);
		String saltoLinea	= " ";
		String espacio		="                      ";
		String linea7		= "Fecha Cierre: ";
		String fecha		= params.get(IConstantesReporte.fecha);
		String labelInstitucion		= params.get(IConstantesReporte.institucionlabel);


		HorizontalListBuilder encabezado = null;
		if(params.get(IConstantesReporte.ubicacionLogo).
				equals(ConstantesIntegridadDominio.acronimoUbicacionIzquierda)){
			
			encabezado=	cmp.horizontalList(
						
						//Componente del Titulo y Subtitulos
					cmp.image(params.get(IConstantesReporte.rutaLogo))
					.setStyle(stl.style().setLeftPadding(20).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER)).setWidth(60)
						,cmp.verticalList(
								cmp.text(saltoLinea).setStyle(EstilosReportesDinamicos.estiloSubTitulo),
								cmp.text(lineaTitulo).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
								cmp.text(linea2).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
								cmp.text(linea3).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
								cmp.text(linea4).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
								cmp.text(linea5).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
								cmp.text(linea6).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER)
						)
				);
		}else


		if(params.get(IConstantesReporte.ubicacionLogo).
				equals(ConstantesIntegridadDominio.acronimoUbicacionDerecha)){
			encabezado=	cmp.horizontalList(
					
					//Componente del Titulo y Subtitulos
				cmp.verticalList(
							cmp.text(saltoLinea).setStyle(EstilosReportesDinamicos.estiloSubTitulo),
							cmp.text(lineaTitulo).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
							cmp.text(linea2).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
							cmp.text(linea3).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
							cmp.text(linea4).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
							cmp.text(linea5).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
							cmp.text(linea6).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER)
					)
				,cmp.image(params.get(IConstantesReporte.rutaLogo))
				.setStyle(stl.style().setLeftPadding(20).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER)).setWidth(60)
			);
		}
		
		
		
		dynamicReportsComponent = 
			cmp.verticalList(
					encabezado
					,cmp.verticalList(
							//Componente de los parametros seleccionados por el usurio para
							//generar el reporte
							
							cmp.horizontalList(
									cmp.text(linea7+fecha)
									.setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.LEFT)
							)
					).setStyle(EstilosReportesDinamicos.estiloEjeCentradoMedioBordeSinBorde)
					,cmp.verticalList(
							//Componente de los parametros seleccionados por el usurio para
							//generar el reporte
							
							cmp.horizontalList(
									cmp.text(labelInstitucion)
									.setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.LEFT)
							)
					).setStyle(EstilosReportesDinamicos.estiloTituloSombreadoL)



			).setStyle(EstilosReportesDinamicos.estiloEjeCentradoMedioBordeSinBorde);

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
		  return cmp.horizontalList(cmp.text(messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.impresion")
		           +espacio+sdf.format(new Date())+"       "
		           +params.get(IConstantesReporte.usuarioProceso))
		          .setStretchWithOverflow(true)
		          .setHorizontalAlignment(HorizontalAlignment.CENTER));
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


	/**
	 * Crea el componente del encabezado (títulos y subtítulos) del reporte
	 * de Ordenes de Capitación Subcontratada para el reporte plano para el formato A. 
	 * @return ComponentBuilder
	 */
	public ComponentBuilder<?, ?> crearComponenteEncabezadoPlanoFormatoA(Map<String, String> params,List<FormasPago> formasPago) 
	{
		ComponentBuilder<?, ?> dynamicReportsComponent = null;

		String lineaTitulo	= messageResource.getMessage("consolidacion_cierres_label_archivo_plano_titulo");
		String linea2		= messageResource.getMessage("consolidacion_cierres_label_archivo_plano_fecha_cierre")+params.get(IConstantesReporte.fecha);
		int tamano=formasPago.size();
		ComponentBuilder[] cols= new ComponentBuilder[tamano+2];
		
		ComponentBuilder institucion=cmp.text(messageResource.getMessage("consolidacion_cierres_label_reporte_intitucion"));
		ComponentBuilder centroAtencion=cmp.text(messageResource.getMessage("consolidacion_cierres_label_reporte_centro_atencion"));
		cols[0]=institucion;
		cols[1]=centroAtencion;
		
		for (int i = 2; i < tamano+2; i++) {
			cols[i]=cmp.text(UtilidadTexto.primeraLetraMayuscula(formasPago.get(i-2).getDescripcion()));
		}
		
		
		
		
		
		dynamicReportsComponent = 
			cmp.verticalList(
					//Componente del Titulo y Subtitulos
					cmp.text(lineaTitulo),
					cmp.text(linea2),
					cmp.horizontalList(cols )) ;

		return dynamicReportsComponent;
	}


















}
