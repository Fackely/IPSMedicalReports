package com.servinte.axioma.generadorReporte.tesoreria.consultaConsolidadCierre;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.sbt;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.subtotal.SubtotalBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.jasperreports.engine.JRDataSource;

import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

import util.UtilidadTexto;
import util.reportes.dinamico.EstilosReportesDinamicos;

import com.princetonsa.dto.tesoreria.DtoConsolidadoCierreReporte;
import com.servinte.axioma.orm.FormasPago;
 
public class GeneradorReporteConsultaConsolidadaCierres {

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	public static Logger logger = Logger.getLogger(GeneradorReporteConsultaConsolidadaCierres.class);


	/**
	 * Mensajes parametrizados de los reportes 
	 */
	private static MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.tesoreria.ConsultarConsolidadosCierres");



	public GeneradorReporteConsultaConsolidadaCierres() {
		// TODO Auto-generated constructor stub
	}	


	/**
	 * @param consolidadoCierre
	 * @param params
	 * @return JasperReportBuilder del reporte generado
	 * @throws Exception
	 */
	public   JasperReportBuilder  buildReportFormatoA(ArrayList<DtoConsolidadoCierreReporte> consolidadoCierre, 
			Map<String, String> params,List<FormasPago> formasPago) throws Exception{
		//Inicialización del reporte
		JasperReportBuilder reportFormatoA = report();
		try{
			//Instancia de la clase que genera el diseño del reporte
			GeneradorDisenioReporteConsultaCierre disenio = new GeneradorDisenioReporteConsultaCierre();
			reportFormatoA
			.setTemplate(disenio.crearPlantillaReporte()) 
			.setPageMargin(disenio.crearMagenesReporte())
			.pageHeader(disenio.crearComponenteEncabezadoFormatoA(params));

			if(!consolidadoCierre.isEmpty()){
				//Se agrega el subreporte de convenios autorizados al reporte principal
				reportFormatoA.summary(cmp.subreport(buildSubReportAutorizado(consolidadoCierre,formasPago)));
			}

			//Obliga a que todos los subreportes muestren el page Header/Footer
			reportFormatoA.setSummaryWithPageHeaderAndFooter(true)
			//Se le agrega el componente Footer de todo el reporte
			.pageFooter(disenio.crearcomponentePiePagina(params))
			.pageFooter(cmp.pageXofY().setFormatExpression("Pag. {0} de {1}"))
			.build();
		}
		catch(Exception e){
			logger.error(e);
			throw e;
		}
		return reportFormatoA;
	}






	/**
	 * Método encargado de generar el reporte plano cierres
	 * @param consolidadoCierre
	 * @param params
	 * @return JasperReportBuilder el reporte compilado
	 * @throws Exception
	 */
	public  JasperReportBuilder  buildReportPlanoFormatoA(ArrayList<DtoConsolidadoCierreReporte> consolidadoCierre, 
			Map<String, String> params,List<FormasPago> formasPago) throws Exception{
		//Inicialización del reporte
		JasperReportBuilder reportPlanoFormatoA = report();
			///////
			
			int tamanio=formasPago.size();
			TextColumnBuilder []cols = new TextColumnBuilder[tamanio+2];
			//Instancia de la clase que permite obtener el nombre del total
			UtilSimpleExpression expression = new UtilSimpleExpression();
			
			
			
			try{
				//Instancia de la clase que genera los diseños del reporte
				GeneradorDisenioReporteConsultaCierre disenio = new GeneradorDisenioReporteConsultaCierre();
				
				TextColumnBuilder<String>     centroAtencion  = col.column(messageResource.getMessage("consolidacion_cierres_label_reporte_centro_atencion"),  "centroAtencion",  type.stringType());
				TextColumnBuilder<String>     institucion  = col.column(messageResource.getMessage("consolidacion_cierres_label_reporte_intitucion"),  "Intitucion",  type.stringType());
				
				cols[0]=institucion;
				cols[1]=centroAtencion;
				//TODO validar tildes
				for (int i = 2; i < tamanio+2; i++) {
				 	TextColumnBuilder<BigDecimal> columnaDinamica= col.column(UtilidadTexto.primeraLetraMayuscula(formasPago.get(i-2).getDescripcion()),  UtilidadTexto.primeraLetraMayuscula(formasPago.get(i-2).getDescripcion()),  type.bigDecimalType()).setStretchWithOverflow(true);
				 	cols[i]=columnaDinamica.setStretchWithOverflow(true);
				}
			reportPlanoFormatoA
			.title(disenio.crearComponenteEncabezadoPlanoFormatoA(params,formasPago))
			.columns(cols)
			.setShowColumnTitle(false)
			.setDataSource(getDataSourceConsultaConsolidadoCierrePlano(consolidadoCierre,formasPago))
			.build();
		}
		catch(Exception e){
			logger.error(e);
			throw e;
		}
		return reportPlanoFormatoA;
	}




