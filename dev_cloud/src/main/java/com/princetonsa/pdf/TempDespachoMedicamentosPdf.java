/*
 * @(#)DespachoMedicamentosPdf.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */
package com.princetonsa.pdf;

import org.apache.log4j.Logger;

import util.UtilidadFecha;
import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;

import com.lowagie.text.BadElementException;
import com.princetonsa.actionform.medicamentos.DespachoMedicamentosForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;


/**
 * Clase para manejar la generación de PDF's para
 * el despacho de medicamentos
 * 
 * @author wrios
 * @version 1.0, 13/09/2004
 */
public class TempDespachoMedicamentosPdf {

	private static Logger logger=Logger.getLogger(DespachoMedicamentosPdf.class);
	/**
	 * @param string
	 * @param despachoForm
	 * @param usuario
	 */
	public static void pdfDespachoMedicamentos(String filename, DespachoMedicamentosForm forma, UsuarioBasico usuario, PersonaBasica paciente) {
		
		PdfReports report = new PdfReports();
		
		String[] datosDetalle={"Fecha de Solicitud: "+forma.getFechaSolicitud(),"Hora de Solicitud: "+forma.getHoraSoliciutd(),"Numero de Solicitud: "+forma.getNumeroSolicitud(),"Estado de la Solicitud: "+forma.getEstadoMedico(),"Profesional que solicitó: "+forma.getMedicoSolicitante()/*,"Numero de Autorizacion: Q"+forma.getNumeroAutorizacion()*/};
		String[] datosMedicamentos;
		String[] dbColumnsMedicamentos={"codigo","descripcion","concentracion","formafarmaceutica","unidadmedida","dosis","frecuencia","via","despacho_total"};
		String[] datosMedicamentosBien=new String[forma.getCol().size()*3];
		String[] headMediamentos={"Articulo","Formulación","Despacho Total"};
        String[] datosInsumos=new String[forma.getNumeroIngresos()*3];
        String[] headInsumos={"Articulo","Unidad de Medida","Despacho Total"};
        int posInsumos=0;//posicion a imprimir para insumos
        int pos=0;//Lleva las posiciones del arreglo que se imprimira para medicamentos.
		String filePath=ValoresPorDefecto.getFilePath();
		String tituloMedicamentos="CONSULTA SOLICITUD DE MEDICAMENTOS (" + UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() + ")";
		
		InstitucionBasica institucionBasica= new InstitucionBasica();
        institucionBasica.cargarXConvenio(usuario.getCodigoInstitucionInt(), paciente.getCodigoConvenio());
        
        //generacion de la cabecera, validando el tipo de separador e utilizado en el path
        report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), tituloMedicamentos);
		
		try
		{
			report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000, 14);
			report.setReportDataSectionAtributes(0x000000, 0xFFFFFF,0x000000, 12);
			
			
			//SECCION DETALLES
			report.openReport(filename);
			report.createSection("seccionDetalle","tablaDetalle",3,3,2);
			report.addSectionTitle("seccionDetalle","tablaDetalle","DETALLE DESPACHO MEDICAMENTOS");
			report.getSectionReference("seccionDetalle").setTableCellsColSpan(1);
			report.addSectionColData("seccionDetalle","tablaDetalle",datosDetalle,false);
			
			//SECCION
			datosMedicamentos=report.getDataFromCollection(dbColumnsMedicamentos,forma.getCol());
			
			for(int k=0;k<datosMedicamentos.length;k++)
			{
				datosMedicamentosBien[pos]=datosMedicamentos[k]+"-"+datosMedicamentos[k+1]+"-"+datosMedicamentos[k+2]+"-"+datosMedicamentos[k+3]+"-"+datosMedicamentos[k+4];
				datosMedicamentosBien[pos+1]= "Dósis: "+datosMedicamentos[k+5]+"	Frecuencia: "+datosMedicamentos[k+6]+"	Via: "+datosMedicamentos[k+7];
				datosMedicamentosBien[pos+2]=datosMedicamentos[k+8];
				pos+=3;//siguiente posicion
				k+=8;//ultima posicion
			}
			report.createColSection("seccionMedicamentos", "MEDICAMENTOS",headMediamentos,datosMedicamentosBien, 10);
			float widths[]={(float) 4,(float) 4,(float) 2};
			try
	        {
			    report.getSectionReference("seccionMedicamentos").getTableReference("parentTable").setWidths(widths);
	        }
	        catch (BadElementException e)
	        {
	            logger.error("Se presentó error generando el pdf " + e);
	        }
			
	        //seccion insumos
	        for(int i=0;i<forma.getNumeroIngresos();i++)
	        {
	        	datosInsumos[posInsumos]=forma.getDespachoMap("articulo_"+i)+"";
	        	datosInsumos[posInsumos+1]=forma.getDespachoMap("unidadMedida_"+i)+"";
	        	datosInsumos[posInsumos+2]=forma.getDespachoMap("despachoTotal_"+i)+"";
	        	posInsumos+=3;
	        }
	        report.createColSection("seccionInsumos","INSUMOS",headInsumos,datosInsumos,10);
	        
			//add las secciones al documentos
			report.addSectionToDocument("seccionDetalle");
			report.addSectionToDocument("seccionMedicamentos");
			report.addSectionToDocument("seccionInsumos");
	        
	        //seccion observaciones
	        if(!forma.getObservacionesGenerales().equals(""))
	        {
	        	report.font.setFontSizeAndAttributes(16,true,false,false);
				report.document.addParagraph("OBSERVACIONES GENERALES",report.font,iTextBaseDocument.ALIGNMENT_CENTER,30);
				report.font.setFontSizeAndAttributes(12,false,false,false);
				report.document.addParagraph("    ",report.font,15);
				report.document.addParagraph(forma.getObservacionesGenerales(),report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,10);
	        }
	        report.document.addParagraph("    ",report.font,15);
	        report.font.setFontSizeAndAttributes(10,true,true,false);
	        report.document.addParagraph(" ",report.font,10);
	        report.document.addParagraph(usuario.getNombreUsuario() + " " + usuario.getNumeroRegistroMedico()+" "+usuario.getEspecialidadesMedico(),report.font,10);
	        report.document.addParagraph(" ",report.font,10);
	        if(forma.getEsDirecto())
	        {
	        	report.document.addParagraph("Entrega directa al paciente.",report.font,10);
	        	report.document.addParagraph(" ",report.font,10);
	        }
	        if(forma.getRadioPendiente().equals("on"))
	        {
	            
	        	report.document.addParagraph("DESPACHO PENDIENTE",report.font,10);
		        report.document.addParagraph(" ",report.font,10);
	        }
	        else
	        {
	        	report.document.addParagraph("DESPACHO FINAL",report.font,10);
		        report.document.addParagraph(" ",report.font,10);
	        }
	        
	        report.document.addParagraph(usuario.getNombreUsuario()+", "+UtilidadFecha.getFechaActual() + "  "+UtilidadFecha.getHoraActual(),report.font,10);
			report.closeReport();
		}
		catch(Exception e){
				logger.error("Se presentó error generando el pdf" + e);
				e.getStackTrace();
			}
	
		
	}

}
