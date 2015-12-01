package com.servinte.axioma.dao.impl.tesoreria;

import com.servinte.axioma.orm.HistTarjFinancieraComision;
import com.servinte.axioma.orm.HistTarjFinancieraComisionHome;

/**
 * Implementación de la interfaz {@link HistTarjFinancieraComisionHome}.
 * 
 * @author Cristhian Murillo
 */
public class HistTarjFinancieraComisionHibernateDAO extends HistTarjFinancieraComisionHome
{
	
	
	/**
	 * Realiza la persistencia de la Instancia
	 * @param transientInstance
	 */
	public void guardarHistTarjFinancieraComision(HistTarjFinancieraComision transientInstance) {
		super.persist(transientInstance);
	}
	
	
}
