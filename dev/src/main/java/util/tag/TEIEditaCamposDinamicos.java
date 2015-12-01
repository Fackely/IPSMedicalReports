/*
 * @(#)TEIEditaCamposDinamicos.java
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
 * Esta clase es auxiliar de <code>TagEditaCamposDinamicos</code> donde se definen
 * las <i>scripting variables</i> necesarias .  Para evitar que en los jsp hayan
 * enumerations y fors, se trabajaran con variables individuales, pero iterando
 * en el Tag. Por esta misma razon las variables no tienen nombres de conjunto
 * Ej codigos sino de elementos individuales, codigo
 * 
 *
 * @version 1.0, Jul 3, 2003
 */
public class TEIEditaCamposDinamicos extends TagExtraInfo 
{

	/**
	 * Define las <i>scripting variables</i> usadas en
	 * TagMuestraIngresosEpicrisis y las pone en un arreglo de objetos
	 * <code>VariableInfo</code>
	 * @see javax.servlet.jsp.tagext.TagExtraInfo#getVariableInfo(TagData)
	 */
	public VariableInfo[] getVariableInfo(TagData data) 
	{

		if (data == null); // NOP para evitar el warning "The argument data is never read"

		return new VariableInfo[] 
		{
			
			new VariableInfo("tipoCampo",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("ordenCampo",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("nombreCampo",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("seccionCampo",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("funcionalidadCampo",  "java.lang.String", true, VariableInfo.NESTED)
			

		};

	}

}
