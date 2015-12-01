package com.servinte.axioma.generadorReporte.tesoreria.comunSubReportesImpresionArqueos;

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
import util.ValoresPorDefecto;
import util.reportes.dinamico.DataSource;

import com.princetonsa.dto.tesoreria.DtoConsolidadoMovimiento;

public class GeneradorSubReporteDevolucionesRecibosCaja {

	public GeneradorSubReporteDevolucionesRecibosCaja() {
	}
	
	
	public   JasperReportBuilder generarReporteDevolucionRecibosCaja(DtoConsolidadoMovimiento dtoConsolidado){
		JasperReportBuilder totalDocumentos = report();

		ComponentBuilder<?, ?> componentBuilder = null;
		HorizontalListBuilder tituloSeccion = null;
		
		tituloSeccion=	cmp.horizontalList(cmp.verticalList(cmp.horizontalList(
				cmp.text("Devoluciones Recibos de Caja (-)").setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT).bold())
		)))	;

		TextColumnBuilder<String>     columnaGrupo = col.column("",  "grupo",  type.stringType()).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT)).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).bold());
		TextColumnBuilder<String>     devolucion = col.column("Devolución",  "devolucion",  type.stringType()).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT)).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).bold());
		TextColumnBuilder<String>     estado = col.column("Estado",  "estado",  type.stringType()).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT)).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).bold());
		TextColumnBuilder<String>     recibidode = col.column("Recibido de",  "recibidode",  type.stringType()).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT)).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).bold());
		TextColumnBuilder<String>     id = col.column("No. Id",  "id",  type.stringType()).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT)).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).bold());
		TextColumnBuilder<String>     formapago = col.column("Forma de pago",  "formapago",  type.stringType()).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.RIGHT)).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.RIGHT).bold());
		TextColumnBuilder<BigDecimal> valor = col.column("Valor",  "valor",  type.bigDecimalType()).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.RIGHT)).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.RIGHT).bold());

		HorizontalListBuilder listaSuma = cmp.horizontalList();
		listaSuma.add(cmp.text("Total Devoluciones Recibos de Caja").setStyle(stl.style().bold()));
		listaSuma.add(cmp.text(UtilidadTexto.formatearValores(dtoConsolidado.getTotalDevolRecibosCaja())).setStyle(stl.style().bold()).setHorizontalAlignment(HorizontalAlignment.RIGHT));
		CustomGroupBuilder grupo = grp.group("grupo", "grupo", String.class).setStyle(stl.style().bold()).setPadding(0).setHeaderLayout(GroupHeaderLayout.EMPTY);
		grupo.addFooterComponent(listaSuma);
     	
		componentBuilder = cmp.verticalList(tituloSeccion);
		totalDocumentos
		.addGroup(grupo)
		.pageHeader(componentBuilder)
		.setDetailSplitType(SplitType.PREVENT)
		.columns(devolucion,estado,recibidode,id,formapago,valor)
		.setDataSource(crearDatasourceDevolucionReciboCaja(dtoConsolidado))
		.setPageMargin(margin().setLeft(0).setRight(0))
		.pageFooter(cmp.text(""))
		;
		
		totalDocumentos.build();
		
		return  totalDocumentos;

	}
	
	public JRDataSource crearDatasourceDevolucionReciboCaja(DtoConsolidadoMovimiento dtoConsolidado){
		
		Integer tamano = dtoConsolidado.getDevolucionReciboCajaDTOs().size();
		
		DataSource dataSource = new DataSource("grupo","devolucion","estado","recibidode","id","formapago","valor");
			for (int i = 0; i <tamano; i++) {
				dataSource.add(" ",
							   dtoConsolidado.getDevolucionReciboCajaDTOs().get(i).getConsecutivoDevolucion()+"",
							   ValoresPorDefecto.getIntegridadDominio(dtoConsolidado.getDevolucionReciboCajaDTOs().get(i).getAcronimoEstadoDevolucion()),
							   dtoConsolidado.getDevolucionReciboCajaDTOs().get(i).getRecibidoDe(),
							   dtoConsolidado.getDevolucionReciboCajaDTOs().get(i).getNumIdentificacion(),
							   dtoConsolidado.getDevolucionReciboCajaDTOs().get(i).getFormaPago(),
							   new BigDecimal(dtoConsolidado.getDevolucionReciboCajaDTOs().get(i).getValor()));
			}
		return dataSource;
	}

}
