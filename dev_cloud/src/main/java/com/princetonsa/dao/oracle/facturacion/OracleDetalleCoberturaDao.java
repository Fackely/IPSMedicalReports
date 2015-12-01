/*
 * Creado May 11, 2007
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * OracleDetalleCoberturaDao
 * com.princetonsa.dao.oracle.facturacion
 * java version "1.5.0_07"
 */
package com.princetonsa.dao.oracle.facturacion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import util.InfoDatosString;

import com.princetonsa.dao.facturacion.DetalleCoberturaDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseDetalleCoberturaDao;
import com.princetonsa.dto.facturacion.DtoCoberturaProgramas;
import com.princetonsa.dto.facturacion.DtoCoberturaServicios;

/**
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * May 11, 2007
 */
public class OracleDetalleCoberturaDao implements DetalleCoberturaDao
{


	/**
	 * 
	 */
	public HashMap obtenerListadoCoberturas(Connection con, int institucion)
	{
		return SqlBaseDetalleCoberturaDao.obtenerListadoCoberturas(con,institucion);
	}

	/**
	 * 
	 */
	public boolean eliminarDetalleCobertura(Connection con, String codigoDetCobertura)
	{
		return SqlBaseDetalleCoberturaDao.eliminarDetalleCobertura(con,codigoDetCobertura);
	}
	

	public HashMap consultarDetalleCoberturaLLave(Connection con, String codigoDetallecobertura)
	{
		return SqlBaseDetalleCoberturaDao.consultarDetalleCoberturaLLave(con,codigoDetallecobertura);
	}

	/**
	 * 
	 */
	public boolean insertarDetalleCobertura(Connection con, HashMap vo)
	{
		String cadena="INSERT INTO detalle_cobertura (codigo_detalle_cob,codigo_cobertura,institucion,via_ingreso,tipo_paciente,naturaleza_paciente,usuario_modifica,fecha_modifica,hora_modifica)  values (seq_detalle_cobertura.nextval,?,?,?,?,?,?,?,?)";
		return SqlBaseDetalleCoberturaDao.insertarDetalleCobertura(con,vo,cadena);
	}

	/**
	 * 
	 */
	public boolean modificarDetalleCobertura(Connection con, HashMap vo)
	{
		return SqlBaseDetalleCoberturaDao.modificarDetalleCobertura(con,vo);
	}

	/**
	 * 
	 */
	public Vector<InfoDatosString> obtenerListadoCoberturasInstitucion(Connection con, int institucion)
	{
		return SqlBaseDetalleCoberturaDao.obtenerListadoCoberturasInstitucion(con,institucion);
	}



	/**
	 * 
	 */
	public HashMap consultarAgrupacionArticulos(Connection con, String codigo)
	{
		return SqlBaseDetalleCoberturaDao.consultarAgrupacionArticulos(con,codigo);
	}

	/**
	 * 
	 */
	public HashMap consultarAgrupacionServicios(Connection con, String codigo)
	{
		return SqlBaseDetalleCoberturaDao.consultarAgrupacionServicios(con,codigo);
	}

	/**
	 * 
	 */
	public HashMap consultarArticulos(Connection con, String codigo)
	{
		return SqlBaseDetalleCoberturaDao.consultarArticulos(con,codigo);
	}

	/**
	 * 
	 */
	public HashMap consultarServicios(Connection con, String codigo)
	{
		return SqlBaseDetalleCoberturaDao.consultarServicios(con,codigo);
	}
	

	/**
	 * 
	 */
	public boolean eliminarAgrupacionArticulos(Connection con, String codigoAgrupacion)
	{
		return SqlBaseDetalleCoberturaDao.eliminarAgrupacionArticulos(con,codigoAgrupacion);
	}
	

	/**
	 * 
	 */
	public HashMap consultarAgrupacionArticulosLLave(Connection con, String codigo)
	{
		return SqlBaseDetalleCoberturaDao.consultarAgrupacionArticulosLLave(con,codigo);
	}

	/**
	 * 
	 */
	public boolean modificarAgrupacionArticulos(Connection con, HashMap vo)
	{
		return SqlBaseDetalleCoberturaDao.modificarAgrupacionArticulos(con,vo);
	}

	/**
	 * 
	 */
	public boolean insertarAgrupacionArticulos(Connection con, HashMap vo)
	{
		String cadena="INSERT INTO cob_agrup_articulos(codigo,codigo_detalle_cob,clase,grupo,subgrupo,naturaleza,institucion,requiere_autorizacion,semanas_min_cotizacion,cantidad_max_cub_x_ingreso,usuario_modifica,fecha_modifica,hora_modifica) values(seq_cob_agrup_articulo.nextval,?,?,?,?,?,?,?,?,?,?,?,?)";
		return SqlBaseDetalleCoberturaDao.insertarAgrupacionArticulos(con,vo,cadena);
	}

	/**
	 * 
	 */
	public boolean eliminarArticulos(Connection con, String codigoComponente)
	{
		return SqlBaseDetalleCoberturaDao.eliminarArticulos(con,codigoComponente);
	}

