/*
 * @(#)TEIMuestraCamas.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
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
 * Este tag hace parte de la evolución de TagMuestraOpciones en su versión
 * Struts. Se define una sola <i>scripting variable</i> para tener toda la
 * flexibilidad necesaria (resultados). Estos resultados estan en la forma res1-
 * res2 ....
 *
 * @version 1.0, Mar 6, 2003
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar L&oacute;pez</a>
 * @author 	<a href="mailto:Camilo@PrincetonSA.com">Camilo Camacho</a>
 */

public class TEIMuestraEnfermerasActivas extends TagExtraInfo {

	/**
	 * Define las <i>scripting variables</i> usadas en TagMuestraCamas y las
	 * pone en un arreglo de objetos <code>VariableInfo</code>
	 * @see javax.servlet.jsp.tagext.TagExtraInfo#getVariableInfo(TagData)
	 */
	public VariableInfo[] getVariableInfo(TagData data) {

		if (data == null); // NOP para evitar el warning "The argument data is never read"

		return new VariableInfo[] 
		{
			new VariableInfo("resultados",  "java.util.Vector", true, VariableInfo.NESTED),
		};

	}

}