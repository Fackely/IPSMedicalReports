/*
 * ValoracionPediatricaHospitalariaPdf.java 
 * 
 * Lenguaje		: Java
 * Compilador	: J2SDK 1.4.1_01
 * 
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 */
package com.princetonsa.pdf;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

import util.ConstantesBD;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseTable;

/**
 * Clase para manejar la generación de PDF's para las valoraciones .
 *
 * @version 1.0
 * @author <a href="mailto:cperalta@princetonsa.com">Carlos Peralta</a>
 */
public class ValoracionGeneralConsultaExternaPdf 
{
	
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private static Logger logger = Logger.getLogger(ValoracionGeneralConsultaExternaPdf.class);
	
	/**
	 * @param filename nombre del archivo pdf a generar.
	 * @param datos mapa de datos cuyas llaves son las siguientes:
	 */
	public static void pdfValoracionGeneralInterconsulta(String nombreArchivo, HashMap datos,  UsuarioBasico usu, int extension, boolean pacienteEmbarazada, PersonaBasica paciente)
	{	
     
		 //variable para manejar el reporte
        PdfReports report = new PdfReports();
        
        //titulo del reporte
        String tituloReporte="";
        if(extension==ConstantesBD.codigoExtensionValoracionGineco)
        {
        	tituloReporte="VALORACIÓN GINECO-OBSTÉTRICA ("+ (String)datos.get("unidadConsulta")+")" ;
        }
        else
        {
        	tituloReporte="VALORACIÓN "+ (String)datos.get("unidadConsulta") ;
        }
        
        InstitucionBasica institucionBasica= new InstitucionBasica();
        institucionBasica.cargarXConvenio(usu.getCodigoInstitucionInt(), paciente.getCodigoConvenio());
        
        report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), tituloReporte);
        
        //abrir reporte
        report.openReport(nombreArchivo);
        
        //Informacion del Paciente
        report.font.setFontSizeAndAttributes(11,true,false,false);
        report.document.addParagraph("Información Personal ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);
        report.font.setFontSizeAndAttributes(11,false,false,false);
        report.document.addParagraph("Apellidos y Nombres: "+(String)datos.get("apellidosNombres"),report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
        report.document.addParagraph(""+(String)datos.get("tipoIdentificacion")+": "+(String)datos.get("numeroIdentificacion")+" De: "+(String)datos.get("ciudadIdentificacion")+"     "+"Edad: "+(String)datos.get("edad")+"     "+"Sexo: "+(String)datos.get("sexo"),report.font,iTextBaseDocument.ALIGNMENT_LEFT,12);
        report.document.addParagraph("Responsable: "+(String)datos.get("empresaResponsable"),report.font,iTextBaseDocument.ALIGNMENT_LEFT,12);
        report.document.addParagraph("Área: "+(String)datos.get("area"),report.font,iTextBaseDocument.ALIGNMENT_LEFT,12);
      	
        //Informacion de la Consulta
        report.font.setFontSizeAndAttributes(11,true,false,false);
        report.createSection("seccionConsulta","tablaConsulta",3,3,0);
        report.getSectionReference("seccionConsulta").setTableBorder("tablaConsulta", 0xFFFFFF, 0.0f);
        report.getSectionReference("seccionConsulta").setTableCellBorderWidth("tablaConsulta", 0.5f);
        report.getSectionReference("seccionConsulta").setTableCellsDefaultColors("tablaConsulta", 0xFFFFFF, 0xFFFFFF);
        report.getSectionReference("seccionConsulta").setTableCellsColSpan(3);
        report.getSectionReference("seccionConsulta").addTableTextCellAligned("tablaConsulta", "Información de la Consulta ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        report.font.setFontSizeAndAttributes(11,false,false,false);
        report.getSectionReference("seccionConsulta").setTableCellsColSpan(1);
        String hora=(String)datos.get("horaValoracion");
        report.getSectionReference("seccionConsulta").addTableTextCellAligned("tablaConsulta", "Fecha Cita: "+(String)datos.get("fechaAdmision")+"     "+(String)datos.get("horaAdmision"), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        report.getSectionReference("seccionConsulta").addTableTextCellAligned("tablaConsulta", "Fecha Atención: "+(String)datos.get("fechaValoracion")+"     "+hora.substring(0,5), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        report.getSectionReference("seccionConsulta").addTableTextCellAligned("tablaConsulta", "Tipo de Cita: "+(String)datos.get("tipoConsulta"), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        report.getSectionReference("seccionConsulta").setTableCellsColSpan(3);
        report.getSectionReference("seccionConsulta").addTableTextCellAligned("tablaConsulta", "Motivo Consulta y Enfermedad Actual: "+(String)datos.get("motivoConsulta"), report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,iTextBaseDocument.ALIGNMENT_LEFT);
        //adicionar la seccion al reporte
        report.addSectionToDocument("seccionConsulta");
        
        
        // Informacion embarazo (Gineco obstetrico)
        if(extension==ConstantesBD.codigoExtensionValoracionGineco && pacienteEmbarazada)
        {
        	report.createSection("seccionEmbarazada","tablaConsulta",2,3,0);
        	report.getSectionReference("seccionEmbarazada").setTableBorder("tablaConsulta", 0xFFFFFF, 0.0f);
            report.getSectionReference("seccionEmbarazada").setTableCellBorderWidth("tablaConsulta", 0.5f);
            report.getSectionReference("seccionEmbarazada").setTableCellsDefaultColors("tablaConsulta", 0xFFFFFF, 0xFFFFFF);
            report.getSectionReference("seccionEmbarazada").setTableCellsColSpan(3);
            report.getSectionReference("seccionEmbarazada").addTableTextCellAligned("tablaConsulta", "Embarazada", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            report.getSectionReference("seccionEmbarazada").setTableCellsColSpan(1);
            String fur=(String)datos.get("fur");
            if(fur!=null && !fur.equals(""))
            {
            	report.getSectionReference("seccionEmbarazada").addTableTextCellAligned("tablaConsulta", "FUR: "+fur, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            }
            String fpp=(String)datos.get("fpp");
            if(fpp!=null && !fpp.equals(""))
            {
            	report.getSectionReference("seccionEmbarazada").addTableTextCellAligned("tablaConsulta", "FPP: "+fpp, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            }
            String edadGestacional=(String)datos.get("edadGestacional");
            if(edadGestacional!=null && !edadGestacional.equals(""))
            {
            	report.getSectionReference("seccionEmbarazada").addTableTextCellAligned("tablaConsulta", "Edad Gestacional: "+edadGestacional+" Semanas", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            }
            report.addSectionToDocument("seccionEmbarazada");
        }
        
        
        // Informacion Historia Menstrual (Gineco obstetrico)
        if(extension==ConstantesBD.codigoExtensionValoracionGineco)
        {
        	int contadorFilas=0;
            String edadMenarquia=(String)datos.get("edadMenarquia");
            String edadMenopausia=(String)datos.get("edadMenopausia");
            String furAnt=(String)datos.get("furAnt");
            String cicloMenstrual=(String)datos.get("cicloMenstrual");
            String duracion=(String)datos.get("duracion");
            String concepto=(String)datos.get("concepto");
            if(concepto!=null && (concepto.equalsIgnoreCase("true") || concepto.equalsIgnoreCase("t") || concepto.equals("1")))
            {
            	concepto="Verdadero";
            }
            if(concepto!=null && (concepto.equalsIgnoreCase("false") || concepto.equalsIgnoreCase("f") || concepto.equals("0")))
            {
            	concepto="Falso";
            }
            String dolor=(String)datos.get("dolor");
            String observacionesMenstruacion=(String)datos.get("observacionesMenstruacion");
            if(
                	(edadMenarquia!=null && !edadMenarquia.equals("")) ||
                	(edadMenopausia!=null && !edadMenopausia.equals("")) ||
                	(furAnt!=null && !furAnt.equals(""))
    		)
            {
            	contadorFilas++;
            }
            if(
                	(cicloMenstrual!=null && !cicloMenstrual.equals("")) ||
                	(duracion!=null && !duracion.equals("")) ||
                	(concepto!=null && !concepto.equals("") && !concepto.equals("1")) ||
                	(dolor!=null && !dolor.equals(""))
    		)
            {
            	contadorFilas++;
            }
            if(observacionesMenstruacion!=null && !observacionesMenstruacion.equals(""))
            {
            	contadorFilas++;
            }
            if(contadorFilas!=0)
            {
	            report.createSection("seccionHistoriaMenstrual","tablaConsulta",contadorFilas,4,0);
	        	report.getSectionReference("seccionHistoriaMenstrual").setTableBorder("tablaConsulta", 0xFFFFFF, 0.0f);
	            report.getSectionReference("seccionHistoriaMenstrual").setTableCellBorderWidth("tablaConsulta", 0.5f);
	            report.getSectionReference("seccionHistoriaMenstrual").setTableCellsDefaultColors("tablaConsulta", 0xFFFFFF, 0xFFFFFF);
	            report.getSectionReference("seccionHistoriaMenstrual").setTableCellsColSpan(1);
	            contadorFilas=0;
	            int contCeldas=0;
	            if(
	            		(edadMenarquia!=null && !edadMenarquia.equals("")) ||
						(edadMenopausia!=null && !edadMenopausia.equals("")) ||
						(furAnt!=null && !furAnt.equals(""))
				)
	            {
		            if(edadMenarquia!=null && !edadMenarquia.equals(""))
		            {
		            	report.getSectionReference("seccionHistoriaMenstrual").addTableTextCellAligned("tablaConsulta", "Edad Menarquia: "+edadMenarquia, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		            	contCeldas++;
		            }
		            if(edadMenopausia!=null && !edadMenopausia.equals(""))
		            {
		            	report.getSectionReference("seccionHistoriaMenstrual").addTableTextCellAligned("tablaConsulta", "Edad Menopausia: "+edadMenopausia, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		            	contCeldas++;
		            }
		            if(furAnt!=null && !furAnt.equals(""))
		            {
		            	report.getSectionReference("seccionHistoriaMenstrual").setTableCellsColSpan(4-contCeldas);
		            	report.getSectionReference("seccionHistoriaMenstrual").addTableTextCellAligned("tablaConsulta", "FUR: "+furAnt, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		            }
		            else
		            {
		            	report.getSectionReference("seccionHistoriaMenstrual").setTableCellsColSpan(4-contCeldas);
		            	report.getSectionReference("seccionHistoriaMenstrual").addTableTextCellAligned("tablaConsulta", "", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		            }
	            }
	            if(
	                	(cicloMenstrual!=null && !cicloMenstrual.equals("")) ||
	                	(duracion!=null && !duracion.equals("")) ||
	                	(concepto!=null && !concepto.equals("") && !concepto.equals("1")) ||
	                	(dolor!=null && !dolor.equals(""))
	    		)
	            {
	            	report.getSectionReference("seccionHistoriaMenstrual").setTableCellsColSpan(1);
	            	contCeldas=0;
		            if(cicloMenstrual!=null && !cicloMenstrual.equals(""))
		            {
		            	report.getSectionReference("seccionHistoriaMenstrual").addTableTextCellAligned("tablaConsulta", "Cilco Menstrual: "+cicloMenstrual, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		            	contCeldas++;
		            }
		            if(duracion!=null && !duracion.equals(""))
		            {
		            	report.getSectionReference("seccionHistoriaMenstrual").addTableTextCellAligned("tablaConsulta", "Duracion: "+duracion, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		            	contCeldas++;
		            }
		            if(concepto!=null && !concepto.equals("") && !concepto.equals("-1"))
		            {
		            	report.getSectionReference("seccionHistoriaMenstrual").addTableTextCellAligned("tablaConsulta", "Concepto: "+concepto, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		            	contCeldas++;
		            }
		            if(dolor!=null && !dolor.equals(""))
		            {
		            	report.getSectionReference("seccionHistoriaMenstrual").setTableCellsColSpan(4-contCeldas);
		            	report.getSectionReference("seccionHistoriaMenstrual").addTableTextCellAligned("tablaConsulta", "Dolor: "+dolor, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		            }
		            else
		            {
		            	report.getSectionReference("seccionHistoriaMenstrual").setTableCellsColSpan(4-contCeldas);
		            	report.getSectionReference("seccionHistoriaMenstrual").addTableTextCellAligned("tablaConsulta", "", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		            }
	            }
	            if(observacionesMenstruacion!=null && !observacionesMenstruacion.equals(""))
	            {
	            	if(observacionesMenstruacion!=null && !observacionesMenstruacion.equals(""))
		            {
		            	report.getSectionReference("seccionHistoriaMenstrual").setTableCellsColSpan(4);
		            	report.getSectionReference("seccionHistoriaMenstrual").addTableTextCellAligned("tablaConsulta", "Observaciones Características Menstruación: "+observacionesMenstruacion, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		            }
	            }
	            report.addSectionToDocument("seccionHistoriaMenstrual");
            }
        }
        
        //Informacion de la Valoracion Medica
        report.font.setFontSizeAndAttributes(11,true,false,false);
        report.document.addParagraph("Valoracion Médica",report.font,iTextBaseDocument.ALIGNMENT_LEFT,10);
        report.font.setFontSizeAndAttributes(11,false,false,false);
        
        
        
        /***************************************************REVISION POR SISTEMAS**************************************************************************************/
        HashMap examenesFisicos=(HashMap)datos.get("examenesFisicos");
        HashMap examenesFisicosOpc=(HashMap)datos.get("examenesFisicosOpc");
        iTextBaseTable section = null;
        
        //************REVISION POR SISTEMAS ***********************************************************************************************
        int numRegistros = Integer.parseInt(examenesFisicos.get("numRegistros").toString());
        int numRegistrosOpc = Integer.parseInt(examenesFisicosOpc.get("numRegistros").toString());
        
        if(numRegistros>0)
        {
        	//Revision por sistemas
	        report.font.setFontSizeAndAttributes(11,true,false,false);
	        report.document.addParagraph("Revisión por Sistemas",report.font,iTextBaseDocument.ALIGNMENT_LEFT,20);
	        report.font.setFontSizeAndAttributes(11,false,false,false);
        	
        	//****************************************************
		    section = report.createSection("seccionRevisionSistemas", "revisionSistemasTable", numRegistros, 6, 0);
	  		report.font.setFontAttributes(0x000000, 10, true, false, false);
	  		section.setTableBorder("revisionSistemasTable", 0xFFFFFF, 0.0f);
		    section.setTableCellBorderWidth("revisionSistemasTable", 0.5f);
		    section.setTableCellPadding("revisionSistemasTable", 1);
		    section.setTableSpaceBetweenCells("revisionSistemasTable", 0.5f);
		    section.setTableCellsDefaultColors("revisionSistemasTable", 0xFFFFFF, 0xFFFFFF);
		    report.font.setFontAttributes(0x000000, 10, false, false, false);
		    section.setTableCellsColSpan(6);
		    
		    for(int i=0;i<numRegistros;i++)
		    {
		    	section.addTableTextCellAligned("revisionSistemasTable", examenesFisicos.get(i+"")+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		    }
		    
		    section.setTableCellsColSpan(1);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableBorderColor("revisionSistemasTable", 0xFFFFFF);
	    	report.addSectionToDocument("seccionRevisionSistemas");
		    //****************************************************
        }
        
        if(numRegistrosOpc>0)
        {
        	if(numRegistros<=0)
        	{
        		//Revision por sistemas
    	        report.font.setFontSizeAndAttributes(11,true,false,false);
    	        report.document.addParagraph("Revisión por Sistemas",report.font,iTextBaseDocument.ALIGNMENT_LEFT,20);
    	        report.font.setFontSizeAndAttributes(11,false,false,false);
            	
        	}
        	
        	//****************************************************
		    section = report.createSection("seccionRevisionSistemasOpc", "revisionSistemasOpcTable", numRegistrosOpc, 6, 0);
	  		report.font.setFontAttributes(0x000000, 11, true, false, false);
	  		section.setTableBorder("revisionSistemasOpcTable", 0xFFFFFF, 0.0f);
		    section.setTableCellBorderWidth("revisionSistemasOpcTable", 0.5f);
		    section.setTableCellPadding("revisionSistemasOpcTable", 1);
		    section.setTableSpaceBetweenCells("revisionSistemasOpcTable", 0.5f);
		    section.setTableCellsDefaultColors("revisionSistemasOpcTable", 0xFFFFFF, 0xFFFFFF);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableCellsColSpan(6);
		    
		    for(int i=0;i<numRegistrosOpc;i++)
		    {
		    	section.addTableTextCellAligned("revisionSistemasOpcTable", examenesFisicosOpc.get(i+"")+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		    }
		    
		    section.setTableCellsColSpan(1);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableBorderColor("revisionSistemasOpcTable", 0xFFFFFF);
	    	report.addSectionToDocument("seccionRevisionSistemasOpc");
		    //****************************************************
        }
        
        //********CAMPOS PARAMETRIZABLES REVISION POR SISTEMAS***************************************************************************
        HashMap camposParam = (HashMap)datos.get("camposParamRevisionSistemas");
        String[] sectionData = new String[0];
	    numRegistros = 0;
	    
	    if(camposParam!=null&&camposParam.get("numRegistros")!=null)
	    	numRegistros = Integer.parseInt(camposParam.get("numRegistros").toString());
	    
	    if(numRegistros>0)
	    {
	    	
	    	
		    //****************************************************
	    	section = report.createSection("seccionCamposParamRV", "camposParamRVTable", numRegistros, 6, 0);
	  		report.font.setFontAttributes(0x000000, 10, true, false, false);
	  		section.setTableBorder("camposParamRVTable", 0xFFFFFF, 0.0f);
		    section.setTableCellBorderWidth("camposParamRVTable", 0.5f);
		    section.setTableCellPadding("camposParamRVTable", 1);
		    section.setTableSpaceBetweenCells("camposParamRVTable", 0.5f);
		    section.setTableCellsDefaultColors("camposParamRVTable", 0xFFFFFF, 0xFFFFFF);
	  		//report.addSectionTitle("antecedentesPediatricos", "revisionSistemasTable", "Revisión por Sistemas");
		    report.font.setFontAttributes(0x000000, 10, false, false, false);
		    section.setTableCellsColSpan(3);
		    
		    int contador = 0;
		    sectionData = new String[numRegistros*2];
		    for(int i=0;i<numRegistros;i++)
		    {
		    	sectionData[contador] = camposParam.get("nombre_"+i).toString();
		    	contador++;
		    	String temp = camposParam.get("info_"+i).toString();
		    	switch(Integer.parseInt(camposParam.get("tipo_"+i).toString()))
		    	{
		    		case ConstantesBD.tipoParametrizablesTexto:
		    		case ConstantesBD.tipoParametrizablesTextArea:
		    		break;
		    		case ConstantesBD.tipoParametrizablesArchivo:
		    			temp = "";
		    		break;
		    		case ConstantesBD.tipoParametrizablesSelect:
		    			temp = UtilidadTexto.getBoolean(temp)?"Sí":"No";
		    		break;
		    	}
		    	sectionData[contador] = temp;
		    	contador++;
		    }
		    
		    report.addSectionData("seccionCamposParamRV", "camposParamRVTable", sectionData);
		    section.setTableCellsColSpan(1);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableBorderColor("camposParamRVTable", 0xFFFFFF);
	    	
		    report.addSectionToDocument("seccionCamposParamRV");
		    //****************************************************
	    }
        
        /***********************************EXÁMENES FÍSICOS********************************************************************************/
        //En esta parte realizao validaciones para estar seguro de solamente
        //adicionar a la impresion los campos que realmente contengan informacion
        HashMap signosVitales=(HashMap)datos.get("signosVitales");
        numRegistros = Integer.parseInt(signosVitales.get("numRegistros").toString());
        if(numRegistros>0)
        {
        	//Exámenes Físicos
	        report.font.setFontSizeAndAttributes(11,true,false,false);
	        report.document.addParagraph("Exámenes Físicos",report.font,iTextBaseDocument.ALIGNMENT_LEFT,10);
	        report.font.setFontSizeAndAttributes(11,false,false,false);
        	
        	
        	//****************************************************
		    section = report.createSection("seccionExamenesFisicos", "examenesFisicosTable", (numRegistros/3+numRegistros%3), 6, 0);
	  		report.font.setFontAttributes(0x000000, 11, true, false, false);
	  		section.setTableBorder("examenesFisicosTable", 0xFFFFFF, 0.0f);
		    section.setTableCellBorderWidth("examenesFisicosTable", 0.5f);
		    section.setTableCellPadding("examenesFisicosTable", 1);
		    section.setTableSpaceBetweenCells("examenesFisicosTable", 0.5f);
		    section.setTableCellsDefaultColors("examenesFisicosTable", 0xFFFFFF, 0xFFFFFF);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableCellsColSpan(2);
		    
		    for(int i=0;i<numRegistros;i++)
		    {
		    	section.addTableTextCellAligned("examenesFisicosTable", signosVitales.get(i+"")+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		    }
		    
		    section.setTableCellsColSpan(1);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableBorderColor("examenesFisicosTable", 0xFFFFFF);
	    	report.addSectionToDocument("seccionExamenesFisicos");
		    //****************************************************
        }
        
        //********CAMPOS PARAMETRIZABLES EXÁMENES FISICOS***************************************************************************
        camposParam = (HashMap)datos.get("camposParamExamenesFisicos");
        sectionData = new String[0];
	    numRegistros = 0;
	    
	    if(camposParam!=null&&camposParam.get("numRegistros")!=null)
	    	numRegistros = Integer.parseInt(camposParam.get("numRegistros").toString());
	    
	    if(numRegistros>0)
	    {
	    	
	    	
		    //****************************************************
	    	section = report.createSection("seccionCamposParamEF", "camposParamEFTable", numRegistros, 6, 0);
	  		report.font.setFontAttributes(0x000000, 10, true, false, false);
	  		section.setTableBorder("camposParamEFTable", 0xFFFFFF, 0.0f);
		    section.setTableCellBorderWidth("camposParamEFTable", 0.5f);
		    section.setTableCellPadding("camposParamEFTable", 1);
		    section.setTableSpaceBetweenCells("camposParamEFTable", 0.5f);
		    section.setTableCellsDefaultColors("camposParamEFTable", 0xFFFFFF, 0xFFFFFF);
	  		//report.addSectionTitle("antecedentesPediatricos", "revisionSistemasTable", "Revisión por Sistemas");
		    report.font.setFontAttributes(0x000000, 10, false, false, false);
		    section.setTableCellsColSpan(3);
		    
		    int contador = 0;
		    sectionData = new String[numRegistros*2];
		    for(int i=0;i<numRegistros;i++)
		    {
		    	sectionData[contador] = camposParam.get("nombre_"+i).toString();
		    	contador++;
		    	String temp = camposParam.get("info_"+i).toString();
		    	switch(Integer.parseInt(camposParam.get("tipo_"+i).toString()))
		    	{
		    		case ConstantesBD.tipoParametrizablesTexto:
		    		case ConstantesBD.tipoParametrizablesTextArea:
		    		break;
		    		case ConstantesBD.tipoParametrizablesArchivo:
		    			temp = "";
		    		break;
		    		case ConstantesBD.tipoParametrizablesSelect:
		    			temp = UtilidadTexto.getBoolean(temp)?"Sí":"No";
		    		break;
		    	}
		    	sectionData[contador] = temp;
		    	contador++;
		    }
		    
		    report.addSectionData("seccionCamposParamEF", "camposParamEFTable", sectionData);
		    section.setTableCellsColSpan(1);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableBorderColor("camposParamEFTable", 0xFFFFFF);
	    	
		    report.addSectionToDocument("seccionCamposParamEF");
		    //****************************************************
	    }
       
        
        String finalidadConsulta=(String)datos.get("finalidadConsulta");
        if(!finalidadConsulta.equals(""))
        {
        	report.font.setFontSizeAndAttributes(11,false,false,false);
            report.document.addParagraph("Finalidad de la Consulta: "+finalidadConsulta,report.font,iTextBaseDocument.ALIGNMENT_LEFT,10);
            report.font.setFontSizeAndAttributes(11,false,false,false);
        }
        
                
        //********CAMPOS PARAMETRIZABLES FINALIDAD CONSULTA***************************************************************************
        camposParam = (HashMap)datos.get("camposParamFinalidad");
        sectionData = new String[0];
	    numRegistros = 0;
	    
	    if(camposParam!=null&&camposParam.get("numRegistros")!=null)
	    	numRegistros = Integer.parseInt(camposParam.get("numRegistros").toString());
	    
	    if(numRegistros>0)
	    {
	    	
	    	
		    //****************************************************
	    	section = report.createSection("seccionCamposParamF", "camposParamFTable", numRegistros, 6, 0);
	  		report.font.setFontAttributes(0x000000, 10, true, false, false);
	  		section.setTableBorder("camposParamFTable", 0xFFFFFF, 0.0f);
		    section.setTableCellBorderWidth("camposParamFTable", 0.5f);
		    section.setTableCellPadding("camposParamFTable", 1);
		    section.setTableSpaceBetweenCells("camposParamFTable", 0.5f);
		    section.setTableCellsDefaultColors("camposParamFTable", 0xFFFFFF, 0xFFFFFF);
	  		//report.addSectionTitle("antecedentesPediatricos", "revisionSistemasTable", "Revisión por Sistemas");
		    report.font.setFontAttributes(0x000000, 10, false, false, false);
		    section.setTableCellsColSpan(3);
		    
		    int contador = 0;
		    sectionData = new String[numRegistros*2];
		    for(int i=0;i<numRegistros;i++)
		    {
		    	sectionData[contador] = camposParam.get("nombre_"+i).toString();
		    	contador++;
		    	String temp = camposParam.get("info_"+i).toString();
		    	switch(Integer.parseInt(camposParam.get("tipo_"+i).toString()))
		    	{
		    		case ConstantesBD.tipoParametrizablesTexto:
		    		case ConstantesBD.tipoParametrizablesTextArea:
		    		break;
		    		case ConstantesBD.tipoParametrizablesArchivo:
		    			temp = "";
		    		break;
		    		case ConstantesBD.tipoParametrizablesSelect:
		    			temp = UtilidadTexto.getBoolean(temp)?"Sí":"No";
		    		break;
		    	}
		    	sectionData[contador] = temp;
		    	contador++;
		    }
		    
		    report.addSectionData("seccionCamposParamF", "camposParamFTable", sectionData);
		    section.setTableCellsColSpan(1);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableBorderColor("camposParamFTable", 0xFFFFFF);
	    	
		    report.addSectionToDocument("seccionCamposParamF");
		    //****************************************************
	    }
        
        
        String causaExterna=(String)datos.get("causaExterna");
        if(!causaExterna.equals(""))
        {
        	report.font.setFontSizeAndAttributes(11,false,false,false);
            report.document.addParagraph("Causa Externa: "+causaExterna,report.font,iTextBaseDocument.ALIGNMENT_LEFT,10);
            report.font.setFontSizeAndAttributes(11,false,false,false);
        }
        
        //********CAMPOS PARAMETRIZABLES CAUSA EXTERNA***************************************************************************
        camposParam = (HashMap)datos.get("camposParamCausaExterna");
        sectionData = new String[0];
	    numRegistros = 0;
	    
	    if(camposParam!=null&&camposParam.get("numRegistros")!=null)
	    	numRegistros = Integer.parseInt(camposParam.get("numRegistros").toString());
	    
	    if(numRegistros>0)
	    {
	    	
	    	
		    //****************************************************
	    	section = report.createSection("seccionCamposParamCE", "camposParamCETable", numRegistros, 6, 0);
	  		report.font.setFontAttributes(0x000000, 10, true, false, false);
	  		section.setTableBorder("camposParamCETable", 0xFFFFFF, 0.0f);
		    section.setTableCellBorderWidth("camposParamCETable", 0.5f);
		    section.setTableCellPadding("camposParamCETable", 1);
		    section.setTableSpaceBetweenCells("camposParamCETable", 0.5f);
		    section.setTableCellsDefaultColors("camposParamCETable", 0xFFFFFF, 0xFFFFFF);
	  		//report.addSectionTitle("antecedentesPediatricos", "revisionSistemasTable", "Revisión por Sistemas");
		    report.font.setFontAttributes(0x000000, 10, false, false, false);
		    section.setTableCellsColSpan(3);
		    
		    int contador = 0;
		    sectionData = new String[numRegistros*2];
		    for(int i=0;i<numRegistros;i++)
		    {
		    	sectionData[contador] = camposParam.get("nombre_"+i).toString();
		    	contador++;
		    	String temp = camposParam.get("info_"+i).toString();
		    	switch(Integer.parseInt(camposParam.get("tipo_"+i).toString()))
		    	{
		    		case ConstantesBD.tipoParametrizablesTexto:
		    		case ConstantesBD.tipoParametrizablesTextArea:
		    		break;
		    		case ConstantesBD.tipoParametrizablesArchivo:
		    			temp = "";
		    		break;
		    		case ConstantesBD.tipoParametrizablesSelect:
		    			temp = UtilidadTexto.getBoolean(temp)?"Sí":"No";
		    		break;
		    	}
		    	sectionData[contador] = temp;
		    	contador++;
		    }
		    
		    report.addSectionData("seccionCamposParamCE", "camposParamCETable", sectionData);
		    section.setTableCellsColSpan(1);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableBorderColor("camposParamCETable", 0xFFFFFF);
	    	
		    report.addSectionToDocument("seccionCamposParamCE");
		    //****************************************************
	    }
        
        
        
        /***********************************************************************************************************/
        
        
        report.font.setFontSizeAndAttributes(11,false,false,false);
       
        report.createSection("seccionDx","tablaDx",5,3,0);
        report.getSectionReference("seccionDx").setTableBorder("tablaDx", 0xFFFFFF, 0.0f);
        report.getSectionReference("seccionDx").setTableCellBorderWidth("tablaDx", 0.5f);
        report.getSectionReference("seccionDx").setTableCellsDefaultColors("tablaDx", 0xFFFFFF, 0xFFFFFF);
        
        
        String dxPpal=(String)datos.get("diagPrinicpal");
        if(!dxPpal.equals(""))
        {
        	report.font.setFontSizeAndAttributes(11,true,false,false);
        	report.getSectionReference("seccionDx").setTableCellsColSpan(1);
            report.getSectionReference("seccionDx").addTableTextCellAligned("tablaDx", "Dx Principal: ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        	report.font.setFontSizeAndAttributes(11,false,false,false);
            report.getSectionReference("seccionDx").setTableCellsColSpan(2);
            report.getSectionReference("seccionDx").addTableTextCellAligned("tablaDx", ""+dxPpal, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);        	
        }
        
        String tipoDx=(String)datos.get("tipoDx");
        if(!tipoDx.equals(""))
        {
        	report.font.setFontSizeAndAttributes(11,true,false,false);
        	report.getSectionReference("seccionDx").setTableCellsColSpan(1);
            report.getSectionReference("seccionDx").addTableTextCellAligned("tablaDx", "Tipo Dx Principal: ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        	report.font.setFontSizeAndAttributes(11,false,false,false);
            report.getSectionReference("seccionDx").setTableCellsColSpan(2);
            report.getSectionReference("seccionDx").addTableTextCellAligned("tablaDx", ""+tipoDx, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        }
        
        String dxRelacionado=(String)datos.get("diagRelacionado");
        if(!dxRelacionado.equals(""))
        {
        	report.font.setFontSizeAndAttributes(11,true,false,false);
        	report.getSectionReference("seccionDx").setTableCellsColSpan(1);
            report.getSectionReference("seccionDx").addTableTextCellAligned("tablaDx", "Dx Relacionados: ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        	report.font.setFontSizeAndAttributes(11,false,false,false);
            report.getSectionReference("seccionDx").setTableCellsColSpan(2);

            String[] dx=dxRelacionado.split("@@@");
            
            
            int i=1;
            while(i<=(dx.length))
            {
            	
              //report.getSectionReference("seccionDx").addTableTextCellAligned("tablaDx", ""+dxRelacionado, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            	report.getSectionReference("seccionDx").addTableTextCellAligned("tablaDx", ""+dx[i], report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            	report.getSectionReference("seccionDx").setTableCellsColSpan(1);
            	report.getSectionReference("seccionDx").addTableTextCellAligned("tablaDx", "", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            	report.getSectionReference("seccionDx").setTableCellsColSpan(2);
            	i++;
            	if(i==dx.length)
            	{
            		break;
            	}
            }
            
        }
        
        
        //adicionar la seccion al reporte
        report.addSectionToDocument("seccionDx");
        
        /***********************************************************************************************************/
        
        
        String concepto=(String)datos.get("concepto");
        if(concepto!=null && !concepto.equals(""))
        {
        	String [] conceptos=concepto.split("-");
        	report.font.setFontSizeAndAttributes(11,false,false,false);
            report.document.addParagraph("Concepto de la Consulta: "+conceptos[0],report.font,iTextBaseDocument.ALIGNMENT_LEFT,10);
            report.font.setFontSizeAndAttributes(11,false,false,false);        	
        }
		
        //********CAMPOS PARAMETRIZABLES CONCEPTO CONSULTA***************************************************************************
        camposParam = (HashMap)datos.get("camposParamConceptoConsulta");
        sectionData = new String[0];
	    numRegistros = 0;
	    
	    if(camposParam!=null&&camposParam.get("numRegistros")!=null)
	    	numRegistros = Integer.parseInt(camposParam.get("numRegistros").toString());
	    
	    if(numRegistros>0)
	    {
	    	
	    	
		    //****************************************************
	    	section = report.createSection("seccionCamposParamCC", "camposParamCCTable", numRegistros, 6, 0);
	  		report.font.setFontAttributes(0x000000, 10, true, false, false);
	  		section.setTableBorder("camposParamCCTable", 0xFFFFFF, 0.0f);
		    section.setTableCellBorderWidth("camposParamCCTable", 0.5f);
		    section.setTableCellPadding("camposParamCCTable", 1);
		    section.setTableSpaceBetweenCells("camposParamCCTable", 0.5f);
		    section.setTableCellsDefaultColors("camposParamCCTable", 0xFFFFFF, 0xFFFFFF);
	  		//report.addSectionTitle("antecedentesPediatricos", "revisionSistemasTable", "Revisión por Sistemas");
		    report.font.setFontAttributes(0x000000, 10, false, false, false);
		    section.setTableCellsColSpan(3);
		    
		    int contador = 0;
		    sectionData = new String[numRegistros*2];
		    for(int i=0;i<numRegistros;i++)
		    {
		    	sectionData[contador] = camposParam.get("nombre_"+i).toString();
		    	contador++;
		    	String temp = camposParam.get("info_"+i).toString();
		    	switch(Integer.parseInt(camposParam.get("tipo_"+i).toString()))
		    	{
		    		case ConstantesBD.tipoParametrizablesTexto:
		    		case ConstantesBD.tipoParametrizablesTextArea:
		    		break;
		    		case ConstantesBD.tipoParametrizablesArchivo:
		    			temp = "";
		    		break;
		    		case ConstantesBD.tipoParametrizablesSelect:
		    			temp = UtilidadTexto.getBoolean(temp)?"Sí":"No";
		    		break;
		    	}
		    	sectionData[contador] = temp;
		    	contador++;
		    }
		    
		    report.addSectionData("seccionCamposParamCC", "camposParamCCTable", sectionData);
		    section.setTableCellsColSpan(1);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableBorderColor("camposParamCCTable", 0xFFFFFF);
	    	
		    report.addSectionToDocument("seccionCamposParamCC");
		    //****************************************************
	    }
        
        report.font.setFontSizeAndAttributes(11,false,false,false);
        report.createSection("seccionFinal","tablaFinal",3,1,0);
        report.getSectionReference("seccionFinal").setTableBorder("tablaFinal", 0xFFFFFF, 0.0f);
        report.getSectionReference("seccionFinal").setTableCellBorderWidth("tablaFinal", 0.5f);
        report.getSectionReference("seccionFinal").setTableCellsDefaultColors("tablaFinal", 0xFFFFFF, 0xFFFFFF);
        report.getSectionReference("seccionFinal").setTableCellsColSpan(1);
        
        String plan=(String)datos.get("plan");
        if(!plan.equals(""))
        {
        	String [] planes=plan.split("-");
            report.getSectionReference("seccionFinal").addTableTextCellAligned("tablaFinal", "Plan Diagnóstico y Terapeútico: "+planes[0], report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);        	
        }

        String comentario=(String)datos.get("comentarios");
        if(!comentario.equals(""))
        {
        	String [] comentarios=comentario.split("-");
            report.getSectionReference("seccionFinal").addTableTextCellAligned("tablaFinal", "Comentarios Generales: "+comentarios[0], report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);        	
        }

        String pronostico=(String)datos.get("pronostico");
        if(!pronostico.equals(""))
        {
        	String [] pronosticos=pronostico.split("-");
            report.getSectionReference("seccionFinal").addTableTextCellAligned("tablaFinal", "Deberes y/o derechos del paciente: "+pronosticos[0], report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);        	
        }
        //adicionar la seccion al reporte
        report.addSectionToDocument("seccionFinal");
        
        //********CAMPOS PARAMETRIZABLES OBSERVACIONES***************************************************************************
        camposParam = (HashMap)datos.get("camposParamObservaciones");
        sectionData = new String[0];
	    numRegistros = 0;
	    
	    if(camposParam!=null&&camposParam.get("numRegistros")!=null)
	    	numRegistros = Integer.parseInt(camposParam.get("numRegistros").toString());
	    
	    if(numRegistros>0)
	    {
	    	
	    	
		    //****************************************************
	    	section = report.createSection("seccionCamposParamO", "camposParamOTable", numRegistros, 6, 0);
	  		report.font.setFontAttributes(0x000000, 10, true, false, false);
	  		section.setTableBorder("camposParamOTable", 0xFFFFFF, 0.0f);
		    section.setTableCellBorderWidth("camposParamOTable", 0.5f);
		    section.setTableCellPadding("camposParamOTable", 1);
		    section.setTableSpaceBetweenCells("camposParamOTable", 0.5f);
		    section.setTableCellsDefaultColors("camposParamOTable", 0xFFFFFF, 0xFFFFFF);
	  		//report.addSectionTitle("antecedentesPediatricos", "revisionSistemasTable", "Revisión por Sistemas");
		    report.font.setFontAttributes(0x000000, 10, false, false, false);
		    section.setTableCellsColSpan(3);
		    
		    int contador = 0;
		    sectionData = new String[numRegistros*2];
		    for(int i=0;i<numRegistros;i++)
		    {
		    	sectionData[contador] = camposParam.get("nombre_"+i).toString();
		    	contador++;
		    	String temp = camposParam.get("info_"+i).toString();
		    	switch(Integer.parseInt(camposParam.get("tipo_"+i).toString()))
		    	{
		    		case ConstantesBD.tipoParametrizablesTexto:
		    		case ConstantesBD.tipoParametrizablesTextArea:
		    		break;
		    		case ConstantesBD.tipoParametrizablesArchivo:
		    			temp = "";
		    		break;
		    		case ConstantesBD.tipoParametrizablesSelect:
		    			temp = UtilidadTexto.getBoolean(temp)?"Sí":"No";
		    		break;
		    	}
		    	sectionData[contador] = temp;
		    	contador++;
		    }
		    
		    report.addSectionData("seccionCamposParamO", "camposParamOTable", sectionData);
		    section.setTableCellsColSpan(1);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableBorderColor("camposParamOTable", 0xFFFFFF);
	    	
		    report.addSectionToDocument("seccionCamposParamO");
		    //****************************************************
	    }
        
        
        //Informacion del medico responsable
        report.font.setFontSizeAndAttributes(11,true,false,false);
        report.createSection("seccionResponsable","tablaResponsable",2,1,0);
        report.getSectionReference("seccionResponsable").setTableBorder("tablaResponsable", 0xFFFFFF, 0.0f);
        report.getSectionReference("seccionResponsable").setTableCellBorderWidth("tablaResponsable", 0.5f);
        report.getSectionReference("seccionResponsable").setTableCellsDefaultColors("tablaResponsable", 0xFFFFFF, 0xFFFFFF);
        report.getSectionReference("seccionResponsable").addTableTextCellAligned("tablaResponsable", "Profesional Responsable", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        report.font.setFontSizeAndAttributes(11,false,false,false);
        report.getSectionReference("seccionResponsable").addTableTextCellAligned("tablaResponsable", ""+(String)datos.get("medico"), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);        	
        //adicionar la seccion al reporte
        report.addSectionToDocument("seccionResponsable");
        
        //cerrando el reporte
        report.closeReport(); 
        
        
	}
	
	
	
	/****************************************************************************************************************************************************
				Metodo para la impresion de la valoracion de consulta externa de tipo pediatrica
	****************************************************************************************************************************************************/
	
	public static void pdfValoracionPediatricaInterconsulta(String nombreArchivo, HashMap datosPediatricos,  UsuarioBasico usu, PersonaBasica paciente)
	{	
     
		 //variable para manejar el reporte
        PdfReports report = new PdfReports();
        
        
        
        //tomando la ruta definida en el web.xml para generar el reporte.
        String filePath=ValoresPorDefecto.getFilePath();
        //titulo del reporte
        String tituloReporte="VALORACION "+ (String)datosPediatricos.get("unidadConsulta");
        
        InstitucionBasica institucionBasica= new InstitucionBasica();
        institucionBasica.cargarXConvenio(usu.getCodigoInstitucionInt(), paciente.getCodigoConvenio());
        
        report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), tituloReporte);

        //generacion de la cabecera, validando el tipo de separador e utilizado en el path
        
        //abrir reporte
        report.openReport(nombreArchivo);
        
        //Informacion del Paciente
        report.font.setFontSizeAndAttributes(11,true,false,false);
        report.document.addParagraph("Información Personal ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);
        report.font.setFontSizeAndAttributes(11,false,false,false);
        
        report.document.addParagraph("Apellidos y Nombres: "+(String)datosPediatricos.get("apellidosNombres"),report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
        report.document.addParagraph(""+(String)datosPediatricos.get("tipoIdentificacion")+": "+(String)datosPediatricos.get("numeroIdentificacion")+" De: "+(String)datosPediatricos.get("ciudadIdentificacion")+"     "+"Edad: "+(String)datosPediatricos.get("edad")+"     "+"Sexo: "+(String)datosPediatricos.get("sexo"),report.font,iTextBaseDocument.ALIGNMENT_LEFT,12);
        report.document.addParagraph("Responsable: "+(String)datosPediatricos.get("empresaResponsable"),report.font,iTextBaseDocument.ALIGNMENT_LEFT,12);
        report.document.addParagraph("Área: "+(String)datosPediatricos.get("area"),report.font,iTextBaseDocument.ALIGNMENT_LEFT,12);
        
       
        //Informacion de la Consulta
        report.font.setFontSizeAndAttributes(11,true,false,false);
        report.createSection("seccionConsulta","tablaConsulta",3,3,0);
        report.getSectionReference("seccionConsulta").setTableBorder("tablaConsulta", 0xFFFFFF, 0.0f);
        report.getSectionReference("seccionConsulta").setTableCellBorderWidth("tablaConsulta", 0.5f);
        report.getSectionReference("seccionConsulta").setTableCellsDefaultColors("tablaConsulta", 0xFFFFFF, 0xFFFFFF);
        report.getSectionReference("seccionConsulta").setTableCellsColSpan(3);
        report.getSectionReference("seccionConsulta").addTableTextCellAligned("tablaConsulta", "Información de la Consulta ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        report.font.setFontSizeAndAttributes(11,false,false,false);
        report.getSectionReference("seccionConsulta").setTableCellsColSpan(1);
        String hora=(String)datosPediatricos.get("horaValoracion");
        report.getSectionReference("seccionConsulta").addTableTextCellAligned("tablaConsulta", "Fecha Cita: "+(String)datosPediatricos.get("fechaAdmision")+"     "+(String)datosPediatricos.get("horaAdmision"), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        report.getSectionReference("seccionConsulta").addTableTextCellAligned("tablaConsulta", "Fecha Atención: "+(String)datosPediatricos.get("fechaValoracion")+"     "+hora.substring(0,5), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        report.getSectionReference("seccionConsulta").addTableTextCellAligned("tablaConsulta", "Tipo de Cita: "+(String)datosPediatricos.get("tipoConsulta"), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        report.getSectionReference("seccionConsulta").setTableCellsColSpan(3);
        report.getSectionReference("seccionConsulta").addTableTextCellAligned("tablaConsulta", "Motivo Consulta y Enfermedad Actual: "+(String)datosPediatricos.get("motivoConsulta"), report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,iTextBaseDocument.ALIGNMENT_LEFT);
        //adicionar la seccion al reporte
        report.addSectionToDocument("seccionConsulta");
        
        
        //Informacion de la Valoracion Medica
        report.font.setFontSizeAndAttributes(11,true,false,false);
        report.document.addParagraph("Valoracion Médica",report.font,iTextBaseDocument.ALIGNMENT_LEFT,10);
        report.font.setFontSizeAndAttributes(11,false,false,false);
        
        /***************************************************EXAMENES FISICOS**************************************************************************************/
        HashMap examenesFisicos=(HashMap)datosPediatricos.get("examenesFisicos");
        HashMap examenesFisicosOpc=(HashMap)datosPediatricos.get("examenesFisicosOpc");
        iTextBaseTable section = null;
        
        //************REVISION POR SISTEMAS ***********************************************************************************************
        int numRegistros = Integer.parseInt(examenesFisicos.get("numRegistros").toString());
        int numRegistrosOpc = Integer.parseInt(examenesFisicosOpc.get("numRegistros").toString());
        
        if(numRegistros>0)
        {
        	//Revision por sistemas
	        report.font.setFontSizeAndAttributes(11,true,false,false);
	        report.document.addParagraph("Revisión por Sistemas",report.font,iTextBaseDocument.ALIGNMENT_LEFT,20);
	        report.font.setFontSizeAndAttributes(11,false,false,false);
        	
        	//****************************************************
		    section = report.createSection("seccionRevisionSistemas", "revisionSistemasTable", numRegistros, 6, 0);
	  		report.font.setFontAttributes(0x000000, 10, true, false, false);
	  		section.setTableBorder("revisionSistemasTable", 0xFFFFFF, 0.0f);
		    section.setTableCellBorderWidth("revisionSistemasTable", 0.5f);
		    section.setTableCellPadding("revisionSistemasTable", 1);
		    section.setTableSpaceBetweenCells("revisionSistemasTable", 0.5f);
		    section.setTableCellsDefaultColors("revisionSistemasTable", 0xFFFFFF, 0xFFFFFF);
		    report.font.setFontAttributes(0x000000, 10, false, false, false);
		    section.setTableCellsColSpan(6);
		    
		    for(int i=0;i<numRegistros;i++)
		    {
		    	section.addTableTextCellAligned("revisionSistemasTable", examenesFisicos.get(i+"")+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		    }
		    
		    section.setTableCellsColSpan(1);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableBorderColor("revisionSistemasTable", 0xFFFFFF);
	    	report.addSectionToDocument("seccionRevisionSistemas");
		    //****************************************************
        }
        
        if(numRegistrosOpc>0)
        {
        	if(numRegistros<=0)
        	{
        		//Revision por sistemas
    	        report.font.setFontSizeAndAttributes(11,true,false,false);
    	        report.document.addParagraph("Revisión por Sistemas",report.font,iTextBaseDocument.ALIGNMENT_LEFT,20);
    	        report.font.setFontSizeAndAttributes(11,false,false,false);
            	
        	}
        	
        	//****************************************************
		    section = report.createSection("seccionRevisionSistemasOpc", "revisionSistemasOpcTable", numRegistrosOpc, 6, 0);
	  		report.font.setFontAttributes(0x000000, 11, true, false, false);
	  		section.setTableBorder("revisionSistemasOpcTable", 0xFFFFFF, 0.0f);
		    section.setTableCellBorderWidth("revisionSistemasOpcTable", 0.5f);
		    section.setTableCellPadding("revisionSistemasOpcTable", 1);
		    section.setTableSpaceBetweenCells("revisionSistemasOpcTable", 0.5f);
		    section.setTableCellsDefaultColors("revisionSistemasOpcTable", 0xFFFFFF, 0xFFFFFF);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableCellsColSpan(6);
		    
		    for(int i=0;i<numRegistrosOpc;i++)
		    {
		    	section.addTableTextCellAligned("revisionSistemasOpcTable", examenesFisicosOpc.get(i+"")+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		    }
		    
		    section.setTableCellsColSpan(1);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableBorderColor("revisionSistemasOpcTable", 0xFFFFFF);
	    	report.addSectionToDocument("seccionRevisionSistemasOpc");
		    //****************************************************
        }
        
        //********CAMPOS PARAMETRIZABLES REVISION POR SISTEMAS***************************************************************************
        HashMap camposParam = (HashMap)datosPediatricos.get("camposParamRevisionSistemas");
        String[] sectionData = new String[0];
	    numRegistros = 0;
	    
	    if(camposParam!=null&&camposParam.get("numRegistros")!=null)
	    	numRegistros = Integer.parseInt(camposParam.get("numRegistros").toString());
	    
	    if(numRegistros>0)
	    {
	    	
	    	
		    //****************************************************
	    	section = report.createSection("seccionCamposParamRV", "camposParamRVTable", numRegistros, 6, 0);
	  		report.font.setFontAttributes(0x000000, 10, true, false, false);
	  		section.setTableBorder("camposParamRVTable", 0xFFFFFF, 0.0f);
		    section.setTableCellBorderWidth("camposParamRVTable", 0.5f);
		    section.setTableCellPadding("camposParamRVTable", 1);
		    section.setTableSpaceBetweenCells("camposParamRVTable", 0.5f);
		    section.setTableCellsDefaultColors("camposParamRVTable", 0xFFFFFF, 0xFFFFFF);
	  		//report.addSectionTitle("antecedentesPediatricos", "revisionSistemasTable", "Revisión por Sistemas");
		    report.font.setFontAttributes(0x000000, 10, false, false, false);
		    section.setTableCellsColSpan(3);
		    
		    int contador = 0;
		    sectionData = new String[numRegistros*2];
		    for(int i=0;i<numRegistros;i++)
		    {
		    	sectionData[contador] = camposParam.get("nombre_"+i).toString();
		    	contador++;
		    	String temp = camposParam.get("info_"+i).toString();
		    	switch(Integer.parseInt(camposParam.get("tipo_"+i).toString()))
		    	{
		    		case ConstantesBD.tipoParametrizablesTexto:
		    		case ConstantesBD.tipoParametrizablesTextArea:
		    		break;
		    		case ConstantesBD.tipoParametrizablesArchivo:
		    			temp = "";
		    		break;
		    		case ConstantesBD.tipoParametrizablesSelect:
		    			temp = UtilidadTexto.getBoolean(temp)?"Sí":"No";
		    		break;
		    	}
		    	sectionData[contador] = temp;
		    	contador++;
		    }
		    
		    report.addSectionData("seccionCamposParamRV", "camposParamRVTable", sectionData);
		    section.setTableCellsColSpan(1);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableBorderColor("camposParamRVTable", 0xFFFFFF);
	    	
		    report.addSectionToDocument("seccionCamposParamRV");
		    //****************************************************
	    }
        
        //*********************************************************************************************************************************
        
        
        /***********************************EXÁMENES FÍSICOS********************************************************************************/
        //En esta parte realizao validaciones para estar seguro de solamente
        //adicionar a la impresion los campos que realmente contengan informacion
        HashMap signosVitales=(HashMap)datosPediatricos.get("signosVitales");
        numRegistros = Integer.parseInt(signosVitales.get("numRegistros").toString());
        if(numRegistros>0)
        {
        	//Exámenes Físicos
	        report.font.setFontSizeAndAttributes(11,true,false,false);
	        report.document.addParagraph("Exámenes Físicos",report.font,iTextBaseDocument.ALIGNMENT_LEFT,10);
	        report.font.setFontSizeAndAttributes(11,false,false,false);
        	
        	
        	//****************************************************
		    section = report.createSection("seccionExamenesFisicos", "examenesFisicosTable", (numRegistros/3+numRegistros%3), 6, 0);
	  		report.font.setFontAttributes(0x000000, 11, true, false, false);
	  		section.setTableBorder("examenesFisicosTable", 0xFFFFFF, 0.0f);
		    section.setTableCellBorderWidth("examenesFisicosTable", 0.5f);
		    section.setTableCellPadding("examenesFisicosTable", 1);
		    section.setTableSpaceBetweenCells("examenesFisicosTable", 0.5f);
		    section.setTableCellsDefaultColors("examenesFisicosTable", 0xFFFFFF, 0xFFFFFF);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableCellsColSpan(2);
		    
		    for(int i=0;i<numRegistros;i++)
		    {
		    	section.addTableTextCellAligned("examenesFisicosTable", signosVitales.get(i+"")+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		    }
		    
		    section.setTableCellsColSpan(1);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableBorderColor("examenesFisicosTable", 0xFFFFFF);
	    	report.addSectionToDocument("seccionExamenesFisicos");
		    //****************************************************
        }
        
        //********CAMPOS PARAMETRIZABLES EXÁMENES FISICOS***************************************************************************
        camposParam = (HashMap)datosPediatricos.get("camposParamExamenesFisicos");
        sectionData = new String[0];
	    numRegistros = 0;
	    
	    if(camposParam!=null&&camposParam.get("numRegistros")!=null)
	    	numRegistros = Integer.parseInt(camposParam.get("numRegistros").toString());
	    
	    if(numRegistros>0)
	    {
	    	
	    	
		    //****************************************************
	    	section = report.createSection("seccionCamposParamEF", "camposParamEFTable", numRegistros, 6, 0);
	  		report.font.setFontAttributes(0x000000, 10, true, false, false);
	  		section.setTableBorder("camposParamEFTable", 0xFFFFFF, 0.0f);
		    section.setTableCellBorderWidth("camposParamEFTable", 0.5f);
		    section.setTableCellPadding("camposParamEFTable", 1);
		    section.setTableSpaceBetweenCells("camposParamEFTable", 0.5f);
		    section.setTableCellsDefaultColors("camposParamEFTable", 0xFFFFFF, 0xFFFFFF);
	  		//report.addSectionTitle("antecedentesPediatricos", "revisionSistemasTable", "Revisión por Sistemas");
		    report.font.setFontAttributes(0x000000, 10, false, false, false);
		    section.setTableCellsColSpan(3);
		    
		    int contador = 0;
		    sectionData = new String[numRegistros*2];
		    for(int i=0;i<numRegistros;i++)
		    {
		    	sectionData[contador] = camposParam.get("nombre_"+i).toString();
		    	contador++;
		    	String temp = camposParam.get("info_"+i).toString();
		    	switch(Integer.parseInt(camposParam.get("tipo_"+i).toString()))
		    	{
		    		case ConstantesBD.tipoParametrizablesTexto:
		    		case ConstantesBD.tipoParametrizablesTextArea:
		    		break;
		    		case ConstantesBD.tipoParametrizablesArchivo:
		    			temp = "";
		    		break;
		    		case ConstantesBD.tipoParametrizablesSelect:
		    			temp = UtilidadTexto.getBoolean(temp)?"Sí":"No";
		    		break;
		    	}
		    	sectionData[contador] = temp;
		    	contador++;
		    }
		    
		    report.addSectionData("seccionCamposParamEF", "camposParamEFTable", sectionData);
		    section.setTableCellsColSpan(1);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableBorderColor("camposParamEFTable", 0xFFFFFF);
	    	
		    report.addSectionToDocument("seccionCamposParamEF");
		    //****************************************************
	    }
       
        
        /*******************************************DESARROLLO**************************************************************************************************/
        HashMap desarrolloPsicomotor=(HashMap)datosPediatricos.get("desarrolloPsicomotor");
        numRegistros = Integer.parseInt(desarrolloPsicomotor.get("numRegistros").toString());
        if(numRegistros>0)
        {
        	//Desarrollo Psicomotor
	        report.font.setFontSizeAndAttributes(11,true,false,false);
	        report.document.addParagraph("Desarrollo Psicomotor",report.font,iTextBaseDocument.ALIGNMENT_LEFT,10);
	        report.font.setFontSizeAndAttributes(11,false,false,false);
        	
        	
        	//****************************************************
		    section = report.createSection("seccionDesarrolloPsico", "desarrolloPsicoTable", numRegistros, 6, 0);
	  		report.font.setFontAttributes(0x000000, 11, true, false, false);
	  		section.setTableBorder("desarrolloPsicoTable", 0xFFFFFF, 0.0f);
		    section.setTableCellBorderWidth("desarrolloPsicoTable", 0.5f);
		    section.setTableCellPadding("desarrolloPsicoTable", 1);
		    section.setTableSpaceBetweenCells("desarrolloPsicoTable", 0.5f);
		    section.setTableCellsDefaultColors("desarrolloPsicoTable", 0xFFFFFF, 0xFFFFFF);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableCellsColSpan(2);
		    
		    for(int i=0;i<numRegistros;i++)
		    {
		    	section.addTableTextCellAligned("desarrolloPsicoTable", desarrolloPsicomotor.get(i+"")+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		    	section.addTableTextCellAligned("desarrolloPsicoTable", desarrolloPsicomotor.get("valor_"+i)+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		    	section.addTableTextCellAligned("desarrolloPsicoTable", desarrolloPsicomotor.get("desc_"+i)+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		    }
		    
		    section.setTableCellsColSpan(1);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableBorderColor("desarrolloPsicoTable", 0xFFFFFF);
	    	report.addSectionToDocument("seccionDesarrolloPsico");
		    //****************************************************
        }
        
        HashMap desarrolloLenguage=(HashMap)datosPediatricos.get("desarrolloLenguaje");
        numRegistros = Integer.parseInt(desarrolloLenguage.get("numRegistros").toString());
        if(numRegistros>0)
        {
        	//Desarrollo Psicomotor
	        report.font.setFontSizeAndAttributes(11,true,false,false);
	        report.document.addParagraph("Desarrollo Lenguaje",report.font,iTextBaseDocument.ALIGNMENT_LEFT,10);
	        report.font.setFontSizeAndAttributes(11,false,false,false);
        	
        	
        	//****************************************************
		    section = report.createSection("seccionDesarrolloLeng", "desarrolloLengTable", numRegistros, 6, 0);
	  		report.font.setFontAttributes(0x000000, 11, true, false, false);
	  		section.setTableBorder("desarrolloLengTable", 0xFFFFFF, 0.0f);
		    section.setTableCellBorderWidth("desarrolloLengTable", 0.5f);
		    section.setTableCellPadding("desarrolloLengTable", 1);
		    section.setTableSpaceBetweenCells("desarrolloLengTable", 0.5f);
		    section.setTableCellsDefaultColors("desarrolloLengTable", 0xFFFFFF, 0xFFFFFF);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableCellsColSpan(2);
		    
		    for(int i=0;i<numRegistros;i++)
		    {
		    	section.addTableTextCellAligned("desarrolloLengTable", desarrolloLenguage.get(i+"")+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		    	section.addTableTextCellAligned("desarrolloLengTable", desarrolloLenguage.get("valor_"+i)+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		    	section.addTableTextCellAligned("desarrolloLengTable", desarrolloLenguage.get("desc_"+i)+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		    }
		    
		    section.setTableCellsColSpan(1);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableBorderColor("desarrolloLengTable", 0xFFFFFF);
	    	report.addSectionToDocument("seccionDesarrolloLeng");
		    //****************************************************
        }
        
        
        
        HashMap desarrolloPC=(HashMap)datosPediatricos.get("desarrolloPC");
        numRegistros = Integer.parseInt(desarrolloPC.get("numRegistros").toString());
        if(numRegistros>0)
        {
        	//Desarrollo Por Conductas
	        report.font.setFontSizeAndAttributes(11,true,false,false);
	        report.document.addParagraph("Desarrollo Por Conductas",report.font,iTextBaseDocument.ALIGNMENT_LEFT,10);
	        report.font.setFontSizeAndAttributes(11,false,false,false);
        	
        	
        	//****************************************************
		    section = report.createSection("seccionDesarrolloPC", "desarrolloPCTable", numRegistros, 6, 0);
	  		report.font.setFontAttributes(0x000000, 11, true, false, false);
	  		section.setTableBorder("desarrolloPCTable", 0xFFFFFF, 0.0f);
		    section.setTableCellBorderWidth("desarrolloPCTable", 0.5f);
		    section.setTableCellPadding("desarrolloPCTable", 1);
		    section.setTableSpaceBetweenCells("desarrolloPCTable", 0.5f);
		    section.setTableCellsDefaultColors("desarrolloPCTable", 0xFFFFFF, 0xFFFFFF);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableCellsColSpan(2);
		    
		    for(int i=0;i<numRegistros;i++)
		    {
		    	section.addTableTextCellAligned("desarrolloPCTable", desarrolloPC.get(i+"")+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		    	section.addTableTextCellAligned("desarrolloPCTable", desarrolloPC.get("valor_"+i)+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		    	section.addTableTextCellAligned("desarrolloPCTable", desarrolloPC.get("desc_"+i)+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		    }
		    
		    section.setTableCellsColSpan(1);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableBorderColor("desarrolloPCTable", 0xFFFFFF);
	    	report.addSectionToDocument("seccionDesarrolloPC");
		    //****************************************************
        }
        
        
        String observacionesDesarrollo=(String)datosPediatricos.get("observacionesDesarrollo");
        if(!observacionesDesarrollo.equals(""))
        {
        	//Observaciones Desarrollo
	        report.font.setFontSizeAndAttributes(11,true,false,false);
	        report.document.addParagraph("Observaciones Desarrollo",report.font,iTextBaseDocument.ALIGNMENT_LEFT,10);
	        report.font.setFontSizeAndAttributes(11,false,false,false);
        	
        	
        	//****************************************************
		    section = report.createSection("seccionObservacionesDesarrollo", "observacionesDesarrolloTable", 1, 6, 0);
	  		report.font.setFontAttributes(0x000000, 11, true, false, false);
	  		section.setTableBorder("observacionesDesarrolloTable", 0xFFFFFF, 0.0f);
		    section.setTableCellBorderWidth("observacionesDesarrolloTable", 0.5f);
		    section.setTableCellPadding("observacionesDesarrolloTable", 1);
		    section.setTableSpaceBetweenCells("observacionesDesarrolloTable", 0.5f);
		    section.setTableCellsDefaultColors("observacionesDesarrolloTable", 0xFFFFFF, 0xFFFFFF);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableCellsColSpan(6);
		    
		    section.addTableTextCellAligned("observacionesDesarrolloTable", observacionesDesarrollo, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		    
		    section.setTableCellsColSpan(1);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableBorderColor("observacionesDesarrolloTable", 0xFFFFFF);
	    	report.addSectionToDocument("seccionObservacionesDesarrollo");
		    //****************************************************
        }
        
        //********CAMPOS PARAMETRIZABLES DESARROLLO***************************************************************************
        camposParam = (HashMap)datosPediatricos.get("camposParamDesarrollo");
        sectionData = new String[0];
	    numRegistros = 0;
	    
	    if(camposParam!=null&&camposParam.get("numRegistros")!=null)
	    	numRegistros = Integer.parseInt(camposParam.get("numRegistros").toString());
	    
	    if(numRegistros>0)
	    {
	    	
	    	
		    //****************************************************
	    	section = report.createSection("seccionCamposParamD", "camposParamDTable", numRegistros, 6, 0);
	  		report.font.setFontAttributes(0x000000, 10, true, false, false);
	  		section.setTableBorder("camposParamDTable", 0xFFFFFF, 0.0f);
		    section.setTableCellBorderWidth("camposParamDTable", 0.5f);
		    section.setTableCellPadding("camposParamDTable", 1);
		    section.setTableSpaceBetweenCells("camposParamDTable", 0.5f);
		    section.setTableCellsDefaultColors("camposParamDTable", 0xFFFFFF, 0xFFFFFF);
	  		//report.addSectionTitle("antecedentesPediatricos", "revisionSistemasTable", "Revisión por Sistemas");
		    report.font.setFontAttributes(0x000000, 10, false, false, false);
		    section.setTableCellsColSpan(3);
		    
		    int contador = 0;
		    sectionData = new String[numRegistros*2];
		    for(int i=0;i<numRegistros;i++)
		    {
		    	sectionData[contador] = camposParam.get("nombre_"+i).toString();
		    	contador++;
		    	String temp = camposParam.get("info_"+i).toString();
		    	switch(Integer.parseInt(camposParam.get("tipo_"+i).toString()))
		    	{
		    		case ConstantesBD.tipoParametrizablesTexto:
		    		case ConstantesBD.tipoParametrizablesTextArea:
		    		break;
		    		case ConstantesBD.tipoParametrizablesArchivo:
		    			temp = "";
		    		break;
		    		case ConstantesBD.tipoParametrizablesSelect:
		    			temp = UtilidadTexto.getBoolean(temp)?"Sí":"No";
		    		break;
		    	}
		    	sectionData[contador] = temp;
		    	contador++;
		    }
		    
		    report.addSectionData("seccionCamposParamD", "camposParamDTable", sectionData);
		    section.setTableCellsColSpan(1);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableBorderColor("camposParamDTable", 0xFFFFFF);
	    	
		    report.addSectionToDocument("seccionCamposParamD");
		    //****************************************************
	    }
        	
        
        
        /**********************************************************LACTANCIA EN CASO DE QUE EXISTA**************************************************************/
        String lactanciaMaterna=(String)datosPediatricos.get("lactanciaMaterna");
        String otrasLeches=(String)datosPediatricos.get("otrasLeches");
        String descOtrasLeches=(String)datosPediatricos.get("descOtrasLeches");
        String alimentacionComplementaria=(String)datosPediatricos.get("alimentacionComplementaria");
        String descAlimentacionComplementaria=(String)datosPediatricos.get("descAlimentacionComplementaria");
        numRegistros = 0;
        
        if((!lactanciaMaterna.equals(""))||(!otrasLeches.equals(""))||(!descOtrasLeches.equals(""))||(!alimentacionComplementaria.equals(""))||(!descAlimentacionComplementaria.equals("")))
        {
        	//Lactancia
	        report.font.setFontSizeAndAttributes(11,true,false,false);
	        report.document.addParagraph("Lactancia",report.font,iTextBaseDocument.ALIGNMENT_LEFT,10);
	        report.font.setFontSizeAndAttributes(11,false,false,false);
	        
	        if(!lactanciaMaterna.equals(""))
	        	numRegistros++;
	        if(!otrasLeches.equals(""))
	        	numRegistros++;
	        if(!descOtrasLeches.equals(""))
	        	numRegistros++;
	        if(!alimentacionComplementaria.equals(""))
	        	numRegistros++;
	        if(!descAlimentacionComplementaria.equals(""))
	        	numRegistros++;
	        
	        
	        
	        //****************************************************
		    section = report.createSection("seccionLactancia", "lactanciaTable", numRegistros, 6, 0);
	  		report.font.setFontAttributes(0x000000, 11, true, false, false);
	  		section.setTableBorder("lactanciaTable", 0xFFFFFF, 0.0f);
		    section.setTableCellBorderWidth("lactanciaTable", 0.5f);
		    section.setTableCellPadding("lactanciaTable", 1);
		    section.setTableSpaceBetweenCells("lactanciaTable", 0.5f);
		    section.setTableCellsDefaultColors("lactanciaTable", 0xFFFFFF, 0xFFFFFF);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableCellsColSpan(3);
		    
		    if(!lactanciaMaterna.equals(""))
		    {
		    	section.addTableTextCellAligned("lactanciaTable", "Lactancia Materna:", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    	section.addTableTextCellAligned("lactanciaTable", lactanciaMaterna, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    }
		    
		    if(!otrasLeches.equals(""))
		    {
		    	section.addTableTextCellAligned("lactanciaTable", "Otras Leches:", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    	section.addTableTextCellAligned("lactanciaTable", otrasLeches, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    }
		    
		    if(!descOtrasLeches.equals(""))
		    {
		    	section.setTableCellsColSpan(6);
		    	section.addTableTextCellAligned("lactanciaTable", descOtrasLeches, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    }
		    section.setTableCellsColSpan(3);
		    
		    if(!alimentacionComplementaria.equals(""))
		    {
		    	section.addTableTextCellAligned("lactanciaTable", "Alimentación Complementaria:", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    	section.addTableTextCellAligned("lactanciaTable", alimentacionComplementaria, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    }
		    
		    if(!descAlimentacionComplementaria.equals(""))
		    {
		    	section.setTableCellsColSpan(6);
		    	section.addTableTextCellAligned("lactanciaTable", descAlimentacionComplementaria, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    }
		    
		    section.setTableCellsColSpan(1);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableBorderColor("lactanciaTable", 0xFFFFFF);
	    	report.addSectionToDocument("seccionLactancia");
		    //****************************************************
        	
    		
        }
        
        
        
        /*********************************************VALORACION NUTRICIONAL************************************************************************************/
        HashMap edadesAlimentacion=(HashMap)datosPediatricos.get("edadesAlimentacion");
        String estadoNutricional=(String)datosPediatricos.get("estadoNutricional");
        String tipoAlimentacion=(String)datosPediatricos.get("tipoAlimentacion");
        String observacionValNut=(String)datosPediatricos.get("observacionesValNut");
        numRegistros = Integer.parseInt(edadesAlimentacion.get("numRegistros").toString());

        if((numRegistros>0)||(!estadoNutricional.equals(""))||(!tipoAlimentacion.equals(""))||(!observacionValNut.equals("")))
        {
        	//Valoración Nutricional
	        report.font.setFontSizeAndAttributes(11,true,false,false);
	        report.document.addParagraph("Valoración Nutricional",report.font,iTextBaseDocument.ALIGNMENT_LEFT,10);
	        report.font.setFontSizeAndAttributes(11,false,false,false);
	        
	        if(numRegistros>0)
	        {
		        //****************************************************
			    section = report.createSection("seccionEdadesAlimentacion", "edadesAlimentacionTable", numRegistros, 6, 0);
		  		report.font.setFontAttributes(0x000000, 11, true, false, false);
		  		section.setTableBorder("edadesAlimentacionTable", 0xFFFFFF, 0.0f);
			    section.setTableCellBorderWidth("edadesAlimentacionTable", 0.5f);
			    section.setTableCellPadding("edadesAlimentacionTable", 1);
			    section.setTableSpaceBetweenCells("edadesAlimentacionTable", 0.5f);
			    section.setTableCellsDefaultColors("edadesAlimentacionTable", 0xFFFFFF, 0xFFFFFF);
			    report.font.setFontAttributes(0x000000, 11, false, false, false);
			    section.setTableCellsColSpan(3);
			    
			    for(int i=0;i<numRegistros;i++)
			    {
			    	section.addTableTextCellAligned("edadesAlimentacionTable", edadesAlimentacion.get(i+"")+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    	section.addTableTextCellAligned("edadesAlimentacionTable", edadesAlimentacion.get("valor_"+i)+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    }
			    
			    section.setTableCellsColSpan(1);
			    report.font.setFontAttributes(0x000000, 11, false, false, false);
			    section.setTableBorderColor("edadesAlimentacionTable", 0xFFFFFF);
		    	report.addSectionToDocument("seccionEdadesAlimentacion");
			    //****************************************************
	        }
	        
	        numRegistros = 0;
	        if(!estadoNutricional.equals(""))
	        	numRegistros++;
	        if(!tipoAlimentacion.equals(""))
	        	numRegistros++;
	        if(!observacionValNut.equals(""))
	        	numRegistros++;
	        
	        if(numRegistros>0)
	        {
	        	//****************************************************
			    section = report.createSection("seccionValNutricional", "valNutricionalTable", numRegistros, 6, 0);
		  		report.font.setFontAttributes(0x000000, 11, true, false, false);
		  		section.setTableBorder("valNutricionalTable", 0xFFFFFF, 0.0f);
			    section.setTableCellBorderWidth("valNutricionalTable", 0.5f);
			    section.setTableCellPadding("valNutricionalTable", 1);
			    section.setTableSpaceBetweenCells("valNutricionalTable", 0.5f);
			    section.setTableCellsDefaultColors("valNutricionalTable", 0xFFFFFF, 0xFFFFFF);
			    report.font.setFontAttributes(0x000000, 11, false, false, false);
			    section.setTableCellsColSpan(3);
			    
			    if(!estadoNutricional.equals(""))
			    {
			    	section.setTableCellsColSpan(1);
			    	section.addTableTextCellAligned("valNutricionalTable", "Estado Nutricional:", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    	section.setTableCellsColSpan(5);
			    	section.addTableTextCellAligned("valNutricionalTable", estadoNutricional, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    }
			    
			    if(!tipoAlimentacion.equals(""))
			    {
			    	section.setTableCellsColSpan(1);
			    	section.addTableTextCellAligned("valNutricionalTable", "Tipo Alimentacion:", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    	section.setTableCellsColSpan(5);
			    	section.addTableTextCellAligned("valNutricionalTable", tipoAlimentacion, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    }
			    
			    if(!observacionValNut.equals(""))
			    {
			    	section.setTableCellsColSpan(1);
			    	section.addTableTextCellAligned("valNutricionalTable", "Observaciones:", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    	section.setTableCellsColSpan(5);
			    	section.addTableTextCellAligned("valNutricionalTable", observacionValNut, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    }
			    
			  
			    
			    section.setTableCellsColSpan(1);
			    report.font.setFontAttributes(0x000000, 11, false, false, false);
			    section.setTableBorderColor("valNutricionalTable", 0xFFFFFF);
		    	report.addSectionToDocument("seccionValNutricional");
			    //****************************************************
	        }
        	
        	
        }
        
        //********CAMPOS PARAMETRIZABLES VALORACIÓN NUTRICIONAL***************************************************************************
        camposParam = (HashMap)datosPediatricos.get("camposParamValoracionNutricional");
        sectionData = new String[0];
	    numRegistros = 0;
	    
	    if(camposParam!=null&&camposParam.get("numRegistros")!=null)
	    	numRegistros = Integer.parseInt(camposParam.get("numRegistros").toString());
	    
	    if(numRegistros>0)
	    {
	    	
	    	
		    //****************************************************
	    	section = report.createSection("seccionCamposParamVN", "camposParamVNTable", numRegistros, 6, 0);
	  		report.font.setFontAttributes(0x000000, 10, true, false, false);
	  		section.setTableBorder("camposParamVNTable", 0xFFFFFF, 0.0f);
		    section.setTableCellBorderWidth("camposParamVNTable", 0.5f);
		    section.setTableCellPadding("camposParamVNTable", 1);
		    section.setTableSpaceBetweenCells("camposParamVNTable", 0.5f);
		    section.setTableCellsDefaultColors("camposParamVNTable", 0xFFFFFF, 0xFFFFFF);
	  		//report.addSectionTitle("antecedentesPediatricos", "revisionSistemasTable", "Revisión por Sistemas");
		    report.font.setFontAttributes(0x000000, 10, false, false, false);
		    section.setTableCellsColSpan(3);
		    
		    int contador = 0;
		    sectionData = new String[numRegistros*2];
		    for(int i=0;i<numRegistros;i++)
		    {
		    	sectionData[contador] = camposParam.get("nombre_"+i).toString();
		    	contador++;
		    	String temp = camposParam.get("info_"+i).toString();
		    	switch(Integer.parseInt(camposParam.get("tipo_"+i).toString()))
		    	{
		    		case ConstantesBD.tipoParametrizablesTexto:
		    		case ConstantesBD.tipoParametrizablesTextArea:
		    		break;
		    		case ConstantesBD.tipoParametrizablesArchivo:
		    			temp = "";
		    		break;
		    		case ConstantesBD.tipoParametrizablesSelect:
		    			temp = UtilidadTexto.getBoolean(temp)?"Sí":"No";
		    		break;
		    	}
		    	sectionData[contador] = temp;
		    	contador++;
		    }
		    
		    report.addSectionData("seccionCamposParamVN", "camposParamVNTable", sectionData);
		    section.setTableCellsColSpan(1);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableBorderColor("camposParamVNTable", 0xFFFFFF);
	    	
		    report.addSectionToDocument("seccionCamposParamVN");
		    //****************************************************
	    }

        /*******************************************SUEÑOS Y HABITOS*******************************************************************************************/
        String suenosHabitos=(String)datosPediatricos.get("suenosHabitos");
        if(!suenosHabitos.equals(""))
        {
        	//Sueños y Hábitos
	        report.document.addParagraph("Sueños y Hábitos: "+suenosHabitos,report.font,iTextBaseDocument.ALIGNMENT_LEFT,10);
        }
        
        //********CAMPOS PARAMETRIZABLES SUEÑO Y HAbitos***************************************************************************
        camposParam = (HashMap)datosPediatricos.get("camposParamSuenoHabitos");
        sectionData = new String[0];
	    numRegistros = 0;
	    
	    if(camposParam!=null&&camposParam.get("numRegistros")!=null)
	    	numRegistros = Integer.parseInt(camposParam.get("numRegistros").toString());
	    
	    if(numRegistros>0)
	    {
	    	
	    	
		    //****************************************************
	    	section = report.createSection("seccionCamposParamS", "camposParamSTable", numRegistros, 6, 0);
	  		report.font.setFontAttributes(0x000000, 10, true, false, false);
	  		section.setTableBorder("camposParamSTable", 0xFFFFFF, 0.0f);
		    section.setTableCellBorderWidth("camposParamSTable", 0.5f);
		    section.setTableCellPadding("camposParamSTable", 1);
		    section.setTableSpaceBetweenCells("camposParamSTable", 0.5f);
		    section.setTableCellsDefaultColors("camposParamSTable", 0xFFFFFF, 0xFFFFFF);
	  		//report.addSectionTitle("antecedentesPediatricos", "revisionSistemasTable", "Revisión por Sistemas");
		    report.font.setFontAttributes(0x000000, 10, false, false, false);
		    section.setTableCellsColSpan(3);
		    
		    int contador = 0;
		    sectionData = new String[numRegistros*2];
		    for(int i=0;i<numRegistros;i++)
		    {
		    	sectionData[contador] = camposParam.get("nombre_"+i).toString();
		    	contador++;
		    	String temp = camposParam.get("info_"+i).toString();
		    	switch(Integer.parseInt(camposParam.get("tipo_"+i).toString()))
		    	{
		    		case ConstantesBD.tipoParametrizablesTexto:
		    		case ConstantesBD.tipoParametrizablesTextArea:
		    		break;
		    		case ConstantesBD.tipoParametrizablesArchivo:
		    			temp = "";
		    		break;
		    		case ConstantesBD.tipoParametrizablesSelect:
		    			temp = UtilidadTexto.getBoolean(temp)?"Sí":"No";
		    		break;
		    	}
		    	sectionData[contador] = temp;
		    	contador++;
		    }
		    
		    report.addSectionData("seccionCamposParamS", "camposParamSTable", sectionData);
		    section.setTableCellsColSpan(1);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableBorderColor("camposParamSTable", 0xFFFFFF);
	    	
		    report.addSectionToDocument("seccionCamposParamS");
		    //****************************************************
	    }
        
        /***********************************FINALIDAD DE LA CONSULTA**********************************************************************************************/
        String finalidadConsulta=(String)datosPediatricos.get("finalidadConsulta");
        if(!finalidadConsulta.equals(""))
        {
            report.document.addParagraph("Finalidad de la Consulta: "+finalidadConsulta, report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,20);
        }
        
        //********CAMPOS PARAMETRIZABLES FINALIDAD CONSULTA***************************************************************************
        camposParam = (HashMap)datosPediatricos.get("camposParamFinalidad");
        sectionData = new String[0];
	    numRegistros = 0;
	    
	    if(camposParam!=null&&camposParam.get("numRegistros")!=null)
	    	numRegistros = Integer.parseInt(camposParam.get("numRegistros").toString());
	    
	    if(numRegistros>0)
	    {
	    	
	    	
		    //****************************************************
	    	section = report.createSection("seccionCamposParamF", "camposParamFTable", numRegistros, 6, 0);
	  		report.font.setFontAttributes(0x000000, 10, true, false, false);
	  		section.setTableBorder("camposParamFTable", 0xFFFFFF, 0.0f);
		    section.setTableCellBorderWidth("camposParamFTable", 0.5f);
		    section.setTableCellPadding("camposParamFTable", 1);
		    section.setTableSpaceBetweenCells("camposParamFTable", 0.5f);
		    section.setTableCellsDefaultColors("camposParamFTable", 0xFFFFFF, 0xFFFFFF);
	  		//report.addSectionTitle("antecedentesPediatricos", "revisionSistemasTable", "Revisión por Sistemas");
		    report.font.setFontAttributes(0x000000, 10, false, false, false);
		    section.setTableCellsColSpan(3);
		    
		    int contador = 0;
		    sectionData = new String[numRegistros*2];
		    for(int i=0;i<numRegistros;i++)
		    {
		    	sectionData[contador] = camposParam.get("nombre_"+i).toString();
		    	contador++;
		    	String temp = camposParam.get("info_"+i).toString();
		    	switch(Integer.parseInt(camposParam.get("tipo_"+i).toString()))
		    	{
		    		case ConstantesBD.tipoParametrizablesTexto:
		    		case ConstantesBD.tipoParametrizablesTextArea:
		    		break;
		    		case ConstantesBD.tipoParametrizablesArchivo:
		    			temp = "";
		    		break;
		    		case ConstantesBD.tipoParametrizablesSelect:
		    			temp = UtilidadTexto.getBoolean(temp)?"Sí":"No";
		    		break;
		    	}
		    	sectionData[contador] = temp;
		    	contador++;
		    }
		    
		    report.addSectionData("seccionCamposParamF", "camposParamFTable", sectionData);
		    section.setTableCellsColSpan(1);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableBorderColor("camposParamFTable", 0xFFFFFF);
	    	
		    report.addSectionToDocument("seccionCamposParamF");
		    //****************************************************
	    }
        
        
        
        /***********************************CAUSA EXTERNA*********************************************************************************************************/
        String causaExterna=(String)datosPediatricos.get("causaExterna");
        if(!causaExterna.equals(""))
        {
            report.document.addParagraph("Causa Externa: "+causaExterna, report.font,iTextBaseDocument.ALIGNMENT_LEFT,20);
        }
        
        //********CAMPOS PARAMETRIZABLES CAUSA EXTERNA***************************************************************************
        camposParam = (HashMap)datosPediatricos.get("camposParamCausaExterna");
        sectionData = new String[0];
	    numRegistros = 0;
	    
	    if(camposParam!=null&&camposParam.get("numRegistros")!=null)
	    	numRegistros = Integer.parseInt(camposParam.get("numRegistros").toString());
	    
	    if(numRegistros>0)
	    {
	    	
	    	
		    //****************************************************
	    	section = report.createSection("seccionCamposParamCE", "camposParamCETable", numRegistros, 6, 0);
	  		report.font.setFontAttributes(0x000000, 10, true, false, false);
	  		section.setTableBorder("camposParamCETable", 0xFFFFFF, 0.0f);
		    section.setTableCellBorderWidth("camposParamCETable", 0.5f);
		    section.setTableCellPadding("camposParamCETable", 1);
		    section.setTableSpaceBetweenCells("camposParamCETable", 0.5f);
		    section.setTableCellsDefaultColors("camposParamCETable", 0xFFFFFF, 0xFFFFFF);
	  		//report.addSectionTitle("antecedentesPediatricos", "revisionSistemasTable", "Revisión por Sistemas");
		    report.font.setFontAttributes(0x000000, 10, false, false, false);
		    section.setTableCellsColSpan(3);
		    
		    int contador = 0;
		    sectionData = new String[numRegistros*2];
		    for(int i=0;i<numRegistros;i++)
		    {
		    	sectionData[contador] = camposParam.get("nombre_"+i).toString();
		    	contador++;
		    	String temp = camposParam.get("info_"+i).toString();
		    	switch(Integer.parseInt(camposParam.get("tipo_"+i).toString()))
		    	{
		    		case ConstantesBD.tipoParametrizablesTexto:
		    		case ConstantesBD.tipoParametrizablesTextArea:
		    		break;
		    		case ConstantesBD.tipoParametrizablesArchivo:
		    			temp = "";
		    		break;
		    		case ConstantesBD.tipoParametrizablesSelect:
		    			temp = UtilidadTexto.getBoolean(temp)?"Sí":"No";
		    		break;
		    	}
		    	sectionData[contador] = temp;
		    	contador++;
		    }
		    
		    report.addSectionData("seccionCamposParamCE", "camposParamCETable", sectionData);
		    section.setTableCellsColSpan(1);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableBorderColor("camposParamCETable", 0xFFFFFF);
	    	
		    report.addSectionToDocument("seccionCamposParamCE");
		    //****************************************************
	    }
    
        /***************************************DIÁGNOSTICOS PRINCIPAL Y RELACIONADOS**********************************************************/
        report.font.setFontSizeAndAttributes(11,false,false,false);
       
        report.createSection("seccionDx","tablaDx",5,3,0);
        report.getSectionReference("seccionDx").setTableBorder("tablaDx", 0xFFFFFF, 0.0f);
        report.getSectionReference("seccionDx").setTableCellBorderWidth("tablaDx", 0.5f);
        report.getSectionReference("seccionDx").setTableCellsDefaultColors("tablaDx", 0xFFFFFF, 0xFFFFFF);
        
        
        String dxPpal=(String)datosPediatricos.get("diagPrinicpal");
        if(!dxPpal.equals(""))
        {
        	report.font.setFontSizeAndAttributes(11,true,false,false);
        	report.getSectionReference("seccionDx").setTableCellsColSpan(1);
            report.getSectionReference("seccionDx").addTableTextCellAligned("tablaDx", "Dx Principal: ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        	report.font.setFontSizeAndAttributes(11,false,false,false);
            report.getSectionReference("seccionDx").setTableCellsColSpan(2);
            report.getSectionReference("seccionDx").addTableTextCellAligned("tablaDx", ""+dxPpal, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);        	
        }
        
        String tipoDx=(String)datosPediatricos.get("tipoDx");
        if(!tipoDx.equals(""))
        {
        	report.font.setFontSizeAndAttributes(11,true,false,false);
        	report.getSectionReference("seccionDx").setTableCellsColSpan(1);
            report.getSectionReference("seccionDx").addTableTextCellAligned("tablaDx", "Tipo Dx Principal: ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        	report.font.setFontSizeAndAttributes(11,false,false,false);
            report.getSectionReference("seccionDx").setTableCellsColSpan(2);
            report.getSectionReference("seccionDx").addTableTextCellAligned("tablaDx", ""+tipoDx, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        }
        
        String dxRelacionado=(String)datosPediatricos.get("diagRelacionado");
        if(!dxRelacionado.equals(""))
        {
        	report.font.setFontSizeAndAttributes(11,true,false,false);
        	report.getSectionReference("seccionDx").setTableCellsColSpan(1);
            report.getSectionReference("seccionDx").addTableTextCellAligned("tablaDx", "Dx Relacionados: ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        	report.font.setFontSizeAndAttributes(11,false,false,false);
            report.getSectionReference("seccionDx").setTableCellsColSpan(2);

            String[] dx=dxRelacionado.split("@@@");
            
            
            int i=1;
            while(i<=(dx.length))
            {
            	
            	report.getSectionReference("seccionDx").addTableTextCellAligned("tablaDx", ""+dx[i], report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            	report.getSectionReference("seccionDx").setTableCellsColSpan(1);
            	report.getSectionReference("seccionDx").addTableTextCellAligned("tablaDx", "", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            	report.getSectionReference("seccionDx").setTableCellsColSpan(2);
            	i++;
            	if(i==dx.length)
            	{
            		break;
            	}
            }
            
        }
        
        
        //adicionar la seccion al reporte
        report.addSectionToDocument("seccionDx");
        
        /********************************SECCIÓN DE COMENTARIOS-CONCEPTO-PLAN-PRONÓSTICO*****************************************************************/
        
        report.font.setFontSizeAndAttributes(11,false,false,false);
        report.createSection("seccionConceptoConsulta","tablaConceptoConsulta",1,1,0);
        report.getSectionReference("seccionConceptoConsulta").setTableBorder("tablaConceptoConsulta", 0xFFFFFF, 0.0f);
        report.getSectionReference("seccionConceptoConsulta").setTableCellBorderWidth("tablaConceptoConsulta", 0.5f);
        report.getSectionReference("seccionConceptoConsulta").setTableCellsDefaultColors("tablaConceptoConsulta", 0xFFFFFF, 0xFFFFFF);
        report.getSectionReference("seccionConceptoConsulta").setTableCellsColSpan(1);
        
        
        String concepto=(String)datosPediatricos.get("concepto");
        if(!concepto.equals(""))
        {
           
        	report.getSectionReference("seccionConceptoConsulta").addTableTextCellAligned("tablaConceptoConsulta", "Concepto de la Consulta: "+concepto, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);        	
        }
        report.addSectionToDocument("seccionConceptoConsulta");
        
        //********CAMPOS PARAMETRIZABLES CONCEPTO CONSULTA***************************************************************************
        camposParam = (HashMap)datosPediatricos.get("camposParamConceptoConsulta");
        sectionData = new String[0];
	    numRegistros = 0;
	    
	    if(camposParam!=null&&camposParam.get("numRegistros")!=null)
	    	numRegistros = Integer.parseInt(camposParam.get("numRegistros").toString());
	    
	    if(numRegistros>0)
	    {
	    	
	    	
		    //****************************************************
	    	section = report.createSection("seccionCamposParamCC", "camposParamCCTable", numRegistros, 6, 0);
	  		report.font.setFontAttributes(0x000000, 10, true, false, false);
	  		section.setTableBorder("camposParamCCTable", 0xFFFFFF, 0.0f);
		    section.setTableCellBorderWidth("camposParamCCTable", 0.5f);
		    section.setTableCellPadding("camposParamCCTable", 1);
		    section.setTableSpaceBetweenCells("camposParamCCTable", 0.5f);
		    section.setTableCellsDefaultColors("camposParamCCTable", 0xFFFFFF, 0xFFFFFF);
	  		//report.addSectionTitle("antecedentesPediatricos", "revisionSistemasTable", "Revisión por Sistemas");
		    report.font.setFontAttributes(0x000000, 10, false, false, false);
		    section.setTableCellsColSpan(3);
		    
		    int contador = 0;
		    sectionData = new String[numRegistros*2];
		    for(int i=0;i<numRegistros;i++)
		    {
		    	sectionData[contador] = camposParam.get("nombre_"+i).toString();
		    	contador++;
		    	String temp = camposParam.get("info_"+i).toString();
		    	switch(Integer.parseInt(camposParam.get("tipo_"+i).toString()))
		    	{
		    		case ConstantesBD.tipoParametrizablesTexto:
		    		case ConstantesBD.tipoParametrizablesTextArea:
		    		break;
		    		case ConstantesBD.tipoParametrizablesArchivo:
		    			temp = "";
		    		break;
		    		case ConstantesBD.tipoParametrizablesSelect:
		    			temp = UtilidadTexto.getBoolean(temp)?"Sí":"No";
		    		break;
		    	}
		    	sectionData[contador] = temp;
		    	contador++;
		    }
		    
		    report.addSectionData("seccionCamposParamCC", "camposParamCCTable", sectionData);
		    section.setTableCellsColSpan(1);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableBorderColor("camposParamCCTable", 0xFFFFFF);
	    	
		    report.addSectionToDocument("seccionCamposParamCC");
		    //****************************************************
	    }
        
        report.font.setFontSizeAndAttributes(11,false,false,false);
        report.createSection("seccionFinal","tablaFinal",3,1,0);
        report.getSectionReference("seccionFinal").setTableBorder("tablaFinal", 0xFFFFFF, 0.0f);
        report.getSectionReference("seccionFinal").setTableCellBorderWidth("tablaFinal", 0.5f);
        report.getSectionReference("seccionFinal").setTableCellsDefaultColors("tablaFinal", 0xFFFFFF, 0xFFFFFF);
        report.getSectionReference("seccionFinal").setTableCellsColSpan(1);
        
        String plan=(String)datosPediatricos.get("plan");
        if(!plan.equals(""))
        {
            report.getSectionReference("seccionFinal").addTableTextCellAligned("tablaFinal", "Plan Diagnóstico y Terapeútico: "+plan, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);        	
        }

        String comentario=(String)datosPediatricos.get("comentarios");
        if(!comentario.equals(""))
        {
            report.getSectionReference("seccionFinal").addTableTextCellAligned("tablaFinal", "Comentarios Generales: "+comentario, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);        	
        }

        String pronostico=(String)datosPediatricos.get("pronostico");
        if(!pronostico.equals(""))
        {
            report.getSectionReference("seccionFinal").addTableTextCellAligned("tablaFinal", "Deberes y/o derechos del paciente: "+pronostico, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);        	
        }
        //adicionar la seccion al reporte
        report.addSectionToDocument("seccionFinal");
        
        //********CAMPOS PARAMETRIZABLES OBSERVACIONES***************************************************************************
        camposParam = (HashMap)datosPediatricos.get("camposParamObservaciones");
        sectionData = new String[0];
	    numRegistros = 0;
	    
	    if(camposParam!=null&&camposParam.get("numRegistros")!=null)
	    	numRegistros = Integer.parseInt(camposParam.get("numRegistros").toString());
	    
	    if(numRegistros>0)
	    {
	    	
	    	
		    //****************************************************
	    	section = report.createSection("seccionCamposParamO", "camposParamOTable", numRegistros, 6, 0);
	  		report.font.setFontAttributes(0x000000, 10, true, false, false);
	  		section.setTableBorder("camposParamOTable", 0xFFFFFF, 0.0f);
		    section.setTableCellBorderWidth("camposParamOTable", 0.5f);
		    section.setTableCellPadding("camposParamOTable", 1);
		    section.setTableSpaceBetweenCells("camposParamOTable", 0.5f);
		    section.setTableCellsDefaultColors("camposParamOTable", 0xFFFFFF, 0xFFFFFF);
	  		//report.addSectionTitle("antecedentesPediatricos", "revisionSistemasTable", "Revisión por Sistemas");
		    report.font.setFontAttributes(0x000000, 10, false, false, false);
		    section.setTableCellsColSpan(3);
		    
		    int contador = 0;
		    sectionData = new String[numRegistros*2];
		    for(int i=0;i<numRegistros;i++)
		    {
		    	sectionData[contador] = camposParam.get("nombre_"+i).toString();
		    	contador++;
		    	String temp = camposParam.get("info_"+i).toString();
		    	switch(Integer.parseInt(camposParam.get("tipo_"+i).toString()))
		    	{
		    		case ConstantesBD.tipoParametrizablesTexto:
		    		case ConstantesBD.tipoParametrizablesTextArea:
		    		break;
		    		case ConstantesBD.tipoParametrizablesArchivo:
		    			temp = "";
		    		break;
		    		case ConstantesBD.tipoParametrizablesSelect:
		    			temp = UtilidadTexto.getBoolean(temp)?"Sí":"No";
		    		break;
		    	}
		    	sectionData[contador] = temp;
		    	contador++;
		    }
		    
		    report.addSectionData("seccionCamposParamO", "camposParamOTable", sectionData);
		    section.setTableCellsColSpan(1);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableBorderColor("camposParamOTable", 0xFFFFFF);
	    	
		    report.addSectionToDocument("seccionCamposParamO");
		    //****************************************************
	    }
        
        
        /****************************************MEDICO RESPONSABLE************************************************************************************************/
        //Informacion del medico responsable
       
        report.font.setFontSizeAndAttributes(11,true,false,false);
        report.document.addParagraph("Profesional Responsable", report.font,iTextBaseDocument.ALIGNMENT_LEFT,20);
        report.font.setFontSizeAndAttributes(11,false,false,false);
        report.document.addParagraph(""+(String)datosPediatricos.get("medico"), report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);        	

        //cerrando el reporte
        report.closeReport(); 
        
        
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
    
}
