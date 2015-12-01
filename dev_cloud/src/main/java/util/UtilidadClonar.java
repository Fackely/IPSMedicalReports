package util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * Clase para clonar objetos
 * No se debe utilizar nuevamente, se reemplaza por {@link UtilidadClonacion}
 * <br>
 * Su correcto uso es:
 * <br>
 * <br>
 * <code>
 * Objeto original=new Objeto();
 * <br>
 * Objeto clonado=(Objeto)UntilidadClonacion.clonar(original)
 * </code>
 * @author Ronald Ángel
 * @version 1.0
 * @deprecated 
 * Se hizo una nueva utilidad par clonar objetos sin importar su profundidad. 
 */
public class UtilidadClonar implements Cloneable
{

	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger = Logger.getLogger(UtilidadClonar.class);

	/***
	 * Clonar
	 * @param Objeto a clonar
	 */
	@Deprecated
	public static Object clone(Object o)
	{

		// Clonacion de la parte del encabezado
		Class<? extends Object> clase = o.getClass();
		try
		{
			Method metodo;
			metodo = clase.getMethod("clone");
			logger.info("CLONANDO --------------->" + clase.getSimpleName());
			o = metodo.invoke(o, (Object[]) null);

		} catch (Exception e)
		{
			/*
			 * El método no existe
			e.printStackTrace();
			*/
		}

		// Se obtienen campos para ver si lo que recibo es un objeto o un
		// arrayList
		Field[] field = clase.getDeclaredFields();

		for (int i = 0; i < field.length; i++)
		{
			Object resultado = new Object();

			try
			{
				String nombreCampo = field[i].getName();
				Method metodo;
				logger.info(nombreCampo);
				nombreCampo = nombreCampo.substring(0, 1).toUpperCase()
						+ nombreCampo.substring(1, nombreCampo.length());
				try
				{
					metodo = clase.getMethod("get" + nombreCampo);
				} catch (NoSuchMethodError e)
				{
					metodo = clase.getMethod("is" + nombreCampo);
					try
					{
						metodo = clase.getMethod("get" + nombreCampo);
					}catch (NoSuchMethodError ex)
					{
						// El método no existe
					}
				}

				resultado = metodo.invoke(o, (Object[]) null);

				// En caso de ser objeto y no primitivo , llamo recursivamente
				// el metodo

				if (resultado instanceof Object
						&& !(resultado instanceof String)
						&& !(resultado instanceof Integer)
						&& !(resultado instanceof Double)
						&& !(resultado instanceof Boolean)
						&& !(resultado instanceof ArrayList))
				{

					resultado = clone(resultado);
				}
				// En caso de ser un arrayList llamo recursivamente el metodo
				// con cada elemento del arrayList

				else if (resultado instanceof ArrayList)
				{
					ArrayList<Object> array = (ArrayList) resultado;
					for (Object x : array)
					{

						x = clone(x);

					}
				}
			} catch (Exception ex)
			{
				logger.error("Error Imprimir" + ex);

			}

		}

		return o;

	}

}
