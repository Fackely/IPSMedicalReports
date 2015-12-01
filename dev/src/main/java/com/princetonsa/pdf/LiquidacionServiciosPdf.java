/*
 * Mar 07, 2008
 */
package com.princetonsa.pdf;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;
import util.pdf.iTextBaseTable;

import com.lowagie.text.BadElementException;
import com.princetonsa.actionform.salasCirugia.ConsultaLiquidacionQxForm;
import com.princetonsa.actionform.salasCirugia.LiquidacionServiciosForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * Clase para manejar la impresion de la liquidacion de servicios 
 * por medio de iText.
 */
public class LiquidacionServiciosPdf 
{
	 /**
	 * Objeto para manejar los logs de esta clase
	*/
    private static Logger logger=Logger.getLogger(LiquidacionServiciosPdf.class);
    
    /**
     * Método para realizar la impresión de la liquidacion de servicios
     * @param filename
     * @param forma
     * @param usuario
     */
    public static void pdfLiquidacionServicios(String filename, LiquidacionServiciosForm forma,UsuarioBasico usuario, HttpServletRequest request, PersonaBasica paciente)
    {
    	InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
    	
    	//*************IMPRESION DEL ENCABEZADO************************************************
    	PdfReports report = new PdfReports(); 
        
        String filePath=ValoresPorDefecto.getFilePath();
        String tituloDespacho="LIQUIDACION DE SERVICIOS" ;
        
        filePath=filePath.substring(0, filePath.lastIndexOf("/", (filePath.length()-2)));
		report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), tituloDespacho);
		
        //Se abre el reporte
        report.openReport(filename);
        report.font.useFont(iTextBaseFont.FONT_TIMES, true, true, true);
        iTextBaseTable section;
        
        //**************SECCION ENCABEZADO*********************************************************************
        section = report.createSection("encabezado", "encabezadoTable", 7, 3, 10);
  		section.setTableBorder("encabezadoTable", 0x000000, 0.0f);
	    section.setTableCellBorderWidth("encabezadoTable", 0.0f);
	    section.setTableCellPadding("encabezadoTable", 1);
	    section.setTableSpaceBetweenCells("encabezadoTable", 0.1f);
	    section.setTableCellsDefaultColors("encabezadoTable", 0xFFFFFF, 0xFFFFFF);
	    
	    //Datos del paciente
	    section.setTableCellsColSpan(3);
	    report.font.setFontAttributes(0x000000, 8, true, false, false);
	    section.addTableTextCellAligned("encabezadoTable", "Información Personal", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellsDefaultColors("encabezadoTable", 0xFFFFFF, 0xFFFFFF);
	    section.addTableTextCellAligned("encabezadoTable", "Apellidos y Nombres: "+paciente.getPrimerApellido()+" "+paciente.getSegundoApellido()+" "+paciente.getPrimerNombre()+" "+paciente.getSegundoNombre(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("encabezadoTable", paciente.getCodigoTipoIdentificacionPersona()+": "+paciente.getNumeroIdentificacionPersona()+" de "+paciente.getNombreCiudadExpedicion()+" ("+paciente.getNombrePaisExpedicion()+")", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.addTableTextCellAligned("encabezadoTable", "Edad : "+paciente.getEdadDetallada(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.addTableTextCellAligned("encabezadoTable", "Sexo : "+paciente.getSexo(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    //Datos de la admision
	    section.setTableCellsColSpan(3);
	    report.font.setFontAttributes(0x000000, 8, true, false, false);
	    section.addTableTextCellAligned("encabezadoTable", "Información de la Admisión", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellsDefaultColors("encabezadoTable", 0xFFFFFF, 0xFFFFFF);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("encabezadoTable", "Fecha Ingreso: "+paciente.getFechaIngreso()+" "+paciente.getHoraIngreso(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(2);
	    section.addTableTextCellAligned("encabezadoTable", "Responsable: "+forma.getNombreResponsable(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    //Datos de la orden
	    section.setTableCellsColSpan(3);
	    report.font.setFontAttributes(0x000000, 8, true, false, false);
	    section.addTableTextCellAligned("encabezadoTable", "Información Orden", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellsDefaultColors("encabezadoTable", 0xFFFFFF, 0xFFFFFF);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("encabezadoTable", "Orden Médica: "+forma.getEncabezadoSolicitud("orden"), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.addTableTextCellAligned("encabezadoTable", "Fecha Orden: "+forma.getEncabezadoSolicitud("fechaOrden")+" "+forma.getEncabezadoSolicitud("horaOrden"), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.addTableTextCellAligned("encabezadoTable", "Usuario Liquida: "+usuario.getLoginUsuario(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);

	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    //se añade la sección al documento
	    report.addSectionToDocument("encabezado");
	    //*****************************************************************************************************************
	    
	    report.font.setFontAttributes(0x000000, 9, true, false, false);
	    report.document.addParagraph("DETALLE DE CIRUGIAS:",report.font,iTextBaseDocument.ALIGNMENT_LEFT,13);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    
	    //***********SECCION DETALLE*****************************************************************************************
	    //Se iteran las cirugias de la orden que fueron liquidadas
	    for(int i=0;i<forma.getNumCirugias();i++)
	    {
	    	if(UtilidadTexto.getBoolean(forma.getCirugiasSolicitud("liquidarServicio_"+i).toString()))
	    	{
	    		int numAsocios = Integer.parseInt(forma.getCirugiasSolicitud("numAsocios_"+i).toString());
	    		
	    		
	    		section = report.createSection("detalle"+i, "detalleTable"+i, (numAsocios+3), 4, 0.5f);
	      		section.setTableBorder("detalleTable"+i, 0x000000, 0.1f);
	    	    section.setTableCellBorderWidth("detalleTable"+i, 0.1f);
	    	    section.setTableCellPadding("detalleTable"+i, 1);
	    	    section.setTableSpaceBetweenCells("detalleTable"+i, 0.1f);
	    	    section.setTableCellsDefaultColors("detalleTable"+i, 0xFFFFFF, 0x000000);
	    	    
	    	    report.font.setFontAttributes(0x000000, 8, true, false, false);
	    	    section.setTableCellsColSpan(3);
	    	    section.addTableTextCellAligned("detalleTable"+i, forma.getCirugiasSolicitud("descripcionServicio_"+i).toString(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    	    section.setTableCellsColSpan(1);
	    	    section.addTableTextCellAligned("detalleTable"+i, UtilidadTexto.formatearValores(forma.getCirugiasSolicitud("valor_"+i).toString()), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	    	    section.setTableCellsColSpan(4);
	    	    section.addTableTextCellAligned("detalleTable"+i, "Especialidad Interviene: "+forma.getCirugiasSolicitud("nombreEspecialidadInterviene_"+i).toString(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    	    
	    	    section.setTableCellsColSpan(1);
	    	    section.addTableTextCellAligned("detalleTable"+i, "Esquema Tar: "+forma.getCirugiasSolicitud("nombreEsquemaTarifario_"+i), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    	    section.addTableTextCellAligned("detalleTable"+i, "Tarifa, Grupo ó UVR: "+forma.getCirugiasSolicitud("grupoUvr_"+i), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    	    section.addTableTextCellAligned("detalleTable"+i, "Tipo cirugía: "+forma.getCirugiasSolicitud("nombreTipoCirugia_"+i), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    	    section.addTableTextCellAligned("detalleTable"+i, "Vía: "+forma.getCirugiasSolicitud("nombreViaCx_"+i), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    	    
	    	    
	    	    
	    	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    	    for(int j=0;j<numAsocios;j++)
	    	    {
	    	    	section.setTableCellsColSpan(3);
	    	    	section.addTableTextCellAligned("detalleTable"+i, forma.getCirugiasSolicitud("nombreAsocio_"+i+"_"+j)+": "+forma.getCirugiasSolicitud("nombreProfesional_"+i+"_"+j), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    	    	section.setTableCellsColSpan(1);
		    	    section.addTableTextCellAligned("detalleTable"+i, UtilidadTexto.formatearValores(forma.getCirugiasSolicitud("valor_"+i+"_"+j).toString()), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
		    	    
	    	    }
	    	    
	    	    
	    	    float widths[]={(float) 2.5,(float) 2.5, (float) 2.5,(float) 2.5};
	    	    
	    	    try 
	            {
	    			section.getTableReference("detalleTable"+i).setWidths(widths);
	    		} 
	            catch (BadElementException e) 
	            {
	    			logger.error("Error al ajustar los anchos de la tabla de UPC: "+e);
	    		}
	    	    //se añade la sección al documento
	    	    report.addSectionToDocument("detalle"+i);
	    	}
	    }
	    
	    //**************SECCION MATERIALES ESPECIALES**********************************************************************
	    if(forma.getNumMaterialesEspeciales()>0)
	    {
	    	section = report.createSection("materiales", "materialesTable", forma.getNumMaterialesEspeciales()+2, 5, 0.5f);
      		section.setTableBorder("materialesTable", 0xFFFFFF, 0.0f);
    	    section.setTableCellBorderWidth("materialesTable", 0.0f);
    	    section.setTableCellPadding("materialesTable", 1);
    	    section.setTableSpaceBetweenCells("materialesTable", 0.1f);
    	    section.setTableCellsDefaultColors("materialesTable", 0xFFFFFF, 0xFFFFFF);
    	    
    	    report.font.setFontAttributes(0x000000, 8, true, false, false);
    	    section.setTableCellsColSpan(5);
    	    section.addTableTextCellAligned("materialesTable", "Materiales Especiales", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
    	    section.setTableCellsColSpan(1);
    	    
    	    
    	    section.addTableTextCellAligned("materialesTable", "Cod.", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
    	    section.addTableTextCellAligned("materialesTable", "Descripción", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
    	    section.addTableTextCellAligned("materialesTable", "Cant.", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    	    section.addTableTextCellAligned("materialesTable", "Valor Uni.", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    	    section.addTableTextCellAligned("materialesTable", "Valor Total.", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    	    
    	    double total = 0;
    	    report.font.setFontAttributes(0x000000, 8, false, false, false);
    	    for(int i=0;i<forma.getNumMaterialesEspeciales();i++)
    	    {
    	    	HashMap elemento = (HashMap)forma.getMaterialesEspeciales().get(i);
    	    	
    	    	section.addTableTextCellAligned("materialesTable", elemento.get("codigoArticulo").toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
        	    section.addTableTextCellAligned("materialesTable", elemento.get("nombreArticulo").toString(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
        	    section.addTableTextCellAligned("materialesTable", elemento.get("cantidad").toString(), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
        	    section.addTableTextCellAligned("materialesTable", UtilidadTexto.formatearValores(elemento.get("valorUnitario").toString()), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
        	    section.addTableTextCellAligned("materialesTable", UtilidadTexto.formatearValores(elemento.get("valorTotal").toString()), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
        	    total += Double.parseDouble(elemento.get("valorTotal").toString());
    	    }
    	    section.setTableCellsColSpan(5);
    	    report.font.setFontAttributes(0x000000, 8, true, false, false);
    	    section.addTableTextCellAligned("materialesTable", UtilidadTexto.formatearValores(total), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    	    
    	    float widths[]={(float) 0.5,(float) 5.5, (float) 1,(float) 1.5,(float) 1.5};
    	    
    	    try 
            {
    			section.getTableReference("materialesTable").setWidths(widths);
    		} 
            catch (BadElementException e) 
            {
    			logger.error("Error al ajustar los anchos de la tabla de UPC: "+e);
    		}
    	    //se añade la sección al documento
    	    report.addSectionToDocument("materiales");
	    }
	    //*******************************************************************************************************************
	    
	    
	    //********************************************************************************************************************
	    report.closeReport();
    }
    
    
    /**
     * Método para realizar la impresión de la consulta liquidacion de servicios
     * @param filename
     * @param forma
     * @param usuario
     */
    public static void pdfConsultaLiquidacionServicios(String filename, ConsultaLiquidacionQxForm forma,UsuarioBasico usuario, HttpServletRequest request, PersonaBasica paciente)
    {
    	InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
    	
    	//*************IMPRESION DEL ENCABEZADO************************************************
    	PdfReports report = new PdfReports(); 
        
        String filePath=ValoresPorDefecto.getFilePath();
        String tituloDespacho="LIQUIDACION DE SERVICIOS" ;
        
        filePath=filePath.substring(0, filePath.lastIndexOf("/", (filePath.length()-2)));
		report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), tituloDespacho);
		
        //Se abre el reporte
        report.openReport(filename);
        report.font.useFont(iTextBaseFont.FONT_TIMES, true, true, true);
        iTextBaseTable section;
        
        //**************SECCION ENCABEZADO*********************************************************************
        section = report.createSection("encabezado", "encabezadoTable", 7, 3, 10);
  		section.setTableBorder("encabezadoTable", 0x000000, 0.0f);
	    section.setTableCellBorderWidth("encabezadoTable", 0.0f);
	    section.setTableCellPadding("encabezadoTable", 1);
	    section.setTableSpaceBetweenCells("encabezadoTable", 0.1f);
	    section.setTableCellsDefaultColors("encabezadoTable", 0xFFFFFF, 0xFFFFFF);
	    
	    //Datos del paciente
	    section.setTableCellsColSpan(3);
	    report.font.setFontAttributes(0x000000, 8, true, false, false);
	    section.addTableTextCellAligned("encabezadoTable", "Información Personal", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellsDefaultColors("encabezadoTable", 0xFFFFFF, 0xFFFFFF);
	    section.addTableTextCellAligned("encabezadoTable", "Apellidos y Nombres: "+paciente.getPrimerApellido()+" "+paciente.getSegundoApellido()+" "+paciente.getPrimerNombre()+" "+paciente.getSegundoNombre(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("encabezadoTable", paciente.getCodigoTipoIdentificacionPersona()+": "+paciente.getNumeroIdentificacionPersona()+" de "+paciente.getNombreCiudadExpedicion()+" ("+paciente.getNombrePaisExpedicion()+")", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.addTableTextCellAligned("encabezadoTable", "Edad : "+paciente.getEdadDetallada(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.addTableTextCellAligned("encabezadoTable", "Sexo : "+paciente.getSexo(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    //Datos de la admision
	    section.setTableCellsColSpan(3);
	    report.font.setFontAttributes(0x000000, 8, true, false, false);
	    section.addTableTextCellAligned("encabezadoTable", "Información de la Admisión", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellsDefaultColors("encabezadoTable", 0xFFFFFF, 0xFFFFFF);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("encabezadoTable", "Fecha Ingreso: "+paciente.getFechaIngreso()+" "+paciente.getHoraIngreso(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(2);
	    section.addTableTextCellAligned("encabezadoTable", "Responsable: "+forma.getConvenio(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    //Datos de la orden
	    section.setTableCellsColSpan(3);
	    report.font.setFontAttributes(0x000000, 8, true, false, false);
	    section.addTableTextCellAligned("encabezadoTable", "Información Orden", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellsDefaultColors("encabezadoTable", 0xFFFFFF, 0xFFFFFF);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("encabezadoTable", "Orden Médica: "+forma.getEncabezadoSolicitud("orden"), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.addTableTextCellAligned("encabezadoTable", "Fecha Orden: "+forma.getEncabezadoSolicitud("fechaOrden")+" "+forma.getEncabezadoSolicitud("horaOrden"), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.addTableTextCellAligned("encabezadoTable", "Autorización: "+forma.getEncabezadoSolicitud("autorizacion"), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);

	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    //se añade la sección al documento
	    report.addSectionToDocument("encabezado");
	    //*****************************************************************************************************************
	    
	    report.font.setFontAttributes(0x000000, 9, true, false, false);
	    report.document.addParagraph("DETALLE DE CIRUGIAS:",report.font,iTextBaseDocument.ALIGNMENT_LEFT,13);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    
	    //***********SECCION DETALLE*****************************************************************************************
	    //Se iteran las cirugias de la orden que fueron liquidadas
	    for(int i=0;i<forma.getNumCirugiasSolicitud();i++)
	    {
	    	
    		int numAsocios = Integer.parseInt(forma.getAsocios("numAsocios_"+i).toString());
    		
    		
    		section = report.createSection("detalle"+i, "detalleTable"+i, (numAsocios+4), 5, 0.5f);
      		section.setTableBorder("detalleTable"+i, 0x000000, 0.1f);
    	    section.setTableCellBorderWidth("detalleTable"+i, 0.1f);
    	    section.setTableCellPadding("detalleTable"+i, 1);
    	    section.setTableSpaceBetweenCells("detalleTable"+i, 0.1f);
    	    section.setTableCellsDefaultColors("detalleTable"+i, 0xFFFFFF, 0x000000);
    	    
    	    report.font.setFontAttributes(0x000000, 8, true, false, false);
    	    section.setTableCellsColSpan(4);
    	    section.addTableTextCellAligned("detalleTable"+i, forma.getCirugiasSolicitud("descripcionServicio_"+i).toString(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
    	    section.setTableCellsColSpan(1);
    	    section.addTableTextCellAligned("detalleTable"+i, forma.getCirugiasSolicitud("valorCirugia_"+i).toString(), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    	    section.setTableCellsColSpan(5);
    	    section.addTableTextCellAligned("detalleTable"+i, "Especialidad Interviene: "+forma.getCirugiasSolicitud("nombreEspecialidadInterviene_"+i).toString().toLowerCase(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
    	    
    	    section.setTableCellsColSpan(1);
    	    section.addTableTextCellAligned("detalleTable"+i, "Esquema Tar: "+forma.getCirugiasSolicitud("nombreEsquemaTarifario_"+i).toString().toLowerCase(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
    	    section.addTableTextCellAligned("detalleTable"+i, "Tarifa, Grupo ó UVR: "+forma.getCirugiasSolicitud("grupoUvr_"+i), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
    	    section.setTableCellsColSpan(2);
    	    section.addTableTextCellAligned("detalleTable"+i, "Tipo cirugía: "+forma.getCirugiasSolicitud("nombreTipoCirugia_"+i), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
    	    section.setTableCellsColSpan(1);
    	    section.addTableTextCellAligned("detalleTable"+i, "Vía: "+forma.getCirugiasSolicitud("nombreViaCx_"+i), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
    	    
    	    
    	    if(numAsocios>0)
    	    {
    	    	section.setTableCellsColSpan(2);
    	    	section.addTableTextCellAligned("detalleTable"+i, "Asocios", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
    	    	section.setTableCellsColSpan(1);
        	    section.addTableTextCellAligned("detalleTable"+i, "Estado", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
        	    section.addTableTextCellAligned("detalleTable"+i, "Cobrable", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
        	    section.addTableTextCellAligned("detalleTable"+i, "Valor", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
        	    
    	    }
    	    report.font.setFontAttributes(0x000000, 8, false, false, false);
    	    for(int j=0;j<numAsocios;j++)
    	    {
    	    	section.setTableCellsColSpan(2);
    	    	section.addTableTextCellAligned("detalleTable"+i, forma.getAsocios("nombreAsocio_"+i+"_"+j)+": "+forma.getAsocios("nombreMedicoAsocio_"+i+"_"+j).toString().toLowerCase()+(UtilidadTexto.getBoolean(forma.getAsocios("paquetizado_"+i+"_"+j).toString())?" (paquetizado)":""), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
    	    	section.setTableCellsColSpan(1);
    	    	section.addTableTextCellAligned("detalleTable"+i, forma.getAsocios("estadoAsocio_"+i+"_"+j).toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
    	    	section.setTableCellsColSpan(1);
    	    	section.addTableTextCellAligned("detalleTable"+i, forma.getAsocios("cobrable_"+i+"_"+j).toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
    	    	section.setTableCellsColSpan(1);
	    	    section.addTableTextCellAligned("detalleTable"+i, forma.getAsocios("valorAsocio_"+i+"_"+j).toString(), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	    	    
    	    }
    	    
    	    
    	    float widths[]={(float) 3.5,(float) 2.5, (float) 1.25,(float)1.25,(float) 1.5};
    	    
    	    try 
            {
    			section.getTableReference("detalleTable"+i).setWidths(widths);
    		} 
            catch (BadElementException e) 
            {
    			logger.error("Error al ajustar los anchos de la tabla de UPC: "+e);
    		}
    	    //se añade la sección al documento
    	    report.addSectionToDocument("detalle"+i);
	    	
	    }
	    
	    //**************SECCION MATERIALES ESPECIALES**********************************************************************
	    if(forma.getNumMaterialesEspeciales()>0)
	    {
	    	section = report.createSection("materiales", "materialesTable", forma.getNumMaterialesEspeciales()+2, 6, 0.5f);
      		section.setTableBorder("materialesTable", 0xFFFFFF, 0.0f);
    	    section.setTableCellBorderWidth("materialesTable", 0.0f);
    	    section.setTableCellPadding("materialesTable", 1);
    	    section.setTableSpaceBetweenCells("materialesTable", 0.1f);
    	    section.setTableCellsDefaultColors("materialesTable", 0xFFFFFF, 0xFFFFFF);
    	    
    	    report.font.setFontAttributes(0x000000, 8, true, false, false);
    	    section.setTableCellsColSpan(6);
    	    section.addTableTextCellAligned("materialesTable", "Materiales Especiales", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
    	    section.setTableCellsColSpan(1);
    	    
    	    
    	    section.addTableTextCellAligned("materialesTable", "Cod.", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
    	    section.addTableTextCellAligned("materialesTable", "Descripción", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
    	    section.addTableTextCellAligned("materialesTable", "Estado", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
    	    section.addTableTextCellAligned("materialesTable", "Cant.", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    	    section.addTableTextCellAligned("materialesTable", "Valor Uni.", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    	    section.addTableTextCellAligned("materialesTable", "Valor Total.", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    	    
    	    double total = 0;
    	    report.font.setFontAttributes(0x000000, 8, false, false, false);
    	    for(int i=0;i<forma.getNumMaterialesEspeciales();i++)
    	    {
    	    	HashMap elemento = (HashMap)forma.getMaterialesEspeciales().get(i);
    	    	
    	    	section.addTableTextCellAligned("materialesTable", elemento.get("codigoArticulo").toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
        	    section.addTableTextCellAligned("materialesTable", elemento.get("nombreArticulo").toString()+(UtilidadTexto.getBoolean(elemento.get("paquetizado").toString())?" (paquetizado)":""), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
        	    section.addTableTextCellAligned("materialesTable", elemento.get("nombreEstadoCargo").toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
        	    section.addTableTextCellAligned("materialesTable", elemento.get("cantidad").toString(), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
        	    section.addTableTextCellAligned("materialesTable", UtilidadTexto.formatearValores(elemento.get("valorUnitario").toString()), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
        	    section.addTableTextCellAligned("materialesTable", UtilidadTexto.formatearValores(elemento.get("valorTotal").toString()), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
        	    total += Double.parseDouble(elemento.get("valorTotal").toString());
    	    }
    	    section.setTableCellsColSpan(6);
    	    report.font.setFontAttributes(0x000000, 8, true, false, false);
    	    section.addTableTextCellAligned("materialesTable", UtilidadTexto.formatearValores(total), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    	    
    	    float widths[]={(float) 0.5,(float) 4.5,(float) 1, (float) 1,(float) 1.5,(float) 1.5};
    	    
    	    try 
            {
    			section.getTableReference("materialesTable").setWidths(widths);
    		} 
            catch (BadElementException e) 
            {
    			logger.error("Error al ajustar los anchos de la tabla de UPC: "+e);
    		}
    	    //se añade la sección al documento
    	    report.addSectionToDocument("materiales");
	    }
	    //*******************************************************************************************************************
	    
	    
	    //********************************************************************************************************************
	    report.closeReport();
    }
}
