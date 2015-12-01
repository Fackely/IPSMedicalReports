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
import util.UtilidadTexto;
import util.reportes.dinamico.DataSource;

import com.princetonsa.dto.tesoreria.DtoInformacionEntrega;

public class GeneradorSubReporteRegistroTrasladoCajaRecaudoCierreTurno {
	
	public GeneradorSubReporteRegistroTrasladoCajaRecaudoCierreTurno() {
	}
	
	public   JasperReportBuilder generarReporteTraladoCaja(DtoInformacionEntrega dtoConsolidado){
		JasperReportBuilder totalDocumentos = report();

		ComponentBuilder<?, ?> componentBuilder = null;
		HorizontalListBuilder tituloSeccion = null;
		
		tituloSeccion=	cmp.horizontalList(cmp.verticalList(cmp.horizontalList(
				cmp.text("Registro de Traslado a Caja de Recaudo Realizado en el cierre").setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT).bold())
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
		.setDataSource(crearDatasourceTraladoCaja(dtoConsolidado))
		.setPageMargin(margin().setLeft(0).setRight(0))
		.pageFooter(cmp.text(""));
		
		totalDocumentos.build();
		return  totalDocumentos;
	}
	
	
	public VerticalListBuilder crearEncabezado(DtoInformacionEntrega dtoConsolidado)
	{
		VerticalListBuilder encabezado=cmp.verticalList(
						cmp.horizontalList(
									cmp.text("Nro Consecutivo Traslado: ").setStyle(stl.style().bold()).setWidth(33),
									cmp.text(dtoConsolidado.getTrasladoCajaRecaudoEnCierre().getNroConsecutivoMovimiento()).setHorizontalAlignment(HorizontalAlignment.LEFT).setStyle(stl.style())
								),
						cmp.horizontalList(
									cmp.text("Caja Solicitada: ").setStyle(stl.style().bold()).setWidth(40),
									cmp.text("("+dtoConsolidado.getTrasladoCajaRecaudoEnCierre().getCodigoCajaRecaudo()
											+") - "+dtoConsolidado.getTrasladoCajaRecaudoEnCierre().getDescripcionCajaRecaudo()
											+" - "+dtoConsolidado.getTrasladoCajaRecaudoEnCierre().getDescripcionCentroAtenCajaRecaudo()).setHorizontalAlignment(HorizontalAlignment.LEFT).setStyle(stl.style()),
									cmp.text("Testigo: ").setStyle(stl.style().bold()).setWidth(20),
									cmp.text(dtoConsolidado.getTrasladoCajaRecaudoEnCierre().getPrimerNombreTestigo()+" "+(UtilidadTexto.isEmpty(dtoConsolidado.getTrasladoCajaRecaudoEnCierre().getSegundoNombreTestigo())?"":dtoConsolidado.getTrasladoCajaRecaudoEnCierre().getSegundoNombreTestigo())
											+" "+dtoConsolidado.getTrasladoCajaRecaudoEnCierre().getPrimerApellidoTestigo()
											+" "+(UtilidadTexto.isEmpty(dtoConsolidado.getTrasladoCajaRecaudoEnCierre().getSegundoApellidoTestigo())?"":dtoConsolidado.getTrasladoCajaRecaudoEnCierre().getSegundoApellidoTestigo())).setHorizontalAlignment(HorizontalAlignment.LEFT).setStyle(stl.style())
								),
						cmp.horizontalList(
								cmp.text("Observaciones Traslado: ").setStyle(stl.style().bold()).setWidth(30),
								cmp.text(dtoConsolidado.getTrasladoCajaRecaudoEnCierre().getObservacionesTraslado()).setHorizontalAlignment(HorizontalAlignment.LEFT).setStyle(stl.style())
							)		
				);
		
		return encabezado;
	}
	
	public JRDataSource crearDatasourceTraladoCaja(DtoInformacionEntrega dtoConsolidado){
		
		Integer tamano = dtoConsolidado.getTrasladoCajaRecaudoEnCierre().getTotalesTraslado().size();
		
		DataSource dataSource = new DataSource("formapago","valor");
			for (int i = 0; i <tamano; i++) {
				dataSource.add(dtoConsolidado.getTrasladoCajaRecaudoEnCierre().getTotalesTraslado().get(i).getFormaPago(),
							   new BigDecimal(dtoConsolidado.getTrasladoCajaRecaudoEnCierre().getTotalesTraslado().get(i).getTotal()));
			}
		return dataSource;
	}
	

}
