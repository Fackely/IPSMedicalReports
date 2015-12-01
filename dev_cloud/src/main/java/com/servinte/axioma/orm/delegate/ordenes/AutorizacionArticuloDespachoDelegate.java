/**
 * 
 */
package com.servinte.axioma.orm.delegate.ordenes;

import com.servinte.axioma.orm.AutorizacionArticuloDespacho;
import com.servinte.axioma.orm.AutorizacionArticuloDespachoHome;

/**
 * @author Cristhian Murillo
 *
 */
public class AutorizacionArticuloDespachoDelegate extends  AutorizacionArticuloDespachoHome 
{
	
	/**
	 * Implementacion del findById
	 * @param id
	 * @return  AutorizacionArticuloDespacho
	 */
	public  AutorizacionArticuloDespacho obtenerArticuloPorId(int id) {
		return super.findById(id);
	}
	
	
	@Override
	public void attachDirty(AutorizacionArticuloDespacho instance) {
		super.attachDirty(instance);
	}

}
