/*
 * @(#)MD5Hash.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Esta clase permite obtener el <i>hash</i> de un password, usando el algoritmo MD5.
 *
 * @version 1.0, Sep 12, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class MD5Hash {

	/**
	 * Obtiene el <i>hash</i> de un password, usando el algoritmo MD5.
	 * @param  password una cadena de texto con el password al cual se le va a obtener su <i>hash</i>
	 * @return una cadena de texto de 32 caracteres de longitud, con el <i>hash</i> del password
	 * en base hexadecimal
	 */
	public static String hashPassword (String password) {

		MessageDigest md = null;

		/* Si un proveeddor criptográfico JCE no se encuentra instalado, está mal configurado
		   o no soporta el algoritmo MD5, la siguiente operación producirá una excepción */
		try {
			md = MessageDigest.getInstance("MD5");
		}
		catch (NoSuchAlgorithmException nsae) {
			System.err.println("El proveedor criptográfico no soporta el algoritmo MD5");
			nsae.getMessage();
			nsae.printStackTrace();
		}

		md.update(password.getBytes());

		// El hash del password queda en un byte []
		byte [] digest = md.digest();
		String resp = "";

		// Convierto el byte [] en una cadena de texto con la representación hexadecimal del hash
		for (int i=0; i<16; i++) {
			resp += Integer.toString( ((digest[i] & 0xF0) >> 4), 16);
			resp += Integer.toString( (digest[i] & 0x0F), 16);
		}

		return resp;

	}

}