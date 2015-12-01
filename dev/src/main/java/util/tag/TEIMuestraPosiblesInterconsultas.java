/*
 * @(#)TEIMuestraPosiblesInterconsultas.java
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
 * Esta clase es auxiliar de <code>TagMuestraPosiblesInterconsultas</code>
 * donde se define la <i>scripting variable</i> necesaria (resultados).
 *
 * @version 1.0, May 23, 2003
 */
public class TEIMuestraPosiblesInterconsultas extends TagExtraInfo 
{

	/**
	 * Define la <i>scripting variable</i> usada en
	 * TagMuestraPosiblesInterconsultas y lo pone en un arreglo de objetos
	 * <code>VariableInfo</code>
	 * @see javax.servlet.jsp.tagext.TagExtraInfo#getVariableInfo(TagData)
	 */
	public VariableInfo[] getVariableInfo(TagData data) 
	{
		if (data == null); // NOP para evitar el warning "The argument data is never read"

		return new VariableInfo[] {
			
			new VariableInfo("resultados",  "java.util.Vector", true, VariableInfo.NESTED),
		};

	}

}

