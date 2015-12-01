/*
 * @(#)AsociarCxCAFacturasDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.capitacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

/**
 *  Interfaz para el acceder a la fuente de datos de associar cuentas cobro capitacion facturas
 *
 * @version 1.0, Julio 31 / 2006
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public interface AsociarCxCAFacturasDao 
{
	/**
	 * busqueda de las cuentas de cobro a asociar
	 * @param con
	 * @param criteriosBusquedaMap
	 * @return
	 * @throws SQLException
	 */	
	public HashMap busquedaCuentasCobroAAsociar(	Connection con,
													HashMap criteriosBusquedaMap
											 )throws SQLException;
	
	/**
	 * insertar
	 * @param con
	 * @param numeroCuentaCobro
	 * @param contabilizado
	 * @param codigoConvenio 
	 * @param loginUsuario
	 * @param institucion
	 * @return
	 */
	public boolean insertar (	Connection con, String numeroCuentaCobro,
								String contabilizado, int codigoConvenio, String loginUsuario,
								int institucion, int cantidadFacturasAsociadas);
	
	/**
	 * selecciona las facturas a asociar a una cuenta de cobro, recibe un vector con las facturas ya seleccionadas para que
	 * no existan problemas con n cuentas de cobro que tengan la misma factura, es decir, se asigna al primero que llegue
	 * @param con
	 * @param fechaInicialCuentaCobro
	 * @param fechaFinalCuentaCobro
	 * @param codigoConvenio
	 * @param facturasYaSelecionadasVector
	 * @return
	 */
	public HashMap seleccionFacturasAAsociar(	Connection con , String fechaInicialCuentaCobro, String fechaFinalCuentaCobro, int codigoConvenio, Vector facturasYaSelecionadasVector);
	
}
