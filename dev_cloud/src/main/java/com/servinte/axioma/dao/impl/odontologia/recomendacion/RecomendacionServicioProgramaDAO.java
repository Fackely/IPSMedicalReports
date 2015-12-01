package com.servinte.axioma.dao.impl.odontologia.recomendacion;

import java.util.List;

import com.servinte.axioma.dao.interfaz.odontologia.recomendacion.IRecomendacionServicioProgramaDAO;
import com.servinte.axioma.orm.RecomendacionesServProg;
import com.servinte.axioma.orm.delegate.odontologia.recomendaciones.RecomendacionesServProgDelegate;


/**
 * 
 * @author Edgar Carvajal
 *
 */
public  class RecomendacionServicioProgramaDAO implements IRecomendacionServicioProgramaDAO {

	
	
	/**
	 * DELEGATE 
	 */
	private RecomendacionesServProgDelegate recomendacionDelegate;
	
	
	
	
	/**
	 * CONSTRUTOR 
	 */
	public RecomendacionServicioProgramaDAO()
	{
		recomendacionDelegate = new RecomendacionesServProgDelegate();
	}
	
	
	
	@Override
	public void eliminarRecomenProgramaServicio(RecomendacionesServProg dtoReProgServ) 
	{
		recomendacionDelegate.delete(dtoReProgServ);
		
	}

	
	
	@Override
	public void guardarRecomenProgramaServicio(	RecomendacionesServProg dtoReProgServ) 
	{
		//recomendacionDelegate.persist(dtoReProgServ);
		recomendacionDelegate.attachDirty(dtoReProgServ);
	}

	
	@Override
	public List<RecomendacionesServProg> listaRecomendacioProgramaServicio(	RecomendacionesServProg dtoReProgServ) 
	{
		return  recomendacionDelegate.listaRecomendacionesSerProg(dtoReProgServ);
	}

	
	
	@Override
	public void modificarRecomenProgramaServicio(RecomendacionesServProg dtoReProgServ) 
	{
		recomendacionDelegate.merge(dtoReProgServ);
	}



	@Override
	public RecomendacionesServProg consultaAvanzadaRecomendacion(RecomendacionesServProg dtoReSerProg) 
	{
		return recomendacionDelegate.consultaAvanzada(dtoReSerProg);
	}

	

}
