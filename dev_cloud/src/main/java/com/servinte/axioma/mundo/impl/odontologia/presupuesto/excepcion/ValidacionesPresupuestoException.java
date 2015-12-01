/**
 * 
 */
package com.servinte.axioma.mundo.impl.odontologia.presupuesto.excepcion;

/**
 * @author Juan David Ramírez
 * @since Jan 13, 2011
 */
@SuppressWarnings("serial")
public class ValidacionesPresupuestoException extends Exception
{
	/**
	 * Mensaje de la excepción
	 */
	private String mensaje;

	
	/**
	 * 
	 */
	public ValidacionesPresupuestoException(String mensaje) {
		this.mensaje=mensaje;

	}
	
	
	/**
	 * Obtiene el valor del atributo mensaje
	 *
	 * @return Retorna atributo mensaje
	 */
	public String getMensaje()
	{
		return mensaje;
	}

	/**
	 * Establece el valor del atributo mensaje
	 *
	 * @param valor para el atributo mensaje
	 */
	public void setMensaje(String mensaje)
	{
		this.mensaje = mensaje;
	}
}
