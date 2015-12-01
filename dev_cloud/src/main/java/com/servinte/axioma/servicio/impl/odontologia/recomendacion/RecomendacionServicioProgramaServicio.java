package com.servinte.axioma.servicio.impl.odontologia.recomendacion;

import java.util.List;

import com.servinte.axioma.mundo.fabrica.odontologia.recomendacion.RecomendacionesFabrica;
import com.servinte.axioma.mundo.interfaz.odontologia.recomendacion.IRecomendacionesServicioProgramasMundo;
import com.servinte.axioma.orm.RecomendacionesServProg;
import com.servinte.axioma.servicio.interfaz.odontologia.recomendacion.IRecomendacionServicioProgramaServicio;


/**
 * 
 * @author Edgar Carvajal 
 *
 */
public class RecomendacionServicioProgramaServicio implements IRecomendacionServicioProgramaServicio {

	
	
	/**
	 * INTERFAZ MUNDO RECOMENDACIONES
	 */
	private IRecomendacionesServicioProgramasMundo recomendacionMundo;
	
	
	/**
	 * CONSTRUTOR
	 */
	public RecomendacionServicioProgramaServicio()
	{
		recomendacionMundo=RecomendacionesFabrica.crearRecomendacionServioProgramaMundo();
	}
	
	
	
	
	
	
	@Override
	public void eliminarRecomendacionesServicioPrograma( RecomendacionesServProg dtoRecomendaciones) 
	{
			recomendacionMundo.eliminarRecomenProgramaServicio(dtoRecomendaciones);
		
	}
	
	
	

	@Override
	public void guardarRecomendacionesServicioPrograma(	RecomendacionesServProg dtoRecomendaciones) 
	{
		recomendacionMundo.guardarRecomenProgramaServicio(dtoRecomendaciones);
	}

	
	
	@Override
	public List<RecomendacionesServProg> listarRecomendacionesServicioPrograma(
			RecomendacionesServProg dtoRecomendaciones) {
	
		return recomendacionMundo.listaRecomendacioProgramaServicio(dtoRecomendaciones);
	}

	
	
	
	@Override
	public void modificarRecomendacionesServicioPrograma(RecomendacionesServProg dtoRecomendaciones) 
	{
		recomendacionMundo.modificarRecomenProgramaServicio(dtoRecomendaciones);
	}

}
