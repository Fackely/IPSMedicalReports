package com.servinte.axioma.dao.interfaz.inventario;

import com.servinte.axioma.orm.Despacho;


/**
 * Interfaz donde se define el comportamiento de los
 * DAO's para las funciones de IDespachoDAO
 * 
 * @author Cristhian Murillo
 */
public interface IDespachoDAO 
{
	
	/**
	 * Metodo de la super clase finById
	 * @param id
	 * @return Despacho
	 */
	public Despacho obtenerDespachoPorId(int id);
	
	
	/**
	 * Metodo de la super clase attachDirty
	 * @param instance
	 */
	public void attachDirty(Despacho instance);
	
}
