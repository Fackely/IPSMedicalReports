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

import com.princetonsa.dto.capitacion.DTOPrioridadOcupacionMedica;
import com.servinte.axioma.orm.PrioridadOcupMedica;
import com.servinte.axioma.orm.PrioridadOcupMedicaHome;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio
 * para la entidad PrioridadOcupacionMedica
 * @author Angela Aguirre
 *
 */
public class PrioridadOcupacionMedicaDelegate extends PrioridadOcupMedicaHome {
	

	/**
	 * 
	 * Este método se encarga de consultar las prioridades para los niveles de
	 * autorización por ocupación médica
	 * 
	 * @param int id
	 * @return ArrayList<DTOPrioridadOcupacionMedica>
	 * @author Angela Maria Aguirre
	 */
	public ArrayList<DTOPrioridadOcupacionMedica> 
			buscarPrioridadOcupacionMedicaPorNivelAutorID(int id){
		
		
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(PrioridadOcupMedica.class,"prioridadOcupacion");
		criteria.createAlias("prioridadOcupacion.nivelAutorOcupMedica", "nivelAutorOcupMedica");	
		//criteria.createAlias("prioridadOcupacion.centrosCostoEntidadesSub", "prioridadCentroCosto");
		
		criteria.add(Restrictions.eq("nivelAutorOcupMedica.codigoPk", id));
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("prioridadOcupacion.codigoPk"),"codigoPk")
				.add(Projections.property("nivelAutorOcupMedica.codigoPk"),"nivelAutorOcupMedicaID")
				//.add(Projections.property("prioridadCentroCosto.consecutivo"),"idPrioridad")
				.add(Projections.property("prioridadOcupacion.nroPrioridad"),"numeroPrioridad"));
				
		criteria.setResultTransformer(Transformers.aliasToBean(DTOPrioridadOcupacionMedica.class));	
		
		criteria.addOrder( Order.asc("prioridadOcupacion.nroPrioridad") );
		
		ArrayList<DTOPrioridadOcupacionMedica> listaMontos = 
			(ArrayList<DTOPrioridadOcupacionMedica>)criteria.list();
	 
		return listaMontos;
		
	}
	
	/**
	 * 
	 * Este método se encarga de eliminar las prioridades para los niveles de
	 * autorización por ocupación médica
	 * 
	 * @param int id
	 * @return boolean
	 * @author Angela Maria Aguirre
	 */
	public boolean eliminarPrioridadOcupacionMedica(int id){		
		boolean save = true;					
		try{
			PrioridadOcupMedica prioridad = super.findById(id);			
			super.delete(prioridad);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo eliminar el registro de " +
					"prioridad de nivel de autorización de una ocupación médica: ",e);
		}				
		return save;	
		
	}
	
	
	/**
	 * 
	 * Este método se encarga de consultar las prioridades para los niveles de
	 * autorización por ocupación médica, ordenados para la validacion de autorizaciones 
	 * 
	 * @param int id
	 * @return ArrayList<Integer>
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Integer> 
		obtenerNumerosPrioridadOcupacionMedicaOrdenadosPorNivelAutorizacion(int id){
		
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(PrioridadOcupMedica.class,"prioridadOcupacion");
		criteria.createAlias("prioridadOcupacion.nivelAutorOcupMedica", "nivelAutorOcupMedica");	
		//criteria.createAlias("prioridadOcupacion.centrosCostoEntidadesSub", "prioridadCentroCosto");
		
		criteria.add(Restrictions.eq("nivelAutorOcupMedica.codigoPk", id));
		
		criteria.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("prioridadOcupacion.nroPrioridad"),"numeroPrioridad")));
				
		criteria.setResultTransformer(Transformers.aliasToBean(DTOPrioridadOcupacionMedica.class));	
		
		criteria.addOrder( Order.asc("prioridadOcupacion.nroPrioridad") );
		
		ArrayList<DTOPrioridadOcupacionMedica> listaPrioridades = 
			(ArrayList<DTOPrioridadOcupacionMedica>)criteria.list();
	 
		ArrayList<Integer> numeroPrio=new ArrayList<Integer>();
		for(DTOPrioridadOcupacionMedica dtoPrioridadOcu:listaPrioridades){
			numeroPrio.add(dtoPrioridadOcu.getNumeroPrioridad());
		}
		
		return numeroPrio;
		
	}

}
