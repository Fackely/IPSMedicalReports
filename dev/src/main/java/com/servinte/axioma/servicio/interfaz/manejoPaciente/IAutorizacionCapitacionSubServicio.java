/**
 * 
 */
package com.servinte.axioma.servicio.interfaz.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.manejoPaciente.DTOAdministracionAutorizacion;
import com.princetonsa.dto.manejoPaciente.DTOBusquedaAutorizacionCapitacionRango;
import com.servinte.axioma.orm.AutorizacionesCapitacionSub;

/**
 * Esta clase se encarga de definir los m�todos de
 * negocio de la entidad AutorizacionCapitacionSub
 * 
 * @author Angela Maria Aguirre
 * @since 03/01/2011
 */
public interface IAutorizacionCapitacionSubServicio {
	
	/**
	 * 
	 * Este M�todo se encarga de consultar las autorizaciones en estado autorizado o
	 * con indicativo temporal de un paciente determinado
	 * 
	 * @param DTOAdministracionAutorizacion dto
	 * @return ArrayList<DTOAdministracionAutorizacion>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTOAdministracionAutorizacion> obtenerAutorizacionesPorPaciente(DTOAdministracionAutorizacion dto);
	
	
	/**
	 * 
	 * Este M�todo se encarga de consultar las autorizaciones en estado autorizado o
	 * con indicativo temporal en un rango determinado
	 * 
	 * @param DTOAdministracionAutorizacion dto
	 * @return ArrayList<DTOAdministracionAutorizacion>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTOAdministracionAutorizacion> obtenerAutorizacionesPorRango(DTOBusquedaAutorizacionCapitacionRango dto);
	
	/**
	 * 
	 * Este M�todo se encarga de actualizar el detalle de una autorizaci�n de capitaci�n
	 * 
	 * @param AutorizacionesCapitacionSub
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarAutorizacionCapitacion(AutorizacionesCapitacionSub autorizacion);
	
	/**
	 * 
	 * Este M�todo se encarga de consultar una  autorizaci�n de capitaci�n 
	 * subcontratada por su id
	 * 
	 * @param long id
	 * @return AutorizacionesCapitacionSub
	 * @author, Angela Maria Aguirresw
	 *
	 */
	public AutorizacionesCapitacionSub findById(long id);

}
