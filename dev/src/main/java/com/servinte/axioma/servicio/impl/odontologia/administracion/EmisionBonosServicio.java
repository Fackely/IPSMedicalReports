/**
 * 
 */
package com.servinte.axioma.servicio.impl.odontologia.administracion;

import java.sql.Connection;

import com.princetonsa.dto.odontologia.DtoBusquedaEmisionBonos;
import com.princetonsa.dto.odontologia.administracion.DtoBonoDescuento;
import com.servinte.axioma.mundo.fabrica.odontologia.administracion.AdministracionFabricaMundo;
import com.servinte.axioma.orm.EmisionBonosDesc;
import com.servinte.axioma.servicio.interfaz.administracion.IEmisionBonosServicio;

/**
 * @author Juan David Ramírez
 * @since 02 Diciembre 2010
 *
 */
public class EmisionBonosServicio implements IEmisionBonosServicio
{

	@Override
	public EmisionBonosDesc buscarEmisionXBono(DtoBusquedaEmisionBonos bono)
	{
		return AdministracionFabricaMundo.crearEmisionBonosMundo().buscarEmisionXBono(bono);
	}

	@Override
	public boolean marcarBonoUtilizado(Connection con, DtoBonoDescuento bono)
	{
		return AdministracionFabricaMundo.crearEmisionBonosMundo().marcarBonoUtilizado(con, bono);
	}


}
