/*
 * Creado en Aug 8, 2006
 * Andrés Mauricio Ruiz Vélez
 * Princeton S.A (Parquesoft - Manizales)
 */
package com.princetonsa.pdf;

import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ConstantesValoresPorDefecto;
import util.UtilidadCadena;
import util.UtilidadN2T;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;
import util.pdf.iTextBaseTable;

import com.lowagie.text.BadElementException;
import com.princetonsa.actionform.capitacion.CuentaCobroCapitacionForm;
import com.princetonsa.dto.administracion.DtoFirmasValoresPorDefecto;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.ParametrizacionInstitucion;

/**
 * Clase para la impresión de las cuentas de cobro en las 
 * funcionalidades de generación, modificación y consulta
 * @author Andrés Mauricio Ruiz Vélez
 * Princeton S.A. (Parquesoft-Manizales) 
 * @version Aug 8, 2006
 */
public class CuentasCobroCapitacionPdf
{
	
	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	private static Logger logger = Logger.getLogger(CuentasCobroCapitacionPdf.class);
	
	/**
	 * Metodo para generar el PDF de una cuenta de cobro de capitación para la 
	 * funcionalidad de consulta de cuenta de cobro
	 * @param filename
	 * @param forma
	 * @param usuario
	 * @param tipoImpresion 
	 * @param con
	 */
	public static void pdfCuentaCobroCapitacion(String filename, CuentaCobroCapitacionForm forma, UsuarioBasico usuario, Connection conec, HttpServletRequest request, String tipoImpresion) 
	{
		//-- Variable para manejar el reporte
        PdfReports report = new PdfReports();
        //-- Consultar la informacion de la Empresa.
        ParametrizacionInstitucion inst=new ParametrizacionInstitucion();
    	inst.cargar(conec, usuario.getCodigoInstitucionInt());
    	InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
    	//--- Tipo de Formato
    	String formato = inst.getFormatoImpresion();
    	report.setFormatoImpresion(formato);
    	//-- MAPA INFORMACION
    	HashMap informacion = llenarInformacion(forma);
    	
        //********************SECCION INFORMACION DE LA INSTITUCION*****************************************************
    	//-- Titulo del reporte
    	String tituloReporte=" CONSULTA CUENTA COBRO CAPITACIÓN";
    
        if(formato.equals(ConstantesIntegridadDominio.acronimoSasaima))
        {
        	/***********************SASAIMA************************************/
        	report.setReportBaseHeaderSasaima(institucionBasica);
        }
        else if(formato.equals(ConstantesIntegridadDominio.acronimoSuba))
        {
        	/***********************SUBA****************************************/
        	String filePathLogo = ValoresPorDefecto.getFilePath(); //path para el logo de Bogotá sin indiferencia
        	if(System.getProperty("file.separator").equals("/"))
        	{
        		filePathLogo=filePathLogo.substring(0, filePathLogo.lastIndexOf("/",(filePathLogo.length()-2)))+"/imagenes/bogsin2.gif";
        		
        	}
        	else
        	{
        		filePathLogo=filePathLogo.substring(0, filePathLogo.lastIndexOf("\\",(filePathLogo.length()-2)))+"\\imagenes\\bogsin2.gif";
        	}
        	report.setReportBaseHeaderSuba(institucionBasica.getLogoReportes(),institucionBasica,informacion.get("numeroCuentaCobro").toString(),true);
        	//Se asignan los datos del pie de pagina
        	report.setMapaPiePagina("filePathLogo", filePathLogo);
        	report.setMapaPiePagina("infoInstitucion", "Dirección: "+institucionBasica.getDireccion()+" Conmutador: "+institucionBasica.getTelefono());
        }
        else
        {
        	/*******************************ESTANDAR*******************************************************************************/
        	//------Encabezado de la página ---------------------//
        
	        //Generacion de la cabecera, validando el tipo de separador e utilizado en el path

        	//Anexo 992 El reporte queda sin titulo alguno, de todas maneras queda disponible 
        	tituloReporte="";
	        report.setReportBaseHeaderIzq(institucionBasica,"" , tituloReporte);
	        
	    }
        //***************************************************************************************************************
        
        //Se abre el reporte
        report.openReport(filename);
        report.font.useFont(iTextBaseFont.FONT_TIMES, true, true, true);
        iTextBaseTable section;
        
        
        //Anexo 992
        if (formato.equals(ConstantesIntegridadDominio.acronimoEstandar))
        {
        	//Anexo 992 Agrego el Nuevo encabezado
        	String encabezado=ValoresPorDefecto.getEncabezadoFormatoImpresionFacturaOCCCapitacion(usuario.getCodigoInstitucionInt());
   
        	if (!encabezado.equals(""))
        	{
        		section = report.createSection("encabezadoParametrizabelEstandar", "encabezadoParametrizabelEstandarTable", 1, 1, 1);
    	  		section.setTableBorder("encabezadoParametrizabelEstandarTable", 0xFFFFFF, 0.0f);
    		    section.setTableCellBorderWidth("encabezadoParametrizabelEstandarTable", 0.5f);
    		    section.setTableCellPadding("encabezadoParametrizabelEstandarTable", 1);
    		    section.setTableSpaceBetweenCells("encabezadoParametrizabelEstandarTable", 0.5f);
    		    section.setTableCellsDefaultColors("encabezadoParametrizabelEstandarTable", 0xFFFFFF, 0xFFFFFF);
    		    
    		    section.setTableCellsColSpan(1);
    		    report.font.setFontAttributes(0x000000, 8, true, false, false);
    		    section.addTableTextCellAligned("encabezadoParametrizabelEstandarTable", encabezado, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
    		    
    		    report.addSectionToDocument("encabezadoParametrizabelEstandar");
        	}
        }
        //Fin Anexo 992
        
        
        //**************SECCION INFORMACION CLIENTE*********************************************************************
        if(formato.equals(ConstantesIntegridadDominio.acronimoSasaima))
        {
        	/***********************SASAIMA************************************/
        	section = report.createSection("informacionCliente", "informacionClienteTable", 4, 2, 10);
	  		section.setTableBorder("informacionClienteTable", 0xFFFFFF, 0.0f);
		    section.setTableCellBorderWidth("informacionClienteTable", 0.5f);
		    section.setTableCellPadding("informacionClienteTable", 1);
		    section.setTableSpaceBetweenCells("informacionClienteTable", 0.5f);
		    section.setTableCellsDefaultColors("informacionClienteTable", 0xFFFFFF, 0xFFFFFF);
		    
		    section.setTableCellsColSpan(2);
		    report.font.setFontAttributes(0x000000, 10, true, false, false);
		    section.addTableTextCellAligned("informacionClienteTable", "FACTURA N° "+informacion.get("numeroCuentaCobro"), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    report.font.setFontAttributes(0x000000, 10, false, false, false);
		    section.addTableTextCellAligned("informacionClienteTable", "Período Facturación: "+informacion.get("fechaInicial")+" a "+informacion.get("fechaFinal"), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    section.setTableCellsColSpan(1);
		    section.addTableTextCellAligned("informacionClienteTable", "Cliente: "+informacion.get("nombreConvenio"), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("informacionClienteTable", "Nit: "+informacion.get("NitConvenio"), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("informacionClienteTable", "Dirección: "+informacion.get("direccion"), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("informacionClienteTable", "Teléfono: "+informacion.get("telefono"), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    //se añade la sección al documento
		    report.addSectionToDocument("informacionCliente");
        }
        else if(formato.equals(ConstantesIntegridadDominio.acronimoEstandar))
        {
        	/*******************************ESTANDAR*******************************************************************************/
        	//-------------------------------- Información del encabezao de la cuenta de cobro ----------------------------------------------
        	
        	String cad="";
            report.font.setFontSizeAndAttributes(10,true,false,false);
                    
             logger.info("\n\ninformacion cuenta cobro:: "+informacion);
                           
             //Cambio de Anexo 992 Se reorganiza el formato y se añade el campo de hora
             
            cad =  "____________________________________________________________________________________________________________";
         	report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_LEFT, 3);
             
            if(Utilidades.convertirAEntero(ValoresPorDefecto.getTipoConsecutivoCapitacion(usuario.getCodigoInstitucionInt())) == (ConstantesBD.codigoTipoConsecutivoFacturaPaciente))
            {
            	cad = "Factura de Venta N°. "+informacion.get("prefijoFactura") + informacion.get("numeroCuentaCobro") +  "        Fecha:"+ informacion.get("fechaGeneracion")+"        Hora:"+ informacion.get("horaElaboracion") +"        Estado:" + informacion.get("estadoCuentaCobro");
            }
            else
            {
            	cad = "Cuenta Cobro N°. " + informacion.get("numeroCuentaCobro") +  "        Fecha:"+ informacion.get("fechaGeneracion")+"        Hora:"+ informacion.get("horaElaboracion") +"        Estado:" + informacion.get("estadoCuentaCobro");
            }
            
        	report.document.addParagraph(cad,report.font,iTextBaseDocument.ALIGNMENT_LEFT,20);
        	
        	cad =  "____________________________________________________________________________________________________________";
         	report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_LEFT,5);
         	
        	cad =  "Responsable: " +informacion.get("nombreConvenio")+  "           Número ID: " + informacion.get("NitConvenio");
        	report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_LEFT,20);
        	
        	cad =  "Dirección: " +informacion.get("direccion")+  "                  Teléfono: " + informacion.get("telefono");
        	report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_LEFT,20);
        	cad =  "____________________________________________________________________________________________________________";
         	report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_LEFT,5);
         	
	    	cad =  "POR CONCEPTO DE CAPITACIÓN";
	        report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_CENTER,30);
	        
	        //Cambio Anexo 992
	        cad =  "Período de Facturación: " +informacion.get("fechaInicial")+  "  a  " + informacion.get("fechaFinal");
        	report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_LEFT,20);
        	
        }
        //***************************************************************************************************************
        
        //***********************SECCION DESCRIPCION**************************************************************
        if(formato.equals(ConstantesIntegridadDominio.acronimoSasaima)||formato.equals(ConstantesIntegridadDominio.acronimoSuba))
        {
        	/***********************SASAIMA/SUBA************************************/
        	section = report.createSection("descripcion", "descripcionTable", 2, 1, 10);
	  		section.setTableBorder("descripcionTable", 0xFFFFFF, 0.0f);
		    section.setTableCellBorderWidth("descripcionTable", 0.5f);
		    section.setTableCellPadding("descripcionTable", 1);
		    section.setTableSpaceBetweenCells("descripcionTable", 0.5f);
		    section.setTableCellsDefaultColors("descripcionTable", 0xFFFFFF, 0xFFFFFF);
		    
		    report.font.setFontAttributes(0x000000, 10, true, false, false);
		    section.addTableTextCellAligned("descripcionTable", "Descripción:", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    report.font.setFontAttributes(0x000000, 10, false, false, false);
		    section.setTableCellsDefaultColors("descripcionTable", 0xFFFFFF, 0x000000);
		    section.addTableTextCellAligned("descripcionTable", informacion.get("descripcion")+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    
		    //se añade la sección al documento
		    report.addSectionToDocument("descripcion");
        }
     
      
        //**********************************************************************************************************
        
        //Se toman los mapas del detalle
        HashMap mapaUPC = (HashMap) informacion.get("mapaUPC");
        HashMap mapaGrupoEtareo = (HashMap) informacion.get("mapaGrupoEtareo");
        
        //**************SECCION DETALLE DE LA CUENTA DE COBRO********************************************************
        if(formato.equals(ConstantesIntegridadDominio.acronimoSasaima))
        {
        	/***********************SASAIMA************************************/
        	cuerpoImpresionFormatoSasaima(mapaUPC,mapaGrupoEtareo,report,-1);
        	/*****************************************************************/
        }
        else if(formato.equals(ConstantesIntegridadDominio.acronimoSuba))
        {
        	/***********************SUBA************************************/
        	cuerpoImpresionFormatoSuba(mapaUPC,mapaGrupoEtareo,report,-1);
        	/*****************************************************************/
        }
        else
        {
        	/*******************************ESTANDAR*******************************************************************************/
        	cuerpoImpresionFormatoEstandar(mapaUPC,mapaGrupoEtareo,report,-1);
            /***************************************************************************************************************************/
        }
        //************************************************************************************************************
        
        //**Cambio Anexo 992 para el formato estandar
        if (formato.equals(ConstantesIntegridadDominio.acronimoEstandar))
        {
          	/*******************************ESTANDAR*******************************************************************************/
          	report.font.setFontSizeAndAttributes(9,true,false,false);
          	String cad =  " Observaciones: " + informacion.get("descripcion");
            report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_LEFT,20);
        }
        
        
        //**************SECCIÓN VALOR DE LA FATURA Y FIRMAS********************************************************************
        if(formato.equals(ConstantesIntegridadDominio.acronimoSasaima))
        {
        	/*********************************************SASAIMA**********************************************************************/
        	report.font.setFontSizeAndAttributes(11,true,false,false);
            String cad =  "Son: "+UtilidadN2T.convertirLetras(informacion.get("valorInicialCuenta").toString().trim(),ConstantesBD.cadenaUnidades,ConstantesBD.cadenaUnidadesDecimales)+" MCTE.\n\n";
            report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);
            
            //Anexo 992 Se quita la seccion de firmas y se reemplaza por la nueva
            
//          section = report.createSection("firmas", "firmasTable", 2, 3, 10);
//	  		section.setTableBorder("firmasTable", 0xFFFFFF, 0.0f);
//		    section.setTableCellBorderWidth("firmasTable", 0.5f);
//		    section.setTableCellPadding("firmasTable", 1);
//		    section.setTableSpaceBetweenCells("firmasTable", 0.5f);
//		    section.setTableCellsDefaultColors("firmasTable", 0xFFFFFF, 0xFFFFFF);
//		    
//		    report.font.setFontAttributes(0x000000, 10, true, false, false);
//		    section.addTableTextCellAligned("firmasTable", "_____________________________", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
//		    section.addTableTextCellAligned("firmasTable", "", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
//		    section.addTableTextCellAligned("firmasTable", "_____________________________", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
//		    section.addTableTextCellAligned("firmasTable", "Firma Institución:", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
//		    section.addTableTextCellAligned("firmasTable", "", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
//		    section.addTableTextCellAligned("firmasTable", "Firma Cliente:", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
            
		    
		    
		    //se añade la sección al documento
//		    report.addSectionToDocument("firmas");
            /*************************************************************************************************************************/
        }
        else if(formato.equals(ConstantesIntegridadDominio.acronimoSuba))
        {
        	/*********************************************SUBA**********************************************************************/
        	report.font.setFontSizeAndAttributes(11,true,false,false);
            String cad =  "Son: "+UtilidadN2T.convertirLetras(informacion.get("valorInicialCuenta").toString().trim(),ConstantesBD.cadenaUnidades,ConstantesBD.cadenaUnidadesDecimales)+" MCTE...$"+UtilidadTexto.formatearValores(informacion.get("valorInicialCuenta").toString().trim())+"\n\n";
            report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);
            
            //Anexo 992 Se quita la seccion de firmas y se reemplaza por la nueva
            
            
//          section = report.createSection("firmas", "firmasTable", 7, 2, 10);
//	  		section.setTableBorder("firmasTable", 0xFFFFFF, 0.0f);
//		    section.setTableCellBorderWidth("firmasTable", 0.5f);
//		    section.setTableCellPadding("firmasTable", 1);
//		    section.setTableSpaceBetweenCells("firmasTable", 0.5f);
//		    section.setTableCellsDefaultColors("firmasTable", 0xFFFFFF, 0xFFFFFF);
//		    
//		    report.font.setFontAttributes(0x000000, 10, true, false, false);
//		    section.setTableCellsColSpan(2);
//		    section.addTableTextCellAligned("firmasTable", "_____________________________", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
//		    section.addTableTextCellAligned("firmasTable", "Dr. Pedro Nel Hernadez Laguna", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
//		    section.addTableTextCellAligned("firmasTable", "Coordinador de Facturación", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
//		    section.addTableTextCellAligned("firmasTable", "                    ", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
//		    section.addTableTextCellAligned("firmasTable", "                      ", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
//		    report.font.setFontAttributes(0x000000, 10, false, false, false);
//		    section.setTableCellsColSpan(1);
//		    //se inserta informacion del convenio
//		    section.addTableTextCellAligned("firmasTable", informacion.get("nombreConvenio").toString(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
//		    section.addTableTextCellAligned("firmasTable", "Nit: "+informacion.get("NitConvenio"), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
//		    section.addTableTextCellAligned("firmasTable", "Dirección: "+informacion.get("direccion"), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
//		    section.addTableTextCellAligned("firmasTable", "Teléfono: "+informacion.get("telefono"), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    
		    //se añade la sección al documento
//			report.addSectionToDocument("firmas");
            /*************************************************************************************************************************/
        }
        else 
        {
        	/**********************************************ESTÁNDAR***************************************************************************/
        	report.font.setFontSizeAndAttributes(12,true,false,false);
        	String valorTotalCuentaCobro= UtilidadTexto.formatearValores(informacion.get("valorInicialCuenta").toString().trim(),2,true,true);
        	String cad =  "Valor Total:  $"+valorTotalCuentaCobro;
            report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_RIGHT,30);
            //Se verifica que haya pagos de cuenta de cobro
            if(!informacion.get("valorPagosCuenta").toString().equals("")&&Double.parseDouble(informacion.get("valorPagosCuenta").toString())>0)
            {
	        	String valorPagosCuentaCobro= UtilidadTexto.formatearValores(informacion.get("valorPagosCuenta").toString().trim(),2,true,true);
	        	cad =  "PAGOS:  $"+valorPagosCuentaCobro;
	        	report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_LEFT,20);
            }
            //Se verifica que haya pagos ajustes debito
        	if(!informacion.get("valorAjusteDebitoCuenta").toString().equals("")&&Double.parseDouble(informacion.get("valorAjusteDebitoCuenta").toString())>0)
        	{
	        	String valorAjusteDebitoCuentaCobro= UtilidadTexto.formatearValores(informacion.get("valorAjusteDebitoCuenta").toString().trim(),2,true,true);
	        	cad =  "AJUSTE DÉBITO:  $"+valorAjusteDebitoCuentaCobro;
	        	report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_LEFT,20);
        	}
        	//Se verifica que haya ajustes credito
        	if(!informacion.get("valorAjusteCreditoCuenta").toString().equals("")&&Double.parseDouble(informacion.get("valorAjusteCreditoCuenta").toString())>0)
        	{
	        	String valorAjusteCreditoCuentaCobro= UtilidadTexto.formatearValores(informacion.get("valorAjusteCreditoCuenta").toString().trim(),2,true,true);
	        	cad =  "AJUSTE CRÉDITO:  $"+valorAjusteCreditoCuentaCobro;
	            report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);
        	}
            
