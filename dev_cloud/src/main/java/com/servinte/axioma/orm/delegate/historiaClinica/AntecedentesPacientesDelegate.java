package com.servinte.axioma.orm.delegate.historiaClinica;

import java.util.ArrayList;

import org.hibernate.Criteria;

import com.servinte.axioma.orm.AntecedentesPacientes;
import com.servinte.axioma.orm.AntecedentesPacientesHome;


/**
 * @author Cristhian Murillo
 */
public class AntecedentesPacientesDelegate extends AntecedentesPacientesHome
{
	
	
	/**
	 * Consulta todos los tipos de antecedentes
	 * 
	 * @return ArrayList<AntecedentesPacientes>
	 *
	 * @autor Cristhian Murillo
	 *
	*/
	@SuppressWarnings("unchecked")
	public ArrayList<AntecedentesPacientes> obtenerAntecedentes()
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(AntecedentesPacientes.class, "antecedentesPacientes");
		ArrayList<AntecedentesPacientes> lista = (ArrayList<AntecedentesPacientes>)criteria.list();
	
		return lista;	
	}
	
}
