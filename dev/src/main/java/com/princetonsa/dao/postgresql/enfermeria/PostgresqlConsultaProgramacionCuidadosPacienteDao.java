package com.princetonsa.dao.postgresql.enfermeria;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.enfermeria.ConsultaProgramacionCuidadosPacienteDao;
import com.princetonsa.dao.sqlbase.enfermeria.SqlBaseConsultaProgramacionCuidadosPacienteDao;
import com.princetonsa.dto.enfermeria.DtoFrecuenciaCuidadoEnferia;

public class PostgresqlConsultaProgramacionCuidadosPacienteDao implements
		ConsultaProgramacionCuidadosPacienteDao {

	@Override
	public ArrayList<DtoFrecuenciaCuidadoEnferia> consultarFrecuenciaCuidado(
			Connection con, HashMap parametros) {
		
		return SqlBaseConsultaProgramacionCuidadosPacienteDao.consultarFrecuenciaCuidado(con,parametros);
	}

	@Override
	public ArrayList<HashMap<String, Object>> consultarTipoFrecuenciaInst(
			Connection con, HashMap parametros) {
		
		return SqlBaseConsultaProgramacionCuidadosPacienteDao.consultarTipoFrecuencias(con,parametros);
	}

	

}
