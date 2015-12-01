package com.servinte.axioma.mundo.interfaz.tesoreria;

import com.servinte.axioma.orm.HistComisionXCentroAten;
import com.servinte.axioma.orm.HistTarjFinancieraComision;
import com.servinte.axioma.orm.HistTarjFinancieraReteica;
import com.servinte.axioma.orm.HistTarjetasFinancieras;


/**
 * Define la l&oacute;gica de negocio relacionada con LogTarjetasFinancieraMundo 
 * 
 * @author Cristhian Murillo
 * @see com.servinte.axioma.mundo.impl.tesoreria.LogTarjetasFinancieraMundo
 */
public interface ILogTarjetasFinancieraMundo 
{


	/**
	 * Realiza la persistencia de la Instancia
	 * @param transientInstance
	 */
	public void guardarHistComisionXCentroAten(HistComisionXCentroAten transientInstance);
	
	
	/**
	 * Realiza la persistencia de la Instancia
	 * @param transientInstance
	 */
	public void guardarHistTarjetasFinancieras(HistTarjetasFinancieras transientInstance); 
	
	
	/**
	 * Realiza la persistencia de la Instancia
	 * @param transientInstance
	 */
	public void guardarHistTarjFinancieraReteica(HistTarjFinancieraReteica transientInstance); 
	
	
	/**
	 * Realiza la persistencia de la Instancia
	 * @param transientInstance
	 */
	public void guardarHistTarjFinancieraComision(HistTarjFinancieraComision transientInstance);
	
}
