/*
 * Creado el 12/04/2005
 * Juan David Ram�rez L�pez
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.util.Collection;

import com.princetonsa.dao.DiasFestivosDao;
import com.princetonsa.dao.sqlbase.SqlBaseDiasFestivos;

/**
 * @author Juan David Ram�rez
 * 
 * CopyRight Princeton S.A.
 * 12/04/2005
 */
public class PostgresqlDiasFestivosDao implements DiasFestivosDao
{

	/**
	 * M�todo para inserar un d�a festivo
	 * @param con
	 * @param fecha
	 * @param descripcion
	 * @param tipo
	 * @return n�mero de elementos ingresados
	 */
	public int insertar(Connection con, String fecha, String descripcion, int tipo)
	{
		return SqlBaseDiasFestivos.insertar(con, fecha, descripcion, tipo);
	}

	/**
	 * M�todo para misdificar un d�a festivo
	 * @param con
	 * @param fecha
	 * @param descripcion
	 * @param tipo
	 * @return n�mero de elementos modificados
	 */
	public int modificar(Connection con, String fecha, String descripcion, int tipo)
	{
		return SqlBaseDiasFestivos.modificar(con, fecha, descripcion, tipo);
	}

	/**
	 * M�todo para eliminar un d�a festivo
	 * @param con
	 * @param fecha
	 * @return numero de elementos eliminados
	 */
	public int eliminar(Connection con, String fecha)
	{
		return SqlBaseDiasFestivos.eliminar(con, fecha);
	}
	
	/**
	 * M�todo para listar todos los d�as desftivos parametrizados para determinado a�o
	 * @param con Conexion con la BD
	 * @param anio A�o que se quiere consultar
	 * @return Collection con el listado de todos los d�as festivos
	 */
	public Collection listar(Connection con, String anio)
	{
		return SqlBaseDiasFestivos.listar(con, anio);
	}

	/**
	 * M�todo para cargar un d�a festivo
	 * @param con Conexi�n con la BD
	 * @param fecha Fecha del d�a festivo que se desea cargar
	 * @return true si carg� el d�a festivo
	 */
	public Collection cargar(Connection con, String fecha, boolean incluirDomingos)
	{
		return SqlBaseDiasFestivos.cargar(con, fecha, incluirDomingos);
	}

}
