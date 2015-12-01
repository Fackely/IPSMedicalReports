/*
 * Ago 28, 2008
 */
package com.princetonsa.pdf;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.CsvFile;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;
import util.pdf.iTextBaseTable;

import com.princetonsa.actionform.manejoPaciente.EstadisticasIngresosForm;
import com.princetonsa.actionform.manejoPaciente.ReporteMortalidadForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.EstadisticasIngresos;
import com.princetonsa.mundo.manejoPaciente.ReporteMortalidad;

/**
 * 
 * Clase usada para generar el reporte de atencion por empresa y convenio
 * @author Cesar Giovanny Arias Galeano.
 *
 */
public class AtencionXempresaYConvenioPdf 
{
	/**
	 * Objeto para manejar los logs de esta clase
	 */
    private static Logger logger=Logger.getLogger(AtencionXempresaYConvenioPdf.class);
    
    /**
     * Método para realizar la impresión del reporte de atencion por empresa y convenio
     * @param filename
     * @param forma
     * @param usuario
     */
    public static void pdfAtencionXEmpresaYConvenio(
    		Connection con,String pathArchivo,String nombreArchivo, EstadisticasIngresosForm forma,UsuarioBasico usuario, 
    		HttpServletRequest request)
    {
    	logger.info("===> ");
    	logger.info("===> Entré a pdfAtencionXEmpresaYConvenio");
    	logger.info("===> nombre archivo = "+nombreArchivo);
    	String filename = pathArchivo + System.getProperty("file.separator") + nombreArchivo;
    	
    	InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
    	
        //**************ENCABEZADO (Se muestran los parámetros de busqueda)*********************************************************************
    	logger.info("===> Hago el encabezado");
	    //******* Crear cadena con los parametros de busqueda
    	logger.info("===> Centro de atención");
    	// Centro de Atención
    	String filtroo="[Centro de Atención: "+Utilidades.obtenerNombreCentroAtencion(con, Utilidades.convertirAEntero(forma.getFiltros("centroAtencion").toString()))+"]  ";
    	logger.info("===> Vía ingreso");
    	// Via de ingreso
    	filtroo+="[Vía de Ingreso: "+Utilidades.obtenerNombreViaIngreso(con, Integer.parseInt(forma.getFiltros("viaIngreso").toString()))+"]  ";
    	
    	logger.info("===> Tipo Paciente");
    	// Tipo de Paciente
		if (forma.getFiltros().containsKey("tipoPaciente") && !forma.getFiltros("tipoPaciente").equals(""))
			filtroo+="[Tipo de Paciente: "+Utilidades.obtenerNombreTipoPaciente(con, forma.getFiltros("tipoPaciente").toString())+"]  ";
		
		logger.info("===> Convenio");
		// Convenio
		if (forma.getFiltros().containsKey("convenio") && !forma.getFiltros("convenio").equals("")){
			int convenio;
			filtroo+="[Convenio(s): ";
			for (int i=0; i<forma.getFiltros("convenio").toString().split(", ").length; i++){
				convenio = Integer.parseInt(forma.getFiltros("convenio").toString().split(", ")[i]);
				if (convenio != ConstantesBD.codigoNuncaValido)
					filtroo +=  Utilidades.obtenerNombreConvenioOriginal(con, convenio)+", ";
			}
			filtroo += "] ";
		}
		
		logger.info("===> Empresa");
		// Empresa
		if (forma.getFiltros().containsKey("empresa") && !forma.getFiltros("empresa").equals("")){
			int empresa;
			filtroo+="[Empresa(s): ";
			for (int i=0; i<forma.getFiltros("empresa").toString().split(", ").length; i++){
				empresa = Integer.parseInt(forma.getFiltros("empresa").toString().split(", ")[i]);
				if (empresa != ConstantesBD.codigoNuncaValido)
					filtroo +=  Utilidades.obtenerNombreEmpresa(con, empresa+"")+", ";
			}
			filtroo += "] ";
		}
		
		logger.info("===> Impresión del encabezado");
		//*************IMPRESION DEL ENCABEZADO************************************************
    	PdfReports report = new PdfReports("false",true);
    	
        String tituloDespacho="ATENCIÓN POR EMPRESA Y CONVENIO" ;
        
		report.setReportBaseHeaderIngresos(institucionBasica, tituloDespacho, filtroo);
		
		logger.info("===> Abrir reporte");
		logger.info("===> filename = "+filename);
        //Se abre el reporte
        report.openReport(filename);
        logger.info("===> Mandamos la font");
        report.font.useFont(iTextBaseFont.FONT_TIMES, true, true, true);
        logger.info("===> Ahora la iTextBaseTable");
        iTextBaseTable section;
	    
	    //*****************************************************************************************************************
	    
	   
	    
	    //***********SECCION DETALLE*****************************************************************************************
	    //Variables claves para armar las dimensiones de la tabla del reporte
	    int numMeses = 0;
	    int numFilasTotales = 0;
	    int numColumnasTotales = 0;
	    int numConvenios = 0;
	    int numEmpresas = 0;
	    double promedioAtencion = 0;
	    double promedioAtencionConvenio = 0;
	    double promedioAtencionMes = 0;
	    int pacientes = 0;
	    int pacientesConvenio = 0;
	    int pacientesMes = 0;
	    int egresosMes=0;
	    int egresosRango=0;
	    int pacientesTotal=0;
	    double promedioAtencionTotal=0;
	    int egresosTotal=0;
	    int egresos=0;
	    ArrayList<HashMap<String,Object>> meses = new ArrayList<HashMap<String,Object>>();
	    ArrayList<HashMap<String,Object>> convenios = new ArrayList<HashMap<String,Object>>();
	    
	    
	    
	    //Arreglos que contienen los datos
	    EstadisticasIngresos mundo = new EstadisticasIngresos();
	    mundo.setFiltrosMap(forma.getFiltros());
	    HashMap<String, Object> datos = new HashMap<String, Object>();
	    datos = mundo.consultarAtencionXEmpresaYConvenio(con, mundo);
	    
	    
	    
	    //* Se cuenta el numero de meses entre la fecha inicial y fecha final--------------------------------------------------------------------
	    String fechaInicial=forma.getFiltros("fechaInicial").toString();
	    while(UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fechaInicial, forma.getFiltros("fechaFinal")+""))
	    {
	    	String[] vectorFecha = UtilidadFecha.conversionFormatoFechaABD(fechaInicial).split("-");
	    	
	    	fechaInicial = UtilidadFecha.incrementarMesesAFecha(fechaInicial, 1, false);
	    	
	    	//Se agrega el elemento del mes al arreglo de meses
	    	HashMap<String, Object> elemento = new HashMap<String, Object>();
	    	elemento.put("nombreMes", UtilidadFecha.obtenerNombreMes(Integer.parseInt(vectorFecha[1]))+" "+vectorFecha[0]);
	    	elemento.put("codigoMes",vectorFecha[0]+"-"+vectorFecha[1]);
	    	egresos = EstadisticasIngresos.consultarEgresosMes(con, vectorFecha[0]+"-"+vectorFecha[1], forma.getFiltros());
	    	elemento.put("egresos", egresos);
	    	egresosTotal+=egresos;
	    	
//*******************************************************elemento.put("totalEgresos",mundo.obtenerTotalEgresosXMes(con, mundo, vectorFecha[0]+"-"+vectorFecha[1]));
	    	meses.add( elemento);
	    	numMeses ++;
	    }
	    
