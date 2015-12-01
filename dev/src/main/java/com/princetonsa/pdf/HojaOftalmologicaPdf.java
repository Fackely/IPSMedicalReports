/*
 * Creado en Oct 3, 2005
 */
package com.princetonsa.pdf;

import java.util.Vector;

import org.apache.log4j.Logger;

import util.UtilidadCadena;
import util.UtilidadFecha;
import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;

import com.lowagie.text.BadElementException;
import com.princetonsa.actionform.hojaOftalmologica.HojaOftalmologicaForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author Andrés Mauricio Ruiz Vélez
 * Princeton S.A. (Parquesoft-Manizales)
 */
public class HojaOftalmologicaPdf
{
	private static Logger logger = Logger.getLogger(HojaOftalmologicaPdf.class);
	
	/**
	 * Método que construye el pdf de la sección Estrabismo
	 * @param filename nombre del archivo pdf a generar.
	 * @param hojaOftalmologicaForm
	 * @param medico
	 * @param paciente
	 */
	public static void pdfEstrabismo(String filename, HojaOftalmologicaForm hojaOftalmologicaForm, UsuarioBasico medico, PersonaBasica paciente)
	{
		
		 //Variable para manejar el reporte
        PdfReports report = new PdfReports();
        
        InstitucionBasica institucionBasica= new InstitucionBasica();
        institucionBasica.cargarXConvenio(medico.getCodigoInstitucionInt(), paciente.getCodigoConvenio());
        
       //titulo del reporte
       String tituloReporte="HOJA OFTALMOLOGICA ESTRABISMO (" + UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() +")";
       
       //generacion de la cabecera, validando el tipo de separador e utilizado en el path
       report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), tituloReporte);
       
       //-----------------------------------------Abrir reporte------------------------------------------------------------
       report.openReport(filename);
       report.font.useFont(iTextBaseFont.FONT_TIMES, true, true, true);
       
