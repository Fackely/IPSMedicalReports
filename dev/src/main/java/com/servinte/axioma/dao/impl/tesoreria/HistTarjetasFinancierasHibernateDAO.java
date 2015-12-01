package com.servinte.axioma.dao.impl.tesoreria;

import com.servinte.axioma.orm.HistTarjetasFinancieras;
import com.servinte.axioma.orm.HistTarjetasFinancierasHome;

/**
 * Implementación de la interfaz {@link HistTarjetasFinancierasHome}.
 * 
 * @author Cristhian Murillo
 */
public class HistTarjetasFinancierasHibernateDAO extends HistTarjetasFinancierasHome
{
	
	/**
	 * Realiza la persistencia de la Instancia
	 * @param transientInstance
	 */
	public void guardarHistTarjetasFinancieras(HistTarjetasFinancieras transientInstance) 
	{
		super.persist(transientInstance);
	}
	
}
