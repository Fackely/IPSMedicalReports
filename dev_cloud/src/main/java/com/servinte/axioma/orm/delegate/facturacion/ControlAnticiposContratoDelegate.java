package com.servinte.axioma.orm.delegate.facturacion;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import util.ConstantesBD;

import com.servinte.axioma.orm.ControlAnticiposContrato;
import com.servinte.axioma.orm.ControlAnticiposContratoHome;

/**
 * Esta clase se encarga de ejecutar los procesos
 * de negocio de la entidad ControlAnticiposContrato
 *
 * @author Cristhian Murillo
 */
@SuppressWarnings("unchecked")

public class ControlAnticiposContratoDelegate extends ControlAnticiposContratoHome 
{
	
	
	/**
	 * Determina si un contrato específico requiere anticipo
	 * 
	 * @param contrato
	 * @return ArrayList<ControlAnticiposContrato>
	 * 
	 * @author Cristhian Murillo
	 */
	public ArrayList<ControlAnticiposContrato> determinarContratoRequiereAnticipo (int contrato)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ControlAnticiposContrato.class, "controlAnticiposContrato");
		
		criteria.createAlias("controlAnticiposContrato.contratos"	, "contratos");
		
		criteria.add(Restrictions.eq("contratos.codigo"							, contrato));
		criteria.add(Restrictions.eq("controlAnticiposContrato.reqAntContPre"	, ConstantesBD.acronimoSiChar));
		
		return (ArrayList<ControlAnticiposContrato>) criteria.list();
	}
	
	
}
