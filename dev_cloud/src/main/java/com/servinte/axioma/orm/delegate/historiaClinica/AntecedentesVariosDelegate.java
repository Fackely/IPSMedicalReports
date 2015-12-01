package com.servinte.axioma.orm.delegate.historiaClinica;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.servinte.axioma.orm.AntecedentesVarios;
import com.servinte.axioma.orm.AntecedentesVariosHome;


/**
 * @author Cristhian Murillo
 */
public class AntecedentesVariosDelegate extends AntecedentesVariosHome
{
	
	
	/**
	 * Consulta los antecedentes varios de un paciente.
	 * 
	 * @param codPaciente
	 * @return ArrayList<AntecedentesVarios>
	 *
	 * @autor Cristhian Murillo
	 *
	*/
	@SuppressWarnings("unchecked")
	public ArrayList<AntecedentesVarios> obtenerAntecedentesVariosPaciente(Integer codPaciente)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(AntecedentesVarios.class, "antecedentesVarios");
		
		criteria.createAlias("antecedentesVarios.antecedentes", "antecedentes");
		criteria.add(Restrictions.eq("antecedentesVarios.id.codigoPaciente", codPaciente));
		criteria.addOrder(Order.asc("antecedentesVarios.descripcion"));
		
		ArrayList<AntecedentesVarios> lista = (ArrayList<AntecedentesVarios>)criteria.list();
	
		return lista;	
	}
	
}
