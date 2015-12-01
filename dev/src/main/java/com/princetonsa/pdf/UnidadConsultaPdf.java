/*
 * Creado  24/08/2004
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.pdf;

import javax.servlet.http.HttpServletRequest;

import com.princetonsa.actionform.parametrizacion.RegUnidadesConsultaForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;

import util.UtilidadFecha;
import util.pdf.PdfReports;
import util.pdf.iTextBaseFont;




/**
 * Clase para manejar la generación de PDF's para
 * las unidades de consulta
 *
 * @version 1.0, 24/08/2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan Lopez</a>
 */
public class UnidadConsultaPdf
{

    public  static void pdfUnidadConsulta(String filename, RegUnidadesConsultaForm regUniConForm,  UsuarioBasico medico, HttpServletRequest request )
   {
   		PdfReports report = new PdfReports();
   		String tituloDiagnosticos="REPORTE DE UNIDAD DE CONSULTA (" + UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() + ")";
		
		InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), tituloDiagnosticos);
		
		report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
		report.openReport(filename);
		report.document.addParagraph("ya casi.",10);
		report.closeReport(); 
   }
}
