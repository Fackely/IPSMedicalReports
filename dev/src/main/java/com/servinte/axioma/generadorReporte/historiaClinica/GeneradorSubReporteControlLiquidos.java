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

public class GeneradorSubReporteControlLiquidos 
{
	public JasperReportBuilder generarReporte(DtoImpresionHistoriaClinica dto) 
	{
		JasperReportBuilder report = report();
		report.setTemplate(crearPlantillaReporte());
		
		 
		report.columns(crearEncabezado(dto));
		report.setDataSource(createDataSource(dto));
		report.title(cmp.text("Control de Liquidos")
				.setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloTituloUnderline).underline().setHorizontalAlignment(HorizontalAlignment.LEFT)));
		
		report
		.setDetailSplitType(SplitType.PREVENT)
		.summary(cmp.subreport(crearSubreporteDetalleControlLiquidos(dto)));
		
		return report;
	}
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	private JasperReportBuilder crearSubreporteDetalleControlLiquidos(DtoImpresionHistoriaClinica dto) 
	{
		JasperReportBuilder report = report();
		report.setTemplate(crearPlantillaReporte());
		report.setPageMargin(crearMagenesSubReporte());
		
		TextColumnBuilder[] cols=crearEncabezadoDetalle(dto);
		report.columns(cols);
		report.setDataSource(createDataSourceDetalle(cols,dto));
		
		return report;
	}
	
	/**
	 * 
	 * @param cols
	 * @param dto
	 * @return
	 */
	private JRDataSource createDataSourceDetalle(TextColumnBuilder[] cols,DtoImpresionHistoriaClinica dto) 
	{
		String[] columnaDatos=new String[cols.length];
		for(int i=0;i<cols.length;i++)
		{
			columnaDatos[i]=cols[i].getName();
		}
		
		Object[] datosReporte=new Object[cols.length];
		
		
		DataSource dataSource = new DataSource(columnaDatos);
		
		float suma = 0;	String  codEnca = "",  codEncaAux = "",  fecha="",  fechaAux="",  cad="",  tipoLiquido = ""; 
		
		HashMap mapaControlLiq=dto.getControlLiquidos();
		int nroRegLiqAdm = Utilidades.convertirAEntero( mapaControlLiq.get("nroRegMedAdm")+"",true);
		int nroRegLiqElim = Utilidades.convertirAEntero( mapaControlLiq.get("nroRegMedElim")+"",true);
		int nroRegBalLiqAdm = Utilidades.convertirAEntero( mapaControlLiq.get("nroRegBalLiqAdm")+"",true);
		int nroRegBalLiqElim = Utilidades.convertirAEntero( mapaControlLiq.get("nroRegBalLiqElim")+"",true);
		float sumaAdm = 0, sumaAcumulado=0;
		String fechaR = "", horaR = "", medicoR="";
		boolean estaFinalizadoTurno = false;
		if (nroRegBalLiqAdm > 0)
		{
			codEnca = "";  codEncaAux = "";  fecha="";  fechaAux="";  cad="";  tipoLiquido="";
			suma = 0;
			for(int i = 0;  i<nroRegBalLiqAdm;  i++) //---Barrer todos los registros de los historicos
			{
				int posDato=0;
				codEncaAux = mapaControlLiq.get("codencabezado_" + i) + "";	
				fechaAux = mapaControlLiq.get("fecha_registro_" + i) + "  " +  mapaControlLiq.get("hora_registro_" + i);
			    fechaR = mapaControlLiq.get("fecha_grabacion_" + i) + "";	
			    horaR = mapaControlLiq.get("hora_grabacion_" + i) + "";	
			    medicoR = mapaControlLiq.get("medico_" + i) + "";
			    if ( !codEnca.equals(codEncaAux)  )
			    {
			    	estaFinalizadoTurno = ( Utilidades.convertirAEntero(codEncaAux) <0 );
			    	
			    	datosReporte[posDato]=fechaAux;
			    	posDato++;
			    	
			    	int ban = 0; boolean entro2 = false;
			    	
			    	for(int j=0; j<nroRegLiqAdm; j++)
			    	{
			    		String reg = mapaControlLiq.get("codigo_" + j) + "_" +  mapaControlLiq.get("param_" + j)  + "_" + mapaControlLiq.get("tipoliquido_" + j)+ "_" + mapaControlLiq.get("codmezcla_" + j); 	
			    		String tpLiq = mapaControlLiq.get("tipoliquido_" + j) + "";
			    		boolean entro = false;
			    		for(int k = 0;  k<nroRegBalLiqAdm; k++)
			    		{
			    			String reg2 = mapaControlLiq.get("codigomed_" + k) + "_" +  mapaControlLiq.get("parammed_" + k) + "_" + mapaControlLiq.get("tipoliquidomed_" +k) + "_" + mapaControlLiq.get("codmezclamed_" + k); 
							  String tpLiq2 = mapaControlLiq.get("tipoliquidomed_" + k) + "";
							  String valormed = mapaControlLiq.get("valormed_" + k) + "";
							  String ce = mapaControlLiq.get("codencabezado_" + k) + "";
							  if ( (ban==0) && util.UtilidadCadena.noEsVacio(valormed) && ce.equals(codEncaAux)  && tpLiq2.equals(tpLiq)  )  
							  { 
								  entro2=true; 
								  suma+= Float.parseFloat(valormed);  
							  } 
						  	  if ( reg.equals(reg2) && ce.equals(codEncaAux)  ) 
						  	  { 
						  		  entro = true;
						  		  datosReporte[posDato]=valormed;
						  		  posDato++;						  		  
						  	  }
			    		}
			    		ban++;
			    		if (!entro) 
			    		{
			    			datosReporte[posDato]="";
			    			posDato++;		
			    		}
			    	}
			    	if (entro2) 
			    	{
			    		datosReporte[posDato]=suma+"";
		    			posDato++;
			    	}
			    	else
			    	{
			    		datosReporte[posDato]="";
		    			posDato++;		
			    	}
			    	sumaAdm = suma; 
			    	suma = 0;
			    	
			    	ban = 0; entro2 = false; suma = 0;
			    	for(int j = 0; j<nroRegLiqElim; j++)
			    	{
			    		String reg = mapaControlLiq.get("codigoelim_" + j) + "_" +  mapaControlLiq.get("paramelim_" + j)  + "_" + mapaControlLiq.get("tipoliquidoelim_" + j) ; 	
			  		    String tpLiq = mapaControlLiq.get("tipoliquidoelim_" + j) + "";	
			  		    boolean entro = false;
				  		for(int k = 0;  k<nroRegBalLiqAdm; k++)
				  		{
				  			String reg2 = mapaControlLiq.get("codigomed_" + k) + "_" +  mapaControlLiq.get("parammed_" + k) + "_" + mapaControlLiq.get("tipoliquidomed_" +k) ;
				  			String tpLiq2 = mapaControlLiq.get("tipoliquidomed_" + k) + "";
				  			String valormed = mapaControlLiq.get("valormed_" + k) + "";
				  			String ce = mapaControlLiq.get("codencabezado_" + k) + "";
				  			if ( (ban==0) && util.UtilidadCadena.noEsVacio(valormed) && ce.equals(codEncaAux)  && tpLiq2.equals(tpLiq)  )  
				  			{ 
				  				entro2=true; 
				  				suma+= Float.parseFloat(valormed);  
				  			}
				  			if ( reg.equals(reg2) && ce.equals(codEncaAux)  ) 
				  			{ 
				  				entro = true;
				  					
				  				datosReporte[posDato]=valormed+"";
				    			posDato++;
				  			}
				  		}
			  			ban++;
			  			if (!entro) 
			  			{
			  				datosReporte[posDato]="";
			    			posDato++;
			  			}
			  		}
			    	if (entro2) 
			    	{
			    		datosReporte[posDato]=suma+"";
		    			posDato++;
			    	}
			    	else
			    	{
			    		datosReporte[posDato]="";
		    			posDato++;		
			    	}
			    	String cd = util.UtilidadTexto.formatearValores(sumaAdm - suma);
			    	if (  Utilidades.convertirAEntero(codEncaAux) > 0 )
			    	{
			    		sumaAcumulado = sumaAcumulado + (sumaAdm - suma);
                    }
			    	datosReporte[posDato]=cd+"";
	    			posDato++;	
	    			
	    			datosReporte[posDato]=util.UtilidadTexto.formatearValores(sumaAcumulado)+"";
	    			posDato++;	
	    			suma=0;
	    			dataSource.add(datosReporte);
			    }	
			    if ( Utilidades.convertirAEntero(codEncaAux) < 0 ) 
			    { 
			    	sumaAcumulado=0; 
			    }
			    codEnca = codEncaAux;
				fecha = fechaAux; 
			} 
			
		}
		
		return dataSource;
	}


	/**
	 * 
	 * @param dto
	 * @return
	 */
	private TextColumnBuilder[] crearEncabezadoDetalle(DtoImpresionHistoriaClinica dto) 
	{
		
		HashMap mapaControlLiq=dto.getControlLiquidos();
		int nroRegLiqAdm = Utilidades.convertirAEntero( mapaControlLiq.get("nroRegMedAdm")+"",true);
		int nroRegLiqElim = Utilidades.convertirAEntero( mapaControlLiq.get("nroRegMedElim")+"",true);
		int nroRegBalLiqAdm = Utilidades.convertirAEntero( mapaControlLiq.get("nroRegBalLiqAdm")+"",true);
		int nroRegBalLiqElim = Utilidades.convertirAEntero( mapaControlLiq.get("nroRegBalLiqElim")+"",true);
		
		ArrayList<TextColumnBuilder> colsArray=new ArrayList<TextColumnBuilder>();
		
		TextColumnBuilder<String> columna  = col.column("Fecha/Hora.","fechahora",type.stringType()).setStyle(stl.style().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK))).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		colsArray.add(columna);
		
		if(nroRegLiqAdm>0)
		{
			int nroMz=0;
			for(int j=0; j<nroRegLiqAdm; j++)
			{
				boolean  esMezcla = util.UtilidadTexto.getBoolean(mapaControlLiq.get("esmezcla_"+j)+ "");
 			   	String cadMz = "";
 			   	String cadMzTempo = "";
 			   	if (!esMezcla) 
 			   	{ 
 			   		cadMz = "Liq. ";
 			   		cadMzTempo="Liq.";
 			   	}
 			   	else 
 			   	{ 
 			   		cadMz = "Mz. "+ ++nroMz; 
 			   		cadMzTempo="Mz."+ ++nroMz; ;
 			   	}
 			   	
		   		String tempo="";
		   		String tempo1="";
		   		if ((mapaControlLiq.get("param_" + j)+"").equals("0") ) 
		   		{
					if (!esMezcla) 
					{
						tempo=cadMz+" "+mapaControlLiq.get("codmedpaciente_" + j);
						tempo1=cadMzTempo+(mapaControlLiq.get("codmedpaciente_" + j)+"").trim();
					}
         			else 
         			{
         				tempo=cadMz;
         				tempo1=cadMzTempo.trim();
         			}
		   		}
		   		else
		   		{
		   			tempo=mapaControlLiq.get("nombre_" + j)+"";
		   			tempo1=(mapaControlLiq.get("nombre_" + j)+"").trim();
		   		}
				columna  = col.column(tempo,tempo1.trim(),type.stringType()).setStyle(stl.style().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK))).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
				colsArray.add(columna);
			}
			
		}
		columna  = col.column("Total Liq. Admin","totalliqadmin",type.stringType()).setStyle(stl.style().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK))).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		colsArray.add(columna);
		
		if(nroRegLiqElim>0)
		{
			for(int j=0; j<nroRegLiqElim; j++)
			{
				String tempo=mapaControlLiq.get("nombreelim_" + j)+"";
				String tempo1=(mapaControlLiq.get("nombreelim_" + j)+"").trim();
				columna  = col.column(tempo,tempo1,type.stringType()).setStyle(stl.style().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK))).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
				colsArray.add(columna);
				
			}
		}
		columna  = col.column("Total Liq. Elim","totalliqelim",type.stringType()).setStyle(stl.style().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK))).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		colsArray.add(columna);
		
		columna  = col.column("Balance de Líquidos","balanceliq",type.stringType()).setStyle(stl.style().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK))).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		colsArray.add(columna);
		
		columna  = col.column("Balance Acumulado de Líquidos","balacumliq",type.stringType()).setStyle(stl.style().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK))).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		colsArray.add(columna);
		
		TextColumnBuilder []cols = new TextColumnBuilder[colsArray.size()];
		int pos=0;
		
		for(TextColumnBuilder column:colsArray)
		{
			cols[pos]=column;
			pos++;
			
			
		}

		return cols;
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
	
	public MarginBuilder crearMagenesReporte()
	 {
	  MarginBuilder margin;
	  margin = margin()
	  .setTop(1)
	  .setBottom(30)
	  .setLeft(1)
	  .setRight(1)
	  ;

	  return margin;
	 }
	
	public MarginBuilder crearMagenesSubReporte()
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
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	private TextColumnBuilder[] crearEncabezado(DtoImpresionHistoriaClinica dto) 
	{
		TextColumnBuilder []cols = new TextColumnBuilder[4];
		
		TextColumnBuilder<String> columna  = col.column("Nro.","nro",type.stringType()).setStyle(stl.style().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK))).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		cols[0]=columna;
		
		columna  = col.column("Descripcion","descripcion",type.stringType()).setStyle(stl.style().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK))).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		cols[1]=columna;
		
		columna  = col.column("Volumen Total","voltotal",type.stringType()).setStyle(stl.style().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK))).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		cols[2]=columna;
				
		columna  = col.column("Vel Infusión","velinfusion",type.stringType()).setStyle(stl.style().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK))).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		cols[3]=columna;
				
		return cols;
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	private JRDataSource createDataSource(DtoImpresionHistoriaClinica dto) 
	{
		DataSource dataSource = new DataSource("nro", "descripcion", "valtotal", "velinfusion");
		HashMap mapaControlLiq=dto.getControlLiquidos();
		int nroRegMedNoParam = 0;
		if ( util.UtilidadCadena.noEsVacio(mapaControlLiq.get("numRegistros")+"") )
		{
			nroRegMedNoParam = Utilidades.convertirAEntero(mapaControlLiq.get("numRegistros")+"");
		}
		if(nroRegMedNoParam > 0)
		{
			int nroMz=0;
			for(int j = 0; j<nroRegMedNoParam; j++)
			{
				String des=mapaControlLiq.get("descripcion_"+ j)+"";
			    String vol=mapaControlLiq.get("volumen_total_"+ j)+"";
			    String vel=mapaControlLiq.get("velocidad_infusion_"+ j)+"";
			    String sus=mapaControlLiq.get("suspender_"+ j)+"";
			    String consecutivo =mapaControlLiq.get("consecutivo_liquido_"+ j)+"";
			    String cons = mapaControlLiq.get("consecutivo_liquido_" + j )+"";
			    boolean pendiente = util.UtilidadTexto.getBoolean(mapaControlLiq.get("suspendido_" + j )+"");
			    
			    String strPendiente = "";
			    
			    String[] datosReporte=new String[4];
			    if (!cons.equals("-1")) 
			    {
			    	datosReporte[0]=consecutivo+"";
			    }
			    else
			    {
			    	datosReporte[0]=strPendiente+"";
			    }
			    datosReporte[1]=des+"";
			    datosReporte[2]=vol+" ml";
			    datosReporte[3]=vel+" ml/h";
			    dataSource.add(datosReporte);
			}			
		}
		
		return dataSource;
	}


}
