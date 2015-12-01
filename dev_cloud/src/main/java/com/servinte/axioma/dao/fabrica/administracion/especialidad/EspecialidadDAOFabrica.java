package com.servinte.axioma.dao.fabrica.administracion.especialidad;

import com.servinte.axioma.dao.impl.administracion.EspecialidadDao;
import com.servinte.axioma.dao.interfaz.administracion.IEspecialidadDao;


/**
 * 
 * @author Edgar Carvajal
 *
 */
public abstract class EspecialidadDAOFabrica  {
	
	

	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	 public static final IEspecialidadDao crearEspecialidad()
	 {
		 return new EspecialidadDao(); 
	 }
	
	

}
