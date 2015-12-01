package com.princetonsa.pdf;

import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import util.UtilidadTexto;
import util.Utilidades;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;

import com.lowagie.text.BadElementException;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;

public class AjustesPdf
{

	public static void imprimirListadoAjustes(Connection con, String fileName, HashMap mapAjustes, UsuarioBasico usuario, HashMap camposBusqueda, HttpServletRequest request)
	{
		PdfReports report = new PdfReports();
		InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		report.setReportBaseHeader1( institucionBasica, institucionBasica.getNit(), "RELACION DE AJUSTES CARTERA");
	    report.openReport(fileName);

		if(!(camposBusqueda.get("estado")+"").trim().equals("")&&Integer.parseInt(camposBusqueda.get("estado")+"")>0)
			report.document.addParagraph("Estado: "+mapAjustes.get("nombre_estado_0"),report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
		if(!(camposBusqueda.get("tipoAjuste")+"").trim().equals("")&&Integer.parseInt(camposBusqueda.get("tipoAjuste")+"")>0)
			report.document.addParagraph("Tipo Ajuste: "+mapAjustes.get("nombre_tipo_ajuste_corto_0"),report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
		if(!(camposBusqueda.get("ajusteInicial")+"").trim().equals(""))
			report.document.addParagraph("Ajuste Inicial: "+camposBusqueda.get("ajusteInicial"),report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
		if(!(camposBusqueda.get("ajusteFinal")+"").trim().equals(""))
			report.document.addParagraph("Ajuste Final: "+camposBusqueda.get("ajusteFinal"),report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
		report.document.addParagraph("Fecha Inicial: "+camposBusqueda.get("fechaInicial"),report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
		report.document.addParagraph("Fecha Final: "+camposBusqueda.get("fechaFinal"),report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
		if(!(camposBusqueda.get("concepto")+"").trim().equals("")&&Integer.parseInt(camposBusqueda.get("concepto")+"")>0)
			report.document.addParagraph("Concepto: "+camposBusqueda.get("concepto"),report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
		if(!(camposBusqueda.get("factura")+"").trim().equals(""))
			report.document.addParagraph("Factura: "+camposBusqueda.get("factura"),report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
		if(!(camposBusqueda.get("convenio")+"").trim().equals("")&&Integer.parseInt(camposBusqueda.get("convenio")+"")>0)
			report.document.addParagraph("Convenio: "+Utilidades.obtenerNombreConvenio(con, Integer.parseInt(camposBusqueda.get("convenio")+"")),report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);

		
		
		
		
	    report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
	    report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
	    report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000, 9);
	    report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 7);
	    
	    
	    
	    
	    report.createSection("ajustes",PdfReports.REPORT_PARENT_TABLE,1,8,10);
		report.setReportDataSectionAtributes(0xFFFFFF, 0xFFFFFF, 0x000000, 9);
		report.getSectionReference("ajustes").setTableCellsColSpan(1);
		report.font.useFont(iTextBaseFont.FONT_TIMES, true, false, false);

		report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, "Ajuste", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
		report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, "Estado", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
		report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, "Fecha Aprobaci�n", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
		report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, "Usuario Aprobaci�n", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
		report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, "Valor", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
		report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, "Factura", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
		report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, "CxC", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
		report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, "Convenio", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
		

		report.setReportDataSectionAtributes(0xFFFFFF, 0xFFFFFF, 0x000000, 8);
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);

		for(int j=0;j<Integer.parseInt(mapAjustes.get("numReg")+"");j++)
		{
			if (!mapAjustes.get("valor_ajuste_"+j).toString().equals("0.00") && !mapAjustes.get("valor_ajuste_"+j).toString().equals("0"))
			{
			HashMap factura=new HashMap();
			String tempoFact="",conv=mapAjustes.get("nombre_convenio_"+j)+"";
			if((mapAjustes.get("esPorCxC_"+j)+"").equals("false"))
			{
				factura=(HashMap)mapAjustes.get("detalleAjuste_"+j);
				tempoFact=factura.get("consecutivo_factura_"+0)+"";
				conv=factura.get("nombre_convenio_"+0)+"";
			}
			report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, mapAjustes.get("nombre_tipo_ajuste_corto_"+j)+" - "+ mapAjustes.get("consecutivo_ajuste_"+j) +" "+mapAjustes.get("reversado_"+j), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
			report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, mapAjustes.get("nombre_estado_"+j)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
			String fechaAprobacion="";
			if(mapAjustes.containsKey("fecha_aprobacion_"+j))
			{
				fechaAprobacion=mapAjustes.get("fecha_aprobacion_"+j)+"";
			}
			String usuarioAprobacion="";
			if(mapAjustes.containsKey("usuario_aprobacion_"+j))
			{
				usuarioAprobacion=mapAjustes.get("usuario_aprobacion_"+j)+"";
			}
			report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, fechaAprobacion, report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
			report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, usuarioAprobacion, report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
			report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, UtilidadTexto.formatearValores(mapAjustes.get("valor_ajuste_"+j)+""), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
			report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, tempoFact, report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
			report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, Integer.parseInt(mapAjustes.get("cuenta_cobro_"+j)+"")>0?mapAjustes.get("cuenta_cobro_"+j)+"":"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
			report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, conv, report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
			}
			}
		
		float widths[]={(float) 1,(float) 1,(float) 1.25,(float) 1.25,(float) 1.5,(float) 1,(float) 1,(float) 2};
		try
        {
		    report.getSectionReference("ajustes").getTableReference("parentTable").setWidths(widths);
        }
        catch (BadElementException e)
        {
            e.printStackTrace();
        }
		
		report.addSectionToDocument("ajustes");
	    
		
		report.document.addParagraph("___________________________________________",report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
		report.document.addParagraph("Elaborado.",report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
		report.document.addParagraph("___________________________________________",report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);
		report.document.addParagraph("Revisado. ",report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);

	    //cerrar documento
	    report.closeReport(); 
	}

	
	/**
	 * Imprimir detalle del ajuste
	 * @param con
	 * @param string
	 * @param mapAjustes
	 * @param usuario
	 * @param regSel
	 */
	public static void imprimirDetalleAjusteConsulta(Connection con, String fileName, String codigoAjuste, HashMap mapAjusteArtSer, UsuarioBasico usuario, HttpServletRequest request)
	{
		InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		HashMap mapAjustes=Utilidades.consultarEncabezadoAjuste(con, codigoAjuste);
		PdfReports report = new PdfReports();
		/**Cabezote principal**/
		report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), "RELACION DE AJUSTES CARTERA");
	    report.openReport(fileName);
	    
		report.document.addParagraph("Ajuste "+mapAjustes.get("nombretipoajuste")+" N� "+mapAjustes.get("consecutivoajuste")+"           Estado: "+mapAjustes.get("nombreestado")+"      "+(UtilidadTexto.getBoolean(mapAjustes.get("castigocartera")+"")?"Castigo":"")+(UtilidadTexto.getBoolean(mapAjustes.get("ajustereversado")+"")?"Reversado":""),report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
		report.document.addParagraph("Elaborado por: "+mapAjustes.get("usuarioelaboracion")+" El "+mapAjustes.get("fechaelaboracion"),report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
		report.document.addParagraph("Observaciones: "+mapAjustes.get("observaciones"),report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
		String fechaAprobacion="";
		if(mapAjustes.containsKey("fechaaprobacion"))
		{
			fechaAprobacion=mapAjustes.get("fechaaprobacion")+"";
		}
		String usuarioAprobacion="";
		if(mapAjustes.containsKey("usuarioaprobacion"))
		{
			usuarioAprobacion=mapAjustes.get("usuarioaprobacion")+"";
		}
		if(!fechaAprobacion.trim().equals("")&&!usuarioAprobacion.trim().equals(""))
			report.document.addParagraph("Aprobado por: "+usuarioAprobacion+" El: "+fechaAprobacion,report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
		
		String conv=mapAjustes.get("nombreconvenio")+"";
		report.document.addParagraph("Convenio: "+conv,report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);

		
		
		crearDetalleDelAjuste(con,codigoAjuste,report,mapAjustes.get("valorajuste")+"", mapAjusteArtSer );
		
		
		report.document.addParagraph("___________________________________________",report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
		report.document.addParagraph("Elaborado.",report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
		report.document.addParagraph("___________________________________________",report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);
		report.document.addParagraph("Revisado. ",report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);

	    //cerrar documento
	    report.closeReport();
	}


	/**
	 * Imprimir detalle del ajuste 
	 * @param con
	 * @param string
	 * @param mapAjustes
	 * @param usuario
	 * @param regSel
	 */
	public static void imprimirDetalleAjusteConsulta(Connection con, String fileName, String codigoAjuste, UsuarioBasico usuario, HttpServletRequest request)
	{
		InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		HashMap mapAjustes=Utilidades.consultarEncabezadoAjuste(con, codigoAjuste);
		PdfReports report = new PdfReports();
		/**Cabezote principal**/
		report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), "RELACION DE AJUSTES CARTERA");
	    report.openReport(fileName);
	    
		report.document.addParagraph("Ajuste "+mapAjustes.get("nombretipoajuste")+" N� "+mapAjustes.get("consecutivoajuste")+"           Estado: "+mapAjustes.get("nombreestado")+"      "+(UtilidadTexto.getBoolean(mapAjustes.get("castigocartera")+"")?"Castigo":"")+(UtilidadTexto.getBoolean(mapAjustes.get("ajustereversado")+"")?"Reversado":""),report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
		report.document.addParagraph("Elaborado por: "+mapAjustes.get("usuarioelaboracion")+" El "+mapAjustes.get("fechaelaboracion"),report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
		report.document.addParagraph("Observaciones: "+mapAjustes.get("observaciones"),report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
		String fechaAprobacion="";
		if(mapAjustes.containsKey("fechaaprobacion"))
		{
			fechaAprobacion=mapAjustes.get("fechaaprobacion")+"";
		}
		String usuarioAprobacion="";
		if(mapAjustes.containsKey("usuarioaprobacion"))
		{
			usuarioAprobacion=mapAjustes.get("usuarioaprobacion")+"";
		}
		if(!fechaAprobacion.trim().equals("")&&!usuarioAprobacion.trim().equals(""))
			report.document.addParagraph("Aprobado por: "+usuarioAprobacion+" El: "+fechaAprobacion,report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
		
		String conv=mapAjustes.get("nombreconvenio")+"";
		report.document.addParagraph("Convenio: "+conv,report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);

		
		
		crearDetalleDelAjuste(con,codigoAjuste,report,mapAjustes.get("valorajuste")+"");
		
		
		report.document.addParagraph("___________________________________________",report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
		report.document.addParagraph("Elaborado.",report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
		report.document.addParagraph("___________________________________________",report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);
		report.document.addParagraph("Revisado. ",report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);

	    //cerrar documento
	    report.closeReport();
	}

	
	/**
	 * 
	 * @param con
	 * @param codigoAjuste
	 * @param report
	 */
	private static void crearDetalleDelAjuste(Connection con, String codigoAjuste, PdfReports report,String valorTotal)
	{
		HashMap detAjustes=Utilidades.consultarDetalleAjustesImpresion(con,codigoAjuste);
		
		
		
		
		report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
	    report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
	    report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000, 9);
	    report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 7);
	    
	    
	    
	    
	    report.createSection("ajustes",PdfReports.REPORT_PARENT_TABLE,1,5,10);
		report.setReportDataSectionAtributes(0xFFFFFF, 0xFFFFFF, 0x000000, 9);
		report.getSectionReference("ajustes").setTableCellsColSpan(1);
		report.font.useFont(iTextBaseFont.FONT_TIMES, true, false, false);

		report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, "Factura", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
		report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, "Solicitud", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
		report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, "Servicio/Articulo", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
		report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, "Vr. Ajuste", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
		report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, "Concepto", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);

		report.setReportDataSectionAtributes(0xFFFFFF, 0xFFFFFF, 0x000000, 8);
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);

		//Mt 6171 Filtro medicamento repetido
		HashMap<Integer, Integer>mapaUbicacionArticulos=new HashMap<Integer, Integer>();
		HashMap detAjusteOrganizado=new HashMap();
		
		int pos=0;
		int tempPos;
		for(int j=0;j<Integer.parseInt(detAjustes.get("numRegistros")+"");j++)
		{
			  String codi = ""+ detAjustes.get("codigo_art_"+j);		  
			  Integer codigoArt = 0;
			  if(!UtilidadTexto.isEmpty(codi)){
				  codigoArt = Integer.parseInt(codi);
		      } 
		      if(codigoArt>0&&mapaUbicacionArticulos.containsKey(codigoArt)){
	        		tempPos=mapaUbicacionArticulos.get(codigoArt);
	        		Double valorAjuste=0d;
	        		if(detAjusteOrganizado.get("valorajuste_"+tempPos)!=null){
	        			valorAjuste=Double.parseDouble(detAjusteOrganizado.get("valorajuste_"+tempPos).toString());
	        		}
	        		Double suma = Double.parseDouble(detAjustes.get("valorajuste_"+j).toString()); 
	        		detAjusteOrganizado.put("valorajuste_"+tempPos,valorAjuste+suma);
	        		
	            }else{
			       if(codigoArt!=null && codigoArt>0){
					  mapaUbicacionArticulos.put(codigoArt,pos);
					}
			       detAjusteOrganizado.put("factura_"+pos, detAjustes.get("factura_"+j));
			       detAjusteOrganizado.put("solicitud_"+pos, detAjustes.get("solicitud_"+j));
			       detAjusteOrganizado.put("artserv_"+pos, detAjustes.get("artserv_"+j));
			       detAjusteOrganizado.put("valorajuste_"+pos, detAjustes.get("valorajuste_"+j));
			       detAjusteOrganizado.put("concepto_"+pos, detAjustes.get("concepto_"+j));
			       detAjusteOrganizado.put("codigo_art_"+pos, detAjustes.get("codigo_art_"+j));
			       detAjusteOrganizado.put("codigo_ser_"+pos, detAjustes.get("codigo_ser_"+j));
			       
			       pos++;
			   }
		     
	   }  
		detAjusteOrganizado.put("numRegistros", pos);
		detAjustes=detAjusteOrganizado;
		pos=0;
		String servicio = "";
		for(int j=0;j<Integer.parseInt(detAjustes.get("numRegistros")+"");j++)
		{
			report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, Utilidades.obtenerConsecutivoFactura(Integer.parseInt(detAjustes.get("factura_"+j)+"")), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
			servicio =detAjustes.get("codigo_ser_"+j).toString();
			if(!servicio.isEmpty()){
			    report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, detAjustes.get("solicitud_"+pos)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
			}else{
				report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, "", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
			}
			report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, detAjustes.get("artserv_"+pos)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
			report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, UtilidadTexto.formatearValores(detAjustes.get("valorajuste_"+j)+" "), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
			report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, detAjustes.get("concepto_"+pos)+" ", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
			
			pos++;
		}
		report.getSectionReference("ajustes").setTableCellsColSpan(3);
		report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE," Valor Total ajuste ", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
		report.getSectionReference("ajustes").setTableCellsColSpan(1);
		report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, UtilidadTexto.formatearValores(valorTotal), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
		report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, " ", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);

		float widths[]={(float) 1,(float) 1,(float) 6,(float) 1,(float) 1};
		try
        {
		    report.getSectionReference("ajustes").getTableReference("parentTable").setWidths(widths);
        }
        catch (BadElementException e)
        {
            e.printStackTrace();
        }
		
		report.addSectionToDocument("ajustes");
	}
	

	
	/**
	 * 
	 * @param con
	 * @param codigoAjuste
	 * @param report
	 */
	private static void crearDetalleDelAjuste(Connection con, String codigoAjuste, PdfReports report,String valorTotal, HashMap mapAjusteArtSer)
	{
//		HashMap detAjustes=Utilidades.consultarDetalleAjustesImpresion(con,codigoAjuste);
		HashMap detAjustes = (HashMap) mapAjusteArtSer.get("detalleFactura_0");
	    int tempPos;
	    int pos=0;
		report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
	    report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
	    report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000, 9);
	    report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 7);
	    
	    
	    
	    
	    report.createSection("ajustes",PdfReports.REPORT_PARENT_TABLE,1,5,10);
		report.setReportDataSectionAtributes(0xFFFFFF, 0xFFFFFF, 0x000000, 9);
		report.getSectionReference("ajustes").setTableCellsColSpan(1);
		report.font.useFont(iTextBaseFont.FONT_TIMES, true, false, false);

		report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, "Factura", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
		report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, "Solicitud", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
		report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, "Servicio/Articulo", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
		report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, "Vr. Ajuste", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
		report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, "Concepto", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);

		report.setReportDataSectionAtributes(0xFFFFFF, 0xFFFFFF, 0x000000, 8);
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);

		for(int j=0;j<Integer.parseInt(detAjustes.get("numReg")+"");j++)
		{
			report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, Utilidades.obtenerConsecutivoFactura(Integer.parseInt(detAjustes.get("factura_"+pos)+"")), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
			detAjustes.get("esServicio_"+j);
			if(detAjustes.get("esServicio_"+j)!=null&&Boolean.parseBoolean((String) detAjustes.get("esServicio_"+j))){
				report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, detAjustes.get("solicitud_"+pos)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
			}else{
				report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, "", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
			}	
			report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, detAjustes.get("nombre_servicio_articulo_"+pos)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
		    report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, UtilidadTexto.formatearValores(detAjustes.get("valor_ajuste_"+pos)+" "), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
			report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, detAjustes.get("nombre_concepto_ajuste_"+pos)+" ", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
	        pos++;
	   }
		report.getSectionReference("ajustes").setTableCellsColSpan(3);
		report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE," Valor Total ajuste ", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
		report.getSectionReference("ajustes").setTableCellsColSpan(1);
		report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, UtilidadTexto.formatearValores(valorTotal), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
		report.getSectionReference("ajustes").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, " ", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);

		float widths[]={(float) 1,(float) 1,(float) 6,(float) 1,(float) 1};
		try
        {
		    report.getSectionReference("ajustes").getTableReference("parentTable").setWidths(widths);
        }
        catch (BadElementException e)
        {
            e.printStackTrace();
        }
		
		report.addSectionToDocument("ajustes");
	}
	

}
