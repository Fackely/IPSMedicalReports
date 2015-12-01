/*
 * @(#)SqlBasePagosEmpresaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadFecha;

import com.princetonsa.dao.DaoFactory;

/**
 * Implementación sql genérico de todas las funciones de acceso a la fuente de datos
 * para los pagos de empresa
 *
 * @version 1.0, Abril 08 / 2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class SqlBasePagosEmpresaDao 
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBasePagosEmpresaDao.class);
	
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
	public static int  insertar(	Connection con,
											int codigoFactura,
											int codigoTipoDoc,
											String fecha,
											int codigoEstadoCartera,
											double valor,
											String insertarPagosEmpresaStr)
	{
		int resp=0;
		try
			{
					if (con == null || con.isClosed()) 
					{
							DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
							con = myFactory.getConnection();
					}
					PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarPagosEmpresaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1,codigoFactura);
					ps.setInt(2, codigoTipoDoc );
					ps.setString(3, UtilidadFecha.conversionFormatoFechaABD(fecha));
					ps.setInt(4, codigoEstadoCartera);
					ps.setDouble(6, valor);
	
					resp=ps.executeUpdate();
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la inserción de datos: SqlBasePagosEmpresaDao "+e.toString() );
					resp=0;
			}
			return resp;
	}

    
}
