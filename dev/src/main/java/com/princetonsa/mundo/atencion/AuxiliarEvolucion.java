/*
 * @(#)AuxiliarEvolucion.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.mundo.atencion;

/**
 * Esta clase permite manejar informaci�n b�sica de una evoluci�n,
 * teniendo en cuenta �nicamente los atributos importantes al 
 * momento de mostrar 
 * 
 *	@version 1.0, Jul 11, 2003
 */
public class AuxiliarEvolucion 
{

	/**
	 * Nombre del m�dico que hizo la evoluci�n
	 */
	private String nombreMedico="";
	
	/**
	 * Fecha de creaci�n de la evoluci�n
	 */
	private String fecha="";
	
	/**
	 * Hora de creaci�n de la evoluci�n
	 */
	private String hora="";
	
	/**
	 * N�mero de la evoluci�n
	 */
	private int numeroEvolucion=0;
	
	/**
	 * Boolean que indica si esta evoluci�n va o no a
	 * la epicrisis
	 */
	private boolean vaEpicrisis=true;

	/**
	 * Constructor vac�o de la clase AuxiliarEvolucion
	 */
	public AuxiliarEvolucion ()
	{
		this.clean();
	}

	/**
	 * M�todo que limpia la informaci�n presente en 
	 * este objeto
	 */
	public void clean()
	{
		nombreMedico="";
		fecha="";
		hora="";
		numeroEvolucion=0;
		vaEpicrisis=true;
	}
	/**
	 * @return
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * @return
	 */
	public String getHora() {
		return hora;
	}

	/**
	 * @return
	 */
	public String getNombreMedico() {
		return nombreMedico;
	}

	/**
	 * @return
	 */
	public int getNumeroEvolucion() {
		return numeroEvolucion;
	}

	/**
	 * @return
	 */
	public boolean isVaEpicrisis() {
		return vaEpicrisis;
	}

	/**
	 * @param string
	 */
	public void setFecha(String string) {
		fecha = string;
	}

	/**
	 * @param string
	 */
	public void setHora(String string) {
		hora = string;
	}

	/**
	 * @param string
	 */
	public void setNombreMedico(String string) {
		nombreMedico = string;
	}

	/**
	 * @param i
	 */
	public void setNumeroEvolucion(int i) {
		numeroEvolucion = i;
	}

	/**
	 * @param b
	 */
	public void setVaEpicrisis(boolean b) {
		vaEpicrisis = b;
	}

	/**
	 * Retorna la hora en formato de cinco caracteres
	 * (hh:mm)
	 * @return
	 */
	public String getHoraCincoCaracteres ()
	{
		if (hora!=null)
		{
			return hora.substring(0, 5);
		}
		else
		{
			return "";
		}
	}
}
