package com.servinte.axioma.mundo.interfaz.tesoreria;

import com.servinte.axioma.orm.TipoCuentaBancaria;


/**
 * Define la l&oacute;gica de negocio relacionada con los Tipos de Cuenta
 * @author Diana Carolina G
 *
 */


public interface ITipoCuentaBancariaMundo {
	
	/**
	 * M&eacute;todo que recibe un Id y retorna un objeto
	 * @param id
	 * @return TipoCuentaBancaria
	 */
	
	public TipoCuentaBancaria findById(char id);
	

}
