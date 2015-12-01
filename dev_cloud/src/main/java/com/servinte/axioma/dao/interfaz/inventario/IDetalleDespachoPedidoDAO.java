package com.servinte.axioma.dao.interfaz.inventario;

import com.servinte.axioma.orm.DetalleDespachoPedido;
import com.servinte.axioma.orm.DetalleDespachoPedidoId;


/**
 * Interfaz donde se define el comportamiento de los
 * DAO's para las funciones de IDetalleDespachoPedidoDAO
 * 
 * @author Cristhian Murillo
 */
public interface IDetalleDespachoPedidoDAO 
{
	
	/**
	 * Implementacion del findById
	 * @param id
	 * @return DetalleDespachoPedido
	 */
	public DetalleDespachoPedido obtenerDetalleDespachosPorId(DetalleDespachoPedidoId id);

	
	
	/**
	 * Implementacion del attachDirty
	 * @param instance
	 */
	public void attachDirty(DetalleDespachoPedido instance);
	
	
	
	/**
	 * Guarda la isntancia en la base de datos
	 * @param transientInstance
	 */
	public void persist(DetalleDespachoPedido transientInstance);
	
	
	
}
