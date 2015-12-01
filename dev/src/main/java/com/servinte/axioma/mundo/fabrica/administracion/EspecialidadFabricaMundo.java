package com.servinte.axioma.mundo.fabrica.administracion;

import com.servinte.axioma.mundo.impl.administracion.EspecialidadMundo;
import com.servinte.axioma.mundo.interfaz.administracion.IEspecialidadMundo;

public abstract class EspecialidadFabricaMundo {
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static IEspecialidadMundo crearEspecialidadMundo()
	{
		return new EspecialidadMundo();
	}
}
