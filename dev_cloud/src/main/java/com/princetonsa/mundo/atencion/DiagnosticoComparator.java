/*
 * @(#)DiagnosticoComparator.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.mundo.atencion;

import java.util.Comparator;

/**
 * Esta clase compara dos diagnósticos. Si tienen el mismo acrónimo, tipoCIE y ambos
 * son principales (o ambos relacionados), se consideran iguales. Si lo anterior no
 * es cierto, se establece un orden total con base en el número del diagnóstico.
 * Esta implementación <i>no es consistente con equals</i>.
 * @see java.util.Comparator
 *
 * @version Jun 3, 2003
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>
 */

public class DiagnosticoComparator implements Comparator {

	/**
	 * Compara dos <code>Diagnosticos</code>, como se explica en la descripción de esta clase.
	 * @param o1 el primer objeto <code>Diagnostico</code> a ser comparado 
	 * @param o2 el segundo objeto <code>Diagnostico</code> a ser comparado
	 * @return 0 si los diagnósticos se consideran iguales, un entero negativo
	 * si o1 &lt; o2 , un entero positivo si o1 &gt; o2
	 */
	public int compare (Object o1, Object o2) {

		Diagnostico d1 = (Diagnostico) o1;
		Diagnostico d2 = (Diagnostico) o2;

		if ( d1.getAcronimo().equals(d2.getAcronimo()) && d1.getTipoCIE() == d2.getTipoCIE() && d1.isPrincipal() == d2.isPrincipal() ) {
			return 0;
		}
		else {
			return d1.getNumero() - d2.getNumero();
		}

	}

}