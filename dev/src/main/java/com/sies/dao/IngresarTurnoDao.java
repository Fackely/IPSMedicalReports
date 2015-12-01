package com.sies.dao;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;


public interface IngresarTurnoDao
{
	/**
	 * M�todo que consulta los tipos de turno existentes
	 * @param con
	 * @param tipoConsulta
	 * @return Colecci�n cno los datos
	 */
	public Collection<HashMap<String, Object>> consultarTipos(Connection con, int tipoConsulta);

	/**
	 * M�todo para generar el nuevo turno
	 * @param con
	 * @param descripcion
	 * @param horaInicio
	 * @param numeroHoras
	 * @param simbolo
	 * @param tipoTurno
	 * @param colorLetra
	 * @param colorFondo
	 * @param codigo
	 * @param esModificar
	 * @param esFestivo
	 * @param centroCosto 
	 * @return true si se guardo bien, false de lo contrario
	 */
	public int guardarModificar(Connection con, String descripcion, String horaInicio, String numeroHoras, String simbolo, char tipoTurno, String colorLetra, String colorFondo, int codigo, boolean esModificar, int esFestivo, Integer centroCosto);

	/**
	 * M�todo para eliminar un tipo de turno
	 * La eliminaci�n se hace inactivando el turno espec�fico
	 * @param con
	 * @param codigo
	 */
	public int inactivar(Connection con, int codigo);
}
