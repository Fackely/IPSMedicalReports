/*
 * @(#)ValidacionesSolicitudDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_03
 *
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import com.princetonsa.dao.ValidacionesSolicitudDao;
import com.princetonsa.dao.sqlbase.SqlBaseValidacionesSolicitudDao;

/**
 * Clase encargada de manejar la persistencia del objeto
 * ValidacionesSolicitud para la implementación en 
 * Postgresql 
 * 
 *	@version 1.0, Feb 22, 2004
 */
public class PostgresqlValidacionesSolicitudDao implements ValidacionesSolicitudDao
{
		
	/**
	 * Implementación del método que carga los datos mínimos necesarios 
	 * para conocer los permisos de acceso a una solicitud en una BD 
	 * Postgresql
	 *
	 * @see com.princetonsa.dao.ValidacionesDao#cargarDatosBasicos (Connection , int ) throws SQLException
	 */
	public ResultSetDecorator cargarDatosBasicos (Connection con, int numeroSolicitud) throws SQLException
	{
		return SqlBaseValidacionesSolicitudDao.cargarDatosBasicos(con,numeroSolicitud);
	}
	
	/**
	 * Implementación del método que carga el código de manejo de una
	 * interconsulta en una BD Postgresql
	 *
	 * @see com.princetonsa.dao.ValidacionesDao#cargarCodigoManejoInterconsulta (Connection , int ) throws SQLException
	 */
	public ResultSetDecorator cargarCodigoManejoInterconsulta (Connection con, int numeroSolicitud) throws SQLException
	{
		return SqlBaseValidacionesSolicitudDao.cargarCodigoManejoInterconsulta(con,numeroSolicitud);
	}
	
	/**
	 * Implementación del método que revisa si una solicitud esta anulada
	 * en una BD Postgresql
	 *
	 * @see com.princetonsa.dao.ValidacionesDao#estaSolicitudAnulada (Connection , int ) throws SQLException
	 */
	public boolean estaSolicitudAnulada (Connection con, int numeroSolicitud) throws SQLException
	{
		return SqlBaseValidacionesSolicitudDao.estaSolicitudAnulada(con,numeroSolicitud);
	}
	

	
	/**
	 * Implementación del método que revisa si la solicitud es de tipo otros
	 * en una BD Postgresql
	 *
	 * @see com.princetonsa.dao.ValidacionesDao#esTipoOtros (Connection , int , int ) throws SQLException
	 */
	public boolean esTipoOtros (Connection con, int numeroSolicitud, int codigoTipoSolicitud) throws SQLException
	{
		return SqlBaseValidacionesSolicitudDao.esTipoOtros(con,numeroSolicitud,codigoTipoSolicitud);
	}
	
	/**
	 * Implementación del método que revisa si la respuesta de una solicitud
	 * de interconsulta es pediátrica o no en una BD Postgresql
	 *
	 * @see com.princetonsa.dao.ValidacionesDao#respuestaInterconsultaEsPediatrica (Connection , int ) throws SQLException
	 */
	public boolean respuestaInterconsultaEsPediatrica (Connection con, int numeroSolicitud) throws SQLException
	{
		return SqlBaseValidacionesSolicitudDao.respuestaInterconsultaEsPediatrica(con,numeroSolicitud);
	}
	
    /**
     * Implementación del método que revisa si la respuesta de una solicitud
     * de interconsulta es de odontología o no en una BD Postgresql
     *
     * @see com.princetonsa.dao.ValidacionesDao#respuestaInterconsultaEsOdontologia (Connection , int ) throws SQLException
     */
    public boolean respuestaInterconsultaEsOdontologia (Connection con, int numeroSolicitud) throws SQLException
    {
        return SqlBaseValidacionesSolicitudDao.respuestaInterconsultaEsOdontologia(con,numeroSolicitud);
    }
    
    /**
     * Implementación del método que revisa si una solicitud generó pedido
	 * de cambio de médico tratante en una BD Postgresql
	 *
	 * @see com.princetonsa.dao.ValidacionesDao#tieneSolicitudCambioTratante (Connection , int ) throws SQLException
	 */
	public boolean tieneSolicitudCambioTratante (Connection con, int numeroSolicitud) 
	{
		return SqlBaseValidacionesSolicitudDao.tieneSolicitudCambioTratante(con,numeroSolicitud);
	}
	
