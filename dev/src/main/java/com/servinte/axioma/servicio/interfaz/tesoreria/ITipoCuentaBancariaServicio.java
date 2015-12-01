package com.servinte.axioma.servicio.interfaz.tesoreria;

import com.servinte.axioma.orm.TipoCuentaBancaria;



/**
 * Define la l&oacute;gica de negocio relacionada con los Tipos de Cuenta Bancaria
 * @author Diana Carolina G
 *
 */

public interface ITipoCuentaBancariaServicio {

	/**
	 * M&eacute;todo que recibe un Id y retorna un objeto
	 * @param id
	 * @return TipoCuentaBancaria
	 */
	
	public TipoCuentaBancaria findById(char id);
	

}
