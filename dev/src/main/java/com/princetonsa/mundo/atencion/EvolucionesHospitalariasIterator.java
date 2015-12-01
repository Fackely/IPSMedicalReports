/*
 * @(#)EvolucionesHospitalariasIterator.java
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
 * Este <code>Iterator</code> me permite recorrer las evoluciones hospitalarias de un paciente.
 * Cada invocación de next(), carga la siguiente y la retorna como un <code>Object</code>.
 *
 * @version Jul 3, 2003
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>
 */

public class EvolucionesHospitalariasIterator implements Iterator {

	/**
	 * Número de evoluciones que recorrerá este Iterator.
	 */
	private int length;

	/**
	 * Índice de la evolución en la que estamos parados.
	 */
	private int index;

	/**
	 * Arreglo con los números de solicitud de las evoluciones sobre las que iteraremos.
	 */
	private ArrayList evolucionesHospitalarias;

	/**
	 * Crea un nuevo <code>Iterator</code> de evoluciones hospitalarias.
	 * @param evolucionesHospitalarias arreglo con los números de solicitud de las evoluciones
	 */
	public EvolucionesHospitalariasIterator (ArrayList evolucionesHospitalarias) throws Exception {

		this.index = 0;
		this.length = evolucionesHospitalarias.size();
		this.evolucionesHospitalarias = evolucionesHospitalarias;

	}

	/**
	 * Indica si todavía quedan evoluciones por iterar.
	 * @return <b>true</b> si aún quedan evoluciones, <b>false</b> si no
	 */
	public boolean hasNext() {
		return (index < length);
	}

	/**
	 * Retorna la siguiente evolución hospitalaria, como un <code>Object</code>.
	 * @return la siguiente evolución hospitalaria
	 */
	public Object next() {

		boolean wasLoaded = false;
		EvolucionHospitalaria evolucionHospitalaria=new EvolucionHospitalaria();
		try 
		{
			Connection con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
			
			wasLoaded = evolucionHospitalaria.cargar(con, ((Integer)evolucionesHospitalarias.get(index++)).intValue());
			UtilidadBD.closeConnection(con);
		}	
		catch (SQLException sqle) {
				sqle.printStackTrace();
		}	finally {
				if (wasLoaded) {
					return evolucionHospitalaria;
				}
				else {
					return null;
				}
		}

	}

	/**
	 * Método vacío, esta implementación de Iterator no lo utiliza.
	 */
	public void remove() {
	}

}