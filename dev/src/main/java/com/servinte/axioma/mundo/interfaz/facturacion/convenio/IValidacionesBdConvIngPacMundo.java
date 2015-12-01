package com.servinte.axioma.mundo.interfaz.facturacion.convenio;

import com.servinte.axioma.orm.ValidacionesBdConvIngPac;


/**
 * 
 * @author axioma
 *
 */
public interface IValidacionesBdConvIngPacMundo {
	
		
	/**
	 * Retorna la ultima validacion en base de datos para el convenio ingreso paciente dado
	 * 
	 * @author Cristhian Murillo
	 * @param conveniosIngresoPacientePk
	 * 
	 * @return ValidacionesBdConvIngPac
	 */
	public ValidacionesBdConvIngPac obtenerUltimaValidacionBd(long conveniosIngresoPacientePk);
	
	

}
