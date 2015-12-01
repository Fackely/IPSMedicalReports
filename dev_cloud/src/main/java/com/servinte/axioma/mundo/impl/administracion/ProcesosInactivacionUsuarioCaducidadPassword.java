/**
 * 
 */
package com.servinte.axioma.mundo.impl.administracion;

import util.Administracion.UtilidadesAdministracion;

import com.servinte.axioma.mundo.interfaz.administracion.IProcesosInactivacionUsuarioCaducidadPassword;

/**
 * @author axioma
 *
 */
public class ProcesosInactivacionUsuarioCaducidadPassword implements
		IProcesosInactivacionUsuarioCaducidadPassword {

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.administracion.IProcesosInactivacionUsuarioCaducidadPassword#procesoInactivacionUsuario()
	 */
	@Override
	public int procesoInactivacionUsuario() 
	{
		return UtilidadesAdministracion.procesoInactivacionUsuario();
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.administracion.IProcesosInactivacionUsuarioCaducidadPassword#procesoCaducidadPassword()
	 */
	@Override
	public int procesoCaducidadPassword() 
	{
		return UtilidadesAdministracion.procesoCaducidadPassword();
	}

}
