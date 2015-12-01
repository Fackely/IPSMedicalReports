package com.servinte.axioma.generadorReporte.capitacion.ordenesCapitacionSubcontratada;

import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.definition.expression.DRISimpleExpression;


/**
 * Utilidad para colocar el nombre de un total o subtotal
 *  en la columna quese requira
 * 
 * @version 1.0, May 11, 2011
 * @author <a href="mailto:ricardo.ruiz@servinte.com.co">Ricardo Ruiz</a>
 * 
 */
public class UtilSimpleExpression {
	
	
	/**
	 * Método que permite obtener el nombre del total Contrato
	 * @return
	 */
	public DRISimpleExpression<String> getExpressionTotalContrato(){
		return new DRISimpleExpression<String>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -7133292142256615252L;
		
			@Override
			public String getName() {
				return "Total Contrato";
			}

			@Override
			public Class<String> getValueClass() {
				return String.class;
			}

			@Override
			public String evaluate(ReportParameters arg0) {
				return "Total Contrato";
			}
			
		};
	}
	
	/**
	 * Método que permite obtener el nombre del total Convenio
	 * @return
	 */
	public DRISimpleExpression<String> getExpressionTotalConvenio(){
		return new DRISimpleExpression<String>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -7133292142256615252L;
		
			@Override
			public String getName() {
				return "Total Convenio";
			}

			@Override
			public Class<String> getValueClass() {
				return String.class;
			}

			@Override
			public String evaluate(ReportParameters arg0) {
				return "Total Convenio";
			}
			
		};
	}
	
	/**
	 * Método que permite obtener el nombre del total Nivel de Atención
	 * @return
	 */
	public DRISimpleExpression<String> getExpressionTotalNivel(){
		return new DRISimpleExpression<String>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -7133292142256615252L;
		
			@Override
			public String getName() {
				return "Total Nivel";
			}

			@Override
			public Class<String> getValueClass() {
				return String.class;
			}

			@Override
			public String evaluate(ReportParameters arg0) {
				return "Total Nivel";
			}
			
		};
	}
	
	/**
	 * Método que permite obtener el nombre del total de Grupos de Servicio o Clases de Inventarios
	 * @return
	 */
	public DRISimpleExpression<String> getExpressionTotalGrupoClase(){
		return new DRISimpleExpression<String>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -7133292142256615252L;
		
			@Override
			public String getName() {
				return "Total";
			}

			@Override
			public Class<String> getValueClass() {
				return String.class;
			}

			@Override
			public String evaluate(ReportParameters arg0) {
				return "Total";
			}
			
		};
	}
	
}
