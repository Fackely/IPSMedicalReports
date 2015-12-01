package com.sies.dao.postgresql;

import com.sies.dao.CategoriaDao;
import com.sies.dao.CuadroTurnosDao;
import com.sies.dao.IngresarTurnoDao;
import com.sies.dao.LogsSiesDao;
import com.sies.dao.NominaDao;
import com.sies.dao.NovedadDao;
import com.sies.dao.OcupacionDao;
import com.sies.dao.RestriccionesDao;
import com.sies.dao.SiEsFactory;
import com.sies.dao.UtilidadSiEsDao;
import com.sies.dao.VacacionesDao;

public class PostgresqlSiEsFactory extends SiEsFactory
{
	@Override
	public IngresarTurnoDao getIngresarTurnoDao()
	{
		return new PostgresqlIngresarTurnoDao();
	}

	@Override
	public UtilidadSiEsDao getUtilidadSiEsDao()
	{
		return new PostgresqlUtilidadSiEsDao();
	}

	@Override
	public CategoriaDao getCategoriaDao() {
		return new PostgresqlCategoriaDao();
	}

	@Override
	public NovedadDao getNovedadDao() {
		return new PostgresqlNovedadDao();
	}
	
	@Override
	public OcupacionDao getOcupacionDao() {
		return new PostgresqlOcupacionDao();
	}

	@Override
	public NominaDao getNominaDao()
	{
		return new PostgresqlNominaDao();
	}

	@Override
	public CuadroTurnosDao getCuadroTurnosDao()
	{
		return new PostgresqlCuadroTurnosDao();
	}

	@Override
	public RestriccionesDao getRestriccionesDao()
	{
		return new PostgresqlRestriccionesDao();
	}

	@Override
	public VacacionesDao getVacacionesDao()
	{
		return new PostgresqlVacacionesDao();
	}

	@Override
	public LogsSiesDao getLogsSiesDao()
	{
		return new PostgresqlLogsSiesDao();
	}

}
