/*
 * @(#)EvolucionInterface.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.mundo.atencion;

/**
 * <i>Interface</i> que deben implementar los objetos <code>Evolucion</code>, para indicar qu�
 * tipo de evoluci�n es (hospitalizaci�n, urgencias, ...).
 *
 * @version 1.0, May 15, 2003
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>
 */

public interface EvolucionInterface {

	/**
	 * Retorna el c�digo que indica el tipo de evoluci�n.
	 * Posibles valores son :
	 * <ul>
	 *   <li>Evolucion.GENERAL : General</li>
	 *   <li>Evolucion.URGENCIAS : Urgencias</li>
	 *   <li>Evolucion.HOSPITALARIA : Hospitalizaci�n</li>
	 * </ul>
	 * Estos c�digos son los definidos en la tabla especialidades_val
	 * @return el tipo de evoluci�n.
	 */
	public int getTipoEvolucion();

}