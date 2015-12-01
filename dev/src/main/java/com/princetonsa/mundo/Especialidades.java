/*
 * @(#)Especialidades.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.mundo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import util.Encoder;
import util.EspecialidadesBean;
import util.UtilidadTexto;

/**
 * Clase contenedora de objetos <code>Especialidad</code>.
 *
 * @version 1.0, Oct 27, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class Especialidades {

	/**
	 * Lista de especialidades, como un objeto <code>ArrayList</code>.
	 */
	private static ArrayList especialidades;

	/**
	 * Constructora de objetos <code>Especialidades</code>
	 */
	public Especialidades () {
		especialidades = new ArrayList();
	}

	/**
	 * Constructora de objetos <code>Especialidades</code>
	 * @param eb objeto <code>EspecialidadesBean</code> que inicializa el estado interno de un objeto <code>Especialidades</code>
	 */
	public Especialidades (EspecialidadesBean eb) {

		especialidades = new ArrayList();
		Set specs = eb.getActuales();
		Iterator i = specs.iterator();
		String [] resp;
		String llave="";


		while (i.hasNext()) {
			llave=(String)i.next();
			resp = UtilidadTexto.separarNombresDeCodigos(llave,1);
			
			if (eb.getActualesActivas().get(llave)!=null&&(   (String)(eb.getActualesActivas().get(llave))   ).equals("true")  )
			{
				especialidades.add(new Especialidad(resp[0], resp[1], true));
			}
			else
			{
				especialidades.add(new Especialidad(resp[0], resp[1], false));
			}
		}

	}

	/**
	 * Añade un nuevo objeto <code>Especialidad</code> inactiva
	 * en el sistema a esta contenedora de especialidades.
	 * @param codigo código de la nueva especialidad
	 * @param nombre nombre de la nueva especialidad
	 */
	public void setEspecialidad (String codigo, String nombre) 
	{
		especialidades.add(new Especialidad(codigo, nombre, false));
	}
	
	/**
	 * Añade un nuevo objeto <code>Especialidad</code> 
	 * en el sistema a esta contenedora de especialidades.
	 * @param codigo código de la nueva especialidad
	 * @param nombre nombre de la nueva especialidad
	 * @param activaSistema boolean que indica si esta /va a 
	 * estar especialidad está activa en el sistema
	 */
	public void setEspecialidad (String codigo, String nombre, boolean activaSistema) 
	{
		especialidades.add(new Especialidad(codigo, nombre, activaSistema));
	}

	/**
	 * Returna los nombres de las especialidades del medico,sus codigos, o ambas en un <code>String []</code>
	 * @param info indica que informacion se debe retornar, según el valor que tenga este parametro,
	 * dentro de los posibles siguientes valores: <br>
	 * <ul>
	 * 	<li> 1 : "codigo-nombre" de la especialidad </li>
	 * 	<li> 2 : "codigo" </li>
	 * 	<li> 3 : "nombre" de la especialidad </li>
	 * </ul>
	 * @param encoded indica si el texto debe retornanrse codificado como <i>character entities</i> de HTML
	 * @return un <code>String []</code> con la informacion indicada segun el valor de los parametros info y encoded
	 */
	public String [] getEspecialidades (int info, boolean encoded) {

		Iterator it = especialidades.iterator();
		int tam = especialidades.size();
		String [] resp = new String[tam];

		if (info < 1 || info > 3) {
			info = 1;
		}

		switch (info) {

			case 1:
				Especialidad e = null;
				for (int i=0; i < tam; i++) {
					e = (Especialidad)it.next();
					if (encoded) {
						resp[i] = Encoder.encode(e.getCodigoEspecialidad() + "-" + e.getEspecialidad());
					}
					else {
						resp[i] = e.getCodigoEspecialidad() + "-" + e.getEspecialidad();
					}
				}
			break;

			case 2:
				for (int i=0; i < tam; i++) {
					if (encoded) {
						resp[i] = Encoder.encode(((Especialidad)it.next()).getCodigoEspecialidad());
					}
					else {
						resp[i] = ((Especialidad)it.next()).getCodigoEspecialidad();
					}
				}
			break;

			case 3:
				for (int i=0; i < tam; i++) {
					if (encoded) {
						resp[i] = Encoder.encode(((Especialidad)it.next()).getEspecialidad());
					}
					else {
						resp[i] = ((Especialidad)it.next()).getEspecialidad();
					}
				}
			break;

		}

		return resp;

	}

	/**
	 * Retorna la representación como cadena de texto de este objeto.
	 * @return una lista separada por comas con las especialidades actualmente presentes en este objeto
	 */
	public String toString () {

		Iterator i = especialidades.iterator();
		String s = "";
		Especialidad e;

		while (i.hasNext()) {
			e = (Especialidad)i.next();
			s += (e.getCodigoEspecialidad() + "-" + Encoder.encode(e.getEspecialidad()) + ", ");
		}

		if (s.length() > 1) {
			char [] tmp = s.toCharArray();
			tmp [tmp.length - 2] = ' ';
			s = (new String(tmp)).trim();
		}

		return s;

	}
	
	/**
	 * Método que retorna el listado de las especialidades guardadas
	 * en este objeto
	 * @return
	 */
	public Collection getListadoEspecialidades()
	{
		return especialidades;
	}
}