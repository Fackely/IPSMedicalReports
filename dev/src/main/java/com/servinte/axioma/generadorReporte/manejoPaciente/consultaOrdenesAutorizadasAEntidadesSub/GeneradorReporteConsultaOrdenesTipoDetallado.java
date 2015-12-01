package com.servinte.axioma.generadorReporte.manejoPaciente.consultaOrdenesAutorizadasAEntidadesSub;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.grp;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;
import static net.sf.dynamicreports.report.builder.DynamicReports.variable;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

import net.sf.dynamicreports.examples.DataSource;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.builder.MarginBuilder;
import net.sf.dynamicreports.report.builder.VariableBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.group.CustomGroupBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.Calculation;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import util.ConstantesBD;
import util.UtilidadTexto;
import util.reportes.dinamico.GeneradorReporteDinamico;

import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.manejoPaciente.DtoBusquedaTotalOrdenesAutorizadasEntSub;
import com.princetonsa.dto.manejoPaciente.DtoConsultaTotalOrdenesAutorizadasEntSub;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.princetonsa.sort.odontologia.SortGenerico;

public class GeneradorReporteConsultaOrdenesTipoDetallado extends GeneradorReporteDinamico{

	
	/** *Atributos utilizados para ajustar las columnas a sus totales */
	private int anchoColumnaTipos=200;
	private int anchoColumnaNivelAtencionEntidad=250;
	
	/** *Constantes del reporte */
	private final int tamanoLetrasColumnasCantidadValor=10;
	private final int tamanoCantidadesValores=10;
	private final int tamanoLetraReporte=10;
	
	private ArrayList<DtoIntegridadDominio> estadosAutoriz = null;
	private DetallesGeneralesFormatos detallesReporte;
	private ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub> listadoResultado;
	private DtoBusquedaTotalOrdenesAutorizadasEntSub filtroConsultaOrdenes;

	/**
	 * Constructor de la clase
	 */
	public GeneradorReporteConsultaOrdenesTipoDetallado() {  
    }  
	
	/**
	 * Constructor de la clase
	 */
	public GeneradorReporteConsultaOrdenesTipoDetallado(ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub> listadoResultado, 
			DtoBusquedaTotalOrdenesAutorizadasEntSub filtroConsultaOrdenes) {
			estadosAutoriz=filtroConsultaOrdenes.getNombresEstadosAutorizaciones();
			this.listadoResultado=listadoResultado;
			this.filtroConsultaOrdenes=filtroConsultaOrdenes;
			this.detallesReporte = new DetallesGeneralesFormatos(filtroConsultaOrdenes);
	}
      
