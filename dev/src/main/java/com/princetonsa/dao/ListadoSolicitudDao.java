/*
 * @(#)ListadoSolicitudDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 * 
 *	@version 1.0, Sep 17, 2003
 */
public interface ListadoSolicitudDao 
{

	public ResultSetDecorator consultaListadoSolicitudesFarmaciaRestriccionMedico (Connection con, String codigoTipoIdentificacionPaciente, String numeroIdentificacionPaciente, String codigoInstitucion) throws SQLException;
	public ResultSetDecorator consultaListadoSolicitudesFarmaciaRestriccionPaciente (Connection con, String codigoTipoIdentificacionPaciente, String numeroIdentificacionPaciente) throws SQLException;
	public ResultSetDecorator consultaListadoSolicitudesFarmaciaRestriccionUsuario (Connection con, String codigoTipoIdentificacionPaciente, String numeroIdentificacionPaciente, String codigoInstitucion) throws SQLException;
}
