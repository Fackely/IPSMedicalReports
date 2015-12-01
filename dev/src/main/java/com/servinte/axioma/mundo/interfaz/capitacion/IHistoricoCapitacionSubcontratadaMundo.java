package com.servinte.axioma.mundo.interfaz.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.manejoPaciente.DTOAutorEntidadSubcontratadaCapitacion;

public interface IHistoricoCapitacionSubcontratadaMundo {
	/**
	 * 
	 * Este M�todo se encarga de consultar por el ID las autorizaciones historicas de
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
