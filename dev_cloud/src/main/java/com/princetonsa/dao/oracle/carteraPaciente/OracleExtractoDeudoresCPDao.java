package com.princetonsa.dao.oracle.carteraPaciente;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dao.carterapaciente.EdadCarteraPacienteDao;
import com.princetonsa.dao.carterapaciente.ExtractoDeudoresCPDao;
import com.princetonsa.dao.sqlbase.carteraPaciente.SqlBaseExtractoDeudoresCPDao;
import com.princetonsa.dto.carteraPaciente.DtoDeudor;
import com.princetonsa.dto.carteraPaciente.DtoExtractosDeudoresCP;

public class OracleExtractoDeudoresCPDao implements ExtractoDeudoresCPDao 
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