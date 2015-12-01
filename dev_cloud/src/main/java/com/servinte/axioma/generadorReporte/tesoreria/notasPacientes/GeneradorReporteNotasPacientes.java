package com.servinte.axioma.generadorReporte.tesoreria.notasPacientes;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.sbt;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.LineStyle;
import net.sf.dynamicreports.report.constant.SplitType;
import net.sf.dynamicreports.report.constant.StretchType;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.definition.expression.DRISimpleExpression;
import net.sf.jasperreports.engine.JRDataSource;

import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesIntegridadDominio;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.reportes.dinamico.EstilosReportesDinamicos;

import com.servinte.axioma.dto.tesoreria.DTONotaPaciente;
import com.servinte.axioma.dto.tesoreria.DtoInfoIngresoPacienteControlarAbonoPacientes;
import com.servinte.axioma.dto.tesoreria.DtoNotasPorNaturaleza;
import com.servinte.axioma.dto.tesoreria.DtoResumenNotasPaciente;
import com.servinte.axioma.generadorReporte.comun.IConstantesReporte;


public class GeneradorReporteNotasPacientes {

	
	/** * Contiene los mensajes correspondiente a este Generador*/
	MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.tesoreria.NotasPacientesForm");
    
	public GeneradorReporteNotasPacientes() {}
	
	/**
	 * Método encargado de generar el reporte Consolidado de Notas Pacientes
	 * Este método itera el mapa que contiene los resultados de la busqueda de 
	 * notas paciente, organizandolos por Institución (clave) y el valor es
	 * un DtoNotasPorNaturaleza el cual contiene dos ArrayList<DtoResumenNotasPaciente> 
	 * uno con las Notas Débito y otro las Crédito 
	 * @param mapaNotasPacientes
	 * @param params
	 * @param paramsValidacion
	 * @return JasperReportBuilder el reporte compilado
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public JasperReportBuilder  buildReporteConsolidadoNotasPacientes(LinkedHashMap<String, DtoNotasPorNaturaleza> mapaNotasPacientes, 
			Map<String, String> params, Map<String, Boolean> paramsValidacion) throws Exception{
		//Inicialización del reporte
		JasperReportBuilder reportFormatoA = report();
		try{
			//Instancia de la clase que genera el diseño del reporte
			GeneradorDisenioReporte disenio = new GeneradorDisenioReporte();
			
			//Se define el contenido del reporte
			reportFormatoA
	          .setTemplate(disenio.crearPlantillaReporteConsolidado())  
	          .setPageMargin(disenio.crearMagenesReporte())
	          .pageHeader(disenio.crearComponenteEncabezadoConsolidadoNotasPacientesFormatoAyB(params));
			  
			//Si el reporte es por paciente se cargan los datos iniciales con la información
			//del paciente
			if (!paramsValidacion.get(IConstantesReporte.reporteRango) && paramsValidacion.get(IConstantesReporte.manejoEspecialInstOdonto)) {
				reportFormatoA.summary(cmp.subreport(this.buildDatosPaciente(params)));
			}
			
			//Se itera el mapa por institución para crear los subreportes correspondientes
			for (Iterator iterator = mapaNotasPacientes.keySet().iterator(); iterator.hasNext();) {
				String key = (String) iterator.next();
				DtoNotasPorNaturaleza dtoNotasPaciente = (DtoNotasPorNaturaleza) mapaNotasPacientes.get(key);
				reportFormatoA.summary(cmp.subreport(buildReportPorInstitucion(
						dtoNotasPaciente.getDtoResumenNotasDebito(), 
						dtoNotasPaciente.getDtoResumenNotasCredito(), 
						key, paramsValidacion)));
				reportFormatoA.setSummaryWithPageHeaderAndFooter(true);
			}
			//Se agrega la sección del pie de página al reporte
			reportFormatoA
			  .pageFooter(disenio.crearComponentePiePagina(params))
	          .pageFooter(cmp.pageXofY().setFormatExpression("Pag. {0} de {1}").setStyle(EstilosReportesDinamicos.estiloLetraFooter))
			  .build();
			
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw e;
		}
		return reportFormatoA;
	}
	
	
	/**
	 * Método encargado de generar el reporte Consolidado de Notas Paciente 
	 * Este método se encarga crear el subreporte que agrupa las notas paciente
	 * por naturaleza debito y crédito
	 * @param listaDtoNotasNaturalezaDebito
	 * @param listaDtoNotasNaturalezaCredito
	 * @param institucion
	 * @param paramsValidacion
	 * @return JasperReportBuilder el reporte compilado
	 * @throws Exception
	 */
	public JasperReportBuilder buildReportPorInstitucion(ArrayList<DtoResumenNotasPaciente> listaDtoNotasNaturalezaDebito,
			ArrayList<DtoResumenNotasPaciente> listaDtoNotasNaturalezaCredito, String institucion, 
			Map<String, Boolean> paramsValidacion) throws Exception{
		//Inicialización del reporte
		JasperReportBuilder reportInstitucion = report();
		try{
			//Instancia de la clase que genera el diseño del reporte
			GeneradorDisenioReporte disenio = new GeneradorDisenioReporte();
			reportInstitucion
	          .setTemplate(disenio.crearPlantillaReporteConsolidado())  
	          .setPageMargin(disenio.crearMagenesSubReporteInstitucion())
	          .title(cmp.text(institucion).setStyle(EstilosReportesDinamicos.estiloTituloSombreadoSuaveLBorder));
			reportInstitucion.summary(cmp.subreport(buildSubReporteNaturalezaNotas(listaDtoNotasNaturalezaDebito, ConstantesIntegridadDominio.acronimoDebito, paramsValidacion)));
			reportInstitucion.summary(cmp.subreport(buildSubReporteNaturalezaNotas(listaDtoNotasNaturalezaCredito, ConstantesIntegridadDominio.acronimoCredito, paramsValidacion)));
			
			reportInstitucion.build();
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw e;
		}
		return reportInstitucion;
	}
	
