/*
 * @(#)OracleFormatoImpresionPresupuestoDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.FormatoImpresionPresupuestoDao;
import com.princetonsa.dao.sqlbase.SqlBaseFormatoImpresionPresupuestoDao;


/**
 * Implementación postgresql de las funciones de acceso a la fuente de datos
 * para los formatos de impresión de presupuesto
 *
 * @version 1.0, 11 /Ene/ 2006
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 */
public class OracleFormatoImpresionPresupuestoDao implements FormatoImpresionPresupuestoDao
{
	
	/**
	 * Cadena con el statement necesario para insertar un formato de impresion basico
	 */
	private static final String insertarFormatoImpresionBasicoStr=" INSERT INTO formato_imp_presupuesto " +
																  "(codigo, " +
																  " nombre_formato," +
																  " titulo_formato," +
																  " cantidad," +
																  " desc_cantidad," +
																  " valor_unitario," +
																  " desc_val_unitario," +
																  " valor_total," +
																  " desc_val_total," +
																  " nota_pie_pagina," +
																  " fecha_hora," +
																  " institucion)" +
																  " VALUES (seq_formato_imp_presupuesto.nextval, ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?)";
	
	/**
	 * Cadena con el statement necesario para insertar el detalle de Ariticulo de un formato de impresión
	 */
	private static final String insertarDetalleArticulosStr=" INSERT INTO det_art_formato_imp " +
														    " (codigo_formato," +
														    " desc_seccion_art," +
														    " detalle_articulos," +
														    " nivel," +
														    " detalle_nivel," +
														    " valores_detalle," +
														    " subtotal_nivel," +
														    " prioridad," +
														    " medicamentos_articulos)" +
															" VALUES (? , ? , ? , ? , ? , ? , ? , ?, ?)";
	
	/**
	 * Cadena con el statement necesario para insertar el detalle de los servicios de un formato de impresión de presupuesto
	 */
	private static final String insertarDetalleServiciosStr=" INSERT INTO det_serv_formato_imp " +
															" (codigo_formato," +
															" detalle," +
															" valores_detalle," +
															" subtotal_grupo," +
															" prioridad," +
															" grupo)" +
														    " VALUES (? , ? , ? , ? , ? , ?)";
	
	
	
