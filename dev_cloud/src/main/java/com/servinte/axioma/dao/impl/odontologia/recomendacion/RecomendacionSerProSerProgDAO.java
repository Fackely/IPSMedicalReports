package com.servinte.axioma.dao.impl.odontologia.recomendacion;

import java.util.List;

import org.axioma.util.log.Log4JManager;
import org.hibernate.HibernateException;

import com.servinte.axioma.dao.interfaz.odontologia.recomendacion.IRecomendacionSerProServProDAO;
import com.servinte.axioma.orm.RecomSerproSerpro;
import com.servinte.axioma.orm.delegate.odontologia.recomendaciones.RecomendacionSerProgSerProgDelegate;
import com.servinte.axioma.orm.delegate.odontologia.recomendaciones.RecomendacionesServProgDelegate;
import com.servinte.axioma.persistencia.UtilidadTransaccion;


/**
 * 
 * @author Edgar Carvajal  Ruiz
 *
 */
public class RecomendacionSerProSerProgDAO  implements IRecomendacionSerProServProDAO{

	
	
	/**
	 * DELEGATE
	 */
	private RecomendacionSerProgSerProgDelegate recomenSerProgDelegate;
	

	
	/**
	 * CONSTRUTOR
	 */
	public RecomendacionSerProSerProgDAO()
	{
		recomenSerProgDelegate= new RecomendacionSerProgSerProgDelegate();
	}
	

	
	
	
	@Override
	public void eliminarRecomenProgramaServicio(RecomSerproSerpro dtoReProgServ) 
	{
		UtilidadTransaccion.getTransaccion().begin();
		recomenSerProgDelegate.delete(dtoReProgServ);
		UtilidadTransaccion.getTransaccion().commit();
		
	}

	@Override
	public void guardarRecomenProgramaServicio(RecomSerproSerpro dtoReProgServ) 
	{
		try{
			
			recomenSerProgDelegate.attachDirty(dtoReProgServ);
		}
		catch (HibernateException e) {
			Log4JManager.info(e);
			Log4JManager.error(e);
		}
	
	}

	
	

	
	
	
	@Override
	public void modificarRecomenProgramaServicio(RecomSerproSerpro dtoReProgServ) 
	{
		UtilidadTransaccion.getTransaccion().begin();
		recomenSerProgDelegate.merge(dtoReProgServ);
		UtilidadTransaccion.getTransaccion().commit();
		
	}





	@Override
	public List<RecomSerproSerpro> listaRecomendacionesxSerProg(RecomSerproSerpro dtoReSerProg) 
	{
		
		List<RecomSerproSerpro> lista= recomenSerProgDelegate.listaRecomendacionesxSerProg(dtoReSerProg);
		
		return  lista;
	}





	
}
