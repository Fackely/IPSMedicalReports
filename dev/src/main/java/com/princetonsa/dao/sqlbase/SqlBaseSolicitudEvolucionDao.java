/*
 * @(#)SqlBaseSolicitudEvolucionDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_03
 *
 */

package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import util.ConstantesBD;

/**
 * Esta clase implementa la funcionalidad com�n a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL est�ndar. M�todos particulares a Solicitud Evoluci�n
 *
 *	@version 1.0, May 10, 2004
 */
public class SqlBaseSolicitudEvolucionDao 
{
    /**
	 * Logger para manejar los errores que se presenten en esta
	 * evoluci�n 
	 */
	private static Logger logger = Logger.getLogger(SqlBaseSolicitudEvolucionDao.class);
    
	/** 
	 * Cadena constante con la sentencia necesaria para insertar 
	 * una solicitud de evoluci�n 
	 */
	private static String insertarSolicitudEvolucionStr="INSERT into solicitudes_evolucion (numero_solicitud, evolucion) VALUES (?,?)";
	
	/** 
	 * Cadena constante con la sentencia necesaria para responder 
	 * una solicitud de evoluci�n 
	 */
	private static String responderSolicitudEvolucionStr="UPDATE solicitudes set estado_historia_clinica=" + ConstantesBD.codigoEstadoHCRespondida + " where numero_solicitud=?";
	
	/** 
	 * Cadena constante con la sentencia necesaria para interpretar
	 * todas las solicitudes de evoluci�n dada la valoraci�n
	 * a la que est� asociada
	 */
	private static String interpretarSolicitudesEvolucionDadaValoracionStr="UPDATE solicitudes set estado_historia_clinica =" + ConstantesBD.codigoEstadoHCInterpretada +" where numero_solicitud IN (SELECT numero_solicitud from solicitudes_evolucion solev, evoluciones ev where solev.evolucion=ev.codigo and ev.valoracion=?)";
	
	/**
	 * Cadena para interpretar las solicitudes de evol  dado el cod de val (numero_sol) y el estado HC
	 */
	private static String interpretarSolicitudesEvolucionDadaValoracionYEstadoHCStr="UPDATE solicitudes SET estado_historia_clinica= "+ConstantesBD.codigoEstadoHCInterpretada+ " WHERE estado_historia_clinica="+ConstantesBD.codigoEstadoHCRespondida+" AND numero_solicitud IN (SELECT numero_solicitud AS numeroSolicitud FROM solicitudes_evolucion  WHERE evolucion IN (SELECT codigo FROM evoluciones WHERE valoracion=?))";
	
	/**
	 * Cadena constante con la sentencia necesaria para interpretar
	 * la solicitud de evoluci�n dado el c�digo de la evoluci�n
	 */
	private static String interpretarSolicitudesEvolucionDadaEvolucionStr="UPDATE solicitudes set estado_historia_clinica =" + ConstantesBD.codigoEstadoHCInterpretada +" where numero_solicitud IN (select numero_solicitud from solicitudes_evolucion where evolucion=?)";
	
	/**
	 * Implementaci�n del m�todo para insertar una solicitud de 
	 * evoluci�n en una BD Gen�rica
	 *
	 * @see com.princetonsa.dao.SolicitudEvolucionDao#insertarSolicitudEvolucion (Connection , int , int ) throws SQLException
	 */
	public static int insertarSolicitudEvolucion (Connection con, int codigoSolicitud, int codigoEvolucion) throws SQLException
	{
		int resp0=0, resp1=0;
		PreparedStatementDecorator instertarSolicitudEvolucionStatement= new PreparedStatementDecorator(con.prepareStatement(insertarSolicitudEvolucionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		instertarSolicitudEvolucionStatement.setInt(1, codigoSolicitud);
		instertarSolicitudEvolucionStatement.setInt(2, codigoEvolucion);
		resp0=instertarSolicitudEvolucionStatement.executeUpdate();

		PreparedStatementDecorator responderSolicitudEvolucionStatement= new PreparedStatementDecorator(con.prepareStatement(responderSolicitudEvolucionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		responderSolicitudEvolucionStatement.setInt(1, codigoSolicitud);
		resp1=responderSolicitudEvolucionStatement.executeUpdate();
		
		if (resp0<=0 ||resp1<=0)
		{
			return 0;
		}
		else
		{
			return 1;
		}
	}
	
	/**
	 * Implementaci�n del m�todo para interpretar todas las solicitudes 
	 * de evoluci�n asociadas a una valoraci�n particular en una BD 
	 * Gen�rica
	 *
	 * @see com.princetonsa.dao.SolicitudEvolucionDao#interpretarSolicitudesEvolucionDadaValoracion(Connection , int ) throws SQLException
	 */
	public static int interpretarSolicitudesEvolucionDadaValoracion(Connection con, int codigoValoracionAsociada) throws SQLException
	{
		PreparedStatementDecorator interpretarSolicitudesEvolucionDadaValoracionStatement= new PreparedStatementDecorator(con.prepareStatement(interpretarSolicitudesEvolucionDadaValoracionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		interpretarSolicitudesEvolucionDadaValoracionStatement.setInt(1, codigoValoracionAsociada);
		return interpretarSolicitudesEvolucionDadaValoracionStatement.executeUpdate();
	}
	
	/**
	 * Implementaci�n del m�todo para interpretar todas las solicitudes 
	 * de evoluci�n asociadas a una valoraci�n particular y un estado de HC en una BD 
	 * Gen�rica
	 *
	 * @see com.princetonsa.dao.SolicitudEvolucionDao#interpretarSolicitudesEvolucionDadaValoracion(Connection , int ) throws SQLException
	 */
	public static int interpretarSolicitudesEvolucionDadaValoracionYEstadoHC(Connection con, int codigoValoracionAsociada) 
	{
	    try
	    {
			PreparedStatementDecorator interpretarSolicitudesEvolucionDadaValoracionStatement= new PreparedStatementDecorator(con.prepareStatement(interpretarSolicitudesEvolucionDadaValoracionYEstadoHCStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			interpretarSolicitudesEvolucionDadaValoracionStatement.setInt(1, codigoValoracionAsociada);
			return interpretarSolicitudesEvolucionDadaValoracionStatement.executeUpdate();
	    }
	    catch(SQLException e)
	    {
	        logger.error("Error cambiando los estados de la solicitud n�mero "+codigoValoracionAsociada+" error-->"+e.toString());
	        return ConstantesBD.codigoNuncaValido;
	    }
	}
	

	/**
	 * Implementaci�n del m�todo para interpretar todas las solicitudes 
	 * de evoluci�n asociadas a una evoluci�n particular en una BD 
	 * Gen�rica
	 *
	 * @see com.princetonsa.dao.SolicitudEvolucionDao#interpretarSolicitudesEvolucionDadaEvolucion(Connection , int ) throws SQLException
	 */
	public static int interpretarSolicitudesEvolucionDadaEvolucion(Connection con, int codigoEvolucion) throws SQLException
	{
		PreparedStatementDecorator interpretarSolicitudesEvolucionDadaEvolucionStatement= new PreparedStatementDecorator(con.prepareStatement(interpretarSolicitudesEvolucionDadaEvolucionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		interpretarSolicitudesEvolucionDadaEvolucionStatement.setInt(1, codigoEvolucion);
		return interpretarSolicitudesEvolucionDadaEvolucionStatement.executeUpdate();
	}

}