	/**
	 * Método para consultar los formatos de impresión 
	 * existentes para una insititución
	 * @param con
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarFormatosExistentes (Connection con, int institucion) throws SQLException
	{
		return SqlBaseFormatoImpresionPresupuestoDao.consultarFormatosExistentes(con, institucion);
	}
	
	/**
	 * Método para consultar los Grupos de Servicios existentes
	 * @param con
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarGruposServicios (Connection con) throws SQLException
	{
		return SqlBaseFormatoImpresionPresupuestoDao.consultarGruposServicios(con);
	}
	
	/**
	 * Método para consultar todo el formato de impresion de presupuesto 
	 * @param con
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarFormatoImpresion (Connection con, int codigoFormato) throws SQLException
	{
		return SqlBaseFormatoImpresionPresupuestoDao.consultarFormatoImpresion(con, codigoFormato);
	}
	
	/**
	 * Método para consultar el detalle de los servicios de un formato de impresion
	 * @param con
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarDetServicios (Connection con, int codigoFormato) throws SQLException
	{
		return SqlBaseFormatoImpresionPresupuestoDao.consultarDetServicios(con, codigoFormato);
	}
	
	/**
	 * Método para consultar el detalle de los articulos de un formato de impresion
	 * @param con
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarDetArticulos (Connection con, int codigoFormato) throws SQLException
	{
		return SqlBaseFormatoImpresionPresupuestoDao.consultarDetAritculos(con, codigoFormato);
	}
	
	/**
	 * Método para insertar un Formato de Impresión de un Presupuesto completo con el detalle
	 * de servicios y con el detalle de articulos
	 * @param con
	 * @param nombreFormato
	 * @param tituloFormato
	 * @param cantidad
	 * @param descripcionCantidad
	 * @param valorUnitario
	 * @param descripcionValUnitario
	 * @param valorTotal
	 * @param descripcionValTotal
	 * @param piePagina
	 * @param fechaHora
	 * @param institucion
	 * @param mapaDetServicios
	 * @param mapaDetArticulos
	 * @param insertarFormatoImpresionBasicoStr
	 * @param insertarDetalleServiciosStr
	 * @param insertarDetalleArticulosStr
	 * @return
	 * @throws SQLException
	 */
	public int insertarFormatoImpresion(Connection con, String nombreFormato, String tituloFormato, boolean cantidad, String descripcionCantidad, boolean valorUnitario, String descripcionValUnitario, boolean valorTotal, String descripcionValTotal, String piePagina, boolean fechaHora, int institucion, HashMap mapaDetServicios, HashMap mapaDetArticulos) throws SQLException
	{
		return SqlBaseFormatoImpresionPresupuestoDao.insertarFormatoImpresion(con, nombreFormato, tituloFormato, cantidad, descripcionCantidad, valorUnitario, descripcionValUnitario, valorTotal, descripcionValTotal, piePagina, fechaHora, institucion, mapaDetServicios, mapaDetArticulos, insertarFormatoImpresionBasicoStr, insertarDetalleServiciosStr, insertarDetalleArticulosStr);
	}
	
	
	/**
	 * Método para eliminar por completo el formato de impresion de presupuesto por medio de tres pasos:
	 * 1. Eliminar el detalle de Servicion
	 * 2. Eliminar el detalle de Articulos
	 * 3. Eliminar el formato basico 
	 * @param con
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public  int eliminarFormatoCompleto(Connection con, int codigoFormato) throws SQLException
	{
		return SqlBaseFormatoImpresionPresupuestoDao.eliminarFormatoCompleto(con, codigoFormato);
	}
	
	/**
	 * Método para modificar un Formato de Impresión de un Presupuesto completo con el detalle
	 * de servicios y con el detalle de articulos
	 * @param con
	 * @param nombreFormato
	 * @param tituloFormato
	 * @param cantidad
	 * @param descripcionCantidad
	 * @param valorUnitario
	 * @param descripcionValUnitario
	 * @param valorTotal
	 * @param descripcionValTotal
	 * @param piePagina
	 * @param fechaHora
	 * @param institucion
	 * @param mapaDetServicios
	 * @param mapaDetArticulos
	 * @param insertarFormatoImpresionBasicoStr
	 * @param insertarDetalleServiciosStr
	 * @param insertarDetalleArticulosStr
	 * @return
	 * @throws SQLException
	 */
	public  int modificarFormatoImpresion(Connection con, int codigoFormato, String nombreFormato, String tituloFormato, boolean cantidad, String descripcionCantidad, boolean valorUnitario, String descripcionValUnitario, boolean valorTotal, String descripcionValTotal, String piePagina, boolean fechaHora,  HashMap mapaDetServicios, HashMap mapaDetArticulos) throws SQLException
	{
		return SqlBaseFormatoImpresionPresupuestoDao.modificarFormatoImpresion(con, codigoFormato, nombreFormato, tituloFormato, cantidad, descripcionCantidad, valorUnitario, descripcionValUnitario, valorTotal, descripcionValTotal, piePagina, fechaHora, mapaDetServicios, mapaDetArticulos, DaoFactory.ORACLE);
	}
	/**
	 * Eliminar un grupo de servicios dado un formato y el codigo del grupo
	 * @param con
	 * @param codigoFormato
	 * @param codigoGrupo
	 * @return
	 * @throws SQLException
	 */
	public int eliminarGrupo(Connection con, int codigoFormato, int codigoGrupo)
	{
		return SqlBaseFormatoImpresionPresupuestoDao.eliminarGrupo(con, codigoFormato, codigoGrupo);
	}
}
