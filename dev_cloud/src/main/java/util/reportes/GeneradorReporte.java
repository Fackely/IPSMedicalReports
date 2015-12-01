package util.reportes;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Random;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRCsvExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRTextExporterParameter;

import org.axioma.util.log.Log4JManager;

import util.ValoresPorDefecto;

import com.princetonsa.enu.general.CarpetasArchivos;

/**
 * Esta clase se encarga de implementar los métodos necesarios
 * para generar un reporte
 * 
 * @author, Angela Maria Aguirre 
 * @version 1.0
 */
public abstract class GeneradorReporte implements IReporte {

	/**
	 * Colección de datos del reporte
	 */
	@SuppressWarnings("unchecked")
	private Collection datos;
	
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de compilar el reporte de extensión jxrml
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public JasperPrint compilarReporte(String rutaOrigen, String rutaReporte){

		try {
			JasperCompileManager.compileReportToFile(rutaOrigen, rutaReporte);
			InputStream plantilla = cargarPlantilla();
			JasperPrint jasperPrint = JasperFillManager.fillReport(plantilla, null);
			return jasperPrint;
			
		} catch (JRException e) {
			e.printStackTrace();
		}		
		return null;
	}
	
	/**
	 * Este m&eacute;odo se encarga de la generaci&oacute;n del reporte
	 * @param Map, con los datos necesarios para la consulta de
	 * la informaci&oacute;n del reporte
	 * @return byte[] con el reporte exportado
	 * @author Angela Maria Aguirre
	 * 
	 */
	@Override
	public JasperPrint generarReporte() {
		
		try {
			JasperPrint jasperPrint = null;
			//Se obtienen los datos del reporte
			this.datos = obtenerDatosReporte();
			
			//Se crea el dataSource que entiende Jasper
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(this.datos);

			//Se carga plantilla de reporte y los parametros adicionales
			InputStream plantilla = cargarPlantilla();
			Map<String, Object> datosAdicionales = obtenerDatosAdicionalesReporte();
						
			jasperPrint = JasperFillManager.fillReport(plantilla, datosAdicionales, dataSource);			
			return jasperPrint;
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return null;
	}
	
	/**
	 * Este método se encarga de la generaciÃ³n del reporte
	 * @param Map, con los datos necesarios para la consulta de
	 * la informaciÃ³n del reporte
	 * @return byte[] con el reporte exportado
	 * @author Angela Maria Aguirre
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public byte[] generarReporteExportado() {
		
		try {
			JasperPrint jasperPrint = null;
			byte[] reporteExportado = null;
			
			this.datos = obtenerDatosReporte();
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(this.datos);
			InputStream plantilla = cargarPlantilla();
			Map datosAdicionales = obtenerDatosAdicionalesReporte();			
			jasperPrint = JasperFillManager.fillReport(plantilla, datosAdicionales, dataSource);
			reporteExportado = exportarReporte(jasperPrint);
			return reporteExportado;
			
		} catch (JRException e) {
			e.printStackTrace();
		}		
		return null;
	}
	
	/**
	 * Este método se encarga de convertir el reporte 
	 * a un flujo de bites
	 * @author, Angela Maria Aguirre
	 * @param JasperPrint, con el objeto a exportar
	 * @return byte[], flujo de bytes a retornar
	 */	
	@Override
	public byte[] exportarReporte(JasperPrint jasperPrint) {
		try {
			JRPdfExporter pdfExporter = new JRPdfExporter();			
			ByteArrayOutputStream salida = new ByteArrayOutputStream();
			pdfExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			pdfExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, salida);
			pdfExporter.exportReport();
			
			
			return salida.toByteArray();		
			
		} catch (JRException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Este método se encarga de exportar el reporte generado
	 * en formato pdf.
	 * @param jasperPrint
	 * @return nombreArchivo nombre del archivo generado
	 * 
	 * @author Yennifer Guerrero
	 */
	public String exportarReportePDF(JasperPrint jasperPrint, String nombreReporte) {
		
		 String nombreArchivo=CarpetasArchivos.REPORTES_JASPER + System.getProperty("file.separator") + nombreReporte + new Random().nextInt()+ ".pdf";
		 String path = ValoresPorDefecto.getDirectorioAxiomaBase();
		 
		 File dir = new File(path + CarpetasArchivos.REPORTES_JASPER + System.getProperty("file.separator"));
			
			if (!dir.exists()) {
				if (dir.mkdirs()) {
					Log4JManager.info("***********************************se crea el directorio "+dir);
				}
			}
		 
		 try {
			   //
			   // proceso en donde se escribe fisicamente el pdf con la información
			   // generada anteriormente
			   //
			   JasperExportManager.exportReportToPdfFile(jasperPrint, path +nombreArchivo);
			   

			  } catch (JRException e) {
					e.printStackTrace();
			  }
			  
		return nombreArchivo;
		
	}
	
	/**
	 * Este método se obtiene la plantilla compilada
	 * para el reporte
	 * @return InputStream con la plantilla obtenida
	 * @author, Angela Maria Aguirre
	 * 
	 */
	@Override
	public InputStream cargarPlantilla() {
		ClassLoader loader = this.getClass().getClassLoader();
		String rutaPlantilla = obtenerRutaPlantilla();
		InputStream plantilla  = loader.getResourceAsStream(rutaPlantilla);		 
		return plantilla;
	}
	

	/**
	 * Este mÃ©todo se encarga de obtener los datos que se deben
	 * mostrar en el reporte. Este mÃ©todo debe ser implementado 
	 * en la clase concreta que lo herede.
	 * 
	 * @param Map, con los datos necesarios para realizar la consulta
	 * @return Collection, con los datos obtenidos en la consulta
	 * @author, Angela Maria Aguirre
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public abstract Collection obtenerDatosReporte();

	
	/**
	 * Este mÃ©todo se encarga de obtener los datos de la 
	 * plantilla del reporte. Este mÃ©todo debe ser implementado 
	 * en la clase concreta que lo herede.
	 * 
	 * @return Map, con los datos obtenidos
	 * @author, Angela Maria Aguirre
	 * 
	 */
	@Override
	public abstract Map<String, Object> obtenerDatosAdicionalesReporte();
	

	/**
	 * Este mÃ©todo se encarga de obtener la ubicaciÃ³n de la 
	 * plantilla del reporte. Este mÃ©todo debe ser implementado 
	 * en la clase concreta que lo herede.
	 * @return String con la ruta de la plantilla
	 * @author, Angela Maria Aguirre
	 * 
	 */
	@Override
	public abstract String obtenerRutaPlantilla();
	
	/**
	 * Este método se encarga de exportar el reporte generado
	 * en formato excel.
	 * @param jasperPrint
	 * @return nombreArchivo nombre del archivo generado
	 * 
	 * @author Yennifer Guerrero
	 */
	public String exportarReporteExcel(JasperPrint jasperPrint, String nombreReporte) {
		
		 String nombreArchivo = CarpetasArchivos.REPORTES_JASPER + System.getProperty("file.separator") + nombreReporte + new Random().nextInt() + ".xls";
		 String path = ValoresPorDefecto.getDirectorioAxiomaBase();
		 
		 File dir = new File(path + CarpetasArchivos.REPORTES_JASPER + System.getProperty("file.separator"));
			
			if (!dir.exists()) {
				if (dir.mkdirs()) {
					Log4JManager.info("***********************************se crea el directorio "+dir);
				}
			}
		 
		 try {
			 
			JExcelApiExporter xlsExporter = new JExcelApiExporter();
			xlsExporter.setParameter(JRExporterParameter.JASPER_PRINT,
					jasperPrint);
			xlsExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
					path + nombreArchivo);
	
			
			xlsExporter.exportReport();

		} catch (JRException e) {
			e.printStackTrace();
		}
			  
		return nombreArchivo;
		
	}
	
	/**
	 * Este método se encarga de exportar el reporte generado
	 * en formato texto plano.
	 * @param jasperPrint
	 * @return nombreArchivo nombre del archivo generado
	 * 
	 * @author Yennifer Guerrero
	 */
	public String exportarReporteTextoPlano(JasperPrint jasperPrint, String nombreReporte) {
		
		
		String nombreArchivo=CarpetasArchivos.REPORTES_JASPER + System.getProperty("file.separator") + nombreReporte + new Random().nextInt()+ ".csv";
		String path = ValoresPorDefecto.getDirectorioAxiomaBase();
		 
		 File dir = new File(path + CarpetasArchivos.REPORTES_JASPER + System.getProperty("file.separator"));
		
		 if (!dir.exists()) {
				if (dir.mkdirs()) {
					Log4JManager.info("***********************************se crea el directorio "+dir);
				}
			}
		 
		 try {
			 
			JRCsvExporter csvExporter = new JRCsvExporter();
			csvExporter.setParameter(JRExporterParameter.JASPER_PRINT,
                    jasperPrint);
			
			csvExporter.setParameter(JRCsvExporterParameter.FIELD_DELIMITER, "|");
			csvExporter.setParameter(JRExporterParameter.CHARACTER_ENCODING,"ISO-8859-1");
			csvExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
					path + nombreArchivo);
			
			csvExporter.exportReport();

		} catch (JRException e) {
			e.printStackTrace();
		}
			  
		return nombreArchivo;
		
	}
	
	/**
	 * Este metodo se encarga de retornar true si la imagen del logo se encuentra almacenada 
	 * en la ruta indicada. 
	 *
	 * @param rutaLogo
	 * @return
	 *
	 * @autor Yennifer Guerrero
	 */
	public boolean existeLogo (String rutaLogo){
		ClassLoader loader = this.getClass().getClassLoader();
		InputStream myInFile = null;
        boolean existe = false;

        try {
	        myInFile = loader.getResourceAsStream(rutaLogo);
	        
	        if (myInFile != null) {
	           existe = true;
	            myInFile.close();
	        }
        }
        catch (Exception e) {
        	e.printStackTrace();
		}
        
        return existe;
	}
	
	
}
