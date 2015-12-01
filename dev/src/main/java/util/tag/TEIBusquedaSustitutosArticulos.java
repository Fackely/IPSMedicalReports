/*
 * @(#)TEIBusquedaSustitutosArticulos.java
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
 * Esta clase es auxiliar de <code>TagBusquedaSustitutosArticulos</code> donde se definen
 * las <i>scripting variables</i> necesarias
 * 
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Rios
 * @version 1.0, Septiembre 2, 2004
 */
public class TEIBusquedaSustitutosArticulos extends TagExtraInfo
{
	/**
	 * Define las <i>scripting variables</i> usadas en TagBusquedaSustitutosArticulos y las
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
