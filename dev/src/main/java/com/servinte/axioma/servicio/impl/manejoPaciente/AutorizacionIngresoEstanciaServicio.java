package com.servinte.axioma.servicio.impl.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.manejoPaciente.DTOAdministracionAutorizacion;
import com.princetonsa.dto.manejoPaciente.DTOAutorizacionIngresoEstancia;
import com.princetonsa.dto.manejoPaciente.DTOBusquedaAutorizacionCapitacionRango;
import com.servinte.axioma.mundo.fabrica.odontologia.manejopaciente.ManejoPacienteFabricaMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IAutorizacionIngresoEstanciaMundo;
import com.servinte.axioma.orm.AutorizacionesIngreEstancia;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IAutorizacionIngresoEstanciaServicio;

/**
 * Esta clase se encarga de ejecutar los métodos de 
 * negocio de la entidad Autorizaciones Ingreso Estancia
 * @author Angela Maria Aguirre
 * @since 3/01/2011
 */
public class AutorizacionIngresoEstanciaServicio implements IAutorizacionIngresoEstanciaServicio 
{
	
	IAutorizacionIngresoEstanciaMundo mundo;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public AutorizacionIngresoEstanciaServicio(){
		mundo  = ManejoPacienteFabricaMundo.crearAutorizacionIngresoEstanciaMundo();
	}	
	
	
	/**
	 * 
	 * Este Método se encarga de consultar las autorizaciones en estado autorizado o
	 * con indicativo temporal de un paciente determinado
	 * 
	 * @param DTOAdministracionAutorizacion dto
	 * @return ArrayList<DTOAdministracionAutorizacion>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTOAdministracionAutorizacion> obtenerAutorizacionesPorPaciente(DTOAdministracionAutorizacion dto){
		return mundo.obtenerAutorizacionesPorPaciente(dto);
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar las autorizaciones en estado autorizado o
	 * con indicativo temporal en un rango determinado
	 * 
	 * @param DTOBusquedaAutorizacionCapitacionRango dto
	 * @return ArrayList<DTOAdministracionAutorizacion>
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public ArrayList<DTOAdministracionAutorizacion> obtenerAutorizacionesPorRango(DTOBusquedaAutorizacionCapitacionRango dto){
		return mundo.obtenerAutorizacionesPorRango(dto);
	}
	
	
	/**
	 * 
	 * Este Método se encarga de consultar una autorización de ingreso estancia
	 * por su id
	 * 
	 * @param DTOAutorizacionIngresoEstancia dto
	 * @return DTOAutorizacionIngresoEstancia
	 * @author Angela Maria Aguirre
	 *
	 */
	public DTOAutorizacionIngresoEstancia consultarAutorizacionPorID(DTOAutorizacionIngresoEstancia dto){
		return mundo.consultarAutorizacionPorID(dto);
	}
	
	/**
	 * 
	 * Este Método se encarga de actualizar una autorización de ingreso estancia
	 * 
	 * @param AutorizacionesIngreEstancia autorizacion
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarAutorizacionIngresoEstancia(AutorizacionesIngreEstancia autorizacion){
		return mundo.actualizarAutorizacionIngresoEstancia(autorizacion);
	}
	
	/**
	 * 
	 * Este Método se encarga de buscar una autorización de ingreso estancia por su ID
	 * 
	 * @param long id
	 * @return AutorizacionesIngreEstancia
	 * @author, Angela Maria Aguirre
	 *
	 */
	public AutorizacionesIngreEstancia buscarAutorizacionIngresoEstanciaPorID(long id){
		return mundo.buscarAutorizacionIngresoEstanciaPorID(id);
	}
	
	
	/**
	 * 
	 * Este Método se encarga de consultar una autorización de ingreso estancia
	 * cuando esta tiene una autorización de capitación asociada
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public AutorizacionesIngreEstancia obtenerAutorizacionIngEstanciaCapitacion(DTOAutorizacionIngresoEstancia dto){
		return mundo.obtenerAutorizacionIngEstanciaCapitacion(dto);
	}


	@Override
	public void attachDirty(AutorizacionesIngreEstancia instance) {
		mundo.attachDirty(instance);
	}


	@Override
	public AutorizacionesIngreEstancia findById(long id) {
		return mundo.findById(id);
	}

	/** 
	 * Este Método se encarga de consultar las autorizaciones de Ingreso estancia 
	 * por paciente
	 * 
	 * @param DTOAdministracionAutorizacion dto
	 * @return ArrayList<DTOAdministracionAutorizacion>
	 * @author, Camilo Gomez
	 */
	public ArrayList<DTOAdministracionAutorizacion> obtenerAutorizacionesIngresoEstanciaPaciente(DTOAdministracionAutorizacion dto){
		return mundo.obtenerAutorizacionesIngresoEstanciaPaciente(dto);
	}
	
}
