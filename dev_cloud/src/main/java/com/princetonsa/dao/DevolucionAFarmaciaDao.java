/*
 * @(#)DevolucionAFarmaciaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao;

import java.sql.Connection;

/**
 *  Interfaz para el acceder a la fuente de datos de la devoluci�n de medicamentos
 *
 * @version 1.0, Septiembre 29 / 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson R�os</a>
 */
public interface DevolucionAFarmaciaDao 
{
	/**
	 * Inserta una devoluci�n de medicamentos
	 * @param con, Connection
	 * @param motivo, String
	 * @param fecha, String
	 * @param hora, String
	 * @param usuario, String
	 * @param estadoDevolucion, int
	 * @param tipoDevolucion, (Autom�tica - Manual)
	 * @return int (0 -ultimoCodigoSequence) 
	 */
	public int insertarDevolucionMedicamentos	(	Connection con, String observaciones,  
	        										String fecha, String hora, 
													String usuario, int estadoDevolucion,
													int centroCostoDevuelve, int farmacia, int tipoDevolucion, String motivo, int institucion);
	
	/**
	 * Carga la ultima devolucion  insertada   (table= devolucion_med))
	 * @param con
	 * @return
	 */
	public  int cargarUltimoCodigoSequence(Connection con);

	/**
	 * Inserta el DETALLE de la devoluci�n del medicamento
	 * @param con, Connection
	 * @param devolucion, C�digo de la tabla devolucion_med
	 * @param numeroSolicitud, N�mero de la solicitud
	 * @param articulo, c�digo del art�culo a devolver
	 * @param cantidad, cantidad
	 * @return
	 */
	public  int insertarDetalleDevolucionMedicamentos		(	Connection con, int  devolucion,  
			   																			int numeroSolicitud, int articulo, 
																						int cantidad,
																						String lote,
																						String fechaVencimientoLote);
	
	/**
	 * Indica si existe o no devolucuion para un numero de solicutd -Articulo especificos
	 * @param con
	 * @param numeroSolicitudStr
	 * @param codigoArticuloStr
	 * @return
	 */
	public boolean existeDevolucionParaNumeroSolicitudArticulo(Connection con, String numeroSolicitudStr, String codigoArticuloStr);
	
}