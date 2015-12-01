/*
 * @(#)BeanNuevoSolicitarInterconsulta.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01 
 *
 */

package util.Beans;

/**
 * Bean para manejar la funcionalidad temporal de solicitar interconsulta,
 * soportando selecci�n de centro de costo, ocupaci�n y especialidad
 *
 * @version 1.0 Nov 18, 2003
 */
public class BeanNuevoSolicitarInterconsulta
{
	/**
	 * C�digo del centro de costo al que se va a realizar
	 * la solicitud (0 si puede responderla cualquiera)
	 */
	private int centroCosto=-1;

	/**
	 * C�digo de la ocupaci�n a la que se va a realizar
	 * la solicitud (0 si puede responderla cualquiera)
	 */
	private int ocupacion=-1;

	/**
	 * C�digo de la especialidad a la que se va a realizar
	 * la solicitud (0 si puede responderla cualquiera)
	 */
	private int especialidad=-1;
	/**
	 * @return
	 */
	public int getCentroCosto()
	{
		return centroCosto;
	}

	/**
	 * @return
	 */
	public int getEspecialidad()
	{
		return especialidad;
	}

	/**
	 * @return
	 */
	public int getOcupacion()
	{
		return ocupacion;
	}

	/**
	 * @param i
	 */
	public void setCentroCosto(int i)
	{
		centroCosto = i;
	}

	/**
	 * @param i
	 */
	public void setEspecialidad(int i)
	{
		especialidad = i;
	}

	/**
	 * @param i
	 */
	public void setOcupacion(int i)
	{
		ocupacion = i;
	}

}
