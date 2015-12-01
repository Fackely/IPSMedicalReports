package com.princetonsa.dao.postgresql;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.consultaExterna.ConsultaMultasAgendaCitasDao;
import com.princetonsa.dao.sqlbase.consultaExterna.SqlBaseConsultaMultasAgendaCitasDao;
import com.princetonsa.dao.sqlbase.consultaExterna.SqlBaseMotivosAnulacionCondonacionDao;
import com.princetonsa.mundo.consultaExterna.ConsultaMultasAgendaCitas;

public class PostgresqlConsultaMultasAgendaCitasDao implements
		ConsultaMultasAgendaCitasDao {


	public HashMap consultaMultasAgendaCitas(String fechaInicial, String fechaFinal, HashMap<String, Object> elmapa) {
		
			return SqlBaseConsultaMultasAgendaCitasDao.consultaMultasAgendaCitas(fechaInicial,fechaFinal,elmapa);
	}
	
	public HashMap<String, Object> consultaMultasAgendaCitasPaciente(
			int codigoPersona, String codigoinstitucion) {
		
		return SqlBaseConsultaMultasAgendaCitasDao.consultaMultasAgendaCitasPaciente(codigoPersona,codigoinstitucion);	}

}