	/**
     * Este método genera y configura el reporte general
     * @return report reporte general
     */
    @SuppressWarnings("unused")
	public JasperReportBuilder generarReporte() {
    	
    	  //---------ESTILOS UTILIZADOS EN EL REPORTE------------
		  StyleBuilder estiloLetraColumnasCantVal 	= stl.style().bold().setFontSize(tamanoLetrasColumnasCantidadValor).setHorizontalAlignment(HorizontalAlignment.CENTER);
		  StyleBuilder estiloCantidadesValores 		= stl.style().setFontSize(tamanoCantidadesValores);
		  StyleBuilder estiloTotalesCantVal 		= stl.style().bold().setFontSize(tamanoCantidadesValores);
		  StyleBuilder letraGeneral 				= stl.style().setFontSize(tamanoLetraReporte);
		  //------------------------------------------------------
		  
		  //---------DEFINICIÓN DE COLUMNAS-----------------------
		  TextColumnBuilder<String>     nivelAtencionColumna      			= col.column("Nivel de Atención", "nivelAtencion",  	 type.stringType());
	      TextColumnBuilder<String>     tipoColumna      					= col.column("tipo",       		  "tipo",      			 type.stringType());
	      TextColumnBuilder<String>     serviciosMedicamentosColumna      	= col.column("",       			  "servicioMedicamento", type.stringType());
	      //------------------------------------------------------
	      
	      //---------DEFINICIÓN DE LOS GRUPOS---------------------
	      CustomGroupBuilder grupoNivelAtencion= grp.group("nivelAtencion", "nivelAtencion", String.class).setTitle("Nivel de Atención: ").setTitleStyle(letraGeneral).setTitleWidth(100).setHorizontalAlignment(HorizontalAlignment.LEFT).setStyle(stl.style()).setPadding(0);
	      CustomGroupBuilder grupoTipo= grp.group("tipo", "tipo", String.class).setStyle(letraGeneral).setPadding(0);
	      //------------------------------------------------------
	      
	      //---------CREACION DE LISTAS CON TOTALES POR CADA GRUPO----------
	      HorizontalListBuilder listaSumaTipos = cmp.horizontalList();
	   	  listaSumaTipos.add(cmp.text("Total").setStyle(letraGeneral.bold()).setWidth(50));
	   	  
	   	  VariableBuilder<String> nombreTipo = variable(tipoColumna, Calculation.NOTHING);
	   	  nombreTipo.setResetGroup(grupoTipo);
	   	  TextFieldBuilder<String> groupNombreTipo = cmp.text(new CustomTextNombreTipo(nombreTipo));
	   	  listaSumaTipos.add(groupNombreTipo.setWidth(anchoColumnaTipos).setStyle(estiloTotalesCantVal));
	   	  
	   	  HorizontalListBuilder listaSumaNivelAtencion = cmp.horizontalList();
	      listaSumaNivelAtencion.add(cmp.text("Total Nivel Atención").setWidth(anchoColumnaNivelAtencionEntidad).setStyle(letraGeneral.bold()));
	      
	      HorizontalListBuilder listaSumaTotalesEntidad = cmp.horizontalList();
	      listaSumaTotalesEntidad.add(cmp.text("TOTAL ENTIDAD SUBCONTRATADA").setWidth(anchoColumnaNivelAtencionEntidad).setStyle(letraGeneral.bold()));
	      //-------------------------------------------------------------------
	      
	      
	      //----------REPORTE GENERAL
	   	  JasperReportBuilder report = report();
	   	  report.columns(serviciosMedicamentosColumna.setWidth(250));
	       	  
	   	  
	      for (int i = 1; i <= estadosAutoriz.size(); i++) {  
			  	   
       			TextColumnBuilder<Integer>    cantidadColumna 	=col.column("Cantidad",    "cantidad" + i, 	type.integerType()).setHorizontalAlignment(HorizontalAlignment.RIGHT).setWidth(60).setStyle(estiloCantidadesValores);
       			TextColumnBuilder<Integer>    espacioColumna 	=col.column(" ",           "espacio" + i, 	type.integerType()).setHorizontalAlignment(HorizontalAlignment.RIGHT).setWidth(20).setStyle(estiloCantidadesValores);
       			TextColumnBuilder<BigDecimal> valorColumna 		=col.column("      Valor", "valor" + i, 	type.bigDecimalType()).setHorizontalAlignment(HorizontalAlignment.RIGHT).setWidth(90).setStyle(estiloCantidadesValores);
		  	   	  
		  	   	  //---------TOTALES PARA GRUPO NIVEL ATENCION---------------
		       	  VariableBuilder<Integer> cantidadGrpSumNivelAtencion = variable(cantidadColumna, Calculation.SUM);
		       	  VariableBuilder<BigDecimal> valorGrpSumNivelAtencion = variable(valorColumna, Calculation.SUM);
		       	  cantidadGrpSumNivelAtencion.setResetGroup(grupoNivelAtencion);
		       	  valorGrpSumNivelAtencion.setResetGroup(grupoNivelAtencion);
	 	       	  TextFieldBuilder<String> groupSumaCantidadNivelAtencion = cmp.text(new CustomTextSubtotalCantidades(cantidadGrpSumNivelAtencion)).setStyle(letraGeneral);
	 	       	  TextFieldBuilder<String> groupSumaValorNivelAtencion = cmp.text(new CustomTextSubtotalValores(valorGrpSumNivelAtencion)).setStyle(letraGeneral);
	 	       	  
	 	       	  listaSumaNivelAtencion.add(groupSumaCantidadNivelAtencion.setHorizontalAlignment(HorizontalAlignment.RIGHT).setStyle(estiloTotalesCantVal).setWidth(60));
	 	       	  listaSumaNivelAtencion.add(cmp.text(" ").setHorizontalAlignment(HorizontalAlignment.RIGHT).setStyle(estiloTotalesCantVal).setWidth(20));
	 	       	  listaSumaNivelAtencion.add(groupSumaValorNivelAtencion.setHorizontalAlignment(HorizontalAlignment.RIGHT).setStyle(estiloTotalesCantVal).setWidth(90));
	 	       	  //---------------------------------------------------------
	 	       	  
	 	       	  //--------TOTALES PARA TIPO SERVICIO O MEDICAMENTO---------
	   	       	  VariableBuilder<Integer> cantidadGrpSumTipo = variable(cantidadColumna, Calculation.SUM);
	   	       	  VariableBuilder<BigDecimal> valorGrpSumTipo = variable(valorColumna, Calculation.SUM);
	   	       	  cantidadGrpSumTipo.setResetGroup(grupoTipo);
	   	       	  valorGrpSumTipo.setResetGroup(grupoTipo);
	   	       	  TextFieldBuilder<String> groupSumaCantidadTipo = cmp.text(new CustomTextSubtotalCantidades(cantidadGrpSumTipo)).setStyle(letraGeneral);
	   	       	  TextFieldBuilder<String> groupSumaValorTipo = cmp.text(new CustomTextSubtotalValores(valorGrpSumTipo)).setStyle(letraGeneral);
	   	       	  
	   	       	  listaSumaTipos.add(groupSumaCantidadTipo.setHorizontalAlignment(HorizontalAlignment.RIGHT).setStyle(estiloTotalesCantVal).setWidth(60));
	   	       	  listaSumaTipos.add(cmp.text(" ").setHorizontalAlignment(HorizontalAlignment.RIGHT).setStyle(estiloTotalesCantVal).setWidth(20));
	   	       	  listaSumaTipos.add(groupSumaValorTipo.setHorizontalAlignment(HorizontalAlignment.RIGHT).setStyle(estiloTotalesCantVal).setWidth(90));
	   	       	  //---------------------------------------------------------
	   	       	  
	   	       	  //--------TOTALES GENERALES--------------------------------
	   	       	  VariableBuilder<Integer> cantidadGrpSumTotales = variable(cantidadColumna, Calculation.SUM);
		       	  VariableBuilder<BigDecimal> valorGrpSumTotales = variable(valorColumna, Calculation.SUM);
	 	       	  TextFieldBuilder<String> groupSumaCantidadTotales = cmp.text(new CustomTextSubtotalCantidades(cantidadGrpSumTotales)).setStyle(letraGeneral);
	 	       	  TextFieldBuilder<String> groupSumaValorTotales = cmp.text(new CustomTextSubtotalValores(valorGrpSumTotales)).setStyle(letraGeneral);
	 	       	  
	 	       	  listaSumaTotalesEntidad.add(groupSumaCantidadTotales.setHorizontalAlignment(HorizontalAlignment.RIGHT).setStyle(estiloTotalesCantVal).setWidth(60));
	 	       	  listaSumaTotalesEntidad.add(cmp.text(" ").setHorizontalAlignment(HorizontalAlignment.RIGHT).setStyle(estiloTotalesCantVal).setWidth(20));
	 	       	  listaSumaTotalesEntidad.add(groupSumaValorTotales.setHorizontalAlignment(HorizontalAlignment.RIGHT).setStyle(estiloTotalesCantVal).setWidth(90));
	 	       	  //---------------------------------------------------------
	 	       	  
	 	       	report.variables(cantidadGrpSumNivelAtencion, valorGrpSumNivelAtencion,
	 	       					cantidadGrpSumTipo, nombreTipo, valorGrpSumTipo,
	 	       					cantidadGrpSumTotales,valorGrpSumTotales
        		  		   );
	 	       	
	 	       	report.columns(cantidadColumna,espacioColumna,valorColumna);
	        } 
	       	
	      	//------------AGREGAR LAS LISTAS AL FOOTER DE CADA GRUPO--------
	       	grupoNivelAtencion.addFooterComponent(listaSumaNivelAtencion, cmp.text("").setHeight(5));
	       	grupoTipo.addFooterComponent(listaSumaTipos, cmp.text("").setHeight(5));
	       	report.summary(cmp.text("").setHeight(5));
   	        report.summary(listaSumaTotalesEntidad);
	   	    
	       	//------------AGREGAR LOS GRUPOS AL REPORTE GENERAL-------------
	   	    report.addGroup(grupoNivelAtencion);
	   	    report.addGroup(grupoTipo);
	   	          
	   	    //------------CONFIGURACIÓN GENERAL DEL REPORTE-----------------
   	        report.setPageFormat(PageType.LETTER, PageOrientation.PORTRAIT);
   	        MarginBuilder margenes = margin().setBottom(30).setTop(40).setLeft(40).setRight(20);
   	        report.setPageMargin(margenes); 
   	        report.setColumnTitleStyle(estiloLetraColumnasCantVal);
   	        report.setColumnStyle(estiloCantidadesValores);
   	        
   	        report.highlightDetailEvenRows();
   	        //report.title(this.detallesReporte.crearEncabezadoReporteDetallado());
            report.setDataSource(crearDataSource());
            if(filtroConsultaOrdenes.getTipoSalida()== EnumTiposSalida.PDF.getCodigo())
    	    {
            	report.pageHeader(this.detallesReporte.crearEncabezadoReporteDetallado());
    	    	report.pageFooter(this.detallesReporte.crearPiePaginaReporte());
    	    }else
    	    {
    	    	report.setIgnorePagination(true);
    	    }
   	        return report;
   	   }  
   	        
