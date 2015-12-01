package com.princetonsa.pdf;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.UtilidadBD;
import util.UtilidadN2T;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseFont;

import com.lowagie.text.BadElementException;
import com.princetonsa.actionform.ordenesmedicas.OrdenesAmbulatoriasForm;
import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.ordenesmedicas.OrdenesAmbulatorias;

public class OrdenesAmbulatoriasArticulosPdf  
{
	  /**
     * Clase para manejar los logs de esta clase
     */
    private static Logger ordenesPdfLogger=Logger.getLogger(OrdenesAmbulatoriasArticulosPdf.class);
    
    /**
     * Método que imprime 
     * 
	 * @param filename Nombre del archivo con el que
	 * se desea generar el PDF
	 * @param diagnosticosForm Form del que se sacan
	 * las colecciones
     * @param medico Médico que está generando el PDF
     * @param institucionActual 
     * @param tipoDocumento 
     */
	public  static void pdf(Connection con, String filename, OrdenesAmbulatoriasForm forma, UsuarioBasico medico, PersonaBasica paciente, InstitucionBasica institucionActual, String tipoDocumento) 
	{
		PdfReports report = new PdfReports("true", false, medico.getLoginUsuario());
		
		String[] reportHeader = {
			"Código", 
			"Descripción", 
			"Dosis",
			"Frecuencia",
			"Vía",
			"Días Trat.",
			"Cantidad",
			"Indicaciones"
		};
		  
		String[] reportData=getMedicamentosInsumos(forma, "true");
		
		String filePath=ValoresPorDefecto.getFilePath();
		String titulo="PRESCRIPCIÓN AMBULATORIA DE MEDICAMENTOS";
		
		InstitucionBasica institucionBasica= new InstitucionBasica();
        if(Utilidades.convertirAEntero(paciente.getCodigoConvenio()+"")>0)
        	institucionBasica.cargarXConvenio(medico.getCodigoInstitucionInt(), paciente.getCodigoConvenio());
        else
        	institucionBasica = institucionActual;
        
        //generacion de la cabecera, validando el tipo de separador e utilizado en el path
        report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), titulo);
        
		report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
		report.openReport(filename);
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000, 12);
		report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 10);
		
		report.font.setFontSizeAndAttributes(9, false, false, false);
		report.document.addParagraph("Paciente: "+paciente.getApellidosNombresPersona(false)+" "+paciente.getCodigoTipoIdentificacionPersona()+" "+paciente.getNumeroIdentificacionPersona()+ ", No Ingreso: "+Utilidades.obtenerConsecutivoIngreso(con, forma.getIdIngreso()+"")+", No Cuenta: "+forma.getCuentaSolicitante(), report.font, 20);
		
		Cuenta cuenta= new Cuenta();
		
		cuenta.cargarCuenta(con, forma.getCuentaSolicitante()+"");
		String nombreEspecialidad=Utilidades.getNombreEspecialidad(con, Integer.parseInt(forma.getEspecialidad()));
		String centroAtencion="";
		
		HashMap infoCuentaSolicitante = Utilidades.obtenerInfoCuentaSolicitanteOrdenAmbulatoria(con, forma.getCodigoOrden());
		try
		{
			centroAtencion=Utilidades.obtenerNombreCentroAtencion(con, Integer.parseInt(forma.getCentroAtencion()));
		}
		catch(Exception e)
		{}
		
		String regimen="";
		regimen=cuenta.getTipoRegimen();
		try
		{
			if(regimen==null || regimen.equals("null") || regimen.equals(""))
			{
				regimen="";
				//se evalua si es capitado, si true entonces debe mostrar el régimen del contrato por el que estan capitados
				regimen=Utilidades.obtenerTipoRegimenSolicitudUsuarioCapitado(con, paciente.getCodigoPersona()+"", forma.getFechaOrden(), medico.getCodigoInstitucionInt()).getNombre();
			}
		}
		catch (Exception e) 
		{
			regimen="";
		}
		
		
		String convenio="", tipoAfiliado="", categoria="";
		if(infoCuentaSolicitante.containsKey("convenio_0"))
			convenio = infoCuentaSolicitante.get("convenio_0").toString();
		if(infoCuentaSolicitante.containsKey("tipoafiliado_0"))
			tipoAfiliado = infoCuentaSolicitante.get("tipoafiliado_0").toString();
		if(infoCuentaSolicitante.containsKey("clasificacionsocioeconomica_0"))
			categoria = infoCuentaSolicitante.get("clasificacionsocioeconomica_0").toString();
		
		report.document.addParagraph("Convenio: "+convenio, report.font, 10);
		report.document.addParagraph("Tipo de Afiliado: "+tipoAfiliado+" Categoría: "+categoria, report.font, 10);
		report.document.addParagraph("Edad: "+paciente.getEdadDetallada() +", Nro. Historia Clínica: "+paciente.getCodigoTipoIdentificacionPersona()+" - "+paciente.getNumeroIdentificacionPersona()+", Tipo de Usuario: "+regimen, report.font, 10);
		report.document.addParagraph("Nro. Orden: "+forma.getNumeroOrden()+", Fecha/Hora: "+forma.getFechaOrden()+"/"+forma.getHora(), report.font, 10);
		report.document.addParagraph("Lugar de Prescripción: "+centroAtencion, report.font, 10);
		report.document.addParagraph("Profesional que Pre-escribe: "+medico.getNombreUsuario()+" , Especialidad: "+nombreEspecialidad, report.font, 10);
		
		DtoDiagnostico diagnosticoOrden= OrdenesAmbulatorias.consultarDiagnosticoOrden(con,forma.getCodigoOrden());
        	ordenesPdfLogger.info("codigo ingreso" + paciente.getCodigoIngreso());
		if(!diagnosticoOrden.getAcronimoDiagnostico().isEmpty())
			report.document.addParagraph("Diagnóstico: CIE-"+diagnosticoOrden.getTipoCieDiagnostico()+" "+diagnosticoOrden.getAcronimoDiagnostico(), report.font, 14);

		//Se realiza modificación por V1.5 del CU 65
		if(diagnosticoOrden!=null){
			forma.setAcronimoDiagnostico(diagnosticoOrden.getAcronimoDiagnostico());
			forma.setTipoCieDiagnostico(diagnosticoOrden.getTipoCieDiagnostico());
		}
		
		/*MEDICAMENTOS*/
		if(!forma.getArticulos("numRegistros").toString().equals("0"))
		{		
			report.font.setFontSizeAndAttributes(10, true, false, false);
			report.document.addParagraph("Medicamentos", report.font, 20);
			report.createColSection("seccionMedicamentos", reportHeader, reportData, 11, 9, 10);
			//Modificado por la tarea 30205. Por el tamaño de los campos
			//report.createColSection("seccionMedicamentos", "Medicamentos", reportHeader, reportData, 10);
			float widths[]={(float) 0.8,(float) 2.9,(float) 0.7,(float) 1.2,(float) 1,(float) 1,(float) 1, (float) 1.5};
			try
	        {
			    report.getSectionReference("seccionMedicamentos").getTableReference("parentTable").setWidths(widths);
	        }
	        catch (BadElementException e)
	        {
	            ordenesPdfLogger.info("Se presentó error generando el pdf " + e);
	        }
			
			report.addSectionToDocument("seccionMedicamentos");
		}
		
		/*INSUMOS*/
		String [] reportHeaderInsumos = {
				"Código", 
				"Descripción", 
				"Cantidad"
			};
		
		String[] reportDataInsumos=getMedicamentosInsumos(forma, "false");
		
		if(reportDataInsumos.length>0)
		{	
			report.font.setFontSizeAndAttributes(12, true, false, false);
			report.document.addParagraph("Insumos", report.font, 10);
			report.createColSection("seccionInsumos", reportHeaderInsumos, reportDataInsumos, 11, 9, 10);
			//Modificado por la tarea 30205. Por el tamaño de los campos
			//report.createColSection("seccionInsumos", "Insumos", reportHeaderInsumos, reportDataInsumos, 10);
			float widthsInsumos[]={(float) 2,(float) 6,(float) 2};
			try
	        {
			    report.getSectionReference("seccionInsumos").getTableReference("parentTable").setWidths(widthsInsumos);
	        }
	        catch (BadElementException e)
	        {
	            ordenesPdfLogger.info("Se presentó error generando el pdf " + e);
	        }
			report.addSectionToDocument("seccionInsumos");
		}	
		
		report.font.setFontSizeAndAttributes(9, false, false, false);
		if(!forma.getOtros().equals(""))
			report.document.addParagraph("Otros Medicamentos e Insumos: "+forma.getOtros(), report.font, 20);
		
		report.document.addParagraph("Indicaciones Generales: "+forma.getObservaciones(), report.font, 20);
		
		report.document.addParagraph("                ", report.font, 30);
		
		ordenesPdfLogger.info("\nFimaDig->"+medico.getFirmaDigital()+" path->"+medico.getPathFirmaDigital()+"\n");
		if(!UtilidadTexto.isEmpty(medico.getFirmaDigital()))
		{	
			report.document.addImage(medico.getPathFirmaDigital());
			report.document.addParagraph(medico.getNombreRegistroMedico()+" "+medico.getEspecialidadesMedico(), report.font, 10);
			report.document.addParagraph(institucionBasica.getPieHistoriaClinica(), report.font, 10);
		}	
		else
		{
			report.document.addParagraph("_______________________________________________________", report.font, 30);
			report.document.addParagraph(medico.getNombreRegistroMedico()+" "+medico.getEspecialidadesMedico(), report.font, 10);
		}
		
		report.document.addParagraph("Profesional que Pre-escribe: "+medico.getNombreRegistroMedico()+" "+medico.getEspecialidadesMedico(), report.font, 18);
		
		report.document.addParagraph("                                                                           ", report.font, 20);
		report.document.addParagraph("Esta Pre-escripción es válida por 72 Horas.", report.font, 10);
		report.document.addParagraph("                                                                           ", report.font, 10);
		report.font.setFontSizeAndAttributes(8, false, true, false);
		/*report.document.addParagraph(	"'Los prestadores de servicios de salud pueden utilizar medios físicos o técnicos como computadoras " +
										"y medios magneto-ópticos permitiendo la identificación del personal responsable de los datos consignados, " +
										"mediante códigos, indicadores u otros medios que REEMPLACEN LA FIRMA Y SELLO DE LAS HISTORIAS EN MEDIOS FÍSICOS, " +
										"de forma que se establezca con exactitud quien realizó los registros, la hora y fecha del registro. " +
										"(Artículo 18, resolución 1995 de 1999).'", report.font, 10);
		*/
		report.document.addParagraph(institucionActual.getPieHistoriaClinica(), report.font, 10);
		
		report.closeReport(tipoDocumento);
		
	}
	
	/**
	 * 
	 * @param forma
	 * @return
	 */
	private static String[] getMedicamentosInsumos(OrdenesAmbulatoriasForm forma, String esMedicamentosBool)
	{
		ordenesPdfLogger.info("Voy a cargar los mediacamentos");
		int numMed=0;
		int numRegistros=Integer.parseInt(forma.getArticulos("numRegistros").toString());
		ordenesPdfLogger.info("numRegistros medicamentos "+numRegistros);
		boolean esMedicamentos=UtilidadTexto.getBoolean(esMedicamentosBool);
		for(int w=0; w<numRegistros; w++)
		{
			if(UtilidadTexto.getBoolean(forma.getArticulos("medicamento_"+w).toString())==esMedicamentos)
			{
				numMed++;
			}
		}
		int numCol=0;
		if(esMedicamentos)
			numCol=8;
		else
			numCol=3;
			
		String[] medicamentos=new String[numMed*numCol];
		int pos=0;
		for(int w=0; w<Integer.parseInt(forma.getArticulos("numRegistros").toString()); w++)
		{
			if(esMedicamentos)
			{	
				if(UtilidadTexto.getBoolean(forma.getArticulos("medicamento_"+w).toString())==esMedicamentos)
				{
					medicamentos[pos]=forma.getArticulos("articulo_"+w).toString();
					pos++;
					medicamentos[pos]=  forma.getArticulos("descarticulo_"+w).toString();
					pos++;
					medicamentos[pos]=  forma.getArticulos("dosis_"+w).toString()+" "+forma.getArticulos("nomunidosis_"+w).toString();;
					pos++;
					medicamentos[pos]=  "Cada "+forma.getArticulos("frecuencia_"+w).toString()+ " "+forma.getArticulos("nomtipofrecuencia_"+w).toString();
					pos++;
					medicamentos[pos]=  forma.getArticulos("nomvia_"+w).toString();
					pos++;
					medicamentos[pos]=  forma.getArticulos("duraciontratamiento_"+w).toString();
					pos++;
					medicamentos[pos]=  forma.getArticulos("cantidad_"+w).toString()+ " ("+ UtilidadN2T.convertirLetras(forma.getArticulos("cantidad_"+w).toString(), "", "")+")";
					pos++;
					medicamentos[pos]=  forma.getArticulos("observaciones_"+w).toString();
					pos++;
				}
			}
			else
			{
				if(UtilidadTexto.getBoolean(forma.getArticulos("medicamento_"+w).toString())==esMedicamentos)
				{
					medicamentos[pos]=forma.getArticulos("articulo_"+w).toString();
					pos++;
					medicamentos[pos]=  forma.getArticulos("descarticulo_"+w).toString();
					pos++;
					medicamentos[pos]=  forma.getArticulos("cantidad_"+w).toString()+ " ("+ UtilidadN2T.convertirLetras(forma.getArticulos("cantidad_"+w).toString(), "", "")+")";
					pos++;
				}
			}
		}
		return medicamentos;
	}
	
}
