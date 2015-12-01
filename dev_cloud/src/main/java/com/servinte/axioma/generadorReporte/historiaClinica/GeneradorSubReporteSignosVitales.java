/**
 * 
 */
package com.servinte.axioma.generadorReporte.historiaClinica;

import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.template;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;

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
import util.UtilidadTexto;
import util.reportes.dinamico.DataSource;
import util.reportes.dinamico.EstilosReportesDinamicosHistoriaClinica;

/**
 * @author JorOsoVe
 *
 */
public class GeneradorSubReporteSignosVitales {

	 
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public JasperReportBuilder generarReporteSignosVitales(DtoSignosVitalesHC signosVitales) 
	{
		JasperReportBuilder report = report();


		TextColumnBuilder []cols = new TextColumnBuilder[8+signosVitales.getSignosVitalesInstitucionCcosto().size()];


		TextColumnBuilder<String> columna  = col.column(" Signos Vitales Fecha/Hora","fechahora",type.stringType()).setStyle(stl.style().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK))).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		columna.setWidth(20);
		cols[0]=columna;

		columna  = col.column("F.C.\npulsos/min","fc",type.stringType()).setStyle(stl.style().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK))).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		columna.setWidth(15);
		cols[1]=columna;

		columna  = col.column("F.R.\n/min","fr",type.stringType()).setStyle(stl.style().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK))).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		columna.setWidth(15);
		cols[2]=columna;

		columna  = col.column("P.A.S.\n/mmHg","pas",type.stringType()).setStyle(stl.style().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK))).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		columna.setWidth(15);
		cols[3]=columna;

		columna  = col.column("P.A.D.\n/mmHg","pad",type.stringType()).setStyle(stl.style().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK))).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		columna.setWidth(15);
		cols[4]=columna;

		columna = col.column("P.A.M.\nmmHg","pam",type.stringType()).setStyle(stl.style().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK))).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		columna.setWidth(15);
		cols[5]=columna;

		columna  = col.column("TEMP\n°C","temp",type.stringType()).setStyle(stl.style().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK))).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		columna.setWidth(15);
		cols[6]=columna;

		Iterator iterador=signosVitales.getSignosVitalesInstitucionCcosto().iterator();
		int contador=7;

		while(iterador.hasNext())
		{
			HashMap fila=(HashMap)iterador.next();
			columna  = col.column(fila.get("nombre")+"",(fila.get("nombre")+"").trim(),type.stringType()).setStyle(stl.style().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK))).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
			columna.setWidth(15);
			cols[contador]=columna;
			contador++;
		}

		columna  = col.column("Usuario","usuario",type.stringType()).setStyle(stl.style().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK))).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		columna.setWidth(15);
		cols[contador]=columna;

		report
		.columns(cols)
		.setDataSource(createDataSource(signosVitales))
		.setDetailSplitType(SplitType.STRETCH)
		.setTemplate(crearPlantillaReporte())
		.setPageMargin(crearMagenesReporte())
		.build();

		return report;
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public JRDataSource createDataSource(DtoSignosVitalesHC signosVitales) 
	{
		Vector[] matrizSigVitalFijos = new Vector[signosVitales.getSignosVitalesFijosHisto().size()];
		Iterator iterador=signosVitales.getSignosVitalesFijosHisto().iterator();
		int numFila=0;
		while(iterador.hasNext())
		{
			HashMap fila=(HashMap)iterador.next();
			matrizSigVitalFijos[numFila]=new Vector();
			matrizSigVitalFijos[numFila].add(fila.get("codigo_histo_enfer"));
			matrizSigVitalFijos[numFila].add(fila.get("fc"));	
			matrizSigVitalFijos[numFila].add(fila.get("fr"));
			matrizSigVitalFijos[numFila].add(fila.get("pas"));
			matrizSigVitalFijos[numFila].add(fila.get("pad"));
			matrizSigVitalFijos[numFila].add(fila.get("pam"));
			matrizSigVitalFijos[numFila].add(fila.get("temp"));
			 
			numFila++;
			
		}
		
		String[] columnasDataSource=new String[8+signosVitales.getSignosVitalesInstitucionCcosto().size()];
		columnasDataSource[0]="fechahora";
		columnasDataSource[1]="fc";
		columnasDataSource[2]="fr";
		columnasDataSource[3]="pas";
		columnasDataSource[4]="pad";
		columnasDataSource[5]="pam";
		columnasDataSource[6]="temp";
		
		iterador=signosVitales.getSignosVitalesInstitucionCcosto().iterator();
		int contador=7;
		while(iterador.hasNext())
		{
			HashMap fila=(HashMap)iterador.next();
			columnasDataSource[contador]=(fila.get("nombre")+"").trim();
			contador++;
		}
		columnasDataSource[contador]="usuario";
		DataSource dataSource = new DataSource(columnasDataSource);
		
		Object[] columnasDatos=new Object[8+signosVitales.getSignosVitalesInstitucionCcosto().size()];
		
		iterador=signosVitales.getSignosVitalesHistoTodos().iterator();
		while(iterador.hasNext())
		{
			HashMap fila3=(HashMap)iterador.next();
			columnasDatos[0]=fila3.get("fecha_registro")+"-"+fila3.get("hora_registro");
			for(int fil1=0; fil1<matrizSigVitalFijos.length; fil1++)
			{
				if(matrizSigVitalFijos[fil1].elementAt(0).equals(fila3.get("codigo_histo_enfer")))
				{
					
					/*
					* Tipo Modificacion: Segun incidencia 6296
					* Autor: Jesús Darío Ríos
					* usuario: jesrioro
					* Fecha: 04/04/2013
					* Descripcion:  Se cambia  elementAt(1)+"")+...  para  que  tome todos  los  datos  de la  matriz
					* 				de los  signos  vitales  en  el  reporte.          
					*/
					columnasDatos[1]=(!UtilidadTexto.isEmpty(matrizSigVitalFijos[fil1].elementAt(1)+""))?(matrizSigVitalFijos[fil1].elementAt(1)+""):"";
					columnasDatos[2]=(!UtilidadTexto.isEmpty(matrizSigVitalFijos[fil1].elementAt(2)+""))?(matrizSigVitalFijos[fil1].elementAt(2)+""):"";
					columnasDatos[3]=(!UtilidadTexto.isEmpty(matrizSigVitalFijos[fil1].elementAt(3)+""))?(matrizSigVitalFijos[fil1].elementAt(3)+""):"";
					columnasDatos[4]=(!UtilidadTexto.isEmpty(matrizSigVitalFijos[fil1].elementAt(4)+""))?(matrizSigVitalFijos[fil1].elementAt(4)+""):"";
					columnasDatos[5]=(!UtilidadTexto.isEmpty(matrizSigVitalFijos[fil1].elementAt(5)+""))?(matrizSigVitalFijos[fil1].elementAt(5)+""):"";
					columnasDatos[6]=(!UtilidadTexto.isEmpty(matrizSigVitalFijos[fil1].elementAt(6)+""))?(matrizSigVitalFijos[fil1].elementAt(6)+""):"";
					break;
				}
			}
			
			Iterator iteradorInterno=signosVitales.getSignosVitalesInstitucionCcosto().iterator();
			contador=7;
			Boolean flag = false;
			while(iteradorInterno.hasNext())
			{
				flag = false;
				HashMap fila1=(HashMap)iteradorInterno.next();
				Iterator iteradorInterno2=signosVitales.getSignosVitalesParamHisto().iterator();
				while(iteradorInterno2.hasNext())
				{
					HashMap fila2=(HashMap)iteradorInterno2.next();
					if((fila3.get("codigo_histo_enfer")+"").equals(fila2.get("codigo_histo_enfer")+"")&&(fila1.get("codigo_tipo")+"").equals(fila2.get("codigo_tipo")+""))
					{
						flag = true;
						columnasDatos[contador]=fila2.get("valor_sig_vital")+"";
						contador++;
					}
				}
				
				if(!flag){
					columnasDatos[contador]=" ";
					contador++;
				}
			}
			
			columnasDatos[contador]=fila3.get("nombre_usuario")+"";
			
			
			dataSource.add(columnasDatos);
			
		
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
		
		reportTemplate = template();
		reportTemplate.setLocale(Locale.ENGLISH);
		//Se define el tamano de la hoja y la orientación de la misma
		reportTemplate.setPageFormat(PageType.LETTER, PageOrientation.PORTRAIT);
		reportTemplate.setColumnStyle(columnStyle);
		reportTemplate.setColumnTitleStyle(columnTitleStyle);
		
		return reportTemplate;
	}
	

	/**
	 * Creacion de margenes del reporte
	 * @return MarginBuilder
	 */
	public MarginBuilder crearMagenesReporte()
	{
		MarginBuilder margin;
		margin = margin()
		.setTop(1)
		.setBottom(30)
		.setLeft(0)
		.setRight(0)
		;

		return margin;
	}
}