	/**
	 * Método encargado de generar el subreporte Consolidado de Notas Paciente
	 * Este método es el encargado de cargar la información que se presentara 
	 * en el reporte obteniendola da la lista de DtoResumenNotasPaciente que
	 * recibe como parámetro
	 * @param listaDtoResumenNotasPaciente
	 * @param naturaleza
	 * @param paramsValidacion
	 * @return JasperReportBuilder el reporte compilado
	 * @throws Exception
	 */
	private JasperReportBuilder buildSubReporteNaturalezaNotas(ArrayList<DtoResumenNotasPaciente> listaDtoResumenNotasPaciente, 
			String naturaleza, Map<String, Boolean> paramsValidacion) throws Exception{
		
		JasperReportBuilder subReporteNaturaleza = report();
		try{
			String naturalezaNota = "";
			UtilExpression expresion = new UtilExpression();
			DRISimpleExpression<String> simpleExpression = null;
			DRISimpleExpression<String> simpleExpressionVacia = 
				expresion.obtenerUtilExpresionVacia();
			
			if (naturaleza.equals(ConstantesIntegridadDominio.acronimoDebito)) {
				naturalezaNota = 
					(String)ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoDebito);
				simpleExpression = expresion.obtenerUtilExpresionNotaDebito();
			} else {
				naturalezaNota = 
					(String)ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoCredito);
				simpleExpression = expresion.obtenerUtilExpresionNotaCredito();
			}
			 
			//Instancia de la clase que genera el diseño del reporte
			GeneradorDisenioReporte disenio = new GeneradorDisenioReporte();
			
			//Se definen las elementos que tendra el Reporte
			subReporteNaturaleza
	          .setTemplate(disenio.crearPlantillaReporteConsolidado())  
	          .setPageMargin(disenio.crearMagenesSubReporte())
	          .title(cmp.text(IConstantesReporte.notasNaturaleza + " " + naturalezaNota)
	        		  .setStyle(EstilosReportesDinamicos.estiloTituloSombreadoSuave2LBorder));
			
			TextColumnBuilder<String>     nroNotaColumn  = col.column(
					messageResource.getMessage("notasPaciente.labelNroNota"),
					"nroNota",  type.stringType());
			
			subReporteNaturaleza.addColumn(nroNotaColumn.setStyle(stl.style().setFontSize(7)
							.setVerticalAlignment(VerticalAlignment.MIDDLE).setPadding(2)
							.setBorder(stl.pen1Point().setLineColor(Color.BLACK)))
							.setHorizontalAlignment(HorizontalAlignment.CENTER));
			subReporteNaturaleza.addSubtotalAtColumnFooter(sbt.customValue(simpleExpressionVacia, nroNotaColumn)
					.setStyle(stl.style().bold().setPadding(2).setFontSize(7)
							.setHorizontalAlignment(HorizontalAlignment.RIGHT)
						 	.setBackgroundColor(new Color(230, 230, 230))
						 	.setLeftBorder(stl.pen1Point().setLineColor(Color.BLACK))
						 	.setTopBorder(stl.pen1Point().setLineColor(Color.BLACK))
						 	.setBottomBorder(stl.pen1Point().setLineColor(Color.BLACK))));
			
			TextColumnBuilder<String>     fechaHoraColumn  = col.column(
					messageResource.getMessage("notasPaciente.labelFechaHora"),
					"fechaHora",  type.stringType());
			
			subReporteNaturaleza.addColumn(fechaHoraColumn.setHorizontalAlignment(HorizontalAlignment.LEFT));
			subReporteNaturaleza.addSubtotalAtColumnFooter(
					sbt.customValue(simpleExpressionVacia, fechaHoraColumn).setStyle(EstilosReportesDinamicos.estiloSubTotalSombreadoSuaveSinLateralIzq));
			
			TextColumnBuilder<String>     centroAtencionColumn  = col.column(
					messageResource.getMessage("notasPaciente.labelCentroAtencionOrigen"),
					"centroAtenciOnrigen",  type.stringType());
			
			subReporteNaturaleza.addColumn(centroAtencionColumn.setHorizontalAlignment(HorizontalAlignment.LEFT));
			subReporteNaturaleza.addSubtotalAtColumnFooter(
					sbt.customValue(simpleExpressionVacia, centroAtencionColumn).setStyle(EstilosReportesDinamicos.estiloSubTotalSombreadoSuaveSinLateralIzq));
			
