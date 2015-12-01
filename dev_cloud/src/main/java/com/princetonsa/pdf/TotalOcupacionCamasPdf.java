package com.princetonsa.pdf;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import util.ConstantesIntegridadDominio;
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.Utilidades;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseTable;
import com.lowagie.text.BadElementException;
import com.princetonsa.actionform.manejoPaciente.TotalOcupacionCamasForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.TotalOcupacionCamas;

/**
 * 
 * Clase para generar el total de ocupacion de camas
 * segun los estados de camas seleccionados por el usuario
 * @author Jhony Alexander Duque
 *
 */

public class TotalOcupacionCamasPdf 
{
	/**
     * Manejo de logs
     */
    private static Logger logger=Logger.getLogger(TotalOcupacionCamasPdf.class);
    
    public static void imprimir (Connection connection,String nombrePdf, TotalOcupacionCamasForm forma,UsuarioBasico usuario, HttpServletRequest request) throws BadElementException
    {
    	logger.info("\n entre a ------------ imprimir------------");
    	PdfReports report = new PdfReports("true",true);
    	//logger.info("\n datosssss ---> "+forma.getDatosConsulta());
		int numRegistros = Utilidades.convertirAEntero(forma.getDatosConsulta("numRegistros")+"");
	
		report.setUsuario(usuario.getLoginUsuario());
		
	    //logger.info("el valor de hashmap "+forma.getCensoCamasMap());  
		//GENERACION DE CABECERA
		//Ruta por defecto donde se genera los PDF
		//String filePath=ValoresPorDefecto.getReportPath();
		
		//*******SE CARGAN LOS DATOS DE LA INSTITUCION*******************************+
		InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
        //*********************************************************************************
		
		String tituloDocumentos="TOTAL OCUPACION CAMAS";
		
		String criterios = "";
		criterios=cargarCriteriosMostrar(connection, forma, usuario);
		// semiran los estados por los cuales se busco
		//-----------------------------------------------------------------------
		String tmp=TotalOcupacionCamas.organizarEstadosBuscar(forma.getInicialMap());
    	String estadosCama [] = tmp.split(",");
    	//-----------------------------------------------------------------------
    	//se miran ciantos tienen detalle
    	ArrayList detallado = TotalOcupacionCamas.organizarDetalle(forma.getInicialMap());
    	//se miran cuales son los tipos de habitacion
    	ArrayList tiposHabitacion= Utilidades.obtenerTiposHabitacion(connection, usuario.getCodigoInstitucion());
    	
    	
    	
    	int columnas=estadosCama.length+2;
    	int filas=2+numRegistros;
		report.setReportBaseHeaderReportes(institucionBasica, tituloDocumentos,criterios,ConstantesIntegridadDominio.acronimoUbicacionDerecha);
		

		
		//abrir reporte
        report.openReport(nombrePdf);
        

        iTextBaseTable section;
        //aqui se crea la seccion donde se va a mostrar los datos del reporte
        
        section = report.createSection("informe", "descripcionTable",  filas, columnas, 10);
        section.setTableBorder("descripcionTable", 0xFFFFFF, 0.0f);
	    section.setTableCellBorderWidth("descripcionTable", 0.5f);
	    section.setTableCellPadding("descripcionTable", 1);
	    section.setTableSpaceBetweenCells("descripcionTable", 0.5f);
	    section.setTableCellsDefaultColors("descripcionTable", 0xCCCCCC, 0x000000);
	    float widths[]= new float [columnas];
	    widths[0]=0.6f;
	    widths[1]=0.4f;
	    
	    for (int i=2; i<columnas;i++)
	    	widths[i]=(9f/columnas);
	    
	  //  for (int i=0; i<widths.length;i++)
	    //   logger.info("\n los widths -->"+widths[i]);
	    
	    section.getTableReference("descripcionTable").setWidths(widths);
	    /*-----------------------------------------------------------------------------------------------------
	     *							 SECCION UTILIZADA PARA EL MONTAJE DEL ENCABEZADO
	     ------------------------------------------------------------------------------------------------------*/
	  
		   report.font.setFontAttributes(0x000000, 7, true, false, false);
		   section.setTableCellsRowSpan(2);
		   
		   section.addTableTextCellAligned("descripcionTable", "PISO", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("descripcionTable", "TOTAL", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    //aqui se ponen los estado ya que son dinamicos
		    logger.info("\n  ----1 ---------");
		    section.setTableCellsRowSpan(1);
		  
		    //se colocan las columnas con los nombres los estados de la cama (dinamico)
		    for (int i=0;i<estadosCama.length;i++)
		    		section.addTableTextCellAligned("descripcionTable", Utilidades.obtenerNombreEstadoCama(connection,estadosCama[i]).toUpperCase(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.setTableCellsDefaultColors("descripcionTable", 0x8BB8D6, 0x000000);
		    //se crea una tabla con los tipos de habitacion para 
		    //mostrar como detalle de cada estado de la cama, 
		    //esto es soli si se escoge detallar tipo de habitacion
		    //de lo contrario solo van total y % (Dinamico)
		    for (int i=0;i<estadosCama.length;i++)
		    { 
		    	//se pregunta si es detallado 
	    		if(TotalOcupacionCamas.esDetallado(detallado, estadosCama[i]))
	    		{
	    			//se crea una tabla en donde se va a crear el detalle
	    			section.useTable("detalle_"+i, tiposHabitacion.size()+1);
	    			for (int j=0;j<tiposHabitacion.size();j++)
	    			{
	    				//se añaden las celdas con los nombres del tipo de habitacion
	    				HashMap dato = (HashMap)tiposHabitacion.get(j);
	    				String tipoHabitacion = (dato.get("nombre")+"").toUpperCase();
	    				if (tipoHabitacion.length()>3)
	    					tipoHabitacion=tipoHabitacion.substring(0, 3);
	    				section.addTableTextCellAligned("detalle_"+i, tipoHabitacion, report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    			}
	    			//se añade la celda %
	    			section.addTableTextCellAligned("detalle_"+i, "%", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    		}
	    		else
	    		{
	    			//esto en el caso en que no se seleccion detallado por tipo habitacion
	    			//se crea una tabla
	    			section.useTable("detalle_"+i, 2);
	    			//se añaden las celdas total y %
	    			section.addTableTextCellAligned("detalle_"+i, "TOTAL", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    			section.addTableTextCellAligned("detalle_"+i, "%", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    		}
	    		//se añade la nueva tabla a la tabla padre.
	    		section.insertTableIntoCell("descripcionTable", "detalle_"+i, 1, (2+i));
	    		
	    		
		    }
		    
		    //se coloca el color normal osea sin color en el fondo.
		    section.setTableCellsDefaultColors("descripcionTable", 0xFFFFFF, 0x000000);
		    /*-----------------------------------------------------------------------------------------------------
		     * 						FIN SECCION UTILIZADA PARA EL MONTAJE DEL ENCABEZADO
		     ------------------------------------------------------------------------------------------------------*/
        
		    /*-----------------------------------------------------------------------------------------------------
		     * 							SECCION DONDE SE CALCULAN LOS TOTALES
		     ------------------------------------------------------------------------------------------------------*/
        
		    
        
		    for (int k=0; k<numRegistros;k++)
		    {
		    	 int totalpiso=0;
		    	 float porcentaje=0;
		    	 
				
				    totalpiso=cantidadCamasPorPiso(estadosCama, detallado, tiposHabitacion, forma.getDatosConsulta(), k);
				  
				    forma.setDatosConsulta("totalPiso_"+k, totalpiso);
				   for (int i=0;i<estadosCama.length;i++)
				    { 
					   float cantEstCama=0;
				    	//se pregunta si es detallado 
			    		if(TotalOcupacionCamas.esDetallado(detallado, estadosCama[i]))
			    		{
			    			//se crea una tabla en donde se va a crear el detalle
			    			section.useTable("detalleDatos_"+k+"_"+i, tiposHabitacion.size()+1);
			    			for (int j=0;j<tiposHabitacion.size();j++)
			    			{
			    				
			    				cantEstCama=cantEstCama+Utilidades.convertirAEntero(forma.getDatosConsulta("cantidad_"+i+"_"+j+"_"+k)+"");
			    				
			    			}
			    			
			    			if (totalpiso>0)
			    				porcentaje=((cantEstCama*100)/totalpiso);
			    			else
			    				porcentaje=0;
			    			
			    			forma.setDatosConsulta("porcentaje_"+i+"_"+k, UtilidadTexto.formatearValores(porcentaje,"0.00"));
			    		}
			    		else
			    		{
		    				cantEstCama=Utilidades.convertirAEntero(forma.getDatosConsulta("cantidad_"+i+"_"+k)+"");
			    			
			    			if (totalpiso>0)
			    				porcentaje=((cantEstCama*100)/totalpiso);
			    			else
			    				porcentaje=0;
			    			
			    			forma.setDatosConsulta("porcentaje_"+i+"_"+k,UtilidadTexto.formatearValores(porcentaje,"0.00"));
			    		}
				    }
		    	
		   
		    }
		    
		    cantidadCamasPorPiso2(estadosCama, detallado, tiposHabitacion, forma);
		    /*-----------------------------------------------------------------------------------------------------
		     * 							FIN SECCION DONDE SE CALCULAN LOS TOTALES
		     ------------------------------------------------------------------------------------------------------*/
		    
		    
		    
		    
		    /*-----------------------------------------------------------------------------------------------------
		     * 						EN ESTA SECCION DE AGREGAN LOS DATOS AL REPORTE
		     ------------------------------------------------------------------------------------------------------*/
		    //se itera el resultado de la consulta
		    int columnDet=2;
		    logger.info("\n los dtos son -->"+forma.getDatosConsulta());
		    for (int k=0; k<numRegistros;k++)
		    {
		    	   report.font.setFontAttributes(0x000000, 7, true, false, false);
				   section.addTableTextCellAligned("descripcionTable", (forma.getDatosConsulta("nombre_piso1_"+k)+"").toLowerCase(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
				    section.addTableTextCellAligned("descripcionTable", forma.getDatosConsulta("totalPiso_"+k)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    //aqui se ponen los estado ya que son dinamicos
				    logger.info("\n  ----1 ---------");
				
				    
				     //se crea una tabla con los tipos de habitacion para 
				    //mostrar como detalle de cada estado de la cama, 
				    //esto es soli si se escoge detallar tipo de habitacion
				    //de lo contrario solo van total y % (Dinamico)
				   for (int i=0;i<estadosCama.length;i++)
				    { 
				    	//se pregunta si es detallado 
			    		if(TotalOcupacionCamas.esDetallado(detallado, estadosCama[i]))
			    		{
			    			//se crea una tabla en donde se va a crear el detalle
			    			section.useTable("detalleDatos_"+k+"_"+i, tiposHabitacion.size()+1);
			    			for (int j=0;j<tiposHabitacion.size();j++)
			    			{
			    				
			    				section.addTableTextCellAligned("detalleDatos_"+k+"_"+i, forma.getDatosConsulta("cantidad_"+i+"_"+j+"_"+k)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    			}
			    			//se añade la celda %
			    			section.addTableTextCellAligned("detalleDatos_"+k+"_"+i, forma.getDatosConsulta("porcentaje_"+i+"_"+k)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    		}
			    		else
			    		{
			    			//esto en el caso en que no se seleccion detallado por tipo habitacion
			    			//se crea una tabla
			    			section.useTable("detalleDatos_"+k+"_"+i, 2);
			    			//se añaden las celdas total y %
			    			section.addTableTextCellAligned("detalleDatos_"+k+"_"+i,  forma.getDatosConsulta("cantidad_"+i+"_"+k)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    			section.addTableTextCellAligned("detalleDatos_"+k+"_"+i, forma.getDatosConsulta("porcentaje_"+i+"_"+k)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    		}
			    		//se añade la nueva tabla a la tabla padre.
			    		section.insertTableIntoCell("descripcionTable", "detalleDatos_"+k+"_"+i, (columnDet+k), (2+i));
				    }
		    }
		    

		   	 // -------------------------------------------------------------------
   		//aqui se mete la seccion de los totales
   	   //-------------------------------------------------------------------
   		
   		   section.addTableTextCellAligned("descripcionTable", "TOTALES", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
   		   section.addTableTextCellAligned("descripcionTable", totalPiso(estadosCama, detallado, tiposHabitacion, forma.getDatosConsulta())+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
   		
   		   for (int i=0;i<estadosCama.length;i++)
			    { 
			    	//se pregunta si es detallado 
		    		if(TotalOcupacionCamas.esDetallado(detallado, estadosCama[i]))
		    		{
		    			//se crea una tabla en donde se va a crear el detalle
		    			section.useTable("totales_"+i, tiposHabitacion.size()+1);
		    			for (int j=0;j<tiposHabitacion.size();j++)
		    			{
		    				
		    				section.addTableTextCellAligned("totales_"+i, forma.getDatosConsulta("totales_"+i+"_"+j+"_"+numRegistros)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    			}
		    			//se añade la celda %
		    			section.addTableTextCellAligned("totales_"+i, forma.getDatosConsulta("porcentaje_"+i+"_"+numRegistros )+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    		}
		    		else
		    		{
		    			//esto en el caso en que no se seleccion detallado por tipo habitacion
		    			//se crea una tabla
		    			section.useTable("totales_"+i, 2);
		    			//se añaden las celdas total y %
		    			section.addTableTextCellAligned("totales_"+i,  forma.getDatosConsulta("totales_"+i+"_"+numRegistros)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    			section.addTableTextCellAligned("totales_"+i, forma.getDatosConsulta("porcentaje_"+i+"_"+numRegistros)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    		}
		    		//se añade la nueva tabla a la tabla padre.
		    		section.insertTableIntoCell("descripcionTable", "totales_"+i, ((columnDet+numRegistros)), (2+i));
			    }
		    
		    /*-----------------------------------------------------------------------------------------------------
		     * 						FIN DE SECCION DONDE SE AGREGRAN LOS DATOS AL REPORTE
		     ------------------------------------------------------------------------------------------------------*/
		    
		    
	    //se añade la sección al documento
		    report.addSectionToDocument("informe");
	   // report.addSectionToDocument("resumen");
	    
	    
	    
	    report.closeReport();
	    
	    logger.info("\n *********** fin reporte ------------");
    }
    
    
    
    public static String cargarCriteriosMostrar (Connection connection,TotalOcupacionCamasForm forma,UsuarioBasico usuario)
    {
    	logger.info("\n *********** cargarCriteriosMostrar  ------------");
    	String criterios = "";
    	    	
    	   	
    	if (UtilidadCadena.noEsVacio(forma.getCodigoCentroAtencion()))
    		criterios=" Centro Atención: "+Utilidades.obtenerNombreCentroAtencion(connection, Utilidades.convertirAEntero(forma.getCodigoCentroAtencion()));
   
    	if (UtilidadTexto.getBoolean(forma.getIncluirCamas()))
    		criterios+="  Se incluyeron las Camas de Urgencias en esta consulta.";
    	else
    		criterios+="  No se incluyeron las Camas de Urgencias en esta consulta.";
    	
    	//se cargan los estados por los cuales se va a buscar
    	//----------------------------------------------------------------------
    	String tmp=TotalOcupacionCamas.organizarEstadosBuscar(forma.getInicialMap());
    	String estadosCama [] = tmp.split(",");
    	//-----------------------------------------------------------------------
    	//se cargan los que necesitan reporte detallado
    	ArrayList detallado = TotalOcupacionCamas.organizarDetalle(forma.getInicialMap());
    	
    	
    	String estados = "";
		int numReg = estadosCama.length;
		logger.info("\n numRegistros -->"+estadosCama.length);
		if (numReg==1)
		{
			if(TotalOcupacionCamas.esDetallado(detallado, estadosCama[0]))
				criterios+= "  Estado Cama: "+Utilidades.obtenerNombreEstadoCama(connection,estadosCama[0])+" [detallado]";
			else
				criterios+= "  Estado Cama: "+Utilidades.obtenerNombreEstadoCama(connection,estadosCama[0]);
			
		}
		else
		{
			if (numReg>1)
				for (int i=0;i<numReg;i++)
					if (i==0)
					{
						if(TotalOcupacionCamas.esDetallado(detallado, estadosCama[i]))
							estados=Utilidades.obtenerNombreEstadoCama(connection,estadosCama[i])+" [detallado]";
						else
							estados=Utilidades.obtenerNombreEstadoCama(connection,estadosCama[i]);
					}
					else
					{
						if(TotalOcupacionCamas.esDetallado(detallado, estadosCama[i]))
							estados+=", "+Utilidades.obtenerNombreEstadoCama(connection,estadosCama[i])+" [detallado]";
						else
							estados+=", "+Utilidades.obtenerNombreEstadoCama(connection,estadosCama[i]);
							
					}
			
			criterios+="  Estados Cama: "+estados;
			
		}
    	
    	return criterios;
    }
    /**
     * Metodo encargado de contar la cantidad de camas por piso
     * @param estadosCama
     * @param detallado
     * @param tiposHabitacion
     * @param mapa
     * @param k
     * @return
     */
    public static int cantidadCamasPorPiso (String [] estadosCama, ArrayList detallado,ArrayList<HashMap<String, Object>> tiposHabitacion,HashMap mapa,int k)
    {
    	int total=0;
    	
		   for (int i=0;i<estadosCama.length;i++)
		    { 
		    	//se pregunta si es detallado 
	    		if(TotalOcupacionCamas.esDetallado(detallado, estadosCama[i]))
	    		{
	    			for (int j=0;j<tiposHabitacion.size();j++)
	    				total=total+Utilidades.convertirAEntero(mapa.get("cantidad_"+i+"_"+j+"_"+k)+"");
	    		}
	    		else
	    			total=total+Utilidades.convertirAEntero(mapa.get("cantidad_"+i+"_"+k)+"");
		    }
    	
    	return total;
    }
    
    /**
     * Metodo encargado de totalizar la cantidad de camas en todos los pisos
     * @param estadosCama
     * @param detallado
     * @param tiposHabitacion
     * @param mapa
     * @return
     */
    public static int totalPiso (String [] estadosCama, ArrayList detallado,ArrayList<HashMap<String, Object>> tiposHabitacion,HashMap mapa)
    {
    	int numReg=Utilidades.convertirAEntero(mapa.get("numRegistros")+"");
    	int totalPiso=0;
    	
    	for (int k=0;k<numReg;k++)
    			totalPiso=totalPiso+Utilidades.convertirAEntero(mapa.get("totalPiso_"+k)+"");
    	return totalPiso;
    }
    
     
    public static void cantidadCamasPorPiso2 (String [] estadosCama, ArrayList detallado,ArrayList<HashMap<String, Object>> tiposHabitacion,TotalOcupacionCamasForm forma)
    {
    	int numReg =Utilidades.convertirAEntero(forma.getDatosConsulta("numRegistros")+"");
    	
    	float cantidadTotalCamasPispos=totalPiso(estadosCama, detallado, tiposHabitacion, forma.getDatosConsulta());
    	float porcentaje=0;
			  // section.addTableTextCellAligned("descripcionTable", (forma.getDatosConsulta("nombre_piso1_"+k)+"").toLowerCase(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    //section.addTableTextCellAligned("descripcionTable", forma.getDatosConsulta("totalPiso_"+k)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    //aqui se ponen los estado ya que son dinamicos
			    logger.info("\n  ----1 ---------");
			
			    
			     //se crea una tabla con los tipos de habitacion para 
			    //mostrar como detalle de cada estado de la cama, 
			    //esto es soli si se escoge detallar tipo de habitacion
			    //de lo contrario solo van total y % (Dinamico)
			   for (int i=0;i<estadosCama.length;i++)
			    { 
				   float canTotxTipoH=0;
			    	//se pregunta si es detallado 
		    		if(TotalOcupacionCamas.esDetallado(detallado, estadosCama[i]))
		    		{
		    			//se crea una tabla en donde se va a crear el detalle
		    			for (int j=0;j<tiposHabitacion.size();j++)
		    			{
		    				 int cant=0;
		    				 for (int k=0; k<numReg;k++)
		    					 	cant=cant+Utilidades.convertirAEntero( forma.getDatosConsulta("cantidad_"+i+"_"+j+"_"+k)+"");

		    				 forma.setDatosConsulta("totales_"+i+"_"+j+"_"+numReg, cant);
		    				 canTotxTipoH=canTotxTipoH+cant;
		    			}
		    			if (cantidadTotalCamasPispos>0)
		    				porcentaje=((canTotxTipoH/cantidadTotalCamasPispos)*100);
		    			else
		    				porcentaje=0;
		    			//se añade la celda %
		    			forma.setDatosConsulta("porcentaje_"+i+"_"+numReg, UtilidadTexto.formatearValores(porcentaje,"0.00"));
		    		//	forma.getDatosConsulta("porcentaje_"+i+"_"+k);
		    			//section.addTableTextCellAligned("detalleDatos_"+k+"_"+i, forma.getDatosConsulta("porcentaje_"+i+"_"+k)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    		
		    		}
		    		else
		    		{
		    			//esto en el caso en que no se seleccion detallado por tipo habitacion
		    			//se crea una tabla
		    			 int cant=0;
	    				 for (int k=0; k<numReg;k++)
	    					 	cant=cant+Utilidades.convertirAEntero( forma.getDatosConsulta("cantidad_"+i+"_"+k)+"");

	    				 forma.setDatosConsulta("totales_"+i+"_"+numReg, cant);
	    				 canTotxTipoH=canTotxTipoH+cant;
		    			
	    				 if (cantidadTotalCamasPispos>0)
			    				porcentaje=((canTotxTipoH/cantidadTotalCamasPispos)*100);
			    			else
			    				porcentaje=0;
			    			//se añade la celda %
			    			forma.setDatosConsulta("porcentaje_"+i+"_"+numReg,UtilidadTexto.formatearValores(porcentaje,"0.00"));
			    			
		    		}
			    }
    }
    
    
    
}