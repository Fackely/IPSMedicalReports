/**
 * 
 */
package com.servinte.axioma.orm.delegate.inventario;

import com.servinte.axioma.orm.Despacho;
import com.servinte.axioma.orm.DespachoHome;

/**
 * @author Cristhian Murillo
 *
 */
public class DespachoDelegate extends DespachoHome 
{
	
	/**
	 * Implementacion del findById
	 * @param id
	 * @return Despacho
	 */
	public Despacho obtenerDespachoPorId(int id) {
		return super.findById(id);
	}

	
	@Override 
	public void attachDirty(Despacho instance) {
		super.attachDirty(instance);
	}
}
