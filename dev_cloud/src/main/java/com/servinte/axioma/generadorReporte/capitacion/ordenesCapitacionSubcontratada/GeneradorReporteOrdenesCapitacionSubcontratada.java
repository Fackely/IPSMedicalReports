package com.servinte.axioma.generadorReporte.capitacion.ordenesCapitacionSubcontratada;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.cnd;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

import static net.sf.dynamicreports.report.builder.DynamicReports.*; 
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.group.ColumnGroupBuilder;
import net.sf.dynamicreports.report.builder.style.ConditionalStyleBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.SplitType;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.jasperreports.engine.JRDataSource;

import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

import util.ConstantesIntegridadDominio;
import util.reportes.dinamico.EstilosReportesDinamicos;

import com.servinte.axioma.dto.capitacion.DtoContratoReporte;
import com.servinte.axioma.dto.capitacion.DtoConvenioReporte;
import com.servinte.axioma.dto.capitacion.DtoNivelReporte;
import com.servinte.axioma.mundo.fabrica.AdministracionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.administracion.IParametrizacionSemaforizacionMundo;
import com.servinte.axioma.orm.ParametrizacionSemaforizacion;
import com.servinte.axioma.persistencia.UtilidadTransaccion;


/**
 * Clase para generar el reporte de Ordenes de Capitación Subcontratada
 * en todos sus formatos
 * 
 * @version 1.0, May 7, 2011
 * @author <a href="mailto:ricardo.ruiz@servinte.com.co">Ricardo Ruiz</a>
 * 
 */
public class GeneradorReporteOrdenesCapitacionSubcontratada{
	
	/**
     * Objeto para manejar los logs de esta clase
    */
    public static Logger logger = Logger.getLogger(GeneradorReporteOrdenesCapitacionSubcontratada.class);
	
    /** * Contiene los mensajes correspondiente a este Generador*/
	MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.capitacion.GeneradorReporteOrdenCapitacionSubcontratada");
    
