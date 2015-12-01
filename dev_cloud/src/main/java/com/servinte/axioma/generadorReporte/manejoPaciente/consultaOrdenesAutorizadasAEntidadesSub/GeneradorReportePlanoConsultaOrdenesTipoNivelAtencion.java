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

public class GeneradorReportePlanoConsultaOrdenesTipoNivelAtencion extends GeneradorReporteDinamico{

	
	private ArrayList<DtoIntegridadDominio> estadosAutoriz = null;
	private ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub> listadoResultado;
	private DtoBusquedaTotalOrdenesAutorizadasEntSub filtroConsultaOrdenes;
	private DetallesGeneralesFormatos detallesReporte;

	/**
	 * Constructor de la clase
	 */
	public GeneradorReportePlanoConsultaOrdenesTipoNivelAtencion() {  
    }  
	
	/**
	 * Constructor de la clase
	 */
	public GeneradorReportePlanoConsultaOrdenesTipoNivelAtencion(ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub> listadoResultado, 
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
    	  TextColumnBuilder<String>     entidadSubColumna      				= col.column("Entidad Subcontratada", "entidadSub",  		type.stringType());
    	  TextColumnBuilder<String>     convenioColumna      				= col.column("Convenio", 			  "convenio",  			type.stringType());
		  TextColumnBuilder<String>     nivelAtencionColumna      			= col.column("Nivel de Atención", 	  "nivelAtencion",  	type.stringType());
	      TextColumnBuilder<String>     servicioColumna    					= col.column("Servicio",       	  	  "servicio",      		type.stringType());
	      TextColumnBuilder<String>     serviciosMedicamentosColumna      	= col.column("Medicamentos/Insumos",  "medicamento", 		type.stringType());
	      //------------------------------------------------------
	      
	      //----------REPORTE GENERAL
	   	  JasperReportBuilder report = report();
	   	  report.addColumn(entidadSubColumna);
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
            report.title(this.detallesReporte.crearEncabezadoReporteArchivoPlanoTipoNivelAtencion());
   	        return report;
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
    		
    		int columnasTotales=(estadosAutoriz.size()*2)+5;
     		String[] columnas = new String[columnasTotales];
     		columnas[0]="entidadSub";
     		columnas[1]="convenio";
     		columnas[2]="nivelAtencion";
     		columnas[3]="servicio";
     		columnas[4]="medicamento";
     		
     		int contador=5;
     		for(int i=0 ; i<estadosAutoriz.size() ; i++)
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
	    			datos[4]="";
	    			contador=5;
	    			for(int i =0;i<totalesServicios.size();i++){
	    				datos[contador]=totalesServicios.get(i);
	    				contador++;
	    			}
	    			
	    			Object[] datosM= new Object[columnasTotales];
	    			datosM[0]=dtoConsulta.getNombreEntidadSub();
	    			datosM[1]=dtoConsulta.getNombreConvenio();
	    			datosM[2]=dtoConsulta.getNivelAtencion();
	    			datosM[3]="";
	    			datosM[4]="Medicamentos/Insumos";
	    			contador=5;
	    			for(int i =0;i<totalesMedicamentos.size();i++){
	    				datosM[contador]=totalesMedicamentos.get(i);
	    				contador++;
	    			}
	    			dataSource.add(datos);
	    			dataSource.add(datosM);
    			}
    		}
    		
    		/*DataSource dataSource = new DataSource("entidadSub","convenio","nivelAtencion","servicio","medicamento","cantidad0","valor0","cantidad1","valor1","cantidad2","valor2");
			dataSource.add("Entidad sub 1","Convenio 1","Nivel Atención I","Servicios","", 1, new BigDecimal(100000), 1, new BigDecimal(10000), 1, new BigDecimal(100000));
			dataSource.add("Entidad sub 1","Convenio 1","Nivel Atención I","","Medicamentos/Insumos", 2, new BigDecimal(20000), 1, new BigDecimal(10000), 1, new BigDecimal(1000));
			dataSource.add("Entidad sub 2","Convenio 3","Nivel Atención I","Servicios","", 3, new BigDecimal(300000), 1, new BigDecimal(10000), 1, new BigDecimal(100));
	         dataSource.add("Entidad sub 1","Convenio 5","Nivel Atención I","","Medicamentos/Insumos",  4, new BigDecimal(4000), 1, new BigDecimal(10000), 1, new BigDecimal(100));
	         dataSource.add("Entidad sub 7","Convenio 6","Nivel Atención II","Servicios","", 5, new BigDecimal(50000), 1, new BigDecimal(100), 1, new BigDecimal(100));
	         dataSource.add("Entidad sub 6","Convenio 7","Nivel Atención II","","Medicamentos/Insumos",  7, new BigDecimal(70000), 1, new BigDecimal(10000), 1, new BigDecimal(500));*/
			
			return dataSource;
    	}
    	
}
