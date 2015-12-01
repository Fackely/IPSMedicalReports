/**
 * 
 */
package com.servinte.axioma.generadorReporte.historiaClinica;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.template;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.MarginBuilder;
import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
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
public class GeneradorSubReporteSoporteRespiratorio 
{
	/** 
	 * 
	 * @param dto
	 * @param columnas 
	 * @return
	 */
	public JasperReportBuilder crearEncabezados(DtoImpresionHistoriaClinica dto, ArrayList columnas) 
	{
		JasperReportBuilder report = report();
		report.setTemplate(crearPlantillaReporte());
		
		HashMap mapa=dto.getSoporteRespiratorio(); 
		
		TextColumnBuilder []cols = new TextColumnBuilder[columnas.size()];
		for(int j=0;j<columnas.size();j++)
		{
			TextColumnBuilder<String> columna  = null;
			if (j==0) {
				columna  =col.column("Soporte Respiratorio "+String.valueOf(columnas.get(j))  ,columnas.get(j)+"",type.stringType());
			}else{
				columna  =col.column(columnas.get(j)+"",columnas.get(j)+"",type.stringType());
			}
			
			
			
			columna.setWidth(10).setStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK))).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
			cols[j]=columna;
		}
		
		report
		.setPageMargin(crearMagenesReporte())
		.setDetailSplitType(SplitType.PREVENT)
		.columns(cols);
		report.title(cmp.text(" ").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloTituloUnderline).underline().setHorizontalAlignment(HorizontalAlignment.LEFT)));
		report.build();
		return report;
	}
	
	/**
	 * 
	 * @param dto
	 * @param columnas 
	 * @return
	 */
	public JRDataSource createDataSource(DtoImpresionHistoriaClinica dto, ArrayList columnas) 
	{
		HashMap mapa=dto.getSoporteRespiratorio();
		HashMap mapaTemp=(HashMap)mapa.get("soportes_0");
		int tamanio=Utilidades.convertirAEntero(mapa.get("numRegistros")+"");
		String[] colDatos=new String[columnas.size()];
		
		for(int j=0;j<columnas.size();j++)
	  	{
			colDatos[j]=columnas.get(j)+"";
		}
	  	
		DataSource dataSource = new DataSource(colDatos);
		
		for(int i=0;i<tamanio;i++)
		{
			int pos=0;
			Object[] datos=new Object[columnas.size()];
			datos[pos]=mapa.get("fecha_"+i)+" "+mapa.get("hora_"+i);
			pos++;
			
		  	mapaTemp=(HashMap)mapa.get("soportes_"+i);
		  	int numTemp = Utilidades.convertirAEntero(mapaTemp.get("numRegistros")+"");
		  	for(int j=0;j<numTemp;j++)
		  	{
		  		datos[obtenerPocisionAdicionReporte(mapaTemp, columnas,j)]=mapaTemp.get("valor_"+j)+"";
		  		pos++;
		  	}
		  	if(numTemp<(columnas.size()-3))//se restan 3, que son los campos fijos, (fecha/hora,observaciones,usuario)
		  	{
		  		for(int j=numTemp;j<(columnas.size()-3);j++)//se restan 3, que son los campos fijos, (fecha/hora,observaciones,usuario)
		  		{
		  			if(datos[pos]==null){
		  				datos[pos]="";
		  			}
		  			pos++;
		  		}
		  	}
  			datos[columnas.size()-2]=mapa.get("observaciones_"+i)+"";

  			datos[columnas.size()-1]=mapa.get("usuario_"+i)+"";
	
			dataSource.add(datos);
		}	
		return dataSource;
	}

	
	
	public Integer obtenerPocisionAdicionReporte(HashMap valorMapa,ArrayList columnas,Integer j){
		String parametro =String.valueOf(valorMapa.get("parametro_"+j));
		Boolean estadoCiclo=true;
		Integer contador=new Integer (0);
		for (int i = 0; i < columnas.size() && estadoCiclo; i++) {
			
			
			if(parametro.equals(columnas.get(i))){
				estadoCiclo=false;
				return contador;
			}
			contador++;
		} 
		
		return contador;
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
		
		reportTemplate = template();
		reportTemplate.setLocale(Locale.ENGLISH);
		//Se define el tamano de la hoja y la orientación de la misma
		reportTemplate.setPageFormat(PageType.LETTER, PageOrientation.PORTRAIT);
		reportTemplate.setColumnStyle(columnStyle);
		reportTemplate.setColumnTitleStyle(columnTitleStyle);
		
		return reportTemplate;
	}
	
	/**
	 * @return
	 */
	public MarginBuilder crearMagenesReporte()
	 {
	  MarginBuilder margin;
	  margin = margin()
	  .setLeft(0)
	  .setRight(0)
	  ;

	  return margin;
	 }


}
