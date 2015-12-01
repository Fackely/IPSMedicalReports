package util.reportes.dinamico;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;

public interface IReporteDinamico {

	/**
	 * M�todo encargado de exportar el reporte generado a formato PDF
	 * 
	 * @param reporte reporte generado
	 * @param nombreReporte nombre dado al archivo a generar
	 * @return nombreArchivo nombre del archivo generado
	 * 
	 * @author Fabi�n Becerra
	 */
	public String exportarReportePDF(JasperReportBuilder reporte, String nombreReporte);
	
	/**
	 * M�todo encargado de exportar el reporte generado a formato xls
	 * 
	 * @param reporte reporte generado
	 * @param nombreReporte nombre dado al archivo a generar
	 * @return nombreArchivo nombre del archivo generado
	 * 
	 * @author Fabi�n Becerra
	 */
	public String exportarReporteExcel(JasperReportBuilder reporte, String nombreReporte) ;
	
	/**
	 * M�todo encargado de exportar el reporte generado a un archivo plano
	 * 
	 * @param reporte reporte generado
	 * @param nombreReporte nombre dado al archivo a generar
	 * @return nombreArchivo nombre del archivo generado
	 * 
	 * @author Fabi�n Becerra
	 */
	public String exportarReporteArchivoPlano(JasperReportBuilder reporte, String nombreReporte) ;
	
}
