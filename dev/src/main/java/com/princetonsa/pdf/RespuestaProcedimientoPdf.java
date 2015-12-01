/*
 * Created on Feb 9, 2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.pdf;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;

import com.lowagie.text.BadElementException;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.Medico;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.ordenesmedicas.solicitudes.Procedimiento;
import com.princetonsa.mundo.solicitudes.SolicitudProcedimiento;

/**
 * Clase para manejar la impresion de la respuesta a la solicitud de procedimientos
 * la consulta y la modificacion de los mismos 
 * @version 1.0, 14/01/2005
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 */
public class RespuestaProcedimientoPdf
{


    /**
     * Metodo para imprimir la respuesta de la solicitud de Procedimientos
     * @param string
     * @param solicitarForm
     * @param usuario
     */
    public static void pdfImprimirRespuesta(Connection con, String nombreArchivo, SolicitudProcedimiento solicitud, UsuarioBasico usu, PersonaBasica pacienteActivo, Procedimiento procedimiento) 
    {
        
        /**
         * Manejador de log de esta clase
         */
    	Logger logger = Logger.getLogger(RespuestaProcedimientoPdf.class);
        //variable para manejar el reporte
        PdfReports report = new PdfReports();
        try
		{
        	solicitud.cargar(con, solicitud.getNumeroSolicitud());
		}
        catch(SQLException e)
		{
        	logger.error("Error cargando el mundo de solicitudes "+e);
		}
        
        InstitucionBasica institucionBasica= new InstitucionBasica();
        institucionBasica.cargarXConvenio(usu.getCodigoInstitucionInt(), pacienteActivo.getCodigoConvenio());
        report.setUsuario(usu.getLoginUsuario());
        
        /**String[] servicioExterno=solicitud.consultarExterno(con,solicitud.getNumeroSolicitud()).split(ConstantesBD.separadorSplit);
        if(servicioExterno.length>1)
        {
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
            if(solicitud.getUrgente()==true)
            {
            	prioridad="Urgente";
            }
            else
            {
            	prioridad="Normal";
            }
            
            try
			{
            	/*Si la información de la cuenta no está cargada sesión debido a que el centro de atención del
            	 * usuario y del paciente son diferentes se vuelve a cargar la información
            	 *
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
            
            /*
            //variable para almacenar el reporte
            try
			{
				pacienteActivo.cargarEmpresaRegimen(con,pacienteActivo.getCodigoPersona(),pacienteActivo.getCodigoTipoRegimen()+"");
			}
			catch(SQLException e1)
			{
				logger.error("Error cargando el tipo de régimen para la solicitud de procedimientos "+e1);
			}*
            
			solicitud.getCodigoCuenta();
            String[] datosReporte=
            {
					pacienteActivo.getUltimaViaIngreso()+"",
					pacienteActivo.getCama()+"",
					pacienteActivo.getArea()+"",
					pacienteActivo.getNombreTipoRegimen(),
					pacienteActivo.getConvenioPersonaResponsable()+"",
					solicitud.getNumeroAutorizacion()+"",
					pacienteActivo.getArea()+"",
					"Externo",
					"Respondida",
					//solicitarForm.getIidi_estadoHistoriaClinica().getDescripcion(),
					UtilidadFecha.getFechaActual()+ "--"+UtilidadFecha.getHoraActual()+"",
					servicioExterno[1]+"",
					prioridad
            };
            //generacion de la cabecera.
            {
            		//tomando la ruta definida en el web.xml para generar el reporte.
                    String filePath=ValoresPorDefecto.getFilePath();
                    //titulo del reporte
                    String tituloReporte="ORDEN DE PROCEDIMIENTOS (" +UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() +")";
                    //generacion de la cabecera, validando el tipo de separador e utilizado en el path
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
           
//          Servicio externo solicitado
            report.font.setFontSizeAndAttributes(8,false,false,false);
            report.document.addParagraph("SERVICIO : "+servicioExterno[0], report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,20);

        		if(!servicioExterno[2].equals(""))
        		{
	        	    report.font.setFontSizeAndAttributes(8,true,false,false);
	              	report.document.addParagraph("COMENTARIOS ADICIONALES HISTORIA CLÌNICA: "+servicioExterno[2],report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,15);
        		}
        		if(!servicioExterno[3].equals(""))
        		{
			        report.font.setFontSizeAndAttributes(8,true,false,false);
		            report.document.addParagraph("RESULTADOS: "+servicioExterno[3],report.font, iTextBaseDocument.ALIGNMENT_JUSTIFIED,15);
        		}
        	
    		if(solicitud.getEstadoHistoriaClinica().getCodigo()==ConstantesBD.codigoEstadoHCSolicitada)
            {
         	   	String firmaDigital= Medico.obtenerFirmaDigitalMedico(solicitud.getCodigoMedicoSolicitante());
    				logger.info("\n\nFIRMA DIGITAL---->"+firmaDigital+" codigomedicosolicitante-->"+solicitud.getCodigoMedicoSolicitante());
       			if(!firmaDigital.equals(""))
       			{
       				report.document.addImage(ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+System.getProperty("FIRMADIGITAL")+System.getProperty("file.separator")+firmaDigital);
       				report.document.addParagraph(solicitud.getDatosMedico(),report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
       			}
            }	
        		
        }
        else
        {**/
		//String[] extra=procedimiento.cargarExtraProcedimiento(con,procedimiento.getNumeroSolicitud()).split(ConstantesBD.separadorSplit);
		
        //cabecera para la tabla del reporte
        String[] cabeceras = {
                "VÍA INGRESO:",
                "CAMA:",
                "ÁREA:",
                "No. INGRESO:",
                "No. CUENTA:",
                "RÉGIMEN:",
                "RESPONSABLE:",
                "C. COSTO SOLICITANTE:",
                "C. COSTO SOLICITADO:",
                "ESTADO:",
				"FECHA:",
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
			logger.error("Error cargando el tipo de régimen para la impresion de la solicitud de procedimientos "+e1);
		}
       
		/*
        //Para cargar el nombre del tipo de regimen debo traer la empresa del regimen
        try
		{
			pacienteActivo.cargarEmpresaRegimen(con,pacienteActivo.getCodigoPersona(),pacienteActivo.getCodigoTipoRegimen()+"");
		}
		catch(SQLException e1)
		{
			logger.error("Error cargando el tipo de régimen para la impresion de la solicitud de procedimientos "+e1);
		}*/
        
		//String fecha  = solicitud.getFechaSolicitud();
		
		//Según aclaraciones de Nury la fecha que se muestra debe ser la de solicitud, la de anulacón debe ir aparte
		/*if(solicitud.getEstadoHistoriaClinica().getCodigo()!=ConstantesBD.codigoEstadoHCAnulada)
		{
			fecha=procedimiento.getFechaEjecucion();
		}
		else
		{
			fecha=solicitud.getFechaGrabacion();
		}*/
		// solicitud.getNumeroAutorizacion()+  
        String[] datosReporte=
        {
				pacienteActivo.getUltimaViaIngreso()+"",
				pacienteActivo.getCama()+"",
				pacienteActivo.getArea()+"",
				pacienteActivo.getConsecutivoIngreso()+"",
				pacienteActivo.getCodigoCuenta()+"",
				pacienteActivo.getNombreTipoRegimen(),
				pacienteActivo.getConvenioPersonaResponsable()+"",
				solicitud.getCentroCostoSolicitante().getNombre()+"",
				solicitud.getCentroCostoSolicitado().getNombre()+"",
				solicitud.getEstadoHistoriaClinica().getNombre(),
				//procedimiento.getFechaEjecucion()+"",
				solicitud.getFechaSolicitud() + " - " + solicitud.getHoraSolicitud(),
				solicitud.getConsecutivoOrdenesMedicas()+"",
				prioridad
        };
        //generacion de la cabecera.
        {
        		//titulo del reporte
                String tituloReporte="ORDEN DE PROCEDIMIENTOS (" +UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() +")";
                
                
                
                
                /*---------------Modificacion por tarea 77564*/
                InfoDatosInt centroAten=UtilidadesHistoriaClinica.obtenerCentroAtencionCuenta(con, pacienteActivo.getCodigoCuenta());
                String direccion ="";
        	     HashMap criterios = new HashMap ();
        	     criterios.put("consecutivo", centroAten.getCodigo());
        	     HashMap tmp=Utilidades.obtenerDatosCentroAtencion(con, criterios);
        	     logger.info("\n centro aten -->"+centroAten);
               	if (UtilidadCadena.noEsVacio(tmp.get("direccion")+""))
               		direccion=tmp.get("direccion")+"";        				
               	else
               		direccion=institucionBasica.getDireccion();
               /*-----------------------------------------------------------*/
               
                
                //generacion de la cabecera, validando el tipo de separador e utilizado en el path
                report.setReportBaseHeaderDir(institucionBasica, institucionBasica.getNit(), tituloReporte,direccion);
        }
        //abrir reporte
        report.openReport(nombreArchivo);
        report.createSection("seccionDetalle","tablaDetalle",5,3,0);
        report.getSectionReference("seccionDetalle").setTableBorder("tablaDetalle", 0xFFFFFF, 0.0f);
        report.getSectionReference("seccionDetalle").setTableCellBorderWidth("tablaDetalle", 0.5f);
        report.getSectionReference("seccionDetalle").setTableCellsDefaultColors("tablaDetalle", 0xFFFFFF, 0xFFFFFF);
        report.font.setFontSizeAndAttributes(10,true,false,false);
        report.getSectionReference("seccionDetalle").setTableCellsColSpan(3);
        report.getSectionReference("seccionDetalle").addTableTextCellAligned("tablaDetalle", "PACIENTE  "+pacienteActivo.getNombrePersona(false)+"         "+pacienteActivo.getCodigoTipoIdentificacionPersona()+": "+pacienteActivo.getNumeroIdentificacionPersona()+"        "+ " EDAD: "+pacienteActivo.getEdadDetallada(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        report.getSectionReference("seccionDetalle").setTableCellsColSpan(1);
        for(int k=0; k<cabeceras.length; k++)
        {
            //Informacion del paciente
        	report.font.setFontSizeAndAttributes(8,false,false,false);
            report.getSectionReference("seccionDetalle").addTableTextCell("tablaDetalle", cabeceras[k]+" "+datosReporte[k], report.font);
        }
        //10 es el total y debe estar distribuido entre la cantidad de columnas que tengo
        float widths[]={(float) 3,(float) 3,(float) 4};
        try
		{
           
        	report.getSectionReference("seccionDetalle").getTableReference("tablaDetalle").setWidths(widths);
		}
        catch (BadElementException e)
		{
        	logger.error("Se presentó error generando el pdf  123123 " + e);
		}
        
        report.setReportDataSectionAtributes(0xFFFFFF, 0xFFFFFF, 0x000000,8);
        //definicion de fuente
        report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
        //definicion de atributos para el titulo de la seccion(tabla)
        report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000,8);
        //definicion de atributos para los datos de la seccion
        report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000,8);
        
        //variable para almacenar el reporte
        String[] datosReporte2=new String[6];
        //cabecera para la tabla del reporte
        String[] cabeceras2 = {
                "CÓDIGO", 
                "PROCEDIMIENTO",
                "ESPECIALIDAD", 
                "FINALIDAD",
                "CANTIDAD",
                "POS"
        };
       
        datosReporte2[0]=solicitud.getCodigoServicioSolicitado()+"";
        datosReporte2[1]=solicitud.getNombreServicioSolicitado();
        
        datosReporte2[2]=solicitud.getEspecialidadSolicitada().getNombre();
        /*Es necesaria la cantidad por standar de impresion, siempre es 1 en la respuesta
        de un procedimiento pues solo se da respuesta de uno en uno*/
        datosReporte2[3]=solicitud.getNombreFinalidad();
        datosReporte2[4]="1";
        datosReporte2[5]=solicitud.getUrgente()+"";
        datosReporte2[5]=procedimiento.getEsPos()+"";
        
        //comparar si tiene datos 
        if(datosReporte2.length == 0)
        {
            return;
        }
        //Comparacion para cambiar el "true" o el "false" por el "SI" o el "NO" respectivamente
        /**if(datosReporte2[5].equals("true"))
        {
        	datosReporte2[5]="SI";
        }
        else
        {
        	datosReporte2[5]="NO";
        }**/
        

        //definicion de fuente
        report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
        //definicion de atributos para el titulo de la seccion(tabla)
        report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000,8);
        //definicion de atributos para los datos de la seccion
        report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000,8);
        //creacion de la seccion o tabla
        report.createColSection("seccionprocedimientos", cabeceras2,datosReporte2, 8);
        
        //10 en total y debe estar divido en la cantidad de columnas que tengo
        float widths1[]={(float) 1,(float) 3.5,(float) 2.5,(float)1.5,(float) 1,(float) 0.5};
        try
		{
           
			report.getSectionReference("seccionprocedimientos").getTableReference("parentTable").setWidths(widths1);
		}
        catch (BadElementException e)
		{
        	logger.error("Se presentó error generando el pdf " + e);
		}
        
