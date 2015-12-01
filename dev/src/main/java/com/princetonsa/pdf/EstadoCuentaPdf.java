/*
 * @(#)EstadoCuentaPdf.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.pdf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import java.util.HashMap;
import org.apache.log4j.Logger;
import java.util.HashMap;
import util.ConstantesBD;
import util.UtilidadTexto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;

import com.lowagie.text.BadElementException;
import com.princetonsa.actionform.resumenAtenciones.EstadoCuentaForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * Clase para manejar la generación de PDF's para
 * el estado de cuenta
 * 
 *	@version 1.0, 13/08/2004
 */
public class EstadoCuentaPdf
{
	

	/**
     * Clase para manejar los logs de esta clase
     */
    private static Logger estadoCuentaPdfLogger=Logger.getLogger(EstadoCuentaPdf.class);
    
    private static int indicador=0;
	/**
	 * Método que ayuda a generar el PDF del listado
	 * de solicitudes
	 * 
	 * @param estadoCuentaForm Form de estadoCuentaForm
	 * @return
	 */
	public static String[] comunListadoSolicitud(EstadoCuentaForm estadoCuentaForm)
	{
		String[] dataListadoSolicitud;
		HashMap filaHashMap;
		int i=0;
		ArrayList resultados=new ArrayList();
		
		if (estadoCuentaForm.getSolicitudesCuenta().size()==0)
		{
		    return new String[0];
		}
		//Iterator it=estadoCuentaForm.getSolicitudesCuenta().iterator();
		Iterator it=estadoCuentaForm.getImpresionSolicitudesCuenta().iterator();
		while (it.hasNext())
		{
		    filaHashMap=(HashMap)it.next();

			resultados.add(i,filaHashMap.get("fechacargo"));
			resultados.add(i+1,filaHashMap.get("servicio"));
			resultados.add(i+2,filaHashMap.get("descripcion"));
			resultados.add(i+3,filaHashMap.get("cantidad"));
			
			resultados.add(i+4,UtilidadTexto.formatearValores(filaHashMap.get("valor").toString()));    
			resultados.add(i+5,UtilidadTexto.formatearValores(filaHashMap.get("recargo").toString()));
			i=i+6;
		}
		dataListadoSolicitud=new String[resultados.size()];
		UtilidadTexto.copiarArrayListAArreglo(resultados, dataListadoSolicitud);
		return dataListadoSolicitud;
	}

	/**
	 * Método que almacena toda la funcionalidad 
	 * necesaria para generar la parte común a las
	 * impresiones con respecto a la cuenta
	 * 
	 * @param detalleCuentaHashMap
	 * @param vaConNaturaleza
	 * @return
	 */
	public static String[] comunDetalleCuenta (HashMap detalleCuentaHashMap, boolean vaConNaturaleza,EstadoCuentaForm estadoCuentaForm)
	{
		ArrayList resultadosDetalleCuenta=new ArrayList();
		int i=0;
		String convenio;
		Iterator it=estadoCuentaForm.getCabezoteImpresion().iterator();
		while (it.hasNext())
		{
			detalleCuentaHashMap=(HashMap)it.next();
			if (vaConNaturaleza)
			{
			    resultadosDetalleCuenta.add(i, detalleCuentaHashMap.get("naturaleza"));
			    indicador=1;
			}
			
			if(detalleCuentaHashMap.get("valormonto")!=null)
			{
//				resultadosDetalleCuenta.add(i, ":  $"+UtilidadTexto.formatearValores(detalleCuentaHashMap.get("valormonto")+"",2,true,true));
				resultadosDetalleCuenta.add(i, ":   "+UtilidadTexto.formatearValores(detalleCuentaHashMap.get("valormonto").toString()));
			}
			else if(!detalleCuentaHashMap.get("porcentajemonto").equals(""))
			{
				resultadosDetalleCuenta.add(i, ":  %"+UtilidadTexto.formatearValores(detalleCuentaHashMap.get("porcentajemonto").toString()));
			}
			//resultadosDetalleCuenta.add(i, ":"+UtilidadTexto.formatearValores(detalleCuentaHashMap.get("valormonto")+"",2,true,true));
			resultadosDetalleCuenta.add(i, detalleCuentaHashMap.get("tipomonto"));
			resultadosDetalleCuenta.add(i, detalleCuentaHashMap.get("estrato"));
			convenio=(String)detalleCuentaHashMap.get("convenio");
			if(convenio.equals("Particular"))
			{
				resultadosDetalleCuenta.add(i, detalleCuentaHashMap.get("responsable"));
			}
			else
			{
				resultadosDetalleCuenta.add(i, detalleCuentaHashMap.get("convenio"));
			}
			resultadosDetalleCuenta.add(i, (detalleCuentaHashMap.get("numerocuenta")+"-"+detalleCuentaHashMap.get("estadofacturacion")));
			resultadosDetalleCuenta.add(i, detalleCuentaHashMap.get("fecha"));
			resultadosDetalleCuenta.add(i, detalleCuentaHashMap.get("viaingreso"));
			
			resultadosDetalleCuenta.add(i,(detalleCuentaHashMap.get("tipoid")+" "+detalleCuentaHashMap.get("numeroid")));
			resultadosDetalleCuenta.add(i, (detalleCuentaHashMap.get("primernombre")+" "+detalleCuentaHashMap.get("segundonombre")+" "+detalleCuentaHashMap.get("primerapellido")+" "+detalleCuentaHashMap.get("segundoapellido")));
			break;
		}
		
		
		String dataDetalleCuenta []= new String[resultadosDetalleCuenta.size()];
			
		UtilidadTexto.copiarArrayListAArreglo(resultadosDetalleCuenta, dataDetalleCuenta);
		return dataDetalleCuenta;
	}
	
