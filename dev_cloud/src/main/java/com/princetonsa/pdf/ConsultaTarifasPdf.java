package com.princetonsa.pdf;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.facturacion.InfoTarifa;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;
import util.pdf.iTextBaseTable;

//import com.lowagie.text.BadElementException;

import com.princetonsa.actionform.facturacion.ConsultaTarifasForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;


public class ConsultaTarifasPdf 
{
	public static Logger logger=Logger.getLogger(ConsultaTarifasPdf.class);
	
	/**
     * M�todo para realizar la impresi�n de la liquidacion de servicios
     * @param filename
     * @param forma
     * @param usuario
     */
    public static void pdfConsultaTarifas(String filename, ConsultaTarifasForm forma, HashMap mapa, UsuarioBasico usuario, HttpServletRequest request)
    {
    	InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
    	
    	//*************IMPRESION DEL ENCABEZADO************************************************
    	PdfReports report = new PdfReports();
    	String tituloConsulta="CONSULTA CAMBIOS TARIFAS" ;
		report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), tituloConsulta);
		
        //Se abre el reporte
        report.openReport(filename);
        report.font.useFont(iTextBaseFont.FONT_TIMES, true, true, true);
        iTextBaseTable section;
        
        //**************SECCION ENCABEZADO*********************************************************************
        section = report.createSection("encabezado", "encabezadoTable", 3, 10, 10);
  		section.setTableBorder("encabezadoTable", 0x000000, 0.5f);
	    section.setTableCellBorderWidth("encabezadoTable", 0.5f);
	    section.setTableCellPadding("encabezadoTable", 4);
	    section.setTableSpaceBetweenCells("encabezadoTable", 0.1f);
	    section.setTableCellsDefaultColors("encabezadoTable", 0x006898, 0x000000);
	    
	    //Titulo del reporte
	    section.setTableCellsColSpan(10);
	    report.font.setFontAttributes(0xFFFFFF, 8, true, false, false);
	    section.addTableTextCellAligned("encabezadoTable", "DETALLE ART�CULO", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellsDefaultColors("encabezadoTable", 0xFFFFFF, 0x000000);
	    
	    //Convenio
	    String nombreEsqTar= forma.getNombreEsquemaTarifario();
        logger.info("===> El nombre del esquema tarifario es: "+nombreEsqTar);
        
	    section.setTableCellsColSpan(10);
	    report.font.setFontAttributes(0x000000, 8, true, false, false);
	    section.addTableTextCellAligned("encabezadoTable", nombreEsqTar, report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellsDefaultColors("encabezadoTable", 0xFFFFFF, 0x000000);
	    
	    //Tipo redondeo art�culos	    
	    section.setTableCellsColSpan(2);
	    report.font.setFontAttributes(0x000000, 8, true, false, false);
	    section.addTableTextCellAligned("encabezadoTable", "Tipo Redondeo: "+mapa.get("tiporedondeo")+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellsDefaultColors("encabezadoTable", 0xFFFFFF, 0x000000);
	    
	    //Art�culo
	    logger.info("===> Art�culo: "+mapa.get("articulo")+"");
	    section.setTableCellsColSpan(8);
	    report.font.setFontAttributes(0x000000, 8, true, false, false);
	    section.addTableTextCellAligned("encabezadoTable", "Art�culo: "+mapa.get("articulo")+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellsDefaultColors("encabezadoTable", 0xFFFFFF, 0x000000);
	    //se a�ade la secci�n al documento
	    report.addSectionToDocument("encabezado");
	    
	    //**************SECCION DATOS TABLA*********************************************************************
        section = report.createSection("datosArticulos", "articulosTable", 10, 10, 10);
  		section.setTableBorder("articulosTable", 0x000000, 0.5f);
	    section.setTableCellBorderWidth("articulosTable", 0.5f);
	    section.setTableCellPadding("articulosTable", 4);
	    section.setTableSpaceBetweenCells("articulosTable", 0.1f);
	    section.setTableCellsDefaultColors("articulosTable", 0xFFFFFF, 0x000000);
	    
	    int contVI=Utilidades.convertirAEntero(mapa.get("numRegistrosViasIngreso")+"");
		int contTC=Utilidades.convertirAEntero(mapa.get("numRegistrosTipoComplejidad")+"");
		
		for(int vi=0;vi<contVI;vi++)
		{
			int viaIngreso=Utilidades.convertirAEntero(mapa.get("viaIngreso_"+vi)+"");
			String tipoPaciente=mapa.get("tipoPaciente_"+vi)+"";
			
			for(int tc=0;tc<contTC;tc++)
			{
				int tipoComplejidad=Utilidades.convertirAEntero(mapa.get("codigoTipoComplejidad_"+tc)+"");
				
			    if(tipoComplejidad>0)
				{
			    	//V�a Ingreso
					logger.info("===> V�a de Ingreso: "+mapa.get("nombreViaIngreso_"+vi)+"");
				    section.setTableCellsColSpan(3);
				    report.font.setFontAttributes(0x000000, 8, true, false, false);
				    section.addTableTextCellAligned("articulosTable", 
				    		"Via Ingreso: "+mapa.get("nombreViaIngreso_"+vi)+"", 
				    		report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
				    report.font.setFontAttributes(0x000000, 8, false, false, false);
				    section.setTableCellsDefaultColors("articulosTable", 0xFFFFFF, 0x000000);
				    
				    //Tipo Paciente
					logger.info("===> Tipo Paciente: "+mapa.get("nombreTipoPaciente_"+vi)+"");
				    section.setTableCellsColSpan(4);
				    report.font.setFontAttributes(0x000000, 8, true, false, false);
				    section.addTableTextCellAligned("articulosTable", 
				    		"Tipo Paciente: "+mapa.get("nombreTipoPaciente_"+vi)+"", 
				    		report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
				    report.font.setFontAttributes(0x000000, 8, false, false, false);
				    section.setTableCellsDefaultColors("articulosTable", 0xFFFFFF, 0x000000);
				    
				    //Tipo Complejidad
			    	logger.info("===> Si tipo complejidad > 0 ");
			    	logger.info("===> Tipo Complejidad: "+mapa.get("descripcionTipoComplejidad_"+tc)+"");
				    section.setTableCellsColSpan(3);
				    report.font.setFontAttributes(0x000000, 8, true, false, false);
				    section.addTableTextCellAligned("articulosTable", 
				    		"Tipo Complejidad: "+mapa.get("descripcionTipoComplejidad_"+tc)+"", 
				    		report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
				    report.font.setFontAttributes(0x000000, 8, false, false, false);
				    section.setTableCellsDefaultColors("articulosTable", 0xFFFFFF, 0x000000);
				}
			    else
			    {
			    	//V�a Ingreso
					logger.info("===> V�a de Ingreso: "+mapa.get("nombreViaIngreso_"+vi)+"");
				    section.setTableCellsColSpan(5);
				    report.font.setFontAttributes(0x000000, 8, true, false, false);
				    section.addTableTextCellAligned("articulosTable", 
				    		"Via Ingreso: "+mapa.get("nombreViaIngreso_"+vi)+"", 
				    		report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
				    report.font.setFontAttributes(0x000000, 8, false, false, false);
				    section.setTableCellsDefaultColors("articulosTable", 0xFFFFFF, 0x000000);
				    
				    //Tipo Paciente
					logger.info("===> Tipo Paciente: "+mapa.get("nombreTipoPaciente_"+vi)+"");
				    section.setTableCellsColSpan(5);
				    report.font.setFontAttributes(0x000000, 8, true, false, false);
				    section.addTableTextCellAligned("articulosTable", 
				    		"Tipo Paciente: "+mapa.get("nombreTipoPaciente_"+vi)+"", 
				    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    report.font.setFontAttributes(0x000000, 8, false, false, false);
				    section.setTableCellsDefaultColors("articulosTable", 0xFFFFFF, 0x000000);
			    }
			    
			    section.setTableCellsColSpan(1);
			    
			    //Fecha Asignaci�n
			    logger.info("===> Fecha Asignaci�n");
			    section.setTableCellsRowSpan(2);
			    report.font.setFontAttributes(0x000000, 8, true, false, false);
			    section.addTableTextCellAligned("articulosTable", 
			    		"Fecha Asignaci�n", 
			    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    report.font.setFontAttributes(0x000000, 8, false, false, false);
			    section.setTableCellsDefaultColors("articulosTable", 0xFFFFFF, 0x000000);
			    
			    //Tarifa Base
			    logger.info("===> Tarifa Base");
			    section.setTableCellsRowSpan(2);
			    report.font.setFontAttributes(0x000000, 8, true, false, false);
			    section.addTableTextCellAligned("articulosTable", 
			    		"Tarifa Base", 
			    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    report.font.setFontAttributes(0x000000, 8, false, false, false);
			    section.setTableCellsDefaultColors("articulosTable", 0xFFFFFF, 0x000000);
			    
			    //Excepciones
			    logger.info("===> Excepciones");
			    section.setTableCellsRowSpan(1);
			    section.setTableCellsColSpan(3);
			    report.font.setFontAttributes(0x000000, 8, true, false, false);
			    section.addTableTextCellAligned("articulosTable", 
			    		"Excepciones", 
			    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    report.font.setFontAttributes(0x000000, 8, false, false, false);
			    section.setTableCellsDefaultColors("articulosTable", 0xFFFFFF, 0x000000);
			    
			    //Descuentos
			    logger.info("===> Descuentos");
			    section.setTableCellsColSpan(2);
			    report.font.setFontAttributes(0x000000, 8, true, false, false);
			    section.addTableTextCellAligned("articulosTable", 
			    		"Descuentos", 
			    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    report.font.setFontAttributes(0x000000, 8, false, false, false);
			    section.setTableCellsDefaultColors("articulosTable", 0xFFFFFF, 0x000000);
			    
			    section.setTableCellsColSpan(1);
			    
			    //Tarifa Final
			    logger.info("===> Tarifa Final");
			    section.setTableCellsRowSpan(2);
			    report.font.setFontAttributes(0x000000, 8, true, false, false);
			    section.addTableTextCellAligned("articulosTable", 
			    		"Tarifa Final", 
			    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    report.font.setFontAttributes(0x000000, 8, false, false, false);
			    section.setTableCellsDefaultColors("articulosTable", 0xFFFFFF, 0x000000);
			    
			    //Usuario Asigna
			    logger.info("===> Usuario Asigna");
			    section.setTableCellsRowSpan(2);
			    report.font.setFontAttributes(0x000000, 8, true, false, false);
			    section.addTableTextCellAligned("articulosTable", 
			    		"Usuario Asigna", 
			    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    report.font.setFontAttributes(0x000000, 8, false, false, false);
			    section.setTableCellsDefaultColors("articulosTable", 0xFFFFFF, 0x000000);
			    
			    //Tipo Cambio
			    logger.info("===> Tipo Cambio");
			    section.setTableCellsRowSpan(2);
			    report.font.setFontAttributes(0x000000, 8, true, false, false);
			    section.addTableTextCellAligned("articulosTable", 
			    		"Tipo Cambio", 
			    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    report.font.setFontAttributes(0x000000, 8, false, false, false);
			    section.setTableCellsDefaultColors("articulosTable", 0xFFFFFF, 0x000000);
			    
			    //%
			    logger.info("===> %");
			    section.setTableCellsRowSpan(1);
			    report.font.setFontAttributes(0x000000, 8, true, false, false);
			    section.addTableTextCellAligned("articulosTable", 
			    		"%", 
			    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    report.font.setFontAttributes(0x000000, 8, false, false, false);
			    section.setTableCellsDefaultColors("articulosTable", 0xFFFFFF, 0x000000);
			    
			    //Vr. Ajuste
			    logger.info("===> Vr. Ajuste");
			    report.font.setFontAttributes(0x000000, 8, true, false, false);
			    section.addTableTextCellAligned("articulosTable", 
			    		"Vr. Ajuste", 
			    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    report.font.setFontAttributes(0x000000, 8, false, false, false);
			    section.setTableCellsDefaultColors("articulosTable", 0xFFFFFF, 0x000000);
			    
			    //Nuev Tar.
			    logger.info("===> Nuev Tar.");
			    report.font.setFontAttributes(0x000000, 8, true, false, false);
			    section.addTableTextCellAligned("articulosTable", 
			    		"Nuev Tar.", 
			    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    report.font.setFontAttributes(0x000000, 8, false, false, false);
			    section.setTableCellsDefaultColors("articulosTable", 0xFFFFFF, 0x000000);
			    
			    //%
			    logger.info("===> %");
			    report.font.setFontAttributes(0x000000, 8, true, false, false);
			    section.addTableTextCellAligned("articulosTable", 
			    		"%", 
			    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    report.font.setFontAttributes(0x000000, 8, false, false, false);
			    section.setTableCellsDefaultColors("articulosTable", 0xFFFFFF, 0x000000);
			    
			    //Valor
			    logger.info("===> Valor");
			    report.font.setFontAttributes(0x000000, 8, true, false, false);
			    section.addTableTextCellAligned("articulosTable", 
			    		"Valor", 
			    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    report.font.setFontAttributes(0x000000, 8, false, false, false);
			    section.setTableCellsDefaultColors("articulosTable", 0xFFFFFF, 0x000000);

			    int tamanio=Utilidades.convertirAEntero(mapa.get("numRegistros_"+tipoComplejidad+"_"+viaIngreso+"_"+tipoPaciente)+"");
			    
				for(int i=0;i<tamanio;i++)
				{
					String indice=tipoComplejidad+"_"+viaIngreso+"_"+tipoPaciente+"_"+i;
					
					//Fecha Asignaci�n
				    logger.info("===> Fecha Asignaci�n: "+UtilidadFecha.conversionFormatoFechaAAp(mapa.get("fechaasignacion_"+indice)+"")+"");
				    report.font.setFontAttributes(0x000000, 8, true, false, false);
				    section.addTableTextCellAligned("articulosTable", 
				    		UtilidadFecha.conversionFormatoFechaAAp(mapa.get("fechaasignacion_"+indice)+"")+"", 
				    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    report.font.setFontAttributes(0x000000, 8, false, false, false);
				    section.setTableCellsDefaultColors("articulosTable", 0xFFFFFF, 0x000000);
				    
				    //Tarifa Base
				    double valorTarifa=Utilidades.convertirADouble(mapa.get("valortarifa_"+indice)+"");
				    report.font.setFontAttributes(0x000000, 8, true, false, false);
				    
					if(valorTarifa>0)
					{
						logger.info("===> Tarifa Base: "+UtilidadTexto.formatearValores(valorTarifa));
						section.addTableTextCellAligned("articulosTable", 
								UtilidadTexto.formatearValores(valorTarifa), 
					    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
					}
					else
					{
						logger.info("===> Tarifa Base: Sin Tarifa");
						section.addTableTextCellAligned("articulosTable", 
								"Sin Tarifa", 
					    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
					}
					
					report.font.setFontAttributes(0x000000, 8, false, false, false);
				    section.setTableCellsDefaultColors("articulosTable", 0xFFFFFF, 0x000000);
				    InfoTarifa excepcion=(InfoTarifa)mapa.get("excepciones_"+indice);
				    
				    //Porcentaje Excepciones
				    String porcentajes="";
				    
					for(int k=0;k<(excepcion.getPorcentajes()).size();k++)
					{
						if(k>0)
							porcentajes=porcentajes+" \n ";
						porcentajes=porcentajes+(excepcion.getPorcentajes()).get(k)+"";
					}
					
					logger.info("===> Porcentaje Excepciones: "+porcentajes);
					section.addTableTextCellAligned("articulosTable", 
							porcentajes, 
				    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
					
					//Valor Ajuste
				    logger.info("===> Valor Ajuste: "+excepcion.getValor());
				    report.font.setFontAttributes(0x000000, 8, true, false, false);
				    section.addTableTextCellAligned("articulosTable", 
				    		excepcion.getValor()+"", 
				    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    report.font.setFontAttributes(0x000000, 8, false, false, false);
				    section.setTableCellsDefaultColors("articulosTable", 0xFFFFFF, 0x000000);
				    
				    //Nueva Tarifa
				    logger.info("===> Nueva Tarifa: "+excepcion.getNuevaTarifa()+"");
				    report.font.setFontAttributes(0x000000, 8, true, false, false);
				    section.addTableTextCellAligned("articulosTable", 
				    		excepcion.getNuevaTarifa()+"", 
				    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    report.font.setFontAttributes(0x000000, 8, false, false, false);
				    section.setTableCellsDefaultColors("articulosTable", 0xFFFFFF, 0x000000);
				    
				    InfoTarifa descuentos=(InfoTarifa)mapa.get("descuentos_"+indice);
				    
				    //Porcentaje Descuentos
				    report.font.setFontAttributes(0x000000, 8, true, false, false);
				   
				    if(descuentos.getPorcentajes().size()==0)
				    {
				    	logger.info("======================================");
				    	logger.info("===> El vector viene vac�o");
				    }
				    
				    if(descuentos.getPorcentajes().size()>0)
					{
				    	logger.info("===> Porcentaje Descuentos: "+descuentos.getPorcentajes().get(0));
				    	section.addTableTextCellAligned("articulosTable", 
				    			(descuentos.getPorcentajes().get(0))+"", 
					    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
					}
				    else
				    {
				    	logger.info("===> Porcentaje Descuentos: El vector viene vac�o");
				    	section.addTableTextCellAligned("articulosTable", 
				    			"", 
					    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    }
				    
				    report.font.setFontAttributes(0x000000, 8, false, false, false);
				    section.setTableCellsDefaultColors("articulosTable", 0xFFFFFF, 0x000000);
				    
				    //Valor Descuentos
				    logger.info("===> Valor Descuentos: "+descuentos.getValor()+"");
				    report.font.setFontAttributes(0x000000, 8, true, false, false);
				    section.addTableTextCellAligned("articulosTable", 
				    			descuentos.getValor()+"", 
					    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    report.font.setFontAttributes(0x000000, 8, false, false, false);
				    section.setTableCellsDefaultColors("articulosTable", 0xFFFFFF, 0x000000);
				    
				    //Tarifa Final
				    double valorTarifaFinal=Utilidades.convertirADouble(mapa.get("tarifafinal_"+indice)+"");
				    report.font.setFontAttributes(0x000000, 8, true, false, false);
				    if(valorTarifa>0)
				    {
				    	logger.info("===> Tarifa Final: "+UtilidadTexto.formatearValores(valorTarifaFinal)+"");
				    	section.addTableTextCellAligned("articulosTable", 
				    			UtilidadTexto.formatearValores(valorTarifaFinal)+"", 
					    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    }
					else
					{
						logger.info("===> Tarifa Final: Sin Tarifa");
						section.addTableTextCellAligned("articulosTable", 
				    			"Sin Tarifa", 
					    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
					}
				    
				    report.font.setFontAttributes(0x000000, 8, false, false, false);
				    section.setTableCellsDefaultColors("articulosTable", 0xFFFFFF, 0x000000);
				    
				    //Usuario Asigna
				    logger.info("===> Usuario Asigna: "+mapa.get("usuarioasigna_"+indice)+"");
				    report.font.setFontAttributes(0x000000, 8, true, false, false);
				    section.addTableTextCellAligned("articulosTable", 
				    			mapa.get("usuarioasigna_"+indice)+"", 
					    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    report.font.setFontAttributes(0x000000, 8, false, false, false);
				    section.setTableCellsDefaultColors("articulosTable", 0xFFFFFF, 0x000000);
				    
				    //Tipo Cambio
				    logger.info("===> Usuario Asigna: "+mapa.get("tipocambio_"+indice)+"");
				    report.font.setFontAttributes(0x000000, 8, true, false, false);
				    section.addTableTextCellAligned("articulosTable", 
				    			mapa.get("tipocambio_"+indice)+"", 
					    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    report.font.setFontAttributes(0x000000, 8, false, false, false);
				    section.setTableCellsDefaultColors("articulosTable", 0xFFFFFF, 0x000000);
				}
			}
		}
		
	    
	    //se a�ade la secci�n al documento
	    report.addSectionToDocument("datosArticulos");
	    
	    
	    
	    
	    
	    /*
	    for(int i=0;i<6;i++)
	    	section.addTableTextCellAligned("encabezadoTable", "celda N� "+(i+1), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    */
	    /*
	    float widths[]={(float) 1,(float) 3, (float) 6};
	    
	    try 
        {
			section.getTableReference("encabezadoTable").setWidths(widths);
		} 
        catch (BadElementException e) 
        {
			;
		}*/
	    
	    //se a�ade la secci�n al documento
	    //report.addSectionToDocument("encabezado");
	    
	    //********************************************************************************************************************
	    report.closeReport();
	    
    }
}