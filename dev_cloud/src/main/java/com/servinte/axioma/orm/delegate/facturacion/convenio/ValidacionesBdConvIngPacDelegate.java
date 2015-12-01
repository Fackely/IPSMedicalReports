package com.servinte.axioma.orm.delegate.facturacion.convenio;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.servinte.axioma.orm.ValidacionesBdConvIngPac;
import com.servinte.axioma.orm.ValidacionesBdConvIngPacHome;



/**
 * Esta clase se encarga de de manejar las transaccciones relacionadas con ValidacionesBdConvIngPac
 * 
 * @author Cristhian Murillo
 *
 */
public class ValidacionesBdConvIngPacDelegate extends ValidacionesBdConvIngPacHome  {

	
	
	/**
	 * Retorna la ultima validacion en base de datos para el convenio ingreso paciente dado
	 * 
	 * @author Cristhian Murillo
	 * @param conveniosIngresoPacientePk
	 * 
	 * @return ValidacionesBdConvIngPac
	 */
	public ValidacionesBdConvIngPac obtenerUltimaValidacionBd(long conveniosIngresoPacientePk)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ValidacionesBdConvIngPac.class, "validacionesBdConvIngPac");
		
		criteria.createAlias("validacionesBdConvIngPac.conveniosIngresoPaciente"	, "conveniosIngresoPaciente");
		
		criteria.add(Restrictions.eq("conveniosIngresoPaciente.codigoPk", conveniosIngresoPacientePk));
		
		criteria.addOrder( Order.desc("fechaModifica") );
		criteria.addOrder( Order.desc("horaModifica") );
		
		criteria.setMaxResults(1);
		
		return (ValidacionesBdConvIngPac) criteria.uniqueResult();
	}
	
	
	
	
	
	
}
