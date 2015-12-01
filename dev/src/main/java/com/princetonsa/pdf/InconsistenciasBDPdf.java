package com.princetonsa.pdf;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.axioma.util.fechas.UtilidadesFecha;

import util.BackUpBaseDatos;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosString;
import util.TxtFile;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadFileUpload;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;
import util.pdf.iTextBaseTable;
import util.xml.UtilidadAutorizacionesEDI;

import com.princetonsa.actionform.manejoPaciente.ConsultaEnvioInconsistenciasenBDForm;
import com.princetonsa.actionform.manejoPaciente.RegistroEnvioInformInconsisenBDForm;
import com.princetonsa.autorizaciones.informePresuntaInconsistencia.EstructuraInformeUrgencias;
import com.princetonsa.autorizaciones.informePresuntaInconsistencia.General;
import com.princetonsa.autorizaciones.informePresuntaInconsistencia.Identificacion;
import com.princetonsa.autorizaciones.informePresuntaInconsistencia.Inconsistencia;
import com.princetonsa.autorizaciones.informePresuntaInconsistencia.InformacionPersonal;
import com.princetonsa.autorizaciones.informePresuntaInconsistencia.NombreCompleto;
import com.princetonsa.autorizaciones.informePresuntaInconsistencia.Paciente;
import com.princetonsa.autorizaciones.informePresuntaInconsistencia.Pagador;
import com.princetonsa.autorizaciones.informePresuntaInconsistencia.PersonaReportante;
import com.princetonsa.autorizaciones.informePresuntaInconsistencia.UbicacionGeografica;
import com.princetonsa.autorizaciones.informePresuntaInconsistencia.VariableInconsistencia;
import com.princetonsa.dao.manejoPaciente.RegistroEnvioInformInconsisenBDDao;
import com.princetonsa.dto.manejoPaciente.DtoDiagInfoIniUrg;
import com.princetonsa.dto.manejoPaciente.DtoInformeAtencionIniUrg;
import com.princetonsa.dto.manejoPaciente.DtoInformeInconsisenBD;
import com.princetonsa.dto.manejoPaciente.DtoVariablesIncorrectasenBD;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.RegistroEnvioInfAtencionIniUrg;
import com.princetonsa.mundo.manejoPaciente.RegistroEnvioInformInconsisenBD;

