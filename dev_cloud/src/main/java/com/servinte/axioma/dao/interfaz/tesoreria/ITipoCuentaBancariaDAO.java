package com.servinte.axioma.dao.interfaz.tesoreria;


import com.servinte.axioma.orm.TipoCuentaBancaria;


/**
 * 
 * Interfaz donde se define el comportamiento de los
 * DAO's para las funciones de Tipo de Cuenta bancaria
 * 
 * @author Diana Carolina G
 *
 */

public interface ITipoCuentaBancariaDAO {
	
	/**
	 * M&eacute;todo que recibe un Id y retorna un objeto
	 * @param id
	 * @return TipoCuentaBancaria
	 */
	public TipoCuentaBancaria findById(char id);
	
	
}
