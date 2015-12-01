package com.servinte.axioma.servicio.interfaz.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.manejoPaciente.DTOAutorEntidadSubcontratadaCapitacion;
import com.princetonsa.dto.manejoPaciente.DTOAutorizacionIngresoEstancia;

public interface IHistoricoIngEstanciaSubcontratadaServicio {

	
	/**
	 * 
	 * Este Método se encarga de consultar por el ID las autorizaciones Historicas de
	 * Ingreso Estancia 
	 * 
	 * @param DTOAutorEntidadSubcontratadaCapitacion dto
	 * @return ArrayList<DTOAutorEntidadSubcontratadaCapitacion>
	 * @author, Camilo Gomez
	 *
	 */
	public ArrayList<DTOAutorizacionIngresoEstancia> obtenerHistoricoAutorizEntSubIngEstanciaPorID(
			DTOAutorizacionIngresoEstancia dto);     
}
