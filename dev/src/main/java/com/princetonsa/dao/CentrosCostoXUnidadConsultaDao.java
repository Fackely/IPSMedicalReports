package com.princetonsa.dao;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

public interface CentrosCostoXUnidadConsultaDao
{
	/**
	 * Método para consultar las
	 * opciones desplegadas en los select
	 * @param con
	 * @param tipoConsulta
	 * @param institucion
	 * @param centroAtencion
	 * @return
	 */
	public Collection consultarListados(Connection con, int tipoConsulta, int institucion, int centroAtencion);
	
	/**
	 * Método que consulta los centros de costo por unidad de consulta
	 * de acuerdo con el centro de atencion entregado
	 * @param con
	 * @param institucion
	 * @param centroAtencion
	 * @return Mapa con los registros
	 */
	public HashMap consultarRegistros(Connection con, int institucion, int centroAtencion);

	/**
	 * Método para guardar los datos en la BD
	 * @param con
	 * @param elementos
	 * @param centroAtencion @todo
	 * @return true si se insertó bien
	 */
	public boolean guardar(Connection con, Vector elementos, int centroAtencion);

}
