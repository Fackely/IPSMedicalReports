package com.servinte.axioma.dao.interfaz.inventario;

import com.servinte.axioma.orm.Pedido;


/**
 * Interfaz donde se define el comportamiento de los
 * DAO's para las funciones de IPedidoDAO
 * 
 * @author Cristhian Murillo
 */
public interface IPedidoDAO 
{
	
	/**
	 * Metodo de la super clase finById
	 * @param id
	 * @return Pedido
	 */
	public Pedido obtenerPedidoPorId(int id);
	
	
	/**
	 * Metodo de la super clase attachDirty
	 * @param instance
	 */
	public void attachDirty(Pedido instance);
	
}
