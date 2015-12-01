/*
 * Created on 17-ene-2005
 *
 * Jorge Armando Osorio Velasquez
 * Princeton
 */
package com.princetonsa.pdf;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.pdf.PdfReports;

import com.princetonsa.actionform.medicamentos.AdminMedicamentosForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.medicamentos.AdminMedicamentos;

/**
 * @author armando
 * @author Wilson
 * @author joan
 * Princeton 17-ene-2005
 */
public class AdministracionMedicamentosPdf 
{
	public static final int tamanioCabecera=10;
	
	/**
	 * Tamanio de las Cabeceras de las tablas 
	 */
	public static final int tamanioDatosCabecerasTablas=9;
	/**
	 * Tamanio de las Cabeceras de las tablas 
	 */
	public static final int tamanioDatosCuerpoTablas=9;
	/**
	 * Tamanio parrafos
	 */
	public static final int tamanioParrafo=9;
	/**
	 * El espacion en blanco que se deja entre objeto y objeto.
	 */
	public static final int espacioEntreArticulos=20;
	/**
	 * El espacion entre una tabla y el parrafo.
	 */
	public static final int espacioEntreTablaParrafos=10;
	/**
	 * Objeto para manejar los logs de esta clase
	*/
    private static Logger logger=Logger.getLogger(AdministracionMedicamentosPdf.class);
    
