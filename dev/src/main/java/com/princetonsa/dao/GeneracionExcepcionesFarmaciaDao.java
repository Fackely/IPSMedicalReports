
/*
 * Creado   7/12/2004
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * Esta <i>interface</i> define el contrato de operaciones que debe implementar la 
 * clase que presta el servicio de acceso a datos para el objeto <code>GeneracionExcepcionesFarmacia</code>.
 *
 * @version 1.0, 7/12/2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Rios</a>
 */
public interface GeneracionExcepcionesFarmaciaDao 
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
	public ResultSetDecorator listadoSolicitudesFarmacia(Connection con, int idCuenta,boolean tipoConsulta);
	
	/**
	 * Inserta una generación de excepciones de farmacia
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param numeroSolicitud
	 * @param codigoArticulo
	 * @param porcentajeNoCubierto
	 * @param usuario
	 * @return int, 0 no inserta, 1 si inserta
	 * @see com.princetonsa.dao.SqlBaseGeneracionExcepcionesFarmaciaDao#insertar(java.sql.Connection,int,int,double,String, int, UsuarioBasico)
	 */
	public int  insertar(	Connection con, int numeroSolicitud, int codigoArticulo,  double porcentajeNoCubierto, String usuario );
	
	/**
	 * modificar una generación de excepciones de farmacia
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param numeroSolicitud
	 * @param codigoArticulo
	 * @param porcentajeNoCubierto
	 * @param usuario
	 * @return int, 0  No modifica, 1 si modifica
	 * @see com.princetonsa.dao.SqlBaseGeneracionExcepcionesFarmaciaDao#modificar(java.sql.Connection,int,int,double,String, Connection, ValoracionHospitalariaForm)
	 */
	public int modificar(	Connection con, int numeroSolicitud, int codigoArticulo,  double porcentajeNoCubierto, String usuario );
	
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
	 * @see com.princetonsa.dao.SqlBaseGeneracionExcepcionesFarmaciaDao#busqueda(java.sql.Connection,int,int,String,boolean,double)
	 */
	public ResultSetDecorator busqueda(		Connection con,
	        										int idCuenta,
													int codigoArea,
													String descripcionArticulo,
													boolean esBusquedaArticuloPorCodigo,
													double porcentajeNoCubierto	) throws SQLException;
	
	  /**
     * Carga el listado de links  con las cuentas que tienen una entrada en la 
	 * generación de excepciones de farmacia
     * @param con
	 * @param codigoPaciente
     * @return
     * @see com.princetonsa.dao.SqlBaseGeneracionExcepcionesFarmaciaDao#linksConsultaGeneracionExcepcionesFarmacia(java.sql.Connection,int)
     */
	public ResultSetDecorator linksConsultaGeneracionExcepcionesFarmacia(Connection con, int codigoPaciente);
	
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
	 * @see com.princetonsa.dao.SqlBaseGeneracionExcepcionesFarmaciaDao#consultaExcepcionesFarmacia(java.sql.Connection,int,int,int)
	 */
	public ResultSetDecorator consultaExcepcionesFarmacia(Connection con, 
														        int codigoConvenio,
														        int codigoCentroCosto,
														        int codigoArticulo,
														        boolean existeExcepcion,
														        boolean consultaExcepcion);
	
	/**
	 * Eliminar Gen Excepciones de farmacia
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoArticulo
	 * @return
	 */
	public int eliminar(	Connection con, 
	        						int numeroSolicitud,
									int codigoArticulo); 
	
}
