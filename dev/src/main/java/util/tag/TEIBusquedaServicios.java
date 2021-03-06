/*
 * @(#)TEIBusquedaServicios.java
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
 * Esta clase es auxiliar de <code>TagBusquedaServicios</code>
 * donde se definen las <i>scripting variables</i> necesarias 
 * (nombres, codigos). Se maneja con colecci?n para conocer de 
 * antemano el n?mero de resultados encontrados (Sin tener 
 * que recorrerlo)
 *
 *	@version 1.0, Feb 13, 2004
 */
public class TEIBusquedaServicios extends TagExtraInfo 
{
	/**
	 * Define las <i>scripting variables</i> usadas en TagBusquedaServicios y las
	 * pone en un arreglo de objetos <code>VariableInfo</code>
	 * @see javax.servlet.jsp.tagext.TagExtraInfo#getVariableInfo(TagData)
	 */
	public VariableInfo[] getVariableInfo(TagData data) {

		if (data == null); // NOP para evitar el warning "The argument data is never read"

		return new VariableInfo[] {
			
			new VariableInfo("esPos",  "java.util.Vector", true, VariableInfo.NESTED),
			new VariableInfo("esExcepcion",  "java.util.Vector", true, VariableInfo.NESTED),
			new VariableInfo("codigos",  "java.util.Vector", true, VariableInfo.NESTED),
			new VariableInfo("codigosPropietarios",  "java.util.Vector", true, VariableInfo.NESTED),
			new VariableInfo("nombres", "java.util.Vector", true, VariableInfo.NESTED),
			new VariableInfo("codigosEspecialidades", "java.util.Vector", true, VariableInfo.NESTED),
			new VariableInfo("especialidades", "java.util.Vector", true, VariableInfo.NESTED),
			new VariableInfo("formularios", "java.util.Vector", true, VariableInfo.NESTED),
			new VariableInfo("solicitadas", "java.util.Vector", true, VariableInfo.NESTED),
			new VariableInfo("gruposServicios", "java.util.Vector", true, VariableInfo.NESTED),
			new VariableInfo("tomaMuestra", "java.util.Vector", true, VariableInfo.NESTED),
			new VariableInfo("repuestaMultiple", "java.util.Vector", true, VariableInfo.NESTED),
			new VariableInfo("cobertura", "java.util.Vector", true, VariableInfo.NESTED),
			new VariableInfo("subcuenta", "java.util.Vector", true, VariableInfo.NESTED),
			new VariableInfo("justificar", "java.util.Vector", true, VariableInfo.NESTED),
			new VariableInfo("portatil", "java.util.Vector", true, VariableInfo.NESTED),
			new VariableInfo("grupoMultiple", "java.util.Vector", true, VariableInfo.NESTED),
			new VariableInfo("numResultadosTotal", "java.lang.Integer", true, VariableInfo.NESTED)

		};

	}

}
