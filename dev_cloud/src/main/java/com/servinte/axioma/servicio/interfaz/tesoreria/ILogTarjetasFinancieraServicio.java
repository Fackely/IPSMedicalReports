package com.servinte.axioma.servicio.interfaz.tesoreria;

import com.servinte.axioma.orm.HistComisionXCentroAten;
import com.servinte.axioma.orm.HistTarjFinancieraComision;
import com.servinte.axioma.orm.HistTarjFinancieraReteica;
import com.servinte.axioma.orm.HistTarjetasFinancieras;

/**
 * Servicio que le delega al negocio las operaciones de LogTarjetasFinanciera
 * 
 * @author Cristhian Murillo
 * @see com.servinte.axioma.servicio.impl.tesoreria.LogTarjetasFinancieraServicio
 */
public interface ILogTarjetasFinancieraServicio 
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
