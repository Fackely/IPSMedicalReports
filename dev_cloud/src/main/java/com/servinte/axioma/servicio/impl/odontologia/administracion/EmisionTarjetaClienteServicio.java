/**
 * 
 */
package com.servinte.axioma.servicio.impl.odontologia.administracion;

import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoEmisionTarjetaCliente;
import com.servinte.axioma.mundo.fabrica.odontologia.administracion.AdministracionFabricaMundo;
import com.servinte.axioma.servicio.interfaz.odontologia.administracion.IEmisionTarjetaClienteServicio;

/**
 * Implementaci�n del servicio para la emisi�n de tarjetas
 * @author Juan David Ram�rez
 * @since 06 Septiembre 2010
 * @version 1.0
 */
public class EmisionTarjetaClienteServicio implements IEmisionTarjetaClienteServicio
{
	@Override
	public ArrayList<DtoEmisionTarjetaCliente> consultarEmisionTarjeta(int tipoTarjeta, int centroAtencion)
	{
		return AdministracionFabricaMundo.crearEmisionTarjetaClienteMundo().consultarEmisionTarjeta(tipoTarjeta, centroAtencion);
	}
}
