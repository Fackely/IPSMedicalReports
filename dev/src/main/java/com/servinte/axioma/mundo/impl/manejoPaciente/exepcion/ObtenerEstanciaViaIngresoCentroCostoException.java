/**
 * 
 */
package com.servinte.axioma.mundo.impl.manejoPaciente.exepcion;

/**
 * @author Cristhian Murillo
 */
@SuppressWarnings("serial")
public class ObtenerEstanciaViaIngresoCentroCostoException extends Exception
{
	/**
	 * Mensaje de la excepción
	 */
	private String mensaje="No se pudo obtener el Centro de Costo definido para la Vía de Ingreso y Entidad Subcontratada. (facturacion.estancia_via_ing_centro_costo)";

	
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
