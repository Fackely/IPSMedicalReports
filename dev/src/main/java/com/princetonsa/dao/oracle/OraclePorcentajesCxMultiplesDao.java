/*
 *Sep 09 / 2005
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.PorcentajesCxMultiplesDao;
import com.princetonsa.dao.sqlbase.SqlBasePorcentajesCxMultiplesDao;

/**
 * @author Sebasti�n G�mez
 *
 * Clase que maneja los m�todos prop�os de Oracle para el acceso a la fuente
 * de datos en la funcionalidad Parametrizaci�n de Porcentajes Cirug�as M�ltiples
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
	 * M�todo para cargar el listado de los procentajes de cirug�as
	 * m�ltiples parametrizadas por instituci�n
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap cargarEncaPorcentajes(Connection con,int institucion,HashMap parametros)
	{
		return SqlBasePorcentajesCxMultiplesDao.cargarEncaPorcentajes(con, institucion, parametros);
	}
	
	/**
	 * M�todo para cargar el listado de los procentajes de cirug�as
	 * m�ltiples parametrizadas por instituci�n
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap cargarPorcentajes(Connection con,int institucion,HashMap parametros)
	{
		return SqlBasePorcentajesCxMultiplesDao.cargarPorcentajes(con, institucion, parametros);
	}
	
	
	/**
	 * M�todo usado para modificar un registros de encabezado de porcentajes
	 * de cirug�as m�ltiples
	 * @param con
	 * @param HashMap parametros	  
	 * @return
	 */
	public int actualizarEncaPorcentaje(Connection con,HashMap parametros)
	{
		return SqlBasePorcentajesCxMultiplesDao.actualizarEncaPorcentaje(con, parametros);
	}
	
	/**
	 * M�todo usado para modificar un registros de los porcentajes
	 * de cirug�as m�ltiples
	 * @param con
	 * @param HashMap parametros	  
	 * @return
	 */
	public int actualizarPorcentaje(Connection con,HashMap parametros)
	{
		return SqlBasePorcentajesCxMultiplesDao.actualizarPorcentaje(con, parametros);
	}
	
	
	/**
	 * M�todo para eliminar un porcentaje de cirug�a m�ltiple
	 * @param con
	 * @param codigo
	 * @return
	 */
	public int eliminarEncaPorcentaje(Connection con,int codigo)
	{
		return SqlBasePorcentajesCxMultiplesDao.eliminarEncaPorcentaje(con, codigo);
	}
	
	/**
	 * M�todo para eliminar un porcentaje de cirug�a m�ltiple
	 * @param con
	 * @param codigo
	 * @return
	 */
	public int eliminarPorcentaje(Connection con,int codigo)
	{
		return SqlBasePorcentajesCxMultiplesDao.eliminarPorcentaje(con, codigo);
	}
	
	
	
	/**
	 * M�todo usado para insertar un nuevo Encabezado de Porcentraje de cirugia m�ltiple
	 * @param con
	 * @param HashMap parametros
	 * @return retorna -1 o 0 si la transacci�n no fue exitosa
	 */
	public int insertarEncaPorcentaje(Connection con,HashMap parametros)
	{
		return SqlBasePorcentajesCxMultiplesDao.insertarEncaPorcentaje(con, parametros, insertarEncaPorcentajeStr);
	}	
	
	
	/**
	 * M�todo usado para insertar un nuevo Porcentraje de cirugia m�ltiple
	 * @param con
	 * @param HashMap parametros
	 * @return retorna -1 o 0 si la transacci�n no fue exitosa
	 */
	public int insertarPorcentaje(Connection con,HashMap parametros)
	{
		return SqlBasePorcentajesCxMultiplesDao.insertarPorcentaje(con, parametros, insertarPorcentajeStr);
	}
	
	
	/**
	 * M�todo usao para cargar un registro de los porcentajes
	 * de cirug�as m�ltiples por su c�digo
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap cargarPorcentaje(Connection con,int codigo)
	{
		return SqlBasePorcentajesCxMultiplesDao.cargarPorcentaje(con, codigo);
	}
	
	
	/**
	 * M�todo usado para la b�squeda de registros en 
	 * Consultar Porcentajes Cx M�ltiples.
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