	    convenios=obtenerConvenios(datos);
	   
	    // Se calcula el número de filas de la tabla -----------------------------------------------------------------------------------------
	    // #Convenios + #Empresas + 4
	    numEmpresas = calcularNumEmpresas(datos);
	    logger.info("numEmpresas: "+numEmpresas);
	    numConvenios = convenios.size();
	    logger.info("numConvenios: "+numConvenios);
	    numFilasTotales = numConvenios + numEmpresas + 4;
	    logger.info("numFilasTotales: "+numFilasTotales);
	    numColumnasTotales=(numMeses*2)+3;
	     
		    //*****************SE GENERA LA TABLA DEL REPORTE*************************************************************************************
		    
		    /**numero de columnas: (numMeses*2)+2 . Debido a que cada mes tiene una columna de tasa y al final hay 2 columnas de total ,tasa global y rango de tiempos **/
		    /**numero de filas: numRangosTiempo + numFilasTotales **/
			section = report.createSection("reporte", "reporteTable", numFilasTotales, numColumnasTotales, 0.5f);
	  		section.setTableBorder("reporteTable", 0x000000, 0.1f);
		    section.setTableCellBorderWidth("reporteTable", 0.1f);
		    section.setTableCellPadding("reporteTable", 1);
		    section.setTableSpaceBetweenCells("reporteTable", 0.1f);
		    section.setTableCellsDefaultColors("reporteTable", 0xFFFFFF, 0x000000);
		    section.setTableCellsBackgroundColor(0xE5E5E5);
		  
