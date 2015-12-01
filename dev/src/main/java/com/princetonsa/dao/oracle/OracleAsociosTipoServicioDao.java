/*
 * @(#)OracleAsociosTipoServicioDao.java
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

import com.princetonsa.dao.AsociosTipoServicioDao;
import com.princetonsa.dao.sqlbase.SqlBaseAsociosTipoServicioDao;

/**
 * Implementación oracle de las funciones de acceso a la fuente de datos
 * para asocios x  tipo servicio
 *
 * @version 1.0, Septiembre  20 / 2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class OracleAsociosTipoServicioDao implements AsociosTipoServicioDao 
{
    /**
	 * Inserta un asocio x tipo servicio
	 */
	private String insertarAsociosTipoServicioStr= 	"INSERT INTO asocios_tipo_serv " +
																			"(codigo, " +
																			"esq_tar_particular, " +
																			"esq_tar_general, " +
																			"tipo_servicio, " +
																			"tipo_asocio, " +
																			"servicio, " +
																			"activo, " +
																			"institucion) " +
																			"VALUES (seq_aso_tipo_serv.nextval, ?, ?, ?, ?, ?, ?, ?)";
    
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
												int codigoInstitucion)
	{
	    return SqlBaseAsociosTipoServicioDao.insertar(con, codigoEsquemaTarifario, esEsquemaTarifarioGeneral, acronimoTipoServicio, codigoTipoAsocio, codigoServicio, activo, codigoInstitucion, insertarAsociosTipoServicioStr);
	}
	
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
				    						)
	{
	    return SqlBaseAsociosTipoServicioDao.modificar(con, codigo, codigoServicio, activo);
	}
	
	/**
	 * Consulta la info de la participación del pool X tarifa dado el cod del pool 
	 * @param con
	 * @param codigoPool
	 * @return
	 */
	public ResultSetDecorator listadoAsociosTipoServicio(Connection con, int codigoInstitucion)
	{
	    return SqlBaseAsociosTipoServicioDao.listadoAsociosTipoServicio(con, codigoInstitucion);
	}
	
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
																				int codigo)
	{
	    return SqlBaseAsociosTipoServicioDao.existeRegistroAsocioTipoServicio(con, codigoEsquemaTarifario, esEsquemaTarifarioGeneral, acronimoTipoServicio, codigoTipoAsocio, codigoServicio, activo, codigoInstitucion, esModificacion, codigo);
	}
	
}