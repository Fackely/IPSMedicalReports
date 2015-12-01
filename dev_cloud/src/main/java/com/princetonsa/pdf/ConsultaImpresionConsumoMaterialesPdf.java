package com.princetonsa.pdf;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PdfDocument.Indentation;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * 
 * @author jduque@princetonsa.com
 *
 */
public class ConsultaImpresionConsumoMaterialesPdf
{

	/**
	 * 
	 * @param con
	 * @param fileName
	 * @param servicios
	 * @param paciente
	 * @param usuario
	 * @param datos
	 * @param op
	 * @param request
	 */
	public static void imprimirListadoActo(Connection con, String fileName, HashMap servicios,PersonaBasica paciente, UsuarioBasico usuario, HashMap datos, HashMap op, HttpServletRequest request)
	{
		int identacionDerecha=3;
		InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
	    Document document = new Document();
	    try {
			PdfWriter.getInstance(document, 
			   new FileOutputStream(fileName));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}
			//document.open();

			
			PdfReports report = new PdfReports("",true);
			
			report.setReportBaseHeader1( institucionBasica.getLogoReportes(), institucionBasica.getRazonSocial(), institucionBasica.getNit(), "ESTADO CONSUMO DE MATERIALES",10);
			report.openReport(fileName);
		    report.setReportMargins((float)0.05,(float)0.05, (float)1, (float)1);
		    			
		    report.document.addParagraph("Fecha de Proceso: "+UtilidadFecha.conversionFormatoFechaAAp(UtilidadFecha.getFechaActual()),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,16);
		    report.document.addParagraph("Usuario Proceso: "+usuario.getLoginUsuario(),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,10);
		    
		    report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
		    report.font.useFont(iTextBaseFont.FONT_COURIER, false, false, false);
		    report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000, 9);
		    report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 5);
		    
	   
		Paragraph p;
		PdfPTable table = new PdfPTable(11);
		
		table.setWidthPercentage(100);
		
		float widths[]={(float) 0.3,
						(float) 1.8, 
						(float) 0.7,
						(float) 0.9,
						(float) 1.4,
						(float) 0.9,
						(float) 0.9,
						(float) 0.8,
						(float) 0.8, 
						(float) 0.5, 
						(float) 1};
		try {
			table.setWidths(widths);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		p= new Paragraph(("Paciente :"+paciente.getNombrePersona()));
		p.setIndentationRight(identacionDerecha);
		p.getFont().setSize(8);
		p.setAlignment(iTextBaseDocument.ALIGNMENT_JUSTIFIED_ALL);
		PdfPCell cell3 = 
			new PdfPCell(p);
		cell3.setColspan(2);
		table.addCell(cell3);
		
		p= new Paragraph((paciente.getCodigoTipoIdentificacionPersona()+": "+paciente.getNumeroIdentificacionPersona()));
		p.setIndentationRight(identacionDerecha);
		p.getFont().setSize(8);
		p.setAlignment(iTextBaseDocument.ALIGNMENT_JUSTIFIED_ALL);
		cell3 = 
			new PdfPCell(p);
		cell3.setColspan(2);
		table.addCell(cell3);
		
		
		p= new Paragraph("Cuenta Numero: "+paciente.getCodigoCuenta());
		p.setIndentationRight(identacionDerecha);
		p.getFont().setSize(8);
		p.setAlignment(iTextBaseDocument.ALIGNMENT_JUSTIFIED_ALL);
		cell3 = 
			new PdfPCell(p);
		cell3.setColspan(2);
		table.addCell(cell3);
		
		p= new Paragraph("Solicitud  Nro. "+datos.get("noSolicitud")+" ");
		p.setIndentationRight(identacionDerecha);
		p.getFont().setSize(8);
		p.setAlignment(iTextBaseDocument.ALIGNMENT_JUSTIFIED_ALL);
		cell3 = 
			new PdfPCell(p);
		cell3.setColspan(2);
		table.addCell(cell3);
		
		p= new Paragraph("CC. solicitado. "+datos.get("farmacia")+" ");
		p.setIndentationRight(identacionDerecha);
		p.getFont().setSize(8);
		p.setAlignment(iTextBaseDocument.ALIGNMENT_JUSTIFIED_ALL);
		cell3 = 
			new PdfPCell(p);
		cell3.setColspan(3);
		table.addCell(cell3);
		
		PdfPCell cell ;
		PdfPCell cell1 ; 
		
		for(int i=0;i<Integer.parseInt(servicios.get("numRegistros").toString());i++)
		{
			p= new Paragraph("Cx"+(i+1));
			p.setIndentationRight(identacionDerecha);
			p.getFont().setSize(8);
			p.setAlignment(iTextBaseDocument.ALIGNMENT_JUSTIFIED_ALL);
			cell = 
				new PdfPCell(p);
			cell.setColspan(1);
			
			p= new Paragraph(servicios.get("servicio_"+i)+"");
			p.setIndentationRight(identacionDerecha);
			p.getFont().setSize(8);
			p.setAlignment(iTextBaseDocument.ALIGNMENT_JUSTIFIED_ALL);
			cell1 = 
				new PdfPCell(p);
			cell1.setColspan(10);
			table.addCell(cell);
			table.addCell(cell1);
		}
		
		int col=0;
		
		
		if (op.get("costo").toString().equals(ConstantesBD.acronimoSi))
		{
			col++;
			col++;
		}
		if (op.get("tarifa").toString().equals(ConstantesBD.acronimoSi))
		{
			col++;
			col++;
		}
		if (op.get("tarifa").toString().equals(ConstantesBD.acronimoNo) && op.get("costo").toString().equals(ConstantesBD.acronimoNo) )
		{
			col++;
		}
		
		int truncar=33;
		if (col==2)
			truncar=truncar+32;
		else
			if(col==1)
				truncar=truncar+50;
			
			
		
		
		p= new Paragraph("Consumo de Materiales");
		p.setIndentationRight(identacionDerecha);
		p.getFont().setSize(9);
		p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		cell3 = 
			new PdfPCell(p);
		cell3.setColspan(11);
		cell3.setBackgroundColor(Color.GRAY );
		cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		table.addCell(cell3);
		
		
		p= new Paragraph("Cod");
		p.setIndentationRight(identacionDerecha);
		p.getFont().setSize(9);
		p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		cell3 = 
			new PdfPCell(p);
		cell3.setBackgroundColor(new Color(213, 220, 228));
		cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		table.addCell(cell3);
		
		
		p= new Paragraph("Descripción");
		p.setIndentationRight(identacionDerecha);
		p.getFont().setSize(9);
		p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		cell3 = 
			new PdfPCell(p);
		
		cell3.setColspan(6-col);
		cell3.setBackgroundColor(new Color(213, 220, 228));
		cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		table.addCell(cell3);

		
		p= new Paragraph("Unidad de Medida");
		p.setIndentationRight(identacionDerecha);
		p.getFont().setSize(9);
		p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		cell3 = 
			new PdfPCell(p);
		cell3.setBackgroundColor(new Color(213, 220, 228));
		cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		table.addCell(cell3);
		
		
		p= new Paragraph("Cantidad  Total  Despachada");
		p.setIndentationRight(identacionDerecha);
		p.getFont().setSize(9);
		p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		cell3 = 
			new PdfPCell(p);
		cell3.setBackgroundColor(new Color(213, 220, 228));
		cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		table.addCell(cell3);
		
		p= new Paragraph("Cantidad Total Consumo");
		p.setIndentationRight(identacionDerecha);
		p.getFont().setSize(9);
		p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		cell3 = 
			new PdfPCell(p);
		cell3.setBackgroundColor(new Color(213, 220, 228));
		cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		table.addCell(cell3);
		
		//--		
		p= new Paragraph("Cantidad Total Devuelta");
		p.setIndentationRight(identacionDerecha);
		p.getFont().setSize(9);
		p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		cell3 = 
			new PdfPCell(p);
		cell3.setBackgroundColor(new Color(213, 220, 228));
		cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		table.addCell(cell3);
		
		
		if (op.get("costo").toString().equals(ConstantesBD.acronimoSi))
		{
			p= new Paragraph("Costo Unitario");
			p.setIndentationRight(identacionDerecha);
			p.getFont().setSize(9);
			p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			cell3 = 
				new PdfPCell(p);
			cell3.setBackgroundColor(new Color(213, 220, 228));
			cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			table.addCell(cell3);
			
			p= new Paragraph("Total Costo  Consumo");
			p.setIndentationRight(identacionDerecha);
			p.getFont().setSize(9);
			p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			cell3 = 
				new PdfPCell(p);
			cell3.setBackgroundColor(new Color(213, 220, 228));
			cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			table.addCell(cell3);
			
			
		}
		
		if (op.get("tarifa").toString().equals(ConstantesBD.acronimoSi))
		{
			p= new Paragraph("Tarifa Unitaria");
			p.setIndentationRight(identacionDerecha);
			p.getFont().setSize(9);
			p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			cell3 = 
				new PdfPCell(p);
			cell3.setBackgroundColor(new Color(213, 220, 228));
			cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			table.addCell(cell3);
			
			p= new Paragraph("Total Consumo Tarifa");
			p.setIndentationRight(identacionDerecha);
			p.getFont().setSize(9);
			p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			cell3 = 
				new PdfPCell(p);
			cell3.setBackgroundColor(new Color(213, 220, 228));
			cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			table.addCell(cell3);
			
			
		}
		
		if (op.get("costo").toString().equals(ConstantesBD.acronimoNo) && op.get("tarifa").toString().equals(ConstantesBD.acronimoNo))
		{
			p= new Paragraph("Total Costo  Consumo");
			p.setIndentationRight(identacionDerecha);
			p.getFont().setSize(9);
			p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			cell3 = 
				new PdfPCell(p);
			cell3.setBackgroundColor(new Color(213, 220, 228));
			cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			table.addCell(cell3);
		}
		

		
int y=0;
	
		for(int i=0;i<Integer.parseInt(datos.get("numRegistros").toString());i++)
		{
			if(datos.get("incluido_"+i).toString().equals(ConstantesBD.acronimoSi+"") || datos.get("indica_"+i).toString().equals(ConstantesBD.acronimoSi+""))
			{
				y++;
				
				//
				p= new Paragraph(datos.get("cod_"+i)+"");
				p.setIndentationRight(identacionDerecha);
				p.getFont().setSize(7);
				p.setAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
				cell3 = 
					new PdfPCell(p);
				cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
				
				if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
				{
					cell3.setBorder(Rectangle.BOTTOM | Rectangle.LEFT);
				}
				else
					cell3.setBorder(Rectangle.LEFT);
					
				table.addCell(cell3);
				
				//
				String descripcion=(datos.get("descripcion_"+i)+"");
				if (descripcion.length()>truncar)
					descripcion=descripcion.substring(0, truncar);
				p= new Paragraph(descripcion);
				p.setIndentationRight(identacionDerecha);
				p.getFont().setSize(7);
				p.setAlignment(iTextBaseDocument.ALIGNMENT_LEFT);
				cell3 = 
					new PdfPCell(p);
				cell3.setColspan(6-col);
				
				if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
					cell3.setBorder(Rectangle.BOTTOM);
				else
					cell3.setBorderWidth(0);
				table.addCell(cell3);
				
				//
				p= new Paragraph(datos.get("unimedida_"+i)+"");
				p.setIndentationRight(identacionDerecha);
				p.getFont().setSize(7);
				p.setAlignment(iTextBaseDocument.ALIGNMENT_LEFT);
				cell3 = 
					new PdfPCell(p);
				if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
					cell3.setBorder(Rectangle.BOTTOM);
				else
					cell3.setBorderWidth(0);				
				
				table.addCell(cell3);
				
				//
				p= new Paragraph(UtilidadTexto.formatearValores(datos.get("cantdesp_"+i)+"", 2, false, true));
				p.setIndentationRight(identacionDerecha);
				p.getFont().setSize(7);
				p.setAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
				cell3 = 
					new PdfPCell(p);
				
				cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
				if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
					cell3.setBorder(Rectangle.BOTTOM);
				else
					cell3.setBorderWidth(0);
				
				table.addCell(cell3);
				
				//
				p= new Paragraph(UtilidadTexto.formatearValores(datos.get("cantcons_"+i)+"", 2, false, true));
				p.setIndentationRight(identacionDerecha);
				p.getFont().setSize(7);
				p.setAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
				cell3 = 
					new PdfPCell(p);
				
				cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
				if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
					cell3.setBorder(Rectangle.BOTTOM);
				else
					cell3.setBorderWidth(0);
				
				table.addCell(cell3);
				
				//
				p= new Paragraph(UtilidadTexto.formatearValores(datos.get("cantdevu_"+i)+"", 2, false, true));
				p.setIndentationRight(identacionDerecha);
				p.getFont().setSize(7);
				p.setAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
				cell3 = 
					new PdfPCell(p);
				
				cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
				if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
					cell3.setBorder(Rectangle.BOTTOM);
				else
					cell3.setBorderWidth(0);
				
				table.addCell(cell3);
				
				
				//
				if (op.get("costo").toString().equals(ConstantesBD.acronimoSi))
				{
					//
					p= new Paragraph(UtilidadTexto.formatearValores(datos.get("costouni_"+i)+"", 2, false, true));
					p.setIndentationRight(identacionDerecha);
					p.getFont().setSize(7);
					p.setAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
					cell3 = 
						new PdfPCell(p);
					
					cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
					if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
						cell3.setBorder(Rectangle.BOTTOM);
					else
						cell3.setBorderWidth(0);
					
					table.addCell(cell3);
						
					//
					p= new Paragraph(UtilidadTexto.formatearValores(datos.get("valortotal_"+i)+"", 2, false, true));
					p.setIndentationRight(identacionDerecha);
					p.getFont().setSize(7);
					p.setAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
					cell3 = 
						new PdfPCell(p);
					
					cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
				
					if (op.get("tarifa").toString().equals(ConstantesBD.acronimoSi) & i<Integer.parseInt(datos.get("numRegistros").toString())-1)
						cell3.setBorderWidth(0);
					
					if (i==Integer.parseInt(datos.get("numRegistros").toString())-1 && op.get("tarifa").toString().equals(ConstantesBD.acronimoNo))
						cell3.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT);
					else
						if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
							cell3.setBorder(Rectangle.BOTTOM);
					else
						if (op.get("tarifa").toString().equals(ConstantesBD.acronimoNo))
							cell3.setBorder(Rectangle.RIGHT);

					table.addCell(cell3);
					
				}
				
				if (op.get("tarifa").toString().equals(ConstantesBD.acronimoSi))
				{
				//
					p= new Paragraph(UtilidadTexto.formatearValores(datos.get("tarifauni_"+i)+"", 2, false, true));
					p.setIndentationRight(identacionDerecha);
					p.getFont().setSize(7);
					p.setAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
					cell3 = 
						new PdfPCell(p);
					cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
					if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
						cell3.setBorder(Rectangle.BOTTOM);
					else
						cell3.setBorderWidth(0);
					table.addCell(cell3);
					
					String t="";
					if (Utilidades.convertirADouble(datos.get("tarifauni_"+i).toString())>0) {
						t=(Utilidades.convertirADouble(datos.get("tarifauni_"+i)+"")*Utilidades.convertirADouble(datos.get("cantcons_"+i)+""))+"";
					}
					
					//
					p= new Paragraph(UtilidadTexto.formatearValores(t, 2, false, true));
					p.setIndentationRight(identacionDerecha);
					p.getFont().setSize(7);
					p.setAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
					cell3 = 
						new PdfPCell(p);
					
					if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
						cell3.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT);
					else
						cell3.setBorder(Rectangle.RIGHT);
					cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
					table.addCell(cell3);
					
				}
				
				
				if (op.get("costo").toString().equals(ConstantesBD.acronimoNo) && op.get("tarifa").toString().equals(ConstantesBD.acronimoNo))
				{
					p= new Paragraph(UtilidadTexto.formatearValores(datos.get("valortotal_"+i)+"", 2, false, true));
					p.setIndentationRight(identacionDerecha);
					p.getFont().setSize(7);
					p.setAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
					cell3 = 
						new PdfPCell(p);
					cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
					if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
						cell3.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT);
					else
						cell3.setBorder(Rectangle.RIGHT);	
					table.addCell(cell3);
					
				}

				
			}
		}
		
		if(y==0)
		{
			p= new Paragraph("No se encontraron Registros de Consumo de Materiales");
			p.setIndentationRight(identacionDerecha);
			p.getFont().setSize(7);
			p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			cell3 = 
				new PdfPCell(p);
			cell3.setColspan(11);
			cell3.setBackgroundColor(Color.LIGHT_GRAY);
			cell3.setBorderWidth(0);
			cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			table.addCell(cell3);
			
		}
		
	/********************************
	 * 
	 * Materiales Especiales
	 * 
	 ********************************/
		y=0;
	if(op.get("materiales").toString().equals(ConstantesBD.acronimoSi))
	{
		
		p= new Paragraph("Materiales Especiales");
		p.setIndentationRight(identacionDerecha);
		p.getFont().setSize(7);
		p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		cell3 = 
			new PdfPCell(p);
		cell3.setColspan(11);
		cell3.setBackgroundColor(Color.GRAY);
		cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		table.addCell(cell3);
		
		
		p= new Paragraph("Cod");
		p.setIndentationRight(identacionDerecha);
		p.getFont().setSize(7);
		p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		cell3 = 
			new PdfPCell(p);
		cell3.setBackgroundColor(new Color(213, 220, 228));
		cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		table.addCell(cell3);
		
		
		p= new Paragraph("Descripción");
		p.setIndentationRight(identacionDerecha);
		p.getFont().setSize(7);
		p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		cell3 = 
			new PdfPCell(p);
		cell3.setColspan(6-col);
		cell3.setBackgroundColor(new Color(213, 220, 228));
		cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		table.addCell(cell3);

		
		p= new Paragraph("Unidad de Medida");
		p.setIndentationRight(identacionDerecha);
		p.getFont().setSize(7);
		p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		cell3 = 
			new PdfPCell(p);
		cell3.setBackgroundColor(new Color(213, 220, 228));
		cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		table.addCell(cell3);
		
		p= new Paragraph("Cantidad  Total  Despachada");
		p.setIndentationRight(identacionDerecha);
		p.getFont().setSize(7);
		p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		cell3 = 
			new PdfPCell(p);
		cell3.setBackgroundColor(new Color(213, 220, 228));
		cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		table.addCell(cell3);
		
		
		p= new Paragraph("Cantidad Total Consumo");
		p.setIndentationRight(identacionDerecha);
		p.getFont().setSize(7);
		p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		cell3 = 
			new PdfPCell(p);
		cell3.setBackgroundColor(new Color(213, 220, 228));
		cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		table.addCell(cell3);
		
		//--		
		p= new Paragraph("Cantidad Total Devuelta");
		p.setIndentationRight(identacionDerecha);
		p.getFont().setSize(7);
		p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		cell3 = 
			new PdfPCell(p);
		cell3.setBackgroundColor(new Color(213, 220, 228));
		cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		table.addCell(cell3);
		
		
		if (op.get("costo").toString().equals(ConstantesBD.acronimoSi))
		{
			p= new Paragraph("Costo Unitario");
			p.setIndentationRight(identacionDerecha);
			p.getFont().setSize(7);
			p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			cell3 = 
				new PdfPCell(p);
			cell3.setBackgroundColor(new Color(213, 220, 228));
			cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			table.addCell(cell3);
			
			p= new Paragraph("Total Costo  Consumo");
			p.setIndentationRight(identacionDerecha);
			p.getFont().setSize(7);
			p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			cell3 = 
				new PdfPCell(p);
			cell3.setBackgroundColor(new Color(213, 220, 228));
			cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			table.addCell(cell3);
		}
		
		if (op.get("tarifa").toString().equals(ConstantesBD.acronimoSi))
		{
			p= new Paragraph("Tarifa Unitaria");
			p.setIndentationRight(identacionDerecha);
			p.getFont().setSize(7);
			p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			cell3 = 
				new PdfPCell(p);
			cell3.setBackgroundColor(new Color(213, 220, 228));
			cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			table.addCell(cell3);
			
			p= new Paragraph("Total Consumo Tarifa ");
			p.setIndentationRight(identacionDerecha);
			p.getFont().setSize(7);
			p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			cell3 = 
				new PdfPCell(p);
			cell3.setBackgroundColor(new Color(213, 220, 228));
			cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			table.addCell(cell3);
			
		}
		if (op.get("costo").toString().equals(ConstantesBD.acronimoNo) && op.get("tarifa").toString().equals(ConstantesBD.acronimoNo))
		{
			p= new Paragraph("Total Costo  Consumo");
			p.setIndentationRight(identacionDerecha);
			p.getFont().setSize(7);
			p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			cell3 = 
				new PdfPCell(p);
			cell3.setBackgroundColor(new Color(213, 220, 228));
			cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			table.addCell(cell3);
		}
		
		
		for(int i=0;i<Integer.parseInt(datos.get("numRegistros").toString());i++)
		{
			if(datos.get("incluido_"+i).toString().equals(ConstantesBD.acronimoNo+"") && datos.get("indica_"+i).toString().equals(ConstantesBD.acronimoNo+""))
			{
				y++;
				
				//
				p= new Paragraph(datos.get("cod_"+i)+"");
				p.setIndentationRight(identacionDerecha);
				p.getFont().setSize(7);
				p.setAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
				cell3 = 
					new PdfPCell(p);
				cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
				
				if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
				{
					cell3.setBorder(Rectangle.BOTTOM | Rectangle.LEFT);
				}
				else
					cell3.setBorder(Rectangle.LEFT);
					
				table.addCell(cell3);
				
				//
				String descripcion=(datos.get("descripcion_"+i)+"");
				if (descripcion.length()>truncar)
					descripcion=descripcion.substring(0, truncar);
				p= new Paragraph(descripcion);
				p.setIndentationRight(identacionDerecha);
				p.getFont().setSize(7);
				p.setAlignment(iTextBaseDocument.ALIGNMENT_LEFT);
				cell3 = 
					new PdfPCell(p);
				cell3.setColspan(6-col);
				
				if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
					cell3.setBorder(Rectangle.BOTTOM);
				else
					cell3.setBorderWidth(0);
				table.addCell(cell3);
				
				//
				p= new Paragraph(datos.get("unimedida_"+i)+"");
				p.setIndentationRight(identacionDerecha);
				p.getFont().setSize(7);
				p.setAlignment(iTextBaseDocument.ALIGNMENT_LEFT);
				cell3 = 
					new PdfPCell(p);
				if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
					cell3.setBorder(Rectangle.BOTTOM);
				else
					cell3.setBorderWidth(0);				
				
				table.addCell(cell3);
				
				//
				p= new Paragraph(UtilidadTexto.formatearValores(datos.get("cantdesp_"+i)+"", 2, false, true));
				p.setIndentationRight(identacionDerecha);
				p.getFont().setSize(7);
				p.setAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
				cell3 = 
					new PdfPCell(p);
				
				cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
				if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
					cell3.setBorder(Rectangle.BOTTOM);
				else
					cell3.setBorderWidth(0);
				
				table.addCell(cell3);
				
				//
				p= new Paragraph(UtilidadTexto.formatearValores(datos.get("cantcons_"+i)+"", 2, false, true));
				p.setIndentationRight(identacionDerecha);
				p.getFont().setSize(7);
				p.setAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
				cell3 = 
					new PdfPCell(p);
				
				cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
				if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
					cell3.setBorder(Rectangle.BOTTOM);
				else
					cell3.setBorderWidth(0);
				
				table.addCell(cell3);
				
				//
				p= new Paragraph(UtilidadTexto.formatearValores(datos.get("cantdevu_"+i)+"", 2, false, true));
				p.setIndentationRight(identacionDerecha);
				p.getFont().setSize(7);
				p.setAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
				cell3 = 
					new PdfPCell(p);
				
				cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
				if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
					cell3.setBorder(Rectangle.BOTTOM);
				else
					cell3.setBorderWidth(0);
				
				table.addCell(cell3);
				
				
				//
				if (op.get("costo").toString().equals(ConstantesBD.acronimoSi))
				{
					//
					p= new Paragraph(UtilidadTexto.formatearValores(datos.get("costouni_"+i)+"", 2, false, true));
					p.setIndentationRight(identacionDerecha);
					p.getFont().setSize(7);
					p.setAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
					cell3 = 
						new PdfPCell(p);
					
					cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
					if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
						cell3.setBorder(Rectangle.BOTTOM);
					else
						cell3.setBorderWidth(0);
					
					table.addCell(cell3);
						
					//
					p= new Paragraph(UtilidadTexto.formatearValores(datos.get("valortotal_"+i)+"", 2, false, true));
					p.setIndentationRight(identacionDerecha);
					p.getFont().setSize(7);
					p.setAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
					cell3 = 
						new PdfPCell(p);
					
					cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
				
					if (op.get("tarifa").toString().equals(ConstantesBD.acronimoSi) & i<Integer.parseInt(datos.get("numRegistros").toString())-1)
						cell3.setBorderWidth(0);
					
					if (i==Integer.parseInt(datos.get("numRegistros").toString())-1 && op.get("tarifa").toString().equals(ConstantesBD.acronimoNo))
						cell3.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT);
					else
						if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
							cell3.setBorder(Rectangle.BOTTOM);
					else
						if (op.get("tarifa").toString().equals(ConstantesBD.acronimoNo))
							cell3.setBorder(Rectangle.RIGHT);

					table.addCell(cell3);
					
				}
				
				if (op.get("tarifa").toString().equals(ConstantesBD.acronimoSi))
				{
				//
					p= new Paragraph(UtilidadTexto.formatearValores(datos.get("tarifauni_"+i)+"", 2, false, true) );
					p.setIndentationRight(identacionDerecha);
					p.getFont().setSize(7);
					p.setAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
					cell3 = 
						new PdfPCell(p);
					cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
					if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
						cell3.setBorder(Rectangle.BOTTOM);
					else
						cell3.setBorderWidth(0);
					table.addCell(cell3);
					
					String t="";
					if (Utilidades.convertirADouble(datos.get("tarifauni_"+i).toString())>0) {
						t=(Utilidades.convertirADouble(datos.get("tarifauni_"+i)+"")*Utilidades.convertirADouble(datos.get("cantcons_"+i)+""))+"";
					}
					
					//
					p= new Paragraph(UtilidadTexto.formatearValores(t, 2, false, true));
					p.setIndentationRight(identacionDerecha);
					p.getFont().setSize(7);
					p.setAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
					cell3 = 
						new PdfPCell(p);
					
					if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
						cell3.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT);
					else
						cell3.setBorder(Rectangle.RIGHT);
					cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
					table.addCell(cell3);
					
				}
				
				
				if (op.get("costo").toString().equals(ConstantesBD.acronimoNo) && op.get("tarifa").toString().equals(ConstantesBD.acronimoNo))
				{
					p= new Paragraph(UtilidadTexto.formatearValores(datos.get("valortotal_"+i)+"", 2, false, true));
					p.setIndentationRight(identacionDerecha);
					p.getFont().setSize(7);
					p.setAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
					cell3 = 
						new PdfPCell(p);
					cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
					if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
						cell3.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT);
					else
						cell3.setBorder(Rectangle.RIGHT);	
					table.addCell(cell3);
					
				}

				
			}
		}
		
		if(y==0)
		{
			p= new Paragraph("No se encontraron Registros de Consumo de Materiales Especiales");
			p.setIndentationRight(identacionDerecha);
			p.getFont().setSize(9);
			p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			cell3 = 
				new PdfPCell(p);
			cell3.setColspan(11);
			cell3.setBackgroundColor(Color.LIGHT_GRAY);
			table.addCell(cell3);
		}
		
	}	

		
		report.document.addTable(table);
		//document.add(table);	
		report.closeReport();
		//document.close();
	    
	    
	    
	  	}


	/**
	 * 
	 * @param con
	 * @param fileName
	 * @param servicios
	 * @param paciente
	 * @param usuario
	 * @param datos
	 * @param op
	 * @param request
	 */
	public static void imprimirListadoCirugia(Connection con, String fileName, HashMap servicios,PersonaBasica paciente, UsuarioBasico usuario, HashMap datos, HashMap op, HttpServletRequest request)
	{
		int identacionDerecha=3;
		InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
	    Document document = new Document();
	    try {
			PdfWriter.getInstance(document, 
			   new FileOutputStream(fileName));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}
		//	document.open();

			PdfReports report = new PdfReports("",true);
			
			report.setReportBaseHeader1( institucionBasica.getLogoReportes(), institucionBasica.getRazonSocial(), institucionBasica.getNit(), "ESTADO CONSUMO DE MATERIALES",10);
		    report.openReport(fileName);

		    report.setReportMargins((float)0.05,(float)0.05, (float)1, (float)1);
			
						    
		    report.document.addParagraph("Fecha de Proceso: "+UtilidadFecha.conversionFormatoFechaAAp(UtilidadFecha.getFechaActual()),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,16);
		    report.document.addParagraph("Usuario Proceso: "+usuario.getLoginUsuario(),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,10);
		    
		    
		    report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
		    report.font.useFont(iTextBaseFont.FONT_COURIER, false, false, false);
		    report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000, 9);
		    report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 5);
		    
	   	Paragraph p;
		
		PdfPTable table = new PdfPTable(13);
		table.setWidthPercentage(100);
		
		float widths[]={(float) 0.3,
						(float) 1.8, 
						(float) 0.7,
						(float) 0.9,
						(float) 1.4,
						(float) 0.9,
						(float) 0.9,
						(float) 0.8,
						(float) 0.8, 
						(float) 0.5, 
						(float) 1};
		try {
			table.setWidths(widths);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		
		p= new Paragraph(("Paciente :"+paciente.getNombrePersona()));
		p.setIndentationRight(identacionDerecha);
		p.getFont().setSize(8);
		p.setAlignment(iTextBaseDocument.ALIGNMENT_JUSTIFIED_ALL);
		PdfPCell cell3 = 
			new PdfPCell(p);
		cell3.setColspan(4);
		table.addCell(cell3);
		
		p= new Paragraph((paciente.getCodigoTipoIdentificacionPersona()+": "+paciente.getNumeroIdentificacionPersona()));
		p.setIndentationRight(identacionDerecha);
		p.getFont().setSize(8);
		p.setAlignment(iTextBaseDocument.ALIGNMENT_JUSTIFIED_ALL);
		cell3 = 
			new PdfPCell(p);
		cell3.setColspan(2);
		table.addCell(cell3);
		
		
		p= new Paragraph("Cuenta Numero: "+paciente.getCodigoCuenta());
		p.setIndentationRight(identacionDerecha);
		p.getFont().setSize(8);
		p.setAlignment(iTextBaseDocument.ALIGNMENT_JUSTIFIED_ALL);
		cell3 = 
			new PdfPCell(p);
		cell3.setColspan(2);
		table.addCell(cell3);
		
		p= new Paragraph("Solicitud  Nro. "+datos.get("noSolicitud")+" ");
		p.setIndentationRight(identacionDerecha);
		p.getFont().setSize(8);
		p.setAlignment(iTextBaseDocument.ALIGNMENT_JUSTIFIED_ALL);
		cell3 = 
			new PdfPCell(p);
		cell3.setColspan(2);
		table.addCell(cell3);
		
		p= new Paragraph("CC. solicitado. "+datos.get("farmacia")+" ");
		p.setIndentationRight(identacionDerecha);
		p.getFont().setSize(8);
		p.setAlignment(iTextBaseDocument.ALIGNMENT_JUSTIFIED_ALL);
		cell3 = 
			new PdfPCell(p);
		cell3.setColspan(3);
		table.addCell(cell3);
		
			
		int col=0;
		
		
		if (op.get("costo").toString().equals(ConstantesBD.acronimoSi))
		{
			col++;
			col++;
		}
		if (op.get("tarifa").toString().equals(ConstantesBD.acronimoSi))
		{
			col++;
			col++;
		}
		if (op.get("tarifa").toString().equals(ConstantesBD.acronimoNo) && op.get("costo").toString().equals(ConstantesBD.acronimoNo))
		{
			col++;
		}
		
		int truncar=33;
		if (col==2)
			truncar=truncar+32;
		else
			if(col==1)
				truncar=truncar+50;
	
		p= new Paragraph("Consumo de Materiales");
		p.setIndentationRight(identacionDerecha);
		p.getFont().setSize(9);
		p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		cell3 = 
			new PdfPCell(p);
		cell3.setColspan(13);
		cell3.setBackgroundColor(Color.GRAY );
		cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		table.addCell(cell3);
		
	//--		
		p= new Paragraph("Cod");
		p.setIndentationRight(identacionDerecha);
		p.getFont().setSize(9);
		p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		cell3 = 
			new PdfPCell(p);
		cell3.setBackgroundColor(new Color(213, 220, 228));
		cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		table.addCell(cell3);
		
		//--	
		p= new Paragraph("Descripción");
		p.setIndentationRight(identacionDerecha);
		p.getFont().setSize(9);
		p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		cell3 = 
			new PdfPCell(p);
		cell3.setColspan(6-col);
		cell3.setBackgroundColor(new Color(213, 220, 228));
		cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		table.addCell(cell3);

		//--	
		p= new Paragraph("Unidad de Medida");
		p.setIndentationRight(identacionDerecha);
		p.getFont().setSize(9);
		p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		cell3 = 
			new PdfPCell(p);
		cell3.setBackgroundColor(new Color(213, 220, 228));
		cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		table.addCell(cell3);

		//--		
		p= new Paragraph("Cantidad Total Despachada");
		p.setIndentationRight(identacionDerecha);
		p.getFont().setSize(9);
		p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		cell3 = 
			new PdfPCell(p);
		cell3.setBackgroundColor(new Color(213, 220, 228));
		cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		table.addCell(cell3);		
		
		//--		
		PdfPTable table1= new PdfPTable(Utilidades.convertirAEntero(servicios.get("numRegistros").toString()));
		p= new Paragraph("Cantidad Total Consumo");
		p.setIndentationRight(identacionDerecha);
		p.getFont().setSize(9);
		p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		cell3 = 
			new PdfPCell(p);
		cell3.setBackgroundColor(new Color(213, 220, 228));
		cell3.setColspan(Utilidades.convertirAEntero(servicios.get("numRegistros").toString()));
		cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		table1.addCell(cell3);
		
		int numserv=Integer.parseInt(servicios.get("numRegistros")+"");
		
		if(Integer.parseInt(servicios.get("numRegistros")+"")>0)
			{
				for(int i=0;i<Integer.parseInt(servicios.get("numRegistros")+"");i++)
					{
					
					p= new Paragraph("Cx"+(i+1));
					p.setIndentationRight(identacionDerecha);
					p.getFont().setSize(9);
					p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
					cell3 = 
						new PdfPCell(p);
					cell3.setBackgroundColor(new Color(213, 220, 228));
					cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
					table1.addCell(cell3);
					} 
			}
			else{
					p= new Paragraph("No Hay Registro De Servicios");
					p.setIndentationRight(identacionDerecha);
					p.getFont().setSize(9);
					p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
					cell3 = 
						new PdfPCell(p);
					cell3.setColspan(Utilidades.convertirAEntero(servicios.get("numRegistros").toString()));
					cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
					table1.addCell(cell3);
				} 
		
		
		cell3 =new PdfPCell(table1);
		cell3.setColspan(3);
		cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		table.addCell(cell3);
		
		
		//--		
		p= new Paragraph("Cantidad Total Devuelta");
		p.setIndentationRight(identacionDerecha);
		p.getFont().setSize(9);
		p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		cell3 = 
			new PdfPCell(p);
		cell3.setBackgroundColor(new Color(213, 220, 228));
		cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		table.addCell(cell3);
		
		//--		
		if (op.get("costo").toString().equals(ConstantesBD.acronimoSi))
		{
			p= new Paragraph("Costo Unitario");
			p.setIndentationRight(identacionDerecha);
			p.getFont().setSize(9);
			p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			cell3 = 
				new PdfPCell(p);
			cell3.setBackgroundColor(new Color(213, 220, 228));
			cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			table.addCell(cell3);
			
			//--		
			p= new Paragraph("Total Costo  Consumo");
			p.setIndentationRight(identacionDerecha);
			p.getFont().setSize(9);
			p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			cell3 = 
				new PdfPCell(p);
			cell3.setBackgroundColor(new Color(213, 220, 228));
			cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			table.addCell(cell3);
			
		}
		
		//--		
		if (op.get("tarifa").toString().equals(ConstantesBD.acronimoSi))
		{
			p= new Paragraph("Tarifa Unitaria");
			p.setIndentationRight(identacionDerecha);
			p.getFont().setSize(9);
			p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			cell3 = 
				new PdfPCell(p);
			cell3.setBackgroundColor(new Color(213, 220, 228));
			cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			table.addCell(cell3);
			
			p= new Paragraph("Total Consumo Tarifa");
			p.setIndentationRight(identacionDerecha);
			p.getFont().setSize(9);
			p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			cell3 = 
				new PdfPCell(p);
			cell3.setBackgroundColor(new Color(213, 220, 228));
			cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			table.addCell(cell3);
			
		}
		
		if (op.get("costo").toString().equals(ConstantesBD.acronimoNo) && op.get("tarifa").toString().equals(ConstantesBD.acronimoNo))
		{
			p= new Paragraph("Total Costo  Consumo");
			p.setIndentationRight(identacionDerecha);
			p.getFont().setSize(9);
			p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			cell3 = 
				new PdfPCell(p);
			cell3.setBackgroundColor(new Color(213, 220, 228));
			cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			table.addCell(cell3);
		}
		

		
		int y=0;
	
		for(int i=0;i<Integer.parseInt(datos.get("numRegistros").toString());i++)
		{
			if((datos.get("incluido_"+i).toString().equals(ConstantesBD.acronimoSi+"") 
					|| datos.get("indica_"+i).toString().equals(ConstantesBD.acronimoSi+""))
					 && datos.get("band_"+i).toString().equals(""+ConstantesBD.acronimoNo))
			{
				y++;
				
				
				p= new Paragraph(datos.get("cod_"+i)+"");
				p.setIndentationRight(identacionDerecha);
				p.getFont().setSize(7);
				p.setAlignment(iTextBaseDocument.ALIGNMENT_LEFT);
				cell3 = 
					new PdfPCell(p);
				cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
				if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
				{
					cell3.setBorder(Rectangle.BOTTOM | Rectangle.LEFT);
				}
				else
					cell3.setBorder(Rectangle.LEFT);
				table.addCell(cell3);
				
				String descripcion=(datos.get("descripcion_"+i)+"");
				if (descripcion.length()>truncar)
					descripcion=descripcion.substring(0, truncar);
				p= new Paragraph(descripcion);
				p.setIndentationRight(identacionDerecha);
				p.getFont().setSize(7);
				p.setAlignment(iTextBaseDocument.ALIGNMENT_LEFT);
				cell3 = 
					new PdfPCell(p);
				cell3.setColspan(6-col);
				if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
					cell3.setBorder(Rectangle.BOTTOM);
				else
					cell3.setBorderWidth(0);
				table.addCell(cell3);
				
				p= new Paragraph(datos.get("unimedida_"+i)+"");
				p.setIndentationRight(identacionDerecha);
				p.getFont().setSize(7);
				p.setAlignment(iTextBaseDocument.ALIGNMENT_LEFT);
				cell3 = 
					new PdfPCell(p);
				if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
					cell3.setBorder(Rectangle.BOTTOM);
				else
					cell3.setBorderWidth(0);
				table.addCell(cell3);

				
				p= new Paragraph(UtilidadTexto.formatearValores(datos.get("cantdesp_"+i)+"", 2, false, true));
				p.setIndentationRight(identacionDerecha);
				p.getFont().setSize(7);
				p.setAlignment(iTextBaseDocument.ALIGNMENT_LEFT);
				cell3 = 
					new PdfPCell(p);
				cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
				if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
					cell3.setBorder(Rectangle.BOTTOM);
				else
					cell3.setBorderWidth(0);
				table.addCell(cell3);
				
				PdfPTable table11= new PdfPTable(Utilidades.convertirAEntero(servicios.get("numRegistros").toString()));
				
				for (int y1=0;y1<Integer.parseInt(servicios.get("numRegistros")+"");y1++)
					{
						
					//<td bgcolor="#FFFFFF" align="center">
					p= new Paragraph("");
					for (int k=0; k<Integer.parseInt(datos.get("numRegistros")+""); k++)
						{
						if(datos.get("cod_"+i).toString().equals(datos.get("cod_"+k).toString())
								&& datos.get("codserv_"+k).toString().equals(servicios.get("codserv_"+y1).toString()))
							{
								p= new Paragraph(UtilidadTexto.formatearValores(datos.get("cantcons_"+k)+"", 2, false, true));
								datos.put("band_"+k,"S");
							} 
						}
					//td
					p.setIndentationRight(identacionDerecha);
					p.getFont().setSize(7);
					p.setAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
					cell3 = 
						new PdfPCell(p);
					cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
					if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
						cell3.setBorder(Rectangle.BOTTOM);
					else
						cell3.setBorderWidth(0);
					table11.addCell(cell3);
					}
				cell3 =new PdfPCell(table11);
				cell3.setColspan(3);
				cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
				if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
					cell3.setBorder(Rectangle.BOTTOM);
				else
					cell3.setBorderWidth(0);
				table.addCell(cell3);
				
				p= new Paragraph(UtilidadTexto.formatearValores(datos.get("cantdevu_"+i)+"", 2, false, true));
				p.setIndentationRight(identacionDerecha);
				p.getFont().setSize(7);
				p.setAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
				cell3 = 
					new PdfPCell(p);
				cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
				if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
					cell3.setBorder(Rectangle.BOTTOM);
				else
					cell3.setBorderWidth(0);
				table.addCell(cell3);
				
				
				//
				if (op.get("costo").toString().equals(ConstantesBD.acronimoSi))
				{
					//
					p= new Paragraph(UtilidadTexto.formatearValores(datos.get("costouni_"+i)+"", 2, false, true));
					p.setIndentationRight(identacionDerecha);
					p.getFont().setSize(7);
					p.setAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
					cell3 = 
						new PdfPCell(p);
					
					cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
					if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
						cell3.setBorder(Rectangle.BOTTOM);
					else
						cell3.setBorderWidth(0);
					
					table.addCell(cell3);
						
					//
					p= new Paragraph(UtilidadTexto.formatearValores(datos.get("valortotal_"+i)+"", 2, false, true));
					p.setIndentationRight(identacionDerecha);
					p.getFont().setSize(7);
					p.setAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
					cell3 = 
						new PdfPCell(p);
					
					cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
				
					if (op.get("tarifa").toString().equals(ConstantesBD.acronimoSi) & i<Integer.parseInt(datos.get("numRegistros").toString())-1)
						cell3.setBorderWidth(0);
					
					if (i==Integer.parseInt(datos.get("numRegistros").toString())-1 && op.get("tarifa").toString().equals(ConstantesBD.acronimoNo))
						cell3.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT);
					else
						if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
							cell3.setBorder(Rectangle.BOTTOM);
					else
						if (op.get("tarifa").toString().equals(ConstantesBD.acronimoNo))
							cell3.setBorder(Rectangle.RIGHT);

					table.addCell(cell3);
					
				}
				
				if (op.get("tarifa").toString().equals(ConstantesBD.acronimoSi))
				{
				//
					p= new Paragraph(UtilidadTexto.formatearValores(datos.get("tarifauni_"+i)+"", 2, false, true));
					p.setIndentationRight(identacionDerecha);
					p.getFont().setSize(7);
					p.setAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
					cell3 = 
						new PdfPCell(p);
					cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
					if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
						cell3.setBorder(Rectangle.BOTTOM);
					else
						cell3.setBorderWidth(0);
					table.addCell(cell3);
					
					String t="";
					if (Utilidades.convertirADouble(datos.get("tarifauni_"+i).toString())>0) {
						t=(Utilidades.convertirADouble(datos.get("tarifauni_"+i)+"")*Utilidades.convertirADouble(datos.get("cantcons_"+i)+""))+"";
					}
					
					//
					p= new Paragraph(UtilidadTexto.formatearValores(t, 2, false, true));
					p.setIndentationRight(identacionDerecha);
					p.getFont().setSize(7);
					p.setAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
					cell3 = 
						new PdfPCell(p);
					
					if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
						cell3.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT);
					else
						cell3.setBorder(Rectangle.RIGHT);
					cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
					table.addCell(cell3);
					
				}
				
				
				if (op.get("costo").toString().equals(ConstantesBD.acronimoNo) && op.get("tarifa").toString().equals(ConstantesBD.acronimoNo))
				{
					p= new Paragraph(UtilidadTexto.formatearValores(datos.get("valortotal_"+i)+"", 2, false, true));
					p.setIndentationRight(identacionDerecha);
					p.getFont().setSize(7);
					p.setAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
					cell3 = 
						new PdfPCell(p);
					cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
					if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
						cell3.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT);
					else
						cell3.setBorder(Rectangle.RIGHT);	
					table.addCell(cell3);
					
				}
				
			}
		}
		
		if(y==0)
		{
			p= new Paragraph("No se encontraron Registros de Consumo de Materiales");
			p.setIndentationRight(identacionDerecha);
			p.getFont().setSize(9);
			p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			cell3 = 
				new PdfPCell(p);
			cell3.setColspan(13);
			cell3.setBackgroundColor(Color.LIGHT_GRAY);
			cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			table.addCell(cell3);
		}
		
	/********************************
	 * 
	 * Materiales Especiales
	 * 
	 ********************************/
	y=0;
	if(op.get("materiales").toString().equals(ConstantesBD.acronimoSi))
	{
		
		p= new Paragraph("Consumo de Materiales");
		p.setIndentationRight(identacionDerecha);
		p.getFont().setSize(9);
		p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		cell3 = 
			new PdfPCell(p);
		cell3.setColspan(13);
		cell3.setBackgroundColor(Color.GRAY );
		cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		table.addCell(cell3);
		
	//--		
		p= new Paragraph("Cod");
		p.setIndentationRight(identacionDerecha);
		p.getFont().setSize(9);
		p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		cell3 = 
			new PdfPCell(p);
		cell3.setBackgroundColor(new Color(213, 220, 228));
		cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		table.addCell(cell3);
		
		//--	
		p= new Paragraph("Descripción");
		p.setIndentationRight(identacionDerecha);
		p.getFont().setSize(9);
		p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		cell3 = 
			new PdfPCell(p);
		cell3.setColspan(6-col);
		cell3.setBackgroundColor(new Color(213, 220, 228));
		cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		table.addCell(cell3);

		//--	
		p= new Paragraph("Unidad de Medida");
		p.setIndentationRight(identacionDerecha);
		p.getFont().setSize(9);
		p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		cell3 = 
			new PdfPCell(p);
		cell3.setBackgroundColor(new Color(213, 220, 228));
		cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		table.addCell(cell3);

		//--		
		p= new Paragraph("Cantidad Total Despachada");
		p.setIndentationRight(identacionDerecha);
		p.getFont().setSize(9);
		p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		cell3 = 
			new PdfPCell(p);
		cell3.setBackgroundColor(new Color(213, 220, 228));
		cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		table.addCell(cell3);		
		
		//--		
		table1= new PdfPTable(Utilidades.convertirAEntero(servicios.get("numRegistros").toString()));
		p= new Paragraph("Cantidad Total Consumo");
		p.setIndentationRight(identacionDerecha);
		p.getFont().setSize(9);
		p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		cell3 = 
			new PdfPCell(p);
		cell3.setBackgroundColor(new Color(213, 220, 228));
		cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		cell3.setColspan(Utilidades.convertirAEntero(servicios.get("numRegistros").toString()));
		table1.addCell(cell3);
		
		numserv=Integer.parseInt(servicios.get("numRegistros")+"");
		
		if(Integer.parseInt(servicios.get("numRegistros")+"")>0)
			{
				for(int i=0;i<Integer.parseInt(servicios.get("numRegistros")+"");i++)
					{
					
					p= new Paragraph("Cx"+(i+1));
					p.setIndentationRight(identacionDerecha);
					p.getFont().setSize(9);
					p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
					cell3 = 
						new PdfPCell(p);
					cell3.setBackgroundColor(new Color(213, 220, 228));
					cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
					table1.addCell(cell3);
					} 
			}
			else{
					p= new Paragraph("No Hay Registro De Servicios");
					p.setIndentationRight(identacionDerecha);
					p.getFont().setSize(9);
					p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
					cell3 = 
						new PdfPCell(p);
					cell3.setColspan(Utilidades.convertirAEntero(servicios.get("numRegistros").toString()));
					cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
					table1.addCell(cell3);
				} 
		
		
		cell3 =new PdfPCell(table1);
		cell3.setColspan(3);
		cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		table.addCell(cell3);
		
		
		//--		
		p= new Paragraph("Cantidad Total Devuelta");
		p.setIndentationRight(identacionDerecha);
		p.getFont().setSize(9);
		p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		cell3 = 
			new PdfPCell(p);
		cell3.setBackgroundColor(new Color(213, 220, 228));
		cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		table.addCell(cell3);
		
		//--		
		if (op.get("costo").toString().equals(ConstantesBD.acronimoSi))
		{
			p= new Paragraph("Costo Unitario");
			p.setIndentationRight(identacionDerecha);
			p.getFont().setSize(9);
			p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			cell3 = 
				new PdfPCell(p);
			cell3.setBackgroundColor(new Color(213, 220, 228));
			cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			table.addCell(cell3);
			
			//--		
			p= new Paragraph("Total Costo  Consumo");
			p.setIndentationRight(identacionDerecha);
			p.getFont().setSize(9);
			p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			cell3 = 
				new PdfPCell(p);
			cell3.setBackgroundColor(new Color(213, 220, 228));
			cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			table.addCell(cell3);
			
		}
		
		//--		
		if (op.get("tarifa").toString().equals(ConstantesBD.acronimoSi))
		{
			p= new Paragraph("Tarifa Unitaria");
			p.setIndentationRight(identacionDerecha);
			p.getFont().setSize(9);
			p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			cell3 = 
				new PdfPCell(p);
			cell3.setBackgroundColor(new Color(213, 220, 228));
			cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			table.addCell(cell3);
			
			p= new Paragraph("Total Consumo Tarifa");
			p.setIndentationRight(identacionDerecha);
			p.getFont().setSize(9);
			p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			cell3 = 
				new PdfPCell(p);
			cell3.setBackgroundColor(new Color(213, 220, 228));
			cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			table.addCell(cell3);
			
		}
		
		if (op.get("costo").toString().equals(ConstantesBD.acronimoNo) && op.get("tarifa").toString().equals(ConstantesBD.acronimoNo))
		{
			p= new Paragraph("Total Costo  Consumo");
			p.setIndentationRight(identacionDerecha);
			p.getFont().setSize(9);
			p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			cell3 = 
				new PdfPCell(p);
			cell3.setBackgroundColor(new Color(213, 220, 228));
			cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			table.addCell(cell3);
		}
		
		y=0;

		for(int i=0;i<Integer.parseInt(datos.get("numRegistros").toString());i++)
		{
			if((datos.get("incluido_"+i).toString().equals(ConstantesBD.acronimoNo+"") 
					&& datos.get("indica_"+i).toString().equals(ConstantesBD.acronimoNo+""))
					 && datos.get("band_"+i).toString().equals(""+ConstantesBD.acronimoNo))
			{
				y++;
				
				
				p= new Paragraph(datos.get("cod_"+i)+"");
				p.setIndentationRight(identacionDerecha);
				p.getFont().setSize(7);
				p.setAlignment(iTextBaseDocument.ALIGNMENT_LEFT);
				cell3 = 
					new PdfPCell(p);
				cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
				if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
					cell3.setBorder(Rectangle.BOTTOM | Rectangle.LEFT);
				else
					cell3.setBorder(Rectangle.LEFT);
				table.addCell(cell3);
				
				
				String descripcion=(datos.get("descripcion_"+i)+"");
				if (descripcion.length()>truncar)
					descripcion=descripcion.substring(0, truncar);
				p= new Paragraph(descripcion);
				p.setIndentationRight(identacionDerecha);
				p.getFont().setSize(7);
				p.setAlignment(iTextBaseDocument.ALIGNMENT_LEFT);
				cell3 = 
					new PdfPCell(p);
				cell3.setColspan(6-col);
				if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
					cell3.setBorder(Rectangle.BOTTOM);
				else
					cell3.setBorderWidth(0);
				table.addCell(cell3);
				
				p= new Paragraph(datos.get("unimedida_"+i)+"");
				p.setIndentationRight(identacionDerecha);
				p.getFont().setSize(7);
				p.setAlignment(iTextBaseDocument.ALIGNMENT_LEFT);
				cell3 = 
					new PdfPCell(p);
				if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
					cell3.setBorder(Rectangle.BOTTOM);
				else
					cell3.setBorderWidth(0);
				table.addCell(cell3);

				
				p= new Paragraph(UtilidadTexto.formatearValores(datos.get("cantdesp_"+i)+"", 2, false, true));
				p.setIndentationRight(identacionDerecha);
				p.getFont().setSize(7);
				p.setAlignment(iTextBaseDocument.ALIGNMENT_LEFT);
				cell3 = 
					new PdfPCell(p);
				cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
				if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
					cell3.setBorder(Rectangle.BOTTOM);
				else
					cell3.setBorderWidth(0);
				table.addCell(cell3);
				
				PdfPTable table11= new PdfPTable(Utilidades.convertirAEntero(servicios.get("numRegistros").toString()));
				
				for (int y1=0;y1<Integer.parseInt(servicios.get("numRegistros")+"");y1++)
					{
						
					//<td bgcolor="#FFFFFF" align="center">
					p= new Paragraph("");
					for (int k=0; k<Integer.parseInt(datos.get("numRegistros")+""); k++)
						{
						if(datos.get("cod_"+i).toString().equals(datos.get("cod_"+k).toString())
								&& datos.get("codserv_"+k).toString().equals(servicios.get("codserv_"+y1).toString()))
							{
								p= new Paragraph(UtilidadTexto.formatearValores(datos.get("cantcons_"+k)+"", 2, false, true));
								datos.put("band_"+k,"S");
							} 
						}
					//td
					p.setIndentationRight(identacionDerecha);
					p.getFont().setSize(7);
					p.setAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
					cell3 = 
						new PdfPCell(p);
					cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
					if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
						cell3.setBorder(Rectangle.BOTTOM);
					else
						cell3.setBorderWidth(0);
					table11.addCell(cell3);
					}
				cell3 =new PdfPCell(table11);
				cell3.setColspan(3);
				cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
				if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
					cell3.setBorder(Rectangle.BOTTOM);
				else
					cell3.setBorderWidth(0);
				table.addCell(cell3);
				
				
				p= new Paragraph(UtilidadTexto.formatearValores(datos.get("cantdevu_"+i)+"", 2, false, true));
				p.setIndentationRight(identacionDerecha);
				p.getFont().setSize(7);
				p.setAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
				cell3 = 
					new PdfPCell(p);
				cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
				if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
					cell3.setBorder(Rectangle.BOTTOM);
				else
					cell3.setBorderWidth(0);
				table.addCell(cell3);
				
				
				//
				if (op.get("costo").toString().equals(ConstantesBD.acronimoSi))
				{
					//
					p= new Paragraph(UtilidadTexto.formatearValores(datos.get("costouni_"+i)+"", 2, false, true));
					p.setIndentationRight(identacionDerecha);
					p.getFont().setSize(7);
					p.setAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
					cell3 = 
						new PdfPCell(p);
					
					cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
					if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
						cell3.setBorder(Rectangle.BOTTOM);
					else
						cell3.setBorderWidth(0);
					
					table.addCell(cell3);
						
					//
					p= new Paragraph(UtilidadTexto.formatearValores(datos.get("valortotal_"+i)+"", 2, false, true));
					p.setIndentationRight(identacionDerecha);
					p.getFont().setSize(7);
					p.setAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
					cell3 = 
						new PdfPCell(p);
					
					cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
				
					if (op.get("tarifa").toString().equals(ConstantesBD.acronimoSi) & i<Integer.parseInt(datos.get("numRegistros").toString())-1)
						cell3.setBorderWidth(0);
					
					if (i==Integer.parseInt(datos.get("numRegistros").toString())-1 && op.get("tarifa").toString().equals(ConstantesBD.acronimoNo))
						cell3.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT);
					else
						if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
							cell3.setBorder(Rectangle.BOTTOM);
					else
						if (op.get("tarifa").toString().equals(ConstantesBD.acronimoNo))
							cell3.setBorder(Rectangle.RIGHT);

					table.addCell(cell3);
					
				}
				
				if (op.get("tarifa").toString().equals(ConstantesBD.acronimoSi))
				{
				//
					p= new Paragraph(UtilidadTexto.formatearValores(datos.get("tarifauni_"+i)+"", 2, false, true));
					p.setIndentationRight(identacionDerecha);
					p.getFont().setSize(7);
					p.setAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
					cell3 = 
						new PdfPCell(p);
					cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
					if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
						cell3.setBorder(Rectangle.BOTTOM);
					else
						cell3.setBorderWidth(0);
					table.addCell(cell3);
					
					String t="";
					if (Utilidades.convertirADouble(datos.get("tarifauni_"+i).toString())>0) {
						t=(Utilidades.convertirADouble(datos.get("tarifauni_"+i)+"")*Utilidades.convertirADouble(datos.get("cantcons_"+i)+""))+"";
					}
					
					//
					p= new Paragraph(UtilidadTexto.formatearValores(t, 2, false, true));
					p.setIndentationRight(identacionDerecha);
					p.getFont().setSize(7);
					p.setAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
					cell3 = 
						new PdfPCell(p);
					
					if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
						cell3.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT);
					else
						cell3.setBorder(Rectangle.RIGHT);
					cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
					table.addCell(cell3);
					
				}
				
				
				if (op.get("costo").toString().equals(ConstantesBD.acronimoNo) && op.get("tarifa").toString().equals(ConstantesBD.acronimoNo))
				{
					p= new Paragraph(UtilidadTexto.formatearValores(datos.get("valortotal_"+i)+"", 2, false, true));
					p.setIndentationRight(identacionDerecha);
					p.getFont().setSize(7);
					p.setAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
					cell3 = 
						new PdfPCell(p);
					cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_RIGHT);
					if (i==Integer.parseInt(datos.get("numRegistros").toString())-1)
						cell3.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT);
					else
						cell3.setBorder(Rectangle.RIGHT);	
					table.addCell(cell3);
					
				}
				
			}
		}
		
		if(y==0)
		{
			p= new Paragraph("No se encontraron Registros de Consumo de Materiales Especiales");
			p.setIndentationRight(identacionDerecha);
			p.getFont().setSize(9);
			p.setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			cell3 = 
				new PdfPCell(p);
			cell3.setColspan(13);
			cell3.setBackgroundColor(Color.CYAN);
			cell3.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
			table.addCell(cell3);
		}
		
	
	}	

		
	report.document.addTable(table);
	//document.add(table);	
	report.closeReport();
	//document.close();
	    
	    
	    
	  	}


}
