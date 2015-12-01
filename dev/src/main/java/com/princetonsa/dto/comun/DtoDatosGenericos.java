package com.princetonsa.dto.comun;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Genérico para transferir cualqueir información.
 *  
 * @author Cristhian Murillo
*/
public class DtoDatosGenericos implements Serializable
{

	/** * Serial  */
	private static final long serialVersionUID = 1L;
	
	/** * Dato campo 1 */
	private String dato1;
	
	/** * Dato campo 2 */
	private String dato2;
	
	/** * Fecha */
	private Date fecha;
	
	/** * Hora */
	private String hora;

	/** * Lista de datos */
	ArrayList<DtoDatosGenericos> listaDatos;
	
	/** * Bandera */
	private boolean flag;
	
	
	/**
	 * Constructor de la clase
	 */
	public DtoDatosGenericos() 
	{
		this.dato1 		= null;
		this.dato2 		= null;
		this.listaDatos = new ArrayList<DtoDatosGenericos>();
		this.fecha		= null;
		this.hora		= null;
		this.flag 		=  false;
	}

	/**
	 * @return valor de dato1
	 */
	public String getDato1() {
		return dato1;
	}

	/**
	 * @param dato1 el dato1 para asignar
	 */
	public void setDato1(String dato1) {
		this.dato1 = dato1;
	}

	/**
	 * @return valor de dato2
	 */
	public String getDato2() {
		return dato2;
	}

	/**
	 * @param dato2 el dato2 para asignar
	 */
	public void setDato2(String dato2) {
		this.dato2 = dato2;
	}

	/**
	 * @return valor de listaDatos
	 */
	public ArrayList<DtoDatosGenericos> getListaDatos() {
		return listaDatos;
	}

	/**
	 * @param listaDatos el listaDatos para asignar
	 */
	public void setListaDatos(ArrayList<DtoDatosGenericos> listaDatos) {
		this.listaDatos = listaDatos;
	}

	/**
	 * @return valor de fecha
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * @param fecha el fecha para asignar
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return valor de hora
	 */
	public String getHora() {
		return hora;
	}

	/**
	 * @param hora el hora para asignar
	 */
	public void setHora(String hora) {
		this.hora = hora;
	}

	/**
	 * @return valor de flag
	 */
	public boolean isFlag() {
		return flag;
	}

	/**
	 * @param flag el flag para asignar
	 */
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
}
