package util.reportes.dinamico.complex;

import net.sf.jasperreports.engine.JRDataSource;

/**
 * @author Cristhian Murillo
 */
public interface ReportData {
	
	public JRDataSource createDataSource();
}