	/**
	 * Método que genera el Pdf de detalle Cuenta
	 * 
	 * @param filename Nombre del archivo con el que
	 * se desea generar el PDF
	 * @param estadoCuentaForm Form del que se sacan
	 * las colecciones
	 * @param medico
	 */
	public static void pdfDetalleCuenta(String filename, EstadoCuentaForm estadoCuentaForm, UsuarioBasico medico, PersonaBasica pacienteActivo, HashMap datosTotales)
	{
		PdfReports report = new PdfReports();
		String[] dataDetalleCuenta;
		//String[] headerDetalleCuenta ;
		HashMap detalleCuentaHashMap;
		Iterator it;
		if (estadoCuentaForm.getDetalleCuenta().size()==0)
		{
		    return;
		}
		else
		{
		    it=estadoCuentaForm.getDetalleCuenta().iterator();
		    detalleCuentaHashMap=(HashMap)it.next();
		    
		    if (!(""+detalleCuentaHashMap.get("codigonaturaleza")).equals("" + ConstantesBD.codigoNaturalezaPacientesNinguno))
		    {
				dataDetalleCuenta=comunDetalleCuenta(detalleCuentaHashMap, true, estadoCuentaForm);
		    }
		    else
		    {
				dataDetalleCuenta=comunDetalleCuenta(detalleCuentaHashMap, false, estadoCuentaForm);
		    }
		    
			String[] headerListadoSolicitud={
		        	
					"Fecha",
					"Servicio",
					"Descripción",
					"Cant.",
					"Valor",
					"Recargo"
				};
			String[] dataListadoSolicitud=comunListadoSolicitud(estadoCuentaForm);
			
			
			double calculoAbonos = Double.parseDouble(datosTotales.get("netoCargoPaciente")+"");
		    /****************************************TOTALES**************************************************/
		    String[] headerTotales={
			        "Total de Cargos",
			        "Total a cargo Convenio",
			        "Total a cargo del Paciente",
			        "Total Abonos del Paciente",
			        calculoAbonos<0?"Valor devolución al Paciente":"Neto a Cargo del Paciente"
				};
			String [] dataTotales={
					(String)datosTotales.get("totalCargos"),
					(String)datosTotales.get("totalConvenio"),
					(String)datosTotales.get("totalPaciente"),
					(String)datosTotales.get("totalAbonos"),
					UtilidadTexto.formatearValores(Math.abs(calculoAbonos))
			};

			InstitucionBasica institucionBasica= new InstitucionBasica();
			institucionBasica.cargarXConvenio(medico.getCodigoInstitucionInt(), pacienteActivo.getCodigoConvenio());
			
		    report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), "ESTADO CUENTA RESUMIDO");
		    
