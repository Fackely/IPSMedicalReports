/*
 * @(#)TEISelectsAdmisionUrgencias.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package util.tag;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

/**
 * Esta clase es auxiliar de <code>TagSelectsAdmisionUrgencias</code> y sirve para
 * definir las <i>scripting variables</i> usadas en dicha clase.
 *
 * @version 1.0, Mar 20, 2003
 * @author 	<a href="mailto:Sandra@PrincetonSA.com">Sandra Moya</a>, <a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>
 */

public class TEISelectsAdmisionUrgencias extends TagExtraInfo {

	/**
	 * Define las <i>scripting variables</i> usadas en TagSelectsAdmisionUrgencias y
	 * las pone en un arreglo de objetos <code>VariableInfo</code>
	 * @see javax.servlet.jsp.tagext.TagExtraInfo#getVariableInfo(TagData)
	 */
	public VariableInfo[] getVariableInfo(TagData data) {

		if (data == null); // NOP para evitar el warning "The argument data is never read"

		return new VariableInfo [] {
			new VariableInfo("codigoOrigenCnDefault", "java.lang.String", true, VariableInfo.AT_END),
			new VariableInfo("nombreOrigenCnDefault", "java.lang.String", true, VariableInfo.AT_END),
			new VariableInfo("codigoCausaExternaCnDefault", "java.lang.String", true, VariableInfo.AT_END),
			new VariableInfo("nombreCausaExternaCnDefault", "java.lang.String", true, VariableInfo.AT_END)
		};

	}

}