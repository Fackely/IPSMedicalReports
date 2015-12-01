package com.princetonsa.dao.postgresql.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.facturacion.ConsultaTarifasDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseConsultaTarifasDao;
import com.servinte.axioma.fwk.exception.IPSException;

public class PostgresqlConsultaTarifasDao implements ConsultaTarifasDao 
{
	
	/**
	 * 
	 */
	public HashMap buscar(Connection con, String codigo,String descripcion, String especialidad, String tipoServicio, String grupo,int codigoEsquemaTarifario) throws IPSException 
	{
		
		return SqlBaseConsultaTarifasDao.buscar(con,codigo,descripcion,especialidad,tipoServicio,grupo,codigoEsquemaTarifario);
		
	}
	
	/**
	 * 
	 */
	public HashMap buscarArticulo(Connection con, String codigoArticulo, String descripcionArticulo, String clase, String grupoArticulo, String subgrupo, String naturaleza,int esquemaTarifario) throws IPSException 
	{
		
		return SqlBaseConsultaTarifasDao.buscarArticulo(con,codigoArticulo,descripcionArticulo,clase,grupoArticulo,subgrupo,naturaleza,esquemaTarifario);
		
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public HashMap consultaArticulo(Connection con, HashMap vo)
	{
		
		return SqlBaseConsultaTarifasDao.consultaArticulo(con,vo);
		
	}
	
	/**
	 * 
	 */
	public HashMap consultaServicio(Connection con, HashMap vo) 
	{
		
		return SqlBaseConsultaTarifasDao.consultaServicio(con,vo);
		
	}

	
	/**
	 * 
	 */
	public HashMap<String,Object> consultarEsquemasVigenciasContrato(Connection con, int contrato, boolean esServicio) 
	{
		return SqlBaseConsultaTarifasDao.consultarEsquemasVigenciasContrato(con,contrato,esServicio);
	}

	/**
	 * 
	 */
	public HashMap consultaServicioTarifaFinal(Connection con, int codigoServicio, int tarifarioOficial,int esquemaTarifario,int convenio,int contrato,int institucion,String fechaVigencia, int grupoServicio) throws IPSException 
	{
		return SqlBaseConsultaTarifasDao.consultaServicioTarifaFinal(con,codigoServicio,tarifarioOficial,esquemaTarifario,convenio,contrato,institucion,fechaVigencia, grupoServicio);
	}

	/**
	 * 
	 */
	public HashMap consultaArticulosTarifaFinal(Connection con, int codigoArti, int esquemaTarifario, int convenio, int contrato, int institucion, String fechaVigencia,int claseInventario) throws IPSException
	{
		return SqlBaseConsultaTarifasDao.consultaArticulosTarifaFinal(con,codigoArti,esquemaTarifario,convenio,contrato,institucion,fechaVigencia,claseInventario);
	}

	@Override
	public String obtenerConsulta(HashMap criterios) {
		
		return null;
	}
	

}
