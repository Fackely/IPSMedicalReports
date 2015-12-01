/*
 * Mayo 18, 2010
 */
package com.servinte.axioma.dao.fabrica.consultaexterna;

import com.servinte.axioma.dao.impl.consultaexterna.ConsultorioDAO;
import com.servinte.axioma.dao.impl.consultaexterna.MultasCitasHibernateDAO;
import com.servinte.axioma.dao.interfaz.consultaexterna.IConsultorioDAO;
import com.servinte.axioma.dao.interfaz.consultaexterna.IMultasCitasDAO;

/**
 * Fábrica para contruir objetos para el módulo de Consulta Externa.
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */
public abstract class ConsultaExternaFabricaDAO {
	
	private ConsultaExternaFabricaDAO(){}
	
	
	
	/**
	 * Crea y retorna un objeto que es implementación de 
	 * {@link IMultasCitasDAO}.
	 * 
	 * @return objeto que es implementación de {@link IMultasCitasDAO}.
	 */
	public static IMultasCitasDAO crearMultasCitasDAO()
	{
		return new MultasCitasHibernateDAO();
	}



	/**
	 * Crea y retorna un objeto que es implementación de 
	 * {@link IConsultorioDAO}.
	 * 
	 * @return objeto que es implementación de {@link IConsultorioDAO}.
	 */
	public static IConsultorioDAO crearConsultorioDAO()
	{
		return new ConsultorioDAO();
	}
}
