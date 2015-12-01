package com.servinte.axioma.generadorReporte.tesoreria.comunSubReportesImpresionArqueos;

import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.math.BigDecimal;
import java.util.ArrayList;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.SplitType;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.jasperreports.engine.JRDataSource;
import util.reportes.dinamico.DataSource;

import com.princetonsa.dto.tesoreria.DtoDetalleDocSopor;

public class GeneradorSubReporteDetallesFormaPago {

	public GeneradorSubReporteDetallesFormaPago() {
	}
	
	public   JasperReportBuilder generarReporteTotalesFormaPago(ArrayList<DtoDetalleDocSopor> listaConsolidado){
		JasperReportBuilder totalDocumentos = report();

		TextColumnBuilder<String>      	recibidoColumna = col.column("Recibido de"	 ,  "recibidode"	,  type.stringType()).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT)).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).bold());
		TextColumnBuilder<String>  		cantidadColumna  = col.column("No. Documento",  "numdocumento"		,  type.stringType()).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.RIGHT)).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.RIGHT).bold());
		TextColumnBuilder<BigDecimal>  	valorColumna 	 = col.column("Valor"		 ,  "valor"		,  type.bigDecimalType()).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.RIGHT)).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.RIGHT).bold());

		totalDocumentos
		.setDetailSplitType(SplitType.PREVENT)
		.columns(recibidoColumna,cantidadColumna,valorColumna)
		.setDataSource(crearDatasourceTotalesFormaPago(listaConsolidado))
		.setPageMargin(margin().setLeft(0).setRight(0))
		;
		
		totalDocumentos.build();
		return  totalDocumentos;
	}
	
	public JRDataSource crearDatasourceTotalesFormaPago(ArrayList<DtoDetalleDocSopor> listaConsolidado){
		
		Integer tamano = listaConsolidado.size();
		
		DataSource dataSource = new DataSource("recibidode","numdocumento","valor");
			for (int i = 0; i <tamano; i++) {
				dataSource.add(	listaConsolidado.get(i).getNombreRecibidoDe(),
								listaConsolidado.get(i).getNroDocumentoEntregado(),
								new BigDecimal(listaConsolidado.get(i).getValorSistemaUnico()));
			}
		return dataSource;
	}
	
}
