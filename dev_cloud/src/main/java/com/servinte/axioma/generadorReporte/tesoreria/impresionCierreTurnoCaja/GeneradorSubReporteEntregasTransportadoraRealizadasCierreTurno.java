package com.servinte.axioma.generadorReporte.tesoreria.impresionCierreTurnoCaja;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.grp;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.math.BigDecimal;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.group.CustomGroupBuilder;
import net.sf.dynamicreports.report.constant.GroupHeaderLayout;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.SplitType;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.jasperreports.engine.JRDataSource;
import util.UtilidadTexto;
import util.reportes.dinamico.DataSource;

import com.princetonsa.dto.tesoreria.DtoConsolidadoMovimiento;

public class GeneradorSubReporteEntregasTransportadoraRealizadasCierreTurno {

	public GeneradorSubReporteEntregasTransportadoraRealizadasCierreTurno() {
	}
	
	
	public   JasperReportBuilder generarReporteEntregasTransportadora(DtoConsolidadoMovimiento dtoConsolidado){
		JasperReportBuilder totalDocumentos = report();

		ComponentBuilder<?, ?> componentBuilder = null;
		HorizontalListBuilder tituloSeccion = null;
		
		tituloSeccion=	cmp.horizontalList(cmp.verticalList(cmp.horizontalList(
				cmp.text("Entregas a Transportadora Realizadas (-)").setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT).bold())
		)))	;

		TextColumnBuilder<String>     columnaGrupo = col.column("",  "grupo",  type.stringType()).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT)).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).bold());
		TextColumnBuilder<String>     formaPago = col.column(" ",  "formapago",  type.stringType()).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT)).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).bold());
		TextColumnBuilder<BigDecimal>  entregado = col.column(" ",  "entregado",  type.bigDecimalType()).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.RIGHT)).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).bold());

		HorizontalListBuilder listaSuma = cmp.horizontalList();
		listaSuma.add(cmp.text("Total Entregas a Transportadora de Valores").setStyle(stl.style().bold()));
		listaSuma.add(cmp.text(UtilidadTexto.formatearValores((dtoConsolidado.getTotalEntregaTransportadoraCajaEntregado()))).setStyle(stl.style().bold()).setHorizontalAlignment(HorizontalAlignment.RIGHT));
		CustomGroupBuilder grupo = grp.group("grupo", "grupo", String.class).setStyle(stl.style().bold()).setPadding(0).setHeaderLayout(GroupHeaderLayout.EMPTY);
		grupo.addFooterComponent(listaSuma);
		
		componentBuilder = cmp.verticalList(tituloSeccion);
		totalDocumentos
		.addGroup(grupo)
		.setShowColumnTitle(false)
		.pageHeader(componentBuilder)
		.setDetailSplitType(SplitType.PREVENT)
		.columns(formaPago,entregado)
		.setDataSource(crearDatasourceEntregasTransportadora(dtoConsolidado))
		.setPageMargin(margin().setLeft(0).setRight(0))
		.pageFooter(cmp.text(""));

		totalDocumentos.build();
		
		return  totalDocumentos;

	}
	
	public JRDataSource crearDatasourceEntregasTransportadora(DtoConsolidadoMovimiento dtoConsolidado){
		
		Integer tamano = dtoConsolidado.getTotalesParcialesEntrTransDTOs().size();
		
		DataSource dataSource = new DataSource("grupo","formapago","entregado");
			for (int i = 0; i <tamano; i++) {
				dataSource.add(" ",
								dtoConsolidado.getTotalesParcialesEntrTransDTOs().get(i).getFormaPago(),
							   new BigDecimal(dtoConsolidado.getTotalesParcialesEntrTransDTOs().get(i).getTotal()));
			}
		return dataSource;
		
	}

}
