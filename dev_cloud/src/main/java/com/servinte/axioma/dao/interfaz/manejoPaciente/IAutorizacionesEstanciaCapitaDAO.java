package com.servinte.axioma.dao.interfaz.manejoPaciente;

import com.servinte.axioma.orm.AutorizacionesEstanciaCapita;

/**
 * Esta clase se encarga de definir los métodos de negocio
 * para la entidad AutorizacionesEstanciaCapita
 * 
 * @author Angela Maria Aguirre
 * @since 15/12/2010
 */
public interface IAutorizacionesEstanciaCapitaDAO {
	
	/**
	 * 
	 * Este Método se encarga de insertar en la base de datos
	 * el registro que asocia la autorización de ingreso estancia con la
	 * autorización de capitación subcontratada
	 * 
	 * @param AutorizacionesEstanciaCapita autorizacion
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarAutorizacionEntidadSubcontratada(AutorizacionesEstanciaCapita autorizacion);

}
