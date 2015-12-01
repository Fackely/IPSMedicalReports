package com.princetonsa.mundo.historiaClinica;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.VerResumenOdontologicoDao;
import com.princetonsa.dto.historiaClinica.componentes.DtoAntecedentesOdontologicosAnt;

public class VerResumenOdontologico {

	private static Logger logger=Logger.getLogger(VerResumenOdontologico.class);

	public static VerResumenOdontologicoDao getVerResumenOdontologicoDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getVerResumenOdontologicoDao();
	}

	public DtoAntecedentesOdontologicosAnt cagarAntecedentesOdontoPrevios(int codigoPaciente) {
		
		return getVerResumenOdontologicoDao().cagarAntecedentesOdontoPrevios(codigoPaciente);
	}
	
	
	
	
}
