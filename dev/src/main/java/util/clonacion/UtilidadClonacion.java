package util.clonacion;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.axioma.util.log.Log4JManager;

/**
 * Utilidad para clonar objetos complejos Genera un buffer de datos para luego
 * copiarlo, es de anotar que los objetos que se copian deben ser serializables.
 * 
 * @author Juan David Ramírez
 * @since 19 Ago 2010
 * @version 1.0
 */
public class UtilidadClonacion
{
	/**
	 * Clonar un objeto sin importar su complejidad
	 * @param fuente Objeto que se desea clonar
	 * @return Objeto clonado
	 */
	// FIXME Hacer el método generico con parametrización java 6
	public static Object clonar(Object fuente)
	{
		Object obj = null;
		try
		{
			// Escribe el objeto en un buffer
			ByteArrayOutputStreamRapido fbos = new ByteArrayOutputStreamRapido();
			ObjectOutputStream out = new ObjectOutputStream(fbos);
			out.writeObject(fuente);
			out.flush();
			out.close();

			/*
			 * Lee el buffer para obtener el array de bytes para luego
			 * convertirlo al objeto clonado
			 */
			ObjectInputStream in = new ObjectInputStream(fbos.getInputStream());
			obj = in.readObject();
		} catch (IOException e)
		{
			Log4JManager.error("Error clonando el objeto", e);
		} catch (ClassNotFoundException cnfe)
		{
			Log4JManager.error("No se encontró la clase que se desea clonar", cnfe);
		}
		return obj;
	}

}
