package com.servinte.axioma.dao.impl.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.manejoPaciente.DTOAdministracionAutorizacion;
import com.princetonsa.dto.manejoPaciente.DTOAutorizacionIngresoEstancia;
import com.princetonsa.dto.manejoPaciente.DTOBusquedaAutorizacionCapitacionRango;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IAutorizacionesIngresoEstanciaDAO;
import com.servinte.axioma.orm.AutorizacionesIngreEstancia;
import com.servinte.axioma.orm.delegate.manejoPaciente.AutorizacionesIngresoEstanciaDelegate;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 3/01/2011
 */
public class AutorizacionesIngresoEstanciaHibernateDAO implements IAutorizacionesIngresoEstanciaDAO 

{
	
	AutorizacionesIngresoEstanciaDelegate delegate;
	
	
	public AutorizacionesIngresoEstanciaHibernateDAO(){
		delegate = new AutorizacionesIngresoEstanciaDelegate();
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
		return delegate.obtenerAutorizacionesPorPaciente(dto);
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
	public ArrayList<DTOAdministracionAutorizacion> obtenerAutorizacionesPorRango(DTOBusquedaAutorizacionCapitacionRango dto){
		return delegate.obtenerAutorizacionesPorRango(dto);
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
		return delegate.consultarAutorizacionPorID(dto);
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
		return delegate.actualizarAutorizacionIngresoEstancia(autorizacion);
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
		return delegate.findById(id);
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
		return delegate.obtenerAutorizacionIngEstanciaCapitacion(dto);
	}


	@Override
	public void attachDirty(AutorizacionesIngreEstancia instance) {
		delegate.attachDirty(instance);
	}


	@Override
	public AutorizacionesIngreEstancia findById(long id) {
		return delegate.findById(id);
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar las autorizaciones por entidad subcontratada
	 * 
	 * @param DcodigoPkEntidadSub
	 * @return ArrayList<DTOAdministracionAutorizacion>
	 * @author, Fabián Becerra
	 *
	 */
	public ArrayList<DTOAdministracionAutorizacion> obtenerAutorizacionesPorEntidadSub(long codigoPkEntidadSub){
		return delegate.obtenerAutorizacionesPorEntidadSub(codigoPkEntidadSub);
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
		return delegate.obtenerAutorizacionesIngresoEstanciaPaciente(dto); 
	}
}
