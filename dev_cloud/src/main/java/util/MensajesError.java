package util;

/**
 * Interfaz para el manejo de cadenas estáticas con los mensajes de error que se
 * manejan en la aplicación
 * @version 1.0, Noviembre 18, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public interface MensajesError
{
	public static String usuarioNoValido = "No hay ningún usuario cargado";
	
	public static String pacienteNoValido = "No hay ningún paciente cargado";
	
	public static String estadoNoValido = "La accion específicada no está definida (comuniquese con su administrador de red)";
	
	public static String usuarioNoAutorizado = "Usuario No autorizado para ingresar a esta opcion";
	
	public static String errorAccesoBD = "Problemas accediendo la fuente de datos, por favor inténtelo de nuevo mas adelante";
}
