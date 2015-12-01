package com.servinte.axioma.servicio.fabrica.administracion;

import com.servinte.axioma.servicio.impl.administracion.EspecialidadServcio;
import com.servinte.axioma.servicio.interfaz.administracion.IEspecialidadServicio;

public abstract class EspecialidadServicioFabrica {
	
	
	
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static IEspecialidadServicio crearEspecialidadServicio()
	{
		return new EspecialidadServcio();
	}

}
