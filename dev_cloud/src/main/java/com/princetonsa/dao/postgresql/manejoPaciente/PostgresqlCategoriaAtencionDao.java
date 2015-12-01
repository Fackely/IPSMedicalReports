package com.princetonsa.dao.postgresql.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dao.manejoPaciente.CategoriaAtencionDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseCategoriaAtencionDao;
import com.princetonsa.dto.manejoPaciente.DtoCategoriaAtencion;

public class PostgresqlCategoriaAtencionDao implements CategoriaAtencionDao {

	@Override
	public ArrayList<DtoCategoriaAtencion> cargar(DtoCategoriaAtencion dtoWhere) {
		
		return SqlBaseCategoriaAtencionDao.cargar(dtoWhere);
	}

}
