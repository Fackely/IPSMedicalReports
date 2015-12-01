/*
 * Created on Jan 27, 2005
 *
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.pdf;

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseFont;


import com.lowagie.text.BadElementException;
import com.princetonsa.actionform.triage.TriageForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author Sebastián Gómez
 *
 *Clase que maneja la impresión del triage
 */
public class TriagePdf {

	 /**
     * Clase para manejar los logs de esta clase
     */
    private static Logger triagePdfLogger=Logger.getLogger(TriagePdf.class);
    
    /**
     * Método que imprime un registro triage
     * 
	 * @param filename Nombre del archivo con el que
	 * se desea generar el PDF
	 * @param triageForm Form del que se saca el triage
     * @param medico Médico que está generando el PDF
     */
	public  static void pdfTriage(String filename, TriageForm triageForm, UsuarioBasico medico, HttpServletRequest request) 
	{
		PdfReports report = new PdfReports();
		report.setUsuario(medico.getLoginUsuario());
		
		      
		//GENERACION DE CABECERA
		//Ruta por defecto donde se genera los PDF
		String tituloDocumentos="TRIAGE Nro."+triageForm.getConsecutivo()+"-"+triageForm.getConsecutivo_fecha()+"   ("+triageForm.getFecha()+" "+triageForm.getHora()+") ";
		
		InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), tituloDocumentos);
				
		//abrir reporte
        report.openReport(filename);
        
        
        //********ADICION DE AVISO QUE NO RESPONDIÓ LLAMADO*********************************
        if(UtilidadTexto.getBoolean(triageForm.getNoRespondioLlamado()))
        {
        	 report.createSection("seccionAviso","tablaTriage",1,1,0);
             report.getSectionReference("seccionAviso").setTableBorder("tablaTriage", 0xFFFFFF, 0.0f);
             report.getSectionReference("seccionAviso").setTableCellBorderWidth("tablaTriage", 0.5f);
             report.getSectionReference("seccionAviso").setTableCellsDefaultColors("tablaTriage", 0xFFFFFF, 0xFFFFFF);
             report.getSectionReference("seccionAviso").setTableCellsColSpan(1);
             report.font.setFontSizeAndAttributes(10,true,false,false);
	        report.getSectionReference("seccionAviso").addTableTextCell("tablaTriage", "El paciente no respondió llamado", report.font);
	        report.addSectionToDocument("seccionAviso");
        }
        //*********************************************************************************
        
        
        //*************VALIDACIONES*****************************************
        //SE MIRA QUE EL TIPO DE IDENTIFICACIÓN EXISTA
        int cabecera=6; //hasta ahora la cabecera lleva 6 elementos
        int hay_id=1; //indica si hay ID de paciente
        int hay_fecha=1; //indica si hay fecha
        int hay_afiliado=1; //indica si hay afiliado beneficiario
        int hay_antecedentes=1; //indica si hay antecedentes
        
		if(triageForm.getTipo_id().equals("null-null")){
				cabecera--;
				hay_id=0;
			}
		if(triageForm.getFecha_nacimiento().equals("")){
				hay_fecha=0;
			}
		if(triageForm.getTipo_afiliado().equals("Cotizante")||triageForm.getTipo_afiliado().equals("")){
				hay_afiliado=0;
			}
		
		if(triageForm.getAntecedentes().equals("")){
			hay_antecedentes=0;
		}
		//SE MIRA SI EXISTE CONVENIO
		String convenio;
		if(triageForm.getConvenio().equals(""))
			if(triageForm.getOtro_convenio().equals(""))
				convenio="";	
			else
				convenio=triageForm.getOtro_convenio();
		else
			convenio=triageForm.getConvenio();
		
        //********************************************************************
        
		// SECCION CENTRO ATENCION ****************************************************************
        String cabecerasCentroAtencion="Centro de Atención:";
		String datosCentroAtencion=triageForm.getNombreCentroAtencion();
		
		report.createSection("seccionCentroAtencion","tablaCentroAtencion",1,2,0);
        report.getSectionReference("seccionCentroAtencion").setTableBorder("tablaCentroAtencion", 0xFFFFFF, 0.0f);
        report.getSectionReference("seccionCentroAtencion").setTableCellBorderWidth("tablaCentroAtencion", 0.5f);
        report.getSectionReference("seccionCentroAtencion").setTableCellsDefaultColors("tablaCentroAtencion", 0xFFFFFF, 0xFFFFFF);
        report.getSectionReference("seccionCentroAtencion").setTableCellsColSpan(1);
        
        report.font.setFontSizeAndAttributes(10,true,false,false);
    	report.getSectionReference("seccionCentroAtencion").addTableTextCell("tablaCentroAtencion",cabecerasCentroAtencion, report.font);
    	report.font.setFontSizeAndAttributes(10,false,false,false);
    	report.getSectionReference("seccionCentroAtencion").addTableTextCell("tablaCentroAtencion",datosCentroAtencion, report.font);
        
        float widths0[]={(float)2.8,(float) 7.2};//segun numeor de columnas
        try {
           report.getSectionReference("seccionCentroAtencion").getTableReference("tablaCentroAtencion").setWidths(widths0);
             }
        catch (BadElementException e)
                        {
                triagePdfLogger.error("Se presentó error generando el pdf " +e);
                        }
        //adicionar la seccion al reporte
        report.addSectionToDocument("seccionCentroAtencion");
		// *****************************************************
		
		
        //PRIMERA SECCION ********************************************************
        String[] cabeceras = {
    			"Paciente:", 
    			(triageForm.getTipo_id().split("-"))[0]+":",
    			"Edad:",
    			"Convenio:",
    			"Tipo Afiliado:",
    			"Id. Cotizante:",
    		};
    		 String[] datosReporte=
             {
    		   triageForm.getPrimer_nombre()+" "+triageForm.getSegundo_nombre()+" "+triageForm.getPrimer_apellido()+" "+triageForm.getSegundo_apellido(),
    		   triageForm.getNumero_id(),
    		   triageForm.getEdad(),
    		   convenio,
    		   triageForm.getTipo_afiliado(),
    		   triageForm.getId_cotizante(),
             };
    		    
    	if(!triageForm.getDescripcionClasificacionTriage().isEmpty())
    	{
    		report.document.addParagraph("Clasificación: "+triageForm.getDescripcionClasificacionTriage(), 10);
    	}
        report.createSection("seccionTriage","tablaTriage",3,6,0);
        report.getSectionReference("seccionTriage").setTableBorder("tablaTriage", 0xFFFFFF, 0.0f);
        report.getSectionReference("seccionTriage").setTableCellBorderWidth("tablaTriage", 0.5f);
        report.getSectionReference("seccionTriage").setTableCellsDefaultColors("tablaTriage", 0xFFFFFF, 0xFFFFFF);
        report.getSectionReference("seccionTriage").setTableCellsColSpan(1);
        for(int k=0; k<cabeceras.length; k++)
        {
        	if((hay_id==0&&k==1)||(hay_fecha==0&&k==2)||(hay_afiliado==0&&k==5)){
        		report.font.setFontSizeAndAttributes(10,true,false,false);
	        	report.getSectionReference("seccionTriage").addTableTextCell("tablaTriage"," ", report.font);
	        	report.font.setFontSizeAndAttributes(10,false,false,false);
	        	report.getSectionReference("seccionTriage").addTableTextCell("tablaTriage"," ", report.font);
        	}
        	else{
	        	report.font.setFontSizeAndAttributes(10,true,false,false);
	        	report.getSectionReference("seccionTriage").addTableTextCell("tablaTriage", cabeceras[k], report.font);
	        	report.font.setFontSizeAndAttributes(10,false,false,false);
	        	report.getSectionReference("seccionTriage").addTableTextCell("tablaTriage", datosReporte[k], report.font);
        	}

        }
        report.getSectionReference("seccionTriage").setTableCellsColSpan(1);
        report.font.setFontSizeAndAttributes(10,true,false,false);
    	report.getSectionReference("seccionTriage").addTableTextCell("tablaTriage","¿Accidente Trabajo?:" , report.font);
    	report.font.setFontSizeAndAttributes(10,false,false,false);
    	report.getSectionReference("seccionTriage").addTableTextCell("tablaTriage", UtilidadTexto.getBoolean(triageForm.getAccidenteTrabajo())?"Sí":"No", report.font);
    	
    	if(UtilidadTexto.getBoolean(triageForm.getAccidenteTrabajo()))
    	{
    		report.getSectionReference("seccionTriage").setTableCellsColSpan(1);
            report.font.setFontSizeAndAttributes(10,true,false,false);
        	report.getSectionReference("seccionTriage").addTableTextCell("tablaTriage","ARP Afiliado:" , report.font);
        	report.getSectionReference("seccionTriage").setTableCellsColSpan(3);
        	report.font.setFontSizeAndAttributes(10,false,false,false);
        	report.getSectionReference("seccionTriage").addTableTextCell("tablaTriage", triageForm.getArpAfiliado(), report.font);
        	
    	}
    	else
    	{
    		report.getSectionReference("seccionTriage").setTableCellsColSpan(4);
    		report.font.setFontSizeAndAttributes(10,false,false,false);
        	report.getSectionReference("seccionTriage").addTableTextCell("tablaTriage","" , report.font);
    	}
        
        float widths[]={(float)1.05,(float) 3.7,(float) 1.3,(float) 1.2,(float) 1.2,(float) 1.55};
        try {
           report.getSectionReference("seccionTriage").getTableReference("tablaTriage").setWidths(widths);
             }
        catch (BadElementException e)
                        {
                triagePdfLogger.error("Se presentó error generando el pdf " +e);
                        }
        //adicionar la seccion al reporte
        report.addSectionToDocument("seccionTriage");
        //*****************************************************
        
        //SEGUNDA SECCION
        String[] cabecerasTriage2={
				"Sistema Motivo Consulta Urg.:",
				"Antecedentes:",
				"Signos Vitales:"
				
			};
        
        String detalleSignosVitales = "";
        for(int i=0;i<triageForm.getNumSignos();i++)
        {
        	if(!triageForm.getSignosVitales("valor_"+i).toString().equals(""))
        	{
	        	if(!detalleSignosVitales.equals(""))
	        		detalleSignosVitales += ",   ";
	        	detalleSignosVitales += triageForm.getSignosVitales("nombre_"+i) + ": " + triageForm.getSignosVitales("valor_"+i) +  " " + triageForm.getSignosVitales("unidad_medida_"+i);
        	}
        }
        
		String[] datosTriage2={
				triageForm.getMotivo_consulta(),
				triageForm.getAntecedentes(),
				detalleSignosVitales
				
			};
		report.createSection("seccionTriage2","tablaTriage2",3,2,0);
        report.getSectionReference("seccionTriage2").setTableBorder("tablaTriage2", 0xFFFFFF, 0.0f);
        report.getSectionReference("seccionTriage2").setTableCellBorderWidth("tablaTriage2", 0.5f);
        report.getSectionReference("seccionTriage2").setTableCellsDefaultColors("tablaTriage2", 0xFFFFFF, 0xFFFFFF);
        report.getSectionReference("seccionTriage2").setTableCellsColSpan(1);
        for(int k=0; k<cabecerasTriage2.length; k++)
        {
        	if((hay_antecedentes==0&&k==1)){
        		report.font.setFontSizeAndAttributes(10,true,false,false);
	        	report.getSectionReference("seccionTriage2").addTableTextCell("tablaTriage2"," ", report.font);
	        	report.font.setFontSizeAndAttributes(10,false,false,false);
	        	report.getSectionReference("seccionTriage2").addTableTextCell("tablaTriage2"," ", report.font);
        	}
        	else{
	        
        	report.font.setFontSizeAndAttributes(10,true,false,false);
        	report.getSectionReference("seccionTriage2").addTableTextCell("tablaTriage2", cabecerasTriage2[k], report.font);
        	report.font.setFontSizeAndAttributes(10,false,false,false);
        	report.getSectionReference("seccionTriage2").addTableTextCell("tablaTriage2", datosTriage2[k], report.font);

        	}
        }
        float widths2[]={(float)2.8,(float) 7.2};//segun numeor de columnas
        try {
           report.getSectionReference("seccionTriage2").getTableReference("tablaTriage2").setWidths(widths2);
             }
        catch (BadElementException e)
                        {
                triagePdfLogger.error("Se presentó error generando el pdf " +e);
                        }
        //adicionar la seccion al reporte
        report.addSectionToDocument("seccionTriage2");
		// *****************************************************
		
        //TERCERA SECCION
       
        String[] cabecerasTriage3={
        		"Signos y Síntomas por Sistema:",
        		"Calificación: ",
				"Destino Paciente: ",
				triageForm.getNombresala().equals("")?"":"Sala de Espera: "
				};
        String[] datosTriage3={
        		triageForm.getSignoSintoma(),
        		triageForm.getCategorias_triage().equals("")?"":(triageForm.getCategorias_triage().split(ConstantesBD.separadorSplit))[1],
				triageForm.getDestino(),
				triageForm.getNombresala()
				};
        report.createSection("seccionTriage3","tablaTriage3",2,4,0);
        report.getSectionReference("seccionTriage3").setTableBorder("tablaTriage3", 0xFFFFFF, 0.0f);
        report.getSectionReference("seccionTriage3").setTableCellBorderWidth("tablaTriage3", 0.5f);
        report.getSectionReference("seccionTriage3").setTableCellsDefaultColors("tablaTriage3", 0xFFFFFF, 0xFFFFFF);
        report.getSectionReference("seccionTriage3").setTableCellsColSpan(1);
        for(int k=0; k<cabecerasTriage3.length; k++)
        {
        	report.font.setFontSizeAndAttributes(10,true,false,false);
        	report.getSectionReference("seccionTriage3").addTableTextCell("tablaTriage3", cabecerasTriage3[k], report.font);
        	report.font.setFontSizeAndAttributes(10,false,false,false);
        	report.getSectionReference("seccionTriage3").addTableTextCell("tablaTriage3", datosTriage3[k], report.font);

        }
        
        float widths3[]={(float)2.8,(float) 2.9,(float) 1.5,(float) 2.8};//segun numeor de columnas
        try {
           report.getSectionReference("seccionTriage3").getTableReference("tablaTriage3").setWidths(widths3);
             }
        catch (BadElementException e)
                        {
                triagePdfLogger.error("Se presentó error generando el pdf " +e);
                        }

         //adicionar la seccion al reporte
        report.addSectionToDocument("seccionTriage3");
		// *****************************************************
		
		// CUARTA SECCION
        String[] cabecerasTriage4={
        		"Observaciones Generales:"
				};
        
        String[] datosTriage4={
        		triageForm.getObservaciones_generales()
				};
        float widths4[]={(float)2.5,(float) 8};//segun numeor de columnas
        if(!triageForm.getObservaciones_generales().equals("")){
			        
			        report.createSection("seccionTriage4","tablaTriage4",1,2,0);
			        report.getSectionReference("seccionTriage4").setTableBorder("tablaTriage4", 0xFFFFFF, 0.0f);
			        report.getSectionReference("seccionTriage4").setTableCellBorderWidth("tablaTriage4", 0.5f);
			        report.getSectionReference("seccionTriage4").setTableCellsDefaultColors("tablaTriage4", 0xFFFFFF, 0xFFFFFF);
			        report.getSectionReference("seccionTriage4").setTableCellsColSpan(1);
			        for(int k=0; k<cabecerasTriage4.length; k++)
			        {
			        	report.font.setFontSizeAndAttributes(10,true,false,false);
			        	report.getSectionReference("seccionTriage4").addTableTextCell("tablaTriage4", cabecerasTriage4[k], report.font);
			        	report.font.setFontSizeAndAttributes(10,false,false,false);
			        	report.getSectionReference("seccionTriage4").addTableTextCell("tablaTriage4", datosTriage4[k], report.font);
			
			        }
			        
			        
			        try {
			           report.getSectionReference("seccionTriage4").getTableReference("tablaTriage4").setWidths(widths4);
			             }
			        catch (BadElementException e)
			                        {
			                triagePdfLogger.error("Se presentó error generando el pdf " +e);
			                        }
			
			         //adicionar la seccion al reporte
			        report.addSectionToDocument("seccionTriage4");
        }
		// *****************************************************
        //QUINTA SECCION
        String[] datosTriage5={
        		
				triageForm.getUsuario()
				};
        report.createSection("seccionTriage5","tablaTriage5",1,1,0);
        report.getSectionReference("seccionTriage5").setTableBorder("tablaTriage5", 0xFFFFFF, 0.0f);
        report.getSectionReference("seccionTriage5").setTableCellBorderWidth("tablaTriage5", 0.5f);
        report.getSectionReference("seccionTriage5").setTableCellsDefaultColors("tablaTriage5", 0xFFFFFF, 0xFFFFFF);
        report.getSectionReference("seccionTriage5").setTableCellsColSpan(1);
        for(int k=0; k<datosTriage5.length; k++)
        {
        	report.font.setFontSizeAndAttributes(10,false,false,false);
        	report.getSectionReference("seccionTriage5").addTableTextCell("tablaTriage5", datosTriage5[k], report.font);

        }
        
        float widths5[]={(float) 10};//segun numeor de columnas
        try {
           report.getSectionReference("seccionTriage5").getTableReference("tablaTriage5").setWidths(widths5);
             }
        catch (BadElementException e)
                        {
                triagePdfLogger.error("Se presentó error generando el pdf " +e);
                        }

         //adicionar la seccion al reporte
        report.addSectionToDocument("seccionTriage5");
		// *****************************************************

        // SEXTA SECCION
        String[] cabecerasTriage6={
        		
				"Responsable Triage:"
				};
        
        String[] datosTriage6={
        		
				triageForm.getUsuario()
				};
        report.createSection("seccionTriage6","tablaTriage6",1,2,0);
        report.getSectionReference("seccionTriage6").setTableBorder("tablaTriage6", 0xFFFFFF, 0.0f);
        report.getSectionReference("seccionTriage6").setTableCellBorderWidth("tablaTriage6", 0.5f);
        report.getSectionReference("seccionTriage6").setTableCellsDefaultColors("tablaTriage6", 0xFFFFFF, 0xFFFFFF);
        report.getSectionReference("seccionTriage6").setTableCellsColSpan(1);
        for(int k=0; k<cabecerasTriage6.length; k++)
        {
        	report.font.setFontSizeAndAttributes(10,true,false,false);
        	report.getSectionReference("seccionTriage6").addTableTextCell("tablaTriage6", cabecerasTriage6[k], report.font);
        	report.font.setFontSizeAndAttributes(10,false,false,false);
        	report.getSectionReference("seccionTriage6").addTableTextCell("tablaTriage6", datosTriage6[k], report.font);

        }
        
        report.getSectionReference("seccionTriage6").setTableCellsColSpan(2);
        report.getSectionReference("seccionTriage6").addTableTextCell("tablaTriage6", institucionBasica.getPieHistoriaClinica(), report.font);
        report.getSectionReference("seccionTriage6").setTableCellsColSpan(1);
        
        float widths6[]={(float)2.5,(float) 8};//segun numeor de columnas
        try {
           report.getSectionReference("seccionTriage6").getTableReference("tablaTriage6").setWidths(widths6);
             }
        catch (BadElementException e)
                        {
                triagePdfLogger.error("Se presentó error generando el pdf " +e);
                        }

         //adicionar la seccion al reporte
        report.addSectionToDocument("seccionTriage6");
		// *****************************************************
		
        report.setReportDataSectionAtributes(0xFFFFFF, 0xFFFFFF,0x000000,8);
        //definicion de fuente
        report.font.useFont(iTextBaseFont.FONT_TIMES, false,false, false);
        //definicion de atributos para el titulo de laseccion(tabla)
        report.setReportTitleSectionAtributes(0x000000,0xFFFFFF,0x000000,8);
        //definicion de atributos para los datos de la seccion
        report.setReportDataSectionAtributes(0x000000, 0xFFFFFF,0x000000,8);
        
        
		report.closeReport(); 
	}
	/**
	 * Función usada para generar el reporte de triage
	 * @param filename
	 * @param triageForm
	 * @param medico
	 * @param datos
	 * @param categorias 
	 */
	public  static void pdfTriage(String filename, TriageForm triageForm, UsuarioBasico medico,Collection datos, HashMap categorias, HttpServletRequest request) 
	{
		PdfReports report = new PdfReports("true",true,medico.getLoginUsuario());
		
		//GENERACION DE CABECERA
		//Ruta por defecto donde se genera los PDF
		String filePath=ValoresPorDefecto.getFilePath();
		String tituloDocumentos="";
		if(!triageForm.getFechaInicial().equals(""))
			tituloDocumentos="INFORME TRIAGE DESDE "+triageForm.getFechaInicial()+" A "+triageForm.getFechaFinal();
		else
			tituloDocumentos="INFORME TRIAGE";
		
		InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), tituloDocumentos);
		
		//***VALIDAR SI USUARIO Y RESPONSABLE EXISTEN************************************
		String usuario;
		if((triageForm.getLogin().split("-"))[0].equals("0")||(triageForm.getLogin().split("-"))[0].equals("Todos")){
			usuario="Todos";
		}
		else{
			usuario=(triageForm.getLogin().split("-"))[1];
		}
		String responsable;
		if((triageForm.getConvenio().split("-"))[1].equals(" ")){
			responsable="Todos";
		}
		else if((triageForm.getConvenio().split("-"))[1].equals("Otra")){
			responsable="Otros";
		}
		else{
			responsable=(triageForm.getConvenio().split("-"))[1];
		}
		//		abrir reporte
        report.openReport(filename);
		//**** *************************************************************
		 //PRIMERA SECCION
        String[] cabeceras = {
        		"Centro Atención:",
    			"Usuario:", 
    			"Responsable:"
    		};
    		 String[] datosReporte=
             {
    		  triageForm.getNombreCentroAtencion(),
    		   usuario,
			   responsable
             };
    		      
        report.createSection("seccionTriage","tablaTriage",2,1,0);
        report.getSectionReference("seccionTriage").setTableBorder("tablaTriage", 0xFFFFFF, 0.0f);
        report.getSectionReference("seccionTriage").setTableCellBorderWidth("tablaTriage", 0.5f);
        report.getSectionReference("seccionTriage").setTableCellsDefaultColors("tablaTriage", 0xFFFFFF, 0xFFFFFF);
        report.getSectionReference("seccionTriage").setTableCellsColSpan(1);
        for(int k=0; k<cabeceras.length; k++)
        {
        		report.font.setFontSizeAndAttributes(10,false,false,false);
	        	report.getSectionReference("seccionTriage").addTableTextCell("tablaTriage", cabeceras[k]+" "+datosReporte[k], report.font);
	        

        }
        
        report.addSectionToDocument("seccionTriage");
