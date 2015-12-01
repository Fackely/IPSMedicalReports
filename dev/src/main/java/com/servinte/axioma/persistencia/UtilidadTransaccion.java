package com.servinte.axioma.persistencia;

import com.servinte.axioma.persistencia.impl.hibernate.TransaccionHibernate;
import com.servinte.axioma.persistencia.interfaz.ITransaccion;


/**
 * 
 * @author Edgar Carvajal
 *
 */
public class UtilidadTransaccion {
	
	private static ITransaccion transaccion ;
	
	static 
	{
		transaccion = new TransaccionHibernate();
	}
	
	/**
	 * 	Constructor privado para evitar la instanciaci&oacute;n de la clase
	 */
	private UtilidadTransaccion()
	{
		
	}
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static ITransaccion getTransaccion()
	{
		return transaccion;
	}
}
