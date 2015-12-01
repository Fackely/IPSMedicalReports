
/*
 * Creado   24/08/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package util.tag;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

/**
 * Esta clase es auxiliar de <code>TagBusquedaBD</code>
 * donde se definen las <i>scripting variables</i> necesarias 
 * (nombres, codigos). Se maneja con colección para conocer de 
 * antemano el número de resultados encontrados (Sin tener 
 * que recorrerlo) 
 *
 * @version 1.0, 24/08/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class TEITagBusquedaBD extends TagExtraInfo 
{
    /**
	 * Define las <i>scripting variables</i> usadas en TagBusquedaBD y las
	 * pone en un arreglo de objetos <code>VariableInfo</code>
	 * @see javax.servlet.jsp.tagext.TagExtraInfo#getVariableInfo(TagData)
	 */
	public VariableInfo[] getVariableInfo(TagData data) 
	{
		if (data == null); // NOP para evitar el warning "The argument data is never read"

		return new VariableInfo[] 
		{
			new VariableInfo("resultados",  "java.util.Vector", true, VariableInfo.NESTED),
		};
	}

}
