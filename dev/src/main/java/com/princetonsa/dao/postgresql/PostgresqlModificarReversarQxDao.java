/*
 * Dic 01, 2005
 *
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.util.HashMap;

import util.ValoresPorDefecto;

import com.princetonsa.dao.ModificarReversarQxDao;
import com.princetonsa.dao.sqlbase.SqlBaseModificarReversarQxDao;

/**
 * @author Sebastián Gómez
 *
 * Clase que maneja los métodos propìos de Postgres para el acceso a la fuente
 * de datos en la funcionalidad Modificar Reversar Liquidacion Qx
 */
public class PostgresqlModificarReversarQxDao implements ModificarReversarQxDao 
{
	/**
	 * Cadena que inserta el encabezado del log modificacion/reversion
	 */
	private static final String insertarEncabezadoLogStr = "INSERT INTO " +
			"cambios_liquidacion_qx " +
			"(codigo,numero_solicitud,fecha_cirugia,tipo_cambio," +
			"fecha_grabacion,hora_grabacion,usuario,motivo) " +
			"VALUES " +
			"(nextval('seq_cambios_liquid_qx'),?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?)";
	
	
	/**
	 * Cadena que inserta el LOG Modificacion/Reversion de la liquidacion Qx.
	 */
	private static final String insertarDetalleLogStr = "INSERT INTO " +
			"det_cambios_liquidacion_qx " +
			"(codigo,codigo_cambio_liquid,consecutivo,codigo_servicio," +
			"descripcion_servicio,codigo_servicio_asocio," +
			"descripcion_servicio_asocio,codigo_asocio,nombre_asocio," +
			"valor_inicial,valor_modificado,det_cx_honorarios,det_asocio_cx_salas_mat) " +
			"VALUES " +
			"(nextval('seq_det_cambios_liquid_qx'),?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * Método implementado para actualizar el valor de un asocio
	 * vinculado a una cirugia específica
	 * @param con
	 * @param codigo
	 * @param valor
	 * @param grupoAsocio
	 * @param estado
	 * @return
	 */
	public int actualizarValorAsocio(Connection con,int codigo,
			double valor,String tipoServicioAsocio,String estado)
	{
		return SqlBaseModificarReversarQxDao.actualizarValorAsocio(con,codigo,valor,tipoServicioAsocio,estado);
	}
	
	/**
	 * Método implementado para reversar la liquidacion de una
	 * orden Quirúrgica
	 * @param con
	 * @param numeroSolicitud
	 * @param estado
	 * @return
	 */
	public int reversarLiquidacionQx(Connection con,int numeroSolicitud,String estado)
	{
		return SqlBaseModificarReversarQxDao.reversarLiquidacionQx(con,numeroSolicitud,estado);
	}
	
	/**
	 * Método que inserta el encabezado de un log
	 * de modificacion/reversion liquidacion _Qx
	 * @param con
	 * @param numeroSolicitud
	 * @param fechaCirugia
	 * @param tipoCambio
	 * @param usuario
	 * @param motivo
	 * @param estado
	 * @return
	 */
	public int insertarEncabezadoLog(Connection con,int numeroSolicitud,String fechaCirugia,
			int tipoCambio,String usuario,String motivo,String estado)
	{
		return SqlBaseModificarReversarQxDao.insertarEncabezadoLog(con,numeroSolicitud,fechaCirugia,tipoCambio,usuario,motivo,estado,insertarEncabezadoLogStr);
	}
	
	/**
	 * Método implementado para insertar el detalle de un log
	 * de modificacion/reversion liquidacion Qx.
	 * @param con
	 * @param codigoCambio
	 * @param numeroServicio
	 * @param codigoServicio
	 * @param nombreServicio
	 * @param codigoServicioAsocio
	 * @param nombreServicioAsocio
	 * @param codigoAsocio
	 * @param nombreAsocio
	 * @param valorInicial
	 * @param valorModificado
	 * @param estado
	 * @return
	 */
	public int insertarDetalleLog(
			Connection con,int codigoCambio,int numeroServicio,int codigoServicio,String nombreServicio,
			int codigoServicioAsocio,String nombreServicioAsocio,int codigoAsocio, String nombreAsocio,double valorInicial,
			double valorModificado,String consecutivoAsocio,String tipoServicioAsocio,String estado)
	{
		return SqlBaseModificarReversarQxDao.insertarDetalleLog(
				con,codigoCambio,numeroServicio,codigoServicio,
				nombreServicio,codigoServicioAsocio,nombreServicioAsocio,
				codigoAsocio,nombreAsocio,valorInicial,valorModificado,consecutivoAsocio,tipoServicioAsocio,
				insertarDetalleLogStr,estado);
	}
	
	/**
	 * Método que realiza la busqueda de los LOG modificacion/reversion Qx.
	 * de la BD
	 * @param con
	 * @param tipoCambio
	 * @param usuario
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param ordenInicial
	 * @param ordenFinal
	 * @param tipoIdentificacion
	 * @param numeroIdentificacion
	 * @param centroAtencion
	 * @return
	 */
	public HashMap busquedaGeneralLOG(
			Connection con,int tipoCambio,String usuario,String fechaInicial,
			String fechaFinal,int ordenInicial,int ordenFinal,
			String tipoIdentificacion,String numeroIdentificacion,int centroAtencion)
	{
		return SqlBaseModificarReversarQxDao.busquedaGeneralLOG(con,tipoCambio,usuario,fechaInicial,fechaFinal,ordenInicial,ordenFinal,tipoIdentificacion,numeroIdentificacion,centroAtencion);
	}
	
	/**
	 * Método que consulta el detalle del LOG Modificar/Reversar Qx.
	 * @param con
	 * @param codigoRegistro
	 * @return
	 */
	public HashMap getDetalleLog(Connection con,int codigoRegistro)
	{
		return SqlBaseModificarReversarQxDao.getDetalleLog(con,codigoRegistro);
	}
	
	/**
	 * Método implementado para actualizar el pool de un asocio
	 * @param con
	 * @param codigo
	 * @param pool
	 * @return
	 */
	public int actualizarPoolAsocio(Connection con,int codigo,int pool)
	{
		return SqlBaseModificarReversarQxDao.actualizarPoolAsocio(con,codigo,pool);
	}
	
	/**
	 * Método implementado para cargar los convenios de un asocio y su correspondiente valor del cargo
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap cargarConveniosAsocio(Connection con,HashMap campos)
	{
		return SqlBaseModificarReversarQxDao.cargarConveniosAsocio(con, campos);
	}
	
	/**
	 * Método que realiza la actualizacion del cargo de un asocio
	 * @param con
	 * @param campos
	 * @return
	 */
	public int actualizarValorAsocioEnCargos(Connection con,HashMap campos)
	{
		return SqlBaseModificarReversarQxDao.actualizarValorAsocioEnCargos(con, campos);
	}
	
	/**
	 * Método para actualizar el profesional y/o la especialidad del asocio
	 * @param con
	 * @param campos
	 * @return
	 */
	public int actualizarProfesionalEspecialidadAsocio(Connection con,HashMap campos)
	{
		return SqlBaseModificarReversarQxDao.actualizarProfesionalEspecialidadAsocio(con, campos);
	}
}
