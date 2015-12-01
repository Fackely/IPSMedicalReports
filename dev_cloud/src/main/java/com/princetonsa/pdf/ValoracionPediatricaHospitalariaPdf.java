/*
 * ValoracionPediatricaHospitalariaPdf.java 
 * Autor			:  coviedo
 * Creado el	:  20-oct-2004
 * 
 * Lenguaje		: Java
 * Compilador	: J2SDK 1.4.1_01
 * 
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 */
package com.princetonsa.pdf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.atencion.Diagnostico;
import com.princetonsa.mundo.atencion.SignoVital;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;
import util.pdf.iTextBaseTable;

/**
 * Clase para manejar la generación de PDF's para las valoraciones pedi&aacute;tricas hospitalarias.
 *
 * @version 1.0, 20-oct-2004
 * @author <a href="mailto:carlos@princetonsa.com">Carlos Oviedo</a>
 */
public class ValoracionPediatricaHospitalariaPdf {

	private static Logger logger=Logger.getLogger(ValoracionPediatricaHospitalariaPdf.class);
	
	/**
	 * @param filename nombre del archivo pdf a generar.
	 * @param datos mapa de datos cuyas llaves son las siguientes:
	 */
	public static void pdfValoracionPediatricaHospitalaria(String filename, HashMap datos, PersonaBasica paciente, UsuarioBasico usuario) 
	{	
        //Verificar que se hayan suministrado los datos
		if (filename != null && filename.length() > 0 && datos != null && datos.size() > 0) 
		{
			PdfReports report = new PdfReports();
			
			//Obtener datos del mapa
			String institucion = (String) datos.get("institucion");
		    if (institucion == null)
		    {
		    	logger.error("Valor \"institucion\" llegó nulo");
		    	return;
		    }
		    String nitInstitucion = (String) datos.get("nitInstitucion");
			if (nitInstitucion == null)
			{
				logger.error("Valor \"nit\" llegó nulo");
				return;
			}
			
            //Armar el encabezado
			InstitucionBasica institucionBasica= new InstitucionBasica();
	        institucionBasica.cargarXConvenio(usuario.getCodigoInstitucionInt(), paciente.getCodigoConvenio());
			
	        report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), "VALORACION DEL PACIENTE");
	        
	        //Abrir reporte y establecer propiedades
	        report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
		    
		    report.openReport(ValoresPorDefecto.getFilePath() +filename);
		    
		    generarCuerpoValoracion(report, false, datos);
		    
