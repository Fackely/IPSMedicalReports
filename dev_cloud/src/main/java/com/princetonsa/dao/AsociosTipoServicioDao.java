/*
 * @(#)AsociosTipoServicioDao.java
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
 *  Interfaz para el acceder a la fuente de datos de asocios tipo servicio
 *
 * @version 1.0, Sep 20 / 2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public interface AsociosTipoServicioDao 
{
    /**
	 * Método para la insercion de un asocio x tipo servicio
	 * @param con
	 * @param codigoEsquemaTarifario
	 * @param esEsquemaTarifarioGeneral
	 * @param acronimoTipoServicio
	 * @param codigoTipoAsocio
	 * @param codigoServicio
	 * @param activo
	 * @param codigoInstitucion
	 * @return
	 */
	public boolean   insertar(		Connection con,
												int codigoEsquemaTarifario,
												boolean esEsquemaTarifarioGeneral,
												String acronimoTipoServicio,
												int codigoTipoAsocio,
												int codigoServicio,
												boolean activo,
												int codigoInstitucion);
	
	/**
	 * Método para la modioficacion de un grupo
	 * @param con
	 * @param grupo
	 * @param codigoAsocio
	 * @param codigoEsquemaTarifario
	 * @param codigoCups
	 * @param codigoSoat
	 * @param codigoTipoLiquidacion
	 * @param unidades
	 * @param valor
	 * @param activo
	 * @param codigoPKGrupo
	 * @return
	 */
	public boolean modificar(		Connection con,
	        									int codigo,	
												int codigoServicio,
												boolean activo
				    						); 
	
	/**
	 * Consulta la info de la participación del pool X tarifa dado el cod del pool 
	 * @param con
	 * @param codigoPool
	 * @return
	 */
	public ResultSetDecorator listadoAsociosTipoServicio(Connection con, int codigoInstitucion);
	
	/**
	 * Metodo que indoca si existe o no un registro con estas caracteristicas, debido a que el unique 
	 * en la bd no evalua en valores null
	 * @param con
	 * @param codigoEsquemaTarifario
	 * @param esEsquemaTarifarioGeneral
	 * @param acronimoTipoServicio
	 * @param codigoTipoAsocio
	 * @param codigoServicio
	 * @param activo
	 * @param codigoInstitucion
	 * @return
	 */
	public boolean existeRegistroAsocioTipoServicio(		Connection con,
																				int codigoEsquemaTarifario,
																				boolean esEsquemaTarifarioGeneral,
																				String acronimoTipoServicio,
																				int codigoTipoAsocio,
																				int codigoServicio,
																				boolean activo,
																				int codigoInstitucion,
																				boolean esModificacion,
																				int codigo);
}