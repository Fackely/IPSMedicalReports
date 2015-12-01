/*
 * TarifasInventarioPdf.java 
 * Autor			:  mdiaz
 * Creado el	:  07-sep-2004
 * 
 * Lenguaje		: Java
 * Compilador	: J2SDK 1.4.1_01
 * 
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 * */
package com.princetonsa.pdf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import org.apache.log4j.Logger;

import util.Listado;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.Administracion.UtilConversionMonedas;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;
import util.pdf.iTextBaseTable;

import com.lowagie.text.BadElementException;
import com.princetonsa.actionform.cargos.TarifasInventarioForm;
import com.princetonsa.dto.administracion.DtoTiposMoneda;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;


/**
 * Clase para manejar la generación de PDF's para
 * las tarifas de inventario
 *
 * @version 1.0, 07-sep-2004
 * @author <a href="mailto:miguel@PrincetonSA.com">Miguel Arturo Diaz</a>
 */
public class TarifasInventarioPdf {

	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private static Logger logger = Logger.getLogger(TarifasInventarioPdf.class);
	
	public static void pdfTarifasInventario(String nombreArchivo, String nomEsquemaTarifario, Collection listado, UsuarioBasico usuario,TarifasInventarioForm form, HttpServletRequest request)
	{
		PdfReports report = new PdfReports("true",false,usuario.getLoginUsuario());
		HashMap<String, Object> mapa = new HashMap<String, Object>();
		String columnas []={"codigo",
							"esquematarifario",
							"articulo",
							"descripcionarticulo",
							"naturalezaarticulo",
							"formafarmaceutica",
							"concentracionarticulo",
							"unidadmedida",
							"tarifa",
							"iva",
							"porcentaje",							
							"actualizautomatic",
							"tipotarifa",
							"nombretipotarifa",
							"fechavigencia"							
							};
		
		mapa=(HashMap)Listado.convertirCollection(columnas, listado, 1);
		mapa.put("numRegistros", listado.size());
		Utilidades.imprimirMapa(mapa);
		
		int numRegistros = Integer.parseInt(mapa.get("numRegistros").toString());
		
		String filePath=ValoresPorDefecto.getFilePath();
		
		//*******SE CARGAN LOS DATOS DE LA INSTITUCION*******************************+
		InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
        //*********************************************************************************
		
		String tituloDocumentos="";
		tituloDocumentos="RELACION DE TARIFAS DE INVENTARIOS - Esquema Tarifario: " + nomEsquemaTarifario;
		
		report.setReportBaseHeaderTarifasInventario(institucionBasica, tituloDocumentos);
		
		//abrir reporte
        report.openReport(nombreArchivo);
        
        iTextBaseTable section;
		
        String articulo="";
      //estos son los atributos de la seccion
    	section = report.createSection("descripcion", "descripcionTable",  Utilidades.convertirAEntero(mapa.get("numRegistros")+""),5 ,10);
        section.setTableBorder("descripcionTable", 0xFFFFFF, 0.0f);
	    section.setTableCellBorderWidth("descripcionTable", 0.1f);
	    section.setTableCellPadding("descripcionTable", 0.3f);
	    section.setTableSpaceBetweenCells("descripcionTable", 0.0f);
	    section.setTableCellsDefaultColors("descripcionTable", 0xFFFFFF, 0xFFFFFF);
	    report.font.setFontAttributes(0x000000, 12, true, false, false);
	    section.setTableCellsColSpan(5);
        section.addTableTextCellAligned("descripcionTable", "Tarifas de Inventarios", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		
        for(int i=0;i<Utilidades.convertirAEntero(mapa.get("numRegistros")+"");i++)
		{
        	float widths[] = { 2.0f, 2.0f, 2.0f, 2.0f, 2.0f};
		    try 
		    {
		    	section.getTableReference("descripcionTable").setWidths(widths);
			}
		    catch (BadElementException e) {
				logger.info("\n Se presentó error generando el pdf del Tarifas Inventario "+e);
			}
		    
		    if(!articulo.equals(mapa.get("articulo_"+i)+""))
		    {
		    	report.font.setFontAttributes(0x000000, 10, false, false, false);
		    	articulo=mapa.get("articulo_"+i)+"";
		    	section.setTableCellsColSpan(5);
		    	section.addTableTextCellAligned("descripcionTable", "", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    	section.addTableTextCellAligned("descripcionTable", "", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    	section.addTableTextCellAligned("descripcionTable", "COD: "+mapa.get("articulo_"+i)+"            DESCRIPCION: "+mapa.get("descripcionarticulo_"+i)+"            FORMA FARMACEUTICA: "+mapa.get("formafarmaceutica_"+i)+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    	section.addTableTextCellAligned("descripcionTable", "CONCENTRACION: "+mapa.get("concentracionarticulo_"+i)+"            NATURALEZA: "+mapa.get("naturalezaarticulo_"+i)+"            UNIDAD MEDIDA: "+mapa.get("unidadmedida_"+i)+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    	section.addTableTextCellAligned("descripcionTable", "", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    	report.font.setFontAttributes(0x000000, 9, true, false, false);
		    	section.setTableCellsColSpan(1);
		    	section.addTableTextCellAligned("descripcionTable", "Fecha Vigencia", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    	section.addTableTextCellAligned("descripcionTable", "Tipo Tarifa", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    	section.addTableTextCellAligned("descripcionTable", "Porcentaje", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    	section.addTableTextCellAligned("descripcionTable", "Tarifa", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    	section.addTableTextCellAligned("descripcionTable", "Indicativo Actualizable", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    }
		    report.font.setFontAttributes(0x000000, 8, false, false, false);
		    section.addTableTextCellAligned("descripcionTable", UtilidadFecha.conversionFormatoFechaAAp(mapa.get("fechavigencia_"+i)+""), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("descripcionTable", ValoresPorDefecto.getIntegridadDominio(mapa.get("tipotarifa_"+i)+"")+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("descripcionTable", mapa.get("porcentaje_"+i)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("descripcionTable", mapa.get("tarifa_"+i)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("descripcionTable", ValoresPorDefecto.getIntegridadDominio(mapa.get("actualizautomatic_"+i)+"")+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		}
		//se añade la sección al documento
  	  	report.addSectionToDocument("descripcion");
		report.closeReport();
		
		
		/*PdfReports report = new PdfReports();
		String[] dataReporte;
		String[] headerReporte;
		

		
     	
		DtoTiposMoneda dto=UtilConversionMonedas.obtenerTipoMonedaManejaInstitucion(usuario.getCodigoInstitucionInt());
		
		int tamanio=11;
		boolean manejaConversion=form.getIndex()>0;
		if(manejaConversion)
		{
			tamanio=12;
		}
		
		headerReporte = new String[tamanio];
		headerReporte[0]="Cod";
		headerReporte[1]="Descripción";
		headerReporte[2]="Forma Farmaceutica";
		headerReporte[3]="Concentración";
		headerReporte[4]="Naturaleza";
		headerReporte[5]="Unid.Medida";
		headerReporte[6]="Fecha Vigencia";
		headerReporte[7]="Tipo Tarifa";
		headerReporte[8]="Porcentaje";
		headerReporte[9]="Tarifa "+dto.getDescripcion()+" "+dto.getSimbolo();
		if(manejaConversion)
		{
			headerReporte[10]="Conversion Valor Tarifa "+form.getTiposMonedaTagMap().get("descripciontipomoneda_"+form.getIndex())+" "+form.getTiposMonedaTagMap().get("simbolotipomoneda_"+form.getIndex())+" "+UtilidadTexto.formatearValores(form.getTiposMonedaTagMap().get("factorconversion_"+form.getIndex())+"");
			headerReporte[11]="Indicativo Actualizable";
		}
		else
		{
			headerReporte[10]="Indicativo Actualizable";
		}
		

		dataReporte = obtenerDatosCollection(listado,form);
		
		//Pasar la tarifa a separacion de miles y decimales
		int cont = 1;
		for(int i=0;i<dataReporte.length;i++)
		{
			if(cont==4)
			{
				String valor = "";
				try
				{
					valor = UtilidadTexto.formatearValores(Double.parseDouble(dataReporte[i]));
				}
				catch(Exception e)
				{
					valor = dataReporte[i];
				}
				dataReporte[i] = valor;
				cont = 0;
			}
			cont ++;
		}

		
		InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), "RELACION DE TARIFAS DE INVENTARIOS - Esquema Tarifario: " + nomEsquemaTarifario);
		

		report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);

		report.openReport(nombreArchivo);

		report.font.setFontAttributes(0x000000, 14, true, false, false);
		    
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		report.setReportTitleSectionAtributes(0xFFFFFF, 0xFFFFFF,0x000000, 12);
		report.setReportDataSectionAtributes(0xFFFFFF, 0xFFFFFF, 0x000000,  7);
		
	    //Añadimos la sección informacion de la consulta
		report.createColSection("tarifasSection", "Tarifas de Inventarios", headerReporte, dataReporte, 10);		
		float widths[] = { 1.0f, 6.5f, 1.0f, 1.5f};
		report.setColumnsWidths("tarifasSection", PdfReports.REPORT_PARENT_TABLE, widths);
		report.addSectionToDocument("tarifasSection");
		report.closeReport();*/
	}

	private static String[] obtenerDatosCollection(Collection col, TarifasInventarioForm forma) 
	{
		 ArrayList arrayList = new ArrayList();
		 String[] stringArray =  null; 		
		 HashMap dyn;
		
		String[] columns = {
    			 "articulo",
    			 "descripcionarticulo",
    			 "naturalezaarticulo",
    			 "formafarmaceutica",
    			 "concentracionarticulo",
    			 "unidadmedida",
    			 "fechavigencia",
    			 "nombretipotarifa",
    			 "porcentaje",
    			 "tarifa",
    			 "actualizautomatic"
    			};	
		boolean manejaConversion=forma.getIndex()>0;
		if(manejaConversion)
		{
			int i, k;
			  if (col==null)
			  {
			  	return new String[0];
			  }
			  
			  Iterator it=col.iterator();
			  k=0;
			  while (it.hasNext())
			  {
		      	dyn=(HashMap)it.next();
			    for(i=0; i<columns.length; i++)
			    {
			    	Object elemento=dyn.get(columns[i]);
			    	if(columns[i].equals("tarifa"))
			    	{
				    	if(elemento==null)
				      	{
				    		arrayList.add(k, "");
				      	}
				    	else
				    	{
				    		arrayList.add(k, UtilidadTexto.formatearValores(elemento+""));
				    		k++;
 				    		arrayList.add(k, UtilidadTexto.formatearValores(Utilidades.convertirADouble(elemento+"")/Utilidades.convertirADouble(forma.getTiposMonedaTagMap().get("factorconversion_"+forma.getIndex())+"")));
				    	}
				      	k++;
			    	}
			    	else
			    	{
				    	if(elemento==null)
				      	{
				    		arrayList.add(k, "");
				      	}
				    	else
				    	{
				    		arrayList.add(k, elemento);
				    	}
				      	k++;
			    	}
			    }
			  }
			   stringArray = new String[arrayList.size()];
				
			   for(i=0; i<arrayList.size(); i++)
			     stringArray[i] = arrayList.get(i) + "";
		}
		else
		{
		  int i, k;
		  if (col==null)
		  {
		  	return new String[0];
		  }
		  
		  Iterator it=col.iterator();
		  k=0;
		  while (it.hasNext())
		  {
	      	dyn=(HashMap)it.next();
		    for(i=0; i<columns.length; i++)
		    {
		    	Object elemento=dyn.get(columns[i]);
		    	if(elemento==null)
		      	{
		    		arrayList.add(k, "");
		      	}
		    	else
		    	{
		    		arrayList.add(k, elemento);
		    	}
		      	k++;
		    }
		  }
		   stringArray = new String[arrayList.size()];
			
		   for(i=0; i<arrayList.size(); i++)
		     stringArray[i] = arrayList.get(i) + "";
		}
	   return stringArray;
	}
		
}
