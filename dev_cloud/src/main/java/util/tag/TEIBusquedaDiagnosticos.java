/*
 * @(#)TEIBusquedaDiagnosticos.java
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
 * Esta clase es auxiliar de <code>TagBusquedaProcedimientosClinicos</code> 
 * y <code>TagBusquedaDiagnosticos</code>donde se definen las
 * <i>scripting variables</i> necesarias (nombres, codigos). Se maneja
 * con colección para conocer de antemano el número de resultados
 * encontrados (Sin tener que recorrerlo)
 *
 * @version 1.0, Mar 6, 2003
 * @author <a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s
 * &oacute;pez P.</a>
 * @author <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho
 * P.</a>,
 * @author <a href="mailto:Diego@PrincetonSA.com">Diego Ram&iacute;rez </a>
 */

public class TEIBusquedaDiagnosticos extends TagExtraInfo {

	/**
	 * Define las <i>scripting variables</i> usadas en TagBusquedaProcedimientosClinicos y las
	 * pone en un arreglo de objetos <code>VariableInfo</code>
	 * @see javax.servlet.jsp.tagext.TagExtraInfo#getVariableInfo(TagData)
	 */
	public VariableInfo[] getVariableInfo(TagData data) {

		if (data == null); // NOP para evitar el warning "The argument data is never read"

		return new VariableInfo[] {
			new VariableInfo("errorCie", "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("codigos",  "java.util.Vector", true, VariableInfo.NESTED),
			new VariableInfo("nombres", "java.util.Vector", true, VariableInfo.NESTED),
			new VariableInfo("fichas", "java.util.Vector", true, VariableInfo.NESTED),
		};

	}

}
