package com.servinte.axioma.dao.interfaz.inventario;

import com.servinte.axioma.orm.DespachoPedido;


/**
 * Interfaz donde se define el comportamiento de los
 * DAO's para las funciones de IPedidoDAO
 * 
 * @author Cristhian Murillo
 */
public interface IDespachoPedidoDAO 
{
	
	
	/**
	 * Implementacion del attachDirty
	 * @param instance
	 */
	public void attachDirty(DespachoPedido instance);
	
	
	
	/**
	 * Guarda la isntancia en la base de datos
	 * @param transientInstance
	 */
	public void persist(DespachoPedido transientInstance);
	
	
	
}
