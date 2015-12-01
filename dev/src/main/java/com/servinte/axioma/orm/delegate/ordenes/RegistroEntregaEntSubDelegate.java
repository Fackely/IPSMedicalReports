/**
 * 
 */
package com.servinte.axioma.orm.delegate.ordenes;

import com.servinte.axioma.orm.RegistroEntregaEntSub;
import com.servinte.axioma.orm.RegistroEntregaEntSubHome;

/**
 * @author Cristhian Murillo
 *
 */
public class RegistroEntregaEntSubDelegate extends RegistroEntregaEntSubHome 
{
	
	/**
	 * Implementacion del findById
	 * @param id
	 * @return RegistroEntregaEntSub
	 */
	public RegistroEntregaEntSub obtenerRegistroEntregaEntSubPorId(int id) {
		return super.findById(id);
	}
	
	
	@Override
	public void attachDirty(RegistroEntregaEntSub instance) {
		super.attachDirty(instance);
	}
	
}
