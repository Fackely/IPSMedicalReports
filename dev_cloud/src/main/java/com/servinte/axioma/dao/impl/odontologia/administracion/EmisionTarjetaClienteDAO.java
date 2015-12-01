/**
 * 
 */
package com.servinte.axioma.dao.impl.odontologia.administracion;

import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoEmisionTarjetaCliente;
import com.servinte.axioma.dao.interfaz.odontologia.administracion.IEmisionTarjetaClienteDAO;
import com.servinte.axioma.orm.delegate.odontologia.administracion.EncaEmiTargetaClienteDelegate;

/**
 * Implementación DAO de emisión tarjeta cliente
 * @author Juan David Ramírez
 * @since 08 Septiembre 2010
 * @version 1.0
 */
public class EmisionTarjetaClienteDAO implements IEmisionTarjetaClienteDAO
{
	@Override
	public ArrayList<DtoEmisionTarjetaCliente> consultarEmisionTarjeta(int tipoTarjeta, int centroAtencion)
	{
		return new EncaEmiTargetaClienteDelegate().consultarEmisionTarjeta(tipoTarjeta, centroAtencion);
	}

}
