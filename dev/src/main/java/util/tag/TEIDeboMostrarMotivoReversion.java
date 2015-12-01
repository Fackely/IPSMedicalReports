/*
 * @(#)TEIDeboMostrarMotivoReversion.java
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
 * Esta clase es auxiliar de <code>TagMuestraAdmisiones</code> donde se definen
 * las <i>scripting variables</i> necesarias (motivoReversionEgreso,
 * motivoReversionEgresoBoolean).  
 * 
 *	@version 1.0, Aug 29, 2003
 */
public class TEIDeboMostrarMotivoReversion extends TagExtraInfo 
{

	/**
	 * Define las <i>scripting variables</i> usadas en
	 * TagDeboMostrarMotivoReversion y las pone en un arreglo de objetos
	 * <code>VariableInfo</code>
	 * @see javax.servlet.jsp.tagext.TagExtraInfo#getVariableInfo(TagData)
	 */
	public VariableInfo[] getVariableInfo(TagData data) 
	{

		if (data == null); // NOP para evitar el warning "The argument data is never read"

		return new VariableInfo[] 
		{
			
			new VariableInfo("motivoReversionEgresoBoolean",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("motivoReversionEgreso",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("fechaReversionEgreso",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("horaReversionEgreso",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("creadorReversionEgreso",  "java.lang.String", true, VariableInfo.NESTED),
			
		};

	}

}