public class InconsistenciasBDPdf
{
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	private static Logger logger=Logger.getLogger(LiquidacionServiciosPdf.class);
	
	
	/**
     * Método para realizar la impresión del informe de incosistencias
     * @param usuario
     * @param request
     * @param paciente
     * @param cod_informe_inco
     */
    public static String pdfInconsistenciasBD(Connection con, UsuarioBasico usuario, HttpServletRequest request, PersonaBasica paciente, int cod_informe_inco)
    {
    	///se edita nombre del archivo PDF
		String nombreArchivo;
    	Random r=new Random();
    	nombreArchivo="/aBorrarInconsistenciasBD" + r.nextInt()  +".pdf";
    	
    	// se cargan los datos requeridos para el informe
    	DtoInformeInconsisenBD dtoInfoInco = RegistroEnvioInformInconsisenBD.getInformeInconsistencias(con, cod_informe_inco);
    	  	
    	logger.info("nombre del archivo: >>>>>>>>>>>>>>>>>"+nombreArchivo);
    	logger.info("\narchivo: >>>>>>>>>>>>>>>>>"+ValoresPorDefecto.getFilePath()+nombreArchivo);
    	
    	InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
    	
    	//*************IMPRESION DEL ENCABEZADO************************************************
    	PdfReports report = new PdfReports(); 
        
    	String filePath=ValoresPorDefecto.getFilePath();
        String tituloDespacho="ATENCION INICIAL URGENCIAS" ;
        
        filePath=filePath.substring(0, filePath.lastIndexOf("/", (filePath.length()-2)));
        
        HashMap encabezado = new HashMap<String,Object>();
        encabezado.put("entidad_requiere", "MINISTERIO DE LA PROTECCION SOCIAL");
        encabezado.put("titulo", "INFORME DE POSIBLES INCOSISTENCIAS EN LA BASE DE DATOS DE LA ENTIDAD RESPONSABLE DEL PAGO");
        encabezado.put("numero_atencion", "Número Informe: "+dtoInfoInco.getConsecutivoIngreso());
        encabezado.put("fecha", dtoInfoInco.getFechaGeneracion());
        encabezado.put("hora", dtoInfoInco.getHoraGeneracion());
        report.setReportBaseHeader2(encabezado);
		
        //Se abre el reporte
        report.openReport(ValoresPorDefecto.getFilePath()+nombreArchivo);
        report.font.useFont(iTextBaseFont.FONT_TIMES, true, true, true);
        
        //**************SECCION PRESTADOR*********************************************************************
        iTextBaseTable section;
        section = report.createSection("prestador", "prestadorTable", 3, 3, 10);
        
        section.setTableBorder("prestadorTable", 0x000000, 1.0f);
	    section.setTableCellBorderWidth("prestadorTable", 1.0f);
	    section.setTableCellPadding("prestadorTable", 1);
	    section.setTableSpaceBetweenCells("prestadorTable", 0.5f);
	    section.setTableCellsDefaultColors("prestadorTable", 0xFFFFFF, 0x000000);
	    
	    // Datos del Prestador
	    section.setTableCellsColSpan(3);
	    report.font.setFontAttributes(0x000000, 12, true, false, false);
	    section.addTableTextCellAligned("prestadorTable", "Información del Prestador", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellBorderWidth("prestadorTable", 0.5f);
	    section.setTableCellsColSpan(2);
	    section.addTableTextCellAligned("prestadorTable", "Nombre: "+institucionBasica.getRazonSocial(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("prestadorTable", (institucionBasica.getTipoIdentificacion().equals("NI")?institucionBasica.getDescripcionTipoIdentificacion():institucionBasica.getTipoIdentificacion())+" "+(!institucionBasica.getNit().equals("")?institucionBasica.getNit():"")+" "+(!institucionBasica.getDigitoVerificacion().equals("")?"-"+institucionBasica.getDigitoVerificacion():""), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("prestadorTable", "Código: "+institucionBasica.getCodMinsalud(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(2);
	    section.addTableTextCellAligned("prestadorTable", "Dirección Prestador: "+institucionBasica.getDireccion(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    String indicativo = institucionBasica.getIndicativo().equals("")?"":"Ind ("+institucionBasica.getIndicativo()+")";
	    String extension = institucionBasica.getExtension().equals("")?"":"( Ext "+institucionBasica.getExtension()+")";
	    section.addTableTextCellAligned("prestadorTable", "Teléfono: "+indicativo+institucionBasica.getTelefono()+extension, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("prestadorTable", "Departamento: "+institucionBasica.getDepto()+" ("+institucionBasica.getCodigoDepto()+")", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("prestadorTable", "Municipio: "+institucionBasica.getCiudad()+" ("+institucionBasica.getCodigoCiudad()+")", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    Iterator iter = dtoInfoInco.getConveniosPaciente().iterator();
	    while(iter.hasNext())
	    {
	    	InfoDatosString datos = (InfoDatosString) iter.next();
	    	section.setTableCellsColSpan(2);
	    	section.addTableTextCellAligned("prestadorTable", "Entidad a al que se le Informa (Pagador): "+datos.getNombre(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    	section.setTableCellsColSpan(1);
	    	section.addTableTextCellAligned("prestadorTable", "Código: "+datos.getCodigo(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    }
	    section.setTableCellsColSpan(3);
	    section.addTableTextCellAligned("prestadorTable", "Tipo Inconsistencia: "+dtoInfoInco.getTipoInconsistenciaInfo().getNombre(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    // se añade la seccion al docuemtno
	    report.addSectionToDocument("prestador");
	    //**************FIN SECCION PRESTADOR*********************************************************************
	    
	    
	    //**************SECCION DATOS DEL PACIENTE*********************************************************************
        section = report.createSection("paciente", "pacienteTable", 3, 4, 10);
        section.setTableBorder("pacienteTable", 0x000000, 1.0f);
	    section.setTableCellBorderWidth("pacienteTable", 1.0f);
	    section.setTableCellPadding("pacienteTable", 1);
	    section.setTableSpaceBetweenCells("pacienteTable", 0.5f);
	    section.setTableCellsDefaultColors("pacienteTable", 0xFFFFFF, 0x000000);
	    
	    // Datos del Prestador
	    section.setTableCellsColSpan(4);
	    report.font.setFontAttributes(0x000000, 12, true, false, false);
	    section.addTableTextCellAligned("pacienteTable", "Datos del Usuario (datos como aparece en la base de datos)", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellBorderWidth("pacienteTable", 0.5f);
	    section.addTableTextCellAligned("pacienteTable", "Apellidos y Nombres: "+dtoInfoInco.getPrimerApellido()+" "+dtoInfoInco.getSegundoApellido()+" "+dtoInfoInco.getPrimerNombre()+" "+dtoInfoInco.getSegundoNombre(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(2);
	    section.addTableTextCellAligned("pacienteTable", "Tipo Identificación y Número: "+dtoInfoInco.getTipoIdentificacion()+" "+dtoInfoInco.getIdPersona()+" de "+paciente.getNombreCiudadExpedicion()+" ("+paciente.getNombrePaisExpedicion()+")", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(2);
	    section.addTableTextCellAligned("pacienteTable", "Fecha Nacimiento : "+dtoInfoInco.getFechaNacimiento(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(3);
	    section.addTableTextCellAligned("pacienteTable", "Dirección de Residencia Habitual: "+dtoInfoInco.getDireccionResidencia(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("pacienteTable", "Teléfono: "+dtoInfoInco.getTelefono(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(2);
	    section.addTableTextCellAligned("pacienteTable", "Departamento: "+dtoInfoInco.getDepartamento()+"("+dtoInfoInco.getCodigoDepartamento()+")" , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(2);
	    section.addTableTextCellAligned("pacienteTable", "Municipio: "+dtoInfoInco.getMunicipio()+"("+dtoInfoInco.getCodigoMunicipio()+")" , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(4);
	    section.addTableTextCellAligned("pacienteTable", "Cobertura en Salud: "+dtoInfoInco.getCobertura() , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    // se añade la seccion al documento    
	    report.addSectionToDocument("paciente");
	    //**************FIN SECCION DATOS DEL PACIENTE*********************************************************************
	    
	    
	    //**************SECCION INFORMACIÓN DE LA POSIBLE INCOSISTENCIA*********************************************************************
        section = report.createSection("inconsistencia", "inconsistenciaTable", 3, 3, 10);
        section.setTableBorder("inconsistenciaTable", 0x000000, 1.0f);
	    section.setTableCellBorderWidth("inconsistenciaTable", 1.0f);
	    section.setTableCellPadding("inconsistenciaTable", 1);
	    section.setTableSpaceBetweenCells("inconsistenciaTable", 0.5f);
	    section.setTableCellsDefaultColors("inconsistenciaTable", 0xFFFFFF, 0x000000);
	    
	    section.setTableCellsColSpan(3);
	    report.font.setFontAttributes(0x000000, 12, true, false, false);
	    section.addTableTextCellAligned("inconsistenciaTable", "Información de la Posible Inconsistencia", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    float[] widths = new float[3];
		widths[0] = 2f;
		widths[1] = 28f;
		widths[2] = 70f;
		report.setColumnsWidths("inconsistencia", "inconsistenciaTable", widths);
	    section.setTableCellsColSpan(2);
	    report.font.setFontAttributes(0x000000, 10, true, false, false);
	    section.addTableTextCellAligned("inconsistenciaTable", "Variable Presuntamente Incorrecta", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    report.font.setFontAttributes(0x000000, 10, true, false, false);
	    section.addTableTextCellAligned("inconsistenciaTable", "Datos Según Documento de Identificacion (físico)", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellBorderWidth("inconsistenciaTable", 0.5f);
	    
		
	    // Datos del Inconsistencias
		boolean band = false;
	    HashMap<String,Object> varincorrectas = new HashMap<String,Object>(); 
	    varincorrectas = RegistroEnvioInformInconsisenBD.cargarVariablesIncorrectas(con);
	    DtoVariablesIncorrectasenBD inco_var_incorrectas = null;
	    iter = dtoInfoInco.getVariablesIncorrectas().iterator();
	    if(iter.hasNext()){
	    	band=true;
	    	inco_var_incorrectas = (DtoVariablesIncorrectasenBD) iter.next();
	    }
	    else
	    	band=false;
	    logger.info("Valor de la Bandera >>>>>>>>>>>>>>>>>>>>"+band);
	    logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	    Utilidades.imprimirMapa(varincorrectas);
	    logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	    for(int cont=0;cont<Utilidades.convertirAEntero(varincorrectas.get("numRegistros").toString());cont++)
	    {
	    	logger.info("Valor de la variable contador "+cont);
	    	if(band){
		    	if(inco_var_incorrectas.getTipoVariable()==Utilidades.convertirAEntero(varincorrectas.get("codigo_"+cont).toString())){
		    		section.addTableTextCellAligned("inconsistenciaTable", "X", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    	section.addTableTextCellAligned("inconsistenciaTable", varincorrectas.get("descripcion_"+cont).toString(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    	section.addTableTextCellAligned("inconsistenciaTable", varincorrectas.get("descripcion_"+cont).toString()+": "+inco_var_incorrectas.getValor(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    	if(iter.hasNext()){
				    	band=true;
				    	inco_var_incorrectas = (DtoVariablesIncorrectasenBD) iter.next();
				    }
				    else
				    	band=false;
		    	}else{
		    		section.addTableTextCellAligned("inconsistenciaTable", " ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    	section.addTableTextCellAligned("inconsistenciaTable", varincorrectas.get("descripcion_"+cont).toString(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    	section.addTableTextCellAligned("inconsistenciaTable", varincorrectas.get("descripcion_"+cont).toString()+": ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    	}
	    	}else{
	    		logger.info("valor >>>>>>>> "+varincorrectas.get("descripcion_"+cont).toString());
	    		section.addTableTextCellAligned("inconsistenciaTable", " ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    	section.addTableTextCellAligned("inconsistenciaTable", varincorrectas.get("descripcion_"+cont).toString(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    	section.addTableTextCellAligned("inconsistenciaTable", varincorrectas.get("descripcion_"+cont).toString()+": ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    	}
	    }
	    section.setTableCellsColSpan(3);
	    section.addTableTextCellAligned("inconsistenciaTable", "Observaciones: "+dtoInfoInco.getObservaciones() , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    // se añade la seccion al documento    
	    report.addSectionToDocument("inconsistencia");
	    //**************FIN SECCION INFORMACIÓN DE LA POSIBLE INCOSISTENCIA*********************************************************************
	    
	  //**************SECCION INFORMACIÓN DE LA PERSONA QUE INFORMA*********************************************************************
        section = report.createSection("persinfo", "persinfoTable", 3, 4, 10);
        section.setTableBorder("persinfoTable", 0x000000, 1.0f);
	    section.setTableCellBorderWidth("persinfoTable", 1.0f);
	    section.setTableCellPadding("persinfoTable", 1);
	    section.setTableSpaceBetweenCells("persinfoTable", 0.5f);
	    section.setTableCellsDefaultColors("persinfoTable", 0xFFFFFF, 0x000000);
	    
	    // Datos del Prestador
	    section.setTableCellsColSpan(4);
	    report.font.setFontAttributes(0x000000, 12, true, false, false);
	    section.addTableTextCellAligned("persinfoTable", "Información de la Persona que Reporta", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellBorderWidth("persinfoTable", 0.5f);
	    section.setTableCellsColSpan(3);
	    section.addTableTextCellAligned("persinfoTable", "Nombre de Quien Reporta: "+dtoInfoInco.getUsuarioGeneracion(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("persinfoTable", "Teléfono: "+indicativo+institucionBasica.getTelefono()+extension, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(3);
	    section.addTableTextCellAligned("persinfoTable", "Cargo: "+dtoInfoInco.getCargo(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("persinfoTable", "Teléfono Celular: "+dtoInfoInco.getTelefonoCelularPer() , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    // se añade la seccion al documento    
	    report.addSectionToDocument("persinfo");
	    //**************FIN SECCION INFORMACIÓN DE LA PERSONA QUE INFORMA*********************************************************************
	    
	    
	    report.closeReport();
	    return nombreArchivo;
    }
    
    /**
     * 
     * @param con
     * @param usuario
     * @param request
     * @param paciente
     * @param cod_informe_inco
     * @param forma
     * @return
     */
    public static String xmlInformeInconsistenciaBD(Connection con, UsuarioBasico usuario, HttpServletRequest request, PersonaBasica paciente, int cod_informe_inco, RegistroEnvioInformInconsisenBDForm forma)
    {
    	logger.info("Medio de Envio: "+forma.getInformeIconsistencias().getMedioEnvio());
    	if(forma.getInformeIconsistencias().getMedioEnvio().equals(ConstantesIntegridadDominio.acronimoIntercambioElectDatos))
		{
    		int band=ConstantesBD.codigoNuncaValido;
    		boolean exist = false;
		  	//se edita nombre del archivo 
			String nombreArchivo = "";
			String path = "";
			String url = "";
	    	Random r=new Random();
	    	nombreArchivo="InconBDXML"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+r.nextInt();
	    	path=ValoresPorDefecto.getFilePath()+System.getProperty("file.separator");
	    	url="../upload"+System.getProperty("file.separator");
	    	// se cargan los datos requeridos para el informe
	    	DtoInformeInconsisenBD dto = RegistroEnvioInformInconsisenBD.getInformeInconsistencias(con, cod_informe_inco);
	    	InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		    EstructuraInformeUrgencias informeIncosistencia = new EstructuraInformeUrgencias();
			
			try 
			{
				generacionArchivoXml(paciente, nombreArchivo, path, dto,institucionBasica, informeIncosistencia,forma.getInformeIconsistencias().getObservaciones());
				
				//****************************************************************************
				// Creacion del archivo .zip y verificacion de lo la existencia de los mismos
				//se genera el archivo en formato Zip
				band=BackUpBaseDatos.EjecutarComandoSO("zip -j "+path+nombreArchivo+".zip -j"+" "+path+nombreArchivo+".xml");
				//se ingresa la direccion donde se almaceno el archivo
				forma.getInformeIconsistencias().setPathArchivoIncoXml(path+nombreArchivo+".xml");
				//se valida si existe el xml
				exist=UtilidadFileUpload.existeArchivo(path, nombreArchivo+".xml");
				//se valida si existe el zip
				exist=UtilidadFileUpload.existeArchivo(path, nombreArchivo+".zip");
				// Creacion del archivo .zip y verificacion de lo la existencia de los mismos
				//****************************************************************************
				if(exist)
					return nombreArchivo+".zip";
				else
					return null;
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		    
		}
    	return null;
    }

    /**
     * 
     * @param con
     * @param usuario
     * @param request
     * @param paciente
     * @param cod_informe_inco
     * @param forma
     * @return
     */
    public static String xmlInformeInconsistenciaBD(Connection con, UsuarioBasico usuario, HttpServletRequest request, PersonaBasica paciente, int cod_informe_inco, ConsultaEnvioInconsistenciasenBDForm forma)
    {
    	logger.info("Medio de Envio: "+forma.getInformeIconsistencias().getMedioEnvio());
    	if(forma.getInformeIconsistencias().getMedioEnvio().equals(ConstantesIntegridadDominio.acronimoIntercambioElectDatos))
		{
    		int band=ConstantesBD.codigoNuncaValido;
    		boolean exist = false;
		  	//se edita nombre del archivo 
			String nombreArchivo = "";
			String path = "";
			String url = "";
	    	Random r=new Random();
	    	nombreArchivo="InconBDXML"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+r.nextInt();
	    	path=ValoresPorDefecto.getFilePath()+System.getProperty("file.separator");
	    	url="../upload"+System.getProperty("file.separator");
	    	// se cargan los datos requeridos para el informe
	    	DtoInformeInconsisenBD dto = RegistroEnvioInformInconsisenBD.getInformeInconsistencias(con, cod_informe_inco);
	    	InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		    EstructuraInformeUrgencias informeIncosistencia = new EstructuraInformeUrgencias();
			
			try 
			{
				generacionArchivoXml(paciente, nombreArchivo, path, dto,institucionBasica, informeIncosistencia,forma.getInformeIconsistencias().getObservaciones());
				
				//****************************************************************************
				// Creacion del archivo .zip y verificacion de lo la existencia de los mismos
				//se genera el archivo en formato Zip
				band=BackUpBaseDatos.EjecutarComandoSO("zip -j "+path+nombreArchivo+".zip -j"+" "+path+nombreArchivo+".xml");
				//se ingresa la direccion donde se almaceno el archivo
				forma.getInformeIconsistencias().setPathArchivoIncoXml(path+nombreArchivo+".xml");
				//se valida si existe el xml
				exist=UtilidadFileUpload.existeArchivo(path, nombreArchivo+".xml");
				//se valida si existe el zip
				exist=UtilidadFileUpload.existeArchivo(path, nombreArchivo+".zip");
				// Creacion del archivo .zip y verificacion de lo la existencia de los mismos
				//****************************************************************************
				if(exist)
					return nombreArchivo+".zip";
				else
					return null;
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		    
		}
    	return null;
    }
    
    /**
     * 
     * @param paciente
     * @param nombreArchivo
     * @param path
     * @param dto
     * @param institucionBasica
     * @param informeIncosistencia
     * @throws DatatypeConfigurationException
     */
	private static void generacionArchivoXml(PersonaBasica paciente,
			String nombreArchivo, String path, DtoInformeInconsisenBD dto,
			InstitucionBasica institucionBasica,
			EstructuraInformeUrgencias informeIncosistencia,
			String observaciones)
			throws DatatypeConfigurationException {
		//******************************************************************
		// Paciente
		NombreCompleto nomcompleto = new NombreCompleto();
		nomcompleto.setPrimerApellido(dto.getPrimerApellido());
		nomcompleto.setSegundoApellido(dto.getSegundoApellido());
		nomcompleto.setPrimerNombre(dto.getPrimerNombre());
		nomcompleto.setSegundoNombre(dto.getSegundoNombre());
		
		Identificacion identificacion = new Identificacion();
		identificacion.setNumeroIdentificacion(dto.getIdPersona());
		identificacion.setTipoIdentificacion(dto.getTipoIdentificacion());
		
		UbicacionGeografica ubicacionGeo = new UbicacionGeografica();
		ubicacionGeo.setDepartamento(dto.getDepartamento()+"("+dto.getCodigoDepartamento()+")");
		ubicacionGeo.setDireccionResidenciaHabitual(dto.getDireccionResidencia());
		ubicacionGeo.setMunicipio(dto.getMunicipio()+"("+dto.getCodigoMunicipio()+")");
		try{
			if(Utilidades.convertirAEntero(dto.getTelefono())!=ConstantesBD.codigoNuncaValido)
				ubicacionGeo.setTelefonoFijo(Utilidades.convertirAEntero(paciente.getTelefono()));
			else
				ubicacionGeo.setTelefonoFijo(0);
		}catch (Exception e) {
			ubicacionGeo.setTelefonoFijo(0);
		}
		
		XMLGregorianCalendar fecha;
		fecha = DatatypeFactory.newInstance().newXMLGregorianCalendar();
		String fechanaci[] = dto.getFechaNacimiento().split("-");
		if(fechanaci.length>0)
		{
			fecha.setDay(Utilidades.convertirAEntero(fechanaci[2].toString()));
			fecha.setMonth(Utilidades.convertirAEntero(fechanaci[1].toString()));
			fecha.setYear(Utilidades.convertirAEntero(fechanaci[0].toString()));
		}
		
		InformacionPersonal infoPersonal = new InformacionPersonal();
		infoPersonal.setFechaNacimiento(fecha);
		infoPersonal.setUbicacion(ubicacionGeo);
		
		Paciente pacInformeInco = new Paciente();
		pacInformeInco.setDatosPersonales(infoPersonal);
		pacInformeInco.setIdentificacion(identificacion);
		pacInformeInco.setNombre(nomcompleto);
		// Fin Paciente
		//******************************************************************
		
		//******************************************************************
		// Pagador
		Pagador pagador = new Pagador();
		Iterator iter = dto.getConveniosPaciente().iterator();
		iter.hasNext();InfoDatosString datos = (InfoDatosString) iter.next();
		pagador.setCodigoEntidad(datos.getCodigo());
		pagador.setEntidadResponsable(datos.getNombre());
		// Fin Pagador
		//******************************************************************
		
		//******************************************************************
		// Persona Reportante
		PersonaReportante perRepor = new PersonaReportante();
		perRepor.setNombre(dto.getUsuarioGeneracion());
		perRepor.setCargo(dto.getCargo());
		perRepor.setCelularInstitucional(dto.getTelefonoPer());
		try{
			perRepor.setTelefono(Utilidades.convertirAEntero(institucionBasica.getTelefono()));
			try{
				perRepor.setExtTele(Utilidades.convertirAEntero(institucionBasica.getExtension()));
			}catch (Exception e) {
				perRepor.setExtTele(0);
			}
			try{
				perRepor.setIndicaTel(Utilidades.convertirAEntero(institucionBasica.getIndicativo()));
			}catch (Exception e) {
				perRepor.setIndicaTel(0);
			}
		}catch (Exception e) {
			perRepor.setTelefono(0);
			perRepor.setExtTele(0);
			perRepor.setIndicaTel(0);
		}
		
		// Fin Persona Reportante
		//******************************************************************
		
		
		//******************************************************************
		// General
		
		General general = new General();
		general.setNumero(Utilidades.convertirAEntero(dto.getConsecutivoIngreso()));
		
		XMLGregorianCalendar fechaGeneral;
		fechaGeneral = DatatypeFactory.newInstance().newXMLGregorianCalendar();
		String fechaAux[] = UtilidadFecha.conversionFormatoFechaAAp(dto.getFechaGeneracion()).split("/");
		fechaGeneral.setDay(Utilidades.convertirAEntero(fechaAux[0]));
		fechaGeneral.setMonth(Utilidades.convertirAEntero(fechaAux[1]));
		fechaGeneral.setYear(Utilidades.convertirAEntero(fechaAux[2]));
		general.setFecha(fechaGeneral);
		
		XMLGregorianCalendar horaGeneral;
		horaGeneral = DatatypeFactory.newInstance().newXMLGregorianCalendar();
		String horaAux[] = dto.getHoraGeneracion().split(":");
		horaGeneral.setTime(Utilidades.convertirAEntero(horaAux[0]), Utilidades.convertirAEntero(horaAux[1]), 0);
		general.setHora(horaGeneral);
		
		general.setPrestador(institucionBasica.getRazonSocial());
		if(institucionBasica.getTipoIdentificacion().equals("NI")){
			general.setTipoIdPrestador(institucionBasica.getDescripcionTipoIdentificacion());
			general.setIDPrestador(institucionBasica.getNit());
			general.setDigVerif(Utilidades.convertirAEntero(institucionBasica.getDigitoVerificacion()));
		}else{
			general.setTipoIdPrestador(institucionBasica.getTipoIdentificacion());
			general.setIDPrestador(institucionBasica.getNit());
			general.setDigVerif(0);
		}
		
		general.setCodPrestador(institucionBasica.getCodMinsalud());
		general.setDireccionPrestador(institucionBasica.getDireccion());
		try{
			general.setIndicTelefPrestador(Utilidades.convertirAEntero(institucionBasica.getIndicativo()));
		}catch (Exception e) {general.setIndicTelefPrestador(0);}
		try{
			general.setTelefonoPrestador(Utilidades.convertirAEntero(institucionBasica.getTelefono()));
		}catch (Exception e) {general.setTelefonoPrestador(0);}
		
		general.setDepartamentoPrestador(institucionBasica.getDepto()+" ("+institucionBasica.getCodigoDepto()+")");
		general.setMunicipioPrestador(institucionBasica.getCiudad()+" ("+institucionBasica.getCodigoCiudad()+")");
		// Fin General
		//******************************************************************
		
		//******************************************************************
		// Inconsistencias
		for(int i=0; i<7;i++)
		{
 			Inconsistencia inconsitencias = new Inconsistencia();
 			switch (i) {
			case 0:
				inconsitencias.setVariableInconsistencia(VariableInconsistencia.PRIMER_APELLIDO);
				if(dto.getPrimerApellido().compareTo(paciente.getPrimerApellido())==0){
					inconsitencias.setDatoDocumento("");
					inconsitencias.setDatoErrado("No");
				}else{
					inconsitencias.setDatoDocumento(paciente.getPrimerApellido());
					inconsitencias.setDatoErrado("Si");
				}
				break;
			case 1:
				inconsitencias.setVariableInconsistencia(VariableInconsistencia.SEGUNDO_APELLIDO);
				if(dto.getSegundoApellido().compareTo(paciente.getSegundoApellido())==0){
					inconsitencias.setDatoDocumento("");
					inconsitencias.setDatoErrado("No");
				}else{
					inconsitencias.setDatoDocumento(paciente.getSegundoApellido());
					inconsitencias.setDatoErrado("Si");
				}
				break;
			case 2:
				inconsitencias.setVariableInconsistencia(VariableInconsistencia.PRIMER_NOMBRE);
				if(dto.getPrimerNombre().compareTo(paciente.getPrimerNombre())==0){
					inconsitencias.setDatoDocumento("");
					inconsitencias.setDatoErrado("No");
				}else{
					inconsitencias.setDatoDocumento(paciente.getPrimerNombre());
					inconsitencias.setDatoErrado("Si");
				}
				break;
			case 3:
				inconsitencias.setVariableInconsistencia(VariableInconsistencia.SEGUNDO_NOMBRE);
				if(dto.getSegundoNombre().compareTo(paciente.getSegundoNombre())==0){
					inconsitencias.setDatoDocumento("");
					inconsitencias.setDatoErrado("No");
				}else{
					inconsitencias.setDatoDocumento(paciente.getSegundoNombre());
					inconsitencias.setDatoErrado("Si");
				}
				break;
			case 4:
				inconsitencias.setVariableInconsistencia(VariableInconsistencia.TIPO_IDENTIFICACION);
				if(dto.getTipoIdentificacion().compareTo(paciente.getTipoIdentificacionPersona())==0){
					inconsitencias.setDatoDocumento("");
					inconsitencias.setDatoErrado("No");
				}else{
					inconsitencias.setDatoDocumento(paciente.getTipoIdentificacionPersona());
					inconsitencias.setDatoErrado("Si");
				}
				break;
			case 5:
				inconsitencias.setVariableInconsistencia(VariableInconsistencia.NUMERO_IDENTIFICACION);
				if(dto.getIdPersona().compareTo(paciente.getNumeroIdentificacionPersona())==0){
					inconsitencias.setDatoDocumento("");
					inconsitencias.setDatoErrado("No");
				}else{
					inconsitencias.setDatoDocumento(paciente.getNumeroIdentificacionPersona());
					inconsitencias.setDatoErrado("Si");
				}
				break;
			case 6:
				inconsitencias.setVariableInconsistencia(VariableInconsistencia.FECHA_NACIMIENTO);
				if(UtilidadFecha.conversionFormatoFechaAAp(dto.getFechaNacimiento()).compareTo(UtilidadFecha.conversionFormatoFechaAAp(paciente.getFechaNacimiento()))==0){
					inconsitencias.setDatoDocumento("");
					inconsitencias.setDatoErrado("No");
				}else{
					inconsitencias.setDatoDocumento(paciente.getFechaNacimiento());
					inconsitencias.setDatoErrado("Si");
				}
				break;
				
			
			}
 			informeIncosistencia.getInconsistencias().add(inconsitencias);
		}	
		
		
		// Fin Inconsistencias
		//******************************************************************
		
		//forma.getInformeIconsistencias().getCobertura()
		informeIncosistencia.setCoberturaSalud(dto.getCobertura());
		informeIncosistencia.setObservaciones(observaciones);
		informeIncosistencia.setGeneral(general);
		informeIncosistencia.setPaciente(pacInformeInco);
		informeIncosistencia.setPagador(pagador);
		informeIncosistencia.setReportante(perRepor);
		
		
		UtilidadAutorizacionesEDI.generarArchivoEDIInformePresuntaInconsistencia(path+nombreArchivo+".xml", informeIncosistencia);
	}
}
