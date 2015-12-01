/*
 * @(#)TEIMuestraAsocioEvolucionValoracion.java
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
 * Esta clase es auxiliar de <code>TagMuestraAsocioEvolucionValoracion</code> 
 * donde se definen las <i>scripting variables</i> necesarias (tipoValoracion,
 * fechaValoracion, codigoTipoIdentificacionMedico, numeroIdentificacionMedico,
 * codigoEspecialidad).  Para evitar que en los jsp hayan enumerations y fors, 
 * se trabajaran con variables individuales, pero iterando en el Tag. Por esta 
 * misma razon las variables no tienen nombres de conjunto. Entrada Válida se
 * refiere a si esta entrada se debe mostrar o no
 * Ej codigos sino de elementos individuales, codigo
 * 
 *	@version 1.0, Oct 17, 2003
 */
public class TEIMuestraAsocioEvolucionValoracion extends TagExtraInfo 
{

	/**
	 * Define las <i>scripting variables</i> usadas en
	 * TagMuestraAsocioEvolucionValoracion y las pone en un arreglo de objetos
	 * <code>VariableInfo</code>
	 * @see javax.servlet.jsp.tagext.TagExtraInfo#getVariableInfo(TagData)
	 */
	public VariableInfo[] getVariableInfo(TagData data) 
	{

		if (data == null); // NOP para evitar el warning "The argument data is never read"

		return new VariableInfo[] 
		{
			
			new VariableInfo("numeroSolicitud",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("entradaValida",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("tipoValoracion",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("fechaValoracion",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("medicoResultadoValoracion",  "com.princetonsa.mundo.UsuarioBasico", true, VariableInfo.NESTED),
			new VariableInfo("especialidad",  "java.lang.String", true, VariableInfo.NESTED),

		};

	}

}
