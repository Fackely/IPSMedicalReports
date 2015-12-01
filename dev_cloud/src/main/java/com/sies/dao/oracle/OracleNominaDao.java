package com.sies.dao.oracle;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

import com.sies.dao.NominaDao;
import com.sies.dao.sqlbase.SqlBaseNominaDao;

public class OracleNominaDao implements NominaDao
{

	public Collection<HashMap<String, Object>> listarTiposVinculacion(Connection con)
	{
		return SqlBaseNominaDao.listarTiposVinculacion(con);
	}

}
