/*
 * @author artotor
 */
package com.princetonsa.pdf;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;

import com.lowagie.text.BadElementException;
import com.princetonsa.actionform.inventarios.KardexForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * 
 * @author artotor
 *
 */
public class KardexPdf 
{

	public static final int tamanioLetra=8;
	
	public static void pdfKardexDetalle(String filename, KardexForm forma, UsuarioBasico usuario, HttpServletRequest request) 
	{
		//PdfReports report = new PdfReports();
		
		PdfReports report = new PdfReports("",true);
		generarEncabezado(report,usuario,forma, request);
		report.openReport(filename);
		report=generarReporteUnArticulo(report,forma,forma.getIndexSeleccionado());
		report.closeReport(); 
	}

	
	/**
	 * 
	 * @param report
	 * @param usuario
	 * @param forma
	 */
	public static void generarEncabezado(PdfReports report, UsuarioBasico usuario, KardexForm forma, HttpServletRequest request) 
	{
		String titulo="KARDEX ARTICULOS - "+forma.getNomAlmacen()+" - "+forma.getNomCentroAtencion()+" Fecha Inicial: "+forma.getFechaInicial()+" Fecha Final: "+forma.getFechaFinal();
		
		InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		report.setReportBaseHeader1(institucionBasica.getLogoReportes(), institucionBasica.getRazonSocial(), institucionBasica.getNit(), titulo,11);
		
		report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
	}


	
	/**
	 * Metodo para generar el reporte de un articulo en especifico.
	 * @param report
	 * @param forma
	 * @param i
	 */
	public  static PdfReports generarReporteUnArticulo(PdfReports report, KardexForm forma, int index) 
	{
		HashMap mapaArticulos=forma.getMapaArticulos();
		HashMap mapa=new HashMap();
		mapa=mapaArticulos.get("detalle_"+index)==null?new HashMap():(HashMap)mapaArticulos.get("detalle_"+index);
		
		String aliasSeccion="detalleKardex_"+index;
		String aliasTabla="tablaKardex_"+index;

		report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 12);

		report.createSection(aliasSeccion,aliasTabla,1,12,10);
		 
		report.getSectionReference(aliasSeccion).getTableReference(aliasTabla).setPadding(0.2f);
		report.getSectionReference(aliasSeccion).getTableReference(aliasTabla).setSpacing(0.2f);
        report.getSectionReference(aliasSeccion).setTableBorder(aliasTabla, 0x000000, 0.1f);
        report.getSectionReference(aliasSeccion).setTableCellBorderWidth(aliasTabla, 0.1f);
        report.getSectionReference(aliasSeccion).setTableCellsDefaultColors(aliasTabla, 0xFFFFFF, 0x000000);
         
        report.font.setFontSizeAndAttributes(iTextBaseFont.FONT_TIMES,tamanioLetra,true,false,false);
        
