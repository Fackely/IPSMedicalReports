/*
 * @(#)OracleTiposConvenioDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK jdk1.5.0_07
 *
 */
package com.princetonsa.dao.oracle.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.facturacion.TiposConvenioDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseTiposConvenioDao;
import com.princetonsa.mundo.facturacion.TiposConvenio;

/**  
 * @author Julián Pacheco Jiménez
 * jpacheco@princetonsa.com
 * Funcionalidad descrita en Anexo 394 - Tipos de Convenio
 */
public class OracleTiposConvenioDao implements TiposConvenioDao
{
	/**
	 * Insertar un registro de tipos convenio
	 * @param con
	 * @param TiposConvenio tiposconvenio
	 */
	public boolean insertarTiposConvenio(Connection con, TiposConvenio tiposconvenio)
	{
		return SqlBaseTiposConvenioDao.insertarTiposConvenio(con, tiposconvenio);
	}
	
	/**
	 * Modifica un tipo de convenio registrado
	 * @param con
	 * @param TiposConvenio tiposconvenio 
	 */
	public boolean modificarTiposConvenio(Connection con, TiposConvenio tiposconvenio, String codigoAntesMod)
	{
		return SqlBaseTiposConvenioDao.modificarTiposConvenio(con, tiposconvenio, codigoAntesMod);
	}
	
	/**
	 * Elimina un tipo de convenio registrado
	 * @param con
	 * @param String codigo
	 * @param int institucion
	 */
	public boolean eliminarTiposConvenio(Connection con, String codigo, int institucion)
	{
		return SqlBaseTiposConvenioDao.eliminarTiposConvenio(con, codigo, institucion);
	}
	
	/**
	 * Consulta los tipos de convenio
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap consultarTiposConvenio(Connection con, int codigoInstitucion)
	{
		return SqlBaseTiposConvenioDao.consultarTiposConvenio(con, codigoInstitucion);
	}
	
	/**
	 * Consulta los tipos de convenio por codigo
	 */
	public HashMap consultarTiposConvenioEspecifico(Connection con, int codigoInstitucion, String codigo)
	{
		return SqlBaseTiposConvenioDao.consultarTiposConvenioEspecifico(con, codigoInstitucion, codigo);
	}
	
	/**
	 * Elimina el Rubro para un registro 
	 */
	public boolean eliminarRubro(Connection con, int codigoEliminar) 
	{
		return SqlBaseTiposConvenioDao.eliminarRubro(con, codigoEliminar);
	}
}
