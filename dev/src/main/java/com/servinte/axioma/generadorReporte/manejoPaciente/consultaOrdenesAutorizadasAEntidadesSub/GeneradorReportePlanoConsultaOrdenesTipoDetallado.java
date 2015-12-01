package com.servinte.axioma.generadorReporte.manejoPaciente.consultaOrdenesAutorizadasAEntidadesSub;

import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

import net.sf.dynamicreports.examples.DataSource;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import util.ConstantesBD;
import util.UtilidadTexto;
import util.reportes.dinamico.GeneradorReporteDinamico;

import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.manejoPaciente.DtoBusquedaTotalOrdenesAutorizadasEntSub;
import com.princetonsa.dto.manejoPaciente.DtoConsultaTotalOrdenesAutorizadasEntSub;
import com.princetonsa.sort.odontologia.SortGenerico;

public class GeneradorReportePlanoConsultaOrdenesTipoDetallado extends GeneradorReporteDinamico{

	
	private ArrayList<DtoIntegridadDominio> estadosAutoriz = null;
	private ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub> listadoResultado;
	private DtoBusquedaTotalOrdenesAutorizadasEntSub filtroConsultaOrdenes;
	private DetallesGeneralesFormatos detallesReporte;

	/**
	 * Constructor de la clase
	 */
	public GeneradorReportePlanoConsultaOrdenesTipoDetallado() {  
    }  
	
	/**
	 * Constructor de la clase
	 */
	public GeneradorReportePlanoConsultaOrdenesTipoDetallado(ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub> listadoResultado, 
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
    public JasperReportBuilder generarReporte() {
    	
		  //---------DEFINICIÓN DE COLUMNAS-----------------------
		  TextColumnBuilder<String>     nivelAtencionColumna      			= col.column("Nivel de Atención", "nivelAtencion",  	type.stringType());
	      TextColumnBuilder<String>     servicioColumna    					= col.column("Servicio",       	  "servicio",      		type.stringType());
	      TextColumnBuilder<String>     serviciosMedicamentosColumna      	= col.column("Medicamento",       "medicamento", 		type.stringType());
	      //------------------------------------------------------
	      
	      //----------REPORTE GENERAL
	   	  JasperReportBuilder report = report();
	   	  report.addColumn(nivelAtencionColumna);
	   	  report.addColumn(servicioColumna);
	   	  report.addColumn(serviciosMedicamentosColumna);
	   	  for (int i = 0; i < estadosAutoriz.size(); i++) {  
			  	   
       			TextColumnBuilder<Integer>    cantidadColumna 	=col.column("Cantidad "+estadosAutoriz.get(i).getDescripcion(),    "cantidad" + i, 	type.integerType()).setHorizontalAlignment(HorizontalAlignment.RIGHT).setWidth(60);
       			TextColumnBuilder<BigDecimal> valorColumna 		=col.column("Valor "+estadosAutoriz.get(i).getDescripcion(), "valor" + i, 	type.bigDecimalType()).setHorizontalAlignment(HorizontalAlignment.RIGHT).setWidth(90);
		  	   	  
	 	       	report.addColumn(cantidadColumna);
	 	       	report.addColumn(valorColumna);
          } 
	       	
	   	    //------------CONFIGURACIÓN GENERAL DEL REPORTE-----------------
   	        report.setPageFormat(PageType.LETTER, PageOrientation.PORTRAIT);
            report.setDataSource(crearDataSource());
            report.ignorePagination();
            report.title(this.detallesReporte.crearEncabezadoReporteArchivoPlanoDetalladoGrupoClase());
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
     		columnas[1]="servicio";
     		columnas[2]="medicamento";
     		
     		int contador=3;
     		for(int i=0 ; i<estadosAutoriz.size() ; i++)
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
    	    			datos[1]="";
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
    	    			datos[1]=dtoConsulta.getNombreServicio();
    	    			datos[2]="";
    	    			contador=3;
    	    			for(int i =0;i<totales.size();i++){
    	    				datos[contador]=totales.get(i);
    	    				contador++;
    	    			}
    	    			
    	    			dataSource.add(datos);
        			}
    			}
    			
    		}
			
    		
    		/*DataSource dataSource = new DataSource("nivelAtencion","servicio","medicamento","cantidad0","valor0","cantidad1","valor1","cantidad2","valor2");
			dataSource.add("Nivel Atención I","Consulta  odontología","", 1, new BigDecimal(100000), 1, new BigDecimal(10000), 1, new BigDecimal(100000));
			dataSource.add("Nivel Atención I","Radiografía cuello","", 2, new BigDecimal(20000), 1, new BigDecimal(10000), 1, new BigDecimal(1000));
			dataSource.add("Nivel Atención I","Consulta medicina general","", 3, new BigDecimal(300000), 1, new BigDecimal(10000), 1, new BigDecimal(100));
	         dataSource.add("Nivel Atención I","","Aguja desechable",  4, new BigDecimal(4000), 1, new BigDecimal(10000), 1, new BigDecimal(100));
	         dataSource.add("Nivel Atención II","Radiografía cuello","", 5, new BigDecimal(50000), 1, new BigDecimal(100), 1, new BigDecimal(100));
	         dataSource.add("Nivel Atención II","","Ibuprofeno",  7, new BigDecimal(70000), 1, new BigDecimal(10000), 1, new BigDecimal(500));*/
			
			return dataSource;
    	}
    	
}