		    //**********SE DIBUJAN LOS TÍTULOS DE LA PRIMERA COLUMNA******************************************************************
		    report.font.setFontAttributes(0x000000, 8, true, false, false);
		    section.setTableCellsColSpan(1); 
		    section.addTableTextCellAligned("reporteTable", "", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.setTableCellsColSpan(2); 
		    for(HashMap<String, Object> elemento:meses)
		    {
		    	section.addTableTextCellAligned("reporteTable", elemento.get("nombreMes").toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    }
		    section.addTableTextCellAligned("reporteTable", "TOTAL", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    //**********************************************************************************************
	    	
	    	//**********SE DIBUJAN LOS TÍTULOS DE LA SEGUNDA COLUMNA******************************************************************
		    report.font.setFontAttributes(0x000000, 8, true, false, false);
		    section.setTableCellsColSpan(1); 
		    section.addTableTextCellAligned("reporteTable", "", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    for(HashMap<String, Object> elemento:meses)
		    {
		    	section.addTableTextCellAligned("reporteTable", "Pacientes", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    	section.addTableTextCellAligned("reporteTable", "Promedio Atención", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    }
		    section.addTableTextCellAligned("reporteTable", "Pacientes", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    	section.addTableTextCellAligned("reporteTable", "Promedio Atención", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    //**********************************************************************************************
	    	
	    	int codEmpresa = ConstantesBD.codigoNuncaValido;
	    	    	
	    	
	    	//Iteración de los convenios
	    	for(HashMap<String, Object> elementoConvenio:convenios)
	    	{
	    		logger.info("codigo convenio: "+elementoConvenio.get("codconvenio"));
	    		
	    		if(codEmpresa!=Utilidades.convertirAEntero(elementoConvenio.get("codempresa").toString()))
	    		{
    				section.setTableCellsColSpan(numColumnasTotales);
    				section.setTableCellsBackgroundColor(0xE5E5E5);
    				section.addTableTextCellAligned("reporteTable", elementoConvenio.get("empresa")+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
    				codEmpresa=Utilidades.convertirAEntero(elementoConvenio.get("codempresa").toString());
    				section.setTableCellsBackgroundColor(0xFFFFFF);
	    		}
	    		
				section.setTableCellsColSpan(1);
				section.addTableTextCellAligned("reporteTable", elementoConvenio.get("convenio")+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				
				//Iteración de los meses
	    		for(HashMap<String, Object> elementoMes:meses)
	    		{
	    			pacientes = calcularNumeroPacientes(datos, Integer.parseInt(elementoConvenio.get("codconvenio").toString()), elementoMes.get("codigoMes").toString());
	    			pacientesConvenio = pacientesConvenio+pacientes;
	    		 	
	    			promedioAtencion = (pacientes/Utilidades.convertirADouble(elementoMes.get("egresos").toString()))*100;
	    		
	    			
	    			section.addTableTextCellAligned("reporteTable", pacientes+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	    			section.addTableTextCellAligned("reporteTable", UtilidadTexto.formatearValores(promedioAtencion+"", 2, true, true), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);		
	    		}
	    		
	    		//Se dibujan los totales por rango
	    		//TOTAL
	    		section.addTableTextCellAligned("reporteTable", pacientesConvenio+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	    		
	    		//TASA GLOBAL
	    		promedioAtencionTotal = (pacientesConvenio/Utilidades.convertirADouble(egresosTotal+""))*100;
				section.addTableTextCellAligned("reporteTable", UtilidadTexto.formatearValores(promedioAtencionTotal+"", 2, true, true), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
				
				
				pacientesConvenio=0;
				promedioAtencionConvenio=0;
	    	}
	    	
	    	section.setTableCellsBackgroundColor(0xE5E5E5);
	    	
	    	//Fila Total General
	    	section.setTableCellsColSpan(1); 
		    section.addTableTextCellAligned("reporteTable", "Total General", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    for(HashMap<String, Object> elemento:meses)
		    {
		    	pacientesMes = calcularNumeroPacientesMes(datos, elemento.get("codigoMes").toString());
		    	section.addTableTextCellAligned("reporteTable", pacientesMes+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
		    	promedioAtencionMes = (pacientesMes/Utilidades.convertirADouble(elemento.get("egresos").toString()))*100;
		    	section.addTableTextCellAligned("reporteTable", UtilidadTexto.formatearValores(promedioAtencionMes+"", 2, true, true), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
		    	pacientesTotal = pacientesTotal + pacientesMes;
		    	promedioAtencionTotal = promedioAtencionTotal +promedioAtencionMes;
		    }
		    section.addTableTextCellAligned("reporteTable", pacientesTotal+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("reporteTable", promedioAtencionTotal+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
		    
	    	//Fila Total Egresos
	    	section.setTableCellsColSpan(1); 
		    section.addTableTextCellAligned("reporteTable", "Total Egresos", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.setTableCellsColSpan(2);
		    for(HashMap<String, Object> elemento:meses)
		    {
		    	section.addTableTextCellAligned("reporteTable", elemento.get("egresos").toString(), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
		    }
		    section.addTableTextCellAligned("reporteTable", UtilidadTexto.formatearValores(egresosTotal+"", 2, true, true), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
		    
		    
		    //se añade la sección al documento
		    report.addSectionToDocument("reporte");
		    
		    
		    
		    //********************************************************************************************************************
		    report.setUsuario(usuario.getLoginUsuario());
		    report.closeReport();
	    
    }

	

	private static int calcularNumeroPacientes(HashMap datos, int convenio, String anioMes) {
		int numPacientes=0;
		for(int i=0; i<Integer.parseInt(datos.get("numRegistros").toString()); i++){
			if (Integer.parseInt(datos.get("codconvenio_"+i).toString())==convenio && datos.get("aniomes_"+i).toString().equals(anioMes)){
				numPacientes++;
			}
		}
		return numPacientes;
	}
	
	private static int calcularNumeroPacientesMes(HashMap datos, String anioMes) {
		int numPacientes=0;
		for(int i=0; i<Integer.parseInt(datos.get("numRegistros").toString()); i++){
			if (datos.get("aniomes_"+i).toString().equals(anioMes)){
				numPacientes++;
			}
		}
		return numPacientes;
	}
	
	private static int calcularNumEmpresas(HashMap<String, Object> datos) {
		int numEmpresas = 0;
		int codEmpresa = ConstantesBD.codigoNuncaValido;
		for (int i=0; i<Utilidades.convertirAEntero(datos.get("numRegistros").toString()); i++){
			if(codEmpresa!=Utilidades.convertirAEntero(datos.get("codempresa_"+i).toString())){
				numEmpresas++;
			}	
			codEmpresa = Utilidades.convertirAEntero(datos.get("codempresa_"+i).toString());
		}
		return numEmpresas;
	}

	private static ArrayList obtenerConvenios(HashMap<String, Object> datos) {
		//for(int i=0;i<Integer.parseInt(datos.get("numRegistros")+"");i++)
			//logger.info("datos["+i+"]: "+datos.get("codconvenio_"+i));
		ArrayList<HashMap<String,Object>> convenios = new ArrayList<HashMap<String,Object>>();
		
		int convenio = ConstantesBD.codigoNuncaValido;
		for (int i=0; i<Utilidades.convertirAEntero(datos.get("numRegistros").toString()); i++){
			if(convenio!=Utilidades.convertirAEntero(datos.get("codconvenio_"+i).toString()))
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("convenio", datos.get("convenio_"+i));
				elemento.put("codconvenio", datos.get("codconvenio_"+i));
				elemento.put("codempresa", datos.get("codempresa_"+i));
				elemento.put("empresa", datos.get("empresa_"+i));
				convenios.add(elemento);
				//logger.info("convenio a añadir: "+datos.get("codconvenio_"+i));
				convenio = Utilidades.convertirAEntero(datos.get("codconvenio_"+i).toString());
			}	
			
		}
		return convenios;
	}
}
