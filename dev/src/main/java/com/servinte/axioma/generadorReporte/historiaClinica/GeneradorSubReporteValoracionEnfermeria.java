/**
 * 
 */
package com.servinte.axioma.generadorReporte.historiaClinica;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.grp;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.template;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.awt.Color;
import java.util.HashMap;
import java.util.Locale;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.group.ColumnGroupBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.LineStyle;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.constant.SplitType;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.jasperreports.engine.JRDataSource;
import util.Utilidades;
import util.reportes.dinamico.DataSource;
import util.reportes.dinamico.EstilosReportesDinamicosHistoriaClinica;

/**
 * @author JorOsoVe
 *
 */ 
public class GeneradorSubReporteValoracionEnfermeria 
{

	

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public JasperReportBuilder crearEncabezados() 
	{
		JasperReportBuilder report = report();
		report.setTemplate(crearPlantillaReporte());
		
		 
        TextColumnBuilder<String> columnaGroup  = col.column("seccion","seccion",type.stringType());
		
        ColumnGroupBuilder itemGroup = grp.group(columnaGroup)
		.setStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)))
		.setPadding(0);

		
		
		TextColumnBuilder []cols = new TextColumnBuilder[4];
		
		TextColumnBuilder<String> columna  = col.column("Fecha y Hora","fechahora",type.stringType()).setStyle(stl.style().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK))).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		columna.setWidth(30);
		cols[0]=columna;
		
		columna  = col.column("Campo","campo",type.stringType()).setStyle(stl.style().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK))).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		columna.setWidth(30);
		cols[1]=columna;
		
		columna  = col.column("Valoración Enfermeria","valoracion",type.stringType()).setStyle(stl.style().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK))).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		columna.setWidth(30);
		cols[2]=columna;
		
		
		columna  = col.column("Usuario","usuario",type.stringType()).setStyle(stl.style().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK))).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		columna.setWidth(30);
		cols[3]=columna;
		
		report
		.setDetailSplitType(SplitType.PREVENT)
		.groupBy(itemGroup);
		
		report.columns(cols);
		report.title(cmp.text(" "));
		return report;
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public JRDataSource createDataSource(DtoImpresionHistoriaClinica dto) 
	{
		DataSource dataSource = new DataSource("seccion","fechahora", "campo", "valoracion", "usuario");
		HashMap mapa=dto.getValoracionesEnfermeria();
		int tamanioSeccion=Utilidades.convertirAEntero(mapa.get("numRegistros")+"");
		if(tamanioSeccion>0)
		{
			for(int k=0;k<tamanioSeccion;k++)
			{
				String nombreSeccion="";
				if(Utilidades.convertirAEntero(mapa.get("codigoseccion_"+k)+"")<=0)
				{
					nombreSeccion="Otros";
				}	
				else
				{
					nombreSeccion=mapa.get("etiquetaseccion_"+k)+"";
				}
				dataSource.add(nombreSeccion,mapa.get("fecha_"+k)+" "+mapa.get("hora_"+k),mapa.get("etiquetacampo_"+k), mapa.get("valor_"+k), mapa.get("nombreusuario_"+k));
			}
		}
		return dataSource;
	}

	/**
	 * @return templante para columnas de tablas de subreportes
	 */
	public ReportTemplateBuilder crearPlantillaReporte()
	{
		ReportTemplateBuilder reportTemplate;

		StyleBuilder rootStyle 				= stl.style().setPadding(1).setFontSize(9);
		StyleBuilder columnStyle 			= EstilosReportesDinamicosHistoriaClinica.estiloDetalleC.setBorder(stl.pen1Point().setLineColor(Color.BLACK));
		StyleBuilder columnTitleStyle		= EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSombra.setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBorder(stl.pen1Point().setLineColor(Color.BLACK)).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen1Point().setLineColor(Color.BLACK));
		StyleBuilder groupStyle				= stl.style(stl.style(rootStyle).bold()).setPadding(1).setFontSize(7).setHorizontalAlignment(HorizontalAlignment.LEFT);
		
		reportTemplate = template();
		reportTemplate.setLocale(Locale.ENGLISH);
		//Se define el tamano de la hoja y la orientación de la misma
		reportTemplate.setPageFormat(PageType.LETTER, PageOrientation.PORTRAIT);
		reportTemplate.setColumnStyle(columnStyle);
		reportTemplate.setColumnTitleStyle(columnTitleStyle);
		reportTemplate.setGroupStyle(groupStyle);
		
		return reportTemplate;
	}
	
	


}
