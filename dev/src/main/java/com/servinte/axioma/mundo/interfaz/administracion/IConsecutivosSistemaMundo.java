/**
 * 
 */
package com.servinte.axioma.mundo.interfaz.administracion;

/**
 * @author Juan David Ram�rez
 * @since Jan 18, 2011
 */
public interface IConsecutivosSistemaMundo
{

	/**
	 * Realiza un proceso de verificaci�n de los consecutivos
	 * disponibles para evitar saltos de consecutivos por alguna posible
	 * ca�da del sistema
	 * 
	 * @return true en caso de inicializar completamente, false de lo contrario
	 */
	public boolean inicializarConsecutivosDisponibles();

}
