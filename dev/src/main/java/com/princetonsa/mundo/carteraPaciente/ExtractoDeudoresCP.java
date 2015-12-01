package com.princetonsa.mundo.carteraPaciente;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.carterapaciente.ExtractoDeudoresCPDao;
import com.princetonsa.dto.carteraPaciente.DtoDeudor;
import com.princetonsa.dto.carteraPaciente.DtoExtractosDeudoresCP;

public class ExtractoDeudoresCP {
	
	static Logger logger = Logger.getLogger(ExtractoDeudoresCP.class);
	
	public static ExtractoDeudoresCPDao geExtractoDeudoresCPDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getExtractoDeudoresCPDao();
	}
	
	public static ArrayList<DtoExtractosDeudoresCP> consultaDatosDeudor(DtoDeudor dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getExtractoDeudoresCPDao().consultaDatosDeudor(dto);    
	}
	
	public static ArrayList<DtoExtractosDeudoresCP> consultaExtractosDeudor(DtoDeudor dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getExtractoDeudoresCPDao().consultaExtractosDeudor(dto);
	}
}