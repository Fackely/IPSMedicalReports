/**
 * 
 */
package com.servinte.axioma.servicio.interfaz.administracion;

import com.princetonsa.dto.odontologia.administracion.DtoTipoTarjetaCliente;
import com.servinte.axioma.servicio.impl.administracion.TipoTarjetaClienteServicio;

/**
 * @author axioma
 *
 */
public interface ITipoTarjetaClienteServicio
{
	/**
	 * Consultar el tipo de tarjeta específico según la clase de venta
	 * @param tipoTarjeta Tipo de tarjeta a buscar
	 * @param claseVenta TODO
	 * @return {@link TipoTarjetaClienteServicio} Instancia con el tipo de tarjeta
	 */
	public DtoTipoTarjetaCliente consultarTipoTarjetaCliente(double tipoTarjeta, String claseVenta);
}
