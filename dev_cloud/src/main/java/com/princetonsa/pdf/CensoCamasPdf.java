package com.princetonsa.pdf;

import java.sql.Connection;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import util.Listado;
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseTable;

import com.lowagie.text.BadElementException;
import com.princetonsa.actionform.manejoPaciente.CensoCamasForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;



public class CensoCamasPdf
{
	/**
     * Clase para manejar los logs de esta clase
     */
    private static Logger logger = Logger.getLogger(CensoCamasPdf.class);
    
    
    public static void imprimir (Connection connection,String nombrePdf, CensoCamasForm forma,UsuarioBasico usuario, HttpServletRequest request)
    {
    	PdfReports report = new PdfReports("false",true);
		int numRegistros = Integer.parseInt(forma.getCensoCamasMap("numRegistros").toString());
		Vector cambios = new Vector ();
		cambios=Listado.detectarCambioRompimiento(forma.getCensoCamasMap(), numRegistros, forma.getTipoRompimiento()+"_");
	
		numRegistros=numRegistros+cambios.size();
	    //logger.info("el valor de hashmap "+forma.getCensoCamasMap());  
		//GENERACION DE CABECERA
		//Ruta por defecto donde se genera los PDF
		String filePath=ValoresPorDefecto.getFilePath();
		
		//*******SE CARGAN LOS DATOS DE LA INSTITUCION*******************************+
		InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
        //*********************************************************************************
		
		String tituloDocumentos="";
		if (forma.getTipoReporte().toString().equals("resumido"))
			tituloDocumentos="CENSO DE CAMAS RESUMIDO  "+forma.getCensoCamasMap("fechaconsulta").toString()+" "+forma.getCensoCamasMap("horaconsulta");
		else 
			tituloDocumentos="CENSO DE CAMAS DETALLADO  "+forma.getCensoCamasMap("fechaconsulta").toString()+" "+forma.getCensoCamasMap("horaconsulta");
		
		
		String criterios ="";
		criterios=cargarCriteriosMostrar(connection, forma, usuario,true);
		
		report.setReportBaseHeaderCensoCamas(institucionBasica, tituloDocumentos,criterios);
		

		
		//abrir reporte
        report.openReport(nombrePdf);
        
        
        iTextBaseTable section;
        //aqui se crea la seccion donde se va a mostrar los datos del reporte
        if (forma.getTipoReporte().toString().equals("resumido"))
        {
        	int numReg=0,init=0,cant=0;
    		//logger.info("\n\n el valor de cambios es "+cambios.size());
    		//logger.info("el mapa es "+forma.getCensoCamasMap());
        	for (int k=0;k<cambios.size();k++)
        	{	
	        	//	logger.info("\n el valor de cambios en k "+k+" ==> "+cambios.get(k));
	        		if (numReg==0)
	        		{
	        			cant=Integer.parseInt(cambios.get(k)+"");
	        			numReg=(cant+1);
	        		}
	        			
	        		else
	        		{
	        			init=numReg;
	        			cant=Integer.parseInt(cambios.get(k)+"")-Integer.parseInt(cambios.get(k-1)+"");
	        			numReg=numReg+cant;
	        		}
	        	    
	        	//estos son los atributos de la seccion
	        	section = report.createSection("descripcion"+numReg, "descripcionTable",  cant+2,9 ,10);
		        section.setTableBorder("descripcionTable", 0xFFFFFF, 0.0f);
			    section.setTableCellBorderWidth("descripcionTable", 0.1f);
			    section.setTableCellPadding("descripcionTable", 0.3f);
			    section.setTableSpaceBetweenCells("descripcionTable", 0.0f);
			    section.setTableCellsDefaultColors("descripcionTable", 0xFFFFFF, 0xFFFFFF);
			    
			    /******************CUANDO EL ROMPIMIENTO ES POR PISO ************************/   			    
			    if (forma.getTipoRompimiento().equals("codigopiso"))
			    {
				    float widths[]={0.92f,1.3f,0.92f,1f,0.8f,0.4f,0.8f,2.5f,1.36f};
				    try 
				    {
				    	section.getTableReference("descripcionTable").setWidths(widths);
					}
				    catch (BadElementException e) {
						logger.info("\n Se presentó error generando el pdf del censo resumido de camas con rompimiento codigopiso "+e);
					}
			    }
			    else/******************CUANDO EL ROMPIMIENTO ES POR EL ESTADO DE LA CAMA************************/
			    	 if (forma.getTipoRompimiento().equals("estadocama"))
			    	 {
			    		 	
			    		 	float widths[]={0.7f,0.7f,1.3f,0.92f,0.82f,0.4f,0.9f,2.4f,1.36f};
						    try 
						    {
						    	section.getTableReference("descripcionTable").setWidths(widths);
							}
						    catch (BadElementException e) {
								logger.info("\n Se presentó error generando el pdf del censo resumido de camas con rompimiento estadocama "+e);
							}
			    		 
			    	 }
			    	 else/******************CUANDO EL ROMPIMIENTO ES POR CENTRO DE COSTO************************/
			    		 if (forma.getTipoRompimiento().equals("codigocentrocosto"))
			    		 {
			    			
			    			 float widths[]={0.9f,0.92f,1.3f,0.92f,1f,0.96f,0.5f,1f,2.6f};
							    try 
							    {
							    	section.getTableReference("descripcionTable").setWidths(widths);
								}
							    catch (BadElementException e) {
									logger.info("\n Se presentó error generando el pdf del censo resumido de camas con rompimiento codigocentrocosto "+e);
								}
							    
			    		 }
			    report.font.setFontAttributes(0x000000, 7, true, false, false);
			    //se carga el encabezado de la seccion.
			    
				   //se valida para saber si el informe lleva este campo o no
			    section.setTableCellsColSpan(9);
			    section.addTableTextCellAligned("descripcionTable", armadorSubtitulo(forma, init), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			  //se valida para saber si el informe lleva este campo o no
			    section.setTableCellsColSpan(1);
			    if (!forma.getTipoRompimiento().equals("codigopiso"))
			    section.addTableTextCellAligned("descripcionTable", "PISO", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    section.addTableTextCellAligned("descripcionTable", "HABITACIÓN", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    section.addTableTextCellAligned("descripcionTable", "TIPO HABITACIÓN", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    section.addTableTextCellAligned("descripcionTable", "CAMA", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			   //se valida para saber si el informe lleva este campo o no
			    if (!forma.getTipoRompimiento().equals("estadocama"))
			    	section.addTableTextCellAligned("descripcionTable", "ESTADO CAMA", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    section.addTableTextCellAligned("descripcionTable", "TIPO USUARIO", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    section.addTableTextCellAligned("descripcionTable", "SEXO", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    section.addTableTextCellAligned("descripcionTable", "RESTRICCIÓN", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    section.addTableTextCellAligned("descripcionTable", "CONVENIO", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    //se valida para saber si el informe lleva este campo o no
			    if (!forma.getTipoRompimiento().equals("codigocentrocosto"))
			    	 section.addTableTextCellAligned("descripcionTable", "CENTRO COSTO", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    	
			    report.font.setFontAttributes(0x000000, 7, false, false, false);
			    for (int i=init;i<numReg;i++)
			    {		
			    		//se valida para saber si el informe lleva este campo o no
			    	if (forma.getCensoCamasMap("cama_"+i)!=null)
			    	{
			    		if (!forma.getTipoRompimiento().equals("codigopiso"))
			    			if (forma.getCensoCamasMap("nombrepiso_"+i).toString().length()>12)
			    				section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("nombrepiso_"+i).toString().toLowerCase().substring(0,12), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    			else
			    				section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("nombrepiso_"+i).toString().toLowerCase(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    		
			    		section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("nombrehabitacion_"+i).toString().toLowerCase(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
					    section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("nombretipohabitacion_"+i).toString().toLowerCase(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
					    section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("cama_"+i).toString().toLowerCase(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
					  //se valida para saber si el informe lleva este campo o no
					    if (!forma.getTipoRompimiento().equals("estadocama"))
					    	section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("nombreestadocama_"+i).toString().toLowerCase(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
					    section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("tipousuariocama_"+i).toString().toLowerCase(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
					    section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("sexocama_"+i).toString().toLowerCase(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
					    section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("restriccioncama_"+i).toString(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
					
					    	if (forma.getCensoCamasMap("convenio_"+i).toString().length()>40)
					    		section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("convenio_"+i).toString().toLowerCase().substring(0, 40), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
					    	else
					    		section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("convenio_"+i).toString().toLowerCase(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
					    
					    //se valida para saber si el informe lleva este campo o no
					    if (!forma.getTipoRompimiento().equals("codigocentrocosto"))
					    {
					    	if (forma.getCensoCamasMap("nombrecentrocosto_"+i).toString().length()>33)
					    		section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("nombrecentrocosto_"+i).toString().toLowerCase().substring(0,33), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
					    	else
					    	section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("nombrecentrocosto_"+i).toString().toLowerCase(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
					    } 
			    	}
			    }
//			  se añade la sección al documento
	    	    report.addSectionToDocument("descripcion"+numReg);
		    
        	}
        }
        else
        	if (forma.getTipoReporte().toString().equals("detallado"))
        	{
        		int numReg=0,init=0,cant=0;
        		//logger.info("\n\n el valor de cambios es "+cambios.size());
        		//logger.info("el mapa es "+forma.getCensoCamasMap());
	        	for (int k=0;k<cambios.size();k++)
	        	{	
	        		//logger.info("\n el valor de cambios en k "+k+" ==> "+cambios.get(k));
	        		if (numReg==0)
	        		{
	        			cant=Integer.parseInt(cambios.get(k)+"");
	        			numReg=(cant+1);
	        		}
	        			
	        		else
	        		{
	        			init=numReg;
	        			cant=Integer.parseInt(cambios.get(k)+"")-Integer.parseInt(cambios.get(k-1)+"");
	        			numReg=numReg+cant;
	        		}
	        		
	        		
	        		section = report.createSection("descripcion"+numReg, "descripcionTable",  cant+2, 13, 10);
			        section.setTableBorder("descripcionTable", 0xFFFFFF, 0.0f);
				    section.setTableCellBorderWidth("descripcionTable", 0.1f);
				    section.setTableCellPadding("descripcionTable", 0.3f);
				    section.setTableSpaceBetweenCells("descripcionTable", 0.0f);
				    section.setTableCellsDefaultColors("descripcionTable", 0xFFFFFF, 0xFFFFFF);
				    
				    
				    if (forma.getTipoRompimiento().equals("codigopiso"))
				    {
				    	float widths[]={1.4f, 0.8f, 1.15f,0.8f,1.15f,0.4f,0.4f,0.75f,0.2f,0.77f,0.3f,1.3f,0.58f};
					    try 
					    {
					    	section.getTableReference("descripcionTable").setWidths(widths);
						}
					    catch (BadElementException e) 
					    {
							logger.info("\n Se presentó error generando el pdf del censo detallado de camas con rompimiento codigopiso "+e);
						}
				    }
				    else
				    	 if (forma.getTipoRompimiento().equals("estadocama"))
				    	 {
				    		 	float widths[]={0.4f, 1.4f, 1.15f,0.8f,1.15f,0.4f,0.4f,0.75f,0.2f,0.77f,0.3f,1.3f,0.8f};
							    try 
							    {
							    	section.getTableReference("descripcionTable").setWidths(widths);
								}
							    catch (BadElementException e) 
							    {
									logger.info("\n Se presentó error generando el pdf del censo detallado de camas con rompimiento estadocama "+e);
								}
				    	 }
				    	 else
				    		 if (forma.getTipoRompimiento().equals("codigocentrocosto"))
				    		 {
				    			 	float widths[]={0.4f, 1.4f, 0.8f, 1.15f,0.8f,1.15f,0.4f,0.4f,0.75f,0.2f,0.77f,0.3f,1.3f};
								    try 
								    {
								    	section.getTableReference("descripcionTable").setWidths(widths);
									}
								    catch (BadElementException e) 
								    {
										logger.info("\n Se presentó error generando el pdf del censo detallado de camas con rompimiento codigocentrocosto "+e);
									}
								    
				    		 }
				    report.font.setFontAttributes(0x000000, 7, true, false, false);
				    section.setTableCellsColSpan(13);
				    section.addTableTextCellAligned("descripcionTable", armadorSubtitulo(forma, init), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
				  //se valida para saber si el informe lleva este campo o no
				    section.setTableCellsColSpan(1);
				    if (!forma.getTipoRompimiento().equals("codigopiso"))
				    	section.addTableTextCellAligned("descripcionTable", "PISO", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    section.addTableTextCellAligned("descripcionTable", "CAMA", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    
				  //se valida para saber si el informe lleva este campo o no
				    if (!forma.getTipoRompimiento().equals("estadocama"))
				    	section.addTableTextCellAligned("descripcionTable", "ESTADO CAMA", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    section.addTableTextCellAligned("descripcionTable", "PACIENTE", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    section.addTableTextCellAligned("descripcionTable", "ID", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    section.addTableTextCellAligned("descripcionTable", "DIAGNOSTICO", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    section.addTableTextCellAligned("descripcionTable", "ING", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    section.addTableTextCellAligned("descripcionTable", "H.C", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    section.addTableTextCellAligned("descripcionTable", "EDAD", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    section.addTableTextCellAligned("descripcionTable", "S", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    section.addTableTextCellAligned("descripcionTable", "FECHA ING", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    section.addTableTextCellAligned("descripcionTable", "D.EST", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    section.addTableTextCellAligned("descripcionTable", "CONVENIO", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    //se valida para saber si el informe lleva este campo o no
				    if (!forma.getTipoRompimiento().equals("codigocentrocosto"))
				    	 section.addTableTextCellAligned("descripcionTable", "C.CTO", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    	
				    report.font.setFontAttributes(0x000000, 7, false, false, false);
				  
				    for (int i=init;i<numReg;i++)
				    {
				    	//logger.info("\n\n ****el valor de i es ==> "+i);
				    	//se cargan todos los datos del reporte detallado.
		//		    	se valida para saber si el informe lleva este campo o no
				    	if (forma.getCensoCamasMap("cama_"+i)!=null)
				    	{
				    		if (!forma.getTipoRompimiento().equals("codigopiso"))
				    			if (forma.getCensoCamasMap("nombrepiso_"+i).toString().length()>8)
				    				section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("nombrepiso_"+i).toString().toLowerCase().substring(0,8), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
				    			else
				    				section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("nombrepiso_"+i).toString().toLowerCase(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
				    		
				    		section.addTableTextCellAligned("descripcionTable", "C."+forma.getCensoCamasMap("cama_"+i).toString().toLowerCase()+" H."+forma.getCensoCamasMap("codhabitmostar_"+i).toString().toLowerCase(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
						    
							  //se valida para saber si el informe lleva este campo o no
						    if (!forma.getTipoRompimiento().equals("estadocama"))
						    {
						    	 if (forma.getCensoCamasMap("nombreestadocama_"+i).toString().length()>17)
						    		 section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("nombreestadocama_"+i).toString().toLowerCase().substring(0, 17), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
						    	 else
						    		 section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("nombreestadocama_"+i).toString().toLowerCase(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
						    }
						    
						    if (forma.getCensoCamasMap("nombrepac_"+i).toString().length()>24)
						    	section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("nombrepac_"+i).toString().toLowerCase().substring(0,24), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
						    else
						    	section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("nombrepac_"+i).toString().toLowerCase(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
						   
						    section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("tipoidentificacionpac_"+i).toString().toLowerCase()+" "+forma.getCensoCamasMap("identificacionpac_"+i).toString(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
						  
						    if (!UtilidadTexto.isEmpty(forma.getCensoCamasMap("diagnostico_"+i)+"")&&forma.getCensoCamasMap("diagnostico_"+i).toString().length()>25)
							    section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("diagnostico_"+i).toString().substring(0,25).toLowerCase(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
						    else
					    		section.addTableTextCellAligned("descripcionTable", "", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
						    
						    section.addTableTextCellAligned("descripcionTable",forma.getCensoCamasMap("consecutivoingreso_"+i).toString().toLowerCase(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
						    section.addTableTextCellAligned("descripcionTable",forma.getCensoCamasMap("historiaclinica_"+i).toString().toLowerCase(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
						    section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("edadpac_"+i).toString().toLowerCase(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
						    if (!forma.getCensoCamasMap("nombresexopac_"+i).toString().equals(""))
						    	section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("nombresexopac_"+i).toString().substring(0,1), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
						    else
						    	section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("nombresexopac_"+i).toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
						    if (forma.getCensoCamasMap("hora_"+i).toString().length()>5)
						    	section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("fecha_"+i).toString()+" "+forma.getCensoCamasMap("hora_"+i).toString().substring(0, 5), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
						    else
						    	section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("fecha_"+i).toString()+" "+forma.getCensoCamasMap("hora_"+i).toString(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
						    section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("diasestancia_"+i).toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
						    if (forma.getCensoCamasMap("convenio_"+i).toString().length()>36)
						    	section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("convenio_"+i).toString().toLowerCase().substring(0,36), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
						    else
						    	section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("convenio_"+i).toString().toLowerCase(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
						    //se valida para saber si el informe lleva este campo o no
						    if (!forma.getTipoRompimiento().equals("codigocentrocosto"))
						    {
						    	if (forma.getCensoCamasMap("nombrecentrocosto_"+i).toString().length()>13)
						    		section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("nombrecentrocosto_"+i).toString().toLowerCase().substring(0,13), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
						    	else
						    		section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("nombrecentrocosto_"+i).toString().toLowerCase(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
						    }
						}
				    }
				    //se añade la sección al documento
		    	    report.addSectionToDocument("descripcion"+numReg);
		   
	        	}
	        	
		   
        	}
      
        //validacion por tarea 64276
	    if (UtilidadTexto.getBoolean(forma.getMostrarResumen()))
	    {
		    /*-----------------------------------------------------------------------------------------
		     * 				SE CREA LA SECCION PARA EL RESUMEN DEL CENSO DE CAMAS
		     -----------------------------------------------------------------------------------------*/
	        section = report.createSection("resumen", "descripcionTable",  9, 3, 20);
	        section.setTableBorder("descripcionTable", 0xFFFFFF, 0.0f);
		    section.setTableCellBorderWidth("descripcionTable", 0.5f);
		    section.setTableCellPadding("descripcionTable", 1);
		    section.setTableSpaceBetweenCells("descripcionTable", 0.5f);
		    section.setTableCellsDefaultColors("descripcionTable", 0xFFFFFF, 0x000000);
		    
		    report.font.setFontAttributes(0x000000, 7, true, false, false);
		    section.setTableCellsColSpan(3);
		    section.addTableTextCellAligned("descripcionTable", "RESUMEN CENSO DE CAMAS (CANTIDADES / PORCENTAJES)", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    section.setTableCellsColSpan(1);
		    section.addTableTextCellAligned("descripcionTable", "ESTADOS", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("descripcionTable", "CANTIDAD", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("descripcionTable", "PORCENTAJE", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    //filas de las Disponibles
		    section.addTableTextCellAligned("descripcionTable", "Disponibles", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    report.font.setFontAttributes(0x000000, 7, false, false, false);
		    section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("disponible").toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("disponibleporcent").toString(), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
		    
		    
		    //fila de las las ocupadas
		    report.font.setFontAttributes(0x000000, 7, true, false, false);
		    section.addTableTextCellAligned("descripcionTable", "Ocupadas", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    report.font.setFontAttributes(0x000000, 7, false, false, false);
		    section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("ocupada").toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("ocupadaporcent").toString(), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
		    
		    //fila de desinfeccion
		    report.font.setFontAttributes(0x000000, 7, true, false, false);
		    section.addTableTextCellAligned("descripcionTable", "Desinfección", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    report.font.setFontAttributes(0x000000, 7, false, false, false);
		    section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("desinfeccion").toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("desinfeccionporcent").toString(), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
		    
		  //fila de Mantenimiento
		    report.font.setFontAttributes(0x000000, 7, true, false, false);
		    section.addTableTextCellAligned("descripcionTable", "Mantenimiento", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    report.font.setFontAttributes(0x000000, 7, false, false, false);
		    section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("mantenimiento").toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("mantenimientoporcent").toString(), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
		    
		  	//fila de Fuera de servicio
		    report.font.setFontAttributes(0x000000, 7, true, false, false);
		    section.addTableTextCellAligned("descripcionTable", "Fuera de Servicio", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    report.font.setFontAttributes(0x000000, 7, false, false, false);
		    section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("fueradeservicio").toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("fueradeservicioporcent").toString(), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
		    
	 	  	//fila de Reservadas
		    report.font.setFontAttributes(0x000000, 7, true, false, false);
		    section.addTableTextCellAligned("descripcionTable", "Reservadas", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    report.font.setFontAttributes(0x000000, 7, false, false, false);
		    section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("reservada").toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("reservadaporcent").toString(), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
		    
		    //fila de Pendiente por Trasladar
		    report.font.setFontAttributes(0x000000, 7, true, false, false);
		    section.addTableTextCellAligned("descripcionTable", "Pendiente por Trasladar", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    report.font.setFontAttributes(0x000000, 7, false, false, false);
		    section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("trasladar").toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("trasladarporcent").toString(), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
		    
		    //fila de Pendiente por Remitir
		    report.font.setFontAttributes(0x000000, 7, true, false, false);
		    section.addTableTextCellAligned("descripcionTable", "Pendiente por Remitir", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    report.font.setFontAttributes(0x000000, 7, false, false, false);
		    section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("remitir").toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("remitirporcent").toString(), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
		    
		    //fila de Con Salida
		    report.font.setFontAttributes(0x000000, 7, true, false, false);
		    section.addTableTextCellAligned("descripcionTable", "Con Salida", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    report.font.setFontAttributes(0x000000, 7, false, false, false);
		    section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("consalida").toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("consalidaporcent").toString(), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
		    
		    
		    //fila de Total
		    report.font.setFontAttributes(0x000000, 7, true, false, false);
		    section.addTableTextCellAligned("descripcionTable", "Total", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    report.font.setFontAttributes(0x000000, 7, false, false, false);
		    section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("total").toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("descripcionTable", forma.getCensoCamasMap("totalporcent").toString(), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
		    
		    /*-----------------------------------------------------------------------------------------
		     * 				FIN DE LA SECCION PARA EL RESUMEN DEL CENSO DE CAMAS
		     -----------------------------------------------------------------------------------------*/
	        
		    //se añade la sección al documento
		    report.addSectionToDocument("resumen");
	    }
	    
	    report.closeReport();
    }
    
    /**
     *Metodo encargado de armar el subtitulo del reporte. 
     * @param forma
     * @param index
     * @return
     */
    public static String armadorSubtitulo (CensoCamasForm forma,int index)
    {
    	String subtitulo="";
    	if (forma.getTipoRompimiento().toString().equals("codigopiso"))
			subtitulo="PISO "+forma.getCensoCamasMap("nombrepiso_"+index).toString().toUpperCase();
		else
			if (forma.getTipoRompimiento().toString().equals("codigocentrocosto"))
				subtitulo="CENTRO DE COSTO "+forma.getCensoCamasMap("nombrecentrocosto_"+index).toString().toUpperCase();
			else
				if (forma.getTipoRompimiento().toString().equals("estadocama"))
					subtitulo="ESTADO "+forma.getCensoCamasMap("nombreestadocama_"+index).toString().toUpperCase();
    	return subtitulo;
    }
    
    public static String cargarCriteriosMostrar (Connection connection,CensoCamasForm forma,UsuarioBasico usuario,boolean mostrarRompi)
    {
    	String criterios = "";
    	    	
    	if (UtilidadCadena.noEsVacio(forma.getConvenioId()))
    		criterios="Convenio: "+Utilidades.obtenerNombreConvenioOriginal(connection, Utilidades.convertirAEntero(forma.getConvenioId()));
    	
    	if (UtilidadCadena.noEsVacio(forma.getCentroAtencionId()))
    		criterios+="  Centro Atención: "+Utilidades.obtenerNombreCentroAtencion(connection, Utilidades.convertirAEntero(forma.getCentroAtencionId()));
    	
    	if (UtilidadCadena.noEsVacio(forma.getCentroCostoId()))
    		criterios+="  Centro Costo: "+Utilidades.obtenerNombreCentroCosto(Utilidades.convertirAEntero(forma.getCentroCostoId()), usuario.getCodigoInstitucionInt());    	
    	
    	if (UtilidadCadena.noEsVacio(forma.getPisoId()))
    		criterios+="  Piso: "+ Utilidades.obtenerNombrePiso(connection, forma.getPisoId());    	
    	
    	String estados = "";
		int numReg = forma.getEstadoCamaId().length;
		if (numReg==1)
		{
			if ((forma.getEstadoCamaId()[0]).equals(""))
				criterios+="  Estado: Todos";
			else
				criterios+="  Estado: "+Utilidades.obtenerNombreEstadoCama(connection,forma.getEstadoCamaId()[0]);
		}
		else
		{
			if (numReg>1)
				for (int i=0;i<numReg;i++)
					if (i==0)
						estados=Utilidades.obtenerNombreEstadoCama(connection,forma.getEstadoCamaId()[i]);
					else
						estados+=", "+Utilidades.obtenerNombreEstadoCama(connection,forma.getEstadoCamaId()[i]);
			
			criterios+="  Estados: "+estados;
			
			logger.info("\n los estados son :"+estados);
		}
    	
    	if (UtilidadCadena.noEsVacio(forma.getTipoReporte()))
    		criterios+="  Tipo Reporte: "+ forma.getTipoReporte();    	
    	
    	if(mostrarRompi)
    	{
	    	if (UtilidadCadena.noEsVacio(forma.getTipoRompimiento()))
	    		if (forma.getTipoRompimiento().equals("codigocentrocosto"))
	    			criterios+="  Tipo Rompimiento: "+ "Centro Costo";    	
	    		else
	    			if (forma.getTipoRompimiento().equals("codigopiso"))
	        			criterios+="  Tipo Rompimiento: "+ "Piso";    	
	    			else
	    				if (forma.getTipoRompimiento().equals("estadocama"))
	            			criterios+="  Tipo Rompimiento: "+ "Estado";    	
    	}
    	
    	
    	return criterios;
    }
    
}