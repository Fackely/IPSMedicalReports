package com.servinte.axioma.persistencia.interfaz;

/**
 * 
 * @author axioma
 *
 */
public interface ITransaccion {
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 */
	public void begin();
		
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 */
	public void commit();
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 */
	public void rollback();
	
	/**
	 * Indica si la transacci&oacute;n es activa o no
	 * @return true en caso de tener una transacci&oacute;n activa, false de lo contrario 
	 */
	public boolean isActive();
	
	/**
	 * Indica si la sesión se encuentra abierta
	 */
	public boolean isSessionOpen();

	
	/**
	 * Forza la transaccion a que se ejecute
	 * @author Ctisthian Murillo
	 */
	public void flush();
	
	
}