			if (paramsValidacion.get(IConstantesReporte.reporteRango)) {
				TextColumnBuilder<String>     pacienteColumn  = col.column(
						messageResource.getMessage("notasPaciente.labelPaciente"),
						"paciente",  type.stringType());
				
				TextColumnBuilder<String>     idPacienteColumn  = col.column(
						messageResource.getMessage("notasPaciente.labelIdPaciente"),
						"idPaciente",  type.stringType());
				
				subReporteNaturaleza.addColumn(pacienteColumn.setHorizontalAlignment(HorizontalAlignment.LEFT), 
						idPacienteColumn.setHorizontalAlignment(HorizontalAlignment.LEFT));
				subReporteNaturaleza.addSubtotalAtColumnFooter(
						sbt.customValue(simpleExpressionVacia, pacienteColumn).setStyle(EstilosReportesDinamicos.estiloSubTotalSombreadoSuaveSinLateralIzq),
						sbt.customValue(simpleExpressionVacia, idPacienteColumn).setStyle(EstilosReportesDinamicos.estiloSubTotalSombreadoSuaveSinLateralIzq));
			}
			
			if (paramsValidacion.get(IConstantesReporte.manejoEspecialInstOdonto) && 
					paramsValidacion.get(IConstantesReporte.reporteRango)) {
				TextColumnBuilder<String>     centroAtencionDuenioColumn  = col.column(
						messageResource.getMessage("notasPaciente.labelCentroAtencionDueno"),
						"centroAtencionDuenio",  type.stringType());
				subReporteNaturaleza.addColumn(centroAtencionDuenioColumn.setHorizontalAlignment(HorizontalAlignment.LEFT));
				subReporteNaturaleza.addSubtotalAtColumnFooter(
						sbt.customValue(simpleExpressionVacia, centroAtencionDuenioColumn).setStyle(EstilosReportesDinamicos.estiloSubTotalSombreadoSuaveSinLateralIzq));
			}
			
			if (paramsValidacion.get(IConstantesReporte.controlAbonoPacientePorIngreso)) {
				TextColumnBuilder<String>     ingresosColumn  = col.column(
						messageResource.getMessage("notasPaciente.labelIngreso"),
						"ingreso",  type.stringType());
				subReporteNaturaleza.addColumn(ingresosColumn.setHorizontalAlignment(HorizontalAlignment.LEFT));
				subReporteNaturaleza.addSubtotalAtColumnFooter(
						sbt.customValue(simpleExpressionVacia, ingresosColumn).setStyle(EstilosReportesDinamicos.estiloSubTotalSombreadoSuaveSinLateralIzq));
			}

			TextColumnBuilder<String>     conceptoColumn  = col.column(
					messageResource.getMessage("notasPaciente.labelConcepto"),
					"concepto",  type.stringType());

			subReporteNaturaleza.addColumn(conceptoColumn.setHorizontalAlignment(HorizontalAlignment.LEFT));
			subReporteNaturaleza.addSubtotalAtColumnFooter(
					sbt.customValue(simpleExpression, conceptoColumn).setStyle(stl.style().bold().setPadding(2).setFontSize(7)
							.setHorizontalAlignment(HorizontalAlignment.RIGHT)
							.setBackgroundColor(new Color(230, 230, 230))
							.setTopBorder(stl.pen1Point().setLineColor(Color.BLACK))
							.setBottomBorder(stl.pen1Point().setLineColor(Color.BLACK))));

			TextColumnBuilder<BigDecimal>     valorNotaColumn  = col.column(
					messageResource.getMessage("notasPaciente.labelValorNota"),
					"valorNota",  type.bigDecimalType()).setHorizontalAlignment(HorizontalAlignment.RIGHT);

