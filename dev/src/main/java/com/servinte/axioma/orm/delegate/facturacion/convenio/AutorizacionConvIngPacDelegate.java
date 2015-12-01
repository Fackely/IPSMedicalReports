package com.servinte.axioma.orm.delegate.facturacion.convenio;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.servinte.axioma.orm.AutorizacionConvIngPac;
import com.servinte.axioma.orm.AutorizacionConvIngPacHome;


/**
 * Esta clase se encarga de de manejar las transaccciones relacionadas con AutorizacionConvIngPacHome
 * 
 * @author Cristhian Murillo
 *
 */
public class AutorizacionConvIngPacDelegate extends AutorizacionConvIngPacHome  {

	
	
	/**
	 * Retorna la ultima validacion en base de datos para el convenio ingreso paciente dado.
	 * 
	 * @author Cristhian Murillo
	 * @param conveniosIngresoPacientePk
	 * 
	 * @return ArrayList<AutorizacionConvIngPac>
	 */
	public AutorizacionConvIngPac obtenerUltimaAutorizacion(long conveniosIngresoPacientePk)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(AutorizacionConvIngPac.class, "autorizacionConvIngPac");
		
		criteria.createAlias("autorizacionConvIngPac.conveniosIngresoPaciente"	, "conveniosIngresoPaciente");
		
		criteria.add(Restrictions.eq("conveniosIngresoPaciente.codigoPk", conveniosIngresoPacientePk));
		
		criteria.addOrder( Order.desc("fechaModifica") );
		criteria.addOrder( Order.desc("horaModifica") );
		
		criteria.setMaxResults(1);
		
		return (AutorizacionConvIngPac) criteria.uniqueResult();
	}
	
}
