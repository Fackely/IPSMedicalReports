package util.struts;

import javax.servlet.ServletException;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;

import util.ConversorBigDecimal;
import util.ConversorIntegerStruts;
import util.ConversorStringAFechaStruts;

/**
 * Plugin de struts que se encarga de registrar los conversores
 * necesarios para el funcionamiento del sistema.
 * @author Juan David Ram&iacute;rez
 */
public class PluginConversores implements PlugIn
{

	@Override
	public void destroy()
	{
		// Por ahora no hago nada
		
	}

	@Override
	public void init(ActionServlet arg0, ModuleConfig arg1)
			throws ServletException
	{
		ConversorStringAFechaStruts stringAFechaStruts = new ConversorStringAFechaStruts("dd/MM/yyyy");
		ConvertUtils.register(stringAFechaStruts, java.util.Date.class);
		
		ConversorBigDecimal bigDecimal = new ConversorBigDecimal();
		ConvertUtils.register(bigDecimal, java.math.BigDecimal.class);
		
		ConversorIntegerStruts integer = new ConversorIntegerStruts();
		ConvertUtils.register(integer, java.lang.Integer.class);
		
		
	}

}
