package com.princetonsa.pdf;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseFont;

import com.lowagie.text.BadElementException;
import com.princetonsa.actionform.tesoreria.TrasladosCajaForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;

public class TrasladosCajaPdf 
{
	/**
     * Clase para manejar los logs de esta clase
     */
    private static Logger logger=Logger.getLogger(TrasladosCajaPdf.class);
    
    /**
     * Método que imprime 
     * 
	 * @param filename Nombre del archivo con el que
	 * se desea generar el PDF
	 * @param Form del que se sacan
	 * las colecciones
     * @param usuario
     */
	public  static void pdf(String filename, TrasladosCajaForm forma, UsuarioBasico usuario, HttpServletRequest request) 
	{
		PdfReports report = new PdfReports();
		Vector nombresFormaspagoVector=(Vector)forma.getTrasladoCajaMap("NOMBRES_FORMAS_PAGO_VECTOR");
		
		int numeroColumnas=nombresFormaspagoVector.size()+4;
		String[] reportHeader= new String[numeroColumnas] ;
		
		int pos=0;
		reportHeader[pos]="#Cierre";
		reportHeader[++pos]="Caja";
		reportHeader[++pos]="Cajero";
		for(int w=0; w<nombresFormaspagoVector.size(); w++)
		{
			reportHeader[++pos]=nombresFormaspagoVector.get(w).toString();
		}
		reportHeader[++pos]="Total";
		
		
		
		String[] reportData=getReportData(forma, numeroColumnas);
		
		String filePath=ValoresPorDefecto.getFilePath();
		String titulo="TRASLADO CAJA";
		
		InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), titulo);
		
		report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
		report.openReport(filename);
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000, 11);
		report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 9);
		
		report.document.addParagraph("Fecha Hora Generación: "+forma.getFechaGeneracionTraslado()+" "+forma.getHoraGeneracionTraslado()+"      Usuario Traslada: "+forma.getUsuarioTraslada(), report.font, 40);
		
		String cajaPpal="";
		int numCajasPpal= Integer.parseInt(forma.getCajasPpalTagMap("numRegistros").toString());
		for(int w=0; w<numCajasPpal; w++)
		{
			if(forma.getCajasPpalTagMap("consecutivo_"+w).toString().equals(forma.getCajaPpal()))
			{
				cajaPpal=forma.getCajasPpalTagMap("descripcion_"+w).toString()+" - "+forma.getCajasPpalTagMap("nombre_centro_atencion_"+w);			
			}
		}
		
		String cajaMayor="";
		int numCajasMayor= Integer.parseInt(forma.getCajasMayorTagMap("numRegistros").toString());
		for(int w=0; w<numCajasMayor; w++)
		{
			if(forma.getCajasMayorTagMap("consecutivo_"+w).toString().equals(forma.getCajaMayor()))
			{
				cajaMayor=forma.getCajasMayorTagMap("descripcion_"+w).toString()+" - "+forma.getCajasMayorTagMap("nombre_centro_atencion_"+w);			
			}
		}
		
		report.document.addParagraph("Caja Ppal: "+cajaPpal +"      Caja Mayor: "+cajaMayor+"      Fecha Traslado: "+forma.getFechaTraslado(), report.font, 20);
		report.createColSection("seccionDetalle", "Detalle", reportHeader, reportData, 10);
		
		pos=0;
		float widths[]=new float[numeroColumnas];
		widths[pos]=1; //#cierre
		widths[++pos]=1; //caja
		widths[++pos]=1; //caja
		
		//se divide los seis que tenemos entre el numero de las formas de pago
		int width=6/nombresFormaspagoVector.size();
		for(int w=0; w<nombresFormaspagoVector.size(); w++)
		{
			widths[++pos]= width;
		}	
		widths[++pos]=1; //total
		
		try
        {
		    report.getSectionReference("seccionDetalle").getTableReference("parentTable").setWidths(widths);
        }
        catch (BadElementException e)
        {
            logger.error("Se presentó error generando el pdf " + e);
        }
		
        report.addSectionToDocument("seccionDetalle");
        
        report.document.addParagraph("______________________________", report.font, 40);
		report.document.addParagraph("Caja Ppal",report.font, 18);
		
		report.document.addParagraph("______________________________", report.font, 40);
		report.document.addParagraph("Caja Mayor",report.font, 18);
        
		report.closeReport(); 
	}
	
	
	
	/**
	 * 
	 * @param forma
	 * @return
	 */
	private static String[] getReportData(TrasladosCajaForm forma, int numeroColumnas)
	{
		int numeroFilas=Integer.parseInt(forma.getTrasladoCajaMap("numRegistros").toString())+1;
		String[] reportData= new String[numeroColumnas*numeroFilas];
		int pos=0;
		for(int w=0; w<Integer.parseInt(forma.getTrasladoCajaMap("numRegistros").toString()); w++)
		{
			reportData[pos]=UtilidadTexto.formatearValores(forma.getTrasladoCajaMap("consecutivocierre_"+w).toString(), "0");
			pos++;
			reportData[pos]=forma.getTrasladoCajaMap("descripcioncaja_"+w).toString();
			pos++;
			reportData[pos]=forma.getTrasladoCajaMap("nombrescajero_"+w).toString();
			pos++;
			Vector keysFormaspago=(Vector)forma.getTrasladoCajaMap("KEYS_FORMAS_PAGO_VECTOR");
			for(int x=0; x<keysFormaspago.size(); x++)
			{
				reportData[pos]=UtilidadTexto.formatearValores( forma.getTrasladoCajaMap(keysFormaspago.get(x)+"_"+w).toString());
				pos++;
			}
			reportData[pos]=UtilidadTexto.formatearValores(forma.getTrasladoCajaMap("TOTAL_CIERRE_"+w).toString());
			pos++;
		}
		reportData[pos]="Total";
		pos++;
		reportData[pos]="Traslado";
		pos++;
		reportData[pos]="Realizado";
		pos++;
		
		Vector vectorTotalesFormasPago=(Vector)forma.getTrasladoCajaMap("TOTALES_FORMAS_PAGO_VECTOR"); 
		for(int y=0; y<vectorTotalesFormasPago.size(); y++)
		{
			reportData[pos]=UtilidadTexto.formatearValores(vectorTotalesFormasPago.get(y).toString());
			pos++;
		}
		reportData[pos]=UtilidadTexto.formatearValores(forma.getTrasladoCajaMap("TOTAL_TOTALES").toString());
		pos++;
		
		return reportData;
	}

}
