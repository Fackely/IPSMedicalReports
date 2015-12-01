/*
 * Mayo 18, 2010
 */
package com.servinte.axioma.dao.fabrica.consultaexterna;

import com.servinte.axioma.dao.impl.consultaexterna.ConsultorioDAO;
import com.servinte.axioma.dao.impl.consultaexterna.MultasCitasHibernateDAO;
import com.servinte.axioma.dao.interfaz.consultaexterna.IConsultorioDAO;
import com.servinte.axioma.dao.interfaz.consultaexterna.IMultasCitasDAO;

/**
 * F�brica para contruir objetos para el m�dulo de Consulta Externa.
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */
public abstract class ConsultaExternaFabricaDAO {
	
	private ConsultaExternaFabricaDAO(){}
	
	
	
	/**
	 * Crea y retorna un objeto que es implementaci�n de 
	 * {@link IMultasCitasDAO}.
	 * 
	 * @return objeto que es implementaci�n de {@link IMultasCitasDAO}.
	 */
	public static IMultasCitasDAO crearMultasCitasDAO()
	{
		return new MultasCitasHibernateDAO();
	}



	/**
	 * Crea y retorna un objeto que es implementaci�n de 
	 * {@link IConsultorioDAO}.
	 * 
	 * @return objeto que es implementaci�n de {@link IConsultorioDAO}.
	 */
	public static IConsultorioDAO crearConsultorioDAO()
	{
		return new ConsultorioDAO();
	}
}
