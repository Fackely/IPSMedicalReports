package com.servinte.axioma.servicio.interfaz.odontologia.agendaOdontologica;

import com.princetonsa.dto.odontologia.DtoInicioAtencionCita;
import com.servinte.axioma.orm.InicioAtencionCita;


public interface IInicioAtencionCitaServicio {

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
	public boolean guardarInicioAtencionCitaOdonto(DtoInicioAtencionCita dto);
	
	
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
	public boolean actualizarInicioAtencionCitaOdonto(DtoInicioAtencionCita dto);
	
	
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
