package com.princetonsa.legacy;
import java.io.BufferedReader;
import java.io.InputStream;

/**
 * @author dramirez
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public interface Update {
	
	/**
	 * Este método permite cargar los datos a partir de un InputStream
	 * @param in InputStream del que se van a cargar los datos
	 */
	public void parseStream(InputStream in);

	/**
	 * Este método permite cargar los datos a partir de un InputStream
	 * @param in InputStream del que se van a cargar los datos
	 */
	public void parseStream(BufferedReader in);
	
	/**
	 * Este método permite cargar los datos a partir de un archivo
	 * @param arch Nombre del archivo plano donde se encuentran los datos
	 */
	public void parseFile(String arch);
}