		    //Cerrar el reporte
		    report.closeReport();
		}
	}
	
	public static void generarCuerpoValoracion(PdfReports report, boolean printEpicrisis, HashMap datos) 
	{
    report.font.setFontAttributes(0x000000, 11, true, false, false);
    
    report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
      
    report.setReportTitleSectionAtributes(0xFFFFFF, 0xFFFFFF,0x000000, 11);
    report.setReportDataSectionAtributes(0xFFFFFF, 0xFFFFFF, 0x000000, 11);
    
        //Se adicionan los datos al documento
    String[] sectionData = new String[5];
    
        //Seccion: Informacion Personal ---------------------------------------------------------------------------------------
    String apellidosNombres = (String)datos.get("apellidosNombres");
    String textData = (apellidosNombres != null ? "Apellidos y Nombres: " + apellidosNombres : "Apellidos y Nombres: ");
    sectionData[0] = textData;
    
    String tipoId = (String)datos.get("tipoIdentificacion");
    String numeroId = (String)datos.get("numeroIdentificacion");
    String ciudadId = (String)datos.get("ciudadIdentificacion");
    textData = (tipoId != null && numeroId != null && ciudadId != null ? tipoId + " No. " + numeroId + " De: " + ciudadId : "Sin identificacion");
    sectionData[1] = textData;
    
    String edad = (String)datos.get("edad");
    String sexo = (String) datos.get("sexo");
    textData = (edad != null ? "   Edad: " + edad: "   Edad: ");
    
    if(sexo != null && !sexo.equals(""))
    {
    	textData  +=  "   Sexo: " +sexo; 
    }

    sectionData[1] += textData;
    
    /*String ciudad = (String)datos.get("ciudadResidencia");
    String barrio = (String)datos.get("barrioResidencia");
    textData = (ciudad != null ? "Ciudad y Barrio Residencia: " + ciudad + "-" : "Ciudad y Barrio Residencia: " + "-");
    if (barrio != null) textData += barrio;
    sectionData[3] = textData;*/
    
    String cama = (String)datos.get("numeroCama");
    textData = (cama != null ? "Cama: " + cama + " ": "Cama: " + " ");
    String descripcionCama = (String)datos.get("descripcionCama");
    if (descripcionCama != null && descripcionCama.length() > 0 ) textData += descripcionCama;
    sectionData[2] = textData;
    
    String area = (String)datos.get("area");
    textData = (area != null && area.length() > 0 ? "   Area: " + area : "   Area: ");
    sectionData[2] += textData;
    
    

    if( printEpicrisis == true){
    String infoIngresoEpicrisis = "Servicio de Ingreso: " +datos.get("servicioIngreso") + "   " +"Fecha Ingreso: " +datos.get("fechaIngreso");
    String infoEgresoEpicrisis = "";

    if(((datos.get("fechaEgreso")+"").equals("")))
    		infoEgresoEpicrisis = "";
    else
        infoEgresoEpicrisis = "Servicio de Egreso: " +datos.get("servicioEgreso") + "   " +"Fecha Egreso: " +datos.get("fechaEgreso");
    
    sectionData[3] = infoIngresoEpicrisis;
    
    if(((datos.get("fechaEgreso")+"").equals("")))
    		sectionData[4] = infoEgresoEpicrisis;
    
    report.createSection("infoPersonal", "infoPersonalTable", 6, 1, 10);
    }
    else{
    	sectionData[3] = null;
    	sectionData[4] = null;
    	report.createSection("infoPersonal", "infoPersonalTable", 4, 1, 10);
    }
    
    report.addSectionTitle("infoPersonal", "infoPersonalTable", "Información Personal");
    report.addSectionData("infoPersonal", "infoPersonalTable", sectionData);
    report.addSectionToDocument("infoPersonal");
    
    //Seccion: Información del Ingreso -------------------------------------------------------------------------------------
    String fechaAdmision = (String) datos.get("fechaAdmision");
  	String horaAdmision = (String) datos.get("horaAdmision");
  	String fechaValoracion = (String) datos.get("fechaValoracion");
  	String horaValoracion = (String) datos.get("horaValoracion");
  	String motivoConsulta = (String) datos.get("motivoConsulta");
  	
  	boolean hayDatos = ((fechaAdmision != null && fechaAdmision.length() > 0) &&
  			(horaAdmision != null && horaAdmision.length() >0) &&
			(fechaValoracion != null && fechaValoracion.length() > 0) &&
			(horaValoracion != null && horaValoracion.length() > 0) &&
			motivoConsulta != null && motivoConsulta.length() > 0);
    
  	if (hayDatos) 
  	{
  		sectionData = new String[3];
  		int index = 0;
  		
  		//Fecha de Admision
  		if (fechaAdmision != null && fechaAdmision.length() > 0) 
  		{
  			sectionData[index] = "Fecha Admisión: " + fechaAdmision;
  			if (horaAdmision != null && horaAdmision.length() >0) sectionData[index] += " " + horaAdmision;
  			index++;
  		}
  		
  		//Fecha Valoración
  		if (fechaValoracion != null && fechaValoracion.length() > 0) 
  		{
  			sectionData[index] = "Fecha Valoración: " + fechaValoracion;
  			if (horaValoracion != null && horaValoracion.length() > 0) 
  				sectionData[index] += " " + UtilidadFecha.convertirHoraACincoCaracteres(horaValoracion);
  			index++;
  		}
  		
  		//Motivo Consulta y Enfermedad
  		if (motivoConsulta != null && motivoConsulta.length() > 0) {
  			sectionData[index] = "Motivo Consulta y Enfermedad Actual: " + motivoConsulta;
  			index++;
  		}
  		
  		//Rellenar de espacios sectionData (si se necesita)
  		while (index < 3) 
  		{
  			sectionData[index] = "    ";
  			index++;
  		}
  		
  		iTextBaseTable section = report.createSection("infoIngreso", "infoIngresoTable", 3, 2, 7);
	    report.addSectionTitle("infoIngreso", "infoIngresoTable", "Información del Ingreso");
	    report.font.setFontAttributes(0x000000, 11, false, false, false);
	    section.addTableTextCell("infoIngresoTable", sectionData[0], report.font, 1, 0);
	    section.addTableTextCell("infoIngresoTable", sectionData[1], report.font, 1, 1);
	    section.setTableCellsColSpan(2);
	    section.addTableTextCell("infoIngresoTable", sectionData[2], report.font, 2, 0);
	    section.setTableCellsColSpan(1);
	    report.font.setFontAttributes(0x000000, 11, true, false, false);
	    report.addSectionToDocument("infoIngreso");
  	}
  	
  	//Antecedentes Pediatricos -----------------------------------------------------------------------------------------------------
  	iTextBaseTable section = null;
  	int globalRow = 1;
  	
  	//Seccion: Información Materna ------------------------------------------------------------------------------------------------
  	boolean cabeceraAntecedentesAgregada = (section != null);
  	String primerApellidoMadre = (String) datos.get("primerApellidoMadre");
  	String primerNombreMadre = (String) datos.get("primerNombreMadre");
  	hayDatos = ((primerApellidoMadre != null && primerApellidoMadre.length() >0) &&
  			(primerNombreMadre != null && primerNombreMadre.length() > 0));
  	
  	if (hayDatos) 
  	{
  		//Si no se ha agregado la cabecera de los antecedentes
  		if (!cabeceraAntecedentesAgregada) {
  			//report.document.addParagraph("Antecedentes Pediatricos", report.font, 30);
  			section = report.createSection("antecedentesPediatricos", "antecedentesPediatricosTable", 2, 1, 10);
    		report.font.setFontAttributes(0x000000, 11, true, false, false);
    		report.addSectionTitle("antecedentesPediatricos", "antecedentesPediatricosTable", "Antecedentes Pediátricos");
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
  			cabeceraAntecedentesAgregada = true;
  		}
  		
  		String segundoApellidoMadre = (String) datos.get("segundoApellidoMadre");
  		String segundoNombreMadre = (String) datos.get("segundoNombreMadre");
  		String tipoIdMadre = (String) datos.get("tipoIdMadre");
  		String numeroIdMadre = (String) datos.get("numeroIdMadre");
  		Integer edadGestacion = (Integer) datos.get("edadGestacion");
  		String fur = (String) datos.get("fur");
  		String fpp = (String) datos.get("fpp");
  		String resumenEmbarazoG = (String) datos.get("resumenEmbarazoG");
  		String resumenEmbarazoP = (String) datos.get("resumenEmbarazoP");
  		String resumenEmbarazoA = (String) datos.get("resumenEmbarazoA");
  		String resumenEmbarazoC = (String) datos.get("resumenEmbarazoC");
  		String resumenEmbarazoV = (String) datos.get("resumenEmbarazoV");
  		String resumenEmbarazoM = (String) datos.get("resumenEmbarazoM");
  		Float semanasGestacion = (Float) datos.get("semanasGestacion");
  		String confiable = (String) datos.get("confiable");
  		String tipoSangre = (String) datos.get("tipoSangre");
  		
  		boolean hayEdadGestacion = (edadGestacion != null);
  		boolean hayFUR = (fur != null && fur.length() > 0);
  		boolean hayFPP = (fpp != null && fpp.length() > 0);
  		boolean hayG = (resumenEmbarazoG != null && resumenEmbarazoG.length() > 0);
  		boolean hayP = (resumenEmbarazoP != null && resumenEmbarazoP.length() > 0);
  		boolean hayA = (resumenEmbarazoA != null && resumenEmbarazoA.length() > 0);
  		boolean hayC = (resumenEmbarazoC != null && resumenEmbarazoC.length() > 0);
  		boolean hayV = (resumenEmbarazoV != null && resumenEmbarazoV.length() > 0);
  		boolean hayM = (resumenEmbarazoM != null && resumenEmbarazoM.length() > 0);
  		boolean haySemanasGestacion = (semanasGestacion != null);
  		boolean hayConfiable = (confiable != null && confiable.length() > 0);
  		boolean hayTipoSangre = (tipoSangre != null);
  		
  		int row = 1, column = 0;
  		
  		//iTextBaseTable section = report.createSection("infoMaterna", "infoMaternaTable", 5, 3, 2);
  		report.createSectionTable("antecedentesPediatricos", "infoMaternaTable", 5, 6);
  		report.font.setFontAttributes(0x000000, 11, true, false, false);
  		section.setTableBorder("infoMaternaTable", 0xFFFFFF, 0.0f);
	    section.setTableCellBorderWidth("infoMaternaTable", 0.5f);
	    section.setTableCellPadding("infoMaternaTable", 1);
	    section.setTableSpaceBetweenCells("infoMaternaTable", 0.5f);
	    section.setTableCellsDefaultColors("infoMaternaTable", 0xFFFFFF, 0xFFFFFF);
  		report.addSectionTitle("antecedentesPediatricos", "infoMaternaTable", "Información Materna");
	    report.font.setFontAttributes(0x000000, 11, false, false, false);
	    
	    //Apellidos y Nombres
	    section.setTableCellsColSpan(3);
	    textData = "Apellidos y Nombres: " + primerApellidoMadre + " ";
	    if (segundoApellidoMadre != null && segundoApellidoMadre.length() > 0) textData += segundoApellidoMadre + " ";
	    textData +=  primerNombreMadre + " ";
	    if (segundoNombreMadre != null && segundoNombreMadre.length() > 0) textData += segundoNombreMadre;
	    section.addTableTextCellAligned("infoMaternaTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, column);
	    column += 3;
	    
	    //Identificacion
	    textData = tipoIdMadre + " " + numeroIdMadre;
	    section.addTableTextCellAligned("infoMaternaTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, column);
	    column = 0;
	    row++;
	    
	    //Edad al momento de gestacion
	    if (hayEdadGestacion) 
	    {
	    	//section.setTableCellsColSpan(2);
	    	textData = "Edad al momento de la Gestación: " + edadGestacion.toString() + " años";
	    	section.addTableTextCellAligned("infoMaternaTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, column);
	    	//section.setTableCellsColSpan(1);
	    	column += 3;
	    }
	    
	    //F.U.R.
	    if (hayFUR) 
	    {
	    	textData = "F.U.R: " + fur;
                //F.P.P.
		    if (hayFPP) 
		    	textData += "     F.P.P: " + fpp;
	    	section.addTableTextCellAligned("infoMaternaTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, column);
	    	column+=3;
	    }
	    
	    //Chequear que columnas fueron llenadas de la 2nda fila
	    if (column > 0) 
	    {
	    	if (column < 6) 
	    	{
	    		section.addTableTextCellAligned("infoMaternaTable", "    ", report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, column);
	    		column+=3;
	    	}
	    	row++;
	    }
	    column = 0;
	    
	    //Resumen Embarazos
	    if (hayG || hayP || hayA || hayC || hayV || hayM) {
	    	section.setTableCellsColSpan(6);
	    	textData = "Resumen Embarazos:     ";
	    	if (hayG) textData += "G " + resumenEmbarazoG + "     ";
	    	if (hayP) textData += "P " + resumenEmbarazoP + "     ";
	    	if (hayA) textData += "A " + resumenEmbarazoA + "     ";
	    	if (hayC) textData += "C " + resumenEmbarazoC + "     ";
	    	if (hayV) textData += "V " + resumenEmbarazoV + "     ";
	    	if (hayM) textData += "M " + resumenEmbarazoM;
	    	section.addTableTextCellAligned("infoMaternaTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, column);
	    	section.setTableCellsColSpan(2);
	    	row++;
	    }
	    
	    //Semanas Gestación
	    if (haySemanasGestacion) {
	    	textData = "Semanas Gestación: " + semanasGestacion.toString();
	    	section.addTableTextCellAligned("infoMaternaTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, column);
	    	column+=2;
	    }
	    
	    //Confiable
	    if (hayConfiable) {
	    	textData = "Confiable: " + confiable;
	    	section.addTableTextCellAligned("infoMaternaTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, column);
	    	column+=2;
	    }
	    
	    //Tipo Sangre
	    if (hayTipoSangre) {
	    	textData = "Tipo Sangre: " + tipoSangre;
	    	section.addTableTextCellAligned("infoMaternaTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, column);
	    	column+=2;
	    }
	    
            //Chequear que columnas fueron llenadas de la 4ta fila
	    if (column > 0) {
	    	while (column < 6) 
	    	{
	    		section.addTableTextCell("infoMaternaTable", "    ", report.font, row, column);
	    		column+=2;
	    	}
	    }
	    
	    report.font.setFontAttributes(0x000000, 11, true, false, false);		    			     
	    section.insertTableIntoCell("antecedentesPediatricosTable", "infoMaternaTable", globalRow, 0);
	    globalRow++;
	    
	    //report.addSectionToDocument("antecedentesPediatricos");
	    //report.addSectionToDocument("infoMaterna");
  	}
  	
  	//Preparar seccion: Embarazos Anteriores --------------------------------------------------------------------------------------------------------------------------------------------------------------
  	boolean haySeccionEmbarazosAnteriores = false, embarazosAnterioresInsertados = false;;
  	String incompatibilidadRH = (String) datos.get("incompatibilidadRH");
  	String incompatibilidadABO = (String) datos.get("incompatibilidadABO");
  	String macrosomicos = (String) datos.get("macrosomicos");
  	String malformacionesCongenitas = (String) datos.get("malformacionesCongenitas");
  	String mortinatos = (String) datos.get("mortinatos");
  	String muertesFetales = (String) datos.get("muertesFetales");
  	String prematuros = (String) datos.get("prematuros");
  	String otros = (String) datos.get("otros");
  	
  	boolean hayIncompatibilidadRH = (incompatibilidadRH != null && incompatibilidadRH.length() > 0);
  	boolean hayIncompatibilidadABO = (incompatibilidadABO != null && incompatibilidadABO.length() > 0);
  	boolean hayMacrosomicos = (macrosomicos != null && macrosomicos.length() > 0);
  	boolean hayMalformacionesCongenitas = (malformacionesCongenitas != null && malformacionesCongenitas.length() > 0);
  	boolean hayMortinatos = (mortinatos != null && mortinatos.length() > 0);
  	boolean hayMuertesFetales= (muertesFetales != null && muertesFetales.length() > 0);
  	boolean hayPrematuros = (prematuros != null && prematuros.length() > 0);
  	boolean hayOtros = (otros != null && otros.length() > 0);
  	
  	haySeccionEmbarazosAnteriores = (hayIncompatibilidadRH || hayIncompatibilidadABO || hayMacrosomicos || hayMalformacionesCongenitas || hayMortinatos ||
  			hayMuertesFetales || hayPrematuros || hayOtros);
  	
  	//Seccion: Información Paterna ---------------------------------------------------------------------------------------------------
  	String primerApellidoPadre = (String) datos.get("primerApellidoPadre");
  	String primerNombrePadre = (String) datos.get("primerNombrePadre");
  	hayDatos = ((primerApellidoPadre != null && primerApellidoPadre.length() >0) &&
  			(primerNombrePadre != null && primerNombrePadre.length() > 0));
  	
  	if (hayDatos) {
            //Si no se ha agregado la cabecera de los antecedentes
  		if (!cabeceraAntecedentesAgregada) {
  			//report.document.addParagraph("Antecedentes Pediatricos", report.font, 30);
  			section = report.createSection("antecedentesPediatricos", "antecedentesPediatricosTable", 2, 1, 10);
    		report.font.setFontAttributes(0x000000, 11, true, false, false);
    		report.addSectionTitle("antecedentesPediatricos", "antecedentesPediatricosTable", "Antecedentes Pediátricos");
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
  			cabeceraAntecedentesAgregada = true;
  		}
  		
  		String segundoApellidoPadre = (String) datos.get("segundoApellidoPadre");
  		String segundoNombrePadre = (String) datos.get("segundoNombrePadre");
  		String tipoIdPadre = (String) datos.get("tipoIdPadre");
  		String numeroIdPadre = (String) datos.get("numeroIdPadre");
  		Integer edadPadre = (Integer) datos.get("edadPadre");
  		String cosanguinidad = (String) datos.get("cosanguinidad");
  		String tipoSangrePadre = (String) datos.get("tipoSangrePadre");
  		
  		boolean hayEdadPadre = (edadPadre != null);
  		boolean hayCosanguinidad = (cosanguinidad != null && cosanguinidad.length() > 0);
  		boolean hayTipoSangrePadre = (tipoSangrePadre != null);
  		
  		int row = 1, column = 0;
  		
  		report.createSectionTable("antecedentesPediatricos", "infoPaternaTable", 3, 6);
  		report.font.setFontAttributes(0x000000, 11, true, false, false);
  		section.setTableBorder("infoPaternaTable", 0xFFFFFF, 0.0f);
	    section.setTableCellBorderWidth("infoPaternaTable", 0.5f);
	    section.setTableCellPadding("infoPaternaTable", 1);
	    section.setTableSpaceBetweenCells("infoPaternaTable", 0.5f);
	    section.setTableCellsDefaultColors("infoPaternaTable", 0xFFFFFF, 0xFFFFFF);
  		report.addSectionTitle("antecedentesPediatricos", "infoPaternaTable", "Información Paterna");
	    report.font.setFontAttributes(0x000000, 11, false, false, false);
	    section.setTableBorderColor("infoPaternaTable", 0xFFFFFF);
  		
  		/*iTextBaseTable section = report.createSection("infoPaterna", "infoPaternaTable", 3, 3, 5);
  		report.font.setFontAttributes(0x000000, 11, true, false, false);
  		report.addSectionTitle("infoPaterna", "infoPaternaTable", "Información Paterna");
	    report.font.setFontAttributes(0x000000, 11, false, false, false);*/
	    
	    //Apellidos y Nombres
	    section.setTableCellsColSpan(3);
	    textData = "Apellidos y Nombres: "+ primerApellidoPadre + " ";
	    if (segundoApellidoPadre != null && segundoApellidoPadre.length() > 0) textData += segundoApellidoPadre + " ";
	    textData += primerNombrePadre + " ";
	    if (segundoNombrePadre != null && segundoNombrePadre.length() > 0) textData += segundoNombrePadre;
	    section.addTableTextCellAligned("infoPaternaTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, column);
	    
	    column += 3;
	    
            //Identificacion
	    textData = tipoIdPadre + " " + numeroIdPadre;
	    section.addTableTextCellAligned("infoPaternaTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, column);
	    column = 0;
	    row++;
	    
	    section.setTableCellsColSpan(2);
	    //Edad 
	    if (hayEdadPadre) {
	    	textData = "Edad: " + edadPadre.toString() + " años";
	    	section.addTableTextCellAligned("infoPaternaTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, column);
	    	column+=2;
	    }
	    
	    //Cosanguinidad
	    if (hayCosanguinidad) {
	    	textData = "Cosanguinidad: " + cosanguinidad;
	    	section.addTableTextCellAligned("infoPaternaTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, column);
	    	column+=2;
	    }
	    
	    //Tipo Sangre
	    if (hayTipoSangrePadre) {
	    	textData = "Tipo Sangre: " + tipoSangrePadre;
	    	section.addTableTextCellAligned("infoPaternaTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, column);
	    	column+=2;
	    }
	    
            //Chequear que columnas fueron llenadas de la 2nda fila
	    if (column > 0) {
	    	while (column < 6) {
	    		section.addTableTextCell("infoPaternaTable", "    ", report.font, row, column);
	    		column+=2;
	    	}
	    	row++;
	    }
	    
	    if (haySeccionEmbarazosAnteriores) {
	    	int numRows = 0, data = 0;
    		
    		if (hayIncompatibilidadRH) data++;
    	    if (hayIncompatibilidadABO) data++;
    	    if (hayMacrosomicos) data++;
    	    if (hayMalformacionesCongenitas) data++;
    	    if (hayMortinatos) data++;
    	    if (hayMuertesFetales) data++;
    	    if (hayPrematuros) data++;
    	    if (hayOtros) data++;
    	    
    	    if (data%4 != 0) numRows += 1;
    	    numRows += (data/4);
    		
    		textData = "Embarazos Anteriores:";
    		boolean hayEmbarazosAnteriores = false;
    		//Incompatibilidad RH
		    if (hayIncompatibilidadRH) {
    	    	textData += " Incompatibilidad RH";
    	    	hayEmbarazosAnteriores = true;
    	    }
		    //Incompatibilidad ABO
		    if (hayIncompatibilidadABO) {
		    	if (hayEmbarazosAnteriores) textData += ",";
		    	textData += " Incompatibilidad ABO";
    	    	hayEmbarazosAnteriores = true;
		    }
		    //Macrosomicos
		    if (hayMacrosomicos) {
		    	if (hayEmbarazosAnteriores) textData += ",";
		    	textData += " Macrosómicos";
    	    	hayEmbarazosAnteriores = true;
		    }
		    //Malformaciones congenitas
		    if (hayMalformacionesCongenitas) {
		    	if (hayEmbarazosAnteriores) textData += ",";
		    	textData += " Malformaciones Congénitas";
    	    	hayEmbarazosAnteriores = true;
		    }
		    //Mortinatos
		    if (hayMortinatos) {
		    	if (hayEmbarazosAnteriores) textData += ",";
		    	textData += " Mortinatos";
    	    	hayEmbarazosAnteriores = true;
		    }
		    
		    //Muertes fetales
		    if (hayMuertesFetales) {
		    	if (hayEmbarazosAnteriores) textData += ",";
		    	textData += " Muertes Fetales Tempranas";
    	    	hayEmbarazosAnteriores = true;
		    }
		    
		    //Prematuros
		    if (hayPrematuros) {
		    	if (hayEmbarazosAnteriores) textData += ",";
		    	textData += " Prematuros";
    	    	hayEmbarazosAnteriores = true;
		    }
		    
		    //Otros
		    if (hayOtros) {
		    	if (hayEmbarazosAnteriores) textData += ",";
		    	textData += " Otros (" + otros + ")";
    	    	hayEmbarazosAnteriores = true;
		    }
		    //Agregar seccion "Embarazos Anteriores" como parte de la tabla "Informacion Paterna" (para ahorrar espacio) ---------------------------------------------------------
    		if (hayEmbarazosAnteriores) 
    		{
			    section.setTableCellsColSpan(6);
			    section.addTableTextCellAligned("infoPaternaTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, 0);
			    embarazosAnterioresInsertados = true;
			    section.setTableCellsColSpan(3);
			    //report.font.setFontAttributes(0x000000, 11, true, false, false);
			    
			    //section.insertTableIntoCell("antecedentesPediatricosTable", "embarazosAnterioresTable", row, 0);
    		}	
	    }
	    
	    report.font.setFontAttributes(0x000000, 11, true, false, false);
	    section.insertTableIntoCell("antecedentesPediatricosTable", "infoPaternaTable", globalRow, 0);
	    globalRow++;
	    //report.addSectionToDocument("infoPaterna");
  	}
  	
  	//Seccion: Embarazos Anteriores  (agregarla ssi no ha sido agregada aún ------------------------------------------------------------------------------------------------------------------
  	if (haySeccionEmbarazosAnteriores && !embarazosAnterioresInsertados) 
  	{
            //Si no se ha agregado la cabecera de los antecedentes
  		if (!cabeceraAntecedentesAgregada) {
  			//report.document.addParagraph("Antecedentes Pediatricos", report.font, 30);
  			section = report.createSection("antecedentesPediatricos", "antecedentesPediatricosTable", 2, 1, 10);
    		report.font.setFontAttributes(0x000000, 11, true, false, false);
    		report.addSectionTitle("antecedentesPediatricos", "antecedentesPediatricosTable", "Antecedentes Pediátricos");
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
  			cabeceraAntecedentesAgregada = true;
  		}
  		
  		int row = 1, numRows = 0, data = 0;
  		
  		if (hayIncompatibilidadRH) data++;
  	    if (hayIncompatibilidadABO) data++;
  	    if (hayMacrosomicos) data++;
  	    if (hayMalformacionesCongenitas) data++;
  	    if (hayMortinatos) data++;
  	    if (hayMuertesFetales) data++;
  	    if (hayPrematuros) data++;
  	    if (hayOtros) data++;
  	    
  	    if (data%4 != 0) numRows += 1;
  	    numRows += (data/4);
  		
  		textData = "Embarazos Anteriores:";
  		boolean hayEmbarazosAnteriores = false;
  		//Incompatibilidad RH
	    if (hayIncompatibilidadRH) {
  	    	textData += " Incompatibilidad RH";
  	    	hayEmbarazosAnteriores = true;
  	    }
	    //Incompatibilidad ABO
	    if (hayIncompatibilidadABO) {
	    	if (hayEmbarazosAnteriores) textData += ",";
	    	textData += " Incompatibilidad ABO";
  	    	hayEmbarazosAnteriores = true;
	    }
	    //Macrosomicos
	    if (hayMacrosomicos) {
	    	if (hayEmbarazosAnteriores) textData += ",";
	    	textData += " Macrosómicos";
  	    	hayEmbarazosAnteriores = true;
	    }
	    //Malformaciones congenitas
	    if (hayMalformacionesCongenitas) {
	    	if (hayEmbarazosAnteriores) textData += ",";
	    	textData += " Malformaciones Congénitas";
  	    	hayEmbarazosAnteriores = true;
	    }
	    //Mortinatos
	    if (hayMortinatos) {
	    	if (hayEmbarazosAnteriores) textData += ",";
	    	textData += " Mortinatos";
  	    	hayEmbarazosAnteriores = true;
	    }
	    
	    //Muertes fetales
	    if (hayMuertesFetales) {
	    	if (hayEmbarazosAnteriores) textData += ",";
	    	textData += " Muertes Fetales Tempranas";
  	    	hayEmbarazosAnteriores = true;
	    }
	    
	    //Prematuros
	    if (hayPrematuros) {
	    	if (hayEmbarazosAnteriores) textData += ",";
	    	textData += " Prematuros";
  	    	hayEmbarazosAnteriores = true;
	    }
	    
	    //Otros
	    if (hayOtros) {
	    	if (hayEmbarazosAnteriores) textData += ",";
	    	textData += " Otros (" + otros + ")";
  	    	hayEmbarazosAnteriores = true;
	    }
  		if (hayEmbarazosAnteriores) {
  			report.createSectionTable("antecedentesPediatricos", "embarazosAnterioresTable", 1, 6);
    		report.font.setFontAttributes(0x000000, 11, false, false, false);
    		section.setTableBorder("embarazosAnterioresTable", 0xFFFFFF, 0.0f);
		    section.setTableCellBorderWidth("embarazosAnterioresTable", 0.5f);
		    section.setTableCellPadding("embarazosAnterioresTable", 1);
		    section.setTableSpaceBetweenCells("embarazosAnterioresTable", 0.5f);
		    section.setTableCellsDefaultColors("embarazosAnterioresTable", 0xFFFFFF, 0xFFFFFF);
    		//report.addSectionTitle("antecedentesPediatricos", "embarazosAnterioresTable", textData);
  			report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableBorderColor("embarazosAnterioresTable", 0xFFFFFF);
		    
		    section.setTableCellsColSpan(6);
		    section.addTableTextCellAligned("embarazosAnterioresTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, 0);
		    section.setTableCellsColSpan(1);
		    report.font.setFontAttributes(0x000000, 11, true, false, false);
		    
		    section.insertTableIntoCell("antecedentesPediatricosTable", "embarazosAnterioresTable", globalRow, 0);
		    globalRow++;
  		}
  	}
  	
  	//Seccion: Embarazo Actual ----------------------------------------------------------------------------------------------------------
  	String controlPrenatal = (String) datos.get("controlPrenatal");
  	String lugar = (String) datos.get("lugar");
  	String frecuencia = (String) datos.get("frecuencia");
  	String serologia = (String) datos.get("serologia");
  	String testSullivan = (String) datos.get("testSullivan");
  	Vector datosCategoriasEmbarazo = (Vector) datos.get("datosCategoriasEmbarazo");
  	String observacionesEmbarazo = (String) datos.get("observacionesEmbarazo");
  	
  	boolean hayControlPrenatal = (controlPrenatal != null && controlPrenatal.length() > 0);
  	boolean hayLugar = (lugar != null && lugar.length() > 0);
  	boolean hayFrecuencia = (frecuencia != null && frecuencia.length() > 0);
  	boolean haySerologia = (serologia != null && serologia.length() > 0);
  	boolean hayTestSullivan = (testSullivan != null && testSullivan.length() > 0);
  	boolean hayCategoriasEmbarazo = (datosCategoriasEmbarazo != null && datosCategoriasEmbarazo.size() > 0);
  	boolean hayObservacionesEmbarazo = (observacionesEmbarazo != null && observacionesEmbarazo.length() > 0);
  	
  	hayDatos = (hayControlPrenatal || hayLugar || hayFrecuencia || haySerologia || hayTestSullivan || hayCategoriasEmbarazo ||
			hayObservacionesEmbarazo);
  	
  	if (hayDatos) {
            //Si no se ha agregado la cabecera de los antecedentes
  		if (!cabeceraAntecedentesAgregada) {
  			//report.document.addParagraph("Antecedentes Pediatricos", report.font, 30);
  			section = report.createSection("antecedentesPediatricos", "antecedentesPediatricosTable", 2, 1, 10);
    		report.font.setFontAttributes(0x000000, 11, true, false, false);
    		report.addSectionTitle("antecedentesPediatricos", "antecedentesPediatricosTable", "Antecedentes Pediátricos");
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
  			cabeceraAntecedentesAgregada = true;
  		}
  		
  		int row = 1, column = 0;
  		
  		report.createSectionTable("antecedentesPediatricos", "embarazoActualTable", 1, 6);
  		report.font.setFontAttributes(0x000000, 11, true, false, false);
  		section.setTableBorder("embarazoActualTable", 0xFFFFFF, 0.0f);
	    section.setTableCellBorderWidth("embarazoActualTable", 0.5f);
	    section.setTableCellPadding("embarazoActualTable", 1);
	    section.setTableSpaceBetweenCells("embarazoActualTable", 0.5f);
	    section.setTableCellsDefaultColors("embarazoActualTable", 0xFFFFFF, 0xFFFFFF);
  		report.addSectionTitle("antecedentesPediatricos", "embarazoActualTable", "Embarazo Actual");
	    report.font.setFontAttributes(0x000000, 11, false, false, false);
	    section.setTableBorderColor("embarazoActualTable", 0xFFFFFF);
  		
  		/*iTextBaseTable section = report.createSection("embarazoActual", "embarazoActualTable", numRows, 4, 5);
  		report.font.setFontAttributes(0x000000, 11, true, false, false);
  		report.addSectionTitle("embarazoActual", "embarazoActualTable", "Embarazo Actual");
	    report.font.setFontAttributes(0x000000, 11, false, false, false);*/
  		
	    section.setTableCellsColSpan(3);
	    
  		//Control Prenatal
  		if (hayControlPrenatal) 
  		{
  			
  			textData = "Control Prenatal: " + controlPrenatal;	    			
  			//Lugar control
  			if (hayLugar) textData += "   Lugar: " + lugar;
  			section.addTableTextCellAligned("embarazoActualTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, column);
  			column+=3;
  		}
  		
  		//Frecuencia
  		if (hayFrecuencia) 
  		{
  			textData = "Frecuencia, fecha o Trimestre inicio: " + frecuencia;
  			section.addTableTextCellAligned("embarazoActualTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, column);
  			column+=3;
  		}
  		
            //Chequear que columnas fueron llenadas de la 1era fila
	    if (column > 0) 
	    {
	    	while (column < 6) 
	    	{
	    		section.addTableTextCellAligned("embarazoActualTable", "    ", report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, column);
	    		column+=3;
	    	}
	    	row++;
	    }
	    column=0;
  		
  		//Serologia
  		if (haySerologia) 
  		{
  			textData = "Serologia: " + serologia;
  			section.addTableTextCellAligned("embarazoActualTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, column);
  			column+=3;
  		}
  		
  		//Test de O'Sullivan
  		if (hayTestSullivan) 
  		{
  			textData = "Test de O'Sullivan: " + testSullivan;
  			section.addTableTextCellAligned("embarazoActualTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, column);
  			column+=3;
  		}
  		
            //Chequear que columnas fueron llenadas de la 2nda fila
	    if (column > 0) 
	    {
	    	while (column < 6) 
	    	{
	    		section.addTableTextCell("embarazoActualTable", "    ", report.font, row, column);
	    		column+=3;
	    	}
	    	row++;
	    }
	    column=0;
	    
	    //Categorias Embarazos
	    if (hayCategoriasEmbarazo) 
	    {
	    	Iterator it = datosCategoriasEmbarazo.iterator();
	    	textData = "";
	    	String categoria = "";
	    	boolean firstTime = true;
	    	
	    	while (it.hasNext()) 
	    	{
	    		String datosCategorias[] = (String[]) it.next();
	    		
	    		if (firstTime) 
	    		{
	    			textData = datosCategorias[0] + ": " + datosCategorias[1];
	    			categoria = datosCategorias[0];
	    			firstTime = false;
	    		}
	    		else 
	    		{
	    			if (datosCategorias[0].equals(categoria)) 
	    				textData += ", "  + datosCategorias[1];
	    			else 
	    			{
	    				//Adicionar categoria anterior
	    				section.setTableCellsColSpan(6);
	    				section.addTableTextCellAligned("embarazoActualTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, column);
	    				row++;
	    				
	    				//Registrar nueva categoria
	    				textData = datosCategorias[0] + ": " + datosCategorias[1];
	    				categoria = datosCategorias[0];
	    			}
	    		}
	    	}
	    }
	    
	    //Observaciones del Embarazo
	    if (hayObservacionesEmbarazo) 
	    {
	    	section.setTableCellsColSpan(1);
	    	section.addTableTextCellAligned("embarazoActualTable", "Observaciones\n Embarazo: ", report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, column);
	    	column++;
	    	section.setTableCellsColSpan(5);
	    	observacionesEmbarazo = observacionesEmbarazo.replaceAll("\n", " ");
	    	observacionesEmbarazo = observacionesEmbarazo.replaceAll("\r\n", " ");
	    	observacionesEmbarazo = observacionesEmbarazo.replaceAll("\n\r", " ");
	    	observacionesEmbarazo = observacionesEmbarazo.replaceAll("\r", " ");
	    	section.addTableTextCellAligned("embarazoActualTable", observacionesEmbarazo, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, column);
	    	section.setTableCellsColSpan(1);
	    	column+=5;
	    	row++;
	    }
	    
	    report.font.setFontAttributes(0x000000, 11, true, false, false);
	    section.insertTableIntoCell("antecedentesPediatricosTable", "embarazoActualTable", globalRow, 0);
	    globalRow++;
	    
	    //report.addSectionToDocument("embarazoActual");
  	}
  	
  	//Seccion: Trabajo de Parto ----------------------------------------------------------------------------------------------------------
  	Integer duracionPartoHoras = (Integer) datos.get("duracionPartoHoras");
  	Integer duracionPartoMinutos = (Integer) datos.get("duracionPartoMinutos");
  	String tipoTrabajoParto = (String) datos.get("tipoTrabajoParto");
  	Integer duracionExpulsivoHoras = (Integer) datos.get("duracionExpulsivoHoras");
  	Integer duracionExpulsivoMinutos = (Integer) datos.get("duracionExpulsivoMinutos");
  	String causaTipoTrabajoParto = (String) datos.get("causaTipoTrabajoParto");
  	String presentacion = (String) datos.get("presentacion");
  	String otraPresentacion = (String) datos.get("otraPresentacion");
  	String situacion = (String) datos.get("situacion");
  	Integer rupturaMembranasHoras = (Integer) datos.get("rupturaMembranasHoras");
  	Integer rupturaMembranasMinutos = (Integer) datos.get("rupturaMembranasMinutos");
  	String anestesia = (String) datos.get("anestesia");
  	String tipoAnestesia = (String) datos.get("tipoAnestesia");
  	String amnionitis = (String) datos.get("amnionitis");
  	String factorNormal = (String) datos.get("factorNormal");
  	String nst = (String) datos.get("nst");
  	String ptc = (String) datos.get("ptc");
  	String perfilBiofisico = (String) datos.get("perfilBiofisico");
  	String otrosExamenes = (String) datos.get("otrosExamenes");
  	
  	boolean hayDuracionPartoHoras = (duracionPartoHoras != null && duracionPartoHoras.intValue() != -1);
  	boolean hayDuracionPartoMinutos = (duracionPartoMinutos != null && duracionPartoMinutos.intValue() != -1);
  	boolean hayTipoTrabajoParto = (tipoTrabajoParto != null && tipoTrabajoParto.length() > 0);
  	boolean hayDuracionExpulsivoHoras = (duracionExpulsivoHoras != null && duracionExpulsivoHoras.intValue() != -1);
  	boolean hayDuracionExpulsivoMinutos = (duracionExpulsivoMinutos != null && duracionExpulsivoMinutos.intValue() != -1);
  	boolean hayCausaTipoTrabajoParto = (causaTipoTrabajoParto != null && causaTipoTrabajoParto.length() > 0);
  	boolean hayPresentacion = (presentacion != null && presentacion.length() > 0);
  	boolean hayOtraPresentacion = (otraPresentacion != null && otraPresentacion.length() > 0);
  	boolean haySituacion = (situacion != null && situacion.length() > 0);
  	boolean hayRupturaMembranasHoras = (rupturaMembranasHoras != null && rupturaMembranasHoras.intValue() != -1);
  	boolean hayRupturaMembranasMinutos = (rupturaMembranasMinutos != null && rupturaMembranasMinutos.intValue() != -1);
  	boolean hayAnestesia = (anestesia != null && anestesia.length() > 0);
  	boolean hayTipoAnestesia = (tipoAnestesia != null && tipoAnestesia.length() > 0);
  	boolean hayAmnionitis = (amnionitis != null && amnionitis.length() > 0);
  	boolean hayFactorNormal = (factorNormal != null && factorNormal.length() > 0);
  	boolean hayNst = (nst != null && nst.length() > 0);
  	boolean hayPtc = (ptc != null && ptc.length() > 0);
  	boolean hayPerfilBiofisico = (perfilBiofisico != null && perfilBiofisico.length() > 0);
  	boolean hayOtrosExamenes = (otrosExamenes != null && otrosExamenes.length() > 0);
  	
  	hayDatos = (hayDuracionPartoHoras || hayDuracionPartoMinutos || hayTipoTrabajoParto || hayDuracionExpulsivoHoras ||
  			hayDuracionExpulsivoMinutos || hayCausaTipoTrabajoParto || hayPresentacion || hayOtraPresentacion || haySituacion || 
			hayRupturaMembranasHoras || hayRupturaMembranasMinutos || hayAnestesia || hayTipoAnestesia || hayAmnionitis || 
			hayFactorNormal|| hayNst || hayPtc || hayPerfilBiofisico || hayOtrosExamenes);
  	
  	if (hayDatos) 
  	{
            //Si no se ha agregado la cabecera de los antecedentes
  		if (!cabeceraAntecedentesAgregada) {
  			//report.document.addParagraph("Antecedentes Pediatricos", report.font, 30);
  			section = report.createSection("antecedentesPediatricos", "antecedentesPediatricosTable", 2, 1, 10);
    		report.font.setFontAttributes(0x000000, 11, true, false, false);
    		report.addSectionTitle("antecedentesPediatricos", "antecedentesPediatricosTable", "Antecedentes Pediátricos");
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
  			cabeceraAntecedentesAgregada = true;
  		}	
  		
  		int row = 1, column = 0;
  		
  		report.createSectionTable("antecedentesPediatricos", "trabajoPartoTable", 2, 6);
  		report.font.setFontAttributes(0x000000, 11, true, false, false);
  		section.setTableBorder("trabajoPartoTable", 0xFFFFFF, 0.0f);
	    section.setTableCellBorderWidth("trabajoPartoTable", 0.5f);
	    section.setTableCellPadding("trabajoPartoTable", 1);
	    section.setTableSpaceBetweenCells("trabajoPartoTable", 0.5f);
	    section.setTableCellsDefaultColors("trabajoPartoTable", 0xFFFFFF, 0xFFFFFF);
  		report.addSectionTitle("antecedentesPediatricos", "trabajoPartoTable", "Trabajo de Parto");
	    report.font.setFontAttributes(0x000000, 11, false, false, false);
	    section.setTableBorderColor("trabajoPartoTable", 0xFFFFFF);
  		
  		/*iTextBaseTable section = report.createSection("trabajoParto", "trabajoPartoTable", numRows, 3, 5);
  		report.font.setFontAttributes(0x000000, 11, true, false, false);
  		report.addSectionTitle("trabajoParto", "trabajoPartoTable", "Trabajo de Parto");
	    report.font.setFontAttributes(0x000000, 11, false, false, false);*/
	    
	    
	    section.setTableCellsColSpan(2);
	    //Duracion
	    if (hayDuracionPartoHoras || hayDuracionPartoMinutos) 
	    {
	    	textData = "Duracion:";
	    	if (hayDuracionPartoHoras) textData += " " + duracionPartoHoras + " hrs.";
	    	if (hayDuracionPartoMinutos) textData += " " + duracionPartoMinutos + " min.";
	    	section.addTableTextCellAligned("trabajoPartoTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, column);
	    	column+=2;
	    }
	    
	    //Tipo Trabajo Parto
	    if (hayTipoTrabajoParto) {
	    	textData = "Tipo Trabajo Parto: " + tipoTrabajoParto;
	    	section.addTableTextCellAligned("trabajoPartoTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, column);
	    	column+=2;
	    }
	    
	    //Duracion Expulsivo
	    if (hayDuracionExpulsivoHoras || hayDuracionExpulsivoMinutos) 
	    {
	    	textData = "Duración Expulsivo:";
	    	if (hayDuracionExpulsivoHoras) textData += " " + duracionExpulsivoHoras + " hrs.";
	    	if (hayDuracionExpulsivoMinutos) textData += " " + duracionExpulsivoMinutos + " min.";
	    	
	    	section.addTableTextCellAligned("trabajoPartoTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, column);
	    	
	    	column+=2;
	    }
	    
	    //Chequear que columnas fueron llenadas de la 1era fila
	    if (column > 0) 
	    {
	    	while (column < 6) 
	    	{
	    		section.addTableTextCell("trabajoPartoTable", "    ", report.font, row, column);
	    		column+=2;
	    	}
	    	row++;
	    }
	    column=0;
	    
	    //Causa Tipo Trabajo Parto
	    if (hayCausaTipoTrabajoParto) 
	    {
	    	textData = "Causa Tipo Trabajo Parto: " + causaTipoTrabajoParto;
	    	section.addTableTextCellAligned("trabajoPartoTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, column);
	    	column+=2;
	    }
	    
	    //Presentacion
	    if (hayPresentacion || hayOtraPresentacion) 
	    {
	    	textData = "Presentación:";
	    	if (hayPresentacion) textData += " " + presentacion;
	    	else textData += " " + otraPresentacion;
	    	section.addTableTextCellAligned("trabajoPartoTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, column);
	    	column+=2;
	    }
	    
	    //Situacion
	    if (haySituacion) 
	    {
	    	textData = "Situacion: " + situacion;
	    	section.addTableTextCellAligned("trabajoPartoTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, column);
	    	column+=2;
	    }
	    
	    //Chequear que columnas fueron llenadas de la 2nda fila
	    if (column > 0) {
	    	while (column < 6) 
	    	{
	    		section.addTableTextCell("trabajoPartoTable", "    ", report.font, row, column);
	    		column+=2;
	    	}
	    	row++;
	    }
	    column=0;
	    
	    //Ruptura de Membranas
	    if (hayRupturaMembranasHoras || hayRupturaMembranasMinutos) 
	    {
	        textData = "Ruptura de Membranas:";
	        if (hayRupturaMembranasHoras) textData += " " + rupturaMembranasHoras + " hrs.";
	        if (hayRupturaMembranasMinutos) textData += " " + rupturaMembranasMinutos + " min.";
	        section.addTableTextCellAligned("trabajoPartoTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, column);
	    	column+=2;
	    }
	    
	    //Anestesia
	    if (hayAnestesia) 
	    {
	    	textData = "Anestesia: " + anestesia;
	    	section.addTableTextCellAligned("trabajoPartoTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, column);
	    	column+=2;
	    }
	    
	    //Tipo Anestesia
	    if (hayTipoAnestesia) 
	    {
	    	textData = "Tipo: " + tipoAnestesia;
	    	section.addTableTextCellAligned("trabajoPartoTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, column);
	    	column+=2;
	    }
	    
	    //Chequear que columnas fueron llenadas de la 3ra fila
	    if (column > 0) 
	    {
	    	while (column < 6) 
	    	{
	    		section.addTableTextCell("trabajoPartoTable", "    ", report.font, row, column);
	    		column++;
	    	}
	    	row++;
	    }
	    column=0;
	    
	    //Amnionitis && Factor Normal
	    if (hayAmnionitis) 
	    {
	    	textData = " Amnionitis: " + amnionitis;
	    	
	    	if (hayFactorNormal)
	    		textData += "   Factor Normal: " + factorNormal;
	    	
	    	section.addTableTextCellAligned("trabajoPartoTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, column);
	    	column+=2;
	    	
	    }
	    
	    //NST
	    if (hayNst) 
	    {
	    	textData = "NST: " + nst;
	    	section.addTableTextCellAligned("trabajoPartoTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, column);
	    	column+=2;
	    }
	    
	    //PTC
	    if (hayPtc) 
	    {
	    	textData = "PTC: " + ptc;
	    	section.addTableTextCellAligned("trabajoPartoTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, column);
	    	column+=2;
	    }
	    
	    //Chequear que columnas fueron llenadas de la 4ta fila
	    if (column > 0) {
	    	while (column < 6) 
	    	{
	    		section.addTableTextCell("trabajoPartoTable", "    ", report.font, row, column);
	    		column+=2;
	    	}
	    	row++;
	    }
	    column=0;
	    
	    section.setTableCellsColSpan(3);
	    
	    //Perfil Biofisico
	    if (hayPerfilBiofisico) 
	    {
	    	textData = " Perfil Biofísico: " + perfilBiofisico;
	    	section.addTableTextCellAligned("trabajoPartoTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, column);
	    	column+=3;
	    }
	    
	    //Otros Exámenes
	    if (hayOtrosExamenes) 
	    {
	    	textData = "Otros Exámenes: " + otrosExamenes;
	    	section.addTableTextCellAligned("trabajoPartoTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, column);
	    	column+=3;
	    }
	    
	    
	    //Chequear que columnas fueron llenadas de la 4ta fila
	    if (column > 0) 
	    {
	    	while (column < 6) 
	    	{
	    		section.addTableTextCell("trabajoPartoTable", "    ", report.font, row, column);
	    		column+=3;
	    	}
	    	row++;
	    }
	    column=0;
	    
	    report.font.setFontAttributes(0x000000, 11, true, false, false);
	    section.insertTableIntoCell("antecedentesPediatricosTable", "trabajoPartoTable", globalRow, 0);
	    globalRow++;
	    //report.addSectionToDocument("trabajoParto");
  	}
  	
  	//Seccion: Atencion del Parto ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  	String fechaNacimiento = (String) datos.get("fechaNacimiento");
  	String horaNacimiento = (String) datos.get("horaNacimiento");
  	Integer talla = (Integer) datos.get("talla");
  	Integer peso = (Integer) datos.get("peso");
  	Float perimetroCefalico = (Float) datos.get("perimetroCefalico");
  	Float perimetroToraxico = (Float) datos.get("perimetroToraxico");
  	Integer edadGestacional = (Integer) datos.get("edadGestacional");
  	String tipoParto = (String) datos.get("tipoParto");
  	
  	String sano = (String) datos.get("sano");
  	String gemelo = (String) datos.get("gemelo");
  	String caracteristicasPlacenta = (String) datos.get("caracteristicasPlacenta");
  	String complicacionesParto = (String) datos.get("complicacionesParto");
  	String liquidoAmnioticoClaro = (String) datos.get("liquidoAmnioticoClaro");
  	String liquidoAmnioticoMeconiado = (String) datos.get("liquidoAmnioticoMeconiado");
  	String gradoLiquidoAmnioticoMeconiado = (String) datos.get("gradoLiquidoAmnioticoMeconiado");
  	String liquidoAmnioticoSanguinolento = (String) datos.get("liquidoAmnioticoSanguinolento");
  	String liquidoAmnioticoFetido = (String) datos.get("liquidoAmnioticoFetido");
  	String muestrasCordonUmbilical = (String) datos.get("muestrasCordonUmbilical");
  	String caracteristicasCordonUmbilical= (String) datos.get("caracteristicasCordonUmbilical");
  	String intrauterinoPeg = (String) datos.get("intrauterinoPeg");
  	String intrauterinoPegCausa = (String) datos.get("intrauterinoPegCausa");
  	Integer intrauterinoAnormalidad = (Integer) datos.get("intrauterinoAnormalidad");
  	String intrauterinoAnormalidadCausa = (String) datos.get("intrauterinoAnormalidadCausa");
  	String intrauterinoArmonico = (String) datos.get("intrauterinoArmonico");
  	String intrauterinoArmonicoCausa = (String) datos.get("intrauterinoArmonicoCausa");
  	String reanimacion = (String) datos.get("reanimacion");
  	String aspiracion = (String) datos.get("aspiracion");
  	
  	boolean hayFechaNacimiento = (fechaNacimiento != null && fechaNacimiento.length() > 0);
      boolean hayHoraNacimiento = (horaNacimiento != null && horaNacimiento.length() > 0);
      boolean hayTalla = (talla != null && talla.intValue() != -1);
      boolean hayPeso = (peso != null && peso.intValue() != -1);
      boolean hayPerimetroCefalico = (perimetroCefalico != null && perimetroCefalico.floatValue() != -1);
      boolean hayPerimetroToraxico = (perimetroToraxico != null && perimetroToraxico.floatValue() != -1);
      boolean hayEdadGestacional = (edadGestacional != null && edadGestacional.intValue() != -1);
      boolean hayTipoParto = (tipoParto != null && tipoParto.length() > 0);
      boolean haySano = (sano != null && sano.length() > 0);
      boolean hayGemelo = (gemelo != null && gemelo.length() > 0);
      boolean hayCaracteristicasPlacenta = (caracteristicasPlacenta != null && caracteristicasPlacenta.length() > 0);
      boolean hayComplicacionesParto = (complicacionesParto != null && complicacionesParto.length() > 0);
      boolean hayLiquidoAmnioticoClaro = (liquidoAmnioticoClaro != null && liquidoAmnioticoClaro.length() >0 );
      boolean hayLiquidoAmnioticoMeconiado = (liquidoAmnioticoMeconiado != null && liquidoAmnioticoMeconiado.length() > 0);
      boolean hayLiquidoAmnioticoSanguinolento = (liquidoAmnioticoSanguinolento != null && liquidoAmnioticoSanguinolento.length() > 0);
      boolean hayLiquidoAmnioticoFetido = (liquidoAmnioticoFetido != null && liquidoAmnioticoFetido.length() > 0);
      boolean hayGradoLiquidoAmnioticoMeconiado = (gradoLiquidoAmnioticoMeconiado != null && gradoLiquidoAmnioticoMeconiado.length() > 0);
      boolean hayMuestrasCordonUmbilical = (muestrasCordonUmbilical != null && muestrasCordonUmbilical.length() > 0);
      boolean hayCaracteristicasCordonUmbilical = (caracteristicasCordonUmbilical != null && caracteristicasCordonUmbilical.length() > 0);
      boolean hayIntrauterinoPeg = (intrauterinoPeg != null && intrauterinoPeg.length() > 0);
      boolean hayIntrauterinoPegCausa = (intrauterinoPegCausa != null && intrauterinoPegCausa.length() > 0);
      boolean hayIntrauterinoAnormalidad = (intrauterinoAnormalidad != null && intrauterinoAnormalidad.intValue() != -1);
      boolean hayIntrauterinoAnormalidadCausa = (intrauterinoAnormalidadCausa != null && intrauterinoAnormalidadCausa.length() > 0);
      boolean hayIntrauterinoArmonico = (intrauterinoArmonico != null && intrauterinoArmonico.length() > 0);
      //boolean hayIntrauterinoArmonicoCausa = (intrauterinoArmonicoCausa != null && intrauterinoArmonicoCausa.length() > 0);
      boolean hayReanimacion = (reanimacion != null && reanimacion.length() > 0);
      boolean hayAspiracion = (aspiracion != null && aspiracion.length() > 0);
      
      
      hayDatos = (hayFechaNacimiento || hayHoraNacimiento || hayTalla || hayPeso || hayPerimetroCefalico || hayPerimetroToraxico || hayEdadGestacional || hayTipoParto || haySano ||
      		hayGemelo || hayCaracteristicasPlacenta || hayComplicacionesParto || hayLiquidoAmnioticoClaro || hayLiquidoAmnioticoMeconiado || hayLiquidoAmnioticoSanguinolento || 
			hayLiquidoAmnioticoFetido || hayMuestrasCordonUmbilical || hayCaracteristicasCordonUmbilical || hayIntrauterinoPeg || hayIntrauterinoAnormalidad || hayIntrauterinoArmonico || 
			hayReanimacion || hayAspiracion);
      
      if (hayDatos) {
      	//Si no se ha agregado la cabecera de los antecedentes
  		if (!cabeceraAntecedentesAgregada) {
  			//report.document.addParagraph("Antecedentes Pediatricos", report.font, 30);
  			section = report.createSection("antecedentesPediatricos", "antecedentesPediatricosTable", 2, 1, 10);
    		report.font.setFontAttributes(0x000000, 11, true, false, false);
    		report.addSectionTitle("antecedentesPediatricos", "antecedentesPediatricosTable", "Antecedentes Pediátricos");
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
  			cabeceraAntecedentesAgregada = true;
  		}
  		
  		int row = 1, column = 0;

  		report.createSectionTable("antecedentesPediatricos", "atencionPartoTable", 1, 6);
  		report.font.setFontAttributes(0x000000, 11, true, false, false);
  		section.setTableBorder("atencionPartoTable", 0xFFFFFF, 0.0f);
	    section.setTableCellBorderWidth("atencionPartoTable", 0.5f);
	    section.setTableCellPadding("atencionPartoTable", 1);
	    section.setTableSpaceBetweenCells("atencionPartoTable", 0.5f);
	    section.setTableCellsDefaultColors("atencionPartoTable", 0xFFFFFF, 0xFFFFFF);
  		report.addSectionTitle("antecedentesPediatricos", "atencionPartoTable", "Atención del Parto");
	    report.font.setFontAttributes(0x000000, 11, false, false, false);
	    section.setTableBorderColor("atencionPartoTable", 0xFFFFFF);

	    /*
	    //Fecha de Nacimiento
	    if (hayFechaNacimiento) {
	    	textData = "Fecha Nacimiento: " + fechaNacimiento;
	    	section.setTableCellsColSpan(2);
	    	section.addTableTextCell("atencionPartoTable", textData, report.font, row, column);
	    	section.setTableCellsColSpan(1);
	    	column+=2;
	    }
	    
	    //Hora de Nacimiento
	    if (hayHoraNacimiento) {
	    	textData = "Hora: " + horaNacimiento;
	    	section.setTableCellsColSpan(2);
	    	section.addTableTextCell("atencionPartoTable", textData, report.font, row, column);
	    	section.setTableCellsColSpan(1);
	    	column+=2;
	    }


	    // Chequear que columnas fueron llenadas de la 1era fila
	    if (column > 0) {
	    	while (column < 4) {
	    		section.addTableTextCell("atencionPartoTable", "    ", report.font, row, column);
	    		column++;
	    	}
	    	row++;
	    }
	    
 	    column=0;
*/
	    	    
	    //Examenes Fisicos al nacer
	    textData = "";
	    if (hayTalla || hayPeso || hayPerimetroCefalico || hayPerimetroToraxico || hayEdadGestacional) {
	    	textData = "Examenes Físicos al Nacer: ";
	    	report.font.setFontAttributes(0x000000, 11, false, false, false);
	    }
	    
	    //Talla
	    if (hayTalla) {
	    	textData += " Talla: " + talla + " cms." +" ";
	    }
	    
	    //Peso
	    if (hayPeso) {
	    	 textData  += " Peso: " + peso + " grs.";
	    }
	    
	    //Perimetro Cefalico
	    if (hayPerimetroCefalico) {
	    	textData  += " Perímetro Cefálico: " + perimetroCefalico + " cms.";
	    }
	    
	    //Perimetro Toraxico
	    if (hayPerimetroToraxico) {
	    	textData  += " Perímetro Torácico: " + perimetroToraxico + " cms.";
     } 
	    
	    //Edad gestacional
	    if (hayEdadGestacional) {
	    	textData  += " Edad Gestacional (Ballard): " + edadGestacional + " semanas";
	    }

	    if (hayTalla || hayPeso || hayPerimetroCefalico || hayPerimetroToraxico || hayEdadGestacional){
	    	section.setTableCellsColSpan(6);
	    	section.addTableTextCellAligned("atencionPartoTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, 0);
	    	section.setTableCellsColSpan(1);
	    	row++;
	    }

	    //Tipo de Parto
	    textData = "";
	    if (hayTipoParto) {
	    	textData = "Tipo de Parto: " + tipoParto;
/*	    	section.setTableCellsColSpan(4);
	    	section.addTableTextCell("atencionPartoTable", textData, report.font, row, column);
	    	section.setTableCellsColSpan(1);
	    	row++;
*/
	    }

	    
	    //Sano
	    if (haySano) {
	    	textData += "  Sano: " + sano;
	    	//section.addTableTextCell("atencionPartoTable", textData, report.font, row, column);
/*	    	
	    	if (column == 3) {
	    		column = 0;
	    		row++;
	    	}
	    	else column++;
*/
	    }
	    
	    //Gemelo
	    if (hayGemelo) {
	    	textData  += "  Gemelo: " + gemelo;
	    	//section.addTableTextCell("atencionPartoTable", textData, report.font, row, column);
/*	    	if (!(hayLiquidoAmnioticoClaro || hayLiquidoAmnioticoMeconiado || hayLiquidoAmnioticoSanguinolento || hayLiquidoAmnioticoFetido)) {
	    		if (column == 3) {
		    		column = 0;
		    		row++;
		    	}
		    	else column++;	
	    	}
	    }
	    if (hayLiquidoAmnioticoClaro || hayLiquidoAmnioticoMeconiado || hayLiquidoAmnioticoSanguinolento || hayLiquidoAmnioticoFetido) {
	    	column = 0;
	    	row++;
*/
	    }


	    if(!textData.equals("")){
	    	section.setTableCellsColSpan(6);
	    	section.addTableTextCellAligned("atencionPartoTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, 0);
	    	section.setTableCellsColSpan(1);
	    	row++;
      }

	    
	    textData = "";
	    //Caracteristicas Liquido Amniotico
	    if (hayLiquidoAmnioticoClaro || hayLiquidoAmnioticoMeconiado || hayLiquidoAmnioticoSanguinolento || hayLiquidoAmnioticoFetido) {
	    	textData = "Caracteristicas Líquido Amniótico : "; 
	    	//section.addTableTextCell("atencionPartoTable", "Caracteristicas Líquido Amniótico", report.font, row, column);
	    	//column++;
	    }
	    
	    //Liquido Amniotico Claro
	    if (hayLiquidoAmnioticoClaro) {
	    	textData += " Claro ";
	    	//section.addTableTextCell("atencionPartoTable", "Claro", report.font, row, column);
	    	//column++;
	    }
	    
	    //Liquido Amniotico Meconiado
	    if (hayLiquidoAmnioticoMeconiado || hayGradoLiquidoAmnioticoMeconiado) {
	    	if (hayLiquidoAmnioticoMeconiado) textData += "  Meconiado  ";
	    	if (hayGradoLiquidoAmnioticoMeconiado) textData += "    Grado " + gradoLiquidoAmnioticoMeconiado;
	    	//section.addTableTextCell("atencionPartoTable", textData, report.font, row, column);
	    	//column++;    
	    }
	    
	    //Liquido Amniotico Sanguinolento & Fetido
	    if (hayLiquidoAmnioticoSanguinolento || hayLiquidoAmnioticoFetido) {
	    	if (hayLiquidoAmnioticoSanguinolento) textData += "  Sanguinolento";
	    	if (hayLiquidoAmnioticoFetido) textData +="  Fetido";
	    	//section.addTableTextCell("atencionPartoTable", textData, report.font, row, column);
	    	
	    	/*if (column == 3) {
	    		column = 0;
	    		row++;
	    	}
	    	else column++;
	    	*/
	    }
	    
	    if(!textData.equals("")){
	    	section.setTableCellsColSpan(6);
	    	section.addTableTextCellAligned("atencionPartoTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, 0);
	    	section.setTableCellsColSpan(1);
	    	row++;
      }
	    
	    
      textData = "";
      //Caracteristicas Placenta
	    if (hayCaracteristicasPlacenta) {
	    	textData = "Características Placenta: " + caracteristicasPlacenta;
	    	/*
 	    	if (!(hayMuestrasCordonUmbilical || hayCaracteristicasCordonUmbilical) && column < 3) section.setTableCellsColSpan(2);
	    	section.addTableTextCell("atencionPartoTable", textData, report.font, row, column);
	    	section.setTableCellsColSpan(1);
	    	
	    	if (column == 3) {
	    		column = 0;
	    		row++;

	    	}
	    	else if (!(hayMuestrasCordonUmbilical || hayCaracteristicasCordonUmbilical) && column < 3) column+=2;
        else column++;
        */
	    }
	    
	    //Muestras del Cordón Umbilical
	    if (hayMuestrasCordonUmbilical) {
	    	textData += "   Muestras del Cordón Umbilical: " + muestrasCordonUmbilical;
	    	/*	    	
        if (!hayCaracteristicasCordonUmbilical && column < 3) section.setTableCellsColSpan(2);
	    	section.addTableTextCell("atencionPartoTable", textData, report.font, row, column);
	    	section.setTableCellsColSpan(1);
	    	
	    	if (column == 3) {
	    		column = 0;
	    		row++;
	    	}
	    	else if (!hayCaracteristicasCordonUmbilical && column < 3) column+=2;
	    	else column++;
	    	*/
	    }
	    
	    //Características Cordón Umbilical
	    if (hayCaracteristicasCordonUmbilical) {
	    	textData  += "  Características Cordón Umbilical: " + caracteristicasCordonUmbilical;
	    	/*
 	    	if (column < 3) section.setTableCellsColSpan(2);
	    	section.addTableTextCell("atencionPartoTable", textData, report.font, row, column);
	    	section.setTableCellsColSpan(1);
	    	*/
	    }
	    //column = 0;
	    //row++;
	    

	    if(!textData.equals("")){
	    	section.setTableCellsColSpan(6);
	    	section.addTableTextCellAligned("atencionPartoTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, 0);
	    	section.setTableCellsColSpan(1);
	    	row++;
	    }
	    
	    
	    //Crecimiento Intrauterino
	    textData = "";
	    if (hayIntrauterinoPeg || hayIntrauterinoAnormalidad || hayIntrauterinoArmonico)  {
	    	textData = "Crecimiento Intrauterino: ";
	    }
	    
	    //Intrauterino Peg
	    if (hayIntrauterinoPeg) {
	    	textData += " " + intrauterinoPeg; 
/*	    	section.addTableTextCell("atencionPartoTable", intrauterinoPeg, report.font, row, column);
	    	column++;
*/	
	    	if (hayIntrauterinoPegCausa) {
	    		textData += " Causa: " + intrauterinoPegCausa;
/*	    		section.addTableTextCell("atencionPartoTable", textData, report.font, row, column);
	    		column++;
*/
	    	}
	    }
	    
	    //Intrauterino Anormalidad
	    if (hayIntrauterinoAnormalidad) {
	    	textData += " Anormalidad (Contra Percentil) " + intrauterinoAnormalidad + "%";
/*	    	section.addTableTextCell("atencionPartoTable", textData, report.font, row, column);
    		column++;
*/    	    
    	    if (hayIntrauterinoAnormalidadCausa) {
    	    	textData += " Causa: " + intrauterinoAnormalidadCausa;
/*    	    	section.addTableTextCell("atencionPartoTable", textData, report.font, row, column);
		    	
		    	if (column == 3) {
		    		column = 0;
		    		row++;
		    	}
		    	else column++;
    	    }
*/    	    
	    }
	    
	    //Intrauterino Armonico
	    if (hayIntrauterinoArmonico) {
	    	textData += " " +  intrauterinoArmonico; 
/*	    	section.addTableTextCell("atencionPartoTable", intrauterinoArmonico, report.font, row, column);
	    	
	    	if (hayIntrauterinoArmonicoCausa) {
	    		if (column == 3) {
		    		column = 0;
		    		row++;
		    	}
		    	else column++;
*/	    		
	    	if(intrauterinoArmonicoCausa != null && intrauterinoArmonicoCausa.length() != 0)	
	    			textData  += " Causa: " + intrauterinoArmonicoCausa;
    	  //  	section.addTableTextCell("atencionPartoTable", textData, report.font, row, column);
	    	}
	    }

	    
	    if (!textData.equals(""))  {	    
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableCellsColSpan(6);
		    section.addTableTextCellAligned("atencionPartoTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, 0);
	    	section.setTableCellsColSpan(1);
	    	column = 0;
	    	row++;
	    }
	    	
	    
	    //Reanimacion
	    if (hayReanimacion || hayAspiracion) {
	    	report.font.setFontAttributes(0x000000, 11, true, false, false);
	    	section.setTableCellsColSpan(6);
	    	section.addTableTextCellAligned("atencionPartoTable", "Reanimación", report.font, iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT,row, column);
	    	section.setTableCellsColSpan(1);
	    	report.font.setFontAttributes(0x000000, 11, false, false, false);
	    	row++;
	    }
	    
	    
	    section.setTableCellsColSpan(3);
	    //Reanimacion
	    if (hayReanimacion) {
	        section.addTableTextCellAligned("atencionPartoTable", reanimacion, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, column);
	        column+=3;
	    }
	    
	    //Aspiracion
	    if (hayAspiracion) 
	    {
	    	textData = "Aspiración en periné, meconio o traquea: " + aspiracion;
	    	section.addTableTextCellAligned("atencionPartoTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, column);
	    }
	    column = 0;
	    row++;
	    
	    //Complicaciones Parto
	    if (hayComplicacionesParto) 
	    {
	    	section.setTableCellsColSpan(6);
	    	textData = "Complicaciones Parto: " + complicacionesParto;
	    	section.addTableTextCellAligned("atencionPartoTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, row, column);
	    	section.setTableCellsColSpan(1);
	    	
	    }
	    
	    if (column > 0) {
	    	while (column  < 4) {
	    		section.addTableTextCell("atencionPartoTable", "    ", report.font, row, column);
	    		column++;
	    	}
	    }
	    
	    report.font.setFontAttributes(0x000000, 11, true, false, false);
	    section.insertTableIntoCell("antecedentesPediatricosTable", "atencionPartoTable", globalRow, 0);
	    globalRow++;
	    //report.addSectionToDocument("atencionParto");

      }
      
      //Seccion: APGAR ---------------------------------------------------------------------------------------------------------------------
      Integer minuto1 = (Integer)datos.get("minuto1");
      Integer minuto5 = (Integer) datos.get("minuto5");
      Integer minuto10 = (Integer) datos.get("minuto10");
      String riesgoMinuto1 = (String) datos.get("riesgoMinuto1");
      String riesgoMinuto5 = (String) datos.get("riesgoMinuto5");
      String riesgoMinuto10 = (String) datos.get("riesgoMinuto10");
      
      boolean hayMinuto1 = (minuto1 != null && minuto1.intValue() != -1);
      boolean hayMinuto5 = (minuto5 != null && minuto5.intValue() != -1);
      boolean hayMinuto10 = (minuto10 != null && minuto10.intValue() != -1);
      boolean hayRiesgoMinuto1 = (riesgoMinuto1 != null && riesgoMinuto1.length() > 0);
      boolean hayRiesgoMinuto5 = (riesgoMinuto5 != null && riesgoMinuto5.length() > 0);
      boolean hayRiesgoMinuto10 = (riesgoMinuto10 != null && riesgoMinuto10.length() > 0);
      
      hayDatos= (hayMinuto1 || hayMinuto5 || hayMinuto10);
      
      if (hayDatos) {
      	//Si no se ha agregado la cabecera de los antecedentes
  		if (!cabeceraAntecedentesAgregada) {
  			//report.document.addParagraph("Antecedentes Pediatricos", report.font, 30);
  			section = report.createSection("antecedentesPediatricos", "antecedentesPediatricosTable", 2, 1, 10);
    		report.font.setFontAttributes(0x000000, 11, true, false, false);
    		report.addSectionTitle("antecedentesPediatricos", "antecedentesPediatricosTable", "Antecedentes Pediátricos");
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
  			cabeceraAntecedentesAgregada = true;
  		}
  		
  		report.createSectionTable("antecedentesPediatricos", "apgarTable", 1, 6);
  		report.font.setFontAttributes(0x000000, 11, true, false, false);
  		section.setTableBorder("apgarTable", 0xFFFFFF, 0.0f);
	    section.setTableCellBorderWidth("apgarTable", 0.5f);
	    section.setTableCellPadding("apgarTable", 1);
	    section.setTableSpaceBetweenCells("apgarTable", 0.5f);
	    section.setTableCellsDefaultColors("apgarTable", 0xFFFFFF, 0xFFFFFF);
  		//report.addSectionTitle("antecedentesPediatricos", "apgarTable", "Apgar");
	    report.font.setFontAttributes(0x000000, 11, false, false, false);
	    section.setTableBorderColor("apgarTable", 0xFFFFFF);
	    
	    textData = "Apgar: ";
	    
	    if (hayMinuto1) {
	    	textData += "Minuto Uno: " + minuto1.intValue();
	    	if (hayRiesgoMinuto1) textData += " " + riesgoMinuto1;
	    	textData += "   ";
	    	//section.addTableTextCell("apgarTable", textData, report.font, 1, column);
	    	//column++;
	    }
	    
	    if (hayMinuto5) {
	    	textData += "Minuto Cinco: " + minuto5.intValue();
	    	if (hayRiesgoMinuto5) textData += " " + riesgoMinuto5;
	    	textData += "   ";
	    	//section.addTableTextCell("apgarTable", textData, report.font, 1, column);
	    	//column++;
	    }
	    
	    if (hayMinuto10) {
	    	textData += "Minuto Diez: " + minuto10.intValue();
	    	if (hayRiesgoMinuto10) textData += " " + riesgoMinuto10;
	    	textData += "   ";
	    	///section.addTableTextCell("apgarTable", textData, report.font, 1, column);
	    	//column++;
	    }
	    
	    section.setTableCellsColSpan(6);
	    section.addTableTextCellAligned("apgarTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, 0, 0);
	    section.setTableCellsColSpan(1);
	    
	    /*if (column > 0) {
	    	while (column < 3) {
	    		section.addTableTextCell("apgarTable", "", report.font, 1, column);
	    		column++;
	    	}
	    }*/
	    
	    report.font.setFontAttributes(0x000000, 11, true, false, false);
	    section.insertTableIntoCell("antecedentesPediatricosTable", "apgarTable", globalRow, 0);
	    globalRow++;
	    //report.addSectionToDocument("apgar");
      }
      
      //Seccion: Inmunizaciones ------------------------------------------------------------------------------------------------------------------------
      String inmunizaciones = (String) datos.get("inmunizaciones");
      if(inmunizaciones == null){
      	hayDatos = false;
      }
      else{
      	hayDatos = (!inmunizaciones.equals(""));
      }
      
      
      if (hayDatos) {
      	if (!cabeceraAntecedentesAgregada) {
    			//report.document.addParagraph("Antecedentes Pediatricos", report.font, 30);
    			section = report.createSection("antecedentesPediatricos", "antecedentesPediatricosTable", 2, 1, 10);
      		report.font.setFontAttributes(0x000000, 11, true, false, false);
      		report.addSectionTitle("antecedentesPediatricos", "antecedentesPediatricosTable", "Antecedentes Pediátricos");
  		    report.font.setFontAttributes(0x000000, 11, false, false, false);
    			cabeceraAntecedentesAgregada = true;
    		}
      	
      	report.createSectionTable("antecedentesPediatricos", "inmunizacionesTable", 1, 6);
    		report.font.setFontAttributes(0x000000, 11, true, false, false);
    		section.setTableBorder("inmunizacionesTable", 0xFFFFFF, 0.0f);
		    section.setTableCellBorderWidth("inmunizacionesTable", 0.5f);
		    section.setTableCellPadding("inmunizacionesTable", 1);
		    section.setTableSpaceBetweenCells("inmunizacionesTable", 0.5f);
		    section.setTableCellsDefaultColors("inmunizacionesTable", 0xFFFFFF, 0xFFFFFF);
    		//report.addSectionTitle("antecedentesPediatricos", "inmunizacionesTable", "Observaciones");
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableBorderColor("inmunizacionesTable", 0xFFFFFF);	
		    
		    textData = "Inmunizaciones: " +inmunizaciones;
		    section.setTableCellsColSpan(6);
		    section.addTableTextCellAligned("inmunizacionesTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, 0, 0);
		    section.setTableCellsColSpan(1);
		    
		    section.insertTableIntoCell("antecedentesPediatricosTable", "inmunizacionesTable", globalRow, 0);
		    globalRow++;
      }
      
      //Seccion: Observaciones del Antecedente Pediatrico ---------------------------------------------------------------------------------
      String observacionesAntecedente = (String) datos.get("observacionesAntecedente");
      hayDatos = (observacionesAntecedente != null && observacionesAntecedente.length() > 0);
      
      if (hayDatos) {
      	//Si no se ha agregado la cabecera de los antecedentes
  		if (!cabeceraAntecedentesAgregada) {
  			//report.document.addParagraph("Antecedentes Pediatricos", report.font, 30);
  			section = report.createSection("antecedentesPediatricos", "antecedentesPediatricosTable", 2, 1, 10);
    		report.font.setFontAttributes(0x000000, 11, true, false, false);
    		report.addSectionTitle("antecedentesPediatricos", "antecedentesPediatricosTable", "Antecedentes Pediátricos");
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
  			cabeceraAntecedentesAgregada = true;
  		}	
  		
  		report.createSectionTable("antecedentesPediatricos", "observacionesGeneralesTable", 2, 6);
  		report.font.setFontAttributes(0x000000, 11, true, false, false);
  		section.setTableBorder("observacionesGeneralesTable", 0xFFFFFF, 0.0f);
	    section.setTableCellBorderWidth("observacionesGeneralesTable", 0.5f);
	    section.setTableCellPadding("observacionesGeneralesTable", 1);
	    section.setTableSpaceBetweenCells("observacionesGeneralesTable", 0.5f);
	    section.setTableCellsDefaultColors("observacionesGeneralesTable", 0xFFFFFF, 0xFFFFFF);
  		report.addSectionTitle("antecedentesPediatricos", "observacionesGeneralesTable", "Observaciones");
	    report.font.setFontAttributes(0x000000, 11, false, false, false);
	    section.setTableBorderColor("observacionesGeneralesTable", 0xFFFFFF);
  		
  		/*iTextBaseTable section = report.createSection("observacionesGenerales",  "observacionesGeneralesTable", 2, 1, 5);
  		report.font.setFontAttributes(0x000000, 11, true, false, false);
  		report.addSectionTitle("observacionesGenerales", "observacionesGeneralesTable", "Observaciones del Antecedente Pediatrico");
	    report.font.setFontAttributes(0x000000, 11, false, false, false);*/
	    
	    textData = observacionesAntecedente;
	    textData = textData.replaceAll("\n", " ");
	    textData = textData.replaceAll("\r\n", " ");
	    textData = textData.replaceAll("\n\r", " ");
	    textData = textData.replaceAll("\r", " ");
	    section.setTableCellsColSpan(6);
	    section.addTableTextCellAligned("observacionesGeneralesTable", textData, report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, 1, 0);
	    section.setTableCellsColSpan(1);
	    
	    report.font.setFontAttributes(0x000000, 11, true, false, false);
	    section.insertTableIntoCell("antecedentesPediatricosTable", "observacionesGeneralesTable", globalRow, 0);
	    globalRow++;
	    //report.addSectionToDocument("observacionesGenerales");
      }
      
      //RESUMEN VALORACION PEDIATRICA ------------------------------------------------------------------------------------------------------
      //section.addTableTextCell("antecedentesPediatricosTable", "", report.font,globalRow,0);
      //globalRow++;

    	if (!cabeceraAntecedentesAgregada) {
  			//report.document.addParagraph("Antecedentes Pediatricos", report.font, 30);
  			section = report.createSection("antecedentesPediatricos", "antecedentesPediatricosTable", 1, 1, 1);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
  			cabeceraAntecedentesAgregada = true;
  		}
      
      report.font.setFontAttributes(0x000000, 11, true, false, false);
    	section.addTableTextCell("antecedentesPediatricosTable", "Valoración Médica", report.font,globalRow,0);
    	report.font.setFontAttributes(0x000000, 11, false, false, false);
    	globalRow++;
      
      //Revisión Sistemas -----------------------------------------------------------------------------------------------------------------------------------------------------------
	    sectionData = new String[1];
	    HashMap examenesFisicos = (HashMap)datos.get("examenesFisicos");
	    int numRegistros = 0;
	    
	    if(examenesFisicos!=null&&examenesFisicos.get("numRegistros")!=null)
	    	numRegistros = Integer.parseInt(examenesFisicos.get("numRegistros").toString());
	    
	    if(numRegistros>0)
	    {
	    	if (!cabeceraAntecedentesAgregada) 
	    	{
	  			section = report.createSection("antecedentesPediatricos", "antecedentesPediatricosTable", 1, 1, 10);
			    report.font.setFontAttributes(0x000000, 11, false, false, false);
	  			cabeceraAntecedentesAgregada = true;
	  		}
	    	
	    	
		    //****************************************************
		    report.createSectionTable("antecedentesPediatricos", "revisionSistemasTable", numRegistros, 6);
	  		report.font.setFontAttributes(0x000000, 10, true, false, false);
	  		section.setTableBorder("revisionSistemasTable", 0xFFFFFF, 0.0f);
		    section.setTableCellBorderWidth("revisionSistemasTable", 0.5f);
		    section.setTableCellPadding("revisionSistemasTable", 1);
		    section.setTableSpaceBetweenCells("revisionSistemasTable", 0.5f);
		    section.setTableCellsDefaultColors("revisionSistemasTable", 0xFFFFFF, 0xFFFFFF);
	  		report.addSectionTitle("antecedentesPediatricos", "revisionSistemasTable", "Revisión por Sistemas");
		    report.font.setFontAttributes(0x000000, 10, false, false, false);
		    section.setTableCellsColSpan(6);
		    
		    sectionData = new String[numRegistros];
		    for(int i=0;i<numRegistros;i++)
		    {
		    	sectionData[i] = examenesFisicos.get(i+"")+"";
		    }
		    
		    report.addSectionData("antecedentesPediatricos", "revisionSistemasTable", sectionData);
		    section.setTableCellsColSpan(1);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableBorderColor("revisionSistemasTable", 0xFFFFFF);
	    	
		    section.insertTableIntoCell("antecedentesPediatricosTable", "revisionSistemasTable", globalRow, 0);
		    globalRow++;
		    //****************************************************
	    }
	    
	   //Campos Parametrizables Revision Sistemas -------------------------------------------------------------------------------------------
	    examenesFisicos = (HashMap)datos.get("camposParamRevisionSistemas");
	    numRegistros = 0;
	    
	    if(examenesFisicos!=null&&examenesFisicos.get("numRegistros")!=null)
	    	numRegistros = Integer.parseInt(examenesFisicos.get("numRegistros").toString());
	    
	    if(numRegistros>0)
	    {
	    	if (!cabeceraAntecedentesAgregada) 
	    	{
	  			section = report.createSection("antecedentesPediatricos", "antecedentesPediatricosTable", 1, 1, 10);
			    report.font.setFontAttributes(0x000000, 11, false, false, false);
	  			cabeceraAntecedentesAgregada = true;
	  		}
	    	
	    	
		    //****************************************************
		    report.createSectionTable("antecedentesPediatricos", "camposParamRevisionSistemasTable", numRegistros, 6);
	  		report.font.setFontAttributes(0x000000, 10, true, false, false);
	  		section.setTableBorder("camposParamRevisionSistemasTable", 0xFFFFFF, 0.0f);
		    section.setTableCellBorderWidth("camposParamRevisionSistemasTable", 0.5f);
		    section.setTableCellPadding("camposParamRevisionSistemasTable", 1);
		    section.setTableSpaceBetweenCells("camposParamRevisionSistemasTable", 0.5f);
		    section.setTableCellsDefaultColors("camposParamRevisionSistemasTable", 0xFFFFFF, 0xFFFFFF);
	  		//report.addSectionTitle("antecedentesPediatricos", "revisionSistemasTable", "Revisión por Sistemas");
		    report.font.setFontAttributes(0x000000, 10, false, false, false);
		    section.setTableCellsColSpan(3);
		    
		    int contador = 0;
		    sectionData = new String[numRegistros*2];
		    for(int i=0;i<numRegistros;i++)
		    {
		    	sectionData[contador] = examenesFisicos.get("nombre_"+i).toString();
		    	contador++;
		    	String temp = examenesFisicos.get("info_"+i).toString();
		    	switch(Integer.parseInt(examenesFisicos.get("tipo_"+i).toString()))
		    	{
		    		case ConstantesBD.tipoParametrizablesTexto:
		    		case ConstantesBD.tipoParametrizablesTextArea:
		    		break;
		    		case ConstantesBD.tipoParametrizablesArchivo:
		    			temp = "";
		    		break;
		    		case ConstantesBD.tipoParametrizablesSelect:
		    			temp = UtilidadTexto.getBoolean(temp)?"Sí":"No";
		    		break;
		    	}
		    	sectionData[contador] = temp;
		    	contador++;
		    }
		    
		    report.addSectionData("antecedentesPediatricos", "camposParamRevisionSistemasTable", sectionData);
		    section.setTableCellsColSpan(1);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableBorderColor("camposParamRevisionSistemasTable", 0xFFFFFF);
	    	
		    section.insertTableIntoCell("antecedentesPediatricosTable", "camposParamRevisionSistemasTable", globalRow, 0);
		    globalRow++;
		    //****************************************************
	    }
	    
      //Exámenes Físicos ---------------------------------------------------------------------------------------------------------------------
      sectionData = new String[30];
      Collection signosVitales =  (Collection) datos.get("signosVitales");
      if(signosVitales == null)
      	hayDatos = false;
      else
      	hayDatos = !signosVitales.isEmpty();	
      
      
    
    if (hayDatos) 
    {
    	sectionData = new String[signosVitales.size()];
    	Iterator it = signosVitales.iterator();
    	String etiqueta, valor, unidades, vitalesString="";
    	int numSignosVitales=0;
    	
    	//Obtener los signos vitales
    	while (it.hasNext())
    	{
    		
    		SignoVital sv = (SignoVital) it.next();
    		 
    		etiqueta = sv.getValue();
    		valor = sv.getValorSignoVital();
    		unidades = sv.getUnidadMedida();
    		sectionData [numSignosVitales] = (etiqueta.length() >0 && valor.length() > 0 && unidades.length() > 0 ? etiqueta + ":" + "   " + valor + " " + unidades : "    " );
    		numSignosVitales ++;
    	}
    	if (numSignosVitales > 0)
    	{
	    	
	      	if (!cabeceraAntecedentesAgregada) 
	      	{
	    			//report.document.addParagraph("Antecedentes Pediatricos", report.font, 30);
	    			section = report.createSection("antecedentesPediatricos", "antecedentesPediatricosTable", 1, 1, 10);
	    			report.font.setFontAttributes(0x000000, 11, false, false, false);
	    			cabeceraAntecedentesAgregada = true;
	    	}
	
	      	//****************************************************
		    report.createSectionTable("antecedentesPediatricos", "examenesFisicosTable", (numSignosVitales/3+numSignosVitales%3), 6);
	  		report.font.setFontAttributes(0x000000, 10, true, false, false);
	  		section.setTableBorder("examenesFisicosTable", 0xFFFFFF, 0.0f);
		    section.setTableCellBorderWidth("examenesFisicosTable", 0.5f);
		    section.setTableCellPadding("examenesFisicosTable", 1);
		    section.setTableSpaceBetweenCells("examenesFisicosTable", 0.5f);
		    section.setTableCellsDefaultColors("examenesFisicosTable", 0xFFFFFF, 0xFFFFFF);
	  		report.addSectionTitle("antecedentesPediatricos", "examenesFisicosTable", "Exámenes Físicos");
		    report.font.setFontAttributes(0x000000, 10, false, false, false);
		    section.setTableCellsColSpan(2);
		    
		    report.addSectionData("antecedentesPediatricos", "examenesFisicosTable", sectionData);
		    section.setTableCellsColSpan(1);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableBorderColor("examenesFisicosTable", 0xFFFFFF);
	    	
		    section.insertTableIntoCell("antecedentesPediatricosTable", "examenesFisicosTable", globalRow, 0);
		    globalRow++;
		    //****************************************************
	        //report.font.setFontAttributes(0x000000, 11, false, false, false);
	      	//section.addTableTextCell("antecedentesPediatricosTable", vitalesString, report.font, globalRow, 0);
	      	//globalRow++;
	    }
    }
    
    //Campos Parametrizables Exámenes Físicos -------------------------------------------------------------------------------------------
	    examenesFisicos = (HashMap)datos.get("camposParamExamenesFisicos");
	    numRegistros = 0;
	    
	    if(examenesFisicos!=null&&examenesFisicos.get("numRegistros")!=null)
	    	numRegistros = Integer.parseInt(examenesFisicos.get("numRegistros").toString());
	    
	    if(numRegistros>0)
	    {
	    	if (!cabeceraAntecedentesAgregada) 
	    	{
	  			section = report.createSection("antecedentesPediatricos", "antecedentesPediatricosTable", 1, 1, 10);
			    report.font.setFontAttributes(0x000000, 11, false, false, false);
	  			cabeceraAntecedentesAgregada = true;
	  		}
	    	
	    	
		    //****************************************************
		    report.createSectionTable("antecedentesPediatricos", "camposParamExamenesFisicosTable", numRegistros, 6);
	  		report.font.setFontAttributes(0x000000, 10, true, false, false);
	  		section.setTableBorder("camposParamExamenesFisicosTable", 0xFFFFFF, 0.0f);
		    section.setTableCellBorderWidth("camposParamExamenesFisicosTable", 0.5f);
		    section.setTableCellPadding("camposParamExamenesFisicosTable", 1);
		    section.setTableSpaceBetweenCells("camposParamExamenesFisicosTable", 0.5f);
		    section.setTableCellsDefaultColors("camposParamExamenesFisicosTable", 0xFFFFFF, 0xFFFFFF);
	  		//report.addSectionTitle("antecedentesPediatricos", "revisionSistemasTable", "Revisión por Sistemas");
		    report.font.setFontAttributes(0x000000, 10, false, false, false);
		    section.setTableCellsColSpan(3);
		    
		    int contador = 0;
		    sectionData = new String[numRegistros*2];
		    for(int i=0;i<numRegistros;i++)
		    {
		    	sectionData[contador] = examenesFisicos.get("nombre_"+i).toString();
		    	contador++;
		    	String temp = examenesFisicos.get("info_"+i).toString();
		    	switch(Integer.parseInt(examenesFisicos.get("tipo_"+i).toString()))
		    	{
		    		case ConstantesBD.tipoParametrizablesTexto:
		    		case ConstantesBD.tipoParametrizablesTextArea:
		    		break;
		    		case ConstantesBD.tipoParametrizablesArchivo:
		    			temp = "";
		    		break;
		    		case ConstantesBD.tipoParametrizablesSelect:
		    			temp = UtilidadTexto.getBoolean(temp)?"Sí":"No";
		    		break;
		    	}
		    	sectionData[contador] = temp;
		    	contador++;
		    }
		    
		    report.addSectionData("antecedentesPediatricos", "camposParamExamenesFisicosTable", sectionData);
		    section.setTableCellsColSpan(1);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableBorderColor("camposParamExamenesFisicosTable", 0xFFFFFF);
	    	
		    section.insertTableIntoCell("antecedentesPediatricosTable", "camposParamExamenesFisicosTable", globalRow, 0);
		    globalRow++;
		    //****************************************************
	    }
    
    //Desarrollo Psicomotor-------------------------------------------------------------------------------------------------------------
    sectionData = new String[1];
    HashMap desarrolloPsico = (HashMap)datos.get("desarrolloPsicomotor");
    numRegistros = 0;
    
    if(desarrolloPsico!=null&&desarrolloPsico.get("numRegistros")!=null)
    	numRegistros = Integer.parseInt(desarrolloPsico.get("numRegistros").toString());
    
    if(numRegistros>0)
    {
    	if (!cabeceraAntecedentesAgregada) 
    	{
  			section = report.createSection("antecedentesPediatricos", "antecedentesPediatricosTable", 1, 1, 10);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
  			cabeceraAntecedentesAgregada = true;
  		}
    	
    	
	    //****************************************************
	    report.createSectionTable("antecedentesPediatricos", "desarrolloPsicomotorTable", numRegistros, 6);
  		report.font.setFontAttributes(0x000000, 10, true, false, false);
  		section.setTableBorder("desarrolloPsicomotorTable", 0xFFFFFF, 0.0f);
	    section.setTableCellBorderWidth("desarrolloPsicomotorTable", 0.5f);
	    section.setTableCellPadding("desarrolloPsicomotorTable", 1);
	    section.setTableSpaceBetweenCells("desarrolloPsicomotorTable", 0.5f);
	    section.setTableCellsDefaultColors("desarrolloPsicomotorTable", 0xFFFFFF, 0xFFFFFF);
  		report.addSectionTitle("antecedentesPediatricos", "desarrolloPsicomotorTable", "Desarrollo Psicomotor");
	    report.font.setFontAttributes(0x000000, 10, false, false, false);
	    section.setTableCellsColSpan(3);
	    
	    int cont = 0;
	    sectionData = new String[numRegistros*2];
	    for(int i=0;i<numRegistros;i++)
	    {
	    	sectionData[cont] = desarrolloPsico.get("nombre_"+i)+":" + " A la edad de " + desarrolloPsico.get("valor_"+i)+"";
	    	cont++;
	    	sectionData[cont] = "Descripción: " + desarrolloPsico.get("desc_"+i)+"";
	    	cont++;
	    	
	    	
	    }
	    
	    report.addSectionData("antecedentesPediatricos", "desarrolloPsicomotorTable", sectionData);
	    section.setTableCellsColSpan(1);
	    report.font.setFontAttributes(0x000000, 11, false, false, false);
	    section.setTableBorderColor("desarrolloPsicomotorTable", 0xFFFFFF);
    	
	    section.insertTableIntoCell("antecedentesPediatricosTable", "desarrolloPsicomotorTable", globalRow, 0);
	    globalRow++;
	    //****************************************************
    }
    
    
    //Desarrollo Lenguaje-------------------------------------------------------------------------------------------------------------
    sectionData = new String[1];
    HashMap desarrolloL = (HashMap)datos.get("desarrolloLenguaje");
    numRegistros = 0;
    
    if(desarrolloL!=null&&desarrolloL.get("numRegistros")!=null)
    	numRegistros = Integer.parseInt(desarrolloL.get("numRegistros").toString());
    
    if(numRegistros>0)
    {
    	if (!cabeceraAntecedentesAgregada) 
    	{
  			section = report.createSection("antecedentesPediatricos", "antecedentesPediatricosTable", 1, 1, 10);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
  			cabeceraAntecedentesAgregada = true;
  		}
    	
    	
	    //****************************************************
	    report.createSectionTable("antecedentesPediatricos", "desarrolloLenguajeTable", numRegistros, 6);
  		report.font.setFontAttributes(0x000000, 10, true, false, false);
  		section.setTableBorder("desarrolloLenguajeTable", 0xFFFFFF, 0.0f);
	    section.setTableCellBorderWidth("desarrolloLenguajeTable", 0.5f);
	    section.setTableCellPadding("desarrolloLenguajeTable", 1);
	    section.setTableSpaceBetweenCells("desarrolloLenguajeTable", 0.5f);
	    section.setTableCellsDefaultColors("desarrolloLenguajeTable", 0xFFFFFF, 0xFFFFFF);
  		report.addSectionTitle("antecedentesPediatricos", "desarrolloLenguajeTable", "Desarrollo Lenguaje");
	    report.font.setFontAttributes(0x000000, 10, false, false, false);
	    section.setTableCellsColSpan(3);
	    
	    int cont = 0;
	    sectionData = new String[numRegistros*2];
	    for(int i=0;i<numRegistros;i++)
	    {
	    	sectionData[cont] = desarrolloL.get("nombre_"+i)+":" + " A la edad de " + desarrolloL.get("valor_"+i)+"";
	    	cont++;
	    	sectionData[cont] = "Descripción: " + desarrolloL.get("desc_"+i)+"";
	    	cont++;
	    	
	    	
	    }
	    
	    report.addSectionData("antecedentesPediatricos", "desarrolloLenguajeTable", sectionData);
	    section.setTableCellsColSpan(1);
	    report.font.setFontAttributes(0x000000, 11, false, false, false);
	    section.setTableBorderColor("desarrolloLenguajeTable", 0xFFFFFF);
    	
	    section.insertTableIntoCell("antecedentesPediatricosTable", "desarrolloLenguajeTable", globalRow, 0);
	    globalRow++;
	    //****************************************************
    }
    
    //Desarrollo Lenguaje-------------------------------------------------------------------------------------------------------------
    sectionData = new String[1];
    HashMap desarrolloC = (HashMap)datos.get("desarrolloConductas");
    numRegistros = 0;
    
    if(desarrolloC!=null&&desarrolloC.get("numRegistros")!=null)
    	numRegistros = Integer.parseInt(desarrolloC.get("numRegistros").toString());
    
    if(numRegistros>0)
    {
    	if (!cabeceraAntecedentesAgregada) 
    	{
  			section = report.createSection("antecedentesPediatricos", "antecedentesPediatricosTable", 1, 1, 10);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
  			cabeceraAntecedentesAgregada = true;
  		}
    	
    	
	    //****************************************************
	    report.createSectionTable("antecedentesPediatricos", "desarrolloConductasTable", numRegistros, 6);
  		report.font.setFontAttributes(0x000000, 10, true, false, false);
  		section.setTableBorder("desarrolloConductasTable", 0xFFFFFF, 0.0f);
	    section.setTableCellBorderWidth("desarrolloConductasTable", 0.5f);
	    section.setTableCellPadding("desarrolloConductasTable", 1);
	    section.setTableSpaceBetweenCells("desarrolloConductasTable", 0.5f);
	    section.setTableCellsDefaultColors("desarrolloConductasTable", 0xFFFFFF, 0xFFFFFF);
  		report.addSectionTitle("antecedentesPediatricos", "desarrolloConductasTable", "Valoración del Desarrollo por Conductas");
	    report.font.setFontAttributes(0x000000, 10, false, false, false);
	    section.setTableCellsColSpan(3);
	    
	    int cont = 0;
	    sectionData = new String[numRegistros*2];
	    for(int i=0;i<numRegistros;i++)
	    {
	    	sectionData[cont] = desarrolloC.get("nombre_"+i)+": "+desarrolloC.get("valor_"+i)+"";
	    	cont++;
	    	sectionData[cont] = "Descripción: " + desarrolloC.get("desc_"+i)+"";
	    	cont++;
	    	
	    	
	    }
	    
	    report.addSectionData("antecedentesPediatricos", "desarrolloConductasTable", sectionData);
	    section.setTableCellsColSpan(1);
	    report.font.setFontAttributes(0x000000, 11, false, false, false);
	    section.setTableBorderColor("desarrolloConductasTable", 0xFFFFFF);
    	
	    section.insertTableIntoCell("antecedentesPediatricosTable", "desarrolloConductasTable", globalRow, 0);
	    globalRow++;
	    //****************************************************
    }
    
    //Observaciones del desarrollo-------------------------------------------------------------------------------------------------------------
    if(datos.get("observacionesDesarrollo")!=null&&!datos.get("observacionesDesarrollo").toString().equals(""))
    {
    	if (!cabeceraAntecedentesAgregada) 
    	{
  			section = report.createSection("antecedentesPediatricos", "antecedentesPediatricosTable", 1, 1, 10);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
  			cabeceraAntecedentesAgregada = true;
  		}
    	
    	
	    //****************************************************
	    report.createSectionTable("antecedentesPediatricos", "observacionesDesarrolloTable", 1, 6);
  		report.font.setFontAttributes(0x000000, 10, true, false, false);
  		section.setTableBorder("observacionesDesarrolloTable", 0xFFFFFF, 0.0f);
	    section.setTableCellBorderWidth("observacionesDesarrolloTable", 0.5f);
	    section.setTableCellPadding("observacionesDesarrolloTable", 1);
	    section.setTableSpaceBetweenCells("observacionesDesarrolloTable", 0.5f);
	    section.setTableCellsDefaultColors("observacionesDesarrolloTable", 0xFFFFFF, 0xFFFFFF);
  		report.addSectionTitle("antecedentesPediatricos", "observacionesDesarrolloTable", "Observaciones Desarrollo");
	    report.font.setFontAttributes(0x000000, 10, false, false, false);
	    section.setTableCellsColSpan(6);
	    
	    sectionData = new String[1];
	    String observaciones = datos.get("observacionesDesarrollo").toString();
	    observaciones = observaciones.replaceAll("<br>", ". ");
	    observaciones = observaciones.replaceAll("<BR>", ". ");
	    sectionData[0] = observaciones;
	    
	    report.addSectionData("antecedentesPediatricos", "observacionesDesarrolloTable", sectionData);
	    section.setTableCellsColSpan(1);
	    report.font.setFontAttributes(0x000000, 11, false, false, false);
	    section.setTableBorderColor("observacionesDesarrolloTable", 0xFFFFFF);
    	
	    section.insertTableIntoCell("antecedentesPediatricosTable", "observacionesDesarrolloTable", globalRow, 0);
	    globalRow++;
	    //****************************************************
    }
    
    //Campos Parametrizables Desarrollo -------------------------------------------------------------------------------------------
	    HashMap camposDesarrollo = (HashMap)datos.get("camposParamDesarrollo");
	    numRegistros = 0;
	    
	    if(camposDesarrollo!=null&&camposDesarrollo.get("numRegistros")!=null)
	    	numRegistros = Integer.parseInt(camposDesarrollo.get("numRegistros").toString());
	    
	    if(numRegistros>0)
	    {
	    	if (!cabeceraAntecedentesAgregada) 
	    	{
	  			section = report.createSection("antecedentesPediatricos", "antecedentesPediatricosTable", 1, 1, 10);
			    report.font.setFontAttributes(0x000000, 11, false, false, false);
	  			cabeceraAntecedentesAgregada = true;
	  		}
	    	
	    	
		    //****************************************************
		    report.createSectionTable("antecedentesPediatricos", "camposDesarrolloTable", numRegistros, 6);
	  		report.font.setFontAttributes(0x000000, 10, true, false, false);
	  		section.setTableBorder("camposDesarrolloTable", 0xFFFFFF, 0.0f);
		    section.setTableCellBorderWidth("camposDesarrolloTable", 0.5f);
		    section.setTableCellPadding("camposDesarrolloTable", 1);
		    section.setTableSpaceBetweenCells("camposDesarrolloTable", 0.5f);
		    section.setTableCellsDefaultColors("camposDesarrolloTable", 0xFFFFFF, 0xFFFFFF);
	  		//report.addSectionTitle("antecedentesPediatricos", "revisionSistemasTable", "Revisión por Sistemas");
		    report.font.setFontAttributes(0x000000, 10, false, false, false);
		    section.setTableCellsColSpan(3);
		    
		    int contador = 0;
		    sectionData = new String[numRegistros*2];
		    for(int i=0;i<numRegistros;i++)
		    {
		    	sectionData[contador] = camposDesarrollo.get("nombre_"+i).toString();
		    	contador++;
		    	String temp = camposDesarrollo.get("info_"+i).toString();
		    	switch(Integer.parseInt(camposDesarrollo.get("tipo_"+i).toString()))
		    	{
		    		case ConstantesBD.tipoParametrizablesTexto:
		    		case ConstantesBD.tipoParametrizablesTextArea:
		    		break;
		    		case ConstantesBD.tipoParametrizablesArchivo:
		    			temp = "";
		    		break;
		    		case ConstantesBD.tipoParametrizablesSelect:
		    			temp = UtilidadTexto.getBoolean(temp)?"Sí":"No";
		    		break;
		    	}
		    	sectionData[contador] = temp;
		    	contador++;
		    }
		    
		    report.addSectionData("antecedentesPediatricos", "camposDesarrolloTable", sectionData);
		    section.setTableCellsColSpan(1);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
		    section.setTableBorderColor("camposDesarrolloTable", 0xFFFFFF);
	    	
		    section.insertTableIntoCell("antecedentesPediatricosTable", "camposDesarrolloTable", globalRow, 0);
		    globalRow++;
		    //****************************************************
	    }
    
    //Sueño y hábitos -------------------------------------------------------------------------------------------------------------------
    if(datos.get("suenoHabitos")!=null&&!datos.get("suenoHabitos").toString().equals(""))
    {
    	if (!cabeceraAntecedentesAgregada) 
    	{
  			section = report.createSection("antecedentesPediatricos", "antecedentesPediatricosTable", 1, 1, 10);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
  			cabeceraAntecedentesAgregada = true;
  		}
    	
    	
	    //****************************************************
	    report.createSectionTable("antecedentesPediatricos", "suenoHabitosTable", 1, 6);
  		report.font.setFontAttributes(0x000000, 10, true, false, false);
  		section.setTableBorder("suenoHabitosTable", 0xFFFFFF, 0.0f);
	    section.setTableCellBorderWidth("suenoHabitosTable", 0.5f);
	    section.setTableCellPadding("suenoHabitosTable", 1);
	    section.setTableSpaceBetweenCells("suenoHabitosTable", 0.5f);
	    section.setTableCellsDefaultColors("suenoHabitosTable", 0xFFFFFF, 0xFFFFFF);
  		report.addSectionTitle("antecedentesPediatricos", "suenoHabitosTable", "Sueño y Hábitos");
	    report.font.setFontAttributes(0x000000, 10, false, false, false);
	    section.setTableCellsColSpan(6);
	    
	    sectionData = new String[1];
	    String observaciones = datos.get("suenoHabitos").toString();
	    observaciones = observaciones.replaceAll("<br>", ". ");
	    observaciones = observaciones.replaceAll("<BR>", ". ");
	    sectionData[0] = observaciones;
	    
	    report.addSectionData("antecedentesPediatricos", "suenoHabitosTable", sectionData);
	    section.setTableCellsColSpan(1);
	    report.font.setFontAttributes(0x000000, 11, false, false, false);
	    section.setTableBorderColor("suenoHabitosTable", 0xFFFFFF);
    	
	    section.insertTableIntoCell("antecedentesPediatricosTable", "suenoHabitosTable", globalRow, 0);
	    globalRow++;
	    //****************************************************
    }
    
    //Campos Parametrizables Sueño y Hábitos -------------------------------------------------------------------------------------------
    HashMap camposSuenoHabitos = (HashMap)datos.get("camposParamSuenoHabitos");
    numRegistros = 0;
    
    if(camposSuenoHabitos!=null&&camposSuenoHabitos.get("numRegistros")!=null)
    	numRegistros = Integer.parseInt(camposSuenoHabitos.get("numRegistros").toString());
    
    if(numRegistros>0)
    {
    	if (!cabeceraAntecedentesAgregada) 
    	{
  			section = report.createSection("antecedentesPediatricos", "antecedentesPediatricosTable", 1, 1, 10);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
  			cabeceraAntecedentesAgregada = true;
  		}
    	
    	
	    //****************************************************
	    report.createSectionTable("antecedentesPediatricos", "camposSuenoHabitosTable", numRegistros, 6);
  		report.font.setFontAttributes(0x000000, 10, true, false, false);
  		section.setTableBorder("camposSuenoHabitosTable", 0xFFFFFF, 0.0f);
	    section.setTableCellBorderWidth("camposSuenoHabitosTable", 0.5f);
	    section.setTableCellPadding("camposSuenoHabitosTable", 1);
	    section.setTableSpaceBetweenCells("camposSuenoHabitosTable", 0.5f);
	    section.setTableCellsDefaultColors("camposSuenoHabitosTable", 0xFFFFFF, 0xFFFFFF);
  		//report.addSectionTitle("antecedentesPediatricos", "revisionSistemasTable", "Revisión por Sistemas");
	    report.font.setFontAttributes(0x000000, 10, false, false, false);
	    section.setTableCellsColSpan(3);
	    
	    int contador = 0;
	    sectionData = new String[numRegistros*2];
	    for(int i=0;i<numRegistros;i++)
	    {
	    	sectionData[contador] = camposSuenoHabitos.get("nombre_"+i).toString();
	    	contador++;
	    	String temp = camposSuenoHabitos.get("info_"+i).toString();
	    	switch(Integer.parseInt(camposSuenoHabitos.get("tipo_"+i).toString()))
	    	{
	    		case ConstantesBD.tipoParametrizablesTexto:
	    		case ConstantesBD.tipoParametrizablesTextArea:
	    		break;
	    		case ConstantesBD.tipoParametrizablesArchivo:
	    			temp = "";
	    		break;
	    		case ConstantesBD.tipoParametrizablesSelect:
	    			temp = UtilidadTexto.getBoolean(temp)?"Sí":"No";
	    		break;
	    	}
	    	sectionData[contador] = temp;
	    	contador++;
	    }
	    
	    report.addSectionData("antecedentesPediatricos", "camposSuenoHabitosTable", sectionData);
	    section.setTableCellsColSpan(1);
	    report.font.setFontAttributes(0x000000, 11, false, false, false);
	    section.setTableBorderColor("camposSuenoHabitosTable", 0xFFFFFF);
    	
	    section.insertTableIntoCell("antecedentesPediatricosTable", "camposSuenoHabitosTable", globalRow, 0);
	    globalRow++;
	    //****************************************************
    }
    
    //Valoración Nutricional-------------------------------------------------------------------------------------------------------------
    sectionData = new String[1];
    HashMap nutricional = (HashMap)datos.get("valNutricional");
    numRegistros = 0;
    
    if(nutricional!=null&&nutricional.get("numRegistros")!=null)
    	numRegistros = Integer.parseInt(nutricional.get("numRegistros").toString());
    
    if(numRegistros>0)
    {
    	if (!cabeceraAntecedentesAgregada) 
    	{
  			section = report.createSection("antecedentesPediatricos", "antecedentesPediatricosTable", 1, 1, 10);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
  			cabeceraAntecedentesAgregada = true;
  		}
    	
    	
	    //****************************************************
	    report.createSectionTable("antecedentesPediatricos", "nutricionalTable", numRegistros, 6);
  		report.font.setFontAttributes(0x000000, 10, true, false, false);
  		section.setTableBorder("nutricionalTable", 0xFFFFFF, 0.0f);
	    section.setTableCellBorderWidth("nutricionalTable", 0.5f);
	    section.setTableCellPadding("nutricionalTable", 1);
	    section.setTableSpaceBetweenCells("nutricionalTable", 0.5f);
	    section.setTableCellsDefaultColors("nutricionalTable", 0xFFFFFF, 0xFFFFFF);
  		report.addSectionTitle("antecedentesPediatricos", "nutricionalTable", "Valoración Nutricional");
	    report.font.setFontAttributes(0x000000, 10, false, false, false);
	    section.setTableCellsColSpan(3);
	    
	    int cont = 0;
	    sectionData = new String[numRegistros*2];
	    for(int i=0;i<numRegistros;i++)
	    {
	    	sectionData[cont] = nutricional.get("nombre_"+i).toString();
	    	cont++;
	    	sectionData[cont] = nutricional.get("valor_"+i).toString();
	    	cont++;
	    	
	    	
	    }
	    
	    report.addSectionData("antecedentesPediatricos", "nutricionalTable", sectionData);
	    section.setTableCellsColSpan(1);
	    report.font.setFontAttributes(0x000000, 11, false, false, false);
	    section.setTableBorderColor("nutricionalTable", 0xFFFFFF);
    	
	    section.insertTableIntoCell("antecedentesPediatricosTable", "nutricionalTable", globalRow, 0);
	    globalRow++;
	    //****************************************************
    }
    
    
    
    //Tipo Alimentación -------------------------------------------------------------------------------------------------------------------
    if(datos.get("tipoAlimentacion")!=null&&!datos.get("tipoAlimentacion").toString().equals(""))
    {
    	if (!cabeceraAntecedentesAgregada) 
    	{
  			section = report.createSection("antecedentesPediatricos", "antecedentesPediatricosTable", 1, 1, 10);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
  			cabeceraAntecedentesAgregada = true;
  		}
    	
    	
	    //****************************************************
	    report.createSectionTable("antecedentesPediatricos", "tipoAlimentacionTable", 1, 6);
  		report.font.setFontAttributes(0x000000, 10, true, false, false);
  		section.setTableBorder("tipoAlimentacionTable", 0xFFFFFF, 0.0f);
	    section.setTableCellBorderWidth("tipoAlimentacionTable", 0.5f);
	    section.setTableCellPadding("tipoAlimentacionTable", 1);
	    section.setTableSpaceBetweenCells("tipoAlimentacionTable", 0.5f);
	    section.setTableCellsDefaultColors("tipoAlimentacionTable", 0xFFFFFF, 0xFFFFFF);
  		report.addSectionTitle("antecedentesPediatricos", "tipoAlimentacionTable", "Tipo de Alimentación");
	    report.font.setFontAttributes(0x000000, 10, false, false, false);
	    section.setTableCellsColSpan(6);
	    
	    sectionData = new String[1];
	    String observaciones = datos.get("tipoAlimentacion").toString();
	    observaciones = observaciones.replaceAll("<br>", ". ");
	    observaciones = observaciones.replaceAll("<BR>", ". ");
	    sectionData[0] = observaciones;
	    
	    report.addSectionData("antecedentesPediatricos", "tipoAlimentacionTable", sectionData);
	    section.setTableCellsColSpan(1);
	    report.font.setFontAttributes(0x000000, 11, false, false, false);
	    section.setTableBorderColor("tipoAlimentacionTable", 0xFFFFFF);
    	
	    section.insertTableIntoCell("antecedentesPediatricosTable", "tipoAlimentacionTable", globalRow, 0);
	    globalRow++;
	    //****************************************************
    }
    
    //Observaciones Valoración Nutricional-----------------------------------------------------------------------------------------------------
    if(datos.get("observacionValNutricional")!=null&&!datos.get("observacionValNutricional").toString().equals(""))
    {
    	if (!cabeceraAntecedentesAgregada) 
    	{
  			section = report.createSection("antecedentesPediatricos", "antecedentesPediatricosTable", 1, 1, 10);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
  			cabeceraAntecedentesAgregada = true;
  		}
    	
    	
	    //****************************************************
	    report.createSectionTable("antecedentesPediatricos", "observacionValNutricionalTable", 1, 6);
  		report.font.setFontAttributes(0x000000, 10, true, false, false);
  		section.setTableBorder("observacionValNutricionalTable", 0xFFFFFF, 0.0f);
	    section.setTableCellBorderWidth("observacionValNutricionalTable", 0.5f);
	    section.setTableCellPadding("observacionValNutricionalTable", 1);
	    section.setTableSpaceBetweenCells("observacionValNutricionalTable", 0.5f);
	    section.setTableCellsDefaultColors("observacionValNutricionalTable", 0xFFFFFF, 0xFFFFFF);
  		report.addSectionTitle("antecedentesPediatricos", "observacionValNutricionalTable", "Observaciones Valoración Nutricional");
	    report.font.setFontAttributes(0x000000, 10, false, false, false);
	    section.setTableCellsColSpan(6);
	    
	    sectionData = new String[1];
	    String observaciones = datos.get("observacionValNutricional").toString();
	    observaciones = observaciones.replaceAll("<br>", ". ");
	    observaciones = observaciones.replaceAll("<BR>", ". ");
	    sectionData[0] = observaciones;
	    
	    report.addSectionData("antecedentesPediatricos", "observacionValNutricionalTable", sectionData);
	    section.setTableCellsColSpan(1);
	    report.font.setFontAttributes(0x000000, 11, false, false, false);
	    section.setTableBorderColor("observacionValNutricionalTable", 0xFFFFFF);
    	
	    section.insertTableIntoCell("antecedentesPediatricosTable", "observacionValNutricionalTable", globalRow, 0);
	    globalRow++;
	    //****************************************************
    }
    
    //Campos Parametrizables Valoracion Nutricional -------------------------------------------------------------------------------------------
    nutricional = (HashMap)datos.get("camposParamValoracionNutricional");
    numRegistros = 0;
    
    if(nutricional!=null&&nutricional.get("numRegistros")!=null)
    	numRegistros = Integer.parseInt(nutricional.get("numRegistros").toString());
    
    if(numRegistros>0)
    {
    	if (!cabeceraAntecedentesAgregada) 
    	{
  			section = report.createSection("antecedentesPediatricos", "antecedentesPediatricosTable", 1, 1, 10);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
  			cabeceraAntecedentesAgregada = true;
  		}
    	
    	
	    //****************************************************
	    report.createSectionTable("antecedentesPediatricos", "camposParamValoracionNutricionalTable", numRegistros, 6);
  		report.font.setFontAttributes(0x000000, 10, true, false, false);
  		section.setTableBorder("camposParamValoracionNutricionalTable", 0xFFFFFF, 0.0f);
	    section.setTableCellBorderWidth("camposParamValoracionNutricionalTable", 0.5f);
	    section.setTableCellPadding("camposParamValoracionNutricionalTable", 1);
	    section.setTableSpaceBetweenCells("camposParamValoracionNutricionalTable", 0.5f);
	    section.setTableCellsDefaultColors("camposParamValoracionNutricionalTable", 0xFFFFFF, 0xFFFFFF);
  		//report.addSectionTitle("antecedentesPediatricos", "revisionSistemasTable", "Revisión por Sistemas");
	    report.font.setFontAttributes(0x000000, 10, false, false, false);
	    section.setTableCellsColSpan(3);
	    
	    int contador = 0;
	    sectionData = new String[numRegistros*2];
	    for(int i=0;i<numRegistros;i++)
	    {
	    	sectionData[contador] = nutricional.get("nombre_"+i).toString();
	    	contador++;
	    	String temp = nutricional.get("info_"+i).toString();
	    	switch(Integer.parseInt(nutricional.get("tipo_"+i).toString()))
	    	{
	    		case ConstantesBD.tipoParametrizablesTexto:
	    		case ConstantesBD.tipoParametrizablesTextArea:
	    		break;
	    		case ConstantesBD.tipoParametrizablesArchivo:
	    			temp = "";
	    		break;
	    		case ConstantesBD.tipoParametrizablesSelect:
	    			temp = UtilidadTexto.getBoolean(temp)?"Sí":"No";
	    		break;
	    	}
	    	sectionData[contador] = temp;
	    	contador++;
	    }
	    
	    report.addSectionData("antecedentesPediatricos", "camposParamValoracionNutricionalTable", sectionData);
	    section.setTableCellsColSpan(1);
	    report.font.setFontAttributes(0x000000, 11, false, false, false);
	    section.setTableBorderColor("camposParamValoracionNutricionalTable", 0xFFFFFF);
    	
	    section.insertTableIntoCell("antecedentesPediatricosTable", "camposParamValoracionNutricionalTable", globalRow, 0);
	    globalRow++;
	    //****************************************************
    }
    
    //Causa Externa ---------------------------------------------------------------------------------------------------------------------
    sectionData = new String[1];
    String causaExterna = (String) datos.get("causaExterna");
    hayDatos = (causaExterna != null && causaExterna.length() > 0);
    
    if (hayDatos) {
    	sectionData[0] = causaExterna;
    	if (!cabeceraAntecedentesAgregada) {
  			//report.document.addParagraph("Antecedentes Pediatricos", report.font, 30);
  			section = report.createSection("antecedentesPediatricos", "antecedentesPediatricosTable", 1, 1, 10);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
  			cabeceraAntecedentesAgregada = true;
  		}
    	
    	report.createSectionTable("antecedentesPediatricos", "causaExternaTable", 1, 6);
  		report.font.setFontAttributes(0x000000, 11, true, false, false);
  		section.setTableBorder("causaExternaTable", 0xFFFFFF, 0.0f);
	    section.setTableCellBorderWidth("causaExternaTable", 0.5f);
	    section.setTableCellPadding("causaExternaTable", 1);
	    section.setTableSpaceBetweenCells("causaExternaTable", 0.5f);
	    section.setTableCellsDefaultColors("causaExternaTable", 0xFFFFFF, 0xFFFFFF);
	    
	    section.setTableCellsColSpan(1);
	    sectionData[0] = "Causa Externa: ";
	    section.addTableTextCellAligned("causaExternaTable", sectionData[0], report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, 0, 0);
	    //report.addSection("antecedentesPediatricos", "causaExternaTable", sectionData);
	    report.font.setFontAttributes(0x000000, 11, false, false, false);
	    section.setTableCellsColSpan(5);
	    sectionData[0] = causaExterna;
	    //report.addSectionData("antecedentesPediatricos", "causaExternaTable", sectionData);
	    section.addTableTextCellAligned("causaExternaTable", sectionData[0], report.font,iTextBaseDocument.ALIGNMENT_TOP,iTextBaseDocument.ALIGNMENT_LEFT, 0, 1);
	    section.setTableCellsColSpan(1);
	    section.setTableBorderColor("causaExternaTable", 0xFFFFFF);
    	
	    section.insertTableIntoCell("antecedentesPediatricosTable", "causaExternaTable", globalRow, 0);
	    globalRow++;
	    
	    /*report.createSection("causaExterna", "causaExternaTable", 1, 1, 2);	
    	report.font.setFontAttributes(0x000000, 11, true, false, false);
    	report.addSectionTitle("causaExterna", "causaExternaTable", "Causa Externa");
    	report.font.setFontAttributes(0x000000, 11, false, false, false);
    	report.addSectionData("causaExterna", "causaExternaTable", sectionData);
    	report.addSectionToDocument("causaExterna");*/
    }
    
    //Campos Parametrizables Causa Externa -------------------------------------------------------------------------------------------
    HashMap camposCausaExterna = (HashMap)datos.get("camposParamCausaExterna");
    numRegistros = 0;
    
    if(camposCausaExterna!=null&&camposCausaExterna.get("numRegistros")!=null)
    	numRegistros = Integer.parseInt(camposCausaExterna.get("numRegistros").toString());
    
    if(numRegistros>0)
    {
    	if (!cabeceraAntecedentesAgregada) 
    	{
  			section = report.createSection("antecedentesPediatricos", "antecedentesPediatricosTable", 1, 1, 10);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
  			cabeceraAntecedentesAgregada = true;
  		}
    	
    	
	    //****************************************************
	    report.createSectionTable("antecedentesPediatricos", "camposCausaExternaTable", numRegistros, 6);
  		report.font.setFontAttributes(0x000000, 10, true, false, false);
  		section.setTableBorder("camposCausaExternaTable", 0xFFFFFF, 0.0f);
	    section.setTableCellBorderWidth("camposCausaExternaTable", 0.5f);
	    section.setTableCellPadding("camposCausaExternaTable", 1);
	    section.setTableSpaceBetweenCells("camposCausaExternaTable", 0.5f);
	    section.setTableCellsDefaultColors("camposCausaExternaTable", 0xFFFFFF, 0xFFFFFF);
  		//report.addSectionTitle("antecedentesPediatricos", "revisionSistemasTable", "Revisión por Sistemas");
	    report.font.setFontAttributes(0x000000, 10, false, false, false);
	    section.setTableCellsColSpan(3);
	    
	    int contador = 0;
	    sectionData = new String[numRegistros*2];
	    for(int i=0;i<numRegistros;i++)
	    {
	    	sectionData[contador] = camposCausaExterna.get("nombre_"+i).toString();
	    	contador++;
	    	String temp = camposCausaExterna.get("info_"+i).toString();
	    	switch(Integer.parseInt(camposCausaExterna.get("tipo_"+i).toString()))
	    	{
	    		case ConstantesBD.tipoParametrizablesTexto:
	    		case ConstantesBD.tipoParametrizablesTextArea:
	    		break;
	    		case ConstantesBD.tipoParametrizablesArchivo:
	    			temp = "";
	    		break;
	    		case ConstantesBD.tipoParametrizablesSelect:
	    			temp = UtilidadTexto.getBoolean(temp)?"Sí":"No";
	    		break;
	    	}
	    	sectionData[contador] = temp;
	    	contador++;
	    }
	    
	    report.addSectionData("antecedentesPediatricos", "camposCausaExternaTable", sectionData);
	    section.setTableCellsColSpan(1);
	    report.font.setFontAttributes(0x000000, 11, false, false, false);
	    section.setTableBorderColor("camposCausaExternaTable", 0xFFFFFF);
    	
	    section.insertTableIntoCell("antecedentesPediatricosTable", "camposCausaExternaTable", globalRow, 0);
	    globalRow++;
	    //****************************************************
    }
    
    //Diagnosticos ----------------------------------------------------------------------------------------------------------------------
    String dxIngresoStr = "";
    String dxPrincipalStr = "";
    ArrayList dxRelacionadosArray = new ArrayList();
    String diagnosticoStr = "";
    Iterator it;
    float [] w;
    Map dxIngreso              = (Map) datos.get("dxIngreso");
    Collection dxDefinitivos = (Collection) datos.get("dxDefinitivos");
    hayDatos = (dxIngreso != null && dxIngreso.size() > 0);
    
  	if (hayDatos) {

    	if (!cabeceraAntecedentesAgregada) {
  			//report.document.addParagraph("Antecedentes Pediatricos", report.font, 30);
  			section = report.createSection("antecedentesPediatricos", "antecedentesPediatricosTable", 1, 1, 10);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
  			cabeceraAntecedentesAgregada = true;
  		}
  		
  		dxIngresoStr = dxIngreso.get("acronimo") + "-" + dxIngreso.get("tipoCie") + " " + dxIngreso.get("nombre");
  		
  		if (dxDefinitivos != null && dxDefinitivos.size() > 0) {
        	it = dxDefinitivos.iterator();
        	
        	while (it.hasNext()) {
        		Diagnostico diag = (Diagnostico) it.next();
        		
        		if (diag.isPrincipal()) {
        			dxPrincipalStr = diag.getAcronimo() +"-" + diag.getTipoCIE() +"-" +diag.getNombre();		        			 
        		}
        		else {
        			diagnosticoStr = diag.getNumero() + "-" +diag.getAcronimo() +"-" + diag.getTipoCIE() +"-" +diag.getNombre();
        			dxRelacionadosArray.add(diagnosticoStr);
        		}
        	}
        }
  		
  		if(dxPrincipalStr.length() > 0  || dxIngresoStr.length() > 0 || dxRelacionadosArray.size() > 0 ){
  			//report.createSectionTable("antecedentesPediatricos", "diagnosticosTable", 1, 2);
  			
  			/*report.createSection("diagnosticos", "diagnosticosTable", 1, 2, 2);
    		report.addSectionToDocument("diagnosticos");*/
    		
		    
    		if (dxIngresoStr.length() > 0 ){
    			report.createSectionTable("antecedentesPediatricos", "diagnosticosTable", 1, 6);
    			section.setTableBorder("diagnosticosTable", 0xFFFFFF, 0.0f);
			    section.setTableCellBorderWidth("diagnosticosTable", 0.5f);
			    section.setTableCellPadding("diagnosticosTable", 1);
			    section.setTableSpaceBetweenCells("diagnosticosTable", 0.5f);
			    section.setTableCellsDefaultColors("diagnosticosTable", 0xFFFFFF, 0xFFFFFF);
    			//report.createSection("diagnosticos0", "diagnosticosTable", 1, 2, 2);
		    	
		    	w = new float [2];
		    	w[0] = 10f; 
		    	w[1] = 90f;
		    	report.setColumnsWidths("antecedentesPediatricos", "diagnosticosTable",  w);
		    	//report.setColumnsWidths("diagnosticos0", "diagnosticosTable",  w);
		    	
		    	section.setTableCellsColSpan(1);
		    	report.font.setFontAttributes(0x000000, 11, true, false, false);
		    	section.addTableTextCell("diagnosticosTable", "Dx de ingreso: ", report.font);
		    	//report.getSectionReference("diagnosticos0").addTableTextCell("diagnosticosTable", "Dx de ingreso: ", report.font);
		    	report.font.setFontAttributes(0x000000, 11, false, false, false);
		    	section.setTableCellsColSpan(5);
		    	section.addTableTextCell("diagnosticosTable", dxIngresoStr, report.font);
		    	section.setTableCellsColSpan(1);
		    	//report.getSectionReference("diagnosticos0").addTableTextCell("diagnosticosTable", dxIngresoStr, report.font);
		    	//report.addSectionToDocument("diagnosticos0");
		    	
		    	section.insertTableIntoCell("antecedentesPediatricosTable", "diagnosticosTable", globalRow, 0);
			    globalRow++;
		    }
		    	
		    if (dxPrincipalStr.length() > 0 ){
		    	report.createSectionTable("antecedentesPediatricos", "diagnosticosTable2", 1, 6);
		    	section.setTableBorder("diagnosticosTable2", 0xFFFFFF, 0.0f);
			    section.setTableCellBorderWidth("diagnosticosTable2", 0.5f);
			    section.setTableCellPadding("diagnosticosTable2", 1);
			    section.setTableSpaceBetweenCells("diagnosticosTable2", 0.5f);
			    section.setTableCellsDefaultColors("diagnosticosTable2", 0xFFFFFF, 0xFFFFFF);
		    	//report.createSection("diagnosticos1", "diagnosticosTable", 1, 2, 2);

		    	w = new float [2];
		    	w[0] = 10f; 
		    	w[1] = 90f;
		    	report.setColumnsWidths("antecedentesPediatricos", "diagnosticosTable2",  w);
		    	//report.setColumnsWidths("diagnosticos1", "diagnosticosTable",  w);
		    	
		    	section.setTableCellsColSpan(1);
		    	report.font.setFontAttributes(0x000000, 11, true, false, false);
		    	section.addTableTextCell("diagnosticosTable2", "Dx principal: ", report.font);
		    	//report.getSectionReference("diagnosticos1").addTableTextCell("diagnosticosTable", "Dx principal: ", report.font);
		    	report.font.setFontAttributes(0x000000, 11, false, false, false);
		    	section.setTableCellsColSpan(5);
		    	section.addTableTextCell("diagnosticosTable2", dxPrincipalStr, report.font);
		    	section.setTableCellsColSpan(1);
		    	//report.getSectionReference("diagnosticos1").addTableTextCell("diagnosticosTable", dxPrincipalStr, report.font);
		    	//report.addSectionToDocument("diagnosticos1");
		    	
		    	section.insertTableIntoCell("antecedentesPediatricosTable", "diagnosticosTable2", globalRow, 0);
			    globalRow++;
		    }
    						    
		    if (dxRelacionadosArray.size() > 0 ){
		    	report.createSectionTable("antecedentesPediatricos", "diagnosticosTable3", dxRelacionadosArray.size(), 6);
		    	section.setTableBorder("diagnosticosTable3", 0xFFFFFF, 0.0f);
			    section.setTableCellBorderWidth("diagnosticosTable3", 0.5f);
			    section.setTableCellPadding("diagnosticosTable3", 1);
			    section.setTableSpaceBetweenCells("diagnosticosTable3", 0.5f);
			    section.setTableCellsDefaultColors("diagnosticosTable3", 0xFFFFFF, 0xFFFFFF);
		    	//report.createSection("diagnosticos2", "diagnosticosTable", dxRelacionadosArray.size(), 2, 2);
		    	
		    	w = new float [2];
		    	w[0] = 10f; 
		    	w[1] = 90f;
		    	report.setColumnsWidths("antecedentesPediatricos", "diagnosticosTable3",  w);
		    	//report.setColumnsWidths("diagnosticos2", "diagnosticosTable",  w);
		    	
		    	section.setTableCellsRowSpan(dxRelacionadosArray.size());
		    	//report.getSectionReference("diagnosticos2").setTableCellsRowSpan(dxRelacionadosArray.size());
					report.font.setFontAttributes(0x000000, 11, true, false, false);
		    	section.addTableTextCellAligned("diagnosticosTable3", "Dx relacionados: ", report.font, iTextBaseDocument.ALIGNMENT_MIDDLE, iTextBaseDocument.ALIGNMENT_TOP);
				//report.getSectionReference("diagnosticos2").addTableTextCellAligned("diagnosticosTable", "Dx relacionados: ", report.font, iTextBaseDocument.ALIGNMENT_MIDDLE, iTextBaseDocument.ALIGNMENT_TOP);
		    	report.font.setFontAttributes(0x000000, 11, false, false, false);
		    	section.setTableCellsRowSpan(1);
		    	//report.getSectionReference("diagnosticos2").setTableCellsRowSpan(1);
		    	
		    	section.setTableCellsColSpan(5);
		    	it = dxRelacionadosArray.iterator();
		    	while(it.hasNext()){
		    		diagnosticoStr = it.next() + ""; 
		    		section.addTableTextCell("diagnosticosTable3", diagnosticoStr, report.font);
		    		//report.getSectionReference("diagnosticos2").addTableTextCell("diagnosticosTable", diagnosticoStr, report.font);
		    	}
		    	section.setTableCellsColSpan(1);
		    	section.insertTableIntoCell("antecedentesPediatricosTable", "diagnosticosTable3", globalRow, 0);
			    globalRow++;
		    	//report.addSectionToDocument("diagnosticos2");
		    }
        }
  	}
  	
  	//Seccion: Plan diagnóstico y terapeútico --------------------------------------------------------------------------------------------
  	sectionData = new String[1];
  	String plan = (String) datos.get("plan");
  	hayDatos = (plan != null && plan.length() > 0);
  	
  	if (hayDatos) {
  		textData = plan;
  		
  		
  		 textData = textData.replaceAll("\n", "ç");
		   textData = textData.replaceAll("\r\n", "ç");
		   textData = textData.replaceAll("\n\r", "ç");
		   textData = textData.replaceAll("\r", "ç");
		   String [] temporal = textData.split("ç");
		   int i = 0;
		  
		  textData = "";
		  while(temporal[i].length() == 0) {
		  	i++;	
		  }
		  i++;
		  
		  
		  for ( ; i < temporal.length-1;i++) textData  += temporal[i] + "  ";
		  
    	if (!cabeceraAntecedentesAgregada) {
  			//report.document.addParagraph("Antecedentes Pediatricos", report.font, 30);
  			section = report.createSection("antecedentesPediatricos", "antecedentesPediatricosTable", 1, 1, 10);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
  			cabeceraAntecedentesAgregada = true;
  		}

		  
		  
  		report.createSectionTable("antecedentesPediatricos", "planTable", 2, 6);
  		report.font.setFontAttributes(0x000000, 11, true, false, false);
  		section.setTableBorder("planTable", 0xFFFFFF, 0.0f);
	    section.setTableCellBorderWidth("planTable", 0.5f);
	    section.setTableCellPadding("planTable", 1);
	    section.setTableSpaceBetweenCells("planTable", 0.5f);
	    section.setTableCellsDefaultColors("planTable", 0xFFFFFF, 0xFFFFFF);
  		//report.addSectionTitle("antecedentesPediatricos", "planTable", "Plan diagnóstico y terapeútico");
	    
	    
	    section.setTableCellsColSpan(2);
	    report.font.setFontAttributes(0x000000, 11, true, false, false);
	    section.addTableTextCellAligned("planTable","Plan diagnóstico y terapeútico: ",report.font, iTextBaseDocument.ALIGNMENT_MIDDLE, iTextBaseDocument.ALIGNMENT_TOP);
	    report.addSectionData("antecedentesPediatricos", "planTable", sectionData);
	    section.setTableCellsColSpan(4);
	    report.font.setFontAttributes(0x000000, 11, false, false, false);
	    section.addTableTextCellAligned("planTable", textData,report.font, iTextBaseDocument.ALIGNMENT_MIDDLE, iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.setTableBorderColor("planTable", 0xFFFFFF);
	    section.insertTableIntoCell("antecedentesPediatricosTable", "planTable", globalRow, 0);
	    globalRow++;
	    
	    /*report.createSection("plan", "planTable", 1, 1, 2);	
    	report.font.setFontAttributes(0x000000, 11, true, false, false);
    	report.addSectionTitle("plan", "planTable", "Plan diagnóstico y terapeútico");
    	report.font.setFontAttributes(0x000000, 11, false, false, false);
    	report.addSectionData("plan", "planTable", sectionData);
    	report.addSectionToDocument("plan");	*/
  	}
  	
  	
  	
  	
   	//Seccion: Comentarios generalesa ----------------------------------------------------------------------------------------------------------------
  	sectionData = new String[1];
  	String comentariosGenerales  = (String) datos.get("comentariosGenerales");
  	hayDatos = (comentariosGenerales != null && comentariosGenerales.length() > 0);
  	
  	if (hayDatos) {
  		textData = comentariosGenerales;
  		textData = textData.replaceAll("\n", "ç");
		   textData = textData.replaceAll("\r\n", "ç");
		   textData = textData.replaceAll("\n\r", "ç");
		   textData = textData.replaceAll("\r", "ç");
		   String [] temporal = textData.split("ç");
		   int i = 0;
		  
		  textData = "";
		  while(temporal[i].length() == 0) {
		  	i++;	
		  }
		  i++;
		  
		  
		  for ( ; i < temporal.length-1;i++) textData  += temporal[i] + "  ";
  		
    	if (!cabeceraAntecedentesAgregada) {
  			//report.document.addParagraph("Antecedentes Pediatricos", report.font, 30);
  			section = report.createSection("antecedentesPediatricos", "antecedentesPediatricosTable", 1, 1, 10);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
  			cabeceraAntecedentesAgregada = true;
  		}

		  
		  
    	
  		report.createSectionTable("antecedentesPediatricos", "comentariosGeneralesTable", 2, 6);
  		report.font.setFontAttributes(0x000000, 11, true, false, false);
  		section.setTableBorder("comentariosGeneralesTable", 0xFFFFFF, 0.0f);
	    section.setTableCellBorderWidth("comentariosGeneralesTable", 0.5f);
	    section.setTableCellPadding("comentariosGeneralesTable", 1);
	    section.setTableSpaceBetweenCells("comentariosGeneralesTable", 0.5f);
	    section.setTableCellsDefaultColors("comentariosGeneralesTable", 0xFFFFFF, 0xFFFFFF);
  		//report.addSectionTitle("antecedentesPediatricos", "pronosticoTable", "Pronóstico");
	    
	    section.setTableCellsColSpan(2);
	    report.font.setFontAttributes(0x000000, 11, true, false, false);
	    section.addTableTextCellAligned("comentariosGeneralesTable", "Comentarios Generales: ",report.font, iTextBaseDocument.ALIGNMENT_MIDDLE, iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(4);
	    report.font.setFontAttributes(0x000000, 11, false, false, false);
	    section.addTableTextCellAligned("comentariosGeneralesTable", textData,report.font,iTextBaseDocument.ALIGNMENT_MIDDLE, iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);  
	    section.setTableBorderColor("comentariosGeneralesTable", 0xFFFFFF);
  		
	    section.insertTableIntoCell("antecedentesPediatricosTable", "comentariosGeneralesTable", globalRow, 0);
	    globalRow++;
	    
	    /*report.createSection("pronostico", "pronosticoTable", 1, 1, 2);	
    	report.font.setFontAttributes(0x000000, 11, true, false, false);
    	report.addSectionTitle("pronostico", "pronosticoTable", "Pronóstico");
    	report.font.setFontAttributes(0x000000, 11, false, false, false);
    	report.addSectionData("pronostico", "pronosticoTable", sectionData);
    	report.addSectionToDocument("pronostico");*/	
  	}

  	
  	
  	
  	
  	
  	//Seccion: Pronóstico ----------------------------------------------------------------------------------------------------------------
  	sectionData = new String[1];
  	String pronostico = (String) datos.get("pronostico");
  	hayDatos = (pronostico != null && pronostico.length() > 0);
  	
  	if (hayDatos) {
  		textData = pronostico;
  		textData = textData.replaceAll("\n", "ç");
		   textData = textData.replaceAll("\r\n", "ç");
		   textData = textData.replaceAll("\n\r", "ç");
		   textData = textData.replaceAll("\r", "ç");
		   String [] temporal = textData.split("ç");
		   int i = 0;
		  
		  textData = "";
		  while(temporal[i].length() == 0) {
		  	i++;	
		  }
		  i++;
		  
		  
		  for ( ; i < temporal.length-1;i++) textData  += temporal[i] + "  ";
  		
    	if (!cabeceraAntecedentesAgregada) {
  			//report.document.addParagraph("Antecedentes Pediatricos", report.font, 30);
  			section = report.createSection("antecedentesPediatricos", "antecedentesPediatricosTable", 1, 1, 10);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
  			cabeceraAntecedentesAgregada = true;
  		}

    	
  		report.createSectionTable("antecedentesPediatricos", "pronosticoTable", 2, 6);
  		report.font.setFontAttributes(0x000000, 11, true, false, false);
  		section.setTableBorder("pronosticoTable", 0xFFFFFF, 0.0f);
	    section.setTableCellBorderWidth("pronosticoTable", 0.5f);
	    section.setTableCellPadding("pronosticoTable", 1);
	    section.setTableSpaceBetweenCells("pronosticoTable", 0.5f);
	    section.setTableCellsDefaultColors("pronosticoTable", 0xFFFFFF, 0xFFFFFF);
  		//report.addSectionTitle("antecedentesPediatricos", "pronosticoTable", "Pronóstico");
	    
	    section.setTableCellsColSpan(2);
	    report.font.setFontAttributes(0x000000, 11, true, false, false);
	    section.addTableTextCellAligned("pronosticoTable", "Deberes y/o derechos del paciente: ",report.font, iTextBaseDocument.ALIGNMENT_MIDDLE, iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(4);
	    report.font.setFontAttributes(0x000000, 11, false, false, false);
	    section.addTableTextCellAligned("pronosticoTable", textData,report.font, iTextBaseDocument.ALIGNMENT_MIDDLE, iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);  
	    section.setTableBorderColor("pronosticoTable", 0xFFFFFF);
  		
	    section.insertTableIntoCell("antecedentesPediatricosTable", "pronosticoTable", globalRow, 0);
	    globalRow++;
	    
	    /*report.createSection("pronostico", "pronosticoTable", 1, 1, 2);	
    	report.font.setFontAttributes(0x000000, 11, true, false, false);
    	report.addSectionTitle("pronostico", "pronosticoTable", "Pronóstico");
    	report.font.setFontAttributes(0x000000, 11, false, false, false);
    	report.addSectionData("pronostico", "pronosticoTable", sectionData);
    	report.addSectionToDocument("pronostico");*/	
  	}
  	

  	//Campos Parametrizables Observaciones -------------------------------------------------------------------------------------------
    HashMap camposObservaciones = (HashMap)datos.get("camposParamObservaciones");
    numRegistros = 0;
    
    if(camposObservaciones!=null&&camposObservaciones.get("numRegistros")!=null)
    	numRegistros = Integer.parseInt(camposObservaciones.get("numRegistros").toString());
    
    if(numRegistros>0)
    {
    	if (!cabeceraAntecedentesAgregada) 
    	{
  			section = report.createSection("antecedentesPediatricos", "antecedentesPediatricosTable", 1, 1, 10);
		    report.font.setFontAttributes(0x000000, 11, false, false, false);
  			cabeceraAntecedentesAgregada = true;
  		}
    	
    	
	    //****************************************************
	    report.createSectionTable("antecedentesPediatricos", "camposObservacionesTable", numRegistros, 6);
  		report.font.setFontAttributes(0x000000, 10, true, false, false);
  		section.setTableBorder("camposObservacionesTable", 0xFFFFFF, 0.0f);
	    section.setTableCellBorderWidth("camposObservacionesTable", 0.5f);
	    section.setTableCellPadding("camposObservacionesTable", 1);
	    section.setTableSpaceBetweenCells("camposObservacionesTable", 0.5f);
	    section.setTableCellsDefaultColors("camposObservacionesTable", 0xFFFFFF, 0xFFFFFF);
  		//report.addSectionTitle("antecedentesPediatricos", "revisionSistemasTable", "Revisión por Sistemas");
	    report.font.setFontAttributes(0x000000, 10, false, false, false);
	    section.setTableCellsColSpan(3);
	    
	    int contador = 0;
	    sectionData = new String[numRegistros*2];
	    for(int i=0;i<numRegistros;i++)
	    {
	    	sectionData[contador] = camposObservaciones.get("nombre_"+i).toString();
	    	contador++;
	    	String temp = camposObservaciones.get("info_"+i).toString();
	    	switch(Integer.parseInt(camposObservaciones.get("tipo_"+i).toString()))
	    	{
	    		case ConstantesBD.tipoParametrizablesTexto:
	    		case ConstantesBD.tipoParametrizablesTextArea:
	    		break;
	    		case ConstantesBD.tipoParametrizablesArchivo:
	    			temp = "";
	    		break;
	    		case ConstantesBD.tipoParametrizablesSelect:
	    			temp = UtilidadTexto.getBoolean(temp)?"Sí":"No";
	    		break;
	    	}
	    	sectionData[contador] = temp;
	    	contador++;
	    }
	    
	    report.addSectionData("antecedentesPediatricos", "camposObservacionesTable", sectionData);
	    section.setTableCellsColSpan(1);
	    report.font.setFontAttributes(0x000000, 11, false, false, false);
	    section.setTableBorderColor("camposObservacionesTable", 0xFFFFFF);
    	
	    section.insertTableIntoCell("antecedentesPediatricosTable", "camposObservacionesTable", globalRow, 0);
	    globalRow++;
	    //****************************************************
    }
  	
  	
  	if(printEpicrisis == false){
  		//Seccion: Medico Responsable --------------------------------------------------------------------------------------------------------
  		String nombreUsuario = (String)datos.get("nombreUsuario");
  		String numRegistroMedico = (String)datos.get("numRegistroMedico");
  		InfoDatosInt[] idi = (InfoDatosInt[])datos.get("idi");
  		hayDatos = (nombreUsuario != null && nombreUsuario.length() > 0);
    
  		if (hayDatos) {
  			int index = 0;
  			sectionData = new String[2];

      	if (!cabeceraAntecedentesAgregada) {
    			//report.document.addParagraph("Antecedentes Pediatricos", report.font, 30);
    			section = report.createSection("antecedentesPediatricos", "antecedentesPediatricosTable", 1, 1, 10);
  		    report.font.setFontAttributes(0x000000, 11, false, false, false);
    			cabeceraAntecedentesAgregada = true;
    		}
  			
  			textData = nombreUsuario;
  			//Numero Registro Medico
  			if (numRegistroMedico != null && numRegistroMedico.length() > 0) {
  				textData += " ";
  				textData += numRegistroMedico;
  			}
  			sectionData[0] = textData;
        
  			//Especialidades 
  			if (idi != null && idi.length > 0) {
        		sectionData[1] = "";
        		while (index < idi.length) {
        			sectionData[1]  +=  (sectionData[1].equals("")? (idi[index].getNombre()):(" ," +idi[index].getNombre()));
        			index++;
        		}
  			}
  			else{
  				sectionData[1] = null;
  			}
        
  			report.createSectionTable("antecedentesPediatricos", "medResponsableTable", 1, 6);
  			report.font.setFontAttributes(0x000000, 11, true, false, false);
  			section.setTableBorder("medResponsableTable", 0xFFFFFF, 0.0f);
  			section.setTableCellBorderWidth("medResponsableTable", 0.5f);
  			section.setTableCellPadding("medResponsableTable", 1);
  			section.setTableSpaceBetweenCells("medResponsableTable", 0.5f);
  			section.setTableCellsDefaultColors("medResponsableTable", 0xFFFFFF, 0xFFFFFF);
  			report.addSectionTitle("antecedentesPediatricos", "medResponsableTable", "Profesional Responsable");
  			report.font.setFontAttributes(0x000000, 11, false, false, false);
  			section.setTableCellsColSpan(6);
  			report.addSectionData("antecedentesPediatricos", "medResponsableTable", sectionData);
  			section.setTableCellsColSpan(1);
  			section.setTableBorderColor("medResponsableTable", 0xFFFFFF);
  			section.insertTableIntoCell("antecedentesPediatricosTable", "medResponsableTable", globalRow, 0);
  			
  			globalRow++;
	    
  			/*report.createSection("medResponsable", "medResponsableTable", (size+1), 1, 7);
  			 report.font.setFontAttributes(0x000000, 11, true, false, false);
  			 report.addSectionTitle("medResponsable", "medResponsableTable", "Médico Responsable");
  			 report.font.setFontAttributes(0x000000, 11, false, false, false);
  			 report.addSectionData("medResponsable", "medResponsableTable", sectionData);
  			 report.addSectionToDocument("medResponsable");*/
  		}
  	}
    
    report.addSectionToDocument("antecedentesPediatricos");		
	}
}