	/**
	 * Implementación del método que revisa si se puede responder una
	 * solicitud en caso de ser consulta externa en una BD Postgresql
	 *
	 * @see com.princetonsa.dao.ValidacionesDao#puedoResponderSolicitudCasoConsultaExterna(Connection , int , int ) throws SQLException
	 */
	public boolean puedoResponderSolicitudCasoConsultaExterna(Connection con, int numeroSolicitud, int codigoMedicoQuiereResponder) throws SQLException
	{
		return SqlBaseValidacionesSolicitudDao.puedoResponderSolicitudCasoConsultaExterna(con,numeroSolicitud,codigoMedicoQuiereResponder);
	}
	public boolean puedoModificarSolicitudCasoConsultaExterna(Connection con, int numeroSolicitud) throws SQLException
	{
		return SqlBaseValidacionesSolicitudDao.puedoModificarSolicitudCasoConsultaExterna(con,numeroSolicitud);
	}
	
	/**
	 * Metodo que retorna un boolean indicando si tiene despachos la solicitud
	 * @param con, Conexion
	 * @param numeroSolicitud, Numero de solicitud
	 * @return boolena.
	 */
	public boolean solicitudMedicamentosTieneDespachos(Connection con, int numeroSolicitud)
	{
		return SqlBaseValidacionesSolicitudDao.solicitudMedicamentosTieneDespachos(con,numeroSolicitud);
	}
	
	/**
	 * Metodo que retorna un boolena indicando si la solicitud esta en estado facturacion pendiente; 
	 * de Medicamentos.
	 * @param con, Conexion
	 * @param nuemoroSolicitud, Numero de solicitud que se desa anular o modificar.
	 * @return boolena, true se puede a/m, false no se puede
	 */
	public boolean solicitudEstadoFacturacionPendiente(Connection con,int nuemoroSolicitud)
	{
		return SqlBaseValidacionesSolicitudDao.solicitudEstadoFacturacionPendiente(con,nuemoroSolicitud);
	}
	
	/**
	 * Metodo que retorna un boolena indicando si la solicitud esta en estado solicitado; 
	 * de Medicamentos.
	 * @param con, Conexion
	 * @param numeroSolicitud, Numero de solicitud que se desa anular o modificar.
	 * @return boolena, true se puede a/m, false no se puede
	 */
	public boolean solicitudEstadoSolicitada(Connection con,int numeroSolicitud)
	{
		return SqlBaseValidacionesSolicitudDao.solicitudEstadoSolicitada(con,numeroSolicitud);
	}
	
	/**
	 * @param con
	 * @param numeroSolicitud
	 * @return Vector de String
	 * Posición 0 el nit
	 * Posición 1 el nombre
	 * Posición 2 el código
	 */
	public String[] obtenerInstitucion(Connection con, int numeroSolicitud, boolean esEvolucion)
	{
		return SqlBaseValidacionesSolicitudDao.obtenerInstitucion(con, numeroSolicitud, esEvolucion);
	}
	
	/**
	 * Método para verificar si el médico puede o no hacer solicitud de procedimientos
	 * para un paciente con vía de ingreso "Consulta Externa"
	 * @param con
	 * @param codigoCuenta
	 * @param codigoMedico
	 * @return
	 */
	public boolean tieneCitasAtendidas(Connection con, int codigoCuenta, int codigoMedico)
	{
		return SqlBaseValidacionesSolicitudDao.tieneCitasAtendidas(con, codigoCuenta, codigoMedico);
	}

	/**
	 * Metodo que retorna un boolena indicando si la solicitud esta en estado despachada;
	 * @param con
	 * @param numeroSolicitud
	 * @return boolean, true se el estado es despachada
	 */
	public boolean solicitudEstadoDespachada(Connection con, int numeroSolicitud)
	{
		return SqlBaseValidacionesSolicitudDao.solicitudEstadoDespachada(con,numeroSolicitud);
	}

    /**
     * Implementación del método que revisa si la respuesta de una solicitud
     * de interconsulta es de oftalmología o no en una BD Postgres
     *
     * @see com.princetonsa.dao.ValidacionesDao#respuestaInterconsultaEsOftalmologica (Connection , int )
     */
    public boolean respuestaInterconsultaEsOftalmologica(Connection con, int numeroSolicitud)
    {
        return SqlBaseValidacionesSolicitudDao.respuestaInterconsultaEsOftalmologica(con,numeroSolicitud);
    }

}
