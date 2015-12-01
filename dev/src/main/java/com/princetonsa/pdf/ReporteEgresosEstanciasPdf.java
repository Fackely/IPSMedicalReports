/*
 * Sep 10, 2008
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

import util.BackUpBaseDatos;
import util.ConstantesBD;
import util.UtilidadFileUpload;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;
import util.pdf.iTextBaseTable;

import com.princetonsa.actionform.manejoPaciente.ReporteEgresosEstanciasForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.ReporteEgresosEstancias;

/**
 * 
 * Clase usada para generar reportes de resumen mensual egresos
 * @author Sebastián Gómez R.
 *
 */
public class ReporteEgresosEstanciasPdf 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
    private static Logger logger=Logger.getLogger(ReporteEgresosEstanciasPdf.class);
    
    /**
     * Método para realizar la impresión del reporte de resumen mensual egresos
     * @param filename
     * @param forma
     * @param usuario
     */
    public static void pdfResumenMensualEgresos(Connection con,String pathArchivo,String nombreArchivo, ReporteEgresosEstanciasForm forma,UsuarioBasico usuario, HttpServletRequest request)
    {
    	String filename = pathArchivo + System.getProperty("file.separator") + nombreArchivo;
    	
    	
    	InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
    	
    	//*************IMPRESION DEL ENCABEZADO************************************************
    	PdfReports report = new PdfReports("false",true); 
        
        String filePath=ValoresPorDefecto.getFilePath();
        String tituloDespacho="RESUMEN MENSUAL EGRESOS" ;
        
        filePath=filePath.substring(0, filePath.lastIndexOf("/", (filePath.length()-2)));
		report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), tituloDespacho);
		
        //Se abre el reporte
        report.openReport(filename);
        report.font.useFont(iTextBaseFont.FONT_TIMES, true, true, true);
        iTextBaseTable section;
        //********************************************************************************************************
        
        //**************SECCION ENCABEZADO (Se muestran los parámetros de busqueda)*********************************************************************
        
        section = report.createSection("encabezado", "encabezadoTable", 1, 4, 10);
  		section.setTableBorder("encabezadoTable", 0x000000, 0.0f);
	    section.setTableCellBorderWidth("encabezadoTable", 0.0f);
	    section.setTableCellPadding("encabezadoTable", 1);
	    section.setTableSpaceBetweenCells("encabezadoTable", 0.1f);
	    section.setTableCellsDefaultColors("encabezadoTable", 0xFFFFFF, 0xFFFFFF);
	    
	    //Se limpian las descripciones de los campos
	    forma.setNombreCentroAtencion("");
	    forma.setNombreSexo("");
	    forma.setNombreViaIngreso("");
	    forma.setNombreTipoPaciente("");
	    
	    String contenidoEncabezado = "";
	    section.setTableCellsColSpan(4);
	    report.font.setFontAttributes(0x000000, 7, false, false, false);
	    contenidoEncabezado += "[Centro de Atención: "+forma.getNombreCentroAtencion()+"] "+
	    	"[Fecha inicial: "+forma.getFechaInicial() +"] "+
	    	"[Fecha final: "+forma.getFechaFinal()+"] "+
	    	"[Vía de ingreso: "+forma.getNombreViaIngreso()+"] "+
	    	"[Tipo Paciente: "+forma.getNombreTipoPaciente() +"] "+
	    	"[Estado Facturacion: "+forma.getDescripcionEstadoFacturacion() +"] "+
	    	"[Sexo: "+forma.getNombreSexo()+"] ";
	    
	    
	    
	    if(forma.getNumCentrosCostoSeleccionados()>0)
	    {
	    	
	    	String listadoCentrosCosto = "";
	    	for(int i=0;i<forma.getNumCentrosCosto();i++)
	    		if(UtilidadTexto.getBoolean(forma.getCentrosCosto("checkbox_"+i).toString()))
	    			listadoCentrosCosto += (listadoCentrosCosto.equals("")?"":", ") + forma.getCentrosCosto("nombre_"+i);
	    	
	    	contenidoEncabezado += "[Centros Costo: "+listadoCentrosCosto+"]";
	    	
	    }
	    else
	    	contenidoEncabezado += "[Centros Costo: Todos]";
	    section.addTableTextCellAligned("encabezadoTable", contenidoEncabezado, report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    
	    //se añade la sección al documento
	    report.addSectionToDocument("encabezado");
	    //*****************************************************************************************************************
	    
	    //****************************SECCION DETALLE*********************************************************************
	    int numRows = 0, numCols = 0, ordenRango = 0;
	    ArrayList<HashMap<String,Object>> rangos = new ArrayList<HashMap<String,Object>>();
	    
	    //Se consultan los datos del reporte
	    HashMap<String, Object> datosReporte = ReporteEgresosEstancias.obtenerDatosResumenMensualEgresos(con, forma.getCodigoCentroAtencion(), forma.getFechaInicial(), forma.getFechaFinal(), forma.getCodigoViaIngreso(), forma.getCodigoTipoPaciente(), forma.getEstadoFacturacion(), forma.getCodigoSexo(), forma.getCentrosCosto(), usuario.getCodigoInstitucionInt());
	    //Se toman los rangos del reporte
	    for(int i=0;i<Integer.parseInt(datosReporte.get("numRegistros").toString());i++)
	    	if(ordenRango!=Integer.parseInt(datosReporte.get("orden_"+i).toString()))
	    	{
	    		numRows ++;
	    		ordenRango = Integer.parseInt(datosReporte.get("orden_"+i).toString());
	    		//Se agrega el elemento del rango al arreglo de rangos
	    		HashMap<String, Object> elemento = new HashMap<String, Object>();
	    		elemento.put("orden",datosReporte.get("orden_"+i));
	    		elemento.put("nombreEtiqueta",datosReporte.get("nombreEtiqueta_"+i));
	    		elemento.put("codigoReporte",datosReporte.get("codigoReporte_"+i));
	    		rangos.add(elemento);
	    	}
	    numRows += 4; // Se suman 4 filas mas para los 3 encabezados de las columnas y el total del final de la tabla
	    
	    //Se verifica el número de columnas
	    if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
	    {
	    	if(forma.getCodigoTipoPaciente().equals(""))
	    		numCols += 5; //Incluye Egresos, estancias y promedio de Hospitalizados, Egresos de Cirugía Ambulatoria y Total Hospitalizacion
	    	else if(forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteHospitalizado))
	    		numCols += 3; //Incluye Egresos, estancias y promedio de Hospitalizados 
	    	else if(forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteCirugiaAmbulatoria))
	    		numCols += 1; //Incluye Egresos de Cirugía Ambulatoria
	    		
	    }
	    if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
	    {
	    	if(forma.getCodigoTipoPaciente().equals("")||forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteAmbulatorio))
	    		numCols += 3; //Incluye Egresos, Estancias y Promedio de URGENCIAS
	    }
	    if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoConsultaExterna||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
	    {
	    	if(forma.getCodigoTipoPaciente().equals("")||forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteAmbulatorio))
	    		numCols += 3; //Incluye Primera vez, Control y Total de CONSULTA EXTERNA
	    }
	    numCols ++; //Se añade una columna mas para los rangos de edad
	    
	  
		    
	    //*****************SE GENERA LA TABLA DEL REPORTE*************************************************************************************
		logger.info("numero de filas: "+numRows);
		logger.info("numero de columnas: "+numCols);
		section = report.createSection("reporte", "reporteTable", numRows, numCols, 0.5f);
  		section.setTableBorder("reporteTable", 0x000000, 0.1f);
	    section.setTableCellBorderWidth("reporteTable", 0.1f);
	    section.setTableCellPadding("reporteTable", 1);
	    section.setTableSpaceBetweenCells("reporteTable", 0.1f);
	    section.setTableCellsDefaultColors("reporteTable", 0xFFFFFF, 0x000000);
	    
	    
	    //**********SE DIBUJAN LOS TÍTULOS DE LAS COLUMNAS******************************************************************
	    report.font.setFontAttributes(0x000000, 8, true, false, false);
	    section.setTableCellsColSpan(1);
	    section.setTableCellsRowSpan(2);
	    logger.info("inserción columna vacía, con colspan 1");
	    section.addTableTextCellAligned("reporteTable", "", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    
		
		//Titulo vía de ingreso hospitalizacion
		if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
		{
			
			if(forma.getCodigoTipoPaciente().equals(""))
			{
				section.setTableCellsRowSpan(1);
				section.setTableCellsColSpan(5); //Incluye Egresos, estancias y promedio de Hospitalizados, Egresos de Cirugía Ambulatoria y Total Hospitalizacion
				
				logger.info("inserción columna hospitalizacion, con colspan 5");
				section.addTableTextCellAligned("reporteTable", "HOSPITALIZACIÓN", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			}
	    	else if(forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteHospitalizado))
	    	{
	    		section.setTableCellsRowSpan(1);
	    		section.setTableCellsColSpan(3); //Incluye Egresos, estancias y promedio de Hospitalizados
	    		
	    		logger.info("inserción columna hospitalizacion, con colspan 3");
	    		section.addTableTextCellAligned("reporteTable", "HOSPITALIZACIÓN", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    	}
	    	else if(forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteCirugiaAmbulatoria))
	    	{
	    		section.setTableCellsRowSpan(1);
	    		section.setTableCellsColSpan(1); //Incluye Egresos de Cirugía Ambulatoria
	    		
	    		logger.info("inserción columna hospitalizacion, con colspan 1");
	    		section.addTableTextCellAligned("reporteTable", "HOSPITALIZACIÓN", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    	}
			
		}
		//Título vía de ingreso consulta externa
		if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoConsultaExterna||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
	    {
			
	    	if(forma.getCodigoTipoPaciente().equals("")||forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteAmbulatorio))
	    	{
	    		section.setTableCellsRowSpan(2);
	    		section.setTableCellsColSpan(3); //Incluye Primera vez, Control y Total de CONSULTA EXTERNA
	    		
	    		logger.info("inserción columna consulta externa, con colspan 3");
	    		section.addTableTextCellAligned("reporteTable", "CONSULTA EXTERNA", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    	}
	    	
	    }
		//Título vía de ingreso urgencias
		 if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
	    {
			
	    	if(forma.getCodigoTipoPaciente().equals("")||forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteAmbulatorio))
	    	{
	    		section.setTableCellsRowSpan(2);
	    		section.setTableCellsColSpan(3); //Incluye Egresos, Estancias y Promedio de URGENCIAS
	    		
	    		logger.info("inserción columna urgencias, con colspan 3");
	    		section.addTableTextCellAligned("reporteTable", "URGENCIAS", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    	}
	    	
	    }
		 
		 section.setTableCellsRowSpan(1);
		//Subtitulo de los tipos paciente de hospitalizacion
		 if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
		{
			if(forma.getCodigoTipoPaciente().equals("")||forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteHospitalizado))
			{
				section.setTableCellsColSpan(3); //Incluye Egresos, estancias y promedio de Hospitalizados,
				
				section.addTableTextCellAligned("reporteTable", "Hospitalizados", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			}
			
			if(forma.getCodigoTipoPaciente().equals("")||forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteCirugiaAmbulatoria))
			{
				section.setTableCellsColSpan(1); //Incluye Egresos de Cirugía Ambulatoria
				
				section.addTableTextCellAligned("reporteTable", "Cirugía Ambulatoria", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			}
			
			if(forma.getCodigoTipoPaciente().equals(""))
			{
				section.setTableCellsColSpan(1); //Incluye Total Hospitalizacion
				
				section.setTableCellsRowSpan(2);
				section.addTableTextCellAligned("reporteTable", "Total Gral", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			}
	    	
		}
		/**if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoConsultaExterna||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
			if(forma.getCodigoTipoPaciente().equals("")||forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteAmbulatorio))
				 bufferCSV.write(",,,");
		if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
			 if(forma.getCodigoTipoPaciente().equals("")||forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteAmbulatorio))
				 bufferCSV.write(",,,");**/
		
		section.setTableCellsRowSpan(1);
		section.setTableCellsColSpan(1);
		//Dibujo de los subtitulos de cada columna especifica
		section.addTableTextCellAligned("reporteTable", "Rangos Edad", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		
		//Subtitulos columna específica para la vía de ingreso de hospitalizacion
		if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
		{
			if(forma.getCodigoTipoPaciente().equals("")||forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteHospitalizado))
			{
				section.addTableTextCellAligned("reporteTable", "Egresos", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				section.addTableTextCellAligned("reporteTable", "Estancias", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				section.addTableTextCellAligned("reporteTable", "Promedio día estancia", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				
			}
			
			if(forma.getCodigoTipoPaciente().equals("")||forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteCirugiaAmbulatoria))
			{
				section.addTableTextCellAligned("reporteTable", "Egresos", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			
			}
			
	    	
		}
		//Subtitulos columna específica para la vía de ingreso de consulta externa
		if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoConsultaExterna||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
	    	if(forma.getCodigoTipoPaciente().equals("")||forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteAmbulatorio))
	    	{
	    		section.addTableTextCellAligned("reporteTable", "Primera vez", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				section.addTableTextCellAligned("reporteTable", "Control", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				section.addTableTextCellAligned("reporteTable", "Total", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				
	    	}
		//Subtitulos columna específica para la vía de ingreso de urgencias
		 if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
	    	if(forma.getCodigoTipoPaciente().equals("")||forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteAmbulatorio))
	    	{
	    		section.addTableTextCellAligned("reporteTable", "Egresos", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				section.addTableTextCellAligned("reporteTable", "Estancias", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				section.addTableTextCellAligned("reporteTable", "Promedio día estancia", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				
	    	}
		
		
    	report.font.setFontAttributes(0x000000, 8, false, false, false);
    	
    	
    	/**  PARTE DONDE SE INSERTAN LOS DATOS DEL REPORTE **/
    	int sumEgresosHospitalizados = 0;
    	int sumEstanciasHospitalizados = 0;
    	
    	int sumEgresosCxAmbulatoria = 0;
    	int totalHospitalizacion = 0;
    	int sumPrimeraVez = 0;
    	int sumControl = 0;
    	int totalConsultaExterna = 0;
    	int sumEgresosUrgencias = 0;
    	int sumEstanciasUrgencias = 0;
    	
    	String valor = "";
    	//Se iteran los rangos ---------------------------------------
    	for(HashMap<String, Object> elemento:rangos)
    	{
    		section.addTableTextCellAligned("reporteTable", elemento.get("nombreEtiqueta").toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
    		
    		
    		if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
			{
    			int egresosCxAmbulatoria = 0;
    			int egresosHospitalizados = 0;
    			
				if(forma.getCodigoTipoPaciente().equals("")||forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteHospitalizado))
				{
					//Columna egresos - hospitalizados
					valor = obtenerValorDatosReporteResumenMensualEgreso(Integer.parseInt(elemento.get("codigoReporte").toString()), "Egresos", ConstantesBD.codigoViaIngresoHospitalizacion, ConstantesBD.tipoPacienteHospitalizado, datosReporte);
					egresosHospitalizados = Utilidades.convertirAEntero(valor, true);
					section.addTableTextCellAligned("reporteTable", egresosHospitalizados+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
					
					//Columna estancias - hospitalizados
					valor = obtenerValorDatosReporteResumenMensualEgreso(Integer.parseInt(elemento.get("codigoReporte").toString()), "Estancias", ConstantesBD.codigoViaIngresoHospitalizacion, ConstantesBD.tipoPacienteHospitalizado, datosReporte);
					int estanciasHospitalizados = Utilidades.convertirAEntero(valor, true);
					section.addTableTextCellAligned("reporteTable", estanciasHospitalizados+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
					
					//Columna promedio día estancia - hospitalizados
					double promedioHospitalizados = ((double)estanciasHospitalizados)/((double)egresosHospitalizados);
					section.addTableTextCellAligned("reporteTable", UtilidadTexto.formatearValores(promedioHospitalizados,"0.00"), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
					
					
					sumEgresosHospitalizados += egresosHospitalizados;
					sumEstanciasHospitalizados += estanciasHospitalizados;
				}
				
				if(forma.getCodigoTipoPaciente().equals("")||forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteCirugiaAmbulatoria))
				{
					//Columna egresos - cirugía ambulatoria
					valor = obtenerValorDatosReporteResumenMensualEgreso(Integer.parseInt(elemento.get("codigoReporte").toString()), "Egresos", ConstantesBD.codigoViaIngresoHospitalizacion, ConstantesBD.tipoPacienteCirugiaAmbulatoria, datosReporte);
					egresosCxAmbulatoria = Utilidades.convertirAEntero(valor, true);
					
					section.addTableTextCellAligned("reporteTable", egresosCxAmbulatoria+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
					
					sumEgresosCxAmbulatoria += egresosCxAmbulatoria;
				}
				
				if(forma.getCodigoTipoPaciente().equals(""))
				{
					//Columna de total hospitalizacion
					section.addTableTextCellAligned("reporteTable", (egresosHospitalizados+egresosCxAmbulatoria)+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
					
					totalHospitalizacion += egresosHospitalizados+egresosCxAmbulatoria; 
				}
		    	
			}
			//Subtitulos columna específica para la vía de ingreso de consulta externa
			if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoConsultaExterna||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
		    	if(forma.getCodigoTipoPaciente().equals("")||forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteAmbulatorio))
		    	{
		    		//Columna primera vez - consulta externa
					valor = obtenerValorDatosReporteResumenMensualEgreso(Integer.parseInt(elemento.get("codigoReporte").toString()), "Primera Vez", ConstantesBD.codigoViaIngresoConsultaExterna, ConstantesBD.tipoPacienteAmbulatorio, datosReporte);
					int primeraVez = Utilidades.convertirAEntero(valor, true);
		    		section.addTableTextCellAligned("reporteTable", primeraVez+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
		    		
		    		//Columna control - consulta externa
					valor = obtenerValorDatosReporteResumenMensualEgreso(Integer.parseInt(elemento.get("codigoReporte").toString()), "Control", ConstantesBD.codigoViaIngresoConsultaExterna, ConstantesBD.tipoPacienteAmbulatorio, datosReporte);
					int control = Utilidades.convertirAEntero(valor, true);
					section.addTableTextCellAligned("reporteTable", control+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
					
					//Columna total - consulta externa
					section.addTableTextCellAligned("reporteTable", (primeraVez+control)+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
					
					
					sumPrimeraVez += primeraVez;
					sumControl += control;
					totalConsultaExterna += (primeraVez+control);
		    	}
			//Subtitulos columna específica para la vía de ingreso de urgencias
			 if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
		    	if(forma.getCodigoTipoPaciente().equals("")||forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteAmbulatorio))
		    	{
		    		//Columna egresos - urgencias
					valor = obtenerValorDatosReporteResumenMensualEgreso(Integer.parseInt(elemento.get("codigoReporte").toString()), "Egresos", ConstantesBD.codigoViaIngresoUrgencias, ConstantesBD.tipoPacienteAmbulatorio, datosReporte);
					int egresosUrgencias = Utilidades.convertirAEntero(valor, true);
		    		section.addTableTextCellAligned("reporteTable", egresosUrgencias+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
		    		
		    		//Columna estancias - urgencias
					valor = obtenerValorDatosReporteResumenMensualEgreso(Integer.parseInt(elemento.get("codigoReporte").toString()), "Estancias", ConstantesBD.codigoViaIngresoUrgencias, ConstantesBD.tipoPacienteAmbulatorio, datosReporte);
					int estanciasUrgencias = Utilidades.convertirAEntero(valor, true);
		    		section.addTableTextCellAligned("reporteTable", estanciasUrgencias+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
		    		
		    		double promedioUrgencias = ((double)estanciasUrgencias)/((double)egresosUrgencias);
					section.addTableTextCellAligned("reporteTable", UtilidadTexto.formatearValores(promedioUrgencias,"0.00"), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
					
					
					sumEgresosUrgencias += egresosUrgencias;
					sumEstanciasUrgencias += estanciasUrgencias;
					
		    	}
			
    		
    		
    		
    		
    	}
    	
    	//Se dibujan los totales de cada columna
    	report.font.setFontAttributes(0x000000, 8, true, false, false);
	    section.addTableTextCellAligned("reporteTable", "TOTAL", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		
		report.font.setFontAttributes(0x000000, 8, false, false, false);
		
		if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
		{
			
			if(forma.getCodigoTipoPaciente().equals("")||forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteHospitalizado))
			{
				//Columna Total egresos - hospitalizados
				section.addTableTextCellAligned("reporteTable", sumEgresosHospitalizados+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
				
				//Columna Total estancias - hospitalizados
				section.addTableTextCellAligned("reporteTable", sumEstanciasHospitalizados+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
				
				//Columna Total promedio día estancia - hospitalizados
				double promedioHospitalizados = ((double)sumEstanciasHospitalizados)/((double)sumEgresosHospitalizados);
				section.addTableTextCellAligned("reporteTable", UtilidadTexto.formatearValores(promedioHospitalizados,"0.00"), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
				
				
				
			}
			
			if(forma.getCodigoTipoPaciente().equals("")||forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteCirugiaAmbulatoria))
			{
				//Columna Total egresos - cirugía ambulatoria
				section.addTableTextCellAligned("reporteTable", sumEgresosCxAmbulatoria+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
				
			}
			
			if(forma.getCodigoTipoPaciente().equals(""))
			{
				//Columna de total hospitalizacion
				section.addTableTextCellAligned("reporteTable", totalHospitalizacion+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
				 
			}
	    	
		}
		//Subtitulos columna específica para la vía de ingreso de consulta externa
		if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoConsultaExterna||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
	    	if(forma.getCodigoTipoPaciente().equals("")||forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteAmbulatorio))
	    	{
	    		//Columna Total primera vez - consulta externa
	    		section.addTableTextCellAligned("reporteTable", sumPrimeraVez+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	    		
	    		//Columna Total control - consulta externa
				section.addTableTextCellAligned("reporteTable", sumControl+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
				
				//Columna total - consulta externa
				section.addTableTextCellAligned("reporteTable", totalConsultaExterna+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
				
	    	}
		//Subtitulos columna específica para la vía de ingreso de urgencias
		 if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
	    	if(forma.getCodigoTipoPaciente().equals("")||forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteAmbulatorio))
	    	{
	    		//Columna total egresos - urgencias
	    		section.addTableTextCellAligned("reporteTable", sumEgresosUrgencias+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	    		
	    		//Columna total estancias - urgencias
	    		section.addTableTextCellAligned("reporteTable", sumEstanciasUrgencias+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	    		
	    		double promedioUrgencias = ((double)sumEstanciasUrgencias)/((double)sumEgresosUrgencias);
				section.addTableTextCellAligned("reporteTable", UtilidadTexto.formatearValores(promedioUrgencias,"0.00"), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
				
				
	    	}
		
    	
	    //**********************************************************************************************
	    
    	
	    
	    ///se añade la sección al documento
	    report.addSectionToDocument("reporte");
	    
	    
	    //********************************************************************************************************************
	    report.closeReport();
		    
		    
	    
	    
	    
	    
	    //******************************************************************************************************************
    }
    
    /**
     * Método para realizar la impresión del reporte de resumen mensual egresos
     * @param filename
     * @param forma
     * @param usuario
     */
    public static String csvResumenMensualEgresos(Connection con,String pathArchivo,String nombreArchivo, ReporteEgresosEstanciasForm forma,UsuarioBasico usuario, HttpServletRequest request)
    {
    	String mensaje = "";
	    //****************************SECCION DETALLE*********************************************************************
	    int numRows = 0, numCols = 0, ordenRango = 0;
	    ArrayList<HashMap<String,Object>> rangos = new ArrayList<HashMap<String,Object>>();
	    
	    //Se consultan los datos del reporte
	    HashMap<String, Object> datosReporte = ReporteEgresosEstancias.obtenerDatosResumenMensualEgresos(con, forma.getCodigoCentroAtencion(), forma.getFechaInicial(), forma.getFechaFinal(), forma.getCodigoViaIngreso(), forma.getCodigoTipoPaciente(), forma.getEstadoFacturacion(), forma.getCodigoSexo(), forma.getCentrosCosto(), usuario.getCodigoInstitucionInt());
	    //Se toman los rangos del reporte
	    for(int i=0;i<Integer.parseInt(datosReporte.get("numRegistros").toString());i++)
	    	if(ordenRango!=Integer.parseInt(datosReporte.get("orden_"+i).toString()))
	    	{
	    		numRows ++;
	    		ordenRango = Integer.parseInt(datosReporte.get("orden_"+i).toString());
	    		//Se agrega el elemento del rango al arreglo de rangos
	    		HashMap<String, Object> elemento = new HashMap<String, Object>();
	    		elemento.put("orden",datosReporte.get("orden_"+i));
	    		elemento.put("nombreEtiqueta",datosReporte.get("nombreEtiqueta_"+i));
	    		elemento.put("codigoReporte",datosReporte.get("codigoReporte_"+i));
	    		rangos.add(elemento);
	    	}
	    numRows += 4; // Se suman 4 filas mas para los 3 encabezados de las columnas y el total del final de la tabla
	    
	    //Se verifica el número de columnas
	    if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
	    {
	    	if(forma.getCodigoTipoPaciente().equals(""))
	    		numCols += 5; //Incluye Egresos, estancias y promedio de Hospitalizados, Egresos de Cirugía Ambulatoria y Total Hospitalizacion
	    	else if(forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteHospitalizado))
	    		numCols += 3; //Incluye Egresos, estancias y promedio de Hospitalizados 
	    	else if(forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteCirugiaAmbulatoria))
	    		numCols += 1; //Incluye Egresos de Cirugía Ambulatoria
	    		
	    }
	    if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
	    {
	    	if(forma.getCodigoTipoPaciente().equals("")||forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteAmbulatorio))
	    		numCols += 3; //Incluye Egresos, Estancias y Promedio de URGENCIAS
	    }
	    if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoConsultaExterna||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
	    {
	    	if(forma.getCodigoTipoPaciente().equals("")||forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteAmbulatorio))
	    		numCols += 3; //Incluye Primera vez, Control y Total de CONSULTA EXTERNA
	    }
	    numCols ++; //Se añade una columna mas para los rangos de edad
	    
	    try
	    {
		    //**************INICIALIZACIÓN DEL ARCHIVO CSV***************************************************************************************
		    File archivoCSV=new File(pathArchivo,nombreArchivo.replaceAll("zip", "csv"));
			FileWriter streamCSV = new FileWriter(archivoCSV,false);
			BufferedWriter bufferCSV=new BufferedWriter(streamCSV);
		    //***********************************************************************************************************************************
		    
		    
		    //*****************SE GENERA LA TABLA DEL REPORTE*************************************************************************************
			logger.info("numero de filas: "+numRows);
			logger.info("numero de columnas: "+numCols);
			
		    
		    //**********SE DIBUJAN LOS TÍTULOS DE LAS COLUMNAS******************************************************************
		    
		    //Se va escribiendo en el archivo CSV
			bufferCSV.write("");
			
			
			//Titulo vía de ingreso hospitalizacion
			if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
			{
				
				if(forma.getCodigoTipoPaciente().equals(""))
					bufferCSV.write(",HOSPITALIZACION,,,,");
		    	else if(forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteHospitalizado))	
		    		bufferCSV.write(",HOSPITALIZACION,,");
		    	else if(forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteCirugiaAmbulatoria))
		    		bufferCSV.write(",HOSPITALIZACION");
				
			}
			//Título vía de ingreso consulta externa
			if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoConsultaExterna||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
		    	if(forma.getCodigoTipoPaciente().equals("")||forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteAmbulatorio))
		    		bufferCSV.write(",CONSULTA EXTERNA,,");
			
			//Título vía de ingreso urgencias
			 if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)	
		    	if(forma.getCodigoTipoPaciente().equals("")||forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteAmbulatorio))
		    		bufferCSV.write(",URGENCIAS,,");
		    
			 bufferCSV.write("\n");
			 
			 
			//Subtitulo de los tipos paciente de hospitalizacion
			 if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
			{
				if(forma.getCodigoTipoPaciente().equals("")||forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteHospitalizado))
					bufferCSV.write(",Hospitalizados,,");
				
				if(forma.getCodigoTipoPaciente().equals("")||forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteCirugiaAmbulatoria))
					bufferCSV.write(",Cirugía Ambulatoria");
					
				if(forma.getCodigoTipoPaciente().equals(""))
					bufferCSV.write(",Total");
					
			}
			/**if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoConsultaExterna||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
				if(forma.getCodigoTipoPaciente().equals("")||forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteAmbulatorio))
					 bufferCSV.write(",,,");
			if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
				 if(forma.getCodigoTipoPaciente().equals("")||forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteAmbulatorio))
					 bufferCSV.write(",,,");**/
			bufferCSV.write("\n");
			 
			
			//Dibujo de los subtitulos de cada columna especifica
			bufferCSV.write("Rangos Edad");
			//Subtitulos columna específica para la vía de ingreso de hospitalizacion
			if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
			{
				if(forma.getCodigoTipoPaciente().equals("")||forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteHospitalizado))
					bufferCSV.write(",Egresos,Estancias,Promedio día estancia");
				
				if(forma.getCodigoTipoPaciente().equals("")||forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteCirugiaAmbulatoria))
					bufferCSV.write(",Egresos");
				
				if(forma.getCodigoTipoPaciente().equals(""))
					bufferCSV.write(",");
		    	
			}
			//Subtitulos columna específica para la vía de ingreso de consulta externa
			if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoConsultaExterna||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
		    	if(forma.getCodigoTipoPaciente().equals("")||forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteAmbulatorio))
					bufferCSV.write(",Primera Vez,Control,Total");
			//Subtitulos columna específica para la vía de ingreso de urgencias
			 if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
		    	if(forma.getCodigoTipoPaciente().equals("")||forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteAmbulatorio))
					bufferCSV.write(",Egresos,Estancias,Promedio día estancia");
			bufferCSV.write("\n");
			
	    	
	    	
	    	
	    	/**  PARTE DONDE SE INSERTAN LOS DATOS DEL REPORTE **/
	    	int sumEgresosHospitalizados = 0;
	    	int sumEstanciasHospitalizados = 0;
	    	
	    	int sumEgresosCxAmbulatoria = 0;
	    	int totalHospitalizacion = 0;
	    	int sumPrimeraVez = 0;
	    	int sumControl = 0;
	    	int totalConsultaExterna = 0;
	    	int sumEgresosUrgencias = 0;
	    	int sumEstanciasUrgencias = 0;
	    	
	    	String valor = "";
	    	//Se iteran los rangos ---------------------------------------
	    	for(HashMap<String, Object> elemento:rangos)
	    	{
	    		
	    		bufferCSV.write(elemento.get("nombreEtiqueta").toString());
	    		
	    		if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
				{
	    			int egresosCxAmbulatoria = 0;
	    			int egresosHospitalizados = 0;
	    			
					if(forma.getCodigoTipoPaciente().equals("")||forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteHospitalizado))
					{
						//Columna egresos - hospitalizados
						valor = obtenerValorDatosReporteResumenMensualEgreso(Integer.parseInt(elemento.get("codigoReporte").toString()), "Egresos", ConstantesBD.codigoViaIngresoHospitalizacion, ConstantesBD.tipoPacienteHospitalizado, datosReporte);
						egresosHospitalizados = Utilidades.convertirAEntero(valor, true);
						
						
						//Columna estancias - hospitalizados
						valor = obtenerValorDatosReporteResumenMensualEgreso(Integer.parseInt(elemento.get("codigoReporte").toString()), "Estancias", ConstantesBD.codigoViaIngresoHospitalizacion, ConstantesBD.tipoPacienteHospitalizado, datosReporte);
						int estanciasHospitalizados = Utilidades.convertirAEntero(valor, true);
						
						
						//Columna promedio día estancia - hospitalizados
						double promedioHospitalizados = ((double)estanciasHospitalizados)/((double)egresosHospitalizados);
						
						
						bufferCSV.write(","+egresosHospitalizados+","+estanciasHospitalizados+","+UtilidadTexto.formatearValores(promedioHospitalizados,"0.00"));
						sumEgresosHospitalizados += egresosHospitalizados;
						sumEstanciasHospitalizados += estanciasHospitalizados;
					}
					
					if(forma.getCodigoTipoPaciente().equals("")||forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteCirugiaAmbulatoria))
					{
						//Columna egresos - cirugía ambulatoria
						valor = obtenerValorDatosReporteResumenMensualEgreso(Integer.parseInt(elemento.get("codigoReporte").toString()), "Egresos", ConstantesBD.codigoViaIngresoHospitalizacion, ConstantesBD.tipoPacienteCirugiaAmbulatoria, datosReporte);
						egresosCxAmbulatoria = Utilidades.convertirAEntero(valor, true);
						
						
						bufferCSV.write(","+egresosCxAmbulatoria);
						sumEgresosCxAmbulatoria += egresosCxAmbulatoria;
					}
					
					if(forma.getCodigoTipoPaciente().equals(""))
					{
						//Columna de total hospitalizacion
						bufferCSV.write(","+(egresosHospitalizados+egresosCxAmbulatoria));
						totalHospitalizacion += egresosHospitalizados+egresosCxAmbulatoria; 
					}
			    	
				}
				//Subtitulos columna específica para la vía de ingreso de consulta externa
				if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoConsultaExterna||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
			    	if(forma.getCodigoTipoPaciente().equals("")||forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteAmbulatorio))
			    	{
			    		//Columna primera vez - consulta externa
						valor = obtenerValorDatosReporteResumenMensualEgreso(Integer.parseInt(elemento.get("codigoReporte").toString()), "Primera Vez", ConstantesBD.codigoViaIngresoConsultaExterna, ConstantesBD.tipoPacienteAmbulatorio, datosReporte);
						int primeraVez = Utilidades.convertirAEntero(valor, true);
			    		
			    		
			    		//Columna control - consulta externa
						valor = obtenerValorDatosReporteResumenMensualEgreso(Integer.parseInt(elemento.get("codigoReporte").toString()), "Control", ConstantesBD.codigoViaIngresoConsultaExterna, ConstantesBD.tipoPacienteAmbulatorio, datosReporte);
						int control = Utilidades.convertirAEntero(valor, true);
						
						
						//Columna total - consulta externa
						
						bufferCSV.write(","+primeraVez+","+control+","+(primeraVez+control));
						
						sumPrimeraVez += primeraVez;
						sumControl += control;
						totalConsultaExterna += (primeraVez+control);
			    	}
				//Subtitulos columna específica para la vía de ingreso de urgencias
				 if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
			    	if(forma.getCodigoTipoPaciente().equals("")||forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteAmbulatorio))
			    	{
			    		//Columna egresos - urgencias
						valor = obtenerValorDatosReporteResumenMensualEgreso(Integer.parseInt(elemento.get("codigoReporte").toString()), "Egresos", ConstantesBD.codigoViaIngresoUrgencias, ConstantesBD.tipoPacienteAmbulatorio, datosReporte);
						int egresosUrgencias = Utilidades.convertirAEntero(valor, true);
			    		
			    		
			    		//Columna estancias - urgencias
						valor = obtenerValorDatosReporteResumenMensualEgreso(Integer.parseInt(elemento.get("codigoReporte").toString()), "Estancias", ConstantesBD.codigoViaIngresoUrgencias, ConstantesBD.tipoPacienteAmbulatorio, datosReporte);
						int estanciasUrgencias = Utilidades.convertirAEntero(valor, true);
			    		
			    		
			    		double promedioUrgencias = ((double)estanciasUrgencias)/((double)egresosUrgencias);
						
						bufferCSV.write(","+egresosUrgencias+","+estanciasUrgencias+","+UtilidadTexto.formatearValores(promedioUrgencias,"0.00"));
						
						sumEgresosUrgencias += egresosUrgencias;
						sumEstanciasUrgencias += estanciasUrgencias;
						
			    	}
				bufferCSV.write("\n");
	    		
	    		
	    		
	    		
	    	}
	    	
	    	//Se dibujan los totales de cada columna
	    	
    		bufferCSV.write("TOTAL");
    		
    		
    		if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
			{
    			
				if(forma.getCodigoTipoPaciente().equals("")||forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteHospitalizado))
				{
					//Columna Total egresos - hospitalizados
					//Columna Total estancias - hospitalizados
					//Columna Total promedio día estancia - hospitalizados
					double promedioHospitalizados = ((double)sumEstanciasHospitalizados)/((double)sumEgresosHospitalizados);
					bufferCSV.write(","+sumEgresosHospitalizados+","+sumEstanciasHospitalizados+","+UtilidadTexto.formatearValores(promedioHospitalizados,"0.00"));
					
				}
				
				if(forma.getCodigoTipoPaciente().equals("")||forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteCirugiaAmbulatoria))
				{
					//Columna Total egresos - cirugía ambulatoria
					bufferCSV.write(","+sumEgresosCxAmbulatoria);
				}
				
				if(forma.getCodigoTipoPaciente().equals(""))
				{
					//Columna de total hospitalizacion
					bufferCSV.write(","+totalHospitalizacion); 
				}
		    	
			}
			//Subtitulos columna específica para la vía de ingreso de consulta externa
			if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoConsultaExterna||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
		    	if(forma.getCodigoTipoPaciente().equals("")||forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteAmbulatorio))
		    	{
		    		//Columna Total primera vez - consulta externa
		    		//Columna Total control - consulta externa
					//Columna total - consulta externa
					bufferCSV.write(","+sumPrimeraVez+","+sumControl+","+totalConsultaExterna);
		    	}
			//Subtitulos columna específica para la vía de ingreso de urgencias
			 if(forma.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias||forma.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
		    	if(forma.getCodigoTipoPaciente().equals("")||forma.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteAmbulatorio))
		    	{
		    		//Columna total egresos - urgencias
		    		//Columna total estancias - urgencias
		    		double promedioUrgencias = ((double)sumEstanciasUrgencias)/((double)sumEgresosUrgencias);
					bufferCSV.write(","+sumEgresosUrgencias+","+sumEstanciasUrgencias+","+UtilidadTexto.formatearValores(promedioUrgencias,"0.00"));
					
		    	}
			bufferCSV.write("\n");
	    	
		    //**********************************************************************************************
		    
	    	
		    
		    //********************************************************************************************************************
		    
		    //Se finaliza el archivo CSV
		    bufferCSV.close();
		    
		    //se genera el archivo en formato Zip
			BackUpBaseDatos.EjecutarComandoSO("zip -j "+pathArchivo+nombreArchivo+" "+pathArchivo+nombreArchivo.replaceAll("zip", "csv"));
			
			if(!UtilidadFileUpload.existeArchivo(pathArchivo, nombreArchivo))
				mensaje = "No se pudo comprimir el archivo. Por favor reportar el error con el administrador del sistema";
	    }
	    catch(IOException e)
	    {
	    	logger.error("Error tratando de generar el archivo CSV: "+nombreArchivo+" (Se cancela proceso) :"+e);
	    }
	    
	    
	    return mensaje;
	    //******************************************************************************************************************
    }
    
    /**
     * Método para obtener el valor de un dato especifico del reporte de resumen mensual egresos
     * @param codigoReporte
     * @param tipo
     * @param codigoViaIngreso
     * @param codigoTipoPaciente
     * @param datosReporte
     * @return
     */
    private static String obtenerValorDatosReporteResumenMensualEgreso(int codigoReporte,String tipo,int codigoViaIngreso,String codigoTipoPaciente,HashMap<String, Object> datosReporte)
    {
    	String valor = "";
    	
    	for(int i=0;i<Integer.parseInt(datosReporte.get("numRegistros").toString());i++)
    	{
    		if(Integer.parseInt(datosReporte.get("codigoReporte_"+i).toString())==codigoReporte&&
    			datosReporte.get("tipo_"+i).toString().equals(tipo)&&
    			Integer.parseInt(datosReporte.get("codigoViaIngreso_"+i).toString())==codigoViaIngreso&&
    			datosReporte.get("codigoTipoPaciente_"+i).toString().equals(codigoTipoPaciente))
    			valor = datosReporte.get("valor_"+i).toString();
    	}
    	
    	return valor;
    }
    
    
    /**
     * Método para realizar la impresión del reporte de resumen mensual egresos
     * @param filename
     * @param forma
     * @param usuario
     */
    public static void pdfDxEgresosRangoEdad(Connection con,String pathArchivo,String nombreArchivo, ReporteEgresosEstanciasForm forma,UsuarioBasico usuario, HttpServletRequest request)
    {
    	String filename = pathArchivo + System.getProperty("file.separator") + nombreArchivo;
    	
    	
    	InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
    	
    	//*************IMPRESION DEL ENCABEZADO************************************************
    	PdfReports report = new PdfReports("false",true); 
        
        String filePath=ValoresPorDefecto.getFilePath();
        String tituloDespacho="DIAGNOSTICOS DE EGRESO POR RANGO DE EDAD" ;
        
        filePath=filePath.substring(0, filePath.lastIndexOf("/", (filePath.length()-2)));
		report.setReportBaseHeader1(institucionBasica,institucionBasica.getNit(), tituloDespacho);
		
        //Se abre el reporte
        report.openReport(filename);
        report.font.useFont(iTextBaseFont.FONT_TIMES, true, true, true);
        iTextBaseTable section;
        //********************************************************************************************************
        
        //**************SECCION ENCABEZADO (Se muestran los parámetros de busqueda)*********************************************************************
        
        section = report.createSection("encabezado", "encabezadoTable", 1, 4, 10);
  		section.setTableBorder("encabezadoTable", 0x000000, 0.0f);
	    section.setTableCellBorderWidth("encabezadoTable", 0.0f);
	    section.setTableCellPadding("encabezadoTable", 1);
	    section.setTableSpaceBetweenCells("encabezadoTable", 0.1f);
	    section.setTableCellsDefaultColors("encabezadoTable", 0xFFFFFF, 0xFFFFFF);
	    
	    //Se limpian las descripciones de los campos
	    forma.setNombreCentroAtencion("");
	    forma.setNombreSexo("");
	    forma.setNombreViaIngreso("");
	    forma.setNombreTipoPaciente("");
	    
	    String contenidoEncabezado = "";
	    section.setTableCellsColSpan(4);
	    report.font.setFontAttributes(0x000000, 7, false, false, false);
	    contenidoEncabezado += "[Centro de Atención: "+forma.getNombreCentroAtencion()+"] "+
	    	"[Fecha inicial: "+forma.getFechaInicial() +"] "+
	    	"[Fecha final: "+forma.getFechaFinal()+"] "+
	    	"[Vía de ingreso: "+forma.getNombreViaIngreso()+"] "+
	    	"[Tipo Paciente: "+forma.getNombreTipoPaciente() +"] "+
	    	"[Prioridad: "+forma.getPrioridad() +"] "+
	    	"[Estado Facturacion: "+forma.getDescripcionEstadoFacturacion() +"] "+
	    	"[Sexo: "+forma.getNombreSexo()+"] ";
	    
	    
	    
	    if(forma.getNumCentrosCostoSeleccionados()>0)
	    {
	    	
	    	String listadoCentrosCosto = "";
	    	for(int i=0;i<forma.getNumCentrosCosto();i++)
	    		if(UtilidadTexto.getBoolean(forma.getCentrosCosto("checkbox_"+i).toString()))
	    			listadoCentrosCosto += (listadoCentrosCosto.equals("")?"":", ") + forma.getCentrosCosto("nombre_"+i).toString().toLowerCase();
	    	
	    	contenidoEncabezado += "[Centros Costo: "+listadoCentrosCosto+"]";
	    	
	    }
	    else
	    	contenidoEncabezado += "[Centros Costo: Todos]";
	    if(forma.getNumConveniosSeleccionados()>0)
	    {
	    	String listadoConvenios = "";
	    	for(int i=0;i<forma.getNumConvenios();i++)
	    		if(UtilidadTexto.getBoolean(forma.getConvenios("checkbox_"+i).toString()))
	    			listadoConvenios += (listadoConvenios.equals("")?"":", ") + forma.getConvenios("nombre_"+i).toString().toLowerCase();
	    	
	    	contenidoEncabezado += "[Convenios: "+listadoConvenios+"]";
	    }
	    else
	    	contenidoEncabezado += "[Convenios: Todos]";
	    section.addTableTextCellAligned("encabezadoTable", contenidoEncabezado, report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    
	    //se añade la sección al documento
	    report.addSectionToDocument("encabezado");
	    //*****************************************************************************************************************
	    
	    //*****************SECCION DETALLE***********************************************************************************
	    int numRows = 0, numCols = 0, ordenRango = 0, totalEgresos = 0, totalEstancia = 0;
	    ArrayList<HashMap<String,Object>> rangos = new ArrayList<HashMap<String,Object>>();
	    ArrayList<HashMap<String,Object>> diagnosticos = new ArrayList<HashMap<String,Object>>();
	    
	    //Se consultan los datos del reporte
	    HashMap<String, Object> datosReporte = ReporteEgresosEstancias.obtenerDatosReporteDxEgresoRangoEdad(con, forma.getCodigoCentroAtencion(), forma.getFechaInicial(), forma.getFechaFinal(), forma.getCodigoViaIngreso(), forma.getCodigoTipoPaciente(), forma.getEstadoFacturacion(), forma.getCodigoSexo(), forma.getCentrosCosto(), forma.getConvenios(), usuario.getCodigoInstitucionInt());
	    //Se consultan los diagnósticos prioritarios
	    diagnosticos = ReporteEgresosEstancias.obtenerDiagnosticosPrioridadEgresos(con, forma.getCodigoCentroAtencion(), forma.getFechaInicial(), forma.getFechaFinal(), forma.getCodigoViaIngreso(), forma.getCodigoTipoPaciente(), Integer.parseInt(forma.getPrioridad()), forma.getEstadoFacturacion(), forma.getCodigoSexo(), forma.getCentrosCosto(), forma.getConvenios());
	    
	    //Se toman los rangos del reporte
	    for(int i=0;i<Integer.parseInt(datosReporte.get("numRegistros").toString());i++)
	    {
	    	if(ordenRango!=Integer.parseInt(datosReporte.get("orden_"+i).toString()))
	    	{
	    		numRows ++;
	    		ordenRango = Integer.parseInt(datosReporte.get("orden_"+i).toString());
	    		//Se agrega el elemento del rango al arreglo de rangos
	    		HashMap<String, Object> elemento = new HashMap<String, Object>();
	    		elemento.put("orden",datosReporte.get("orden_"+i));
	    		elemento.put("nombreEtiqueta",datosReporte.get("nombreEtiqueta_"+i));
	    		elemento.put("codigoReporte",datosReporte.get("codigoReporte_"+i));
	    		rangos.add(elemento);
	    	}
	    	totalEgresos += Integer.parseInt(datosReporte.get("egreso_"+i).toString());
	    	totalEstancia += Integer.parseInt(datosReporte.get("estancia_"+i).toString());
	    }
	    //Calculo de número de filas y columnas
	    numCols  = 3 /**columnas de diagnostico**/ + rangos.size() + 4 /**columnas de totales**/;
	    numRows = 2 /**filas de encabezados**/ + diagnosticos.size() + 3 /**filas de totales**/;
	    
	    //*****************SE GENERA LA TABLA DEL REPORTE*************************************************************************************
		
		section = report.createSection("reporte", "reporteTable", numRows, numCols, 0.5f);
  		section.setTableBorder("reporteTable", 0x000000, 0.1f);
	    section.setTableCellBorderWidth("reporteTable", 0.1f);
	    section.setTableCellPadding("reporteTable", 1);
	    section.setTableSpaceBetweenCells("reporteTable", 0.1f);
	    section.setTableCellsDefaultColors("reporteTable", 0xFFFFFF, 0x000000);
	    
	    //SE DIBUJAN LOS TITULOS DE LAS COLUMNAS ********************************************************************
	    report.font.setFontAttributes(0x000000, 8, true, false, false);
	    section.setTableCellsColSpan(1);
	    section.setTableCellsRowSpan(2);
	    
	    section.addTableTextCellAligned("reporteTable", "#", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    section.addTableTextCellAligned("reporteTable", "Código", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    section.addTableTextCellAligned("reporteTable", "Diagnóstico", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    section.setTableCellsColSpan(rangos.size());
	    section.setTableCellsRowSpan(1);
	    section.addTableTextCellAligned("reporteTable", "Rangos de edad", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    section.setTableCellsColSpan(1);
	    section.setTableCellsRowSpan(2);
	    section.addTableTextCellAligned("reporteTable", "Total egresos", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    section.addTableTextCellAligned("reporteTable", "%", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    section.addTableTextCellAligned("reporteTable", "Total estancia", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    section.addTableTextCellAligned("reporteTable", "Promedio estancia", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    
	    section.setTableCellsRowSpan(1);
	    
	    //Se dibujan los titulos de las columnas de cada rango
	    for(HashMap<String, Object> elemento:rangos)
	    	section.addTableTextCellAligned("reporteTable", elemento.get("nombreEtiqueta").toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    //SE DIBUJAN LOS DATOS DE LOS DIAGNÓSTICOS QUE ABARCAN LA PRIORIDAD**************************************************
	    int contDx = 0, contRango = 0, sumEgresosRango = 0, totalEgresosCausas = 0, totalEstanciasCausas = 0;
	    int[][] totalEgresosMatriz = new int[diagnosticos.size()][rangos.size()];
	    int[] totalEgresosDx = new int[diagnosticos.size()];
	    int[] totalEstanciasDx = new int[diagnosticos.size()];
	    int[] totalEgresosCausasRango = new int[rangos.size()];
	    int[] totalEgresosRango = new int[rangos.size()];
	    
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    
	    for(HashMap<String, Object> elementoD:diagnosticos)
	    {
	    	contRango = 0;
	    	sumEgresosRango = 0; //variable para sumar todos los egresos de un Dx en todos los rangos
	    	
	    	section.addTableTextCellAligned("reporteTable", (contDx+1)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    	section.addTableTextCellAligned("reporteTable", elementoD.get("codigo").toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    	report.font.setFontAttributes(0x000000, 7, false, false, false);
	    	section.addTableTextCellAligned("reporteTable", elementoD.get("nombre").toString().toString().toLowerCase(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    	report.font.setFontAttributes(0x000000, 8, false, false, false);
	    	
	    	//Iteración de los rangos x diagnostico
	    	for(HashMap<String, Object> elementoR:rangos)
	    	{
	    		totalEgresosMatriz[contDx][contRango] = obtenerTotalEgresosDXRango(datosReporte,elementoD.get("codigo").toString() , elementoR.get("codigoReporte").toString());
	    		sumEgresosRango += totalEgresosMatriz[contDx][contRango]; 
	    		section.addTableTextCellAligned("reporteTable", totalEgresosMatriz[contDx][contRango]+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	    		contRango ++;
	    	}
	    	
	    	//Se muestra el total de egresos x diagnostico
	    	totalEgresosDx[contDx] = sumEgresosRango;
	    	section.addTableTextCellAligned("reporteTable", totalEgresosDx[contDx] +"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	    	
	    	//Se calcula el % de egresos por diagnostico
	    	double porcentaje = ((double)sumEgresosRango/(double)totalEgresos)*100;
	    	section.addTableTextCellAligned("reporteTable", UtilidadTexto.formatearValores(porcentaje,"0.00"), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	    	
	    	//Se calcula el total de estancia por diagnóstico
	    	totalEstanciasDx[contDx] = obtenerTotalEstanciaDx(datosReporte, elementoD.get("codigo").toString() );
	    	section.addTableTextCellAligned("reporteTable", totalEstanciasDx[contDx]+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	    	
	    	//Se calcula el promedio estancia
	    	double promedio = (double)totalEstanciasDx[contDx] / (double)sumEgresosRango;
	    	section.addTableTextCellAligned("reporteTable", UtilidadTexto.formatearValores(promedio,"0.00"), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	    	
	    	contDx++;
	    }
	    
	    //SE DIBUJA EL SUBTOTAL DE PRIMERA 'P' CAUSAS********************************************************************
	    section.setTableCellsColSpan(3);
	    report.font.setFontAttributes(0x000000, 8, true, false, false);
	    section.addTableTextCellAligned("reporteTable", "Subtotal primeras 'P' causas", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellsColSpan(1);
	    
	    //Iteración de los rangos x diagnostico
    	for(int i=0;i<rangos.size();i++)
    	{
    		int sumEgresosDx = 0;
    		for(int j=0;j<diagnosticos.size();j++)
    			sumEgresosDx += totalEgresosMatriz[j][i];
    		totalEgresosCausasRango[i] = sumEgresosDx;
    		section.addTableTextCellAligned("reporteTable", totalEgresosCausasRango[i]+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    		
    	
    	}	
    	
    	//Calculo de total egresos causas
    	for(int i=0;i<totalEgresosDx.length;i++)
    		totalEgresosCausas += totalEgresosDx[i];
    	section.addTableTextCellAligned("reporteTable", totalEgresosCausas+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    	
    	//Cálculo del porcentaje
    	double porcentaje = ((double)totalEgresosCausas/(double)totalEgresos)*100;
    	section.addTableTextCellAligned("reporteTable", UtilidadTexto.formatearValores(porcentaje, "0.00"), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    	
    	//Calculo de total estancia causas
    	for(int i=0;i<totalEstanciasDx.length;i++)
    		totalEstanciasCausas += totalEstanciasDx[i];
    	section.addTableTextCellAligned("reporteTable", totalEstanciasCausas+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    	
    	//Cálculo del promedio
    	double promedio = (double)totalEstanciasCausas/(double)totalEgresosCausas;
    	section.addTableTextCellAligned("reporteTable", UtilidadTexto.formatearValores(promedio, "0.00"), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    	
    	//SE DIBUJA SUBTOTAL DE RESTO DE CAUSAS *********************************************************************************
    	section.setTableCellsColSpan(3);
	    report.font.setFontAttributes(0x000000, 8, true, false, false);
	    section.addTableTextCellAligned("reporteTable", "Subtotal resto de causas", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellsColSpan(1);
	    
	    
	    //Iteración de los rangos x diagnostico
	    contRango = 0;
    	for(HashMap<String, Object> elemento:rangos)
    	{
    		totalEgresosRango[contRango] = obtenerTotalEgresosRango(datosReporte, elemento.get("codigoReporte").toString());
    		int totalEgresosResto = totalEgresosRango[contRango] - totalEgresosCausasRango[contRango];
    		section.addTableTextCellAligned("reporteTable", totalEgresosResto+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    		contRango ++;
    	}
    	
    	//Calculo del total egresos resto
    	section.addTableTextCellAligned("reporteTable", (totalEgresos-totalEgresosCausas)+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    	
    	//Calculo del procentaje resto
    	porcentaje = ((double)(totalEgresos-totalEgresosCausas)/(double)totalEgresos)*100;
    	section.addTableTextCellAligned("reporteTable", UtilidadTexto.formatearValores(porcentaje, "0.00"), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    	
    	//Calculo de total estancia resto
    	section.addTableTextCellAligned("reporteTable", (totalEstancia-totalEstanciasCausas)+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    	
    	//Calculo del promedio estancia resto
    	promedio = ((double)(totalEstancia-totalEstanciasCausas))/((double)(totalEgresos-totalEgresosCausas));
    	section.addTableTextCellAligned("reporteTable", UtilidadTexto.formatearValores(promedio, "0.00"), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    	
    	
    	//SE DIBUJA TOTALES **************************************************************************************************
    	section.setTableCellsColSpan(3);
	    report.font.setFontAttributes(0x000000, 8, true, false, false);
	    section.addTableTextCellAligned("reporteTable", "Total", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellsColSpan(1);
	    
	    for(int i=0;i<totalEgresosRango.length;i++)
	    	section.addTableTextCellAligned("reporteTable", totalEgresosRango[i]+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	    //Se dibuja total egresos
	    section.addTableTextCellAligned("reporteTable", totalEgresos+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.addTableTextCellAligned("reporteTable", "-", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    //Se dibuja total estancia
	    section.addTableTextCellAligned("reporteTable", totalEstancia+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	    //Se dibuja promedio estancia
	    promedio = ((double)totalEstancia)/((double)totalEgresos);
	    section.addTableTextCellAligned("reporteTable", UtilidadTexto.formatearValores(promedio, "0.00"), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
    	
	    
	    ////se añade la sección al documento
	    report.addSectionToDocument("reporte");
	    
	    
	    //********************************************************************************************************************
	    report.closeReport();
	    
	    //*******************************************************************************************************************
    }
    
    /**
     * Método para obtener el total de egresos por diagnostico y rango
     * @param datos
     * @param codigoDx
     * @param codigoReporte
     * @return
     */
    private static int obtenerTotalEgresosDXRango(HashMap<String, Object> datos,String codigoDx,String codigoReporte)
    {
    	int total = 0;
    	
    	for(int i=0;i<Integer.parseInt(datos.get("numRegistros").toString());i++)
    		if(datos.get("codigoReporte_"+i).toString().equals(codigoReporte)&&datos.get("codigoDiagnostico_"+i).toString().equals(codigoDx))
    			total += Integer.parseInt(datos.get("egreso_"+i).toString());
    	
    	return total;
    }
    
    /**
     * Método para obtener el total de estancia por diagnostico
     * @param datos
     * @param codigoDx
     * @return
     */
    private static int obtenerTotalEstanciaDx(HashMap<String, Object> datos,String codigoDx)
    {
    	int total = 0;
    	
    	for(int i=0;i<Integer.parseInt(datos.get("numRegistros").toString());i++)
    		if(datos.get("codigoDiagnostico_"+i).toString().equals(codigoDx))
    			total += Integer.parseInt(datos.get("estancia_"+i).toString());
    	
    	return total;
    }
    
    /**
     * Método para obtener el total de egresos de un rango de edad específico
     * @param datos
     * @param codigoReporte
     * @return
     */
    private static int obtenerTotalEgresosRango(HashMap<String, Object> datos,String codigoReporte)
    {
    	int total = 0;
    	
    	for(int i=0;i<Integer.parseInt(datos.get("numRegistros").toString());i++)
    		if(datos.get("codigoReporte_"+i).toString().equals(codigoReporte))
    			total += Integer.parseInt(datos.get("egreso_"+i).toString());
    	
    	return total;
    }
    
    /**
     * Método para realizar la impresión del reporte de resumen mensual egresos
     * @param filename
     * @param forma
     * @param usuario
     */
    public static String csvDxEgresosRangoEdad(Connection con,String pathArchivo,String nombreArchivo, ReporteEgresosEstanciasForm forma,UsuarioBasico usuario, HttpServletRequest request)
    {
    	String mensaje = "";
    	///*****************SECCION DETALLE***********************************************************************************
	    int numRows = 0, numCols = 0, ordenRango = 0, totalEgresos = 0, totalEstancia = 0;
	    ArrayList<HashMap<String,Object>> rangos = new ArrayList<HashMap<String,Object>>();
	    ArrayList<HashMap<String,Object>> diagnosticos = new ArrayList<HashMap<String,Object>>();
	    
	    //Se consultan los datos del reporte
	    HashMap<String, Object> datosReporte = ReporteEgresosEstancias.obtenerDatosReporteDxEgresoRangoEdad(con, forma.getCodigoCentroAtencion(), forma.getFechaInicial(), forma.getFechaFinal(), forma.getCodigoViaIngreso(), forma.getCodigoTipoPaciente(), forma.getEstadoFacturacion(), forma.getCodigoSexo(), forma.getCentrosCosto(), forma.getConvenios(), usuario.getCodigoInstitucionInt());
	    //Se consultan los diagnósticos prioritarios
	    diagnosticos = ReporteEgresosEstancias.obtenerDiagnosticosPrioridadEgresos(con, forma.getCodigoCentroAtencion(), forma.getFechaInicial(), forma.getFechaFinal(), forma.getCodigoViaIngreso(), forma.getCodigoTipoPaciente(), Integer.parseInt(forma.getPrioridad()), forma.getEstadoFacturacion(), forma.getCodigoSexo(), forma.getCentrosCosto(), forma.getConvenios());
	    
	    //Se toman los rangos del reporte
	    for(int i=0;i<Integer.parseInt(datosReporte.get("numRegistros").toString());i++)
	    {
	    	if(ordenRango!=Integer.parseInt(datosReporte.get("orden_"+i).toString()))
	    	{
	    		numRows ++;
	    		ordenRango = Integer.parseInt(datosReporte.get("orden_"+i).toString());
	    		//Se agrega el elemento del rango al arreglo de rangos
	    		HashMap<String, Object> elemento = new HashMap<String, Object>();
	    		elemento.put("orden",datosReporte.get("orden_"+i));
	    		elemento.put("nombreEtiqueta",datosReporte.get("nombreEtiqueta_"+i));
	    		elemento.put("codigoReporte",datosReporte.get("codigoReporte_"+i));
	    		rangos.add(elemento);
	    	}
	    	totalEgresos += Integer.parseInt(datosReporte.get("egreso_"+i).toString());
	    	totalEstancia += Integer.parseInt(datosReporte.get("estancia_"+i).toString());
	    }
	    //Calculo de número de filas y columnas
	    numCols  = 3 /**columnas de diagnostico**/ + rangos.size() + 4 /**columnas de totales**/;
	    numRows = 2 /**filas de encabezados**/ + diagnosticos.size() + 3 /**filas de totales**/;
	    
	    try
	    {
		    //**************INICIALIZACIÓN DEL ARCHIVO CSV***************************************************************************************
		    File archivoCSV=new File(pathArchivo,nombreArchivo.replaceAll("zip", "csv"));
			FileWriter streamCSV = new FileWriter(archivoCSV,false);
			BufferedWriter bufferCSV=new BufferedWriter(streamCSV);
		    //***********************************************************************************************************************************
		    
		    
		    //*****************SE GENERA LA TABLA DEL REPORTE*************************************************************************************
			logger.info("numero de filas: "+numRows);
			logger.info("numero de columnas: "+numCols);
			
		
		    
		    //SE DIBUJAN LOS TITULOS DE LAS COLUMNAS ********************************************************************
			bufferCSV.write("#,Código,Diagnóstico,Rangos Edad");
			//Relleno de columnas segun columnas de rangos
			for(int i=1;i<rangos.size();i++)
				bufferCSV.write(",");
			bufferCSV.write(",Total egresos,%,Total estancia,Promedio estancia\n");
			bufferCSV.write(",,");
			for(HashMap<String, Object> elemento:rangos)
				bufferCSV.write(","+elemento.get("nombreEtiqueta"));
			bufferCSV.write(",,,,\n");
			
		    //SE DIBUJAN LOS DATOS DE LOS DIAGNÓSTICOS QUE ABARCAN LA PRIORIDAD**************************************************
		    int contDx = 0, contRango = 0, sumEgresosRango = 0, totalEgresosCausas = 0, totalEstanciasCausas = 0;
		    int[][] totalEgresosMatriz = new int[diagnosticos.size()][rangos.size()];
		    int[] totalEgresosDx = new int[diagnosticos.size()];
		    int[] totalEstanciasDx = new int[diagnosticos.size()];
		    int[] totalEgresosCausasRango = new int[rangos.size()];
		    int[] totalEgresosRango = new int[rangos.size()];
		    
		    
		    
		    for(HashMap<String, Object> elementoD:diagnosticos)
		    {
		    	contRango = 0;
		    	sumEgresosRango = 0; //variable para sumar todos los egresos de un Dx en todos los rangos
		    	
		    	bufferCSV.write((contDx+1)+"");
		    	bufferCSV.write(","+elementoD.get("codigo").toString());
		    	bufferCSV.write(","+elementoD.get("nombre").toString().replaceAll(",", ""));
		    	
		    	//Iteración de los rangos x diagnostico
		    	for(HashMap<String, Object> elementoR:rangos)
		    	{
		    		totalEgresosMatriz[contDx][contRango] = obtenerTotalEgresosDXRango(datosReporte,elementoD.get("codigo").toString() , elementoR.get("codigoReporte").toString());
		    		sumEgresosRango += totalEgresosMatriz[contDx][contRango];
		    		bufferCSV.write(","+totalEgresosMatriz[contDx][contRango]);
		    		
		    		contRango ++;
		    	}
		    	
		    	//Se muestra el total de egresos x diagnostico
		    	totalEgresosDx[contDx] = sumEgresosRango;
		    	bufferCSV.write(","+totalEgresosDx[contDx]);
		    	
		    	//Se calcula el % de egresos por diagnostico
		    	double porcentaje = ((double)sumEgresosRango/(double)totalEgresos)*100;
		    	bufferCSV.write(","+UtilidadTexto.formatearValores(porcentaje,"0.00"));
		    	
		    	//Se calcula el total de estancia por diagnóstico
		    	totalEstanciasDx[contDx] = obtenerTotalEstanciaDx(datosReporte, elementoD.get("codigo").toString() );
		    	bufferCSV.write(","+totalEstanciasDx[contDx]);
		    	
		    	
		    	//Se calcula el promedio estancia
		    	double promedio = (double)totalEstanciasDx[contDx] / (double)sumEgresosRango;
		    	bufferCSV.write(","+UtilidadTexto.formatearValores(promedio,"0.00")+"\n");
		    	
		    	contDx++;
		    }
		    
		    //SE DIBUJA EL SUBTOTAL DE PRIMERA 'P' CAUSAS********************************************************************
		    bufferCSV.write(",,Subtotal primeras 'P' causas");
		    
		    //Iteración de los rangos x diagnostico
	    	for(int i=0;i<rangos.size();i++)
	    	{
	    		int sumEgresosDx = 0;
	    		for(int j=0;j<diagnosticos.size();j++)
	    			sumEgresosDx += totalEgresosMatriz[j][i];
	    		totalEgresosCausasRango[i] = sumEgresosDx;
	    		bufferCSV.write(","+totalEgresosCausasRango[i]);
	    		
	    	
	    	}	
	    	
	    	//Calculo de total egresos causas
	    	for(int i=0;i<totalEgresosDx.length;i++)
	    		totalEgresosCausas += totalEgresosDx[i];
	    	bufferCSV.write(","+totalEgresosCausas);
	    	
	    	
	    	//Cálculo del porcentaje
	    	double porcentaje = ((double)totalEgresosCausas/(double)totalEgresos)*100;
	    	bufferCSV.write(","+UtilidadTexto.formatearValores(porcentaje, "0.00"));
	    	
	    	//Calculo de total estancia causas
	    	for(int i=0;i<totalEstanciasDx.length;i++)
	    		totalEstanciasCausas += totalEstanciasDx[i];
	    	bufferCSV.write(","+totalEstanciasCausas);
	    	
	    	
	    	//Cálculo del promedio
	    	double promedio = (double)totalEstanciasCausas/(double)totalEgresosCausas;
	    	bufferCSV.write(","+UtilidadTexto.formatearValores(promedio, "0.00")+"\n");
	    	
	    	//SE DIBUJA SUBTOTAL DE RESTO DE CAUSAS *********************************************************************************
	    	bufferCSV.write(",,Subtotal resto de causas");
	    	
		    
		    //Iteración de los rangos x diagnostico
		    contRango = 0;
	    	for(HashMap<String, Object> elemento:rangos)
	    	{
	    		totalEgresosRango[contRango] = obtenerTotalEgresosRango(datosReporte, elemento.get("codigoReporte").toString());
	    		int totalEgresosResto = totalEgresosRango[contRango] - totalEgresosCausasRango[contRango];
	    		bufferCSV.write(","+totalEgresosResto);
	    		
	    		contRango ++;
	    	}
	    	
	    	//Calculo del total egresos resto
	    	bufferCSV.write(","+(totalEgresos-totalEgresosCausas));
	    	
	    	//Calculo del procentaje resto
	    	porcentaje = ((double)(totalEgresos-totalEgresosCausas)/(double)totalEgresos)*100;
	    	bufferCSV.write(","+UtilidadTexto.formatearValores(porcentaje, "0.00"));
	    	
	    	//Calculo de total estancia resto
	    	bufferCSV.write(","+(totalEstancia-totalEstanciasCausas));
	    	
	    	
	    	//Calculo del promedio estancia resto
	    	promedio = ((double)(totalEstancia-totalEstanciasCausas))/((double)(totalEgresos-totalEgresosCausas));
	    	bufferCSV.write(","+UtilidadTexto.formatearValores(promedio, "0.00")+"\n");
	    	
	    	
	    	
	    	//SE DIBUJA TOTALES **************************************************************************************************
	    	bufferCSV.write(",,Total");
	    	
		    for(int i=0;i<totalEgresosRango.length;i++)
		    	bufferCSV.write(","+totalEgresosRango[i]);
		    	
		    //Se dibuja total egresos
		    bufferCSV.write(","+totalEgresos);
		    bufferCSV.write(",");
		    bufferCSV.write(","+totalEstancia);
		    promedio = ((double)totalEstancia)/((double)totalEgresos);
		    bufferCSV.write(","+UtilidadTexto.formatearValores(promedio, "0.00")+"\n");
		    
		    
		    
		    //********************************************************************************************************************
		   //Se finaliza el archivo CSV
		    bufferCSV.close();
		    
		    //se genera el archivo en formato Zip
			BackUpBaseDatos.EjecutarComandoSO("zip -j "+pathArchivo+nombreArchivo+" "+pathArchivo+nombreArchivo.replaceAll("zip", "csv"));
			
			if(!UtilidadFileUpload.existeArchivo(pathArchivo, nombreArchivo))
				mensaje = "No se pudo comprimir el archivo. Por favor reportar el error con el administrador del sistema";
		    
		    //*******************************************************************************************************************
			
	    }
	    catch(IOException e)
	    {
	    	logger.error("Error tratando de generar el archivo CSV: "+nombreArchivo+" (Se cancela proceso) :"+e);
	    }
	    return mensaje;
    }
}
