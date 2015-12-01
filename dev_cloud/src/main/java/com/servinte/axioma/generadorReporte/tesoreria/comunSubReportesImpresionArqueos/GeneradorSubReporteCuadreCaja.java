package com.servinte.axioma.generadorReporte.tesoreria.comunSubReportesImpresionArqueos;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.exp;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.sbt;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.math.BigDecimal;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.constant.Calculation;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.SplitType;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.jasperreports.engine.JRDataSource;
import util.reportes.dinamico.DataSource;

import com.princetonsa.dto.tesoreria.DtoConsolidadoMovimiento;

public class GeneradorSubReporteCuadreCaja {

	public GeneradorSubReporteCuadreCaja() {
	}
	
	
	public   JasperReportBuilder generarReporteCuadreCaja(DtoConsolidadoMovimiento dtoConsolidado){
		JasperReportBuilder totalDocumentos = report();

		ComponentBuilder<?, ?> componentBuilder = null;
		HorizontalListBuilder tituloSeccion = null;
		
		tituloSeccion=	cmp.horizontalList(cmp.verticalList(cmp.horizontalList(
				cmp.text("Cuadre Caja").setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT).bold())
		)))	;

		TextColumnBuilder<String>     formaPago = col.column("Forma de Pago",  "formadepago",  type.stringType()).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT)).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).bold());
		TextColumnBuilder<BigDecimal>     valorCaja = col.column("Valor en Caja",  "valorencaja",  type.bigDecimalType()).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.RIGHT)).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.RIGHT).bold());
		TextColumnBuilder<BigDecimal>     valorSistema = col.column("Valor Sistema",  "valorsistema",  type.bigDecimalType()).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.RIGHT)).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.RIGHT).bold());
		TextColumnBuilder<BigDecimal>     diferencia = col.column("Diferencia",  "diferencia",  type.bigDecimalType()).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.RIGHT)).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.RIGHT).bold());

		componentBuilder = cmp.verticalList(tituloSeccion);
		totalDocumentos
		.pageHeader(componentBuilder)
		.setDetailSplitType(SplitType.PREVENT)
		.columns(formaPago,valorCaja,valorSistema,diferencia)
		.subtotalsAtSummary(  
                sbt.aggregate(exp.text("Total Forma de Pago"), formaPago, Calculation.NOTHING), sbt.sum(valorCaja),sbt.sum(valorSistema),sbt.sum(diferencia))
                .setSubtotalStyle(stl.style().bold().setPadding(0))
		.setDataSource(crearDatasourceCuadreCaja(dtoConsolidado))
		.setPageMargin(margin().setLeft(0).setRight(0))
		.pageFooter(cmp.text(""));
		
		totalDocumentos.build();
		
		return  totalDocumentos;

	}
	
	public JRDataSource crearDatasourceCuadreCaja(DtoConsolidadoMovimiento dtoConsolidado){
		
		Integer tamano = dtoConsolidado.getCuadreCajaDTOs().size();
		
		DataSource dataSource = new DataSource("formadepago","valorencaja","valorsistema","diferencia");
			for (int i = 0; i <tamano; i++) {
				dataSource.add(dtoConsolidado.getCuadreCajaDTOs().get(i).getFormaPago()+"",
							   new BigDecimal(dtoConsolidado.getCuadreCajaDTOs().get(i).getValorCaja()),
							   new BigDecimal(dtoConsolidado.getCuadreCajaDTOs().get(i).getValorSistema()),
							   new BigDecimal(dtoConsolidado.getCuadreCajaDTOs().get(i).getValorDiferencia()));
			}
		return dataSource;
		
	}

}
