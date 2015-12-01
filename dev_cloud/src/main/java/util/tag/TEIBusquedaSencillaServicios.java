/*
 * @(#)TEIBusquedaSencillaServicios.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_03
 *
 */

package util.tag;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

/**
 * Esta clase es auxiliar de <code>TagBusquedaSencillaServicios</code>
 * donde se definen las <i>scripting variables</i> necesarias 
 * (nombres, codigos). Se maneja con colección para conocer de 
 * antemano el número de resultados encontrados (Sin tener 
 * que recorrerlo)
 *
 *	@version 1.0, May 7, 2004
 */
public class TEIBusquedaSencillaServicios extends TagExtraInfo 
{

	/**
	 * Define las <i>scripting variables</i> usadas en TagBusquedaSencillaServicios y las
	 * pone en un arreglo de objetos <code>VariableInfo</code>
	 * @see javax.servlet.jsp.tagext.TagExtraInfo#getVariableInfo(TagData)
	 */
	public VariableInfo[] getVariableInfo(TagData data) {

		if (data == null); // NOP para evitar el warning "The argument data is never read"

		return new VariableInfo[] {
			
			new VariableInfo("codigos",  "java.util.Vector", true, VariableInfo.NESTED),
			new VariableInfo("nombres", "java.util.Vector", true, VariableInfo.NESTED),
			new VariableInfo("codigosEspecialidades", "java.util.Vector", true, VariableInfo.NESTED),
			new VariableInfo("especialidades", "java.util.Vector", true, VariableInfo.NESTED),
			new VariableInfo("activos", "java.util.Vector", true, VariableInfo.NESTED),
			new VariableInfo("tipoServicio", "java.util.Vector", true, VariableInfo.NESTED),
		};

	}

}
