package com.princetonsa.pdf;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseFont;

import com.lowagie.text.BadElementException;
import com.princetonsa.actionform.ordenesmedicas.OrdenesAmbulatoriasForm;
import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.ordenesmedicas.OrdenesAmbulatorias;

public class OrdenesAmbulatoriasServiciosPdf 
{
	  /**
     * Clase para manejar los logs de esta clase
     */
    private static Logger ordenesPdfLogger=Logger.getLogger(OrdenesAmbulatoriasServiciosPdf.class);
    
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
			"Nombre", 
			"Pos?",
			"Finalidad",
			"Cantidad",
			"Urg"
		};
		  
		String[] reportData=getServicios(forma, medico.getCodigoInstitucionInt());
		
		String filePath=ValoresPorDefecto.getFilePath();
		String titulo="ORDEN AMBULATORIA DE SERVICIOS";
		
		InstitucionBasica institucionBasica= new InstitucionBasica();
        if(Utilidades.convertirAEntero(paciente.getCodigoConvenio()+"")>0)
        	institucionBasica.cargarXConvenio(con, medico.getCodigoInstitucionInt(), paciente.getCodigoConvenio());
        else
        	institucionBasica = institucionActual;
        	
        //generacion de la cabecera, validando el tipo de separador e utilizado en el path
        report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), titulo);
        
		report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
		report.openReport(filename);
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000, 12);
		report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 10);
		
		String nombreEspecialidad=Utilidades.getNombreEspecialidad(con, Integer.parseInt(forma.getEspecialidad()));
		HashMap infoCuentaSolicitante = Utilidades.obtenerInfoCuentaSolicitanteOrdenAmbulatoria(con, forma.getCodigoOrden());
		
		String convenio="", tipoAfiliado="", categoria="";
		if(infoCuentaSolicitante.containsKey("convenio_0"))
			convenio = infoCuentaSolicitante.get("convenio_0").toString();
		if(infoCuentaSolicitante.containsKey("tipoafiliado_0"))
			tipoAfiliado = infoCuentaSolicitante.get("tipoafiliado_0").toString();
		if(infoCuentaSolicitante.containsKey("clasificacionsocioeconomica_0"))
			categoria = infoCuentaSolicitante.get("clasificacionsocioeconomica_0").toString();
		
		String ordenes="";
		if(forma.getConsecutivosOrdenesInsertadas1().size()>0)
			ordenes = UtilidadTexto.convertirVectorACodigosSeparadosXComas(forma.getConsecutivosOrdenesInsertadas1(), false);
		else
			ordenes = forma.getNumeroOrden();
		
		report.document.addParagraph("Paciente: "+paciente.getApellidosNombresPersona(false)+", "+paciente.getCodigoTipoIdentificacionPersona()+" "+paciente.getNumeroIdentificacionPersona()+", Edad: "+paciente.getEdad()+", No Ingreso: "+Utilidades.obtenerConsecutivoIngreso(con, forma.getIdIngreso()+"")+", No Cuenta: "+forma.getCuentaSolicitante(), report.font, 40);
		report.document.addParagraph("Convenio: "+convenio, report.font, 14);
		report.document.addParagraph("Tipo de afiliado: "+tipoAfiliado+" Categoria: "+categoria, report.font, 14);
		report.document.addParagraph("Números orden: "+ordenes+" Fecha/hora: "+forma.getFechaOrden()+"/"+forma.getHora(), report.font, 14);
		report.document.addParagraph("Profesional: "+medico.getNombreUsuario()+" , Especialidad: "+nombreEspecialidad, report.font, 14);		
			
		DtoDiagnostico diagnosticoOrden=OrdenesAmbulatorias.consultarDiagnosticoOrden(con,forma.getCodigoOrden());
			ordenesPdfLogger.info("Codigo Paciente: "+paciente.getCodigoPersona());
			ordenesPdfLogger.info("Codigo Ingreso: "+paciente.getCodigoIngreso());			
		if(!diagnosticoOrden.getAcronimoDiagnostico().isEmpty())
			report.document.addParagraph("Diagnóstico:  CIE-"+diagnosticoOrden.getTipoCieDiagnostico()+" "+diagnosticoOrden.getAcronimoDiagnostico(), report.font, 14);

		if(!forma.getServicios("numRegistros").toString().equals("0"))
		{		
			/*servicios*/
			report.createColSection("seccionServicios", "Servicios", reportHeader, reportData, 8);
			float widths[]={(float) 1.2,(float) 4,(float) 0.8,(float) 1.2,(float) 1,(float) 0.8};
			try
	        {
			    report.getSectionReference("seccionServicios").getTableReference("parentTable").setWidths(widths);
	        }
	        catch (BadElementException e)
	        {
	            ordenesPdfLogger.error("Se presentó error generando el pdf " + e);
	        }
			
			report.addSectionToDocument("seccionServicios");
		}
		
		if(!forma.getOtros().equals(""))
			report.document.addParagraph("Otros Servicios: "+forma.getOtros(), report.font, 20);

		report.document.addParagraph("Observaciones: "+forma.getObservaciones(), report.font, 20);
		report.document.addParagraph("Resultado: "+forma.getResultado(), report.font, 20);
		
		ordenesPdfLogger.info("\nFimaDig->"+medico.getFirmaDigital()+" path->"+medico.getPathFirmaDigital()+"    login-->"+medico.getLoginUsuario()+"\n");
		
		report.document.addParagraph("                ", report.font, 30);
		
		if(!UtilidadTexto.isEmpty(medico.getFirmaDigital()))
		{	
			report.document.addImage(medico.getPathFirmaDigital());
			report.document.addParagraph(medico.getNombreRegistroMedico()+" "+medico.getEspecialidadesMedico(), report.font, 12);
			report.document.addParagraph(institucionBasica.getPieHistoriaClinica(), report.font, 12);
		}	
		else
		{
			report.document.addParagraph("_______________________________________________________", report.font, 20);
			report.document.addParagraph(medico.getNombreRegistroMedico()+" "+medico.getEspecialidadesMedico(), report.font, 12);
		}	
		
		report.closeReport(tipoDocumento); 
	}
	
	/**
	 * 
	 * @param forma
	 * @return
	 */
	private static String[] getServicios(OrdenesAmbulatoriasForm forma, int institucion)
	{
		int numeroServicios= Integer.parseInt(forma.getServicios("numRegistros").toString());
		int numServiciosNoEliminados=0;
		for(int w=0; w<numeroServicios; w++)
		{
			if(!UtilidadTexto.getBoolean(forma.getServicios("fueEliminadoServicio_"+w).toString()))
			{
				numServiciosNoEliminados++;
			}
		}
		
		String[] servicios=new String[numServiciosNoEliminados*6];
		int pos=0;
		for(int w=0; w<numeroServicios; w++)
		{
			if(!UtilidadTexto.getBoolean(forma.getServicios("fueEliminadoServicio_"+w).toString()))
			{
				Utilidades.imprimirMapa(forma.getServicios());
				servicios[pos]=forma.getServicios("codigoCups_"+w).toString();
				//servicios[pos]= Servicios.obtenerCodigoTarifarioServicioConDesc ( Utilidades.convertirAEntero(forma.getServicios("codigoCups_"+w).toString().split("-")[1].trim()), Utilidades.convertirAEntero( ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion)));
				pos++;
				
				servicios[pos]=  forma.getServicios("descripcionServicio_"+w).toString();
					
				pos++;
				
				if(UtilidadTexto.getBoolean(forma.getServicios("esPos_"+w).toString()) || forma.getServicios("esPos_"+w).toString().equals("t")
					|| forma.getServicios("esPos_"+w).toString().equals("1") || forma.getServicios("esPos_"+w).toString().equals("POS"))
					servicios[pos]="Si";
				else
					servicios[pos]="No";
				//servicios[pos]=  forma.getServicios("esPos_"+w).toString();
				pos++;
				String finalidad="";
				try
				{
					if (!forma.getServicios("finalidad_"+w).toString().equals("")){
					finalidad=Utilidades.getNombreFinalidadServicio(forma.getServicios("finalidad_"+w).toString());
					if(finalidad.equals("null"))
						finalidad="";
					}
				}
				catch(Exception e)
				{
					finalidad="";
				}
				servicios[pos]=  finalidad;
				pos++;
				servicios[pos]=  forma.getServicios("cantidad_"+w).toString();
				pos++;
				String urgenteStr="";
				if(UtilidadTexto.getBoolean(forma.getServicios("urgente_"+w).toString()))
					urgenteStr="Si";
				else
					urgenteStr="No";
				servicios[pos]=  urgenteStr;
				pos++;
			}
		}
		return servicios;
	}
	
}