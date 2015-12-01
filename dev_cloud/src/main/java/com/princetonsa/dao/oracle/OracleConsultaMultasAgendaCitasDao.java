package com.princetonsa.dao.oracle;

import java.util.HashMap;

import com.princetonsa.dao.consultaExterna.ConsultaMultasAgendaCitasDao;
import com.princetonsa.dao.sqlbase.consultaExterna.SqlBaseConsultaMultasAgendaCitasDao;

public class OracleConsultaMultasAgendaCitasDao implements
		ConsultaMultasAgendaCitasDao {

	
	public HashMap consultaMultasAgendaCitas(String fechaInicial, String fechaFinal,HashMap<String, Object> elmapa) {
		
		return SqlBaseConsultaMultasAgendaCitasDao.consultaMultasAgendaCitas(fechaInicial,fechaFinal,elmapa);
	}

	public HashMap<String, Object> consultaMultasAgendaCitasPaciente(
			int codigoPersona, String codigoinstitucion) {
	
		return SqlBaseConsultaMultasAgendaCitasDao.consultaMultasAgendaCitasPaciente(codigoPersona,codigoinstitucion);
	}

}
