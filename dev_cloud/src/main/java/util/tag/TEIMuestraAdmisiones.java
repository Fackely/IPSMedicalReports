/*
 * @(#)TEIMuestraAdmisiones.java
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
 * Esta clase es auxiliar de <code>TagMuestraAdmisiones</code> donde se definen
 * las <i>scripting variables</i> necesarias (fechaIngreso, horaIngreso,
 * fechaEgreso, horaEgreso, viaIngreso).  Para evitar que en los jsp hayan
 * enumerations y fors, se trabajaran con variables individuales, pero iterando
 * en el Tag. Por esta misma razon las variables no tienen nombres de conjunto
 * Ej codigos sino de elementos individuales, codigo
 * 
 * Este Tag funciona tanto para Admisiones Hospitalarias como para Admisiones
 * Urgencias y solo muestra la información básica de las mismas, de acuerdo a
 * los requerimientos entregados
 *
 * @version 1.0, Mar 27, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho
 */
public class TEIMuestraAdmisiones extends TagExtraInfo 
{

	/**
	 * Define las <i>scripting variables</i> usadas en
	 * TagMuestraAdmisiones y las pone en un arreglo de objetos
	 * <code>VariableInfo</code>
	 * @see javax.servlet.jsp.tagext.TagExtraInfo#getVariableInfo(TagData)
	 */
	public VariableInfo[] getVariableInfo(TagData data) 
	{

		if (data == null); // NOP para evitar el warning "The argument data is never read"

		return new VariableInfo[] 
		{
			
			new VariableInfo("codigo",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("fechaIngreso",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("horaIngreso",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("fechaEgreso",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("horaEgreso",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("estado",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("codigoCuenta",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("viaIngreso", "java.lang.String", true, VariableInfo.NESTED)

		};

	}

}
