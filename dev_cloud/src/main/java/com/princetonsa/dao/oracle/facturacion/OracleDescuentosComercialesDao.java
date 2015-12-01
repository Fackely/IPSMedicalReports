/*
 * Creado May 22, 2007
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * OracleDescuentosComercialesDao
 * com.princetonsa.dao.oracle.facturacion
 * java version "1.5.0_07"
 */
package com.princetonsa.dao.oracle.facturacion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.facturacion.DescuentosComercialesDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseDescuentosComercialesDao;
import com.princetonsa.dto.facturacion.DtoConvenio;
import com.princetonsa.dto.facturacion.DtoProgDescComercialConvenioContrato;

/**
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * May 22, 2007
 */
public class OracleDescuentosComercialesDao implements DescuentosComercialesDao
{

	/**
	 * 
	 */
	public HashMap obtenerListadoViasDescuento(Connection con, int institucion, int contrato, boolean vigentes, boolean todas)
	{
		return SqlBaseDescuentosComercialesDao.obtenerListadoViasDescuento(con,institucion,contrato,vigentes,todas);
	}

	/**
	 * 
	 */
	public HashMap consultarViaDescuentoLLave(Connection con, String codigo)
	{
		return SqlBaseDescuentosComercialesDao.consultarViaDescuentoLLave(con,codigo);
	}

	/**
	 * 
	 */
	public boolean eliminarViaDescuento(Connection con, String codigo)
	{
		return SqlBaseDescuentosComercialesDao.eliminarViaDescuento(con,codigo);
	}

	/**
	 * 
	 */
	public boolean insertarViaDescuento(Connection con, HashMap vo)
	{
		String cadena="INSERT INTO desc_com_convcont (codigo,codigo_contrato,institucion,via_ingreso,usuario_modifica,fecha_modifica,hora_modifica,tipo_paciente,fecha_vigencia) values(seq_desc_com_convcont.nextval,?,?,?,?,?,?,?,?)";
		return SqlBaseDescuentosComercialesDao.insertarViaDescuento(con,vo,cadena);
	}

	/**
	 * 
	 */
	public boolean modificarViaDescuento(Connection con, HashMap vo)
	{
		return SqlBaseDescuentosComercialesDao.modificarViaDescuento(con,vo);
	}
	
	/**
	 * 
	 */
	public HashMap consultarAgrupacionArticulos(Connection con, String codigo)
	{
		return SqlBaseDescuentosComercialesDao.consultarAgrupacionArticulos(con,codigo);
	}

	/**
	 * 
	 */
	public HashMap consultarAgrupacionServicios(Connection con, String codigo)
	{
		return SqlBaseDescuentosComercialesDao.consultarAgrupacionServicios(con,codigo);
	}

	/**
	 * 
	 */
	public HashMap consultarArticulos(Connection con, String codigo)
	{
		return SqlBaseDescuentosComercialesDao.consultarArticulos(con,codigo);
	}

	/**
	 * 
	 */
	public HashMap consultarServicios(Connection con, String codigo)
	{
		return SqlBaseDescuentosComercialesDao.consultarServicios(con,codigo);
	}
	
	/**
	 * 
	 */
	public HashMap consultarAgrupacionArticulosLLave(Connection con, String codigo)
	{
		return SqlBaseDescuentosComercialesDao.consultarAgrupacionArticulosLLave(con,codigo);
	}

	/**
	 * 
	 */
	public HashMap consultarArticulosLLave(Connection con, String codigo)
	{
		return SqlBaseDescuentosComercialesDao.consultarArticulosLLave(con,codigo);
	}

	
	/**
	 * 
	 */
	public HashMap consultarAgrupacionServiciosLLave(Connection con, String codigo)
	{
		return SqlBaseDescuentosComercialesDao.consultarAgrupacionServiciosLLave(con,codigo);
	}

	
	/**
	 * 
	 */
	public HashMap consultarServiciosLLave(Connection con, String codigo)
	{
		return SqlBaseDescuentosComercialesDao.consultarServiciosLLave(con,codigo);
	}
	

	/**
	 * 
	 */
	public boolean eliminarAgrupacionArticulos(Connection con, String codigoAgrupacion)
	{
		return SqlBaseDescuentosComercialesDao.eliminarAgrupacionArticulos(con,codigoAgrupacion);
	}
	

	/**
	 * 
	 */
	public boolean modificarAgrupacionArticulos(Connection con, HashMap vo)
	{
		return SqlBaseDescuentosComercialesDao.modificarAgrupacionArticulos(con,vo);
	}

