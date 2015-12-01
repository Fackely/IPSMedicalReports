package com.servinte.axioma.generadorReporte.tesoreria.consultaConsolidadCierre;

import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.definition.expression.DRISimpleExpression;

import org.apache.struts.util.MessageResources;
 

/**
 * Utilidad para colocar el nombre de un total o subtotal
 *  en la columna quese requira
 * 
 * @version 1.0, May 11, 2011
 * @author <a href="mailto:luis.castellanos@servinte.com.co">Luis Castellanos</a>
 * 
 */
public class UtilSimpleExpression {
	
	
	/**
	 * Mensajes parametrizados de componente de titulos de reportes
	 */
	private MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.tesoreria.ConsultarConsolidadosCierres");

	
	
	/**
	 * Método que permite obtener el nombre del total Contrato
	 * @return titulo de total
	 */
	public DRISimpleExpression<String> getExpressionTotalContrato(){
		return new DRISimpleExpression<String>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -7133292142256615252L;
		
			@Override
			public String getName() {
				return messageResource.getMessage("consolidacion_cierres_label_totales_total_contrato");
			}

			@Override
			public Class<String> getValueClass() {
				return String.class;
			}

			@Override
			public String evaluate(ReportParameters arg0) {
				return messageResource.getMessage("consolidacion_cierres_label_totales_total_contrato");
			}
			
		};
	}
	
	
	
	/**
	 * Método que permite obtener el nombre del total Convenio
	 * @return titulo de total cierre
	 */
	public DRISimpleExpression<String> getExpressionTotalCierre(){
		return new DRISimpleExpression<String>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -7133292142256615252L;
		
			@Override
			public String getName() {
				return messageResource.getMessage("consolidacion_cierres_label_totales_total_cierre");
			}

			@Override
			public Class<String> getValueClass() {
				return String.class;
			}

			@Override
			public String evaluate(ReportParameters arg0) {
				return messageResource.getMessage("consolidacion_cierres_label_totales_total_cierre");
			}
			
		};
	}
	
	/**
	 * Método que permite obtener el nombre del total Centro de Atencion
	 * @return titulo de totales de centros de atencion
	 */
	public DRISimpleExpression<String> getExpressionTotalCentroAtencion(){
		return new DRISimpleExpression<String>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -7133292142256615252L;
		
			@Override
			public String getName() {
				return messageResource.getMessage("consolidacion_cierres_label_totales_centro_atencion");
			}

			@Override
			public Class<String> getValueClass() {
				return String.class;
			}

			@Override
			public String evaluate(ReportParameters arg0) {
				return messageResource.getMessage("consolidacion_cierres_label_totales_centro_atencion");
			}
			
		};
	}
	
	
	/**
	 * Método que permite obtener el nombre del total Centro de Atencion
	 * @return titulo de totales de centros de atencion
	 */
	public DRISimpleExpression<String> getExpressionTotalCentroAtencionCajaCajero(){
		return new DRISimpleExpression<String>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -7133292142256615252L;
		
			@Override
			public String getName() {
				return messageResource.getMessage("consolidacion_cierres_reportes_total_centro_atencion");
			}

			@Override
			public Class<String> getValueClass() {
				return String.class;
			}

			@Override
			public String evaluate(ReportParameters arg0) {
				return messageResource.getMessage("consolidacion_cierres_reportes_total_centro_atencion");
			}
			
		};
	}
	
	
	
	
	
	/**
	 * Método que permite obtener vacios de campos de reporte para acomodar los titulos
	 * @return vacios para acomodar los titulos de totales
	 */
	public DRISimpleExpression<String> getExpressionCajero(){
		return new DRISimpleExpression<String>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -7133292142256615252L;
		
			@Override
			public String getName() {
				return " ";
			}

			@Override
			public Class<String> getValueClass() {
				return String.class;
			}

			@Override
			public String evaluate(ReportParameters arg0) {
				return " ";
			}
			
		};
	}
	
	/**
	 * Método que permite obtener vacios de campos de reporte para acomodar los titulos
	 * @return vacios para acomodar los titulos de totales
	 */
	public DRISimpleExpression<String> getExpressionCajero2(){
		return new DRISimpleExpression<String>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -7133292142256615252L;
		
			@Override
			public String getName() {
				return "   ";
			}

			@Override
			public Class<String> getValueClass() {
				return String.class;
			}

			@Override
			public String evaluate(ReportParameters arg0) {
				return "   ";
			}
			
		};
	}
	
	/**
	 * Método que permite obtener el nombre del total Centro de Atencion
	 * @return titulo de totales de centros de atencion
	 */
	public DRISimpleExpression<String> getExpressionTotalCentroAtencionCajaCajero2(){
		return new DRISimpleExpression<String>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -7133292142256615252L;
		
			@Override
			public String getName() {
				return messageResource.getMessage("consolidacion_cierres_reportes_total_institucion");
			}

			@Override
			public Class<String> getValueClass() {
				return String.class;
			}

			@Override
			public String evaluate(ReportParameters arg0) {
				return messageResource.getMessage("consolidacion_cierres_reportes_total_institucion");
			}
			
		};
	}
	
	
	
	
	
	/**
	 * Método que permite obtener vacios de campos de reporte para acomodar los titulos
	 * @return vacios para acomodar los titulos de totales
	 */
	public DRISimpleExpression<String> getExpressionCajero3(){
		return new DRISimpleExpression<String>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -7133292142256615252L;
		
			@Override
			public String getName() {
				return "  ";
			}

			@Override
			public Class<String> getValueClass() {
				return String.class;
			}

			@Override
			public String evaluate(ReportParameters arg0) {
				return "  ";
			}
			
		};
	}
	
	/**
	 * Método que permite obtener vacios de campos de reporte para acomodar los titulos
	 * @return vacios para acomodar los titulos de totales
	 */
	public DRISimpleExpression<String> getExpressionCajero4(){
		return new DRISimpleExpression<String>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -7133292142256615252L;
		
			@Override
			public String getName() {
				return "    ";
			}

			@Override
			public Class<String> getValueClass() {
				return String.class;
			}

			@Override
			public String evaluate(ReportParameters arg0) {
				return "    ";
			}
			
		};
	}
	
}
