/*
 * @(#)PostgresqlMotivoAnulacionFacturasDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dao.MotivoAnulacionFacturasDao;
import com.princetonsa.dao.sqlbase.SqlBaseMotivoAnulacionFacturasDao;

import util.ValoresPorDefecto;

/**
 * Implementación postgresql de las funciones de acceso a la fuente de datos
 * para el motivo de anulacion de facturas
 *
 * @version 1.0, May 05 / 2005
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 */
public class PostgresqlMotivoAnulacionFacturasDao implements MotivoAnulacionFacturasDao
{
	/**
	 *  Insertar un motivo de anulacion para las facturas
	 */
	private final static String insertarMotivoAnulacionFacturasStr = "INSERT INTO motivos_anul_fact " +
																	 "(codigo, descripcion, activo, institucion ) " +
																	 "VALUES (nextval('seq_motivos_anul_fact'), lower(?), ?, ? )" ;
	
	/**
	 * Consulta la info de los motivos de anulacion de facturas según la institución
	 */
	private final static String consultaStandarMotivosAnulacionFacturasStr= "SELECT codigo, descripcion, activo, " + ValoresPorDefecto.getValorFalseParaConsultas() + " AS puedoborrar " +
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
	    return SqlBaseMotivoAnulacionFacturasDao.insertar(con,descripcion,activo,codigoInstitucion, insertarMotivoAnulacionFacturasStr);
	}
	
	/**
	 * Consulta  la info  de los motivos de anulacion de facturas según la institución
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
