package com.princetonsa.dao.oracle.carteraPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.capitacion.EdadCarteraCapitacionDao;
import com.princetonsa.dao.carterapaciente.EdadCarteraPacienteDao;
import com.princetonsa.dao.sqlbase.carteraPaciente.SqlBaseEdadCarteraPacienteDao;
import com.princetonsa.dto.carteraPaciente.DtoDatosFinanciacion;

public class OracleEdadCarteraPacienteDao implements EdadCarteraPacienteDao {
	
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
