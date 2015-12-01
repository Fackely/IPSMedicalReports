package com.princetonsa.dao.postgresql.carteraPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.carterapaciente.EdadCarteraPacienteDao;
import com.princetonsa.dao.sqlbase.carteraPaciente.SqlBaseEdadCarteraPacienteDao;
import com.princetonsa.dto.carteraPaciente.DtoDatosFinanciacion;

public class PostgresqlEdadCarteraPacienteDao implements EdadCarteraPacienteDao {
	
	/**
	 * Consulta listado de edad glosa
	 * @param Connection con
	 * @param  HashMap parametros
	 * */
	public ArrayList<DtoDatosFinanciacion> getListadoEdadGlosa(Connection con, HashMap parametros)
	{
		return SqlBaseEdadCarteraPacienteDao.getListadoEdadGlosa(con, parametros);
	}

}