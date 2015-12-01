/**
 * 
 */
package com.servinte.axioma.orm.delegate.administracion;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Query;

import util.ConstantesBD;

import com.servinte.axioma.orm.UsoConsecutivosHome;

/**
 * @author Juan David Ram�rez
 * @since Jan 19, 2011
 */
public class UsoConsecutivosDelegate extends UsoConsecutivosHome
{

	/**
	 * M�todo para liberar los consecutivos que por alg�n motivo
	 * no fueron liberados durante la transacci�n en caso de haberse caido el sistema
	 */
	public boolean liberarConsecutivosNoFinalizados()
	{
		try{
			String hql = 
					"UPDATE UsoConsecutivos " +
						"SET usado=:usado " +
					"WHERE finalizado= :finalizado";
	        Query query = sessionFactory.getCurrentSession().createQuery(hql);
	        query.setString("usado",ConstantesBD.acronimoSi);
	        query.setString("finalizado",ConstantesBD.acronimoNo);
	        
	        if(query.executeUpdate()<0)
	        {
	        	Log4JManager.error("Error liberando los consecutivos");
	        	return false;
	        }
		}
		catch (Exception e) {
        	Log4JManager.error("Error liberando los consecutivos",e);
        	return false;
		}
		return true;
		
	}
}
