package com.princetonsa.dao.postgresql;

import java.util.HashMap;

import com.princetonsa.dao.BusquedaConciliacionesDao;
import com.princetonsa.dao.sqlbase.SqlBaseBusquedaConciliacionesDao;
import com.princetonsa.dao.sqlbase.glosas.SqlBaseRegistrarConciliacionDao;


public class PostgresqlBusquedaConciliacionesDao implements BusquedaConciliacionesDao
{
	public HashMap consultarConciliaciones(HashMap<String, Object> mapa)
	{
		return SqlBaseBusquedaConciliacionesDao.consultarConciliaciones(mapa);
	}
}