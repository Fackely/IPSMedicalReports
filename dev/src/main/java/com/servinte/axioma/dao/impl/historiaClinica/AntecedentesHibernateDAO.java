package com.servinte.axioma.dao.impl.historiaClinica;

import java.util.ArrayList;

import com.servinte.axioma.dao.interfaz.historiaClinica.IAntecedentesDAO;
import com.servinte.axioma.orm.Antecedentes;
import com.servinte.axioma.orm.delegate.historiaClinica.AntecedentesDelegate;


/**
 * 
 * @author Cristhian Murillo
 *
 */
public class AntecedentesHibernateDAO implements IAntecedentesDAO
{
	
	
	AntecedentesDelegate antecedentesDelegate;
	
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Cristhian Murillo
	 */
	public AntecedentesHibernateDAO(){
		antecedentesDelegate = new AntecedentesDelegate();
	}


	@Override
	public ArrayList<Antecedentes> listarAntecedentes() {
		return antecedentesDelegate.listarAntecedentes();
	}

	

}
