/**
 * 
 */
package com.servinte.axioma.orm.delegate.capitacion;

import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.princetonsa.dto.capitacion.DTOPrioridadUsuarioEspecifico;
import com.servinte.axioma.orm.PrioridadUsuEsp;
import com.servinte.axioma.orm.PrioridadUsuEspHome;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio
 * para la entidad PrioridadUsuarioEspecifico
 * @author Angela Aguirre
 *
 */
public class PrioridadUsuarioEspecificoDelegate extends PrioridadUsuEspHome {
	
	/**
	 * 
	 * Este método se encarga de consultar las prioridades para los niveles de
	 * autorización por usuario específico
	 * 
	 * @param int id
	 * @return ArrayList<DTOPrioridadUsuarioEspecifico>
	 * @author Angela Maria Aguirre
	 */
	public ArrayList<DTOPrioridadUsuarioEspecifico> 
			buscarPrioridadUsuarioEspecificoPorNivelAutorID(int id){
		
		
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(PrioridadUsuEsp.class,"prioridadUsuarioEspecifico");
		criteria.createAlias("prioridadUsuarioEspecifico.nivelAutorUsuEspec", "nivelAutorUsuEsp");	
		//criteria.createAlias("prioridadUsuarioEspecifico.centrosCostoEntidadesSub", "prioridadCentroCosto");
		
		criteria.add(Restrictions.eq("nivelAutorUsuEsp.codigoPk", id));
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("prioridadUsuarioEspecifico.codigoPk"),"codigoPk")
				.add(Projections.property("nivelAutorUsuEsp.codigoPk"),"nivelAutorUsuEspecID")
				.add(Projections.property("prioridadUsuarioEspecifico.nroPrioridad"),"numeroPrioridad"));
				
		criteria.setResultTransformer(Transformers.aliasToBean(DTOPrioridadUsuarioEspecifico.class));	
		
		
		criteria.addOrder( Order.asc("prioridadUsuarioEspecifico.nroPrioridad") );
		
		ArrayList<DTOPrioridadUsuarioEspecifico> listaMontos = 
			(ArrayList<DTOPrioridadUsuarioEspecifico>)criteria.list();
	 
		return listaMontos;
		
	}
	
	/**
	 * 
	 * Este método se encarga de eliminar las prioridades para los niveles de
	 * autorización por usuario específico
	 * 
	 * @param int id
	 * @return boolean
	 * @author Angela Maria Aguirre
	 */
	public boolean eliminarPrioridadUsuarioEspecifico(int id){		
		boolean save = true;					
		try{
			PrioridadUsuEsp prioridad = super.findById(id);			
			super.delete(prioridad);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo eliminar el registro de " +
					"prioridad de nivel de autorización de un usuario específico: ",e);
		}				
		return save;	
		
	}
	
	
	/**
	 * 
	 * Este método se encarga de consultar los numeros de prioridades para los niveles de
	 * autorización por usuario específico, ordenados para la validacion de autorizaciones 
	 * 
	 * @param int id
	 * @return ArrayList<Integer> Lista de Numeros Prioridades
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Integer> 
			obtenerNumerosPrioridadOrdenadosPorNivelAutorizacion(int id){
		
		
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(PrioridadUsuEsp.class,"prioridadUsuarioEspecifico");
		criteria.createAlias("prioridadUsuarioEspecifico.nivelAutorUsuEspec", "nivelAutorUsuEsp");	
		//criteria.createAlias("prioridadUsuarioEspecifico.centrosCostoEntidadesSub", "prioridadCentroCosto");
		
		criteria.add(Restrictions.eq("nivelAutorUsuEsp.codigoPk", id));
		
		criteria.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("prioridadUsuarioEspecifico.nroPrioridad"),"numeroPrioridad")));
				
		criteria.setResultTransformer(Transformers.aliasToBean(DTOPrioridadUsuarioEspecifico.class));	
		
		criteria.addOrder( Order.asc("prioridadUsuarioEspecifico.nroPrioridad") );
		
		ArrayList<DTOPrioridadUsuarioEspecifico> listaNumerosPrioridad = 
			(ArrayList<DTOPrioridadUsuarioEspecifico>)criteria.list();
		
		ArrayList<Integer> numeroPrio=new ArrayList<Integer>();
		for(DTOPrioridadUsuarioEspecifico dtoPrioridadUsu:listaNumerosPrioridad){
			numeroPrio.add(dtoPrioridadUsu.getNumeroPrioridad());
		}
		
	 
		return numeroPrio;
		
	}

}
