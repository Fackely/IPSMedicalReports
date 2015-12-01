package com.princetonsa.dao.consultaExterna;

import java.util.HashMap;

import com.princetonsa.mundo.UsuarioBasico;

public interface ConsultaMultasAgendaCitasDao {

	public HashMap consultaMultasAgendaCitas(String fechaInicial, String fechaFinal, HashMap<String, Object> elmapa);

	public HashMap<String, Object> consultaMultasAgendaCitasPaciente(
			int codigoPersona, String codigoinstitucion);

}
