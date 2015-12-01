package com.servinte.axioma.generadorReporte.tesoreria.impresionEntregaATransportadora;

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
import util.ConstantesBD;
import util.reportes.dinamico.DataSource;

import com.princetonsa.dto.tesoreria.DtoDetalleDocSopor;
import com.princetonsa.dto.tesoreria.DtoEntidadesFinancieras;

public class GeneradorSubReporteDetalleEntrega {

	public GeneradorSubReporteDetalleEntrega() {
	}
	
	public   JasperReportBuilder generarReporteTotalesFormaPago(ArrayList<DtoEntidadesFinancieras> listaConsolidado,int codigoTipoDetalleFormaPago){
		JasperReportBuilder totalDocumentos = report();

		TextColumnBuilder<String>      	giradorColumna = col.column("Girador"	 ,  "girador"	,  type.stringType()).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT)).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).bold());
		TextColumnBuilder<String>  		numeroColumna  = col.column("Número",  "numero"		,  type.stringType()).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT)).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).bold());
		TextColumnBuilder<String>  		entidadColumna  = col.column("Entidad",  "entidad"		,  type.stringType()).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT)).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).bold());
		TextColumnBuilder<BigDecimal>  	valorColumna 	 = col.column("Valor"		 ,  "valor"		,  type.bigDecimalType()).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.RIGHT)).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).bold());

		totalDocumentos
		.setDetailSplitType(SplitType.PREVENT)
		
		.setPageMargin(margin().setLeft(0).setRight(0))
		;
		
		if(codigoTipoDetalleFormaPago==ConstantesBD.codigoTipoDetalleFormasPagoBono){
			totalDocumentos.columns(giradorColumna,numeroColumna,valorColumna);
			totalDocumentos.setDataSource(crearDatasourceTotalesFormaPago(listaConsolidado));
		}else
		{
			totalDocumentos.columns(giradorColumna,numeroColumna,entidadColumna,valorColumna);
			totalDocumentos.setDataSource(crearDatasourceTotalesFormaPagoEntidad(listaConsolidado));
		}
		
		
		totalDocumentos.build();
		return  totalDocumentos;
	}
	
	public JRDataSource crearDatasourceTotalesFormaPago(ArrayList<DtoEntidadesFinancieras> listaConsolidado){
		
		DataSource dataSource = new DataSource("girador","numero","valor");
			for (DtoEntidadesFinancieras listaEntidad: listaConsolidado) {
				
				for(DtoDetalleDocSopor listaDtoDoc:listaEntidad.getListadoDtoDetDocSoporte())
				{
					dataSource.add(	listaDtoDoc.getNombreRecibidoDe(),
							listaDtoDoc.getNroDocumentoEntregado(),
							new BigDecimal(listaDtoDoc.getValorSistemaUnico()));
				}
				
			}
		return dataSource;
	}
	
	public JRDataSource crearDatasourceTotalesFormaPagoEntidad(ArrayList<DtoEntidadesFinancieras> listaConsolidado){
		
		DataSource dataSource = new DataSource("girador","numero","entidad","valor");
			for (DtoEntidadesFinancieras listaEntidad: listaConsolidado) {
				
				for(DtoDetalleDocSopor listaDtoDoc:listaEntidad.getListadoDtoDetDocSoporte())
				{
					dataSource.add(	listaDtoDoc.getNombreRecibidoDe(),
							listaDtoDoc.getNroDocumentoEntregado(),
							listaEntidad.getDescripcionTercero(),
							new BigDecimal(listaDtoDoc.getValorSistemaUnico()));
				}
				
			}
		return dataSource;
	}
	
}
