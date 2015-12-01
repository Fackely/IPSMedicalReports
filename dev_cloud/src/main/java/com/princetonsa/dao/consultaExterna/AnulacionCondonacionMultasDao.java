package com.princetonsa.dao.consultaExterna;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dto.consultaExterna.DtoMultasCitas;

public interface AnulacionCondonacionMultasDao {

	ArrayList<DtoMultasCitas> obtenerCitasconMultas(int codigoPersona,String loginUsuario, String unidadesAgenda, boolean conUnidadesAgenda);

	HashMap guardarMultaCita(Connection con, HashMap parametros);

	HashMap unidadesAgendaCentrosAtencion(HashMap centrosAtencion);

	ArrayList<DtoMultasCitas> busquedaPorRangoMultasCita(String usuario,HashMap parametros);

	HashMap unidadesAgendaXCentroAtencion(int centroAtencion);
	
	

}
