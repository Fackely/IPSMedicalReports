package com.servinte.axioma.servicio.impl.odontologia.recomendacion;



import com.servinte.axioma.mundo.fabrica.odontologia.recomendacion.RecomendacionesFabrica;
import com.servinte.axioma.mundo.interfaz.odontologia.recomendacion.IRecomSerproSerproMundo;
import com.servinte.axioma.orm.RecomSerproSerpro;
import com.servinte.axioma.orm.RecomendacionesServProg;
import com.servinte.axioma.servicio.interfaz.odontologia.recomendacion.IRecomendacionSerProgSerProgSERVICIO;


/**
 * 
 * @author Edgar Carvajal
 *
 */
public class RecomendacionSerProgSerProgSERVICIO implements IRecomendacionSerProgSerProgSERVICIO {

	
	/**
	 * INTERFAZ
	 */
	private IRecomSerproSerproMundo  recomMundo;
	
	
	
	
	/**
	 *CONSTRUTORR 
	 */
	public RecomendacionSerProgSerProgSERVICIO()
	{
		recomMundo = RecomendacionesFabrica.crearRecomendacionSerProSerProMundo();
	}
	
	
	
	@Override
	public RecomendacionesServProg consultarRecomendacionProgramaServicio( RecomSerproSerpro dtoReSerProg) 
	{
		return recomMundo.consultarRecomendacionProgramaServicio(dtoReSerProg);
	}

}
