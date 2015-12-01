/**
 * 
 */
package util;

/**
 * @author jeilones
 * @created 8/10/2012
 *
 */
public interface ExpresionesRegulares {
	String permitirSoloNumeros="[0-9]{3,}";
	String permitirNumerosLetrasGuion="[0-9a-zA-Z\\-]{3,}";
	String permitirNumerosPunto="[0-9]{1,1}|[0-9]{1,1}[.]{1,1}[0-9]{1,2}";
}
