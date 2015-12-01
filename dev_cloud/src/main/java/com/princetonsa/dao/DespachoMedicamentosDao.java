/*
 * @(#)DespachoMedicamentosDao.java
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
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import com.princetonsa.decorator.ResultSetDecorator;
/**
 *  Interfaz para el acceder a la fuente de datos del despacho de medicamentos
 *
 * @version 1.0, Abril 29 / 2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan López</a>
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public interface DespachoMedicamentosDao 
{
	/**
	 * Carga el Listado de las solicitudes internas de medicamentos
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param codigoCentroCostoUser, int, codigo del centro de costo del usuario (filtro)
	 * @param filtrarPorCantidad Filtrar si se muestran o no las solicitudes en las cuales no se ha ingresado cantidad
	 * @return ResulSet list
	 */
	public ResultSetDecorator listadoSolicitudesMedicamentos(Connection con, HashMap<String, Object> criteriosBusquedaMap);
	
	/**
	 * Carga la Información de la creación de la solicitud o Carga el Listado de la info de medicamentos solicitado
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param numeroSolicitud, int, numero de la solicitud 
	 * @param esMedicamentos, boolean, determina el tipo de consulta
	 * @param codigoInstitucion Institución del usuario
	 * @return ResulSet list
	 */
	public ResultSetDecorator infoSolicitud(Connection con, int numeroSolicitud, boolean esMedicamentos, int codigoInstitucion, int codigoArticulo);
	
	/**
	 * Carga todos los registros existentes para Insumos.
	 * @param con Connection, Conexion con la fuente de datos
	 * @param numeroSolicitud, int, numero de la solicitud
	 * @return ResultSet
	 */
	public ResultSetDecorator infoInsumos(Connection con, int numeroSolicitud);
	
	/**
	 * Cuando un User adiciona sustitutos a los medicamentos originales, entonces se tiene que añadir más filas a la collection,
	 * para hacer esto se adaptó éste método, el cual en principio reutiliza la consulta listadoMedicamentosStr,
	 * y por medio del 'numeroOriginal y el numeroSustituto' hace un UNION adicionandole el valor del sustituto 
	 * a la colección.
	 * @param con, Connection, Conexión con la fuente de datos
	 * @param numeroSolicitud, número de la solicitud,
	 * @param coleccionNumerosSustitutos, Collection, contiene el numeroSustituto-numeroOriginal
	 * @param codigoInstitucion @todo
	 * 
	 * @return ResulSet, con la lista de los medicamentos originales y sus sustitutos
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaGenerada (Connection con, int numeroSolicitud, Collection coleccionNumerosSustitutos, int codigoInstitucion, int codigoArticulo) throws SQLException;
	
	/**
	 * Cuando un User adiciona sustitutos a los medicamentos originales,se deben consultar los equivalentes entre medicamentos sustituidos
	 * @param con, Connection, Conexión con la fuente de datos
	 * @param numeroSolicitud, número de la solicitud,
	 * @param coleccionNumerosSustitutos, Collection, contiene el numeroSustituto-numeroOriginal
	 * @param codigoInstitucion @todo
	 * 
	 * @return ResulSet, con la lista de los medicamentos originales y sus sustitutos
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaEquivalentes (Connection con, int codSus, int codPrin ) throws SQLException;
	
	/**
	 * Inserta la info básica del despacho
	 * @param con
	 * @param usuario
	 * @param fecha
	 * @param hora
	 * @param esDirecto
	 * @param numeroSolicitud
	 * @return
	 */
	public  int insertarDespachoBasico			(	Connection con, 
																		String  usuario, 
																		boolean esDirecto,
																		int numeroSolicitud,
																		String nombrePersonaRecibe) ;
	
	/**
	 * Carga el ultimo despacho  insertado   (table= despacho))
	 * @param con
	 * @return
	 */
	public int cargarUltimoCodigoSequence(Connection con);
	
	/**
	 * Inserta el detalle del despacho
	 * @param con
	 * @param articulo
	 * @param articuloPrincipal
	 * @param cantidad
	 * @param saldoDosis 
	 * @param codigoSeq
	 * @return
	 */
	public  int insertarDetalleDespacho			(	Connection con, 
													int articulo, 
													int articuloPrincipal, 
													int cantidad,
													int codigoSequenciaDespacho,
													String lote,
													String fechaVencimiento,
													String tipoDespacho,
													String almacenConsignacion,
													String proveedorCompra,
													String proveedorCatalogo, double saldoDosis);
	
	/**
	 * @param esMedicamento 
	 * 
	 */
	public boolean validarNOPOS(Connection con, int numeroSolicitud, int codigoArticulo, boolean esMedicamento);
	
	/**
	 * 
	 */
	public boolean insertarJusNP(Connection con, int numeroSolicitud, int codigoArticulo, String usuario);

	/**
	 * Método que elimina un sustituto con despacho total 0 
	 */
	public  int eliminarSustitutoConDespachoCero(Connection con, int articulo, int numeroSolicitud);

	/**
	 * Consulta los articulos (Principales - Sustitutos) que ya han sido insertados
	 * para restringir la búsqueda en el tag
	 * @param con
	 * @return
	 */
	public Collection articulosInsertados(Connection con, int numeroSolicitud);
	
	/**
	 * Método que Actualiza el estado médico de la solicitud en (Administrada-Despachada) 
	 * y su correspondiente Número de Autorización
	 */
	public  int cambiarEstadoMedicoSolicitudYNumAutorizacion(Connection con, int estadoMedico, /*String numeroAutorizacion, */int numeroSolicitud);
	
	/**
	 * Método que one la solicitu en estado anulada cuando no se genera ningun cargo
	 * @param con
	 * @param estadoFacturacion
	 * @param numeroSolicitud
	 * @return
	 */
	public int cambiarEstadoFacturacionSolicitud(Connection con, int estadoFacturacion, int numeroSolicitud);
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean dosisConsistentesSolicitud(Connection con, int numeroSolicitud);

	/**
	 * 
	 * @param con
	 * @param codigoCentroCosto
	 * @param codigoArticulo
	 * @param unidadmedida 
	 * @param dosis
	 * @param sumarDosis
	 * @return
	 */
	public boolean actualizarDosisSobrantes(Connection con, int codigoCentroCosto, int codigoArticulo, String unidadmedida, double dosis, boolean sumarDosis);

	/**
	 * 
	 * @param con
	 * @param codigoCentroCosto
	 * @param codigoArticulo
	 * @param unidadMedida
	 * @return
	 */
	public double obtenerDosisSaldoArticulo(Connection con, int codigoCentroCosto, String codigoArticulo, String unidadMedida);
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean existeDespachocConEntregaDirPac(Connection con, int numeroSolicitud);
}