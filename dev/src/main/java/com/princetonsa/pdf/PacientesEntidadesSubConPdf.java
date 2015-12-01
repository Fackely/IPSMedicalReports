/*
 * Ene 28, 2007
 */
package com.princetonsa.pdf;

import java.sql.Connection;
import java.util.HashMap;
import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;
import util.pdf.iTextBaseTable;

import com.lowagie.text.BadElementException;
import com.princetonsa.actionform.manejoPaciente.PacientesEntidadesSubConForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Convenio;
import com.princetonsa.mundo.facturacion.EntidadesSubContratadas;
import com.princetonsa.mundo.facturacion.ParametrizacionInstitucion;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * 
 * Clase que realiza la impresión de los registros de pacientes entidades subcontratadas
 * @author Sebastián Gómez R.
 *
 */
public class PacientesEntidadesSubConPdf 
{
	/**
     * Clase para manejar los logs de esta clase
     */
    private static Logger logger=Logger.getLogger(PacientesEntidadesSubConPdf.class);
    
    /**
     * Objeto institucion
     */
    private ParametrizacionInstitucion p = new ParametrizacionInstitucion();
    
    /**
     * Objeto del mundo del convenio
     */
    private Convenio mundoConvenio = new Convenio();
    
    
    public void imprimir(Connection con,String filename,PacientesEntidadesSubConForm forma,UsuarioBasico usuario, PersonaBasica paciente) throws IPSException
    {
    	PdfReports report = new PdfReports();
    	
    	
    	//Se cargan los datos de la institucion
		p.cargar(con, usuario.getCodigoInstitucionInt());
		
		//Se cargan los datos del convenio
		mundoConvenio.cargarResumen(con, Integer.parseInt(forma.getRegistroEntidadesSubMap("codigoConvenio").toString()));
		
		//Se carga la entidad subcontratada
		HashMap criterios = new HashMap();
		criterios.put("codigoPk0_0", forma.getRegistroEntidadesSubMap("entidadSubcontratada"));
		criterios.put("institucion2_0", usuario.getCodigoInstitucion());
		HashMap entidadSubcontratada = EntidadesSubContratadas.buscar0(con, criterios);
		
		//**********************GENERAR INFORMACION DELA INSTITUCION*****************************
		this.generarInformacionInstitucion(con,usuario,report,paciente);
		//****************************************************************************************
		
		//se abre el reporte
		report.openReport(filename);
        report.font.useFont(iTextBaseFont.FONT_TIMES, true, true, true);
        iTextBaseTable section;
        
        //**************SECCION REGISTRO INGRESO*********************************************************************
        section = report.createSection("registroIngreso", "registroIngresoTable", 6, 6, 10);
  		section.setTableBorder("registroIngresoTable", 0x000000, 0.5f);
	    section.setTableCellBorderWidth("registroIngresoTable", 0.5f);
	    section.setTableCellPadding("registroIngresoTable", 1);
	    section.setTableSpaceBetweenCells("registroIngresoTable", 0.5f);
	    section.setTableCellsDefaultColors("registroIngresoTable", 0xFFFFFF, 0xFFFFFF);
	    
	    section.setTableCellsColSpan(2);
	    report.font.setFontAttributes(0x000000, 10, false, false, false);
	    section.addTableTextCellAligned("registroIngresoTable", "Paciente: "+forma.getDatosPacienteMap("primerApellido")+ " "+ forma.getDatosPacienteMap("segundoApellido") + " " + forma.getDatosPacienteMap("primerNombre") + " " + forma.getDatosPacienteMap("segundoNombre"), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.addTableTextCellAligned("registroIngresoTable", "Nro Id: "+forma.getDatosPacienteMap("numeroIdentificacion"), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.addTableTextCellAligned("registroIngresoTable", "Tipo Id: "+forma.getDatosPacienteMap("tipoIdentificacion"), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    section.setTableCellsColSpan(4);
	    section.addTableTextCellAligned("registroIngresoTable", "Convenio Paciente: "+mundoConvenio.getNombre(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(2);
	    section.addTableTextCellAligned("registroIngresoTable", "Nro Id: "+mundoConvenio.getNumeroIdentificacion(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    section.setTableCellsColSpan(6);
	    section.addTableTextCellAligned("registroIngresoTable", "Entidad autorizada: "+entidadSubcontratada.get("razonSocial3_0"), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(3);
	    section.addTableTextCellAligned("registroIngresoTable", "Dirección: "+entidadSubcontratada.get("direccion6_0"), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.addTableTextCellAligned("registroIngresoTable", "Teléfono: "+entidadSubcontratada.get("telefono7_0"), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.addTableTextCellAligned("registroIngresoTable", "Autorización Nro: "+forma.getRegistroEntidadesSubMap("autorizacionIngreso"), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.addTableTextCellAligned("registroIngresoTable", "Fecha/Hora autorización: "+forma.getRegistroEntidadesSubMap("fechaAutorizacion")+" / "+forma.getRegistroEntidadesSubMap("horaAutorizacion"), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(6);
	    section.addTableTextCellAligned("registroIngresoTable", "Observaciones: "+forma.getRegistroEntidadesSubMap("observaciones"), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    //se añade la sección al documento
	    report.addSectionToDocument("registroIngreso");
	    //*****************************************************************************************************************
		
	    
	    //Si el estado es diferente de imprimir registro entonces se imprime el detalle de los servicios
	    if(!forma.getEstado().equals("imprimirRegistro"))
	    {
	    	//*************************SECCION DETALLE DE LOS SERVICIOS*************************************************************
	    	int numRegistros = Utilidades.convertirAEntero(forma.getServiciosAutorizadosMap("numRegistros")+"",true);
		    float widths[]={(float) 1,(float) 0.5, (float) 4.5,(float) 1, (float) 3};
		    section = report.createSection("detalleServicios", "detalleServiciosTable",numRegistros+1 , 5, 8);
	  		section.setTableBorder("detalleServiciosTable", 0xFFFFFF, 0.5f);
	  		
		    section.setTableCellBorderWidth("detalleServiciosTable", 0.5f);
		    section.setTableCellPadding("detalleServiciosTable", 1);
		    section.setTableSpaceBetweenCells("detalleServiciosTable", 0.5f);
		    section.setTableCellsDefaultColors("detalleServiciosTable", 0xFFFFFF, 0xFFFFFF);
		    report.setReportTitleSectionAtributes(0xFFFFFF, 0xFFFFFF,0x000000, 10);
		    //report.addSectionTitle("detalleAnexos", "detalleServiciosTable", "Facturas");
		    
		    //Se asignan los encabezados de la tabla
		    report.font.setFontAttributes(0x000000, 8, true, false, false);
		    section.addTableTextCellHeaderAligned("detalleServiciosTable", "Código", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,1,0);
		    section.addTableTextCellHeaderAligned("detalleServiciosTable", "Cant.", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,1,1);
		    section.addTableTextCellHeaderAligned("detalleServiciosTable", "Servicio", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,1,2);
		    section.addTableTextCellHeaderAligned("detalleServiciosTable", "Autorización", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,1,3);
		    section.addTableTextCellHeaderAligned("detalleServiciosTable", "Usuario Regist.", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,1,4);
		    section.getTableReference("detalleServiciosTable").endHeaders();
		    
		    report.font.setFontAttributes(0x000000, 8, false, false, false);
		    
		    //Se muestra la informacion de cada servicio
		    for(int i=0;i<numRegistros;i++)
		    {
		    	
		    	if(forma.getEstado().equals("imprimirTodo")||(forma.getEstado().equals("imprimirServicio")&&forma.getIndexImpresion()==i))
		    	{
			    	section.addTableTextCellAligned("detalleServiciosTable", Utilidades.obtenerCodigoPropietarioServicio(con, forma.getServiciosAutorizadosMap("servicio_"+i).toString(), ConstantesBD.codigoTarifarioCups), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);		    	
				    section.addTableTextCellAligned("detalleServiciosTable", forma.getServiciosAutorizadosMap("cantidad_"+i).toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    section.addTableTextCellAligned("detalleServiciosTable", forma.getServiciosAutorizadosMap("nombreServicio_"+i).toString(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
				    section.addTableTextCellAligned("detalleServiciosTable", forma.getServiciosAutorizadosMap("autorizacionServicio_"+i).toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    section.addTableTextCellAligned("detalleServiciosTable", forma.getServiciosAutorizadosMap("nombreResponsable_"+i).toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    	}
		    }
		    
		    
		    
		    try 
            {
				section.getTableReference("detalleServiciosTable").setWidths(widths);
			} 
            catch (BadElementException e) 
            {
				logger.error("Error al ajustar los anchos de la tabla de servicios pacientes entidades subcontratadas: "+e);
			}
		    //se añade la sección al documento
		    report.addSectionToDocument("detalleServicios");
	    	//************************************************************************************************************************
	    	
	    }
		
	    report.closeReport();
		//*******************************************************************************************************************
    }


    /**
     * Método usado para imprimir el encabezado de la institucion
     * @param con
     * @param usuario
     * @param report
     */
	private void generarInformacionInstitucion(Connection con, UsuarioBasico usuario, PdfReports report, PersonaBasica paciente) 
	{
		//se verifica si se maneja multiempresa
		boolean multiempresa = UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(usuario.getCodigoInstitucionInt()));
    	
		
		///Se carga la empresa-institucion (Si aplica)
		HashMap<String, Object> empresaInstitucion = new HashMap<String, Object>();
		if(multiempresa)
		{
			HashMap criterios = new HashMap();
			criterios.put("codigo1_", mundoConvenio.getEmpresaInstitucion()); 
			criterios.put("institucion3_", usuario.getCodigoInstitucion());
			empresaInstitucion = ParametrizacionInstitucion.consultarEmpresas(con, criterios);
			
			if(Utilidades.convertirAEntero(empresaInstitucion.get("numRegistros")+"", true)>0)
			{
				//Se cambian los datos de la institucion
				p.setRazonSocial(empresaInstitucion.get("razonSocial4_0").toString());
				p.setNit(empresaInstitucion.get("nit2_0").toString());
				p.setDireccion(empresaInstitucion.get("direccion7_0").toString());
				p.setTelefono(empresaInstitucion.get("telefono8_0").toString());
			}
			else
				multiempresa = false;
		}
		
		//**************IMPRESION DE LA INFORMACION DE LA INSTITUCIOIN************************************************
		
		String tituloReporte="";
		
		InstitucionBasica institucionBasica= new InstitucionBasica();
		institucionBasica.cargarXConvenio(usuario.getCodigoInstitucionInt(), paciente.getCodigoConvenio());
				
	    report.setReportBaseHeader1(institucionBasica.getLogoReportes(), institucionBasica.getRazonSocial(), institucionBasica.getNit(), tituloReporte, 12);
		
	}
    
    
}
