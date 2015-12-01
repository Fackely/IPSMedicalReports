/*
 * @(#)ConsultaFacturasPdf.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.pdf;


import javax.servlet.http.HttpServletRequest;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;
import com.princetonsa.actionform.facturacion.ConsultaFacturasForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * Clase para manejar la generación de PDF's para
 * La consulta de Facturas
 * 
 *	@version 1.0, 10 /Jun/ 2005
 */
public class ConsultaFacturasPdf
{

	
	/**
	 * Impresion de la consulta de las facturas por el flujo de todos
	 * @param filename
	 * @param consultaFacturasForm
	 * @param medico
	 */
	public static void pdfFacturasTodos(String filename, ConsultaFacturasForm consultaFacturasForm, UsuarioBasico medico, HttpServletRequest request)
	{
		PdfReports report = new PdfReports();
		/**Tamaño del mapa que contien todas las facturas consultadas por el flujo de TODAS**/
		int tamanioMapa=Integer.parseInt(consultaFacturasForm.getMapaFacturasTodos("numRegistros")+"");
		
		if (tamanioMapa==0)
		{
		    return;
		}
		else
		{
		   
			/**String[] headerListadoSolicitud={
		        	
					"Factura",
					"Fecha-Hora Elaboración",
					"Vía Ingreso",
					"Responsable",
					"Estado Factura",
					"Estado Paciente",
					"Valor Total",
					"Valor Convenio",
					"Valor Paciente"
				};
			
			/**Cabezote principal**/
			
			InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), "LISTADO DE FACTURAS");
		    
		    report.openReport(filename);
		    report.document.addParagraph(UtilidadFecha.getFechaActual()+"  "+UtilidadFecha.getHoraActual(),report.font,iTextBaseDocument.ALIGNMENT_CENTER,25);
		    
		    report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
		    

