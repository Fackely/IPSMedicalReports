package com.servinte.axioma.generadorReporte.tesoreria.impresionCierreTurnoCaja;

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
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.constant.Calculation;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.SplitType;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.jasperreports.engine.JRDataSource;
import util.reportes.dinamico.DataSource;

import com.princetonsa.dto.tesoreria.DtoInformacionEntrega;

public class GeneradorSubReporteRegistroEntregaCajaMayorCierreTurno {
	
	public GeneradorSubReporteRegistroEntregaCajaMayorCierreTurno() {
	}
	
	public   JasperReportBuilder generarReporteEntregaCajaMayor(DtoInformacionEntrega dtoConsolidado){
		JasperReportBuilder totalDocumentos = report();

		ComponentBuilder<?, ?> componentBuilder = null;
		HorizontalListBuilder tituloSeccion = null;
		
		tituloSeccion=	cmp.horizontalList(cmp.verticalList(cmp.horizontalList(
				cmp.text("Registro Entrega Caja Mayor / Principal Realizado en el cierre").setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT).bold())
		)))	;

		TextColumnBuilder<String>      documentoColumna = col.column("Forma Pago",  "formapago",  type.stringType()).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT)).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).bold());
		TextColumnBuilder<BigDecimal>     cantidadColumna = col.column("Valor Trasladado",  "valor",  type.bigDecimalType()).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.RIGHT)).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.RIGHT).bold());

		componentBuilder = cmp.verticalList(tituloSeccion,crearEncabezado(dtoConsolidado));
		totalDocumentos
		.pageHeader(componentBuilder)
		.setDetailSplitType(SplitType.PREVENT)
		.columns(documentoColumna,cantidadColumna)
		.subtotalsAtSummary(  
                sbt.aggregate(exp.text("Total Forma de Pago"), documentoColumna, Calculation.NOTHING), sbt.sum(cantidadColumna))
                .setSubtotalStyle(stl.style().bold().setPadding(0))
		.setDataSource(crearDatasourceEntregaCajaMayor(dtoConsolidado))
		.setPageMargin(margin().setLeft(0).setRight(0))
		.pageFooter(cmp.text(""));
		
		totalDocumentos.build();
		return  totalDocumentos;
	}
	
	
	public VerticalListBuilder crearEncabezado(DtoInformacionEntrega dtoConsolidado)
	{
		VerticalListBuilder encabezado=cmp.verticalList(
						cmp.horizontalList(
									cmp.text("Nro. Consecutivo Entrega: ").setStyle(stl.style().bold()).setWidth(45),
									cmp.text(dtoConsolidado.getTrasladoCajaMayorEnCierre().getNroConsecutivoMovimiento()).setHorizontalAlignment(HorizontalAlignment.LEFT).setStyle(stl.style()).setWidth(10),
									cmp.text("Caja Mayor/Principal: ").setStyle(stl.style().bold()).setWidth(40),
									cmp.text("("+dtoConsolidado.getTrasladoCajaMayorEnCierre().getCodigoCajaMayor()
											+") - "+dtoConsolidado.getTrasladoCajaMayorEnCierre().getDescripcionCajaMayor()
											+" - "+dtoConsolidado.getTrasladoCajaMayorEnCierre().getDescripcionCentroAtenCajaMayor()).setHorizontalAlignment(HorizontalAlignment.LEFT).setStyle(stl.style())
							)		
				);
		
		return encabezado;
	}
	
	public JRDataSource crearDatasourceEntregaCajaMayor(DtoInformacionEntrega dtoConsolidado){
		
		Integer tamano = dtoConsolidado.getTrasladoCajaMayorEnCierre().getTotalesTraslado().size();
		
		DataSource dataSource = new DataSource("formapago","valor");
			for (int i = 0; i <tamano; i++) {
				dataSource.add(dtoConsolidado.getTrasladoCajaMayorEnCierre().getTotalesTraslado().get(i).getFormaPago(),
							   new BigDecimal(dtoConsolidado.getTrasladoCajaMayorEnCierre().getTotalesTraslado().get(i).getTotal()));
			}
		return dataSource;
	}
	

}
