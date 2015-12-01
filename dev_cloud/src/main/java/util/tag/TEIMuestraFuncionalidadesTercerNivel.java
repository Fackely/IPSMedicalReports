/*
 * Created on 31-may-2004
 * Usuario juanda
 *
 * juan@princetonSA.com
 */
package util.tag;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

/**
 * Creado el 31-may-2004
 * @author juanda
 * 
 *	juan@princetonSA.com
 */
public class TEIMuestraFuncionalidadesTercerNivel extends TagExtraInfo
{
	public VariableInfo[] getVariableInfo(TagData data)
	{
		if (data == null);
		return new VariableInfo[]
		{
			new VariableInfo("mensajeError",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("funcionalidades",  "java.util.Collection", true, VariableInfo.NESTED)
		};
	}
	
	
}
