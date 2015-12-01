package com.servinte.axioma.dao.interfaz.odontologia.recomendacion;

import java.util.List;

import com.servinte.axioma.orm.RecomendacionesServProg;

public interface IRecomendacionServicioProgramaDAO 
{
	
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param dtoReProgServ
	 * @return
	 */
	public List<RecomendacionesServProg> listaRecomendacioProgramaServicio(RecomendacionesServProg dtoReProgServ);
	

	

	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param dtoReProgServ
	 */
	public void modificarRecomenProgramaServicio (RecomendacionesServProg dtoReProgServ);
	

	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param dtoReProgServ
	 */
	public void eliminarRecomenProgramaServicio (RecomendacionesServProg dtoReProgServ);
	
	
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param dtoReProgServ
	 */
	public void guardarRecomenProgramaServicio (RecomendacionesServProg dtoReProgServ);
	
	
	
	
	/**
	 * CONSULTA AVANZADA 
	 * @author Edgar Carvajal Ruiz
	 * @param dtoReSerProg
	 * @return
	 */
	public RecomendacionesServProg consultaAvanzadaRecomendacion(RecomendacionesServProg dtoReSerProg);
	
	
	

}
