/*
 * @(#)TEIBusquedaEspecialidades.java
 * 
 * Created on 07-May-2004
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados
 * 
 * Lenguaje : Java
 * Compilador : J2SDK 1.4
 */
package util.tag;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

/**
 * Esta clase es auxiliar de <code>TagBusquedaEspecialidades</code>donde se definen las
 * <i>scripting variables</i> necesarias (nombres, codigos). Se maneja
 * con colección para conocer de antemano el número de resultados
 * encontrados (Sin tener que recorrerlo) 
 * 
 * @version 1.0, 07-May-2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class TEIBusquedaEspecialidades extends TagExtraInfo
{
	/**
	 * Define las <i>scripting variables</i> usadas en TagBusquedaEspecialidades y las
	 * pone en un arreglo de objetos <code>VariableInfo</code>
	 * @see javax.servlet.jsp.tagext.TagExtraInfo#getVariableInfo(TagData)
	 */
	public VariableInfo[] getVariableInfo(TagData data) 
	{
		return new VariableInfo[] 
		{			
			new VariableInfo("especialidades",  "java.util.ArrayList", true, VariableInfo.NESTED),
		};
	}
}
