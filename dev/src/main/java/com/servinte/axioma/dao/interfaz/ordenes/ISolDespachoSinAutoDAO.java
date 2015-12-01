package com.servinte.axioma.dao.interfaz.ordenes;

import com.servinte.axioma.orm.SolDespachoSinAuto;


/**
 * Interfaz donde se define el comportamiento de los
 * DAO's para las funciones de ISolDespachoSinAutoDAO
 * 
 * @author Cristhian Murillo
 */
public interface ISolDespachoSinAutoDAO 
{
	
	/**
	 * Implementaci�n del metodo attachDirty
	 * @param instance
	 */
	public void attachDirtySolDespachoSinAuto(SolDespachoSinAuto instance);

	
}
