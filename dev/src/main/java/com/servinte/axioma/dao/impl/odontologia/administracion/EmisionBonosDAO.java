/**
 * 
 */
package com.servinte.axioma.dao.impl.odontologia.administracion;

import java.sql.Connection;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.odontologia.EmisionBonosDescDao;
import com.princetonsa.dto.odontologia.DtoBusquedaEmisionBonos;
import com.princetonsa.dto.odontologia.administracion.DtoBonoDescuento;
import com.servinte.axioma.dao.interfaz.odontologia.administracion.IEmisionBonosDAO;
import com.servinte.axioma.orm.EmisionBonosDesc;
import com.servinte.axioma.orm.delegate.odontologia.administracion.EmisionBonosDescDelegate;

/**
 * @author Juan David Ramírez
 * @since 02 Diciembre 2010
 *
 */
public class EmisionBonosDAO implements IEmisionBonosDAO
{

	@Override
	public EmisionBonosDesc buscarEmisionXBono(DtoBusquedaEmisionBonos bono)
	{
		return new EmisionBonosDescDelegate().buscarEmisionXBono(bono);
	}

	@Override
	public boolean marcarBonoUtilizado(Connection con, DtoBonoDescuento bono)
	{
		EmisionBonosDescDao emisionBonos=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEmisionBonosDescDao();
		return emisionBonos.marcarBonoUtilizado(con, bono);
	}

	@Override
	public boolean asociarBonoProgramaConvenio(Connection con,
			DtoBonoDescuento bono)
	{
		EmisionBonosDescDao emisionBonos=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEmisionBonosDescDao();
		return emisionBonos.asociarBonoProgramaConvenio(con, bono);
	}

}