//     --------------------------------Nombre y Registro Médico del responsable-------------------------------------------------
       report.font.setFontSizeAndAttributes(12,true,false,false);
       report.document.addParagraph("Profesional Responsable: "+hojaOftalmologicaForm.getDatosMedico() ,report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);
       report.document.addParagraph("Fecha de Grabación: "+hojaOftalmologicaForm.getFechaEstrabismo() ,report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
       
       //---------------------------------------------------------Informacion del Paciente----------------------------------------------------
        report.document.addParagraph("INFORMACIÓN PERSONAL ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);
       report.font.setFontSizeAndAttributes(11,false,false,false);
       report.document.addParagraph("Identificación : "+ paciente.getCodigoTipoIdentificacionPersona() + " "+  paciente.getNumeroIdentificacionPersona() ,report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
       report.document.addParagraph("Apellidos y Nombres : " + paciente.getApellidosNombresPersona(false) ,report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
       
       //-------------------------------------------------------Sección de Estrabismo------------------------------------------------------------------//
       report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
       //Se crea la sección indicando el número de filas y columnas y cellspacing
      report.createSection("seccionEstrabismo","tablaEstrabismo",16,9,0);
      //Se establece el borde de la tabla con su color y grosor
      report.getSectionReference("seccionEstrabismo").setTableBorder("tablaEstrabismo", 0x000000, 0.2f);
      //Tamaño del borde de las celdas
      report.getSectionReference("seccionEstrabismo").setTableCellBorderWidth("tablaEstrabismo", 0.2f);
      //Color de fondo de la celda
      report.getSectionReference("seccionEstrabismo").setTableCellsBackgroundColor(0xEBEBEB);
      //Color, tamaño y estilo de la letra
      report.font.setFontAttributes(0x000000, 12, true, false, false);
      //Se realiza el colpan
      report.getSectionReference("seccionEstrabismo").setTableCellsColSpan(9);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", "ESTRABISMO", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      
      //Color de fondo de la celda
      report.getSectionReference("seccionEstrabismo").setTableCellsBackgroundColor(0xFFFFFF);
      //Color, tamaño y estilo de la letra
      report.font.setFontAttributes(0x000000, 11, false, false, false);
 //---------------------------------------------------------PPM-------------------------------------------------------------------//   
      //Se realiza el colpan
      report.getSectionReference("seccionEstrabismo").setTableCellsColSpan(9);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", "PPM : "+hojaOftalmologicaForm.getPpm(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      
// ---------------------------------------------------------COVER TEST-------------------------------------------------------------------//
      //Se realiza el colpan
      report.getSectionReference("seccionEstrabismo").setTableCellsColSpan(2);
      //Se realiza el rowspan
      report.getSectionReference("seccionEstrabismo").setTableCellsRowSpan(2);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", "COVER TEST ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      //Se realiza el colpan
      report.getSectionReference("seccionEstrabismo").setTableCellsColSpan(4);
      //Se realiza el rowspan
      report.getSectionReference("seccionEstrabismo").setTableCellsRowSpan(1);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", "Cerca s.c : "+hojaOftalmologicaForm.getCoverTestCercaSc(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      //Se realiza el colpan
      report.getSectionReference("seccionEstrabismo").setTableCellsColSpan(3);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", "Lejos s.c : "+hojaOftalmologicaForm.getCoverTestLejosSc(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      
      //Se realiza el colpan
      report.getSectionReference("seccionEstrabismo").setTableCellsColSpan(4);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", "Cerca cc : "+hojaOftalmologicaForm.getCoverTestCercaCc(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      //Se realiza el colpan
      report.getSectionReference("seccionEstrabismo").setTableCellsColSpan(3);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", "Lejos c.c : "+hojaOftalmologicaForm.getCoverTestLejosCc(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      
//    ---------------------------------------------------------OJO FIJADOR------ -------------------------------------------------------------------//
     
//    Se realiza el colpan
      report.getSectionReference("seccionEstrabismo").setTableCellsColSpan(4);
      String ojoFijador="";
      if (hojaOftalmologicaForm.getOjoFijador()==-1)
      		ojoFijador="Sin Seleccionar";
      else
      		if(hojaOftalmologicaForm.getOjoFijador()==1)
			 	ojoFijador="Derecho";
			 else
			 	ojoFijador="Izquierdo";
      
		//Se agrega la celda a la tabla
		report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", "OJO FIJADOR: "+ojoFijador, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      
//	    ---------------------------------------------------------PPC------ -------------------------------------------------------------------//
      //Se realiza el colpan
      report.getSectionReference("seccionEstrabismo").setTableCellsColSpan(5);
      
      String[] dbColumns={"codigo","nombre"};
      
      String[] tiposPpc = report.getDataFromCollection(dbColumns, hojaOftalmologicaForm.getListadoTiposPpc());
      
      int posicion=UtilidadCadena.indexOf(tiposPpc, hojaOftalmologicaForm.getPpcInstitucion()+"");
      
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", "PPC: "+tiposPpc[++posicion], report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      
//	  ---------------------------------------------------------PRISMAS CERCA------ -------------------------------------------------------------------//
      //--------------Se guarda en dos vectores de Strings prisma cerca s.c y c.c para que guarde vació y no null---------------//
      String[] cercaSc=new String[9];
      String[] cercaCc=new String[9];
      
      for(int i=0,j=1; i<9; i++,j++)
      {
      	if (hojaOftalmologicaForm.getMapa("prismaCercaSc_"+j)!=null)
	      	{
	      		String valorPrismaCercaSc=(String)hojaOftalmologicaForm.getMapa("prismaCercaSc_"+j);	
	      		cercaSc[i]=valorPrismaCercaSc;
	      	}
      	else
	      	{
	      		cercaSc[i]="";
	      	}
      	
      	if (hojaOftalmologicaForm.getMapa("prismaCercaCc_"+j)!=null)
	      	{
	      		String valorPrismaCercaCc=(String)hojaOftalmologicaForm.getMapa("prismaCercaCc_"+j);
	      		cercaCc[i]=valorPrismaCercaCc;
	      	}
      	else
	      	{
	      		cercaCc[i]="";
	      	}	
      }
      
      //Se realiza el colpan
      report.getSectionReference("seccionEstrabismo").setTableCellsColSpan(1);
      //Se realiza el rowspan 
      report.getSectionReference("seccionEstrabismo").setTableCellsRowSpan(4);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", "PRISMAS ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      //Se realiza el rowspan 
      report.getSectionReference("seccionEstrabismo").setTableCellsRowSpan(3);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", "Cerca s.c ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      //Se realiza el rowspan 
      report.getSectionReference("seccionEstrabismo").setTableCellsRowSpan(1);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", cercaSc[0], report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", cercaSc[1], report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", cercaSc[2], report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      
      //Se realiza el rowspan 
      report.getSectionReference("seccionEstrabismo").setTableCellsRowSpan(3);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", "Cerca c.c ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      //Se realiza el rowspan 
      report.getSectionReference("seccionEstrabismo").setTableCellsRowSpan(1);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", cercaCc[0], report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", cercaCc[1], report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", cercaCc[2], report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      
      //Se realiza el rowspan 
      report.getSectionReference("seccionEstrabismo").setTableCellsRowSpan(1);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", cercaSc[3], report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", cercaSc[4], report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", cercaSc[5], report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      
      //Se realiza el rowspan 
      report.getSectionReference("seccionEstrabismo").setTableCellsRowSpan(1);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", cercaCc[3], report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", cercaCc[4], report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", cercaCc[5], report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      
      //Se realiza el rowspan 
      report.getSectionReference("seccionEstrabismo").setTableCellsRowSpan(1);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", cercaSc[6], report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", cercaSc[7], report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", cercaSc[8], report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      
      //Se realiza el rowspan 
      report.getSectionReference("seccionEstrabismo").setTableCellsRowSpan(1);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", cercaCc[6], report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", cercaCc[7], report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", cercaCc[8], report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      
      //Se realiza el rowspan 
      report.getSectionReference("seccionEstrabismo").setTableCellsRowSpan(1);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", "Lejos s.c ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      //Se realiza el colspan
      report.getSectionReference("seccionEstrabismo").setTableCellsColSpan(3);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", hojaOftalmologicaForm.getPrismaScLejos(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      
      //Se realiza el colspan
      report.getSectionReference("seccionEstrabismo").setTableCellsColSpan(1);
      //Se realiza el rowspan 
      report.getSectionReference("seccionEstrabismo").setTableCellsRowSpan(1);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", "Lejos c.c ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      //Se realiza el colspan
      report.getSectionReference("seccionEstrabismo").setTableCellsColSpan(3);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", hojaOftalmologicaForm.getPrismaCcLejos(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      
//	    ---------------------------------------------------------DUCCIONES Y VERSIONES------ -------------------------------------------------------------------//
      //Se realiza el colpan
      report.getSectionReference("seccionEstrabismo").setTableCellsColSpan(9);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", "DUCCIONES Y VERSIONES : "+hojaOftalmologicaForm.getDuccionesVersiones(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      
//	    ---------------------------------------------------------TEST DE VISION BINOCULAR------ -------------------------------------------------------------------//
      //Se realiza el colpan
      report.getSectionReference("seccionEstrabismo").setTableCellsColSpan(9);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", "TEST DE VISIÓN BINOCULAR : "+hojaOftalmologicaForm.getTestVisionBinocular(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);

//	    ---------------------------------------------------------ESTEREOPSIS------ -------------------------------------------------------------------//
      //Se realiza el colpan
      report.getSectionReference("seccionEstrabismo").setTableCellsColSpan(9);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", "ESTEREOPSIS : "+hojaOftalmologicaForm.getEstereopsis(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      
//	    ---------------------------------------------------------AMPLITUD DE FUSIÓN------ -------------------------------------------------------------------//
      //Se realiza el colpan
      report.getSectionReference("seccionEstrabismo").setTableCellsColSpan(2);
      //Se realiza el rowspan
      report.getSectionReference("seccionEstrabismo").setTableCellsRowSpan(2);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", "AMPLITUD DE FUSIÓN ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      //Se realiza el rowspan
      report.getSectionReference("seccionEstrabismo").setTableCellsRowSpan(1);
      //Se realiza el colpan
      report.getSectionReference("seccionEstrabismo").setTableCellsColSpan(1);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", "Cerca ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      //Se realiza el colpan
      report.getSectionReference("seccionEstrabismo").setTableCellsColSpan(3);
       //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", " -  ( "+hojaOftalmologicaForm.getAmplitudFusionCercaMenos()+")", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", " a+  ( "+hojaOftalmologicaForm.getAmplitudFusionCercaMas()+")", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      
      //Se realiza el colpan
      report.getSectionReference("seccionEstrabismo").setTableCellsColSpan(1);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", "Lejos ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      //Se realiza el colpan
      report.getSectionReference("seccionEstrabismo").setTableCellsColSpan(3);
       //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", " -  ( "+hojaOftalmologicaForm.getAmplitudFusionLejosMenos()+")", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", " a+  ( "+hojaOftalmologicaForm.getAmplitudFusionLejosMas()+")", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      
//	    ---------------------------------------------------------PRISMA COMPENSADOR------ -------------------------------------------------------------------//
      //Se realiza el colpan
      report.getSectionReference("seccionEstrabismo").setTableCellsColSpan(2);
      //Se realiza el rowspan
      report.getSectionReference("seccionEstrabismo").setTableCellsRowSpan(2);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", "PRISMA COMPENSADOR ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      //Se realiza el colpan
      report.getSectionReference("seccionEstrabismo").setTableCellsColSpan(1);
      //Se realiza el rowspan
      report.getSectionReference("seccionEstrabismo").setTableCellsRowSpan(1);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", "Cerca ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      //Se realiza el colpan
      report.getSectionReference("seccionEstrabismo").setTableCellsColSpan(6);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", hojaOftalmologicaForm.getPrismaCompensadorCerca(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      //Se realiza el colpan
      report.getSectionReference("seccionEstrabismo").setTableCellsColSpan(1);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", "Lejos ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      //Se realiza el colpan
      report.getSectionReference("seccionEstrabismo").setTableCellsColSpan(6);
      //Se agrega la celda a la tabla
      report.getSectionReference("seccionEstrabismo").addTableTextCellAligned("tablaEstrabismo", hojaOftalmologicaForm.getPrismaCompensadorLejos(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
      
      //Adionar la sección al documento pdf 
      report.addSectionToDocument("seccionEstrabismo");
      
      //Verificar que no esté vacía las observaciones para que se muestre la sección
      if (!hojaOftalmologicaForm.getObservacionEstrabismo().trim().equals(""))
      	{
		      //-------------------------------------------------------Observaciones de Estrabismo------------------------------------------------------------------//
		      report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		       //Se crea la sección indicando el número de filas y columnas y cellspacing
		      report.createSection("observacionEstrabismo","tablaObserv",2,1,0);
		      //Se establece el borde de la tabla con su color y grosor
		      report.getSectionReference("observacionEstrabismo").setTableBorder("tablaObserv", 0x000000, 0.2f);
		      //Tamaño del borde de las celdas
		      report.getSectionReference("observacionEstrabismo").setTableCellBorderWidth("tablaObserv", 0.2f);
		      //Color de fondo de la celda
		      report.getSectionReference("observacionEstrabismo").setTableCellsBackgroundColor(0xEBEBEB);
		      //Color, tamaño y estilo de la letra
		      report.font.setFontAttributes(0x000000, 12, true, false, false);
		      //Se agrega la celda a la tabla
		      report.getSectionReference("observacionEstrabismo").addTableTextCellAligned("tablaObserv", "Observaciones", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		      
		      //Color de fondo de la celda
		      report.getSectionReference("observacionEstrabismo").setTableCellsBackgroundColor(0xFFFFFF);
		      //Color, tamaño y estilo de la letra
		      report.font.setFontAttributes(0x000000, 11, false, false, false);
		      //Se agrega la celda a la tabla
		      report.getSectionReference("observacionEstrabismo").addTableTextCellAligned("tablaObserv", hojaOftalmologicaForm.getObservacionEstrabismo(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		      
		      // Pie Historia clinica
		      report.getSectionReference("observacionEstrabismo").addTableTextCellAligned("tablaObserv", institucionBasica.getPieHistoriaClinica(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		      
		      //Adionar la sección al documento pdf 
		      report.addSectionToDocument("observacionEstrabismo");      
      	}//fin if
              
       //Cerrando el reporte
		report.closeReport(medico.getNombreUsuario()); 
	}
	
	/**
	 * Método que construye el pdf de la sección Segmento Anterior
	 * @param filename nombre del archivo pdf a generar.
	 * @param hojaOftalmologicaForm
	 * @param medico
	 * @param paciente
	 */
	public static void pdfSegmentoAnterior (String filename, HojaOftalmologicaForm hojaOftalmologicaForm, UsuarioBasico medico, PersonaBasica paciente)
	{
		String[] tiposSegmentoAnt;
		
		String[] dbColumns =  {
	      		"codigo",
				"nombre"
				};
		
		 String[] titulosCol = {
				"Segmento Anterior", 
				"Ojo Derecho", 
				"Ojo Izquierdo"
			};
		
		 //Variable para manejar el reporte
        PdfReports report = new PdfReports();
        
       //--------------Se calcula el tamaño de vector de Strings datos de acuerdo a la cantidad de datos-----------------//
       Vector cods=(Vector)hojaOftalmologicaForm.getMapa("codigosSegmentoAnt");
       int filas=0;
       if(cods != null)
       		{
	       	for (int i=0; i<cods.size(); i++)
	       		{
	       		int segmentoAntInst=Integer.parseInt(cods.elementAt(i)+"");
				//Valor del ojo derecho
				String valorOd=(String)hojaOftalmologicaForm.getMapa("segmentoAntOd_"+segmentoAntInst);
				//Valor del ojo izquierdo
				String valorOs=(String)hojaOftalmologicaForm.getMapa("segmentoAntOs_"+segmentoAntInst);
				
				if(UtilidadCadena.noEsVacio(valorOd) || UtilidadCadena.noEsVacio(valorOs))
					{
						filas++;
					}
	       		}
       		}
       
       // ---------------Se ingresan los datos del form en el vector de Strings
       tiposSegmentoAnt = report.getDataFromCollection(dbColumns, hojaOftalmologicaForm.getListadoTiposSegmentoAnt()); 
      Vector codigos=(Vector)hojaOftalmologicaForm.getMapa("codigosSegmentoAnt");
      int posicion=-1, cont=0;
      String[] datos;
      datos = new String[filas*3];
      		
		if(codigos != null)
		{
			for (int i=0; i<codigos.size(); i++)
			{
				int segmentoAntInst=Integer.parseInt(codigos.elementAt(i)+"");
				//Valor del ojo derecho
				String valorOd=(String)hojaOftalmologicaForm.getMapa("segmentoAntOd_"+segmentoAntInst);
				//Valor del ojo izquierdo
				String valorOs=(String)hojaOftalmologicaForm.getMapa("segmentoAntOs_"+segmentoAntInst);
				
				if(UtilidadCadena.noEsVacio(valorOd) || UtilidadCadena.noEsVacio(valorOs))
				{
					posicion=UtilidadCadena.indexOf(tiposSegmentoAnt, segmentoAntInst+"");
					
					if(posicion!=-1)
						{
						datos[cont]=tiposSegmentoAnt[++posicion];
						cont++;
						datos[cont]=valorOd;
						cont++;
						datos[cont]=valorOs;
						cont++;
						}
				}
				
			}//for
		}
				
       //Tomando la ruta definida en el web.xml para generar el reporte.
		InstitucionBasica institucionBasica= new InstitucionBasica();
        institucionBasica.cargarXConvenio(medico.getCodigoInstitucionInt(), paciente.getCodigoConvenio());
       
       //titulo del reporte
       String tituloReporte="HOJA OFTALMOLOGICA SEGMENTO ANTERIOR (" + UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() +")";
       
       //generacion de la cabecera, validando el tipo de separador e utilizado en el path
       report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), tituloReporte);
       
       String filePath=ValoresPorDefecto.getFilePath();
       //-----------------------------------------Abrir reporte------------------------------------------------------------
       report.openReport(filename);
       report.font.useFont(iTextBaseFont.FONT_TIMES, true, true, true);
       
       //--------------------------------Nombre y Registro Médico del responsable-------------------------------------------------
       report.font.setFontSizeAndAttributes(12,true,false,false);
       report.document.addParagraph("Profesional Responsable: "+hojaOftalmologicaForm.getMedicoSegmentoAnt() ,report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);
       report.document.addParagraph("Fecha de Grabación: "+hojaOftalmologicaForm.getFechaSegmentoAnt() ,report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
       //---------------------------------------------------------Informacion del Paciente----------------------------------------------------
       report.document.addParagraph("INFORMACIÓN PERSONAL ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);
       report.font.setFontSizeAndAttributes(11,false,false,false);
       report.document.addParagraph("Identificación : "+ paciente.getCodigoTipoIdentificacionPersona() + " "+  paciente.getNumeroIdentificacionPersona() ,report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
       report.document.addParagraph("Apellidos y Nombres : " + paciente.getApellidosNombresPersona(false) ,report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
       
       
       //-------------------------------------------------------Sección de Segmento Anterior------------------------------------------------------------------//
       report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
	      
		report.setReportTitleSectionAtributes(0x000000, 0xEBEBEB,0x000000, 12);
		report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 11);
		
		//Enviar los datos al reporte
		report.createColSection("seccionSegmentoAnt", "SEGMENTO ANTERIOR ", titulosCol, datos, 3);
		
		float widths[]={(float) 2,(float) 4,(float) 4};
		try
        {
		    report.getSectionReference("seccionSegmentoAnt").getTableReference("parentTable").setWidths(widths);
        }
        catch (BadElementException e)
        {
        	logger.error("Se presentó error generando el pdf " + e);
        }
        
        report.addSectionToDocument("seccionSegmentoAnt");
       
  //------------------------ Se muestran las imágenes agregadas en la sección ----------------------------------------------------------//
        if(UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getImagenSegmentoAntOD()) || UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getImagenSegmentoAntOS()))
        		{
			        //Se crea la sección indicando el número de filas y columnas y cellspacing
			        report.createSection("imagenSegmentoAnt","tablaImgSAnt",2,2,0);
			        //Se establece el borde de la tabla con su color y grosor
			        report.getSectionReference("imagenSegmentoAnt").setTableBorder("tablaImgSAnt", 0x000000, 0.2f);
			        //Tamaño del borde de las celdas
			        report.getSectionReference("imagenSegmentoAnt").setTableCellBorderWidth("tablaImgSAnt", 0.2f);
			        //Color de fondo de la celda
			        report.getSectionReference("imagenSegmentoAnt").setTableCellsBackgroundColor(0xEBEBEB);
			        //Color, tamaño y estilo de la letra
			        report.font.setFontAttributes(0x000000, 12, true, false, false);
			         //Se agrega la celda a la tabla
			        report.getSectionReference("imagenSegmentoAnt").addTableTextCellAligned("tablaImgSAnt", "Segmento Anterior OD", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
			        //Se agrega la celda a la tabla
			        report.getSectionReference("imagenSegmentoAnt").addTableTextCellAligned("tablaImgSAnt", "Segmento Anterior OS", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
			
			        //Color de fondo de la celda
			        report.getSectionReference("imagenSegmentoAnt").setTableCellsBackgroundColor(0xFFFFFF);
			        
			        //Si se adjunto segmento anterior ojo derecho
			        if(UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getImagenSegmentoAntOD()))
			        	report.getSectionReference("imagenSegmentoAnt").addTableImageCell("tablaImgSAnt", filePath+"/fotos/"+hojaOftalmologicaForm.getImagenSegmentoAntOD(), 75f, 1, 0);
			        
			        //Si se adjunto segmento anterior ojo izquierdo
			        if (UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getImagenSegmentoAntOS()))
			        	report.getSectionReference("imagenSegmentoAnt").addTableImageCell("tablaImgSAnt", filePath+"/fotos/"+hojaOftalmologicaForm.getImagenSegmentoAntOS(), 75f, 1, 1);
			        
			        //Adionar la sección al documento pdf 
			        report.addSectionToDocument("imagenSegmentoAnt");
        		}
        
 //------------------------------------------------------------------------------------------------------------------------------------------------------------//
		        
        //Verificar que no esté vacía las observaciones para que se muestre la sección
        if (!hojaOftalmologicaForm.getObservacionSegmentoAnt().trim().equals(""))
        	{
		  //-------------------------------------------------------Observaciones de Segmento Anterior------------------------------------------------------------------//
		        report.font.setFontSizeAndAttributes(10,true,false,false);
		        //Se crea la sección indicando el número de filas y columnas y cellspacing
		        report.createSection("observacionSegmentoAnt","tablaObserv",2,1,0);
		        //Se establece el borde de la tabla con su color y grosor
		        report.getSectionReference("observacionSegmentoAnt").setTableBorder("tablaObserv", 0x000000, 0.2f);
		        //Tamaño del borde de las celdas
		        report.getSectionReference("observacionSegmentoAnt").setTableCellBorderWidth("tablaObserv", 0.2f);
		        //Color de fondo de la celda
		        report.getSectionReference("observacionSegmentoAnt").setTableCellsBackgroundColor(0xEBEBEB);
		        //Color, tamaño y estilo de la letra
		        report.font.setFontAttributes(0x000000, 12, true, false, false);
		        //Se agrega la celda a la tabla
		        report.getSectionReference("observacionSegmentoAnt").addTableTextCellAligned("tablaObserv", "Observaciones", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		        
		        //Color de fondo de la celda
		        report.getSectionReference("observacionSegmentoAnt").setTableCellsBackgroundColor(0xFFFFFF);
		        //Color, tamaño y estilo de la letra
		        report.font.setFontAttributes(0x000000, 11, false, false, false);
		        //Se agrega la celda a la tabla
		        report.getSectionReference("observacionSegmentoAnt").addTableTextCellAligned("tablaObserv", hojaOftalmologicaForm.getObservacionSegmentoAnt(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		        
		        // Pie Historia clinica
		        report.getSectionReference("observacionSegmentoAnt").addTableTextCellAligned("tablaObserv", institucionBasica.getPieHistoriaClinica(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		        
		        //Adionar la sección al documento pdf 
		        report.addSectionToDocument("observacionSegmentoAnt");
        	}//fin if
		
//----------------------------------------------------------------------------------------------------------------------------------------------------
              
       //Cerrando el reporte
		report.closeReport(medico.getNombreUsuario()); 
	}
	
	/**
	 * Método que construye el pdf de la sección Orbita y Anexos
	 * @param filename nombre del archivo pdf a generar.
	 * @param hojaOftalmologicaForm
	 * @param medico
	 * @param paciente
	 */
	public static void pdfOrbitaAnexos (String filename, HojaOftalmologicaForm hojaOftalmologicaForm, UsuarioBasico medico, PersonaBasica paciente)
	{
		String[] tiposOrbitaAnexos;
		
		String[] dbColumns =  {
	      		"codigo",
				"nombre"
				};
		
		 String[] titulosCol = {
				"Orbita y Anexos", 
				"Ojo Derecho", 
				"Ojo Izquierdo"
			};
		 
		 //Variable para manejar el reporte
        PdfReports report = new PdfReports();
        
       //--------------Se calcula el tamaño de vector de Strings datos de acuerdo a la cantidad de datos-----------------//
       Vector cods=(Vector)hojaOftalmologicaForm.getMapa("codigosOrbitaAnexos");
       int filas=0;
       if(cods != null)
       		{
	       	for (int i=0; i<cods.size(); i++)
	       		{
	       		int orbitaAnexoInst=Integer.parseInt(cods.elementAt(i)+"");
				//Valor del ojo derecho
				String valorOd=(String)hojaOftalmologicaForm.getMapa("orbitaAnexoOd_"+orbitaAnexoInst);
				//Valor del ojo izquierdo
				String valorOs=(String)hojaOftalmologicaForm.getMapa("orbitaAnexoOs_"+orbitaAnexoInst);
				
				if(UtilidadCadena.noEsVacio(valorOd) || UtilidadCadena.noEsVacio(valorOs))
					{
						filas++;
					}
	       		}
       		}
       
       //---------------Se ingresan los datos del form en el vector de Strings
       tiposOrbitaAnexos = report.getDataFromCollection(dbColumns, hojaOftalmologicaForm.getListadoTiposOrbitaAnexos());
       
      Vector codigos=(Vector)hojaOftalmologicaForm.getMapa("codigosOrbitaAnexos");
      int posicion=-1, cont=0;
      String[] datos;
      
      datos = new String[filas*3];
      		
		if(codigos != null)
		{
			for (int i=0; i<codigos.size(); i++)
			{
				int orbitaAnexoInst=Integer.parseInt(codigos.elementAt(i)+"");
				//Valor del ojo derecho
				String valorOd=(String)hojaOftalmologicaForm.getMapa("orbitaAnexoOd_"+orbitaAnexoInst);
				//Valor del ojo izquierdo
				String valorOs=(String)hojaOftalmologicaForm.getMapa("orbitaAnexoOs_"+orbitaAnexoInst);
				
				if(UtilidadCadena.noEsVacio(valorOd) || UtilidadCadena.noEsVacio(valorOs))
				{
					posicion=UtilidadCadena.indexOf(tiposOrbitaAnexos, orbitaAnexoInst+"");
					
					if(posicion!=-1)
						{
						datos[cont]=tiposOrbitaAnexos[++posicion];
						cont++;
						datos[cont]=valorOd;
						cont++;
						datos[cont]=valorOs;
						cont++;
						}
				}
				
			}//for
		}
				
       //Tomando la ruta definida en el web.xml para generar el reporte.
       String filePath=ValoresPorDefecto.getFilePath();
       
       //titulo del reporte
       String tituloReporte="HOJA OFTALMOLOGICA ORBITA Y ANEXOS (" + UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() +")";
       
       InstitucionBasica institucionBasica= new InstitucionBasica();
       institucionBasica.cargarXConvenio(medico.getCodigoInstitucionInt(), paciente.getCodigoConvenio());
       
       //generacion de la cabecera, validando el tipo de separador e utilizado en el path
       report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), tituloReporte);
       
       //-----------------------------------------Abrir reporte------------------------------------------------------------
       report.openReport(filename);
       report.font.useFont(iTextBaseFont.FONT_TIMES, true, true, true);
       
//     --------------------------------Nombre y Registro Médico del responsable-------------------------------------------------
       report.font.setFontSizeAndAttributes(12,true,false,false);
       report.document.addParagraph("Profesional Responsable: "+hojaOftalmologicaForm.getMedicoOrbitaAnexos() ,report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);
       report.document.addParagraph("Fecha de Grabación: "+hojaOftalmologicaForm.getFechaOrbitaAnexos() ,report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
       //---------------------------------------------------------Informacion del Paciente----------------------------------------------------
       report.document.addParagraph("INFORMACIÓN PERSONAL ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);
       report.font.setFontSizeAndAttributes(11,false,false,false);
       report.document.addParagraph("Identificación : "+ paciente.getCodigoTipoIdentificacionPersona() + " "+  paciente.getNumeroIdentificacionPersona() ,report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
       report.document.addParagraph("Apellidos y Nombres : " + paciente.getApellidosNombresPersona(false) ,report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
      
       //-------------------------------------------------------Sección de Segmento Anterior------------------------------------------------------------------//
       report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
	      
		report.setReportTitleSectionAtributes(0x000000, 0xEBEBEB,0x000000, 12);
		report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 11);
		
		//Enviar los datos al reporte
		report.createColSection("seccionOrbitaAnexos", "ÓRBITA Y ANEXOS ", titulosCol, datos, 3);
		
		float widths[]={(float) 2,(float) 4,(float) 4};
		try
        {
		    report.getSectionReference("seccionOrbitaAnexos").getTableReference("parentTable").setWidths(widths);
        }
        catch (BadElementException e)
        {
        	logger.error("Se presentó error generando el pdf " + e);
        }
		
		report.addSectionToDocument("seccionOrbitaAnexos");
		
	  //Verificar que no esté vacía las observaciones para que se muestre la sección
      if (!hojaOftalmologicaForm.getObservacionOrbitaAnexos().trim().equals(""))
      	{
		 //-------------------------------------------------------Observaciones de Orbita y Anexos------------------------------------------------------------------//
		        report.font.setFontSizeAndAttributes(10,true,false,false);
		        //Se crea la sección indicando el número de filas y columnas y cellspacing
		        report.createSection("observacionOrbita","tablaObserv",2,1,0);
		        //Se establece el borde de la tabla con su color y grosor
		        report.getSectionReference("observacionOrbita").setTableBorder("tablaObserv", 0x000000, 0.2f);
		        //Tamaño del borde de las celdas
		        report.getSectionReference("observacionOrbita").setTableCellBorderWidth("tablaObserv", 0.2f);
		        //Color de fondo de la celda
		        report.getSectionReference("observacionOrbita").setTableCellsBackgroundColor(0xEBEBEB);
		        //Color, tamaño y estilo de la letra
		        report.font.setFontAttributes(0x000000, 12, true, false, false);
		        //Se agrega la celda a la tabla
		        report.getSectionReference("observacionOrbita").addTableTextCellAligned("tablaObserv", "Observaciones", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		        
		        //Color de fondo de la celda
		        report.getSectionReference("observacionOrbita").setTableCellsBackgroundColor(0xFFFFFF);
		        //Color, tamaño y estilo de la letra
		        report.font.setFontAttributes(0x000000, 11, false, false, false);
		        //Se agrega la celda a la tabla
		        report.getSectionReference("observacionOrbita").addTableTextCellAligned("tablaObserv", hojaOftalmologicaForm.getObservacionOrbitaAnexos(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		        
		        //Pie Historia clinica
		        report.getSectionReference("observacionOrbita").addTableTextCellAligned("tablaObserv", institucionBasica.getPieHistoriaClinica(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		        
		        //Adionar la sección al documento pdf 
		        report.addSectionToDocument("observacionOrbita");
      	}//fin if
 //-------------------------------------------------------------------------------------------------------------------------------
       //Cerrando el reporte
		report.closeReport(medico.getNombreUsuario()); 
		
	}
	
	/**
	 * Método que construye el pdf de la sección Retina y Vítreo
	 * @param filename nombre del archivo pdf a generar.
	 * @param hojaOftalmologicaForm
	 * @param medico
	 * @param paciente
	 */
	public static void pdfRetinaVitreo (String filename, HojaOftalmologicaForm hojaOftalmologicaForm, UsuarioBasico medico, PersonaBasica paciente)
	{
			 //Variable para manejar el reporte
	        PdfReports report = new PdfReports();
	        
	        //Tomando la ruta definida en el web.xml para generar el reporte.
	        String filePath=ValoresPorDefecto.getFilePath();
	        
	        //titulo del reporte
	        String tituloReporte="HOJA OFTALMOLOGICA RETINA Y VÍTREO (" + UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() +")";
	        
	        InstitucionBasica institucionBasica= new InstitucionBasica();
	        institucionBasica.cargarXConvenio(medico.getCodigoInstitucionInt(), paciente.getCodigoConvenio());
	        
	        //generacion de la cabecera, validando el tipo de separador e utilizado en el path
	        report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), tituloReporte);
	       
	        
	        //-----------------------------------------Abrir reporte------------------------------------------------------------
	        report.openReport(filename);
	        report.font.useFont(iTextBaseFont.FONT_TIMES, true, true, true);
	        
//	      --------------------------------Nombre y Registro Médico del responsable-------------------------------------------------
	        report.font.setFontSizeAndAttributes(12,true,false,false);
	        report.document.addParagraph("Profesional Responsable: "+hojaOftalmologicaForm.getMedicoRetinaVitreo() ,report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);
	        report.document.addParagraph("Fecha de Grabación: "+hojaOftalmologicaForm.getFechaRetinaVitreo() ,report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
	        //---------------------------------------------------------Informacion del Paciente----------------------------------------------------
	        report.document.addParagraph("INFORMACIÓN PERSONAL ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);
	        report.font.setFontSizeAndAttributes(11,false,false,false);
	        report.document.addParagraph("Identificación : "+ paciente.getCodigoTipoIdentificacionPersona() + " "+  paciente.getNumeroIdentificacionPersona() ,report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
	        report.document.addParagraph("Apellidos y Nombres : " + paciente.getApellidosNombresPersona(false) ,report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
	        report.document.addFooter("Mauricio Ruiz", 20, false, false);
	        
	        //------------------------ Se muestran las imágenes de retina del ojo derecho e izquiedo ----------------------------------------------------------//
	        if(UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getImagenRetinaOD()) || UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getImagenRetinaOS()))
	        		{
				        //Se crea la sección indicando el número de filas y columnas y cellspacing
				        report.createSection("imagenRetina","tablaImgRetina",2,2,0);
				        //Se establece el borde de la tabla con su color y grosor
				        report.getSectionReference("imagenRetina").setTableBorder("tablaImgRetina", 0x000000, 0.2f);
				        //Tamaño del borde de las celdas
				        report.getSectionReference("imagenRetina").setTableCellBorderWidth("tablaImgRetina", 0.2f);
				        //Color de fondo de la celda
				        report.getSectionReference("imagenRetina").setTableCellsBackgroundColor(0xEBEBEB);
				        //Color, tamaño y estilo de la letra
				        report.font.setFontAttributes(0x000000, 12, true, false, false);
				         //Se agrega la celda a la tabla
				        report.getSectionReference("imagenRetina").addTableTextCellAligned("tablaImgRetina", "Retina OD", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
				        //Se agrega la celda a la tabla
				        report.getSectionReference("imagenRetina").addTableTextCellAligned("tablaImgRetina", "Retina OS", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
				
				        //Color de fondo de la celda
				        report.getSectionReference("imagenRetina").setTableCellsBackgroundColor(0xFFFFFF);
				        
				        //Si se adjunto retina ojo derecho
				        if(UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getImagenRetinaOD()))
				        	report.getSectionReference("imagenRetina").addTableImageCell("tablaImgRetina", filePath+"/fotos/"+hojaOftalmologicaForm.getImagenRetinaOD(), 75f, 1, 0);
				        
				        //Si se adjunto retina ojo izquierdo
				        if (UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getImagenRetinaOS()))
				        	report.getSectionReference("imagenRetina").addTableImageCell("tablaImgRetina", filePath+"/fotos/"+hojaOftalmologicaForm.getImagenRetinaOS(), 75f, 1, 1);
				        
				        //Adionar la sección al documento pdf 
				        report.addSectionToDocument("imagenRetina");
	        		}

	        //------------------------ Se muestran las imágenes de vítreo del ojo derecho e izquiedo ----------------------------------------------------------//
	        if(UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getImagenVitreoOD()) || UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getImagenVitreoOS()))
	        		{
				        //Se crea la sección indicando el número de filas y columnas y cellspacing
				        report.createSection("imagenVitreo","tablaImgVitreo",2,2,0);
				        //Se establece el borde de la tabla con su color y grosor
				        report.getSectionReference("imagenVitreo").setTableBorder("tablaImgVitreo", 0x000000, 0.2f);
				        //Tamaño del borde de las celdas
				        report.getSectionReference("imagenVitreo").setTableCellBorderWidth("tablaImgVitreo", 0.2f);
				        //Color de fondo de la celda
				        report.getSectionReference("imagenVitreo").setTableCellsBackgroundColor(0xEBEBEB);
				        //Color, tamaño y estilo de la letra
				        report.font.setFontAttributes(0x000000, 12, true, false, false);
				         //Se agrega la celda a la tabla
				        report.getSectionReference("imagenVitreo").addTableTextCellAligned("tablaImgVitreo", "Vítreo OD", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
				        //Se agrega la celda a la tabla
				        report.getSectionReference("imagenVitreo").addTableTextCellAligned("tablaImgVitreo", "Vìtreo OS", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
				
				        //Color de fondo de la celda
				        report.getSectionReference("imagenVitreo").setTableCellsBackgroundColor(0xFFFFFF);
				        
				        //Si se adjunto vitreo ojo derecho
				        if(UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getImagenVitreoOD()))
				        	report.getSectionReference("imagenVitreo").addTableImageCell("tablaImgVitreo", filePath+"/fotos/"+hojaOftalmologicaForm.getImagenVitreoOD(), 75f, 1, 0);
				        
				        //Si se adjunto vitreo ojo izquierdo
				        if (UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getImagenVitreoOS()))
				        	report.getSectionReference("imagenVitreo").addTableImageCell("tablaImgVitreo", filePath+"/fotos/"+hojaOftalmologicaForm.getImagenVitreoOS(), 75f, 1, 1);
				        
				        //Adionar la sección al documento pdf 
				        report.addSectionToDocument("imagenVitreo");
	        		}
   
	        
	        
        //Verificar que no esté vacía las observaciones para que se muestre la sección
        if (!hojaOftalmologicaForm.getObservacionRetinaVitreo().trim().equals(""))
        	{
		        //-------------------------------------------------------Observaciones de Retina y Vítreo------------------------------------------------------------------//
		        report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		         //Se crea la sección indicando el número de filas y columnas y cellspacing
		        report.createSection("observacionRetina","tablaObserv",2,1,0);
		        //Se establece el borde de la tabla con su color y grosor
		        report.getSectionReference("observacionRetina").setTableBorder("tablaObserv", 0x000000, 0.2f);
		        //Tamaño del borde de las celdas
		        report.getSectionReference("observacionRetina").setTableCellBorderWidth("tablaObserv", 0.2f);
		        //Color de fondo de la celda
		        report.getSectionReference("observacionRetina").setTableCellsBackgroundColor(0xEBEBEB);
		        //Color, tamaño y estilo de la letra
		        report.font.setFontAttributes(0x000000, 12, true, false, false);
		        //Se agrega la celda a la tabla
		        report.getSectionReference("observacionRetina").addTableTextCellAligned("tablaObserv", "Observaciones", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		        
		        //Color de fondo de la celda
		        report.getSectionReference("observacionRetina").setTableCellsBackgroundColor(0xFFFFFF);
		        //Color, tamaño y estilo de la letra
		        report.font.setFontAttributes(0x000000, 11, false, false, false);
		        //Se agrega la celda a la tabla
		        report.getSectionReference("observacionRetina").addTableTextCellAligned("tablaObserv", hojaOftalmologicaForm.getObservacionRetinaVitreo(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		        
		        //Pie Historia Clinica
		        report.getSectionReference("observacionRetina").addTableTextCellAligned("tablaObserv", institucionBasica.getPieHistoriaClinica(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		        
		        //Adionar la sección al documento pdf 
		        report.addSectionToDocument("observacionRetina");
        	}//fin if
	        
	 //-------------------------------------------------------------------------------------------------------------------------------
			
        	//Cerrando el reporte
        	report.closeReport(medico.getNombreUsuario());

	}
	
}
