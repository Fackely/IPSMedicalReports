/**
 * 
 */
package com.servinte.axioma.dao.impl.administracion;

import com.servinte.axioma.dao.interfaz.administracion.IConsecutivosSistemaDAO;
import com.servinte.axioma.orm.delegate.administracion.UsoConsecutivosDelegate;

/**
 * @author Juan David Ramírez
 * @since Jan 18, 2011
 */
public class ConsecutivosSistemaDAO implements IConsecutivosSistemaDAO
{

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.administracion.IConsecutivosSistemaDAO#inicializarConsecutivosDisponibles()
	 */
	@Override
	public boolean consultarConsecutivosDisponiblesInestables()
	{
		UsoConsecutivosDelegate usoConsecutivosDelegate=new UsoConsecutivosDelegate();
		return usoConsecutivosDelegate.liberarConsecutivosNoFinalizados();
	}

}
