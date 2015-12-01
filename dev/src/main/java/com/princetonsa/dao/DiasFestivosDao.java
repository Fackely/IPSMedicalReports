/*
 * Creado el 12/04/2005
 * Juan David Ramírez López
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.Collection;

/**
 * @author Juan David Ramírez
 * 
 * CopyRight Princeton S.A.
 * 12/04/2005
 */
public interface DiasFestivosDao
{
	/**
	 * Método para inserar un día festivo
	 * @param con
	 * @param fecha
	 * @param descripcion
	 * @param tipo
	 * @return número de elementos ingresados
	 */
	public int insertar(Connection con, String fecha, String descripcion, int tipo);

	/**
	 * Método para misdificar un día festivo
	 * @param con
	 * @param fecha
	 * @param descripcion
	 * @param tipo
	 * @return número de elementos modificados
	 */
	public int modificar(Connection con, String fecha, String descripcion, int tipo);

	/**
	 * Método para eliminar un día festivo
	 * @param con
	 * @param fecha
	 * @return numero de elementos eliminados
	 */
	public int eliminar(Connection con, String fecha);

	/**
	 * Método para listar todos los días desftivos parametrizados para determinado año
	 * @param con Conexion con la BD
	 * @param anio Año que se quiere consultar
	 * @return Collection con el listado de todos los días festivos
	 */
	public Collection listar(Connection con, String anio);

	/**
	 * Método para cargar un día festivo
	 * @param con Conexión con la BD
	 * @param fecha Fecha del día festivo que se desea cargar
	 * @param incluirDomingos @todo
	 * @return true si cargó el día festivo
	 */
	public Collection cargar(Connection con, String fecha, boolean incluirDomingos);
}
