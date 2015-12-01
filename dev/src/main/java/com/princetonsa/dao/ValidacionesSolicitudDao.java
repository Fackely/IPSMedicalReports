/*
 * @(#)ValidacionesSolicitudDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_03
 *
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Clase que define los métodos necesarios que debe tener
 * cualquier implementación d
 * 
 *	@version 1.0, Feb 14, 2004
 */
public interface ValidacionesSolicitudDao
{
	/**
	 * Método que carga los datos mínimos necesarios
	 * para revisar los permisos de una solicitud
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param numeroSolicitud Número de la solicitud
	 * a revisar
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarDatosBasicos (Connection con, int numeroSolicitud) throws SQLException;
	
	/**
	 * Método que para una solicitud de interconsulta, carga
	 * el código de la manejo de la misma
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param numeroSolicitud Número de la solicitud de
	 * interconsulta
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarCodigoManejoInterconsulta (Connection con, int numeroSolicitud) throws SQLException;
	
	/**
	 * Método que revisa si la solicitud está anulada
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param numeroSolicitud Número de la solicitud
	 * @return
	 * @throws SQLException
	 */
	public boolean estaSolicitudAnulada (Connection con, int numeroSolicitud) throws SQLException;
	
	
	/**
	 * Método que dice si la solicitud es de tipo otros o no
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param numeroSolicitud Número de la solicitud
	 * @param codigoTipoSolicitud
	 * @return
	 * @throws SQLException
	 */
	public boolean esTipoOtros (Connection con, int numeroSolicitud, int codigoTipoSolicitud) throws SQLException;
	
	/**
	 * Método que permite saber sila respuesta de una 
	 * interconsulta es pedíatrica o no
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param numeroSolicitud Número de la solicitud
	 * @return
	 * @throws SQLException
	 */
	public boolean respuestaInterconsultaEsPediatrica (Connection con, int numeroSolicitud) throws SQLException;
	
    /**
     * Método que permite saber sila respuesta de una 
     * interconsulta es de odontología o no
     * 
     * @param con Conexión con la fuente de datos
     * @param numeroSolicitud Número de la solicitud
     * @return
     * @throws SQLException
     */
    public boolean respuestaInterconsultaEsOdontologia (Connection con, int numeroSolicitud) throws SQLException;

    /**
     * Método que permite saber sila respuesta de una 
     * interconsulta es de oftalmología o no
     * 
     * @param con Conexión con la fuente de datos
     * @param numeroSolicitud Número de la solicitud
     * @return true o false
     */
    public boolean respuestaInterconsultaEsOftalmologica(Connection con, int numeroSolicitud);

	/**
	 * Método que revisa si una solicitud generó pedido
	 * de cambio de médico tratante
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param numeroSolicitud Número de la solicitud
	 * @return
	 * @throws SQLException
	 */
	public boolean tieneSolicitudCambioTratante (Connection con, int numeroSolicitud) ;
	
	/**
	 * Método que revisa si una solicitud de consulta externa
	 * puede ser  respondida (flujo diferente al resto)
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param numeroSolicitud Número de la solicitud
	 * @param codigoMedicoQuiereResponder Código del 
	 * médico que desea responder la solicitud
	 * @return
	 * @throws SQLException
	 */
	public boolean puedoResponderSolicitudCasoConsultaExterna(Connection con, int numeroSolicitud, int codigoMedicoQuiereResponder) throws SQLException;
	public boolean puedoModificarSolicitudCasoConsultaExterna(Connection con, int numeroSolicitud) throws SQLException;
	
	/**
	 * Metodo que retorna un boolean indicando si la solicitud tiene despachos.
	 * @param con, Conexion
	 * @param numeroSolicitud, Numero de solicitud
	 * @return boolena.
	 */
	public boolean solicitudMedicamentosTieneDespachos(Connection con, int numeroSolicitud);
	
	/**
	 * Metodo que retorna un boolena indicando si la solicitud esta en estado facturacion pendiente; 
	 * de Medicamentos.
	 * @param con, Conexion
	 * @param nuemoroSolicitud, Numero de solicitud que se desa anular o modificar.
	 * @return boolena, true se puede a/m, false no se puede
	 */
	public boolean solicitudEstadoFacturacionPendiente(Connection con,int nuemoroSolicitud);
	
	/**
	 * Metodo que retorna un boolena indicando si la solicitud esta en estado solicitado; 
	 * de Medicamentos.
	 * @param con, Conexion
	 * @param nuemoroSolicitud, Numero de solicitud que se desa anular o modificar.
	 * @return boolean, true se puede a/m, false no se puede
	 */
	public boolean solicitudEstadoSolicitada(Connection con,int nuemoroSolicitud);

	/**
	 * @param con
	 * @param numeroSolicitud
	 * @param esEvolucion
	 * @return Vector de String
	 * Posición 0 el nit
	 * Posición 1 el nombre
	 * Posición 2 el código
	 */
	public String[] obtenerInstitucion(Connection con, int numeroSolicitud, boolean esEvolucion);
	
	/**
	 * Método para verificar si el médico puede o no hacer solicitud de procedimientos
	 * para un paciente con vía de ingreso "Consulta Externa"
	 * @param con
	 * @param codigoCuenta
	 * @param codigoMedico
	 * @return
	 */
	public boolean tieneCitasAtendidas(Connection con, int codigoCuenta, int codigoMedico);

	/**
	 * Metodo que retorna un boolena indicando si la solicitud esta en estado despachada;
	 * @param con
	 * @param numeroSolicitud
	 * @return boolean, true se el estado es despachada
	 */
	public boolean solicitudEstadoDespachada(Connection con, int numeroSolicitud);

}
