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


public class ConsultaTarifasServiciosPdf 
{
	public static Logger logger=Logger.getLogger(ConsultaTarifasServiciosPdf.class);
	
	/**
     * Método para realizar la impresión de la liquidacion de servicios
     * @param filename
     * @param forma
     * @param usuario
     */
    public static void pdfConsultaTarifasServicios(String filename, ConsultaTarifasForm forma, HashMap mapa, UsuarioBasico usuario, HttpServletRequest request)
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
	    section.addTableTextCellAligned("encabezadoTable", "DETALLE SERVICIO", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
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
	    
	    //Tipo redondeo artículos	    
	    section.setTableCellsColSpan(2);
	    report.font.setFontAttributes(0x000000, 8, true, false, false);
	    section.addTableTextCellAligned("encabezadoTable", "Tipo Redondeo: "+mapa.get("tiporedondeo")+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellsDefaultColors("encabezadoTable", 0xFFFFFF, 0x000000);
	    
	    //Servicio
	    logger.info("===> Servicio: "+mapa.get("servicio")+"");
	    section.setTableCellsColSpan(8);
	    report.font.setFontAttributes(0x000000, 8, true, false, false);
	    section.addTableTextCellAligned("encabezadoTable", "Servicio: "+mapa.get("servicio")+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellsDefaultColors("encabezadoTable", 0xFFFFFF, 0x000000);
	    //se añade la sección al documento
	    report.addSectionToDocument("encabezado");
	    
	    //**************SECCION DATOS TABLA*********************************************************************
        section = report.createSection("datosServicios", "serviciosTable", 10, 11, 10);
  		section.setTableBorder("serviciosTable", 0x000000, 0.5f);
	    section.setTableCellBorderWidth("serviciosTable", 0.5f);
	    section.setTableCellPadding("serviciosTable", 4);
	    section.setTableSpaceBetweenCells("serviciosTable", 0.1f);
	    section.setTableCellsDefaultColors("serviciosTable", 0xFFFFFF, 0x000000);
	    
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
			    	//Vía Ingreso
					logger.info("===> Vía de Ingreso: "+mapa.get("nombreViaIngreso_"+vi)+"");
				    section.setTableCellsColSpan(3);
				    report.font.setFontAttributes(0x000000, 8, true, false, false);
				    section.addTableTextCellAligned("serviciosTable", 
				    		"Via Ingreso: "+mapa.get("nombreViaIngreso_"+vi)+"", 
				    		report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
				    report.font.setFontAttributes(0x000000, 8, false, false, false);
				    section.setTableCellsDefaultColors("serviciosTable", 0xFFFFFF, 0x000000);
				    
				    //Tipo Paciente
					logger.info("===> Tipo Paciente: "+mapa.get("nombreTipoPaciente_"+vi)+"");
				    section.setTableCellsColSpan(4);
				    report.font.setFontAttributes(0x000000, 8, true, false, false);
				    section.addTableTextCellAligned("serviciosTable", 
				    		"Tipo Paciente: "+mapa.get("nombreTipoPaciente_"+vi)+"", 
				    		report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
				    report.font.setFontAttributes(0x000000, 8, false, false, false);
				    section.setTableCellsDefaultColors("serviciosTable", 0xFFFFFF, 0x000000);
				    
				    //Tipo Complejidad
			    	logger.info("===> Si tipo complejidad > 0 ");
			    	logger.info("===> Tipo Complejidad: "+mapa.get("descripcionTipoComplejidad_"+tc)+"");
				    section.setTableCellsColSpan(4);
				    report.font.setFontAttributes(0x000000, 8, true, false, false);
				    section.addTableTextCellAligned("serviciosTable", 
				    		"Tipo Complejidad: "+mapa.get("descripcionTipoComplejidad_"+tc)+"", 
				    		report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
				    report.font.setFontAttributes(0x000000, 8, false, false, false);
				    section.setTableCellsDefaultColors("serviciosTable", 0xFFFFFF, 0x000000);
				}
			    else
			    {
			    	//Vía Ingreso
					logger.info("===> Vía de Ingreso: "+mapa.get("nombreViaIngreso_"+vi)+"");
				    section.setTableCellsColSpan(5);
				    report.font.setFontAttributes(0x000000, 8, true, false, false);
				    section.addTableTextCellAligned("serviciosTable", 
				    		"Via Ingreso: "+mapa.get("nombreViaIngreso_"+vi)+"", 
				    		report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
				    report.font.setFontAttributes(0x000000, 8, false, false, false);
				    section.setTableCellsDefaultColors("serviciosTable", 0xFFFFFF, 0x000000);
				    
				    //Tipo Paciente
					logger.info("===> Tipo Paciente: "+mapa.get("nombreTipoPaciente_"+vi)+"");
				    section.setTableCellsColSpan(6);
				    report.font.setFontAttributes(0x000000, 8, true, false, false);
				    section.addTableTextCellAligned("serviciosTable", 
				    		"Tipo Paciente: "+mapa.get("nombreTipoPaciente_"+vi)+"", 
				    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    report.font.setFontAttributes(0x000000, 8, false, false, false);
				    section.setTableCellsDefaultColors("serviciosTable", 0xFFFFFF, 0x000000);
			    }
			    
			    section.setTableCellsColSpan(1);
			    
			    //Fecha Asignación
			    logger.info("===> Fecha Asignación");
			    section.setTableCellsRowSpan(2);
			    report.font.setFontAttributes(0x000000, 8, true, false, false);
			    section.addTableTextCellAligned("serviciosTable", 
			    		"Fecha Asignación", 
			    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    report.font.setFontAttributes(0x000000, 8, false, false, false);
			    section.setTableCellsDefaultColors("serviciosTable", 0xFFFFFF, 0x000000);
			    
			    //Tarifa Base
			    logger.info("===> Tarifa Base");
			    section.setTableCellsRowSpan(2);
			    report.font.setFontAttributes(0x000000, 8, true, false, false);
			    section.addTableTextCellAligned("serviciosTable", 
			    		"Tarifa Base", 
			    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    report.font.setFontAttributes(0x000000, 8, false, false, false);
			    section.setTableCellsDefaultColors("serviciosTable", 0xFFFFFF, 0x000000);
			    
			    //Excepciones
			    logger.info("===> Excepciones");
			    section.setTableCellsRowSpan(1);
			    section.setTableCellsColSpan(3);
			    report.font.setFontAttributes(0x000000, 8, true, false, false);
			    section.addTableTextCellAligned("serviciosTable", 
			    		"Excepciones", 
			    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    report.font.setFontAttributes(0x000000, 8, false, false, false);
			    section.setTableCellsDefaultColors("serviciosTable", 0xFFFFFF, 0x000000);
			    
			    //Descuentos
			    logger.info("===> Descuentos");
			    section.setTableCellsColSpan(2);
			    report.font.setFontAttributes(0x000000, 8, true, false, false);
			    section.addTableTextCellAligned("serviciosTable", 
			    		"Descuentos", 
			    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    report.font.setFontAttributes(0x000000, 8, false, false, false);
			    section.setTableCellsDefaultColors("serviciosTable", 0xFFFFFF, 0x000000);
			    
			    section.setTableCellsColSpan(1);
			    
			    //Tarifa Final
			    logger.info("===> Tarifa Final");
			    section.setTableCellsRowSpan(2);
			    report.font.setFontAttributes(0x000000, 8, true, false, false);
			    section.addTableTextCellAligned("serviciosTable", 
			    		"Tarifa Final", 
			    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    report.font.setFontAttributes(0x000000, 8, false, false, false);
			    section.setTableCellsDefaultColors("serviciosTable", 0xFFFFFF, 0x000000);
			    
			    //Usuario Asigna
			    logger.info("===> Usuario Asigna");
			    section.setTableCellsColSpan(2);
			    section.setTableCellsRowSpan(2);
			    report.font.setFontAttributes(0x000000, 8, true, false, false);
			    section.addTableTextCellAligned("serviciosTable", 
			    		"Usuario Asigna", 
			    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    report.font.setFontAttributes(0x000000, 8, false, false, false);
			    section.setTableCellsDefaultColors("serviciosTable", 0xFFFFFF, 0x000000);
			    
			    //Tipo Cambio
			    logger.info("===> Tipo Cambio");
			    section.setTableCellsColSpan(1);
			    section.setTableCellsRowSpan(2);
			    report.font.setFontAttributes(0x000000, 8, true, false, false);
			    section.addTableTextCellAligned("serviciosTable", 
			    		"Tipo Cambio", 
			    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    report.font.setFontAttributes(0x000000, 8, false, false, false);
			    section.setTableCellsDefaultColors("serviciosTable", 0xFFFFFF, 0x000000);
			    
			    //%
			    logger.info("===> %");
			    section.setTableCellsRowSpan(1);
			    report.font.setFontAttributes(0x000000, 8, true, false, false);
			    section.addTableTextCellAligned("serviciosTable", 
			    		"%", 
			    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    report.font.setFontAttributes(0x000000, 8, false, false, false);
			    section.setTableCellsDefaultColors("serviciosTable", 0xFFFFFF, 0x000000);
			    
			    //Vr. Ajuste
			    logger.info("===> Vr. Ajuste");
			    report.font.setFontAttributes(0x000000, 8, true, false, false);
			    section.addTableTextCellAligned("serviciosTable", 
			    		"Vr. Ajuste", 
			    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    report.font.setFontAttributes(0x000000, 8, false, false, false);
			    section.setTableCellsDefaultColors("serviciosTable", 0xFFFFFF, 0x000000);
			    
			    //Nuev Tar.
			    logger.info("===> Nuev Tar.");
			    report.font.setFontAttributes(0x000000, 8, true, false, false);
			    section.addTableTextCellAligned("serviciosTable", 
			    		"Nuev Tar.", 
			    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    report.font.setFontAttributes(0x000000, 8, false, false, false);
			    section.setTableCellsDefaultColors("serviciosTable", 0xFFFFFF, 0x000000);
			    
			    //%
			    logger.info("===> %");
			    report.font.setFontAttributes(0x000000, 8, true, false, false);
			    section.addTableTextCellAligned("serviciosTable", 
			    		"%", 
			    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    report.font.setFontAttributes(0x000000, 8, false, false, false);
			    section.setTableCellsDefaultColors("serviciosTable", 0xFFFFFF, 0x000000);
			    
			    //Valor
			    logger.info("===> Valor");
			    report.font.setFontAttributes(0x000000, 8, true, false, false);
			    section.addTableTextCellAligned("serviciosTable", 
			    		"Valor", 
			    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    report.font.setFontAttributes(0x000000, 8, false, false, false);
			    section.setTableCellsDefaultColors("serviciosTable", 0xFFFFFF, 0x000000);

			    int tamanio=Utilidades.convertirAEntero(mapa.get("numRegistros_"+tipoComplejidad+"_"+viaIngreso+"_"+tipoPaciente)+"");
			    
				for(int i=0;i<tamanio;i++)
				{
					String indice=tipoComplejidad+"_"+viaIngreso+"_"+tipoPaciente+"_"+i;
					
					//Fecha Asignación
				    logger.info("===> Fecha Asignación: "+UtilidadFecha.conversionFormatoFechaAAp(mapa.get("fechaasignacion_"+indice)+"")+"");
				    report.font.setFontAttributes(0x000000, 8, true, false, false);
				    section.addTableTextCellAligned("serviciosTable", 
				    		UtilidadFecha.conversionFormatoFechaAAp(mapa.get("fechaasignacion_"+indice)+"")+"", 
				    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    report.font.setFontAttributes(0x000000, 8, false, false, false);
				    section.setTableCellsDefaultColors("serviciosTable", 0xFFFFFF, 0x000000);
				    
				    //Tarifa Base
				    double valorTarifa=Utilidades.convertirADouble(mapa.get("valortarifa_"+indice)+"");
				    report.font.setFontAttributes(0x000000, 8, true, false, false);
				    
					if(valorTarifa>0)
					{
						logger.info("===> Tarifa Base: "+UtilidadTexto.formatearValores(valorTarifa));
						section.addTableTextCellAligned("serviciosTable", 
								UtilidadTexto.formatearValores(valorTarifa), 
					    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
					}
					else
					{
						logger.info("===> Tarifa Base: Sin Tarifa");
						section.addTableTextCellAligned("serviciosTable", 
								"Sin Tarifa", 
					    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
					}
					
					report.font.setFontAttributes(0x000000, 8, false, false, false);
				    section.setTableCellsDefaultColors("serviciosTable", 0xFFFFFF, 0x000000);
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
					section.addTableTextCellAligned("serviciosTable", 
							porcentajes, 
				    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
					
					//Valor Ajuste
				    logger.info("===> Valor Ajuste: "+excepcion.getValor());
				    report.font.setFontAttributes(0x000000, 8, true, false, false);
				    section.addTableTextCellAligned("serviciosTable", 
				    		excepcion.getValor()+"", 
				    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    report.font.setFontAttributes(0x000000, 8, false, false, false);
				    section.setTableCellsDefaultColors("serviciosTable", 0xFFFFFF, 0x000000);
				    
				    //Nueva Tarifa
				    logger.info("===> Nueva Tarifa: "+excepcion.getNuevaTarifa()+"");
				    report.font.setFontAttributes(0x000000, 8, true, false, false);
				    section.addTableTextCellAligned("serviciosTable", 
				    		excepcion.getNuevaTarifa()+"", 
				    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    report.font.setFontAttributes(0x000000, 8, false, false, false);
				    section.setTableCellsDefaultColors("serviciosTable", 0xFFFFFF, 0x000000);
				    
				    InfoTarifa descuentos=(InfoTarifa)mapa.get("descuentos_"+indice);
				    
				    //Porcentaje Descuentos
				    report.font.setFontAttributes(0x000000, 8, true, false, false);
				   
				    if(descuentos.getPorcentajes().size()==0)
				    {
				    	logger.info("======================================");
				    	logger.info("===> El vector viene vacío");
				    }
				    
				    if(descuentos.getPorcentajes().size()>0)
					{
				    	logger.info("===> Porcentaje Descuentos: "+descuentos.getPorcentajes().get(0));
				    	section.addTableTextCellAligned("serviciosTable", 
				    			(descuentos.getPorcentajes().get(0))+"", 
					    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
					}
				    else
				    {
				    	logger.info("===> Porcentaje Descuentos: El vector viene vacío");
				    	section.addTableTextCellAligned("serviciosTable", 
				    			"", 
					    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    }
				    
				    report.font.setFontAttributes(0x000000, 8, false, false, false);
				    section.setTableCellsDefaultColors("serviciosTable", 0xFFFFFF, 0x000000);
				    
				    //Valor Descuentos
				    logger.info("===> Valor Descuentos: "+descuentos.getValor()+"");
				    report.font.setFontAttributes(0x000000, 8, true, false, false);
				    section.addTableTextCellAligned("serviciosTable", 
				    			descuentos.getValor()+"", 
					    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    report.font.setFontAttributes(0x000000, 8, false, false, false);
				    section.setTableCellsDefaultColors("serviciosTable", 0xFFFFFF, 0x000000);
				    
				    //Tarifa Final
				    double valorTarifaFinal=Utilidades.convertirADouble(mapa.get("tarifafinal_"+indice)+"");
				    report.font.setFontAttributes(0x000000, 8, true, false, false);
				    if(valorTarifa>0)
				    {
				    	logger.info("===> Tarifa Final: "+UtilidadTexto.formatearValores(valorTarifaFinal)+"");
				    	section.addTableTextCellAligned("serviciosTable", 
				    			UtilidadTexto.formatearValores(valorTarifaFinal)+"", 
					    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    }
					else
					{
						logger.info("===> Tarifa Final: Sin Tarifa");
						section.addTableTextCellAligned("serviciosTable", 
				    			"Sin Tarifa", 
					    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
					}
				    
				    report.font.setFontAttributes(0x000000, 8, false, false, false);
				    section.setTableCellsDefaultColors("serviciosTable", 0xFFFFFF, 0x000000);
				    
				    //Usuario Asigna
				    section.setTableCellsColSpan(2);
				    logger.info("===> Usuario Asigna: "+mapa.get("usuarioasigna_"+indice)+"");
				    report.font.setFontAttributes(0x000000, 8, true, false, false);
				    section.addTableTextCellAligned("serviciosTable", 
				    			mapa.get("usuarioasigna_"+indice)+"", 
					    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    report.font.setFontAttributes(0x000000, 8, false, false, false);
				    section.setTableCellsDefaultColors("serviciosTable", 0xFFFFFF, 0x000000);
				    
				    //Tipo Cambio
				    section.setTableCellsColSpan(1);
				    logger.info("===> Usuario Asigna: "+mapa.get("tipocambio_"+indice)+"");
				    report.font.setFontAttributes(0x000000, 8, true, false, false);
				    section.addTableTextCellAligned("serviciosTable", 
				    			mapa.get("tipocambio_"+indice)+"", 
					    		report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    report.font.setFontAttributes(0x000000, 8, false, false, false);
				    section.setTableCellsDefaultColors("serviciosTable", 0xFFFFFF, 0x000000);
				}
			}
		}
		
	    
	    //se añade la sección al documento
	    report.addSectionToDocument("datosServicios");
	    
	    
	    
	    
	    
	    /*
	    for(int i=0;i<6;i++)
	    	section.addTableTextCellAligned("encabezadoTable", "celda Nº "+(i+1), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
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
	    
	    //se añade la sección al documento
	    //report.addSectionToDocument("encabezado");
	    
	    //********************************************************************************************************************
	    report.closeReport();
	    
    }
}