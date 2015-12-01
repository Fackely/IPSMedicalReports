/*
 * @(#)TEIMuestraDatosSalidaEvolucion.java
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
 * Esta clase es auxiliar de <code>TagMuestraDatosSalidaEvolucion</code> donde se definen
 * las <i>scripting variables</i> necesarias (estadoSalida, acronimoMuerte
 * diagnosticoMuerte, tipoCIEMuerte, destinoSalida).  Para evitar que en los jsp hayan
 * enumerations y fors, se trabajaran con variables individuales, pero iterando
 * en el Tag. Por esta misma razon las variables no tienen nombres de conjunto
 * Ej codigos sino de elementos individuales, codigo
 * 
 *	@version 1.0, Aug 14, 2003
 */
public class TEIMuestraDatosSalidaEvolucion extends TagExtraInfo
{

	/**
	 * Define las <i>scripting variables</i> usadas en
	 * TagMuestraDatosSalidaEvolucion y las pone en un arreglo de objetos
	 * <code>VariableInfo</code>
	 * @see javax.servlet.jsp.tagext.TagExtraInfo#getVariableInfo(TagData)
	 */
	public VariableInfo[] getVariableInfo(TagData data) 
	{

		if (data == null); // NOP para evitar el warning "The argument data is never read"

		return new VariableInfo[] 
		{
			
			new VariableInfo("tieneEgresoTag",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("estadoSalidaTag",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("acronimoMuerteTag",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("tipoCIEMuerteTag",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("diagnosticoMuerteTag",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("destinoSalidaTag",  "java.lang.String", true, VariableInfo.NESTED),
			

		};

	}
}