		    report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);

		    report.openReport(filename);

		    report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		    report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000, 14);
		    report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 12);
		    
		    report.createSection("detalleCuenta","tablaCuenta",5,3,0);
            report.getSectionReference("detalleCuenta").setTableBorder("tablaCuenta", 0xFFFFFF, 0.0f);
            report.getSectionReference("detalleCuenta").setTableCellBorderWidth("tablaCuenta", 0.5f);
            report.getSectionReference("detalleCuenta").setTableCellsDefaultColors("tablaCuenta", 0xFFFFFF, 0xFFFFFF);
            report.font.setFontSizeAndAttributes(10,true,false,false);
            report.getSectionReference("detalleCuenta").setTableCellsColSpan(3);
            report.getSectionReference("detalleCuenta").addTableTextCellAligned("tablaCuenta", "PACIENTE    "+dataDetalleCuenta[0]+"                   "+dataDetalleCuenta[1]+"                                  EDAD: "+pacienteActivo.getEdadDetallada() , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            report.getSectionReference("detalleCuenta").setTableCellsColSpan(1);

            report.font.setFontSizeAndAttributes(8,false,false,false);
            report.getSectionReference("detalleCuenta").addTableTextCell("tablaCuenta", "VIA INGRESO: "+dataDetalleCuenta[2], report.font);
            report.getSectionReference("detalleCuenta").addTableTextCell("tablaCuenta", "FECHA INGRESO: "+dataDetalleCuenta[3], report.font);
            report.getSectionReference("detalleCuenta").addTableTextCell("tablaCuenta", "No. CUENTA: "+dataDetalleCuenta[4].split("-")[0], report.font);
            report.getSectionReference("detalleCuenta").addTableTextCell("tablaCuenta", "ESTADO CUENTA: "+dataDetalleCuenta[4].split("-")[1], report.font);
            report.getSectionReference("detalleCuenta").addTableTextCell("tablaCuenta", "RESPONSABLE: "+dataDetalleCuenta[5], report.font);
            report.getSectionReference("detalleCuenta").addTableTextCell("tablaCuenta", "CLS. SOCIOECONOMICA: "+dataDetalleCuenta[6], report.font);
            report.getSectionReference("detalleCuenta").addTableTextCell("tablaCuenta", ""+dataDetalleCuenta[7]+"   "+dataDetalleCuenta[8], report.font);
            if(indicador==1&&dataDetalleCuenta.length>=10)
            {
                report.getSectionReference("detalleCuenta").addTableTextCell("tablaCuenta", "NATURALEZA DEL PACIENTE: " +dataDetalleCuenta[9], report.font);
            	
            }
            report.getSectionReference("detalleCuenta").setTableCellsColSpan(1);
            report.setReportDataSectionAtributes(0xFFFFFF, 0xFFFFFF, 0x000000,8);
            //definicion de fuente
            report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
            //definicion de atributos para el titulo de la seccion(tabla)
            report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000,8);
            //definicion de atributos para los datos de la seccion
            report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000,8);
            report.addSectionToDocument("detalleCuenta");
		    
		    //Añadimos la sección de detalle de la cuenta
		    
		    //Ahora añadimos el listado de las solicitud
			//if (estadoCuentaForm.getSolicitudesCuenta().size()>0)
		    /*********************************** SECCIÓN DETALLE SOLICITUDES ****************************************************************/
            int auxI1 = 0;
		    if (estadoCuentaForm.getImpresionSolicitudesCuenta().size()>0)
			{
			    //**********************************************************************************************************************
			    report.createSection("listadoSolicitud",PdfReports.REPORT_PARENT_TABLE,(dataListadoSolicitud.length/6)+1,6,0);
	            report.getSectionReference("listadoSolicitud").setTableBorder(PdfReports.REPORT_PARENT_TABLE, 0x000000, 0.5f);
	            report.getSectionReference("listadoSolicitud").setTableCellBorderWidth(PdfReports.REPORT_PARENT_TABLE, 0.5f);
	            report.getSectionReference("listadoSolicitud").setTableCellsDefaultColors(PdfReports.REPORT_PARENT_TABLE, 0xFFFFFF, 0x000000);
	            //report.getSectionReference("listadoSolicitud").getTableReference(PdfReports.REPORT_PARENT_TABLE).setCellpadding(0.0f);
	            //report.getSectionReference("listadoSolicitud").getTableReference(PdfReports.REPORT_PARENT_TABLE).setCellspacing(0.0f);
	            
	            
	            //INSERCIÓN DEL ENCABEZADO
	            report.font.setFontSizeAndAttributes(8,true,false,false);
	            int auxI2 = 0;
	            while(auxI2<6)
	            {
	            	report.getSectionReference("listadoSolicitud").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, headerListadoSolicitud[auxI2],report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	            	auxI2 ++;
	            }
	            //INSERCIÓN DEL CONTENIDO
	            report.font.setFontSizeAndAttributes(8,false,false,false);
	            
		        while(auxI1<dataListadoSolicitud.length)
		        {
	        		//CAMPO FECHA
	        		report.getSectionReference("listadoSolicitud").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, dataListadoSolicitud[auxI1],report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	        		auxI1++;
	        		//CAMPO SERVICIO
	        		report.getSectionReference("listadoSolicitud").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, dataListadoSolicitud[auxI1],report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	        		auxI1++;
	        		//CAMPO DESCRIPCION
	        		report.getSectionReference("listadoSolicitud").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, dataListadoSolicitud[auxI1],report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	        		auxI1++;
	        		//CAMPO CANTIDAD
	        		report.getSectionReference("listadoSolicitud").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, dataListadoSolicitud[auxI1],report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	        		auxI1++;
	        		//CAMPO VALOR
	        		report.getSectionReference("listadoSolicitud").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, dataListadoSolicitud[auxI1],report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	        		auxI1++;
	        		//CAMPO RECARGO
	        		report.getSectionReference("listadoSolicitud").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, dataListadoSolicitud[auxI1],report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	        		auxI1++;
		        	
		        }
			    //*************************************************************************************************************************
			}
			else
			{
			    String arreglo1[]={"Solicitudes"}, arreglo2[]={" No existen Solicitudes para el Criterio Específicado"};  
			    report.createColSection("listadoSolicitud", arreglo1, arreglo2, 8);
			}
		    
		   
		    float widths[]={(float)0.8,(float)0.7,(float) 5.9,(float) 0.5,(float) 1.3,(float)0.7};
	        try {
	        		report.getSectionReference("listadoSolicitud").getTableReference(PdfReports.REPORT_PARENT_TABLE).setWidths(widths);
	             }
	        catch (BadElementException e)
                {
	        	estadoCuentaPdfLogger.error("Se presentó error generando el pdf " +e);
                }
	        report.getSectionReference("listadoSolicitud").getTableReference(PdfReports.REPORT_PARENT_TABLE).setAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
		    report.addSectionToDocument("listadoSolicitud");
		    /********************************************************************************************************************************************************/
		    
		    report.createSection("espacios",PdfReports.REPORT_PARENT_TABLE,1,1,0);
	        report.getSectionReference("espacios").setTableBorder(PdfReports.REPORT_PARENT_TABLE, 0xFFFFFF, 0.0f);
            report.getSectionReference("espacios").setTableCellBorderWidth(PdfReports.REPORT_PARENT_TABLE, 0.0f);
            report.getSectionReference("espacios").setTableCellsDefaultColors(PdfReports.REPORT_PARENT_TABLE, 0xFFFFFF, 0xFFFFFF);
	        report.addSectionToDocument("espacios");
		    
		    /*************************SECCIÓN TOTALES************************************************************************************************************************/
	        report.createSection("totales",PdfReports.REPORT_PARENT_TABLE,5,2,0);
            report.getSectionReference("totales").setTableBorder(PdfReports.REPORT_PARENT_TABLE, 0x000000, 0.5f);
            report.getSectionReference("totales").setTableCellBorderWidth(PdfReports.REPORT_PARENT_TABLE, 0.5f);
            report.getSectionReference("totales").setTableCellsDefaultColors(PdfReports.REPORT_PARENT_TABLE, 0xFFFFFF, 0x000000);
            
            
            //INSERCIÓN DEL CONTENIDO
            auxI1 = 0;
    		//TOTAL DE CARGOS
        	report.font.setFontSizeAndAttributes(8,true,false,false);
    		report.getSectionReference("totales").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, headerTotales[auxI1],report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
    		report.font.setFontSizeAndAttributes(8,false,false,false);
    		report.getSectionReference("totales").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, dataTotales[auxI1],report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
    		auxI1++;
    		//TOTAL A CARGO CONVENIO
    		report.font.setFontSizeAndAttributes(8,true,false,false);
    		report.getSectionReference("totales").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, headerTotales[auxI1],report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
    		report.font.setFontSizeAndAttributes(8,false,false,false);
    		report.getSectionReference("totales").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, dataTotales[auxI1],report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
    		auxI1++;
    		//TOTAL A CARGO DEL PACIENTE
    		report.font.setFontSizeAndAttributes(8,true,false,false);
    		report.getSectionReference("totales").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, headerTotales[auxI1],report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
    		report.font.setFontSizeAndAttributes(8,false,false,false);
    		report.getSectionReference("totales").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, dataTotales[auxI1],report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
    		auxI1++;
    		//TOTAL ABONOS DEL PACIENTE
    		report.font.setFontSizeAndAttributes(8,true,false,false);
    		report.getSectionReference("totales").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, headerTotales[auxI1],report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
    		report.font.setFontSizeAndAttributes(8,false,false,false);
    		report.getSectionReference("totales").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, dataTotales[auxI1],report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
    		auxI1++;
    		//NETO A CARGO DEL PACIENTE
    		report.font.setFontSizeAndAttributes(8,true,false,false);
    		report.getSectionReference("totales").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, headerTotales[auxI1],report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
    		report.font.setFontSizeAndAttributes(8,false,false,false);
    		report.getSectionReference("totales").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, dataTotales[auxI1],report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
    		auxI1++;
	        
	        report.addSectionToDocument("totales");
		    //Ahora los totales
		    /**report.createRowSection("totales", headerTotales, dataTotales, 8);
		    
		    report.addSectionToDocument("totales");**/
		    /*****************************************************************************************************************************************************/
		    report.closeReport(); 
		}

      }

	/**
	 * Método que genera los datos especificos
	 * del detalle de una solicitud
	 * 
	 * @param colHashMap Colección de HashMap
	 * (Máx. un elemento)
	 * @return
	 */
	public static String[] comunDetalleSolicitud (EstadoCuentaForm estadoCuentaForm)
	{
	   
		
	    if (estadoCuentaForm.getSolicitudesCuenta().size()==0)
		{
		    return new String[0];
		}
	   
	    
	    ArrayList respuesta=new ArrayList();
	    HashMap elemento;
	    int i=0;
	    
	    if (estadoCuentaForm.getDetalleCuenta().size()==0)
		{
		    return new String[0];
		}
	    
	    Iterator it=estadoCuentaForm.getImpresionDetSolicitudesCuenta().iterator();
		while (it.hasNext())
		{
			
			elemento=(HashMap)it.next();
			
			respuesta.add(i, elemento.get("orden"));
			respuesta.add(i+1, elemento.get("fechacargo")==null?"":elemento.get("fechacargo"));
			respuesta.add(i+2, elemento.get("medico")==null?"":elemento.get("medico"));
			respuesta.add(i+3, elemento.get("centrocosto"));
			respuesta.add(i+4,elemento.get("servicio")==null?"":elemento.get("servicio"));
			respuesta.add(i+5, elemento.get("descripcion")==null?"":elemento.get("descripcion"));
			respuesta.add(i+6, elemento.get("cantidad")==null?"":elemento.get("cantidad"));
			respuesta.add(i+7, UtilidadTexto.formatearValores(elemento.get("valor")==null?"0":elemento.get("valor").toString()));
			respuesta.add(i+8, UtilidadTexto.formatearValores(elemento.get("recargo")==null?"0":elemento.get("recargo").toString()));
			   			    
			    
			i=i+9;
			
		}
		String dataDetalleSolicitud []= new String[respuesta.size()];
		UtilidadTexto.copiarArrayListAArreglo(respuesta, dataDetalleSolicitud);
	    return dataDetalleSolicitud;
	    
	  
	}
	/**
	 * Método que genera el Pdf de Listado de 
	 * Solicitudes
	 * 
	 * @param filename Nombre del archivo con el que
	 * se desea generar el PDF
	 * @param estadoCuentaForm Form del que se sacan
	 * las colecciones
	 * @param medico
	 */
	public static  void pdfDetalleSolicitud(String filename, EstadoCuentaForm estadoCuentaForm, UsuarioBasico medico, Collection detalleSolicitud, PersonaBasica pacienteActivo, HashMap datosTotales)
	{
		PdfReports report = new PdfReports();
		String[] dataDetalleCuenta;
		//String[] headerDetalleCuenta ;
		detalleSolicitud.size();
		HashMap detalleCuentaHashMap;
		Iterator it;
		
		if (estadoCuentaForm.getDetalleCuenta().size()==0)
		{
		    return;
		}
		else
		{
		    it=estadoCuentaForm.getDetalleCuenta().iterator();
		    detalleCuentaHashMap=(HashMap)it.next();
		    
		   
		    if (!(""+detalleCuentaHashMap.get("codigonaturaleza")).equals("" + ConstantesBD.codigoNaturalezaPacientesNinguno))
				//dataDetalleCuenta=comunDetalleCuenta(detalleCuentaHashMap, false);
				dataDetalleCuenta=comunDetalleCuenta(detalleCuentaHashMap, true, estadoCuentaForm);
		    else   
				dataDetalleCuenta=comunDetalleCuenta(detalleCuentaHashMap, false, estadoCuentaForm);
		    
		    String headerDetalleSolicitud[]={
		            
		    		"Orden",
					"Fecha",
					"Profesional que Responde",
					"Centro de Costo",
					"Servicio",
					"Descripción",
					"Cant.",
					"Valor",
					"Recargo"
		    };
		    //String[] dataDetalleSolicitud=comunDetalleSolicitud(colHashMap);
		    String[] dataDetalleSolicitud=comunDetalleSolicitud(estadoCuentaForm);

		    InstitucionBasica institucionBasica= new InstitucionBasica();
		    institucionBasica.cargarXConvenio(medico.getCodigoInstitucionInt(), pacienteActivo.getCodigoConvenio());
		    report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), "ESTADO CUENTA DETALLADO");
		    
		    report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);

		    report.openReport(filename);
		    
		    double calculoAbonos = Double.parseDouble(datosTotales.get("netoCargoPaciente")+"");
		    /****************************************TOTALES**************************************************/
		    String[] headerTotales={
			        "Total de Cargos",
			        "Total a cargo Convenio",
			        "Total a cargo del Paciente",
			        "Total Abonos del Paciente",
			        calculoAbonos<0?"Valor devolución al Paciente":"Neto a Cargo del Paciente"
				};
			String [] dataTotales={
					(String)datosTotales.get("totalCargos"),
					(String)datosTotales.get("totalConvenio"),
					(String)datosTotales.get("totalPaciente"),
					(String)datosTotales.get("totalAbonos"),
					UtilidadTexto.formatearValores(Math.abs(calculoAbonos))
			};
		    
		    
		    /****************************************INFO PACIENTE****************************************************/
		    
		    report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		    report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000, 14);
		    report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 12);
		    
		    report.createSection("detalleCuenta","tablaCuenta",5,3,0);
            report.getSectionReference("detalleCuenta").setTableBorder("tablaCuenta", 0xFFFFFF, 0.0f);
            report.getSectionReference("detalleCuenta").setTableCellBorderWidth("tablaCuenta", 0.5f);
            report.getSectionReference("detalleCuenta").setTableCellsDefaultColors("tablaCuenta", 0xFFFFFF, 0xFFFFFF);
            report.font.setFontSizeAndAttributes(10,true,false,false);
            report.getSectionReference("detalleCuenta").setTableCellsColSpan(3);
            report.getSectionReference("detalleCuenta").addTableTextCellAligned("tablaCuenta", "PACIENTE    "+dataDetalleCuenta[0]+"                   "+dataDetalleCuenta[1]+"                                  EDAD: "+pacienteActivo.getEdadDetallada() , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            report.getSectionReference("detalleCuenta").setTableCellsColSpan(1);

            report.font.setFontSizeAndAttributes(8,false,false,false);
            report.getSectionReference("detalleCuenta").addTableTextCell("tablaCuenta", "VIA INGRESO: "+dataDetalleCuenta[2], report.font);
            report.getSectionReference("detalleCuenta").addTableTextCell("tablaCuenta", "FECHA INGRESO: "+dataDetalleCuenta[3], report.font);
            report.getSectionReference("detalleCuenta").addTableTextCell("tablaCuenta", "No. CUENTA: "+dataDetalleCuenta[4].split("-")[0], report.font);
            report.getSectionReference("detalleCuenta").addTableTextCell("tablaCuenta", "ESTADO CUENTA: "+dataDetalleCuenta[4].split("-")[1], report.font);
            report.getSectionReference("detalleCuenta").addTableTextCell("tablaCuenta", "RESPONSABLE: "+dataDetalleCuenta[5], report.font);
            report.getSectionReference("detalleCuenta").addTableTextCell("tablaCuenta", "CLS. SOCIOECONOMICA: "+dataDetalleCuenta[6], report.font);
            report.getSectionReference("detalleCuenta").addTableTextCell("tablaCuenta", ""+dataDetalleCuenta[7]+"   "+dataDetalleCuenta[8], report.font);
            if(indicador==1&&dataDetalleCuenta.length>=10)
            {
                report.getSectionReference("detalleCuenta").addTableTextCell("tablaCuenta", "NATURALEZA DEL PACIENTE: " +dataDetalleCuenta[9], report.font);
            	
            }
            report.getSectionReference("detalleCuenta").setTableCellsColSpan(1);
            report.setReportDataSectionAtributes(0xFFFFFF, 0xFFFFFF, 0x000000,8);
            //definicion de fuente
            report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
            //definicion de atributos para el titulo de la seccion(tabla)
            report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000,8);
            //definicion de atributos para los datos de la seccion
            report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000,8);
            report.addSectionToDocument("detalleCuenta");
		    
		    /*******************************************************************************************************/
            /***********************SECCIÓN DETALLE DE SOLICITUDES**************************************************/
            int auxI1 = 0;
            int auxI2 = 0;
            if (estadoCuentaForm.getImpresionDetSolicitudesCuenta().size()>0)
			{
	            report.createSection("detalleSolicitud",PdfReports.REPORT_PARENT_TABLE,(dataDetalleSolicitud.length/9)+1,9,0);
	            //report.getSectionReference("detalleSolicitud").getTableReference("tablaCuenta").setCellpadding(0.0f);
	            //report.getSectionReference("detalleSolicitud").getTableReference("tablaCuenta").setCellspacing(0.0f);
	            report.getSectionReference("detalleSolicitud").setTableBorder(PdfReports.REPORT_PARENT_TABLE, 0x000000, 0.5f);
	            report.getSectionReference("detalleSolicitud").setTableCellBorderWidth(PdfReports.REPORT_PARENT_TABLE, 0.5f);
	            report.getSectionReference("detalleSolicitud").setTableCellsDefaultColors(PdfReports.REPORT_PARENT_TABLE, 0xFFFFFF, 0x000000);
	            //report.getSectionReference("detalleSolicitud").getTableReference(PdfReports.REPORT_PARENT_TABLE).setCellpadding(0.0f);
	            //report.getSectionReference("detalleSolicitud").getTableReference(PdfReports.REPORT_PARENT_TABLE).setCellspacing(0.0f);
	            //SE ESTABLECEN ANCHOS
	            float widths[]={(float)0.7,(float)0.8,(float)2.0,(float) 1.0,(float) 0.7,(float)2.3,(float) 0.5, (float)1.3, (float)0.7};
		        try 
				{
		        		report.getSectionReference("detalleSolicitud").getTableReference(PdfReports.REPORT_PARENT_TABLE).setWidths(widths);
		        }
		        catch (BadElementException e)
	            {
		        	estadoCuentaPdfLogger.error("Se presentó error generando el pdf " +e);
	            }
	            
	            //INSERCIÓN DEL ENCABEZADO
	            report.font.setFontSizeAndAttributes(8,true,false,false);
	            
	            while(auxI2<9)
	            {
	            	report.getSectionReference("detalleSolicitud").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, headerDetalleSolicitud[auxI2],report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	            	auxI2 ++;
	            }
	            //INSERCIÓN DEL CONTENIDO
	            report.font.setFontSizeAndAttributes(8,false,false,false);
	            
		        while(auxI1<dataDetalleSolicitud.length)
		        {
	        		//CAMPO ORDEN
	        		report.getSectionReference("detalleSolicitud").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, dataDetalleSolicitud[auxI1],report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	        		auxI1++;
	        		//CAMPO FECHA
	        		report.getSectionReference("detalleSolicitud").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, dataDetalleSolicitud[auxI1],report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	        		auxI1++;
	        		//CAMPO PROFESIONAL DE LA SALUD
	        		report.getSectionReference("detalleSolicitud").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, dataDetalleSolicitud[auxI1],report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	        		auxI1++;
	        		//CAMPO CENTRO DE COSTO
	        		report.getSectionReference("detalleSolicitud").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, dataDetalleSolicitud[auxI1],report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	        		auxI1++;
	        		//CAMPO SERVICIO
	        		report.getSectionReference("detalleSolicitud").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, dataDetalleSolicitud[auxI1],report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	        		auxI1++;
	        		//CAMPO DESCRIPCION
	        		report.getSectionReference("detalleSolicitud").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, dataDetalleSolicitud[auxI1],report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	        		auxI1++;
	        		//CAMPO CANTIDAD
	        		report.getSectionReference("detalleSolicitud").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, dataDetalleSolicitud[auxI1],report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	        		auxI1++;
	        		//CAMPO VALOR
	        		report.getSectionReference("detalleSolicitud").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, dataDetalleSolicitud[auxI1],report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	        		auxI1++;
	        		//CAMPO RECARGO
	        		report.getSectionReference("detalleSolicitud").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, dataDetalleSolicitud[auxI1],report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	        		auxI1++;
		        	
		        }
		        report.addSectionToDocument("detalleSolicitud");
			}
            else
            {
            	 String arreglo1[]={"Solicitudes"}, arreglo2[]={" No existen Solicitudes para el Criterio Específicado"};  
 			    report.createColSection("detalleSolicitud", arreglo1, arreglo2, 8);
 			   report.addSectionToDocument("detalleSolicitud");
            }
	        /************************************************************************************************************/
	        
	        report.createSection("espacios",PdfReports.REPORT_PARENT_TABLE,1,1,0);
	        report.getSectionReference("espacios").setTableBorder(PdfReports.REPORT_PARENT_TABLE, 0xFFFFFF, 0.0f);
            report.getSectionReference("espacios").setTableCellBorderWidth(PdfReports.REPORT_PARENT_TABLE, 0.0f);
            report.getSectionReference("espacios").setTableCellsDefaultColors(PdfReports.REPORT_PARENT_TABLE, 0xFFFFFF, 0xFFFFFF);
	        report.addSectionToDocument("espacios");
	        
	        /******************************SECCIÓN DE LOS TOTALES*******************************************************/
	        report.createSection("totales",PdfReports.REPORT_PARENT_TABLE,5,2,0);
            report.getSectionReference("totales").setTableBorder(PdfReports.REPORT_PARENT_TABLE, 0x000000, 0.5f);
            report.getSectionReference("totales").setTableCellBorderWidth(PdfReports.REPORT_PARENT_TABLE, 0.5f);
            report.getSectionReference("totales").setTableCellsDefaultColors(PdfReports.REPORT_PARENT_TABLE, 0xFFFFFF, 0x000000);
            
            
            //INSERCIÓN DEL CONTENIDO
            auxI1 = 0;
    		//TOTAL DE CARGOS
        	report.font.setFontSizeAndAttributes(8,true,false,false);
    		report.getSectionReference("totales").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, headerTotales[auxI1],report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
    		report.font.setFontSizeAndAttributes(8,false,false,false);
    		report.getSectionReference("totales").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, dataTotales[auxI1],report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
    		auxI1++;
    		//TOTAL A CARGO CONVENIO
    		report.font.setFontSizeAndAttributes(8,true,false,false);
    		report.getSectionReference("totales").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, headerTotales[auxI1],report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
    		report.font.setFontSizeAndAttributes(8,false,false,false);
    		report.getSectionReference("totales").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, dataTotales[auxI1],report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
    		auxI1++;
    		//TOTAL A CARGO DEL PACIENTE
    		report.font.setFontSizeAndAttributes(8,true,false,false);
    		report.getSectionReference("totales").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, headerTotales[auxI1],report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
    		report.font.setFontSizeAndAttributes(8,false,false,false);
    		report.getSectionReference("totales").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, dataTotales[auxI1],report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
    		auxI1++;
    		//TOTAL ABONOS DEL PACIENTE
    		report.font.setFontSizeAndAttributes(8,true,false,false);
    		report.getSectionReference("totales").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, headerTotales[auxI1],report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
    		report.font.setFontSizeAndAttributes(8,false,false,false);
    		report.getSectionReference("totales").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, dataTotales[auxI1],report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
    		auxI1++;
    		//NETO A CARGO DEL PACIENTE
    		report.font.setFontSizeAndAttributes(8,true,false,false);
    		report.getSectionReference("totales").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, headerTotales[auxI1],report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
    		report.font.setFontSizeAndAttributes(8,false,false,false);
    		report.getSectionReference("totales").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, dataTotales[auxI1],report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
    		auxI1++;
	        
	        report.addSectionToDocument("totales");
	        
	        /**********************************************************************************************************/
		   
		    report.closeReport(); 
		}
	}
	
	
}
