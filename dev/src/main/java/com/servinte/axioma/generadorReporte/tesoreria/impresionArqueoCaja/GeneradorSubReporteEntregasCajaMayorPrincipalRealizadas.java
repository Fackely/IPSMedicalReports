package com.servinte.axioma.generadorReporte.tesoreria.impresionArqueoCaja;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.grp;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;
import static net.sf.dynamicreports.report.builder.DynamicReports.variable;

import java.math.BigDecimal;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.builder.VariableBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.group.CustomGroupBuilder;
import net.sf.dynamicreports.report.constant.Calculation;
import net.sf.dynamicreports.report.constant.GroupHeaderLayout;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.SplitType;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.jasperreports.engine.JRDataSource;
import util.UtilidadTexto;
import util.reportes.dinamico.DataSource;

import com.princetonsa.dto.tesoreria.DtoConsolidadoMovimiento;

public class GeneradorSubReporteEntregasCajaMayorPrincipalRealizadas {
	
	public GeneradorSubReporteEntregasCajaMayorPrincipalRealizadas() {
	}
	
	
	public   JasperReportBuilder generarReporteEntregasCajaMayPrincRealizadas(DtoConsolidadoMovimiento dtoConsolidado){
		JasperReportBuilder totalDocumentos = report();

		ComponentBuilder<?, ?> componentBuilder = null;
		HorizontalListBuilder tituloSeccion = null;
		
		tituloSeccion=	cmp.horizontalList(cmp.verticalList(cmp.horizontalList(
				cmp.text("Entregas a Caja Mayor/Principal Realizadas (-)").setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT).bold())
		)))	;

		TextColumnBuilder<String>     columnaGrupo = col.column("",  "grupo",  type.stringType()).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT)).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).bold());
		TextColumnBuilder<String>     formaPago = col.column(" ",  "formapago",  type.stringType()).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT)).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).bold());
		TextColumnBuilder<BigDecimal>     valor = col.column(" ",  "valor",  type.bigDecimalType()).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.RIGHT)).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.RIGHT).bold());

		HorizontalListBuilder listaSuma = cmp.horizontalList();
     	listaSuma.add(cmp.text("TOTAL ENTREGAS A CAJA MAYOR/PRINCIPAL").setStyle(stl.style().bold()));
		CustomGroupBuilder grupo = grp.group("grupo", "grupo", String.class).setStyle(stl.style().bold()).setPadding(0).setHeaderLayout(GroupHeaderLayout.EMPTY);
		VariableBuilder<BigDecimal> valorGrpSum = variable(valor, Calculation.SUM);
		TextFieldBuilder<String> groupSuma = cmp.text(new CustomTextSubtotalValores(valorGrpSum));
		listaSuma.add(groupSuma.setHorizontalAlignment(HorizontalAlignment.RIGHT).setStyle(stl.style().bold()));
		grupo.addFooterComponent(listaSuma);
		
		totalDocumentos.variables(valorGrpSum);
		
		componentBuilder = cmp.verticalList(tituloSeccion);
		totalDocumentos
		.setShowColumnTitle(false)
		.addGroup(grupo)
		.pageHeader(componentBuilder)
		.setDetailSplitType(SplitType.PREVENT)
		.columns(formaPago,valor)
		.setDataSource(crearDatasourceEntregasCajaMayPrincRealizadas(dtoConsolidado))
		.setPageMargin(margin().setLeft(0).setRight(0))
		.pageFooter(cmp.text(""));
		
		totalDocumentos.build();
		
		return  totalDocumentos;

	}
	
	public JRDataSource crearDatasourceEntregasCajaMayPrincRealizadas(DtoConsolidadoMovimiento dtoConsolidado){
		
		Integer tamano = dtoConsolidado.getTotalesParcialesEntrCajaDTOs().size();
		
		DataSource dataSource = new DataSource("grupo","formapago","valor");
			for (int i = 0; i <tamano; i++) {
				dataSource.add(" ",
							   dtoConsolidado.getTotalesParcialesEntrCajaDTOs().get(i).getFormaPago(),
							   new BigDecimal(dtoConsolidado.getTotalesParcialesEntrCajaDTOs().get(i).getTotal()));
			}
		return dataSource;
		
	}

	/**
	 * Esta clase ayuda a calcular los totales de los valores 
	 * almacenados en variables del reporte
	 * @author Fabi?n Becerra
	 */
    private class CustomTextSubtotalValores extends AbstractSimpleExpression<String> {
		private static final long serialVersionUID = 1L;
		private VariableBuilder<BigDecimal> valorSum;
		public CustomTextSubtotalValores(VariableBuilder<BigDecimal> valorSum) {
			this.valorSum = valorSum;
		}
		public String evaluate(ReportParameters reportParameters) {
			BigDecimal valorSumValue = reportParameters.getValue(valorSum);
			return type.bigDecimalType().valueToString(valorSumValue, reportParameters.getLocale());			       
		}		
	}


}