	/**
	 * Método encargado de generar el subreporte de  cierres
	 * @param consolidadoCierre
	 * @param params
	 * @return JasperReportBuilder el reporte compilado
	 * @throws Exception
	 */
	public  JasperReportBuilder buildSubReportAutorizado(ArrayList<DtoConsolidadoCierreReporte> consolidadoCierre,List<FormasPago> formasPago) throws Exception{
		//Inicialización del reporte
		JasperReportBuilder reportAutorizados = report();
		
		int tamanio=formasPago.size();
		TextColumnBuilder []cols = new TextColumnBuilder[tamanio+2];
		SubtotalBuilder [] subTotales= new SubtotalBuilder[tamanio+2];
		//Instancia de la clase que permite obtener el nombre del total
		UtilSimpleExpression expression = new UtilSimpleExpression();
		
		
		
		try{
			//Instancia de la clase que genera los diseños del reporte
			GeneradorDisenioReporteConsultaCierre disenio = new GeneradorDisenioReporteConsultaCierre();
			
			TextColumnBuilder<String>     centroAtencion  = col.column(messageResource.getMessage("consolidacion_cierres_label_reporte_centro_atencion"),  "centroAtencion",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen1Point().setLineColor(Color.WHITE)));
			
			
			cols[0]=centroAtencion;
			//TODO validar tildes
			for (int i = 1; i < tamanio+1; i++) {
			 	TextColumnBuilder<BigDecimal> columnaDinamica= col.column(UtilidadTexto.primeraLetraMayuscula(formasPago.get(i-1).getDescripcion()),  UtilidadTexto.primeraLetraMayuscula(formasPago.get(i-1).getDescripcion()),  type.bigDecimalType()).setStretchWithOverflow(true);
			 	cols[i]=columnaDinamica.setStretchWithOverflow(true).setStyle(EstilosReportesDinamicos.estiloBordeBR.setVerticalAlignment(VerticalAlignment.MIDDLE));
			}
			
			TextColumnBuilder<BigDecimal> total=col.column(messageResource.getMessage("consolidacion_cierres_label_reporte_saldo_total"),  "totalCentroAtencion",  type.bigDecimalType()).setStyle(EstilosReportesDinamicos.estiloNegrillaR);
			cols[tamanio+1]=total;
			
			//var arg de subtotales
			
			
			SubtotalBuilder valorCustom = sbt.customValue(expression.getExpressionTotalCierre(), centroAtencion)
			.setStyle(EstilosReportesDinamicos.estiloTituloSombreadoL.setVerticalAlignment(VerticalAlignment.MIDDLE));
			subTotales[0]=valorCustom;
			
			for (int i = 1; i < tamanio+2; i++) {
				valorCustom = (SubtotalBuilder) sbt.sum(cols[i]).setStyle(EstilosReportesDinamicos.estiloTituloSombreadoRight);
				subTotales[i]=valorCustom;
			}
			
			

		reportAutorizados
			.setTemplate(disenio.crearPlantillaReporte()) 
			//Se se colocan margenes en 0
			.setPageMargin(disenio.crearMagenesSubReporte())
			
			.columns(cols) 
					
							//Se define que se realizara totalización por cada convenio
							.subtotalsAtSummary(  
									subTotales
							)
							
							.lastPageFooter(cmp.text("").setHeight(20))
							.setDataSource(getDataSourceConsultaConsolidadoCierrePDF(consolidadoCierre,formasPago))
							.build();  
		} catch (Exception e) {  
			logger.error(e);
			throw e;  
		}
		return reportAutorizados;
	}







	/**
	 * Método que obtiene el data source de los cierres
	 * @param consolidadoCierre
	 * @return JRDataSource
	 */
	private   JRDataSource getDataSourceConsultaConsolidadoCierrePDF(ArrayList<DtoConsolidadoCierreReporte> consolidadoCierre,List<FormasPago> formasPago) {
		ConsolidadoCierreDataSource dataSource = new ConsolidadoCierreDataSource(consolidadoCierre,formasPago);
		return dataSource.crearDataSource();
	}


	/**
	 * Método que obtiene el data source de los cierres para generar el reporte plano
	 * @param consolidadoCierre
	 * @return JRDataSource
	 */
	private  JRDataSource getDataSourceConsultaConsolidadoCierrePlano(ArrayList<DtoConsolidadoCierreReporte> consolidadoCierre,List<FormasPago> formasPago ) {
		ConsolidadoCierreDataSource dataSource = new ConsolidadoCierreDataSource(consolidadoCierre,formasPago);
		return dataSource.crearDataSourcePlano();
	}



	

}
