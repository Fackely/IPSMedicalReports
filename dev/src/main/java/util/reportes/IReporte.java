package util.reportes;

import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

import net.sf.jasperreports.engine.JasperPrint;

/**
 * 
 * Esta Interface se encarga de declarar los métodos que se deben
 * implementar para la generación de un reporte 
 * 
 * @author, Angela Maria Aguirre
 * @version 1.0
 */
public interface IReporte {
	
	public byte[] exportarReporte(JasperPrint jasperPrint);
	public JasperPrint generarReporte();
	public byte[] generarReporteExportado();
	public InputStream cargarPlantilla();
	public abstract Collection obtenerDatosReporte();
	public abstract Map obtenerDatosAdicionalesReporte();
	public abstract String obtenerRutaPlantilla();
	public JasperPrint compilarReporte(String rutaOrigen, String rutaReporte);

	/**
	 * Este m&eacute;todo se encarga de Exportar el reporte a PDF 
	 * @param jasperPrint
	 * @return Nombre del archivo generado
	 *
	 * @author Yennifer Guerrero
	 */
	public String exportarReportePDF(JasperPrint jasperPrint, String nombreReporte);
}
