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
 * Esta clase se encarga de ejecutar los métodos de negocio
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
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public AutorizacionesCapitacionSubHibernateDAO(){
		delegate = new AutorizacionesCapitacionSubDelegate();
	}
	
	
	/**
	 * 
	 * Este Método se encarga de insertar en la base de datos
	 * un registro de autorización de capitación subcontratada
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
	 * Este Método se encarga de consultar una  autorización de capitación 
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
	 * Implementación del método attachDirty de la super clase  AutorizacionesCapitacionSubHome
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
	
	/** Este Método se encarga de consultar las autorizaciones en estado autorizado o
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
	 * Este Método se encarga de actualizar el detalle de una autorización de capitación
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
	 * Este Método se encarga de consultar las autorizaciones de capitacion generadas para cada 
	 * convenio-contrato por dia en un rango de fechas determinado
	 * Para Proceso Autorizaciones 1027 Cierre
	 * 
	 * @param DtoProcesoPresupuestoCapitado
	 * @return ArrayList<DtoConsultaProcesoAutorizacion>
	 * @author, Camilo Gómez
	 */
	public ArrayList<DtoConsultaProcesoAutorizacion> obtenerAutorizacionesCapitaServiArtiInconsistentes(DtoProcesoPresupuestoCapitado dto){
		return delegate.obtenerAutorizacionesCapitaServiArtiInconsistentes(dto);
	}

	/**
	 * Este Método se encarga de consultar las autorizaciones de capitacion generadas para cada 
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
	 * Este Método se encarga de consultar las autorizaciones de capitacion generadas para cada 
	 * convenio-contrato por dia en un rango de fechas determinado para -SERVICIOS-
	 * Para Proceso Autorizaciones 1027 Cierre
	 * 
	 * @param DTOProcesoPresupuestoCapitado dto
	 * @return ArrayList<DtoConsultaProcesoAutorizacion>
	 * @author, Camilo Gómez 
	 */
	public ArrayList<DtoConsultaProcesoAutorizacion> obtenerAutorizacionesCapitacionServicios(DtoProcesoPresupuestoCapitado dto){
		return delegate.obtenerAutorizacionesCapitacionServicios(dto);
	}



	/**
	 * Este Método se encarga de consultar las autorizaciones de capitacion generadas para cada 
	 * convenio-contrato por dia en un rango de fechas determinado para -ARTICULOS-
	 * Para Proceso Autorizaciones 1027 Cierre
	 * 
	 * @param DTOProcesoPresupuestoCapitado dto
	 * @return ArrayList<DtoConsultaProcesoAutorizacion>
	 * @author, Camilo Gómez 
	 */
	public ArrayList<DtoConsultaProcesoAutorizacion> obtenerAutorizacionesCapitacionArticulos(DtoProcesoPresupuestoCapitado dto){
		return delegate.obtenerAutorizacionesCapitacionArticulos(dto);
	}
	
	/**
	 * Este Método se encarga de consultar las autorizaciones de capitacion -ANULADAS- generadas para cada 
	 * convenio-contrato por dia en un rango de fechas determinado para el -SERVICIO- especifico
	 * Para Proceso Autorizaciones 1027 Cierre
	 * 
	 * @param DTOProcesoPresupuestoCapitado dto
	 * @return ArrayList<DtoConsultaProcesoAutorizacion>
	 * @author, Camilo Gómez 
	 */
	public ArrayList<DtoConsultaProcesoAutorizacion> obtenerAutorizacionesCapitacionServicioAnulada(DtoProcesoPresupuestoCapitado dto){
		return delegate.obtenerAutorizacionesCapitacionServicioAnulada(dto);
	}

	/**
	 * Este Método se encarga de consultar las autorizaciones de capitacion -ANULADAS- generadas para cada 
	 * convenio-contrato por dia en un rango de fechas determinado para cada -ARTICULO- especifico
	 * Para Proceso Autorizaciones 1027 Cierre
	 * 
	 * @param DTOProcesoPresupuestoCapitado dto
	 * @return ArrayList<DtoConsultaProcesoAutorizacion>
	 * @author, Camilo Gómez 
	 */
	public ArrayList<DtoConsultaProcesoAutorizacion> obtenerAutorizacionesCapitacionArticuloAnulada(DtoProcesoPresupuestoCapitado dto){
		return delegate.obtenerAutorizacionesCapitacionArticuloAnulada(dto);
	}
}
