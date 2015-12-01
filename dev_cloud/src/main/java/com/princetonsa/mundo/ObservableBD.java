/*
 * @(#)ObservableBD.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.mundo;

import java.io.Serializable;
import java.util.Observable;

/**
 * Esta clase es un <code>Observable</code> que avisa de cambios en los datos de pacientes y usuarios
 * en la BD; tiene como observadores un conjunto de objetos <code>PersonaBasica</code> y
 * <code>UsuarioBasico</code>, que permanecen en sesi�n durante el uso de la aplicaci�n. Se llama el
 * notifyObservers() de <code>ObservableBD</code> cuando se descubre que en alguna parte del c�digo
 * o de los JSPs se ha efectuado un cambio en la BD, para que avise a todos sus <code>Observer</code>s
 * qui�n cambi�, y estos a su vez, en su update(), deber�n verificar contra sus propios datos,
 * y decidir si se deben recargar.
 *
 * @version Jun 17, 2003
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>
 */

public class ObservableBD extends Observable implements Serializable
{
	/**
	 * Este m�todo sobrecargado vuelve public el m�todo
	 * setChanged() de Observable, que es protected.
	 * @see java.util.Observable#setChanged()
	 */
	public void setChanged()
	{
		super.setChanged();
	}
}