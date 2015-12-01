package com.servinte.axioma.servicio.interfaz.odontologia.recomendacion;



import com.servinte.axioma.orm.RecomSerproSerpro;
import com.servinte.axioma.orm.RecomendacionesServProg;



/**
 * 
 * @author Edgar Carvajal
 *
 */
public interface IRecomendacionSerProgSerProgSERVICIO 
{
	
	/**
	 * CONSULTAR RECOMENDACIONES POR SERVICIO O PROGRAMA
	 * @author Edgar Carvajal Ruiz
	 * @param dtoReSerProg
	 * @return
	 */
	public RecomendacionesServProg consultarRecomendacionProgramaServicio(RecomSerproSerpro dtoReSerProg) ;

}
