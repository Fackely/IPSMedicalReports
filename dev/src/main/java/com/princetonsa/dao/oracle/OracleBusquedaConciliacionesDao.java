package com.princetonsa.dao.oracle;

import java.util.HashMap;

import com.princetonsa.dao.BusquedaConciliacionesDao;
import com.princetonsa.dao.sqlbase.SqlBaseBusquedaConciliacionesDao;

public class OracleBusquedaConciliacionesDao implements BusquedaConciliacionesDao
{
	public HashMap consultarConciliaciones(HashMap<String, Object> mapa)
	{
		return SqlBaseBusquedaConciliacionesDao.consultarConciliaciones(mapa);
	}
}