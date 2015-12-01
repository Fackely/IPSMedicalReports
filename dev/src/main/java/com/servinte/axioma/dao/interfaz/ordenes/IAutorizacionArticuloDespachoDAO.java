package com.servinte.axioma.dao.interfaz.ordenes;

import com.servinte.axioma.orm.AutorizacionArticuloDespacho;



/**
 * Interfaz donde se define el comportamiento de los
 * DAO's para las funciones de IAutorizacionArticuloDespachoDAO
 * 
 * @author Cristhian Murillo
 */
public interface IAutorizacionArticuloDespachoDAO 
{
	
	
	public void attachDirty(AutorizacionArticuloDespacho instance);
	
}
