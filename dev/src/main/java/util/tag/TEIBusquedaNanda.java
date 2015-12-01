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
 * 
 * @author Juan David Ramírez
 * 
 * Tag para buscar los diagnosticos nanda
 * CopyRight Princeton S.A.
 * 22/02/2006
 */
public class TEIBusquedaNanda extends TagExtraInfo {

	/**
	 * Método que pone los resultados del tag n el JSP
	 */
	public VariableInfo[] getVariableInfo(TagData data)
	{
		return new VariableInfo[] 
		{
			new VariableInfo("resultados",  "java.util.Vector", true, VariableInfo.NESTED),
		};
	}
}