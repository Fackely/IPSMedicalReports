package com.sies.dao.postgresql;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

import com.sies.dao.IngresarTurnoDao;
import com.sies.dao.sqlbase.SqlBaseIngresarTurnoDao;

public class PostgresqlIngresarTurnoDao implements IngresarTurnoDao
{
	@Override
	public Collection<HashMap<String, Object>> consultarTipos(Connection con, int tipoConsulta)
	{
		return SqlBaseIngresarTurnoDao.consultarTipos(con, tipoConsulta);
	}

	@Override
	public int guardarModificar(Connection con, String descripcion, String horaInicio, String numeroHoras, String simbolo, char tipoTurno, String colorLetra, String colorFondo, int codigo, boolean esModificar, int esFestivo, Integer centroCosto)
	{
		return SqlBaseIngresarTurnoDao.guardarModificar(con, descripcion, horaInicio, numeroHoras, simbolo, tipoTurno, colorLetra, colorFondo, codigo, esModificar, esFestivo, centroCosto);
	}

	@Override
	public int inactivar(Connection con, int codigo)
	{
		return SqlBaseIngresarTurnoDao.inactivar(con, codigo);
	}

}
