package com.servinte.axioma.orm.delegate.odontologia.contrato;

import java.util.List;


import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.ContratoOdontologico;
import com.servinte.axioma.orm.FirmasContOtrsiempr;
import com.servinte.axioma.orm.FirmasContOtrsiemprHome;

public class FirmasContratoMultiEmpDelegate  extends FirmasContOtrsiemprHome{

	
	
	
	
	/**
	 * cargar Firmas Por Instucion
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	  public List<FirmasContOtrsiempr> cargarFirmasPorInstucion(FirmasContOtrsiempr firmas)
	  {
		
		  Criteria criterio =sessionFactory.getCurrentSession().createCriteria(FirmasContOtrsiempr.class);
		  
		  
		  if(firmas.getEmpresasInstitucion()!=null && firmas.getEmpresasInstitucion().getCodigo()>0)
		  {
			  criterio.add(Restrictions.eq("empresasInstitucion.codigo", firmas.getEmpresasInstitucion().getCodigo() ));
			  
		  }
		  
		  List<FirmasContOtrsiempr> listaFirmas= criterio.list();
		  
		 
		  
		  
		  return listaFirmas ;
	  } 
	 
	 
	
	  /**
		 * 
		 * @author Edgar Carvajal Ruiz
		 * @param instance
		 */
/*		public void insertarOModificar(FirmasContOtrsiempr instance) {
			
			Session session =HibernateUtil.getSession();
			try 
			{
				session.saveOrUpdate(instance);
			} 
			catch (RuntimeException re) 
			{
				
				throw re;
			}
		}*/
	
}
