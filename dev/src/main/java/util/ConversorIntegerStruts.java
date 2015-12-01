package util;

import java.lang.Integer;

import org.apache.commons.beanutils.Converter;
import org.axioma.util.log.Log4JManager;

/**
 * Clase encargada de hacer la conversi&oacute;n Integer
 * a tipo <code>java.lang.Integer</code> para ser asignadas a los DTO.
 * @author Cristhian Murillo
 */
public final class ConversorIntegerStruts implements Converter
{
	@SuppressWarnings("unchecked")
	@Override
	public Object convert(Class clase, Object valor)
	{
		String valorStr=(String)valor;
		
		if( UtilidadTexto.isEmpty(valorStr) )
		{
			return null;
		}
		else
		{
			try{
				Integer integer = Integer.parseInt(valorStr);
				return integer;
			}
			catch (Exception e) {
				Log4JManager.error("Error convirtiendo, la cadena ("+valorStr+") no representa un valor java.lang.Integer",e);
			}
		}
		return null;
			
	}
}
