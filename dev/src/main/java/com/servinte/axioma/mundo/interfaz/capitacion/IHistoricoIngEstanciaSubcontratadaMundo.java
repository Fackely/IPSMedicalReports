package com.servinte.axioma.mundo.interfaz.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.manejoPaciente.DTOAutorEntidadSubcontratadaCapitacion;
import com.princetonsa.dto.manejoPaciente.DTOAutorizacionIngresoEstancia;

public interface IHistoricoIngEstanciaSubcontratadaMundo {

	
	/**
	 * 
	 * Este Método se encarga de consultar por el ID las autorizaciones historicas de
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
