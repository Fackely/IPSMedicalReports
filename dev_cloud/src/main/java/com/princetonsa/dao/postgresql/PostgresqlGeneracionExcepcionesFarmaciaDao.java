
/*
 * Creado   7/12/2004
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import com.princetonsa.dao.GeneracionExcepcionesFarmaciaDao;
import com.princetonsa.dao.sqlbase.SqlBaseGeneracionExcepcionesFarmaciaDao;

/**
 * Esta clase implementa el contrato estipulado en <code>GeneracionExcepcionesFarmacia</code>, proporcionando los servicios
 * de acceso a una base de datos PostgreSQL requeridos por la clase <code>GeneracionExcepcionesFarmacia</code>
 *
 * @version 1.0, 7/12/2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Rios</a>
 */
public class PostgresqlGeneracionExcepcionesFarmaciaDao implements
        GeneracionExcepcionesFarmaciaDao 
{

    /**
	 * Carga el Listado de las solicitudes  de la cuenta cuando son de
	 * farmacia y != de centro_costo_externo, estado_med = Despachada - Admin,
	 * estado_fact= pendiente - cargada 
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param idCuenta, int, id de la cuenta
	 * @param tipoConsulta, boolean true realiza la consulta con todos los porcentajes
	 * 								false realiza la consulta con los porcentajes diferentes de cero 
	 * @return ResulSet list
	 * @see com.princetonsa.dao.SqlBaseGeneracionExcepcionesFarmaciaDao#listadoSolicitudesFarmacia(java.sql.Connection,int,boolean)
	 */
	public ResultSetDecorator listadoSolicitudesFarmacia(Connection con, int idCuenta,boolean tipoConsulta)
	{
	    return SqlBaseGeneracionExcepcionesFarmaciaDao.listadoSolicitudesFarmacia(con,idCuenta,tipoConsulta);
	}
	
	/**
	 * Inserta una generación de excepciones de farmacia
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param numeroSolicitud
	 * @param codigoArticulo
	 * @param porcentajeNoCubierto
	 * @param usuario
	 * @return int, 0 no inserta, 1 si inserta
	 */
	public int  insertar(	Connection con, int numeroSolicitud, int codigoArticulo,  double porcentajeNoCubierto, String usuario )
	{
	    return SqlBaseGeneracionExcepcionesFarmaciaDao.insertar(con,numeroSolicitud,codigoArticulo,porcentajeNoCubierto,usuario);
	}
	
	/**
	 * modificar una generación de excepciones de farmacia
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param numeroSolicitud
	 * @param codigoArticulo
	 * @param porcentajeNoCubierto
	 * @param usuario
	 * @return int, 0  No modifica, 1 si modifica
	 */
	public int modificar(	Connection con, int numeroSolicitud, int codigoArticulo,  double porcentajeNoCubierto, String usuario )
	{
	    return SqlBaseGeneracionExcepcionesFarmaciaDao.modificar(con,numeroSolicitud, codigoArticulo, porcentajeNoCubierto, usuario);
	}
	
	/**
	 * Método que contiene el Resulset de la búsqueda avanzada 
	 * de la generación de Excepciones farmacia
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param idCuenta
	 * @param codigoArea
	 * @param descripcionArticulo
	 * @param esBusquedaArticuloPorCodigo
	 * @param porcentajeNoCubierto
	 * @return Resultset con todos los datos de la tabla empresas
	 * @throws SQLException
	 */
	public ResultSetDecorator busqueda(		Connection con,
	        										int idCuenta,
													int codigoArea,
													String descripcionArticulo,
													boolean esBusquedaArticuloPorCodigo,
													double porcentajeNoCubierto	) throws SQLException
	{
	    return SqlBaseGeneracionExcepcionesFarmaciaDao.busqueda(con, idCuenta, codigoArea, descripcionArticulo, esBusquedaArticuloPorCodigo, porcentajeNoCubierto);
	}
	
	  /**
     * Carga el listado de links  con las cuentas que tienen una entrada en la 
	 * generación de excepciones de farmacia
     * @param con
	 * @param codigoPaciente
     * @return
     */
	public ResultSetDecorator linksConsultaGeneracionExcepcionesFarmacia(Connection con, int codigoPaciente)
	{
	    return SqlBaseGeneracionExcepcionesFarmaciaDao.linksConsultaGeneracionExcepcionesFarmacia(con, codigoPaciente);
	}
	
	/**
	 * consulta de las excepciones de farmacia, segun los códigos de
	 * convenio, centroCosto y articulo
	 * @param con, Connection con la fuente de datos
	 * @param codigoConvenio, código del convenio
	 * @param codigoCentroCosto, código del centro de costo
	 * @param codigoArticulo, código del articulo
	 * @param existeExcepcion, boolean true para realizar la consulta solo por código del convenio
	 * @param consultaExcepcion, boolean true para la consulta por código convenio, centro de costo y articulo
	 * @return ResultSetDecorator con los datos de la consulta.
	 * @see com.princetonsa.dao.SqlBaseGeneracionExcepcionesFarmaciaDao#consultaExcepcionesFarmacia(java.sql.Connection,int,int,int,boolean,boolean)
	 */
	public ResultSetDecorator consultaExcepcionesFarmacia(Connection con, 
														        int codigoConvenio,
														        int codigoCentroCosto,
														        int codigoArticulo,
														        boolean existeExcepcion,
														        boolean consultaExcepcion)
	{
	    return SqlBaseGeneracionExcepcionesFarmaciaDao.consultaExcepcionesFarmacia(con,codigoConvenio,codigoCentroCosto,codigoArticulo,existeExcepcion,consultaExcepcion);
	}
	
	/**
	 * Eliminar Gen Excepciones de farmacia
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoArticulo
	 * @return
	 */
	public int eliminar(	Connection con, 
	        						int numeroSolicitud,
									int codigoArticulo)
	{
	    return SqlBaseGeneracionExcepcionesFarmaciaDao.eliminar(con, numeroSolicitud, codigoArticulo);
	}
    
}
