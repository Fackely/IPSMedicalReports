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

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.inventarios.UtilidadInventarios;
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
public class DespachoMedicamentosPdf 
{	
    /**
     * Manejo de logs
     */
    private static Logger logger=Logger.getLogger(DespachoMedicamentosPdf.class);
    
    /**
     * Tamanio de la cabecera
     */
    public static final int tamanioCabecera=9;
    
    /**
     * Método ppal
     * @param filename
     * @param forma
     * @param usuario
     * @param paciente
     * @param con
     * @throws SQLException
     */
    public static void pdfDespachoMedicamentos(String filename, DespachoMedicamentosForm forma, UsuarioBasico usuario,PersonaBasica paciente,Connection con) throws SQLException 
    {
        PdfReports report = new PdfReports();
		cargarEmpresaRegimenPaciente(con,paciente);
		
		//GENERALIDADES**********************************************************************************************************
		String tituloMedicamentos="DESPACHO DE MEDICAMENTOS (" + UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() + ")";
		String tipoDespacho="";
		
		InstitucionBasica institucionBasica= new InstitucionBasica();
		institucionBasica.cargarXConvenio(usuario.getCodigoInstitucionInt(), paciente.getCodigoConvenio());
		
		report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), tituloMedicamentos);
		
		//FIN GENERALIDADES*****************************************************************************************************
		
		
		//GENERAR CABECERA DEL DOCUMENTO*****************************************************************************
		if(forma.getEsDirecto())
            tipoDespacho= "ENTREGA DIRECTA PACIENTE - ";
		if(forma.getRadioPendiente().equals("radioPendiente"))
		    tipoDespacho+="DESPACHO PENDIENTE";
		else
		    tipoDespacho+="DESPACHO FINAL";
		
		String[] datosCabecera={
				"VIA DE INGRESO: "+paciente.getUltimaViaIngreso(),
				"CAMA: "+paciente.getCama(),
				"ÁREA: "+paciente.getArea(),
				"RESPONSABLE: "+paciente.getConvenioPersonaResponsable(),
				//(forma.getNumeroAutorizacion()!=null&&!forma.getNumeroAutorizacion().equals("null"))?"Nº AUTORIZACIÓN: "+forma.getNumeroAutorizacion():" ",
				"RÉGIMEN: "+paciente.getNombreTipoRegimen(),
				"MÉDICO SOLICITANTE: "+forma.getMedicoSolicitante(),
				"FECHA,HORA ORDEN: "+forma.getFechaSolicitud(),
				forma.getHoraSoliciutd(),
				"Nº ORDEN: "+forma.getOrden(),
				"ESTADO: "+forma.getEstadoMedico(),
				tipoDespacho
				};
		
		report.createSection("Cabecera",PdfReports.REPORT_HEADER_TABLE,5,3,2);
		report.getSectionReference("Cabecera").setTableBorder(PdfReports.REPORT_HEADER_TABLE, 0xFFFFFF, 0.0f);
		report.getSectionReference("Cabecera").setTableCellBorderWidth(PdfReports.REPORT_HEADER_TABLE, 0.1f);
		report.getSectionReference("Cabecera").setTableCellsDefaultColors(PdfReports.REPORT_HEADER_TABLE, 0xFFFFFF, 0xFFFFFF);
		report.setReportDataSectionAtributes(0xFFFFFF, 0xFFFFFF, 0x000000, tamanioCabecera);
		
		//paciente
		report.getSectionReference("Cabecera").setTableCellsColSpan(3);
		report.font.setFontSizeAndAttributes(9,true,false,false);
		report.getSectionReference("Cabecera").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, "PACIENTE: "+paciente.getApellidosNombresPersona(false)+"\n"+paciente.getCodigoTipoIdentificacionPersona()+": " +paciente.getNumeroIdentificacionPersona()+"\n"+ "EDAD: " +paciente.getEdadDetallada(), report.font);
