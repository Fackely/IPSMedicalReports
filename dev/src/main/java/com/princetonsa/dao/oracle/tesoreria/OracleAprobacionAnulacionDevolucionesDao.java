package com.princetonsa.dao.oracle.tesoreria;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.sqlbase.tesoreria.SqlBaseAprobacionAnulacionDevolucionesDao;
import com.princetonsa.dao.tesoreria.AprobacionAnulacionDevolucionesDao;
import com.princetonsa.decorator.ResultSetDecorator;

public class OracleAprobacionAnulacionDevolucionesDao implements
		AprobacionAnulacionDevolucionesDao 
		
{
	
	
	/**
	 * 
	 */
	public HashMap BusquedaDevoluciones(Connection con, String fechaInicial, String fechaFinal, String devolInicial, String devolFinal, String motivoDevolucion, String tipoId, String numeroId, String centroAtencion, String caja, String reciboCaja) 
	{
		return SqlBaseAprobacionAnulacionDevolucionesDao.BusquedaDevoluciones(con, fechaInicial, fechaFinal, devolInicial, devolFinal, motivoDevolucion, tipoId, numeroId, centroAtencion, caja, reciboCaja);
	}
	
	/**
	 * 
	 */
	public HashMap ConsultaDetalleDevolucion(Connection con, int devolucion) 
	{
		return SqlBaseAprobacionAnulacionDevolucionesDao.ConsultaDetalleDevolucion(con, devolucion);
	}
	
	/**
	 * 
	 */
	public String ConsultaEstadoRecibo(Connection con, int recibo) 
	{
		return SqlBaseAprobacionAnulacionDevolucionesDao.ConsultaEstadoRecibo(con, recibo);
	}
	
	/**
	 * 
	 */
	public String consultaTipoConcepto(Connection con, int codigoDevol) 
	{
		return SqlBaseAprobacionAnulacionDevolucionesDao.consultaTipoConcepto(con, codigoDevol);
	}
	
	/**
	 * 
	 */
	public String consultaPagoConvenio(Connection con, int documento) 
	{
		return SqlBaseAprobacionAnulacionDevolucionesDao.consultaPagoConvenio(con, documento);
	}
	
	/**
	 * 
	 */
	public int insertarDevolucion(Connection con, int codigoDevol, String estadoDevolucion, String loginUsuario, String motivoAnulacion, double valor, long turnoAfecta) 
	{
		return SqlBaseAprobacionAnulacionDevolucionesDao.insertarDevolucion(con, codigoDevol, estadoDevolucion, loginUsuario, motivoAnulacion, valor, turnoAfecta);
	}
	
	/**
	 * 
	 */
	public HashMap consultaDetalleDevolucionConsecutivo(Connection con, int devolucion) 
	{
		return SqlBaseAprobacionAnulacionDevolucionesDao.consultaDetalleDevolucionConsecutivo(con, devolucion);
	}

	@Override
	public boolean actualizarValorAnticipo(Connection con,int numContratoOdontologia, double nuevoValorAnticipoRecConv, String usuario) {
		
		return SqlBaseAprobacionAnulacionDevolucionesDao.actualizarValorAnticipoContrato(con, numContratoOdontologia, nuevoValorAnticipoRecConv, usuario);
	}

	@Override
	public ResultSetDecorator consultarValorConceptosRecibosCaja(Connection con, String numReciboCaja, int codInstitucion) {
		
		return SqlBaseAprobacionAnulacionDevolucionesDao.consultarValorConceptosRecibosCaja(con, numReciboCaja, codInstitucion);
	}

	@Override
	public ResultSetDecorator esReciboCajaXConceptAnticipConvenioOdonto(Connection con, String numeroReciboCaja) {
		
		return SqlBaseAprobacionAnulacionDevolucionesDao.esReciboCajaXConceptAnticipConvenioOdonto(con, numeroReciboCaja);
	}

	@Override
	public ResultSetDecorator obtenerValorAnticipoDipoContratConveRecibCaja(Connection con, int numContrato) {
		
		return SqlBaseAprobacionAnulacionDevolucionesDao.obtenerValorAnticipoDipoContratConveRecibCaja(con, numContrato);
	}
	
}
