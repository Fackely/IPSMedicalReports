/*
 * Creado May 23, 2007
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * OracleInclusionExclusionConvenioDao
 * com.princetonsa.dao.oracle.facturacion
 * java version "1.5.0_07"
 */
package com.princetonsa.dao.oracle.facturacion;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

import util.InfoDatosString;

import com.princetonsa.dao.facturacion.InclusionExclusionConvenioDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseInclusionExclusionConvenioDao;

/**
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * May 23, 2007
 */
public class OracleInclusionExclusionConvenioDao implements InclusionExclusionConvenioDao
{

	/**
	 * 
	 */
	public Vector<InfoDatosString> obtenerDetalleInclusionesExclusiones(Connection con, int institucion)
	{
		return SqlBaseInclusionExclusionConvenioDao.obtenerDetalleInclusionesExclusiones(con,institucion);
	}

	/**
	 * 
	 */
	public HashMap obtenerExcepciones(Connection con, int institucion, int contrato,boolean vigentes,boolean todas)
	{
		return SqlBaseInclusionExclusionConvenioDao.obtenerExcepciones(con,institucion, contrato,vigentes,todas);
	}

	/**
	 * 
	 */
	public HashMap obtenerInclusionesExclusiones(Connection con, int institucion, int contrato)
	{
		return SqlBaseInclusionExclusionConvenioDao.obtenerInclusionesExclusiones(con,institucion,contrato);
	}
	

	/**
	 * 
	 */
	public boolean eliminareExepciones(Connection con, String codigo)
	{
		return SqlBaseInclusionExclusionConvenioDao.eliminareExepciones(con, codigo);
	}

	/**
	 * 
	 */
	public HashMap consultarExcepcionLLave(Connection con, String codigo)
	{
		return SqlBaseInclusionExclusionConvenioDao.consultaExcepcionLLave(con, codigo);
	}

	/**
	 * 
	 */
	public boolean insertarExcepcion(Connection con, HashMap vo)
	{
		String cadena="INSERT INTO excep_incluexclu_contr(codigo,codigo_contrato,institucion,codigo_centro_costo,usuario_modifica,fecha_modifica,hora_modifica,fecha_vigencia) values(seq_excep_incluexclu_contr.nextval,?,?,?,?,?,?,?)";
		return SqlBaseInclusionExclusionConvenioDao.insertarExcepcion(con, vo, cadena);
	}

	/**
	 * 
	 */
	public boolean modificarExcepcion(Connection con, HashMap vo)
	{
		return SqlBaseInclusionExclusionConvenioDao.modificarExcepcion(con, vo);
	}
	
	/**
	 * 
	 */
	public boolean eliminarInclusionExclusion(Connection con, String codigoIncluExclu, int contrato, int institucion)
	{
		return SqlBaseInclusionExclusionConvenioDao.eliminarInclusionExclusion(con,codigoIncluExclu,contrato,institucion);
	}

	/**
	 * 
	 */
	public boolean insertarInclusionExclusion(Connection con, HashMap vo)
	{
		return SqlBaseInclusionExclusionConvenioDao.insertarInclusionExclusion(con, vo);
	}
	


	/**
	 * 
	 */
	public HashMap consultarAgrupacionArticulos(Connection con, String codigo)
	{
		return SqlBaseInclusionExclusionConvenioDao.consultarAgrupacionArticulos(con,codigo);
	}

	/**
	 * 
	 */
	public HashMap consultarAgrupacionServicios(Connection con, String codigo)
	{
		return SqlBaseInclusionExclusionConvenioDao.consultarAgrupacionServicios(con,codigo);
	}

	/**
	 * 
	 */
	public HashMap consultarArticulos(Connection con, String codigo)
	{
		return SqlBaseInclusionExclusionConvenioDao.consultarArticulos(con,codigo);
	}

	/**
	 * 
	 */
	public HashMap consultarServicios(Connection con, String codigo)
	{
		return SqlBaseInclusionExclusionConvenioDao.consultarServicios(con,codigo);
	}
	
	/**
	 * 
	 */
	public HashMap consultarAgrupacionArticulosLLave(Connection con, String codigo)
	{
		return SqlBaseInclusionExclusionConvenioDao.consultarAgrupacionArticulosLLave(con,codigo);
	}

	/**
	 * 
	 */
	public HashMap consultarArticulosLLave(Connection con, String codigo)
	{
		return SqlBaseInclusionExclusionConvenioDao.consultarArticulosLLave(con,codigo);
	}

	
	/**
	 * 
	 */
	public HashMap consultarAgrupacionServiciosLLave(Connection con, String codigo)
	{
		return SqlBaseInclusionExclusionConvenioDao.consultarAgrupacionServiciosLLave(con,codigo);
	}

	
	/**
	 * 
	 */
	public HashMap consultarServiciosLLave(Connection con, String codigo)
	{
		return SqlBaseInclusionExclusionConvenioDao.consultarServiciosLLave(con,codigo);
	}
	

