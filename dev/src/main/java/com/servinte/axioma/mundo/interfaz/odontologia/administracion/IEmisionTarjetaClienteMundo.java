/**
 * 
 */
package com.servinte.axioma.mundo.interfaz.odontologia.administracion;

import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoEmisionTarjetaCliente;

/**
 * L�gica de Negocio Emisi�n tarjeta cliente
 * @author Juan David Ram�rez
 * @since 06 Septiembre 2010
 * @version 1.0
 */
public interface IEmisionTarjetaClienteMundo
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