//      *****************************************************
        //Información de las categorías
        int numCategorias = Integer.parseInt(categorias.get("numRegistros").toString());
        
        
        // ANÁLISIS DEL CONTENIDO DEL REPORTE
        Iterator iterador=datos.iterator();
        String fecha_analizada="";
        String fecha_anterior="";
		String fecha_nacimiento="";
		String categoria_triage="";
		int edad=0;
		int bandera=0; //bandera que señala impresión de tupla
		int contador=0; //cuenta el número de tuplas del reporte
		int cuenta=0; //cuenta el número de elementos de la conexion
		//variables usadas para las estadisticas dle triage
		int[] adulto=new int[numCategorias];
		int[] pediatrico=new int[numCategorias];
		int[] sin_edad=new int[numCategorias];
		//variables usadas para calcular el total de triage
		int[] totales=new int[numCategorias*2];
		int[] subtotales=new int[datos.size()];
		// inicializacion
		for(int i=0;i<numCategorias;i++){
			adulto[i]=0;
			pediatrico[i]=0;
			sin_edad[i]=0;
		}
		for(int i=0;i<(numCategorias*2);i++){
			totales[i]=0;
		}
		for(int i=0;i<datos.size();i++){
			subtotales[i]=0;
		}
		
		//________________________________
		//CREACIÓN DEL ENCABEZADO DE LA TABLA
		
		report.createSection("seccionReporte","tablaReporte",3,(2+(numCategorias*2)),0);
		report.getSectionReference("seccionReporte").setTableCellsDefaultHAlignment("tablaReporte",1);
		
        report.getSectionReference("seccionReporte").setTableCellsRowSpan(2);
        report.font.setFontSizeAndAttributes(10,true,false,false);
	    report.getSectionReference("seccionReporte").addTableTextCell("tablaReporte","FECHA", report.font);
	    
	    report.getSectionReference("seccionReporte").setTableCellsColSpan(numCategorias);
	    report.getSectionReference("seccionReporte").setTableCellsRowSpan(1); 
        report.font.setFontSizeAndAttributes(10,true,false,false);
	    report.getSectionReference("seccionReporte").addTableTextCell("tablaReporte","PEDIATRICO", report.font);
	    
	    report.getSectionReference("seccionReporte").setTableCellsColSpan(numCategorias);
        report.font.setFontSizeAndAttributes(10,true,false,false);
	    report.getSectionReference("seccionReporte").addTableTextCell("tablaReporte","ADULTO", report.font);
	    
	    report.getSectionReference("seccionReporte").setTableCellsColSpan(1);
	    report.getSectionReference("seccionReporte").setTableCellsRowSpan(2);
        report.font.setFontSizeAndAttributes(10,true,false,false);
	    report.getSectionReference("seccionReporte").addTableTextCell("tablaReporte","TOTAL DIA", report.font);
	    
	    report.getSectionReference("seccionReporte").setTableCellsRowSpan(1);
	    for(int i=0;i<numCategorias;i++)
	    {
	    	report.getSectionReference("seccionReporte").setTableCellsColSpan(1);
	    	report.getSectionReference("seccionReporte").setTableCellsBackgroundColor(getColor(categorias.get("nombrecolor_"+i).toString()));
	    	if(categorias.get("nombrecolor_"+i).toString().equalsIgnoreCase("black"))
	    		report.font.setFontColorAndAttributes(getColor(""),false,false,false);
	    	else
	    		report.font.setFontColorAndAttributes(getColor("black"),false,false,false);
	        report.font.setFontSizeAndAttributes(10,true,false,false);
		    report.getSectionReference("seccionReporte").addTableTextCell("tablaReporte",categorias.get("codigo_"+i).toString(), report.font);
	    }
	    
	    report.getSectionReference("seccionReporte").setTableCellsBackgroundColor(getColor(""));
	    report.font.setFontColorAndAttributes(getColor("black"),false,false,false);
	    
	    for(int i=0;i<numCategorias;i++)
	    {
	    	report.getSectionReference("seccionReporte").setTableCellsColSpan(1);
	    	report.getSectionReference("seccionReporte").setTableCellsBackgroundColor(getColor(categorias.get("nombrecolor_"+i).toString()));
	    	if(categorias.get("nombrecolor_"+i).toString().equalsIgnoreCase("black"))
	    		report.font.setFontColorAndAttributes(getColor(""),false,false,false);
	    	else
	    		report.font.setFontColorAndAttributes(getColor("black"),false,false,false);
	        report.font.setFontSizeAndAttributes(10,true,false,false);
		    report.getSectionReference("seccionReporte").addTableTextCell("tablaReporte",categorias.get("codigo_"+i).toString(), report.font);
	    }
	    
	    report.getSectionReference("seccionReporte").setTableCellsBackgroundColor(getColor(""));
	    report.font.setFontColorAndAttributes(getColor("black"),false,false,false);
	    
	    
	    report.font.setFontSizeAndAttributes(9,false,false,false);
	    //________________________________________________________________________
	    //GENERACIÓN DEL REPORTE
		while(iterador.hasNext())
		{
			HashMap datosBD=(HashMap)iterador.next();
			fecha_analizada=datosBD.get("fecha")+"";
			fecha_nacimiento=datosBD.get("fecha_nacimiento")+"";
			
			categoria_triage=datosBD.get("numero_triage")+"";
			// se compara que la fecha anterior y la actual de la conexión, no sean iguales
			if(!fecha_anterior.equals("")&&fecha_analizada.compareTo(fecha_anterior)!=0){
				bandera=1;
				
			}
			
			
			if(bandera==0){
						if(!fecha_nacimiento.equals("null")){
							edad=UtilidadFecha.calcularEdad(fecha_nacimiento.substring(8,10),
									fecha_nacimiento.substring(5,7),
									fecha_nacimiento.substring(0,4),
									UtilidadFecha.getFechaActual().substring(0,2),
									UtilidadFecha.getFechaActual().substring(3,5),
									UtilidadFecha.getFechaActual().substring(6,10));
							if(edad>14)
							{
								for(int i=0;i<numCategorias;i++)
								{
									if(categoria_triage.equals(categorias.get("consecutivo_"+i).toString()))
										adulto[i]++;
								}
								
							}
							else
							{
								for(int i=0;i<numCategorias;i++)
								{
									if(categoria_triage.equals(categorias.get("consecutivo_"+i).toString()))
										pediatrico[i]++;
								}
								
							}
						}
						
			} //fin if bandera
			else
			{
				//Inserción de una tupla
				 report.getSectionReference("seccionReporte").addTableTextCell("tablaReporte",UtilidadFecha.conversionFormatoFechaAAp(fecha_anterior), report.font);
				 for(int i=0;i<numCategorias;i++)
					 report.getSectionReference("seccionReporte").addTableTextCell("tablaReporte",pediatrico[i]+"", report.font);
				 for(int i=0;i<numCategorias;i++)
					 report.getSectionReference("seccionReporte").addTableTextCell("tablaReporte",adulto[i]+"", report.font);
				 
				 
				 for(int i=0;i<numCategorias;i++){
				 	subtotales[contador]+=pediatrico[i]+adulto[i];
				 	totales[i]+=pediatrico[i];
				 	totales[i+numCategorias]+=adulto[i];
				 }
				 report.getSectionReference("seccionReporte").addTableTextCell("tablaReporte",subtotales[contador]+"", report.font);
				 contador++;
				 //SE reinician variables
				 // inicializacion
					for(int i=0;i<numCategorias;i++){
						adulto[i]=0;
						pediatrico[i]=0;
					
					}
				 //se vuelve y se calcula todo
							 if(!fecha_nacimiento.equals("null")){
							 	
								edad=UtilidadFecha.calcularEdad(fecha_nacimiento.substring(8,10),
										fecha_nacimiento.substring(5,7),
										fecha_nacimiento.substring(0,4),
										UtilidadFecha.getFechaActual().substring(0,2),
										UtilidadFecha.getFechaActual().substring(3,5),
										UtilidadFecha.getFechaActual().substring(6,10));
								if(edad>14)
								{
									for(int i=0;i<numCategorias;i++)
									{
										if(categoria_triage.equals(categorias.get("consecutivo_"+i).toString()))
											adulto[i]++;
									}
								}
								else
								{
									for(int i=0;i<numCategorias;i++)
									{
										if(categoria_triage.equals(categorias.get("consecutivo_"+i).toString()))
											pediatrico[i]++;
									}
								}
							}//fin if fecha_nacimiento
							bandera=0;
							
			}
			fecha_anterior=fecha_analizada;
			//Impresión del último análisis
			if(cuenta==datos.size()-1){
				//Inserción de una tupla
				 report.getSectionReference("seccionReporte").addTableTextCell("tablaReporte",UtilidadFecha.conversionFormatoFechaAAp(fecha_anterior), report.font);
				 for(int i=0;i<numCategorias;i++)
					 report.getSectionReference("seccionReporte").addTableTextCell("tablaReporte",pediatrico[i]+"", report.font);
				 for(int i=0;i<numCategorias;i++)
					 report.getSectionReference("seccionReporte").addTableTextCell("tablaReporte",adulto[i]+"", report.font);
				 
				
				 for(int i=0;i<numCategorias;i++)
				 {
				 	subtotales[contador]+=pediatrico[i]+adulto[i];
				 	totales[i]+=pediatrico[i];
				 	totales[i+numCategorias]+=adulto[i];
				 }
				 report.getSectionReference("seccionReporte").addTableTextCell("tablaReporte",subtotales[contador]+"", report.font);
				 contador++;
				 //SE reinician variables
				 // inicializacion
					for(int i=0;i<numCategorias;i++){
						adulto[i]=0;
						pediatrico[i]=0;
					
					}
				
			}
			cuenta++;
		}
		 float widths[]={(float)1.2,(float) 0.8,(float) 0.8,(float) 0.8,(float) 0.8,(float) 0.8,(float) 0.8,(float) 0.8,(float) 0.8,(float) 0.8,(float) 0.8,(float) 0.8};
	        try {
	           report.getSectionReference("seccionReporte").getTableReference("tablaReporte").setWidths(widths);
	             }
	        catch (BadElementException e)
	                        {
	                triagePdfLogger.error("Se presentó error generando el pdf " +e);
	                        }
	    report.addSectionToDocument("seccionReporte");
	    //ELABORACIÓN DEL FINAL DEL REPORTE
	    int sumatoria=0;
	    float calculo=0;
	    String cad="";
	    report.createSection("seccionFinalReporte","tablaFinalReporte",2,(2+(numCategorias*2)),0);
		report.getSectionReference("seccionFinalReporte").setTableCellsDefaultHAlignment("tablaFinalReporte",1);
		
		report.font.setFontSizeAndAttributes(10,true,false,false);
	    report.getSectionReference("seccionFinalReporte").addTableTextCell("tablaFinalReporte","TOTAL TRIAGE", report.font);
	    report.font.setFontSizeAndAttributes(9,false,false,false);
        for(int i=0;i<(numCategorias*2);i++){
        	report.getSectionReference("seccionFinalReporte").addTableTextCell("tablaFinalReporte",totales[i]+"", report.font);
        	sumatoria+=totales[i];
        }
        //adición del total de triage
        report.font.setFontSizeAndAttributes(9,true,false,false);
        report.getSectionReference("seccionFinalReporte").addTableTextCell("tablaFinalReporte",sumatoria+"", report.font);
        //titulo de los porcentajes
        report.font.setFontSizeAndAttributes(7,true,false,false);
        report.getSectionReference("seccionFinalReporte").addTableTextCell("tablaFinalReporte","PORCENTAJE", report.font);
        report.font.setFontSizeAndAttributes(9,false,false,false);
        for(int i=0;i<(numCategorias*2);i++){
        	calculo=(float)totales[i]*100/sumatoria;
        	cad=calculo+"";
        	if(cad.length()>=5)
        		report.getSectionReference("seccionFinalReporte").addTableTextCell("tablaFinalReporte",cad.substring(0,5), report.font);
        	else
        		report.getSectionReference("seccionFinalReporte").addTableTextCell("tablaFinalReporte",cad, report.font);
        }
        report.getSectionReference("seccionFinalReporte").addTableTextCell("tablaFinalReporte","100", report.font);
        try {
           report.getSectionReference("seccionFinalReporte").getTableReference("tablaFinalReporte").setWidths(widths);
             }
        catch (BadElementException e)
                        {
                triagePdfLogger.error("Se presentó error generando el pdf " +e);
                        }
        report.addSectionToDocument("seccionFinalReporte");
       //________________________________________________________________________________
        //SECCION TOTAL PEDRIÁTICO Y TOTAL ADULTOS
        int suma_pedriatico=0;
        int suma_adulto=0;
        
        report.createSection("seccionTotalTriage","tablaTotalTriage",2,1,0);
        report.getSectionReference("seccionTotalTriage").setTableBorder("tablaTotalTriage", 0xFFFFFF, 0.0f);
        report.getSectionReference("seccionTotalTriage").setTableCellBorderWidth("tablaTotalTriage", 0.5f);
        report.getSectionReference("seccionTotalTriage").setTableCellsDefaultColors("tablaTotalTriage", 0xFFFFFF, 0xFFFFFF);
        for(int i=0;i<numCategorias;i++){
        	suma_pedriatico+=totales[i];
        	suma_adulto+=totales[i+numCategorias];
        }
        report.font.setFontSizeAndAttributes(9,true,false,false);
	    report.getSectionReference("seccionTotalTriage").addTableTextCell("tablaTotalTriage","TOTAL PEDIÁTRICO: "+suma_pedriatico, report.font);
	    report.getSectionReference("seccionTotalTriage").addTableTextCell("tablaTotalTriage","TOTAL ADULTO: "+suma_adulto, report.font);
	      
        report.addSectionToDocument("seccionTotalTriage");
        
        //SECCIÓN DE LOS QUE NO TIENEN IDENTIFICACIÓN******************************************************************************
        report.createSection("seccionSinEdadReporte","tablaSinEdadReporte",2,(2+numCategorias),0);
		report.getSectionReference("seccionSinEdadReporte").setTableCellsDefaultHAlignment("tablaSinEdadReporte",1);
		
		report.getSectionReference("seccionSinEdadReporte").setTableCellsColSpan(1);
        report.font.setFontSizeAndAttributes(10,true,false,false);
	    report.getSectionReference("seccionSinEdadReporte").addTableTextCell("tablaSinEdadReporte","TRIAGE SIN EDAD", report.font);
	    
	    report.getSectionReference("seccionSinEdadReporte").setTableCellsRowSpan(1);
	    
	    for(int i=0;i<numCategorias;i++)
	    {
	    	report.getSectionReference("seccionSinEdadReporte").setTableCellsColSpan(1);
	    	report.getSectionReference("seccionSinEdadReporte").setTableCellsBackgroundColor(getColor(categorias.get("nombrecolor_"+i).toString()));
	    	if(categorias.get("nombrecolor_"+i).toString().equalsIgnoreCase("black"))
	    		report.font.setFontColorAndAttributes(getColor(""),false,false,false);
	    	else
	    		report.font.setFontColorAndAttributes(getColor("black"),false,false,false);
	        report.font.setFontSizeAndAttributes(10,true,false,false);
		    report.getSectionReference("seccionSinEdadReporte").addTableTextCell("tablaSinEdadReporte",categorias.get("codigo_"+i).toString(), report.font);
	    }
	    report.getSectionReference("seccionSinEdadReporte").setTableCellsBackgroundColor(getColor(""));
	    report.font.setFontColorAndAttributes(getColor("black"),false,false,false);
	    
	    report.getSectionReference("seccionSinEdadReporte").setTableCellsColSpan(1);
        report.font.setFontSizeAndAttributes(10,true,false,false);
	    report.getSectionReference("seccionSinEdadReporte").addTableTextCell("tablaSinEdadReporte","TOTAL", report.font);
	    
	    
	    
	    report.font.setFontSizeAndAttributes(9,false,false,false);
		// inicializacion
		for(int i=0;i<numCategorias;i++){
			
			sin_edad[i]=0;
		}
		for(int i=0;i<(numCategorias*2);i++){
			totales[i]=0;
		}
		for(int i=0;i<datos.size();i++){
			subtotales[i]=0;
		}
		iterador=datos.iterator();
		bandera=0;
		contador=0; //cuenta el número de tuplas del reporte
		cuenta=0; //cuenta el número de elementos de la conexion
		fecha_anterior="";
		//---------------------------------------
	    while(iterador.hasNext())
		{
	    	HashMap datosBD=(HashMap)iterador.next();
			fecha_analizada=datosBD.get("fecha")+"";
			fecha_nacimiento=datosBD.get("fecha_nacimiento")+"";
			
			categoria_triage=datosBD.get("numero_triage")+"";
			// se compara que la fecha anterior y la actual de la conexión, no sean iguales
			if(!fecha_anterior.equals("")&&fecha_analizada.compareTo(fecha_anterior)!=0){
				bandera=1;
				
			}
			
			
			if(bandera==0){
						if(fecha_nacimiento.equals("null"))
						{
							
							for(int i=0;i<numCategorias;i++)
							{
								if(categoria_triage.equals(categorias.get("consecutivo_"+i).toString()))
									sin_edad[i]++;
							}
							
							
						}
						
						
			} //fin if bandera
			else{
				//Inserción de una tupla
				 report.getSectionReference("seccionSinEdadReporte").addTableTextCell("tablaSinEdadReporte",UtilidadFecha.conversionFormatoFechaAAp(fecha_anterior), report.font);
				 for(int i=0;i<numCategorias;i++)
					 report.getSectionReference("seccionSinEdadReporte").addTableTextCell("tablaSinEdadReporte",sin_edad[i]+"", report.font);
				 
				 
				 for(int i=0;i<numCategorias;i++){
				 	subtotales[contador]+=sin_edad[i]+adulto[i];
				 	totales[i]+=sin_edad[i];
				 	
				 }
				 report.getSectionReference("seccionSinEdadReporte").addTableTextCell("tablaSinEdadReporte",subtotales[contador]+"", report.font);
				 contador++;
				 //SE reinician variables
				 // inicializacion
					for(int i=0;i<numCategorias;i++){
						sin_edad[i]=0;
					
					}
				 //se vuelve y se calcula todo
							 if(fecha_nacimiento.equals("null"))
							 {
							 	
								
								 for(int i=0;i<numCategorias;i++)
									{
										if(categoria_triage.equals(categorias.get("consecutivo_"+i).toString()))
											sin_edad[i]++;
									}
								}
							}//fin if fecha_nacimiento
							bandera=0;
							fecha_anterior=fecha_analizada;
							//Impresión del último análisis
							if(cuenta==datos.size()-1){
								//Inserción de una tupla
								 report.getSectionReference("seccionSinEdadReporte").addTableTextCell("tablaSinEdadReporte",UtilidadFecha.conversionFormatoFechaAAp(fecha_anterior), report.font);
								 for(int i=0;i<numCategorias;i++)
									 report.getSectionReference("seccionSinEdadReporte").addTableTextCell("tablaSinEdadReporte",sin_edad[i]+"", report.font);
								 
								 for(int i=0;i<numCategorias;i++){
								 	subtotales[contador]+=sin_edad[i];
								 	totales[i]+=sin_edad[i];
								 	
								 }
								 report.getSectionReference("seccionSinEdadReporte").addTableTextCell("tablaSinEdadReporte",subtotales[contador]+"", report.font);
								 contador++;
								 //SE reinician variables
								 // inicializacion
									for(int i=0;i<numCategorias;i++){
										sin_edad[i]=0;
										
									
									}
								
							}
							cuenta++;			
			}
	    float widths2[]={(float)2,(float) 1.4,(float) 1.4,(float) 1.4,(float) 1.4,(float) 1.4,(float) 1};
        try {
           report.getSectionReference("seccionSinEdadReporte").getTableReference("tablaSinEdadReporte").setWidths(widths2);
             }
        catch (BadElementException e)
                        {
                triagePdfLogger.error("Se presentó error generando el pdf " +e);
                        }
        report.addSectionToDocument("seccionSinEdadReporte");
        //_______________________________________________________________________________________
        //ELABORACIÓN DEL FINAL DEL REPORTE SIN EDAD
	    int sum_sin_edad=0;
	    calculo=0;
	    cad="";
	    report.createSection("seccionFinalSinEdadReporte","tablaFinalSinEdadReporte",2,(2+numCategorias),0);
		report.getSectionReference("seccionFinalSinEdadReporte").setTableCellsDefaultHAlignment("tablaFinalSinEdadReporte",1);
		
		report.font.setFontSizeAndAttributes(10,true,false,false);
	    report.getSectionReference("seccionFinalSinEdadReporte").addTableTextCell("tablaFinalSinEdadReporte","TOTAL", report.font);
	    report.font.setFontSizeAndAttributes(9,false,false,false);
        for(int i=0;i<numCategorias;i++){
        	report.getSectionReference("seccionFinalSinEdadReporte").addTableTextCell("tablaFinalSinEdadReporte",totales[i]+"", report.font);
        	sumatoria+=totales[i];
        	sum_sin_edad+=totales[i];
        }
        //adición del total de triage
        report.font.setFontSizeAndAttributes(9,true,false,false);
        report.getSectionReference("seccionFinalSinEdadReporte").addTableTextCell("tablaFinalSinEdadReporte",sum_sin_edad+"", report.font);
        //titulo de los porcentajes
        report.font.setFontSizeAndAttributes(8,true,false,false);
        report.getSectionReference("seccionFinalSinEdadReporte").addTableTextCell("tablaFinalSinEdadReporte","PORCENTAJE", report.font);
        report.font.setFontSizeAndAttributes(9,false,false,false);
        for(int i=0;i<numCategorias;i++){
        	calculo=(float)totales[i]*100/sum_sin_edad;
        	cad=calculo+"";
        	if(cad.length()>=5)
        		report.getSectionReference("seccionFinalSinEdadReporte").addTableTextCell("tablaFinalSinEdadReporte",cad.substring(0,5), report.font);
        	else
        		report.getSectionReference("seccionFinalSinEdadReporte").addTableTextCell("tablaFinalSinEdadReporte",cad, report.font);
        }
        report.getSectionReference("seccionFinalSinEdadReporte").addTableTextCell("tablaFinalSinEdadReporte","100", report.font);
        try {
           report.getSectionReference("seccionFinalSinEdadReporte").getTableReference("tablaFinalSinEdadReporte").setWidths(widths);
             }
        catch (BadElementException e)
                        {
                triagePdfLogger.error("Se presentó error generando el pdf " +e);
                        }
        
        
        try {
           report.getSectionReference("seccionFinalSinEdadReporte").getTableReference("tablaFinalSinEdadReporte").setWidths(widths2);
             }
        catch (BadElementException e)
                        {
                triagePdfLogger.error("Se presentó error generando el pdf " +e);
                        }
        report.addSectionToDocument("seccionFinalSinEdadReporte");
        //______________________________________________________________
        //SECCION TRIAGE TOTAL
        report.createSection("seccionSumaTriage","tablaSumaTriage",1,1,0);
        report.getSectionReference("seccionSumaTriage").setTableBorder("tablaSumaTriage", 0xFFFFFF, 0.0f);
        report.getSectionReference("seccionSumaTriage").setTableCellBorderWidth("tablaSumaTriage", 0.5f);
        report.getSectionReference("seccionSumaTriage").setTableCellsDefaultColors("tablaSumaTriage", 0xFFFFFF, 0xFFFFFF);
      
        report.font.setFontSizeAndAttributes(9,true,false,false);
	    report.getSectionReference("seccionSumaTriage").addTableTextCell("tablaSumaTriage","TOTAL TRIAGE: "+sumatoria, report.font);
	   
	      
        report.addSectionToDocument("seccionSumaTriage");
        
        //_________________________________________________________________
        report.closeReport();
        
        
	}
	
	/**
	 * Método implementado para obtener color Triage
	 * @param color
	 * @return
	 */
	private static int getColor(String color) 
	{
		Color valor = new Color(255,255,255);
		if(color.equalsIgnoreCase("black"))
			valor = new Color(0,0,0);
		else if(color.equalsIgnoreCase("red"))
			valor = new Color(255,0,0);
		else if(color.equalsIgnoreCase("green"))
			valor = new Color(0,255,0);
		else if(color.equalsIgnoreCase("yellow"))
			valor = new Color(255,255,0);
		
		return valor.getRGB();
	}
		
	
}
