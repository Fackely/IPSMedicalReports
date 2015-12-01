/**
 * 
 */
package com.princetonsa.dao.oracle.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.facturacion.ExcepcionesTarifas1Dao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseExcepcionesTarifasDao;

/**
 * @author axioma
 *
 */
public class OracleExcepcionesTarifas1Dao implements ExcepcionesTarifas1Dao 
{
	
	/**
	 * 
	 */
	public HashMap obtenerExcepciones(Connection con, int institucion, int contrato, boolean vigentes, boolean todas)
	{
		return SqlBaseExcepcionesTarifasDao.obtenerExcepciones(con,institucion, contrato,vigentes,todas);
	}

	/**
	 * 
	 */
	public boolean eliminareExepciones(Connection con, String codigo)
	{
		return SqlBaseExcepcionesTarifasDao.eliminareExepciones(con, codigo);
	}

	/**
	 * 
	 */
	public HashMap consultarExcepcionLLave(Connection con, String codigo)
	{
		return SqlBaseExcepcionesTarifasDao.consultaExcepcionLLave(con, codigo);
	}

	/**
	 * 
	 */
	public boolean insertarExcepcion(Connection con, HashMap vo)
	{
		String cadena="INSERT INTO excep_tarifas_contrato(codigo,codigo_contrato,institucion,via_ingreso,tipo_paciente,tipo_complejidad,usuario_modifica,fecha_modifica,hora_modifica,fecha_vigencia,observaciones,centro_atencion) values(seq_excep_tarifas_contrato.nextval,?,?,?,?,?,?,?,?,?,?,?)";
		return SqlBaseExcepcionesTarifasDao.insertarExcepcion(con, vo, cadena);
	}

	/**
	 * 
	 */
	public boolean modificarExcepcion(Connection con, HashMap vo)
	{
		return SqlBaseExcepcionesTarifasDao.modificarExcepcion(con, vo);
	}
	
	/**
	 * 
	 */
	public HashMap consultarAgrupacionArticulos(Connection con, String codigo)
	{
		return SqlBaseExcepcionesTarifasDao.consultarAgrupacionArticulos(con,codigo);
	}

	/**
	 * 
	 */
	public HashMap consultarAgrupacionServicios(Connection con, String codigo)
	{
		return SqlBaseExcepcionesTarifasDao.consultarAgrupacionServicios(con,codigo);
	}

	/**
	 * 
	 */
	public HashMap consultarArticulos(Connection con, String codigo)
	{
		return SqlBaseExcepcionesTarifasDao.consultarArticulos(con,codigo);
	}

	/**
	 * 
	 */
	public HashMap consultarServicios(Connection con, String codigo)
	{
		return SqlBaseExcepcionesTarifasDao.consultarServicios(con,codigo);
	}
	
	/**
	 * 
	 */
	public HashMap consultarAgrupacionArticulosLLave(Connection con, String codigo)
	{
		return SqlBaseExcepcionesTarifasDao.consultarAgrupacionArticulosLLave(con,codigo);
	}

	/**
	 * 
	 */
	public HashMap consultarArticulosLLave(Connection con, String codigo)
	{
		return SqlBaseExcepcionesTarifasDao.consultarArticulosLLave(con,codigo);
	}

	
	/**
	 * 
	 */
	public HashMap consultarAgrupacionServiciosLLave(Connection con, String codigo)
	{
		return SqlBaseExcepcionesTarifasDao.consultarAgrupacionServiciosLLave(con,codigo);
	}

	
	/**
	 * 
	 */
	public HashMap consultarServiciosLLave(Connection con, String codigo)
	{
		return SqlBaseExcepcionesTarifasDao.consultarServiciosLLave(con,codigo);
	}
	

	/**
	 * 
	 */
	public boolean eliminarAgrupacionArticulos(Connection con, String codigoAgrupacion)
	{
		return SqlBaseExcepcionesTarifasDao.eliminarAgrupacionArticulos(con,codigoAgrupacion);
	}
	

	/**
	 * 
	 */
	public boolean modificarAgrupacionArticulos(Connection con, HashMap vo)
	{
		return SqlBaseExcepcionesTarifasDao.modificarAgrupacionArticulos(con,vo);
	}

	/**
	 * 
	 */
	public int insertarAgrupacionArticulos(Connection con, HashMap vo)
	{
		String cadena="INSERT INTO agru_art_exce_tari_cont(codigo,codigo_excepcion,clase,grupo,subgrupo,naturaleza,institucion,valor_ajuste,base_excepcion,nueva_tarifa,usuario_modifica,fecha_modifica,hora_modifica,fecha_vigencia) values(seq_agru_art_exce_tari_cont.nextval,?,?,?,?,?,?,?,?,?,?,?,?)";
		return SqlBaseExcepcionesTarifasDao.insertarAgrupacionArticulos(con,vo);
	}

