package com.servinte.axioma.generadorReporte.tesoreria.comunSubReportesImpresionArqueos;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.exp;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.sbt;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;
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

/**
 * Clase que genera el subreporte con el total documentos generados
 * @author Fabián Becerra
 *
 */
public class GeneradorSubReporteTotalDocumentos {

	/**
	 * Constructor de la clase
	 */
	public GeneradorSubReporteTotalDocumentos() {
	}
	
	/**
     * Este método genera y configura el subreporte total documentos
     * @return reporte subreporte total documentos
     */
	public  JasperReportBuilder generarReporteTotalDocumentos(DtoConsolidadoMovimiento dtoConsolidado){
		JasperReportBuilder totalDocumentos = report();

		ComponentBuilder<?, ?> componentBuilder = null;
		HorizontalListBuilder tituloSeccion = null;
		
		tituloSeccion=	cmp.horizontalList(cmp.verticalList(cmp.horizontalList(
				cmp.text("Total Documentos").setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT).bold())
		)))	;

		TextColumnBuilder<String>      documentoColumna = col.column("Documento",  "documento",  type.stringType()).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT)).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).bold());
		TextColumnBuilder<Integer>     cantidadColumna = col.column("Cantidad",  "cantidad",  type.integerType()).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.RIGHT)).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.RIGHT).bold());

		componentBuilder = cmp.verticalList(tituloSeccion);
		totalDocumentos
		.pageHeader(componentBuilder)
		.setDetailSplitType(SplitType.PREVENT)
		.columns(documentoColumna,cantidadColumna)
		.subtotalsAtSummary(  
                sbt.aggregate(exp.text("Total Documentos"), documentoColumna, Calculation.NOTHING), sbt.sum(cantidadColumna))
                .setSubtotalStyle(stl.style().bold().setPadding(0))
		.setDataSource(crearDatasourceTotalDocumentos(dtoConsolidado))
		.setPageMargin(margin().setLeft(0).setRight(0))
		.pageFooter(cmp.text(""));
		
		totalDocumentos.build();
		return  totalDocumentos;
	}
	
	/**
	 * Metodo que crea el datasource para el reporte total documentos
	 * @param dtoConsolidado dto con información para generar el subreporte
	 * @return datasource
	 */
	public JRDataSource crearDatasourceTotalDocumentos(DtoConsolidadoMovimiento dtoConsolidado){
		
		Integer tamano = dtoConsolidado.getTotalesDocumentoDTOs().size();
		
		DataSource dataSource = new DataSource("documento","cantidad");
			for (int i = 0; i <tamano; i++) {
				dataSource.add(dtoConsolidado.getTotalesDocumentoDTOs().get(i).getDescripcion(),
							   dtoConsolidado.getTotalesDocumentoDTOs().get(i).getTotal());
			}
		return dataSource;
	}
}
