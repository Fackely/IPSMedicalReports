package com.servinte.axioma.generadorReporte.tesoreria.impresionCierreTurnoCaja;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.math.BigDecimal;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.SplitType;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.jasperreports.engine.JRDataSource;
import util.ConstantesIntegridadDominio;
import util.reportes.dinamico.DataSource;

import com.princetonsa.dto.tesoreria.DtoInformacionEntrega;

public class GeneradorSubReporteFaltanteSobranteCierreTurno {

	public GeneradorSubReporteFaltanteSobranteCierreTurno() {
	}
		
	public   JasperReportBuilder generarReporteFaltanteSobrante(DtoInformacionEntrega dtoConsolidado){
		JasperReportBuilder faltanteSobrante = report();

		ComponentBuilder<?, ?> componentBuilder = null;
		HorizontalListBuilder tituloSeccion = null;
		
		tituloSeccion=	cmp.horizontalList(cmp.verticalList(cmp.horizontalList(
				cmp.text("Faltante / Sobrante en el cierre").setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT).bold())
		)))	;

		TextColumnBuilder<String>      formaPagoColumna = col.column("", "formaPago",  type.stringType()).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT)).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).bold());
		TextColumnBuilder<String>  	   faltanteSobranteColumn = col.column("", "faltanteSobrante",  type.stringType()).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.RIGHT)).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.RIGHT).bold());
		TextColumnBuilder<BigDecimal>  valorColumna 	= col.column("", "valor",  type.bigDecimalType()).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.RIGHT)).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.RIGHT).bold());

		componentBuilder = cmp.verticalList(tituloSeccion);
		faltanteSobrante
		.setShowColumnTitle(false)
		.pageHeader(componentBuilder)
		.setDetailSplitType(SplitType.PREVENT)
		.columns(formaPagoColumna,faltanteSobranteColumn,valorColumna)
		.setDataSource(crearDatasourceFaltanteSobrante(dtoConsolidado))
		.setPageMargin(margin().setLeft(0).setRight(0))
		.pageFooter(
				cmp.text("Observaciones Cierre Turno: "+dtoConsolidado.getObservacionesAceptacion()),
				cmp.text(""));
		
		faltanteSobrante.build();
		return  faltanteSobrante;
	}
		
	public JRDataSource crearDatasourceFaltanteSobrante(DtoInformacionEntrega dtoConsolidado){
		
		Integer tamano = dtoConsolidado.getFaltanteSobranteDTOs().size();
		
		DataSource dataSource = new DataSource("formaPago","faltanteSobrante","valor");
			for (int i = 0; i <tamano; i++) {
				dataSource.add(dtoConsolidado.getFaltanteSobranteDTOs().get(i).getFormaPago(),
							   (dtoConsolidado.getFaltanteSobranteDTOs().get(i).getTipoDiferencia().equals(ConstantesIntegridadDominio.acronimoDiferenciaFaltante)
									   ?"Faltante - "+dtoConsolidado.getFaltanteSobranteDTOs().get(i).getIdDetalleFaltanteSobrante()
									   :"Sobrante - "+dtoConsolidado.getFaltanteSobranteDTOs().get(i).getIdDetalleFaltanteSobrante()),
							   new BigDecimal(dtoConsolidado.getFaltanteSobranteDTOs().get(i).getValorDiferencia()));
			}
		return dataSource;
	}
		
}
