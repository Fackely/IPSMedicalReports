/*
 * @(#)PagosEmpresaDao.java
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
 *  Interfaz para el acceder a la fuente de datos de los pagos de empresa
 *
 * @version 1.0, Abril 08 / 2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public interface PagosEmpresaDao 
{
	/**
	 * Inserta un pago de empresas
	 * @param con
	 * @param codigoFactura
	 * @param codigoTipoDoc
	 * @param fecha
	 * @param codigoEstadoCartera
	 * @param valor
	 * @param insertarPagosEmpresaStr
	 * @return
	 */
	public int  insertar(	Connection con,
									int codigoFactura,
									int codigoTipoDoc,
									String fecha,
									int codigoEstadoCartera,
									double valor);

}
