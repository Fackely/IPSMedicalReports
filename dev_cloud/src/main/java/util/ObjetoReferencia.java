/*
 * Creado en 3/02/2005
 *
 * Princeton S.A.
 */
package util;

/**
 * @author Juan David Ramírez López
 *
 * Princeton S.A.
 */
public class ObjetoReferencia
{
	/**
	 * String para manejar como referencia
	 */
	private String stringReferencia;
	
	/**
	 * Objeto para manejar como referencia
	 */
	private Object objectReferencia;
	
	/**
	 * Entero para manejar como referencia
	 */
	private int intReferencia;

	/**
	 * Entero para manejar como referencia
	 */
	private float floatReferencia;
	
	/**
	 * Entero para manejar como referencia
	 */
	private double doubleReferencia;
	/**
	 * @return Retorna doubleReferencia.
	 */
	public double getDoubleReferencia()
	{
		return doubleReferencia;
	}
	/**
	 * @param doubleReferencia Asigna doubleReferencia.
	 */
	public void setDoubleReferencia(double doubleReferencia)
	{
		this.doubleReferencia = doubleReferencia;
	}
	/**
	 * @return Retorna floatReferencia.
	 */
	public float getFloatReferencia()
	{
		return floatReferencia;
	}
	/**
	 * @param floatReferencia Asigna floatReferencia.
	 */
	public void setFloatReferencia(float floatReferencia)
	{
		this.floatReferencia = floatReferencia;
	}
	/**
	 * @return Retorna intReferencia.
	 */
	public int getIntReferencia()
	{
		return intReferencia;
	}
	/**
	 * @param intReferencia Asigna intReferencia.
	 */
	public void setIntReferencia(int intReferencia)
	{
		this.intReferencia = intReferencia;
	}
	/**
	 * @return Retorna objectReferencia.
	 */
	public Object getObjectReferencia()
	{
		return objectReferencia;
	}
	/**
	 * @param objectReferencia Asigna objectReferencia.
	 */
	public void setObjectReferencia(Object objectReferencia)
	{
		this.objectReferencia = objectReferencia;
	}
	/**
	 * @return Retorna stringReferencia.
	 */
	public String getStringReferencia()
	{
		return stringReferencia;
	}
	/**
	 * @param stringReferencia Asigna stringReferencia.
	 */
	public void setStringReferencia(String stringReferencia)
	{
		this.stringReferencia = stringReferencia;
	}
	
	/**
	 * Constructor vacío
	 */
	public ObjetoReferencia()
	{
		this.stringReferencia=new String();
		this.objectReferencia=new Object();
		this.intReferencia=0;
		this.floatReferencia=0;
		this.doubleReferencia=0;
	}
}