    	private JRDataSource crearDataSource(){
    		
    		SortGenerico sortG=new SortGenerico("NivelAtencion",true);
    		Collections.sort(listadoResultado ,sortG);
    		
    		ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub> listaOrdenada= new ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub>();
    		
    		ArrayList<String> nivelesAtencion= new ArrayList<String>();
    		
    		for(DtoConsultaTotalOrdenesAutorizadasEntSub dtoConsulta:listadoResultado)
    		{
    			if(!nivelesAtencion.contains(dtoConsulta.getNivelAtencion()))
    			{
    				for(DtoConsultaTotalOrdenesAutorizadasEntSub dtoConsultaInterno:listadoResultado)
        			{
        				if(dtoConsultaInterno.getNivelAtencion().equals(dtoConsulta.getNivelAtencion())
        						&&dtoConsultaInterno.getCodigoServicio()!=null)
        				{
        					listaOrdenada.add(dtoConsultaInterno);
        				}
        						
        			}
        			for(DtoConsultaTotalOrdenesAutorizadasEntSub dtoConsultaInterno:listadoResultado)
        			{
        				if(dtoConsultaInterno.getNivelAtencion().equals(dtoConsulta.getNivelAtencion())
        						&&dtoConsultaInterno.getCodigoArticulo()!=null)
        				{
        					listaOrdenada.add(dtoConsultaInterno);
        				}
        						
        			}
        			nivelesAtencion.add(dtoConsulta.getNivelAtencion());
    			}
    			
    		}
    		
    		for(DtoConsultaTotalOrdenesAutorizadasEntSub dtoConsulta:listaOrdenada)
    		{
    			if(UtilidadTexto.isEmpty(dtoConsulta.getGrupoServicio()))
    			{
    				if(dtoConsulta.getCantidadArticulo()==null)
    				{
    					dtoConsulta.setCantidadArticulo(0);
    				}
    				if(dtoConsulta.getValorArticulo()==null)
    				{
    					dtoConsulta.setValorArticulo(new BigDecimal(0));
    				}
    				
    			}else
    			{
    				if(dtoConsulta.getCantidadServicio()==null)
    				{
    					dtoConsulta.setCantidadServicio(0);
    				}
    				if(dtoConsulta.getValorServicio()==null)
    				{
    					dtoConsulta.setValorServicio(new BigDecimal(0));
    				}
    			}
    		}
    		
    		ArrayList<String> nivelServicio= new ArrayList<String>();
    		ArrayList<String> nivelArticulo= new ArrayList<String>();
    		ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub> listaDefinitiva= new ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub>();
    		
    		for(int i=0;i<listaOrdenada.size();i++)
    		{
    			if(UtilidadTexto.isEmpty(listaOrdenada.get(i).getGrupoServicio()))
    			{
    				if(!nivelArticulo.contains(listaOrdenada.get(i).getNivelAtencion()+ConstantesBD.separadorSplit+
    						listaOrdenada.get(i).getCodigoArticulo()+ConstantesBD.separadorSplit+
    						listaOrdenada.get(i).getEstadoAutorizacion()))
    				{
        				for(int j=i+1;j<listaOrdenada.size();j++)
            			{
        					if(listaOrdenada.get(j).getCodigoArticulo()!=null)
        					{
        						if(listaOrdenada.get(j).getNivelAtencion().equals(listaOrdenada.get(i).getNivelAtencion())
        	    						&&listaOrdenada.get(j).getCodigoArticulo().equals(listaOrdenada.get(i).getCodigoArticulo())
        	    						&&listaOrdenada.get(j).getEstadoAutorizacion().equals(listaOrdenada.get(i).getEstadoAutorizacion())
        	    						)
            					{
        							listaOrdenada.get(j).setConsecutivo(null);
        							listaOrdenada.get(i).setCantidadArticulo(listaOrdenada.get(i).getCantidadArticulo()+listaOrdenada.get(j).getCantidadArticulo());
        							listaOrdenada.get(i).setValorArticulo(listaOrdenada.get(i).getValorArticulo().add(listaOrdenada.get(j).getValorArticulo()));
            						
            						nivelArticulo.add(listaOrdenada.get(j).getNivelAtencion()+ConstantesBD.separadorSplit+
            								listaOrdenada.get(j).getCodigoArticulo()+ConstantesBD.separadorSplit+
            								listaOrdenada.get(j).getEstadoAutorizacion());
            					}
        					}
            			}
    				}
    			}else
    			{
    				if(!nivelServicio.contains(listaOrdenada.get(i).getNivelAtencion()+ConstantesBD.separadorSplit+
    						listaOrdenada.get(i).getCodigoServicio()+ConstantesBD.separadorSplit+
    						listaOrdenada.get(i).getEstadoAutorizacion()))
    				{
        				for(int j=i+1;j<listaOrdenada.size();j++)
            			{
        					if(listaOrdenada.get(j).getCodigoServicio()!=null)
        					{
        						if(listaOrdenada.get(j).getNivelAtencion().equals(listaOrdenada.get(i).getNivelAtencion())
        	    						&&listaOrdenada.get(j).getCodigoServicio().equals(listaOrdenada.get(i).getCodigoServicio())
        	    						&&listaOrdenada.get(j).getEstadoAutorizacion().equals(listaOrdenada.get(i).getEstadoAutorizacion())
        	    						)
            					{
        							listaOrdenada.get(j).setConsecutivo(null);
        							listaOrdenada.get(i).setCantidadServicio(listaOrdenada.get(i).getCantidadServicio()+listaOrdenada.get(j).getCantidadServicio());
        							listaOrdenada.get(i).setValorServicio(listaOrdenada.get(i).getValorServicio().add(listaOrdenada.get(j).getValorServicio()));
            						
            						nivelServicio.add(listaOrdenada.get(j).getNivelAtencion()+ConstantesBD.separadorSplit+
            								listaOrdenada.get(j).getCodigoServicio()+ConstantesBD.separadorSplit+
            								listaOrdenada.get(j).getEstadoAutorizacion());
            					}
        					}
            			}
    				}
    			}
    		}
    		
    		for(DtoConsultaTotalOrdenesAutorizadasEntSub dtoConsulta:listaOrdenada)
    		{
    			if(dtoConsulta.getConsecutivo()!=null)
    				listaDefinitiva.add(dtoConsulta);
    		}
    		
    		
    		int columnasTotales=(estadosAutoriz.size()*2)+3;
     		String[] columnas = new String[columnasTotales];
     		columnas[0]="nivelAtencion";
     		columnas[1]="tipo";
     		columnas[2]="servicioMedicamento";
     		
     		int contador=3;
     		for(int i=1 ; i<=estadosAutoriz.size() ; i++)
     		{
     			columnas[contador] = "cantidad" + i;
     			columnas[contador+1] = "valor" + i;
     			contador+=2;
     		}
     		DataSource dataSource = new DataSource(columnas);
     		
    		ArrayList<Object> totales= new ArrayList<Object>();
    		ArrayList<String> nivelS= new ArrayList<String>();
    		ArrayList<String> nivelM= new ArrayList<String>();
    		
    		for(DtoConsultaTotalOrdenesAutorizadasEntSub dtoConsulta:listaDefinitiva)
    		{
    			if(dtoConsulta.getCodigoArticulo()!=null)
    			{
    				totales= new ArrayList<Object>();
        			if(!nivelM.contains(dtoConsulta.getNivelAtencion()+ConstantesBD.separadorSplit+
    						dtoConsulta.getCodigoArticulo()))
    				{
    	    			for(int i=0;i<filtroConsultaOrdenes.getNombresEstadosAutorizaciones().size();i++)
    	    			{
    	    				int totalSize=totales.size();
    	    				for(DtoConsultaTotalOrdenesAutorizadasEntSub dtoConsultaInterno:listaDefinitiva)
    		    			{
    	    					if(dtoConsultaInterno.getCodigoArticulo()!=null)
    	    					{
    	    						if(dtoConsultaInterno.getNivelAtencion().equals(dtoConsulta.getNivelAtencion()))
    			    				{
    			    						
    			    					if(dtoConsultaInterno.getEstadoAutorizacion().equals(filtroConsultaOrdenes.getNombresEstadosAutorizaciones().get(i).getAcronimo())
    			    							&&dtoConsultaInterno.getCodigoArticulo().equals(dtoConsulta.getCodigoArticulo()))
    			    					{
    			    						totales.add((dtoConsultaInterno.getCantidadArticulo()!=null?dtoConsultaInterno.getCantidadArticulo():0));
    			    						totales.add((dtoConsultaInterno.getValorArticulo()!=null?dtoConsultaInterno.getValorArticulo():new BigDecimal(0.0)));
    			    						nivelM.add(dtoConsultaInterno.getNivelAtencion()+ConstantesBD.separadorSplit+
    											     dtoConsultaInterno.getCodigoArticulo());
    			    					}
    			    				}
    	    					}
    		    			}
    	    				if(totales.size()==totalSize)
    	    				{
    	    					totales.add(0);
    	    					totales.add(new BigDecimal(0.0));
    	    					nivelM.add(dtoConsulta.getNivelAtencion()+ConstantesBD.separadorSplit+
    									dtoConsulta.getCodigoArticulo());
    	    				}
        				}
        			}
    	    			
        			if(!totales.isEmpty()){
    	    			Object[] datos= new Object[columnasTotales];
    	    			datos[0]=dtoConsulta.getNivelAtencion();
    	    			datos[1]="Medicamentos / Insumos";
    	    			datos[2]=dtoConsulta.getNombreArticulo();
    	    			contador=3;
    	    			for(int i =0;i<totales.size();i++){
    	    				datos[contador]=totales.get(i);
    	    				contador++;
    	    			}
    	    			
    	    			dataSource.add(datos);
        			}
    			}
    			else
    			{
    				totales= new ArrayList<Object>();
        			if(!nivelS.contains(dtoConsulta.getNivelAtencion()+ConstantesBD.separadorSplit+
    						dtoConsulta.getCodigoServicio()))
    				{
    	    			for(int i=0;i<filtroConsultaOrdenes.getNombresEstadosAutorizaciones().size();i++)
    	    			{
    	    				int totalSize=totales.size();
    	    				for(DtoConsultaTotalOrdenesAutorizadasEntSub dtoConsultaInterno:listaDefinitiva)
    		    			{
    	    					if(dtoConsultaInterno.getCodigoServicio()!=null)
    	    					{
    	    						if(dtoConsultaInterno.getNivelAtencion().equals(dtoConsulta.getNivelAtencion()))
    	    						{
				    					if(dtoConsultaInterno.getEstadoAutorizacion().equals(filtroConsultaOrdenes.getNombresEstadosAutorizaciones().get(i).getAcronimo())
				    							&&dtoConsultaInterno.getCodigoServicio().equals(dtoConsulta.getCodigoServicio()))
				    					{
				    						totales.add((dtoConsultaInterno.getCantidadServicio()!=null?dtoConsultaInterno.getCantidadServicio():0));
				    						totales.add((dtoConsultaInterno.getValorServicio()!=null?dtoConsultaInterno.getValorServicio():new BigDecimal(0.0)));
				    						nivelS.add(dtoConsultaInterno.getNivelAtencion()+ConstantesBD.separadorSplit+
												     dtoConsultaInterno.getCodigoServicio());
				    					}
    	    						}
    	    					}
    		    			}
    	    				if(totales.size()==totalSize)
    	    				{
    	    					totales.add(0);
    	    					totales.add(new BigDecimal(0.0));
    	    					nivelS.add(dtoConsulta.getNivelAtencion()+ConstantesBD.separadorSplit+
    									dtoConsulta.getCodigoServicio());
    	    				}
        				}
        			}
    	    			
        			if(!totales.isEmpty()){
    	    			Object[] datos= new Object[columnasTotales];
    	    			datos[0]=dtoConsulta.getNivelAtencion();
    	    			datos[1]="Servicios";
    	    			datos[2]=dtoConsulta.getNombreServicio();
    	    			contador=3;
    	    			for(int i =0;i<totales.size();i++){
    	    				datos[contador]=totales.get(i);
    	    				contador++;
    	    			}
    	    			
    	    			dataSource.add(datos);
        			}
    			}
    			
    		}
    		
    		
    		/*DataSource dataSource = new DataSource("nivelAtencion","tipo","servicioMedicamento","cantidad1","valor1","cantidad2","valor2","cantidad3","valor3");
			dataSource.add("Nivel Atención I","Servicios","Consulta  odontología", 1, new BigDecimal(100000), 1, new BigDecimal(10000), 1, new BigDecimal(100000));
			dataSource.add("Nivel Atención I","Servicios","Radiografía cuello", 2, new BigDecimal(20000), 1, new BigDecimal(10000), 1, new BigDecimal(1000));
			dataSource.add("Nivel Atención I","Servicios","Consulta medicina general", 3, new BigDecimal(300000), 1, new BigDecimal(10000), 1, new BigDecimal(100));
	         dataSource.add("Nivel Atención I","Medicamentos / insumos","Aguja desechable",  4, new BigDecimal(4000), 1, new BigDecimal(10000), 1, new BigDecimal(100));
	         dataSource.add("Nivel Atención II","Servicios","Radiografía cuello", 5, new BigDecimal(50000), 1, new BigDecimal(100), 1, new BigDecimal(100));
	         dataSource.add("Nivel Atención II","Medicamentos / insumos","Ibuprofeno",  7, new BigDecimal(70000), 1, new BigDecimal(10000), 1, new BigDecimal(100));*/
			
			return dataSource;
    	}
   	   
