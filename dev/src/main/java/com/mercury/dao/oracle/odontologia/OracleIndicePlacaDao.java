package com.mercury.dao.oracle.odontologia;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import com.mercury.dao.odontologia.IndicePlacaDao;
import com.mercury.dao.sqlbase.odontologia.SqlBaseIndicePlacaDao;

public class OracleIndicePlacaDao implements IndicePlacaDao
{
	@Override
	public int insertar(Connection con, String codTratamientoOdo, String observaciones, int codMedico, String fecha, int numeroSuperficies, int numeroDientes) throws SQLException
	{
		return SqlBaseIndicePlacaDao.insertar(con, codTratamientoOdo, observaciones, codMedico, fecha, numeroSuperficies, numeroDientes);
	}

	@Override
	public Collection<HashMap<String, Object>> consultarIndicesPlacaTratamiento(Connection con, int codTratamiento) throws SQLException
	{
		return SqlBaseIndicePlacaDao.consultarIndicesPlacaTratamiento(con, codTratamiento);
	}

	@Override
	public Collection<HashMap<String, Object>> consultar(Connection con, int codigo) throws SQLException
	{
		return SqlBaseIndicePlacaDao.consultar(con, codigo);
	}

	@Override
	public int modificar(Connection con, int codigo, String observaciones)
			throws SQLException
	{
		return SqlBaseIndicePlacaDao.modificar(con, codigo, observaciones);
	}

	@Override
	public int insertarSectorDientePlaca(Connection con, int codIndicePlaca, int numeroDiente, int numeroSector) throws SQLException
	{
		return SqlBaseIndicePlacaDao.insertarSectorDientePlaca(con, codIndicePlaca, numeroDiente, numeroSector);
	}

	@Override
	public Collection<HashMap<String, Object>> consultarSectoresDientesPlaca(Connection con, int codIndicePlaca) throws SQLException
	{
		return SqlBaseIndicePlacaDao.consultarSectoresDientesPlaca(con, codIndicePlaca);
	}

	@Override
	public int consultarNumeroSuperficies(Connection con, int codigoIndicePlaca)
	{
		return SqlBaseIndicePlacaDao.consultarNumeroSuperficies(con, codigoIndicePlaca);
	}

}
