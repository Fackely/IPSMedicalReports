package com.princetonsa.dao.oracle.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dao.manejoPaciente.CategoriaAtencionDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseCategoriaAtencionDao;

import com.princetonsa.dto.manejoPaciente.DtoCategoriaAtencion;

public class OracleCategoriaAtencionDao implements CategoriaAtencionDao {

	@Override
	public ArrayList<DtoCategoriaAtencion> cargar(DtoCategoriaAtencion dtoWhere) {
		
		return SqlBaseCategoriaAtencionDao.cargar(dtoWhere);
	}

}
