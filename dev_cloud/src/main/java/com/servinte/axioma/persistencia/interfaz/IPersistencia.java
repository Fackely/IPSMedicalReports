package com.servinte.axioma.persistencia.interfaz;

import java.sql.Connection;



/**
 * Encargada de interfazar los m&eacute;todos de persistencia
 * @author Juan David Ram&iacute;rez
 * @version 1.0.0
 * @since 23 Julio 2010
 */
public interface IPersistencia {

	/**
	 * Obtener la conexción de Hibernate con la BD
	 * @return
	 */
	public Connection obtenerConexion();
	
	/**
	 * Alterar la sessión
	 * @return true en caso de proceso exitoso, false de lo contrario
	 */
	public boolean alterarSession();

}
