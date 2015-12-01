/*
 * Dic 01, 2005
 *
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Sebastián Gómez R
 *
 * Interface utilizada para gestionar los métodos DAO de la funcionalidad
 * Modificar/Reversar Liquidación Qx
 */
public interface ModificarReversarQxDao 
{
	/**
	 * Método implementado para actualizar el valor de un asocio
	 * vinculado a una cirugia específica
	 * @param con
	 * @param codigo
	 * @param valor
	 * @param tipoServicioAsocio
	 * @param estado
	 * @return
	 */
	public int actualizarValorAsocio(Connection con,int codigo,
			double valor,String tipoServicioAsocio,String estado);
	
	/**
	 * Método implementado para reversar la liquidacion de una
	 * orden Quirúrgica
	 * @param con
	 * @param numeroSolicitud
	 * @param estado
	 * @return
	 */
	public int reversarLiquidacionQx(Connection con,int numeroSolicitud,String estado);
	
	
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
			int tipoCambio,String usuario,String motivo,String estado);
	
	
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
			double valorModificado,String consecutivoAsocio,String tipoServicioAsocio, String estado);
	
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
			String tipoIdentificacion,String numeroIdentificacion,int centroAtencion);
	
	/**
	 * Método que consulta el detalle del LOG Modificar/Reversar Qx.
	 * @param con
	 * @param codigoRegistro
	 * @return
	 */
	public HashMap getDetalleLog(Connection con,int codigoRegistro);
	
	/**
	 * Método implementado para actualizar el pool de un asocio
	 * @param con
	 * @param codigo
	 * @param pool
	 * @return
	 */
	public int actualizarPoolAsocio(Connection con,int codigo,int pool);
	
	/**
	 * Método implementado para cargar los convenios de un asocio y su correspondiente valor del cargo
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap cargarConveniosAsocio(Connection con,HashMap campos);
	
	/**
	 * Método que realiza la actualizacion del cargo de un asocio
	 * @param con
	 * @param campos
	 * @return
	 */
	public int actualizarValorAsocioEnCargos(Connection con,HashMap campos);
	
	/**
	 * Método para actualizar el profesional y/o la especialidad del asocio
	 * @param con
	 * @param campos
	 * @return
	 */
	public int actualizarProfesionalEspecialidadAsocio(Connection con,HashMap campos);
}
