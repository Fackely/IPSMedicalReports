package com.sies.dao;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

public interface NominaDao
{
	/**
	 * Consulta los tipos de vinculación
	 * @return Collection<HashMap<String, Object>>
	 */
	public abstract Collection<HashMap<String, Object>> listarTiposVinculacion(Connection con);

}