	/**
	 * 
	 */
	public boolean insertarAgrupacionArticulos(Connection con, HashMap vo)
	{
		String cadena="INSERT INTO agrup_art_desc_com_convcont(codigo,codigo_descuento,clase,grupo,subgrupo,naturaleza,institucion,porcentaje,valor,usuario_modifica,fecha_modifica,hora_modifica,fecha_vigencia) values(seq_desc_com_convcont.nextval,?,?,?,?,?,?,?,?,?,?,?,?)";
		return SqlBaseDescuentosComercialesDao.insertarAgrupacionArticulos(con,vo,cadena);
	}

	/**
	 * 
	 */
	public boolean eliminarArticulos(Connection con, String codigoComponente)
	{
		return SqlBaseDescuentosComercialesDao.eliminarArticulos(con,codigoComponente);
	}

	/**
	 * 
	 */
	public boolean modificarArticulos(Connection con, HashMap vo)
	{
		return SqlBaseDescuentosComercialesDao.modificarArticulos(con,vo);
	}

	/**
	 * 
	 */
	public boolean insertarArticulos(Connection con, HashMap vo)
	{
		String cadena="INSERT INTO art_desc_com_convxcont (codigo,codigo_descuento,codigo_articulo,porcentaje,valor,usuario_modifica,fecha_modifica,hora_modifica,fecha_vigencia) values (seq_art_desc_com_convxcont.nextval,?,?,?,?,?,?,?,?)";
		return SqlBaseDescuentosComercialesDao.insertarArticulos(con,vo,cadena);
	}

	/**
	 * 
	 */
	public boolean eliminarAgrupacionServicios(Connection con, String codigoAgrupacion)
	{
		return SqlBaseDescuentosComercialesDao.eliminarAgrupacionServicios(con,codigoAgrupacion);
	}

	/**
	 * 
	 */
	public boolean insertarAgrupacionServicios(Connection con, HashMap vo)
	{
		String cadena="INSERT INTO agru_ser_desc_com_convxcont(codigo,codigo_descuento,pos,grupo_servicio,tipo_servicio,especialidad,porcentaje,valor,usuario_modifica,fecha_modifica,hora_modifica,fecha_vigencia) values (seq_agru_desc_com_convxcont.nextval,?,?,?,?,?,?,?,?,?,?,?)";
		return SqlBaseDescuentosComercialesDao.insertarAgrupacionServicios(con,vo,cadena);
	}

	/**
	 * 
	 */
	public boolean modificarAgrupacionServicios(Connection con, HashMap vo)
	{
		return SqlBaseDescuentosComercialesDao.modificarAgrupacionServicios(con,vo);
	}
	

	/**
	 * 
	 */
	public boolean eliminarServicios(Connection con, String codigoComponente)
	{
		return SqlBaseDescuentosComercialesDao.eliminarServicios(con,codigoComponente);
	}

	
	/**
	 * 
	 */
	public boolean insertarServicios(Connection con, HashMap vo)
	{
		String cadena="INSERT INTO serv_desc_com_convxcont(codigo,codigo_descuento,codigo_servicio,porcentaje,valor,usuario_modifica,fecha_modifica,hora_modifica,fecha_vigencia) values (seq_serv_desc_com_convxcont.nextval,?,?,?,?,?,?,?,?)";
		return SqlBaseDescuentosComercialesDao.insertarServicios(con,vo,cadena);
	}

	/**
	 * 
	 */
	public boolean modificarServicios(Connection con, HashMap vo)
	{
		return SqlBaseDescuentosComercialesDao.modificarServicios(con,vo);
	}

	
	@Override
	public ArrayList<DtoProgDescComercialConvenioContrato> cargarProgDescComercialContrato(	DtoProgDescComercialConvenioContrato dto) {
		
		return SqlBaseDescuentosComercialesDao.cargarProgDescComercialContrato(dto);
	}

	@Override
	public boolean eliminarProgDescComercialContrato(Connection con ,DtoProgDescComercialConvenioContrato dto) {
		
		return SqlBaseDescuentosComercialesDao.eliminarProgDescComercialContrato(con, dto);
	}

	@Override
	public boolean insertProgDescComercialConvenioContrato(Connection con, DtoProgDescComercialConvenioContrato dto) {
		
		return SqlBaseDescuentosComercialesDao.insertProgDescComercialConvenioContrato(con, dto);
	}

	@Override
	public boolean modificarProgDescComercialConvenioContrato(Connection con, DtoProgDescComercialConvenioContrato dto) {
		
		return SqlBaseDescuentosComercialesDao.modificarProgDescComercialConvenioContrato(con, dto);
	}

	@Override
	public int validarTipoAtencionOdontologica(
			DtoConvenio dto) {
		
		return SqlBaseDescuentosComercialesDao.validarTipoAtencionOdontologica(dto);
	}

}
