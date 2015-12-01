package com.princetonsa.mundo.consultaExterna;

import java.util.HashMap;
import org.apache.log4j.Logger;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.consultaExterna.ConsultaMultasAgendaCitasDao;



public class ConsultaMultasAgendaCitas {

	private ConsultaMultasAgendaCitasDao consultaMultasAgendaCitasDao;
	public String[] indicesListado={
			"consecutivo_",
			"fechaGeneracion_",
			"estado_",
			"valor_",
			"paciente_",
			"idd_",
			"unidad_",
			"fecha_",
			"hora_",
			"estadocita_",
			"medico_",
			};
			

	private static ConsultaMultasAgendaCitasDao getConsultaMultasAgendaCitasDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaMultasAgendaCitasDao();
	}
	
	public HashMap consultaMultasAgendaCitas(String fechaInicial, String fechaFinal, HashMap<String, Object> elmapa) {
		
		return getConsultaMultasAgendaCitasDao().consultaMultasAgendaCitas(fechaInicial,fechaFinal,elmapa);
		
	}

	public HashMap<String, Object> consultaMultasAgendaCitasPaciente(
			int codigoPersona, String codigoinstitucion) {
		
		return getConsultaMultasAgendaCitasDao().consultaMultasAgendaCitasPaciente(codigoPersona,codigoinstitucion);
	}
	
}