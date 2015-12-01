package com.princetonsa.pdf;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import util.BackUpBaseDatos;
import util.ConstantesIntegridadDominio;
import util.InfoDatosDouble;
import util.InfoDatosInt;
import util.UtilidadFecha;
import util.UtilidadFileUpload;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;
import util.pdf.iTextBaseTable;

import com.lowagie.text.Font;
import com.princetonsa.dto.carteraPaciente.DtoCuotasDatosFinanciacion;
import com.princetonsa.dto.carteraPaciente.DtoDatosFinanciacion;
import com.princetonsa.dto.carteraPaciente.DtoReporteEdadCarteraPaciente;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.Secciones;

/**
 * 
 * Clase usada para generar reporte de Cartera Paciente
 * @author 
 *
 */
public class ReporteEdadCarteraPacientePdf 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(ReporteEdadCarteraPacientePdf.class);
	
	/**
	 * Genera el pdf
	 * @param Connection con
	 * @param String pathArchivo
	 * @param String nombreArchivo
	 * @param UsuarioBasico usuario
	 * @param HttpServletRequest request
	 * */
	public static HashMap pdfCancelacionCita(
			String pathArchivo,			
			DtoReporteEdadCarteraPaciente dtoReporte,
			UsuarioBasico usuario, 
			HttpServletRequest request)
	{
		String tituloDespacho = "" ;
		InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		HashMap respuesta = new HashMap();
		String nombreArchivo = "", parametros = "" ;
		Random r=new Random();
		
		nombreArchivo = "EdadCarteraPaciente"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+"_"+UtilidadFecha.getHoraActual()+"_"+usuario.getLoginUsuario();
		//Se pone el valor del nombre del archivo
		respuesta.put("nombreArchivo",nombreArchivo);
		
		//Se pone el valor del nombre del archivo
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
	    
	    parametros = "Criterios => Centro Atención: ["+(!dtoReporte.getCentroAtencion().equals("")?dtoReporte.getNombreCentroAtencion():"Todos.")+"] "+
	    			 "Tipo Documento: ["+ValoresPorDefecto.getIntegridadDominio(dtoReporte.getTipoDocumento())+"] "+
	    			 "Fecha Corte: ["+dtoReporte.getFechaCorte()+"] ";
	    
	    section.setTableCellsColSpan(2);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.addTableTextCellAligned("encabezadoTable",parametros, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    
	    //se añade la sección al documento
	    report.addSectionToDocument("encabezado");
	    
	    section = report.createSection("reporte","reporteTable",dtoReporte.getArrayDatos().size()+10,(dtoReporte.getArrayRangos().size()+4),0.1f);
  		section.setTableBorder("reporteTable", 0x000000, 0.01f);
	    section.setTableCellBorderWidth("reporteTable", 0.01f);
	    //Espaciado entre la sección actual y la anterior
	    section.setTableOffset("reporteTable", 5.0f);
	    section.setTableCellPadding("reporteTable", 1.0f);
	    section.setTableSpaceBetweenCells("reporteTable", 0.0f);
	    section.setTableCellsDefaultColors("reporteTable", 0xFFFFFF, 0x000000);
	    
	    //**********SE DIBUJAN LOS TÍTULOS DE LAS COLUMNAS******************************************************************
	    report.font.setFontAttributes(0x000000, 8, true, false, false);
	    section.setTableCellsColSpan((dtoReporte.getArrayRangos().size()+4));
	    section.addTableTextCellAligned("reporteTable","CARTERA VENCIDA EN DÍAS", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    report.font.setFontAttributes(0x000000, 8, true, false, false);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("reporteTable","Documento", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellBorderWidth("reporteTable",0.0f);
	    
	    
	    report.font.setFontAttributes(0x000000, 8, true, false, false);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("reporteTable","Centro de Atención", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    for(InfoDatosInt rango :dtoReporte.getArrayRangos())
	    {
	    	report.font.setFontAttributes(0x000000, 8, true, false, false);
	    	section.setTableCellsColSpan(1);
	    	section.setTableCellBorderWidth("reporteTable",0.0f);
	    	section.addTableTextCellAligned("reporteTable",rango.getDescripcion(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    }
	    
	    report.font.setFontAttributes(0x000000, 8, true, false, false);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("reporteTable","Total Cartera Vencida", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);

	    report.font.setFontAttributes(0x000000, 8, true, false, false);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("reporteTable","Total cartera", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	
	    String aux = "";
	    
	    for(DtoDatosFinanciacion dtoDatos : dtoReporte.getArrayDatos())
	    {
	    	if(dtoDatos.getObservaciones().equals(ConstantesIntegridadDominio.acronimoTotal) ||
				dtoDatos.getObservaciones().equals(ConstantesIntegridadDominio.acronimoSubTotal))
	    	{
	    		report.font.setFontAttributes(0x000000, 8, true, false, false);
	    	    section.setTableCellsColSpan(1);
	    	    section.addTableTextCellAligned("reporteTable",dtoDatos.getTipoDocumento(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    	    
	    	    report.font.setFontAttributes(0x000000, 8, true, false, false);
	    	    section.setTableCellsColSpan(1);
	    	    section.addTableTextCellAligned("reporteTable","", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);

	    	    for(DtoCuotasDatosFinanciacion dtoDatos1 : dtoDatos.getCuotasDatosFinan())
	    	    {
    	    		report.font.setFontAttributes(0x000000, 8, true, false, false);
    	    	    section.setTableCellsColSpan(1);
    	    	    report.font.getCurrentFont().setStyle(Font.UNDERLINE+"");
    	    	    section.addTableTextCellAligned("reporteTable",dtoDatos1.getValorCuota()+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	    	    }
	    	}
	    	else
	    	{
				if(!aux.equals(dtoDatos.getTipoDocumento()))
				{
					aux = dtoDatos.getTipoDocumento();
					
					report.font.setFontAttributes(0x000000, 8, true, false, false);
		    	    section.setTableCellsColSpan((dtoReporte.getArrayRangos().size()+4));
		    	    section.addTableTextCellAligned("reporteTable",ValoresPorDefecto.getIntegridadDominio(dtoDatos.getTipoDocumento()).toString().toUpperCase(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
				}
				
				report.font.setFontAttributes(0x000000, 8, false, false, false);
	    	    section.setTableCellsColSpan(1);
	    	    section.addTableTextCellAligned("reporteTable",dtoDatos.getConsecutivo()+" "+dtoDatos.getAnioConsecutivo(),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    	    
	    	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    	    section.setTableCellsColSpan(1);
	    	    section.addTableTextCellAligned("reporteTable",dtoDatos.getNombreCentroAtenDocGaran(),report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);

	    	    for(DtoCuotasDatosFinanciacion dtoDatos1 : dtoDatos.getCuotasDatosFinan())
	    	    {
	    	    	report.font.setFontAttributes(0x000000, 8, false, false, false);
		    	    section.setTableCellsColSpan(1);
		    	    section.addTableTextCellAligned("reporteTable",dtoDatos1.getValorCuota()+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	    	    }
			}
	    }    
	    
	    //se añade la sección al documento
 	    report.addSectionToDocument("reporte");
 	    
	    report.closeReport();	    
	    return respuesta;	
	    //********************************************************************************************************************
	}
	
	/**
	 * Genera el pdf
	 * @param Connection con
	 * @param String pathArchivo
	 * @param String nombreArchivo
	 * @param UsuarioBasico usuario
	 * @param HttpServletRequest request
	 * */
	public static HashMap cvsCancelacionCita(
			String pathArchivo,			
			DtoReporteEdadCarteraPaciente dtoReporte,
			UsuarioBasico usuario, 
			HttpServletRequest request)
	{	
		HashMap respuesta = new HashMap();
		String nombreArchivo = "", parametros = "" ;
		Random r = new Random();
		
		nombreArchivo = "EdadCarteraPaciente"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+"_"+UtilidadFecha.getHoraActual()+"_"+usuario.getLoginUsuario();
		parametros = "Criterios => Centro Atención: ["+(!dtoReporte.getCentroAtencion().equals("")?dtoReporte.getNombreCentroAtencion():"Todos.")+"] "+
		 "Tipo Documento: ["+ValoresPorDefecto.getIntegridadDominio(dtoReporte.getTipoDocumento())+"] "+
		 "Fecha Corte: ["+dtoReporte.getFechaCorte()+"] ";
		
		try
		{
			//**************INICIALIZACIÓN DEL ARCHIVO CSV**********************************************************************
			File archivoCSV=new File(pathArchivo,nombreArchivo+".cvs");
			FileWriter streamCSV = new FileWriter(archivoCSV,false);
			BufferedWriter bufferCSV=new BufferedWriter(streamCSV);
			//******************************************************************************************************************
					    
			//*****************SE GENERA LA TABLA DEL REPORTE*******************************************************************
			bufferCSV.write("");
			bufferCSV.write("Reporte: Edad Cartera Pacientes");
			bufferCSV.write("\n");
			
			bufferCSV.write(parametros);
			bufferCSV.write("\n");
			
			//**********SE DIBUJAN LOS TÍTULOS DE LAS COLUMNAS*****************************************************************
			//Se va escribiendo en el archivo CSV
			bufferCSV.write("Documento;");
			bufferCSV.write("Centro de Atención;");
			
			for(InfoDatosInt rango :dtoReporte.getArrayRangos())
				bufferCSV.write(rango.getDescripcion()+";");
			
			bufferCSV.write("Total Cartera Vencida;");
			bufferCSV.write("Total Cartera;");
			bufferCSV.write("\n");
			
		    String aux = "";
		    for(DtoDatosFinanciacion dtoDatos : dtoReporte.getArrayDatos())
		    {
		    	if(dtoDatos.getObservaciones().equals(ConstantesIntegridadDominio.acronimoTotal) ||
					dtoDatos.getObservaciones().equals(ConstantesIntegridadDominio.acronimoSubTotal))
		    	{
		    		bufferCSV.write(dtoDatos.getTipoDocumento()+";");
		    		bufferCSV.write(";");
		    	    
		    	    for(DtoCuotasDatosFinanciacion dtoDatos1 : dtoDatos.getCuotasDatosFinan())
		    	    	bufferCSV.write(dtoDatos1.getValorCuota()+";");
		    	}
		    	else
		    	{
					if(!aux.equals(dtoDatos.getTipoDocumento()))
					{
						aux = dtoDatos.getTipoDocumento();
						bufferCSV.write(ValoresPorDefecto.getIntegridadDominio(dtoDatos.getTipoDocumento()).toString().toUpperCase()+";");
						bufferCSV.write("\n");
					}
					
					bufferCSV.write(dtoDatos.getConsecutivo()+" "+dtoDatos.getAnioConsecutivo()+";");
					bufferCSV.write(dtoDatos.getNombreCentroAtenDocGaran()+";");
					
		    	    for(DtoCuotasDatosFinanciacion dtoDatos1 : dtoDatos.getCuotasDatosFinan())
		    	    	bufferCSV.write(dtoDatos1.getValorCuota()+";");
				}
		    	
		    	bufferCSV.write("\n");
		    }
		    
		    //Se finaliza el archivo CSV
		    bufferCSV.close();		   
		   	    		    	    
		    //se genera el archivo en formato Zip
			BackUpBaseDatos.EjecutarComandoSO("zip -j "+pathArchivo+nombreArchivo+".zip "+pathArchivo+nombreArchivo+".cvs");		
			
			if(!UtilidadFileUpload.existeArchivo(pathArchivo, nombreArchivo+".zip"))
			{
				respuesta.put("nombreArchivo","");
				respuesta.put("nombreArchivo2","");
			}
			else
			{
				respuesta.put("nombreArchivo",System.getProperty("ADJUNTOS")+System.getProperty("file.separator")+nombreArchivo+".zip");
				respuesta.put("nombreArchivo2",pathArchivo+nombreArchivo+".zip");
			}
					
			logger.info("valor del mensaje >> "+respuesta.get("nombreArchivo"));
	    }
	    catch(IOException e)
	    {
	    	logger.error("Error tratando de generar el archivo CSV: "+nombreArchivo+" (Se cancela proceso) :"+e);
	    }

	    return respuesta;	
	    //********************************************************************************************************************
	}
}