/*
 * Created on 06-oct-2004
 *
 * Jorge Armando Osorio Velasquez
 * Princeton
 */
package com.princetonsa.pdf;

import java.sql.Connection;
import java.sql.SQLException;

import util.UtilidadFecha;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;

import com.princetonsa.actionform.resumenAtenciones.ResumenAtencionesForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * Clase que genera los archivos PDF del resumen de atenciones
 * @author armando
 * Princeton 06-oct-2004
 */
public class ResumenAtencionesPdf 
{

	/**
	 * Colores utilizados en el reporte
	 */
	/**
	 * Color de la barra de titulo de la seccion articulos.
	 */
	public static final int colorNomArtiulo=0xDEDADA;
	/**
	 * Colot de la barra de titulo de la seccion despachos y la seccion administraciones.
	 */
	public static final int barraDespachoAdmin=0xEFEFEF;
	/**
	 * Color Blanco
	 */
	public static final int blanco=0xFFFFFF;
	/**
	 * Color Negro
	 */
	public static final int negro=0x000000;
	
	/**
	 * Tamanio de las Cabecera del documento
	 */
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
	public static final int espacionEntreParrafos=15;
	
	/**
	 * El espacion en blanco que se deja entre objeto y objeto.
	 */
	public static final int espacionEntreSolicitudes=25;
	
	/**
	 * Espacion que hay entre una tabla y un parrafo.
	 */
	public static final int espacionEntreTablaParrafo=6;
	
	/**
	 * Numero de columnas de la tabla administracion
	 */
	public static final int columnasAdministracion=5;
	/**
	 * Metodo para generan el PDF del resumen de atenciones para la
	 * administracion de medicamentos que ha tenido un paciente.
	 * @param filename, Nombre del archivo temporal base para la generacion del PDF
	 * @param resumenAtencionesForm, form que contien los datos.
	 * @param paciente, paciente al cual se esta consultando el resumen de atenciones que tiene.
	 * @param usuario
	 * @param con
	 */
	public static void pdfResumenAtenciones(String filename, ResumenAtencionesForm resumenAtencionesForm, PersonaBasica paciente, UsuarioBasico usuario, Connection con)
	{
		PdfReports report = new PdfReports();
		cargarEmpresaRegimenPaciente(con,paciente);
		String tituloAtenciones="ADMINISTRACION MEDICAMENTOS PACIENTE (" + UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() + ")";
		crearPlantillaReporte(usuario,report,tituloAtenciones, paciente);
		crearCabecerraParaReporte(report,resumenAtencionesForm,paciente);
		report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
		report.openReport(filename);
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		report.setReportTitleSectionAtributes(negro, blanco,negro, 14);
		report.setReportDataSectionAtributes(negro, blanco, negro, 12);
		insertarSolicitudesAlReporte(report,resumenAtencionesForm);
		report.closeReport();
	}




