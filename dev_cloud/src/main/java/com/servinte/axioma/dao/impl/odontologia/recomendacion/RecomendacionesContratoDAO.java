package com.servinte.axioma.dao.impl.odontologia.recomendacion;

import java.util.ArrayList;
import java.util.List;

import org.axioma.util.log.Log4JManager;
import org.hibernate.HibernateException;

import com.princetonsa.dto.odontologia.DtoRecomendaciones;
import com.servinte.axioma.dao.interfaz.odontologia.recomendacion.IRecomendacionesContratoDAO;
import com.servinte.axioma.orm.RecomendacionesContOdonto;
import com.servinte.axioma.orm.delegate.odontologia.recomendaciones.RecomendacionesContOdontoDelegate;


/**
 * DAO DE RECOMENDACIONES ODONTOLOGICAS 
 * 
 * @author Edgar Carvajal
 *
 */
public class RecomendacionesContratoDAO implements IRecomendacionesContratoDAO{

	
	
	/**
	 * DELEGATE
	 */
	private RecomendacionesContOdontoDelegate delegateRecomendaciones;
	
	
	/**
	 * CONSTRUTOR 
	 */
	public RecomendacionesContratoDAO()
	{
		delegateRecomendaciones= new RecomendacionesContOdontoDelegate();
	}
	
	
	
	@Override
	public List<RecomendacionesContOdonto> listaRecomendaciones( RecomendacionesContOdonto dtoRecomendaciones, int institucion) 
	{
		List<RecomendacionesContOdonto>  lista= delegateRecomendaciones.listaRecomendaciones(dtoRecomendaciones, institucion);
		return lista;
	}



	@Override
	public void eliminarRecomendaciones(RecomendacionesContOdonto dtoRecomendacion) 
	{
	
		delegateRecomendaciones.delete(dtoRecomendacion);
	}


	

	@Override
	public void guardarRecomendaciones(	RecomendacionesContOdonto dtoRecomendacion) 
	{
		
		//BorrarSimularcionContext<RecomendacionesContOdonto> context = new BorrarSimularcionContext<RecomendacionesContOdonto>();
		
		try
		{
			//context.persist(dtoRecomendacion);
			
			delegateRecomendaciones.persist(dtoRecomendacion);
		}
		catch (HibernateException e) 
		{
			Log4JManager.info(e.getMessage());
			Log4JManager.error("error DAO Recomendaciones "+e);
		}
	}



	@Override
	public void modificarRecomendaciones(RecomendacionesContOdonto dtoRecomendacion) 
	{
		try{
			delegateRecomendaciones.merge(dtoRecomendacion);
		}
		catch (HibernateException e) 
		{
			Log4JManager.info(e.getMessage());
			Log4JManager.error("error DAO Recomendaciones "+e);
		}
	}



	@Override
	public List<DtoRecomendaciones> listarRecomendaciones(RecomendacionesContOdonto dtoRecomen,	ArrayList<Integer> listaCodigos) 
	{
		List<DtoRecomendaciones> listarRecomendaciones =delegateRecomendaciones.listarRecomendaciones(dtoRecomen, listaCodigos);
		return listarRecomendaciones;
	}


	

	@Override
	public List<RecomendacionesContOdonto> busquedaRecomendaciones(	RecomendacionesContOdonto dtoRecomendaciones, boolean aplicaCodigoPK) 
	{
	
		 List<RecomendacionesContOdonto> listTmp=null;
		 
		 try {
		
			 listTmp= delegateRecomendaciones.busquedaRecomendaciones(dtoRecomendaciones, aplicaCodigoPK);
		} 
		catch (HibernateException e) 
		{
			Log4JManager.info(e.getMessage());
			Log4JManager.error(e.getMessage());
		}
		 
		return listTmp;
	}


	
	
	@Override
	public RecomendacionesContOdonto buscarRecomenacionxId(	RecomendacionesContOdonto recomendacion) 
	{
		RecomendacionesContOdonto tmp =null;
		try
		{
			tmp=delegateRecomendaciones.buscarRecomenacionxId(recomendacion);
		}
		catch (HibernateException e) 
		{
			Log4JManager.info(e);
			Log4JManager.error(e);
		}
		return tmp;
	}



	@Override
	public ArrayList<DtoRecomendaciones> obtenerRecomendacionesPresuOdonto(
			long codPresoOdonto) {
		return delegateRecomendaciones.obtenerRecomendacionesPresuOdonto(codPresoOdonto);
	}
	
	
}
