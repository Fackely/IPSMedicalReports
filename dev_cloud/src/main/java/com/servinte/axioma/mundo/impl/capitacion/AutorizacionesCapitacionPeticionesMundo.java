package com.servinte.axioma.mundo.impl.capitacion;

import java.util.ArrayList;

import util.ConstantesIntegridadDominio;

import com.servinte.axioma.dto.capitacion.DtoAutorizacionCapitacionPeticion;
import com.servinte.axioma.mundo.fabrica.salasCirugia.SalasCirugiaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IAutorizacionesCapitacionPeticionesMundo;
import com.servinte.axioma.mundo.interfaz.salasCirugia.IPeticionQxMundo;
import com.servinte.axioma.orm.AutoEntsubSolicitudes;
import com.servinte.axioma.orm.AutoEntsubSolicitudesHome;
import com.servinte.axioma.orm.AutorizacionesEntidadesSub;
import com.servinte.axioma.orm.Solicitudes;

public class AutorizacionesCapitacionPeticionesMundo implements IAutorizacionesCapitacionPeticionesMundo{

	
	/**
	 * M�todo que se encarga de validar si la peticion est� asociada a una autorizaci�n de capitaci�n subcontratada.
	 * Cambio RQF-02 -0025 Autorizaciones Capitaci�n Subcontratada
	 * 
	 * @author Camilo G�mez
	 * @param dtoAutorizCapitaPeticion
	 * @return ArrayList<DtoAutorizacionCapitacionPeticion>
	 */
	public ArrayList<DtoAutorizacionCapitacionPeticion> existeAutorizacionPeticion(DtoAutorizacionCapitacionPeticion dtoAutorizCapitaPeticion)
	{
		IPeticionQxMundo peticionQxMundo=SalasCirugiaFabricaMundo.crearPeticionQxMundo();
		ArrayList<DtoAutorizacionCapitacionPeticion> listaDtoAutorizCapitaPeticion	=new ArrayList<DtoAutorizacionCapitacionPeticion>();
		ArrayList<DtoAutorizacionCapitacionPeticion> listaFinalPeticiones			=new ArrayList<DtoAutorizacionCapitacionPeticion>();
		
		listaDtoAutorizCapitaPeticion = peticionQxMundo.existeAutorizacionCapitaPeticion(dtoAutorizCapitaPeticion);
		
		for (DtoAutorizacionCapitacionPeticion dtoPeticion : listaDtoAutorizCapitaPeticion) 
		{
			if(dtoPeticion.getEstadoAutorizacion().equals(ConstantesIntegridadDominio.acronimoAutorizado))
				listaFinalPeticiones.add(dtoPeticion);
		}		
		return  listaFinalPeticiones;
	}
	
	/**
	 * M�todo que se encarga de asociar la solicitud de la petici�n a la Autorizaci�n de Capitaci�n 
	 * 
	 * Cambio RQF-02 -0025 Autorizaciones Capitaci�n Subcontratada
	 * 
	 * @author Camilo G�mez
	 * @param dtoAsociaSoliciAutorizPeticion
	 */
	public void asociarSolicitudAutorizacionPeticion(DtoAutorizacionCapitacionPeticion dtoAsociaSoliciAutorizPeticion)
	{
		AutoEntsubSolicitudesHome autoEntsubSolicitudesHome	=new AutoEntsubSolicitudesHome();
		AutoEntsubSolicitudes autoEntsubSolicitudes			=new AutoEntsubSolicitudes();
		
		Solicitudes solicitudes=new Solicitudes();
		solicitudes.setNumeroSolicitud(dtoAsociaSoliciAutorizPeticion.getNumeroSolicitudAutorizar());
		autoEntsubSolicitudes.setSolicitudes(solicitudes);
		
		AutorizacionesEntidadesSub autorizacionesEntidadesSub=new AutorizacionesEntidadesSub();
		autorizacionesEntidadesSub.setConsecutivo(dtoAsociaSoliciAutorizPeticion.getConsecutivoAutorEntSub());
		autoEntsubSolicitudes.setAutorizacionesEntidadesSub(autorizacionesEntidadesSub);
		
		autoEntsubSolicitudesHome.attachDirty(autoEntsubSolicitudes);
		
	}
}
