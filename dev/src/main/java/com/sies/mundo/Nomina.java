package com.sies.mundo;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

import com.sies.dao.NominaDao;
import com.sies.dao.SiEsFactory;

public class Nomina
{
	NominaDao nominaDao=null;
	
	/**
	 * Constructor que crea el daofactory de nómina
	 *
	 */
	public Nomina()
	{
		if(nominaDao==null)
		{
			nominaDao=SiEsFactory.getDaoFactory().getNominaDao();
		}
	}
	
	/**
	 * Consulta los tipos de vinculación
	 * @return Collection<HashMap<String, Object>>
	 */
	public Collection<HashMap<String, Object>> listarTiposVinculacion(Connection con)
	{
		return nominaDao.listarTiposVinculacion(con);
	}
}
