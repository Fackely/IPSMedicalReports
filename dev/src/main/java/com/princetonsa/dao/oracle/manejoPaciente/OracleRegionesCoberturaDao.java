package com.princetonsa.dao.oracle.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dao.manejoPaciente.RegionesCoberturaDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseRegionesCoberturaDao;
import com.princetonsa.dto.manejoPaciente.DtoRegionesCobertura;

public class OracleRegionesCoberturaDao implements RegionesCoberturaDao {

	@Override
	public ArrayList<DtoRegionesCobertura> cargar(DtoRegionesCobertura dtoWhere) {
		
		return SqlBaseRegionesCoberturaDao.cargar(dtoWhere);
	}

}
