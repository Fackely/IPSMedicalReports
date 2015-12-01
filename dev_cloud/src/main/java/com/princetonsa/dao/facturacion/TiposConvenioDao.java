/*
 * @(#)TiposConveniosDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK jdk1.5.0_07
 *
 */
package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.mundo.facturacion.TiposConvenio;

/**
 * 
 * @author Julián Pacheco Jiménez 
 * jpacheco@princetonsa.com
 */
public interface TiposConvenioDao 
{
	/**
	 * Insertar un registro de tipo convenio
	 * @param con
	 * @param TiposConvenio tiposconvenio
	 */
	public boolean insertarTiposConvenio(Connection con, TiposConvenio tiposconvenio); 
	
	/**
	 * Modifica un tipo de convenio registrado
	 * @param con
	 * @param TiposConvenio tiposconvenio
	 */
	public boolean modificarTiposConvenio(Connection con, TiposConvenio tiposconvenio, String codigoAntesMod);
	
	/**
	 * Elimina un tipo de convenio registrado
	 * @param con
	 * @String codigo
	 * @int institucion
	 */
	public boolean eliminarTiposConvenio(Connection con, String codigo,int institucion);
	
	/**
	 * Consulta de los tipos de convenio
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap consultarTiposConvenio(Connection con, int codigoInstitucion);
	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @param codigo
	 * @return
	 */
	public HashMap consultarTiposConvenioEspecifico(Connection con, int codigoInstitucion, String codigo);
	
	/**
	 * 
	 * @param con
	 * @param codigoEliminar
	 * @return
	 */
	public boolean eliminarRubro(Connection con, int codigoEliminar);
	
}
