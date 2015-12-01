/*
 * Ago 28, 2008
 */
package com.princetonsa.pdf;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.CsvFile;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;
import util.pdf.iTextBaseTable;

import com.princetonsa.actionform.manejoPaciente.ReporteMortalidadForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.consultaExterna.ReporteEstadisticoConsultaEx;
import com.princetonsa.mundo.manejoPaciente.ReporteMortalidad;

/**
 * 
 * Clase usada para generar reportes de Cancelacion Cita por Paciente o Institucion
 * @author 
 *
 */
public class CancelacionCitaPacienteInstitu 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(ReporteMortalidadPdf.class);
	
	/**
	 * Genera el pdf
	 * @param Connection con
	 * @param String pathArchivo
	 * @param String nombreArchivo
	 * @param UsuarioBasico usuario
	 * @param HttpServletRequest request
	 * */
	public static HashMap pdfCancelacionCita(
			Connection con,
			String pathArchivo,			
			int codigoReporte,
			HashMap mapaBusqueda,
			UsuarioBasico usuario, 
			HttpServletRequest request)
	{
		String tituloDespacho = "" ;
		InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		String nombreArchivo = "";
		Random r=new Random();
		HashMap condicionesWhere = ReporteEstadisticoConsultaEx.getCondicionesBuquedaReporte(codigoReporte,mapaBusqueda);
		HashMap respuesta = new HashMap();
		HashMap datosBaseMap = new HashMap();
		
		//Valida el tipo de reporte a generar
		if(codigoReporte == ConstantesBD.codigoReporteMotivoCancelacionCitaPaciente)
		{
			tituloDespacho = "CANCELACIÓN CITA POR PACIENTE";			
			nombreArchivo  = "cancelacionCitaPaciente" + r.nextInt()  +".pdf";			
		}
		else if(codigoReporte == ConstantesBD.codigoReporteMotivoCancelacionCitaInstitucion)
		{
			tituloDespacho = "CANCELACIÓN CITA POR INSTITUCIÓN";
			nombreArchivo  = "cancelacionCitaInstitucion" + r.nextInt()  +".pdf";			
		}
		else
			return new HashMap();
		
		//Se pone el valor del nombre del archivo
		respuesta.put("nombreArchivo",nombreArchivo);
		String filename = pathArchivo + System.getProperty("file.separator") + nombreArchivo;			
		//*************IMPRESION DEL ENCABEZADO************************************************
    	PdfReports report = new PdfReports("true",true,usuario.getLoginUsuario());        
        String filePath=ValoresPorDefecto.getFilePath();        
        
        filePath = filePath.substring(0, filePath.lastIndexOf("/", (filePath.length()-2)));
		report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), tituloDespacho);
		
        //Se abre el reporte
        report.openReport(filename);
        report.font.useFont(iTextBaseFont.FONT_TIMES, true, true, true);
        iTextBaseTable section;
        
        //**************SECCION ENCABEZADO (Se muestran los parámetros de busqueda)*********************************************************************
        section = report.createSection("encabezado", "encabezadoTable", 1, 2, 10);
  		section.setTableBorder("encabezadoTable", 0x000000, 0.0f);
	    section.setTableCellBorderWidth("encabezadoTable", 0.0f);
	    //Espaciado entre la sección actual y la anterior
	    section.setTableOffset("encabezadoTable", 0.0f);
	    section.setTableCellPadding("encabezadoTable", 1);
	    section.setTableSpaceBetweenCells("encabezadoTable", 0.1f);
	    section.setTableCellsDefaultColors("encabezadoTable", 0xFFFFFF, 0xFFFFFF);
	    
	    section.setTableCellsColSpan(2);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.addTableTextCellAligned("encabezadoTable", "Párametros de Búsqueda: "+condicionesWhere.get("parametrosbusqueda").toString(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);	    
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    
	    //se añade la sección al documento
	    report.addSectionToDocument("encabezado");
	    //********************************************************************************************************************
	    
	    //***********SECCION DETALLE*****************************************************************************************
	    datosBaseMap = ReporteEstadisticoConsultaEx.getCargarDatosBasicosCancelacionCitas(con,condicionesWhere.get("where").toString(),condicionesWhere.get("where0").toString(),false);
	    
	    HashMap encabezadoColumnaMap = new HashMap();
	    HashMap encabezadoFilaMap = new HashMap();
	    HashMap cantidadesColumnaMap = new HashMap();
	    HashMap cantidadesColumnaReprogramadaMap = new HashMap();
	    HashMap cantidadesFilaMap = new HashMap();
	    HashMap cantidadesFilaReprogramadaMap = new HashMap();
	    int numeroFilas = 0;
	    int numeroColumnas = 0;
	    int cantidadTmp = 0;
	    double promedio = 0;
	    
	    if(!datosBaseMap.get("numRegistros").toString().equals("0") 
	    		&& !datosBaseMap.get("codigoMotivo_0").toString().equals(ConstantesBD.codigoNuncaValido+""))
	    {	    
		    encabezadoColumnaMap = getDatosAgrupadosMap(datosBaseMap,"codigoMotivo_","codigoMotivo_","descripcionMotivo_",ConstantesBD.codigoNuncaValido+"");
		    encabezadoFilaMap = getDatosAgrupadosMap(datosBaseMap,"codigoEspecialidad_","codigoMotivo_","descripcionEspecialidad_",ConstantesBD.codigoNuncaValido+"");
		    cantidadesColumnaMap = getCantidadesFilaColumna(encabezadoColumnaMap,datosBaseMap,"codigoMotivo_","codigoCita_",false);		 
		    cantidadesColumnaReprogramadaMap = getCantidadesFilaColumna(encabezadoColumnaMap,datosBaseMap,"codigoMotivo_","codigoCita_",true);
		    cantidadesFilaMap = getCantidadesFilaColumna(encabezadoFilaMap,datosBaseMap,"codigoEspecialidad_","codigoCita_",false);
		    cantidadesFilaReprogramadaMap = getCantidadesFilaColumna(encabezadoFilaMap,datosBaseMap,"codigoEspecialidad_","codigoCita_",true);
		    
		    numeroFilas = Utilidades.convertirAEntero(encabezadoFilaMap.get("numRegistros").toString()) + 5;
		    numeroColumnas = (Utilidades.convertirAEntero(cantidadesColumnaMap.get("numRegistros").toString())*2) + 5;
		    cantidadTmp = 0;
		    promedio = 0;
	   
		    //********************************************************************************************************************		    
		    	        
			section = report.createSection("reporte","reporteTable",numeroFilas,numeroColumnas,0.1f);
	  		section.setTableBorder("reporteTable", 0x000000, 0.01f);
		    section.setTableCellBorderWidth("reporteTable", 0.01f);
		    //Espaciado entre la sección actual y la anterior
		    section.setTableOffset("reporteTable", 5.0f);
		    section.setTableCellPadding("reporteTable", 1.0f);
		    section.setTableSpaceBetweenCells("reporteTable", 0.0f);
		    section.setTableCellsDefaultColors("reporteTable", 0xFFFFFF, 0x000000);
		    
		    //**********SE DIBUJAN LOS TÍTULOS DE LAS COLUMNAS******************************************************************
		    report.font.setFontAttributes(0x000000, 8, true, false, false);
		    section.setTableCellsColSpan(1);
		    section.addTableTextCellAligned("reporteTable","", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    
		    report.font.setFontAttributes(0x000000, 8, true, false, false);
		    section.setTableCellsColSpan((Utilidades.convertirAEntero(cantidadesColumnaMap.get("numRegistros").toString())*2));
		    section.addTableTextCellAligned("reporteTable","MOTIVO CANCELACIÓN", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
		    
		    report.font.setFontAttributes(0x000000, 8, true, false, false);
		    section.setTableCellsColSpan(4);	    
		    section.addTableTextCellAligned("reporteTable","TOTAL", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
		    
		    report.font.setFontAttributes(0x000000, 8, true, false, false);
		    section.setTableCellsColSpan(1);	    
		    section.addTableTextCellAligned("reporteTable","ESPECIALIDAD", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
	
		    //Recorre los motivos de cancelacion que son las columnas del reporte
		    for(int i = 0; i < Utilidades.convertirAEntero(encabezadoColumnaMap.get("numRegistros").toString()); i++ )
		    {
		    	if(!encabezadoColumnaMap.get("descripcionEncabezado_"+i).toString().equals(ConstantesBD.codigoNuncaValido+""))
		    	{
			    	report.font.setFontAttributes(0x000000, 8, true, false, false);
				    section.setTableCellsColSpan(1);	    
				    section.addTableTextCellAligned("reporteTable",encabezadoColumnaMap.get("descripcionEncabezado_"+i).toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
				    
				    report.font.setFontAttributes(0x000000, 8, true, false, false);
				    section.setTableCellsColSpan(1);	    
				    section.addTableTextCellAligned("reporteTable","%", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);	    	
		    	}
		    }
		    
		    report.font.setFontAttributes(0x000000, 8, true, false, false);
		    section.setTableCellsColSpan(1);	    
		    section.addTableTextCellAligned("reporteTable","Citas Cancel.", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
		    
		    report.font.setFontAttributes(0x000000, 8, true, false, false);
		    section.setTableCellsColSpan(1);	    
		    section.addTableTextCellAligned("reporteTable","% Citas Cancel.", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
		    
		    report.font.setFontAttributes(0x000000, 8, true, false, false);
		    section.setTableCellsColSpan(1);	    
		    section.addTableTextCellAligned("reporteTable","Citas Reprog.", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
		    
		    report.font.setFontAttributes(0x000000, 8, true, false, false);
		    section.setTableCellsColSpan(1);	    
		    section.addTableTextCellAligned("reporteTable","% Citas Reprog.", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
		    
		    //Inserta el Detalle
		    for(int i = 0; i < Utilidades.convertirAEntero(encabezadoFilaMap.get("numRegistros").toString()); i++ )
		    {
		    	//Nombre del especialidad
		    	report.font.setFontAttributes(0x000000, 8, true, false, false);
		 	    section.setTableCellsColSpan(1);	    
		 	    section.addTableTextCellAligned("reporteTable",encabezadoFilaMap.get("descripcionEncabezado_"+i).toString().toUpperCase(),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		 	    
		 	    cantidadTmp = 0;
		 	    
			    for(int j = 0; j< Utilidades.convertirAEntero(encabezadoColumnaMap.get("numRegistros").toString()); j++)
			    {		    	
			    	cantidadTmp = Utilidades.convertirAEntero(getvalorMapaDatos(datosBaseMap,"codigoCita_",encabezadoFilaMap.get("codigoEncabezado_"+i).toString(),encabezadoColumnaMap.get("codigoEncabezado_"+j).toString()));
			    	report.font.setFontAttributes(0x000000, 8, false, false, false);
				    section.setTableCellsColSpan(1);	    
				    section.addTableTextCellAligned("reporteTable",cantidadTmp>0?cantidadTmp+"":"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
				    
				    //Motivo de cancelación
				    if(Utilidades.convertirAEntero(cantidadesFilaMap.get("cantidad_"+i).toString()) > 0 && cantidadTmp > 0)
				    	promedio = (cantidadTmp / Utilidades.convertirADouble(cantidadesFilaMap.get("cantidad_"+i).toString()))*100 ;
				    else	
				    	promedio = 0;
				    
				    report.font.setFontAttributes(0x000000, 8,false, false, false);
				    section.setTableCellsColSpan(1);	    
				    section.addTableTextCellAligned("reporteTable",promedio>0?UtilidadTexto.formatearValores(promedio,"0.00"):"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);		    	
			    }
			    
			    //Citas Canceladas
			    report.font.setFontAttributes(0x000000, 8, true, false, false);
			    section.setTableCellsColSpan(1);	    
			    if(Utilidades.convertirAEntero(cantidadesFilaMap.get("cantidad_"+i).toString()) > 0)
			    	section.addTableTextCellAligned("reporteTable",cantidadesFilaMap.get("cantidad_"+i).toString(), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
			    else
			    	section.addTableTextCellAligned("reporteTable","", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
			    			    
			    //% Citas Canceladas
			    if(Utilidades.convertirAEntero(cantidadesFilaMap.get("cantidad_"+i).toString()) > 0 && 
			    		Utilidades.convertirAEntero(cantidadesColumnaMap.get("sumaTotal").toString()) > 0)
			    	promedio = (Utilidades.convertirADouble(cantidadesFilaMap.get("cantidad_"+i).toString()) / Utilidades.convertirADouble(cantidadesColumnaMap.get("sumaTotal").toString())) * 100;
			    else	
			    	promedio = 0;			    
			    
			    report.font.setFontAttributes(0x000000, 8, true, false, false);
			    section.setTableCellsColSpan(1);	    
			    section.addTableTextCellAligned("reporteTable",promedio>0?UtilidadTexto.formatearValores(promedio,"0.00"):"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
			    
			    //Citas Reprogramadas		
			    report.font.setFontAttributes(0x000000, 8, true, false, false);
			    section.setTableCellsColSpan(1);		    		    
			    section.addTableTextCellAligned("reporteTable",cantidadesFilaReprogramadaMap.get("cantidad_"+i).toString(), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
			    		    		   
			    //% Citas Reprogramadas		  
			    if(Utilidades.convertirAEntero(cantidadesFilaReprogramadaMap.get("cantidad_"+i).toString()) > 0 && 
			    		Utilidades.convertirAEntero(cantidadesColumnaReprogramadaMap.get("sumaTotal").toString()) > 0)
			    	promedio = (Utilidades.convertirADouble(cantidadesFilaReprogramadaMap.get("cantidad_"+i).toString()) / Utilidades.convertirADouble(cantidadesColumnaReprogramadaMap.get("sumaTotal").toString())) * 100;
			    else	
			    	promedio = 0;			    
			    
			    report.font.setFontAttributes(0x000000, 8, true, false, false);
			    section.setTableCellsColSpan(1);	    
			    section.addTableTextCellAligned("reporteTable",promedio>0?UtilidadTexto.formatearValores(promedio,"0.00"):"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);		   	    
		    }
		    
		    //Total Citas Canceladas
	    	report.font.setFontAttributes(0x000000, 8, true, false, false);
	 	    section.setTableCellsColSpan(1);	    
	 	    section.addTableTextCellAligned("reporteTable","Total Citas Canceladas",report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	 	    
	 	    cantidadTmp = 0;
	 	    int valorG = (Utilidades.convertirAEntero(cantidadesColumnaReprogramadaMap.get("sumaTotal").toString())+Utilidades.convertirAEntero(cantidadesColumnaMap.get("sumaTotal").toString()));
	 	    
		    for(int i = 0; i< Utilidades.convertirAEntero(cantidadesColumnaMap.get("numRegistros").toString()); i++)
		    {
		    	cantidadTmp = Utilidades.convertirAEntero(cantidadesColumnaMap.get("cantidad_"+i).toString());
		    	report.font.setFontAttributes(0x000000, 8, true, false, false);
			    section.setTableCellsColSpan(1);	    
			    section.addTableTextCellAligned("reporteTable",cantidadTmp>0?cantidadTmp+"":"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
			    
			    //Total Columna Motivo Cancelacion
			    if(Utilidades.convertirAEntero(cantidadesColumnaMap.get("cantidad_"+i).toString()) > 0 && cantidadTmp > 0)
			    	promedio = (cantidadTmp / Utilidades.convertirADouble(cantidadesColumnaMap.get("sumaTotal").toString()))*100 ;
			    else	
			    	promedio = 0;
			    
			    report.font.setFontAttributes(0x000000, 8, true, false, false);
			    section.setTableCellsColSpan(1);	    
			    section.addTableTextCellAligned("reporteTable",UtilidadTexto.formatearValores(promedio+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);	   
		    }
		    
		    //Totales Citas Canceladas Columnas
	    	report.font.setFontAttributes(0x000000, 8, true, false, false);
	 	    section.setTableCellsColSpan(1);	    
	 	    section.addTableTextCellAligned("reporteTable",cantidadesColumnaMap.get("sumaTotal").toString(),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	 	    
	 	    //% Citas Canceladas
	    	report.font.setFontAttributes(0x000000, 8, true, false, false);
	 	    section.setTableCellsColSpan(1);	    
	 	    section.addTableTextCellAligned("reporteTable",UtilidadTexto.formatearValores((Utilidades.convertirADouble(cantidadesColumnaMap.get("sumaTotal").toString())/valorG)*100,"0.00"),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	 	    
	 	    //% Espacio en Blanco
	    	report.font.setFontAttributes(0x000000, 8, true, false, false);
	 	    section.setTableCellsColSpan(2);	    
	 	    section.addTableTextCellAligned("reporteTable","",report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	 	    
	 	    //Total Citas Reprogramadas
	    	report.font.setFontAttributes(0x000000, 8, true, false, false);
	 	    section.setTableCellsColSpan(1);	    
	 	    section.addTableTextCellAligned("reporteTable","Total Citas Reprogramadas",report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	 	    
	 	    //Espacio en Blanco
	    	report.font.setFontAttributes(0x000000, 8, true, false, false);
	 	    section.setTableCellsColSpan((Utilidades.convertirAEntero(cantidadesColumnaMap.get("numRegistros").toString())*2) + 2);	    
	 	    section.addTableTextCellAligned("reporteTable","",report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	 	    
	 	    //Total Citas Reprogramadas   
	    	report.font.setFontAttributes(0x000000, 8, true, false, false);
	 	    section.setTableCellsColSpan(1);	    
	 	    section.addTableTextCellAligned("reporteTable",cantidadesColumnaReprogramadaMap.get("sumaTotal").toString(),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	 	       	
	 	    //% Total Citas Reprogramadas
	    	report.font.setFontAttributes(0x000000, 8, true, false, false);
	 	    section.setTableCellsColSpan(1);	    
	 	    section.addTableTextCellAligned("reporteTable",UtilidadTexto.formatearValores((Utilidades.convertirADouble(cantidadesColumnaReprogramadaMap.get("sumaTotal").toString())/valorG)*100,"0.00"),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	 	    
	 	    //Total General
	    	report.font.setFontAttributes(0x000000, 8, true, false, false);
	 	    section.setTableCellsColSpan(1);	    
	 	    section.addTableTextCellAligned("reporteTable","TOTAL GENERAL",report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	 	    
	 	    //Espacio en Blanco
	    	report.font.setFontAttributes(0x000000, 8, true, false, false);
	 	    section.setTableCellsColSpan((Utilidades.convertirAEntero(cantidadesColumnaMap.get("numRegistros").toString())*2) + 2);	    
	 	    section.addTableTextCellAligned("reporteTable","",report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	 	    
	 	    //Total General Citas Reprogramadas
	    	report.font.setFontAttributes(0x000000, 8, true, false, false);
	 	    section.setTableCellsColSpan(1);	    
	 	    section.addTableTextCellAligned("reporteTable",valorG+"",report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	 	    
	 	    //% Total General Citas Reprogramadas
	    	report.font.setFontAttributes(0x000000, 8, true, false, false);
	 	    section.setTableCellsColSpan(1);	    
	 	    section.addTableTextCellAligned("reporteTable","100",report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	 	    
		    //**********************************************************************************************
	 	    
	 	    //se añade la sección al documento
	 	    report.addSectionToDocument("reporte");
	    }
	    else
	    {
	    	logger.info("no existen datos para el reporte");
	    }
	   	    
	    report.closeReport();	    
	    return respuesta;		
	}
	
	//************************************************************************************************************************
	
	
	/**
	 * Genera el pdf
	 * @param Connection con
	 * @param String pathArchivo
	 * @param String nombreArchivo
	 * @param UsuarioBasico usuario
	 * @param HttpServletRequest request
	 * */
	public static HashMap pdfCancelacionCitaPorInstitucion(
			Connection con,
			String pathArchivo,			
			int codigoReporte,
			HashMap mapaBusqueda,
			UsuarioBasico usuario, 
			HttpServletRequest request)
	{
		String tituloDespacho = "" ;
		InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		String nombreArchivo = "";
		Random r=new Random();
		HashMap condicionesWhere = ReporteEstadisticoConsultaEx.getCondicionesBuquedaReporte(codigoReporte,mapaBusqueda);
		HashMap respuesta = new HashMap();
		HashMap datosBaseMap = new HashMap();
		
		//Valida el tipo de reporte a generar		
		tituloDespacho = "CANCELACIÓN CITA POR INSTITUCIÓN";			
		nombreArchivo  = "cancelacionCitaInstitucion" + r.nextInt()  +".pdf";			
				
		//Se pone el valor del nombre del archivo
		respuesta.put("nombreArchivo",nombreArchivo);
		
		String filename = pathArchivo + System.getProperty("file.separator") + nombreArchivo;			
		
		//*************IMPRESION DEL ENCABEZADO************************************************
    	PdfReports report = new PdfReports("true",true,usuario.getLoginUsuario());        
        String filePath=ValoresPorDefecto.getFilePath();        
        
        filePath = filePath.substring(0, filePath.lastIndexOf("/", (filePath.length()-2)));
		report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), tituloDespacho);
		
        //Se abre el reporte
        report.openReport(filename);
        report.font.useFont(iTextBaseFont.FONT_TIMES, true, true, true);
        iTextBaseTable section;
        
        //**************SECCION ENCABEZADO (Se muestran los parámetros de busqueda)*********************************************************************
        section = report.createSection("encabezado", "encabezadoTable", 1, 2, 10);
  		section.setTableBorder("encabezadoTable", 0x000000, 0.0f);
	    section.setTableCellBorderWidth("encabezadoTable", 0.0f);
	    section.setTableCellPadding("encabezadoTable", 1);
	    //Espaciado entre la sección actual y la anterior
	    section.setTableOffset("encabezadoTable", 0.0f);
	    section.setTableSpaceBetweenCells("encabezadoTable", 0.1f);
	    section.setTableCellsDefaultColors("encabezadoTable", 0xFFFFFF, 0xFFFFFF);
	    section.setTableCellsColSpan(2);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.addTableTextCellAligned("encabezadoTable", "Párametros de Busqueda: "+condicionesWhere.get("parametrosbusqueda").toString(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);	    
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    
	    //se añade la sección al documento
	    report.addSectionToDocument("encabezado");
	    //********************************************************************************************************************
	    
	    //***********SECCION DETALLE*****************************************************************************************
	    datosBaseMap = ReporteEstadisticoConsultaEx.getCargarDatosBasicosCancelacionCitas(con,condicionesWhere.get("where").toString(),condicionesWhere.get("where0").toString(),true);
	    
	    HashMap encabezadoColumnaMap = new HashMap();
	    HashMap encabezadoFilaMap = new HashMap();
	    HashMap cantidadesColumnaMap = new HashMap();
	    HashMap cantidadesColumnaReprogramadaMap = new HashMap();
	    HashMap cantidadesFilaMap = new HashMap();
	    HashMap cantidadesFilaReprogramadaMap = new HashMap();
	    int numeroFilas = 0;
	    int numeroColumnas = 0;
	    int cantidadTmp = 0;
	    double promedio = 0;
	    
	    //Utilidades.imprimirMapa(datosBaseMap);
	    
	    if(!datosBaseMap.get("numRegistros").toString().equals("0") 
	    		&& !datosBaseMap.get("codigoMotivo_0").toString().equals(ConstantesBD.codigoNuncaValido+""))
	    {
		    encabezadoColumnaMap = getDatosAgrupadosMap(datosBaseMap,"codigoMotivo_","codigoMotivo_","descripcionMotivo_",ConstantesBD.codigoNuncaValido+"");
		    encabezadoFilaMap = getDatosAgrupadosDobleMap(datosBaseMap,"codigoEspecialidad_","descripcionEspecialidad_","codigoMedico_","descripcionMedico_",ConstantesBD.codigoNuncaValido+"");
		    cantidadesColumnaMap = getCantidadesFilaColumna(encabezadoColumnaMap,datosBaseMap,"codigoMotivo_","codigoCita_",false);		 
		    cantidadesColumnaReprogramadaMap = getCantidadesFilaColumna(encabezadoColumnaMap,datosBaseMap,"codigoMotivo_","codigoCita_",true);		    	    
		    cantidadesFilaMap = getCantidadesFilaColumnaDoble(encabezadoFilaMap,datosBaseMap,"codigoEspecialidad_","codigoMedico_","codigoCita_",false);		    
		    cantidadesFilaReprogramadaMap = getCantidadesFilaColumnaDoble(encabezadoFilaMap,datosBaseMap,"codigoEspecialidad_","codigoMedico_","codigoCita_",true);	    
		    
		    numeroFilas = Utilidades.convertirAEntero(encabezadoFilaMap.get("numRegistros").toString()) + 5 ;
		    //+ Utilidades.convertirAEntero(encabezadoFilaMap.get("numCodigoEncabezado").toString());
		    numeroColumnas = (Utilidades.convertirAEntero(cantidadesColumnaMap.get("numRegistros").toString())*2) + 6;
		    cantidadTmp = 0;
		    promedio = 0;
	   
		    //********************************************************************************************************************
		    
		    //logger.info("valor del mapa >> ");
		    //Utilidades.imprimirMapa(encabezadoColumnaMap);
		    //logger.info("\n\n\nvalor del mapa >> ");
		    //Utilidades.imprimirMapa(cantidadesColumnaMap);
		    //logger.info("valor del mapa Fila >> ");
		    //Utilidades.imprimirMapa(encabezadoFilaMap);
		    
			section = report.createSection("reporte","reporteTable",numeroFilas,numeroColumnas,0.5f);
	  		section.setTableBorder("reporteTable", 0x000000, 0.01f);
		    section.setTableCellBorderWidth("reporteTable", 0.01f);
		    section.setTableCellPadding("reporteTable", 1.0f);
		    section.setTableSpaceBetweenCells("reporteTable", 0.0f);
		    //Espaciado entre la sección actual y la anterior
		    section.setTableOffset("reporteTable", 5.0f);
		    section.setTableSpaceBetweenCells("reporteTable", 0.1f);
		    section.setTableCellsDefaultColors("reporteTable", 0xFFFFFF, 0x000000);
		    
		    //**********SE DIBUJAN LOS TÍTULOS DE LAS COLUMNAS******************************************************************
		    report.font.setFontAttributes(0x000000, 8, true, false, false);
		    section.setTableCellsColSpan(1);
		    section.addTableTextCellAligned("reporteTable","", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    
		    report.font.setFontAttributes(0x000000, 8, true, false, false);
		    section.setTableCellsColSpan(1);
		    section.addTableTextCellAligned("reporteTable","", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    
		    report.font.setFontAttributes(0x000000, 8, true, false, false);
		    section.setTableCellsColSpan((Utilidades.convertirAEntero(cantidadesColumnaMap.get("numRegistros").toString())*2));	    
		    section.addTableTextCellAligned("reporteTable","MOTIVO CANCELACIÓN", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
		    
		    report.font.setFontAttributes(0x000000, 8, true, false, false);
		    section.setTableCellsColSpan(4);	    
		    section.addTableTextCellAligned("reporteTable","TOTAL", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
		    
		    report.font.setFontAttributes(0x000000, 8, true, false, false);
		    section.setTableCellsColSpan(1);	    
		    section.addTableTextCellAligned("reporteTable","ESPECIALIDAD", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
		    
		    report.font.setFontAttributes(0x000000, 8, true, false, false);
		    section.setTableCellsColSpan(1);	    
		    section.addTableTextCellAligned("reporteTable","PROFESIONAL DE LA SALUD", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
	
		    //Recorre los motivos de cancelacion que son las columnas del reporte
		    for(int i = 0; i < Utilidades.convertirAEntero(encabezadoColumnaMap.get("numRegistros").toString()); i++ )
		    {
		    	if(!encabezadoColumnaMap.get("descripcionEncabezado_"+i).toString().equals(ConstantesBD.codigoNuncaValido+""))
		    	{
			    	report.font.setFontAttributes(0x000000, 8, true, false, false);
				    section.setTableCellsColSpan(1);	    
				    section.addTableTextCellAligned("reporteTable",encabezadoColumnaMap.get("descripcionEncabezado_"+i).toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
				    
				    report.font.setFontAttributes(0x000000, 8, true, false, false);
				    section.setTableCellsColSpan(1);	    
				    section.addTableTextCellAligned("reporteTable","%", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);	    	
		    	}
		    }
		    
		    report.font.setFontAttributes(0x000000, 8, true, false, false);
		    section.setTableCellsColSpan(1);	    
		    section.addTableTextCellAligned("reporteTable","Citas Cancel.", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
		    
		    report.font.setFontAttributes(0x000000, 8, true, false, false);
		    section.setTableCellsColSpan(1);	    
		    section.addTableTextCellAligned("reporteTable","% Citas Cancel.", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
		    
		    report.font.setFontAttributes(0x000000, 8, true, false, false);
		    section.setTableCellsColSpan(1);	    
		    section.addTableTextCellAligned("reporteTable","Citas Reprog.", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
		    
		    report.font.setFontAttributes(0x000000, 8, true, false, false);
		    section.setTableCellsColSpan(1);	    
		    section.addTableTextCellAligned("reporteTable","% Citas Reprog.", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
		    
		    String codigoEncabezado = "";
		    
		    //Inserta el Detalle
		    for(int i = 0; i < Utilidades.convertirAEntero(encabezadoFilaMap.get("numRegistros").toString()); i++ )
		    {	
		    	if(!codigoEncabezado.equals(encabezadoFilaMap.get("codigoEncabezado_"+i).toString()))
			    {
		    		codigoEncabezado = encabezadoFilaMap.get("codigoEncabezado_"+i).toString();
		    			
			    	//Nombre del especialidad
			    	report.font.setFontAttributes(0x000000, 8, true, false, false);
			    	section.setTableCellsColSpan(1);
			 	    section.setTableCellsRowSpan(getCantidadGrupo(encabezadoFilaMap,encabezadoFilaMap.get("codigoEncabezado_"+i).toString()));	    
			 	    section.addTableTextCellAligned("reporteTable",encabezadoFilaMap.get("descripcionEncabezado_"+i).toString().toUpperCase(),report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
			    }
		 			 	   
		 	    //Nombre del Medico
		 	    report.font.setFontAttributes(0x000000, 8, true, false, false);
		 	    section.setTableCellsColSpan(1);
		 	    section.setTableCellsRowSpan(1);
		 	    section.addTableTextCellAligned("reporteTable",encabezadoFilaMap.get("descripcionEncabezado2_"+i).toString().toUpperCase(),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		 	   		 	    
		 	    cantidadTmp = 0;
		 	    
			    for(int j = 0; j< Utilidades.convertirAEntero(encabezadoColumnaMap.get("numRegistros").toString()); j++)
			    {		    	
			    	cantidadTmp = Utilidades.convertirAEntero(getvalorMapaDatosDoble(datosBaseMap,"codigoCita_",encabezadoFilaMap.get("codigoEncabezado_"+i).toString(),encabezadoFilaMap.get("codigoEncabezado2_"+i).toString(),encabezadoColumnaMap.get("codigoEncabezado_"+j).toString()));
			    	report.font.setFontAttributes(0x000000, 8, false, false, false);
				    section.setTableCellsColSpan(1);	    
				    section.addTableTextCellAligned("reporteTable",cantidadTmp>0?cantidadTmp+"":"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
				    
				    //Motivo de cancelación
				    if(Utilidades.convertirAEntero(cantidadesFilaMap.get("cantidad_"+i).toString()) > 0 && cantidadTmp > 0)
				    	promedio = (cantidadTmp / Utilidades.convertirADouble(cantidadesFilaMap.get("cantidad_"+i).toString()))*100 ;
				    else	
				    	promedio = 0;
				    
				    report.font.setFontAttributes(0x000000, 8,false, false, false);
				    section.setTableCellsColSpan(1);	    
				    section.addTableTextCellAligned("reporteTable",promedio>0?UtilidadTexto.formatearValores(promedio,"0.00"):"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);		    	
			    }
			    
			    //Citas Canceladas
			    report.font.setFontAttributes(0x000000, 8, true, false, false);
			    section.setTableCellsColSpan(1);	    
			    if(Utilidades.convertirAEntero(cantidadesFilaMap.get("cantidad_"+i).toString()) > 0)
			    	section.addTableTextCellAligned("reporteTable",cantidadesFilaMap.get("cantidad_"+i).toString(), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
			    else
			    	section.addTableTextCellAligned("reporteTable","", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
			    			    
			    //% Citas Canceladas
			    if(Utilidades.convertirAEntero(cantidadesFilaMap.get("cantidad_"+i).toString()) > 0 && 
			    		Utilidades.convertirAEntero(cantidadesColumnaMap.get("sumaTotal").toString()) > 0)
			    	promedio = (Utilidades.convertirADouble(cantidadesFilaMap.get("cantidad_"+i).toString()) / Utilidades.convertirADouble(cantidadesColumnaMap.get("sumaTotal").toString())) * 100;
			    else	
			    	promedio = 0;			    
			    
			    report.font.setFontAttributes(0x000000, 8, true, false, false);
			    section.setTableCellsColSpan(1);	    
			    section.addTableTextCellAligned("reporteTable",promedio>0?UtilidadTexto.formatearValores(promedio,"0.00"):"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
			    
			    //Citas Reprogramadas		
			    report.font.setFontAttributes(0x000000, 8, true, false, false);
			    section.setTableCellsColSpan(1);		    		    
			    section.addTableTextCellAligned("reporteTable",cantidadesFilaReprogramadaMap.get("cantidad_"+i).toString(), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
			    		    		   
			    //% Citas Reprogramadas		  
			    if(Utilidades.convertirAEntero(cantidadesFilaReprogramadaMap.get("cantidad_"+i).toString()) > 0 && 
			    		Utilidades.convertirAEntero(cantidadesColumnaReprogramadaMap.get("sumaTotal").toString()) > 0)
			    	promedio = (Utilidades.convertirADouble(cantidadesFilaReprogramadaMap.get("cantidad_"+i).toString()) / Utilidades.convertirADouble(cantidadesColumnaReprogramadaMap.get("sumaTotal").toString())) * 100;
			    else	
			    	promedio = 0;			    
			    
			    report.font.setFontAttributes(0x000000, 8, true, false, false);
			    section.setTableCellsColSpan(1);	    
			    section.addTableTextCellAligned("reporteTable",promedio>0?UtilidadTexto.formatearValores(promedio,"0.00"):"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
			       
		    }
		    
		    //Total Citas Canceladas
	    	report.font.setFontAttributes(0x000000, 8, true, false, false);
	 	    section.setTableCellsColSpan(2);	    
	 	    section.addTableTextCellAligned("reporteTable","Total Citas Canceladas",report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	 	    
	 	    cantidadTmp = 0;
	 	    int valorG = (Utilidades.convertirAEntero(cantidadesColumnaReprogramadaMap.get("sumaTotal").toString())+Utilidades.convertirAEntero(cantidadesColumnaMap.get("sumaTotal").toString()));
	 	    
		    for(int i = 0; i< Utilidades.convertirAEntero(cantidadesColumnaMap.get("numRegistros").toString()); i++)
		    {
		    	cantidadTmp = Utilidades.convertirAEntero(cantidadesColumnaMap.get("cantidad_"+i).toString());
		    	report.font.setFontAttributes(0x000000, 8, true, false, false);
			    section.setTableCellsColSpan(1);	    
			    section.addTableTextCellAligned("reporteTable",cantidadTmp>0?cantidadTmp+"":"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
			    
			    //Total Columna Motivo Cancelacion
			    if(Utilidades.convertirAEntero(cantidadesColumnaMap.get("cantidad_"+i).toString()) > 0 && cantidadTmp > 0)
			    	promedio = (cantidadTmp / Utilidades.convertirADouble(cantidadesColumnaMap.get("sumaTotal").toString()))*100 ;
			    else	
			    	promedio = 0;
			    
			    report.font.setFontAttributes(0x000000, 8, true, false, false);
			    section.setTableCellsColSpan(1);	    
			    section.addTableTextCellAligned("reporteTable",UtilidadTexto.formatearValores(promedio+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);	   
		    }
		    
		    //Totales Citas Canceladas Columnas
	    	report.font.setFontAttributes(0x000000, 8, true, false, false);
	 	    section.setTableCellsColSpan(1);	    
	 	    section.addTableTextCellAligned("reporteTable",cantidadesColumnaMap.get("sumaTotal").toString(),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	 	    
	 	    //% Citas Canceladas
	    	report.font.setFontAttributes(0x000000, 8, true, false, false);
	 	    section.setTableCellsColSpan(1);	    
	 	    section.addTableTextCellAligned("reporteTable",UtilidadTexto.formatearValores((Utilidades.convertirADouble(cantidadesColumnaMap.get("sumaTotal").toString())/valorG)*100,"0.00"),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	 	    
	 	    //% Espacio en Blanco
	    	report.font.setFontAttributes(0x000000, 8, true, false, false);
	 	    section.setTableCellsColSpan(2);	    
	 	    section.addTableTextCellAligned("reporteTable","",report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	 	    
	 	    //Total Citas Reprogramadas
	    	report.font.setFontAttributes(0x000000, 8, true, false, false);
	 	    section.setTableCellsColSpan(2);	    
	 	    section.addTableTextCellAligned("reporteTable","Total Citas Reprogramadas",report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	 	    
	 	    //Espacio en Blanco
	    	report.font.setFontAttributes(0x000000, 8, true, false, false);
	 	    section.setTableCellsColSpan((Utilidades.convertirAEntero(cantidadesColumnaMap.get("numRegistros").toString())*2) + 2);	    
	 	    section.addTableTextCellAligned("reporteTable","",report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	 	    
	 	    //Total Citas Reprogramadas   
	    	report.font.setFontAttributes(0x000000, 8, true, false, false);
	 	    section.setTableCellsColSpan(1);	    
	 	    section.addTableTextCellAligned("reporteTable",cantidadesColumnaReprogramadaMap.get("sumaTotal").toString(),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	 	       	
	 	    //% Total Citas Reprogramadas
	    	report.font.setFontAttributes(0x000000, 8, true, false, false);
	 	    section.setTableCellsColSpan(1);	    
	 	    section.addTableTextCellAligned("reporteTable",UtilidadTexto.formatearValores((Utilidades.convertirADouble(cantidadesColumnaReprogramadaMap.get("sumaTotal").toString())/valorG)*100,"0.00"),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	 	    
	 	    //Total General
	    	report.font.setFontAttributes(0x000000, 8, true, false, false);
	 	    section.setTableCellsColSpan(2);	    
	 	    section.addTableTextCellAligned("reporteTable","TOTAL GENERAL",report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	 	    
	 	    //Espacio en Blanco
	    	report.font.setFontAttributes(0x000000, 8, true, false, false);
	 	    section.setTableCellsColSpan((Utilidades.convertirAEntero(cantidadesColumnaMap.get("numRegistros").toString())*2) + 2);	    
	 	    section.addTableTextCellAligned("reporteTable","",report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	 	    
	 	    //Total General Citas Reprogramadas
	    	report.font.setFontAttributes(0x000000, 8, true, false, false);
	 	    section.setTableCellsColSpan(1);	    
	 	    section.addTableTextCellAligned("reporteTable",valorG+"",report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	 	    
	 	    //% Total General Citas Reprogramadas
	    	report.font.setFontAttributes(0x000000, 8, true, false, false);
	 	    section.setTableCellsColSpan(1);	    
	 	    section.addTableTextCellAligned("reporteTable","100",report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	 	    
		    //**********************************************************************************************
	 	    
	 	    //se añade la sección al documento
	 	    report.addSectionToDocument("reporte");
	    }
	    else
	    {
	    	logger.info("no existen datos para el reporte. ");
	    }
	   	    
	    report.closeReport();	    
	    return respuesta;		
	}
	
	//************************************************************************************************************************
	
	/**
	 * Arma el encabezado del detalle 
	 * @param String codigo
	 * @param String descripcion
	 * */
	public static HashMap getDatosAgrupadosMap(HashMap datosBasico,String codigo,String codigoCruce, String descripcion,String filtro)
	{
		int numRegistros = Utilidades.convertirAEntero(datosBasico.get("numRegistros").toString());
		String valor = "";
		String codigoConcatenados = "";
		int numDetalle = 0;
		HashMap respuesta = new HashMap();
		respuesta.put("numRegistros","0");
		
		for(int i = 0; i < numRegistros; i++)
		{			
			if(!valor.equals(datosBasico.get(codigo+i).toString()) && 
					!getDatoInMap(respuesta,datosBasico.get(codigo+i).toString(),"",false)  && 
						!datosBasico.get(codigo+i).toString().equals(filtro))
			{
				valor = datosBasico.get(codigo+i).toString();
				respuesta.put("codigoEncabezado_"+numDetalle,datosBasico.get(codigo+i).toString());
				respuesta.put("descripcionEncabezado_"+numDetalle,datosBasico.get(descripcion+i).toString());				
				respuesta.put("codigoCruce_"+numDetalle,datosBasico.get(codigoCruce+i).toString());
				numDetalle++;
				respuesta.put("numRegistros",numDetalle);
				codigoConcatenados += datosBasico.get(codigo+i).toString()+",";
			}
		}
			
		if(codigoConcatenados.endsWith(","))		
			codigoConcatenados = codigoConcatenados.substring(0,codigoConcatenados.length()-1);
		
		respuesta.put("codigosConcatenados",codigoConcatenados);	
		//Utilidades.imprimirMapa(respuesta);		
		return respuesta;
	}
	
	//***************************************************************************************************************************
	
	/**
	 * Retorna el numero de veces que se repite un codigo en el HashMap
	 * @param HashMap datosBasico
	 * @param String codigo
	 * */
	public static int getCantidadGrupo(HashMap datosBasico,String codigo)
	{
		int cont = 0;
		int numRegistros = Utilidades.convertirAEntero(datosBasico.get("numRegistros").toString());
		
		for(int i = 0; i <numRegistros; i++)
		{
			if(datosBasico.get("codigoEncabezado_"+i).toString().equals(codigo))
				cont++;
		}
		
		return cont;
	}
	
	//***************************************************************************************************************************
	
	/**
	 * Arma el encabezado del detalle 
	 * @param String codigo
	 * @param String descripcion
	 * */
	public static HashMap getDatosAgrupadosDobleMap(
			HashMap datosBasico,
			String codigo,
			String descripcion,
			String codigo2,
			String descripcion2,
			String filtro)
	{
		int numRegistros = Utilidades.convertirAEntero(datosBasico.get("numRegistros").toString());
		String valor = "";
		String valor2 = "";	
		int contCodigoEncab = 0;	
		int numDetalle = 0;
		HashMap respuesta = new HashMap();
		respuesta.put("numRegistros","0");
		
		for(int i = 0; i < numRegistros; i++)
		{			
			if((!valor.equals(datosBasico.get(codigo+i).toString()) || !valor2.equals(datosBasico.get(codigo2+i).toString())) &&
					!getDatoInMap(respuesta,datosBasico.get(codigo+i).toString(),datosBasico.get(codigo2+i).toString(),true)  && 
						!datosBasico.get(codigo+i).toString().equals(filtro))
			{
				valor = datosBasico.get(codigo+i).toString();
				valor2 = datosBasico.get(codigo2+i).toString();			
				respuesta.put("codigoEncabezado_"+numDetalle,datosBasico.get(codigo+i).toString());
				respuesta.put("codigoEncabezado2_"+numDetalle,datosBasico.get(codigo2+i).toString());
				respuesta.put("descripcionEncabezado_"+numDetalle,datosBasico.get(descripcion+i).toString());			
				respuesta.put("descripcionEncabezado2_"+numDetalle,datosBasico.get(descripcion2+i).toString());				
				numDetalle++;
				respuesta.put("numRegistros",numDetalle);
			}
		}
					
		//Utilidades.imprimirMapa(respuesta);
		respuesta.put("numCodigoEncabezado",contCodigoEncab);
		return respuesta;
	}
	
	//***************************************************************************************************************************
	
	/**
	 * Consulta si existe un dato en mapa
	 * @param HashMap datos
	 * @param String parametro
	 * @param String parametro2
	 * */
	public static boolean getDatoInMap(HashMap datos, String parametro, String parametro2,boolean validartodos)
	{
		for(int i = 0; i< Utilidades.convertirAEntero(datos.get("numRegistros").toString()); i++)
		{
			if(datos.get("codigoEncabezado_"+i).toString().equals(parametro) && !validartodos)
				return true;
			else if(datos.get("codigoEncabezado_"+i).toString().equals(parametro) 
					&& datos.get("codigoEncabezado2_"+i).toString().equals(parametro2) 
						&& validartodos)
				return true;
		}
		
		return false;
	}
	
	//***************************************************************************************************************************
	
	/**
	 * Consulta la cantidad de la especialidad
	 * @param HashMap datos
	 * @param HashMap datos
	 * @param String keyBusqueda
	 * @param String keyRespuesta
	 * @param String valorParametroBusqueda
	 * */
	public static String getDatoKeyMap(HashMap datos,String keyBusqueda, String keyRespuesta, String valorParametroBusqueda)
	{
		for(int i = 0; i< Utilidades.convertirAEntero(datos.get("numRegistros").toString()); i++)
		{
			if(datos.get(keyBusqueda+i).toString().equals(valorParametroBusqueda))
				return datos.get(keyRespuesta+i).toString();
		}
		
		return "";
	}
	
	//***************************************************************************************************************************
	
	/**
	 * Realiza la suma de cantidades
	 * @param HashMap titulares
	 * @param HashMap datosBasico
	 * @param String codigo
	 * @param String cantidad
	 * */
	public static HashMap getCantidadesFilaColumna(HashMap titulares,HashMap datosBasico,String codigo, String cantidad,boolean esReprogramada)
	{
		int numRegistros = Utilidades.convertirAEntero(titulares.get("numRegistros").toString());
		String valor = "";
		int numDetalle = 0;
		int cantidadSuma = 0;
		int cantidadSumaTotal = 0;
		HashMap respuesta = new HashMap();
		
		for(int i=0; i<numRegistros; i++)
		{			
			for(int j=0; j<Utilidades.convertirAEntero(datosBasico.get("numRegistros").toString()); j++)
			{				
				if((esReprogramada && Utilidades.convertirAEntero(datosBasico.get("codigoMotivo_"+j).toString()) < 0) 
						|| (!esReprogramada && Utilidades.convertirAEntero(datosBasico.get("codigoMotivo_"+j).toString()) >= 0)	)
				{					
					if((titulares.get("codigoEncabezado_"+i).toString().equals(datosBasico.get(codigo+j).toString())) 
							|| (esReprogramada &&  Utilidades.convertirAEntero(datosBasico.get(codigo+j).toString()) < 0))
					{
						if(Utilidades.convertirAEntero(datosBasico.get(cantidad+j).toString()) > 0)
							cantidadSuma += Utilidades.convertirAEntero(datosBasico.get(cantidad+j).toString());
						else
							cantidadSuma += 0;					
					}
				}
			}
			
			respuesta.put("cantidad_"+i,cantidadSuma);		
			cantidadSumaTotal += cantidadSuma;
			cantidadSuma = 0;			
		}	
		
		respuesta.put("sumaTotal",cantidadSumaTotal);
		respuesta.put("numRegistros",numRegistros);		
		
		//logger.info("valor del mapa2 >> "+respuesta);
		return respuesta;
	}
	
	//***************************************************************************************************************************
	
	/**
	 * Realiza la suma de cantidades
	 * @param HashMap titulares
	 * @param HashMap datosBasico
	 * @param String codigo
	 * @param String cantidad
	 * */
	public static HashMap getCantidadesFilaColumnaDoble(HashMap titulares,HashMap datosBasico,String codigo,String codigo2, String cantidad,boolean esReprogramada)
	{
		int numRegistros = Utilidades.convertirAEntero(titulares.get("numRegistros").toString());
		String valor = "";
		int numDetalle = 0;
		int cantidadSuma = 0;
		int cantidadSumaTotal = 0;
		HashMap respuesta = new HashMap();
		
		for(int i=0; i<numRegistros; i++)
		{			
			for(int j=0; j<Utilidades.convertirAEntero(datosBasico.get("numRegistros").toString()); j++)
			{				
				if((esReprogramada && Utilidades.convertirAEntero(datosBasico.get("codigoMotivo_"+j).toString()) < 0) 
						|| (!esReprogramada && Utilidades.convertirAEntero(datosBasico.get("codigoMotivo_"+j).toString()) >= 0)	)
				{					
					if((titulares.get("codigoEncabezado_"+i).toString().equals(datosBasico.get(codigo+j).toString()) && 
							(titulares.get("codigoEncabezado2_"+i).toString().equals(datosBasico.get(codigo2+j).toString()))) 
							|| (esReprogramada &&  Utilidades.convertirAEntero(datosBasico.get(codigo+j).toString()) < 0))
					{
						if(Utilidades.convertirAEntero(datosBasico.get(cantidad+j).toString()) > 0)
							cantidadSuma += Utilidades.convertirAEntero(datosBasico.get(cantidad+j).toString());
						else
							cantidadSuma += 0;					
					}
				}
			}
			
			respuesta.put("cantidad_"+i,cantidadSuma);		
			cantidadSumaTotal += cantidadSuma;
			cantidadSuma = 0;			
		}	
		
		respuesta.put("sumaTotal",cantidadSumaTotal);
		respuesta.put("numRegistros",numRegistros);		
		
		//logger.info("valor del mapa2 >> "+respuesta);
		return respuesta;
	}
	
	//***************************************************************************************************************************
	
	/**
	 *  Devuelve el valor de cruce de un mapa  
	 *  @param HashMap datosBaseMap
	 *  @param String keyDevolver
	 *  @param String codigoFilaEncabezado
	 *  @param String codigoColumnaEncabezado
	 * */
	public static String getvalorMapaDatos(HashMap datosBaseMap,String keyDevolver,String codigoFilaEncabezado,String codigoColumnaEncabezado)
	{
		String resultado = "";
		
		for(int i=0; i < Utilidades.convertirAEntero(datosBaseMap.get("numRegistros").toString()); i++)
		{
			if(datosBaseMap.get("codigoEspecialidad_"+i).toString().equals(codigoFilaEncabezado) && 
					datosBaseMap.get("codigoMotivo_"+i).toString().equals(codigoColumnaEncabezado))
			{
				return datosBaseMap.get(keyDevolver+i).toString();
			}
		}
		
		return resultado;
	}
	
	//***************************************************************************************************************************
	

	/**
	 *  Devuelve el valor de cruce de un mapa  
	 *  @param HashMap datosBaseMap
	 *  @param String keyDevolver
	 *  @param String codigoFilaEncabezado
	 *  @param String codigoColumnaEncabezado
	 * */
	public static String getvalorMapaDatosDoble(HashMap datosBaseMap,String keyDevolver,String codigoFilaEncabezado,String codigoFilaEncabezado2,String codigoColumnaEncabezado)
	{
		String resultado = "";
		
		for(int i=0; i < Utilidades.convertirAEntero(datosBaseMap.get("numRegistros").toString()); i++)
		{
			if((datosBaseMap.get("codigoEspecialidad_"+i).toString().equals(codigoFilaEncabezado) && 
					datosBaseMap.get("codigoMedico_"+i).toString().equals(codigoFilaEncabezado2)) && 
						datosBaseMap.get("codigoMotivo_"+i).toString().equals(codigoColumnaEncabezado))
			{
				return datosBaseMap.get(keyDevolver+i).toString();
			}
		}
		
		return resultado;
	}
	
	//***************************************************************************************************************************
	
    /**
     * Método para obtener el total de egresos 
     * @param mapa
     * @param codigoMes
     * @return
     */
    private static int calcularTotalSinRangos(HashMap<String, Object> mapa, String codigoMes,String llave) 
    {
    	int total = 0;
    	
		for(int i=0;i<Utilidades.convertirAEntero(mapa.get("numRegistros")+"");i++)
			if(codigoMes.equals(mapa.get("mesEgreso_"+i).toString()))
				total = Utilidades.convertirAEntero(mapa.get(llave+"_"+i).toString(), true);
		return total;
    }
    
	//***************************************************************************************************************************

	/**
     * Método para obtener el total de muertes rango mes
     * @param datosHospitalizados
     * @param orden
     * @param codigoMes
     * @return
     */
    private static int calcularTotalMuertesRangoMes(HashMap<String, Object> datosHospitalizados, String orden,String codigoMes) 
    {
    	int totalMuertesRangoMes = 0;
    	
    	for(int i=0;i<Utilidades.convertirAEntero(datosHospitalizados.get("numRegistros")+"");i++)
    		if(codigoMes.equals(datosHospitalizados.get("mesEgreso_"+i).toString())&&orden.equals(datosHospitalizados.get("orden_"+i).toString()))
    			totalMuertesRangoMes += Utilidades.convertirAEntero(datosHospitalizados.get("muerte_"+i).toString(), true); 
		
		return totalMuertesRangoMes;
	}
    
	//***************************************************************************************************************************

	/**
     * Método para obtener el totla de egresos de pacientes hospitalizados
     * @param datosHospitalizados
     * @param codigoMes
     * @return
     */
	private static int calcularTotalEgresosHospitalizados(HashMap<String, Object> datosHospitalizados, String codigoMes) 
	{
		int totalEgresos = 0;
		
		for(int i=0;i<Utilidades.convertirAEntero(datosHospitalizados.get("numRegistros")+"");i++)
			if(codigoMes.equals(datosHospitalizados.get("mesEgreso_"+i).toString()))
				totalEgresos += Utilidades.convertirAEntero(datosHospitalizados.get("paciente_"+i).toString(), true);
		
		return totalEgresos;
	}
	
	//***************************************************************************************************************************
}