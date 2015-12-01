/*
 * Creado el 29/04/2005
 * Juan David Ramírez López
 */
package util.tag;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

/**
 * @author Juan David Ramírez
 * 
 * CopyRight Princeton S.A.
 * 29/04/2005
 */
public class TEIMuestraAtributosJustificacion extends TagExtraInfo
{
	/**
	 * Define las <i>scripting variables</i> usadas en TagMuestraAtributosJustificacion y las
	 * pone en un arreglo de objetos <code>VariableInfo</code>
	 * @see javax.servlet.jsp.tagext.TagExtraInfo#getVariableInfo(TagData)
	 */
	public VariableInfo[] getVariableInfo(TagData data) {

		if (data == null); // NOP para evitar el warning "The argument data is never read"

		return new VariableInfo[] 
		{
			new VariableInfo("resultados",  "java.util.Vector", true, VariableInfo.NESTED)
		};

	}

}
