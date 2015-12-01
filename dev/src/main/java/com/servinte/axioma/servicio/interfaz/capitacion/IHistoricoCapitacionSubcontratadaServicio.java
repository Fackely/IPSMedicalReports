package com.servinte.axioma.servicio.interfaz.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.manejoPaciente.DTOAutorEntidadSubcontratadaCapitacion;

public interface IHistoricoCapitacionSubcontratadaServicio {

	
	/**
	 * 
	 * Este M�todo se encarga de consultar por el ID las autorizaciones Historicas de
	 * entidades subcontratadas y su respectiva autorizaci�n de capitaci�n
	 * 
	 * @param DTOAutorEntidadSubcontratadaCapitacion dto
	 * @return ArrayList<DTOAutorEntidadSubcontratadaCapitacion>
	 * @author, Camilo Gomez
	 *
	 */
	public ArrayList<DTOAutorEntidadSubcontratadaCapitacion> obtenerHistoricoAutorizacionEntidadSubCapitacionPorID(
			DTOAutorEntidadSubcontratadaCapitacion dto);
}
