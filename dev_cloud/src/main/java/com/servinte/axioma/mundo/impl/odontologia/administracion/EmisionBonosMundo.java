/**
 * 
 */
package com.servinte.axioma.mundo.impl.odontologia.administracion;

import java.sql.Connection;

import com.princetonsa.dto.odontologia.DtoBusquedaEmisionBonos;
import com.princetonsa.dto.odontologia.administracion.DtoBonoDescuento;
import com.servinte.axioma.dao.fabrica.odontologia.administracion.AdministracionFabricaDAO;
import com.servinte.axioma.mundo.interfaz.odontologia.administracion.IEmisionBonosMundo;
import com.servinte.axioma.orm.EmisionBonosDesc;

/**
 * @author Juan David Ramírez
 * @since 02 Diciembre 2010
 *
 */
public class EmisionBonosMundo implements IEmisionBonosMundo
{

	@Override
	public EmisionBonosDesc buscarEmisionXBono(DtoBusquedaEmisionBonos bono)
	{
		return AdministracionFabricaDAO.crearEmisionBonosDAO().buscarEmisionXBono(bono);
	}

	@Override
	public boolean marcarBonoUtilizado(Connection con,
			DtoBonoDescuento bono)
	{
		boolean respuesta=AdministracionFabricaDAO.crearEmisionBonosDAO().marcarBonoUtilizado(con, bono);
		if(respuesta)
		{
			AdministracionFabricaDAO.crearEmisionBonosDAO().asociarBonoProgramaConvenio(con, bono);
		}
		return false;
	}

}
