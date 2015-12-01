/*
 * Creado el Aug 1, 2006
 * por Julian Montoya
 */
package com.princetonsa.pdf;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadCadena;
import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;

import com.lowagie.text.BadElementException;
import com.princetonsa.actionform.capitacion.AprobacionAjustesForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;

public class AprobacionAjustesPdf {

	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private static Logger logger = Logger.getLogger(AprobacionAjustesPdf.class);
	

	/**
	 * Metodo para generar el PDF con la Información de Un Ajuste Especifico.
	 * @param filename
	 * @param forma
	 * @param usuario
	 * @param cone
	 */
	public static void pdfDetalleAjusteConsulta(String filename, AprobacionAjustesForm forma, UsuarioBasico usuario, Connection cone, HttpServletRequest request) 
	{
		//-- Variable para manejar el reporte
        PdfReports report = new PdfReports();

        //-Crear el String para enviar los datos al reporte 	
        int numRows = UtilidadCadena.noEsVacio(forma.getMapaCargue("numRegistros")+"") ? Integer.parseInt(forma.getMapaCargue("numRegistros")+"") : 0;
		
		int filCero = 0, con = 0;
		//-Verificar si algun cargue tiene ajuste de Cero -- Para No Imprimir Este.
		for(int i=0; i<numRows; i++)
		{ 
			String valor =forma.getMapaCargue("valor_"+i)+"";
			if ( Float.parseFloat(valor) == 0 )
			{
				filCero++;	
			}

		}
		
        String[] datos = new String[(numRows-filCero) * 3];
		
		
		for(int i=0; i<numRows; i++)
		{ 
			String valor =forma.getMapaCargue("valor_"+i)+"";
			if ( Float.parseFloat(valor) != 0 )
			{
				datos[con] = forma.getMapaCargue("contrato_"+i)+"";		 	con++;
				datos[con] = valor;											con++;	 
				datos[con] = forma.getMapaCargue("nombre_concepto_"+i)+""; 	con++;
				//datos[con] = forma.getMapaCargue("fecha_cargue_"+i)+"";   con++;
				//datos[con] = forma.getMapaCargue("saldo_"+i)+""; 			con++;	
			}	
		}
       
   	 	//-- Titulos de la tabla  
		String[] titulosCol = {"Contrato","Valor Ajuste","Concepto"};
		
        
	
       //-- Tomando la ruta definida en el web.xml para generar el reporte.
       String filePath=ValoresPorDefecto.getFilePath();
       
       
       //-- consultar la informacion de la Empresa.
       InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
       
       //-- Titulo del reporte
       String tituloReporte="  RELACION DE AJUSTES CARTERA CAPITACION ";
       String encabezado =  " " + institucionBasica.getRazonSocial() + "  \n " +institucionBasica.getTipoIdentificacion()+" "+institucionBasica.getNit() + 
       						"\n Dirección " + institucionBasica.getDireccion() +  "  \n" +" Telefono " + institucionBasica.getTelefono() +  "  ";
       
       //generacion de la cabecera, validando el tipo de separador e utilizado en el path
       
       report.setReportBaseHeader1(institucionBasica,"align-left" , tituloReporte);
       
       //-----------------------------------------Abrir Reporte------------------------------------------------------------
       report.openReport(filename);
       report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
       
       //-------------------------------- Información de detalle del ajustes ----------------------------------------------
       report.font.setFontSizeAndAttributes(10,true,false,false);
       
       String cad = " Ajuste " + forma.getMapaAjuste("d_nom_tipo_ajuste_0") + " No. " + forma.getMapaAjuste("d_consecutivo_0");
       		  cad += "          Estado:  " + forma.getMapaAjuste("d_estado_ajuste_0");  
       report.document.addParagraph(cad,report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);
       
       cad = " Cuenta Cobro:  " + forma.getMapaAjuste("d_cuenta_cobro_0");
		report.document.addParagraph(cad,report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);

       
       cad =  " ELABORADO Fecha: " + forma.getMapaAjuste("d_fecha_elaboracion_0") + " Usuario: " + forma.getMapaAjuste("d_usuario_elaboracion_0");
       report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);

