package com.servinte.axioma.orm.delegate.historiaClinica;

import java.util.ArrayList;

import org.hibernate.Criteria;

import com.servinte.axioma.orm.Antecedentes;
import com.servinte.axioma.orm.AntecedentesHome;



/**
 * @author Cristhian Murillo
 */
public class AntecedentesDelegate extends AntecedentesHome
{
	
	/**
	 * Consulta todos los antecedentes del sistema
	 * 
	 * @param codPaciente
	 * @return ArrayList<Antecedentes>
	 *
	 * @autor Cristhian Murillo
	 *
	*/
	@SuppressWarnings("unchecked")
	public ArrayList<Antecedentes> listarAntecedentes()
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Antecedentes.class, "antecedentes");
		ArrayList<Antecedentes> lista = (ArrayList<Antecedentes>)criteria.list();
	
		return lista;	
	}
	

}
