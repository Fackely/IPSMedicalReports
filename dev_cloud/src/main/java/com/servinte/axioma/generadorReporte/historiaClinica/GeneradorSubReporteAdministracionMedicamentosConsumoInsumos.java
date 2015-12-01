package com.servinte.axioma.generadorReporte.historiaClinica;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.grp;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.awt.Color;
import java.util.HashMap;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.group.ColumnGroupBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.LineStyle;
import net.sf.dynamicreports.report.constant.SplitType;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.jasperreports.engine.JRDataSource;

import org.apache.struts.util.MessageResources;

import util.Utilidades;
import util.reportes.dinamico.DataSource;
import util.reportes.dinamico.EstilosReportesDinamicosHistoriaClinica;

import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

public class GeneradorSubReporteAdministracionMedicamentosConsumoInsumos {
	
	/**
	 * Mensajes parametrizados del reporte.
	 */
	private static MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.historiaclinica.impresionHistoriaClinica");

	
	public GeneradorSubReporteAdministracionMedicamentosConsumoInsumos() {
		// TODO Auto-generated constructor stub
	}
	
	
	public JasperReportBuilder generarReporteSeccionConsumoInsumos(DtoImpresionHistoriaClinica dto
			,UsuarioBasico usuario, PersonaBasica paciente){
		JasperReportBuilder consumoInsumos = report();
		
		TextColumnBuilder []cols = new TextColumnBuilder[4];
		ComponentBuilder<?, ?> componentBuilder = null;
		HorizontalListBuilder tituloSeccion = null;
		GeneradorDisenoSubReportes disenio = new GeneradorDisenoSubReportes();
		
		
		tituloSeccion=	cmp.horizontalList(cmp.verticalList(cmp.horizontalList(
				cmp.text(messageResource.getMessage("historia_clinica_lable_valoracion_titulo_Insumos")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloTituloUnderline).underline().setHorizontalAlignment(HorizontalAlignment.LEFT))
		)))	;
		
		componentBuilder = cmp.verticalList(tituloSeccion);
		consumoInsumos
		.summary(componentBuilder)
		.summary(cmp.subreport(generarReporteSeccionConsumoInsumosDetail(dto, usuario, paciente)))
		.setTemplate(disenio.crearPlantillaReporte())
		.setPageMargin(disenio.crearMagenesReporte())
		.build();
		return consumoInsumos;
		
	}
	
	public JasperReportBuilder generarReporteSeccionConsumoInsumosDetail(DtoImpresionHistoriaClinica dto
			,UsuarioBasico usuario, PersonaBasica paciente){
		JasperReportBuilder consumoInsumos = report();
		
		TextColumnBuilder []cols = new TextColumnBuilder[4];
		ComponentBuilder<?, ?> componentBuilder = null;
		HorizontalListBuilder tituloSeccion = null;
		GeneradorDisenoSubReportes disenio = new GeneradorDisenoSubReportes();
		
		
		TextColumnBuilder<String>     fecha = col.column("Fecha/Hora Consumo",  "fecha",  type.stringType()).setTitleStyle(stl.style().setBackgroundColor(Color.LIGHT_GRAY).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     insumo = col.column("Insumos Descripción",  "insumo",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     cantidad = col.column("Unidades Consumidas",  "cantidad",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     responsable = col.column("Responsable",  "responsable",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		
		
		cols[0]=fecha;
		cols[1]=insumo.setWidth(180);
		cols[2]=cantidad.setWidth(80);
		cols[3]=responsable.setWidth(80);
		
		ColumnGroupBuilder fechaGroup = grp.group(fecha)
		.setStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)))
		.setPadding(0);
		
		consumoInsumos
		.columns(cols)
		.groupBy(fechaGroup)
		.setDataSource(crearDatasourceConsumoInsumos(dto.getConsumosInsumos()))
		.setTemplate(disenio.crearPlantillaReporte())
		.setDetailSplitType(SplitType.PREVENT)
		.setPageMargin(disenio.crearMagenesSubReporte())
		.build();
		
		
		return consumoInsumos;
		
	}
	
	public JRDataSource crearDatasourceConsumoInsumos (HashMap insumos){
		DataSource dataSource = new DataSource("fecha","insumo","cantidad","responsable");
		
		Integer tamanio=Utilidades.convertirAEntero(insumos.get("numRegistros")+"");
		if (tamanio>0) {
			for (int i = 0; i < tamanio; i++) {
				dataSource.add(String.valueOf(insumos.get("fecha_"+i))+" "+ String.valueOf(insumos.get("hora_"+i)),
						       String.valueOf(insumos.get("insumo_"+i)),
						       String.valueOf(insumos.get("cantidad_"+i)),
						       String.valueOf(insumos.get("responsable_"+i)));
			}	
		}
		
		return dataSource;
	}
	

}
