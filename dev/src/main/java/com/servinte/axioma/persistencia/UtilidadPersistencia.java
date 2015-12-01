package com.servinte.axioma.persistencia;

import com.servinte.axioma.persistencia.impl.hibernate.PersistenciaHibernate;
import com.servinte.axioma.persistencia.impl.hibernate.TransaccionHibernate;
import com.servinte.axioma.persistencia.interfaz.IPersistencia;
import com.servinte.axioma.persistencia.interfaz.ITransaccion;


/**
 * Encargada de interfazar los m&eacute;todos de persistencia
 * @author Juan David Ram&iacute;rez
 * @version 1.0.0
 * @since 23 Julio 2010
*/
public class UtilidadPersistencia {
	
	private static IPersistencia persistencia ;
	
	static 
	{
		persistencia = new PersistenciaHibernate();
	}
	
	/**
	 * 	Constructor privado para evitar la instanciaci&oacute;n de la clase
	 */
	private UtilidadPersistencia()
	{
		
	}
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static IPersistencia getPersistencia()
	{
		return persistencia;
	}
}
