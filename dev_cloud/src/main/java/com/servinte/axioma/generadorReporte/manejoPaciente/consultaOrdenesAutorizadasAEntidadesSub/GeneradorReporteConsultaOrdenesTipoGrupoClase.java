package com.servinte.axioma.generadorReporte.manejoPaciente.consultaOrdenesAutorizadasAEntidadesSub;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.grp;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;
import static net.sf.dynamicreports.report.builder.DynamicReports.variable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

import org.axioma.util.log.Log4JManager;

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
import net.sf.jasperreports.engine.JRDataSource;
import util.ConstantesBD;
import util.UtilidadTexto;
import util.reportes.dinamico.GeneradorReporteDinamico;

import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.manejoPaciente.DtoBusquedaTotalOrdenesAutorizadasEntSub;
import com.princetonsa.dto.manejoPaciente.DtoConsultaTotalOrdenesAutorizadasEntSub;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.princetonsa.sort.odontologia.SortGenerico;


public class GeneradorReporteConsultaOrdenesTipoGrupoClase extends GeneradorReporteDinamico{

	/** *Atributos utilizados para ajustar las columnas a sus totales */
	private int anchoColumnaTipos=150;
	private int anchoColumnaNivelAtencion=200;
	private int anchoColumnaConvenioEntidad=200;
	
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
	public GeneradorReporteConsultaOrdenesTipoGrupoClase() {  
    }  
      
