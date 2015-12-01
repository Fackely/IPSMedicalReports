package com.servinte.axioma.dao.interfaz.manejoPaciente;

import com.servinte.axioma.orm.AutorizacionesEstanciaCapita;

/**
 * Esta clase se encarga de definir los m�todos de negocio
 * para la entidad AutorizacionesEstanciaCapita
 * 
 * @author Angela Maria Aguirre
 * @since 15/12/2010
 */
public interface IAutorizacionesEstanciaCapitaDAO {
	
	/**
	 * 
	 * Este M�todo se encarga de insertar en la base de datos
	 * el registro que asocia la autorizaci�n de ingreso estancia con la
	 * autorizaci�n de capitaci�n subcontratada
	 * 
	 * @param AutorizacionesEstanciaCapita autorizacion
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarAutorizacionEntidadSubcontratada(AutorizacionesEstanciaCapita autorizacion);

}
