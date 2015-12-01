package com.princetonsa.dao.postgresql.historiaClinica;

import java.util.ArrayList;

import com.princetonsa.dao.historiaClinica.HistoricoAtencionesDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseHistoricoAtencionesDao;
import com.princetonsa.dto.historiaClinica.historicoAtenciones.DtoHistoricosHC;

public class PostgresHistoricoAtencionesDao implements HistoricoAtencionesDao 
{

	@Override
	public ArrayList<DtoHistoricosHC> cargarHistoricoAtenciones(int codigoPaciente) 
	{
		return SqlBaseHistoricoAtencionesDao.cargarHistoricoAtenciones(codigoPaciente);
	}

}
