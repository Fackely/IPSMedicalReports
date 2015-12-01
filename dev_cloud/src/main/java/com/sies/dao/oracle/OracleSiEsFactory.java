package com.sies.dao.oracle;

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

public class OracleSiEsFactory extends SiEsFactory
{

	@Override
	public IngresarTurnoDao getIngresarTurnoDao()
	{
		return new OracleIngresarTurnoDao();
	}

	@Override
	public UtilidadSiEsDao getUtilidadSiEsDao()
	{
		return new OracleUtilidadSiEsDao();
	}

	@Override
	public CategoriaDao getCategoriaDao() {
		return new OracleCategoriaDao();
	}

	@Override
	public NovedadDao getNovedadDao() {
		return new OracleNovedadDao();
	}

	@Override
	public OcupacionDao getOcupacionDao() {
		return new OracleOcupacionDao();
	}

	@Override
	public NominaDao getNominaDao()
	{
		return new OracleNominaDao();
	}

	@Override
	public CuadroTurnosDao getCuadroTurnosDao()
	{
		return new OracleCuadroTurnosDao();
	}

	@Override
	public RestriccionesDao getRestriccionesDao()
	{
		return new OracleRestriccionesDao();
	}

	@Override
	public VacacionesDao getVacacionesDao()
	{
		return new OracleVacacionesDao();
	}

	@Override
	public LogsSiesDao getLogsSiesDao()
	{
		return new OracleLogsSiesDao();
	}
}
