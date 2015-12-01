package com.servinte.axioma.dao.impl.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DtoConsultaProcesoAutorizacion;
import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.princetonsa.dto.capitacion.DtoResultadoConsultaProcesosCierre;
import com.princetonsa.dto.manejoPaciente.DTOAdministracionAutorizacion;
import com.princetonsa.dto.manejoPaciente.DTOBusquedaAutorizacionCapitacionRango;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IAutorizacionesCapitacionSubDAO;
import com.servinte.axioma.orm.AutorizacionesCapitacionSub;
import com.servinte.axioma.orm.delegate.manejoPaciente.AutorizacionesCapitacionSubDelegate;

/**
 * Esta clase se encarga de ejecutar los m�todos de negocio
 * de la entidad  AutorizacionesCapitacionSub
 * 
 * @author Angela Maria Aguirre
 * @since 10/12/2010
 */
public class AutorizacionesCapitacionSubHibernateDAO implements
		IAutorizacionesCapitacionSubDAO {
	
	AutorizacionesCapitacionSubDelegate delegate;
	
	/**
	 * 
	 * M�todo constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public AutorizacionesCapitacionSubHibernateDAO(){
		delegate = new AutorizacionesCapitacionSubDelegate();
	}
	
	
	/**
	 * 
	 * Este M�todo se encarga de insertar en la base de datos
	 * un registro de autorizaci�n de capitaci�n subcontratada
	 * 
	 * @param AutorizacionesCapitacionSub autorizacion
	 * @return boolean
	 * @author, Angela Maria Aguirresw
	 *
	 */
	public boolean guardarAutorizacionCapitacioncontratada(AutorizacionesCapitacionSub autorizacion){
		return delegate.guardarAutorizacionCapitacioncontratada(autorizacion);
	}
	
	
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
	public AutorizacionesCapitacionSub findById(long id){
		return delegate.findById(id);
	}


	
	/**
	 * 
	 * Implementaci�n del m�todo attachDirty de la super clase  AutorizacionesCapitacionSubHome
	 * 
	 * @param AutorizacionesCapitacionSub autorizacion
	 * @return boolean
	 * @author, Angela Maria Aguirre, Cristhian Murillo
	 *
	 */
	@Override
	public boolean sincronizarAutorizacionCapitacioncontratada(AutorizacionesCapitacionSub autorizacion) {
		return delegate.sincronizarAutorizacionCapitacioncontratada(autorizacion);
	}
	
	/** Este M�todo se encarga de consultar las autorizaciones en estado autorizado o
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
	 * Este M�todo se encarga de consultar las autorizaciones en estado autorizado o
	 * con indicativo temporal en un rango determinado
	 * 
	 * @param DTOAdministracionAutorizacion dto
	 * @return ArrayList<DTOAdministracionAutorizacion>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTOAdministracionAutorizacion> obtenerAutorizacionesPorRango(DTOBusquedaAutorizacionCapitacionRango dto){
		return delegate.obtenerAutorizacionesPorRango(dto);
	}
	
	
	/**
	 * 
	 * Este M�todo se encarga de actualizar el detalle de una autorizaci�n de capitaci�n
	 * 
	 * @param AutorizacionesCapitacionSub
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarAutorizacionCapitacion(AutorizacionesCapitacionSub autorizacion){
		return delegate.actualizarAutorizacionCapitacion(autorizacion);
	}
	
	/**
	 * Este M�todo se encarga de consultar las autorizaciones de capitacion generadas para cada 
	 * convenio-contrato por dia en un rango de fechas determinado
	 * Para Proceso Autorizaciones 1027 Cierre
	 * 
	 * @param DtoProcesoPresupuestoCapitado
	 * @return ArrayList<DtoConsultaProcesoAutorizacion>
	 * @author, Camilo G�mez
	 */
	public ArrayList<DtoConsultaProcesoAutorizacion> obtenerAutorizacionesCapitaServiArtiInconsistentes(DtoProcesoPresupuestoCapitado dto){
		return delegate.obtenerAutorizacionesCapitaServiArtiInconsistentes(dto);
	}

	/**
	 * Este M�todo se encarga de consultar las autorizaciones de capitacion generadas para cada 
	 * convenio-contrato por dia en un rango de fechas determinado para -SERVICIOS-
	 * Para Proceso Autorizaciones 1027 Cierre
	 * 
	 * @param DTOProcesoPresupuestoCapitado dto
	 * @return ArrayList<DtoConsultaProcesoAutorizacion>
	 * @author, sandra barreto
	 * //MT6715
	 * */
	public ArrayList<DtoConsultaProcesoAutorizacion> obtenerAutorizacionesCapitacionServiciosEstado(DtoProcesoPresupuestoCapitado dto){
		return delegate.obtenerAutorizacionesCapitacionServiciosEstado(dto);
	}

	/**
	 * Este M�todo se encarga de consultar las autorizaciones de capitacion generadas para cada 
	 * convenio-contrato por dia en un rango de fechas determinado para -SERVICIOS-
	 * Para Proceso Autorizaciones 1027 Cierre
	 * 
	 * @param DTOProcesoPresupuestoCapitado dto
	 * @return ArrayList<DtoConsultaProcesoAutorizacion>
	 * @author, Camilo G�mez 
	 */
	public ArrayList<DtoConsultaProcesoAutorizacion> obtenerAutorizacionesCapitacionServicios(DtoProcesoPresupuestoCapitado dto){
		return delegate.obtenerAutorizacionesCapitacionServicios(dto);
	}



	/**
	 * Este M�todo se encarga de consultar las autorizaciones de capitacion generadas para cada 
	 * convenio-contrato por dia en un rango de fechas determinado para -ARTICULOS-
	 * Para Proceso Autorizaciones 1027 Cierre
	 * 
	 * @param DTOProcesoPresupuestoCapitado dto
	 * @return ArrayList<DtoConsultaProcesoAutorizacion>
	 * @author, Camilo G�mez 
	 */
	public ArrayList<DtoConsultaProcesoAutorizacion> obtenerAutorizacionesCapitacionArticulos(DtoProcesoPresupuestoCapitado dto){
		return delegate.obtenerAutorizacionesCapitacionArticulos(dto);
	}
	
	/**
	 * Este M�todo se encarga de consultar las autorizaciones de capitacion -ANULADAS- generadas para cada 
	 * convenio-contrato por dia en un rango de fechas determinado para el -SERVICIO- especifico
	 * Para Proceso Autorizaciones 1027 Cierre
	 * 
	 * @param DTOProcesoPresupuestoCapitado dto
	 * @return ArrayList<DtoConsultaProcesoAutorizacion>
	 * @author, Camilo G�mez 
	 */
	public ArrayList<DtoConsultaProcesoAutorizacion> obtenerAutorizacionesCapitacionServicioAnulada(DtoProcesoPresupuestoCapitado dto){
		return delegate.obtenerAutorizacionesCapitacionServicioAnulada(dto);
	}

	/**
	 * Este M�todo se encarga de consultar las autorizaciones de capitacion -ANULADAS- generadas para cada 
	 * convenio-contrato por dia en un rango de fechas determinado para cada -ARTICULO- especifico
	 * Para Proceso Autorizaciones 1027 Cierre
	 * 
	 * @param DTOProcesoPresupuestoCapitado dto
	 * @return ArrayList<DtoConsultaProcesoAutorizacion>
	 * @author, Camilo G�mez 
	 */
	public ArrayList<DtoConsultaProcesoAutorizacion> obtenerAutorizacionesCapitacionArticuloAnulada(DtoProcesoPresupuestoCapitado dto){
		return delegate.obtenerAutorizacionesCapitacionArticuloAnulada(dto);
	}
}
