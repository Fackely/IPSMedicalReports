/**
 * 
 */
package com.servinte.axioma.servicio.impl.administracion;

import com.princetonsa.dto.odontologia.administracion.DtoTipoTarjetaCliente;
import com.servinte.axioma.mundo.fabrica.odontologia.administracion.AdministracionFabricaMundo;
import com.servinte.axioma.servicio.interfaz.administracion.ITipoTarjetaClienteServicio;

/**
 * @author axioma
 *
 */
public class TipoTarjetaClienteServicio implements ITipoTarjetaClienteServicio
{

	@Override
	public DtoTipoTarjetaCliente consultarTipoTarjetaCliente(double tipoTarjeta, String claseVenta)
	{
		return AdministracionFabricaMundo.crearTipoTarjetaClienteMundo().consultarTipoTarjetaCliente(tipoTarjeta, claseVenta);
		
	}

}
