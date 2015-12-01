package com.servinte.axioma.servicio.interfaz.odontologia.recomendacion;

import java.util.List;

import com.servinte.axioma.orm.RecomendacionesServProg;


/**
 * 
 * @author Edgar Carvajal 
 *
 */
public interface IRecomendacionServicioProgramaServicio
{
	
	
	

	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public List<RecomendacionesServProg> listarRecomendacionesServicioPrograma(RecomendacionesServProg dtoRecomendaciones);
	
	
	
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param dtoRecomendaciones
	 */
	public void guardarRecomendacionesServicioPrograma(RecomendacionesServProg dtoRecomendaciones);
	

	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param dtoRecomendaciones
	 */
	public void modificarRecomendacionesServicioPrograma(RecomendacionesServProg dtoRecomendaciones);

	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param dtoRecomendaciones
	 */
	public void eliminarRecomendacionesServicioPrograma(RecomendacionesServProg dtoRecomendaciones);
	

	
}
