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
 * Esta clase es auxiliar de <code>TagMuestraCamas</code> donde se definen las
 * <i>scripting variables</i> necesarias (nombre, codigo, centroCosto).
 *
 * @version 1.0, Mar 6, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho
 */

public class TEIMuestraCamas extends TagExtraInfo {

	/**
	 * Define las <i>scripting variables</i> usadas en TagMuestraCamas y las
	 * pone en un arreglo de objetos <code>VariableInfo</code>
	 * @see javax.servlet.jsp.tagext.TagExtraInfo#getVariableInfo(TagData)
	 */
	public VariableInfo[] getVariableInfo(TagData data) 
	{

		if (data == null); // NOP para evitar el warning "The argument data is never read"

		return new VariableInfo[] 
		{
			
			new VariableInfo("codigos",  "java.util.Vector", true, VariableInfo.NESTED),
			new VariableInfo("nombres", "java.util.Vector", true, VariableInfo.NESTED),
			new VariableInfo("centrosCosto",  "java.util.Vector", true, VariableInfo.NESTED),
			new VariableInfo("estadosCama", "java.util.Vector", true, VariableInfo.NESTED),
			new VariableInfo("nombresCentroCosto", "java.util.Vector", true, VariableInfo.NESTED),
			new VariableInfo("tiposUsuario", "java.util.Vector", true, VariableInfo.NESTED),
			new VariableInfo("descripciones", "java.util.Vector", true, VariableInfo.NESTED),
			new VariableInfo("habitaciones", "java.util.Vector", true, VariableInfo.NESTED),
			new VariableInfo("esUci", "java.util.Vector", true, VariableInfo.NESTED),
			new VariableInfo("tipoMonitoreo", "java.util.Vector", true, VariableInfo.NESTED)
			
		};

	}

}