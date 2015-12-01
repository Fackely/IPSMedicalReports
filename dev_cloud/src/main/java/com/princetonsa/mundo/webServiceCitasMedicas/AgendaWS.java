/**
 * @author Jorge Armando Osorio Velasquez
 */
package com.princetonsa.mundo.webServiceCitasMedicas;

import java.util.HashMap;

import util.ConstantesBD;
import util.Utilidades;

/**
 * @author Jorge Armando Osorio Velasquez
 */
public class AgendaWS
{

	/**
	 * Metodo que retorna las agendas de una espeicialidad
	 * @param codigoEspecialidad
	 * @return
	 */
	public static HashMap consultarAgendaEspecialidad(String codigoEspecialidad)
	{
		return Utilidades.obtenerAgendaDisponibleMedicoUnidadConsulta(codigoEspecialidad,ConstantesBD.codigoNuncaValido+"");
	}

	/**
	 * Metodo que retorna las agendas de una espeicialidad Y un Medico
	 * @param codigoEspecialidad
	 * @param codigoMedico
	 * @return
	 */
	public static HashMap consultarAgendaEspecialidad(String codigoEspecialidad, String codigoMedico)
	{
		return Utilidades.obtenerAgendaDisponibleMedicoUnidadConsulta(codigoEspecialidad,codigoMedico);
	}

}