		    report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		    report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000, 9);
		    report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 7);
		    
		    int codigoEstadoFactura=0;
		    /**Enteros para calcular totales por separado**/
		    double totalGeneral=0;
		    double totalConvenio=0;
		    double totalPaciente=0;
		    double totalGeneralAnulado=0;
		    double totalAnuladoXConvenio=0;
		    double totalAnuladoXPaciente=0;
		    double totalGeneralFacturado=0;
		    double totalFacturadoXConvenio=0;
		    double totalFacturadoXPaciente=0;

		    /**Para calcular los totales por separados necesarios para la impresion*/
		    for(int i=0; i<tamanioMapa;i++)
		    {
		    	totalGeneral+=Double.parseDouble(consultaFacturasForm.getMapaFacturasTodos("valorTotal_"+i)+"");
		    	totalConvenio+=Double.parseDouble(consultaFacturasForm.getMapaFacturasTodos("valorConvenio_"+i)+"");
		    	totalPaciente+=Double.parseDouble(consultaFacturasForm.getMapaFacturasTodos("valorPaciente_"+i)+"");
		    	
		    	codigoEstadoFactura=Integer.parseInt(consultaFacturasForm.getMapaFacturasTodos("codigoEstadoFactura_"+i)+"");
		    	if(codigoEstadoFactura==ConstantesBD.codigoEstadoFacturacionAnulada)
		    	{
		    		totalGeneralAnulado+=Double.parseDouble(consultaFacturasForm.getMapaFacturasTodos("valorTotal_"+i)+"");
		    		totalAnuladoXConvenio+=Double.parseDouble(consultaFacturasForm.getMapaFacturasTodos("valorConvenio_"+i)+"");
			    	totalAnuladoXPaciente+=Double.parseDouble(consultaFacturasForm.getMapaFacturasTodos("valorPaciente_"+i)+"");
		    	}
		    	if(codigoEstadoFactura==ConstantesBD.codigoEstadoFacturacionFacturada)
		    	{
		    		totalGeneralFacturado+=Double.parseDouble(consultaFacturasForm.getMapaFacturasTodos("valorTotal_"+i)+"");
		    		totalFacturadoXConvenio+=Double.parseDouble(consultaFacturasForm.getMapaFacturasTodos("valorConvenio_"+i)+"");
		    		totalFacturadoXPaciente+=Double.parseDouble(consultaFacturasForm.getMapaFacturasTodos("valorPaciente_"+i)+"");
		    	}
		    }
		    
		  /**  report.font.setFontSizeAndAttributes(10,true,false,false);
		    int tamanioVector=tamanioMapa*9;
		    String[] datos= new String[tamanioVector];

		    for(int k=0,cont=0; k<tamanioMapa; k++)
			{
		    	datos[cont]=consultaFacturasForm.getMapaFacturasTodos("consecutivo_"+k)+"";cont++;
		    	datos[cont]=consultaFacturasForm.getMapaFacturasTodos("fechaHoraElaboracion_"+k)+"";cont++;
		    	datos[cont]=consultaFacturasForm.getMapaFacturasTodos("viaIngreso_"+k)+"";cont++;
		    	datos[cont]=consultaFacturasForm.getMapaFacturasTodos("responsable_"+k)+"";cont++;
		    	datos[cont]=consultaFacturasForm.getMapaFacturasTodos("estadoFactura_"+k)+"";cont++;
		    	datos[cont]=consultaFacturasForm.getMapaFacturasTodos("estadoPaciente_"+k)+"";cont++;
		    	datos[cont]=UtilidadTexto.formatearValores((consultaFacturasForm.getMapaFacturasTodos("valorTotal_"+k)+""));cont++;
		    	datos[cont]=UtilidadTexto.formatearValores((consultaFacturasForm.getMapaFacturasTodos("valorConvenio_"+k)+""));cont++;
		    	datos[cont]=UtilidadTexto.formatearValores((consultaFacturasForm.getMapaFacturasTodos("valorPaciente_"+k)+""));cont++;
			}
		    report.createColSection("titulosTabla",headerListadoSolicitud ,datos, 10);
            report.addSectionToDocument("titulosTabla");
            
            
            
            **/
            report.createSection("facturasTodos","tablafacturasTodos",1,11,0);
    	    //report.getSectionReference("facturasTodos").getTableReference("tablafacturasTodos").setCellpadding(0.2f);
            //report.getSectionReference("facturasTodos").getTableReference("tablafacturasTodos").setCellspacing(0.2f);
            report.getSectionReference("facturasTodos").setTableBorder("tablafacturasTodos", 0x000000, 0.1f);
            report.getSectionReference("facturasTodos").setTableCellBorderWidth("tablafacturasTodos", 0.1f);
            report.getSectionReference("facturasTodos").setTableCellsDefaultColors("tablafacturasTodos", 0xFFFFFF, 0x000000);
            
            
            //Titulos de las columnas
            report.font.setFontSizeAndAttributes(8,true,false,false);
            report.getSectionReference("facturasTodos").setTableCellsColSpan(1);
            report.getSectionReference("facturasTodos").addTableTextCellAligned("tablafacturasTodos", "Factura", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
            report.getSectionReference("facturasTodos").addTableTextCellAligned("tablafacturasTodos", "Fecha-Hora Elaboración", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
            report.getSectionReference("facturasTodos").addTableTextCellAligned("tablafacturasTodos", "Usuario Elabora", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
            report.getSectionReference("facturasTodos").addTableTextCellAligned("tablafacturasTodos", "Vía Ingreso", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
            report.getSectionReference("facturasTodos").addTableTextCellAligned("tablafacturasTodos", "Nro. Ingreso", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
            report.getSectionReference("facturasTodos").addTableTextCellAligned("tablafacturasTodos", "Responsable", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
            report.getSectionReference("facturasTodos").addTableTextCellAligned("tablafacturasTodos", "Estado Factura", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
            report.getSectionReference("facturasTodos").addTableTextCellAligned("tablafacturasTodos", "Estado Paciente", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
            report.getSectionReference("facturasTodos").addTableTextCellAligned("tablafacturasTodos", "Valor Total", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
            report.getSectionReference("facturasTodos").addTableTextCellAligned("tablafacturasTodos", "Valor Convenio", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
            report.getSectionReference("facturasTodos").addTableTextCellAligned("tablafacturasTodos", "Valor Paciente", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);

            
            //Datos de las columnas de la impresion
            report.font.setFontSizeAndAttributes(8,false,false,false);
	        report.getSectionReference("facturasTodos").setTableCellsColSpan(1);
	        for(int i = 0; i < Integer.parseInt(consultaFacturasForm.getMapaFacturasTodos("numRegistros")+"") ; i++)
	        {
	        	report.getSectionReference("facturasTodos").addTableTextCellAligned("tablafacturasTodos", ""+consultaFacturasForm.getMapaFacturasTodos("consecutivo_"+i),report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
	        	report.getSectionReference("facturasTodos").addTableTextCellAligned("tablafacturasTodos", ""+consultaFacturasForm.getMapaFacturasTodos("fechaHoraElaboracion_"+i),report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
	        	report.getSectionReference("facturasTodos").addTableTextCellAligned("tablafacturasTodos", ""+consultaFacturasForm.getMapaFacturasTodos("usuario_"+i),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
	        	report.getSectionReference("facturasTodos").addTableTextCellAligned("tablafacturasTodos", ""+consultaFacturasForm.getMapaFacturasTodos("viaIngreso_"+i),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
	        	report.getSectionReference("facturasTodos").addTableTextCellAligned("tablafacturasTodos", ""+consultaFacturasForm.getMapaFacturasTodos("codigoIngreso_"+i),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
	        	report.getSectionReference("facturasTodos").addTableTextCellAligned("tablafacturasTodos", ""+consultaFacturasForm.getMapaFacturasTodos("responsable_"+i),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
	        	report.getSectionReference("facturasTodos").addTableTextCellAligned("tablafacturasTodos", ""+consultaFacturasForm.getMapaFacturasTodos("estadoFactura_"+i),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
	        	report.getSectionReference("facturasTodos").addTableTextCellAligned("tablafacturasTodos", ""+consultaFacturasForm.getMapaFacturasTodos("estadoPaciente_"+i),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
	        	report.getSectionReference("facturasTodos").addTableTextCellAligned("tablafacturasTodos", ""+UtilidadTexto.formatearValores((consultaFacturasForm.getMapaFacturasTodos("valorTotal_"+i)+"")),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
	        	report.getSectionReference("facturasTodos").addTableTextCellAligned("tablafacturasTodos", ""+UtilidadTexto.formatearValores((consultaFacturasForm.getMapaFacturasTodos("valorConvenio_"+i)+"")),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
	        	report.getSectionReference("facturasTodos").addTableTextCellAligned("tablafacturasTodos", ""+UtilidadTexto.formatearValores((consultaFacturasForm.getMapaFacturasTodos("valorPaciente_"+i)+"")),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
	        }

	        
	        report.addSectionToDocument("facturasTodos");
	        
			/**Totales Generales**/
            report.createSection("facturasTodosTotales","tablaTotales",1,10,0);
    	    //report.getSectionReference("facturasTodosTotales").getTableReference("tablaTotales").setCellpadding(0.2f);
            //report.getSectionReference("facturasTodosTotales").getTableReference("tablaTotales").setCellspacing(0.2f);
            report.getSectionReference("facturasTodosTotales").setTableBorder("tablaTotales", 0xFFFFFF, 0.1f);
            report.getSectionReference("facturasTodosTotales").setTableCellBorderWidth("tablaTotales", 0.1f);
            report.getSectionReference("facturasTodosTotales").setTableCellsDefaultColors("tablaTotales", 0xFFFFFF, 0xFFFFFF);
            
            
            report.font.setFontSizeAndAttributes(10,true,false,false);
            report.getSectionReference("facturasTodosTotales").setTableCellsColSpan(3);
            report.getSectionReference("facturasTodosTotales").addTableTextCellAligned("tablaTotales", "", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            report.getSectionReference("facturasTodosTotales").setTableCellsColSpan(2);
            report.getSectionReference("facturasTodosTotales").addTableTextCellAligned("tablaTotales", "TOTAL FACTURADO", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
            report.getSectionReference("facturasTodosTotales").setTableCellsColSpan(3);
            report.getSectionReference("facturasTodosTotales").addTableTextCellAligned("tablaTotales", "FACTURAS ANULADAS", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
            report.getSectionReference("facturasTodosTotales").setTableCellsColSpan(2);
            report.getSectionReference("facturasTodosTotales").addTableTextCellAligned("tablaTotales", "NETO FACTURADO", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
            
            
            
            report.getSectionReference("facturasTodosTotales").setTableCellsColSpan(3);
            report.font.setFontSizeAndAttributes(10,true,false,false);
            report.getSectionReference("facturasTodosTotales").addTableTextCellAligned("tablaTotales", "TOTAL CONVENIO:", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            report.getSectionReference("facturasTodosTotales").setTableCellsColSpan(2);
            report.font.setFontSizeAndAttributes(10,false,false,false);
            report.getSectionReference("facturasTodosTotales").addTableTextCellAligned("tablaTotales", ""+UtilidadTexto.formatearValores((totalConvenio+"")), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
            report.getSectionReference("facturasTodosTotales").setTableCellsColSpan(3);
            report.getSectionReference("facturasTodosTotales").addTableTextCellAligned("tablaTotales", ""+UtilidadTexto.formatearValores((totalAnuladoXConvenio+"")), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
            report.getSectionReference("facturasTodosTotales").setTableCellsColSpan(2);
            report.getSectionReference("facturasTodosTotales").addTableTextCellAligned("tablaTotales", ""+UtilidadTexto.formatearValores((totalFacturadoXConvenio+"")), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
            
            
            report.getSectionReference("facturasTodosTotales").setTableCellsColSpan(3);
            report.font.setFontSizeAndAttributes(10,true,false,false);
            report.getSectionReference("facturasTodosTotales").addTableTextCellAligned("tablaTotales", "TOTAL PACIENTE:", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            report.getSectionReference("facturasTodosTotales").setTableCellsColSpan(2);
            report.font.setFontSizeAndAttributes(10,false,false,false);
            report.getSectionReference("facturasTodosTotales").addTableTextCellAligned("tablaTotales", ""+UtilidadTexto.formatearValores((totalPaciente+"")), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
            report.getSectionReference("facturasTodosTotales").setTableCellsColSpan(3);
            report.getSectionReference("facturasTodosTotales").addTableTextCellAligned("tablaTotales", ""+UtilidadTexto.formatearValores((totalAnuladoXPaciente+"")), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
            report.getSectionReference("facturasTodosTotales").setTableCellsColSpan(2);
            report.getSectionReference("facturasTodosTotales").addTableTextCellAligned("tablaTotales", ""+UtilidadTexto.formatearValores((totalFacturadoXPaciente+"")), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
            
            report.getSectionReference("facturasTodosTotales").setTableCellsColSpan(3);
            report.font.setFontSizeAndAttributes(10,true,false,false);
            report.getSectionReference("facturasTodosTotales").addTableTextCellAligned("tablaTotales", "TOTAL GENERAL:", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            report.getSectionReference("facturasTodosTotales").setTableCellsColSpan(2);
            report.font.setFontSizeAndAttributes(10,false,false,false);
            report.getSectionReference("facturasTodosTotales").addTableTextCellAligned("tablaTotales", ""+UtilidadTexto.formatearValores((totalGeneral+"")), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
            report.getSectionReference("facturasTodosTotales").setTableCellsColSpan(3);
            report.getSectionReference("facturasTodosTotales").addTableTextCellAligned("tablaTotales", ""+UtilidadTexto.formatearValores((totalGeneralAnulado+"")), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
            report.getSectionReference("facturasTodosTotales").setTableCellsColSpan(2);
            report.getSectionReference("facturasTodosTotales").addTableTextCellAligned("tablaTotales", ""+UtilidadTexto.formatearValores((totalGeneralFacturado+"")), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
            
            
            report.addSectionToDocument("facturasTodosTotales");
            
		    /**Se cierra el reporte**/
		    report.closeReport(); 
		}

      }
}
