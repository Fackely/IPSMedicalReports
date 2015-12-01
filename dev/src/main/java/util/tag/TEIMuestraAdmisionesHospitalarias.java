/*
 * @(#)TEIMuestraAdmisionesHospitalarias.java
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
 * Esta clase es auxiliar de <code>TagMuestraAdmisionesHospitalarias</code>
 * donde se definen las <i>scripting variables</i> necesarias (numeroCuenta,
 * numeroAdmision, fechaAdmision, horaAdmision, origenAdmision, medico,
 * causaExterna, diagnostico, codigodiagnostico, codigociediagnostico,
 * numeroCama, centroCosto, estadoCama, tipoUsuarioCama,  descripcionCama,
 * numeroautorizacion). Todas estas variables corresponden a los nombres, ya que
 * lo que se busca es mostrar la información de todas las admisiones en la
 * presentación esperada. Para evitar que en los jsp hayan enumerations y fors,
 * se trabajaran con variables individuales, pero iterando en el Tag. Por esta
 * misma razon las variables no tienen nombres de conjunto Ej codigos sino de
 * elementos individuales, codigo
 *
 * @version 1.0, Mar 25, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho
 */

public class TEIMuestraAdmisionesHospitalarias extends TagExtraInfo {

	/**
	 * Define las <i>scripting variables</i> usadas en
	 * TagMuestraAdmisionesHospitalarias y las pone en un arreglo de objetos
	 * <code>VariableInfo</code>
	 * @see javax.servlet.jsp.tagext.TagExtraInfo#getVariableInfo(TagData)
	 */
	public VariableInfo[] getVariableInfo(TagData data) 
	{

		if (data == null); // NOP para evitar el warning "The argument data is never read"

		return new VariableInfo[] 
		{
			
			new VariableInfo("numeroCuenta",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("numeroAdmision",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("fechaAdmision",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("horaAdmision",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("origenAdmision",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("medico",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("causaExterna",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("diagnostico",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("codigoDiagnostico",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("codigoCIEDiagnostico",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("numeroCama",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("centroCosto",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("estadoCama",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("tipoUsuarioCama",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("descripcionCama",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("loginUsuario",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("estadoAdmision",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("numeroAutorizacion", "java.lang.String", true, VariableInfo.NESTED)


		};

	}

}