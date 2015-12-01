package com.servinte.axioma.dao.interfaz.odontologia.agendaOdontologica;

import com.servinte.axioma.orm.InicioAtencionCita;

public interface IInicioAtencionCitaDAO {

	/**
	 * 
	 * Este M�todo se encarga de guardar un registro de
	 * inicio atenci�n de citas odontol�gicas
	 * 
	 * @param 
	 * @return boolean
	 * @author, Fabian Becerra
	 *
	 */
	public boolean guardarInicioAtencionCitaOdonto(InicioAtencionCita iac);
	
	
	/**
	 * 
	 * Este M�todo se encarga de actualizar un registro de
	 * inicio atenci�n de citas odontol�gicas
	 * 
	 * @param 
	 * @return boolean
	 * @author, Fabian Becerra
	 *
	 */
	public boolean actualizarInicioAtencionCitaOdonto(InicioAtencionCita iac);
	
	
	
	/**
	 * 
	 * Este M�todo se encarga de buscar un registro de
	 * inicio atenci�n de citas odontol�gicas por su id
	 * 
	 * @param 
	 * @return boolean
	 * @author, Fabian Becerra
	 *
	 */
	public InicioAtencionCita buscarRegistroInicioAtencionCitaOdontoPorID(long codigoCita);
	
	
}
