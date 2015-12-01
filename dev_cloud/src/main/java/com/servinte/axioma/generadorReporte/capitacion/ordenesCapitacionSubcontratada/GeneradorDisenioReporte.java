package com.servinte.axioma.generadorReporte.capitacion.ordenesCapitacionSubcontratada;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.template;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import net.sf.dynamicreports.report.builder.MarginBuilder;
import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.constant.StretchType;
import net.sf.dynamicreports.report.constant.VerticalAlignment;

import org.apache.struts.util.MessageResources;

import util.ConstantesIntegridadDominio;
import util.reportes.dinamico.EstilosReportesDinamicos;

import com.servinte.axioma.generadorReporte.comun.IConstantesReporte;
import com.servinte.axioma.orm.ParametrizacionSemaforizacion;

/**
 * Clase para generar los diseños generales de los formatos
 * del reporte de Ordenes de Capitación Subcontratada
 * 
 * @version 1.0, May 7, 2011
 * @author <a href="mailto:ricardo.ruiz@servinte.com.co">Ricardo Ruiz</a>
 * 
 */
public class GeneradorDisenioReporte {

	/** * Contiene los mensajes correspondiente a este Generador*/
	MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.capitacion.GeneradorReporteOrdenCapitacionSubcontratada");
	
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
	public ComponentBuilder<?, ?> crearcomponentePiePagina(Map<String, String> params)
	{
		return	cmp.horizontalList(cmp.text(messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.footer")
											+espacio+params.get(IConstantesReporte.fechaProcesa)+espacioLargo
											+params.get(IConstantesReporte.usuarioProceso)+" ("
											+params.get(IConstantesReporte.loginUsuarioProceso)+")")
										.setStretchWithOverflow(true)
										.setHorizontalAlignment(HorizontalAlignment.CENTER));
	}
	
	
	
	
	/**
	 * Crea el componente del encabezado (títulos y subtítulos) del reporte
	 * de Ordenes de Capitación Subcontratada para el formato A. 
	 * @return ComponentBuilder
	 */
	@SuppressWarnings("rawtypes")
	public ComponentBuilder<?, ?> crearComponenteEncabezadoFormatoA(Map<String, String> params, ArrayList<ParametrizacionSemaforizacion> semaforizacion) 
	{
		ComponentBuilder<?, ?> dynamicReportsComponent = null;
		
		String lineaTitulo	= params.get(IConstantesReporte.nombreInstitucion);
		String linea2		= params.get(IConstantesReporte.nitInstitucion);
		String linea3		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.tituloReporte");
		String linea4		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.tipoConsulta")
									+espacio+params.get(IConstantesReporte.tipoConsulta);
		String linea5		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.fecha")+espacio;
		String fecha		= params.get(IConstantesReporte.fecha);
		String convenio		= "";
		String contrato		= "";
		if(params.get(IConstantesReporte.convenio) != null && !params.get(IConstantesReporte.convenio).isEmpty()){
			convenio = messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.convenio")+espacio+params.get(IConstantesReporte.convenio);
		}
		if(params.get(IConstantesReporte.contrato) != null && !params.get(IConstantesReporte.contrato).isEmpty()){
			contrato = messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.contrato")+espacio+params.get(IConstantesReporte.contrato);
		}
		
		HorizontalListBuilder convenciones = cmp.horizontalList();
		convenciones.add(cmp.text(messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.convenciones")).setStyle(EstilosReportesDinamicos.estiloSubTituloL));
		for(ParametrizacionSemaforizacion parame:semaforizacion){
			convenciones.add(cmp.text(parame.getRangoInicial()+"% - "+parame.getRangoFinal()+"%")
								.setHorizontalAlignment(HorizontalAlignment.RIGHT)
								.setStyle(EstilosReportesDinamicos.estiloSubTitulo));
			StringBuilder buildColor = new StringBuilder(parame.getColor());
			buildColor.replace(0, 1, "0x");
			Color color= Color.decode(buildColor.toString());
			convenciones.add(cmp.text(espacio).setDimension(10, 10).setStyle(stl.style().setBackgroundColor(color).setForegroudColor(color)));
		}
		
		ComponentBuilder encabezado;
		if(params.get(IConstantesReporte.ubicacionLogo).
				equals(ConstantesIntegridadDominio.acronimoUbicacionIzquierda)){
			//Componente del Logo
			encabezado=cmp.horizontalList(
							cmp.image(params.get(IConstantesReporte.rutaLogo))
									.setDimension(60, 30)
									.setStyle(stl.style().setLeftPadding(20))
									.setWidth(60),
							//Componente del Titulo y Subtitulos
							cmp.verticalList(
								cmp.text(lineaTitulo).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
								cmp.text(linea2).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
								cmp.text(linea3).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
								cmp.text(linea4).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER)
							).setStretchType(StretchType.RELATIVE_TO_TALLEST_OBJECT)
			);
		}
		else{		
			//Componente del Logo
			encabezado=cmp.horizontalList(
							//Componente del Titulo y Subtitulos
							cmp.verticalList(
								cmp.text(lineaTitulo).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
								cmp.text(linea2).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
								cmp.text(linea3).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
								cmp.text(linea4).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER)
							).setStretchType(StretchType.RELATIVE_TO_TALLEST_OBJECT),
							cmp.image(params.get(IConstantesReporte.rutaLogo))
								.setDimension(60, 30)
								.setStyle(stl.style().setLeftPadding(50))
								.setWidth(60));
		}
		dynamicReportsComponent = 
			cmp.verticalList(
				encabezado,
				cmp.horizontalList(cmp.text(espacio))
				//Componente de los parametros seleccionados por el usurio para
				//generar el reporte
				,cmp.horizontalList(
						cmp.text(linea5+fecha+espacioLargo+convenio+espacioLargo+contrato)
						.setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.LEFT)
				),
				convenciones.setStretchType(StretchType.RELATIVE_TO_TALLEST_OBJECT)
			);

