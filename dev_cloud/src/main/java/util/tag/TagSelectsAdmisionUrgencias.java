/*
 * @(#)TagSelectsAdmisionUrgencias.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package util.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import util.UtilidadTexto;

/**
 * Esta clase implementa un tag que conserva el estado de los selects elegidos en la página de
 * ingreso de admisiones por urgencias, aún después de hacer un reload. Define
 * las <i>scripting variables</i> : <b>codigoOrigenCnDefault</b>, <b>nombreOrigenCnDefault</b>,
 * <b>codigoCausaExternaCnDefault</b>, <b>nombreCausaExternaCnDefault</b>; con
 * un scope que permite que sean asequibles en toda la página. Se apoya en la
 * clase <code>TEISelectsAdmisionUrgencias.java</code>.
 *
 * @version 1.0, Mar 20, 2003
 * @author 	<a href="mailto:Sandra@PrincetonSA.com">Sandra Moya</a>, <a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>
 */

public class TagSelectsAdmisionUrgencias extends TagSupport {

	/**
	 * Ponemos en pageContext las <i>scripting variables</i> con el estado de los
	 * selects elegidos por el usuario en la página de admisiones por urgencias.
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doEndTag() throws JspTagException {

		String [] resultados;
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

		// datos del select 'origen_cn'
		String codigoOrigenCnDefault, nombreOrigenCnDefault;
		String origenCnDefault = request.getParameter("origen_cn");
		if (origenCnDefault != null) {
			resultados = UtilidadTexto.separarNombresDeCodigos(origenCnDefault, 1);
			codigoOrigenCnDefault = resultados[0];
			nombreOrigenCnDefault = resultados[1];
		}
		else {
			codigoOrigenCnDefault = "1";
			nombreOrigenCnDefault = "Urgencias";
		}

		// datos del select 'causaExterna_cn'
		String codigoCausaExternaCnDefault, nombreCausaExternaCnDefault;
		String causaExternaCnDefault = request.getParameter("causaExterna_cn");
		codigoCausaExternaCnDefault = "0";
		nombreCausaExternaCnDefault = "Seleccione Causa Externa";
		
		if (origenCnDefault != null) {
			if(causaExternaCnDefault != null){				
				resultados = UtilidadTexto.separarNombresDeCodigos(causaExternaCnDefault, 1);		
				codigoCausaExternaCnDefault = resultados[0];
				nombreCausaExternaCnDefault = resultados[1];
			}
		}

		// Ponemos en pageContext nuestras scripting variables
		pageContext.setAttribute("codigoOrigenCnDefault", codigoOrigenCnDefault);
		pageContext.setAttribute("nombreOrigenCnDefault", nombreOrigenCnDefault);
		pageContext.setAttribute("codigoCausaExternaCnDefault", codigoCausaExternaCnDefault);
		pageContext.setAttribute("nombreCausaExternaCnDefault", nombreCausaExternaCnDefault);

		return EVAL_PAGE;

	}

}