//		if(forma.getNombrePersonaRecibe() != null && !forma.getNombrePersonaRecibe().equals(""))
//			report.getSectionReference("Cabecera").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, "PERSONA RECIBE: " + forma.getNombrePersonaRecibe(), report.font);
		report.getSectionReference("Cabecera").setTableCellsColSpan(1);
		
		
		report.getSectionReference("Cabecera").setTableCellsRowSpan(1);
		report.font.setFontSizeAndAttributes(tamanioCabecera,false,false,false);
		for(int k=0; k<datosCabecera.length; k++){	
			//MT2607 se cambia el tamaño de las celdas 
			report.getSectionReference("Cabecera").setTableCellPadding(PdfReports.REPORT_HEADER_TABLE, 0.1F);
			report.getSectionReference("Cabecera").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, datosCabecera[k], report.font);
		}
		float widths[]={(float) 3.7,(float) 2.5,(float) 3.8};
		try
        {
			report.getSectionReference("Cabecera").getTableReference(PdfReports.REPORT_HEADER_TABLE).setWidths(widths);
		}
        catch (Exception e)
        {
        	e.getStackTrace();
        }
        //FIN GENERAR CABECERA*******************************************************************************************************
        
        
		//SECCION TABLE MEDICAMENTOS********************************************************************************************
        
        String[] datosMedicamentos;
        String[] dbColumnsMedicamentos={"codigo", "descripcion","concentracion","formafarmaceutica","unidadmedida","dosis","frecuencia","via","despacho_total","observaciones"};
		datosMedicamentos=report.getDataFromCollection(dbColumnsMedicamentos,forma.getCol());
		String[] datosMedicamentosBien=new String[forma.getCol().size()*11];
		String[] headMediamentos={"ART.","MEDICAMENTO","DOSIS", "FREC.","VÍA","DESP.","D.TOTAL", "LOTE", "F.VENCIMIENTO", "EXIS.LOTE","OBSERV."};
		 int pos=0;//Lleva las posiciones del arreglo que se imprimira para medicamentos.
		int posMap=1;
		 
		for(int k=0;k<datosMedicamentos.length;k++)
		{
			String codigoInterfaz= UtilidadInventarios.obtenerCodigoInterfazArticulo(Utilidades.convertirAEntero(datosMedicamentos[k]));
			codigoInterfaz+=UtilidadTexto.isEmpty(codigoInterfaz)?"":" - ";
			datosMedicamentosBien[pos]=codigoInterfaz+datosMedicamentos[k];
			datosMedicamentosBien[pos+1]=datosMedicamentos[k+1]+", CONC "+datosMedicamentos[k+2]+", F.F. "+datosMedicamentos[k+3]+", UM: "+datosMedicamentos[k+4];
			datosMedicamentosBien[pos+2]= datosMedicamentos[k+5];
			datosMedicamentosBien[pos+3]= datosMedicamentos[k+6];
			datosMedicamentosBien[pos+4]= datosMedicamentos[k+7];
			//+"	Frecuencia: "+datosMedicamentos[k+6]+"	Via: "+datosMedicamentos[k+7];
			datosMedicamentosBien[pos+5]=forma.getDespachoMap("parcial_"+posMap)+"";
			datosMedicamentosBien[pos+6]=datosMedicamentos[k+8];
			
			if(!UtilidadTexto.isEmpty(forma.getDespachoMap("lote_"+posMap)+""))
				datosMedicamentosBien[pos+7]=forma.getDespachoMap("lote_"+posMap)+"";
			else
				datosMedicamentosBien[pos+7]="";
			
			if(!UtilidadTexto.isEmpty(forma.getDespachoMap("fechaVencimientoLote_"+posMap)+""))
				datosMedicamentosBien[pos+8]=forma.getDespachoMap("fechaVencimientoLote_"+posMap)+"";
			else
				datosMedicamentosBien[pos+8]="";
			
			if(!UtilidadTexto.isEmpty(forma.getDespachoMap("fechaVencimientoLote_"+posMap)+""))
				datosMedicamentosBien[pos+9]= (Integer.parseInt(forma.getDespachoMap("existenciaXLote_"+posMap)+"") - Integer.parseInt(forma.getDespachoMap("parcial_"+posMap)+""))+"";
			else
				datosMedicamentosBien[pos+9]="";
			datosMedicamentosBien[pos+10]=datosMedicamentos[k+9];
			pos+=11;//siguiente posicion
			k+=9;//ultima posicion
			posMap++;//pos del map para tomar el valor del valor parcial despachado
		}
		
		report.setReportDataSectionAtributes(0x000000, 0xFFFFFF,0x000000, 9);
		if(datosMedicamentosBien.length>0)
		{
			report.createColSectionSinTitulo("seccionMedicamentos",headMediamentos,datosMedicamentosBien, 10);
			//float widths2[]={(float) 0.7,(float) 4.8,(float) 1.4, (float) 1.4, (float) 1.7};
			float widths1[]={(float) 1,(float) 2.2,(float) 0.8, (float) 0.8, (float) 0.9, (float) 0.6, (float) 0.7, (float) 0.6, (float) 0.6, (float)0.6, (float)1.2};
			try
	        {
			    report.getSectionReference("seccionMedicamentos").getTableReference("parentTable").setWidths(widths1);
	        }
	        catch (BadElementException e)
	        {
	            logger.error("Se presentó error generando el pdf " + e);
	        }
	        //FIN TABLE MEDICAMENTOS*********************************************************************************************
		}
        //SECCION TABLE INSUMOS********************************************************************************************
        
        String[] datosInsumos=new String[forma.getNumeroIngresos()*7];
        int posInsumos=0;//posicion a imprimir para insumos
        String partir[]= new String [2];
        String[] headInsumos={"ART","INSUMO","DESP.","D.TOTAL", "LOTE", "F.VENCIMIENTO", "EXIS.LOTE" };
        
        for(int i=0;i<forma.getNumeroIngresos();i++)
        {
        	partir=(forma.getDespachoMap("articulo_"+i)+"").split("-", 2);
        	String codigoInterfaz= UtilidadInventarios.obtenerCodigoInterfazArticulo(Utilidades.convertirAEntero(partir[0]));
        	codigoInterfaz+=UtilidadTexto.isEmpty(codigoInterfaz)?"":" - ";
        	datosInsumos[posInsumos]=codigoInterfaz+partir[0];
        	datosInsumos[posInsumos+1]=partir[1]+", UM: "+forma.getDespachoMap("unidadMedida_"+i)+"";
        	datosInsumos[posInsumos+2]=forma.getDespachoMap("cantidadInsumo_"+i)+"";
        	datosInsumos[posInsumos+3]=forma.getDespachoMap("despachoTotal_"+i)+"";
        	datosInsumos[posInsumos+4]=forma.getDespachoMap("loteInsumo_"+i)+"";
        	datosInsumos[posInsumos+5]=forma.getDespachoMap("fechaVencimientoLoteInsumo_"+i)+"";
        	
        	if(!UtilidadTexto.isEmpty(forma.getDespachoMap("existenciaXLoteInsumo_"+i)+""))
        		datosInsumos[posInsumos+6]= (Integer.parseInt(forma.getDespachoMap("existenciaXLoteInsumo_"+i)+"") - Integer.parseInt(forma.getDespachoMap("despacho_"+i)+""))+"";
        	posInsumos+=7;
        }
        if(datosInsumos.length>0)
        {	
	        report.createColSectionSinTitulo("seccionInsumos",headInsumos,datosInsumos,10);
	        
			float widths2[]={(float) 1,(float) 4.5,(float) 0.6, (float) 0.9, (float) 1, (float) 1, (float) 1};
			try
	        {
			    report.getSectionReference("seccionInsumos").getTableReference("parentTable").setWidths(widths2);
	        }
	        catch (BadElementException e)
	        {
	            logger.error("Se presentó error generando el pdf " + e);
	        }
	        //FIN SECCION INSUMOS************************************************************************************************
        }
        
        
        report.openReport(filename);
        
    	//report.getSectionReference("Cabecera").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, , report.font);
		//report.getSectionReference("Cabecera").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, , report.font);
		//report.getSectionReference("Cabecera").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, ", report.font);
        
        //report.font.setFontSizeAndAttributes(9,true,false,false);
		/*report.document.addParagraph(" ",report.font,15);
		report.document.addParagraph("PACIENTE: "+paciente.getApellidosNombresPersona()+", "+paciente.getCodigoTipoIdentificacionPersona()+": " +paciente.getNumeroIdentificacionPersona()+", EDAD: " +paciente.getEdadDetallada() ,report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,10);
		report.document.addParagraph("    ",report.font,5);
        */
        report.addSectionToDocument("seccionMedicamentos");
        report.addSectionToDocument("seccionInsumos");
        
        //SECCION OBSERVACIONES USER- PACIENTE*******************************************************************
        try
        {
	        if(!forma.getObservacionesGenerales().equals(""))
	        {
	        	//report.font.setFontSizeAndAttributes(9,false,false,false);
				//report.document.addParagraph("OBSERVACIONES ",report.font,iTextBaseDocument.ALIGNMENT_CENTER,30);
				report.font.setFontSizeAndAttributes(9,false,false,false);
				report.document.addParagraph(" ",report.font,15);
				report.document.addParagraph("OBSERVACIONES: "+forma.getObservacionesGenerales(),report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,10);
				report.document.addParagraph("    ",report.font,5);
	        }
	        else
	        {
	            report.document.addParagraph(" ",report.font,15);
	        }
        }
        catch(Exception e)
        {
        	report.document.addParagraph(" ",report.font,15);
        }    
        report.font.setFontSizeAndAttributes(9,true,true,false);
        report.document.addParagraph(usuario.getNombreUsuario()+", "+UtilidadFecha.getFechaActual() + "  "+UtilidadFecha.getHoraActual(),report.font,10);
        //FIN SECCION OBSERVACIONES USER- PACIENTE**************************************************************
        
        try
        {
        	if(forma.getNombrePersonaRecibe() != null && !forma.getNombrePersonaRecibe().equals("")){
        		int numEspacios = 218 - forma.getNombrePersonaRecibe().length() - (70 * forma.getNombrePersonaRecibe().length() / 100);
        		String tabulacion = "";
        		for (int i = 0; i < numEspacios; i++) {
        			tabulacion = tabulacion + " ";
        		}
        		report.closeReport("Persona Recibe: " + forma.getNombrePersonaRecibe() + tabulacion);
        	}
        	else{
        	report.closeReport();
        	}
        }
        catch (Exception e) 
        {
        	e.printStackTrace();
		}
    }

	/**
	 * Metodo que carga los datos restantes de un paciente.
	 * @param con
	 * @param paciente
	 */
	private static void cargarEmpresaRegimenPaciente(Connection con, PersonaBasica paciente) 
	{
		try 
		{
			paciente.cargarEmpresaRegimen(con,paciente.getCodigoPersona(),paciente.getCodigoTipoRegimen()+"");
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}


}
