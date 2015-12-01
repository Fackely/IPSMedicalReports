package com.princetonsa.dao.postgresql.carteraPaciente;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dao.carterapaciente.ExtractoDeudoresCPDao;
import com.princetonsa.dao.sqlbase.carteraPaciente.SqlBaseExtractoDeudoresCPDao;
import com.princetonsa.dto.carteraPaciente.DtoDeudor;
import com.princetonsa.dto.carteraPaciente.DtoExtractosDeudoresCP;

public class PostgresqlExtractoDeudoresCPDao implements ExtractoDeudoresCPDao
{
	public ArrayList<DtoExtractosDeudoresCP> consultaDatosDeudor(DtoDeudor dto)
	{
		return SqlBaseExtractoDeudoresCPDao.consultaDatosDeudor(dto);
	}
	
	public ArrayList<DtoExtractosDeudoresCP> consultaExtractosDeudor(DtoDeudor dto)
	{
		return SqlBaseExtractoDeudoresCPDao.consultaExtractosDeudor(dto);
	}
}