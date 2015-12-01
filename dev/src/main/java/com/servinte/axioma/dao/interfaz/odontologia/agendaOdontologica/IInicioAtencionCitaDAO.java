package com.servinte.axioma.dao.interfaz.odontologia.agendaOdontologica;

import com.servinte.axioma.orm.InicioAtencionCita;

public interface IInicioAtencionCitaDAO {

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
	public boolean guardarInicioAtencionCitaOdonto(InicioAtencionCita iac);
	
	
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
	public boolean actualizarInicioAtencionCitaOdonto(InicioAtencionCita iac);
	
	
	
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
