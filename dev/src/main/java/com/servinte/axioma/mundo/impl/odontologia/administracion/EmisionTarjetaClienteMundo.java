/**
 * 
 */
package com.servinte.axioma.mundo.impl.odontologia.administracion;

import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoEmisionTarjetaCliente;
import com.servinte.axioma.dao.fabrica.odontologia.administracion.AdministracionFabricaDAO;
import com.servinte.axioma.mundo.interfaz.odontologia.administracion.IEmisionTarjetaClienteMundo;

/**
 * L�gica de Negocio Emisi�n tarjeta cliente
 * @author Juan David Ram�rez
 * @since 06 Septiembre 2010
 * @version 1.0
 */
public class EmisionTarjetaClienteMundo implements IEmisionTarjetaClienteMundo
{

	@Override
	public ArrayList<DtoEmisionTarjetaCliente> consultarEmisionTarjeta(int tipoTarjeta, int centroAtencion)
	{
		return AdministracionFabricaDAO.crearEmisionTarjetaClienteDAO().consultarEmisionTarjeta(tipoTarjeta, centroAtencion);
	}

}
