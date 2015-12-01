package com.servinte.axioma.dao.interfaz.inventario;

import com.servinte.axioma.orm.DetallePedidos;
import com.servinte.axioma.orm.DetallePedidosId;


/**
 * Interfaz donde se define el comportamiento de los
 * DAO's para las funciones de IPedidoDAO
 * 
 * @author Cristhian Murillo
 */
public interface IDetallePedidosDAO 
{
	
	/**
	 * Implementacion del findById
	 * @param id
	 * @return DetallePedidos
	 */
	public DetallePedidos obtenerDetalleDespachosPorId(DetallePedidosId id);

	
	
	/**
	 * Implementacion del attachDirty
	 * @param instance
	 */
	public void attachDirty(DetallePedidos instance);
	
	
	
	/**
	 * Guarda la isntancia en la base de datos
	 * @param transientInstance
	 */
	public void persist(DetallePedidos transientInstance);
	
	
	
}