//          String saldoTotalCuentaCobro= UtilidadTexto.formatearValores(informacion.get("valorSaldoCuenta").toString().trim(),2,true,true);
//        	cad =  "SALDO TOTAL CUENTA DE COBRO:  $"+saldoTotalCuentaCobro;
//          report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_LEFT,30);
        	    
            report.font.setFontSizeAndAttributes(10,true,false,false);
            cad =  "TOTAL A PAGAR: "+UtilidadN2T.convertirLetras(informacion.get("valorSaldoCuenta").toString().trim(),ConstantesBD.cadenaUnidades,ConstantesBD.cadenaUnidadesDecimales)+" MCTE\n\n";
            report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);

            /*************************************************************************************************************************/
        }
        //**********************************************************************************************************************
        
        
        //Anexo 992 Agrego las nuevas firmas
        //NUEVA SECCION DE FIRMAS APLICA PARA FORMATO ESTANDAR, SASAIMA Y SUBA
        if (formato.equals(ConstantesIntegridadDominio.acronimoSasaima)||formato.equals(ConstantesIntegridadDominio.acronimoSuba)||formato.equals(ConstantesIntegridadDominio.acronimoEstandar))
        {
        	//Agrego las Firmas por Anexo 992
            
            if (ValoresPorDefecto.getImprimirFirmasImpresionCCCapitacion(usuario.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoSi))
            {
	            DtoFirmasValoresPorDefecto dtoFirmas=new DtoFirmasValoresPorDefecto();
	            dtoFirmas.setValorPorDefecto(ConstantesValoresPorDefecto.nombreValoresDefectoImprimirFirmasImpresionCCCapitacion);
	            dtoFirmas.setInstitucion(usuario.getCodigoInstitucionInt());
	            
	            forma.setListadoFirmas(ValoresPorDefecto.consultarFirmasValoresPorDefecto(dtoFirmas));
	            
	            int sizeArray=forma.getListadoFirmas().size();
	            
	            int col[]=crearCeldarFirmas(sizeArray);           
	            section = report.createSection("firmas", "firmasTable", 3, col[1], 10);
		  		section.setTableBorder("firmasTable", 0xFFFFFF, 0.0f);
			    section.setTableCellBorderWidth("firmasTable", 0.5f);
			    section.setTableCellPadding("firmasTable", 1);
			    section.setTableSpaceBetweenCells("firmasTable", 0.5f);
			    section.setTableCellsDefaultColors("firmasTable", 0xFFFFFF, 0xFFFFFF);
	            section.setTableBorderColor("firmasTable", 0xFFFFFF);
	            			    
			    report.font.setFontAttributes(0x000000, 10, true, false, false);
			   
	            //Para 1 Firma
			    if (sizeArray==1)
	            {
	            	if (!forma.getListadoFirmas().get(0).getFirmaDigital().trim().isEmpty())
	            		section.addTableImageCell("firmasTable",ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+System.getProperty("FIRMADIGITAL")+System.getProperty("file.separator")+forma.getListadoFirmas().get(0).getFirmaDigital(),40,0,0);		
	            	else
	            		section.addTableTextCellAligned("firmasTable", "____________________________________", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_BOTTOM,0,0);
	            	
	            	section.addTableTextCellAligned("firmasTable", forma.getListadoFirmas().get(0).getUsuario().toUpperCase(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,1,0);
	            	section.addTableTextCellAligned("firmasTable", forma.getListadoFirmas().get(0).getCargo(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,2,0);
	            }
			    //2 Firmas
	            else if (sizeArray==2)
	            {
	            	if (!forma.getListadoFirmas().get(0).getFirmaDigital().trim().isEmpty())
	            		section.addTableImageCell("firmasTable",ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+System.getProperty("FIRMADIGITAL")+System.getProperty("file.separator")+forma.getListadoFirmas().get(0).getFirmaDigital(),40,0,0);		
	            	else
	            		section.addTableTextCellAligned("firmasTable", "____________________________________", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_BOTTOM,0,0);
	            	
	            	section.addTableTextCellAligned("firmasTable", forma.getListadoFirmas().get(0).getUsuario().toUpperCase(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,1,0);
	            	section.addTableTextCellAligned("firmasTable", forma.getListadoFirmas().get(0).getCargo(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,2,0);
	            	
	            	if (!forma.getListadoFirmas().get(1).getFirmaDigital().trim().isEmpty())
	            		section.addTableImageCell("firmasTable",ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+System.getProperty("FIRMADIGITAL")+System.getProperty("file.separator")+forma.getListadoFirmas().get(1).getFirmaDigital(),40,0,1);		
	            	else
	            		section.addTableTextCellAligned("firmasTable", "____________________________________", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_BOTTOM,0,1);
	            	
	            	section.addTableTextCellAligned("firmasTable", forma.getListadoFirmas().get(1).getUsuario().toUpperCase(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,1,1);
	            	section.addTableTextCellAligned("firmasTable", forma.getListadoFirmas().get(1).getCargo(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,2,1);
	            } 
			    //3 Firmas
	            else if (sizeArray==3)
	            {
	            	if (!forma.getListadoFirmas().get(0).getFirmaDigital().trim().isEmpty())
	            		section.addTableImageCell("firmasTable",ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+System.getProperty("FIRMADIGITAL")+System.getProperty("file.separator")+forma.getListadoFirmas().get(0).getFirmaDigital(),40,0,0);		
	            	else
	            		section.addTableTextCellAligned("firmasTable", "____________________________________", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_BOTTOM,0,0);
	            	
	            	section.addTableTextCellAligned("firmasTable", forma.getListadoFirmas().get(0).getUsuario().toUpperCase(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,1,0);
	            	section.addTableTextCellAligned("firmasTable", forma.getListadoFirmas().get(0).getCargo(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,2,0);
	            	
	            	if (!forma.getListadoFirmas().get(1).getFirmaDigital().trim().isEmpty())
	            		section.addTableImageCell("firmasTable",ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+System.getProperty("FIRMADIGITAL")+System.getProperty("file.separator")+forma.getListadoFirmas().get(1).getFirmaDigital(),40,0,1);		
	            	else
	            		section.addTableTextCellAligned("firmasTable", "____________________________________", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_BOTTOM,0,1);
	            	
	            	section.addTableTextCellAligned("firmasTable", forma.getListadoFirmas().get(1).getUsuario().toUpperCase(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,1,1);
	            	section.addTableTextCellAligned("firmasTable", forma.getListadoFirmas().get(1).getCargo(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,2,1);
	            	
	            	if (!forma.getListadoFirmas().get(2).getFirmaDigital().trim().isEmpty())
	            		section.addTableImageCell("firmasTable",ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+System.getProperty("FIRMADIGITAL")+System.getProperty("file.separator")+forma.getListadoFirmas().get(2).getFirmaDigital(),40,3,0);		
	            	else
	            		section.addTableTextCellAligned("firmasTable", "____________________________________", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_BOTTOM,3,0);
	            	
	            	section.addTableTextCellAligned("firmasTable", forma.getListadoFirmas().get(2).getUsuario().toUpperCase(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,4,0);
	            	section.addTableTextCellAligned("firmasTable", forma.getListadoFirmas().get(2).getCargo(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,5,0);
	            }			    
			  //4 Firmas			    
	            else if (sizeArray==4)
	            {
	            	if (!forma.getListadoFirmas().get(0).getFirmaDigital().trim().isEmpty())
	            		section.addTableImageCell("firmasTable",ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+System.getProperty("FIRMADIGITAL")+System.getProperty("file.separator")+forma.getListadoFirmas().get(0).getFirmaDigital(),40,0,0);	            	
	            	else
	            		section.addTableTextCellAligned("firmasTable", "____________________________________", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_BOTTOM,0,0);
	            	
	            	section.addTableTextCellAligned("firmasTable", forma.getListadoFirmas().get(0).getUsuario().toUpperCase(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,1,0);
	            	section.addTableTextCellAligned("firmasTable", forma.getListadoFirmas().get(0).getCargo(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,2,0);
	            	
	            	if (!forma.getListadoFirmas().get(1).getFirmaDigital().trim().isEmpty())
	            	section.addTableImageCell("firmasTable",ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+System.getProperty("FIRMADIGITAL")+System.getProperty("file.separator")+forma.getListadoFirmas().get(1).getFirmaDigital(),40,0,1);
	            	else
	            		section.addTableTextCellAligned("firmasTable", "____________________________________", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_BOTTOM,0,1);

	            	section.addTableTextCellAligned("firmasTable", forma.getListadoFirmas().get(1).getUsuario().toUpperCase(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,1,1);
	            	section.addTableTextCellAligned("firmasTable", forma.getListadoFirmas().get(1).getCargo(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,2,1);

	            	if (!forma.getListadoFirmas().get(2).getFirmaDigital().trim().isEmpty())
	            		section.addTableImageCell("firmasTable",ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+System.getProperty("FIRMADIGITAL")+System.getProperty("file.separator")+forma.getListadoFirmas().get(2).getFirmaDigital(),40,3,0);
	            	else
	            		section.addTableTextCellAligned("firmasTable", "____________________________________", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_BOTTOM,3,0);
	            	
	            	section.addTableTextCellAligned("firmasTable", forma.getListadoFirmas().get(2).getUsuario().toUpperCase(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,4,0);
	            	section.addTableTextCellAligned("firmasTable", forma.getListadoFirmas().get(2).getCargo(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,5,0);
	            	
	            	if (!forma.getListadoFirmas().get(3).getFirmaDigital().trim().isEmpty())
	            		section.addTableImageCell("firmasTable",ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+System.getProperty("FIRMADIGITAL")+System.getProperty("file.separator")+forma.getListadoFirmas().get(3).getFirmaDigital(),40,3,1);
	            	else
	            		section.addTableTextCellAligned("firmasTable", "____________________________________", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_BOTTOM,3,1);
	            	
	            	section.addTableTextCellAligned("firmasTable", forma.getListadoFirmas().get(3).getUsuario().toUpperCase(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,4,1);
	            	section.addTableTextCellAligned("firmasTable", forma.getListadoFirmas().get(3).getCargo(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,5,1);
	            } 
	            report.addSectionToDocument("firmas");	            
            } 	
        }
        
        if (formato.equals(ConstantesIntegridadDominio.acronimoEstandar))
        {
        	 String pie=ValoresPorDefecto.getPiePaginaFormatoImpresionFacturaOCCCapitacion(usuario.getCodigoInstitucionInt());
             
         	if (!pie.equals(""))
         	{
         		section = report.createSection("pieParametrizabelEstandar", "pieParametrizabelEstandarTable", 1, 1, 10);
     	  		section.setTableBorder("pieParametrizabelEstandarTable", 0x000000, 0.0f);
     		    section.setTableCellBorderWidth("pieParametrizabelEstandarTable", 0.5f);
     		    section.setTableCellPadding("pieParametrizabelEstandarTable", 1);
     		    section.setTableSpaceBetweenCells("pieParametrizabelEstandarTable", 0.5f);
     		    section.setTableCellsDefaultColors("pieParametrizabelEstandarTable", 0xFFFFFF, 0xFFFFFF);
     		    
     		    section.setTableCellsColSpan(1);
     		    report.font.setFontAttributes(0x000000, 8, true, false, false);
     		    section.addTableTextCellAligned("pieParametrizabelEstandarTable", pie, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP,0,0);
     		    
     		    //section.addTableTextCellAligned("encabezadoParametrizabelEstandarTable", pie, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP,0,1);
     		    
     		    report.addSectionToDocument("pieParametrizabelEstandar");
         	}
             
        }
        //Fin Anexo 992
        
      
      //-- Cerrar el reporte 
        report.setUsuario(usuario.getLoginUsuario());
      report.closeReport(tipoImpresion);		
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
	
	/**
	 * Método implementado para imprimir del detalle de la cuenta de cobro en el formato de Suba
	 * @param mapaUPC
	 * @param mapaGrupoEtareo
	 * @param report
	 * @param POS
	 */
	private static void cuerpoImpresionFormatoSuba(HashMap mapaUPC, HashMap mapaGrupoEtareo, PdfReports report, int pos) 
	{
		iTextBaseTable section;
		int numUPC = Integer.parseInt(mapaUPC.get("numRegistros").toString());
    	
    	if(numUPC>0)
    	{
    		float widths[]={(float) 2,(float) 2, (float) 2,(float) 2, (float) 2};
    		double sumatoria = 0;
    		int numUsuarios = 0;
    		report.font.setFontSizeAndAttributes(12,true,false,false);
	    	
        	//Se crea seccion para cargues UPC
        	section = report.createSection("detalleUPC"+(pos>=0?"_"+pos:""), "detalleUPCTable", numUPC+1+(numUPC>1?1:0), 5, 10); //si ya hay mas de 2 contrato se debe hacer la totalizacion
	  		section.setTableBorder("detalleUPCTable", 0x000000, 0.5f);
		    section.setTableCellBorderWidth("detalleUPCTable", 0.5f);
		    section.setTableCellPadding("detalleUPCTable", 1);
		    section.setTableSpaceBetweenCells("detalleUPCTable", 0.5f);
		    section.setTableCellsDefaultColors("detalleUPCTable", 0xFFFFFF, 0x000000);
        	
		    //Se insertan los titulos de cada columna
		    report.font.setFontAttributes(0x000000, 10, true, false, false);
		    section.addTableTextCellAligned("detalleUPCTable", "Contrato", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("detalleUPCTable", "Valor del Contrato", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("detalleUPCTable", "UPC", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("detalleUPCTable", "N° Usuarios", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("detalleUPCTable", "Valor Total", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
        	
		    report.font.setFontAttributes(0x000000, 10, false, false, false);
            //Se inserta informacion del detalle cargue UPC
            for(int i=0;i<numUPC;i++)
            {
            	section.addTableTextCellAligned("detalleUPCTable", mapaUPC.get("numero_contrato_"+i)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
            	section.addTableTextCellAligned("detalleUPCTable", UtilidadTexto.formatearValores(mapaUPC.get("valor_contrato_"+i)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
            	if(!mapaUPC.get("valor_upc_contrato_"+i).toString().equals(""))
            		section.addTableTextCellAligned("detalleUPCTable", "$"+mapaUPC.get("valor_upc_contrato_"+i)+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
            	else
            		section.addTableTextCellAligned("detalleUPCTable", mapaUPC.get("porcentaje_upc_contrato_"+i)+"%", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
            	int totalPacientes = 0;
            	try
            	{
            		totalPacientes=Integer.parseInt(mapaUPC.get("total_pacientes_"+i)+"");
            	}
            	catch(Exception e)
            	{
            		totalPacientes = 0;
            	}
            	numUsuarios += totalPacientes;
            	section.addTableTextCellAligned("detalleUPCTable", mapaUPC.get("total_pacientes_"+i)+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
            	section.addTableTextCellAligned("detalleUPCTable", UtilidadTexto.formatearValores(mapaUPC.get("valor_total_"+i)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
            	sumatoria += Double.parseDouble(mapaUPC.get("valor_total_"+i)+"");
            	
            }
            
            if(numUPC>1)
            {
            	section.setTableCellsColSpan(3);
            	report.font.setFontAttributes(0x000000, 10, true, false, false);
            	section.addTableTextCellAligned("detalleUPCTable", "Total", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
            	section.setTableCellsColSpan(1);
            	section.addTableTextCellAligned("detalleUPCTable", numUsuarios+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
            	section.addTableTextCellAligned("detalleUPCTable", UtilidadTexto.formatearValores(sumatoria), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
            	report.font.setFontAttributes(0x000000, 10, false, false, false);
            }
            
            try 
            {
				section.getTableReference("detalleUPCTable").setWidths(widths);
			} 
            catch (BadElementException e) 
            {
				logger.error("Error al ajustar los anchos de la tabla de UPC: "+e);
			}
            report.addSectionToDocument("detalleUPC"+(pos>=0?"_"+pos:""));
    	}
        
        int numGrupoEtareo = Integer.parseInt(mapaGrupoEtareo.get("numRegistros").toString());
        
        if(numGrupoEtareo>0)
        {
        	
        	for(int i=0;i<numGrupoEtareo;i++)
        	{
        		float widths1[]={(float) 2.5,(float) 2.5, (float) 2.5,(float) 2.5};
        		//Se crea seccion encabezado para cargues GRUPO ETAREO
	        	section = report.createSection("encabezadoGrupoEtareo"+(pos>=0?"_"+pos:"")+"_"+i, "encabezadoGrupoEtareoTable", 1, 4, 5);
		  		section.setTableBorder("encabezadoGrupoEtareoTable", 0x000000, 0.5f);
			    section.setTableCellBorderWidth("encabezadoGrupoEtareoTable", 0.5f);
			    section.setTableCellPadding("encabezadoGrupoEtareoTable", 1);
			    section.setTableSpaceBetweenCells("encabezadoGrupoEtareoTable", 0.5f);
			    section.setTableCellsDefaultColors("encabezadoGrupoEtareoTable", 0xFFFFFF, 0x000000);
			    
			    //Se insertan títulos de cada columna
			    report.font.setFontAttributes(0x000000, 10, true, false, false);
			    section.addTableTextCellAligned("encabezadoGrupoEtareoTable", "Contrato", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    report.font.setFontAttributes(0x000000, 10, false, false, false);
			    section.addTableTextCellAligned("encabezadoGrupoEtareoTable", mapaGrupoEtareo.get("numero_contrato_"+i)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    report.font.setFontAttributes(0x000000, 10, true, false, false);
			    section.addTableTextCellAligned("encabezadoGrupoEtareoTable", "Valor del Contrato", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    report.font.setFontAttributes(0x000000, 10, false, false, false);
			    section.addTableTextCellAligned("encabezadoGrupoEtareoTable", UtilidadTexto.formatearValores(mapaGrupoEtareo.get("valor_contrato_"+i)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
			    
            	try 
            	{
					section.getTableReference("encabezadoGrupoEtareoTable").setWidths(widths1);
				} catch (BadElementException e) 
				{
					logger.error("Error al ajustar los anchos del encabezado de la tabla GRupo Etareo: "+e);
				}
            	report.addSectionToDocument("encabezadoGrupoEtareo"+(pos>=0?"_"+pos:"")+"_"+i);
            	
            	//Se crea sección para el detalle del cargue GRUPO ETAREO
            	HashMap mapaTemp = (HashMap) mapaGrupoEtareo.get("mapaDetalle_"+i);
            	int numDetalle = Integer.parseInt(mapaTemp.get("numRegistros").toString());
            	
            	if(numDetalle>0)
            	{
            		float widthsEtareo[]={(float) 1,(float) 1, (float) 1.5,(float) 1.5, (float) 1.5, (float) 1, (float) 1.5};
            		section = report.createSection("detalleGrupoEtareo"+(pos>=0?"_"+pos:"")+"_"+i, "detalleGrupoEtareoTable", numDetalle+2, 7, 10);
    		  		section.setTableBorder("detalleGrupoEtareoTable", 0x000000, 0.5f);
    			    section.setTableCellBorderWidth("detalleGrupoEtareoTable", 0.5f);
    			    section.setTableCellPadding("detalleGrupoEtareoTable", 1);
    			    section.setTableSpaceBetweenCells("detalleGrupoEtareoTable", 0.5f);
    			    section.setTableCellsDefaultColors("detalleGrupoEtareoTable", 0xFFFFFF, 0x000000);
            		
    			    //Se insertan títulos de cada columna
    			    report.font.setFontAttributes(0x000000, 10, true, false, false);
    			    section.addTableTextCellAligned("detalleGrupoEtareoTable", "Edad Inicial", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
    			    section.addTableTextCellAligned("detalleGrupoEtareoTable", "Edad Final", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
    			    section.addTableTextCellAligned("detalleGrupoEtareoTable", "Sexo", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
    			    section.addTableTextCellAligned("detalleGrupoEtareoTable", "Valor UPC", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
    			    section.addTableTextCellAligned("detalleGrupoEtareoTable", "% PyP", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
    			    section.addTableTextCellAligned("detalleGrupoEtareoTable", "N° Usuarios", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
    			    section.addTableTextCellAligned("detalleGrupoEtareoTable", "Valor Total", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
    			    report.font.setFontAttributes(0x000000, 10, false, false, false);
    			    
	            	for(int j=0;j<numDetalle;j++)
	            	{
	            		section.addTableTextCellAligned("detalleGrupoEtareoTable", mapaTemp.get("edad_inicial_"+j)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	            		section.addTableTextCellAligned("detalleGrupoEtareoTable", mapaTemp.get("edad_final_"+j)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	            		String sexo = mapaTemp.get("sexo_"+j).toString();
	            		if(sexo.equals(ConstantesBD.codigoSexoMasculino+""))
	            			sexo = "Masculino";
	            		else if(sexo.equals(ConstantesBD.codigoSexoFemenino+""))
	            			sexo = "Femenino";
	            		else
	            			sexo = "Ambos";
	            		section.addTableTextCellAligned("detalleGrupoEtareoTable", sexo, report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	            		section.addTableTextCellAligned("detalleGrupoEtareoTable", UtilidadTexto.formatearValores(mapaTemp.get("valor_upc_"+j)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	            		section.addTableTextCellAligned("detalleGrupoEtareoTable", mapaTemp.get("porcentaje_pyp_"+j)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	            		section.addTableTextCellAligned("detalleGrupoEtareoTable", mapaTemp.get("total_usuarios_"+j)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	            		section.addTableTextCellAligned("detalleGrupoEtareoTable", UtilidadTexto.formatearValores(mapaTemp.get("valor_total_"+j)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	            	}
	            	
	            	section.setTableCellsColSpan(6);
	            	report.font.setFontAttributes(0x000000, 10, true, false, false);
	            	section.addTableTextCellAligned("detalleGrupoEtareoTable", "Total", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	            	section.setTableCellsColSpan(1);
	            	section.addTableTextCellAligned("detalleGrupoEtareoTable", UtilidadTexto.formatearValores(mapaGrupoEtareo.get("valor_total_"+i)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	            	report.font.setFontAttributes(0x000000, 10, false, false, false);
	            	try 
	            	{
						section.getTableReference("detalleGrupoEtareoTable").setWidths(widthsEtareo);
					} 
	            	catch (BadElementException e) 
	            	{
						logger.error("Error al ajustar los anchos de la tabla del detalle del grupo etareo: "+e);
					}
	            	report.addSectionToDocument("detalleGrupoEtareo"+(pos>=0?"_"+pos:"")+"_"+i);
            	}
            	
        	}
        }
		
	}

	/**
	 * Método implementado para imprimir el cuerpo del formato Estándar
	 * @param mapaUPC
	 * @param mapaGrupoEtareo
	 * @param report
	 * @param pos 
	 */
	private static void cuerpoImpresionFormatoEstandar(HashMap mapaUPC, HashMap mapaGrupoEtareo, PdfReports report, int pos) 
	{
		iTextBaseTable section;
		int numUPC = Integer.parseInt(mapaUPC.get("numRegistros").toString());
    	
    	if(numUPC>0)
    	{
    		//float widths[]={(float) 2,(float) 1, (float) 1,(float) 1.5, (float) 1.5, (float) 1.5, (float) 1.5};
    		float widths[]={(float) 2,(float) 1, (float) 1,(float) 1.5, (float) 1.5};
    		report.font.setFontSizeAndAttributes(12,true,false,false);
	    
    		
        	//Se crea seccion para cargues UPC
        	section = report.createSection("detalleUPC"+(pos>=0?"_"+pos:""), "detalleUPCTable", numUPC+1, 5, 10);
	  		section.setTableBorder("detalleUPCTable", 0x000000, 0.5f);
		    section.setTableCellBorderWidth("detalleUPCTable", 0.5f);
		    section.setTableCellPadding("detalleUPCTable", 1);
		    section.setTableSpaceBetweenCells("detalleUPCTable", 0.5f);
		    section.setTableCellsDefaultColors("detalleUPCTable", 0xFFFFFF, 0x000000);
        	
		    //Se insertan los titulos de cada columna
		    report.font.setFontAttributes(0x000000, 10, true, false, false);
		    section.addTableTextCellAligned("detalleUPCTable", "Fecha Inicial - Fecha Final", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("detalleUPCTable", "Contrato", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("detalleUPCTable", "Usuarios", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("detalleUPCTable", "UPC", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    //section.addTableTextCellAligned("detalleUPCTable", "Ajustes", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    //section.addTableTextCellAligned("detalleUPCTable", "Valor Total", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("detalleUPCTable", "Total", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
        	
		    report.font.setFontAttributes(0x000000, 10, false, false, false);
            //Se inserta informacion del detalle cargue UPC
            for(int i=0;i<numUPC;i++)
            {
            	section.addTableTextCellAligned("detalleUPCTable", mapaUPC.get("fecha_inicial_"+i)+"- "+mapaUPC.get("fecha_final_"+i), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
            	section.addTableTextCellAligned("detalleUPCTable", mapaUPC.get("numero_contrato_"+i)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
            	section.addTableTextCellAligned("detalleUPCTable", mapaUPC.get("total_pacientes_"+i)+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
            	section.addTableTextCellAligned("detalleUPCTable", mapaUPC.get("upc_"+i)+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
            	//section.addTableTextCellAligned("detalleUPCTable", mapaUPC.get("ajustes_"+i)+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
            	//section.addTableTextCellAligned("detalleUPCTable", UtilidadTexto.formatearValores(mapaUPC.get("valor_total_"+i)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
            	section.addTableTextCellAligned("detalleUPCTable", UtilidadTexto.formatearValores(mapaUPC.get("total_"+i)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
            }
            
            try 
            {
				section.getTableReference("detalleUPCTable").setWidths(widths);
			} 
            catch (BadElementException e) 
            {
				logger.error("Error al ajustar los anchos de la tabla de UPC: "+e);
			}
            report.addSectionToDocument("detalleUPC"+(pos>=0?"_"+pos:""));
    	}
        
        int numGrupoEtareo = Integer.parseInt(mapaGrupoEtareo.get("numRegistros").toString());
        
        if(numGrupoEtareo>0)
        {
        	report.font.setFontSizeAndAttributes(12,true,false,false);
	    	String cad =  "POR CONCEPTO DE CAPITACIÓN GRUPO ETÁREO";
	        report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_CENTER,30);
        	
        	
        	
        	for(int i=0;i<numGrupoEtareo;i++)
        	{
        		float widths1[]={(float) 2.5,(float) 1, (float) 1.5,(float) 1.5, (float) 2, (float) 1.5};
        		//Se crea seccion encabezado para cargues GRUPO ETAREO
	        	section = report.createSection("encabezadoGrupoEtareo"+(pos>=0?"_"+pos:"")+"_"+i, "encabezadoGrupoEtareoTable", 2, 6, 5);
		  		section.setTableBorder("encabezadoGrupoEtareoTable", 0x000000, 0.5f);
			    section.setTableCellBorderWidth("encabezadoGrupoEtareoTable", 0.5f);
			    section.setTableCellPadding("encabezadoGrupoEtareoTable", 1);
			    section.setTableSpaceBetweenCells("encabezadoGrupoEtareoTable", 0.5f);
			    section.setTableCellsDefaultColors("encabezadoGrupoEtareoTable", 0xFFFFFF, 0x000000);
			    
			    //Se insertan títulos de cada columna
			    report.font.setFontAttributes(0x000000, 10, true, false, false);
			    section.addTableTextCellAligned("encabezadoGrupoEtareoTable", "Fecha Inicial - Fecha Final", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    section.addTableTextCellAligned("encabezadoGrupoEtareoTable", "Contrato", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    section.addTableTextCellAligned("encabezadoGrupoEtareoTable", "Usuarios", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    //section.addTableTextCellAligned("encabezadoGrupoEtareoTable", "Ajustes", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    //section.addTableTextCellAligned("encabezadoGrupoEtareoTable", "Valor Total", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    section.addTableTextCellAligned("encabezadoGrupoEtareoTable", "Total", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    report.font.setFontAttributes(0x000000, 10, false, false, false);
			    section.addTableTextCellAligned("encabezadoGrupoEtareoTable", mapaGrupoEtareo.get("fecha_inicial_"+i)+"- "+mapaGrupoEtareo.get("fecha_final_"+i), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
            	section.addTableTextCellAligned("encabezadoGrupoEtareoTable", mapaGrupoEtareo.get("numero_contrato_"+i)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
            	section.addTableTextCellAligned("encabezadoGrupoEtareoTable", mapaGrupoEtareo.get("total_pacientes_"+i)+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
            	//section.addTableTextCellAligned("encabezadoGrupoEtareoTable", mapaGrupoEtareo.get("ajustes_"+i)+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
            	//section.addTableTextCellAligned("encabezadoGrupoEtareoTable", UtilidadTexto.formatearValores(mapaGrupoEtareo.get("valor_total_"+i)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
            	section.addTableTextCellAligned("encabezadoGrupoEtareoTable", UtilidadTexto.formatearValores(mapaGrupoEtareo.get("total_"+i)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
            	
            	try 
            	{
					section.getTableReference("encabezadoGrupoEtareoTable").setWidths(widths1);
				} catch (BadElementException e) 
				{
					logger.error("Error al ajustar los anchos del encabezado de la tabla GRupo Etareo: "+e);
				}
            	report.addSectionToDocument("encabezadoGrupoEtareo"+(pos>=0?"_"+pos:"")+"_"+i);
            	
            	//Se crea sección para el detalle del cargue GRUPO ETAREO
            	HashMap mapaTemp = (HashMap) mapaGrupoEtareo.get("mapaDetalle_"+i);
            	int numDetalle = Integer.parseInt(mapaTemp.get("numRegistros").toString());
            	
            	if(numDetalle>0)
            	{
            		float widthsEtareo[]={(float) 1,(float) 1, (float) 1.5,(float) 1.5, (float) 1.5, (float) 1, (float) 1.5};
            		section = report.createSection("detalleGrupoEtareo"+(pos>=0?"_"+pos:"")+"_"+i, "detalleGrupoEtareoTable", numDetalle+2, 7, 10);
    		  		section.setTableBorder("detalleGrupoEtareoTable", 0x000000, 0.5f);
    			    section.setTableCellBorderWidth("detalleGrupoEtareoTable", 0.5f);
    			    section.setTableCellPadding("detalleGrupoEtareoTable", 1);
    			    section.setTableSpaceBetweenCells("detalleGrupoEtareoTable", 0.5f);
    			    section.setTableCellsDefaultColors("detalleGrupoEtareoTable", 0xFFFFFF, 0x000000);
            		
    			    //Se insertan títulos de cada columna
    			    report.font.setFontAttributes(0x000000, 10, true, false, false);
    			    section.addTableTextCellAligned("detalleGrupoEtareoTable", "Edad Inicial", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
    			    section.addTableTextCellAligned("detalleGrupoEtareoTable", "Edad Final", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
    			    section.addTableTextCellAligned("detalleGrupoEtareoTable", "Sexo", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
    			    section.addTableTextCellAligned("detalleGrupoEtareoTable", "Valor Grupo Etáreo", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
    			    section.addTableTextCellAligned("detalleGrupoEtareoTable", "Número de Personas", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
    			    section.addTableTextCellAligned("detalleGrupoEtareoTable", "% de PyP", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
    			    section.addTableTextCellAligned("detalleGrupoEtareoTable", "Valor Total", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
    			    report.font.setFontAttributes(0x000000, 10, false, false, false);
    			    
	            	for(int j=0;j<numDetalle;j++)
	            	{
	            		section.addTableTextCellAligned("detalleGrupoEtareoTable", mapaTemp.get("edad_inicial_"+j)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	            		section.addTableTextCellAligned("detalleGrupoEtareoTable", mapaTemp.get("edad_final_"+j)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	            		String sexo = mapaTemp.get("sexo_"+j).toString();
	            		if(sexo.equals(ConstantesBD.codigoSexoMasculino+""))
	            			sexo = "Masculino";
	            		else if(sexo.equals(ConstantesBD.codigoSexoFemenino+""))
	            			sexo = "Femenino";
	            		else
	            			sexo = "Ambos";
	            		section.addTableTextCellAligned("detalleGrupoEtareoTable", sexo, report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	            		section.addTableTextCellAligned("detalleGrupoEtareoTable", UtilidadTexto.formatearValores(mapaTemp.get("valor_"+j)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	            		section.addTableTextCellAligned("detalleGrupoEtareoTable", mapaTemp.get("total_usuarios_"+j)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	            		section.addTableTextCellAligned("detalleGrupoEtareoTable", mapaTemp.get("porcentaje_pyp_"+j)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	            		section.addTableTextCellAligned("detalleGrupoEtareoTable", UtilidadTexto.formatearValores(mapaTemp.get("valor_total_"+j)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	            	}
	            	
	            	section.setTableCellsColSpan(6);
	            	section.addTableTextCellAligned("detalleGrupoEtareoTable", "VALOR TOTAL POR GRUPO ETÁREO", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	            	section.setTableCellsColSpan(1);
	            	section.addTableTextCellAligned("detalleGrupoEtareoTable", UtilidadTexto.formatearValores(mapaGrupoEtareo.get("valor_total_"+i)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	            	try 
	            	{
						section.getTableReference("detalleGrupoEtareoTable").setWidths(widthsEtareo);
					} 
	            	catch (BadElementException e) 
	            	{
						logger.error("Error al ajustar los anchos de la tabla del detalle del grupo etareo: "+e);
					}
	            	report.addSectionToDocument("detalleGrupoEtareo"+(pos>=0?"_"+pos:"")+"_"+i);
            	}
            	
        	}
        }
		
	}

	/**
	 * Método implementado ara imprimir el cuerpo del formato Sasaima
	 * @param mapaUPC
	 * @param mapaGrupoEtareo
	 * @param report
	 * @param pos 
	 */
	private static void cuerpoImpresionFormatoSasaima(HashMap mapaUPC, HashMap mapaGrupoEtareo, PdfReports report, int pos) 
	{
		iTextBaseTable section;
		int numUPC = Integer.parseInt(mapaUPC.get("numRegistros").toString());
    	
    	if(numUPC>0)
    	{
    		float widths[]={(float) 2,(float) 2, (float) 2,(float) 2, (float) 2};
    		double sumatoria = 0;
    		report.font.setFontSizeAndAttributes(12,true,false,false);
	    	
        	//Se crea seccion para cargues UPC
        	section = report.createSection("detalleUPC"+(pos>=0?"_"+pos:""), "detalleUPCTable", numUPC+1+(numUPC>1?1:0), 5, 10); //si ya hay mas de 2 contrato se debe hacer la totalizacion
	  		section.setTableBorder("detalleUPCTable", 0x000000, 0.5f);
		    section.setTableCellBorderWidth("detalleUPCTable", 0.5f);
		    section.setTableCellPadding("detalleUPCTable", 1);
		    section.setTableSpaceBetweenCells("detalleUPCTable", 0.5f);
		    section.setTableCellsDefaultColors("detalleUPCTable", 0xFFFFFF, 0x000000);
        	
		    //Se insertan los titulos de cada columna
		    report.font.setFontAttributes(0x000000, 10, true, false, false);
		    section.addTableTextCellAligned("detalleUPCTable", "Contrato", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("detalleUPCTable", "Valor del Contrato", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("detalleUPCTable", "UPC", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("detalleUPCTable", "N° Usuarios", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("detalleUPCTable", "Valor Total", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
        	
		    report.font.setFontAttributes(0x000000, 10, false, false, false);
            //Se inserta informacion del detalle cargue UPC
            for(int i=0;i<numUPC;i++)
            {
            	section.addTableTextCellAligned("detalleUPCTable", mapaUPC.get("numero_contrato_"+i)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
            	section.addTableTextCellAligned("detalleUPCTable", UtilidadTexto.formatearValores(mapaUPC.get("valor_contrato_"+i)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
            	if(!mapaUPC.get("valor_upc_contrato_"+i).toString().equals(""))
            		section.addTableTextCellAligned("detalleUPCTable", "$"+mapaUPC.get("valor_upc_contrato_"+i)+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
            	else
            		section.addTableTextCellAligned("detalleUPCTable", mapaUPC.get("porcentaje_upc_contrato_"+i)+"%", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
            	section.addTableTextCellAligned("detalleUPCTable", mapaUPC.get("total_pacientes_"+i)+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
            	section.addTableTextCellAligned("detalleUPCTable", UtilidadTexto.formatearValores(mapaUPC.get("valor_total_"+i)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
            	sumatoria += Double.parseDouble(mapaUPC.get("valor_total_"+i)+"");
            	
            }
            
            if(numUPC>1)
            {
            	section.setTableCellsColSpan(4);
            	report.font.setFontAttributes(0x000000, 10, true, false, false);
            	section.addTableTextCellAligned("detalleUPCTable", "Total", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
            	section.setTableCellsColSpan(1);
            	section.addTableTextCellAligned("detalleUPCTable", UtilidadTexto.formatearValores(sumatoria), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
            	report.font.setFontAttributes(0x000000, 10, false, false, false);
            }
            
            try 
            {
				section.getTableReference("detalleUPCTable").setWidths(widths);
			} 
            catch (BadElementException e) 
            {
				logger.error("Error al ajustar los anchos de la tabla de UPC: "+e);
			}
            report.addSectionToDocument("detalleUPC"+(pos>=0?"_"+pos:""));
    	}
        
        int numGrupoEtareo = Integer.parseInt(mapaGrupoEtareo.get("numRegistros").toString());
        
        if(numGrupoEtareo>0)
        {
        	
        	for(int i=0;i<numGrupoEtareo;i++)
        	{
        		float widths1[]={(float) 2.5,(float) 2.5, (float) 2.5,(float) 2.5};
        		//Se crea seccion encabezado para cargues GRUPO ETAREO
	        	section = report.createSection("encabezadoGrupoEtareo"+(pos>=0?"_"+pos:"")+"_"+i, "encabezadoGrupoEtareoTable", 1, 4, 5);
		  		section.setTableBorder("encabezadoGrupoEtareoTable", 0x000000, 0.5f);
			    section.setTableCellBorderWidth("encabezadoGrupoEtareoTable", 0.5f);
			    section.setTableCellPadding("encabezadoGrupoEtareoTable", 1);
			    section.setTableSpaceBetweenCells("encabezadoGrupoEtareoTable", 0.5f);
			    section.setTableCellsDefaultColors("encabezadoGrupoEtareoTable", 0xFFFFFF, 0x000000);
			    
			    //Se insertan títulos de cada columna
			    report.font.setFontAttributes(0x000000, 10, true, false, false);
			    section.addTableTextCellAligned("encabezadoGrupoEtareoTable", "Contrato", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    report.font.setFontAttributes(0x000000, 10, false, false, false);
			    section.addTableTextCellAligned("encabezadoGrupoEtareoTable", mapaGrupoEtareo.get("numero_contrato_"+i)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    report.font.setFontAttributes(0x000000, 10, true, false, false);
			    section.addTableTextCellAligned("encabezadoGrupoEtareoTable", "Valor del Contrato", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    report.font.setFontAttributes(0x000000, 10, false, false, false);
			    section.addTableTextCellAligned("encabezadoGrupoEtareoTable", UtilidadTexto.formatearValores(mapaGrupoEtareo.get("valor_contrato_"+i)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
			    
            	try 
            	{
					section.getTableReference("encabezadoGrupoEtareoTable").setWidths(widths1);
				} catch (BadElementException e) 
				{
					logger.error("Error al ajustar los anchos del encabezado de la tabla GRupo Etareo: "+e);
				}
            	report.addSectionToDocument("encabezadoGrupoEtareo"+(pos>=0?"_"+pos:"")+"_"+i);
            	
            	//Se crea sección para el detalle del cargue GRUPO ETAREO
            	HashMap mapaTemp = (HashMap) mapaGrupoEtareo.get("mapaDetalle_"+i);
            	int numDetalle = Integer.parseInt(mapaTemp.get("numRegistros").toString());
            	
            	if(numDetalle>0)
            	{
            		float widthsEtareo[]={(float) 1,(float) 1, (float) 1.5,(float) 1.5, (float) 1.5, (float) 1, (float) 1.5};
            		section = report.createSection("detalleGrupoEtareo"+(pos>=0?"_"+pos:"")+"_"+i, "detalleGrupoEtareoTable", numDetalle+2, 7, 10);
    		  		section.setTableBorder("detalleGrupoEtareoTable", 0x000000, 0.5f);
    			    section.setTableCellBorderWidth("detalleGrupoEtareoTable", 0.5f);
    			    section.setTableCellPadding("detalleGrupoEtareoTable", 1);
    			    section.setTableSpaceBetweenCells("detalleGrupoEtareoTable", 0.5f);
    			    section.setTableCellsDefaultColors("detalleGrupoEtareoTable", 0xFFFFFF, 0x000000);
            		
    			    //Se insertan títulos de cada columna
    			    report.font.setFontAttributes(0x000000, 10, true, false, false);
    			    section.addTableTextCellAligned("detalleGrupoEtareoTable", "Edad Inicial", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
    			    section.addTableTextCellAligned("detalleGrupoEtareoTable", "Edad Final", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
    			    section.addTableTextCellAligned("detalleGrupoEtareoTable", "Sexo", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
    			    section.addTableTextCellAligned("detalleGrupoEtareoTable", "Valor UPC", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
    			    section.addTableTextCellAligned("detalleGrupoEtareoTable", "% PyP", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
    			    section.addTableTextCellAligned("detalleGrupoEtareoTable", "N° Usuarios", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
    			    section.addTableTextCellAligned("detalleGrupoEtareoTable", "Valor Total", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
    			    report.font.setFontAttributes(0x000000, 10, false, false, false);
    			    
	            	for(int j=0;j<numDetalle;j++)
	            	{
	            		section.addTableTextCellAligned("detalleGrupoEtareoTable", mapaTemp.get("edad_inicial_"+j)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	            		section.addTableTextCellAligned("detalleGrupoEtareoTable", mapaTemp.get("edad_final_"+j)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	            		String sexo = mapaTemp.get("sexo_"+j).toString();
	            		if(sexo.equals(ConstantesBD.codigoSexoMasculino+""))
	            			sexo = "Masculino";
	            		else if(sexo.equals(ConstantesBD.codigoSexoFemenino+""))
	            			sexo = "Femenino";
	            		else
	            			sexo = "Ambos";
	            		section.addTableTextCellAligned("detalleGrupoEtareoTable", sexo, report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	            		section.addTableTextCellAligned("detalleGrupoEtareoTable", UtilidadTexto.formatearValores(mapaTemp.get("valor_upc_"+j)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	            		section.addTableTextCellAligned("detalleGrupoEtareoTable", mapaTemp.get("porcentaje_pyp_"+j)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	            		section.addTableTextCellAligned("detalleGrupoEtareoTable", mapaTemp.get("total_usuarios_"+j)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	            		section.addTableTextCellAligned("detalleGrupoEtareoTable", UtilidadTexto.formatearValores(mapaTemp.get("valor_total_"+j)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	            	}
	            	
	            	section.setTableCellsColSpan(6);
	            	report.font.setFontAttributes(0x000000, 10, true, false, false);
	            	section.addTableTextCellAligned("detalleGrupoEtareoTable", "Total", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	            	section.setTableCellsColSpan(1);
	            	section.addTableTextCellAligned("detalleGrupoEtareoTable", UtilidadTexto.formatearValores(mapaGrupoEtareo.get("valor_total_"+i)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	            	report.font.setFontAttributes(0x000000, 10, false, false, false);
	            	try 
	            	{
						section.getTableReference("detalleGrupoEtareoTable").setWidths(widthsEtareo);
					} 
	            	catch (BadElementException e) 
	            	{
						logger.error("Error al ajustar los anchos de la tabla del detalle del grupo etareo: "+e);
					}
	            	report.addSectionToDocument("detalleGrupoEtareo"+(pos>=0?"_"+pos:"")+"_"+i);
            	}
            	
        	}
        }
		
	}

	/**
	 * Método implementado para cargar la informacion de la forma
	 * en un mapa
	 * @param forma
	 * @return
	 */
	private static HashMap llenarInformacion(CuentaCobroCapitacionForm forma) 
	{
		HashMap mapa = new HashMap();
		
		//Informacion del cliente------------------------
		mapa.put("prefijoFactura", forma.getPrefijoFactura());
		mapa.put("numeroCuentaCobro",forma.getCuentaCobroBuscar());
		mapa.put("estadoCuentaCobro",forma.getNombreEstadoCxCModificar());
		mapa.put("fechaGeneracion",forma.getFechaElaboracionModificar());
		mapa.put("fechaRadicacion",UtilidadCadena.noEsVacio(forma.getFechaRadicacion())?forma.getFechaRadicacion():"");
		mapa.put("numeroRadicacion", forma.getNumeroRadicacion());
		mapa.put("nombreConvenio",forma.getNombreConvenioModificar());
		mapa.put("NitConvenio",forma.getNitConvenio());
		mapa.put("fechaInicial", forma.getFechaInicialModificar());
		mapa.put("fechaFinal", forma.getFechaFinalModificar());
		mapa.put("direccion",forma.getDireccionConvenio());
		mapa.put("telefono",forma.getTelefonoConvenio());
		mapa.put("descripcion",forma.getObservacionesModificar());
		mapa.put("valorInicialCuenta", forma.getValorTotalCuentaCobro());
		mapa.put("valorPagosCuenta",forma.getPagosCuentaCobro());
		mapa.put("valorAjusteDebitoCuenta", forma.getValorAjusteDebito());
		mapa.put("valorAjusteCreditoCuenta",forma.getValorAjusteCredito());
		mapa.put("valorSaldoCuenta", forma.getSaldoTotalCuentaCobro());
		
		
		//Anexo 992
		mapa.put("horaElaboracion", forma.getHoraElaboracion());
		//Fin Anexo 992
		
		
		
		//Se organiza la informacion (se diferencia lo que es UPC de GRUPO ETAREO)*******************************************
		HashMap mapaUPC = new HashMap();
		HashMap mapaGrupoEtareo = new HashMap();
		int contUPC = 0;
		int contGrupoEtareo = 0;
		
		int numRegistros = UtilidadCadena.noEsVacio(forma.getMapaDetalleCuentaCobro("numRegistros")+"") ? Integer.parseInt(forma.getMapaDetalleCuentaCobro("numRegistros")+"") : 0;
		boolean esGrupoEtareo=UtilidadTexto.getBoolean(forma.getMapaDetalleCuentaCobro("esGrupoEtareo")+"");
		int numGrupoEtareo = 0;
		if(esGrupoEtareo)
			numGrupoEtareo = UtilidadCadena.noEsVacio(forma.getMapaCarguesGrupoEtareo("numRegistros")+"") ? Integer.parseInt(forma.getMapaCarguesGrupoEtareo("numRegistros")+"") : 0;
		
		boolean tieneGrupoEtareo = false;
		for(int i=0;i<numRegistros;i++)
		{
			//************SE TOMA LA INFORMACION DE GRUPO ETÁREO SE EXISTE************************
			tieneGrupoEtareo = false;
			HashMap mapaTemp = new HashMap();
			
			if(esGrupoEtareo)
			{
				int cont = 0;
				//Se verifica si el cargue tiene grupo etareo
				for(int j=0;j<numGrupoEtareo;j++)
				{
					if(forma.getMapaDetalleCuentaCobro("cargue_"+i).toString().equals(forma.getMapaCarguesGrupoEtareo("contrato_cargue_"+j).toString()))
					{
						tieneGrupoEtareo = true;
						mapaTemp.put("edad_inicial_"+cont,forma.getMapaCarguesGrupoEtareo("edad_inicial_"+j));
						mapaTemp.put("edad_final_"+cont,forma.getMapaCarguesGrupoEtareo("edad_final_"+j));
						mapaTemp.put("sexo_"+cont,forma.getMapaCarguesGrupoEtareo("sexo_"+j));
						mapaTemp.put("valor_"+cont,forma.getMapaCarguesGrupoEtareo("valor_"+j));
						mapaTemp.put("total_usuarios_"+cont,forma.getMapaCarguesGrupoEtareo("total_usuarios_"+j));
						mapaTemp.put("porcentaje_pyp_"+cont,forma.getMapaCarguesGrupoEtareo("porcentaje_pyp_"+j));
						mapaTemp.put("valor_total_"+cont,forma.getMapaCarguesGrupoEtareo("valor_total_"+j));
						mapaTemp.put("contrato_cargue_"+cont,forma.getMapaCarguesGrupoEtareo("contrato_cargue_"+j));
						mapaTemp.put("contrato_"+cont,forma.getMapaCarguesGrupoEtareo("contrato_"+j));
						mapaTemp.put("cuenta_cobro_"+cont,forma.getMapaCarguesGrupoEtareo("cuenta_cobro_"+j));
						mapaTemp.put("convenio_"+cont,forma.getMapaCarguesGrupoEtareo("convenio_"+j));
						mapaTemp.put("valor_upc_"+cont,forma.getMapaCarguesGrupoEtareo("valor_upc_"+j));
						cont++;
					}
				}
				mapaTemp.put("numRegistros", cont+"");
			}
			//*******************************************************************************
			
			if(tieneGrupoEtareo)
			{
				mapaGrupoEtareo.put("mapaDetalle_"+contGrupoEtareo,mapaTemp);
				mapaGrupoEtareo.put("fecha_inicial_"+contGrupoEtareo,forma.getMapaDetalleCuentaCobro("fecha_inicial_"+i));
				mapaGrupoEtareo.put("fecha_final_"+contGrupoEtareo,forma.getMapaDetalleCuentaCobro("fecha_final_"+i));
				mapaGrupoEtareo.put("total_pacientes_"+contGrupoEtareo,forma.getMapaDetalleCuentaCobro("total_pacientes_"+i));
				mapaGrupoEtareo.put("upc_"+contGrupoEtareo,forma.getMapaDetalleCuentaCobro("upc_"+i));
				mapaGrupoEtareo.put("ajustes_"+contGrupoEtareo,forma.getMapaDetalleCuentaCobro("ajustes_"+i));
				mapaGrupoEtareo.put("valor_total_"+contGrupoEtareo,forma.getMapaDetalleCuentaCobro("valor_total_"+i));
				mapaGrupoEtareo.put("total_"+contGrupoEtareo,forma.getMapaDetalleCuentaCobro("total_"+i));
				mapaGrupoEtareo.put("cargue_"+contGrupoEtareo,forma.getMapaDetalleCuentaCobro("cargue_"+i));
				mapaGrupoEtareo.put("numero_contrato_"+contGrupoEtareo,forma.getMapaDetalleCuentaCobro("numero_contrato_"+i));
				mapaGrupoEtareo.put("fecha_cargue_"+contGrupoEtareo,forma.getMapaDetalleCuentaCobro("fecha_cargue_"+i));
				mapaGrupoEtareo.put("saldo_"+contGrupoEtareo,forma.getMapaDetalleCuentaCobro("saldo_"+i));
				mapaGrupoEtareo.put("valor_contrato_"+contGrupoEtareo,forma.getMapaDetalleCuentaCobro("valor_contrato_"+i));
				contGrupoEtareo++;
			}
			else
			{
				mapaUPC.put("fecha_inicial_"+contUPC,forma.getMapaDetalleCuentaCobro("fecha_inicial_"+i));
				mapaUPC.put("fecha_final_"+contUPC,forma.getMapaDetalleCuentaCobro("fecha_final_"+i));
				mapaUPC.put("total_pacientes_"+contUPC,forma.getMapaDetalleCuentaCobro("total_pacientes_"+i));
				mapaUPC.put("upc_"+contUPC,forma.getMapaDetalleCuentaCobro("upc_"+i));
				mapaUPC.put("ajustes_"+contUPC,forma.getMapaDetalleCuentaCobro("ajustes_"+i));
				mapaUPC.put("valor_total_"+contUPC,forma.getMapaDetalleCuentaCobro("valor_total_"+i));
				mapaUPC.put("total_"+contUPC,forma.getMapaDetalleCuentaCobro("total_"+i));
				mapaUPC.put("cargue_"+contUPC,forma.getMapaDetalleCuentaCobro("cargue_"+i));
				mapaUPC.put("numero_contrato_"+contUPC,forma.getMapaDetalleCuentaCobro("numero_contrato_"+i));
				mapaUPC.put("fecha_cargue_"+contUPC,forma.getMapaDetalleCuentaCobro("fecha_cargue_"+i));
				mapaUPC.put("saldo_"+contUPC,forma.getMapaDetalleCuentaCobro("saldo_"+i));
				mapaUPC.put("valor_upc_contrato_"+contUPC,forma.getMapaDetalleCuentaCobro("valor_upc_contrato_"+i));
				mapaUPC.put("porcentaje_upc_contrato_"+contUPC,forma.getMapaDetalleCuentaCobro("porcentaje_upc_contrato_"+i));
				mapaUPC.put("valor_contrato_"+contUPC,forma.getMapaDetalleCuentaCobro("valor_contrato_"+i));
				contUPC++;
			}
		}
		mapaUPC.put("numRegistros",contUPC+"");
		mapaGrupoEtareo.put("numRegistros",contGrupoEtareo+"");
		
		mapa.put("mapaUPC", mapaUPC);
		mapa.put("mapaGrupoEtareo", mapaGrupoEtareo);
		return mapa;
	}

	/**
	 * Metodo para generar el PDF del listado de cuentas de cobro de capitación
	 * seleccionados en la generación
	 * @param filename
	 * @param forma
	 * @param usuario
	 * @param tipoImpresion 
	 * @param con
	 */
	public static void pdfListadoCuentasCobro(String filename, CuentaCobroCapitacionForm forma, UsuarioBasico usuario, Connection conec, HttpServletRequest request, String tipoImpresion) 
	{
		
		//-- Variable para manejar el reporte
        PdfReports report = new PdfReports();
        //-- Consultar la informacion de la Empresa.
        ParametrizacionInstitucion inst=new ParametrizacionInstitucion();
    	inst.cargar(conec, usuario.getCodigoInstitucionInt());
    	InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
    	//--- Tipo de Formato
    	String formato = inst.getFormatoImpresion();
    	report.setFormatoImpresion(formato);
        
       //********************SECCION INFORMACION DE LA INSTITUCION*****************************************************
    	///-- Tomando la ruta definida en el web.xml para generar el reporte.
    	String filePath=ValoresPorDefecto.getFilePath();
    	//-- Titulo del reporte
    	String tituloReporte=" CONSULTA CUENTA COBRO CAPITACIÓN";
    
        if(formato.equals(ConstantesIntegridadDominio.acronimoSasaima))
        {
        	/***********************SASAIMA************************************/
        	report.setReportBaseHeaderSasaima(institucionBasica);
        }
        else if(formato.equals(ConstantesIntegridadDominio.acronimoSuba))
        {
        	/***********************SUBA****************************************/
        	String filePathLogo = ValoresPorDefecto.getFilePath(); //path para el logo de Bogotá sin indiferencia
        	if(System.getProperty("file.separator").equals("/"))
        	{
        		filePath=filePath.substring(0, filePath.lastIndexOf("/",(filePath.length()-2)))+"/imagenes/logo-grey.gif";
        		filePathLogo=filePathLogo.substring(0, filePathLogo.lastIndexOf("/",(filePathLogo.length()-2)))+"/imagenes/bogsin2.gif";
        		
        	}
        	else
        	{
        		filePath=filePath.substring(0, filePath.lastIndexOf("\\",(filePath.length()-2)))+"\\imagenes\\logo-grey.gif";
        		filePathLogo=filePathLogo.substring(0, filePathLogo.lastIndexOf("\\",(filePathLogo.length()-2)))+"\\imagenes\\bogsin2.gif";
        	}
        	report.setReportBaseHeaderSuba(filePath,institucionBasica,null,false);
        	//Se asignan los datos del pie de pagina
        	report.setMapaPiePagina("filePathLogo", filePathLogo);
        	report.setMapaPiePagina("infoInstitucion", "Dirección: "+institucionBasica.getDireccion()+" Conmutador: "+institucionBasica.getTelefono());
        }
        else
        {
        	/*******************************ESTANDAR*******************************************************************************/
        	//------Encabezado de la página ---------------------//
        	String encabezado =  " " + institucionBasica.getRazonSocial() + "  \n " +institucionBasica.getTipoIdentificacion()+" "+institucionBasica.getNit() + 
        						"\n Dirección " + institucionBasica.getDireccion() +  "  \n" +" Telefono " + institucionBasica.getTelefono() +  "  ";
        
	        //Generacion de la cabecera, validando el tipo de separador e utilizado en el path
	        report.setReportBaseHeader1(institucionBasica,"align-left" , tituloReporte);
	    }
        //***************************************************************************************************************
        
        //Se abre el reporte
        report.openReport(filename);
        report.font.useFont(iTextBaseFont.FONT_TIMES, true, true, true);
        iTextBaseTable section;
        //Se pasa la informacion a mapa
        HashMap mapaCuentasCobro = (HashMap) forma.getMapaCuentasCobroConvenio(); //registro de las cuentas de cobro a imprimir
        mapaCuentasCobro = llenarInformacionListado(forma,mapaCuentasCobro);
        
        /***************************************************************************************************************************/
        /*******************************ITERACION DE LAS CUENTAS DE COBRO***********************************************************/
        /***************************************************************************************************************************/
        boolean primeraPagina = false;
        //---Número de registros de cuentas de cobro mostradas en el listado -------//
        int numCuentasCobro=0;
        if(mapaCuentasCobro != null && UtilidadCadena.noEsVacio(mapaCuentasCobro.get("numRegistros")+""))
        	numCuentasCobro=Integer.parseInt(mapaCuentasCobro.get("numRegistros")+"");
        for(int i=0;i<numCuentasCobro;i++)
        {
        	if(UtilidadTexto.getBoolean(mapaCuentasCobro.get("imprimir_"+i).toString()))
        	{
        	
	        	//Validacion para saber si se debe añadir una nueva página
	        	if(primeraPagina)
		        	report.document.addNewPage();
		        
		        primeraPagina=true;
	        	
	        	//**************SECCION INFORMACION CLIENTE*********************************************************************
	            if(formato.equals(ConstantesIntegridadDominio.acronimoSasaima))
	            {
	            	/***********************SASAIMA************************************/
	            	section = report.createSection("informacionCliente_"+i, "informacionClienteTable", 4, 2, 10);
	    	  		section.setTableBorder("informacionClienteTable", 0xFFFFFF, 0.0f);
	    		    section.setTableCellBorderWidth("informacionClienteTable", 0.5f);
	    		    section.setTableCellPadding("informacionClienteTable", 1);
	    		    section.setTableSpaceBetweenCells("informacionClienteTable", 0.5f);
	    		    section.setTableCellsDefaultColors("informacionClienteTable", 0xFFFFFF, 0xFFFFFF);
	    		    
	    		    section.setTableCellsColSpan(2);
	    		    report.font.setFontAttributes(0x000000, 10, true, false, false);
	    		    section.addTableTextCellAligned("informacionClienteTable", "FACTURA N° "+mapaCuentasCobro.get("cuenta_cobro_"+i), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    		    report.font.setFontAttributes(0x000000, 10, false, false, false);
	    		    section.addTableTextCellAligned("informacionClienteTable", "Período Facturación: "+mapaCuentasCobro.get("fecha_inicial_"+i)+" a "+mapaCuentasCobro.get("fecha_final_"+i), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    		    section.setTableCellsColSpan(1);
	    		    section.addTableTextCellAligned("informacionClienteTable", "Cliente: "+mapaCuentasCobro.get("nombre_convenio_"+i), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    		    section.addTableTextCellAligned("informacionClienteTable", "Nit: "+mapaCuentasCobro.get("nit_convenio_"+i), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    		    section.addTableTextCellAligned("informacionClienteTable", "Dirección: "+mapaCuentasCobro.get("direccion_"+i), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    		    section.addTableTextCellAligned("informacionClienteTable", "Teléfono: "+mapaCuentasCobro.get("telefono_"+i), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    		    //se añade la sección al documento
	    		    report.addSectionToDocument("informacionCliente_"+i);
	            }
	            else if(formato.equals(ConstantesIntegridadDominio.acronimoEstandar))
	            {
	            	/*******************************ESTANDAR*******************************************************************************/
	            	//-------------------------------- Información del encabezao de la cuenta de cobro ----------------------------------------------
	                report.font.setFontSizeAndAttributes(11,true,false,false);
	                String cad = "Cuenta de Cobro No. " + mapaCuentasCobro.get("cuenta_cobro_"+i) + "          Estado:" + mapaCuentasCobro.get("nombre_estado_"+i) + "        Fecha Generación:"+ mapaCuentasCobro.get("fecha_elaboracion_"+i);
	            	report.document.addParagraph(cad,report.font,iTextBaseDocument.ALIGNMENT_LEFT,30);
	            	 if(!mapaCuentasCobro.get("fecha_radicacion_"+i).toString().equals(""))
	            	 {
	            	    cad="Número Radicación: "+mapaCuentasCobro.get("numero_radicacion_"+i) + "        Fecha Radicación: "+mapaCuentasCobro.get("fecha_radicacion_"+i);
	            	    report.document.addParagraph(cad,report.font,iTextBaseDocument.ALIGNMENT_LEFT,20);
	            	 }
	            	
	            	cad =  "Convenio: " +mapaCuentasCobro.get("nombre_convenio_"+i)+  "        Nit: " + mapaCuentasCobro.get("nit_convenio_"+i);
	            	report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_LEFT,20);
	            	
	            	cad =  "Período de Facturación: " +mapaCuentasCobro.get("fecha_inicial_"+i)+  "  a  " + mapaCuentasCobro.get("fecha_final_"+i);
	            	report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_LEFT,20);
	            }
	            //***************************************************************************************************************
	        	
	        	
	        	//***********************SECCION DESCRIPCION**************************************************************
	            if(formato.equals(ConstantesIntegridadDominio.acronimoSasaima))
	            {
	            	/***********************SASAIMA************************************/
	            	section = report.createSection("descripcion_"+i, "descripcionTable", 2, 1, 10);
	    	  		section.setTableBorder("descripcionTable", 0xFFFFFF, 0.0f);
	    		    section.setTableCellBorderWidth("descripcionTable", 0.5f);
	    		    section.setTableCellPadding("descripcionTable", 1);
	    		    section.setTableSpaceBetweenCells("descripcionTable", 0.5f);
	    		    section.setTableCellsDefaultColors("descripcionTable", 0xFFFFFF, 0xFFFFFF);
	    		    
	    		    report.font.setFontAttributes(0x000000, 10, true, false, false);
	    		    section.addTableTextCellAligned("descripcionTable", "Descripción:", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    		    report.font.setFontAttributes(0x000000, 10, false, false, false);
	    		    section.setTableCellsDefaultColors("descripcionTable", 0xFFFFFF, 0x000000);
	    		    section.addTableTextCellAligned("descripcionTable", mapaCuentasCobro.get("obs_generacion_"+i)+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    		    
	    		    //se añade la sección al documento
	    		    report.addSectionToDocument("descripcion_"+i);
	            }
	            else if(formato.equals(ConstantesIntegridadDominio.acronimoSuba))
	            {
	            	/***********************SUBA************************************/
	            	section = report.createSection("descripcion_"+i, "descripcionTable", 4, 1, 10);
	    	  		section.setTableBorder("descripcionTable", 0xFFFFFF, 0.0f);
	    		    section.setTableCellBorderWidth("descripcionTable", 0.5f);
	    		    section.setTableCellPadding("descripcionTable", 1);
	    		    section.setTableSpaceBetweenCells("descripcionTable", 0.5f);
	    		    section.setTableCellsDefaultColors("descripcionTable", 0xFFFFFF, 0xFFFFFF);

	    		    report.font.setFontAttributes(0x000000, 10, true, false, false);
	    		    section.addTableTextCellAligned("descripcionTable", "FACTURA N° " + mapaCuentasCobro.get("cuenta_cobro_"+i).toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    		    section.addTableTextCellAligned("descripcionTable", "", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    		    section.addTableTextCellAligned("descripcionTable", "Descripción:", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    		    report.font.setFontAttributes(0x000000, 10, false, false, false);
	    		    section.setTableCellsDefaultColors("descripcionTable", 0xFFFFFF, 0x000000);
	    		    section.addTableTextCellAligned("descripcionTable", mapaCuentasCobro.get("obs_generacion_"+i)+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    		    
	    		    //se añade la sección al documento
	    		    report.addSectionToDocument("descripcion_"+i);
	            }
	            else
	            {
	            	/*******************************ESTANDAR*******************************************************************************/
	            	report.font.setFontSizeAndAttributes(11,true,false,false);
	            	String cad =  " Observaciones: " + mapaCuentasCobro.get("obs_generacion_"+i);
	                report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_LEFT,20);
	            }
	            //**********************************************************************************************************
	            
	            //Se toman los mapas del detalle
	            HashMap mapaUPC = (HashMap) mapaCuentasCobro.get("mapaUPC_"+i);
	            HashMap mapaGrupoEtareo = (HashMap) mapaCuentasCobro.get("mapaGrupoEtareo_"+i);
	            
		        //**************SECCION DETALLE DE LA CUENTA DE COBRO********************************************************
		        if(formato.equals(ConstantesIntegridadDominio.acronimoSasaima))
		        {
		        	/***********************SASAIMA************************************/
		        	cuerpoImpresionFormatoSasaima(mapaUPC,mapaGrupoEtareo,report,i);
		        	/*****************************************************************/
		        }
		        else if(formato.equals(ConstantesIntegridadDominio.acronimoSuba))
		        {
		        	/***********************SUBA************************************/
		        	cuerpoImpresionFormatoSuba(mapaUPC,mapaGrupoEtareo,report,i);
		        	/*****************************************************************/
		        }
		        else
		        {
		        	/*******************************ESTANDAR*******************************************************************************/
		        	cuerpoImpresionFormatoEstandar(mapaUPC,mapaGrupoEtareo,report,i);
		            /***************************************************************************************************************************/
		        }
		        //************************************************************************************************************
	            
	            //**************SECCIÓN VALOR DE LA FATURA Y FIRMAS********************************************************************
	            if(formato.equals(ConstantesIntegridadDominio.acronimoSasaima))
	            {
	            	/*********************************************SASAIMA**********************************************************************/
	            	report.font.setFontSizeAndAttributes(11,true,false,false);
	                String cad =  "Son: "+UtilidadN2T.convertirLetras(mapaCuentasCobro.get("valor_inicial_cuenta_"+i).toString().trim(),ConstantesBD.cadenaUnidades,ConstantesBD.cadenaUnidadesDecimales)+" MCTE.";
	                report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);
	                
	                section = report.createSection("firmas_"+i, "firmasTable", 2, 3, 10);
	    	  		section.setTableBorder("firmasTable", 0xFFFFFF, 0.0f);
	    		    section.setTableCellBorderWidth("firmasTable", 0.5f);
	    		    section.setTableCellPadding("firmasTable", 1);
	    		    section.setTableSpaceBetweenCells("firmasTable", 0.5f);
	    		    section.setTableCellsDefaultColors("firmasTable", 0xFFFFFF, 0xFFFFFF);
	    		    
	    		    report.font.setFontAttributes(0x000000, 10, true, false, false);
	    		    section.addTableTextCellAligned("firmasTable", "_____________________________", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    		    section.addTableTextCellAligned("firmasTable", "", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    		    section.addTableTextCellAligned("firmasTable", "_____________________________", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    		    section.addTableTextCellAligned("firmasTable", "Firma Institución:", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    		    section.addTableTextCellAligned("firmasTable", "", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    		    section.addTableTextCellAligned("firmasTable", "Firma Cliente:", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    		    
	    		    
	    		    //se añade la sección al documento
	    		    report.addSectionToDocument("firmas_"+i);
	                /*************************************************************************************************************************/
	            }
	            else if(formato.equals(ConstantesIntegridadDominio.acronimoSuba))
	            {
	            	/*********************************************SUBA**********************************************************************/
	            	report.font.setFontSizeAndAttributes(11,true,false,false);
	                String cad =  "Son: "+UtilidadN2T.convertirLetras(mapaCuentasCobro.get("valor_inicial_cuenta_"+i).toString().trim(),ConstantesBD.cadenaUnidades,ConstantesBD.cadenaUnidadesDecimales)+" MCTE...$"+UtilidadTexto.formatearValores(mapaCuentasCobro.get("valor_inicial_cuenta_"+i).toString().trim());
	                report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);
	                
	                section = report.createSection("firmas_"+i, "firmasTable", 7, 2, 10);
	    	  		section.setTableBorder("firmasTable", 0xFFFFFF, 0.0f);
	    		    section.setTableCellBorderWidth("firmasTable", 0.5f);
	    		    section.setTableCellPadding("firmasTable", 1);
	    		    section.setTableSpaceBetweenCells("firmasTable", 0.5f);
	    		    section.setTableCellsDefaultColors("firmasTable", 0xFFFFFF, 0xFFFFFF);
	    		    
	    		    report.font.setFontAttributes(0x000000, 10, true, false, false);
	    		    section.setTableCellsColSpan(2);
	    		    section.addTableTextCellAligned("firmasTable", "_____________________________", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    		    section.addTableTextCellAligned("firmasTable", "Dr. Pedro Nel Hernadez Laguna", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    		    section.addTableTextCellAligned("firmasTable", "Coordinador de Facturación", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    		    section.addTableTextCellAligned("firmasTable", "                    ", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    		    section.addTableTextCellAligned("firmasTable", "                      ", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    		    report.font.setFontAttributes(0x000000, 10, false, false, false);
	    		    section.setTableCellsColSpan(1);
	    		    //se inserta informacion del convenio
	    		    section.addTableTextCellAligned("firmasTable", mapaCuentasCobro.get("nombre_convenio_"+i).toString(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    		    section.addTableTextCellAligned("firmasTable", "Nit: "+mapaCuentasCobro.get("nit_convenio_"+i), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    		    section.addTableTextCellAligned("firmasTable", "Dirección: "+mapaCuentasCobro.get("direccion_"+i), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    		    section.addTableTextCellAligned("firmasTable", "Teléfono: "+mapaCuentasCobro.get("telefono_"+i), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    		    
	    		    //se añade la sección al documento
	    		    report.addSectionToDocument("firmas_"+i);
	                /*************************************************************************************************************************/
	            }
	            else
	            {
	            	/**********************************************ESTÁNDAR***************************************************************************/
	            	report.font.setFontSizeAndAttributes(12,true,false,false);
	            	String valorTotalCuentaCobro= UtilidadTexto.formatearValores(mapaCuentasCobro.get("valor_inicial_cuenta_"+i).toString().trim(),2,true,true);
	            	String cad =  "VALOR TOTAL:  $"+valorTotalCuentaCobro;
	                report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_LEFT,20);
	                //Se verifica que haya pagos de cuenta de cobro
	                if(!mapaCuentasCobro.get("valor_pagos_"+i).toString().equals("")&&Double.parseDouble(mapaCuentasCobro.get("valor_pagos_"+i).toString())>0)
	                {
	    	        	String valorPagosCuentaCobro= UtilidadTexto.formatearValores(mapaCuentasCobro.get("valor_pagos_"+i).toString().trim(),2,true,true);
	    	        	cad =  "PAGOS:  $"+valorPagosCuentaCobro;
	    	        	report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_LEFT,20);
	                }
	                //Se verifica que haya pagos ajustes debito
	            	if(!mapaCuentasCobro.get("ajuste_debito_"+i).toString().equals("")&&Double.parseDouble(mapaCuentasCobro.get("ajuste_debito_"+i).toString())>0)
	            	{
	    	        	String valorAjusteDebitoCuentaCobro= UtilidadTexto.formatearValores(mapaCuentasCobro.get("ajuste_debito_"+i).toString().trim(),2,true,true);
	    	        	cad =  "AJUSTE DÉBITO:  $"+valorAjusteDebitoCuentaCobro;
	    	        	report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_LEFT,20);
	            	}
	            	//Se verifica que haya ajustes credito
	            	if(!mapaCuentasCobro.get("ajuste_credito_"+i).toString().equals("")&&Double.parseDouble(mapaCuentasCobro.get("ajuste_credito_"+i).toString())>0)
	            	{
	    	        	String valorAjusteCreditoCuentaCobro= UtilidadTexto.formatearValores(mapaCuentasCobro.get("ajuste_credito_"+i).toString().trim(),2,true,true);
	    	        	cad =  "AJUSTE CRÉDITO:  $"+valorAjusteCreditoCuentaCobro;
	    	            report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);
	            	}
	                
	                String saldoTotalCuentaCobro= UtilidadTexto.formatearValores(mapaCuentasCobro.get("saldo_cuenta_"+i).toString().trim(),2,true,true);
	            	cad =  "SALDO TOTAL CUENTA DE COBRO:  $"+saldoTotalCuentaCobro;
	                report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_LEFT,30);
	            	    
	                report.font.setFontSizeAndAttributes(11,true,false,false);
	                cad =  "Valor en Letras: "+UtilidadN2T.convertirLetras(mapaCuentasCobro.get("saldo_cuenta_"+i).toString().trim(),ConstantesBD.cadenaUnidades,ConstantesBD.cadenaUnidadesDecimales);
	                report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);
	                
	          	  	cad =  "___________________________";
	                report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_LEFT,60);
	                cad =  "Firma Cartera";
	                report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_LEFT,17);
	                /*************************************************************************************************************************/
	            }
	            //**********************************************************************************************************************
        	}
        	
        }
        /***************************************************************************************************************************/
        /*******************************FIN ITERACION DE LAS CUENTAS DE COBRO*******************************************************/
        /***************************************************************************************************************************/
        
        
        
        
        //-- Cerrar el reporte 
        report.closeReport(tipoImpresion);		
	}
	
	/**
	 * Método implementado para organizar la informacion del detalle de cada cuenta de cobro
	 * @param mapa 
	 */
	private static HashMap llenarInformacionListado(CuentaCobroCapitacionForm forma, HashMap mapa) 
	{
		//Se toma el número de registros de cuentas de cobro
		int numCuentasCobro=0;
        if(mapa != null && UtilidadCadena.noEsVacio(mapa.get("numRegistros")+""))
        	numCuentasCobro=Integer.parseInt(mapa.get("numRegistros")+"");
        
        
        //Se toma el numero de contratos de todas las cuentas de cobro
        int numDetImpresion = 0;
        if(forma.getMapaDetImpresion() != null && UtilidadCadena.noEsVacio(forma.getMapaDetImpresion("numRegistros")+""))
        	numDetImpresion = Integer.parseInt(forma.getMapaDetImpresion("numRegistros")+"");
        
        //se toma el numero de registros Grupo Etareo de los contratos de todas las cuentas de cobro
        int numGrupoEtareo = 0;
        if(forma.getMapaCarguesGrupoEtareo()!=null)
			numGrupoEtareo = UtilidadCadena.noEsVacio(forma.getMapaCarguesGrupoEtareo("numRegistros")+"") ? Integer.parseInt(forma.getMapaCarguesGrupoEtareo("numRegistros")+"") : 0;
       
        //Iteracion de cada cuenta de cobro
        for(int i=0;i<numCuentasCobro;i++)
        {
        	HashMap mapaUPC = new HashMap();
        	HashMap mapaGrupoEtareo = new HashMap();
        	int contUPC = 0;
        	int contGrupoEtareo = 0;
        	
        	//Iteracion del detalle de cada cuenta cobro
        	for(int j=0;j<numDetImpresion;j++)
        	{
        		if(mapa.get("cuenta_cobro_"+i).toString().equals(forma.getMapaDetImpresion("cuenta_cobro_"+j).toString()))
        		{
        			//Se verifica el tipo de pago para saber si es UPC o GRUPO ETAREO
        			if(forma.getMapaDetImpresion("tipo_pago_"+j).toString().equals(ConstantesBD.codigoTipoPagoUpc))
        			{
        				//*******************UPC***********************************************
        				mapaUPC.put("fecha_inicial_"+contUPC,forma.getMapaDetImpresion("fecha_inicial_"+j));
        				mapaUPC.put("fecha_final_"+contUPC,forma.getMapaDetImpresion("fecha_final_"+j));
        				mapaUPC.put("numero_contrato_"+contUPC,forma.getMapaDetImpresion("contrato_"+j));
        				mapaUPC.put("total_pacientes_"+contUPC,forma.getMapaDetImpresion("total_pacientes_"+j));
        				mapaUPC.put("upc_"+contUPC,forma.getMapaDetImpresion("upc_"+j));
        				mapaUPC.put("ajustes_"+contUPC,forma.getMapaDetImpresion("ajustes_"+j));
        				mapaUPC.put("valor_total_"+contUPC,forma.getMapaDetImpresion("valor_total_"+j));
        				mapaUPC.put("total_"+contUPC,forma.getMapaDetImpresion("total_"+j));
        				mapaUPC.put("cargue_"+contUPC,forma.getMapaDetImpresion("contrato_cargue_"+j));
        				mapaUPC.put("fecha_cargue_"+contUPC,forma.getMapaDetImpresion("fecha_cargue_"+j));
        				mapaUPC.put("saldo_"+contUPC,forma.getMapaDetImpresion("saldo_"+j));
        				mapaUPC.put("valor_upc_contrato_"+contUPC,forma.getMapaDetImpresion("valor_upc_contrato_"+j));
        				mapaUPC.put("porcentaje_upc_contrato_"+contUPC,forma.getMapaDetImpresion("porcentaje_upc_contrato_"+j));
        				mapaUPC.put("valor_contrato_"+contUPC,forma.getMapaDetImpresion("valor_contrato_"+j));
        				contUPC++;
        				//**********************************************************************
        			}
        			else
        			{
        				//***************GRUPO ETAREO**********************************************+
        				mapaGrupoEtareo.put("fecha_inicial_"+contGrupoEtareo,forma.getMapaDetImpresion("fecha_inicial_"+j));
        				mapaGrupoEtareo.put("fecha_final_"+contGrupoEtareo,forma.getMapaDetImpresion("fecha_final_"+j));
        				mapaGrupoEtareo.put("numero_contrato_"+contGrupoEtareo,forma.getMapaDetImpresion("contrato_"+j));
        				mapaGrupoEtareo.put("total_pacientes_"+contGrupoEtareo,forma.getMapaDetImpresion("total_pacientes_"+j));
        				mapaGrupoEtareo.put("upc_"+contGrupoEtareo,forma.getMapaDetImpresion("upc_"+j));
        				mapaGrupoEtareo.put("ajustes_"+contGrupoEtareo,forma.getMapaDetImpresion("ajustes_"+j));
        				mapaGrupoEtareo.put("valor_total_"+contGrupoEtareo,forma.getMapaDetImpresion("valor_total_"+j));
        				mapaGrupoEtareo.put("total_"+contGrupoEtareo,forma.getMapaDetImpresion("total_"+j));
        				mapaGrupoEtareo.put("cargue_"+contGrupoEtareo,forma.getMapaDetImpresion("contrato_cargue_"+j));
        				mapaGrupoEtareo.put("fecha_cargue_"+contGrupoEtareo,forma.getMapaDetImpresion("fecha_cargue_"+j));
        				mapaGrupoEtareo.put("saldo_"+contGrupoEtareo,forma.getMapaDetImpresion("saldo_"+j));
        				mapaGrupoEtareo.put("valor_contrato_"+contGrupoEtareo,forma.getMapaDetImpresion("valor_contrato_"+j));
        				
        				//****iteracion del detalle de un contrato grupo etareo**************
        				HashMap mapaTemp = new HashMap();
        				int cont = 0;
        				for(int k=0;k<numGrupoEtareo;k++)
        				{
        					if(forma.getMapaDetImpresion("contrato_cargue_"+j).toString().equals(forma.getMapaCarguesGrupoEtareo("contrato_cargue_"+k).toString()))
        					{
        						mapaTemp.put("edad_inicial_"+cont,forma.getMapaCarguesGrupoEtareo("edad_inicial_"+k));
        						mapaTemp.put("edad_final_"+cont,forma.getMapaCarguesGrupoEtareo("edad_final_"+k));
        						mapaTemp.put("sexo_"+cont,forma.getMapaCarguesGrupoEtareo("sexo_"+k));
        						mapaTemp.put("valor_"+cont,forma.getMapaCarguesGrupoEtareo("valor_"+k));
        						mapaTemp.put("total_usuarios_"+cont,forma.getMapaCarguesGrupoEtareo("total_usuarios_"+k));
        						mapaTemp.put("porcentaje_pyp_"+cont,forma.getMapaCarguesGrupoEtareo("porcentaje_pyp_"+k));
        						mapaTemp.put("valor_total_"+cont,forma.getMapaCarguesGrupoEtareo("valor_total_"+k));
        						mapaTemp.put("contrato_cargue_"+cont,forma.getMapaCarguesGrupoEtareo("contrato_cargue_"+k));
        						mapaTemp.put("contrato_"+cont,forma.getMapaCarguesGrupoEtareo("contrato_"+k));
        						mapaTemp.put("cuenta_cobro_"+cont,forma.getMapaCarguesGrupoEtareo("cuenta_cobro_"+k));
        						mapaTemp.put("convenio_"+cont,forma.getMapaCarguesGrupoEtareo("convenio_"+k));
        						mapaTemp.put("valor_upc_"+cont,forma.getMapaCarguesGrupoEtareo("valor_upc_"+k));
        						cont++;
        					}
        				}
        				mapaTemp.put("numRegistros",cont+"");
        				//*******************************************************************
        				mapaGrupoEtareo.put("mapaDetalle_"+contGrupoEtareo, mapaTemp);
        				contGrupoEtareo++;
        				//***************************************************************************
        			}
        			
        		}
        		
        	}
        	

    		mapaUPC.put("numRegistros", contUPC+"");
    		mapaGrupoEtareo.put("numRegistros",contGrupoEtareo+"");
    		//Se almacenan los cargues de cada tipo en el mapa de las cuentas de cobro
    		mapa.put("mapaUPC_"+i,mapaUPC);
    		mapa.put("mapaGrupoEtareo_"+i,mapaGrupoEtareo);
        }
       
        
		
		
		return mapa;
	}

	
	
	

}
