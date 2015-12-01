package com.princetonsa.pdf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import util.InfoDatosInt;
import util.InfoDatosString;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.consultaExterna.UtilidadesConsultaExterna;
import util.odontologia.UtilidadOdontologia;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;
import util.pdf.iTextBaseTable;

import com.princetonsa.dto.manejoPaciente.DtoInformeAtencionIniUrg;
import com.princetonsa.dto.odontologia.DtoAgendaOdontologica;
import com.princetonsa.dto.odontologia.DtoCitaOdontologica;
import com.princetonsa.dto.odontologia.DtoServicioCitaOdontologica;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.RegistroEnvioInfAtencionIniUrg;
import com.princetonsa.mundo.odontologia.CitaOdontologica;

/**
 * @author Víctor Hugo Gómez L.
 */

public class CitasOdontologicasPdf 
{
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	private static Logger logger=Logger.getLogger(CitasOdontologicasPdf.class);
	
	public static String pdfCitaOdontologica(
			UsuarioBasico usuario,
			HttpServletRequest request,
			DtoAgendaOdontologica dto,
			DtoCitaOdontologica dtoCita,
			PersonaBasica paciente)
	{
		//se edita nombre del archivo PDF
		String nombreArchivo;
    	Random r=new Random();
    	nombreArchivo="/aBorrar" + r.nextInt()  +".pdf";

    	logger.info("nombre del archivo: >>>>>>>>>>>>>>>>>"+nombreArchivo);
    	logger.info("\narchivo: >>>>>>>>>>>>>>>>>"+ValoresPorDefecto.getFilePath()+nombreArchivo);
    	
    	InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
    	
    	//*************IMPRESION DEL ENCABEZADO************************************************
    	PdfReports report = new PdfReports(); 
        
    	String filePath=ValoresPorDefecto.getFilePath();
        filePath=filePath.substring(0, filePath.lastIndexOf("/", (filePath.length()-2)));
        
        //-- Titulo del reporte
        String tituloReporte="  Cita Odontológica ";
        report.setReportBaseHeader1(institucionBasica,"align-left" , tituloReporte);
		
        //Se abre el reporte
        report.openReport(ValoresPorDefecto.getFilePath()+nombreArchivo);
        report.font.useFont(iTextBaseFont.FONT_TIMES, true, true, true);
        
        //**************SECCION DATOS CITA ODONTOLÓGICA*****************************************
        iTextBaseTable section;
        section = report.createSection("citaOdonto", "citaOdontoTable", 3,5 , 10);
        
        section.setTableBorder("citaOdontoTable", 0x000000, 0.0f);
	    section.setTableCellBorderWidth("citaOdontoTable", 0.0f);
	    section.setTableCellPadding("citaOdontoTable", 1);
	    section.setTableSpaceBetweenCells("citaOdontoTable", 0.5f);
	    section.setTableCellsDefaultColors("citaOdontoTable", 0xFFFFFF, 0x000000);
	    
	    // Datos Sección
	    /*section.setTableCellsColSpan(5);
	    report.font.setFontAttributes(0x000000, 12, true, false, false);
	    section.addTableTextCellAligned("citaOdontoTable", "Información del Prestador", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellBorderWidth("citaOdontoTable", 0.5f);*/
	    report.font.setFontAttributes(0x000000, 8, true, false, false);
	    InfoDatosString centroAtencion = new InfoDatosString();
	    centroAtencion = CitaOdontologica.obtenerInfoCentroAtencion(usuario.getCodigoCentroAtencion());
	    section.setTableCellsColSpan(5);
	    section.addTableTextCellAligned("citaOdontoTable", "Centro de Ateción: "+centroAtencion.getId()+" "+centroAtencion.getDescripcion()+" "+centroAtencion.getIndicativo(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(5);
	    section.addTableTextCellAligned("citaOdontoTable", "Paciente: "+paciente.getPrimerNombre()+" "+paciente.getSegundoNombre()+" "+paciente.getPrimerApellido()+" "+paciente.getSegundoApellido()+" "+paciente.getTipoIdentificacionPersona(false)+" "+paciente.getNumeroIdentificacionPersona(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(5);
	    section.addTableTextCellAligned("citaOdontoTable", "Unidad de Agenda: "+dto.getDescripcionUniAgen(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(5);
	    section.addTableTextCellAligned("citaOdontoTable", "Profesional de la Salud: "+dto.getNombreMedico(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(2);
	    section.addTableTextCellAligned("citaOdontoTable", "Fecha y Hora: "+dto.getFecha()+" - "+dtoCita.getHoraInicio()+" ("+dtoCita.getDuracion()+ "Min.)", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("citaOdontoTable", "Consultorio: "+dto.getDescripcionConsultorio(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("citaOdontoTable", "No. Cita: "+dtoCita.getCodigoPk(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("citaOdontoTable", "Estado: "+ValoresPorDefecto.getIntegridadDominio(dtoCita.getEstado()), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);

	    section.setTableCellsColSpan(5);
	    report.font.setFontAttributes(0x000000, 10, true, false, false);
	    section.addTableTextCellAligned("citaOdontoTable", "DETALLE SERVICIOS", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellBorderWidth("citaOdontoTable", 0.5f);
	    
	    for(DtoServicioCitaOdontologica elem: dtoCita.getServiciosCitaOdon())
	    {
	    	elem.setNombreServicio(UtilidadOdontologia.obenerNombreServicio(
	    			elem.getServicio(), 
	    			Utilidades.convertirAEntero(institucionBasica.getCodigo())));
	    	ArrayList<InfoDatosInt> arrayConToma = new ArrayList<InfoDatosInt>();
	    	arrayConToma = UtilidadOdontologia.obtenerCondicionesTomaServicio(elem.getServicio(), Utilidades.convertirAEntero(institucionBasica.getCodigo()));
	    	section.setTableCellsColSpan(2);
	    	if(arrayConToma.size()>0)
	    	{
	    		section.setTableCellsRowSpan(arrayConToma.size());
	    		section.addTableTextCellAligned("citaOdontoTable", elem.getNombreServicio(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    		for(InfoDatosInt elem1: arrayConToma)
			    {
		    		section.setTableCellsColSpan(3);
		    		section.setTableCellsRowSpan(1);
				    section.addTableTextCellAligned("citaOdontoTable", elem1.getNombre(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    }
	    	}else{
	    		section.setTableCellsRowSpan(1);
	    		section.addTableTextCellAligned("citaOdontoTable", elem.getNombreServicio(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    		section.setTableCellsColSpan(3);
			    section.addTableTextCellAligned("citaOdontoTable", "No hay requisitos de toma del servicio.", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    	}
	    	
	    }
	    // se añade la seccion al documento
	    report.addSectionToDocument("citaOdonto");
	    //**************FIN SECCION DATOS CITA ODONTOLÓGICA***************************************
	    
	    report.setUsuario(usuario.getLoginUsuario());
	    report.closeReport();
	    return nombreArchivo;
	}
}
