/*
 * Created on Feb 7, 2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.pdf;

import java.sql.Connection;
import java.sql.SQLException;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.pdf.PdfReports;
import util.ValoresPorDefecto;
import util.pdf.iTextBaseFont;
import org.apache.log4j.Logger;
import util.pdf.iTextBaseDocument;

import com.princetonsa.actionform.ordenesmedicas.interconsultas.ListarSolicitudesForm;
import com.princetonsa.actionform.ordenesmedicas.interconsultas.SolicitarForm;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.Medico;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.solicitudes.SolicitudProcedimiento;
import com.lowagie.text.BadElementException;

/**
 * Clase para manejar la impresion de la solicitud de procedimientos 
 * @version 1.0, 14/01/2005
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 */
public class ResumenGuardarProcedimientoPdf
{

	private static Logger logger = Logger.getLogger(ResumenGuardarProcedimientoPdf.class);
	
    /**
     * Metodo para imprimir el resumen de la solicitud de Procedimientos
     * @param string
     * @param solicitarForm
     * @param usuario
     */
    public static void pdfImprimirResumen(Connection con, String nombreArchivo,SolicitudProcedimiento solicitud, UsuarioBasico usu, PersonaBasica pacienteActivo,boolean usarCuentaActiva, InstitucionBasica institucionActual)
    {
        
        
        //variable para manejar el reporte
        PdfReports report = new PdfReports();
                //cabecera para la tabla del reporte
                String[] cabeceras = 
                {
                        "VÍA INGRESO:",
                        "CAMA:",
                        "ÁREA:",
                        "RÉGIMEN:",
                        "RESPONSABLE:",
                        "No. AUTORIZACIÓN:",
                        "C. COSTO SOLICITANTE:",
                        //"C. COSTO SOLICITADO:",
                        "ESTADO:",
						"FECHA-HORA:",
						"No. ORDEN:",
						"PRIORIDAD:"
                };
                
                String prioridad="";
                if(solicitud.getUrgente()==true)
                {
                	prioridad="Urgente";
                }
                else
                {
                	prioridad="Normal";
                }
                //variable para almacenar el reporte
                try
				{
                	/*Si la información de la cuenta no está cargada sesión debido a que el centro de atención del
                	 * usuario y del paciente son diferentes se vuelve a cargar la información
                	 */ 
                	if(!UtilidadCadena.noEsVacio(pacienteActivo.getCodigoTipoRegimen()+""))
                	{
                		               	
	                	Cuenta cuenta=new Cuenta();
	                	cuenta.cargarCuenta(con, pacienteActivo.getCodigoPersona()+"");
	                	
	                	int centroAtencion=cuenta.getCodigoCentroAtencion();
	                	                	                	
	                	pacienteActivo.cargarPaciente2(con, pacienteActivo.getCodigoPersona(), pacienteActivo.getTipoInstitucion(), centroAtencion+"");
                	}
                	
					pacienteActivo.cargarEmpresaRegimen(con,pacienteActivo.getCodigoPersona(),pacienteActivo.getCodigoTipoRegimen()+"");
				}
				catch(SQLException e1)
				{
					logger.error("Error cargando el tipo de régimen para la solicitud de procedimientos "+e1);
				}
				
				// solicitud.getNumeroAutorizacion()+
                String[] datosReporte=
                {
						pacienteActivo.getUltimaViaIngreso()+"",
						pacienteActivo.getCama()+"",
						pacienteActivo.getArea()+"",
						pacienteActivo.getNombreTipoRegimen(),
						pacienteActivo.getConvenioPersonaResponsable()+"",
						"",
						solicitud.getCentroCostoSolicitante().getNombre()+"",
						//solicitud.getCentroCostoSolicitado().getNombre()+"",
						solicitud.getEstadoHistoriaClinica().getNombre(),
						solicitud.getFechaSolicitud()+ "--"+solicitud.getHoraSolicitud()+"",
						solicitud.getConsecutivoOrdenesMedicas()+"",
						prioridad
                };
                //generacion de la cabecera.
                {
                		//titulo del reporte
                        String tituloReporte="ORDEN DE PROCEDIMIENTOS (" +UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() +")";
                        
                        /*InstitucionBasica institucionBasica= new InstitucionBasica();
                        institucionBasica.cargarXConvenio(usu.getCodigoInstitucionInt(), pacienteActivo.getCodigoConvenio());*/
                        
                        //generacion de la cabecera, validando el tipo de separador e utilizado en el path
                        report.setReportBaseHeader1(institucionActual, institucionActual.getNit(), tituloReporte);
                }
                //abrir reporte
                report.openReport(nombreArchivo);
                
                report.createSection("seccionDetalle","tablaDetalle",5,3,0);
                report.getSectionReference("seccionDetalle").setTableBorder("tablaDetalle", 0xFFFFFF, 0.0f);
                report.getSectionReference("seccionDetalle").setTableCellBorderWidth("tablaDetalle", 0.5f);
                report.getSectionReference("seccionDetalle").setTableCellsDefaultColors("tablaDetalle", 0xFFFFFF, 0xFFFFFF);
                report.font.setFontSizeAndAttributes(10,true,false,false);
                report.getSectionReference("seccionDetalle").setTableCellsColSpan(3);
                report.getSectionReference("seccionDetalle").addTableTextCellAligned("tablaDetalle", "PACIENTE    "+pacienteActivo.getNombrePersona(false)+"         "+pacienteActivo.getCodigoTipoIdentificacionPersona()+": "+pacienteActivo.getNumeroIdentificacionPersona()+"        "+ " EDAD: "+pacienteActivo.getEdadDetallada(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
                report.getSectionReference("seccionDetalle").setTableCellsColSpan(1);
                for(int k=0; k<cabeceras.length; k++)
                {
                    //La informacion del paciente
                	report.font.setFontSizeAndAttributes(8,false,false,false);
                    report.getSectionReference("seccionDetalle").addTableTextCell("tablaDetalle", cabeceras[k]+" "+datosReporte[k], report.font);
                }
                report.setReportDataSectionAtributes(0xFFFFFF, 0xFFFFFF, 0x000000,8);
                //definicion de fuente
                report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
                //definicion de atributos para el titulo de la seccion(tabla)
                report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000,8);
                //definicion de atributos para los datos de la seccion
                report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000,8);
                //variable para almacenar el reporte
                String[] datosReporte2;
                //cabecera para la tabla del reporte
                String[] cabeceras2 = {
                        "CÓDIGO", 
                        "PROCEDIMIENTO", 
                        "FINALIDAD",
                        "CENTRO COSTO",
                        "FRECUENCIA",
                        "CANTIDAD",
                        "POS",
                        "MULTIPLE"
                };
                //nombre de las columna para tomar los datos de la coleccion
                String[] nombreColumnas =  {
                        "codigo", 
                        "procedimiento",
                        "finalidad",
                        "centro_costo", 
                        "frecuencia",
                        "cantidad",
                        "pos",
                        "multiple"
                };
                /*tomar los datos de la coleccion y pasarlos al string de datos, si trae valores
                boolean los pasa por SI o NO, los dos ultimos parametros que se le pasan a la funcion*/
                if(usarCuentaActiva)
                	datosReporte2= report.getDataFromCollectionModificarBooleanos(nombreColumnas,solicitud.solicitudesXCuentaFechaHora(con, pacienteActivo.getCodigoCuenta(),solicitud.getFechaSolicitud(),solicitud.getHoraSolicitud(), usu.getCodigoInstitucionInt()), "SI", "NO");
                else
                	datosReporte2= report.getDataFromCollectionModificarBooleanos(nombreColumnas,solicitud.solicitudesXCuentaFechaHora(con, solicitud.getCodigoCuenta(),solicitud.getFechaSolicitud(),solicitud.getHoraSolicitud(), usu.getCodigoInstitucionInt()), "SI", "NO");
                //comparar si tiene datos 
                if(datosReporte2.length == 0)
                {
                	logger.error("LA CONSULTA DE PROCEDIMIENTOS, NO RETORNO RESULTADOS");
                	report.closeReport();
                	UtilidadBD.closeConnection(con);
                    return;
                }
                //definicion de fuente
                report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
                //definicion de atributos para el titulo de la seccion(tabla)
                report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000,8);
                //definicion de atributos para los datos de la seccion
                report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000,8);
                //creacion de la seccion o tabla
                report.createColSection("seccionprocedimientos", cabeceras2,datosReporte2, 8);
                
                //10 el total y debe estar dividido en el total de columnas que tengo
                float widths1[]={(float) 1,(float) 2.3,(float)1.5,(float) 1.5,(float) 1.2,(float) 1,(float) 0.5, (float)1};
                try
				{
        			report.getSectionReference("seccionprocedimientos").getTableReference("parentTable").setWidths(widths1);
				}
                catch (BadElementException e)
				{
                	logger.error("Se presentó error generando el pdf " + e);
				}
                
            	//adicionar la seccion al reporte
                report.addSectionToDocument("seccionDetalle");
        		//adicionar la seccion al reporte
                report.addSectionToDocument("seccionprocedimientos");
                
                //Comentarios
                report.font.setFontSizeAndAttributes(8,true,false,false);
                if(!solicitud.getComentario().equals(""))
                {
                	report.document.addParagraph("Observación por Caso Clínico: "+solicitud.getComentario(),report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,20);
                }
                
                String firmaDigital= Medico.obtenerFirmaDigitalMedico(solicitud.getCodigoMedicoSolicitante());
    			logger.info("\n\nFIRMA DIGITAL---->"+firmaDigital+" codigomedicosolicitante-->"+solicitud.getCodigoMedicoSolicitante());
    			if(!firmaDigital.equals(""))
    			{
    				report.document.addImage(ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+System.getProperty("FIRMADIGITAL")+System.getProperty("file.separator")+firmaDigital);
    			}
            	//Si el comentario es vacío solo pone la información general del profesional de la salud
            	//@todo Implementar los datos del médico cuando se estén insertando en la BD
            	report.document.addParagraph(solicitud.getDatosMedico(),report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,20);
            	
            	
            	if(solicitud.getEstadoHistoriaClinica().getNombre().trim().equals("Anulada")){

            		String datosMedicoAnula;
            		try {
            			datosMedicoAnula = solicitud.consultarDatosMedicoAnulacion(con, solicitud.getCodigoMedicoAnulacion());

            			report.document.addParagraph(" ",report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
            			report.document.addParagraph("Orden ANULADA "+solicitud.getFechaAnulacion()+" "+solicitud.getHoraAnulacion(),report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
            			report.document.addParagraph("Motivo: "+solicitud.getMotivoAnulacion(),report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
            			report.document.addParagraph(datosMedicoAnula,report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);

            		} catch (SQLException e) {
            			logger.error("error en consulta de datos del medico que anula "+e.getMessage());
            		}
            	}
            	
            	//report.document.addParagraph(usu.getInformacionGeneralPersonalSalud(),report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,20);
                //cerrando el reporte
                report.closeReport(); 
    }
    
    
    /**
     * Metodo para imprimir una solicitud de procedimiento externo
     * @param con
     * @param nombreArchivo
     * @param solicitud
     * @param usu
     * @param pacienteActivo
     */
    public static void pdfImprimirExterno(Connection con, String nombreArchivo, SolicitudProcedimiento solicitud, UsuarioBasico usu, PersonaBasica pacienteActivo, SolicitarForm solicitarForm) 
    {
        //variable para manejar el reporte
        PdfReports report = new PdfReports();
                //cabecera para la tabla del reporte
               String[] cabeceras = {
                        "VÍA INGRESO:",
                        "CAMA:",
                        "ÁREA:",
                        "RÉGIMEN:",
                        "RESPONSABLE:",
                        "No. AUTORIZACIÓN:",
                        "C. COSTO SOLICITANTE:",
                        "C. COSTO SOLICITADO:",
                        "ESTADO:",
						"FECHA-HORA:",
						"No. ORDEN:",
						"PRIORIDAD:"
                };
                
                String prioridad="";
                if(solicitarForm.isUrgenteOtros()==true)
                {
                	prioridad="Urgente";
                }
                else
                {
                	prioridad="Normal";
                }
                //variable para almacenar el reporte
                try
				{
                	/*Si la información de la cuenta no está cargada sesión debido a que el centro de atención del
                	 * usuario y del paciente son diferentes se vuelve a cargar la información
                	 */ 
                	if(!UtilidadCadena.noEsVacio(pacienteActivo.getCodigoTipoRegimen()+""))
                	{
                	
                	Cuenta cuenta=new Cuenta();
                	cuenta.cargarCuenta(con, pacienteActivo.getCodigoPersona()+"");
                	
                	int centroAtencion=cuenta.getCodigoCentroAtencion();
                	                	                	
                	pacienteActivo.cargarPaciente2(con, pacienteActivo.getCodigoPersona(), pacienteActivo.getTipoInstitucion(), centroAtencion+"");
                	}
                	
					pacienteActivo.cargarEmpresaRegimen(con,pacienteActivo.getCodigoPersona(),pacienteActivo.getCodigoTipoRegimen()+"");
				}
				catch(SQLException e1)
				{
					logger.error("Error cargando el tipo de régimen para la solicitud de procedimientos "+e1);
				}
                
				solicitud.getCodigoCuenta();
                String[] datosReporte=
                {
						pacienteActivo.getUltimaViaIngreso()+"",
						pacienteActivo.getCama()+"",
						pacienteActivo.getArea()+"",
						pacienteActivo.getNombreTipoRegimen(),
						pacienteActivo.getConvenioPersonaResponsable()+"",
						"",
						pacienteActivo.getArea()+"",
						//solicitarForm.getCentroCostoSolicitante()+"",
						"Externo",
						"Solicitada",
						//solicitarForm.getIidi_estadoHistoriaClinica().getDescripcion(),
						UtilidadFecha.getFechaActual()+ "--"+UtilidadFecha.getHoraActual()+"",
						solicitarForm.getConsecutivoOrdenesMedicas()+"",
						prioridad
                };
                //generacion de la cabecera.
                {
                		//titulo del reporte
                        String tituloReporte="ORDEN DE PROCEDIMIENTOS (" +UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() +")";
                        
                        InstitucionBasica institucionBasica= new InstitucionBasica();
                        institucionBasica.cargarXConvenio(usu.getCodigoInstitucionInt(), pacienteActivo.getCodigoConvenio());
                        
                        //generacion de la cabecera, validando el tipo de separador e utilizado en el path
                        report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), tituloReporte);
                        
                }
                //abrir reporte
                report.openReport(nombreArchivo);
                
                report.createSection("seccionDetalle","tablaDetalle",5,3,0);
                report.getSectionReference("seccionDetalle").setTableBorder("tablaDetalle", 0xFFFFFF, 0.0f);
                report.getSectionReference("seccionDetalle").setTableCellBorderWidth("tablaDetalle", 0.5f);
                report.getSectionReference("seccionDetalle").setTableCellsDefaultColors("tablaDetalle", 0xFFFFFF, 0xFFFFFF);
                report.font.setFontSizeAndAttributes(10,true,false,false);
                report.getSectionReference("seccionDetalle").setTableCellsColSpan(3);
                report.getSectionReference("seccionDetalle").addTableTextCellAligned("tablaDetalle", "PACIENTE    "+pacienteActivo.getNombrePersona(false)+"         "+pacienteActivo.getCodigoTipoIdentificacionPersona()+": "+pacienteActivo.getNumeroIdentificacionPersona()+"        "+ " EDAD: "+pacienteActivo.getEdadDetallada(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
                report.getSectionReference("seccionDetalle").setTableCellsColSpan(1);
                for(int k=0; k<cabeceras.length; k++)
                {
                    //La informacion del paciente
                	report.font.setFontSizeAndAttributes(8,false,false,false);
                    report.getSectionReference("seccionDetalle").addTableTextCell("tablaDetalle", cabeceras[k]+" "+datosReporte[k], report.font);
                }
                report.setReportDataSectionAtributes(0xFFFFFF, 0xFFFFFF, 0x000000,8);
                //definicion de fuente
                report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
                //definicion de atributos para el titulo de la seccion(tabla)
                report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000,8);
                //definicion de atributos para los datos de la seccion
                report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000,8);

            	//adicionar la seccion al reporte
                report.addSectionToDocument("seccionDetalle");
               
                
                report.font.setFontSizeAndAttributes(8,false,false,false);
                report.document.addParagraph("SERVICIO: "+solicitarForm.getOtroServicio(), report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,20);

                //Comentarios
                report.font.setFontSizeAndAttributes(8,true,false,false);
                if(solicitarForm.getComentario()!=null)
                {
	                if(!solicitarForm.getComentario().equals(""))
	                {
	                	report.font.setFontSizeAndAttributes(8,false,false,false);
	                	report.document.addParagraph("Observación por caso clínico: "+solicitarForm.getComentario(),report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,20);
	                	report.font.setFontSizeAndAttributes(8,true,false,false);
	                	report.document.addParagraph(usu.getInformacionGeneralPersonalSalud(),report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,20);
	                }
	                else
	                {
	                	//Si el comentario es vacío solo pone la información general del profesional de la salud
	                	//@todo Implementar los datos del médico cuando se estén insertando en la BD
	                	//report.document.addParagraph(solicitud.getDatosMedico(),report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,20);
	                	report.document.addParagraph(usu.getInformacionGeneralPersonalSalud(),report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,20);
	                }
                }
                //cerrando el reporte
                report.closeReport(); 
    }
    
    /**
     * Método para la impresion cuando es en la consulta de procedimientos que sean externos con servico externo
     * @param con
     * @param nombreArchivo
     * @param solicitud
     * @param usu
     * @param pacienteActivo
     * @param solicitarForm
     * @param listarSolicitudesform
     */
    public static void pdfImprimirExterno2(Connection con, String nombreArchivo,SolicitudProcedimiento solicitud, UsuarioBasico usu, PersonaBasica pacienteActivo, SolicitarForm solicitarForm, ListarSolicitudesForm listarSolicitudesform) 
    {
        //variable para manejar el reporte
        PdfReports report = new PdfReports();
                //cabecera para la tabla del reporte
               String[] cabeceras = {
                        "VÍA INGRESO:",
                        "CAMA:",
                        "ÁREA:",
                        "RÉGIMEN:",
                        "RESPONSABLE:",
                        "C. COSTO SOLICITANTE:",
                        "C. COSTO SOLICITADO:",
                        "ESTADO:",
						"FECHA-HORA:",
						"No. ORDEN:",
						"PRIORIDAD:"
                };
                
                String prioridad="";
                if(solicitarForm.isUrgenteOtros()==true)
                {
                	prioridad="Urgente";
                }
                else
                {
                	prioridad="Normal";
                }
                //variable para almacenar el reporte
                try
				{
                	/*Si la información de la cuenta no está cargada sesión debido a que el centro de atención del
                	 * usuario y del paciente son diferentes se vuelve a cargar la información
                	 */ 
                	if(!UtilidadCadena.noEsVacio(pacienteActivo.getCodigoTipoRegimen()+""))
                	{
                	
                	Cuenta cuenta=new Cuenta();
                	cuenta.cargarCuenta(con, pacienteActivo.getCodigoPersona()+"");
                	
                	int centroAtencion=cuenta.getCodigoCentroAtencion();
                	                	                	
                	pacienteActivo.cargarPaciente2(con, pacienteActivo.getCodigoPersona(), pacienteActivo.getTipoInstitucion(), centroAtencion+"");
                	}
                	
                	pacienteActivo.cargarEmpresaRegimen(con,pacienteActivo.getCodigoPersona(),pacienteActivo.getCodigoTipoRegimen()+"");
				}
				catch(SQLException e1)
				{
					logger.error("Error cargando el tipo de régimen para la solicitud de procedimientos "+e1);
				}
                
				//String numeroAutorizacion="";				
				
				String[] servicioExterno=solicitud.consultarExterno(con,listarSolicitudesform.getNumeroSolicitud()).split(ConstantesBD.separadorSplit);
				String estadoSolicitud="";
				if(listarSolicitudesform.getEstadoHistoriaClinicaResumen().equals("1"))
				{
					estadoSolicitud="Solicitada";
				}
				else if(listarSolicitudesform.getEstadoHistoriaClinicaResumen().equals("2"))
				{
					estadoSolicitud="Respondida";
				}
				else if(listarSolicitudesform.getEstadoHistoriaClinicaResumen().equals("3"))
				{
					estadoSolicitud="Interpretada";
				}
				else if(listarSolicitudesform.getEstadoHistoriaClinicaResumen().equals("4"))
				{
					estadoSolicitud="Anulada";
				}
				else if(listarSolicitudesform.getEstadoHistoriaClinicaResumen().equals("5"))
				{
					estadoSolicitud="Despachada";
				}
				else if(listarSolicitudesform.getEstadoHistoriaClinicaResumen().equals("6"))
				{
					estadoSolicitud="Admisitrada";
				}
				else if(listarSolicitudesform.getEstadoHistoriaClinicaResumen().equals("7"))
				{
					estadoSolicitud="Cargo Directo";
				}
				
                String[] datosReporte=
                {
						pacienteActivo.getUltimaViaIngreso()+"",
						pacienteActivo.getCama()+"",
						pacienteActivo.getArea()+"",
						pacienteActivo.getNombreTipoRegimen(),
						pacienteActivo.getConvenioPersonaResponsable()+"",
						pacienteActivo.getArea()+"",
						"Externo",
						estadoSolicitud+"",
						solicitud.getFechaSolicitud()+"--"+solicitud.getHoraSolicitud()+"",
						servicioExterno[1]+"",
						prioridad
                };
                //generacion de la cabecera.
                {
                		//titulo del reporte
                        String tituloReporte="ORDEN DE PROCEDIMIENTOS (" +UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() +")";
                        
                        InstitucionBasica institucionBasica= new InstitucionBasica();
                        institucionBasica.cargarXConvenio(usu.getCodigoInstitucionInt(), pacienteActivo.getCodigoConvenio());
                        
                        //generacion de la cabecera, validando el tipo de separador e utilizado en el path
                        report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), tituloReporte);
                }
                //abrir reporte
                report.openReport(nombreArchivo);
                
                report.createSection("seccionDetalle","tablaDetalle",5,3,0);
                report.getSectionReference("seccionDetalle").setTableBorder("tablaDetalle", 0xFFFFFF, 0.0f);
                report.getSectionReference("seccionDetalle").setTableCellBorderWidth("tablaDetalle", 0.5f);
                report.getSectionReference("seccionDetalle").setTableCellsDefaultColors("tablaDetalle", 0xFFFFFF, 0xFFFFFF);
                report.font.setFontSizeAndAttributes(10,true,false,false);
                report.getSectionReference("seccionDetalle").setTableCellsColSpan(3);
                report.getSectionReference("seccionDetalle").addTableTextCellAligned("tablaDetalle", "PACIENTE    "+pacienteActivo.getNombrePersona(false)+"         "+pacienteActivo.getCodigoTipoIdentificacionPersona()+": "+pacienteActivo.getNumeroIdentificacionPersona()+"        "+ " EDAD: "+pacienteActivo.getEdadDetallada(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
                report.getSectionReference("seccionDetalle").setTableCellsColSpan(1);
                for(int k=0; k<cabeceras.length; k++)
                {
                    //La informacion del paciente
                	report.font.setFontSizeAndAttributes(8,false,false,false);
                    report.getSectionReference("seccionDetalle").addTableTextCell("tablaDetalle", cabeceras[k]+" "+datosReporte[k], report.font);
                }
                report.setReportDataSectionAtributes(0xFFFFFF, 0xFFFFFF, 0x000000,8);
                //definicion de fuente
                report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
                //definicion de atributos para el titulo de la seccion(tabla)
                report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000,8);
                //definicion de atributos para los datos de la seccion
                report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000,8);

            	//adicionar la seccion al reporte
                report.addSectionToDocument("seccionDetalle");
               
                
                
                //Servicio externo solicitado
                report.font.setFontSizeAndAttributes(8,false,false,false);
                report.document.addParagraph("SERVICIO : "+servicioExterno[0], report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,20);

                //Comentarios
                report.font.setFontSizeAndAttributes(8,true,false,false);
                if(solicitarForm.getComentario()!=null)
                {
	                if(!solicitarForm.getComentario().equals(""))
	                {
	                	report.font.setFontSizeAndAttributes(8,false,false,false);
	                	report.document.addParagraph("Observación por caso clínico: "+solicitarForm.getComentario(),report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,20);
	                	report.font.setFontSizeAndAttributes(8,true,false,false);
	                	report.document.addParagraph(usu.getInformacionGeneralPersonalSalud(),report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,20);
	                }
	                else
	                {
	                	//Si el comentario es vacío solo pone la información general del profesional de la salud
	                	//@todo Implementar los datos del médico cuando se estén insertando en la BD
	                	//report.document.addParagraph(solicitud.getDatosMedico(),report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,20);
	                	report.document.addParagraph(usu.getInformacionGeneralPersonalSalud(),report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,20);
	                }
                }
                else
                {
                	//Si el comentario es vacío solo pone la información general del profesional de la salud
                	//@todo Implementar los datos del médico cuando se estén insertando en la BD
                	//report.document.addParagraph(solicitud.getDatosMedico(),report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,20);
                	report.document.addParagraph(usu.getInformacionGeneralPersonalSalud(),report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,20);
                }
                //cerrando el reporte
                report.closeReport(); 
    }
    
    
}