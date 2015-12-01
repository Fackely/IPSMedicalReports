package com.princetonsa.mundo.webServiceCitasMedicas;

import java.util.HashMap;

import util.Utilidades;

public class PacienteWS
{

	/**
	 * 
	 * @param numeroDocumento
	 * @return
	 */
	public static HashMap consultarPaciente(String numeroDocumento)
	{
		return Utilidades.consultarDatosGeneralesPaciente(numeroDocumento);
	}

}