	/**
	 * Constructor de la clase
	 */
	public GeneradorReporteConsultaOrdenesTipoGrupoClase(ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub> listadoResultado, 
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
		  TextColumnBuilder<String>     convenioColumna      	= col.column("Convenio",       	   	"convenio",      	type.stringType());
	      TextColumnBuilder<String>     nivelAtencionColumna    = col.column("Nivel de Atención",  	"nivelAtencion",    type.stringType());
	      TextColumnBuilder<String>     tipoColumna      		= col.column("tipo",       		   	"tipo",      		type.stringType());
	      TextColumnBuilder<String>     grupoClaseColumna      	= col.column("", 					"grupoClase",      	type.stringType());
		  //------------------------------------------------------
		  
	      //---------DEFINICIÓN DE LOS GRUPOS---------------------
   	      CustomGroupBuilder grupoConvenio= grp.group("convenio", "convenio", String.class).setTitle("Convenio: ").setTitleStyle(letraGeneral).setTitleWidth(60).setHorizontalAlignment(HorizontalAlignment.LEFT).setStyle(stl.style()).setPadding(0);
   	      CustomGroupBuilder grupoNivelAtencion= grp.group("nivelAtencion", "nivelAtencion", String.class).setTitle("Nivel de Atención: ").setTitleStyle(letraGeneral).setTitleWidth(100).setHorizontalAlignment(HorizontalAlignment.LEFT).setStyle(stl.style()).setPadding(0);
   	      CustomGroupBuilder grupoTipo= grp.group("tipo", "tipo", String.class).setStyle(letraGeneral).setPadding(0);
   	      //------------------------------------------------------
   	      
   	      //---------CREACION DE LISTAS CON TOTALES POR CADA GRUPO----------
          HorizontalListBuilder listaSumaTipos = cmp.horizontalList();
       	  listaSumaTipos.add(cmp.text("Total").setStyle(letraGeneral.bold()).setWidth(50));
       	  
       	  VariableBuilder<String> nombreTipo = variable(tipoColumna, Calculation.NOTHING);
       	  nombreTipo.setResetGroup(grupoTipo);
       	  TextFieldBuilder<String> groupNombreTipo = cmp.text(new CustomTextNombreTipo(nombreTipo));
       	  listaSumaTipos.add(groupNombreTipo.setWidth(anchoColumnaTipos).setStyle(letraGeneral.bold())); 
       	  
       	  HorizontalListBuilder listaSumaNivelAtencion = cmp.horizontalList();
          listaSumaNivelAtencion.add(cmp.text("Total Nivel Atención").setWidth(anchoColumnaNivelAtencion).setStyle(letraGeneral.bold()));
          
          HorizontalListBuilder listaSumaConvenio = cmp.horizontalList();
          listaSumaConvenio.add(cmp.text("TOTAL CONVENIO").setWidth(anchoColumnaConvenioEntidad).setStyle(letraGeneral.bold()));
          
          HorizontalListBuilder listaSumaTotalesEntidad = cmp.horizontalList();
          listaSumaTotalesEntidad.add(cmp.text("TOTAL ENTIDAD SUBCONTRATADA").setWidth(anchoColumnaConvenioEntidad).setStyle(letraGeneral.bold()));
          //------------------------------------------------------
          
          //----------REPORTE GENERAL
       	  JasperReportBuilder report = report();
       	  report.columns(grupoClaseColumna.setWidth(200));
	       	  
	      for (int i = 1; i <= estadosAutoriz.size(); i++) {  
			  	   
       			TextColumnBuilder<Integer>    cantidadColumna =col.column(" Cantidad", "cantidad" + i, type.integerType()).setHorizontalAlignment(HorizontalAlignment.RIGHT).setWidth(60).setStyle(estiloCantidadesValores);
       			TextColumnBuilder<Integer>    espacioColumna 	=col.column(" ",           "espacio" + i, 	type.integerType()).setHorizontalAlignment(HorizontalAlignment.RIGHT).setWidth(20).setStyle(estiloCantidadesValores);
       			TextColumnBuilder<BigDecimal> valorColumna = col.column("      Valor", "valor" + i, type.bigDecimalType()).setHorizontalAlignment(HorizontalAlignment.RIGHT).setWidth(90).setStyle(estiloCantidadesValores);
		  	   	  
       			  //--------TOTALES PARA TIPO
	   	       	  VariableBuilder<Integer> cantidadGrpSumTipo = variable(cantidadColumna, Calculation.SUM);
	   	       	  VariableBuilder<BigDecimal> valorGrpSumTipo = variable(valorColumna, Calculation.SUM);
	   	       	  cantidadGrpSumTipo.setResetGroup(grupoTipo);
	   	       	  valorGrpSumTipo.setResetGroup(grupoTipo);
	   	       	  TextFieldBuilder<String> groupSumaCantidadTipo = cmp.text(new CustomTextSubtotalCantidades(cantidadGrpSumTipo)).setStyle(letraGeneral);
	   	       	  TextFieldBuilder<String> groupSumaValorTipo = cmp.text(new CustomTextSubtotalValores(valorGrpSumTipo)).setStyle(letraGeneral);
	   	       	  
	   	       	  listaSumaTipos.add(groupSumaCantidadTipo.setHorizontalAlignment(HorizontalAlignment.RIGHT).setStyle(estiloTotalesCantVal).setWidth(60));
	   	       	  listaSumaTipos.add(cmp.text(" ").setHorizontalAlignment(HorizontalAlignment.RIGHT).setStyle(estiloTotalesCantVal).setWidth(20));
	   	       	  listaSumaTipos.add(groupSumaValorTipo.setHorizontalAlignment(HorizontalAlignment.RIGHT).setStyle(estiloTotalesCantVal).setWidth(90));
       			
		  	   	  //---------TOTALES PARA GRUPO NIVEL ATENCION
		       	  VariableBuilder<Integer> cantidadGrpSumNivelAtencion = variable(cantidadColumna, Calculation.SUM);
		       	  VariableBuilder<BigDecimal> valorGrpSumNivelAtencion = variable(valorColumna, Calculation.SUM);
		       	  cantidadGrpSumNivelAtencion.setResetGroup(grupoNivelAtencion);
		       	  valorGrpSumNivelAtencion.setResetGroup(grupoNivelAtencion);
	 	       	  TextFieldBuilder<String> groupSumaCantidadNivelAtencion = cmp.text(new CustomTextSubtotalCantidades(cantidadGrpSumNivelAtencion)).setStyle(letraGeneral);
	 	       	  TextFieldBuilder<String> groupSumaValorNivelAtencion = cmp.text(new CustomTextSubtotalValores(valorGrpSumNivelAtencion)).setStyle(letraGeneral);
	 	       	  
	 	       	  listaSumaNivelAtencion.add(groupSumaCantidadNivelAtencion.setHorizontalAlignment(HorizontalAlignment.RIGHT).setStyle(estiloTotalesCantVal).setWidth(60));
	 	       	  listaSumaNivelAtencion.add(cmp.text(" ").setHorizontalAlignment(HorizontalAlignment.RIGHT).setStyle(estiloTotalesCantVal).setWidth(20));
	 	       	  listaSumaNivelAtencion.add(groupSumaValorNivelAtencion.setHorizontalAlignment(HorizontalAlignment.RIGHT).setStyle(estiloTotalesCantVal).setWidth(90));
	 	       	  
	 	       	  //--------TOTALES PARA CONVENIO
	   	       	  VariableBuilder<Integer> cantidadGrpSumConvenio = variable(cantidadColumna, Calculation.SUM);
	   	       	  VariableBuilder<BigDecimal> valorGrpSumConvenio = variable(valorColumna, Calculation.SUM);
	   	       	  cantidadGrpSumConvenio.setResetGroup(grupoConvenio);
	   	       	  valorGrpSumConvenio.setResetGroup(grupoConvenio);
	   	       	  TextFieldBuilder<String> groupSumaCantidadConvenio = cmp.text(new CustomTextSubtotalCantidades(cantidadGrpSumConvenio)).setStyle(letraGeneral);
	   	       	  TextFieldBuilder<String> groupSumaValorConvenio = cmp.text(new CustomTextSubtotalValores(valorGrpSumConvenio)).setStyle(letraGeneral);
	   	       	  
	   	       	  listaSumaConvenio.add(groupSumaCantidadConvenio.setHorizontalAlignment(HorizontalAlignment.RIGHT).setStyle(estiloTotalesCantVal).setWidth(60));
	   	       	  listaSumaConvenio.add(cmp.text(" ").setHorizontalAlignment(HorizontalAlignment.RIGHT).setStyle(estiloTotalesCantVal).setWidth(20));
	   	       	  listaSumaConvenio.add(groupSumaValorConvenio.setHorizontalAlignment(HorizontalAlignment.RIGHT).setStyle(estiloTotalesCantVal).setWidth(90));
		       	  
	   	       	  // TOTALES GENERALES
	   	       	  VariableBuilder<Integer> cantidadGrpSumTotales = variable(cantidadColumna, Calculation.SUM);
		       	  VariableBuilder<BigDecimal> valorGrpSumTotales = variable(valorColumna, Calculation.SUM);
	 	       	  TextFieldBuilder<String> groupSumaCantidadTotales = cmp.text(new CustomTextSubtotalCantidades(cantidadGrpSumTotales)).setStyle(letraGeneral);
	 	       	  TextFieldBuilder<String> groupSumaValorTotales = cmp.text(new CustomTextSubtotalValores(valorGrpSumTotales)).setStyle(letraGeneral);
	 	       	  
	 	       	  listaSumaTotalesEntidad.add(groupSumaCantidadTotales.setHorizontalAlignment(HorizontalAlignment.RIGHT).setStyle(estiloTotalesCantVal).setWidth(60));
	 	       	  listaSumaTotalesEntidad.add(cmp.text(" ").setHorizontalAlignment(HorizontalAlignment.RIGHT).setStyle(estiloTotalesCantVal).setWidth(20));
	 	       	  listaSumaTotalesEntidad.add(groupSumaValorTotales.setHorizontalAlignment(HorizontalAlignment.RIGHT).setStyle(estiloTotalesCantVal).setWidth(90));
	 	       	  
	 	       	report.variables(cantidadGrpSumNivelAtencion, valorGrpSumNivelAtencion,
	 	       					cantidadGrpSumTipo, nombreTipo, valorGrpSumTipo,
	 	       					cantidadGrpSumConvenio, valorGrpSumConvenio,
	 	       					cantidadGrpSumTotales,valorGrpSumTotales
        		  		   );
	 	       	
	 	       	report.columns(cantidadColumna,espacioColumna,valorColumna);
	        } 
	       	  
	      //------------AGREGAR LAS LISTAS AL FOOTER DE CADA GRUPO--------
	      grupoNivelAtencion.addFooterComponent(listaSumaNivelAtencion, cmp.text("").setHeight(5));
	      grupoTipo.addFooterComponent(listaSumaTipos, cmp.text("").setHeight(5));
	      grupoConvenio.addFooterComponent(listaSumaConvenio, cmp.text("").setHeight(15));
	      report.summary(cmp.text("").setHeight(5));
	      report.summary(listaSumaTotalesEntidad);
	   	  
	      //------------AGREGAR LOS GRUPOS AL REPORTE GENERAL-------------
	      report.addGroup(grupoConvenio);
	   	  report.addGroup(grupoNivelAtencion);
	   	  report.addGroup(grupoTipo);
	   	          
	   	  //------------CONFIGURACIÓN GENERAL DEL REPORTE-----------------
   	      report.setPageFormat(PageType.LETTER, PageOrientation.PORTRAIT);
   	      MarginBuilder margenes = margin().setBottom(30).setTop(40).setLeft(40).setRight(20);
   	      report.setPageMargin(margenes); 
	      report.setColumnTitleStyle(estiloLetraColumnasCantVal);
	      report.setColumnStyle(estiloCantidadesValores);
	      report.highlightDetailEvenRows();
 	     // report.title(this.detallesReporte.crearEncabezadoReporteGrupoClase());
          report.setDataSource(crearDataSource());

          if(filtroConsultaOrdenes.getTipoSalida()== EnumTiposSalida.PDF.getCodigo())
  	      {
  	    	 report.pageFooter(this.detallesReporte.crearPiePaginaReporte());
  	    	report.pageHeader(this.detallesReporte.crearEncabezadoReporteGrupoClase());
  	      }else
  	      {
  	    	 report.setIgnorePagination(true);
  	      }
          
          return report;
   	  }  
   	        