	/**
	 * Metodo para generar la plantilla de un reporte
	 * @param report,Controlador del Reporte
	 * @param usuario,Usuario que genera el reporte.
	 * @param titulo,Titulo del reporte.
	 * 
	 */
	private static void crearPlantillaReporte(UsuarioBasico usuario, PdfReports report,String titulo, PersonaBasica paciente) 
	{
		InstitucionBasica institucionBasica = new InstitucionBasica();
		institucionBasica.cargarXConvenio(usuario.getCodigoInstitucionInt(), paciente.getCodigoConvenio());
		report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), titulo);
	}
	
	
	
	/**
	 * Metodo para recorrer solicitud por solicitud del paciente y generar una seccion por cada una.
	 * @param report, Controlador del reporte.
	 * @param resumenAtencionesForm,Form que contiene los datos
	 */
	private static void insertarSolicitudesAlReporte(PdfReports report, ResumenAtencionesForm resumenAtencionesForm) 
	{
		for(int i=0;i<resumenAtencionesForm.getNumeroSolicitudes();i++)
		{
			report.setReportTitleSectionAtributes(negro, blanco,negro, 12);
			report.setReportDataSectionAtributes(negro, blanco, negro, 10);
			report.font.setFontSizeAndAttributes(tamanioParrafo,true,false,false);
			report.document.addParagraph("Nº ORDEN: "+resumenAtencionesForm.getSolicitudes("numeroOrden_"+i)+"; "+resumenAtencionesForm.getSolicitudes("fechahorasolicitud_"+i),report.font,iTextBaseDocument.ALIGNMENT_LEFT,espacionEntreSolicitudes);
			insertarArticulos(report,resumenAtencionesForm,i);
		}
	}
	
	
	
	/**
	 * Metodo para generar la cabecera del reporte.
	 * @param report, controlador del reporte
	 * @param resumenAtencionesForm, Form que contiene los datos.
	 * @param paciente, Paciente
	 * 
	 */
	private static void crearCabecerraParaReporte(PdfReports report, ResumenAtencionesForm resumenAtencionesForm, PersonaBasica paciente) 
	{	
		String[] datosCabecera={
				"VIA DE INGRESO: "+resumenAtencionesForm.getNomViaIngreso(),
				"CAMA: "+paciente.getCama(),
				"ÁREA: "+paciente.getArea(),
				"RESPONSABLE: "+paciente.getConvenioPersonaResponsable(),
				"RÉGIMEN: "+paciente.getNombreTipoRegimen(),
				resumenAtencionesForm.getFechIngreso()==null||resumenAtencionesForm.getFechIngreso().equals("")?"":"FECHA INGRESO: "+resumenAtencionesForm.getFechIngreso(),
				};
		report.createSection("Cabecera",PdfReports.REPORT_HEADER_TABLE,3,3,2);
		report.getSectionReference("Cabecera").setTableBorder(PdfReports.REPORT_HEADER_TABLE, blanco, 0.0f);
		report.getSectionReference("Cabecera").setTableCellBorderWidth(PdfReports.REPORT_HEADER_TABLE, 0.5f);
		report.getSectionReference("Cabecera").setTableCellsDefaultColors(PdfReports.REPORT_HEADER_TABLE, blanco, blanco);
		report.setReportDataSectionAtributes(blanco, blanco, negro, tamanioCabecera);
		report.getSectionReference("Cabecera").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, "PACIENTE: "+paciente.getApellidosNombresPersona(false), report.font);
		report.getSectionReference("Cabecera").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, paciente.getCodigoTipoIdentificacionPersona()+": " +paciente.getNumeroIdentificacionPersona(), report.font);
		report.getSectionReference("Cabecera").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, "EDAD: " +paciente.getEdadDetallada(), report.font);
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
	}

	
	
	/**
	 * Metodo que realiza creacion e insercion de la seccion articulo en la seccion solicitudes
	 * @param report, Maneja el reporte.
	 * @param nomSeccion, nombre de las seccion en la cual se insertara, (seccion de solicitudes).
	 * @param resumenAtencionesForm, Form que contiene los datos que se mostraran en el reporte.
	 * @param i, Numero de seccion(solicitud), en la que nos encontramos
	 * 
	 */
	private static void insertarArticulos(PdfReports report, ResumenAtencionesForm resumenAtencionesForm, int i)
	{
		for(int k=0;k<Integer.parseInt(resumenAtencionesForm.getSolicitudes("numeroArticulos_"+i)+"");k++)
		{
			String[] cabecerasDespacho=
			{
					"DOSIS",
					"FREC.",
					"VIA",
					"DESPACHO TOTAL",
					"TOTAL ADM. FARMACIA",
					"TOTAL ADM. PACIENTE",
					"TOTAL ADMINISTRADO"
			};
			String[] datosDespacho=
			{
					resumenAtencionesForm.getSolicitudes("dosis_Articulo_"+k+"_Solicitud_"+i)+""==null||(resumenAtencionesForm.getSolicitudes("dosis_Articulo_"+k+"_Solicitud_"+i)+"").equals("null")?"0":resumenAtencionesForm.getSolicitudes("dosis_Articulo_"+k+"_Solicitud_"+i)+"",
					resumenAtencionesForm.getSolicitudes("frecuencia_Articulo_"+k+"_Solicitud_"+i)+""==null||(resumenAtencionesForm.getSolicitudes("frecuencia_Articulo_"+k+"_Solicitud_"+i)+"").equals("null")?"0":resumenAtencionesForm.getSolicitudes("frecuencia_Articulo_"+k+"_Solicitud_"+i)+"",
					resumenAtencionesForm.getSolicitudes("via_Articulo_"+k+"_Solicitud_"+i)+""==null||(resumenAtencionesForm.getSolicitudes("via_Articulo_"+k+"_Solicitud_"+i)+"").equals("null")?"0":resumenAtencionesForm.getSolicitudes("via_Articulo_"+k+"_Solicitud_"+i)+"",
					resumenAtencionesForm.getSolicitudes("resumenDespachoTotal_Articulo_"+k+"_Solicitud_"+i)+""==null||(resumenAtencionesForm.getSolicitudes("resumenDespachoTotal_Articulo_"+k+"_Solicitud_"+i)+"").equals("null")?"0":resumenAtencionesForm.getSolicitudes("resumenDespachoTotal_Articulo_"+k+"_Solicitud_"+i)+"",
					resumenAtencionesForm.getSolicitudes("resumenAdminFarmacia_Articulo_"+k+"_Solicitud_"+i)+""==null||(resumenAtencionesForm.getSolicitudes("resumenAdminFarmacia_Articulo_"+k+"_Solicitud_"+i)+"").equals("null")?"0":resumenAtencionesForm.getSolicitudes("resumenAdminFarmacia_Articulo_"+k+"_Solicitud_"+i)+"",
					resumenAtencionesForm.getSolicitudes("resumenAdminPaciente_Articulo_"+k+"_Solicitud_"+i)+""==null||(resumenAtencionesForm.getSolicitudes("resumenAdminPaciente_Articulo_"+k+"_Solicitud_"+i)+"").equals("null")?"0":resumenAtencionesForm.getSolicitudes("resumenAdminPaciente_Articulo_"+k+"_Solicitud_"+i)+"",
					resumenAtencionesForm.getSolicitudes("resumenTotalAdmin_Articulo_"+k+"_Solicitud_"+i)+""==null||(resumenAtencionesForm.getSolicitudes("resumenTotalAdmin_Articulo_"+k+"_Solicitud_"+i)+"").equals("null")?"0":resumenAtencionesForm.getSolicitudes("resumenTotalAdmin_Articulo_"+k+"_Solicitud_"+i)+""
			};
			report.font.setFontSizeAndAttributes(tamanioParrafo,true,false,false);
			report.document.addParagraph(resumenAtencionesForm.getSolicitudes(("resumenArt_Articulo_"+k+"_Solicitud_"+i))+"",report.font,espacionEntreParrafos);
			crearSeccionDespachos(report,cabecerasDespacho,datosDespacho);
			if(resumenAtencionesForm.getSolicitudes("administraciones_Articulo_"+k+"_Solicitud_"+i)+""!=null&&!(resumenAtencionesForm.getSolicitudes("administraciones_Articulo_"+k+"_Solicitud_"+i)).equals("null"))
				crearSeccionAdministraciones(report,resumenAtencionesForm,i,k);
			report.font.setFontSizeAndAttributes(tamanioParrafo,false,false,false);
			report.document.addParagraph(cabecerasDespacho[3]+": "+datosDespacho[3]+"     "+cabecerasDespacho[4]+": "+datosDespacho[4]+"     "+cabecerasDespacho[5]+": "+datosDespacho[5]+"     "+cabecerasDespacho[6]+": "+datosDespacho[6],report.font,espacionEntreTablaParrafo);
		}
	}




	/**
	 * Metodo que realiza creacion  de la seccion despacho de un articulo y su insercion en la seccion de articulos 
	 * @param report, Maneja el reporte.
	 * @param resumenAtencionesForm, Form con los datos
	 * @param nomSeccionArticulo, Nombre de la seccion de articulo donde se insertara.
	 * @param datosDespacho
	 * @param cabecerasDespacho
	 * @param k, Numero de seccion(solicitud), en la que nos encontramos.
	 * 
	 */
	private static void crearSeccionDespachos(PdfReports report,  String[] cabecerasDespacho, String[] datosDespacho)
	{
		report.font.setFontSizeAndAttributes(tamanioParrafo,false,false,false);
		report.document.addParagraph(cabecerasDespacho[0]+": "+datosDespacho[0]+"     "+cabecerasDespacho[1]+": "+datosDespacho[1]+"     "+cabecerasDespacho[2]+": "+datosDespacho[2],report.font,espacionEntreParrafos);
		//Crea cuadro para dosis,frec,via,despachoTotal,total admin farmacia,total admi paciente, total admin
		/*
		String nomSeccionDespachos="seccionDespachos_"+i+"_"+k;
		report.createColSection(nomSeccionDespachos,cabecerasDespacho,datosDespacho,tamanioDatosCabecerasTablas,tamanioDatosCuerpoTablas,0);
		float widths[]={(float) 0.7,(float) 0.7,(float) 0.5,(float) 1.7,(float) 2.2,(float) 2.1,(float) 2.2};
		try
        {
			report.getSectionReference(nomSeccionDespachos).getTableReference(PdfReports.REPORT_PARENT_TABLE).setWidths(widths);
		}
        catch (Exception e)
        {
        	e.getStackTrace();
        }
		report.addSectionToDocument(nomSeccionDespachos);
		*/
	}
	
	
	
	/**
	 * Metodo que genera un seccion de administracion de Medicamentos y la inserta en la seccion de articulos
	 * @param report, manejado del reporte
	 * @param resumenAtencionesForm, Form con los datos de la administracion de Medicamentos
	 * @param nomSeccionArticulo, seccion de articulos,
	 * @param i, numero de solicitudes
	 * @param k, numero del articulo
	 */
	private static void crearSeccionAdministraciones(PdfReports report, ResumenAtencionesForm resumenAtencionesForm,  int i, int k) 
	{
		String[] cabecerasAdministracion=
		{
				"CANT.",
				"OBSERVACIONES",
				"FECHA - HORA ADM.",
				"USUARIO, FECHA Y HORA GRAB.",
				"TRAIDO PACIENTE"
		};
		String[] datosAdministracion=new String[Integer.parseInt(resumenAtencionesForm.getSolicitudes("administraciones_Articulo_"+k+"_Solicitud_"+i)+"")*columnasAdministracion];
		int posVector=0;
		for(int z=0;z<Integer.parseInt(resumenAtencionesForm.getSolicitudes("administraciones_Articulo_"+k+"_Solicitud_"+i)+"");z++)
		{
			datosAdministracion[posVector]=resumenAtencionesForm.getSolicitudes("cantidadArt_"+z+"_Articulo_"+k+"_Solicitud_"+i)+""==null||(resumenAtencionesForm.getSolicitudes("cantidadArt_"+z+"_Articulo_"+k+"_Solicitud_"+i)+"").equals("null")?"0":resumenAtencionesForm.getSolicitudes("cantidadArt_"+z+"_Articulo_"+k+"_Solicitud_"+i)+"";
			posVector++;
			datosAdministracion[posVector]=resumenAtencionesForm.getSolicitudes("observaciones_Admin_"+z+"_Articulo_"+k+"_Solicitud_"+i)+""==null||(resumenAtencionesForm.getSolicitudes("observaciones_Admin_"+z+"_Articulo_"+k+"_Solicitud_"+i)+"").equals("null")?"0":resumenAtencionesForm.getSolicitudes("observaciones_Admin_"+z+"_Articulo_"+k+"_Solicitud_"+i)+"";
			posVector++;
			datosAdministracion[posVector]=resumenAtencionesForm.getSolicitudes("fechaHora_Admin_"+z+"_Articulo_"+k+"_Solicitud_"+i)+""==null||(resumenAtencionesForm.getSolicitudes("fechaHora_Admin_"+z+"_Articulo_"+k+"_Solicitud_"+i)+"").equals("null")?"0":resumenAtencionesForm.getSolicitudes("fechaHora_Admin_"+z+"_Articulo_"+k+"_Solicitud_"+i)+"";
			posVector++;
			datosAdministracion[posVector]=resumenAtencionesForm.getSolicitudes("usuarionFechaHora_Admin_"+z+"_Articulo_"+k+"_Solicitud_"+i)+""==null||(resumenAtencionesForm.getSolicitudes("usuarionFechaHora_Admin_"+z+"_Articulo_"+k+"_Solicitud_"+i)+"").equals("null")?"0":resumenAtencionesForm.getSolicitudes("usuarionFechaHora_Admin_"+z+"_Articulo_"+k+"_Solicitud_"+i)+"";
			posVector++;
			datosAdministracion[posVector]=resumenAtencionesForm.getSolicitudes("traidoPaciente_Admin_"+z+"_Articulo_"+k+"_Solicitud_"+i)+""==null||(resumenAtencionesForm.getSolicitudes("traidoPaciente_Admin_"+z+"_Articulo_"+k+"_Solicitud_"+i)+"").equals("null")?"0":resumenAtencionesForm.getSolicitudes("traidoPaciente_Admin_"+z+"_Articulo_"+k+"_Solicitud_"+i)+"";
			posVector++;
		}
		String nomSeccionAdministracion="seccionAdmin_"+i+"_"+k;
		report.createColSection(nomSeccionAdministracion,cabecerasAdministracion,datosAdministracion,tamanioDatosCabecerasTablas,tamanioDatosCuerpoTablas,0);
		float widths[]={(float) 0.7,(float) 3.0,(float) 1.8,(float) 2.8,(float) 1.7};
		try
        {
			report.getSectionReference(nomSeccionAdministracion).getTableReference(PdfReports.REPORT_PARENT_TABLE).setWidths(widths);
		}
        catch (Exception e)
        {
        	e.getStackTrace();
        }
		report.addSectionToDocument(nomSeccionAdministracion);
	}




	/**
	 * Metodo para cargar las administraciones que se le han realizado a un medicamento.
	 * @param z, numero de administracion
	 * @param k, numero del articulo
	 * @param i, numero de solicitud
	 * @param resumenAtencionesForm, form con los tado
	 * @return String[], un vecto strin con los datos.
	 */
	private static String[] cargarAdministracion(ResumenAtencionesForm resumenAtencionesForm, int i, int k, int z) 
	{
		String[] admin={
				resumenAtencionesForm.getSolicitudes("cantidadArt_"+z+"_Articulo_"+k+"_Solicitud_"+i)+""==null||(resumenAtencionesForm.getSolicitudes("cantidadArt_"+z+"_Articulo_"+k+"_Solicitud_"+i)+"").equals("null")?"0":resumenAtencionesForm.getSolicitudes("cantidadArt_"+z+"_Articulo_"+k+"_Solicitud_"+i)+"",
				resumenAtencionesForm.getSolicitudes("fechaHora_Admin_"+z+"_Articulo_"+k+"_Solicitud_"+i)+""==null||(resumenAtencionesForm.getSolicitudes("fechaHora_Admin_"+z+"_Articulo_"+k+"_Solicitud_"+i)+"").equals("null")?"0":resumenAtencionesForm.getSolicitudes("fechaHora_Admin_"+z+"_Articulo_"+k+"_Solicitud_"+i)+"",
				resumenAtencionesForm.getSolicitudes("observaciones_Admin_"+z+"_Articulo_"+k+"_Solicitud_"+i)+""==null||(resumenAtencionesForm.getSolicitudes("observaciones_Admin_"+z+"_Articulo_"+k+"_Solicitud_"+i)+"").equals("null")?"0":resumenAtencionesForm.getSolicitudes("observaciones_Admin_"+z+"_Articulo_"+k+"_Solicitud_"+i)+"",
				resumenAtencionesForm.getSolicitudes("usuarionFechaHora_Admin_"+z+"_Articulo_"+k+"_Solicitud_"+i)+""==null||(resumenAtencionesForm.getSolicitudes("usuarionFechaHora_Admin_"+z+"_Articulo_"+k+"_Solicitud_"+i)+"").equals("null")?"0":resumenAtencionesForm.getSolicitudes("usuarionFechaHora_Admin_"+z+"_Articulo_"+k+"_Solicitud_"+i)+"",
				resumenAtencionesForm.getSolicitudes("traidoPaciente_Admin_"+z+"_Articulo_"+k+"_Solicitud_"+i)+""==null||(resumenAtencionesForm.getSolicitudes("traidoPaciente_Admin_"+z+"_Articulo_"+k+"_Solicitud_"+i)+"").equals("null")?"0":resumenAtencionesForm.getSolicitudes("traidoPaciente_Admin_"+z+"_Articulo_"+k+"_Solicitud_"+i)+""
				};
		return admin;
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

