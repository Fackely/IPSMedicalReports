package com.servinte.axioma.dao.impl.historiaClinica;

import java.util.ArrayList;

import com.servinte.axioma.dao.interfaz.historiaClinica.IAntecedentesVariosDAO;
import com.servinte.axioma.orm.AntecedentesVarios;
import com.servinte.axioma.orm.delegate.historiaClinica.AntecedentesVariosDelegate;


/**
 * 
 * @author Cristhian Murillo
 *
 */
public class AntecedentesVariosHibernateDAO implements IAntecedentesVariosDAO{
	
	
	AntecedentesVariosDelegate antecedentesVariosDelegate;
	
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Cristhian Murillo
	 */
	public AntecedentesVariosHibernateDAO(){
		antecedentesVariosDelegate = new AntecedentesVariosDelegate();
	}


	@Override
	public ArrayList<AntecedentesVarios> obtenerAntecedentesVariosPaciente(Integer codPaciente) 
	{
		return antecedentesVariosDelegate.obtenerAntecedentesVariosPaciente(codPaciente);
	}
	

}
