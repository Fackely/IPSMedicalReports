package util.reportes.dinamico.complex;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.exception.DRException;

/**
 * @author Cristhian Murillo
*/
public abstract class AbstractReportMain<T extends ReportDesign<U>, U extends ReportData> 
{
	
	JasperReportBuilder reportBuilder;
	
	/*
	public AbstractReportMain() {
		build();
	}
	*/
	
	protected void build() 
	{
		this.reportBuilder = DynamicReports.report();			
		U data = getReportData();
		
		if (data != null) {
			reportBuilder.setDataSource(data.createDataSource());
		}
		
		getReportDesign().configureReport(reportBuilder, data);
		this.reportBuilder.build();
		
		/*
		try {
			reportBuilder.show();
		} catch (DRException e) {
			e.printStackTrace();
		}
		*/
	}
	
	
	protected U getReportData() {
		return null;
	}
	
	protected abstract T getReportDesign();

	/**
	 * @return valor de reportBuilder
	 */
	public JasperReportBuilder getReportBuilder() {
		return reportBuilder;
	}

	/**
	 * @param reportBuilder el reportBuilder para asignar
	 */
	public void setReportBuilder(JasperReportBuilder reportBuilder) {
		this.reportBuilder = reportBuilder;
	}

}
