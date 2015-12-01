/**
 * 
 */
package com.servinte.axioma.orm.delegate.ordenes;

import com.servinte.axioma.orm.SolDespachoSinAuto;
import com.servinte.axioma.orm.SolDespachoSinAutoHome;

/**
 * @author Cristhian Murillo
 */
public class SolDespachoSinAutoDelegate extends SolDespachoSinAutoHome 
{
	
	/**
	 * Implementación del metodo attachDirty
	 * @param instance
	 */
	public void attachDirtySolDespachoSinAuto(SolDespachoSinAuto instance) {
		super.attachDirty(instance);
	}
	
}
