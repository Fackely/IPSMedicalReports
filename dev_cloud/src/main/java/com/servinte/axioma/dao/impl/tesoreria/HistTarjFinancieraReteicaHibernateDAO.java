package com.servinte.axioma.dao.impl.tesoreria;

import com.servinte.axioma.orm.HistTarjFinancieraReteica;
import com.servinte.axioma.orm.HistTarjFinancieraReteicaHome;

/**
 * Implementación de la interfaz {@link HistTarjFinancieraReteicaHome}.
 * 
 * @author Cristhian Murillo
 */
public class HistTarjFinancieraReteicaHibernateDAO extends HistTarjFinancieraReteicaHome
{
	
	/**
	 * Realiza la persistencia de la Instancia
	 * @param transientInstance
	 */
	public void guardarHistTarjFinancieraReteica(HistTarjFinancieraReteica transientInstance) 
	{
		super.persist(transientInstance);
	}
	
	
}
