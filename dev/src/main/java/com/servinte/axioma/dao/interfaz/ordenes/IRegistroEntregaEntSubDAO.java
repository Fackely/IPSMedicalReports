package com.servinte.axioma.dao.interfaz.ordenes;

import com.servinte.axioma.orm.RegistroEntregaEntSub;



/**
 * Interfaz donde se define el comportamiento de los
 * DAO's para las funciones de IRegistroEntregaEntSubDAO.java
 * 
 * @author Cristhian Murillo
 */
public interface IRegistroEntregaEntSubDAO 
{
	
	public void attachDirty(RegistroEntregaEntSub instance);
}
