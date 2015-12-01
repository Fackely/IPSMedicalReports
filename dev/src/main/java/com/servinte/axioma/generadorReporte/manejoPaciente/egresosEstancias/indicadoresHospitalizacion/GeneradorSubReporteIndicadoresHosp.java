package com.servinte.axioma.generadorReporte.manejoPaciente.egresosEstancias.indicadoresHospitalizacion;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.grp;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;
import static net.sf.dynamicreports.report.builder.DynamicReports.variable;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;

import java.awt.Color;
import java.util.ArrayList;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.builder.VariableBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.group.CustomGroupBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.Calculation;
import net.sf.dynamicreports.report.constant.GroupHeaderLayout;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.jasperreports.engine.JRDataSource;
import util.reportes.dinamico.DataSource;

import com.princetonsa.dto.manejoPaciente.DtoResultadoConsultaIndicadoresHospitalizacion;

public class GeneradorSubReporteIndicadoresHosp {

	/**
	 * Método constructor de la clas
	 */
	public GeneradorSubReporteIndicadoresHosp() {
	}

	/**
	 * Método que genera el subreporte indicadores hospitalización
	 * @param dtoResultado datos de la consulta
	 * @return JasperReportBuilder reporte consolidado
	 */
	@SuppressWarnings("unused")
	public JasperReportBuilder generarReporte(ArrayList<DtoResultadoConsultaIndicadoresHospitalizacion> listaDtoResultados, boolean isPlano)
	{
		JasperReportBuilder reporte = report();
		
		//----------------Estilos Para tabla datos---------------
		StyleBuilder estiloBordesTitulos= stl.style().setBorder(stl.penThin().setLineColor(Color.BLACK))
						.setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).bold();
		StyleBuilder estiloBordesDatosDerecha= stl.style().setBorder(stl.penThin().setLineColor(Color.BLACK)).setHorizontalAlignment(HorizontalAlignment.RIGHT);
		StyleBuilder estiloBordesDatosIzquierda= stl.style().setBorder(stl.penThin().setLineColor(Color.BLACK)).setHorizontalAlignment(HorizontalAlignment.LEFT);
		//--------------------------------------------------------
		
		reporte.setPageFormat(PageType.LETTER, PageOrientation.PORTRAIT);
		
		TextColumnBuilder<String>  convenioColumna = col.column("Convenio",  "convenio",  type.stringType()).setWidth(180)
			.setStyle(estiloBordesDatosIzquierda)
			.setTitleStyle(stl.style(estiloBordesTitulos));
		TextColumnBuilder<String> estadoEgresoColumna 	= col.column("Estado Egreso",  "estadoEgreso",  type.stringType()).setWidth(70)
			.setStyle(estiloBordesDatosIzquierda)
			.setTitleStyle(stl.style(estiloBordesTitulos));
		TextColumnBuilder<String> sexoColumna 	= col.column("Sexo Paciente",  "sexo",  type.stringType()).setWidth(60)
			.setStyle(estiloBordesDatosIzquierda)
			.setTitleStyle(stl.style(estiloBordesTitulos));
		TextColumnBuilder<Integer> cantidadColumna 	= col.column("Cantidad Pacientes",  "cantidad",  type.integerType()).setWidth(60)
			.setStyle(estiloBordesDatosDerecha)
			.setTitleStyle(stl.style(estiloBordesTitulos));
		
		if(!isPlano)
		{
			TextColumnBuilder<String>  columnaGrupo = col.column("",  "grupo",  type.stringType());
			CustomGroupBuilder grupoCantidades= grp.group("grupo", "grupo", String.class).setHorizontalAlignment(HorizontalAlignment.LEFT).setStyle(stl.style()).setPadding(0).setHeaderLayout(GroupHeaderLayout.EMPTY);
			HorizontalListBuilder listaGrupo = cmp.horizontalList();
			listaGrupo.add(cmp.text("Total Pacientes Hospitalizados").setWidth(310).setStyle(stl.style().bold().setBorder(stl.penThin().setLineColor(Color.BLACK))));
			VariableBuilder<Integer> cantidadGrpSum = variable(cantidadColumna, Calculation.SUM);
			TextFieldBuilder<String> sumaCantidades = cmp.text(new CustomTextSubtotalCantidades(cantidadGrpSum)).setStyle(stl.style());
			listaGrupo.add(sumaCantidades.setHorizontalAlignment(HorizontalAlignment.RIGHT).setStyle(stl.style().setBorder(stl.penThin().setLineColor(Color.BLACK))).setWidth(60));
			grupoCantidades.addFooterComponent(listaGrupo, cmp.text("").setHeight(5));
			reporte.addGroup(grupoCantidades);
			reporte.variables(cantidadGrpSum);
		}
		
		reporte.columns(convenioColumna,estadoEgresoColumna,sexoColumna,cantidadColumna);
		reporte.setDataSource(crearDatasourceIndicHospitalizacion(listaDtoResultados));
		reporte.setPageMargin(margin().setLeft(0).setRight(0));
		reporte.build();
		
		return reporte;
	}
	
	/**
	 * Metodo que crea el datasource para el reporte indicadores hospitalizacion
	 * @param dtoConsolidado dto con información para generar el reporte
	 * @return datasource
	 */
	public JRDataSource crearDatasourceIndicHospitalizacion(ArrayList<DtoResultadoConsultaIndicadoresHospitalizacion> listaDtoResultados){
		
		DataSource dataSource = new DataSource("convenio","estadoEgreso","sexo","cantidad");
			for (DtoResultadoConsultaIndicadoresHospitalizacion lista: listaDtoResultados) {
				dataSource.add(" "+lista.getNombreConvenio(),
							" "+lista.getEstadoEgreso(),
							" "+lista.getSexoPaciente(),
							lista.getCantidadPacientes());
			}
		return dataSource;
	}
	
	
	/**
	 * Esta clase ayuda a calcular los totales de las cantidades
	 * almacenadas en variables del reporte
	 * @author Fabián Becerra
	 */
    private class CustomTextSubtotalCantidades extends AbstractSimpleExpression<String> {
		private static final long serialVersionUID = 1L;
		private VariableBuilder<Integer> cantidadSum;
		public CustomTextSubtotalCantidades(VariableBuilder<Integer> cantidadSum) {
			this.cantidadSum = cantidadSum;
		}
		public String evaluate(ReportParameters reportParameters) {
			Integer cantidadSumValue = reportParameters.getValue(cantidadSum);
			return type.integerType().valueToString(cantidadSumValue, reportParameters.getLocale());			       
		}		
	} 
}