	/**
	 * 
	 */
	public boolean eliminarAgrupacionArticulos(Connection con, String codigoAgrupacion)
	{
		return SqlBaseInclusionExclusionConvenioDao.eliminarAgrupacionArticulos(con,codigoAgrupacion);
	}
	

	/**
	 * 
	 */
	public boolean modificarAgrupacionArticulos(Connection con, HashMap vo)
	{
		return SqlBaseInclusionExclusionConvenioDao.modificarAgrupacionArticulos(con,vo);
	}

	/**
	 * 
	 */
	public boolean insertarAgrupacionArticulos(Connection con, HashMap vo)
	{
		String cadena="INSERT INTO agru_art_incluexclu_econt(codigo,codigo_excepcion,clase,grupo,subgrupo,naturaleza,institucion,incluye,cantidad,usuario_modifica,fecha_modifica,hora_modifica,fecha_vigencia) values(seq_agru_art_incluexclu_econt.nextval,?,?,?,?,?,?,?,?,?,?,?,?)";
		return SqlBaseInclusionExclusionConvenioDao.insertarAgrupacionArticulos(con,vo,cadena);
	}

	/**
	 * 
	 */
	public boolean eliminarArticulos(Connection con, String codigoComponente)
	{
		return SqlBaseInclusionExclusionConvenioDao.eliminarArticulos(con,codigoComponente);
	}

	/**
	 * 
	 */
	public boolean modificarArticulos(Connection con, HashMap vo)
	{
		return SqlBaseInclusionExclusionConvenioDao.modificarArticulos(con,vo);
	}

	/**
	 * 
	 */
	public boolean insertarArticulos(Connection con, HashMap vo)
	{
		String cadena="INSERT INTO art_incluexclu_econt (codigo,codigo_excepcion,codigo_articulo,incluye,cantidad,usuario_modifica,fecha_modifica,hora_modifica,fecha_vigencia) values (seq_art_incluexclu_econt.nextval,?,?,?,?,?,?,?,?)";
		return SqlBaseInclusionExclusionConvenioDao.insertarArticulos(con,vo,cadena);
	}

	/**
	 * 
	 */
	public boolean eliminarAgrupacionServicios(Connection con, String codigoAgrupacion)
	{
		return SqlBaseInclusionExclusionConvenioDao.eliminarAgrupacionServicios(con,codigoAgrupacion);
	}

	/**
	 * 
	 */
	public boolean insertarAgrupacionServicios(Connection con, HashMap vo)
	{
		String cadena="INSERT INTO agru_ser_incluexclu_econt(codigo,codigo_excepcion,pos,grupo_servicio,tipo_servicio,especialidad,incluye,cantidad,usuario_modifica,fecha_modifica,hora_modifica,fecha_vigencia) values (seq_agru_ser_incluexclu_econt.nextval,?,?,?,?,?,?,?,?,?,?,?)";
		return SqlBaseInclusionExclusionConvenioDao.insertarAgrupacionServicios(con,vo,cadena);
	}

	/**
	 * 
	 */
	public boolean modificarAgrupacionServicios(Connection con, HashMap vo)
	{
		return SqlBaseInclusionExclusionConvenioDao.modificarAgrupacionServicios(con,vo);
	}
	

	/**
	 * 
	 */
	public boolean eliminarServicios(Connection con, String codigoComponente)
	{
		return SqlBaseInclusionExclusionConvenioDao.eliminarServicios(con,codigoComponente);
	}

	
	/**
	 * 
	 */
	public boolean insertarServicios(Connection con, HashMap vo)
	{
		String cadena="INSERT INTO ser_incluexclu_econt(codigo,codigo_excepcion,codigo_servicio,incluye,cantidad,usuario_modifica,fecha_modifica,hora_modifica,fecha_vigencia) values (seq_ser_incluexclu_econt.nextval,?,?,?,?,?,?,?,?)";
		return SqlBaseInclusionExclusionConvenioDao.insertarServicios(con,vo,cadena);
	}

	/**
	 * 
	 */
	public boolean modificarServicios(Connection con, HashMap vo)
	{
		return SqlBaseInclusionExclusionConvenioDao.modificarServicios(con,vo);
	}

	/**
	 * 
	 */
	public HashMap consultarIncluExcluLLave(Connection con, String codigoIncluExclu, int contrato, int institucion) 
	{
		return SqlBaseInclusionExclusionConvenioDao.consultarIncluExcluLLave(con,codigoIncluExclu,contrato,institucion);
	}

	/**
	 * 
	 */
	public boolean modificarIncluExclu(Connection con, HashMap vo) 
	{
		return SqlBaseInclusionExclusionConvenioDao.modificarIncluExclu(con,vo);
	}


}
