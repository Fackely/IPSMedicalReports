package com.servinte.axioma.dao.interfaz.odontologia.recomendacion;

import java.util.List;

import com.servinte.axioma.orm.RecomSerproSerpro;

public interface IRecomendacionSerProServProDAO {
	
	

	

	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param dtoReProgServ
	 */
	public void modificarRecomenProgramaServicio (RecomSerproSerpro dtoReProgServ);
	

	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param dtoReProgServ
	 */
	public void eliminarRecomenProgramaServicio (RecomSerproSerpro dtoReProgServ);
	
	
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param dtoReProgServ
	 */
	public void guardarRecomenProgramaServicio (RecomSerproSerpro dtoReProgServ);
	
	
	
	/**
	 * LISTA RECOMENDACIONES POR SERVICIO O POR PROGRAMA
	 * @author Edgar Carvajal Ruiz
	 * @param dtoReSerProg
	 * @return
	 */
	public List<RecomSerproSerpro> listaRecomendacionesxSerProg(RecomSerproSerpro dtoReSerProg);
	
	

}
