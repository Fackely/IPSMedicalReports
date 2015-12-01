package com.servinte.axioma.mundo.impl.odontologia.recomendacion;

import java.util.Iterator;
import java.util.List;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.dao.fabrica.odontologia.recomendacion.RecomendacionesDAOFabrica;
import com.servinte.axioma.dao.impl.odontologia.recomendacion.RecomendacionServicioProgramaDAO;
import com.servinte.axioma.dao.interfaz.odontologia.recomendacion.IRecomendacionSerProServProDAO;
import com.servinte.axioma.dao.interfaz.odontologia.recomendacion.IRecomendacionServicioProgramaDAO;
import com.servinte.axioma.mundo.fabrica.odontologia.recomendacion.RecomendacionesFabrica;
import com.servinte.axioma.mundo.interfaz.odontologia.recomendacion.IRecomendacionesServicioProgramasMundo;
import com.servinte.axioma.orm.RecomSerproSerpro;
import com.servinte.axioma.orm.RecomendacionesServProg;
import com.servinte.axioma.persistencia.UtilidadTransaccion;


/**
 * CLASE EN DONDE SE LLEVA LA LOGICA DE NEGOCIO DE RECOMENDACIONES SERVICIO PROGRAMA MUNDO
 * @author Edgar Carvajal
 *
 */
public class RecomendacionesServiciosProgramaMundo implements IRecomendacionesServicioProgramasMundo
{

	
	
	
	/**
	 * DAO
	 */
	private  IRecomendacionServicioProgramaDAO recomenDao;
	private  IRecomendacionSerProServProDAO recomenSerProSerProDao;
	
	
	
	/**
	 *CONSTRUTOR 
	 */
	public RecomendacionesServiciosProgramaMundo()
	{
		this.recomenDao=  RecomendacionesDAOFabrica.crearRecomendacionServicioProgramaDao();
		this.recomenSerProSerProDao =RecomendacionesDAOFabrica.crearRecomendacionServProServProDao() ;
	}
	
	
	
	
	
	@Override
	public void eliminarRecomenProgramaServicio(RecomendacionesServProg dtoReProgServ) 
	{
		UtilidadTransaccion.getTransaccion().begin();
		recomenDao.eliminarRecomenProgramaServicio(dtoReProgServ);
		UtilidadTransaccion.getTransaccion().commit();
	}

	
	
	@Override
	public void guardarRecomenProgramaServicio(	RecomendacionesServProg dtoReProgServ) 
	{
		try{
			
			UtilidadTransaccion.getTransaccion().begin();
			recomenDao.guardarRecomenProgramaServicio(dtoReProgServ);
			
			Iterator iter = dtoReProgServ.getRecomSerproSerpros().iterator();
			
			while(iter.hasNext())
			{
				RecomSerproSerpro objRecomSer=(RecomSerproSerpro) iter.next();
				objRecomSer.setRecomendacionesServProg(dtoReProgServ);
				recomenSerProSerProDao.guardarRecomenProgramaServicio(objRecomSer);
			}
			
			
			UtilidadTransaccion.getTransaccion().commit();

		}
		catch (Exception e) {
			UtilidadTransaccion.getTransaccion().rollback();
			Log4JManager.info(e);
			Log4JManager.error(e);
		}
		
	}

	
	
	
	@Override
	public List<RecomendacionesServProg> listaRecomendacioProgramaServicio(	RecomendacionesServProg dtoReProgServ) 
	{
		UtilidadTransaccion.getTransaccion().begin();
		List<RecomendacionesServProg> lista= recomenDao.listaRecomendacioProgramaServicio(dtoReProgServ);
		UtilidadTransaccion.getTransaccion().commit();
		
		return lista;
	}

	
	
	
	@Override
	public void modificarRecomenProgramaServicio(RecomendacionesServProg dtoReProgServ) 
	{
		UtilidadTransaccion.getTransaccion().begin();
		recomenDao.modificarRecomenProgramaServicio(dtoReProgServ);
		
		
	}



	@Override
	public RecomendacionesServProg consultaAvanzadaRecomendacion(RecomendacionesServProg dtoReSerProg) 
	{
		
		RecomendacionesServProg recomSerProg =recomenDao.consultaAvanzadaRecomendacion(dtoReSerProg);
	
		
		return recomSerProg;
	}

	


}


