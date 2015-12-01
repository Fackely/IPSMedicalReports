package util;

import java.math.BigDecimal;

import org.apache.commons.beanutils.Converter;
import org.axioma.util.log.Log4JManager;

/**
 * Clase encargada de hacer la conversi&oacute;n de fechas de la aplicaci&oacute;n
 * a tipo <code>BigDecimal</code> para ser asignadas a los DTO.
 * @author Juan David Ram&iacute;rez
 */
public class ConversorBigDecimal implements Converter
{

	@SuppressWarnings("unchecked")
	@Override
	public Object convert(Class clase, Object valor)
	{
		String valorStr=(String)valor;
		if(UtilidadTexto.isEmpty(valorStr))
		{
			return null;
		}
		else
		{
			try{
				BigDecimal bigDecimal=new BigDecimal(valorStr);
				return bigDecimal;
			}
			catch (Exception e) {
				Log4JManager.error("Error convirtiendo, la cadena ("+valorStr+") no representa un valor BigDecimal",e);
			}
		}
		return null;
			
	}

}