    /**
     * 
     * @param filename
     * @param forma
     * @param usuario
     * @param paciente
     * @param con
     * @param request 
     * @throws SQLException
     */
    public static void pdfAdminMedicamentos(String filename, AdminMedicamentosForm forma, UsuarioBasico usuario,PersonaBasica paciente,Connection con, HttpServletRequest request) throws SQLException 
    {
		PdfReports report = new PdfReports();
		InstitucionBasica ins= new InstitucionBasica();
		ins.cargarXConvenio(usuario.getCodigoInstitucionInt(), paciente.getCodigoConvenio());
		
		cargarEmpresaRegimenPaciente(con,paciente);
        String[] headMedicamentos={"U.CONSUMO ACUM","FECHA/HORA ADMIN.","OBSERVACIONES","USUARIO, FECHA/HORA GRAB.","TRAIDO PAC."};
		//Generar Cabecera del documento-*******************************************************************************
		
		String tituloMedicamentos="ADMINISTRACIÓN DE MEDICAMENTOS (" + UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() + ")";
		report.setReportBaseHeader1(ins, ins.getNit(), tituloMedicamentos);
		
		//seccion de cabecera
		String[] datosCabecera={
				"VIA DE INGRESO: "+paciente.getUltimaViaIngreso(),
				"CAMA: "+paciente.getCama(),
				"ÁREA: "+paciente.getArea(),
				"RESPONSABLE: "+paciente.getConvenioPersonaResponsable(),
				"RÉGIMEN: "+paciente.getNombreTipoRegimen(),
				//(forma.getNumeroAutorizacion()!=null&&!forma.getNumeroAutorizacion().equals("null"))?"Nº AUTORIZACIÓN: "+forma.getNumeroAutorizacion():" ",
				"Nº ORDEN: "+forma.getOrden(),
				"PRIORIDAD: "+forma.getPrioridad(),
				"ESTADO: Administrada"//NO es necesario tomar el estado medico pues siempre que se llegue a este punto va a ser "Administrada"
		};
				//"ESTADO: "+forma.getEstadoMedico()
		//le sume una fila para dar un espacion de la cabecera a los datos.
		report.createSection("Cabecera",PdfReports.REPORT_HEADER_TABLE,5,3,2);
		report.getSectionReference("Cabecera").setTableBorder(PdfReports.REPORT_HEADER_TABLE, 0xFFFFFF, 0.0f);
		report.getSectionReference("Cabecera").setTableCellBorderWidth(PdfReports.REPORT_HEADER_TABLE, 0.5f);
		report.getSectionReference("Cabecera").setTableCellsDefaultColors(PdfReports.REPORT_HEADER_TABLE, 0xFFFFFF, 0xFFFFFF);
		report.setReportDataSectionAtributes(0xFFFFFF, 0xFFFFFF, 0x000000, tamanioCabecera);
		
		//paciente
		report.getSectionReference("Cabecera").setTableCellsColSpan(3);
		report.font.setFontSizeAndAttributes(9,true,false,false);
		report.getSectionReference("Cabecera").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, "PACIENTE: "+paciente.getApellidosNombresPersona(false)+"		"+paciente.getCodigoTipoIdentificacionPersona()+": " +paciente.getNumeroIdentificacionPersona()+"		"+ "EDAD: " +paciente.getEdadDetallada(), report.font);
		report.getSectionReference("Cabecera").setTableCellsColSpan(1);

		
		/*report.getSectionReference("Cabecera").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, "PACIENTE: "+paciente.getApellidosNombresPersona(), report.font);
		report.getSectionReference("Cabecera").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, paciente.getCodigoTipoIdentificacionPersona()+": " +paciente.getNumeroIdentificacionPersona(), report.font);
		report.getSectionReference("Cabecera").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, "EDAD: " +paciente.getEdadDetallada(), report.font);
		*/
		report.font.setFontSizeAndAttributes(tamanioCabecera,false,false,false);
		for(int k=0; k<datosCabecera.length; k++)	
			report.getSectionReference("Cabecera").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, datosCabecera[k], report.font);
		float widths[]={(float) 5,(float) 2,(float) 3};
		try
        {
			report.getSectionReference("Cabecera").getTableReference(PdfReports.REPORT_HEADER_TABLE).setWidths(widths);
		}
        catch (Exception e)
        {
        	e.getStackTrace();
        }
		//Seccion de datos.////////////////////
		report.openReport(filename);
	    ArrayList arrayList = new ArrayList();
        String[] datosDetalle =  null; 	
        int i=0;
        int pos=0;
        String articulo="";
        AdminMedicamentos mundo= new AdminMedicamentos();
        mundo.resumenAdmiMedicamentosPart1(con, forma.getNumeroSolicitud());
        mundo.resumenAdmiMedicamentosPart2(con, forma.getNumeroSolicitud());
        for(int k=0; k < Integer.parseInt(forma.getResumenAdminMap("numRegistrosPart1").toString()); k++)
		{
			articulo=String.valueOf(forma.getResumenAdminMap("resumenArt_"+k));
			for(int j=0; j < Integer.parseInt(forma.getResumenAdminMap("numRegistrosPart2").toString());j++)
			{
				if(Integer.parseInt(forma.getResumenAdminMap("codigoArt_"+j)+"") == Integer.parseInt(forma.getResumenAdminMap("resumenCodigoArt_"+k)+""))
				{
					arrayList.add(pos,Integer.parseInt(forma.getResumenAdminMap("cantidadArt_"+j).toString()));
			        pos ++;
			        arrayList.add(pos,(forma.getResumenAdminMap("fechaHoraAdminArt_"+j)+""));
			        pos ++;
			        arrayList.add(pos,forma.getResumenAdminMap("observacionesArt_"+j));
			        pos ++;
			        arrayList.add(pos,(forma.getResumenAdminMap("usuarioFechaHoraArt_"+j)+"").substring(0,(forma.getResumenAdminMap("usuarioFechaHoraArt_"+j)+"").length()-7));
			        pos ++;
			        arrayList.add(pos,forma.getResumenAdminMap("traidoPacienteArt_"+j));
			        pos ++;
				}
			}
			datosDetalle = new String[arrayList.size()];
		   /*String temporalDos="";
		   String temporalFrec="";
		   String temporalVia="";
		   String temporalObs="";*/
	       for(i=0; i<arrayList.size(); i++)
	           datosDetalle[i] = arrayList.get(i) + "";
	       pos = 0;
	       arrayList.clear();
	       /*articulo=mundo.getDevolucionMap("impresionCodigoArt_"+k)+" "+
			  				mundo.getDevolucionMap("impresionDescripcion_"+k)
			  				+", CONC. "+mundo.getDevolucionMap("impresionConcentracion_"+k)
			  				+", FF. "+mundo.getDevolucionMap("impresionFormaFarmaceutica_"+k)
			  				+", UM. "+mundo.getDevolucionMap("impresionUnidadMedida_"+k);
			//Dosis
			temporalDos=mundo.getDevolucionMap("impresionDosis_"+k)+"";
			if(temporalDos.equals("null")||temporalDos.equals(null))
			{
				temporalDos="";
			}
			else
			{
				temporalDos=mundo.getDevolucionMap("impresionDosis_"+k)+"";
			}
			//Frecuencia
			temporalFrec=mundo.getDevolucionMap("impresionFrecuencia_"+k)+"";
			if(temporalFrec.equals("null")||temporalFrec.equals(null)||temporalFrec.equals("0.0")||temporalFrec.equals("0 null"))
			{
				temporalFrec="";
			}
			else
			{
				temporalFrec=mundo.getDevolucionMap("impresionFrecuencia_"+k)+"";
			}
			//Via
			temporalVia=mundo.getDevolucionMap("impresionVia_"+k)+"";
			if(temporalVia.equals("null")||temporalVia.equals(null))
			{
				temporalVia="";
			}
			else
			{
				temporalVia=mundo.getDevolucionMap("impresionVia_"+k)+"";
			}
			//Observaciones
			temporalObs=mundo.getDevolucionMap("impresionObservaciones_"+k)+"";
			if(temporalObs.equals("null")||temporalObs.equals(null))
			{
				temporalObs="";
			}
			else
			{
				temporalObs=mundo.getDevolucionMap("impresionObservaciones_"+k)+"";
			}*/
			//String[] detalleAdicArticulo={"DOSIS: "+mundo.getDevolucionMap("impresionDosis_"+k),"FRECUENCIA: "+mundo.getDevolucionMap("impresionFrecuencia_"+k),"VIA: "+mundo.getDevolucionMap("impresionVia_"+k),(mundo.getDevolucionMap("impresionObservaciones_"+k)!=null&&(mundo.getDevolucionMap("impresionObservaciones_"+k)+"").equals("null"))?"OBSERVACIONES: "+mundo.getDevolucionMap("impresionObservaciones_"+k):""};
			//String[] detalleAdicArticulo={"DOSIS: "+temporalDos,"FRECUENCIA: "+temporalFrec,"VIA: "+temporalVia,"OBSERVACIONES: "+temporalObs};report.font.setFontSizeAndAttributes(tamanioParrafo,true,false,false);
			report.document.addParagraph(articulo,report.font,espacioEntreArticulos);
			report.font.setFontSizeAndAttributes(tamanioParrafo,false,false,false);
			//report.document.addParagraph(detalleAdicArticulo[0]+"     "+detalleAdicArticulo[1]+"     "+detalleAdicArticulo[2]+"     "+detalleAdicArticulo[3],report.font,espacioEntreTablaParrafos);
			report.setReportTitleSectionAtributes(0x000000, 0xEBEBEB,0x000000, 11);
			report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 10);
			report.createColSection("seccionMedicamentos_"+k,headMedicamentos,datosDetalle,10,10,0);
			float widthsDatos[]={(float)1.3,(float) 2.2,(float) 2.2, (float) 2.9, (float) 1.4};
			try
			{
			    report.getSectionReference("seccionMedicamentos_"+k).getTableReference(PdfReports.REPORT_PARENT_TABLE).setWidths(widthsDatos);
			}
			catch (Exception e)
			{
			    logger.error("Se presentó error generando el pdf creando la seccion de medicamentos" + e);
			}
			report.addSectionToDocument("seccionMedicamentos_"+k);
			
			double unidadesSumFarma=Utilidades.convertirADouble(forma.getResumenAdminMap("resumenAdminFarmacia_"+k)+"");
			int unidadesSaldos=0;
			if(unidadesSumFarma>0&&forma.isEntidadControlaDespachoSaldoMultidosis()&&UtilidadTexto.getBoolean(forma.getResumenAdminMap("esmultidosis_"+k)+"")&&Utilidades.convertirADouble(forma.getResumenAdminMap("totaldespachosaldos_"+k)+"")>0)
			{
				unidadesSaldos=(int)(Utilidades.convertirADouble(forma.getResumenAdminMap("totaldespachosaldos_"+k)+"")/Utilidades.convertirADouble(forma.getResumenAdminMap("cantidadunidosis_"+k)+""));
				if(unidadesSumFarma<unidadesSaldos)
				{
					unidadesSaldos=(int)unidadesSumFarma;
				}
			}
			String[] despachoArticulo={
			        (!(forma.getResumenAdminMap("resumenDespachoTotal_"+k)+"").equals("null"))?"DESPACHO TOTAL: "+forma.getResumenAdminMap("resumenDespachoTotal_"+k):"Despacho Total: "+"0",
			        ((!(forma.getResumenAdminMap("resumenAdminFarmacia_"+k)+"").equals("null"))?"TOTAL UNIDADES CONSUMIDAS FARMACIA: "+forma.getResumenAdminMap("resumenAdminFarmacia_"+k):"Total Unidades Consumidas Farmacia: "+"0")+(unidadesSaldos>0?" (Unidades Saldos: "+unidadesSaldos+")":""),
			        (!(forma.getResumenAdminMap("resumenAdminPaciente_"+k)+"").equals("null"))?"TOTAL UNIDADES CONSUMIDAS PACIENTE: "+forma.getResumenAdminMap("resumenAdminPaciente_"+k):"Total Unidades Consumidas Paciente: "+"0",
			        (!(forma.getResumenAdminMap("resumenTotalAdmin_"+k)+"").equals("null"))?"TOTAL UNIDADES CONSUMIDAS: "+forma.getResumenAdminMap("resumenTotalAdmin_"+k):"Total Unidades Consumidas: "+"0"
			};
			report.font.setFontSizeAndAttributes(tamanioParrafo,false,false,false);
			report.document.addParagraph(despachoArticulo[0]+"     "+despachoArticulo[1]+"     "+despachoArticulo[2]+"     "+despachoArticulo[3],report.font,espacioEntreTablaParrafos);
		}
		report.closeReport();
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
	