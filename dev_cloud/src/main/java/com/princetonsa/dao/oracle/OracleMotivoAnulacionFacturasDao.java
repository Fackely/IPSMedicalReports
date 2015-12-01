/*
 * @(#)OracleMotivoAnulacionFacturasDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dao.MotivoAnulacionFacturasDao;
import com.princetonsa.dao.sqlbase.SqlBaseMotivoAnulacionFacturasDao;

import util.ConstantesBD;
import util.ValoresPorDefecto;

/**
 * Implementación Oracle de las funciones de acceso a la fuente de datos
 * para los motivos de anulacion de facturas
 *
 * @version 1.0, May 05 / 2005
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 */
public class OracleMotivoAnulacionFacturasDao implements MotivoAnulacionFacturasDao
{
	/**
	 *  Insertar un motivo anulacion de facturas
	 */
	private final static String insertarMotivoStr ="INSERT INTO motivos_anul_fact " +
													   "( codigo, descripcion, activo, institucion ) " +
													   "VALUES (seq_motivos_anul_fact.nextval, lower(?), ?, ? )" ;
	
	/**
	 * Consulta la info de los motivos de anulacion de facturas según la institución
	 */
	private final static String consultaStandarMotivosAnulacionFacturasStr= "SELECT codigo, descripcion, CASE WHEN activo=1 THEN '"+ConstantesBD.valorTrueEnString+"' else '"+ConstantesBD.valorFalseEnString+"' end as activo , " + ValoresPorDefecto.getValorFalseParaConsultas() + " AS puedoborrar " +
																	        "FROM motivos_anul_fact WHERE institucion= ? ORDER BY descripcion ";
	
	
	/**
	 * Inserta un motivo de anulacion de facturas dependiendo de la institución
	 * @param con
	 * @param descripcion
	 * @param activo
	 * @param institucion
	 * @return
	 */
	public int  insertar(Connection con, String descripcion, boolean activo, int codigoInstitucion)
	{
	    return SqlBaseMotivoAnulacionFacturasDao.insertar(con,descripcion,activo,codigoInstitucion, insertarMotivoStr);
	}
	
	/**
	 * Consulta  la info  de los motivos de anulacion facturas según la institución
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public ResultSetDecorator cargarMotivosAnulacion(Connection con, int codigoInstitucion)
	{
	    return SqlBaseMotivoAnulacionFacturasDao.cargarMotivosAnulacion(con, codigoInstitucion, consultaStandarMotivosAnulacionFacturasStr);
	}
	
	/**
	 * Modifica un motivo de anulacion de facturas dado su codigo
	 * @param con
	 * @param descripcion
	 * @param activo
	 * @param codigo
	 * @return
	 */
	public int modificar(Connection con, String descripcion, boolean activo, int codigo)
	{
	    return SqlBaseMotivoAnulacionFacturasDao.modificar(con, descripcion, activo, codigo);
	}


	/**
	 * Consulta  la info  motivos de anulacion de facturas según la institución
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public ResultSetDecorator cargarMotivos(Connection con, int codigoInstitucion)
	{
	    return SqlBaseMotivoAnulacionFacturasDao.cargarMotivos(con, codigoInstitucion, consultaStandarMotivosAnulacionFacturasStr);
	}
	

	
}
