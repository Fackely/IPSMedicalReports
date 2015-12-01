/**
 * 
 */
package com.servinte.axioma.mundo.interfaz.odontologia.administracion;

import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoEmisionTarjetaCliente;

/**
 * Lógica de Negocio Emisión tarjeta cliente
 * @author Juan David Ramírez
 * @since 06 Septiembre 2010
 * @version 1.0
 */
public interface IEmisionTarjetaClienteMundo
{
	/**
	 * Consultar una emisión de tarjeta cliente dados el
	 * tipo de tarjeta y el centro de atención
	 * @author Juan David Ramírez
	 * @param tipoTarjeta Tipo de tarjeta a filtrar
	 * @param centroAtencion Centro de atención a filtrar
	 * @return {@link DtoEmisionTarjetaCliente} Dto con los datos de la emisión encontrada
	 */
	public ArrayList<DtoEmisionTarjetaCliente> consultarEmisionTarjeta(int tipoTarjeta, int centroAtencion);

}
