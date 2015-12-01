/**
 * 
 */
package com.servinte.axioma.vista.autenticacion;

/**
 * Enumeraci&oacute;n con las llaves y valores de los 
 * atributos guardados en sesi&oacute;n o en la 
 * peticiones.
 * 
 * @author Fernando Ocampo
 *
 */
public enum EAtributosAutenticacion {

	/**
	 * Llave para el atributo de sesi&oacute;n
	 * del usuario autenticado.
	 */
	LLAVE_USUARIO_BASICO("usuarioBasico"),

	/**
	 * Llave para el atributo de intentos de
	 * autenticaci&oacute;n.
	 */
	LLAVE_INTENTOS_LOGIN("com.servinte.axioma.auth.intentoslogin"),

	/**
	 * Llave para el atributo de resultado de autenticaci&oacute;n 
	 */
	LLAVE_RESULTADO("com.servinte.axioma.auth.resultado"),

	/**
	 * Valor de un atributo que indica que el usuario
	 * supero el n&uacute;mero de intentos permitidos.
	 */
	VALOR_INTENTOS_SUPERADOS("superointentospermitidos"),
	
	/**
	 * Valor de un atributo indicando que el usuario
	 * autentico exitosamente.
	 */
	VALOR_SI_AUTENTICO("siautentico");

	/**
	 * Valor de la enumeraci&oacute;n.
	 */
	private String valor;
	
	private EAtributosAutenticacion(String valor) {
		this.valor = valor;
	}
	public String getValor(){
		return valor;
	}
}
