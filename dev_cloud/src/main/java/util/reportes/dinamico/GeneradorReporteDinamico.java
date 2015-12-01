package util.reportes.dinamico;

import static net.sf.dynamicreports.report.builder.DynamicReports.export;

import java.io.File;
import java.util.Random;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperCsvExporterBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperXlsExporterBuilder;
import net.sf.dynamicreports.report.exception.DRException;

import org.axioma.util.log.Log4JManager;

import util.ValoresPorDefecto;

import com.princetonsa.enu.general.CarpetasArchivos;

public class GeneradorReporteDinamico implements IReporteDinamico{

	
	/**
	 * Método encargado de exportar el reporte generado a formato PDF
	 * 
	 * @param reporte reporte generado
	 * @param nombreReporte nombre dado al archivo a generar
	 * @return nombreArchivo nombre del archivo generado
	 * 
	 * @author Fabián Becerra
	 */
	public String exportarReportePDF(JasperReportBuilder reporte, String nombreReporte)
	{
		String nombreArchivo=CarpetasArchivos.REPORTES_JASPER + System.getProperty("file.separator") + nombreReporte + new Random().nextInt()+ ".pdf";
		 String path = ValoresPorDefecto.getDirectorioAxiomaBase();
		 
		 File dir = new File(path + CarpetasArchivos.REPORTES_JASPER + System.getProperty("file.separator"));
			
			if (!dir.exists()) {
				if (dir.mkdirs()) {
					Log4JManager.info("***********************************se   crea el directorio "+dir);
				}
			}
		 
		 	try {
		 		JasperPdfExporterBuilder pdfExporter = export.pdfExporter(path +nombreArchivo);
				reporte.toPdf(pdfExporter);
			} catch (DRException e) {
				e.printStackTrace();
			}
			   
		return nombreArchivo;
	}
	
	
	/**
	 * Método encargado de exportar el reporte generado a formato xls
	 * 
	 * @param reporte reporte generado
	 * @param nombreReporte nombre dado al archivo a generar
	 * @return nombreArchivo nombre del archivo generado
	 * 
	 * @author Fabián Becerra
	 */
	public String exportarReporteExcel(JasperReportBuilder reporte, String nombreReporte) {
		
		 String nombreArchivo = CarpetasArchivos.REPORTES_JASPER + System.getProperty("file.separator") + nombreReporte + new Random().nextInt() + ".xls";
		 String path = ValoresPorDefecto.getDirectorioAxiomaBase();
		 
		 File dir = new File(path + CarpetasArchivos.REPORTES_JASPER + System.getProperty("file.separator"));
			
			if (!dir.exists()) {
				if (dir.mkdirs()) {
					Log4JManager.info("***********************************se crea el directorio "+dir);
				}
			}
		 
			try {
				JasperXlsExporterBuilder xlsExporter = export.xlsExporter(path +nombreArchivo)  
		           .setDetectCellType(true)  
		           .setIgnorePageMargins(true)  
		           .setWhitePageBackground(false)  
		           .setRemoveEmptySpaceBetweenColumns(true);
				reporte.toXls(xlsExporter);
			} catch (DRException e) {
				e.printStackTrace();
			}
		return nombreArchivo;
		
	}
	
	/**
	 * Método encargado de exportar el reporte generado a un archivo plano
	 * 
	 * @param reporte reporte generado
	 * @param nombreReporte nombre dado al archivo a generar
	 * @return nombreArchivo nombre del archivo generado
	 * 
	 * @author Fabián Becerra
	 */
	public String exportarReporteArchivoPlano(JasperReportBuilder reporte, String nombreReporte) {
		
		
		String nombreArchivo=CarpetasArchivos.REPORTES_JASPER + System.getProperty("file.separator") + nombreReporte + new Random().nextInt()+ ".csv";
		String path = ValoresPorDefecto.getDirectorioAxiomaBase();
		 
		 File dir = new File(path + CarpetasArchivos.REPORTES_JASPER + System.getProperty("file.separator"));
		
		 if (!dir.exists()) {
				if (dir.mkdirs()) {
					Log4JManager.info("***********************************se crea el directorio "+dir);
				}
			}
		try {
			JasperCsvExporterBuilder csvExporter=export.csvExporter(path + nombreArchivo);
			csvExporter.setCharacterEncoding("ISO-8859-1");
			csvExporter.setFieldDelimiter("|");
			reporte.toCsv(csvExporter);
		} catch (DRException e) {
			e.printStackTrace();
		}
			
		return nombreArchivo;
	}
	
	
}
