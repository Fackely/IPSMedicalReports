package com.princetonsa.dao.oracle.agendaProcedimiento;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.agendaProcedimiento.UnidadProcedimientoDao;
import com.princetonsa.dao.sqlbase.agendaProcedimiento.SqlBaseUnidadProcedimiento;
import com.princetonsa.mundo.agendaProcedimiento.UnidadProcedimiento;

/**
 * @author Jose Eduardo Arias Doncel
 * jearias@princetonsa.com   
 * */

public class OracleUnidadProcedimientoDao implements UnidadProcedimientoDao
{
	//-- Metodos
	/**
	 * Consulta basica de Unidad de Procedimiento 
	 * @param Connection con 
	 * @param int codigo unidad de procedimiento 
	 * @param int institucion 
	 * */
	public HashMap consultarUnidadProcBasica(Connection con, int codigoUnidadProc, int institucion)
	{
		return SqlBaseUnidadProcedimiento.consultarUnidadProcBasica(con, codigoUnidadProc, institucion);
	}
	
	
	/**
	 * Consulta basica de Servicios por Unidad Procedimiento
	 * @param Connection con
	 * @param int codigoServiUndProce
	 * @param int codigoUndProce
	 * */
	public HashMap consultarServicioUnidadProcBasica(Connection con, int codigoServiUndProce, int codigoUndProce)
	{
		return SqlBaseUnidadProcedimiento.consultarServicioUnidadProcBasica(con, codigoServiUndProce, codigoUndProce);		
	}
		
	/**
	 * Consulta basica de Detalle de Servicios por Unidad Procedimiento
	 * @param Connection con
	 * @param int codigoServiDetalle
	 * @param int codigoServiUndProce
	 * */	
	public HashMap consultarServicioDetUnidadProcBasica(Connection con, int codigoServiDetalle, int codigoServiUndProce)
	{
		return SqlBaseUnidadProcedimiento.consultarServicioDetUnidadProcBasica(con, codigoServiDetalle, codigoServiUndProce);
	}
	
	/**
	 * Consulta basica de Detalle de Servicios por -> servicio, condicion de toma de examne, articulo 
	 * @param Connection con
	 * @param int codigoServiDetalle
	 * @param int codigoServiUndProce
	 * @param String tipo de consulta
	 * */	
	public HashMap consultarServicioDetalles(Connection con, int codigoServiDetalle, int codigoServiUndProce, String tipo)
	{
		return SqlBaseUnidadProcedimiento.consultarServicioDetalles(con, codigoServiDetalle, codigoServiUndProce, tipo);
	}
	
	
	/**
	 * Inserta un registro en Unidad Procedimiento
	 * */
	public boolean insertarUnidadProcedimiento(Connection con, UnidadProcedimiento unidadProcedimiento)
	{
		unidadProcedimiento.setCadenaSecuenciaUndProceStr("SELECT seq_unidad_procedimiento.NEXTVAL");
		return SqlBaseUnidadProcedimiento.insertarUnidadProcedimiento(con, unidadProcedimiento);		
	}
	
	/**
	 * Inserta un registro en Servicios por Unidad Procedimiento 
	 * @param Connection con
	 * @param UnidadProcedimiento unidadProcedimiento
	 * */
	public boolean insertarServiciosUnidadProcedimiento(Connection con, UnidadProcedimiento unidadProcedimiento)
	{
		unidadProcedimiento.setCadenaSecuenciaServicioUndProceStr("SELECT seq_servicios_undproce.NEXTVAL");
		return SqlBaseUnidadProcedimiento.insertarServiciosUnidadProcedimiento(con, unidadProcedimiento);	
	}
	
	/**
	 * Insertar un registro en Detalle de Servicios por Unidad Procedimiento
	 * @param Connection con
	 * @param UnidadProcedimiento unidadProcedimiento 
	 * */
	public boolean insertarServiciosDetUnidadProcedimiento(Connection con, UnidadProcedimiento unidadProcedimiento)
	{
		unidadProcedimiento.setCadenaSecuenciaDetServicioUndProceStr("SELECT seq_servi_det_undproce.NEXTVAL");
		return SqlBaseUnidadProcedimiento.insertarServiciosDetUnidadProcedimiento(con, unidadProcedimiento);		
	}
	
	
	/**
	 * Modifica un registro de Unidad de Procedimiento
	 * @param Connecition con
	 * @param UnidadProcedimiento unidadProcedimiento
	 * */
	public boolean modificarUnidadProcedimiento(Connection con, UnidadProcedimiento unidadProcedimiento)
	{
		return SqlBaseUnidadProcedimiento.modificarUnidadProcedimiento(con, unidadProcedimiento);
	}
	
