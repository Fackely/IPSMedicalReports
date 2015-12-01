package com.princetonsa.dao.carterapaciente;

import java.sql.Connection;
import java.util.ArrayList;
import com.princetonsa.dto.carteraPaciente.DtoDeudor;
import com.princetonsa.dto.carteraPaciente.DtoExtractosDeudoresCP;

public interface ExtractoDeudoresCPDao
{
	public ArrayList<DtoExtractosDeudoresCP> consultaDatosDeudor(DtoDeudor dto);
	
	public ArrayList<DtoExtractosDeudoresCP> consultaExtractosDeudor(DtoDeudor dto);
}