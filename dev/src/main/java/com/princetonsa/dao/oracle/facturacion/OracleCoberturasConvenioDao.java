/*
 * Creado May 17, 2007
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * OracleCoberturasConvenioDao
 * com.princetonsa.dao.oracle.facturacion
 * java version "1.5.0_07"
 */
package com.princetonsa.dao.oracle.facturacion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.facturacion.CoberturasConvenioDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseCoberturasConvenioDao;
import com.princetonsa.dto.facturacion.DtoProExeCobConvXCont;
/**
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * May 17, 2007
 */
public class OracleCoberturasConvenioDao implements CoberturasConvenioDao
{

	/**
	 * 
	 */
	public HashMap obtenerListadoCoberturas(Connection con, int institucion, int contrato)
	{
		return SqlBaseCoberturasConvenioDao.obtenerListadoCoberturas(con,institucion,contrato);
	}

	/**
	 * 
	 */
	public HashMap obtenerListadoExcepciones(Connection con, int institucion, int contrato)
	{
		return SqlBaseCoberturasConvenioDao.obtenerListadoExcepciones(con,institucion,contrato);
	}

	/**
	 * 
	 */
	public boolean eliminareExepciones(Connection con, String codigo)
	{
		return SqlBaseCoberturasConvenioDao.eliminareExepciones(con, codigo);
	}

	/**
	 * 
	 */
	public HashMap consultarExcepcionLLave(Connection con, String codigo)
	{
		return SqlBaseCoberturasConvenioDao.consultaExcepcionLLave(con, codigo);
	}

	/**
	 * 
	 */
	public boolean insertarExcepcion(Connection con, HashMap vo)
	{
		String cadena="INSERT INTO exep_para_cob_x_convcont(codigo,codigo_contrato,institucion,via_ingreso,tipo_paciente,naturaleza_paciente,usuario_modifica,fecha_modifica,hora_modifica) values(seq_exep_para_cob_x_convcont.nextval,?,?,?,?,?,?,?,?)";
		return SqlBaseCoberturasConvenioDao.insertarExcepcion(con, vo, cadena);
	}

	public boolean modificarExcepcion(Connection con, HashMap vo)
	{
		return SqlBaseCoberturasConvenioDao.modificarExcepcion(con, vo);
	}
	
	/**
	 * 
	 */
	public boolean eliminarCobertura(Connection con, String codigo, int contrato, int institucion)
	{
		return SqlBaseCoberturasConvenioDao.eliminarCobertura(con, codigo, contrato, institucion);
	}
	
	/**
	 * 
	 */
	public boolean insertarCobertura(Connection con, HashMap vo)
	{
		String cadena="INSERT INTO coberturas_x_contrato(codigo_contrato,codigo_cobertura,prioridad,institucion,usuario_modifica,fecha_modifica,hora_modifica) values(?,?,?,?,?,?,?)";
		return SqlBaseCoberturasConvenioDao.insertarCobertura(con, vo, cadena);
	}
	
	/**
	 * 
	 */
	public HashMap consultarAgrupacionArticulos(Connection con, String codigo)
	{
		return SqlBaseCoberturasConvenioDao.consultarAgrupacionArticulos(con,codigo);
	}

	/**
	 * 
	 */
	public HashMap consultarAgrupacionServicios(Connection con, String codigo)
	{
		return SqlBaseCoberturasConvenioDao.consultarAgrupacionServicios(con,codigo);
	}

	/**
	 * 
	 */
	public HashMap consultarArticulos(Connection con, String codigo)
	{
		return SqlBaseCoberturasConvenioDao.consultarArticulos(con,codigo);
	}

	/**
	 * 
	 */
	public HashMap consultarServicios(Connection con, String codigo)
	{
		return SqlBaseCoberturasConvenioDao.consultarServicios(con,codigo);
	}
	
	/**
	 * 
	 */
	public HashMap consultarAgrupacionArticulosLLave(Connection con, String codigo)
	{
		return SqlBaseCoberturasConvenioDao.consultarAgrupacionArticulosLLave(con,codigo);
	}

	/**
	 * 
	 */
	public HashMap consultarArticulosLLave(Connection con, String codigo)
	{
		return SqlBaseCoberturasConvenioDao.consultarArticulosLLave(con,codigo);
	}

	
	/**
	 * 
	 */
	public HashMap consultarAgrupacionServiciosLLave(Connection con, String codigo)
	{
		return SqlBaseCoberturasConvenioDao.consultarAgrupacionServiciosLLave(con,codigo);
	}

	
	/**
	 * 
	 */
	public HashMap consultarServiciosLLave(Connection con, String codigo)
	{
		return SqlBaseCoberturasConvenioDao.consultarServiciosLLave(con,codigo);
	}


	/**
	 * 
	 */
	public boolean eliminarAgrupacionArticulos(Connection con, String codigoAgrupacion)
	{
		return SqlBaseCoberturasConvenioDao.eliminarAgrupacionArticulos(con,codigoAgrupacion);
	}
	

	/**
	 * 
	 */
	public boolean modificarAgrupacionArticulos(Connection con, HashMap vo)
	{
		return SqlBaseCoberturasConvenioDao.modificarAgrupacionArticulos(con,vo);
	}

