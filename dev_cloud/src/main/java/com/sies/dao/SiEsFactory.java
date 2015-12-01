package com.sies.dao;

import com.sies.dao.oracle.OracleSiEsFactory;
import com.sies.dao.postgresql.PostgresqlSiEsFactory;

public abstract class SiEsFactory
{
	public static SiEsFactory getDaoFactory ()
	{
		String bd=System.getProperty("TIPOBD");
		if(bd.equalsIgnoreCase("postgres") || bd.equalsIgnoreCase("postgresql"))
		{
			return new PostgresqlSiEsFactory();
		}
		else if(bd.equalsIgnoreCase("oracle"))
		{
			return new OracleSiEsFactory();
		}
		return null;
	}

	/**
	 * DAO para las utilidades
	 * @return
	 */
	public abstract UtilidadSiEsDao getUtilidadSiEsDao();

	/**
	 * DAO para ingreso de turnos
	 * @return Instancia del DAO
	 */
	public abstract IngresarTurnoDao getIngresarTurnoDao();
	public abstract NovedadDao getNovedadDao();
	public abstract CategoriaDao getCategoriaDao();
	public abstract OcupacionDao getOcupacionDao();
	public abstract NominaDao getNominaDao();
	public abstract VacacionesDao getVacacionesDao();
	public abstract RestriccionesDao getRestriccionesDao();
	public abstract CuadroTurnosDao getCuadroTurnosDao();
	public abstract LogsSiesDao getLogsSiesDao();

}
