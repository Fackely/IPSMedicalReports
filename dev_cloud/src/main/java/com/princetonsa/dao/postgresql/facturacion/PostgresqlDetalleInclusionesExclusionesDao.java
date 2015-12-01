/*
 * Creado May 22, 2007
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * PostgresqlDetalleInclusionesExclusionesDao
 * com.princetonsa.dao.postgresql.facturacion
 * java version "1.5.0_07"
 */
package com.princetonsa.dao.postgresql.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.facturacion.DetalleInclusionesExclusionesDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseDetalleInclusionesExclusionesDao;

/**
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * May 22, 2007
 */
public class PostgresqlDetalleInclusionesExclusionesDao implements DetalleInclusionesExclusionesDao
{

	/**
	 * 
	 */
	public HashMap consultarInclusionesExclusiones(Connection con, int institucion)
	{
		return SqlBaseDetalleInclusionesExclusionesDao.consultarInclusionesExclusiones(con,institucion);
	}

	/**
	 * 
	 */
	public HashMap consultarInclusionExclusionLLave(Connection con, String codigo)
	{
		return SqlBaseDetalleInclusionesExclusionesDao.consultarInclusionExclusionLLave(con,codigo);
	}

	/**
	 * 
	 */
	public boolean eliminarInclusionesExclusiones(Connection con, String codigo)
	{
		return SqlBaseDetalleInclusionesExclusionesDao.eliminarInclusionesExclusiones(con,codigo);
	}

	/**
	 * 
	 */
	public boolean insertarInclusionExclusion(Connection con, HashMap vo)
	{
		String cadena="INSERT INTO inclu_exclu_cc (codigo,codigo_inclu_exclu,institucion,codigo_centro_costo,usuario_modifica,fecha_modifica,hora_modifica) values(nextval('seq_inclu_exclu_cc'),?,?,?,?,?,?)";
		return SqlBaseDetalleInclusionesExclusionesDao.insertarInclusionExclusion(con,vo,cadena);
	}

	/**
	 * 
	 */
	public boolean modificarInclusionExclusion(Connection con, HashMap vo)
	{
		return SqlBaseDetalleInclusionesExclusionesDao.modificarInclusionExclusion(con,vo);
	}
	

	/**
	 * 
	 */
	public HashMap consultarAgrupacionArticulos(Connection con, String codigo)
	{
		return SqlBaseDetalleInclusionesExclusionesDao.consultarAgrupacionArticulos(con,codigo);
	}

	/**
	 * 
	 */
	public HashMap consultarAgrupacionServicios(Connection con, String codigo)
	{
		return SqlBaseDetalleInclusionesExclusionesDao.consultarAgrupacionServicios(con,codigo);
	}

	/**
	 * 
	 */
	public HashMap consultarArticulos(Connection con, String codigo)
	{
		return SqlBaseDetalleInclusionesExclusionesDao.consultarArticulos(con,codigo);
	}

	/**
	 * 
	 */
	public HashMap consultarServicios(Connection con, String codigo)
	{
		return SqlBaseDetalleInclusionesExclusionesDao.consultarServicios(con,codigo);
	}
	
	/**
	 * 
	 */
	public HashMap consultarAgrupacionArticulosLLave(Connection con, String codigo)
	{
		return SqlBaseDetalleInclusionesExclusionesDao.consultarAgrupacionArticulosLLave(con,codigo);
	}

	/**
	 * 
	 */
	public HashMap consultarArticulosLLave(Connection con, String codigo)
	{
		return SqlBaseDetalleInclusionesExclusionesDao.consultarArticulosLLave(con,codigo);
	}

	
	/**
	 * 
	 */
	public HashMap consultarAgrupacionServiciosLLave(Connection con, String codigo)
	{
		return SqlBaseDetalleInclusionesExclusionesDao.consultarAgrupacionServiciosLLave(con,codigo);
	}

	
	/**
	 * 
	 */
	public HashMap consultarServiciosLLave(Connection con, String codigo)
	{
		return SqlBaseDetalleInclusionesExclusionesDao.consultarServiciosLLave(con,codigo);
	}
	