	/**
	 * Contructor de la clase
	 */
	public GeneradorReporteOrdenesCapitacionSubcontratada(){
	}
	
	
	/**
	 * Método encargado de generar el reporte de Ordenes de Capitación
	 * Subcontratada en el formato A
	 * @param conveniosAutorizados
	 * @param conveniosNoAutorizados
	 * @param params
	 * @return JasperReportBuilder el reporte compilado
	 * @throws Exception
	 */
	public JasperReportBuilder  buildReportFormatoA(ArrayList<DtoConvenioReporte> conveniosAutorizados,
			ArrayList<DtoConvenioReporte> conveniosNoAutorizados, Map<String, String> params) throws Exception{
		//Inicialización del reporte
		JasperReportBuilder reportFormatoA = report();
		try{
			//Instancia de la clase que genera el diseño del reporte
			GeneradorDisenioReporte disenio = new GeneradorDisenioReporte();
	        ArrayList<ParametrizacionSemaforizacion> semaforizacion = obtenerParametrizacionSemaforizacion();
			reportFormatoA
	        	  .setTemplate(disenio.crearPlantillaReporte()) 
	        	  .setPageMargin(disenio.crearMagenesReporte())
	        	  .pageHeader(disenio.crearComponenteEncabezadoFormatoA(params, semaforizacion));
	        	if(!conveniosAutorizados.isEmpty()){
	        		if(!conveniosNoAutorizados.isEmpty()){
	        			//Se agregan los dos tipos de convenios
	  	              	reportFormatoA.summary(cmp.subreport(buildSubReportAmbos(conveniosAutorizados, conveniosNoAutorizados)));
	        		}
	        		else{
	        			//Se agregan los dos tipos de convenios
	  	              	reportFormatoA.summary(cmp.subreport(buildSubReportAutorizado(conveniosAutorizados)));
	        		}
	        	}
	        	else{
	        		if(!conveniosNoAutorizados.isEmpty()){
			              //Se agrega el subreporte de convenios NO autorizados al reporte principal
			              reportFormatoA.summary(cmp.subreport(buildSubReportNoAutorizado(conveniosNoAutorizados)));
	        		}
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
	 * Método encargado de generar el reporte plano de Ordenes de Capitación
	 * Subcontratada en el formato A
	 * @param conveniosAutorizados
	 * @param conveniosNoAutorizados
	 * @param params
	 * @return JasperReportBuilder el reporte compilado
	 * @throws Exception
	 */
	public JasperReportBuilder  buildReportPlanoFormatoA(ArrayList<DtoConvenioReporte> conveniosAutorizados,
			ArrayList<DtoConvenioReporte> conveniosNoAutorizados, Map<String, String> params) throws Exception{
		//Inicialización del reporte
		JasperReportBuilder reportPlanoFormatoA = report();
		try{
			//Instancia de la clase que genera el diseño del reporte
			GeneradorDisenioReporte disenio = new GeneradorDisenioReporte();
			
			//Se definen las columnas que tendra el Reporte
			TextColumnBuilder<String>     tipoConvenioColumn  = col.column(
																	messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnTipoConvenio"),
																	"tipoConvenio",  type.stringType());
			TextColumnBuilder<String>     convenioColumn  = col.column(
																	messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnNombreConvenio"),
																	"nombreConvenio",  type.stringType());
			TextColumnBuilder<String>     contratoColumn  = col.column(
																	messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.contratoPlano"),
																	"numeroContrato",  type.stringType());
			TextColumnBuilder<String>     nivelColumn  = col.column(
																	messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnNivelAtencion"),
																	"nombre",  type.stringType());
			TextColumnBuilder<BigDecimal>     presupuestadoColumn  = col.column(
																		messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVPresupuestado"),
																		"totalPresupuestado",  type.bigDecimalType());
			TextColumnBuilder<BigDecimal>     ordenadoColumn  = col.column(
																		messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVOrdenado"),
																		"totalOrdenado",  type.bigDecimalType());															
			TextColumnBuilder<BigDecimal>     autorizadoColumn  = col.column(
																		messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVAutorizado"),
																		"totalAutorizado",  type.bigDecimalType());
			TextColumnBuilder<BigDecimal>     cargosColumn  = col.column(
																		messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVCargos"),
																		"totalCargosCuenta",  type.bigDecimalType());
			TextColumnBuilder<BigDecimal>     facturadoColumn  = col.column(
																		messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVFacturado"),
																		"totalFacturado",  type.bigDecimalType());		
			TextColumnBuilder<BigDecimal>     porcentajeOrdenadoColumn  = col.column(
																				messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPorcentaje"),
																				"porcentajeOrdenado",  type.bigDecimalType());
			TextColumnBuilder<BigDecimal>     porcentajeAutorizadoColumn  = col.column(
																					messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPorcentaje"),
																					"porcentajeAutorizado",  type.bigDecimalType());
			TextColumnBuilder<BigDecimal>     porcentajeCargosColumn  = col.column(
																				messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPorcentaje"),
																				"porcentajeCargos",  type.bigDecimalType());
			TextColumnBuilder<BigDecimal>     porcentajeFacturadoColumn  = col.column(
																				messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPorcentaje"),
																				"porcentajeFacturado",  type.bigDecimalType());	
			
	        reportPlanoFormatoA
	        	  .title(disenio.crearComponenteEncabezadoPlanoFormatoA(params))
	        	  .columns(tipoConvenioColumn, convenioColumn, contratoColumn,
	        			  nivelColumn, presupuestadoColumn, ordenadoColumn,
	        			  porcentajeOrdenadoColumn, autorizadoColumn, porcentajeAutorizadoColumn,
	        			  cargosColumn, porcentajeCargosColumn, facturadoColumn,
	        			  porcentajeFacturadoColumn)
	        	  .setShowColumnTitle(false)
	        	  .setDetailSplitType(SplitType.PREVENT)
	        	  .setDataSource(getDataSourcePlanoConvenios(conveniosAutorizados, conveniosNoAutorizados))
	              .build();
		}
		catch(Exception e){
			logger.error(e);
			throw e;
		}
		return reportPlanoFormatoA;
	}
	
	/**
	 * Método encargado de generar el reporte de Ordenes de Capitación
	 * Subcontratada en el formato B
	 * @param contratos
	 * @param params
	 * @return JasperReportBuilder el reporte compilado
	 * @throws Exception
	 */
	public JasperReportBuilder buildReportFormatoB(ArrayList<DtoContratoReporte> contratos, Map<String, String> params) throws Exception{
		//Inicialización del reporte
		JasperReportBuilder reportFormatoB = report();
		try{
			//Instancia de la clase que genera los diseños del reporte
			GeneradorDisenioReporte disenio = new GeneradorDisenioReporte();
			
			//Se definen las columnas que tendra el Reporte
			TextColumnBuilder<String>     contratoColumn  = col.column(
																	messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.contratoPlano"),
																	"numeroContrato",  type.stringType());
			TextColumnBuilder<String>     nivelColumn  = col.column(
																	messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnNivelAtencion"),
																	"nombreNivel",  type.stringType());
			TextColumnBuilder<String>     grupoClaseColumn  = col.column(
																	messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnDescripcion"),
																	"nombreGrupoClase",  type.stringType());
			TextColumnBuilder<BigDecimal>     presupuestadoColumn  = col.column(
																		messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVPresupuestado"),
																		"totalPresupuestado",  type.bigDecimalType());
			TextColumnBuilder<BigDecimal>     ordenadoColumn  = col.column(
																		messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVOrdenado"),
																		"totalOrdenado",  type.bigDecimalType());															
			TextColumnBuilder<BigDecimal>     autorizadoColumn  = col.column(
																		messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVAutorizado"),
																		"totalAutorizado",  type.bigDecimalType());
			TextColumnBuilder<BigDecimal>     cargosColumn  = col.column(
																		messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVCargos"),
																		"totalCargosCuenta",  type.bigDecimalType());
			TextColumnBuilder<BigDecimal>     facturadoColumn  = col.column(
																		messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVFacturado"),
																		"totalFacturado",  type.bigDecimalType());		
			TextColumnBuilder<BigDecimal>     porcentajeOrdenadoColumn  = col.column(
																				messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPorcentaje"),
																				"porcentajeOrdenado",  type.bigDecimalType()).setWidth(35);
			TextColumnBuilder<BigDecimal>     porcentajeAutorizadoColumn  = col.column(
																					messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPorcentaje"),
																					"porcentajeAutorizado",  type.bigDecimalType()).setWidth(35);
			TextColumnBuilder<BigDecimal>     porcentajeCargosColumn  = col.column(
																				messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPorcentaje"),
																				"porcentajeCargos",  type.bigDecimalType()).setWidth(35);
			TextColumnBuilder<BigDecimal>     porcentajeFacturadoColumn  = col.column(
																				messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPorcentaje"),
																				"porcentajeFacturado",  type.bigDecimalType()).setWidth(35);		
			//Se obtiene la paramretrizacion de semaforizacion
			ArrayList<ParametrizacionSemaforizacion> parametrizacion=obtenerParametrizacionSemaforizacion();
			//Definición de los estilos de las columnas que requieren que se les
			//aplique la semaforización
			StyleBuilder conditionStyleOrdenado = stl.style().conditionalStyles(obtenerSemaforizacion(porcentajeOrdenadoColumn, parametrizacion)).setParentStyle(EstilosReportesDinamicos.estiloPadre);
			ordenadoColumn.setStyle(conditionStyleOrdenado);
			porcentajeOrdenadoColumn.setStyle(conditionStyleOrdenado);
			StyleBuilder conditionStyleAutorizado= stl.style().conditionalStyles(obtenerSemaforizacion(porcentajeAutorizadoColumn, parametrizacion)).setParentStyle(EstilosReportesDinamicos.estiloPadre);
			autorizadoColumn.setStyle(conditionStyleAutorizado);
			porcentajeAutorizadoColumn.setStyle(conditionStyleAutorizado);
			StyleBuilder conditionStyleCargos= stl.style().conditionalStyles(obtenerSemaforizacion(porcentajeCargosColumn, parametrizacion)).setParentStyle(EstilosReportesDinamicos.estiloPadre);
			cargosColumn.setStyle(conditionStyleCargos);
			porcentajeCargosColumn.setStyle(conditionStyleCargos);
			StyleBuilder conditionStyleFacturado = stl.style().conditionalStyles(obtenerSemaforizacion(porcentajeFacturadoColumn, parametrizacion)).setParentStyle(EstilosReportesDinamicos.estiloPadre);
			facturadoColumn.setStyle(conditionStyleFacturado);
			porcentajeFacturadoColumn.setStyle(conditionStyleFacturado);
			
			//Definición de las columnas por las cuales se realiza la agrupación
			ColumnGroupBuilder contratoGroup = grp.group(contratoColumn)
													.setStyle(EstilosReportesDinamicos.estiloTituloSombraL)
													.setPadding(0);
			ColumnGroupBuilder nivelGroup = grp.group(nivelColumn).setPadding(0)
													.setStyle(EstilosReportesDinamicos.estiloSubTituloSombraL);
			
			//Instancia de la clase que me permite obtener el nombre del total
			UtilSimpleExpression expression = new UtilSimpleExpression();
			reportFormatoB
		          .setTemplate(disenio.crearPlantillaReporte())  
		          .setPageMargin(disenio.crearMagenesReporte())
		          .pageHeader(disenio.crearComponenteEncabezadoFormatoB(params, parametrizacion))
		          .columns(contratoColumn, nivelColumn, 
		            grupoClaseColumn, presupuestadoColumn, 
		            ordenadoColumn, porcentajeOrdenadoColumn, autorizadoColumn,
		            porcentajeAutorizadoColumn, cargosColumn, porcentajeCargosColumn,
		            facturadoColumn, porcentajeFacturadoColumn)  
		          .groupBy(  
		        		  contratoGroup, nivelGroup)
		          //Se define que se van a generar totales por cada contrato
		          .subtotalsAtGroupFooter(  
		            contratoGroup, 
		            sbt.customValue(expression.getExpressionTotalContrato(), grupoClaseColumn)
		            	.setStyle(EstilosReportesDinamicos.estiloSubTituloSombraL),
		            sbt.sum(presupuestadoColumn), 
		            sbt.sum(ordenadoColumn), 
		            sbt.avg(porcentajeOrdenadoColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)),
		            sbt.sum(autorizadoColumn),
		            sbt.avg(porcentajeAutorizadoColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)),
		            sbt.sum(cargosColumn),
		            sbt.avg(porcentajeCargosColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)),
		            sbt.sum(facturadoColumn),
		            sbt.avg(porcentajeFacturadoColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)))
		          //Se define que se van a generar totales por cada nivel de atención
		          .subtotalsAtGroupFooter(  
		            nivelGroup, 
		            sbt.customValue(expression.getExpressionTotalNivel(), grupoClaseColumn)
		            	.setStyle(EstilosReportesDinamicos.estiloSubTituloSombraL),
		            sbt.sum(presupuestadoColumn), 
		            sbt.sum(ordenadoColumn), 
		            sbt.avg(porcentajeOrdenadoColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)),
		            sbt.sum(autorizadoColumn),
		            sbt.avg(porcentajeAutorizadoColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)),
		            sbt.sum(cargosColumn),
		            sbt.avg(porcentajeCargosColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)),
		            sbt.sum(facturadoColumn),
		            sbt.avg(porcentajeFacturadoColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)))
		          //Se define que se van a generar totales por todo el reporte
		          .subtotalsAtSummary(  
		            sbt.customValue(expression.getExpressionTotalConvenio(), grupoClaseColumn)
		            	.setStyle(EstilosReportesDinamicos.estiloSubTituloSombraL),
		            sbt.sum(presupuestadoColumn), 
		            sbt.sum(ordenadoColumn), 
		            sbt.avg(porcentajeOrdenadoColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)),
		            sbt.sum(autorizadoColumn),
		            sbt.avg(porcentajeAutorizadoColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)),
		            sbt.sum(cargosColumn),
		            sbt.avg(porcentajeCargosColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)),
		            sbt.sum(facturadoColumn),
		            sbt.avg(porcentajeFacturadoColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)))
		          //Se define una fila en blanco por cada nivel de atención
		          .groupFooter(contratoGroup, cmp.text("").setHeight(10))
		          .setDetailSplitType(SplitType.PREVENT)
		          .pageFooter(disenio.crearcomponentePiePagina(params))
		          .pageFooter(cmp.pageXofY().setFormatExpression("Pag. {0} de {1}"))
		          .setDataSource(getDataSourceContratos(contratos))
		          .build();
		}
		catch(Exception e){
			logger.error(e);
			throw e;
		}
		return reportFormatoB;
	}
	
	
	/**
	 * Método encargado de generar el reporte plano de Ordenes de Capitación
	 * Subcontratada en el formato A
	 * @param conveniosAutorizados
	 * @param conveniosNoAutorizados
	 * @param params
	 * @return JasperReportBuilder el reporte compilado
	 * @throws Exception
	 */
	public JasperReportBuilder  buildReportPlanoFormatoB(ArrayList<DtoContratoReporte> contratos, Map<String, String> params) throws Exception{
		//Inicialización del reporte
		JasperReportBuilder reportPlanoFormatoB = report();
		try{
			//Instancia de la clase que genera el diseño del reporte
			GeneradorDisenioReporte disenio = new GeneradorDisenioReporte();
			
			//Se definen las columnas que tendra el Reporte
			TextColumnBuilder<String>     contratoColumn  = col.column(
																	messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.contratoPlano"),
																	"numeroContrato",  type.stringType());
			TextColumnBuilder<String>     nivelColumn  = col.column(
																	messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnNivelAtencion"),
																	"nombreNivel",  type.stringType());
			TextColumnBuilder<String>     grupoClaseColumn  = col.column(
																	messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnDescripcion"),
																	"nombreGrupoClase",  type.stringType());
			TextColumnBuilder<BigDecimal>     presupuestadoColumn  = col.column(
																		messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVPresupuestado"),
																		"totalPresupuestado",  type.bigDecimalType());
			TextColumnBuilder<BigDecimal>     ordenadoColumn  = col.column(
																		messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVOrdenado"),
																		"totalOrdenado",  type.bigDecimalType());															
			TextColumnBuilder<BigDecimal>     autorizadoColumn  = col.column(
																		messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVAutorizado"),
																		"totalAutorizado",  type.bigDecimalType());
			TextColumnBuilder<BigDecimal>     cargosColumn  = col.column(
																		messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVCargos"),
																		"totalCargosCuenta",  type.bigDecimalType());
			TextColumnBuilder<BigDecimal>     facturadoColumn  = col.column(
																		messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVFacturado"),
																		"totalFacturado",  type.bigDecimalType());		
			TextColumnBuilder<BigDecimal>     porcentajeOrdenadoColumn  = col.column(
																				messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPorcentaje"),
																				"porcentajeOrdenado",  type.bigDecimalType());
			TextColumnBuilder<BigDecimal>     porcentajeAutorizadoColumn  = col.column(
																					messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPorcentaje"),
																					"porcentajeAutorizado",  type.bigDecimalType());
			TextColumnBuilder<BigDecimal>     porcentajeCargosColumn  = col.column(
																				messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPorcentaje"),
																				"porcentajeCargos",  type.bigDecimalType());
			TextColumnBuilder<BigDecimal>     porcentajeFacturadoColumn  = col.column(
																				messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPorcentaje"),
																				"porcentajeFacturado",  type.bigDecimalType());
			
			reportPlanoFormatoB
	        	  .title(disenio.crearComponenteEncabezadoPlanoFormatoB(params))
	        	  .columns(contratoColumn, nivelColumn, 
			            grupoClaseColumn, presupuestadoColumn, 
			            ordenadoColumn, porcentajeOrdenadoColumn, autorizadoColumn,
			            porcentajeAutorizadoColumn, cargosColumn, porcentajeCargosColumn,
			            facturadoColumn, porcentajeFacturadoColumn)
	        	  .setShowColumnTitle(false)
	        	  .setDetailSplitType(SplitType.PREVENT)
	        	  .setDataSource(getDataSourcePlanoContratos(contratos))
	              .build();
		}
		catch(Exception e){
			logger.error(e);
			throw e;
		}
		return reportPlanoFormatoB;
	}
	
	
	/**
	 * Método encargado de generar el reporte de Ordenes de Capitación
	 * Subcontratada en el formato C
	 * @param nivelesAtencion
	 * @param params
	 * @return JasperReportBuilder el reporte compilado
	 * @throws Exception
	 */
	public JasperReportBuilder buildReportFormatoC(ArrayList<DtoNivelReporte> nivelesAtencion, Map<String, String> params) throws Exception{
		//Se inicializa el reporte
		JasperReportBuilder reportFormatoC = report();
		try{
			//Instancia que genera los diseños del reporte
			GeneradorDisenioReporte disenio = new GeneradorDisenioReporte();
			
			//Se definen las columnas que tendra el Reporte
			TextColumnBuilder<String>     nivelColumn  = col.column(
																	messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnNivelAtencion"),
																	"nombreNivel",  type.stringType());
			TextColumnBuilder<String>     grupoClaseColumn  = col.column(
																	messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnDescripcion"),
																	"nombreGrupoClase",  type.stringType());
			TextColumnBuilder<String>     productoServicioColumn  = col.column(
																	messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnGruposClases"),
																	"nombreProductoServicio",  type.stringType());
			TextColumnBuilder<BigDecimal>     presupuestadoColumn  = col.column(
																		messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVPresupuestado"),
																		"totalPresupuestado",  type.bigDecimalType());
			TextColumnBuilder<BigDecimal>     ordenadoColumn  = col.column(
																		messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVOrdenado"),
																		"totalOrdenado",  type.bigDecimalType());															
			TextColumnBuilder<BigDecimal>     autorizadoColumn  = col.column(
																		messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVAutorizado"),
																		"totalAutorizado",  type.bigDecimalType());
			TextColumnBuilder<BigDecimal>     cargosColumn  = col.column(
																		messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVCargos"),
																		"totalCargosCuenta",  type.bigDecimalType());
			TextColumnBuilder<BigDecimal>     facturadoColumn  = col.column(
																		messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVFacturado"),
																		"totalFacturado",  type.bigDecimalType());		
			TextColumnBuilder<BigDecimal>     porcentajeOrdenadoColumn  = col.column(
																				messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPorcentaje"),
																				"porcentajeOrdenado",  type.bigDecimalType()).setWidth(35);
			TextColumnBuilder<BigDecimal>     porcentajeAutorizadoColumn  = col.column(
																					messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPorcentaje"),
																					"porcentajeAutorizado",  type.bigDecimalType()).setWidth(35);
			TextColumnBuilder<BigDecimal>     porcentajeCargosColumn  = col.column(
																				messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPorcentaje"),
																				"porcentajeCargos",  type.bigDecimalType()).setWidth(35);
			TextColumnBuilder<BigDecimal>     porcentajeFacturadoColumn  = col.column(
																				messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPorcentaje"),
																				"porcentajeFacturado",  type.bigDecimalType()).setWidth(35);
			
			//Se obtiene la paramretrizacion de semaforizacion
			ArrayList<ParametrizacionSemaforizacion> parametrizacion=obtenerParametrizacionSemaforizacion();
			//Se definen los estilos que se aplicaran a las columnas
			//de acuerdo a la semaforización
			StyleBuilder conditionStyleOrdenado = stl.style().conditionalStyles(obtenerSemaforizacion(porcentajeOrdenadoColumn, parametrizacion)).setParentStyle(EstilosReportesDinamicos.estiloPadre);
			ordenadoColumn.setStyle(conditionStyleOrdenado);
			porcentajeOrdenadoColumn.setStyle(conditionStyleOrdenado);
			StyleBuilder conditionStyleAutorizado= stl.style().conditionalStyles(obtenerSemaforizacion(porcentajeAutorizadoColumn, parametrizacion)).setParentStyle(EstilosReportesDinamicos.estiloPadre);
			autorizadoColumn.setStyle(conditionStyleAutorizado);
			porcentajeAutorizadoColumn.setStyle(conditionStyleAutorizado);
			StyleBuilder conditionStyleCargos= stl.style().conditionalStyles(obtenerSemaforizacion(porcentajeCargosColumn, parametrizacion)).setParentStyle(EstilosReportesDinamicos.estiloPadre);
			cargosColumn.setStyle(conditionStyleCargos);
			porcentajeCargosColumn.setStyle(conditionStyleCargos);
			StyleBuilder conditionStyleFacturado = stl.style().conditionalStyles(obtenerSemaforizacion(porcentajeFacturadoColumn, parametrizacion)).setParentStyle(EstilosReportesDinamicos.estiloPadre);
			facturadoColumn.setStyle(conditionStyleFacturado);
			porcentajeFacturadoColumn.setStyle(conditionStyleFacturado);
			
			//Se definen las columnas por las cuales se va a realizar el agrupamiento
			ColumnGroupBuilder nivelGroup = grp.group(nivelColumn)
													.setStyle(EstilosReportesDinamicos.estiloTituloSombraL)
													.setPadding(0);
			ColumnGroupBuilder grupoClaseGroup = grp.group(grupoClaseColumn).setPadding(0)
													.setStyle(EstilosReportesDinamicos.estiloSubTituloSombraL);
			
			//Instancia de la utilidad que permite obtener el nombre de los totales
			UtilSimpleExpression expression = new UtilSimpleExpression();
			reportFormatoC
	          .setTemplate(disenio.crearPlantillaReporte())  
	          .setPageMargin(disenio.crearMagenesReporte())
	          .pageHeader(disenio.crearComponenteEncabezadoFormatoC(params, parametrizacion))
	          .columns(nivelColumn, grupoClaseColumn, 
	        	productoServicioColumn, presupuestadoColumn, 
	            ordenadoColumn, porcentajeOrdenadoColumn, autorizadoColumn,
	            porcentajeAutorizadoColumn, cargosColumn, porcentajeCargosColumn,
	            facturadoColumn, porcentajeFacturadoColumn)  
	          .groupBy(  
	        		  nivelGroup, grupoClaseGroup)
	          //Se define que se realizara totalización por cada nivel de atención
	          .subtotalsAtGroupFooter(  
	            nivelGroup, 
	            sbt.customValue(expression.getExpressionTotalNivel(), productoServicioColumn)
	            	.setStyle(EstilosReportesDinamicos.estiloSubTituloSombraL),
	            sbt.sum(presupuestadoColumn), 
	            sbt.sum(ordenadoColumn), 
	            sbt.avg(porcentajeOrdenadoColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)),
	            sbt.sum(autorizadoColumn),
	            sbt.avg(porcentajeAutorizadoColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)),
	            sbt.sum(cargosColumn),
	            sbt.avg(porcentajeCargosColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)),
	            sbt.sum(facturadoColumn),
	            sbt.avg(porcentajeFacturadoColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)))
	          //Se define que se realizara totalización por cada grupo de servicios o
	          //clase de inventarión
	          .subtotalsAtGroupFooter(  
	            grupoClaseGroup, 
	            sbt.customValue(expression.getExpressionTotalGrupoClase(), productoServicioColumn)
	            	.setStyle(EstilosReportesDinamicos.estiloSubTituloSombraL),
	            sbt.sum(presupuestadoColumn), 
	            sbt.sum(ordenadoColumn), 
	            sbt.avg(porcentajeOrdenadoColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)),
	            sbt.sum(autorizadoColumn),
	            sbt.avg(porcentajeAutorizadoColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)),
	            sbt.sum(cargosColumn),
	            sbt.avg(porcentajeCargosColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)),
	            sbt.sum(facturadoColumn),
	            sbt.avg(porcentajeFacturadoColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)))
	          //Se define que se realizara totalización por todo el reporte
	          .subtotalsAtSummary(  
	            sbt.customValue(expression.getExpressionTotalContrato(), productoServicioColumn)
	            	.setStyle(EstilosReportesDinamicos.estiloSubTituloSombraL),
	            sbt.sum(presupuestadoColumn), 
	            sbt.sum(ordenadoColumn), 
	            sbt.avg(porcentajeOrdenadoColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)),
	            sbt.sum(autorizadoColumn),
	            sbt.avg(porcentajeAutorizadoColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)),
	            sbt.sum(cargosColumn),
	            sbt.avg(porcentajeCargosColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)),
	            sbt.sum(facturadoColumn),
	            sbt.avg(porcentajeFacturadoColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)))
	          .groupFooter(grupoClaseGroup, cmp.text("").setHeight(10))
	          .groupFooter(nivelGroup, cmp.text("").setHeight(20))
	          .setDetailSplitType(SplitType.PREVENT)
	          .pageFooter(disenio.crearcomponentePiePagina(params))
	          .pageFooter(cmp.pageXofY().setFormatExpression("Pag. {0} de {1}"))
	          .setDataSource(getDataSourceNiveles(nivelesAtencion))
	          .build();  
		}
		catch(Exception e){
			logger.error(e);
			throw e;
		}
		return reportFormatoC;
	}
	
	/**
	 * Método encargado de generar el reporte plano de Ordenes de Capitación
	 * Subcontratada en el formato C
	 * @param conveniosAutorizados
	 * @param conveniosNoAutorizados
	 * @param params
	 * @return JasperReportBuilder el reporte compilado
	 * @throws Exception
	 */
	public JasperReportBuilder  buildReportPlanoFormatoC(ArrayList<DtoNivelReporte> nivelesAtencion, Map<String, String> params) throws Exception{
		//Inicialización del reporte
		JasperReportBuilder reportPlanoFormatoC = report();
		try{
			//Instancia de la clase que genera el diseño del reporte
			GeneradorDisenioReporte disenio = new GeneradorDisenioReporte();
			
			//Se definen las columnas que tendra el Reporte
			TextColumnBuilder<String>     nivelColumn  = col.column(
																	messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnNivelAtencion"),
																	"nombreNivel",  type.stringType());
			TextColumnBuilder<String>     grupoClaseColumn  = col.column(
																	messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnDescripcion"),
																	"nombreGrupoClase",  type.stringType());
			TextColumnBuilder<String>     productoServicioColumn  = col.column(
																	messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnGruposClases"),
																	"nombreProductoServicio",  type.stringType());
			TextColumnBuilder<BigDecimal>     presupuestadoColumn  = col.column(
																		messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVPresupuestado"),
																		"totalPresupuestado",  type.bigDecimalType());
			TextColumnBuilder<BigDecimal>     ordenadoColumn  = col.column(
																		messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVOrdenado"),
																		"totalOrdenado",  type.bigDecimalType());															
			TextColumnBuilder<BigDecimal>     autorizadoColumn  = col.column(
																		messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVAutorizado"),
																		"totalAutorizado",  type.bigDecimalType());
			TextColumnBuilder<BigDecimal>     cargosColumn  = col.column(
																		messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVCargos"),
																		"totalCargosCuenta",  type.bigDecimalType());
			TextColumnBuilder<BigDecimal>     facturadoColumn  = col.column(
																		messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVFacturado"),
																		"totalFacturado",  type.bigDecimalType());		
			TextColumnBuilder<BigDecimal>     porcentajeOrdenadoColumn  = col.column(
																				messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPorcentaje"),
																				"porcentajeOrdenado",  type.bigDecimalType());
			TextColumnBuilder<BigDecimal>     porcentajeAutorizadoColumn  = col.column(
																					messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPorcentaje"),
																					"porcentajeAutorizado",  type.bigDecimalType());
			TextColumnBuilder<BigDecimal>     porcentajeCargosColumn  = col.column(
																				messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPorcentaje"),
																				"porcentajeCargos",  type.bigDecimalType());
			TextColumnBuilder<BigDecimal>     porcentajeFacturadoColumn  = col.column(
																				messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPorcentaje"),
																				"porcentajeFacturado",  type.bigDecimalType());		
	        
			reportPlanoFormatoC
	        	  .title(disenio.crearComponenteEncabezadoPlanoFormatoC(params))
	        	  .columns(nivelColumn, grupoClaseColumn, 
			            productoServicioColumn, presupuestadoColumn, 
			            ordenadoColumn, porcentajeOrdenadoColumn, autorizadoColumn,
			            porcentajeAutorizadoColumn, cargosColumn, porcentajeCargosColumn,
			            facturadoColumn, porcentajeFacturadoColumn)
	        	  .setShowColumnTitle(false)
	        	  .setDetailSplitType(SplitType.PREVENT)
	        	  .setDataSource(getDataSourcePlanoNiveles(nivelesAtencion))
	              .build();
		}
		catch(Exception e){
			logger.error(e);
			throw e;
		}
		return reportPlanoFormatoC;
	}
	
	/**
	 * Método encargado de generar el reporte de  convenios autorizados y NO Autorizados
	 * para el reporte de Ordenes de Capitación
	 * Subcontratada en el formato A
	 * @param conveniosAutorizados
	 * @param conveniosNoAutorizados
	 * @param params
	 * @return JasperReportBuilder el reporte compilado
	 * @throws Exception
	 */
	public JasperReportBuilder buildSubReportAmbos(ArrayList<DtoConvenioReporte> conveniosAutorizados, ArrayList<DtoConvenioReporte> conveniosNoAutorizados) throws Exception{
		//Inicialización del reporte
		JasperReportBuilder reportAutorizados = report();
		try{
			//Instancia de la clase que genera los diseños del reporte
			GeneradorDisenioReporte disenio = new GeneradorDisenioReporte();
				
			//Se definen las columnas que tendra el Reporte
			TextColumnBuilder<String>     tipoConvenioColumn  = col.column(
																	messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnTipoConvenio"),
																	"tipoConvenio",  type.stringType());
			TextColumnBuilder<String>     convenioColumn  = col.column(
																	messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnNombreConvenio"),
																	"nombreConvenio",  type.stringType());
			TextColumnBuilder<String>     contratoColumn  = col.column(
																	messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.contratoPlano"),
																	"numeroContrato",  type.stringType());
			TextColumnBuilder<String>     nivelColumn  = col.column(
																	messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnNivelAtencion"),
																	"nombre",  type.stringType());
			TextColumnBuilder<BigDecimal>     presupuestadoColumn  = col.column(
																		messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVPresupuestado"),
																		"totalPresupuestado",  type.bigDecimalType());
			TextColumnBuilder<BigDecimal>     ordenadoColumn  = col.column(
																		messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVOrdenado"),
																		"totalOrdenado",  type.bigDecimalType());															
			TextColumnBuilder<BigDecimal>     autorizadoColumn  = col.column(
																		messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVAutorizado"),
																		"totalAutorizado",  type.bigDecimalType());
			TextColumnBuilder<BigDecimal>     cargosColumn  = col.column(
																		messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVCargos"),
																		"totalCargosCuenta",  type.bigDecimalType());
			TextColumnBuilder<BigDecimal>     facturadoColumn  = col.column(
																		messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVFacturado"),
																		"totalFacturado",  type.bigDecimalType());		
			TextColumnBuilder<BigDecimal>     porcentajeOrdenadoColumn  = col.column(
																				messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPorcentaje"),
																				"porcentajeOrdenado",  type.bigDecimalType()).setWidth(35);
			TextColumnBuilder<BigDecimal>     porcentajeAutorizadoColumn  = col.column(
																					messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPorcentaje"),
																					"porcentajeAutorizado",  type.bigDecimalType()).setWidth(35);
			TextColumnBuilder<BigDecimal>     porcentajeCargosColumn  = col.column(
																				messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPorcentaje"),
																				"porcentajeCargos",  type.bigDecimalType()).setWidth(35);
			TextColumnBuilder<BigDecimal>     porcentajeFacturadoColumn  = col.column(
																				messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPorcentaje"),
																				"porcentajeFacturado",  type.bigDecimalType()).setWidth(35);
					
			//Se obtiene la paramretrizacion de semaforizacion
			ArrayList<ParametrizacionSemaforizacion> parametrizacion=obtenerParametrizacionSemaforizacion();
			//Se definen los estilos que se aplicaran a las columnas
			//de acuerdo a la semaforización
			StyleBuilder conditionStyleOrdenado = stl.style().conditionalStyles(obtenerSemaforizacion(porcentajeOrdenadoColumn, parametrizacion)).setParentStyle(EstilosReportesDinamicos.estiloPadre);
			ordenadoColumn.setStyle(conditionStyleOrdenado);
			porcentajeOrdenadoColumn.setStyle(conditionStyleOrdenado);
			StyleBuilder conditionStyleAutorizado= stl.style().conditionalStyles(obtenerSemaforizacion(porcentajeAutorizadoColumn, parametrizacion)).setParentStyle(EstilosReportesDinamicos.estiloPadre);
			autorizadoColumn.setStyle(conditionStyleAutorizado);
			porcentajeAutorizadoColumn.setStyle(conditionStyleAutorizado);
			StyleBuilder conditionStyleCargos= stl.style().conditionalStyles(obtenerSemaforizacion(porcentajeCargosColumn, parametrizacion)).setParentStyle(EstilosReportesDinamicos.estiloPadre);
			cargosColumn.setStyle(conditionStyleCargos);
			porcentajeCargosColumn.setStyle(conditionStyleCargos);
			StyleBuilder conditionStyleFacturado = stl.style().conditionalStyles(obtenerSemaforizacion(porcentajeFacturadoColumn, parametrizacion)).setParentStyle(EstilosReportesDinamicos.estiloPadre);
			facturadoColumn.setStyle(conditionStyleFacturado);
			porcentajeFacturadoColumn.setStyle(conditionStyleFacturado);
			
			//Se definen las columnas por la cuales se realizara el agrupamiento
			ColumnGroupBuilder tipoConvenioGroup = grp.group(tipoConvenioColumn)
													.setStyle(EstilosReportesDinamicos.estiloTituloSombraL)
													.setPadding(0);
			ColumnGroupBuilder convenioGroup = grp.group(convenioColumn).setPadding(0);
			ColumnGroupBuilder contratoGroup = grp.group(contratoColumn)
													.setStyle(EstilosReportesDinamicos.estiloSubTituloSombraL)
													.setPadding(0);
			
			//Instancia de la clase que permite obtener el nombre del total
			UtilSimpleExpression expression = new UtilSimpleExpression();
			reportAutorizados
	              .setTemplate(disenio.crearPlantillaReporte()) 
	              //Se se colocan margenes en 0
	              .setPageMargin(disenio.crearMagenesSubReporte())
	              .columns(tipoConvenioColumn,  
	                convenioColumn, contratoColumn, nivelColumn, presupuestadoColumn, 
	                ordenadoColumn, porcentajeOrdenadoColumn, autorizadoColumn,
	                porcentajeAutorizadoColumn, cargosColumn, porcentajeCargosColumn,
	                facturadoColumn, porcentajeFacturadoColumn)  
	              .groupBy(  
	                tipoConvenioGroup,convenioGroup, contratoGroup)
	              //Se define que se realizara totalización por cada convenio
	              .subtotalsAtGroupFooter(  
	                convenioGroup, 
	                sbt.customValue(expression.getExpressionTotalConvenio(), nivelColumn)
	                	.setStyle(EstilosReportesDinamicos.estiloSubTituloSombraL),
	                sbt.sum(presupuestadoColumn), 
	                sbt.sum(ordenadoColumn), 
	                sbt.avg(porcentajeOrdenadoColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)),
	                sbt.sum(autorizadoColumn),
	                sbt.avg(porcentajeAutorizadoColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)),
	                sbt.sum(cargosColumn),
	                sbt.avg(porcentajeCargosColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)),
	                sbt.sum(facturadoColumn),
	                sbt.avg(porcentajeFacturadoColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)))
	              //Se define que se realizara totalización por cada contrato
	              .subtotalsAtGroupFooter(  
	                contratoGroup, 
	                sbt.customValue(expression.getExpressionTotalContrato(), nivelColumn)
	                	.setStyle(EstilosReportesDinamicos.estiloSubTituloSombraL),
	                sbt.sum(presupuestadoColumn), 
	                sbt.sum(ordenadoColumn), 
	                sbt.avg(porcentajeOrdenadoColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)),
	                sbt.sum(autorizadoColumn),
	                sbt.avg(porcentajeAutorizadoColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)),
	                sbt.sum(cargosColumn),
	                sbt.avg(porcentajeCargosColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)),
	                sbt.sum(facturadoColumn),
	                sbt.avg(porcentajeFacturadoColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)))
	              .groupFooter(convenioGroup, cmp.text("").setHeight(10))
	              .summary(cmp.subreport(buildSubReportNoAutorizado(conveniosNoAutorizados)))
	              .setDetailSplitType(SplitType.PREVENT)
	              .lastPageFooter(cmp.text("").setHeight(20))
	              .setDataSource(getDataSourceConveniosAutorizados(conveniosAutorizados))
	              .build();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }
        return reportAutorizados;
	}
	
	
	/**
	 * Método encargado de generar el subreporte de  convenios autorizados
	 * para el reporte de Ordenes de Capitación
	 * Subcontratada en el formato A
	 * @param conveniosAutorizados
	 * @param params
	 * @return JasperReportBuilder el reporte compilado
	 * @throws Exception
	 */
	public JasperReportBuilder buildSubReportAutorizado(ArrayList<DtoConvenioReporte> conveniosAutorizados) throws Exception{
		//Inicialización del reporte
		JasperReportBuilder reportAutorizados = report();
		try{
			//Instancia de la clase que genera los diseños del reporte
			GeneradorDisenioReporte disenio = new GeneradorDisenioReporte();
				
			//Se definen las columnas que tendra el Reporte
			TextColumnBuilder<String>     tipoConvenioColumn  = col.column(
																	messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnTipoConvenio"),
																	"tipoConvenio",  type.stringType());
			TextColumnBuilder<String>     convenioColumn  = col.column(
																	messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnNombreConvenio"),
																	"nombreConvenio",  type.stringType());
			TextColumnBuilder<String>     contratoColumn  = col.column(
																	messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.contratoPlano"),
																	"numeroContrato",  type.stringType());
			TextColumnBuilder<String>     nivelColumn  = col.column(
																	messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnNivelAtencion"),
																	"nombre",  type.stringType());
			TextColumnBuilder<BigDecimal>     presupuestadoColumn  = col.column(
																		messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVPresupuestado"),
																		"totalPresupuestado",  type.bigDecimalType());
			TextColumnBuilder<BigDecimal>     ordenadoColumn  = col.column(
																		messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVOrdenado"),
																		"totalOrdenado",  type.bigDecimalType());															
			TextColumnBuilder<BigDecimal>     autorizadoColumn  = col.column(
																		messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVAutorizado"),
																		"totalAutorizado",  type.bigDecimalType());
			TextColumnBuilder<BigDecimal>     cargosColumn  = col.column(
																		messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVCargos"),
																		"totalCargosCuenta",  type.bigDecimalType());
			TextColumnBuilder<BigDecimal>     facturadoColumn  = col.column(
																		messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVFacturado"),
																		"totalFacturado",  type.bigDecimalType());		
			TextColumnBuilder<BigDecimal>     porcentajeOrdenadoColumn  = col.column(
																				messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPorcentaje"),
																				"porcentajeOrdenado",  type.bigDecimalType()).setWidth(35);
			TextColumnBuilder<BigDecimal>     porcentajeAutorizadoColumn  = col.column(
																					messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPorcentaje"),
																					"porcentajeAutorizado",  type.bigDecimalType()).setWidth(35);
			TextColumnBuilder<BigDecimal>     porcentajeCargosColumn  = col.column(
																				messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPorcentaje"),
																				"porcentajeCargos",  type.bigDecimalType()).setWidth(35);
			TextColumnBuilder<BigDecimal>     porcentajeFacturadoColumn  = col.column(
																				messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPorcentaje"),
																				"porcentajeFacturado",  type.bigDecimalType()).setWidth(35);
					
			//Se obtiene la paramretrizacion de semaforizacion
			ArrayList<ParametrizacionSemaforizacion> parametrizacion=obtenerParametrizacionSemaforizacion();
			//Se definen los estilos que se aplicaran a las columnas
			//de acuerdo a la semaforización
			StyleBuilder conditionStyleOrdenado = stl.style().conditionalStyles(obtenerSemaforizacion(porcentajeOrdenadoColumn, parametrizacion)).setParentStyle(EstilosReportesDinamicos.estiloPadre);
			ordenadoColumn.setStyle(conditionStyleOrdenado);
			porcentajeOrdenadoColumn.setStyle(conditionStyleOrdenado);
			StyleBuilder conditionStyleAutorizado= stl.style().conditionalStyles(obtenerSemaforizacion(porcentajeAutorizadoColumn, parametrizacion)).setParentStyle(EstilosReportesDinamicos.estiloPadre);
			autorizadoColumn.setStyle(conditionStyleAutorizado);
			porcentajeAutorizadoColumn.setStyle(conditionStyleAutorizado);
			StyleBuilder conditionStyleCargos= stl.style().conditionalStyles(obtenerSemaforizacion(porcentajeCargosColumn, parametrizacion)).setParentStyle(EstilosReportesDinamicos.estiloPadre);
			cargosColumn.setStyle(conditionStyleCargos);
			porcentajeCargosColumn.setStyle(conditionStyleCargos);
			StyleBuilder conditionStyleFacturado = stl.style().conditionalStyles(obtenerSemaforizacion(porcentajeFacturadoColumn, parametrizacion)).setParentStyle(EstilosReportesDinamicos.estiloPadre);
			facturadoColumn.setStyle(conditionStyleFacturado);
			porcentajeFacturadoColumn.setStyle(conditionStyleFacturado);
			
			//Se definen las columnas por la cuales se realizara el agrupamiento
			ColumnGroupBuilder tipoConvenioGroup = grp.group(tipoConvenioColumn)
													.setStyle(EstilosReportesDinamicos.estiloTituloSombraL)
													.setPadding(0);
			ColumnGroupBuilder convenioGroup = grp.group(convenioColumn).setPadding(0);
			ColumnGroupBuilder contratoGroup = grp.group(contratoColumn)
													.setStyle(EstilosReportesDinamicos.estiloSubTituloSombraL)
													.setPadding(0);
			
			//Instancia de la clase que permite obtener el nombre del total
			UtilSimpleExpression expression = new UtilSimpleExpression();
			reportAutorizados
	              .setTemplate(disenio.crearPlantillaReporte()) 
	              //Se se colocan margenes en 0
	              .setPageMargin(disenio.crearMagenesSubReporte())
	              .columns(tipoConvenioColumn,  
	                convenioColumn, contratoColumn, nivelColumn, presupuestadoColumn, 
	                ordenadoColumn, porcentajeOrdenadoColumn, autorizadoColumn,
	                porcentajeAutorizadoColumn, cargosColumn, porcentajeCargosColumn,
	                facturadoColumn, porcentajeFacturadoColumn)  
	              .groupBy(  
	                tipoConvenioGroup,convenioGroup, contratoGroup)
	              //Se define que se realizara totalización por cada convenio
	              .subtotalsAtGroupFooter(  
	                convenioGroup, 
	                sbt.customValue(expression.getExpressionTotalConvenio(), nivelColumn)
	                	.setStyle(EstilosReportesDinamicos.estiloSubTituloSombraL),
	                sbt.sum(presupuestadoColumn), 
	                sbt.sum(ordenadoColumn), 
	                sbt.avg(porcentajeOrdenadoColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)),
	                sbt.sum(autorizadoColumn),
	                sbt.avg(porcentajeAutorizadoColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)),
	                sbt.sum(cargosColumn),
	                sbt.avg(porcentajeCargosColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)),
	                sbt.sum(facturadoColumn),
	                sbt.avg(porcentajeFacturadoColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)))
	              //Se define que se realizara totalización por cada contrato
	              .subtotalsAtGroupFooter(  
	                contratoGroup, 
	                sbt.customValue(expression.getExpressionTotalContrato(), nivelColumn)
	                	.setStyle(EstilosReportesDinamicos.estiloSubTituloSombraL),
	                sbt.sum(presupuestadoColumn), 
	                sbt.sum(ordenadoColumn), 
	                sbt.avg(porcentajeOrdenadoColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)),
	                sbt.sum(autorizadoColumn),
	                sbt.avg(porcentajeAutorizadoColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)),
	                sbt.sum(cargosColumn),
	                sbt.avg(porcentajeCargosColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)),
	                sbt.sum(facturadoColumn),
	                sbt.avg(porcentajeFacturadoColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)))
	              .groupFooter(convenioGroup, cmp.text("").setHeight(10))
	              .setDetailSplitType(SplitType.PREVENT)
	              .lastPageFooter(cmp.text("").setHeight(20))
	              .setDataSource(getDataSourceConveniosAutorizados(conveniosAutorizados))
	              .build();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }
        return reportAutorizados;
	}

	/**
	 * Método encargado de generar el subreporte de  convenios NO autorizados
	 * para el reporte de Ordenes de Capitación
	 * Subcontratada en el formato A
	 * @param conveniosNoAutorizados
	 * @param params
	 * @return JasperReportBuilder el reporte compilado
	 * @throws Exception
	 */
	public JasperReportBuilder buildSubReportNoAutorizado(ArrayList<DtoConvenioReporte> conveniosNoAurotizados) throws Exception{
		//Inicialización del reporte
		JasperReportBuilder reportNoAutorizados = report();
		try{
			//Instancia de la clase que genera los diseños del reporte
			GeneradorDisenioReporte disenio = new GeneradorDisenioReporte();
				
			//Se definen las columnas que tendra el Reporte
			TextColumnBuilder<String>     tipoConvenioColumn  = col.column(
																	messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnTipoConvenio"),
																	"tipoConvenio",  type.stringType());
			TextColumnBuilder<String>     convenioColumn  = col.column(
																	messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnNombreConvenio"),
																	"nombreConvenio",  type.stringType());
			TextColumnBuilder<String>     contratoColumn  = col.column(
																	messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.contratoPlano"),
																	"numeroContrato",  type.stringType());
			TextColumnBuilder<String>     nivelColumn  = col.column(
																	messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnNivelAtencion"),
																	"nombre",  type.stringType());
			TextColumnBuilder<BigDecimal>     presupuestadoColumn  = col.column(
																		messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVPresupuestado"),
																		"totalPresupuestado",  type.bigDecimalType());
			TextColumnBuilder<BigDecimal>     ordenadoColumn  = col.column(
																		messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVOrdenado"),
																		"totalOrdenado",  type.bigDecimalType());															
			TextColumnBuilder<BigDecimal>     cargosColumn  = col.column(
																		messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVCargos"),
																		"totalCargosCuenta",  type.bigDecimalType());
			TextColumnBuilder<BigDecimal>     facturadoColumn  = col.column(
																		messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnVFacturado"),
																		"totalFacturado",  type.bigDecimalType());		
			TextColumnBuilder<BigDecimal>     porcentajeOrdenadoColumn  = col.column(
																				messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPorcentaje"),
																				"porcentajeOrdenado",  type.bigDecimalType()).setWidth(35);
			TextColumnBuilder<BigDecimal>     porcentajeCargosColumn  = col.column(
																				messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPorcentaje"),
																				"porcentajeCargos",  type.bigDecimalType()).setWidth(35);
			TextColumnBuilder<BigDecimal>     porcentajeFacturadoColumn  = col.column(
																				messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.columnPorcentaje"),
																				"porcentajeFacturado",  type.bigDecimalType()).setWidth(35);
			//Se obtiene la paramretrizacion de semaforizacion
			ArrayList<ParametrizacionSemaforizacion> parametrizacion=obtenerParametrizacionSemaforizacion();
			//Se definen los estilos a aplicar a las columnas de acuerdo
			//a la semaforización
			StyleBuilder conditionStyleOrdenado = stl.style().conditionalStyles(obtenerSemaforizacion(porcentajeOrdenadoColumn, parametrizacion)).setParentStyle(EstilosReportesDinamicos.estiloPadre);
			ordenadoColumn.setStyle(conditionStyleOrdenado);
			porcentajeOrdenadoColumn.setStyle(conditionStyleOrdenado);
			StyleBuilder conditionStyleCargos= stl.style().conditionalStyles(obtenerSemaforizacion(porcentajeCargosColumn, parametrizacion)).setParentStyle(EstilosReportesDinamicos.estiloPadre);
			cargosColumn.setStyle(conditionStyleCargos);
			porcentajeCargosColumn.setStyle(conditionStyleCargos);
			StyleBuilder conditionStyleFacturado = stl.style().conditionalStyles(obtenerSemaforizacion(porcentajeFacturadoColumn, parametrizacion)).setParentStyle(EstilosReportesDinamicos.estiloPadre);
			facturadoColumn.setStyle(conditionStyleFacturado);
			porcentajeFacturadoColumn.setStyle(conditionStyleFacturado);
	
			//Se definen los grupos por los cuales se realiza el agrupamiento
			ColumnGroupBuilder tipoConvenioGroup = grp.group(tipoConvenioColumn)
														.setStyle(EstilosReportesDinamicos.estiloTituloSombraL)
														.setPadding(0);
			ColumnGroupBuilder convenioGroup = grp.group(convenioColumn).setPadding(0);
			ColumnGroupBuilder contratoGroup = grp.group(contratoColumn)
													.setStyle(EstilosReportesDinamicos.estiloSubTituloSombraL)
													.setPadding(0);
			//Instancia de la clase que permite obtener el label del total
			UtilSimpleExpression expression = new UtilSimpleExpression();						
	        reportNoAutorizados
	              .setTemplate(disenio.crearPlantillaReporte())
	              //Se se colocan margenes en 0
	              .setPageMargin(disenio.crearMagenesSubReporte())
	              .columns(  
	            		tipoConvenioColumn, convenioColumn, contratoColumn, nivelColumn, presupuestadoColumn, 
	            		ordenadoColumn, porcentajeOrdenadoColumn, cargosColumn, 
	            		porcentajeCargosColumn, facturadoColumn, porcentajeFacturadoColumn)  
	              .groupBy(  
	                tipoConvenioGroup,convenioGroup, contratoGroup)
	              //Se define que se totalizan los resultados por casa convenio
	              .subtotalsAtGroupFooter(  
	                convenioGroup,
	                sbt.customValue(expression.getExpressionTotalConvenio(), nivelColumn)
	                	.setStyle(EstilosReportesDinamicos.estiloSubTituloSombraL),
	                sbt.sum(presupuestadoColumn), 
	                sbt.sum(ordenadoColumn),
	                sbt.avg(porcentajeOrdenadoColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)),
	                sbt.sum(cargosColumn), 
	                sbt.avg(porcentajeCargosColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)),
	                sbt.sum(facturadoColumn),
	                sbt.avg(porcentajeFacturadoColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)))
	              //Se define que se totalizan los resultados por cada contrato
	              .subtotalsAtGroupFooter(  
	                contratoGroup, 
	                sbt.customValue(expression.getExpressionTotalContrato(), nivelColumn)
	                	.setStyle(EstilosReportesDinamicos.estiloSubTituloSombraL),
	                sbt.sum(presupuestadoColumn), 
	                sbt.sum(ordenadoColumn),
	                sbt.avg(porcentajeOrdenadoColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)),
	                sbt.sum(cargosColumn), 
	                sbt.avg(porcentajeCargosColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)),
	                sbt.sum(facturadoColumn),
	                sbt.avg(porcentajeFacturadoColumn).setStyle(stl.style(EstilosReportesDinamicos.estiloSubTituloInvisible)))
	              .groupFooter(convenioGroup, cmp.text("").setHeight(10))
	              .setDetailSplitType(SplitType.PREVENT)
	              .setDataSource(getDataSourceConveniosNoAutorizados(conveniosNoAurotizados))
	              .build();
              
        } catch (Exception e) {  
            e.printStackTrace();  
        }
        return reportNoAutorizados;
	}
	
	/**
	 * Método encargado de generar los diferentes estilos
	 * a aplicar a una columna de acuerdo a la semaforización
	 * 
	 * @param colum
	 * @return ConditionalStyleBuilder[] el array con los estilos
	 * que se obtienen en las parametricas
	 */
	private ConditionalStyleBuilder[] obtenerSemaforizacion(TextColumnBuilder<BigDecimal> column,
										ArrayList<ParametrizacionSemaforizacion> parametrizacionSemaforizacion){
		ConditionalStyleBuilder[] condiciones = new ConditionalStyleBuilder[parametrizacionSemaforizacion.size()+1];
		int i=0;
		if(!parametrizacionSemaforizacion.isEmpty()){
			for(ParametrizacionSemaforizacion parametrizacion: parametrizacionSemaforizacion){
				StringBuilder buildColor = new StringBuilder(parametrizacion.getColor());
				buildColor.replace(0, 1, "0x");
				Color color= Color.decode(buildColor.toString());
				ConditionalStyleBuilder condition = stl.conditionalStyle(
															cnd.between(column, parametrizacion.getRangoInicial(),parametrizacion.getRangoFinal()))
														.setBackgroundColor(color)
														.setFontSize(7).setHorizontalAlignment(HorizontalAlignment.RIGHT)
														.setVerticalAlignment(VerticalAlignment.MIDDLE)
														.setBorder(stl.pen1Point().setLineColor(Color.WHITE))
														.setForegroudColor(new Color(0,0,0));
				condiciones[i]=condition;
				i++;
			}
		}
		//Se crea un estylo generico para los valores que no cumplan ninguna de 
		//las condiciones esablecidas en la semaforización
		ConditionalStyleBuilder conditionGeneral = stl.conditionalStyle(cnd.between(column, 0, 9999))
														.setBackgroundColor(new Color(255, 255, 255))
														.setFontSize(7).setHorizontalAlignment(HorizontalAlignment.RIGHT)
														.setVerticalAlignment(VerticalAlignment.MIDDLE)
														.setBorder(stl.pen1Point().setLineColor(Color.WHITE))														.setForegroudColor(new Color(0,0,0));
		condiciones[i]=conditionGeneral;
		return condiciones;
	}
	
	/**
	 * Método encargado de consultar la parametrizacion
	 * de semaforización
	 * 
	 * @return ArrayList<ParametrizacionSemaforizacion> la lista de parametrizaciones
	 */
	private ArrayList<ParametrizacionSemaforizacion> obtenerParametrizacionSemaforizacion(){
		UtilidadTransaccion.getTransaccion().begin();
		IParametrizacionSemaforizacionMundo semaforizacion = AdministracionFabricaMundo.crearParametrizacionMundo();
		ArrayList<ParametrizacionSemaforizacion> parametrizacionSemaforizacion=semaforizacion.consultarParametrizaciones(ConstantesIntegridadDominio.acronimoTipoReportePresupuesto);
		UtilidadTransaccion.getTransaccion().commit();
		return parametrizacionSemaforizacion;
	}
	
	
	/**
	 * Método que obtiene el data source de los convenios autorizados
	 * @param conveniosAutorizados
	 * @return
	 */
	private JRDataSource getDataSourceConveniosAutorizados(ArrayList<DtoConvenioReporte> conveniosAutorizados) {
		ConveniosDataSource dataSource = new ConveniosDataSource(conveniosAutorizados);
		return dataSource.crearDataSourceAutorizado();
	}
	
	/**
	 * Método que obtiene el data source de los convenios No autorizados
	 * @param conveniosNoAutorizados
	 * @return JRDataSource
	 */ 
	private JRDataSource getDataSourceConveniosNoAutorizados(ArrayList<DtoConvenioReporte> conveniosNoAutorizados) {
		ConveniosDataSource dataSource = new ConveniosDataSource(conveniosNoAutorizados);
		return dataSource.crearDataSourceNoAutorizado();
	}
	
	/**
	 * Método que obtiene el data source de los contratos
	 * @param contratos
	 * @return JRDataSource
	 */
	private JRDataSource getDataSourceContratos(ArrayList<DtoContratoReporte> contratos) {
		ContratosDataSource dataSource = new ContratosDataSource(contratos);
		return dataSource.crearDataSource();
	}
	
	/**
	 * Método que obtiene el data source de los niveles de atención
	 * @param nivelesAtencion
	 * @return JRDataSource
	 */
	private JRDataSource getDataSourceNiveles(ArrayList<DtoNivelReporte> nivelesAtencion) {
		NivelesDataSource dataSource = new NivelesDataSource(nivelesAtencion);
		return dataSource.crearDataSource();
	}
	
	
	/**
	 * Método que obtiene el data source de los convenios para generar el reporte plano
	 * @param conveniosAutorizados
	 * @param conveniosNoAutorizados
	 * @return JRDataSource
	 */
	private JRDataSource getDataSourcePlanoConvenios(ArrayList<DtoConvenioReporte> conveniosAutorizados,
															ArrayList<DtoConvenioReporte> conveniosNoAutorizados) {
		ConveniosDataSource dataSource = new ConveniosDataSource(conveniosAutorizados, conveniosNoAutorizados);
		return dataSource.crearDataSourcePlano();
	}
	
	/**
	 * Método que obtiene el data source de los contratos para el formato plano
	 * @param contratos
	 * @return JRDataSource
	 */
	private JRDataSource getDataSourcePlanoContratos(ArrayList<DtoContratoReporte> contratos) {
		ContratosDataSource dataSource = new ContratosDataSource(contratos);
		return dataSource.crearDataSourcePlano();
	}
	
	/**
	 * Método que obtiene el data source de los niveles de atención
	 * para el formato plano
	 * @param nivelesAtencion
	 * @return JRDataSource
	 */
	private JRDataSource getDataSourcePlanoNiveles(ArrayList<DtoNivelReporte> nivelesAtencion) {
		NivelesDataSource dataSource = new NivelesDataSource(nivelesAtencion);
		return dataSource.crearDataSource();
	}
	
}
