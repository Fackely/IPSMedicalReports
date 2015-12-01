/*
 * @(#)TEIMuestraDiagnosticoValoracionHospitalaria.java
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
 * Esta clase es auxiliar de <code>TagMuestraDiagnosticoValoracionHospitalaria</code> y sirve para
 * definir las <i>scripting variables</i> usadas en dicha clase.
 *
 * @version 1.0, Jun 05, 2003
 */

public class TEIMuestraDiagnosticoValoracionHospitalaria extends TagExtraInfo 
{

	/**
	 * Define las <i>scripting variables</i> usadas en TagValoresPorDefecto y
	 * las pone en un arreglo de objetos <code>VariableInfo</code>
	 * @see javax.servlet.jsp.tagext.TagExtraInfo#getVariableInfo(TagData)
	 */
	public VariableInfo[] getVariableInfo(TagData data) 
	{

		if (data == null); // NOP para evitar el warning "The argument data is never read"

		return new VariableInfo[] 
		{
			new VariableInfo("acronimo",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("tipoCie",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("nombre", "java.lang.String", true, VariableInfo.NESTED)
		};

	}

}
