/*
 * @(#)ModuloMenu.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package util;

import java.util.Vector;


/**
 * Clase utilitaria para encapsular funcionalidades de la aplicacion en modulos.
 * Estos modulos se utilizaran para presentar el menu con divisiones
 * funcionales segun su definicion en MenuFilter
 *
 * @version 1.0, Abr 01, 2003
 * @author 	<a href="mailto:Julian@PrincetonSA.com">Julian Gomez J.</a>
 */

public class ModuloMenu implements java.io.Serializable {

	/**
	 * Almacena el nombre del modulo.
	 */
	private String nombreModulo = "";

	/**
	 * Almacena las funcionalidades que tiene el modulo, segun las
	 * funcionalidades que se le asignen al usuario.
	 */
	public Vector funcionalidades = new Vector();

	/**
	 * Crea un nuevo objeto <code>ModuloMenu</code>.
	 * @param nombreModulo nombre del rol del usuario
	 */
	public ModuloMenu (String nombre) {
		this.nombreModulo = nombre;
	}

	/**
	 * Retorna el nombre del modulo.
	 * @return el nombre del modulo
	 */
	public String getNombreModulo() {
		return nombreModulo;
	}

	/**
	 * Compara dos objetos ModuloMenu basado en su nombre.
	 * @param moduloComp
	 * @return boolean
	 */	
	public boolean equals(ModuloMenu moduloComp){
		return (this.nombreModulo.equals(moduloComp.getNombreModulo()));			
	}
	
}