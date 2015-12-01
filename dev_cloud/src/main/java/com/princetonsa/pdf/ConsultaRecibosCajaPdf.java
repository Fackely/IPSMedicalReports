/*
 * Created on 10/10/2005
 * 
 * @author <a href="mailto:artotor@hotmail.com">Jorge Armando Osorio Velásquez</a>
 * 
 * Copyright Princeton S.A. &copy;&reg; 2005. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 *
 */
package com.princetonsa.pdf;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import com.lowagie.text.BadElementException;
import com.princetonsa.actionform.tesoreria.ConsultaRecibosCajaForm;
import com.princetonsa.actionform.tesoreria.RecibosCajaForm;
import com.princetonsa.dto.administracion.DtoTiposMoneda;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.tesoreria.ConsultaRecibosCaja;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadN2T;
import util.UtilidadTexto;
import util.Utilidades;
import util.Administracion.UtilConversionMonedas;
import util.facturacion.UtilidadesFacturacion;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;

/**
 * @version 1.0, 10/10/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public class ConsultaRecibosCajaPdf
{

    /**
     * Clase para manejar los logs de esta clase
     */
    private static Logger logger=Logger.getLogger(ConsultaRecibosCajaPdf.class);
    
    
    /**
     * @param report
     * @param usuario
     */
    public static void generarEncabezadoReporte(PdfReports report,UsuarioBasico usuario,boolean ponerTitulo, String infoCentroAtencion, HttpServletRequest request)
    {
    	InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
        
		String titulo=ponerTitulo?"RECIBOS DE CAJA (" + UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() + ")":"";
		report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit()+""+infoCentroAtencion, titulo);
    }
    /**
     * @param string
     * @param forma
     * @param usuario
     */
    public static void imprimirListado(String filename, ConsultaRecibosCajaForm forma, UsuarioBasico usuario, HttpServletRequest request)
    {
        PdfReports report = new PdfReports();
        report.setFormatoImpresion("infoPiePaginaInicioMargen");
        HashMap recibosCaja=new HashMap();
        recibosCaja=forma.getRecibosCaja();
        generarEncabezadoReporte(report,usuario,true, "", request);
        
        report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
		
		report.openReport(filename);
		
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		      
		report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000, 14);
		report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 12);
		
		int tamanio=Integer.parseInt(recibosCaja.get("numeroregistros")+"");
		String[] cabeceras = {"Recibo No.","Fecha","Estado","Usuario","Caja","Valor"};
		String[] datos = new String[tamanio*cabeceras.length];
		int conColumnas=0;
		for(int j = 0 ; j < tamanio ; j ++)
		{
		    datos[conColumnas]=recibosCaja.get("consecutivorc_"+j)+"";conColumnas++;
		    datos[conColumnas]=recibosCaja.get("fecha_"+j)+"";conColumnas++;
		    datos[conColumnas]=recibosCaja.get("descripcionestado_"+j)+"";conColumnas++;
		    datos[conColumnas]=recibosCaja.get("usuario_"+j)+"";conColumnas++;
		    datos[conColumnas]=recibosCaja.get("descripcioncaja_"+j)+"";conColumnas++;
		    datos[conColumnas]=recibosCaja.get("valortotal_"+j)==null||(recibosCaja.get("valortotal_"+j)+"").equals("null")||(recibosCaja.get("valortotal_"+j)+"").equals("")?"0":recibosCaja.get("valortotal_"+j)+"";conColumnas++;
		}
		
		String nomCentroAtencion="";
		if(forma.getCodigoCentroAtencion()==-1)
		{
			nomCentroAtencion=" Todos ";
		}
		else
		{
			Connection con= UtilidadBD.abrirConexion();
			nomCentroAtencion=Utilidades.obtenerNombreCentroAtencion(con, forma.getCodigoCentroAtencion());
			UtilidadBD.closeConnection(con);
		}
		
		report.document.addParagraph("Fecha Inicial: "+forma.getFechaReciboCajaInicial()+" Fecha Final: "+forma.getFechaReciboCajaFina()+ " Centro Atención: "+nomCentroAtencion,20);
		if(!forma.getReciboCajaInicial().trim().equals("")&&!forma.getReciboCajaFinal().trim().equals(""))
		{
		    report.document.addParagraph("No Recibo Inicial: "+forma.getReciboCajaInicial()+" No Recibo Caja Final: "+forma.getReciboCajaFinal(),20);
		}
		if(!forma.getCodigoConceptoReciboCaja().trim().equals("")&&!forma.getCodigoConceptoReciboCaja().trim().equals("-1"))
		{

		    report.document.addParagraph("Concepto: "+forma.getDescripcionConcepto() ,20);
		}
		if(!forma.getDocSoporte().trim().equals(""))
		{
			report.document.addParagraph("Doc.Soporte: "+forma.getDocSoporte(),20);
		}
		if(!forma.getTipoIdBeneficiario().trim().equals(""))
		{
			report.document.addParagraph("Tipo Id Beneficiario: "+forma.getTipoIdBeneficiario() + " " +forma.getDescripcionTipoId(),20);
		}
		if(!forma.getNumeroIdBeneficiario().trim().equals(""))
		{
			report.document.addParagraph("Número Id Beneficiario: "+forma.getNumeroIdBeneficiario(),20);
		}
		if(forma.getEstadoReciboCaja()>0)
		{
		    report.document.addParagraph("Estado Recibo: "+forma.getDescripcionEstadoReciboCaja(),20);
		}
		if(!forma.getUsuarioElaboraReciboCaja().trim().equals("")&&!forma.getUsuarioElaboraReciboCaja().trim().equals("-1"))
		{
		    report.document.addParagraph("Usuario: "+forma.getNombreUsuario(),20);
		}
		if(forma.getCajaElaboraReciboCaja()>0)
		{
		    report.document.addParagraph("Caja: "+forma.getDescripcionCajaFiltro(),20);
		}
		
		if(forma.getCodigoFormaPago()>0)
		{
		    report.document.addParagraph("Forma de Pago: " + forma.getDescripcionFormaPago() ,20);
		}
		
	    if(forma.isManejaConversionMoneda())
	    {
	    	report.document.addParagraph(forma.getDescripcionConversion(),report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
	    }
	    double facConversion=1;
        if(forma.isManejaConversionMoneda())
		{
        	facConversion=forma.getFactorConversion();
		}

		report.createSection("seccionListado","recibos",1,6,10);
		report.font.setFontSizeAndAttributes(8,true,false,false);
		report.addSectionColHeaders("seccionListado","recibos", cabeceras, false);
		report.font.setFontSizeAndAttributes(8,false,false,false);
		for(int j = 0 ; j < tamanio ; j ++)
		{
			report.getSectionReference("seccionListado").addTableTextCellAligned("recibos", recibosCaja.get("consecutivorc_"+j)+"",report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
			report.getSectionReference("seccionListado").addTableTextCellAligned("recibos", recibosCaja.get("fecha_"+j)+"",report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
			report.getSectionReference("seccionListado").addTableTextCellAligned("recibos", recibosCaja.get("descripcionestado_"+j)+"",report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
			report.getSectionReference("seccionListado").addTableTextCellAligned("recibos", recibosCaja.get("usuario_"+j)+"",report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
			report.getSectionReference("seccionListado").addTableTextCellAligned("recibos", recibosCaja.get("descripcioncaja_"+j)+"",report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
			report.getSectionReference("seccionListado").addTableTextCellAligned("recibos", UtilidadTexto.formatearValores((Utilidades.convertirADouble(recibosCaja.get("valortotal_"+j)+"")/facConversion)+""),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
		}
		
		report.getSectionReference("seccionListado").setTableCellsColSpan(5);
		report.getSectionReference("seccionListado").addTableTextCellAligned("recibos", "Valor Total Recibos:",report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		report.getSectionReference("seccionListado").setTableCellsColSpan(1);
		report.getSectionReference("seccionListado").addTableTextCellAligned("recibos", UtilidadTexto.formatearValores((Utilidades.convertirADouble(recibosCaja.get("totalRC")+"")/facConversion)+""),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
		report.getSectionReference("seccionListado").setTableCellsColSpan(5);
		report.getSectionReference("seccionListado").addTableTextCellAligned("recibos", "Menos Recibos Anulados:",report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		report.getSectionReference("seccionListado").setTableCellsColSpan(1);
		report.getSectionReference("seccionListado").addTableTextCellAligned("recibos", UtilidadTexto.formatearValores((Utilidades.convertirADouble(recibosCaja.get("totalRCAnulados")+"")/facConversion)+""),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
		report.getSectionReference("seccionListado").setTableCellsColSpan(5);
		report.getSectionReference("seccionListado").addTableTextCellAligned("recibos", "Neto Valor Recibos:",report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		report.getSectionReference("seccionListado").setTableCellsColSpan(1);
		report.getSectionReference("seccionListado").addTableTextCellAligned("recibos", UtilidadTexto.formatearValores((Utilidades.convertirADouble(recibosCaja.get("totalRCMenosAnulados")+"")/facConversion)+""),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
		
		/*
		float widths[]={(float) 1,(float) 2,(float) 7,(float) 1};
		try
        {
		    report.getSectionReference("seccionListado").getTableReference("parentTable").setWidths(widths);
        }
        catch (BadElementException e)
        {
            logger.error("Se presentó error generando el pdf " + e);
        }
        */
		report.addSectionToDocument("seccionListado");
		report.closeReport(); 
    }
    
    /**
     * @param string
     * @param forma
     * @param nombreUsuario 
     * @param idUsuario 
     * @param usuarioBasicoSesion
     */
    public static void imprimirDetalle(String filename, ConsultaRecibosCajaForm forma, UsuarioBasico usuario, HttpServletRequest request, String nombreUsuario, String idUsuario)
    {
		PdfReports report = new PdfReports();
		report.setUsuario(usuario.getNombreUsuario());
        HashMap conceptos=new HashMap();
        conceptos=forma.getConceptosRC();
        double facConversion=1;
        DtoTiposMoneda tipoMoneda=UtilConversionMonedas.obtenerTipoMonedaManejaInstitucion(usuario.getCodigoInstitucionInt());
        if(forma.isManejaConversionMoneda())
		{
        	facConversion=forma.getFactorConversion();
        	tipoMoneda=new DtoTiposMoneda();
			int index=forma.getIndexTipoMoneda();
			String tipoMonedaStr="";
			if(index>ConstantesBD.codigoNuncaValido)
			{
				tipoMonedaStr=forma.getTiposMonedaTagMap("descripciontipomoneda_"+index)+"";
			}

        	tipoMoneda.setDescripcion(tipoMonedaStr);
		}
        
        String infoCentroAtencion= " \n Centro Atención: "+ConsultaRecibosCaja.getCentroAtencionReciboCaja(forma.getNumeroReciboCaja(), usuario.getCodigoInstitucionInt()).getNombre();
        
        generarEncabezadoReporte(report,usuario,false, infoCentroAtencion, request);
        
        report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
		
		report.openReport(filename);
		
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		      
		report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000, 14);
		report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 12);
		
		int tamanio=Integer.parseInt(conceptos.get("numeroregistros")+"");
		String[] cabeceras = {"Concepto","Doc","Beneficiario","Valor"};
		String[] datos = new String[tamanio*cabeceras.length];
		int conColumnas=0;
		for(int j = 0 ; j < tamanio ; j ++)
		{
		    datos[conColumnas]=conceptos.get("codigoconceptorc_"+j)+" - "+conceptos.get("descripcionconceptorc_"+j);conColumnas++;
		    if(Utilidades.convertirAEntero(conceptos.get("tipoconceptorc_0")+"")==ConstantesBD.codigoTipoIngresoTesoreriaPacientes)
		    {
		    	datos[conColumnas]=UtilidadesFacturacion.obtenerPrefijoFacturaXConsecutivo(conceptos.get("docsoporteconceptorc_"+j)+"",usuario.getCodigoInstitucionInt())+" "+conceptos.get("docsoporteconceptorc_"+j)+"" ;conColumnas++;
		    }
		    else
		    {
		    	datos[conColumnas]=conceptos.get("docsoporteconceptorc_"+j)+"";conColumnas++;
		    }
		    datos[conColumnas]=conceptos.get("nombrebeneficiario_"+j)+"";conColumnas++;
		    datos[conColumnas]=UtilidadTexto.formatearValores(conceptos.get("valorconceptorc_"+j)==null||(conceptos.get("valorconceptorc_"+j)+"").equals("null")||(conceptos.get("valorconceptorc_"+j)+"").equals("")?"0":(Utilidades.convertirADouble(conceptos.get("valorconceptorc_"+j)+"")/facConversion)+"");conColumnas++;
		}
		report.document.addParagraph("RECIBO DE CAJA No: "+forma.getNumeroReciboCaja()+"                    Estado: "+forma.getDescripcionEstadoReciboCaja()+"              Fecha de Emisión: "+forma.getFechaReciboCaja(),20);
	    report.document.addParagraph("Recibido de: "+forma.getRecibidoDe(),15);
	    report.document.addParagraph("Caja: "+forma.getDescripcionCaja() +"                     Cajero: "+forma.getUsuarioElaboraReciboCaja(),15);
	    if(forma.isManejaConversionMoneda())
	    {
	    	report.document.addParagraph(forma.getDescripcionConversion(),report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
	    }
		report.createColSection("seccionConceptos",  cabeceras, datos, 10);
		float widths[]={(float) 35,(float) 15,(float) 35,(float) 15};
		try
        {
		    report.getSectionReference("seccionConceptos").getTableReference("parentTable").setWidths(widths);
        }
        catch (BadElementException e)
        {
            logger.error("Se presentó error generando el pdf " + e);
        }
		report.getSectionReference("seccionConceptos").getTableReference("parentTable").setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		report.addSectionToDocument("seccionConceptos");
		report.document.addParagraph("Total...................................... "+UtilidadTexto.formatearValores(Utilidades.convertirADouble(forma.getValorTotalReciboCaja())/facConversion),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,15);
		report.document.addParagraph("Son:   "+UtilidadN2T.convertirLetras((Utilidades.convertirADouble(forma.getValorTotalReciboCaja())/facConversion)+"",ConstantesBD.cadenaUnidades,ConstantesBD.cadenaUnidadesDecimales+" MCTE"), 22);
		

		
		for(int cont=0;cont<Integer.parseInt(forma.getTotalPagos().get("numRegistros").toString());cont++)
		{
			if(atributoMapaValido(forma.getTotalPagos().get("descripcion_"+cont))&&atributoMapaValido(forma.getTotalPagos().get("valor_"+cont)))
			{
				report.document.addParagraph(forma.getTotalPagos().get("descripcion_"+cont)+"             "+UtilidadTexto.formatearValores(Utilidades.convertirADouble(forma.getTotalPagos().get("valor_"+cont)+"")/facConversion),15);
			}	
		}
		/*if(atributoMapaValido(forma.getTotalPagos().get("totalefectivodescripcion"))&&atributoMapaValido(forma.getTotalPagos().get("totalefectivovalor")))
			report.document.addParagraph(forma.getTotalPagos().get("totalefectivodescripcion")+"             "+UtilidadTexto.formatearValores(forma.getTotalPagos().get("totalefectivovalor")+""),25);
		for(int cont=0;cont<Integer.parseInt(forma.getTotalPagos().get("numPagosCheque").toString());cont++)
		{
			if(atributoMapaValido(forma.getTotalPagos().get("totalchequedescripcion"))&&atributoMapaValido(forma.getTotalPagos().get("totalchequevalor")))
				report.document.addParagraph(forma.getTotalPagos().get("totalchequedescripcion")+"             "+UtilidadTexto.formatearValores(forma.getTotalPagos().get("totalchequevalor")+""),15);
		}
		for(int cont=0;cont<Integer.parseInt(forma.getTotalPagos().get("numPagosTarjetasCredito").toString());cont++)
		{
			if(atributoMapaValido(forma.getTotalPagos().get("totaltarjetacreditodescripcion"))&&atributoMapaValido(forma.getTotalPagos().get("totaltarjetacreditovalor")))
				report.document.addParagraph(forma.getTotalPagos().get("totaltarjetacreditodescripcion")+"             "+UtilidadTexto.formatearValores(forma.getTotalPagos().get("totaltarjetacreditovalor")+""),15);
		}
		for(int cont=0;cont<Integer.parseInt(forma.getTotalPagos().get("numPagosTarjetasDebito").toString());cont++)
		{
			if(atributoMapaValido(forma.getTotalPagos().get("totaltarjetadebitodescripcion"))&&atributoMapaValido(forma.getTotalPagos().get("totaltarjetadebitovalor")))
				report.document.addParagraph(forma.getTotalPagos().get("totaltarjetadebitodescripcion")+"             "+UtilidadTexto.formatearValores(forma.getTotalPagos().get("totaltarjetadebitovalor")+""),15);
		}*/

		logger.info("========>Observaciones PDF: "+forma.getObservaciones());
		if(UtilidadCadena.noEsVacio(forma.getObservaciones()))
		{
			report.document.addParagraph("OBSERVACIONES: ", 40);
			report.document.addParagraph(forma.getObservaciones(), 20);
		}
		
		report.document.addParagraph("______________________________________________",40);
		report.document.addParagraph("                           Elaborado",15);
		report.closeReport(); 
    }
    
    private static boolean atributoMapaValido(Object valor) 
    {
    	if(valor!=null&&!valor.equals("null")&&!valor.toString().trim().equals(""))
    		return true;
    	return false;
	}
	/**
     * 
     * @param filename
     * @param forma
     * @param usuario
	 * @param nombreUsuario 
	 * @param idUsuario 
     */
	public static void imprimirDetalle(String filename, RecibosCajaForm forma, UsuarioBasico usuario, HttpServletRequest request, String nombreUsuario, String idUsuario)
	{
		PdfReports report = new PdfReports();
		report.setUsuario(usuario.getNombreUsuario());
        HashMap conceptos=new HashMap();
        conceptos=forma.getMapaConceptos();
        String infoCentroAtencion= " \n Centro Atención: "+ConsultaRecibosCaja.getCentroAtencionReciboCaja(forma.getNumReciboCaja(), usuario.getCodigoInstitucionInt()).getNombre();
        generarEncabezadoReporte(report,usuario,false, infoCentroAtencion, request);
        
        report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
		
		report.openReport(filename);
		
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		      
		report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000, 14);
		report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 12);
		
		int tamanio=Integer.parseInt(conceptos.get("numeroregistros")+"");
		String[] cabeceras = {"Concepto","Doc","Beneficiario","Valor"};
		String[] datos = new String[tamanio*cabeceras.length];
		int conColumnas=0;
		for(int j = 0 ; j < tamanio ; j ++)
		{
		    datos[conColumnas]=conceptos.get("codigoconceptorc_"+j)+" - "+conceptos.get("descripcionconceptorc_"+j);conColumnas++;
		    if(Utilidades.convertirAEntero(conceptos.get("tipoconceptorc_0")+"")==ConstantesBD.codigoTipoIngresoTesoreriaPacientes)
		    {
		    	datos[conColumnas]=UtilidadesFacturacion.obtenerPrefijoFacturaXConsecutivo(conceptos.get("docsoporteconceptorc_"+j)+"",usuario.getCodigoInstitucionInt())+" "+conceptos.get("docsoporteconceptorc_"+j)+"" ;conColumnas++;
		    }
		    else
		    {
		    	datos[conColumnas]=conceptos.get("docsoporteconceptorc_"+j)+"";conColumnas++;
		    }
		    datos[conColumnas]=conceptos.get("nombrebeneficiario_"+j)+"";conColumnas++;
		    datos[conColumnas]=conceptos.get("valorconceptorc_"+j)==null||(conceptos.get("valorconceptorc_"+j)+"").equals("null")||(conceptos.get("valorconceptorc_"+j)+"").equals("")?"0":conceptos.get("valorconceptorc_"+j)+"";conColumnas++;
		}
		report.document.addParagraph("RECIBO DE CAJA No: "+forma.getNumReciboCaja()+"                    Estado: "+(Utilidades.obtenerEstadoReciboCaja(forma.getNumReciboCaja(),usuario.getCodigoInstitucionInt())).split(ConstantesBD.separadorSplit)[1]+"              Fecha de Emisión: "+UtilidadFecha.getFechaActual(),20);
	    report.document.addParagraph("Recibido de: "+Utilidades.reciboCajaRecibidoDe(usuario.getCodigoInstitucionInt(),forma.getNumReciboCaja()),15);
	    report.document.addParagraph("Caja: "+usuario.getDescripcionCaja() +"                     Cajero: "+usuario.getNombreUsuario(),15);
		report.createColSection("seccionConceptos",  cabeceras, datos, 10);
		float widths[]={(float) 35,(float) 15,(float) 35,(float) 15};
		try
        {
		    report.getSectionReference("seccionConceptos").getTableReference("parentTable").setWidths(widths);
        }
        catch (BadElementException e)
        {
            logger.error("Se presentó error generando el pdf " + e);
        }
		report.getSectionReference("seccionConceptos").getTableReference("parentTable").setAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
		report.addSectionToDocument("seccionConceptos");
		double valorTotalRecibo=Utilidades.obtenerValorTotalReciboCaja(usuario.getCodigoInstitucionInt(),forma.getNumReciboCaja());
		report.document.addParagraph("Total...................................... "+UtilidadTexto.formatearValores(valorTotalRecibo),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,15);
		DtoTiposMoneda tipoMoneda=UtilConversionMonedas.obtenerTipoMonedaManejaInstitucion(usuario.getCodigoInstitucionInt());
		report.document.addParagraph("Son:   "+UtilidadN2T.convertirLetras(UtilidadTexto.formatearValores(valorTotalRecibo+"" ).replaceAll(",", ""),tipoMoneda.getDescripcion(),""), 22);

		if(forma.isManejaConversionMoneda())
		{
			int index=forma.getIndexConversionOriginal();
			if(index>ConstantesBD.codigoNuncaValido)
			{
				report.document.addParagraph("Total Convertido.......(Moneda: "+forma.getTiposMonedaTagMap("descripciontipomoneda_"+index)+" "+forma.getTiposMonedaTagMap("simbolotipomoneda_"+index)+" Factor Conversion:"+UtilidadTexto.formatearValores(forma.getTiposMonedaTagMap("factorconversion_"+index)+"")+")............................... "+UtilidadTexto.formatearValores(valorTotalRecibo/Utilidades.convertirADouble(forma.getTiposMonedaTagMap("factorconversion_"+index)+"")),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,15);
			}
		}
				
		for(int cont=0;cont<Integer.parseInt(forma.getMapaTotalPagos().get("numRegistros").toString()); cont++)
		{
			if(atributoMapaValido(forma.getMapaTotalPagos().get("descripcion_"+cont))&&atributoMapaValido(forma.getMapaTotalPagos().get("valor_"+cont)))
			{
				report.document.addParagraph(forma.getMapaTotalPagos().get("descripcion_"+cont)+"             "+UtilidadTexto.formatearValores(forma.getMapaTotalPagos().get("valor_"+cont)+""),15);
			}	
		}
		/*if(forma.getMapaTotalPagos().get("totalefectivodescripcion")!=null&&!forma.getMapaTotalPagos().get("totalefectivodescripcion").equals("null"))
			report.document.addParagraph(forma.getMapaTotalPagos().get("totalefectivodescripcion")+"             "+UtilidadTexto.formatearValores(forma.getMapaTotalPagos().get("totalefectivovalor")==null||(forma.getMapaTotalPagos().get("totalefectivovalor")+"").trim().equals("")?"0":forma.getMapaTotalPagos().get("totalefectivovalor")+""),25);
		for(int cont=0;cont<Integer.parseInt(forma.getMapaTotalPagos().get("numPagosCheque").toString());cont++)
		{
			if(forma.getMapaTotalPagos().get("totalchequedescripcion_"+cont)!=null&&!forma.getMapaTotalPagos().get("totalchequedescripcion_"+cont).equals("null"))
				report.document.addParagraph(forma.getMapaTotalPagos().get("totalchequedescripcion_"+cont)+"             "+UtilidadTexto.formatearValores(forma.getMapaTotalPagos().get("totalchequevalor_"+cont)==null||(forma.getMapaTotalPagos().get("totalchequevalor_"+cont)+"").trim().equals("")?"0":forma.getMapaTotalPagos().get("totalchequevalor_"+cont)+""),15);
		}
		for(int cont=0;cont<Integer.parseInt(forma.getMapaTotalPagos().get("numPagosTarjetasCredito").toString());cont++)
		{
			if(forma.getMapaTotalPagos().get("totaltarjetacreditodescripcion_"+cont)!=null&&!forma.getMapaTotalPagos().get("totaltarjetacreditodescripcion_"+cont).equals("null"))
				report.document.addParagraph(forma.getMapaTotalPagos().get("totaltarjetacreditodescripcion_"+cont)+"             "+UtilidadTexto.formatearValores(forma.getMapaTotalPagos().get("totaltarjetacreditovalor_"+cont)==null||(forma.getMapaTotalPagos().get("totaltarjetacreditovalor_"+cont)+"").trim().equals("")?"0":forma.getMapaTotalPagos().get("totaltarjetacreditovalor_"+cont)+""),15);
		}
		for(int cont=0;cont<Integer.parseInt(forma.getMapaTotalPagos().get("numPagosTarjetasDebito").toString());cont++)
		{
			if(forma.getMapaTotalPagos().get("totaltarjetadebitodescripcion_"+cont)!=null&&!forma.getMapaTotalPagos().get("totaltarjetadebitodescripcion_"+cont).equals("null"))
				report.document.addParagraph(forma.getMapaTotalPagos().get("totaltarjetadebitodescripcion_"+cont)+"             "+UtilidadTexto.formatearValores(forma.getMapaTotalPagos().get("totaltarjetadebitovalor_"+cont)==null||(forma.getMapaTotalPagos().get("totaltarjetadebitovalor_"+cont)+"").trim().equals("")?"0":forma.getMapaTotalPagos().get("totaltarjetadebitovalor_"+cont)+""),15);
		}*/

		logger.info("========>Observaciones PDF: "+forma.getObservacionesImprimir());
		if(UtilidadCadena.noEsVacio(forma.getObservacionesImprimir()))
		{
			report.document.addParagraph("OBSERVACIONES: ", 40);
			report.document.addParagraph(forma.getObservacionesImprimir(), 20);
		}
		
		report.document.addParagraph("______________________________________________",40);
		report.document.addParagraph("                           Elaborado",15);
		report.closeReport(); 
	}
    
}