	/**
	 * Modifica un registro de Servicios por Unidad de Procedimiento
	 * @param Connection con
	 * @param UnidadProcedimiento unidadProcedimiento
	 * */
	public boolean modificarServiciosUnidadProcedimiento(Connection con, UnidadProcedimiento unidadProcedimiento)
	{
		return SqlBaseUnidadProcedimiento.modificarServiciosUnidadProcedimiento(con, unidadProcedimiento);
	}
	
	/**
	 * Modifica un registro del Detalle del Servico por Unidad de Procedimiento
	 * @param Connection con
	 * @param UnidadProcedimiento unidadProcedimiento 
	 * */
	public boolean modificarServiciosDetUnidadProcedimiento(Connection con, UnidadProcedimiento unidadProcedimiento)
	{
		return SqlBaseUnidadProcedimiento.modificarServiciosDetUnidadProcedimiento(con, unidadProcedimiento);
	}
	
	
	/**
	 * Elimina un registro de Unidad de Procedimiento
	 * @param Connection con
	 * @param int codigoUndProce
	 * @param int institucion
	 * */
	public boolean eliminarUnidadProcedimiento(Connection con, int codigoUndProce, int institucion)
	{
		return SqlBaseUnidadProcedimiento.eliminarUnidadProcedimiento(con, codigoUndProce, institucion);
	}	
	
	/**
	 * Elimina un registro de Servicios por Unidad de Procedimiento
	 * @param Connection con
	 * @param int CodigoConsecutivoServiUndproce
	 * @param int CodigoUndProce
	 * */
	public boolean eliminarServiciosUnidadProcedimiento(Connection con, int codigoConsecutivoServiUndProce, int codigoUndProce)
	{
		return SqlBaseUnidadProcedimiento.eliminarServiciosUnidadProcedimiento(con, codigoConsecutivoServiUndProce, codigoUndProce);
	}
	
	/**
	 * Eliminar un registro del Detalle de Servicios por Unidad de Procedimiento
	 * @param Connection con
	 * @param codigoConsecutivoServiDetalle
	 * @param codigoConsecutivoServiUndProce 
	 * */
	public boolean eliminarServiciosDetUnidadProcedimiento(Connection con, int codigoConsecutivoServiDetalle, int codigoConsecutivoServiUndProce)
	{
		return SqlBaseUnidadProcedimiento.eliminarServiciosDetUnidadProcedimiento(con, codigoConsecutivoServiDetalle, codigoConsecutivoServiUndProce);
	}
	
	
	/**
	 * Elimina Todos los registros de un Servicio por Unidad de Procedimiento asociado a un codigo de Unidad de Procedimiento
	 * @param Connection con
	 * @param int CodigoUndProce 
	 * */
	public boolean eliminarServiciosTodoUndProc(Connection con, int codigoUndProce)
	{
		return SqlBaseUnidadProcedimiento.eliminarServiciosTodoUndProc(con, codigoUndProce);
	}
	
	
	/**
	 * Elimina Todo el Detalle de un Servicio por Unidad de Procedimiento
	 * @param Connection con
	 * @param int codigoConsecutivoServiUndProce
	 * */
	public boolean eliminarServiciosDetTodoUndProc(Connection con, int codigoUndProce, int codigoServiUndProce, int codigoServiDetalle)
	{
		return SqlBaseUnidadProcedimiento.eliminarServiciosDetTodoUndProc(con, codigoUndProce, codigoServiUndProce, codigoServiDetalle);
	}
	
	/**
	 * Retorna cuantos servicios se encuentra dentro de una unidad de procedimiento
	 * @param Connection con
	 * @param int codigoUndProce
	 * @return int numero de registros 
	 * */
	public  int consultaServicioUndProcCuantos(Connection con, int codigoUndProce)
	{
		return SqlBaseUnidadProcedimiento.consultaServicioUndProcCuantos(con, codigoUndProce);
	}
	
	/**
	 * Retorna cuantos detalles (servicios, condiciones de toma de examen, articulos) existen en un servicio por unidad de procedimiento
	 * @param Connection con
	 * @param int codifgoUndProce
	 * @param int codigoServiUndProce
	 * @return int numero de registros 
	 * */
	public  int consultaDetalleCuantos(Connection con, int codigoUndProce, int codigoServiUndProce)
	{
		return SqlBaseUnidadProcedimiento.consultaDetalleCuantos(con,codigoUndProce,codigoServiUndProce);	
	}
}
 
