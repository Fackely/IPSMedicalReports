/*
 * @(#)ProcedimientosIterator.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.mundo.atencion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.axioma.util.log.Log4JManager;

import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.ordenesmedicas.solicitudes.Procedimiento;

/**
 * Este <code>Iterator</code> me permite recorrer las 
 * respuestas de procedimientos de un paciente.
 * Cada invocaci�n de next(), carga la siguiente y 
 * la retorna como un <code>Object</code>.
 * 
 *	@version 1.0, 26/08/2004
 */
public class ProcedimientosIterator implements Iterator
{
	/**
	 * �ndice de los procedimientos que recorrer� este Iterator.
	 */
	private int length;

	/**
	 * �ndice de los procedimientos en los que estamos parados.
	 */
	private int index;

	/**
	 * Arreglo con los n�meros de solicitud sobre las que iteraremos.
	 * (El n�mero de solicitud controla los procedimientos)
	 */
	private ArrayList procedimientos;
	
	/**
	 * Crea un nuevo <code>Iterator</code> de procedimientos.
	 * @param procedimientos arreglo con los n�meros de 
	 * solicitud de las respuestas de procedimientos
	 */
	public ProcedimientosIterator (ArrayList procedimientos)
	{
	    this.index=0;
	    this.procedimientos=procedimientos;
	    this.length=procedimientos.size();
	}

	/**
	 * Indica si todav�a quedan solicitudes por iterar.
	 * @return <b>true</b> si a�n quedan procedimientos, <b>false</b> si no
	 */
	public boolean hasNext() 
	{
		return (index < length);
	}

	/**
	 * Retorna el siiguiente procedimiento, como un 
	 * <code>Object</code>.,
	 * @return el siguiente procedimiento
	 */
	public Object next() 
	{
		Connection con=null;
		boolean wasLoaded = false;
		Procedimiento procedimiento=new Procedimiento();
		try
		{
		    con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
			procedimiento.setNumeroSolicitud(((Integer)procedimientos.get(index++)).intValue());
			wasLoaded=(procedimiento.cargar(con, procedimiento.getNumeroSolicitud(), false)).isTrue();
			if (wasLoaded) 
			{
				return procedimiento;
			}else 
			{
				return null;
			}
					
		}
		catch (Exception e) 
		{
				Log4JManager.error(e);
		}	
		finally 
		{
			UtilidadBD.closeConnection(con);
		}
		return null;
		
	}
	/**
	 * M�todo vac�o, esta implementaci�n de Iterator no lo utiliza.
	 */
	public void remove() 
	{
	}
}
