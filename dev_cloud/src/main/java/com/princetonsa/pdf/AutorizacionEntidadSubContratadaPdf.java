package com.princetonsa.pdf;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;
import util.pdf.iTextBaseTable;

import com.princetonsa.actionform.manejoPaciente.AutorizacionesEntidadesSubcontratadasForm;
import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;


public class AutorizacionEntidadSubContratadaPdf {

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	private static Logger logger=Logger.getLogger(AutorizacionEntidadSubContratadaPdf.class);
	
	
	
	public static String pdfResumenAutorizacionEntidadSubContratada(UsuarioBasico usuario, HttpServletRequest request, PersonaBasica paciente,AutorizacionesEntidadesSubcontratadasForm forma)
	{
		///se edita nombre del archivo PDF
		String nombreArchivo;
    	Random r=new Random();
    	nombreArchivo="/aInfoEst" + r.nextInt()  +".pdf";
    	
    	// se carga los datos de la institucion
    	InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
    	
    	// se cargan los datos requeridos para el informe 
    	DtoSolicitudesSubCuenta dtoSol = (DtoSolicitudesSubCuenta)forma.getSolicitudesPendientes().get(forma.getPosSolicitud());
        
    	
    	logger.info("nombre del archivo: >>>>>>>>>>>>>>>>>"+nombreArchivo);
    	logger.info("\narchivo: >>>>>>>>>>>>>>>>>"+ValoresPorDefecto.getFilePath()+nombreArchivo);
    	
    	//*************IMPRESION DEL ENCABEZADO************************************************
    	PdfReports report = new PdfReports(); 
        
    	String filePath=ValoresPorDefecto.getFilePath();
        String titulo="AUTORIZACION ENTIDADES SUBCONTRATADAS" ;
        
        filePath=filePath.substring(0, filePath.lastIndexOf("/", (filePath.length()-2)));
        
        String tituloMedicamentos="AUTORIZACION ORDENES MEDICAS";
		report.setReportBaseHeaderIzq(institucionBasica, institucionBasica.getNit(), tituloMedicamentos);
		report.setUsuario(usuario.getLoginUsuario());
        
        //Se abre el reporte
        report.openReport(ValoresPorDefecto.getFilePath()+nombreArchivo);
        report.font.useFont(iTextBaseFont.FONT_TIMES, true, true, true);
        
        iTextBaseTable section;
		
      //**************SECCION DATOS DEL PACIENTE*********************************************************************
        section = report.createSection("paciente", "pacienteTable", 2, 3, 10);
        section.setTableBorder("pacienteTable", 0x000000, 1.0f);
	    section.setTableCellBorderWidth("pacienteTable", 1.0f);
	    section.setTableCellPadding("pacienteTable", 1);
	    section.setTableSpaceBetweenCells("pacienteTable", 0.5f);
	    section.setTableCellsDefaultColors("pacienteTable", 0xFFFFFF, 0x000000);
	    
	    // Datos Solicitud Autorizacion
	    section.setTableCellsColSpan(3);
	    report.font.setFontAttributes(0x000000, 12, true, false, false);
	    section.addTableTextCellAligned("pacienteTable", "DATOS PACIENTE", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellBorderWidth("pacienteTable", 0.5f);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("pacienteTable", "Paciente: "+forma.getNombrePacinte(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("pacienteTable", "Tipo y Nro ID: "+forma.getTipoIdPacinte() , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("pacienteTable", "Tipo Contrato Paciente: "+dtoSol.getTipoContrato(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    // se añade la seccion al documento    
	    report.addSectionToDocument("paciente");
	    //**************FIN SECCION DATOS DEL PACIENTE*********************************************************************
        
        
	    //**************SECCION DATOS AUTORIZACION*********************************************************************
        section = report.createSection("autorizacion", "autorizacionTable", 2, 5, 10);
        section.setTableBorder("autorizacionTable", 0x000000, 1.0f);
	    section.setTableCellBorderWidth("autorizacionTable", 1.0f);
	    section.setTableCellPadding("autorizacionTable", 1);
	    section.setTableSpaceBetweenCells("autorizacionTable", 0.5f);
	    section.setTableCellsDefaultColors("autorizacionTable", 0xFFFFFF, 0x000000);
	    
	    // Datos Autorizacion
	    section.setTableCellsColSpan(5);
	    report.font.setFontAttributes(0x000000, 12, true, false, false);
	    section.addTableTextCellAligned("autorizacionTable", "DATOS AUTORIZACION", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellBorderWidth("autorizacionTable", 0.5f);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("autorizacionTable", "Entidad Autorizacion: "+forma.getNomEntidadAutorizada(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("autorizacionTable", "Direccion Entidad: "+forma.getDirEntidadAutorizada() , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("autorizacionTable", "Telefono Entidad: "+forma.getTelEntidadAutorizada(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("autorizacionTable", "Nro Autorizacion: "+forma.getNumAutorizacion() , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("autorizacionTable", "Estado Autorizacion: "+forma.getEstadoAutorizacion(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    // se añade la seccion al documento    
	    report.addSectionToDocument("autorizacion");
	    //**************FIN SECCION DATOS DE LA AUTORIZACION*********************************************************************
        
	    
	    //**************SECCION2 (FECHAS) DATOS AUTORIZACION*********************************************************************
        section = report.createSection("autorizaciondos", "autorizaciondosTable", 2, 2, 10);
        section.setTableBorder("autorizaciondosTable", 0x000000, 1.0f);
	    section.setTableCellBorderWidth("autorizaciondosTable", 1.0f);
	    section.setTableCellPadding("autorizaciondosTable", 1);
	    section.setTableSpaceBetweenCells("autorizaciondosTable", 0.5f);
	    section.setTableCellsDefaultColors("autorizaciondosTable", 0xFFFFFF, 0x000000);
	    
	    // Datos Autorizacion
	
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellBorderWidth("autorizaciondosTable", 0.5f);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("autorizaciondosTable", "Fecha Autorizacion: "+UtilidadFecha.conversionFormatoFechaAAp(UtilidadFecha.getFechaActual()), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("autorizaciondosTable", "Fecha Vencimiento: "+forma.getFechaVencimiento() , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("autorizaciondosTable", "Entidad que Autoriza: "+forma.getInstitucionBasica(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("autorizaciondosTable", "Usuario que Autoriza: "+forma.getUsuarioBasico() , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    // se añade la seccion al documento    
	    report.addSectionToDocument("autorizaciondos");
	    //**************FIN SECCION2 DATOS DE LA AUTORIZACION*********************************************************************
        
	    
	    //**************SECCION DATOS DE LA ORDEN  ********************************************************************************
        section = report.createSection("orden", "ordenTable", 3, 5, 10);
        section.setTableBorder("ordenTable", 0x000000, 1.0f);
	    section.setTableCellBorderWidth("ordenTable", 1.0f);
	    section.setTableCellPadding("ordenTable", 1);
	    section.setTableSpaceBetweenCells("ordenTable", 0.5f);
	    section.setTableCellsDefaultColors("ordenTable", 0xFFFFFF, 0x000000);
	    
	    if(dtoSol.getArticulo().getCodigo().equals(-1))
	    {
	    	// Datos Orden Autorizacion
	    	section.setTableCellsColSpan(5);
	    	report.font.setFontAttributes(0x000000, 12, true, false, false);
	    	section.addTableTextCellAligned("ordenTable", "DATOS ORDEN", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    	report.font.setFontAttributes(0x000000, 8, false, false, false);
	    	section.setTableCellBorderWidth("ordenTable", 0.5f);
	    	section.setTableCellsColSpan(1);
	    	section.addTableTextCellAligned("ordenTable", "ORDEN ", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    	section.setTableCellsColSpan(1);
	    	section.addTableTextCellAligned("ordenTable", "CODIGO " , report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    	section.setTableCellsColSpan(1);
	    	section.addTableTextCellAligned("ordenTable", "SERVICIO ", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    	section.setTableCellsColSpan(1);
	    	section.addTableTextCellAligned("ordenTable", "CANTIDAD " , report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    	section.setTableCellsColSpan(1);
	    	section.addTableTextCellAligned("ordenTable", "NIVEL " , report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    	section.setTableCellsColSpan(1);
	    	section.addTableTextCellAligned("ordenTable", dtoSol.getNumeroSolicitud(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    	section.setTableCellsColSpan(1);
	    	section.addTableTextCellAligned("ordenTable", dtoSol.getCodServicioCodManualEstandar() , report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    	section.setTableCellsColSpan(1);
	    	section.addTableTextCellAligned("ordenTable", dtoSol.getServicio().getNombre(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    	section.setTableCellsColSpan(1);
	    	section.addTableTextCellAligned("ordenTable",  1+"" , report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    	section.setTableCellsColSpan(1);
	    	section.addTableTextCellAligned("ordenTable", forma.getNivelServicio() , report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    	//se añade la seccion al documento    
	    	report.addSectionToDocument("orden");
	    	//**************FIN SECCION DATOS DE LA ORDEN *********************************************************************
	    }else
	    {
	    	for(int i=0;i<dtoSol.getAgrupaListadoAutoriPendEntSub().size();i++)
	    	{
	    		if(dtoSol.getAgrupaListadoAutoriPendEntSub().get(i).getEsMedicamento().toString().equals(ConstantesBD.acronimoSi))
	    		{	
	    			// Datos Orden Autorizacion	    	
	    			section.setTableCellsColSpan(5);
	    			report.font.setFontAttributes(0x000000, 12, true, false, false);
	    			section.addTableTextCellAligned("ordenTable", "DATOS ORDEN", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    			report.font.setFontAttributes(0x000000, 8, false, false, false);
	    			section.setTableCellBorderWidth("ordenTable", 0.5f);
	    			section.setTableCellsColSpan(1);
	    			section.addTableTextCellAligned("ordenTable", "CODIGO", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    			section.setTableCellsColSpan(1);
	    			section.addTableTextCellAligned("ordenTable", "DESCRIPCION " , report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    			section.setTableCellsColSpan(1);
	    			section.addTableTextCellAligned("ordenTable", "U. MEDIDA ", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    			section.setTableCellsColSpan(1);
	    			section.addTableTextCellAligned("ordenTable", "DOSIS " , report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    			section.setTableCellsColSpan(1);
	    			section.addTableTextCellAligned("ordenTable", "FRECUENCIA " , report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    			section.setTableCellsColSpan(1);
	    			section.addTableTextCellAligned("ordenTable", "VIA " , report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    			section.setTableCellsColSpan(1);
	    			section.addTableTextCellAligned("ordenTable", "DIAS TRATAMIENTO" , report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    			section.setTableCellsColSpan(1);
	    			section.addTableTextCellAligned("ordenTable", "CANTIDAD" , report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    			section.setTableCellsColSpan(1);
	    			section.addTableTextCellAligned("ordenTable", dtoSol.getAgrupaListadoAutoriPendEntSub().get(i).getArticulo().getCodigo(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    			section.setTableCellsColSpan(1);
	    			section.addTableTextCellAligned("ordenTable", dtoSol.getAgrupaListadoAutoriPendEntSub().get(i).getArticulo().getNombre() , report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    			section.setTableCellsColSpan(1);
	    			section.addTableTextCellAligned("ordenTable", dtoSol.getAgrupaListadoAutoriPendEntSub().get(i).getUnidadMedidaArticulo(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    			section.setTableCellsColSpan(1);
	    			section.addTableTextCellAligned("ordenTable", dtoSol.getAgrupaListadoAutoriPendEntSub().get(i).getDosisArticulo()  , report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    				String  frec    =dtoSol.getAgrupaListadoAutoriPendEntSub().get(i).getFrecuArticulo();
	    				String 	tipoFrec=dtoSol.getAgrupaListadoAutoriPendEntSub().get(i).getTipoFrecueArticulo();
	    			section.setTableCellsColSpan(1);
	    			section.addTableTextCellAligned("ordenTable", tipoFrec+" "+frec, report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    			section.setTableCellsColSpan(1);
	    			section.addTableTextCellAligned("ordenTable", dtoSol.getAgrupaListadoAutoriPendEntSub().get(i).getViaArticulo() , report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    			section.setTableCellsColSpan(1);
	    			section.addTableTextCellAligned("ordenTable", dtoSol.getAgrupaListadoAutoriPendEntSub().get(i).getDuracionArticulo() , report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    			section.setTableCellsColSpan(1);
	    			section.addTableTextCellAligned("ordenTable", dtoSol.getAgrupaListadoAutoriPendEntSub().get(i).getNroDosisTotalArticulo() , report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    		}else
	    		{
	    			// Datos Orden Autorizacion	    	
	    			section.setTableCellsColSpan(5);
	    			report.font.setFontAttributes(0x000000, 12, true, false, false);
	    			section.addTableTextCellAligned("ordenTable", "DATOS ORDEN", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    			report.font.setFontAttributes(0x000000, 8, false, false, false);
	    			section.setTableCellBorderWidth("ordenTable", 0.5f);
	    			section.setTableCellsColSpan(1);
	    			section.addTableTextCellAligned("ordenTable", "CODIGO", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    			section.setTableCellsColSpan(1);
	    			section.addTableTextCellAligned("ordenTable", "DESCRIPCION " , report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    			section.setTableCellsColSpan(1);
	    			section.addTableTextCellAligned("ordenTable", "U. MEDIDA ", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);	    			
	    			section.setTableCellsColSpan(1);
	    			section.addTableTextCellAligned("ordenTable", "CANTIDAD" , report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    			section.setTableCellsColSpan(1);
	    			section.addTableTextCellAligned("ordenTable", dtoSol.getAgrupaListadoAutoriPendEntSub().get(i).getArticulo().getCodigo(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    			section.setTableCellsColSpan(1);
	    			section.addTableTextCellAligned("ordenTable", dtoSol.getAgrupaListadoAutoriPendEntSub().get(i).getArticulo().getNombre() , report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    			section.setTableCellsColSpan(1);
	    			section.addTableTextCellAligned("ordenTable", dtoSol.getAgrupaListadoAutoriPendEntSub().get(i).getUnidadMedidaArticulo(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    			section.setTableCellsColSpan(1);
	    			section.addTableTextCellAligned("ordenTable", dtoSol.getAgrupaListadoAutoriPendEntSub().get(i).getNroDosisTotalArticulo() , report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    			
	    		}
	    	}
	    	//se añade la seccion al documento    
	    	report.addSectionToDocument("orden");
	    	//**************FIN SECCION DATOS DE LA ORDEN *********************************************************************
	    	
	    }
	    
	    //**************SECCION OBSERVACIONES AUTORIZACION*********************************************************************
        section = report.createSection("observaciones", "observacionesTable", 2, 1, 10);
        section.setTableBorder("observacionesTable", 0x000000, 1.0f);
	    section.setTableCellBorderWidth("observacionesTable", 1.0f);
	    section.setTableCellPadding("observacionesTable", 1);
	    section.setTableSpaceBetweenCells("observacionesTable", 0.5f);
	    section.setTableCellsDefaultColors("observacionesTable", 0xFFFFFF, 0x000000);
	    
	    // Observaciones
	    section.setTableCellsColSpan(1);
	    report.font.setFontAttributes(0x000000, 12, true, false, false);
	    section.addTableTextCellAligned("observacionesTable", "OBSERVACIONES AUTORIZACION", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellBorderWidth("observacionesTable", 0.5f);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("observacionesTable", forma.getObservaciones(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	   
	    // se añade la seccion al documento    
	    report.addSectionToDocument("observaciones");
	    //**************FIN SECCION OBSERVACIONES AUTORIZACION*********************************************************************
	    	    
	    
	    report.closeReport();
	    return nombreArchivo;
	}
	
	
}
