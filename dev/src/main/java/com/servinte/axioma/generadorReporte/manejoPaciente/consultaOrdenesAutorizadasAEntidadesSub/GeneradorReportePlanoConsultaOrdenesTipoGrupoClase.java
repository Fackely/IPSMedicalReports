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

public class GeneradorReportePlanoConsultaOrdenesTipoGrupoClase extends GeneradorReporteDinamico{

	
	private ArrayList<DtoIntegridadDominio> estadosAutoriz = null;
	private ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub> listadoResultado;
	private DtoBusquedaTotalOrdenesAutorizadasEntSub filtroConsultaOrdenes;
	private DetallesGeneralesFormatos detallesReporte;

	/**
	 * Constructor de la clase
	 */
	public GeneradorReportePlanoConsultaOrdenesTipoGrupoClase() {
	}  
	
	/**
	 * Constructor de la clase
	 */
	public GeneradorReportePlanoConsultaOrdenesTipoGrupoClase(ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub> listadoResultado, 
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
    	  TextColumnBuilder<String>     convenioColumna      				= col.column("Convenio", 		  	 "convenio",  			type.stringType());
		  TextColumnBuilder<String>     nivelAtencionColumna      			= col.column("Nivel de Atención", 	 "nivelAtencion",  		type.stringType());
	      TextColumnBuilder<String>     servicioColumna    					= col.column("Grupo de Servicios",	 "grupoServicio",      	type.stringType());
	      TextColumnBuilder<String>     serviciosMedicamentosColumna      	= col.column("Clase de Inventarios", "claseInventario", 	type.stringType());
	      //------------------------------------------------------
	      
	      //----------REPORTE GENERAL
	   	  JasperReportBuilder report = report();
	   	  report.addColumn(convenioColumna);
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
     		columnas[2]="grupoServicio";
     		columnas[3]="claseInventario";
     		
     		int contador=4;
     		for(int i=0 ; i<estadosAutoriz.size() ; i++)
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
    			if(dtoConsulta.getCodigoArticulo()!=null)
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
    	    						if(dtoConsultaInterno.getNombreConvenio().equals(dtoConsulta.getNombreConvenio())&&
    			    						dtoConsultaInterno.getNivelAtencion().equals(dtoConsulta.getNivelAtencion())
    			    						&&dtoConsultaInterno.getClaseInventario().equals(dtoConsulta.getClaseInventario())
    			    						&&dtoConsultaInterno.getEstadoAutorizacion().equals(filtroConsultaOrdenes.getNombresEstadosAutorizaciones().get(i).getAcronimo())
    			    						)
    			    				{
    			    						totales.add((dtoConsultaInterno.getCantidadArticulo()!=null?dtoConsultaInterno.getCantidadArticulo():0));
    			    						totales.add((dtoConsultaInterno.getValorArticulo()!=null?dtoConsultaInterno.getValorArticulo():new BigDecimal(0.0)));
    			    						convenioNivelClase.add(dtoConsulta.getNivelAtencion()+ConstantesBD.separadorSplit+
    			    								dtoConsultaInterno.getNombreConvenio()+ConstantesBD.separadorSplit+
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
    	    			datos[2]="";
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
    	    						if(dtoConsultaInterno.getNombreConvenio().equals(dtoConsulta.getNombreConvenio())&&
    			    						dtoConsultaInterno.getNivelAtencion().equals(dtoConsulta.getNivelAtencion())
    			    						&&dtoConsultaInterno.getGrupoServicio().equals(dtoConsulta.getGrupoServicio())
    			    						)
    			    				{
    			    						
    			    					if(dtoConsultaInterno.getEstadoAutorizacion().equals(filtroConsultaOrdenes.getNombresEstadosAutorizaciones().get(i).getAcronimo())
    			    							)
    			    					{
    			    						totales.add((dtoConsultaInterno.getCantidadServicio()!=null?dtoConsultaInterno.getCantidadServicio():0));
    			    						totales.add((dtoConsultaInterno.getValorServicio()!=null?dtoConsultaInterno.getValorServicio():new BigDecimal(0.0)));
    			    						convenioNivelGrupo.add(dtoConsulta.getNombreConvenio()+ConstantesBD.separadorSplit+
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
    	    			datos[2]=dtoConsulta.getGrupoServicio();
    	    			datos[3]="";
    	    			contador=4;
    	    			for(int i =0;i<totales.size();i++){
    	    				datos[contador]=totales.get(i);
    	    				contador++;
    	    			}
    	    			
    	    			dataSource.add(datos);
        			}
    			}
    			
    		}
    		
    		/*DataSource dataSource = new DataSource("convenio","nivelAtencion","grupoServicio","claseInventario","cantidad0","valor0","cantidad1","valor1","cantidad2","valor2");
			dataSource.add("Convenio 1","Nivel Atención I","Consulta","", 1, new BigDecimal(100000), 1, new BigDecimal(10000), 1, new BigDecimal(100000));
			dataSource.add("Convenio 2","Nivel Atención I","Cirugías","", 2, new BigDecimal(20000), 1, new BigDecimal(10000), 1, new BigDecimal(1000));
			dataSource.add("Convenio 3","Nivel Atención I","","Medicamentos", 3, new BigDecimal(300000), 1, new BigDecimal(10000), 1, new BigDecimal(100));
	         dataSource.add("Convenio 4","Nivel Atención I","","Laboratorio",  4, new BigDecimal(4000), 1, new BigDecimal(10000), 1, new BigDecimal(100));
	         dataSource.add("Convenio 5","Nivel Atención II","","Artículos", 5, new BigDecimal(50000), 1, new BigDecimal(100), 1, new BigDecimal(100));
	         dataSource.add("Convenio 6","Nivel Atención II","Consulta","",  7, new BigDecimal(70000), 1, new BigDecimal(10000), 1, new BigDecimal(500));*/
	         
			
			return dataSource;
    	}
    	
    	
    	
}

