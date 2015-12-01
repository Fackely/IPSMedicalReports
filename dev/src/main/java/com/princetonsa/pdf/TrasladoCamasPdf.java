/*
 * @(#)TrasladoCamasPdf.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.pdf;

import util.UtilidadFecha;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;
import util.pdf.iTextBaseTable;

import com.princetonsa.actionform.TrasladoCamasForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * Clase para manejar la generación de PDF's para
 * el Traslado de Camas
 * 
 *	@version 1.0, 11 /Jul/ 2005
 */
public class TrasladoCamasPdf
{

	public static void pdfTrasladoCamasFechas(String filename, TrasladoCamasForm trasladoCamasForm, UsuarioBasico medico, PersonaBasica paciente)
	{
		PdfReports report = new PdfReports();
		report.setFormatoImpresion("infoPiePaginaInicioMargen");
		/**Tamaño del mapa que contien la consulta de traslados por fecha**/
		int tamanioMapa=Integer.parseInt(trasladoCamasForm.getMapaTrasladosFecha("numRegistros").toString());
		
		
		if (tamanioMapa==0)
		{
		    return;
		}
		else
		{
		  
			InstitucionBasica institucionBasica= new InstitucionBasica();
	        institucionBasica.cargarXConvenio(medico.getCodigoInstitucionInt(), paciente.getCodigoConvenio());
	        report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), "TRASLADO DE CAMAS DE: "+trasladoCamasForm.getFechaTraslado());
	        
		    report.openReport(filename);
		    /**Fecha y Hora de la Impresion**/
		    report.document.addParagraph(UtilidadFecha.getFechaActual()+"  "+UtilidadFecha.getHoraActual(),report.font,iTextBaseDocument.ALIGNMENT_CENTER,25);
		    
		    report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
		    