//              adicionar la seccion al reporte
        report.addSectionToDocument("seccionDetalle");
//              adicionar la seccion al reporte
        report.addSectionToDocument("seccionprocedimientos");
//              Comentarios
        report.font.setFontSizeAndAttributes(8,true,false,false);
        //if(!extra[0].equals("**")&&!extra[1].equals("**"))
        if(!solicitud.getComentario().equals(""))
        {
        		report.document.addParagraph("Observación por caso clínico:  "+solicitud.getComentario(),report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,15);
        }
        if(!solicitud.getJustificacionSolicitud().equals(""))
        {
        		report.document.addParagraph("Justificación Solicitud:  "+solicitud.getJustificacionSolicitud(),report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,15);
        }
        /**else if(extra[0].equals("**"))
        {
        	report.document.addParagraph("COMENTARIO:2 "+procedimiento.getComentarioHistoriaClinica(),report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,15);
        }**/
        
        
    	
		
//              Resultados
        //Si el estado es anulada no muestra este campo
       /*if (!extra[0].equals("**")/**&&extra[1].equals("**")**)
        {
        	report.font.setFontSizeAndAttributes(8,true,false,false);
            report.document.addParagraph("RESULTADOS: "+procedimiento.getResultados(),report.font, iTextBaseDocument.ALIGNMENT_JUSTIFIED,15);
        }*/
       
       if(solicitud.getEstadoHistoriaClinica().getCodigo()==ConstantesBD.codigoEstadoHCSolicitada)
       {
    	   	String firmaDigital= Medico.obtenerFirmaDigitalMedico(solicitud.getCodigoMedicoSolicitante());
			logger.info("\n\nFIRMA DIGITAL---->"+firmaDigital+" codigomedicosolicitante-->"+solicitud.getCodigoMedicoSolicitante());
   			if(!firmaDigital.equals(""))
   			{
   				report.document.addImageWithSize(ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+System.getProperty("FIRMADIGITAL")+System.getProperty("file.separator")+firmaDigital, 200L, 80L);
   				report.document.addParagraph(solicitud.getDatosMedico(),report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
   				report.document.addParagraph(institucionBasica.getPieHistoriaClinica(),report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
   			}
       }
       
       /**
        else if (!extra[1].equals("**"))
    	{
            report.font.setFontSizeAndAttributes(8,true,false,false);
            report.document.addParagraph("RESULTADOS: "+extra[1],report.font, iTextBaseDocument.ALIGNMENT_JUSTIFIED,15);
    	}
        else if (extra[1].equals("**"))
        {
        	report.font.setFontSizeAndAttributes(8,true,false,false);
            report.document.addParagraph("RESULTADOS: "+procedimiento.getResultados(),report.font, iTextBaseDocument.ALIGNMENT_JUSTIFIED,15);
        }**/
        
        
        
        if(solicitud.getEstadoHistoriaClinica().getCodigo()!=ConstantesBD.codigoEstadoHCSolicitada || solicitud.getEstadoHistoriaClinica().getCodigo()==ConstantesBD.codigoEstadoHCInterpretada || solicitud.getEstadoHistoriaClinica().getCodigo()==ConstantesBD.codigoEstadoHCAnulada)
        {
        
        	
        	//Respuesta del procedimiento 
        	/**if (solicitud.getEstadoHistoriaClinica().getCodigo()!=ConstantesBD.codigoEstadoHCAnulada && !extra[1].equals("**"))
        	{
            	report.font.setFontSizeAndAttributes(10,true,false,false);
                report.document.addParagraph("RESPUESTA:"+extra[1],report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,25);
        	}	**
            DocumentosAdjuntos documentosAdjuntos=procedimiento.getDocumentosAdjuntos();
            //cargo el documentosAdjuntos con la funcion de cargarDocumentosAdjuntos
            documentosAdjuntos.cargarDocumentosAdjuntos(con, procedimiento.getNumeroSolicitud(), false, "");
            
			String documentosAdjuntosStr="";
			
			//Tomo el nombre del documento adjunto y lo almaceno en una cadena
			for(int i=0; i<documentosAdjuntos.getNumDocumentosAdjuntos(); i++)
			{
				DocumentoAdjunto documento=documentosAdjuntos.getDocumentoAdjunto(i);
				documentosAdjuntosStr+=(i+1)+". "+documento.getNombreOriginal()+"  ";
			}
			
			//Documentos adjuntos
			if(!documentosAdjuntosStr.equals(""))
			{
				report.font.setFontSizeAndAttributes(8,true,false,false);
				report.document.addParagraph("DOCUMENTOS ADJUNTOS:   " +documentosAdjuntosStr,report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,15);
			}
			
			//Comentarios adicionales
	        report.font.setFontSizeAndAttributes(8,true,false,false);
            if(procedimiento.getComentarioHistoriaClinica()!=null)
            {
            	if(!procedimiento.getComentarioHistoriaClinica().equals(""))
            	{
            		report.document.addParagraph("COMENTARIOS ADICIONALES HISTORIA CLÌNICA: "+procedimiento.getComentarioHistoriaClinica(),report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,15);
            	}
            }

           //Diagnosticos de la Respuesta
            /**
            report.createSection("diagnosticos","tablaDiagnosticos",1,2,0);
    	    report.getSectionReference("diagnosticos").getTableReference("tablaDiagnosticos").setCellpadding(0.0f);
            report.getSectionReference("diagnosticos").getTableReference("tablaDiagnosticos").setCellspacing(0.0f);
            report.getSectionReference("diagnosticos").setTableBorder("tablaDiagnosticos", 0xFFFFFF, 0.0f);
            report.getSectionReference("diagnosticos").setTableCellBorderWidth("tablaDiagnosticos", 0.2f);
            report.getSectionReference("diagnosticos").setTableCellsDefaultColors("tablaDiagnosticos", 0xFFFFFF, 0xFFFFFF);
            float widths2[]={(float) 2.5,(float) 7.5};
    		try 
    		{
    			report.getSectionReference("diagnosticos").getTableReference("tablaDiagnosticos").setWidths(widths2);
    		}
    		catch (BadElementException e) 
    		{
    			e.printStackTrace();
    		}
            
            HashMap mapaDiagnosticos=procedimiento.getDiagnosticos();
            //Diagnositco principal
            if(mapaDiagnosticos.get("acronimoPrincipal")!=null&&!mapaDiagnosticos.get("acronimoPrincipal").toString().equals(""))
            {
            	report.font.setFontSizeAndAttributes(8,true,false,false);
                report.getSectionReference("diagnosticos").setTableCellsColSpan(1);
                report.getSectionReference("diagnosticos").addTableTextCellAligned("tablaDiagnosticos", "Dx PRINCIPAL:", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            	report.font.setFontSizeAndAttributes(8,false,false,false);
                report.getSectionReference("diagnosticos").addTableTextCellAligned("tablaDiagnosticos", mapaDiagnosticos.get("acronimoPrincipal")+"-"+mapaDiagnosticos.get("tipoCiePrincipal")+"-"+mapaDiagnosticos.get("nombrePrincipal"), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            }
            
            //Diagnostico de complicacion
            if(mapaDiagnosticos.get("acronimoComplicacion")!=null&&!mapaDiagnosticos.get("acronimoComplicacion").toString().equals(""))
            {
            	report.font.setFontSizeAndAttributes(8,true,false,false);
                report.getSectionReference("diagnosticos").setTableCellsColSpan(1);
                report.getSectionReference("diagnosticos").addTableTextCellAligned("tablaDiagnosticos", "Dx DE COMPLICACIÓN:", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            	report.font.setFontSizeAndAttributes(8,false,false,false);
                report.getSectionReference("diagnosticos").addTableTextCellAligned("tablaDiagnosticos", mapaDiagnosticos.get("acronimoComplicacion")+"-"+mapaDiagnosticos.get("tipoCieComplicacion")+"-"+mapaDiagnosticos.get("nombreComplicacion"), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            }
            if(procedimiento.getNumDiagnosticos() > 0)
            {
            	report.font.setFontSizeAndAttributes(8,true,false,false);
                report.getSectionReference("diagnosticos").setTableCellsColSpan(1);
                report.getSectionReference("diagnosticos").addTableTextCellAligned("tablaDiagnosticos", "Dx RELACIONADOS:", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
                report.font.setFontSizeAndAttributes(8,false,false,false);
                for (int i = 0; i < procedimiento.getNumDiagnosticos(); i++)
                {
                	report.getSectionReference("diagnosticos").addTableTextCellAligned("tablaDiagnosticos", mapaDiagnosticos.get("acronimoRel_"+i)+"-"+mapaDiagnosticos.get("tipoCieRel_"+i)+"-"+mapaDiagnosticos.get("nombreRel_"+i), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
                	report.getSectionReference("diagnosticos").addTableTextCellAligned("tablaDiagnosticos", "", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
                }
            }
            
            report.addSectionToDocument("diagnosticos");*/
            
            
            //Observaciones
            /**report.font.setFontSizeAndAttributes(8,true,false,false);
            if(!procedimiento.getObservaciones().equals(""))
            {
            	report.document.addParagraph("OBSERVACIONES: "+procedimiento.getObservaciones(),report.font, iTextBaseDocument.ALIGNMENT_JUSTIFIED,15);
            }**/

            //Si el estado es interpretada muestra todo lo anterior mas este campo si no es null
            /**if(solicitud.getEstadoHistoriaClinica().getCodigo()==ConstantesBD.codigoEstadoHCInterpretada)
            {
                //Interpretacion
                report.font.setFontSizeAndAttributes(8,true,false,false);
	            if(solicitud.getInterpretacion()!=null)
	            {
	            	report.document.addParagraph("INTERPRETACION: "+solicitud.getInterpretacion(),report.font, iTextBaseDocument.ALIGNMENT_JUSTIFIED,15);
	            }
            }**/
            
            //Si el estado es anulada muestra todo lo anterior mas este campo si no es null
            if(solicitud.getEstadoHistoriaClinica().getCodigo()==ConstantesBD.codigoEstadoHCAnulada)
            {
                //Anulacion de un servicio antes de ser interpretado
                if(solicitud.getMotivoAnulacion()!=null)
                {
                    report.font.setFontSizeAndAttributes(8,true,false,false);
                	report.document.addParagraph(usu.getInformacionGeneralPersonalSalud(),report.font, iTextBaseDocument.ALIGNMENT_JUSTIFIED,15);
                    report.document.addParagraph("\n SOLICITUD ANULADA MOTIVO:   " + solicitud.getMotivoAnulacion() +
                    							 "\n FECHA ANULACIÓN:   " + solicitud.getFechaAnulacion() + " - " + solicitud.getHoraAnulacion() +
                    							 "\n MÉDICO ANULA:   " + Medico.obtenerApellidosNombresPersona(con, solicitud.getCodigoMedicoAnulacion()),
                    							 report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,15);
                }
            
            }
        }
           
         //cerrando el reporte
         report.closeReport();
	    }
}