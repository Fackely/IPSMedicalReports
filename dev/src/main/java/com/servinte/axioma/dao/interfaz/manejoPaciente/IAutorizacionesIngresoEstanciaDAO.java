package com.servinte.axioma.dao.interfaz.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.manejoPaciente.DTOAdministracionAutorizacion;
import com.princetonsa.dto.manejoPaciente.DTOAutorizacionIngresoEstancia;
import com.princetonsa.dto.manejoPaciente.DTOBusquedaAutorizacionCapitacionRango;
import com.servinte.axioma.orm.AutorizacionesIngreEstancia;

/**
 * Esta clase se encarga de definir los m�todos de 
 * negocio de la entidad Autorizaciones Ingreso Estancia
 * 
 * @author Angela Maria Aguirre
 * @since 3/01/2011
 */
public interface IAutorizacionesIngresoEstanciaDAO {
	

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
	 * @param DTOBusquedaAutorizacionCapitacionRango dto
	 * @return ArrayList<DTOAdministracionAutorizacion>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTOAdministracionAutorizacion> obtenerAutorizacionesPorRango(DTOBusquedaAutorizacionCapitacionRango dto);
	
	/**
	 * 
	 * Este M�todo se encarga de consultar una autorizaci�n de ingreso estancia
	 * por su id
	 * 
	 * @param DTOAutorizacionIngresoEstancia dto
	 * @return DTOAutorizacionIngresoEstancia
	 * @author Angela Maria Aguirre
	 *
	 */
	public DTOAutorizacionIngresoEstancia consultarAutorizacionPorID(DTOAutorizacionIngresoEstancia dto);
	
	
	/**
	 * 
	 * Este M�todo se encarga de actualizar una autorizaci�n de ingreso estancia
	 * 
	 * @param AutorizacionesIngreEstancia autorizacion
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarAutorizacionIngresoEstancia(AutorizacionesIngreEstancia autorizacion);
	
	
	/**
	 * 
	 * Este M�todo se encarga de buscar una autorizaci�n de ingreso estancia por su ID
	 * 
	 * @param long id
	 * @return AutorizacionesIngreEstancia
	 * @author, Angela Maria Aguirre
	 *
	 */
	public AutorizacionesIngreEstancia buscarAutorizacionIngresoEstanciaPorID(long id);
	
	
	/**
	 * 
	 * Este M�todo se encarga de consultar una autorizaci�n de ingreso estancia
	 * cuando esta tiene una autorizaci�n de capitaci�n asociada
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public AutorizacionesIngreEstancia obtenerAutorizacionIngEstanciaCapitacion(DTOAutorizacionIngresoEstancia dto);
	
	
	/**
	 * attachDirty
	 * @param instance
	 */
	public void attachDirty(AutorizacionesIngreEstancia instance);
	
	
	/**
	 * findById
	 * @param id
	 */
	public AutorizacionesIngreEstancia findById(long id) ;
	
	/**
	 * 
	 * Este M�todo se encarga de consultar las autorizaciones por entidad subcontratada
	 * 
	 * @param DcodigoPkEntidadSub
	 * @return ArrayList<DTOAdministracionAutorizacion>
	 * @author, Fabi�n Becerra
	 *
	 */
	public ArrayList<DTOAdministracionAutorizacion> obtenerAutorizacionesPorEntidadSub(long codigoPkEntidadSub);

	/** 
	 * Este M�todo se encarga de consultar las autorizaciones de Ingreso estancia 
	 * por paciente
	 * 
	 * @param DTOAdministracionAutorizacion dto
	 * @return ArrayList<DTOAdministracionAutorizacion>
	 * @author, Camilo Gomez
	 */
	public ArrayList<DTOAdministracionAutorizacion> obtenerAutorizacionesIngresoEstanciaPaciente(DTOAdministracionAutorizacion dto);
}