		    report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		    report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000, 9);
		    report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 7);
		    report.font.setFontSizeAndAttributes(10,true,false,false);
		    
		    
		    //********************CUERPO DE LA IMPRESION*******************************************************
		    iTextBaseTable  section = report.createSection("descripcion", "descripcionTable", tamanioMapa+2, 12, 10);
	  		section.setTableBorder("descripcionTable", 0xFFFFFF, 0.0f);
		    section.setTableCellBorderWidth("descripcionTable", 0.0f);
		    section.setTableCellPadding("descripcionTable", 1);
		    section.setTableSpaceBetweenCells("descripcionTable", 0.5f);
		    section.setTableCellsDefaultColors("descripcionTable", 0xFFFFFF,  0xFFFFFF);
		    
		    //
		    report.font.setFontAttributes(0x000000, 10, true, false, false);
		    section.setTableCellsRowSpan(2);
		    section.addTableTextCellAligned("descripcionTable", "Paciente", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("descripcionTable", "Tipo y N° Id.", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("descripcionTable", "Fecha Ingreso", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("descripcionTable", "Respons.", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.setTableCellsRowSpan(1);
		    section.setTableCellsColSpan(4);
		    section.addTableTextCellAligned("descripcionTable", "Datos Cama Actual", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("descripcionTable", "Datos Cama Anterior", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.setTableCellsColSpan(1);
		    section.addTableTextCellAligned("descripcionTable", "Piso", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("descripcionTable", "Habit", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("descripcionTable", "Cama", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("descripcionTable", "Tipo Hab.", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("descripcionTable", "Piso", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("descripcionTable", "Habit", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("descripcionTable", "Cama", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("descripcionTable", "Tipo Hab.", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    
		    
		    section.getTableReference("descripcionTable").endHeaders();
		    
		    
		    report.font.setFontAttributes(0x000000, 8, false, false, false);
		    

		    //int tamanioVector=tamanioMapa*12;
		    //String[] datos= new String[tamanioVector];
		    /**Llenado del arreglo con los datos para mostrar en la impresion**/
		    for(int k=0;k<tamanioMapa; k++)
			{
		    	section.addTableTextCellAligned("descripcionTable", trasladoCamasForm.getMapaTrasladosFecha("paciente_"+k)+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    	section.addTableTextCellAligned("descripcionTable", trasladoCamasForm.getMapaTrasladosFecha("tipoId_"+k)+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    	section.addTableTextCellAligned("descripcionTable", trasladoCamasForm.getMapaTrasladosFecha("fechaIngreso_"+k)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    	section.addTableTextCellAligned("descripcionTable", trasladoCamasForm.getMapaTrasladosFecha("responsable_"+k)+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    	section.addTableTextCellAligned("descripcionTable", trasladoCamasForm.getMapaTrasladosFecha("pisoCamaActual_"+k)+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    	section.addTableTextCellAligned("descripcionTable", trasladoCamasForm.getMapaTrasladosFecha("habitacionActual_"+k)+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    	section.addTableTextCellAligned("descripcionTable", trasladoCamasForm.getMapaTrasladosFecha("camaActual_"+k)+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    	section.addTableTextCellAligned("descripcionTable", trasladoCamasForm.getMapaTrasladosFecha("tipoHabitacionCamaActual_"+k)+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    	section.addTableTextCellAligned("descripcionTable", trasladoCamasForm.getMapaTrasladosFecha("pisoCamaAnterior_"+k)==null?"":trasladoCamasForm.getMapaTrasladosFecha("pisoCamaAnterior_"+k).toString(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    	section.addTableTextCellAligned("descripcionTable", trasladoCamasForm.getMapaTrasladosFecha("habitacionAnterior_"+k)==null?"":trasladoCamasForm.getMapaTrasladosFecha("habitacionAnterior_"+k).toString(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    	section.addTableTextCellAligned("descripcionTable", trasladoCamasForm.getMapaTrasladosFecha("camaAnterior_"+k)==null?"":trasladoCamasForm.getMapaTrasladosFecha("camaAnterior_"+k).toString(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    	section.addTableTextCellAligned("descripcionTable", trasladoCamasForm.getMapaTrasladosFecha("tipoHabitacionCamaAnterior_"+k)==null?"":trasladoCamasForm.getMapaTrasladosFecha("tipoHabitacionCamaAnterior_"+k).toString(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    	
		    	/**datos[cont]=trasladoCamasForm.getMapaTrasladosFecha("paciente_"+k)+"";cont++;
		    	datos[cont]=trasladoCamasForm.getMapaTrasladosFecha("tipoId_"+k)+"";cont++;
		    	datos[cont]=trasladoCamasForm.getMapaTrasladosFecha("fechaIngreso_"+k)+"";cont++;
		    	datos[cont]=trasladoCamasForm.getMapaTrasladosFecha("responsable_"+k)+"";cont++;
		    	datos[cont]=trasladoCamasForm.getMapaTrasladosFecha("pisoCamaActual_"+k)+"";cont++;
		    	datos[cont]=trasladoCamasForm.getMapaTrasladosFecha("habitacionActual_"+k)+"";cont++;
		    	datos[cont]=trasladoCamasForm.getMapaTrasladosFecha("camaActual_"+k)+"";cont++;
		    	datos[cont]=trasladoCamasForm.getMapaTrasladosFecha("tipoHabitacionCamaActual_"+k)+"";cont++;
		    	datos[cont]=trasladoCamasForm.getMapaTrasladosFecha("pisoCamaAnterior_"+k)==null?"":trasladoCamasForm.getMapaTrasladosFecha("pisoCamaAnterior_"+k).toString();cont++;
		    	datos[cont]=trasladoCamasForm.getMapaTrasladosFecha("habitacionAnterior_"+k)==null?"":trasladoCamasForm.getMapaTrasladosFecha("habitacionAnterior_"+k).toString();cont++;
		    	datos[cont]=trasladoCamasForm.getMapaTrasladosFecha("camaAnterior_"+k)==null?"":trasladoCamasForm.getMapaTrasladosFecha("camaAnterior_"+k).toString();cont++;
		    	datos[cont]=trasladoCamasForm.getMapaTrasladosFecha("tipoHabitacionCamaAnterior_"+k)==null?"":trasladoCamasForm.getMapaTrasladosFecha("tipoHabitacionCamaAnterior_"+k).toString();cont++;
		    	**/
			}
		    
		    /**Adicion de la seccion con el encabezado de las columnas y los datos al reporte**/
		    //report.createColSection("titulosTabla",headerListadoSolicitud ,datos, 10);
            report.addSectionToDocument("descripcion");

		    /**Se cierra el reporte**/
		    report.closeReport(); 
		}

      }
}