	/**
	 * 
	 */
	public boolean insertarAgrupacionArticulos(Connection con, HashMap vo)
	{
		String cadena="INSERT INTO agrup_art_exep_cob_convxcont(codigo,codigo_excepcion,clase,grupo,subgrupo,naturaleza,institucion,requiere_autorizacion,semanas_min_cotizacion,cantidad_max_cub_x_ingreso,presentar_factura_compra,incluido,usuario_modifica,fecha_modifica,hora_modifica) values(seq_agru_art_exp_cob_convxcont.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		return SqlBaseCoberturasConvenioDao.insertarAgrupacionArticulos(con,vo,cadena);
	}

	/**
	 * 
	 */
	public boolean eliminarArticulos(Connection con, String codigoComponente)
	{
		return SqlBaseCoberturasConvenioDao.eliminarArticulos(con,codigoComponente);
	}

	/**
	 * 
	 */
	public boolean modificarArticulos(Connection con, HashMap vo)
	{
		return SqlBaseCoberturasConvenioDao.modificarArticulos(con,vo);
	}

	/**
	 * 
	 */
	public boolean insertarArticulos(Connection con, HashMap vo)
	{
		String cadena="INSERT INTO art_exp_cob_convxcont (codigo,codigo_excepcion,codigo_articulo,requiere_autorizacion,semanas_min_cotizacion,cantidad_max_cub_x_ingreso,presentar_factura_compra,incluido,usuario_modifica,fecha_modifica,hora_modifica) values (seq_art_exp_cob_convxcont.nextval,?,?,?,?,?,?,?,?,?,?)";
		return SqlBaseCoberturasConvenioDao.insertarArticulos(con,vo,cadena);
	}

	/**
	 * 
	 */
	public boolean eliminarAgrupacionServicios(Connection con, String codigoAgrupacion)
	{
		return SqlBaseCoberturasConvenioDao.eliminarAgrupacionServicios(con,codigoAgrupacion);
	}

	/**
	 * 
	 */
	public boolean insertarAgrupacionServicios(Connection con, HashMap vo)
	{
		String cadena="INSERT INTO agru_ser_exep_cob_convxcont(codigo,codigo_excepcion,pos,possubsidiado,grupo_servicio,tipo_servicio,especialidad,requiere_autorizacion,semanas_min_cotizacion,cantidad_max_cub_x_ingreso,incluido,usuario_modifica,fecha_modifica,hora_modifica) values (seq_agru_ser_exp_cob_convxcont.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		return SqlBaseCoberturasConvenioDao.insertarAgrupacionServicios(con,vo,cadena);
	}

	/**
	 * 
	 */
	public boolean modificarAgrupacionServicios(Connection con, HashMap vo)
	{
		return SqlBaseCoberturasConvenioDao.modificarAgrupacionServicios(con,vo);
	}
	

	/**
	 * 
	 */
	public boolean eliminarServicios(Connection con, String codigoComponente)
	{
		return SqlBaseCoberturasConvenioDao.eliminarServicios(con,codigoComponente);
	}

	
	/**
	 * 
	 */
	public boolean insertarServicios(Connection con, HashMap vo)
	{
		String cadena="INSERT INTO serv_exe_cob_convxcont(codigo,codigo_excepcion,codigo_servicio,requiere_autorizacion,semanas_min_cotizacion,cantidad_max_cub_x_ingreso,incluido,usuario_modifica,fecha_modifica,hora_modifica) values (seq_serv_exe_cob_convxcont.nextval,?,?,?,?,?,?,?,?,?)";
		return SqlBaseCoberturasConvenioDao.insertarServicios(con,vo,cadena);
	}

	/**
	 * 
	 */
	public boolean modificarServicios(Connection con, HashMap vo)
	{
		return SqlBaseCoberturasConvenioDao.modificarServicios(con,vo);
	}
	

	/**
	 * 
	 */
	public HashMap consultarCoberturaLLave(Connection con, String codigoCobertura, int contrato, int institucion) 
	{
		return SqlBaseCoberturasConvenioDao.consultarCoberturaLLave(con,codigoCobertura,contrato,institucion);
	}

	/**
	 * 
	 */
	public boolean modificarCobertura(Connection con, HashMap vo) 
	{
		return SqlBaseCoberturasConvenioDao.modificarCobertura(con,vo);
	}


	/**
	 * 
	 */
	public ArrayList obtenerConvenios(Connection con) 
	{
		return SqlBaseCoberturasConvenioDao.obtenerConvenios(con);
	}
	
	/**
	 * 
	 */
	public boolean insertarProgExcepcionConvenio(Connection con, DtoProExeCobConvXCont dto)
	{
		return SqlBaseCoberturasConvenioDao.insertarProgExcepcionConvenio(con, dto);
	}
	
	/**
	 * 
	 */
	public ArrayList<DtoProExeCobConvXCont> consultaProgExcepcionConvenio(Connection con, DtoProExeCobConvXCont dto)
	{
		return SqlBaseCoberturasConvenioDao.consultaProgExcepcionConvenio(con, dto);
	}
	
	/**
	 * 
	 */
	public boolean eliminarProgExcepcionConvenio(Connection con, double excepcion)
	{
		return SqlBaseCoberturasConvenioDao.eliminarProgExcepcionConvenio(con, excepcion);
	}

}