	/**
	 * 
	 */
	public HashMap consultarArticulosLLave(Connection con, String codigo)
	{
		return SqlBaseDetalleCoberturaDao.consultarArticulosLLave(con,codigo);
	}

	/**
	 * 
	 */
	public boolean modificarArticulos(Connection con, HashMap vo)
	{
		return SqlBaseDetalleCoberturaDao.modificarArticulos(con,vo);
	}

	/**
	 * 
	 */
	public boolean insertarArticulos(Connection con, HashMap vo)
	{
		String cadena="INSERT INTO cobertura_articulos (codigo,codigo_detalle_cob,codigo_articulo,requiere_autorizacion,semanas_min_cotizacion,cantidad_max_cub_x_ingreso,usuario_modifica,fecha_modifica,hora_modifica) values (seq_cobertura_articulo.nextval,?,?,?,?,?,?,?,?)";
		return SqlBaseDetalleCoberturaDao.insertarArticulos(con,vo,cadena);
	}

	/**
	 * 
	 */
	public boolean eliminarAgrupacionServicios(Connection con, String codigoAgrupacion)
	{
		return SqlBaseDetalleCoberturaDao.eliminarAgrupacionServicios(con,codigoAgrupacion);
	}

	/**
	 * 
	 */
	public HashMap consultarAgrupacionServiciosLLave(Connection con, String codigo)
	{
		return SqlBaseDetalleCoberturaDao.consultarAgrupacionServiciosLLave(con,codigo);
	}

	/**
	 * 
	 */
	public boolean insertarAgrupacionServicios(Connection con, HashMap vo)
	{
		String cadena="INSERT INTO cob_agrup_servicios(codigo,codigo_detalle_cob,pos,possubsidiado,grupo_servicio,tipo_servicio,especialidad,requiere_autorizacion,semanas_min_cotizacion,cantidad_max_cub_x_ingreso,usuario_modifica,fecha_modifica,hora_modifica) values (seq_cob_agrup_servicios.nextval,?,?,?,?,?,?,?,?,?,?,?,?)";
		return SqlBaseDetalleCoberturaDao.insertarAgrupacionServicios(con,vo,cadena);
	}

	/**
	 * 
	 */
	public boolean modificarAgrupacionServicios(Connection con, HashMap vo)
	{
		return SqlBaseDetalleCoberturaDao.modificarAgrupacionServicios(con,vo);
	}
	

	/**
	 * 
	 */
	public boolean eliminarServicios(Connection con, String codigoComponente)
	{
		return SqlBaseDetalleCoberturaDao.eliminarServicios(con,codigoComponente);
	}

	/**
	 * 
	 */
	public HashMap consultarServiciosLLave(Connection con, String codigo)
	{
		return SqlBaseDetalleCoberturaDao.consultarServiciosLLave(con,codigo);
	}

	/**
	 * 
	 */
	public boolean insertarServicios(Connection con, HashMap vo)
	{
		String cadena="INSERT INTO cobertura_servicios(codigo,codigo_detalle_cob,codigo_servicio,requiere_autorizacion,semanas_min_cotizacion,cantidad_max_cub_x_ingreso,usuario_modifica,fecha_modifica,hora_modifica) values (seq_cobertura_servicios.nextval,?,?,?,?,?,?,?,?)";
		return SqlBaseDetalleCoberturaDao.insertarServicios(con,vo,cadena);
	}

	/**
	 * 
	 */
	public boolean modificarServicios(Connection con, HashMap vo)
	{
		return SqlBaseDetalleCoberturaDao.modificarServicios(con,vo);
	}
	
	/**
	 * 
	 */
	public boolean insertarCoberturaProgramas(Connection con, DtoCoberturaProgramas dto)
	{
		return SqlBaseDetalleCoberturaDao.insertarCoberturaProgramas(con,dto);
	}
	
	/**
	 * 
	 */
	public ArrayList<DtoCoberturaProgramas> consultarCoberturaProgramas(Connection con, DtoCoberturaProgramas dto)
	{
		return SqlBaseDetalleCoberturaDao.consultarCoberturaProgramas(con, dto);
	}
	
	/**
	 * 
	 */
	public boolean eliminarCoberturaProgramas(Connection con, double codigoCoberturaAEliminar)
	{
		return SqlBaseDetalleCoberturaDao.eliminarCoberturaProgramas(con, codigoCoberturaAEliminar);
	}
	
	/**
	 * 
	 */
	public ArrayList<DtoCoberturaServicios> consultarCoberturaServicios(Connection con, DtoCoberturaServicios dto)
	{
		return SqlBaseDetalleCoberturaDao.consultarCoberturaServicios(con, dto);
	}
	
	/**
	 * 
	 */
	public boolean insertarCoberturaServicios(Connection con, DtoCoberturaServicios dto)
	{
		return SqlBaseDetalleCoberturaDao.insertarCoberturaServicios(con, dto);
	}
	
	/**
	 * 
	 */
	public boolean eliminarCoberturaServicios(Connection con, double codigoCoberturaAEliminar)
	{
		return SqlBaseDetalleCoberturaDao.eliminarCoberturaServicios(con, codigoCoberturaAEliminar);
	}
}
