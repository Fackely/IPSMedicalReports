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
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.builder.group.CustomGroupBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.Calculation;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import util.ConstantesBD;
import util.UtilidadTexto;
import util.reportes.dinamico.GeneradorReporteDinamico;

import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.manejoPaciente.DtoBusquedaTotalOrdenesAutorizadasEntSub;
import com.princetonsa.dto.manejoPaciente.DtoConsultaTotalOrdenesAutorizadasEntSub;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.princetonsa.sort.odontologia.SortGenerico;

public class GeneradorReporteConsultaOrdenesTipoNivelAtencion extends GeneradorReporteDinamico{

	
	/** *Constantes del reporte */
	private final int tamanoLetrasColumnasCantidadValor=9;
	private final int tamanoCantidadesValores=10;
	private final int tamanoLetraReporte=10;
	private final int ancho=280;
	
	private ArrayList<DtoIntegridadDominio> estadosAutoriz = null;
	private DetallesGeneralesFormatos detallesReporte;
	private ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub> listadoResultado;
	private DtoBusquedaTotalOrdenesAutorizadasEntSub filtroConsultaOrdenes;
	
	/**
	 * Constructor de la clase
	 */
	public GeneradorReporteConsultaOrdenesTipoNivelAtencion() {
    }  
    
	/**
	 * Constructor de la clase
	 */
	public GeneradorReporteConsultaOrdenesTipoNivelAtencion(ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub> listadoResultado, 
			DtoBusquedaTotalOrdenesAutorizadasEntSub filtroConsultaOrdenes) {
			estadosAutoriz=filtroConsultaOrdenes.getNombresEstadosAutorizaciones();
			this.listadoResultado=listadoResultado;
			this.filtroConsultaOrdenes=filtroConsultaOrdenes;
			this.detallesReporte = new DetallesGeneralesFormatos(filtroConsultaOrdenes);
			
			//this.generarReporte();
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
        TextColumnBuilder<String>     entidadSubColumna      	= col.column("entidad Subcontratada",   "entidadSub",      	type.stringType());
	    TextColumnBuilder<String>     convenioColumna      		= col.column("Convenio",       			"convenio",      	type.stringType());
	    TextColumnBuilder<String>     nivelAtencionColumna    	= col.column("Nivel Atención",       	"nivelAtencion",    type.stringType());
	    TextColumnBuilder<String>     espacioColumna      		= col.column(" ",       				"prueba",      		type.stringType());
	    TextColumnBuilder<String>     tipoColumna      			= col.column("",       					"tipo",      		type.stringType()).setWidth(180);
   	    //------------------------------------------------------      
   	          
	    //---------CREACION DE LISTAS CON TOTALES POR CADA GRUPO----------
	    CustomGroupBuilder grupoEntidadSub	= grp.group("entidadSub", "entidadSub", String.class).setTitle("Entidad Subcontratada: ").setTitleStyle(letraGeneral).setTitleWidth(120).setHorizontalAlignment(HorizontalAlignment.LEFT).setStyle(stl.style()).setPadding(0);
     	CustomGroupBuilder grupoConvenio	= grp.group("convenio", "convenio", String.class).setTitle("Convenio: ").setTitleStyle(letraGeneral).setTitleWidth(60).setHorizontalAlignment(HorizontalAlignment.LEFT).setStyle(stl.style()).setPadding(0);
     	CustomGroupBuilder grupoNivelAtencion= grp.group("nivelAtencion", "nivelAtencion", String.class).setPadding(0);
	    //------------------------------------------------------
   	          
     	//---------CREACION DE LISTAS CON TOTALES POR CADA GRUPO---------- 
     	HorizontalListBuilder listaSuma = cmp.horizontalList();
     	listaSuma.add(cmp.text("Total Nivel Atención").setWidth(ancho).setStyle(letraGeneral.bold()));
     	  
     	HorizontalListBuilder listaSumaConvenios = cmp.horizontalList();
      	listaSumaConvenios.add(cmp.text("Total Convenio").setWidth(ancho).setStyle(letraGeneral.bold()));
      	  
      	VerticalListBuilder listaVerticalEntidades= cmp.verticalList();
     	listaVerticalEntidades.add(cmp.text("TOTAL ENTIDAD SUBCONTRATADA").setStyle(letraGeneral.bold()).setHorizontalAlignment(HorizontalAlignment.LEFT));
     	HorizontalListBuilder listaSumaEntidades = cmp.horizontalList();
     	VariableBuilder<String> nombreEntidadSub = variable(entidadSubColumna, Calculation.NOTHING);
     	nombreEntidadSub.setResetGroup(grupoEntidadSub);
     	TextFieldBuilder<String> groupNombreEntidad = cmp.text(new CustomTextNombreEntidadSub(nombreEntidadSub));
     	listaSumaEntidades.add(groupNombreEntidad.setWidth(ancho).setStyle(letraGeneral.bold()));
     	  
     	HorizontalListBuilder listaSumaTotales = cmp.horizontalList();
     	listaSumaTotales.add(cmp.text("TOTAL GENERAL").setWidth(ancho).setStyle(letraGeneral.bold()));
     	//------------------------------------------------------
   	          
	    JasperReportBuilder report = report();
	    report.columns(espacioColumna,tipoColumna);
	       	  
   	    for (int i = 1; i <= estadosAutoriz.size(); i++) {  
	  	   
	      TextColumnBuilder<Integer>    cantidadColumna =col.column("Cantidad", "cantidad" + i, type.integerType()).setHorizontalAlignment(HorizontalAlignment.RIGHT).setWidth(60).setStyle(estiloCantidadesValores);
	  	  TextColumnBuilder<Integer>    columnaEsp 	=col.column(" ",           "espacio" + i, 	type.integerType()).setHorizontalAlignment(HorizontalAlignment.RIGHT).setWidth(20).setStyle(estiloCantidadesValores);
	  	  TextColumnBuilder<BigDecimal> valorColumna = col.column("   Valor", "valor" + i, type.bigDecimalType()).setHorizontalAlignment(HorizontalAlignment.RIGHT).setWidth(90).setStyle(estiloCantidadesValores);
	  	  
	  	  //---------TOTALES PARA GRUPO NIVEL ATENCION
		  VariableBuilder<Integer> cantidadGrpSumNivelAtencion = variable(cantidadColumna, Calculation.SUM);
		  VariableBuilder<BigDecimal> valorGrpSumNivelAtencion = variable(valorColumna, Calculation.SUM);
		  cantidadGrpSumNivelAtencion.setResetGroup(grupoNivelAtencion);
		  valorGrpSumNivelAtencion.setResetGroup(grupoNivelAtencion);
       	  TextFieldBuilder<String> groupSumaCantidadNivelAtencion = cmp.text(new CustomTextSubtotalCantidades(cantidadGrpSumNivelAtencion)).setStyle(letraGeneral);
       	  TextFieldBuilder<String> groupSumaValorNivelAtencion = cmp.text(new CustomTextSubtotalValores(valorGrpSumNivelAtencion)).setStyle(letraGeneral);
       	  
       	  listaSuma.add(groupSumaCantidadNivelAtencion.setHorizontalAlignment(HorizontalAlignment.RIGHT).setStyle(estiloTotalesCantVal).setWidth(60));
       	  listaSuma.add(cmp.text(" ").setHorizontalAlignment(HorizontalAlignment.RIGHT).setStyle(estiloTotalesCantVal).setWidth(20));
       	  listaSuma.add(groupSumaValorNivelAtencion.setHorizontalAlignment(HorizontalAlignment.RIGHT).setStyle(estiloTotalesCantVal).setWidth(90));
       	  
       	  //--------TOTALES PARA GRUPO CONVENIO
       	  VariableBuilder<Integer> cantidadGrpSumConvenio = variable(cantidadColumna, Calculation.SUM);
       	  VariableBuilder<BigDecimal> valorGrpSumConvenio = variable(valorColumna, Calculation.SUM);
       	  cantidadGrpSumConvenio.setResetGroup(grupoConvenio);
       	  valorGrpSumConvenio.setResetGroup(grupoConvenio);
       	  TextFieldBuilder<String> groupSumaCantidadConvenio = cmp.text(new CustomTextSubtotalCantidades(cantidadGrpSumConvenio)).setStyle(letraGeneral);
       	  TextFieldBuilder<String> groupSumaValorConvenio = cmp.text(new CustomTextSubtotalValores(valorGrpSumConvenio)).setStyle(letraGeneral);
       	  
       	  listaSumaConvenios.add(groupSumaCantidadConvenio.setHorizontalAlignment(HorizontalAlignment.RIGHT).setStyle(estiloTotalesCantVal).setWidth(60));
       	  listaSumaConvenios.add(cmp.text(" ").setHorizontalAlignment(HorizontalAlignment.RIGHT).setStyle(estiloTotalesCantVal).setWidth(20));
    	  listaSumaConvenios.add(groupSumaValorConvenio.setHorizontalAlignment(HorizontalAlignment.RIGHT).setStyle(estiloTotalesCantVal).setWidth(90));
       	  
       	  //--------TOTALES PARA GRUPO ENTIDAD SUBCONTRATADA
       	  VariableBuilder<Integer> cantidadGrpSumEntidadSub = variable(cantidadColumna, Calculation.SUM);
       	  VariableBuilder<BigDecimal> valorGrpSumEntidadSub = variable(valorColumna, Calculation.SUM);
       	  cantidadGrpSumEntidadSub.setResetGroup(grupoEntidadSub);
       	  valorGrpSumEntidadSub.setResetGroup(grupoEntidadSub);
       	  TextFieldBuilder<String> groupSumaCantidadEntidad = cmp.text(new CustomTextSubtotalCantidades(cantidadGrpSumEntidadSub)).setStyle(letraGeneral);
       	  TextFieldBuilder<String> groupSumaValorEntidad = cmp.text(new CustomTextSubtotalValores(valorGrpSumEntidadSub)).setStyle(letraGeneral);
       	  
       	  listaSumaEntidades.add(groupSumaCantidadEntidad.setHorizontalAlignment(HorizontalAlignment.RIGHT).setStyle(estiloTotalesCantVal).setWidth(60));
       	  listaSumaEntidades.add(cmp.text(" ").setHorizontalAlignment(HorizontalAlignment.RIGHT).setStyle(estiloTotalesCantVal).setWidth(20));
       	  listaSumaEntidades.add(groupSumaValorEntidad.setHorizontalAlignment(HorizontalAlignment.RIGHT).setStyle(estiloTotalesCantVal).setWidth(90));
    	  
       	  //TOTALES GENERALES
       	  VariableBuilder<Integer> cantidadGrpSumTotales = variable(cantidadColumna, Calculation.SUM);
       	  VariableBuilder<BigDecimal> valorGrpSumTotales = variable(valorColumna, Calculation.SUM);
       	  TextFieldBuilder<String> groupSumaCantidadTotales = cmp.text(new CustomTextSubtotalCantidades(cantidadGrpSumTotales)).setStyle(letraGeneral);
       	  TextFieldBuilder<String> groupSumaValorTotales = cmp.text(new CustomTextSubtotalValores(valorGrpSumTotales)).setStyle(letraGeneral);
       	  
       	  listaSumaTotales.add(groupSumaCantidadTotales.setHorizontalAlignment(HorizontalAlignment.RIGHT).setStyle(estiloTotalesCantVal).setWidth(60));
       	  listaSumaTotales.add(cmp.text(" ").setHorizontalAlignment(HorizontalAlignment.RIGHT).setStyle(estiloTotalesCantVal).setWidth(20));
       	  listaSumaTotales.add(groupSumaValorTotales.setHorizontalAlignment(HorizontalAlignment.RIGHT).setStyle(estiloTotalesCantVal).setWidth(90));
		 	       	  
		 	report.variables(cantidadGrpSumNivelAtencion, valorGrpSumNivelAtencion,
	        		  		   cantidadGrpSumConvenio, valorGrpSumConvenio,
	        		  		   cantidadGrpSumEntidadSub,nombreEntidadSub,valorGrpSumEntidadSub,
	        		  		   cantidadGrpSumTotales,valorGrpSumTotales);
		 	       	
		 	report.columns(cantidadColumna,columnaEsp,valorColumna);
   	     } 
   	          
   	    //------------AGREGAR LAS LISTAS AL FOOTER DE CADA GRUPO--------
   	    listaVerticalEntidades.add(listaSumaEntidades);
	    listaVerticalEntidades.add(cmp.text("").setHeight(10));
	    grupoNivelAtencion.addFooterComponent(listaSuma);
	    grupoConvenio.addFooterComponent(listaSumaConvenios, cmp.text("").setHeight(5));
	    grupoEntidadSub.addFooterComponent(listaVerticalEntidades);
	    report.summary(listaSumaTotales);
	    
	    //------------AGREGAR LOS GRUPOS AL REPORTE GENERAL-------------
   	    report.addGroup(grupoEntidadSub);
   	    report.addGroup(grupoConvenio);
   	    report.addGroup(grupoNivelAtencion);
   	          
   	    //------------CONFIGURACIÓN GENERAL DEL REPORTE-----------------
   	    report.setPageFormat(PageType.LETTER, PageOrientation.PORTRAIT);
   	    MarginBuilder margenes = margin().setBottom(30).setTop(40).setLeft(40).setRight(20);
	   	report.setPageMargin(margenes); 
	    report.setColumnTitleStyle(estiloLetraColumnasCantVal);
	    report.setColumnStyle(estiloCantidadesValores);
	    report.highlightDetailEvenRows();
	   
	    
	    if(filtroConsultaOrdenes.getTipoSalida()== EnumTiposSalida.PDF.getCodigo())
	    {
	    	report.pageHeader(this.detallesReporte.crearEncabezadoReporte());
	    	report.pageFooter(this.detallesReporte.crearPiePaginaReporte());
	    }else
	    {
	    	report.title(this.detallesReporte.crearEncabezadoReporte());
	    	report.setIgnorePagination(true);
	    }
	    report.setDataSource(crearDataSource());	
	    
   	    
	    return report;
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
			int cantidadSumValue = reportParameters.getValue(cantidadSum);
			return type.integerType().valueToString(cantidadSumValue, reportParameters.getLocale());			       
		}		
	}
    
    /**
	 * Esta clase ayuda a retornar el nombre del tipo (Grupo de servicio o clase de inventario)
	 * para ser utilizado en el nombre de los totales
	 * @author Fabián Becerra
	 */
    private class CustomTextNombreEntidadSub extends AbstractSimpleExpression<String> {
		private static final long serialVersionUID = 1L;
		private VariableBuilder<String> nombreEntidad;
		public CustomTextNombreEntidadSub(VariableBuilder<String> nombreEntidad) {
			this.nombreEntidad = nombreEntidad;
		}
		public String evaluate(ReportParameters reportParameters) {
			String valorNombreEntidad = reportParameters.getValue(nombreEntidad);
			return type.stringType().valueToString(valorNombreEntidad, reportParameters.getLocale()).toUpperCase();			       
		}		
	}
    
    	private JRDataSource crearDataSource(){
			
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
    		
    		ArrayList<String> entidadConvenioNATServicio= new ArrayList<String>();
    		ArrayList<String> entidadConvenioNATArticulo= new ArrayList<String>();
    		ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub> listaDefinitiva= new ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub>();
    		
    		for(int i=0;i<listadoResultado.size();i++)
    		{
    			if(listadoResultado.get(i).getCodigoServicio()!=null)
    			{
    				if(!entidadConvenioNATServicio.contains(listadoResultado.get(i).getCodigoEntidadSub()+ConstantesBD.separadorSplit+
    						listadoResultado.get(i).getCodigoConvenio()+ConstantesBD.separadorSplit+
    						listadoResultado.get(i).getNivelAtencionServicio()+ConstantesBD.separadorSplit+
    						listadoResultado.get(i).getEstadoAutorizacion()))
    				{
        				for(int j=i+1;j<listadoResultado.size();j++)
            			{
        					if(listadoResultado.get(j).getCodigoServicio()!=null)
        					{
        						if(listadoResultado.get(j).getCodigoEntidadSub().equals(listadoResultado.get(i).getCodigoEntidadSub())
        	    						&&listadoResultado.get(j).getCodigoConvenio().equals(listadoResultado.get(i).getCodigoConvenio())
        	    						&&listadoResultado.get(j).getNivelAtencionServicio().equals(listadoResultado.get(i).getNivelAtencionServicio())
        	    						&&listadoResultado.get(j).getEstadoAutorizacion().equals(listadoResultado.get(i).getEstadoAutorizacion())
        	    						)
            					{
            						listadoResultado.get(j).setConsecutivo(null);
            						listadoResultado.get(i).setCantidadServicio(listadoResultado.get(i).getCantidadServicio()+listadoResultado.get(j).getCantidadServicio());
            						listadoResultado.get(i).setValorServicio(listadoResultado.get(i).getValorServicio().add(listadoResultado.get(j).getValorServicio()));
            						
            						entidadConvenioNATServicio.add(listadoResultado.get(i).getCodigoEntidadSub()+ConstantesBD.separadorSplit+
            	    						listadoResultado.get(i).getCodigoConvenio()+ConstantesBD.separadorSplit+
            	    						listadoResultado.get(i).getNivelAtencionServicio()+ConstantesBD.separadorSplit+
            	    						listadoResultado.get(i).getEstadoAutorizacion());
            					}
        					}
            			}
    				}
    			}else
    			{
    				if(!entidadConvenioNATArticulo.contains(listadoResultado.get(i).getCodigoEntidadSub()+ConstantesBD.separadorSplit+
    						listadoResultado.get(i).getCodigoConvenio()+ConstantesBD.separadorSplit+
    						listadoResultado.get(i).getNivelAtencionArticulo()+ConstantesBD.separadorSplit+
    						listadoResultado.get(i).getEstadoAutorizacion()))
    				{
        				for(int j=i+1;j<listadoResultado.size();j++)
            			{
        					if(listadoResultado.get(j).getCodigoArticulo()!=null)
        					{
        						if(listadoResultado.get(j).getCodigoEntidadSub().equals(listadoResultado.get(i).getCodigoEntidadSub())
        	    						&&listadoResultado.get(j).getCodigoConvenio().equals(listadoResultado.get(i).getCodigoConvenio())
        	    						&&listadoResultado.get(j).getNivelAtencionArticulo().equals(listadoResultado.get(i).getNivelAtencionArticulo())
        	    						&&listadoResultado.get(j).getEstadoAutorizacion().equals(listadoResultado.get(i).getEstadoAutorizacion())
        	    						)
            					{
            						listadoResultado.get(j).setConsecutivo(null);
            						listadoResultado.get(i).setCantidadArticulo(listadoResultado.get(i).getCantidadArticulo()+listadoResultado.get(j).getCantidadArticulo());
            						listadoResultado.get(i).setValorArticulo(listadoResultado.get(i).getValorArticulo().add(listadoResultado.get(j).getValorArticulo()));
            						
            						entidadConvenioNATArticulo.add(listadoResultado.get(i).getCodigoEntidadSub()+ConstantesBD.separadorSplit+
            	    						listadoResultado.get(i).getCodigoConvenio()+ConstantesBD.separadorSplit+
            	    						listadoResultado.get(i).getNivelAtencionArticulo()+ConstantesBD.separadorSplit+
            	    						listadoResultado.get(i).getEstadoAutorizacion());
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
    		
    		
    		/***ORDENAR REGISTROS POR ENTIDAD - CONVENIO  - NIVEL ATENCION*******/
    		
    		ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub> listaPorConvenios= new ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub>();
    		ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub> listaDefinitivaOrdenada= new ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub>();
    		ArrayList<String> entidadConvenio= new ArrayList<String>();
    		
    		for(DtoConsultaTotalOrdenesAutorizadasEntSub dtoConsulta:listaDefinitiva)
    		{
    			if(!entidadConvenio.contains(dtoConsulta.getCodigoEntidadSub()+ConstantesBD.separadorSplit+
    					dtoConsulta.getCodigoConvenio()))
				{
    				listaPorConvenios= new ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub>();
        			for(DtoConsultaTotalOrdenesAutorizadasEntSub dtoConsultaInterno:listaDefinitiva)
        			{
        				if(dtoConsulta.getCodigoEntidadSub().equals(dtoConsultaInterno.getCodigoEntidadSub())
        						&&dtoConsulta.getCodigoConvenio().equals(dtoConsultaInterno.getCodigoConvenio()))
            			{
        					listaPorConvenios.add(dtoConsultaInterno);
            			}
        			}
        			
        			SortGenerico sortG=new SortGenerico("NivelAtencion",true);
            		Collections.sort(listaPorConvenios ,sortG);
            		
            		listaDefinitivaOrdenada.addAll(listaPorConvenios);
            		
            		entidadConvenio.add(dtoConsulta.getCodigoEntidadSub()+ConstantesBD.separadorSplit+
    					dtoConsulta.getCodigoConvenio());
				}
    			
    			
    		}
    		
    		
    		int columnasTotales=(estadosAutoriz.size()*2)+4;
     		String[] columnas = new String[columnasTotales];
     		columnas[0]="entidadSub";
     		columnas[1]="convenio";
     		columnas[2]="nivelAtencion";
     		columnas[3]="tipo";
     		
     		int contador=4;
     		for(int i=1 ; i<=estadosAutoriz.size() ; i++)
     		{
     			columnas[contador] = "cantidad" + i;
     			columnas[contador+1] = "valor" + i;
     			contador+=2;
     		}
     		DataSource dataSource = new DataSource(columnas);
     		
    		ArrayList<Object> totalesServicios= new ArrayList<Object>();
    		ArrayList<Object> totalesMedicamentos= new ArrayList<Object>();
    		
    		ArrayList<String> entidadConvenioNivel= new ArrayList<String>();
    		
    		for(DtoConsultaTotalOrdenesAutorizadasEntSub dtoConsulta:listaDefinitivaOrdenada)
    		{
    			totalesServicios= new ArrayList<Object>();
    			totalesMedicamentos= new ArrayList<Object>();
    			for(int i=0;i<filtroConsultaOrdenes.getNombresEstadosAutorizaciones().size();i++)
    			{
    				if(!entidadConvenioNivel.contains(dtoConsulta.getNombreEntidadSub()+ConstantesBD.separadorSplit+
    						dtoConsulta.getNombreConvenio()+ConstantesBD.separadorSplit+
    						dtoConsulta.getNivelAtencion()+ConstantesBD.separadorSplit+
    						filtroConsultaOrdenes.getNombresEstadosAutorizaciones().get(i).getAcronimo()))
    				{
	    				int serviciosCantidad=0,articulosCantidad=0;
	    				BigDecimal serviciosTarifa=new BigDecimal(0);
	    				BigDecimal articulosTarifa=new BigDecimal(0);
	    				boolean encuentra=false;
	    				
	    				for(DtoConsultaTotalOrdenesAutorizadasEntSub dtoConsultaInterno:listaDefinitivaOrdenada)
		    			{
			    				if(dtoConsultaInterno.getNombreEntidadSub().equals(dtoConsulta.getNombreEntidadSub())
			    						&&dtoConsultaInterno.getNombreConvenio().equals(dtoConsulta.getNombreConvenio())
			    						&&dtoConsultaInterno.getNivelAtencion().equals(dtoConsulta.getNivelAtencion())
			    					    &&dtoConsultaInterno.getEstadoAutorizacion().equals(filtroConsultaOrdenes.getNombresEstadosAutorizaciones().get(i).getAcronimo())
			    						)
			    				{
			    						
			    						serviciosCantidad=serviciosCantidad+(dtoConsultaInterno.getCantidadServicio()!=null?dtoConsultaInterno.getCantidadServicio():0);
			    						serviciosTarifa=serviciosTarifa.add((dtoConsultaInterno.getValorServicio()!=null?dtoConsultaInterno.getValorServicio():new BigDecimal(0.0)));
			    						articulosCantidad=articulosCantidad+(dtoConsultaInterno.getCantidadArticulo()!=null?dtoConsultaInterno.getCantidadArticulo():0);
			    						articulosTarifa=articulosTarifa.add((dtoConsultaInterno.getValorArticulo()!=null?dtoConsultaInterno.getValorArticulo():new BigDecimal(0.0)));
			    						encuentra=true;
			    				}
		    			}
	    				if(!encuentra)
	    				{
	    					totalesServicios.add(0);
							totalesServicios.add(new BigDecimal(0.0));
							totalesMedicamentos.add(0);
							totalesMedicamentos.add(new BigDecimal(0.0));
							entidadConvenioNivel.add(dtoConsulta.getNombreEntidadSub()+ConstantesBD.separadorSplit+
									dtoConsulta.getNombreConvenio()+ConstantesBD.separadorSplit+
									dtoConsulta.getNivelAtencion()+ConstantesBD.separadorSplit+
									filtroConsultaOrdenes.getNombresEstadosAutorizaciones().get(i).getAcronimo());
	    				}else
	    				{
	    					totalesServicios.add(serviciosCantidad);
    						totalesServicios.add(serviciosTarifa);
    						totalesMedicamentos.add(articulosCantidad);
    						totalesMedicamentos.add(articulosTarifa);
    						entidadConvenioNivel.add(dtoConsulta.getNombreEntidadSub()+ConstantesBD.separadorSplit+
    	    						dtoConsulta.getNombreConvenio()+ConstantesBD.separadorSplit+
    	    						dtoConsulta.getNivelAtencion()+ConstantesBD.separadorSplit+
    	    						filtroConsultaOrdenes.getNombresEstadosAutorizaciones().get(i).getAcronimo());
	    				}
	    				
	    				
    				}
    			}
	    			
    			if(!totalesServicios.isEmpty()&&!totalesMedicamentos.isEmpty()){
	    			Object[] datos= new Object[columnasTotales];
	    			datos[0]=dtoConsulta.getNombreEntidadSub();
	    			datos[1]=dtoConsulta.getNombreConvenio();
	    			datos[2]=dtoConsulta.getNivelAtencion();
	    			datos[3]="Servicios";
	    			contador=4;
	    			for(int i =0;i<totalesServicios.size();i++){
	    				datos[contador]=totalesServicios.get(i);
	    				contador++;
	    			}
	    			
	    			Object[] datosM= new Object[columnasTotales];
	    			datosM[0]=dtoConsulta.getNombreEntidadSub();
	    			datosM[1]=dtoConsulta.getNombreConvenio();
	    			datosM[2]=dtoConsulta.getNivelAtencion();
	    			datosM[3]="Medicamentos/Insumos";
	    			contador=4;
	    			for(int i =0;i<totalesMedicamentos.size();i++){
	    				datosM[contador]=totalesMedicamentos.get(i);
	    				contador++;
	    			}
	    			dataSource.add(datos);
	    			dataSource.add(datosM);
    			}
    		}
    		
    		/*DataSource dataSource = new DataSource("entidadSub","convenio","nivelAtencion","tipo","cantidad1","valor1","cantidad2","valor2","cantidad3","valor3");
			dataSource.add("Entidad Sub 1","Convenio 1","Nivel Atención I","Servicios", 1, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 1","Convenio 1","Nivel Atención I","Medicamentos/Insumos", 1, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 1","Convenio 1","Nivel Atención II","Servicios", 2, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 1","Convenio 1","Nivel Atención II","Medicamentos/Insumos", 2, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 1","Convenio 2","Nivel Atención I","Servicios", 3, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 1","Convenio 2","Nivel Atención I","Medicamentos/Insumos", 3, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 1","Convenio 2","Nivel Atención II","Servicios", 4, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 1","Convenio 2","Nivel Atención II","Medicamentos/Insumos", 4, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 2","Convenio 3","Nivel Atención I","Servicios", 5, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 2","Convenio 3","Nivel Atención I","Medicamentos/Insumos", 5, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 2","Convenio 3","Nivel Atención II","Servicios", 6, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 2","Convenio 3","Nivel Atención II","Medicamentos/Insumos", 6, new BigDecimal(10000), 1, new BigDecimal(100), 1, new BigDecimal(100));


			dataSource.add("Entidad Sub 3","Convenio 1","Nivel Atención I","Servicios", 1, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 3","Convenio 1","Nivel Atención I","Medicamentos/Insumos", 1, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 3","Convenio 1","Nivel Atención II","Servicios", 2, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 3","Convenio 1","Nivel Atención II","Medicamentos/Insumos", 2, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 3","Convenio 2","Nivel Atención I","Servicios", 3, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 3","Convenio 2","Nivel Atención I","Medicamentos/Insumos", 3, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 3","Convenio 2","Nivel Atención II","Servicios", 4, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 3","Convenio 2","Nivel Atención II","Medicamentos/Insumos", 4, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			
			dataSource.add("Entidad Sub 4","Convenio 1","Nivel Atención I","Servicios", 1, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 4","Convenio 1","Nivel Atención I","Medicamentos/Insumos", 1, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 4","Convenio 1","Nivel Atención II","Servicios", 2, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 4","Convenio 1","Nivel Atención II","Medicamentos/Insumos", 2, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 4","Convenio 2","Nivel Atención I","Servicios", 3, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 4","Convenio 2","Nivel Atención I","Medicamentos/Insumos", 3, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 4","Convenio 2","Nivel Atención II","Servicios", 4, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 4","Convenio 2","Nivel Atención II","Medicamentos/Insumos", 4, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			
			dataSource.add("Entidad Sub 5","Convenio 1","Nivel Atención I","Servicios", 1, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 5","Convenio 1","Nivel Atención I","Medicamentos/Insumos", 1, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 5","Convenio 1","Nivel Atención II","Servicios", 2, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 5","Convenio 1","Nivel Atención II","Medicamentos/Insumos", 2, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 5","Convenio 2","Nivel Atención I","Servicios", 3, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 5","Convenio 2","Nivel Atención I","Medicamentos/Insumos", 3, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 5","Convenio 2","Nivel Atención II","Servicios", 4, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 5","Convenio 2","Nivel Atención II","Medicamentos/Insumos", 4, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			
			dataSource.add("Entidad Sub 6","Convenio 1","Nivel Atención I","Servicios", 1, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 6","Convenio 1","Nivel Atención I","Medicamentos/Insumos", 1, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 6","Convenio 1","Nivel Atención II","Servicios", 2, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 6","Convenio 1","Nivel Atención II","Medicamentos/Insumos", 2, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 6","Convenio 2","Nivel Atención I","Servicios", 3, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 6","Convenio 2","Nivel Atención I","Medicamentos/Insumos", 3, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 6","Convenio 2","Nivel Atención II","Servicios", 4, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 6","Convenio 2","Nivel Atención II","Medicamentos/Insumos", 4, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			
			dataSource.add("Entidad Sub 7","Convenio 1","Nivel Atención I","Servicios", 1, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 7","Convenio 1","Nivel Atención I","Medicamentos/Insumos", 1, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 7","Convenio 1","Nivel Atención II","Servicios", 2, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 7","Convenio 1","Nivel Atención II","Medicamentos/Insumos", 2, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 7","Convenio 2","Nivel Atención I","Servicios", 3, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 7","Convenio 2","Nivel Atención I","Medicamentos/Insumos", 3, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 7","Convenio 2","Nivel Atención II","Servicios", 4, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));
			dataSource.add("Entidad Sub 7","Convenio 2","Nivel Atención II","Medicamentos/Insumos", 4, new BigDecimal(100), 1, new BigDecimal(100), 1, new BigDecimal(100));*/
			
			
			return dataSource;
    	}
    	
    	 
		
}
