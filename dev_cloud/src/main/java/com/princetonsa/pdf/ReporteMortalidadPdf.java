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

import com.princetonsa.actionform.manejoPaciente.ReporteMortalidadForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.ReporteMortalidad;

/**
 * 
 * Clase usada para generar reportes de mortalidad
 * @author Sebastián Gómez R.
 *
 */
public class ReporteMortalidadPdf 
{
	 /**
	 * Objeto para manejar los logs de esta clase
	*/
    private static Logger logger=Logger.getLogger(ReporteMortalidadPdf.class);
    
    /**
     * Método para realizar la impresión del reporte de tasa de mortalidad global
     * @param filename
     * @param forma
     * @param usuario
     */
    public static void pdfTasaMortalidadGlobal(Connection con,String pathArchivo,String nombreArchivo, ReporteMortalidadForm forma,UsuarioBasico usuario, HttpServletRequest request)
    {
    	String filename = pathArchivo + System.getProperty("file.separator") + nombreArchivo;
    	
    	
    	InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
    	
    	//*************IMPRESION DEL ENCABEZADO************************************************
    	PdfReports report = new PdfReports("false",true); 
        
        String filePath=ValoresPorDefecto.getFilePath();
        String tituloDespacho="TASA MORTALIDAD GLOBAL" ;
        
        filePath=filePath.substring(0, filePath.lastIndexOf("/", (filePath.length()-2)));
		report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), tituloDespacho);
		
        //Se abre el reporte
        report.openReport(filename);
        report.font.useFont(iTextBaseFont.FONT_TIMES, true, true, true);
        iTextBaseTable section;
        
        //**************SECCION ENCABEZADO (Se muestran los parámetros de busqueda)*********************************************************************
        section = report.createSection("encabezado", "encabezadoTable", 1, 1, 10);
  		section.setTableBorder("encabezadoTable", 0x000000, 0.0f);
	    section.setTableCellBorderWidth("encabezadoTable", 0.0f);
	    section.setTableCellPadding("encabezadoTable", 1);
	    section.setTableSpaceBetweenCells("encabezadoTable", 0.1f);
	    section.setTableCellsDefaultColors("encabezadoTable", 0xFFFFFF, 0xFFFFFF);
	    
	    //Se limpian las descripciones de los campos
	    forma.setNombreCentroAtencion("");
	    forma.setNombreSexo("");
	    forma.setNombreViaIngreso("");
	    
	    section.setTableCellsColSpan(1);
	    report.font.setFontAttributes(0x000000, 8, true, false, false);
	    String encabezado = "" +
	    	"[Centro de Atención: "+forma.getNombreCentroAtencion()+"] " +
	    	"[Fecha inicial - final: "+forma.getFechaInicial()+"] " +
	    	"[Vía de ingreso: "+forma.getNombreViaIngreso()+"] " +
	    	"[Edad: "+forma.getEdad()+"] " +
	    	"[Dx. Muerte: "+(!UtilidadTexto.isEmpty(forma.getDiagnosticosMuerte("valor")+"")?forma.getDiagnosticosMuerte("valor").toString().split(ConstantesBD.separadorSplit)[0]:"")+"] " +
	    	"[Dx. Egreso: "+forma.getDiagnosticoEgreso().getAcronimo()+"] " +
	    	"[Centro Costo: "+forma.getNombreCentroCosto()+"]";
	    section.addTableTextCellAligned("encabezadoTable", encabezado, report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    //se añade la sección al documento
	    report.addSectionToDocument("encabezado");
	    //*****************************************************************************************************************
	    
	   
	    
	    //***********SECCION DETALLE*****************************************************************************************
	    //Variables claves para armar las dimensiones de la tabla del reporte
	    int numRangosTiempo = 0, numMeses = 0, ordenRango = ConstantesBD.codigoNuncaValido;
	    int numFilasTotales = 0;
	    ArrayList<HashMap<String,Object>> meses = new ArrayList<HashMap<String,Object>>();
	    ArrayList<HashMap<String,Object>> rangos = new ArrayList<HashMap<String,Object>>();
	    //Arreglos que contienen los datos
	    HashMap<String, Object> datosHospitalizados = new HashMap<String, Object>();
	    HashMap<String, Object> datosCxAmbulatoria = new HashMap<String, Object>();
	    HashMap<String, Object> datosUrgencias = new HashMap<String, Object>();
	    
	    

	    //* Si la vía de ingreso fue hospitalizacion o ambos se consulta los datos de hospitalizados-----------------------------------------
	    if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
	    {
	    	datosHospitalizados = ReporteMortalidad.obtenerDatosTasaMortalidadGlobalHospitalizados(con, forma.getCodigoCentroAtencion(), forma.getFechaInicial(), forma.getFechaFinal(), forma.getCodigoSexo(), Utilidades.convertirAEntero(forma.getEdad()), forma.getDiagnosticosMuerte(), forma.getCentrosCosto(), forma.getDiagnosticoEgreso(), usuario.getCodigoInstitucionInt());
	    	datosCxAmbulatoria = ReporteMortalidad.obtenerDatosTasaMortalidadGlobalSinRangos(con, forma.getCodigoCentroAtencion(), forma.getFechaInicial(), forma.getFechaFinal(), forma.getCodigoSexo(), Utilidades.convertirAEntero(forma.getEdad()), forma.getDiagnosticosMuerte(), forma.getCentrosCosto(), forma.getDiagnosticoEgreso(), ConstantesBD.codigoViaIngresoHospitalizacion, ConstantesBD.tipoPacienteCirugiaAmbulatoria);
	    }
	    //Si la vía de ingreso fue urgencias o ambas se consulta los datos de urgencias
	    if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
	    	datosUrgencias = ReporteMortalidad.obtenerDatosTasaMortalidadGlobalSinRangos(con, forma.getCodigoCentroAtencion(), forma.getFechaInicial(), forma.getFechaFinal(), forma.getCodigoSexo(), Utilidades.convertirAEntero(forma.getEdad()), forma.getDiagnosticosMuerte(), forma.getCentrosCosto(), forma.getDiagnosticoEgreso(), ConstantesBD.codigoViaIngresoUrgencias, "");
	    
	    
	    
	    //* Se cuenta el numero de meses entre la fecha inicial y fecha final--------------------------------------------------------------------
	    String fechaInicial = forma.getFechaInicial();
	    while(UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fechaInicial, forma.getFechaFinal()))
	    {
	    	String[] vectorFecha = UtilidadFecha.conversionFormatoFechaABD(fechaInicial).split("-");
	    	
	    	fechaInicial = UtilidadFecha.incrementarMesesAFecha(fechaInicial, 1, false);
	    	
	    	//Se agrega el elemento del mes al arreglo de meses
	    	HashMap<String, Object> elemento = new HashMap<String, Object>();
	    	elemento.put("nombreMes", UtilidadFecha.obtenerNombreMes(Integer.parseInt(vectorFecha[1]))+" "+vectorFecha[0]);
	    	elemento.put("codigoMes",vectorFecha[0]+"-"+vectorFecha[1]);
	    	elemento.put("totalEgresosHospitalizacion",ReporteMortalidad.obtenerTotalEgresos(con, forma.getCodigoCentroAtencion(), forma.getFechaInicial(), forma.getFechaFinal(), forma.getCodigoSexo(), Utilidades.convertirAEntero(forma.getEdad()), forma.getDiagnosticosMuerte(), forma.getCentrosCosto(), forma.getDiagnosticoEgreso(), ConstantesBD.codigoViaIngresoHospitalizacion,vectorFecha[0]+"-"+vectorFecha[1]));
	    	elemento.put("totalEgresosHospitalizados",calcularTotalEgresosHospitalizados(datosHospitalizados,vectorFecha[0]+"-"+vectorFecha[1]));
	    	elemento.put("totalEgresosCxAmbulatoria",calcularTotalSinRangos(datosCxAmbulatoria,vectorFecha[0]+"-"+vectorFecha[1],"totalEgresos"));
	    	elemento.put("totalMortalidadCxAmbulatoria",calcularTotalSinRangos(datosCxAmbulatoria,vectorFecha[0]+"-"+vectorFecha[1],"totalMortalidad"));
	    	elemento.put("totalEgresosUrgencias",calcularTotalSinRangos(datosUrgencias,vectorFecha[0]+"-"+vectorFecha[1],"totalEgresos"));
	    	elemento.put("totalMortalidadUrgencias",calcularTotalSinRangos(datosUrgencias,vectorFecha[0]+"-"+vectorFecha[1],"totalMortalidad"));
	    	
	    	meses.add( elemento);
	    	numMeses ++;
	    }
	    
	    /*logger.info("MAPA DE LOS DATOS HOSPITALIZADOS "+datosHospitalizados);
    	Utilidades.imprimirMapa(datosHospitalizados);
    	logger.info("FIN MAPA DE LOS DATOS HOPSITALIZADOS");*/
	    //* Se calcula el número de rangos de tiempo -----------------------------------------------------------------------------------------
	    for(int i=0;i<Utilidades.convertirAEntero(datosHospitalizados.get("numRegistros")+"");i++)
	    {
	    	if(ordenRango!=Integer.parseInt(datosHospitalizados.get("orden_"+i).toString()))
	    	{
	    		numRangosTiempo ++;
	    		ordenRango = Integer.parseInt(datosHospitalizados.get("orden_"+i).toString());
	    		//Se agrega el elemento del rango al arreglo de rangos
	    		HashMap<String, Object> elemento = new HashMap<String, Object>();
	    		elemento.put("orden",datosHospitalizados.get("orden_"+i));
	    		elemento.put("nombreEtiqueta",datosHospitalizados.get("nombreEtiqueta_"+i));
	    		rangos.add(elemento);
	    	}
	    }
	    
	    //* Se verifica cuantas filas totales tendrá la tabla -----------------------------------------------------------------
	    switch(forma.getCodigoViaIngreso())
	    {
	    	//Quiere decir ambos
	    	case ConstantesBD.codigoNuncaValido:
	    		numFilasTotales = 9;
	    	break;
	    	case ConstantesBD.codigoViaIngresoUrgencias:
	    		numFilasTotales = 3;
	    	break;
	    	case ConstantesBD.codigoViaIngresoHospitalizacion:
	    		numFilasTotales = 7;
	    	break;
	    	
	    }
	    
	   
		    
		    
	    //*****************SE GENERA LA TABLA DEL REPORTE*************************************************************************************
	    
	    /**numero de columnas: (numMeses*2)+2 . Debido a que cada mes tiene una columna de tasa y al final hay 3 columnas de total ,tasa global y rango de tiempos **/
	    /**numero de filas: numRangosTiempo + numFilasTotales **/
		section = report.createSection("reporte", "reporteTable", numRangosTiempo + numFilasTotales, (numMeses*2)+3, 0.5f);
  		section.setTableBorder("reporteTable", 0x000000, 0.1f);
	    section.setTableCellBorderWidth("reporteTable", 0.1f);
	    section.setTableCellPadding("reporteTable", 1);
	    section.setTableSpaceBetweenCells("reporteTable", 0.1f);
	    section.setTableCellsDefaultColors("reporteTable", 0xE5E5E5, 0x000000);
	    
	    //**********SE DIBUJAN LOS TÍTULOS DE LAS COLUMNAS******************************************************************
	    report.font.setFontAttributes(0x000000, 8, true, false, false);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("reporteTable", numRangosTiempo>0?"Rango de Tiempos":"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    for(HashMap<String, Object> elemento:meses)
	    {
	    	section.addTableTextCellAligned("reporteTable", elemento.get("nombreMes").toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    	section.addTableTextCellAligned("reporteTable", "Tasa", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    	
	    }
	    section.addTableTextCellAligned("reporteTable", "TOTAL", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    section.addTableTextCellAligned("reporteTable", "TASA GLOBAL", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
    	report.font.setFontAttributes(0x000000, 8, false, false, false);
    	section.setTableCellsDefaultColors("reporteTable", 0xFFFFFF, 0x000000);
	    //**********************************************************************************************
	    
	    //*******SE DIBUJAN LOS CALCULOS DE LOS RANGOS DE TIEMPO***************************************************************
    	ordenRango = ConstantesBD.codigoNuncaValido;
    	int totalMuertesRangoMes = 0;
    	double totalTasaRangoMes = 0;
    	
    	int totalMuertesRango = 0;
    	double totalTasaRango = 0;
    	
    	    	
    	//Iteración de los rangos
    	for(HashMap<String, Object> elementoRango:rangos)
    	{
    		totalMuertesRango = 0;
    		totalTasaRango = 0;
    		
    		
    		section.addTableTextCellAligned("reporteTable", elementoRango.get("nombreEtiqueta").toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
    		
    		//Iteración de los meses
    		for(HashMap<String, Object> elementoMes:meses)
    		{
    			//Si no existe esta llave retorna 0 para comenzar
    			int totalMortalidadHospitalizados = Utilidades.convertirAEntero(elementoMes.get("totalMortalidadHospitalizados")+"",true);
    			int totalEgresosHospitalizacion = Utilidades.convertirAEntero(elementoMes.get("totalEgresosHospitalizacion")+"",true);
    			logger.info("\n\n\n");
    			logger.info("totalMortalidadHospitalizados: "+totalMortalidadHospitalizados);
    			logger.info("totalEgresosHospitalizacion: "+totalEgresosHospitalizacion);
    			totalMuertesRangoMes = calcularTotalMuertesRangoMes(datosHospitalizados,elementoRango.get("orden").toString(),elementoMes.get("codigoMes").toString());
    			logger.info("totalMuertesRangoMes: "+totalMuertesRangoMes);
    			if(totalEgresosHospitalizacion>0)
    			{
    				totalTasaRangoMes = (((double)totalMuertesRangoMes/(double)totalEgresosHospitalizacion))*100;
    				logger.info("paso por aqui A");
    			}
    			else
    			{
    				totalTasaRangoMes = 0;
    				logger.info("paso por aqui B");
    			}
    			logger.info("totalTasaRangoMes: "+totalTasaRangoMes);
    			section.addTableTextCellAligned("reporteTable", totalMuertesRangoMes +"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    			section.addTableTextCellAligned("reporteTable", UtilidadTexto.formatearValores(totalTasaRangoMes,"0.00"), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    			
    			//Se asigna el acumulado por mes
    			totalMortalidadHospitalizados += totalMuertesRangoMes;
    			elementoMes.put("totalMortalidadHospitalizados", totalMortalidadHospitalizados);
    			
    			//Se va acumulando lo calculado por cada rango
    			totalMuertesRango += totalMuertesRangoMes;
    			totalTasaRango += totalTasaRangoMes;
    		}
    		
    		//Se dibujan los totales por rango
    		//TOTAL
    		section.addTableTextCellAligned("reporteTable", totalMuertesRango +"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    		//TASA GLOBAL
			section.addTableTextCellAligned("reporteTable", UtilidadTexto.formatearValores(totalTasaRango/meses.size(),"0.00"), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
			
    	}
    	//**********************************************************************************************************************
    	//***************SE DIBUJAN LOS TOTALES DE LOS RANGOS DE TIEMPO***********************************************************
    	//Solo aplica para via de ingreso hospitalizacion o ambas
    	if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
    	{
    		/*** ****************************************************************************************************************************************************** ***/
    		//Etiqueta MORTALIDAD PACIENTES HOSPITALIZADOS-----------------------------------------------------------------------------------
    		report.font.setFontAttributes(0x000000, 8, true, false, false);
    		section.addTableTextCellAligned("reporteTable", "Mortalidad Ptes Hospitalizados", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
    		report.font.setFontAttributes(0x000000, 8, false, false, false);
    		
    		int totalMortalidadHospitalizados = 0;
    		double totalTasaMortalidadHospitalizados = 0;
    		
    		for(HashMap<String, Object> elementoMes:meses)
    		{
    			//Se saca el total de mortalidad de hospitalizados x cada mes
    			int totalMortalidadHospitalizadosMes = Utilidades.convertirAEntero(elementoMes.get("totalMortalidadHospitalizados")+"",true);
    			int totalEgresosHospitalizacion = Utilidades.convertirAEntero(elementoMes.get("totalEgresosHospitalizacion")+"",true);
    			
    			double totalTasaMortalidadHospitalizadosMes = 0;
    			if(totalEgresosHospitalizacion>0)
    				totalTasaMortalidadHospitalizadosMes = ((double)totalMortalidadHospitalizadosMes/(double)totalEgresosHospitalizacion)*100;
    			else
    				totalTasaMortalidadHospitalizadosMes = 0;
    			section.addTableTextCellAligned("reporteTable", totalMortalidadHospitalizadosMes +"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    			section.addTableTextCellAligned("reporteTable",UtilidadTexto.formatearValores(totalTasaMortalidadHospitalizadosMes,"0.00") +"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    			
    			totalMortalidadHospitalizados += totalMortalidadHospitalizadosMes;
    			totalTasaMortalidadHospitalizados += totalTasaMortalidadHospitalizadosMes;
    		}
    		
    		//Se dibujan los totales MORTALIDAD DE HOSPITALIZADOS
    		//TOTAL
    		section.addTableTextCellAligned("reporteTable", totalMortalidadHospitalizados +"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    		//TASA GLOBAL
			section.addTableTextCellAligned("reporteTable", UtilidadTexto.formatearValores(totalTasaMortalidadHospitalizados/meses.size(),"0.00"), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
			
			/*** ****************************************************************************************************************************************************** ***/
			//Etiqueta EGRESOS PACIENTES HOSPITALIZADOS--------------------------------------------------------------------------
			report.font.setFontAttributes(0x000000, 8, true, false, false);
    		section.addTableTextCellAligned("reporteTable", "Total Egresos Ptes Hospitalizados", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
    		report.font.setFontAttributes(0x000000, 8, false, false, false);
    		
    		int totalEgresosPtesHospitalizados = 0;
    		double totalTasaEgresosHospitalizados = 0;
    		
    		for(HashMap<String, Object> elementoMes:meses)
    		{
    			//Se saca el total de mortalidad de hospitalizados x cada mes
    			totalMortalidadHospitalizados = Utilidades.convertirAEntero(elementoMes.get("totalMortalidadHospitalizados")+"",true);
    			//Se saca el total de egresos de hospitalizados x cadad mes
    			int totalEgresosHospitalizados = Utilidades.convertirAEntero(elementoMes.get("totalEgresosHospitalizados")+"",true);
    			
    			double totalTasaEgresosHospitalizadosMes = 0;
    			if(totalEgresosHospitalizados>0)
    				totalTasaEgresosHospitalizadosMes = ((double)totalMortalidadHospitalizados/(double)totalEgresosHospitalizados)*100;
    			else
    				totalTasaEgresosHospitalizadosMes = 0;
    			section.addTableTextCellAligned("reporteTable", totalEgresosHospitalizados +"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    			section.addTableTextCellAligned("reporteTable", UtilidadTexto.formatearValores(totalTasaEgresosHospitalizadosMes,"0.00") +"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    			
    			totalEgresosPtesHospitalizados += totalEgresosHospitalizados;
    			totalTasaEgresosHospitalizados += totalTasaEgresosHospitalizadosMes;
    		}
    		
    		//Se dibujan los totales EGRESOS PACIENTES HOSPITALIZADOS
    		//TOTAL
    		section.addTableTextCellAligned("reporteTable", totalEgresosPtesHospitalizados +"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    		//TASA GLOBAL
			section.addTableTextCellAligned("reporteTable", UtilidadTexto.formatearValores(totalTasaEgresosHospitalizados/meses.size(),"0.00"), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
			
			/*** ****************************************************************************************************************************************************** ***/
    		//Etiqueta MORTALIDAD PACIENTES CIRUGIA AMBULATORIA ------------------------------------------------------------
			report.font.setFontAttributes(0x000000, 8, true, false, false);
    		section.addTableTextCellAligned("reporteTable", "Mortalidad Ptes Cx Ambulatoria", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
    		report.font.setFontAttributes(0x000000, 8, false, false, false);
    		
    		int totalMortalidadPtesCxAmbulatoria = 0;
    		double totalTasaMortalidadPtesCxAmbulatoria = 0;
    		
    		for(HashMap<String, Object> elementoMes:meses)
    		{
    			//Se saca el total de mortalidad de cx ambulatoria x cada mes
    			int totalMortalidadCxAmbulatoriaMes = Utilidades.convertirAEntero(elementoMes.get("totalMortalidadCxAmbulatoria")+"",true);
    			//Se saca el total de egresos de via ingrso hospitakizacion x cadad mes
    			int totalEgresosHospitalizacion = Utilidades.convertirAEntero(elementoMes.get("totalEgresosHospitalizacion")+"",true);
    			
    			double totalTasaMortalidadCxAmbulatoriaMes = 0;
    			if(totalEgresosHospitalizacion>0)
    				totalTasaMortalidadCxAmbulatoriaMes = ((double)totalMortalidadCxAmbulatoriaMes/(double)totalEgresosHospitalizacion)*100;
    			else
    				totalTasaMortalidadCxAmbulatoriaMes = 0;
    			section.addTableTextCellAligned("reporteTable", totalMortalidadCxAmbulatoriaMes +"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    			section.addTableTextCellAligned("reporteTable",UtilidadTexto.formatearValores(totalTasaMortalidadCxAmbulatoriaMes,"0.00") +"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    			
    			totalMortalidadPtesCxAmbulatoria += totalMortalidadCxAmbulatoriaMes;
    			totalTasaMortalidadPtesCxAmbulatoria += totalTasaMortalidadCxAmbulatoriaMes;
    		}
    		
    		//Se dibujan los totales MORTALIDAD PACIENTES CIRUGÍA AMBULATORIA
    		//TOTAL
    		section.addTableTextCellAligned("reporteTable", totalMortalidadPtesCxAmbulatoria +"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    		//TASA GLOBAL
			section.addTableTextCellAligned("reporteTable", UtilidadTexto.formatearValores(totalTasaMortalidadPtesCxAmbulatoria/meses.size(),"0.00"), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
			
			/*** ****************************************************************************************************************************************************** ***/
			//Etiqueta TOTAL EGRESOS PACIENTES CIRUGIA AMBULATORIA ------------------------------------------------------------
			report.font.setFontAttributes(0x000000, 8, true, false, false);
    		section.addTableTextCellAligned("reporteTable", "Total Egresos Ptes Cx Ambulatoria", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
    		report.font.setFontAttributes(0x000000, 8, false, false, false);
			
    		int totalEgresosPtesCxAmbulatoria = 0;
    		double totalTasaEgresosPtesCxAmbulatoria = 0;
    		
    		for(HashMap<String, Object> elementoMes:meses)
    		{
    			//Se saca el total de mortalidad de cx ambulatoria x cada mes
    			int totalMortalidadCxAmbulatoriaMes = Utilidades.convertirAEntero(elementoMes.get("totalMortalidadCxAmbulatoria")+"",true);
    			//Se saca el total de egresos de cx ambulatoria x cadad mes
    			int totalEgresosCxAmbulatoriaMes = Utilidades.convertirAEntero(elementoMes.get("totalEgresosCxAmbulatoria")+"",true);
    			
    			double totalTasaEgresosCxAmbulatoriaMes = 0;
    			if(totalEgresosCxAmbulatoriaMes>0)
    				totalTasaEgresosCxAmbulatoriaMes = ((double)totalMortalidadCxAmbulatoriaMes/(double)totalEgresosCxAmbulatoriaMes)*100;
    			else
    				totalTasaEgresosCxAmbulatoriaMes = 0;
    			section.addTableTextCellAligned("reporteTable", totalEgresosCxAmbulatoriaMes +"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    			section.addTableTextCellAligned("reporteTable", UtilidadTexto.formatearValores(totalTasaEgresosCxAmbulatoriaMes,"0.00"), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    			
    			totalEgresosPtesCxAmbulatoria += totalEgresosCxAmbulatoriaMes;
    			totalTasaEgresosPtesCxAmbulatoria += totalTasaEgresosCxAmbulatoriaMes;
    		}
    		
    		//Se dibujan los totales TOTAL EGRESOS PACIENTES CIRUGÍA AMBULATORIA
    		//TOTAL
    		section.addTableTextCellAligned("reporteTable", totalEgresosPtesCxAmbulatoria +"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    		//TASA GLOBAL
			section.addTableTextCellAligned("reporteTable", UtilidadTexto.formatearValores(totalTasaEgresosPtesCxAmbulatoria/meses.size(),"0.00"), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
			
			/*** ****************************************************************************************************************************************************** ***/
			//Etiqueta TOTAL EGRESOS HOSPITALIZACION------------------------------------------------------------------------------------
			report.font.setFontAttributes(0x000000, 8, true, false, false);
    		section.addTableTextCellAligned("reporteTable", "Total Egresos Hospitalización", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
    		report.font.setFontAttributes(0x000000, 8, false, false, false);
    		
    		int totalEgresosHospitalizacion = 0;
    		
    		for(HashMap<String, Object> elementoMes:meses)
    		{
    			//Se saca el total de egresos de hosptilizacion por mes
    			int totalEgresosHospitalizacionMes = Utilidades.convertirAEntero(elementoMes.get("totalEgresosHospitalizacion")+"",true);
    			
    			section.addTableTextCellAligned("reporteTable", totalEgresosHospitalizacionMes+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    			section.addTableTextCellAligned("reporteTable", "-", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
    			
    			totalEgresosHospitalizacion += totalEgresosHospitalizacionMes;
    		}
    		
    		//Se dibujan los totales TOTAL EGRESOS HOSPITALIZACION
    		//TOTAL
    		section.addTableTextCellAligned("reporteTable", totalEgresosHospitalizacion+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    		//TASA GLOBAL
			section.addTableTextCellAligned("reporteTable", "-", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			
			
    		/*** ****************************************************************************************************************************************************** ***/
			
			
    	}
    	//*************************************************************************************************************************
    	//***************SE DIBUJAN LOS TOTALES QUE TIENEN QUE VER CON URGENCIAS***************************************************
    	if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
    	{
    		
    		/*** ****************************************************************************************************************************************************** ***/
    		//Etiqueta MORTALIDAD PACIENTES URGENCIAS------------------------------------------------------------
			report.font.setFontAttributes(0x000000, 8, true, false, false);
    		section.addTableTextCellAligned("reporteTable", "Mortalidad Urgencias", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
    		report.font.setFontAttributes(0x000000, 8, false, false, false);
    		
    		int totalMortalidadUrgencias = 0;
    		double totalTasaMortalidadUrgencias = 0;
    		
    		for(HashMap<String, Object> elementoMes:meses)
    		{
    			//Se saca el total de mortalidad de urgencias x cada mes
    			int totalMortalidadUrgenciasMes = Utilidades.convertirAEntero(elementoMes.get("totalMortalidadUrgencias")+"",true);
    			//Se saca el total de egresos de via ingrso hospitakizacion x cadad mes
    			int totalEgresosUrgenciasMes = Utilidades.convertirAEntero(elementoMes.get("totalEgresosUrgencias")+"",true);
    			
    			double totalTasaMortalidadUrgenciasMes = 0;
    			if(totalEgresosUrgenciasMes>0)
    				totalTasaMortalidadUrgenciasMes = ((double)totalMortalidadUrgenciasMes/(double)totalEgresosUrgenciasMes)*100;
    			else
    				totalTasaMortalidadUrgenciasMes = 0;
    			section.addTableTextCellAligned("reporteTable", totalMortalidadUrgenciasMes +"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    			section.addTableTextCellAligned("reporteTable", UtilidadTexto.formatearValores(totalTasaMortalidadUrgenciasMes,"0.00"), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    			
    			totalMortalidadUrgencias += totalMortalidadUrgenciasMes;
    			totalTasaMortalidadUrgencias += totalTasaMortalidadUrgenciasMes;
    		}
    		
    		//Se dibujan los totales MORTALIDAD PACIENTES URGENCIAS
    		//TOTAL
    		section.addTableTextCellAligned("reporteTable", totalMortalidadUrgencias +"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    		//TASA GLOBAL
			section.addTableTextCellAligned("reporteTable", UtilidadTexto.formatearValores(totalTasaMortalidadUrgencias/meses.size(),"0.00"), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
			
			/*** ****************************************************************************************************************************************************** ***/
			//Etiqueta TOTAL EGRESOS URGENCIAS------------------------------------------------------------------------------------
			report.font.setFontAttributes(0x000000, 8, true, false, false);
    		section.addTableTextCellAligned("reporteTable", "Total Egresos Urgencias", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
    		report.font.setFontAttributes(0x000000, 8, false, false, false);
    		
    		int totalEgresosUrgencias = 0;
    		
    		for(HashMap<String, Object> elementoMes:meses)
    		{
    			//Se saca el total de egresos de urgencias por mes
    			int totalEgresosUrgenciasMes = Utilidades.convertirAEntero(elementoMes.get("totalEgresosUrgencias")+"",true);
    			
    			section.addTableTextCellAligned("reporteTable", totalEgresosUrgenciasMes+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    			section.addTableTextCellAligned("reporteTable", "-", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
    			
    			totalEgresosUrgencias += totalEgresosUrgenciasMes;
    		}
    		
    		//Se dibujan los totales TOTAL EGRESOS URGENCIAS
    		//TOTAL
    		section.addTableTextCellAligned("reporteTable", totalEgresosUrgencias+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    		//TASA GLOBAL
			section.addTableTextCellAligned("reporteTable", "-", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			
			
    		/*** ****************************************************************************************************************************************************** ***/
    		
    		
    		
    	}
    	//*************************************************************************************************************************
    	//***********SE DIBUJAN LOS DATOS DE LA MORTALIDAD GENERAL*******************************************************************
    	if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
    	{
    		/*** ****************************************************************************************************************************************************** ***/
    		//Etiqueta MORTALIDAD GENERAL------------------------------------------------------------
			report.font.setFontAttributes(0x000000, 8, true, false, false);
    		section.addTableTextCellAligned("reporteTable", "Mortalidad General", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
    		report.font.setFontAttributes(0x000000, 8, false, false, false);
    		
    		int totalMortalidadGeneral = 0;
    		double totalTasaMortalidadGeneral = 0;
    		
    		for(HashMap<String, Object> elementoMes:meses)
    		{
    			//Se saca el total de mortalidad de urgencias x cada mes
    			int totalMortalidadHospitalizadosMes = Utilidades.convertirAEntero(elementoMes.get("totalMortalidadHospitalizados")+"",true);
    			//Se saca el total de mortalidad de urgencias x cada mes
    			int totalMortalidadCxAmbulatoriaMes = Utilidades.convertirAEntero(elementoMes.get("totalMortalidadCxAmbulatoria")+"",true);
    			//Se saca el total de mortalidad de urgencias x cada mes
    			int totalMortalidadUrgenciasMes = Utilidades.convertirAEntero(elementoMes.get("totalMortalidadUrgencias")+"",true);
    			int totalMortalidadGeneralMes = totalMortalidadHospitalizadosMes + totalMortalidadCxAmbulatoriaMes + totalMortalidadUrgenciasMes;
    			
    			//Se saca el total de egresos de via ingrso hospitalizacion x cadad mes
    			int totalEgresosHospitalizacionMes = Utilidades.convertirAEntero(elementoMes.get("totalEgresosHospitalizacion")+"",true);
    			//Se saca el total de egresos de via ingrso urgencias x cadad mes
    			int totalEgresosUrgenciasMes = Utilidades.convertirAEntero(elementoMes.get("totalEgresosUrgencias")+"",true);
    			int totalEgresosGeneralMes = totalEgresosHospitalizacionMes + totalEgresosUrgenciasMes;
    			
    			double totalTasaMortalidadGeneralMes = 0;
    			if(totalEgresosUrgenciasMes>0||totalEgresosHospitalizacionMes>0)
    				totalTasaMortalidadGeneralMes = ((double)totalMortalidadGeneralMes/(double)totalEgresosGeneralMes)*100;
    			else
    				totalTasaMortalidadGeneralMes = 0;
    			section.addTableTextCellAligned("reporteTable", totalMortalidadGeneralMes +"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    			section.addTableTextCellAligned("reporteTable", UtilidadTexto.formatearValores(totalTasaMortalidadGeneralMes,"0.00"), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    			
    			totalMortalidadGeneral += totalMortalidadGeneralMes;
    			totalTasaMortalidadGeneral += totalTasaMortalidadGeneralMes;
    		}
    		
    		//Se dibujan los totales MORTALIDAD GENERAL
    		//TOTAL
    		section.addTableTextCellAligned("reporteTable", totalMortalidadGeneral +"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    		//TASA GLOBAL
			section.addTableTextCellAligned("reporteTable", UtilidadTexto.formatearValores(totalTasaMortalidadGeneral/meses.size(),"0.00"), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
			
			/*** ****************************************************************************************************************************************************** ***/
    	}
    	//***************************************************************************************************************************
    	
    	
    	
	    
	    
	    /**float widths[]={(float) 2.5,(float) 2.5, (float) 2.5,(float) 2.5};
	    
	    try 
        {
			section.getTableReference("detalleTable"+i).setWidths(widths);
		} 
        catch (BadElementException e) 
        {
			logger.error("Error al ajustar los anchos de la tabla de UPC: "+e);
		}**/
	    //se añade la sección al documento
	    report.addSectionToDocument("reporte");
	    
	    
	    
	    //********************************************************************************************************************
	    report.closeReport();
		    
		   
	    
	    
    }
    
    
    /**
     * Método para realizar la impresión del reporte de tasa de mortalidad global
     * @param filename
     * @param forma
     * @param usuario
     */
    public static void csvTasaMortalidadGlobal(Connection con,String pathArchivo,String nombreArchivo, ReporteMortalidadForm forma,UsuarioBasico usuario, HttpServletRequest request)
    {
    	
    	String nombreArchivoCsv = nombreArchivo;
    	
    	
	    //***********SECCION DETALLE*****************************************************************************************
	    //Variables claves para armar las dimensiones de la tabla del reporte
	    int numRangosTiempo = 0, numMeses = 0, ordenRango = ConstantesBD.codigoNuncaValido;
	    ArrayList<HashMap<String,Object>> meses = new ArrayList<HashMap<String,Object>>();
	    ArrayList<HashMap<String,Object>> rangos = new ArrayList<HashMap<String,Object>>();
	    //Arreglos que contienen los datos
	    HashMap<String, Object> datosHospitalizados = new HashMap<String, Object>();
	    HashMap<String, Object> datosCxAmbulatoria = new HashMap<String, Object>();
	    HashMap<String, Object> datosUrgencias = new HashMap<String, Object>();
	    
	    

	    //* Si la vía de ingreso fue hospitalizacion o ambos se consulta los datos de hospitalizados-----------------------------------------
	    if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
	    {
	    	datosHospitalizados = ReporteMortalidad.obtenerDatosTasaMortalidadGlobalHospitalizados(con, forma.getCodigoCentroAtencion(), forma.getFechaInicial(), forma.getFechaFinal(), forma.getCodigoSexo(), Utilidades.convertirAEntero(forma.getEdad()), forma.getDiagnosticosMuerte(), forma.getCentrosCosto(), forma.getDiagnosticoEgreso(), usuario.getCodigoInstitucionInt());
	    	datosCxAmbulatoria = ReporteMortalidad.obtenerDatosTasaMortalidadGlobalSinRangos(con, forma.getCodigoCentroAtencion(), forma.getFechaInicial(), forma.getFechaFinal(), forma.getCodigoSexo(), Utilidades.convertirAEntero(forma.getEdad()), forma.getDiagnosticosMuerte(), forma.getCentrosCosto(), forma.getDiagnosticoEgreso(), ConstantesBD.codigoViaIngresoHospitalizacion, ConstantesBD.tipoPacienteCirugiaAmbulatoria);
	    }
	    //Si la vía de ingreso fue urgencias o ambas se consulta los datos de urgencias
	    if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
	    	datosUrgencias = ReporteMortalidad.obtenerDatosTasaMortalidadGlobalSinRangos(con, forma.getCodigoCentroAtencion(), forma.getFechaInicial(), forma.getFechaFinal(), forma.getCodigoSexo(), Utilidades.convertirAEntero(forma.getEdad()), forma.getDiagnosticosMuerte(), forma.getCentrosCosto(), forma.getDiagnosticoEgreso(), ConstantesBD.codigoViaIngresoUrgencias, "");
	    
	    
	    
	    //* Se cuenta el numero de meses entre la fecha inicial y fecha final--------------------------------------------------------------------
	    String fechaInicial = forma.getFechaInicial();
	    while(UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fechaInicial, forma.getFechaFinal()))
	    {
	    	String[] vectorFecha = UtilidadFecha.conversionFormatoFechaABD(fechaInicial).split("-");
	    	
	    	fechaInicial = UtilidadFecha.incrementarMesesAFecha(fechaInicial, 1, false);
	    	
	    	//Se agrega el elemento del mes al arreglo de meses
	    	HashMap<String, Object> elemento = new HashMap<String, Object>();
	    	elemento.put("nombreMes", UtilidadFecha.obtenerNombreMes(Integer.parseInt(vectorFecha[1]))+" "+vectorFecha[0]);
	    	elemento.put("codigoMes",vectorFecha[0]+"-"+vectorFecha[1]);
	    	elemento.put("totalEgresosHospitalizacion",ReporteMortalidad.obtenerTotalEgresos(con, forma.getCodigoCentroAtencion(), forma.getFechaInicial(), forma.getFechaFinal(), forma.getCodigoSexo(), Utilidades.convertirAEntero(forma.getEdad()), forma.getDiagnosticosMuerte(), forma.getCentrosCosto(), forma.getDiagnosticoEgreso(), ConstantesBD.codigoViaIngresoHospitalizacion,vectorFecha[0]+"-"+vectorFecha[1]));
	    	elemento.put("totalEgresosHospitalizados",calcularTotalEgresosHospitalizados(datosHospitalizados,vectorFecha[0]+"-"+vectorFecha[1]));
	    	elemento.put("totalEgresosCxAmbulatoria",calcularTotalSinRangos(datosCxAmbulatoria,vectorFecha[0]+"-"+vectorFecha[1],"totalEgresos"));
	    	elemento.put("totalMortalidadCxAmbulatoria",calcularTotalSinRangos(datosCxAmbulatoria,vectorFecha[0]+"-"+vectorFecha[1],"totalMortalidad"));
	    	elemento.put("totalEgresosUrgencias",calcularTotalSinRangos(datosUrgencias,vectorFecha[0]+"-"+vectorFecha[1],"totalEgresos"));
	    	elemento.put("totalMortalidadUrgencias",calcularTotalSinRangos(datosUrgencias,vectorFecha[0]+"-"+vectorFecha[1],"totalMortalidad"));
	    	
	    	meses.add( elemento);
	    	numMeses ++;
	    }
	    
	    /*logger.info("MAPA DE LOS DATOS HOSPITALIZADOS "+datosHospitalizados);
    	Utilidades.imprimirMapa(datosHospitalizados);
    	logger.info("FIN MAPA DE LOS DATOS HOPSITALIZADOS");*/
	    //* Se calcula el número de rangos de tiempo -----------------------------------------------------------------------------------------
	    for(int i=0;i<Utilidades.convertirAEntero(datosHospitalizados.get("numRegistros")+"");i++)
	    {
	    	if(ordenRango!=Integer.parseInt(datosHospitalizados.get("orden_"+i).toString()))
	    	{
	    		numRangosTiempo ++;
	    		ordenRango = Integer.parseInt(datosHospitalizados.get("orden_"+i).toString());
	    		//Se agrega el elemento del rango al arreglo de rangos
	    		HashMap<String, Object> elemento = new HashMap<String, Object>();
	    		elemento.put("orden",datosHospitalizados.get("orden_"+i));
	    		elemento.put("nombreEtiqueta",datosHospitalizados.get("nombreEtiqueta_"+i));
	    		rangos.add(elemento);
	    	}
	    }
	    
	   
	    
	    try
	    {
		    //**************INICIALIZACIÓN DEL ARCHIVO CSV***************************************************************************************
		    File archivoCSV=new File(pathArchivo,nombreArchivoCsv);
			FileWriter streamCSV = new FileWriter(archivoCSV,false);
			BufferedWriter bufferCSV=new BufferedWriter(streamCSV);
		    //***********************************************************************************************************************************
		    
		    
		    //*****************SE GENERA LA TABLA DEL REPORTE*************************************************************************************
		    
		    
		    
		    //**********SE DIBUJAN LOS TÍTULOS DE LAS COLUMNAS******************************************************************
		    
		    //Se va escribiendo en el archivo CSV
			bufferCSV.write(numRangosTiempo>0?"Rango de Tiempos":"");
			
		    for(HashMap<String, Object> elemento:meses)
		    {
		    	
		    	bufferCSV.write(","+elemento.get("nombreMes"));
		    	bufferCSV.write(","+elemento.get("nombreMes"));
		    }
		    
		    bufferCSV.write(","+"TOTAL");
	    	bufferCSV.write(","+"TASA GLOBAL\n");
	    	
		    //**********************************************************************************************
		    
		    //*******SE DIBUJAN LOS CALCULOS DE LOS RANGOS DE TIEMPO***************************************************************
	    	ordenRango = ConstantesBD.codigoNuncaValido;
	    	int totalMuertesRangoMes = 0;
	    	double totalTasaRangoMes = 0;
	    	
	    	int totalMuertesRango = 0;
	    	double totalTasaRango = 0;
	    	
	    	    	
	    	//Iteración de los rangos
	    	for(HashMap<String, Object> elementoRango:rangos)
	    	{
	    		totalMuertesRango = 0;
	    		totalTasaRango = 0;
	    		
	    		
	    		
	    		bufferCSV.write(elementoRango.get("nombreEtiqueta").toString());
	    		
	    		//Iteración de los meses
	    		for(HashMap<String, Object> elementoMes:meses)
	    		{
	    			//Si no existe esta llave retorna 0 para comenzar
	    			int totalMortalidadHospitalizados = Utilidades.convertirAEntero(elementoMes.get("totalMortalidadHospitalizados")+"",true);
	    			int totalEgresosHospitalizacion = Utilidades.convertirAEntero(elementoMes.get("totalEgresosHospitalizacion")+"",true);
	    			logger.info("\n\n\n");
	    			logger.info("totalMortalidadHospitalizados: "+totalMortalidadHospitalizados);
	    			logger.info("totalEgresosHospitalizacion: "+totalEgresosHospitalizacion);
	    			totalMuertesRangoMes = calcularTotalMuertesRangoMes(datosHospitalizados,elementoRango.get("orden").toString(),elementoMes.get("codigoMes").toString());
	    			logger.info("totalMuertesRangoMes: "+totalMuertesRangoMes);
	    			if(totalEgresosHospitalizacion>0)
	    			{
	    				totalTasaRangoMes = (((double)totalMuertesRangoMes/(double)totalEgresosHospitalizacion))*100;
	    				logger.info("paso por aqui A");
	    			}
	    			else
	    			{
	    				totalTasaRangoMes = 0;
	    				logger.info("paso por aqui B");
	    			}
	    			logger.info("totalTasaRangoMes: "+totalTasaRangoMes);
	    			
	    			bufferCSV.write(","+totalMuertesRangoMes);
	    			
	    			bufferCSV.write(","+UtilidadTexto.formatearValores(totalTasaRangoMes,"0.00"));
	    			
	    			//Se asigna el acumulado por mes
	    			totalMortalidadHospitalizados += totalMuertesRangoMes;
	    			elementoMes.put("totalMortalidadHospitalizados", totalMortalidadHospitalizados);
	    			
	    			//Se va acumulando lo calculado por cada rango
	    			totalMuertesRango += totalMuertesRangoMes;
	    			totalTasaRango += totalTasaRangoMes;
	    		}
	    		
	    		//Se dibujan los totales por rango
	    		//TOTAL
	    		
	    		bufferCSV.write(","+totalMuertesRango);
	    		//TASA GLOBAL
				
				bufferCSV.write(","+UtilidadTexto.formatearValores(totalTasaRango/meses.size(),"0.00")+"\n");
	    	}
	    	//**********************************************************************************************************************
	    	//***************SE DIBUJAN LOS TOTALES DE LOS RANGOS DE TIEMPO***********************************************************
	    	//Solo aplica para via de ingreso hospitalizacion o ambas
	    	if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
	    	{
	    		/*** ****************************************************************************************************************************************************** ***/
	    		//Etiqueta MORTALIDAD PACIENTES HOSPITALIZADOS-----------------------------------------------------------------------------------
	    		bufferCSV.write("Mortalidad Ptes Hospitalizados");
	    		
	    		
	    		int totalMortalidadHospitalizados = 0;
	    		double totalTasaMortalidadHospitalizados = 0;
	    		
	    		for(HashMap<String, Object> elementoMes:meses)
	    		{
	    			//Se saca el total de mortalidad de hospitalizados x cada mes
	    			int totalMortalidadHospitalizadosMes = Utilidades.convertirAEntero(elementoMes.get("totalMortalidadHospitalizados")+"",true);
	    			int totalEgresosHospitalizacion = Utilidades.convertirAEntero(elementoMes.get("totalEgresosHospitalizacion")+"",true);
	    			
	    			double totalTasaMortalidadHospitalizadosMes = 0;
	    			if(totalEgresosHospitalizacion>0)
	    				totalTasaMortalidadHospitalizadosMes = ((double)totalMortalidadHospitalizadosMes/(double)totalEgresosHospitalizacion)*100;
	    			else
	    				totalTasaMortalidadHospitalizadosMes = 0;
	    			
	    			bufferCSV.write(","+totalMortalidadHospitalizadosMes);
	    			bufferCSV.write(","+UtilidadTexto.formatearValores(totalTasaMortalidadHospitalizadosMes,"0.00"));
	    			
	    			totalMortalidadHospitalizados += totalMortalidadHospitalizadosMes;
	    			totalTasaMortalidadHospitalizados += totalTasaMortalidadHospitalizadosMes;
	    		}
	    		
	    		//Se dibujan los totales MORTALIDAD DE HOSPITALIZADOS
	    		//TOTAL
	    		bufferCSV.write(","+totalMortalidadHospitalizados);
	    		//TASA GLOBAL
				bufferCSV.write(","+UtilidadTexto.formatearValores(totalTasaMortalidadHospitalizados/meses.size(),"0.00")+"\n");
				
				/*** ****************************************************************************************************************************************************** ***/
				//Etiqueta EGRESOS PACIENTES HOSPITALIZADOS--------------------------------------------------------------------------
				bufferCSV.write("Total Egresos Ptes Hospitalizados");
	    		
	    		int totalEgresosPtesHospitalizados = 0;
	    		double totalTasaEgresosHospitalizados = 0;
	    		
	    		for(HashMap<String, Object> elementoMes:meses)
	    		{
	    			//Se saca el total de mortalidad de hospitalizados x cada mes
	    			totalMortalidadHospitalizados = Utilidades.convertirAEntero(elementoMes.get("totalMortalidadHospitalizados")+"",true);
	    			//Se saca el total de egresos de hospitalizados x cadad mes
	    			int totalEgresosHospitalizados = Utilidades.convertirAEntero(elementoMes.get("totalEgresosHospitalizados")+"",true);
	    			
	    			double totalTasaEgresosHospitalizadosMes = 0;
	    			if(totalEgresosHospitalizados>0)
	    				totalTasaEgresosHospitalizadosMes = ((double)totalMortalidadHospitalizados/(double)totalEgresosHospitalizados)*100;
	    			else
	    				totalTasaEgresosHospitalizadosMes = 0;
	    			totalEgresosPtesHospitalizados += totalEgresosHospitalizados;
	    			totalTasaEgresosHospitalizados += totalTasaEgresosHospitalizadosMes;
	    		}
	    		
	    		//Se dibujan los totales EGRESOS PACIENTES HOSPITALIZADOS
	    		//TOTAL
	    		bufferCSV.write(","+totalEgresosPtesHospitalizados);
	    		//TASA GLOBAL
				bufferCSV.write(","+UtilidadTexto.formatearValores(totalTasaEgresosHospitalizados/meses.size(),"0.00")+"\n");
				
				/*** ****************************************************************************************************************************************************** ***/
	    		//Etiqueta MORTALIDAD PACIENTES CIRUGIA AMBULATORIA ------------------------------------------------------------
				bufferCSV.write("Mortalidad Ptes Cx Ambulatoria");
	    		
	    		int totalMortalidadPtesCxAmbulatoria = 0;
	    		double totalTasaMortalidadPtesCxAmbulatoria = 0;
	    		
	    		for(HashMap<String, Object> elementoMes:meses)
	    		{
	    			//Se saca el total de mortalidad de cx ambulatoria x cada mes
	    			int totalMortalidadCxAmbulatoriaMes = Utilidades.convertirAEntero(elementoMes.get("totalMortalidadCxAmbulatoria")+"",true);
	    			//Se saca el total de egresos de via ingrso hospitakizacion x cadad mes
	    			int totalEgresosHospitalizacion = Utilidades.convertirAEntero(elementoMes.get("totalEgresosHospitalizacion")+"",true);
	    			
	    			double totalTasaMortalidadCxAmbulatoriaMes = 0;
	    			if(totalEgresosHospitalizacion>0)
	    				totalTasaMortalidadCxAmbulatoriaMes = ((double)totalMortalidadCxAmbulatoriaMes/(double)totalEgresosHospitalizacion)*100;
	    			else
	    				totalTasaMortalidadCxAmbulatoriaMes = 0;
	    			bufferCSV.write(","+totalMortalidadCxAmbulatoriaMes);
	    			bufferCSV.write(","+UtilidadTexto.formatearValores(totalTasaMortalidadCxAmbulatoriaMes,"0.00"));
	    			
	    			totalMortalidadPtesCxAmbulatoria += totalMortalidadCxAmbulatoriaMes;
	    			totalTasaMortalidadPtesCxAmbulatoria += totalTasaMortalidadCxAmbulatoriaMes;
	    		}
	    		
	    		//Se dibujan los totales MORTALIDAD PACIENTES CIRUGÍA AMBULATORIA
	    		//TOTAL
	    		bufferCSV.write(","+totalMortalidadPtesCxAmbulatoria);
	    		//TASA GLOBAL
				bufferCSV.write(","+UtilidadTexto.formatearValores(totalTasaMortalidadPtesCxAmbulatoria/meses.size(),"0.00")+"\n");
				
				/*** ****************************************************************************************************************************************************** ***/
				//Etiqueta TOTAL EGRESOS PACIENTES CIRUGIA AMBULATORIA ------------------------------------------------------------
				bufferCSV.write("Total Egresos Ptes Cx Ambulatoria");
	    		
	    		int totalEgresosPtesCxAmbulatoria = 0;
	    		double totalTasaEgresosPtesCxAmbulatoria = 0;
	    		
	    		for(HashMap<String, Object> elementoMes:meses)
	    		{
	    			//Se saca el total de mortalidad de cx ambulatoria x cada mes
	    			int totalMortalidadCxAmbulatoriaMes = Utilidades.convertirAEntero(elementoMes.get("totalMortalidadCxAmbulatoria")+"",true);
	    			//Se saca el total de egresos de cx ambulatoria x cadad mes
	    			int totalEgresosCxAmbulatoriaMes = Utilidades.convertirAEntero(elementoMes.get("totalEgresosCxAmbulatoria")+"",true);
	    			
	    			double totalTasaEgresosCxAmbulatoriaMes = 0;
	    			if(totalEgresosCxAmbulatoriaMes>0)
	    				totalTasaEgresosCxAmbulatoriaMes = ((double)totalMortalidadCxAmbulatoriaMes/(double)totalEgresosCxAmbulatoriaMes)*100;
	    			else
	    				totalTasaEgresosCxAmbulatoriaMes = 0;
	    			bufferCSV.write(","+totalEgresosCxAmbulatoriaMes);
	    			bufferCSV.write(","+UtilidadTexto.formatearValores(totalTasaEgresosCxAmbulatoriaMes,"0.00"));
	    			
	    			totalEgresosPtesCxAmbulatoria += totalEgresosCxAmbulatoriaMes;
	    			totalTasaEgresosPtesCxAmbulatoria += totalTasaEgresosCxAmbulatoriaMes;
	    		}
	    		
	    		//Se dibujan los totales TOTAL EGRESOS PACIENTES CIRUGÍA AMBULATORIA
	    		//TOTAL
	    		bufferCSV.write(","+totalEgresosPtesCxAmbulatoria);
	    		//TASA GLOBAL
				bufferCSV.write(","+UtilidadTexto.formatearValores(totalTasaEgresosPtesCxAmbulatoria/meses.size(),"0.00")+"\n");
	    		
				/*** ****************************************************************************************************************************************************** ***/
				//Etiqueta TOTAL EGRESOS HOSPITALIZACION------------------------------------------------------------------------------------
				bufferCSV.write("Total Egresos Hospitalización");
	    		
	    		int totalEgresosHospitalizacion = 0;
	    		
	    		for(HashMap<String, Object> elementoMes:meses)
	    		{
	    			//Se saca el total de egresos de hosptilizacion por mes
	    			int totalEgresosHospitalizacionMes = Utilidades.convertirAEntero(elementoMes.get("totalEgresosHospitalizacion")+"",true);
	    			
	    			bufferCSV.write(","+totalEgresosHospitalizacionMes);
	    			bufferCSV.write(",-");
	    			
	    			totalEgresosHospitalizacion += totalEgresosHospitalizacionMes;
	    		}
	    		
	    		//Se dibujan los totales TOTAL EGRESOS HOSPITALIZACION
	    		//TOTAL
	    		bufferCSV.write(","+totalEgresosHospitalizacion);
	    		//TASA GLOBAL
				bufferCSV.write(",-\n");
	    		
				
	    		/*** ****************************************************************************************************************************************************** ***/
				
				
	    	}
	    	//*************************************************************************************************************************
	    	//***************SE DIBUJAN LOS TOTALES QUE TIENEN QUE VER CON URGENCIAS***************************************************
	    	if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
	    	{
	    		
	    		/*** ****************************************************************************************************************************************************** ***/
	    		//Etiqueta MORTALIDAD PACIENTES URGENCIAS------------------------------------------------------------
				bufferCSV.write("Mortalidad Urgencias");
	    		
	    		int totalMortalidadUrgencias = 0;
	    		double totalTasaMortalidadUrgencias = 0;
	    		
	    		for(HashMap<String, Object> elementoMes:meses)
	    		{
	    			//Se saca el total de mortalidad de urgencias x cada mes
	    			int totalMortalidadUrgenciasMes = Utilidades.convertirAEntero(elementoMes.get("totalMortalidadUrgencias")+"",true);
	    			//Se saca el total de egresos de via ingrso hospitakizacion x cadad mes
	    			int totalEgresosUrgenciasMes = Utilidades.convertirAEntero(elementoMes.get("totalEgresosUrgencias")+"",true);
	    			
	    			double totalTasaMortalidadUrgenciasMes = 0;
	    			if(totalEgresosUrgenciasMes>0)
	    				totalTasaMortalidadUrgenciasMes = ((double)totalMortalidadUrgenciasMes/(double)totalEgresosUrgenciasMes)*100;
	    			else
	    				totalTasaMortalidadUrgenciasMes = 0;
	    			bufferCSV.write(","+totalMortalidadUrgenciasMes);
	    			bufferCSV.write(","+UtilidadTexto.formatearValores(totalTasaMortalidadUrgenciasMes,"0.00"));
	    			
	    			totalMortalidadUrgencias += totalMortalidadUrgenciasMes;
	    			totalTasaMortalidadUrgencias += totalTasaMortalidadUrgenciasMes;
	    		}
	    		
	    		//Se dibujan los totales MORTALIDAD PACIENTES URGENCIAS
	    		//TOTAL
	    		bufferCSV.write(","+totalMortalidadUrgencias);
	    		//TASA GLOBAL
				bufferCSV.write(","+UtilidadTexto.formatearValores(totalTasaMortalidadUrgencias/meses.size(),"0.00")+"\n");
				
				/*** ****************************************************************************************************************************************************** ***/
				//Etiqueta TOTAL EGRESOS URGENCIAS------------------------------------------------------------------------------------
				bufferCSV.write("Total Egresos Urgencias");
	    		
	    		int totalEgresosUrgencias = 0;
	    		
	    		for(HashMap<String, Object> elementoMes:meses)
	    		{
	    			//Se saca el total de egresos de urgencias por mes
	    			int totalEgresosUrgenciasMes = Utilidades.convertirAEntero(elementoMes.get("totalEgresosUrgencias")+"",true);
	    			
	    			bufferCSV.write(","+totalEgresosUrgenciasMes);
	    			bufferCSV.write(",-");
	    			
	    			totalEgresosUrgencias += totalEgresosUrgenciasMes;
	    		}
	    		
	    		//Se dibujan los totales TOTAL EGRESOS URGENCIAS
	    		//TOTAL
	    		bufferCSV.write(","+totalEgresosUrgencias);
	    		//TASA GLOBAL
				bufferCSV.write(",-\n");
	    		
				
	    		/*** ****************************************************************************************************************************************************** ***/
	    		
	    		
	    		
	    	}
	    	//*************************************************************************************************************************
	    	//***********SE DIBUJAN LOS DATOS DE LA MORTALIDAD GENERAL*******************************************************************
	    	if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
	    	{
	    		/*** ****************************************************************************************************************************************************** ***/
	    		//Etiqueta MORTALIDAD GENERAL------------------------------------------------------------
				bufferCSV.write("Mortalidad General");
	    		
	    		int totalMortalidadGeneral = 0;
	    		double totalTasaMortalidadGeneral = 0;
	    		
	    		for(HashMap<String, Object> elementoMes:meses)
	    		{
	    			//Se saca el total de mortalidad de urgencias x cada mes
	    			int totalMortalidadHospitalizadosMes = Utilidades.convertirAEntero(elementoMes.get("totalMortalidadHospitalizados")+"",true);
	    			//Se saca el total de mortalidad de urgencias x cada mes
	    			int totalMortalidadCxAmbulatoriaMes = Utilidades.convertirAEntero(elementoMes.get("totalMortalidadCxAmbulatoria")+"",true);
	    			//Se saca el total de mortalidad de urgencias x cada mes
	    			int totalMortalidadUrgenciasMes = Utilidades.convertirAEntero(elementoMes.get("totalMortalidadUrgencias")+"",true);
	    			int totalMortalidadGeneralMes = totalMortalidadHospitalizadosMes + totalMortalidadCxAmbulatoriaMes + totalMortalidadUrgenciasMes;
	    			
	    			//Se saca el total de egresos de via ingrso hospitalizacion x cadad mes
	    			int totalEgresosHospitalizacionMes = Utilidades.convertirAEntero(elementoMes.get("totalEgresosHospitalizacion")+"",true);
	    			//Se saca el total de egresos de via ingrso urgencias x cadad mes
	    			int totalEgresosUrgenciasMes = Utilidades.convertirAEntero(elementoMes.get("totalEgresosUrgencias")+"",true);
	    			int totalEgresosGeneralMes = totalEgresosHospitalizacionMes + totalEgresosUrgenciasMes;
	    			
	    			double totalTasaMortalidadGeneralMes = 0;
	    			if(totalEgresosUrgenciasMes>0||totalEgresosHospitalizacionMes>0)
	    				totalTasaMortalidadGeneralMes = ((double)totalMortalidadGeneralMes/(double)totalEgresosGeneralMes)*100;
	    			else
	    				totalTasaMortalidadGeneralMes = 0;
	    			bufferCSV.write(","+totalMortalidadGeneralMes);
	    			bufferCSV.write(","+UtilidadTexto.formatearValores(totalTasaMortalidadGeneralMes,"0.00"));
	    			
	    			totalMortalidadGeneral += totalMortalidadGeneralMes;
	    			totalTasaMortalidadGeneral += totalTasaMortalidadGeneralMes;
	    		}
	    		
	    		//Se dibujan los totales MORTALIDAD GENERAL
	    		//TOTAL
	    		bufferCSV.write(","+totalMortalidadGeneral);
	    		//TASA GLOBAL
				bufferCSV.write(","+UtilidadTexto.formatearValores(totalTasaMortalidadGeneral/meses.size(),"0.00")+"\n");
				
				/*** ****************************************************************************************************************************************************** ***/
	    	}
	    	//***************************************************************************************************************************
	    	
		    //********************************************************************************************************************
		    
		    //Se finaliza el archivo CSV
		    bufferCSV.close();
	    }
	    catch(IOException e)
	    {
	    	logger.error("Error tratando de generar el archivo : "+nombreArchivoCsv+" (Se cancela proceso de impresión) :"+e);
	    }
    }

    /**
     * Método para obtener el total de egresos 
     * @param mapa
     * @param codigoMes
     * @return
     */
    private static int calcularTotalSinRangos(HashMap<String, Object> mapa, String codigoMes,String llave) 
    {
    	int total = 0;
    	
		for(int i=0;i<Utilidades.convertirAEntero(mapa.get("numRegistros")+"");i++)
			if(codigoMes.equals(mapa.get("mesEgreso_"+i).toString()))
				total = Utilidades.convertirAEntero(mapa.get(llave+"_"+i).toString(), true);
		return total;
	}

	/**
     * Método para obtener el total de muertes rango mes
     * @param datosHospitalizados
     * @param orden
     * @param codigoMes
     * @return
     */
    private static int calcularTotalMuertesRangoMes(HashMap<String, Object> datosHospitalizados, String orden,String codigoMes) 
    {
    	int totalMuertesRangoMes = 0;
    	
    	for(int i=0;i<Utilidades.convertirAEntero(datosHospitalizados.get("numRegistros")+"");i++)
    		if(codigoMes.equals(datosHospitalizados.get("mesEgreso_"+i).toString())&&orden.equals(datosHospitalizados.get("orden_"+i).toString()))
    			totalMuertesRangoMes += Utilidades.convertirAEntero(datosHospitalizados.get("muerte_"+i).toString(), true); 
		
		return totalMuertesRangoMes;
	}

	/**
     * Método para obtener el totla de egresos de pacientes hospitalizados
     * @param datosHospitalizados
     * @param codigoMes
     * @return
     */
	private static int calcularTotalEgresosHospitalizados(HashMap<String, Object> datosHospitalizados, String codigoMes) 
	{
		int totalEgresos = 0;
		
		for(int i=0;i<Utilidades.convertirAEntero(datosHospitalizados.get("numRegistros")+"");i++)
			if(codigoMes.equals(datosHospitalizados.get("mesEgreso_"+i).toString()))
				totalEgresos += Utilidades.convertirAEntero(datosHospitalizados.get("paciente_"+i).toString(), true);
		
		return totalEgresos;
	}
}
