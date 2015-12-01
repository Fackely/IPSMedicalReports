package com.servinte.axioma.generadorReporte.tesoreria.consultaConsolidadCierre;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.grp;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.sbt;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.group.ColumnGroupBuilder;
import net.sf.dynamicreports.report.builder.subtotal.SubtotalBuilder;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.jasperreports.engine.JRDataSource;

import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

import util.UtilidadTexto;
import util.reportes.dinamico.EstilosReportesDinamicos;

import com.princetonsa.dto.tesoreria.DtoConsolidadoCierreReporte;
import com.servinte.axioma.orm.FormasPago;
 
public class GeneradorReporteConsultaConsolidadaCierresCajaCajero {

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	public static Logger logger = Logger.getLogger(GeneradorReporteConsultaConsolidadaCierresCajaCajero.class);

	/**
	 * Mensajes parametrizados de titulos de reporte.
	 */
	private MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.tesoreria.ConsultarConsolidadosCierres");


	public GeneradorReporteConsultaConsolidadaCierresCajaCajero() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param consolidadoCierre
	 * @param params
	 * @return JasperReportBuilder del reporte generado
	 * @throws Exception
	 */
	public    JasperReportBuilder  buildReportFormatoA(ArrayList<DtoConsolidadoCierreReporte> consolidadoCierre, 
			Map<String, String> params,List<FormasPago> formasPago) throws Exception{
		//Inicialización del reporte
		JasperReportBuilder reportFormatoA = report();
		try{
			//Instancia de la clase que genera el diseño del reporte
			GeneradorDisenioReporteConsultaCierreCajaCajero disenio = new GeneradorDisenioReporteConsultaCierreCajaCajero();
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
		}//control de errores
		catch(Exception e){
			logger.error(e);
			throw e;
		}
		return reportFormatoA;
	}


