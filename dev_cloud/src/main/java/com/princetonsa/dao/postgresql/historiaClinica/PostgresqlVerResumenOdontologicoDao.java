package com.princetonsa.dao.postgresql.historiaClinica;

import java.util.ArrayList;

import com.princetonsa.dao.historiaClinica.VerResumenOdontologicoDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseVerResumenOdontologicoDao;
import com.princetonsa.dto.historiaClinica.componentes.DtoAntecedentesOdontologicosAnt;

public class PostgresqlVerResumenOdontologicoDao  implements VerResumenOdontologicoDao  {

	@Override
	public DtoAntecedentesOdontologicosAnt cagarAntecedentesOdontoPrevios(int codigoPaciente) {
		
		return SqlBaseVerResumenOdontologicoDao.cagarAntecedentesOdontoPrevios(codigoPaciente);
	}
           
	
	
	
}
