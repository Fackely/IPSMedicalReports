/*
 * @(#)FormatoImpresionPresupuestoDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

/**
 *  Interfaz para el acceder a la fuente de datos de los formatos
 *  de impresi�n de presupuesto
 *
 * @version 1.0, 11 /Ene/ 2006
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 */
public interface FormatoImpresionPresupuestoDao 
{
	/**
	 * M�todo para consultar los formatos de impresi�n 
	 * existentes para una insitituci�n
	 * @param con
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarFormatosExistentes (Connection con, int institucion) throws SQLException;
	
	/**
	 * M�todo para consultar los Grupos de Servicios existentes
	 * @param con
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarGruposServicios (Connection con) throws SQLException;
	
	
	/**
	 * M�todo para consultar todo el formato de impresion de presupuesto 
	 * @param con
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarFormatoImpresion (Connection con, int codigoFormato) throws SQLException;
	
	/**
	 * M�todo para consultar el detalle de los servicios de un formato de impresion
	 * @param con
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarDetServicios (Connection con, int codigoFormato) throws SQLException;
	
	/**
	 * M�todo para consultar el detalle de los artitculos de un formato de impresion
	 * @param con
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarDetArticulos (Connection con, int codigoFormato) throws SQLException;
	
	
	/**
	 * M�todo para insertar un Formato de Impresi�n de un Presupuesto completo con el detalle
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
	public int insertarFormatoImpresion(Connection con, String nombreFormato, String tituloFormato, boolean cantidad, String descripcionCantidad, boolean valorUnitario, String descripcionValUnitario, boolean valorTotal, String descripcionValTotal, String piePagina, boolean fechaHora, int institucion, HashMap mapaDetServicios, HashMap mapaDetArticulos) throws SQLException;
	
	
	/**
	 * M�todo para eliminar por completo el formato de impresion de presupuesto por medio de tres pasos:
	 * 1. Eliminar el detalle de Servicion
	 * 2. Eliminar el detalle de Articulos
	 * 3. Eliminar el formato basico 
	 * @param con
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public int eliminarFormatoCompleto(Connection con, int codigoFormato) throws SQLException;
	
	
	/**
	 * M�todo para modificar un Formato de Impresi�n de un Presupuesto completo con el detalle
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
	public int modificarFormatoImpresion(Connection con, int codigoFormato, String nombreFormato, String tituloFormato, boolean cantidad, String descripcionCantidad, boolean valorUnitario, String descripcionValUnitario, boolean valorTotal, String descripcionValTotal, String piePagina, boolean fechaHora,  HashMap mapaDetServicios, HashMap mapaDetArticulos) throws SQLException;
	
	/**
	 * Eliminar un grupo de servicios dado un formato y el codigo del grupo
	 * @param con
	 * @param codigoFormato
	 * @param codigoGrupo
	 * @return
	 * @throws SQLException
	 */
	public int eliminarGrupo(Connection con, int codigoFormato, int codigoGrupo);
	
}
