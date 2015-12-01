package com.servinte.axioma.dao.impl.tesoreria;

import com.servinte.axioma.orm.HistComisionXCentroAten;
import com.servinte.axioma.orm.HistComisionXCentroAtenHome;

/**
 * Implementación de la interfaz {@link HistComisionXCentroAtenHome}.
 * 
 * @author Cristhian Murillo
 */
public class HistComisionXCentroAtenHibernateDAO extends HistComisionXCentroAtenHome
{
	
	/**
	 * Realiza la persistencia de la Instancia
	 * @param transientInstance
	 */
	public void guardarHistComisionXCentroAten(HistComisionXCentroAten transientInstance) {
		super.persist(transientInstance);
	}
	
}
