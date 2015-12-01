package com.servinte.axioma.orm.delegate.odontologia.recomendaciones;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.servinte.axioma.orm.RecomSerproSerpro;
import com.servinte.axioma.orm.RecomendacionesContOdonto;
import com.servinte.axioma.orm.RecomendacionesServProg;
import com.servinte.axioma.orm.RecomendacionesServProgHome;




/**
 * 
 * @author Edgar Carvajal Ruiz
 *
 */
public class RecomendacionesServProgDelegate extends RecomendacionesServProgHome 
{
	
	
	
	/**
	 *  TODO MODIFICAR POR BUSQUEDA AVANZANDA LISTA RECOMENDACIONES 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public List<RecomendacionesServProg> listaRecomendacionesSerProg(RecomendacionesServProg dtoReSerProg)
	{	
		
		Criteria criteriosList= sessionFactory.getCurrentSession().createCriteria(RecomendacionesServProg.class);
		/*
		 *FILTROS  
		 */
		/*
		 *CONSULTA POR CODIGO  
		 */
		if(dtoReSerProg.getCodigoPk() >0)
		{
			criteriosList.add(Restrictions.eq("codigoPk",dtoReSerProg.getCodigoPk()));
		}
		/*
		 *CONSULTAR POR INSTITUCION 
		 */
		if(dtoReSerProg.getInstituciones()!=null && dtoReSerProg.getInstituciones().getCodigo()>0)
		{
			criteriosList.add(Restrictions.eq("instituciones.codigo", dtoReSerProg.getInstituciones().getCodigo()));
		}
		
		
		return (List<RecomendacionesServProg> )criteriosList.list();
	}
	
	
	
	
	

	/**
	 * CARGA LAS RECOMENDACION POR DETALLE RECOMENDACION SERVICIO PROGRAMA
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public RecomendacionesServProg consultaAvanzada(RecomendacionesServProg dtoReSerProg)
	{	
		
		
		Criteria criteriosList= sessionFactory.getCurrentSession().createCriteria(RecomendacionesServProg.class);
		
		
		/*
		 *CONSULTA POR CODIGO  
		 */
		if(dtoReSerProg.getCodigoPk() >0)
		{
			criteriosList.add(Restrictions.eq("codigoPk",dtoReSerProg.getCodigoPk()));
		}
		/*
		 *CONSULTAR POR INSTITUCION 
		 */
		if(dtoReSerProg.getInstituciones()!=null && dtoReSerProg.getInstituciones().getCodigo()>0)
		{
			criteriosList.add(Restrictions.eq("instituciones.codigo", dtoReSerProg.getInstituciones().getCodigo()));
		}
	
		
		/*
		 * TODO ITERACION PROGRAMAS SERVICIOS
		 */
		
		RecomendacionesServProg  dtoRecom=  (RecomendacionesServProg )criteriosList.uniqueResult();
		
		
		
		Iterator iterRecom= dtoRecom.getRecomendacionesContOdontos().iterator();
		
		while(iterRecom.hasNext())
		{
			RecomendacionesContOdonto dto=(RecomendacionesContOdonto) iterRecom.next();
			//dto.getDescripcion();
			//(dto.getCodigo();
		}
	
		
		
		
		Iterator iterProg = dtoRecom.getRecomSerproSerpros().iterator();
		
		while(iterProg.hasNext())
		{
			RecomSerproSerpro dtoPro = (RecomSerproSerpro)iterProg.next();
			dtoPro.getProgramas().getNombre();
			dtoPro.getServicios();
		}
		
		
		
		return dtoRecom;
	}





	
	
	

	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param dtoReSerProg
	 * @return
	 */
	private Criteria accionCargarRecomendacionSerProg( RecomendacionesServProg dtoReSerProg) 
	
	
	{
		Criteria criteriosList= sessionFactory.getCurrentSession().createCriteria(RecomendacionesServProg.class);
		/*
		 *FILTROS  
		 */
		/*
		 *CONSULTA POR CODIGO  
		 */
		if(dtoReSerProg.getCodigoPk() >0)
		{
			criteriosList.add(Restrictions.eq("codigoPk",dtoReSerProg.getCodigoPk()));
		}
		/*
		 *CONSULTAR POR INSTITUCION 
		 */
		if(dtoReSerProg.getInstituciones()!=null && dtoReSerProg.getInstituciones().getCodigo()>0)
		{
			criteriosList.add(Restrictions.eq("instituciones.codigo", dtoReSerProg.getInstituciones().getCodigo()));
		}
		/*
		 * TODO ITERACION PROGRAMAS SERVICIOS
		 */
		
		criteriosList.list();
		return criteriosList;
	}
	


}
