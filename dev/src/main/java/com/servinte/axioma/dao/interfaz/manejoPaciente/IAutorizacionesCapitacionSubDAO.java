package com.servinte.axioma.dao.interfaz.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DtoConsultaProcesoAutorizacion;
import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.princetonsa.dto.capitacion.DtoResultadoConsultaProcesosCierre;
import com.princetonsa.dto.manejoPaciente.DTOAdministracionAutorizacion;
import com.princetonsa.dto.manejoPaciente.DTOBusquedaAutorizacionCapitacionRango;
import com.servinte.axioma.orm.AutorizacionesCapitacionSub;

/**
 * Esta clase se encarga de definir los m�todos de negocio
 * de la entidad  AutorizacionesCapitacionSub
 * 
 * @author Angela Maria Aguirre
 * @since 10/12/2010
 */
public interface IAutorizacionesCapitacionSubDAO {
	
	/**
	 * 
	 * Este M�todo se encarga de insertar en la base de datos
	 * un registro de autorizaci�n de capitaci�n subcontratada
	 * 
	 * @param AutorizacionesCapitacionSub autorizacion
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarAutorizacionCapitacioncontratada(AutorizacionesCapitacionSub autorizacion);
	
	
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
	
	

	/**
	 * 
	 * Implementaci�n del m�todo attachDirty de la super clase  AutorizacionesCapitacionSubHome
	 * 
	 * @param AutorizacionesCapitacionSub autorizacion
	 * @return boolean
	 * @author, Angela Maria Aguirre, Cristhian Murillo
	 *
	 */
	public boolean sincronizarAutorizacionCapitacioncontratada(AutorizacionesCapitacionSub autorizacion);
	
	
	 
	/** Este M�todo se encarga de consultar las autorizaciones en estado autorizado o
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
	 * Este M�todo se encarga de consultar las autorizaciones de capitacion generadas para cada 
	 * convenio-contrato por dia en un rango de fechas determinado
	 * Para Proceso Autorizaciones 1027 Cierre
	 * 
	 * @param DtoProcesoPresupuestoCapitado
	 * @return ArrayList<DtoResultadoConsultaProcesosCierre>
	 * @author, Camilo G�mez
	 */
	public ArrayList<DtoConsultaProcesoAutorizacion> obtenerAutorizacionesCapitaServiArtiInconsistentes(DtoProcesoPresupuestoCapitado dto);
	
	/**
	 * Este M�todo se encarga de consultar las autorizaciones de capitacion generadas para cada 
	 * convenio-contrato por dia en un rango de fechas determinado para -SERVICIOS-
	 * Para Proceso Autorizaciones 1027 Cierre
	 * 
	 * @param DTOProcesoPresupuestoCapitado dto
	 * @return ArrayList<DtoConsultaProcesoAutorizacion>
	 * @author, Camilo G�mez 
	 */
	public ArrayList<DtoConsultaProcesoAutorizacion> obtenerAutorizacionesCapitacionServicios(DtoProcesoPresupuestoCapitado dto);

	/**
	 * Este M�todo se encarga de consultar las autorizaciones de capitacion generadas para cada 
	 * convenio-contrato por dia en un rango de fechas determinado para -SERVICIOS-
	 * Para Proceso Autorizaciones 1027 Cierre
	 * suma de la autorizaciones
	 * @param DTOProcesoPresupuestoCapitado dto
	 * @return ArrayList<DtoConsultaProcesoAutorizacion>
	 * //MT6715
	 */
	public ArrayList<DtoConsultaProcesoAutorizacion> obtenerAutorizacionesCapitacionServiciosEstado(DtoProcesoPresupuestoCapitado dto);

	
	/**
	 * Este M�todo se encarga de consultar las autorizaciones de capitacion generadas para cada 
	 * convenio-contrato por dia en un rango de fechas determinado para -ARTICULOS-
	 * Para Proceso Autorizaciones 1027 Cierre
	 * 
	 * @param DTOProcesoPresupuestoCapitado dto
	 * @return ArrayList<DtoConsultaProcesoAutorizacion>
	 * @author, Camilo G�mez 
	 */
	public ArrayList<DtoConsultaProcesoAutorizacion> obtenerAutorizacionesCapitacionArticulos(DtoProcesoPresupuestoCapitado dto);
	
	/**
	 * Este M�todo se encarga de consultar las autorizaciones de capitacion -ANULADAS- generadas para cada 
	 * convenio-contrato por dia en un rango de fechas determinado para el -SERVICIO- especifico
	 * Para Proceso Autorizaciones 1027 Cierre
	 * 
	 * @param DTOProcesoPresupuestoCapitado dto
	 * @return ArrayList<DtoConsultaProcesoAutorizacion>
	 * @author, Camilo G�mez 
	 */
	public ArrayList<DtoConsultaProcesoAutorizacion> obtenerAutorizacionesCapitacionServicioAnulada(DtoProcesoPresupuestoCapitado dto);

	/**
	 * Este M�todo se encarga de consultar las autorizaciones de capitacion -ANULADAS- generadas para cada 
	 * convenio-contrato por dia en un rango de fechas determinado para cada -ARTICULO- especifico
	 * Para Proceso Autorizaciones 1027 Cierre
	 * 
	 * @param DTOProcesoPresupuestoCapitado dto
	 * @return ArrayList<DtoConsultaProcesoAutorizacion>
	 * @author, Camilo G�mez 
	 */
	public ArrayList<DtoConsultaProcesoAutorizacion> obtenerAutorizacionesCapitacionArticuloAnulada(DtoProcesoPresupuestoCapitado dto);

}
