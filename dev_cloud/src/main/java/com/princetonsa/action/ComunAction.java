/*
 * @(#)ComunAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.action;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.UtilidadBD;


/**
 * Esta clase maneja m�todos comunes a los actions, para
 * evitar la replicaci�n de c�digo
 * 
 *	@version 1.0, Mar 8, 2004
 */

public class ComunAction 
{
	/**
	 * M�todo que se llama siempre que el usuario haya 
	 * dado un estado o una accion invalida (evita 
	 * repetir c�digo)
	 * 
	 * @param mapping Mapa (control) de Struts
	 * @param request request de la pagina web
	 * @param con Conexi�n con la fuente de datos
	 * @param logger Logger de la clase con la que 
	 * se est� trabajando
	 * @param mensajeDebug Mensaje que se pondr� en
	 * el logger
	 * @param codigoProximaCitaRegistrada 
	 * @return
	 * @throws Exception
	 */
	public static ActionForward accionSalirCasoError(ActionMapping mapping,HttpServletRequest request, Connection con, Logger logger, String mensajeDebug , String error, boolean esErrorPorCodigo)
	{
		if(logger!=null)
		{
			if( logger.isDebugEnabled() )
			{
				logger.debug(mensajeDebug);
			}
			else
			{
				logger.warn("Debug deshabilitado . Error encontrado " + mensajeDebug);
			}
		}
		try
		{
			if(con!=null)
				UtilidadBD.cerrarConexion(con);
			if (esErrorPorCodigo)
			{
				request.setAttribute("codigoDescripcionError", error);
			}
			else
			{
			    request.setAttribute("descripcionError", error);
			}
			
		}
		catch (SQLException e)
		{
			logger.error("Error cerrando la conexi�n "+e);
			request.setAttribute("codigoDescripcionError", "errors.problemasBd");
		}
		return mapping.findForward("paginaError");
	}
	
	/**
	 * M�todo que se llama siempre que el usuario haya 
	 * dado un estado o una acci&oacute;n inv&aacute;lida (evita 
	 * repetir c&oacute;digo)
	 * 
	 * @param mapping Mapa (control) de Struts
	 * @param request request de la pagina web
	 * @param con Conexi�n con la fuente de datos
	 * @param mensajeDebug Mensaje que se pondr� en
	 * el logger
	 * @return
	 * @throws Exception
	 */
	public static ActionForward accionSalirCasoError(ActionMapping mapping,HttpServletRequest request, Connection con, String mensajeDebug , String error, boolean esErrorPorCodigo)
	{
		Log4JManager.debug(mensajeDebug);
		try
		{
			if(con!=null)
				UtilidadBD.cerrarConexion(con);
			if (esErrorPorCodigo)
			{
				request.setAttribute("codigoDescripcionError", error);
			}
			else
			{
			    request.setAttribute("descripcionError", error);
			}
		}
		catch (SQLException e)
		{
			Log4JManager.error("Error cerrando la conexi�n "+e);
			request.setAttribute("codigoDescripcionError", "errors.problemasBd");
		}
		return mapping.findForward("paginaError");
	}
	
	
	/**
	 * M�todo que permite enviar m�ltiples errores, 
	 * a trav�s de una colecci�n de objetos 
	 * ElementoApResource
	 * 
	 * @param mapping Mapa (control) de Struts
	 * @param request request de la pagina web
	 * @param con Conexi�n con la fuente de datos
	 * @param tituloErrores Titulo con el que van
	 * a aparecer los errores
	 * @param erroresEncontrados Colecci�n de 
	 * objetos ElementoApResource a mostrar
	 * @param logger Logger de la clase con la que 
	 * se est� trabajando
	 * @return
	 * @throws Exception
	 */
	public static ActionForward envioMultiplesErrores(ActionMapping mapping,HttpServletRequest request, Connection con, String tituloErrores, Collection erroresEncontrados, Logger logger )
	{
	    try
	    {
		    request.setAttribute("conjuntoErroresObjeto", erroresEncontrados);
		    request.setAttribute("codigoDescripcionError", tituloErrores);
		    if(con!=null)
		    {	
		    	UtilidadBD.cerrarConexion(con);
		    }	
	    }
		catch (SQLException e)
		{
			logger.error("Error cerrando la conexi�n "+e);
			request.setAttribute("codigoDescripcionError", "errors.problemasBd");
		}
	    return mapping.findForward("paginaError");
	}
	

	
}
