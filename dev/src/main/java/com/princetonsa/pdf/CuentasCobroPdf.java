/*
 * @(#)CuentasCobroPdf.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.pdf;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ConstantesValoresPorDefecto;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadN2T;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;
import util.pdf.iTextBaseTable;

import com.lowagie.text.BadElementException;
import com.princetonsa.actionform.cartera.CuentasCobroForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.administracion.DtoFirmasValoresPorDefecto;
import com.princetonsa.dto.facturacion.DtoFactura;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cartera.CuentasCobro;
import com.princetonsa.mundo.facturacion.ParametrizacionInstitucion;

/**
 * Clase para manejar la generación de PDF's para
 * las cuentas de cobro
 * 
 *	
 *  @author <a href="mailto:cperalta@princetonsa.com">Carlos Peralta</a>
 */
public class CuentasCobroPdf
{

	/**
     * Clase para manejar los logs de esta clase
     */
    private static Logger cuentasCobroPdfLogger=Logger.getLogger(CuentasCobroPdf.class);
    
    /**
	 * Método que genera el Pdf de la impresion detallada de
	 * La cuenta de cobro con el cabezote, la tabla de informacion y los totales
	 * @param filename
	 * @param cxcForm
	 * @param medico
	 * @param con
	 * @param mundoCxC
	 */
	private static Boolean pdfDetalleCxC(String filename, CuentasCobroForm cxcForm, UsuarioBasico medico, Connection con, CuentasCobro mundoCxC, HttpServletRequest request)
	{
		PdfReports report = new PdfReports();
		filename = filename.replace("\\\\", "\\");
		double cuenta_cobro=Double.parseDouble(cxcForm.getMapMovimientos("codigoCXC")+"");
		
		mundoCxC.cargarCuentaCobroCompleta(con,cuenta_cobro,medico.getCodigoInstitucionInt());
		cxcForm.setNumCuentaCobro(cuenta_cobro);
		cxcForm.setDetalleFacturas(mundoCxC.getFacturas());
		cxcForm.setMaxPageItems(mundoCxC.getFacturas().size());
		ParametrizacionInstitucion p = new ParametrizacionInstitucion();
		p.cargar(con, medico.getCodigoInstitucionInt());
		
		String[] dataDetalleCuenta=null;
		if (mundoCxC.getFacturas().size()==0)
		{
		    return false;
		}
		else
		{
			//Cargamos desde el mundo  las facturas que le corresponden a una cuenta de cobro
			mundoCxC.getFacturas();
			dataDetalleCuenta=comunCabezoteCxC(mundoCxC, cxcForm, con,medico);
			String[] headerListadoFacturas={
					"No. Factura",
					"Centro Atención",
					"Paciente",
					"Vía Ingreso",
					"Valor Factura"
				};
			String[] dataListadoFacturas=comunListadoFacturas(mundoCxC);
			
			
			//TOTALES 
//			String dataTotal="$ "+UtilidadTexto.formatearValores(mundoCxC.getValInicialCuenta()+"",2,true,true);
			String dataTotal="  "+UtilidadTexto.formatearValores(mundoCxC.getValInicialCuenta()+"");
			String dataTotalLetras=UtilidadN2T.convertirLetras(UtilidadTexto.formatearValores(mundoCxC.getValInicialCuenta()+"","#.00"),ConstantesBD.cadenaUnidades,ConstantesBD.cadenaUnidadesDecimales);
			String totalFacturas=mundoCxC.getFacturas().size()+"";
			
			//Seccion superior de la impresion con la informacion de la institucion
			String tituloReporte="DETALLE DE CARTERA"+"(" +UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() +")";
			
			InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			
		    report.setReportBaseHeaderCxC1(institucionBasica.getLogoReportes(), institucionBasica.getRazonSocial(), institucionBasica.getNit(), tituloReporte, institucionBasica.getDireccion(), institucionBasica.getTelefono());
		    
		    report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);

		    report.openReport(filename);
		    report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		    report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000, 14);
		    report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 12);
		    
		    //Seccion del cabezote de la impresion con toda la informacion de una cuenta de cobro
		    report.createSection("detalleCxC","tablaCuenta",5,4,0);
            report.getSectionReference("detalleCxC").setTableBorder("tablaCuenta", 0xFFFFFF, 0.0f);
            report.getSectionReference("detalleCxC").setTableCellBorderWidth("tablaCuenta", 0.5f);
            report.getSectionReference("detalleCxC").setTableCellsDefaultColors("tablaCuenta", 0xFFFFFF, 0xFFFFFF);
            report.font.setFontSizeAndAttributes(8,true,false,false);
            report.getSectionReference("detalleCxC").setTableCellsColSpan(1);

            report.getSectionReference("detalleCxC").addTableTextCell("tablaCuenta", "Cuenta de Cobro No.:  "+UtilidadTexto.formatearValores(dataDetalleCuenta[0],"0"), report.font);
            report.getSectionReference("detalleCxC").addTableTextCell("tablaCuenta", "Estado:  "+dataDetalleCuenta[1], report.font);
            report.getSectionReference("detalleCxC").addTableTextCell("tablaCuenta", "Fecha Generación:  "+UtilidadFecha.conversionFormatoFechaAAp(dataDetalleCuenta[2]), report.font);
            if (!dataDetalleCuenta[3].equals(""))
            {
            	report.getSectionReference("detalleCxC").addTableTextCell("tablaCuenta", "Fecha Radicación:  "+UtilidadFecha.conversionFormatoFechaAAp(dataDetalleCuenta[3]), report.font);
            	report.getSectionReference("detalleCxC").setTableCellsColSpan(2);
                report.getSectionReference("detalleCxC").addTableTextCell("tablaCuenta", "Convenio:  "+dataDetalleCuenta[4], report.font);
            }
            else
            {
            	report.getSectionReference("detalleCxC").setTableCellsColSpan(1);
            	report.getSectionReference("detalleCxC").addTableTextCell("tablaCuenta", " ", report.font);
            	report.getSectionReference("detalleCxC").setTableCellsColSpan(2);
            	report.getSectionReference("detalleCxC").addTableTextCell("tablaCuenta", "Convenio:  "+dataDetalleCuenta[4], report.font);
            }
            try
			{
	            if((mundoCxC.convenioxCuentaCobro(con,cuenta_cobro))>0)
	            {
	             report.getSectionReference("detalleCxC").setTableCellsColSpan(2);
	             report.getSectionReference("detalleCxC").addTableTextCell("tablaCuenta", "Nit: "+mundoCxC.getNitConvenio(), report.font);
	            }
			}
            catch(SQLException e)
			{
            	e.printStackTrace();
			}
            report.getSectionReference("detalleCxC").setTableCellsColSpan(4);
            report.getSectionReference("detalleCxC").addTableTextCell("tablaCuenta", "Periódo Facturacion:   "+UtilidadFecha.conversionFormatoFechaAAp(dataDetalleCuenta[7])+"   a   "+UtilidadFecha.conversionFormatoFechaAAp(dataDetalleCuenta[6]), report.font);
            if(!dataDetalleCuenta[8].equals(""))
            {
            	report.getSectionReference("detalleCxC").setTableCellsColSpan(4);
            	report.getSectionReference("detalleCxC").addTableTextCell("tablaCuenta", "Observaciones:		"+dataDetalleCuenta[8], report.font);
            }
            else
            {
            	report.getSectionReference("detalleCxC").setTableCellsColSpan(4);
            	report.getSectionReference("detalleCxC").addTableTextCell("tablaCuenta", " ", report.font);
            }
            report.setReportDataSectionAtributes(0xFFFFFF, 0xFFFFFF, 0x000000,8);
            //definicion de fuente
            report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
            //definicion de atributos para el titulo de la seccion(tabla)
            report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000,8);
            //definicion de atributos para los datos de la seccion
            report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000,8);
            report.addSectionToDocument("detalleCxC");
		    
		   
		    if(cxcForm.isImpresionAnexos())
		    {
			    //Seccion del listado de las facturas correspondientes a una cuenta de cobro
			    if (mundoCxC.getFacturas().size()>0)
				{
				    report.createColSection("listadoFacturas", "Facturas", headerListadoFacturas, dataListadoFacturas,8);
				}
				else
				{
				    String arreglo1[]={"Solicitudes"}, arreglo2[]={" No existen Facturas asociadas para esta Cuenta de Cobro"};  
				    report.createColSection("listadoFacturas", arreglo1, arreglo2, 8);
				}
			    
			   
			    float widths[]={(float)1.0,(float)5.5,(float) 2.5,(float) 1.0};
		        try {
		        		report.getSectionReference("listadoFacturas").getTableReference(PdfReports.REPORT_PARENT_TABLE).setWidths(widths);
		             }
		        catch (BadElementException e)
	                {
		        	cuentasCobroPdfLogger.error("Se presentó error generando el pdf de las Cuentas de Cobro " +e);
	                }
		        report.getSectionReference("listadoFacturas").getTableReference(PdfReports.REPORT_PARENT_TABLE).setAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
			    report.addSectionToDocument("listadoFacturas");
		    }
		    
		    //Seccion de totales
		    report.createSection("totales","totalesCxC",1,2,0);
		    report.getSectionReference("totales").setTableBorder("totalesCxC", 0xFFFFFF, 0.0f);
            report.getSectionReference("totales").setTableCellBorderWidth("totalesCxC", 0.5f);
            report.getSectionReference("totales").setTableCellsDefaultColors("totalesCxC", 0xFFFFFF, 0xFFFFFF);
            report.font.setFontSizeAndAttributes(10,true,false,false);
            report.getSectionReference("totales").setTableCellsColSpan(1);
            report.getSectionReference("totales").addTableTextCellAligned("totalesCxC", "Total Facturas Procesadas   "+totalFacturas , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            report.getSectionReference("totales").setTableCellsColSpan(1);
            report.getSectionReference("totales").addTableTextCellAligned("totalesCxC", "Total Cuenta de Cobro:   "+dataTotal , report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
            report.addSectionToDocument("totales");
            
            //Totales en letras
		    report.createSection("totalesLetras","totalesCxCLetras",1,2,0);
		    report.getSectionReference("totalesLetras").setTableBorder("totalesCxCLetras", 0xFFFFFF, 0.0f);
            report.getSectionReference("totalesLetras").setTableCellBorderWidth("totalesCxCLetras", 0.5f);
            report.getSectionReference("totalesLetras").setTableCellsDefaultColors("totalesCxCLetras", 0xFFFFFF, 0xFFFFFF);
            report.font.setFontSizeAndAttributes(10,true,false,false);
            report.getSectionReference("totalesLetras").setTableCellsColSpan(2);
            report.getSectionReference("totalesLetras").addTableTextCellAligned("totalesCxCLetras", "Valor en Letras:   "+dataTotalLetras+"  Moneda Corriente", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            report.addSectionToDocument("totalesLetras");
            
            //Seccion de la firma que se adiciona coom un parrafo para disponer bien del espacio
            
          //Anexo 992
            iTextBaseTable section=new iTextBaseTable();
    	    agregarFirmasValoresXDefecto(cxcForm,medico.getCodigoInstitucionInt(), ConstantesIntegridadDominio.acronimoSuba, section, report);
    	   //Anexo 992
            

		    report.closeReport(); 
		    return true;
		}

      }
	
	/**
	 * * Método que genera el Pdf de la impresion resumida de
	 * La cuenta de cobro con el cabezote, la tabla de informacion de resumidoCxC()  y los totales
	 * @param filename
	 * @param cuentasCobroForm
	 * @param medico
	 * @param con
	 */
	private static Boolean  pdfResumidoCxC(String filename, CuentasCobroForm cuentasCobroForm, UsuarioBasico medico, Connection con, HttpServletRequest request)
	{
		PdfReports report = new PdfReports();
		CuentasCobro mundoCxC=new CuentasCobro();
		String[] dataDetalleCuenta;
		ParametrizacionInstitucion p = new ParametrizacionInstitucion();
		p.cargar(con, medico.getCodigoInstitucionInt());
		//HashMap detalleCuentaHashMap;
		
		double cuenta_cobro=Double.parseDouble(cuentasCobroForm.getMapMovimientos("codigoCXC")+"");
		mundoCxC.cargarCuentaCobroCompleta(con,cuenta_cobro,medico.getCodigoInstitucionInt());
		cuentasCobroForm.setNumCuentaCobro(cuenta_cobro);
		cuentasCobroForm.setDetalleFacturas(mundoCxC.getFacturas());
		cuentasCobroForm.setMaxPageItems(mundoCxC.getFacturas().size());
		
		//Iterator it;
		try
		{	
			//Cargamos desde el mundo la cuenta de cobro completa con su informacion y las facturas que a ella le corresponden
			if (mundoCxC.cargarResumenImpresion(con, cuenta_cobro).size()==0)
			{
			    return false;
			}
		
				else
				{
					//it=mundoCxC.cargarResumenImpresion(con, cuenta_cobro).iterator();
				    //detalleCuentaHashMap=(HashMap)it.next();
					mundoCxC.cargarResumenImpresion(con, cuenta_cobro);
				    
				    dataDetalleCuenta=comunCabezoteCxC(mundoCxC, cuentasCobroForm, con,medico);
					
					String[] headerResumen={
							"Vía de Ingreso",
							"Valor Facturado"
						};
					String[] dataResumido=resumidoCxC(mundoCxC,con,cuentasCobroForm,medico.getCodigoInstitucionInt());
					
//					String dataTotal="$ "+UtilidadTexto.formatearValores(mundoCxC.getValInicialCuenta()+"",2,true,true);
					String dataTotal="  "+UtilidadTexto.formatearValores(mundoCxC.getValInicialCuenta()+"");
					String dataTotalLetras=UtilidadN2T.convertirLetras(UtilidadTexto.formatearExponenciales(mundoCxC.getValInicialCuenta()), ConstantesBD.cadenaUnidades,ConstantesBD.cadenaUnidadesDecimales);
					
					//Parte superior de la impresion con la informacion de la intitucion
					String tituloReporte="RESUMEN DE CARTERA"+"(" +UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() +")";
					
					InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
					report.setReportBaseHeaderCxC1(institucionBasica.getLogoReportes(), institucionBasica.getRazonSocial(), institucionBasica.getNit(), tituloReporte, institucionBasica.getDireccion(), institucionBasica.getTelefono());
				    
				    report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
				    report.openReport(filename);
		
				    report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
				    report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000, 14);
				    report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 12);
				    
				    
				    //El cabezote de la impresion con todos los datos de la cuenta de cobro
				    report.createSection("detalleCxC","tablaCuenta",5,4,0);
		            report.getSectionReference("detalleCxC").setTableBorder("tablaCuenta", 0xFFFFFF, 0.0f);
		            report.getSectionReference("detalleCxC").setTableCellBorderWidth("tablaCuenta", 0.5f);
		            report.getSectionReference("detalleCxC").setTableCellsDefaultColors("tablaCuenta", 0xFFFFFF, 0xFFFFFF);
		            report.font.setFontSizeAndAttributes(8,true,false,false);
		            report.getSectionReference("detalleCxC").setTableCellsColSpan(1);
		
		            report.getSectionReference("detalleCxC").addTableTextCell("tablaCuenta", "Cuenta de Cobro No.: "+UtilidadTexto.formatearValores(dataDetalleCuenta[0],"0"), report.font);
		            report.getSectionReference("detalleCxC").addTableTextCell("tablaCuenta", "Estado: "+dataDetalleCuenta[1], report.font);
		            report.getSectionReference("detalleCxC").addTableTextCell("tablaCuenta", "Fecha Generación: "+UtilidadFecha.conversionFormatoFechaAAp(dataDetalleCuenta[2]), report.font);
		            if (!dataDetalleCuenta[3].equals(""))
		            {
		            	report.getSectionReference("detalleCxC").addTableTextCell("tablaCuenta", "Fecha Radicación:  "+UtilidadFecha.conversionFormatoFechaAAp(dataDetalleCuenta[3]), report.font);
		            	report.getSectionReference("detalleCxC").setTableCellsColSpan(2);
		                report.getSectionReference("detalleCxC").addTableTextCell("tablaCuenta", "Convenio:  "+dataDetalleCuenta[4], report.font);
		            }
		            else
		            {
		            	report.getSectionReference("detalleCxC").setTableCellsColSpan(1);
		            	report.getSectionReference("detalleCxC").addTableTextCell("tablaCuenta", " ", report.font);
		            	report.getSectionReference("detalleCxC").setTableCellsColSpan(2);
		            	report.getSectionReference("detalleCxC").addTableTextCell("tablaCuenta", "Convenio:  "+dataDetalleCuenta[4], report.font);
		            }
		            if((mundoCxC.convenioxCuentaCobro(con,cuenta_cobro))>0)
		            {
		             report.getSectionReference("detalleCxC").setTableCellsColSpan(2);
		             report.getSectionReference("detalleCxC").addTableTextCell("tablaCuenta", "Nit: "+mundoCxC.getNitConvenio(), report.font);
		            }
		            report.getSectionReference("detalleCxC").setTableCellsColSpan(4);
		            report.getSectionReference("detalleCxC").addTableTextCell("tablaCuenta", "Periódo Facturacion:"+UtilidadFecha.conversionFormatoFechaAAp(dataDetalleCuenta[7])+"  a   "+UtilidadFecha.conversionFormatoFechaAAp(dataDetalleCuenta[6]), report.font);
		            if(!dataDetalleCuenta[8].equals(""))
		            {
		            	report.getSectionReference("detalleCxC").setTableCellsColSpan(4);
		            	report.getSectionReference("detalleCxC").addTableTextCell("tablaCuenta", "Observaciones:		"+dataDetalleCuenta[8], report.font);
		            }
		            else
		            {
		            	report.getSectionReference("detalleCxC").setTableCellsColSpan(4);
		            	report.getSectionReference("detalleCxC").addTableTextCell("tablaCuenta", " ", report.font);
		            }
		            
		
		            report.setReportDataSectionAtributes(0xFFFFFF, 0xFFFFFF, 0x000000,8);
		            //definicion de fuente
		            report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		            //definicion de atributos para el titulo de la seccion(tabla)
		            report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000,8);
		            //definicion de atributos para los datos de la seccion
		            report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000,8);
		            report.addSectionToDocument("detalleCxC");
				    
				    //Añadimos la sección de detalle de la cuenta
		            //Ahora añadimos el listado de las solicitud
				    if ((mundoCxC.cargarResumenImpresion(con, cuenta_cobro).size())>0)
					{
					    report.createColSection("listadoFacturas", headerResumen, dataResumido,8);
					}
					else
					{
					    String arreglo1[]={"Solicitudes"}, arreglo2[]={" No existen Facturas para el Criterio Específicado"};  
					    report.createColSection("listadoFacturas", arreglo1, arreglo2, 8);
					}
				    
				   
				    float widths[]={(float)5.0,(float)5.0};
			        try {
			        		report.getSectionReference("listadoFacturas").getTableReference(PdfReports.REPORT_PARENT_TABLE).setWidths(widths);
			             }
			        catch (BadElementException e)
		                {
			        	cuentasCobroPdfLogger.error("Se presentó error generando el pdf de las Cuentas de Cobro en el modo resumido " +e);
		                }
			        report.getSectionReference("listadoFacturas").getTableReference(PdfReports.REPORT_PARENT_TABLE).setAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
				    report.addSectionToDocument("listadoFacturas");
				    
				    //Seccion de los Totales
				    report.createSection("totales","totalesCxC",1,2,0);
				    report.getSectionReference("totales").setTableBorder("totalesCxC", 0xFFFFFF, 0.0f);
		            report.getSectionReference("totales").setTableCellBorderWidth("totalesCxC", 0.5f);
		            report.getSectionReference("totales").setTableCellsDefaultColors("totalesCxC", 0xFFFFFF, 0xFFFFFF);
		            report.font.setFontSizeAndAttributes(10,true,false,false);
		            report.getSectionReference("totales").setTableCellsColSpan(1);
		            report.getSectionReference("totales").addTableTextCellAligned("totalesCxC", "Total Facturado:   ", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
		            report.getSectionReference("totales").addTableTextCellAligned("totalesCxC", ""+dataTotal , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		            report.addSectionToDocument("totales");
		            
		            //Totales en letras
		            report.createSection("totalesLetras","totalesCxCLetras",1,2,0);
				    report.getSectionReference("totalesLetras").setTableBorder("totalesCxCLetras", 0xFFFFFF, 0.0f);
		            report.getSectionReference("totalesLetras").setTableCellBorderWidth("totalesCxCLetras", 0.5f);
		            report.getSectionReference("totalesLetras").setTableCellsDefaultColors("totalesCxCLetras", 0xFFFFFF, 0xFFFFFF);
		            report.font.setFontSizeAndAttributes(10,true,false,false);
		            report.getSectionReference("totalesLetras").setTableCellsColSpan(2);
		            report.getSectionReference("totalesLetras").addTableTextCellAligned("totalesCxCLetras", "Valor en Letras:   "+dataTotalLetras+"  Moneda Corriente", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		            report.addSectionToDocument("totalesLetras");
		            
		   
		            
		            //Firma de Cartera en un parrafo para poder disponer bien del espacio
//		            report.document.addParagraph("_________________________________________", report.font,iTextBaseDocument.ALIGNMENT_LEFT,80);
//		            report.document.addParagraph("Firma Cartera", report.font,iTextBaseDocument.ALIGNMENT_LEFT,20);
		            
		          //Anexo 992
		            iTextBaseTable section=new iTextBaseTable();
		    	    agregarFirmasValoresXDefecto(cuentasCobroForm,medico.getCodigoInstitucionInt(), ConstantesIntegridadDominio.acronimoSuba, section, report);
		    	    
		            if (ValoresPorDefecto.getImprimirFirmasEnImpresionCC(medico.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoSi))
		            {
//		            	report.document.addParagraph("________________________________________________", report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
//		            	report.document.addParagraph("Dr. Edgar Silvio Sanchez Villegas", report.font,iTextBaseDocument.ALIGNMENT_LEFT,12);
//		            	report.document.addParagraph("Gerente Hospital de Suba II nivel E.S.E", report.font,iTextBaseDocument.ALIGNMENT_LEFT,11);


		            	report.document.addParagraph("Elaboró_________________________________________", report.font,iTextBaseDocument.ALIGNMENT_LEFT,32);
		            	report.document.addParagraph("Revisó__________________________________________", report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);
		            	report.document.addParagraph("Aprobó__________________________________________", report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);

		            	report.document.addParagraph( dataDetalleCuenta[4].toUpperCase()+" Nit: "+mundoCxC.getNitConvenio(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,30);
		            	report.document.addParagraph( "Dirección: "+mundoCxC.getDireccionConvenio().toUpperCase()+" "+ " Teléfono : "+mundoCxC.getTelefonoConvenio(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
		            }
		    	    //Anexo 992
		            
		            //Cerramos el reporte
				    report.closeReport(); 
				    return true;
				}
		}
		 
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return false;
      }
    
    /**
	 * Método que ayuda a generar el PDF de las cuentas de cobro
	 * carga la tabla del formato de impresion  detallado
	 * @param mundoCxC mundo de CuentasCobro
	 * @return
	 */
	private static String[] comunListadoFacturas(CuentasCobro mundoCxC)
	{

		String[] dataListadoFacturas;
		int i=0;
		ArrayList resultados=new ArrayList();
		double valorFactura;
		DtoFactura facturas;
		//Coleccion de todas las facturas de una cuenta de cobro con los datos necesarios para la impresion
		// Incidencia no se estabateniendo en cuenta los ajustes al mommento de presentar el valor de las facturas
	Connection con=null;
	try{
		String tipoBD = System.getProperty("TIPOBD");
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
		con = myFactory.getConnection();
	}
	catch(Exception e)
	{
		cuentasCobroPdfLogger.error("Se presentó al conectar a la Base de Datos " +e);
		return null;
	} 

		Object[] oldcol=mundoCxC.getFacturas().toArray();
		DtoFactura tempcol= new DtoFactura();
		HashMap ajusta= new HashMap();
		Collection <DtoFactura> newcol= new ArrayList();
				
		for (int m=0; m<mundoCxC.getFacturas().size(); m++)
		{
			String busqAjustes="SELECT DISTINCT " +
					" ae.tipo_ajuste as tipo_ajuste, " +
					" getNombreTipoAjuste(ae.tipo_ajuste) as nombre_tipo_ajuste, " +
					" ae.valor_ajuste as valor_ajuste " +
					" from ajustes_empresa ae " +
	" left outer join ajus_fact_empresa afe on (afe.codigo=ae.codigo) " +
	" left outer join cuentas_cobro cc on (cc.numero_cuenta_cobro=ae.cuenta_cobro and cc.institucion=ae.institucion)  " +
	" left outer join facturas f on (f.codigo=afe.factura) " +
	" where ae.institucion=2 ";
		tempcol= (DtoFactura) oldcol[m];
		tempcol.getConsecutivoFactura();
		busqAjustes += " AND ae.cuenta_cobro is null and  f.consecutivo_factura ="+tempcol.getConsecutivoFactura();
		if(tempcol.getAjustesCredito()==-1.0 && tempcol.getAjustesDebito()==-1.0 )
		{ 
			try{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(busqAjustes));
				ajusta=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
				ps.close();
				try{
					double valorT=tempcol.getValorTotal();
					for (int j=0; j<Integer.parseInt(ajusta.get("numRegistros").toString()+"");j++)
					{
						if(Integer.parseInt(ajusta.get("tipo_ajuste_"+j+"").toString())==2 || Integer.parseInt(ajusta.get("tipo_ajuste_"+j+"").toString())==4)
						{
							valorT=valorT-Double.parseDouble(ajusta.get("valor_ajuste_"+j).toString());
						}
						if(Integer.parseInt(ajusta.get("tipo_ajuste_"+j+"").toString())==1 || Integer.parseInt(ajusta.get("tipo_ajuste_"+j+"").toString())==3)
						{
							valorT=valorT+Double.parseDouble(ajusta.get("valor_ajuste_"+j).toString());
						}
					}
					tempcol.setValorCartera(valorT);
					
				}
				catch(Exception e){
					cuentasCobroPdfLogger.error("Se presentó error generando haciendo la iteracion de los ajustes de facturas " +e);
				}
			}
			catch(Exception e){
				cuentasCobroPdfLogger.error("Se presentó error buscando los ajustes de las facturas " +e);
			}
		
		}
		newcol.add(tempcol);
		}
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			cuentasCobroPdfLogger.error("Se presentó al cerrar la Base de Datos " +e);
		}
		mundoCxC.setFacturas(newcol);
		Iterator it=mundoCxC.getFacturas().iterator();
		while (it.hasNext())
		{
		    facturas=(DtoFactura)it.next();

	    	resultados.add(i, (Utilidades.convertirAEntero((facturas.getConsecutivoFactura())+"")+""));
	    	resultados.add(i+1, (facturas.getCentroAtencion().getNombre()+""));
    		resultados.add(i+2,facturas.getNombrePaciente());
	    	resultados.add(i+3, facturas.getViaIngreso().getNombre());
	    	valorFactura=facturas.getValorCartera();
		    if (valorFactura>0)
		    {
		    	resultados.add(i+4, "  "+ UtilidadTexto.formatearValores(valorFactura+""));
		    }
		    i=i+5;
		}
		dataListadoFacturas=new String[resultados.size()];
		UtilidadTexto.copiarArrayListAArreglo(resultados, dataListadoFacturas);
		return dataListadoFacturas;
	}

	
	
	/**
	 * Método que almacena toda la funcionalidad 
	 * necesaria para generar la parte común a las
	 * impresiones con respecto a la cuenta de cobro
	 * El cabezote de la impresion detallada y resumida
	 * @param mundoCxC
	 * @param cxcForm
	 * @param con
	 * @param medico
	 * @return
	 */
	private static String[] comunCabezoteCxC (CuentasCobro mundoCxC, CuentasCobroForm cxcForm, Connection con, UsuarioBasico medico)
	{
		ArrayList resultadosDetalleCxC=new ArrayList();
		int i=0;

		String dato="";
		//Cargamos desde el mundo la cuenta de cobro completa con su informacion y las facturas que a ella le corresponden
		if(mundoCxC.cargarCuentaCobroCompleta( con, cxcForm.getNumCuentaCobro(),medico.getCodigoInstitucionInt()))
		{
				/**Observaciones de la Cuenta de Cobro**/
				if(!UtilidadTexto.isEmpty(mundoCxC.getObservacionesGen()) &&  !mundoCxC.getObservacionesGen().equals(""))
				{
					resultadosDetalleCxC.add(i, mundoCxC.getObservacionesGen());//Observaciones de en generacion de la Cuenta de cobro
				}
				else
				{
					resultadosDetalleCxC.add(i, dato);
				}
				resultadosDetalleCxC.add(i, mundoCxC.getFechaInicial());//Fecha Inicial de la Cuenta de Cobro
				resultadosDetalleCxC.add(i, mundoCxC.getFechaFinal());//Fecha Inicial de la Cuenta de Cobro
				resultadosDetalleCxC.add(i, (mundoCxC.getValInicialCuenta()+""));//Valor de la Cuenta de Cobro
				resultadosDetalleCxC.add(i, mundoCxC.getConvenio().getNombre());//El convenio de la Cuenta de Cobro
				
				/**Fecha de Radiacion de la Cuenta de Cobro**/
				if(!mundoCxC.getFechaRadicacion().equals(""))
				{
					resultadosDetalleCxC.add(i, mundoCxC.getFechaRadicacion());//Fecha de Radicacion de la Cuenta de Cobro
				}
				else
				{
					resultadosDetalleCxC.add(i, dato);
				}
				resultadosDetalleCxC.add(i, mundoCxC.getFechaElaboracion());//Fecha de Elaboracion de la Cuenta de Cobro
				
				/**Estado de la Cuenta de Cobro**/
				String estado="";
				if(mundoCxC.getEstado()==1)
				{
					estado="Generado";
				}
				if(mundoCxC.getEstado()==2)
				{
					estado="Anulado";
				}
				if(mundoCxC.getEstado()==3)
				{
					estado="Aprobado";
				}
				if(mundoCxC.getEstado()==4)
				{
					estado="Radicada";
				}
				resultadosDetalleCxC.add(i, estado);
				/**Número de la Cuenta de Cobro**/
				resultadosDetalleCxC.add(i, (mundoCxC.getNumeroCuentaCobro()+""));
		}
		
		String dataCabezoteCxC []= new String[resultadosDetalleCxC.size()];
			
		UtilidadTexto.copiarArrayListAArreglo(resultadosDetalleCxC, dataCabezoteCxC);
		return dataCabezoteCxC;
	}
	

	

	/**
	 * Método que carga los datos necesarios para impresion resumida de una cuenta de cobro
	 * agrupando los totales por via de ingreso
	 * @param mundoCxC
	 * @param con
	 * @param cxcForm
	 * @param institucion
	 * @return
	 */
	private static String[] resumidoCxC (CuentasCobro mundoCxC, Connection con, CuentasCobroForm cxcForm, int institucion)
	{
	   
		String[] dataResumidCxC;
		HashMap filaHashMap;
		int i=0;
		ArrayList resultados=new ArrayList();
		Double valor;
		
		double cuenta_cobro=Double.parseDouble(cxcForm.getMapMovimientos("codigoCXC")+"");
		mundoCxC.cargarCuentaCobroCompleta(con,cuenta_cobro,institucion);
		cxcForm.setNumCuentaCobro(cuenta_cobro);
		cxcForm.setDetalleFacturas(mundoCxC.getFacturas());
		cxcForm.setMaxPageItems(mundoCxC.getFacturas().size());
		
		//Incidencia 5072  no se tenian en cuenta los valores de los ajustes a facturas al traer el valor de la via de ingreso
		HashMap ajust = new HashMap();
		String busqAjustes=" SELECT f.numero_cuenta_cobro, f.codigo as codFactura, dfa.codigo  AS codigoAjuste, dfa.valor_ajuste as valor,"+
  "ae.tipo_ajuste as concepto, manejopaciente.getnombreviaingreso(dm.via_ingreso_codigo) as viaingreso"+
 " FROM facturacion.facturas f "+
 "INNER JOIN manejopaciente.sub_cuentas sc on (f.sub_cuenta=sc.sub_cuenta) "+
 "INNER JOIN facturacion.detalle_monto dm ON (dm.detalle_codigo=sc.monto_cobro)"+
 " INNER JOIN CARTERA.ajus_fact_empresa dfa ON (dfa.factura=f.codigo)" +
 " INNER JOIN CARTERA.ajustes_empresa ae ON (ae.codigo=dfa.codigo)"+
  " WHERE f.numero_cuenta_cobro="+cuenta_cobro+" ";
		try {
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(busqAjustes));
			ajust=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			ps.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String viaIngreso;
		
		try
		{
			//Cargamos la coleccion del munod donde se guardo la informacion de una consulta a la base  de datos
			//con todos los datos necesarios para la impresion de una cuenta de cobro resumida
			Iterator it=mundoCxC.cargarResumenImpresion(con, cuenta_cobro).iterator();
			while (it.hasNext())
			{
				filaHashMap=(HashMap)it.next();
				
				//Incidencia 5072
				for (int m=0; m<Integer.parseInt(ajust.get("numRegistros").toString()); m++)
				{
					if(((String)filaHashMap.get("viaingreso")).equals(ajust.get("viaingreso_"+m).toString()) && (String.valueOf(filaHashMap.get("numerocxc"))).equals(ajust.get("numero_cuenta_cobro_"+m).toString()))
					{
						double valora=Utilidades.convertirADouble(filaHashMap.get("valor_via_ingreso")+"");
						if(Integer.parseInt(ajust.get("concepto_"+m+"").toString())==2 || Integer.parseInt(ajust.get("concepto_"+m+"").toString())==4)
						{
							valora=valora-Double.parseDouble(ajust.get("valor_"+m).toString());
						}
						if(Integer.parseInt(ajust.get("concepto_"+m+"").toString())==1 || Integer.parseInt(ajust.get("concepto_"+m+"").toString())==3)
						{
							valora=valora+Double.parseDouble(ajust.get("valor_"+m).toString());
						}
						filaHashMap.put("valor_via_ingreso", valora);
						
					}
					
				}
				
				
				viaIngreso=(String)filaHashMap.get("viaingreso");
				if(viaIngreso!=null)
			    {
			    	resultados.add(i, filaHashMap.get("viaingreso"));
			    }
				else
			    {
			    	resultados.add(i,""); 
			    }
				
				
				valor=Utilidades.convertirADouble(filaHashMap.get("valor_via_ingreso")+"");
				if (valor!=null&&valor!=ConstantesBD.codigoNuncaValidoDoubleNegativo)
			    {
					resultados.add(i+1, "  "+ UtilidadTexto.formatearValores(valor.doubleValue()+""));
			    }
				else
			    {
			    	resultados.add(i+1," "+UtilidadTexto.formatearValores(0)+"");
			    }
				i=i+2;
			   
			}
			dataResumidCxC=new String[resultados.size()];
			UtilidadTexto.copiarArrayListAArreglo(resultados, dataResumidCxC);
			return dataResumidCxC;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		
	}

	/**
	 * Método implementado para realizar la impresion de cuentas de cobro cartera
	 * @param cxcForm
	 * @param usuario
	 * @param con
	 * @param mundoCxC
	 * @param nombreArchivo1
	 * @param nombreArchivo2
	 * @param request 
	 */
	public static String imprimir(CuentasCobroForm cxcForm, UsuarioBasico usuario, Connection con, CuentasCobro mundoCxC, String nombreArchivo1, String nombreArchivo2, HttpServletRequest request) 
	{
		//-- Consultar la informacion de la Empresa.
        ParametrizacionInstitucion ins=new ParametrizacionInstitucion();
    	ins.cargar(con, usuario.getCodigoInstitucionInt());
    	//--- Tipo de Formato
    	String formato = ins.getFormatoImpresion();
    	InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");


    	//Segun el formato se realiza cierto tipo de impresion
    	if(formato.equals(ConstantesIntegridadDominio.acronimoSasaima))
    	{
    		/************************************************SASAIMA********************************************/
    		pdfReporteSasaima(cxcForm,institucionBasica,nombreArchivo1,nombreArchivo2);
    		/***************************************************************************************************/
    		
    	}
    	else if(formato.equals(ConstantesIntegridadDominio.acronimoSuba))
    	{
    		/************************************************SUBA********************************************/
    		pdfReporteSuba(cxcForm,institucionBasica,nombreArchivo1,nombreArchivo2);
    		/***************************************************************************************************/
    	}
    	else
    	{
    		/************************************************ESTANDAR********************************************/
    		pdfDetalleCxC(nombreArchivo1, cxcForm, usuario, con, mundoCxC, request);
    		//No en todas las funcionalidades se está llamando la impresion resumida
    		if(nombreArchivo2!=null)
    			pdfResumidoCxC(nombreArchivo2,cxcForm, usuario, con, request);
    		/**********************************************************************************************/
    	}
    	
    	return formato;
		
	}

	
	/**
	 * @param con
	 * @param mundoCxC
	 * @param cuentasCobroForm
	 * @return Si existe Reporte de resumen 
	 * @throws SQLException
	 */
	public static Boolean existeReporteResumen(Connection con,CuentasCobro mundoCxC,CuentasCobroForm cuentasCobroForm) {
		
		Double cuenta_cobro=Double.parseDouble(cuentasCobroForm.getMapMovimientos("codigoCXC")+"");
		try {
			if(mundoCxC.cargarResumenImpresion(con, cuenta_cobro).size()==0){
				return false;
			}else{
				return true;
			}
		} catch (SQLException e) {
			cuentasCobroPdfLogger.error("Error Consultando resumen de impresion : "+e);
		}
		return false;
	}
	
	 /**
	 * @param mundoCxC
	 * @return Si existe reporte Detallado
	 */
	public static Boolean existeReporteDetallado( CuentasCobro mundoCxC){
		if (mundoCxC.getFacturas().size()==0)
		{
		    return false;
		}else{
			return true;
		}
	}
	
	
	
	
	/**
	 * Método implementado para generar el reporte de cuentas de cbro en el formato de suba
	 * @param cxcForm
	 * @param ins
	 * @param filename
	 * @param filename
	 */
	private static void pdfReporteSuba(CuentasCobroForm cxcForm, InstitucionBasica ins, String filename, String filename2) 
	{
		//-- Variable para manejar el reporte
        PdfReports report = new PdfReports();
        //se asigna el formato de la impresion
        report.setFormatoImpresion(ConstantesIntegridadDominio.acronimoSuba);
        
        //Se toma ruta del logo
        String filePath=ValoresPorDefecto.getFilePath();
        String filePathLogo = ValoresPorDefecto.getFilePath(); //path para el logo de Bogotá sin indiferencia
	    if(System.getProperty("file.separator").equals("/"))
	    {
	    	filePath=filePath.substring(0, filePath.lastIndexOf("/", (filePath.length()-2)))+"/imagenes/logo-grey.gif";
	    	filePathLogo=filePathLogo.substring(0, filePathLogo.lastIndexOf("/",(filePathLogo.length()-2)))+"/imagenes/bogsin2.gif";
	    }
	    else
	    {
	    	filePath=filePath.substring(0, filePath.lastIndexOf("\\", (filePath.length()-2)))+"\\imagenes\\logo-grey.gif";
	    	filePathLogo=filePathLogo.substring(0, filePathLogo.lastIndexOf("\\",(filePathLogo.length()-2)))+"\\imagenes\\bogsin2.gif";
	    }
	    
	    //Se asignan los datos del pie de pagina
        report.setMapaPiePagina("filePathLogo", filePathLogo);
        report.setMapaPiePagina("infoInstitucion", "Dirección: "+ins.getDireccion()+" Conmutador: "+ins.getTelefono());
	        
        
        //Se genera el encabezado de la impresion
		report.setReportBaseHeaderSubaEvento(filePath,ins,UtilidadTexto.formatearValores(cxcForm.getNumCuentaCobro(),"0"),cxcForm.getFechaElaboracion());
		
		//Se consulta informacion de la cuenta de cobro
		
		//Se abre el reporte
        report.openReport(filename);
        report.font.useFont(iTextBaseFont.FONT_TIMES, true, true, true);
        iTextBaseTable section;
        
        
	    //***********************SECCION DESCRIPCION Y VALOR TOTAL**************************************************************
	    section = report.createSection("descripcion", "descripcionTable", 4, 1, 10);
  		section.setTableBorder("descripcionTable", 0xFFFFFF, 0.0f);
	    section.setTableCellBorderWidth("descripcionTable", 0.5f);
	    section.setTableCellPadding("descripcionTable", 1);
	    section.setTableSpaceBetweenCells("descripcionTable", 0.5f);
	    section.setTableCellsDefaultColors("descripcionTable", 0xFFFFFF, 0xFFFFFF);
	    
	    report.font.setFontAttributes(0x000000, 10, true, false, false);
	    section.addTableTextCellAligned("descripcionTable", "Descripción:", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 10, false, false, false);
	    section.setTableCellsDefaultColors("descripcionTable", 0xFFFFFF, 0x000000);
	    if(!UtilidadTexto.isEmpty(cxcForm.getObservaciones())){
	    	section.addTableTextCellAligned("descripcionTable", cxcForm.getObservaciones(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    }else{
	    	section.addTableTextCellAligned("descripcionTable", "", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    }
	    section.setTableCellsDefaultColors("descripcionTable", 0xFFFFFF, 0xFFFFFF);
	    report.font.setFontAttributes(0x000000, 11, true, false, false);
	    section.addTableTextCellAligned("descripcionTable", "", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.addTableTextCellAligned("descripcionTable", "Son: "+UtilidadN2T.convertirLetras(cxcForm.getValorInicialCuenta(),ConstantesBD.cadenaUnidades,ConstantesBD.cadenaUnidadesDecimales)+" MCTE...$"+UtilidadTexto.formatearValores(cxcForm.getValorInicialCuenta()), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    //se añade la sección al documento
	    report.addSectionToDocument("descripcion");
	    //*****************************************************************************************************************
	    
	    //**************FIRMAS E INFORMACION DEL CAMPO********************************************************************
//	    	    section = report.createSection("firmas", "firmasTable", 7, 2, 10);
//  		section.setTableBorder("firmasTable", 0xFFFFFF, 0.0f);
//	    section.setTableCellBorderWidth("firmasTable", 0.5f);
//	    section.setTableCellPadding("firmasTable", 1);
//	    section.setTableSpaceBetweenCells("firmasTable", 0.5f);
//	    section.setTableCellsDefaultColors("firmasTable", 0xFFFFFF, 0xFFFFFF);
//	    
//	    report.font.setFontAttributes(0x000000, 10, true, false, false);
//	    section.setTableCellsColSpan(2);
//	    section.addTableTextCellAligned("firmasTable", "_____________________________", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
//	    section.addTableTextCellAligned("firmasTable", "Dr. Pedro Nel Hernadez Laguna", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
//	    section.addTableTextCellAligned("firmasTable", "Coordinador Financiero", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
//	    section.addTableTextCellAligned("firmasTable", "                    ", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
//	    section.addTableTextCellAligned("firmasTable", "                      ", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
//	    report.font.setFontAttributes(0x000000, 10, false, false, false);
//	    section.setTableCellsColSpan(1);
	    //se inserta informacion del convenio
	    String[] vecConvenio = cxcForm.getConvenio().split(ConstantesBD.separadorSplit);
//	    section.addTableTextCellAligned("firmasTable", (vecConvenio.length>1?vecConvenio[1]:cxcForm.getConvenio()), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
//	    section.addTableTextCellAligned("firmasTable", "Nit: "+cxcForm.getNitConvenio(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
//	    section.addTableTextCellAligned("firmasTable", "Dirección: "+cxcForm.getDireccionConvenio(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
//	    section.addTableTextCellAligned("firmasTable", "Teléfono: "+cxcForm.getTelefonoConvenio(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
//	    
////	    //se añade la sección al documento
//	    report.addSectionToDocument("firmas");
//	    
//	  //Anexo 992
//	    agregarFirmasValoresXDefecto(cxcForm, Utilidades.convertirAEntero(ins.getCodigo()), ConstantesIntegridadDominio.acronimoSuba, section, report);
//	    //Anexo 992
	    
	    
	    //*****************************************************************************************************************
	    
	    
	    
	    report.closeReport();
	    
	    //Se verifica si se debe imprimir los anexos
	    if(cxcForm.isImpresionAnexos())
	    {
	    	//******************************************************************************************************************
	    	//*****************************IMPRESION DE ANEXOS******************************************************************
	    	//*****************************************************************************************************************
	    	//Se genera nuevo reporte
	    	report = new PdfReports();
	    	report.setReportBaseHeaderSubaAnexos(filePath,ins,UtilidadTexto.formatearValores(cxcForm.getNumCuentaCobro(),"0"),(vecConvenio.length>1?vecConvenio[1]:cxcForm.getConvenio()));
	    	report.openReport(filename2);
	    	report.font.useFont(iTextBaseFont.FONT_TIMES, true, true, true);
	    	
	    	
	    	HashMap mapaFacturas = ordenarDetalleFacturas(cxcForm);
	    	
		    int numRegistros = Integer.parseInt(mapaFacturas.get("numRegistros").toString());
		    int codigoCentroAnterior = 0;
		    double subtotales = 0;
		    double total = 0;
		    
		    //*****************SECCION DETALLE************************************************************************
		    for(int i=0;i<numRegistros;i++)
		    {
		    	//si el centro de atencion es diferente se crea una nueva sección
		    	if(codigoCentroAnterior!=Integer.parseInt(mapaFacturas.get("codigo_centro_atencion_"+i).toString()))
		    	{
		    		if(codigoCentroAnterior!=0)
		    		{
		    			//Se agrega sumatoria subtotales
		    			report.font.setFontAttributes(0x000000, 8, false, false, false);
		    			section.setTableCellsColSpan(6);
		    			total += subtotales;
		    			section.addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, "Subtotales Centro Atención "+mapaFacturas.get("nombre_centro_atencion_"+(i-1)), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    			section.setTableCellsColSpan(1);
		    			section.addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, UtilidadTexto.formatearValores(subtotales), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
		    			
		    			//se finaliza la sección anterior
		    		    report.addSectionToDocument("detalleAnexos_"+codigoCentroAnterior);
		    		}
		    		
		    		subtotales = 0;
		    		codigoCentroAnterior = Integer.parseInt(mapaFacturas.get("codigo_centro_atencion_"+i).toString()); 
		    		float widths[]={(float) 1,(float) 1, (float) 3.45,(float) 1.35, (float) 1, (float) 1.2, (float) 1};
		    		int numFacXCentro = obtenerNumeroFacturasXCentro(mapaFacturas,codigoCentroAnterior);
		    		
		    		//Se crea una nueva sección
		    		section = report.createSection("detalleAnexos_"+codigoCentroAnterior, PdfReports.REPORT_PARENT_TABLE, numFacXCentro+3, 7, 8);
			  		section.setTableBorder(PdfReports.REPORT_PARENT_TABLE, 0xFFFFFF, 0.5f);
			  		
				    section.setTableCellBorderWidth(PdfReports.REPORT_PARENT_TABLE, 0.5f);
				    section.setTableCellPadding(PdfReports.REPORT_PARENT_TABLE, 1);
				    section.setTableSpaceBetweenCells(PdfReports.REPORT_PARENT_TABLE, 0.5f);
				    section.setTableCellsDefaultColors(PdfReports.REPORT_PARENT_TABLE, 0xFFFFFF, 0xFFFFFF);
				    report.setReportTitleSectionAtributes(0xFFFFFF, 0xFFFFFF,0x000000, 10);
				    report.font.setFontAttributes(0x000000, 10, true, false, false);
				    
				    //Se añade el titutlo de la sección
				    report.addSectionTitle("detalleAnexos_"+codigoCentroAnterior, PdfReports.REPORT_PARENT_TABLE, mapaFacturas.get("nombre_centro_atencion_"+i).toString());
				    
				    //Se asignan los encabezados de la tabla
				    section.addTableTextCellHeaderAligned(PdfReports.REPORT_PARENT_TABLE, "Factura", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,1,0);
				    section.addTableTextCellHeaderAligned(PdfReports.REPORT_PARENT_TABLE, "Fecha Atención", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,1,1);
				    section.addTableTextCellHeaderAligned(PdfReports.REPORT_PARENT_TABLE, "Paciente", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,1,2);
				    section.addTableTextCellHeaderAligned(PdfReports.REPORT_PARENT_TABLE, "Identificación", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,1,3);
				    section.addTableTextCellHeaderAligned(PdfReports.REPORT_PARENT_TABLE, "N° Póliza", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,1,4);
				    section.addTableTextCellHeaderAligned(PdfReports.REPORT_PARENT_TABLE, "Servicio", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,1,5);
				    section.addTableTextCellHeaderAligned(PdfReports.REPORT_PARENT_TABLE, "Valor", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,1,6);
				    
				    section.getTableReference(PdfReports.REPORT_PARENT_TABLE).endHeaders();
				    
				    try 
		            {
						section.getTableReference(PdfReports.REPORT_PARENT_TABLE).setWidths(widths);
					} 
		            catch (BadElementException e) 
		            {
						cuentasCobroPdfLogger.error("Error al ajustar los anchos de la tabla de UPC: "+e);
					}
		    	}
		    	
		    	report.font.setFontAttributes(0x000000, 8, false, false, false);
		    	section.addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, UtilidadTexto.formatearValores(mapaFacturas.get("factura_"+i)+"","0"), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    section.addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, mapaFacturas.get("fecha_"+i)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    section.addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, mapaFacturas.get("paciente_"+i)+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);			    
			    section.addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, mapaFacturas.get("identificacion_"+i)+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    section.addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, mapaFacturas.get("poliza_"+i)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    section.addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, mapaFacturas.get("servicio_"+i)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    subtotales += Double.parseDouble(mapaFacturas.get("valor_"+i).toString());
			    section.addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, UtilidadTexto.formatearValores(mapaFacturas.get("valor_"+i).toString()), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
			    
		    	
		    }
	    	
		    if(numRegistros>0)
		    {
		    	//Se agrega sumatoria subtotales
				report.font.setFontAttributes(0x000000, 8, false, false, false);
				section.setTableCellsColSpan(6);
				total += subtotales;
				section.addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, "Subtotales Centro Atención "+mapaFacturas.get("nombre_centro_atencion_"+(numRegistros-1)), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
				section.setTableCellsColSpan(1);
				section.addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, UtilidadTexto.formatearValores(subtotales), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
				
				//se finaliza la sección anterior
			    report.addSectionToDocument("detalleAnexos_"+codigoCentroAnterior);
		    }
		   //***********************************************************************************************************
		    
		    //*******************SECCION TOTAL RELACIÓN DE ENVÍO***********************************************************
		    section = report.createSection("totales", PdfReports.REPORT_PARENT_TABLE, 1, 2, 2);
	  		section.setTableBorder(PdfReports.REPORT_PARENT_TABLE, 0xFFFFFF, 0.5f);
		    section.setTableCellBorderWidth(PdfReports.REPORT_PARENT_TABLE, 0.5f);
		    section.setTableCellPadding(PdfReports.REPORT_PARENT_TABLE, 1);
		    section.setTableSpaceBetweenCells(PdfReports.REPORT_PARENT_TABLE, 0.5f);
		    section.setTableCellsDefaultColors(PdfReports.REPORT_PARENT_TABLE, 0xFFFFFF, 0x000000);
		    report.setReportTitleSectionAtributes(0xFFFFFF, 0xFFFFFF,0x000000, 10);
		    report.font.setFontAttributes(0x000000, 10, true, false, false);
		    section.addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, "TOTAL RELACIÓN DE ENVÍO", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    report.font.setFontAttributes(0x000000, 10, true, false, false);
		    section.addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, UtilidadTexto.formatearValores(total), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
		    report.addSectionToDocument("totales");
		    
		    report.font.setFontSizeAndAttributes(11,true,false,false);
	        String cad =  "Son: "+UtilidadN2T.convertirLetras(total,ConstantesBD.cadenaUnidades,ConstantesBD.cadenaUnidadesDecimales)+" MCTE.";
	        report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);
	        report.document.addParagraph("________________________________________________", report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
            report.document.addParagraph("Dr. Gabriel Enrique Castilla Castillo", report.font,iTextBaseDocument.ALIGNMENT_LEFT,12);
            report.document.addParagraph("Gerente Hospital de Suba II nivel E.S.E", report.font,iTextBaseDocument.ALIGNMENT_LEFT,11);
            
            
            report.document.addParagraph("Elaboró_________________________________________", report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);
            report.document.addParagraph("Revisó__________________________________________", report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);
            report.document.addParagraph("Aprobó__________________________________________", report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);
            
            report.document.addParagraph( vecConvenio.length>1?vecConvenio[1]:cxcForm.getConvenio()+" Nit: "+cxcForm.getNitConvenio(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,30);
            report.document.addParagraph( "Dirección: "+cxcForm.getDireccionConvenio().toUpperCase()+" "+ " Teléfono : "+cxcForm.getTelefonoConvenio(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
		    
            
            
            
//    	    section.addTableTextCellAligned("firmasTable", (vecConvenio.length>1?vecConvenio[1]:cxcForm.getConvenio()), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
//    	    section.addTableTextCellAligned("firmasTable", "Nit: "+cxcForm.getNitConvenio(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
//    	    section.addTableTextCellAligned("firmasTable", "Dirección: "+cxcForm.getDireccionConvenio(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
//    	    section.addTableTextCellAligned("firmasTable", "Teléfono: "+cxcForm.getTelefonoConvenio(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
//    	    
            
            
            
            
		    //*********************************************************************************************************
		    report.closeReport();
	    	
	    	//*****************************************************************************************************************
	    	//*****************************FIN IMPRESION DE ANEXOS******************************************************************
	    	//******************************************************************************************************************
	    }
	    
		
	}

	/**
	 * Método que calcula el número de facturas que tiene un centro de atencion específico
	 * @param mapa
	 * @param centroAtencion
	 * @return
	 */
	private static int obtenerNumeroFacturasXCentro(HashMap mapa, int centroAtencion) 
	{
		int cuenta = 0;
		
		for(int i=0;i<Integer.parseInt(mapa.get("numRegistros").toString());i++)
		{
			if(centroAtencion==Integer.parseInt(mapa.get("codigo_centro_atencion_"+i).toString()))
				cuenta++;
		}
		
		return cuenta;
	}

	/**
	 * Método implementado para ordenar el detalle de la cuenta de cobro
	 * @param cxcForm
	 * @return
	 */
	private static HashMap ordenarDetalleFacturas(CuentasCobroForm cxcForm) 
	{
		HashMap mapa = new HashMap();
		//Se muestra la informacion de cada factura
	    Iterator iterador = cxcForm.getDetalleFacturas().iterator();
	    int i = 0;
	    
	    
	    while(iterador.hasNext())
	    {
	    	DtoFactura factura=(DtoFactura)iterador.next();
	    	mapa.put("codigo_centro_atencion_"+i,factura.getCentroAtencion().getCodigo()+"");
	    	mapa.put("nombre_centro_atencion_"+i,factura.getCentroAtencion().getNombre());
	    	mapa.put("factura_"+i, factura.getConsecutivoFactura()+"");
	    	mapa.put("fecha_"+i,factura.getFecha());
	    	mapa.put("paciente_"+i,factura.getNombrePaciente());
	    	mapa.put("identificacion_"+i,factura.getIdPaciente());
	    	mapa.put("poliza_"+i,"");//pendiente número de la poliza
	    	mapa.put("servicio_"+i,factura.getViaIngreso().getNombre());
	    	mapa.put("valor_"+i,factura.getValorCartera()+"");
	    	i++;
	    	
	    }
	    
	    //**********SE REALIZA ORDENACION DE REGISTROS POR NOMBRE DE CENTRO DE ATENCION*******************
	    String[] columnas={"codigo_centro_atencion_","nombre_centro_atencion_","factura_","fecha_","paciente_","identificacion_","poliza_","servicio_","valor_"};
	    
	    mapa = Listado.ordenarMapa(columnas, "nombre_centro_atencion_", "", mapa, i);
	    
	    mapa.put("numRegistros",i+"");
	    
	    return mapa;
	}

	/**
	 * Método implementado para realizar la impresion de cuenta de cobro en el formato Sasaima
	 * @param cxcForm
	 * @param ins
	 * @param filename
	 */
	private static void pdfReporteSasaima(CuentasCobroForm cxcForm, InstitucionBasica ins, String filename,String filename2) 
	{
		//-- Variable para manejar el reporte
        PdfReports report = new PdfReports();
        //Se genera el encabezado de la impresion
		report.setReportBaseHeaderSasaima(ins);
		
		//Se consulta informacion de la cuenta de cobro
		
		//Se abre el reporte
        report.openReport(filename);
        report.font.useFont(iTextBaseFont.FONT_TIMES, true, true, true);
        iTextBaseTable section;
        
        //**************SECCION INFORMACION CLIENTE*********************************************************************
        section = report.createSection("informacionCliente", "informacionClienteTable", 4, 2, 10);
  		section.setTableBorder("informacionClienteTable", 0xFFFFFF, 0.0f);
	    section.setTableCellBorderWidth("informacionClienteTable", 0.5f);
	    section.setTableCellPadding("informacionClienteTable", 1);
	    section.setTableSpaceBetweenCells("informacionClienteTable", 0.5f);
	    section.setTableCellsDefaultColors("informacionClienteTable", 0xFFFFFF, 0xFFFFFF);
	    
	    section.setTableCellsColSpan(2);
	    report.font.setFontAttributes(0x000000, 10, true, false, false);
	    section.addTableTextCellAligned("informacionClienteTable", "FACTURA N° "+UtilidadTexto.formatearValores(cxcForm.getNumCuentaCobro(),"0"), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 10, false, false, false);
	    section.addTableTextCellAligned("informacionClienteTable", "Período Facturación: "+
	    	UtilidadFecha.conversionFormatoFechaAAp(cxcForm.getFechaInicial())+" a "+
	    	UtilidadFecha.conversionFormatoFechaAAp(cxcForm.getFechaFinal()), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    String[] vecConvenio = cxcForm.getConvenio().split(ConstantesBD.separadorSplit);
	    section.addTableTextCellAligned("informacionClienteTable", "Cliente: "+(vecConvenio.length>1?vecConvenio[1]:cxcForm.getConvenio()), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.addTableTextCellAligned("informacionClienteTable", "Nit: "+cxcForm.getNitConvenio(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.addTableTextCellAligned("informacionClienteTable", "Dirección: "+cxcForm.getDireccionConvenio(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.addTableTextCellAligned("informacionClienteTable", "Teléfono: "+cxcForm.getTelefonoConvenio(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    //se añade la sección al documento
	    report.addSectionToDocument("informacionCliente");
	    //*****************************************************************************************************************
	    
	    //***********************SECCION DESCRIPCION Y VALOR TOTAL**************************************************************
	    section = report.createSection("descripcion", "descripcionTable", 4, 1, 10);
  		section.setTableBorder("descripcionTable", 0xFFFFFF, 0.0f);
	    section.setTableCellBorderWidth("descripcionTable", 0.5f);
	    section.setTableCellPadding("descripcionTable", 1);
	    section.setTableSpaceBetweenCells("descripcionTable", 0.5f);
	    section.setTableCellsDefaultColors("descripcionTable", 0xFFFFFF, 0xFFFFFF);
	    
	    report.font.setFontAttributes(0x000000, 10, true, false, false);
	    section.addTableTextCellAligned("descripcionTable", "Descripción:", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 10, false, false, false);
	    section.setTableCellsDefaultColors("descripcionTable", 0xFFFFFF, 0x000000);
	    section.addTableTextCellAligned("descripcionTable", cxcForm.getObservaciones(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsDefaultColors("descripcionTable", 0xFFFFFF, 0xFFFFFF);
	    report.font.setFontAttributes(0x000000, 10, true, false, false);
	    section.addTableTextCellAligned("descripcionTable", "", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.addTableTextCellAligned("descripcionTable", "Valor Total de la Factura: $"+UtilidadTexto.formatearValores(cxcForm.getValorInicialCuenta()), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    //se añade la sección al documento
	    report.addSectionToDocument("descripcion");
	    //*****************************************************************************************************************
	    
	    //**************SECCIÓN VALOR DE LA FATURA Y FIRMAS********************************************************************
	    report.font.setFontSizeAndAttributes(11,true,false,false);
        String cad =  "Son: "+UtilidadN2T.convertirLetras(cxcForm.getValorInicialCuenta(),ConstantesBD.cadenaUnidades,ConstantesBD.cadenaUnidadesDecimales)+" MCTE.";
        report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);
        
//        section = report.createSection("firmas", "firmasTable", 2, 3, 10);
//  		section.setTableBorder("firmasTable", 0xFFFFFF, 0.0f);
//	    section.setTableCellBorderWidth("firmasTable", 0.5f);
//	    section.setTableCellPadding("firmasTable", 1);
//	    section.setTableSpaceBetweenCells("firmasTable", 0.5f);
//	    section.setTableCellsDefaultColors("firmasTable", 0xFFFFFF, 0xFFFFFF);
//	    
//	    report.font.setFontAttributes(0x000000, 10, true, false, false);
//	    section.addTableTextCellAligned("firmasTable", "_____________________________", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
//	    section.addTableTextCellAligned("firmasTable", "", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
//	    section.addTableTextCellAligned("firmasTable", "_____________________________", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
//	    section.addTableTextCellAligned("firmasTable", "Firma Institución:", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
//	    section.addTableTextCellAligned("firmasTable", "", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
//	    section.addTableTextCellAligned("firmasTable", "Firma Cliente:", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
//	    
//	    //se añade la sección al documento
//	    report.addSectionToDocument("firmas");
        
        //Anexo 992
        
        agregarFirmasValoresXDefecto(cxcForm, Utilidades.convertirAEntero(ins.getCodigo()), ConstantesIntegridadDominio.acronimoSasaima, section, report);
        
        //Fin anexo 992
        
	    //*****************************************************************************************************************
	    
	    report.closeReport();
	    
	    //Se verifica si se debe imprimir los anexos
	    if(cxcForm.isImpresionAnexos())
	    {
	    	//******************************************************************************************************************
	    	//*****************************IMPRESION DE ANEXOS******************************************************************
	    	//*****************************************************************************************************************
	    	//Se genera nuevo reporte
	    	report = new PdfReports();
	    	report.setReportBaseHeaderSasaimaAnexo(ins,UtilidadFecha.conversionFormatoFechaAAp(cxcForm.getFechaInicial()),UtilidadFecha.conversionFormatoFechaAAp(cxcForm.getFechaFinal()),(vecConvenio.length>1?vecConvenio[1]:cxcForm.getConvenio()));
	    	report.openReport(filename2);
	    	report.font.useFont(iTextBaseFont.FONT_TIMES, true, true, true);
	    	
		    
		    //*****************SECCION DETALLE************************************************************************
		    float widths[]={(float) 1,(float) 1, (float) 2.2,(float) 1.55, (float) 1.25, (float) 0.75, (float) 1, (float) 1.25};
		    section = report.createSection("detalleAnexos", PdfReports.REPORT_PARENT_TABLE, cxcForm.getNumeroFacturasCxC()+3, 8, 8);
	  		section.setTableBorder(PdfReports.REPORT_PARENT_TABLE, 0xFFFFFF, 0.5f);
	  		
		    section.setTableCellBorderWidth(PdfReports.REPORT_PARENT_TABLE, 0.5f);
		    section.setTableCellPadding(PdfReports.REPORT_PARENT_TABLE, 1);
		    section.setTableSpaceBetweenCells(PdfReports.REPORT_PARENT_TABLE, 0.5f);
		    section.setTableCellsDefaultColors(PdfReports.REPORT_PARENT_TABLE, 0xFFFFFF, 0xFFFFFF);
		    report.setReportTitleSectionAtributes(0xFFFFFF, 0xFFFFFF,0x000000, 10);
		    report.addSectionTitle("detalleAnexos", PdfReports.REPORT_PARENT_TABLE, "Facturas");
		    //Se asignan los encabezados de la tabla
		    report.font.setFontAttributes(0x000000, 10, true, false, false);
		    section.addTableTextCellHeaderAligned(PdfReports.REPORT_PARENT_TABLE, "Factura", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,1,0);
		    section.addTableTextCellHeaderAligned(PdfReports.REPORT_PARENT_TABLE, "Fecha", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,1,1);
		    section.addTableTextCellHeaderAligned(PdfReports.REPORT_PARENT_TABLE, "Paciente", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,1,2);
		    section.addTableTextCellHeaderAligned(PdfReports.REPORT_PARENT_TABLE, "Id. Paciente", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,1,3);
		    section.addTableTextCellHeaderAligned(PdfReports.REPORT_PARENT_TABLE, "Valor Cliente", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,1,4);
		    section.addTableTextCellHeaderAligned(PdfReports.REPORT_PARENT_TABLE, "Tipo Monto", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,1,5);
		    section.addTableTextCellHeaderAligned(PdfReports.REPORT_PARENT_TABLE, "Valor Monto", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,1,6);
		    section.addTableTextCellHeaderAligned(PdfReports.REPORT_PARENT_TABLE, "Valor Total", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,1,7);
		    section.getTableReference(PdfReports.REPORT_PARENT_TABLE).endHeaders();
		    
		    report.font.setFontAttributes(0x000000, 8, false, false, false);
		    
		    //Se muestra la informacion de cada factura
		    Iterator iterador = cxcForm.getDetalleFacturas().iterator();
		    double totalCliente = 0;
		    double totalMonto = 0;
		    double totalFactura = 0;
		    while(iterador.hasNext())
		    {
		    	DtoFactura factura = (DtoFactura)iterador.next();
		    	section.addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, factura.getConsecutivoFactura()+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    section.addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, factura.getFecha(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    report.font.setFontAttributes(0x000000, 8, false, false, false);
			    section.addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, factura.getNombrePaciente(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    report.font.setFontAttributes(0x000000, 8, false, false, false);
			    section.addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, factura.getIdPaciente(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    totalCliente += factura.getValorCartera();
			    section.addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, UtilidadTexto.formatearValores(factura.getValorCartera()), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
			    //Segun el estado de paciente se muestra el tipo monto y valor monto
			    if(factura.getEstadoPaciente().getCodigo()==ConstantesBD.codigoEstadoFacturacionPacientePorCobrar||
			    	factura.getEstadoPaciente().getCodigo()==ConstantesBD.codigoEstadoFacturacionPacienteCancelada||
			    	factura.getEstadoPaciente().getCodigo()==ConstantesBD.codigoEstadoFacturacionPacienteConDevolucion)
			    {
			    	totalMonto += factura.getValorBrutoPac();
			    	section.addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, (factura.getTipoMonto().getCodigo()==ConstantesBD.codigoTipoMontoCopago?"CO":"CM"), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    	section.addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, UtilidadTexto.formatearValores(factura.getValorBrutoPac()), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
			    }
			    else
			    {
			    	section.addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, "", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    	section.addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, "", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
			    }
			    totalFactura += factura.getValorTotal();
			    section.addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, UtilidadTexto.formatearValores(factura.getValorTotal()), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
		    }
		    
		    //se muestran los totales
		    report.font.setFontAttributes(0x000000, 10, true, false, false);
		    section.setTableCellsColSpan(4);
		    section.addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, "Totales", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.setTableCellsColSpan(1);
		    section.addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, UtilidadTexto.formatearValores(totalCliente), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
		    section.setTableCellsColSpan(2);
		    section.addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, UtilidadTexto.formatearValores(totalMonto), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
		    section.setTableCellsColSpan(1);
		    section.addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, UtilidadTexto.formatearValores(totalFactura), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
		    report.font.setFontAttributes(0x000000, 10, false, false, false);
		    try 
            {
				section.getTableReference(PdfReports.REPORT_PARENT_TABLE).setWidths(widths);
			} 
            catch (BadElementException e) 
            {
				cuentasCobroPdfLogger.error("Error al ajustar los anchos de la tabla de UPC: "+e);
			}
		    //se añade la sección al documento
		    report.addSectionToDocument("detalleAnexos");
		    //*********************************************************************************************************
		    report.closeReport();
	    	
	    	//*****************************************************************************************************************
	    	//*****************************FIN IMPRESION DE ANEXOS******************************************************************
	    	//******************************************************************************************************************
	    }
	    
	    //-- Cerrar el reporte 
        	
		
	}
	
	
	//Función para agregar las firmas a los resportes estandar, sasaima y suba
	
	private static void agregarFirmasValoresXDefecto(CuentasCobroForm forma, int institucion, String formato, iTextBaseTable section, PdfReports report)
	{
		 if (formato.equals(ConstantesIntegridadDominio.acronimoSasaima)||formato.equals(ConstantesIntegridadDominio.acronimoSuba)||formato.equals(ConstantesIntegridadDominio.acronimoEstandar))
	        {
	        	
	        	//Agrego las Firmas por Anexo 992
	            
	            if (ValoresPorDefecto.getImprimirFirmasEnImpresionCC(institucion).equals(ConstantesBD.acronimoSi))
	            {
		            DtoFirmasValoresPorDefecto dtoFirmas=new DtoFirmasValoresPorDefecto();
		            dtoFirmas.setValorPorDefecto(ConstantesValoresPorDefecto.nombreValoresDefectoImprimirFirmasEnImpresionCC);
		            dtoFirmas.setInstitucion(institucion);
		            
		            forma.setListadoFirmas(ValoresPorDefecto.consultarFirmasValoresPorDefecto(dtoFirmas));
		            
		            int sizeArray=forma.getListadoFirmas().size();
		            /**
		             * Inc. 1397 
		             * Se debe validar si existe parametrizada alguna firma
		             * @author Diana Ruiz
		             * @since 22/08/2011
		             */
		            if (forma.getListadoFirmas().size() != 0 ){
		            int col[]=crearCeldarFirmas(sizeArray);
		            section = report.createSection("firmas", "firmasTable", col[0], col[1], 10);
			  		section.setTableBorder("firmasTable", 0xFFFFFF, 0.0f);
				    section.setTableCellBorderWidth("firmasTable", 0.5f);
				    section.setTableCellPadding("firmasTable", 0);
				    section.setTableSpaceBetweenCells("firmasTable", 0.5f);
				    section.setTableCellsDefaultColors("firmasTable", 0xFFFFFF, 0xFFFFFF);
				    section.setTableBorderColor("firmasTable", 0xFFFFFF);
				    
				    report.font.setFontAttributes(0x000000, 10, true, false, false);
		            }
		            
				   
		            //Para 1 Firma
				    if (sizeArray==1)
		            {	            	
		            	if (!forma.getListadoFirmas().get(0).getFirmaDigital().trim().isEmpty())
		            		section.addTableImageCell("firmasTable",ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+System.getProperty("FIRMADIGITAL")+System.getProperty("file.separator")+forma.getListadoFirmas().get(0).getFirmaDigital(),40,0,0);		
		            	else
		            		section.addTableTextCellAligned("firmasTable", "____________________________________", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_BOTTOM,0,0);
		            	
		            	section.addTableTextCellAligned("firmasTable", forma.getListadoFirmas().get(0).getUsuario().toUpperCase(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP,1,0);
		            	section.addTableTextCellAligned("firmasTable", forma.getListadoFirmas().get(0).getCargo(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP,2,0);
		            	
		            	
		            	
		            	
//		               	report.document.addParagraph("________________________________________________", report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
//		            	report.document.addParagraph("Dr. Edgar Silvio Sanchez Villegas", report.font,iTextBaseDocument.ALIGNMENT_LEFT,12);
//		            	report.document.addParagraph("Gerente Hospital de Suba II nivel E.S.E", report.font,iTextBaseDocument.ALIGNMENT_LEFT,11);


		            	
		            	
		            	
		            }
				    //2 Firmas
		            else if (sizeArray==2)
		            {
		            	if (!forma.getListadoFirmas().get(0).getFirmaDigital().trim().isEmpty())
		            		section.addTableImageCell("firmasTable",ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+System.getProperty("FIRMADIGITAL")+System.getProperty("file.separator")+forma.getListadoFirmas().get(0).getFirmaDigital(),40,0,0);		
		            	else
		            		section.addTableTextCellAligned("firmasTable", "____________________________________", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_BOTTOM,0,0);
		            	
		            	section.addTableTextCellAligned("firmasTable", forma.getListadoFirmas().get(0).getUsuario().toUpperCase(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP,1,0);
		            	section.addTableTextCellAligned("firmasTable", forma.getListadoFirmas().get(0).getCargo(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP,2,0);
		            	
		            	if (!forma.getListadoFirmas().get(1).getFirmaDigital().trim().isEmpty())
		            		section.addTableImageCell("firmasTable",ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+System.getProperty("FIRMADIGITAL")+System.getProperty("file.separator")+forma.getListadoFirmas().get(1).getFirmaDigital(),40,0,1);		
		            	else
		            		section.addTableTextCellAligned("firmasTable", "____________________________________", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_BOTTOM,0,1);
		            	
		            	section.addTableTextCellAligned("firmasTable", forma.getListadoFirmas().get(0).getUsuario().toUpperCase(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP,1,1);
		            	section.addTableTextCellAligned("firmasTable", forma.getListadoFirmas().get(0).getCargo(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP,2,1);
		            } 
				    //3 Firmas
		            else if (sizeArray==3)
		            {
		            	if (!forma.getListadoFirmas().get(0).getFirmaDigital().trim().isEmpty())
		            		section.addTableImageCell("firmasTable",ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+System.getProperty("FIRMADIGITAL")+System.getProperty("file.separator")+forma.getListadoFirmas().get(0).getFirmaDigital(),40,0,0);		
		            	else
		            		section.addTableTextCellAligned("firmasTable", "____________________________________", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_BOTTOM,0,0);
		            	
		            	section.addTableTextCellAligned("firmasTable", forma.getListadoFirmas().get(0).getUsuario().toUpperCase(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP,1,0);
		            	section.addTableTextCellAligned("firmasTable", forma.getListadoFirmas().get(0).getCargo(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP,2,0);
		            	
		            	if (!forma.getListadoFirmas().get(1).getFirmaDigital().trim().isEmpty())
		            		section.addTableImageCell("firmasTable",ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+System.getProperty("FIRMADIGITAL")+System.getProperty("file.separator")+forma.getListadoFirmas().get(1).getFirmaDigital(),40,0,1);		
		            	else
		            		section.addTableTextCellAligned("firmasTable", "____________________________________", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_BOTTOM,0,1);
		            	
		            	section.addTableTextCellAligned("firmasTable", forma.getListadoFirmas().get(1).getUsuario().toUpperCase(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP,1,1);
		            	section.addTableTextCellAligned("firmasTable", forma.getListadoFirmas().get(1).getCargo(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP,2,1);
		            	
		            	if (!forma.getListadoFirmas().get(2).getFirmaDigital().trim().isEmpty())
		            		section.addTableImageCell("firmasTable",ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+System.getProperty("FIRMADIGITAL")+System.getProperty("file.separator")+forma.getListadoFirmas().get(2).getFirmaDigital(),40,3,0);		
		            	else
		            		section.addTableTextCellAligned("firmasTable", "____________________________________", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_BOTTOM,3,0);
		            	
		            	section.addTableTextCellAligned("firmasTable", forma.getListadoFirmas().get(2).getUsuario().toUpperCase(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP,4,0);
		            	section.addTableTextCellAligned("firmasTable", forma.getListadoFirmas().get(2).getCargo(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP,5,0);
		            } 
				  //4 Firmas
		            else if (sizeArray==4)
		            {
		            	if (!forma.getListadoFirmas().get(0).getFirmaDigital().trim().isEmpty())
		            		section.addTableImageCell("firmasTable",ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+System.getProperty("FIRMADIGITAL")+System.getProperty("file.separator")+forma.getListadoFirmas().get(0).getFirmaDigital(),40,0,0);		
		            	else
		            		section.addTableTextCellAligned("firmasTable", "____________________________________", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_BOTTOM,0,0);
		            	
		            	section.addTableTextCellAligned("firmasTable", forma.getListadoFirmas().get(0).getUsuario().toUpperCase(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP,1,0);
		            	section.addTableTextCellAligned("firmasTable", forma.getListadoFirmas().get(0).getCargo(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP,2,0);
		            	
		            	if (!forma.getListadoFirmas().get(1).getFirmaDigital().trim().isEmpty())
		            		section.addTableImageCell("firmasTable",ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+System.getProperty("FIRMADIGITAL")+System.getProperty("file.separator")+forma.getListadoFirmas().get(1).getFirmaDigital(),40,0,1);		
		            	else
		            		section.addTableTextCellAligned("firmasTable", "____________________________________", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_BOTTOM,0,1);
		            	
		            	section.addTableTextCellAligned("firmasTable", forma.getListadoFirmas().get(1).getUsuario().toUpperCase(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP,1,1);
		            	section.addTableTextCellAligned("firmasTable", forma.getListadoFirmas().get(1).getCargo(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP,2,1);
		            	
		            	if (!forma.getListadoFirmas().get(2).getFirmaDigital().trim().isEmpty())
		            		section.addTableImageCell("firmasTable",ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+System.getProperty("FIRMADIGITAL")+System.getProperty("file.separator")+forma.getListadoFirmas().get(2).getFirmaDigital(),40,3,0);		
		            	else
		            		section.addTableTextCellAligned("firmasTable", "____________________________________", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_BOTTOM,3,0);
		            	
		            	section.addTableTextCellAligned("firmasTable", forma.getListadoFirmas().get(2).getUsuario().toUpperCase(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,4,0);
		            	section.addTableTextCellAligned("firmasTable", forma.getListadoFirmas().get(2).getCargo(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP,5,0);
		            	
		            	if (!forma.getListadoFirmas().get(3).getFirmaDigital().trim().isEmpty())
		            		section.addTableImageCell("firmasTable",ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+System.getProperty("FIRMADIGITAL")+System.getProperty("file.separator")+forma.getListadoFirmas().get(3).getFirmaDigital(),40,3,1);		
		            	else
		            		section.addTableTextCellAligned("firmasTable", "____________________________________", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_BOTTOM,3,1);
		            	
		            	section.addTableTextCellAligned("firmasTable", forma.getListadoFirmas().get(3).getUsuario().toUpperCase(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP,4,1);
		            	section.addTableTextCellAligned("firmasTable", forma.getListadoFirmas().get(3).getCargo(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP,5,1);
		            } 
		            		
		            report.addSectionToDocument("firmas");	            
	            }
	        }
		
	}
		 
	/**
	 * Metodo que se encarga de establecer el numero de columnas y filas de la seccion de firmas de acuerdo 
	 * a lo parametrizado por el numero de firmas.
	 * 
	 * @param numFirmas
	 * @param report
	 * @return
	 */
	public static int[] crearCeldarFirmas(int numFirmas )
	{		
		int numCol=2;
		int filaCol[]=new int[2];
		if(numFirmas==1||numFirmas==2)
		{	
			if(numFirmas==1)
			{	numCol=1;
			}
			filaCol[0]=3;		
			filaCol[1]=numCol;
	  		
		}else if(numFirmas==3||numFirmas==4)
		{
			filaCol[0]=6;		
			filaCol[1]=numCol;	  		
		}
		
		return filaCol;
	}
	
	
	
}