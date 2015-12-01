/*
 * Creado el 07-oct-2005
 * por Julian Montoya
 */
package com.princetonsa.pdf;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import util.UtilidadCadena;
import util.UtilidadFecha;
import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;

import com.lowagie.text.BadElementException;
import com.princetonsa.action.hojaOftalmologica.AntecedentesOftalmologicosAction;
import com.princetonsa.actionform.hojaOftalmologica.AntecedentesOftalmologicosForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author Julian Montoya
 * 
 * Princeton S.A. (ParqueSoft Manizales)
 */
public class AntecedentesOftalmologicosPdf {

	
	 /**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private static Logger logger = Logger.getLogger(AntecedentesOftalmologicosAction.class);
    


	   public  static void pdfAntecedentesOftalmoloficosPersonales(String filename, AntecedentesOftalmologicosForm antecedesOftalmologicosForm,UsuarioBasico medico, PersonaBasica paciente, HttpServletRequest request)
	   {
		 //Variable para manejar el reporte
        PdfReports report = new PdfReports();
        int filas=0;

        //--Barrer el HashMap para insertar los datos seleccionados   
		Vector codigos = (Vector) antecedesOftalmologicosForm.getMapa("codigosTipoEnferOftal");
		
			for(int i=0; i<codigos.size();i++)
			{ 
				//------El codigo del tipo de enfermedad oftalmologica 
				int tipoEnfer = Integer.parseInt(codigos.elementAt(i)+"");
				
				String desdeCuando = (String)antecedesOftalmologicosForm.getMapa("desdeCuando_"+tipoEnfer);
				String tratamiento = (String)antecedesOftalmologicosForm.getMapa("tratamiento_"+tipoEnfer);
				if (UtilidadCadena.noEsVacio(desdeCuando) || UtilidadCadena.noEsVacio(tratamiento) )
				{
					filas++;
				}
			}
		//-Crear el String para enviar los datos al reporte 	
		String[] datos = new String[filas * 3];
		int con = 0;
		for(int i=0; i<codigos.size();i++)
		{ 
			//------El codigo del tipo de enfermedad oftalmologica 
			int tipoEnfer = Integer.parseInt(codigos.elementAt(i)+"");
			
			String nombre = (String)antecedesOftalmologicosForm.getMapa("nombre_"+tipoEnfer);
			String desdeCuando = (String)antecedesOftalmologicosForm.getMapa("desdeCuando_"+tipoEnfer);
			String tratamiento = (String)antecedesOftalmologicosForm.getMapa("tratamiento_"+tipoEnfer);
			if (UtilidadCadena.noEsVacio(desdeCuando) || UtilidadCadena.noEsVacio(tratamiento) )
			{
				datos[con] = nombre; con++; 
				datos[con] = desdeCuando; con++; 
				datos[con] = tratamiento; con++; 
			}
		}
        
   	 	//-Titulos de la tabla  
		String[] titulosCol = {"Nombre","Desde Cuando","Tratamiento"};
		String[] titulosQui = {"Nombre Procedimiento","Fecha","Causa"};
		//String[] titulosObs = {"Observaciones Generales"};
		
        
       //Tomando la ruta definida en el web.xml para generar el reporte.
       
       //titulo del reporte
       String tituloReporte="ANTECEDENTES OFTALMOLOGICOS PERSONALES (" + UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() +")";
       
       InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
       
       report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), tituloReporte);
       
       //-----------------------------------------Abrir Reporte------------------------------------------------------------
       report.openReport(filename);
       report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
       
       //--------------------------------Nombre y Registro Médico del responsable------------------------------------------
       report.font.setFontSizeAndAttributes(10,true,false,false);
       report.document.addParagraph("Profesional Responsable: "+ medico.getNombreRegistroMedico(),report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);
       
       
       //---------------------------------------------------------Informacion del Paciente----------------------------------------------------
       report.font.setFontSizeAndAttributes(12,true,false,false);
       report.document.addParagraph("INFORMACIÓN PERSONAL ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);
       report.font.setFontSizeAndAttributes(9,false,false,false);
       report.document.addParagraph("Identificación : "+ paciente.getCodigoTipoIdentificacionPersona() + " "+  paciente.getNumeroIdentificacionPersona() ,report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
       report.document.addParagraph("Apellidos y Nombres : " + paciente.getApellidosNombresPersona(false),report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
       

       report.setReportTitleSectionAtributes(0x000000, 0xEBEBEB,0x000000, 12);
	   report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 11);

       //---Colocar los datos al reporte
		report.createColSection("medico", "Antecedentes Oftalmológicos Medicos", titulosCol, datos, 3);
		
		//-Para adicionar la seccion solamente si hay datos
		if (con > 0)
			report.addSectionToDocument("medico");
		
		       
       //----------------------------------------------------------------------------------
       //-------------------Insertar los antecedentes quirurgicos--------------------------
       //----------------------------------------------------------------------------------
       
        filas = 0;
		Vector codigosQuirur = (Vector) antecedesOftalmologicosForm.getMapa("codigosReg");
		System.out.print("[AntecedentesOftalmologicos ]   codigosQuirur --> "+codigosQuirur);
		String proce = (String)antecedesOftalmologicosForm.getMapa("codigosQuirur");
		String [] vec = proce.split("-");
		
		//-Crear el String para enviar los datos al reporte
		String[] datosQuirur = new String[vec.length * 3];
	
		con = 0;

		if (UtilidadCadena.noEsVacio(proce))
		for(int i=0; i<vec.length; i++)
		{
			//------El codigo del tipo de enfermedad oftalmologica 
			int cod = Integer.parseInt(vec[i]);
			String nombre = (String)antecedesOftalmologicosForm.getMapa("nombreQuirur_"+cod);
			String fecha = (String)antecedesOftalmologicosForm.getMapa("fechaQuirur_"+cod);
			String causa = (String)antecedesOftalmologicosForm.getMapa("causaQuirur_"+cod); 
			
			datosQuirur[con] = nombre; con++; 
			datosQuirur[con] = fecha; con++; 
			datosQuirur[con] = causa; con++; 
		}


		//------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------
		//---Colocar los datos al reporte
		report.createColSection("quirur", "Antecedentes Oftalmológicos Quirúgicos", titulosQui, datosQuirur, 3);
       //-Adicionar la seccion al reporte
		if (con > 0)
		{
		       report.addSectionToDocument("quirur");
		}
		
		
		   try
	       {
		   	float widths[]={(float) 4,(float) 2,(float) 4};
		    report.getSectionReference("medico").getTableReference("parentTable").setWidths(widths);
		    float width[]={(float) 4,(float) 2,(float) 4};
		    report.getSectionReference("quirur").getTableReference("parentTable").setWidths(width);
	       }
		   
	       catch (BadElementException e)
	       {
	       	logger.error("Se presentó error generando el pdf " + e);
	       }
	       
	       
	       
	       if(!antecedesOftalmologicosForm.getObservacionesPersonales().trim().equals(""))
	       {
	       
	       report.setReportTitleSectionAtributes(0x000000, 0xEBEBEB,0x000000, 12);
		   report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 11);
			
	        //Se crea la sección indicando el número de filas y columnas y cellspacing
	        report.createSection("observacion","tablaObserv",2,1,0);
	        
	        //Se establece el borde de la tabla con su color y grosor
	        //report.getSectionReference("observacion").setTableBorder("tablaObserv", 0x000000, 1.0f);
	        
	        //Tamaño del borde de las celdas
	        //report.getSectionReference("observacion").setTableCellBorderWidth("tablaObserv", 0.1f);
	        //Color de fondo de la celda
	        report.getSectionReference("observacion").setTableCellsBackgroundColor(0xEBEBEB);
	        
	        //Color, tamaño y estilo de la letra
	        report.font.setFontAttributes(0x000000, 12, true, false, false);
	        
	        //Se agrega la celda a la tabla
	        report.getSectionReference("observacion").addTableTextCellAligned("tablaObserv", "Observaciones Generales", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
	        
	        //Color de fondo de la celda
	        report.getSectionReference("observacion").setTableCellsBackgroundColor(0xFFFFFF);
	        //Color, tamaño y estilo de la letra
	        
	        report.font.setFontAttributes(0x000000, 11, false, false, false);
	        //Se agrega la celda a la tabla
	        report.getSectionReference("observacion").addTableTextCellAligned("tablaObserv", antecedesOftalmologicosForm.getObservacionesPersonales(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
	        
	        //Adionar la sección al documento pdf 
	        report.addSectionToDocument("observacion");
	       }
	       
        //---------------------------------------Cerrar el reporte ---------------------------
        report.closeReport();
	   }

	   public  static void pdfAntecedentesOftalmoloficosFamiliares(String filename, AntecedentesOftalmologicosForm antecedesOftalmologicosForm,UsuarioBasico medico, PersonaBasica paciente, HttpServletRequest request)
	   {
		 //Variable para manejar el reporte
        PdfReports report = new PdfReports();
        int filas=0;

   	 	//-Titulos de la tabla  
		String[] titulosCol = {"Nombre","Quién Padece"};
		
        
       //Tomando la ruta definida en el web.xml para generar el reporte.
       String filePath=ValoresPorDefecto.getFilePath();
       
       //titulo del reporte
       String tituloReporte="ANTECEDENTES OFTALMOLOGICOS FAMILIARES  (" + UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() +")";
       
       InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
       report.setReportBaseHeader1(institucionBasica,medico.getNit(), tituloReporte);
       
       //-----------------------------------------Abrir Reporte------------------------------------------------------------
       
       
       report.openReport(filename);
       report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
       
       //--------------------------------Nombre y Registro Médico del responsable------------------------------------------
       report.font.setFontSizeAndAttributes(10,true,false,false);
       report.document.addParagraph("Profesional Responsable: "+ medico.getNombreRegistroMedico(),report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);
       
       
       //---------------------------------------------------------Informacion del Paciente----------------------------------------------------
       report.font.setFontSizeAndAttributes(12,true,false,false);
       report.document.addParagraph("INFORMACIÓN PERSONAL ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);
       report.font.setFontSizeAndAttributes(9,false,false,false);
       report.document.addParagraph("Identificación : "+ paciente.getCodigoTipoIdentificacionPersona() + " "+  paciente.getNumeroIdentificacionPersona() ,report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
       report.document.addParagraph("Apellidos y Nombres : " + paciente.getApellidosNombresPersona(false) ,report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
       

       report.setReportTitleSectionAtributes(0x000000, 0xEBEBEB,0x000000, 12);
	   report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 11);

       //---Colocar los datos al reporte
		//report.createColSection("principal", "Antecedentes Oftalmológicos Familiares", titulosCol, datos, 3);
		       
       //----------------------------------------------------------------------------------
	   //-Obtener la informacion 	
	   
        filas = 0; 
        String codigosParent = (String) antecedesOftalmologicosForm.getMapa("codParentesco");
        String nombresParent = (String) antecedesOftalmologicosForm.getMapa("nomParentesco");

        String tmp = ""; 
        for (int i = 0; i < codigosParent.length(); i++)
        {
  		  if (codigosParent.charAt(i) == '$' )
		  	tmp = tmp + "-";
		  else
		  	tmp = tmp + codigosParent.charAt(i);
		}
        codigosParent = tmp; tmp = "";
        
        for (int i = 0; i < nombresParent.length(); i++)
        {
		  if (nombresParent.charAt(i) == '$' )
		  	tmp = tmp + "-";
		  else
		  	tmp = tmp + nombresParent.charAt(i);
		}
        nombresParent = tmp;
        String [] codVec = codigosParent.split("-"); 
        String [] nomVec = nombresParent.split("-"); 
        
        
        String codigosEn = (String) antecedesOftalmologicosForm.getMapa("codEnfer");
        String [] codiEnfer = codigosEn.split("@");
        
        for (int i = 0; i < codiEnfer.length; i++)
        {
        	String vecCod = (String) antecedesOftalmologicosForm.getMapa("codEnfer_" + codiEnfer[i]);
        	if (UtilidadCadena.noEsVacio(vecCod))
        	{
        		filas++;
        	}
        	
		}
        
        
        String[] datos = new String[filas * 2];
        int cont = 0;
        //-Barrer 
        for (int i = 0; i < codiEnfer.length; i++)
        {
        	String nombre = (String) antecedesOftalmologicosForm.getMapa("nombreEnfer_" + codiEnfer[i]);
        	String vecCod = (String) antecedesOftalmologicosForm.getMapa("codEnfer_" + codiEnfer[i]);
        	

        	if (UtilidadCadena.noEsVacio(vecCod))
        	{
        		datos[cont] = nombre; cont++;
        		datos[cont] = nombresParientes (vecCod, codVec, nomVec); cont++;
        	}
        	
		}
        
        
        //------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------
		//---Colocar los datos al reporte
		report.createColSection("principal", "Antecedentes Oftalmológicos Familiares", titulosCol, datos, 3);
		
		
		   try
	       {
		   	float widths[]={(float) 5,(float) 5};
		    report.getSectionReference("principal").getTableReference("parentTable").setWidths(widths);
	       }
	       catch (BadElementException e)
	       {
	       	logger.error("Se presentó error generando el pdf " + e);
	       }
	       
	       //-Adicionar las secciones al reporte
	       report.addSectionToDocument("principal");
	   
	       if (!antecedesOftalmologicosForm.getObservacionesFamiliares().trim().equals(""))
	       {
	         report.setReportTitleSectionAtributes(0x000000, 0xEBEBEB,0x000000, 12);
			 report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 11);
			
	        //Se crea la sección indicando el número de filas y columnas y cellspacing
	        report.createSection("observacion","tablaObserv",2,1,0);
	        report.getSectionReference("observacion").setTableCellsBackgroundColor(0xEBEBEB);
	        
	        //Color, tamaño y estilo de la letra
	        report.font.setFontAttributes(0x000000, 12, true, false, false);
	        
	        //Se agrega la celda a la tabla
	        report.getSectionReference("observacion").addTableTextCellAligned("tablaObserv", "Observaciones Generales", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
	        
	        //Color de fondo de la celda
	        report.getSectionReference("observacion").setTableCellsBackgroundColor(0xFFFFFF);
	        //Color, tamaño y estilo de la letra
	        report.font.setFontAttributes(0x000000, 11, false, false, false);
	        //Se agrega la celda a la tabla
	        report.getSectionReference("observacion").addTableTextCellAligned("tablaObserv", antecedesOftalmologicosForm.getObservacionesFamiliares(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
	        
	        //Adionar la sección al documento pdf 
	        report.addSectionToDocument("observacion");
	       } 
	       
        //---------------------------------------Cerrar el reporte ---------------------------
        report.closeReport();
	   }

	   public  static String nombresParientes (String codigos, String [] codVec, String [] nomVec)
	   {
	   	String tmp="";  
	   	String [] codigo = codigos.split("-");
	   	
		   	for (int i = 0; i < codigo.length; i++)
		   	{
		   		for (int j = 0; j < codVec.length; j++) 
		   		{
					if (Integer.parseInt(codigo[i]) == Integer.parseInt(codVec[j]))
					{
						
						if (tmp.equals(""))
							tmp = nomVec[j];
						else
							tmp = tmp + ", "+  nomVec[j];
					}
				}
			}
	   	
		   	return tmp;
	   }
	   
	} //-FIN DE LA CLASE


