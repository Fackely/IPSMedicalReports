/**
 * 
 */
package com.servinte.axioma.vista.helper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

/**
 * Ayudante que contiene funcionalidades de DWR 
 * {@link http://directwebremoting.org/dwr/index.html}.
 * Funciona unicamente con objetos los cuales su ciclo de
 * vida esta administrado por DWR.
 * 
 * @author Fernando Ocampo
 */
public final class DWRHelper {
	
	private DWRHelper() {
	}

	/**
	 * Retorna el contexto Web asociado al hilo que ejecute 
	 * este m&eacute;todo.
	 * 
	 * @return contexto Web.
	 */
    public static WebContext getContextoWeb() {
    	return WebContextFactory.get();
    }

    /**
     * Retorna la petici&oacute;n HTTP a partir del context Web.
     * 
     * @return petici&oacute;n HTTP
     */
    public static HttpServletRequest getHTTPRequest() {
    	return getContextoWeb().getHttpServletRequest();
    }

    /**
     * Retorna la respuesta HTTP a partir del context Web.
     * 
     * @return respuesta HTTP
     */
    public static HttpServletResponse getHTTPResponse() {
    	return getContextoWeb().getHttpServletResponse();
    }

    /**
     * Retorna la sesi&oacute;n HTTP a partir del context Web.
     * 
     * @param debeCrearse Indica si la sesi&oacute;n debe
     * crearse para el caso en que no haya. True indica que si
     * debe crearse para el caso que no exista.
     * 
     * @return sesi&oacute;n HTTP.
     */
    public static HttpSession getHTTPSession(boolean debeCrearse) {
    	return getHTTPRequest().getSession(debeCrearse);
    }

    /**
     * Agrega un atributo en los atributos de {@link HttpServletRequest}
     * 
     * @param llave Llave del atributo.
     * @param objeto Valor del atributo.
     */
    public static void agregarAtributoARequest(String llave, Object objeto) {
    	getHTTPRequest().setAttribute(llave, objeto);
    }

    /**
     * Agrega un atributo en los atributos de {@link HttpSession}
     * 
     * @param llave Llave del atributo.
     * @param objeto Valor del atributo.
     */
    public static void agregarAtributoASesion(String llave, Object objeto) {
    	getHTTPSession(true).setAttribute(llave, objeto);
    }

    /**
     * Retorna un atributo de sesi&oacute;n de acuerdo a la llave pasada
     * c&oacute;mo parametro.
     * 
     * @param llave Identificante del atributo buscado.
     * 
     * @return atributo de sesi&oacute;n relacionado a la llave dada.
     */
    public static Object obtenerAtributoDeSesion(String llave) {
    	Object atributo = null;
    	if(getHTTPSession(false) != null) {
    		atributo = getHTTPSession(false).getAttribute(llave);
    	}
    	return atributo;
    }

    public static void removerAtributoDeSesion(String llave) {
    	
    }
}
