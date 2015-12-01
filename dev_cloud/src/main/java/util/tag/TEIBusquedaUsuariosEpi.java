package util.tag;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;



public class TEIBusquedaUsuariosEpi extends TagExtraInfo {
	
	public VariableInfo[] getVariableInfo(TagData data) {

		if (data == null); // NOP para evitar el warning "The argument data is never read"

		return new VariableInfo[] {
			new VariableInfo("resultados", "java.util.Vector", true, VariableInfo.NESTED),
		};

	}
}
