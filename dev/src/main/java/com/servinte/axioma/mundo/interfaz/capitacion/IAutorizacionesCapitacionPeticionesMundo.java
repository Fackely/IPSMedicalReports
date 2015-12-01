package com.servinte.axioma.mundo.interfaz.capitacion;

import java.util.ArrayList;

import com.servinte.axioma.dto.capitacion.DtoAutorizacionCapitacionPeticion;
import com.servinte.axioma.orm.AutoEntsubSolicitudes;
import com.servinte.axioma.orm.AutoEntsubSolicitudesHome;
import com.servinte.axioma.orm.AutorizacionesEntidadesSub;
import com.servinte.axioma.orm.Solicitudes;

public interface IAutorizacionesCapitacionPeticionesMundo {

	/**
	 * Método que se encarga de validar si la peticion está asociada a una autorización de capitación subcontratada.
	 * Cambio RQF-02 -0025 Autorizaciones Capitación Subcontratada
	 * 
	 * @author Camilo Gómez
	 * @param dtoAutorizCapitaPeticion
	 * @return ArrayList<DtoAutorizacionCapitacionPeticion>
	 */
	public ArrayList<DtoAutorizacionCapitacionPeticion> existeAutorizacionPeticion(DtoAutorizacionCapitacionPeticion dtoAutorizCapitaPeticion);
	
	/**
	 * Método que se encarga de asociar la solicitud de la petición a la Autorización de Capitación 
	 * 
	 * Cambio RQF-02 -0025 Autorizaciones Capitación Subcontratada
	 * 
	 * @author Camilo Gómez
	 * @param dtoAsociaSoliciAutorizPeticion
	 */
	public void asociarSolicitudAutorizacionPeticion(DtoAutorizacionCapitacionPeticion dtoAsociaSoliciAutorizPeticion);
	
}
