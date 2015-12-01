/**
 * 
 */
package com.servinte.axioma.servicio.interfaz.odontologia.administracion;

import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoEmisionTarjetaCliente;

/**
 * @author axioma
 *
 */
public interface IEmisionTarjetaClienteServicio
{
	/**
	 * Consultar una emisi�n de tarjeta cliente dados el
	 * tipo de tarjeta y el centro de atenci�n
	 * @author Juan David Ram�rez
	 * @param tipoTarjeta Tipo de tarjeta a filtrar
	 * @param centroAtencion Centro de atenci�n a filtrar
	 * @return {@link DtoEmisionTarjetaCliente} Dto con los datos de la emisi�n encontrada
	 */
	public ArrayList<DtoEmisionTarjetaCliente> consultarEmisionTarjeta(int tipoTarjeta, int centroAtencion);

}
