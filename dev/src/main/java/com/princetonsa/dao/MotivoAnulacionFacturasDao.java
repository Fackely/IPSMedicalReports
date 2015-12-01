/*
 * @(#)MotivoAnulacionFacturasDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao;

import java.sql.Connection;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 *  Interfaz para el acceder a la fuente de datos de los motivos de anulacion
 *  de una factura
 *
 * @version 1.0, May 05 / 2005
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 */
public interface MotivoAnulacionFacturasDao 
{
	/**
	 * Inserta un motivo de anulacion de facturas dependiendo de la institución
	 * @param con
	 * @param descripcion
	 * @param activo
	 * @param institucion
	 * @return
	 */
	public int  insertar(Connection con, String descripcion, boolean activo, int codigoInstitucion);
	
	/**
	 *  Consulta  la info  de los motivos de anulacion de facturas según la institución
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public ResultSetDecorator cargarMotivosAnulacion(Connection con, int codigoInstitucion);
	
	/**
	 * Modifica un motivo de anulacion de facturas dado su codigo
	 * @param con
	 * @param descripcion
	 * @param activo
	 * @param codigo
	 * @return
	 */
	public int modificar(Connection con, String descripcion, boolean activo, int codigo);
	
	/**
	 *  Consulta  la info  de los motivos de anulacion de facturas según la institución
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public ResultSetDecorator cargarMotivos(Connection con, int codigoInstitucion);
	
	
}