        //primera fila
        report.getSectionReference(aliasSeccion).setTableCellsColSpan(12);
        report.getSectionReference(aliasSeccion).setTableCellsBackgroundColor(0xDDDDDD);
        String articuloStr="Artículo: "+mapaArticulos.get("cod_articulo_"+index)+" - "+mapaArticulos.get("desc_articulo_"+index)+" Unidad Medida: "+mapaArticulos.get("unidad_medida_"+index);
        if(forma.isPorLote())
        	articuloStr+=" Lote/Fecha Vencimiento: "+forma.getLote()+" - "+forma.getFechaVencimiento();
        report.getSectionReference(aliasSeccion).addTableTextCellAligned(aliasTabla,articuloStr, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        
        report.getSectionReference(aliasSeccion).setTableCellsBackgroundColor(0xFFFFFF);
        
        //segunda fila
        report.getSectionReference(aliasSeccion).setTableCellsColSpan(9);
        report.getSectionReference(aliasSeccion).addTableTextCellAligned(aliasTabla, "Saldo Inicial a "+forma.getFechaInicial(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        report.getSectionReference(aliasSeccion).setTableCellsColSpan(1);
        report.getSectionReference(aliasSeccion).addTableTextCellAligned(aliasTabla, mapaArticulos.get("calculoCantidad")==null?"0":mapaArticulos.get("calculoCantidad")+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        report.getSectionReference(aliasSeccion).addTableTextCellAligned(aliasTabla, UtilidadTexto.formatearValores(mapaArticulos.get("calculoCosto")==null?"0":mapaArticulos.get("calculoCosto")+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        report.getSectionReference(aliasSeccion).addTableTextCellAligned(aliasTabla, UtilidadTexto.formatearValores(mapaArticulos.get("calculoValorTotalInicial")==null?"0":mapaArticulos.get("calculoValorTotalInicial")+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        
        //tercera fila -- fila titulos.
        report.getSectionReference(aliasSeccion).addTableTextCellAligned(aliasTabla, "Transacción.", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
        report.getSectionReference(aliasSeccion).addTableTextCellAligned(aliasTabla, "Doc.", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
        report.getSectionReference(aliasSeccion).addTableTextCellAligned(aliasTabla, "F. Elabora.", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
        report.getSectionReference(aliasSeccion).addTableTextCellAligned(aliasTabla, "F. Atenc.", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
        report.getSectionReference(aliasSeccion).addTableTextCellAligned(aliasTabla, "Costo Unit.", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
        report.getSectionReference(aliasSeccion).addTableTextCellAligned(aliasTabla, "Cant. Entr.", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
        report.getSectionReference(aliasSeccion).addTableTextCellAligned(aliasTabla, "Vr. Entr.", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
        report.getSectionReference(aliasSeccion).addTableTextCellAligned(aliasTabla, "Cant. Sal.", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
        report.getSectionReference(aliasSeccion).addTableTextCellAligned(aliasTabla, "Vr. Sal.", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
        report.getSectionReference(aliasSeccion).addTableTextCellAligned(aliasTabla, "Cant. Saldo.", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
        report.getSectionReference(aliasSeccion).addTableTextCellAligned(aliasTabla, "Costo. Unit.", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
        report.getSectionReference(aliasSeccion).addTableTextCellAligned(aliasTabla, "Vr. Saldo", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);

        report.font.setFontSizeAndAttributes(iTextBaseFont.FONT_TIMES,tamanioLetra,false,false,false);

        for(int k=0;k<Integer.parseInt(mapa.get("numRegistros")==null?"0":mapa.get("numRegistros")+"");k++)
		  {
        	report.getSectionReference(aliasSeccion).addTableTextCellAligned(aliasTabla, mapa.get("cod_transaccion_"+k)+"-"+mapa.get("desc_transaccion_"+k), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        	report.getSectionReference(aliasSeccion).addTableTextCellAligned(aliasTabla, mapa.get("documento_"+k)+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        	report.getSectionReference(aliasSeccion).addTableTextCellAligned(aliasTabla, mapa.get("fecha_elaboracion_"+k)+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        	report.getSectionReference(aliasSeccion).addTableTextCellAligned(aliasTabla, mapa.get("fecha_atencion_"+k)+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        	if(!mapa.get("costo_unitario_"+k).equals("0"))
			{
        		report.getSectionReference(aliasSeccion).addTableTextCellAligned(aliasTabla, UtilidadTexto.formatearValores(mapa.get("costo_unitario_"+k)==null?"0":mapa.get("costo_unitario_"+k)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
			}
        	else
        	{
        		report.getSectionReference(aliasSeccion).addTableTextCellAligned(aliasTabla, "", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        	}
        	
        	if(!mapa.get("cantidad_entrada_"+k).equals("0"))
			{
        		report.getSectionReference(aliasSeccion).addTableTextCellAligned(aliasTabla, UtilidadTexto.formatearValores(mapa.get("cantidad_entrada_"+k)==null?"0":mapa.get("cantidad_entrada_"+k)+"","####0"), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
			}
        	else
        	{
        		report.getSectionReference(aliasSeccion).addTableTextCellAligned(aliasTabla, "", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        	}

        	if(!mapa.get("valor_entrada_"+k).equals("0"))
			{
        		report.getSectionReference(aliasSeccion).addTableTextCellAligned(aliasTabla, UtilidadTexto.formatearValores(mapa.get("valor_entrada_"+k)==null?"0":mapa.get("valor_entrada_"+k)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
			}
        	else
        	{
        		report.getSectionReference(aliasSeccion).addTableTextCellAligned(aliasTabla, "", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        	}

        	if(!mapa.get("cantidad_salida_"+k).equals("0"))
			{
        		report.getSectionReference(aliasSeccion).addTableTextCellAligned(aliasTabla, UtilidadTexto.formatearValores(mapa.get("cantidad_salida_"+k)==null?"0":mapa.get("cantidad_salida_"+k)+"","####0"), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
			}
        	else
        	{
        		report.getSectionReference(aliasSeccion).addTableTextCellAligned(aliasTabla, "", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        	}

        	if(!mapa.get("valor_salida_"+k).equals("0"))
			{
        		report.getSectionReference(aliasSeccion).addTableTextCellAligned(aliasTabla, UtilidadTexto.formatearValores(mapa.get("valor_salida_"+k)==null?"0":mapa.get("valor_salida_"+k)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
			}
        	else
        	{
        		report.getSectionReference(aliasSeccion).addTableTextCellAligned(aliasTabla, "", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        	}

        	if(!mapa.get("cantidad_saldo_"+k).equals("0"))
			{
        		report.getSectionReference(aliasSeccion).addTableTextCellAligned(aliasTabla, UtilidadTexto.formatearValores(mapa.get("cantidad_saldo_"+k)==null?"0":mapa.get("cantidad_saldo_"+k)+"","####0"), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
			}
        	else
        	{
        		report.getSectionReference(aliasSeccion).addTableTextCellAligned(aliasTabla, "", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        	}

        	if(!mapa.get("costo_unitario_dos_"+k).equals("0"))
			{
        		report.getSectionReference(aliasSeccion).addTableTextCellAligned(aliasTabla, UtilidadTexto.formatearValores(mapa.get("costo_unitario_dos_"+k)==null?"0":mapa.get("costo_unitario_dos_"+k)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
			}
        	else
        	{
        		report.getSectionReference(aliasSeccion).addTableTextCellAligned(aliasTabla, "", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        	}

        	if(!mapa.get("valor_saldo_"+k).equals("0"))
			{
        		report.getSectionReference(aliasSeccion).addTableTextCellAligned(aliasTabla, UtilidadTexto.formatearValores(mapa.get("valor_saldo_"+k)==null?"0":mapa.get("valor_saldo_"+k)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
			}
        	else
        	{
        		report.getSectionReference(aliasSeccion).addTableTextCellAligned(aliasTabla, "", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        	}
		  }

        report.font.setFontSizeAndAttributes(iTextBaseFont.FONT_TIMES,tamanioLetra,true,false,false);

        //ultima fila
        report.getSectionReference(aliasSeccion).setTableCellsColSpan(9);
        report.getSectionReference(aliasSeccion).addTableTextCellAligned(aliasTabla, "Saldo Final a "+forma.getFechaFinal(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        report.getSectionReference(aliasSeccion).setTableCellsColSpan(1);
        report.getSectionReference(aliasSeccion).addTableTextCellAligned(aliasTabla, mapaArticulos.get("calculoCantidadFinal")==null?"0":mapaArticulos.get("calculoCantidadFinal")+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        report.getSectionReference(aliasSeccion).addTableTextCellAligned(aliasTabla, UtilidadTexto.formatearValores(mapaArticulos.get("calculoCostoFinal")==null?"0":mapaArticulos.get("calculoCostoFinal")+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        report.getSectionReference(aliasSeccion).addTableTextCellAligned(aliasTabla, UtilidadTexto.formatearValores(mapaArticulos.get("calculoValorTotalFinal")==null?"0":mapaArticulos.get("calculoValorTotalFinal")+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);

        
        float widths[]={(float) 2.2,(float) 0.9,(float) 0.6,(float) 0.6,(float) 0.8,(float) 0.8,(float) 0.7,(float) 0.5,(float) 0.7,(float) 0.8,(float) 0.8,(float) 0.7};
		try
        {
		    report.getSectionReference(aliasSeccion).getTableReference(aliasTabla).setWidths(widths);
        }
        catch (BadElementException e)
        {
            e.printStackTrace();
        }
		
        
		report.addSectionToDocument(aliasSeccion);

		return report;
	}

}