    	/**
    	 * Esta clase ayuda a calcular los totales de los valores 
    	 * almacenados en variables del reporte
    	 * @author Fabián Becerra
    	 */
    	private class CustomTextSubtotalValores extends AbstractSimpleExpression<String> {
    		private static final long serialVersionUID = 1L;
    		private VariableBuilder<BigDecimal> valorSum;
    		public CustomTextSubtotalValores(VariableBuilder<BigDecimal> valorSum) {
    			this.valorSum = valorSum;
    		}
    		public String evaluate(ReportParameters reportParameters) {
    			BigDecimal valorSumValue = reportParameters.getValue(valorSum);
    			return type.bigDecimalType().valueToString(valorSumValue, reportParameters.getLocale());			       
    		}		
    	}
        
    	/**
    	 * Esta clase ayuda a calcular los totales de las cantidades
    	 * almacenadas en variables del reporte
    	 * @author Fabián Becerra
    	 */
        private class CustomTextSubtotalCantidades extends AbstractSimpleExpression<String> {
    		private static final long serialVersionUID = 1L;
    		private VariableBuilder<Integer> cantidadSum;
    		public CustomTextSubtotalCantidades(VariableBuilder<Integer> cantidadSum) {
    			this.cantidadSum = cantidadSum;
    		}
    		public String evaluate(ReportParameters reportParameters) {
    			Integer cantidadSumValue = reportParameters.getValue(cantidadSum);
    			return type.integerType().valueToString(cantidadSumValue, reportParameters.getLocale());			       
    		}		
    	}    
        
        /**
    	 * Esta clase ayuda a retornar el nombre del tipo (Servicio o Medicamento)
    	 * para ser utilizado en el nombre de los totales
    	 * @author Fabián Becerra
    	 */
        private class CustomTextNombreTipo extends AbstractSimpleExpression<String> {
    		private static final long serialVersionUID = 1L;
    		private VariableBuilder<String> nombreTipo;
    		public CustomTextNombreTipo(VariableBuilder<String> nombreTipo) {
    			this.nombreTipo = nombreTipo;
    		}
    		public String evaluate(ReportParameters reportParameters) {
    			String valorNombreTipo = reportParameters.getValue(nombreTipo);
    			return type.stringType().valueToString(valorNombreTipo, reportParameters.getLocale()).toLowerCase();			       
    		}		
    	}
    
}
