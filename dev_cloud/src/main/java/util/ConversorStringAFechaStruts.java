package util;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.beanutils.Converter;
import org.axioma.util.log.Log4JManager;

/**
 * Clase encargada de hacer la conversi&oacute;n de fechas de la aplicaci&oacute;n
 * a tipo <code>java.util.Date</code> para ser asignadas a los DTO.
 * @author Juan David Ram&iacute;rez
 */
public final class ConversorStringAFechaStruts implements Converter
{
	/**
	 * Formato utilizado para la conversi&oacute;n de las fechas.
	 * Lo tengo que poner estático debido a que no puedo
	 * perder el patr&oacute;n de conversi&oacute;n
	 */
	private static SimpleDateFormat dateFormat;

	/**
	 * Constructor
	 * Internamente crea un objeto SimplDateFormat, el cual
	 * es el encargado de manejar la conversi&oacute;n.
	 * @param datePattern Patr&oacute;n de conversi&oacute;n Ej: dd/MM/yyyy
	 */
	public ConversorStringAFechaStruts(String datePattern)
	{
		try{
			dateFormat = new SimpleDateFormat(datePattern);
		}
		catch (IllegalArgumentException e) {
			Log4JManager.error("Error de formato "+datePattern, e);

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object convert(Class type, Object value)
	{
		try
		{
			if(UtilidadTexto.isEmpty((String)value)){
				return null;
			}
			return dateFormat.parse((String) value);
		}
		catch (ParseException e)
		{
			Log4JManager.error("Error convirtiendo, la cadena ("+value+") no representa un valor java.util.Date", e);
		}
		return null;
	}
}