	/**
	 * 
	 */
	public boolean eliminarAgrupacionArticulos(Connection con, String codigoAgrupacion)
	{
		return SqlBaseDetalleInclusionesExclusionesDao.eliminarAgrupacionArticulos(con,codigoAgrupacion);
	}
	

	/**
	 * 
	 */
	public boolean modificarAgrupacionArticulos(Connection con, HashMap vo)
	{
		return SqlBaseDetalleInclusionesExclusionesDao.modificarAgrupacionArticulos(con,vo);
	}

	/**
	 * 
	 */
	public boolean insertarAgrupacionArticulos(Connection con, HashMap vo)
	{
		String cadena="INSERT INTO agru_art_incluexclu_cc(codigo,codigo_inclu_exclu_cc,clase,grupo,subgrupo,naturaleza,institucion,incluye,cantidad,usuario_modifica,fecha_modifica,hora_modifica) values(nextval('seq_agru_art_incluexclu_cc'),?,?,?,?,?,?,?,?,?,?,?)";
		return SqlBaseDetalleInclusionesExclusionesDao.insertarAgrupacionArticulos(con,vo,cadena);
	}

	/**
	 * 
	 */
	public boolean eliminarArticulos(Connection con, String codigoComponente)
	{
		return SqlBaseDetalleInclusionesExclusionesDao.eliminarArticulos(con,codigoComponente);
	}

	/**
	 * 
	 */
	public boolean modificarArticulos(Connection con, HashMap vo)
	{
		return SqlBaseDetalleInclusionesExclusionesDao.modificarArticulos(con,vo);
	}

	/**
	 * 
	 */
	public boolean insertarArticulos(Connection con, HashMap vo)
	{
		String cadena="INSERT INTO art_incluexclu_cc (codigo,codigo_inclu_exclu_cc,codigo_articulo,incluye,cantidad,usuario_modifica,fecha_modifica,hora_modifica) values (nextval('seq_art_incluexclu_cc'),?,?,?,?,?,?,?)";
		return SqlBaseDetalleInclusionesExclusionesDao.insertarArticulos(con,vo,cadena);
	}

	/**
	 * 
	 */
	public boolean eliminarAgrupacionServicios(Connection con, String codigoAgrupacion)
	{
		return SqlBaseDetalleInclusionesExclusionesDao.eliminarAgrupacionServicios(con,codigoAgrupacion);
	}

	/**
	 * 
	 */
	public boolean insertarAgrupacionServicios(Connection con, HashMap vo)
	{
		String cadena="INSERT INTO agru_ser_incluexclu_cc(codigo,codigo_inclu_exclu_cc,pos,grupo_servicio,tipo_servicio,especialidad,incluye,cantidad,usuario_modifica,fecha_modifica,hora_modifica) values (nextval('seq_agru_ser_incluexclu_cc'),?,?,?,?,?,?,?,?,?,?)";
		return SqlBaseDetalleInclusionesExclusionesDao.insertarAgrupacionServicios(con,vo,cadena);
	}

	/**
	 * 
	 */
	public boolean modificarAgrupacionServicios(Connection con, HashMap vo)
	{
		return SqlBaseDetalleInclusionesExclusionesDao.modificarAgrupacionServicios(con,vo);
	}
	

	/**
	 * 
	 */
	public boolean eliminarServicios(Connection con, String codigoComponente)
	{
		return SqlBaseDetalleInclusionesExclusionesDao.eliminarServicios(con,codigoComponente);
	}

	
	/**
	 * 
	 */
	public boolean insertarServicios(Connection con, HashMap vo)
	{
		String cadena="INSERT INTO ser_incluexclu_cc(codigo,codigo_inclu_exclu_cc,codigo_servicio,incluye,cantidad,usuario_modifica,fecha_modifica,hora_modifica) values (nextval('seq_ser_incluexclu_cc'),?,?,?,?,?,?,?)";
		return SqlBaseDetalleInclusionesExclusionesDao.insertarServicios(con,vo,cadena);
	}

	/**
	 * 
	 */
	public boolean modificarServicios(Connection con, HashMap vo)
	{
		return SqlBaseDetalleInclusionesExclusionesDao.modificarServicios(con,vo);
	}

}
