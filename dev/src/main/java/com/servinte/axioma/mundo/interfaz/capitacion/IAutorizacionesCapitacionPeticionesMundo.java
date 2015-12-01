package com.servinte.axioma.mundo.interfaz.capitacion;

import java.util.ArrayList;

import com.servinte.axioma.dto.capitacion.DtoAutorizacionCapitacionPeticion;
import com.servinte.axioma.orm.AutoEntsubSolicitudes;
import com.servinte.axioma.orm.AutoEntsubSolicitudesHome;
import com.servinte.axioma.orm.AutorizacionesEntidadesSub;
import com.servinte.axioma.orm.Solicitudes;

public interface IAutorizacionesCapitacionPeticionesMundo {

	/**
	 * M�todo que se encarga de validar si la peticion est� asociada a una autorizaci�n de capitaci�n subcontratada.
	 * Cambio RQF-02 -0025 Autorizaciones Capitaci�n Subcontratada
	 * 
	 * @author Camilo G�mez
	 * @param dtoAutorizCapitaPeticion
	 * @return ArrayList<DtoAutorizacionCapitacionPeticion>
	 */
	public ArrayList<DtoAutorizacionCapitacionPeticion> existeAutorizacionPeticion(DtoAutorizacionCapitacionPeticion dtoAutorizCapitaPeticion);
	
	/**
	 * M�todo que se encarga de asociar la solicitud de la petici�n a la Autorizaci�n de Capitaci�n 
	 * 
	 * Cambio RQF-02 -0025 Autorizaciones Capitaci�n Subcontratada
	 * 
	 * @author Camilo G�mez
	 * @param dtoAsociaSoliciAutorizPeticion
	 */
	public void asociarSolicitudAutorizacionPeticion(DtoAutorizacionCapitacionPeticion dtoAsociaSoliciAutorizPeticion);
	
}
