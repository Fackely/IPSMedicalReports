package com.princetonsa.dao.postgresql.consultaExterna;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.consultaExterna.AnulacionCondonacionMultasDao;
import com.princetonsa.dao.sqlbase.consultaExterna.SqlBaseAnulacionCondonacionMultasDao;
import com.princetonsa.dto.consultaExterna.DtoMultasCitas;

public class PostgresqlAnulacionCondonacionMultasDao implements
		AnulacionCondonacionMultasDao {

	@Override
	public ArrayList<DtoMultasCitas> obtenerCitasconMultas(int codigoPersona, String loginUsuario, String unidadesAgenda, boolean conUnidadesAgenda) {
		
		return SqlBaseAnulacionCondonacionMultasDao.obtenerCitasconMultas(codigoPersona,loginUsuario,unidadesAgenda, conUnidadesAgenda);
	}

	@Override
	public HashMap guardarMultaCita(Connection con, HashMap parametros) {
		
		return SqlBaseAnulacionCondonacionMultasDao.guardarMultaCita(con, parametros);
	}

	@Override
	public HashMap unidadesAgendaCentrosAtencion(HashMap centrosAtencion) {
		
		return SqlBaseAnulacionCondonacionMultasDao.unidadesAgendaCentrosAtencion(centrosAtencion);
	}

	@Override
	public ArrayList<DtoMultasCitas> busquedaPorRangoMultasCita(String usuario,
			HashMap parametros) {
		
		return SqlBaseAnulacionCondonacionMultasDao.busquedaPorRangoMultasCita(usuario, parametros);
	}

	@Override
	public HashMap unidadesAgendaXCentroAtencion(int centroAtencion) {
		
		return SqlBaseAnulacionCondonacionMultasDao.unidadesAgendaXCentroAtencion(centroAtencion);
	}
    
}
