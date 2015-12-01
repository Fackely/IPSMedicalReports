/*
 * @(#)EvolucionesIterator.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.mundo.atencion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;

/**
 * Este <code>Iterator</code> me permite recorrer las evoluciones de un paciente.
 * Cada invocaci�n de next(), carga la siguiente y la retorna como un <code>Object</code>.
 *
 * @version Jul 3, 2003
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>
 */

public class EvolucionesIterator implements Iterator {

	/**
	 * N�mero de evoluciones que recorrer� este Iterator.
	 */
	private int length;

	/**
	 * �ndice de la evoluci�n en la que estamos parados.
	 */
	private int index;

	/**
	 * Arreglo con los n�meros de solicitud de las evoluciones sobre las que iteraremos.
	 */
	private ArrayList evoluciones;

	/**
	 * Crea un nuevo <code>Iterator</code> de evoluciones.
	 * @param evoluciones arreglo con los n�meros de solicitud de las evoluciones
	 */
	public EvolucionesIterator (ArrayList evoluciones) throws Exception {

		this.index = 0;
		this.length = evoluciones.size();
		this.evoluciones = evoluciones;
	}

	/**
	 * Indica si todav�a quedan evoluciones por iterar.
	 * @return <b>true</b> si a�n quedan evoluciones, <b>false</b> si no
	 */
	public boolean hasNext() {
		return (index < length);
	}

	/**
	 * Retorna la siguiente evoluci�n, como un <code>Object</code>.
	 * @return la siguiente evoluci�n
	 */
	public Object next() {

		boolean wasLoaded = false;

		Evolucion evolucion=new Evolucion();
		try {
			Connection con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
			wasLoaded = evolucion.cargar(con, ((Integer)evoluciones.get(index++)).intValue());
			UtilidadBD.closeConnection(con);
		}	catch (SQLException sqle) {
				sqle.printStackTrace();
		}	finally {
				if (wasLoaded) {
					return evolucion;
				}
				else {
					return null;
				}
		}

	}

	/**
	 * M�todo vac�o, esta implementaci�n de Iterator no lo necesita.
	 */
	public void remove() {
	}

}