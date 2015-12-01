/*
 * Creado en 03-nov-2005
 * 
 * Autor Santiago Salamanca
 * SysMédica
 */
package util.tag;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

/**
 * @author santiago
 *
 */
public class TEIHayNotificacionesPendientes extends TagExtraInfo {

    public VariableInfo[] getVariableInfo(TagData data) {

		if (data == null); // NOP para evitar el warning "The argument data is never read"

		return new VariableInfo[] {
			new VariableInfo("incompletas", "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("seguimiento",  "java.lang.String", true, VariableInfo.NESTED),
			new VariableInfo("completas", "java.lang.String", true, VariableInfo.NESTED),
		//	new VariableInfo("tipoMsg", "java.lang.String", true, VariableInfo.NESTED),
		};

	}
}
