package com.servinte.axioma.generadorReporte.tesoreria.impresionEntregaCajaMayorPrincipal;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.exp;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.sbt;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.awt.Color;
import java.math.BigDecimal;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.Calculation;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.SplitType;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.jasperreports.engine.JRDataSource;
import util.UtilidadTexto;
import util.reportes.dinamico.DataSource;

import com.princetonsa.dto.tesoreria.DtoInformacionEntrega;

public class GeneradorSubReporteTotalFormaPagoEntregadoArqueoParcial {

	public GeneradorSubReporteTotalFormaPagoEntregadoArqueoParcial() {
	}
	
	public   JasperReportBuilder generarReporteTotalesFormaPago(DtoInformacionEntrega dtoConsolidado, boolean esConsulta, String mostrarParaSeccionEspecial, String tituloSubReporte){
		JasperReportBuilder totalDocumentos = report();

		ComponentBuilder<?, ?> componentBuilder = null;
		HorizontalListBuilder tituloSeccion = null;
		StyleBuilder estiloSubTituloSombraL;
			   
		if(UtilidadTexto.isEmpty(tituloSubReporte))
		{
			tituloSubReporte="Totales por Forma de Pago Entregados";
			estiloSubTituloSombraL = stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT).bold();
		}else
		{
			estiloSubTituloSombraL = stl.style().bold().setPadding(1).setFontSize(9).setHorizontalAlignment(HorizontalAlignment.LEFT)
			.setBackgroundColor(Color.LIGHT_GRAY);
		}
			
		
		tituloSeccion=	cmp.horizontalList(cmp.verticalList(cmp.horizontalList(
				cmp.text(tituloSubReporte).setStyle(estiloSubTituloSombraL)
		)))	;

		TextColumnBuilder<String>      documentoColumna = col.column("Forma Pago",  "formapago",  type.stringType()).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT)).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).bold());
		TextColumnBuilder<BigDecimal>  cantidadColumna = col.column("Valor Entregado",  "valor",  type.bigDecimalType()).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.RIGHT)).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.RIGHT).bold());

		componentBuilder = cmp.verticalList(tituloSeccion);
		totalDocumentos
		.pageHeader(componentBuilder)
		.setDetailSplitType(SplitType.PREVENT)
		.columns(documentoColumna,cantidadColumna)
		.subtotalsAtSummary(  
                sbt.aggregate(exp.text("Total Entregado"), documentoColumna, Calculation.NOTHING), sbt.sum(cantidadColumna))
                .setSubtotalStyle(stl.style().bold().setPadding(0))
		.setDataSource(crearDatasourceTotalesFormaPago(dtoConsolidado,esConsulta,mostrarParaSeccionEspecial))
		.setPageMargin(margin().setLeft(0).setRight(0))
		
		;
		if(dtoConsolidado.getCajaMayorPrincipal()!=null)
			totalDocumentos.pageFooter(cmp.text(""),crearInfoCajaMayorPrincipal(dtoConsolidado),cmp.text(""));
		else
			totalDocumentos.pageFooter(cmp.text(""));
			
		totalDocumentos.build();
		return  totalDocumentos;
	}
	
	private VerticalListBuilder crearInfoCajaMayorPrincipal(DtoInformacionEntrega dtoConsolidado){
		
		VerticalListBuilder informacion=cmp.verticalList(
				cmp.text("Información Caja Mayor/Principal").setStyle(stl.style().bold()),
				cmp.horizontalList(
						cmp.text("Caja: ").setStyle(stl.style().bold()).setWidth(6),
						cmp.text("("+dtoConsolidado.getCajaMayorPrincipal().getCodigo()
								+") - "+dtoConsolidado.getCajaMayorPrincipal().getDescripcion()
								+" - "+dtoConsolidado.getCajaMayorPrincipal().getCentroAtencion().getDescripcion()).setHorizontalAlignment(HorizontalAlignment.LEFT).setStyle(stl.style())
				),
				cmp.horizontalList(
						cmp.text("Observaciones: ").setStyle(stl.style().bold()).setWidth(18),
						cmp.text(dtoConsolidado.getObservaciones())
				)
		);
		
		return informacion; 
	}
	
	public JRDataSource crearDatasourceTotalesFormaPago(DtoInformacionEntrega dtoConsolidado,boolean esConsulta, String mostrarParaSeccionEspecial){
		
		Integer tamano = dtoConsolidado.getCuadreCajaDTOs().size();
		DataSource dataSource = new DataSource("formapago","valor");
			for (int i = 0; i <tamano; i++) {
				BigDecimal valorFormaPago= new BigDecimal(0.0);
				if(esConsulta)
					valorFormaPago = new BigDecimal(dtoConsolidado.getCuadreCajaDTOs().get(i).getValorSistema());
				else
				{
					if(mostrarParaSeccionEspecial.equals("entregaCajaMayorPrincipal"))
					{
						valorFormaPago = new BigDecimal(dtoConsolidado.getCuadreCajaDTOs().get(i).getValorSistema());
					}else{
						valorFormaPago = new BigDecimal(dtoConsolidado.getCuadreCajaDTOs().get(i).getValorCaja());
					}
				}
				dataSource.add(dtoConsolidado.getCuadreCajaDTOs().get(i).getFormaPago(),
								valorFormaPago);
			}
		return dataSource;
	}
}
