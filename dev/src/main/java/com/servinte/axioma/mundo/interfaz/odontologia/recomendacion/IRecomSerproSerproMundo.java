package com.servinte.axioma.mundo.interfaz.odontologia.recomendacion;

import java.util.List;

import com.servinte.axioma.orm.RecomSerproSerpro;
import com.servinte.axioma.orm.RecomendacionesServProg;



/**
 * 
 * @author axioma
 *
 */
public interface IRecomSerproSerproMundo 

{
	
	
	

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
	
	
	
	/**
	 * CONSULTA LA RECOMENDACIONES POR EL DETALLE RECOMENDACION PROGRAMA SERVICIO
	 * @author Edgar Carvajal Ruiz
	 * @param dtoReSerProg
	 */
	public RecomendacionesServProg consultarRecomendacionProgramaServicio(RecomSerproSerpro dtoReSerProg);
	
	
	

}
