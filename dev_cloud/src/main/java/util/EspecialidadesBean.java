/*
 * @(#)EspecialidadesBean.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package util;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.jsp.PageContext;

import com.princetonsa.mundo.Especialidad;
import com.princetonsa.mundo.Especialidades;

/**
 * Esta clase sirve como <i>helper bean</i> para los jsp's que permiten ingresar las
 * especialidades de un médico (espec.jsp y especCtrl.jsp). Proporciona la funcionalidad
 * necesaria para quitar y poner especialidades dinámicamente.
 *
 * @version 1.0, Oct 26, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class EspecialidadesBean {

	/**
	 * Especialidad seleccionada actualmente para ser agregada al conjunto de especialidades actuales.
	 */
	private String especialidad;

	/**
	 * Define si esta especialidad está activa en el sistema o no 
	 */
	private String activaSistema;

	/**
	 * Lista de las especialidades elegidas para ser eliminadas. 
	 */
	private String [] elegidas;

	/**
	 * Conjunto de especialidades actualmente asociadas al médico, implementado de esta forma
	 * para impedir que una misma especialidad se agregue dos veces.
	 */
	private Set actuales;
	
	/**
	 * Conjunto para manejar si una especialidad en particular está activa 
	 * o no, no se manejo como  parte del set actuales para seguir teniendo
	 * manejo sobre los repetidos
	 */
	private HashMap actualesActivas;

	/**
	 * Constructor vacio, necesario para poder usar esta clase como un JavaBean
	 */
	public EspecialidadesBean () {
		clean();
	}

	/**
	 * Método que inicializa el estado interno de este objeto, a partir de los datos
	 * provenientes de un objeto <code>Especialidades</code>
	 */
	public void init (Especialidades e) {
		clean();
		Collection listaEspecialidades = e.getListadoEspecialidades();
		Iterator it=listaEspecialidades.iterator();
		while (it.hasNext())
		{
			Especialidad esp=(Especialidad)it.next();
			String enFormatoNecesario=esp.getCodigoEspecialidad() + "-" + esp.getEspecialidad();
			actuales.add(enFormatoNecesario);
			if (esp.getActivaSistema())
			{
				actualesActivas.put(enFormatoNecesario, "true");
			}
			else
			{
				actualesActivas.put(enFormatoNecesario, "false");
			}
		}
		
	}

	/**
	 * Retorna las especialidades asignadas al medico.
	 * @return conjunto de especialidades actualmente asignadas al medico
	 */
	public Set getActuales() {
		return actuales;
	}

	/**
	 * Establece las especialidades elegidas para ser eliminadas.
	 * @param elegidas un arreglo de cadenas con las especialidades a ser establecidas
	*/ 
	public void setElegidas(String [] elegidas) {
		this.elegidas = elegidas;
	}

	/**
	 * Establece una especialidad seleccionada para ser agregada al conjunto de especialidades.
	 * @param especialidad la nueva especialidad que va a ser añadida
	 */
	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}

	/**
	 * Este metodo inicializa en valores vacios (mas no nulos) los atributos de <code>EspecialidadesBean</code>.
	 */
	public void clean () {
		especialidad = "";
		elegidas = new String [0];
		actuales = new TreeSet(new EspecialidadComparator());
		this.actualesActivas = new HashMap();
	}

	/**
	 * Añade una nueva especialidad a la lista de especialidades actualmente asociadas al médico.
	 */
	public void addEspecialidad () {
		// El valor por defecto en el tag de html es "0-Seleccione Especialidad", si cambia, en este punto debe reflejarse el nuevo valor
		if (!especialidad.equals("0-Seleccione Especialidad")) {
			actuales.add(especialidad); 
			if (activaSistema!=null)
			{
				actualesActivas.put(especialidad, activaSistema);
				activaSistema=null;
			}
			else
			{
				actualesActivas.put(especialidad, "false");
			}
		}
	}

	/**
	 * Dado un arreglo de cadenas con las especialidades elegidas para ser eliminadas, las remueve
	 * del conjunto actual de especialidades del médico.
	 */
	public void delEspecialidad () {
		String tmp = new String();
		for (int i=0; i<elegidas.length; i++) 
		{
			tmp = elegidas[i];
			// El valor por defecto en el tag de html es "0-Especialidades", si cambia, en este punto debe reflejarse el nuevo valor
			if (!tmp.equals("0-Especialidades")) 
			{
				actuales.remove(tmp);
			}
		}
	}

	/**
	 * Retorna dentro de tags <i>option</i> de html la lista (codificada) de especialidades
	 * actuales del médico.
	 * @return una cadena con las especialidades del médico, para ser mostrada dentro de un
	 * tag &lt;select&gt; de html.
	 */
	public String listaActuales () {

		StringBuffer resp = new StringBuffer();
		Iterator i = actuales.iterator();
		String s = new String();

		while (i.hasNext()) {
			s = (String) i.next();
			resp.append("<option value=\"");
			resp.append(s);
			resp.append("\">");
			if (actualesActivas.get(s)!=null&&   ((String)(actualesActivas.get(s))).equals("true")     )
			{
				resp.append( Encoder.encode((UtilidadTexto.separarNombresDeCodigos(s,1))[1]) +" (Activo en Sistema)" );
			}
			else
			{
				resp.append( Encoder.encode((UtilidadTexto.separarNombresDeCodigos(s,1))[1]) +" (Inactivo en Sistema)" );
			}
			resp.append("</option>");
		}

		return resp.toString();

	}

	/**
	 * Retorna la representación codificada de este objeto como una cadena de texto.
	 * @return la lista de especialidades asociadas a un médico
	 */
	public String toString () {

		Iterator i = actuales.iterator();
		String s = "";

		while (i.hasNext()) {
			s += (UtilidadTexto.separarNombresDeCodigos((String)i.next(),1))[1] + ", ";
		}

		if (s.length() > 1) {
			char [] tmp = s.toCharArray();
			tmp [tmp.length - 2] = ' ';
			s = (new String(tmp)).trim();
		}

		return s;

	}

	/**
	 * Remueve una instancia de este objeto del <i>scope</i> de <i>session</i>
	 * de una aplicación web. Nótese que para que este método funcione, el objeto
	 * debe haber sido registrado previamente con el nombre "specs" en el contexto
	 * de sesión.
	 */
	public void killSelf (PageContext pc) {
		pc.removeAttribute("specs", PageContext.SESSION_SCOPE);
	}

	/**
	 * Esta clase interna es necesaria para poder implementar las especialidades de un médico
	 * como un <code>Set</code> : se requiere de un <code>Comparator</code> para comparar una
	 * especialidad con otra y determinar si son iguales.
	 *
	 * @version 1.0, Oct 26, 2002
	 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
	 */
	private class EspecialidadComparator implements Comparator {

		/**
		 * Compara un objeto de los posibles pertenecientes al conjunto de
		 * especialidades, con otro. Se trata simplemente de una comparación
		 * de <code>Strings</code>.
		 * @param o1 primer objeto a ser comparado
		 * @param o2 segundo objeto a ser comparado
		 * @return -1 si o1&lt;o2, 1 si o1&gt;o2, 0 si o1=o2 , se usa el orden
		 * lexicográfico por tratarse de comparaciones de cadenas de texto.
		 */
		public int compare(Object o1, Object o2) {
			String s1 = (String) o1;
			String s2 = (String) o2;
			return s1.compareTo(s2);
		}

	}

	/**
	 * @return
	 */
	public String getActivaSistema() {
		return activaSistema;
	}

	/**
	 * @param string
	 */
	public void setActivaSistema(String string) {
		activaSistema = string;
	}

	/**
	 * @return
	 */
	public HashMap getActualesActivas() {
		return actualesActivas;
	}

	/**
	 * @param map
	 */
	public void setActualesActivas(HashMap map) {
		actualesActivas = map;
	}

}