/**
 * 
 */
package com.servinte.axioma.orm.delegate.odontologia;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;

import com.princetonsa.dto.odontologia.DtoPaquetesOdontologicosConvenio;
import com.servinte.axioma.orm.PaqOdontConvenio;
import com.servinte.axioma.orm.PaqOdontConvenioHome;

/**
 * @author armando
 *
 */
public class PaqOdontConvenioDelegate extends PaqOdontConvenioHome {

	public PaqOdontConvenio consultarPaquetesOdontologicosConvenioContrato(int codigoConvenio,int codigoContrato) 
	{
		Criteria criteria= sessionFactory.getCurrentSession().createCriteria(PaqOdontConvenio.class);
		if(codigoContrato>0)
		{
			criteria.add(Expression.eq("contratos.codigo", codigoContrato));
		}
		if(codigoConvenio>0)
		{
			criteria.add(Expression.eq("convenios.codigo", codigoConvenio));
		}
		
		
		return (PaqOdontConvenio)criteria.uniqueResult();
	}

}
