package util;

/**
 * Interfaz para el manejo de cadenas est�ticas con los mensajes de error que se
 * manejan en la aplicaci�n
 * @version 1.0, Noviembre 18, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public interface MensajesError
{
	public static String usuarioNoValido = "No hay ning�n usuario cargado";
	
	public static String pacienteNoValido = "No hay ning�n paciente cargado";
	
	public static String estadoNoValido = "La accion espec�ficada no est� definida (comuniquese con su administrador de red)";
	
	public static String usuarioNoAutorizado = "Usuario No autorizado para ingresar a esta opcion";
	
	public static String errorAccesoBD = "Problemas accediendo la fuente de datos, por favor int�ntelo de nuevo mas adelante";
}