       cad = " APROBADO Fecha: "  + forma.getMapaAjuste("d_fecha_aprobacion_0") + " Usuario: " + forma.getMapaAjuste("d_usuario_aprobacion_0");
       report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);
       
       
       cad =  " Convenio: " + forma.getMapaAjuste("d_convenio_0");
       report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);

       cad =  " Observaciones: " + forma.getMapaAjuste("d_observaciones_0");
       report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);
       

       report.setReportTitleSectionAtributes(0x000000, 0xEBEBEB,0x000000, 12);
	   report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 11);

       //---Colocar los datos al reporte (El detalle la inforamcion del cargue).
	   report.createColSection("cargue", " Información Cargues", titulosCol, datos, 5);
	   
	   //-- Establecer el tamaño de cada columna.
	  	float widths[]={(float) 3,(float) 3, (float) 4};
	    try {
			report.getSectionReference("cargue").getTableReference("parentTable").setWidths(widths);
		} catch (BadElementException e) {  e.printStackTrace();	}

	   report.addSectionToDocument("cargue");

       cad =  " Valor Total 	$  " + forma.getMapaAjuste("d_valor_total_0");
       report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_CENTER,25);

       cad =  "";
       report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_CENTER,25);
       report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_CENTER,25);
       report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_CENTER,25);

       cad =  "\t\t                     _____________________                                    _____________________";
       report.document.addPhrase(cad);		
       
       cad =  "";
       report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_CENTER,25);
       
       cad =  "\t\t                          Elaborado                                                            Revisado";
       report.document.addPhrase(cad);		
	      
        //-- Cerrar el reporte 
        report.closeReport();		
		
	}


	/**
	 * Metodo para generar el PDF con el listado de Ajustes segun los parametros de la Busqueda Avanzada. 
	 * @param string
	 * @param forma
	 * @param usuario
	 * @param con
	 */
	public static void pdfListadoAjusteConsulta(String filename, AprobacionAjustesForm forma, UsuarioBasico usuario, Connection cone, HttpServletRequest request)
	{
		//-- Variable para manejar el reporte
        PdfReports report = new PdfReports();

        //-- Crear el String para enviar los datos al reporte 	
        int numRows = UtilidadCadena.noEsVacio(forma.getMapaAjuste("numRegistros")+"") ? Integer.parseInt(forma.getMapaAjuste("numRegistros")+"") : 0;
		
		int con = 0;
		
		//-- Para almacenar Los datos de la consulta.
        String[] datos = new String[numRows * 10];
		

		String ajuste = "";
		
		for(int i=0; i<numRows; i++)
		{ 
			ajuste  = forma.getMapaAjuste("cod_tipo_ajuste_"+i)+"";
			
			if ( Integer.parseInt(ajuste) ==  ConstantesBD.codigoConceptosCarteraDebito  ) { datos[con] = "D"; }
			else  { datos[con] = "C"; }
			con++;
			
			datos[con] = forma.getMapaAjuste("estado_ajuste_"+i)+"";		con++;
			datos[con] = forma.getMapaAjuste("fecha_elaboracion_"+i)+"";	con++;
			datos[con] = forma.getMapaAjuste("usuario_elaboracion_"+i)+"";	con++;
			datos[con] = forma.getMapaAjuste("fecha_aprobacion_"+i)+"";		con++;
			datos[con] = forma.getMapaAjuste("usuario_aprobacion_"+i)+"";	con++;
			datos[con] = forma.getMapaAjuste("valor_total_"+i)+"";		 	con++;
			datos[con] = forma.getMapaAjuste("codigo_contrato_"+i)+"";		con++;
			datos[con] = forma.getMapaAjuste("cuenta_cobro_"+i)+"";		 	con++;
			datos[con] = forma.getMapaAjuste("convenio_"+i)+"";		 		con++;
		}
       
   	   //-- Titulos de la tabla  
	   String[] titulosCol = {"Ajuste", "Estado", "Fecha Elaboración", "Usuario Elaboración", "Fecha Aprobación", 
						      "Usuario Aprobación", "Valor", "Contrato", "CxC", "Convenio"};
	
       //-- Tomando la ruta definida en el web.xml para generar el reporte.
       String filePath=ValoresPorDefecto.getFilePath();
       
       //-- Consultar la informacion de la Empresa.
       InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");

       //-- Titulo del reporte
       String tituloReporte="  RELACION DE AJUSTES CARTERA CAPITACION ";
       String encabezado =  " " + institucionBasica.getRazonSocial() + "  \n " +institucionBasica.getTipoIdentificacion()+" "+institucionBasica.getNit() + 
       						"\n Dirección " + institucionBasica.getDireccion() +  "  \n" +" Telefono " + institucionBasica.getTelefono() +  "  ";
       
       report.setReportBaseHeader1(institucionBasica,"align-left" , tituloReporte);
       
       //-----------------------------------------Abrir Reporte------------------------------------------------------------
       report.openReport(filename);
       report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
       
       //-------------------------------- Información de detalle del ajustes ----------------------------------------------
       report.font.setFontSizeAndAttributes(8,true,false,false);
       
       String cad = " Parametros de Busqueda :";
       report.document.addParagraph(cad,report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);
       
       //-- Adicionar Todos Los Parametros de Busqueda que se utilizaron 
       
       if ( forma.getTipoAjuste() != 0 )    //-- Verificar el Tipo de Ajuste.
       {
    	   //-- Encontrar el nombre del Tipo del Ajuste.
    	   int nr = UtilidadCadena.noEsVacio( forma.getMapa("numRegistros") +"" ) ? Integer.parseInt( forma.getMapa("numRegistros")+"") : 0;
    	   
    	   for (int i = 0; i < nr; i++) 
    	   {
			if ( Integer.parseInt(forma.getMapa("cod_ajuste_"+i)+"")  == forma.getTipoAjuste() )
			{
				cad = " Tipo Ajuste: " + forma.getMapa("nom_ajuste_" + i);
				break;
			}
    	   }
       }
       
       if ( forma.getEstadoAjuste() != 0 )   //-- Verificar el estado del Ajuste.
       {
    	   //-- Encontrar el nombre del Tipo del Ajuste.
    	   int nr = UtilidadCadena.noEsVacio( forma.getMapa("numRegistrosTa") +"" ) ? Integer.parseInt( forma.getMapa("numRegistrosTa")+"") : 0;
    	   for (int i = 0; i < nr; i++) 
    	   {
			if ( Integer.parseInt(forma.getMapa("codigo_ta_"+i)+"")  == forma.getEstadoAjuste() )
			{
				cad += "\n Estado Ajuste: " + forma.getMapa("nombre_ta_" + i);
				break;
			}
    	   }
    	   
       }
       
       if ( forma.getNroAjuste() != 0 )  //-- Verificar los numeros de Ajuste inicial y Final.
       {
			cad += "\n Numero de Ajuste Inicial: " + forma.getNroAjuste() + "  Numero Ajuste Final: " + forma.getNroAjusteFinal();
       }

       if ( !forma.getFechaAjuste().equals("") )  //-- Verificar La Fecha Inicial Elaboracion y la Fecha Final.
       {
			cad += "\n Fecha Ajuste Inicial: " + forma.getFechaAjuste() + "  Fecha Ajuste Final: " + forma.getFechaAprobacionAjuste();
       }
       
       
       if ( forma.getCodigoConcepto() != 0 ) //-- Verificar si buscaron por "Concepto"
       {
    	   int nr = UtilidadCadena.noEsVacio( forma.getMapa("numRegConceptos") +"" ) ? Integer.parseInt( forma.getMapa("numRegConceptos")+"") : 0;
    	   for (int i = 0; i < nr; i++) 
    	   {
			if ( Integer.parseInt(forma.getMapa("codigo_concepto_"+i)+"")  == forma.getCodigoConcepto() )
			{
				cad += "\n Concepto: [" + forma.getMapa("codigo_concepto_" + i) +  "] "  + forma.getMapa("descripcion_concepto_" + i)+ "  [" + forma.getMapa("tipo_concepto_"+ i) + "] " ;
				break;
			}
    	   }
        }

       if ( forma.getNroCuentaCobro() != 0 )  //-- Verificar si se Busco Por La Cuenta de Cobro.
       {
			cad += "\n Cuenta de Cobro: " + forma.getNroCuentaCobro();
       }

       if ( forma.getNroConvenio() != 0 )    //-- Verificar si se Busco Por El Convenio.
       {
      	   int nr = UtilidadCadena.noEsVacio( forma.getMapa("numRegConvenios") +"" ) ? Integer.parseInt( forma.getMapa("numRegConvenios")+"") : 0;
      	   
    	   for (int i = 0; i < nr; i++) 
    	   {
			if ( Integer.parseInt(forma.getMapa("cod_convenio_"+i)+"")  == forma.getCodigoConcepto() )
			{
				cad += "\n Convenio: " + forma.getMapa("nom_convenio_"+i);
				break;
			}
    	   }
       }

       
       //-- Adicion de Parametros de Busqueda. 
       report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);

       report.setReportTitleSectionAtributes(0x000000, 0xEBEBEB,0x000000, 12);
	   report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 11);

       //---Colocar los datos al reporte (El detalle la inforamcion del cargue).
	   report.createColSection("cargue", " Información Ajustes", titulosCol, datos, 5);
	   
	   //-- Establecer el tamaño de cada columna.
	   float widths[]={(float) 0.5,(float) 1, (float) 1.5, (float) 1,(float) 1, (float) 1, (float) 1,(float) 1, (float) 0.5, (float) 1.5};
	  	
	    try {
			report.getSectionReference("cargue").getTableReference("parentTable").setWidths(widths);
		} catch (BadElementException e) {  e.printStackTrace();	}

	   report.addSectionToDocument("cargue");


	   /*String he[] = {"Elabora",""};
	   String da[] = {"",""};
       //---Colocar los datos al reporte (El detalle la inforamcion del cargue).
	   report.createColSectionSinTitulo("firma", " Información Ajustes", titulosCol, datos, 5);
	   report.addSectionToDocument("firma");
	   */
	   
       cad =  "";
       report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_CENTER,25);
       report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_CENTER,25);
       report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_CENTER,25);

       cad =  "\t\t                     _____________________                                    _____________________";
       report.document.addPhrase(cad);		
       
       cad =  "";
       report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_CENTER,25);
       
       cad =  "\t\t                          Elaborado                                                            Revisado";
       report.document.addPhrase(cad);		

        //-- Cerrar el reporte 
        report.closeReport();		
	}
}