	/**
	 * Método encargado de generar el subreporte de  cierres de caja cajero
	 * @param cierres
	 * @param params
	 * @return JasperReportBuilder el reporte compilado
	 * @throws Exception
	 */
	public   JasperReportBuilder buildSubReportAutorizado(ArrayList<DtoConsolidadoCierreReporte> consolidadoCierre,List<FormasPago> formasPago) throws Exception{
		//Inicialización del reporte
		JasperReportBuilder reportAutorizados = report();
		int tamanio=formasPago.size();

		TextColumnBuilder []cols = new TextColumnBuilder[tamanio+6];
		SubtotalBuilder [] subTotales= new SubtotalBuilder[tamanio+4];
		SubtotalBuilder [] totales= new SubtotalBuilder[tamanio+4];

		UtilSimpleExpression expression = new UtilSimpleExpression();

		try{
			//Instancia de la clase que genera los diseños del reporte
			GeneradorDisenioReporteConsultaCierreCajaCajero disenio = new GeneradorDisenioReporteConsultaCierreCajaCajero();

			//Se definen las columnas que tendra el subreporte
			TextColumnBuilder<String>     Intitucion  = col.column(messageResource.getMessage("consolidacion_cierres_label_reporte_caja_cajero_intitucion"),  "Intitucion",  type.stringType());
			TextColumnBuilder<String>     centroAtencion  = col.column(messageResource.getMessage("consolidacion_cierres_label_reporte_caja_cajero_centro_atencion"),  "centroAtencion",  type.stringType());
			TextColumnBuilder<String>     caja  = col.column(messageResource.getMessage("consolidacion_cierres_label_reporte_caja_cajero_caja"),  "caja",  type.stringType());
			TextColumnBuilder<String>     cajero  = col.column(messageResource.getMessage("consolidacion_cierres_label_reporte_caja_cajero_cajero"),  "Cajero",  type.stringType());
			TextColumnBuilder<String>     horaTurnoCajero  = col.column(messageResource.getMessage("consolidacion_cierres_label_reporte_caja_cajero_turno"),  "horaTurnoCajero",  type.stringType());

			cols[0]=Intitucion.setStretchWithOverflow(true);
			cols[1]=centroAtencion.setStretchWithOverflow(true);
			cols[2]=caja.setStretchWithOverflow(true);
			cols[3]=cajero.setStretchWithOverflow(true);
			cols[4]=horaTurnoCajero.setStretchWithOverflow(true);


			for (int i = 5; i < tamanio+5; i++) {
				TextColumnBuilder<BigDecimal> columnaDinamica= col.column(UtilidadTexto.primeraLetraMayuscula(formasPago.get(i-5).getDescripcion()),  UtilidadTexto.primeraLetraMayuscula(formasPago.get(i-5).getDescripcion()),  type.bigDecimalType()).setStretchWithOverflow(true);
				cols[i]=columnaDinamica.setStretchWithOverflow(true).setStyle(EstilosReportesDinamicos.estiloBordeBR.setVerticalAlignment(VerticalAlignment.MIDDLE));
			}

			TextColumnBuilder<BigDecimal>     totalCentroAtencion  = col.column(messageResource.getMessage("consolidacion_cierres_label_reporte_caja_cajero_total_centro_atencion"),  "totalCentroAtencion",  type.bigDecimalType()).setStyle(EstilosReportesDinamicos.estiloBordeBRNegrilla.setVerticalAlignment(VerticalAlignment.MIDDLE));
			cols[tamanio+5]=totalCentroAtencion.setStretchWithOverflow(true);




			SubtotalBuilder valorCustom2 = sbt.customValue(expression.getExpressionTotalCentroAtencionCajaCajero(), caja)
			.setStyle(EstilosReportesDinamicos.estiloTituloSombreadoL);


			SubtotalBuilder valorCustom3 = sbt.customValue(expression.getExpressionCajero(), cajero)
			.setStyle(EstilosReportesDinamicos.estiloTituloSombreadoL);

			SubtotalBuilder valorCustom = sbt.customValue(expression.getExpressionCajero2(), horaTurnoCajero)
			.setStyle(EstilosReportesDinamicos.estiloTituloSombreadoL);
			subTotales[0]=valorCustom2;
			subTotales[1]=valorCustom3;
			subTotales[2]=valorCustom;




			for (int i = 3; i < tamanio+3; i++) {
				valorCustom = (SubtotalBuilder) sbt.sum(cols[i+2]).setStyle(EstilosReportesDinamicos.estiloTituloSombreadoRight);
				subTotales[i]=valorCustom;
			}
			subTotales[tamanio+3]=(SubtotalBuilder) sbt.sum(cols[tamanio+5]).setStyle(EstilosReportesDinamicos.estiloTituloSombreadoRight);


			
			// se adicionan subtotales
			SubtotalBuilder valorCustom4 = sbt.customValue(expression.getExpressionTotalCentroAtencionCajaCajero2(), caja)
			.setStyle(EstilosReportesDinamicos.estiloTituloSombreadoL);


			SubtotalBuilder valorCustom5 = sbt.customValue(expression.getExpressionCajero3(), cajero)
			.setStyle(EstilosReportesDinamicos.estiloTituloSombreadoL);

			SubtotalBuilder valorCustom6 = sbt.customValue(expression.getExpressionCajero4(), horaTurnoCajero)
			.setStyle(EstilosReportesDinamicos.estiloTituloSombreadoL);
			totales[0]=valorCustom4;
			totales[1]=valorCustom5;
			totales[2]=valorCustom6;




			for (int i = 3; i < tamanio+3; i++) {
				valorCustom = (SubtotalBuilder) sbt.sum(cols[i+2]).setStyle(EstilosReportesDinamicos.estiloTituloSombreadoRight);
				totales[i]=valorCustom;
			}
			totales[tamanio+3]=(SubtotalBuilder) sbt.sum(cols[tamanio+5]).setStyle(EstilosReportesDinamicos.estiloTituloSombreadoRight);
			
		// se adicionan sumas finales
			
			
			ColumnGroupBuilder efectivoGroup = grp.group(Intitucion)
			.setStyle(EstilosReportesDinamicos.estiloSubTituloSombraBLOscura)
			.setPadding(0);

			ColumnGroupBuilder centroAtencionGroup = grp.group(centroAtencion)
			.setStyle(EstilosReportesDinamicos.estiloSubTituloSombraBL)
			.setPadding(0);




			reportAutorizados
			.setTemplate(disenio.crearPlantillaReporte()) 
			//Se se colocan margenes en 0
			.setPageMargin(disenio.crearMagenesSubReporte())
			.columns(cols)  
			.groupBy(  
					efectivoGroup,centroAtencionGroup)
					//Se define que se realizara totalización por cada convenio
					.subtotalsAtGroupFooter( centroAtencionGroup,subTotales)
					.subtotalsAtSummary(totales)
					//.groupFooter(efectivoGroup, cmp.text("").setHeight(10))
					.groupFooter(centroAtencionGroup, cmp.text("").setHeight(10))
					//.lastPageFooter(cmp.text("").setHeight(20))
					.setDataSource(getDataSourceConsultaConsolidadoCierrePDF(consolidadoCierre,formasPago))
					.build();  
		}//control de erroes
		catch (Exception e) {  
			logger.error(e);
			throw e;
		}
		return reportAutorizados;
	}