    	private JRDataSource crearDataSource(){
    		
    		SortGenerico sortG=new SortGenerico("NivelAtencion",true);
    		
    		Collections.sort(listadoResultado ,sortG);
			
    		for(DtoConsultaTotalOrdenesAutorizadasEntSub dtoConsulta:listadoResultado)
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
    		
    		ArrayList<String> convenioNivelClase= new ArrayList<String>();
    		ArrayList<String> convenioNivelGrupo= new ArrayList<String>();
    		ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub> listaDefinitiva= new ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub>();
    		
    		for(int i=0;i<listadoResultado.size();i++)
    		{
    			if(UtilidadTexto.isEmpty(listadoResultado.get(i).getGrupoServicio()))
    			{
    				if(!convenioNivelClase.contains(listadoResultado.get(i).getNombreConvenio()+ConstantesBD.separadorSplit+
    						listadoResultado.get(i).getNivelAtencion()+ConstantesBD.separadorSplit+
    						listadoResultado.get(i).getClaseInventario()+ConstantesBD.separadorSplit+
    						listadoResultado.get(i).getEstadoAutorizacion()))
    				{
        				for(int j=i+1;j<listadoResultado.size();j++)
            			{
        					if(listadoResultado.get(j).getCodigoArticulo()!=null)
        					{
        						if(listadoResultado.get(j).getNombreConvenio().equals(listadoResultado.get(i).getNombreConvenio())
        	    						&&listadoResultado.get(j).getNivelAtencion().equals(listadoResultado.get(i).getNivelAtencion())
        	    						&&listadoResultado.get(j).getClaseInventario().equals(listadoResultado.get(i).getClaseInventario())
        	    						&&listadoResultado.get(j).getEstadoAutorizacion().equals(listadoResultado.get(i).getEstadoAutorizacion())
        	    						)
            					{
            						listadoResultado.get(j).setConsecutivo(null);
            						listadoResultado.get(i).setCantidadArticulo(listadoResultado.get(i).getCantidadArticulo()+listadoResultado.get(j).getCantidadArticulo());
            						listadoResultado.get(i).setValorArticulo(listadoResultado.get(i).getValorArticulo().add(listadoResultado.get(j).getValorArticulo()));
            						
            						convenioNivelClase.add(listadoResultado.get(j).getNombreConvenio()+ConstantesBD.separadorSplit+
            								listadoResultado.get(j).getNivelAtencion()+ConstantesBD.separadorSplit+
            								listadoResultado.get(j).getClaseInventario()+ConstantesBD.separadorSplit+
            								listadoResultado.get(j).getEstadoAutorizacion());
            					}
        					}
            			}
    				}
    			}else
    			{
    				if(!convenioNivelGrupo.contains(listadoResultado.get(i).getNombreConvenio()+ConstantesBD.separadorSplit+
    						listadoResultado.get(i).getNivelAtencion()+ConstantesBD.separadorSplit+
    						listadoResultado.get(i).getGrupoServicio()+ConstantesBD.separadorSplit+
    						listadoResultado.get(i).getEstadoAutorizacion()))
    				{
        				for(int j=i+1;j<listadoResultado.size();j++)
            			{
        					if(listadoResultado.get(j).getCodigoServicio()!=null)
        					{
        						if(listadoResultado.get(j).getNombreConvenio().equals(listadoResultado.get(i).getNombreConvenio())
        	    						&&listadoResultado.get(j).getNivelAtencion().equals(listadoResultado.get(i).getNivelAtencion())
        	    						&&listadoResultado.get(j).getGrupoServicio().equals(listadoResultado.get(i).getGrupoServicio())
        	    						&&listadoResultado.get(j).getEstadoAutorizacion().equals(listadoResultado.get(i).getEstadoAutorizacion())
        	    						)
            					{
            						listadoResultado.get(j).setConsecutivo(null);
            						listadoResultado.get(i).setCantidadServicio(listadoResultado.get(i).getCantidadServicio()+listadoResultado.get(j).getCantidadServicio());
            						listadoResultado.get(i).setValorServicio(listadoResultado.get(i).getValorServicio().add(listadoResultado.get(j).getValorServicio()));
            						
            						convenioNivelGrupo.add(listadoResultado.get(j).getNombreConvenio()+ConstantesBD.separadorSplit+
            								listadoResultado.get(j).getNivelAtencion()+ConstantesBD.separadorSplit+
            								listadoResultado.get(j).getGrupoServicio()+ConstantesBD.separadorSplit+
            								listadoResultado.get(j).getEstadoAutorizacion());
            					}
        					}
            			}
    				}
    			}
    		}
    		
    		for(DtoConsultaTotalOrdenesAutorizadasEntSub dtoConsulta:listadoResultado)
    		{
    			if(dtoConsulta.getConsecutivo()!=null)
    				listaDefinitiva.add(dtoConsulta);
    		}
    		
    		
    		
    		int columnasTotales=(estadosAutoriz.size()*2)+4;
     		String[] columnas = new String[columnasTotales];
     		columnas[0]="convenio";
     		columnas[1]="nivelAtencion";
     		columnas[2]="tipo";
     		columnas[3]="grupoClase";
     		
     		int contador=4;
     		for(int i=1 ; i<=estadosAutoriz.size() ; i++)
     		{
     			columnas[contador] = "cantidad" + i;
     			columnas[contador+1] = "valor" + i;
     			contador+=2;
     		}
     		DataSource dataSource = new DataSource(columnas);
     		
    		ArrayList<Object> totales= new ArrayList<Object>();
    		convenioNivelClase= new ArrayList<String>();
    		convenioNivelGrupo= new ArrayList<String>();
    		
    		for(DtoConsultaTotalOrdenesAutorizadasEntSub dtoConsulta:listaDefinitiva)
    		{
    			if(UtilidadTexto.isEmpty(dtoConsulta.getGrupoServicio()))
    			{
    				totales= new ArrayList<Object>();
        			if(!convenioNivelClase.contains(dtoConsulta.getNombreConvenio()+ConstantesBD.separadorSplit+
    						dtoConsulta.getNivelAtencion()+ConstantesBD.separadorSplit+
    						dtoConsulta.getClaseInventario()))
    				{
    	    			for(int i=0;i<filtroConsultaOrdenes.getNombresEstadosAutorizaciones().size();i++)
    	    			{
    	    				int totalSize=totales.size();
    	    				for(DtoConsultaTotalOrdenesAutorizadasEntSub dtoConsultaInterno:listaDefinitiva)
    		    			{
    	    					if(dtoConsultaInterno.getCodigoArticulo()!=null)
    	    					{
    	    						if(dtoConsultaInterno.getNombreConvenio().equals(dtoConsulta.getNombreConvenio())
    			    						&&dtoConsultaInterno.getNivelAtencion().equals(dtoConsulta.getNivelAtencion())
    			    						&&dtoConsultaInterno.getClaseInventario().equals(dtoConsulta.getClaseInventario())
    			    						&&dtoConsultaInterno.getEstadoAutorizacion().equals(filtroConsultaOrdenes.getNombresEstadosAutorizaciones().get(i).getAcronimo())
    			    						)
    			    				{
    			    						
    			    						totales.add((dtoConsultaInterno.getCantidadArticulo()!=null?dtoConsultaInterno.getCantidadArticulo():0));
    			    						totales.add((dtoConsultaInterno.getValorArticulo()!=null?dtoConsultaInterno.getValorArticulo():new BigDecimal(0.0)));
    			    						convenioNivelClase.add(dtoConsultaInterno.getNombreConvenio()+ConstantesBD.separadorSplit+
    												 dtoConsultaInterno.getNivelAtencion()+ConstantesBD.separadorSplit+
    											     dtoConsultaInterno.getClaseInventario());
    			    				}
    	    					}
    		    			}
    	    				if(totales.size()==totalSize)
    	    				{
    	    					totales.add(0);
    	    					totales.add(new BigDecimal(0.0));
    	    					convenioNivelClase.add(dtoConsulta.getNombreConvenio()+ConstantesBD.separadorSplit+
    									dtoConsulta.getNivelAtencion()+ConstantesBD.separadorSplit+
    									dtoConsulta.getClaseInventario());
    	    				}
        				}
        			}
    	    			
        			if(!totales.isEmpty()){
    	    			Object[] datos= new Object[columnasTotales];
    	    			datos[0]=dtoConsulta.getNombreConvenio();
    	    			datos[1]=dtoConsulta.getNivelAtencion();
    	    			datos[2]="Clase de Inventarios";
    	    			datos[3]=dtoConsulta.getClaseInventario();
    	    			contador=4;
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
        			if(!convenioNivelGrupo.contains(dtoConsulta.getNombreConvenio()+ConstantesBD.separadorSplit+
    						dtoConsulta.getNivelAtencion()+ConstantesBD.separadorSplit+
    						dtoConsulta.getGrupoServicio()))
    				{
    	    			for(int i=0;i<filtroConsultaOrdenes.getNombresEstadosAutorizaciones().size();i++)
    	    			{
    	    				int totalSize=totales.size();
    	    				for(DtoConsultaTotalOrdenesAutorizadasEntSub dtoConsultaInterno:listaDefinitiva)
    		    			{
    	    					if(dtoConsultaInterno.getCodigoServicio()!=null)
    	    					{
    	    						if(dtoConsultaInterno.getNombreConvenio().equals(dtoConsulta.getNombreConvenio())
    			    						&&dtoConsultaInterno.getNivelAtencion().equals(dtoConsulta.getNivelAtencion())
    			    								&&dtoConsultaInterno.getGrupoServicio().equals(dtoConsulta.getGrupoServicio())
    			    						)
    			    				{
    			    						
    			    					if(dtoConsultaInterno.getEstadoAutorizacion().equals(filtroConsultaOrdenes.getNombresEstadosAutorizaciones().get(i).getAcronimo())
    			    							)
    			    					{
    			    						totales.add((dtoConsultaInterno.getCantidadServicio()!=null?dtoConsultaInterno.getCantidadServicio():0));
    			    						totales.add((dtoConsultaInterno.getValorServicio()!=null?dtoConsultaInterno.getValorServicio():new BigDecimal(0.0)));
    			    						convenioNivelGrupo.add(dtoConsultaInterno.getNombreConvenio()+ConstantesBD.separadorSplit+
    												 dtoConsultaInterno.getNivelAtencion()+ConstantesBD.separadorSplit+
    											     dtoConsultaInterno.getGrupoServicio());
    			    					}
    			    				}
    	    					}
    		    			}
    	    				if(totales.size()==totalSize)
    	    				{
    	    					totales.add(0);
    	    					totales.add(new BigDecimal(0.0));
    	    					convenioNivelGrupo.add(dtoConsulta.getNombreConvenio()+ConstantesBD.separadorSplit+
    									dtoConsulta.getNivelAtencion()+ConstantesBD.separadorSplit+
    									dtoConsulta.getGrupoServicio());
    	    				}
        				}
        			}
    	    			
        			if(!totales.isEmpty()){
    	    			Object[] datos= new Object[columnasTotales];
    	    			datos[0]=dtoConsulta.getNombreConvenio();
    	    			datos[1]=dtoConsulta.getNivelAtencion();
    	    			datos[2]="Grupo de Servicios";
    	    			datos[3]=dtoConsulta.getGrupoServicio();
    	    			contador=4;
    	    			for(int i =0;i<totales.size();i++){
    	    				datos[contador]=totales.get(i);
    	    				contador++;
    	    			}
    	    			
    	    			dataSource.add(datos);
        			}
    			}
    			
    		}
    		
    		/*DataSource dataSource = new DataSource("convenio","nivelAtencion","tipo","grupoClase","cantidad1","valor1","cantidad2","valor2","cantidad3","valor3");
			dataSource.add("Convenio 1","Nivel Atención I","Grupo de Servicios","Consulta", 1, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Convenio 1","Nivel Atención I","Clase de Inventarios","Medicamentos", 2, new BigDecimal(200), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Convenio 2","Nivel Atención I","Grupo de Servicios","Cirugías", 3, new BigDecimal(300), 1, new BigDecimal(100), 1, new BigDecimal(100));
	         dataSource.add("Convenio 2","Nivel Atención I","Clase de Inventarios","Medicamentos",  4, new BigDecimal(400), 1, new BigDecimal(100), 1, new BigDecimal(100));
	         dataSource.add("Convenio 2","Nivel Atención II","Grupo de Servicios","Exámenes de Laboratorio", 5, new BigDecimal(500), 1, new BigDecimal(100), 1, new BigDecimal(100));
	         dataSource.add("Convenio 3","Nivel Atención II","Clase de Inventarios","Artículos",  7, new BigDecimal(700), 1, new BigDecimal(100), 1, new BigDecimal(100));*/
			
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
    	 * Esta clase ayuda a retornar el nombre del tipo (Grupo de servicio o clase de inventario)
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
