/**
 * 
 */
package com.servinte.axioma.orm.delegate.tesoreria;

import java.util.ArrayList;

import com.servinte.axioma.orm.TarjetasFinancieras;
import com.servinte.axioma.orm.TiposTarjetaFinanciera;
import com.servinte.axioma.orm.TiposTarjetaFinancieraHome;

/**
 * @author armando
 *
 */
public class TiposTarjetaFinancieraDelegate extends TiposTarjetaFinancieraHome 
{

	/**
	 * 
	 * @return
	 */
	public ArrayList<TiposTarjetaFinanciera> listarTiposTarjetasFinancieras() 
	{
		return (ArrayList<TiposTarjetaFinanciera>) sessionFactory.getCurrentSession().createCriteria(TiposTarjetaFinanciera.class).list();
	}

}
