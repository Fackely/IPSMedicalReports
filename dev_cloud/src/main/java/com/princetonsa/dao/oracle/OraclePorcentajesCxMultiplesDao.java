/*
 *Sep 09 / 2005
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.PorcentajesCxMultiplesDao;
import com.princetonsa.dao.sqlbase.SqlBasePorcentajesCxMultiplesDao;

/**
 * @author Sebastián Gómez
 *
 * Clase que maneja los métodos propìos de Oracle para el acceso a la fuente
 * de datos en la funcionalidad Parametrización de Porcentajes Cirugías Múltiples
 */
public class OraclePorcentajesCxMultiplesDao implements
		PorcentajesCxMultiplesDao {
		
	/**
	 * Cadena usada para insertar un nuevo registro en la
	 * tabla porcentajes_cx_multi
	 */
	private final String insertarPorcentajeStr="INSERT INTO porcentajes_cx_multi " +
			"(codigo,codigo_encab,tipo_cirugia,tipo_asocio,tipo_especialista,via_acceso,liquidacion,adicional,politraumatismo,tipo_servicio,tipo_sala,usuario_modifica,fecha_modifica,hora_modifica) " +
			"VALUES(seq_porcentajes_cx_multi.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * Cadena usada para insertar un nuevo registro en la
	 * tabla enca_porcen_cx_multi
	 */
	private final String insertarEncaPorcentajeStr="INSERT INTO enca_porcen_cx_multi " +
			"(codigo,institucion,esq_tar_particular,esq_tar_general,convenio,fecha_inicial,fecha_final,usuario_modifica,fecha_modifica,hora_modifica) " +
			"VALUES(seq_enca_porcecx.nextval,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * Método para cargar el listado de los procentajes de cirugías
	 * múltiples parametrizadas por institución
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap cargarEncaPorcentajes(Connection con,int institucion,HashMap parametros)
	{
		return SqlBasePorcentajesCxMultiplesDao.cargarEncaPorcentajes(con, institucion, parametros);
	}
	
	/**
	 * Método para cargar el listado de los procentajes de cirugías
	 * múltiples parametrizadas por institución
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap cargarPorcentajes(Connection con,int institucion,HashMap parametros)
	{
		return SqlBasePorcentajesCxMultiplesDao.cargarPorcentajes(con, institucion, parametros);
	}
	
	
	/**
	 * Método usado para modificar un registros de encabezado de porcentajes
	 * de cirugías múltiples
	 * @param con
	 * @param HashMap parametros	  
	 * @return
	 */
	public int actualizarEncaPorcentaje(Connection con,HashMap parametros)
	{
		return SqlBasePorcentajesCxMultiplesDao.actualizarEncaPorcentaje(con, parametros);
	}
	
	/**
	 * Método usado para modificar un registros de los porcentajes
	 * de cirugías múltiples
	 * @param con
	 * @param HashMap parametros	  
	 * @return
	 */
	public int actualizarPorcentaje(Connection con,HashMap parametros)
	{
		return SqlBasePorcentajesCxMultiplesDao.actualizarPorcentaje(con, parametros);
	}
	
	
	/**
	 * Método para eliminar un porcentaje de cirugía múltiple
	 * @param con
	 * @param codigo
	 * @return
	 */
	public int eliminarEncaPorcentaje(Connection con,int codigo)
	{
		return SqlBasePorcentajesCxMultiplesDao.eliminarEncaPorcentaje(con, codigo);
	}
	
	/**
	 * Método para eliminar un porcentaje de cirugía múltiple
	 * @param con
	 * @param codigo
	 * @return
	 */
	public int eliminarPorcentaje(Connection con,int codigo)
	{
		return SqlBasePorcentajesCxMultiplesDao.eliminarPorcentaje(con, codigo);
	}
	
	
	
	/**
	 * Método usado para insertar un nuevo Encabezado de Porcentraje de cirugia múltiple
	 * @param con
	 * @param HashMap parametros
	 * @return retorna -1 o 0 si la transacción no fue exitosa
	 */
	public int insertarEncaPorcentaje(Connection con,HashMap parametros)
	{
		return SqlBasePorcentajesCxMultiplesDao.insertarEncaPorcentaje(con, parametros, insertarEncaPorcentajeStr);
	}	
	
	
	/**
	 * Método usado para insertar un nuevo Porcentraje de cirugia múltiple
	 * @param con
	 * @param HashMap parametros
	 * @return retorna -1 o 0 si la transacción no fue exitosa
	 */
	public int insertarPorcentaje(Connection con,HashMap parametros)
	{
		return SqlBasePorcentajesCxMultiplesDao.insertarPorcentaje(con, parametros, insertarPorcentajeStr);
	}
	
	
	/**
	 * Método usao para cargar un registro de los porcentajes
	 * de cirugías múltiples por su código
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap cargarPorcentaje(Connection con,int codigo)
	{
		return SqlBasePorcentajesCxMultiplesDao.cargarPorcentaje(con, codigo);
	}
	
	
	/**
	 * Método usado para la búsqueda de registros en 
	 * Consultar Porcentajes Cx Múltiples.
	 * @param con
	 * @param esGeneral
	 * @param esquemaTarifario
	 * @param tipoCirugia
	 * @param asocio
	 * @param medico
	 * @param via
	 * @param liquidacion
	 * @param politra
	 * @param activo
	 * @param institucion
	 * @return
	 */
	public HashMap busquedaPorcentajes(
			Connection con,
			String esGeneral,
			int esquemaTarifario,
			String tipoCirugia,
			int asocio,
			int medico,
			int via,
			float liquidacion,
			float adicional,
			float politra,
			int institucion)
	{
		return SqlBasePorcentajesCxMultiplesDao.busquedaPorcentajes(con, esGeneral, esquemaTarifario, tipoCirugia, asocio, medico, via, liquidacion, adicional, politra,  institucion);
	}
}