	/**
	 * @param consolidadoCierre
	 * @param params
	 * @return JasperReportBuilder del reporte plano 
	 * @throws Exception
	 */
	public  JasperReportBuilder  buildReportPlanoFormatoA(ArrayList<DtoConsolidadoCierreReporte> consolidadoCierre, 
			Map<String, String> params,List<FormasPago> formasPago) throws Exception{
		//Inicialización del reporte
		JasperReportBuilder reportPlanoFormatoA = report();
		int tamanio=formasPago.size();
		TextColumnBuilder []cols = new TextColumnBuilder[tamanio+5];
		try{
			//Instancia de la clase que genera el diseño del reporte
			GeneradorDisenioReporteConsultaCierreCajaCajero disenio = new GeneradorDisenioReporteConsultaCierreCajaCajero();

			TextColumnBuilder<String>     Intitucion  = col.column(messageResource.getMessage("consolidacion_cierres_label_reporte_caja_cajero_intitucion"),  "Intitucion",  type.stringType());
			TextColumnBuilder<String>     centroAtencion  = col.column(messageResource.getMessage("consolidacion_cierres_label_reporte_caja_cajero_centro_atencion"),  "centroAtencion",  type.stringType());
			TextColumnBuilder<String>     caja  = col.column(messageResource.getMessage("consolidacion_cierres_label_reporte_caja_cajero_caja"),  "caja",  type.stringType());
			TextColumnBuilder<String>     cajero  = col.column(messageResource.getMessage("consolidacion_cierres_label_reporte_caja_cajero_cajero"),  "Cajero",  type.stringType());
			TextColumnBuilder<String>     horaTurnoCajero  = col.column(messageResource.getMessage("consolidacion_cierres_label_reporte_caja_cajero_turno"),  "horaTurnoCajero",  type.stringType());

			cols[0]=Intitucion;
			cols[1]=centroAtencion;
			cols[2]=caja;
			cols[3]=cajero;
			cols[4]=horaTurnoCajero;


			for (int i = 5; i < tamanio+5; i++) {
				TextColumnBuilder<BigDecimal> columnaDinamica= col.column(UtilidadTexto.primeraLetraMayuscula(formasPago.get(i-5).getDescripcion()),  UtilidadTexto.primeraLetraMayuscula(formasPago.get(i-5).getDescripcion()),  type.bigDecimalType()).setStretchWithOverflow(true);
				cols[i]=columnaDinamica.setStretchWithOverflow(true).setStyle(EstilosReportesDinamicos.estiloBordeBR);
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
	 * Método que obtiene el data source de los cierres de caja cajero el reporte plano
	 * @param conveniosAutorizados
	 * @param conveniosNoAutorizados
	 * @return JRDataSource
	 */
	private  JRDataSource getDataSourceConsultaConsolidadoCierrePlano(ArrayList<DtoConsolidadoCierreReporte> consolidadoCierre,List<FormasPago> formasPago ) {
		ConsolidadoCierreDataSourceCajaCajero dataSource = new ConsolidadoCierreDataSourceCajaCajero(consolidadoCierre,formasPago);
		return dataSource.crearDataSourcePlano();
	}




	/**
	 * Método que obtiene el data source de los cierres autorizados
	 * @param conveniosAutorizados
	 * @return JRDataSource
	 */
	private   JRDataSource getDataSourceConsultaConsolidadoCierrePDF(ArrayList<DtoConsolidadoCierreReporte> consolidadoCierre,List<FormasPago> formasPago) {
		ConsolidadoCierreDataSourceCajaCajero dataSource = new ConsolidadoCierreDataSourceCajaCajero(consolidadoCierre,formasPago);
		return dataSource.crearDataSource();
	}

	
	
	


}