	/**
	 * 
	 */
	public boolean eliminarArticulos(Connection con, String codigoComponente)
	{
		return SqlBaseExcepcionesTarifasDao.eliminarArticulos(con,codigoComponente);
	}

	/**
	 * 
	 */
	public boolean modificarArticulos(Connection con, HashMap vo)
	{
		return SqlBaseExcepcionesTarifasDao.modificarArticulos(con,vo);
	}

	/**
	 * 
	 */
	public int insertarArticulos(Connection con, HashMap vo)
	{
		String cadena="INSERT INTO art_exce_tari_cont (codigo,codigo_excepcion,codigo_articulo,valor_ajuste,base_excepcion,nueva_tarifa,usuario_modifica,fecha_modifica,hora_modifica,fecha_vigencia) values (seq_art_exce_tari_cont.nextval,?,?,?,?,?,?,?,?)";
		return SqlBaseExcepcionesTarifasDao.insertarArticulos(con,vo);
	}

	/**
	 * 
	 */
	public boolean eliminarAgrupacionServicios(Connection con, String codigoAgrupacion)
	{
		return SqlBaseExcepcionesTarifasDao.eliminarAgrupacionServicios(con,codigoAgrupacion);
	}

	/**
	 * 
	 */
	public int insertarAgrupacionServicios(Connection con, HashMap vo)
	{
		String cadena="INSERT INTO agru_ser_exce_tari_cont(codigo,codigo_excepcion,pos,grupo_servicio,tipo_servicio,especialidad,valor_ajuste,nueva_tarifa,usuario_modifica,fecha_modifica,hora_modifica,fecha_vigencia) values (seq_agru_ser_exce_tari_cont.nextval,?,?,?,?,?,?,?,?,?,?)";
		return SqlBaseExcepcionesTarifasDao.insertarAgrupacionServicios(con,vo);
	}

	/**
	 * 
	 */
	public boolean modificarAgrupacionServicios(Connection con, HashMap vo)
	{
		return SqlBaseExcepcionesTarifasDao.modificarAgrupacionServicios(con,vo);
	}
	

	/**
	 * 
	 */
	public boolean eliminarServicios(Connection con, String codigoComponente)
	{
		return SqlBaseExcepcionesTarifasDao.eliminarServicios(con,codigoComponente);
	}

	
	/**
	 * 
	 */
	public int insertarServicios(Connection con, HashMap vo)
	{
		String cadena="INSERT INTO ser_exce_tari_cont(codigo,codigo_excepcion,codigo_servicio,valor_ajuste,nueva_tarifa,usuario_modifica,fecha_modifica,hora_modifica,fecha_vigencia) values (seq_ser_exce_tari_cont.nextval,?,?,?,?,?,?,?)";
		return SqlBaseExcepcionesTarifasDao.insertarServicios(con,vo);
	}

	/**
	 * 
	 */
	public boolean modificarServicios(Connection con, HashMap vo)
	{
		return SqlBaseExcepcionesTarifasDao.modificarServicios(con,vo);
	}

	/**
	 * 
	 */
	public HashMap<String, Object> consultarPorcentaje(Connection con, String codigosPKSeccion, int codigoSeccion) 
	{
		return SqlBaseExcepcionesTarifasDao.consultarPorcentaje(con,codigosPKSeccion,codigoSeccion);
	}

	/**
	 * 
	 */
	public boolean insertarPorcentaje(Connection con, HashMap vo, int codigoSeccion) 
	{
		return SqlBaseExcepcionesTarifasDao.insertarPorcentaje(con,vo,codigoSeccion);
	}

	/**
	 * 
	 */
	public boolean modificarPorcentaje(Connection con, HashMap vo, int codigoSeccion) 
	{
		return SqlBaseExcepcionesTarifasDao.modificarPorcentaje(con,vo,codigoSeccion);
	}
	

	/**
	 * 
	 */
	public boolean eliminarPorcentajes(Connection con, String codigo,int codigoSeccion) 
	{
		return SqlBaseExcepcionesTarifasDao.eliminarPorcentajes(con,codigo,codigoSeccion);
	}
	

	public HashMap consultarPorcentajeLLave(Connection con, int codigo, int codigoSeccion, int posRegistroExcepcion) 
	{
		return SqlBaseExcepcionesTarifasDao.consultarPorcentajeLLave(con,codigo,codigoSeccion,posRegistroExcepcion);
	}
}
