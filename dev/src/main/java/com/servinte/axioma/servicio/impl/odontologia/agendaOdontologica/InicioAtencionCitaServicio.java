package com.servinte.axioma.servicio.impl.odontologia.agendaOdontologica;

import com.princetonsa.dto.odontologia.DtoInicioAtencionCita;
import com.servinte.axioma.mundo.fabrica.odontologia.agendaOdontologica.AgendaOdontologicaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.agendaOdontologica.IInicioAtencionCitaMundo;
import com.servinte.axioma.orm.InicioAtencionCita;
import com.servinte.axioma.servicio.interfaz.odontologia.agendaOdontologica.IInicioAtencionCitaServicio;

public class InicioAtencionCitaServicio implements IInicioAtencionCitaServicio {
	
	private IInicioAtencionCitaMundo mundo;
	
	public InicioAtencionCitaServicio() {
		mundo = AgendaOdontologicaFabricaMundo.crearInicioAtencionCitaMundo();
	}
	
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
	public boolean guardarInicioAtencionCitaOdonto(DtoInicioAtencionCita dto){
		return mundo.guardarInicioAtencionCitaOdonto(dto);
	}
	
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
	public boolean actualizarInicioAtencionCitaOdonto(DtoInicioAtencionCita dto){
		return mundo.actualizarInicioAtencionCitaOdonto(dto);
	}
	
	
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
	public InicioAtencionCita buscarRegistroInicioAtencionCitaOdontoPorID(long codigoCita){
		return mundo.buscarRegistroInicioAtencionCitaOdontoPorID(codigoCita);
	}
	
	
	

}