		return dynamicReportsComponent;
	}
	
	/**
	 * Crea el componente del encabezado (títulos y subtítulos) del reporte
	 * de Ordenes de Capitación Subcontratada para el reporte plano para el formato A. 
	 * @return ComponentBuilder
	 */
	public ComponentBuilder<?, ?> crearComponenteEncabezadoPlanoFormatoA(Map<String, String> params) 
	{
		ComponentBuilder<?, ?> dynamicReportsComponent = null;
		
		String lineaTitulo	= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.tituloReportePlano");
		String linea2		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.tipoConsulta")
									+espacio+params.get(IConstantesReporte.tipoConsulta);
		String linea3		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.fecha")
									+espacio+params.get(IConstantesReporte.fecha);
		String convenio		= "";
		String contrato		= "";
		if(params.get(IConstantesReporte.convenio) != null && !params.get(IConstantesReporte.convenio).isEmpty()){
			convenio = messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.convenioPlano")+espacio+params.get(IConstantesReporte.convenio);
		}
		if(params.get(IConstantesReporte.contrato) != null && !params.get(IConstantesReporte.contrato).isEmpty()){
			contrato = messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.contratoPlano")+espacio+params.get(IConstantesReporte.contrato);
		}
		String linea4		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.tipoConvenio");
		String linea5		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.convenioPlano");
		String linea6		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.contratoPlano");
		String linea7		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnNivelAtencion");
		String linea8		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVPresupuestado");
		String linea9		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVOrdenado");
		String linea10		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPOrdenado");
		String linea11		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVAutorizado");
		String linea12		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPAutorizado");
		String linea13		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVCargos");
		String linea14		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPCargos");
		String linea15		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVFacturado");
		String linea16		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPFacturado");
		dynamicReportsComponent = 
			cmp.verticalList(
				//Componente del Titulo y Subtitulos
				cmp.text(lineaTitulo),
				cmp.text(linea2),
				cmp.text(linea3+espacioLargo+convenio+espacioLargo+contrato),
				cmp.horizontalList(cmp.text(linea4), cmp.text(linea5), cmp.text(linea6), 
						cmp.text(linea7), cmp.text(linea8), cmp.text(linea9), cmp.text(linea10),
						cmp.text(linea11), cmp.text(linea12), cmp.text(linea13), cmp.text(linea14),
						cmp.text(linea15), cmp.text(linea16))
			);
			
		return dynamicReportsComponent;
	}
	
	/**
	 * Crea el componente del encabezado (títulos y subtítulos) del reporte
	 * de Ordenes de Capitación Subcontratada para el reporte plano para el formato B. 
	 * @return ComponentBuilder
	 */
	public ComponentBuilder<?, ?> crearComponenteEncabezadoPlanoFormatoB(Map<String, String> params) 
	{
		ComponentBuilder<?, ?> dynamicReportsComponent = null;
		
		
		String lineaTitulo	= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.tituloReportePlano");
		String linea2		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.tipoConsulta")
									+espacio+params.get(IConstantesReporte.tipoConsulta);
		String linea3		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.fecha")
									+espacio+params.get(IConstantesReporte.fecha);
		String convenio		= "";
		String contrato		= "";
		if(params.get(IConstantesReporte.convenio) != null && !params.get(IConstantesReporte.convenio).isEmpty()){
			convenio = messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.convenioPlano")+espacio+params.get(IConstantesReporte.convenio);
		}
		if(params.get(IConstantesReporte.contrato) != null && !params.get(IConstantesReporte.contrato).isEmpty()){
			contrato = messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.contratoPlano")+espacio+params.get(IConstantesReporte.contrato);
		}
		String linea5		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.contratoPlano");
		String linea6		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnNivelAtencion");
		String linea7		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnDescripcion");
		String linea8		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVPresupuestado");
		String linea9		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVOrdenado");
		String linea10		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPOrdenado");
		String linea11		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVAutorizado");
		String linea12		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPAutorizado");
		String linea13		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVCargos");
		String linea14		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPCargos");
		String linea15		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVFacturado");
		String linea16		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPFacturado");
		
		dynamicReportsComponent = 
			cmp.verticalList(
				//Componente del Titulo y Subtitulos
				cmp.text(lineaTitulo),
				cmp.text(linea2),
				cmp.text(linea3+espacioLargo+convenio+espacioLargo+contrato),
				cmp.horizontalList(cmp.text(linea5), cmp.text(linea6), 
						cmp.text(linea7), cmp.text(linea8), cmp.text(linea9), cmp.text(linea10),
						cmp.text(linea11), cmp.text(linea12), cmp.text(linea13), cmp.text(linea14),
						cmp.text(linea15), cmp.text(linea16))
			);
			
		return dynamicReportsComponent;
	}

	/**
	 * Crea el componente del encabezado (títulos y subtítulos) del reporte
	 * de Ordenes de Capitación Subcontratada para el reporte plano para el formato C. 
	 * @return ComponentBuilder
	 */
	public ComponentBuilder<?, ?> crearComponenteEncabezadoPlanoFormatoC(Map<String, String> params) 
	{
		ComponentBuilder<?, ?> dynamicReportsComponent = null;
		
		String lineaTitulo	= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.tituloReportePlano");
		String linea2		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.tipoConsulta")
									+espacio+params.get(IConstantesReporte.tipoConsulta);
		String linea3		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.fecha")
									+espacio+params.get(IConstantesReporte.fecha);
		String convenio		= "";
		String contrato		= "";
		if(params.get(IConstantesReporte.convenio) != null && !params.get(IConstantesReporte.convenio).isEmpty()){
			convenio = messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.convenioPlano")+espacio+params.get(IConstantesReporte.convenio);
		}
		if(params.get(IConstantesReporte.contrato) != null && !params.get(IConstantesReporte.contrato).isEmpty()){
			contrato = messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.contratoPlano")+espacio+params.get(IConstantesReporte.contrato);
		}
		String linea5		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnNivelAtencion");
		String linea6		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnDescripcion");
		String linea7		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnGruposClases");
		String linea8		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVPresupuestado");
		String linea9		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVOrdenado");
		String linea10		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPOrdenado");
		String linea11		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVAutorizado");
		String linea12		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPAutorizado");
		String linea13		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVCargos");
		String linea14		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPCargos");
		String linea15		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVFacturado");
		String linea16		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPFacturado");
		
		dynamicReportsComponent = 
			cmp.verticalList(
				//Componente del Titulo y Subtitulos
				cmp.text(lineaTitulo),
				cmp.text(linea2),
				cmp.text(linea3+espacioLargo+convenio+espacioLargo+contrato),
				cmp.horizontalList(cmp.text(linea5), cmp.text(linea6), 
						cmp.text(linea7), cmp.text(linea8), cmp.text(linea9), cmp.text(linea10),
						cmp.text(linea11), cmp.text(linea12), cmp.text(linea13), cmp.text(linea14),
						cmp.text(linea15), cmp.text(linea16))
			);
			
		return dynamicReportsComponent;
	}
	
	
	/**
	 * Crea el componente del encabezado (títulos y subtítulos) del reporte
	 * de Ordenes de Capitación Subcontratada para el formato B. 
	 * @return ComponentBuilder
	 */
	@SuppressWarnings("rawtypes")
	public ComponentBuilder<?, ?> crearComponenteEncabezadoFormatoB(Map<String, String> params,
										ArrayList<ParametrizacionSemaforizacion> semaforizacion) 
	{
		ComponentBuilder<?, ?> dynamicReportsComponent = null;
		
		String lineaTitulo	= params.get(IConstantesReporte.nombreInstitucion);
		String linea2		= params.get(IConstantesReporte.nitInstitucion);
		String linea3		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.tituloReporte");
		String linea4		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.tipoConsulta")
									+espacio+params.get(IConstantesReporte.tipoConsulta);
		String linea5		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.fecha")+espacio;
		String fecha		= params.get(IConstantesReporte.fecha);
		String convenio		= "";
		String contrato		= "";
		if(params.get(IConstantesReporte.convenio) != null && !params.get(IConstantesReporte.convenio).isEmpty()){
			convenio = messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.convenio")+espacio+params.get(IConstantesReporte.convenio);
		}
		if(params.get(IConstantesReporte.contrato) != null && !params.get(IConstantesReporte.contrato).isEmpty()){
			contrato = messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.contrato")+espacio+params.get(IConstantesReporte.contrato);
		}
		
		HorizontalListBuilder convenciones = cmp.horizontalList();
		convenciones.add(cmp.text(messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.convenciones")).setStyle(EstilosReportesDinamicos.estiloSubTituloL));
		for(ParametrizacionSemaforizacion parame:semaforizacion){
			convenciones.add(cmp.text(parame.getRangoInicial()+"% - "+parame.getRangoFinal()+"%")
										.setHorizontalAlignment(HorizontalAlignment.RIGHT)
										.setStyle(EstilosReportesDinamicos.estiloSubTitulo));
			StringBuilder buildColor = new StringBuilder(parame.getColor());
			buildColor.replace(0, 1, "0x");
			Color color= Color.decode(buildColor.toString());
			convenciones.add(cmp.text(espacio).setDimension(10, 10).setStyle(stl.style().setBackgroundColor(color).setForegroudColor(color)));
		}
		
		ComponentBuilder encabezado;
		if(params.get(IConstantesReporte.ubicacionLogo).
				equals(ConstantesIntegridadDominio.acronimoUbicacionIzquierda)){
			//Componente del Logo
			encabezado= cmp.horizontalList(
					cmp.image(params.get(IConstantesReporte.rutaLogo)).setDimension(60, 30)
						.setStyle(stl.style().setLeftPadding(20)).setWidth(60)
					//Componente del Titulo y Subtitulos
					,cmp.verticalList(
						cmp.text(lineaTitulo).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
						cmp.text(linea2).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
						cmp.text(linea3).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
						cmp.text(linea4).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER)
					)
			);
		}
		else{
			//Componente del Logo
			encabezado= cmp.horizontalList(
					//Componente del Titulo y Subtitulos
					cmp.verticalList(
						cmp.text(lineaTitulo).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
						cmp.text(linea2).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
						cmp.text(linea3).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
						cmp.text(linea4).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER)
					),
					cmp.image(params.get(IConstantesReporte.rutaLogo)).setDimension(60, 30)
						.setStyle(stl.style().setLeftPadding(20)).setWidth(60)
			);
		}
		
		dynamicReportsComponent = 
			cmp.verticalList(
				encabezado,
				cmp.horizontalList(cmp.text(espacio))
				,cmp.verticalList(
						//Componente de los parametros seleccionados por el usurio para
						//generar el reporte
						cmp.horizontalList(
							cmp.text(linea5+fecha+espacioLargo+convenio+espacioLargo+contrato)
							.setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.LEFT)
						)
				)
				,convenciones.setStretchType(StretchType.RELATIVE_TO_TALLEST_OBJECT)
			);

		return dynamicReportsComponent;
	}
	
	/**
	 * Crea el componente del encabezado (títulos y subtítulos) del reporte
	 * de Ordenes de Capitación Subcontratada para el formato C. 
	 * @return ComponentBuilder
	 */
	@SuppressWarnings("rawtypes")
	public ComponentBuilder<?, ?> crearComponenteEncabezadoFormatoC(Map<String, String> params,
									ArrayList<ParametrizacionSemaforizacion> semaforizacion) 
	{
		ComponentBuilder<?, ?> dynamicReportsComponent = null;
		
		String lineaTitulo	= params.get(IConstantesReporte.nombreInstitucion);
		String linea2		= params.get(IConstantesReporte.nitInstitucion);
		
		String linea3		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.tituloReporte");
		String linea4		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.tipoConsulta")
									+espacio+params.get(IConstantesReporte.tipoConsulta);
		String linea5		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.fecha")+espacio;
		String fecha		= params.get(IConstantesReporte.fecha);
		String convenio		= "";
		String contrato		= "";
		if(params.get(IConstantesReporte.convenio) != null && !params.get(IConstantesReporte.convenio).isEmpty()){
			convenio = messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.convenio")+espacio+params.get(IConstantesReporte.convenio);
		}
		if(params.get(IConstantesReporte.contrato) != null && !params.get(IConstantesReporte.contrato).isEmpty()){
			contrato = messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.contrato")+espacio+params.get(IConstantesReporte.contrato);
		}
		
		String linea8		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.valorContrato")+espacio;
		String valorMensual	= params.get(IConstantesReporte.valorMensualContrato);
		String linea9		= messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.porcentajeValorGasto")+espacio;
		String porcentajeGastoMensual	= params.get(IConstantesReporte.porcentajeGastoMensual);
		String valorGastoMensual	= params.get(IConstantesReporte.valorGastoMensual);
		
		HorizontalListBuilder convenciones = cmp.horizontalList();
		convenciones.add(cmp.text(messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.convenciones")).setStyle(EstilosReportesDinamicos.estiloSubTituloL));
		for(ParametrizacionSemaforizacion parame:semaforizacion){
			convenciones.add(cmp.text(parame.getRangoInicial()+"% - "+parame.getRangoFinal()+"%")
								.setHorizontalAlignment(HorizontalAlignment.RIGHT)
								.setStyle(EstilosReportesDinamicos.estiloSubTitulo));
			StringBuilder buildColor = new StringBuilder(parame.getColor());
			buildColor.replace(0, 1, "0x");
			Color color= Color.decode(buildColor.toString());
			convenciones.add(cmp.text(espacio).setDimension(10, 10).setStyle(stl.style().setBackgroundColor(color).setForegroudColor(color)));
		}
		
		
		ComponentBuilder encabezado;
		if(params.get(IConstantesReporte.ubicacionLogo).
				equals(ConstantesIntegridadDominio.acronimoUbicacionIzquierda)){
			//Componente del Logo
			encabezado=cmp.horizontalList(
						cmp.image(params.get(IConstantesReporte.rutaLogo)).setDimension(60, 30)
							.setStyle(stl.style().setLeftPadding(20)).setWidth(60)
						//Componente del Titulo y Subtitulos
						,cmp.verticalList(
							cmp.text(lineaTitulo).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
							cmp.text(linea2).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
							cmp.text(linea3).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
							cmp.text(linea4).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER)
						)
					);
		}
		else{
			//Componente del Logo
			encabezado=cmp.horizontalList(
						//Componente del Titulo y Subtitulos
						cmp.verticalList(
							cmp.text(lineaTitulo).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
							cmp.text(linea2).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
							cmp.text(linea3).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
							cmp.text(linea4).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER)
						),
						cmp.image(params.get(IConstantesReporte.rutaLogo)).setDimension(60, 30)
							.setStyle(stl.style().setLeftPadding(20)).setWidth(60)
					);
		}
		
		dynamicReportsComponent = 
			cmp.verticalList(
				encabezado,
				cmp.horizontalList(cmp.text(espacio))
				,cmp.verticalList(
						//Componente de los parametros seleccionados por el usurio para
						//generar el reporte
						cmp.text(linea5+fecha+espacioLargo+convenio)
							.setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.LEFT),
						cmp.text(contrato+espacioLargo+linea8+valorMensual+espacio+linea9+porcentajeGastoMensual+"% - "+valorGastoMensual)
							.setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.LEFT)
				)
				,convenciones.setStretchType(StretchType.RELATIVE_TO_TALLEST_OBJECT)
			);

		return dynamicReportsComponent;
	}
	
	
	/**
	 * Crea la plantilla del reporte de Ordenes de Capitación Subcontratada para todos los formatos. 
	 * @return ReportTemplateBuilder
	 */
	public ReportTemplateBuilder crearPlantillaReporte()
	{
		ReportTemplateBuilder reportTemplate;
		
		StyleBuilder rootStyle 				= stl.style().setPadding(1).setFontSize(7);
		StyleBuilder columnStyle 			= stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
													.setBorder(stl.pen1Point().setLineColor(Color.WHITE));
		StyleBuilder columnTitleStyle		= stl.style(columnStyle).setHorizontalAlignment(HorizontalAlignment.CENTER)
										        .setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7)
										        .setBorder(stl.pen1Point().setLineColor(Color.WHITE));
		StyleBuilder boldStyle          	= stl.style(rootStyle).bold();
		StyleBuilder groupStyle				= stl.style(boldStyle).setPadding(1).setFontSize(7)
													.setHorizontalAlignment(HorizontalAlignment.LEFT)
													.setBorder(stl.pen1Point().setLineColor(Color.WHITE));
		StyleBuilder subtotalStyle      	= stl.style(EstilosReportesDinamicos.estiloSubTotalRSB)
													.setBorder(stl.pen1Point().setLineColor(Color.WHITE));

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
