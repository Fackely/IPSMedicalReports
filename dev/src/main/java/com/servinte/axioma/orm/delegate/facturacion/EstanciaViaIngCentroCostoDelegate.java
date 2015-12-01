package com.servinte.axioma.orm.delegate.facturacion;

import java.util.List;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.servinte.axioma.orm.EstanciaViaIngCentroCosto;
import com.servinte.axioma.orm.EstanciaViaIngCentroCostoHome;
import com.servinte.axioma.orm.MontosCobro;

/**
 * 
 * @author axioma
 *
 */
public class EstanciaViaIngCentroCostoDelegate extends EstanciaViaIngCentroCostoHome{
	
	
	
	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<EstanciaViaIngCentroCosto> listarEstanciasxEntidadesSubContratadas(Long idEntidadSubContratada){
		
		Criteria criterio= sessionFactory.getCurrentSession().createCriteria(EstanciaViaIngCentroCosto.class);
		criterio.add(Restrictions.eq("entidadesSubcontratadas.codigoPk", idEntidadSubContratada));
		
		List<EstanciaViaIngCentroCosto> lis=criterio.list();
		
		
		return lis;
		
	}
	
	public boolean guardarEstancia(EstanciaViaIngCentroCosto estancia){
		boolean save = true;					
		try{
			super.persist(estancia);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo guardar el registro de " +
					"Estancia Via Ingreso por Centro de Costo: ",e);
		}				
		return save;				
	}
	
	
	

}
