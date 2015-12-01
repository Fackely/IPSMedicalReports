package com.servinte.axioma.mundo.interfaz.manejoPaciente;
import java.util.List;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionCapitacionDto;
import com.servinte.axioma.dto.manejoPaciente.DtoValidacionGeneracionAutorizacionCapitada;

/**
 * Esta clase se encarga de definir los m&eacute;todos para generar
 * la autorizaci&oacute;n de capitaci&oacute;n subcontratada para ordenes ambulatorias,
 * y peticiones Qx. 
 * @author Diana Ruiz
 * @since 21/06/2011
 */
public interface IValidacionGeneracionAutorizacionCapitadaMundo 
{

	/**
	 * M&eacute;todo que permite validar y generar la autorizaci&oacute;n para la poblaci&oacute;n capitada. 
	 * @param con
	 * @param dtoValidacionGeneracionAutorizacionCapitada
	 * @param usuario
	 * @author Diana Ruiz 
	 * @return 
	 * @throws Exception 
	 */	
	
	public List<AutorizacionCapitacionDto> generarValidacionGeneracionAutorizacionCapitada(		
		List<DtoValidacionGeneracionAutorizacionCapitada> listaValidacionGeneracionAutorizacionCapitada,
		UsuarioBasico usuario) throws Exception;
}
