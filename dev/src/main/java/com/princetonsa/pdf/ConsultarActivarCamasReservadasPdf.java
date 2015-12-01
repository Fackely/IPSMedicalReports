package com.princetonsa.pdf;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadFileUpload;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseTable;

import com.princetonsa.actionform.manejoPaciente.ConsultarActivarCamasReservadasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.ParamInstitucionDao;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.ParametrizacionInstitucion;



public class ConsultarActivarCamasReservadasPdf
{
	/**
     * Clase para manejar los logs de esta clase
     */
    private static Logger logger = Logger.getLogger(ConsultarActivarCamasReservadasPdf.class);
    
    
    public static void imprimir (Connection connection,String nombrePdf, ConsultarActivarCamasReservadasForm forma,UsuarioBasico usuario,  HttpServletRequest request)
    {
    	PdfReports report = new PdfReports();
    	int numRegistros = Integer.parseInt(forma.getConsultarActivarCamasReservadasMAP("numRegistros").toString());
    	logger.info("el valor de hashmap "+forma.getConsultarActivarCamasReservadasMAP()); 
    	 int cont=2;
    	for (int j=0;j<numRegistros;j++)
    	{
    		
    		if (j!=0 && !forma.getConsultarActivarCamasReservadasMAP("centroatencion_"+j).toString().equals(forma.getConsultarActivarCamasReservadasMAP("centroatencion_"+(j-1)).toString()))
    		{
    			cont=cont+2;
    		}
    	}
    	
    	//GENERACION DE CABECERA
		//Ruta por defecto donde se genera los PDF
		//String filePath=ValoresPorDefecto.getFilePath();
		
		//*******SE CARGAN LOS DATOS DE LA INSTITUCION*******************************+
        ParametrizacionInstitucion ins=new ParametrizacionInstitucion();
        ins.cargar(connection,usuario.getCodigoInstitucionInt());
        //*********************************************************************************
        
        
        //Cargar Institucion Basica para manejo de logo
        InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		String logo="";
		
		try {
			ParamInstitucionDao institucionDao=null;
			DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			if (myFactory != null) {
				institucionDao = myFactory.getParamInstitucionDao();
			}
			Connection con=UtilidadBD.abrirConexion();
			ResultSetDecorator rs=institucionDao.consultaInstituciones(con,
					institucionBasica.getCodigoInstitucionBasica(), "0", "0", "0", false);
			if (rs.next()) {
				if (!UtilidadFileUpload.existeArchivoRutaCompelta(rs
						.getString("logo"))) {
					logo = "";
				} else {
					logo=rs.getString("logo");
				}
				
			}
			rs.close();
			
			UtilidadBD.cerrarConexion(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		String filePath="";//ValoresPorDefecto.getFilePath();
		if(UtilidadTexto.isEmpty(logo))
		{
	        filePath= institucionBasica.getLogoReportes();
		}
		else
		{
			filePath=logo;
		}
        
        logger.info("filepath image->"+filePath);
        logger.info("Ubicacion del Logo en el Reporte: " + institucionBasica.getUbicacionLogo());
        
        
        String tituloDocumentos="LISTADO DE RESERVAS DE CAMAS ";
        String subTituloDocumentos="Fecha de Impresión: "+UtilidadFecha.getFechaActual();
		
        /*if(System.getProperty("file.separator").equals("/"))
		{
			filePath=filePath.substring(0, filePath.lastIndexOf("/", (filePath.length()-2)));
			report.setReportBaseHeaderWithSubtitle(/*filePath+"/imagenes/logo-grey.gif"filePath+"/imagenes/"+ins.getLogo(), ins.getRazonSocial(),ins.getNit(), tituloDocumentos,subTituloDocumentos);
		/*}
		else
		{*/
			//filePath=filePath.substring(0, filePath.lastIndexOf("\\", (filePath.length()-2)));
			//filePath=filePath+"..\\imagenes\\"+ins.getLogo();
			report.setReportBaseHeaderWithSubtitle(/*filePath+"\\imagenes\\logo-grey.gif"*/filePath, ins.getRazonSocial(),ins.getNit(), tituloDocumentos,subTituloDocumentos);
		//}
    	
    	//abrir reporte
        report.openReport(nombrePdf);
        
        
        iTextBaseTable section;
        //aqui se crea la seccion donde se va a mostrar los datos del reporte
        
        section = report.createSection("informe", "descripcionTable",  numRegistros+cont, 9, 10);
        section.setTableBorder("descripcionTable", 0xFFFFFF, 0.0f);
	    section.setTableCellBorderWidth("descripcionTable", 0.5f);
	    section.setTableCellPadding("descripcionTable", 1);
	    section.setTableSpaceBetweenCells("descripcionTable", 0.5f);
	    section.setTableCellsDefaultColors("descripcionTable", 0xFFFFFF, 0x000000);
	    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy"); 
       for (int i=0;i<numRegistros;i++)
       {
    	   if (i==0)
    	   {
    		   section.setTableCellsColSpan(9);
    		   report.font.setFontAttributes(0x000000, 7, true, false, false);
				   
			    section.addTableTextCellAligned("descripcionTable", "CENTRO DE ATENCIÓN "+forma.getConsultarActivarCamasReservadasMAP("nombrecentroatencion_"+i).toString().toUpperCase(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			 	// se añade la sección al documento
			   
    	   }
    	   else
    		   if (i<numRegistros && !forma.getConsultarActivarCamasReservadasMAP("centroatencion_"+i).toString().equals(forma.getConsultarActivarCamasReservadasMAP("centroatencion_"+(i-1)).toString()))
    		   {
    			   
    			   section.setTableCellsColSpan(9);
	   			    report.font.setFontAttributes(0x000000, 7, true, false, false);
	   				   
	   			    section.addTableTextCellAligned("descripcionTable", "CENTRO DE ATENCIÓN "+forma.getConsultarActivarCamasReservadasMAP("nombrecentroatencion_"+i).toString().toUpperCase(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	   			 	// se añade la sección al documento
	   			
    		   
    		   }
    	   
    	   if (i==0 || i<numRegistros && !forma.getConsultarActivarCamasReservadasMAP("centroatencion_"+i).toString().equals(forma.getConsultarActivarCamasReservadasMAP("centroatencion_"+(i-1)).toString()) )
    	   {  
    		  	    
    		   section.setTableCellsColSpan(1);
			    section.addTableTextCellAligned("descripcionTable", "Centro Costo", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    section.addTableTextCellAligned("descripcionTable", "Piso", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    section.addTableTextCellAligned("descripcionTable", "Habitación", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    section.addTableTextCellAligned("descripcionTable", "Tipo Habitación", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    section.addTableTextCellAligned("descripcionTable", "Cama", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    section.addTableTextCellAligned("descripcionTable", "Paciente", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    section.addTableTextCellAligned("descripcionTable", "Tipo y No.Id", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    section.addTableTextCellAligned("descripcionTable", "Fecha Reservada", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    section.addTableTextCellAligned("descripcionTable", "Estado Reserva", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    
			 	//se añade la sección al documento
   			   
   			    report.font.setFontAttributes(0x000000, 7, false, false, false);
    	   }
			    
			    
	        
    	   			section.setTableCellsColSpan(1);
				    
			    	//se cargan todos los datos del reporte detallado.
			    	section.addTableTextCellAligned("descripcionTable", forma.getConsultarActivarCamasReservadasMAP("nombrecentrocosto_"+i).toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    section.addTableTextCellAligned("descripcionTable", forma.getConsultarActivarCamasReservadasMAP("codigopiso_"+i).toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    section.addTableTextCellAligned("descripcionTable", forma.getConsultarActivarCamasReservadasMAP("codigohabitacion_"+i).toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    section.addTableTextCellAligned("descripcionTable", forma.getConsultarActivarCamasReservadasMAP("nombretipohabitacion_"+i).toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    
				    section.addTableTextCellAligned("descripcionTable", forma.getConsultarActivarCamasReservadasMAP("nombrecama_"+i).toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    section.addTableTextCellAligned("descripcionTable", forma.getConsultarActivarCamasReservadasMAP("nombrepac_"+i).toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    section.addTableTextCellAligned("descripcionTable", forma.getConsultarActivarCamasReservadasMAP("tipoidentificacionpac_"+i).toString()+" "+forma.getConsultarActivarCamasReservadasMAP("identificacionpac_"+i).toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    String fecha="";
				    if(forma.getConsultarActivarCamasReservadasMAP("fechareservada_"+i) != null
				    		&& !forma.getConsultarActivarCamasReservadasMAP("fechareservada_"+i).toString().isEmpty()){
				    	fecha=format.format(forma.getConsultarActivarCamasReservadasMAP("fechareservada_"+i));
				    }
				    section.addTableTextCellAligned("descripcionTable", fecha, report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    section.addTableTextCellAligned("descripcionTable", ValoresPorDefecto.getIntegridadDominio(forma.getConsultarActivarCamasReservadasMAP("estadoreserva_"+i).toString()).toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
				    
				   
			    	
			    
				  	
       }
			   
			   
			   
//     se añade la sección al documento
	    report.addSectionToDocument("informe");
	        
	        
	        
	        
	        
	        
	
	    report.closeReport();
        
    	
    }
    
}