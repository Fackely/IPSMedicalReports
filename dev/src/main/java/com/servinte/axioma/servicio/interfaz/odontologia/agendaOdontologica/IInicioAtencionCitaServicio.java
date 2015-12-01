package com.servinte.axioma.servicio.interfaz.odontologia.agendaOdontologica;

import com.princetonsa.dto.odontologia.DtoInicioAtencionCita;
import com.servinte.axioma.orm.InicioAtencionCita;


public interface IInicioAtencionCitaServicio {

	/**
	 * 
	 * Este Método se encarga de guardar un registro de
	 * inicio atención de citas odontológicas
	 * 
	 * @param 
	 * @return boolean
	 * @author, Fabian Becerra
	 *
	 */
	public boolean guardarInicioAtencionCitaOdonto(DtoInicioAtencionCita dto);
	
	
	/**
	 * 
	 * Este Método se encarga de actualizar un registro de
	 * inicio atención de citas odontológicas
	 * 
	 * @param 
	 * @return boolean
	 * @author, Fabian Becerra
	 *
	 */
	public boolean actualizarInicioAtencionCitaOdonto(DtoInicioAtencionCita dto);
	
	
	/**
	 * 
	 * Este Método se encarga de buscar un registro de
	 * inicio atención de citas odontológicas por su id
	 * 
	 * @param 
	 * @return boolean
	 * @author, Fabian Becerra
	 *
	 */
	public InicioAtencionCita buscarRegistroInicioAtencionCitaOdontoPorID(long codigoCita);
	
		
	
}
