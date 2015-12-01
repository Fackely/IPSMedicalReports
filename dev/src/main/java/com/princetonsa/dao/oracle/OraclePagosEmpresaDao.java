/*
 * @(#)OraclePagosEmpresaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import com.princetonsa.dao.PagosEmpresaDao;
import com.princetonsa.dao.sqlbase.SqlBasePagosEmpresaDao;

/**
 * Implementación postgresql de las funciones de acceso a la fuente de datos
 * para los pagos de empresa
 *
 * @version 1.0, Abril 08 / 2005
 * @author wrios
 */
public class OraclePagosEmpresaDao implements PagosEmpresaDao
{
    /**
	 *  Insertar pagos de empresa
	 */
	private final static String insertarPagosEmpresaStr="INSERT INTO ........" ;
    
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
									double valor)
	{
	    return SqlBasePagosEmpresaDao.insertar(	con, codigoFactura, codigoTipoDoc, 
	            														fecha, codigoEstadoCartera, valor, 
	            														insertarPagosEmpresaStr);
	}
	    
}