			subReporteNaturaleza.addColumn(valorNotaColumn);
			subReporteNaturaleza.addSubtotalAtColumnFooter(sbt.sum(valorNotaColumn));
			subReporteNaturaleza.setDataSource(getDataSourceNotasPacientes(listaDtoResumenNotasPaciente))
					.setTitleSplitType(SplitType.PREVENT)
					.build();  
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw e;
		}
		return subReporteNaturaleza;
	}
	
	/**
	 * Método encargado de generar el subreporte Consolidado de Notas Paciente en formato plano
	 * Este método es el encargado de cargar la información que se presentara 
	 * en el reporte obteniendola da la lista de DtoResumenNotasPaciente que
	 * recibe como parámetro
	 * @param listaDtoResumenNotasPaciente
	 * @param params
	 * @param paramsValidacion
	 * @return JasperReportBuilder el reporte compilado
	 * @throws Exception
	 */
	public JasperReportBuilder  buildReporteConsolidadoPlano(ArrayList<DtoResumenNotasPaciente> listaDtoResumenNotasPaciente,
			Map<String, String> params, Map<String, Boolean> paramsValidacion) throws Exception{
		

		final String espacio=" ";
		
		final String espacioLargo="          ";
		
		//Inicialización del reporte
		JasperReportBuilder subReporteConsolidadoPlano = report();
		
		HorizontalListBuilder listaTitulos = cmp.horizontalList(
				cmp.text(messageResource.getMessage("notasPaciente.labelInstitucion")));
		HorizontalListBuilder listaTitulo = cmp.horizontalList(
				cmp.text(messageResource.getMessage("notasPaciente.reporte.tituloReportePlano.reporteNotasPaciente")));
		HorizontalListBuilder listaDatosPaciente = cmp.horizontalList();
		
		try{
			//Si el reporte es por paciente se define el encabezado que debe llevar con
			//la información del mismo
			if (!paramsValidacion.get(IConstantesReporte.reporteRango)) {
				String linea1		= messageResource.getMessage("notasPaciente.labelPaciente") + ":" + espacio + 
					params.get(IConstantesReporte.nombrepaciente);
				String linea2		= messageResource.getMessage("notasPaciente.labelIdPaciente") + ":" + espacio +
					params.get(IConstantesReporte.tipoNumeroID);
				String linea3		= messageResource.getMessage("notasPaciente.labelCentroAtencionDueno") + ":" + espacio +
					params.get(IConstantesReporte.centroAtencionDuenio);
				listaDatosPaciente.add(cmp.text(linea1 + espacioLargo + linea2 + espacioLargo + linea3));
			}
			
			//Se definen las columnas que llevata el reporte
			TextColumnBuilder<String>     institucionColumn  = col.column(
					messageResource.getMessage("notasPaciente.labelInstitucion"),
					"institucion",  type.stringType());
			subReporteConsolidadoPlano.addColumn(institucionColumn.setHorizontalAlignment(HorizontalAlignment.LEFT));
			
			TextColumnBuilder<String>     naturalezaColumn  = col.column(
					messageResource.getMessage("notasPaciente.labelNaturaleza"),
					"naturaleza",  type.stringType());
			listaTitulos.add(cmp.text(messageResource.getMessage("notasPaciente.labelNaturaleza")));
			subReporteConsolidadoPlano.addColumn(naturalezaColumn.setHorizontalAlignment(HorizontalAlignment.LEFT));
			
			TextColumnBuilder<String>     nroNotaColumn  = col.column(
					messageResource.getMessage("notasPaciente.labelNroNota"),
					"nroNota",  type.stringType());
			listaTitulos.add(cmp.text(messageResource.getMessage("notasPaciente.labelNroNota")));
			subReporteConsolidadoPlano.addColumn(nroNotaColumn.setHorizontalAlignment(HorizontalAlignment.LEFT));
			
			TextColumnBuilder<String>     fechaHoraColumn  = col.column(
					messageResource.getMessage("notasPaciente.labelFechaHora"),
					"fechaHora",  type.stringType());
			listaTitulos.add(cmp.text(messageResource.getMessage("notasPaciente.labelFechaHora")));
			subReporteConsolidadoPlano.addColumn(fechaHoraColumn.setHorizontalAlignment(HorizontalAlignment.LEFT));
			
			TextColumnBuilder<String>     centroAtencionColumn  = col.column(
					messageResource.getMessage("notasPaciente.labelCentroAtencionOrigen"),
					"centroAtenciOnrigen",  type.stringType());
			listaTitulos.add(cmp.text(messageResource.getMessage("notasPaciente.labelCentroAtencionOrigen")));
			subReporteConsolidadoPlano.addColumn(centroAtencionColumn.setHorizontalAlignment(HorizontalAlignment.LEFT));
			
			if (paramsValidacion.get(IConstantesReporte.reporteRango)) {
				TextColumnBuilder<String>     pacienteColumn  = col.column(
						messageResource.getMessage("notasPaciente.labelPaciente"),
						"paciente",  type.stringType());
				listaTitulos.add(cmp.text(messageResource.getMessage("notasPaciente.labelPaciente")));
				
				TextColumnBuilder<String>     idPacienteColumn  = col.column(
						messageResource.getMessage("notasPaciente.labelIdPaciente"),
						"idPaciente",  type.stringType());
				listaTitulos.add(cmp.text(messageResource.getMessage("notasPaciente.labelIdPaciente")));
				subReporteConsolidadoPlano.addColumn(pacienteColumn.setHorizontalAlignment(HorizontalAlignment.LEFT), 
						idPacienteColumn.setHorizontalAlignment(HorizontalAlignment.LEFT));
			}
			
			if (paramsValidacion.get(IConstantesReporte.manejoEspecialInstOdonto) && 
					paramsValidacion.get(IConstantesReporte.reporteRango)) {
				TextColumnBuilder<String>     centroAtencionDuenioColumn  = col.column(
						messageResource.getMessage("notasPaciente.labelCentroAtencionDueno"),
						"centroAtencionDuenio",  type.stringType());
				listaTitulos.add(cmp.text(messageResource.getMessage("notasPaciente.labelCentroAtencionDueno")));
				subReporteConsolidadoPlano.addColumn(centroAtencionDuenioColumn.setHorizontalAlignment(HorizontalAlignment.LEFT));
			}
			
			if (paramsValidacion.get(IConstantesReporte.controlAbonoPacientePorIngreso)) {
				TextColumnBuilder<String>     ingresosColumn  = col.column(
						messageResource.getMessage("notasPaciente.labelIngreso"),
						"ingreso",  type.stringType());
				listaTitulos.add(cmp.text(messageResource.getMessage("notasPaciente.labelConcepto")));
				subReporteConsolidadoPlano.addColumn(ingresosColumn.setHorizontalAlignment(HorizontalAlignment.LEFT));
			}

			TextColumnBuilder<String>     conceptoColumn  = col.column(
					messageResource.getMessage("notasPaciente.labelConcepto"),
					"concepto",  type.stringType());
			listaTitulos.add(cmp.text(messageResource.getMessage("notasPaciente.labelConcepto")));
			subReporteConsolidadoPlano.addColumn(conceptoColumn.setHorizontalAlignment(HorizontalAlignment.LEFT));
			
			TextColumnBuilder<BigDecimal>     valorNotaColumn  = col.column(
					messageResource.getMessage("notasPaciente.labelValorNota"),
					"valorNota",  type.bigDecimalType()).setHorizontalAlignment(HorizontalAlignment.RIGHT);
			listaTitulos.add(cmp.text(messageResource.getMessage("notasPaciente.labelValorNota")));
			subReporteConsolidadoPlano.addColumn(valorNotaColumn);
			
			subReporteConsolidadoPlano.title(cmp.verticalList(
							cmp.horizontalList(listaTitulo)),
							cmp.horizontalList(listaDatosPaciente), 
							cmp.horizontalList(listaTitulos));
			subReporteConsolidadoPlano.setDetailSplitType(SplitType.PREVENT);
			subReporteConsolidadoPlano.setShowColumnTitle(false);
			subReporteConsolidadoPlano.setDataSource(getDataSourceNotasPacientesConsolidadoPlano(listaDtoResumenNotasPaciente))
					.build();  
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw e;
		}
		return subReporteConsolidadoPlano;
	}
	
	
	/**
	 * Método encargado de generar el reporte de Notas Paciente Formato A
	 * @param dtoNotaPaciente
	 * @param params
	 * @return JasperReportBuilder el reporte compilado
	 * @throws Exception
	 */
	public JasperReportBuilder  buildReporteNotasPacientes(DTONotaPaciente dtoNotaPaciente, 
			Map<String, String> params) throws Exception{
		//Inicialización del reporte
		JasperReportBuilder reportFormatoA = report();
		try{
			//Instancia de la clase que genera el diseño del reporte
			GeneradorDisenioReporte disenio = new GeneradorDisenioReporte();
			
			final String lineaFirma = "_________________________________";
			
			final int padding = 1;
			
			HorizontalListBuilder espacio;
			HorizontalListBuilder espacio2;
			HorizontalListBuilder linea1;
			HorizontalListBuilder linea4;
			HorizontalListBuilder linea5;
			HorizontalListBuilder linea6;
			HorizontalListBuilder linea7;
			HorizontalListBuilder linea8;
			
			VerticalListBuilder verticalList;
			VerticalListBuilder verticalList1;
			VerticalListBuilder verticalList2;
			
			espacio = cmp.horizontalList(cmp.text("").setHeight(15));
			
			double valorTotalNota = 0;
			
			StyleBuilder subtitulo = stl.style().bold().setVerticalAlignment(VerticalAlignment.TOP).setHorizontalAlignment(HorizontalAlignment.LEFT).setPadding(padding);
			StyleBuilder contenido = stl.style().setVerticalAlignment(VerticalAlignment.TOP).setHorizontalAlignment(HorizontalAlignment.LEFT).setPadding(padding);
			
			for (DtoInfoIngresoPacienteControlarAbonoPacientes dtoInfo : dtoNotaPaciente.getListaDtoInfoIngresoPacienteControlarAbonoPacientes()) {
				valorTotalNota += dtoInfo.getValorDevolucion();
			}
			
			verticalList1 = cmp.verticalList(
					cmp.horizontalList(cmp.text(messageResource.getMessage("notasPaciente.labelNota") + " " + dtoNotaPaciente.getNaturalezaNota() + " " + messageResource.getMessage("notasPaciente.labelNumero") + ":").setStyle(subtitulo).setWidth(58),
							cmp.text(dtoNotaPaciente.getConsecutivo().toString()).setStyle(contenido)),
					cmp.horizontalList(cmp.text(messageResource.getMessage("notasPaciente.labelPaciente") + ":").setStyle(subtitulo).setWidth(28),
							cmp.text(dtoNotaPaciente.getNombreCompletoPaciente()).setStyle(contenido)),
					cmp.horizontalList(cmp.text(messageResource.getMessage("notasPaciente.labelNaturalezaNota") + ":").setStyle(subtitulo).setWidth(52),
							cmp.text(dtoNotaPaciente.getNaturalezaNota()).setStyle(contenido)));
			
			verticalList2 = cmp.verticalList(
					cmp.horizontalList(cmp.text(messageResource.getMessage("notasPaciente.labelFechaGeneracion") + ":").setStyle(subtitulo).setWidth(55),
							cmp.text(dtoNotaPaciente.getFecha() + " " + dtoNotaPaciente.getHora()).setStyle(contenido)),			
					cmp.horizontalList(cmp.text(messageResource.getMessage("notasPaciente.labelIdPaciente") + ":").setStyle(subtitulo).setWidth(74),
							cmp.text(dtoNotaPaciente.getIdentificacionPaciente()).setStyle(contenido)),							
					cmp.horizontalList(cmp.text(messageResource.getMessage("notasPaciente.labelConcepto") + ":").setStyle(subtitulo).setWidth(27),
							cmp.text(dtoNotaPaciente.getDescripcionConcepto())).setStyle(contenido));	
			
			linea4 = cmp.horizontalList(cmp.text(messageResource.getMessage("notasPaciente.labelValorNota") + ":").setStyle(subtitulo).setWidth(13),
					cmp.text(UtilidadTexto.formatearValores(valorTotalNota)).setStyle(contenido));
			linea5 = cmp.horizontalList(cmp.text(messageResource.getMessage("notasPaciente.labelNota") + " " + dtoNotaPaciente.getNaturalezaNota() + " " + messageResource.getMessage("notasPaciente.labelGeneradaPor") + ":").setStyle(subtitulo).setWidth(35),
					cmp.text(dtoNotaPaciente.getUsuarioGeneraNota()).setStyle(contenido));
			linea6 = cmp.horizontalList(cmp.text(messageResource.getMessage("notasPaciente.labelObservaciones") + ":").setStyle(subtitulo).setWidth(20),
					cmp.text(dtoNotaPaciente.getObservaciones()).setStyle(contenido));
			
			linea7 = cmp.horizontalList(cmp.text(lineaFirma).setHorizontalAlignment(HorizontalAlignment.CENTER), 
					cmp.text(lineaFirma).setHorizontalAlignment(HorizontalAlignment.CENTER));
			linea8 = cmp.horizontalList(cmp.text(params.get(IConstantesReporte.usuarioProceso) + " (" + params.get(IConstantesReporte.loginUsuarioProceso) + ")").setHorizontalAlignment(HorizontalAlignment.CENTER), 
					cmp.text(messageResource.getMessage("notasPaciente.labelFirmaPaciente")).setHorizontalAlignment(HorizontalAlignment.CENTER));
			
			espacio2 = cmp.horizontalList(cmp.text("  ").setHeight(40));
			
			linea1 = cmp.horizontalList(
					verticalList1.setStretchType(StretchType.RELATIVE_TO_TALLEST_OBJECT), 
					verticalList2.setStretchType(StretchType.RELATIVE_TO_TALLEST_OBJECT))
					.setStretchType(StretchType.RELATIVE_TO_TALLEST_OBJECT);
			
			verticalList = cmp.verticalList(espacio, linea1, linea4, linea5, linea6, espacio2, linea7,  linea8);
			
			reportFormatoA
	          .setTemplate(disenio.crearPlantillaReporteDetalle())  
	          .setPageMargin(disenio.crearMagenesReporteDetalle())
	          .pageHeader(disenio.crearComponenteEncabezadoNotasPacientesFormatoAyB(params))
			  .summary(verticalList)
			  .setSummaryWithPageHeaderAndFooter(true)
			  .pageFooter(disenio.crearComponentePiePagina(params))
	          .pageFooter(cmp.pageXofY().setFormatExpression("Pag. {0} de {1}").setStyle(EstilosReportesDinamicos.estiloLetraFooter))
			  .build();
			
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw e;
		}
		return reportFormatoA;
	}
	
	/**
	 * Método encargado de generar el reporte de Notas Paciente Formato B
	 * @param dtoNotaPaciente
	 * @param params
	 * @return JasperReportBuilder el reporte compilado
	 * @throws Exception
	 */
	public JasperReportBuilder  buildReporteNotasPacientesXIngreso(DTONotaPaciente dtoNotaPaciente, 
			Map<String, String> params) throws Exception{
		//Inicialización del reporte
		JasperReportBuilder reportFormatoA = report();
		try{
			//Instancia de la clase que genera el diseño del reporte
			GeneradorDisenioReporte disenio = new GeneradorDisenioReporte();
			
			final String lineaFirma = "_________________________________";
			
			final int padding = 1;
			
			final int paddingDetalle = 2;
			
			HorizontalListBuilder espacio;
			HorizontalListBuilder espacio2;
			HorizontalListBuilder espacioPequeno;
			HorizontalListBuilder linea1;
			HorizontalListBuilder linea2;
			HorizontalListBuilder linea3;
			HorizontalListBuilder linea5;
			HorizontalListBuilder linea6;
			HorizontalListBuilder linea7;
			HorizontalListBuilder linea8;
			HorizontalListBuilder lineaDetalle;
			HorizontalListBuilder lineaTotal;
			
			VerticalListBuilder verticalList;
			VerticalListBuilder verticalList1;
			VerticalListBuilder verticalList2;
			VerticalListBuilder verticalListDetalle;
			
			espacio = cmp.horizontalList(cmp.text("").setHeight(10));
			
			espacioPequeno = cmp.horizontalList(cmp.text("").setHeight(5));
			
			double valorTotalNota = 0;
			
			StyleBuilder subtitulo = stl.style().bold().setFontSize(8).setVerticalAlignment(VerticalAlignment.TOP).setHorizontalAlignment(HorizontalAlignment.LEFT).setPadding(padding);
			StyleBuilder contenido = stl.style().setFontSize(8).setVerticalAlignment(VerticalAlignment.TOP).setHorizontalAlignment(HorizontalAlignment.LEFT).setPadding(padding);
			StyleBuilder tituloDetalle = stl.style().bold().setFontSize(8).setVerticalAlignment(VerticalAlignment.TOP).setPadding(paddingDetalle).setRightBorder(stl.pen(0.5F, LineStyle.SOLID).setLineColor(Color.BLACK));
			StyleBuilder tituloDetalleSinBorde = stl.style().bold().setFontSize(8).setVerticalAlignment(VerticalAlignment.TOP).setPadding(paddingDetalle);
			StyleBuilder detalle = stl.style().setFontSize(7).setVerticalAlignment(VerticalAlignment.TOP).setPadding(paddingDetalle).setRightBorder(stl.pen(0.5F, LineStyle.SOLID).setLineColor(Color.BLACK));
			StyleBuilder detalleSinBorde = stl.style().setFontSize(8).setVerticalAlignment(VerticalAlignment.TOP).setPadding(paddingDetalle);
			
			verticalList1 = cmp.verticalList(
					cmp.horizontalList(cmp.text(messageResource.getMessage("notasPaciente.labelNota") + " " + dtoNotaPaciente.getNaturalezaNota() + " " + messageResource.getMessage("notasPaciente.labelNumero") + ":").setStyle(subtitulo).setWidth(45),
							cmp.text(dtoNotaPaciente.getConsecutivo().toString()).setStyle(contenido)),
					cmp.horizontalList(cmp.text(messageResource.getMessage("notasPaciente.labelPaciente") + ":").setStyle(subtitulo).setWidth(20),
							cmp.text(dtoNotaPaciente.getNombreCompletoPaciente()).setStyle(contenido)),
					cmp.horizontalList(cmp.text(messageResource.getMessage("notasPaciente.labelNaturalezaNota") + ":").setStyle(subtitulo).setWidth(40),
							cmp.text(dtoNotaPaciente.getNaturalezaNota()).setStyle(contenido)));
			
			verticalList2 = cmp.verticalList(
					cmp.horizontalList(cmp.text(messageResource.getMessage("notasPaciente.labelFechaGeneracion") + ":").setStyle(subtitulo).setWidth(42),
							cmp.text(dtoNotaPaciente.getFecha() + " " + dtoNotaPaciente.getHora()).setStyle(contenido)),			
					cmp.horizontalList(cmp.text(messageResource.getMessage("notasPaciente.labelIdPaciente") + ":").setStyle(subtitulo).setWidth(55),
							cmp.text(dtoNotaPaciente.getIdentificacionPaciente()).setStyle(contenido)),							
					cmp.horizontalList(cmp.text(messageResource.getMessage("notasPaciente.labelConcepto") + ":").setStyle(subtitulo).setWidth(22),
							cmp.text(dtoNotaPaciente.getDescripcionConcepto()).setStyle(contenido)));	
			
			linea2 = cmp.horizontalList(cmp.text(messageResource.getMessage("notasPaciente.labelDetalleNota") + 
										" " + dtoNotaPaciente.getNaturalezaNota() +
										" " +messageResource.getMessage("notasPaciente.labelPorIngreso"))
						.setStyle(EstilosReportesDinamicos.estiloSombreadoSinBorde));
			
			linea3 = cmp.horizontalList(cmp.text(messageResource.getMessage("notasPaciente.labelNroIngreso")).setStyle(tituloDetalle).setHorizontalAlignment(HorizontalAlignment.CENTER),
						cmp.text(messageResource.getMessage("notasPaciente.labelCentroAtencion")).setStyle(tituloDetalle).setHorizontalAlignment(HorizontalAlignment.LEFT),
						cmp.text(messageResource.getMessage("notasPaciente.labelValor")).setStyle(tituloDetalleSinBorde).setHorizontalAlignment(HorizontalAlignment.RIGHT));
			
			verticalListDetalle = cmp.verticalList(linea2, linea3);
			
			for (DtoInfoIngresoPacienteControlarAbonoPacientes dtoInfo : dtoNotaPaciente.getListaDtoInfoIngresoPacienteControlarAbonoPacientes()) {
				lineaDetalle = cmp.horizontalList(cmp.text(dtoInfo.getConsecutivoIngresos()).setStyle(detalle).setHorizontalAlignment(HorizontalAlignment.CENTER),
											cmp.text(dtoInfo.getNombreCentroAtencionIngresos()).setStyle(detalle).setHorizontalAlignment(HorizontalAlignment.LEFT),
											cmp.text(dtoInfo.getValorNotaFormateado()).setStyle(detalleSinBorde).setHorizontalAlignment(HorizontalAlignment.RIGHT));
				verticalListDetalle.add(lineaDetalle);
				valorTotalNota += dtoInfo.getValorDevolucion();
			}
			
			lineaTotal = cmp.horizontalList(cmp.text("").setStyle(EstilosReportesDinamicos.estiloSombreadoSinBorde),
											cmp.text(messageResource.getMessage("notasPaciente.labelValorNota"))
												.setStyle(EstilosReportesDinamicos.estiloSombreadoSinBorde).setHorizontalAlignment(HorizontalAlignment.RIGHT),
											cmp.text(UtilidadTexto.formatearValores(valorTotalNota))
												.setStyle(EstilosReportesDinamicos.estiloSombreadoBordesIzq).setHorizontalAlignment(HorizontalAlignment.RIGHT));
			
			verticalListDetalle.add(lineaTotal);
			
			linea5 = cmp.horizontalList(cmp.text(messageResource.getMessage("notasPaciente.labelNota") + " " + dtoNotaPaciente.getNaturalezaNota() + " " + messageResource.getMessage("notasPaciente.labelGeneradaPor") + ":").setStyle(subtitulo).setWidth(25),
					cmp.text(dtoNotaPaciente.getUsuarioGeneraNota()).setStyle(contenido));
			linea6 = cmp.horizontalList(cmp.text(messageResource.getMessage("notasPaciente.labelObservaciones") + ":").setStyle(subtitulo).setWidth(15),
					cmp.text(dtoNotaPaciente.getObservaciones()).setStyle(contenido));
			
			linea7 = cmp.horizontalList(cmp.text(lineaFirma).setHorizontalAlignment(HorizontalAlignment.CENTER), 
					cmp.text(lineaFirma).setHorizontalAlignment(HorizontalAlignment.CENTER));
			linea8 = cmp.horizontalList(cmp.text(params.get(IConstantesReporte.usuarioProceso) + " (" + params.get(IConstantesReporte.loginUsuarioProceso) + ")").setHorizontalAlignment(HorizontalAlignment.CENTER), 
					cmp.text(messageResource.getMessage("notasPaciente.labelFirmaPaciente")).setHorizontalAlignment(HorizontalAlignment.CENTER));
			
			espacio2 = cmp.horizontalList(cmp.text("  ").setHeight(40));
			
			linea1 = cmp.horizontalList(
					verticalList1.setStretchType(StretchType.RELATIVE_TO_TALLEST_OBJECT), 
					verticalList2.setStretchType(StretchType.RELATIVE_TO_TALLEST_OBJECT))
					.setStretchType(StretchType.RELATIVE_TO_TALLEST_OBJECT);
			
			verticalList = cmp.verticalList(espacio, 
					linea1, espacioPequeno, verticalListDetalle, espacioPequeno, linea5, 
					linea6, espacio2, linea7,  linea8);
			
			reportFormatoA
	          .setTemplate(disenio.crearPlantillaReporteDetalle())  
	          .setPageMargin(disenio.crearMagenesReporteDetalle())
	          .pageHeader(disenio.crearComponenteEncabezadoNotasPacientesFormatoAyB(params))
			  .summary(verticalList)
			  .setSummaryWithPageHeaderAndFooter(true)
			  .pageFooter(disenio.crearComponentePiePagina(params))
	          .pageFooter(cmp.pageXofY().setFormatExpression("Pag. {0} de {1}").setStyle(EstilosReportesDinamicos.estiloLetraFooter))
			  .build();
			
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw e;
		}
		return reportFormatoA;
	}
	
	/**
	 * Método encargado de generar el subreporte con los datos del paciente
	 * para el reporte por paciente
	 * @param params
	 * @return JasperReportBuilder el reporte compilado
	 * @throws Exception
	 */
	public JasperReportBuilder buildDatosPaciente(Map<String, String> params) throws Exception{
		//Inicialización del reporte
		JasperReportBuilder infoPaciente = report();
		try{
			//Instancia de la clase que genera el diseño del reporte
			GeneradorDisenioReporte disenio = new GeneradorDisenioReporte();
			//Se definen las columnas que tendra el Reporte
			
			infoPaciente
	          .setTemplate(disenio.crearPlantillaReporteConsolidado())  
	          .setPageMargin(disenio.crearMagenesSubReporteInstitucion())
	          .title(disenio.crearComponenteInfoUsuario(params));
			infoPaciente.build();
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw e;
		}
		return infoPaciente;
	}
	
	/**
	 * Método que obtiene el data source de los notas paciente
	 * @param DtoResumentNotasPacientes
	 * @return JRDataSource
	 */
	private JRDataSource getDataSourceNotasPacientes(ArrayList<DtoResumenNotasPaciente> notasPacientes) {
		NotasPacienteDataSource dataSource = new NotasPacienteDataSource(notasPacientes);
		return dataSource.crearDataSourceNotaPacienteFormatoA();
	}
	
	/**
	 * Método que obtiene el data source de los notas paciente para el reporte plano
	 * @param DtoResumentNotasPacientes
	 * @return JRDataSource
	 */
	private JRDataSource getDataSourceNotasPacientesConsolidadoPlano(ArrayList<DtoResumenNotasPaciente> notasPacientes) {
		NotasPacienteDataSource dataSource = new NotasPacienteDataSource(notasPacientes);
		return dataSource.crearDataSourceNotaPacienteConsolidadoPlano();
	}
	
	/**
	 * Clase que permite crear un UtilExpression para 
	 * obtener los subtitulos de los totales del reporte según 
	 * el tipo de naturaleza requerida
	 * @author diecorqu
	 *
	 */
	private class UtilExpression {
		
		public DRISimpleExpression<String> obtenerUtilExpresionNotaDebito() {
			return new DRISimpleExpression<String>() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public Class<? super String> getValueClass() {
					return String.class;
				}
				
				@Override
				public String getName() {
					return "Total Notas Débito";
				}
				
				@Override
				public String evaluate(ReportParameters arg0) {
					return "Total Notas Débito";
				}
			};
		}
		
		public DRISimpleExpression<String> obtenerUtilExpresionNotaCredito() {
			return new DRISimpleExpression<String>() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public Class<? super String> getValueClass() {
					return String.class;
				}
				
				@Override
				public String getName() {
					return "Total Notas Crédito";
				}
				
				@Override
				public String evaluate(ReportParameters arg0) {
					return "Total Notas Crédito";
				}
			};
		}
		
		public DRISimpleExpression<String> obtenerUtilExpresionVacia() {
			return new DRISimpleExpression<String>() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public Class<? super String> getValueClass() {
					return String.class;
				}
				
				@Override
				public String getName() {
					return "";
				}
				
				@Override
				public String evaluate(ReportParameters arg0) {
					return "";
				}
			};
		}
	}
	
}
